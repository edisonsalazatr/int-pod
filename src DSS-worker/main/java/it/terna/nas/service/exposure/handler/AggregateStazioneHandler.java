package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.Activity;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.OrderActions;
import it.terna.nas.service.dal.entity.eam.X4PolicyAggregateKP;
import it.terna.nas.service.dal.service.AssetDataService;
import it.terna.nas.service.dal.service.PolicyAggregateKPService;
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
import it.terna.nas.service.material.utility.Catalog.WorkOrderStatus;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AggregateStazioneHandler extends AbstractDssHandler
{
    private static final Set<String> STATI_ELEGGIBILI =  Set.of(WorkOrderStatus.APERTO.getCode(), WorkOrderStatus.SCHEDULABILE.getCode(), WorkOrderStatus.SCHEDULATO.getCode(), WorkOrderStatus.INOLTRATO.getCode(), WorkOrderStatus.AVVIATO.getCode(), WorkOrderStatus.ESEGUITO_PAR.getCode());
    
    @Autowired
    private ActivityHandler activityHandler;
    
    @Autowired
    private AssetDataService assetDataService;

    @Autowired
    private OrderHandler orderHandler;
    
    @Autowired
    private X4PolicyService policyService;
    
    @Autowired
    private PrevalentOrderHandler prevalentOrderHandler;
    
    @Autowired
    private WorkOrderQueryService workOrderQueryService;
    
    @Autowired
    private PolicyAggregateKPService policyAggregateKPService;
    
    public EsitoElab manageAggregateStazione(PolicyRequestEvent event, Policy policy, AssetData assetData) 
            throws EAMRetryException 
    {
    	log.info("  - manageAggregateStazione ... policy={}, assetnum={}", policy.getId(), assetData.getAsset().getAssetNum());
    	EsitoElab esitoElab = new EsitoElab();
    	
        try {
            if (notNull(event.getAggregationLevel()) && Objects.equals(event.getAggregationLevel(), Integer.valueOf(3))) 
            {
                esitoElab = manageAggregate(event, policy, assetData);
            }

        } catch (DataLayerException l) {
            esitoElab.setEsito(EAMErrorMapping.EAM_9999);
            esitoElab.setDescription(l.getMessage());
            log.error(l.getMessage(), l);
            throw new EAMRetryException(esitoElab.getEsito().getEamCode(), esitoElab.getDescription(), l);
        } catch (EAMException q) {
            esitoElab.setEsito(EAMErrorMapping.findCausaleEsito(q.getErrorCode()));
            esitoElab.setDescription(q.getMessage());
            log.error(q.getMessage(), q);
            if (q instanceof EAMRetryException)
                throw (EAMRetryException) q;
        } finally {
            updateStatus(event.getDssWorkerId(), event.getCorrelationId(), esitoElab);
        }

        return esitoElab;
    }
    

    private EsitoElab manageAggregate(PolicyRequestEvent event, Policy policy, AssetData rootAssetData) 
            throws DataLayerException, EAMException
    {
    	
    	List<String> classStructureIds = policyService.getClassStructureIds(policy);
    
    	List<String> listAssetnum = assetDataService.getAssetnumByParentAndClassStructureIds(rootAssetData.getAsset().getAssetNum(), 
    																						 classStructureIds);
    	
    	log.info(" -- Retrieved {} children assets for {}", Assorted.isEmpty(listAssetnum)
    														? 0
    														: listAssetnum.size(),
    														rootAssetData.getAsset().getAssetNum());
    	
    	var kpStallo = policyAggregateKPService.getKpByTipologiaAsset(rootAssetData.getAsset().getClassificationId());
    	
    	BigDecimal kStallo = listAssetnum.stream()
    									 .map(assetnum -> getAsset(assetnum, 
    										  					   Assorted.equals(PolicyType.PERIODICA.getCode(), policy.getTipoPolicy())))
    									 .filter(x -> x != null)
    									 .map(data -> calculateKIVLelel(rootAssetData, data, kpStallo.getKp()))
    									 .filter(x -> x != null)
    									 .reduce(BigDecimal.ZERO, BigDecimal::add);
    	
    	BigDecimal sElegStallo = kpStallo.getSEleg();
    	
    	log.debug("sElegStallo: {}, kStallo: {}", sElegStallo, kStallo);
    	
    	// se kstallo > sElegStallo
    	EsitoElab result = kStallo.compareTo(sElegStallo) > 0
    						? saveOdL(event)
    						: new EsitoElab(EAMErrorMapping.EAM_0021);
    	
        return result;
    }
    
    private AssetData getAsset(String assetnum, boolean slim) {
    	AssetData assetData = null;
    	
    	log.debug("  --- Loading asset {}, slim={}", assetnum, slim);
    	try {
    		assetData = assetDataService.getAssetDataByAssetNum(assetnum, slim);
    	} catch (EAMException e) {
    		log.error(" - Error loading asset {}", assetnum, e);
		}
    	
    	return assetData;
    }
    
    
    
    private BigDecimal calculateKIVLelel(AssetData assetStallo, 
    									AssetData assetData, 
    									BigDecimal kpStallo) {
    	
    	boolean eleggibile = assetData.getAttivita()
    	                              .stream()
    	                              .anyMatch(x -> Assorted.equals(Constants.REPLACEMENT_ACTIVITY, x.getTipologiaAttivita()) && STATI_ELEGGIBILI.contains(x.getStatus()));
    	
    	X4PolicyAggregateKP kpAsset = policyAggregateKPService.getKpByTipologiaAsset(assetData.getAsset().getMacroComponente());
    	
    	BigDecimal k = kpAsset != null
    					? kpAsset.getKp().divide(kpStallo, 8, RoundingMode.HALF_EVEN)
    					: null;
    	
    	log.debug(" --- K IV livello for assetnum='{}' is='{}'. Eleggibile: {}", assetData.getAsset().getAssetNum(), k, eleggibile);
    	
    	if(k != null && (!eleggibile)) 
    	{
    		
    		log.debug(" --- K IV livello for assetnum='{}' eleggibile...", assetData.getAsset().getAssetNum());
    		
    		var paramEleg = kpAsset.getParamEleg();
    		var sEleg     = kpAsset.getSEleg();
    		
    		if(Assorted.notNull(paramEleg, sEleg)) {
    			k = k.multiply(paramEleg.divide(sEleg, 8, RoundingMode.HALF_EVEN));
    			log.debug(" --- calculateKIVLevel assetnum='{}', kp='{}', kpStallo='{}', paramEleg='{}', sEleg='{}'", assetData.getAsset().getAssetNum(),
    																	  kpAsset.getKp(),
    																	  k,
    																	  kpStallo,
    																	  paramEleg,
    																	  sEleg);
    		} else {
    			k = null;
    			log.debug(" --- calculateKIVLevel assetnum='{}', kp='{}', kpStallo='{}', paramEleg='{}', sEleg='{}'", assetData.getAsset().getAssetNum(),
						  kpAsset.getKp(),
						  k,
						  kpStallo,
						  paramEleg,
						  sEleg);
    		}
    	}
    		
    	return k;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public EsitoElab saveOdL(PolicyRequestEvent event) throws EAMException, DataLayerException
    {
        EsitoElab esito       = new EsitoElab();
        esito.setEsito(EAMErrorMapping.EAM_0000);
        
        Policy policy               = policyService.loadPolicy(event.getPolicy());
        List<String> classStructureIds = policyService.getClassStructureIds(policy);
        List<String> listAssetnum = assetDataService.getAssetnumByParentAndClassStructureIds(event.getAsset(), 
        																					 classStructureIds);
        esito = saveOdL(policy, listAssetnum, event);
           
        return esito;
    }

    
    private EsitoElab saveOdL(Policy policy, List<String> children, PolicyRequestEvent event) 
            throws DataLayerException, EAMException
    {
        List<Order> listOrder = new ArrayList<>();
        EsitoElab esito       = new EsitoElab();
        esito.setEsito(EAMErrorMapping.EAM_0000);


        if (isNotEmpty(children))
        {
            for(String assetChild: children)
            {
                AssetData assetData = assetDataService.getAssetDataByAssetNum(assetChild, PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
                Activity activity   = activityHandler.getActivityByMacroActivity(assetData, policy);
                Order order         = orderHandler.createOrder(assetData, policy, activity); 
                if (notNull(order))
                    listOrder.add(order);
            }
            
            if (isNotEmpty(listOrder))
            {    
                Order orderFirst        = listOrder.get(0);
                AssetData assetStazione = assetDataService.getAssetDataByAssetNum(event.getAsset(), PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
                
                // Controllo se ordine già è presente
                String existingWonum = workOrderQueryService.getWonumByPolicyAssetAndJpnum(
                        policy.getId(),
                        orderFirst.getJpnum(),
                        assetStazione.getAsset().getAssetNum()
                );
                
                
                if (isEmpty(existingWonum))
                {
                    Order rootOrder = new Order(orderFirst);
                    rootOrder.setAssetnum(event.getAsset());
                    rootOrder.setParent(null);
                    rootOrder.setWonum(Constants.PREFIX_DSS_WONUM + workOrderQueryService.getSequenceForAggregate().get(0).getNextval());
                    rootOrder.setX4aggregati(1);
                    listOrder.stream().forEach(x -> x.setParent(rootOrder.getWonum()));
                    listOrder.add(rootOrder);                  
                } else {
                    listOrder.stream().forEach(x -> x.setParent(existingWonum));
                }
  
                OrderActions orderActions   = prevalentOrderHandler.checkPrevalentOrder(listOrder, policy);
                checkStatusOrderActions(orderActions);
                esito.setListPairAssetWonum(orderHandler.sendOrders(orderActions, event.getAsset(), policy, esito));
            } else {
                esito.setEsito(EAMErrorMapping.EAM_0009);
            }
            
        }  else {
            esito.setEsito(EAMErrorMapping.EAM_0009);
        }
            
        return esito;
    }
}