package it.terna.nas.service.exposure.handler;

import static it.terna.nas.service.material.utility.EamEtlUtils.*;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.dal.entity.eam.Activity;
import it.terna.nas.service.dal.entity.eam.AssetWithYearTronchi;
import it.terna.nas.service.dal.entity.eam.Assetspec;
import it.terna.nas.service.dal.entity.eam.AttivitaAsset;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.SequenceValue;
import it.terna.nas.service.dal.service.AssetspecService;
import it.terna.nas.service.dal.service.WorkOrderQueryService;
import it.terna.nas.service.dal.service.X4PolicyService;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.policy.AttributesDTO;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.utility.Catalog.PolicyTimeUnit;
import it.terna.nas.service.material.utility.Catalog.WorkOrderStatus;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PeriodicOrderHandler 
{
    @Autowired
    private AssetspecService assetspecService;
    
    @Value("${periodicStatusDisable: false}")
    private Boolean               periodicStatusDisable;
    
    @Autowired
    private WorkOrderQueryService workOrderQueryService;
    
    @Autowired
    private X4PolicyService policyService;
    
	public List<Order> createPeriodicOrders(AssetData assetData, Policy policy, Activity activity) 
	        throws DataLayerException 
	{
	    log.debug("  --- Starting creating periodic orders ....");

	    if (EamEtlUtils.isOverOneYear(policy.getFrequenza(), policy.getTimeUnit())) {
	        return createPeriodicOrdersForOverOneYear(assetData, policy, activity);
	    } else {
	        return createPeriodicOrdersWithinOneYear(assetData, policy, activity);
	    }
	}
	
	private List<Order> createPeriodicOrdersForOverOneYear(AssetData assetData, Policy policy, Activity activity) 
	        throws DataLayerException 
	{
        log.info("Policy frequency is greater than one year; calculating schedule dates.");
        Order order = initializeOrder(assetData, policy, activity);
        order.setWonum(Constants.PREFIX_DSS_WONUM + workOrderQueryService.getSequenceForAggregate().get(0).getNextval());
        order.setToCreate(Boolean.TRUE);

        // Controllo se ordine già è presente
        String existingWonum = workOrderQueryService.getWonumByPolicyAssetAndJpnum(
                policy.getId(),
                activity.getJpnum(),
                assetData.getAsset().getAssetNum()
        );

        if (existingWonum != null) {
	    	log.info("Order already present for this activity and policy!");
	    	return new ArrayList<>();	    	
	    }

        
        AttivitaAsset attivitaAsset = null;
        // Recupero attivitaAsset, se presente
        if (assetData.getAttivita() != null) {
            attivitaAsset = assetData.getAttivita()
                                     .stream()
                                     .filter(assetActivity -> Assorted.equals(assetActivity.getJpnum(), activity.getJpnum())
                                    		 && assetActivity.getActfinish() != null)
                                     .max(Comparator.comparing(AttivitaAsset::getActfinish)) 
                                     .orElse(null);
        }
        
        LocalDateTime baseDataModello = attivitaAsset == null
                ? LocalDateTime.now() 
                : attivitaAsset.getActfinish().toInstant()
                .atZone(ZoneId.systemDefault()) 
                .toLocalDateTime();
        
		LocalDateTime dataModello = calcolaDataModello(baseDataModello, policy.getFrequenza(), policy.getTimeUnit(), activity.getDuration());
        log.debug("Calculated model date: {}", dataModello);
	    setOrderDates(order, dataModello, policy, activity);
	        
	    return List.of(order);
	}
	
	
    private List<Order> createPeriodicOrdersWithinOneYear(AssetData assetData, Policy policy, Activity activity) 
            throws DataLayerException 
    {
        List<Order> orders = new ArrayList<>();
        LocalDateTime aPartireDa = null;
        if (Assorted.notNull(policy.getAPartireDa())) 
        {
            aPartireDa = policy.getAPartireDa().toInstant()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime();
        }
        
        int currentYear = LocalDate.now().getYear();
        int frequency = policy.getFrequenza();
        ChronoUnit timeUnit = policy.getTimeUnit().getTemporalUnit();

        int controlsPerYear;
        switch (timeUnit) 
        {
            case MONTHS -> controlsPerYear = 12 / frequency;
            case WEEKS -> controlsPerYear = 52 / frequency;
            case DAYS -> controlsPerYear = 365 / frequency;
            default -> controlsPerYear = 1; // Per YEARS, un solo ordine all'anno
        }

        int totalYears = 5; // Numero di anni da coprire
        Set<Integer> existingYears                        = null;
        Map<Integer, Set<String>> existingYearsTronchi    = null;
        Set<String> tronchiForAsset                       = null;
        
        if (policy.getLogicaTronco() == 1) 
        {
            existingYearsTronchi  = getExistingYearsTronchi(assetData, policy, activity != null ? activity.getJpnum() : null);
            tronchiForAsset       = getTronchiForAsset(assetData);
        } else {
            existingYears         = getExistingYears(assetData, policy);
        }
        
        for (int yearOffset = 0; yearOffset < totalYears; yearOffset++) 
        {
            int year = currentYear + yearOffset;

            boolean skipYear              = false;
            Set<String> intersectTronchi  = null;
            
            if (policy.getLogicaTronco() == 1)
            {
               Set<String> tronchi = existingYearsTronchi.get(year);
               if (Assorted.isNotEmpty(tronchi)) 
               {
                   intersectTronchi = new HashSet<>(tronchiForAsset);
                   intersectTronchi.removeAll(tronchi);
                   
                   if (intersectTronchi.isEmpty()) 
                       skipYear = true;
               }
               
            } else if (existingYears.contains(year)) {
                skipYear = true;
            }

            if (skipYear)
            {
                log.debug("Skipping order creation for year {}", year);
                continue;
            }
            
            // Calcoliamo il mese iniziale
            LocalDate startDate;
            if (timeUnit == ChronoUnit.MONTHS) {
                startDate = LocalDate.of(year, frequency, 1); // Es. frequenza 3 → Marzo
            } else if (timeUnit == ChronoUnit.YEARS) {
                startDate = LocalDate.of(year, 12, 1); // Sempre dicembre
            } else if (timeUnit == ChronoUnit.WEEKS) {
                LocalDate firstMondayOfYear = LocalDate.of(year, 1, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
                startDate = firstMondayOfYear.plusWeeks(frequency - 1); // Frequenza 2 → seconda settimana di gennaio
            } else { // DAYS
                startDate = LocalDate.of(year, 1, 1).plusDays(frequency - 1); // Frequenza 10 → 10 gennaio
            }

            for (int i = 0; i < controlsPerYear; i++) 
            {
                if (i > 0) 
                {
                    startDate = startDate.plus(frequency, timeUnit);
                }

                // Calcoliamo l'ultimo giorno del mese per mesi/anni
                LocalDateTime modelDate = (timeUnit == ChronoUnit.MONTHS || timeUnit == ChronoUnit.YEARS)
                    ? EamEtlUtils.calculateLastDayOfMonth(year, startDate.getMonthValue())
                    : startDate.atStartOfDay(); // Per settimane/giorni usa direttamente la data

                // Crea l'ordine
                Order order = initializeOrder(assetData, policy, activity);
                setOrderDatesWithinOneYear(order, modelDate, policy, activity);
                if (modelDate.isBefore(LocalDateTime.now())) 
                {
                    continue;
                }
                
                if (aPartireDa != null && aPartireDa.isAfter(modelDate)) 
                {
                    continue;               
                }
                
                order.setTroncoIntersection(intersectTronchi);
                orders.add(order);
            }
        }
        
        if (Assorted.isNotEmpty(orders))
        {
            List<SequenceValue> seqWo = workOrderQueryService.getSequenceForAggregate(Integer.toString(orders.size()));
            
            for (int i = 0; i < seqWo.size(); i++) 
            {
                Order order = orders.get(i);
                order.setWonum(Constants.PREFIX_DSS_WONUM + seqWo.get(i).getNextval());
                order.setToCreate(Boolean.TRUE);
            }            
        }
        
        return orders;
    }
    
	
	private Set<Integer> getExistingYears(AssetData assetData, Policy policy) {
	    
		if (assetData.getAttivita() == null) {
	        return Collections.emptySet();
	    }
		
	    return assetData.getAttivita()
	            .stream()
	            .filter(attivita -> policy.getCode().equals(attivita.getX4policy()) && 
	            											attivita.getX4datamodello() != null)
	            .map(attivita -> attivita.getX4datamodello()
	                    .toInstant()
	                    .atZone(ZoneId.systemDefault())
	                    .getYear())
	            .collect(Collectors.toSet());
	}
	
	
    private Map<Integer, Set<String>> getExistingYearsTronchi(AssetData assetData, Policy policy, String jpnum) 
            throws DataLayerException 
    {
        List<AssetWithYearTronchi> assetWithYearTronchi = workOrderQueryService.findAssetWithYearTronchi(assetData.getAsset().getAssetNum(), policy.getCode(), jpnum);

        if (Assorted.isEmpty(assetWithYearTronchi)) {
            return Collections.emptyMap();
        }

        return assetWithYearTronchi.stream()
                .filter(item -> item.getAnno() != null)
                .collect(Collectors.toMap(
                        AssetWithYearTronchi::getAnno,
                        item -> {
                            Set<String> tronchi = new HashSet<>();
                            if (Assorted.isNotEmpty(item.getTronchi())) 
                            {
                                tronchi.addAll(Arrays.stream(item.getTronchi().split("\\|"))
                                        .map(String::trim)
                                        .filter(value -> !value.isEmpty())
                                        .collect(Collectors.toSet()));
                            }
                            return tronchi;
                        },
                        (existing, incoming) -> {
                            existing.addAll(incoming);
                            return existing;
                        }
                ));
    }

	
    private Set<String> getTronchiForAsset(AssetData assetData)
    {
        List<Assetspec> troncoByAssetnum = assetspecService.getTroncoByAssetnum(assetData.getAsset().getAssetNum(), null);
        if (Assorted.isEmpty(troncoByAssetnum))
            return Collections.emptySet();
        
        return troncoByAssetnum.stream()
                    .map(Assetspec::getTableValue)
                    .filter(Assorted::isNotEmpty)
                    .collect(Collectors.toSet());
    }
    
    
    private Order initializeOrder(AssetData assetData, Policy policy, Activity activity) 
	{
	    Order order = new Order();
	    order.setAssetnum(assetData.getAsset().getAssetNum());
	    order.setX4PolicyId(policy.getId());
	    order.setIdPolicy(policy.getCode());
	    order.setStato(Boolean.TRUE.equals(periodicStatusDisable) ? WorkOrderStatus.DA_VALIDARE.getCode() : WorkOrderStatus.APERTO.getCode());
	    order.setJpnum(activity != null ? activity.getJpnum() : null);
	    order.setX4outsourcing(order.getX4outsourcing());
	    return order;
	}
	
	
	private void setOrderDates(Order order, LocalDateTime  dataModello, Policy policy, Activity activity) {
		 // Aggiunge al modello la quantità massima di posticipo, in giorni
	    LocalDateTime dataMaxPosticipo = addToDate(dataModello, policy.getMaxPosticipo(), PolicyTimeUnit.DAY);
	    order.setDataMaxPosticipo(toDate(dataMaxPosticipo));

	    // Sottrae la quantità massima di anticipo, in giorni
	    order.setDataNonPrimaDi(EamEtlUtils.calcolaDataNonPrimaDi(dataModello, policy.getMaxAnticipo()));

	    // Sottrae la durata dell'attività, in ore (se presente)
		Date dataPianificataInizio = dataPianificataInizio(dataModello, 	
				   activity != null && activity.getDuration() != null
				   		? activity.getDuration()
						: BigDecimal.ZERO);
		order.setDataPianificataInizio(dataPianificataInizio);

	    // Imposta la data di fine (nessuna modifica, rimane a dataModello)
	    order.setDataPianificataFine(toDate(dataModello));

	    // Imposta la data del modello
	    order.setDataModello(toDate(dataModello));
	}
	
	public List<Order> modifyPeriodicOrdersWithinOneYear(
	        AssetData assetData, Policy policy, Activity activity,
	        List<AttivitaAsset> listOrders, AttributesDTO attributesDTO) throws DataLayerException {
	    
	    log.info("[START] modifyPeriodicOrdersWithinOneYear for asset: {} and policy: {}", assetData.getAsset().getAssetNum(), policy.getId());
	    
	    int currentYear = LocalDate.now().getYear();
		LocalDateTime aPartireDa = null;
		if (Assorted.notNull(policy.getAPartireDa())) {
		    aPartireDa = policy.getAPartireDa().toInstant()
		                        .atZone(ZoneId.systemDefault())
		                        .toLocalDateTime();
		}

	    List<AttivitaAsset> relevantOrders = listOrders.stream()
	            .filter(order -> order.getX4datamodello() != null && order.getStatus().equals(Boolean.TRUE.equals(periodicStatusDisable) ? WorkOrderStatus.DA_VALIDARE.getCode() : WorkOrderStatus.APERTO.getCode()))
	            .filter(order -> {
	                LocalDate modelDate = order.getX4datamodello().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	                int orderYear = modelDate.getYear();
	                return orderYear >= currentYear;
	            })
	            .collect(Collectors.toList());
	    
	    relevantOrders.sort(Comparator.comparing(AttivitaAsset::getX4datamodello, Comparator.nullsLast(Comparator.naturalOrder())));
	    log.info("Found {} relevant orders for modification.", relevantOrders.size());
	    
	    // Puntatore agli ordini esistenti
	    int orderIndex = 0;
	    int totalOrders = relevantOrders.size();
	    
	    List<Order> modifyOrders = new ArrayList<>();
	    List<Order> newOrders = new ArrayList<>();
	    List<Order> ordersToSend = new ArrayList<>();
	    
	    int newFrequency = Assorted.notNull(attributesDTO.getFrequency()) ? attributesDTO.getFrequency().getNewValue() : policy.getFrequenza();
	    ChronoUnit newTimeUnit = Assorted.notNull(attributesDTO.getRifTemporale()) ?
	            PolicyTimeUnit.fromCode(attributesDTO.getRifTemporale().getNewValue()).getTemporalUnit() : policy.getTimeUnit().getTemporalUnit();
	    int oldFrequency = Assorted.notNull(attributesDTO.getFrequency()) ? attributesDTO.getFrequency().getOldValue() : policy.getFrequenza();
	    ChronoUnit oldTimeUnit = Assorted.notNull(attributesDTO.getRifTemporale()) ?
	            PolicyTimeUnit.fromCode(attributesDTO.getRifTemporale().getOldValue()).getTemporalUnit() : policy.getTimeUnit().getTemporalUnit();
	    
	    int yearToStart = currentYear;
	    int controlsPerYear;
		switch (newTimeUnit) {
		    case MONTHS -> controlsPerYear = 12 / newFrequency;
		    case WEEKS -> controlsPerYear = 52 / newFrequency;
		    case DAYS -> controlsPerYear = 365 / newFrequency;
		    default -> controlsPerYear = 1; 
		}
	    boolean isFrequencyDecreased = newTimeUnit.equals(oldTimeUnit) ? newFrequency < oldFrequency : 
	            convertToDays(newFrequency, newTimeUnit) < convertToDays(oldFrequency, oldTimeUnit);
	    
	    if (isFrequencyDecreased) {
	        log.info("Frequency decreased: modifying existing orders and creating new ones if necessary.");
            int lastYearOrders = relevantOrders.stream()
                    .filter(a -> a.getX4datamodello() != null)
                    .max(Comparator.comparing(AttivitaAsset::getX4datamodello))
                    .map(a -> a.getX4datamodello().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear())
                    .orElse(currentYear);
            
	        int totalYears = (lastYearOrders - currentYear + 1) < 5 ? 5 : (lastYearOrders - currentYear + 1);
			for (int yearOffset = 0; yearOffset < totalYears; yearOffset++) {
			    int year = yearToStart + yearOffset;

			    // Calcoliamo il mese iniziale
			    LocalDate startDate;
			    if (newTimeUnit == ChronoUnit.MONTHS) {
			        startDate = LocalDate.of(year, newFrequency, 1);
			    } else if (newTimeUnit == ChronoUnit.YEARS) {
			        startDate = LocalDate.of(year, 12, 1);
			    } else if (newTimeUnit == ChronoUnit.WEEKS) {			       
			    	LocalDate firstMondayOfYear = LocalDate.of(year, 1, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
			        startDate = firstMondayOfYear.plusWeeks(newFrequency - 1); 			    	
			    } else {
			        startDate = LocalDate.of(year, 1, 1).plusDays(newFrequency - 1); 
			    }

			    for (int i = 0; i < controlsPerYear; i++) {
		            Order order = null;

			        if (i > 0) {
			            startDate = startDate.plus(newFrequency, newTimeUnit);
			        }

			        // Calcoliamo l'ultimo giorno del mese per mesi/anni
			        LocalDateTime modelDate = (newTimeUnit == ChronoUnit.MONTHS || newTimeUnit == ChronoUnit.YEARS)
			                ? EamEtlUtils.calculateLastDayOfMonth(year, startDate.getMonthValue())
			                : startDate.atStartOfDay();
			        
			        if(modelDate.isBefore(LocalDateTime.now())) {
			        	continue;
			        }
			        
			        
			        if(aPartireDa != null && aPartireDa.isAfter(modelDate)) {
			        	continue;	        	
			        }

			        if (orderIndex < totalOrders) {

		                Date currentModelDate = relevantOrders.get(orderIndex).getX4datamodello();
		                List<AttivitaAsset> sameDateOrders = new ArrayList<>();

		                while (orderIndex < totalOrders &&
		                        relevantOrders.get(orderIndex).getX4datamodello().equals(currentModelDate)) {
		                    sameDateOrders.add(relevantOrders.get(orderIndex));
		                    orderIndex++;
		                }

		                for (AttivitaAsset aa : sameDateOrders) {
		                    order = createOrder(aa);
		                    order.setX4datemod(false);
		                    setOrderDatesWithinOneYear(order, modelDate, policy, activity);
		                    modifyOrders.add(order);
		                }

		            } else {
		                // CREAZIONE NUOVO ORDINE
		                order = initializeOrder(assetData, policy, activity);
		                setOrderDatesWithinOneYear(order, modelDate, policy, activity);
		                newOrders.add(order);
		            }

			    }
			}
	    } else {
	        log.info("Frequency increased: modifying existing orders.");
		    LocalDate dateToStart;
		    if (newTimeUnit == ChronoUnit.MONTHS) {
		        dateToStart = LocalDate.of(yearToStart, newFrequency, 1);
		    } else if (newTimeUnit == ChronoUnit.YEARS) {
		        dateToStart = LocalDate.of(yearToStart, 12, 1);
		    } else if (newTimeUnit == ChronoUnit.WEEKS) {
		    	LocalDate firstMondayOfYear = LocalDate.of(yearToStart, 1, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
		    	dateToStart = firstMondayOfYear.plusWeeks(newFrequency - 1);
		    } else { 
		        dateToStart = LocalDate.of(yearToStart, 1, 1).plusDays(newFrequency - 1); 
		    }
	        LocalDate currentDate = dateToStart; 
            int maxIterations = Math.max(relevantOrders.size(), controlsPerYear * 5);
	        for (int i = 0; i < maxIterations; i++) {	
	            Order order = null;
	            if (i > 0) {
	            	currentDate = currentDate.plus(newFrequency, newTimeUnit);
	                if (currentDate.getYear() > dateToStart.getYear()) {
	                	dateToStart = dateToStart.plusYears(1);
	                    currentDate = LocalDate.of(dateToStart.getYear(), dateToStart.getMonth(), dateToStart.getDayOfMonth());
	                }
		        }
	            LocalDateTime modelDate;
	            do {
	                modelDate = (newTimeUnit == ChronoUnit.MONTHS || newTimeUnit == ChronoUnit.YEARS)
	                    ? EamEtlUtils.calculateLastDayOfMonth(currentDate.getYear(), currentDate.getMonthValue())
	                    : currentDate.atStartOfDay();
	                
	                if (modelDate.isBefore(LocalDateTime.now()) || (aPartireDa != null && aPartireDa.isAfter(modelDate))) {
	                    currentDate = currentDate.plus(newFrequency, newTimeUnit);
		                if (currentDate.getYear() > dateToStart.getYear()) {
		                	dateToStart = dateToStart.plusYears(1);
		                    currentDate = LocalDate.of(dateToStart.getYear(), dateToStart.getMonth(), dateToStart.getDayOfMonth());
		                }
	                }
	                
	            } while (modelDate.isBefore(LocalDateTime.now()));
	            
		        if (orderIndex < totalOrders) {

	                Date currentModelDate = relevantOrders.get(orderIndex).getX4datamodello();
	                List<AttivitaAsset> sameDateOrders = new ArrayList<>();

	                while (orderIndex < totalOrders &&
	                        relevantOrders.get(orderIndex).getX4datamodello().equals(currentModelDate)) {
	                    sameDateOrders.add(relevantOrders.get(orderIndex));
	                    orderIndex++;
	                }

	                for (AttivitaAsset aa : sameDateOrders) {
	                    order = createOrder(aa);
	                    order.setX4datemod(false);
			            if (modelDate.getYear() > (yearToStart + 4)) {
			                // L'anno di modelDate è maggiore di yearToStart + 4
			                order.setStato(WorkOrderStatus.NON_ESEGUITO.getCode());
			            }
			            else {
		                    setOrderDatesWithinOneYear(order, modelDate, policy, activity);		            	
			            }
	                    modifyOrders.add(order);


	                }

	            } else {
	                // CREAZIONE NUOVO ORDINE
	                order = initializeOrder(assetData, policy, activity);
		            if (modelDate.getYear() > (yearToStart + 4)) {
		                // L'anno di modelDate è maggiore di yearToStart + 4
		                continue;
		            }
	                setOrderDatesWithinOneYear(order, modelDate, policy, activity);
	                newOrders.add(order);
	            }
	        }
	    }
	    
	    if (Assorted.isNotEmpty(newOrders)) {
	        log.info("Generating sequence numbers for {} new orders.", newOrders.size());
	        List<SequenceValue> seqWo = workOrderQueryService.getSequenceForAggregate(Integer.toString(newOrders.size()));
	        
	        for (int i = 0; i < seqWo.size(); i++) {
                Order order = newOrders.get(i);
                order.setWonum(Constants.PREFIX_DSS_WONUM + seqWo.get(i).getNextval());
                order.setToCreate(Boolean.TRUE);
	        }
	        ordersToSend.addAll(newOrders);
	    }
	    
	    if (Assorted.isNotEmpty(modifyOrders)) {
	        ordersToSend.addAll(modifyOrders);
	    }
	    
	    log.info("[END] modifyPeriodicOrdersWithinOneYear - Total modified orders: {}", ordersToSend.size());
	    return ordersToSend;
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
		newOrder.setX4OrdinePrevalente(order.getX4OrdinePrevalente()); 
		newOrder.setDataMaxPosticipo(order.getFnlConstraint());
		newOrder.setDataNonPrimaDi(order.getSneConstraint());
		newOrder.setX4Trainante(order.getX4Trainante());
		newOrder.setToCreate(false);
		newOrder.setX4outsourcing(order.getX4outsourcing());
		return newOrder;
	}

	public List<Order> modifyPeriodicOrdersForOverOneYear(AssetData assetData, Policy policy, Activity activity,
			List<AttivitaAsset> listOrders, AttributesDTO attributesDTO) {
	    log.info("[START] modifyPeriodicOrdersForOverOneYear for asset: {} and policy: {}", assetData.getAsset().getAssetNum(), policy.getId());
	    List<Order> ordersToSend = new ArrayList<>();
        for(AttivitaAsset orderToModify:listOrders) {
        	Order order = createOrder(orderToModify);
            AttivitaAsset attivitaAsset = null;
            // Recupero attivitaAsset, se presente
            if (assetData.getAttivita() != null) {
                attivitaAsset = assetData.getAttivita()
                                         .stream()
                                         .filter(assetActivity -> Assorted.equals(assetActivity.getJpnum(), activity.getJpnum())
                                        		 && assetActivity.getActfinish() != null)
                                         .max(Comparator.comparing(AttivitaAsset::getActfinish)) 
                                         .orElse(null);
            }
            
            LocalDateTime baseDataModello = attivitaAsset == null
                    ? LocalDateTime.now() 
                    : attivitaAsset.getActfinish().toInstant()
                    .atZone(ZoneId.systemDefault()) 
                    .toLocalDateTime();
            
    		LocalDateTime dataModello = calcolaDataModello(baseDataModello, policy.getFrequenza(), policy.getTimeUnit(), activity.getDuration());
            if (dataModello.isBefore(LocalDateTime.now())) {
                dataModello = LocalDateTime.now();
            }
            log.debug("Calculated model date: {}", dataModello);
    	    setOrderDates(order, dataModello, policy, activity); 	
    	    ordersToSend.add(order);
        }   
	    log.info("[END] modifyPeriodicOrdersForOverOneYear - Total modified orders: {}", ordersToSend.size());        
	    return ordersToSend;
	}

	public List<Order> modifyPeriodicOrdersZonizzazione(AssetData assetData, Policy policy, Activity activity,
			 List<AttivitaAsset> existingOrders) {
	    log.info("[START] modifyPeriodicOrdersZonizzazione for asset: {} and policy: {}", assetData.getAsset().getAssetNum(), policy.getId());
	    List<Order> ordersToSend = new ArrayList<>();
	    for(AttivitaAsset existingOrder: existingOrders) {
	    	Order order = createOrder(existingOrder);
            AttivitaAsset attivitaAsset = null;
            // Recupero attivitaAsset, se presente
            if (assetData.getAttivita() != null) {
                attivitaAsset = assetData.getAttivita()
                                         .stream()
                                         .filter(assetActivity -> Assorted.equals(assetActivity.getJpnum(), activity.getJpnum())
                                        		 && assetActivity.getActfinish() != null)
                                         .max(Comparator.comparing(AttivitaAsset::getActfinish)) 
                                         .orElse(null);
            }
            
            LocalDateTime baseDataModello = attivitaAsset == null
                    ? LocalDateTime.now() 
                    : attivitaAsset.getActfinish().toInstant()
                    .atZone(ZoneId.systemDefault()) 
                    .toLocalDateTime();
            
    		LocalDateTime dataModello = calcolaDataModello(baseDataModello, policy.getFrequenza(), policy.getTimeUnit(), activity.getDuration());
            if (dataModello.isBefore(LocalDateTime.now())) {
                dataModello = LocalDateTime.now();
            }
            log.debug("Calculated model date: {}", dataModello);
    	    setOrderDates(order, dataModello, policy, activity); 
    	    if(policy.getLogicaTronco() == 0) {
        	    order.setX4PolicyId(policy.getId());
        	    order.setIdPolicy(policy.getCode());	
    	    }
    	    ordersToSend.add(order);       	
	    }       	
	    log.info("[END] modifyPeriodicOrdersZonizzazione - Total modified orders: {}", ordersToSend.size());        
	    return ordersToSend;
	}

	public List<Order> modifyPeriodicOrdersZonizzazioneWithinOneYear(AssetData assetData, Policy policy,
			Activity activity) throws DataLayerException {
		
	    log.info("[START] modifyPeriodicOrdersZonizzazioneWithinOneYear for asset: {} and policy: {}", assetData.getAsset().getAssetNum(), policy.getId());
		List<Order> ordersToSend = new ArrayList<>();
		LocalDateTime aPartireDa = null;
		if (Assorted.notNull(policy.getAPartireDa())) {
		    aPartireDa = policy.getAPartireDa().toInstant()
		                        .atZone(ZoneId.systemDefault())
		                        .toLocalDateTime();
		}
		int currentYear = LocalDate.now().getYear();
		List<AttivitaAsset> existingOrders = assetData.getAttivita().stream()
		    	    .filter(a -> a.getJpnum().equals(activity.getJpnum()) && 
		    	    		Assorted.notNull(a.getX4policyid()) &&
		    	        (a.getStatus().equals(WorkOrderStatus.APERTO.getCode()) || 
		    	         a.getStatus().equals(WorkOrderStatus.DA_VALIDARE.getCode())))
		    	    .collect(Collectors.toList());
		        
		if(Assorted.isNotEmpty(existingOrders)) {
		    	   List<AttivitaAsset> relevantOrders = existingOrders.stream()
		   	            .filter(order -> order.getX4datamodello() != null)
		   	            .filter(order -> {
		   	                LocalDate modelDate = order.getX4datamodello().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		   	                int orderYear = modelDate.getYear();
		   	                return orderYear >= currentYear;
		   	            })
		   	            .collect(Collectors.toList());
		   	    
	   	    relevantOrders.sort(Comparator.comparing(AttivitaAsset::getX4datamodello, Comparator.nullsLast(Comparator.naturalOrder())));
   		    // Puntatore agli ordini esistenti
   		    int orderIndex = 0;
   		    int totalOrders = relevantOrders.size();


	   	    Long oldPolicyId = relevantOrders.stream()
	   	            .findFirst()
	   	            .map(AttivitaAsset::getX4policyid)
	   	            .orElse(null);
	   	    
	   	    String oldPolicyCode = relevantOrders.stream()
	   	            .findFirst()
	   	            .map(AttivitaAsset::getX4policy)
	   	            .orElse(null);

	   	    log.info("Found {} relevant orders for modification.", relevantOrders.size());
	   	    
	   	    List<Order> modifyOrders = new ArrayList<>();
	   	    List<Order> newOrders = new ArrayList<>();
	   		Policy oldPolicy = policyService.loadPolicy(oldPolicyId);
	   	    int newFrequency = policy.getFrequenza();
	   	    ChronoUnit newTimeUnit = policy.getTimeUnit().getTemporalUnit();
	   	    int oldFrequency =  oldPolicy.getFrequenza();
	   		ChronoUnit  oldTimeUnit = oldPolicy.getTimeUnit().getTemporalUnit(); 
	   	    int yearToStart = currentYear;
	   	    int controlsPerYear;
	   		switch (newTimeUnit) {
	   		    case MONTHS -> controlsPerYear = 12 / newFrequency;
	   		    case WEEKS -> controlsPerYear = 52 / newFrequency;
	   		    case DAYS -> controlsPerYear = 365 / newFrequency;
	   		    default -> controlsPerYear = 1; 
	   		}
	   	    boolean isFrequencyDecreased = newTimeUnit.equals(oldTimeUnit) ? newFrequency < oldFrequency : 
	   	            convertToDays(newFrequency, newTimeUnit) < convertToDays(oldFrequency, oldTimeUnit);
	   	    
	   	    if (isFrequencyDecreased) {
	   	        log.info("Frequency decreased: modifying existing orders and creating new ones if necessary.");
	               int lastYearOrders = relevantOrders.stream()
	                       .filter(a -> a.getX4datamodello() != null)
	                       .max(Comparator.comparing(AttivitaAsset::getX4datamodello))
	                       .map(a -> a.getX4datamodello().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear())
	                       .orElse(currentYear);
	               
	   	        int totalYears = (lastYearOrders - currentYear + 1) < 5 ? 5 : (lastYearOrders - currentYear + 1);
	   			for (int yearOffset = 0; yearOffset < totalYears; yearOffset++) {
	   			    int year = yearToStart + yearOffset;

	   			    // Calcoliamo il mese iniziale
	   			    LocalDate startDate;
	   			    if (newTimeUnit == ChronoUnit.MONTHS) {
	   			        startDate = LocalDate.of(year, newFrequency, 1);
	   			    } else if (newTimeUnit == ChronoUnit.YEARS) {
	   			        startDate = LocalDate.of(year, 12, 1);
	   			    } else if (newTimeUnit == ChronoUnit.WEEKS) {
	   			    	LocalDate firstMondayOfYear = LocalDate.of(year, 1, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
	   			    	startDate = firstMondayOfYear.plusWeeks(newFrequency - 1);
	   			    } else {
	   			        startDate = LocalDate.of(year, 1, 1).plusDays(newFrequency - 1); 
	   			    }

	   			    for (int i = 0; i < controlsPerYear; i++) {
	   		            Order order = null;

	   			        if (i > 0) {
	   			            startDate = startDate.plus(newFrequency, newTimeUnit);
	   			        }

	   			        // Calcoliamo l'ultimo giorno del mese per mesi/anni
	   			        LocalDateTime modelDate = (newTimeUnit == ChronoUnit.MONTHS || newTimeUnit == ChronoUnit.YEARS)
	   			                ? EamEtlUtils.calculateLastDayOfMonth(year, startDate.getMonthValue())
	   			                : startDate.atStartOfDay();
	   			        
	   			        if(modelDate.isBefore(LocalDateTime.now())) {
				        	continue;
				        }
	   			        
				        if(aPartireDa != null && aPartireDa.isAfter(modelDate)) {
				        	continue;	        	
				        }

				        if (orderIndex < totalOrders) {

			                Date currentModelDate = relevantOrders.get(orderIndex).getX4datamodello();
			                List<AttivitaAsset> sameDateOrders = new ArrayList<>();

			                while (orderIndex < totalOrders &&
			                        relevantOrders.get(orderIndex).getX4datamodello().equals(currentModelDate)) {
			                    sameDateOrders.add(relevantOrders.get(orderIndex));
			                    orderIndex++;
			                }

			                for (AttivitaAsset aa : sameDateOrders) {
			                    order = createOrder(aa);
			                    order.setX4datemod(false);
					    	    if(policy.getLogicaTronco() == 0) {
					        	    order.setX4PolicyId(policy.getId());
					        	    order.setIdPolicy(policy.getCode());	
					    	    }
			                    setOrderDatesWithinOneYear(order, modelDate, policy, activity);
			                    modifyOrders.add(order);
			                }

			            } else {
			                // CREAZIONE NUOVO ORDINE
			                order = initializeOrder(assetData, policy, activity);
				    	    if(policy.getLogicaTronco() == 0) {
				        	    order.setX4PolicyId(policy.getId());
				        	    order.setIdPolicy(policy.getCode());	
				    	    }
			                setOrderDatesWithinOneYear(order, modelDate, policy, activity);
			                newOrders.add(order);
			            }
	   			    }
	   			}
	   	    } else {
	   	        log.info("Frequency increased: modifying existing orders.");
	   		    LocalDate dateToStart;
	   		    if (newTimeUnit == ChronoUnit.MONTHS) {
	   		        dateToStart = LocalDate.of(yearToStart, newFrequency, 1);
	   		    } else if (newTimeUnit == ChronoUnit.YEARS) {
	   		        dateToStart = LocalDate.of(yearToStart, 12, 1);
	   		    } else if (newTimeUnit == ChronoUnit.WEEKS) {
   			    	LocalDate firstMondayOfYear = LocalDate.of(yearToStart, 1, 1).with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
   			    	dateToStart = firstMondayOfYear.plusWeeks(newFrequency - 1);
	   		    } else { 
	   		        dateToStart = LocalDate.of(yearToStart, 1, 1).plusDays(newFrequency - 1); 
	   		    }
	   	        LocalDate currentDate = dateToStart; 
                int maxIterations = Math.max(relevantOrders.size(), controlsPerYear * 5);
		        for (int i = 0; i < maxIterations; i++) {	
		            Order order = null;
		            if (i > 0) {
		            	currentDate = currentDate.plus(newFrequency, newTimeUnit);
		                if (currentDate.getYear() > dateToStart.getYear()) {
		                	dateToStart = dateToStart.plusYears(1);
		                    currentDate = LocalDate.of(dateToStart.getYear(), dateToStart.getMonth(), dateToStart.getDayOfMonth());
		                }
			        }
		            LocalDateTime modelDate;
		            do {
		                modelDate = (newTimeUnit == ChronoUnit.MONTHS || newTimeUnit == ChronoUnit.YEARS)
		                    ? EamEtlUtils.calculateLastDayOfMonth(currentDate.getYear(), currentDate.getMonthValue())
		                    : currentDate.atStartOfDay();
		                
		                if (modelDate.isBefore(LocalDateTime.now())) {
		                    currentDate = currentDate.plus(newFrequency, newTimeUnit);
			                if (currentDate.getYear() > dateToStart.getYear()) {
			                	dateToStart = dateToStart.plusYears(1);
			                    currentDate = LocalDate.of(dateToStart.getYear(), dateToStart.getMonth(), dateToStart.getDayOfMonth());
			                }
		                }
		                
		            } while (modelDate.isBefore(LocalDateTime.now()));
		            
			        if (orderIndex < totalOrders) {

		                Date currentModelDate = relevantOrders.get(orderIndex).getX4datamodello();
		                List<AttivitaAsset> sameDateOrders = new ArrayList<>();

		                while (orderIndex < totalOrders &&
		                        relevantOrders.get(orderIndex).getX4datamodello().equals(currentModelDate)) {
		                    sameDateOrders.add(relevantOrders.get(orderIndex));
		                    orderIndex++;
		                }

		                for (AttivitaAsset aa : sameDateOrders) {
		                    order = createOrder(aa);
		                    order.setX4datemod(false);
				            if (modelDate.getYear() > (yearToStart + 4)) {
				                // L'anno di modelDate è maggiore di yearToStart + 4
				                order.setStato(WorkOrderStatus.NON_ESEGUITO.getCode());
				            }
				            else {
			                    setOrderDatesWithinOneYear(order, modelDate, policy, activity);		            	
				            }
				    	    if(policy.getLogicaTronco() == 0) {
				        	    order.setX4PolicyId(policy.getId());
				        	    order.setIdPolicy(policy.getCode());	
				    	    }
		                    modifyOrders.add(order);
		                }

		            } else {
		                // CREAZIONE NUOVO ORDINE
		                order = initializeOrder(assetData, policy, activity);
			    	    if(policy.getLogicaTronco() == 1) {
			        	    order.setX4PolicyId(oldPolicyId);
			        	    order.setIdPolicy(oldPolicyCode);	
			    	    }
			    	    else if(policy.getLogicaTronco() == 0) {
			        	    order.setX4PolicyId(policy.getId());
			        	    order.setIdPolicy(policy.getCode());	
			    	    }
			            if (modelDate.getYear() > (yearToStart + 4)) {
			                // L'anno di modelDate è maggiore di yearToStart + 4
			                continue;
			            }
		                setOrderDatesWithinOneYear(order, modelDate, policy, activity);
		                newOrders.add(order);
		            }
		        }
	   	    }
	   	    
	   	    if (Assorted.isNotEmpty(newOrders)) {
	   	        log.info("Generating sequence numbers for {} new orders.", newOrders.size());
	   	        List<SequenceValue> seqWo = workOrderQueryService.getSequenceForAggregate(Integer.toString(newOrders.size()));
	   	        
	   	        for (int i = 0; i < seqWo.size(); i++) {
	                   Order order = newOrders.get(i);
	                   order.setWonum(Constants.PREFIX_DSS_WONUM + seqWo.get(i).getNextval());
	                   order.setToCreate(Boolean.TRUE);
	   	        }
	   	        ordersToSend.addAll(newOrders);
	   	    }
	   	    
	   	    if (Assorted.isNotEmpty(modifyOrders)) {
	   	        ordersToSend.addAll(modifyOrders);
	   	    }
	    	
	    } 
	    log.info("[END] modifyPeriodicOrdersZonizzazioneWithinOneYear - Total modified orders: {}", ordersToSend.size());
	    return ordersToSend;
	}
	
	private void setOrderDatesWithinOneYear(Order order, LocalDateTime  dataModello, Policy policy, Activity activity) {
		 // Aggiunge al modello la quantità massima di posticipo, in giorni
	    LocalDateTime dataMaxPosticipo = addToDate(dataModello, policy.getMaxPosticipo(), PolicyTimeUnit.DAY);
	    order.setDataMaxPosticipo(toDate(dataMaxPosticipo));

	    // Sottrae la quantità massima di anticipo, in giorni
	    LocalDateTime notBeforeThan = calculateNonBeforeThanDate(dataModello,  policy.getTimeUnit().getTemporalUnit(), policy.getFrequenza());
	    order.setDataNonPrimaDi(toDate(notBeforeThan));

	    // Sottrae la durata dell'attività, in ore (se presente)
		Date originalDatePianificataInizio = dataPianificataInizio(dataModello, 	
				   activity != null && activity.getDuration() != null
			   		? activity.getDuration()
					: BigDecimal.ZERO);

		LocalDate localDatePianificataInizio = originalDatePianificataInizio.toInstant()
		        .atZone(ZoneId.systemDefault())
		        .toLocalDate(); // estrae solo la parte di data

		// Converte in Date a mezzanotte
		Date dataPianificataInizio = Date.from(
		        localDatePianificataInizio.atStartOfDay(ZoneId.systemDefault()).toInstant()
		);

		order.setDataPianificataInizio(dataPianificataInizio);

	    // Imposta la data di fine (nessuna modifica, rimane a dataModello)
	    order.setDataPianificataFine(toDate(dataModello));

	    // Imposta la data del modello
	    order.setDataModello(toDate(dataModello));
	}
	
	private LocalDateTime calculateNonBeforeThanDate(LocalDateTime referenceDate, ChronoUnit timeUnit, int frequencyValue) {
	    if (frequencyValue <= 0) {
	        return referenceDate; // Caso di errore o valore non gestito
	    }

	    switch (timeUnit) {
	        case DAYS: { // Giornaliero (da 1 a 6 giorni)
	            return referenceDate.minusDays(frequencyValue);
	        }
	        case WEEKS: { 
	            // Trova la data di riferimento
	            LocalDate correctDate = referenceDate.toLocalDate().minusWeeks(frequencyValue);
	            
	            correctDate = correctDate.plusDays(1);
	            
	            return correctDate.atStartOfDay(); 
	        }
	        
	        case MONTHS: { // Mensile (1, 2, 3, 4, 6, 12 mesi)
	            int currentMonth = referenceDate.getMonthValue();
	            int year = referenceDate.getYear();

	            if (frequencyValue == 12) { 
	                currentMonth = 1;
	            } else if (frequencyValue == 6) { 
	                currentMonth = (currentMonth <= 6) ? 1 : 7;
	            } else { 	              
	                currentMonth = ((currentMonth - 1) / frequencyValue) * frequencyValue + 1;
	            }

	            LocalDate firstDayOfPeriod = LocalDate.of(year, currentMonth, 1);
	            return firstDayOfPeriod.atStartOfDay();
	        }
	        case YEARS: { // Annuale (1 anno) → Come la 12M
	            return LocalDate.of(referenceDate.getYear(), 1, 1).atStartOfDay();
	        }
	        default:
	            throw new IllegalArgumentException("ChronoUnit not supported: " + timeUnit);
	    }
	}


}
