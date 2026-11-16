#PARAMETERS: lineeStrno

from psdi.server import MXServer

mxServer = MXServer.getMXServer()
x3SapAssetSet = mxServer.getMboSet("X3SAPASSET", mxServer.getSystemUserInfo())
param = "%s"%lineeStrno
paramArr = param.split(",")
for linea in paramArr:
    linea = linea.replace(" ", "")
    whereClause = "ROOTSTRNO = '%s' AND SAPLEVEL = 3 AND PARENTSTRNO = '%s'"%(linea,linea)
    x3SapAssetSet.setWhere(whereClause)
    if x3SapAssetSet.count() > 0:
        failed = True
        x3SapAssetObj = x3SapAssetSet.moveFirst()
        while (x3SapAssetObj):
            strno = x3SapAssetObj.getString("STRNO")
            parentstrno = x3SapAssetObj.getString("PARENTSTRNO")
            log.log("%s PARENTSTRNO NOT VALID FOR STRNO %s"%(parentstrno,strno))
            x3SapAssetObj= x3SapAssetSet.moveNext()
x3SapAssetSet.close()