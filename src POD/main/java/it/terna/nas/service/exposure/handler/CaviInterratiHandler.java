package it.terna.nas.service.exposure.handler;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.pool.ExecutorConfigurator;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.X4EsitoInt;
import it.terna.nas.service.dal.service.AssetQueryService;
import it.terna.nas.service.dal.service.X4EsitoIntApiService;
import it.terna.nas.service.dal.service.X4EsitoIntQueryService;
import it.terna.nas.service.dal.service.X4WorkorderApiService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.events.WODeleteCAVI;
import it.terna.nas.service.exposure.events.WorkOrderCaviInterrati;
import it.terna.nas.service.exposure.handler.rest.CaviInterratiRestClient;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.exposure.ws.maximo.WorkOrderCaviResponse;
import it.terna.nas.service.material.dto.X4EsitoIntRequest;
import it.terna.nas.service.material.dto.cavi.InvioOdlDeletionToCaviRequest;
import it.terna.nas.service.material.dto.cavi.InvioOdlDeletionToCaviResponse;
import it.terna.nas.service.material.dto.cavi.InvioOdlToCaviRequest;
import it.terna.nas.service.material.dto.cavi.InvioOdlToCaviResponse;
import it.terna.nas.service.material.dto.cavi.OrderChild;
import it.terna.nas.service.material.utility.Catalog.CaviInterratiInterface;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CaviInterratiHandler extends AbstractHandler
{
	
	@Autowired
	private X4EsitoIntApiService x4EsitoIntApiService;
	
	@Autowired
	private X4EsitoIntQueryService x4EsitoIntQueryService;
	
	@Autowired
	CaviInterratiRestClient caviInterratiRestClient;
	
	@Autowired
	private AssetQueryService assetQueryService;
	
	@Autowired
	private X4WorkorderApiService x4WorkorderApiService;
	
	@Autowired
	private ExecutorConfigurator	executorConfigurator;
	
	
	public void sendDeletion(String topic, String uuidResend, String payload) 
	{
		Instant startDate	= Instant.now();
		log.info(" - CAVI INTERRATI DELETE ODL - start at : {}", startDate);
		X4EsitoIntRequest logRequest = null;
		InvioOdlDeletionToCaviResponse response = null;
		try {
			// log message received 
			log.debug("  - CAVI INTERRATI DELETE ODL - Handler - Message to process received from topic [{}]", topic);
			
			logRequest = createLogRequest(CaviInterratiInterface.SEND_ODL_DELETION_TO_CAVI_INTERFACE.getName(),
					Constants.EAM,
					Constants.CAVI_INTERRATI, 
					topic,
					payload,
					null,
					null,
					StatusX4Esito.OK.name(),
					uuidResend);

			// convert received payload
			WODeleteCAVI	event	= Assorted.fromJson(payload, 
														WODeleteCAVI.class, 
														false, 
														false);
			
		    	
	        if (Assorted.isNull(event) && (Assorted.isNull(event.getHref())))
	        	throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription());
	        
	        logRequest.setWonum(event.getWonum());
			InvioOdlDeletionToCaviRequest request = buildRequest(event);
	        log.info("CAVI INTERRATI DELETE ODL - wonum={}", event.getWonum());	      
	    	long startTime = System.currentTimeMillis();
			response = caviInterratiRestClient.sendDeletionToCavi(request);
	    	long endTime = System.currentTimeMillis();
			log.info("CAVI INTERRATI DELETE ODL - received the following object response: {}, duration: {} ms", response.toString(), endTime - startTime);
			if (StatusX4Esito.KO.name().equals(response.getEsito()) || Assorted.isNull(response.getEsito())) 
			{
				log.info("CAVI INTERRATI DELETE ODL - returned KO - message={}", response.getMessaggioErrore());
				if(Constants.INSPECTION_NOT_FOUND.equalsIgnoreCase(response.getMessaggioErrore()))
					logRequest.setRetry(null);
				throw new EAMException(response.getCodiceErrore(), response.getMessaggioErrore());
			}
				
			
		} 
		catch (EAMException eam) 
		{
			log.error("  - CAVI INTERRATI DELETE ODL - An error occurred while reading Message from topic [{}]",  topic);
			logRequest.setEsito(StatusX4Esito.KO.name());
			logRequest.setCodesito(Assorted.isNull(eam.getErrorCode()) ? EAMErrorMapping.EAM_0000.getEamCode() : eam.getErrorCode());
			logRequest.setDesc_esito(Assorted.isNull(eam.getMessage()) ? EAMErrorMapping.EAM_0000.getErrorDescription() : eam.getMessage());
		}
		catch (Exception e) 
		{
			log.error("  - CAVI DELETE ODL - An error occurred while reading Message from topic [{}]",  topic);
			logRequest.setEsito(StatusX4Esito.KO.name());
			logRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			logRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + e.getMessage());
		}
		 finally 
		 {
			 log.info("  - CAVI INTERRATI DELETE ODL - Creating logging request to X4ESITOINT");
			 logRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
			 try {
				x4EsitoIntApiService.sendEsitoInt(logRequest);
			 } catch(Exception e) {
					log.error("Rest Error when executing Maximo call to write X4ESITOINT result using this request: {}, occured exception {}", 
							  Assorted.notNull(logRequest)
							  ? logRequest.toString() 
							  : null,
							  e.getMessage());
			}
			log.info(" - end CAVI INTERRATI DELETE ODL at : {}", Instant.now());
		}
	}
	
	public void sendOdL(String topic, String uuidResend, String payload) {
		
		Instant startDate	= Instant.now();
		log.info(" - CAVI INTERRATI INVIO ODL - start at : {}", startDate);
		X4EsitoIntRequest logRequest = null;
		InvioOdlToCaviResponse response = null;
		try {
			// log message received 
			log.debug("  - CAVI INTERRATI INVIO ODL - Handler - Message to process received from topic [{}]", topic);
			
			logRequest = createLogRequest(CaviInterratiInterface.SEND_ODL_TO_CAVI_INTERFACE.getName(),
					Constants.EAM,
					Constants.CAVI_INTERRATI, 
					topic,
					payload,
					null,
					null,
					StatusX4Esito.OK.name(),
					uuidResend);

			// convert received payload
			WorkOrderCaviInterrati	event	= Assorted.fromJson(payload, 
																WorkOrderCaviInterrati.class, 
																false, 
																false);
			
	    	
	        if (Assorted.isNull(event) && (Assorted.isNull(event.getHref())))
	        	throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription());
			
	        logRequest.setWonum(event.getWonum());
	        logRequest.setAssetnum(event.getAssetnum());
	        
			InvioOdlToCaviRequest request = buildRequest(event);
			log.info(" - Calling CAVI INTERRATI INVIO ODL with the following body: {}", request.toJson());
	    	long startTime = System.currentTimeMillis();
			response = caviInterratiRestClient.sendOdlToCavi(request);
	    	long endTime = System.currentTimeMillis();
			log.info("CAVI INTERRATI INVIO ODL - received the following object response: {}, duration: {} ms", response.toString(), endTime - startTime);
			if (StatusX4Esito.KO.name().equals(response.getEsito()) || Assorted.isNull(response.getEsito())) {
				log.info("CAVI INTERRATI INVIO ODL - returned KO - message={}", response.getMessaggioErrore());
				throw new EAMException(response.getCodiceErrore(), response.getMessaggioErrore());
			}
		} 
		catch (EAMException eam) 
		{
			log.error("  - CAVI INTERRATI INVIO ODL - An error occurred while reading Message from topic [{}]",  topic);
			logRequest.setEsito(StatusX4Esito.KO.name());
			logRequest.setCodesito(Assorted.isNull(eam.getErrorCode()) ? EAMErrorMapping.EAM_0000.getEamCode() : eam.getErrorCode());
			logRequest.setDesc_esito(Assorted.isNull(eam.getMessage()) ? EAMErrorMapping.EAM_0000.getErrorDescription() : eam.getMessage());
		}
		catch (Exception e) 
		{
			log.error("  - CAVI INTERRATI INVIO ODL - An error occurred while reading Message from topic [{}]", topic);
			logRequest.setEsito(StatusX4Esito.KO.name());
			logRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			logRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + e.getMessage());
		}
		finally {
			 
			log.info("  - CAVI INTERRATI INVIO ODL - Creating logging request to X4ESITOINT");
			logRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
			try {
				x4EsitoIntApiService.sendEsitoInt(logRequest);
			} catch(java.lang.Exception e) {
					log.error("Rest Error when executing Maximo call to write X4ESITOINT result using this request: {}, occured exception {}", 
							  Assorted.notNull(logRequest)
							  ? logRequest.toString() 
							  : null,
							  e.getMessage());
			}
			Duration duration = Duration.between(startDate, Instant.now());
			log.info(" - end CAVI INTERRATI INVIO ODL at : {} took {}", duration.toMillis());
		}
	}
	
	/**
	 * Create and fill a request object to send odl to CAVI INTERRATI
	 * @param event
	 * @return
	 */
	private InvioOdlToCaviRequest buildRequest(WorkOrderCaviInterrati event) {
		InvioOdlToCaviRequest request = new InvioOdlToCaviRequest();
		
		Assert.notNull(event.getAssetnum(), "Assetnum is null");
		Assert.notNull(event.getSiteid(), "Siteid is null");
		
		Asset asset = assetQueryService.getAssetForCaviInterrati(event.getAssetnum(), event.getSiteid());
		if(asset != null) {
			Assert.notNull(asset.getX3Swerk(), "X3Swerk/AotCode is null");
			Assert.notNull(asset.getX3Ingrp(), "X3Ingrp/UiCode is null");
			Assert.notNull(asset.getDescription(), "Description/LineName is null");
			request.setAotCode(asset.getX3Swerk());
			request.setUiCode(asset.getX3Ingrp()); 
			request.setLineName(asset.getDescription());
		}else {
			log.error("Asset not found for assetnum = {}", event.getAssetnum());
		}
		
		Assert.notNull(event.getX4sapidasset(), "X4SapIdAsset/TrunkCode is null");
		Assert.notNull(event.getJpnum(), "Jpnum/CodiceAttivita is null");
		Assert.notNull(event.getTargstartdate(), "TargStartDate/Date is null");
		Assert.notNull(event.getWonum(), "Wonum is null");
		request.setLineCode(event.getAssetnum()); 
		request.setTrunkCode(event.getX4sapidasset());
		request.setPlanType("REGULAR");
		request.setCodiceAttivita(event.getJpnum());
		request.setDate(Assorted.notNull(event.getTargstartdate()) ? EamEtlUtils.convertToDateyyyyMMdd(event.getTargstartdate()) : null);
		request.setWonum(event.getWonum());
		
		if(event.getWorkorder() != null) {
			List<OrderChild> children = buildOrderChildren(event.getWorkorder()
					.stream()
					.map(wo -> EamEtlUtils.removeOslcFromHref(wo.getHref()))
					.toList());
			request.setListaOrdini(children);
		} else {
			request.setListaOrdini(new ArrayList<>());
			request.getListaOrdini().add(new OrderChild());
		}

		return request;
	}
	
	private List<OrderChild> buildOrderChildren(List<String> orderUris){
		
		List<CompletableFuture<MaximoResponse>> listFutureResponse = new ArrayList<>();
		
		orderUris.forEach(uri -> {
			listFutureResponse.add(retriveResourceAsync(uri, WorkOrderCaviResponse.class));
		});
		
		log.debug("  - Fetching data from EAM - executing and APIs invocation");
		
		// wait and collect all asynchronous future responses 
		List<MaximoResponse>	combinedResponses = listFutureResponse.stream()
																	.map(CompletableFuture::join)
																	.collect(Collectors.toList());
		
		log.debug("  - Fetching data from EAM - collected all responses, beginning trasformation");
		
		List<OrderChild> children = combinedResponses.stream()
													 .map(x -> WorkOrderCaviResponse.class.cast(x))
													 .map(x -> new OrderChild(x.getWonum(), x.getAssetnum()))
													 .collect(Collectors.toList());
		
		return children;
	}
	
	public CompletableFuture<MaximoResponse> retriveResourceAsync(String uri, Class<? extends MaximoResponse> clazz) {
		// if not managed inside CompletableFuture every exception will be wrapped by java.util.concurrent.CompletionException

			return CompletableFuture.supplyAsync(() -> 
										x4WorkorderApiService.retrieveResource(uri, clazz),
										executorConfigurator.getExecutorService());
	}	
	
	/**
	 * Create and fill a request object to send odl deletion to CAVI INTERRATI
	 * @param event
	 * @return
	 */
	private InvioOdlDeletionToCaviRequest buildRequest(WODeleteCAVI event) {
		InvioOdlDeletionToCaviRequest request = new InvioOdlDeletionToCaviRequest();
		Assert.notNull(event.getWonum(), "Wonum is null");
		request.setWonum(event.getWonum());
		
		return request;
	}
	
	
	/**
	 * 
	 * @param function
	 * @param source
	 * @param dest
	 * @param topic
	 * @param payload
	 * @param uuid
	 * @return
	 * @throws DataLayerException 
	 */
	private X4EsitoIntRequest createLogRequest(String function, 
											   String source, 
											   String dest, 
											   String topic, 
											   String payload,
											   String wonum,
											   String assetnum,
											   String esito,
											   String uuidResend) 
	throws DataLayerException 
	{
		
		Instant startDate	= Instant.now();
		
		X4EsitoIntRequest logRequest = new X4EsitoIntRequest();
		logRequest.setIdinterfaccia(function);
		logRequest.setSource(source);
		logRequest.setDest(dest);
		logRequest.setKafkatopic(topic);
		logRequest.setMessage(payload);
		logRequest.setUniqueid(UUID.randomUUID().toString());
		logRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		logRequest.setEsito(esito);
		logRequest.setAssetnum(assetnum);
		logRequest.setWonum(wonum);
		if(Assorted.notNull(uuidResend)) 
    	{
			logRequest.setUniqueid(uuidResend);
    		X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
    		logRequest.setParent(Assorted.notNull(parent) ? parent.getX4EsitoIntId() : null);
    	}
		else
    		logRequest.setRetry(0L);
		
		return logRequest;
	}
	

}
