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
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.service.exposure.ExposureResponse;
import it.terna.kernel.service.outcome.OutcomeEntity;
import it.terna.nas.service.exposure.handler.EliveOdlRecoveryHandler;
import it.terna.nas.service.material.dto.elive.RecoveryEliveResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/eam/eliveRecovery")
@Slf4j
public class EliveRecoveryController {

    private final AtomicBoolean recoveryRunning = new AtomicBoolean(false);

    @Autowired
    private EliveOdlRecoveryHandler eliveOdlRecoveryHandler;

    @PostMapping("/recovery-odl")
    @Exposure(apiDomain = "/eam/eliveRecovery/recovery-odl", apiMethod = "POST")
    public OutcomeEntity<ExposureResponse> recoveryElive(@RequestParam("batchCode") String batchCode) {

        RecoveryEliveResponse response = new RecoveryEliveResponse();

        if (!StringUtils.hasText(batchCode)) {
            response.setHttpCode(HttpStatus.BAD_REQUEST.value());
            response.setEsito("KO");
            response.setMessage("Missing required parameter batchCode");

            return new OutcomeEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }

        if (!recoveryRunning.compareAndSet(false, true)) {
            response.setHttpCode(HttpStatus.CONFLICT.value());
            response.setEsito("KO");
            response.setMessage("ELIVE ODL recovery is already running");

            return new OutcomeEntity<>(response, new HttpHeaders(), HttpStatus.CONFLICT);
        }

        String executionId = UUID.randomUUID().toString();

        try {
            MDC.put(Common.MDC_TRANSACTION_ID, executionId);

            log.info("EAM Integration - ELIVE ODL RECOVERY - Request [{}] triggered - batchCode |{}|",
                    executionId,
                    batchCode);

            CompletableFuture.runAsync(() -> {
                try {
                    MDC.put(Common.MDC_TRANSACTION_ID, executionId);

                    log.info("EAM Integration - ELIVE ODL RECOVERY - Async execution [{}] started - batchCode |{}|",
                            executionId,
                            batchCode);

                    eliveOdlRecoveryHandler.odlRecoveryToElive(batchCode);

                    log.info("EAM Integration - ELIVE ODL RECOVERY - Async execution [{}] completed - batchCode |{}|",
                            executionId,
                            batchCode);

                } catch (Exception ex) {
                    log.error("Error during ELIVE ODL recovery - executionId [{}] - batchCode [{}]",
                            executionId,
                            batchCode,
                            ex);
                } finally {
                    recoveryRunning.set(false);
                    MDC.clear();
                }
            });

            response.setHttpCode(HttpStatus.ACCEPTED.value());
            response.setEsito("OK");
            response.setMessage(String.format(
                    "Started ELIVE ODL recovery execution with id %s for batchCode %s",
                    executionId,
                    batchCode
            ));

            return new OutcomeEntity<>(response, new HttpHeaders(), HttpStatus.ACCEPTED);

        } finally {
            MDC.clear();
        }
    }
}