package it.terna.nas.service.configuration;

import java.io.File;

import javax.net.ssl.SSLContext;

import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessageFactory;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import it.terna.kernel.soap.clientgen.sap.acquisition.dc.QueryWBSResponse;
import it.terna.kernel.soap.clientgen.sap.acquisition.ntw.ZacquisizioneNtwResponse;
import it.terna.kernel.soap.clientgen.sap.acquisition.odm.Zstpms4InputOrdDiMan;
import it.terna.kernel.soap.clientgen.sap.acquisition.oi.Zstpms4OutputOrdiniInterni;
import it.terna.kernel.soap.clientgen.sap.closure.ntw.ZchiusuraNtwResponse;
import it.terna.kernel.soap.clientgen.sap.closure.odm.ChiusuraODMResponse;
import it.terna.kernel.soap.clientgen.sap.consuntivazione.confirm.Zfmpms4EamConfirm;
import it.terna.kernel.soap.clientgen.sap.consuntivazione.controlloPeriodo.ZmosCheckPeriodContFi;
import it.terna.kernel.soap.clientgen.sap.storno.counter.ZmosWsResearchCounterOdm;
import it.terna.kernel.soap.clientgen.sap.storno.tot.ZmosWsReversalHoursOdmResponse;
import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class SapClientConfig {

	@Value("${http.basic.esb.username}")
	private String	username;

	@Value("${http.basic.esb.password}")
	private String	password;

	@Value("${http.certificateCaEam.truststoreLocation:null}")
	private String	trustStoreLocation;

	@Value("${http.certificateCaEam.truststorePassword:null}")
	private String	trustStorePassword;

	@Value("${http.certificateCaEam.enable:false}")
	private Boolean	enable;

	@Value("${http.certificateCaEam.enableOSB:false}")
	private Boolean	enableOsb;

	@Bean
	public WebServiceMessageFactory messageFactory11() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.setSoapVersion(SoapVersion.SOAP_11);
		return messageFactory;
	}

	@Bean
	public WebServiceMessageFactory messageFactory12() {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.setSoapVersion(SoapVersion.SOAP_12);
		return messageFactory;
	}

	@Bean
	public Jaxb2Marshaller marshallerOI() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(Zstpms4OutputOrdiniInterni.class.getPackageName());
		return marshaller;
	}

	@Bean
	public Jaxb2Marshaller marshallerODM() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(Zstpms4InputOrdDiMan.class.getPackageName());
		return marshaller;
	}

	@Bean
	public Jaxb2Marshaller marshallerStorno() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(ZmosWsReversalHoursOdmResponse.class.getPackageName());
		return marshaller;
	}

	@Bean
	public Jaxb2Marshaller marshallerCounterOdM() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(ZmosWsResearchCounterOdm.class.getPackageName());
		return marshaller;
	}

	@Bean
	public Jaxb2Marshaller marshallerCheckPeriodo() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(ZmosCheckPeriodContFi.class.getPackageName());
		return marshaller;
	}

	@Bean
	public Jaxb2Marshaller marshallerConsuntivazione() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(Zfmpms4EamConfirm.class.getPackageName());
		return marshaller;
	}
	
	@Bean
	public Jaxb2Marshaller marshallerDC() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(QueryWBSResponse.class.getPackageName());
		return marshaller;
	}

	@Bean
	public Jaxb2Marshaller marshallerNtk() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(ZacquisizioneNtwResponse.class.getPackageName());
		return marshaller;
	}

	@Bean
	public Jaxb2Marshaller marshallerCloseNtk() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(ZchiusuraNtwResponse.class.getPackageName());
		return marshaller;
	}
	
	@Bean
	public Jaxb2Marshaller marshallerCloseOdM() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan(ChiusuraODMResponse.class.getPackageName());
		return marshaller;
	}

	@Bean
	public WebServiceTemplate sapWSTemplateOI() {
		WebServiceTemplate template = new WebServiceTemplate();
		try {
			template.setMarshaller(marshallerOI());
			template.setUnmarshaller(marshallerOI());
			template.setMessageFactory(messageFactory11());
			template.setMessageSender(httpComponentsMessageSenderSAPOauth2());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return template;
	}

	@Bean
	public WebServiceTemplate sapWSTemplateODM() {
		WebServiceTemplate template = new WebServiceTemplate();
		try {
			template.setMarshaller(marshallerODM());
			template.setUnmarshaller(marshallerODM());
			template.setMessageFactory(messageFactory11());
			template.setMessageSender(httpComponentsMessageSenderSAPOauth2());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return template;
	}

	@Bean
	public WebServiceTemplate sapWSTemplateStorno() {
		WebServiceTemplate template = new WebServiceTemplate();
		try {
			template.setMarshaller(marshallerStorno());
			template.setUnmarshaller(marshallerStorno());
			template.setMessageFactory(messageFactory11());
			template.setMessageSender(httpComponentsMessageSenderSAP());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return template;
	}

	@Bean
	public WebServiceTemplate sapWSTemplateCounter() {
		WebServiceTemplate template = new WebServiceTemplate();
		try {
			template.setMarshaller(marshallerCounterOdM());
			template.setUnmarshaller(marshallerCounterOdM());
			template.setMessageFactory(messageFactory11());
			template.setMessageSender(httpComponentsMessageSenderSAP());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return template;
	}
	
	@Bean
	public WebServiceTemplate sapWSTemplateDC() {
		WebServiceTemplate template = new WebServiceTemplate();
		try {
			template.setMarshaller(marshallerDC());
			template.setUnmarshaller(marshallerDC());
			template.setMessageFactory(messageFactory11());
			template.setMessageSender(httpComponentsMessageSenderSAP());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return template;
	}

	@Bean
	public WebServiceTemplate sapWSTemplateCheckPeriodo() {
		WebServiceTemplate template = new WebServiceTemplate();
		try {
			template.setMarshaller(marshallerCheckPeriodo());
			template.setUnmarshaller(marshallerCheckPeriodo());
			template.setMessageFactory(messageFactory11());
			template.setMessageSender(httpComponentsMessageSenderSAP());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return template;
	}

	@Bean
	public WebServiceTemplate sapWSTemplateConsuntivazione() {
		WebServiceTemplate template = new WebServiceTemplate();
		try {
			template.setMarshaller(marshallerConsuntivazione());
			template.setUnmarshaller(marshallerConsuntivazione());
			template.setMessageFactory(messageFactory11());
			template.setMessageSender(httpComponentsMessageSenderSAP());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return template;
	}

	@Bean
	public WebServiceTemplate sapWSTemplateNtk() {
		WebServiceTemplate template = new WebServiceTemplate();
		try {
			template.setMarshaller(marshallerNtk());
			template.setUnmarshaller(marshallerNtk());
			template.setMessageFactory(messageFactory11());
			template.setMessageSender(httpComponentsMessageSenderSAP());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return template;
	}

	@Bean
	public WebServiceTemplate sapWSTemplateCloseNtk() {
		WebServiceTemplate template = new WebServiceTemplate();
		try {
			template.setMarshaller(marshallerCloseNtk());
			template.setUnmarshaller(marshallerCloseNtk());
			template.setMessageFactory(messageFactory11());
			template.setMessageSender(httpComponentsMessageSenderSAP());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return template;
	}
	
	@Bean
	public WebServiceTemplate sapWSTemplateCloseOdM() {
		WebServiceTemplate template = new WebServiceTemplate();
		try {
			template.setMarshaller(marshallerCloseOdM());
			template.setUnmarshaller(marshallerCloseOdM());
			template.setMessageFactory(messageFactory11());
			template.setMessageSender(httpComponentsMessageSenderSAP());
		}catch(Exception e) {
			log.error(e.getMessage(), e);
		}
		return template;
	}

	@Bean
	@DependsOn({ "usernamePasswordSAPCredentials" })
	public HttpComponentsMessageSender httpComponentsMessageSenderSAP() throws Exception
	{
		HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
		httpComponentsMessageSender.setCredentials(usernamePasswordSAPCredentials());
		if (Boolean.TRUE.equals(enable) && Boolean.TRUE.equals(enableOsb))
			httpComponentsMessageSender.setHttpClient(httpClient());

		return httpComponentsMessageSender;
	}

	@Bean
	public HttpComponentsMessageSender httpComponentsMessageSenderSAPOauth2() throws Exception
	{
		HttpComponentsMessageSender httpComponentsMessageSender = new HttpComponentsMessageSender();
		if (Boolean.TRUE.equals(enable))
			httpComponentsMessageSender.setHttpClient(httpClient());

		return httpComponentsMessageSender;
	}

	@Bean
	public UsernamePasswordCredentials usernamePasswordSAPCredentials()
	{
		return new UsernamePasswordCredentials(username, password);
	}

	public HttpClient httpClient() throws Exception
	{
		return HttpClientBuilder.create().setSSLSocketFactory(sslConnectionSocketFactory())
				.addInterceptorFirst(new HttpComponentsMessageSender.RemoveSoapHeadersInterceptor()).build();
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
