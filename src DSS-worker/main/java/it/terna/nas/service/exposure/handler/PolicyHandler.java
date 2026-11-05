package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.EamEtlUtils.notEmptyAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;
import io.lettuce.core.RedisException;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.Activity;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.OrderActions;
import it.terna.nas.service.dal.service.AssetDataService;
import it.terna.nas.service.dal.service.X4PolicyService;
import it.terna.nas.service.dal.service.ZonizzazioneSupport;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.PolicyRequestEvent;
import it.terna.nas.service.exposure.service.EvaluatorService;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.EsitoElab;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.ModifyPolicyResultType;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PolicyHandler extends AbstractDssHandler
{
	@Autowired
    private ActivityHandler activityHandler;

    @Autowired
    private AggregateCondFuneTelHandler aggregateCondFuneTelHandler;
    
    @Autowired
    private AggregateStazioneHandler aggregateStazioneHandler;

    @Autowired
	private AssetDataService assetDataService;
	
    @Autowired 
    private EvaluatorService evaluatorService;

    @Autowired
    private X4PolicyService policyService;

    @Autowired
	private OrderHandler orderHandler;
	
    @Autowired
    private TrainatoTrainanteHandler trainatoTrainanteHandler;
    
    @Autowired
    private TrunkIspectionHandler trunkIspectionHandler;
    
    @Autowired
    private ModifyPolicyHandler modifyPolicyHandler;
    
    @Autowired
    private ZonizzazioneHandler zonizzazioneHandler;
    
    @Autowired
    private ZonizzazioneSupport zonizzazioneSupport;
    
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	@Transactional	
	public void processMessage(PolicyRequestEvent event) 
	    throws EAMRetryException
	{
	    Policy policy          = null;
	    AssetData assetData    = null;
		EsitoElab esito        = new EsitoElab();
		esito.setEsito(EAMErrorMapping.EAM_0000);

		long startTime = System.currentTimeMillis();

		try {
			log.info(" - Evaluating policy with id='{}' on assetnum='{}' |{}|", event.getPolicy(), event.getAsset(), startTime);
			Boolean result = null;
			Boolean zonizzazione = false;
			ModifyPolicyResultType checkModify = null;
			policy = policyService.loadPolicy(event.getPolicy());

			assetData = assetDataService.getAssetDataByAssetNum(event.getAsset(), PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));

			if (notNull(event.getAttributes()) && notEmptyAttributes(event.getAttributes())) 
			{
			    checkModify = modifyPolicyHandler.processModify(event, assetData, policy, esito);
			}

			if (checkModify == null || checkModify == ModifyPolicyResultType.NOT_FOUND) 
			{  
			    if (!EAMErrorMapping.EAM_0002.equals(esito.getEsito())) 
			        result = evaluatorService.evaluate(event, assetData, policy);  
			}

			long endTime     = System.currentTimeMillis();
			log.info("  - End evaluate policy with id='{}' on assetnum='{}' with result='{}' |{}|", event.getPolicy(), event.getAsset(), result, endTime - startTime);
			
			if (notNull(result) && result && event.getEvent().equals(Constants.ZONIZZAZIONE)) 
			{
				zonizzazione = zonizzazioneSupport.processModifyZonizzazione(assetData, policy, esito, event);
			}
			
			if (notNull(result) && result && !zonizzazione) 
			{
				esito = processPolicyType(policy, assetData, event);	
			} else if(notNull(result) && !result) {
				esito.setEsito(EAMErrorMapping.EAM_0001);
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
		
		notifyManager(event, policy, assetData, esito);
	}

	
    private EsitoElab handleOnConditionPolicy(AssetData assetData, Policy policy, PolicyRequestEvent requestEvent, Activity activity) 
            throws DataLayerException, EAMException 
    {
        log.info("  - Handling ON-CONDITION policy");

        if (notNull(requestEvent.getAggregationLevel()))
        {
        	if(assetDataService.checkIfIsStazione(assetData.getAsset().getAssetNum())) {
                return aggregateStazioneHandler.manageAggregateStazione(requestEvent, policy, assetData);
        	} else {
        		boolean esito = aggregateCondFuneTelHandler.manageAggregate(requestEvent, policy, assetData);
                aggregateCondFuneTelHandler.sendWorkerFeedbackEvent(requestEvent, esito);
                return new EsitoElab(EAMErrorMapping.EAM_0004);
        	}
        }
        
        Order order           = orderHandler.createOrder(assetData, policy, activity);
        List<Order> listOrder = new ArrayList<>();
        if (order != null) 
            listOrder.add(order);
        
        if (trainatoTrainanteHandler.hasTrainatoTrainante(assetData, activity.getClassificationId()))
        {
            List<String> trainante = trainatoTrainanteHandler.checkIsTowingTowed(requestEvent, assetData, listOrder);
            if (isNotEmpty(trainante))
            {
                trainatoTrainanteHandler.sendWorkerFeedbackEvent(requestEvent, listOrder);
                return new EsitoElab(EAMErrorMapping.EAM_0004);
            }
        }
    
        if (trunkIspectionHandler.hasTrunkInspectionOrder(assetData, policy)) 
        {
        	listOrder = trunkIspectionHandler.updateTrunkIspection(requestEvent, assetData.getAsset(), policy, listOrder);
            trunkIspectionHandler.sendWorkerFeedbackEvent(requestEvent, listOrder);
        	return new EsitoElab(EAMErrorMapping.EAM_0004);
        }
        
        OrderActions orderActions = orderHandler.checkPrevalentOrder(listOrder, policy);
        zonizzazioneHandler.handleZonizzazioneOdl(policy, assetData, activity.getJpnum(), orderActions.getOrders())
        .ifPresent(odl -> {
            if (isNotEmpty(orderActions.getOrders())) {
            	orderActions.getOrders().addAll(odl);
            }
        });
        log.info("  - Sending ON-CONDITION order");
        EsitoElab esito = new EsitoElab(EAMErrorMapping.EAM_0000);
        esito.setListPairAssetWonum(orderHandler.sendOrders(orderActions, requestEvent.getAsset(), policy, esito));
        return esito;
    }


    private EsitoElab handlePeriodicaPolicy(AssetData assetData, Policy policy, PolicyRequestEvent requestEvent, Activity activity) 
            throws DataLayerException, EAMException 
    {
        log.info("  - Handling PERIODICA policy");      
        if (notNull(requestEvent.getAggregationLevel()))
        {
        	if(assetDataService.checkIfIsStazione(assetData.getAsset().getAssetNum())) {
        		return aggregateStazioneHandler.manageAggregateStazione(requestEvent, policy, assetData);
        	} else {
        		boolean esito = aggregateCondFuneTelHandler.manageAggregate(requestEvent, policy, assetData);
                aggregateCondFuneTelHandler.sendWorkerFeedbackEvent(requestEvent, esito);
                return new EsitoElab(EAMErrorMapping.EAM_0004);
        	}
        }

        List<Order> periodicOrders = orderHandler.createPeriodicOrders(assetData, policy, activity);

        if (trainatoTrainanteHandler.hasTrainatoTrainante(assetData, activity.getClassificationId())) 
        {
            List<String> trainante = trainatoTrainanteHandler.checkIsTowingTowed(requestEvent, assetData, periodicOrders);
            if (isNotEmpty(trainante))
            {
                trainatoTrainanteHandler.sendWorkerFeedbackEvent(requestEvent, periodicOrders);
                return new EsitoElab(EAMErrorMapping.EAM_0004);
            }
        }

        if (trunkIspectionHandler.hasTrunkInspectionOrder(assetData, policy)) 
        {
            periodicOrders = trunkIspectionHandler.updateTrunkIspection(requestEvent, assetData.getAsset(), policy, periodicOrders);
            trunkIspectionHandler.sendWorkerFeedbackEvent(requestEvent, periodicOrders);
            return new EsitoElab(EAMErrorMapping.EAM_0004);
        }

        Optional<List<Order>> opZonizzazioneOdl = zonizzazioneHandler.handleZonizzazioneOdl(policy, assetData, activity.getJpnum(), periodicOrders);
        List<Order> zonizzazioneOdlList         = null;
        if (opZonizzazioneOdl.isPresent())
            zonizzazioneOdlList = opZonizzazioneOdl.get();
        
        if (isNotEmpty(periodicOrders) && isNotEmpty(zonizzazioneOdlList))
            periodicOrders.addAll(zonizzazioneOdlList);
        
        log.info("  - Sending PERIODICA orders");
        EsitoElab esito = new EsitoElab(EAMErrorMapping.EAM_0000);
        esito.setListPairAssetWonum(orderHandler.sendOrders(new OrderActions(periodicOrders), requestEvent.getAsset(), policy, esito));        
        return esito;
    }


    private void notifyManager(PolicyRequestEvent event, Policy policy, AssetData assetData, EsitoElab esito)
    {
        if (trunkIspectionHandler.hasTrunkInspectionOrder(assetData, policy))
        {
            x4DssWorkedHistoryQueryService.saveTroncoNum(Long.parseLong(event.getDssWorkerId()));
            
            boolean notifyStream = (!esito.getEsito().equals(EAMErrorMapping.EAM_0004));
            
            if (notifyStream)
                sendWorkerFeedbackEvent(event, null);
        } else if (isNull(event.getAggregationLevel()) && trainatoTrainanteHandler.hasTrainatoTrainante(assetData, event.getEvent())) {
            
            boolean notifyStream = (!esito.getEsito().equals(EAMErrorMapping.EAM_0004));
            
            if (notifyStream)
                sendWorkerFeedbackEvent(event, null);
            
        } else if (notNull(event.getAggregationLevel()) && Constants.ASSET_LEVEL_1.equals(event.getAggregationLevel())) {
            
            boolean notifyStream = (!esito.getEsito().equals(EAMErrorMapping.EAM_0004));
            
            if (notifyStream)
                sendWorkerFeedbackEvent(event, null);
        }
    }
    
    private boolean checkOrBypassAssetStatus(Policy policy, AssetData assetData, PolicyRequestEvent event, Activity activity) throws DataLayerException, EAMException  {
    	if (notNull(event.getAggregationLevel()) 
    	    	&& assetDataService.checkIfIsStazione(assetData.getAsset().getAssetNum())) {
            log.debug(" - Policy {} aggregata stazione: bypass status!", policy.getId());
            return true;
        } 
    	return assetDataService.checkAssetStatus(assetData, activity);
    }


    private EsitoElab processPolicyType(Policy policy, AssetData assetData, PolicyRequestEvent event) 
	        throws DataLayerException, EAMException 
	{
        Activity activity  = activityHandler.getActivityByMacroActivity(assetData, policy);
	    if (activity == null) 
	    {  
	        log.warn("No activity found related to macroactivity: {}. Possible catalog configuration error!", policy.getMacroActivityId());  
	        return new EsitoElab(EAMErrorMapping.EAM_0002);  
	    }    	
	    
	    boolean suitable = checkOrBypassAssetStatus(policy, assetData, event, activity);
	    
	    if (!suitable) 
	    {
	        log.info("  - Activity not suitable for policy '{}'", policy.getId());
	        return new EsitoElab(EAMErrorMapping.EAM_0003);
	    }
	    

	    if (PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy())) 
	    {
	        return handlePeriodicaPolicy(assetData, policy, event, activity);
	    } else {
	        return handleOnConditionPolicy(assetData, policy, event, activity);
	    }
	}
    
}
