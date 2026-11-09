#---------------------------------------------------------------------------------------------------
#
# X4INVOKEASSETLOOKUP
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

log = MXLoggerFactory.getLogger("maximo.application")
logs = ""	

def info(message, *args):
	# logs 
	global logs
	msg = StringUtils.format("[%s] %s"%(service.scriptName, message), args)
	log.info(msg)
	# logs += "[I] " + msg + "\r\n"

def error(message, *args):
	# logs 
	global logs
	msg = StringUtils.format("[%s] %s"%(service.scriptName, message), args)
	log.error(msg)
	logs += "[E] " + msg + "\r\n"


# get the integration service
catalogService = service.invokeScript("X4CATALOGSERVICE").get("CatalogService")(service)

ruleSet = MXUtils.getMboSet("X4_RULES")
assetSet = MXUtils.getMboSet("ASSET")
navigationSet = MXUtils.getMboSet("X4_TIPOLOGIE_CORRELATE")
navigationSet.setWhere("catalogo in ('DIFFORMITA', 'INTERFERENZA')")

i = 0
navigationMbo = navigationSet.getMbo(0)
while navigationMbo != None:
	source = navigationMbo.getString("elemento_riferimento")
	target = navigationMbo.getString("elemento_target")
	info("testing navigation path {0} -> {1}", source, target)

	# get rule id
	ruleSet.setWhere("x4_tipologie_correlateid = %d"%navigationMbo.getUniqueIDValue())
	ruleSet.reset()
	rule = ruleSet.getMbo(0)
	if rule == None:
		error("{0} -> {1}: rule not found", source, target)
	else:
		ruleId = rule.getUniqueIDValue()
		# get asset
		assetSet.setWhere("siteid = 'SIT01' and classstructureid in (select classstructureid from classstructure where x4elemento = '%s')"%source)
		assetSet.reset()
		j = 0
		asset = assetSet.getMbo(0)
		if asset == None:
			error("{0} -> {1}: asset not found", source, target)
		
		while asset != None and j < 100:
			targetAssetKey = AssetKey(asset)
			try:
				# invoke look-up	
				assets = catalogService.invokeAssetLookUp(targetAssetKey, ruleId, None)
				info("navigation path {0} -> {1} source {2} -> {3} assets returned: {4}", source, target, targetAssetKey, assets.size(), StringUtils.join(", ", assets))
			except Exception as e:
				error("{0} -> {1}: source {2} failed", source, target, targetAssetKey)
			j += 1
			asset = assetSet.getMbo(j)

	i += 1
	navigationMbo = navigationSet.getMbo(i)
	
responseBody = logs