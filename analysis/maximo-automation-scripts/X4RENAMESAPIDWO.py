if launchPoint=="X4RENAMESAPIDWO":
    newsapid=mbo.getString("SAPID_DEST")
    oldsapid=mbo.getString("SAPID_ORIG")
    assetnum=mbo.getString("ASSETNUM")
    
    conKey = mbo.getThisMboSet().getUserInfo().getConnectionKey();
    con = mbo.getThisMboSet().getMboServer().getDBConnection(conKey)
    s = con.createStatement()
    val = "update WORKORDER set WOEQ9 = '"+str(newsapid)+"' where assetnum ='"+str(assetnum)+"' and istask=0 and historyflag=0"
    rs = s.execute(val)
    con.commit()
    
elif launchPoint=="X4RENAMESAPIDASSET":
    if mbo.isModified("X3SAPID"):
        newsapid=mbo.getString("X3SAPID")
        assetnum=mbo.getString("ASSETNUM")
        
        conKey = mbo.getThisMboSet().getUserInfo().getConnectionKey();
        con = mbo.getThisMboSet().getMboServer().getDBConnection(conKey)
        s = con.createStatement()
        val = "update WORKORDER set WOEQ9 = '"+str(newsapid)+"' where assetnum ='"+str(assetnum)+"' and istask=0 and historyflag=0"
        rs = s.execute(val)
        con.commit()
        
elif launchPoint=="X4RENAMESAPIDASSETLOC":
    if onadd:
        newsapid=mbo.getString("X3SAPID")
        assetnum=mbo.getString("TARGETASSETNUM")
        
        checkExtWo=mbo.getMboSet("$WOANC$","WORKORDER","istask=0 and historyflag=0 and assetnum in (select assetnum from asset where assetnum=:targetassetnum or ancestor=:targetassetnum)")
        
        if not checkExtWo.isEmpty():
            conKey = mbo.getThisMboSet().getUserInfo().getConnectionKey();
            con = mbo.getThisMboSet().getMboServer().getDBConnection(conKey)
            s = con.createStatement()
            val = "update WORKORDER set WOEQ9 = '"+str(newsapid)+"' where istask=0 and historyflag=0 and assetnum in (select assetnum from asset where assetnum='"+str(assetnum)+"' or ancestor='"+str(assetnum)+"')"
            rs = s.execute(val)
            con.commit()"
XQCONALTRATERNASTESSAPALIFIC,Custom rule handler del connettore Maximo-SAP per la caratteristica  PMT_ALTRA_TERNA_STESSA_PALIFIC della Linea,Draft,"# X3CONALTRATERNASTESSA_PALIFIC
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_ALTRA_TERNA_STESSA_PALIFIC.
# Calcola il codice sede tecnica 
# v1.0 - 16/07/2023
# v2.0 - 27/09/2023 Gestione di più valori in output
# v2.1 - 26/02/20024 Gestione della caratteristica nel caso in cui fosse null
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning
from java.util import ArrayList

## prende la linea
assetSet = sapAsset.getMboSet("$LIN_ALTRE_TERNE_ASSOC","ASSET","(ASSETNUM,SITEID) IN (SELECT OBJECTNUM,SITEID FROM XQEAMSAPTOMAXOBJECTS3 WHERE SAPASSETID=:X3SAPASSETID AND HIERARCHYPATH = 'LINEA')")
assetSet.reset()
countAsset = assetSet.count()

# list of lines
mappedRawValue = ArrayList()

if countAsset == 0 :
    # non esiste la linea
	ruleHandler.addSimpleWarningForRule(mappingRule)

else:
    linea = assetSet.moveFirst()
    
    relatedLineaSet = linea.getMboSet("$RELLINEA", "ASSET" ,"ASSETNUM IN (SELECT a2.SOURCEASSETNUM FROM ASSETLOCRELATION a1, ASSETLOCRELATION a2 WHERE a1.SOURCEASSETNUM = :assetnum AND a1.SOURCEASSETNUM != a2.SOURCEASSETNUM AND a1.TARGETASSETNUM = a2.TARGETASSETNUM)")
    relatedLineaSet.reset()
    countRelLinea = relatedLineaSet.count()
    if countRelLinea > 0 :
        relatedLinea = relatedLineaSet.moveFirst()
        while relatedLinea != None:
            mappedRawValue.add(relatedLinea.getString("X3SAPID"))
            relatedLinea=relatedLineaSet.moveNext()
    else:
        mappedRawValue.add("")"
X3RELATEDASSETNEWSAVE,X3RELATEDASSETNEWSAVE,Draft,"# X3RELATEDASSETNEWSAVE
#


from com.terna.common.string import StringUtils
from psdi.mbo import MboConstants
from psdi.server import MXServer
from psdi.webclient.system.runtime import WebClientRuntime

COMPARTO_CLASSES = ("1371", "1674")
TIPO_COMPARTO_SPEC = "STZ_TIPO_COMPARTO"


def getClassificationName(classStructure, classStructureId):
    """Restituisce il nome leggibile della classificazione."""
    classificationName = classStructure.getString("CLASSIFICATIONID")
    if StringUtils.isEmpty(classificationName):
        classificationName = classStructure.getString("DESCRIPTION")
    if StringUtils.isEmpty(classificationName):
        classificationName = classStructureId
    return classificationName


def escapeSql(value):
    """Esegue l'escape minimo dei valori ALN usati nelle where clause."""
    return str(value).replace("'", "''")


def isStationClass(classStructureId, userInfo):
    """Verifica che la radice della gerarchia di classificazione sia STAZIONE."""
    classAncestorSet = MXServer.getMXServer().getMboSet(
        "CLASSANCESTOR", userInfo)
    try:
        escapedClassStructureId = escapeSql(classStructureId)
        classAncestorSet.setWhere(
            "classstructureid = '" + escapedClassStructureId +
            "' and ancestorclassid = 'STAZIONE'" +
            " and hierarchylevels = (" +
            "select max(ca.hierarchylevels) from classancestor ca" +
            " where ca.classstructureid = '" + escapedClassStructureId + "')"
        )
        classAncestorSet.reset()
        return classAncestorSet.moveFirst() is not None
    finally:
        classAncestorSet.close()


def isClassSpecValueAllowed(classStructure, assetAttrId, value, userInfo, orgId, siteId):
    """Verifica che il dominio ALN della caratteristica contenga il valore."""
    classSpecSet = classStructure.getMboSet("CLASSSPEC")
    classSpec = classSpecSet.moveFirst()
    while classSpec is not None:
        if classSpec.getString("ASSETATTRID").strip().upper() == assetAttrId:
            domainId = classSpec.getString("DOMAINID")

            # Senza dominio la caratteristica non limita i valori ALN.
            if StringUtils.isEmpty(domainId):
                return True

            alnDomainSet = MXServer.getMXServer().getMboSet("ALNDOMAIN", userInfo)
            try:
                alnDomainSet.setWhere(
                    "domainid = '" + escapeSql(domainId) +
                    "' and value = '" + escapeSql(value) +
                    "' and (orgid is null or orgid = '" + escapeSql(orgId) +
                    "') and (siteid is null or siteid = '" + escapeSql(siteId) + "')"
                )
                alnDomainSet.reset()
                return alnDomainSet.moveFirst() is not None
            finally:
                alnDomainSet.close()

        classSpec = classSpecSet.moveNext()

    classificationName = getClassificationName(
        classStructure, classStructure.getString("CLASSSTRUCTUREID"))
    service.error(
        "x3terna",
        "ErrorMsg",
        [
            "La caratteristica " + assetAttrId +
            " non e' presente sulla classificazione " + classificationName + "."
        ])


def getAssetSpec(asset, assetAttrId, classStructureId):
    """Restituisce la specifica richiesta, bloccando il salvataggio se manca."""
    specSet = asset.getMboSet("ASSETSPECCLASS")
    currentSpec = specSet.moveFirst()
    while currentSpec is not None:
        if currentSpec.getString("ASSETATTRID").strip().upper() == assetAttrId:
            return currentSpec
        currentSpec = specSet.moveNext()

    classificationName = classStructureId
    try:
        classificationName = asset.getString("CLASSSTRUCTURE.CLASSIFICATIONID")
    except:
        pass
    service.error( "x3terna", "ErrorMsg", ["La caratteristica " + assetAttrId + " non e' presente sulla classificazione " + classificationName + " (" + classStructureId + ")."] )

def setStationSapId(asset):
    """Genera X3SAPIDEQ tramite autokey e lo riporta in X3SAPID."""
    sapId = asset.getString("X3SAPIDEQ")
    if StringUtils.isEmpty(sapId):
        # Applica l'autokey definita sull'attributo X3SAPIDEQ.
        asset.getMboValue("X3SAPIDEQ").autoKey()
        sapId = asset.getString("X3SAPIDEQ")

    if StringUtils.isEmpty(sapId):
        service.error( "x3terna", "ErrorMsg", ["Impossibile generare il SAP ID per il nuovo asset."] )

    asset.setValue("X3SAPID", sapId, MboConstants.NOACCESSCHECK)

currentMbo = None
try:
    currentMbo = mbo
except:
    pass

if currentMbo is None:
    try:
        currentMbo = mboset.getMbo(0)
    except:
        pass

if currentMbo is None:
    service.error( "x3terna", "ErrorMsg", ["Impossibile recuperare il record corrente della dialog."] )

referenceAsset = currentMbo.getOwner()
if referenceAsset is None or referenceAsset.getName().upper() != "ASSET":
    service.error( "x3terna", "ErrorMsg", ["Impossibile recuperare l'asset di partenza dalla dialog."] )

userInfo = currentMbo.getUserInfo()
assetNum = referenceAsset.getString("ASSETNUM")
classStructureId = referenceAsset.getString("CLASSSTRUCTUREID")
relClassStructureId = currentMbo.getString("RELCLASSSTRUCTUREID")
parentAssetNum = referenceAsset.getString("PARENT")
compartoFlow = classStructureId in COMPARTO_CLASSES
stationFlow = isStationClass(classStructureId, userInfo)

if currentMbo.isNull("INSTALLDATE"):
    service.error( "x3terna", "ErrorMsg", ["La data di installazione e' obbligatoria per poter proseguire."] )
if StringUtils.isEmpty(relClassStructureId):
    service.error( "x3terna", "ErrorMsg", ["Inserire una classificazione di asset relazionato prima di procedere."] )

configSet = None
assetSet = None
itemSet = None
classStructureSet = None

try:
    # Entrambi i flussi devono usare una coppia configurata nella tabella.
    configSet = MXServer.getMXServer().getMboSet("X3RELATEDASSET", userInfo)
    configSet.setWhere(StringUtils.format("classstructureid = '{0}' and relclassstructureid = '{1}'",classStructureId,relClassStructureId))
    configSet.reset()
    configMbo = configSet.moveFirst()
    if configMbo is None:
        service.error("x3terna","ErrorMsg",["Nessuna configurazione X3RELATEDASSET per le classificazioni "+ classStructureId + " e " + relClassStructureId + "."])
    relAssetAttrId = configMbo.getString("RELASSETATTRID")
    if StringUtils.isEmpty(relAssetAttrId):
        service.error( "x3terna", "ErrorMsg", ["La configurazione X3RELATEDASSET non indica la caratteristica " + "di collegamento."] )
    # La descrizione del ramo Stazioni deriva direttamente dalla
    # classificazione, poiche' per queste classi non e' configurato un ITEM.
    classStructureSet = MXServer.getMXServer().getMboSet( "CLASSSTRUCTURE", userInfo )
    classStructureSet.setWhere( StringUtils.format( "classstructureid = '{0}'", relClassStructureId ) )
    classStructureSet.reset()
    classStructureMbo = classStructureSet.moveFirst()
    if classStructureMbo is None:
        service.error( "x3terna", "ErrorMsg", ["Classificazione " + relClassStructureId + " non trovata."] )

    itemnum = None
    if not compartoFlow and not stationFlow:
        # Flusso Linee Req_U_001: conserva la creazione basata su ITEM.
        itemSet = MXServer.getMXServer().getMboSet("ITEM", userInfo)
        itemSet.setWhere( StringUtils.format( "classstructureid = '{0}'", relClassStructureId ) )
        itemSet.reset()
        itemMbo = itemSet.moveFirst()
        if itemMbo is None:
            service.error( "x3terna", "ErrorMsg", ["Nessun item configurato per la classificazione " + relClassStructureId + "."] )
        itemnum = itemMbo.getString("ITEMNUM")

    tipoComparto = None
    tipoCompartoAllowed = True
    continueCreation = True
    if compartoFlow:
        # La validazione avviene prima di aggiungere il nuovo MBO ASSET.
        tipoCompartoSpec = getAssetSpec( referenceAsset, TIPO_COMPARTO_SPEC, classStructureId )
        tipoComparto = tipoCompartoSpec.getString("ALNVALUE")

        # Un tipo comparto nullo non deve essere validato e non genera warning.
        if not StringUtils.isEmpty(tipoComparto):
            tipoCompartoAllowed = isClassSpecValueAllowed(
                classStructureMbo,
                TIPO_COMPARTO_SPEC,
                tipoComparto,
                userInfo,
                referenceAsset.getString("ORGID"),
                referenceAsset.getString("SITEID"))

        if not tipoCompartoAllowed:
            classificationName = getClassificationName(
                classStructureMbo, relClassStructureId)
            warningMessage = (
                "La classificazione <b>" + classificationName +
                "</b> non puo' avere come tipo comparto <b>" + tipoComparto +
                "</b>. Continuare?")

            # Il click sulla message box viene restituito sullo stesso evento
            # UI che ha eseguito dialogok.
            webClientSession = service.webclientsession()

            messageReturn = (
                webClientSession.getCurrentEvent().getMessageReturn())
            if messageReturn < 0:
                service.yncerror(
                    "x3terna", "YesNoMsg", [warningMessage])

            continueCreation = (
                messageReturn == WebClientRuntime.MSG_BTNYES)

    if continueCreation:
        # Il set usa la stessa transazione del non persistente: asset e ASSETSPEC
        # vengono quindi salvati atomicamente.
        assetSet = MXServer.getMXServer().getMboSet("ASSET", userInfo)
        assetSet.setMXTransaction(currentMbo.getMXTransaction())
        assetSet.setInsertOrg(referenceAsset.getString("ORGID"))
        assetSet.setInsertSite(referenceAsset.getString("SITEID"))
        newAsset = assetSet.add()
        newAsset.setValue("PARENT", parentAssetNum)
        newAsset.setValue("CLASSSTRUCTUREID", relClassStructureId)
        newAsset.setValue("INSTALLDATE", currentMbo.getDate("INSTALLDATE"))

        if compartoFlow or stationFlow:
            newAsset.setValue("DESCRIPTION", classStructureMbo.getString("DESCRIPTION"))
            if stationFlow and not referenceAsset.isNull("X3SAPID"):
                setStationSapId(newAsset)
            if (compartoFlow and tipoCompartoAllowed and
                    not StringUtils.isEmpty(tipoComparto)):
                tipoCompartoSpec = getAssetSpec( newAsset, TIPO_COMPARTO_SPEC, relClassStructureId )
                tipoCompartoSpec.setValue("ALNVALUE", tipoComparto)
        else:
            newAsset.setValue("ITEMNUM", itemnum)
            newAsset.setValue("DESCRIPTION", itemnum)

        relatedSpec = getAssetSpec( newAsset, relAssetAttrId, relClassStructureId )
        relatedSpec.setValue( "TABLEVALUE", assetNum, MboConstants.NOVALIDATION )

        # Il salvataggio del set ASSET persiste anche ASSETSPECCLASS nella stessa
        # transazione associata al record della dialog.
        assetSet.save()

    service.closeDialog()
finally:
    if configSet:
        configSet.close()
    if itemSet:
        itemSet.close()
    if classStructureSet:
        classStructureSet.close()
    if assetSet:
        assetSet.close()"
XQCONSOSTINIZIOCOMPETENZA,,Draft,"# X3CONSOSTINIZIOCOMPETENZA
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_SOST_INIZIO_COMPETENZA.
# Calcola Codice sede tecnica SAP del sostegno o estremo cavo con POSNR minore (escluse derivazioni e asset decommisioned)
# v1.0 - 12\07\2023
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning
from psdi.server import MXServer


## recupero il mapping dell'asset di primo livello (linea)
mappingSet = sapAsset.getMboSet("$XQEAMSAPTOMAXOBJECTS3SOSTINCOMP$", "XQEAMSAPTOMAXOBJECTS3", "sapassetid=:x3sapassetid and hierarchypath = 'LINEA'")
mapping = mappingSet.getMbo(0)

if mapping == None :
	# non esiste il mapping verso la linea
	ruleHandler.addSimpleWarningForRule(mappingRule, "Linea %s not found"%sapAsset.getString("ROOTSTRNO"))
else:
	# sostegni aerei e estremo cavo della linea non dismesse
	assetRelationSet = mapping.getMboSet("$ASSETLOCRELATION$", "ASSETLOCRELATION", "ASSETLOCRELATIONID IN (SELECT ASSETLOCRELATIONID FROM ASSETLOCRELATION a, ASSET b WHERE a.TARGETASSETNUM = b.ASSETNUM AND a.SOURCEASSETNUM = :OBJECTNUM AND NOT REGEXP_LIKE(a.X3SEQUENCE, '^[A-Za-z]') AND b.CLASSSTRUCTUREID IN (SELECT CLASSSTRUCTUREID FROM CLASSSTRUCTURE c WHERE c.CLASSIFICATIONID IN ('SOSTEGNO AEREO','ESTREMO CAVO')) AND b.status NOT IN (SELECT VALUE FROM SYNONYMDOMAIN WHERE DOMAINID = 'LOCASSETSTATUS' AND MAXVALUE = 'DECOMMISSIONED'))")
	assetRelationSet.setOrderBy("X3SEQUENCE")
	assetRelation = assetRelationSet.getMbo(0)

	if assetRelation == None :
		# non esistono sostegni o estremo Cavo in questa linea
		ruleHandler.addSimpleWarningForRule(mappingRule, "SOSTEGNO AEREO/ESTREMO CAVO not found for LINEA %s"%sapAsset.getString("ROOTSTRNO"))
	else:
		mappedRawValue = assetRelation.getString("X3SAPID")"
XQCONSTZRIFASSET,,Draft,"'''
X3CONSTZRIFASSET
Richiamato dallo Step 3 del connettore Maximo-SAP.
Calcola il valore della caratteristica EAM STZ_RIF_ASSET.
v1.0 - 12/04/2024
'''
from com.terna.common.string import StringUtils
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning

# Get SAPCLASS
sapClass = mappingRule.getString("SAPCLASS")

# Get STZ_RIF_ASSET specification for given SAPCLASS
if sapClass == "ISOLATORE PASSANTE OLIO/ARIA":
	assetSpecSet = sapAsset.getMboSet("$x3isopass$","ASSETSPEC","(siteid, assetnum) in (select siteid, objectnum from XQEAMSAPTOMAXOBJECTS3 where sapassetid=:x3sapassetid and classificationid = 'ISOLATORE PASSANTE OLIO/ARIA') and assetattrid='STZ_RIF_ASSET'")
elif sapClass == "ISOLATORE PASSANTE OLIO/OLIO":
	assetSpecSet = sapAsset.getMboSet("$x3isopass$","ASSETSPEC","(siteid, assetnum) in (select siteid, objectnum from XQEAMSAPTOMAXOBJECTS3 where sapassetid=:x3sapassetid and classificationid = 'ISOLATORE PASSANTE OLIO/OLIO') and assetattrid='STZ_RIF_ASSET'")
elif sapClass == "ISOLATORE PASSANTE OLIO/SF6":
	assetSpecSet = sapAsset.getMboSet("$x3isopass$","ASSETSPEC","(siteid, assetnum) in (select siteid, objectnum from XQEAMSAPTOMAXOBJECTS3 where sapassetid=:x3sapassetid and classificationid = 'ISOLATORE PASSANTE OLIO/SF6') and assetattrid='STZ_RIF_ASSET'")
elif sapClass == "COMANDO PER SEZIONATORE AT BLINDATO":
	assetSpecSet = sapAsset.getMboSet("$x3comsezblind$","ASSETSPEC","(siteid, assetnum) in (select siteid, objectnum from XQEAMSAPTOMAXOBJECTS3 where sapassetid=:x3sapassetid and classificationid = 'COMANDO PER SEZIONATORE AT BLINDATO') and assetattrid='STZ_RIF_ASSET'")
elif sapClass == "COMANDO PER INTERRUTTORE AT BLINDATO":
	assetSpecSet = sapAsset.getMboSet("$x3comintblind$","ASSETSPEC","(siteid, assetnum) in (select siteid, objectnum from XQEAMSAPTOMAXOBJECTS3 where sapassetid=:x3sapassetid and classificationid = 'COMANDO PER INTERRUTTORE AT BLINDATO') and assetattrid='STZ_RIF_ASSET'")

assetSpecMbo = assetSpecSet.getMbo(0)
if assetSpecMbo == None:
	ruleHandler.addSimpleWarningForRule(mappingRule)
else:
	if StringUtils.isEmpty(assetSpecMbo.getString("TABLEVALUE")):
		pass
	else:
		# Get ASSETNUM from TABLEVALUE
		assetNum = assetSpecMbo.getString("TABLEVALUE")
		sapAssetMboSet = sapAsset.getMboSet("$RIF_XQEAMSAPASSETS3","XQEAMSAPASSETS3","STRNO LIKE '%%' || (SELECT X3SAPID FROM ASSET WHERE ASSETNUM = '%s' AND SITEID = '%s') AND SUBSTR(PARENTSTRNO,1,14) = SUBSTR(:parentstrno,1,14)"%(assetNum, assetSpecMbo.getString("siteid")))
		sapAssetMbo = sapAssetMboSet.getMbo(0)
		if sapAssetMbo == None:
			service.error("x3terna", "ErrorMsg", ["SAP Asset not found"])
		sapUid = sapAssetMbo.getString("sapuid")
		if StringUtils.isEmpty(sapUid):
			service.error("x3terna", "ErrorMsg", ["SAP Asset has not been created yet on SAP - SAPUID is null"])
		else:
			mappedRawValue = sapUid"
XQCONTERMINALEFINE,,Draft,"# X3CONTERMINALEFINE
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_TERMINALE_FINE.
# Calcolare il valore dell'ultimo estremo cavo seguendo il verso convenzionale della linea.
# v1.0 - 12\07\2023
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning
from com.terna.common.maximo import MXUtils

if sapAsset.getBoolean("dummy"):
	strno = sapAsset.getString("strno")
	childSet = sapAsset.getMboSet("$child$", "ASSETLOCRELATION", "regexp_like(x3sapid, '"+ strno + "-[RH]$')")
	childSet.reset()
	childMbo = childSet.getMbo(0)

	if childMbo == None:
		ruleHandler.addSimpleWarningForRule(mappingRule, "Estremo cavo non trovato")
	elif childSet.getMbo(1) != None:
		ruleHandler.addSimpleWarningForRule(mappingRule, "La tratta cavo ha più di un elemento associato")
	else:
		mappedRawValue = childMbo.getString("x3sapid")
else:
	# prendo tratta cavo
	trattaCavoRelSet = sapAsset.getMboSet("$trattaassoc$", "ASSETLOCRELATION", "(siteid, sourceassetnum, targetassetnum) in (select siteid, rootassetnum, objectnum from XQEAMSAPTOMAXOBJECTS3 where sapassetid = :x3sapassetid and hierarchypath = 'LINEA\TRATTA CAVO')")
	trattaCavoRelSet.reset()
	trattaCavoRelMbo = trattaCavoRelSet.getMbo(0)

	if trattaCavoRelMbo == None:
		# la relazione non esiste
		ruleHandler.addSimpleWarningForRule(mappingRule)
	else:
		sequenceId = trattaCavoRelMbo.getString("x3sequence")
		if sequenceId == "":
			ruleHandler.addSimpleWarningForRule(mappingRule, "X3SEQUENCE is null for TRATTA CAVO" + trattaCavoRelMbo.getString("x3sapid"))
		else:
			# prendo targetasset per lo stato
			trattaCavoMbo = trattaCavoRelMbo.getMboSet("TARGETASSET").getMbo(0)
			#statusTrattaCavo = MXUtils.getInternalValue(trattaCavoMbo, "status")
			statusTrattaCavo = trattaCavoMbo.getString("status")

			if statusTrattaCavo == "DEMO":
				strno = sapAsset.getString("strno")
				childSet = sapAsset.getMboSet("$child$", "ASSETLOCRELATION", "regexp_like(x3sapid, '"+ strno + "-[RH]$')")
				childSet.reset()
				childMbo = childSet.getMbo(0)

				if childMbo == None:
					ruleHandler.addSimpleWarningForRule(mappingRule, "Estremo cavo non trovato")
				elif childSet.getMbo(1) != None:
					ruleHandler.addSimpleWarningForRule(mappingRule, "La tratta cavo ha più di un elemento associato")
				else:
					mappedRawValue = childMbo.getString("x3sapid")
			else:
				sqlWhere = "sourceassetnum = :sourceassetnum and siteid = :siteid and (x3sapid like '%-R' or x3sapid like '%-H') and x3sequence > :x3sequence"
				# verifica se esiste un asset di tipo ESTREMO CAVO legato alla tratta
				estremoCavoRelSet = trattaCavoRelMbo.getMboSet("$estremisuccessivi$", "ASSETLOCRELATION", sqlWhere)
				estremoCavoRelSet.setOrderBy("x3sequence asc")
				estremoCavoRelMbo = estremoCavoRelSet.getMbo(0)

				if estremoCavoRelMbo == None:
					ruleHandler.addSimpleWarningForRule(mappingRule, "there are no following ESTREMO CAVO")
				else:
					estremoCavoMbo = estremoCavoRelMbo.getMboSet("TARGETASSET").getMbo(0)
					statusEstremoCavo = MXUtils.getInternalValue(estremoCavoMbo, "status")
					# se stato dell'estremo è alias di DECOMMISSIONED
					if statusEstremoCavo == "DECOMMISSIONED":
						# prendo estremo con sequence successiva
						i = 0
						while i < estremoCavoRelSet.getSize():
							i += 1
							estremoCavoRelMbo = estremoCavoRelSet.getMbo(i)
							if estremoCavoRelMbo == None:
								mappedRawValue = ""
								break
							else:
								estremoCavoMbo = estremoCavoRelMbo.getMboSet("TARGETASSET").getMbo(0)
								statusEstremoCavoSucc = MXUtils.getInternalValue(estremoCavoMbo, "status")
								if statusEstremoCavoSucc != "DECOMMISSIONED":
									mappedRawValue = estremoCavoRelMbo.getString("x3sapid")
									break
								else:
									continue
					else:
						mappedRawValue = estremoCavoRelMbo.getString("x3sapid")"
XQCONTERMINALEINIZIO,,Draft,"# X3CONTERMINALEINIZIO
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_TERMINALE_INIZIO.
# Calcolare il valore del primo ESTREMO CAVO seguendo il verso convenzionale della linea.
# v1.0 - 12/07/2023
# v1.1 - 30/10/2023 - Gestione ST fittizie e derivazioni
# V1.2 - 02/04/2025 - Gestione OP_252
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning
from com.terna.integration.service import IntegrationService
from com.terna.common.maximo import MXUtils

# gestione tratta cavo fittizia
if sapAsset.getBoolean("dummy"):
    strno = sapAsset.getString("strno")
    childSet = sapAsset.getMboSet("$child$", "ASSETLOCRELATION", "regexp_like(x3sapid, '"+ strno + "-[RH]$')")
    childSet.reset()
    childMbo = childSet.getMbo(0)

    if childMbo == None:
        ruleHandler.addSimpleWarningForRule(mappingRule, "Estremo cavo non trovato")
    elif childSet.getMbo(1) != None:
        ruleHandler.addSimpleWarningForRule(mappingRule, "La tratta cavo ha più di un elemento associato")
    else:
        mappedRawValue = childMbo.getString("x3sapid")
else:
    # Prende le relazioni verso la tratta cavo
    trattaCavoRelSet = sapAsset.getMboSet("$trattaassoc$", "ASSETLOCRELATION", "(siteid, sourceassetnum, targetassetnum) in (select siteid, rootassetnum, objectnum from XQEAMSAPTOMAXOBJECTS3 where sapassetid = :x3sapassetid and hierarchypath = 'LINEA\TRATTA CAVO')")
    trattaCavoRelSet.reset()
    trattaCavoRelMbo = trattaCavoRelSet.getMbo(0)

    if trattaCavoRelMbo == None:
        # relazione non trovata
        ruleHandler.addSimpleWarningForRule(mappingRule)
    else:
        sequenceId = trattaCavoRelMbo.getString("x3sequence")
        if sequenceId == "":
            ruleHandler.addSimpleWarningForRule(mappingRule, "X3SEQUENCE is null for TRATTA CAVO" + trattaCavoRelMbo.getString("x3sapid"))
        else:
            trattaCavoMbo = trattaCavoRelMbo.getMboSet("TARGETASSET").getMbo(0)
            #statusTrattaCavo = MXUtils.getInternalValue(trattaCavoMbo, "status")
            statusTrattaCavo = trattaCavoMbo.getString("status")

            if statusTrattaCavo == "DEMO":
                strno = sapAsset.getString("strno")
                childSet = sapAsset.getMboSet("$child_demo$", "ASSETLOCRELATION", "regexp_like(x3sapid, '"+ strno + "-[RH]$')")
                childSet.reset()
                childMbo = childSet.getMbo(0)

                if childMbo == None:
                    ruleHandler.addSimpleWarningForRule(mappingRule, "Estremo cavo non trovato")
                elif childSet.getMbo(1) != None:
                    ruleHandler.addSimpleWarningForRule(mappingRule, "La tratta cavo ha più di un elemento associato")
                else:
                    mappedRawValue = childMbo.getString("x3sapid")
            else:
                # OP_252 : Se è valorizzato l'attributo LIN_ESTREMO_INIZIO restituisce il valore altrimenti lo calcola
                linTermInizio = None
                trattaStrno = sapAsset.getString("strno")
                lineaStrno = trattaStrno[:7]
                assetSpecSet = trattaCavoMbo.getMboSet("ASSETSPEC")
                assetSpecSet.setWhere("ASSETATTRID='LIN_ESTREMO_INIZIO' and (section = '" + lineaStrno + "' or section is null)")
                assetSpecSet.setOrderBy("section asc")
                assetSpecSet.reset()
                assetSpec = assetSpecSet.getMbo(0)
                if assetSpec != None:
                    linTermInizio = assetSpec.getString("ALNVALUE")
                    if linTermInizio != "":
                        # L'attributo LIN_TERMINALE_INIZIO è valorizzato
                        mappedRawValue = linTermInizio
                    else:
                        # L'attributo LIN_TERMINALE_INIZIO non è valorizzato
                        sqlWhere = "sourceassetnum = :sourceassetnum and siteid = :siteid and (x3sapid like '%-R' or x3sapid like '%-H') and x3sequence < :x3sequence"
                        # se fa parte di una derivazione aggiunge alla clausola where la restrizione su X3SEQUENCE che iniziano o finiscono
                        # con la lettera della derivazione
                        if sequenceId[0].isalpha():
                            sqlWhere += " and (x3sequence like '" + sequenceId[0] + "%' or x3sequence like '%" + sequenceId[0] + "')"

                        # recupero gli estremi precedenti ordinati in modo decrescente
                        estremoCavoRelSet = trattaCavoRelMbo.getMboSet("$estremiprecedenti$", "ASSETLOCRELATION", sqlWhere)
                        estremoCavoRelSet.setOrderBy("x3sequence desc")

                        # recupero il primo estremo non DECOMMISSIONED
                        mappedRawValue = ""
                        i = 0
                        estremoCavoRelMbo = estremoCavoRelSet.getMbo(0)
                        while estremoCavoRelMbo != None:
                            estremoCavoMbo = estremoCavoRelMbo.getMboSet("TARGETASSET").getMbo(0)
                            statusEstremoCavo = MXUtils.getInternalValue(estremoCavoMbo, "status")
                            if statusEstremoCavo != "DECOMMISSIONED":
                                mappedRawValue = estremoCavoRelMbo.getString("x3sapid")
                                break
                            i += 1
                            estremoCavoRelMbo = estremoCavoRelSet.getMbo(i)

                        if estremoCavoRelMbo == None:
                            # l'estremo cavo non è stato trovato
                            ruleHandler.addSimpleWarningForRule(mappingRule, "there are no previous ESTREMO CAVO")
                else:
                    ruleHandler.addSimpleWarningForRule(mappingRule, "Can't find attribute LIN_ESTREMO_INIZIO on TRATTA CAVO " + trattaCavoRelMbo.getString("x3sapid"))"
XQCONTRONCOISP,,Draft,"# X3CONTRONCOISP
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_TRONCO_ISP.
# Calcola Codice sede tecnica SAP del Tronco di Ispezione.
# v1.0 - 17/07/2023
# v1.1 - 20/09/2023
# v1.2 - 04/10/2023 - Gestione ammazzettamento
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning

# prende sapclass
sapClass = mappingRule.getString("SAPCLASS")

# prende la specifica che contiene il tronconum
if sapClass=="CAMPATA":
	tronchiSpecSet = sapAsset.getMboSet("$TRONCHISPEC","ASSETSPEC","(SITEID,ASSETNUM) IN (SELECT o.SITEID, o.OBJECTNUM FROM XQEAMSAPTOMAXOBJECTS3 o, ASSET a WHERE a.assetnum = o.objectnum and a.siteid = o.siteid and a.x3dadoppiaterna = 0 and o.SAPASSETID = :X3SAPASSETID AND o.HIERARCHYPATH = 'LINEA\CAMPATA AEREA\CONDUTTORI') AND ASSETATTRID = 'LIN_TRONCO_ISPEZIONE'")
else:
	tronchiSpecSet = sapAsset.getMboSet("$TRONCHISPEC","ASSETSPEC","(SITEID,ASSETNUM) IN (SELECT SITEID, OBJECTNUM FROM XQEAMSAPTOMAXOBJECTS3 WHERE SAPASSETID = :X3SAPASSETID AND HIERARCHYPATH = 'LINEA\TRATTA CAVO\CAVI') AND ASSETATTRID = 'LIN_TRONCO_ISPEZIONE'")

countTronchi = tronchiSpecSet.count()
if countTronchi == 0 :
	# non è stato trovato l'attributo nelle ASSETSPEC
	ruleHandler.addSimpleWarningForRule(mappingRule)
else:
	troncoSpec = tronchiSpecSet.moveFirst()
	tronchiSet=troncoSpec.getMboSet("$TRONCO","X3TRONCHI",":TABLEVALUE=TRONCONUM")
	tronchiCount = tronchiSet.count()
	if tronchiCount > 0:
		troncoMbo = tronchiSet.moveFirst()
		mappedRawValue = troncoMbo.getString("SAPID")"
X4EVALCOND,Valutazione condizioni caratteristiche tecniche,Draft,"# ============================================
# LIBRARY: check condizioni asset vs CARD
# ============================================

class X4EvalCondLib():

    def __init__(self, service):
        self.service = service

    # --------------------------------------------
    # ENTRY POINT PUBBLICO
    # --------------------------------------------
    def check(self, userInfo, assetnum, tipo, idElemento):
        return self.check_condizioni(userInfo, assetnum, tipo, idElemento)

    # --------------------------------------------
    # ENTRY POINT INTERNO
    # --------------------------------------------
    def check_condizioni(self, userInfo, assetnum, tipo, idElemento):
        """
        tipo: DIFFOR | INTERF | MISURA
        idElemento: id difformità / interferenza / misura
        """

        from psdi.server import MXServer

        mxServer = MXServer.getMXServer()

        assetSet = None
        cardSet = None

        try:
            assetSet = self._get_asset_set(mxServer, userInfo, assetnum)

            asset = assetSet.getMbo(0)
            if asset is None:
                self.service.log(
                    "X4EVALCOND - asset non trovato assetnum=[" +
                    str(assetnum) + "]"
                )
                return False

            # Costruisce una mappa unica contenente:
            # - attributi ASSETSPEC
            # - campi diretti ASSET
            assetValueMap = self._build_asset_value_map(asset)

            self.service.log(
                "X4EVALCOND - assetnum=[" + str(assetnum) + "]"
            )
            self.service.log(
                "X4EVALCOND - assetValueMap=[" +
                str(assetValueMap) + "]"
            )

            cardSet = self._get_card_set(
                mxServer,
                userInfo,
                tipo,
                idElemento
            )

            if cardSet is None:
                self.service.log(
                    "X4EVALCOND - cardSet None tipo=[" +
                    str(tipo) +
                    "] idElemento=[" +
                    str(idElemento) + "]"
                )
                return True

            self.service.log(
                "X4EVALCOND - tipo=[" + str(tipo) +
                "] idElemento=[" + str(idElemento) +
                "] card count=[" + str(cardSet.count()) + "]"
            )

            if cardSet.isEmpty():
                self.service.log(
                    "X4EVALCOND - cardSet vuoto, ritorno True"
                )
                return True

            result = self._eval_condizioni(
                cardSet,
                assetValueMap
            )

            self.service.log(
                "X4EVALCOND - result=[" + str(result) + "]"
            )

            return result

        finally:
            if cardSet is not None:
                cardSet.close()

            if assetSet is not None:
                assetSet.close()

    # --------------------------------------------
    # ASSET SET
    # --------------------------------------------
    def _get_asset_set(self, mxServer, userInfo, assetnum):

        assetSet = mxServer.getMboSet("ASSET", userInfo)
        assetSet.setWhere(
            "assetnum = '" + str(assetnum) + "'"
        )
        assetSet.reset()

        return assetSet

    # --------------------------------------------
    # COSTRUZIONE MAPPA VALORI ASSET
    # --------------------------------------------
    def _build_asset_value_map(self, asset):
        """
        Costruisce un'unica mappa:

        {
            ATTRIBUTE_CARD: valore
        }

        Gli attributi standard vengono letti da ASSETSPEC.
        Gli attributi speciali vengono letti direttamente da ASSET.
        """

        valueMap = {}

        # ============================================
        # 1. ATTRIBUTI DA ASSETSPEC
        # ============================================

        specSet = asset.getMboSet("ASSETSPEC")

        i = 0
        while True:
            spec = specSet.getMbo(i)

            if spec is None:
                break

            attr = spec.getString("ASSETATTRID")

            if attr is not None and attr.strip() != "":
                attr = attr.strip()

                val = None

                if not spec.isNull("ALNVALUE"):
                    val = spec.getString("ALNVALUE")

                elif not spec.isNull("NUMVALUE"):
                    val = spec.getDouble("NUMVALUE")

                valueMap[attr] = val

            i += 1

        # ============================================
        # 2. ATTRIBUTI LETTI DIRETTAMENTE DA ASSET
        # ============================================

        directAssetFields = {
            "SOCIETA": "X3BUKRS",
            "SORT": "X3EQFNR",
            "IMPIANTO": "X3MSGRP",
            "CENTRO_LAVORO_RESPONSABILE": "X3GEWRK",
            "IN_FUNZIONE_DA": "INSTALLDATE",
            "GRUPPO_RESPONSABILE_PIANIF_MANUTEN": "X3INGRP",
            "CONTROLLING_AREA": "X3KOKRS",
            "CENTRO_DI_COSTO": "X3KOSTL",
            "SEDE_TECNICA_DESCRIZIONE": "DESCRIPTION",
            "SEDE_TECNICA_CODICE": "X3SAPID",
            "STATO": "STATUS",
            "DIVISIONE_UBICAZIONE": "X3SWERK",
            "SOCIETA_PROPRIETARIA": "X3ZZSOCP",
            "CODICE_COSTRUTTORE": "X3COSTRUTTORECOMPONENTE",
            "MODELLO": "X3FUNZIONEELEMENTO",
            "RIFERIMENTO_FASE": "X3RIFERIMENTOFASE",
            "TIPO_COMPONENTE": "X3TIPOCOMPONENTE"
        }

        for attr in directAssetFields:

            field = directAssetFields[attr]

            if asset.isNull(field):
                valueMap[attr] = None
                
            elif attr == "IN_FUNZIONE_DA":
                valueMap[attr] = asset.getDate(field)
            else:
                valueMap[attr] = asset.getString(field)

        return valueMap

    # --------------------------------------------
    # CARD GENERICA
    # --------------------------------------------
    def _get_card_set(
        self,
        mxServer,
        userInfo,
        tipo,
        idElemento
    ):

        if tipo == "DIFFOR":
            obj = "X4CARDIFFOR"
            idField = "X4CATDIFFORID"
            tabOrig = "X4CATDIFFOR"
            idLett = "IDDIFFORMITA"

        elif tipo == "INTERF":
            obj = "X4CARDINTERF"
            idField = "X4CATINTERFID"
            tabOrig = "X4CATINTERF"
            idLett = "IDINTERFERENZA"

        elif tipo == "MISURA":
            obj = "X4CARDMISURE"
            idField = "X4CATMISUREID"
            tabOrig = "X4CATMISURE"
            idLett = "IDMISURA"

        else:
            return None

        cardSet = mxServer.getMboSet(obj, userInfo)

        cardSet.setWhere(
            "%s in (select %s from %s where %s = '%s')" %
            (
                idField,
                idField,
                tabOrig,
                idLett,
                idElemento
            )
        )

        cardSet.reset()

        return cardSet

    # --------------------------------------------
    # VALUTAZIONE CONDIZIONI
    # --------------------------------------------
    def _eval_condizioni(
        self,
        cardSet,
        assetValueMap
    ):

        condByAttr = {}

        # Logica:
        # - AND tra attributi diversi
        # - OR tra valori dello stesso attributo

        i = 0
        while True:
            cond = cardSet.getMbo(i)

            if cond is None:
                break

            attr = cond.getString("ATTRIBUTE")
            val = cond.getString("VALUE")

            if attr is None or attr.strip() == "":
                return False

            attr = attr.strip()

            if attr not in condByAttr:
                condByAttr[attr] = []

            condByAttr[attr].append(val)

            i += 1

        # AND tra attributi diversi
        for attr in condByAttr:

            values = condByAttr[attr]
            match_found = False

            # OR tra condizioni dello stesso ATTRIBUTE
            for val in values:

                if self._check_single_condition(
                    assetValueMap,
                    attr,
                    val
                ):
                    match_found = True
                    break

            # Nessun valore del gruppo corrisponde
            if not match_found:
                return False

        return True

    # --------------------------------------------
    # CHECK SINGOLA CONDIZIONE
    # --------------------------------------------
    def _check_single_condition(
        self,
        assetValueMap,
        attr,
        val
    ):

        if attr not in assetValueMap:
            return False

        assetVal = assetValueMap[attr]

        if assetVal is None:
            return False

        if val is None:
            return False

        # ------------------------------------------------------------
        # CASO 1: valore asset NUMERICO (letto da ASSETSPEC.NUMVALUE).
        # Il VALUE della card e' testo copiato dalla maschera come
        # VARCHAR2 e puo' contenere il separatore come '.' o ','. Provo
        # entrambe le letture: separatore decimale OPPURE separatore
        # delle migliaia, confrontando col numero reale dell'asset.
        # ------------------------------------------------------------
        if isinstance(assetVal, float):
            target = float(assetVal)
            for cand in self._card_number_candidates(val):
                if abs(target - cand) < 0.0000001:
                    return True
            return False

        # ------------------------------------------------------------
        # CASO 2: valore asset TESTUALE (ALNVALUE, campi diretti).
        # Comportamento invariato: prima confronto numerico "classico"
        # (virgola = decimale), poi fallback su confronto stringa esatto.
        # Le stringhe con virgole / caratteri speciali cadono sul
        # confronto stringa tramite l'except.
        # ------------------------------------------------------------
        try:
            assetNum = float(
                assetVal.replace(",", ".").strip()
            )

            condNum = float(
                val.replace(",", ".").strip()
            )

            return abs(assetNum - condNum) < 0.0000001

        except:
            pass

        # Fallback confronto stringa
        return (
            assetVal.strip() == val.strip()
        )

    # --------------------------------------------
    # LETTURE NUMERICHE POSSIBILI DEL VALUE CARD
    # --------------------------------------------
    def _card_number_candidates(self, val):
        """
        Restituisce le possibili letture numeriche del VALUE testuale
        della card, tenendo conto che il separatore ('.' o ',') puo'
        essere decimale o delle migliaia:
            a) virgola = separatore decimale  -> '1,5' = 1.5 ; '1,600' = 1.6
            b) virgola = separatore migliaia  -> '1,600' = 1600
            c) formato IT completo            -> '1.234,56' = 1234.56
        """
        s = str(val).strip()

        cands = []
