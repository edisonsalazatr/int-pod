package it.terna.nas.service.exposure.controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import it.terna.kernel.aspect.exposure.annotation.Exposure;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.service.exposure.ExposureResponse;
import it.terna.kernel.service.outcome.OutcomeEntity;
import it.terna.nas.service.exposure.handler.GinRecoveryHandler;
import it.terna.nas.service.material.dto.agile.RecoveryGinResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/eam/gin")
@Slf4j
public class GinController {

    private final AtomicBoolean recoveryRunning = new AtomicBoolean(false);

    @Autowired
    private GinRecoveryHandler ginRecoveryHandler;

    @PostMapping("/recovery-odl-indisp")
    @Exposure(apiDomain = "/eam/gin/recovery-odl-indisp", apiMethod = "POST")
    public OutcomeEntity<ExposureResponse> recoveryGin(@RequestParam("batchCode") String batchCode) throws DataLayerException {

        RecoveryGinResponse response = new RecoveryGinResponse();

        if (!StringUtils.hasText(batchCode)) {
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            response.setEsito("KO");
            response.setMessage("Missing required parameter batchCode");

            return new OutcomeEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        if (!recoveryRunning.compareAndSet(false, true)) {
            response.setHttpCode(HttpStatus.CONFLICT.value());
            response.setEsito("KO");
            response.setMessage("GIN ODL RECOVERY is already running");

            return new OutcomeEntity<>(response, new HttpHeaders(), HttpStatus.CONFLICT);
        }

        String scheduleUUID = UUID.randomUUID().toString();

        try {
            MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID);

            log.info("EAM Integration - GIN ODL RECOVERY - Request [{}] triggered at |{}| - batchCode |{}|",
                    scheduleUUID,
                    System.currentTimeMillis(),
                    batchCode);

            String finalBatchCode = batchCode;

            CompletableFuture.runAsync(() -> {
                try {
                    MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID);

                    log.info("EAM Integration - GIN ODL RECOVERY - Async execution [{}] started - batchCode |{}|",
                            scheduleUUID,
                            finalBatchCode);

                    ginRecoveryHandler.odlRecovery(finalBatchCode);

                    log.info("EAM Integration - GIN ODL RECOVERY - Async execution [{}] completed - batchCode |{}|",
                            scheduleUUID,
                            finalBatchCode);

                } catch (Exception ex) {
                    log.error("Errore durante la procedura di recupero GIN ODL - executionId [{}] - batchCode [{}]",
                            scheduleUUID,
                            finalBatchCode,
                            ex);
                } finally {
                    recoveryRunning.set(false);
                    MDC.clear();
                }
            });

            response.setHttpCode(HttpStatus.ACCEPTED.value());
            response.setEsito("OK");
            response.setMessage(String.format("Started GIN ODL recovery execution with id %s for batchCode %s", scheduleUUID, batchCode));

            return new OutcomeEntity<>(response, new HttpHeaders(), HttpStatus.ACCEPTED);

        } finally {
            MDC.clear();
        }
    }
}