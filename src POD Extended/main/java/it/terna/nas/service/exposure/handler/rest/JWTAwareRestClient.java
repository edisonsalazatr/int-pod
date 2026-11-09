package it.terna.nas.service.exposure.handler.rest;

import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_ID;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.CLIENT_SECRET;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.GRANT_TYPE;
import static org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames.SCOPE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import it.terna.kernel.basement.payload.ErrorContainer;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.http.client.RestHandlerProxy;
import it.terna.nas.service.exposure.ws.maximo.JwtTokenResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTAwareRestClient {
	
	@Autowired
	protected RestHandlerProxy restHandlerProxy;
	
	public JwtTokenResponse retrieveJwtToken(String azureUrl,
											 String tokenEndpoint,
											 String clientId,
											 String clientSecret,
											 String clientCredentials,
											 String scope)
	{
		Assert.hasText(azureUrl, 		  	"azureUrl not configured");
		Assert.hasText(tokenEndpoint, 	  	"tokenEndpoint not configured");
		Assert.hasText(clientId,		  	"clientId not configured");
		Assert.hasText(clientSecret, 	  	"clientSecret not configured");
		Assert.hasText(clientCredentials, 	"clientCredentials not configured");
		Assert.hasText(scope, 				"scope not configured");
		
		JwtTokenResponse finalResponse = new JwtTokenResponse();
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add(GRANT_TYPE, clientCredentials);
			body.add(CLIENT_ID, clientId);
			body.add(CLIENT_SECRET, clientSecret);
			body.add(SCOPE, scope);
			ResponseEntity<JwtTokenResponse> response = restHandlerProxy.httpCall(azureUrl + tokenEndpoint, headers, 
																				JwtTokenResponse.class, null, 
																				null, null, 
																				body.toSingleValueMap(), true, 
																				Common.EHTTPMethod.POST,
																				MediaType.MULTIPART_FORM_DATA.getType());
			finalResponse = response.getBody();
			finalResponse.setHttpCode(response.getStatusCodeValue());
		} catch (Exception ex) {
			log.error(ex.getMessage(), ex);
			// log occurred error
			finalResponse.setHttpCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			finalResponse.setError(new ErrorContainer(HttpStatus.INTERNAL_SERVER_ERROR.toString(), "Error"));
		} finally {
			log.info("Retrieve response {}", finalResponse);
		}
		return finalResponse;
	}
}
