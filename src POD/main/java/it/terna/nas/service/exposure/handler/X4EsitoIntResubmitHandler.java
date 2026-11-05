package it.terna.nas.service.exposure.handler;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.X4EsitoInt;
import it.terna.nas.service.dal.service.X4EsitoIntQueryService;
import it.terna.nas.service.dal.service.repository.X4EsitoIntRepository;
import it.terna.nas.service.material.utility.Catalog.GeoCallInterface;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class X4EsitoIntResubmitHandler
{
	@Autowired
	private BrokerEntityConfiguration brokerEntities;
	
	@Autowired
	private KafkaProducerHandler kafkaHandler;
	
	@Autowired
	private X4EsitoIntQueryService x4EsitoIntQueryService;
	
	@Autowired
	private X4EsitoIntRepository x4EsitoIntRepository;
	
	@Value("${resubmit:0}")
	private int resubmitConfig;
	
	@Value("${resubmitSapId:0}")
	private int resubmitSapIdConfig;
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	@Transactional(readOnly = false)
	public void elaborate()
	{
		try {
			Map<String, String> headers = new HashMap<>();
			try (Stream<BigDecimal> regularItems = x4EsitoIntRepository.getListServiceToRestart(resubmitConfig))
			{
				log.debug("Sending Regular X4EsitoInt Requests |{}|.", System.currentTimeMillis());
				elaborateKafkaMessages(regularItems, headers);
			}
			try (Stream<BigDecimal> sapIdItems = x4EsitoIntRepository.getListSapIdServiceToRestart(resubmitSapIdConfig))
			{
				log.debug("Sending SapId X4EsitoInt Requests |{}|.", System.currentTimeMillis());
				elaborateKafkaMessages(sapIdItems, headers);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	@Transactional(readOnly = false)
	private void elaborateKafkaMessages(Stream<BigDecimal> items, Map<String, String> headers) 
			throws DataLayerException
	{
		Iterator<BigDecimal> iterator = items.iterator();
	    while (iterator.hasNext()) 
	    {
	    	headers.clear();
	    	X4EsitoInt row = x4EsitoIntQueryService.getListServiceToRestartContentAndUpdate(iterator.next().longValue());
			String kafkaTopic 	= row.getKafkaTopic();
			String message 		= row.getMessage();
			Long retry 			= row.getRetry();
			
			if (Assorted.isNotEmpty(kafkaTopic) && Assorted.isNotEmpty(message) && Assorted.notNull(retry))
			{
				headers.put(Constants.UUID_RESEND_PARAM, row.getUniqueId());
				
				if(row.getIdInterface().equalsIgnoreCase(GeoCallInterface.SEND_ODL_INTERFACE.getName())) 
					headers.put(Constants.OPERATION_PARAM, Constants.INVIO_ODL);
				
				if(row.getIdInterface().equalsIgnoreCase(GeoCallInterface.SEND_CANCEL_BLOCK_INTERFACE.getName()))
					headers.put(Constants.OPERATION_PARAM, Constants.ANNULLA_BLOCCO_ODL);
				
				BrokerEntityInfo brokerEntityInfo = brokerEntities.getKafkaTopics().get(kafkaTopic);
				
				if (!Assorted.notNull(brokerEntityInfo))
					log.error("Topic not defined {}", kafkaTopic);
				
				if (Assorted.notNull(brokerEntityInfo))
				{	
					kafkaHandler.shoot(EOperation.UPDATE, 
										brokerEntityInfo,
										headers, message, null);
				}
			}
	    }
	}
}
