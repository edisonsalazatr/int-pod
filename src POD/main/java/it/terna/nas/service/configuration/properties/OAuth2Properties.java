package it.terna.nas.service.configuration.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class OAuth2Properties {

	@Value("${http.settings.azureUri}")
	private String		azureUri;

	@Value("${http.settings.tokenEndpoint}")
	private String		tokenEndpoint;

	@Value("${http.settings.authGrantType}")
	private String		authGrantType;

	@Value("${eam-client-id:null}")
	private String		clientId;

    @Value("${eam-client-id-osb:null}")
    private String		clientIdOSB;

	@Value("${eam-client-secret:null}")
	private String		clientSecret;

    @Value("${eam-client-secret-osb:null}")
    private String		clientSecretOSB;

	@Value("${http.settings.scope}")
	private String		scope;

    @Value("${http.settings.scopeOSB}")
    private String		scopeOSB;

	@Value("${http.settings.clientCredential}")
	private String		clientCredential;

}
