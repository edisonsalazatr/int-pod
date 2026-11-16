# OSIN.X3SRVPRIMILVL
# Processa i messaggi del servizio del connettore MAXIMO-SAP
# che allinea le sedi tecniche di primo livello
# v1.0 - 19/07/2023
# v1.1 - 27/07/2023
# v1.2 - 03/08/2023
# v1.3 - 12/09/2023
# v1.4 - 14/09/2023 - Gestione stato NAND->DELE + setScenario
# v1.5 - 22/05/2024 - Aggiunto controllo STRNO
from com.terna.common.log import Logger
from com.terna.common.maximo import MXUtils
from com.terna.common.string import StringUtils
from com.terna.integration.service import IntegrationService
from com.terna.integration.sapconnector import SapConnectorScenario
from psdi.mbo import SqlFormat, MboConstants
from psdi.server import MXServer
from psdi.iface.mos import ConversionUtil
from java.util.regex import Pattern

log = Logger.getLogger("X3SRVPRIMILVL")

statusMapping = {"PROG": "INAT", "CANT": "INAT", "DATT": "INAT", "ESER": "DATT", "NAND": "DELE"}

def beforeMboData(ocx):
	data = ocx.getData()
	# Traccia il messaggio
	log.info(data.getDataAsString())
	data.setAsCurrent(data.getPrimaryObject())
	
	# Legge i dati dal messaggio
	bukrs = data.getCurrentDataAsString("BUKRS")
	city = data.getCurrentDataAsString("CITY")
	country = data.getCurrentDataAsString("COUNTRY")
	eqfnr = data.getCurrentDataAsString("EQFNR")
	gewrk = data.getCurrentDataAsString("GEWRK")
	inbdt = data.getCurrentDataAsString("INBDT") # Si aspetta una data in formato ISO
	if inbdt != None:
		inbdt = ConversionUtil.stringToDate(inbdt)
	ingrp = data.getCurrentDataAsString("INGRP")
	kostl = data.getCurrentDataAsString("KOSTL")
	kokrs = data.getCurrentDataAsString("KOKRS")
	msgrp = data.getCurrentDataAsString("MSGRP")
	name1 = data.getCurrentDataAsString("NAME1")
	name2 = data.getCurrentDataAsString("NAME2")
	pltxt = data.getCurrentDataAsString("PLTXT")
	postcode1 = data.getCurrentDataAsString("POSTCODE1")
	region = data.getCurrentDataAsString("REGION")
	street = data.getCurrentDataAsString("STREET")
	strno = data.getCurrentDataAsString("STRNO")
	sttxu = data.getCurrentDataAsString("STTXU")
	swerk = data.getCurrentDataAsString("SWERK")
	tplnr = data.getCurrentDataAsString("TPLNR")
	zzsocp = data.getCurrentDataAsString("ZZSOCP")
	fltyp = data.getCurrentDataAsString("FLTYP")
	if fltyp == "L":
		assetType = "LINEA"
		regex = "^2[0-5][0-9|A-Z|-]{3}[A-Z][0-9]$"
	elif fltyp == "Z":
		assetType = "STAZIONE"
		regex = "^3[0-6][0-9]{3}[A-Z]$"
	else:
		ocx.error("x3terna", "ErrorMsg", ["FLTYP must be either L or Z"])
	# Verifica l'strno
	pattern = Pattern.compile(regex)
	matcher = pattern.matcher(strno)
	if not matcher.find():
		ocx.error("x3terna", "ErrorMsg", ["%s is not a valid strno"%strno])
	
	# Carica la X3SAPASSET
	sapAssetSet = MXUtils.getMboSet("X3SAPASSET")
	sqf = SqlFormat("sapuid = :1")
	sqf.setObject(1, "X3SAPASSET", "SAPUID", tplnr)
	sapAssetSet.setWhere(sqf.format())
	if sapAssetSet.isEmpty():
		# Nuovo primo livello
		log.info("Create first level: " + strno)
		sapAsset = sapAssetSet.add()
		sapAsset.setValue("rootstrno", strno)
		sapAsset.setValue("sapclass", assetType)
		sapAsset.setValue("saplevel", 1)
		sapAsset.setValue("sapuid", tplnr)
		sapAsset.setValue("strno", strno)
		sapAsset.setValue("sapobject", "S")
		sapAsset.setValue("rootassettype", fltyp)
	else:
		# SAP Asset esistente
		log.info("Update first level: " + strno)
		sapAsset = sapAssetSet.getMbo(0)
		
	# Carica l'asset di primo livello
	assetSet = sapAsset.getMboSet("$FIRSTLVLASSET", "ASSET", "(assetnum, siteid) in (select objectnum, siteid from X3SAPTOMAXOBJECT where sapassetid = :x3sapassetid and hierarchypath in ('" + assetType + "'))")
	if assetSet.isEmpty():
		# Nuovo Asset
		asset = assetSet.add()
		# Valori di default TODO Chiedere se ci sono altre informazioni di default che vanno settate
		asset.setValue("CLASSSTRUCTURE.hierarchypath", assetType)
		# Valori dal messaggio SAP
		asset.setValue("x3sapid", strno)
		asset.setValue("status", "INAT", MboConstants.NOACCESSCHECK)
		# Crea l'indirizzo e setta EQ1 se è una stazione
		if assetType == "STAZIONE":
			asset.setValue("eq1", strno)
			serviceAddressSet = MXUtils.getMboSet("SERVICEADDRESS")
			serviceAddressSet.setMXTransaction(assetSet.getMXTransaction())
			serviceAddress = serviceAddressSet.add()
			serviceAddress.setValue("orgid", asset.getString("orgid"))
			asset.setValue("saddresscode", serviceAddress.getString("addresscode"), MboConstants.NOVALIDATION)
		# Crea il link tra la Sede Tecnica e l'Asset
		sapToMaxObjectSet = sapAsset.getMboSet("$SAPTOMAX", "X3SAPTOMAXOBJECT", "sapassetid = :x3sapassetid and hierarchypath = '" + assetType + "'")
		sapToMaxObject = sapToMaxObjectSet.add()
		sapToMaxObject.setValue("hierarchypath", assetType)
		sapToMaxObject.setValue("maxobject", "ASSET")
		sapToMaxObject.setValue("objectnum", asset.getString("assetnum"))
		sapToMaxObject.setValue("rootassetnum", asset.getString("assetnum"))
		sapToMaxObject.setValue("sapassetid", sapAsset.getLong("x3sapassetid"))
	else:
		# Asset esistente
		asset = assetSet.getMbo(0)
		serviceAddress = asset.getMboSet("SERVICEADDRESS").getMbo(0)
	# Verifica se lo stato attuale è INAT
	if asset.getString("status") == "INAT":
		# L'asset può essere aggiornato nella sua totalità
		log.info(StringUtils.format("SAP is owner: updating data for {0} {1} ({2})", assetType, strno, asset.getString("assetnum")))
		asset.setValue("description", pltxt)
		asset.setValue("installdate", inbdt)
		asset.setValue("x3eqfnr", eqfnr)
		asset.setValue("x3bukrs", bukrs)
		asset.setValue("x3ingrp", ingrp)
		asset.setValue("x3swerk", swerk)
		asset.setValue("x3gewrk", gewrk)
		
		# Aggiorna i dati dell'indirizzo se è una stazione
		if assetType == "STAZIONE":
			serviceAddress.setValue("description", name1, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("addressline2", name2, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("city", city, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("country", country, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("regiondistrict", region, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("streetaddress", street, MboConstants.NOACCESSCHECK)
			serviceAddress.setValue("postalcode", postcode1, MboConstants.NOACCESSCHECK)
		
		if sttxu not in statusMapping:
			ocx.error("x3terna", "ErrorMsg", [StringUtils.format("STTXU {0} is not valid a valid status", sttxu)])
		if statusMapping[sttxu] != "INAT":
			log.info("Changing asset status to: " + statusMapping[sttxu])
			asset.changeStatus(statusMapping[sttxu], MXServer.getMXServer().getDate(), "Modificato da SAP")
	else:
		log.info(StringUtils.format("Maximo is owner: updating only accounting data for {0} {1} ({2})", assetType, strno, asset.getString("assetnum")))
	# Azioni sempre ammesse sull'asset (add/update)
	asset.setValue("x3zzsocp", zzsocp)
	asset.setValue("x3msgrp", msgrp)
	asset.setValue("x3kostl", kostl)
	asset.setValue("x3kokrs", kokrs)
	
	# Salva
	IntegrationService.getService().saveNoSync(sapAssetSet)