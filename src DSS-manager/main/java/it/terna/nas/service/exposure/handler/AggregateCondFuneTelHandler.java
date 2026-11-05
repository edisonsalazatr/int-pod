package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.nas.service.material.utility.Constants.CLASS_CONDUTTORI;
import static it.terna.nas.service.material.utility.Constants.CLASS_TELAIO;
import static it.terna.nas.service.material.utility.Constants.END_POINT_EVALUATE_WORKER_POLICY;
import static it.terna.nas.service.material.utility.Constants.SEP3;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.entity.eam.X4PolicyAggregateEleg;
import it.terna.nas.service.dal.service.ElaborationCondFuneTelSupport;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.dal.service.repository.X4DssAggregateRepository;
import it.terna.nas.service.dal.service.repository.X4DssWorkedHistoryRepository;
import it.terna.nas.service.dal.service.repository.X4PolicyAggregateElegRepository;
import it.terna.nas.service.executor.ExecutorTroncoConfigurator;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.Shoot;
import it.terna.nas.service.material.dto.worker.MessageStreamCounter;
import it.terna.nas.service.material.utility.Catalog.EleggibilitaType;
import it.terna.nas.service.material.utility.EamEtlUtils;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AggregateCondFuneTelHandler extends AbstractHandler
{
    private static final int PARTITION_SIZE_WORKORDER    = 150;
    
    @Autowired
    private ElaborationCondFuneTelSupport  elabCondFuneTel;
    
    @Autowired
    private PolicyQueryService policyQueryService;
    
    @Autowired
    private ExecutorTroncoConfigurator threadPool;
    
    @Autowired
    private X4DssAggregateRepository x4DssAggregateRepository;
   
    @Autowired
    private X4DssPolicyRunnerQueryService x4DssPolicyRunnerQueryService;

    @Autowired
    private X4DssWorkedHistoryRepository x4DssWorkedHistoryRepository;
    
    @Autowired
    private X4PolicyAggregateElegRepository x4PolicyAggregateElegRepository;
    
    
    private CompletableFuture<Boolean> elabCondFune(String scheduleUUID, int step, X4DssPolicyRunner runner, 
                                                    MessageStreamCounter request, X4Policy policy, Integer recoveryCount, 
                                                    List<String> linee, double sogliaElegLine, double kEleg, Long timeStampKeyLineeDoppie)
    {
        return CompletableFuture.supplyAsync(() -> {
            
            MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID + SEP3 + step);
            boolean esitoElab = Boolean.TRUE;
            try {
                BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);
                Shoot<WorkedPolicyRequestEvent> events = new Shoot<>(topicKafka);
                esitoElab = elabCondFuneTel.elabCondFune(runner, request, policy, recoveryCount, linee, sogliaElegLine, kEleg, timeStampKeyLineeDoppie, events);
                shootMessages(events);
            } catch (Exception e) {
                esitoElab = Boolean.FALSE;
                log.error(e.getMessage(), e);
            } finally {
                MDC.clear();
            }
            
            return esitoElab;
            
        }, threadPool.getExecutorService());
    }
    
    
    private CompletableFuture<Boolean> elabTelaio(String scheduleUUID, int step, X4DssPolicyRunner runner, 
                                                  MessageStreamCounter request, X4Policy policy, Integer recoveryCount, 
                                                  List<String> linee, double sogliaSost, double kEleg)
    {
        return CompletableFuture.supplyAsync(() -> {
            
            MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID + SEP3 + step);
            boolean esitoElab = Boolean.TRUE;
            try {
                BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);
                Shoot<WorkedPolicyRequestEvent> events = new Shoot<>(topicKafka);
                esitoElab = elabCondFuneTel.elabTelaio(runner, request, policy, recoveryCount, linee, sogliaSost, kEleg, events);
                shootMessages(events);
            } catch (Exception e) {
                esitoElab = Boolean.FALSE;
                log.error(e.getMessage(), e);
            } finally {
                MDC.clear();
            }
            
            return esitoElab;
            
        }, threadPool.getExecutorService());
    }
    
    
    @Transactional(readOnly = true)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void handleEvents(MessageStreamCounter request) 
            throws DataLayerException
    {
        //Generatione ordini aggregati e invio al worker
        long startTime = System.currentTimeMillis();
        log.debug("call handleEvents({}, {}) |{}|", request.getKey(), request.getPolicy(), startTime);
        String policyIdStr          = Long.toString(request.getPolicy());
        X4Policy policy             = policyQueryService.getPolicy(policyIdStr);
        X4DssPolicyRunner runner    = x4DssPolicyRunnerQueryService.findRunnerByCorrelationId(KafkaUtility.getCorrelationId(request.getKey()));
        Integer recoveryCount       = x4DssWorkedHistoryRepository.findRecoveryCountByCorrelationId(request.getKey(), policyIdStr);
        
        if (CLASS_TELAIO.equals(policy.getTipologiaAsset()))
            manageTelaio(request, policy, runner, recoveryCount);
        else
            manageCondFune(request, policy, runner, recoveryCount);

        long endTime = System.currentTimeMillis();
        log.info("end handleEvents({}, {}) |{}|", request.getKey(), request.getPolicy(), endTime - startTime);
    }


    private void manageCondFune(MessageStreamCounter request, X4Policy policy, X4DssPolicyRunner runner, Integer recoveryCount) 
            throws DataLayerException
    {
        List<List<String>> lineePartitions = EamEtlUtils.partition(x4DssAggregateRepository.findLineaAggregate(request.getKey()), PARTITION_SIZE_WORKORDER);
        
        if (isNotEmpty(lineePartitions))
        {
            final X4PolicyAggregateEleg sogliaAgg = CLASS_CONDUTTORI.endsWith(policy.getTipologiaAsset()) ? x4PolicyAggregateElegRepository.findByParameter(EleggibilitaType.S_ELEG_LINEA_COND.getCode()) : x4PolicyAggregateElegRepository.findByParameter(EleggibilitaType.S_ELEG_LINEA_FDG.getCode());
            final double sogliaElegLine = sogliaAgg.getValue();

            final X4PolicyAggregateEleg kAggElig = CLASS_CONDUTTORI.endsWith(policy.getTipologiaAsset()) ? x4PolicyAggregateElegRepository.findByParameter(EleggibilitaType.K_ELEG_COND.getCode()) : x4PolicyAggregateElegRepository.findByParameter(EleggibilitaType.K_ELEG_FDG.getCode());
            final double kEleg = kAggElig.getValue();

            List<CompletableFuture<Boolean>> combinedFutures = new ArrayList<>();
            String scheduleUUID = MDC.get(Common.MDC_TRANSACTION_ID);
			Long timeStampKeyLineeDoppie = System.currentTimeMillis();
            int i               = 0;
            for(List<String> partition: lineePartitions)
            {
                combinedFutures.add(elabCondFune(scheduleUUID, i++, runner, request, policy, recoveryCount, partition, sogliaElegLine, kEleg, timeStampKeyLineeDoppie));
            }
            
            List<Boolean> combinedResponses = combinedFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
            
            if (log.isInfoEnabled())
            {
                long count = combinedResponses.stream().filter(x -> Boolean.FALSE.equals(x)).count();
                log.info("Number block with errors {}.", count);
            }
        }
    }


    private void manageTelaio(MessageStreamCounter request, X4Policy policy, X4DssPolicyRunner runner, Integer recoveryCount) 
            throws DataLayerException
    {
        List<List<String>> lineePartitions = EamEtlUtils.partition(x4DssAggregateRepository.findLineaAggregate(request.getKey()), PARTITION_SIZE_WORKORDER);
        if (isNotEmpty(lineePartitions))
        {
            X4PolicyAggregateEleg sogliaAgg = x4PolicyAggregateElegRepository.findByParameter(EleggibilitaType.S_ELEG_LINEA_STRUTTURA.getCode());
            final double sogliaSost         = sogliaAgg.getValue();
          
            final X4PolicyAggregateEleg kAggElig = x4PolicyAggregateElegRepository.findByParameter(EleggibilitaType.K_ELEG_STRUTTURA.getCode());
            final double kEleg = kAggElig.getValue();
            
            List<CompletableFuture<Boolean>> combinedFutures = new ArrayList<>();
            String scheduleUUID = MDC.get(Common.MDC_TRANSACTION_ID);
            int i               = 0;
            for(List<String> partition: lineePartitions)
            {
                combinedFutures.add(elabTelaio(scheduleUUID, i++, runner, request, policy, recoveryCount, partition, sogliaSost, kEleg));
            }
            
            List<Boolean> combinedResponses = combinedFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
            
            if (log.isInfoEnabled())
            {
                long count = combinedResponses.stream().filter(x -> Boolean.FALSE.equals(x)).count();
                log.info("Number block with errors {}.", count);
            }
        }
    }
}