#X3ETLPROC_X3SAPIDOBLE
from psdi.server import MXServer
from psdi.mbo import SqlFormat
from psdi.mbo import MboConstants
from psdi.mbo import DBShortcut
import time

mxServer = MXServer.getMXServer()
dbShortcut=DBShortcut()
dbShortcut.connect(mxServer.getSystemUserInfo().getConnectionKey())

def log(nivel,descripcion,numrows,error,notas):
    logSet=mbo.getMboSet("X3SCRIPTLOG")
    NewLog = logSet.add()
    NewLog.setValue("DESCRIPTION",nivel+" - "+descripcion)
    NewLog.setValue("X3SCRIPT",scriptName)
    NewLog.setValue("TIMESTAMP",MXServer.getMXServer().getDate())
    NewLog.setValue("NUMROWS",numrows)
    NewLog.setValue("X3ERROR",error)
    NewLog.setValue("X3NOTES",notas)
    logSet.save()
    logSet.close()

i=0
log("INICIO","Actualizacion X3SAPID duplicada campata","","","Inicio Actualizacion X3SAPID de campatas duplicadas SAP")

sqlCampata = """
SELECT 
	campatasCargadas.ASSETNUM,
	campatasCargadas.X3SAPID,
	x3sts.STRNO,
	campatasCargadas.X3SAPID||','||x3sts.STRNO AS CONCATEN
FROM X3SEDITECHSAP x3sts
LEFT JOIN X3CARATSAP x3cs
	ON (x3sts.STRNO = x3cs.OBJECT_ID
		AND x3cs.ATNAM = 'PMT_CAMP_TER_ASS')
LEFT JOIN ASSET campatasCargadas
	ON (x3cs.CHAR_TYP = campatasCargadas.X3SAPID)
WHERE x3sts.DUPLICATE = 1 
AND x3sts.RBNR IN ('PMTSTLIL')
"""

sqfQueryCampata=SqlFormat(sqlCampata)
resultSetCampata=dbShortcut.executeQuery(sqfQueryCampata.format())

while resultSetCampata.next():
    assetnum=resultSetCampata.getString("ASSETNUM")
    x3sapid=resultSetCampata.getString("X3SAPID")
    strno=resultSetCampata.getString("STRNO")
    concaten=resultSetCampata.getString("CONCATEN")
    i=i+1
    AssetSet=mbo.getMboSet("$ASS$","ASSET", "ASSETNUM='"+ assetnum + "'")
    AssetSet.reset()
    asset=AssetSet.getMbo(0)
    asset.setValue("X3SAPID",concaten)
    AssetSet.save()

resultSetCampata.close()
log("FIN","Actualizacion X3SAPID duplicada campata",i,"","Fin Actualizacion X3SAPID de campatas duplicadas SAP")


i=0
log("INICIO","Actualizacion X3SAPID duplicada sostegno","","","Inicio Actualizacion X3SAPID de sostegnos duplicados SAP")

sqlSostegno = """
SELECT
	sostegnosCargados.ASSETNUM,
	sostegnosCargados.X3SAPID,
	x3sts.STRNO,
	sostegnosCargados.X3SAPID||','||x3sts.STRNO AS CONCATEN
FROM X3SEDITECHSAP x3sts
LEFT JOIN X3CARATSAP x3cs
	ON (SUBSTR(x3sts.STRNO,1,16) = x3cs.OBJECT_ID
		AND x3cs.ATNAM = 'PMT_CAMP_TER_ASS')
LEFT JOIN X3SEDITECHSAP x3sts2
	ON (x3cs.CHAR_TYP||'-S' = x3sts2.STRNO)
LEFT JOIN ASSET sostegnosCargados
	ON (x3sts2.STRNO = sostegnosCargados.X3SAPID
		AND sostegnosCargados.CLASSSTRUCTUREID = '1036')
WHERE x3sts.DUPLICATE = 1 
AND x3sts.RBNR IN ('PMTSTLIS')
"""

sqfQuerySostegno=SqlFormat(sqlSostegno)
resultSetSostegno=dbShortcut.executeQuery(sqfQuerySostegno.format())

while resultSetSostegno.next():
    assetnum=resultSetSostegno.getString("ASSETNUM")
    x3sapid=resultSetSostegno.getString("X3SAPID")
    strno=resultSetSostegno.getString("STRNO")
    concaten=resultSetSostegno.getString("CONCATEN")
    i=i+1
    AssetSet=mbo.getMboSet("$ASS$","ASSET", "ASSETNUM='"+ assetnum + "'")
    AssetSet.reset()
    asset=AssetSet.getMbo(0)
    asset.setValue("X3SAPID",concaten)
    AssetSet.save()

resultSetSostegno.close()
log("FIN","Actualizacion X3SAPID duplicada sostegnos",i,"","Fin Actualizacion X3SAPID de sostegnos duplicados SAP")


i=0
log("INICIO","Actualizacion X3SAPID duplicada FuneDiGuardia","","","Inicio Actualizacion X3SAPID de FuneDiGuardia duplicados SAP")

sqlFuneDiGuardia = """
SELECT
	FuneDiGuardiaCargados.ASSETNUM,
	FuneDiGuardiaCargados.X3SAPID,
	x3sts.STRNO,
	FuneDiGuardiaCargados.X3SAPID||','||x3sts.STRNO AS CONCATEN
FROM X3SEDITECHSAP x3sts
LEFT JOIN X3CARATSAP x3cs
	ON (SUBSTR(x3sts.STRNO,1,16) = x3cs.OBJECT_ID
		AND x3cs.ATNAM = 'PMT_CAMP_TER_ASS')
LEFT JOIN X3SEDITECHSAP x3sts2
	ON (x3cs.CHAR_TYP||'-G' = x3sts2.STRNO)
LEFT JOIN ASSET FuneDiGuardiaCargados
	ON (x3sts2.STRNO = FuneDiGuardiaCargados.X3SAPID
		AND FuneDiGuardiaCargados.CLASSSTRUCTUREID = '1016')
WHERE x3sts.DUPLICATE = 1 
AND x3sts.RBNR IN ('PMTSTLIG')
"""

sqfQueryFuneDiGuardia=SqlFormat(sqlFuneDiGuardia)
resultSetFuneDiGuardia=dbShortcut.executeQuery(sqfQueryFuneDiGuardia.format())

while resultSetFuneDiGuardia.next():
    assetnum=resultSetFuneDiGuardia.getString("ASSETNUM")
    x3sapid=resultSetFuneDiGuardia.getString("X3SAPID")
    strno=resultSetFuneDiGuardia.getString("STRNO")
    concaten=resultSetFuneDiGuardia.getString("CONCATEN")
    i=i+1
    AssetSet=mbo.getMboSet("$ASS$","ASSET", "ASSETNUM='"+ assetnum + "'")
    AssetSet.reset()
    asset=AssetSet.getMbo(0)
    asset.setValue("X3SAPID",concaten)
    AssetSet.save()

resultSetFuneDiGuardia.close()
log("FIN","Actualizacion X3SAPID duplicada FuneDiGuardia",i,"","Fin Actualizacion X3SAPID de FuneDiGuardia duplicados SAP")


i=0
log("INICIO","Actualizacion X3SAPID duplicada Isolatori","","","Inicio Actualizacion X3SAPID de Isolatori duplicados SAP")

sqlIsolatori = """
SELECT
	IsolatoriCargados.ASSETNUM,
	IsolatoriCargados.X3SAPID,
	x3sts.STRNO,
	IsolatoriCargados.X3SAPID||','||x3sts.STRNO AS CONCATEN
FROM X3SEDITECHSAP x3sts
LEFT JOIN X3CARATSAP x3cs
	ON (SUBSTR(x3sts.STRNO,1,16) = x3cs.OBJECT_ID
		AND x3cs.ATNAM = 'PMT_CAMP_TER_ASS')
LEFT JOIN X3SEDITECHSAP x3sts2
	ON (x3cs.CHAR_TYP||'-I' = x3sts2.STRNO)
LEFT JOIN ASSET IsolatoriCargados
	ON (x3sts2.STRNO = IsolatoriCargados.X3SAPID
		AND IsolatoriCargados.CLASSSTRUCTUREID = '1039')
WHERE x3sts.DUPLICATE = 1 
AND x3sts.RBNR IN ('PMTSTLII')
"""

sqfQueryIsolatori=SqlFormat(sqlIsolatori)
resultSetIsolatori=dbShortcut.executeQuery(sqfQueryIsolatori.format())

while resultSetIsolatori.next():
    assetnum=resultSetIsolatori.getString("ASSETNUM")
    x3sapid=resultSetIsolatori.getString("X3SAPID")
    strno=resultSetIsolatori.getString("STRNO")
    concaten=resultSetIsolatori.getString("CONCATEN")
    i=i+1
    AssetSet=mbo.getMboSet("$ASS$","ASSET", "ASSETNUM='"+ assetnum + "'")
    AssetSet.reset()
    asset=AssetSet.getMbo(0)
    asset.setValue("X3SAPID",concaten)
    AssetSet.save()

resultSetIsolatori.close()
log("FIN","Actualizacion X3SAPID duplicada Isolatori",i,"","Fin Actualizacion X3SAPID de Isolatori duplicados SAP")


dbShortcut.close()