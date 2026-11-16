"""
X3ETLSTZ_PLANNER
ETL SAP - Script richiamabile come API per pianificare l'import di una o più stazioni
Ricava l'elenco delle stazioni esportate da SAP con l'extraction id passato come parametro,
le ordina per numerosità di oggetti (Sedi Tecniche + Equipment) e crea le righe nella X3ETLSTZLOG per pianificare gli import
cercando di bilanciare il numero di oggetti per ogni import.
v1.0 23/04/2024
v1.1 25/07/2025
	aggiunta gestione delete mode, skip delete e mode
Parametri:
	extractionid (obbligatorio): id univoco dell'estrazione SAP, usato per ricavare le stazioni da importare
	maxstations (obbligatorio): numero massimo di stazioni per singolo import
	deleteMode: indica se l'ETL deve essere eseguito in modalità cancellazione, ovvero verranno ripuliti i dati senza ulteriori caricamenti (default: non attivo)
	skipDelete: indica se saltare la fase di delete (default: non attivo)
	mode (obbligatorio): modalità di import, può essere "FULL" (importa l'intera stazione) o "DISMESSI" (importa solo asset dismessi)
"""
from com.terna.common.log import Logger
from com.terna.common.maximo import MXUtils
from psdi.server import MXServer
from java.text import SimpleDateFormat
from java.lang import System

# Carica il logger
logger = Logger.getLogger("ETLSTZ.X3ETLSTZ_PLANNER")

# Recupera i parametri
extractionId = request.getQueryParam("extractionid")
maxStations = request.getQueryParam("maxstations")
# CR: Asset_Dismessi_20250722
deleteMode = request.getQueryParam("deletemode") or "0"
skipDelete = request.getQueryParam("skipdelete") or "0"
mode = request.getQueryParam("mode") # Modalità di import (FULL o DISMESSI)
# ------------------------------

MAXSTATIONSLIMIT = 15

# Verifica i parametri obbligatori
errorMsg = None
if extractionId == None:
	errorMsg = "Il parametro extractionid è obbligatorio"
elif maxStations == None:
	errorMsg = "Il parametro maxstations è obbligatorio"
# CR: Asset_Dismessi_20250722
elif deleteMode not in ["0", "1"]:
	errorMsg = "Il parametro deletemode deve essere '0' (non attivo) o '1' (attivo)"
elif skipDelete not in ["0", "1"]:
	errorMsg = "Il parametro skipdelete deve essere '0' (non attivo) o '1' (attivo)"
elif mode == None or mode not in ["FULL", "DISMESSI"]:
	errorMsg = "Il parametro mode è obbligatorio e deve essere 'FULL' o 'DISMESSI'"
# ------------------------------
else:
	try:
		maxStations = int(maxStations)
		if maxStations > MAXSTATIONSLIMIT:
			errorMsg = "Numero massimo di stazioni per import: %s"%MAXSTATIONSLIMIT
	except:
		errorMsg = "Il parametro maxstations deve essere un numero"

if errorMsg == None:
	logger.debug("Start - Planning stations import")
	start = System.currentTimeMillis()
	etlLogSet = MXUtils.getMboSet("X3ETLSTZLOG") # Mbo Set per creare le righe di pianificazione
	utils = service.invokeScript("X3ETLSTZ_UTILS").get("X3ETLSTZUTILS")(None, None) # Utilities
	# Genera l'import id
	sdf = SimpleDateFormat("yyyyMMddHHmmss")
	now = MXServer.getMXServer().getDate()
	importId = sdf.format(now)
	try:
		# CR: Asset_Dismessi_20250722
		if mode == "FULL":
			# Recupera le stazioni da importare dal DB On Premise
			selectSql = """SELECT ROOTSTRNO, COUNT(*) TOTOBJ FROM (
							SELECT substr(STRNO,1,6) ROOTSTRNO FROM X3SEDITECHSAP_STZ_IF@ONPREMISE
							WHERE EXTRACTION_ID = '%s'
							UNION ALL
							SELECT substr(TPLNR,1,6) ROOTSTRNO FROM X3EQUIPMENTSAP_STZ_IF@ONPREMISE
							WHERE EXTRACTION_ID = '%s'
						) GROUP BY ROOTSTRNO
						ORDER BY TOTOBJ DESC"""%(extractionId, extractionId)
		
		elif mode == "DISMESSI":
			# Recupera le stazioni per il caricamento degli asset dismessi
			selectSql = """SELECT substr(TPLNR,1,6) ROOTSTRNO, count(*) TOTOBJ FROM X3EQUIPMENTSAP_STZ
							WHERE EXTRACTION_ID = '%s'
							GROUP BY substr(TPLNR,1,6)
							ORDER BY TOTOBJ DESC"""%(extractionId)
		# ------------------------------
	
		stationsToImport = []
		rs = utils.executeQuery("SELECT", "Recupero Stazioni da importare", selectSql)
		while rs.next():
			strno = rs.getString("rootstrno")
			totObj = rs.getInt("totobj")
			stationsToImport.append((strno, totObj))
		totStationsToImport = len(stationsToImport)
		while len(stationsToImport) > 0:
			strnoSingleImport = []
			totObjSingleImport = 0
			count = 0
			firstEl = True
			# Prepara gli strno per singolo import
			while len(stationsToImport) > 0 and count < maxStations:
				if firstEl:
					# Prende la stazione col maggior numero di oggetti
					station = stationsToImport.pop(0)
				else:
					# Prende la stazione col minor numero di oggetti
					station = stationsToImport.pop()
				strnoSingleImport.append(station[0])
				totObjSingleImport += station[1]
				firstEl = not firstEl
				count += 1
			# Crea la riga di pianificazione
			etlLog = etlLogSet.add()
			etlLog.getMboValue("etlnum").autoKey()
			etlLog.setValue("phase", "ETL")
			etlLog.setValue("status", "WSTART")
			etlLog.setValue("numrecords", totObjSingleImport)
			etlLog.setValue("importid", importId)
			etlLog.setValue("extractionid", extractionId)
			stationsStr = ",".join(strnoSingleImport)
			etlLog.setValue("rootstrno", stationsStr)
			# CR: Asset_Dismessi_20250722
			etlLog.setValue("deletemode", deleteMode)
			etlLog.setValue("skipdelete", skipDelete)
			etlLog.setValue("etlmode", mode)
			# ------------------------------
			logger.info('Generated import plan for stations "%s" with %s objects (ST + EQ)'%(stationsStr, totObjSingleImport))
		etlLogSet.save()
		execTime = (System.currentTimeMillis() - start) / 1000.0
		responseBody = '{"rc": 0, "msg": "Generato piano di import per %s stazioni", "total_time": %f}'%(totStationsToImport, execTime)
		logger.debug("End - Planning stations import")
	finally:
		etlLogSet.close()
		utils.close()
else:
	# Ritorna un messaggio d'errore
	responseBody = '{"rc": 1, "errormsg": "%s"}'%errorMsg.decode("UTF-8")