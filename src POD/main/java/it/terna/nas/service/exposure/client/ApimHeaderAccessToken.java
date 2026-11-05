package it.terna.nas.service.exposure.client;

import static it.terna.nas.service.material.utility.Constants.AUTHORIZATION;
import static it.terna.nas.service.material.utility.Constants.BEARER;
import static it.terna.nas.service.material.utility.Constants.HEADER_HOST;
import static it.terna.nas.service.material.utility.Constants.HEADER_X_API_KEY;
import static it.terna.nas.service.material.utility.Constants.SOAPACTION;

import java.io.IOException;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpComponentsConnection;
import org.springframework.ws.transport.http.HttpUrlConnection;

import it.terna.kernel.basement.utility.Assorted;

public class ApimHeaderAccessToken implements WebServiceMessageCallback
{
	private String accessToken;
	private String apikey;
	private String soapAction;
	private String host;
	
	public ApimHeaderAccessToken(String accessToken, String apikey, String host, String soapAction)
	{
		this.accessToken = accessToken;
		this.apikey		 = apikey;
		this.host        = host;
		this.soapAction  = soapAction;
		
		if (Assorted.isEmpty(accessToken))
			throw new IllegalArgumentException("Field AccessToken mandatory");

		if (Assorted.isEmpty(apikey))
			throw new IllegalArgumentException("Field ApiKey mandatory");
	}

	@Override
	public void doWithMessage(WebServiceMessage message) throws IOException
	{
		TransportContext context = TransportContextHolder.getTransportContext();
		Object contextConnection = context.getConnection();
		if (contextConnection instanceof HttpComponentsConnection)
		{
			HttpComponentsConnection connection = (HttpComponentsConnection) contextConnection;
			connection.addRequestHeader(AUTHORIZATION, BEARER + accessToken);
			connection.addRequestHeader(HEADER_X_API_KEY, apikey);
			
			if (Assorted.isNotEmpty(host))
				connection.addRequestHeader(HEADER_HOST, host);

			if (Assorted.isNotEmpty(soapAction))
				connection.addRequestHeader(SOAPACTION, soapAction);

		} else {
			HttpUrlConnection connection = (HttpUrlConnection) contextConnection;
			connection.addRequestHeader(AUTHORIZATION, BEARER + accessToken);
			connection.addRequestHeader(HEADER_X_API_KEY, apikey);
			
			if (Assorted.isNotEmpty(host))
				connection.addRequestHeader(HEADER_HOST, host);

			if (Assorted.isNotEmpty(soapAction))
				connection.addRequestHeader(SOAPACTION, soapAction);
		}
		
	}
}
