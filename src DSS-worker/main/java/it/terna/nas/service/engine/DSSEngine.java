package it.terna.nas.service.engine;

import static it.terna.nas.service.material.utility.Constants.AGGREGATE_COND_SAVE;
import static it.terna.nas.service.material.utility.Constants.AGGREGATE_FUNE_SAVE;
import static it.terna.nas.service.material.utility.Constants.AGGREGATE_STRUTTURA_SAVE;
import static it.terna.nas.service.material.utility.Constants.TRAINATO_TRAINANTE_SAVE;
import static it.terna.nas.service.material.utility.Constants.TRONCO_SAVE;

import org.slf4j.MDC;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.basement.utility.Common.ENasHeader;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.PolicyRequestEvent;
import it.terna.nas.service.exposure.handler.AggregateCondFuneTelHandler;
import it.terna.nas.service.exposure.handler.PolicyHandler;
import it.terna.nas.service.exposure.handler.TrainatoTrainanteHandler;
import it.terna.nas.service.exposure.handler.TrunkIspectionHandler;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DSSEngine
{	
	private static final String 	   TEMPLATE_MESSAGE        = "{} - Transaction '{}' - Consumer '{}' received a Kafka message from Topic='{}' with Offset='{}' on Partition='{}' identified by KAFKA_KEY='{}' ReceivedTopic='{}' at time='{}' with TimeStampType='{}' and body:{} ";

    @Value("${bypassEventWorkerBusinessLogic: false}")
    private Boolean                   bypassEventWorkerBusinessLogic;

    private Gson                      gson             = KafkaUtility.createGson();

    @Autowired
    private PolicyHandler             policyHandler;
    
    @Autowired
    private AggregateCondFuneTelHandler aggregateCondFuneTelHandler;
    
    @Autowired
    private TrainatoTrainanteHandler trainatoTrainanteHandler;

    @Autowired
    private TrunkIspectionHandler    trunkIspectionHandler; 
	
    @KafkaListener(
            topics = "${broker.endPoints.evaluatePolicy.entityName}",
            groupId = "${kafka.consumer.groupId}",
            containerFactory = "kafkaListenerContainerFactory",
            concurrency = "${broker.endPoints.evaluatePolicy.entityConcurrency}")
    @RetryableTopic(
            attempts = "${broker.endPoints.evaluatePolicy.entityNumberRetry:1}",
            include = {EAMRetryException.class},
            backoff = @Backoff(delay = 5000),
            numPartitions = "${broker.endPoints.evaluatePolicy.entityNumberOfPartition:1}",
            replicationFactor = "${broker.endPoints.evaluatePolicy.entityNumberOfReplicas:1}")
    public void evaluatePolicyConsumer(
            @Headers MessageHeaders headers,
            ConsumerRecordMetadata meta,
            @Payload String payload) throws EAMRetryException
    {
        logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS WORKER - EVALUATE POLICY", headers, null, Level.INFO);
        handleEvaluatePolicy(headers, payload);
        logKafkaMessage("############## CONSUMED KAFKA MESSAGE EVALUATE POLICY - ",
                headers, payload, Level.DEBUG);
    }
    
    @KafkaListener(
            topics = "${broker.endPoints.evaluatePolicyPeriodiche.entityName}",
            groupId = "${kafka.consumer.periodicGroupId}",
            containerFactory = "kafkaListenerContainerFactory",
    		concurrency = "${broker.endPoints.evaluatePolicyPeriodiche.entityConcurrency}")
    @RetryableTopic(
            attempts = "${broker.endPoints.evaluatePolicyPeriodiche.entityNumberRetry:1}",
            include = {EAMRetryException.class},
            backoff = @Backoff(delay = 5000),
            numPartitions = "${broker.endPoints.evaluatePolicyPeriodiche.entityNumberOfPartition:1}",
            replicationFactor = "${broker.endPoints.evaluatePolicyPeriodiche.entityNumberOfReplicas:1}")
    public void evaluatePolicyPeriodiche(
            @Headers MessageHeaders headers,
            ConsumerRecordMetadata meta,
            @Payload String payload) throws EAMRetryException
    {
        logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS WORKER - EVALUATE POLICY PERIODICA", headers, null, Level.INFO);
        handleEvaluatePolicy(headers, payload);
        logKafkaMessage("############## CONSUMED KAFKA MESSAGE EVALUATE POLICY PERIODICA - ",
                headers, payload, Level.DEBUG);
    }


    public void handleEvaluatePolicy(
	        MessageHeaders headers,
	        String payload) throws EAMRetryException
	{
        if (Assorted.isFalse(bypassEventWorkerBusinessLogic))
        {
    		// retrieve topic name from supplied message headers
    		String topic			= headers.get(KafkaHeaders.RECEIVED_TOPIC).toString();
    		String transactionId	= headers.get(ENasHeader.ORIGINAL_TRANSACTION_ID.getKey()) instanceof byte[] ? new String((byte[]) headers.get(ENasHeader.ORIGINAL_TRANSACTION_ID.getKey())) 
    				                    	                                                                             : headers.get(ENasHeader.ORIGINAL_TRANSACTION_ID.getKey(), String.class);
			try 
			{
				// log message received 
				logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS WORKER - EVALUATE POLICY", headers, payload, Level.DEBUG);
				log.debug("  - DSS WORKER - Source Data Collector - Message to process received from topic [{}]", topic);

				// convert received payload
				PolicyRequestEvent	event	= gson.fromJson(payload, PolicyRequestEvent.class);
				switch (event.getEvent())
				{
				    case TRAINATO_TRAINANTE_SAVE:   trainatoTrainanteHandler.saveOdL(event);
				                                    break;
				    
				    case TRONCO_SAVE:               trunkIspectionHandler.saveOdL(event);
				                                    break;
				                                    
				    case AGGREGATE_COND_SAVE:       aggregateCondFuneTelHandler.saveOdL(event);
				                                    break; 
				    
                    case AGGREGATE_FUNE_SAVE:       aggregateCondFuneTelHandler.saveOdL(event);
                                                    break; 
				                                    
                    case AGGREGATE_STRUTTURA_SAVE:  aggregateCondFuneTelHandler.saveOdL(event);
                                                    break; 
                    
				    default: policyHandler.processMessage(event);
				}		
			} catch (EAMRetryException q) {
                logKafkaMessage("############## FAILED TO CONSUME KAFKA MESSAGE POLICY EVALUATE OPERATION - ", headers, q.getMessage(), Level.ERROR);
                log.error("  - DSS WORKER - EVALUATION POLICY - Correlation Key [{}] - An error occurred while reading Message from topic [{}]", transactionId, topic, q);
                throw q;
			} catch (Exception e) {
				log.error("  - DSS WORKER - EVALUATION POLICY - Correlation Key [{}] - An error occurred while reading Message from topic [{}]", transactionId, topic, e);
				logKafkaMessage("############## FAILED TO CONSUME KAFKA MESSAGE POLICY EVALUATE OPERATION - ", headers, e.getMessage(), Level.ERROR);
			}
		}
			
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
	
	
    @DltHandler
    public void processDltMessage(@Headers MessageHeaders headers, 
                                  @Header(KafkaHeaders.ORIGINAL_TOPIC) String topic, 
                                  @Header(KafkaHeaders.EXCEPTION_FQCN) String descException, 
                                  @Header(KafkaHeaders.EXCEPTION_STACKTRACE) String stacktrace,
                                  @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage, 
                                  @Payload String payload)
    {
        log.error("call DSSEngine.processDltMessage({}, {})", payload, topic);
    }

}
