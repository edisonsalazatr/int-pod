package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.END_POINT_MANAGER_EVENTS_HANDLER;
import static it.terna.nas.service.material.utility.Constants.SEP4;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.AssetQueryService;
import it.terna.nas.service.dal.service.CacheService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.ActivityChangedEvent;
import it.terna.nas.service.material.dto.policy.EventDTO;
import it.terna.nas.service.material.utility.Catalog.CatalogTipologyType;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Catalog.EventAttributes;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ActivityEventHandler
{
	@Autowired
	private BrokerEntityConfiguration brokerEntities;	
	
	@Autowired
	private CacheService cacheService;
	
	@Autowired
	private KafkaProducerHandler kafkaHandler;
	
	@Autowired
	private AssetQueryService assetQueryService;
	
	@Autowired
	private PolicyQueryService policyQueryService;
	
	public void handleEvent(EventDTO request) 
	        throws EAMException 
	{
		try {
			
            Map<String, String> attributes  = request.getAttributes();
            String jpnum          = attributes.get(EventAttributes.JPNUM.getValue());
            
            if (notNull(jpnum)) 
            {
            	cacheService.evictCache(Constants.CACHE_MACROATTIVITA, jpnum, CatalogTipologyType.ATTIVITA);
            	
            	// retrieve classificationId for asset
            	List<X4Policy> policies = policyQueryService.getPolicyByJpnum(jpnum);
                if (isNotEmpty(policies)) 
                {
                    for (X4Policy policy: policies)
                    {
                    	// evict from cache
            			cacheService.evictCache(Constants.CACHE_POLICY, String.valueOf(policy.getX4PolicyId()), CatalogTipologyType.POLICY);
                    }
                }
                
                // retrieve assets 
            	List<Asset> assets = assetQueryService.getAssetByJpnum(jpnum);
                if (isNotEmpty(assets)) {
                    
                    for (Asset asset: assets)
                    {
                    	// evict from cache
            			cacheService.evictCache(Constants.CACHE_ASSET, asset.getAssetNum(), CatalogTipologyType.ASSET);
                    }
                }
            } else {
            	log.error("  - Message doesn't carry a value for jpnum [{}]", request);
            }
            
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
        }
        
    }
	

	public void notifyChanged(ActivityChangedEvent request)
	{
		log.debug("call notifyChanged({}, {}, {}, {})", request.getJpnum(), 
													request.getDescription(),
													request.getClassstructureid(),
													request.getStatus());
		
		if (Constants.ACTIVITY_STATUS_ACTIVE.equals(request.getStatus()))
		{
    		Date currentDate = new Date();
    		EventDTO event   = new EventDTO();
    		
    		String key = EventType.ATTIVITA.getValue() + SEP4 + request.getJobplanid() + SEP4 + currentDate.getTime();
    		
    		event.setKafkaKey(key);
    		event.setEvent(EventActionType.MODIFY.getValue());
    		event.setType(EventType.ATTIVITA.getValue());
    		event.setAttributes(new HashMap<>());
    		event.getAttributes().put(EventAttributes.JPNUM.getValue(), request.getJpnum());
    
    		log.debug("notifyChanged ==> {}", event);
    		BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
    		
    		kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
		}
	}
}
