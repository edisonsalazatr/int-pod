package it.terna.nas.service.dal.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.dal.entity.eam.Activity;
import it.terna.nas.service.dal.entity.eam.AttivitaAsset;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.OrderActions;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.events.PolicyRequestEvent;
import it.terna.nas.service.exposure.handler.ActivityHandler;
import it.terna.nas.service.exposure.handler.OrderHandler;
import it.terna.nas.service.exposure.handler.PeriodicOrderHandler;
import it.terna.nas.service.exposure.handler.TrunkIspectionHandler;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.EsitoElab;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.WorkOrderStatus;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ZonizzazioneSupport
{
    @Autowired
    private OrderHandler orderHandler;
    
    @Autowired
    private TrunkIspectionHandler trunkIspectionHandler;
    
    @Autowired
    private PeriodicOrderHandler periodicOrderHandler;
    
    @Autowired
    private ActivityHandler activityHandler;
    
    
    public boolean processModifyZonizzazione(AssetData assetData, Policy policy, EsitoElab esito, PolicyRequestEvent event) 
            throws DataLayerException, EAMException {
        
        log.info("[START] processModifyZonizzazione for assetnum='{}', policy='{}'", assetData.getAsset().getAssetNum(), policy.getId());
        boolean result = false;
        Activity activity = activityHandler.getActivityByMacroActivity(assetData, policy);
        log.debug("Retrieved activity: {}", activity);
        
        if (activity == null) {
            log.warn("no activity found related to macroactivity: {}. possible catalog configuration error!", policy.getMacroActivityId());
            esito.setEsito(EAMErrorMapping.EAM_0002);
            result = true;
            return result;
        }
        

        // Find existing order with status APERTO o DA VALIDARE
        boolean exists = assetData.getAttivita().stream()
                .anyMatch(a -> a.getJpnum().equals(activity.getJpnum()) && 
                              !a.getX4policyid().equals(policy.getId()) &&
                              (a.getStatus().equals(WorkOrderStatus.APERTO.getCode()) || 
                               a.getStatus().equals(WorkOrderStatus.DA_VALIDARE.getCode())));

        


        if (exists) {
            result = true;
            log.debug("Found existing orders for activity: {}", activity.getJpnum());
            List<AttivitaAsset> existingOrders = assetData.getAttivita().stream()
                    .filter(a -> a.getJpnum().equals(activity.getJpnum()) && 
                                !a.getX4policyid().equals(policy.getId()) &&
                                (a.getStatus().equals(WorkOrderStatus.APERTO.getCode()) || 
                                 a.getStatus().equals(WorkOrderStatus.DA_VALIDARE.getCode())) &&
                                (policy.getLogicaTronco() == 1 ? a.getParent() != null : a.getParent() == null))
                    .collect(Collectors.toList());
            if(Assorted.isNotEmpty(existingOrders)) {
                OrderActions orderActions = new OrderActions();
                List<Order> orderToModify = null;
                if (EamEtlUtils.isOverOneYear(policy.getFrequenza(), policy.getTimeUnit())) {
                    orderToModify = periodicOrderHandler.modifyPeriodicOrdersZonizzazione(assetData, policy, activity, existingOrders);
                } else {
                    orderToModify = periodicOrderHandler.modifyPeriodicOrdersZonizzazioneWithinOneYear(assetData, policy, activity);
                }
                log.debug("Number of orders to modify: {}", orderToModify.size());
                if (trunkIspectionHandler.hasTrunkInspectionOrder(assetData, policy))
                {
                    event.setEvent(Constants.CLASS_TRONCO_ISPEZIONE);
                    List<Order> listOrder = trunkIspectionHandler.updateTrunkIspection(event, assetData.getAsset(), policy, orderToModify);
                    trunkIspectionHandler.sendWorkerFeedbackEvent(event, listOrder);
                    esito.setEsito(EAMErrorMapping.EAM_0004);
                }
                else {
                    orderActions.setOrders(orderToModify);
                    esito.setEsito(EAMErrorMapping.EAM_0007);
                    orderHandler.sendOrders(orderActions, assetData.getAsset().getAssetNum(), policy, esito);
                    log.info("Orders sent for assetnum='{}'", assetData.getAsset().getAssetNum());
                }
            }
            else {
                log.info("No existing valid order found for jpnum='{}'", activity.getJpnum());
                esito.setEsito(EAMErrorMapping.EAM_0008);
            }    
        } 
        else {
            log.info("No existing order found for jpnum='{}'", activity.getJpnum());
            if (trunkIspectionHandler.hasTrunkInspectionOrder(assetData, policy))
            {
                event.setEvent(Constants.CLASS_TRONCO_ISPEZIONE);
            }
        }
        log.info("[END] processModifyZonizzazione for assetnum='{}', policy='{}'", assetData.getAsset().getAssetNum(), policy.getId());
        return result;

    }
}
