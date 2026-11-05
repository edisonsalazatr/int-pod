package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.REPLACEMENT_ACTIVITY;
import static it.terna.nas.service.material.utility.Constants.SEP5;
import static it.terna.nas.service.material.utility.Constants.TRAINATO_TRAINANTE_ENABLE;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;
import io.lettuce.core.RedisException;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.OrderActions;
import it.terna.nas.service.dal.entity.eam.X4DssOrders;
import it.terna.nas.service.dal.repository.X4DssOrdersRepository;
import it.terna.nas.service.dal.service.AssetDataService;
import it.terna.nas.service.dal.service.WorkOrderQueryService;
import it.terna.nas.service.dal.service.X4PolicyService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.PolicyRequestEvent;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.EsitoElab;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Catalog.VeroOrFalso;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TrainatoTrainanteHandler extends AbstractDssHandler
{
    @Autowired
    private AssetDataService assetDataService;
    
    @Autowired
    private OrderHandler orderHandler;
    
    @Autowired
    private X4PolicyService policyService;
    
    @Autowired
    private PrevalentOrderHandler prevalentOrderHandler;
    
    @Autowired
    private X4DssOrdersRepository x4DssOrdersRepository; 
    
    @Autowired
    private ZonizzazioneHandler zonizzazioneHandler;
    
    @Autowired
    private WorkOrderQueryService workOrderQueryService;
    
    public List<String> checkIsTowingTowed(PolicyRequestEvent requestEvent, AssetData assetData, List<Order> listOrder) 
            throws EAMException
    {
    	if (Assorted.isNotEmpty(listOrder)) 
    	{
    	    Asset asset            = assetData.getAsset();
    	    AssetData assetParent  = assetDataService.getAssetDataByAssetNum(asset.getParent(), PolicyType.PERIODICA.getCode().equals(requestEvent.getTipology()));
    		List<String> assetForTesatura = assetDataService.findAssetForTesatura(assetData, assetParent); 
            if (isNotEmpty(assetForTesatura) && assetForTesatura.size() > 1)
            {
                assetForTesatura = new ArrayList<String>(assetForTesatura);
                Collections.sort(assetForTesatura);
                Map<String, List<Order>> mapOrders = listOrder.stream()
                                                              .filter(x -> isNotEmpty(x.getAssetnum()))
                                                              .collect(Collectors.groupingBy(Order::getAssetnum));
                
                String assetInput       = asset.getAssetNum();
                List<Order> listOrders  = mapOrders.get(assetInput);
                
                if (!assetForTesatura.contains(assetInput))
                {
                    log.error("Trainato/Trainante: No asset input {} for Tesatura {}", assetInput, assetForTesatura);
                    return null;                
                }
                
                for (Order x: listOrders)
                    x.setTesatura(assetForTesatura);
                
                List<Order> result = new ArrayList<>();
                mapOrders.values().stream().forEach(x -> result.addAll(x));
                saveDbOrders(requestEvent, result);
                return assetForTesatura;
            }
    	}
    	
        return null;
    }
    
    
    public boolean hasTrainatoTrainante(AssetData assetData, String tipoAttivita)
    {
        if (notNull(assetData) && notNull(tipoAttivita))
        {
            Asset asset = assetData.getAsset();
            log.debug("call hasTrainatoTrainante({}, {}, {})", asset.getAssetNum(), asset.getClassificationId(), tipoAttivita);
            String tipoAsset = assetData.getAsset().getClassificationId();
            boolean esito    = TRAINATO_TRAINANTE_ENABLE.contains(tipoAsset) && REPLACEMENT_ACTIVITY.equals(tipoAttivita);
            log.debug("end hasTrainatoTrainante({}, {}) ==> {}", asset.getAssetNum(), asset.getClassificationId(), esito);
            return esito;
        } else
            log.warn("assetData or tipoAttivita is null");
        
        return false;
    }
    
    
    private void saveDbOrders(PolicyRequestEvent requestEvent, List<Order> listOrders)
    {
        if (isNotEmpty(listOrders))
        {
            List<X4DssOrders> toSave = new ArrayList<>();
            Long idDssWorker = Long.parseLong(requestEvent.getDssWorkerId());
            for (Order order: listOrders)
            {
                X4DssOrders row = new X4DssOrders();
                row.setIdDssWorked(idDssWorker);
                row.setDssPcCorrelationId(requestEvent.getCorrelationId());
                row.setAssetNum(order.getAssetnum());
                row.setJpNum(order.getJpnum());
                row.setDataModello(order.getDataModello());
                row.setDataNonPrimaDi(order.getDataNonPrimaDi());
                row.setDataMaxPosticipo(order.getDataMaxPosticipo());
                row.setDataPianifInizio(order.getDataPianificataInizio());
                row.setDataPianifFine(order.getDataPianificataFine());
                row.setToCreate(Boolean.TRUE.equals(order.getToCreate()) ? VeroOrFalso.SI.getCode() : VeroOrFalso.NO.getCode());
                row.setX4PolicyId(order.getX4PolicyId());
                row.setX4Policy(order.getIdPolicy());
                row.setX4Trainante(order.getX4Trainante());
                row.setX4WoPrevalente(order.getX4OrdinePrevalente());
                row.setWonum(order.getWonum());
                row.setWoParent(order.getParent());
                row.setTroncoNum(order.getTronconum());
                row.setStatus(order.getStato());
                row.setX4Note(order.getX4Note());
                row.setSapIdAsset(order.getSapIdAsset());
                row.setX4Aggregate(order.getX4aggregati());
                row.setTratta(String.join(SEP5, order.getTesatura()));
                row.setX4outsourcing(order.getX4outsourcing());
                toSave.add(row);
            }
            
            x4DssOrdersRepository.saveAll(toSave);
        }
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public void saveOdL(PolicyRequestEvent event) 
            throws EAMRetryException
    {
        EsitoElab esito       = new EsitoElab();
        esito.setEsito(EAMErrorMapping.EAM_0000);

        try {
            
            if (isNotEmpty(event.getOrders())) 
            {
                Policy policy                   = policyService.loadPolicy(event.getPolicy());
                List<Order> listOrderCompatible = new ArrayList<>();
                
                for (Order orderJ: event.getOrders())
                {
                    AssetData assetJ = assetDataService.getAssetDataByAssetNum(orderJ.getAssetnum(), PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
                    
                    // Controllo se ordine già è presente
                    String existingWonum = workOrderQueryService.getWonumByPolicyAssetAndJpnum(policy.getId(),
                                                                                               orderJ.getJpnum(),
                                                                                               assetJ.getAsset().getAssetNum());

                    if (existingWonum == null) 
                    {
                        boolean suitable = assetDataService.checkAssetStatus(assetJ, orderJ.getJpnum());
                        if (Boolean.TRUE.equals(suitable))
                            listOrderCompatible.add(orderJ);
                    }
                }

                if (isNotEmpty(listOrderCompatible))
                {   
                    OrderActions orderActions = null;
                    if (PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()))
                        orderActions = new OrderActions(listOrderCompatible);
                    else
                        orderActions = prevalentOrderHandler.checkPrevalentOrder(listOrderCompatible, policy);
                    
                    List<Order> listOrdersTmp   = orderActions.getOrders();
                    List<Order> listOrder       = new ArrayList<>(listOrdersTmp);
                    listOrder.addAll(zonizzazioneHandler.updateZonizzazioneTroncoAndTrainato(listOrdersTmp, policy, event.getCorrelationId()));
                    
                    orderActions.setOrders(listOrder);
                    esito.setListPairAssetWonum(orderHandler.sendOrders(orderActions, isNotEmpty(listOrder) ? listOrder.get(0).getAssetnum() : null, policy, esito));
                } else {
                    esito.setEsito(EAMErrorMapping.EAM_0009);
                }
            }
            
        } catch(RedisException | DataLayerException | QueryTimeoutException l) {
            esito.setEsito(EAMErrorMapping.EAM_9999);
            esito.setDescription(l.getMessage());
            log.error(l.getMessage(), l);
            throw new EAMRetryException(esito.getEsito().getEamCode(), esito.getDescription(), l);            
        } catch(EAMException q) {
            esito.setEsito(EAMErrorMapping.findCausaleEsito(q.getErrorCode()));
            esito.setDescription(q.getMessage());
            log.error(q.getMessage(), q);
            if (q instanceof EAMRetryException)
                throw (EAMRetryException) q;
        } catch(Exception e) {
            esito.setEsito(EAMErrorMapping.EAM_9999);
            esito.setDescription(e.getMessage());
            log.error(" - Error while evaluating policy", e);
        } finally {
            updateStatus(event.getDssWorkerId(), event.getCorrelationId(), esito);
        }
    }
}