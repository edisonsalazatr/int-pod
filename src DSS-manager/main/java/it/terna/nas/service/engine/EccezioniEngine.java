package it.terna.nas.service.engine;

import com.google.gson.Gson;
import org.slf4j.MDC;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.kafka.annotation.DltHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.ExceptionChangedEvent;
import it.terna.nas.service.exposure.handler.EccezioneEventHandler;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class EccezioniEngine
{	
	private static final String    TEMPLATE_MESSAGE        = "{} - Transaction '{}' - Consumer '{}' received a Kafka message from Topic='{}' with Offset='{}' on Partition='{}' identified by KAFKA_KEY='{}' ReceivedTopic='{}' at time='{}' with TimeStampType='{}' and body:{} ";
	
	@Autowired
	private EccezioneEventHandler  handler;
	
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
	
	
	@KafkaListener(topics = "${broker.endPoints.dssChangeEccezioneAsset.entityName}", 
			   groupId = "${kafka.consumer.groupId}", 
			   concurrency = "${broker.endPoints.dssChangeEccezioneAsset.entityConcurrency}", 
			   containerFactory = "kafkaListenerContainerFactory")
	@RetryableTopic(attempts = "${broker.endPoints.dssChangeEccezioneAsset.entityNumberRetry:1}", include= {EAMRetryException.class}, backoff = @Backoff(delay = 5000), numPartitions = "${broker.endPoints.dssChangeEccezioneAsset.entityNumberOfPartition:1}", replicationFactor = "${broker.endPoints.dssChangeEccezioneAsset.entityNumberOfReplicas:1}")
	public void handleAssetModelloChangedEvent(@Headers MessageHeaders headers, @Payload String payload)
	        throws EAMRetryException
	{		
        if (Assorted.isFalse(bypassBusinessLogic))
        {
    		try {
    			logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS - CHANGE ECCEZIONE FOR ASSET", headers, payload, Level.DEBUG);
    			logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS - CHANGE ECCEZIONE FOR ASSET", headers, null, Level.INFO);
    			ExceptionChangedEvent request = gson.fromJson(payload, ExceptionChangedEvent.class);
    			handler.notifyChanged(request);
    		} catch (EAMRetryException q) {
    			log.error(q.getMessage(), q);
    			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE DSS - CHANGE ECCEZIONE FOR ASSET", headers, q.getMessage(), Level.ERROR);
    			throw q;
    		} catch (Exception e) {
    			log.error(e.getMessage(), e);
    			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE DSS - CHANGE ECCEZIONE FOR ASSET", headers, e.getMessage(), Level.ERROR);
    			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e);
    		}
    		
    		//Dopo Esecuzione
    		logKafkaMessage("##############CONSUMED KAFKA MESSAGE DSS - CHANGE ECCEZIONE FOR ASSET", headers, payload, Level.DEBUG);
        }
	}
	
	
    @DltHandler
    public void processDltMessage(  @Headers MessageHeaders headers,
                                    @Header(KafkaHeaders.ORIGINAL_TOPIC) String topic,
                                    @Header(KafkaHeaders.EXCEPTION_FQCN) String descException,
                                    @Header(KafkaHeaders.EXCEPTION_STACKTRACE) String stacktrace,
                                    @Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
                                    @Payload String payload) 
    {
        log.error("call EccezioniEngine.processDltMessage({}, {})", payload, topic);
    }
}
