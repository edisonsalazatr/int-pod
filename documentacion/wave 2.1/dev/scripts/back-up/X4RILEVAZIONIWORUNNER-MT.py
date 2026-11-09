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
	ctx.put("catalogService", catalogService)
	ctx.put("wo", wo)
	cacheService.addCache("ASSET")
	service.invokeScript("X4RILEVAZIONIWO", ctx)
	cacheService.clearCache("ASSET")
				
def evalQuery(query):
	# get the work order
	woSet = MXUtils.getMboSet("WORKORDER")
	woSet.setWhere(query.getString("whereclause"))

	startedAt = System.currentTimeMillis()
	# add catalog cache which will be shared among all the work order evaluations
	cacheService.addCache("CATALOG")

	scriptPoolExecutor = service.invokeScript("X4SCRIPTPOOLEXECUTOR").get("X4SCRIPTPOOLEXECUTOR")(service, "X4RILEVAZIONIWO", 5)

	# go thru work orders
	i = 0
	wo = woSet.getMbo(0)
	while wo != None:
		# evaluate work order
		ctx = HashMap()
		ctx.put("service", service)
		ctx.put("cacheService", cacheService)
		ctx.put("catalogService", catalogService)
		ctx.put("wo", wo)
		scriptPoolExecutor.startThread(ctx)

		# move to next work order
		i += 1
		wo = woSet.getMbo(i)

	scriptPoolExecutor.waitAllThreads()
	# clear catalog cache
	cacheService.clearCache("CATALOG")
	
	# update the query info
	elapsed = System.currentTimeMillis() - startedAt
	query.setValue("lastexecutiontime", DateUtils.format(elapsed))
	query.setValue("lastrun", DateUtils.now())
	query.setValue("lastprocessed", i)
	query.setValue("pending", 0)

	# dump some statistics
	info("{0} work orders completed in {1}", i, DateUtils.format(System.currentTimeMillis() - startedAt))
					
# set log verbosity
logVerbose = False
# get logger
log = MXLoggerFactory.getLogger("maximo.application")

# set-up the cache service
cacheService = service.invokeScript("X4CACHESERVICE").get("CacheService")(service)
# get the catalog service
catalogService = service.invokeScript("X4CATALOGSERVICE").get("CatalogService")(service, cacheService, not True)

querySet = MXUtils.getMboSet("X4RILEVAZIONIODLQUERY")
i = 0
query = querySet.getMbo(0)
while query != None:
	evalQuery(query)
	i += 1
	query = querySet.getMbo(i)
	
querySet.save()