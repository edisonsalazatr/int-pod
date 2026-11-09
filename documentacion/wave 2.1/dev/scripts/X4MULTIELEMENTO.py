"""
X4MULTIELEMENTO
The script manages MULTIELEMENTO flag change for difformità and interferenze.
If the flag is set to yes the related asset types will be loaded into X4CATCHILD.
If the flag is set to no the related asset types will be removed from X4CATCHILD.

Version history
1.0 - 02/02/2026:
	first implementation
"""
from psdi.util.logging import MXLoggerFactory
from com.terna.common.string import StringUtils;
from psdi.mbo import SqlFormat
from com.terna.common.maximo import MXUtils

# logs an INFO message 
def info(message, *args):
	# logs 
	log.info(StringUtils.format("[" + scriptName + "] " + message, args))
	
# get logger
log = MXLoggerFactory.getLogger("maximo.catalog")

# get catalog info
mboName = mbo.getName()
if mboName == "X4CATDIFFOR":
	catalogType = "DIFFORMITA" 
	catalogId = mbo.getString("iddifformita")
elif mboName == "X4CATINTERF":
	catalogType = "INTERFERENZA" 
	catalogId = mbo.getString("idinterferenza")
else:
	service.error("x3terna", "ErrorMsg", [StringUtils.format("Il catalogo {0} non e' supportato", mboName)])

multiLevel = mbo.getBoolean("multielemento")
id = mbo.getUniqueIDValue()
catalogAssetType = mbo.getString("tipologiaasset")

info("flagging catalog {0}.{1} as multi element {2}", catalogType, catalogId, multiLevel)

if multiLevel == False:
	mainAssetType = mbo.getString("tipologiaasset")
	info("removing specifications excpet for {0}", mainAssetType)
	# delete specifications for related asset types
	relation = ""
	if mboName == "X4CATDIFFOR":
		relation = "X4CARDIFFOR"
	else: 
		relation = "X4CARDINTERF"
	specSet = mbo.getMboSet(relation) 
	i = 0
	spec = specSet.getMbo(0)
	while spec != None:
		if spec.getString("tipologiaasset") != mainAssetType:
			spec.delete()
		i += 1
		spec = specSet.getMbo(i)
	
	# delete related asset types
	info("removing related asset types")
	childSet = mbo.getMboSet("X4CATCHILD")
	childSet.deleteAll()

	# save
	info("saving sepcifications")
	MXUtils.saveSet(specSet)
	info("saving related asset types")
	MXUtils.saveSet(childSet)
