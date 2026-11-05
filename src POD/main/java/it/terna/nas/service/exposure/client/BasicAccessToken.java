package it.terna.nas.service.exposure.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import javax.xml.soap.MimeHeaders;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;
import it.terna.kernel.basement.utility.Assorted;

public class BasicAccessToken implements WebServiceMessageCallback
{
    private String userName;
    private String password;

    public BasicAccessToken(String userName, String password)
    {
        this.userName = userName;
        this.password = password;

        if (Assorted.isEmpty(userName) || Assorted.isEmpty(password))
            throw new IllegalArgumentException("Field userName/password mandatory");

    }


    @Override
    public void doWithMessage(WebServiceMessage message) throws IOException
    {
        String auth = userName + ":" + password;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
        SaajSoapMessage soapMessage = (SaajSoapMessage) message;
        MimeHeaders mimeHeader = soapMessage.getSaajMessage().getMimeHeaders();
        mimeHeader.setHeader("Authorization", "Basic " + encodedAuth);
    }

    @Override
    public String toString() {
        return "BasicAccessToken{" +
                "userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
