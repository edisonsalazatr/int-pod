##PARAMETERS: lineeStrno (linea in esamina)

from psdi.server import MXServer
mxServer = MXServer.getMXServer()
x3SapToMaxSet = mxServer.getMboSet("X3SAPTOMAXOBJECT", mxServer.getSystemUserInfo())
lineeAssetnum = lineeAssetnum
lineeAssetnumArr = lineeAssetnum.split(',')

countAsset = 0
countTronchi=0

for linea in lineeAssetnumArr:
    linea = linea.replace(" ", "")
    log.log("Analyzing line %s"%linea)
    
    whereClauseAsset = "ROOTASSETNUM = '" + linea + "' AND OBJECTNUM NOT IN (SELECT ASSETNUM FROM ASSET)"
    
    whereClauseTronchi = "ROOTASSETNUM = '" + linea + "' AND HIERARCHYPATH like '"+ linea + "%TRONCO DI ISPEZIONE%' and OBJECTNUM NOT IN (SELECT TRONCONUM FROM X3TRONCHI)"
    
    x3SapToMaxSet.setWhere(whereClauseAsset)
    countAsset = x3SapToMaxSet.count()
    
    if countAsset>0:
        x3SapToMaxObj = x3SapToMaxSet.moveFirst()
        
        while (x3SapToMaxObj):
        
            objNum = x3SapToMaxObj.getString("OBJECTNUM")
            
            log.log("Binding not found for object %s"%objNum)
            
            x3SapToMaxObj = x3SapToMaxSet.moveNext()
            
            
    x3SapToMaxSet.setWhere(whereClauseTronchi)
    
    x3SapToMaxSet.reset()
    countTronchi = x3SapToMaxSet.count()
    if countTronchi>0:
        x3SapToMaxObj = x3SapToMaxSet.moveFirst()
        
        while (x3SapToMaxObj):
        
            objNum = x3SapToMaxObj.getString("OBJECTNUM")
            
            log.log("Binding not found for object %s"%objNum)
            
            x3SapToMaxObj = x3SapToMaxSet.moveNext()

x3SapToMaxSet.close()

if countTronchi > 0 or countAsset>0:
    raise Exception, "%s Asset not found or %s Tronchi not found"%(countAsset,countTronchi)