package it.terna.nas.service.exposure.handler;

import java.time.Instant;

import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.nas.service.dal.service.X4CostiTagetikQueryService;
import it.terna.nas.service.dal.service.X4EsitoIntApiService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.ws.elive.InvioProvvedimentiResponse;
import it.terna.nas.service.material.dto.X4EsitoIntRequest;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.Catalog.TagetikInterface;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TagetikHandler extends AbstractHandler{

	@Autowired
	private X4EsitoIntApiService 		x4EsitoIntApiService;

	@Autowired
	private X4CostiTagetikQueryService  x4CostiTagetikQueryService;
	
	public InvioProvvedimentiResponse costsRetrievalTagetik(String data)
	{
		Instant startDate	= Instant.now();
		String uuid 		= Uuid.randomUuid().toString();

		X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(TagetikInterface.COSTS_RETRIEVAL.getName());
		x4EsitoIntRequest.setSource(Constants.EAM);
		x4EsitoIntRequest.setDest(Constants.TAGETIK);
		x4EsitoIntRequest.setMessage(data);
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());
		
		InvioProvvedimentiResponse response = new InvioProvvedimentiResponse();
		response.setEsito(StatusX4Esito.OK.name());
		response.setHttpCode(HttpStatus.OK.value());
		
		try {	
			if(Assorted.notNull(data)) {
				String formattedDate = data.substring(0, 4) + "-" + data.substring(4, 6) + "-" + data.substring(6, 8);
				x4CostiTagetikQueryService.processAllTagetikCosts(formattedDate);
			}
			else {
				throw new EAMException(EAMErrorMapping.EAM_0001.getEamCode(), EAMErrorMapping.EAM_0001.getErrorDescription());
			}
		
		} catch(EAMException q) {
			log.error(q.getMessage());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(q.getErrorCode());
			x4EsitoIntRequest.setDesc_esito(q.getMessage());
			response.setCodiceErrore(q.getErrorCode());
			response.setDescrizioneErrore(q.getMessage());
		} catch(java.lang.Exception q) {
			log.error("Generic Error: " + q.getMessage(), q);
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + q.getMessage());
			response.setCodiceErrore(EAMErrorMapping.EAM_0000.getEamCode());
			response.setDescrizioneErrore(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + q.getMessage());
		} finally {
			x4EsitoIntRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
		}
		
		try {
			x4EsitoIntApiService.sendEsitoInt(x4EsitoIntRequest);
		} catch(java.lang.Exception e) {
			log.error("Rest Error when executing Maximo call to write X4ESITOINT result using this request: {}, occured exception {}", 
					  Assorted.notNull(x4EsitoIntRequest)
					  ? x4EsitoIntRequest.toString() 
					  : null,
					  e.getMessage());
		}
		return response;
	}
}