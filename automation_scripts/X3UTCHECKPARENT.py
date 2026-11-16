from psdi.server import MXServer

mxServer = MXServer.getMXServer()
systemUserInfo = MXServer.getMXServer().getSystemUserInfo()

#Get X3SAPASSET MboSet
sapAssetSet = mxServer.getMboSet("X3SAPASSET", systemUserInfo)
#A: modificata query verificare se funziona, l'idea è quella di verificare che sia popolato sia il PARENTSTRNO che il SAPPARENTUID 
sapAssetSet.setWhere("SAPCLASS <> 'LINEA' AND (PARENTSTRNO IS NULL OR SAPPARENTUID IS NULL)")

numNotFound = 0

# Check if records > 0
if sapAssetSet.count() > 0:
    sapAsset = sapAssetSet.moveFirst()
    while (sapAsset):
        numNotFound += 1
        log.log("(STRNO:%s, PARENTSTRNO:%s, SAPPARENTUID:%s) parent or sapparentuid not found in X3SAPASSET"%(sapAsset.getString("STRNO"), sapAsset.getString("PARENTSTRNO"), sapAsset.getString("SAPPARENTUID")))
        sapAsset = sapAssetSet.moveNext()
sapAssetSet.close()

raise Exception, "%s parent non trovati in X3SAPASSET"%numNotFound