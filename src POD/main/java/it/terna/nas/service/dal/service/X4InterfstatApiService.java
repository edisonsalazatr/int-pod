package it.terna.nas.service.dal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.nas.service.exposure.ws.maximo.AbstractMaximoResponse;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponseArray;
import it.terna.nas.service.material.dto.X4InterfstatRequest;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class X4InterfstatApiService {
	
	@Autowired
	private RestHandler restHandler;
	
	@Autowired
	private MaximoApiService maximoApiService;
	
	@Autowired
	private ApiKey apiKey;
	
	public MaximoResponse sendInterferenza(X4InterfstatRequest interferenzaRequest) 
			throws RestException 
	{
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_POST_X4INTERFSTAT.getEntityKey());
		return maximoApiService.maximoInsert(restEntity.getUri(), new HttpHeaders(), MaximoResponse.class, 
											 restEntity.getComponent(), interferenzaRequest.toJson(), null, null, 
											 apiKey.retrieveSplittedApiKey(), false);
	}
	
	public <T extends AbstractMaximoResponse> MaximoResponseArray<T> insertAndUpdateX4InterfstatBulk(List<X4InterfstatRequest> request, Class<T[]> responseType) 
			throws RestException 
	{
		
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_POST_X4INTERFSTAT.getEntityKey());
		String uri = restEntity.getUri();
		log.debug("call insertAndUpdatex4interfwoBulk({}, {})", uri, request);
		MaximoResponseArray<T> responseWrapper = maximoApiService.maximoBulk(uri, new HttpHeaders(), responseType, 
			    restEntity.getComponent(), EamEtlUtils.listToJson(request), null, null, 
			    apiKey.retrieveSplittedApiKey(), false);
		return responseWrapper;
	}
	

}
