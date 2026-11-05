package it.terna.nas.service.dal.service;

import static it.terna.kernel.basement.utility.Common.EHTTPMethod.fromHTTPMethodName;
import static it.terna.kernel.basement.utility.Common.EHTTPMethod.fromOperation;
import static it.terna.nas.service.material.utility.Constants.API_KEY;
import static it.terna.nas.service.material.utility.Constants.PARAM_LEAN;
import static it.terna.nas.service.material.utility.Constants.PARAM_VALUE_LEAN;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.basement.payload.ErrorContainer;
import it.terna.kernel.basement.payload.InfoContainer;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.ExposureRequest;
import it.terna.nas.service.exposure.ws.maximo.QueryApiResponse;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class QueryApiService 
{
	@Autowired
	private RestHandler restHandler;

	@Autowired
	private ApiKey apiKey;
	
	
	public QueryApiResponse invokeScript(EDataIntegrationEntity uri, Map<String, String> param) 
	{
		QueryApiResponse scriptResponse = new QueryApiResponse();
		RestEntityInfo restEntity 		= restHandler.getRestEntity(uri.getEntityKey());
		long startTime 					= System.currentTimeMillis();
		Map<String, String> queryParams = new HashMap<>();
		ResponseEntity<QueryApiResponse> responseEntity = null;

		try {

			String apiKeyValue = apiKey.retrieveSplittedApiKey();
			queryParams.put(PARAM_LEAN, PARAM_VALUE_LEAN);
			queryParams.put(API_KEY, apiKeyValue);
            
			if (Assorted.isNotEmpty(param))
				queryParams.putAll(param);
			
			scriptResponse.setApikey(apiKeyValue);

            log.debug("buildQueryParams: {}", queryParams);

					// Calling external API using HTTP library
            responseEntity =  restHandler.httpCall( restEntity.getUri(),
                    								null,
                    								QueryApiResponse.class,
                    								restEntity.getComponent(),
                    								null,
                    								null,
                    								ExposureRequest.convertQueryParamsForHTTPCall(queryParams),
                    								true,
                    								fromOperation(fromHTTPMethodName(restEntity.getMethod())));
			
			scriptResponse.setHttpCode(Assorted.notNull(responseEntity) ? responseEntity.getStatusCodeValue() : 400);
		} catch (RestException restException) {
			
			Throwable cause = EamEtlUtils.getCause(restException);
			if (cause instanceof ConnectException || cause instanceof InterruptedIOException) 
			{
				log.error(" - Retrieve Script Response Data - Error while waiting response of Maximo", cause);
				scriptResponse.setHttpCode(HttpStatus.SERVICE_UNAVAILABLE.value());
			}
			scriptResponse.setError(new ErrorContainer(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), restException.getMessage()));
		} catch (Exception ex) {
			log.error("  - Retrieve Script Response Data - Error occurred during HTTP call [{}]", restEntity.getUri(), ex);
			scriptResponse.setError(new ErrorContainer(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()), ex.getMessage()));
			scriptResponse.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
		} finally {
			
			if (Assorted.notNull(responseEntity)) 
			{
				if (scriptResponse.getHttpCode().intValue() != HttpStatus.OK.value()) 
				{
					if (Assorted.notNull(responseEntity.getBody())) 
					{
						scriptResponse.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
						String errorCode = Assorted.notNull(responseEntity.getBody().getErrorInfo()) && Assorted.notNull(responseEntity.getBody().getErrorInfo().getReasonCode()) ? responseEntity.getBody().getErrorInfo().getReasonCode() : String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value());
						scriptResponse.setError(new ErrorContainer(errorCode,
								" <Headers: [podname: "
										+ ApiKey.getPodName(responseEntity) + "]> - <Body: ["
										+ responseEntity.getBody()
										+ "]> - "));
					} else {
						scriptResponse.setInfo(new InfoContainer(" <Headers: [podname: "
								+ ApiKey.getPodName(responseEntity) + "]> - <Body: ["
								+ responseEntity.getBody() + "]> - "));
					}
					
				} else {
					if (Assorted.notNull(responseEntity.getBody())) 
					{
						scriptResponse.setMember(responseEntity.getBody().getMember());
						scriptResponse.setHref(responseEntity.getBody().getHref());
						scriptResponse.setResponseInfo(responseEntity.getBody().getResponseInfo());
					}
				}
				
			} else {
				
				scriptResponse.setInfo(new InfoContainer(" <Headers: [podname: "
						+ ApiKey.getPodName(responseEntity) + "]> - <Body: ["
						+ "###NULL###"
						+ "]> - "));
			}

			log.info("  - Retrieve Script Response Data - >> [{}] ms - Script apikey: {} - response: {}",
					System.currentTimeMillis() - startTime,
					scriptResponse.getApikey(),
					Assorted.notNull(scriptResponse) ? scriptResponse : null);

		}
		
		return scriptResponse;
	}
}