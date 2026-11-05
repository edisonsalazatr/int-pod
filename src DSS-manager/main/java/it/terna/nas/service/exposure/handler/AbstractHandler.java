package it.terna.nas.service.exposure.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import static it.terna.kernel.basement.utility.Assorted.*;
import static it.terna.nas.service.material.utility.Constants.END_POINT_HI_CALC_EVENTS_HANDLER;
import java.util.List;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.service.TransactionBlocks;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.Shoot;
import it.terna.nas.service.material.dto.hi.HiCalcDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AbstractHandler
{
    @Autowired
    protected BrokerEntityConfiguration brokerEntities;
    
    @Value("${disableHiCalc: false}")
    private Boolean                 disableHiCalc;   
    
    @Autowired
    protected KafkaProducerHandler kafkaHandler;
    
    @Autowired
    protected TransactionBlocks transactionBlocks;
    
    protected void sendTopicForHiCalcStaging(String assetNum, String siteId)
    {
        if (Boolean.TRUE.equals(disableHiCalc))
            return;

        long startTime = System.currentTimeMillis();
        log.debug("call saveHiCalcStaging({}, {}) |{}|", assetNum, siteId, startTime);
        
        if (isNotEmpty(assetNum) && isNotEmpty(siteId))
        {
            HiCalcDTO event = new HiCalcDTO();
            event.setAssetNum(assetNum);
            event.setSiteId(siteId);
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_HI_CALC_EVENTS_HANDLER);
            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
        } else {
            log.warn("saveHiCalcStaging ==> AssetNum is null or site is null. AssetNum: {}, SiteId: {}", assetNum, siteId);
        }
        
        long endTime = System.currentTimeMillis();
        log.info("end saveHiCalcStaging({}, {}) |{}|", assetNum, siteId, endTime - startTime);
    }
    
    
    protected void shootMessages(Shoot<WorkedPolicyRequestEvent> messages)
    {
        if (notNull(messages) && isNotEmpty(messages.getItems()))
        {
            log.info("call shootMessages, num messages: {}", messages.getItems().size());
            messages.getItems().forEach(x -> kafkaHandler.shoot(EOperation.UPDATE, messages.getEntity(), List.of(x)));
        }
    }
}
