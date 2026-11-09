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
import it.terna.nas.service.dal.entity.eam.X4EsitoIntSnow;
import it.terna.nas.service.dal.service.X4EsitoIntSnowQueryService;
import it.terna.nas.service.dal.service.repository.X4EsitoIntSnowRepository;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class X4EsitoIntSnowResubmitHandler
{
	@Autowired
	private BrokerEntityConfiguration brokerEntities;
	
	@Autowired
	private KafkaProducerHandler kafkaHandler;
	
	@Autowired
	private X4EsitoIntSnowQueryService x4EsitoIntSnowQueryService;
	
	@Autowired
	private X4EsitoIntSnowRepository x4EsitoIntSnowRepository;
	
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
			try (Stream<BigDecimal> regularItems = x4EsitoIntSnowRepository.getListServiceToRestart(resubmitConfig))
			{
				log.debug("Sending Regular X4EsitoIntSnow Requests |{}|.", System.currentTimeMillis());
				elaborateSnowKafkaMessages(regularItems, headers);
			}
			try (Stream<BigDecimal> sapIdItems = x4EsitoIntSnowRepository.getListSapIdServiceToRestart(resubmitSapIdConfig))
			{
				log.debug("Sending SapId X4EsitoIntSnow Requests |{}|.", System.currentTimeMillis());
				elaborateSnowKafkaMessages(sapIdItems, headers);
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	@Transactional(readOnly = false)
	private void elaborateSnowKafkaMessages(Stream<BigDecimal> items, Map<String, String> headers) 
			throws DataLayerException
	{
		Iterator<BigDecimal> iterator = items.iterator();
	    while (iterator.hasNext()) 
	    {
	    	headers.clear();
			X4EsitoIntSnow row = x4EsitoIntSnowQueryService.getListServiceToRestartContentAndUpdate(iterator.next().longValue());
			String kafkaTopic 	= row.getKafkaTopic();
			String message 		= row.getMessage();
			
			if (Assorted.isNotEmpty(kafkaTopic) && Assorted.isNotEmpty(message))
			{
				headers.put(Constants.UUID_RESEND_PARAM, row.getUniqueId());
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
