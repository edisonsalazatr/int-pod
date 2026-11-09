from psdi.mbo import Mbo
from java.util.regex import Pattern

arrclassliv2 = ['CAMPATA AEREA','TRATTA CAVO']
arrclassliv3 = ['SOSTEGNO AEREO','ESTREMO CAVO']
sapid = ""
REGEX_1_LVL = "^2[0-5][A-Z0-9-]{3}[A-Z][0-9]$"
REGEX_2_LVL = "^[0-9]{3}[A-Z0-9-]{0,5}$"
REGEX_3_LVL = "^[S|H|K|R][0-9]?$"


def isCorrectLvl1(stringa):        
    if not Pattern.matches(REGEX_1_LVL, stringa):
        service.error("x3err_format_liv1", "x3err_format_liv1", None)        

def isCorrectLvl2(stringa):
    if not Pattern.matches(REGEX_2_LVL, stringa):
        service.error("x3err_format_liv2", "x3err_format_liv2", None)
        
def isCorrectLvl3(stringa):
    if not Pattern.matches(REGEX_3_LVL, stringa):
        service.error("x3err_format_liv3", "x3err_format_liv3", None)        
             
def stringFiller(msg, filler, align, width):
    return '{message:{fill}{align}{width}}'.format(message=msg,
                                                   fill=filler,
                                                   align=align,
                                                   width=width)



if interactive and vi_sapid is not None:
    if vi_classstrac in arrclassliv2:
        sapid = str(vi_sapid)
        firstLvl    = sapid[:7] 
        secondLvl   = sapid[8:]
        isCorrectLvl1(firstLvl)
        isCorrectLvl2(secondLvl)
        if len(secondLvl) < 8:
            secondLvl=stringFiller(secondLvl, '-', '<', 8)
            new_sapid = firstLvl + '-' + secondLvl
            mbo.setValue("X3SAPID", new_sapid)
            #Save the changes
            mbo.getThisMboSet().save()
            mbo.getThisMboSet().close()
            
    elif vi_classstrac in arrclassliv3:
        
        new_sapid = None
        sapid = str(vi_sapid)
        
        idxLastLvl  = sapid.rfind("-") if sapid.rfind("-") == 16 else len(sapid)
        firstLvl    = sapid[:7] 
        secondLvl   = sapid[8:idxLastLvl]
        thirdLvl    = sapid[idxLastLvl+1:]
        
        #check first level
        isCorrectLvl1(firstLvl)
        
        #check second level
        isCorrectLvl2(secondLvl)
        if len(secondLvl) < 8:
            secondLvl=stringFiller(secondLvl, '-', '<', 8)
            new_sapid = firstLvl + '-' + secondLvl
                        
        #check third level
        if len(thirdLvl) > 0:
            isCorrectLvl3(thirdLvl)
            if new_sapid is not None:
                new_sapid = firstLvl + '-' + secondLvl + '-' + thirdLvl   
        else:
            if vi_classstrac == 'SOSTEGNO AEREO':
                new_sapid = firstLvl + '-' + secondLvl + '-S'
                
        if new_sapid is not None:
            #Save the changes
            mbo.setValue("X3SAPID", new_sapid)
            mbo.getThisMboSet().save()
            mbo.getThisMboSet().close()"
X3CONTERMINALEFINE,Custom rule handler del connettore MAXIMO-SAP per la caratteristica PMT_TERMINALE_FINE della tratta cavo,Draft,"# X3CONTERMINALEFINE
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
	trattaCavoRelSet = sapAsset.getMboSet("$trattaassoc$", "ASSETLOCRELATION", "(siteid, sourceassetnum, targetassetnum) in (select siteid, rootassetnum, objectnum from x3saptomaxobject where sapassetid = :x3sapassetid and hierarchypath = 'LINEA\TRATTA CAVO')")
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
X3CREATEDER,Gestisce creazioni derivazioni,Draft,"from psdi.server import MXServer 
from psdi.mbo import MboConstants
from psdi.util.logging import MXLoggerFactory
from psdi.webclient.beans.location import ApplyIASBean 
from com.terna.asset import SapIdGenerator
from com.terna.integration.service import IntegrationService
from com.terna.integration.service import Scenario
from com.terna.integration.sapconnector import SapConnectorScenario
from com.terna.integration.sapconnector import SapConnectorScenarioOperation
from com.terna.integration.sapconnector.step1.handler.touch import AssetRelationAddArg
from java.util import Calendar

logger = MXLoggerFactory.getLogger("maximo.script")
assetnum = mboset.getMbo(0).getOwner().getString("ASSETNUM")
AssetCreati = []

def get_max_x3sequence():
    assetlocrelationSet = thismbo.getMboSet("$ASSETLOCRELSEQ$", "ASSETLOCRELATION", "sourceassetnum='"+str(assetnum)+"'")
    assetlocrelationSet.setOrderBy("x3sequence DESC")
    max_x3sequence_mbo = assetlocrelationSet.getMbo(0)

    if not max_x3sequence_mbo.isNull("x3sequence"):
        max_x3sequence = max_x3sequence_mbo.getString("x3sequence")
        numeric_part = int(max_x3sequence[1:])
        alphabetic_part = max_x3sequence[0]
    else:
        # Default values if no existing records are found
        numeric_part = 100
        alphabetic_part = 'A'

    return numeric_part, alphabetic_part
        

def get_new_letter_sequence(num_elementi):
    max_numeric_part, max_alphabetic_part = get_max_x3sequence()
    
    # Controllo per verificare se last_sequence ha una lettera o è vuota
    if not max_alphabetic_part or not max_alphabetic_part.isalpha():
        max_alphabetic_part = 'A'
    else:
        last_letter = max_alphabetic_part[-1]
        max_alphabetic_part = max_alphabetic_part[:-1] + chr(ord(last_letter) + 1)
    
    sequenza = []
    for i in range(num_elementi):
        numeric_part = max_numeric_part + i + 1
        formatted_numeric_part = "{:0>6}".format(numeric_part)
        sequenza.append("{}{}".format(max_alphabetic_part, formatted_numeric_part))
    
    return sequenza, max_alphabetic_part


def get_next_x3posnr(num_elementi, lettera, parte_da_sostegno=False):
    """
    Ogni nuova derivazione (nuova lettera) riparte SEMPRE da 00.
    Formato: lettera + 2 cifre.

    AEREA (che parte da CAMPATA): coppie CAMPATA/SOSTEGNO a partire
           dall'indice 0 -> A00 A00 A02 A02 A04 A04 ...
    CAVO, oppure AEREA che parte da un ESTREMO CAVO (quindi inizia dal
           SOSTEGNO): il primo elemento è da solo, poi coppie a partire
           dall'indice 1 -> A00, A02 A02, A04 A04, A06 A06 ...

    L'elemento di ORIGINE (punto di partenza) non passa da qui:
    resta gestito da aggiungi_lettera_al_posnr più sotto nello script.
    """
    all_x3posnr = []
    base_numeric = 0
    usa_offset_iniziale = (tipoTratta == "CAVO") or parte_da_sostegno

    for i in range(num_elementi):
        if usa_offset_iniziale:
            gruppo = (i + 1) // 2
        else:
            gruppo = i // 2
        new_digit = base_numeric + gruppo * 2
        formatted_numeric_part = "{:0>2}".format(new_digit)
        all_x3posnr.append("{}{}".format(lettera, formatted_numeric_part))

    return all_x3posnr

    
def get_next_x3sapid(i, num_elementi):
    assetlocrelationX3SAPIDSet = thismbo.getMboSet("$ASSETLOCRELSAPID$", "ASSETLOCRELATION", "x3sapid is not null and sourceassetnum='"+str(assetnum)+"'")
    assetlocrelationX3SAPIDSet.setOrderBy("x3sapid DESC")
    assetlocrelationX3SAPIDSet.reset()
    max_x3sapid_mbo = assetlocrelationX3SAPIDSet.moveFirst()
    logger.debug("itemnum="+str(itemnum))
    if max_x3sapid_mbo is not None and not max_x3sapid_mbo.isNull("x3sapid"):
        if itemnum == "CAMPATA-S" or itemnum == "CAMPATA-D" or itemnum == "TRATTA CAVO":
            max_x3sapid = max_x3sapid_mbo.getString("x3sapid")
            calc_x3sapid = int(max_x3sapid[8:11]) + 1
            formatted_nextsapid = "{:0>3}".format(calc_x3sapid)
            next_x3sapid = str(max_x3sapid[0:8]) + str(formatted_nextsapid) + "-----"
        elif itemnum == "SOSTEGNO-S" or itemnum == "SOSTEGNO-D" or itemnum == "ESTREMO CAVO GIUNTI" or itemnum == "ESTREMO CAVO TERMINALI":
            max_x3sapid = max_x3sapid_mbo.getString("x3sapid")
            if itemnum == "SOSTEGNO-S" or itemnum == "SOSTEGNO-D":
                next_x3sapid = max_x3sapid + "-S"
            elif itemnum == "ESTREMO CAVO GIUNTI":
                next_x3sapid = max_x3sapid + "-R"
            elif itemnum == "ESTREMO CAVO TERMINALI":
                next_x3sapid = max_x3sapid + "-H"
    return next_x3sapid

def calcola_posnr(posnr_base, is_multipla):
    """
    Restituisce il POSNR corretto.
    Se is_multipla è True (derivazione multipla) e posnr_base termina con una lettera,
    la lettera viene sostituita con '*'.
    Negli altri casi viene restituito posnr_base invariato.
    """
    if is_multipla and posnr_base and posnr_base[-1].isalpha():
        return posnr_base[:-1] + "*"
    else:
        return posnr_base

def aggiungi_lettera_al_posnr(posnr_base, lettera):
    """
    Formato base: lettera + 2 cifre (3 caratteri), es. 'A02'.
    In caso di derivazione annidata si accoda semplicemente la nuova lettera,
    ottenendo lettera + 2 cifre + lettera (4 caratteri), es. 'A02B'.
    """
    return posnr_base + lettera
        
##Funzione per settare installdate sui figli creati        
def setInstallDateOnAllChildren(childrenSet, installDate):
    if not installDate or childrenSet is None:
        return
    child = childrenSet.moveFirst()
    while child:
        try:
            # Set data installazione sul figlio
            child.setValue("INSTALLDATE", installDate, MboConstants.NOACCESSCHECK)
            # Ricorsione sui figli del figlio
            grandChildrenSet = child.getMboSet("ASSETCHILDREN")
            if grandChildrenSet is not None and not grandChildrenSet.isEmpty():
                setInstallDateOnAllChildren(grandChildrenSet, installDate)
        except Exception as e:
            logger.error(u"""Errore setInstallDateOnAllChildren su asset %s (site %s)""" % (child.getString("ASSETNUM"), child.getString("SITEID")),e)
        child = childrenSet.moveNext()

########################################        
thismbo = mboset.getMbo(0)
derivazione = thismbo.getDouble("DERIVAZIONE")
tipoTratta = thismbo.getString("tipotratta")
installdate = thismbo.getDate("INSTALLDATE")
if installdate:
    cal = Calendar.getInstance()
    cal.add(Calendar.HOUR_OF_DAY, 1)
    currentDateTime = cal.getTime()
    if installdate > currentDateTime:
        thismbo.setValueNull("installdate")
        service.error("labor", "YearInFuture")
if not installdate:
    service.error("x3terna", "ErrorMsg", ["Valori obbligatori mancanti"])
if derivazione:
    sequenza_partenza = thismbo.getString("x3sequence")
    num_elementi = thismbo.getInt("quantity")
    sequenza_ritorno = thismbo.getString("x3sequenceend")
    if sequenza_ritorno:
        sequenza_ritorno = sequenza_ritorno.strip()

    # --- INIZIO gestione derivazione multipla ---
    # BLOCCO DERIVAZIONE MULTIPLA OLTRE 3 LETTERE
    # Esempio: 000007ABC (3 lettere finali, OK), 000007ABCD (4 lettere finali, ERRORE)
    if (sequenza_partenza 
        and len(sequenza_partenza) >= 3 
        and sequenza_partenza[-1].isalpha() 
        and sequenza_partenza[-2].isalpha() 
        and sequenza_partenza[-3].isalpha()):
        service.error("x3createder", "x3derivazionemult", None)
    # Riconosci derivazione multipla: la sequenza di partenza termina con lettera
    isDerivazioneMultipla = sequenza_partenza and sequenza_partenza[-1].isalpha()
    # --- FINE gestione derivazione multipla ---

    if sequenza_partenza == sequenza_ritorno:
        service.error("x3createder", "x3sequenzenoval", None)
    if (sequenza_ritorno and not sequenza_ritorno[-1].isdigit()):
        service.error("x3createder", "x3sequenzenoval", None)
    if tipoTratta == "AEREA":
        itemuno = "CAMPATA-S"
        itemdue = "SOSTEGNO-S"
    elif tipoTratta == "CAVO":
        itemuno = "TRATTA CAVO"
        itemdue = "ESTREMO CAVO"
    #itemuno = thismbo.getString("ITEMNUM")
    #itemdue = thismbo.getString("ITEMNUM2")
    ingrp = thismbo.getString("X3INGRP")
    gewrk = thismbo.getString("X3GEWRK")
    firstLevelAsset = thismbo.getOwner()
    sourceRootAsset = firstLevelAsset.getString("ASSETNUM")
    siteId = firstLevelAsset.getString("SITEID")
    sapIdGenerator = SapIdGenerator(firstLevelAsset)
    # Calcola la sequenza (recuperato qui, prima del calcolo di num_elementi,
    # perché serve sapere subito se si parte da un ESTREMO CAVO)
    asset_selezionato_set = mboset.getMbo(0).getMboSet("$ASSETSEL$", "ASSETLOCRELATION", "sourceassetnum='" + assetnum + "' and x3sequence='" + sequenza_partenza + "' and siteid='" + siteId + "'")
    asset_selezionato = asset_selezionato_set.moveFirst()

    # Determina la classificazione dell'elemento di origine (serve per capire se una
    # derivazione AEREA parte da un ESTREMO CAVO, nel qual caso deve iniziare dal
    # SOSTEGNO e non dalla CAMPATA). Si usa CLASSSTRUCTUREID invece di itemnum
    # per coprire automaticamente TERMINALI e GIUNTI, ed eventuali nuovi item
    # che in futuro venissero aggiunti sotto la stessa classificazione.
    CLASSSTRUCTUREID_ESTREMO_CAVO = "1026"
    classstructureid_partenza = None
    if asset_selezionato is not None:
        target_assetnum_partenza = asset_selezionato.getString("targetassetnum")
        assetMboSetPartenza = MXServer.getMXServer().getMboSet("ASSET", thismbo.getUserInfo())
        assetMboSetPartenza.setWhere("siteid = '" + siteId + "' and assetnum = '" + target_assetnum_partenza + "'")
        assetMboSetPartenza.reset()
        assetMboPartenza = assetMboSetPartenza.getMbo(0)
        if assetMboPartenza is not None:
            classstructureid_partenza = assetMboPartenza.getString("CLASSSTRUCTUREID")
        assetMboSetPartenza.close()

    parte_da_sostegno = (tipoTratta == "AEREA" and classstructureid_partenza == CLASSSTRUCTUREID_ESTREMO_CAVO)

    #Faccio dei check sui dati
    if tipoTratta == "CAVO":
        # usa x3sequenceend (sequenza_ritorno) per decidere la parità e il conteggio
        if not sequenza_ritorno or thismbo.isNull("x3sequenceend"):
            # vogliamo un totale DISPARI: +1
            num_elementi = (num_elementi * 2) + 1
        else:
            num_elementi = num_elementi * 2

    elif tipoTratta == "AEREA":
        # anche per aerea usare x3sequenceend come riferimento
        if not sequenza_ritorno or thismbo.isNull("x3sequenceend"):
            if parte_da_sostegno:
                # si parte da un ESTREMO CAVO: quantity = numero di SOSTEGNI da
                # creare, con quantity-1 CAMPATE intermedie (mai il numero di campate).
                # Sequenza: SOSTEGNO-CAMPATA-SOSTEGNO-...-SOSTEGNO -> totale DISPARI
                num_elementi = (num_elementi * 2) - 1
            else:
                num_elementi = num_elementi * 2
        else:
            num_elementi = (num_elementi * 2) - 1

    # Il prolungamento è gestito da un'altra funzione/script: qui è SEMPRE derivazione normale
    isProlungamento = False
    sequenza, lettera = get_new_letter_sequence(num_elementi)

    asset_selezionato_set.close()
    #lettera=max_lettera()
    # Calcola POSNR: ogni nuova derivazione riparte sempre da 00 con la propria lettera
    all_x3posnr = get_next_x3posnr(num_elementi, lettera, parte_da_sostegno)
    #imposto i valori di x3sequence dei sostegni di inizio e fine
    assetinizio = mboset.getMbo(0).getMboSet("$ASSETINI$", "ASSETLOCRELATION", "sourceassetnum='"+str(assetnum)+"' and x3sequence='"+str(sequenza_partenza)+"' and siteid='"+str(siteId)+"'")
    if not assetinizio.isEmpty() and not isProlungamento:
        ai = assetinizio.getMbo(0)
        valx3seqini = str(ai.getString("x3sequence")) + str(lettera)
        ai.setValue("x3sequence", valx3seqini)
        sapidsostin = ai.getString("X3SAPID")
        campatasostini = ai.getMboSet("$CAMPINI$", "ASSETLOCRELATION", "sourceassetnum='"+str(assetnum)+"' and siteid='"+str(siteId)+"' and x3sapid='"+str(sapidsostin[:16])+"'")
        if not campatasostini.isEmpty():
            posnrcampini = campatasostini.getMbo(0).getString("X3POSNR")
            
            if len(posnrcampini) == 3:
                if isDerivazioneMultipla:
                    # Caso derivazione multipla: sostituisci lettera finale con '*'
                    final_posnrcampini = calcola_posnr(posnrcampini, True)
                else:
                    # Caso normale: aggiungi lettera, togli zero a sinistra se necessario
                    final_posnrcampini = aggiungi_lettera_al_posnr(posnrcampini, lettera)
                AssetCreati.append(campatasostini.getMbo(0).getString("TARGETASSETNUM"))
                campatasostini.getMbo(0).setValue("X3POSNR", final_posnrcampini)
                # Aggiorna SEMPRE il POSNR del punto di partenza (ai): non dipende dal
                # suffisso "-S" (valido solo per SOSTEGNO/AEREA). Per ESTREMO CAVO
                # (suffisso -H/-R) la query sottostante serve solo a raccogliere
                # l'asset per l'integrazione SAP, se esiste.
                ai.setValue("X3POSNR", final_posnrcampini)
                sostini = campatasostini.getMbo(0).getMboSet(
                    "$SOSTINI$", "ASSETLOCRELATION",
                    "sourceassetnum='"+str(assetnum)+"' and siteid='"+str(siteId)+"' and x3sapid='"+str(sapidsostin[:16])+"-S'")
                if not sostini.isEmpty():
                    AssetCreati.append(sostini.getMbo(0).getString("TARGETASSETNUM"))
            else:
                # Per posnr più lunghi o più corti: continua come prima
                if isDerivazioneMultipla:
                    final_posnrcampini = calcola_posnr(posnrcampini, True)
                else:
                    final_posnrcampini = aggiungi_lettera_al_posnr(posnrcampini, lettera)
                campatasostini.getMbo(0).setValue("X3POSNR", final_posnrcampini)
                # Aggiorna SEMPRE il POSNR del punto di partenza (ai), stesso motivo di sopra
                ai.setValue("X3POSNR", final_posnrcampini)
                AssetCreati.append(ai.getString("TARGETASSETNUM"))
        else:
            posnrsost = ai.getString("X3POSNR")
            AssetCreati.append(ai.getString("TARGETASSETNUM"))
            final_posnrsost = calcola_posnr(posnrsost + str(lettera), isDerivazioneMultipla)
            ai.setValue("X3POSNR", final_posnrsost)
    assetfine = thismbo.getMboSet("$ASSETIFIN$", "ASSETLOCRELATION", "sourceassetnum='"+str(assetnum)+"' and x3sequence='"+str(sequenza_ritorno)+"' and siteid='"+str(siteId)+"'")
    if not assetfine.isEmpty() and not thismbo.isNull("x3sequenceend"):
        af = assetfine.getMbo(0)
        #AssetCreati.append(af.getString("TARGETASSETNUM"))
        valx3seqfin = str(af.getString("x3sequence")) + str(lettera) + "Z"
        af.setValue("x3sequence", valx3seqfin)
    # Determina da dove iniziare
    if tipoTratta == "CAVO" and isProlungamento:
        num_elementi = thismbo.getInt("quantity") * 2
        prolung = True
    elif tipoTratta == "CAVO":
        if not sequenza_ritorno:
            num_elementi = (thismbo.getInt("quantity") * 2) + 1
        else:
            num_elementi = (thismbo.getInt("quantity") * 2) - 1
        prolung = False
    else:
        # AEREA: stessa formula usata più sopra per generare sequenza/posnr
        if not sequenza_ritorno:
            if parte_da_sostegno:
                num_elementi = (thismbo.getInt("quantity") * 2) - 1
            else:
                num_elementi = thismbo.getInt("quantity") * 2
        else:
            num_elementi = (thismbo.getInt("quantity") * 2) - 1
        prolung = False

    # inizializza baseSap per generazione SAPID in modo pulito
    baseSap = None
        
    for i in range(num_elementi):
        if tipoTratta == "AEREA":
            if parte_da_sostegno:
                itemnum = "SOSTEGNO-S" if i % 2 == 0 else "CAMPATA-S"
            else:
                itemnum = "CAMPATA-S" if i % 2 == 0 else "SOSTEGNO-S"
        elif tipoTratta == "CAVO":
            if prolung:
                idx = i + 1
                if idx == num_elementi:
                    itemnum = "ESTREMO CAVO TERMINALI"
                elif idx % 2 == 0:
                    itemnum = "ESTREMO CAVO GIUNTI"
                else:
                    itemnum = "TRATTA CAVO"
            else:
                # Se c'è una sequenza di ritorno, NON creare il terminale H finale:
                if sequenza_ritorno and not thismbo.isNull("x3sequenceend"):
                    if i == 0:
                        itemnum = "ESTREMO CAVO TERMINALI"
                    elif i == num_elementi - 1:
                        # rientro -> ultimo = GIUNTI (non TERMINALI)
                        itemnum = "ESTREMO CAVO GIUNTI"
                    elif i % 2 == 0:
                        itemnum = "ESTREMO CAVO GIUNTI"
                    else:
                        itemnum = "TRATTA CAVO"
                else:
                    # comportamento precedente (nessun ritorno)
                    if i == 0 or i == num_elementi - 1:
                        itemnum = "ESTREMO CAVO TERMINALI"
                    elif i % 2 == 0:
                        itemnum = "ESTREMO CAVO GIUNTI"
                    else:
                        itemnum = "TRATTA CAVO"

        assetSet = MXServer.getMXServer().getMboSet("ASSET", firstLevelAsset.getUserInfo())
        assetSet.setMXTransaction(firstLevelAsset.getMXTransaction())
        assetSet.setInsertOrg(firstLevelAsset.getString("orgid"))
        assetSet.setInsertSite(firstLevelAsset.getString("siteid"))
        
        secondLevelAsset = assetSet.add()
        secondLevelAssetNum = secondLevelAsset.getString("assetnum")
        secondLevelAsset.setValue("itemnum", itemnum)
        if installdate:
            secondLevelAsset.setValue("INSTALLDATE", installdate)
        if itemnum in ('SOSTEGNO-S', 'SOSTEGNO-D', 'CAMPATA-S', 'CAMPATA-D'):
            secondLevelAsset.setValue("description", itemnum[0:(len(itemnum)-2)])
        else:
            secondLevelAsset.setValue("description", itemnum)
        
        # create relationship
        relationshipAssetSet = firstLevelAsset.getMboSet("ASSETLOCRELATION")
        relationshipAsset = relationshipAssetSet.add()
        AssetCreati.append(secondLevelAsset.getString("assetnum"))
        relationshipAsset.setValue("sourceassetnum", firstLevelAsset.getString("assetnum"))
        relationshipAsset.setValue("targetassetnum", secondLevelAsset.getString("assetnum"), MboConstants.NOVALIDATION)
        relationshipAsset.setValue("assetrelationnum", "INCLUDES")
        
        # set x3sequence e posnr
        relationshipAsset.setValue("x3sequence", sequenza[i])
        # --- APPLICA calcola_posnr per la derivazione multipla ---
        posnr_finale = calcola_posnr(all_x3posnr[i], isDerivazioneMultipla)
        relationshipAsset.setValue("x3posnr", posnr_finale)

        # Generazione SAPID: usa sempre baseSap come source per suffissi
        if itemnum in ("CAMPATA-S", "CAMPATA-D", "TRATTA CAVO") or (itemnum == "ESTREMO CAVO TERMINALI" and i == 0):
            # creiamo una base pulita quando serve
            baseSap = sapIdGenerator.next()
            if itemnum == "ESTREMO CAVO TERMINALI" and i == 0:
                newSapId = baseSap + "-H"
            else:
                newSapId = baseSap
        elif itemnum in ("SOSTEGNO-S", "SOSTEGNO-D"):
            if not baseSap:
                baseSap = sapIdGenerator.next()
            newSapId = baseSap + "-S"
        elif itemnum == "ESTREMO CAVO GIUNTI":
            if not baseSap:
                baseSap = sapIdGenerator.next()
            newSapId = baseSap + "-R"
        elif itemnum == "ESTREMO CAVO TERMINALI":
            # non aggiungere -H finale se c'è sequenza di ritorno
            if sequenza_ritorno and not thismbo.isNull("x3sequenceend"):
                if not baseSap:
                    baseSap = sapIdGenerator.next()
                newSapId = baseSap
            else:
                if not baseSap:                       # riusa la base esistente se presente (allinea al -R precedente)
                    baseSap = sapIdGenerator.next()
                newSapId = baseSap + "-H"
        else:
            baseSap = sapIdGenerator.next()
            newSapId = baseSap

        # log per debug
        

        relationshipAsset.setValue("x3sapid", newSapId)
        secondLevelAsset.setValue("X3INGRP", ingrp, 11L)
        secondLevelAsset.setValue("X3GEWRK", gewrk, 11L)
        IntegrationService.getService().saveNoSync(relationshipAssetSet)
        IntegrationService.getService().saveNoSync(assetSet)
        
        assetSet = MXServer.getMXServer().getMboSet("ASSET", firstLevelAsset.getUserInfo())
        assetSet.setWhere("siteid = '" + firstLevelAsset.getString("siteid") + "' and assetnum = '" + secondLevelAssetNum + "'")
        assetSet.reset()
        
        secondLevelAsset = assetSet.getMbo(0)
        
        if secondLevelAsset:
            # setup item structure for the asset
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
            ###setta installdate
            #childrenSet = secondLevelAsset.getMboSet("ASSETCHILDREN")
            #setInstallDateOnAllChildren(secondLevelAsset.getMboSet("ASSETCHILDREN"), installdate)
            #IntegrationService.getService().saveNoSync(childrenSet)
            new3lassetSet = secondLevelAsset.getMboSet("ASSETCHILDREN")
            #setta installdate sui figli 
            setInstallDateOnAllChildren(new3lassetSet, installdate)
            new3lasset = new3lassetSet.moveFirst()
            while new3lasset:
                itemnum = new3lasset.getString("itemnum")
                if itemnum == "ATTACCO CONDUTTORE" or itemnum == "CONDUTTORI":
                    new3lasset.setValue("x3rootassetnum", firstLevelAsset.getString("assetnum"))
                elif itemnum == "FUNI DI GUARDIA":
                    fdg4Set = new3lasset.getMboSet("$FDG4$", "ASSET", "parent = :assetnum and siteid = :siteid and itemnum='FUNE DI GUARDIA'")
                    fdg4 = fdg4Set.getMbo(0)
                    fdg4SpecSet = fdg4.getMboSet("$LIN_TIPO_FUNE_GUARDIA$", "ASSETSPEC", "assetnum = :assetnum and siteid=:siteid and assetattrid='LIN_RIF_FUNE'")
                    if not fdg4SpecSet.isEmpty():
                        fdg4 = fdg4SpecSet.getMbo(0)
                        fdg4.setValue("SECTION", firstLevelAsset.getString("x3sapid"), MboConstants.NOACCESSCHECK)
                new3lasset = new3lassetSet.moveNext()
            IntegrationService.getService().saveNoSync(new3lassetSet)
        assetSet.close()
    for asset in AssetCreati:
        assetCreatiMboSet = MXServer.getMXServer().getMboSet("ASSETLOCRELATION", MXServer.getMXServer().getSystemUserInfo())
        assetCreatiMboSet.setWhere("SITEID='" + siteId + "' and SOURCEASSETNUM='" + sourceRootAsset + "' and targetassetnum = '" + asset + "'")
        assetCreatiMboSet.reset()
        args = AssetRelationAddArg()
        args.relation = assetCreatiMboSet.getMbo(0)
        scenario = Scenario(SapConnectorScenario.TOUCH)
        scenario.callOperation(SapConnectorScenarioOperation.TOUCH_ADD_ASSET_RELATION, args)
        scenario.save()
        assetCreatiMboSet.close()"
X3ETLBLACKLISTCON,Rest Script to set non black listed lines for connectore,Draft,"#X3ETLBLACKLISTCON
from psdi.server import MXServer
from psdi.mbo import SqlFormat
from psdi.mbo import MboConstants
from psdi.mbo import DBShortcut
from psdi.util import MXException
from java.lang import Exception as JException
from java.sql import SQLException
from com.terna.integration.sapconnector import SapConnectorScenario
from com.terna.integration.sapconnector import SapConnectorScenarioOperation
from com.terna.integration.sapconnector.step1.handler.rebindall import RebindAllArg
from com.terna.integration.service import IntegrationService

import time

def log(nivel, descripcion, numrows, error, notas):
    logSet = MXServer.getMXServer().getMboSet("X3SCRIPTLOG", MXServer.getMXServer().getSystemUserInfo());
    NewLog = logSet.add()
    NewLog.setValue("DESCRIPTION", nivel + " - " + descripcion)
    NewLog.setValue("X3SCRIPT", "X3ETLBLACKLISTCON")
    NewLog.setValue("TIMESTAMP", MXServer.getMXServer().getDate())
    NewLog.setValue("NUMROWS", numrows)
    NewLog.setValue("X3ERROR", error)
    NewLog.setValue("X3NOTES", notas)
    logSet.save()
    logSet.close()
    time.sleep(2)

mxServer = MXServer.getMXServer()
userInfo = mxServer.getSystemUserInfo()	
dbShortcut=DBShortcut()
dbShortcut.connect(mxServer.getSystemUserInfo().getConnectionKey())

loaded = request.getQueryParam("LOADED")

i = 0
log("INICIO", "ETL Blacklist Conectore", "", "", "ETL Blacklist Conectore")

sql = """
SELECT DISTINCT
    S.STRNO AS LineCode
FROM X3SEDITECHSAP S
LEFT JOIN (
    SELECT DISTINCT SUBSTR(STRNO, 1, 7) AS STRNO FROM X3SEDITECHSAP WHERE BLACKLIST IS NOT NULL AND LOADED = """+ str(loaded) +""" UNION
    SELECT DISTINCT SUBSTR(STRNO, 1, 7) AS STRNO FROM X3ESTREMOCAVO WHERE BLACKLIST IS NOT NULL AND LOADED = """+ str(loaded) +""" UNION
    SELECT DISTINCT SUBSTR(X3SAPIDETL, 1, 7) AS STRNO FROM X3GENLVL3NOSAP WHERE BLACKLIST IS NOT NULL AND LOADED = """+ str(loaded) +""" UNION
    SELECT DISTINCT SUBSTR(STRNO, 1, 7) AS STRNO FROM X3GENLVL4SAPISOLATORE WHERE BLACKLIST IS NOT NULL AND LOADED = """+ str(loaded) +""" UNION
    SELECT DISTINCT SUBSTR(X3SAPID, 1, 7) AS STRNO FROM X3GENLVL4NOSAP WHERE BLACKLIST IS NOT NULL AND LOADED = """+ str(loaded) +""" UNION
    SELECT DISTINCT SUBSTR(X3SAPID, 1, 7) AS STRNO FROM X3GENLVL4FROMATT WHERE BLACKLIST IS NOT NULL AND LOADED = """+ str(loaded) +"""
) B 
      ON (S.STRNO = B.STRNO)
WHERE S.RBNR = 'PMTSTLIN'
AND S.LOADED = """+ str(loaded) +"""
AND B.STRNO IS NULL
"""
lineCode = ""
try:
    sqfQuery = SqlFormat(sql)
    resultSet = dbShortcut.executeQuery(sqfQuery.format())
    
    while resultSet.next():
        try:
            lineCode = resultSet.getString("LineCode")
            args = RebindAllArg()
            assetSet = MXServer.getMXServer().getMboSet("ASSET", MXServer.getMXServer().getSystemUserInfo());
            assetSet.setWhere("X3SAPIDETL='"+ lineCode + "'")
            args.rootAsset = assetSet.getMbo(0)
            
            IntegrationService.getService().setScenario(SapConnectorScenario.REBIND_ALL)
            IntegrationService.getService().callScenarioOperation(SapConnectorScenarioOperation.REBIND_ALL, args)
            IntegrationService.getService().commitScenario()
            assetSet.close()
            
        except Exception as e:
            etlResult = 'ERROR JYTHON'
            etlResultDescription = e.message[0:99]
            log("ERRORE", "Errore JYTHON alla linea " + str(lineCode), "", etlResultDescription, "")
        except MXException as e:
            etlResult = 'ERROR MX'
            etlResultDescription = e.message[0:99]
            log("ERRORE", "Errore MX alla linea " + str(lineCode), "", etlResultDescription, "")
        except JException as e:
            etlResult = 'ERROR JAVA'
            etlResultDescription = e.message[0:99]
            log("ERRORE", "Errore JAVA alla linea " + str(lineCode), "", etlResultDescription, "")
        finally:
            try:
                if not assetSet.isClosed():
                    assetSet.close()
            finally:
                    if not assetSet.isClosed():
                        assetSet.close()
        i += 1
    resultSet.close()
    log("FIN", "Carga de atributos sobre activos provenientes de SAP", i, "", "Fin Carga de atributos sobre activos provenientes de SAP")
except SQLException as e:
    etlResult = 'ERROR SQL'
    etlResultDescription = e.message[0:99]
    responseBody = "Process ended with SQL ERROR for Line " + str(lineCode) + ", total processed: "+str(i)+". Error message: "+etlResultDescription
except Exception as e:
    etlResult = 'ERROR JYTHON'
    etlResultDescription = e.message[0:99]
    log("ERROR","JYTHON Error on script","",etlResultDescription,"")
    responseBody = "Process ended with JYTHON ERROR for Line " + str(lineCode) + ", total processed: "+str(i)+". Error message: "+etlResultDescription
except MXException as e:
    etlResult = 'ERROR MX'
    etlResultDescription = e.message[0:99]
    log("ERROR","MX Error on script","",etlResultDescription,"")
    responseBody = "Process ended with MX ERROR for Line " + str(lineCode) + ", total processed: "+str(i)+". Error message: "+etlResultDescription
except JException as e:
    etlResult = 'ERROR JAVA'
    etlResultDescription = e.message[0:99]
    log("ERROR","JAVA Error on script","",etlResultDescription,"")
    responseBody = "Process ended with JAVA ERROR for Line " + str(lineCode) + ", total processed: "+str(i)+". Error message: "+etlResultDescription
else:
    responseBody = "Process ended SUCCESSFULLY - total send: "+str(i)+"."
finally:
    dbShortcut.close()"
X3SETPOSNRETL,Set POSNR post action ETL,Draft,"from psdi.server import MXServer
from com.terna.integration.service import IntegrationService
from com.terna.integration.sapconnector import SapConnectorScenario

linea=mbo.getString("ASSETNUM")

checkSplitNeeded=MXServer.getMXServer().getMboSet("ASSETLOCRELATION",MXServer.getMXServer().getSystemUserInfo())
checkSplitNeeded.setWhere("length(x3sequence)=9 AND sourceassetnum='"+str(linea)+"'")
checkSplitNeeded.reset()
if not checkSplitNeeded.isEmpty():
    assetSecondoLivelloSplitSostMboSet=mbo.getMboSet("X3ASSETLOCRELATION_SOURCE")
    assetSecondoLivelloSplitSostMboSet.setWhere("LENGTH(x3sequence)=6 AND x3sequence IN (SELECT substr(x3sequence,1,6) FROM assetlocrelation WHERE length(x3sequence)=9 AND sourceassetnum='"+str(linea)+"')")
    assetSecondoLivelloSplitSostMboSet.reset()
    if not assetSecondoLivelloSplitSostMboSet.isEmpty():
        IntegrationService.getService().setScenario(SapConnectorScenario.SKIP)
        assetSecondoLivelloSplitSostMbo = assetSecondoLivelloSplitSostMboSet.moveFirst()
        while (assetSecondoLivelloSplitSostMbo):
            pos=assetSecondoLivelloSplitSostMbo.getString("X3SEQUENCE")
            if len(pos)==6:
                assetSecondoLivelloSplitSostMbo.setValue("X3SEQUENCE",pos+"-01")
            assetSecondoLivelloSplitSostMbo=assetSecondoLivelloSplitSostMboSet.moveNext()
        IntegrationService.getService().setScenario(SapConnectorScenario.SKIP)
        assetSecondoLivelloSplitSostMboSet.save()
    assetSecondoLivelloSplitSostMboSet.close()
checkSplitNeeded.close()

assetSecondoPOSNRMboSet=MXServer.getMXServer().getMboSet("ASSETLOCRELATION",MXServer.getMXServer().getSystemUserInfo())
assetSecondoPOSNRMboSet.setWhere("X3POSNR is NULL and sourceassetnum='"+str(linea)+"'")
assetSecondoPOSNRMboSet.reset()
if not assetSecondoPOSNRMboSet.isEmpty():
    IntegrationService.getService().setScenario(SapConnectorScenario.SKIP)
    assetSecondoPOSNRMbo = assetSecondoPOSNRMboSet.moveFirst()
    while(assetSecondoPOSNRMbo):
        x3sapidsostegno=assetSecondoPOSNRMbo.getString("X3SAPID")
        assetSecondoCampataPOSNRMboSet=assetSecondoPOSNRMbo.getMboSet("$POSNRCAMPATA$","ASSETLOCRELATION","x3sapid='"+str(x3sapidsostegno[:16])+"'")
        if not assetSecondoCampataPOSNRMboSet.isEmpty():
            posnrcampata=assetSecondoCampataPOSNRMboSet.getMbo(0).getString("X3POSNR")
            assetSecondoPOSNRMbo.setValue("X3POSNR",posnrcampata)
        elif not assetSecondoPOSNRMbo.getMboSet("$POSNRCAMPATAETL$","X3SEDITECHSAP","STRNO='"+str(x3sapidsostegno)+"'").isEmpty():
            assetSecondoCampataETLPOSNRMboSet=assetSecondoPOSNRMbo.getMboSet("$POSNRSOSTETL$","X3SEDITECHSAP","STRNO='"+str(x3sapidsostegno[:16])+"'")
            if not assetSecondoCampataETLPOSNRMboSet.isEmpty():
                posnrsost=assetSecondoCampataETLPOSNRMboSet.getMbo(0).getString("POSNR")
                assetSecondoPOSNRMbo.setValue("X3POSNR",posnrsost)
        else:
            assetSecondoCampataETLPOSNRMboSet=assetSecondoPOSNRMbo.getMboSet("$POSNRCAMPATAETL$","X3SEDITECHSAP","RBNR='PMTSTLIC' and TPLMA='"+str(x3sapidsostegno[:16])+"'")
            if not assetSecondoCampataETLPOSNRMboSet.isEmpty():
                posnrcampata=assetSecondoCampataETLPOSNRMboSet.getMbo(0).getString("POSNR")
                assetSecondoPOSNRMbo.setValue("X3POSNR",posnrcampata)
        assetSecondoPOSNRMbo=assetSecondoPOSNRMboSet.moveNext()
    IntegrationService.getService().setScenario(SapConnectorScenario.SKIP)
    assetSecondoPOSNRMboSet.save()
assetSecondoPOSNRMboSet.close()