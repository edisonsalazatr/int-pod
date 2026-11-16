##PARAMETERS: lineeStrno (linea in esamina)

from psdi.server import MXServer
mxServer = MXServer.getMXServer()
prefix = "[X3UTNOBINDINGSAPASSSAPTOMAXOBJ]"
x3SapToMaxSet = mxServer.getMboSet("X3SAPTOMAXOBJECT", mxServer.getSystemUserInfo())
lineeStrno = "%s"%lineeStrno
lineeStrnoArr = lineeStrno.split(',')
for linea in lineeStrnoArr:
    linea = linea.replace(" ", "")
    whereClause = "ROOTASSETNUM = '"+ linea + "' AND SAPASSETID NOT IN (SELECT X3SAPASSETID FROM X3SAPASSET)"
    x3SapToMaxSet.setWhere(whereClause)
    if x3SapToMaxSet.count()>0:
        failed=True
        x3SapToMaxObj = x3SapToMaxSet.moveFirst()
        while (x3SapToMaxObj):
            sapassetid = x3SapToMaxObj.getString("SAPASSETID")
            log.log(prefix + "%s not in X3SAPASSET for line %s"%(sapassetid,linea))
            x3SapToMaxObj = x3SapToMaxSet.moveNext()
x3SapToMaxSet.close()