# INVOKE.X3CONSTEP4EQ.USEREXIT.IN.AFTER
# Processa le risposte del servizio SAP per la gestione delle Sedi Tecniche
# Aggiorna il record della SAP Asset in fase di sincronizzazione
# In caso di OK:
#	Decrementa il counter per le statiscihe di network time dello Step 4
#	Svuota l'errore e setta a 0 errorflag
#	Se SYNCHACTION è ADD:
#		modifica la synchaction in UPDATE
#		popola il SAPUID con l'EQUNR ritornato da SAP
#	Se SYNCHACTION è UPDATE o DELETE: rimuove la synchaction e svuota OLDSTRNO
#		in più se è DELETE setta a 1 historyflag
# In caso di KO: aggiunge l'errore e setta a 1 errorflag
# v1.0 - 04/03/2024
# v1.1 - 31/03/2025
#	Resetta l'historyflag a 0 quando vanno a buon fine chiamate non di tipo DELETE
from com.terna.common.log import Logger
from com.terna.common.string import StringUtils
from psdi.iface.mic import IntegrationContext
from psdi.server import MXServer

log = Logger.getLogger("INTEGRATION.STEP4")
# Logga la risposta
log.debug("SAP Response: " + irData.getDataAsString())
# Recupera il record della SAP Asset
cntx = IntegrationContext.getCurrentContext()
sapAsset = cntx.getProperty("targetobject")
# Decrementa il contatore per la statistica network time dello Step 4
sapReqCounterStats = cntx.getProperty("sapReqCounterStats")
sapReqCounterStats.decrement()
# Carica l'xml Document della risposta
data = irData.getData()
root = data.getRootElement()
# Carica il tipo di risposta (OK o KO)
resultEl = root.getChild("EResult")
if resultEl == None:
	service.error("x3terna", "ErrorMsg", ["Unknown response"])
resultType = resultEl.getValue()
log.info("Result type: " + resultType)
# Aggiorna i campi comuni del record di log
step4Log = sapAsset.getMboSet("STEP4LOGNEW").getMbo(0)
if step4Log != None:
	step4Log.setValue("response", irData.getDataAsString())
	step4Log.setValue("endtime", MXServer.getMXServer().getDate())
if resultType == "OK":
	# Aggiorna l'esito nel record di log
	if step4Log != None:
		step4Log.setValue("result", "OK")
	# La chiamata è andata a buon fine, applica un'azione in base alla SYNCHACTION
	synchAction = sapAsset.getString("synchaction")
	if synchAction == "ADD":
		# Modifica la SYNCHACTION in UPDATE
		sapAsset.setValue("synchaction", "UPDATE")
		# Popola il SAPUID
		equnr = root.getChildText("EEqunr")
		log.info("Setting SAPUID: " + equnr)
		sapAsset.setValue("sapuid", equnr)
	else:
		# In caso di UPDATE o DELETE rimuove la SYNCHACTION
		sapAsset.setValueNull("synchaction")
		sapAsset.setValueNull("oldstrno")
		# Se la SYNCHACTION è di tipo DELETE imposta a 1 l'historyflag, altrimenti lo resetta a 0
		if synchAction == "DELETE":
			sapAsset.setValue("historyflag", True)
		else:
			sapAsset.setValue("historyflag", False)
	# Svuota error e setta a 0 errorflag
	sapAsset.setValueNull("error")
	sapAsset.setValue("errorflag", False)
elif resultType == "KO":
	# Aggiorna l'esito nel record di log
	if step4Log != None:
		step4Log.setValue("result", "FAILED")
	# La chiamata è fallita, costruisce e setta l'errore
	etReturnEl = root.getChild("EtReturn")
	itemList = etReturnEl.getChildren("item")
	errors = ""
	for i in range(len(itemList)):
		itemEl = itemList[i]
		if errors != "":
			errors += "\r\n"
		errors += itemEl.getChildText("Number") + ": " + itemEl.getChildText("Message")
	sapAsset.setValue("error", errors)
	sapAsset.setValue("errorflag", True)
	log.info("KO message: " + errors)
else:
	service.error("x3terna", "ErrorMsg", ["Unknown response"])
	
	
# Salta il resto della transaction
service.raiseSkipTransaction()