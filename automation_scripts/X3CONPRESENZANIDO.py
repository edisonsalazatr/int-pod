# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore della caratteristica PMT_CATEGORIA_NIDO.
# Calcola il valore della specifica LIN_CATEGORIA_NIDO per l'asset NIDO AVIFAUNA
# v1.0 - 17\07\2023

from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning

nidoAvifaunaSpecSet = sapAsset.getMboSet("$NIDO", "ASSETSPEC", """(SITEID,ASSETNUM) IN (SELECT SITEID, OBJECTNUM FROM X3SAPTOMAXOBJECT 
                                        WHERE SAPASSETID = :X3SAPASSETID AND HIERARCHYPATH = 'LINEA\SOSTEGNO AEREO\TELAIO\NIDO AVIFAUNA') AND ASSETATTRID = 'LIN_CATEGORIA_NIDO'""")

countNidoAvifaunaSpec = nidoAvifaunaSpecSet.count()

if countNidoAvifaunaSpec == 0:
    ruleHandler.addSimpleWarningForRule(mappingRule)
else:
    returnValue = ""
    for i in range(0, countNidoAvifaunaSpec):
        nidoAvifaunaSpecMbo = nidoAvifaunaSpecSet.getMbo(i)
        specValue = nidoAvifaunaSpecMbo.getString("ALNVALUE");
        if returnValue == "":
            returnValue = specValue
        else:
            if specValue == returnValue:
                continue
            else:
                if returnValue != "" and returnValue != specValue:
                    returnValue = "AN"
                    
    mappedRawValue = returnValue