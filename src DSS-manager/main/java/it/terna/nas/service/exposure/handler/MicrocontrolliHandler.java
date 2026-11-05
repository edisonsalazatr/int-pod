package it.terna.nas.service.exposure.handler;



import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.pool.ExecutorConfigurator;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.basement.utility.Values;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.X4MicrocontrolloMigBatch;
import it.terna.nas.service.dal.service.X4MicrocontrolliQueryService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.MicrocontrolliRequestEvent;
import it.terna.nas.service.exposure.events.MicrocontrolliStartRequestEvent;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MicrocontrolliHandler
{
	@Autowired
	private BrokerEntityConfiguration brokerEntities;	
	
	@Autowired
	private KafkaProducerHandler kafkaHandler;
	
	@Autowired
	private X4MicrocontrolliQueryService microcontrolliService;
	
	@Autowired
	protected ExecutorConfigurator executorConfigurator;
	
	@Value("${batchSizeMicrocontrolli:500}")
	private Integer batchSize;
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public void prepareMicrocontrolliRequests(MicrocontrolliStartRequestEvent request)
		throws EAMException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call loading orders for import ({}) |{}|", request.getImportKey(), startTime);
		
		
		Long lastId = 0l;
		int batchCount = 0;
		int totalProcessed = 0;
		
		try {
			
			while(true) {
				long batchStart = System.currentTimeMillis();
				List<X4MicrocontrolloMigBatch> batch = microcontrolliService.loadBatchByStatus(lastId, batchSize);
				if(batch.isEmpty()) break;
				
				Long firstId = batch.get(Values.FIRST_ELEMENT).getId();
				Long lastInBatch = batch.get(batch.size() -1).getId();
				
				log.info("Processing batch #{} (IDs {} - {}), size = {} [Started={}]", ++batchCount, firstId, lastInBatch, batch.size(), batchStart);
				
				List<CompletableFuture<Void>> futures = batch.stream()
															 .map(entity -> CompletableFuture.runAsync(() -> {
																 processRecord(entity);
															 }, executorConfigurator.getExecutorService()))
															 .toList();
				CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
				
				totalProcessed += batch.size();
				lastId = lastInBatch;
				
				log.info(" Batch #{} completed. Time: {} ms", batchCount,  System.currentTimeMillis() - batchStart);
			}
			long duration = System.currentTimeMillis();
			log.info(" Batch update completed. {} records processed in batches. Time: {} ms", totalProcessed, batchCount, duration);
			
		} 
		catch(Exception q) {
			log.error(q.getMessage(), q);
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), q.getMessage(), q);
		}
		
		long endTime = System.currentTimeMillis();
		log.debug("end loading orders({}) |{}|", request.getImportKey(), endTime - startTime);
	}
	
	private void processRecord(X4MicrocontrolloMigBatch entity) {
		BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(Constants.END_POINT_WORKER_MICROCONTROLLI);
		
		try {
			 
			 if (microcontrolliService.updateStatusBulk(entity.getId(), "RUNNING")) {						
				MicrocontrolliRequestEvent event = createEvent(EventActionType.MODIFY.getValue(), entity);
				
				// send kafka message
				kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
			}
		 } 
		 catch(DataLayerException dle) {
			log.error(dle.getMessage(), dle);
			throw new RuntimeException(
					new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), dle.getMessage(), dle)); 
		}
	}

	private MicrocontrolliRequestEvent createEvent(String eventType, X4MicrocontrolloMigBatch entity) {
		MicrocontrolliRequestEvent event = new MicrocontrolliRequestEvent();
		event.setWonum(entity.getWonum());
		event.setAssetnum(entity.getAssetnum());
		event.setClassStructureId(entity.getClassStructureIdAsset());
		event.setJpnum(entity.getJpnum());
		event.setEvent(eventType);
		event.setRecordId(entity.getId().toString());
		return event;
	}
	
	public void startBatch() {
		BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(Constants.END_POINT_BATCH_MICROCONTROLLI);
		
		MicrocontrolliStartRequestEvent event = new MicrocontrolliStartRequestEvent();
		event.setEvent(EventActionType.ADD.getValue());
		
		long time = (new Date()).getTime();
		event.setImportKey("START_MC_BATCH-" + time);

		// send kafka message
		kafkaHandler.shoot(EOperation.CREATE, topicKafka, List.of(event));
	}

}
