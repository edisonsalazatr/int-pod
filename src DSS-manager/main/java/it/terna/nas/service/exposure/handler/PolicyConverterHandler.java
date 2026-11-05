package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.*;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.AX4Policy;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.CacheService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.material.dto.policy.EventDTO;
import it.terna.nas.service.material.dto.policy.PolicyDTO;
import it.terna.nas.service.material.dto.policy.RecoveryDTO;
import it.terna.nas.service.material.utility.Catalog.CatalogTipologyType;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Catalog.EventAttributes;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.Catalog.PolicyAttributes;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PolicyConverterHandler
{
	@Autowired
	private BrokerEntityConfiguration brokerEntities;	

    @Autowired
    private CacheService cacheService;
    
	@Autowired
	private KafkaProducerHandler kafkaHandler;
	
	@Autowired
	private PolicyQueryService policyQueryService;
	
	@Autowired
	private X4DssPolicyRunnerQueryService x4DssPolicyRunnerQueryService;
	
	public void elabChangeEventPolicy(PolicyDTO request) 
			throws EAMException
	{
		Long startTime = System.currentTimeMillis();
		log.debug("call elabChangeEventPolicy({}) |{}|", request, startTime);
		
		try {
		    if ((!POLICY_ATTIVA_STATUS.equals(request.getStatus())))
		    {	
		        log.info("Policy {} has status {} and tipology {}", request.getX4policyid(), request.getStatus(), request.getTipopolicy());
		        return;
		    }
			
			AX4Policy policy 	= policyQueryService.getPolicyHistory(request.getX4policyid(), request.getVersion());
			EventDTO eventDto 	= new EventDTO();
			Long currentDate	= System.currentTimeMillis(); 
			eventDto.setPolicy(request.getX4policyid());
			eventDto.setTipology(request.getTipopolicy());
			eventDto.setType(EventType.POLICY.name());
			eventDto.setKafkaKey(request.getX4policyid() + SEP4 + currentDate); 
			
			if (isNull(policy))
			{
				eventDto.setEvent(EventActionType.ADD.name());
			} else {
				Boolean changeFrequenza = 	(isNull(request.getFrequenza()) && notNull(policy.getFrequenza())) ||
											(notNull(request.getFrequenza()) && isNull(policy.getFrequenza())) ||
											(notNull(request.getFrequenza()) && notNull(policy.getFrequenza()) && (!request.getFrequenza().equals(policy.getFrequenza())));

				Boolean changeDeferrability = 	(isNull(request.getDifferibilita()) && notNull(policy.getDifferibilita())) ||
												(notNull(request.getDifferibilita()) && isNull(policy.getDifferibilita())) ||
												(notNull(request.getDifferibilita()) && notNull(policy.getDifferibilita()) && (!request.getDifferibilita().equals(policy.getDifferibilita())));

				Boolean changeMaxAdvance = 	(isNull(request.getMaxanticipo()) && notNull(policy.getMaxAnticipo())) ||
											(notNull(request.getMaxanticipo()) && isNull(policy.getMaxAnticipo())) ||
											(notNull(request.getMaxanticipo()) && notNull(policy.getMaxAnticipo()) && (!request.getMaxanticipo().equals(policy.getMaxAnticipo())));

				Boolean changeMaxSnooze	= 	(isNull(request.getMaxposticipo()) && notNull(policy.getMaxPosticipo())) ||
											(notNull(request.getMaxposticipo()) && isNull(policy.getMaxPosticipo())) ||
											(notNull(request.getMaxposticipo()) && notNull(policy.getMaxPosticipo()) && (!request.getMaxposticipo().equals(policy.getMaxPosticipo())));
				
				Boolean changeRifTemporale = (isNull(request.getX4riftemporale()) && notNull(policy.getX4RifTemporale())) ||
											 (notNull(request.getX4riftemporale()) && isNull(policy.getX4RifTemporale())) ||
											 (notNull(request.getX4riftemporale()) && notNull(policy.getX4RifTemporale()) && (!request.getX4riftemporale().equals(policy.getX4RifTemporale())));
				
				eventDto.setEvent(EventActionType.MODIFY.name());
				eventDto.setAttributes(new HashMap<>());
				eventDto.getAttributes().put(PolicyAttributes.FREQUENCY.name(), 	changeFrequenza.toString());
				eventDto.getAttributes().put(PolicyAttributes.DEFERRABILITY.name(), changeDeferrability.toString());
				eventDto.getAttributes().put(PolicyAttributes.MAX_ADVANCE.name(), 	changeMaxAdvance.toString());
				eventDto.getAttributes().put(PolicyAttributes.MAX_SNOOZE.name(), 	changeMaxSnooze.toString());
				eventDto.getAttributes().put(PolicyAttributes.RIF_TEMPORALE.name(), changeRifTemporale.toString());
			}
			
			cacheService.evictCache(CACHE_POLICY, request.getX4policyid(), CatalogTipologyType.POLICY);
			BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
			kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(eventDto));
			
			Long endTime = System.currentTimeMillis();
			log.info("end elabChangeEventPolicy({}) |{}|", request, endTime - startTime);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
			throw new EAMException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e);
		}
	}


    public void elabRecoveryPolicy(RecoveryDTO request) 
    		throws EAMException
    {
    	Long startTime = System.currentTimeMillis();
    	log.debug("call elabRecoveryPolicy({}) |{}|", request, startTime);
    	
    	try {

    	    X4DssPolicyRunner runner = x4DssPolicyRunnerQueryService.findRunnerById(request.getIdrunner());
    	    
    	    if (isNull(runner))
    	    {  
                log.error("Impossible to find runner by id {}", request.getIdrunner());
                return;
            }    
    	    
    	    X4Policy policyLoad = policyQueryService.getPolicy(runner.getX4PolicyId().toString());
    	    if (isNull(policyLoad) || (!POLICY_STATUS_ATTIVA.equals(policyLoad.getStatus())))
    	    {
    	        log.error("Policy {} has status {} ", runner.getX4PolicyId(), notNull(policyLoad) ? policyLoad.getStatus() : null);
    	        return;
    	    }
    	    
    	    
    		AX4Policy policy 	= policyQueryService.getPolicyHistory(policyLoad.getX4PolicyId().toString(), policyLoad.getVersion());
    		EventDTO eventDto 	= new EventDTO();
    		Long currentDate	= System.currentTimeMillis(); 
    		eventDto.setPolicy(policyLoad.getX4PolicyId().toString());
    		eventDto.setTipology(policyLoad.getTipoPolicy());
    		eventDto.setType(EventType.RECOVERY.name());
    		eventDto.setKafkaKey(policyLoad.getX4PolicyId() + SEP4 + currentDate); 
            eventDto.setAttributes(new HashMap<>());
            eventDto.getAttributes().put(EventAttributes.RUNNER.getValue(), runner.getX4DssPolicyRunnerId().toString());
            
    		if (isNull(policy))
    		{
    			eventDto.setEvent(EventActionType.ADD.name());
    		} else {
    			Boolean changeFrequenza = 	(isNull(policyLoad.getFrequenza()) && notNull(policy.getFrequenza())) ||
    										(notNull(policyLoad.getFrequenza()) && isNull(policy.getFrequenza())) ||
    										(notNull(policyLoad.getFrequenza()) && notNull(policy.getFrequenza()) && (!policyLoad.getFrequenza().equals(policy.getFrequenza())));
    
    			Boolean changeDeferrability = 	(isNull(policyLoad.getDifferibilita()) && notNull(policy.getDifferibilita())) ||
    											(notNull(policyLoad.getDifferibilita()) && isNull(policy.getDifferibilita())) ||
    											(notNull(policyLoad.getDifferibilita()) && notNull(policy.getDifferibilita()) && (!policyLoad.getDifferibilita().equals(policy.getDifferibilita())));
    
    			Boolean changeMaxAdvance = 	(isNull(policyLoad.getMaxAnticipo()) && notNull(policy.getMaxAnticipo())) ||
    										(notNull(policyLoad.getMaxAnticipo()) && isNull(policy.getMaxAnticipo())) ||
    										(notNull(policyLoad.getMaxAnticipo()) && notNull(policy.getMaxAnticipo()) && (!policyLoad.getMaxAnticipo().equals(policy.getMaxAnticipo())));
    
    			Boolean changeMaxSnooze	= 	(isNull(policyLoad.getMaxPosticipo()) && notNull(policy.getMaxPosticipo())) ||
    										(notNull(policyLoad.getMaxPosticipo()) && isNull(policy.getMaxPosticipo())) ||
    										(notNull(policyLoad.getMaxPosticipo()) && notNull(policy.getMaxPosticipo()) && (!policyLoad.getMaxPosticipo().equals(policy.getMaxPosticipo())));
    			
    			Boolean changeRifTemporale = (isNull(policyLoad.getX4RifTemporale()) && notNull(policy.getX4RifTemporale())) ||
    										 (notNull(policyLoad.getX4RifTemporale()) && isNull(policy.getX4RifTemporale())) ||
    										 (notNull(policyLoad.getX4RifTemporale()) && notNull(policy.getX4RifTemporale()) && (!policyLoad.getX4RifTemporale().equals(policy.getX4RifTemporale())));
    			
    			eventDto.setEvent(EventActionType.MODIFY.name());
    			eventDto.getAttributes().put(PolicyAttributes.FREQUENCY.name(), 	changeFrequenza.toString());
    			eventDto.getAttributes().put(PolicyAttributes.DEFERRABILITY.name(), changeDeferrability.toString());
    			eventDto.getAttributes().put(PolicyAttributes.MAX_ADVANCE.name(), 	changeMaxAdvance.toString());
    			eventDto.getAttributes().put(PolicyAttributes.MAX_SNOOZE.name(), 	changeMaxSnooze.toString());
    			eventDto.getAttributes().put(PolicyAttributes.RIF_TEMPORALE.name(), changeRifTemporale.toString());
    		}
    		
    		cacheService.evictCache(CACHE_POLICY, String.valueOf(policyLoad.getX4PolicyId()), CatalogTipologyType.POLICY);
    		BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
    		kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(eventDto));
    		
    		Long endTime = System.currentTimeMillis();
    		log.info("end elabRecoveryPolicy({}) |{}|", request, endTime - startTime);
    	} catch(Exception e) {
    		log.error(e.getMessage(), e);
    		throw new EAMException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e);
    	}
    }
}
