package it.terna.nas.service.exposure.handler.rest;

import static it.terna.kernel.basement.utility.Common.EHTTPMethod.fromHTTPMethodName;
import static it.terna.kernel.basement.utility.Common.EHTTPMethod.fromOperation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.configuration.properties.OAuth2Properties;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.ws.maximo.GenericEsitoResponse;
import it.terna.nas.service.exposure.ws.maximo.JwtTokenResponse;
import it.terna.nas.service.exposure.ws.snow.SnowModifyAssetRequest;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ServiceNowRestClient {
	
	@Value("${http.oauth2.servicenow.api-key-servicenow}")
	private String					apikey;
	
	@Autowired 
	private OAuth2Properties 		oauth2Snow;
	
	@Autowired
	private RestHandler				restHandler;
	
	@Autowired
	private JWTAwareRestClient      jwtAwareRestClient;
	
	@Autowired
	private EamEtlUtils			    eamEtlUtils;

	
	public GenericEsitoResponse sendModifyAssetSnow(SnowModifyAssetRequest request) throws EAMException {
		
		GenericEsitoResponse response = new GenericEsitoResponse();
		
		String transactionId = UUID.randomUUID().toString();
		MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
		
		long startTime = System.currentTimeMillis(); 
		
		EDataIntegrationEntity integrationEntity = EDataIntegrationEntity.SERVICENOW_MODIFICA_IMPIANTO;
		// get information about endpoint to call
		RestEntityInfo 			    restEntity = restHandler.getRestEntity(integrationEntity.getEntityKey());
		
		try
		{
			log.debug("  - sendModifyAssetSnow - retrieving from {} HTTP call [{}]", integrationEntity.getEntityKey(),
			          														 		 restEntity.getUri());
			log.info(" - sendModifyAssetSnow - request body: {}", request.toJson());
			
			// prepare queryParams
     	 	Map<String, String> queryParams = new HashMap<>();
			
			// Calling external API using HTTP library
			ResponseEntity<GenericEsitoResponse> responseEntity =  restHandler.httpCall(EamEtlUtils.getUri(restEntity),
																   eamEtlUtils.addHostHeader(buildHeaders()),
																   GenericEsitoResponse.class,
																   restEntity.getComponent(),
																   request.toJson(),
																   null,
																   ExposureRequest.convertQueryParamsForHTTPCall(queryParams),
																   true,
																   fromOperation(fromHTTPMethodName(restEntity.getMethod())));
			
			log.info(" - response body: {}", Assorted.notNull(responseEntity, responseEntity.getBody()) 
															  ? responseEntity.getBody() 
															  : null);
			log.trace("  - sendModifyAssetSnow - sent {} HTTP call [{}] response [{}]", restEntity, 
																						restEntity.getUri(), 
																						responseEntity);
			if(Assorted.notNull(responseEntity.getBody())) 
				response = responseEntity.getBody();
			
			if(Assorted.notNull(responseEntity)) 
			{
				response.setHttpCode(responseEntity.getStatusCodeValue());
			}
			else {
				throw new EAMException(EAMErrorMapping.EAM_0000.getEamCode(), EAMErrorMapping.EAM_0000.getErrorDescription() + " Not able to get Response Data.");
			}
			
			log.trace("  - sendModifyAssetSnow - sent {} HTTP call [{}] response [{}]", integrationEntity.getEntityKey(), 
																						restEntity.getUri(),
																					 	response);
		}
		catch ( Exception ex )
		{
			// log occurred error
			log.error("  - sendModifyAssetSnow - Error occurred during EAM-INTEGRATION-EXTENDED {} HTTP call [{}]", integrationEntity.getEntityKey(), 
																													restEntity.getUri(),
																													ex);
			response = new GenericEsitoResponse();
			response.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			throw new EAMException(EAMErrorMapping.EAM_0000.getEamCode(), EAMErrorMapping.EAM_0000.getErrorDescription() + " Not able to get Response Data.");
		} 
		finally 
		{
			//in the response must be set the value retrieved from the request, because it can be the MT/null case
			log.info("  - sendModifyAssetSnow - retrieve {} >> [{}] HTTP call [{}] took [{}] milliseconds", integrationEntity.getEntityKey(),
																											restEntity.getUri(), 
																											System.currentTimeMillis() - startTime);
			MDC.clear();
		}
		return response;
	}
	
	private HttpHeaders buildHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.add(Constants.HEADER_X_API_KEY, apikey);
		JwtTokenResponse tokenResponse = jwtAwareRestClient.retrieveJwtToken(oauth2Snow.getAzureUri(),
																			 oauth2Snow.getTokenEndpoint(),
																			 oauth2Snow.getClientId(),
																			 oauth2Snow.getClientSecret(),
																			 oauth2Snow.getClientCredential(),
																			 oauth2Snow.getScope());
		if(tokenResponse != null) {
			log.debug("OAuth2 Token Generated : {}", tokenResponse.getAccessToken());
			headers.setBearerAuth(tokenResponse.getAccessToken());
		} else {
			log.error("  - Error retrieving jwt token for auth");
		}
		
		return headers;
	}

}
