#Verifica che gli asset di una linea/e siano presenti nella X3OBJECTCHANGE
#Parameters: lineeStrno


from psdi.server import MXServer
from psdi.mbo import SqlFormat
from psdi.mbo import DBShortcut

mxServer = MXServer.getMXServer()
dbShortcut=DBShortcut()
dbShortcut.connect(mxServer.getSystemUserInfo().getConnectionKey())
lineeStrno = lineeStrno.replace(",","','")
lineeStrno = "'"+lineeStrno+"'"

count=0 #Number of records 

sql = """
SELECT xs.STRNO,xs.SAPCLASS 
FROM X3SAPASSET xs 
WHERE xs.SAPCLASS NOT IN (SELECT xm.SAPCLASS FROM X3MAXIMOSAPMAP xm) AND xs.ROOTSTRNO in (%s)
"""%lineeStrno
try:
    sqfQuery=SqlFormat(sql)
    resultSet=dbShortcut.executeQuery(sqfQuery.format())

    while resultSet.next():
        count+=1
        log.log("[%3s]\t(%30s %30s)\t SAPCLASS NOT EXISTS"%(count,resultSet.getString("STRNO"), resultSet.getString("SAPCLASS")))
    resultSet.close()
finally:
    dbShortcut.close()
    if count > 0:
        failed=True