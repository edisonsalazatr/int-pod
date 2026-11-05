package it.terna.nas.service.exposure.controller;

import static it.terna.kernel.basement.utility.Common.TRANSACTION_ID;
import static it.terna.kernel.basement.utility.Common.USER_ID;
import static it.terna.kernel.basement.utility.Common.USER_ROLES;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import it.terna.kernel.aspect.exposure.annotation.Exposure;
import it.terna.kernel.service.exposure.ExposureResponse;
import it.terna.kernel.service.outcome.OutcomeEntity;
import it.terna.nas.service.exposure.handler.EliveHandler;
import it.terna.nas.service.exposure.ws.elive.InvioProvvedimentiRequest;
import it.terna.nas.service.exposure.ws.elive.InvioProvvedimentiResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/eam/elive")
@Slf4j
public class EliveController {
	
	@Autowired
	private EliveHandler eliveHandler;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setAllowedFields("nomeFile", "record");
	}

	@Exposure(apiDomain	= "/eam/elive/provvedimenti", apiMethod = "POST")
    @PostMapping(value = "/provvedimenti", produces = { MediaType.APPLICATION_JSON_VALUE})
    public OutcomeEntity<ExposureResponse> invioProvvedimentiPiante(@RequestHeader(value = TRANSACTION_ID, required = false) String nasTransactionId,
	                                                @RequestHeader(value = USER_ID,        required = false) String nasUserId,
	                                                @RequestHeader(value = USER_ROLES,     required = false) String nasUserRoles,
	                                                @RequestBody @Valid									 InvioProvvedimentiRequest request) {
		log.info("  - Invio Provvedimenti Piante - START");
		
		InvioProvvedimentiResponse response = null;
		request.fillHeaders(nasTransactionId, nasUserId, nasUserRoles);
		response = eliveHandler.invioProvvedimenti(request);

		
		return new OutcomeEntity<ExposureResponse>(response, 
				   response.getResponseHeaders(),
				   HttpStatus.valueOf(response.getHttpCode()));
		
	}

}
