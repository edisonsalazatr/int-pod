package it.terna.nas.service.exposure.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.exception.RestException;
import static it.terna.kernel.basement.utility.Assorted.*;
import it.terna.kernel.basement.utility.Values;
import it.terna.nas.service.dal.service.RetryableErrorService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.SendOrdersRetryException;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponseArray;
import it.terna.nas.service.exposure.ws.maximo.ResponseMeta;
import it.terna.nas.service.exposure.ws.maximo.WorkorderBulkResponse;
import it.terna.nas.service.exposure.ws.maximo.WorkorderRequest;
import it.terna.nas.service.exposure.ws.maximo.WorkorderResponse;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RetryableWorkorderService {

    @Autowired
    private WorkorderApiService workorderApiService;

    @Autowired
    private RetryableErrorService retryableErrorService;

    @Retryable(
    	    value = {SendOrdersRetryException.class},
    	    maxAttemptsExpression = "#{@apiProperties.retry.maxAttempts}",
    	    backoff = @Backoff(delayExpression = "#{@apiProperties.retry.delay}"))
    public MaximoResponseArray<WorkorderBulkResponse> callWorkorderApiWithRetry(Boolean disableTransaction,
                                                                                List<WorkorderRequest> ordersToSend,
                                                                                List<WorkorderRequest> allOrders,
                                                                                Set<String> ordersSent)
            throws SendOrdersRetryException, EAMException, RestException 
    {
        MaximoResponseArray<WorkorderBulkResponse> response = workorderApiService.insertAndUpdateWorkorderBulk(disableTransaction, ordersToSend, WorkorderBulkResponse[].class);

        if (Boolean.TRUE.equals(disableTransaction))
        {
            if (isNotEmpty(response.getResponses()))
            {
                Set<String> errorSet    = new HashSet<>();
                Set<String> errorCode   = new HashSet<>();
                for (WorkorderBulkResponse respJ: response.getResponses())
                {
                    WorkorderResponse responseData  = respJ.getResponsedata();
                    ResponseMeta responseMeta       = respJ.getResponsemeta();
                    
                    if (notNull(responseMeta) && isNotEmpty(responseMeta.getStatus()))
                        errorCode.add(responseMeta.getStatus());
                    
                    if (notNull(responseData) && notNull(responseData.getErrorData()) && isNotEmpty(responseData.getErrorData().getMessage()))
                        errorSet.add(responseData.getErrorData().getMessage()); 
                }
                
                long numErrors = errorSet.stream().filter(x -> Integer.parseInt(x) > 300).count();
                log.debug("API POST call completed with {} errors", numErrors);
                if (numErrors > 0)
                {
                    String errorMsg = String.join(System.lineSeparator(), errorSet);
                    log.error("Errors detected in POST response: {}",     errorMsg);
                    
                    if (retryableErrorService.isRetryable(errorMsg) && ordersToSend.size() == 1) 
                    {
                        log.warn("Recoverable error,a retry will be attempted.");
                        throw new SendOrdersRetryException(EAMErrorMapping.EAM_9999.getEamCode(), errorMsg);
                    }
                    
                    throw new EAMException(EAMErrorMapping.EAM_9999.getEamCode(), errorMsg != null ? errorMsg : "OdL not created due to HTTP error");
                
                } else {
                    ordersToSend.stream().forEach(x -> ordersSent.add(x.getWonum()));
                }
            }
            
        } else {

            log.debug("API POST call completed with HTTP code: {}", response.getHttpCode());
            
            if (response.getHttpCode() > 300) 
            {
                String errorMsg = response.getError() != null ? response.getError().getMessage() : null;
                log.error("Errors detected in POST response: {}", errorMsg);

                if (retryableErrorService.isRetryable(errorMsg)) 
                {
                    log.warn("Recoverable error,a retry will be attempted.");
                    throw new SendOrdersRetryException(EAMErrorMapping.EAM_9999.getEamCode(), errorMsg);
                }

                throw new EAMException(EAMErrorMapping.EAM_9999.getEamCode(), errorMsg != null ? errorMsg : "OdL not created due to HTTP error");
                
            } else {
                ordersToSend.stream().forEach(x -> ordersSent.add(x.getWonum()));
            }
        }


        return response;
    }

    
    /**
     * Recover per callWorkorderApiWithRetry maxRetry effettuati 
     */
    @Recover
    public MaximoResponseArray<WorkorderBulkResponse> recoverFromRetry( SendOrdersRetryException ex, 
                                                                        Boolean disableTransaction,
                                                                        List<WorkorderRequest> ordersToSend,
                                                                        List<WorkorderRequest> allOrders,
                                                                        Set<String> ordersSent) 
    {
        log.error("All retries failed for Workorder API call: {}", ex.getMessage());

        log.warn("WorkOrder request in error {}", ordersToSend);
        String errorMsg = ex.getMessage();
        
        throw buildSendOrderException(ordersToSend, allOrders, ordersSent, errorMsg);
    }
    
    
    @Recover
    public MaximoResponseArray<WorkorderBulkResponse> recoverFromRetry( Exception ex, 
                                                                        Boolean disableTransaction,
                                                                        List<WorkorderRequest> ordersToSend,
                                                                        List<WorkorderRequest> allOrders,
                                                                        Set<String> ordersSent) 
    {
        log.error("All retries failed for Workorder API call: {}", ex.getMessage());
        log.warn("WorkOrder request in error {}", ordersToSend);
        String errorMsg = ex.getMessage();
        
        throw buildSendOrderException(ordersToSend, allOrders, ordersSent, errorMsg);
    }
    
    
	private RuntimeException buildSendOrderException(List<WorkorderRequest> ordersToSend,
	                                                 List<WorkorderRequest> allOrders,
	                                                 Set<String> ordersSent, 
	                                                 String errorMsg) 
	{
		// Determine which order were NOT sent
		Set<String> missingWonum = allOrders.stream()
											.map(x -> x.getWonum())
											.filter(x -> !ordersSent.contains(x))
											.collect(Collectors.toSet());
		

		// Always include default context message
		String defaultMessage = String.format("OdL with wonum='%s' not created.", ordersToSend.get(Values.FIRST_ELEMENT).getWonum());

		// Combine errorMsg (if any) with the default message
		String combinedMessage = isNotEmpty(errorMsg) 
									? String.format("%s. %s", errorMsg, defaultMessage)
									: defaultMessage;

		// Incomplete message includes missing years
		String incompleteMessage = String.format("%s. Missing wonum not sent: %s", combinedMessage,
				missingWonum.isEmpty() ? "none" : missingWonum);

		// Build exceptions
		EAMException exceptionIncomplete = new EAMException(EAMErrorMapping.EAM_0500.getEamCode(), incompleteMessage);

		EAMException exception9999 = new EAMException(EAMErrorMapping.EAM_9999.getEamCode(), combinedMessage);

		// Return correct exception
		if (allOrders.size() == 1 || ordersSent.isEmpty()) {
			return new RuntimeException(exception9999);
		}
		return new RuntimeException(exceptionIncomplete);
    }
}