package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.ACTION_KAKFA_REPLACE;
import static it.terna.nas.service.material.utility.Constants.CACHE_ASSET;
import static it.terna.nas.service.material.utility.Constants.CLASS_SOSTITUZIONE;
import static it.terna.nas.service.material.utility.Constants.CLASS_TRONCO_ISPEZIONE;
import static it.terna.nas.service.material.utility.Constants.END_POINT_EVALUATE_WORKER_POLICY;
import static it.terna.nas.service.material.utility.Constants.END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY;
import static it.terna.nas.service.material.utility.Constants.END_POINT_MANAGER_EVENTS_HANDLER;
import static it.terna.nas.service.material.utility.Constants.OPEN_STATUS;
import static it.terna.nas.service.material.utility.Constants.SEP4;
import static it.terna.nas.service.material.utility.Constants.SLIM_VERSION;
import static it.terna.nas.service.material.utility.Constants.STATUS_APERTO_ODL;
import static it.terna.nas.service.material.utility.Constants.STATUS_CHIUSO_ODL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.Shoot;
import it.terna.nas.service.material.dto.policy.EventDTO;
import it.terna.nas.service.material.dto.workorder.WorkOrderDTO;
import it.terna.nas.service.material.utility.Catalog.CatalogTipologyType;
import it.terna.nas.service.material.utility.Catalog.DssKafka;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Catalog.EventAttributes;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkOrderHandler extends AbstractHandler
{	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private ClassificationQueryService classificationQueryService;
	
	@Autowired
	private PolicyQueryService policyQueryService;
	
	@Autowired
	private X4DssPolicyRunnerQueryService x4DssPolicyRunnerQueryService;

	@Autowired
	private X4DssWorkedHistoryQueryService x4DssWorkedHistoryQueryService;
	
	@Autowired
	private AssetQueryService assetQueryService;
	
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void handleCreateOdLForAsset(EventDTO request) 
            throws EAMRetryException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call handleCreateOdLForAssetEvent({}, {})  |{}|", request.getAttributes(), request.getAsset(), startTime);
        Shoot<WorkedPolicyRequestEvent> result = transactionBlocks.requiresNew(() -> {        
            return manageCreateOdLForAsset(request);
        });           
        shootMessages(result);
        
        long endTime = System.currentTimeMillis();
        log.debug("end handleCreateOdLForAssetEvent({}, {})  |{}|", request.getAttributes(), request.getAsset(), endTime - startTime);
    }
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	private Shoot<WorkedPolicyRequestEvent> manageCreateOdLForAsset(EventDTO request)
		throws EAMException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call manageCreateOdLForAsset({}, {}, {})  |{}|", request.getAttributes(), request.getWonum(), request.getAsset(), startTime);
        Shoot<WorkedPolicyRequestEvent> results = new Shoot<>();
		
		try {
			Asset asset = assetQueryService.getAssetByAssetnum(request.getAsset());
			if (notNull(asset))
			{
				String attivita 		= request.getAttributes().get(EventAttributes.JPNUM.getValue());
				String x4PolicyId 		= request.getAttributes().get(EventAttributes.POLICY.getValue());
				List<X4Policy> policies = policyQueryService.getPolicyByClassStructureIdAndJpnum(asset.getClassStructureId(), attivita, x4PolicyId);
				
				if (isNotEmpty(policies))
				{
					String eventType = EventActionType.MODIFY.getValue();
					ClassStructureTipology typeJpnum = classificationQueryService.getClassStructureTipologyByAttivita(attivita);
					
					if (notNull(typeJpnum) && CLASS_SOSTITUZIONE.equals(typeJpnum.getClassificationId()))
						eventType = typeJpnum.getClassificationId();
	
					BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);
                    results.setEntity(topicKafka);
					
					for (X4Policy policy: policies)
					{
						String policyStr 		= policy.getX4PolicyId().toString();
						String correlationKey 	= policyStr + SEP4 + KafkaUtility.getTimeStamp(request.getKafkaKey());
						String correlationKey2  = correlationKey + SEP4 + 1;
						String eventTypePolicy  = (notNull(policy.getLogicaTronco()) && policy.getLogicaTronco() == 1) ? CLASS_TRONCO_ISPEZIONE : eventType;
						        
						try {
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
                                row.setTipoEvento(EventType.ODL.getValue());
                                row.setTopic(KafkaUtility.getTopicName(DssKafka.CREATE_ODL, brokerEntities.getEndPoints()));
                                row.setHasLd(0L);
                                row = x4DssPolicyRunnerQueryService.saveOrUpdate(row);

								rowHistory = new X4DssWorkedHistory();
								rowHistory.setCreatedDate(new java.sql.Date(startTime));
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
								
								WorkedPolicyRequestEvent event 	= new WorkedPolicyRequestEvent();
								event.setPolicy(policy.getX4PolicyId());
								event.setEvent(eventTypePolicy);
								event.setCorrelationId(correlationKey2);
								event.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(request.getAsset()));
								event.setAsset(request.getAsset());
								event.setDssWorkerId(rowHistory.getIdDssWorked().toString());
                                results.getItems().add(event);
							}
							
						} catch(Exception e) {
							log.error(e.getMessage(), e);
							throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
						}
					}
				}
			}
			
			
		} catch(EAMException q) {
			throw q;
		} catch(Exception q) {
			log.error(q.getMessage(), q);
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), q.getMessage(), q);
		}
		
		long endTime = System.currentTimeMillis();
		log.debug("end manageCreateOdLForAsset({}, {}, {})  |{}|", request.getAttributes(), request.getWonum(), request.getAsset(), endTime - startTime);
		return results;
	}

	
	public void sendCreateOdLForAsset(WorkOrderDTO request)
	{
		log.debug("call sendCreateOdLForAsset({}, {}, {}, {})", request.getWonum(), request.getAction(), request.getAssetnum(), request.getJpnum());
		
		cacheService.evictCache(CACHE_ASSET, request.getAssetnum(), CatalogTipologyType.ASSET);
		cacheService.evictCache(CACHE_ASSET, request.getAssetnum() + SLIM_VERSION, CatalogTipologyType.ASSET);
		sendTopicForHiCalcStaging(request.getAssetnum(), request.getSiteid());
		if (STATUS_APERTO_ODL.equals(request.getStatus()) && isNotEmpty(request.getAssetnum()) && isNotEmpty(request.getJpnum()))
		{
			Date currentDate = new Date();
			String key = EventType.ODL.getValue() + SEP4 + request.getWonum() + SEP4 + currentDate.getTime();

			EventDTO event = new EventDTO();
			event.setType(EventType.ODL.getValue());
			event.setAsset(request.getAssetnum());
			event.setEvent(EventActionType.MODIFY.getValue());
			event.setWonum(request.getWonum());
			event.setKafkaKey(key);
			event.setAttributes(new HashMap<>());
			event.getAttributes().put(EventAttributes.JPNUM.getValue(), request.getJpnum());

			if (notNull(request.getX4policyid()))
				event.getAttributes().put(EventAttributes.POLICY.getValue(), request.getX4policyid().toString());
			
			log.debug("sendCreateOdLForAsset ==> {}", event);
			
			BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
			
			kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
		}
	}
	
	
	public void sendClosedOdLForAsset(WorkOrderDTO request)
	{
		log.debug("call sendClosedOdLForAsset({}, {}, {}, {})", request.getWonum(), request.getAction(), request.getAssetnum(), request.getJpnum());
		
		cacheService.evictCache(CACHE_ASSET, request.getAssetnum(), CatalogTipologyType.ASSET);
		cacheService.evictCache(CACHE_ASSET, request.getAssetnum() + SLIM_VERSION, CatalogTipologyType.ASSET);
		
		if (ACTION_KAKFA_REPLACE.equals(request.getAction())
				&& STATUS_CHIUSO_ODL.equals(request.getStatus())
				&& isNotEmpty(request.getAssetnum()) 
				&& isNotEmpty(request.getJpnum()) 
				&& request.getParent() == null
				&& request.getX4policyid() != null)
		{
			Date currentDate = new Date();
			String key = EventType.ODLCLOSED.getValue() + SEP4 + request.getWonum() + SEP4 + currentDate.getTime();

			EventDTO event = new EventDTO();
			event.setType(EventType.ODLCLOSED.getValue());
			event.setAsset(request.getAssetnum());
			event.setEvent(EventActionType.MODIFY.getValue());
			event.setWonum(request.getWonum());
			event.setKafkaKey(key);
			event.setAttributes(new HashMap<>());
			event.getAttributes().put(EventAttributes.JPNUM.getValue(), request.getJpnum());
			event.getAttributes().put(EventAttributes.POLICY.getValue(), request.getX4policyid().toString());
			event.getAttributes().put(EventAttributes.X4TRONCONUM.getValue(), request.getX4tronconum());

			
			log.debug("sendClosedOdLForAsset ==> {}", event);
			
			BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
			
			kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
		}
	}
	
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void handleClosedOdLForAsset(EventDTO request) 
            throws EAMRetryException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call handleClosedOdLForAsset({}, {})  |{}|", request.getAttributes(), request.getAsset(), startTime);
        Shoot<WorkedPolicyRequestEvent> result = transactionBlocks.requiresNew(() -> {        
            return manageClosedOdLForAsset(request);
        });           
        shootMessages(result);
        
        long endTime = System.currentTimeMillis();
        log.debug("end handleClosedOdLForAsset({}, {})  |{}|", request.getAttributes(), request.getAsset(), endTime - startTime);
    }
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	private Shoot<WorkedPolicyRequestEvent> manageClosedOdLForAsset(EventDTO request)
			throws EAMException
		{
			long startTime = System.currentTimeMillis();
			log.debug("call manageClosedOdLForAsset({}, {}, {})  |{}|", request.getAttributes(), request.getWonum(), request.getAsset(), startTime);
	        Shoot<WorkedPolicyRequestEvent> results = new Shoot<>();
			
			try {
				ClassStructureTipology typeAsset = classificationQueryService.getClassStructureTipologyByAsset(request.getAsset());
				
				if (notNull(typeAsset))
				{
					String x4PolicyId 		= request.getAttributes().get(EventAttributes.POLICY.getValue());
					X4Policy policy         = policyQueryService.findActivePeriodicPolicyById(x4PolicyId);
					
					if (Assorted.notNull(policy))
					{
						String eventType = EventActionType.MODIFY.getValue();
		
						BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY);
	                    results.setEntity(topicKafka);
						String policyStr 		= policy.getX4PolicyId().toString();
						String correlationKey 	= policyStr + SEP4 + KafkaUtility.getTimeStamp(request.getKafkaKey());
						String correlationKey2  = correlationKey + SEP4 + 1;
						
							try {
								X4DssWorkedHistory rowHistory = x4DssWorkedHistoryQueryService.getDssWorkedHistoryByCorrelationAndPolicy(correlationKey2, policyStr);
								
								if (Assorted.isNull(rowHistory))
								{		

								    if (policy.getLogicaTronco() == 1 && request.getAttributes() != null && Assorted.notNull(request.getAttributes().get(EventAttributes.X4TRONCONUM.getValue()))) 
									{
										List<Asset> trunkRelatedAssets = assetQueryService.getAssetByTronco(request.getAttributes().get(EventAttributes.X4TRONCONUM.getValue()));
										int progressiveCounter = 1;

										if (Assorted.isEmpty(trunkRelatedAssets))
										    return null;
										
	                                    X4DssPolicyRunner row = new X4DssPolicyRunner();
	                                    row.setIdMacroAttivita(policy.getIdMacroAttivita());
	                                    row.setX4PolicyId(policy.getX4PolicyId());
	                                    row.setIdPolicy(policy.getIdPolicy());
	                                    row.setCorrelationId(correlationKey);
	                                    row.setDataElaborazione(new Date());
	                                    row.setAssetnum(request.getAsset());
	                                    row.setNumElaborati(Long.valueOf(trunkRelatedAssets.size()));
	                                    row.setTipoEvento(EventType.ODLCLOSED.getValue());
	                                    row.setTopic(KafkaUtility.getTopicName(DssKafka.CLOSE_ODL, brokerEntities.getEndPoints()));
	                                    
	                                    row.setHasLd(0L);
	                                    row = x4DssPolicyRunnerQueryService.saveOrUpdate(row);
										for(Asset assetnum: trunkRelatedAssets) 
										{
											rowHistory = new X4DssWorkedHistory();
											rowHistory.setCreatedDate(new java.sql.Date(startTime));
											rowHistory.setPolicyId(policyStr);
											rowHistory.setCorrelationId(correlationKey2);
											rowHistory.setStatus(OPEN_STATUS);
											rowHistory.setTotalRelatedMessage(trunkRelatedAssets.size());
											rowHistory.setProgressiveRelatedMessage(progressiveCounter);
											rowHistory.setAssetNum(assetnum.getAssetNum());
											rowHistory.setHasLd(0);
			                                rowHistory.setRecoveryCount(1);
			                                rowHistory.setX4DssPolicyRunnerId(row.getX4DssPolicyRunnerId());
											rowHistory = x4DssWorkedHistoryQueryService.saveOrUpdate(rowHistory);
											
											WorkedPolicyRequestEvent event 	= new WorkedPolicyRequestEvent();
											event.setPolicy(policy.getX4PolicyId());
											event.setEvent(CLASS_TRONCO_ISPEZIONE);
											event.setCorrelationId(correlationKey2);
											event.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(request.getAsset()));
											event.setAsset(assetnum.getAssetNum());
											event.setNumberMessagePolicy(Long.valueOf(trunkRelatedAssets.size()));
											event.setDssWorkerId(rowHistory.getIdDssWorked().toString());
			                                results.getItems().add(event);
										    progressiveCounter++;
										}
										
									} else {
									    X4DssPolicyRunner row = new X4DssPolicyRunner();
                                        row.setIdMacroAttivita(policy.getIdMacroAttivita());
                                        row.setX4PolicyId(policy.getX4PolicyId());
                                        row.setIdPolicy(policy.getIdPolicy());
                                        row.setCorrelationId(correlationKey);
                                        row.setDataElaborazione(new Date());
                                        row.setAssetnum(request.getAsset());
                                        row.setNumElaborati(1L);
                                        row.setTipoEvento(EventType.ODLCLOSED.getValue());
                                        row.setTopic(KafkaUtility.getTopicName(DssKafka.CLOSE_ODL, brokerEntities.getEndPoints()));
                                        row.setHasLd(0L);
                                        row = x4DssPolicyRunnerQueryService.saveOrUpdate(row);
	                                        
										rowHistory = new X4DssWorkedHistory();
										rowHistory.setCreatedDate(new java.sql.Date(startTime));
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
										
										WorkedPolicyRequestEvent event 	= new WorkedPolicyRequestEvent();
										event.setPolicy(policy.getX4PolicyId());
										event.setEvent(eventType);
										event.setCorrelationId(correlationKey2);
										event.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(request.getAsset()));
										event.setAsset(request.getAsset());
										event.setDssWorkerId(rowHistory.getIdDssWorked().toString());
		                                results.getItems().add(event);
									}

								}
								
							} catch(Exception e) {
								log.error(e.getMessage(), e);
								throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
							}
						}
					}
				
				
			} catch(EAMException q) {
				throw q;
			} catch(Exception q) {
				log.error(q.getMessage(), q);
				throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), q.getMessage(), q);
			}
			
			long endTime = System.currentTimeMillis();
			log.debug("end manageClosedOdLForAsset({}, {}, {})  |{}|", request.getAttributes(), request.getWonum(), request.getAsset(), endTime - startTime);
			return results;
		}

}
