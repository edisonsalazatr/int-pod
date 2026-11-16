# X3CONSTTXU
# Richiamato dallo Step 3 del connettore Maximo-SAP.
# Calcola il valore dello Stato delle Sedi Tecniche SAP 
# v1.0 - 16\07\2023
# v1.1 - 08\09\2023 - Modificato mapping stati DATT <-> DATT in DATT <-> ESER
# v2.0 - 26\02\2024 - Gestione della campata/tratta cavo fittizia

from com.terna.integration.sapconnector.step3 import SapTechSpecMappingWarning


#Mappa contenente {STATO_EAM : STATO_SAP}
statusMap = {"PROG":"PROG","CANT":"CANT","DATT":"ESER","ESER":"ESER","ATTV":"ATTV","NADI":"NADI","NAND":"NAND", "CEAL":"CEAL","EFFE":"ATTV","EFDI":"ATTV","EFPR":"ATTV","EFSC":"ATTV","GUAS":"NAND","NIED":"NAND","NIEP":"NAND","NIES":"NAND","NIEO":"NAND","NIGU":"NAND","RIPR":"NAND","DIST":"DEMO","DEMO":"DEMO","INAT":"PROG","DELE":"DEMO"}

def getAssetStatus(sapAsset):
    #SapClass dell'oggetto processato
    sapClass = sapAsset.getString("SAPCLASS")
    #Clausola where per determinare Asset
    #lo hierarchypath viene determinato dinamicamente dallo switch
    whereClause = "(assetnum,siteid) in (select objectnum,siteid from X3SAPTOMAXOBJECT where :x3sapassetid=sapassetid and hierarchypath='%s')"
    if sapClass == "LINEA":
        whereClause = whereClause%"LINEA"
    elif sapClass in ("CAMPATA", "CONDUTTORE", "CORDA DI GUARDIA"):
        whereClause = whereClause%"LINEA\CAMPATA AEREA"
    elif sapClass in ("TRATTA CAVO", "CAVO"):
        whereClause= whereClause%"LINEA\TRATTA CAVO"
    elif sapClass in ("SOSTEGNO", "MORSETTERIA", "ISOLATORE"):
        whereClause = whereClause%"LINEA\SOSTEGNO AEREO"
    elif sapClass == "CAMERA GIUNTI" or sapClass == "TERMINALE":
        whereClause = whereClause%"LINEA\ESTREMO CAVO"
    
    #MboSet degli asset correlati al SapAsset
    assetMboSet = sapAsset.getMboSet("$X3CONSTTXU","ASSET", whereClause)
    countAsset = assetMboSet.count()
    if countAsset == 0:
        ruleHandler.addSimpleWarningForRule(mappingRule)
        return None
    else:
        asset = assetMboSet.moveFirst()
        #Determino lo stato in base alla mappa
        return statusMap[asset.getString("STATUS")]

#Se processo una campata fittizia allora prendo in considerazione il sostegno / terminale / giunto successivo
if sapAsset.getBoolean("DUMMY"):
    strnoParent = sapAsset.getString("STRNO")
    sapAssetChildSet = sapAsset.getMboSet("$X3SAPASSETCHILD", "X3SAPASSET", "parentstrno='%s' and sapclass in ('SOSTEGNO','TERMINALE','CAMERA GIUNTI')"%strnoParent)
    countSapAssetChild = sapAssetChildSet.count()
    if countSapAssetChild == 0 :
        ruleHandler.addSimpleWarningForRule(mappingRule)
    else:
        sapAssetChild = sapAssetChildSet.moveFirst()
        mappedRawValue = getAssetStatus(sapAssetChild)
else:
    mappedRawValue = getAssetStatus(sapAsset)