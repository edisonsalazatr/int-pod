package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.END_POINT_MANAGER_EVENTS_HANDLER;
import static it.terna.nas.service.material.utility.Constants.SEP4;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.entity.eam.X4TabEccezione;
import it.terna.nas.service.dal.service.CacheService;
import it.terna.nas.service.dal.service.EccezioniQueryService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.ExceptionChangedEvent;
import it.terna.nas.service.material.dto.policy.EventDTO;
import it.terna.nas.service.material.utility.Catalog.CatalogTipologyType;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Catalog.EventAttributes;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class EccezioneEventHandler
{
	private final BrokerEntityConfiguration brokerEntities;		
	private final CacheService cacheService;	
	private final KafkaProducerHandler kafkaHandler;
	private final EccezioniQueryService eccezioniQueryService;
	private final PolicyQueryService policyQueryService;
	
	public void handleEvent(EventDTO request) throws EAMRetryException {
		
		try {
			
            Map<String, String> attributes  = request.getAttributes();
            String x4tabeccezioneid          = attributes.get(EventAttributes.X4TABECCEZIONEID.getValue());
            
            // evict from cache
			cacheService.evictCache(Constants.CACHE_ECCEZIONI, x4tabeccezioneid, CatalogTipologyType.ECCEZIONE);
         
            if ( notNull(x4tabeccezioneid)) {
            	
            	// retrieve classificationId for asset
            	X4TabEccezione eccezione = eccezioniQueryService.getEccezioneById(x4tabeccezioneid);
                if (notNull(eccezione)) {
                	
                	// retrieve all policies linked by the measure
                    List<X4Policy> policies = policyQueryService.getPolicyByClassificationFromEccezioni(eccezione.getTipologiaAsset());
                    if (isNotEmpty(policies))
                    {
                    	   
                        BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
                        
                        for (X4Policy policy: policies)
                        {

                            String policyStr        = policy.getX4PolicyId().toString();
                            String correlationKey   = policyStr + SEP4 + KafkaUtility.getTimeStamp(request.getKafkaKey());
                            
                            EventDTO event     = new EventDTO();
                            event.setKafkaKey(correlationKey);
                            event.setTipology(policy.getTipoPolicy());
                            event.setEvent(EventActionType.MODIFY.getValue());
                            event.setType(EventType.POLICY.getValue());
                            event.setPolicy(policyStr);
                            
                            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
                        }
                    }
                } else {
                	log.error("  - Cannot find a classificationId for asset {}", request.getAsset());
                }
            } else {
            	log.error("  - Message doesn't carry a value for x4MisureAssetId [{}]", request);
            }
            
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
        }
        
    }
	

	public void notifyChanged(ExceptionChangedEvent request)
	        throws EAMRetryException, DataLayerException
	{
		log.debug("call notifyChanged({}, {}, {})", request.getX4tabeccezioniid(), 
												            request.getIdAttivitaExt(),
												            request.getStatoIdoneo());
		
		validateEccezioneState(request);
		
		Date currentDate = new Date();
		EventDTO event = new EventDTO();
		/*
		 *  La chiave del messaggio è data dalla concatenazione della stringa 
		 *  "MODELLO-<request. x4catmodelloassetid>-" più il timestamp in millisecondi.
		 */
		String key = EventType.ECCEZIONE.getValue() + SEP4 + request.getX4tabeccezioniid() + SEP4 + currentDate.getTime();
		
		event.setKafkaKey(key);
		event.setEvent(EventActionType.MODIFY.getValue());
		event.setType(EventType.ECCEZIONE.getValue());
		event.setAttributes(new HashMap<>());
		event.getAttributes().put(EventAttributes.X4TABECCEZIONEID.getValue(), request.getX4tabeccezioniid());

		log.debug("notifyChanged ==> {}", event);
		BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
		
		kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
	}


	private void validateEccezioneState(ExceptionChangedEvent request) 
        throws EAMRetryException, DataLayerException 
    {
		String x4tabeccezioniid = request.getX4tabeccezioniid();
		String statoIdoneo      = request.getStatoIdoneo();
		if (!notNull(x4tabeccezioniid))
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Message doesn't carry a value for x4tabeccezioniid");

		X4TabEccezione eccezione = eccezioniQueryService.getEccezioneById(x4tabeccezioniid);
		if (!notNull(eccezione))
		    throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Eccezione not found for x4tabeccezioniid " + x4tabeccezioniid);

		String storedState = eccezione.getValidState();
		if (!notNull(statoIdoneo) || !notNull(storedState) || !storedState.equals(statoIdoneo)) 
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Invalid statoIdoneo for x4tabeccezioniid " + x4tabeccezioniid + ": expected " + storedState + " but received " + statoIdoneo);
	}
}
