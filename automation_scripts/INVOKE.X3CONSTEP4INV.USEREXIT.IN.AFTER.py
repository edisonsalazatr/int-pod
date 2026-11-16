# INVOKE.X3CONSTEP4INV.USEREXIT.IN.AFTER
# Processa le risposte del servizio SAP per le inversioni delle linee
# Aggiorna il record della SAP Asset in fase di sincronizzazione
# In caso di OK:
#	Decrementa il counter per le statiscihe di network time dello Step 4
#	Per tutte le ST figlie della linea coinvolte nell'inversione (INVERSIONFLAG = 1)
#		resetta INVERSIONFLAG a 0
#		svuota OLDSTRNO
# In caso di KO: aggiunge l'errore e setta a 1 errorflag
# v1.0 - 31/07/2023
# v1.1 - 12/06/2024 - rimosso sleep prima delle updates
# v1.2 - 05/05/2025 - svuota l'errore e setta a 0 errorflag in caso di OK
from com.terna.common.log import Logger
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
	# Resetta a 0 INVERSIONFLAG e svuota OLDSTRNO del primo livello
	sapAsset.setValueNull("oldstrno")
	sapAsset.setValue("inversionflag", False)
	sapAsset.setValueNull("error")
	sapAsset.setValue("errorflag", False)
	# Carica le ST figlie della linea coinvolte nell'inversione
	sapAssetChildSet = sapAsset.getMboSet("$CHILDINVSAPASSET", "X3SAPASSET", "rootstrno = :strno and inversionflag = 1 and strno != :strno")
	# Resetta a 0 INVERSIONFLAG e svuota OLDSTRNO
	sapAssetChild = sapAssetChildSet.moveFirst()
	while sapAssetChild != None:
		sapAssetChild.setValueNull("oldstrno")
		sapAssetChild.setValue("inversionflag", False)
		sapAssetChild = sapAssetChildSet.moveNext()
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
	
# Salta il resto della transaction
service.raiseSkipTransaction()