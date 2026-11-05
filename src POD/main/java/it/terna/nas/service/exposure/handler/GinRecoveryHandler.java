package it.terna.nas.service.exposure.handler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.service.GinRecoveryService;
import it.terna.nas.service.dal.service.GinRecoveryUpdateService;
import it.terna.nas.service.dal.service.repository.X4GinRecoveryRepository;
import it.terna.nas.service.material.dto.KafkaOdlToAgileRequest;
import it.terna.nas.service.producer.GinRecoveryKafkaProducer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GinRecoveryHandler {

    private static final int COMMIT_CHUNK_SIZE = 100;

    @Autowired
    private X4GinRecoveryRepository repository;

    @Autowired
    private GinRecoveryService service;

    @Autowired
    private GinRecoveryKafkaProducer kafkaProducer;

    @Autowired
    private GinRecoveryUpdateService updateService;

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(readOnly = true)
    public void odlRecovery(String batchCode) {
        log.info("ODL Kafka recovery START - batchCode |{}|", batchCode);

        List<String> processedWonums = new ArrayList<>();

        int totalRead = 0;
        int totalSent = 0;
        int totalKo = 0;

        try (Stream<String> wonums = repository.getListWonumToRecover()) {

            log.info("ODL Kafka recovery stream opened - batchCode |{}|", batchCode);

            Iterator<String> iterator = wonums.iterator();

            while (iterator.hasNext()) {
                String wonum = iterator.next();

                totalRead++;

                boolean success = processWonum(batchCode, wonum);

                if (success) {
                    processedWonums.add(wonum);
                    totalSent++;
                } else {
                    totalKo++;
                }

                if (processedWonums.size() >= COMMIT_CHUNK_SIZE) {
                    flushProcessedWonums(processedWonums);
                }

                if (totalRead % 100 == 0) {
                    log.info("ODL Kafka recovery progress - batchCode |{}| - read |{}| - sent |{}| - ko |{}|",
                            batchCode,
                            totalRead,
                            totalSent,
                            totalKo);
                }
            }

            flushProcessedWonums(processedWonums);

            log.info("ODL Kafka recovery END - batchCode |{}| - read |{}| - sent |{}| - ko |{}|",
                    batchCode,
                    totalRead,
                    totalSent,
                    totalKo);

        } catch (Exception e) {
            log.error("ODL Kafka recovery fatal error - batchCode |{}| - read |{}| - sent |{}| - ko |{}|",
                    batchCode,
                    totalRead,
                    totalSent,
                    totalKo,
                    e);

            throw new RuntimeException("ODL Kafka recovery failed for batchCode " + batchCode, e);
        }
    }

    private void flushProcessedWonums(List<String> processedWonums) {
        if (processedWonums.isEmpty()) {
            return;
        }

        log.info("Updating recovery staging -  size |{}|",
                processedWonums.size());

        updateService.markAsProcessedBatch(new ArrayList<>(processedWonums));

        processedWonums.clear();
    }

    private boolean processWonum(String batchCode, String wonum) {
        String step = "START";

        try {
            log.debug("Processing recovery WONUM - batchCode |{}| - wonum |{}|",
                    batchCode,
                    wonum);

            step = "LOAD_WORKORDER";
            Tuple workOrderTuple = repository.loadWorkOrder(wonum);

            if (workOrderTuple == null) {
                log.warn("No WORKORDER found - batchCode |{}| - wonum |{}|",
                        batchCode,
                        wonum);
                return false;
            }

            step = "BUILD_KAFKA_INPUT";
            KafkaOdlToAgileRequest inputObject = service.buildKafkaOdlToAgileRequest(workOrderTuple);

            if (inputObject == null) {
                log.warn("Unable to build KafkaOdlToAgileRequest - batchCode |{}| - wonum |{}|",
                        batchCode,
                        wonum);
                return false;
            }

            step = "SEND_KAFKA";
            kafkaProducer.sendOdlToAgile(inputObject);

            return true;

        } catch (Exception e) {
            log.error("Error processing WONUM |{}| - batchCode |{}| - step |{}|",
                    wonum,
                    batchCode,
                    step,
                    e);

            return false;
        }
    }
}