package it.terna.nas.service.dal.service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.exposure.ws.maximo.AbstractMaximoResponse;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponseArray;
import it.terna.nas.service.exposure.ws.maximo.WorkorderGetResponse;
import it.terna.nas.service.exposure.ws.maximo.WorkorderRequest;
import it.terna.nas.service.exposure.ws.maximo.X4WorkorderRequest;
import it.terna.nas.service.exposure.ws.maximo.X4WorkorderResponse;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4WorkorderApiService 
{
	@Autowired
	private RestHandler restHandler;
	
	@Autowired
	private MaximoApiService maximoApiService;
	
	@Autowired
	private ApiKey apiKey;
	
	private static final List<String> X4WorkorderResponseParameters = Arrays.asList("wonum", "status", "changedate", "parent");
	
	public X4WorkorderResponse retrieveWorkorder(String wonum) 
			throws RestException 
	{
		log.debug("call retrieveWorkorder({})", wonum);
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4WORKORDER.getEntityKey());
		Map<String, String> params = new HashMap<>();
		params.put(Constants.OSLC_SELECT, String.join(Constants.COMMA_SEPARATOR, X4WorkorderResponseParameters));
		params.put(Constants.OSLC_WHERE, Constants.WONUM + Constants.EQUAL + Constants.DOUBLE_QUOTES + wonum + Constants.DOUBLE_QUOTES);		
		return maximoApiService.maximoFetch(restEntity.getUri(), new HttpHeaders(), X4WorkorderResponse.class, 
											   restEntity.getComponent(),  
											   ExposureRequest.convertQueryParamsForHTTPCall(params), 
											   apiKey.retrieveSplittedApiKey(), 
											   false);
	}
	
	public MaximoResponse retrieveResource(String uri, Class<? extends MaximoResponse> clazz) {
		log.debug("call retrieveResource({})", uri);
		
		MaximoResponse response = null;
		try {
			Map<String, String> params = new HashMap<>();
			response = maximoApiService.maximoFetch(uri, new HttpHeaders(), clazz, 
												   "EAM_RESOURCE",  
												   ExposureRequest.convertQueryParamsForHTTPCall(params), 
												   apiKey.retrieveSplittedApiKey(), 
												   false);
		} catch (RestException re) {
			log.error(" - Error retrieving resource from EAM: [uri:{}]", uri);
			
			try {
				response = clazz.getDeclaredConstructor().newInstance();
				response.setHttpCode(HttpStatus.BAD_REQUEST.value());
			} catch (NoSuchMethodException 
						| SecurityException 
						| InstantiationException 
						| IllegalAccessException 
						| IllegalArgumentException 
						| InvocationTargetException e) {
				log.error(" -- Error constructing object response of type {}\n Exception is:\n {}", clazz.getName(), e);
			}
		}
		return response;
		
	}

	public MaximoResponse updateWorkorder(String uri, X4WorkorderRequest request) 
			throws RestException 
	{
		log.debug("call updateWorkorder({}, {})", uri, request.toJson());
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4WORKORDER.getEntityKey());
		return maximoApiService.maximoUpdate(uri, new HttpHeaders(), MaximoResponse.class, 
											 restEntity.getComponent(), request.toJson(), null, null, 
											 apiKey.retrieveSplittedApiKey(), Constants.PATCH, true, false);
	}
	
	public WorkorderGetResponse retrieveWorkorders(List<String> wonumList) 
			throws RestException 
	{
		log.debug("call retrieveWorkorders({})", wonumList);
		List<String> parameters = Arrays.asList("wonum", "status", "changedate", "parent", "targcompdate", "targstartdate");
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4WORKORDER.getEntityKey());
		Map<String, String> params = new HashMap<>();
		String wonumCondition = "wonum IN [" + String.join(",", wonumList.stream()
		        .map(w -> "\"" + w + "\"") 
		        .collect(Collectors.toList())) + "]";
		params.put(Constants.OSLC_SELECT, String.join(Constants.COMMA_SEPARATOR, parameters));		
		params.put(Constants.OSLC_WHERE, wonumCondition);
		params.put(Constants.OSLC_PAGE_SIZE, Constants.PAGE_SIZE);
		params.put(Constants.OSLC_PAGING, "true");
		return maximoApiService.maximoFetch(restEntity.getUri(), new HttpHeaders(), WorkorderGetResponse.class, 
											   restEntity.getComponent(),  
											   ExposureRequest.convertQueryParamsForHTTPCall(params), 
											   apiKey.retrieveSplittedApiKey(), 
											   false);
	}
	
	public <T extends AbstractMaximoResponse> MaximoResponseArray<T> insertAndUpdateX4WorkorderBulk(List<WorkorderRequest> request, Class<T[]> responseType) 
	{
		
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4WORKORDER.getEntityKey());
		String uri = restEntity.getUri();
		log.debug("call insertAndUpdateWorkorderBulk({}, {})", uri, request);
		MaximoResponseArray<T> responseWrapper;
		try {
			responseWrapper = maximoApiService.maximoBulk(uri, new HttpHeaders(), responseType, 
				    restEntity.getComponent(), EamEtlUtils.listToJson(request), null, null, 
				    apiKey.retrieveSplittedApiKey(), false);
			return responseWrapper;
		} catch (Exception e) {
			return null;
		}
	}
	
	public <T extends AbstractMaximoResponse> MaximoResponseArray<T> insertAndUpdateX4WorkorderChangeStatusBulk(List<X4WorkorderRequest> request, Class<T[]> responseType) 
	{
		
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4WORKORDER.getEntityKey());
		String uri = restEntity.getUri();
		log.debug("call insertAndUpdateX4WorkorderChangeStatusBulk({}, {})", uri, request);
		MaximoResponseArray<T> responseWrapper;
		try {
			responseWrapper = maximoApiService.maximoBulk(uri, new HttpHeaders(), responseType, 
				    restEntity.getComponent(), EamEtlUtils.listToJson(request), null, null, 
				    apiKey.retrieveSplittedApiKey(), false);
			return responseWrapper;
		} catch (Exception e) {
			return null;
		}
	}
}