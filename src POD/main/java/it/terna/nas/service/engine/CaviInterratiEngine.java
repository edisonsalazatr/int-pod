package it.terna.nas.service.engine;

import java.util.UUID;

import org.jboss.logging.MDC;
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
import it.terna.nas.service.exposure.handler.CaviInterratiHandler;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CaviInterratiEngine
{	

	@Autowired
	private CaviInterratiHandler 				caviInterratiHandler;
	
	private static final String 		TEMPLATE_MESSAGE        =  "{} - Transaction '{}' - Consumer '{}' received a Kafka message from Topic='{}' with Offset='{}' on Partition='{}' identified by KAFKA_KEY='{}' at time='{}' with TimeStampType='{}' and body:{} ";

	
	
	@KafkaListener(topics = "${broker.endPoints.eamCaviInterratiEliminaIspezioni.entityName}", 
				   groupId = "${kafka.consumer.groupIdCaviInterratiEliminaIspezioni}", 
				   containerFactory = "kafkaListenerContainerFactory", 
				   autoStartup ="${broker.endPoints.eamCaviInterratiEliminaIspezioni.entityEnabledTopic}")
	public void sendCancelOperation(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
	{
		String uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
		try {
			String transactionId = UUID.randomUUID().toString();
			MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
			
			logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE DELETE CAVI OPERATION ", uuidResend, headers, payload, Level.DEBUG);
	        logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE DELETE CAVI OPERATION ", uuidResend, headers, null, Level.INFO);
	        		
	        caviInterratiHandler.sendDeletion(meta.topic(), uuidResend, payload);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			logKafkaMessage("############## FAILED TO CONSUME KAFKA MESSAGE DELETE CAVI OPERATION ", uuidResend, headers, e.getMessage(), Level.ERROR);
		}
		finally {
			MDC.clear();
			logKafkaMessage("############## CONSUMED KAFKA MESSAGE DELETE CAVI OPERATION ", uuidResend, headers, payload, Level.DEBUG);
		}
	}

	
	@KafkaListener(topics = "${broker.endPoints.eamCaviInterratiInvioIspezioni.entityName}", 
				   groupId = "${kafka.consumer.groupIdCaviInterratiInvioIspezioni}", 
				   containerFactory = "kafkaListenerContainerFactory",
				   autoStartup ="${broker.endPoints.eamCaviInterratiInvioIspezioni.entityEnabledTopic}")
	public void sendOdL(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
	{
		String uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
		try {
			String transactionId = UUID.randomUUID().toString();
			MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
			
			logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE SEND ODL TO CAVI ", uuidResend, headers, payload, Level.DEBUG);
	        logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE SEND ODL TO CAVI ", uuidResend, headers, null, Level.INFO);
	        caviInterratiHandler.sendOdL(meta.topic(), uuidResend, payload);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			logKafkaMessage("############## FAILED TO CONSUME KAFKA MESSAGE SEND ODL TO CAVI ", uuidResend, headers, e.getMessage(), Level.ERROR);
		}
		finally {
			MDC.clear();
			logKafkaMessage("############## CONSUMED KAFKA MESSAGE SEND ODL TO CAVI ", uuidResend, headers, payload, Level.DEBUG);
		}
	}

	private void logKafkaMessage(String baseLogString, String uuidResend, MessageHeaders headers, String payload, Level logLevel) 
    {
        Object[] logArguments = {baseLogString,
        		uuidResend,
        		headers.get(KafkaHeaders.GROUP_ID),
                headers.get(KafkaHeaders.RECEIVED_TOPIC),
                headers.get(KafkaHeaders.OFFSET),
                headers.get(KafkaHeaders.RECEIVED_PARTITION_ID),
                headers.get(KafkaHeaders.RECEIVED_MESSAGE_KEY),
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