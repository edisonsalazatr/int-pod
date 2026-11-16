# X3ETLSTZ_DATATRANSFER
# ETL SAP - Riporta i dati delle stazioni dalle tabelle di interfaccia del db on premise
# alle tabelle di staging sul db di Maximo
# v1.0 10/04/2024
# v1.1 04/10/2024
#	Aggiunta delete mode
# Parametri attesi nel contesto dello script:
#	stazStrno: elenco degli strno delle stazioni separati da virgola (es. 31202C,31204B,31301A)
#	importId: id univoco dell'import condiviso tra N chiamate legate allo stesso processo (es. 20240322161017)
#	extractionId: id univoco dell'estrazione SAP comune a tutte le tabelle a parità di flusso
#	deleteMode: indica se lo step deve essere eseguito in modalità di sola cancellazione
from psdi.server import MXServer
from java.lang import System
from com.terna.common.log import Logger

logger = Logger.getLogger("ETLSTZ.X3ETLSTZ_DATATRANSFER")

start = System.currentTimeMillis()

def throwError(msg):
	service.error("x3terna", "ErrorMsg", [msg])
	
"""
Elimina gli eventuali record creati con precedenti import
"""
def cleanPreviousImport():
	# Elimina le caratteristiche degli EQ
	deleteSql = "DELETE FROM X3CARATSAP_STZ WHERE substr(OBJNR,3) IN (SELECT e.EQUNR FROM X3EQUIPMENTSAP_STZ e WHERE SUBSTR(e.TPLNR,1,6) IN (%s))"%stazStrno
	utils.executeQuery("UPDATE", "Elimina le caratteristiche degli EQ", deleteSql)
	# Elimina le caratteristiche delle ST
	deleteSql = "DELETE FROM X3CARATSAP_STZ WHERE OBJNR IN (SELECT s.TPLNR FROM X3SEDITECHSAP_STZ s WHERE SUBSTR(s.STRNO,1,6) IN (%s))"%stazStrno
	utils.executeQuery("UPDATE", "Elimina le caratteristiche delle ST", deleteSql)
	# Elimina gli EQ
	deleteSql = "DELETE FROM X3EQUIPMENTSAP_STZ e WHERE SUBSTR(e.TPLNR,1,6) IN (%s)"%stazStrno
	utils.executeQuery("UPDATE", "Elimina gli EQ", deleteSql)
	# Elimina le ST
	deleteSql = "DELETE FROM X3SEDITECHSAP_STZ s WHERE SUBSTR(s.STRNO,1,6) IN (%s)"%stazStrno
	utils.executeQuery("UPDATE", "Elimina le ST", deleteSql)
	
# Prende la stazione da importare dal contesto
if stazStrno == None:
	throwError("Parametro stazStrno obbligatorio mancante")
stazStrno = "'" + stazStrno.replace(",","','") + "'"
if importId == None:
	throwError("Parametro importId obbligatorio mancante")
if extractionId == None and deleteMode == "0":
	throwError("Parametro extractionId obbligatorio mancante")
	
utils = service.invokeScript("X3ETLSTZ_UTILS").get("X3ETLSTZUTILS")(etlnum, "DATATRANSFER") # Utilities

try:
	# Pulisce import precedenti
	cleanPreviousImport()
	
	if deleteMode == "0":
		# Copia delle ST
		insertSql = """INSERT INTO X3SEDITECHSAP_STZ(X3SEDITECHSAP_STZID,BUKRS,BUTXT,CITY,COUNTRY,EQFNR,FLTYP,GEWRK,HASLD,INBDT,INGRP,INNAM,IWERK,IWERKNAME,KOKRS,KOSTL,KTEXT,LIVELLO,MSGRP,NAME1,NAME2,OBJNR,PLTXT,POSNR,POST_CODE1,RBNR,RBNRX,REGION,STREET,STRNO,STTXT,STTXU,SUBMT,SWERK,TPLKZ,TPLMA,TPLNR,TXT_GEWRK,TXT_TPLKZ,TYPTX,WERGW,ZZITO_ORD41,ZZITO_UMWKZ,ZZSOCP,EXTRACTION_ID)
	SELECT X3SEDITECHSAP_STZIDSEQ.nextval,BUKRS,BUTXT,CITY1,COUNTRY,EQFNR,FLTYP,GEWRK,'0',INBDT,INGRP,INNAM,IWERK,NAME1_MAN,KOKRS,KOSTL,KTEXT,LIVELLO,MSGRP,NAME1,NAME2,OBJNR,PLTXT,POSNR_CSV,POST_CODE1,RBNR,RBNRX,REGION,STREET,STRNO,ZSTATO_SISTEMA,ZSTATO_UTENTE,SUBMT,SWERK,TPLKZ,TPLMA,TPLNR,TXT_GEWRK,TXT_TPLKZ,TYPTX,WERGW,ZZITO_ORD41_CSV,ZZITO_UMWKZ_CSV,ZSOCPR,EXTRACTION_ID
	FROM X3SEDITECHSAP_STZ_IF@ONPREMISE s WHERE SUBSTR(s.STRNO,1,6) IN (%s) AND EXTRACTION_ID = '%s'"""%(stazStrno, extractionId)
		utils.executeQuery("UPDATE", "Copia delle ST", insertSql)
		
		# Copia degli EQ
		insertSql = """INSERT INTO X3EQUIPMENTSAP_STZ(X3EQUIPMENTSAP_STZID,EXTRACTION_ID,SPART,STREET,STTXT,STTXU,SUBMT,SWERK,SWERK_DESC,TIDNR,TPLNR,TXT_GEWRK,TYPBZ,TYPTX,WERGW,ZSOCPR,ANSDT,ANSWT,BAUJJ,BAUMM,BRGEW,BUKRS,BUTXT,CITY,COUNTRY,DATAB,DATBI,EARTX,EQART,EQFNR,EQKTX,EQTYP,EQUNR,GEWEI,GEWRK,HEQUI,HERLD,HERST,INBDT,INGRP,INNAM,INVNR,IWERK,IWERK_DESC,KOKRS,KOSTL,KTEXT,MAPAR,NAME1,NAME2,POSNR,POST_COD,RBNR,RBNRX,REGION,SERGE)
	SELECT X3EQUIPMENTSAP_STZIDSEQ.nextval,EXTRACTION_ID,SPARTE,STREET,ZSTATO_SISTEMA,ZSTATO_UTENTE,SUBMT,SWERK,NAME1_SWERK,TIDNR,TPLNR,TXT_GEWRK,TYPBZ,TYPTX,WERGW,ZSOCPR_E,ANSDT,ANSWT,BAUJJ,BAUMM,BRGEW,BUKRS,BUTXT,CITY,COUNTRY,DATAB,DATBI,EARTX,EQART,EQFNR,EQKTX,EQTYP,EQUNR,GEWEI,GEWRK,HEQUI,HERLD,HERST,INBDT,INGRP,INNAM,INVNR,IWERK,NAME1_IWERK,KOKRS,KOSTL,KTEXT,MAPAR,NAME1,NAME2,POSNR,POST_CODE1,RBNR,RBNRX,REGION,SERGE
	FROM X3EQUIPMENTSAP_STZ_IF@ONPREMISE e WHERE SUBSTR(e.TPLNR,1,6) IN (%s) AND EXTRACTION_ID = '%s'"""%(stazStrno, extractionId)
		utils.executeQuery("UPDATE", "Copia degli EQ", insertSql)
		
		# Copia delle caratteristiche delle ST
		insertSql = """INSERT INTO X3CARATSAP_STZ(X3CARATSAP_STZID,EXTRACTION_ID,ATNAM,ATNAMTX,BOO_TYP,CHAR_TYP,DAT_TYP,DEC_TYP,KLASSE,NUM_TYP,OBJECT_ID,OBJNR,UDM,UDM_TYP)
	SELECT  X3CARATSAP_STZIDSEQ.nextval,a.EXTRACTION_ID,a.ATNAM,ATNAMTX,a.BOO_TYP,NVL(a.CHAR_TYP_DOM, a.CHAR_TYP),a.DAT_TYP,DEC_TYP,a.KLASSE,NUM_TYP,a.OBJECT_ID,a.OBJNR,a.UDM,UDM_TYP
	FROM X3CARATSAP_STZ_IF@ONPREMISE a, X3SEDITECHSAP_STZ_IF@ONPREMISE s 
	WHERE a.OBJNR = s.TPLNR 
	and SUBSTR(s.STRNO,1,6) IN (%s)
	and s.EXTRACTION_ID = '%s' 
	AND a.EXTRACTION_ID = '%s'"""%(stazStrno, extractionId, extractionId)
		utils.executeQuery("UPDATE", "Copia delle caratteristiche delle ST", insertSql)
		
		# Copia delle caratteristiche degli EQ
		insertSql = """INSERT INTO X3CARATSAP_STZ(X3CARATSAP_STZID,EXTRACTION_ID,ATNAM,ATNAMTX,BOO_TYP,CHAR_TYP,DAT_TYP,DEC_TYP,KLASSE,NUM_TYP,OBJECT_ID,OBJNR,UDM,UDM_TYP)
	SELECT X3CARATSAP_STZIDSEQ.nextval,a.EXTRACTION_ID,ATNAM,ATNAMTX,BOO_TYP,NVL(CHAR_TYP_DOM, CHAR_TYP),DAT_TYP,DEC_TYP,KLASSE,NUM_TYP,OBJECT_ID,OBJNR,UDM,UDM_TYP
	FROM X3CARATSAP_STZ_IF@ONPREMISE a, X3EQUIPMENTSAP_STZ_IF@ONPREMISE e
	WHERE a.OBJECT_ID = e.EQUNR
	AND SUBSTR(e.TPLNR,1,6) IN (%s)
	AND a.EXTRACTION_ID = '%s'
	AND e.EXTRACTION_ID = '%s'"""%(stazStrno, extractionId, extractionId)
		utils.executeQuery("UPDATE", "Copia delle caratteristiche degli EQ", insertSql)
finally:
	utils.close()

execTime = System.currentTimeMillis() - start
logger.info("Total execution time: %s seconds"%(execTime / 1000.0))