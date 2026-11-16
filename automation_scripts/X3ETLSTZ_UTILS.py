# X3ETLSTZ_UTILS
# Classe di utilities per il caricamento delle Stazioni da SAP
# v1.0 25/03/2024
# v1.1 28/04/2024
#	Aggiunta gestione righe di log in X3ETLSTZLOG
# v1.2 28/02/2025
#	Aggiunta gestione autokey e sequenza per creazione asset

class X3ETLSTZUTILS:
	from psdi.server import MXServer
	from java.lang import System
	from java.lang import Exception
	from com.terna.common.log import Logger
	from com.terna.common.date import DateUtils
	from com.terna.common.string import StringUtils

	"""
	Inizializza l'oggetto:
	recupera la connection al DB,
	carica l'mbo set della pianificazione di import se è stato passato l'etlnum
	"""
	def __init__(self, etlnum, phase):
		self.mxServer = self.MXServer.getMXServer()
		self.conKey = self.mxServer.getSystemUserInfo().getConnectionKey()
		self.con = self.mxServer.getDBManager().getConnection(self.conKey)
		self.logger = self.Logger.getLogger("ETLSTZ.X3ETLSTZ_UTILS")
		self.statement = None
		self.etlnum = etlnum
		self.phase = phase
		self.etlLogSet = self.mxServer.getMboSet("X3ETLSTZLOG", self.mxServer.getSystemUserInfo()) if etlnum != None else None
		self.lastLogSave = -1
	
	"""
	Esegue le query, logga tempi di esecuzione e numero di record aggiunti/modificati/eliminati
	Parametri:
		type: tipo di query da eseguire (SELECT o UPDATE)
		title: titolo della query
		sql: istruzione sql da eseguire
	"""
	def executeQuery(self, type, title, sql):
		startSql = self.System.currentTimeMillis()
		self.logger.info("Executing query: %s"%title)
		if self.statement != None:
			self.statement.close()
		self.statement = self.con.createStatement()
		etlLog = None
		if self.etlnum and self.phase:
			# Crea la riga di log per l'esecuzione della query
			etlLog = self.etlLogSet.add()
			etlLog.setValue("etlnum", self.etlnum)
			etlLog.setValue("phase", self.phase)
			etlLog.setValue("subphase", title)
			etlLog.setValue("startdate", self.mxServer.getDate())
		try:
			if type == "SELECT":
				rs = self.statement.executeQuery(sql)
			else:
				# Setta il numero massimo di secondi che può impiegare l'esecuzione dell'executeUpdate
				self.statement.setQueryTimeout(90)
				
				numRecords = self.statement.executeUpdate(sql)
				if etlLog:
					# Setta il numero di record creati/modificati/eliminati nella riga di log
					etlLog.setValue("numrecords", numRecords)
		except self.Exception, e:
			if etlLog:
				# Setta lo stato di fallimento nella riga di log
				etlLog.setValue("status", "FAILED")
				etlLog.setValue("error", self.StringUtils.getStackTraceAsString(e))
			raise e
		
		endSql = self.System.currentTimeMillis()
		execTimeSql = endSql - startSql
		if etlLog:
			# Completa le informazioni della riga di log
			etlLog.setValue("status", "COMPLETED")
			etlLog.setValue("finishdate", self.mxServer.getDate())
			etlLog.setValue("elapsed", execTimeSql)
			etlLog.setValue("elapsedaln", self.DateUtils.format(execTimeSql))
			if endSql - self.lastLogSave >= 2000:
				# Salva le righe di log se sono passati più di 2 secondi dall'ultimo salvataggio
				self.lastLogSave = endSql
				self.etlLogSet.save()
		if type == "SELECT":
			self.logger.debug("Execution time for query '%s': %s seconds"%(title, execTimeSql / 1000.0))
			return rs
		self.logger.debug("Execution time for query '%s': %s seconds (%s)"%(title, execTimeSql / 1000.0, numRecords))
		
	"""
	Aggiorna l'autokey degli asset per lasciare lo spazio alla creazione
	e resetta la sequence x3etlassetnumseq in corrispondenza del primo valore utile
	"""
	def updateAutoKeyAndSequence(self, rootStrno):
		try:
			if self.statement != None:
				self.statement.close()
				
			startSql = self.System.currentTimeMillis()
			
			etlLog = None
			if self.etlnum and self.phase:
				# Crea la riga di log per l'esecuzione della query
				etlLog = self.etlLogSet.add()
				etlLog.setValue("etlnum", self.etlnum)
				etlLog.setValue("phase", self.phase)
				etlLog.setValue("subphase", "Gestione autokey e sequence")
				etlLog.setValue("startdate", self.mxServer.getDate())
			statement = self.con.createStatement()
			# Recupera la previsione della numerosità di asset (ST + EQ) da creare (inclusi variatori e sistemi di raffreddamento)
			selectSql = """SELECT sum(numobj) numobj FROM (
		SELECT count(*) numobj FROM X3ETLSTSAPS2 s
		WHERE (s.rootstrno IN (%s) AND s.sttxu != 'UNIDENTIFIED' AND s.sapclass != 'UNIDENTIFIED' AND s.classstructureid != 'UNIDENTIFIED') AND (s.saplevel < 4
		OR (s.saplevel = 4 AND s.sapclass = 'IMPIANTO DI TERRA'))
		UNION ALL
		SELECT count(*) numobj FROM X3ETLEQSAPS2 e
		WHERE e.rootstrno IN (%s) AND e.sttxu != 'UNIDENTIFIED' AND e.SAPCLASS != 'UNIDENTIFIED' AND e.CLASSSTRUCTUREID != 'UNIDENTIFIED'
		UNION ALL
		SELECT count(*) numobj FROM X3ETLEQSAPS2 e
		WHERE e.rootstrno IN (%s) AND e.sttxu != 'UNIDENTIFIED' AND e.SAPCLASS != 'UNIDENTIFIED' AND e.CLASSSTRUCTUREID != 'UNIDENTIFIED'
		AND e.SAPCLASS IN ('BOOSTER','REATTORE','TRASFORMATORE A 3 AVVOLGIMENTI','TRASFORMATORE AT/MT','TRASFORMATORE DI GRUPPO','TRASFORMATORE DI INTERCONNESSIONE','TRASFORMATORE MT/MT')
		AND EXISTS (SELECT 1 FROM X3ETLCARATSAPS2 c WHERE c.OBJNR = e.EQUNR AND ((c.NAME IN ('PMT_TR22','PMT_T322','PMT_TD22','PMT_TG22','PMT_TI22','PMT_TM22','PMT_T328','PMT_TD28','PMT_TG28','PMT_TI28','PMT_TM28') AND c.CHAR_TYP = 'SI') OR (c.NAME = 'PMT_TB15' AND c.CHAR_TYP IS NOT NULL)))
		UNION ALL
		SELECT count(*) numobj FROM X3ETLEQSAPS2 e
		WHERE e.rootstrno IN (%s) AND e.sttxu != 'UNIDENTIFIED' AND e.SAPCLASS != 'UNIDENTIFIED' AND e.CLASSSTRUCTUREID != 'UNIDENTIFIED'
		AND e.SAPCLASS IN ('BOOSTER','REATTORE','TRASFORMATORE A 3 AVVOLGIMENTI','TRASFORMATORE AT/MT','TRASFORMATORE DI GRUPPO','TRASFORMATORE DI INTERCONNESSIONE','TRASFORMATORE MT/MT')
		AND EXISTS (SELECT 1 FROM X3ETLCARATSAPS2 c WHERE c.OBJNR = e.EQUNR AND c.NAME IN ('PMT_TB22','PMT_TR35','PMT_T335','PMT_TD35','PMT_TG35','PMT_TI35','PMT_TM35') AND c.CHAR_TYP IS NOT NULL)
	)"""%(rootStrno, rootStrno, rootStrno, rootStrno)
			rs = statement.executeQuery(selectSql)
			rs.next()
			totAssetsToGen = rs.getString("numobj")
			# Aggiorna l'autokey degli asset
			statement.executeUpdate("UPDATE AUTOKEY SET SEED = SEED + %s WHERE AUTOKEYNAME = 'ASSETNUM'"%totAssetsToGen)
			# Aggiorna il seed della sequence x3etlassetnumseq
			rs = statement.executeQuery("SELECT SEED - %s nextseed FROM AUTOKEY a WHERE AUTOKEYNAME = 'ASSETNUM'"%totAssetsToGen)
			rs.next()
			nextSeed = rs.getString("nextseed")
			statement.executeUpdate("ALTER SEQUENCE X3ETLASSETNUMSEQ RESTART START WITH %s"%nextSeed)
			statement.executeQuery("SELECT X3ETLASSETNUMSEQ.nextval FROM dual")
			
			self.con.commit()
			
			endSql = self.System.currentTimeMillis()
			execTimeSql = endSql - startSql
			if etlLog:
				# Completa le informazioni della riga di log
				etlLog.setValue("status", "COMPLETED")
				etlLog.setValue("finishdate", self.mxServer.getDate())
				etlLog.setValue("elapsed", execTimeSql)
				etlLog.setValue("elapsedaln", self.DateUtils.format(execTimeSql))
				self.etlLogSet.save()
		except self.Exception, e:
			try:
				self.con.rollback()
			except:
				pass
			if etlLog:
				# Setta lo stato di fallimento nella riga di log
				etlLog.setValue("status", "FAILED")
				etlLog.setValue("error", self.StringUtils.getStackTraceAsString(e))
			raise e
		finally:
			if statement != None:
				try:
					statement.close()
				except:
					pass
		
		
	"""
	Rilascia la connessione e chiude lo statement
	"""
	def close(self):
		if self.statement != None:
			try:
				self.statement.close()
			except:
				pass
		self.con = None
		self.mxServer.getDBManager().freeConnection(self.conKey)
		if self.etlLogSet != None:
			self.etlLogSet.save()
			self.etlLogSet.close()