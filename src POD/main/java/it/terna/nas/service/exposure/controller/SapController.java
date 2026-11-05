package it.terna.nas.service.exposure.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.service.exposure.VoidResponse;
import it.terna.kernel.service.outcome.OutcomeEntity;
import it.terna.nas.service.dal.service.sap.AsyncSapService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static it.terna.kernel.basement.utility.Common.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/eam/sap")
@Slf4j
public class SapController {

    @Autowired
    private AsyncSapService asyncSapService;

    @GetMapping(value = "/method", produces = {MediaType.APPLICATION_JSON_VALUE})
    public OutcomeEntity<VoidResponse> sap(@RequestHeader(value = TRANSACTION_ID, required = false) String nasTransactionId,
                                           @RequestHeader(value = USER_ID, required = false) String nasUserId,
                                           @RequestHeader(value = USER_ROLES, required = false) String nasUserRoles,
                                           @RequestParam(value = "method", required = true) String method,
                                           @RequestParam(value = "loaded", required = false) String loaded,
                                           @RequestBody(required = false) String body) throws JsonProcessingException, RestException {

        VoidResponse response = new VoidResponse();
        response.fillHeaders(nasTransactionId, nasUserId, nasUserRoles);

        asyncSapService.runSAP(method, loaded, body);
        response.setHttpCode(HttpStatus.ACCEPTED.value());
        log.info("  - Method [{}] started asynchronously", method);

        return new OutcomeEntity<>(response, response.getResponseHeaders(),
                HttpStatus.valueOf(response.getHttpCode()));
    }

}
