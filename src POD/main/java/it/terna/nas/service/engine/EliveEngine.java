package it.terna.nas.service.engine;

import java.util.Map.Entry;
import java.util.UUID;

import org.jboss.logging.MDC;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.handler.EliveHandler;
import it.terna.nas.service.material.dto.KafkaOdlToEliveRequest;
import it.terna.nas.service.material.dto.KafkaProvvedimentiPianteMessage;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EliveEngine {
	
	@Autowired
	private BrokerEntityConfiguration	brokerEntities;
	
	@Autowired
	private EliveHandler 				eliveHandler;
		
	private static ObjectMapper			objectMapper	= new ObjectMapper();
	
	private static final String 		TEMPLATE_MESSAGE        = "{} - Transaction '{}' - Consumer '{}' received a Kafka message from Topic='{}' with Offset='{}' on Partition='{}' identified by KAFKA_KEY='{}' at time='{}' with TimeStampType='{}' and body:{} ";

	@KafkaListener(topics = "${broker.endPoints.sendOdLToElive.entityName}", 
	               groupId = "${kafka.consumer.groupIdSendOdLToElive}", 
	               containerFactory = "kafkaListenerContainerFactory",
	               autoStartup ="${broker.endPoints.sendOdLToElive.entityEnabledTopic}")
	public void kafkaEliveOdlReceiver(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
	{
		// retrieve topic name from supplied message headers
		String topic = headers.get(KafkaHeaders.RECEIVED_TOPIC).toString();
		String uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);

		// trying to get entity mapped by consumed topic
		Entry<String, BrokerEntityInfo> sourceEntityEntry = brokerEntities.getEndPoints().entrySet().stream().filter(entry -> Assorted.equals(topic, entry.getValue().getEntityName())).findFirst().orElse(null);

		// check for a broker entity to manage
		if (Assorted.notNull(sourceEntityEntry)) 
		{
			String transactionId = UUID.randomUUID().toString();
			MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
			
			try {
				
				// log message received
				logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE SEND ODL TO ELIVE", uuidResend, headers, payload, Level.DEBUG);
		        logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE SEND ODL TO ELIVE", uuidResend, headers, null, Level.INFO);				
		        KafkaOdlToEliveRequest request = objectMapper.readValue(payload, KafkaOdlToEliveRequest.class);
				eliveHandler.sendOdlToElive(topic, request, uuidResend);
			} catch (Exception e) {
				logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE SEND ODL TO ELIVE", uuidResend, headers, e.getMessage(), Level.ERROR);
			}
			finally {
				MDC.clear();
				logKafkaMessage("##############CONSUMED KAFKA MESSAGE SEND ODL TO ELIVE", uuidResend, headers, payload, Level.DEBUG);
			}
		} else {
			// log message discarded because belonging to topic not managed here
			log.warn("Elive Send OdL: Nothing to do. Message from topic [{}] is not to be processed", topic);
		}
	}
	

	@KafkaListener(topics = "${broker.endPoints.sendProvvedimentiPianteToEam.entityName}", 
	               groupId = "${kafka.consumer.groupIdSendProvvedimentiPianteToEam}", 
	               containerFactory = "kafkaListenerContainerFactory",
	               autoStartup ="${broker.endPoints.sendProvvedimentiPianteToEam.entityEnabledTopic}")
	public void kafkaProvvedimentiPianteToEam(@Headers MessageHeaders headers, @Payload String payload) throws EAMException
	{
		//retrieve topic name from supplied message headers
		String topic = headers.get(KafkaHeaders.RECEIVED_TOPIC).toString();

		// trying to get entity mapped by consumed topic
		Entry<String, BrokerEntityInfo> sourceEntityEntry = brokerEntities.getEndPoints().entrySet().stream().filter(entry -> Assorted.equals(topic, entry.getValue().getEntityName())).findFirst().orElse(null);

		// check for a broker entity to manage
		if (Assorted.notNull(sourceEntityEntry)) 
		{
			try {				
				logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE INVIO PROVVEDIMENTI PIANTE", null, headers, payload, Level.DEBUG);
		        logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE INVIO PROVVEDIMENTI PIANTE", null, headers, null, Level.INFO);				
				KafkaProvvedimentiPianteMessage request;
				request = objectMapper.readValue(payload, KafkaProvvedimentiPianteMessage.class);
				eliveHandler.createInterferenza(topic, request);			       
			} 
			catch (EAMException e) {
				logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE INVIO PROVVEDIMENTI PIANTE", null, headers, e.getMessage(), Level.ERROR);
				throw e;
			}
			catch (Exception e) {
				logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE INVIO PROVVEDIMENTI PIANTE", null, headers, e.getMessage(), Level.ERROR);
			} 
		} else {
			// log message discarded because belonging to topic not managed here
			log.warn("Eam invio provvedimenti piante: Nothing to do. Message from topic [{}] is not to be processed", topic);
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