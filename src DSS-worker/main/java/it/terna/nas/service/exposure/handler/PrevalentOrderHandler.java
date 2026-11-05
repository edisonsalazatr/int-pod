package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.nas.service.dal.entity.eam.ClassStructureLevel2;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.OrderActions;
import it.terna.nas.service.dal.entity.eam.WorkOrder;
import it.terna.nas.service.dal.entity.eam.X4AttPrevalenti;
import it.terna.nas.service.dal.service.AssetDataService;
import it.terna.nas.service.dal.service.ClassStructureService;
import it.terna.nas.service.dal.service.WorkOrderQueryService;
import it.terna.nas.service.dal.service.X4AttPrevalentiQueryService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Catalog.WorkOrderStatus;
import it.terna.nas.service.material.utility.Constants;
import static it.terna.nas.service.material.utility.EamEtlUtils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrevalentOrderHandler
{
    @Value("${periodicStatusDisable: false}")
    private Boolean                        periodicStatusDisable;
    
    private final AssetDataService               assetDataService;
    private final ClassStructureService          classStructureService;
	private final WorkOrderQueryService          workOrderQueryService;
	private final X4AttPrevalentiQueryService    x4AttPrevalentiQueryService;
	
	
	private List<Order> calculateAutoPrevalentOrders(Policy policy, 
    												 Order inputOrder, 
    												 List<WorkOrder> workerList)
    {
        long startTime = System.currentTimeMillis();
    	if (log.isDebugEnabled())
    	{
    		log.debug("call calculateAutoPrevalentOrders({}, {}) |{}|", policy.getId(), inputOrder.getJpnum(), startTime);
    
    		StringBuilder info = new StringBuilder();
    		
    		String infoWO = workerList.stream()
                    				.map(WorkOrder::getWonum) 
                    				.collect(Collectors.joining(Constants.SEP2));
    		
    		info.append("\nworkerList: ").append(infoWO);
    		
    		log.debug("calculateAutoPrevalentOrders: result elaboration ==> {}", info.toString());
    	}
    
    	Set<Order> resultSet = new HashSet<>();
    	List<Order> orderListActive = new ArrayList<>();
    	workerList.forEach(x -> orderListActive.add(createOrder(x)));
    	inputOrder.setPrevalent(Boolean.FALSE);
    	
    	resultSet.addAll(checkPrevalentCase3(inputOrder, orderListActive));
    	
    	List<Order> result = new ArrayList<>(resultSet);
    	
    	if (log.isDebugEnabled())
    	{
    		StringBuilder info = new StringBuilder();
    		result.stream().forEach(x-> {
    			info.append("\nwonum: ").append(x.getWonum())
    				.append(", jpnum: ").append(x.getJpnum())
    				.append(", prevalent: ").append(x.getPrevalent())
    				.append(", status: ").append(x.getStato());
    		});
    		
    		info.append("\nInputOrder: jpnum: ")
    		    .append(inputOrder.getJpnum())
    		    .append(", prevalent: ")
    		    .append(inputOrder.getPrevalent())
    		    .append(", status: ")
    		    .append(inputOrder.getStato());
    		log.debug("calculatePrevalentOrders: result elaboration ==> {}", info.toString());
    	}
    	
    	long endTime = System.currentTimeMillis();
    	log.info("end calculateAutoPrevalentOrders({}, {}) |{}|", policy.getId(), inputOrder.getJpnum(), endTime - startTime);
    	
    	return result;
    }


    private List<Order> calculatePrevalentOrders(	Policy policy, 
													Order inputOrder, 
													List<WorkOrder> workerList, 
													Map<String, List<X4AttPrevalenti>> mapAttPrevMap, 
													boolean hasSameJpnum)
	{
	    long startTime = System.currentTimeMillis();
		if (log.isDebugEnabled())
		{
			log.debug("call calculatePrevalentOrders({}, {}, {}) |{}|", policy.getId(), inputOrder.getJpnum(), hasSameJpnum, startTime);

			StringBuilder info = new StringBuilder();
			
			String infoWO = workerList.stream()
                    				.map(WorkOrder::getWonum) 
                    				.collect(Collectors.joining(Constants.SEP2));
			
			info.append("\nworkerList: ").append(infoWO);
			info.append("\nmapAttPrevMap: ");
			mapAttPrevMap.forEach((x, y) -> {
				info.append(Constants.SEP3).append(x).append(Constants.SEP4).append(" = ").append(Constants.SEP3);
				String infoPrev = y.stream()
        							.map(X4AttPrevalenti::getAttivitaPrev) 
        							.collect(Collectors.joining(Constants.SEP2));
				info.append(infoPrev).append(Constants.SEP4).append(Constants.SEP2).append("  ");
			});
			
			log.debug("calculatePrevalentOrders: result elaboration ==> {}", info.toString());
		}

		Set<Order> resultSet = new HashSet<>();
		List<Order> orderListActive = new ArrayList<>();
		workerList.forEach(x -> orderListActive.add(createOrder(x)));
		
		inputOrder.setPrevalent(Boolean.FALSE);
		String inputJpnum = inputOrder.getJpnum();
		List<Order> listOrderPrevalent = null;
		//Case 2
		if (isNotEmpty(mapAttPrevMap.get(inputJpnum))) 
		{	
			listOrderPrevalent = checkPrevalentCase2A(inputOrder, mapAttPrevMap.get(inputJpnum), orderListActive);
			resultSet.addAll(elabPrevalentOrders(policy, inputOrder, listOrderPrevalent));
		}
		
		//Case 1
		listOrderPrevalent = checkPrevalentCase1A(inputOrder, mapAttPrevMap, orderListActive);
		resultSet.addAll(elabPrevalentOrders(policy, inputOrder, listOrderPrevalent));
	
		//Case 3
		if (hasSameJpnum)
		    resultSet.addAll(checkPrevalentCase3(inputOrder, orderListActive));
		
		List<Order> result = new ArrayList<>(resultSet);
		
		if (log.isDebugEnabled())
		{
			StringBuilder info = new StringBuilder();
			result.stream().forEach(x-> {
				info.append("\nwonum: ").append(x.getWonum())
					.append(", jpnum: ").append(x.getJpnum())
					.append(", prevalent: ").append(x.getPrevalent())
					.append(", status: ").append(x.getStato());
			});
			
			info.append("\nInputOrder: jpnum: ")
			    .append(inputOrder.getJpnum())
			    .append(", prevalent: ")
			    .append(inputOrder.getPrevalent())
			    .append(", status: ")
			    .append(inputOrder.getStato());
			log.debug("calculatePrevalentOrders: result elaboration ==> {}", info.toString());
		}
		
		long endTime = System.currentTimeMillis();
		log.info("end calculatePrevalentOrders({}, {}) |{}|", policy.getId(), inputOrder.getJpnum(), endTime - startTime);
		
		return result;
	}
	

	/**
	 * Caso 2):
	 * Se jpNum associato all'ordine in ingresso ha delle attivita' prevalenti e se gli ordini attivi sono associati a queste attivita' 
	 * prevalenti dell'ordine in ingresso ==> l'ordine in ingresso e' prevalso dagli ordini attivi questi ordini saranno inseriti 
	 * nella List<OrderPrevalent> assieme ad un oggetto OrderPrevalent creato a partire dal Order inputOrder passato in ingresso. 
	 * L'attributo isPrevalent dell'oggetto OrderPrevalent permettera' di discriminare se un ordine e' prevalente rispetto all'altro.
	 *  
	 * @param jpNum
	 * @param attrPrevList
	 * @param workerList
	 * @return
	 */
	private List<Order> checkPrevalentCase2A(Order inputOrder, List<X4AttPrevalenti> attrPrevList, List<Order> orderListActive)
	{
		List<Order> result = new ArrayList<>();
		if (isNotEmpty(attrPrevList))
		{
			Set<String> attPrevAssociateList = attrPrevList.stream()
			                                                .filter(x -> isNotEmpty(x.getAttivitaPrev()) && (!x.getAttivitaPrev().equals(x.getJpNum())))
			                                                .map(X4AttPrevalenti::getAttivitaPrev)
															.collect(Collectors.toSet());
			
			List<Order>	listWo = orderListActive.stream()
												.filter(x -> attPrevAssociateList.contains(x.getJpnum()))
												.collect(Collectors.toList());
		
			if (isNotEmpty(listWo))
			{
				inputOrder.setPrevalent(Boolean.FALSE);
				for (Order wo: listWo)
				{
					wo.setPrevalent(Boolean.TRUE);
					result.add(wo);
				}
			}
		}
		
		return result;
	}


	/**
	 * Case 1): 
	 * Per ogni Ordine restituito al punto (**) sara' verificato tramite la mappa mapPrevalent se presente il jpNum dell'ordine passato 
	 * in ingresso. Se presente Se jpNum ==> L'ordine in ingresso prevale su ordini attivi, questi ordini saranno inseriti 
	 * nella List<OrderPrevalent> assieme ad un oggetto OrderPrevalent creato a partire dal Order inputOrder passato in ingresso. 
	 * L'attributo isPrevalent dell'oggetto OrderPrevalent permettera' di discriminare se un ordine e' prevalente rispetto all'altro.
	 *  
	 * @param inputOrder
	 * @param mapAttPrevMap
	 * @param orderListActive
	 * @return
	 */
	private List<Order> checkPrevalentCase1A(Order inputOrder, Map<String, List<X4AttPrevalenti>> mapAttPrevMap, List<Order> orderListActive)
	{
		List<Order> result = new ArrayList<Order>();
		for (Order wo: orderListActive)
		{
			log.debug("Check wo {}, jpnum: {}", wo.getWonum(), wo.getJpnum());
			if (isEmpty(wo.getJpnum()) || (WorkOrderStatus.NON_ESEGUITO.getCode().equals(wo.getStato())))
                continue;
			
			List<X4AttPrevalenti> listAttrPrev = mapAttPrevMap.get(wo.getJpnum());
			
			if (isNotEmpty(listAttrPrev))
			{
				long isPrevalent = listAttrPrev.stream()
												.filter(x -> x.getAttivitaPrev().equals(inputOrder.getJpnum()) && (!x.getAttivitaPrev().equals(x.getJpNum())))
												.count();
				
				if (isPrevalent > 0)
				{
					log.debug("Check wo {}, jpnum: {}, isPrevalent: {}", wo.getWonum(), wo.getJpnum(), isPrevalent);
					wo.setPrevalent(Boolean.FALSE);
					inputOrder.setPrevalent(Boolean.TRUE);
					result.add(wo);
				}
			}
		}
		
		return result;
	}

	
	/**
	 * Case 3:
	 * 
	 * Se invece l'ordine in ingresso e' associato ad una attivita' che e' gia' associata ad un ordine attivo, per determinare la prevalenza guidera' la data modello quella più vicino alla data di elaborazione. 
	 * In questo caso quindi. Come negli altri casi questi ordini saranno inseriti nella List<OrderPrevalent> assieme ad un oggetto OrderPrevalent creato a partire dal Order inputOrder passato in ingresso. 
	 * L'attributo isPrevalent dell'oggetto OrderPrevalent permettera' di discriminare se un ordine e' prevalente rispetto all'altro.
	 *  
	 * @param inputOrder
	 * @param orderListActive
	 * @return
	 */
	private List<Order> checkPrevalentCase3(Order inputOrder, List<Order> orderListActive)
	{
		List<Order> result 		= new ArrayList<>();
		Date inputOrderDate     = truncDate(inputOrder.getDataModello());
		
		if (!Constants.STATUS_OPEN.contains(inputOrder.getStato()))
		    return result;
		
		for (Order wo: orderListActive)
		{
		    if (inputOrder.getJpnum().equals(wo.getJpnum()) && notNull(inputOrder.getDataModello()) && notNull(wo.getDataModello()))
            {
                Date dataModello = truncDate(wo.getDataModello());
                if (dataModello.compareTo(inputOrderDate) <= 0) 
                {
                    inputOrder.setPrevalent(Boolean.FALSE);
                    inputOrder.setStato(WorkOrderStatus.NON_ESEGUITO.getCode());
                    inputOrder.setX4Note(Constants.ODL_PREVALSO_NOTE);
                    inputOrder.setX4OrdinePrevalente(wo.getWonum());
                    wo.setPrevalent(Boolean.TRUE);
                    break;
                } else if (inputOrderDate.compareTo(dataModello) <= 0) {
                    wo.setPrevalent(Boolean.FALSE);
                    wo.setStato(WorkOrderStatus.NON_ESEGUITO.getCode());
                    wo.setX4Note(Constants.ODL_PREVALSO_NOTE);
                    wo.setX4OrdinePrevalente(inputOrder.getWonum());
                    inputOrder.setPrevalent(Boolean.TRUE);
                    result.add(wo);
                    break;
                }
            }
		}
		
		return result;
	}


	public OrderActions checkPrevalentOrder(List<Order> inputOrders, Policy policy) 
			throws DataLayerException, EAMException
	{
		long startTime = System.currentTimeMillis();
	    log.debug("call checkPrevalentOrders(listSize={}, policyId={}) |{}|", notNull(inputOrders) ? inputOrders.size() : 0, policy.getId(), startTime);    	
	    OrderActions orderActions = new OrderActions();
    	orderActions.setOrders(new ArrayList<Order>());
    	
        if (isNull(inputOrders) || inputOrders.isEmpty()) 
        {
            log.debug("Input orders are null or empty, returning empty OrderActions.");
            return orderActions;
        }
        
        Map<String, ClassStructureLevel2> classStructureMap = classStructureService.classStructureMap();
        for (Order inputOrder : inputOrders) 
        {
            if (isNull(inputOrder) || isEmpty(inputOrder.getJpnum())) 
            {
                log.debug("Skipping order with null or empty jpnum: {}", inputOrder);
                continue;
            }
            
            orderActions.getOrders().add(inputOrder);
    	
            AssetData assetData = assetDataService.getAssetDataByAssetNum(inputOrder.getAssetnum(), PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
            ClassStructureLevel2 classStructureLevel = classStructureMap.get(assetData.getAsset().getClassStructureId());
            if (isNull(classStructureLevel))
            {
                log.warn("Unable to determine classStructure for the asset {}", inputOrder.getAssetnum());
                continue;
            }
            
            List<WorkOrder> workerList = null;
            if (classStructureLevel.getLevel() == 3 && Constants.CLASSSTRUCTURE_LINEA.equals(classStructureLevel.getType()))
                workerList = workOrderQueryService.findWorkOrderByAssetLin3AndStatus(inputOrder.getAssetnum(), List.of(WorkOrderStatus.APERTO.getCode(), WorkOrderStatus.DA_VALIDARE.getCode()));
            else if (classStructureLevel.getLevel() == 4 && Constants.CLASSSTRUCTURE_LINEA.equals(classStructureLevel.getType()))
                workerList = workOrderQueryService.findWorkOrderByAssetLin4AndStatus(inputOrder.getAssetnum(), List.of(WorkOrderStatus.APERTO.getCode(), WorkOrderStatus.DA_VALIDARE.getCode()));
            else if (classStructureLevel.getLevel() == 4 && Constants.CLASSSTRUCTURE_STAZIONE.equals(classStructureLevel.getType()))
                workerList = workOrderQueryService.findWorkOrderByAsseStaz4AndStatus(inputOrder.getAssetnum(), List.of(WorkOrderStatus.APERTO.getCode(), WorkOrderStatus.DA_VALIDARE.getCode()));
            else
                workerList = workOrderQueryService.findWorkOrderByAssetAndStatus(inputOrder.getAssetnum(), List.of(WorkOrderStatus.APERTO.getCode(), WorkOrderStatus.DA_VALIDARE.getCode()));
	    	
            List<X4AttPrevalenti> attPrevList 	= x4AttPrevalentiQueryService.findAttPrevalentiByJpNum(findAllActivities(inputOrder, workerList));
	    	
	    	boolean hasSameJpnum = (attPrevList.stream()
	    	                                   .filter(x -> isNotEmpty(x.getJpNum()) && x.getJpNum().equals(x.getAttivitaPrev()))
	    	                                   .count()) > 0;
            
	        boolean hasDifferentJpnum = (attPrevList.stream()
	    	                                        .filter(x -> isEmpty(x.getJpNum()) || (!x.getJpNum().equals(x.getAttivitaPrev())))
	    	                                        .count()) > 0;

            if (log.isDebugEnabled())
                log.debug("attPrevList ==> {}, hasSameJpnum ==> {}, hasDifferentJpnum ==> {}", attPrevList, hasSameJpnum, hasDifferentJpnum);
                                                    
	    	Map<String, List<X4AttPrevalenti>> mapAttPrevMap = new HashMap<>();
	    	if (isNotEmpty(attPrevList))
	    		mapAttPrevMap.putAll(attPrevList.stream().collect(Collectors.groupingBy(X4AttPrevalenti::getJpNum)));
	    	
	    	if (isNotEmpty(workerList) && isNotEmpty(mapAttPrevMap))
	    	{
	    	    if (hasSameJpnum && (!hasDifferentJpnum))
	    	        orderActions.getOrders().addAll(calculateAutoPrevalentOrders(policy, inputOrder, workerList));
	    	    else
	    	        orderActions.getOrders().addAll(calculatePrevalentOrders(policy, inputOrder, workerList, mapAttPrevMap, hasSameJpnum));
	    	}
        }
    	
    	long endTime = System.currentTimeMillis();
        log.info("call checkPrevalentOrders(listSize={}, policyId={}) |{}|", inputOrders.size(), policy.getId(), endTime - startTime);    	
        return orderActions;
	}
	
	
	private Order createOrder(WorkOrder wo)
	{
		Order newOrder = new Order();
		newOrder.setAssetnum(wo.getAssetNum());
		newOrder.setStato(wo.getStatus());
		newOrder.setJpnum(wo.getJpnum());
		newOrder.setX4PolicyId(wo.getX4Policyid());
		newOrder.setIdPolicy(wo.getX4Policy());
		newOrder.setParent(wo.getParent());
		newOrder.setWonum(wo.getWonum());
		newOrder.setX4Note(wo.getX4Note());
		newOrder.setTronconum(wo.getX4TroncoNum());
		newOrder.setX4MicroControllo(wo.getX4Microcontrollo());
		newOrder.setDataModello(wo.getX4DataModello());
		newOrder.setDataPianificataInizio(wo.getTargStartDate());
		newOrder.setDataPianificataFine(wo.getTargCompDate());
		newOrder.setDataMaxPosticipo(wo.getFnlConstraint());
		newOrder.setDataNonPrimaDi(wo.getSneConstraint());
		newOrder.setX4OrdinePrevalente(wo.getX4OrdinePrevalente()); 
		newOrder.setX4Trainante(wo.getX4Trainante());
		newOrder.setX4aggregati(wo.getX4Aggregati());
		newOrder.setToCreate(Boolean.FALSE);
		newOrder.setX4naturalavoro(wo.getX4naturalavoro());
		return newOrder;
	}


	private List<Order> elabPrevalentOrders(Policy policy, Order inputOrder, List<Order> listOrderPrevalent)
	{
		List<Order> result = new ArrayList<>();
		if (isEmpty(listOrderPrevalent))
			return result;
		
		Date dataModelloInput = truncDate(inputOrder.getDataModello());
		if (isNull(dataModelloInput))
		{
			log.error("Data modello errata per odl input {}.", dataModelloInput);
			return result;
		}
	
		for (Order order: listOrderPrevalent)
		{
			Date dataModelloOrder = truncDate(order.getDataModello());
			
			if (isNull(dataModelloOrder))
			{
				log.error("Data modello errata per wonum {} dataModelloOrder: {}",  order.getWonum(), dataModelloOrder);
				continue;
			}
			
			if (Boolean.TRUE.equals(inputOrder.getPrevalent())) //Case 1
			{
			    if (Boolean.TRUE.equals(periodicStatusDisable))
			        inputOrder.setStato(WorkOrderStatus.DA_VALIDARE.getCode());
			    else
			        inputOrder.setStato(PolicyType.ON_CONDITION.getCode().equals(policy.getTipoPolicy())  ? WorkOrderStatus.DA_VALIDARE.getCode() : WorkOrderStatus.APERTO.getCode()); 
				
			    order.setX4Note(Constants.ODL_PREVALSO_NOTE);
				order.setX4OrdinePrevalente(inputOrder.getWonum()); 
				if (dataModelloInput.compareTo(dataModelloOrder) < 1)
					order.setStato(WorkOrderStatus.NON_ESEGUITO.getCode());
			
				result.add(order);
				
			} else if (Boolean.TRUE.equals(order.getPrevalent())) {
				
			    if (dataModelloOrder.compareTo(dataModelloInput) < 1)
			        inputOrder.setStato(WorkOrderStatus.NON_ESEGUITO.getCode());
				
				inputOrder.setX4Note(Constants.ODL_PREVALSO_NOTE);
				inputOrder.setX4OrdinePrevalente(order.getWonum());
			}
		}
		
		return result;
	}


	private List<String> findAllActivities(Order inputOrder, List<WorkOrder> workerList)
	{
		Set<String> activiesSet = new HashSet<>();
		String jpnum = inputOrder.getJpnum();
		
		if (isNotEmpty(jpnum))
			activiesSet.add(jpnum);
		
		if (isNotEmpty(workerList))
		{
			List<String> jpNumList = workerList.stream()
											.filter(x -> isNotEmpty(x.getJpnum()))
											.map(WorkOrder::getJpnum)
											.collect(Collectors.toList());
			
			activiesSet.addAll(jpNumList);
		}
		
		List<String> result = new ArrayList<String>(activiesSet);
		
		log.debug("findAllActivities ==> {}", result);
		return result;
	}
}