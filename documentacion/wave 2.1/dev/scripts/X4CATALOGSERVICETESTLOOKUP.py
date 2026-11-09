#---------------------------------------------------------------------------------------------------
#
# X4CATALOGSERVICETESTLOOKUP
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
from java.util import Set
from java.util import HashSet
from java.util import ArrayList
from java.util import HashMap
from java.lang import StringBuffer
from com.terna.integration.common.asset import AssetKey

	

def info(self, message, *args):
	if log == None:
	    log = MXLoggerFactory.getLogger("maximo.application")
	# logs 
	log.info(self.StringUtils.format("[%s] %s"%(self.service.scriptName, message), args))

# get asset
assetSet = MXUtils.getMboSet("ASSET")
assetSet.setWhere("assetnum = '22238378' and siteid = 'SIT01'")
asset = assetSet.getMbo(0)
targetAssetKey = AssetKey(asset)

# get asset type
classStructureSet = MXUtils.getMboSet("CLASSSTRUCTURE")
classStructureSet.setWhere("classstructureid = %d"%(asset.getLong("classstructureid")))
assetType = classStructureSet.getMbo(0).getString("x4elemento")

# get the integration service
catalogService = service.invokeScript("X4CATALOGSERVICE").get("CatalogService")(service)

relatedSet = MXUtils.getMboSet("X4_TIPOLOGIE_CORRELATE")
relatedSet.setWhere("elemento_riferimento = '" + assetType + "' and catalogo = 'DIFFORMITA'")
related = relatedSet.getMbo(0)
i = 0
ruleSet = MXUtils.getMboSet("X4_RULES")
while related != None:
	ruleSet.setWhere("x4_tipologie_correlateid = %d"%(related.getUniqueIDValue()))
	ruleSet.reset()
	rule = ruleSet.getMbo(0)
	# check the catalog
	catalogService.invokeAssetLookUp(targetAssetKey, rule.getUniqueIDValue(), None)


	i += 1
	related = relatedSet.getMbo(i)

#catalogService.invokeAssetLookUp(targetAssetKey, 28, None)