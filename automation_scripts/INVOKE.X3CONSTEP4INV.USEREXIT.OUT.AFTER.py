# INVOKE.X3CONSTEP4INV.USEREXIT.OUT.AFTER
# Determina l'ordine delle rename e costruisce la chiamata per il servizio di inversione linee di SAP
# v1.0 - 20/10/2023
# v1.1 - 12/06/2024 - Le rinomine dei terminali vengono inviate per ultime su richiesta di SAP
# v1.2 - 21/05/2025 - I terminali non sono più soggetti a rinomina, hanno tutti la lettera H
from psdi.iface.mic import IntegrationContext
from psdi.iface.util import XMLUtils
from psdi.mbo import MboConstants
from com.terna.common.log import Logger
from com.terna.common.string import StringUtils
from java.lang import String
from org.jdom2 import Namespace

log = Logger.getLogger("INTEGRATION.STEP4")

"""
Crea strno temporanei partendo dalla prima parte di quello della ST
e aggiungendo la stringa TEMPx dove x è un numero che parte da 1
e viene incrementato fino a trovare un strno che non è presente tra gli old
o non è già stato usato per altre rinomine temporanee
"""
def createTempStrno(strno):
	fixed = strno[:11]
	i = 1
	tempStrno = fixed + "TEMP" + str(i)
	while tempStrno in o2n or tempStrno in tempStrnos:
		i += 1
		tempStrno = fixed + "TEMP" + str(i)
	tempStrnos.append(tempStrno)
	return tempStrno

"""
Verifica le informazioni obbligatorie per rinomine e spostamenti
Comuni:
	SAPUID (Tplnr)
Solo rinomine:
	OLDSTRNO
Solo spostamenti:
	SAPPARENTUID (Tplma)
lanciando un'eccezione quando sono vuoti
"""
def checkRequiredFields(invSapAsset, fieldsToCheck):
	for field in fieldsToCheck:
		if invSapAsset.isNull(field):
			service.error("x3terna", StringUtils.format("{0} is required for {1} '{2}'", field, sapClass, invSapAsset.getString("strno")))

# Strutture per il calcolo delle operazioni da inviare a SAP
o2n = {} # Mappa da vecchio a nuovo strno più i tplnr
n2o = {} # Mappa da nuovo a vecchio strno
temp = [] # Rinomine temporanee
tempDict = {} # Conversione da vecchio a temporaneo
tempStrnos = [] # Strno per rinomine temporanee
renames = [] # Rinomine finali
moves = [] # Spostamenti

## Carica le ST della linea coinvolte nell'inversione
cntx = IntegrationContext.getCurrentContext()
lineSapAsset = cntx.getProperty("sourceobject")
invSapAssetSet = lineSapAsset.getMboSet("$INVSAPASSET", "X3SAPASSET", "rootstrno = :strno and strno != :strno and inversionflag = 1")
invSapAssetSet.setFlag(MboConstants.DISCARDABLE, True)

log.debug(StringUtils.format("Creating SAP request for inversion of line '{0}'", lineSapAsset.getString("strno")))

# Converte l'mbo set nelle strutture per il calcolo delle operazioni da mandare a SAP
invSapAsset = invSapAssetSet.moveFirst()
while invSapAsset != None:
	sapClass = invSapAsset.getString("sapclass")
	if sapClass in ["CAMPATA", "TRATTA CAVO"]:
		# Verifica i dati obbligatori per la rinomina
		checkRequiredFields(invSapAsset, ["OLDSTRNO", "SAPUID"])
		# Se la ST è una campata o una tratta cavo viene aggiunta alle strutture o2n e n2o
		o2n[invSapAsset.getString("oldstrno")] = [invSapAsset.getString("strno"), invSapAsset.getString("sapuid")]
		n2o[invSapAsset.getString("strno")] = invSapAsset.getString("oldstrno")
	elif sapClass in ["SOSTEGNO", "TERMINALE", "CAMERA GIUNTI"]:
		# Verifica i dati obbligatori per lo spostamento
		checkRequiredFields(invSapAsset, ["SAPUID", "SAPPARENTUID"])
		# Se la ST è un sostegno, un terminale o una camera giunti viene aggiunta alla struttura moves
		moves.append([invSapAsset.getString("sapuid"), invSapAsset.getString("sapparentuid")])
	invSapAsset = invSapAssetSet.moveNext()
	
## Calcola le operazioni da mandare a SAP
# Inizia da una qualsiasi ST presa da o2n
while len(o2n) > 0:
	currentItem = o2n.popitem()
	currentOld = currentItem[0]
	currentNew = currentItem[1][0]
	currentTplnr = currentItem[1][1]
	if currentNew in o2n:
		# Il nuovo strno è in uso, crea rinomina temporanea
		newTemp = createTempStrno(currentNew)
		newItem = o2n[currentNew]
		temp.append([newItem[1], currentNew, newTemp])
		tempDict[currentNew] = newTemp
	while currentItem != None:
		actualOld = currentOld
		if actualOld in tempDict:
			actualOld = tempDict[actualOld]
		renames.append([currentTplnr, actualOld, currentNew])
		n2o.pop(currentNew)
		if currentOld in n2o:
			# E' possibile continuare le rinomine senza sovrapposizioni
			currentOld = n2o[currentOld]
			currentItem = o2n.pop(currentOld)
			currentNew = currentItem[0]
			currentTplnr = currentItem[1]
		else:
			# Sono finite le rinomine o serve una temporanea
			currentItem = None

## Costruisce la request per SAP
# Ripulisce la struttura preparata da Maximo
erData.breakData()
erData.moveToFirstObjectStruture()
erData.setParentAsCurrent()
erData.removeCurrentData()
erData.getCurrentData().setAttributes(None)
erData.setCurrentNamespace("", "")
erData.getCurrentData().setNamespace(Namespace.getNamespace("urn", "urn:sap-com:document:sap:soap:functions:mc-style"))
erData.getCurrentData().removeNamespaceDeclaration(Namespace.getNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance"))

# Rinomina la root
erData.getCurrentData().setName("Zfmpms4InversionManage")

# Aggiunge la parte comune
erData.createChildrenData("IInput",True)
erData.createChildrenData("item",True)
erData.createChildrenData("Inversione",True)

# Aggiunge la parte relativa agli spostamenti
erData.createChildrenData("Moves",True)
for i in range(len(moves)):
	erData.createChildrenData("item",True)
	erData.createChildrenData("Move",True)
	erData.setCurrentData("Tplnr", moves[i][0])
	erData.setCurrentData("Tplma", moves[i][1])
	erData.setParentAsCurrent()
	erData.setParentAsCurrent()

# Aggiunge la parte relativa alle rinomine
renames = temp + renames
erData.setParentAsCurrent()
erData.createChildrenData("Renames",True)
for i in range(len(renames)):
	erData.createChildrenData("item",True)
	erData.createChildrenData("Rename",True)
	erData.setCurrentData("Tplnr", renames[i][0])
	erData.setCurrentData("VecchioStrno", renames[i][1])
	erData.setCurrentData("NuovoStrno", renames[i][2])
	erData.setParentAsCurrent()
	erData.setParentAsCurrent()

log.debug("SAP Request: " + erData.getDataAsString())

# Aggiorna il record di log
step4Log = lineSapAsset.getMboSet("STEP4LOGNEW").getMbo(0)
if step4Log != None:
	step4Log.setValue("request", String(XMLUtils.prettyPrintXML(erData.getDataAsBytes()), "UTF-8"))

# Incrementa il contatore per la statistica network time dello Step 4
sapReqCounterStats = cntx.getProperty("sapReqCounterStats")
sapReqCounterStats.increment()