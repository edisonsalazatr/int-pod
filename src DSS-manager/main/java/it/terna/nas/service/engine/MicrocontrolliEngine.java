package it.terna.nas.service.engine;

import com.google.gson.Gson;
import org.slf4j.MDC;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.events.MicrocontrolliStartRequestEvent;
import it.terna.nas.service.exposure.handler.MicrocontrolliHandler;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MicrocontrolliEngine
{	
	private static final String    TEMPLATE_MESSAGE        = "{} - Transaction '{}' - Consumer '{}' received a Kafka message from Topic='{}' with Offset='{}' on Partition='{}' identified by KAFKA_KEY='{}' ReceivedTopic='{}' at time='{}' with TimeStampType='{}' and body:{} ";
	
	@Autowired
	private MicrocontrolliHandler   handler;
	
	@Value("${bypassBusinessLogic: false}")
	private Boolean                bypassBusinessLogic;
	   
	private Gson                   gson = KafkaUtility.createGson();
	
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
	
	@KafkaListener(topics = "${broker.endPoints.dssMicrocontrolliStart.entityName}", 
			   groupId = "${kafka.consumer.groupId}",  
			   containerFactory = "kafkaListenerContainerFactory")
	public void handleMicrocontrolliMigEvent(@Headers MessageHeaders headers, @Payload String payload)
		throws EAMException
	{		
        if (Assorted.isFalse(bypassBusinessLogic))
        {
    		try {
    			logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE DSS - MIGRATE MICROCONTROLLI", headers, payload, Level.DEBUG);
    			logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE DSS - MIGRATE MICROCONTROLLI", headers, null, Level.INFO);
    			MicrocontrolliStartRequestEvent request = gson.fromJson(payload, MicrocontrolliStartRequestEvent.class);
    			handler.prepareMicrocontrolliRequests(request);
    		} catch (Exception e) {
    			log.error(e.getMessage(), e);
    			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE DSS - CHANGE IDONEITA FOR ASSET", headers, e.getMessage(), Level.ERROR);
    		}
        }
        
		//Dopo Esecuzione
		logKafkaMessage("##############CONSUMED KAFKA MESSAGE DSS - CHANGE IDONEITA FOR ASSET", headers, payload, Level.DEBUG);
	}
}
