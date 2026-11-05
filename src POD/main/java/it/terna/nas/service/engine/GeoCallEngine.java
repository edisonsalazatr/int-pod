package it.terna.nas.service.engine;

import java.util.Objects;
import java.util.UUID;

import org.slf4j.MDC;
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

import it.terna.kernel.basement.utility.Common;
import it.terna.nas.service.exposure.handler.AnagraficaHandler;
import it.terna.nas.service.exposure.handler.GeoCallHandler;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;
import static it.terna.nas.service.material.utility.Constants.INVIO_ODL;
import static it.terna.nas.service.material.utility.Constants.ANNULLA_BLOCCO_ODL;

@Slf4j
@Configuration
public class GeoCallEngine
{	
	@Autowired
	private GeoCallHandler 		geoCallHandler;
	
	@Autowired
	private AnagraficaHandler	anagraficaHandler;
	
	private static final String 		TEMPLATE_MESSAGE        = "{} - Transaction '{}' - Consumer '{}' received a Kafka message from Topic='{}' with Offset='{}' on Partition='{}' identified by KAFKA_KEY='{}' at time='{}' with TimeStampType='{}' and body:{} ";
	private static final String			OPERATION_NOT_ALLOWED	= "Received an OPERATION value that's not allowed.";
	
	@KafkaListener(autoStartup ="${broker.endPoints.eamNotifyToGeocall.entityEnabledTopic}", topics = "${broker.endPoints.eamNotifyToGeocall.entityName}", groupId = "${kafka.consumer.groupIdEamNotifyToGeocall}", containerFactory = "kafkaListenerContainerFactory")
	public void notifyGeoCall(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
	{
		String transactionId = UUID.randomUUID().toString();
		MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
		String uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
		logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE NOTIFY GEOCALL", uuidResend, headers, payload, Level.DEBUG);
        logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE NOTIFY GEOCALL", uuidResend, headers, null, Level.INFO);
		try {
			String operation = KafkaUtility.getParamByteToString(headers, Constants.OPERATION_PARAM);
			log.debug("call notifyGeoCall(uuidResend={}, operation={}, payload={}) |{}|", uuidResend, operation, payload, System.currentTimeMillis());
			switch(Objects.requireNonNull(operation)) {
				case INVIO_ODL: 
					geoCallHandler.sendOdL(meta.topic(), uuidResend, operation, payload);
					break;
				case ANNULLA_BLOCCO_ODL:
					geoCallHandler.sendCancelOrBlockOdL(meta.topic(), uuidResend, operation, payload);
					break;
				default:
					logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE NOTIFY GEOCALL", uuidResend, headers, OPERATION_NOT_ALLOWED, Level.ERROR);
					break;
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE NOTIFY GEOCALL", uuidResend, headers, e.getMessage(), Level.ERROR);
		}
        //Dopo Esecuzione
		logKafkaMessage("##############CONSUMED KAFKA MESSAGE NOTIFY GEOCALL", uuidResend, headers, payload, Level.DEBUG);
		MDC.clear();
	}
		
	@KafkaListener(autoStartup ="${broker.endPoints.sendTypeAsset.entityEnabledTopic}", topics = "${broker.endPoints.sendTypeAsset.entityName}", groupId = "${kafka.consumer.groupIdSendTypeAsset}", containerFactory = "kafkaListenerContainerFactory")
	public void sendTypeAsset(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
	{
		String transactionId = UUID.randomUUID().toString();
		MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
		String uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
		logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE SEND TYPE ASSET", uuidResend, headers, payload, Level.DEBUG);
        logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE SEND TYPE ASSET", uuidResend, headers, null, Level.INFO);
		try {
			log.debug("call sendTypeAsset(uuidResend={}, payload={}) |{}|", uuidResend, payload, System.currentTimeMillis());
	        geoCallHandler.sendTypeAsset(meta.topic(), uuidResend, payload);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE SEND TYPE ASSET", uuidResend, headers, e.getMessage(), Level.ERROR);
		}
        //Dopo Esecuzione
		logKafkaMessage("##############CONSUMED KAFKA MESSAGE SEND TYPE ASSET", uuidResend, headers, payload, Level.DEBUG);
		MDC.clear();
	}
	
	@KafkaListener(autoStartup ="${broker.endPoints.sendAsset.entityEnabledTopic}",  topics = "${broker.endPoints.sendAsset.entityName}", groupId = "${kafka.consumer.groupIdModifyAsset}", containerFactory = "kafkaListenerContainerFactory")
	public void sendAsset(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
	{
		String transactionId = UUID.randomUUID().toString();
		MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
		String uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
		logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE ASSET", uuidResend, headers, payload, Level.DEBUG);
        logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE ASSET", uuidResend, headers, null, Level.INFO);
		try {
	        log.debug("call sendAsset(uuidResend={}, payload={}) |{}|", uuidResend, payload, System.currentTimeMillis());
	        anagraficaHandler.sendAssetInformation(meta.topic(), uuidResend, payload);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE ASSET", uuidResend, headers, e.getMessage(), Level.ERROR);
		}
	    //Dopo Esecuzione
		logKafkaMessage("##############CONSUMED KAFKA MESSAGE ASSET", uuidResend, headers, payload, Level.DEBUG);
		MDC.clear();
	}
	
	@KafkaListener(autoStartup ="${broker.endPoints.sendSapStornoGeocall.entityEnabledTopic}", topics = "${broker.endPoints.sendSapStornoGeocall.entityName}", groupId = "${kafka.consumer.groupIdSendSapStornoToGeocall}", containerFactory = "kafkaListenerContainerFactory")
	public void sendSapStorno(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
	{
		String transactionId = UUID.randomUUID().toString();
		MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
		String uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
		logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE GEOCALL STORNO", uuidResend, headers, payload, Level.DEBUG);
        logKafkaMessage("############### TRY TO CONSUME KAFKA MESSAGE GEOCALL STORNO", uuidResend, headers, null, Level.INFO);
		try {
			log.debug("call sendSapStorno(uuidResend={}, payload={}) |{}|", uuidResend, payload, System.currentTimeMillis());
	        geoCallHandler.sendSapStorno(meta.topic(), uuidResend, payload);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE GEOCALL STORNO", uuidResend, headers, e.getMessage(), Level.ERROR);
		}
	    //Dopo Esecuzione
		logKafkaMessage("##############CONSUMED KAFKA MESSAGE GEOCALL STORNO", uuidResend, headers, payload, Level.DEBUG);
		MDC.clear();
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