Validation SAPID X3tronchi, asset e assetlocrelation",Draft,"'''
Created on 11 lug 2023

- 4.6.1    ID SAP per le linee

La maschera di editing che SAP applica per le linee e che dovrà essere implementata in IBM MAS Manage dovrà adottare le seguenti regole:
•    Livello 1 SAP – NNXXXCN (7 digit)
    o    Digit 1: indica la tipologia di asset, per le linee l’unico valore ammesso “2”;
    o    Digit 2: indica il livello di tensione;
    o    Digit 3÷5: usati per l’identificativo di esercizio della terna (la linea), in caso di lettere solo maiuscole;
    o    Digit 6: carattere alfabetico solo maiuscolo, utilizzato per differenziare quei casi in cui esiste già una linea con i precedenti 5 caratteri uguali a quella che deve essere creata;
    o    Digit 7: carattere numerico utilizzato per indicare un differente titolare o DT (distretto o Dipartimento territoriale) per ciascuna linea. In pratica, per ogni DT o titolare viene creato un nuovo primo livello, con i precedenti 6 caratteri identici;
•    Livello 2 SAP - NNNXXXXX (8 digit)
    o    Digit 1÷3: caratteri numerici;
    o    Digit 4÷8: caratteri alfanumerici, facoltativi;
    o    In ogni caso ciascuno degli 8 digit del 2° livello, se non imputati dall’utente, verranno popolati con il trattino (-);
•    Livello 3 SAP - XN (2 digit)
    o    Digit 1: carattere alfabetico solo maiuscolo, indica il tipo di oggetto. In creazione da IBM MAS Manage dovranno essere gestiti solo i seguenti: 
            S (sostegno);
            E (estremo cavo);
            T (tratta cavo);
    o    Digit 2: possibilità facoltativa, mai impiegata in SAP

@version: v1 - initial coding


'''

from psdi.server import MXServer
from psdi.util.logging import MXLoggerFactory
from java.util.regex import Pattern

logger = MXLoggerFactory.getLogger("maximo.script")

REGEX_VALIDATIONS = { 
                        "LINEA": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]$", "ERRMSG": "x3err_format_liv1"},
                        "X3TRONCHI": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-T[0-9]{2}$", "ERRMSG": "x3error_tronchi"},#NON HA CLASSIFICAZIONE VIENE GESTITA CON TABELLA X3TRONCHI
                        #"X3TRONCHI": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-}$", "ERRMSG": "x3error_tronchi_fr"},
                        "CAMPATA AEREA": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}$", "ERRMSG": "x3err_format_liv2"},
                        "CONDUTTORI": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-C$", "ERRMSG": "x3err_format_liv3"},
                        "FUNI DI GUARDIA": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-G$", "ERRMSG": "x3err_format_liv3"},
                        "ISOLATORE CONDUTTORE": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-I$", "ERRMSG": "x3err_format_liv3"},
                        "ISOLATORE FUNE DI GUARDIA": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-I$", "ERRMSG": "x3err_format_liv3"},
                        "????MORSETTERIA": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-M$", "ERRMSG": "x3err_format_liv3"},
                        "SOSTEGNO AEREO": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-S$", "ERRMSG": "x3err_format_liv3"},
                        "TRATTA CAVO": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}$", "ERRMSG": "x3err_format_liv2"},
                        "TERMINALE": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-[HK]$", "ERRMSG": "x3err_format_liv3"},
                        "CAMERA ISPEZIONABILE": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-R$", "ERRMSG": "x3err_format_liv3"},
                        "CAVI": {"REGEX": "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]-[0-9]{3}[0-9|A-Z|-]{5}-V$", "ERRMSG": "x3err_format_liv3"}
                        
                    }


def getMboSet(name):
    return MXServer.getMXServer().getMboSet(name, MXServer.getMXServer().getSystemUserInfo())


if launchPoint == "X3TRONCHI":
    #X3TRONCHI
    if not Pattern.matches(REGEX_VALIDATIONS.get("X3TRONCHI").get("REGEX"), mbo.getString("SAPID")):
        if interactive:
            service.error(REGEX_VALIDATIONS.get("X3TRONCHI").get("ERRMSG"), REGEX_VALIDATIONS.get("X3TRONCHI").get("ERRMSG"), None)
        else:
            raise ValueError('Field X3TRONCHI.SAPID has bad format')
    lineaMboSet=mbo.getMboSet("PARENT")
    if not lineaMboSet.isEmpty() and onadd:
        sapidLinea=lineaMboSet.getMbo(0).getString("X3SAPID")
        sapidTronco=mbo.getString("SAPID")
        if sapidTronco[:7]!=sapidLinea:
            service.error("x3terna","ErrorMsg",["Incongruenza SAP ID livello 1."])
        if not mbo.getMboSet("$TRONCOESISTENTE$","X3TRONCHI","SAPID='"+str(sapidTronco)+"'").isEmpty():
            service.error("x3renamesapid", "x3renamesapid_exists", None)
###verificare classificationid utilizzate nelle regex
elif launchPoint == "X3ASSET" or launchPoint == "X3ASSETLOCRELATION":
    if launchPoint == "X3ASSETLOCRELATION":
        #ASSETLOCRELATION
        assetMboSet = mbo.getMboSet("TARGETASSET")
        assetMbo = assetMboSet.getMbo(0)
        classStructMboSet = assetMbo.getMboSet("CLASSSTRUCTURE")
    else: 
        #ASSET 
        classStructMboSet = mbo.getMboSet("CLASSSTRUCTURE")
        
    classStructMbo = classStructMboSet.getMbo(0)
    classId = classStructMbo.getString("CLASSIFICATIONID")
    rule = REGEX_VALIDATIONS.get(classId)
#####verificared da integrazione    
    if rule is not None:
        if not Pattern.matches(rule.get("REGEX"), mbo.getString("X3SAPID")):
            if not interactive: 
                raise ValueError('Field ' + 'ASSET.X3SAPID' if  launchPoint == "X3ASSET" else 'ASSETLOCRELATION.X3SAPID' + ' has bad format')
            #if interactive: 
               # service.error(rule.get("ERRMSG"), rule.get("ERRMSG"), None)
            #else:
                #raise ValueError('Field ' + 'ASSET.X3SAPID' if  launchPoint == "X3ASSET" else 'ASSETLOCRELATION.X3SAPID' + ' has bad format')
    else:
        logger.error(" - x3sapidvalidation - IMPOSSIBLE TO VALIDATE STRNO FIELD, no rules found")"
X3ASSOCIATELINE,Handles Associate line dialog,Draft,"#--------------------------------------------------------------------------
# Script: X3ASSOCIATELINE
# Launch Point: X3ASSOCIATELINE - action
	# Handles Associate line dialog OK button
#--------------------------------------------------------------------------

from psdi.server import MXServer
from psdi.mbo import MboConstants
from psdi.util.logging import MXLoggerFactory
from psdi.webclient.beans.location import ApplyIASBean 
from psdi.app.inventory.virtual import AssetInput
from psdi.app.asset.virtual import FldChangeItemnumNewItemnum
from psdi.app.asset import AssetRemote
from psdi.mbo import SqlFormat
from com.terna.common.log import Logger
from com.terna.asset import SapIdGenerator
from com.terna.integration.service import IntegrationService
from com.terna.integration.service import Scenario
from com.terna.integration.sapconnector import SapConnectorScenario
from com.terna.integration.sapconnector import SapConnectorScenarioOperation
from com.terna.integration.sapconnector.step1.handler.touch import AssetRelationAddArg
log = Logger.getLogger("SCRIPT.X3ASSOCIATELINE")
log.debug(">>> Entering script X3ASSOCIATELINE")

#---------------------------------------------------------------------
#---------------------------------------------------------------------
def renumSequence(sequence, renumFactor):

	if len(sequence)==6:
		newSeqInt = int(sequence) + renumFactor
		newSeq=str(newSeqInt).zfill(6)
		return newSeq
	elif len(sequence)==7 or len(sequence)==8:
		if sequence[0].isalpha() and sequence[-1].isalpha():
			oldSeqNum=sequence[1:-1]
			newSeqNum=int(oldSeqNum)+renumFactor
			newSeqStr=str(newSeqNum).zfill(6)
			newSeq=sequence[0]+newSeqStr+sequence[-1]
			return newSeq
		elif sequence[0].isalpha():
			oldSeqNum=sequence[1:]
			newSeqNum=int(oldSeqNum)+renumFactor
			newSeqStr=str(newSeqNum).zfill(6)
			newSeq=sequence[0]+newSeqStr
			return newSeq
		elif sequence[0].isalpha():
			oldSeqNum=sequence[1:]
			newSeqNum=int(oldSeqNum)+renumFactor
			newSeqStr=str(newSeqNum).zfill(6)
			newSeq=sequence[0]+newSeqStr
			return newSeq
	elif len(sequence)==9 or len(sequence)==10:
		if sequence[0].isalpha():
			alpha=sequence[0]
			oldSeqNum=sequence[1:7]
			newSeqNum=int(oldSeqNum)+renumFactor
			newSEQstr=str(newSeqNum).zfill(6)
			newSeq=alpha+newSEQstr+sequence[-3:]
			return newSeq
		else:
			oldSeqNum=sequence[0:6]
			newSeqNum=int(oldSeqNum)+renumFactor
			newSEQstr=str(newSeqNum).zfill(6)
			newSeq=newSEQstr+sequence[-3:]
			return newSeq
#-----------------------------------------------------------------------------------------------------
#-----------------------------------------------------------------------------------------------------
def renumPosNr(posNr, renumFactor):
    if len(posNr) == 3:
        # Gestione formato a tre cifre con lettera iniziale, es. "A02"
        if posNr[0].isalpha() and posNr[1:].isdigit():
            alpha = posNr[0]
            numPart = posNr[1:]
            newPosNrInt = int(numPart) + renumFactor
            newPosNr = str(newPosNrInt).zfill(len(numPart))
            return alpha + newPosNr
        # Gestione numerico puro, es. "002"
        elif posNr.isdigit():
            newPosNrInt = int(posNr) + renumFactor
            return str(newPosNrInt).zfill(3)
    
    elif len(posNr) == 4:
        # Gestione formato con lettera iniziale e finale, es. "A02B"
        if posNr[0].isalpha() and posNr[-1].isalpha() and posNr[1:-1].isdigit():
            oldPosNr = posNr[1:-1]
            newPosNrNum = int(oldPosNr) + renumFactor
            newPosNrStr = str(newPosNrNum).zfill(len(oldPosNr))
            return posNr[0] + newPosNrStr + posNr[-1]
        
        # Gestione formato con lettera iniziale, es. "A023"
        elif posNr[0].isalpha() and posNr[1:].isdigit():
            oldPosNr = posNr[1:]
            newPosNrNum = int(oldPosNr) + renumFactor
            newPosNrStr = str(newPosNrNum).zfill(len(oldPosNr))
            return posNr[0] + newPosNrStr
        
        # Gestione formato con lettera finale, es. "023A"
        elif posNr[-1].isalpha() and posNr[:-1].isdigit():
            oldPosNr = posNr[:-1]
            newPosNrNum = int(oldPosNr) + renumFactor
            newPosNrStr = str(newPosNrNum).zfill(len(oldPosNr))
            return newPosNrStr + posNr[-1]
    
    return posNr 
#-----------------------------------------------------------------------------------------------------
#-----------------------------------------------------------------------------------------------------
def setX3AssetLinea(new3LAssetSet):
	try:
		if not new3LAssetSet.isEmpty():
			new3LAsset = new3LAssetSet.moveFirst()
			while(new3LAsset):
				#log.info( " while start " )
				classstrcSet = new3LAsset.getMboSet("CLASSSTRUCTURE")
				if not classstrcSet.isEmpty():
					classstrc = classstrcSet.moveFirst()
					classId3L = classstrc.getString("classificationid")
					if classId3L == "ATTACCHI CONDUTTORE" or classId3L == "CONDUTTORI" or classId3L == "CAVI" or classId3L == "TERMINALI" or classId3L == "GIUNTI":
						#log.info( new3LAsset.getString("x3rootassetnum") )
						if new3LAsset.getString("x3rootassetnum") =="" or  new3LAsset.getString("x3rootassetnum") ==None:
							#log.info( new3LAsset.getString("x3rootassetnum") )
							new3LAsset.setValue("x3rootassetnum", sourceRootAssetNum, MboConstants.NOVALIDATION)
							#new3LAsset.setValue("x3dadoppiaterna", 1)
							IntegrationService.getService().saveNoSync(new3LAssetSet)
						   
					elif classId3L == "FUNI DI GUARDIA":
						assetFdgSet = MXServer.getMXServer().getMboSet("ASSET",userInfo)
						assetFdgSet.setWhere("parent ='"+str(new3LAsset.getString("ASSETNUM"))+"' and classstructureid in(select classstructureid from classstructure where classificationid='FUNE DI GUARDIA')")
						assetFdgSet.reset()
						for n in range (0,assetFdgSet.count()):
							assetFdg=assetFdgSet.getMbo(n).getMboSet("ASSETSPEC")
							assetFdgNew=assetFdg.add()
							assetFdgNew.setValue("SECTION",x3SapiId_linea, MboConstants.NOACCESSCHECK | MboConstants.NOVALIDATION)
							assetFdgNew.setValue("ASSETATTRID","LIN_RIF_FUNE",MboConstants.NOACCESSCHECK |	MboConstants.NOVALIDATION)
							IntegrationService.getService().saveNoSync(assetFdgSet)
						 
				new3LAsset = new3LAssetSet.moveNext()
			IntegrationService.getService().saveNoSync(new3LAssetSet)
					   
	except:
		log.info( " Exception for setX3AssetLinea " )


# #####################################################################
# #####################################################################

mxServer = MXServer.getMXServer()
userInfo = mxServer.getSystemUserInfo()

wcs = service.webclientsession()
app = wcs.getCurrentApp()

# get the 1st level asset 
sourceRootAsset = app.getAppBean().getMbo()
userinfo = sourceRootAsset.getUserInfo()
# get parameters
sourceRootAssetNum = sourceRootAsset.getString("assetnum")
siteId = sourceRootAsset.getString("siteid")
x3SapiId_linea = sourceRootAsset.getString("x3sapid")
sapIdGenarator = SapIdGenerator(sourceRootAsset)
# dialog selected assets
assetDialogSet = app.getDataBean("x3associatelinedialog")
selectedAssets = assetDialogSet.getSelection()
numSelAssets  = selectedAssets.size()
AssetSAP=[]
arrayRiseq=[]
sequence = assetDialogSet.getMbo(0).getString("x3sequencelist")
if sequence=="" or not sequence:
	for l in range (0,assetDialogSet.count()):
		if not assetDialogSet.getMbo(l).isNull("x3sequencelist"):
			sequence = assetDialogSet.getMbo(l).getString("x3sequencelist")
			break
sostegno=assetDialogSet.getMbo(0).getDouble("X3SOSTEGNO")
if sostegno=="" or sostegno!="N" or not sostegno:
	for s in range (0,assetDialogSet.count()):
		if not assetDialogSet.getMbo(s).getBoolean("X3SOSTEGNO")==False:
			sostegno = assetDialogSet.getMbo(s).getDouble("X3SOSTEGNO")
			break


if numSelAssets > 0 and not sostegno:
	creaCampataFittizia = False
	relationshipAssetSet = sourceRootAsset.getMboSet("ASSETLOCRELATION")
	if (not relationshipAssetSet.isEmpty()) and (sequence == "" or sequence == None):
		creaCampataFittizia=True
		sqlf = SqlFormat("siteid = :1 and sourceassetnum = :2 and  x3sequence = '000001'")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "SOURCEASSETNUM", sourceRootAssetNum)
		assetLocRel = sourceRootAsset.getMboSet("$assetloc_add_inizio$", "ASSETLOCRELATION", sqlf.format()).getMbo(0)
		sapIdCampataFittizia = assetLocRel.getString("x3sapid")
	# risequenziazione
	x3Sequence = ""
	x3PosNr= ""
	#x3SequenceRiseq=numSelAssets *2
	if sequence == "" or sequence == None: 
		sequence = "000000"
		#x3SequenceRiseq= x3SequenceRiseq-2
		# voglio aggiungere all'inizio della linea
		sqlf = SqlFormat("siteid = :1 and sourceassetnum = :2  ")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "SOURCEASSETNUM", sourceRootAssetNum)
	else:
		# cerco sui secondi livelli una sequence maggiore di quella indicata
		sqlf = SqlFormat("siteid = :1 and sourceassetnum = :2 and  x3sequence > :3")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "SOURCEASSETNUM", sourceRootAssetNum)
		sqlf.setObject(3, "ASSETLOCRELATION", "X3SEQUENCE", sequence)
	assetLocRelationSet = sourceRootAsset.getMboSet("$assetloc$", "ASSETLOCRELATION", sqlf.format())
	if not assetLocRelationSet.isEmpty():
		assetLocRelation = assetLocRelationSet.moveFirst()
		while (assetLocRelation):
			if creaCampataFittizia:
				x3PosNr = renumPosNr(assetLocRelation.getString("x3posnr"), (numSelAssets * 2)+2)
				x3Sequence = renumSequence(assetLocRelation.getString("x3sequence"), (numSelAssets * 2)+2)
			else:
				x3PosNr = renumPosNr(assetLocRelation.getString("x3posnr"), numSelAssets * 2)
				x3Sequence = renumSequence(assetLocRelation.getString("x3sequence"), numSelAssets * 2)
			assetLocRelation.setValue("x3sequence", x3Sequence)
			assetLocRelation.setValue("x3posnr", x3PosNr)
			arrayRiseq.append(assetLocRelation.getString("targetassetnum"))
			assetLocRelation = assetLocRelationSet.moveNext()
	

	if sequence == "000000":
		#creo sostegno fittizio
		
		# crea asset di secondo livello
		# il primo elemento creato sarà di tipo sostegno
		assetSet = MXServer.getMXServer().getMboSet("ASSET", sourceRootAsset.getUserInfo())
		assetSet.setMXTransaction(sourceRootAsset.getMXTransaction())
		assetSet.setInsertOrg(sourceRootAsset.getString("orgid"))
		assetSet.setInsertSite(sourceRootAsset.getString("siteid"))
		secondLevelAsset = assetSet.add()
		secondLevelAssetNum = secondLevelAsset.getString("assetnum")
		
		# crea relazione tra sostegno/tratta cavo e la linea
		relationshipAsset = relationshipAssetSet.add()
		relationshipAsset.setValue("sourceassetnum", sourceRootAssetNum)
		relationshipAsset.setValue("targetassetnum", secondLevelAssetNum, MboConstants.NOVALIDATION)
		AssetSAP.append(secondLevelAssetNum)
		relationshipAsset.setValue("assetrelationnum", "INCLUDES")
		
		#prendo il maggiore sapid impostato sui secondi livelli - campata / tratta cavo
		sqlf = SqlFormat("siteid = :1 and x3sapid = (select max(x3sapid) from assetlocrelation where sourceassetnum = :2) ")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "SOURCEASSETNUM", sourceRootAssetNum)
		assetLocrelationSet_forSapID = sourceRootAsset.getMboSet("$max_x3sapid_inizio$", "ASSETLOCRELATION", sqlf.format())
		if not assetLocrelationSet_forSapID.isEmpty():
			assetLocrelation = assetLocrelationSet_forSapID.getMbo(0)
			maxX3SapId = assetLocrelation.getString("x3sapid")
			sapId = maxX3SapId[8:]
			newSapId = ""
			for j in range(0,len(sapId)-1):
				if not sapId[j].isdigit():
					break
				else:
					newSapId = str(newSapId)+ sapId[j]
			newSapId = int(newSapId) + 1 
			newX3SapId = x3SapiId_linea + "-" + str(newSapId).zfill(3) + "------S"

			log.debug("maxX3SapId = {0}",maxX3SapId)
			log.debug("newX3SapId = {0}",newX3SapId)

		else:
			newX3SapId = sourceRootAsset.getString("x3sapid") + "-" + str(1).zfill(3) + "------S"		 
	
		newSapId=sapIdGenarator.next()
		secondLevelAsset.setValue("itemnum", "SOSTEGNO-S")
		secondLevelAsset.setValue("description", secondLevelAsset.getMboSet("ITEM").getMbo(0).getString("description"))
	   
		#set x3sapid
		relationshipAsset.setValue("x3sapid", newSapId+"-S")
		relationshipAsset.setValue("x3sequence", "000001")
		relationshipAsset.setValue("x3posnr", "000")
		
		#relationshipAssetSet.save()
		# the TOPITEMSTRUCT is oddly set to 1=1 therefore the set must be 
		# correctly filtered as done in the ApplyIASBean.initialize() method	
		itemStructSet = secondLevelAsset.getMboSet("TOPITEMSTRUCT")
		itemStructSet.setQbe("itemnum", secondLevelAsset.getString("itemnum"))
		itemStructSet.setQbe("itemsetid", secondLevelAsset.getString("itemsetid"))
		itemStructSet.setQbe("itemid", secondLevelAsset.getString("itemnum"))
		itemStructSet.setQbeExactMatch(True)
		itemStructSet.moveTo(0)
		itemStructSet.clearItemStructsVector()
	
		# apply IAS
		secondLevelAsset.applyIASAutoNumAll()
		IntegrationService.getService().saveNoSync(assetSet)
				 
		# set x3assetlinea
		setX3AssetLinea(secondLevelAsset.getMboSet("ASSETCHILDREN"))
		IntegrationService.getService().saveNoSync(relationshipAsset)
						  
		#assetSet.save()
		assetSet.close()
  
	
	#imposto generatore sapid
	#sapIdGenarator = SapIdGenerator(sourceRootAsset)
		
	for i in range (numSelAssets):
		selAssetlocrelation = selectedAssets.get(i)
		
		targetAssetNum = selAssetlocrelation.getString("targetassetnum")
		secondLevelAssetMbo = selAssetlocrelation.getMboSet("TARGETASSET").getMbo(0)
		x3parentItem=""
		itemSetID=""
		#change itemnum
		itemSet = secondLevelAssetMbo.getMboSet("ITEM")
		if not itemSet.isEmpty():
			item = itemSet.getMbo(0)
			x3parentItem = item.getString("x3parentitem")
			itemSetID = item.getString("itemsetid")
		else:
			classstructureId=secondLevelAssetMbo.getString("CLASSSTRUCTUREID")
			itemSet=secondLevelAssetMbo.getMboSet("$PARENTITEM$","ITEM","classstructureid='"+str(classstructureId)+"' and itemnum like '%-S%'")
			if not itemSet.isEmpty():
				item = itemSet.getMbo(0)
				x3parentItem = item.getString("x3parentitem")
				itemSetID = item.getString("itemsetid")
		itemnum = secondLevelAssetMbo.getString("itemnum")
		
		
		
		#posnr
		sqlf = SqlFormat("siteid = :1 and sourceassetnum = :2 and x3sequence = :3")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "SOURCEASSETNUM", sourceRootAssetNum)
		sqlf.setObject(3, "ASSETLOCRELATION", "X3SEQUENCE", sequence)
		assetLocSet = sourceRootAsset.getMboSet("$asset_loc$", "ASSETLOCRELATION", sqlf.format())
		if not assetLocSet.isEmpty():
			assetLoc = assetLocSet.getMbo(0)
			posNr = assetLoc.getString("x3posnr")
		else:
			#posNr = "000"
			posNr = "002"
		
		
		

		# create relationship
		relationshipAsset = relationshipAssetSet.add()
		AssetSAP.append(targetAssetNum)
		relationshipAsset.setValue("sourceassetnum", sourceRootAssetNum)
		relationshipAsset.setValue("targetassetnum", targetAssetNum, MboConstants.NOVALIDATION)
		relationshipAsset.setValue("assetrelationnum", "INCLUDES")
		
		#X3SAPID
		#prendo il maggiore sapid impostato sui secondi livelli - campata / tratta cavo
		sqlf = SqlFormat("siteid = :1 and x3sapid = (select max(x3sapid) from assetlocrelation where sourceassetnum = :2) ")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "SOURCEASSETNUM", sourceRootAssetNum)
		assetLocrelationSet_forSapID = sourceRootAsset.getMboSet("$max_x3sapid$", "ASSETLOCRELATION", sqlf.format())
		if not assetLocrelationSet_forSapID.isEmpty():
			assetLocrelation = assetLocrelationSet_forSapID.getMbo(0)
			maxX3SapId = assetLocrelation.getString("x3sapid")
			sapId = maxX3SapId[8:]
			newSapId = ""
			for j in range(0,len(sapId)-1):
				if not sapId[j].isdigit():
					break
				else:
					newSapId = str(newSapId)+ sapId[j]
			newSapId = int(newSapId) + 1 + i
			newX3SapId = x3SapiId_linea + "-" + str(newSapId).zfill(3) + "-----"

			log.debug("maxX3SapId = {0}",maxX3SapId)
			log.debug("newX3SapId = {0}",newX3SapId)

		else:
			newX3SapId = sourceRootAsset.getString("x3sapid") + "-" + str(i+1).zfill(3) + "-----"		 
		
		if sequence == "000000":
			renumFactorPorNr = (i*2)
			renumFactorSequence = (i*2)+2
  
		else:
			renumFactorPorNr = (i*2)+2
			renumFactorSequence = (i*2)+1	
		
		newSapId = sapIdGenarator.next()	
		
		#set x3sapid
		relationshipAsset.setValue("x3sapid", newSapId)
		#set x3sequence
		newSequence = renumSequence(sequence, renumFactorSequence)
		relationshipAsset.setValue("x3sequence", newSequence)
		#set x3posNr
		newPosNr = renumPosNr(posNr, renumFactorPorNr)
		relationshipAsset.setValue("x3posnr", newPosNr)
		
		#service.error(posNr,"")
		
		log.debug("x3parentItem = {0}" , x3parentItem)
			
		IntegrationService.getService().saveNoSync(relationshipAsset)
						  
		IntegrationService.getService().saveNoSync(relationshipAssetSet)
							  


		log.debug( "apply IAS" )
		# apply IAS
		sqlf = SqlFormat("siteid = :1 and assetnum = :2 ")
		sqlf.setObject(1, "ASSET", "SITEID", siteId)
		sqlf.setObject(2, "ASSET", "ASSETNUM", targetAssetNum)
		secondLevelAsset = sourceRootAsset.getMboSet("$secondLevelAsset$", "ASSET", sqlf.format()).getMbo(0)

		if	secondLevelAsset:

			# setup item structure for the asset
			# the TOPITEMSTRUCT is oddly set to 1=1 therefore the set must be 
			# correctly filtered as done in the ApplyIASBean.initialize() method	
			itemStructSet = secondLevelAsset.getMboSet("TOPITEMSTRUCT")
			itemStructSet.setQbe("itemnum", x3parentItem)
			itemStructSet.setQbe("itemsetid", secondLevelAsset.getString("itemsetid"))
			itemStructSet.setQbe("itemid", x3parentItem)
			itemStructSet.setQbeExactMatch(True)
			itemStructSet.moveTo(0)
			itemStructSet.clearItemStructsVector()
			
			# apply IAS
			secondLevelAsset.applyIASAutoNumAll()
			IntegrationService.getService().saveNoSync(secondLevelAsset.getThisMboSet())
											  
			log.debug( "IAS applicata")
			
			#new3lassetSet = secondLevelAsset.getMboSet("ASSETCHILDREN")
			#set x3assetlinea
			IntegrationService.getService().saveNoSync(secondLevelAsset)
						  
			setX3AssetLinea(secondLevelAsset.getMboSet("ASSETCHILDREN"))
		
	
		sqlf = SqlFormat("siteid = :1 and targetassetnum = :2 and sourceassetnum = :3 ")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "TARGETASSETNUM", targetAssetNum)
		sqlf.setObject(3, "ASSETLOCRELATION", "SOURCEASSETNUM", selAssetlocrelation.getString("sourceassetnum"))
		targetCampataLoc = sourceRootAsset.getMboSet("$target_campata_loc$", "ASSETLOCRELATION", sqlf.format()).getMbo(0)
		targetSostegnoRelation = sourceRootAsset.getMboSet("$target_sostegno$", "ASSETLOCRELATION", "sourceassetnum='"+str(selAssetlocrelation.getString("sourceassetnum"))+"' and x3sapid = '" + targetCampataLoc.getString("x3sapid") + "-S'").getMbo(0)
		#service.error("TEST","whereclause su assetloc: x3sapid='" + targetCampataLoc.getString("x3sapid") + "-S'")
		targetSostegno = targetSostegnoRelation.getMboSet("TARGETASSET").getMbo(0)
		
		targetSostegnoAssetNum = targetSostegno.getString("assetnum")
	
	
		if sequence == "000000":
			renumSequenceFactor = (i*2)+3
		else:
			renumSequenceFactor = (i*2)+2	
	
			
		#creo relazione tra linea a sostegno 
		relationshipAssetSostegno = relationshipAssetSet.add()
		relationshipAssetSostegno.setValue("sourceassetnum", sourceRootAssetNum)
		AssetSAP.append(targetSostegnoAssetNum)
		relationshipAssetSostegno.setValue("targetassetnum", targetSostegnoAssetNum, MboConstants.NOVALIDATION)
		relationshipAssetSostegno.setValue("assetrelationnum", "INCLUDES")
		relationshipAssetSostegno.setValue("x3sapid", newSapId + "-S")
		relationshipAssetSostegno.setValue("x3sequence", renumSequence(sequence, renumSequenceFactor))
		relationshipAssetSostegno.setValue("x3posnr", newPosNr)
		IntegrationService.getService().saveNoSync(relationshipAssetSostegno)
								  
	
		
		
		# apply IAS
		sqlf = SqlFormat("siteid = :1 and assetnum = :2 ")
		sqlf.setObject(1, "ASSET", "SITEID", siteId)
		sqlf.setObject(2, "ASSET", "ASSETNUM", targetSostegnoAssetNum)
		secondLevelAssetSostegno = sourceRootAsset.getMboSet("$secondLevelAssetSostegno$", "ASSET", sqlf.format()).getMbo(0)
				
		if	secondLevelAssetSostegno:
		
			x3parentItem=""
			itemSetID=""
			#change itemnum
			itemSet = secondLevelAssetSostegno.getMboSet("ITEM")
			if not itemSet.isEmpty():
				item = itemSet.getMbo(0)
				x3parentItem = item.getString("x3parentitem")
				itemSetID = item.getString("itemsetid")
			else:
				classstructureId=secondLevelAssetSostegno.getString("CLASSSTRUCTUREID")
				itemSet=secondLevelAssetSostegno.getMboSet("$PARENTITEM$","ITEM","classstructureid='"+str(classstructureId)+"' and itemnum like '%-S%'")
				if not itemSet.isEmpty():
					item = itemSet.getMbo(0)
					x3parentItem = item.getString("x3parentitem")
					itemSetID = item.getString("itemsetid")
			itemnum = secondLevelAssetSostegno.getString("itemnum")

			# setup item structure for the asset
			# the TOPITEMSTRUCT is oddly set to 1=1 therefore the set must be 
			# correctly filtered as done in the ApplyIASBean.initialize() method	
			itemStructSet = secondLevelAssetSostegno.getMboSet("TOPITEMSTRUCT")
			itemStructSet.setQbe("itemnum", x3parentItem)
			itemStructSet.setQbe("itemsetid", secondLevelAssetSostegno.getString("itemsetid"))
			itemStructSet.setQbe("itemid", x3parentItem)
			itemStructSet.setQbeExactMatch(True)
			itemStructSet.moveTo(0)
			itemStructSet.clearItemStructsVector()
			
			# apply IAS
			secondLevelAssetSostegno.applyIASAutoNumAll()
																																								 
			IntegrationService.getService().saveNoSync(secondLevelAssetSostegno.getThisMboSet())
			log.debug( "IAS applicata")
			IntegrationService.getService().saveNoSync(secondLevelAssetSostegno)
								  
			#set x3assetlinea
			setX3AssetLinea(secondLevelAssetSostegno.getMboSet("ASSETCHILDREN"))
			
		IntegrationService.getService().saveNoSync(relationshipAssetSet)
							 
		IntegrationService.getService().saveNoSync(sourceRootAsset)
						
	
	if creaCampataFittizia :
		#creo campata fittizia
		# crea asset di secondo livello
		# il primo elemento creato sarà di tipo sostegno
		assetCampataSet = MXServer.getMXServer().getMboSet("ASSET", sourceRootAsset.getUserInfo())
		secondLevelAsset = assetCampataSet.add()
		secondLevelAssetNum = secondLevelAsset.getString("assetnum")
		
		# crea relazione tra sostegno/tratta cavo e la linea
		
		relationshipAssetCampata = relationshipAssetSet.add()
		relationshipAssetCampata.setValue("sourceassetnum", sourceRootAssetNum)
		AssetSAP.append(secondLevelAssetNum)
		relationshipAssetCampata.setValue("targetassetnum", secondLevelAssetNum, MboConstants.NOVALIDATION)
		relationshipAssetCampata.setValue("assetrelationnum", "INCLUDES")
		#set x3sapid
		relationshipAssetCampata.setValue("x3sapid", sapIdCampataFittizia.replace("-S", ""))
		relationshipAssetCampata.setValue("x3sequence",renumSequence(sequence, (numSelAssets*2)+2))
		relationshipAssetCampata.setValue("x3posnr", renumPosNr(newPosNr, 2))
		
		secondLevelAsset.setValue("itemnum", "CAMPATA-S")
		secondLevelAsset.setValue("description", secondLevelAsset.getMboSet("ITEM").getMbo(0).getString("description"))		
		
		# the TOPITEMSTRUCT is oddly set to 1=1 therefore the set must be 
		# correctly filtered as done in the ApplyIASBean.initialize() method	
		itemStructSet = secondLevelAsset.getMboSet("TOPITEMSTRUCT")
		itemStructSet.setQbe("itemnum", secondLevelAsset.getString("itemnum"))
		itemStructSet.setQbe("itemsetid", secondLevelAsset.getString("itemsetid"))
		itemStructSet.setQbe("itemid", secondLevelAsset.getString("itemnum"))
		itemStructSet.setQbeExactMatch(True)
		itemStructSet.moveTo(0)
		itemStructSet.clearItemStructsVector()

		# apply IAS
		secondLevelAsset.applyIASAutoNumAll()
		IntegrationService.getService().saveNoSync(assetSet)
				 
		# set x3assetlinea
		setX3AssetLinea(secondLevelAsset.getMboSet("ASSETCHILDREN"))
		
		IntegrationService.getService().saveNoSync(relationshipAssetCampata)
								 
		IntegrationService.getService().saveNoSync(relationshipAssetSet)
							 
		IntegrationService.getService().saveNoSync(assetCampataSet)
						
																																								
		assetCampataSet.close()
	IntegrationService.getService().saveNoSync(sourceRootAsset)
					   

if sostegno:
	if numSelAssets > 1:
		service.error("x3terna", "ErrorMsg", ["Si prega di selezionare un solo asset per la generazione del sostegno a sei mensole."])
	if not sequence:
		relationshipAssetSet = sourceRootAsset.getMboSet("ASSETLOCRELATION")
		#check se necessario creare campata per ex-sostegno fittizio
		sqlf = SqlFormat("siteid = :1 and sourceassetnum = :2  ")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "SOURCEASSETNUM", sourceRootAssetNum)

		# recupero anticipato di targetSostegno per uso nella campata fittizia
		selAssetlocrelation_pre = selectedAssets.get(0)
		targetAssetNum_pre = selAssetlocrelation_pre.getString("targetassetnum")
		sqlfPre = SqlFormat("siteid = :1 and targetassetnum = :2 and sourceassetnum = :3 ")
		sqlfPre.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlfPre.setObject(2, "ASSETLOCRELATION", "TARGETASSETNUM", targetAssetNum_pre)
		sqlfPre.setObject(3, "ASSETLOCRELATION", "SOURCEASSETNUM", selAssetlocrelation_pre.getString("sourceassetnum"))
		targetCampataLocPre = sourceRootAsset.getMboSet("$target_campata_loc_pre$", "ASSETLOCRELATION", sqlfPre.format()).getMbo(0)
		targetSostegnoPre = None
		if targetCampataLocPre:
			targetSostegnoRelPre = sourceRootAsset.getMboSet("$target_sostegno_pre$", "ASSETLOCRELATION", "x3sapid = '" + targetCampataLocPre.getString("x3sapid") + "-S'").getMbo(0)
			if targetSostegnoRelPre:
				targetSostegnoPre = targetSostegnoRelPre.getMboSet("TARGETASSET").getMbo(0)

		assetLocRelationSet = sourceRootAsset.getMboSet("$assetloc$", "ASSETLOCRELATION", sqlf.format())
		if not assetLocRelationSet.isEmpty():
			assetLocRelation = assetLocRelationSet.moveFirst()
			
			while (assetLocRelation):
				x3PosNr = renumPosNr(assetLocRelation.getString("x3posnr"), 2)
				x3Sequence = renumSequence(assetLocRelation.getString("x3sequence"), 2)
				assetLocRelation.setValue("x3sequence", x3Sequence)
				assetLocRelation.setValue("x3posnr", x3PosNr)
				assetLocRelation = assetLocRelationSet.moveNext()
			if not assetLocRelationSet.isEmpty() and assetLocRelationSet.moveFirst().getMboSet("TARGETASSET").getMbo(0).getMboSet("CLASSSTRUCTURE").getMbo(0).getString("CLASSIFICATIONID")=="SOSTEGNO AEREO":
				sqlf = SqlFormat("siteid = :1 and sourceassetnum = :2 and  x3sequence = '000001'")
				sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
				sqlf.setObject(2, "ASSETLOCRELATION", "SOURCEASSETNUM", sourceRootAssetNum)
				assetLocRel = sourceRootAsset.getMboSet("$assetloc_add_inizio$", "ASSETLOCRELATION", sqlf.format()).getMbo(0)
				sapIdCampataFittizia = assetLocRel.getString("x3sapid")
				#creo campata fittizia
				assetCampataSet = MXServer.getMXServer().getMboSet("ASSET", sourceRootAsset.getUserInfo())
				secondLevelAsset = assetCampataSet.add()
				secondLevelAssetNum = secondLevelAsset.getString("assetnum")
					
				# crea relazione tra sostegno/tratta cavo e la linea
				relationshipAssetCampata = relationshipAssetSet.add()
				relationshipAssetCampata.setValue("sourceassetnum", sourceRootAssetNum)
				AssetSAP.append(secondLevelAssetNum)
				relationshipAssetCampata.setValue("targetassetnum", secondLevelAssetNum, MboConstants.NOVALIDATION)
				relationshipAssetCampata.setValue("assetrelationnum", "INCLUDES")
				#set x3sapid
				relationshipAssetCampata.setValue("x3sapid", sapIdCampataFittizia.replace("-S", ""))
				relationshipAssetCampata.setValue("x3sequence","000002")
				relationshipAssetCampata.setValue("x3posnr", "002")

				secondLevelAsset.setValue("itemnum", "CAMPATA-S")
				secondLevelAsset.setValue("description", secondLevelAsset.getMboSet("ITEM").getMbo(0).getString("description"))
				# copia installdate dal sostegno selezionato
				if targetSostegnoPre:
					targetSostegnoInstallDate = targetSostegnoPre.getDate("installdate")
					targetSostegnoIngrp = targetSostegnoPre.getString("X3INGRP")
					targetSostegnoGwerk = targetSostegnoPre.getString("X3GEWRK")
					if targetSostegnoInstallDate:
						secondLevelAsset.setValue("installdate", targetSostegnoInstallDate, MboConstants.NOVALIDATION)
						secondLevelAsset.setValue("X3INGRP", targetSostegnoIngrp, MboConstants.NOVALIDATION)
						secondLevelAsset.setValue("X3GEWRK", targetSostegnoGwerk, MboConstants.NOVALIDATION)


				# the TOPITEMSTRUCT is oddly set to 1=1 therefore the set must be 
				# correctly filtered as done in the ApplyIASBean.initialize() method	
				itemStructSet = secondLevelAsset.getMboSet("TOPITEMSTRUCT")
				itemStructSet.setQbe("itemnum", secondLevelAsset.getString("itemnum"))
				itemStructSet.setQbe("itemsetid", secondLevelAsset.getString("itemsetid"))
				itemStructSet.setQbe("itemid", secondLevelAsset.getString("itemnum"))
				itemStructSet.setQbeExactMatch(True)
				itemStructSet.moveTo(0)
				itemStructSet.clearItemStructsVector()

				# apply IAS
				secondLevelAsset.applyIASAutoNumAll()
				# set x3assetlinea
				setX3AssetLinea(secondLevelAsset.getMboSet("ASSETCHILDREN"))

				IntegrationService.getService().saveNoSync(relationshipAssetCampata)
								   
				IntegrationService.getService().saveNoSync(relationshipAssetSet)
							   
				IntegrationService.getService().saveNoSync(assetCampataSet)
				assetCampataSet.close()

		relationshipAssetSet = sourceRootAsset.getMboSet("ASSETLOCRELATION")
		selAssetlocrelation = selectedAssets.get(0)
		targetAssetNum = selAssetlocrelation.getString("targetassetnum")
		# cerco il sostegno di fine della campata 
		sqlf = SqlFormat("siteid = :1 and targetassetnum = :2 and sourceassetnum = :3 ")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "TARGETASSETNUM", targetAssetNum)
		sqlf.setObject(3, "ASSETLOCRELATION", "SOURCEASSETNUM", selAssetlocrelation.getString("sourceassetnum"))
		targetCampataLoc = sourceRootAsset.getMboSet("$target_campata_loc$", "ASSETLOCRELATION", sqlf.format()).getMbo(0)
		targetSostegnoRelation = sourceRootAsset.getMboSet("$target_sostegno$", "ASSETLOCRELATION", "x3sapid = '" + targetCampataLoc.getString("x3sapid") + "-S'").getMbo(0)
		targetSostegno = targetSostegnoRelation.getMboSet("TARGETASSET").getMbo(0)
		
		targetSostegnoAssetNum = targetSostegno.getString("assetnum")
		newSapId = sapIdGenarator.next()
		relationshipAssetSostegno = relationshipAssetSet.add()
		AssetSAP.append(targetSostegnoAssetNum)
		relationshipAssetSostegno.setValue("sourceassetnum", sourceRootAssetNum)
		relationshipAssetSostegno.setValue("targetassetnum", targetSostegnoAssetNum, MboConstants.NOVALIDATION)
		relationshipAssetSostegno.setValue("assetrelationnum", "INCLUDES")
		relationshipAssetSostegno.setValue("x3sapid", newSapId + "-S")
		relationshipAssetSostegno.setValue("x3sequence", "000001")
		relationshipAssetSostegno.setValue("x3posnr", "000")
		IntegrationService.getService().saveNoSync(relationshipAssetSostegno)
								  
		# apply IAS
		sqlf = SqlFormat("siteid = :1 and assetnum = :2 ")
		sqlf.setObject(1, "ASSET", "SITEID", siteId)
		sqlf.setObject(2, "ASSET", "ASSETNUM", targetSostegnoAssetNum)
		secondLevelAssetSostegno = sourceRootAsset.getMboSet("$secondLevelAssetSostegno$", "ASSET", sqlf.format()).getMbo(0)
				
		if	secondLevelAssetSostegno:
		
			x3parentItem=""
			itemSetID=""
			#change itemnum
			itemSet = secondLevelAssetSostegno.getMboSet("ITEM")
			if not itemSet.isEmpty():
				item = itemSet.getMbo(0)
				x3parentItem = item.getString("x3parentitem")
				itemSetID = item.getString("itemsetid")
			else:
				classstructureId=secondLevelAssetSostegno.getString("CLASSSTRUCTUREID")
				itemSet=secondLevelAssetSostegno.getMboSet("$PARENTITEM$","ITEM","classstructureid='"+str(classstructureId)+"' and itemnum like '%-S%'")
				if not itemSet.isEmpty():
					item = itemSet.getMbo(0)
					x3parentItem = item.getString("x3parentitem")
					itemSetID = item.getString("itemsetid")
			itemnum = secondLevelAssetSostegno.getString("itemnum")
		

			# setup item structure for the asset
			# the TOPITEMSTRUCT is oddly set to 1=1 therefore the set must be 
			# correctly filtered as done in the ApplyIASBean.initialize() method	
			itemStructSet = secondLevelAssetSostegno.getMboSet("TOPITEMSTRUCT")
			itemStructSet.setQbe("itemnum", x3parentItem)
			itemStructSet.setQbe("itemsetid", secondLevelAssetSostegno.getString("itemsetid"))
			itemStructSet.setQbe("itemid", x3parentItem)
			itemStructSet.setQbeExactMatch(True)
			itemStructSet.moveTo(0)
			itemStructSet.clearItemStructsVector()
			
			# apply IAS
			secondLevelAssetSostegno.applyIASAutoNumAll()
																																								 
			IntegrationService.getService().saveNoSync(secondLevelAssetSostegno.getThisMboSet())
			log.debug( "IAS applicata")
			IntegrationService.getService().saveNoSync(secondLevelAssetSostegno)
								  
			#set x3assetlinea
			setX3AssetLinea(secondLevelAssetSostegno.getMboSet("ASSETCHILDREN"))	
	else:
		sqlf = SqlFormat("siteid = :1 and sourceassetnum = :2 and  x3sequence > :3")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "SOURCEASSETNUM", sourceRootAssetNum)
		sqlf.setObject(3, "ASSETLOCRELATION", "X3SEQUENCE", sequence)
		assetLocRelationSet = sourceRootAsset.getMboSet("$assetloc$", "ASSETLOCRELATION", sqlf.format())
		if not assetLocRelationSet.isEmpty():
			assetLocRelation = assetLocRelationSet.moveFirst()
			while (assetLocRelation):
				x3Sequence = renumSequence(assetLocRelation.getString("x3sequence"),2)
				x3PosNr = renumPosNr(assetLocRelation.getString("x3posnr"), 2)
				assetLocRelation.setValue("x3sequence", x3Sequence)
				assetLocRelation.setValue("x3posnr", x3PosNr)
				assetLocRelation = assetLocRelationSet.moveNext()
		sqlf = SqlFormat("siteid = :1 and sourceassetnum = :2 and  x3sequence = :3")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "SOURCEASSETNUM", sourceRootAssetNum)
		sqlf.setObject(3, "ASSETLOCRELATION", "X3SEQUENCE", sequence)
		assetLocRelation2Set = sourceRootAsset.getMboSet("$assetloc2$", "ASSETLOCRELATION", sqlf.format())
		if not assetLocRelation2Set.isEmpty():
			assetLoc = assetLocRelation2Set.getMbo(0)
			posNr = assetLoc.getString("x3posnr")
		assetSet = MXServer.getMXServer().getMboSet("ASSET", sourceRootAsset.getUserInfo())
		assetSet.setMXTransaction(sourceRootAsset.getMXTransaction())
		assetSet.setInsertOrg(sourceRootAsset.getString("orgid"))
		assetSet.setInsertSite(sourceRootAsset.getString("siteid"))
		secondLevelAsset = assetSet.add()
		secondLevelAssetNum = secondLevelAsset.getString("assetnum")
		# crea relazione tra campata e linea
		relationshipAssetSet = sourceRootAsset.getMboSet("ASSETLOCRELATION")
		relationshipAsset = relationshipAssetSet.add()
		relationshipAsset.setValue("sourceassetnum", sourceRootAssetNum)
		AssetSAP.append(secondLevelAssetNum)
		relationshipAsset.setValue("targetassetnum", secondLevelAssetNum, MboConstants.NOVALIDATION)
		relationshipAsset.setValue("assetrelationnum", "INCLUDES")
		secondLevelAsset.setValue("itemnum", "CAMPATA-S")
		secondLevelAsset.setValue("description", secondLevelAsset.getMboSet("ITEM").getMbo(0).getString("description"))
		# recupero installdate dal sostegno selezionato (campata reale, non fittizia)
		selAssetlocrelation = selectedAssets.get(0)
		targetAssetNum = selAssetlocrelation.getString("targetassetnum")
		sqlfSost = SqlFormat("siteid = :1 and targetassetnum = :2 and sourceassetnum = :3 ")
		sqlfSost.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlfSost.setObject(2, "ASSETLOCRELATION", "TARGETASSETNUM", targetAssetNum)
		sqlfSost.setObject(3, "ASSETLOCRELATION", "SOURCEASSETNUM", selAssetlocrelation.getString("sourceassetnum"))
		targetCampataLocForDate = sourceRootAsset.getMboSet("$target_campata_loc_date$", "ASSETLOCRELATION", sqlfSost.format()).getMbo(0)
		if targetCampataLocForDate:
			targetSostegnoRelForDate = sourceRootAsset.getMboSet("$target_sostegno_date$", "ASSETLOCRELATION", "x3sapid = '" + targetCampataLocForDate.getString("x3sapid") + "-S'").getMbo(0)
			if targetSostegnoRelForDate:
				targetSostegnoForDate = targetSostegnoRelForDate.getMboSet("TARGETASSET").getMbo(0)
				if targetSostegnoForDate:
					targetSostegnoInstallDate = targetSostegnoForDate.getDate("installdate")
					targetSostegnoIngrp = targetSostegnoForDate.getString("X3INGRP")
					targetSostegnoGwerk = targetSostegnoForDate.getString("X3GEWRK")
					if targetSostegnoInstallDate:
						secondLevelAsset.setValue("installdate", targetSostegnoInstallDate, MboConstants.NOVALIDATION)
						secondLevelAsset.setValue("X3INGRP", targetSostegnoIngrp, MboConstants.NOVALIDATION)
						secondLevelAsset.setValue("X3GEWRK", targetSostegnoGwerk, MboConstants.NOVALIDATION)
		newSapId = sapIdGenarator.next()
		relationshipAsset.setValue("x3sequence", renumSequence(sequence,1))
		newPosNr = renumPosNr(posNr,2)
		relationshipAsset.setValue("x3posnr", newPosNr)
		relationshipAsset.setValue("x3sapid", newSapId)
		itemStructSet = secondLevelAsset.getMboSet("TOPITEMSTRUCT")
		itemStructSet.setQbe("itemnum", secondLevelAsset.getString("itemnum"))
		itemStructSet.setQbe("itemsetid", secondLevelAsset.getString("itemsetid"))
		itemStructSet.setQbe("itemid", secondLevelAsset.getString("itemnum"))
		itemStructSet.setQbeExactMatch(True)
		itemStructSet.moveTo(0)
		itemStructSet.clearItemStructsVector()
			
		# apply IAS
		secondLevelAsset.applyIASAutoNumAll()
		
		IntegrationService.getService().saveNoSync(relationshipAssetSet)				
							 
		IntegrationService.getService().saveNoSync(assetSet)
				 
		# set x3assetlinea
		setX3AssetLinea(secondLevelAsset.getMboSet("ASSETCHILDREN"))
		
		# cerco il sostegno di fine della campata 
		sqlf = SqlFormat("siteid = :1 and targetassetnum = :2 and sourceassetnum = :3 ")
		sqlf.setObject(1, "ASSETLOCRELATION", "SITEID", siteId)
		sqlf.setObject(2, "ASSETLOCRELATION", "TARGETASSETNUM", targetAssetNum)
		sqlf.setObject(3, "ASSETLOCRELATION", "SOURCEASSETNUM", selAssetlocrelation.getString("sourceassetnum"))
		targetCampataLoc = sourceRootAsset.getMboSet("$target_campata_loc$", "ASSETLOCRELATION", sqlf.format()).getMbo(0)
		targetSostegnoRelation = sourceRootAsset.getMboSet("$target_sostegno$", "ASSETLOCRELATION", "x3sapid = '" + targetCampataLoc.getString("x3sapid") + "-S'").getMbo(0)
		targetSostegno = targetSostegnoRelation.getMboSet("TARGETASSET").getMbo(0)
		
		targetSostegnoAssetNum = targetSostegno.getString("assetnum")
		#Aggiungo sostegno in doppia terna
		relationshipAssetSostegno = relationshipAssetSet.add()
		relationshipAssetSostegno.setValue("sourceassetnum", sourceRootAssetNum)
		AssetSAP.append(targetSostegnoAssetNum)
		relationshipAssetSostegno.setValue("targetassetnum", targetSostegnoAssetNum, MboConstants.NOVALIDATION)
		relationshipAssetSostegno.setValue("assetrelationnum", "INCLUDES")
		relationshipAssetSostegno.setValue("x3sapid", newSapId + "-S")
		relationshipAssetSostegno.setValue("x3sequence", renumSequence(sequence,2))
		relationshipAssetSostegno.setValue("x3posnr", newPosNr)
		IntegrationService.getService().saveNoSync(relationshipAssetSostegno)
								  
		# apply IAS
		sqlf = SqlFormat("siteid = :1 and assetnum = :2 ")
		sqlf.setObject(1, "ASSET", "SITEID", siteId)
		sqlf.setObject(2, "ASSET", "ASSETNUM", targetSostegnoAssetNum)
		secondLevelAssetSostegno = sourceRootAsset.getMboSet("$secondLevelAssetSostegno$", "ASSET", sqlf.format()).getMbo(0)
				
		if	secondLevelAssetSostegno:
		
			x3parentItem=""
			itemSetID=""
			#change itemnum
			itemSet = secondLevelAssetSostegno.getMboSet("ITEM")
			if not itemSet.isEmpty():
				item = itemSet.getMbo(0)
				x3parentItem = item.getString("x3parentitem")
				itemSetID = item.getString("itemsetid")
			else:
				classstructureId=secondLevelAssetSostegno.getString("CLASSSTRUCTUREID")
				itemSet=secondLevelAssetSostegno.getMboSet("$PARENTITEM$","ITEM","classstructureid='"+str(classstructureId)+"' and itemnum like '%-S%'")
				if not itemSet.isEmpty():
					item = itemSet.getMbo(0)
					x3parentItem = item.getString("x3parentitem")
					itemSetID = item.getString("itemsetid")
			itemnum = secondLevelAssetSostegno.getString("itemnum")
		

			# setup item structure for the asset
			# the TOPITEMSTRUCT is oddly set to 1=1 therefore the set must be 
			# correctly filtered as done in the ApplyIASBean.initialize() method	
			itemStructSet = secondLevelAssetSostegno.getMboSet("TOPITEMSTRUCT")
			itemStructSet.setQbe("itemnum", x3parentItem)
			itemStructSet.setQbe("itemsetid", secondLevelAssetSostegno.getString("itemsetid"))
			itemStructSet.setQbe("itemid", x3parentItem)
			itemStructSet.setQbeExactMatch(True)
			itemStructSet.moveTo(0)
			itemStructSet.clearItemStructsVector()
			
			# apply IAS
			secondLevelAssetSostegno.applyIASAutoNumAll()
																																								 
			IntegrationService.getService().saveNoSync(secondLevelAssetSostegno.getThisMboSet())
			log.debug( "IAS applicata")
			IntegrationService.getService().saveNoSync(secondLevelAssetSostegno)
								  
			#set x3assetlinea
			setX3AssetLinea(secondLevelAssetSostegno.getMboSet("ASSETCHILDREN"))	
		IntegrationService.getService().saveNoSync(sourceRootAsset)
						
	if numSelAssets==1:
		assetSelezionatoSet=MXServer.getMXServer().getMboSet("ASSETLOCRELATION", MXServer.getMXServer().getSystemUserInfo())
		assetSelezionatoSet.setWhere("SITEID='"+siteId+"' and SOURCEASSETNUM='"+selAssetlocrelation.getString("sourceassetnum")+"' and targetassetnum = '"+selAssetlocrelation.getString("targetassetnum")+"'")
		assetSelezionatoSet.reset()
		if not assetSelezionatoSet.isEmpty():
			assetSelezionatoSet.getMbo(0).setValue("X3SEQUENCELISTP","1",11L)
			IntegrationService.getService().saveNoSync(assetSelezionatoSet)
							 
		
assetCreatiMboSet=MXServer.getMXServer().getMboSet("ASSETLOCRELATION",MXServer.getMXServer().getSystemUserInfo())
for asset in AssetSAP:
	assetCreatiMboSet.setWhere("SITEID='"+siteId+"' and SOURCEASSETNUM='"+sourceRootAssetNum+"' and targetassetnum = '"+asset+"'")
	assetCreatiMboSet.reset()
	args = AssetRelationAddArg()
	args.relation =assetCreatiMboSet.getMbo(0)
	scenario = Scenario(SapConnectorScenario.TOUCH)
	scenario.callOperation(SapConnectorScenarioOperation.TOUCH_ADD_ASSET_RELATION,args)
	scenario.save()
assetCreatiMboSet.close()

assetCreati2MboSet=MXServer.getMXServer().getMboSet("ASSET",MXServer.getMXServer().getSystemUserInfo())
for assetNew in AssetSAP:
	assetCreati2MboSet.setWhere("SITEID='"+siteId+"' and assetnum = '"+assetNew+"' or ancestor='"+assetNew+"'")
	assetCreati2MboSet.reset()
	for ancfi in range (0,assetCreati2MboSet.count()):	    assetCreati2Mbo=assetCreati2MboSet.getMbo(ancfi)
	    assetCreati2Mbo.setModified(True)
	assetCreati2MboSet.save()
assetCreati2MboSet.close()

# Simula modifiche sulle relazioni selezionate per inviare un evento al connettore
for i in range (numSelAssets):
	selectedRelSet = MXServer.getMXServer().getMboSet("ASSETLOCRELATION",MXServer.getMXServer().getSystemUserInfo())
	selAssetlocrelation = selectedAssets.get(i)
	selectedRelSet.setWhere("sourceassetnum = '%s' and targetassetnum = '%s'"%(selAssetlocrelation.getString("sourceassetnum"), selAssetlocrelation.getString("targetassetnum")))
	selectedRelSet.reset()
	selectedRel = selectedRelSet.getMbo(0)
	selectedRel.setModified(True)
	selectedRelSet.save()
	selectedRelSet.close()

for k in arrayRiseq:
	riseqMboSet = MXServer.getMXServer().getMboSet("ASSETLOCRELATION",MXServer.getMXServer().getSystemUserInfo())
	riseqMboSet.setWhere("sourceassetnum = '%s' and targetassetnum = '%s'"%(sourceRootAssetNum, k))
	riseqMboSet.reset()
	riseqMbo = riseqMboSet.getMbo(0)
	riseqMbo.setModified(True)
	riseqMboSet.save()
	riseqMboSet.close()
if numSelAssets:
	selAssetlocrelation = selectedAssets.get(0)
	lineaAssetSet= MXServer.getMXServer().getMboSet("ASSET",MXServer.getMXServer().getSystemUserInfo())
	lineaAssetSet.setWhere("assetnum= '%s' and siteid='%s'"%(selAssetlocrelation.getString("sourceassetnum"),selAssetlocrelation.getString("siteid")))
	lineaAssetSet.reset()
	if not lineaAssetSet.isEmpty():
		lineaAsset=lineaAssetSet.getMbo(0)
		lineaAsset.setModified(True)
		lineaAssetSet.save()
	lineaAssetSet.close()
service.closeDialog()
if numSelAssets:
	lineaPropriaAssetSet= MXServer.getMXServer().getMboSet("ASSET",MXServer.getMXServer().getSystemUserInfo())
	lineaPropriaAssetSet.setWhere("assetnum= '%s' and siteid='%s'"%(sourceRootAsset.getString("ASSETNUM"),sourceRootAsset.getString("siteid")))
	lineaPropriaAssetSet.reset()
	if not lineaPropriaAssetSet.isEmpty():
		lineaPropriaAsset=lineaPropriaAssetSet.getMbo(0)
		lineaPropriaAsset.setModified(True)
		lineaPropriaAssetSet.save()
	lineaPropriaAssetSet.close()
	sourceRootAsset.save()"
X3SAPIDLINEA,Gestione formato SAPID Linea (non utilizzato),Draft,"from psdi.mbo import Mbo
from psdi.mbo import MboConstants
from psdi.mbo import MboSet
from psdi.server import MXServer

#####Controllo Digit su singolo livello

def isCorrectLvl1(sapid):
    if (len(sapid) != 7):
        service.error("x3err_length_liv1","x3err_length_liv1",None)
    elif(sapid[0] !='2' or sapid[1].isnumeric() == False or sapid[2].isalnum() == False or sapid[3].isalnum() == False or sapid[4].isalnum() == False or sapid[5].isalpha() == False or sapid[6].isnumeric() == False):
        service.error("x3err_format_liv1","x3err_format_liv1",None)
    elif((sapid[2].isalpha() and sapid[2].islower()) or (sapid[2].isalpha() and sapid[2].islower()) or (sapid[2].isalpha()and sapid[2].islower()) or sapid[5].islower()):
        service.error("x3err_maiuscole_liv1","x3err_maiuscole_liv1",None)



if interactive and vi_sapid is not None:
    if vi_linea == "LINEA":
        isCorrectLvl1(vi_sapid)         
    elif vi_linea == "STAZIONE":
        if (len(vi_sapid) != 6):
            service.error("x3err_lenght_staz","x3err_lenght_staz",None)
        elif(vi_sapid[0] !='3' or vi_sapid[1].isnumeric() == False or vi_sapid[2].isalnum() == False or vi_sapid[3].isalnum() == False or vi_sapid[4].isalnum() == False or vi_sapid[5].isalpha() == False):
            service.error("x3err_format_staz","x3err_format_staz",None)"
X3RIEPILOGO,Riepilogo dati (X3COUNT),Draft,"'''
Created on 15 giu 2023

- 3.11.4    Visualizzazione aggregata sul 1 livello di attributi relativi al 2 livello
            La necessità espressa è quella di mostrare sulla linea un set di informazioni ottenute aggregando il contenuto di differenti attributi presenti 
            sugli elementi di secondo livello oppure contando il numero degli elementi di secondo livello stessi. Di seguito il dettaglio di fonti e regole:
            
        1) Tabella con tutte le unità impianti presenti su una linea, e per ciascuna UI e CLR (centro di lavoro responsabile):
            a) - Il totale dei sostegni (conteggio asset di classificazione Sostegno);
            b) - Il totale dei Km equivalenti (sommatoria dell’attributo Km di tracciato);
            c) - La lunghezza della terna (sommatoria dell’attributo Km di circuito);
            d) - Il totale dei Km di cavo terrestre (sommatoria dell’attributo Km per le tratte di cavo con l’attributo Tipo posa = Terrestre);
            e) - Il totale dei Km di cavo marino (sommatoria dell’attributo Km per le tratte di cavo con l’attributo Tipo posa = Marino);
        2) Informazione dell’unità impianti prevalente di una linea (il dato viene ricavato inserendo l’unità impianti con la maggiore quantità di 
           chilometri di tracciato)
            

- Script with Action Launch Point action: 'X3COUNT'

    IN v_assetnum = ASSETNUM tabella ASSET corrisponde alla linea di livello 1
    IN v_siteid = SITEID tabella ASSET

- Script with Attribute Launch Point: field NUMVALUE table ASSETSPEC

    IN v_assetnum = ASSETNUM tabella ASSETSPEC
    IN v_siteid = SITEID tabella ASSETSPEC
    
- Script with Attribute Launch Point: field X3SEQUENCE table ASSETLOCRELATION ONLY ONUPDATE (ONADD IS MANAGED BY ASSETLOCRELATION OBJECT LAUNCH POINT)

    IN v_siteid = SITEID tabella ASSETLOCRELATION
    
- Script with Attribute Launch Point: field X3INGRP table ASSET

    IN v_assetnum = ASSETNUM tabella ASSETSPEC
    IN v_siteid = SITEID tabella ASSETSPEC 
    
- Script with Attribute Launch Point: field STATUS table ASSET

    IN v_assetnum = ASSETNUM tabella ASSETSPEC
    IN v_siteid = SITEID tabella ASSETSPEC                    

@version: v2 - Added IN Clause to WHERE CONDITION for retrieving ASSETSPEC ATTRIBUTE
@version: v3 - Removed JSON lib and String Format. Added method toString to class Counter, Added status clause
@version: v4 - Modified Attribute name and saving result to DB
@version: v5 - Save result on ASSETSPEC TABLE -> added method saveAssetSpecCounter and updated saveResult method
@version: v6 - Refactoring method getInfoAssetSpec and restored writing to custom table
@version: v7 - Put assetspecfield in READONLY and refacotoring all method for the attribute launch point
@version: v8 - Changed class structure id with classification id and refactoring method added object launch point
@version: v9 - Changed Object Launch point from ASSET to ASSETLOCRELATION, added new logic for calculate LIN_UIM_SOSTEGNO_INI and LIN_UIM_SOSTEGNO_FIN
@version: v10 - Added new launch point X3RIEPILOGO_ATT_X3INGRP --> LA LOGICA PER IL CALCOLO DEI SOSTEGNI è STATA SPOSTATA QUI
@version: v11 - Added new launch point X3RIEPILOGO_ATT_STATUS and added 2 attribute in x3rollup table (SOSTEGNO_ESTREMO_INI, SOSTEGNO_ESTREMO_FIN)
@version: v12 - Added new AGGREGATION on field X3GEWRK ("CENTRO DI LAVORO RESPONSABILE")
@version: v13 - Modified counter for sostegni, added estremo cavo to the counter

N.B.: Attivare questo script o su ACTION LAUNCH POINT oppure su ATTRIBUTE e OBJECT LAUNCH POINT

ASSUNZIONI:
    1) L'ATTRIBUTO LIN_UIM%s_ID (dove %s in [1,2,3,4]) già POPOLATO

@author: M.Altobelli
'''
from psdi.server import MXServer
from psdi.util.logging import MXLoggerFactory
from psdi.mbo import MboConstants
from java.lang import String
#OP57
from com.terna.integration.service import IntegrationService
from com.terna.integration.sapconnector import SapConnectorScenario

logger = MXLoggerFactory.getLogger("maximo.script.X3RIEPILOGO")
ARR_ASSETATTRID_DEST = ['LIN_UIM%s_KM_EQUIVALENTI', 'LIN_UIM%s_LUNG_TERNA', 'LIN_UIM%s_NUM_SOSTEGNI']
ARR_STATUS_DECOMMISSIONED = ['CEAL', 'DEMO', 'DIST']
ALL = "ALL"
ACTION = "X3RIEPILOGO"
ATTRIBUTE = "X3RIEPILOGO_ATT"
ATTRIBUTE_X3SEQUENCE = "X3RIEPILOGO_ATT_X3SEQUENCE"
ATTRIBUTE_X3GEWRK = "X3RIEPILOGO_ATT_X3INGRP"
ATTRIBUTE_STATUS = "X3RIEPILOGO_ATT_STATUS"
OBJECT = "X3RIEPILOGO_OBJ"
IS_ATTR_LAUNCH_POINT = False
IS_ATTR_X3_GWERK_LAUNCH_POINT = False
IS_ATTR_STATUS_LAUNCH_POINT = False
arr_idx = [4,3,2,1]

'''
@summary This class holds all the counter: tot sostegni, tot km tracciato, tot km circuito, tot km cavo marino, tot km cavo terrestre
if action point is ACTION holds all the counter for the whole linea
if action point is Attribute holds only the attribute changed
'''
class Counters:    
    def __init__(self):
        self.totSostegni = 0
        self.totKmTracciato = 0
        self.totKmCircuito = 0
        self.totKmCavoTerrestre = 0
        self.totKmCavoMarino = 0
    def toString(self):
        return str("totSostegni=" + str(self.totSostegni) + ", totKmTracciato=" + str(self.totKmTracciato) + 
                   ", totKmCircuito=" + str(self.totKmCircuito) + ", totKmCavoTerrestre=" + str(self.totKmCavoTerrestre) + 
                   ", totKmCavoMarino=" + str(self.totKmCavoMarino))        
    def addSostegno(self):
        self.totSostegni +=1
    def remSostegno(self):
        self.totSostegni -=1                 
    def addKmTracciato(self, km):
        if km is not None:
            self.totKmTracciato += km                   
    def addKmCircuito(self, km):
        if km is not None:
            self.totKmCircuito += km
    def addKmCavoTerrestre(self, km):
        if km is not None:
            self.totKmCavoTerrestre += km        
    def addKmCavoMarino(self, km):
        if km is not None:
            self.totKmCavoMarino += km
 
dict_result={"x3ingrp" : {}, "x3gewrk" : {} , ALL : Counters()} 
                  
def getMboSet(name):
    return MXServer.getMXServer().getMboSet(name, MXServer.getMXServer().getSystemUserInfo())


'''
@method getInfoAsset
@param assetnum: asset number
@return: ui: unità impianti, clr: centro di lavoro responsabile, classId: classificazione asset id 
@summary: this method return some info of specific asset like X3INGRP, CLASSSTRUCTUREID
'''    
def getInfoAsset(assetnum):
    assetSet = getMboSet("ASSET")
    where = "assetnum='" + assetnum + "' and siteid='" + v_siteid + "'" + " and status not in ('"+ '\',\''.join(ARR_STATUS_DECOMMISSIONED) +"')"
    assetSet.setWhere(where)
    asset = assetSet.getMbo(0)
    if asset is not None:
        classStructMboSet = asset.getMboSet("CLASSSTRUCTURE")
        classStructMbo = classStructMboSet.getMbo(0)
        classId = classStructMbo.getString("CLASSIFICATIONID")
        ui = asset.getString("X3INGRP")
        clr = asset.getString("X3GEWRK")
        classStructMboSet.close()
        assetSet.close()
        return ui, clr, classId
    else:
        return None, None, None
    

'''
@method getInfoChildAsset
@param parentAssetNum: parent asset number, classStructChild: child classification
@return: assetnum: child asset number
@summary: this method return the third child level asset number
''' 
def getInfoChildAsset(parentAssetNum, classStructChild):
    assetSet = getMboSet("ASSET")
    where = ("parent='" + parentAssetNum + "' and siteid='" + v_siteid + "'" + 
             " and classstructureid='" + classStructChild + "'" +
             " and status not in ('"+ '\',\''.join(ARR_STATUS_DECOMMISSIONED) +"')")
    assetSet.setWhere(where)
    asset = assetSet.getMbo(0)
    assetnum = None
    if asset is not None:
        assetnum = asset.getString("assetnum")
    assetSet.close()
    return assetnum


'''
@method getAssetSpecValue
@param assetnum: asset number, assetattrid: asset attribute id, isNumeric: return numeric value or string value
@return: NUMVALUE or ALNVALUE of ASSETSPEC TABLE
@summary: this method return the field NUMVALUE/ALNVALUE of ASSETSPEC TABLE
'''
def getAssetSpecValue(assetnum, assetattrid, isNumeric):
    #GET THE VALUE FROM ASSETSPEC TABLE      
    assetSpecSet = getMboSet("ASSETSPEC")
    where = "assetnum='" + assetnum + "' and siteid='" + v_siteid + "'" + " and assetattrid = '" + assetattrid + "'"       
    assetSpecSet.setWhere(where)
    assetSpec = assetSpecSet.getMbo(0)
    if assetSpec is not None:
        value = assetSpec.getDouble("NUMVALUE") if isNumeric else assetSpec.getString("ALNVALUE")
        assetSpecSet.close()
        return value  

'''
@method getInfoAssetSpec
@param assetnum: asset number, classId: asset classification id
@return: VALUE: value to update to DB, isTrattaCavo: True/False, isCavoMarino: True/False
@summary: this method return an attribute value retrieved from ASSET SPEC TABLE or from MBOVALUE OBJECT
'''
def getInfoAssetSpec(assetnum, classId):
    isTrattaCavo = False
    isCavoMarino = False
    
    #Get the class structure id if classification id is not null
    classStructMboSet = getMboSet("CLASSSTRUCTURE")
    where = "classificationid=" + ("'CAVI'" if classId == 'TRATTA CAVO' else "'CONDUTTORI'")
    classStructMboSet.setWhere(where)
    classStructMbo = classStructMboSet.getMbo(0)
    classStructId = classStructMbo.getString("CLASSSTRUCTUREID")
    #Get the 3rd level children
    #if class is TRATTA_CAVO RETRIEVE 3 LEVEL CHILDREN CAVI AND THEN RETRIEVE RELATED ATTRIBUTE ->  LINEA->TRATTA CAVO->CAVI(0,1)
    #if class is CAMPATA RETRIEVE 3 LEVEL CHILDREN CONDUTTORI AND THEN RETRIEVE RELATED ATTRIBUTE ->  LINEA->CAMPATA->CONDUTTORI(0,1)      
    assetnumchild=getInfoChildAsset(assetnum, classStructId)

    if classId == 'TRATTA CAVO':
        isTrattaCavo = True
        if assetnumchild is not None:
            tipoPosa = getAssetSpecValue(assetnumchild, "LIN_TIPO_POSA", False) 
            logger.info(" - X3RIEPILOGO - tipoPosa: "+str(tipoPosa) )
            if tipoPosa and tipoPosa.upper() == 'S': 
                isCavoMarino = True
            elif (not tipoPosa or tipoPosa==''):
                isTrattaCavo = False
 
    elif classId == 'CAMPATA AEREA' and assetnumchild is not None: 
        campataRif = getAssetSpecValue(assetnumchild, "LIN_CAMPATA_RIFERIMENTO", False)     
        if campataRif is not None and len(campataRif) > 0:
            #if LIN_CAMPATA_RIFERIMENTO is not None return void dict_result
            return None, False, False
    logger.info(" - X3RIEPILOGO - isCavoMarino?: "+str(isCavoMarino)+" assetnum"+str(assetnum) )
    if IS_ATTR_LAUNCH_POINT:
        #LAUNCH POINT IS ATTRIBUTE GET THE VALUE FROM MBOVALUE
        if onadd:
            value = mbovalue.getCurrentValue().asDouble()
        elif onupdate:
            value = ( mbovalue.getCurrentValue().asDouble() - mbovalue.getInitialValue().asDouble() 
                      if mbovalue.getInitialValue() is not None else mbovalue.getCurrentValue().asDouble() )
        elif ondelete:
            value = -1 * mbovalue.getInitialValue().asDouble()
       
      
    else:
        #LAUNCH POINT IS ACTION GET THE VALUE FROM ASSETSPEC TABLE
        value = getAssetSpecValue(assetnum, "LIN_LUNGHEZZA", True)
        if ondelete:
            value = -1 * value        

    classStructMboSet.close()         
    return value, isTrattaCavo, isCavoMarino


'''
@method rollupAttribute
@param ui: unità impianti, clr: centro di lavoro responsabile, classStructureId: asset classification, assetnum: asset number, fattoreK: multiplier voltage level 
@return: None
@summary: this method rollup the second level attribute value to the first level
'''
def rollupAttribute(ui, clr, classId, assetnum, fattoreK):
    #logger.info(" - X3RIEPILOGO - rollupAttribute() - UI: " + ui + ", CLR: " + clr + ", CLASSID: " + classId)

    #Retrieve counters by ui
    if ui is not None and dict_result.get("x3ingrp").get(ui) is None:
        dict_result["x3ingrp"][ui]=Counters()
        
    if clr is not None and dict_result.get("x3gewrk").get(clr) is None:
        dict_result["x3gewrk"][clr]=Counters()
    
    counterUi = dict_result.get("x3ingrp").get(ui)
    counterClr = dict_result.get("x3gewrk").get(clr)
    
    if classId == 'SOSTEGNO AEREO' or classId == 'ESTREMO CAVO':
        #INCREMENT ONLY COUNTER 'TOT_SOSTEGNI'
        if ondelete:
            if counterUi is not None:
                counterUi.remSostegno()
            if counterClr is not None:
                counterClr.remSostegno()
        else:
            if counterUi is not None:
                counterUi.addSostegno()
            if counterClr is not None:
                counterClr.addSostegno()
            
    elif ( classId == 'TRATTA CAVO' or classId == 'CAMPATA AEREA' ):
        #INCREMENT OTHER COUNTER
        value, isTrattaCavo, isCavoMarino = getInfoAssetSpec(assetnum, classId)
        if value is not None:
            value = value / 1000
            if isTrattaCavo:
                counterAll = dict_result.get(ALL)
                if isCavoMarino:
                    counterAll.addKmCavoMarino(value)
                else:
                    counterAll.addKmCavoTerrestre(value)
                                       
            if fattoreK is not None:
                valueK = value * fattoreK
                if counterUi is not None:
                    counterUi.addKmTracciato(valueK)
                if counterClr is not None:
                    counterClr.addKmTracciato(valueK)                
            
            if counterUi is not None:    
                counterUi.addKmCircuito(value)
            if counterClr is not None:    
                counterClr.addKmCircuito(value)


'''
@method saveAssetSpecValue
@param assetAttrId: attribute id, value: attribute value, p_assetnum: asset number, isNumeric: field is numeric or not 
@return: None
@summary: this method save to the table ASSETSPEC the value of the attribute present in the params
'''
def saveAssetSpecValue(assetAttrId, value, p_assetnum, isNumeric = True):
    #CHECK IF UIM IS ALREADY PRESENT ON DB
    assetSpecSet2 = getMboSet("ASSET")
    assetSpecSet2.reset()
    where = "assetnum='" + p_assetnum + "' and siteid='" + v_siteid + "'" 
    assetSpecSet2.setWhere(where)
    assetSpec2 = assetSpecSet2.getMbo(0)
    assetSpecSet= assetSpec2.getMboSet("$ASSETSPECDISPFASIEST$","ASSETSPEC",":assetnum=assetnum and siteid=:siteid and assetattrid = '"+assetAttrId+"'")
    assetSpec=assetSpecSet.getMbo(0)
    
    if assetSpec is None:
        logger.warn("ERROR - ATTRIBUTE DOESN'T EXIST: " + assetAttrId)
    else:     
        #Save ASSETSPEC ATTRIBUTE
        if isNumeric:
            if IS_ATTR_LAUNCH_POINT or IS_ATTR_X3_GWERK_LAUNCH_POINT or IS_ATTR_STATUS_LAUNCH_POINT:
                numval = assetSpec.getDouble("NUMVALUE") + round(value,3)
            else:
                numval = round(value,3)
                
            assetSpec.setValue("NUMVALUE", numval if numval > 0 else 0 , 
                               MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
            assetSpec.setFieldFlag("NUMVALUE", MboConstants.READONLY, True)
        else:
            assetSpec.setValue("ALNVALUE", value, MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
            assetSpec.setFieldFlag("ALNVALUE", MboConstants.READONLY, True)        
            
        assetSpecSet.save()
    assetSpecSet.close()



'''
@method deleteOldUim
@param p_assetnum: asset number 
@return: None
@summary: this method delete all the record from the X3ROLLUP, ASSETSPEC table where INGRP is not in keys of dict_result
'''
def deleteOldUim(p_assetnum):
    logger.info(" - X3RIEPILOGO - deleteOldUim() - richiamato metodo")
    counterSetToDel = getMboSet("X3ROLLUP")
    where = "assetnum='" + p_assetnum + "' and ingrp not in (SELECT X3INGRP FROM asset WHERE status not in ('DEMO','CEAL') and assetnum IN (SELECT targetassetnum FROM assetlocrelation WHERE sourceassetnum='" + p_assetnum + "' and siteid='" + v_siteid + "')) and siteid='" + v_siteid + "'"
    logger.info(" - X3RIEPILOGO - clausola where di deleteOldUim() - "+str(where))
    counterSetToDel.setWhere(where)
    
    if not counterSetToDel.isEmpty():
        logger.info(" - X3RIEPILOGO - deleteOldUim() - removed " + str(counterSetToDel.count()) + " records on X3ROLLUP table")
        counterSetToDel.deleteAll(MboConstants.NOACCESSCHECK)
        counterSetToDel.save()
    counterSetToDel.close()
    
    assetSpecToClean = getMboSet("ASSETSPEC")
    where = "assetnum='" + p_assetnum + "' and alnvalue not in (SELECT X3GEWRK FROM asset WHERE status not in ('DEMO','CEAL') and assetnum IN (SELECT targetassetnum FROM assetlocrelation WHERE sourceassetnum='" + p_assetnum + "' and siteid='" + v_siteid + "')) and assetattrid like 'LIN_UIM%ID'  and siteid='" + v_siteid + "'"
    logger.info(" - X3RIEPILOGO - clausola where2 di deleteOldUim() - "+str(where))
    assetSpecToClean.setWhere(where)
    if not assetSpecToClean.isEmpty():
        for i in range(0, assetSpecToClean.count()):
            assetSpec = assetSpecToClean.getMbo(i)
            clr = assetSpec.getString("ALNVALUE")
            logger.info(" - X3RIEPILOGO - deleteOldUim() - clean attribute for this clr: " +clr)
            cleanUiAttributes(clr, p_assetnum)
    assetSpecToClean.close()
'''
@method existAssetForUI
@param uim: unità impianti 
@return: Boolean
@summary: this method check if exist asset for a specific UI
'''
def existAssetForUI(uim):
    assetSet = getMboSet("ASSET")
    where = "x3gewrk='" + uim + "' and siteid='" + v_siteid + "' and assetnum != '"+v_assetnum+"'"
    assetSet.setWhere(where)
    value = True
    if assetSet.isEmpty():
        value = False
    assetSet.close()
    return value

'''
@method cleanUiAttribute
@param uim: unità impianti 
@return: None
@summary: this method clean all attribute for a specific UI
'''
def cleanUiAttributes(uim, assetNumParent):
    assetSpecSet = getMboSet("ASSETSPEC")
    where = ("assetnum='" + assetNumParent  + "' and siteid='" + v_siteid + "'" + " and assetattrid like 'LIN_UIM%' and alnvalue ='"+uim+"'")
    assetSpecSet.setWhere(where)
    assetSpec = assetSpecSet.getMbo(0)
    arr_idx = [4,3,2,1]
    if assetSpec is not None:
        res = [int(i) for i in assetSpec.getString("assetattrid") if i.isdigit()]
        idx = arr_idx.pop(arr_idx.index(res[0]))
        linUimId = String.format("LIN_UIM%s", idx) 
        assetSpecSet.reset()
        where = ("assetnum='" + assetNumParent  + "' and siteid='" + v_siteid + "'" + " and assetattrid like '"+linUimId+"_%'")
        assetSpecSet.setWhere(where)
        for i in range(0, assetSpecSet.count()):
            
            assetSpec = assetSpecSet.getMbo(i)
            logger.info(" - X3RIEPILOGO - cleanUiAttributes() - clean attribute: " +assetSpec.getString("assetattrid"))
            #clean NUMVALUE
            assetSpec.setValue("NUMVALUE", None, MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
            assetSpec.setFieldFlag("NUMVALUE", MboConstants.READONLY, True)
            #clean ALNVALUE
            assetSpec.setValue("ALNVALUE", None, MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
            assetSpec.setFieldFlag("ALNVALUE", MboConstants.READONLY, True)        
        
        assetSpecSet.save()
    assetSpecSet.close()            
    

'''
@method saveResultOnCustomTable
@param p_assetnum: asset number 
@return: None
@summary: this method save the values of the attributes present in the counter object to the custom table X3ROLLUP if UIM is <> 'ALL'
'''
def saveResultOnCustomTable(p_assetnum):
            
    for uim in dict_result.get("x3ingrp"):
        logger.debug(" - X3RIEPILOGO - saveResultOnCustomTable() - save counter for: " + uim)
        counter = dict_result.get("x3ingrp").get(uim)
        #SAVE UIM ON TABLE CUSTOM
        counterSet = getMboSet("X3ROLLUP")
        where = "assetnum='" + p_assetnum + "' and ingrp='" + uim + "' and siteid='" + v_siteid + "'"
        counterSet.setWhere(where)
        counterMbo = counterSet.getMbo(0)
        
        if counterMbo is None:
            counterMbo = counterSet.add()
        
        counterMbo.setValue("ASSETNUM", v_assetnum, MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
        counterMbo.setValue("INGRP", uim, MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
        counterMbo.setValue("SITEID", v_siteid, MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)   
        counterMbo.setValue("TOT_SOSTEGNI", counter.totSostegni if not IS_ATTR_LAUNCH_POINT else counterMbo.getDouble("TOT_SOSTEGNI") + counter.totSostegni, 
                            MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
        counterMbo.setValue("KM_EQUIVALENTI", counter.totKmTracciato if not IS_ATTR_LAUNCH_POINT else counterMbo.getDouble("KM_EQUIVALENTI") + counter.totKmTracciato, 
                            MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
        counterMbo.setValue("LUNGHEZZA_TERNA", counter.totKmCircuito if not IS_ATTR_LAUNCH_POINT else counterMbo.getDouble("LUNGHEZZA_TERNA") + counter.totKmCircuito, 
                            MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
        '''counterMbo.setValue("X3KM_CAVOTERRESTRE", counter.totKmCavoTerrestre if not IS_ATTR_LAUNCH_POINT else counterMbo.getDouble("X3KM_CAVOTERRESTRE") + counter.totKmCavoTerrestre, 
                            MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
        counterMbo.setValue("X3KM_CAVOMARINO", counter.totKmCavoMarino if not IS_ATTR_LAUNCH_POINT else counterMbo.getDouble("X3KM_CAVOMARINO") + counter.totKmCavoMarino, 
                            MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)'''
        
        #SAVE SUPPORT INITIAL AND FINAL SAPID
        sapIdIni, sapIdFin = trovaEstremoIniAndFin(v_assetnum, uim, "X3INGRP")
        counterMbo.setValue("SOSTEGNO_ESTREMO_INI", sapIdIni, MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
        counterMbo.setValue("SOSTEGNO_ESTREMO_FIN", sapIdFin, MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)           
        counterSet.save()
        counterSet.close() 

'''
@method checkX3Sequence
@param sapId: x3sapid of the initial/final sostegno, assetNumParent: asset number, attributeId: attribute id, isInitial: boolean if the sostegno is inital or final
@return: None
@summary: this method save X3SAPID of the new initial/final sostegno 
'''
def checkX3Sequence(sapId, assetNumParent, attributeId, isInitial):
    #Get the x3sequence of the initial sostegno if isInitial = True otherwise get the x3sequence of the final sostegno
    assetSet = getMboSet("ASSETLOCRELATION")
    where = "X3SAPID='" + sapId + "' and siteid='" + v_siteid + "'"
    assetSet.setWhere(where)
    asset = assetSet.getMbo(0)
    if asset is not None:
        x3Sequence = asset.getString("X3SEQUENCE")
        
        #Get the value updated on the filed x3sequence
        newX3Sequence = mbovalue.getCurrentValue().asString()
        if isInitial:
            #Check if the new x3sequence is less than x3sequence of the initial sostegno
            if newX3Sequence < x3Sequence:
                saveAssetSpecValue(attributeId, mbo.getString("X3SAPID"), assetNumParent, False)
                logger.info(" - X3RIEPILOGO - checkX3Sequence() - save new initial sostegno with this sapid: " + mbo.getString("X3SAPID"))
        else:
            #Check if the new x3sequence is greater than x3sequence of the final sostegno 
            if newX3Sequence > x3Sequence:
                saveAssetSpecValue(attributeId, mbo.getString("X3SAPID"), assetNumParent, False)
                logger.info(" - X3RIEPILOGO - checkX3Sequence() - save new final sostegno with this sapid: " + mbo.getString("X3SAPID"))
    else:
        logger.info(" - X3RIEPILOGO - checkX3Sequence() - Impossible to check sequence on asset with sapid equal to none")
            
    assetSet.close()    


'''
@method checkAttributeIndexLinUimId
@param p_assetnum: asset number, clr: centro lavoro responsabile 
@return: None
@summary: this method find out the index where the clr passed as parameter has been saved. The index could be 1,2,3 or 4
'''
def checkAttributeIndexLinUimId(p_assetnum, clr):
    #UIM IS NOT ALL SAVE OTHER COUNTER TO ASSET SPEC TABLE 
    #CHECK IF UIM IS ALREADY PRESENT ON DB
    assetSpecSet = getMboSet("ASSETSPEC")
    where = ("assetnum='" + p_assetnum  + "' and siteid='" + v_siteid + "'" + " and assetattrid like 'LIN_UIM%' and alnvalue ='"+clr+"'")
    assetSpecSet.setWhere(where)
    assetSpec = assetSpecSet.getMbo(0)
    idx = 0
    
    if assetSpec is None:
        #UIM IS NOT PRESENT
        #Get the first available INDEX
        #assetSpecSet = getMboSet("ASSETSPEC")
        assetSpecSet.reset()
        #where = ("assetnum='" + p_assetnum  + "' and siteid='" + v_siteid + "'" + " and assetattrid IN ('LIN_UIM1_ID', 'LIN_UIM2_ID', 'LIN_UIM3_ID', 'LIN_UIM4_ID') and (alnvalue is null or alnvalue not in ('"+ '\',\''.join(dict_result.get("x3gewrk").keys()) +"'))")
        where = ("assetnum='" + p_assetnum  + "' and siteid='" + v_siteid + "'" + " and assetattrid IN ('LIN_UIM1_ID', 'LIN_UIM2_ID', 'LIN_UIM3_ID', 'LIN_UIM4_ID') and alnvalue is null" )
        assetSpecSet.setWhere(where)
        assetSpecSet.setOrderBy("assetattrid ASC")

        assetSpec = assetSpecSet.getMbo(0)
        if assetSpec is not None:
            logger.info(" - X3RIEPILOGO - checkAttributeIndexLinUimId() - save CLR : ("+clr+ ") on attribute : (" +assetSpec.getString("assetattrid")+") for asset: "+p_assetnum)
            res = [int(i) for i in assetSpec.getString("assetattrid") if i.isdigit()] 
            #idx = arr_idx.pop(arr_idx.index(res[0]))
            idx = res[0]
            logger.info(" - X3RIEPILOGO - checkAttributeIndexLinUimId() - save CLR : ("+clr+ ") on index: "+str(idx))

            #Save UIM ASSETSPEC ATTRIBUTE
            assetSpec.setValue("ALNVALUE", clr, MboConstants.NOACCESSCHECK|MboConstants.NOVALIDATION)
            assetSpec.setFieldFlag("ALNVALUE", MboConstants.READONLY, True)
            assetSpecSet.save()
            

        else:
            logger.warn(" - X3RIEPILOGO - checkAttributeIndexLinUimId() - impossible to find an empty index to save new CLR for asset "+p_assetnum)
            #service.error("impossibile salvare piu' di 4 responsabile centro di lavoro per l'asset ", p_assetnum)
            idx = -1
                    
    else:
        #UIM IS ALREADY PRESENT
        #Get the INDEX FROM ASSETATTRID
        logger.info(" - X3RIEPILOGO - checkAttributeIndexLinUimId() - CLR is already present: "+clr)
        res = [int(i) for i in assetSpec.getString("assetattrid") if i.isdigit()] 
        #idx = arr_idx.pop(arr_idx.index(res[0]))
        idx = res[0]
  
    assetSpecSet.close()        
    return idx
        
'''
@method saveResultOn1Lvl
@param p_assetnum: asset number 
@return: None
@summary: this method save the values of the attributes present in the counter object to the table ASSETSPEC
'''             
def saveResultOn1Lvl(p_assetnum, isTrovaEstremi = False, excludeEstremo = None):
    for key in dict_result:

        if key == ALL:
            logger.info(" - X3RIEPILOGO - saveResultOn1Lvl() - save counter for ALL")
            counter = dict_result.get(key)
            #UIM IS ALL SAVE ONLY LUNGHEZZA CT AND LUNGHEZZA CM
            saveAssetSpecValue("LIN_LUNGHEZZA_CT", counter.totKmCavoTerrestre, p_assetnum)
            saveAssetSpecValue("LIN_LUNGHEZZA_CM", counter.totKmCavoMarino, p_assetnum)
        elif key == "x3gewrk":
            for clr in dict_result.get("x3gewrk"):
                if clr.strip():
                    logger.info(" - X3RIEPILOGO - saveResultOn1Lvl() - save counter for: " + clr)
                    counter = dict_result.get("x3gewrk").get(clr)
                    idx = checkAttributeIndexLinUimId(p_assetnum, clr)
                               
                    if not IS_ATTR_LAUNCH_POINT:
                        #SALVO IL NUMERO DEI SOSTEGNI SE è ACTION LAUNCH POINT
                        saveAssetSpecValue(String.format("LIN_UIM%s_NUM_SOSTEGNI", idx), counter.totSostegni, p_assetnum)
                    

                    saveAssetSpecValue(String.format("LIN_UIM%s_KM_EQUIVALENTI", idx), counter.totKmTracciato, p_assetnum)
                    saveAssetSpecValue(String.format("LIN_UIM%s_LUNG_TERNA", idx), counter.totKmCircuito, p_assetnum)
                    
                    #SAVE SUPPORT INITIAL AND FINAL SAPID
                    if isTrovaEstremi:
                        sapIdIni, sapIdFin = trovaEstremoIniAndFin(p_assetnum, clr, "X3GEWRK", excludeEstremo)
                        saveAssetSpecValue(String.format("LIN_UIM%s_SOSTEGNO_ESTREMO_INI", idx), sapIdIni, p_assetnum, False)
                        saveAssetSpecValue(String.format("LIN_UIM%s_SOSTEGNO_ESTREMO_FIN", idx), sapIdFin, p_assetnum, False)         
                

'''
@method calcSostegnoIniAndFin
@param assetNumParent: asset number of the 1st level, clr: resp. centro di lavoro
@return: None
@summary: this method save the values of the attributes LIN_UIM%s_SOSTEGNO_ESTREMO_INI and LIN_UIM%s_SOSTEGNO_ESTREMO_FIN
''' 
def calcSostegnoIniAndFin(assetNumParent, clr):
    #GET THE INDEX ON WHICH CHECK THE INITIAL AND FINAL SOSTEGNO
    idx = checkAttributeIndexLinUimId(assetNumParent, clr)
    attrSostegnoIni = String.format("LIN_UIM%s_SOSTEGNO_ESTREMO_INI", idx)
    attrSostegnoFin = String.format("LIN_UIM%s_SOSTEGNO_ESTREMO_FIN", idx)
    #GET THE SAP ID OF THE INITAL AND FINAL SOSTEGNO
    sapIdSostIni=getAssetSpecValue(assetNumParent, attrSostegnoIni, False)
    sapIdSostFin=getAssetSpecValue(assetNumParent, attrSostegnoFin, False)
    #CHECK THE INITIAL X3SEQUENCE
    if sapIdSostIni is None or len(sapIdSostIni)==0:
        saveAssetSpecValue(attrSostegnoIni, mbo.getString("X3SAPID"), assetNumParent, False)
    else:
        checkX3Sequence(sapIdSostIni, assetNumParent, attrSostegnoIni, True)
    #CHECK THE FINAL X3SEQUENCE
    if sapIdSostFin is None or len(sapIdSostFin)==0:
        saveAssetSpecValue(attrSostegnoFin, mbo.getString("X3SAPID"), assetNumParent, False)
    else:
        checkX3Sequence(sapIdSostFin, assetNumParent, attrSostegnoFin, False)
        
        
'''
@method trovaEstremoIniAndFin
@param assetnum: asset number, value: valore della unità impianti o del centro responsabile, field: nome del campo della tabella su cui fare filtro
@return: sapIdIni: sap id sostegno inziale, sapIdFin; sap id sostegno finale
@summary: this method find the initial and final support by the UI parameter
''' 
def trovaEstremoIniAndFin(assetnum, value, field, excludeEstremo = None):
    sapIdIni = None
    sapIdFin = None
    #GET THE FIRST AND LAST SUPPORT BY UI
    assetLocSet = getMboSet("ASSETLOCRELATION")
    
    excludeAssetWhere = " AND a2.assetnum != '"+excludeEstremo+"'" if excludeEstremo is not None else ""
    
    where = ("targetassetnum IN ( SELECT a2.assetnum FROM ASSET a2 WHERE a2.classstructureid in  (select classstructureid from classstructure where classificationid in ('SOSTEGNO AEREO', 'ESTREMO CAVO'))" +
                                " AND a2." + field + " = '" + value + "' AND a2.assetnum IN (SELECT targetassetnum FROM ASSETLOCRELATION a WHERE a.sourceassetnum = '" + assetnum + "')" +
                                " AND a2.status not in ('"+ '\',\''.join(ARR_STATUS_DECOMMISSIONED) +"')" + excludeAssetWhere +
             ") and sourceassetnum = '" + assetnum + "' ORDER BY x3sequence")
    
    assetLocSet.setWhere(where)

    if assetLocSet.count() > 0:
        if assetLocSet.count() == 1:
            sapIdIni = assetLocSet.getMbo(0).getString("X3SAPID")
            sapIdFin = sapIdIni
        else:    
            sapIdIni = assetLocSet.getMbo(0).getString("X3SAPID")
            sapIdFin = assetLocSet.getMbo(assetLocSet.count()-1).getString("X3SAPID")   
    assetLocSet.close()
    return sapIdIni, sapIdFin


'''
@method trovaFattoreK
@param livelloTensione: livello di tensione
@return: fattoreK: moltiplicatore
@summary: this method find the K multiplier  
''' 
def trovaFattoreK(livelloTensione):
    fattoreK = None

    if livelloTensione is not None:
        if livelloTensione in ['A','B','C'] or len(livelloTensione) == 0:
            fattoreK = 1.0
        else:
            livelloTensione=int(livelloTensione)
            if livelloTensione <= 1:
                fattoreK = 0.93
            elif livelloTensione == 2:
                fattoreK = 1
            elif livelloTensione >= 3 and livelloTensione <= 9:
                fattoreK = 1.18
            
    return fattoreK
    
            
if interactive or launchPoint == ACTION:
    
    #ACTION LAUNCH POINT -> SEZIONE RIEPILOGO DATI DELLA LINEA
    if launchPoint == ACTION:
        #Remove old UI values
        deleteOldUim(v_assetnum)  
        logger.info(" - X3RIEPILOGO - START - Launch Point is ACTION")
        #RETRIEVE ALL CHILDS OF LEVEL 2
        aLocRelationSet = getMboSet("ASSETLOCRELATION")
        where = "sourceassetnum='" + v_assetnum + "' and siteid='" + v_siteid + "'"
        aLocRelationSet.setWhere(where)
        
    
        if aLocRelationSet is not None and not aLocRelationSet.isEmpty():
             
            #RETRIEVE LIN_LIVELLO_TENSIONE_ESERCIZIO ATTRIBUTE ON 1ST LEVEL
            fattoreK = trovaFattoreK(getAssetSpecValue(v_assetnum, "LIN_LIVELLO_TENSIONE_ESERCIZIO", False)) 
                       
            #FOR EACH CHILD GET UI 
            for i in range(0, aLocRelationSet.count()):
                assetLocRelation = aLocRelationSet.getMbo(i)
                assetNumChild = assetLocRelation.getString("targetassetnum")
                ui, clr, classId = getInfoAsset(assetNumChild)
                if( (ui is not None and ui.strip()) or (clr is not None and clr.strip()) ):         
                    rollupAttribute(ui, clr, classId, assetNumChild, fattoreK)
            
            #Save Result to DB
            saveResultOnCustomTable(v_assetnum)
            saveResultOn1Lvl(v_assetnum, True)
                     
        aLocRelationSet.close()
        logger.info(" - X3RIEPILOGO - END - Launch Point is ACTION")
        
    #ATTRIBUTE LAUNCH POINT -> UPDATE CAMPO NUMVALUE DELLA TABELLA ASSETSPEC 
    elif launchPoint == ATTRIBUTE:
        assetMbo = mbo.getMboSet("ASSET").getMbo(0)
        if (assetMbo.getDouble("LOADED")==False or (assetMbo.getDouble("LOADED")==True and assetMbo.getDouble("EQ2")==True)):
            #Get info related to asset
            ui, clr, classId = getInfoAsset(v_assetnum)
            
            if ui is not None and ui.strip() and mbo.getString("ASSETATTRID") == "LIN_LUNGHEZZA" and classId in ('TRATTA CAVO', 'CAMPATA AEREA'):
                IS_ATTR_LAUNCH_POINT = True
                logger.info(" - X3RIEPILOGO - START - Launch Point is ATTRIBUTE")
                #p_assetnum = mbo.getString("ASSETNUM")
                #RETRIEVE PARENT OF LEVEL 1
                aLocRelationSet = getMboSet("ASSETLOCRELATION")
                where = "targetassetnum='" + v_assetnum + "' and siteid='" + v_siteid + "'"
                aLocRelationSet.setWhere(where)
                if aLocRelationSet is not None and not aLocRelationSet.isEmpty():
                    #PER OGNI PADRE RECUPERATO -> LE LINEE IN DOPPIA TERNA NE HANNO PIU DI UNO
                    for i in range(0, aLocRelationSet.count()):
                        assetLocRelation = aLocRelationSet.getMbo(i)
                        assetNumParent = assetLocRelation.getString("sourceassetnum")
                        #RETRIEVE LIN_LIVELLO_TENSIONE_ESERCIZIO ATTRIBUTE ON 1ST LEVEL ONLY FOR ADD AND UPDATE ATTRIBUTE EVENT
                        fattoreK = None 
                        if onadd or onupdate:
                            fattoreK = trovaFattoreK(getAssetSpecValue(assetNumParent, "LIN_LIVELLO_TENSIONE_ESERCIZIO", False)) 
        
                        #ROLLUP attribute
                        rollupAttribute(None, clr, classId, v_assetnum, fattoreK)
                        #Save Result to DB
                        #saveResultOnCustomTable(assetNumParent)
                        saveResultOn1Lvl(assetNumParent)
                aLocRelationSet.close()

#ATTRIBUTE LAUNCH POINT -> UPDATE SEQUENCE                 
    elif launchPoint==ATTRIBUTE_X3SEQUENCE and onupdate:
        targetAssetSet = mbo.getMboSet("TARGETASSET")
        targetAssetMbo = None
        if targetAssetSet is not None and not targetAssetSet.isEmpty():
            targetAssetMbo = targetAssetSet.getMbo(0)
        if targetAssetMbo is not None:
            # leggo LOADED/EQ2 solo se non nulli
            loaded_val = None
            if not targetAssetMbo.isNull("LOADED"):
                loaded_val = targetAssetMbo.getDouble("LOADED")
            eq2_val = None
            if not targetAssetMbo.isNull("EQ2"):
                eq2_val = targetAssetMbo.getDouble("EQ2")
            # normalizzo in booleani (None => False)
            loaded = (loaded_val != 0) if loaded_val is not None else False
            eq2 = (eq2_val != 0) if eq2_val is not None else False
            if (not loaded) or (loaded and eq2):
                # GET CLASSSTRUCTURE OF THE TARGET ASSET (controllo esistenza)
                classStructMboSet = targetAssetMbo.getMboSet("CLASSSTRUCTURE")
                if classStructMboSet is not None and not classStructMboSet.isEmpty():
                    classStructMbo = classStructMboSet.getMbo(0)
                    if classStructMbo is not None:
                        classId = classStructMbo.getString("CLASSIFICATIONID")
                        if classId == "SOSTEGNO AEREO":
                            logger.info(" - X3RIEPILOGO - START - Launch Point is attribute (ONUPDATE) on X3SEQUENCE field of table ASSETLOCRELATION")
                            v_assetnum = targetAssetMbo.getString("ASSETNUM")
                            clr = targetAssetMbo.getString("X3GEWRK")
                            # RETRIEVE PARENT OF LEVEL 1
                            assetNumParent = mbo.getString("sourceassetnum")
                            logger.info(" - X3RIEPILOGO - launch point attribute_x3sequence for target asset: " + v_assetnum + " - source asset: " + assetNumParent)
                            # if clr is not None and len(clr) > 0:
                                # Calcola sostegno iniziale e finale
                                # calcSostegnoIniAndFin(assetNumParent, clr)
                    # chiudo il set classStructMboSet se aperto
                    classStructMboSet.close()
        else:
            logger.debug(" - X3RIEPILOGO - attribute_x3sequence: no TARGETASSET for relation (sourceassetnum=%s)" % (mbo.getString("sourceassetnum") if not mbo.isNull("sourceassetnum") else "unknown",))
        if targetAssetSet is not None:
            targetAssetSet.close()
            
    #ATTRIBUTE LAUNCH POINT -> UPDATE CAMPO X3INGRP DELLA TABELLA ASSET     
    elif launchPoint == ATTRIBUTE_X3GEWRK and (mbo.getDouble("LOADED")==False or (mbo.getDouble("LOADED")==True and mbo.getDouble("EQ2")==True)):
        IS_ATTR_X3_GWERK_LAUNCH_POINT = True
        #GET THE CLASSIFICATION ID
        classStructMboSet = mbo.getMboSet("CLASSSTRUCTURE")
        classStructMbo = classStructMboSet.getMbo(0)
        classId = classStructMbo.getString("CLASSIFICATIONID")
        classStructMboSet.close()
        
        if classId in ['TRATTA CAVO', 'CAMPATA AEREA', 'SOSTEGNO AEREO', 'ESTREMO CAVO']: 
            logger.info(" - X3RIEPILOGO - START - Launch Point is attribute (ONUPDATE) on X3GEWRK field of table ASSET")            
            #GET NEW UI VALUE
            currentClr = mbovalue.getCurrentValue().asString()
            #GET OLD UI VALUE
            initialClr = mbovalue.getInitialValue().asString()
            #RETRIEVE PARENT OF LEVEL 1
            aLocRelationSet = getMboSet("ASSETLOCRELATION")
            where = "targetassetnum='" + v_assetnum + "' and siteid='" + v_siteid + "'"
            aLocRelationSet.setWhere(where)
            if aLocRelationSet is not None and not aLocRelationSet.isEmpty():
                #PER OGNI PADRE RECUPERATO -> LE LINEE IN DOPPIA TERNA NE HANNO PIU DI UNO
                for i in range(0, aLocRelationSet.count()):
                    ondelete = False
                    assetLocRelation = aLocRelationSet.getMbo(i)
                    assetNumParent = assetLocRelation.getString("sourceassetnum")
                    #RETRIEVE LIN_LIVELLO_TENSIONE_ESERCIZIO ATTRIBUTE ON 1ST LEVEL ONLY FOR ADD AND UPDATE ATTRIBUTE EVENT
                    fattoreK = None 
                    if classId not in ['SOSTEGNO AEREO', 'ESTREMO CAVO'] and (onadd or onupdate):
                        fattoreK = trovaFattoreK(getAssetSpecValue(assetNumParent, "LIN_LIVELLO_TENSIONE_ESERCIZIO", False)) 
                    
                    if initialClr is not None and len(initialClr) > 0:
                        if existAssetForUI(initialClr):
                            logger.info(" - X3RIEPILOGO - ATTRIBUTE_X3GEWRK - Update counter for initial UI: " + initialClr)
                            ondelete = True
                            rollupAttribute(None, initialClr, classId, v_assetnum, fattoreK)
                        else:
                            logger.info(" - X3RIEPILOGO - ATTRIBUTE_X3GEWRK - Clean all counter for initial UI: " + initialClr)
                            cleanUiAttributes(initialClr, assetNumParent)

                    #ROLLUP attribute
                    if currentClr is not None and len(currentClr) > 0:
                        logger.info(" - X3RIEPILOGO - ATTRIBUTE_X3GEWRK - Update counter for current UI: " + currentClr)
                        ondelete = False
                        rollupAttribute(None, currentClr, classId, v_assetnum, fattoreK)                                            
                    #Save Result to DB
                    saveResultOn1Lvl(assetNumParent)
            aLocRelationSet.close()

    #ATTRIBUTE LAUNCH POINT -> UPDATE CAMPO X3STATUS DELLA TABELLA ASSET                    
    elif ( launchPoint == ATTRIBUTE_STATUS and 
           mbovalue.getCurrentValue().asString() in ARR_STATUS_DECOMMISSIONED and 
          (mbo.getDouble("LOADED")==False or (mbo.getDouble("LOADED")==True and mbo.getDouble("EQ2")==True)) ):
        #GET THE CLASSIFICATION ID
        IS_ATTR_STATUS_LAUNCH_POINT = True
        classStructMboSet = mbo.getMboSet("CLASSSTRUCTURE")
        classStructMbo = classStructMboSet.getMbo(0)
        classId = classStructMbo.getString("CLASSIFICATIONID")
        classStructMboSet.close()
        
        if classId in ('TRATTA CAVO', 'CAMPATA AEREA', 'SOSTEGNO AEREO', 'ESTREMO CAVO'):
            logger.info(" - X3RIEPILOGO - START - Launch Point is attribute (ONUPDATE) on STATUS field of table ASSET")
            ondelete = True 
            #RETRIEVE PARENT OF LEVEL 1
            aLocRelationSet = getMboSet("ASSETLOCRELATION")
            where = "targetassetnum='" + v_assetnum + "' and siteid='" + v_siteid + "'"
            aLocRelationSet.setWhere(where)
            if aLocRelationSet is not None and not aLocRelationSet.isEmpty():
                #PER OGNI PADRE RECUPERATO -> LE LINEE IN DOPPIA TERNA NE HANNO PIU DI UNO
                for i in range(0, aLocRelationSet.count()):
                    assetLocRelation = aLocRelationSet.getMbo(i)
                    assetNumParent = assetLocRelation.getString("sourceassetnum")
                    #RETRIEVE LIN_LIVELLO_TENSIONE_ESERCIZIO ATTRIBUTE ON 1ST LEVEL ONLY FOR ADD AND UPDATE ATTRIBUTE EVENT
                    fattoreK = None 
                    if classId not in ['SOSTEGNO AEREO', 'ESTREMO CAVO'] and (onadd or onupdate):
                        fattoreK = trovaFattoreK(getAssetSpecValue(assetNumParent, "LIN_LIVELLO_TENSIONE_ESERCIZIO", False)) 
                        
                    rollupAttribute(mbo.getString("x3ingrp"), mbo.getString("x3gewrk"), classId, v_assetnum, fattoreK)
                    
                    #Save Result to DB
                    saveResultOn1Lvl(assetNumParent, True, v_assetnum)
            aLocRelationSet.close()
           
if launchPoint=="X3RIEPILOGO":
    #Ricalcolo lunghezze
    logger.info(" - X3RIEPILOGO - aggiornamento lunghezze")
    assetPrimoLvl=mbo.getMboSet("$ALOCSECLVL1$","ASSETLOCRELATION","sourceassetnum='"+str(v_assetnum)+"' and siteid=:siteid and targetassetnum in (select assetnum from asset where classstructureid in (select classstructureid from classstructure where classificationid='CAMPATA AEREA' or classificationid='TRATTA CAVO'))")
    if not assetPrimoLvl.isEmpty():
        assetPrimo=assetPrimoLvl.getMbo(0)
        assetPrimoMboSet=assetPrimo.getMboSet("TARGETASSET")
        AssetSpecSet=assetPrimoMboSet.getMbo(0).getMboSet("$ASPECLUN$","ASSETSPEC","ASSETNUM=:ASSETNUM and SITEID=:SITEID AND ASSETATTRID='LIN_LUNGHEZZA'")
        if not AssetSpecSet.isEmpty():
            AssetSpec=AssetSpecSet.getMbo(0)
            AssetSpec.setModified(True)
        IntegrationService.getService().saveNoSync(assetPrimoLvl)
    assetPrimoLvl.close()
    
    #Ricalcolo sostegni
    assetPrimoSostLvl=mbo.getMboSet("$ALOCSECLVLSOST1$","ASSETLOCRELATION","sourceassetnum='"+str(v_assetnum)+"' and siteid=:siteid and targetassetnum not in (select targetassetnum from assetlocrelation group by TARGETASSETNUM having count(*)>1) and targetassetnum in (select assetnum from asset where classstructureid in (select classstructureid from classstructure where classificationid='SOSTEGNO AEREO' or classificationid='ESTREMO CAVO'))")
    if not assetPrimoSostLvl.isEmpty():
        logger.info(" - X3RIEPILOGO - aggiornamento sostegni")
        assetPrimoSost=assetPrimoSostLvl.getMbo(0)
        assetSetSec=assetPrimoSost.getMboSet("TARGETASSET")
        assetSost=assetSetSec.getMbo(0)
        #assetSost.setValue("DESCRIPTION","TEST 2808")
        assetSost.setModified(True)
        IntegrationService.getService().saveNoSync(assetPrimoSostLvl)
    assetPrimoSostLvl.close()"
OSIN.X3SRVPRIMILVL,Processa i messaggi del servizio del connettore MAXIMO-SAP che allinea le sedi tecniche di primo livello,Draft,"# OSIN.X3SRVPRIMILVL
# Processa i messaggi del servizio del connettore MAXIMO-SAP
# che allinea le sedi tecniche di primo livello
# v1.0 - 19/07/2023
# v1.1 - 27/07/2023
# v1.2 - 03/08/2023
# v1.3 - 12/09/2023
# v1.4 - 14/09/2023 - Gestione stato NAND->DELE + setScenario
# v1.5 - 22/05/2024 - Aggiunto controllo STRNO
from com.terna.common.log import Logger
from com.terna.common.maximo import MXUtils
from com.terna.common.string import StringUtils
from com.terna.integration.service import IntegrationService
from com.terna.integration.sapconnector import SapConnectorScenario
from psdi.mbo import SqlFormat, MboConstants
from psdi.server import MXServer
from psdi.iface.mos import ConversionUtil
from java.util.regex import Pattern

log = Logger.getLogger("X3SRVPRIMILVL")

statusMapping = {"PROG": "INAT", "CANT": "INAT", "DATT": "INAT", "ESER": "DATT", "NAND": "DELE"}

def beforeMboData(ocx):
	data = ocx.getData()
	# Traccia il messaggio
	log.info(data.getDataAsString())
	data.setAsCurrent(data.getPrimaryObject())
	
	# Legge i dati dal messaggio
	bukrs = data.getCurrentDataAsString("BUKRS")
	city = data.getCurrentDataAsString("CITY")
	country = data.getCurrentDataAsString("COUNTRY")
	eqfnr = data.getCurrentDataAsString("EQFNR")
	gewrk = data.getCurrentDataAsString("GEWRK")
	inbdt = data.getCurrentDataAsString("INBDT") # Si aspetta una data in formato ISO
	if inbdt != None:
		inbdt = ConversionUtil.stringToDate(inbdt)
	ingrp = data.getCurrentDataAsString("INGRP")
	kostl = data.getCurrentDataAsString("KOSTL")
	kokrs = data.getCurrentDataAsString("KOKRS")
	msgrp = data.getCurrentDataAsString("MSGRP")
	name1 = data.getCurrentDataAsString("NAME1")
	name2 = data.getCurrentDataAsString("NAME2")
	pltxt = data.getCurrentDataAsString("PLTXT")
	postcode1 = data.getCurrentDataAsString("POSTCODE1")
	region = data.getCurrentDataAsString("REGION")
	street = data.getCurrentDataAsString("STREET")
	strno = data.getCurrentDataAsString("STRNO")
	sttxu = data.getCurrentDataAsString("STTXU")
	swerk = data.getCurrentDataAsString("SWERK")
	tplnr = data.getCurrentDataAsString("TPLNR")	zzsocp = data.getCurrentDataAsString("ZZSOCP")
	fltyp = data.getCurrentDataAsString("FLTYP")
	if fltyp == "L":
		assetType = "LINEA"
		regex = "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]$"
	elif fltyp == "Z":
		assetType = "STAZIONE"
		regex = "^3[0-6][0-9]{3}[A-Z]$"
	else:
		ocx.error("x3terna", "ErrorMsg", ["FLTYP must be either L or Z"])
	# Verifica l'strno
	pattern = Pattern.compile(regex)
	matcher = pattern.matcher(strno)
	if not matcher.find():
		ocx.error("x3terna", "ErrorMsg", ["%s is not a valid strno"%strno])
	
	# Carica la X3SAPASSET
	sapAssetSet = MXUtils.getMboSet("X3SAPASSET")
	sqf = SqlFormat("sapuid = :1")
	sqf.setObject(1, "X3SAPASSET", "SAPUID", tplnr)
	sapAssetSet.setWhere(sqf.format())
	if sapAssetSet.isEmpty():
		# Nuovo primo livello
		log.info("Create first level: " + strno)
		sapAsset = sapAssetSet.add()
		sapAsset.setValue("rootstrno", strno)
		sapAsset.setValue("sapclass", assetType)
		sapAsset.setValue("saplevel", 1)
		sapAsset.setValue("sapuid", tplnr)
		sapAsset.setValue("strno", strno)
		sapAsset.setValue("sapobject", "S")
		sapAsset.setValue("rootassettype", fltyp)
	else:
		# SAP Asset esistente
		log.info("Update first level: " + strno)
		sapAsset = sapAssetSet.getMbo(0)
		
	# Carica l'asset di primo livello
	assetSet = sapAsset.getMboSet("$FIRSTLVLASSET", "ASSET", "(assetnum, siteid) in (select objectnum, siteid from X3SAPTOMAXOBJECT where sapassetid = :x3sapassetid and hierarchypath in ('" + assetType + "'))")
	if assetSet.isEmpty():
		# Nuovo Asset
		asset = assetSet.add()
		# Valori di default TODO Chiedere se ci sono altre informazioni di default che vanno settate
		asset.setValue("CLASSSTRUCTURE.hierarchypath", assetType)
		# Valori dal messaggio SAP
		asset.setValue("x3sapid", strno)
		asset.setValue("status", "INAT", MboConstants.NOACCESSCHECK)
		# Crea l'indirizzo e setta EQ1 se è una stazione
		if assetType == "STAZIONE":
			asset.setValue("eq1", strno)
			serviceAddressSet = MXUtils.getMboSet("SERVICEADDRESS")
			serviceAddressSet.setMXTransaction(assetSet.getMXTransaction())
			serviceAddress = serviceAddressSet.add()
			serviceAddress.setValue("orgid", asset.getString("orgid"))
			asset.setValue("saddresscode", serviceAddress.getString("addresscode"), MboConstants.NOVALIDATION)
		# Crea il link tra la Sede Tecnica e l'Asset
		sapToMaxObjectSet = sapAsset.getMboSet("$SAPTOMAX", "X3SAPTOMAXOBJECT", "sapassetid = :x3sapassetid and hierarchypath = '" + assetType + "'")
		sapToMaxObject = sapToMaxObjectSet.add()
		sapToMaxObject.setValue("hierarchypath", assetType)
		sapToMaxObject.setValue("maxobject", "ASSET")
		sapToMaxObject.setValue("objectnum", asset.getString("assetnum"))
		sapToMaxObject.setValue("rootassetnum", asset.getString("assetnum"))
		sapToMaxObject.setValue("sapassetid", sapAsset.getLong("x3sapassetid"))
	else:
		# Asset esistente
		asset = assetSet.getMbo(0)
		serviceAddress = asset.getMboSet("SERVICEADDRESS").getMbo(0)
	# Verifica se lo stato attuale è INAT
	if asset.getString("status") == "INAT":
		# L'asset può essere aggiornato nella sua totalità
		log.info(StringUtils.format("SAP is owner: updating data for {0} {1} ({2})", assetType, strno, asset.getString("assetnum")))
		asset.setValue("description", pltxt)
		asset.setValue("installdate", inbdt)
		asset.setValue("x3eqfnr", eqfnr)
		asset.setValue("x3bukrs", bukrs)
		asset.setValue("x3ingrp", ingrp)
		asset.setValue("x3swerk", swerk)
		asset.setValue("x3gewrk", gewrk)
		
		# Aggiorna i dati dell'indirizzo se è una stazione
		if assetType == "STAZIONE":
			serviceAddress.setValue("description", name1, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("addressline2", name2, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("city", city, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("country", country, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("regiondistrict", region, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("streetaddress", street, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("postalcode", postcode1, MboConstants.NOACCESSCHECK)
		
		if sttxu not in statusMapping:
			ocx.error("x3terna", "ErrorMsg", [StringUtils.format("STTXU {0} is not valid a valid status", sttxu)])
		if statusMapping[sttxu] != "INAT":
			log.info("Changing asset status to: " + statusMapping[sttxu])
			asset.changeStatus(statusMapping[sttxu], MXServer.getMXServer().getDate(), "Modificato da SAP")
	else:
		log.info(StringUtils.format("Maximo is owner: updating only accounting data for {0} {1} ({2})", assetType, strno, asset.getString("assetnum")))
	# Azioni sempre ammesse sull'asset (add/update)
	asset.setValue("x3zzsocp", zzsocp)
	asset.setValue("x3msgrp", msgrp)
	asset.setValue("x3kostl", kostl)
	asset.setValue("x3kokrs", kokrs)
	
	# Salva
	IntegrationService.getService().saveNoSync(sapAssetSet)"
X3CONALTRATERNASTESSAPALIFIC,Custom rule handler del connettore Maximo-SAP per la caratteristica  PMT_ALTRA_TERNA_STESSA_PALIFIC della Linea,Draft,"# X3CONALTRATERNASTESSA_PALIFIC
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_ALTRA_TERNA_STESSA_PALIFIC.
# Calcola il codice sede tecnica 
# v1.0 - 16/07/2023
# v2.0 - 27/09/2023 Gestione di più valori in output
# v2.1 - 26/02/20024 Gestione della caratteristica nel caso in cui fosse null
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning
from java.util import ArrayList

## prende la linea
assetSet = sapAsset.getMboSet("$LIN_ALTRE_TERNE_ASSOC","ASSET","(ASSETNUM,SITEID) IN (SELECT OBJECTNUM,SITEID FROM X3SAPTOMAXOBJECT WHERE SAPASSETID=:X3SAPASSETID AND HIERARCHYPATH = 'LINEA')")

countAsset = assetSet.count()

# list of lines
mappedRawValue = ArrayList()

if countAsset == 0 :
    # non esiste la linea
	ruleHandler.addSimpleWarningForRule(mappingRule)

else:
    linea = assetSet.moveFirst()
    
    relatedLineaSet = linea.getMboSet("$RELLINEA", "ASSET" ,"ASSETNUM IN (SELECT a2.SOURCEASSETNUM FROM ASSETLOCRELATION a1, ASSETLOCRELATION a2 WHERE a1.SOURCEASSETNUM = :assetnum AND a1.SOURCEASSETNUM != a2.SOURCEASSETNUM AND a1.TARGETASSETNUM = a2.TARGETASSETNUM)")
    
    countRelLinea = relatedLineaSet.count()
    if countRelLinea > 0 :
        relatedLinea = relatedLineaSet.moveFirst()
        while relatedLinea != None:
            mappedRawValue.add(relatedLinea.getString("X3SAPID"))
            relatedLinea=relatedLineaSet.moveNext()
    else:
        mappedRawValue.add("")