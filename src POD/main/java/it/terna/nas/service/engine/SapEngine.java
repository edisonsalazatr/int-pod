package it.terna.nas.service.engine;

import it.terna.nas.service.exposure.handler.SapHandler;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.util.Pair;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

import static it.terna.nas.service.material.utility.Constants.PARZIALE;
import static it.terna.nas.service.material.utility.Constants.TOTALE;

@Slf4j
@Configuration
public class SapEngine {

    @Autowired
    private SapHandler sapHandler;

    @KafkaListener(autoStartup ="${broker.endPoints.acquisitionOrdIntFromSAP.entityEnabledTopic}", topics = "${broker.endPoints.acquisitionOrdIntFromSAP.entityName}", groupId = "${kafka.consumer.groupIdAcquisitionOrdIntFromSAP}", containerFactory = "kafkaListenerContainerFactory")
    public void acquisitionOrdInt(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
    {
        String uuidResend = null;
        String oiConsumer = "ACQUISITION OI";
        try {
            uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE ACQUISITION OI - ", headers, payload, uuidResend, oiConsumer, Level.DEBUG);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE ACQUISITION OI - ", headers, null, uuidResend, oiConsumer, Level.INFO);
            sapHandler.acquisitionOrdInt(meta.topic(), uuidResend, payload);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE ACQUISITION OI - ", headers, e.getMessage(), uuidResend, oiConsumer, Level.ERROR);
        }
        //Dopo Esecuzione
        logKafkaMessage("##############CONSUMED KAFKA MESSAGE ACQUISITION OI - ", headers, payload, uuidResend, oiConsumer, Level.DEBUG);
    }

    @KafkaListener(autoStartup ="${broker.endPoints.acquisitionOdMFromSAP.entityEnabledTopic}", topics = "${broker.endPoints.acquisitionOdMFromSAP.entityName}", groupId = "${kafka.consumer.groupIdAcquisitionOdMFromSAP}", containerFactory = "kafkaListenerContainerFactory")
    public void acquisitionOdM(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
    {
        String uuidResend = null;
        String odmConsumer = "ACQUISITION ODM";
        try {
            uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE ACQUISITION ODM - ", headers, payload, uuidResend, odmConsumer, Level.DEBUG);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE ACQUISITION ODM - ", headers, null, uuidResend, odmConsumer, Level.INFO);
            sapHandler.acquisitionOdM(meta.topic(), uuidResend, payload);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE ACQUISITION ODM - ", headers, e.getMessage(), uuidResend, odmConsumer, Level.ERROR);
        }
        //Dopo Esecuzione
        logKafkaMessage("##############CONSUMED KAFKA MESSAGE ACQUISITION ODM - ", headers, payload, uuidResend, odmConsumer, Level.DEBUG);
    }


    @KafkaListener(autoStartup ="${broker.endPoints.stornoTotaleSAP.entityEnabledTopic}", topics = "${broker.endPoints.stornoTotaleSAP.entityName}", groupId = "${kafka.consumer.groupIdStornoTotaleSAP}", containerFactory = "kafkaListenerContainerFactory")
    public void stornoTotale(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
    {
        String uuidResend = null;
        String stornoTotConsumer = "STORNO TOTALE";
        try {
            uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE STORNO TOTALE - ", headers, payload, uuidResend, stornoTotConsumer, Level.DEBUG);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE STORNO TOTALE - ", headers, null, uuidResend, stornoTotConsumer, Level.INFO);
            sapHandler.storno(meta.topic(), uuidResend, payload, TOTALE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE STORNO TOTALE - ", headers, e.getMessage(), uuidResend, stornoTotConsumer, Level.ERROR);
        }
        //Dopo Esecuzione
        logKafkaMessage("##############CONSUMED KAFKA MESSAGE STORNO TOTALE - ", headers, payload, uuidResend, stornoTotConsumer, Level.DEBUG);
    }

    @KafkaListener(autoStartup ="${broker.endPoints.stornoParzialeSAP.entityEnabledTopic}", topics = "${broker.endPoints.stornoParzialeSAP.entityName}", groupId = "${kafka.consumer.groupIdStornoParzialeSAP}", containerFactory = "kafkaListenerContainerFactory")
    public void stornoParziale(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
    {
        String uuidResend = null;
        String stornoParzConsumer = "STORNO PARZIALE";
        try {
            uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE STORNO PARZIALE - ", headers, payload, uuidResend, stornoParzConsumer, Level.DEBUG);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE STORNO PARZIALE - ", headers, null, uuidResend, stornoParzConsumer, Level.INFO);
            sapHandler.storno(meta.topic(), uuidResend, payload, PARZIALE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE STORNO PARZIALE - ", headers, e.getMessage(), uuidResend, stornoParzConsumer, Level.ERROR);
        }
        //Dopo Esecuzione
        logKafkaMessage("##############CONSUMED KAFKA MESSAGE STORNO PARZIALE - ", headers, payload, uuidResend, stornoParzConsumer, Level.DEBUG);
    }

    @KafkaListener(autoStartup ="${broker.endPoints.acquisitionCountableDataFromSAP.entityEnabledTopic}", topics = "${broker.endPoints.acquisitionCountableDataFromSAP.entityName}", groupId = "${kafka.consumer.groupIdAcquisitionCountableDataFromSAP}", containerFactory = "kafkaListenerContainerFactory")
    public void acquisitionCountableData(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
    {
        String uuidResend = null;
        String wbeConsumer = "ACQUISITION WBE";
        try {
            uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE ACQUISITION WBE - ", headers, payload, uuidResend, wbeConsumer, Level.DEBUG);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE ACQUISITION WBE - ", headers, null, uuidResend, wbeConsumer, Level.INFO);
            sapHandler.acquisitionCountableData(meta.topic(), uuidResend, payload);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE ACQUISITION WBE - ", headers, e.getMessage(), uuidResend, wbeConsumer, Level.ERROR);
        }
        //Dopo Esecuzione
        logKafkaMessage("##############CONSUMED KAFKA MESSAGE ACQUISITION WBE - ", headers, payload, uuidResend, wbeConsumer, Level.DEBUG);
    }


    @KafkaListener(autoStartup ="${broker.endPoints.laborSAP.entityEnabledTopic}", topics = "${broker.endPoints.laborSAP.entityName}", groupId = "${kafka.consumer.groupIdLaborSAP}", containerFactory = "kafkaListenerContainerFactory")
    public void invioConsuntiviExt(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
    {
        String uuidResend = null;
        String laborExtConsumer = "INVIO CONSUNTIVI EXT";
        try {
            uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE INVIO CONSUNTIVI EXT - ", headers, payload, uuidResend, laborExtConsumer, Level.DEBUG);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE INVIO CONSUNTIVI EXT - ", headers, null, uuidResend, laborExtConsumer, Level.INFO);
            sapHandler.invioConsuntiviToExt(meta.topic(), uuidResend, payload);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE INVIO CONSUNTIVI EXT - ", headers, e.getMessage(), uuidResend, laborExtConsumer, Level.ERROR);
        }
        //Dopo Esecuzione
        logKafkaMessage("##############CONSUMED KAFKA MESSAGE INVIO CONSUNTIVI EXT - ", headers, payload, uuidResend, laborExtConsumer, Level.DEBUG);
    }

    @KafkaListener(autoStartup ="${broker.endPoints.laborSAPExt.entityEnabledTopic}", topics = "${broker.endPoints.laborSAPExt.entityName}", groupId = "${kafka.consumer.groupIdLaborSAPExt}", containerFactory = "kafkaListenerContainerFactory", concurrency = "${broker.endPoints.laborSAPExt.entityConcurrency}")
    public void invioConsuntivi(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
    {
        String uuidResend = null;
        String laborConsumer = "INVIO CONSUNTIVI";
        try {
            uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE INVIO CONSUNTIVI - ", headers, payload, uuidResend, laborConsumer, Level.DEBUG);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE INVIO CONSUNTIVI - ", headers, null, uuidResend, laborConsumer, Level.INFO);
            sapHandler.invioConsuntivi(meta.topic(), uuidResend, payload);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE INVIO CONSUNTIVI - ", headers, e.getMessage(), uuidResend, laborConsumer, Level.ERROR);
        }
        //Dopo Esecuzione
        logKafkaMessage("##############CONSUMED KAFKA MESSAGE INVIO CONSUNTIVI - ", headers, payload, uuidResend, laborConsumer, Level.DEBUG);
    }

    @KafkaListener(autoStartup ="${broker.endPoints.acquisitionNtkFromSAP.entityEnabledTopic}", topics = "${broker.endPoints.acquisitionNtkFromSAP.entityName}", groupId = "${kafka.consumer.groupIdAcquisitionNtk}", containerFactory = "kafkaListenerContainerFactory")
    public void acquisitionNtk(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
    {
        String uuidResend = null;
        String ntkConsumer = "ACQUISITION NTK";
        try {
            uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE ACQUISITION NTK - ", headers, payload, uuidResend, ntkConsumer, Level.DEBUG);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE ACQUISITION NTK - ", headers, null, uuidResend, ntkConsumer, Level.INFO);
            sapHandler.acquisitionNtk(meta.topic(), uuidResend, payload);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE ACQUISITION NTK - ", headers, e.getMessage(), uuidResend, ntkConsumer, Level.ERROR);
        }
        //Dopo Esecuzione
        logKafkaMessage("##############CONSUMED KAFKA MESSAGE ACQUISITION NTK - ", headers, payload, uuidResend, ntkConsumer, Level.DEBUG);
    }

    @KafkaListener(autoStartup ="${broker.endPoints.closureOdMSAP.entityEnabledTopic}", topics = "${broker.endPoints.closureOdMSAP.entityName}", groupId = "${kafka.consumer.groupIdClosureOdMSAP}", containerFactory = "kafkaListenerContainerFactory")
    public void chiusuraOdM(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
    {
        String uuidResend = null;
        String closeOdmConsumer = "CLOSURE ODM";
        try {
            uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE CLOSURE ODM - ", headers, payload, uuidResend, closeOdmConsumer, Level.DEBUG);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE CLOSURE ODM - ", headers, null, uuidResend, closeOdmConsumer, Level.INFO);
            sapHandler.closureOdM(meta.topic(), uuidResend, payload);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE CLOSURE ODM - ", headers, e.getMessage(), uuidResend, closeOdmConsumer, Level.ERROR);
        }
        //Dopo Esecuzione
        logKafkaMessage("##############CONSUMED KAFKA MESSAGE CLOSURE ODM - ", headers, payload, uuidResend, closeOdmConsumer, Level.DEBUG);
    }

    @KafkaListener(autoStartup ="${broker.endPoints.closureNtkSAP.entityEnabledTopic}", topics = "${broker.endPoints.closureNtkSAP.entityName}", groupId = "${kafka.consumer.groupIdClosureNtkSAP}", containerFactory = "kafkaListenerContainerFactory")
    public void chiusuraNtk(@Headers MessageHeaders headers, ConsumerRecordMetadata meta, @Payload String payload)
    {
        String uuidResend = null;
        String closeNtkConsumer = "CLOSURE NTK";
        try {
            uuidResend = KafkaUtility.getParamByteToString(headers, Constants.UUID_RESEND_PARAM);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE CLOSURE NTK - ", headers, payload, uuidResend, closeNtkConsumer, Level.DEBUG);
            logKafkaMessage("###############TRY TO CONSUME KAFKA MESSAGE CLOSURE NTK - ", headers, null, uuidResend, closeNtkConsumer, Level.INFO);
            sapHandler.closureNtk(meta.topic(), uuidResend, payload);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            logKafkaMessage("##############FAILED TO CONSUME KAFKA MESSAGE - ", headers, e.getMessage(), uuidResend, closeNtkConsumer, Level.ERROR);
        }
        //Dopo Esecuzione
        logKafkaMessage("##############CONSUMED KAFKA MESSAGE - ", headers, payload, uuidResend, closeNtkConsumer, Level.DEBUG);
    }

    private static final String TEMPLATE_MESSAGE = "{} - Transaction '{}' - Consumer '{}' received a Kafka message from Topic='{}' with Offset='{}' on Partition='{}' identified by KAFKA_KEY='{}' at time='{}' with TimeStampType='{}' and body:{}";

    private void logKafkaMessage(String baseLogString, MessageHeaders headers, String payload, String uuidResend, String consumer, Level logLevel)
    {
        Object[] logArguments = {baseLogString,
                uuidResend,
                consumer,
                headers.get(KafkaHeaders.RECEIVED_TOPIC),
                headers.get(KafkaHeaders.OFFSET),
                headers.get(KafkaHeaders.RECEIVED_PARTITION_ID),
                headers.get(KafkaHeaders.RECEIVED_MESSAGE_KEY),
                headers.get(KafkaHeaders.RECEIVED_TIMESTAMP),
                headers.get(KafkaHeaders.TIMESTAMP_TYPE),
                payload};


        Pair<String, Object[]> pair = Pair.of(TEMPLATE_MESSAGE, logArguments);

        switch(logLevel) {
            case WARN  -> log.warn(pair.getFirst(), pair.getSecond());
            case INFO  -> log.info(pair.getFirst(), pair.getSecond());
            case DEBUG -> log.debug(pair.getFirst(), pair.getSecond());
            case ERROR -> log.error(pair.getFirst(), pair.getSecond());
            case TRACE -> log.trace(pair.getFirst(), pair.getSecond());
        }
    }

}
