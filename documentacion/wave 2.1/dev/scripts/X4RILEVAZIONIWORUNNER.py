#---------------------------------------------------------------------------------------------------
#
# X4RILEVAZIONIWORUNNER
#
#   1.0: implementation
#
#---------------------------------------------------------------------------------------------------

from java.lang import System 
from java.lang import Exception 
from java.util import HashMap
from psdi.util.logging import MXLoggerFactory
from psdi.mbo import SqlFormat
from psdi.server import MXServer
from psdi.mbo import SqlFormat
from psdi.mbo import MboConstants
from com.terna.common.string import StringUtils
from com.terna.common.maximo import MXUtils
from com.terna.common.date import DateUtils

# logs an INFO verbose message 
def verbose(message, *args):
	# logs 
	if logVerbose:
		log.info(StringUtils.format("[X4RILEVAZIONIWORUNNER] " + message, args))

# logs an INFO message 
def info(message, *args):
	# logs 
	log.info(StringUtils.format("[X4RILEVAZIONIWORUNNER] " + message, args))

# 
# logs an ERROR message 
#
def error(self, message, *args):
	# logs 
	log.error(StringUtils.format("[X4RILEVAZIONIWORUNNER] " + message, args))


def evalWo(wo):
	ctx = HashMap()
	ctx.put("service", service)
	ctx.put("cacheService", cacheService)
	ctx.put("wo", wo)
	scriptPoolExecutor.startThread(ctx)
				
def evalQuery(query):
	startedAt = System.currentTimeMillis()
	
	# get limit
	limit = query.getInt("recordlimit")

	# get the work order
	woSet = MXUtils.getMboSet("WORKORDER")
	woSet.setFlag(MboConstants.DISCARDABLE, True)
	woSet.setWhere(query.getString("whereclause"))
	woSet.setOrderBy("assetnum,jpnum")

	# go thru work orders
	i = 0
	wo = woSet.getMbo(0)
	while wo != None:
		# check limit
		if limit > 0 and limit < (i+1):
			info("record limit {0} reached, stopping evaluation", limit)
			break
		# evaluate work order
		info("evaluating work-order {0}", i)
		evalWo(wo)
		# move to next work order
		i += 1
		wo = woSet.getMbo(i)
	
	# wait all threads
	scriptPoolExecutor.waitAllThreads()
	
	# update the query info
	elapsed = System.currentTimeMillis() - startedAt
	query.setValue("lastexecutiontime", DateUtils.format(elapsed))
	query.setValue("lastrun", DateUtils.now())
	query.setValue("lastprocessed", i)
	query.setValue("pending", 0)

	# dump some statistics
	elapsed = System.currentTimeMillis() - startedAt
	info("{0} work orders completed in {1}, average {2}", i, DateUtils.format(elapsed), DateUtils.format(elapsed/(i+1)))
					
# set the multi-threading level
maxThreads = 15					
# set log verbosity
logVerbose = False
# get logger
log = MXLoggerFactory.getLogger("maximo.catalog")

# set-up the cache service
info("setting up cache service")
cacheService = service.invokeScript("X4CACHESERVICE").get("CacheService")(service)
# add caches which will be shared among all the work order evaluations
cacheService.addCache("CATALOG")
cacheService.addCache("ASSET")
scriptPoolExecutor = service.invokeScript("X4SCRIPTPOOLEXECUTOR").get("X4SCRIPTPOOLEXECUTOR")(service, "X4RILEVAZIONIWO", maxThreads)	
	
querySet = MXUtils.getMboSet("X4RILEVAZIONIODLQUERY")
i = 0
query = querySet.getMbo(0)
while query != None:
	info("[{0}]evaluating query {1}", (i+1), query.getString("whereclause"))
	evalQuery(query)
	i += 1
	query = querySet.getMbo(i)

# clear cache
info("cleaning cache service")
cacheService.stat()
cacheService.clearCache("CATALOG")
cacheService.clearCache("ASSET")
	
info("saving results")
querySet.save()