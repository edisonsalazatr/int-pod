package it.terna.nas.service.configuration;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.Servlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition;
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition;

@EnableWs
@Configuration
public class WebServiceConfig
{
	@Bean
	public ServletRegistrationBean<Servlet> messageDispatcherServlet(ApplicationContext applicationContext)
	{
		MessageDispatcherServlet servlet = new MessageDispatcherServlet();
		servlet.setApplicationContext(applicationContext);
		servlet.setTransformWsdlLocations(true);
		
		return new ServletRegistrationBean<>(servlet, "/eam/ws/*");
	}

	@Bean(name = "notifyChiusuraOdl")
	public Wsdl11Definition notifyChiusuraOdlWsDefinition()
	{
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource("/soap/EAM/geocall/WSDL/WS_GEOCALL_ODL_Notify_4.0.wsdl"));

		return wsdl11Definition;
	}
	
	
	@Bean(name = "ricezioneImpianti")
	public Wsdl11Definition ricezioneImpiantiWsDefinition()
	{
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource("/soap/EAM/geocall/WSDL/WS_GEOCALL_RicezioneImpianti_2.0.wsdl"));

		return wsdl11Definition;
	}
	
	
	@Bean
	@DependsOn (value = {"messageDispatcherServlet"})
	public SimpleUrlHandlerMapping simpleUrlHandlerMapping()
	{
		SimpleUrlHandlerMapping simpleUrlHandler = new SimpleUrlHandlerMapping();
		Map<String, Object> urlMap = new HashMap<>();
		urlMap.put("/oauth2/notifyChiusuraOdl.wsdl", notifyChiusuraOdlWsDefinition());
		urlMap.put("/oauth2/ricezioneImpianti.wsdl", ricezioneImpiantiWsDefinition());
		urlMap.put("/oauth2/changeStatusOdl.wsdl", changeStatusOdlWsDefinition());
		urlMap.put("/oauth2/richiestaConsuntivazioneCallback.wsdl", odlConsuntivazioneCallbackWsDefinition());
		urlMap.put("/oauth2/notificaIndisponibilita.wsdl", odlAgileIndisponibilitaWsDefinition());
		return simpleUrlHandler;
	}
	
	@Bean(name = "changeStatusOdl")
	public Wsdl11Definition changeStatusOdlWsDefinition()
	{
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource("/soap/EAM/geocall/WSDL/WS_GEOCALL_ODL_Status_1.0.wsdl"));

		return wsdl11Definition;
	}

	@Bean(name = "odlConsuntivazioneCallback")
	public Wsdl11Definition odlConsuntivazioneCallbackWsDefinition() {
		SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
		wsdl11Definition.setWsdl(new ClassPathResource("/soap/SAP/WS_EAM_ODL_Consuntivazione_Callback_1.0.wsdl"));
		return wsdl11Definition;
	}

	@Bean(name = "notificaIndisponibilita")
    public Wsdl11Definition odlAgileIndisponibilitaWsDefinition() {
        SimpleWsdl11Definition wsdl11Definition = new SimpleWsdl11Definition();
        wsdl11Definition.setWsdl(new ClassPathResource("/soap/EAM/agile/notificaIndisponibilita.wsdl"));
        return wsdl11Definition;
    }
}
