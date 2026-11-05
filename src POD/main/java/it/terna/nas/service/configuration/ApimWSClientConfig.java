package it.terna.nas.service.configuration;

import java.io.File;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;
import org.springframework.ws.transport.http.HttpComponentsMessageSender.RemoveSoapHeadersInterceptor;

import it.terna.kernel.soap.clientgen.agile.richiestaindisponibilita.RichiestaAddOrdineLavoro;
import it.terna.kernel.soap.clientgen.agile.richiestaindisponibilita.RichiestaUpdateOrdineLavoro;
import it.terna.kernel.soap.clientgen.odl.inviotipoimpianto.DatiTipoImpiantoRequest;
import it.terna.kernel.soap.clientgen.odl.storno.StornoRequest;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ApimWSClientConfig
{
	private static final String[]	PACKAGES_SCAN	= {
													   RichiestaAddOrdineLavoro.class.getPackageName(),
													   RichiestaUpdateOrdineLavoro.class.getPackageName(),
													   DatiTipoImpiantoRequest.class.getPackageName(),
													   StornoRequest.class.getPackageName()
													  };

	@Value("${http.certificateCaEam.truststoreLocation:null}")
	private String					trustStoreLocation;

	@Value("${http.certificateCaEam.truststorePassword:null}")
	private String					trustStorePassword;

	@Value("${http.certificateCaEam.enable:false}")
	private Boolean					enable;

	
	private Jaxb2Marshaller jaxb2Marshaller()
	{
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setPackagesToScan(PACKAGES_SCAN);
		return jaxb2Marshaller;
	}

	@Bean
	public WebServiceTemplate apimWsTemplate()
	{
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		try {
			Jaxb2Marshaller jaxb2Marshaller = jaxb2Marshaller();
			webServiceTemplate.setMarshaller(jaxb2Marshaller);
			webServiceTemplate.setUnmarshaller(jaxb2Marshaller);
			if (Boolean.TRUE.equals(enable))
				webServiceTemplate.setMessageSender(httpComponentsMessageSender());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return webServiceTemplate;
	}

	
	@Bean
	public HttpComponentsMessageSender httpComponentsMessageSender() throws Exception
	{
		HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
		httpComponentsMessageSender.setHttpClient(httpClient());
		return httpComponentsMessageSender;
	}

	
	public HttpClient httpClient() throws Exception
	{
		return HttpClientBuilder.create().setSSLSocketFactory(sslConnectionSocketFactory())
			   .addInterceptorFirst(new RemoveSoapHeadersInterceptor()).build();
	}

	
	public SSLConnectionSocketFactory sslConnectionSocketFactory() throws Exception
	{
		return new SSLConnectionSocketFactory(sslContext(), NoopHostnameVerifier.INSTANCE);
	}

	
	public SSLContext sslContext() throws Exception
	{
		return SSLContextBuilder.create().loadTrustMaterial(new File(trustStoreLocation), trustStorePassword.toCharArray()).build();
	}
}