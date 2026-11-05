package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.OrderActions;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.dal.service.X4DssWorkedHistoryQueryService;
import it.terna.nas.service.exposure.events.PolicyRequestEvent;
import it.terna.nas.service.exposure.events.WorkerFeedbackRequestEvent;
import it.terna.nas.service.material.dto.EsitoElab;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.Catalog.WorkOrderStatus;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public abstract class AbstractDssHandler extends AbstractHandler
{
    private static final int PARTITION_SIZE = 40;
    
    @Autowired
    protected BrokerEntityConfiguration brokerEntities;
    
    @Autowired
    protected KafkaProducerHandler kafkaHandler;
    
    @Autowired
    protected X4DssWorkedHistoryQueryService x4DssWorkedHistoryQueryService;

    public void sendWorkerFeedbackEvent(PolicyRequestEvent requestEvent, List<Order> orders) 
    {
        log.debug("call sendWorkerFeedbackEvent({})", requestEvent.getCorrelationId());
    
        WorkerFeedbackRequestEvent event = new WorkerFeedbackRequestEvent();
        event.setAsset(requestEvent.getAsset());
        event.setCorrelationId(requestEvent.getCorrelationId());
        event.setEvent(requestEvent.getEvent());
        event.setPolicy(requestEvent.getPolicy());
        event.setNumberMessagePolicy(requestEvent.getNumberMessagePolicy());
        event.setTipology(requestEvent.getTipology());
        event.setType(requestEvent.getType());
        event.setAddTronco(requestEvent.getAddTronco());
        
        if (isNotEmpty(orders)) 
        {
            event.setEsito(StatusX4Esito.OK.name());
            log.debug("Orders set in the event. Esito: {}", StatusX4Esito.OK.name());
        } else {
            event.setEsito(StatusX4Esito.KO.name());
            log.debug("No orders found. Esito set to {}", StatusX4Esito.KO.name());
        }
    
        BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(Constants.END_POINT_HANDLE_WORKER_FEEDBACK);
        kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
    }
    
    protected void updateStatus(String dssWorkerId, String correlationId, EsitoElab esito) 
    {
        try {
            List<Pair<String, String>> listPairAssetWonum = null;
            X4DssWorkedHistory row = x4DssWorkedHistoryQueryService.getDssWorkedHistoryById(dssWorkerId);
            
            if (notNull(row))
            {
                row.setCode(null);
                row.setDescription(null);
                row.setStatus(Constants.CLOSED_STATUS);
                row.setChangeDate(new java.sql.Date((System.currentTimeMillis())));
                if (esito != null) 
                {
                    listPairAssetWonum = esito.getListPairAssetWonum();
                    if (isNotEmpty(listPairAssetWonum) && listPairAssetWonum.size() == 1)
                        row.setOrderNum(listPairAssetWonum.get(0).getSecond());
                    
                    EAMErrorMapping esitoMapping = esito.getEsito();
                    if (notNull(esitoMapping))
                    {    
                        row.setCode(esitoMapping.getEamCode());
                        row.setDescription(esitoMapping.getErrorDescription());
                        row.setErrorFlag(esitoMapping.getErrorFlag());
                        row.setShortDescription(esitoMapping.getShortDescription());
                    }
                    
                    if (isNotEmpty(esito.getDescription()))
                        row.setDescription(esito.getDescription());  
                }
                
                if (isNotEmpty(row.getDescription()) && row.getDescription().length() > 4000)
                    row.setDescription(row.getDescription().substring(0, 4000));
                
                x4DssWorkedHistoryQueryService.saveDssWorkedHistory(row);
            }
            else 
            {
            	log.warn("Unable to insert row: {} on x4_dss_worked_history", dssWorkerId);
            }
        
            if (isNotEmpty(listPairAssetWonum) && listPairAssetWonum.size() > 1)
                saveWonum(correlationId, esito);
            
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
    
    
    private void saveWonum(String correlationId, EsitoElab esito) 
            throws DataLayerException
    {
        List<Pair<String, String>> listPairAssetWonum = esito.getListPairAssetWonum();
        String troncoNum = esito.getTroncoNum();
        List<List<Pair<String, String>>> partition = EamEtlUtils.partition(listPairAssetWonum, PARTITION_SIZE);
        for(List<Pair<String, String>> itemList: partition)
        {
            Set<String> setAssetWonumUpdate = new HashSet<>();
            for (Pair<String, String> item: itemList)
                setAssetWonumUpdate.add(item.getFirst() + Constants.SEP6 + item.getSecond()); //ASSETNUM=WONUM
            
            //ASSETNUM0=WONUM0|ASSETNUM1=WONUM1|....|ASSETNUMn=WONUMn
            String rowsToSearchUpdate = String.join(Constants.SEP1, setAssetWonumUpdate);
            x4DssWorkedHistoryQueryService.updateHistoryDss(correlationId, rowsToSearchUpdate, troncoNum);
        }
    }

    
    protected void checkStatusOrderActions(OrderActions orderActions)
    {
        List<Order> orders = orderActions.getOrders();
        if (isEmpty(orders))
            return;
        
        List<Order> listOrdersRoot = orders.stream()
                                           .filter(x -> isEmpty(x.getParent()) && Constants.ONE.equals(x.getX4aggregati()))
                                           .collect(Collectors.toList());
            
        Map<String, List<Order>> mapOrdersByParent = null;
        if (isNotEmpty(listOrdersRoot))
        {    
            mapOrdersByParent = orders.stream().filter(x -> isNotEmpty(x.getParent())).collect(Collectors.groupingBy(Order::getParent));
            
            for (Order root: listOrdersRoot) 
            {
                List<Order> listChilds = mapOrdersByParent.get(root.getWonum());
                
                if (isEmpty(listChilds))
                    continue;
                
                List<Order> listChildsNonEseguito   = listChilds.stream().filter(x -> (!WorkOrderStatus.NON_ESEGUITO.getCode().equals(x.getStato()))).collect(Collectors.toList());
                long numOrderDiversoNonEseguito     = isNotEmpty(listChildsNonEseguito) ? listChildsNonEseguito.size() : 0;
                
                if (numOrderDiversoNonEseguito < 1)
                    root.setStato(WorkOrderStatus.NON_ESEGUITO.getCode());
                else if (numOrderDiversoNonEseguito > 0 && WorkOrderStatus.NON_ESEGUITO.getCode().equals(root.getStato()))
                    root.setStato(listChildsNonEseguito.get(0).getStato());
            }
        }
    }
}
