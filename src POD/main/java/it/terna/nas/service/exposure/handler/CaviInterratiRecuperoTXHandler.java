package it.terna.nas.service.exposure.handler;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.X4RecuperoCavi;
import it.terna.nas.service.dal.service.repository.X4RecuperoCaviRepository;
import it.terna.nas.service.exposure.events.WorkOrderCaviInterrati;
import it.terna.nas.service.material.dto.cavi.CaviRecoveryResult;
import it.terna.nas.service.material.dto.cavi.WorkOrderRecuperoCavi;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CaviInterratiRecuperoTXHandler {
	
	@Autowired
	private BrokerEntityConfiguration brokerEntities;
	
	@Autowired
	private KafkaProducerHandler kafkaHandler;
	
	@Autowired
	private X4RecuperoCaviRepository repository;

	@Value("${uriMaximo}")
	private String uriMaximo;
	
	private static final String X4WO_INVIO_CAVI_BASE_URL = "https://%s/maximo/oslc/os/x4wo_invio_cavi/_%s";
	
	
	public static final String TOPIC_INVIO_CAVI = "eamCaviInterratiInvioIspezioni";

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void processWonum(String wonum, CaviRecoveryResult result) {
		log.debug(" - Process wonum {}", wonum);
		X4RecuperoCavi record = repository.findById(wonum)
	            .orElseThrow(() -> new IllegalArgumentException(
	                "No record found by wonum=" + wonum));

	        try {
	            log.debug(" - Processing wonum='{}'", wonum);

	            WorkOrderCaviInterrati request = createRequestMessage(wonum);
	            String message = toJson(request);
	            record.setMessage(message);
	            BrokerEntityInfo brokerEntityInfo = getTopicSafe(TOPIC_INVIO_CAVI);
	    
	            Map<String, String> headers = new HashMap<>();
	            headers.put("UUID_RECOVERY", UUID.randomUUID().toString());
	            kafkaHandler.shoot(EOperation.INSERT, 
						brokerEntityInfo, headers, message, null);
	            
	            record.setDaProcessare(0);
	            record.setResult("OK");

	        } finally {
	            log.debug(" - Updating staging table");
	            repository.save(record);
	            result.setCount(result.getCount() + 1);
	        }
	}
	
	private BrokerEntityInfo getTopicSafe(String topicKey) {
		BrokerEntityInfo brokerEntityInfo = brokerEntities.getEndPoints().get(topicKey);
		if (brokerEntityInfo == null) {
			log.error("Topic not defined {}", topicKey);
			throw new IllegalArgumentException("No topic defined for " + topicKey);
		}
		return brokerEntityInfo;
	}
	
	private WorkOrderCaviInterrati createRequestMessage(String wonum) {
		
		
		WorkOrderRecuperoCavi wo = WorkOrderRecuperoCavi.from(repository.loadWorkOrder(wonum));
		
		WorkOrderCaviInterrati request = new WorkOrderCaviInterrati();
		request.setAssetnum(wo.assetnum());
		request.setJpnum(wo.jpnum());
		request.setParent(wo.parent());
		request.setSiteid(wo.siteid());
		request.setStatus(wo.status());
		
		request.setChangedate(wo.changedate());
		request.setTargstartdate(wo.x4datamodello()); // attenzione!
		request.setWonum(wo.wonum());
		request.setX3gewrk(wo.x3gewrk());
		request.setX3ingrp(wo.x3ingrp());
		request.setX4sapidasset(wo.x4sapidasset());
		request.setX4tronconum(wo.x4tronconum());
		request.setHref(buildUriResource(wonum, wo.siteid()));
		
		List<String> children = repository.loadWorkorderChildren(wonum);
		if(Assorted.isNotEmpty(children)) {
			List<WorkOrderCaviInterrati> woChildren = new ArrayList<>();
			for(String child : children) {
				WorkOrderCaviInterrati woChild = new WorkOrderCaviInterrati();
				woChild.setHref(buildUriResource(child, wo.siteid()));
				woChildren.add(woChild);
			}
			request.setWorkorder(woChildren);
		}
		
		
		return request;
	}
	
	private String buildUriResource(String wonum, String siteid) {
		log.debug(" - building href url");
		String id = siteid + "/" + wonum;
		String encodedId = Base64.getEncoder().encodeToString(id.getBytes());
		String href = String.format(X4WO_INVIO_CAVI_BASE_URL, uriMaximo, encodedId);
		log.debug(" - Created href=[{}]", href);
		return href;
	}
	
	
	private String toJson(WorkOrderCaviInterrati request) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.getFactory().setCharacterEscapes(null);
		mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		String json = null;
		try {
			json = mapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			log.error(" - Error serializing request to json", e);
		}
		return json;
	}
}
