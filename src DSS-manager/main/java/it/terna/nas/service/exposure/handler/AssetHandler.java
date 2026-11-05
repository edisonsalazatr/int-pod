package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.Assetspec;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.AssetQueryService;
import it.terna.nas.service.dal.service.AssetspecService;
import it.terna.nas.service.dal.service.CacheService;
import it.terna.nas.service.dal.service.ClassificationQueryService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.dal.service.X4DssWorkedHistoryQueryService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.Pair;
import it.terna.nas.service.material.dto.Shoot;
import it.terna.nas.service.material.dto.asset.AssetDTO;
import it.terna.nas.service.material.dto.asset.AssetSpecDTO;
import it.terna.nas.service.material.dto.policy.EventDTO;
import it.terna.nas.service.material.utility.Catalog.CatalogTipologyType;
import it.terna.nas.service.material.utility.Catalog.DssKafka;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Catalog.EventAttributes;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetHandler extends AbstractHandler
{
    @Autowired
    private AssetQueryService assetQueryService;
	
    @Autowired
    private AssetspecService assetspecService;
    
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

	
	private List<Pair<String, List<X4Policy>>> findPolicyWitchTronco(String assetNum) 
	        throws DataLayerException
    {
	    List<Pair<String, List<X4Policy>>> result = new ArrayList<>();
	    List<Asset> assetAssociatedTronco = assetQueryService.getAssetAssociatedTronco(assetNum);
	    if (isNotEmpty(assetAssociatedTronco))
	    {
	        for (Asset asset: assetAssociatedTronco)
	        {
	            List<X4Policy> policies = policyQueryService.getPolicyByClassStructureIdAndLogicaTronco(asset.getClassStructureId(), 1);
                if (isNotEmpty(policies)) {
                	result.add(new Pair<>(asset.getAssetNum(), policies));
                }
	        }
	    }
	    
	    return result;
    }


	@Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void manageAsset(EventDTO request) 
			throws EAMException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call manageAsset({}, {})  |{}|", request.getAttributes(), request.getAsset(), startTime);

		List<Shoot<WorkedPolicyRequestEvent>> results = transactionBlocks.requiresNew(() -> {
		    
		Map<String, String> attributes = request.getAttributes();
		if (isNotEmpty(attributes))
		{
			String assetAttrid   = attributes.get(EventAttributes.ASSETATTRID.getValue());
			String action        = attributes.get(EventAttributes.ACTION.getValue());
			String alnvalue      = notNull(attributes.get(EventAttributes.ALNVALUE.getValue()))  ? attributes.get(EventAttributes.ALNVALUE.getValue()) : null;

			if (isNotEmpty(assetAttrid)) 
			{
				if (ATTRIBUTO_ZONIZZAZIONE.equals(assetAttrid) && ACTION_KAKFA_REPLACE.equals(action) && isNotEmpty(alnvalue)) 
				{
	                    return manageAssetSpecZonizzazioneForAsset(request);
				} else {
	                    return manageAssetSpecForAsset(request);
				}
				
			} else
	                return manageAssetChange(request);

		} else
	            return manageAssetChange(request);		    
		});

		results.stream().forEach(x -> shootMessages(x));
		long endTime = System.currentTimeMillis();
		log.debug("end manageAsset({}, {})  |{}|", request.getAttributes(), request.getAsset(), endTime - startTime);
	}


	private List<Shoot<WorkedPolicyRequestEvent>> manageAssetChange(EventDTO request) 
			throws EAMException
	{
	    List<Shoot<WorkedPolicyRequestEvent>> result = new ArrayList<>();
	    
		try {
			Asset asset = assetQueryService.getAssetByAssetnum(request.getAsset());
			if (notNull(asset))
			{
			    List<Pair<String, List<X4Policy>>> policiesByAsset = new ArrayList<>();
			    if (Boolean.TRUE.equals(request.getAddTronco()))
			        policiesByAsset.addAll(findPolicyWitchTronco(request.getAsset()));
			    else {
			        List<X4Policy> policyByClassif = policyQueryService.getPolicyByClassStructureIdAndLogicaTronco(asset.getClassStructureId(), 0);
			        
			        if (isNotEmpty(policyByClassif))
			            policiesByAsset.add(new Pair<>(request.getAsset(), policyByClassif));
	                
			        policiesByAsset.addAll(findPolicyWitchTronco(request.getAsset()));
			    }
				 
				if (isNotEmpty(policiesByAsset))
				{
					Set<Integer> enabledPeriodicLevel = new HashSet<>(policyQueryService.getLivelliAbilitati());
					
					for (Pair<String, List<X4Policy>> item: policiesByAsset)
					{
	                    String assetNum         = item.getA();
	                    List<X4Policy> policies = item.getB();
	                        
	                    for (X4Policy policy: policies)
                        {
	                        BrokerEntityInfo topicKafka = PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy())
	                                ? brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY)
	                                : brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);
	                    	String policyStr        = policy.getX4PolicyId().toString();
    						String correlationKey   = policyStr + SEP4 + KafkaUtility.getTimeStamp(request.getKafkaKey());
    						String correlationKey2  = correlationKey + SEP4 + 1;
    						
	                    	
	                    	if(PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy())) 
	                    	{
	                    		boolean changeStatus = isChangeStatus(asset);
	                    		boolean levelEnabled = enabledPeriodicLevel.contains(policy.getLivPolicy());
	                    		
	                    		if(EventActionType.ADD.getValue().equalsIgnoreCase(request.getEvent())  
	                    			|| ( changeStatus && levelEnabled) ) {
	                    			 log.info(" - Processing policy: event={}, level={}, changeStatus={}, x4policyId={}, idPolicy={}", request.getEvent(),
																							                    				       policy.getLivPolicy(),
																							                    				       changeStatus,
																							                    				       policy.getX4PolicyId(),
																							                    				       policy.getIdPolicy());
	                    		} else {
	                    			log.info(" - Skipping policy: event={}, level={}, changeStatus={}, levelEnabled={}, x4policyId={}, idPolicy={}", request.getEvent(),
																											                    			         policy.getLivPolicy(),
																											                    			         changeStatus,
																											                    			         levelEnabled,
																											                    			         policy.getX4PolicyId(),
																											                    			         policy.getIdPolicy());
	                    			continue;
	                    		}
	                    	}
	                    	
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
    						
    						X4DssWorkedHistory rowHistory = x4DssWorkedHistoryQueryService.getDssWorkedHistoryByCorrelationAndPolicy(correlationKey2, policyStr);
    						if (isNull(rowHistory))
    						{                        
                                X4DssPolicyRunner row = new X4DssPolicyRunner();
                                row.setIdMacroAttivita(policy.getIdMacroAttivita());
                                row.setX4PolicyId(policy.getX4PolicyId());
                                row.setIdPolicy(policy.getIdPolicy());
                                row.setCorrelationId(correlationKey);
                                row.setDataElaborazione(new Date());
                                row.setAssetnum(assetNum);
                                row.setNumElaborati(1L);
                                row.setTipoEvento(EventType.ASSET.getValue());
                                row.setTopic(KafkaUtility.getTopicName(DssKafka.ASSET, brokerEntities.getEndPoints()));
                                row.setHasLd(0L);
                                row = x4DssPolicyRunnerQueryService.saveOrUpdate(row);
    
    							rowHistory = new X4DssWorkedHistory();
    							rowHistory.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));
    							rowHistory.setPolicyId(policyStr);
    							rowHistory.setCorrelationId(correlationKey2);
    							rowHistory.setStatus(OPEN_STATUS);
    							rowHistory.setTotalRelatedMessage(1);
    							rowHistory.setProgressiveRelatedMessage(1);
    							rowHistory.setAssetNum(assetNum);
                                rowHistory.setHasLd(0);
                                rowHistory.setRecoveryCount(1);
                                rowHistory.setX4DssPolicyRunnerId(row.getX4DssPolicyRunnerId());
    							rowHistory = x4DssWorkedHistoryQueryService.saveOrUpdate(rowHistory);
    
    							WorkedPolicyRequestEvent event     = new WorkedPolicyRequestEvent();
    							event.setPolicy(policy.getX4PolicyId());
    							event.setEvent(eventType);
    							event.setCorrelationId(correlationKey2);
    							event.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(assetNum));
    							event.setAsset(assetNum);
    							event.setDssWorkerId(rowHistory.getIdDssWorked().toString());
    							event.setNumberMessagePolicy(1L);
    							event.setTipology(policy.getTipoPolicy());
    							event.setType(EventType.POLICY.getValue());
    							event.setAddTronco(CLASS_TRONCO_ISPEZIONE.equals(eventType) ? Boolean.TRUE : null);
    							result.add(new Shoot<WorkedPolicyRequestEvent>(topicKafka, event));
    						}
                        }
					}
				}
			}     

		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
		}
		
		return result;
	}
	
	private boolean isChangeStatus(Asset asset) {
		Objects.requireNonNull(asset, "Asset is null");
		log.debug(" - Check for status change");
		return Assorted.isNotEmpty(asset.getX4oldstatus())
				? Assorted.notEquals(asset.getStatus(), asset.getX4oldstatus())
				: false;
	}


	private List<Shoot<WorkedPolicyRequestEvent>> manageAssetSpecForAsset(EventDTO request) 
			throws EAMException
	{
	    List<Shoot<WorkedPolicyRequestEvent>> result = new ArrayList<>();
	    
		try {
			Map<String, String> attributes   = request.getAttributes();
			String assetAttrid               = attributes.get(EventAttributes.ASSETATTRID.getValue());
			ClassStructureTipology typeAsset = classificationQueryService.getClassStructureTipologyByAsset(request.getAsset());
			if (notNull(typeAsset))
			{
				List<X4Policy> policies = policyQueryService.getPolicyByClassifAttrib(typeAsset.getClassificationId(), assetAttrid);
				if (isNotEmpty(policies))
				{
					BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);                   
					for (X4Policy policy: policies)
					{
						String eventType                            = EventActionType.MODIFY.getValue();
						List<ClassStructureTipology> typeJpnumList  = classificationQueryService.getClassStructureTipologyByMacroAttivita(policy.getIdMacroAttivita());
						ClassStructureTipology classTroncoSostit    = typeJpnumList.stream()
								                                                   .filter(x -> CLASS_SOSTITUZIONE.equals(x.getClassificationId()))
								                                                   .findFirst()
								                                                   .orElse(null);

						if (notNull(classTroncoSostit))
							eventType = classTroncoSostit.getClassificationId();
                        else if (notNull(policy.getLogicaTronco()) && policy.getLogicaTronco() == 1)
                            eventType = CLASS_TRONCO_ISPEZIONE;
						
						String policyStr        = policy.getX4PolicyId().toString();
						String correlationKey   = policyStr + SEP4 + KafkaUtility.getTimeStamp(request.getKafkaKey());
						String correlationKey2  = correlationKey + SEP4 + 1;
						X4DssWorkedHistory rowHistory = x4DssWorkedHistoryQueryService.getDssWorkedHistoryByCorrelationAndPolicy(correlationKey2, policyStr);

						if (isNull(rowHistory))
						{                       
                            X4DssPolicyRunner row = new X4DssPolicyRunner();
                            row.setIdMacroAttivita(policy.getIdMacroAttivita());
                            row.setX4PolicyId(policy.getX4PolicyId());
                            row.setIdPolicy(policy.getIdPolicy());
                            row.setCorrelationId(correlationKey);
                            row.setDataElaborazione(new Date());
                            row.setAssetnum(request.getAsset());
                            row.setNumElaborati(1L);
                            row.setTipoEvento(EventType.ASSETSPEC.getValue());
                            row.setTopic(KafkaUtility.getTopicName(DssKafka.ASSETSPEC, brokerEntities.getEndPoints()));
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
							result.add(new Shoot<WorkedPolicyRequestEvent>(topicKafka, event));
						}
					}
				}
			}

		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
		}
		
		return result;
	}


	private List<Shoot<WorkedPolicyRequestEvent>> manageAssetSpecZonizzazioneForAsset(EventDTO request) 
    		throws EAMException
    {
	    List<Shoot<WorkedPolicyRequestEvent>> result = new ArrayList<>();
	    
    	try {
    		long startTime = System.currentTimeMillis();
    
    		Map<String, String> attributes   = request.getAttributes();
    		String assetAttrid               = attributes.get(EventAttributes.ASSETATTRID.getValue());
    		String alnvalue 				 = attributes.get(EventAttributes.ALNVALUE.getValue());
    		
    		Asset requestedAsset = assetQueryService.getAssetByAssetnum(request.getAsset());
    		
    		if (requestedAsset == null) 
    		    return result;
    
    		List<X4Policy> policies = policyQueryService.getPolicyByClassStructureIdAttribForZonizzazione(requestedAsset.getClassStructureId(), 
    																									  assetAttrid, 
    																									  alnvalue);
    		if (!isNotEmpty(policies)) 
    		    return result;
    		
    		Set<Integer> enabledPeriodicLevel = new HashSet<>(policyQueryService.getLivelliAbilitati());
    
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY);
            
    		for (X4Policy policy: policies)
    		{

				if(enabledPeriodicLevel.contains(policy.getLivPolicy())) {
        		    log.info(" - Level {} is enabled", policy.getLivPolicy());
    			} else {
    				log.info(" - Level {} is not enabled. x4policyid={}", policy.getLivPolicy(), policy.getX4PolicyId());
    				continue;
    			}
    			
    			String eventType              = EventActionType.ZONIZZAZIONE.getValue();    
    			String policyStr              = policy.getX4PolicyId().toString();
    			String correlationKey         = policyStr + SEP4 + KafkaUtility.getTimeStamp(request.getKafkaKey());
    			String correlationKey2        = correlationKey + SEP4 + 1;
    			X4DssWorkedHistory rowHistory = x4DssWorkedHistoryQueryService.getDssWorkedHistoryByCorrelationAndPolicy(correlationKey2, policyStr);
    
    			if (rowHistory != null) 
    			    return result;
    			
    			if (policy.getLogicaTronco() == 1) 
    			{
    				Optional<String> troncoNum = assetspecService.getTroncoByAssetnum(request.getAsset())
                                    							.stream()
                                    							.findFirst()
                                    							.map(Assetspec::getTableValue);
    
    				if (troncoNum.isEmpty()) 
    				{
    					log.info("Unable to retrieve trunk or zoning attributes");
    					return result;
    				}
    
    				List<Assetspec> trunkRelatedAssets = assetspecService.getAssetAndZonizzazioneByTronco(troncoNum.get());
    				boolean allAlnValuesMatch = trunkRelatedAssets.stream()
    						                                      .allMatch(asset -> alnvalue.equals(asset.getAlnValue()));
    
    				if (trunkRelatedAssets.isEmpty() || !allAlnValuesMatch) 
    				{
    					log.info("Zonizzazione mismatch for assets in trunk {}", troncoNum.get());
    				} else {
    				    
    					int progressiveCounter = 1;
    					
                        X4DssPolicyRunner row = new X4DssPolicyRunner();
                        row.setIdMacroAttivita(policy.getIdMacroAttivita());
                        row.setX4PolicyId(policy.getX4PolicyId());
                        row.setIdPolicy(policy.getIdPolicy());
                        row.setCorrelationId(correlationKey);
                        row.setDataElaborazione(new Date());
                        row.setAssetnum(request.getAsset());
                        row.setNumElaborati(Long.valueOf(trunkRelatedAssets.size()));
                        row.setTipoEvento(EventType.ASSET.getValue());
                        row.setTopic(KafkaUtility.getTopicName(DssKafka.ASSET, brokerEntities.getEndPoints()));
                        row.setHasLd(0L);
                        row = x4DssPolicyRunnerQueryService.saveOrUpdate(row);
    					
                        for(Assetspec assetnum: trunkRelatedAssets) 
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
    						event.setEvent(eventType);
    						event.setCorrelationId(correlationKey2);
    						event.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(request.getAsset()));
    						event.setAsset(assetnum.getAssetNum());
    						event.setNumberMessagePolicy(Long.valueOf(trunkRelatedAssets.size()));
    						event.setDssWorkerId(rowHistory.getIdDssWorked().toString());
    						result.add(new Shoot<WorkedPolicyRequestEvent>(topicKafka, event));
    						progressiveCounter++;
    					}									
    				}
    			}
    			else 
    			{
                    X4DssPolicyRunner row = new X4DssPolicyRunner();
                    row.setIdMacroAttivita(policy.getIdMacroAttivita());
                    row.setX4PolicyId(policy.getX4PolicyId());
                    row.setIdPolicy(policy.getIdPolicy());
                    row.setCorrelationId(correlationKey);
                    row.setDataElaborazione(new Date());
                    row.setAssetnum(request.getAsset());
                    row.setNumElaborati(1L);
                    row.setTipoEvento(EventType.ASSET.getValue());
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
    
    				WorkedPolicyRequestEvent event = new WorkedPolicyRequestEvent();
    				event.setPolicy(policy.getX4PolicyId());
    				event.setEvent(eventType);
    				event.setCorrelationId(correlationKey2);
    				event.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(request.getAsset()));
    				event.setAsset(request.getAsset());
    				event.setDssWorkerId(rowHistory.getIdDssWorked().toString());
    				event.setNumberMessagePolicy(1L);
    				event.setTipology(policy.getTipoPolicy());
    				event.setType(EventType.POLICY.getValue());
    				result.add(new Shoot<WorkedPolicyRequestEvent>(topicKafka, event));
    			}
    		}
    	} catch(Exception e) {
    		log.error(e.getMessage(), e);
    		throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
    	}
    	
    	return result;
    }


    public void sendCreateAsset(AssetDTO request)
	{
		log.debug("call sendCreateAsset({}, {})", request.getAssetnum(), request.getStatus());
		
		sendTopicForHiCalcStaging(request.getAssetnum(), request.getSiteid());
        cacheService.evictCache(CACHE_ASSET, request.getAssetnum(), CatalogTipologyType.ASSET);
        cacheService.evictCache(CACHE_ASSET, request.getAssetnum() + SLIM_VERSION, CatalogTipologyType.ASSET);
		if (notNull(request.getStatus()) && (!STATUS_DECOM_ASSET.contains(request.getStatus())))
		{
			Date currentDate = new Date();
			String key = EventType.ASSET.getValue() + SEP4 + request.getAssetnum() + SEP4 + currentDate.getTime();

			String action = EventActionType.MODIFY.getValue();
			if (EventActionType.ADD.getValue().equalsIgnoreCase(request.getAction()))
				action = EventActionType.ADD.getValue();

			EventDTO event = new EventDTO();
			event.setType(EventType.ASSET.getValue());
			event.setAsset(request.getAssetnum());
			event.setEvent(action);
			event.setKafkaKey(key);

			log.debug("sendCreateAsset ==> {}", event);

			BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);

			kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
		}
	}


    public void sendCreateAssetSpecForAsset(AssetSpecDTO request) 
            throws DataLayerException
	{
		log.debug("call sendCreateAssetSpecForAsset({}, {}, {})", request.getAssetattrid(), request.getAssetnum(), request.getClassstructureid());

		sendTopicForHiCalcStaging(request.getAssetnum(), request.getSiteid());
        cacheService.evictCache(CACHE_ASSET, request.getAssetnum(), CatalogTipologyType.ASSET);
        cacheService.evictCache(CACHE_ASSET, request.getAssetnum() + SLIM_VERSION, CatalogTipologyType.ASSET);
		if (isNotEmpty(request.getAssetnum()) && isNotEmpty(request.getAssetattrid()) && notNull(request.getAssetspecid()))
		{
			Date currentDate = new Date();
			String key = EventType.ASSETSPEC.getValue() + SEP4 + request.getAssetspecid() + SEP4 + currentDate.getTime();

			EventDTO event = new EventDTO();
			event.setType(EventType.ASSETSPEC.getValue());
			event.setAsset(request.getAssetnum());
			event.setEvent(EventActionType.MODIFY.getValue());
			event.setKafkaKey(key);
			event.setAttributes(new HashMap<>());
			event.getAttributes().put(EventAttributes.ASSETATTRID.getValue(),    request.getAssetattrid());
			event.getAttributes().put(EventAttributes.ACTION.getValue(),         request.getAction());
			if (isNotEmpty(request.getAlnvalue()))
				event.getAttributes().put(EventAttributes.ALNVALUE.getValue(), request.getAlnvalue());
			
			log.debug("sendCreateAssetSpecForAsset ==> {}", event);

			BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
			kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
		}
	}
}
