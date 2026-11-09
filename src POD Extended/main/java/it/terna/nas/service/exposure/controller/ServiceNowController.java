package it.terna.nas.service.exposure.controller;

import static it.terna.kernel.basement.utility.Common.TRANSACTION_ID;
import static it.terna.kernel.basement.utility.Common.USER_ID;
import static it.terna.kernel.basement.utility.Common.USER_ROLES;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.terna.kernel.aspect.exposure.annotation.Exposure;
import it.terna.kernel.service.outcome.OutcomeEntity;
import it.terna.nas.service.exposure.handler.ServiceNowHandler;
import it.terna.nas.service.exposure.handler.X4SnowRilancioHandler;
import it.terna.nas.service.exposure.ws.maximo.GenericEsitoResponse;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/eam/snow")
@Slf4j
@RequiredArgsConstructor
public class ServiceNowController 
{		    
    private final ServiceNowHandler serviceNowHandler;

    private final X4SnowRilancioHandler x4SnowRilancioHandler;
	
    @Exposure(apiDomain	= "/eam/snow/modificaAnagraficaMassiva", apiMethod = "GET")
    @GetMapping(value = "/modificaAnagraficaMassiva", produces = { MediaType.APPLICATION_JSON_VALUE})
    public OutcomeEntity<GenericEsitoResponse> modificaAnagraficaMassiva(@RequestHeader(value = TRANSACTION_ID, required = false) String nasTransactionId,
                                               						 	 @RequestHeader(value = USER_ID,        required = false) String nasUserId,
                                               						 	 @RequestHeader(value = USER_ROLES,     required = false) String nasUserRoles) 
    {

    	GenericEsitoResponse response = new GenericEsitoResponse();
        response.fillHeaders(nasTransactionId,nasUserId,nasUserRoles);
		response.setEsito(StatusX4Esito.OK.name());
		response.setHttpCode(HttpStatus.OK.value());
		serviceNowHandler.modificaAnagraficaMassiva();
        // log HTTP status code
        log.info( "  - EAM Integration Extended - ServiceNowController - Modifica Anagrafica Massiva - Response (preview) comes from <Source> and its HTTP status code is [{}]", response.getHttpCode());
        // returning created object(s) response
        return new OutcomeEntity<GenericEsitoResponse>(response, response.getResponseHeaders(), HttpStatus.valueOf(response.getHttpCode()));
    }

    
    @Exposure(apiDomain	= "/eam/snow/recupero", apiMethod = "GET")
    @GetMapping(value = "/recupero", produces = { MediaType.APPLICATION_JSON_VALUE})
    public OutcomeEntity<GenericEsitoResponse> recupero(@RequestHeader(value = TRANSACTION_ID, required = false) String nasTransactionId,
                                               			 @RequestHeader(value = USER_ID,        required = false) String nasUserId,
                                               			 @RequestHeader(value = USER_ROLES,     required = false) String nasUserRoles) 
    {

    	GenericEsitoResponse response = new GenericEsitoResponse();
        response.fillHeaders(nasTransactionId,nasUserId,nasUserRoles);
		response.setEsito(StatusX4Esito.OK.name());
		response.setHttpCode(HttpStatus.OK.value());
		x4SnowRilancioHandler.recuperoSnow();
        // log HTTP status code
        log.info( "  - EAM Integration Extended - ServiceNowController - Recupero X4SNOW_RILANCIO - Response (preview) comes from <Source> and its HTTP status code is [{}]", response.getHttpCode());
        // returning created object(s) response
        return new OutcomeEntity<GenericEsitoResponse>(response, response.getResponseHeaders(), HttpStatus.valueOf(response.getHttpCode()));
    }
}