package it.terna.nas.service.exposure.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.RestException;
import it.terna.nas.service.dal.service.RetryableErrorService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.SendOrdersRetryException;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponseArray;
import it.terna.nas.service.exposure.ws.maximo.MicrocontrolloRequest;
import it.terna.nas.service.exposure.ws.maximo.WorkorderBulkResponse;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RetryableMicrocontrolliService {
	
    @Autowired
    private WorkorderApiService workorderApiService;

    @Autowired
    private RetryableErrorService retryableErrorService;
    
    /**
     * Metodo retryable per microcontrolli
     */
    @Retryable(
    	    value = { SendOrdersRetryException.class },
            maxAttemptsExpression = "#{@apiProperties.retry.maxAttempts}",
            backoff = @Backoff(delayExpression = "#{@apiProperties.retry.delay}"))
    public MaximoResponseArray<WorkorderBulkResponse> callMicrocontrolliApiWithRetry(
            List<MicrocontrolloRequest> microRequests)
            throws SendOrdersRetryException, EAMException, RestException {

        MaximoResponseArray<WorkorderBulkResponse> response =
                workorderApiService.insertAndUpdateMicrocontrolliBulk(microRequests, WorkorderBulkResponse[].class);

        log.debug("API POST call (Microcontrolli) completed with HTTP code: {}", response.getHttpCode());

        if (response.getHttpCode() > 300) {
            String errorMsg = response.getError() != null ? response.getError().getMessage() : null;
            log.error("Errors detected in POST Microcontrolli response: {}", errorMsg);

            if (retryableErrorService.isRetryable(errorMsg)) {
                log.warn("Recoverable error,a retry will be attempted.");
                throw new SendOrdersRetryException(EAMErrorMapping.EAM_9999.getEamCode(), errorMsg);
            }

            throw new EAMException(
                    EAMErrorMapping.EAM_9999.getEamCode(),
                    errorMsg != null ? errorMsg : "Microcontrolli not created due to non-retryable HTTP error"
            );
        }

        return response;
    }
    
    /**
     * Recover per callMicrocontrolliApiWithRetry maxRetry effettuati 
     */
    @Recover
    public MaximoResponseArray<WorkorderBulkResponse> recoverFromRetryMicro(
            SendOrdersRetryException ex, List<MicrocontrolloRequest> microRequests) throws EAMException {

        log.error("All retries failed for Microcontrolli API call: {}", ex.getMessage());
        log.warn("Request in error {}",microRequests);
        throw new RuntimeException(
                new EAMException(
                        EAMErrorMapping.EAM_9999.getEamCode(),
                        "Microcontrolli API call failed after retries: " + ex.getMessage(),
                        ex
                )
        );
    }
    
    /**
     * Recover per callMicrocontrolliApiWithRetry errore generico
     */
    @Recover
    public MaximoResponseArray<WorkorderBulkResponse> recoverFromRetry(
    		Exception ex, List<MicrocontrolloRequest> microRequests) throws EAMException {

        log.error("Error during Microcontrolli API call: {}", ex.getMessage());
        log.warn("Request in error {}",microRequests);
        throw new RuntimeException(
                new EAMException(
                        EAMErrorMapping.EAM_9999.getEamCode(),
                        "Microcontrolli API call failed: " + ex.getMessage(),
                        ex
                )
        );
    }

}
