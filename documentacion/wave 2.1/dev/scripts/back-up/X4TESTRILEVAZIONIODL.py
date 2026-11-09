from java.lang import System 
from java.lang import Exception 
from java.util import HashMap
from java.util import HashSet
from java.util import ArrayList
from psdi.util.logging import MXLoggerFactory
from psdi.mbo import SqlFormat
from psdi.server import MXServer
from psdi.mbo import SqlFormat
from psdi.mbo import MboConstants
from com.terna.common.string import StringUtils
from com.terna.common.maximo import MXUtils
from com.terna.common.date import DateUtils
from com.terna.integration.common.asset import AssetKey
from com.terna.common.timer import Timer 

# logs an INFO verbose message 
def verbose(message, *args):
	# logs 
	if logVerbose:
		log.info(StringUtils.format("[X4RILEVAZIONIODL] " + message, args))

# logs an INFO message 
def info(message, *args):
	# logs 
	log.info(StringUtils.format("[X4RILEVAZIONIODL] " + message, args))

# 
# logs an ERROR message 
#
def error(self, message, *args):
	# logs 
	log.error(StringUtils.format("[X4RILEVAZIONIODL] " + message, args))
#
# @return [String]: the reference for the asset.
#
def evalRef(assetMbo):
	# check the cache
	assetNum = assetMbo.getString("assetnum")
	if refMap.containsKey(assetNum):
		return refMap.get(assetNum)
		
	# get classification
	classification = assetMbo.getString("classstructure.classificationid")
	# initialized black reference
	ref = ""
	if not StringUtils.isEmpty(classification):
		if StringUtils.in(classification, "CONDUTTORE", "ARMAMENTO CONDUTTORE"):
			# handle CONDUTTORE, ARMAMENTO CONDUTTORE
			specSet = assetMbo.getMboSet("$evalRef1$", "ASSETSPEC", "siteid = :siteid and assetnum = :assetnum and assetattrid = 'LIN_RIF_POSIZIONE'")
			if not specSet.isEmpty():
				ref = specSet.getMbo(0).getString("numvalue")
				
		elif StringUtils.in(classification, "FUNE DI GUARDIA", "ARMAMENTO FUNE DI GUARDIA"):
			# handle FUNE DI GUARDIA, ARMAMENTO FUNE DI GUARDIA
			specSet = assetMbo.getMboSet("$evalRef2$", "ASSETSPEC", "siteid = :siteid and assetnum=:assetnum and assetattrid = 'LIN_RIF_FUNE'")
			specSet.reset()
			if not specSet.isEmpty():
				ref = specSet.getMbo(0).getString("alnvalue")
	
		else:
			# handle classification related to 
			#	CONDUTTORE (having specification LIN_RIF_ASSET_COND)
			#	ARMAMENTO CONDUTTORE (having specification LIN_RIF_ASSET_ARM_COND)
			#	FUNE DI GUARDIA (having specification LIN_RIF_ASSET_FDG)
			#	ARMAMENTO FUNE DI GUARDIA (having specification LIN_RIF_ASSET_ARM_FDG)
			#
			# assuming we'll find just one of the four specifications
			check1Set = assetMbo.getMboSet("$evalRef3$", "ASSETSPEC", "siteid = :siteid and assetnum = :assetnum and assetattrid in ('LIN_RIF_ASSET_ARM_COND', 'LIN_RIF_ASSET_ARM_FDG', 'LIN_RIF_ASSET_COND', 'LIN_RIF_ASSET_FDG')")
			if not check1Set.isEmpty():
				# jump to the main asset (CONDUTTORE, ARMAMENTO CONDUTTORE, FUNE DI GUARDIA, ARMAMENTO FUNE DI GUARDIA) thru the table value (the asset number) 
				specSet = check1Set.getMbo(0).getMboSet("$evalRef4$", "ASSETSPEC", "siteid = :siteid and assetnum = :tablevalue and assetattrid in ('LIN_RIF_FUNE', 'LIN_RIF_POSIZIONE')")
				if not specSet.isEmpty():
					if specSet.getMbo(0).getString("assetattrid") == "LIN_RIF_FUNE":
						ref = specSet.getMbo(0).getString("alnvalue")
					else:
						ref = specSet.getMbo(0).getString("numvalue")
			else:
				# handle equipments
				check2Set = assetMbo.getMboSet("$evalRef4$", "CLASSANCESTOR", "classstructureid = :classstructureid and ancestorclassid = 'DISPOSITIVI E COMPONENTI'")
				if not check2Set.isEmpty():
					ref = assetMbo.getString("x3riferimentofase")
	
	# put into the cache
	refMap.put(assetNum, ref)
	return ref
	
#
# Add set an MBO to the X4RILEVAZIONIODL set with the given parameters.
#
#	catalogMatchSet [MboSetRemote]: the X4RILEVAZIONIODL set to add in
#	wo [MboRemote]: the WORKORDER MBO
#	asset [MboRemote]: the ASSET MBO
#	catalog [MboRemote]: the catalog (X4CATMISURE, X4CATDIFFOR, X4CATINTERF) MBO
#	asset [MboRemote]: the ASSET MBO
#	idx [int]: the iteration index
#	@return the added MBO (can be None)
#
def addCatalogMatch(catalogMatchSet, wo, asset, catalog, idx):
	# get the object name for the catalog
	catalogType = catalog.getName()
	# add the record
	catalogMatch = catalogMatchSet.add()
	# set common attributes
	catalogMatch.setValue("assetnum", asset.getString("assetnum"))
	catalogMatch.setValue("assetdesc", asset.getString("description"))
	catalogMatch.setValue("workorderid", wo.getLong("workorderid"))
	catalogMatch.setValue("riferimento", evalRef(asset))
	if idx == 0:
		# odd setting!!! preserve the previous behaviour
		catalogMatch.setValue("classificationid", "1")
	
	# set catalog specific attributes
	if catalogType == "X4CATMISURE":
		catalogMatch.setValue("id", catalog.getString("idmisura"))
		catalogMatch.setValue("description", catalog.getString("description"))
		catalogMatch.setValue("tipo", "MIS")
		catalogMatch.setValue("wonum", catalog.getString("um"))
	elif catalogType == "X4CATDIFFOR":
		catalogMatch.setValue("id", catalog.getString("iddifformita"))
		catalogMatch.setValue("description", catalog.getString("description"))
		catalogMatch.setValue("tipo", "DIFF")
	elif catalogType == "X4CATINTERF":
		catalogMatch.setValue("id", catalog.getString("idinterferenza"))
		catalogMatch.setValue("description", catalog.getString("description"))
		catalogMatch.setValue("tipo", "INT")
		
	return catalogMatch	

#
# Evaluate the catalog match for the asset related to the work order and catalog entries identified 
# by the catalog name and the where clause.
#
#	catalogMatchSet [MboSetRemote]: the X4RILEVAZIONIODL set to add in
#	wo [MboRemote]: the WORKORDER MBO
#	asset [MboRemote]: the ASSET MBO
#	catalogName [String]: the catalog name(X4CATMISURE, X4CATDIFFOR, X4CATINTERF) MBO
#	where [String]: the where clause for the catalog
#	@return the number of added catalog matches
#
def evalAsset(catalogMatchSet, wo, asset, catalogName, where):
	# check catalog
	if not StringUtils.in(catalogName, "X4CATMISURE", "X4CATDIFFOR", "X4CATINTERF"):
		# catalog not supported
		return 0

	# get catalog entries for the asset
	catalogSet = MXUtils.getMboSet(catalogName)
	catalogSet.setWhere(where)
	catalogSet.setFlag(MboConstants.DISCARDABLE, True)
	# go thru catalog entries
	total = 0
	x = 0
	catalogMbo = catalogSet.getMbo(0)
	while catalogMbo != None:
		addCatalogMatchCheck = True
		if StringUtils.in(catalogName, "X4CATDIFFOR", "X4CATINTERF"):
			# check the catalog
			addCatalogMatchCheck = catalogService.checkCatalog(catalogMbo, asset)
			catalogId = ""
			if catalogName == "X4CATDIFFOR":
				catalogId = catalogMbo.getString("iddifformita")
			else:
				catalogId = catalogMbo.getString("idinterferenza")
			if addCatalogMatchCheck:
				verbose("    catalog {0} matched", catalogId)
			else:
				verbose("    catalog {0} not matched", catalogId)
		# add the catalog match
		if addCatalogMatchCheck:	
			added = addCatalogMatch(catalogMatchSet, wo, asset, catalogMbo, x)
			if added != None:
				total += 1
				
		# move to the next catalog
		x += 1
		catalogMbo = catalogSet.getMbo(x)
	verbose("    catalog {0}: added {1} catalog match(s)", catalogName, total)
	# get rid off of DB resources
	catalogSet.close()
	return total
			
#
# Returns an MBO set containing all the asset related to the workorder's asset based on the 
# related asset types defined in the workorder's job plan. The workorder's asset will be included.
#
#	wo[MboRemote]: the work order
#	@return[MboSetRemote]: the asset set
#
def getWoAssets(wo):
	st = System.currentTimeMillis()
	# set which collects all the asset number
	assets = HashSet()
	# get the work order asset key
	targetAssetKey = AssetKey(wo.getMboSet("ASSET").getMbo(0))
	# put the root asset
	assets.add(wo.getString("assetnum"))
	
	# get the job plan
	jobPlan = wo.getMboSet("WO_REV_JOBPLAN").getMbo(0)
	if jobPlan != None:
		info("getting related asset types for job plan {0} revision {1}", jobPlan.getString("jpnum"), jobPlan.getInt("pluscrevnum"))
		# go thru related asset types
		relatedAssetTypes = jobPlan.getMboSet("X4CATASSOCELEMENTO")
		i = 0
		relatedAssetTypeMbo = relatedAssetTypes.getMbo(0)
		while relatedAssetTypeMbo != None:
			# invoke the asset look-up
			relatedAssetType = relatedAssetTypeMbo.getString("tipologiacorrelata")
			verbose("    invoking asset look-up for asset type {0}", relatedAssetType)
			ruleId = relatedAssetTypeMbo.getLong("rulesid")
			classStructureId = relatedAssetTypeMbo.getString("classstructureid")
			relatedAssets = catalogService.invokeAssetLookUp(targetAssetKey, ruleId, classStructureId)
			verbose("    found {0} assets for asset type {1}", relatedAssets.size(), relatedAssetType)
			# getting asset numbers
			for j in range(0, relatedAssets.size()):
				assetNum = relatedAssets.get(j).getNum()
				assets.add(assetNum)
				assetTypeByAsset.put(assetNum, relatedAssetType)
			
			i += 1
			relatedAssetTypeMbo = relatedAssetTypes.getMbo(i)

	verbose("found {0} assets", assets.size())	
	# get the asset set
	query = "siteid = :siteid and assetnum in ('" + StringUtils.join("','", assets) + "')"
	sqlFormat = SqlFormat(wo, query)
	where = sqlFormat.format()
	verbose("getting assets with query: {0}", where)
	assetSet = MXUtils.getMboSet("ASSET")
	assetSet.setFlag(MboConstants.DISCARDABLE, True)
	assetSet.setWhere(where)

	info("got {0} assets in {1}", assets.size(), DateUtils.format(System.currentTimeMillis()-st))
	return assetSet

#
# Returns the asset number for the related asset (None if not found)
#
#	asset[MboRemote]: the asset
#	@return[String]: the asset number for the related asset (None if not found)
#
def getRelatedSecondLevelAssetNum(asset):
	# get the asset classification
	classification = asset.getString("asset.classstructure.classificationid")
	
	# get the query for the related asset based on the classification 
	if classification == "CAMPATA AEREA":
		query = "assetlocrelationid in (select b.assetlocrelationid from assetlocrelation a, assetlocrelation b where a.siteid = :siteid and a.targetassetnum = :assetnum and b.x3sapid = a.x3sapid || '-S')"
	elif classification == "SOSTEGNO AEREO":		
		query = "assetlocrelationid in (select b.assetlocrelationid from assetlocrelation a, assetlocrelation b where a.siteid = :siteid and a.targetassetnum = :assetnum and b.x3sapid = substr(a.x3sapid, 1, 16)"
	elif classification == "TRATTA CAVO":
		query = "assetlocrelationid in (select b.assetlocrelationid from assetlocrelation a, assetlocrelation b where a.siteid = :siteid and a.targetassetnum = :assetnum and b.x3sapid = a.x3sapid || '-H')"
	elif classification == "ESTREMO CAVO":		
		query = "assetlocrelationid in (select b.assetlocrelationid from assetlocrelation a, assetlocrelation b where a.siteid = :siteid and a.targetassetnum = :assetnum and b.x3sapid = substr(a.x3sapid, 1, 16)"

	# get relation
	relations = MXUtils.getMboSet("ASSETLOCRELATION")
	sqlFormat = SqlFormat(asset, query)
	relations.setWhere(sqlFormat.format())
	relation = relations.getMbo(0)
	relations.close()

	if relation == None:
		# no related asset found
		return None
	else:
		# no related asset found
		return relation.getString("targetassetnum")

#
# Returns an MBO set containing all the assets related to the work-order's asset for which the micro-controls 
# must be checked
#
#	wo[MboRemote]: the work-order
#	assetType[String]: the asset type
#	@return[MboSetRemote]: the micro-controls asset set
#
def getAssetSetForMC(wo, assetType):
	# get the asset
	asset = wo.getMboSet("asset").getMbo(0)
	# get the asset classification
	classification = asset.getString("classstructure.classificationid")
	
	where = ""
	if StringUtils.in(classification, "CAMPATA AEREA", "SOSTEGNO AEREO", "TRATTA CAVO", "ESTREMO CAVO"):
		# get the related asset
		relatedAssetNum = getRelatedSecondLevelAssetNum(asset)
		if relatedAssetNum == None:
			# no related asset
			query = "(siteid = :siteid and (ancestor = :assetnum or assetnum = :assetnum))"		
			sqlFormat = SqlFormat(asset, query)
			where = sqlFormat.format()
		else:
			# we have a related asset
			query = """
				(siteid, assetnum) in 
				(
					(select siteid, assetnum from asset a where a.siteid = :siteid and (a.ancestor = :assetnum or a.assetnum = :assetnum))
					union
					(select siteid, assetnum from asset b where b.siteid = :siteid and (b.ancestor = :0 or b.assetnum = :0))
				)
			"""		
			sqlFormat = SqlFormat(asset, query)
			sqlFormat.setObject(0, "UPPER", relatedAssetNum)
			where = sqlFormat.format()
	else:
		query = "siteid = :siteid and (assetnum = :assetnum or parent = :parent)"
		sqlFormat = SqlFormat(wo, query)
		where = sqlFormat.format()

	where += " and status not in ('DELE', 'DEMO', 'DIST', 'CEAL')"
	info("    getting assets with query clause {0}", where)
	mcAssetSet = MXUtils.getMboSet("ASSET")
	mcAssetSet.setWhere(where)
	return mcAssetSet

#
# Returns the asset types for the work-order micro-controls 
#
#	wo[MboRemote]: the work-order
#	@return[Set]: the set of asset types
#
def getAssetTypesForMc(wo):
	assetTypes = HashSet()
	mcMboSet = MXUtils.getMboSet("X3CATASSOCIAZIONE")
	sqlFormat = SqlFormat(wo, "(iddifformita is not null or idinterferenza is not null) and idorigine in (select x4microcontrollo from jobtask where jpnum=:jpnum)")
	mcMboSet.setWhere(sqlFormat.format())
	mcMbo = mcMboSet.moveFirst()
	while mcMbo != None:
		assetTypes.add(mcMbo.getString("tipologiaasset"))
		mcMbo = mcMboSet.moveNext()
	mcMboSet.close()
	return assetTypes

def evalWo(wo):
	# common MBO sets
	catalogMatchSet = None
	assetSet = None
	
	try:
		info("producing X4RILEVAZIONIODL for work order {0} on asset {1} {2}", wo.getString("wonum"), wo.getString("assetnum"), wo.getString("asset.classstructure.classificationid"))
		# get the X4RILEVAZIONIODL records for the work order 
		catalogMatchSet = MXUtils.getMboSet("X4RILEVAZIONIODL")
		sqlFormat = SqlFormat("workorderid = :0")
		sqlFormat.setLong(0, wo.getLong("workorderid"))
		catalogMatchSet.setWhere(sqlFormat.format())

		if not catalogMatchSet.isEmpty():
			info("work order already has records for X4RILEVAZIONIODL, give up")
		else:
			st = System.currentTimeMillis()
			
			# prepare counters
			totalAdded1 = 0
			totalAdded2 = 0
			totalAdded3 = 0
			totalAssets = 0

			# get related assets
			info("evaluating related assets")
			assetSet = getWoAssets(wo)

			# go thru the assets
			info("evaluating catalogs")
			i = 0
			asset = assetSet.getMbo(0)
			while asset != None:
				# add catalog matches for the asset
				assetNum = asset.getString("assetnum")
				assetClassification = asset.getString("classstructure.classificationid")
				verbose("[{0}] working on asset {1} type {2} classification {3}", i, assetNum, assetTypeByAsset.get(assetNum), assetClassification)
				
				# add X4RILEVAZIONIODL for catalog X4CATMISURE entries for the asset
				sqlFormat = SqlFormat(asset, "x4catmisureid in (select x4catmisureid from x4catmisureclassifications where classificationid = :classstructure.classificationid) and tipoparametro = 'Misura'")
				added1 = evalAsset(catalogMatchSet, wo, asset, "X4CATMISURE", sqlFormat.format())
				totalAdded1 += added1
				
				# add X4RILEVAZIONIODL for catalog X4CATDIFFOR entries for the asset
				sqlFormat = SqlFormat(wo, 
					"""
					status = 'ATTIVO' and 
					x4catdifforid in (select x4catdifforid from x4difforlassifications where classificationid = :0) and
					iddifformita in (select iddifformita from x3catassociazione where iddifformita is not null and idorigine = :jpnum)
					""")
				sqlFormat.setObject(0, "UPPER", assetClassification)
				added2 = evalAsset(catalogMatchSet, wo, asset, "X4CATDIFFOR", sqlFormat.format())
				totalAdded2 += added2
				
				# add X4RILEVAZIONIODL for catalog X4CATINTERF entries for the asset
				sqlFormat = SqlFormat(wo, 
					"""
					status = 'ATTIVO' and 
					x4catinterfid in (select x4catinterfid from x4interfclassifications where classificationid = :0) and
					idinterferenza in (select idinterferenza from x3catassociazione where idinterferenza is not null and idorigine = :jpnum)
					""")
				sqlFormat.setObject(0, "UPPER", assetClassification)
				added3 = evalAsset(catalogMatchSet, wo, asset, "X4CATINTERF", sqlFormat.format())
				totalAdded3 += added3
				
				verbose("    added misure={0}, difformita={1}, interferenze={2}", added1, added2, added3)
				
				# move to next asset
				i += 1
				asset = assetSet.getMbo(i)
				totalAssets += 1

			# get rid off of DB resources
			assetSet.close()


			# get micro-controls asset types
			info("evaluating micro-controls assets")
			assetTypes = getAssetTypesForMc(wo)
			# go thru asset types
			it = assetTypes.iterator()
			while it.hasNext():
				assetType = it.next()
				# get the assets for the current asset type
				info("evaluating asset type {0}", assetType)
				assetSet = getAssetSetForMC(wo, assetType)
				# go thru assets
				i = 0
				asset = assetSet.getMbo(i)
				while asset != None:
					assetNum = asset.getString("assetnum")
					assetClassification = asset.getString("classstructure.classificationid")
					verbose("[{0}] working on asset {1} type {2} classification {3}", i, assetNum, assetTypeByAsset.get(assetNum), assetClassification)
	
					# add X4RILEVAZIONIODL for catalog X4CATMISURE entries for the asset
					sqlFormat = SqlFormat(asset, "x4catmisureid in (select x4catmisureid from x4catmisureclassifications where classificationid = :classstructure.classificationid) and tipoparametro = 'Misura'")
					added1 = evalAsset(catalogMatchSet, wo, asset, "X4CATMISURE", sqlFormat.format())
					totalAdded1 += added1
				
					# add X4RILEVAZIONIODL for catalog X4CATDIFFOR entries for the asset
					sqlFormat = SqlFormat(wo,
						"""
						status = 'ATTIVO' and 
						x4catdifforid in (select x4catdifforid from x4difforlassifications where classificationid = :0) and
						iddifformita in (select iddifformita from x3catassociazione where iddifformita is not null and idorigine in (select x4microcontrollo from jobtask where jpnum = :jpnum))
						""")
					sqlFormat.setObject(0, "UPPER", assetClassification)
					added2 = evalAsset(catalogMatchSet, wo, asset, "X4CATDIFFOR", sqlFormat.format())
					totalAdded2 += added2

					# add X4RILEVAZIONIODL for catalog X4CATINTERF entries for the asset
					sqlFormat = SqlFormat("status = 'ATTIVO' and idinterferenza in (select idinterferenza from x3catassociazione where idinterferenza is not null and idorigine in (select x4microcontrollo from jobtask where jpnum=:jpnum))")
					sqlFormat = SqlFormat(wo,
						"""
						status = 'ATTIVO' and 
						x4catinterfid in (select x4catinterfid from x4interfclassifications where classificationid = :0) and
						idinterferenza in (select idinterferenza from x3catassociazione where idinterferenza is not null and idorigine in (select x4microcontrollo from jobtask where jpnum = :jpnum))
						""")
					sqlFormat.setObject(0, "UPPER", assetClassification)
					added3 = evalAsset(catalogMatchSet, wo, asset, "X4CATINTERF", sqlFormat.format())
					totalAdded3 += added3
				
					verbose("    added misure={0}, difformita={1}, interferenze={2}", added1, added2, added3)
					
					# move to next asset
					i += 1
					asset = assetSet.getMbo(i)
					totalAssets += 1

				# get rid off of DB resources
				assetSet.close()
				
			info("evaluated {0} assets in {1} ms", totalAssets, DateUtils.format(System.currentTimeMillis()-st))
			
			# saving
			total = totalAdded1 + totalAdded2 + totalAdded3
			info("found {0} matches (misure={1}, difformita={2}, interferenze={3})", total, totalAdded1, totalAdded2, totalAdded3)
			if total > 0:
				st = System.currentTimeMillis()
				verbose("saving {0} X4RILEVAZIONIODL record(s)", total)
				catalogMatchSet.save()
				info("saved {0} X4RILEVAZIONIODL record(s) in {1}", total, DateUtils.format(System.currentTimeMillis()-st))

		# get rid off of DB resources
		catalogMatchSet.close()
		
	except Exception as t:
		error("failure {0}", t.getMessage())
		
	finally:
		# get rid off of DB resources
		if catalogMatchSet != None:
			catalogMatchSet.close()
		if assetSet != None:
			assetSet.close()

# set log verbosity
logVerbose = False
# get logger
log = MXLoggerFactory.getLogger("maximo.application")
# map of misure references by asset (HashMap<String, String>)
refMap = HashMap()
# map of asset type for each asset  (HashMap<String, String>)
assetTypeByAsset = HashMap()
# set-up the cache service
cacheService = service.invokeScript("X4CACHESERVICE").get("CacheService")(service)
cacheService.addCache("CATALOG")
cacheService.addCache("ASSET")
# get the catalog service
catalogService = service.invokeScript("X4CATALOGSERVICE").get("CatalogService")(service, cacheService, not True)


# get the work order
woSet = MXUtils.getMboSet("WORKORDER")
#woSet.setWhere("wonum = 'EVTT5_16304696'")
#woSet.setWhere("wonum = 'BVTT2_8434185_FCNG_021'")
#woSet.setWhere("wonum in ('CVTT4_8411299', 'CVTT4_8411854', 'CVTT4_8411857', 'CVTT4_8412021', 'CVTT4_8412024')")
#woSet.setWhere("wonum in ('EVTT2_8455557_FCNG_010', 'EVTT1_16394288_FCNG_010', 'DVTT1_8439793_FCNG_010', 'EVTT1_16342948_FCNG_003', 'EVTT5_8455139_FCNG_008')")
woSet.setWhere("wonum in ('EVTT3_16514915', 'FVTT2_16497714', 'CVTT5_8537385', 'CVTT4_8517350', 'CVTT4_8517351', 'DVTT4_8269516', 'CVTT1_8552711', 'CVTT1_8552712', 'CVTT1_8552713', 'CVTT5_8536580', 'CVTT5_8536578', 'CVTT5_8536579', 'CVTT5_8536581', 'CVTT5_8536610', 'DVTT4_8547743', 'CVTT5_8536608', 'CVTT5_8536609', 'CVTT5_8536614', 'CVTT5_8536611', 'CVTT5_8536612', 'CVTT5_8536613', 'CVTT5_8536616', 'DVTT3_8530411', 'DVTT3_8530412', 'DVTT3_8530413', 'DVTT3_8530414', 'DVTT3_8530432', 'DVTT3_8530470', 'DVTT3_8530471', 'DVTT3_8530472', 'DVTT3_8530515', 'FVTT6_8520739', 'FVTT6_8520738', 'FVTT6_8520740', 'HVTT1_8549948', 'AVTT3_16514789', 'AVTT3_16514786', 'AVTT3_16514787', 'AVTT3_16514788', 'AVTT3_16514790', 'AVTT3_16514791', 'AVTT3_16514792', 'AVTT3_16514793', 'EVTT2_16514046', 'AVTT3_16514794', 'EVTT2_16514047', 'AVTT3_16514795', 'EVTT2_16514048', 'EVTT2_16514057', 'EVTT2_16514049', 'AVTT4_16200610', 'AVTT4_16200608', 'AVTT4_16200609', 'AVTT4_16200611', 'AVTT4_16200612', 'CVTT5_16450679', 'CVTT5_16450678', 'CVTT5_16450680', 'CVTT5_16450681', 'CVTT5_16450682', 'CVTT5_16450683', 'CVTT5_16450721', 'CVTT5_16450722', 'CVTT5_16450723', 'CVTT5_16450724', 'CVTT5_16450725', 'CVTT5_16450726', 'CVTT5_16450727', 'CVTT5_16450728', 'CVTT5_16450729', 'CVTT5_16450730', 'CVTT5_16450731', 'FVTT1_16514571', 'FVTT5_16507353', 'FVTT2_16201485', 'FVTT2_16201486', 'FVTT2_16201487', 'FVTT2_16201488', 'FVTT2_16201489', 'FVTT2_16201493', 'FVTT2_16201490', 'FVTT2_16201491', 'FVTT2_16201492', 'FVTT2_16201494', 'FVTT2_16201495', 'EVTT3_16514912', 'EVTT3_16514913', 'EVTT3_16514914', 'EVTT3_16514916', 'EVTT3_16514917', 'EVTT3_16514918', 'EVTT3_16514919', 'EVTT3_16514920', 'EVTT3_16514924', 'EVTT3_16514921', 'EVTT3_16514922', 'EVTT3_16514923', 'EVTT3_16514925', 'DVTT4_16502649', 'DVTT4_16502651', 'DVTT4_8548286', 'DVTT4_8548283', 'DVTT4_8548284', 'DVTT4_8548285', 'DVTT4_8547944', 'DVTT4_8548287', 'DVTT4_8547942', 'DVTT4_8547943', 'DVTT4_8547945', 'DVTT4_8547946', 'DVTT4_8547947', 'FVTT2_16497712', 'FVTT2_16497713', 'FVTT2_16497715', 'FVTT2_16497716', 'AVTT2_8558158', 'AVTT2_8558139', 'AVTT2_8558138', 'AVTT2_8558136', 'AVTT2_8558137', 'AVTT2_8558159', 'AVTT2_8558160', 'AVTT2_8558161', 'AVTT2_8558162', 'AVTT2_8558163', 'AVTT2_8558164', 'AVTT2_8558167', 'AVTT2_8558135', 'AVTT2_8558166', 'AVTT2_8558165', 'BVTT2_16514868', 'BVTT2_16514870', 'BVTT2_16514866', 'BVTT2_16514865', 'BVTT2_16514864', 'BVTT2_16514863', 'BVTT2_16514862', 'BVTT2_16514861', 'BVTT2_16514873', 'BVTT2_16514874', 'BVTT2_16514875', 'BVTT2_16514869', 'BVTT2_16514872', 'BVTT2_16514871', 'BVTT2_16514867', 'DVTT4_16227455', 'DVTT4_16227458', 'DVTT4_16227456', 'DVTT4_16227457', 'DVTT4_8547742', 'BVTT2_16514847', 'BVTT2_16514845', 'BVTT2_16514846', 'BVTT2_16514834', 'BVTT2_16514835', 'BVTT2_16514839', 'BVTT2_16514836', 'BVTT2_16514837', 'BVTT2_16514838', 'BVTT2_16514840', 'BVTT2_16514841', 'BVTT2_16514842', 'BVTT2_16514843', 'BVTT2_16514844', 'DVTT2_16514421', 'DVTT2_16514424', 'DVTT2_16514422', 'DVTT2_16514423', 'DVTT2_16514420', 'DVTT2_16514419')")


startedAt = System.currentTimeMillis()

i = 0
wo = woSet.getMbo(0)
while wo != None:
	evalWo(wo)
	cacheService.stat()
	cacheService.clearCache("ASSET")
	i += 1
	wo = woSet.getMbo(i)

cacheService.clearCache("CATALOG")

Timer.dump("ASSETLOOKUP")
Timer.dump("CATALOGLOAD")
Timer.dump("EVALASSET")
Timer.reset()
info("{0} work orders completed in {1}", i, DateUtils.format(System.currentTimeMillis() - startedAt))