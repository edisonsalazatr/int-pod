from java.lang import System
from java.lang import Throwable
from java.util import HashMap
from java.util import HashSet
from java.util import ArrayList
from java.util import Date
from psdi.util.logging import MXLoggerFactory
from psdi.mbo import SqlFormat
from psdi.server import MXServer
from psdi.mbo import SqlFormat
from psdi.mbo import MboConstants
from com.terna.common.string import StringUtils
from com.terna.common.maximo import MXUtils
from com.terna.common.date import DateUtils
from com.terna.common.db import DbUtils
from com.terna.integration.common.asset import AssetKey

# logs an INFO verbose message
def verbose(message, *args):
	# logs
	if logVerbose and log is not None:
		if "wo" in globals() and wo != None:
			log.info(StringUtils.format("[X4GETWORELATEDASSETS(" + wo.getString("wonum") + ")] " + message, args))
		else:
			log.info(StringUtils.format("[X4GETWORELATEDASSETS] " + message, args))


# logs an INFO message
def info(message, *args):
	# logs
	if log is not None:
		if "wo" in globals() and wo != None:
			log.info(StringUtils.format("[X4GETWORELATEDASSETS(" + wo.getString("wonum") + ")] " + message, args))
		else:
			log.info(StringUtils.format("[X4GETWORELATEDASSETS] " + message, args))

#
# logs an ERROR message
#
def error(message, *args):
	# logs
	if log is not None:
		if "wo" in globals() and wo != None:
			log.error(StringUtils.format("[X4GETWORELATEDASSETS(" + wo.getString("wonum") + ")] " + message, args))
		else:
			log.error(StringUtils.format("[X4GETWORELATEDASSETS] " + message, args))

#
#	wo[MboRemote]: the work order
#	@return[bool]: if the work order must be updated
#
def canUpdateRelatedAssets(wo):
	if wo.getBoolean("istask"):
		# does not apply to tasks
		verbose("work order is a task, skipped")
		return False

	if wo.getBoolean("x4serbatoio"):
		# does not apply to workorder where x4serbatoio is flagged
		verbose("work order is a serbatoio, skipped")
		return False
		
	# check job plan classification
	jobPlanSet = wo.getMboSet("$active_jobplan$", "JOBPLAN", "jpnum=:jpnum and siteid=:siteid and status = 'ACTIVE'")
	jobPlan = jobPlanSet.getMbo(0)
	if jobPlan is None:
		# job plan not found
		verbose("job plan not found, skipped")
		return False
	if jobPlan.isNull("classstructureid"):
		# job plan not classified
		verbose("work order not classified, skipped")
		return False
	classificationMbo = jobPlan.getMboSet("CLASSSTRUCTURE").getMbo(0)
	if classificationMbo == None:
		# no classification found
		verbose("work order classification not found, skipped")
		return False
	classification = classificationMbo.getString("classificationid")
	if StringUtils.notIn(classification, "CONTROLLO DI LEGGE" , "CONTROLLO OCCASIONALE", "CONTROLLO PERIODICO", "CONTROLLO SU CONDIZIONE", "CONTROLLO TECNICO", "PRONTO INTERVENTO"):
		verbose("work order classified has {0}, skipped", classification)
		return False
	info("work order classified has {0}", classification)
	return True


#
# Returns an HashMap containing all the assets related to the workorder's asset based on the
# related asset types defined in the workorder's job plan. The workorder's asset will be included.
#
#	wo[MboRemote]: the work order
#	@return[HashMap]: map containing the asset number (key) and asset type (value)
#
def getWoAssets(wo):
	st = System.currentTimeMillis()
	# set which collects all the asset number
	assets = HashMap()

	# get the last active job plan
	jobPlanSet = wo.getMboSet("$active_jobplan$", "JOBPLAN", "jpnum=:jpnum and siteid=:siteid and status = 'ACTIVE'")
	jobPlan = jobPlanSet.getMbo(0)

	# get the work order asset key
	targetAssetKey = AssetKey(wo.getMboSet("ASSET").getMbo(0))
	# put the root asset
	assets.put(wo.getString("assetnum"), jobPlan.getString("x4tipologiaasset"))

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
		info("    found {0} assets for asset type {1}", relatedAssets.size(), relatedAssetType)
		# getting asset numbers
		for j in range(0, relatedAssets.size()):
			assetNum = relatedAssets.get(j).getNum()
			assets.put(assetNum, relatedAssetType)

		i += 1
		relatedAssetTypeMbo = relatedAssetTypes.getMbo(i)

	verbose("got {0} assets in {1}", assets.size(), DateUtils.format(System.currentTimeMillis()-st))
	return assets

#
# Saves the related assets
#
#	wo[MboRemote]: the work order
#	assets[HashMap]: the asset map
#
def saveWoAsset(wo, assets):
	st = System.currentTimeMillis()
	# get common data
	orgId = wo.getString("orgid")
	siteId = wo.getString("siteid") 
	woNum = wo.getString("wonum")
	jpNum = wo.getString("jpnum")
	revision = wo.getString("pluscjprevnum")
	woAssetNum = wo.getString("assetnum")

	# remove previous records
	deleteStatement = "delete from x4worelatedasset where siteid = '{0}' and jpnum = '{1}' and woassetnum = '{2}'"
	deleteStatement = StringUtils.format(deleteStatement, siteId, jpNum, woAssetNum)
	verbose("removing previous records. query = {0}", deleteStatement)
	DbUtils.runSql(deleteStatement)

	# add assets
	mboSet = None
	try:
		verbose("adding {0} assets", assets.size())
		mboSet = MXUtils.getMboSet("X4WORELATEDASSET")
		mboSet.setInsertOrg(orgId)
		mboSet.setInsertSite(siteId)
		for assetNum in assets.keySet():
			mbo = mboSet.add()
			mbo.setValue("sourcewonum", woNum)
			mbo.setValue("jpnum", jpNum)
			mbo.setValue("revisionnum", revision)
			mbo.setValue("woassetnum", woAssetNum)
			mbo.setValue("assetnum", assetNum)
			mbo.setValue("assettype", assets.get(assetNum))

		# save assets		
		verbose("saving")
		mboSet.save()
		verbose("saved {0} assets in {1}", assets.size(), DateUtils.format(System.currentTimeMillis()-st))
		return assets
	finally:
		if mboSet != None:
			mboSet.close()

def getWo(mbo):
	if mbo != None:
		return mbo
	else:
		# get work order
		woSet = MXUtils.getMboSet("WORKORDER")	
		#woSet.setWhere("status = 'APERTO' and JPNUM = 'AT.L.1.0.0.1.1' and istask = 0")
		woSet.setWhere("wonum in ('PL_1265576')")
		return woSet.getMbo(0)

def main(mbo):
	# current work order
	global wo
	wo = None
	woNum = ""
	# initialize services
	global cacheService
	global catalogService
	cacheService = None
	catalogService = None
	# set log info
	logCatalog = False
	# set log verbosity
	global log
	global logVerbose
	log = None
	logVerbose = False
	try:	
		# get logger
		log = MXLoggerFactory.getLogger("maximo.catalog")

		# get the work order
		wo = getWo(mbo)
		if wo is not None:
			woNum = wo.getString("wonum")
			st = System.currentTimeMillis()
			
			if not canUpdateRelatedAssets(wo):
				info("work order {0} must not be updated", woNum)
			else:
				# set-up the cache service
				verbose("setting up cache service")
				cacheService = service.invokeScript("X4CACHESERVICE").get("CacheService")(service)
				# add caches which will be shared among all the work order evaluations
				cacheService.addCache("ASSET")
				# get the catalog service
				catalogService = service.invokeScript("X4CATALOGSERVICE").get("CatalogService")(service, cacheService, logCatalog)
				catalogService.setLogId(woNum)
				
				# getting related assets
				info("getting related assets for work order {0}", woNum)
				assets = getWoAssets(wo)

				# save related assets
				info("saving related assets for work order {0}", woNum)
				saveWoAsset(wo, assets)

				info("added {0} assets in {1} for work order {2} jpnum {3} assetnum {4}", assets.size(), DateUtils.format(System.currentTimeMillis()-st), woNum, wo.getString("jpnum"), wo.getString("assetnum"))

	except (Exception, Throwable) as e:
		error("unable to collect related assets for work order {0}", woNum)
		error("error {0}", str(e))
	finally:
		try:
			# clear cache
			if cacheService is not None:
				verbose("cleaning cache service")
				cacheService.stat()
				cacheService.clearCache("ASSET")
		except (Exception, Throwable) as e:
			error("error cleaning cache {0}", str(e))
				

if "mbo" in globals():
	main(mbo)
else:
	main(None)