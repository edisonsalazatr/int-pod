package it.terna.nas.service.exposure.handler;

import java.util.Iterator;
import java.util.stream.Stream;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.service.EliveRecoveryService;
import it.terna.nas.service.dal.service.EliveRecoveryUpdateService;
import it.terna.nas.service.dal.service.repository.X4EliveRecoveryRepository;
import it.terna.nas.service.material.dto.KafkaOdlToEliveRequest;
import it.terna.nas.service.material.dto.elive.EliveRecoveryProcessResult;
import it.terna.nas.service.producer.EliveRecoveryKafkaProducer;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EliveOdlRecoveryHandler {

    private static final int LOG_PROGRESS_SIZE = 100;

    @Autowired
    private X4EliveRecoveryRepository repository;

    @Autowired
    private EliveRecoveryService eliveRecoveryService;

    @Autowired
    private EliveRecoveryUpdateService updateService;

    @Autowired
    private EliveRecoveryKafkaProducer eliveRecoveryKafkaProducer;

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(readOnly = true)
    public void odlRecoveryToElive(String batchCode) {

        log.info("ODL ELIVE recovery START - batchCode |{}|", batchCode);

        int totalRead = 0;
        int totalSent = 0;
        int totalKo = 0;

        try (Stream<String> wonums = repository.getListWonumToRecover(batchCode)) {

            Iterator<String> iterator = wonums.iterator();

            while (iterator.hasNext()) {

                String wonum = iterator.next();
                totalRead++;

                EliveRecoveryProcessResult result = processWonum(batchCode, wonum);

                if (result.isSuccess()) {
                    updateService.markAsProcessed(batchCode, result.getRequest());
                    totalSent++;
                } else {
                    updateService.markAsKo(batchCode, wonum, result.getErrorMessage());
                    totalKo++;
                }

                if (totalRead % LOG_PROGRESS_SIZE == 0) {
                    log.info(
                        "ODL ELIVE recovery progress - batchCode |{}| - read |{}| - sent |{}| - ko |{}|",
                        batchCode,
                        totalRead,
                        totalSent,
                        totalKo
                    );
                }
            }

            log.info(
                "ODL ELIVE recovery END - batchCode |{}| - read |{}| - sent |{}| - ko |{}|",
                batchCode,
                totalRead,
                totalSent,
                totalKo
            );

        } catch (Exception e) {

            log.error(
                "ODL ELIVE recovery fatal error - batchCode |{}| - read |{}| - sent |{}| - ko |{}|",
                batchCode,
                totalRead,
                totalSent,
                totalKo,
                e
            );

            throw new RuntimeException(
                "ODL ELIVE recovery failed for batchCode " + batchCode,
                e
            );
        }
    }
    private EliveRecoveryProcessResult processWonum(String batchCode, String wonum) {
        String step = "START";

        try {
            log.debug("Processing ELIVE recovery WONUM - batchCode |{}| - wonum |{}|",
                    batchCode,
                    wonum);

            step = "LOAD_WORKORDER";
            Tuple tuple = repository.loadWorkOrderForElive(wonum);

            if (tuple == null) {
                return EliveRecoveryProcessResult.ko("No WORKORDER found for WONUM " + wonum);
            }

            step = "BUILD_ELIVE_REQUEST";
            KafkaOdlToEliveRequest kafkaMessage =
                    eliveRecoveryService.buildKafkaOdlToEliveRequest(tuple);

            step = "SEND_TO_ELIVE";
            eliveRecoveryKafkaProducer.sendOdlToElive(kafkaMessage);


            return EliveRecoveryProcessResult.ok(kafkaMessage);

        } catch (Exception e) {
            log.error("Error processing ELIVE WONUM |{}| - batchCode |{}| - step |{}|",
                    wonum,
                    batchCode,
                    step,
                    e);

            return EliveRecoveryProcessResult.ko("Step " + step + " failed: " + e.getMessage());
        }
    }
}
