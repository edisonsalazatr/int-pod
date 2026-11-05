package it.terna.nas.service.engine;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.slf4j.MDC;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.exposure.handler.AgileHandler;
import it.terna.nas.service.material.dto.KafkaOdlToAgileRequest;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class AgileEngine {

    private static final String TOPIC_INVIO_ODL_AGILE_MULTI = "eamAgileInvioIndisponibilitaMulti";

    @Autowired
    private BrokerEntityConfiguration brokerEntities;

    @Autowired
    private KafkaProducerHandler kafkaHandler;

    @Value("${eamOdlDssSkip}")
    private boolean eamOdlDssSkip;

    @Autowired
    private AgileHandler agileHandler;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final String TEMPLATE_MESSAGE = "{} - Transaction '{}' - Consumer '{}' received a Kafka message from Topic='{}' with Offset='{}' on Partition='{}' identified by KAFKA_KEY='{}' ReceivedTopic='{}' at time='{}' with TimeStampType='{}' and body:{} ";

    @KafkaListener(topics = "${broker.endPoints.eamAgileInvioIndisponibilita.entityName}", groupId = "${kafka.consumer.groupIdAgileInvioIndisponibilita}", containerFactory = "kafkaListenerContainerFactory", autoStartup = "${broker.endPoints.eamAgileInvioIndisponibilita.entityEnabledTopic}")
    public void kafkaAgileOdlReceiver(@Headers MessageHeaders headers, @Payload String payload) {

        String topic = headers.get(KafkaHeaders.RECEIVED_TOPIC).toString();

        Entry<String, BrokerEntityInfo> sourceEntityEntry = brokerEntities.getEndPoints()
                .entrySet()
                .stream()
                .filter(entry -> Assorted.equals(topic, entry.getValue().getEntityName()))
                .findFirst()
                .orElse(null);

        if (Assorted.notNull(sourceEntityEntry)) {

            String transactionId = UUID.randomUUID().toString();
            MDC.put(Common.MDC_TRANSACTION_ID, transactionId);

            String uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);

            try {

                logKafkaMessage("###############TRY TO DISPATCH KAFKA MESSAGE SEND ODL TO AGILE MULTI - ", uuidResend,
                        headers, payload, Level.DEBUG);
                logKafkaMessage("###############TRY TO DISPATCH KAFKA MESSAGE SEND ODL TO AGILE MULTI - ", uuidResend,
                        headers, null, Level.INFO);

                KafkaOdlToAgileRequest request = objectMapper.readValue(payload, KafkaOdlToAgileRequest.class);

                String assetnum = request.getAssetnum();

                if (assetnum == null || assetnum.isBlank()) {
                    log.warn("Assetnum null/blank for wonum [{}]. Message will be sent to multi topic with null key",
                            request.getWonum());
                    assetnum = null;
                }

                BrokerEntityInfo brokerEntityInfo = getTopicSafe(TOPIC_INVIO_ODL_AGILE_MULTI);

                Map<String, String> attributes = new HashMap<>();
                attributes.put("payload", payload);

                if (uuidResend != null && !uuidResend.isBlank()) {
                    attributes.put(Constants.UUID_RESEND_PARAM, uuidResend);
                }

                kafkaHandler.shoot(
                        EOperation.CREATE,
                        brokerEntityInfo,
                        List.of(request));

                logKafkaMessage("##############DISPATCHED KAFKA MESSAGE SEND ODL TO AGILE MULTI - ", uuidResend,
                        headers, payload, Level.DEBUG);

            } catch (Exception e) {

                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }

                logKafkaMessage("##############FAILED TO DISPATCH KAFKA MESSAGE SEND ODL TO AGILE MULTI - ", uuidResend,
                        headers, e.getMessage(), Level.ERROR);

                throw new RuntimeException(e);

            } finally {
                MDC.clear();
            }

        } else {
            log.warn("Agile invio richiesta indisponibilita dispatcher: Message from topic [{}] is not to be processed",
                    topic);
        }
    }

    @KafkaListener(topics = "${broker.endPoints.eamAgileInvioIndisponibilitaMulti.entityName}", groupId = "${kafka.consumer.groupIdAgileInvioIndisponibilitaMulti}", containerFactory = "kafkaListenerContainerFactory", autoStartup = "${broker.endPoints.eamAgileInvioIndisponibilitaMulti.entityEnabledTopic}", concurrency = "${broker.endPoints.eamAgileInvioIndisponibilitaMulti.entityConcurrency}")
    public void kafkaAgileOdlMultiReceiver(@Headers MessageHeaders headers, @Payload String payload) {

        // retrieve topic name from supplied message headers
        String topic = headers.get(KafkaHeaders.RECEIVED_TOPIC).toString();

        // trying to get entity mapped by consumed topic
        Entry<String, BrokerEntityInfo> sourceEntityEntry = brokerEntities.getEndPoints().entrySet().stream()
                .filter(entry -> Assorted.equals(topic, entry.getValue().getEntityName())).findFirst().orElse(null);

        // check for a broker entity to manage
        if (Assorted.notNull(sourceEntityEntry)) {

            String transactionId = UUID.randomUUID().toString();
            MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
            String uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);

            try {

                try {
                    // log message received
                    logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE SEND ODL TO AGILE - ", uuidResend,
                            headers, payload, Level.DEBUG);
                    logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE SEND ODL TO AGILE - ", uuidResend,
                            headers, null, Level.INFO);
                    KafkaOdlToAgileRequest request = objectMapper.readValue(payload, KafkaOdlToAgileRequest.class);

                    // Check per wonum generato da DSS
                    if (eamOdlDssSkip) {
                        log.info("eamOdlDssSkip=true, skip elaborazione requestUnavailability");
                    } else {
                        agileHandler.requestUnavailability(topic, request, uuidResend);
                    }

                } catch (Exception e) {
                    logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE SEND ODL TO AGILE - ", uuidResend,
                            headers, e.getMessage(), Level.ERROR);
                }
                logKafkaMessage("##############CONSUMED KAFKA MESSAGE SEND ODL TO AGILE - ", uuidResend, headers,
                        payload, Level.DEBUG);

            } finally {

                MDC.clear();
            }
        } else {
            // log message discarded because belonging to topic not managed here
            log.warn("Agile invio richiesta indisponibilita: Message from topic [{}] is not to be processed", topic);
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

    private void logKafkaMessage(String baseLogString, String uuidResend, MessageHeaders headers, String payload,
            Level logLevel) {

        Object[] logArguments = {
                baseLogString,
                uuidResend,
                "REQUEST_UNAVAILABILITY_DISPATCHER",
                headers.get(KafkaHeaders.RECEIVED_TOPIC),
                headers.get(KafkaHeaders.OFFSET),
                headers.get(KafkaHeaders.RECEIVED_PARTITION_ID),
                headers.get(KafkaHeaders.RECEIVED_MESSAGE_KEY),
                headers.get(KafkaHeaders.RECEIVED_TOPIC),
                headers.get(KafkaHeaders.RECEIVED_TIMESTAMP),
                headers.get(KafkaHeaders.TIMESTAMP_TYPE),
                payload
        };

        Pair<String, Object[]> pair = Pair.of(TEMPLATE_MESSAGE, logArguments);

        switch (logLevel) {
            case WARN -> log.warn(pair.getFirst(), pair.getSecond());
            case INFO -> log.info(pair.getFirst(), pair.getSecond());
            case DEBUG -> log.debug(pair.getFirst(), pair.getSecond());
            case ERROR -> log.error(pair.getFirst(), pair.getSecond());
            case TRACE -> log.trace(pair.getFirst(), pair.getSecond());
        }
    }
}