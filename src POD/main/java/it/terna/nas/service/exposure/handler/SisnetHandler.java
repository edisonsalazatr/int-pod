package it.terna.nas.service.exposure.handler;

import static it.terna.nas.service.material.utility.Constants.MSG_001;
import static it.terna.nas.service.material.utility.Constants.MSG_002;
import static it.terna.nas.service.material.utility.Constants.MSG_003;
import static it.terna.nas.service.material.utility.Constants.MSG_004;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.kernel.soap.clientgen.odl.geolocalizzazione.ModificaImpiantoResponse;
import it.terna.kernel.soap.clientgen.odl.geolocalizzazione.VoidResponse;
import it.terna.kernel.soap.clientgen.odl.modificaimpianto.FacilityAddress;
import it.terna.kernel.soap.clientgen.odl.modificaimpianto.FacilityRequest;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.AssetLocRelation;
import it.terna.nas.service.dal.entity.eam.ClassStructureHierarchy;
import it.terna.nas.service.dal.entity.eam.ConnectorSapInfo;
import it.terna.nas.service.dal.entity.eam.X4RenameSapIdGeo;
import it.terna.nas.service.dal.entity.eam.X4SisnetAddress;
import it.terna.nas.service.dal.service.AssetLocRelationQueryService;
import it.terna.nas.service.dal.service.AssetQueryService;
import it.terna.nas.service.dal.service.ClassificationService;
import it.terna.nas.service.dal.service.SapQueryService;
import it.terna.nas.service.dal.service.X4RenameSapIdGeoQueryService;
import it.terna.nas.service.dal.service.X4SisnetAddressQueryService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.client.OdLClient;
import it.terna.nas.service.exposure.ws.maximo.JwtTokenResponse;
import it.terna.nas.service.material.utility.Catalog.ClassificationType;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SisnetHandler extends AbstractHandler
{
	@Getter @Setter @AllArgsConstructor
	private class ElabType 
	{
		private String type;
		private List<Asset> listAsset;
	}

	private static final String LINEA_PREFIX 				= "2";
	private static final String PATTERN_TRONCO_REGEX		= "[0-9A-Z]{7}-T[0-9]{2}";
	private static final Pattern PATTERN_TRONCO				= Pattern.compile(PATTERN_TRONCO_REGEX);
	private static final String STAZIONE_PREFIX 			= "3";
	private static final String UFFICIO_PREFIX				= "UFF";
	private static final String FOTOVOLTAICO_PREFIX			= "FTV";
	private static final String NOTI_GEOCALL				= "Y";
	private static final Set<String> HIERARCHY_SECOND_L2	= Set.of("LINEA\\CAMPATA AEREA", "LINEA\\TRATTA CAVO");
	
	
	@Autowired
	private AssetLocRelationQueryService assetLocRelationQueryService;
	
	@Autowired
	private AssetQueryService assetQueryService;
	
	@Autowired
	private ClassificationService classificationService;
	
	@Autowired
	private OdLClient odlClient;
	
	@Autowired
	private SapQueryService sapQueryService;
	
	@Autowired
	private X4SisnetAddressQueryService x4SisnetAddressQueryService;
	
	@Autowired
	private X4RenameSapIdGeoQueryService x4RenameSapIdGeoQueryService;
	
	
	private void addAddressIntoRequest(List<FacilityRequest> requestForGeoCall, it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpianto request)
	{
		it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpiantoIndirizzo indirizzo = request.getIndirizzo();
		
		if (Assorted.notNull(indirizzo))
		{	
			for (FacilityRequest row: requestForGeoCall)
			{
				row.setIndirizzo(new FacilityAddress());
				row.getIndirizzo().setToponimo(indirizzo.getToponimo());
				row.getIndirizzo().setVia(indirizzo.getVia());
				row.getIndirizzo().setCivico(indirizzo.getCivico());
				row.getIndirizzo().setLocalita(indirizzo.getLocalita());
				row.getIndirizzo().setProvincia(indirizzo.getProvincia());
				row.getIndirizzo().setCap(indirizzo.getCap());
				row.getIndirizzo().setComune(indirizzo.getComune());
				row.getIndirizzo().setIstat(indirizzo.getIstat());
				row.getIndirizzo().setLatitudine1(Assorted.notNull(indirizzo.getLatitudine1()) ? BigDecimal.valueOf(indirizzo.getLatitudine1()) : null);
				row.getIndirizzo().setLongitudine1(Assorted.notNull(indirizzo.getLongitudine1()) ? BigDecimal.valueOf(indirizzo.getLongitudine1()) : null);
				row.getIndirizzo().setLatitudine2(Assorted.notNull(indirizzo.getLatitudine2()) ? BigDecimal.valueOf(indirizzo.getLatitudine2()) : null);
				row.getIndirizzo().setLongitudine2(Assorted.notNull(indirizzo.getLongitudine2()) ? BigDecimal.valueOf(indirizzo.getLongitudine2()) : null);
			}
		}
	}
	
	
	private void callGeocall(List<FacilityRequest> requestForGeoCall) 
			throws EAMException
	{
		if (Assorted.isNotEmpty(requestForGeoCall))
		{
			JwtTokenResponse jwtTokenResponse = odlClient.getTokenOSB(EDataIntegrationEntity.GEOCALL_MODIFICA_IMPIANTO);
			for (FacilityRequest row: requestForGeoCall)
			{
				it.terna.kernel.soap.clientgen.odl.modificaimpianto.FacilityResponse esito = odlClient.sendModificaImpiantoOdL(row, jwtTokenResponse);
				if (StatusX4Esito.KO.name().equals(esito.getESITO()))
					throw new EAMException(esito.getCODICEERRORE(), esito.getDESCRIZIONEERRORE());	
			}
		}
	}


	private List<ConnectorSapInfo> checkConnector(String codiceOggetto) 
			throws DataLayerException, EAMException
	{
		List<ConnectorSapInfo> sapListByStrno = sapQueryService.getSapListByStrno(codiceOggetto); 
		
		if (Assorted.isEmpty(sapListByStrno))
		{
			String msg = String.format(MSG_001, codiceOggetto);
			throw new EAMException(EAMErrorMapping.EAM_0009.getEamCode(), msg);
		}
		
		return sapListByStrno;
	}
	
	
	private List<FacilityRequest> createRequestForGeoCall(ElabType assetToElaborate) 
			throws DataLayerException, EAMException
	{
		List<FacilityRequest> result 						    = new ArrayList<>(); 
		Map<String, ClassStructureHierarchy> mapClassStructure 	= getClassStructure(assetToElaborate);
		boolean isLinee 										= LINEA_PREFIX.equals(assetToElaborate.getType());
		
		for (Asset asset: assetToElaborate.getListAsset())
		{
			log.trace("start elab for asset {}", asset.getAssetNum());
			ClassStructureHierarchy classStructureHierarchy = mapClassStructure.get(asset.getClassStructureId());
			
			if (Assorted.isNull(classStructureHierarchy))
			{
				log.error("classStructure {} not exists", asset.getClassStructureId());
				continue;
			}
			
			FacilityRequest datiImpianto = new FacilityRequest();
			datiImpianto.setStatoOggetto(asset.getStatus());
			datiImpianto.setTipoOggetto(asset.getClassStructureId());
			datiImpianto.setFiliale(asset.getX3Ingrp());
			datiImpianto.setCentroDiLavoro(asset.getX3Gewrk());
			datiImpianto.setAreaLavorazione(asset.getX3Ingrp());
			datiImpianto.setDescrizione(asset.getDescription());
			datiImpianto.setCodiceOggetto(asset.getAssetNum());
			datiImpianto.setCodicePadre(asset.getParent());
			
			if (isLinee) 
			{
				
				if (Assorted.isNull(asset.getParent()))
				{
					int level = classStructureHierarchy.getLevel();
					if (level == 2) //2° livello
					{
						List<AssetLocRelation> listRoot = assetLocRelationQueryService.getLineaByLevel2(asset.getAssetNum());
						if (Assorted.isNotEmpty(listRoot))
						{
							for (int i = 0; i < listRoot.size(); i++)
							{
								if (i == 0)
								{
									datiImpianto.setCodicePadre(listRoot.get(i).getSourceAssetNum());
									datiImpianto.setCodiceRadice(listRoot.get(i).getSourceAssetNum());
									datiImpianto.setSAPID(listRoot.get(i).getX3SapId());
								} else if (i == 1) {
									datiImpianto.setCodicePadre2(listRoot.get(i).getSourceAssetNum());
									datiImpianto.setCodiceRadice2(listRoot.get(i).getSourceAssetNum());
									datiImpianto.setSAPID2(listRoot.get(i).getX3SapId());
								} else
									break;
							}
						}
						
					} else if (level == 1) { //Linea
						datiImpianto.setSAPID(asset.getX3SapId());
					} else {
						String msg = String.format(MSG_004, asset.getAssetNum(), Integer.toString(level));
						throw new EAMException(EAMErrorMapping.EAM_0009.getEamCode(), msg);
					}
					
				} else {
					
					if (Assorted.notNull(asset.getX3RootAssetNum())) //3° livello
					{
						List<AssetLocRelation> listRoot = assetLocRelationQueryService.getLineaByLevel2AndRoot(asset.getParent(), asset.getX3RootAssetNum());
						datiImpianto.setCodiceRadice(asset.getX3RootAssetNum());
						if (Assorted.isNotEmpty(listRoot))
							datiImpianto.setSAPID(listRoot.get(0).getX3SapId());
					} else {
						
						List<AssetLocRelation> listRoot = assetLocRelationQueryService.getLineaByLevel2(asset.getAncestor());
						if (Assorted.isNotEmpty(listRoot))
						{
							for (int i = 0; i < listRoot.size(); i++)
							{
								if (i == 0)
								{
									datiImpianto.setCodiceRadice(listRoot.get(i).getSourceAssetNum());
									datiImpianto.setSAPID(listRoot.get(i).getX3SapId());
								} else if (i == 1) {
									datiImpianto.setCodicePadre2(null);
									datiImpianto.setCodiceRadice2(listRoot.get(i).getSourceAssetNum());
									datiImpianto.setSAPID2(listRoot.get(i).getX3SapId());
								} else
									break;
							}
						}
					}
				}
				
			} else { //Stazioni - Ufficio - Fotovoltaico
				datiImpianto.setSAPID(asset.getX3SapId());
				datiImpianto.setCodiceRadice(asset.getAncestor());
			}
			
			result.add(datiImpianto);
		}
		
		return result;
	}


	private ElabType findAssetToElaborate(String codiceOggetto) 
			throws DataLayerException, EAMException
	{
		ElabType output 	= null;
		boolean isTronco	= false;
		if (codiceOggetto.startsWith(STAZIONE_PREFIX))
			output = new ElabType(STAZIONE_PREFIX, assetQueryService.getAssetStazioniByStrno(codiceOggetto));
		else if (codiceOggetto.startsWith(LINEA_PREFIX)) {
			int codiceOggettoLen 	 = codiceOggetto.length();
			List<ConnectorSapInfo> connectorSapInfo = checkConnector(codiceOggetto);
			boolean hasCampataTratta = connectorSapInfo.stream().filter(x -> HIERARCHY_SECOND_L2.contains(x.getHierarchypath())).count() > 0;
			if (codiceOggettoLen == 7)
				output = new ElabType(LINEA_PREFIX, assetQueryService.getAssetLineeLevel1ByStrno(codiceOggetto));
			else if (codiceOggettoLen == 16 && hasCampataTratta)
				output = new ElabType(LINEA_PREFIX, assetQueryService.getAssetLineeLevel2ByStrno(codiceOggetto));
			else
				output = new ElabType(LINEA_PREFIX, assetQueryService.getAssetLineeLevel34ByStrno(codiceOggetto));
		} else if (PATTERN_TRONCO.matcher(codiceOggetto).matches()) {
			isTronco 	= true;
			output 		= new ElabType(PATTERN_TRONCO_REGEX, new ArrayList<>());
		} else if (codiceOggetto.startsWith(UFFICIO_PREFIX)) {
			output = new ElabType(UFFICIO_PREFIX, assetQueryService.getAssetUfficioVoltaicoByStrno(codiceOggetto));
		}else if (codiceOggetto.startsWith(FOTOVOLTAICO_PREFIX)) {
			output = new ElabType(FOTOVOLTAICO_PREFIX, assetQueryService.getAssetUfficioVoltaicoByStrno(codiceOggetto));
		} else
			throw new EAMException(EAMErrorMapping.EAM_0008.getEamCode(), EAMErrorMapping.EAM_0008.getErrorDescription());
	
	
		if (Assorted.isEmpty(output.getListAsset()) && (!isTronco))
		{
			String msg = String.format(MSG_002, codiceOggetto);
			throw new EAMException(EAMErrorMapping.EAM_0009.getEamCode(), msg);
		}
		
		return output;
	}


	private Map<String, ClassStructureHierarchy> getClassStructure(ElabType assetToElaborate) 
			throws DataLayerException
	{
		if (LINEA_PREFIX.equals(assetToElaborate.getType()))
			return classificationService.getMapClassStructureHierarchy(ClassificationType.LINEA);
		else if (STAZIONE_PREFIX.equals(assetToElaborate.getType()))
			return classificationService.getMapClassStructureHierarchy(ClassificationType.STAZIONE);
		else if (UFFICIO_PREFIX.equals(assetToElaborate.getType()))
			return classificationService.getMapClassStructureHierarchy(ClassificationType.UFFICIO);
		else if (FOTOVOLTAICO_PREFIX.equals(assetToElaborate.getType()))
			return classificationService.getMapClassStructureHierarchy(ClassificationType.FOTOVOLTAICO);
		
		return null;
	}

	
	@Async
	public void modificaImpianto(it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpianto request)
	{
		modificaImpiantoInternal(request);
	}
	
	
	private void modificaImpiantoInternal(it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpianto request) 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call modificaImpiantoInternal(codiceOggetto={}) |{}|", request.getCodiceOggetto(), startTime);
		
		try {
			String codiceOggetto = request.getCodiceOggetto();
			ElabType assetToElaborate = findAssetToElaborate(codiceOggetto);
			if (Assorted.isNotEmpty(assetToElaborate.getListAsset()))
			{
				List<FacilityRequest> requestForGeoCall = createRequestForGeoCall(assetToElaborate);
				addAddressIntoRequest(requestForGeoCall, request);
				callGeocall(requestForGeoCall);
			}
			
			x4SisnetAddressQueryService.updateNotiDetails(request.getCodiceOggetto(), null, null);
			
		} catch (EAMException q) {
			log.error("Error elaboration asset {}. Error: {} Descr: {}", request.getCodiceOggetto(), q.getErrorCode(), q.getMessage());
			
			try {
				x4SisnetAddressQueryService.updateNotiDetails(request.getCodiceOggetto(), NOTI_GEOCALL, q.getMessage());
			} catch(Exception k) {
				log.error(k.getMessage(), k);
			}
			
		} catch (Exception e) {
			log.error("Generic Error " + e.getMessage(), e);
			try {
				x4SisnetAddressQueryService.updateNotiDetails(request.getCodiceOggetto(), NOTI_GEOCALL, String.format(MSG_003, request.getCodiceOggetto(), e.getMessage()));
			} catch(Exception k) {
				log.error(k.getMessage(), k);
			}
		}
		
		long endTime = System.currentTimeMillis();
		log.info("end call modificaImpiantoInternal(codiceOggetto={}) |{}|", request.getCodiceOggetto(), endTime - startTime);
	}
	
	
	public ModificaImpiantoResponse saveRequestSisnet(it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpianto request) 
			throws EAMException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call saveRequestSisnet(codiceOggetto={}) |{}|", request.getCodiceOggetto(), startTime);
		ModificaImpiantoResponse response = new ModificaImpiantoResponse();
		response.setModificaImpiantoReturn(new VoidResponse());
		
		try {
			X4SisnetAddress requestoToSave = new X4SisnetAddress();
			requestoToSave.setTipoOggetto(request.getTipoOggetto());
			requestoToSave.setStatoOggetto(request.getStatoOggetto());
			requestoToSave.setCodiceOggetto(request.getCodiceOggetto());
			requestoToSave.setCodicePadre(request.getCodicePadre());
			requestoToSave.setDescrizione(request.getDescrizione());
			requestoToSave.setAreaLavorazione(request.getAreaLavorazione());
			requestoToSave.setCentroDiLavoro(request.getCentroDiLavoro());
			requestoToSave.setFiliale(request.getFiliale());
			requestoToSave.setCreationDate(new java.sql.Date(new java.util.Date().getTime()));
			it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpiantoIndirizzo indirizzo = request.getIndirizzo();
			
			if (Assorted.notNull(indirizzo))
			{
				requestoToSave.setToponimo(indirizzo.getToponimo());
				requestoToSave.setVia(indirizzo.getVia());
				requestoToSave.setCivico(indirizzo.getCivico());
				requestoToSave.setLocalita(indirizzo.getLocalita());
				requestoToSave.setProvincia(indirizzo.getProvincia());
				requestoToSave.setCap(indirizzo.getCap());
				requestoToSave.setComune(indirizzo.getComune());
				requestoToSave.setIstat(indirizzo.getIstat());
				requestoToSave.setLatitudine1(indirizzo.getLatitudine1());
				requestoToSave.setLongitudine1(indirizzo.getLongitudine1());
				requestoToSave.setLatitudine2(indirizzo.getLatitudine2());
				requestoToSave.setLongitudine2(indirizzo.getLongitudine2());
			}
			
			x4SisnetAddressQueryService.saveOrUpdate(requestoToSave);
				
			
		} catch(Exception p) {
			log.error(p.getMessage(), p);
			throw new EAMException(EAMErrorMapping.EAM_0000.getEamCode(), EAMErrorMapping.EAM_0000.getErrorDescription() + " " + p.getMessage(), p);
		}
		
		long endTime = System.currentTimeMillis();
		log.info("end call saveRequestSisnet (codiceOggetto={}) |{}|", request.getCodiceOggetto(), endTime - startTime);
		
		return response;
	}


	public void modificaImpiantoRelaunch() 
	{
		try {
			List<X4SisnetAddress> objectToConvertList = x4SisnetAddressQueryService.getObjectsToNotify();
			if(Assorted.isNotEmpty(objectToConvertList)) 
			{
				for(X4SisnetAddress item : objectToConvertList) 
				{
					it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpianto objectToResend = convertToDatiImpianto(item);
					modificaImpiantoInternal(objectToResend);
				}
			}
		} 
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		
	}
	
	private it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpianto convertToDatiImpianto(X4SisnetAddress object)
	{
		it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpianto datiImpianto = new it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpianto();
		datiImpianto.setTipoOggetto(object.getTipoOggetto());
		datiImpianto.setStatoOggetto(object.getStatoOggetto());
		datiImpianto.setCodiceOggetto(object.getCodiceOggetto());
		datiImpianto.setCodicePadre(object.getCodicePadre());
		datiImpianto.setDescrizione(object.getDescrizione());
		datiImpianto.setAreaLavorazione(object.getAreaLavorazione());
		datiImpianto.setCentroDiLavoro(object.getCentroDiLavoro());
		datiImpianto.setFiliale(object.getFiliale());
		
		it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpiantoIndirizzo datiImpiantoIndirizzo = new it.terna.kernel.soap.clientgen.odl.geolocalizzazione.DatiImpiantoIndirizzo();
		datiImpiantoIndirizzo.setToponimo(object.getToponimo());
		datiImpiantoIndirizzo.setVia(object.getVia());
		datiImpiantoIndirizzo.setCivico(object.getCivico());
		datiImpiantoIndirizzo.setLocalita(object.getLocalita());
		datiImpiantoIndirizzo.setProvincia(object.getProvincia());
		datiImpiantoIndirizzo.setCap(object.getCap());
		datiImpiantoIndirizzo.setComune(object.getComune());
		datiImpiantoIndirizzo.setIstat(object.getIstat());
		datiImpiantoIndirizzo.setLatitudine1(object.getLatitudine1());
		datiImpiantoIndirizzo.setLongitudine1(object.getLongitudine1());
		datiImpiantoIndirizzo.setLatitudine2(object.getLatitudine2());
		datiImpiantoIndirizzo.setLongitudine2(object.getLongitudine2());
	    
		datiImpianto.setIndirizzo(datiImpiantoIndirizzo);

		return datiImpianto;
	}

	public void checkRenameSapIdAndRelaunch() 
	{
		try 
		{
			List<X4RenameSapIdGeo> objectsToNotify = x4RenameSapIdGeoQueryService.getObjectsToNotify();
			if(Assorted.isNotEmpty(objectsToNotify)) 
			{
				for(X4RenameSapIdGeo item : objectsToNotify) 
				{
					try 
					{
						List<X4SisnetAddress> x4SisnetList = x4SisnetAddressQueryService.getObjectsBySapId(item.getSapIdOrig());
						if(Assorted.notNull(x4SisnetList)) 
						{
							for(X4SisnetAddress object : x4SisnetList) 
							{
								x4SisnetAddressQueryService.updateObjectCode(object.getX4SisnetAddressId(), item.getSapIdDest());
								object.setCodiceOggetto(item.getSapIdDest());
								modificaImpiantoInternal(convertToDatiImpianto(object));
								
							}
							x4RenameSapIdGeoQueryService.updateNotiDetails(item.getX4RenameSapIdGeoId(), null);
						}
						else
							x4RenameSapIdGeoQueryService.updateNotiDetails(item.getX4RenameSapIdGeoId(), NOTI_GEOCALL);
					}
					catch(Exception e) {
						log.error(e.getMessage(), e);
						x4RenameSapIdGeoQueryService.updateNotiDetails(item.getX4RenameSapIdGeoId(), NOTI_GEOCALL);
					}
				}
			}
		} 
		catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
