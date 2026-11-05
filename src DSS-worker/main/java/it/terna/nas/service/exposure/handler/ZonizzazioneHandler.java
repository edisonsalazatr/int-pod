package it.terna.nas.service.exposure.handler;

import static it.terna.nas.service.material.utility.EamEtlUtils.shouldUpdateZonizzazione;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.dal.entity.eam.AttivitaAsset;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.OrderActions;
import it.terna.nas.service.dal.service.AssetDataService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.events.PolicyRequestEvent;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.EsitoElab;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Catalog.WorkOrderStatus;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ZonizzazioneHandler extends AbstractDssHandler
{
    @Autowired
    private AssetDataService assetDataService;
    
    @Autowired
    private OrderHandler orderHandler;

    public List<Order> updateZonizzazioneTroncoAndTrainato(List<Order> listOrder, Policy policy, String correlationId) 
            throws EAMException
    {
        List<Order> result = new ArrayList<>();
        if (Assorted.isNotEmpty(listOrder))
        {
            if (shouldUpdateZonizzazione(correlationId)) {
                for (Order order: listOrder)
                {
                    AssetData assetData = assetDataService.getAssetDataByAssetNum(order.getAssetnum(), PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
                    Optional<List<Order>> handleZonizzazioneOdlList = handleZonizzazioneOdl(policy, assetData, order.getJpnum(), listOrder);
                    if (handleZonizzazioneOdlList.isPresent())
                        result.addAll(handleZonizzazioneOdlList.get());
                }          	
            }
                      
    	    if(policy.getLogicaTronco() == 1 &&
    	    		policy.getListPolicyInput() != null &&
            		policy.getListPolicyInput().stream()
                    .anyMatch(a -> a.getIdInput().equals(Constants.ATTRIBUTO_ZONIZZAZIONE))) {
    	    	listOrder.forEach(order -> {
    	            order.setX4PolicyId(policy.getId());
    	            order.setIdPolicy(policy.getCode());
    	        });	
    	    }
        }
        
        return result;
    }
    
    
    public List<Order> closedOldZonizzazioneOdl(Policy policy, AssetData assetData, String activity, List<Order> listOrders) {
        log.info("[START] closedOldZonizzazioneOdl");

        Optional<Long> optionalPolicyId = listOrders.stream()
        	    .map(Order::getX4PolicyId)
        	    .filter(Objects::nonNull)
        	    .findFirst();

        Predicate<AttivitaAsset> finalFilter = a -> {

        	    // Controllo JPNum ---
        	    String jpnum = a.getJpnum();
        	    if (!Objects.equals(jpnum, activity)) {
        	        return false;
        	    }

        	    // Controllo Status ---
        	    String status = a.getStatus();
        	    boolean statusOk =
        	        Objects.equals(status, WorkOrderStatus.APERTO.getCode()) ||
        	        Objects.equals(status, WorkOrderStatus.DA_VALIDARE.getCode());
        	    if (!statusOk) {
        	        return false;
        	    }

        	    // Controllo X4PolicyId ---
        	    Long x4policyid = a.getX4policyid();
        	    Long policyId = Optional.ofNullable(policy)
        	        .map(Policy::getId)
        	        .orElse(null);

        	    // se optionalPolicyId è presente, deve essere diverso da x4policyid
        	    // e x4policyid deve essere diverso dal policyId
        	    boolean policyCondition =
        	        optionalPolicyId.map(id -> !Objects.equals(id, x4policyid)).orElse(true)
        	        && !Objects.equals(x4policyid, policyId);

        	    if (!policyCondition) {
        	        return false;
        	    }

        	    // Controllo Date ---
        	    Date dataModello = a.getX4datamodello();
        	    Date aPartireDa = Optional.ofNullable(policy)
        	        .map(Policy::getAPartireDa)
        	        .orElse(null);

        	    if (aPartireDa != null) {
        	        // Se la policy ha una data "a partire da", allora dataModello deve essere dopo quella
        	        if (dataModello == null || !dataModello.after(aPartireDa)) {
        	            return false;
        	        }
        	    }

        	    return true;
        };

        List<Order> ordersToModify = assetData.getAttivita().stream()
            .filter(finalFilter)
            .map(this::createOrderToClose)
            .collect(Collectors.toList());

        if (ordersToModify.isEmpty()) {
            log.info("No OdL found for the same activity zonizzazione!");
        } else {
            log.info("Found {} OdL for the same activity zonizzazione. Brought OdL to NON VALIDATO status!",
                     ordersToModify.size());
        }

        log.info("[END] closedOldZonizzazioneOdl");
        return ordersToModify;
    }


	
    public Optional<List<Order>> handleZonizzazioneOdl(Policy policy, AssetData assetData, String activity, List<Order> listOrders) {
        if (Assorted.isNotEmpty(listOrders) && 
        		policy.getListPolicyInput() != null &&
        		policy.getListPolicyInput().stream()
                .anyMatch(a -> a.getIdInput().equals(Constants.ATTRIBUTO_ZONIZZAZIONE))) {
            
            List<Order> closedOrders = closedOldZonizzazioneOdl(policy, assetData, activity, listOrders);
            
            return closedOrders.isEmpty() ? Optional.empty() : Optional.of(closedOrders);
        }
        return Optional.empty();
    }


	
	private Order createOrderToClose(AttivitaAsset order)
	{
		Order newOrder = new Order();
		newOrder.setAssetnum(order.getAssetnum());
		newOrder.setStato(WorkOrderStatus.NON_ESEGUITO.getCode());
		newOrder.setJpnum(order.getJpnum());
		newOrder.setX4PolicyId(order.getX4policyid());
		newOrder.setIdPolicy(order.getX4policy());
		newOrder.setParent(order.getParent());
		newOrder.setWonum(order.getWonum());
		newOrder.setX4Note(order.getX4Note());
		newOrder.setTronconum(order.getX4TroncoNum());
		newOrder.setX4MicroControllo(order.getX4Microcontrollo());
		newOrder.setDataModello(order.getX4datamodello());
		newOrder.setDataPianificataInizio(order.getTargStartDate());
		newOrder.setDataPianificataFine(order.getTargCompDate());
		newOrder.setX4OrdinePrevalente(order.getX4OrdinePrevalente()); 
		newOrder.setDataMaxPosticipo(order.getFnlConstraint());
		newOrder.setDataNonPrimaDi(order.getSneConstraint());
		newOrder.setX4Trainante(order.getX4Trainante());
		newOrder.setToCreate(false);
		newOrder.setX4outsourcing(order.getX4outsourcing());
		return newOrder;
	}
	
	public void closeOldOrder(List<Order> orders, PolicyRequestEvent requestEvent, Policy policy) throws EAMException 
	{
	    OrderActions orderActions = new OrderActions();
	    orderActions.setOrders(orders);
	    orderHandler.sendOrders(orderActions, requestEvent.getAsset(), policy, new EsitoElab());
	}
}
