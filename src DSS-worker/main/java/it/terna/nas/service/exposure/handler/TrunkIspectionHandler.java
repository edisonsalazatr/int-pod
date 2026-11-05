package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.CLASS_CAMPATA;
import static it.terna.nas.service.material.utility.Constants.CLASS_TRATTA_CAVO;
import static it.terna.nas.service.material.utility.Constants.STATI_ELEGGIBILI;
import static it.terna.nas.service.material.utility.Constants.STATI_SCHEDULATI;
import static it.terna.nas.service.material.utility.EamEtlUtils.extractYear;
import static it.terna.nas.service.material.utility.KafkaUtility.getCorrelationIdTronco;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import io.lettuce.core.RedisException;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.Assetspec;
import it.terna.nas.service.dal.entity.eam.ClassStructure;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.OrderActions;
import it.terna.nas.service.dal.entity.eam.WorkOrder;
import it.terna.nas.service.dal.entity.eam.X3Tronchi;
import it.terna.nas.service.dal.entity.eam.X4DssOrders;
import it.terna.nas.service.dal.repository.WorkOrderRepository;
import it.terna.nas.service.dal.repository.X4DssOrdersRepository;
import it.terna.nas.service.dal.service.AssetDataService;
import it.terna.nas.service.dal.service.AssetspecService;
import it.terna.nas.service.dal.service.ClassStructureService;
import it.terna.nas.service.dal.service.WorkOrderQueryService;
import it.terna.nas.service.dal.service.X3TronchiService;
import it.terna.nas.service.dal.service.X4PolicyService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.PolicyRequestEvent;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.EsitoElab;
import it.terna.nas.service.material.dto.TroncoInfo;
import it.terna.nas.service.material.dto.policy.AttributesDTO;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.Catalog.VeroOrFalso;
import it.terna.nas.service.material.utility.Catalog.WorkOrderStatus;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrunkIspectionHandler extends AbstractDssHandler
{
	@Autowired
	private AssetspecService assetspecService;
	
    @Autowired 
	private ClassStructureService classStructureService;
	   
    @Autowired
    private OrderHandler orderHandler;
    
    @Autowired
    private X4PolicyService policyService;
    
    @Autowired
    private PrevalentOrderHandler prevalentOrderHandler;
    
	@Autowired
	private X3TronchiService x3TronchiService;
	
	@Autowired
    private X4DssOrdersRepository x4DssOrdersRepository; 
    
    @Autowired
    private WorkOrderQueryService workOrderQueryService;

    @Autowired
    private WorkOrderRepository workOrderRepository;
    
    @Autowired
    private ZonizzazioneHandler zonizzazioneHandler;
    
    @Autowired
	private AssetDataService assetDataService;
    
    
    private List<Order> filterOrders(List<Order> listOrders, Policy policy)
    {
        List<Order> results         = new ArrayList<>();
        List<Order> listPolicy      = listOrders.stream().filter(x -> policy.getCode().equals(x.getIdPolicy())).toList();
        List<Order> listNoPolicy    = listOrders.stream().filter(x -> (!policy.getCode().equals(x.getIdPolicy()))).toList();
        List<Order> toCheckDb       = new ArrayList<>();
        results.addAll(listNoPolicy);

        if (isNotEmpty(listPolicy))
        {
            String nonEseguitoStatus = WorkOrderStatus.NON_ESEGUITO.getCode();
            for (Order x: listPolicy)
            {
                if (Boolean.TRUE.equals(x.getToCreate()))
                {
                    if (nonEseguitoStatus.equals(x.getStato()))
                        results.add(x);
                    else
                        toCheckDb.add(x);
                    
                } else
                    results.add(x);
            }
        }
        
        if (isNotEmpty(toCheckDb))
        {
            Set<String> assets      = new HashSet<>();
            Set<String> jpnums      = new HashSet<>();
            String troncoNum        = null;
            Date x4DataModello      = null;
            
            for (Order x: toCheckDb)
            {
                if (isNotEmpty(x.getAssetnum()))
                    assets.add(x.getAssetnum());

                if (isNotEmpty(x.getJpnum()))
                    jpnums.add(x.getJpnum());

                if (isNotEmpty(x.getTronconum()))
                    troncoNum = x.getTronconum();

                if (notNull(x.getDataModello()))
                    x4DataModello = x.getDataModello();
            }
            
            List<WorkOrder> workOrdersFind = workOrderRepository.findWorkOrderForTronco(List.copyOf(STATI_ELEGGIBILI), 
                                                                                        List.copyOf(assets), 
                                                                                        List.copyOf(jpnums), 
                                                                                        policy.getId(), 
                                                                                        x4DataModello, 
                                                                                        troncoNum);
            
            if (isEmpty(workOrdersFind))
                results.addAll(toCheckDb);
            else {
                Set<String> assetsFind = workOrdersFind.stream().map(WorkOrder::getAssetNum).collect(Collectors.toSet());
                List<Order> orderToAdd = new ArrayList<>();
                for (Order x: toCheckDb) 
                {
                    if (!assetsFind.contains(x.getAssetnum()))
                        orderToAdd.add(x);
                }
                
                if (isNotEmpty(orderToAdd))
                {
                    Order root = orderToAdd.stream().filter(x -> isEmpty(x.getParent())).findFirst().orElse(null);
                    if (isNull(root))
                    {
                        WorkOrder rootWO = workOrdersFind.stream().filter(x -> isNotEmpty(x.getX4TroncoNum())).findFirst().orElse(null);
                        if (notNull(rootWO))
                        {
                            orderToAdd.forEach(x -> x.setParent(rootWO.getWonum()));
                            results.addAll(orderToAdd);
                        }
                        
                    } else
                        results.addAll(orderToAdd);
                }
            }
        }
        
        return results;
    }


    public boolean hasTrunkInspectionOrder(AssetData assetData, Policy policy)
	{
	    boolean esito = false;
	    if (notNull(assetData) && notNull(policy))
        {
    	    String assetNum  = assetData.getAsset().getAssetNum();
    	    String assetType = assetData.getAsset().getClassStructureId();
    	    
            log.debug("call hasTrunkInspectionOrder({}, {}, {})", assetNum, assetType, policy.getId());
    	    ClassStructure classCampata      = classStructureService.getByClassification(CLASS_CAMPATA);
            ClassStructure classTrattaCavo   = classStructureService.getByClassification(CLASS_TRATTA_CAVO);
            boolean isMatchingClassStructure = assetType.equals(classCampata.getClassStructureId()) || assetType.equals(classTrattaCavo.getClassStructureId());
            boolean hasPolicyInputLinTronco  = notNull(policy.getLogicaTronco()) && policy.getLogicaTronco() == 1;
            
            esito = isMatchingClassStructure && hasPolicyInputLinTronco;
    
            log.debug("end hasTrunkInspectionOrder({}, {}, {}) ==> {}", assetNum, assetType, policy.getId(), esito);
        } else
            log.warn("assetData or policy is null");
        
	    return esito;
	}
	
    
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<Order> updateTrunkIspection(PolicyRequestEvent requestEvent, Asset asset, Policy policy, List<Order> listOrders) 
	        throws DataLayerException 
	{
	    log.info("call updateTrunkIspection({}, {}, {}, {})", requestEvent.getDssWorkerId(), asset.getAssetNum(), policy.getId(), listOrders.size());

	    if (isEmpty(listOrders))
	        return listOrders;
	    
	    String troncoNumParams   = null;
	    AttributesDTO attributes = requestEvent.getAttributes();
	    if (notNull(attributes))
	        troncoNumParams = attributes.getTroncoNum();
	    
        List<TroncoInfo> listInfoTronco = new ArrayList<TroncoInfo>();
        List<String> troncoNums         = assetspecService.getTroncoByAssetnum(asset.getAssetNum(), troncoNumParams)
                                                          .stream()
                                                          .map(Assetspec::getTableValue)
                                                          .distinct()
                                                          .collect(Collectors.toList());
        if (isNotEmpty(troncoNums))
        {
            for (String troncoNum : troncoNums)
            {
                X3Tronchi troncoInf = x3TronchiService.getX3TronchiByTronconum(troncoNum).stream().findFirst().orElse(null);
                        
                if (notNull(troncoInf))
                {
                    TroncoInfo info = new TroncoInfo();
                    info.setLinea(troncoInf.getAssetnum());
                    info.setSapId(troncoInf.getSapid());
                    info.setX3TroncoId(troncoInf.getX3Tronchiid());
                    info.setTroncoNum(troncoNum);
                    listInfoTronco.add(info);
                }
            }
            
        } else {
            log.info("No tronco d'ispezione per l'asset {}", asset.getAssetNum());
            //nessun ordine dev'essere creato in quanto non è presente il tronco d'ispezione
            return new ArrayList<>();
        }
            
        
        log.debug("listInfoTronco {}", listInfoTronco);
        
        if (isNotEmpty(listInfoTronco)) 
        {
            listOrders = filterOrdersByExistingWorkorders(requestEvent, listOrders, listInfoTronco);
        }
	    
	    saveDbOrders(requestEvent, listOrders);
	    log.debug("Completed updating trunk inspection. Orders to send: {}", listOrders.size());
	    return listOrders;
	}
	
    
	private void saveDbOrders(PolicyRequestEvent requestEvent, List<Order> listOrders) 
	        throws DataLayerException
    {
	    if (isNotEmpty(listOrders))
	    {
	        Long idDssWorker = Long.parseLong(requestEvent.getDssWorkerId());
	        for (Order order: listOrders)
	        {
	            if (isEmpty(order.getListInfoTronco()))
	                continue;
	            
	            if (isNotEmpty(order.getParent())) //Gestione modifica 
	            {
	                //Order aggregatore associato alla linea
	                List<X3Tronchi> listTronchi = x3TronchiService.getX3TronchiByWonumRoot(order.getParent()); 
	                if (isNotEmpty(listTronchi))
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
	                    row.setTroncoNum(listTronchi.get(0).getTronconum());
	                    row.setStatus(order.getStato());
	                    row.setX4Note(order.getX4Note());
	                    row.setSapIdAsset(listTronchi.get(0).getSapid());
	                    row.setX4Aggregate(order.getX4aggregati());
	                    row.setX4outsourcing(order.getX4outsourcing());
	                    row.setX3TronchiId(listTronchi.get(0).getX3Tronchiid());
	                    x4DssOrdersRepository.save(row);
	                }
	                
	            } else { //Fase creazione e controllare campata doppia terna
	                
	                int i = 0;
	                for (TroncoInfo tronco: order.getListInfoTronco())
                    {
	                    i++;
	                    X4DssOrders row    = new X4DssOrders();
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
                        row.setTroncoNum(tronco.getTroncoNum());
                        row.setStatus(order.getStato());
                        row.setX4Note(order.getX4Note());
                        row.setSapIdAsset(tronco.getSapId());
                        row.setX4Aggregate(order.getX4aggregati());
                        row.setX4outsourcing(order.getX4outsourcing());
	                    row.setX3TronchiId(tronco.getX3TroncoId());
	                    
	                    if (i > 1) 
	                    {
	                        row.setWonum(Constants.PREFIX_DSS_WONUM + workOrderQueryService.getSequenceForAggregate().get(0).getNextval());
	                    }
	                    
	                    x4DssOrdersRepository.save(row);
                    }
	            }
	        }
	    }
    }
	
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public void saveOdL(PolicyRequestEvent event) 
            throws EAMRetryException
    {
    	log.info(" - Start saveOdLTrunk - Correlation Id: {}", event.getCorrelationId());
        EsitoElab esito       = new EsitoElab();
        esito.setEsito(EAMErrorMapping.EAM_0000);
        esito.setTroncoNum(event.getTroncoNum());
        
        try {        
            if (isNotEmpty(event.getOrders())) 
            {
                Policy policy               = policyService.loadPolicy(event.getPolicy());
                if (checkStatusAssetAggregatore(event.getOrders(), policy)) 
                {
                	setX4RespAssetOrderFather(event.getOrders(), policy);
                    List<Order> listOrder       = new ArrayList<>();
                    listOrder.addAll(zonizzazioneHandler.updateZonizzazioneTroncoAndTrainato(event.getOrders(), policy, event.getCorrelationId()));
                    listOrder.addAll(event.getOrders());                 
                    OrderActions orderActions = null;
                    if (PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()))
                        orderActions = new OrderActions(listOrder);
                    else
                        orderActions = prevalentOrderHandler.checkPrevalentOrder(listOrder, policy);
                    
                    checkStatusOrderActions(orderActions);
                    orderActions.setOrders(filterOrders(orderActions.getOrders(), policy));
                    esito.setListPairAssetWonum(orderHandler.sendOrders(orderActions, event.getAsset(), policy, esito));
                } else {
                    esito.setEsito(EAMErrorMapping.EAM_0022);
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
            EAMErrorMapping esitoCode = esito.getEsito();
            if (notNull(esitoCode) && StatusX4Esito.KO.name().equals(esitoCode.getErrorFlag()))
                x4DssWorkedHistoryQueryService.checkTroncoNum(getCorrelationIdTronco(event.getCorrelationId()), event.getTroncoNum(), event.getPolicy().toString());
            log.info(" - End saveOdLTrunk - Esito: {}", esito != null && esito.getEsito() != null ? esito.getEsito().getEamCode() : "N/A");
        }
    }

    
    private void setX4RespAssetOrderFather(List<Order> orders, Policy policy) throws EAMException {

        Optional<Order> parentOrderOpt = orders.stream()
                .filter(o -> o.getParent() == null)
                .filter(o -> o.getTronconum() != null && !o.getTronconum().isEmpty())
                .findFirst();

        if (!parentOrderOpt.isPresent()) {
            return;
        }

        Order parentOrder = parentOrderOpt.get();

        List<Order> childOrders = orders.stream()
                .filter(o -> Objects.equals(o.getParent(), parentOrder.getWonum()))
                .filter(o -> o.getAssetnum() != null && !o.getAssetnum().isEmpty())
                .collect(Collectors.toList());

        if (childOrders.isEmpty()) {
            return;
        }

        Collections.shuffle(childOrders);

        for (Order childOrder : childOrders) {

            AssetData assetData = assetDataService.getAssetDataByAssetNum(
                    childOrder.getAssetnum(),
                    PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy())
            );

            if (assetData == null || assetData.getAsset() == null) {
                continue;
            }

            String gruppoResp = assetData.getAsset().getGruppoResponsabilePianifManuten();

            if (gruppoResp != null && !gruppoResp.trim().isEmpty()) {
                parentOrder.setX4respasset(gruppoResp);
                return;
            }
        }
    }


	private boolean checkStatusAssetAggregatore(List<Order> listOrders, Policy policy) 
            throws EAMException, DataLayerException 
    {
        Optional<Order> parentOrderOpt = listOrders.stream()
            .filter(o -> o.getParent() == null)
            .filter(o -> o.getTronconum() != null && !o.getTronconum().isEmpty())
            .findFirst();    
        
        if (parentOrderOpt.isPresent()) 
        {
            Order parentOrder       = parentOrderOpt.get();
            AssetData lineaTronco   = assetDataService.getAssetDataByAssetNum(parentOrder.getAssetnum(), PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()));
            boolean suitable        = assetDataService.checkAssetStatus(lineaTronco, parentOrder.getJpnum());
            if (suitable) 
            	return true;
            else
            	return false;
        }
        
        return true;
    }
    
    
    private List<Order> filterOrdersByExistingWorkorders(PolicyRequestEvent requestEvent,
            List<Order> listOrders,
            List<TroncoInfo> listInfoTronco) 
    {

		if (isEmpty(listOrders) || isEmpty(listInfoTronco))
			return listOrders;
		
	    String jpnum = listOrders.stream()
	            .filter(o -> Boolean.TRUE.equals(o.getToCreate()))
	            .map(Order::getJpnum)
	            .filter(Objects::nonNull)
	            .findFirst()
	            .orElse(null);

	    if (jpnum == null) 
	    {
	        listOrders.stream().forEach(x -> x.setListInfoTronco(listInfoTronco));
	        return listOrders;
	    }

	    
	    Long x4policyid = requestEvent.getPolicy();	
	    
		Map<String, Set<Integer>> troncoYearMap = new HashMap<>();
				
		for (TroncoInfo tronco : listInfoTronco) {
		
	        List<WorkOrder> existing = workOrderRepository
	                .findOdlMigratiByLineaAndStatusBeyondSchedulato(
	                        List.copyOf(STATI_SCHEDULATI),
	                        tronco.getLinea(),
	                        jpnum,
	                        x4policyid,
	                        tronco.getTroncoNum()
	                );
			
			Set<Integer> years = existing.stream()
				.map(w -> extractYear(w.getX4DataModello()))
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
			
			troncoYearMap.put(tronco.getTroncoNum(), years);
		}
		
		Iterator<Order> iterator = listOrders.iterator();
		
		while (iterator.hasNext()) 
		{
			Order order = iterator.next();
			
			if (isNotEmpty(order.getParent())) 
			{
				order.setListInfoTronco(listInfoTronco);
				continue;
			}

			List<TroncoInfo> listInfoTroncoFinal = null;
			if (isEmpty(order.getTroncoIntersection()))
                listInfoTroncoFinal = listInfoTronco;
            else {
                listInfoTroncoFinal = listInfoTronco.stream()
                                                    .filter(t -> order.getTroncoIntersection().contains(t.getTroncoNum()))
                                                    .collect(Collectors.toList());
            }
                
			
			Integer orderYear = extractYear(order.getDataModello());
			
			if (orderYear == null)
				continue;
			
			List<TroncoInfo> validTronchi = listInfoTroncoFinal.stream()
			        .filter(t -> {
			            Set<Integer> years = troncoYearMap.get(t.getTroncoNum());
			            return years == null || !years.contains(orderYear);
			        })
			        .collect(Collectors.toList());
			
			if (validTronchi.isEmpty()) {
				iterator.remove();
			} else {
				order.setListInfoTronco(validTronchi);
			}
		}
		
		return listOrders;
	}
}
