package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY;
import static it.terna.nas.service.material.utility.Constants.END_POINT_EVALUATE_WORKER_POLICY;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.dal.service.WorkOrderQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.dal.service.X4DssWorkedHistoryQueryService;
import it.terna.nas.service.dal.service.repository.X4DssOrdersRepository;
import it.terna.nas.service.dal.service.repository.X4DssWorkedHistoryRepository;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.Shoot;
import it.terna.nas.service.material.dto.worker.MessageStreamCounter;
import it.terna.nas.service.material.dto.worker.Order;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Catalog.TrainatoTrainanteType;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.KafkaUtility;
import it.terna.nas.service.material.utility.OrderUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrainatoTrainanteHandler extends AbstractHandler
{
    @Autowired
    private BrokerEntityConfiguration brokerEntities;
    
    @Autowired
    private PolicyQueryService policyQueryService;   
    
    @Autowired
    private WorkOrderQueryService workOrderQueryService;

    @Autowired
    private X4DssOrdersRepository x4DssOrdersRepository;

    @Autowired
    private X4DssPolicyRunnerQueryService x4DssPolicyRunnerQueryService;
    
    @Autowired
    private X4DssWorkedHistoryQueryService x4DssWorkedHistoryQueryService;
    
    @Autowired
    private X4DssWorkedHistoryRepository x4DssWorkedHistoryRepository;
    
    private void createOrdiniTesatura(String key, X4Policy x4policy, Map<String, List<Order>> mapAssetOrders, 
                                      List<String> tesatura, X4DssPolicyRunner runner, 
                                      Integer recoveryCount, Shoot<WorkedPolicyRequestEvent> shoot) 
            throws DataLayerException
    {
        log.debug("call createOrdiniTesatura({}, {})", key, x4policy.getX4PolicyId());
        List<Order> ordersToSendWorker  = new ArrayList<>();
        List<Order> listOrdersFirst     = findTheFirst(tesatura, mapAssetOrders);
        String assetFirst               = isNotEmpty(listOrdersFirst) ? listOrdersFirst.get(0).getAssetnum() : null;
        
        if (isEmpty(assetFirst)) //Protezione al codice, non dovrebbe mai accadere
        {    
            log.warn("No orders for tesatura {}", tesatura);
            return;
        }
            
        for(String asset: tesatura)
        {
            List<Order> listOrder = mapAssetOrders.get(asset);
            if (isEmpty(listOrder)) 
            {
                listOrder = new ArrayList<>();
                for (Order x: listOrdersFirst)
                {
                    Order newOrder = new Order(x);
                    newOrder.setAssetnum(asset);
                    newOrder.setWonum(Constants.PREFIX_DSS_WONUM + workOrderQueryService.getSequenceForAggregate().get(0).getNextval());
                    listOrder.add(newOrder);
                }
                
                listOrder.forEach(x -> {x.setTesatura(null); x.setX4Trainante(TrainatoTrainanteType.TRAINATO.getValue());});
            } else {
                listOrder.forEach(x -> {x.setTesatura(null); x.setX4Trainante(TrainatoTrainanteType.TRAINANTE.getValue());});
            }
            
            ordersToSendWorker.addAll(listOrder);
        }
        
        if (isNotEmpty(ordersToSendWorker)) 
        {
            Collections.sort(ordersToSendWorker, (a, b) -> a.getAssetnum().compareTo(b.getAssetnum()));
            shoot.getItems().add(save(key, x4policy, ordersToSendWorker, runner, recoveryCount));
        }
    }
    
    
    private List<Order> findTheFirst(List<String> tesatura, Map<String, List<Order>> mapAssetOrders)
    {
        List<Order> result = null;
        String assetResult = null;
        for(String asset: tesatura)
        {
            List<Order> listOrders = mapAssetOrders.get(asset);
            
            if (isNotEmpty(listOrders))
            {
                assetResult = asset;
                result      = listOrders;
                break;
            }
        }
        
        log.debug("For tesatura {} the first is ", tesatura, assetResult);
        return result;
    }

    
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void handleEvents(MessageStreamCounter request) 
            throws DataLayerException
    {
        Shoot<WorkedPolicyRequestEvent> result = transactionBlocks.requiresNew(() -> {
            try {
                return generateEvents(request);
            } catch (DataLayerException e) {
                throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e);
            }
        });
        
        shootMessages(result);
    }
    
    
    private Shoot<WorkedPolicyRequestEvent> generateEvents(MessageStreamCounter request) 
            throws DataLayerException
        {
            String policyIdStr        = Long.toString(request.getPolicy());
            X4Policy x4policy         = policyQueryService.getPolicy(policyIdStr);
        
        Shoot<WorkedPolicyRequestEvent> result  = new Shoot<>();
        BrokerEntityInfo topicKafka             = PolicyType.PERIODICA.getCode().equals(x4policy.getTipoPolicy())
                                                    ? brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY)
                                                    : brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);
        result.setEntity(topicKafka);
        
        try (Stream<String> trattaStream = x4DssOrdersRepository.findTrattaByCorrelation(request.getKey())) 
        {
            X4DssPolicyRunner runner  = x4DssPolicyRunnerQueryService.findRunnerByCorrelationId(KafkaUtility.getCorrelationId(request.getKey()));
            Integer recoveryCount     = x4DssWorkedHistoryRepository.findRecoveryCountByCorrelationId(request.getKey(), policyIdStr);
            Iterator<String> iterator = trattaStream.iterator();
            while (notNull(runner) && iterator.hasNext())
            {
                String tronco           = iterator.next();
                List<String> tesatura   = Arrays.asList(tronco.split(Constants.SEP1));
                List<Order> allOrders   = OrderUtility.convertFromX4DssOrders(x4DssOrdersRepository.findOrdersByCorrelationAndTratta(request.getKey(), tronco));
                
                Map<String, List<Order>> mapAssetOrders = allOrders.stream()
                                                                   .filter(x -> isNotEmpty(x.getAssetnum()))
                                                                   .collect(Collectors.groupingBy(Order::getAssetnum));
                
                if (log.isDebugEnabled())
                {
                    log.debug("Tesatura: {}", tesatura);
                    log.debug("Numero orders indivuati: {}", mapAssetOrders.size());
                }
                
                createOrdiniTesatura(request.getKey(), x4policy, mapAssetOrders, tesatura, runner, recoveryCount, result);
            }
        }
    
        return result;
    }
    
    
    private WorkedPolicyRequestEvent save(String correlationKey, X4Policy x4policy, List<Order> ordersToSendWorker, X4DssPolicyRunner runner, Integer recoveryCount) 
            throws DataLayerException
    {
        String assetFirst = ordersToSendWorker.get(0).getAssetnum();
        String policyStr  = x4policy.getX4PolicyId().toString();
        X4DssWorkedHistory rowHistory   = x4DssWorkedHistoryQueryService.getDssWorkedHistoryByCorrelationAndPolicyAndAsset(correlationKey, policyStr, assetFirst);
        if (Assorted.isNull(rowHistory))
        {
            rowHistory = new X4DssWorkedHistory();
            rowHistory.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));
            rowHistory.setPolicyId(policyStr);
            rowHistory.setCorrelationId(correlationKey);
            rowHistory.setStatus(Constants.OPEN_STATUS);
            rowHistory.setTotalRelatedMessage(1);
            rowHistory.setProgressiveRelatedMessage(1);
            rowHistory.setAssetNum(assetFirst);
            rowHistory.setHasLd(0);
            rowHistory.setX4DssPolicyRunnerId(runner.getX4DssPolicyRunnerId());
            rowHistory.setRecoveryCount(recoveryCount);
            rowHistory = x4DssWorkedHistoryQueryService.saveOrUpdateRequiresNew(rowHistory);
        } else {
            rowHistory.setStatus(Constants.OPEN_STATUS);
            rowHistory.setCode(null);
            rowHistory.setDescription(null);
            rowHistory.setErrorFlag(null);
            rowHistory.setShortDescription(null);
            rowHistory = x4DssWorkedHistoryQueryService.saveOrUpdateRequiresNew(rowHistory);
        }
            
        
        WorkedPolicyRequestEvent newEvent = new WorkedPolicyRequestEvent();
        newEvent.setEvent(EventActionType.TRAINATO_TRAINANTE_SAVE.getValue());
        newEvent.setPolicy(x4policy.getX4PolicyId());
        newEvent.setCorrelationId(correlationKey);
        newEvent.setDssWorkerId(rowHistory.getIdDssWorked().toString());
        newEvent.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(assetFirst));
        newEvent.setAsset(assetFirst);
        newEvent.setOrders(ordersToSendWorker);
        newEvent.setNumberMessagePolicy(1L);
        newEvent.setTipology(x4policy.getTipoPolicy());
        newEvent.setType(EventType.POLICY.getValue());
        return newEvent;
    }
}
