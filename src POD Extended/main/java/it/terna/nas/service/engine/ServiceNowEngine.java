package it.terna.nas.service.engine;

import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import it.terna.nas.service.exposure.handler.ServiceNowHandler;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ServiceNowEngine
{	
	@Autowired
	private ServiceNowHandler	serviceNowHandler;
	
	private static final String 		TEMPLATE_MESSAGE        = "{} - Transaction '{}' - Consumer '{}' received a Kafka message from Topic='{}' with Offset='{}' on Partition='{}' identified by KAFKA_KEY='{}' ReceivedTopic='{}' at time='{}' with TimeStampType='{}' and body:{} ";

	@KafkaListener(topics = "${broker.endPoints.sendAsset.entityName}", groupId = "${kafka.consumer.groupId}", containerFactory = "kafkaListenerContainerFactory")
	public void sendAsset(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
	{
		try {
			String uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
			logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE ASSET - ", headers, payload, Level.DEBUG);
	        logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE ASSET - ", headers, null, Level.INFO);
	        serviceNowHandler.sendAssetInformation(meta.topic(), uuidResend, payload);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE ASSET - ", headers, e.getMessage(), Level.ERROR);
		}
	    //Dopo Esecuzione
		logKafkaMessage("##############CONSUMED KAFKA MESSAGE ASSET - ", headers, payload, Level.DEBUG);
	}
	
	private void logKafkaMessage(String baseLogString, MessageHeaders headers, String payload, Level logLevel) 
    {
    	Object[] logArguments = {baseLogString,
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
}
