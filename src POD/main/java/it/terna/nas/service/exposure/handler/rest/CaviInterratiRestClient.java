package it.terna.nas.service.exposure.handler.rest;

import static it.terna.kernel.basement.utility.Common.EHTTPMethod.fromHTTPMethodName;
import static it.terna.kernel.basement.utility.Common.EHTTPMethod.fromOperation;

import java.lang.reflect.Modifier;
import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import it.terna.kernel.basement.json.gson.serdes.OffsetDateTimeSerDes;
import it.terna.kernel.basement.json.gson.strategy.exclusion.GsonExclusionStrategy;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Values;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.ExposureHeaders;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.configuration.properties.OAuth2Properties;
import it.terna.nas.service.exposure.ws.maximo.JwtTokenResponse;
import it.terna.nas.service.material.dto.cavi.InvioOdlDeletionToCaviRequest;
import it.terna.nas.service.material.dto.cavi.InvioOdlDeletionToCaviResponse;
import it.terna.nas.service.material.dto.cavi.InvioOdlToCaviRequest;
import it.terna.nas.service.material.dto.cavi.InvioOdlToCaviResponse;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CaviInterratiRestClient extends JWTAwareRestClient {
	
	@Value("${http.oauth2.cavi.api-key-cavi-interrati}")
	private String		apikey;
	
	@Autowired
	private RestHandler				restHandler;
	
	@Autowired 
	private OAuth2Properties oauth2CaviInterrati;

	/**
	 * 
	 * @param request
	 * @return
	 */
	public InvioOdlDeletionToCaviResponse sendDeletionToCavi(InvioOdlDeletionToCaviRequest request) {
		
		InvioOdlDeletionToCaviResponse response = new InvioOdlDeletionToCaviResponse();
		
		long startTime = System.currentTimeMillis(); 
		
		EDataIntegrationEntity integrationEntity = EDataIntegrationEntity.CAVI_DELETE_ODL;
		// get information about endpoint to call
		RestEntityInfo 			    restEntity = restHandler.getRestEntity(integrationEntity.getEntityKey());
		
		try
		{
			log.debug("  - OdL to CAVI - retrieving from {} HTTP call [{}]", integrationEntity.getEntityKey(),
			          														 restEntity.getUri());
			
			// prepare queryParams
     	 	Map<String, String> queryParams = new HashMap<>();
     	 	
     	 	String json = Assorted.toJson(new String[] {request.getWonum()});
     	 	
     	 	log.info(" - request body: {}", json);
			
			// Calling external API using HTTP library
			ResponseEntity<InvioOdlDeletionToCaviResponse[]> responseEntity = restHandler.httpCall(EamEtlUtils.getUri(restEntity),
																		buildHeaders(restEntity, null),
																		InvioOdlDeletionToCaviResponse[].class,
																		restEntity.getComponent(),
																		json,
																		null,
																		ExposureRequest.convertQueryParamsForHTTPCall(queryParams),
																		true,
																		fromOperation(fromHTTPMethodName(restEntity.getMethod())));
			
			log.info(" - response body: {}", Assorted.notNull(responseEntity, responseEntity.getBody()) 
																? responseEntity.getBody() 
																: null);
			log.trace("  - OdL DELETION to CAVI - sent {} HTTP call [{}] response [{}]", restEntity, 
																						 restEntity.getUri(), 
																						 response);
			
			InvioOdlDeletionToCaviResponse[] body = responseEntity != null
														? responseEntity.getBody()
														: null;

		    if (body != null && body.length > 0) {
		    	response = responseEntity.getBody()[Values.FIRST_ELEMENT];
		    	response.setHttpCode(responseEntity.getStatusCodeValue());
		    } else {
		    	response.setHttpCode(HttpStatus.TOO_MANY_REQUESTS.value());
		    }
			
			log.trace("  - OdL DELETION to CAVI - sent {} HTTP call [{}] response [{}]", integrationEntity.getEntityKey(), 
																								restEntity.getUri(),
																					 			response);
		}
		catch ( Exception ex )
		{
			// log occurred error
			log.error("  - OdL DELETION to CAVI - Error occurred during EAM-INTEGRATION {} HTTP call [{}]", 
																	integrationEntity.getEntityKey(), 
																	restEntity.getUri(),
																	ex);
		
			response = new InvioOdlDeletionToCaviResponse();
			response.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} 
		finally 
		{
			response.setIntegrationEntity(integrationEntity);
			response.setRequest(request);
			//in the response must be set the value retrieved from the request, because it can be the MT/null case
			
			log.info("  - OdL DELETION to CAVI - retrieve {} >> [{}] HTTP call [{}] took [{}] milliseconds", 
										integrationEntity.getEntityKey(),
								restEntity.getUri(), 
				          		System.currentTimeMillis() - startTime);
			
		}
		return response;
	}
	
	private HttpHeaders buildHeaders(RestEntityInfo restEntity, ExposureHeaders nativeHeaders) {
		HttpHeaders headers = restHandler.buildHeaders(restEntity, nativeHeaders);
		if(headers == null) {
			headers = new HttpHeaders();
		}
		headers.add(Constants.HEADER_X_API_KEY, apikey);
		JwtTokenResponse tokenResponse = retrieveJwtToken(oauth2CaviInterrati.getAzureUri(),
														  oauth2CaviInterrati.getTokenEndpoint(),
														  oauth2CaviInterrati.getClientId(),
														  oauth2CaviInterrati.getClientSecret(),
														  oauth2CaviInterrati.getClientCredential(),
														  oauth2CaviInterrati.getScope());
		
		if(tokenResponse != null) {
			headers.setBearerAuth(tokenResponse.getAccessToken());
		} else {
			log.error("  - Error retrieving jwt token for auth");
		}
		
		return headers;
	}
	
	/**
	 * 
	 * @param transactionId
	 * @param request
	 * @return
	 */
	public InvioOdlToCaviResponse sendOdlToCavi(InvioOdlToCaviRequest	request) {
		
		InvioOdlToCaviResponse result = new InvioOdlToCaviResponse();
		
		long startTime = System.currentTimeMillis(); 
		
		EDataIntegrationEntity integrationEntity = EDataIntegrationEntity.CAVI_INVIO_ODL;
		// get information about endpoint to call
		RestEntityInfo 			    restEntity = restHandler.getRestEntity(integrationEntity.getEntityKey());
		
		ResponseEntity<String> responseEntity = null;
		
		try
		{
			log.debug("  - OdL to CAVI - retrieving from {} HTTP call [{}]", integrationEntity.getEntityKey(),
			          														 restEntity.getUri());
			// prepare queryParams
     	 	Map<String, String> queryParams = new HashMap<>();     	 	
     	 	String json = Assorted.toJson(new InvioOdlToCaviRequest[] {request});
     	 	
     	 	log.info(" - request body: {}", json);
     	 	
			// Calling external API using HTTP library
			responseEntity =  restHandler.httpCall(EamEtlUtils.getUri(restEntity),
																		buildHeaders(restEntity, null),
																		String.class,
																		restEntity.getComponent(),
																		json,
																		null,
																		ExposureRequest.convertQueryParamsForHTTPCall(queryParams),
																		true,
																		fromOperation(fromHTTPMethodName(restEntity.getMethod())));
			
			log.info(" - response body: {}", Assorted.notNull(responseEntity, responseEntity.getBody()) 
																? responseEntity.getBody() 
																: null);
			log.trace("  - OdL to CAVI - sent {} HTTP call [{}] response [{}]", restEntity, 
																				restEntity.getUri(), 
																				responseEntity);
			
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(responseEntity.getBody());
			
			Gson gson	= new GsonBuilder().setExclusionStrategies(new GsonExclusionStrategy())
					   .registerTypeHierarchyAdapter(OffsetDateTime.class, 
					                                 new OffsetDateTimeSerDes.Serializer())
					   .registerTypeHierarchyAdapter(OffsetDateTime.class, 
					                                 new OffsetDateTimeSerDes.Deserializer())
					   .excludeFieldsWithModifiers(Modifier.STATIC)
					   .create();

			InvioOdlToCaviResponse[] caviResponse;

			if (element.isJsonArray()) {
			    caviResponse = gson.fromJson(element, InvioOdlToCaviResponse[].class);
			    
			    result.setEsito(caviResponse[Values.FIRST_ELEMENT].getEsito());
			    result.setCodiceErrore(caviResponse[Values.FIRST_ELEMENT].getCodiceErrore());
			    result.setMessaggioErrore(caviResponse[Values.FIRST_ELEMENT].getMessaggioErrore());
			    result.setWonum(caviResponse[Values.FIRST_ELEMENT].getWonum());
			    
			    result.setHttpCode(caviResponse[Values.FIRST_ELEMENT].getHttpCode());
			    
			} else if (element.isJsonObject()) {
			    log.error("  - OdL to CAVI - Expected array but got object: {}", element.toString());
			    
			    result.setHttpCode(HttpStatus.BAD_GATEWAY.value()); 
			    result.setEsito("KO");
			} else {
			    log.error("  - OdL to CAVI - Unexpected JSON format: {}", element.toString());
			    
			    result.setEsito("KO");
			    result.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			}
			
			
			log.trace("  - OdL to CAVI - sent {} HTTP call [{}] response [{}]", integrationEntity.getEntityKey(), 
																								restEntity.getUri(),
																								result);
		}
		catch ( Exception ex )
		{
			// log occurred error
			log.error("  - OdL to CAVI - Error occurred during EAM-INTEGRATION {} HTTP call [{}]", 
																	integrationEntity.getEntityKey(), 
																	restEntity.getUri(),
																	ex);
		
			result = new InvioOdlToCaviResponse();
			result.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			
		} 
		finally 
		{
			result.setIntegrationEntity(integrationEntity);
			result.setRequest(request);
			//in the response must be set the value retrieved from the request, because it can be the MT/null case
			
			log.info("  - OdL to CAVI - retrieve {} >> [{}] HTTP call [{}] took [{}] milliseconds", 
										integrationEntity.getEntityKey(),
										String.format("%s-%s", request.getWonum(),
															   request.getTrunkCode()),
								restEntity.getUri(), 
				          		System.currentTimeMillis() - startTime);
			
		}
		return result;
	}
}
