package it.terna.nas.service.exposure.controller;

import static it.terna.kernel.basement.utility.Common.TRANSACTION_ID;
import static it.terna.kernel.basement.utility.Common.USER_ID;
import static it.terna.kernel.basement.utility.Common.USER_ROLES;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.terna.kernel.aspect.exposure.annotation.Exposure;
import it.terna.kernel.service.outcome.OutcomeEntity;
import it.terna.nas.service.exposure.handler.TagetikHandler;
import it.terna.nas.service.exposure.ws.elive.InvioProvvedimentiResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/eam/tagetik")
@Slf4j
public class TagetikController 
{		
    @Autowired
    private TagetikHandler tagetikHandler;
	
    @Exposure(apiDomain	= "/eam/tagetik/costsRetrieval/{data}", apiMethod = "POST")
    @PostMapping(value = "/costsRetrieval/{data}", produces = { MediaType.APPLICATION_JSON_VALUE})
    public OutcomeEntity<InvioProvvedimentiResponse> costsRetrievalTagetik(@RequestHeader(value = TRANSACTION_ID, required = false) String nasTransactionId,
						                                                   @RequestHeader(value = USER_ID,        required = false) String nasUserId,
						                                                   @RequestHeader(value = USER_ROLES,     required = false) String nasUserRoles,
						                                                   @PathVariable("data") String data) 
	{
    	InvioProvvedimentiResponse response = null;
		log.info(" - Recupero Costi Esterni - START ");
		response = tagetikHandler.costsRetrievalTagetik(data);
        log.info( "  - EAM-INTEGRATION - Recupero Costi Esterni - Response (preview) comes from <Source> and its HTTP status code is [{}]", response.getHttpCode());
		return new OutcomeEntity<InvioProvvedimentiResponse>(response, 
											   response.getResponseHeaders(),
											   HttpStatus.valueOf(response.getHttpCode()));
    }
}