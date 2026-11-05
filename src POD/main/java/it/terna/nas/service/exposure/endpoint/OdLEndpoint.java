package it.terna.nas.service.exposure.endpoint;

import java.util.UUID;

import javax.xml.bind.JAXBElement;

import it.terna.kernel.soap.clientgen.sap.consuntivazione.callback.RichiestaConsuntivazioneCallback;
import it.terna.kernel.soap.clientgen.sap.consuntivazione.callback.RichiestaConsuntivazioneCallbackResponse;
import it.terna.nas.service.exposure.handler.SapHandler;

import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.soap.clientgen.odl.changestatusodl.ChangeStatusResponseType;
import it.terna.kernel.soap.clientgen.odl.changestatusodl.ChangeStatusType;
import it.terna.kernel.soap.clientgen.odl.consuntivazione.ChiusuraOdlResponseType;
import it.terna.kernel.soap.clientgen.odl.consuntivazione.ChiusuraOdlType;
import it.terna.kernel.soap.clientgen.odl.consuntivazione.ObjectFactory;
import it.terna.kernel.soap.clientgen.odl.consuntivazione.SchedulazioneOdlResponseType;
import it.terna.kernel.soap.clientgen.odl.consuntivazione.SchedulazioneOdlType;
import it.terna.kernel.soap.clientgen.odl.geolocalizzazione.ModificaImpiantoResponse;
import it.terna.kernel.soap.clientgen.odl.geolocalizzazione.ModificaImpianto_Type;
import it.terna.kernel.soap.clientgen.odl.indisponibilita.NotificaIndisponibilitaRequest;
import it.terna.kernel.soap.clientgen.odl.indisponibilita.NotificaIndisponibilitaResponse;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.handler.AgileHandler;
import it.terna.nas.service.exposure.handler.GeoCallHandler;
import it.terna.nas.service.exposure.handler.SisnetHandler;
import lombok.extern.slf4j.Slf4j;

@Endpoint
@Slf4j
public class OdLEndpoint
{
	@Autowired
	private GeoCallHandler geoCallHandler;
	
	@Autowired
	private SisnetHandler sisnetHandler;

	@Autowired
	private AgileHandler agileHandler;

	@Autowired
	private SapHandler sapHandler;
	
	@PayloadRoot(namespace = "http://modificaimpianto.inbound.cxf.ternawfm.geocallapp.overit/", localPart = "modificaImpianto")
	@SoapAction(value = "")
	@ResponsePayload
	public JAXBElement<ModificaImpiantoResponse> modificaImpianto(@RequestPayload JAXBElement<ModificaImpianto_Type> request) 
			throws EAMException
	{
		String transactionId = UUID.randomUUID().toString();
		MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
		ModificaImpianto_Type modificaImpiantoType = request.getValue();
		long startTime = System.currentTimeMillis();
		log.info("call modificaImpianto({}) |{}|", modificaImpiantoType.getModificaImpiantoRequest().getCodiceOggetto(), startTime);
		ModificaImpiantoResponse response = sisnetHandler.saveRequestSisnet(modificaImpiantoType.getModificaImpiantoRequest());
		sisnetHandler.modificaImpianto(modificaImpiantoType.getModificaImpiantoRequest());
		it.terna.kernel.soap.clientgen.odl.geolocalizzazione.ObjectFactory factory = new it.terna.kernel.soap.clientgen.odl.geolocalizzazione.ObjectFactory();
		MDC.clear();
		return factory.createModificaImpiantoResponse(response);
	}


	@PayloadRoot(namespace = "http://soa.terna.it/GEOCALL/Odl_Notify/4.0", localPart = "notifyChiusuraOdl_Input")
	@SoapAction(value = "notifyChiusuraOdl/4.0")
	@ResponsePayload
	public JAXBElement<ChiusuraOdlResponseType> notifyChiusuraOdl(@RequestPayload JAXBElement<ChiusuraOdlType> request)
	{
		String transactionId = UUID.randomUUID().toString();
		MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
		ChiusuraOdlType chiusuraOdlType = request.getValue();
		long startTime = System.currentTimeMillis();
		log.info("call notifyChiusuraOdl({}) |{}|", chiusuraOdlType.getCODICEORDINEESTERNO(), startTime);
		ChiusuraOdlResponseType response = geoCallHandler.notifyChiusuraOdl(request);
		ObjectFactory factory = new ObjectFactory();
		MDC.clear();
		return factory.createNotifyChiusuraOdlOutput(response);
	}


	@PayloadRoot(namespace = "http://soa.terna.it/GEOCALL/Odl_Notify/4.0", localPart = "notifySchedulazioneOdl_Input")
	@SoapAction(value = "notifySchedulazioneOdl/4.0")
	@ResponsePayload
	public JAXBElement<SchedulazioneOdlResponseType> notifySchedulazioneOdl(@RequestPayload JAXBElement<SchedulazioneOdlType> request)
	{
		String transactionId = UUID.randomUUID().toString();
		MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
		SchedulazioneOdlType schedulazioneOdlType = request.getValue();
		long startTime = System.currentTimeMillis();
		log.info("call notifySchedulazioneOdl({}) |{}|", schedulazioneOdlType.getCODICEORDINEESTERNO(), startTime);
		SchedulazioneOdlResponseType response = geoCallHandler.scheduleOdl(request);
		ObjectFactory factory = new ObjectFactory();
		MDC.clear();
		return factory.createNotifySchedulazioneOdlOutput(response);
	}
	
	@PayloadRoot(namespace = "http://soa.terna.it/GEOCALL/Odl_Status/1.0", localPart = "changeStatusOdl_Input")
	@SoapAction(value = "changeStatusOdl/1.0")
	@ResponsePayload
	public JAXBElement<ChangeStatusResponseType> changeStatusOdl(@RequestPayload JAXBElement<ChangeStatusType> request)
	{
		String transactionId = UUID.randomUUID().toString();
		MDC.put(Common.MDC_TRANSACTION_ID, transactionId);
		ChangeStatusType changeStatusType = request.getValue();
		long startTime = System.currentTimeMillis();
		log.info("call changeStatusOdl({}) |{}|", changeStatusType.getCODICEPOSIZIONE(), startTime);
		ChangeStatusResponseType response = geoCallHandler.changeStatusOdl(request);
		it.terna.kernel.soap.clientgen.odl.changestatusodl.ObjectFactory factory = new it.terna.kernel.soap.clientgen.odl.changestatusodl.ObjectFactory();
		MDC.clear();
		return factory.createChangeStatusOdlOutput(response);
	}

	@PayloadRoot(namespace = "http://soa.terna.it/EAM/notificaIndisponibilita/1.0", localPart = "notificaIndisponibilitaRequest")
	@SoapAction(value = "NotificaIndisponibilita")
	@ResponsePayload
	public NotificaIndisponibilitaResponse notifyUnavailability(@RequestPayload NotificaIndisponibilitaRequest request) {
	    
		String transactionId = UUID.randomUUID().toString();
	    MDC.put(Common.MDC_TRANSACTION_ID, transactionId);

	    try {
	        
	    	NotificaIndisponibilitaRequest notificaIndisponibilitaRequest = request;
	        long startTime = System.currentTimeMillis();

	        log.info("notifyUnavailability started for codiceOdL={}", notificaIndisponibilitaRequest.getCodiceOdL());

	        NotificaIndisponibilitaResponse response = agileHandler.notifyUnavailability(notificaIndisponibilitaRequest);

	        long totalTime = System.currentTimeMillis() - startTime;
	        log.info("notifyUnavailability completed for codiceOdL={} in {} ms",
	                notificaIndisponibilitaRequest.getCodiceOdL(), totalTime);

	        return response;
	    
	    } finally {
	        MDC.clear();
	    }
	}

	@PayloadRoot(namespace = "http://soa.terna.it/EAM/ODL_Consuntivazione_Callback/1.0", localPart = "richiestaConsuntivazioneCallback")
	@ResponsePayload
	public JAXBElement<RichiestaConsuntivazioneCallbackResponse> richiestaConsuntivazioneCallback(@RequestPayload JAXBElement<RichiestaConsuntivazioneCallback> request)
	{
        long startTime = System.currentTimeMillis();
		log.debug("call richiestaConsuntivazioneCallback() |{}|", startTime);
		JAXBElement<RichiestaConsuntivazioneCallbackResponse> response = sapHandler.richiestaConsuntivazioneCallback(request, null);
		long endTime = System.currentTimeMillis();
		log.debug("call richiestaConsuntivazioneCallback() |{}|", endTime - startTime);
		return response;
	}

}
