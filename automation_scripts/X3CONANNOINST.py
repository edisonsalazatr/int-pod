# X3CONANNOINST
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore inserito più recentemente tra gli attributi LIN_ANNO_INSTALLAZIONE_LIN, LIN_ANNO_INSTALLAZIONE_DER, LIN_ANNO_INSTALLAZIONE_RIC
# v1.0 - 05\10\2023
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning

# nome caratteristica in Maximo
maximoAttribute = mappingRule.getString("MAXNAME")

# caratteristiche associate al sapasset
assetSpecSet = sapAsset.getMboSet("$MAXOBJECT", "ASSETSPEC", "(ASSETNUM,SITEID) IN (SELECT OBJECTNUM,SITEID FROM X3SAPTOMAXOBJECT WHERE SAPASSETID = :X3SAPASSETID AND HIERARCHYPATH='LINEA\SOSTEGNO AEREO\ATTACCHI CONDUTTORE\ARMAMENTO CONDUTTORE') AND ASSETATTRID IN ('LIN_ANNO_INSTALLAZIONE_LIN', 'LIN_ANNO_INSTALLAZIONE_DER', 'LIN_ANNO_INSTALLAZIONE_RIC')")
assetSpecSet.setOrderBy("CHANGEDATE DESC")
countAssetSpec = assetSpecSet.count()

if countAssetSpec == 0:
    #se non ci sono caratteristiche associate genera warning
    ruleHandler.addSimpleWarningForRule(mappingRule)
else:
    #prendo la prima ordinata per CHANGEDETE
    assetSpecMbo = assetSpecSet.moveFirst()
    mappedRawValue = assetSpecMbo.getInt("NUMVALUE")