#---------------------------------------------------------------------------------------------------
#
# X4CATALOGSERVICETEST
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


# get the integration service
catalogService = service.invokeScript("X4CATALOGSERVICE").get("CatalogService")(service)

# get asset
assetNum = request.getQueryParam("assetnum")
if StringUtils.isEmpty(assetNum):
	error("no assetnum provided")
else:
	assetSet = MXUtils.getMboSet("ASSET")
	assetSet.setWhere("siteid = 'SIT01' and assetnum = '%s'"%assetNum)
	asset = assetSet.getMbo(0)
	classStructure = asset.getMboSet("CLASSSTRUCTURE").getMbo(0)	
	tipologiaAsset = classStructure.getString("x4elemento")
		
	# get catalog (DIFFORMITA)
	catalogSet = MXUtils.getMboSet("X4CATDIFFOR")
	catalogSet.setWhere("tipologiaasset = '%s'"%tipologiaAsset)

	i = 0
	catalog = catalogSet.getMbo(0)
	while catalog != None:
		# check the catalog
		isoElemento =  catalog.getBoolean("isoelemento")
		try:
			catalogService.setLogId(asset.getString("assetnum"))
			evaluation = catalogService.checkCatalog(catalog, asset)		
			info("catalog {0} tipologia {1} isoelemento {2} evaluated with success result {3}", catalog.getString("iddifformita"), tipologiaAsset, isoElemento, evaluation)
		except Exception as e:
			error("unable to evaluate catalog {0} tipologia {1} isoelemento {2} error {3}", catalog.getString("iddifformita"), tipologiaAsset, isoElemento, e.getMessage())
		i += 1
		catalog = catalogSet.getMbo(i)


	# get catalog (INTERFERENZA)
	catalogSet = MXUtils.getMboSet("X4CATINTERF")
	catalogSet.setWhere("tipologiaasset = '%s' and 1=0"%tipologiaAsset)
	i = 0
	catalog = catalogSet.getMbo(0)
	while catalog != None:
		# check the catalog
		isoElemento =  catalog.getBoolean("isoelemento")
		try:
			evaluation = catalogService.checkCatalog(catalog, asset)		
			info("catalog {0} tipologia {1} isoelemento {2} evaluated with success result {3}", catalog.getString("idinterferenza"), tipologiaAsset, isoElemento, evaluation)
		except Exception as e:
			error("unable to evaluate catalog {0} tipologia {1} isoelemento {2} error {3}", catalog.getString("idinterferenza"), tipologiaAsset, isoElemento, e.getMessage())
		i += 1
		catalog = catalogSet.getMbo(i)

responseBody = logs