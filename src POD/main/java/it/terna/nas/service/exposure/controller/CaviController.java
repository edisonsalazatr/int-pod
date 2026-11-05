package it.terna.nas.service.exposure.controller;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.terna.kernel.aspect.exposure.annotation.Exposure;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.process.ScheduleCatalog.EScheduleName;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.process.service.ProcessStatusInfoService;
import it.terna.kernel.service.exposure.ExposureResponse;
import it.terna.kernel.service.outcome.OutcomeEntity;
import it.terna.nas.service.exposure.handler.CaviInterratiRecuperoHandler;
import it.terna.nas.service.material.dto.cavi.RecoveryCaviResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/eam/cavi")
@Slf4j
public class CaviController {
	
	@Autowired
	private CaviInterratiRecuperoHandler handler;
	
	@Autowired
	private ProcessStatusInfoService processStatusInfoService;

	
	@PostMapping("/recovery-cavi")
	@Exposure(apiDomain	= "/eam/cavi/recovery-cavi", apiMethod = "POST")
	public OutcomeEntity<ExposureResponse> recoveryCavi() throws DataLayerException {
	   	
		RecoveryCaviResponse response = new RecoveryCaviResponse();

        if (!processStatusInfoService.isScheduleRunning(EScheduleName.RECUPERO_CAVI_INVIO)) {
        	String scheduleUUID = UUID.randomUUID().toString();
        	MDC.put(Common.MDC_TRANSACTION_ID, scheduleUUID);
            log.info("EAM Integration - RESEND TO CAVI - Request [{}] triggered at |{}|", scheduleUUID, System.currentTimeMillis());
            processStatusInfoService.startSchedule(EScheduleName.RECUPERO_CAVI_INVIO, scheduleUUID);
            
            CompletableFuture.runAsync(() -> {
                try {
                    handler.doRecovery();
                } catch (Exception ex) {
                    log.error("Errore durante la creazione dei messaggi di recupero.", ex);
                } finally {
                	try {
                        processStatusInfoService.closeSchedule(EScheduleName.RECUPERO_CAVI_INVIO, scheduleUUID);
                    } catch (Exception e) {
                        log.error("Errore durante la chiusura della schedule.", e);
                    } finally {
                        MDC.clear();
                    }
                }
            });
            
            response.setHttpCode(HttpStatus.ACCEPTED.value());
        	response.setEsito("OK");
        	response.setMessage(String.format(" - Started an execution with id %s", scheduleUUID));
        	   
        } else {
            log.warn("EAM Integration - RESEND TO CAVI - [{}] already running. Waiting for next execution.", EScheduleName.RECUPERO_CAVI_INVIO.name());
            response.setHttpCode(HttpStatus.CONFLICT.value());
    	    response.setEsito("KO");
    	    response.setDescrizioneErrore("Another request is being processed.");
        }
       
	    return new OutcomeEntity<ExposureResponse>(response, 
	 				   response.getResponseHeaders(),
	 				   HttpStatus.valueOf(response.getHttpCode()));
	}
}
