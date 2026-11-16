# X3CONELEMINDISPSTZ
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola Codice sede tecnica SAP delL' Elemento di indisponibilità delle stazioni. 
# v1.0 - 21/06/2024

from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning

# relazione verso l'asset
relationshipName = mappingRule.getString("MAXRELATIONSHIP")
relationshipSet = mappingRule.getMboSet("$CONDITIONINDISP","CONDITION","CONDITIONNUM='%s'"%relationshipName)
relationship = relationshipSet.moveFirst()
# prende la specifica LIN_ELEMENTO_INDISP della linea
assetSpecSet = sapAsset.getMboSet("$ASSETSPECINDISP","ASSETSPEC","%s AND ASSETATTRID = 'STZ_ELEMENTO_INDISP'"%relationship.getString("EXPRESSION"))
countSpec = assetSpecSet.count()

if countSpec == 0 :
    # non è stato trovato l'attributo nelle ASSETSPEC
    ruleHandler.addSimpleWarningForRule(mappingRule)
else:
    assetSpec = assetSpecSet.moveFirst()
    mappedRawValue = assetSpec.getString("TABLEVALUE")