package it.terna.nas.service.exposure.handler;


import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.soap.clientgen.agile.richiestaindisponibilita.RichiestaAddOrdineLavoro;
import it.terna.kernel.soap.clientgen.agile.richiestaindisponibilita.RichiestaUpdateOrdineLavoro;
import it.terna.kernel.soap.clientgen.agile.richiestaindisponibilita.Risposta;
import it.terna.kernel.soap.clientgen.odl.indisponibilita.EsitoType;
import it.terna.kernel.soap.clientgen.odl.indisponibilita.NotificaIndisponibilitaRequest;
import it.terna.kernel.soap.clientgen.odl.indisponibilita.NotificaIndisponibilitaResponse;
import it.terna.nas.service.dal.entity.eam.WorkorderPlannedDate;
import it.terna.nas.service.dal.entity.eam.X4EsitoInt;
import it.terna.nas.service.dal.service.AgileQueryService;
import it.terna.nas.service.dal.service.AssetQueryService;
import it.terna.nas.service.dal.service.WorkorderQueryService;
import it.terna.nas.service.dal.service.X4EsitoIntApiService;
import it.terna.nas.service.dal.service.X4EsitoIntQueryService;
import it.terna.nas.service.dal.service.X4IndispWoApiService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exceptions.UnavailabilityDataException;
import it.terna.nas.service.exposure.client.OdLClient;
import it.terna.nas.service.exposure.ws.maximo.JwtTokenResponse;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.material.dto.KafkaOdlToAgileRequest;
import it.terna.nas.service.material.dto.X4EsitoIntRequest;
import it.terna.nas.service.material.dto.agile.X4IndispWoRequest;
import it.terna.nas.service.material.utility.Catalog;
import it.terna.nas.service.material.utility.Catalog.AgileInterface;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.StatoCluster;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import it.terna.nas.service.material.utility.XmlUtility;
import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class AgileHandler {

    @Autowired
    private WorkorderQueryService workorderQueryService;

    @Autowired
    private X4IndispWoApiService x4IndispWoApiService;

    @Autowired
    private X4EsitoIntApiService x4EsitoIntApiService;
    
    @Autowired
    private AssetQueryService assetQueryService;
    
    @Autowired
    private AgileQueryService agileQueryService;
    
	@Autowired
	private X4EsitoIntQueryService x4EsitoIntQueryService;
    
    @Autowired
    private OdLClient odLClient;
    
    private static final Set<String> STATI_AMMESSI = Set.of(
            "DA VALUTARE", "SOSPESO", "RINVIATO", "ANNULLATO", 
            "NON PROGRAMMABILE", "IN PROGRAMMAZIONE", "DELIBERATO", 
            "CONSUNTIVATO", "TRASMESSO", "IN ESECUZIONE"
    );
    
	//RichiestaPeriodoIndisponibilitaOdL   ==============================================================================
    
	public void requestUnavailability(String topic, KafkaOdlToAgileRequest kafkaMessage, String uuidResend) {

		X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
		Risposta agileRisposta = null;
		X4IndispWoRequest saveX4IndispWo = null;
		X4IndispWoRequest errorX4IndispWo = null;
		RichiestaAddOrdineLavoro addRequest = null;
		RichiestaUpdateOrdineLavoro updateRequest = null;

		Instant startDate = Instant.now();
		log.info("start requestUnavailability | startTime={}", startDate);

		try {

			String uuid = Uuid.randomUuid().toString();

			x4EsitoIntRequest.setIdinterfaccia(AgileInterface.REQUEST_UNAVAILABILITY.getName());
			x4EsitoIntRequest.setSource(Constants.EAM);
			x4EsitoIntRequest.setDest(Constants.AGILE);
			x4EsitoIntRequest.setWonum(kafkaMessage.getWonum());
			x4EsitoIntRequest.setAssetnum(kafkaMessage.getAssetnum());
			x4EsitoIntRequest.setKafkatopic(topic);
			x4EsitoIntRequest.setMessage(kafkaMessage.toJson());
			x4EsitoIntRequest.setUniqueid(uuid);
			x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
			x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());

			if (Assorted.notNull(uuidResend)) {
				x4EsitoIntRequest.setUniqueid(uuidResend);
				X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
				x4EsitoIntRequest.setParent(Assorted.notNull(parent) ? parent.getX4EsitoIntId() : null);
			} else
				x4EsitoIntRequest.setRetry(0L);

			boolean checkPrevious = false;

			// Query check add or update, TRUE se non vengono trovate corrispondenze(ADD), altrimenti false(UPDATE)
			checkPrevious = checkPreviousUnavailability(kafkaMessage, x4EsitoIntRequest);

			if (checkPrevious) {

				addRequest = new RichiestaAddOrdineLavoro();
				addRequest = populateAddRequest(kafkaMessage, x4EsitoIntRequest, addRequest);
				JwtTokenResponse jwtToken = odLClient.getToken(EDataIntegrationEntity.AGILE_ADD_RICHIESTA_INDISPONIBILITA);
				agileRisposta = odLClient.sendAgileAddUnavailabilityRequest(addRequest, jwtToken);

			} else {

				updateRequest = new RichiestaUpdateOrdineLavoro();
				updateRequest = populateUpdateRequest(kafkaMessage, x4EsitoIntRequest, updateRequest);
				JwtTokenResponse jwtToken = odLClient.getToken(EDataIntegrationEntity.AGILE_UPDATE_RICHIESTA_INDISPONIBILITA);
				agileRisposta = odLClient.sendAgileUpdateUnavailabilityRequest(updateRequest, jwtToken);
			}

			if (agileRisposta.getEsito().equalsIgnoreCase(StatusX4Esito.OK.name())) {
				x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());
				x4EsitoIntRequest.setCodesito(null);
				x4EsitoIntRequest.setDesc_esito(null);
				x4EsitoIntRequest.setDescription(buildDescription(addRequest, updateRequest));

				saveX4IndispWo = new X4IndispWoRequest();
				saveX4IndispWo.setWonum(kafkaMessage.getWonum());
				saveX4IndispWo.setDescription(AgileInterface.REQUEST_UNAVAILABILITY.getName());
				saveX4IndispWo.setEsito(StatusX4Esito.OK.name());
			} else {

				errorX4IndispWo = new X4IndispWoRequest();
				errorX4IndispWo.setWonum(kafkaMessage.getWonum());
				errorX4IndispWo.setDescription(AgileInterface.REQUEST_UNAVAILABILITY.getName());
				errorX4IndispWo.setEsito(agileRisposta.getEsito());
				errorX4IndispWo.setIderrore(agileRisposta.getCodiceErrore());
				errorX4IndispWo.setDescerrore(agileRisposta.getDescrizioneErrore());
				log.info(" - Send requestUnavailability FAILED : {} {}", agileRisposta.getCodiceErrore(),
						agileRisposta.getDescrizioneErrore());

				x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
				x4EsitoIntRequest.setCodesito(agileRisposta.getCodiceErrore());
				x4EsitoIntRequest.setDesc_esito(agileRisposta.getDescrizioneErrore());
				x4EsitoIntRequest.setDescription(buildDescription(addRequest, updateRequest));
			}

		} catch (UnavailabilityDataException ex) {
			errorX4IndispWo = new X4IndispWoRequest();
			errorX4IndispWo.setWonum(kafkaMessage.getWonum());
			errorX4IndispWo.setDescription(AgileInterface.REQUEST_UNAVAILABILITY.getName());
			errorX4IndispWo.setEsito(StatusX4Esito.KO.name());
			errorX4IndispWo.setIderrore(ex.getErrorCode());
			errorX4IndispWo.setDescerrore(ex.getErrorDescription());

			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(ex.getErrorCode());
			x4EsitoIntRequest.setDesc_esito(ex.getErrorDescription());
			x4EsitoIntRequest.setDescription(buildDescription(addRequest, updateRequest));

			log.error(" - Send requestUnavailability FAILED : {} {}", ex.getErrorCode(), ex.getErrorDescription());

		} catch (Exception ex) {
			errorX4IndispWo = new X4IndispWoRequest();
			errorX4IndispWo.setWonum(kafkaMessage.getWonum());
			errorX4IndispWo.setDescription(AgileInterface.REQUEST_UNAVAILABILITY.getName());
			errorX4IndispWo.setEsito(StatusX4Esito.KO.name());
			errorX4IndispWo.setIderrore(EAMErrorMapping.EAM_0000.getEamCode());
			errorX4IndispWo.setDescerrore(EAMErrorMapping.EAM_0000.getErrorDescription());

			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(ex.getMessage());
			x4EsitoIntRequest.setDescription(buildDescription(addRequest, updateRequest));
			log.error(" - Send requestUnavailability FAILED : {} {}", ex.getMessage(), ex.getCause());

		} finally {
			x4EsitoIntRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
		}

		try {

			if (errorX4IndispWo != null) {
				// Se in KO errorX4IndispWo
				x4IndispWoApiService.saveIndispWo(errorX4IndispWo);
			} else if (saveX4IndispWo != null) {
				// Se in OK saveX4IndispWo
				x4IndispWoApiService.saveIndispWo(saveX4IndispWo);
			}

			x4EsitoIntApiService.sendEsitoInt(x4EsitoIntRequest);

		} catch (java.lang.Exception e) {
			log.error(
					"Rest Error when executing Maximo call to write X4ESITOINT/X4INDISPWO ERROR result using this request: {}, occured exception {}",
					Assorted.notNull(x4EsitoIntRequest) ? x4EsitoIntRequest.toString() : null, e.getMessage());
		} finally {
			Instant endDate = Instant.now();
			Duration elapsed = Duration.between(startDate, endDate);
			log.info("end requestUnavailability |end={} |start={} |elapsedMs={}", EamEtlUtils.esitoDate(endDate),
					EamEtlUtils.esitoDate(startDate), elapsed.toMillis());
		}
	}

	private RichiestaAddOrdineLavoro populateAddRequest(KafkaOdlToAgileRequest kafkaMessage, X4EsitoIntRequest x4EsitoIntRequest, RichiestaAddOrdineLavoro request) {
		
		log.info(" - populateAddRequest");
		
		// PARTE 1 RECUPERO INFO DA MESSAGGIO KAFKA
		if(Assorted.notNull(kafkaMessage.getWonum())) {
			request.setCodiceODL(kafkaMessage.getWonum());
		} else {
		    log.error(" - No wonum found in Kafka message");
		    throw new RuntimeException("No wonum found in Kafka message");
		}
		
		if(Assorted.notNull(kafkaMessage.getDescription())) {
			request.setDescrizioneODL(kafkaMessage.getDescription());
		} else {
		    log.error(" - No description found in Kafka message");
		    throw new RuntimeException("No description found in Kafka message");
		}
		
		if(Assorted.notNull(kafkaMessage.getStatus())) {
			request.setStatoODL(kafkaMessage.getStatus());
		} else {
		    log.error(" - No Status found in Kafka message");
		    throw new RuntimeException("No Status found in Kafka message");
		}
		
		if(Assorted.notNull(kafkaMessage.getJpnum())) {
			request.setPianoLavoro(kafkaMessage.getJpnum());
		} else {
		    log.error(" - No jpnum found in Kafka message");
		    throw new RuntimeException("No jpnum found in Kafka message");
		}
		
		if (Assorted.notNull(kafkaMessage.getSchedstart()) && Assorted.notNull(kafkaMessage.getSchedfinish())) {

		    log.info(" - GEOCALL scheduled dates found in Kafka message");

		    XMLGregorianCalendar startDate = EamEtlUtils.stringDateToXmlGC(kafkaMessage.getSchedstart());
		    request.setDataInizioPianificata(startDate);

		    XMLGregorianCalendar endDate = EamEtlUtils.stringDateToXmlGC(kafkaMessage.getSchedfinish());
		    request.setDataFinePianificata(endDate);

		} else {    

		    boolean hasTargStart = Assorted.notNull(kafkaMessage.getTargstartdate());
		    boolean hasTargComp = Assorted.notNull(kafkaMessage.getTargcompdate());

		    if (!hasTargStart || !hasTargComp) {
		        log.error(" - Missing one or both target dates in Kafka message");
		        throw new RuntimeException("Missing one or both planned unavailability dates in Kafka message");
		    }

		    XMLGregorianCalendar startDate = EamEtlUtils.stringDateToXmlGC(kafkaMessage.getTargstartdate());
		    request.setDataInizioPianificata(startDate);

		    XMLGregorianCalendar endDate = EamEtlUtils.stringDateToXmlGC(kafkaMessage.getTargcompdate());
		    request.setDataFinePianificata(endDate);
		}
		
		if(Assorted.notNull(kafkaMessage.getX4wbs())) {
			request.setWbsCbs(kafkaMessage.getX4wbs());
		} else {
		    log.info(" - No X4wbs found in Kafka message");
		    request.setWbsCbs(null);
		}
		
		if(Assorted.notNull(kafkaMessage.getX4tipospesa())) {
			request.setTipoSpesa(kafkaMessage.getX4tipospesa());
		} else {
		    log.error(" - No X4tipoSpesa found in Kafka message");
		    throw new RuntimeException("No X4tipoSpesa found in Kafka message");
		}
		
		//HLD v21 ============================
//		if(Assorted.notNull(kafkaMessage.getOwnerGroup())) {
//			request.setTitolareUnitaImpianti(kafkaMessage.getOwnerGroup());
//		} else {
//		    log.error(" - No OwnerGroup found in Kafka message");
//		    throw new RuntimeException("No OwnerGroup found in Kafka message");
//		}
		//====================================
		
		// PARTE 2 RECUPERO INFO DA QUERY DB
		
		// 2.1 Recupero ancestor tramite l'asset
		String ancestor = getAncestor(kafkaMessage, x4EsitoIntRequest);
		
		// 2.2 Recupero lista OdL figli
		
		// 2.2.2 Nuova istanza della lista figli
		List<String> odLFigliList = getWonumChildList(kafkaMessage, x4EsitoIntRequest);
		
		RichiestaAddOrdineLavoro.ListaOdlFigli tempFigliObj = new RichiestaAddOrdineLavoro.ListaOdlFigli();
		if (odLFigliList == null || odLFigliList.isEmpty()) {
		    
		    //Se la lista degli OdL figli è vuota va inserito il padre	
//			tempFigliObj.getCodOdlFiglio().add(kafkaMessage.getWonum());
//			request.setListaOdlFigli(tempFigliObj);
			
			//Modifica 04/02/2025 - lista degli OdL figli è vuota => null
			request.setListaOdlFigli(null);
			
		}else {			
			tempFigliObj.getCodOdlFiglio().addAll(odLFigliList);
			request.setListaOdlFigli(tempFigliObj);			
		}
		
		//2.3 Recupero Sede tecnica indisponibilità
		String sedeTecnicaIndisponibilita = getUnavailabilityElement(kafkaMessage, x4EsitoIntRequest);
		if(Assorted.notNull(sedeTecnicaIndisponibilita)) {			
			request.setCodiceSedeTecnicaIndisp(sedeTecnicaIndisponibilita);
		} else{
			throw new UnavailabilityDataException(
					EAMErrorMapping.EAM_0031.getEamCode(),
					EAMErrorMapping.EAM_0031.getErrorDescription()
			);
		}		
		
		//2.4 Recupero Sede tecnica indisponibilità padre
		if(null == ancestor) {
			//2.4.1 Caso LINEA
			log.info("Ancestor non presente, SedeTecnicaIndispPadre = CodiceSedeTecnicaIndisp");
			request.setSedeTecnicaIndispPadre(sedeTecnicaIndisponibilita);			
		}else {			
			request.setSedeTecnicaIndispPadre(getUnavailabilityFatherElement(kafkaMessage, ancestor, x4EsitoIntRequest));
		}
		
		//2.5 Recupero Sedi tecniche interferenti		
		List<String> sediInterferentiList = new ArrayList<>();
		RichiestaAddOrdineLavoro.SediTecnicheInterferenti tempInterObj = new RichiestaAddOrdineLavoro.SediTecnicheInterferenti();
		
		//2.5.1
		sediInterferentiList = getInterferingAssets(kafkaMessage, ancestor, x4EsitoIntRequest);
		
		//2.5.2
		sediInterferentiList.addAll(getInterferingStationAssets(kafkaMessage, x4EsitoIntRequest));
		
		//2.5.3
		sediInterferentiList.addAll(getInterferingLineAssets(kafkaMessage, ancestor, x4EsitoIntRequest));
		
		tempInterObj.getSedeTecnicaInterferente().addAll(sediInterferentiList);
		request.setSediTecnicheInterferenti(tempInterObj);
		
		//2.6 Recupero Stato PdL
		request.setRichiestaIndisponibilita(getUnavailabilityRequestFlag(kafkaMessage, x4EsitoIntRequest));
		
		//2.7 Recupero Responsabile asset HLD v21
		request.setTitolareUnitaImpianti(getAssetHolderUnit(kafkaMessage, x4EsitoIntRequest));
		
		return request;
	}
	
	private RichiestaUpdateOrdineLavoro populateUpdateRequest(KafkaOdlToAgileRequest kafkaMessage, X4EsitoIntRequest x4EsitoIntRequest, RichiestaUpdateOrdineLavoro request) {
		
		log.info(" - populateUpdateRequest");
		
		// PARTE 1 RECUPERO INFO DA MESSAGGIO KAFKA
		if(Assorted.notNull(kafkaMessage.getWonum())) {
			request.setCodiceODL(kafkaMessage.getWonum());
		} else {
		    log.error(" - No wonum found in Kafka message");
		    throw new RuntimeException("No wonum found in Kafka message");
		}
		
		if(Assorted.notNull(kafkaMessage.getDescription())) {
			request.setDescrizioneODL(kafkaMessage.getDescription());
		} else {
		    log.error(" - No description found in Kafka message");
		    throw new RuntimeException("No description found in Kafka message");
		}
		
		if(Assorted.notNull(kafkaMessage.getStatus())) {
			request.setStatoODL(kafkaMessage.getStatus());
		} else {
		    log.error(" - No Status found in Kafka message");
		    throw new RuntimeException("No Status found in Kafka message");
		}
		
		if(Assorted.notNull(kafkaMessage.getJpnum())) {
			request.setPianoLavoro(kafkaMessage.getJpnum());
		} else {
		    log.error(" - No jpnum found in Kafka message");
		    throw new RuntimeException("No jpnum found in Kafka message");
		}
		
		if (Assorted.notNull(kafkaMessage.getSchedstart()) && Assorted.notNull(kafkaMessage.getSchedfinish())) {

		    log.info(" - GEOCALL scheduled dates found in Kafka message");

		    XMLGregorianCalendar startDate = EamEtlUtils.stringDateToXmlGC(kafkaMessage.getSchedstart());
		    request.setDataInizioPianificata(startDate);

		    XMLGregorianCalendar endDate = EamEtlUtils.stringDateToXmlGC(kafkaMessage.getSchedfinish());
		    request.setDataFinePianificata(endDate);

		} else {    

		    boolean hasTargStart = Assorted.notNull(kafkaMessage.getTargstartdate());
		    boolean hasTargComp = Assorted.notNull(kafkaMessage.getTargcompdate());

		    if (!hasTargStart || !hasTargComp) {
		        log.error(" - Missing one or both target dates in Kafka message");
		        throw new RuntimeException("Missing one or both planned unavailability dates in Kafka message");
		    }

		    XMLGregorianCalendar startDate = EamEtlUtils.stringDateToXmlGC(kafkaMessage.getTargstartdate());
		    request.setDataInizioPianificata(startDate);

		    XMLGregorianCalendar endDate = EamEtlUtils.stringDateToXmlGC(kafkaMessage.getTargcompdate());
		    request.setDataFinePianificata(endDate);
		}
		
		if(Assorted.notNull(kafkaMessage.getX4wbs())) {
			request.setWbsCbs(kafkaMessage.getX4wbs());
		} else {
		    log.info(" - No X4wbs found in Kafka message");
		    request.setWbsCbs(null);
		}
		
		if(Assorted.notNull(kafkaMessage.getX4tipospesa())) {
			request.setTipoSpesa(kafkaMessage.getX4tipospesa());
		} else {
		    log.error(" - No X4tipoSpesa found in Kafka message");
		    throw new RuntimeException("No X4tipoSpesa found in Kafka message");
		}
		
		//HLD v21 ============================
//		if(Assorted.notNull(kafkaMessage.getOwnerGroup())) {
//			request.setTitolareUnitaImpianti(kafkaMessage.getOwnerGroup());
//		} else {
//		    log.error(" - No OwnerGroup found in Kafka message");
//		    throw new RuntimeException("No OwnerGroup found in Kafka message");
//		}
		//====================================
		
		// PARTE 2 RECUPERO INFO DA QUERY DB
		
		// 2.1 Recupero ancestor tramite l'asset
		String ancestor = getAncestor(kafkaMessage, x4EsitoIntRequest);
		
		// 2.2 Recupero lista OdL figli
		
		// 2.2.2 Nuova istanza della lista figli
		List<String> odLFigliList = getWonumChildList(kafkaMessage, x4EsitoIntRequest);
		
		RichiestaUpdateOrdineLavoro.ListaOdlFigli tempFigliObj = new RichiestaUpdateOrdineLavoro.ListaOdlFigli();
		if (odLFigliList == null || odLFigliList.isEmpty()) {
		    
		    //Se la lista degli OdL figli è vuota va inserito il padre	
//			tempFigliObj.getCodOdlFiglio().add(kafkaMessage.getWonum());
//			request.setListaOdlFigli(tempFigliObj);
			
			//Modifica 04/02/2025 - lista degli OdL figli è vuota => null
			request.setListaOdlFigli(null);
			
		}else {			
			tempFigliObj.getCodOdlFiglio().addAll(odLFigliList);
			request.setListaOdlFigli(tempFigliObj);			
		}
		
		//2.3 Recupero Sede tecnica indisponibilità
		String sedeTecnicaIndisponibilita = getUnavailabilityElement(kafkaMessage, x4EsitoIntRequest);
		if(Assorted.notNull(sedeTecnicaIndisponibilita)) {			
			request.setCodiceSedeTecnicaIndisp(sedeTecnicaIndisponibilita);
		} else{
			throw new UnavailabilityDataException(
					EAMErrorMapping.EAM_0031.getEamCode(),
					EAMErrorMapping.EAM_0031.getErrorDescription()
			);
		}	
		
		//2.4 Recupero Sede tecnica indisponibilità padre
		if(null == ancestor) {
			//2.4.1 Caso LINEA
			log.info("Ancestor non presente, SedeTecnicaIndispPadre = CodiceSedeTecnicaIndisp");
			request.setSedeTecnicaIndispPadre(sedeTecnicaIndisponibilita);			
		}else {			
			request.setSedeTecnicaIndispPadre(getUnavailabilityFatherElement(kafkaMessage, ancestor, x4EsitoIntRequest));
		}
		
		//2.5 Recupero Sedi tecniche interferenti		
		List<String> sediInterferentiList = new ArrayList<>();
		RichiestaUpdateOrdineLavoro.SediTecnicheInterferenti tempInterObj = new RichiestaUpdateOrdineLavoro.SediTecnicheInterferenti();
		
		//2.5.1
		sediInterferentiList = getInterferingAssets(kafkaMessage, ancestor, x4EsitoIntRequest);
		
		//2.5.2
		sediInterferentiList.addAll(getInterferingStationAssets(kafkaMessage, x4EsitoIntRequest));
		
		//2.5.3
		sediInterferentiList.addAll(getInterferingLineAssets(kafkaMessage, ancestor, x4EsitoIntRequest));
		
		tempInterObj.getSedeTecnicaInterferente().addAll(sediInterferentiList);
		request.setSediTecnicheInterferenti(tempInterObj);
		
		//2.6 Recupero stato PdL
		request.setRichiestaIndisponibilita(getUnavailabilityRequestFlag(kafkaMessage, x4EsitoIntRequest));
		
		//2.7 Recupero Responsabile asset HLD v21
		request.setTitolareUnitaImpianti(getAssetHolderUnit(kafkaMessage, x4EsitoIntRequest));
		
		return request;
	}
	
	private String getAssetHolderUnit(KafkaOdlToAgileRequest kafkaMessage, X4EsitoIntRequest x4EsitoIntRequest) {
		
		log.info(" - getAssetHolderUnit");
		
		String result = Constants.BLANK_STRING;
		
		try {
			
			result = assetQueryService.findX3ingrpByAssetNumAndSiteId(kafkaMessage.getAssetnum(), kafkaMessage.getSiteid());
			
		} catch (DataLayerException ex) {
			
			log.error(" - Error while retrieving AssetHolderUnit");
			
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(ex.getMessage());	
		}
		return result;
	}

	private boolean getUnavailabilityRequestFlag(KafkaOdlToAgileRequest kafkaMessage, X4EsitoIntRequest x4EsitoIntRequest) {
		
		try {
			
	        Integer queryResult = agileQueryService.findRequestUnavailabilityFlag(kafkaMessage.getJpnum(), kafkaMessage.getPluscjprevnum());

	        log.info("  - Search getRequestUnavailabilityFlag  value: [{}]", queryResult);
	        
	        // Se il risultato è 1, ritorna true; se è 0, ritorna false
	        return queryResult != null && queryResult == 1;

	    } catch (DataLayerException ex) {
	        log.error("Errore durante il recupero del flag di indisponibilita': {}", ex.getMessage(), ex);

	        x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
	        x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
	        x4EsitoIntRequest.setDesc_esito("Errore nel recupero del flag di indisponibilita'");

	        throw new RuntimeException("Errore nel recupero del flag di indisponibilita'", ex);
	    }
	}
 	
	private List<String> getInterferingAssets(KafkaOdlToAgileRequest kafkaMessage, String ancestor, X4EsitoIntRequest x4EsitoIntRequest) {
		
		List<String> result = new ArrayList<>();
		
		try {
			
			result = agileQueryService.findInterferingAssets(kafkaMessage.getAssetnum(), ancestor);
			
		} catch (DataLayerException ex) {
			
			log.error(" - Error while retrieving Interfering Assets");
			
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(ex.getMessage());	
		}
		return result != null ? result : new ArrayList<>();
	}

	private List<String> getInterferingStationAssets(KafkaOdlToAgileRequest kafkaMessage, X4EsitoIntRequest x4EsitoIntRequest) {
		
		List<String> result = new ArrayList<>();
		
		try {
			
			result = agileQueryService.findInterferingStationAssets(kafkaMessage.getAssetnum());
			
		} catch (DataLayerException ex) {
			
			log.error(" - Error while retrieving Interfering Station Assets");
			
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(ex.getMessage());	
		}
		return result != null ? result : new ArrayList<>();
	}	
	
	private List<String> getInterferingLineAssets(KafkaOdlToAgileRequest kafkaMessage, String ancestor, X4EsitoIntRequest x4EsitoIntRequest) {
		
		List<String> result = new ArrayList<>();
		
		try {
			
			result = agileQueryService.findInterferingLineAssets(kafkaMessage.getAssetnum(), ancestor);
			
		} catch (DataLayerException ex) {
			
			log.error(" - Error while retrieving Interfering Line Assets");
			
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(ex.getMessage());	
		}
		return result != null ? result : new ArrayList<>();
	}
	
	private String getUnavailabilityFatherElement(KafkaOdlToAgileRequest kafkaMessage, String ancestor, X4EsitoIntRequest x4EsitoIntRequest) {
		
		String result = Constants.BLANK_STRING;
		
		try {
			
			result = agileQueryService.findUnavailabilityFatherElement(kafkaMessage.getAssetnum(), ancestor);
			
		} catch (DataLayerException ex) {
			
			log.error(" - Error while retrieving UnavailabilityFatherElement");
			
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(ex.getMessage());	
		}
		return result;
	}
	
	private String getUnavailabilityElement(KafkaOdlToAgileRequest kafkaMessage, X4EsitoIntRequest x4EsitoIntRequest) {
		
		log.info(" - getUnavailabilityElement");
		
		String result = Constants.BLANK_STRING;
		
		try {
			
			result = agileQueryService.findUnavailabilityElement(kafkaMessage.getAssetnum());
			
		} catch (DataLayerException ex) {
			
			log.error(" - Error while retrieving UnavailabilityElement");
			
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(ex.getMessage());	
		}
		return result;
	}
	
	private List<String> getWonumChildList(KafkaOdlToAgileRequest kafkaMessage, X4EsitoIntRequest x4EsitoIntRequest) {
		
		List<String> result = new ArrayList<>();
		
		try {
			
			result = workorderQueryService.findWonumChildList(kafkaMessage.getWonum());
			
		} catch (DataLayerException ex) {
			
			log.error(" - Error while retrieving wonum child list");
			
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(ex.getMessage());	
		}
		return result != null ? result : new ArrayList<>();
	}
	
	private String getAncestor(KafkaOdlToAgileRequest kafkaMessage, X4EsitoIntRequest x4EsitoIntRequest) {
		
		String ancestor = Constants.BLANK_STRING;
		
		try {
			
			ancestor = assetQueryService.findAncestorByAssetNumAndSiteId(kafkaMessage.getAssetnum(), kafkaMessage.getSiteid());
			
		} catch (DataLayerException ex) {
			
			log.error(" - Error while retrieving Ancestor");
			
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(ex.getMessage());	
		}
		return ancestor;
	}
	
	private boolean checkPreviousUnavailability(KafkaOdlToAgileRequest kafkaMessage, X4EsitoIntRequest x4EsitoIntRequest) {
		
		log.info(" - checkPreviousUnavailability");
		boolean checkPrevious = false;
		
		try {
			
			checkPrevious = agileQueryService.findPreviousUnavailabilityRequest(kafkaMessage.getWonum());
			
		} catch (DataLayerException ex) {
			
			log.error(" - Error while checking previous unavailability");
			
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(ex.getMessage());	
		}
		return checkPrevious;
	}
	
	//NotificaPeriodoIndisponibilitaOdL  ==============================================================================

	public NotificaIndisponibilitaResponse notifyUnavailability(NotificaIndisponibilitaRequest request) {

        log.debug("call notifyUnavailability({})", request.getCodiceOdL());
        Instant startDate								= Instant.now();
        String uuid 									= Uuid.randomUuid().toString();
        X4IndispWoRequest errorX4IndispWo 				= null;
        NotificaIndisponibilitaResponse response        = new NotificaIndisponibilitaResponse();
        Long eamStartPlannedDate = null;
        Long eamCompPlannedDate = null;
        
        EsitoType esitoType = new EsitoType();
        esitoType.setESITO(Constants.ESITO_OK);
        response.setEsito(esitoType);
        X4EsitoIntRequest x4EsitoIntRequest 			= new X4EsitoIntRequest();
        x4EsitoIntRequest.setIdinterfaccia(Catalog.AgileInterface.NOTIFY_UNAVAILABILITY.getName());
        x4EsitoIntRequest.setSource(Constants.AGILE);
        x4EsitoIntRequest.setDest(Constants.EAM);
        x4EsitoIntRequest.setUniqueid(uuid);
        x4EsitoIntRequest.setEsito(Catalog.StatusX4Esito.OK.name());
        x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
        x4EsitoIntRequest.setMessage(XmlUtility.convertToString(NotificaIndisponibilitaRequest.class.getPackageName(), request));
        try{
        	
        	long startTime = System.currentTimeMillis();
        	log.debug("call notifyUnavailability({}) |{}|", request.getCodiceOdL(), startTime);
        	log.info("- REQUEST AGILE => CodiceOdL:{} - CodiceClusterTitolare:{} - DataInizioProgrammazione:{} - DataFineProgrammazione:{} - StatoCluster:{} - MOD:{}",
        			request.getCodiceOdL(),
        			request.getCodiceClusterTitolare(),
        			request.getDataInizioProgrammazione(),
        			request.getDataFineProgrammazione(),
        			request.getStatoCluster(),
        			request.getMOD());
        	
            List<WorkorderPlannedDate> workorders = workorderQueryService.getWorkorderPlannedDateByWonum(request.getCodiceOdL());            
            if(Assorted.isNotEmpty(workorders)) {
            	log.info("- workorders found: {}", workorders.size());
            	eamStartPlannedDate = convertDateToUnix(workorders.get(0).getTargstartdate());
            	eamCompPlannedDate = convertDateToUnix(workorders.get(0).getTargcompdate());        	
            
            }
            
            if(verifyRequest(request, workorders)) {
            	
            	boolean verifyDates = validateAgileDates(request, eamStartPlannedDate, eamCompPlannedDate);
            	MaximoResponse maximoResponse = x4IndispWoApiService.saveIndispWo(buildX4IndispWoRequest(request, verifyDates));                
                if (maximoResponse.getHttpCode() != 200)
                    throw new EAMException(Catalog.EAMErrorMapping.EAM_0000.getEamCode(), Catalog.EAMErrorMapping.EAM_0000.getErrorDescription());
            } else {
                throw new EAMException(Catalog.EAMErrorMapping.EAM_0000.getEamCode(), Catalog.EAMErrorMapping.EAM_0000.getErrorDescription());
            }
        }catch(EAMException e){
            log.error("- Errore:{}", e.getMessage());
            errorX4IndispWo = new X4IndispWoRequest();
            errorX4IndispWo.setWonum(request.getCodiceOdL());
            errorX4IndispWo.setEsito(Constants.ESITO_KO);
            errorX4IndispWo.setIderrore(e.getErrorCode());
            errorX4IndispWo.setDescerrore(e.getMessage());
            esitoType.setESITO(Constants.ESITO_KO);
            esitoType.setCODICEERRORE(e.getErrorCode());
            esitoType.setDESCRIZIONEERRORE(e.getMessage());
            response.setEsito(esitoType);
            x4EsitoIntRequest.setWonum(request.getCodiceOdL());
            x4EsitoIntRequest.setEsito(Catalog.StatusX4Esito.KO.name());
            x4EsitoIntRequest.setCodesito(e.getErrorCode());
            x4EsitoIntRequest.setDesc_esito(e.getMessage());            
            
        }catch(Exception ex){
            log.error("###Errore:{}", ex.getMessage());
            errorX4IndispWo = new X4IndispWoRequest();
            errorX4IndispWo.setWonum(request.getCodiceOdL());
            errorX4IndispWo.setEsito(Constants.ESITO_KO);
            errorX4IndispWo.setIderrore(Catalog.EAMErrorMapping.EAM_0000.getEamCode());
            errorX4IndispWo.setDescerrore(Catalog.EAMErrorMapping.EAM_0000.getErrorDescription());
            esitoType.setESITO(Constants.ESITO_KO);
            esitoType.setCODICEERRORE(Catalog.EAMErrorMapping.EAM_0000.getEamCode());
            esitoType.setDESCRIZIONEERRORE(Catalog.EAMErrorMapping.EAM_0000.getErrorDescription());
            x4EsitoIntRequest.setWonum(request.getCodiceOdL());
            x4EsitoIntRequest.setEsito(Catalog.StatusX4Esito.KO.name());
            x4EsitoIntRequest.setCodesito(Catalog.EAMErrorMapping.EAM_0000.getEamCode());
            x4EsitoIntRequest.setDesc_esito(Catalog.EAMErrorMapping.EAM_0000.getErrorDescription() + " " + ex.getMessage());
        }
        finally {
            x4EsitoIntRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
            try {
            	//Salva solo se in KO
            	if(null != errorX4IndispWo) {
            		x4IndispWoApiService.saveIndispWo(errorX4IndispWo);
            	}
            	
                x4EsitoIntApiService.sendEsitoInt(x4EsitoIntRequest);
                
            } catch (RestException e) {
                log.error("Rest Error when executing Maximo call to write X4ESITOINT/X4INDISPWO ERROR result using this request: {}, occured exception {}",
                        Assorted.notNull(x4EsitoIntRequest)
                                ? x4EsitoIntRequest.toString()
                                : null,
                        e.getMessage());
            }
        }
        return response;
    }

    private boolean verifyRequest(NotificaIndisponibilitaRequest request, List<WorkorderPlannedDate> workorders) throws DataLayerException, EAMException {
        
    	log.info(" - verifyRequest");
    	
        if (!Assorted.notNull(request, request.getCodiceOdL(), request.getCodiceClusterTitolare(), request.getStatoCluster())) {
            throw new EAMException(Catalog.EAMErrorMapping.EAM_0001.getEamCode(), Catalog.EAMErrorMapping.EAM_0001.getErrorDescription());
        }

        if (request.getStatoCluster() == null || !STATI_AMMESSI.contains(request.getStatoCluster())) {
            throw new EAMException(Catalog.EAMErrorMapping.EAM_0002.getEamCode(), "Field statoCluster not valorized correctly.");
        }
        
        if (workorders == null || workorders.size() != 1) {
            throw new EAMException(Catalog.EAMErrorMapping.EAM_0004.getEamCode(), Catalog.EAMErrorMapping.EAM_0004.getErrorDescription());
        }

        if (request.getDataInizioProgrammazione() != null && request.getDataFineProgrammazione() != null &&
                request.getDataFineProgrammazione() < request.getDataInizioProgrammazione()) {
                throw new EAMException(Catalog.EAMErrorMapping.EAM_0001.getEamCode(), Catalog.EAMErrorMapping.EAM_0001.getErrorDescription());
        }

        return true;
    }

    private boolean validateAgileDates(NotificaIndisponibilitaRequest request, Long eamStartPlannedDate, Long eamCompPlannedDate) throws DataLayerException, EAMException {
        
    	log.info(" - verifyRequest");
    	
    	Long agileStartIndispDate = request.getDataInizioProgrammazione();
        Long agileEndIndispDate = request.getDataFineProgrammazione();

        // Validazione superata se l'intervallo temporale AGILE è contenuto o coincide con l' intervallo temporale EAM
        if (agileStartIndispDate >= eamStartPlannedDate && agileEndIndispDate <= eamCompPlannedDate) {
            return true; // Date valide
        }

        log.info("Unavailability dates not compliant with OdL dates.");
        return false; // Date non valide
    }
    
    private X4IndispWoRequest buildX4IndispWoRequest(NotificaIndisponibilitaRequest request, boolean verifyDates){
        
    	X4IndispWoRequest finalRequest = new X4IndispWoRequest();    	
    	
        finalRequest.setWonum(request.getCodiceOdL());        
        
        finalRequest.setX4cluster(request.getCodiceClusterTitolare());
        
        finalRequest.setInizioindisponibilita(request.getDataInizioProgrammazione() != null ? 
        	    convertToISO8601(request.getDataInizioProgrammazione()) : null);
        
        finalRequest.setFineindisponibilita(    request.getDataFineProgrammazione() != null ? 
        	    convertToISO8601(request.getDataFineProgrammazione()) : null);
        
        //Mappatura stati AGILE -> EAM
        StatoCluster mappedStatus = StatoCluster.valueOf(request.getStatoCluster().replace(" ", "_").toUpperCase());
        finalRequest.setStatus(mappedStatus.getStatusMapping());
        
        if(finalRequest.getStatus().equalsIgnoreCase("DELIBERATO") || 
        		finalRequest.getStatus().equalsIgnoreCase("CONSUNTIVATO")) {
        	finalRequest.setEsito(Constants.ESITO_OK);
        }
        
        if(finalRequest.getStatus().equalsIgnoreCase("DELIBERATO") && !verifyDates){
        	finalRequest.setEsito(Constants.ESITO_KO);
        	finalRequest.setIderrore(Catalog.EAMErrorMapping.EAM_0017.getEamCode());
        	finalRequest.setDescerrore("Date deliberate non conformi.");
        }

        if(finalRequest.getStatus().equalsIgnoreCase("SOSPESO") || 
        		finalRequest.getStatus().equalsIgnoreCase("ANNULLATO")) {
        	finalRequest.setEsito(Constants.ESITO_KO);
        	finalRequest.setIderrore(null);
        	finalRequest.setDescerrore(null);
        }
        
        finalRequest.setMotivoann(request.getMOD());
        
        return finalRequest;
        
    }
    
    public static long convertDateToUnix(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("Date value cannot be null");
        }
        return date.getTime();
    }
    
    public static String convertToISO8601(Long timestamp) {
        if (timestamp == null) return null;

        return Instant.ofEpochMilli(timestamp)
                      .atOffset(ZoneOffset.UTC) // Imposta il fuso orario UTC
                      .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME); // Formato ISO 8601
    }
    
	public static String buildDescription(RichiestaAddOrdineLavoro addReq, RichiestaUpdateOrdineLavoro updReq) {

		// sceglie la request valorizzata
		String p = "";
		String c = "";

		if (hasAny(addReq)) {
			p = normalize(addReq.getSedeTecnicaIndispPadre());
			c = normalize(addReq.getCodiceSedeTecnicaIndisp());
		} else if (hasAny(updReq)) {
			p = normalize(updReq.getSedeTecnicaIndispPadre());
			c = normalize(updReq.getCodiceSedeTecnicaIndisp());
		} else {
			return "";
		}

		return composeWithTruncSecond(p, c, 50, ";");
	}

	private static boolean hasAny(RichiestaAddOrdineLavoro r) {
		if (r == null)
			return false;
		return !normalize(r.getSedeTecnicaIndispPadre()).isEmpty()
				|| !normalize(r.getCodiceSedeTecnicaIndisp()).isEmpty();
	}

	private static boolean hasAny(RichiestaUpdateOrdineLavoro r) {
		if (r == null)
			return false;
		return !normalize(r.getSedeTecnicaIndispPadre()).isEmpty()
				|| !normalize(r.getCodiceSedeTecnicaIndisp()).isEmpty();
	}

	private static String normalize(String s) {
		if (s == null)
			return "";
		String t = s.trim();
		return t.isEmpty() ? "" : t;
	}
	
	private static String composeWithTruncSecond(String first, String second, int max, String sep) {
		if (first.isEmpty() && second.isEmpty())
			return "";

		if (second.isEmpty())
			return limit(first, max);
		if (first.isEmpty())
			return limit(second, max);

		int availableForSecond = max - (first.length() + sep.length());

		//primo già troppo lungo
		if (availableForSecond <= 0)
			return limit(first, max);

		String second2 = second.length() <= availableForSecond ? second : second.substring(0, availableForSecond);

		return first + sep + second2;
	}

	private static String limit(String s, int max) {
		if (s == null)
			return "";
		return s.length() <= max ? s : s.substring(0, max);
	}
    
}
