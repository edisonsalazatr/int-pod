#---------------------------------------------------------------------------------------------------
#
# X4TESTRILEVAZIONIWO
#
#   1.0: implementation
#
#---------------------------------------------------------------------------------------------------


from psdi.server import MXServer
from psdi.util.logging import MXLoggerFactory
from psdi.mbo import SqlFormat
from psdi.mbo import MboConstants
from psdi.mbo import SqlFormat
from com.terna.common.string import StringUtils
from com.terna.integration.common.asset import AssetKey
from com.terna.common.maximo import MXUtils
from java.lang import Exception
from java.util import Set
from java.util import HashSet
from java.util import ArrayList
from java.util import HashMap
from java.lang import StringBuffer

log = MXLoggerFactory.getLogger("maximo.catalog")
logs = ""	

def info(message, *args):
	# logs 
	global logs
	msg = StringUtils.format("[%s] %s"%(service.scriptName, message), args)
	log.info(msg)
	logs += msg + "\r\n"

def error(message, *args):
	# logs 
	global logs
	msg = StringUtils.format("[%s] %s"%(service.scriptName, message), args)
	log.error(msg)
	logs += msg + "\r\n"


# get the work order
woNum = "DVTT2_16303619"
info("getting work order {0}", woNum)
woSet = MXUtils.getMboSet("WORKORDER")
woSet.setWhere("siteid = 'SIT01' and wonum = '%s'"%woNum)
wo = woSet.getMbo(0)

# set-up the cache service
info("setting up cache service")
cacheService = service.invokeScript("X4CACHESERVICE").get("CacheService")(service)
# add caches which will be shared among all the work order evaluations
cacheService.addCache("CATALOG")
cacheService.addCache("ASSET")


ctx = HashMap()
ctx.put("service", service)
ctx.put("cacheService", cacheService)
ctx.put("wo", wo)

# get the script
info("evaluating work order")
woService = service.invokeScript("X4RILEVAZIONIWO", ctx).evalWo()
info("done")

