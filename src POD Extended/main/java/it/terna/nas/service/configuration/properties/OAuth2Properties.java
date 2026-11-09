package it.terna.nas.service.configuration.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OAuth2Properties {

	@Value("${http.settings.azureUri}")
	private String		azureUri;

	@Value("${http.settings.tokenEndpoint}")
	private String		tokenEndpoint;

	@Value("${http.settings.authGrantType}")
	private String		authGrantType;

	@Value("${eam-client-id:null}")
	private String		clientId;

	@Value("${eam-client-secret:null}")
	private String		clientSecret;

	@Value("${http.settings.scope}")
	private String		scope;

	@Value("${http.settings.clientCredential}")
	private String		clientCredential;

	public String getAzureUri() {
		return azureUri;
	}

	public void setAzureUri(String azureUri) {
		this.azureUri = azureUri;
	}

	public String getTokenEndpoint() {
		return tokenEndpoint;
	}

	public void setTokenEndpoint(String tokenEndpoint) {
		this.tokenEndpoint = tokenEndpoint;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public String getClientCredential() {
		return clientCredential;
	}

	public void setClientCredential(String clientCredential) {
		this.clientCredential = clientCredential;
	}

	public String getAuthGrantType() {
		return authGrantType;
	}

	public void setAuthGrantType(String authGrantType) {
		this.authGrantType = authGrantType;
	}
}
