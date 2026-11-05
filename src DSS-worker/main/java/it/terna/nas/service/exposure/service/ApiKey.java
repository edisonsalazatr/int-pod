package it.terna.nas.service.exposure.service;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureResponse;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ApiKey
{
	private static final SecureRandom SECURE_RANDOM_OBJ = new SecureRandom();

//	@Value("${maximoApiKey}")
//	private String apiKey;
	
	@Value("${maximoApiKey1}, ${maximoApiKey2}")
	private String[] apiKeys;
	
	
	public static <T> String getPodName(ResponseEntity<T> responseEntity)
	{
		String podName = "";
		if (Assorted.notNull(responseEntity)) 
		{
			if (Assorted.isNotEmpty(responseEntity.getHeaders())) 
			{
				if (responseEntity.getHeaders().containsKey(Constants.POD_NAME)) 
				{
					podName = responseEntity.getHeaders().getFirst(Constants.POD_NAME);
				} else {
					log.info("No podName in response headers");
				}
			}
		}

		return podName;
	}
	
	
	public static <T> String getPodName(ExposureResponse response)
	{
		String podName = "";
		if (Assorted.notNull(response)) 
		{
			if (Assorted.isNotEmpty(response.getResponseHeaders())) 
			{
				if (response.getResponseHeaders().containsKey(Constants.POD_NAME)) 
				{
					podName = response.getResponseHeaders().getFirst(Constants.POD_NAME);
				} else {
					log.info("No podName in response headers");
				}
			}
		}

		return podName;
	}
	
	
	public synchronized String retrieveSplittedApiKey()
	{
		String retValue = "";
		if (apiKeys != null && apiKeys.length > 0) 
		{
			//String[] apiKeyList = apiKey.split("&&");
			int index = SECURE_RANDOM_OBJ.nextInt(apiKeys.length);
			retValue = apiKeys[index];
		}
		
		return retValue;
	}
	
//	@PostConstruct
//	private void show() {
//		System.out.println("");
//		log.debug(" reading key {}", apiKeys);
//	}
}
