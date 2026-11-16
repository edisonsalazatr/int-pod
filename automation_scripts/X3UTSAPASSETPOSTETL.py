#UnitTest
#Verifica delle sedi tecniche non presenti nella X3SAPASSET
#parameters: linee
from psdi.server import MXServer

linee=lineeStrno.replace(" ","") # remove space from string
array_linee = linee.split(",") #get array of lines

#settings mxserver
mxServer =MXServer.getMXServer()
systemUserInfo = MXServer.getMXServer().getSystemUserInfo()

numNotFound = 0 #number of Sedi Tecniche not found

#for each line in array_linee
for linea in array_linee:
    log.log("Analyzing line %s"%linea)
    sediTecnicheSet = mxServer.getMboSet("X3SEDITECHSAP",systemUserInfo)
    sediTecnicheSet.setWhere("(STRNO,TPLNR,TPLMA) NOT IN (SELECT X3SAPASSET.STRNO, X3SAPASSET.SAPUID, X3SAPASSET.PARENTSTRNO FROM X3SAPASSET) AND STRNO LIKE '"+linea+"%'")
    #if (STRNO,TPLNR,TPLMA) not found in X3SAPASSET
    if sediTecnicheSet.count() > 0:
        sedeTecnicaSap = sediTecnicheSet.moveFirst()
        #for each (STRNO,TPLNR,TPLMA) that not found in X3SAPASSET 
        while(sedeTecnicaSap):
            numNotFound+=1
            log.log("[%5s]\t(%15s,%30s,%30s,%30s) not found in X3SAPASSET"%(numNotFound,sedeTecnicaSap.getString("RBNRX"),sedeTecnicaSap.getString("STRNO"),sedeTecnicaSap.getString("TPLNR"),sedeTecnicaSap.getString("TPLMA")))
            sedeTecnicaSap = sediTecnicheSet.moveNext()
    sediTecnicheSet.close()
if numNotFound > 0:
    raise Exception, "%s sedi tecniche non trovate in X3SAPASSET"%numNotFound