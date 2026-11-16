# X3CONSOSTFINE
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_SOST_FINE.
# Calcolare il valore dell'ultimo sostegno di una campata seguendo il verso convenzionale della linea.
# v1.0 - 12-07-2023
# v1.1 - 27-10-2023 Gestione split
# v1.2 - 30-10-2023 Gestione derivazioni e campate fittizie
# v1.3 - 12-12-2023 Gestione rientro derivazione
# v1.4 - 06-06-2024 Gestione campate in stato alias di DECOMMISSIONED
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning
from com.terna.integration.service import IntegrationService
from com.terna.common.maximo import MXUtils

# gestione campata fittizia
if sapAsset.getBoolean("dummy"):
    strnoSostegno = sapAsset.getString("strno") + "-S"
    sostMboSet = sapAsset.getMboSet("$sostegnoassoc$", "X3SAPASSET", "strno = '%s'"%strnoSostegno)
    sostMboSet.reset()
    if sostMboSet.isEmpty():
        mappedRawValue = ""
    else:
        mappedRawValue = strnoSostegno
else:
    # prendo relazione campata
    campataRelSet = sapAsset.getMboSet("$campassoc$", "ASSETLOCRELATION", "(siteid, sourceassetnum, targetassetnum) in (select siteid, rootassetnum, objectnum from x3saptomaxobject where sapassetid = :x3sapassetid and hierarchypath = 'LINEA\CAMPATA AEREA')")
    campataRelMbo = campataRelSet.getMbo(0)
    
    if campataRelMbo == None:
        # relazione non trovata
        ruleHandler.addSimpleWarningForRule(mappingRule)
    else:
        # prendo status e sequence della campata
        sequenceId = campataRelMbo.getString("x3sequence")
        if sequenceId == "":
            ruleHandler.addSimpleWarningForRule(mappingRule, "X3SEQUENCE is null for CAMPATA " + campataRelMbo.getString("x3sapid"))
        else:    
            # prendo targetasset per lo stato
            campataMbo = campataRelMbo.getMboSet("TARGETASSET").getMbo(0)
            #statusCampata = MXUtils.getInternalValue(campataMbo, "status")
            statusCampata = campataMbo.getString("status")
            if statusCampata == "DEMO":
                # se status è alias di decommissioned ritorna valore vuoto
                mappedRawValue = sapAsset.getString("strno") + "-S"
            else:
                sostegnoClause = "(siteid = :siteid and sourceassetnum = :sourceassetnum and x3sapid like '%-S' and x3sequence > :x3sequence)"
                
                # sostegni successivi al corrente (gestione split)
                if '-' in sequenceId and not sequenceId[0].isalpha():
                    #Valori alla destra del '-' esclusi 0
                    split = sequenceId.split("-")[1].replace("0","")
                    #Sostegni successivi al corrente
                    sostegnoClause += " and (substr(x3sequence,8,9) like '%" + split + "%' or length(x3sequence) = 6)"
                # sostegni successivi al corrente (gestione derivazione)
                # verifico che il primo carattere sia una lettera
                elif sequenceId[0].isalpha():
                    # la campata appartiene ad una derivazione (ad esempio Bnnnnnn)
                    # la ricerca dei sostegni precedenti deve essere circoscritta 
                    # allo derivazione (Bnnnnnn%), 
                    # all'elemento della dorsale da cui parte la derivazione (nnnnnn%B%) 
                    # e all'elementodi una  eventuale derivazione intermedia (ad esempio Annnnnn%B%)
                    sostegnoClause += " and (x3sequence like '" + sequenceId[0] + "%' or x3sequence like '%" + sequenceId[0] + "')"
                    if '-' in sequenceId:
                        # recupero il numero dello split (a destra del -) al netto del valore 0
                        split = sequenceId.split("-")[1].replace("0","")
                        # limito la ricerca allo split e ad un eventuale split intermedio (su derivazione) e alla dorsale
                        sostegnoClause += " and (substr(x3sequence,8,9) like '%" + split + "%' or length(x3sequence) = 7)"
                
                sostegnoRelSet = campataRelMbo.getMboSet("$sostegnisuccessivi$", "ASSETLOCRELATION", sostegnoClause)
                sostegnoRelSet.setOrderBy("x3sequence asc")
                # caso in cui non trovo sostegni ma potrebbe essere il rientro di una derivazione 
                # ed ha lettera finale Z e X3SEQUENCE più basso.
                if sostegnoRelSet.getMbo(0) == None:
                    if sequenceId[0].isalpha():
                        sostegnoRelSet = campataRelMbo.getMboSet("$sostegnisuccessivi$", "ASSETLOCRELATION","siteid = :siteid and sourceassetnum=:sourceassetnum and x3sapid like '%-S' and x3sequence < :x3sequence and x3sequence like '%" + sequenceId[0] + "Z'")
                
                sostegnoRelMbo = sostegnoRelSet.getMbo(0)
                if sostegnoRelMbo == None:
                    ruleHandler.addSimpleWarningForRule(mappingRule, "there are no previous SOSTEGNO AEREO")
                else:
                    sostegnoMbo = sostegnoRelMbo.getMboSet("TARGETASSET").getMbo(0)
                    statusSostegno = MXUtils.getInternalValue(sostegnoMbo, "status")
                    # se stato del sostegno è alias di DECOMMISSIONED
                    if statusSostegno == "DECOMMISSIONED":
                        # prendo sostegno con sequence successiva
                        i = 0
                        while i < sostegnoRelSet.getSize():
                            i += 1
                            sostegnoRelMbo = sostegnoRelSet.getMbo(i)
                            if sostegnoRelMbo == None:
                                mappedRawValue = ""
                                break
                            else:
                                sostegnoMbo = sostegnoRelMbo.getMboSet("TARGETASSET").getMbo(0)
                                statusSostegnoSucc = MXUtils.getInternalValue(sostegnoMbo, "status")
                                if statusSostegnoSucc != "DECOMMISSIONED":
                                    mappedRawValue = sostegnoRelMbo.getString("x3sapid")
                                    break
                                else:
                                    continue
                    else:
                        mappedRawValue = sostegnoRelMbo.getString("x3sapid")