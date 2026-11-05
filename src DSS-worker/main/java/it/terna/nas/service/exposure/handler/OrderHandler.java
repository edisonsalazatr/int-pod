package it.terna.nas.service.exposure.handler;

import static it.terna.nas.service.material.utility.EamEtlUtils.addToDate;
import static it.terna.nas.service.material.utility.EamEtlUtils.calcolaDataModello;
import static it.terna.nas.service.material.utility.EamEtlUtils.dataPianificataInizio;
import static it.terna.nas.service.material.utility.EamEtlUtils.toDate;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.nas.service.configuration.properties.ApiProperties;
import it.terna.nas.service.configuration.properties.FilterProperties;
import it.terna.nas.service.dal.entity.eam.Activity;
import it.terna.nas.service.dal.entity.eam.AssetWithYearTronchi;
import it.terna.nas.service.dal.entity.eam.Assetspec;
import it.terna.nas.service.dal.entity.eam.AttivitaAsset;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.OrderActions;
import it.terna.nas.service.dal.service.AssetDataService;
import it.terna.nas.service.dal.service.AssetspecService;
import it.terna.nas.service.dal.service.DssDictionaryService;
import it.terna.nas.service.dal.service.MicroControlService;
import it.terna.nas.service.dal.service.WorkOrderQueryService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.service.RetryableMicrocontrolliService;
import it.terna.nas.service.exposure.service.RetryableWorkorderService;
import it.terna.nas.service.exposure.service.WorkorderApiService;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponseArray;
import it.terna.nas.service.exposure.ws.maximo.MicrocontrolloRequest;
import it.terna.nas.service.exposure.ws.maximo.WorkorderBulkResponse;
import it.terna.nas.service.exposure.ws.maximo.WorkorderGetResponse;
import it.terna.nas.service.exposure.ws.maximo.WorkorderGetResponse.WorkorderResponseItem;
import it.terna.nas.service.exposure.ws.maximo.WorkorderRequest;
import it.terna.nas.service.exposure.ws.maximo.WorkorderResponse;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.EsitoElab;
import it.terna.nas.service.material.dto.Task;
import it.terna.nas.service.material.dto.policy.AttributesDTO;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.PolicyInputType;
import it.terna.nas.service.material.utility.Catalog.PolicyTimeUnit;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Catalog.WorkOrderStatus;
import it.terna.nas.service.material.utility.Catalog;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EamEtlUtils;
import it.terna.nas.service.material.utility.TemporalAmountComparator;
import it.terna.nas.service.material.utility.TemporalAmountFactory;
import lombok.extern.slf4j.Slf4j;;


@Service
@Slf4j
public class OrderHandler extends AbstractHandler
{
    private static final Integer           LOGICA_TRONCO_ENABLED = 1;
    private static final Integer           LOGICA_AGGR_ENABLED   = 1;

    @Autowired
    private ApiProperties apiProperties;
    
    @Autowired
    private AssetspecService assetspecService;
    
    @Autowired
    private PeriodicOrderHandler periodicOrderHandler;
    
	@Autowired
	private DssDictionaryService dictionaryService;

	@Autowired
	private MicroControlService microControllService;

    @Autowired
	private PrevalentOrderHandler prevalentOrderHandler;

    @Autowired
	private WorkOrderQueryService workOrderQueryService;
            
    @Autowired
	private WorkorderApiService workorderApiService;
    
    @Autowired
	private AssetDataService assetDataService;
    
    @Autowired
  	private RetryableWorkorderService retryableWorkorderService;

    @Autowired
    private RetryableMicrocontrolliService retryableMicrocontrolliService;
    
    /**
	 * 
	 * @param assetData
	 * @param policy
	 * @return
	 * @throws DataLayerException 
	 */
	public Order createOrder(AssetData assetData, Policy policy, Activity activity) 
	        throws DataLayerException 
	{
		
		log.debug("  --- Starting creating order ....");
		
		Assert.notNull(assetData, "assetData is null");
		Assert.notNull(policy, "assetData is null");
        Assert.notNull(activity, "activity is null");
		
		log.debug("  --- Asset {} elegible", policy.getEleggibilita() == 1
												? "is"
												: "is not");
		
		/*
		 * eleggibile -- c'è altra attivita eleggibile? 
		 *                        |
		 *                        |_ SI ==> prendi la data ordine esistente
		 *                        |
		 *                        |_ NO ==>  prendi la data corrente
		 *                        
		 * - NON ELEGGIBILE
		 * 		|
		 * 		|_ DIFFORMITA O INTERFERENZE  
		 * 						 |
		 * 						 |_ SI prendi data rilevazione
		 * 						 |
		 * 						 |_ NO prendi data corrente
		 * 	
		 * 
		 */
		
		Order order = isElegible(policy, activity)
						? creaOrdineAssetEleggibile(assetData, policy, activity)
						: creaOrdineAssetNonEleggibile(assetData, policy, activity);
		
		return order;
	}
	
	private boolean isElegible(Policy policy, Activity activity) {
		
		boolean hasHI = policy.getInputs().stream().anyMatch(x -> x.getType() == PolicyInputType.HI);
		
		return (1 == policy.getEleggibilita())
				|| (
					hasHI 
					&& Assorted.equals(Constants.REPLACEMENT_ACTIVITY, activity.getClassificationId()));
	}
	
	/**
	 * 	
	 * A.	Se asset non eleggibile e non presente difformita/interferenza in input alla policy 
	 * data_modello = differibilita (valore campo X4POLICY.DIFFERIBILITA) + data generazione ODL (sysdate)
	 *
	 * B.	Se asset non eleggibile e presente difformita/interferenza in input alla policy allora vengono ricercati 
	 * nella consistenza asset nella mappa difformità attive o interferenza attiva quella piu' recente 
	 * (campo PRIMARILEVAZIONE delle tabelle X4INTERFSTAT e X4DIFFORSTAT) e sommando la differibilita alla data di rilevamento.
	 *
	 * @param assetData
	 * @param policy
	 * @return
	 * @throws DataLayerException 
	 */
	private Order creaOrdineAssetNonEleggibile(AssetData assetData, Policy policy, Activity activity) 
	        throws DataLayerException 
	{
		log.debug("  --- Creating order fon NON elegible asset ....");
		Order order = new Order();
		order.setToCreate(Boolean.TRUE);
		order.setWonum(Constants.PREFIX_DSS_WONUM + workOrderQueryService.getSequenceForAggregate().get(0).getNextval());
		order.setAssetnum(assetData.getAsset().getAssetNum());
		order.setX4PolicyId(policy.getId());
		order.setIdPolicy(policy.getCode());
		order.setStato(WorkOrderStatus.DA_VALIDARE.getCode());
		
		order.setJpnum(activity != null
				? activity.getJpnum()
				: null);
		
		if (policy.getLogicaTronco() == 1)
        {
		    Set<String> tronchiMancanti = getTronchiMancanti(assetData, policy, activity);
		    if (Assorted.isEmpty(tronchiMancanti)) 
		    {
		        log.info("Order already present for this policy or tronchi non esists.");
                return null;
		    }
		    
		    order.setTroncoIntersection(tronchiMancanti);
        } else {
            // Controllo se ordine già è presente
            String existingWonum = workOrderQueryService.getWonumByPolicyAssetAndJpnum(
                    policy.getId(),
                    activity.getJpnum(),
                    assetData.getAsset().getAssetNum()
            );
    
            if (existingWonum != null) 
            {
    	    	log.info("Order already present for this activity and policy!");
    	    	return null;	    	
    	    }
        }
		
		List<Date> listRilevazioni = new ArrayList<>();
		
		List<String> listIdInterferenze = Assorted.isNotEmpty(policy.getListPolicyInput())
											? policy.getListPolicyInput().stream()
			   	  									 .filter(x -> PolicyInputType.INTERFERENZA.getCode().equals(x.getTipologiaInput()))
			   	  									 .map(x -> x.getIdInput())
			   	  									 .toList()
			   	  							: new ArrayList<>();
		
		
		if(Assorted.isNotEmpty(listIdInterferenze)){
			if(assetData.getInterferenze() != null) {
				listRilevazioni.addAll(assetData.getInterferenze()
												.values()
												.stream()
												.flatMap(Collection::stream)
												.filter(x ->  x.getX4CatInterfId() != null 
													&& listIdInterferenze.contains( x.getX4CatInterfId().toString()))
												.map(x -> x.getPrimaRilevazione())
												.collect(Collectors.toList()));
			}
		}
		
		List<String> listIdDifformita = Assorted.isNotEmpty(policy.getListPolicyInput())
											? policy.getListPolicyInput().stream()
																		 .filter(x -> PolicyInputType.DIFFORMITA.getCode().equals(x.getTipologiaInput()))
																		 .map(x -> x.getIdInput())
																		 .toList()
											: new ArrayList<>();
		
		if(Assorted.isNotEmpty(listIdDifformita)) {
			if(assetData.getDifformita() != null) {
				listRilevazioni.addAll(assetData.getDifformita()
												.values()
												.stream()
												.flatMap(Collection::stream)
												.filter(x ->  x.getX4CatDifforId() != null 
																&& listIdDifformita.contains( x.getX4CatDifforId().toString()))
												.map(x -> x.getPrimaRilevazione())
												.collect(Collectors.toList()));
			}
		}
		
	
		Date lastDetection = listRilevazioni.stream()
											.filter(x -> x != null)
											.max(Comparator.naturalOrder())
											.orElse(null);
				
		LocalDateTime baseDataModello = lastDetection == null
									? LocalDateTime.now()
									: lastDetection.toInstant()
												   .atZone(ZoneId.systemDefault())
												   .toLocalDateTime();
		
		
		LocalDateTime dataModello = calcolaDataModello(baseDataModello, policy.getDifferibilita(), policy.getTimeUnit(), activity.getDuration());
		order.setDataModello(toDate(dataModello));
		
		//-	Data di max posticipo: Data modello + Massimo posticipo attività; inserito nella WORKORDER.FNLCONSTRAINT
		LocalDateTime dataMaxPosticipo = addToDate(dataModello, 
				  								policy.getMaxPosticipo(),
				  								PolicyTimeUnit.DAY);
		order.setDataMaxPosticipo(toDate(dataMaxPosticipo));
		order.setDataNonPrimaDi(EamEtlUtils.calcolaDataNonPrimaDi(dataModello, policy.getMaxAnticipo()));
		
		order.setDataPianificataFine(toDate(dataModello));
		
		//-	Data pianificata inizio: Data pianificata fine – durata pianificata;
		Date dataPianificataInizio = dataPianificataInizio(dataModello, 
																activity != null && activity.getDuration() != null
																	? activity.getDuration()
																	: BigDecimal.ZERO);
		order.setDataPianificataInizio(dataPianificataInizio);
		order.setX4outsourcing(activity.getX4Outsourcing());
		return order;
	}
	
	
	/**
	 * -	Asset eleggibile
	 * Esiste altro ordine        --> reportDate ordine precedente + differibilità policy corrente
	 * Non esiste                 --> new Date() + differibilità policy corrente

	 * @param assetData
	 * @param policy
	 * @return
	 * @throws DataLayerException 
	 */
	private Order creaOrdineAssetEleggibile(AssetData assetData, Policy policy, Activity activity) 
	        throws DataLayerException 
	{
		log.debug("  --- Creating order fon ELEGIBLE asset ....");
		Order order = new Order();
		order.setToCreate(Boolean.TRUE);
		order.setWonum(Constants.PREFIX_DSS_WONUM + workOrderQueryService.getSequenceForAggregate().get(0).getNextval());
		order.setAssetnum(assetData.getAsset().getAssetNum());
		order.setX4PolicyId(policy.getId());
		order.setIdPolicy(policy.getCode());
		order.setStato(WorkOrderStatus.DA_VALIDARE.getCode());
		
		if (policy.getLogicaTronco() == 1)
        {
            Set<String> tronchiMancanti = getTronchiMancanti(assetData, policy, activity);
            if (Assorted.isEmpty(tronchiMancanti)) 
            {
                log.info("Order already present for this policy or tronchi non esists.");
                return null;
            }
            
            order.setTroncoIntersection(tronchiMancanti);
        } else {
        
            // Controllo se ordine già è presente
            String existingWonum = workOrderQueryService.getWonumByPolicyAssetAndJpnum(
                    policy.getId(),
                    activity.getJpnum(),
                    assetData.getAsset().getAssetNum()
            );
    
            if (existingWonum != null) 
            {
    	    	log.info("Order already present for this activity and policy!");
    	    	return null;	    	
    	    }
        }
		
		Pair<LocalDateTime, LocalDateTime> pairDateEleggibilita = findDataModello(assetData, policy, activity);
		
		order.setDataModello(toDate(pairDateEleggibilita.getFirst()));
		order.setPrimaEleggibilita(toDate(pairDateEleggibilita.getSecond()));
		
		order.setDataPianificataFine(order.getDataModello());
		
		//-	Data di max posticipo: Data modello + Massimo posticipo attivita'; inserito nella WORKORDER.FNLCONSTRAINT
		LocalDateTime dataMaxPosticipo = addToDate(pairDateEleggibilita.getFirst(), 
						  					   	   policy.getMaxPosticipo(), // espresso in giorni
						  					   	   PolicyTimeUnit.DAY); 
		order.setDataMaxPosticipo(toDate(dataMaxPosticipo));
				
		/*
		 * Data non prima di: Data modello – Massimo anticipo attività; 
		 * inserito nella TARGSTARTDATE.SNECONSTRAINT
		 * 
		 * Modifica del 10/12/2025 se maxAnticipo == null ==> valorizza con data corrente.
		 */
		order.setDataNonPrimaDi(EamEtlUtils.calcolaDataNonPrimaDi(pairDateEleggibilita.getFirst(), policy.getMaxAnticipo()));
		
		//-	Data pianificata inizio: Data pianificata fine – durata pianificata;
		Date dataPianificataInizio = dataPianificataInizio(pairDateEleggibilita.getFirst(), 	
														   activity != null && activity.getDuration() != null
														   		? activity.getDuration()
																: BigDecimal.ZERO);
		order.setDataPianificataInizio(dataPianificataInizio);
		
		order.setJpnum(activity != null
						? activity.getJpnum()
						: null);
		
		order.setX4outsourcing(activity.getX4Outsourcing());
		return order;
	}
	
	private Pair<LocalDateTime, LocalDateTime> findDataModello(AssetData assetData, Policy policy, Activity activity) {
		var now = LocalDateTime.now();
		
		LocalDateTime currentDataModello = calcolaDataModello(
																now, 
																policy.getDifferibilita(),
																policy.getTimeUnit(),
																activity.getDuration()
															  );
		// check if another order with same activity exist;
		AttivitaAsset attivitaAsset = getElegibleActivity(activity.getJpnum(), assetData);
		
		if(attivitaAsset == null) {
			// no previous activity on asset, return now
			return Pair.of(currentDataModello, now);
		} 
		
		
		TemporalAmount newDifferability = TemporalAmountFactory.of(policy.getDifferibilita(), 
																   policy.getTimeUnit().getTemporalUnit());
		TemporalAmount oldDifferability = TemporalAmountFactory.of(attivitaAsset.getDifferibilita() != null
																		? attivitaAsset.getDifferibilita()
																		: 0, 
																	Assorted.isNotEmpty(attivitaAsset.getX4RifTemporale())
																		? PolicyTimeUnit.fromCode(attivitaAsset.getX4RifTemporale())
																   				 			.getTemporalUnit()
																   		: ChronoUnit.MINUTES
																   	);
		
		LocalDateTime oldDataModello =  Assorted.notNull(attivitaAsset.getX4datamodello())
																? attivitaAsset.getX4datamodello().toInstant()
																		.atZone(ZoneId.systemDefault())
																		.toLocalDateTime()
																: null;
		
		
		
		LocalDateTime dataPrimaEleggibilita  = Assorted.notNull(attivitaAsset.getPrimaEleggibilita())
													? attivitaAsset.getPrimaEleggibilita().toInstant()
															.atZone(ZoneId.systemDefault())
															.toLocalDateTime()
													: Assorted.notNull(attivitaAsset.getReportDate())
														? attivitaAsset.getReportDate().toInstant()
																	.atZone(ZoneId.systemDefault())
																	.toLocalDateTime()
														: null;
		
		LocalDateTime dataModello = null;
		TemporalAmountComparator comparator = new TemporalAmountComparator();
		if(comparator.compare(newDifferability, oldDifferability) <= 0) {
			/*
			 * Se minore (differibilita' ordine corrente < differibilità ordine precedente)
			 * sarà impostata la data modello = data modello minore tra la data modello corrente
			 * (calcolata come data corrente + differibilità corrente) e data modello precedente. 
			 * In altre parole, viene associata la data modello piu' prossima.
			 * 
			 * Chiarimento di MARZO 2026: non si deve produrre una nuova data modello nel passato
			 * le implicazioni sono: primaEleggibilita + nuova differibilita < now ==> nessun effetto, si prende la vecchia data modello
			 *                       entrambe le date sono passate ==> nessun effetto, si prende la vecchia data modello
			 *                       
			 * CR Giugno 2026: scegliere sempre la data modello piu' piccola.
			 */
			dataModello = Stream.of(currentDataModello, oldDataModello)
					  			.filter(Objects::nonNull)
				    			.min(Comparator.naturalOrder())
				    			.orElse(null);
		} else {
			/*
			 * se maggiore (differibilita' ordine corrente > differibilita ordine precedente) 
			 * sarà impostata la data modello = data generazione (data in cui sto calcolando l'ordine) + differibilita' nuova 
			 * (differibilita' della policy in fase di elaborazione) – 
			 * [anno di calcolo ordine corrente – anno in cui l'asset è diventato eleggibile la prima volta].
			 */
			if(dataPrimaEleggibilita != null) {
				Duration diff = dataPrimaEleggibilita.isBefore(now)
								? Duration.between(dataPrimaEleggibilita, now)
								: Duration.between(now, dataPrimaEleggibilita);
				dataModello = currentDataModello.minus(diff);
			} else {
				dataModello = currentDataModello;
			}
		}
		
		return Pair.of(dataModello, dataPrimaEleggibilita != null
												? dataPrimaEleggibilita
												: now);
	}
	

	 /**
     * 
     * @param activityId 
     * @return an activity if exists a workorder with jpnum=activityId and type activity = SOSTITUZIONE
     */
    private AttivitaAsset getElegibleActivity(String activityId, AssetData assetData) 
    {
    	
    	if(Assorted.isEmpty(assetData.getAttivita())) {
    		return null;
    	}
    	
    	return assetData.getAttivita().stream()
    				    .filter(activity ->  Assorted.equals(activity.getJpnum(), activityId)
    						   				  && Objects.equals(Constants.REPLACEMENT_ACTIVITY, activity.getTipologiaAttivita()))
    				    .sorted(Comparator.comparing(AttivitaAsset::getReportDate, 
    				    		Comparator.nullsLast(Comparator.reverseOrder())))    				  
    				    .findFirst()
    				    .orElse(null);
    				   
    }

    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<Pair<String, String>> sendOrders(OrderActions outputOrdersActions, String asset, Policy policy, EsitoElab esito) 
	        throws EAMException 
    {
        List<Pair<String, String>> results = new ArrayList<>();
        long startTime = System.currentTimeMillis();
	    log.debug("call sendOrders({}) |{}|", asset, startTime);
	    if (log.isDebugEnabled())
	    {
	        if (Assorted.notNull(policy))
	            log.debug("Policy {} {}", policy.getId(), policy.getCode());
	        
	        log.debug("Orders Size: {}", Assorted.isNotEmpty(outputOrdersActions.getOrders()) ? outputOrdersActions.getOrders().size() : 0);
	    }
	    
	    try {
	        
    	    if (Assorted.isNotEmpty(outputOrdersActions.getOrders())) 
    	    {
    	        // Creazione di una copia della lista originale
    	        List<Order> listOrders = outputOrdersActions.getOrders();
    
    	        // Step 1: Raccolta dei wonum
    	        List<String> listWonum  = listOrders.stream()
    	                                            .filter(x -> Boolean.FALSE.equals(x.getToCreate()))
    	                                            .map(Order::getWonum)
    	                                            .collect(Collectors.toList());
    	        
    	        log.debug("Collected {} wonum(s) for retrieval: {}", listWonum.size(), listWonum);
    	        
    	             
    	        // Step 2: Verifica e gestione parent aggregatore
    	        listOrders = prepareOrderSequence(listOrders);
    
    	        // Step 3: Recupero dei dati esistenti via API GET
    	        WorkorderGetResponse responseGetApi = null;
    	        if (Assorted.isNotEmpty(listWonum)) 
    	        {
    	            responseGetApi = recoveryHrefForUpdate(listWonum);
    	        } else {
    	            log.info("No wonum found to retrieve. Skipping API GET call.");
    	        }
    
    
    	        // Step 4: Preparazione e invio delle richieste bulk
    	        List<WorkorderRequest> ordersToSend = ordersToWorkorderRequest(listOrders, responseGetApi != null ? responseGetApi.getMember() : null);
    	        log.debug("Prepared {} WorkorderRequest(s) for sending.", ordersToSend.size());
    	        
    	        // set tipoOdl
    	        if (Assorted.isNotEmpty(policy.getTipoPolicy()) && Assorted.isNotEmpty(ordersToSend)) 
    	        {
            		ordersToSend.forEach(order -> order.setX4tipoodl(dictionaryService.getDictionary(policy.getTipoPolicy())));
    			}
    	       
    	        
    	        boolean disableTransaction = calculateDisableTransaction(policy);
    	        log.info("disableTransaction: {}", disableTransaction);
    	        // retrieve MicroControlli
		        if(assetDataService.checkIfIsStazione(asset)) 
		        {
		        	ordersToSend.forEach(order -> {
			            if (Constants.ADD.equals(order.get_action()))
			            {
			            	List<Task> microControllList = microControllService.manageMicrocontrols(order.getAssetnum(), order.getJpnum());
			            	order.setWoactivity(Assorted.isNotEmpty(microControllList) ? microControllList : null);
			            }
		            });
		        	
		        	boolean calculateSplitMicro = calculateSplitMicrocontrolli(policy);
		        	log.info("calculateSplitMicro: {}", calculateSplitMicro);
		        	if (Boolean.TRUE.equals(calculateSplitMicro))
		        	    return sendMicrocontrolliStazione(disableTransaction, ordersToSend);
		        }
		        
                
		        MaximoResponseArray<WorkorderBulkResponse> responsePostApi = retryableWorkorderService.callWorkorderApiWithRetry(disableTransaction,
		                                                                                                                         ordersToSend, 
		        																												 ordersToSend,
		        																												 new HashSet<String>());
		        
		        
	            log.info("All orders sent successfully without errors.");
	            List<WorkorderBulkResponse> responseWorkorder = responsePostApi.getResponses();
	            if (responseWorkorder != null) 
	            {
	            	 List<WorkorderResponse> responseDataList = responseWorkorder.stream()
	 	            	                                                         .map(WorkorderBulkResponse::getResponsedata) 
	 	            	                                                         .filter(Objects::nonNull)                   
	 	            	                                                         .collect(Collectors.toList()); 
	            	 
	            	 if (Assorted.isNotEmpty(responseDataList))
	            	 {
	            	     responseDataList.stream()
	            	                     .filter(x -> Assorted.isNotEmpty(x.getAssetnum()) && Assorted.isNotEmpty(x.getWonum()))
	            	                     .forEach(x -> results.add(Pair.of(x.getAssetnum(), x.getWonum())));
	            	 }
	            }
	            
	            if (Assorted.isEmpty(results))
	                throw new EAMException(EAMErrorMapping.EAM_9999.getEamCode(), "OdL not created for asset.");
	            
	            log.debug("End sendOrders.");
    	    }
    	    else 
    	    {
    	        log.info("empty list, no order creation for asset {}", asset);
    	        esito.setEsito(EAMErrorMapping.EAM_0009);
    	    }
	    
        } catch (RestException e) {
            log.error("REST exception occurred during sendOrders: {}", e.getMessage(), e);
            throw new EAMException(EAMErrorMapping.EAM_0015.getEamCode(), EAMErrorMapping.EAM_0015.getErrorDescription());
        } catch (DataLayerException dle) {
        	log.error("DataLayerException occurred during sendOrders: {}", dle.getMessage(), dle);
            throw new EAMException(EAMErrorMapping.EAM_0017.getEamCode(), EAMErrorMapping.EAM_0017.getErrorDescription(), dle);
    	} finally {
            long endTime = System.currentTimeMillis();
            log.info("end sendOrders({}) |{}|", asset, endTime - startTime);
        }
        
        return results;
    }


	private boolean calculateDisableTransaction(Policy policy)
    {
        if (Boolean.TRUE.equals(apiProperties.getDisableTransaction().getEnabled()))
        {
            FilterProperties filter = apiProperties.getDisableTransaction().getFilter();
            if (Boolean.TRUE.equals(filter.getAggregate()) && LOGICA_AGGR_ENABLED.equals(policy.getPolicyAggr()))
                return true;
            else if (Boolean.TRUE.equals(filter.getTronco()) && LOGICA_TRONCO_ENABLED.equals(policy.getLogicaTronco()))
                return true;
            else {
                Set<String> policies   = EamEtlUtils.splitByField(filter.getPolicy(),          Constants.SPLIT_SEP1);
                Set<String> tipoAssets = EamEtlUtils.splitByField(filter.getTipologiaAsset(),  Constants.SPLIT_SEP1);
                
                if (tipoAssets.contains(policy.getAssetType()) || policies.contains(policy.getCode()))
                    return true;
            }
        }
        
        return false;
    }

	
    private boolean calculateSplitMicrocontrolli(Policy policy)
    {
        if (Boolean.TRUE.equals(apiProperties.getMicrocontrolli().getEnabled()))
        {
            Set<String> policies   = EamEtlUtils.splitByField(apiProperties.getMicrocontrolli().getPolicy(),          Constants.SPLIT_SEP1);
            Set<String> tipoAssets = EamEtlUtils.splitByField(apiProperties.getMicrocontrolli().getTipologiaAsset(),  Constants.SPLIT_SEP1);
            
            if (tipoAssets.contains(policy.getAssetType()) || policies.contains(policy.getCode()))
                return true;
        }
        
        return false;
    }

	
    private WorkorderGetResponse recoveryHrefForUpdate(List<String> listWonum) 
            throws RestException, EAMException 
    {
		WorkorderGetResponse responseGetApi;
		log.info("Calling API to retrieve existing workorders.");
		responseGetApi = workorderApiService.retrieveWorkorders(listWonum);
		log.info("API GET call completed with HTTP code: {}", responseGetApi.getHttpCode());

		if (responseGetApi.getHttpCode() >= 300) 
		{
		    log.error("Error in retrieving workorders. HTTP code: {}, wonums: {}, response: {} ", responseGetApi.getHttpCode(), listWonum, responseGetApi.toJson());
		    throw new EAMException(EAMErrorMapping.EAM_0016.getEamCode(), EAMErrorMapping.EAM_0016.getErrorDescription());
		}
		
		return responseGetApi;
	}


	private List<Order> prepareOrderSequence(List<Order> listOrder) 
	{
        log.debug("call prepareOrderSequence()");
	
        List<Order> orderRoot   = listOrder.stream().filter(x -> Assorted.isEmpty(x.getParent())).collect(Collectors.toList());
        List<Order> orderChild  = listOrder.stream().filter(x -> Assorted.isNotEmpty(x.getParent())).collect(Collectors.toList());
        List<Order> result      = new ArrayList<>();
        result.addAll(orderRoot);
        result.addAll(orderChild);
        
        return result;
	}


	private List<WorkorderRequest> ordersToWorkorderRequest(List<Order> orders, List<WorkorderResponseItem> members) 
	        throws EAMException 
	{
		List<WorkorderRequest> requestOrders = new ArrayList<>();
		log.debug("Converting orders to WorkorderRequest format.");

		if (log.isDebugEnabled() && Assorted.isNotEmpty(members))
		{
		    Set<String> wonumFind = members.stream().filter(x -> Assorted.isNotEmpty(x.getWonum())).map(WorkorderResponseItem::getWonum).collect(Collectors.toSet());
		    log.debug("wonumFind: {}", wonumFind);
		}
		
		for (Order order : orders) 
		{
			WorkorderRequest request = Order.prepareWorkorderRequest(order);

			if (Assorted.notNull(members)) 
			{
			    try {
    				members.stream()
    				       .filter(member -> member.getWonum().equals(order.getWonum()))
    				       .findFirst()
    				       .ifPresentOrElse(wonumMember -> {
    							log.debug("wonum found {}: {}", order.getWonum(), wonumMember.getHref());
    							request.setHref(wonumMember.getHref());
    							request.set_action(Constants.UPDATE);
    							request.setWonum(Assorted.notNull(order.getWonum()) ? order.getWonum() : null);
    							if(request.getTargstartdate() == null) 
    							{
    								request.setTargstartdate(Assorted.dateToString(wonumMember.getTargstartdate(), Constants.MAXIMO_DATE_AND_TIME));
    							}
    							
    							if(request.getTargcompdate() == null) 
    							{
    								request.setTargcompdate(Assorted.dateToString(wonumMember.getTargcompdate(), Constants.MAXIMO_DATE_AND_TIME));
    							}
    						},
    						() -> {
    							request.setWonum(Assorted.notNull(order.getWonum()) ? order.getWonum() : null);
    							request.set_action(Constants.ADD);
    						});
    				
			    } catch (Exception e) {
			        StringBuilder data = new StringBuilder();
                    data.append("Error while matching order with API response for wonum ").append(order.getWonum());
                    data.append(". Exception message: ").append(e.getMessage());
                    data.append("\nAPI response: ").append(members.toString());
                    log.error(data.toString(), e);
                    throw new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e);
			    }
			    
			} else {
				request.setWonum(Assorted.notNull(order.getWonum()) ? order.getWonum() : null);
				request.set_action(Constants.ADD);
			}

			requestOrders.add(request);
		}

		log.debug("Completed conversion of orders to WorkorderRequest. Total requests prepared: {}", requestOrders.size());
		return requestOrders;
	}
	
	
    
    public OrderActions checkPrevalentOrder(List<Order> inputOrders, Policy policy)
            throws DataLayerException, EAMException
    {
    	return prevalentOrderHandler.checkPrevalentOrder(inputOrders, policy);
    }
    
    
    public List<Order> createPeriodicOrders(AssetData assetData, Policy policy, Activity activity) 
            throws DataLayerException 
    {
    	return periodicOrderHandler.createPeriodicOrders(assetData, policy, activity);    	
    }

	public List<Order> modifyMaxAdvanceOrMaxSnooze(AssetData assetData, Policy policy, Activity activity,
			List<AttivitaAsset> listOrders, AttributesDTO attributesDTO) {
        log.info("[START] modifyMaxAdvanceOrMaxSnooze");
        List<Order> orders = new ArrayList<>();
        boolean hasChanges = false;

		for(AttivitaAsset orderToUpdate: listOrders) {
			Order order = createOrder(orderToUpdate);
			LocalDateTime dataModello = order.getDataModello().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			if (Assorted.notNull(attributesDTO.getMaxAdvance()) &&  
				    !PolicyType.PERIODICA.getCode().equals(policy.getTipoPolicy()) ||  
				    EamEtlUtils.isOverOneYear(policy.getFrequenza(), policy.getTimeUnit())) {
				int maxAdvance = attributesDTO.getMaxAdvance().getNewValue();
				LocalDateTime notBeforeThan = addToDate(dataModello, 
						- maxAdvance, 
						PolicyTimeUnit.DAY);
				order.setDataNonPrimaDi(toDate(notBeforeThan));	
				hasChanges = true;
		    }
			if (Assorted.notNull(attributesDTO.getMaxSnooze())) {
				int maxSnooze = attributesDTO.getMaxSnooze().getNewValue();
				LocalDateTime dataMaxPosticipo = addToDate(dataModello, 
							maxSnooze,
					   	   PolicyTimeUnit.DAY); 
				order.setDataMaxPosticipo(toDate(dataMaxPosticipo));
				hasChanges = true;
			}
			
			if(!hasChanges) {
				continue;
			}
			orders.add(order);		
		}
        log.info("[END] modifyMaxAdvanceOrMaxSnooze");
		return orders;
	}

	public List<Order> modifyOnCondictionOrders(AssetData assetData, Policy policy, Activity activity,
			List<AttivitaAsset> listOrders, AttributesDTO attributesDTO) throws DataLayerException {
		
		Assert.notNull(assetData, "assetData is null");
		Assert.notNull(policy, "assetData is null");
        Assert.notNull(activity, "activity is null");
		
		log.debug("  --- Asset {} elegible", policy.getEleggibilita() == 1
												? "is"
												: "is not");
		
		List<Order> order = isElegible(policy, activity)
						? modificaOrdineAssetEleggibile(assetData, policy, activity, listOrders, attributesDTO)
						: modificaOrdineAssetNonEleggibile(assetData, policy, activity, listOrders, attributesDTO);
		
		return order;
	}
	
	private List<Order> modificaOrdineAssetNonEleggibile(AssetData assetData, Policy policy, Activity activity, List<AttivitaAsset> listOrders, AttributesDTO attributesDTO) 
	        throws DataLayerException 
	{
		log.debug("[START] modificaOrdineAssetNonEleggibile");
		List<Order> orderToCreate = new ArrayList<>();
		for(AttivitaAsset orderToModify: listOrders) {
			Order order = createOrder(orderToModify);
			List<Date> listRilevazioni = new ArrayList<>();
			
			
			List<String> listIdInterferenze = Assorted.isNotEmpty(policy.getListPolicyInput())
												? policy.getListPolicyInput().stream()
																			 .filter(x -> PolicyInputType.INTERFERENZA.getCode().equals(x.getTipologiaInput()))
																			 .map(x -> x.getIdInput())
																			 .toList()
												: new ArrayList<>();


			if(Assorted.isNotEmpty(listIdInterferenze)){
				if(assetData.getInterferenze() != null) {
					listRilevazioni.addAll(assetData.getInterferenze()
								   .values()
								   .stream()
								   .flatMap(Collection::stream)
								   .filter(x -> x.getX4CatInterfId() != null
								    		    && listIdInterferenze.contains(x.getX4CatInterfId().toString())) 
								   .map(x -> x.getPrimaRilevazione())
								   .collect(Collectors.toList()));
				}
			}

			List<String> listIdDifformita = Assorted.isNotEmpty(policy.getListPolicyInput())
												? policy.getListPolicyInput().stream()
														 .filter(x -> PolicyInputType.DIFFORMITA.getCode().equals(x.getTipologiaInput())) 
														 .map(x -> x.getIdInput())
														 .toList()
												: new ArrayList<>();

			if(Assorted.isNotEmpty(listIdDifformita)) {
				if(assetData.getDifformita() != null) {
					listRilevazioni.addAll(assetData.getDifformita()
								   .values()
								   .stream()
								   .flatMap(Collection::stream)
								   .filter(x -> x.getX4CatDifforId() != null
								   				&& listIdDifformita.contains(x.getX4CatDifforId().toString())) 
								   .map(x -> x.getPrimaRilevazione())
								   .collect(Collectors.toList()));
				}
			}
		
			Date lastDetection = listRilevazioni.stream()
												.filter(x -> x != null)
												.max(Comparator.naturalOrder())
												.orElse(null);
					
			LocalDateTime baseDataModello = lastDetection == null
										? orderToModify.getReportDate().toInstant()
												.atZone(ZoneId.systemDefault()).toLocalDateTime()
										: lastDetection.toInstant()
														.atZone(ZoneId.systemDefault()).toLocalDateTime();
			
			/**
			 * CR Giugno: non ha piu' importanza se la data modello risulta passata, rimosso il check successivo e relativo ricalcolo con base  LocalDateTime.now()
			 */
			LocalDateTime dataModello = calcolaDataModello(baseDataModello, 
							Assorted.notNull(attributesDTO.getDeferrability()) ? attributesDTO.getDeferrability().getNewValue() : policy.getDifferibilita(), 
							Assorted.notNull(attributesDTO.getRifTemporale()) ? PolicyTimeUnit.fromCode(attributesDTO.getRifTemporale().getNewValue()) : policy.getTimeUnit(),
							activity.getDuration());
			order.setDataModello(toDate(dataModello));
			
			LocalDateTime dataMaxPosticipo = addToDate(dataModello, 
					  								policy.getMaxPosticipo(),
					  								PolicyTimeUnit.DAY);
			order.setDataMaxPosticipo(toDate(dataMaxPosticipo));
			
			LocalDateTime notBeforeThan = addToDate(dataModello, 
						- policy.getMaxAnticipo(),
						PolicyTimeUnit.DAY);
			order.setDataNonPrimaDi(toDate(notBeforeThan));
			
			order.setDataPianificataFine(toDate(dataModello));
			
			Date dataPianificataInizio = dataPianificataInizio(dataModello, 
																	activity != null && activity.getDuration() != null
																		? activity.getDuration()
																		: BigDecimal.ZERO);
			order.setDataPianificataInizio(dataPianificataInizio);
			orderToCreate.add(order);
			
		}
		log.debug("[END] modificaOrdineAssetNonEleggibile");
		return orderToCreate;
	}
	
	private List<Order> modificaOrdineAssetEleggibile(AssetData assetData, Policy policy, Activity activity, List<AttivitaAsset> listOrders, AttributesDTO attributesDTO) 
	        throws DataLayerException 
	{
		log.debug("[START] modificaOrdineAssetEleggibile");
		List<Order> orderToCreate = new ArrayList<>();
		for(AttivitaAsset orderToModify: listOrders) {
			Order order = createOrder(orderToModify);
			
			Pair<LocalDateTime, LocalDateTime> pairDateEleggibilita = findDataModello(assetData, policy, activity);
			LocalDateTime dataModello = pairDateEleggibilita.getFirst();
			
            if (pairDateEleggibilita.getFirst().isBefore(LocalDateTime.now())) {
                dataModello = calcolaDataModello(LocalDateTime.now(), policy.getDifferibilita(), policy.getTimeUnit(), activity.getDuration());;
            }
			order.setDataModello(toDate(dataModello));
			
			order.setDataPianificataFine(order.getDataModello());
			
			//-	Data di max posticipo: Data modello + Massimo posticipo attività; inserito nella WORKORDER.FNLCONSTRAINT
			LocalDateTime dataMaxPosticipo = addToDate(dataModello, 
							  					   	   policy.getMaxPosticipo(), // espresso in giorni
							  					   	   PolicyTimeUnit.DAY); 
			order.setDataMaxPosticipo(toDate(dataMaxPosticipo));
					
			//-	Data non prima di: Data modello – Massimo anticipo attività; inserito nella TARGSTARTDATE.SNECONSTRAINT
			LocalDateTime notBeforeThan = addToDate(dataModello, 
						- policy.getMaxAnticipo(), // espresso in giorni
						PolicyTimeUnit.DAY);
			order.setDataNonPrimaDi(toDate(notBeforeThan));		
			
			//-	Data pianificata inizio: Data pianificata fine – durata pianificata;
			Date dataPianificataInizio = dataPianificataInizio(dataModello, 	
															   activity != null && activity.getDuration() != null
															   		? activity.getDuration()
																	: BigDecimal.ZERO);
			order.setDataPianificataInizio(dataPianificataInizio);
			
			// provo ad aggiornare la data di prima eleggibilita' se null
			if(orderToModify.getPrimaEleggibilita() == null) {
				orderToModify.setPrimaEleggibilita(orderToModify.getX4datamodello());
			}
			
			orderToCreate.add(order);

		}
		log.debug("[END] modificaOrdineAssetEleggibile");
		return orderToCreate;
	}
	
	
	private Order createOrder(AttivitaAsset order)
	{
		Order newOrder = new Order();
		newOrder.setAssetnum(order.getAssetnum());
		newOrder.setStato(order.getStatus());
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
		newOrder.setDataMaxPosticipo(order.getFnlConstraint());
		newOrder.setX4OrdinePrevalente(order.getX4OrdinePrevalente()); 
		newOrder.setDataNonPrimaDi(order.getSneConstraint());
		newOrder.setX4Trainante(order.getX4Trainante());
		newOrder.setPrimaEleggibilita(order.getPrimaEleggibilita());
		newOrder.setToCreate(false);
		newOrder.setX4outsourcing(order.getX4outsourcing());
		return newOrder;
	}
	
	
    private MicrocontrolloRequest prepareMicrocontrolloRequest(String wonum, Task task) 
    {
        MicrocontrolloRequest request = new MicrocontrolloRequest();
        request.setAssetnum(task.getAssetnum());
        request.setTaskid(task.getTaskid());
        request.setParent(wonum);
        request.setDescription(task.getDescription());
        request.setX4microcontrollo(task.getX4microcontrollo());
        request.setSiteid(Constants.SITEID);
        request.setWoclass("ACTIVITY");
        request.setIstask("1");
        return request;
    }

    
	private List<Pair<String, String>> sendMicrocontrolliStazione(boolean disableTransaction, List<WorkorderRequest> ordersToSend) 
	        throws EAMException, RestException 
	{
		List<Pair<String, String>> results = new ArrayList<>();
		
		// Keep order sent
        Set<String> orderSent = new HashSet<>();
        for (WorkorderRequest request : ordersToSend) 
        {
            log.debug(" - Sending order with wonum {}", request.getWonum());
            List<Task> listTasks = request.getWoactivity();
            request.setWoactivity(null);
            
            MaximoResponseArray<WorkorderBulkResponse> responsePostApi = retryableWorkorderService.callWorkorderApiWithRetry(disableTransaction, List.of(request), ordersToSend, orderSent);
            log.debug("API POST call completed with HTTP code: {}", responsePostApi.getHttpCode());

            List<WorkorderBulkResponse> responseWorkorder = responsePostApi.getResponses();
            if (responseWorkorder != null)
            {
                List<WorkorderResponse> responseDataList  = responseWorkorder.stream()
                                                                             .map(WorkorderBulkResponse::getResponsedata)
                                                                             .filter(Objects::nonNull)
                                                                             .collect(Collectors.toList());

                if (Assorted.isNotEmpty(responseDataList)) 
                {
                    responseDataList.stream()
                                    .filter(x -> Assorted.isNotEmpty(x.getAssetnum()) && Assorted.isNotEmpty(x.getWonum()))
                                    .forEach(x -> results.add(Pair.of(x.getAssetnum(), x.getWonum())));
                }
            }

            if (Assorted.isEmpty(results))
                throw new EAMException(EAMErrorMapping.EAM_9999.getEamCode(), "OdL not created for asset.");

            log.info("Order sent successfully without errors for wonum {}.", request.getWonum());
            orderSent.add(request.getWonum());
            
            if (Assorted.isNotEmpty(listTasks))
            {
                List<MicrocontrolloRequest> listRequest = listTasks.stream().map(x -> prepareMicrocontrolloRequest(request.getWonum(), x)).toList();
                log.debug("Prepared {} MicrocontrolloRequest(s) for sending.", listRequest.size());
                    
                List<List<MicrocontrolloRequest>> partitionMicro = EamEtlUtils.partition(listRequest, apiProperties.getMicrocontrolli().getBatchSize());
                for (List<MicrocontrolloRequest> requestMicroPartition: partitionMicro)
                {
                    MaximoResponseArray<WorkorderBulkResponse> responsePostApiMicro = retryableMicrocontrolliService.callMicrocontrolliApiWithRetry(requestMicroPartition);
                    log.debug("All microcontrolli orders sent successfully without errors.");
                    List<WorkorderBulkResponse> responseWorkorderMicro = responsePostApiMicro.getResponses();
                   
                    if (log.isDebugEnabled())
                        log.debug("End sendOrders micro ==> {} ", Assorted.isNotEmpty(responseWorkorderMicro) ? responseWorkorderMicro.toString() : null);
                }
            }
            
            log.debug("End sendOrders.");
        }
        
        return results;
	}
	
	
	private Set<String> getTronchiMancanti(AssetData assetData, Policy policy, Activity activity) 
	        throws DataLayerException 
	{
	    List<Assetspec> troncoByTypeAssetnum = assetspecService.getTroncoByAssetnum(assetData.getAsset().getAssetNum(), null);
        if (Assorted.isEmpty(troncoByTypeAssetnum))
            return Collections.emptySet();
        
         Set<String> tronchiAsset = troncoByTypeAssetnum.stream()
                                                        .map(Assetspec::getTableValue)
                                                        .filter(Assorted::isNotEmpty)
                                                        .collect(Collectors.toSet());
         
         List<AssetWithYearTronchi> assetWithYearTronchi = workOrderQueryService.findAssetWithYearTronchi(assetData.getAsset().getAssetNum(), policy.getCode(), activity != null ? activity.getJpnum() : null);
         if (Assorted.isNotEmpty(assetWithYearTronchi))
         {
              Set<String> tronchiFinded = assetWithYearTronchi.stream()
                                                             .filter(item -> Assorted.isNotEmpty(item.getTronchi()))
                                                             .flatMap(item -> Arrays.stream(item.getTronchi().split("\\|")))
                                                             .map(String::trim)
                                                             .filter(value -> !value.isEmpty())
                                                             .collect(Collectors.toSet());
              
              
              Set<String> intersectTronchi = new HashSet<>(tronchiAsset);
              intersectTronchi.removeAll(tronchiFinded);
              
              if (intersectTronchi.isEmpty()) 
                  return Collections.emptySet();
              else
                  return intersectTronchi;
              
         } else
             return tronchiAsset;
	}
}
