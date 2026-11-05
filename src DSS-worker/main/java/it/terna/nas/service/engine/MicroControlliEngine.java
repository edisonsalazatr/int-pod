package it.terna.nas.service.engine;

import com.google.gson.Gson;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.MDC;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.basement.utility.Common.ENasHeader;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.MicrocontrolliRequestEvent;
import it.terna.nas.service.exposure.handler.MicrocontrolliHandler;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MicroControlliEngine
{	
	private static final String 	   TEMPLATE_MESSAGE        = "{} - Transaction '{}' - Consumer '{}' received a Kafka message from Topic='{}' with Offset='{}' on Partition='{}' identified by KAFKA_KEY='{}' ReceivedTopic='{}' at time='{}' with TimeStampType='{}' and body:{} ";

   
    private Gson                      gson             = KafkaUtility.createGson();
    
    @Autowired
    private MicrocontrolliHandler microcontrolliHandler;
    
	@KafkaListener(topics = "${broker.endPoints.workerHandleMicrocontrolli.entityName}", 
				   groupId = "${kafka.consumer.secondaryGroupIdList}", 
				   containerFactory = "kafkaListenerContainerFactory",
				   concurrency = "${broker.endPoints.workerHandleMicrocontrolli.entityConcurrency:3}")
	//@RetryableTopic(attempts = "${broker.endPoints.workerHandleMicrocontrolli.entityNumberRetry:1}", include= {EAMRetryException.class}, backoff = @Backoff(delay = 5000), numPartitions = "${broker.endPoints.workerHandleMicrocontrolli.entityNumberOfPartition:1}", replicationFactor = "${broker.endPoints.workerHandleMicrocontrolli.entityNumberOfReplicas:1}")
	public void consumeRequest(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, ConsumerRecord<String, String> consumerRecord, @Payload String payload)           
	        throws EAMRetryException
	{
		String topic			= headers.get(KafkaHeaders.RECEIVED_TOPIC).toString();
		String transactionId	= headers.get(ENasHeader.ORIGINAL_TRANSACTION_ID.getKey()) instanceof byte[] ? new String((byte[]) headers.get(ENasHeader.ORIGINAL_TRANSACTION_ID.getKey())) 
				                    	                                                                             : headers.get(ENasHeader.ORIGINAL_TRANSACTION_ID.getKey(), String.class);
		try 
		{
			// log message received 
			logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS WORKER - MICROCONTROLLI PER WORKORDER", headers, payload, Level.DEBUG);
	        logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS WORKER - MICROCONTROLLI PER WORKORDER", headers, null, Level.INFO);
			log.debug("  - DSS WORKER - Source Data Collector - Message to process received from topic [{}]", topic);
        
	        // convert received payload
			MicrocontrolliRequestEvent	event	= gson.fromJson(payload, MicrocontrolliRequestEvent.class);
			
			microcontrolliHandler.processMessage(event);
			
		} catch (EAMRetryException q) {
            log.error("  - DSS WORKER - MICROCONTROLLI PER WORKORDER - Correlation Key [{}] - An error occurred while reading Message from topic [{}]", transactionId, topic, q);
            logKafkaMessage("############## FAILED TO CONSUME KAFKA MESSAGE MICROCONTROLLI PER WORKORDER OPERATION - ", headers, q.getMessage(), Level.ERROR);
           // throw q;
		} catch (Exception e) {
            log.error("  - DSS WORKER - MICROCONTROLLI PER WORKORDER - Correlation Key [{}] - An error occurred while reading Message from topic [{}]", transactionId, topic, e);
            logKafkaMessage("############## FAILED TO CONSUME KAFKA MESSAGE MICROCONTROLLI PER WORKORDER OPERATION - ", headers, e.getMessage(), Level.ERROR);
		}
		
		logKafkaMessage("############## CONSUMED KAFKA MESSAGE MICROCONTROLLI PER WORKORDER - ", headers, payload, Level.DEBUG);
	}

	
	private void logKafkaMessage(String baseLogString, MessageHeaders headers, String payload, Level logLevel) 
    {
    	Object[] logArguments = {baseLogString,
    	        MDC.get(Common.MDC_TRANSACTION_ID),
    			headers.get(KafkaHeaders.CONSUMER),
    			headers.get(KafkaHeaders.TOPIC),
    			headers.get(KafkaHeaders.OFFSET),
    			headers.get(KafkaHeaders.RECEIVED_PARTITION_ID),
    			headers.get(KafkaHeaders.RECEIVED_MESSAGE_KEY),
    			headers.get(KafkaHeaders.RECEIVED_TOPIC),
    			headers.get(KafkaHeaders.RECEIVED_TIMESTAMP),
    			headers.get(KafkaHeaders.TIMESTAMP_TYPE),
    			payload};
    	
    	
    	Pair<String, Object[]> pair = Pair.of(TEMPLATE_MESSAGE, logArguments);
    			
    	switch(logLevel) {
    		case WARN  -> log.warn(pair.getFirst(), pair.getSecond());
    		case INFO  -> log.info(pair.getFirst(), pair.getSecond());
    		case DEBUG -> log.debug(pair.getFirst(), pair.getSecond());
    		case ERROR -> log.error(pair.getFirst(), pair.getSecond());
    		case TRACE -> log.trace(pair.getFirst(), pair.getSecond());
    	}
    }
	
	
//    @DltHandler
//    public void processDltMessage(@Headers MessageHeaders headers, 
//                                  @Header(KafkaHeaders.ORIGINAL_TOPIC) String topic, 
//                                  @Header(KafkaHeaders.EXCEPTION_FQCN) String descException, 
//                                  @Header(KafkaHeaders.EXCEPTION_STACKTRACE) String stacktrace,
//                                  @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage, 
//                                  @Payload String payload)
//    {
//        log.error("call MicroControlliEngine.processDltMessage({}, {})", payload, topic);
//    }
}
