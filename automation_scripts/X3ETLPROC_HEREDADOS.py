#X3ETLPROC_HEREDADOS
#Modified on 2023-11-24 by p.cano: Removed wrong LIN_RIF_ASSET attributes, created script for the right LIN_RIF_ASSET attributes (only LIN_RIF_ASSET_FDG for some assets has to be populated).
#Modified on 2023-11-24 by l.peco.san.nicolas: Added conditions to all querys to update only specs without value defined, removed/commented unnecesary fields on select to improve performance
#Modified on 2023-11-23 by l.peco.san.nicolas: Added update for attribute LIN_TIPO_ATTACCO for 1038

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
    time.sleep(2)

mbo.setValue("DESCRIPTION","ENTRA POR ATRIBUTOS HEREDADOS")


i=0
log("INICIO","Carga de atributos heredados sobre activos no SAP","","","Inicio Carga de atributos heredados sobre activos no SAP")

sql = """
SELECT 
    --asset.ASSETNUM, 
    --asset.CLASSSTRUCTUREID,
    --classtruc.CLASSIFICATIONID,
    --aspec.ASSETATTRID,
    aspec.ASSETSPECID,
    --aattrib.DATATYPE,
    --x3map.SAPATT,
    CASE 
        WHEN x3sapcar.ATNAM IS NULL THEN NULL
        WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NOT NULL THEN 'NUMVALUE'
        WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN 'NUMVALUE'
        WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
        WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'TABLEVALUE'
        WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN 'TABLEVALUE'
        WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
        WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'ALNVALUE'
        WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'ALNVALUE'
        WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NULL THEN NULL
        WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NOT NULL THEN 'DATEVALUE'
        WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NULL THEN NULL
        ELSE 'Escenario no contemplado'
    END AS CAMPODESTINAZIONE,
    CASE 
		WHEN x3sapcar.ATNAM IS NULL THEN NULL
		WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NOT NULL THEN TO_CHAR(x3sapcar.NUM_TYP)
    	WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN REPLACE(TO_CHAR(x3sapcar.DEC_TYP),',','.')
     	WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NOT NULL THEN x3sapcar.CHAR_TYP
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN REPLACE(TO_CHAR(x3sapcar.DEC_TYP),',','.')
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NOT NULL THEN x3sapcar.CHAR_TYP
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NOT NULL THEN REPLACE(TO_CHAR(x3sapcar.DEC_TYP),',','.')
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NOT NULL THEN TO_CHAR(x3sapcar.DAT_TYP,'yyyy-mm-dd')
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NULL THEN NULL
        ELSE 'Escenario no contemplado'
    END AS VALOREDESTINAZIONE
FROM ASSET asset
INNER JOIN CLASSSTRUCTURE classtruc
    ON (asset.CLASSSTRUCTUREID = classtruc.CLASSSTRUCTUREID)
INNER JOIN ASSETSPEC aspec
    ON (asset.ASSETNUM = aspec.ASSETNUM
        AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID)
INNER JOIN ASSETATTRIBUTE aattrib
    ON (aspec.ASSETATTRID = aattrib.ASSETATTRID)
INNER JOIN X3SAPMAXCLASS x3map
    ON (aattrib.ASSETATTRID = x3map.MAXATT
        AND aspec.CLASSSTRUCTUREID = x3map.MAXCLASS)
INNER JOIN ASSET assetparent
    ON (asset.PARENT = assetparent.ASSETNUM)
INNER JOIN X3CARATSAP x3sapcar
    ON (x3map.SAPCLASS = x3sapcar.KLASSE
        AND x3map.SAPATT = x3sapcar.ATNAM
        AND assetparent.X3SAPIDETL = x3sapcar.OBJECT_ID)
WHERE asset.LOADED = 1
AND asset.CLASSSTRUCTUREID IN ('1006','1007','1009','1010','1011','1012','1015','1017','1020','1022','1024','1028','1029','1030','1035','1037','1038','1040','1041','1046','1052')
AND aspec.ASSETATTRID NOT IN ('LIN_SIGLA_UNIFICAZIONE_FDG','LIN_TIPO_ATTACCO','LIN_TIPO_FUNE_GUARDIA','LIN_UNIFICAZIONE_FDG')
AND ((asset.CLASSSTRUCTUREID = '1022' AND aspec.ASSETATTRID <> 'LIN_ANNO_INSTALLAZIONE') OR (asset.CLASSSTRUCTUREID <> '1022' AND aspec.ASSETATTRID = 'LIN_ANNO_INSTALLAZIONE') OR (asset.CLASSSTRUCTUREID <> '1022' AND aspec.ASSETATTRID <> 'LIN_ANNO_INSTALLAZIONE'))
AND (to_char(aspec.NUMVALUE)||aspec.TABLEVALUE||aspec.ALNVALUE||to_char(aspec.DATEVALUE) IS NULL)
AND (x3sapcar.NUM_TYP IS NOT NULL OR x3sapcar.DEC_TYP IS NOT NULL OR x3sapcar.CHAR_TYP IS NOT NULL OR x3sapcar.DAT_TYP IS NOT NULL)
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    #assetnum=resultSet.getString("ASSETNUM")
    #classstructureid=resultSet.getString("CLASSSTRUCTUREID")
    #classificationid=resultSet.getString("CLASSIFICATIONID")
    #assetattrid=resultSet.getString("ASSETATTRID")
    assetspecid=resultSet.getString("ASSETSPECID")
    #datatype=resultSet.getString("DATATYPE")
    #atnam=resultSet.getString("SAPATT")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINAZIONE")
    if not valoredestinazione is None:
        i=i+1
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        ASSPECSet.reset()
        asspec=ASSPECSet.getMbo(0)
        if campodestinazione== "NUMVALUE":
            asspec.setValue("NUMVALUE",valoredestinazione,11L)
        if campodestinazione== "TABLEVALUE":
            asspec.setValue("TABLEVALUE",valoredestinazione,11L)
        if campodestinazione== "ALNVALUE":
            asspec.setValue("ALNVALUE",valoredestinazione,11L)
        if campodestinazione== "DATEVALUE":
            asspec.setValue("DATEVALUE",valoredestinazione,11L)
        ASSPECSet.save()
        ASSPECSet.close()

resultSet.close()
log("FIN","Carga de atributos heredados sobre activos no SAP",i,"","Fin Carga de atributos heredados sobre activos no SAP")

i=0
log("INICIO","Carga de atributos de sede tecnica que no genera el activo - ALN18 a Morsetteria","","","Inicio Carga de atributos de sede tecnica que no genera el activo - ALN18 a Morsetteria")

sql = """
SELECT 
	aspec.ASSETSPECID,
	CASE 
		WHEN x3sapcar.ATNAM IS NULL THEN NULL
		WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NOT NULL THEN 'NUMVALUE'
    	WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN 'NUMVALUE'
     	WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'TABLEVALUE'
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN 'TABLEVALUE'
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'ALNVALUE'
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'ALNVALUE'
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NOT NULL THEN 'DATEVALUE'
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NULL THEN NULL
     	ELSE 'Escenario no contemplado'
	END AS CAMPODESTINAZIONE,
	CASE 
		WHEN x3sapcar.ATNAM IS NULL THEN NULL
		WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NOT NULL THEN TO_CHAR(x3sapcar.NUM_TYP)
    	WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN REPLACE(TO_CHAR(x3sapcar.DEC_TYP),',','.')
     	WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NOT NULL THEN x3sapcar.CHAR_TYP
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN REPLACE(TO_CHAR(x3sapcar.DEC_TYP),',','.')
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NOT NULL THEN x3sapcar.CHAR_TYP
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NOT NULL THEN REPLACE(TO_CHAR(x3sapcar.DEC_TYP),',','.')
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NOT NULL THEN TO_CHAR(x3sapcar.DAT_TYP,'yyyy-mm-dd')
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NULL THEN NULL
     	ELSE 'Escenario no contemplado'
	END AS VALOREDESTINAZIONE
FROM ASSET asset
INNER JOIN CLASSSTRUCTURE classtruc
	ON (asset.CLASSSTRUCTUREID = classtruc.CLASSSTRUCTUREID)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID)
INNER JOIN ASSETATTRIBUTE aattrib
	ON (aspec.ASSETATTRID = aattrib.ASSETATTRID)
INNER JOIN X3SAPMAXCLASS x3map
	ON (aattrib.ASSETATTRID = x3map.MAXATT
		AND aspec.CLASSSTRUCTUREID = x3map.MAXCLASS)
INNER JOIN X3CARATSAP x3sapcar
	ON (x3map.SAPCLASS = x3sapcar.KLASSE
		AND x3map.SAPATT = x3sapcar.ATNAM
		AND SUBSTR(asset.X3SAPIDETL,0,17)||'M' = x3sapcar.OBJECT_ID)
WHERE asset.LOADED = 1
AND asset.CLASSSTRUCTUREID IN ('1006','1010','1012','1017','1020','1038')
AND aspec.ASSETATTRID IN ('LIN_ANNO_INSTALLAZIONE','LIN_ANNO_INSTALLAZIONE_DER','LIN_ANNO_INSTALLAZIONE_LIN','LIN_ANNO_INSTALLAZIONE_RIC','LIN_NUM_OGGETTI','LIN_SIGLA_UNIFICAZIONE_DER','LIN_SIGLA_UNIFICAZIONE_LIN','LIN_SIGLA_UNIFICAZIONE_RIC','LIN_TIPO_ARMAMENTO_DER','LIN_TIPO_ARMAMENTO_LIN','LIN_TIPO_ARMAMENTO_RIC','LIN_TIPO_ATTACCO','LIN_UNIFICAZIONE_DER','LIN_UNIFICAZIONE_LIN','LIN_UNIFICAZIONE_RIC')
AND (x3sapcar.NUM_TYP IS NOT NULL OR x3sapcar.DEC_TYP IS NOT NULL OR x3sapcar.CHAR_TYP IS NOT NULL OR x3sapcar.DAT_TYP IS NOT NULL)
AND (TO_CHAR(aspec.NUMVALUE)||aspec.TABLEVALUE||aspec.ALNVALUE||TO_CHAR(aspec.DATEVALUE,'yyyymmdd') IS NULL)
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    #assetnum=resultSet.getString("ASSETNUM")
    #classstructureid=resultSet.getString("CLASSSTRUCTUREID")
    #classificationid=resultSet.getString("CLASSIFICATIONID")
    #assetattrid=resultSet.getString("ASSETATTRID")
    assetspecid=resultSet.getString("ASSETSPECID")
    #datatype=resultSet.getString("DATATYPE")
    #atnam=resultSet.getString("SAPATT")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINAZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            ASSPECSet.close()
            i=i+1

resultSet.close()
log("FIN","Carga de atributos de sede tecnica que no genera el activo - ALN18 a Morsetteria",i,"","Fin Carga de atributos de sede tecnica que no genera el activo - ALN18 a Morsetteria")

i=0
log("INICIO","Carga de atributos Especificos: LIN_RIF_ASSET_FDG para 1018, 1019 y 1021","","","Inicio Carga de atributos Especificos: LIN_RIF_ASSET_FDG para 1018, 1019 y 1021")

sql = """
SELECT 
	ASSETSPEC.ASSETSPECID,
	'TABLEVALUE' AS CAMPODESTINAZIONE,
    ASSET2.ASSETNUM AS VALOREDESTINAZIONE
FROM ASSETSPEC 
INNER JOIN ASSET ASSET1 ON ASSETSPEC.ASSETNUM = ASSET1.ASSETNUM
INNER JOIN ASSET ASSET2 ON SUBSTR(ASSET1.X3SAPIDETL,0,17)||'G' = ASSET2.X3SAPIDETL
WHERE ASSETSPEC.ASSETATTRID = 'LIN_RIF_ASSET_FDG'
AND ASSET1.LOADED = 1
AND ASSET2.LOADED = 1
AND ASSET2.CLASSSTRUCTUREID = '1022'
AND (
(ASSETSPEC.CLASSSTRUCTUREID = '1018' AND ASSET1.DESCRIPTION = 'ANTIVIBRANTI FUNE DI GUARDIA DESTRA' AND ASSET2.DESCRIPTION = 'FUNE DI GUARDIA DESTRA')
OR
(ASSETSPEC.CLASSSTRUCTUREID = '1018' AND ASSET1.DESCRIPTION = 'ANTIVIBRANTI FUNE DI GUARDIA SINISTRA' AND ASSET2.DESCRIPTION = 'FUNE DI GUARDIA SINISTRA')
OR
(ASSETSPEC.CLASSSTRUCTUREID = '1018' AND ASSET1.DESCRIPTION = 'ANTIVIBRANTI FUNE DI GUARDIA TERZA' AND ASSET2.DESCRIPTION = 'FUNE DI GUARDIA TERZA')
OR
(ASSETSPEC.CLASSSTRUCTUREID = '1019' AND ASSET1.DESCRIPTION = 'BARRETTA PREFORMATA F.G DESTRA' AND ASSET2.DESCRIPTION = 'FUNE DI GUARDIA DESTRA')
OR
(ASSETSPEC.CLASSSTRUCTUREID = '1019' AND ASSET1.DESCRIPTION = 'BARRETTA PREFORMATA F.G SINISTRA' AND ASSET2.DESCRIPTION = 'FUNE DI GUARDIA SINISTRA')
OR
(ASSETSPEC.CLASSSTRUCTUREID = '1019' AND ASSET1.DESCRIPTION = 'BARRETTA PREFORMATA F.G TERZA' AND ASSET2.DESCRIPTION = 'FUNE DI GUARDIA TERZA')
OR
(ASSETSPEC.CLASSSTRUCTUREID = '1021' AND ASSET1.DESCRIPTION = 'FIBRA OTTICA FUNE DI GUARDIA DESTRA' AND ASSET2.DESCRIPTION = 'FUNE DI GUARDIA DESTRA')
OR
(ASSETSPEC.CLASSSTRUCTUREID = '1021' AND ASSET1.DESCRIPTION = 'FIBRA OTTICA FUNE DI GUARDIA SINISTRA' AND ASSET2.DESCRIPTION = 'FUNE DI GUARDIA SINISTRA')
OR
(ASSETSPEC.CLASSSTRUCTUREID = '1021' AND ASSET1.DESCRIPTION = 'FIBRA OTTICA FUNE DI GUARDIA TERZA' AND ASSET2.DESCRIPTION = 'FUNE DI GUARDIA TERZA')
)
AND ASSET1.ASSETNUM NOT IN (SELECT ASSETNUM FROM ASSET WHERE X3SAPIDETL LIKE ('22213A1%') OR X3SAPIDETL LIKE ('21323A1%') OR X3SAPIDETL LIKE ('21322B1%'))
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    #assetnum=resultSet.getString("ASSETNUM")
    #classstructureid=resultSet.getString("CLASSSTRUCTUREID")
    #classificationid=resultSet.getString("CLASSIFICATIONID")
    #assetattrid=resultSet.getString("ASSETATTRID")
    assetspecid=resultSet.getString("ASSETSPECID")
    #datatype=resultSet.getString("DATATYPE")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINAZIONE")
    if not valoredestinazione is None:
        i=i+1
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        ASSPECSet.reset()
        asspec=ASSPECSet.getMbo(0)
        if campodestinazione== "NUMVALUE":
            asspec.setValue("NUMVALUE",valoredestinazione,11L)
        if campodestinazione== "TABLEVALUE":
            asspec.setValue("TABLEVALUE",valoredestinazione,11L)
        if campodestinazione== "ALNVALUE":
            asspec.setValue("ALNVALUE",valoredestinazione,11L)
        if campodestinazione== "DATEVALUE":
            asspec.setValue("DATEVALUE",valoredestinazione,11L)
        ASSPECSet.save()

resultSet.close()
log("FIN","Carga de atributos Especificos: LIN_RIF_ASSET_FDG para 1018, 1019 y 1021",i,"","Fin Carga de atributos Especificos: LIN_RIF_ASSET_FDG para 1018, 1019 y 1021")

i=0
log("INICIO","Carga de atributos Especificos: LIN_RIF_FASE para 1009 y 1038","","","Inicio Carga de atributos Especificos: LIN_RIF_FASE para 1009 y 1038")

sql = """
SELECT 
	--asset.ASSETNUM, 
	--asset.CLASSSTRUCTUREID,
	--classtruc.CLASSIFICATIONID,
	--aspec.ASSETATTRID,
	aspec.ASSETSPECID,
	--aattrib.DATATYPE,
	CASE 
		WHEN aattrib.DATATYPE = 'NUMERIC' THEN 'NUMVALUE'
     	WHEN aattrib.DATATYPE = 'TABLE' THEN 'TABLEVALUE'
     	WHEN aattrib.DATATYPE = 'ALN' THEN 'ALNVALUE'
     	WHEN aattrib.DATATYPE = 'DATE' THEN 'DATEVALUE'
     	ELSE 'Escenario no contemplado'
	END AS CAMPODESTINAZIONE,
	CASE
		WHEN asset.DESCRIPTION = 'CONDUTTORE SINISTRO' THEN 'S'
		WHEN asset.DESCRIPTION = 'CONDUTTORE DESTRO' THEN 'D'
		WHEN asset.DESCRIPTION = 'CONDUTTORE TERZO' THEN 'T'
		WHEN asset.DESCRIPTION = 'ARMAMENTO CONDUTTORE SINISTRA' THEN 'S'
		WHEN asset.DESCRIPTION = 'ARMAMENTO CONDUTTORE DESTRA' THEN 'D'
		WHEN asset.DESCRIPTION = 'ARMAMENTO CONDUTTORE TERZA' THEN 'T'
	END AS VALOREDESTINAZIONE
FROM ASSET asset
INNER JOIN CLASSSTRUCTURE classtruc
	ON (asset.CLASSSTRUCTUREID = classtruc.CLASSSTRUCTUREID)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID)
INNER JOIN ASSETATTRIBUTE aattrib
	ON (aspec.ASSETATTRID = aattrib.ASSETATTRID)
WHERE asset.LOADED = 1
AND asset.CLASSSTRUCTUREID in ('1009','1038')
AND aspec.ASSETATTRID = 'LIN_RIF_FASE'
AND (to_char(aspec.NUMVALUE)||aspec.TABLEVALUE||aspec.ALNVALUE||to_char(aspec.DATEVALUE) IS NULL)
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    #assetnum=resultSet.getString("ASSETNUM")
    #classstructureid=resultSet.getString("CLASSSTRUCTUREID")
    #classificationid=resultSet.getString("CLASSIFICATIONID")
    #assetattrid=resultSet.getString("ASSETATTRID")
    assetspecid=resultSet.getString("ASSETSPECID")
    #datatype=resultSet.getString("DATATYPE")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINAZIONE")
    if not valoredestinazione is None:
        i=i+1
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        ASSPECSet.reset()
        asspec=ASSPECSet.getMbo(0)
        if campodestinazione== "NUMVALUE":
            asspec.setValue("NUMVALUE",valoredestinazione,11L)
        if campodestinazione== "TABLEVALUE":
            asspec.setValue("TABLEVALUE",valoredestinazione,11L)
        if campodestinazione== "ALNVALUE":
            asspec.setValue("ALNVALUE",valoredestinazione,11L)
        if campodestinazione== "DATEVALUE":
            asspec.setValue("DATEVALUE",valoredestinazione,11L)
        ASSPECSet.save()

resultSet.close()
log("FIN","Carga de atributos Especificos: LIN_RIF_FASE para 1009 y 1038",i,"","Fin Carga de atributos Especificos: LIN_RIF_FASE para 1009 y 1038")


i=0
log("INICIO","Carga de atributos Especificos: LIN_RIF_FUNE para 1022 y 1041","","","Inicio Carga de atributos Especificos: LIN_RIF_FUNE para 1022 y 1041")

sql = """
SELECT 
	--asset.ASSETNUM, 
	--asset.CLASSSTRUCTUREID,
	--asset.DESCRIPTION,
	--classtruc.CLASSIFICATIONID,
	--aspec.ASSETATTRID,
	aspec.ASSETSPECID,
	--aattrib.DATATYPE,
	CASE 
		WHEN aattrib.DATATYPE = 'NUMERIC' THEN 'NUMVALUE'
     	WHEN aattrib.DATATYPE = 'TABLE' THEN 'TABLEVALUE'
     	WHEN aattrib.DATATYPE = 'ALN' THEN 'ALNVALUE'
     	WHEN aattrib.DATATYPE = 'DATE' THEN 'DATEVALUE'
     	ELSE 'Escenario no contemplado'
	END AS CAMPODESTINAZIONE,
	CASE
		WHEN asset.DESCRIPTION LIKE '%SINISTRA' THEN 'SX'
		WHEN asset.DESCRIPTION LIKE '%DESTRA' THEN 'DX'
		WHEN asset.DESCRIPTION LIKE '%TERZA' THEN '3'
	END AS VALOREDESTINAZIONE
FROM ASSET asset
INNER JOIN CLASSSTRUCTURE classtruc
	ON (asset.CLASSSTRUCTUREID = classtruc.CLASSSTRUCTUREID)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID)
INNER JOIN ASSETATTRIBUTE aattrib
	ON (aspec.ASSETATTRID = aattrib.ASSETATTRID)
WHERE asset.LOADED = 1
AND asset.CLASSSTRUCTUREID IN ('1022','1041')
AND aspec.ASSETATTRID = 'LIN_RIF_FUNE'
AND (to_char(aspec.NUMVALUE)||aspec.TABLEVALUE||aspec.ALNVALUE||to_char(aspec.DATEVALUE) IS NULL)
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    #assetnum=resultSet.getString("ASSETNUM")
    #classstructureid=resultSet.getString("CLASSSTRUCTUREID")
    #classificationid=resultSet.getString("CLASSIFICATIONID")
    #assetattrid=resultSet.getString("ASSETATTRID")
    assetspecid=resultSet.getString("ASSETSPECID")
    #datatype=resultSet.getString("DATATYPE")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINAZIONE")
    if not valoredestinazione is None:
        i=i+1
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        ASSPECSet.reset()
        asspec=ASSPECSet.getMbo(0)
        if campodestinazione== "NUMVALUE":
            asspec.setValue("NUMVALUE",valoredestinazione,11L)
        if campodestinazione== "TABLEVALUE":
            asspec.setValue("TABLEVALUE",valoredestinazione,11L)
        if campodestinazione== "ALNVALUE":
            asspec.setValue("ALNVALUE",valoredestinazione,11L)
        if campodestinazione== "DATEVALUE":
            asspec.setValue("DATEVALUE",valoredestinazione,11L)
        ASSPECSet.save()

resultSet.close()
log("FIN","Carga de atributos Especificos: LIN_RIF_FUNE para 1022 y 1041",i,"","Fin Carga de atributos Especificos: LIN_RIF_FUNE para 1022 y 1041")

i=0
log("INICIO","Carga de atributos Especificos: LIN_TRONCO_ISPEZIONE para 1005 y 1051","","","Inicio Carga de atributos Especificos: LIN_TRONCO_ISPEZIONE para 1005 y 1051")

sql = """
SELECT 
	--asset.ASSETNUM, 
	--asset.X3SAPIDETL,
	--asset.CLASSSTRUCTUREID,
	--classtruc.CLASSIFICATIONID,
	--aspec.ASSETATTRID,
	aspec.ASSETSPECID,
--	TO_CHAR(aspec.NUMVALUE)||aspec.TABLEVALUE||aspec.ALNVALUE||TO_CHAR(aspec.DATEVALUE,'dd.mm.yyyy') AS VALUE,
	--aattrib.DATATYPE,
--	x3map.MAXATT,
--	x3map.MAXCLASS,
--	x3map.SAPATT,
--	x3map.SAPCLASS,
	CASE 
		WHEN x3sapcar.ATNAM IS NULL THEN NULL
		WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NOT NULL THEN 'NUMVALUE'
    		WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN 'NUMVALUE'
     	WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'TABLEVALUE'
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN 'TABLEVALUE'
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'ALNVALUE'
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'ALNVALUE'
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NOT NULL THEN 'DATEVALUE'
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NULL THEN NULL
     	ELSE 'Escenario no contemplado'
	END AS CAMPODESTINAZIONE,
	x3ind.TRONCONUM AS VALOREDESTINAZIONE,
--	cspec.DOMAINID,
--	cspec.TABLEATTRIBUTE,
--	cspec.LOOKUPNAME,
--	x3ind.SAPID,
--	x3ind.TRONCONUM,
	''
FROM ASSET asset
INNER JOIN CLASSSTRUCTURE classtruc
	ON (asset.CLASSSTRUCTUREID = classtruc.CLASSSTRUCTUREID)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID)
INNER JOIN ASSETATTRIBUTE aattrib
	ON (aspec.ASSETATTRID = aattrib.ASSETATTRID)
INNER JOIN X3SAPMAXCLASS x3map
	ON (aattrib.ASSETATTRID = x3map.MAXATT
		AND aspec.CLASSSTRUCTUREID = x3map.MAXCLASS)
INNER JOIN X3CARATSAP x3sapcar
	ON (x3map.SAPCLASS = x3sapcar.KLASSE
		AND x3map.SAPATT = x3sapcar.ATNAM
		AND SUBSTR(asset.X3SAPIDETL,1,16) = x3sapcar.OBJECT_ID)
INNER JOIN CLASSSPEC cspec
	ON (cspec.ASSETATTRID = aspec.ASSETATTRID
		AND cspec.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID)
INNER JOIN X3TRONCHI x3ind
	ON (x3sapcar.CHAR_TYP = x3ind.SAPID)
WHERE asset.LOADED = 1
AND asset.CLASSSTRUCTUREID IN ('1005','1051')
AND aspec.ASSETATTRID IN ('LIN_TRONCO_ISPEZIONE')
AND (x3sapcar.NUM_TYP IS NOT NULL OR x3sapcar.DEC_TYP IS NOT NULL OR x3sapcar.CHAR_TYP IS NOT NULL OR x3sapcar.DAT_TYP IS NOT NULL)
AND (TO_CHAR(aspec.NUMVALUE)||aspec.TABLEVALUE||aspec.ALNVALUE||TO_CHAR(aspec.DATEVALUE,'yyyymmdd') IS NULL)
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    #assetnum=resultSet.getString("ASSETNUM")
    #classstructureid=resultSet.getString("CLASSSTRUCTUREID")
    #classificationid=resultSet.getString("CLASSIFICATIONID")
    #assetattrid=resultSet.getString("ASSETATTRID")
    assetspecid=resultSet.getString("ASSETSPECID")
    #datatype=resultSet.getString("DATATYPE")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINAZIONE")
    if not valoredestinazione is None:
        i=i+1
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        ASSPECSet.reset()
        asspec=ASSPECSet.getMbo(0)
        if campodestinazione== "NUMVALUE":
            asspec.setValue("NUMVALUE",valoredestinazione,11L)
        if campodestinazione== "TABLEVALUE":
            asspec.setValue("TABLEVALUE",valoredestinazione,11L)
        if campodestinazione== "ALNVALUE":
            asspec.setValue("ALNVALUE",valoredestinazione,11L)
        if campodestinazione== "DATEVALUE":
            asspec.setValue("DATEVALUE",valoredestinazione,11L)
        ASSPECSet.save()

resultSet.close()
log("FIN","Carga de atributos Especificos: LIN_TRONCO_ISPEZIONE para 1005 y 1051",i,"","Fin Carga de atributos Especificos: LIN_TRONCO_ISPEZIONE para 1005 y 1051")


i=0
log("INICIO","Carga de atributos Especificos: LIN_ELEMENTO_INDISP para 1003, 1005 y 1051","","","Inicio Carga de atributos Especificos: LIN_ELEMENTO_INDISP para 1003, 1005 y 1051")

sql = """
SELECT 
	--asset.ASSETNUM, 
	--asset.X3SAPIDETL,
	--asset.CLASSSTRUCTUREID,
	--classtruc.CLASSIFICATIONID,
	--aspec.ASSETATTRID,
	aspec.ASSETSPECID,
--	TO_CHAR(aspec.NUMVALUE)||aspec.TABLEVALUE||aspec.ALNVALUE||TO_CHAR(aspec.DATEVALUE,'dd.mm.yyyy') AS VALUE,
	--aattrib.DATATYPE,
--	x3map.MAXATT,
--	x3map.MAXCLASS,
--	x3map.SAPATT,
--	x3map.SAPCLASS,
	CASE 
		WHEN x3sapcar.ATNAM IS NULL THEN NULL
		WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NOT NULL THEN 'NUMVALUE'
    	WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN 'NUMVALUE'
     	WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'TABLEVALUE'
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN 'TABLEVALUE'
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'ALNVALUE'
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NOT NULL THEN 'ALNVALUE'
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NOT NULL THEN 'DATEVALUE'
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NULL THEN NULL
     	ELSE 'Escenario no contemplado'
	END AS CAMPODESTINAZIONE,
	CASE 
		WHEN x3sapcar.ATNAM IS NULL THEN NULL
		WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NOT NULL THEN TO_CHAR(x3sapcar.NUM_TYP)
    	WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN TO_CHAR(x3sapcar.DEC_TYP)
     	WHEN aattrib.DATATYPE = 'NUMERIC' AND x3sapcar.NUM_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NOT NULL THEN x3sapcar.CHAR_TYP
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NOT NULL THEN TO_CHAR(x3sapcar.DEC_TYP)
     	WHEN aattrib.DATATYPE = 'TABLE' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.DEC_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NOT NULL THEN x3sapcar.CHAR_TYP
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NOT NULL THEN TO_CHAR(x3sapcar.DEC_TYP)
     	WHEN aattrib.DATATYPE = 'ALN' AND x3sapcar.CHAR_TYP IS NULL AND x3sapcar.CHAR_TYP IS NULL THEN NULL
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NOT NULL THEN TO_CHAR(x3sapcar.DAT_TYP,'dd.mm.yyyy')
     	WHEN aattrib.DATATYPE = 'DATE' AND x3sapcar.DAT_TYP IS NULL THEN NULL
     	ELSE 'Escenario no contemplado'
	END AS VALOREDESTINAZIONE,
--	cspec.DOMAINID,
--	cspec.TABLEATTRIBUTE,
--	cspec.LOOKUPNAME,
--	x3ind.STRNO,
    ''
FROM ASSET asset
LEFT JOIN CLASSSTRUCTURE classtruc
	ON (asset.CLASSSTRUCTUREID = classtruc.CLASSSTRUCTUREID)
LEFT JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID)
LEFT JOIN ASSETATTRIBUTE aattrib
	ON (aspec.ASSETATTRID = aattrib.ASSETATTRID)
LEFT JOIN X3SAPMAXCLASS x3map
	ON (aattrib.ASSETATTRID = x3map.MAXATT
		AND aspec.CLASSSTRUCTUREID = x3map.MAXCLASS)
LEFT JOIN X3CARATSAP x3sapcar
	ON (x3map.SAPCLASS = x3sapcar.KLASSE
		AND x3map.SAPATT = x3sapcar.ATNAM
		AND SUBSTR(asset.X3SAPIDETL,1,16) = x3sapcar.OBJECT_ID)
--LEFT JOIN CLASSSPEC cspec
--	ON (cspec.ASSETATTRID = aspec.ASSETATTRID
--		AND cspec.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID)
--LEFT JOIN X3SEDIINDISP x3ind
--	ON (x3sapcar.CHAR_TYP = x3ind.STRNO)
WHERE asset.LOADED = 1
AND asset.CLASSSTRUCTUREID IN ('1003','1005','1051')
AND aspec.ASSETATTRID IN ('LIN_ELEMENTO_INDISP')
AND (x3sapcar.NUM_TYP IS NOT NULL OR x3sapcar.DEC_TYP IS NOT NULL OR x3sapcar.CHAR_TYP IS NOT NULL OR x3sapcar.DAT_TYP IS NOT NULL)
AND (TO_CHAR(aspec.NUMVALUE)||aspec.TABLEVALUE||aspec.ALNVALUE||TO_CHAR(aspec.DATEVALUE,'yyyymmdd') IS NULL)
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    #assetnum=resultSet.getString("ASSETNUM")
    #classstructureid=resultSet.getString("CLASSSTRUCTUREID")
    #classificationid=resultSet.getString("CLASSIFICATIONID")
    #assetattrid=resultSet.getString("ASSETATTRID")
    assetspecid=resultSet.getString("ASSETSPECID")
    #datatype=resultSet.getString("DATATYPE")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINAZIONE")
    if not valoredestinazione is None:
        i=i+1
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        ASSPECSet.reset()
        asspec=ASSPECSet.getMbo(0)
        if campodestinazione== "NUMVALUE":
            asspec.setValue("NUMVALUE",valoredestinazione,11L)
        if campodestinazione== "TABLEVALUE":
            asspec.setValue("TABLEVALUE",valoredestinazione,11L)
        if campodestinazione== "ALNVALUE":
            asspec.setValue("ALNVALUE",valoredestinazione,11L)
        if campodestinazione== "DATEVALUE":
            asspec.setValue("DATEVALUE",valoredestinazione,11L)
        ASSPECSet.save()

resultSet.close()
log("FIN","Carga de atributos Especificos: LIN_ELEMENTO_INDISP para 1003, 1005 y 1051",i,"","Fin Carga de atributos Especificos: LIN_ELEMENTO_INDISP para 1003, 1005 y 1051")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 DESTRA","","","Inicio Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 DESTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'NUMVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.NUM_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1018'
		AND asset.DESCRIPTION = 'ANTIVIBRANTI FUNE DI GUARDIA DESTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_NUM_OGGETTI')
WHERE x3sapcar.ATNAM = 'PMT_NUM_ANTIVIBR_CDG_DX'
AND x3sapcar.NUM_TYP IS NOT NULL
AND aspec.NUMVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 DESTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 DESTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 SINISTRA","","","Inicio Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 SINISTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'NUMVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.NUM_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1018'
		AND asset.DESCRIPTION = 'ANTIVIBRANTI FUNE DI GUARDIA SINISTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_NUM_OGGETTI')
WHERE x3sapcar.ATNAM = 'PMT_NUM_ANTIVIBR_CDG_SX'
AND x3sapcar.NUM_TYP IS NOT NULL
AND aspec.NUMVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 SINISTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 SINISTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 TERZA","","","Inicio Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 TERZA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'NUMVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.NUM_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1018'
		AND asset.DESCRIPTION = 'ANTIVIBRANTI FUNE DI GUARDIA TERZA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_NUM_OGGETTI')
WHERE x3sapcar.ATNAM = 'PMT_NUM_ANTIVIBR_CDG_3'
AND x3sapcar.NUM_TYP IS NOT NULL
AND aspec.NUMVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 TERZA",i,"","Fin Carga de atributos especificos de SAP: LIN_NUM_OGGETTI para 1018 TERZA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 DESTRA","","","Inicio Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 DESTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'NUMVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.NUM_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1022'
		AND asset.DESCRIPTION = 'FUNE DI GUARDIA DESTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_ANNO_INSTALLAZIONE')
WHERE x3sapcar.ATNAM = 'PMT_ANNO_INST_DX'
AND x3sapcar.NUM_TYP IS NOT NULL
AND aspec.NUMVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 DESTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 DESTRA")

log("INICIO","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 SINISTRA","","","Inicio Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 SINISTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'NUMVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.NUM_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1022'
		AND asset.DESCRIPTION = 'FUNE DI GUARDIA SINISTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_ANNO_INSTALLAZIONE')
WHERE x3sapcar.ATNAM = 'PMT_ANNO_INST_SX'
AND x3sapcar.NUM_TYP IS NOT NULL
AND aspec.NUMVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 SINISTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 SINISTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 TERZA","","","Inicio Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 TERZA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'NUMVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.NUM_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1022'
		AND asset.DESCRIPTION = 'FUNE DI GUARDIA TERZA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_ANNO_INSTALLAZIONE')
WHERE x3sapcar.ATNAM = 'PMT_ANNO_INST_3'
AND x3sapcar.NUM_TYP IS NOT NULL
AND aspec.NUMVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 TERZA",i,"","Fin Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1022 TERZA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 DESTRA","","","Inicio Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 DESTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1022'
		AND asset.DESCRIPTION = 'FUNE DI GUARDIA DESTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_TIPO_FUNE_GUARDIA')
WHERE x3sapcar.ATNAM = 'PMT_TIPO_CDG_DX'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 DESTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 DESTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 SINISTRA","","","Inicio Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 SINISTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1022'
		AND asset.DESCRIPTION = 'FUNE DI GUARDIA SINISTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_TIPO_FUNE_GUARDIA')
WHERE x3sapcar.ATNAM = 'PMT_TIPO_CDG_SX'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 SINISTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 SINISTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 TERZA","","","Inicio Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 TERZA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1022'
		AND asset.DESCRIPTION = 'FUNE DI GUARDIA TERZA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_TIPO_FUNE_GUARDIA')
WHERE x3sapcar.ATNAM = 'PMT_TIPO_CDG_3'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 TERZA",i,"","Fin Carga de atributos especificos de SAP: LIN_TIPO_FUNE_GUARDIA para 1022 TERZA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 DESTRA","","","Inicio Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 DESTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'NUMVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.NUM_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA DESTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_ANNO_INSTALLAZIONE')
WHERE x3sapcar.ATNAM = 'PMT_ANNO_MORS_CDG_DX'
AND x3sapcar.NUM_TYP IS NOT NULL
AND aspec.NUMVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 DESTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 DESTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 SINISTRA","","","Inicio Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 SINISTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'NUMVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.NUM_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA SINISTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_ANNO_INSTALLAZIONE')
WHERE x3sapcar.ATNAM = 'PMT_ANNO_MORS_CDG_SX'
AND x3sapcar.NUM_TYP IS NOT NULL
AND aspec.NUMVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 SINISTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 SINISTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 TERZA","","","Inicio Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 TERZA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'NUMVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.NUM_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA TERZA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_ANNO_INSTALLAZIONE')
WHERE x3sapcar.ATNAM = 'PMT_ANNO_MORS_CDG_3'
AND x3sapcar.NUM_TYP IS NOT NULL
AND aspec.NUMVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 TERZA",i,"","Fin Carga de atributos especificos de SAP: LIN_ANNO_INSTALLAZIONE para 1041 TERZA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 DESTRA","","","Inicio Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 DESTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA DESTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_TIPO_ATTACCO')
WHERE x3sapcar.ATNAM = 'PMT_ATT_CDG_DX'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 DESTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 DESTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 SINISTRA","","","Inicio Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 SINISTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA SINISTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_TIPO_ATTACCO')
WHERE x3sapcar.ATNAM = 'PMT_ATT_CDG_SX'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 SINISTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 SINISTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 TERZA","","","Inicio Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 TERZA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA TERZA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_TIPO_ATTACCO')
WHERE x3sapcar.ATNAM = 'PMT_ATT_CDG_3'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 TERZA",i,"","Fin Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1041 TERZA")


i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 DESTRA","","","Inicio Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 DESTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET asset
	ON (asset.X3SAPIDETL = SUBSTR(x3sapcar.OBJECT_ID,1,17)||'S'
		AND asset.CLASSSTRUCTUREID = '1038'
		AND asset.DESCRIPTION = 'ARMAMENTO CONDUTTORE DESTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_TIPO_ATTACCO')
WHERE x3sapcar.ATNAM = 'PMT_ATT_COND'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 DESTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 DESTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 SINISTRA","","","Inicio Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 SINISTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET asset
	ON (asset.X3SAPIDETL = SUBSTR(x3sapcar.OBJECT_ID,1,17)||'S'
		AND asset.CLASSSTRUCTUREID = '1038'
		AND asset.DESCRIPTION = 'ARMAMENTO CONDUTTORE SINISTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_TIPO_ATTACCO')
WHERE x3sapcar.ATNAM = 'PMT_ATT_COND'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 SINISTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 SINISTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 TERZA","","","Inicio Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 TERZA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET asset
	ON (asset.X3SAPIDETL = SUBSTR(x3sapcar.OBJECT_ID,1,17)||'S'
		AND asset.CLASSSTRUCTUREID = '1038'
		AND asset.DESCRIPTION = 'ARMAMENTO CONDUTTORE TERZA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_TIPO_ATTACCO')
WHERE x3sapcar.ATNAM = 'PMT_ATT_COND'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 TERZA",i,"","Fin Carga de atributos especificos de SAP: LIN_TIPO_ATTACCO para 1038 TERZA")


i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 DESTRA","","","Inicio Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 DESTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA DESTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_SIGLA_UNIFICAZIONE_FDG')
WHERE x3sapcar.ATNAM = 'PMT_SIGLA_ARM_CDG_DX'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 DESTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 DESTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 SINISTRA","","","Inicio Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 SINISTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA SINISTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_SIGLA_UNIFICAZIONE_FDG')
WHERE x3sapcar.ATNAM = 'PMT_SIGLA_ARM_CDG_SX'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 SINISTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 SINISTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 TERZA","","","Inicio Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 TERZA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA TERZA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_SIGLA_UNIFICAZIONE_FDG')
WHERE x3sapcar.ATNAM = 'PMT_SIGLA_ARM_CDG_3'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 TERZA",i,"","Fin Carga de atributos especificos de SAP: LIN_SIGLA_UNIFICAZIONE_FDG para 1041 TERZA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 DESTRA","","","Inicio Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 DESTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA DESTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_UNIFICAZIONE_FDG')
WHERE x3sapcar.ATNAM = 'PMT_UNIFIC_MORS_CDG_DX'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 DESTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 DESTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 SINISTRA","","","Inicio Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 SINISTRA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA SINISTRA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_UNIFICAZIONE_FDG')
WHERE x3sapcar.ATNAM = 'PMT_UNIFIC_MORS_CDG_SX'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 SINISTRA",i,"","Fin Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 SINISTRA")

i=0
log("INICIO","Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 TERZA","","","Inicio Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 TERZA")

sql = """
SELECT 
	aspec.ASSETSPECID AS ASSETSPECID,
	'ALNVALUE' AS CAMPODESTINAZIONE,
	x3sapcar.CHAR_TYP AS VALOREDESTINZIONE
FROM X3CARATSAP x3sapcar
INNER JOIN ASSET
	ON (asset.X3SAPIDETL = x3sapcar.OBJECT_ID
		AND asset.CLASSSTRUCTUREID = '1041'
		AND asset.DESCRIPTION = 'ARMAMENTO FUNE DI GUARDIA TERZA'
		AND asset.LOADED = 1)
INNER JOIN ASSETSPEC aspec
	ON (asset.ASSETNUM = aspec.ASSETNUM
		AND asset.CLASSSTRUCTUREID = aspec.CLASSSTRUCTUREID
		AND aspec.ASSETATTRID = 'LIN_UNIFICAZIONE_FDG')
WHERE x3sapcar.ATNAM = 'PMT_UNIFIC_MORS_CDG_3'
AND x3sapcar.CHAR_TYP IS NOT NULL
AND aspec.ALNVALUE IS NULL
"""

sqfQuery=SqlFormat(sql)
resultSet=dbShortcut.executeQuery(sqfQuery.format())

while resultSet.next():
    
    assetspecid=resultSet.getString("ASSETSPECID")
    campodestinazione=resultSet.getString("CAMPODESTINAZIONE")
    valoredestinazione=resultSet.getString("VALOREDESTINZIONE")
    if not valoredestinazione is None:
        ASSPECSet=mbo.getMboSet("$ASPEC$","ASSETSPEC", "ASSETSPECID='"+ assetspecid + "'")
        asspec=ASSPECSet.getMbo(0)
        if not asspec is None:
            if campodestinazione== "NUMVALUE":
                asspec.setValue("NUMVALUE",valoredestinazione,11L)
            if campodestinazione== "TABLEVALUE":
                asspec.setValue("TABLEVALUE",valoredestinazione,11L)
            if campodestinazione== "ALNVALUE":
                asspec.setValue("ALNVALUE",valoredestinazione,11L)
            if campodestinazione== "DATEVALUE":
                asspec.setValue("DATEVALUE",valoredestinazione,11L)
            ASSPECSet.save()
            i=i+1
        ASSPECSet.close()

resultSet.close()

log("FIN","Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 TERZA",i,"","Fin Carga de atributos especificos de SAP: LIN_UNIFICAZIONE_FDG para 1041 TERZA")

dbShortcut.close()