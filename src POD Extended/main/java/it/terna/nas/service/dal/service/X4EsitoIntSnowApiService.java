package it.terna.nas.service.dal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.material.dto.X4EsitoIntSnowRequest;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class X4EsitoIntSnowApiService 
{
	@Autowired
	private RestHandler restHandler;
	
	@Autowired
	private MaximoApiService maximoApiService;
	
	@Autowired
	private ApiKey apiKey;
	
	public MaximoResponse sendEsitoIntSnow(X4EsitoIntSnowRequest esitoRequest) 
			throws RestException 
	{
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_POST_X4ESITOINTSNOW.getEntityKey());
		log.debug("EsitoInt Snow Request: " + esitoRequest.toJson());
		return maximoApiService.maximoInsert(restEntity.getUri(), new HttpHeaders(), MaximoResponse.class, 
											 restEntity.getComponent(), esitoRequest.toJson(), null, null, 
											 apiKey.retrieveSplittedApiKey(), false);
	}
}