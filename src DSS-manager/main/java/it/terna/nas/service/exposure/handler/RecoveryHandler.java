package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY;
import static it.terna.nas.service.material.utility.Constants.END_POINT_EVALUATE_WORKER_POLICY;
import static it.terna.nas.service.material.utility.Constants.POLICY_STATUS_ATTIVA;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.AX4Policy;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyError;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.ClassificationQueryService;
import it.terna.nas.service.dal.service.PolicyAggregateKPService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyErrorQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.dal.service.X4DssWorkedHistoryQueryService;
import it.terna.nas.service.dal.service.repository.X4DssWorkedHistoryRepository;
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
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.Esito;
import it.terna.nas.service.material.utility.Catalog.EventAttributes;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.Catalog.PolicyAttributes;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RecoveryHandler extends AbstractHandler
{
    private static final int                      BUFFER_BUILDER = 2500;

    @Autowired
    private BrokerEntityConfiguration             brokerEntities;

    @Autowired
    private ClassificationQueryService            classificationService;
    
    @Autowired
    private PolicyAggregateKPService              policyAggregateKPService;

    @Autowired
    private PolicyQueryService                    policyQueryService;

    @Autowired
    private ExecutorSendKafkaAssetNumConfigurator threadPool;

    @Autowired
    private X4DssPolicyErrorQueryService          x4DssPolicyErrorQueryService;

    @Autowired
    private X4DssPolicyRunnerQueryService         x4DssPolicyRunnerQueryService;

    @Autowired
    private X4DssWorkedHistoryQueryService        x4DssWorkedHistoryQueryService;
    
    @Autowired
    private X4DssWorkedHistoryRepository          x4DssWorkedHistoryRepository;
    
    private AttributesDTO createAttributesForPolicy(EventDTO request, X4Policy policy) 
            throws DataLayerException
    {
        AttributesDTO result = null;
        
        if (Assorted.isEmpty(request.getAttributes()))
            return result;
        
        AX4Policy policyHistory = policyQueryService.getPolicyHistory(policy.getX4PolicyId().toString(), policy.getVersion());
        if (Assorted.notNull(policyHistory))
        {
            result                      = new AttributesDTO();
            Boolean changeFrequenza     = Boolean.parseBoolean(request.getAttributes().get(PolicyAttributes.FREQUENCY.name()));
            Boolean changeDeferrability = Boolean.parseBoolean(request.getAttributes().get(PolicyAttributes.DEFERRABILITY.name()));
            Boolean changeMaxAdvance    = Boolean.parseBoolean(request.getAttributes().get(PolicyAttributes.MAX_ADVANCE.name()));
            Boolean changeMaxSnooze     = Boolean.parseBoolean(request.getAttributes().get(PolicyAttributes.MAX_SNOOZE.name()));
            Boolean changeRifTemporale  = Boolean.parseBoolean(request.getAttributes().get(PolicyAttributes.RIF_TEMPORALE.name()));

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
        
        return result;
    }
    
    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void handleEvent(EventDTO request) 
            throws EAMException
    {
        Shoot<WorkedPolicyRequestEvent> results = transactionBlocks.requiresNew(() -> {
            return generateEvents(request);
        });
        
        shootMessages(results);
    }
    
    
    private Shoot<WorkedPolicyRequestEvent> generateEvents(EventDTO request) 
            throws EAMException
    {
        Shoot<WorkedPolicyRequestEvent> result = new Shoot<>();
        try {
            Map<String, String> attributes  = request.getAttributes();
            Long runnerId                   = Long.parseLong(attributes.get(EventAttributes.RUNNER.getValue()));
            X4DssPolicyRunner runner        = x4DssPolicyRunnerQueryService.findRunnerById(runnerId);
            
            if (isNull(runner))
            {
                log.error("Impossible to find runner by id {}", runnerId);
                return null;
            }    
            
            X4Policy policyLoad = policyQueryService.getPolicy(runner.getX4PolicyId().toString());
            if (isNull(policyLoad) || (!POLICY_STATUS_ATTIVA.equals(policyLoad.getStatus())))
            {
                log.info("Policy {} has status {} ", runner.getX4PolicyId(), notNull(policyLoad) ? policyLoad.getStatus() : null);
                return null;
            }
            
            Integer maxRecovery = x4DssWorkedHistoryRepository.findMaxRecovery(runnerId);
            log.debug("maxRecovery: {} for runner: {} ", maxRecovery, runnerId);
            
            Integer countRecordToRecovery = null;
            List<String> listRecovery  = null;
                    
            if (isPolicyAggregateStazione(policyLoad))
            {
                listRecovery            = x4DssWorkedHistoryRepository.findAllRecordToRecoveryStation(runnerId, maxRecovery);
                countRecordToRecovery   = x4DssWorkedHistoryRepository.countAllRecordToRecoveryStation(runnerId, maxRecovery);
            } else {
                listRecovery            = x4DssWorkedHistoryRepository.findAllRecordToRecovery(runnerId, maxRecovery, runner.getX4PolicyId());
                countRecordToRecovery   = x4DssWorkedHistoryRepository.countAllRecordToRecovery(runnerId, maxRecovery, runner.getX4PolicyId());
            }
            
            BrokerEntityInfo topicKafka = PolicyType.PERIODICA.getCode().equals(policyLoad.getTipoPolicy())
                    ? brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY)
                    : brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);
            
            result.setEntity(topicKafka);
            if (isNotEmpty(listRecovery))
            {
                Integer newMaxRecovery                  = maxRecovery + 1;
                AttributesDTO attributesDTO             = createAttributesForPolicy(request, policyLoad);
                boolean first                           = true;
                StringBuilder assetList                 = new StringBuilder(BUFFER_BUILDER);
                String scheduleUUID                     = MDC.get(Common.MDC_TRANSACTION_ID);
                int step                                = 0;
                List<CompletableFuture<ExecuteDtoResult>> combinedFutures = new ArrayList<>();
                
                request.setKafkaKey(runner.getCorrelationId());
                if (Assorted.notNull(policyLoad.getLogicaTronco()) && policyLoad.getLogicaTronco() == 1)
                    request.setEvent(Constants.CLASS_TRONCO_ISPEZIONE);
                else {
                    List<ClassStructureTipology> typeJpnumList  = classificationService.getClassStructureTipologyByMacroAttivita(policyLoad.getIdMacroAttivita());
                    ClassStructureTipology classSostit          = typeJpnumList.stream()
                                                                               .filter(x -> Constants.CLASS_SOSTITUZIONE.equals(x.getClassificationId()))
                                                                               .findFirst()
                                                                               .orElse(null);
                
                    if (Assorted.notNull(classSostit))
                        request.setEvent(classSostit.getClassificationId());
                }
                
                for (String assetNum: listRecovery)
                {
                    if (first) 
                    {
                        assetList.append(assetNum);
                        first = false;
                    } else if ((assetList.length() + assetNum.length() + 1) > BUFFER_BUILDER) {
                        combinedFutures.add(save(scheduleUUID, step++, request, policyLoad, attributesDTO, assetList.toString(), countRecordToRecovery, runner, newMaxRecovery));
                        assetList    = new StringBuilder(BUFFER_BUILDER);
                        assetList.append(assetNum);
                    } else {
                        assetList.append(Constants.SEP2).append(assetNum);
                    }
                }
            
                if (assetList.length() > 0)
                    combinedFutures.add(save(scheduleUUID, step++, request, policyLoad, attributesDTO, assetList.toString(), countRecordToRecovery, runner, newMaxRecovery));
            
                List<ExecuteDtoResult> combinedResponses = combinedFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
                List<ExecuteDtoResult> errorList         = combinedResponses.stream().filter(x -> x.getEsito().equals(Esito.KO)).collect(Collectors.toList());
            
                if (Assorted.isNotEmpty(errorList))
                {
                    for (ExecuteDtoResult x: errorList)
                    {
                        X4DssPolicyError row = new X4DssPolicyError();
                        row.setPolicyId(policyLoad.getX4PolicyId());
                        row.setErrorDate(Assorted.notNull(x.getTimeStampError()) ? new java.sql.Date(x.getTimeStampError().getTime()) : null);
                        row.setErrorDescription(x.getDescrError());
                        x4DssPolicyErrorQueryService.saveDssPolicyError(row);
                    }
                    
                    ExecuteDtoResult firstError = errorList.get(0);
                    throw new EAMRetryException(firstError.getCodError(), firstError.getDescrError());
                
                } else {
                    x4DssWorkedHistoryQueryService.updateProgressiveMessage(runner.getX4DssPolicyRunnerId(), newMaxRecovery);
                    combinedResponses.stream()
                                     .filter(x -> x.getEsito().equals(Esito.OK))
                                     .forEach(x -> result.getItems().addAll(x.getListShoot()));
                }
            }
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e);
        }
        
        return result;
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


    private CompletableFuture<ExecuteDtoResult> save(String scheduleUUID, int step, 
                                                     EventDTO request, X4Policy policy, 
                                                     AttributesDTO attributesDTO, String assetList, 
                                                     Integer numAsset, X4DssPolicyRunner runner, 
                                                     int maxRecovery)
    {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID + Constants.SEP3 + step);
            ExecuteDtoResult output = new ExecuteDtoResult();
            output.setListShoot(new ArrayList<>());

            try {
                boolean isAggregate         = Constants.CLASS_SOSTITUZIONE.equals(request.getEvent()) && Constants.IS_POLICY_AGGREGATE.equals(policy.getPolicyAggr()) && Assorted.notNull(policy.getLivAggr());
                String correlation          = request.getKafkaKey();
                Pair<String, String> assetToSend = x4DssWorkedHistoryQueryService.saveDssWorkedHistory(correlation, numAsset, request.getPolicy(), Assorted.checkNullString(null), assetList, runner, maxRecovery);

                if (Assorted.isNotEmpty(assetToSend.getA())) 
                {
                    long startTimeSendKafka         = System.currentTimeMillis();
                    List<String> listAssetToSend    = Arrays.asList(assetToSend.getA().split("\\|"));
                    List<String> listWorkDssToSend  = Arrays.asList(assetToSend.getB().split("\\|"));
                   
                    for (int i = 0; i < listAssetToSend.size(); i++) 
                    {
                        WorkedPolicyRequestEvent event  = new WorkedPolicyRequestEvent();
                        event.setPolicy(policy.getX4PolicyId());
                        event.setEvent(request.getEvent());
                        event.setAttributes(attributesDTO);
                        event.setNumberMessagePolicy(numAsset.longValue());
                        event.setTipology(policy.getTipoPolicy());
                        event.setType(EventType.POLICY.getValue());
                        event.setAggregationLevel(isAggregate ? policy.getLivAggr() : null);
                        String assetNum = listAssetToSend.get(i);
                        event.setCorrelationId(correlation + Constants.SEP4 + maxRecovery);
                        event.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(assetNum) );
                        event.setAsset(assetNum);
                        event.setDssWorkerId(listWorkDssToSend.get(i));
                        output.getListShoot().add(event);
                    }
                    
                    long endTimeSendkafka = System.currentTimeMillis();
                    log.debug("Generate Event in |{}| for step {}. Num shoot: {}", endTimeSendkafka - startTimeSendKafka, step, listAssetToSend.size());
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
}
