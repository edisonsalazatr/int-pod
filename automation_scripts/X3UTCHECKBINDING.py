#Verifica consistenza della X3OBJECTCHANGE
#Parameters: lineeAssetnum


from psdi.server import MXServer
from psdi.mbo import SqlFormat
from psdi.mbo import DBShortcut

mxServer = MXServer.getMXServer()
dbShortcut=DBShortcut()
dbShortcut.connect(mxServer.getSystemUserInfo().getConnectionKey())
lineeAssetnum = lineeAssetnum.replace(",","','")
lineeAssetnum = "'"+lineeAssetnum+"'"

count=0 #Number of records 

sql = """
WITH class_hierarchies AS (
SELECT
	LEVEL lvl,
	lpad(' ', LEVEL, '	') || c.CLASSIFICATIONID paddedclassificationid,
	connect_by_root CLASSIFICATIONID root,
	ltrim(SYS_CONNECT_BY_PATH(CLASSIFICATIONID , '\\' ), '\\') HIERARCHYPATH,
	c.*
FROM
	CLASSSTRUCTURE c
START WITH
	CLASSIFICATIONID = 'LINEA'
CONNECT BY
	PRIOR CLASSSTRUCTUREID = PARENT
ORDER siblings BY CLASSIFICATIONID
)
SELECT
	a.ASSETNUM,
	h.hierarchypath,
	m.SAPCLASS
FROM
	ASSETANCESTOR aa
JOIN ASSETLOCRELATION ar ON
	aa.ANCESTOR = ar.targetassetnum
	AND ar.SOURCEASSETNUM IN (%s)
JOIN asset a ON
	aa.ASSETNUM = a.ASSETNUM
	AND (a.X3ROOTASSETNUM IS NULL
		OR a.X3ROOTASSETNUM = ar.SOURCEASSETNUM)
JOIN CLASS_HIERARCHIES h ON
	h.CLASSSTRUCTUREID = a.CLASSSTRUCTUREID
LEFT JOIN asset pa ON
	pa.ASSETNUM = a.PARENT
JOIN X3MAXIMOSAPMAP m ON
	m.HIERARCHYPATH = h.HIERARCHYPATH
WHERE
	(pa.X3ROOTASSETNUM IS NULL
		OR pa.X3ROOTASSETNUM = ar.SOURCEASSETNUM)
	AND (a.ASSETUID,
	m.sapclass) NOT IN (
	SELECT
		o.MAXUID,
		sa.SAPCLASS
	FROM
		X3SAPTOMAXOBJECT o,
		X3SAPASSET sa
	WHERE
		o.SAPASSETID = sa.X3SAPASSETID)
"""%lineeAssetnum
try:
    sqfQuery=SqlFormat(sql)
    resultSet=dbShortcut.executeQuery(sqfQuery.format())
    while resultSet.next():
        count+=1
        log.log("[%3s] %20s %60s %20s \t BINDING NOT FOUND"%(count,resultSet.getString("ASSETNUM"), resultSet.getString("HIERARCHYPATH"), resultSet.getString("SAPCLASS")))
    resultSet.close()
finally:
    dbShortcut.close()
    if count > 0:
        failed=True