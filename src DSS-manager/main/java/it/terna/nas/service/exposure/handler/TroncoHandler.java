package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.SEP3;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.ElaborationTroncoSupport;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.dal.service.repository.X4DssOrdersRepository;
import it.terna.nas.service.dal.service.repository.X4DssWorkedHistoryRepository;
import it.terna.nas.service.executor.ExecutorTroncoConfigurator;
import it.terna.nas.service.material.dto.worker.MessageStreamCounter;
import it.terna.nas.service.material.utility.Catalog.PolicyTimeUnit;
import it.terna.nas.service.material.utility.EamEtlUtils;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TroncoHandler extends AbstractHandler
{
    private static final int PARTITION_SIZE_ELAB_TRONCHI = 50;
    
    @Autowired
    private ElaborationTroncoSupport elabTronco;
    
    @Autowired
    private ExecutorTroncoConfigurator threadPool;

    @Autowired
    private PolicyQueryService policyQueryService;   
    
    @Autowired
    private X4DssOrdersRepository x4DssOrdersRepository;

    @Autowired
    private X4DssPolicyRunnerQueryService x4DssPolicyRunnerQueryService;
    
    @Autowired
    private X4DssWorkedHistoryRepository x4DssWorkedHistoryRepository;

    private CompletableFuture<Boolean> elabTroncoNum(String scheduleUUID, int step, X4DssPolicyRunner runner, 
                                                  MessageStreamCounter request, X4Policy x4policy, 
                                                  Integer recoveryCount, List<Long> x3troncoIds) 
    {
        return CompletableFuture.supplyAsync(() -> {
            
            MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID + SEP3 + step);
            boolean esitoElab = Boolean.FALSE;
            try {
                esitoElab = elabTronco.elabTroncoNum(runner, request, x4policy, recoveryCount, x3troncoIds);        
            } catch (Exception e) {
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
        log.debug("call createOrdiniWithTronco({}, {}) |{}|", request.getKey(), request.getPolicy(), startTime);
        try (Stream<Long> x3Tronchi = x4DssOrdersRepository.findTronchiByCorrelation(request.getKey())) 
        {
            String policyIdStr           = Long.toString(request.getPolicy());
            X4Policy x4policy            = policyQueryService.getPolicy(policyIdStr);
            Integer recoveryCount        = x4DssWorkedHistoryRepository.findRecoveryCountByCorrelationId(request.getKey(), policyIdStr);
            X4DssPolicyRunner runner     = x4DssPolicyRunnerQueryService.findRunnerByCorrelationId(KafkaUtility.getCorrelationId(request.getKey()));
            
            List<Long> listTronchi       = new ArrayList<>();
            Iterator<Long> iter          = x3Tronchi.iterator();
            while(iter.hasNext())
                listTronchi.add(iter.next());
                
            List<List<Long>> partitions = EamEtlUtils.partition(listTronchi, getPartitionBlock(x4policy));
            if (isNotEmpty(partitions))
            {
                List<CompletableFuture<Boolean>> combinedFutures = new ArrayList<>();
                String scheduleUUID = MDC.get(Common.MDC_TRANSACTION_ID);
                int i               = 0;
                for(List<Long> partition: partitions)
                {
                    combinedFutures.add(elabTroncoNum(scheduleUUID, i++, runner, request, x4policy, recoveryCount, partition));
                }
                
                List<Boolean> combinedResponses = combinedFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
                
                if (log.isInfoEnabled())
                {
                    long count = combinedResponses.stream().filter(x -> Boolean.FALSE.equals(x)).count();
                    if (count > 0)
                        log.error("Number block with errors {}.", count);
                }
            }
        }    
        
        long endTime = System.currentTimeMillis();
        log.debug("end createOrdiniWithTronco({}, {}) |{}|", request.getKey(), request.getPolicy(), endTime - startTime);
    }
    
    
    
    private int getPartitionBlock(X4Policy policy)
    {
        ChronoUnit timeUnit     = PolicyTimeUnit.fromCode(policy.getX4RifTemporale()).getTemporalUnit();
        Integer frequency       = notNull(policy.getFrequenza()) ? policy.getFrequenza() : (notNull(policy.getDifferibilita()) ? policy.getDifferibilita() : 1);
        long result             = 1;
        double operando         = frequency;
        
        switch (timeUnit) 
        {
            case YEARS  -> result = PARTITION_SIZE_ELAB_TRONCHI;
            case MONTHS -> result = Math.round(PARTITION_SIZE_ELAB_TRONCHI * (operando / 12));
            case WEEKS  -> result = Math.round(PARTITION_SIZE_ELAB_TRONCHI * (operando / 52));
            case DAYS   -> result = 1L;
            default     -> result = 1L; 
        }
        
        if (result < 1)
            result = frequency;
        
        log.info("Calculated partition size for policy {} is {}. TimeUnit: {}, Frequency: {}", policy.getIdPolicy(), result, timeUnit, frequency);
        
        return Math.toIntExact(result);
    }
}
