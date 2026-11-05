package it.terna.nas.service.dal.service;

import static it.terna.kernel.basement.utility.Assorted.isEmpty;
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
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.SequenceValue;
import it.terna.nas.service.dal.entity.eam.WorkOrder;
import it.terna.nas.service.dal.entity.eam.X3Tronchi;
import it.terna.nas.service.dal.entity.eam.X4DssOrders;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.repository.WorkOrderRepository;
import it.terna.nas.service.dal.service.repository.X3TronchiRepository;
import it.terna.nas.service.dal.service.repository.X4DssOrdersRepository;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.exposure.handler.AbstractHandler;
import it.terna.nas.service.material.dto.Pair;
import it.terna.nas.service.material.dto.Shoot;
import it.terna.nas.service.material.dto.policy.EventDTO;
import it.terna.nas.service.material.dto.worker.MessageStreamCounter;
import it.terna.nas.service.material.dto.worker.Order;
import it.terna.nas.service.material.utility.Catalog.Esito;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.Catalog.PolicyAttributes;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EamEtlUtils;
import it.terna.nas.service.material.utility.OrderUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ElaborationTroncoSupport extends AbstractHandler
{
    private static final int PARTITION_SIZE_WORKORDER    = 150;
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private WorkOrderQueryService workOrderQueryService;
    
    @Autowired
    private WorkOrderRepository workOrderRepository;
    
    @Autowired
    private X3TronchiRepository x3TronchiRepository;
    
    @Autowired
    private X4DssOrdersRepository x4DssOrdersRepository;

    @Autowired
    private X4DssWorkedHistoryQueryService x4DssWorkedHistoryQueryService;
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public Boolean elabTroncoNum(X4DssPolicyRunner runner, 
                                 MessageStreamCounter request, X4Policy x4policy, 
                                 Integer recoveryCount, List<Long> x3troncoIds) 
    {
        Boolean esitoElab = Boolean.TRUE;
        
        try {
            BrokerEntityInfo topicKafka = PolicyType.PERIODICA.getCode().equals(x4policy.getTipoPolicy())
                                            ? brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY)
                                            : brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);

            Shoot<WorkedPolicyRequestEvent> events = new Shoot<>(topicKafka);
            String policyIdStr  = Long.toString(request.getPolicy());
            for (Long x3troncoId: x3troncoIds)
            {
                Optional<X3Tronchi> troncoOpt = x3TronchiRepository.findById(x3troncoId);
                X3Tronchi tronco              = null;
                
                if (troncoOpt.isPresent())
                    tronco = troncoOpt.get();
                else {
                    log.warn("tronco {} not exists", x3troncoId);
                    esitoElab = Boolean.FALSE;
                    continue;
                }

                entityManager.detach(tronco);
                
                String esito        = x4DssWorkedHistoryQueryService.checkTroncoNum(request.getKey(), tronco.getTroncoNum(), policyIdStr);
                if (!Esito.OK.toString().equals(esito))
                {
                    log.info("check tronco num {} not passed.", tronco.getTroncoNum());
                    esitoElab = Boolean.FALSE;
                    continue;
                }
                
                List<X4DssOrders> x4DssOrders = x4DssOrdersRepository.findOrdersByCorrelationAndTronco(request.getKey(), tronco.getX3Tronchi());
                List<Order> orders  = OrderUtility.convertFromX4DssOrders(x4DssOrders);
                if (isEmpty(orders))
                {
                    esitoElab = Boolean.FALSE;
                    continue;
                }
                
                x4DssOrders.stream().forEach(x -> entityManager.detach(x));
                
                Set<String> setParent   = new HashSet<>(); 
                setParent.addAll(orders.stream().filter(x -> isNotEmpty(x.getParent())).map(Order::getParent).toList());
                setParent.addAll(workOrderRepository.findWorkOderByLinea(orders.get(0).getJpnum(), tronco.getAssetNum(), tronco.getTroncoNum(), request.getPolicy()));
                List<String> listParent = new ArrayList<>(setParent);
                if (isNotEmpty(listParent)) //MODIFY
                {
                    List<Order> parentOrderList      = loadParentOrder(listParent);
                    Map<Date, List<Order>> mapByDate = splitOrdersAndSave(orders);
                    sendOrderModifyToSave(request, x4policy, tronco, mapByDate, parentOrderList, runner, recoveryCount, events);
                } else { //Solo tipologia ADD
                    Map<Date, List<Order>> mapByDate = splitOrdersAndSave(orders);
                    if (isNotEmpty(mapByDate))
                        sendOrderAddToSave(request, x4policy, tronco, mapByDate, runner, recoveryCount, events);
                }
            }
            
        } catch (Exception e) {
            esitoElab = Boolean.FALSE;
            log.error(e.getMessage(), e);
        }
        
        return esitoElab;
    }


    private Order findRoot(List<Order> itemsParent, List<Order> items, X3Tronchi tronco)
    {
        Order root            = null;
        List<Order> listOrder = itemsParent.stream().filter(x -> isNotEmpty(tronco.getAssetNum()) && tronco.getAssetNum().equals(x.getAssetnum()) &&
                                                                 isNotEmpty(tronco.getTroncoNum()) && tronco.getTroncoNum().equals(x.getTronconum())).toList();
        
        if (isEmpty(listOrder))
        {
            List<String> wonumList  = items.stream().map(Order::getWonum).toList();
            String wonumConcat      = String.join(SEP5, wonumList);
            log.info("Unable to determine a unique root for the following wonums {} - Step1", wonumConcat);
        } if (listOrder.size() > 1) {
            
            //PER PERIODICHE 
            
            Set<String> parentCandidate = items.stream()
                                               .filter(x -> isNotEmpty(x.getParent()))
                                               .map(Order::getParent)
                                               .collect(Collectors.toSet());
            
            listOrder = itemsParent.stream().filter(x -> isNotEmpty(tronco.getAssetNum()) && tronco.getAssetNum().equals(x.getAssetnum()) &&
                                                         isNotEmpty(tronco.getTroncoNum()) && tronco.getTroncoNum().equals(x.getTronconum()) &&
                                                         parentCandidate.contains(x.getWonum())).toList();

            if (isEmpty(listOrder) || listOrder.size() > 1)
            {
                List<String> wonumList = items.stream().map(Order::getWonum).toList();
                String wonumConcat = String.join(SEP5, wonumList);
                log.info("Unable to determine a unique root for the following wonums {} - Step2", wonumConcat);                
            } else
                root = listOrder.get(0);
                
        } else {
            
            Set<String> parentCandidate = items.stream()
                                               .filter(x -> isNotEmpty(x.getParent()))
                                               .map(Order::getParent)
                                               .collect(Collectors.toSet());

            if (isNotEmpty(parentCandidate))
                root = listOrder.get(0);
            else {
                Set<Date> listOrderDate = items.stream().map(x -> EamEtlUtils.truncDate(x.getDataModello())).collect(Collectors.toSet());
                root = listOrder.stream().filter(x -> listOrderDate.contains(EamEtlUtils.truncDate(x.getDataModello()))).findFirst().orElse(null);
            }
        }
            
        
        return root;
    }

    
    private List<Order> loadParentOrder(List<String> listParent)
    {
        List<Order> result           = new ArrayList<>();
        List<List<String>> partition = EamEtlUtils.partition(listParent, PARTITION_SIZE_WORKORDER);
        for (List<String> x: partition)
        {    
            List<WorkOrder> workOderByWonum = workOrderRepository.findWorkOderByWonum(x);
            if (isNotEmpty(workOderByWonum))
            {
                workOderByWonum.stream().forEach(j -> entityManager.detach(j));
            }
            
            result.addAll(OrderUtility.convertFromWorkOrder(workOderByWonum));
        }
        
        return result;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    private WorkedPolicyRequestEvent createObjToSave(String correlationKey, 
                                                     X4Policy policy, 
                                                     X3Tronchi tronco, 
                                                     List<Order> ordersToSendWorker, 
                                                     X4DssPolicyRunner runner, 
                                                     Integer recoveryCount, 
                                                     List<Pair<WorkedPolicyRequestEvent, X4DssWorkedHistory>> bulkHistory) 
            throws DataLayerException
    {
        String policyStr                = policy.getX4PolicyId().toString();
        X4DssWorkedHistory rowHistory   = x4DssWorkedHistoryQueryService.getDssWorkedHistoryByCorrelationAndPolicyAndAsset(correlationKey, policyStr, tronco.getAssetNum());
        if (isNull(rowHistory)) 
        {
            rowHistory = new X4DssWorkedHistory();
            rowHistory.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));
            rowHistory.setPolicyId(policyStr);
            rowHistory.setCorrelationId(correlationKey);
            rowHistory.setStatus(OPEN_STATUS);
            rowHistory.setTotalRelatedMessage(1);
            rowHistory.setProgressiveRelatedMessage(1);
            rowHistory.setAssetNum(tronco.getAssetNum());
            rowHistory.setTroncoNum(tronco.getTroncoNum());
            rowHistory.setX4DssPolicyRunnerId(runner.getX4DssPolicyRunnerId());
            rowHistory.setHasLd(0);
            rowHistory.setRecoveryCount(recoveryCount);
            rowHistory.setSent("Y");

            WorkedPolicyRequestEvent newEvent = new WorkedPolicyRequestEvent();
            newEvent.setEvent(EventActionType.TRONCO_SAVE.getValue());
            newEvent.setPolicy(policy.getX4PolicyId());
            newEvent.setCorrelationId(correlationKey);
            newEvent.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(tronco.getAssetNum()));
            newEvent.setAsset(tronco.getAssetNum());
            newEvent.setOrders(ordersToSendWorker);
            newEvent.setNumberMessagePolicy(1L);
            newEvent.setTipology(policy.getTipoPolicy());
            newEvent.setType(EventType.POLICY.getValue());
            newEvent.setTroncoNum(tronco.getTroncoNum());
            bulkHistory.add(new Pair<>(newEvent, rowHistory));
            
            return newEvent;
        }
        
        return null;
    }
    
    
    private void sendOrderAddToSave(MessageStreamCounter request, 
                                    X4Policy policy, 
                                    X3Tronchi tronco, 
                                    Map<Date, List<Order>> mapByDate, 
                                    X4DssPolicyRunner runner, 
                                    Integer recoveryCount, 
                                    Shoot<WorkedPolicyRequestEvent> events) 
            throws DataLayerException
    {
        if (Boolean.TRUE.equals(request.getAddTronco()))
        {
            sendPolicyForSingleTronco(request, policy, tronco);
        } else {
            List<Pair<WorkedPolicyRequestEvent, X4DssWorkedHistory>> bulkHistory = new ArrayList<>();
            int i = 0;
            for (List<Order> items: mapByDate.values())
            {
                List<SequenceValue> seqWo = workOrderQueryService.getSequenceForAggregate();
                String wo = Constants.PREFIX_DSS_WONUM + seqWo.get(0).getNextval();
                items.stream().forEach(x -> x.setParent(wo));
                
                Order orderRoot = new Order(items.get(0));
                orderRoot.setToCreate(Boolean.TRUE);
                orderRoot.setAssetnum(tronco.getAssetNum());
                orderRoot.setTronconum(tronco.getTroncoNum());
                orderRoot.setSapIdAsset(tronco.getSapId());
                orderRoot.setWonum(wo);
                orderRoot.setParent(null);
                orderRoot.setX4aggregati(1);
                items.add(orderRoot);
               
                for (Order x: items)
                {
                    if (isNotEmpty(x.getParent()))
                    {
                        x.setTronconum(null);
                        x.setSapIdAsset(null);
                    }
                }
                
                WorkedPolicyRequestEvent saved = createObjToSave(request.getKey() + SEP4 + tronco.getTroncoNum() + SEP4 + (++i), policy, tronco, items, runner, recoveryCount, bulkHistory);
                if (notNull(saved))
                    events.getItems().add(saved);
                
                if (isNotEmpty(bulkHistory) && bulkHistory.size() >= Constants.BULK_SIZE)
                {
                    x4DssWorkedHistoryQueryService.saveAllDssBulkWorkedHistory(bulkHistory);
                    bulkHistory.clear();
                    shootMessages(events);
                    events.getItems().clear();
                }
            }
            
            //Invio eventuali rimanenti
            if (isNotEmpty(bulkHistory))
            {
                x4DssWorkedHistoryQueryService.saveAllDssBulkWorkedHistory(bulkHistory);
                bulkHistory.clear();
                shootMessages(events);
                events.getItems().clear();
            }
        }
    }


    private void sendOrderModifyToSave(MessageStreamCounter request, 
                                       X4Policy policy, 
                                       X3Tronchi tronco, 
                                       Map<Date, List<Order>> mapByDate, 
                                       List<Order> parentOrderList, 
                                       X4DssPolicyRunner runner, 
                                       Integer recoveryCount, 
                                       Shoot<WorkedPolicyRequestEvent> events) 
            throws DataLayerException
    {
        if (Boolean.TRUE.equals(request.getAddTronco()))
        {
            sendPolicyForSingleTronco(request, policy, tronco);
        } else {
            int i = 0;
            List<Pair<WorkedPolicyRequestEvent, X4DssWorkedHistory>> bulkHistory = new ArrayList<>();
            for (Entry<Date, List<Order>> item: mapByDate.entrySet())
            {
                List<Order> items   = new ArrayList<>(item.getValue());
                Order root          = findRoot(parentOrderList, items, tronco);
                
                if (isNull(root))
                {
                    List<SequenceValue> seqWo = workOrderQueryService.getSequenceForAggregate();
                    String wo = Constants.PREFIX_DSS_WONUM + seqWo.get(0).getNextval();
                    
                    root = new Order(items.get(0));
                    root.setToCreate(Boolean.TRUE);
                    root.setAssetnum(tronco.getAssetNum());
                    root.setTronconum(tronco.getTroncoNum());
                    root.setSapIdAsset(tronco.getSapId());
                    root.setWonum(wo);
                    root.setParent(null);
                    root.setX4aggregati(1);
                } else if (!Boolean.TRUE.equals(request.getAddTronco())) {
                    Order firstOrder = items.get(0);
                    root.setDataModello(firstOrder.getDataModello());
                    root.setDataPianificataInizio(firstOrder.getDataPianificataInizio());
                    root.setDataPianificataFine(firstOrder.getDataPianificataFine());
                    root.setDataMaxPosticipo(firstOrder.getDataMaxPosticipo());
                    root.setDataNonPrimaDi(firstOrder.getDataNonPrimaDi());                
                }
    
                for (Order x: items)
                {
                    x.setTronconum(null);
                    x.setSapIdAsset(null);
                }
    
                items.add(root);
                for (Order wo: items)
                {
                    if ((!wo.getAssetnum().equals(root.getAssetnum())) && isEmpty(wo.getParent())) 
                        wo.setParent(root.getWonum());
                }
                
                //items contiene sono l'aggregatore. Scartiamo aggiornamento
                if (items.size() == 1 && isEmpty(items.get(0).getParent()))
                    continue;
                
                WorkedPolicyRequestEvent saved = createObjToSave(request.getKey() + SEP4 + tronco.getTroncoNum() + SEP4 + (++i), policy, tronco, items, runner, recoveryCount, bulkHistory);
                if (notNull(saved))
                    events.getItems().add(saved);
                
                if (isNotEmpty(bulkHistory) && bulkHistory.size() >= Constants.BULK_SIZE)
                {
                    x4DssWorkedHistoryQueryService.saveAllDssBulkWorkedHistory(bulkHistory);
                    bulkHistory.clear();
                    shootMessages(events);
                    events.getItems().clear();
                }
            }
            
            //Invio eventuali rimanenti
            if (isNotEmpty(bulkHistory))
            {
                x4DssWorkedHistoryQueryService.saveAllDssBulkWorkedHistory(bulkHistory);
                bulkHistory.clear();
                shootMessages(events);
                events.getItems().clear();
            }
        }
    }

    
    private void sendPolicyForSingleTronco(MessageStreamCounter request, X4Policy policy, X3Tronchi tronco)
    {
        BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(Constants.END_POINT_MANAGER_EVENTS_HANDLER);
        long time                   = (new Date()).getTime();
        EventDTO newEvent           = new EventDTO();
        newEvent.setEvent(EventActionType.ADD.getValue());
        newEvent.setPolicy(policy.getX4PolicyId().toString());
        newEvent.setTipology(policy.getTipoPolicy());
        newEvent.setType(EventType.POLICY.getValue());
        newEvent.setKafkaKey(policy.getX4PolicyId().toString() + Constants.SEP4 + time);
        newEvent.setAttributes(new HashMap<>());
        newEvent.getAttributes().put(PolicyAttributes.TRONCONUM.name(), tronco.getTroncoNum());
        kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(newEvent));
    }
    
    
    private Map<Date, List<Order>> splitOrdersAndSave(List<Order> orders)
    {
        Map<Date, List<Order>> mapByDate = new TreeMap<>();
        for (Order order: orders)
        {
            Date dataModello = EamEtlUtils.truncDate(order.getDataModello());
            if (isNull(dataModello))
            {
                log.warn("data modello non valorizzata per order associato all'asset {}", order.getAssetnum());
                continue;
            }
            
            List<Order> listOrder = mapByDate.get(dataModello);
            if (isNull(listOrder))
            {
                listOrder = new ArrayList<>();
                mapByDate.put(dataModello, listOrder);
            }
            
            listOrder.add(order);
        }
        
        return mapByDate;
    }
}
