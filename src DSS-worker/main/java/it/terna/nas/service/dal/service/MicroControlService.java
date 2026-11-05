package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.repository.WorkOrderRepository;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.ConditionTaskProjection;
import it.terna.nas.service.material.dto.NoConditionTaskProjection;
import it.terna.nas.service.material.dto.ResetDto;
import it.terna.nas.service.material.dto.Task;
import it.terna.nas.service.material.utility.Catalog;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MicroControlService 
{

	@Autowired
	private AssetDataService assetDataService;
	
	@Autowired
	private WorkOrderRepository workOrderRepository;
	
	/**
	 * 1) Controlla se l'asset è di tipo stazione tramite assetnum
	 * 2) Recupera tutti i task partendo dall'assetnum
	 *
	 * @param parameters Mappa di parametri per la ricerca.
	 * @return Rrestituisce una lista combinata di {@code Task}.
	 *         In caso di errore, le liste non disponibili verranno restituite come vuote.
	 */
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<Task> manageMicrocontrols (String assetnum, String jpnum) 
	{
	    long startTime = System.currentTimeMillis();
	    log.debug("call manageMicrocontrols({}, {}) |{}|", assetnum, jpnum, startTime);
	    
		List<Task> tasks      = new ArrayList<>();

		try {		  
			if (Assorted.notNull(jpnum) && Assorted.notNull(assetnum)) 
			{
				Boolean isStazione = assetDataService.checkIfIsStazione(assetnum);
				log.info("MicroControlService - manageMicroControls - Checking is asset is Stazione: {}", isStazione);
				
				if (Boolean.TRUE.equals(isStazione)) 
				{
					// Metodo che recupera tutti i task 
					Collection<Task> searchResult = retrieveTasks(assetnum, jpnum);
					if(searchResult != null) {
						int taskId = Constants.TASK_ID_INCREMENT;
						for(Task task : searchResult) {
							log.debug(" Checking status for {}", task.getAssetnum());
							if(checkStatus(task, jpnum)) {
								log.debug(" Status is valid for {}", task.getAssetnum());
								tasks.add(task);
								// set taskId
								task.setTaskid(taskId);
								taskId += Constants.TASK_ID_INCREMENT;
							} else {
								log.debug(" Status is not valid for {}", task.getAssetnum());
							}
						}
					}
				}
			}

		} catch (Exception e) {
			log.error(e.getMessage(), e);
		} finally {
		    long endTime = System.currentTimeMillis();
			log.info("end manageMicrocontrols({}, {}) |{}|", assetnum, jpnum, endTime - startTime);
		}
		
		return tasks;
	}


	/**
	 * Esegue due query asincrone in parallelo per recuperare: <br>
	 * - Una lista di {@code ConditionTaskProjection}, ovvero task con condizione <br>
	 * - Una lista di {@Code NoConditionTaskProjection}, ovvero task senza condizione <br>
	 * Aggiunge alla List di {@code Task} "allTasks" tutti i task senza condizione, e per ogni assetnum univoco associa N task con condizione
	 *
	 * @param parameters Mappa di parametri per la ricerca.
	 * @return Rrestituisce una lista combinata di {@code Task}.
	 *         In caso di errore lancia un eccezione {@code EAMException} EAM_0017
	 */
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public Collection<Task> retrieveTasks(String assetnum, String jpnum) 
	{

		// Start the pool
		ExecutorService executor = Executors.newCachedThreadPool();
		Set<Task> allTasks = new HashSet<>();

		// QUERY1: recupero microcontrolli con condizione
		CompletableFuture<List<Task>> conditionTasksFuture = CompletableFuture.supplyAsync(() -> {
			try {

				List<Task> conditionTask = new ArrayList<>();
				List<ConditionTaskProjection> retrivedConditionTask = assetDataService.findTaskConditions(assetnum, jpnum);
				//controlla che abbia recuperato dei microcontrolli con condizione
				if(Assorted.notNull(retrivedConditionTask)) 
				{
					// Filtra per quelli verificati e trasforma in Task
					conditionTask = retrivedConditionTask.stream()
														 .map(checked -> new Task(
															  checked.getAssetnum(),
															  checked.getTaskDescription(),
															  checked.getSiteid(),
															  checked.getIdMicroControllo(),
															  checked.getStatus(),
															  checked.getClassstructureid(),
															  checked.getClassificationid()
														  )).collect(Collectors.toList());	        		
				}

				return conditionTask;

			} catch (Exception ex) {
				log.error("MicroControlService - retrieveTasks - Errore retrieving conditionTasks query: [{}]", ex);
				throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_0017.getEamCode(), "Error retrieving conditions task (microcontrols)"));
			}
		}, executor);

		// QUERY2: recupero microcontrolli senza condizione
		CompletableFuture<List<Task>> noConditionTasksFuture = CompletableFuture.supplyAsync(() -> {
			try {
				List<Task> noConditionTask = new ArrayList<>();
				List<NoConditionTaskProjection> listNoConditionTask = assetDataService.findTaskNoConditions(assetnum, jpnum);
				log.debug("MicroControlService - retrieveTasks - Finished query findTaskNoConditions ");
				if(Assorted.notNull(listNoConditionTask)) 
				{
					// Filtra per quelli verificati e trasforma in Task
					noConditionTask = listNoConditionTask.stream()
															 .map(unchecked -> new Task(
																  unchecked.getAssetNum(),
																  unchecked.getTaskDescription(),
																  unchecked.getSiteid(),
																  unchecked.getX4microcontrollo(),
																  unchecked.getStatus(),
																  unchecked.getClassstructureid(),
																  unchecked.getClassificationid()))
															 .collect(Collectors.toList());	   
				}
				return noConditionTask;
				
			} catch (Exception ex) {
				log.error("MicroControlService - retrieveTasks - Errore retrieving noConditionTasks query: {}", ex);
				throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_0017.getEamCode(), "Error retrieving no conditions task (microcontrols)"));
			}
		}, executor);

		// FASE FINALE: Attende il completamento di entrambi i task
		CompletableFuture.allOf(conditionTasksFuture, noConditionTasksFuture).join();

		// Unisce i risultati
		try {
			List<Task> conditionTasks = conditionTasksFuture.join();
			List<Task> noConditionTasks = noConditionTasksFuture.join();

			// Aggiunta diretta di tutti i task con condizione
			allTasks.addAll(conditionTasks);
			allTasks.addAll(noConditionTasks);
			
		} finally {
			executor.shutdown(); // Chiude il pool di thread
		}
		return allTasks;
	}
	
	private boolean checkStatus(Task task, String jpnum) throws EAMException, DataLayerException {
		
		if(Objects.equals(Constants.ASSET_LEVEL_3, assetDataService.getLevel(task.getClassStructureId()))){
			AssetData assetData = assetDataService.getAssetDataByAssetNum(task.getAssetnum(), true);
		    log.debug(" - Asset {} for assetnum={}", assetData == null 
		    												? "not found"
		    												: "found", 
		    										 task.getAssetnum());
		    if(assetData == null) {
		    	throw new EAMException(Catalog.EAMErrorMapping.EAM_0019.getEamCode(), "Asset not found for " + task.getAssetnum());
		    }
		    
		    return assetDataService.checkAssetStatus(assetData, jpnum);
		}
		
		
	    return assetDataService.checkStatusAssetAllLevel(task.getStatus(), task.getClassificationId(), jpnum);
	}
	
	@Transactional
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public ResetDto resetImport() {
		
		ResetDto result = new ResetDto();
		
		log.info(" --- Attempting to delete WoAncestor from X4_MICROCONTROLLI_MIG_BATCH ...");
		int wostatus = workOrderRepository.resetMicrocontrolliWoStatus();
		result.setWostatus(wostatus);
		log.info(" --- Deleted {} records from WoStatus.", wostatus);
		
		log.info(" --- Attempting to delete WoAncestor from X4_MICROCONTROLLI_MIG_BATCH ...");
		int woancestors = workOrderRepository.resetMicrocontrolliWoAncestor();
		result.setWoancestors(woancestors);
		log.info(" --- Deleted {} records from WoAncestor.", woancestors);
		
		log.info(" --- Attempting to delete ownerHistories from X4_MICROCONTROLLI_MIG_BATCH ...");
		int ownerHistories = workOrderRepository.resetMicrocontrolliWoOwnerHistory();
		result.setOwnerHistories(ownerHistories);
		log.info(" --- Deleted {} records from WoStatus.", ownerHistories);
		
		log.info(" --- Attempting to delete A_WORKORDER from X4_MICROCONTROLLI_MIG_BATCH ...");
		int aWorkorders = workOrderRepository.resetMicrocontrolliAWorkOrder();
		result.setAWorkorders(aWorkorders);
		log.info(" --- Deleted {} from A_WORKORDER.", aWorkorders);
		
		log.info(" --- Attempting to delete WorkOrder from X4_MICROCONTROLLI_MIG_BATCH ...");
		int workOrders = workOrderRepository.resetMicrocontrolliWorkOrder();
		result.setWorkorders(workOrders);
		log.info(" --- Deleted {} from WorkOrder.", workOrders);
		
		return result;
	}

}
