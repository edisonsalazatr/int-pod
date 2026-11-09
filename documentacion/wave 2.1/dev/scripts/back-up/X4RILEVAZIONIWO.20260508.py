#---------------------------------------------------------------------------------------------------
#
# X4RILEVAZIONIWO
#
#   1.0: implementation
#
#---------------------------------------------------------------------------------------------------


from java.lang import System 
from java.lang import Exception as javaEx 
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


# logs an INFO verbose message 
def verbose(message, *args):
	# logs 
	if logVerbose:
		log.info(StringUtils.format("[X4RILEVAZIONIWO(" + wo.getString("wonum") + ")] " + message, args))

# logs an INFO message 
def info(message, *args):
	# logs 
	log.info(StringUtils.format("[X4RILEVAZIONIWO(" + wo.getString("wonum") + ")] " + message, args))

# 
# logs an ERROR message 
#
def error(self, message):
	# logs 
	log.error(StringUtils.format("[X4RILEVAZIONIWO(" + wo.getString("wonum") + ")] " + message))
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
		query = "siteid = :siteid and (assetnum = :assetnum or parent = :asset.parent)"
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
	sqlFormat = SqlFormat(wo, "(iddifformita is not null or idinterferenza is not null) and idorigine in (select x4microcontrollo from jobtask where jpnum=:jpnum and pluscjprevnum = :pluscjprevnum)")
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
		woAssetNum = wo.getString("assetnum")
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
			where = "status = 'ATTIVO' and x4catdifforid in (select x4catdifforid from x4difforlassifications where classificationid = :0)"
			where += " and iddifformita in (select iddifformita from x3catassociazione where iddifformita is not null and idorigine = :jpnum)"
			sqlFormat = SqlFormat(wo, where)
			sqlFormat.setObject(0, "UPPER", assetClassification)
			added2 = evalAsset(catalogMatchSet, wo, asset, "X4CATDIFFOR", sqlFormat.format())
			totalAdded2 += added2
			
			# add X4RILEVAZIONIODL for catalog X4CATINTERF entries for the asset
			where = "status = 'ATTIVO' and x4catinterfid in (select x4catinterfid from x4interfclassifications where classificationid = :0)"
			where += " and idinterferenza in (select idinterferenza from x3catassociazione where idinterferenza is not null and idorigine = :jpnum)"
			sqlFormat = SqlFormat(wo, where)
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
				where = "status = 'ATTIVO' and x4catdifforid in (select x4catdifforid from x4difforlassifications where classificationid = :0)"
				where += " and iddifformita in (select iddifformita from x3catassociazione where iddifformita is not null and idorigine in (select x4microcontrollo from jobtask where jpnum = :jpnum))"
				sqlFormat = SqlFormat(wo, where)
				sqlFormat.setObject(0, "UPPER", assetClassification)
				added2 = evalAsset(catalogMatchSet, wo, asset, "X4CATDIFFOR", sqlFormat.format())
				totalAdded2 += added2

				# add X4RILEVAZIONIODL for catalog X4CATINTERF entries for the asset
				where = "status = 'ATTIVO' and x4catinterfid in (select x4catinterfid from x4interfclassifications where classificationid = :0)"
				where += " and idinterferenza in (select idinterferenza from x3catassociazione where idinterferenza is not null and idorigine in (select x4microcontrollo from jobtask where jpnum = :jpnum))"
				sqlFormat = SqlFormat(wo, where)
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

	if catalogMatchSet != None:
		catalogMatchSet.close()
	if assetSet != None:
		assetSet.close()

# set log info
logCatalog = not False
# set log verbosity
logVerbose = False
# get logger
log = MXLoggerFactory.getLogger("maximo.catalog")
# map of misure references by asset (HashMap<String, String>)
refMap = HashMap()
# map of asset type for each asset  (HashMap<String, String>)
assetTypeByAsset = HashMap()
# get the catalog service
catalogService = service.invokeScript("X4CATALOGSERVICE").get("CatalogService")(service, cacheService, logCatalog)
catalogService.setLogId(wo.getString("wonum"))
# evaluate work order
evalWo(wo)