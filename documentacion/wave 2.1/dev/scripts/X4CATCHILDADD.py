"""
X4CATCHILDADD
Inizializza il MBO nel caso di aggiunta.

Version history
1.0 - 27/03/2026:
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
	
if mbo.isNew():
	info("set-up related asset type")

	# get catalog info
	catalog = mbo.getOwner()
	if not catalog.getBoolean("multielemento"):
		service.error("x3terna", "ErrorMsg", ["Non e' possibile aggiunger una tipologia correlata poichè il catalogo e' isolivello"])
		
	catalogName = catalog.getName()
	if catalogName == "X4CATDIFFOR":
		catalogType = "DIFFORMITA" 
	elif catalogName == "X4CATINTERF":
		catalogType = "INTERFERENZA" 
	else:
		service.error("x3terna", "ErrorMsg", [StringUtils.format("Il catalogo {0} non e' supportato", mboName)])

	mbo.setValue("type", catalogType)
	mbo.setValue("parentid", catalog.getUniqueIDValue())
	mbo.setValue("tipologiaparent", catalog.getString("tipologiaasset"))
	
