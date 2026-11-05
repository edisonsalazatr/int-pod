package it.terna.nas.service.exposure.handler;

import static it.terna.nas.service.material.utility.Constants.END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY;
import static it.terna.nas.service.material.utility.Constants.END_POINT_EVALUATE_WORKER_POLICY;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.AX4Policy;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyError;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.entity.eam.X4PolicyClassifications;
import it.terna.nas.service.dal.service.CacheService;
import it.terna.nas.service.dal.service.ClassificationQueryService;
import it.terna.nas.service.dal.service.PolicyAggregateKPService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyErrorQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.dal.service.X4DssWorkedHistoryQueryService;
import it.terna.nas.service.dal.service.repository.IAssetMinimal;
import it.terna.nas.service.dal.service.repository.X4PolicyClassificationsRepository;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.executor.ExecutorSendKafkaAssetNumConfigurator;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.ExecuteDtoResult;
import it.terna.nas.service.material.dto.Pair;
import it.terna.nas.service.material.dto.Shoot;
import it.terna.nas.service.material.dto.policy.AttributesDTO;
import it.terna.nas.service.material.dto.policy.EventDTO;
import it.terna.nas.service.material.dto.policy.PairAttributeDTO;
import it.terna.nas.service.material.dto.policy.PairAttributeStringDTO;
import it.terna.nas.service.material.utility.Catalog.CatalogTipologyType;
import it.terna.nas.service.material.utility.Catalog.DssKafka;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.Esito;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.Catalog.PolicyAttributes;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PolicyHandler extends AbstractHandler
{
	private static final String MSG_ERROR_PERIODIC     = "Error generic periodic manage";
	private static final int BUFFER_BUILDER            = 2500;
	
    @Autowired
    private CacheService cacheService;
    
	@Autowired
	private ClassificationQueryService classificationService;

	@Autowired
	private PolicyQueryService policyQueryService;
	
	@Autowired
	private ExecutorSendKafkaAssetNumConfigurator threadPool;
	
	@Autowired
	private X4PolicyClassificationsRepository x4PolicyClassifications; 
	
	@Autowired
	private X4DssPolicyErrorQueryService x4DssPolicyErrorQueryService;
	
	@Autowired
	private X4DssPolicyRunnerQueryService x4DssPolicyRunnerQueryService;
	
	@Autowired
    private X4DssWorkedHistoryQueryService x4DssWorkedHistoryQueryService;
    
	@Autowired 
	private AssetDiscoverer assetDiscoverer;
	
	@Autowired
    private PolicyAggregateKPService policyAggregateKPService;
	
	
	private AttributesDTO createAttributesForPolicy(EventDTO request, X4Policy policy) 
			throws DataLayerException
	{
		AttributesDTO result = null;
		
		if (Assorted.isEmpty(request.getAttributes()))
			return result;
		
		AX4Policy policyHistory = policyQueryService.getPolicyHistory(policy.getX4PolicyId().toString(), policy.getVersion());
		String troncoNum        = request.getAttributes().get(PolicyAttributes.TRONCONUM.name());
		if (Assorted.notNull(policyHistory))
		{
		    result                      = new AttributesDTO();
		    Boolean changeFrequenza 	= Boolean.parseBoolean(request.getAttributes().get(PolicyAttributes.FREQUENCY.name()));
			Boolean changeDeferrability = Boolean.parseBoolean(request.getAttributes().get(PolicyAttributes.DEFERRABILITY.name()));
			Boolean changeMaxAdvance	= Boolean.parseBoolean(request.getAttributes().get(PolicyAttributes.MAX_ADVANCE.name()));
			Boolean changeMaxSnooze		= Boolean.parseBoolean(request.getAttributes().get(PolicyAttributes.MAX_SNOOZE.name()));
			Boolean changeRifTemporale	= Boolean.parseBoolean(request.getAttributes().get(PolicyAttributes.RIF_TEMPORALE.name()));

			if (Boolean.TRUE.equals(changeFrequenza))
				result.setFrequency(new PairAttributeDTO(policyHistory.getFrequenza(), policy.getFrequenza()));

			if (Boolean.TRUE.equals(changeDeferrability))
				result.setDeferrability(new PairAttributeDTO(policyHistory.getDifferibilita(), policy.getDifferibilita()));

			if (Boolean.TRUE.equals(changeMaxAdvance))
				result.setMaxAdvance(new PairAttributeDTO(policyHistory.getMaxAnticipo(), policy.getMaxAnticipo()));

			if (Boolean.TRUE.equals(changeMaxSnooze))
				result.setMaxSnooze(new PairAttributeDTO(policyHistory.getMaxPosticipo(), policy.getMaxPosticipo()));
			
			if (Boolean.TRUE.equals(changeRifTemporale))
				result.setRifTemporale(new PairAttributeStringDTO(policyHistory.getX4RifTemporale(), policy.getX4RifTemporale()));
		}
		
		if (Assorted.isNotEmpty(troncoNum))
		{
		    if (Assorted.isNull(result))
		        result = new AttributesDTO();
		    
		    result.setTroncoNum(troncoNum);
		}
		
		return result;
	}


	private List<String> getClassStructureIdsByPolicy(X4Policy policy) 
			throws EAMException
	{
		log.debug("call getClassStructureIdsByPolicy({})", policy.getX4PolicyId());
		
		List<String> output 	= new ArrayList<>();
		Set<String> setType		= new HashSet<>();
		
		List<X4PolicyClassifications> classifications = x4PolicyClassifications.findClassificationByPolicy(policy.getX4PolicyId());
		if (Assorted.isNotEmpty(classifications))
		{
			classifications.stream().forEach(x -> setType.add(x.getClassStructureId()));
		}
		
		output.addAll(setType);
		log.debug("getClassStructureIdsByPolicy({}) ==> {}", policy.getX4PolicyId(), output);
		return output;
	}


	@Transactional(readOnly = true)
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public void managePeriodicPolicy(EventDTO request)
		throws EAMException
	{
		boolean retry = false;
		try {
			List<X4Policy> policy = policyQueryService.getPolicyByTipologia(Constants.TIPOLOGY_PERIODICA, request.getPolicy());
			retry = sendTopicKafkaForPeriodic(request, policy);			
		} catch(Exception p) {
			log.error(p.getMessage(), p);
			retry = true;
		}
		
		if (retry)
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), MSG_ERROR_PERIODIC);
	}


    @Transactional(propagation = Propagation.NOT_SUPPORTED)
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public void managePolicy(EventDTO request)
    {
        Shoot<WorkedPolicyRequestEvent> result = transactionBlocks.requiresNew(() -> {
            return generateEvents(request);
        });
        
        shootMessages(result);        
    }
    
    
	public Shoot<WorkedPolicyRequestEvent> generateEvents(EventDTO request)
		throws EAMException
	{
		Assert.notNull(request.getKafkaKey(), "kafka key is null");
		String scheduleUUID = MDC.get(Common.MDC_TRANSACTION_ID);
		Long timeStamp		= System.currentTimeMillis();
		log.info("managePolicy(TimeStamp Elab {})", timeStamp);
		
		try {
			X4Policy policy = policyQueryService.getPolicy(request.getPolicy());
			
			if (Assorted.isNull(policy))
			{
				log.error("Policy not found: {}", request.getPolicy());
				throw new EAMException(EAMErrorMapping.EAM_0002.getEamCode(), EAMErrorMapping.EAM_0002.getErrorDescription());
			}
			
			List<String> classStructureIds = getClassStructureIdsByPolicy(policy);
			log.debug("ClassStructureTipology {}", classStructureIds);
			
			if (Assorted.isEmpty(classStructureIds))
			{	
				log.error("ClassStructureTipology not found, check policy classification for X4PolicyID: {}", policy.getX4PolicyId());				
				throw new EAMException(EAMErrorMapping.EAM_0003.getEamCode(), EAMErrorMapping.EAM_0003.getErrorDescription());
			}

			if (Assorted.notNull(policy.getLogicaTronco()) && policy.getLogicaTronco() == 1)
                request.setEvent(Constants.CLASS_TRONCO_ISPEZIONE);
			else {
			    List<ClassStructureTipology> typeJpnumList  = classificationService.getClassStructureTipologyByMacroAttivita(policy.getIdMacroAttivita());
			    ClassStructureTipology classTroncoSostit    = typeJpnumList.stream()
                                                                           .filter(x -> Constants.CLASS_SOSTITUZIONE.equals(x.getClassificationId()))
                                                                           .findFirst()
                                                                           .orElse(null);
            
			    if (Assorted.notNull(classTroncoSostit))
			        request.setEvent(classTroncoSostit.getClassificationId());
			}
			
			return sendTopicKafkaToWorker(scheduleUUID, request, policy, classStructureIds);
		} catch(EAMException e) {
			log.error(e.getMessage());
			throw e;
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new EAMException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e);
		}
	}


	private CompletableFuture<ExecuteDtoResult> save(String scheduleUUID, 
													int step, 
													EventDTO request,
													X4Policy policy,
													AttributesDTO attributes,
													String listAssetNumStr, 
													Map<String, Pair<String, Integer>> mapAssetParent,
													Integer numAsset,
													X4DssPolicyRunner runner) 
			throws DataLayerException
	{
		return CompletableFuture.supplyAsync(() -> {
			long startTime = System.currentTimeMillis();
			MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID + Constants.SEP3 + step);
			ExecuteDtoResult output = new ExecuteDtoResult();
			output.setListShoot(new ArrayList<>());

			try {
			    boolean isAggregate = Constants.CLASS_SOSTITUZIONE.equals(request.getEvent()) && Constants.IS_POLICY_AGGREGATE.equals(policy.getPolicyAggr()) && Assorted.notNull(policy.getLivAggr());
			    boolean aggregateStazione = isPolicyAggregateStazione(policy);
			    String correlation  = request.getKafkaKey();
				Pair<String, String> assetToSend = x4DssWorkedHistoryQueryService.saveDssWorkedHistory(correlation, numAsset, request.getPolicy(), Assorted.checkNullString(null), listAssetNumStr, runner, 1);

				if (Assorted.isNotEmpty(assetToSend.getA())) 
				{
					long startTimeSendKafka 	 	= System.currentTimeMillis();
					List<String> listAssetToSend 	= Arrays.asList(assetToSend.getA().split("\\|"));
					List<String> listWorkDssToSend 	= Arrays.asList(assetToSend.getB().split("\\|"));
                   
					for (int i = 0; i < listAssetToSend.size(); i++) 
					{
                        WorkedPolicyRequestEvent event 	= new WorkedPolicyRequestEvent();
                        event.setPolicy(policy.getX4PolicyId());
                        event.setEvent(request.getEvent());
                        event.setAttributes(attributes);
                        event.setNumberMessagePolicy(numAsset.longValue());
                        event.setTipology(policy.getTipoPolicy());
                        event.setType(EventType.POLICY.getValue());
                        event.setAggregationLevel(isAggregate ? policy.getLivAggr() : null);
                        String assetNum = listAssetToSend.get(i);
                        event.setCorrelationId(correlation + Constants.SEP4 + 1);
                        event.setNotify(aggregateStazione
                                                ? String.valueOf(mapAssetParent.get(assetNum).getB())
                                                : x4DssWorkedHistoryQueryService.getDssAssetNum(assetNum) );
                        event.setAsset(assetNum);
                        event.setDssWorkerId(listWorkDssToSend.get(i));
                        output.getListShoot().add(event);
                    }
					long endTimeSendkafka = System.currentTimeMillis();
					log.debug("Send Event in |{}| for step {}. Num shoot: {}", endTimeSendkafka - startTimeSendKafka, step, listAssetToSend.size());
				}

				output.setEsito(Esito.OK);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
				output.setEsito(Esito.KO);
				output.setCodError(EAMErrorMapping.EAM_9999.getEamCode());
				output.setDescrError(e.getMessage());
				output.setTimeStampError(new Date());
			} finally {
				long endTime = System.currentTimeMillis();
				log.debug("Saved Dss Workerd item for step {}. |{}|", step, endTime - startTime);

				MDC.clear();
			}
			
			return output;
		}, threadPool.getExecutorService());
	}
	

    private X4DssPolicyRunner saveDssPolicyRunner(X4Policy policy, EventDTO request, int numeroAsset) 
	        throws DataLayerException 
    {
	    String correlationKey = request.getKafkaKey();
	    String policyStr = policy.getX4PolicyId().toString();

	    X4DssPolicyRunner existing = x4DssPolicyRunnerQueryService
	        .findByPolicyAndCorrelation(policyStr, correlationKey);

	    if (existing != null) {
	        return existing;
	    }

	    X4DssPolicyRunner row = new X4DssPolicyRunner();
	    row.setIdMacroAttivita(policy.getIdMacroAttivita());
	    row.setX4PolicyId(policy.getX4PolicyId());
	    row.setIdPolicy(policy.getIdPolicy());
	    row.setCorrelationId(correlationKey);
	    row.setDataElaborazione(new Date());
        row.setNumElaborati(Long.valueOf(numeroAsset));
        row.setTipoEvento(EventType.POLICY.getValue());
        row.setTopic(KafkaUtility.getTopicName(DssKafka.POLICY, brokerEntities.getEndPoints()));
	    row.setHasLd(0L);

	    return x4DssPolicyRunnerQueryService.saveOrUpdateRequiresNew(row);
	}



    private boolean sendTopicKafkaForPeriodic(EventDTO request, List<X4Policy> policy)
	{
		boolean retry = false;
		if (Assorted.isNotEmpty(policy))
		{
			String correlationKey 		= request.getKafkaKey();
			BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(Constants.END_POINT_MANAGER_EVENTS_HANDLER);
			Date currentDate			= new Date();
			
			Assert.notNull(correlationKey, "kafka key is null");
			for (X4Policy row: policy)
			{
				String policyStr = row.getX4PolicyId().toString();
				
				try {
					X4DssWorkedHistory rowHistory = x4DssWorkedHistoryQueryService.getDssWorkedHistoryByCorrelationAndPolicy(correlationKey, policyStr);
					
					if (Assorted.isNull(rowHistory))
					{
					    cacheService.evictCache(Constants.CACHE_POLICY, String.valueOf(row.getX4PolicyId()), CatalogTipologyType.POLICY);
					    
						long time = (new Date()).getTime();
						EventDTO newEvent = new EventDTO();
						newEvent.setEvent(request.getEvent());
						newEvent.setPolicy(row.getX4PolicyId().toString());
						newEvent.setTipology(request.getTipology());
						newEvent.setType(request.getType());
						newEvent.setKafkaKey(row.getX4PolicyId().toString() + Constants.SEP4 + time);
						kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(newEvent));
						
						rowHistory = new X4DssWorkedHistory();
						rowHistory.setCreatedDate(new java.sql.Date(currentDate.getTime()));
						rowHistory.setPolicyId(policyStr);
						rowHistory.setCorrelationId(correlationKey);
						rowHistory.setStatus(Constants.CLOSED_STATUS);
						rowHistory.setTotalRelatedMessage(policy.size());
						rowHistory.setHasLd(0);
						x4DssWorkedHistoryQueryService.saveOrUpdateRequiresNew(rowHistory);
					}
					
				} catch(Exception e) {
					retry = true;
					log.error(e.getMessage(), e);
					
					try {
	            		X4DssPolicyError rowPolicyError = new X4DssPolicyError();
	            		rowPolicyError.setPolicyId(row.getX4PolicyId());
	            		rowPolicyError.setErrorDate(new java.sql.Date(currentDate.getTime()));
	            		rowPolicyError.setErrorDescription(e.getMessage());
	            		x4DssPolicyErrorQueryService.saveDssPolicyError(rowPolicyError);
					} catch(Exception p) {
						log.error(p.getMessage(), p);
					}
				}
			}
		}
		
		return retry;
	}
	
	private boolean isPolicyAggregateStazione(X4Policy policy)
    {				
	    boolean esito = 
	    		// check if is an aggregate policy
	    		Constants.IS_POLICY_AGGREGATE.equals(policy.getPolicyAggr()) 
	    		// check level of aggregazione
	    		&& (policy.getLivAggr() != null && (2 == policy.getLivAggr() || 3 == policy.getLivAggr())
	    		// check if policy has "stazione" as target
	    		&& policyAggregateKPService.checkForStazione(policy));
	    log.debug("end aggregata per stazione ==> {}", esito);
	    return esito;
    }
	
		
	private Integer getCount(String assetnum, Map<String, Integer> map) 
	{
		return map != null 
				&& map.containsKey(assetnum)
					? map.get(assetnum)
					: null;
	}

	
	private Shoot<WorkedPolicyRequestEvent> sendTopicKafkaToWorker(String scheduleUUID, EventDTO request, X4Policy policy, List<String> classStructureId) 
			throws DataLayerException, EAMException
	{
		long startTime = System.currentTimeMillis();
		log.debug("Start sendTopicKafkaToWorker |{}|", startTime);
		Shoot<WorkedPolicyRequestEvent> result = new Shoot<>();
		AttributesDTO attributes 	= createAttributesForPolicy(request, policy);
		boolean aggregateStazione   = isPolicyAggregateStazione(policy);
		
        BrokerEntityInfo topicKafka = PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy())
                                            ? brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY)
                                            : brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);
        
        result.setEntity(topicKafka);
		try (StreamHolder<IAssetMinimal> assetStreamHolder = findAsset(request, policy, classStructureId)) 
		{
		    X4DssPolicyRunner runner        = saveDssPolicyRunner(policy, request, assetStreamHolder.getCount());
		    StringBuilder assetList 		= new StringBuilder(BUFFER_BUILDER);
			boolean first 					= true;
			int step						= 0;
			Iterator<IAssetMinimal> iterator = assetStreamHolder.getStream().iterator();
			// to use for stazioni
			Map<String, Pair<String, Integer>> mapAssetParent		= new HashMap<>();
			List<CompletableFuture<ExecuteDtoResult>> combinedFutures = new ArrayList<>();
			while (iterator.hasNext()) 
			{
				IAssetMinimal x =  iterator.next();
				
				if (first) 
				{
					assetList.append(x.getAssetNum());
					mapAssetParent.put(x.getAssetNum(), Pair.makePair(x.getParent(), getCount(x.getParent(), assetStreamHolder.getMapParentCountChildren())));
					first = false;
				} else if ((assetList.length() + x.getAssetNum().length() + 1) > BUFFER_BUILDER) {
					combinedFutures.add(save(scheduleUUID, step++, request, policy, attributes, assetList.toString(), mapAssetParent,
							aggregateStazione 
								? getCount(x.getParent(), assetStreamHolder.getMapParentCountChildren())
								: assetStreamHolder.getCount(), runner));
					mapAssetParent = new HashMap<>();
					mapAssetParent.put(x.getAssetNum(), Pair.makePair(x.getParent(), getCount(x.getParent(), assetStreamHolder.getMapParentCountChildren())));
					assetList 	 = new StringBuilder(BUFFER_BUILDER);
					assetList.append(x.getAssetNum());
				} else {
					assetList.append(Constants.SEP2).append(x.getAssetNum());
					mapAssetParent.put(x.getAssetNum(), Pair.makePair(x.getParent(), getCount(x.getParent(), assetStreamHolder.getMapParentCountChildren())));
				}
			}
			
			if (assetList.length() > 0 && Assorted.isNotEmpty(mapAssetParent))
				combinedFutures.add(save(scheduleUUID, step++, request, policy, attributes, assetList.toString(), mapAssetParent, assetStreamHolder.getCount(), runner));
			
			List<ExecuteDtoResult> combinedResponses = combinedFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
			List<ExecuteDtoResult> errorList 		 = combinedResponses.stream().filter(x -> x.getEsito().equals(Esito.KO)).collect(Collectors.toList());
        	
        	if (Assorted.isNotEmpty(errorList))
        	{
        		for (ExecuteDtoResult x: errorList)
        		{
            		X4DssPolicyError row = new X4DssPolicyError();
            		row.setPolicyId(policy.getX4PolicyId());
            		row.setErrorDate(Assorted.notNull(x.getTimeStampError()) ? new java.sql.Date(x.getTimeStampError().getTime()) : null);
            		row.setErrorDescription(x.getDescrError());
            		x4DssPolicyErrorQueryService.saveDssPolicyError(row);
        		}
        		
        		ExecuteDtoResult firstError = errorList.get(0);
        		throw new EAMRetryException(firstError.getCodError(), firstError.getDescrError());
        	
        	} else {
        		x4DssWorkedHistoryQueryService.updateProgressiveMessage(runner.getX4DssPolicyRunnerId(), 1);
        	
                combinedResponses.stream()
                                 .filter(x -> x.getEsito().equals(Esito.OK))
                                 .forEach(x -> result.getItems().addAll(x.getListShoot()));
        	}
		}
		
		long endTime = System.currentTimeMillis();
		log.debug("End sendTopicKafkaToWorker |{}|", endTime - startTime);
		
		return result;
	}
	
	
	private StreamHolder<IAssetMinimal> findAsset(EventDTO request, X4Policy policy, List<String> classStructureId) 
	        throws DataLayerException
	{
	    Map<String, String> attributes = request.getAttributes();
	    String troncoNum               = null;
	    
	    if (Assorted.isNotEmpty(attributes))
	    {
	        troncoNum  = attributes.get(PolicyAttributes.TRONCONUM.name());
	    }
	    
	    if (Assorted.isNotEmpty(troncoNum))
	        return assetDiscoverer.getAssetsTronco(troncoNum, classStructureId);
	    else if (isPolicyAggregateStazione(policy))
	        return assetDiscoverer.getStazioniAssetStream(policy, classStructureId);
	    else
	        return assetDiscoverer.getDefaultAssetStream(policy, classStructureId);
	}
}
