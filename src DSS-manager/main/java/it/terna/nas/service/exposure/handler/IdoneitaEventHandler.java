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
import it.terna.nas.service.dal.entity.eam.X4TabIdoneita;
import it.terna.nas.service.dal.service.CacheService;
import it.terna.nas.service.dal.service.IdoneitaQueryService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.IdoneitaChangedEvent;
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
public class IdoneitaEventHandler
{
	private final BrokerEntityConfiguration brokerEntities;	
	private final CacheService cacheService;
	private final KafkaProducerHandler kafkaHandler;
	private final IdoneitaQueryService idoneitaQueryService;
	private final PolicyQueryService policyQueryService;
	
	public void handleEvent(EventDTO request) 
	        throws EAMRetryException 
	{
		try {
			
            Map<String, String> attributes  = request.getAttributes();
            String x4tabidoneitaid          = attributes.get(EventAttributes.X4TABIDONEITAID.getValue());
            
            // evict from cache
			cacheService.evictCache(Constants.CACHE_IDONEITA, x4tabidoneitaid, CatalogTipologyType.IDONEITA);
                        
            if ( notNull(x4tabidoneitaid)) {
            	
            	// retrieve classificationId for asset
            	X4TabIdoneita idoneita = idoneitaQueryService.getIdoneitaById(x4tabidoneitaid);
                if (notNull(idoneita)) {
                	
                	// retrieve all policies linked by the measure
                    List<X4Policy> policies = policyQueryService.getPolicyByClassificationFromIdoneita(idoneita.getTipologiaAsset());
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
            	log.error("  - Message doesn't carry a value for x4tabidoneitaid [{}]", request);
            }
            
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
        }
        
    }
	

	public void notifyChanged(IdoneitaChangedEvent request)
	        throws EAMRetryException, DataLayerException
	{
		log.debug("call notifyChanged({}, {}, {})", request.getX4tabidoneitaid(), 
												            request.getTipologiaasset(),
												            request.getStatoidoneita());
		
		validateIdoneitaState(request);
		Date currentDate = new Date();
		EventDTO event = new EventDTO();
		
		String key = EventType.IDONEITA.getValue() + SEP4 + request.getX4tabidoneitaid() + SEP4 + currentDate.getTime();
		
		event.setKafkaKey(key);
		event.setEvent(EventActionType.MODIFY.getValue());
		event.setType(EventType.IDONEITA.getValue());
		event.setAttributes(new HashMap<>());
		event.getAttributes().put(EventAttributes.X4TABIDONEITAID.getValue(), request.getX4tabidoneitaid());

		log.debug("notifyChanged ==> {}", event);
		BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
		
		kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
	}
	
	private void validateIdoneitaState(IdoneitaChangedEvent request) 
        throws EAMRetryException, DataLayerException 
{
		String x4tabidoneitaid = request.getX4tabidoneitaid();
		String statoIdoneita = request.getStatoidoneita();
		String tipologiaAsset = request.getTipologiaasset();
		if (!notNull(x4tabidoneitaid)) 
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(),	"Message doesn't carry a value for x4tabidoneita");
		
		X4TabIdoneita idoneita = idoneitaQueryService.getIdoneitaById(x4tabidoneitaid);
		if (!notNull(idoneita)) 
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Idoneita not found for x4tabidoneita " + x4tabidoneitaid);
		
		String storedState = idoneita.getValidState();
		if (!notNull(statoIdoneita) || !notNull(storedState) || !storedState.equals(statoIdoneita)) 
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Invalid statoIdoneita for x4tabidoneita " + x4tabidoneitaid + ": expected " + storedState + " but received " + statoIdoneita);
		
		String storedTipologia = idoneita.getTipologiaAsset();
		if (!notNull(tipologiaAsset) || !notNull(storedTipologia) || !storedTipologia.equals(tipologiaAsset)) 
			throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Invalid tipologiaAsset for x4tabidoneita " + x4tabidoneitaid + ": expected " + storedTipologia + " but received " + tipologiaAsset);
	}
}
