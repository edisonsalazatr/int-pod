package it.terna.nas.service.exposure.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.exposure.ws.maximo.AbstractMaximoResponse;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponseArray;
import it.terna.nas.service.exposure.ws.maximo.MicrocontrolloRequest;
import it.terna.nas.service.exposure.ws.maximo.WorkorderGetResponse;
import it.terna.nas.service.exposure.ws.maximo.WorkorderRequest;
import it.terna.nas.service.exposure.ws.maximo.WorkorderResponse;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkorderApiService 
{
	@Autowired
	private RestHandler restHandler;
	
	@Autowired
	private MaximoApiService maximoApiService;
	
	@Autowired
	private ApiKey apiKey;
	
	private static final List<String> X4WorkorderResponseParameters = Arrays.asList("href, wonum, targcompdate, targstartdate");

	public <T extends AbstractMaximoResponse> MaximoResponseArray<T> insertAndUpdateWorkorderBulk(Boolean disableTransaction, List<WorkorderRequest> request, Class<T[]> responseType) 
			throws RestException 
	{
		
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_MXAPIWO.getEntityKey());
		String uri = restEntity.getUri();
		log.debug("call insertAndUpdateWorkorderBulk({}, {})", uri, request);
		MaximoResponseArray<T> responseWrapper = maximoApiService.maximoBulk(uri, new HttpHeaders(), responseType, 
			    restEntity.getComponent(), EamEtlUtils.listToJson(request), null, null, 
			    apiKey.retrieveSplittedApiKey(), false, disableTransaction);
		return responseWrapper;
	}
	
	public <T extends AbstractMaximoResponse> MaximoResponseArray<T> insertAndUpdateMicrocontrolliBulk(List<MicrocontrolloRequest> request, Class<T[]> responseType) 
			throws RestException 
	{
		
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4WOACTIVITY.getEntityKey());
		String uri = restEntity.getUri();
		log.debug("call insertAndUpdateMicrocontrolliBulk({}, {})", uri, request);
		MaximoResponseArray<T> responseWrapper = maximoApiService.maximoBulk(uri, new HttpHeaders(), responseType, 
			    restEntity.getComponent(), EamEtlUtils.listToJson(request), null, null, 
			    apiKey.retrieveSplittedApiKey(), false, false);
		return responseWrapper;
	}
	
	public WorkorderGetResponse retrieveWorkorders(List<String> wonumList) 
			throws RestException 
	{
		log.debug("call retrieveWorkorders({})", wonumList);
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_MXAPIWO.getEntityKey());
		Map<String, String> params = new HashMap<>();
		String wonumCondition = "wonum IN [" + String.join(",", wonumList.stream()
		        .map(w -> "\"" + w + "\"") 
		        .collect(Collectors.toList())) + "]";
		params.put(Constants.OSLC_SELECT, String.join(Constants.COMMA_SEPARATOR, X4WorkorderResponseParameters));
		params.put(Constants.OSLC_WHERE, wonumCondition);
		return maximoApiService.maximoFetch(restEntity.getUri(), new HttpHeaders(), WorkorderGetResponse.class, 
											   restEntity.getComponent(),  
											   ExposureRequest.convertQueryParamsForHTTPCall(params), 
											   apiKey.retrieveSplittedApiKey(), 
											   false);
	}
	
	public WorkorderResponse insertWorkorder(WorkorderRequest request) 
			throws RestException 
	{
		log.debug("call insertWorkorder({})", request);
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_MXAPIWO.getEntityKey());
		return maximoApiService.maximoInsert(restEntity.getUri(), new HttpHeaders(), WorkorderResponse.class, 
											 restEntity.getComponent(), request.toJson(), null, null, 
											 apiKey.retrieveSplittedApiKey(), false, false);
	}

}