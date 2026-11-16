# X3CONTERMINALEINIZIO
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
    trattaCavoRelSet = sapAsset.getMboSet("$trattaassoc$", "ASSETLOCRELATION", "(siteid, sourceassetnum, targetassetnum) in (select siteid, rootassetnum, objectnum from x3saptomaxobject where sapassetid = :x3sapassetid and hierarchypath = 'LINEA\TRATTA CAVO')")
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
                    ruleHandler.addSimpleWarningForRule(mappingRule, "Can't find attribute LIN_ESTREMO_INIZIO on TRATTA CAVO " + trattaCavoRelMbo.getString("x3sapid"))