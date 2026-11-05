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
import it.terna.nas.service.dal.entity.eam.Modello;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.CacheService;
import it.terna.nas.service.dal.service.ModelloQueryService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.AssetModelloChangedEvent;
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
public class ModelloEventHandler
{
    private final BrokerEntityConfiguration brokerEntities;

    private final CacheService cacheService;

    private final KafkaProducerHandler kafkaHandler;

    private final ModelloQueryService modelloQueryService;

    private final PolicyQueryService policyQueryService;
	
	public void handleEvent(EventDTO request) 
	        throws EAMRetryException 
	{
		try {
			
            Map<String, String> attributes  = request.getAttributes();
            String x4CatModelloAssetId          = attributes.get(EventAttributes.X4CATMODELLOASSETID.getValue());
            
            if ( notNull(x4CatModelloAssetId)) {
            	
            	// retrieve classificationId for asset
            	Modello modello = modelloQueryService.getModelloByModelloAssetId(x4CatModelloAssetId);
                if (notNull(modello)) {
                	
                	// retrieve all policies linked by the measure
                    List<X4Policy> policies = policyQueryService.getPolicyByModello(modello.getModello(), modello.getModelloTarget());
                    if (isNotEmpty(policies))
                    {
                    	// evict from cache
            			cacheService.evictCache(Constants.CACHE_MODELLO_ASSET, modello.getModello(), CatalogTipologyType.MODELLO);
                        
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
	

public void notifyModelloChanged(AssetModelloChangedEvent request) 
    throws EAMRetryException, DataLayerException
    {
        log.debug("call sendNotifyModelloChanged({}, {}, {})", request.getX4catmodelloid(),
                                                                       request.getX4catmodelloassetid(),
                                                                       request.getAttributo());

        String x4CatModelloAssetId = request.getX4catmodelloassetid();
        validateModelloExistsOrThrow(x4CatModelloAssetId);

        Date currentDate = new Date();
        EventDTO event = new EventDTO();
        /*
         *  La chiave del messaggio è data dalla concatenazione della stringa
         *  "MODELLO-<request. x4catmodelloassetid>-" più il timestamp in millisecondi.
         */
        String key = EventType.MODELLO.getValue() + SEP4 + x4CatModelloAssetId + SEP4 + currentDate.getTime();

        event.setKafkaKey(key);
        event.setEvent(EventActionType.MODIFY.getValue());
        event.setType(EventType.MODELLO.getValue());
        event.setAttributes(new HashMap<>());
        event.getAttributes().put(EventAttributes.X4CATMODELLOASSETID.getValue(), x4CatModelloAssetId);

        log.debug("sendNotifyModelloChanged ==> {}", event);
        BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);

        kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
    }

    
    private void validateModelloExistsOrThrow(String x4CatModelloAssetId) 
        throws EAMRetryException, DataLayerException
    {
        if (notNull(x4CatModelloAssetId))
        {
            Modello modello = modelloQueryService.getModelloByModelloAssetId(x4CatModelloAssetId);
            if (notNull(modello))
                return;
        }

        String errorMessage = String.format("Cannot find a modello for asset %s", x4CatModelloAssetId);
        throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), errorMessage);
    }
}
