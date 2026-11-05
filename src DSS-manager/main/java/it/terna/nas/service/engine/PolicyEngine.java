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
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.handler.*;
import it.terna.nas.service.material.dto.policy.EventDTO;
import it.terna.nas.service.material.dto.policy.PolicyDTO;
import it.terna.nas.service.material.dto.policy.RecoveryDTO;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class PolicyEngine
{	
	private static final String 	TEMPLATE_MESSAGE        = "{} - Transaction '{}' - Consumer '{}' received a Kafka message from Topic='{}' with Offset='{}' on Partition='{}' identified by KAFKA_KEY='{}' ReceivedTopic='{}' at time='{}' with TimeStampType='{}' and body:{} ";

	@Autowired
	private AssetHandler            assetHandler;

    @Value("${bypassBusinessLogic: false}")
    private Boolean                 bypassBusinessLogic;
    
    @Value("${bypassEventManagerBusinessLogic: false}")
    private Boolean                 bypassEventManagerBusinessLogic;
    
	@Autowired
	private DifformitaHandler       difformitaHandler;
	
	@Autowired
	private HiHandler               hiHandler;
	
    @Autowired
    private InterferenzaHandler     interferenzaHandler;
	
	@Autowired
	private PolicyHandler           policyHandler;

	@Autowired
	private PolicyConverterHandler  policyConverterHandler;
	
	@Autowired
	private AssetEventHandler  		assetEventHandler;
	
	@Autowired
	private ModelloEventHandler  	modelloEventHandler;
	
	@Autowired
	private IdoneitaEventHandler  	idoneitaEventHandler;
	
	@Autowired
	private EccezioneEventHandler 	eccezioneEventHandler;
	
	@Autowired
	private ActivityEventHandler 	activityEventHandler;
	
	@Autowired
	private RecoveryHandler         recoveryHandler;
	
	@Autowired
	private WorkOrderHandler 		workOrderHandler;
	
	private Gson                    gson = KafkaUtility.createGson();
	
	@KafkaListener(topics = "${broker.endPoints.dssPolicyChangeEventHandlerManager.entityName}", groupId = "${kafka.consumer.groupId}", concurrency = "${broker.endPoints.dssPolicyChangeEventHandlerManager.entityConcurrency}", containerFactory = "kafkaListenerContainerFactory")
    @RetryableTopic(attempts = "${broker.endPoints.dssPolicyChangeEventHandlerManager.entityNumberRetry}", include= {EAMException.class, EAMRetryException.class}, backoff = @Backoff(delay = 5000), numPartitions = "${broker.endPoints.dssPolicyChangeEventHandlerManager.entityNumberOfPartition:1}", replicationFactor = "${broker.endPoints.dssPolicyChangeEventHandlerManager.entityNumberOfReplicas:1}")	
	public void dssPolicyChangeEventHandler(@Headers MessageHeaders headers, @Payload String payload)
		throws EAMException
	{
        if (Assorted.isFalse(bypassBusinessLogic))
        {
    		try {
    			logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS - DSS POLICY CHANGE EVENT", headers, payload, Level.DEBUG);
    	        logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS - DSS POLICY CHANGE EVENT", headers, null, Level.INFO);
    	        PolicyDTO request = gson.fromJson(payload, PolicyDTO.class);
    	        policyConverterHandler.elabChangeEventPolicy(request);
    		} catch (EAMException q) {
    			log.error(q.getMessage(), q);
    			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE DSS - DSS POLICY CHANGE EVENT", headers, q.getMessage(), Level.ERROR);
    			throw q;
    		} catch (Exception e) {
    			log.error(e.getMessage(), e);
    			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE DSS - DSS POLICY CHANGE EVENT", headers, e.getMessage(), Level.ERROR);
    		}
            //Dopo Esecuzione
    		logKafkaMessage("##############CONSUMED KAFKA MESSAGE DSS - DSS POLICY CHANGE EVENT", headers, payload, Level.DEBUG);
        }
	}
	
	
	@KafkaListener(topics = "${broker.endPoints.dssPolicyRecoveryEventHandlerManager.entityName}", groupId = "${kafka.consumer.groupId}", containerFactory = "kafkaListenerContainerFactory", concurrency = "${broker.endPoints.dssPolicyRecoveryEventHandlerManager.entityConcurrency}")
    @RetryableTopic(attempts = "${broker.endPoints.dssPolicyRecoveryEventHandlerManager.entityNumberRetry}", include= {EAMException.class, EAMRetryException.class}, backoff = @Backoff(delay = 5000), numPartitions = "${broker.endPoints.dssPolicyRecoveryEventHandlerManager.entityNumberOfPartition:1}", replicationFactor = "${broker.endPoints.dssPolicyRecoveryEventHandlerManager.entityNumberOfReplicas:1}")
	public void dssPolicyRecoveryHandler(@Headers MessageHeaders headers, @Payload String payload)
    	throws EAMException
    {
        if (Assorted.isFalse(bypassBusinessLogic))
        {
    		try {
    			logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS - DSS POLICY RECOVERY", headers, payload, Level.DEBUG);
    	        logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS - DSS POLICY RECOVERY", headers, null, Level.INFO);
    	        RecoveryDTO request = gson.fromJson(payload, RecoveryDTO.class);
    	        policyConverterHandler.elabRecoveryPolicy(request);
    		} catch (EAMException q) {
    			log.error(q.getMessage(), q);
    			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE DSS - DSS POLICY RECOVERY", headers, q.getMessage(), Level.ERROR);
    			throw q;
    		} catch (Exception e) {
    			log.error(e.getMessage(), e);
    			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE DSS - DSS POLICY RECOVERY", headers, e.getMessage(), Level.ERROR);
    		}
            //Dopo Esecuzione
    		logKafkaMessage("##############CONSUMED KAFKA MESSAGE DSS - DSS POLICY RECOVERY", headers, payload, Level.DEBUG);
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
	
	
	@KafkaListener(topics = "${broker.endPoints.dssPeriodicPolicyHandlerManager.entityName}", groupId = "${kafka.consumer.groupId}", concurrency = "${broker.endPoints.dssPeriodicPolicyHandlerManager.entityConcurrency}", containerFactory = "kafkaListenerContainerFactory")
	@RetryableTopic(attempts = "${broker.endPoints.dssPeriodicPolicyHandlerManager.entityNumberRetry}", include= {EAMException.class, EAMRetryException.class}, backoff = @Backoff(delay = 5000), numPartitions = "${broker.endPoints.dssPeriodicPolicyHandlerManager.entityNumberOfPartition:1}", replicationFactor = "${broker.endPoints.dssPeriodicPolicyHandlerManager.entityNumberOfReplicas:1}")
	public void periodicPolicyHandlerManager(@Headers MessageHeaders headers, @Payload String payload)
		throws EAMException
	{	
        if (Assorted.isFalse(bypassBusinessLogic))
        {
    		try {
    			logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS - PERIODIC", headers, payload, Level.DEBUG);
    	        logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS - PERIODIC", headers, null, Level.INFO);
    	        EventDTO request = gson.fromJson(payload, EventDTO.class);
    	        request.setKafkaKey(KafkaUtility.getParamByteToString(headers, KafkaHeaders.RECEIVED_MESSAGE_KEY));
    	        policyHandler.managePeriodicPolicy(request);
    		} catch (EAMException q) {
    			log.error(q.getMessage(), q);
    			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE DSS - PERIODIC", headers, q.getMessage(), Level.ERROR);
    			throw q;
    		} catch (Exception e) {
    			log.error(e.getMessage(), e);
    			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE DSS - PERIODIC", headers, e.getMessage(), Level.ERROR);
    		}
    		
            //Dopo Esecuzione
    		logKafkaMessage("##############CONSUMED KAFKA MESSAGE DSS - PERIODIC", headers, payload, Level.DEBUG);
        }
	}
	
	
	@KafkaListener(topics = "${broker.endPoints.dssEventHandlerManager.entityName}", groupId = "${kafka.consumer.groupId}", concurrency = "${broker.endPoints.dssEventHandlerManager.entityConcurrency}", containerFactory = "kafkaListenerContainerFactory")
	@RetryableTopic(attempts = "${broker.endPoints.dssEventHandlerManager.entityNumberRetry}", include= {EAMException.class, EAMRetryException.class}, backoff = @Backoff(delay = 5000), numPartitions = "${broker.endPoints.dssEventHandlerManager.entityNumberOfPartition:1}", replicationFactor = "${broker.endPoints.dssEventHandlerManager.entityNumberOfReplicas:1}")
	public void eventManager(@Headers MessageHeaders headers, @Payload String payload)
		throws EAMException
	{
        if (Assorted.isFalse(bypassEventManagerBusinessLogic))
        {
    		try {
    			logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS - EVENT", headers, payload, Level.DEBUG);
    	        logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE DSS - EVENT", headers, null, Level.INFO);
    	        EventDTO request = gson.fromJson(payload, EventDTO.class);
    	        request.setKafkaKey(KafkaUtility.getParamByteToString(headers, KafkaHeaders.RECEIVED_MESSAGE_KEY));
    	        
    	        EventType event = EventType.valueOf(request.getType());
    	        
    	        switch (event) 
    	        {
    	        	case POLICY:    policyHandler.managePolicy(request);
    	        	                break;
    
    	        	case ODL:       workOrderHandler.handleCreateOdLForAsset(request);
    	        	                break;
    
    	            case DIFFORMITA:   difformitaHandler.manageDifformita(request);
                                       break;  
                                       
    	            case INTERFERENZA:  interferenzaHandler.manageInterferenza(request);
                                        break;
                                        
    	            case ATTIVITA:  activityEventHandler.handleEvent(request);
		               				break;
		               				
                    case MISURA:  assetEventHandler.handleMisuraEvent(request);
                        		  break;
                    
                    case MODELLO:  modelloEventHandler.handleEvent(request);
        			               break;
        			               
                    case ECCEZIONE:  eccezioneEventHandler.handleEvent(request);
          		  				     break;
      
                    case IDONEITA:  idoneitaEventHandler.handleEvent(request);
		                            break;

                    case HI:            hiHandler.handleEvent(request);
                                        break;
    
                    case ASSET:         assetHandler.manageAsset(request);
                                        break;
                    
                    case ASSETSPEC:     assetHandler.manageAsset(request);
                                        break;
                                        
                    case ODLCLOSED:     workOrderHandler.handleClosedOdLForAsset(request);
                    					break;
                                        
                    case RECOVERY:      recoveryHandler.handleEvent(request);
                                        break;
                                        
    	        	default:        log.error("Unhandled message: {}", event);
    	        }
    	        
    	        
    		} catch (EAMException q) {
    			log.error(q.getMessage(), q);
    			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE DSS - EVENT", headers, q.getMessage(), Level.ERROR);
    			throw q;
    		} catch (Exception e) {
    			log.error(e.getMessage(), e);
    			logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE DSS - EVENT", headers, e.getMessage(), Level.ERROR);
    		}
            //Dopo Esecuzione
    		logKafkaMessage("##############CONSUMED KAFKA MESSAGE DSS - EVENT", headers, payload, Level.DEBUG);
        }
	}
	
	
	@DltHandler
	public void processDltMessage(	@Headers MessageHeaders headers,
									@Header(KafkaHeaders.ORIGINAL_TOPIC) String topic,
									@Header(KafkaHeaders.EXCEPTION_FQCN) String descException,
									@Header(KafkaHeaders.EXCEPTION_STACKTRACE) String stacktrace,
									@Header(KafkaHeaders.EXCEPTION_MESSAGE) String errorMessage,
									@Payload String payload) 
	{
		log.error("call PolicyEngine.processDltMessage({}, {})", payload, topic);
	}
}
