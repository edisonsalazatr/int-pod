package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.AssetQueryService;
import it.terna.nas.service.dal.service.CacheService;
import it.terna.nas.service.dal.service.ClassificationQueryService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.dal.service.X4DssWorkedHistoryQueryService;
import it.terna.nas.service.dal.service.X4MisureAssetQueryService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.AssetMeasureChangedEvent;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.Shoot;
import it.terna.nas.service.material.dto.policy.EventDTO;
import it.terna.nas.service.material.utility.Catalog.CatalogTipologyType;
import it.terna.nas.service.material.utility.Catalog.DssKafka;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Catalog.EventAttributes;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AssetEventHandler extends AbstractHandler
{
    private final CacheService cacheService;
    private final ClassificationQueryService classificationQueryService;
    private final PolicyQueryService policyQueryService;
    private final X4DssWorkedHistoryQueryService x4DssWorkedHistoryQueryService;
    private final X4DssPolicyRunnerQueryService x4DssPolicyRunnerQueryService;
    private final AssetQueryService assetQueryService;
    private final X4MisureAssetQueryService x4MisureAssetQueryService;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public void handleMisuraEvent(EventDTO request) 
            throws EAMRetryException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call handleMisuraEvent({}, {})  |{}|", request.getAttributes(), request.getAsset(), startTime);
        Shoot<WorkedPolicyRequestEvent> result = transactionBlocks.requiresNew(() -> {        
            return manageAssetMeasureEvent(request);
        });           
        shootMessages(result);
        
        long endTime = System.currentTimeMillis();
        log.debug("end handleMisuraEvent({}, {})  |{}|", request.getAttributes(), request.getAsset(), endTime - startTime);
    }

    @Datasource(datasourceKey = EDatasourceKey.EAM)
	private Shoot<WorkedPolicyRequestEvent> manageAssetMeasureEvent(EventDTO request) 
	        throws EAMException 
	{
        Shoot<WorkedPolicyRequestEvent> results = new Shoot<>();
		try {
            Map<String, String> attributes  = request.getAttributes();
            String idMisura         		= attributes.get(EventAttributes.IDMISURA.getValue());
            if (notNull(idMisura)) 
            {
            	// retrieve classificationId for asset
            	Asset asset = assetQueryService.getAssetByAssetnum(request.getAsset());
                if (notNull(asset))
                {
                	
                	// retrieve all policies linked by the measure
                    List<X4Policy> policies = policyQueryService.getPolicyByClassStructureIdAndTipologiaInput(asset.getClassStructureId(), 
                    																						  CatalogTipologyType.VARIABILE.getValue(),
                    																						  idMisura);
                    if (isNotEmpty(policies))
                    {                       
                        BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);
                        results.setEntity(topicKafka);
                        for (X4Policy policy: policies)
                        {
                            String eventType                            = EventActionType.MODIFY.getValue();
                            List<ClassStructureTipology> typeJpnumList  = classificationQueryService.getClassStructureTipologyByMacroAttivita(policy.getIdMacroAttivita());
                            ClassStructureTipology classSostit          = typeJpnumList.stream()
                                                                                       .filter(x -> CLASS_SOSTITUZIONE.equals(x.getClassificationId()))
                                                                                       .findFirst()
                                                                                       .orElse(null);

                            if (notNull(classSostit))
                                eventType = classSostit.getClassificationId();
                            else if (notNull(policy.getLogicaTronco()) && policy.getLogicaTronco() == 1)
                                eventType = CLASS_TRONCO_ISPEZIONE;
                            
                            String policyStr        = policy.getX4PolicyId().toString();
                            String correlationKey   = policyStr + SEP4 + KafkaUtility.getTimeStamp(request.getKafkaKey());
                            String correlationKey2  = correlationKey + SEP4 + 1;
                            X4DssWorkedHistory rowHistory = x4DssWorkedHistoryQueryService.getDssWorkedHistoryByCorrelationAndPolicy(correlationKey2, policyStr);
                              
                            if (Assorted.isNull(rowHistory))
                            {                        
                                X4DssPolicyRunner row = new X4DssPolicyRunner();
                                row.setIdMacroAttivita(policy.getIdMacroAttivita());
                                row.setX4PolicyId(policy.getX4PolicyId());
                                row.setIdPolicy(policy.getIdPolicy());
                                row.setCorrelationId(correlationKey);
                                row.setDataElaborazione(new Date());
                                row.setAssetnum(request.getAsset());
                                row.setNumElaborati(1L);
                                row.setTipoEvento(EventType.MISURA.getValue());
                                row.setTopic(KafkaUtility.getTopicName(DssKafka.MISURA, brokerEntities.getEndPoints()));
                                row.setHasLd(0L);
                                row = x4DssPolicyRunnerQueryService.saveOrUpdate(row);
                                
                                rowHistory = new X4DssWorkedHistory();
                                rowHistory.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));
                                rowHistory.setPolicyId(policyStr);
                                rowHistory.setCorrelationId(correlationKey2);
                                rowHistory.setStatus(OPEN_STATUS);
                                rowHistory.setTotalRelatedMessage(1);
                                rowHistory.setProgressiveRelatedMessage(1);
                                rowHistory.setAssetNum(request.getAsset());
                                rowHistory.setHasLd(0);
                                rowHistory.setRecoveryCount(1);
                                rowHistory.setX4DssPolicyRunnerId(row.getX4DssPolicyRunnerId());
                                rowHistory = x4DssWorkedHistoryQueryService.saveOrUpdate(rowHistory);
                              
                                WorkedPolicyRequestEvent event     = new WorkedPolicyRequestEvent();
                                event.setPolicy(policy.getX4PolicyId());
                                event.setEvent(eventType);
                                event.setCorrelationId(correlationKey2);
                                event.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(request.getAsset()));
                                event.setAsset(request.getAsset());
                                event.setDssWorkerId(rowHistory.getIdDssWorked().toString());
                                event.setNumberMessagePolicy(1L);
                                event.setTipology(policy.getTipoPolicy());
                                event.setType(EventType.POLICY.getValue());
                                results.getItems().add(event);
                            }
                        }
                    }
                } else {
                	log.error("  - Cannot find a classificationId for asset {}", request.getAsset());
                }
            } else {
            	log.error("  - Message doesn't carry a value for x4MisureAssetId [{}]", request);
            }
            
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
        }
		return results;     
    }
	
	
	/**
	 * Will produce a dto to map a json object like the following
	 * 
	 * {
	 *    "event": "MODIFY",
	 *    "type": "MISURA",
	 *    "asset": "<request.assetnum>",
	 *    "attributes": {
	 *        "x4misureassetid": "<request.x4misureassetid>"
	 *     }
	 * }
	 * @param event
	 * @throws EAMRetryException if the measure doesn't exist on X4MISUREASSET
	 */
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public void notifyAssetMeasureChanged(AssetMeasureChangedEvent request)
			throws EAMRetryException
	{
		log.debug("call sendNotifyAssetMeasureChanged({}, {}, {}, {})", request.getAssetnum(), 
																		        request.getX4misureassetid(),
																		        request.getIdmisura(), 
																		        request.getValore());
		
		checkMeasureExists(request);

        cacheService.evictCache(CACHE_ASSET, request.getAssetnum(), CatalogTipologyType.ASSET);
        cacheService.evictCache(CACHE_ASSET, request.getAssetnum() + SLIM_VERSION, CatalogTipologyType.ASSET);        
		sendTopicForHiCalcStaging(request.getAssetnum(), request.getSiteid());
		
        // evict from cache
        
		Date currentDate  = new Date();
		EventDTO event    = new EventDTO();
		/*
		 *  La chiave del messaggio è data dalla concatenazione della stringa 
		 *  “MISURA-<request. x4misureassetid>-” più il timestamp in millisecondi.
		 */
		String key = EventType.MISURA.getValue() + SEP4 + request.getX4misureassetid() + SEP4 + currentDate.getTime();
		event.setKafkaKey(key);
		
		event.setEvent(EventActionType.MODIFY.getValue());
		event.setType(EventType.MISURA.getValue());
		event.setAsset(request.getAssetnum());
		event.setAttributes(new HashMap<>());
		event.getAttributes().put(EventAttributes.X4MISUREASSETID.getValue(), request.getX4misureassetid());
		event.getAttributes().put(EventAttributes.IDMISURA.getValue(), request.getIdmisura());

		log.debug("sendNotifyAssetMeasureChanged ==> {}", event);
		
		BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
		
		kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
	}
	
	
	/**
	 * Verifies that the measure identified by x4misureassetid actually exists on X4MISUREASSET
	 * with an ULTIMARILEVAZIONE greater than or equal to the one carried by the event.
	 * If the check fails (missing/invalid id, unparsable date or measure not found) an
	 * {@link EAMRetryException} is thrown so the message can be retried.
	 * 
	 * @param request the event to validate
	 * @throws EAMRetryException if the measure doesn't exist or the request is invalid
	 */
	private void checkMeasureExists(AssetMeasureChangedEvent request) 
			throws EAMRetryException
	{
		if (!notNull(request.getX4misureassetid())) 
		{
			log.error("Message doesn't carry a value for x4misureassetid");
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Message doesn't carry a value for x4misureassetid");
		}
		
		Long x4MisureAssetId;
		try {
			x4MisureAssetId = Long.valueOf(request.getX4misureassetid());
		} catch (NumberFormatException e) {
			log.error("Invalid value for x4misureassetid [{}]", request.getX4misureassetid());
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Invalid value for x4misureassetid: " + request.getX4misureassetid(), e);
		}
		
        Date ultimaRilevazione =  notNull(request.getUltimarilevazione()) 
                                    ? Assorted.stringToDate(request.getUltimarilevazione(), MAXIMO_DATE_AND_TIME)
                                    : null;

		if (ultimaRilevazione != null && (!x4MisureAssetQueryService.isMeasureExists(x4MisureAssetId, ultimaRilevazione)))
		{
			log.error("Measure not found for x4misureassetid [{}] with ultimarilevazione >= [{}]", request.getX4misureassetid(), request.getUltimarilevazione());
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Measure not found for x4misureassetid " + request.getX4misureassetid());
		}
	}
}
