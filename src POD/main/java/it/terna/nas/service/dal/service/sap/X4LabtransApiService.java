package it.terna.nas.service.dal.service.sap;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.dal.service.ApiKey;
import it.terna.nas.service.dal.service.MaximoApiService;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.ws.maximo.LabTransItem;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.exposure.ws.sap.X4LabtransRequest;
import it.terna.nas.service.exposure.ws.sap.X4LabtransResponse;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class X4LabtransApiService {

    @Autowired
    private RestHandler restHandler;

    @Autowired
    private MaximoApiService maximoApiService;

    @Autowired
    private ApiKey apiKey;

    public X4LabtransResponse retrieveLabtransByRefwo(String refwo)
            throws RestException
    {
        log.debug("call retrieveLabtransByRefwo({})", refwo);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4LABTRANS.getEntityKey());
        Map<String, String> params = new HashMap<>();
        params.put(Constants.OSLC_SELECT, Constants.SELECT_ALL);
        params.put(Constants.OSLC_WHERE, Constants.REFWO + Constants.EQUAL + Constants.DOUBLE_QUOTES + refwo + Constants.DOUBLE_QUOTES);
        return maximoApiService.maximoFetch(restEntity.getUri(), new HttpHeaders(), X4LabtransResponse.class,
                restEntity.getComponent(),
                ExposureRequest.convertQueryParamsForHTTPCall(params),
                apiKey.retrieveSplittedApiKey(),
                false);
    }



    public MaximoResponse updateLabtrans(String uri, X4LabtransRequest request)
            throws RestException
    {
        log.debug("call updateLabtrans({}, {})", uri, request);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4LABTRANS.getEntityKey());
        return maximoApiService.maximoUpdate(uri, new HttpHeaders(), MaximoResponse.class,
                restEntity.getComponent(), request.toJson(), null, null,
                apiKey.retrieveSplittedApiKey(), Constants.PATCH, true, false);
    }

    public X4LabtransResponse retrieveLabtransById(Long labtransid)
            throws RestException
    {
        log.debug("call retrieveLabtransById({})", labtransid);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4LABTRANS.getEntityKey());
        Map<String, String> params = new HashMap<>();
        params.put(Constants.OSLC_SELECT, Constants.SELECT_ALL);
        params.put(Constants.OSLC_WHERE, Constants.LABTRANSID + Constants.EQUAL + labtransid);
        return maximoApiService.maximoFetch(restEntity.getUri(), new HttpHeaders(), X4LabtransResponse.class,
                restEntity.getComponent(),
                ExposureRequest.convertQueryParamsForHTTPCall(params),
                apiKey.retrieveSplittedApiKey(),
                false);
    }
    
    @Retryable(value = {EAMRetryException.class}, maxAttemptsExpression = "${resubmitLabTransConsuntivi:1}", backoff = @Backoff(delay = 1000, multiplier = 2))
    public MaximoResponse insertLabtrans(LabTransItem labTransItem) throws RestException, EAMRetryException
    {
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4LABTRANS.getEntityKey());
        MaximoResponse response = maximoApiService.maximoInsert(restEntity.getUri(), new HttpHeaders(), MaximoResponse.class,
												                restEntity.getComponent(), labTransItem.toJson(), null, null,
												                apiKey.retrieveSplittedApiKey(), false);
        if (response.getHttpCode() != HttpStatus.OK.value() 
        		&& Assorted.notNull(response.getErrorInfo())
        			&& Constants.MAXIMO_SAME_USER_CONSTRAINT.equalsIgnoreCase(response.getErrorInfo().getReasonCode())) 
        {
        	log.error("insertLabtrans tracked multiple user on same record error. Executing Retry.");
			throw new EAMRetryException(EAMErrorMapping.EAM_0000.getEamCode(), EamEtlUtils.errorGenericMessage(response.getError()));
        }
        return response;
    }
    
    public X4LabtransResponse retrieveLabtransByCorrelationID(Long correlationID)
            throws RestException
    {
        log.debug("call retrieveLabtransByCorrelationID({})", correlationID);
        RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4LABTRANS.getEntityKey());
        Map<String, String> params = new HashMap<>();
        params.put(Constants.OSLC_SELECT, Constants.SELECT_ALL);
        params.put(Constants.OSLC_WHERE, Constants.CORRELATIONID + Constants.EQUAL + correlationID);
        return maximoApiService.maximoFetch(restEntity.getUri(), new HttpHeaders(), X4LabtransResponse.class,
                restEntity.getComponent(),
                ExposureRequest.convertQueryParamsForHTTPCall(params),
                apiKey.retrieveSplittedApiKey(),
                false);
    }
}