# X3CONELEMINDISP
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola Codice sede tecnica SAP delL' Elemento di indisponibilità. 
# v1.0 - 30/11/2023

from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning
# prende la specifica LIN_ELEMENTO_INDISP della linea
assetSpecSet = sapAsset.getMboSet("$ASSETSPEC","ASSETSPEC","(SITEID,ASSETNUM) IN (SELECT SITEID, OBJECTNUM FROM X3SAPTOMAXOBJECT WHERE SAPASSETID = :X3SAPASSETID AND HIERARCHYPATH = 'LINEA') AND ASSETATTRID = 'LIN_ELEMENTO_INDISP'")

countSpec = assetSpecSet.count()
if countSpec == 0 :
	# non è stato trovato l'attributo nelle ASSETSPEC
	ruleHandler.addSimpleWarningForRule(mappingRule)
else:
    assetSpec = assetSpecSet.moveFirst()
    mappedRawValue = assetSpec.getString("TABLEVALUE")