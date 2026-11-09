package it.terna.nas.service.exposure.handler;

import java.util.List;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.X4SnowRilancio;
import it.terna.nas.service.dal.service.SnowAssetQueryService;
import it.terna.nas.service.material.dto.AssetDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class X4SnowRilancioHandler
{
	private final SnowAssetQueryService snowAssetQueryService;

	private final BrokerEntityConfiguration brokerEntities;

	private final KafkaProducerHandler kafkaHandler;

	@Value("${broker.endPoints.sendAsset.entityName}")
	private String sendAssetTopic;

	@Async
	public void recuperoSnow()
	{
	    MDC.put(Common.MDC_TRANSACTION_ID, UUID.randomUUID().toString());
	    
		try
		{
			BrokerEntityInfo brokerEntityInfo = brokerEntities.getKafkaTopics().get(sendAssetTopic);
			if (!Assorted.notNull(brokerEntityInfo))
			{
				log.error("Topic not defined {}", sendAssetTopic);
				return;
			}

			// Stream-based processing: records are consumed one at a time and never
			// accumulated in memory, so the method is safe even for very large result sets.
			long processed = snowAssetQueryService.recuperoAssetDaElaborare(item -> sendToKafka(item, brokerEntityInfo));
			log.info("recuperoSnow completed, [{}] messages sent to topic '{}'", processed, sendAssetTopic);
		}
		catch (Exception e)
		{
			log.error("Generic Error: " + e.getMessage(), e);
		} finally {
            MDC.clear();
        }
	}

	
	private void sendToKafka(X4SnowRilancio item, BrokerEntityInfo brokerEntityInfo)
	{
		try
		{
			AssetDTO asset = new AssetDTO();
			asset.setAssetnum(item.getAssetnum());
			asset.setClassstructureid(item.getClassStructureId());
			kafkaHandler.shoot(EOperation.UPDATE, brokerEntityInfo, List.of(asset));
			// message sent: flag the record as processed (DA_ELABORARE = 'N')
			snowAssetQueryService.markAsElaborato(item.getId());
		} catch (Exception e) {
			log.error("Error sending Kafka message for X4SnowRilancio with assetnum '{}': {}", item.getAssetnum(), e.getMessage(), e);
		}
	}
}
