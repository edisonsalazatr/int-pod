package it.terna.nas.service.dal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.basement.utility.Common.EHTTPMethod;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.nas.service.exposure.ws.maximo.X4NotifyScheduleResponse;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;

@Service
public class X4NotifyScheduleOdlApiService 
{
	@Autowired
	private RestHandler restHandler;
	
	@Autowired
	private MaximoApiService maximoApiService;
	
	@Autowired
	private ApiKey apiKey;
	
	public X4NotifyScheduleResponse launchNotifyScheduleOdlScript(String body) 
			throws RestException 
	{
		RestEntityInfo restEntity = restHandler.getRestEntity(EDataIntegrationEntity.X4NOTIFYSCHEDULEODL.getEntityKey());
		return maximoApiService.maximoLaunchScript(restEntity.getUri(), new HttpHeaders(), X4NotifyScheduleResponse.class, 
											 restEntity.getComponent(), body, null, null, 
											 apiKey.retrieveSplittedApiKey(), false, 
											 EHTTPMethod.fromOperation(EHTTPMethod.fromHTTPMethodName(restEntity.getMethod())));
	}
}