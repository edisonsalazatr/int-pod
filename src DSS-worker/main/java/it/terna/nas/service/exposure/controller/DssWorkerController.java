package it.terna.nas.service.exposure.controller;

import static it.terna.kernel.basement.utility.Common.TRANSACTION_ID;
import static it.terna.kernel.basement.utility.Common.USER_ID;
import static it.terna.kernel.basement.utility.Common.USER_ROLES;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import it.terna.kernel.aspect.exposure.annotation.Exposure;
import it.terna.kernel.service.exposure.VoidResponse;
import it.terna.kernel.service.outcome.OutcomeEntity;
import it.terna.nas.service.dal.entity.eam.Order;
import it.terna.nas.service.dal.entity.eam.OrderActions;
import it.terna.nas.service.dal.service.AssetDataService;
import it.terna.nas.service.dal.service.MicroControlService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.handler.OrderHandler;
import it.terna.nas.service.material.dto.EsitoElab;
import it.terna.nas.service.material.dto.ResetDto;
import it.terna.nas.service.material.dto.Task;
import it.terna.nas.service.material.dto.TaskResponse;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import lombok.extern.slf4j.Slf4j;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/eam/dss-worker")
@Slf4j
public class DssWorkerController {
	
	@Autowired
	private AssetDataService assetDataService;
	
	@Autowired
	private OrderHandler orderHandler;
	
	@Autowired
	private MicroControlService microControlService;
	
	@Exposure(apiDomain	= "/eam/dss-worker/test/{assetnum}", apiMethod = "POST")
    @PostMapping(value = "/test/{assetnum}", produces = { MediaType.APPLICATION_JSON_VALUE})
    public OutcomeEntity<VoidResponse> testcache(@RequestHeader(value = TRANSACTION_ID, required = false) String nasTransactionId,
                                                 @RequestHeader(value = USER_ID,        required = false) String nasUserId,
                                                 @RequestHeader(value = USER_ROLES,     required = false) String nasUserRoles,
                                                 @PathVariable("assetnum") 								  String assetNum) 
	{
		
		VoidResponse response = new VoidResponse();
		try {
			log.info(" - test creazione asset");
			assetDataService.getAssetDataByAssetNum(assetNum, false);
		} catch (EAMException e) {
		    log.error(e.getMessage(), e);
		}
		response.setHttpCode(HttpStatus.OK.value());
        
        // log HTTP status code
        log.info( "  - Test DSS-WORKER - Response (preview) comes from <Source> and its HTTP status code is [{}]", response.getHttpCode());
        
        // returning created object(s) response
		return new OutcomeEntity<VoidResponse>(response, 
											   response.getResponseHeaders(),
											   HttpStatus.valueOf(response.getHttpCode()));
    }
	
	@Exposure(apiDomain	= "/eam/dss-worker/test/insert", apiMethod = "POST")
    @PostMapping(value = "/test/insert", produces = { MediaType.APPLICATION_JSON_VALUE})
    public OutcomeEntity<VoidResponse> testInsert(@RequestHeader(value = TRANSACTION_ID, required = false) String nasTransactionId,
                                                 @RequestHeader(value = USER_ID,        required = false) String nasUserId,
                                                 @RequestHeader(value = USER_ROLES,     required = false) String nasUserRoles) 
	{
		
		VoidResponse response = new VoidResponse();
		log.info("start");
		Order order2 = new Order();
		order2.setWonum("35430");
		order2.setJpnum("AT.L.1.1.1.0");		
		OrderActions orders = new OrderActions();
		orders.setOrders(List.of(order2));
		
		Policy policy = new Policy();
		policy.setTipoPolicy(PolicyType.ON_CONDITION.getCode());
//		workorderApiService.insertAndUpdateWorkorderBulk(request);
		try {
			orderHandler.sendOrders(orders, "test", policy, new EsitoElab());
		} catch (EAMException e) {
		    log.error(e.getMessage(), e);
		}
		
			
		response.setHttpCode(HttpStatus.OK.value());
        
        // log HTTP status code
        log.info( "  - Test DSS-WORKER - Response (preview) comes from <Source> and its HTTP status code is [{}]", response.getHttpCode());
        
        // returning created object(s) response
		return new OutcomeEntity<VoidResponse>(response, 
											   response.getResponseHeaders(),
											   HttpStatus.valueOf(response.getHttpCode()));
    }
	
	@Exposure(apiDomain	= "/eam/dss-worker/test/mc", apiMethod = "POST")
    @PostMapping(value = "/test/mc", produces = { MediaType.APPLICATION_JSON_VALUE})
	public OutcomeEntity<TaskResponse> testMc(@RequestHeader(value = TRANSACTION_ID, required = false) String nasTransactionId,
                                              @RequestParam(required = false) String assetnum,
                                              @RequestParam(required = false) String jpnum) 
	{
		
		List<Task> tasks = microControlService.manageMicrocontrols(jpnum, assetnum);
		
		TaskResponse response = new TaskResponse();
		response.setHttpCode(HttpStatus.OK.value());
		response.setTasks(tasks);
		
		// log HTTP status code
		log.info("  - Test DSS-WORKER - Response (preview) comes from <Source> and its HTTP status code is [{}]", response.getHttpCode());
		
		// returning created object(s) response
		return new OutcomeEntity<>(response, response.getResponseHeaders(), HttpStatus.valueOf(response.getHttpCode()));
	}
	
	@Exposure(apiDomain	= "/eam/dss-worker/reset/mc", apiMethod = "POST")
    @PostMapping(value = "/reset/mc", produces = { MediaType.APPLICATION_JSON_VALUE})
	public OutcomeEntity<ResetDto> resetMicrocontrolli(@RequestHeader(value = TRANSACTION_ID, required = false) String nasTransactionId) 
	{
		
		log.info(" - Requested reset data created from import of microcontrolli");
		ResetDto resetDto = microControlService.resetImport();
		log.info(" - Rest done - {}", resetDto != null
										? resetDto.toJson()
										: null);
		
		// returning created object(s) response
		return new OutcomeEntity<>(resetDto, new HttpHeaders(), resetDto != null
				? HttpStatus.OK
				: HttpStatus.INTERNAL_SERVER_ERROR);
	}
}