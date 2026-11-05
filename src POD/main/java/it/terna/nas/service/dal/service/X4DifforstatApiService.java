package it.terna.nas.service.dal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.material.dto.X4DifforstatRequest;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;

@Service
public class X4DifforstatApiService {
	
	@Autowired
	private RestHandler restHandler;
	
	@Autowired
	private MaximoApiService maximoApiService;
	
	@Autowired
	private ApiKey apiKey;
	
	public MaximoResponse sendDifformita(X4DifforstatRequest difformitaRequest) 
			throws RestException 
	{
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.MAXIMO_POST_X4DIFFORSTAT.getEntityKey());
		return maximoApiService.maximoInsert(restEntity.getUri(), new HttpHeaders(), MaximoResponse.class, 
											 restEntity.getComponent(), difformitaRequest.toJson(), null, null, 
											 apiKey.retrieveSplittedApiKey(), false);
	}

}
