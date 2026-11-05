package it.terna.nas.service.exposure.controller;

import static it.terna.kernel.basement.utility.Common.TRANSACTION_ID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import it.terna.kernel.aspect.exposure.annotation.Exposure;
import it.terna.kernel.service.exposure.VoidResponse;
import it.terna.kernel.service.outcome.OutcomeEntity;
import it.terna.nas.service.exposure.handler.MicrocontrolliHandler;
import it.terna.nas.service.exposure.handler.RicalcoloPolicyAssetHandle;
import it.terna.nas.service.material.dto.GenericEsitoResponse;
import it.terna.nas.service.material.utility.Catalog.Esito;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/eam/dss-manager")
@Slf4j
public class DssManagerController 
{
	@Autowired
	private MicrocontrolliHandler microcontrolliHandler;

    @Autowired
    private RicalcoloPolicyAssetHandle ricalcoloPolicyAssetHandle;
	
	@Exposure(apiDomain	= "/eam/dss-manager/start/mc", apiMethod = "POST")
    @PostMapping(value = "/start/mc", produces = { MediaType.APPLICATION_JSON_VALUE})
	public OutcomeEntity<VoidResponse> startBatchMicrocontrolli(@RequestHeader(value = TRANSACTION_ID, required = false) String nasTransactionId) 
	{
		
		log.info(" - Requested start import microcontrolli from X4_MICROCONTROLLI_MIG_BATCH");
		microcontrolliHandler.startBatch();
		log.info(" - Request sent to kafka topic");
		
		VoidResponse response = new VoidResponse();
		response.setHttpCode(HttpStatus.OK.value());
		
		// returning created object(s) response
        return new OutcomeEntity<>(response,
                                   response.getResponseHeaders(),
                                   HttpStatus.valueOf(response.getHttpCode()));
	}

    @Exposure(apiDomain	= "/eam/dss-manager/ricalcoloasset", apiMethod = "POST")
    @GetMapping(value = "/ricalcoloasset", produces = { MediaType.APPLICATION_JSON_VALUE})
    public OutcomeEntity<GenericEsitoResponse> ricalcoloAsset(@RequestHeader(value = TRANSACTION_ID, required = false) String nasTransactionId) 
    {
    	
    	log.info(" - Requested start ricalcolo policy/asset from X4RICALCOLO_POLICY_ASSET");
    	ricalcoloPolicyAssetHandle.elabora();
    	
    	GenericEsitoResponse response = new GenericEsitoResponse();
    	response.setHttpCode(HttpStatus.OK.value());
    	response.setEsito(Esito.OK.name());
    	 
    	// returning created object(s) response
        return new OutcomeEntity<>(response,
                                   response.getResponseHeaders(),
                                   HttpStatus.valueOf(response.getHttpCode()));
    }
}