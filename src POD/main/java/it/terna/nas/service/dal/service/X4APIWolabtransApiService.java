package it.terna.nas.service.dal.service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.exposure.ws.maximo.X4ApiWolabtransRequest;
import it.terna.nas.service.exposure.ws.maximo.X4ApiWolabtransResponse;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4APIWolabtransApiService 
{
	@Autowired
	private RestHandler restHandler;
	
	@Autowired
	private MaximoApiService maximoApiService;
	
	@Autowired
	private ApiKey apiKey;
	
	private static final List<String> X4ApiWolabtransResponseParameters = Arrays.asList("labtrans");
	
	public X4ApiWolabtransResponse retrieveWolabTrans(String wonum) 
			throws RestException 
	{
		log.debug("call retrieveWolabTrans({})", wonum);
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4APIWOLABTRANS.getEntityKey());
		Map<String, String> params = new HashMap<>();
		params.put(Constants.OSLC_SELECT, String.join(Constants.COMMA_SEPARATOR, X4ApiWolabtransResponseParameters));
		params.put(Constants.OSLC_WHERE, "wonum" + Constants.EQUAL + wonum);		
		return maximoApiService.maximoFetch(restEntity.getUri(), new HttpHeaders(), X4ApiWolabtransResponse.class, 
											   restEntity.getComponent(),  
											   ExposureRequest.convertQueryParamsForHTTPCall(params), 
											   apiKey.retrieveSplittedApiKey(), 
											   false);
	}

	public MaximoResponse updateWolabTrans(String uri, X4ApiWolabtransRequest request) 
			throws RestException 
	{
		log.debug("call updateWolabTrans({}, {})", uri, request);
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_X4APIWOLABTRANS.getEntityKey());
		return maximoApiService.maximoUpdate(uri, new HttpHeaders(), MaximoResponse.class, 
											 restEntity.getComponent(), request.toJson(), null, null, 
											 apiKey.retrieveSplittedApiKey(), Constants.PATCH, false, false);
	}
}