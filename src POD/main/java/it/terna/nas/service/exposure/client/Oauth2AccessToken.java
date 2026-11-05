package it.terna.nas.service.exposure.client;

import it.terna.kernel.basement.utility.Assorted;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpComponentsConnection;
import org.springframework.ws.transport.http.HttpUrlConnection;

import java.io.IOException;

import static it.terna.nas.service.material.utility.Constants.*;
import static it.terna.nas.service.material.utility.Constants.SOAPACTION;

public class Oauth2AccessToken implements WebServiceMessageCallback {

    private String accessToken;
    private String soapAction;

    public Oauth2AccessToken(String accessToken, String soapAction)
    {
        this.accessToken = accessToken;
        this.soapAction  = soapAction;

        if (Assorted.isEmpty(accessToken))
            throw new IllegalArgumentException("Field AccessToken mandatory");

    }

    @Override
    public void doWithMessage(WebServiceMessage message) throws IOException {
        TransportContext context = TransportContextHolder.getTransportContext();
        Object contextConnection = context.getConnection();
        if (contextConnection instanceof HttpComponentsConnection)
        {
            HttpComponentsConnection connection = (HttpComponentsConnection) contextConnection;
            connection.addRequestHeader(AUTHORIZATION, BEARER + accessToken);

            if (Assorted.isNotEmpty(soapAction))
                connection.addRequestHeader(SOAPACTION, soapAction);

        } else {
            HttpUrlConnection connection = (HttpUrlConnection) contextConnection;
            connection.addRequestHeader(AUTHORIZATION, BEARER + accessToken);

            if (Assorted.isNotEmpty(soapAction))
                connection.addRequestHeader(SOAPACTION, soapAction);
        }
    }

    @Override
    public String toString() {
        return "Oauth2AccessToken{" +
                "accessToken='" + accessToken + '\'' +
                ", soapAction='" + soapAction + '\'' +
                '}';
    }

}
