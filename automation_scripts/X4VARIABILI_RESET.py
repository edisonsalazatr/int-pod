"""
X4VARIABILI_RESET
Resetta i valori delle variabili calcolate di tipo FOMRULATESTOLIBERO al passaggio di un'attività in stato ESEGUITO
v1.0 - 11/06/2025
"""
from com.terna.common.maximo import MXUtils
from psdi.server import MXServer
from psdi.util.logging import MXLoggerFactory

logger = MXLoggerFactory.getLogger("maximo.terna")
prefix = "[ X4VARIABILI_RESET ] "

if mbo.isModified("status"): # Scatta solo se il salvataggio è legato al cambio stato
	# Recupera i record di X4MISUREASSET di tipo FORMULATESTOLIBERO associati all'asset dell'OdL e che hanno come parametro di reset l'attività
	misureAssetSet = MXUtils.getMboSet("X4MISUREASSET")
	try:
		misureAssetSet.setWhere("ASSETNUM = '%s' AND IDMISURA_EXT IN (SELECT m.IDMISURA FROM X4CATMISURE m, X4CATMISUREPARAMETRI p WHERE m.X4CATMISUREID = p.X4CATMISUREID AND m.CALCOLO = 'FORMULATESTOLIBERO' AND m.RESET = 1 AND p.CALCOLO_RESET = 'RESET' AND p.PARAMETER = '%s')"%(mbo.getString("assetnum"), mbo.getString("jpnum")))
		# Resetta a 0 il valore di tutti i record di X4MISUREASSET impattati
		if not misureAssetSet.isEmpty():
			for i in range(misureAssetSet.count()):
				misureAsset = misureAssetSet.getMbo(i)
				logger.info(prefix + "Status changed to ESEGUITO for activity %s: resetting value to 0 for variable %s and asset %s"%(mbo.getString("jpnum"), misureAsset.getString("idmisura_ext"), mbo.getString("assetnum")))
				misureAsset.setValue("valore", 0)
				misureAsset.setValue("ultimarilevazione", MXServer.getMXServer().getDate())
			misureAssetSet.save()
	finally:
		misureAssetSet.close()