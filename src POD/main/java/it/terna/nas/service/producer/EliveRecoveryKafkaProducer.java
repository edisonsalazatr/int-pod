package it.terna.nas.service.producer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.material.dto.KafkaOdlToEliveRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EliveRecoveryKafkaProducer {

    public static final String TOPIC_INVIO_ODL_ELIVE = "sendOdLToElive";

    @Autowired
    private BrokerEntityConfiguration brokerEntities;

    @Autowired
    private KafkaProducerHandler kafkaHandler;

    public void sendOdlToElive(KafkaOdlToEliveRequest request) {
        if (request == null || request.getWonum() == null) {
            throw new IllegalArgumentException("KafkaOdlToEliveRequest or WONUM is null");
        }

        try {
            BrokerEntityInfo brokerEntityInfo = getTopicSafe(TOPIC_INVIO_ODL_ELIVE);

            String message = toJson(request);

            if (message == null) {
                throw new IllegalStateException(
                        "Unable to serialize KafkaOdlToEliveRequest for WONUM " + request.getWonum()
                );
            }

            Map<String, String> headers = new HashMap<>();
            headers.put("UUID_RECOVERY", UUID.randomUUID().toString());

            kafkaHandler.shoot(
                    EOperation.INSERT,
                    brokerEntityInfo,
                    headers,
                    message,
                    null
            );

            log.info("Recovery ELIVE Kafka message sent - topicKey |{}| - wonum |{}|",
                    TOPIC_INVIO_ODL_ELIVE,
                    request.getWonum());

        } catch (Exception e) {
            throw new RuntimeException(
                    "Error sending recovery ELIVE Kafka message for WONUM " + request.getWonum(),
                    e
            );
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

    private String toJson(KafkaOdlToEliveRequest request) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.getFactory().setCharacterEscapes(null);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        try {
            return mapper.writeValueAsString(request);
        } catch (JsonProcessingException e) {
            log.error("Error serializing KafkaOdlToEliveRequest to json - wonum |{}|",
                    request != null ? request.getWonum() : null,
                    e);
            return null;
        }
    }
}