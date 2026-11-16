# X3CONSOSTINIZIO
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_SOST_INIZIO.
# Calcolare il valore del primo sostegno di una campata seguendo il verso convenzionale della linea.
# v1.0 - 12-07-2023
# v1.1 - 27-10-2023 Gestione split
# v1.2 - 30-10-2023 Gestione derivazioni e campate fittizie
# v1.3 - 06-06-2024 Gestione campate in stato alias di DECOMMISSIONED
# v1.4 - 02-04-2025 Gestione OP_252
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning
from com.terna.integration.service import IntegrationService
from com.terna.common.maximo import MXUtils

# gestione campata fittizia
if sapAsset.getBoolean("dummy"):
    strnoSostegno = sapAsset.getString("strno") + "-S"
    sostMboSet = sapAsset.getMboSet("$sostassoc$", "X3SAPASSET", "strno = '%s'"%strnoSostegno)
    sostMboSet.reset()
    if sostMboSet.isEmpty():
        mappedRawValue = ""
    else:
        mappedRawValue = strnoSostegno
else:
    # prende le relazioni verso la campata
    campataRelSet = sapAsset.getMboSet("$campassoc$", "ASSETLOCRELATION", "(siteid, sourceassetnum, targetassetnum) in (select siteid, rootassetnum, objectnum from x3saptomaxobject where sapassetid = :x3sapassetid and maxobject = 'ASSET' and hierarchypath = 'LINEA\CAMPATA AEREA')")
    campataRelMbo = campataRelSet.getMbo(0)
    
    if campataRelMbo == None:
        # relazione non trovata
        ruleHandler.addSimpleWarningForRule(mappingRule)
    else:
        # relazione trovata
        sequenceId = campataRelMbo.getString("x3sequence")
        if sequenceId == "":
            # sequenza non valorizzata
            ruleHandler.addSimpleWarningForRule(mappingRule, "X3SEQUENCE is null for CAMPATA " + campataRelMbo.getString("x3sapid"))
        else:
            # prendo lo stato del target asset (campata area)
            campataMbo = campataRelMbo.getMboSet("TARGETASSET").getMbo(0)
            #statusCampata = MXUtils.getInternalValue(campataMbo, "status")
            statusCampata = campataMbo.getString("status")
            
            if statusCampata == "DEMO":
                # se la campata è in uno stato alias di DECOMMISSIONED ritorna valore vuoto
                mappedRawValue = sapAsset.getString("strno") + "-S"
            else:
                # OP_252, OP_253: se l'attributo LIN_SOSTEGNO_INIZIO è valorizzato restituisce il valore impostato altrimenti deve essere calcolato
                linSostegnoInizio = None
                campataStrno = sapAsset.getString("strno")
                lineaStrno = campataStrno[:7]
                assetSpecSet = campataMbo.getMboSet("ASSETSPEC")
                assetSpecSet.setWhere("assetattrid = 'LIN_SOSTEGNO_INIZIO' and (section = '" + lineaStrno + "' or section is null)")
                assetSpecSet.setOrderBy("section asc")
                assetSpecSet.reset()
                assetSpec = assetSpecSet.getMbo(0)
                if assetSpec != None:
                    linSostegnoInizio = assetSpec.getString("ALNVALUE")
                    if linSostegnoInizio != "":
                        # l'attributo LIN_SOSTEGNO_INIZIO è valorizzato
                        mappedRawValue = linSostegnoInizio
                    else:
                        # l'attributo LIN_SOSTEGNO_INIZIO non è valorizzato
                        sostegnoClause = "(siteid = :siteid and sourceassetnum = :sourceassetnum and x3sapid like '%-S' and x3sequence < :x3sequence)"
                        if '-' in sequenceId and not sequenceId[0].isalpha():
                            # la campata appartiene ad uno split (ad esempio nnnnnn-03)
                            # la ricerca dei sostegni precedenti deve essere circoscritta 
                            # allo split (nnnnnn-03), alla dorsale (nnnnnn) e ad un eventuale split intermedio (ad esempio nnnnnn-23)
                            
                            # recupero il numero dello split (a destra del -) al netto del valore 0
                            split = sequenceId.split("-")[1].replace("0","")
                            # limito la ricerca allo split e ad un eventuale split intermedio e alla dorsale
                            sostegnoClause += " and (substr(x3sequence,8,9) like '%" + split + "%' or length(x3sequence) = 6)"
                        elif sequenceId[0].isalpha():
                            # la campata appartiene ad una derivazione (ad esempio Bnnnnnn)
                            # la ricerca dei sostegni precedenti deve essere circoscritta 
                            # allo derivazione (Bnnnnnn%), 
                            # all'elemento della dorsale da cui parte la derivazione (nnnnnn%B%) 
                            # e all'elementodi una  eventuale derivazione intermedia (ad esempio Annnnnn%B%)
                            sostegnoClause += " and (x3sequence like '%" + sequenceId[0] + "%' and not x3sequence like '%Z%')"
                            if '-' in sequenceId:
                                # recupero il numero dello split (a destra del -) al netto del valore 0
                                split = sequenceId.split("-")[1].replace("0","")
                                # limito la ricerca allo split e ad un eventuale split intermedio e alla derivazione
                                sostegnoClause += " and (substr(x3sequence,8,9) like '%" + split + "%' or length(x3sequence) = 7)"
                            
                        # recupero i sostegni precedenti ordinati in modo decrescente    
                        sostegnoRelSet = campataRelMbo.getMboSet("$sostegniprecedenti$", "ASSETLOCRELATION", sostegnoClause)
                        sostegnoRelSet.setOrderBy("x3sequence desc")
                        
                        # recupera il primo sostegno non DECOMMISSIONED
                        mappedRawValue = ""
                        i = 0
                        sostegnoRelMbo = sostegnoRelSet.getMbo(0)
                        while sostegnoRelMbo != None:
                            sostegnoMbo = sostegnoRelMbo.getMboSet("TARGETASSET").getMbo(0)
                            statusSostegno = MXUtils.getInternalValue(sostegnoMbo, "status")
                            if statusSostegno != "DECOMMISSIONED":
                                mappedRawValue = sostegnoRelMbo.getString("x3sapid")
                                break
                            i += 1
                            sostegnoRelMbo = sostegnoRelSet.getMbo(i)
                            
                        if sostegnoRelMbo == None:
                            # il sostegno non è stato trovato
                            ruleHandler.addSimpleWarningForRule(mappingRule, "there are no previous SOSTEGNO AEREO")
                else:
                    ruleHandler.addSimpleWarningForRule(mappingRule, "Can't find attribute LIN_SOSTEGNO_INIZIO on CAMPATA AEREA " + campataRelMbo.getString("x3sapid"))