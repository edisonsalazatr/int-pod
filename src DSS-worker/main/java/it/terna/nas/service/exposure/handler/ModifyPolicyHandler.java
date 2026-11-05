package it.terna.nas.service.exposure.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.EsitoElab;
import it.terna.nas.service.material.dto.policy.AttributesDTO;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.ModifyPolicyResultType;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Catalog.WorkOrderStatus;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ModifyPolicyHandler 
{
	@Autowired
    private ActivityHandler activityHandler;
	
    @Autowired
	private OrderHandler orderHandler;

    @Autowired
    private PeriodicOrderHandler periodicOrderHandler;
    
    @Autowired
    private TrunkIspectionHandler trunkIspectionHandler;
    
    public ModifyPolicyResultType processModify(PolicyRequestEvent event, AssetData assetData, Policy policy, EsitoElab esito) 
            throws DataLayerException, EAMException 
    {
        Activity activity = activityHandler.getActivityByMacroActivity(assetData, policy);
        log.debug("Retrieved activity: {}", activity);
        
        if (activity == null) 
        {
            log.warn("no activity found related to macroactivity: {}. possible catalog configuration error!", policy.getMacroActivityId());
            esito.setEsito(EAMErrorMapping.EAM_0002);
            return null;
        }
        
        ModifyPolicyResultType checkModify = checkModifyPolicy(assetData, policy, activity);
        log.debug("Check modify result: {}", checkModify);
        
        switch (checkModify) 
        {
            case MODIFIABLE: modifyOrders(event, assetData, policy, activity, esito);
                             break;
                             
            case NOT_MODIFIABLE: esito.setEsito(EAMErrorMapping.EAM_0005);
                                 break;
                                 
            case NOT_FOUND: log.info("No OdL to modify for asset: {} and policy: {}", assetData.getAsset().getAssetNum(), policy.getId());
                            break;
        }
        
        return checkModify;
    }

    
    private void modifyOrders(PolicyRequestEvent event, AssetData assetData, Policy policy, Activity activity, EsitoElab esito) 
            throws DataLayerException, EAMException
    {
        OrderActions orderActions = copyOrModifyOrder(assetData, policy, activity, event.getAttributes());
        
        if (log.isDebugEnabled())
            log.debug("Order actions created: {}", orderActions);
        
        if (trunkIspectionHandler.hasTrunkInspectionOrder(assetData, policy))
        {
            List<Order> listOrder = trunkIspectionHandler.updateTrunkIspection(event, assetData.getAsset(), policy, orderActions.getOrders());
            trunkIspectionHandler.sendWorkerFeedbackEvent(event, listOrder);
            esito.setEsito(EAMErrorMapping.EAM_0004);
        } else {
            esito.setEsito(EAMErrorMapping.EAM_0006);
            log.info("Order modification completed.");
            
            if (Assorted.notNull(orderActions.getOrders())) 
            {
                esito.setListPairAssetWonum(orderHandler.sendOrders(orderActions, assetData.getAsset().getAssetNum(), policy, esito));
                log.info("Orders sent for asset: {}", assetData.getAsset().getAssetNum());
            }            
        }
    }
    
    
	private ModifyPolicyResultType checkModifyPolicy(AssetData assetData, Policy policy, Activity activity) 
	{
	    log.info("[START] checkModifyPolicy");

	    boolean exists = assetData.getAttivita().stream()
	    	    .anyMatch(a -> a.getJpnum().equals(activity.getJpnum()) 
	    	    	&& Objects.equals(a.getX4policyid(), policy.getId()));

	    if (!exists) 
	    {
	        log.info("[END] checkModifyPolicy - No matching order found.");
	        return ModifyPolicyResultType.NOT_FOUND;
	    }

	    boolean isModifiable = assetData.getAttivita().stream()
	    	    .anyMatch(a -> a.getJpnum().equals(activity.getJpnum())
	    	        && Objects.equals(a.getX4policyid(), policy.getId())
	    	        && (a.getStatus().equals(WorkOrderStatus.APERTO.getCode()) 
	    	            || a.getStatus().equals(WorkOrderStatus.DA_VALIDARE.getCode())));

	    if (isModifiable) {
	        log.info("[END] checkModifyPolicy - Order is modifiable.");
	        return ModifyPolicyResultType.MODIFIABLE;
	    } else {
	        log.info("[END] checkModifyPolicy - Order is not modifiable.");
	        return ModifyPolicyResultType.NOT_MODIFIABLE;
	    }
	}
	
	
	private OrderActions copyOrModifyOrder(AssetData assetData, Policy policy, Activity activity, AttributesDTO attributesDTO) throws DataLayerException {
        log.info("[START] copyOrModifyOrder");

        List<AttivitaAsset> listOrders = assetData.getAttivita().stream()
        	    .filter(order -> order.getJpnum().equals(activity.getJpnum()) 
        	    		&& Objects.equals(order.getX4policyid(), policy.getId()) 
        	    		&& (order.getStatus().equals(WorkOrderStatus.APERTO.getCode()) 
        	    				|| order.getStatus().equals(WorkOrderStatus.DA_VALIDARE.getCode())))
        	    .collect(Collectors.toList());

        log.info("Found {} matching orders", listOrders.size());

        List<Order> orders = new ArrayList<>();
        OrderActions orderActions = new OrderActions();

        if (Assorted.notNull(attributesDTO.getFrequency()) || 
            (PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()) && Assorted.notNull(attributesDTO.getRifTemporale()))) {

            log.info("Handling frequency modification");

            if (EamEtlUtils.isOverOneYear(policy.getFrequenza(), policy.getTimeUnit())) {
                orders = periodicOrderHandler.modifyPeriodicOrdersForOverOneYear(assetData, policy, activity, listOrders, attributesDTO);
            } else {
                orders = periodicOrderHandler.modifyPeriodicOrdersWithinOneYear(assetData, policy, activity, listOrders, attributesDTO);
            }
        } else if (Assorted.notNull(attributesDTO.getDeferrability())|| 
                (PolicyType.ON_CONDITION.getCode().equals(policy.getTipoPolicy()) && Assorted.notNull(attributesDTO.getRifTemporale()))) {
            log.info("Handling deferrability modification");
            orders = orderHandler.modifyOnCondictionOrders(assetData, policy, activity, listOrders, attributesDTO);
        } else if (attributesDTO.getDeferrability() == null &&  
                attributesDTO.getFrequency() == null &&  
                attributesDTO.getRifTemporale() == null &&  
                (Assorted.notNull(attributesDTO.getMaxAdvance()) ||  
                 Assorted.notNull(attributesDTO.getMaxSnooze()))) {  
        	orders = orderHandler.modifyMaxAdvanceOrMaxSnooze(assetData, policy, activity, listOrders, attributesDTO);
        	
       }
        if(Assorted.isNotEmpty(orders)) {
            orderActions.setOrders(orders);       	
        }
        log.info("[END] copyOrModifyOrder. Order modification completed with {} orders", Assorted.isNotEmpty(orders) ? orders.size() : 0);
        return orderActions;
    }

}
