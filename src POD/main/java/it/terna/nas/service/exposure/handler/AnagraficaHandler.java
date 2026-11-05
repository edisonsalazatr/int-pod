package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.EAM;
import static it.terna.nas.service.material.utility.Constants.FOTOVOLTAICO;
import static it.terna.nas.service.material.utility.Constants.GEOCALL;
import static it.terna.nas.service.material.utility.Constants.LINEA;
import static it.terna.nas.service.material.utility.Constants.MSG_004;
import static it.terna.nas.service.material.utility.Constants.MSG_005;
import static it.terna.nas.service.material.utility.Constants.STAZIONE;
import static it.terna.nas.service.material.utility.Constants.UFFICIO;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.kernel.soap.clientgen.odl.modificaimpianto.FacilityAddress;
import it.terna.kernel.soap.clientgen.odl.modificaimpianto.FacilityRequest;
import it.terna.kernel.soap.clientgen.odl.modificaimpianto.FacilityResponse;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.AssetForModificaAnagrafica;
import it.terna.nas.service.dal.entity.eam.AssetLocRelation;
import it.terna.nas.service.dal.entity.eam.ClassStructureHierarchy;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.dal.entity.eam.ConnectorSapInfo;
import it.terna.nas.service.dal.entity.eam.X4EsitoInt;
import it.terna.nas.service.dal.entity.eam.X4SisnetAddress;
import it.terna.nas.service.dal.service.AssetLocRelationQueryService;
import it.terna.nas.service.dal.service.AssetQueryService;
import it.terna.nas.service.dal.service.ClassificationService;
import it.terna.nas.service.dal.service.SapQueryService;
import it.terna.nas.service.dal.service.X4EsitoIntApiService;
import it.terna.nas.service.dal.service.X4EsitoIntQueryService;
import it.terna.nas.service.dal.service.X4SisnetAddressQueryService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.client.OdLClient;
import it.terna.nas.service.exposure.ws.maximo.GenericEsitoResponse;
import it.terna.nas.service.exposure.ws.maximo.JwtTokenResponse;
import it.terna.nas.service.material.dto.AssetDTO;
import it.terna.nas.service.material.dto.X4EsitoIntRequest;
import it.terna.nas.service.material.utility.Catalog.ClassificationType;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.GeoCallInterface;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AnagraficaHandler extends AbstractHandler
{
	private static final Set<String> HIERARCHY_SECOND_L2	= Set.of("CAMPATA AEREA", "TRATTA CAVO");
	
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
	private X4EsitoIntApiService x4EsitoIntApiService;
	
	@Autowired
	private X4EsitoIntQueryService x4EsitoIntQueryService;
	
	@Autowired
	private X4SisnetAddressQueryService x4SisnetAddressQueryService;
	
	@Autowired
	private BrokerEntityConfiguration brokerEntities;
	
	@Autowired
	private KafkaProducerHandler kafkaHandler;

	
	private void convertToDatiImpianto(FacilityRequest requestForGeoCall, String sapId) 
			throws DataLayerException
	{
		List<X4SisnetAddress> objectsBySapId = x4SisnetAddressQueryService.getObjectsBySapId(sapId);
		
		if (Assorted.isNotEmpty(objectsBySapId))
		{
			X4SisnetAddress indirizzo = objectsBySapId.get(0);
			requestForGeoCall.setIndirizzo(new FacilityAddress());
			FacilityAddress datiImpiantoIndirizzo = requestForGeoCall.getIndirizzo();
			datiImpiantoIndirizzo.setToponimo(indirizzo.getToponimo());
			datiImpiantoIndirizzo.setVia(indirizzo.getVia());
			datiImpiantoIndirizzo.setCivico(indirizzo.getCivico());
			datiImpiantoIndirizzo.setLocalita(indirizzo.getLocalita());
			datiImpiantoIndirizzo.setProvincia(indirizzo.getProvincia());
			datiImpiantoIndirizzo.setCap(indirizzo.getCap());
			datiImpiantoIndirizzo.setComune(indirizzo.getComune());
			datiImpiantoIndirizzo.setIstat(indirizzo.getIstat());
			datiImpiantoIndirizzo.setLatitudine1(Assorted.notNull(indirizzo.getLatitudine1()) ? BigDecimal.valueOf(indirizzo.getLatitudine1()) : null);
			datiImpiantoIndirizzo.setLongitudine1(Assorted.notNull(indirizzo.getLongitudine1()) ? BigDecimal.valueOf(indirizzo.getLongitudine1()) : null);
			datiImpiantoIndirizzo.setLatitudine2(Assorted.notNull(indirizzo.getLatitudine2()) ? BigDecimal.valueOf(indirizzo.getLatitudine2()) : null);
			datiImpiantoIndirizzo.setLongitudine2(Assorted.notNull(indirizzo.getLongitudine2()) ? BigDecimal.valueOf(indirizzo.getLongitudine2()) : null);
		}
	}

	private FacilityRequest createRequestForGeoCall(AssetDTO asset, ClassStructureTipology classStructureTipology) 
			throws DataLayerException, EAMException
	{
		Map<String, ClassStructureHierarchy> mapClassStructure 	= getClassStructure(classStructureTipology);
		boolean isLinee 										= LINEA.equals(classStructureTipology.getType());
		boolean isStazioni										= STAZIONE.equals(classStructureTipology.getType());
		
		log.trace("start elab for asset {}", asset.getAssetnum());
		ClassStructureHierarchy classStructureHierarchy = mapClassStructure.get(asset.getClassstructureid());
		
		if (Assorted.isNull(classStructureHierarchy))
		{
			log.error("classStructure {} not exists", asset.getClassstructureid());
			throw new EAMException(EAMErrorMapping.EAM_0009.getEamCode(), EAMErrorMapping.EAM_0009.getErrorDescription());
		}
		
		
		FacilityRequest datiImpianto = new FacilityRequest();
		datiImpianto.setStatoOggetto(asset.getStatus());
		datiImpianto.setTipoOggetto(asset.getClassstructureid());
		datiImpianto.setCentroDiLavoro(asset.getX3gewrk());
		datiImpianto.setDescrizione(asset.getDescription());
		datiImpianto.setCodiceOggetto(asset.getAssetnum());
		datiImpianto.setCodicePadre(asset.getParent());
		
		if (isLinee) 
		{
			String filiale = Assorted.notNull(asset.getX3ingrp()) ? asset.getX3ingrp() : assetQueryService.findFilialeByAncestor(asset.getAncestor());
			datiImpianto.setFiliale(filiale);
			datiImpianto.setAreaLavorazione(filiale);
			
			if (isEmpty(asset.getParent()))
			{
				int level = classStructureHierarchy.getLevel();
				if (level == 2) //2° livello
				{
					List<AssetLocRelation> listRoot = assetLocRelationQueryService.getLineaByLevel2(asset.getAssetnum());
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
					datiImpianto.setSAPID(asset.getX3sapid());
				} else {
					String msg = String.format(MSG_004, asset.getAssetnum(), Integer.toString(level));
					throw new EAMException(EAMErrorMapping.EAM_0009.getEamCode(), msg);
				}
				
			} else {
				
				if (isNotEmpty(asset.getX3rootassetnum())) //3° livello
				{
					List<AssetLocRelation> listRoot = assetLocRelationQueryService.getLineaByLevel2AndRoot(asset.getParent(), asset.getX3rootassetnum());
					datiImpianto.setCodiceRadice(asset.getX3rootassetnum());
					if (Assorted.isNotEmpty(listRoot))
						datiImpianto.setSAPID(listRoot.get(0).getX3SapId());
				
				} else {
					
				    List<Asset> assetParent         = assetQueryService.getAssetByAssetNum(asset.getParent());
                    List<AssetLocRelation> listRoot = assetLocRelationQueryService.getLineaByLevel2(asset.getAncestor());				    
				    
                    if (isNotEmpty(listRoot))
                    {
                        if (isNotEmpty(assetParent))  //Se il padre ha X3RootAssetNum allora bisogna prendere la campata associata a questa linea
                        {
                            Asset parent = assetParent.get(0);

                            AssetLocRelation locRelLinea = listRoot.stream()
                                                                   .filter(x-> isNotEmpty(parent.getX3RootAssetNum()) && parent.getX3RootAssetNum().equals(x.getSourceAssetNum()))
                                                                   .findFirst()
                                                                   .orElse(null);
                            
                            if (notNull(locRelLinea))
                            {
                                datiImpianto.setCodiceRadice(locRelLinea.getSourceAssetNum());
                                datiImpianto.setSAPID(locRelLinea.getX3SapId());                                
                            }
                            
                        } else {
                            
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
			}
			
		} 
		else if(isStazioni) 
		{ //Stazioni 
			String filiale = Assorted.notNull(asset.getX3ingrp()) ? asset.getX3ingrp() : assetQueryService.findFilialeByAssetnum(asset.getAssetnum());
			datiImpianto.setFiliale(filiale);
			datiImpianto.setAreaLavorazione(filiale);
			String sapId = assetQueryService.findSapFotStazioniByAssetNum(asset.getAssetnum());
			datiImpianto.setSAPID(sapId);
			datiImpianto.setCodiceRadice(asset.getAncestor());
		}
		else 
		{ //Ufficio - Fotovoltaico
			String filiale = Assorted.notNull(asset.getX3ingrp()) ? asset.getX3ingrp() : assetQueryService.findFilialeByAssetnum(asset.getAssetnum());
			datiImpianto.setFiliale(filiale);
			datiImpianto.setAreaLavorazione(filiale);
			String sapId = asset.getX3sapid();
			datiImpianto.setSAPID(sapId);
			datiImpianto.setCodiceRadice(asset.getAncestor());
		}
		
		return datiImpianto;
	}
	
	
	private String findSapId(AssetDTO asset, ClassStructureTipology classStructureTipology) 
			throws DataLayerException
	{
		String sapId = null;
		Long startTime = System.currentTimeMillis();
		log.info("call findSapId(assetNum={}, type={}) |{}|", asset.getAssetnum(), classStructureTipology.getType(), startTime);
		
		if (UFFICIO.equals(classStructureTipology.getType()) 
			|| FOTOVOLTAICO.equals(classStructureTipology.getType()))
		{
			sapId = asset.getX3sapid();
		}
		else if (STAZIONE.equals(classStructureTipology.getType())) {
			sapId = assetQueryService.findSapFotStazioniByAssetNum(asset.getAssetnum());
		}
		else if (LINEA.equals(classStructureTipology.getType())) {
			
			Map<String, ClassStructureHierarchy> mapClassStructureHierarchy = classificationService.getMapClassStructureHierarchy(ClassificationType.LINEA);
			ClassStructureHierarchy classStructureHierarchy = mapClassStructureHierarchy.get(asset.getClassstructureid());
			
			if (Assorted.notNull(classStructureHierarchy))
			{
				List<ConnectorSapInfo> sapListByAssetNum = null;
				if (classStructureHierarchy.getLevel() == 1)
				{
					sapId = asset.getX3sapid();
				} else if (HIERARCHY_SECOND_L2.contains(classStructureTipology.getClassificationId())) {
					sapListByAssetNum = sapQueryService.getSapListL2ByAssetNum(asset.getAssetnum());
				} else {
					sapListByAssetNum = sapQueryService.getSapListByAssetNum(asset.getAssetnum());
				}
				
				if (Assorted.isNotEmpty(sapListByAssetNum))
				{
					ConnectorSapInfo connectorSapInfo = sapListByAssetNum.get(0);
					sapId = connectorSapInfo.getStrno();
				}
			}
		}
		Long endTime = System.currentTimeMillis();
		log.info("end findSapId(assetNum={}) ==> sapId={} |{}|", asset.getAssetnum(), sapId, endTime - startTime);
		return sapId;
	}
	
	
	private Map<String, ClassStructureHierarchy> getClassStructure(ClassStructureTipology classStructureTipology) 
			throws DataLayerException
	{
		if (LINEA.equals(classStructureTipology.getType()))
			return classificationService.getMapClassStructureHierarchy(ClassificationType.LINEA);
		else if (STAZIONE.equals(classStructureTipology.getType()))
			return classificationService.getMapClassStructureHierarchy(ClassificationType.STAZIONE);
		else if (UFFICIO.equals(classStructureTipology.getType()))
			return classificationService.getMapClassStructureHierarchy(ClassificationType.UFFICIO);
		else if (FOTOVOLTAICO.equals(classStructureTipology.getType()))
			return classificationService.getMapClassStructureHierarchy(ClassificationType.FOTOVOLTAICO);
		
		return null;
	}
	
	
	public void sendAssetInformation(String topic, String uuidResend, String payload) 
	{
		Instant startDate	= Instant.now();
		String uuid 		= Uuid.randomUuid().toString();
		Long startTime 		= System.currentTimeMillis();

		X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(GeoCallInterface.SEND_ASSET_INTERFACE.getName());
		x4EsitoIntRequest.setSource(EAM);
		x4EsitoIntRequest.setDest(GEOCALL);
		x4EsitoIntRequest.setKafkatopic(topic);
		x4EsitoIntRequest.setMessage(payload);
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());
		
		try {
	        if(Assorted.notNull(payload))
	        {
	        	if(Assorted.notNull(uuidResend)) 
	        	{
	        		x4EsitoIntRequest.setUniqueid(uuidResend);
	        		X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
	        		x4EsitoIntRequest.setParent(Assorted.notNull(parent) ? parent.getX4EsitoIntId() : null);
	        	}
	        	else
	        		x4EsitoIntRequest.setRetry(0L);
	        	
	        	AssetDTO asset = new Gson().fromJson(payload, AssetDTO.class);
	        	x4EsitoIntRequest.setAssetnum(asset.getAssetnum());
	        	if (Assorted.notNull(asset.getClassstructureid())) 
	        	{
		        	ClassStructureTipology classStructureTipology = classificationService.getClassStructureTipology(asset.getClassstructureid());
		        	if(Assorted.isNull(classStructureTipology)) 
		        	{
		        		x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0014.getEamCode());
		        		x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0014.getErrorDescription());
		        	} else {
			        	String sapId = findSapId(asset, classStructureTipology);
			        	if (Assorted.isEmpty(sapId)) 
			        		throw new EAMException(EAMErrorMapping.EAM_0012.getEamCode(), String.format(Constants.SAPID_NOT_FOUND_MESSAGE, asset.getAssetnum()));
			        	
			        	FacilityRequest requestForGeoCall = createRequestForGeoCall(asset, classStructureTipology);
			        	convertToDatiImpianto(requestForGeoCall, sapId);
			        	if (log.isInfoEnabled())
							log.info(GeoCallInterface.SEND_ASSET_INTERFACE.getName() + " Request: " + EamEtlUtils.writeJaxbObjectAsString(requestForGeoCall));
						JwtTokenResponse jwtTokenResponse = odlClient.getTokenOSB(EDataIntegrationEntity.GEOCALL_MODIFICA_IMPIANTO);
			        	FacilityResponse response = odlClient.sendModificaImpiantoOdL(requestForGeoCall, jwtTokenResponse);
						if (StatusX4Esito.KO.name().equals(response.getESITO()))
							throw new EAMException(response.getCODICEERRORE(), response.getDESCRIZIONEERRORE());
		        	}
	        	}
	        	else {
	        		String msg = String.format(MSG_005, asset.getAssetnum());
	        		log.info(msg);
	        		x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0014.getEamCode());
	        		x4EsitoIntRequest.setDesc_esito(msg);
	        	}
	        }
		        		
		} catch(EAMException q) {
			log.error(q.getMessage());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(q.getErrorCode());
			x4EsitoIntRequest.setDesc_esito(q.getMessage());
		} catch(java.lang.Exception q) {
			log.error("Generic Error: " + q.getMessage(), q);
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + q.getMessage());			
		} finally {
			x4EsitoIntRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
		}
		
		try {
			x4EsitoIntApiService.sendEsitoInt(x4EsitoIntRequest);
		} catch(java.lang.Exception e) {
			log.error("Rest Error when executing Maximo call to write X4ESITOINT result using this request: {}, occured exception {}", 
					  Assorted.notNull(x4EsitoIntRequest)
					  ? x4EsitoIntRequest.toString() 
					  : null,
					  e.getMessage());
		}
		Long endTime = System.currentTimeMillis();
		log.info("end call sendAsset(uuidResend={}, payload={}) |{}|", uuidResend, payload, endTime - startTime);
	}
	

	public GenericEsitoResponse modificaAnagraficaBulk(String assetNum) {
		Long startTime = System.currentTimeMillis();
		GenericEsitoResponse response = new GenericEsitoResponse();
		response.setEsito(StatusX4Esito.OK.name());
		response.setHttpCode(HttpStatus.OK.value());
		log.info("modificaAnagraficaBulk({}) started at [{}]", assetNum, startTime);
		try {
			List<AssetForModificaAnagrafica> assets = assetQueryService.findAssetForModificaAnagrafica(assetNum);
			if(Assorted.isNotEmpty(assets)) {
				List<AssetForModificaAnagrafica> listToProcess = null;
				boolean isDoppiaTerna = assets.stream().anyMatch(a -> Assorted.notNull(a.getCodiceSap2()));
				if(isDoppiaTerna) {
					List<String> rootList = assets.stream().filter(a -> Assorted.notNull(a.getCodiceRadice2()))
											.map(AssetForModificaAnagrafica::getCodiceRadice2).toList();
					listToProcess = assetQueryService.findDoppiaTernaForModificaAnagrafica(rootList, assetNum);
				}
				else {
					listToProcess = assets;
				}
				List<AssetDTO> assetsToSend = convertToAssetDTO(listToProcess);
				BrokerEntityInfo brokerEntityInfoPublicationEndpoint = brokerEntities.getEndPoints().get(Constants.ASSET_MODIFY_ENTITY);
				if(Assorted.isNotEmpty(assetsToSend)) 
					kafkaHandler.shoot(EOperation.CREATE, brokerEntityInfoPublicationEndpoint, assetsToSend);
				else {
					response.setEsito(StatusX4Esito.KO.name());
					response.setHttpCode(HttpStatus.NOT_FOUND.value());
					response.setErrorDescription("didn't find any assets with corresponding assetnum: " + assetNum);
				}
					
			}
			else {
				response.setEsito(StatusX4Esito.KO.name());
				response.setHttpCode(HttpStatus.NOT_FOUND.value());
				response.setErrorDescription("didn't find any assets with corresponding assetnum: " + assetNum);
			}
		}
		catch (Exception e) {
			log.info("modificaAnagraficaBulk() run into an error: {}", e.getMessage());
			response.setEsito(StatusX4Esito.KO.name());
			response.setHttpCode(HttpStatus.SERVICE_UNAVAILABLE.value());
			response.setErrorDescription(e.getMessage());
		}
		return response;
	}

	private List<AssetDTO> convertToAssetDTO(List<AssetForModificaAnagrafica> listToProcess) {
		if(Assorted.notNull(listToProcess)) {
			List<AssetDTO> assetToReturn = new ArrayList<AssetDTO>();
			listToProcess.forEach(item -> {
				AssetDTO asset = new AssetDTO();
				asset.setAssetnum(item.getAssetNum());
				asset.setClassstructureid(item.getClassStructureId());
				asset.setX3sapid(item.getX3SapId());
				asset.setStatus(item.getStatus());
				asset.setX3gewrk(item.getX3Gewrk());
				asset.setX3ingrp(item.getX3Ingrp());
				asset.setDescription(item.getDescription());
				asset.setParent(item.getParent());
				asset.setX3rootassetnum(item.getX3RootAssetNum());
				assetToReturn.add(asset);
			});
			return assetToReturn;
		}
		else {
			return null;
		}
	}
}
