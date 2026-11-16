from psdi.server import MXServer

mxServer = MXServer.getMXServer()
systemUserInfo = mxServer.getSystemUserInfo()

#elab parameter
lineeAssetnum = lineeAssetnum.replace(",","','")
lineeAssetnum = "'"+lineeAssetnum+"'"

assetLocRelationSet = mxServer.getMboSet("ASSETLOCRELATION", systemUserInfo)
assetLocRelationSet.setWhere("X3SAPID IS NULL AND SOURCEASSETNUM IN (%s)"%lineeAssetnum)
countAssetLocRelation = assetLocRelationSet.count()
assetLocRelation = assetLocRelationSet.moveFirst()
count=0
while(assetLocRelation != None):
    count+=1
    log.log("[%4s] X3SAPID MISSING IN ASSETLOCRELATION (SOURCEASSETNUM:%20s , TARGETASSETNUM:%20s"(count,assetLocRelation.getString("SOURCEASSETNUM"),assetLocRelation.getString("TARGETASSETNUM")))
    assetLocRelation = assetLocRelation.moveNext()

if countAssetLocRelation > 0:
    failed=True