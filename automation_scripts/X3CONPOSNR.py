# X3CONPOSNR
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore dell'attributo POSNR.
# v1.0 - 17/10/2023
# v1.1 - 23/12/2024 correzione SAPCLASS CAMERA GIUNTI
from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning


# Verifica se il valore booleano "DUMMY" in X3SAPASSET è vero
if sapAsset.getBoolean("DUMMY"):
    # Se è vero, recupera gli asset correlati di tipo "SOSTEGNO", "TERMINALI" o "GIUNTO"
    relatedSapAssetSet = sapAsset.getMboSet("$RELATEDSAPASSET","X3SAPASSET","PARENTSTRNO = :STRNO AND SAPCLASS IN ('SOSTEGNO','TERMINALE','CAMERA GIUNTI')")
    countRelatedSapAsset = relatedSapAssetSet.count()
    
    if countRelatedSapAsset == 0:
        ruleHandler.addSimpleWarningForRule(mappingRule)
    else:
        # ottieni il primo asset correlato
        relatedSapAsset = relatedSapAssetSet.moveFirst()
        
        # Ottieni ASSETLOCRELATION dei sostegni aerei o terminali correlati
        relatedAssetLocRelationSet = relatedSapAsset.getMboSet("$X3CONPOSNR","ASSETLOCRELATION",
            "(SOURCEASSETNUM,TARGETASSETNUM) IN (SELECT o.ROOTASSETNUM , o.OBJECTNUM  FROM X3SAPTOMAXOBJECT o WHERE o.HIERARCHYPATH IN ('LINEA\SOSTEGNO AEREO','LINEA\ESTREMO CAVO') AND o.SAPASSETID=:X3SAPASSETID)")
        relatedAssetLocRelationSet.reset()
        countRelatedAssetLocRelation = relatedAssetLocRelationSet.count()
        
        if countRelatedAssetLocRelation == 0:
            ruleHandler.addSimpleWarningForRule(mappingRule)
        else:
            # Altrimenti, ottieni il primo asset correlato alla posizione e memorizza il valore della colonna "X3POSNR" in "mappedRawValue"
            relatedAssetLocRelation = relatedAssetLocRelationSet.moveFirst()
            mappedRawValue = relatedAssetLocRelation.getString("X3POSNR")
else:
    # Se il valore booleano "DUMMY" è falso, recupera ASSETLOCRELATION di campate/tratte cavo
    relatedAssetLocRelationSet = sapAsset.getMboSet("$X3CONPOSNR","ASSETLOCRELATION",
        "(SOURCEASSETNUM,TARGETASSETNUM) IN (SELECT o.ROOTASSETNUM , o.OBJECTNUM  FROM X3SAPTOMAXOBJECT o WHERE o.HIERARCHYPATH IN ('LINEA\CAMPATA AEREA','LINEA\TRATTA CAVO') AND o.SAPASSETID=:X3SAPASSETID)")
    relatedAssetLocRelationSet.reset()
    countRelatedAssetLocRelation = relatedAssetLocRelationSet.count()

    if countRelatedAssetLocRelation == 0:
        ruleHandler.addSimpleWarningForRule(mappingRule)
    else:
        relatedAssetLocRelation = relatedAssetLocRelationSet.moveFirst()
        mappedRawValue = relatedAssetLocRelation.getString("X3POSNR")