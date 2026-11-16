"""
X3ETLSTZ_LAUNCHER
ETL SAP - Script richiamabile come API o da crontask per avviare l'import di una o più stazioni
Richiama nell'ordine i seguenti script:
	X3ETLSTZ_DATATRANSFER
	X3ETLSTZ_PREIMPORT
	X3ETLSTZ_IMPORT
v1.0 23/03/2024
v1.1 26/04/2024
	Aggiunta doppia gestione (da API e da crontask)
	Aggiunta gestione righe di log nella tabella X3ETLSTZLOG
v1.2 04/10/2024
	Aggiunta delete mode
Parametri:
	etlnum: id del record di pianificazione, se passato i parametri rootstrno, importid ed extractionid vengono ignorati
	rootstrno (obbligatorio senza etlnum): elenco degli strno delle stazioni separati da virgola (es. 31202C,31204B,31301A)
	importid: id univoco dell'import, usato per associare diverse chiamate dello script allo stesso processo di import. In caso di API se non passato viene impostato al timestamp corrente
	extractionid (obbligatorio per step data transfer senza etlnum e non in delete mode): id univoco dell'estrazione SAP, usato per copiare i dati dal DB on premise
	deletemode: indica se l'ETL deve essere eseguito in modalità cancellazione, ovvero verranno ripuliti i dati senza ulteriori caricamenti (default: non attivo)
	step_datatransfer (solo API): indica se eseguire lo step di DATATRANSFER (0 = non attivo, altrimenti attivo) (default: attivo)
	step_preimport (solo API): indica se eseguire lo step di PREIMPORT (0 = non attivo, altrimenti attivo) (default: attivo)
	step_import (solo API): indica se eseguire lo step di IMPORT (0 = non attivo, altrimenti attivo) (default: attivo)
"""
from com.terna.common.maximo import MXUtils
from com.terna.common.string import StringUtils
from com.terna.common.date import DateUtils
from com.terna.common.log import Logger
from psdi.mbo import SqlFormat
from psdi.server import MXServer
from java.util import HashMap
from java.text import SimpleDateFormat
from java.lang import Exception
from java.lang import System

def createEtlLog():
	utils = service.invokeScript("X3ETLSTZ_UTILS").get("X3ETLSTZUTILS")(None, None) # Utilities
	try:
		stazStrno = "'" + rootStrno.replace(",","','") + "'"
		if steps2Exec["DATATRANSFER"] != "0":
			selectSql = """SELECT count(*) numrecords FROM (
	SELECT 1 FROM X3SEDITECHSAP_STZ_IF@ONPREMISE s WHERE substr(s.STRNO,1,6) IN (%s) AND s.EXTRACTION_ID = '%s'
	UNION ALL
	SELECT 1 FROM X3EQUIPMENTSAP_STZ_IF@ONPREMISE e WHERE substr(e.TPLNR,1,6) IN (%s) AND e.EXTRACTION_ID = '%s'
)"""%(stazStrno, extractionId, stazStrno, extractionId)
		else:
			selectSql = """SELECT count(*) numrecords FROM (
	SELECT 1 FROM X3SEDITECHSAP_STZ s WHERE substr(s.STRNO,1,6) IN (%s)
	UNION ALL
	SELECT 1 FROM X3EQUIPMENTSAP_STZ e WHERE substr(e.TPLNR,1,6) IN (%s)
)"""%(stazStrno, stazStrno)
		rs = utils.executeQuery("SELECT", "Recupero numero oggetti da processare", selectSql)
		numRecords = rs.getInt("numrecords") if rs.next() else -1
		# Crea la riga di pianificazione
		etlLog = etlLogSet.add()
		etlLog.getMboValue("etlnum").autoKey()
		etlLog.setValue("phase", "ETL")
		etlLog.setValue("status", "WSTART")
		etlLog.setValue("numrecords", numRecords)
		etlLog.setValue("importid", importId)
		if steps2Exec["DATATRANSFER"] != "0":
			etlLog.setValue("extractionid", extractionId)
		etlLog.setValue("rootstrno", rootStrno)
		return etlLog
	finally:
		utils.close()
	
# Gestione parametri non passati	
try: request
except: request = None
try: etlnum
except: etlnum = None

# Carica il logger
logger = Logger.getLogger("ETLSTZ.X3ETLSTZ_LAUNCHER")

# Verifica se richiamato da API o da CronTask
if request == None:
	fromCron = True
else:
	fromCron = False
	etlnum = request.getQueryParam("etlnum")
	
etlLogSet = MXUtils.getMboSet("X3ETLSTZLOG") # Mbo Set delle righe di pianificazione
etlLog = None

# Recupera i parametri
etlSteps = ["DATATRANSFER", "PREIMPORT", "IMPORT"]
steps2Exec = {}
if etlnum:
	# Invocato da CronTask o da API con parametro etlnum, recupera i parametri dalla pianificazione
	sqf = SqlFormat("etlnum = :1 and status = 'WSTART'")
	sqf.setObject(1, "UPPER", etlnum)
	etlLogSet.setWhere(sqf.format())
	etlLog = etlLogSet.getMbo(0)
	if etlLog:
		rootStrno = etlLog.getString("rootstrno")
		importId = etlLog.getString("importid")
		extractionId = etlLog.getString("extractionid")
		steps2Exec["DATATRANSFER"] = "1"
		steps2Exec["PREIMPORT"] = "1"
		steps2Exec["IMPORT"] = "1"
else:
	# Invocato da API senza etlnum, recupera i parametri dalla request
	rootStrno = request.getQueryParam("rootstrno")
	importId = request.getQueryParam("importid")
	if importId == None:
		sdf = SimpleDateFormat("yyyyMMddHHmmss")
		now = MXServer.getMXServer().getDate()
		importId = sdf.format(now)
	extractionId = request.getQueryParam("extractionid")
	steps2Exec["DATATRANSFER"] = request.getQueryParam("step_datatransfer") or "1"
	steps2Exec["PREIMPORT"] = request.getQueryParam("step_preimport") or "1"
	steps2Exec["IMPORT"] = request.getQueryParam("step_import") or "1"
	
# Gestisce i parametri non obbligatori per applicare valori di default
if request:
	deleteMode = request.getQueryParam("deletemode") or "0"
else:
	# Se richiamato dal crontask si assume che la procedura sia in modalità di caricamento
	deleteMode = "0"

# Verifica i parametri obbligatori
errorMsg = None
if etlnum and not etlLog:
	errorMsg = "L'import pianificato %s non esiste o non si trova nello stato WSTART"%etlnum
elif rootStrno == None:
	errorMsg = "Il parametro rootstrno è obbligatorio"
elif importId == None:
	errorMsg = "Il parametro importid è obbligatorio"
elif steps2Exec["DATATRANSFER"] != "0" and deleteMode == "0" and extractionId == None:
	errorMsg = "Il parametro extractionid è obbligatorio se lo step data transfer è attivo e non è abilitata la delete mode"

# Se non ci sono errori avvia l'import
if errorMsg == None:
	if not etlnum:
		etlLog = createEtlLog()
		etlnum = etlLog.getString("etlnum")
	execTimes = {
		"DATATRANSFER": 0,
		"PREIMPORT": 0,
		"IMPORT": 0
	}
	mxServer = MXServer.getMXServer()
	etlLogId = etlLog.getUniqueIDValue()
	start = System.currentTimeMillis()
	etlLog.setValue("startdate", mxServer.getDate()) # Setta la data di inizio del record principale
	etlLog.setValue("status", "EXECUTING")
	etlLogSet.save()
	hasErrors = False
	# Esegue gli step di import
	for etlStep in etlSteps:
		if steps2Exec[etlStep] != "0":
			logger.info("Avvio fase di %s per i root strno: %s"%(etlStep, rootStrno))
			# Aggiunge la riga di log per lo step
			etlStepLog = etlLogSet.add()
			etlStepLog.setValue("etlnum", etlnum)
			etlStepLog.setValue("phase", etlStep)
			etlStepLog.setValue("startdate", mxServer.getDate())
			etlStepLog.setValue("status", "EXECUTING")
			etlStepLogId = etlStepLog.getUniqueIDValue()
			etlLogSet.save()
			etlStepLog = MXUtils.getMboForSet(etlLogSet, etlStepLogId)
			try:
				# Esegue lo script dello step
				ctx = HashMap()
				ctx.put("stazStrno", rootStrno)
				ctx.put("importId", importId)
				ctx.put("extractionId", extractionId)
				ctx.put("etlnum", etlnum)
				ctx.put("deleteMode", deleteMode)
				service.invokeScript("X3ETLSTZ_%s"%etlStep, ctx)
				execTime = ctx.get("execTime")
				execTimes[etlStep] = execTime / 1000.0
				etlStepLog.setValue("status", "COMPLETED")
				etlStepLog.setValue("elapsed", execTime)
				etlStepLog.setValue("elapsedaln", DateUtils.format(execTime))
			except Exception, e:
				# Si è verificata un'eccezione, setta l'errore e lo stato a FAILED, poi interrompe l'import
				hasErrors = True
				logger.exception(e)
				etlStepLog.setValue("status", "FAILED")
				etlStepLog.setValue("error", StringUtils.getStackTraceAsString(e))
				break
			finally:
				etlStepLog.setValue("finishdate", mxServer.getDate())
				etlLogSet.save()
	etlLog = MXUtils.getMboForSet(etlLogSet, etlLogId)
	elapsed = System.currentTimeMillis() - start
	etlLog.setValue("finishdate", mxServer.getDate())
	etlLog.setValue("status", ("FAILED" if hasErrors else "COMPLETED")) 
	etlLog.setValue("elapsed", elapsed)
	etlLog.setValue("elapsedaln", DateUtils.format(elapsed))
	etlLogSet.save()
	etlLogSet.close()
	if not fromCron:
		if hasErrors:
			responseBody = '{"rc": 1, "errormsg": "Si sono verificati errori durante l\'import, verificare i log"}'
		else:
			responseBody = '{"exectime_datatransfer": %f, "exectime_preimport": %f, "exectime_import": %f, "total_time": %f}'%(execTimes["DATATRANSFER"], execTimes["PREIMPORT"], execTimes["IMPORT"], execTimes["DATATRANSFER"] + execTimes["PREIMPORT"] + execTimes["IMPORT"])
else:
	etlLogSet.close()
	# Ritorna un messaggio d'errore
	if fromCron:
		service.error("x3terna", "ErrorMsg", [errorMsg.decode("UTF-8")])
	responseBody = '{"rc": 1, "errormsg": "%s"}'%errorMsg.decode("UTF-8")