# X3CONALTRATERNASTESSA_PALIFIC
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
        mappedRawValue.add("")