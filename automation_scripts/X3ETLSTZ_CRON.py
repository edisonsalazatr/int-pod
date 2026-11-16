"""
X3ETLSTZ_CRON
ETL Stazioni - Script richiamabile come crontask per avviare un'istanza di import delle Stzioni
Carica una riga dalla tabella delle pianificazioni X3ETLSTZLOG in stato WSTART e con ETLMODE uguale al parametro del crontask SCRIPTARG (obbligatorio)
ed avvia il processo di import richiamando uno degli script X3ETLSTZ_LAUNCHER o X3ETLSTZ_LAUNCHER_D in base al valore di ETLMODE
v1.0 26/04/2024
v1.1 22/07/2025
	Aggiunta gestione parametro crontask SCRIPTARG per indicare la modalità di esecuzione dell'ETL (FULL o DISMESSI)
"""
from com.terna.common.maximo import MXUtils
from com.terna.common.log import Logger
from com.terna.common.string import StringUtils
from java.util import HashMap

# Carica il logger
logger = Logger.getLogger("ETLSTZ.X3ETLSTZ_CRON")

if StringUtils.isEmpty(arg) or arg not in ["FULL", "DISMESSI"]:
	logger.warn("SCRIPTARG must be FULL or DISMESSI")
else:
	# Carica una riga di pianificazione da processare
	etlLogSet = MXUtils.getMboSet("X3ETLSTZLOG") # Mbo Set delle righe di pianificazione
	etlLogSet.setWhere("status = 'WSTART' and etlmode = '%s'"%arg)
	etlLogSet.setOrderBy("etlnum")
	etlLog = etlLogSet.getMbo(0)
	
	try:
		if etlLog:
			# Avvia il processo di import
			etlNum = etlLog.getString("etlnum")
			logger.info("Starting planned import %s with mode %s for stations: %s"%(etlNum, arg, etlLog.getString("rootstrno")))
			ctx = HashMap()
			ctx.put("etlnum", etlNum)
			scriptName = "X3ETLSTZ_LAUNCHER" if arg == "FULL" else "X3ETLSTZ_LAUNCHER_D"
			service.invokeScript(scriptName, ctx)
		else:
			logger.debug("No planned import to start")
	finally:
		etlLogSet.close()