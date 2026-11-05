package it.terna.nas.service.exposure.handler;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.nas.service.configuration.properties.EliveInterferenzeProperties;
import it.terna.nas.service.dal.entity.eam.AlnvalueResult;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.DatiReportElive;
import it.terna.nas.service.dal.entity.eam.SapIdAssetNumResult;
import it.terna.nas.service.dal.entity.eam.ScartiReportElive;
import it.terna.nas.service.dal.entity.eam.X4CatInterf;
import it.terna.nas.service.dal.entity.eam.X4CatModelloAsset;
import it.terna.nas.service.dal.entity.eam.X4EliveInterfConfig;
import it.terna.nas.service.dal.entity.eam.X4EsitoInt;
import it.terna.nas.service.dal.service.EliveService;
import it.terna.nas.service.dal.service.X4EsitoIntApiService;
import it.terna.nas.service.dal.service.X4EsitoIntQueryService;
import it.terna.nas.service.dal.service.X4InterfstatApiService;
import it.terna.nas.service.dal.service.X4WorkorderApiService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.handler.rest.EliveRestClient;
import it.terna.nas.service.exposure.ws.elive.EliveInvioOdlRequest;
import it.terna.nas.service.exposure.ws.elive.EliveInvioOdlResponse;
import it.terna.nas.service.exposure.ws.elive.InvioProvvedimentiRequest;
import it.terna.nas.service.exposure.ws.elive.InvioProvvedimentiResponse;
import it.terna.nas.service.exposure.ws.elive.RecordFileElive;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponseArray;
import it.terna.nas.service.exposure.ws.maximo.WorkorderBulkResponse;
import it.terna.nas.service.exposure.ws.maximo.WorkorderGetResponse;
import it.terna.nas.service.exposure.ws.maximo.WorkorderRequest;
import it.terna.nas.service.exposure.ws.maximo.X4InterfstatResponse;
import it.terna.nas.service.material.dto.InterferenzaStats;
import it.terna.nas.service.material.dto.KafkaOdlToEliveRequest;
import it.terna.nas.service.material.dto.KafkaOdlToEliveRequest.AssetLocKafka;
import it.terna.nas.service.material.dto.KafkaProvvedimentiPianteMessage;
import it.terna.nas.service.material.dto.SperimentazioneAggregationKey;
import it.terna.nas.service.material.dto.X4EsitoIntRequest;
import it.terna.nas.service.material.dto.X4InterfstatRequest;
import it.terna.nas.service.material.utility.Catalog;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EliveInterface;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EamEtlUtils;
import it.terna.nas.service.material.utility.VoltageLevelLinea;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EliveHandler extends AbstractHandler{

	@Autowired
	private BrokerEntityConfiguration	brokerEntities;

	@Autowired
	private KafkaProducerHandler		kafkaHandler;

	@Autowired
	private X4EsitoIntApiService 		x4EsitoIntApiService;

	@Autowired
	private X4InterfstatApiService 		x4InterfstatApiService;

	@Autowired
	private EliveService 				eliveService;

	@Autowired
	private EliveRestClient 			eliveRestClient;
	
	@Autowired
	private EliveInterferenzeProperties eliveInterferenzeProperties;
	
	@Autowired
	private X4EsitoIntQueryService 		x4EsitoIntQueryService;
	
	@Autowired 
	private X4WorkorderApiService       x4WorkorderApiService;
	
    @Value("${TargetUrgentiInterferenzaElive: Urgenti}")
    private String               		targetUrgentiInterferenzaElive;
    
    @Value("${TargetDifferibiliInterferenzaElive: Differibili}")
    private String               		targetDifferibiliInterferenzaElive;


	public void sendOdlToElive(String topic, KafkaOdlToEliveRequest kafkaMessage, String uuidResend) throws DataLayerException
	{
		Instant startDate	= Instant.now();
		log.info(" - start sendOdlToELive at : {}", startDate);
		String uuid 		= Uuid.randomUuid().toString();

		X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(EliveInterface.SEND_ODL_ELIVE.getName());
		x4EsitoIntRequest.setSource(Constants.EAM);
		x4EsitoIntRequest.setDest(Constants.ELIVE);
		x4EsitoIntRequest.setKafkatopic(topic);
		x4EsitoIntRequest.setMessage(kafkaMessage.toJson());
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());
		
		if(Assorted.notNull(uuidResend)) 
    	{
		    log.info("Retry detected for uuid={}", uuidResend);
    		x4EsitoIntRequest.setUniqueid(uuidResend);
    		X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
    		x4EsitoIntRequest.setParent(Assorted.notNull(parent) ? parent.getX4EsitoIntId() : null);
    	}
		else
    		x4EsitoIntRequest.setRetry(0L);
		
		EliveInvioOdlRequest request = new EliveInvioOdlRequest();
		if(Assorted.notNull(kafkaMessage.getWonum())) {
			request.setCodiceOdl(kafkaMessage.getWonum());
			x4EsitoIntRequest.setWonum(kafkaMessage.getWonum());
		} else {
		    log.error(" - No wonum found in Kafka message");
		    throw new RuntimeException("No wonum found in Kafka message");
		}
		if (Assorted.notNull(kafkaMessage.getJpnum())) {
		    request.setCodiceAttivita(kafkaMessage.getJpnum());
		} else {
		    log.error(" - No jpnum found in Kafka message");
		    throw new RuntimeException("No jpnum found in Kafka message");
		}
		if (Assorted.notNull(kafkaMessage.getTargstartdate())) {
		    request.setDataPianificazione(EamEtlUtils.convertToDateyyyyMMdd(kafkaMessage.getTargstartdate()));
		} else {
		    log.error(" - No data pianificazione found in Kafka message");
		    throw new RuntimeException("No data pianificazione found in Kafka message");
		}
		if(Assorted.notNull(kafkaMessage.getAssetnum())) {
			x4EsitoIntRequest.setAssetnum(String.valueOf(kafkaMessage.getAssetnum()));
		}
		List<AssetLocKafka> assets = kafkaMessage.getAssetlocrelation();
		if (!Assorted.isEmpty(assets) && Assorted.notNull(assets.get(0).getX3sapid())) {
		    request.setCodiceCampata(assets.get(0).getX3sapid());
		} else {
		    log.error(" - No asset or sapId found in Kafka message");
		    throw new RuntimeException("No asset or sapId found in Kafka message");
		}
	    String x3Ingrp = eliveService.getAssetByAssetNum(String.valueOf(kafkaMessage.getAssetnum()))
	            .stream()
	            .findFirst()
	            .map(Asset::getX3Ingrp)
	            .orElse(null);
	    request.setUnitaImpianto(x3Ingrp);
		try {
			log.info(" - Calling ELIVE with the following body: {}", request.toJson());
	    	long startTime = System.currentTimeMillis();
			EliveInvioOdlResponse response = eliveRestClient.sendOdlToElive(request);
	    	long endTime = System.currentTimeMillis();
			log.info(" - received the following object response: {}, httpcode: {}, duration: {} ms", response.toString(), response.getHttpCode(), endTime - startTime);
			if(response.getHttpCode() == 200 || response.getHttpCode() == 201) {
				x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());	
			} 
			else {
				x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
				x4EsitoIntRequest.setDesc_esito(response.toString());			
			}			
		}
		catch(Exception ex) {
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(ex.getMessage());			
		}
		finally {
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
		finally {
			log.info(" - end sendOdlToELive at : {}", Instant.now());
		}
	}

	public InvioProvvedimentiResponse invioProvvedimenti(InvioProvvedimentiRequest request) {
		Instant startDate	= Instant.now();
		log.info(" - start invio provvedimenti piante at : {}", startDate);
		InvioProvvedimentiResponse response = new InvioProvvedimentiResponse();
		log.info(" - body request : {}", request.toJson());
		BrokerEntityInfo brokerEntityInfoPublicationEndpoint = brokerEntities.getEndPoints().get("sendProvvedimentiPianteToEam");
		KafkaProvvedimentiPianteMessage kakfaMessage = new KafkaProvvedimentiPianteMessage();
		kakfaMessage.setUuid(UUID.randomUUID().toString());
		kakfaMessage.setReport(request);
		kafkaHandler.shoot(EOperation.CREATE, brokerEntityInfoPublicationEndpoint, List.of(kakfaMessage));
		response = enrichResponse(EOperation.CREATE, response, request);
		response.setEsito("OK");
		response.setHttpCode(HttpServletResponse.SC_CREATED);
		if (Assorted.notNull(response)) {
			response.fillHeaders(request);// NOSONAR
		}
		return response;
	}

	public void createInterferenza(String topic, KafkaProvvedimentiPianteMessage originRequest) throws EAMException {
		log.info(" - ELIVE - Start create interferenza");
	    KafkaProvvedimentiPianteMessage request = deepCopy(originRequest);
		try {
			if(Assorted.isNotEmpty(request.getReport().getRecord())) {
				List<RecordFileElive> recordsReportElive = request.getReport().getRecord();
				
				if (request.getReport().getNomeFile() == null || request.getReport().getNomeFile().isEmpty()) {
					log.error("no file name!");
					insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(), "nomefile non presente nella chiamata!");
					return;
				}

				boolean sperimentazioneReport = isSperimentazioneReport(request.getReport().getNomeFile());
				
				//	recupero sapid della linea del report
				String lineaReport = extractLineFromName(request.getReport().getNomeFile());
				if(lineaReport == null) {
					log.error("File name does not contain a valid code before the dash!");
					insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(), "File name does not contain a valid code before the dash!");
					return;					
				}
				
				//	recupero data
				Date dataUltimaRilevazione = extractDateFromNameFile(request.getReport().getNomeFile());
				if(dataUltimaRilevazione == null) {
					log.error("File name does not contain a valid date!");
					insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(), "File name does not contain a valid date!");
					return;					
				}
				
				//	recupero tutti i sapid delle campate, e con una query creo una mappa sapid/assetnum relativi
				List<String> distinctSapId = getDistinctSapId(recordsReportElive);	
				List<SapIdAssetNumResult> assetNumSapIdList = eliveService.getSapIdAndAssetNum(distinctSapId);
				log.info(" - result: {}", assetNumSapIdList);
				
				if (sperimentazioneReport) {
					setPrioritaEamDaReport(recordsReportElive);
				} else {
					// soltanto per i report standard.
					Optional<AlnvalueResult> alnvalueResultOpt = Optional.ofNullable(eliveService.findAlnvalueByLinea(lineaReport))
							.orElse(Collections.emptyList())
							.stream()
							.findFirst();

					if (alnvalueResultOpt.isEmpty()) {
						log.error(" - No voltage found for the following line: {}", lineaReport);
						insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(), "No voltage found for this line");
						return;
					}

					String valoreTensione = alnvalueResultOpt.get().getAlnvalue();
					if (!isValorizzato(valoreTensione)) {
						log.error(" - Voltage value is null or empty for the following line: {}", lineaReport);
						insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(),
								"Voltage value is null or empty for this line");
						return;
					}

					VoltageLevelLinea voltageLineaReport;
					try {
						voltageLineaReport = VoltageLevelLinea.fromValue(valoreTensione);
					} catch (IllegalArgumentException ex) {
						log.error(" - Unknown voltage value '{}' for the following line: {}", valoreTensione, lineaReport);
						insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(),
								"Unknown voltage value '" + valoreTensione + "' for this line");
						return;
					}
					log.info(" - Voltage for this line: {}", voltageLineaReport);

					Optional<Double> urgentDistanceOpt = eliveService.findRecordsByModello(voltageLineaReport.getTargetUrgente())
							.stream().map(X4CatModelloAsset::getN1).findFirst();

					if (urgentDistanceOpt.isEmpty()) {
						log.error(" - No record found for id modello: {}", voltageLineaReport.getTargetUrgente());
						insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(), "No record found for urgent target");
						return;
					}

					Double urgentDistance = urgentDistanceOpt.get();
					log.info(" - {} = {}", voltageLineaReport.getTargetUrgente(), urgentDistance);

					Optional<Double> deferrableDistanceOpt = eliveService.findRecordsByModello(voltageLineaReport.getTargetDifferibile())
							.stream().map(X4CatModelloAsset::getN1).findFirst();

					if (deferrableDistanceOpt.isEmpty()) {
						log.error(" - No record found for id modello: {}", voltageLineaReport.getTargetDifferibile());
						insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(), "No record found for deferrable target");
						return;
					}

					Double deferrableDistance = deferrableDistanceOpt.get();
					log.info(" - {} = {}", voltageLineaReport.getTargetDifferibile(), deferrableDistance);
					setPrioritaEam(recordsReportElive, voltageLineaReport, urgentDistance, deferrableDistance);
				}
				
				log.debug(" - Filtering record for update Odl");
	            List<RecordFileElive> flagOdlUpdateRecords = recordsReportElive.stream()
	                    .filter(o ->
	                    o.getDist_int() != null &&
	                    Double.compare(o.getDist_int(), Constants.ELIVE_DIST_INT_VALORE) == 0 &&
	                    	Constants.ELIVE_PRIORITA_VALORE.equals(o.getPriorita()) &&
	                    o.getTipologia() != null && o.getTipologia().isEmpty() &&
	                    isValorizzato(o.getCampata()) &&
	                    isValorizzato(o.getWonum())
	                )
	                .collect(Collectors.toList());

	            if (Assorted.isNotEmpty(flagOdlUpdateRecords)) {
	                log.info(" - Found {} records for ODL update", flagOdlUpdateRecords.size());
	                recordsReportElive.removeAll(flagOdlUpdateRecords);	               
	            }
						
				// validazione nel caso non ci sia un'associazione sapid/assetnum, e inserimento in tabella scarti
				handleRecordsScarti(request, recordsReportElive, assetNumSapIdList, flagOdlUpdateRecords);
				
				// inserimento in tabella DATIREPORTELIVE
				handleRecordsToInsert(recordsReportElive, request, flagOdlUpdateRecords);
				List<X4InterfstatRequest> interferenzaRequests = new ArrayList<>();
				if (sperimentazioneReport) {
					processSperimentazioneRecords(recordsReportElive, assetNumSapIdList, interferenzaRequests);
				} else {
					processNonGrigliaRecords(recordsReportElive, assetNumSapIdList, interferenzaRequests);
				}
				
				if(Assorted.isNotEmpty(interferenzaRequests)) {
					interferenzaRequests.stream().forEach(x -> x.setDataultimaril(Assorted.dateToString(dataUltimaRilevazione, Constants.MAXIMO_DATE_AND_TIME)));
					MaximoResponseArray<X4InterfstatResponse> response = sendInterferenzaRequest(interferenzaRequests);
					if(response.getHttpCode() > 300) {
						insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(), response.getError().getMessage());
						log.error("Error insert interference! It was not possible to insert in x4interf_wo!");
						return;
					}					
				}
				
				if(Assorted.isNotEmpty(recordsReportElive) || Assorted.isNotEmpty(flagOdlUpdateRecords)) {				
					Set<String> uniqueWonumSet = new HashSet<>();
					uniqueWonumSet.addAll(recordsReportElive.stream().map(RecordFileElive::getWonum).toList());
					if (Assorted.isNotEmpty(flagOdlUpdateRecords)) {
					    uniqueWonumSet.addAll(flagOdlUpdateRecords.stream().map(RecordFileElive::getWonum).toList());
					}
					
					List<String> listWonum = new ArrayList<>(uniqueWonumSet);		            
			        WorkorderGetResponse responseGetApi = recoveryHrefForUpdate(listWonum);
		            if (Assorted.notNull(responseGetApi) && Assorted.isNotEmpty(responseGetApi.getMember())) {
		                log.debug(" - Retrieved {} work orders for update", responseGetApi.getMember().size());
		                List<WorkorderRequest> ordersToSend = responseGetApi.getMember().stream().map(member -> {
		                    WorkorderRequest orderToUpdate = new WorkorderRequest();
		                    orderToUpdate.set_action(Constants.UPDATE);
		                    orderToUpdate.setHref(member.getHref());
		                    orderToUpdate.setX4flagfisico(1);
		                    orderToUpdate.setTargstartdate(Assorted.dateToString(member.getTargstartdate(), Constants.MAXIMO_DATE_AND_TIME));
		                    orderToUpdate.setTargcompdate(Assorted.dateToString(member.getTargcompdate(), Constants.MAXIMO_DATE_AND_TIME));
		                    return orderToUpdate;
		                }).collect(Collectors.toList());
		                
		                MaximoResponseArray<WorkorderBulkResponse> responsePostApi = x4WorkorderApiService.insertAndUpdateX4WorkorderBulk(ordersToSend, WorkorderBulkResponse[].class);
		                if (responsePostApi == null || responsePostApi.getHttpCode() > 300) {
		                    log.error("error in Odl update. Interferences created but odl flags not updated.");
							insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(), "Error detected in POST response. No orders flags were updated.");
		                    return;
		                } else {
		                    log.info("Updated flag for orders successfully!");
		                }
		            } 
		            else {
	                    log.error("error or odl not found during GET API. Interferences created but odl flags not updated.");
						insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(), "Error detected in GET href for Odl. No orders flags were updated.");
	                    return;
		            }
				}								
			}	
			else {
				log.info("empty report! No interference created or Elive acquisition odl updated");
			}
			insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.OK.name(), null);
		}
		catch (Exception e) {
			insertIntoX4EsitoInt(topic, originRequest, StatusX4Esito.KO.name(), e.getMessage());
			log.error(" - ELIVE - End create interferenza, error during the procedure");
			throw new EAMException(Catalog.EAMErrorMapping.EAM_0000.getEamCode(), e.getMessage());
		}
		log.info(" - ELIVE - End create interferenza ");
	}

	private void insertIntoX4EsitoInt(String topic, KafkaProvvedimentiPianteMessage request, String esito, String errore) {
		X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(EliveInterface.INVIO_PROVVEDIMENTI_TO_EAM.getName());
		x4EsitoIntRequest.setSource(Constants.ELIVE);
		x4EsitoIntRequest.setDest(Constants.EAM);
		x4EsitoIntRequest.setKafkatopic(topic);
		x4EsitoIntRequest.setMessage(request.toJson());
		x4EsitoIntRequest.setUniqueid(request.getUuid());
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(Instant.now()));
		x4EsitoIntRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
		x4EsitoIntRequest.setDesc_esito(errore);
		x4EsitoIntRequest.setEsito(esito);
		try {
			x4EsitoIntApiService.sendEsitoInt(x4EsitoIntRequest);
			log.debug(" - inserted the following object in the table x4esitoint: {}", x4EsitoIntRequest);
		} catch(java.lang.Exception e) {
			log.error("Rest Error when executing Maximo call to write X4ESITOINT result using this request: {}, occured exception {}", 
					Assorted.notNull(x4EsitoIntRequest)
					? x4EsitoIntRequest.toString() 
							: null,
							e.getMessage());
		}
	}

	private List<RecordFileElive> filterAndSeparateRecordsWithoutAssetNum(List<RecordFileElive> recordList, List<SapIdAssetNumResult> sapIdList, List<RecordFileElive> flagOdlUpdateRecords) {
	    List<RecordFileElive> recordsToSave = new ArrayList<>();
		// Creare una lista di campate con assetNum null
		List<String> sapIdsWithNullAssetNum = sapIdList.stream()
				.filter(sapId -> sapId.getTargetassetnum() == null)
				.map(SapIdAssetNumResult::getX3SapId)
				.collect(Collectors.toList());

		// Filtrare i record da salvare
		List<RecordFileElive> recordsToSaveOriginal = recordList.stream()
				.filter(record -> sapIdsWithNullAssetNum.contains(record.getCampata()))
				.collect(Collectors.toList());

		// Rimuovere i record dalla lista originale
		recordList.removeAll(recordsToSaveOriginal);
		
	    recordsToSave.addAll(recordsToSaveOriginal);

		
		if(Assorted.isNotEmpty(flagOdlUpdateRecords)) {
			// Filtra i record per il flag
			
			List<RecordFileElive> recordsToSaveFlag = flagOdlUpdateRecords.stream()
					.filter(record -> sapIdsWithNullAssetNum.contains(record.getCampata()))
					.collect(Collectors.toList());
			
			flagOdlUpdateRecords.removeAll(recordsToSaveFlag);
		    recordsToSave.addAll(recordsToSaveFlag);
			
		}
		
		return recordsToSave;
	}



	private String extractLineFromName(String fileName) {
		Pattern pattern = Pattern.compile("^([^\\-]+)-");
		Matcher matcher = pattern.matcher(fileName);
		if (matcher.find()) {
			return matcher.group(1);
		} else {
			return null;
		}
	}
	
	private Date extractDateFromNameFile(String nomeFile) {
        Pattern pattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4})");
        Matcher matcher = pattern.matcher(nomeFile);

        if (matcher.find()) {
            String dataString = matcher.group(1);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            LocalDate localDate = LocalDate.parse(dataString, formatter);

            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }

        return null;
    }

	private List<String> getDistinctSapId(List<RecordFileElive> recordsReportElive) {
		return recordsReportElive.stream()
				.map(RecordFileElive::getCampata)
				.distinct()
				.collect(Collectors.toList());
	}

	private void handleRecordsScarti(KafkaProvvedimentiPianteMessage request, List<RecordFileElive> recordsReportElive, List<SapIdAssetNumResult> assetNumSapIdList, List<RecordFileElive> flagOdlUpdateRecords) throws EAMException, DataLayerException {
	    // Filtro e separazione dei record senza assetNum
	    List<RecordFileElive> recordsScartiNoAssetNum = filterAndSeparateRecordsWithoutAssetNum(recordsReportElive, assetNumSapIdList, flagOdlUpdateRecords);
	    
	    // Filtro e separazione dei record senza coppia wonum e assetnum
	    List<RecordFileElive> recordsScartiNoPairAssetNumWonum = filterAndSeparateRecordsWithoutPairAssetNumWonum(recordsReportElive, assetNumSapIdList, flagOdlUpdateRecords);	    

	    // I record senza distanza non possono essere elaborati e vengono gestiti come scarti.
	    List<RecordFileElive> recordsScartiDistIntNull = recordsReportElive.stream()
	            .filter(record -> Assorted.isNull(record.getDist_int()))
	            .collect(Collectors.toList());
	    recordsReportElive.removeAll(recordsScartiDistIntNull);
	    
	    // Filtro dei record con prioritaEam null
	    List<RecordFileElive> recordsScartiPrioritaNull = recordsReportElive.stream()
	            .filter(record -> Assorted.isNull(record.getPrioritaEam()))
	            .collect(Collectors.toList());
	    
	    //Filto tipologia null
	    List<RecordFileElive> recordsScartiTipologiaNull = recordsReportElive.stream()
	            .filter(record -> Assorted.isNull(record.getTipologia()))
	            .collect(Collectors.toList());	    
	    

	    // Creazione lista scarti
	    List<ScartiReportElive> scartiToInsert = new ArrayList<>();

	    // Aggiungi scarti per i record senza assetNum
	    if (Assorted.isNotEmpty(recordsScartiNoAssetNum)) {
	        scartiToInsert.addAll(recordsScartiNoAssetNum.stream()
	                .map(record -> ScartiReportElive.fromRecordFileElive(record, request.getReport().getNomeFile(), "Non è presente un assetNum per questo sapId"))
	                .collect(Collectors.toList()));
	    }
	    
	    // Aggiungi scarti per i record senza coppia wonum e assetnum
	    if (Assorted.isNotEmpty(recordsScartiNoPairAssetNumWonum)) {
	        scartiToInsert.addAll(recordsScartiNoPairAssetNumWonum.stream()
	                .map(record -> ScartiReportElive.fromRecordFileElive(record, request.getReport().getNomeFile(), "il wonum indicato non esiste, non è associato a questa campata o non è presente nel record"))
	                .collect(Collectors.toList()));
	    }

	    // Aggiungi scarti per i record senza dist_int. La descrizione viene completata
	    // nella normalizzazione comune prima dell'inserimento.
	    if (Assorted.isNotEmpty(recordsScartiDistIntNull)) {
	        scartiToInsert.addAll(recordsScartiDistIntNull.stream()
	                .map(record -> ScartiReportElive.fromRecordFileElive(record, request.getReport().getNomeFile(), null))
	                .collect(Collectors.toList()));
	    }

	    // Aggiungi scarti per i record con prioritaEam null
	    if (Assorted.isNotEmpty(recordsScartiPrioritaNull)) {
	    	recordsReportElive.removeAll(recordsScartiPrioritaNull);
	        scartiToInsert.addAll(recordsScartiPrioritaNull.stream()
	                .map(record -> ScartiReportElive.fromRecordFileElive(record, request.getReport().getNomeFile(), "La priorità è null per questo record"))
	                .collect(Collectors.toList()));
	    }
	    
	    if(Assorted.isNotEmpty(recordsScartiTipologiaNull)) {
	    	recordsReportElive.removeAll(recordsScartiTipologiaNull);
	        scartiToInsert.addAll(recordsScartiTipologiaNull.stream()
	                .map(record -> ScartiReportElive.fromRecordFileElive(record, request.getReport().getNomeFile(), null))
	                .collect(Collectors.toList()));	    	
	    }

	    // Inserimento scarti
	    if (Assorted.isNotEmpty(scartiToInsert)) {
	    	scartiToInsert.forEach(scarto -> {
	    	        StringBuilder desc = new StringBuilder(scarto.getDescScarto() != null ? scarto.getDescScarto() : "");
	
	    	        if (scarto.getDistInt() == null) {
	    	            scarto.setDistInt(-1.0);
	    	            if (desc.length() > 0) desc.append(" | ");
	    	            desc.append("Dist int non presente per questo record");
	    	        }

	    	        if (scarto.getCampata() == null) {
	    	            scarto.setCampata("-");
	    	            if (desc.length() > 0) desc.append(" | ");
	    	            desc.append("Campata non presente per questo record");
	    	        }
	    	        if (scarto.getTipologia() == null || Assorted.isEmpty(scarto.getTipologia())) {
	    	            scarto.setTipologia(Collections.singletonList(-1));
	    	            if (desc.length() > 0) desc.append(" | ");
	    	            desc.append("Tipologia non presente per questo record");
	    	        }
	    	        if (scarto.getWonum() == null) {
	    	            scarto.setWonum("-");
	    	        }
	    	        if (desc.length() > 0) {
	    	            scarto.setDescScarto(desc.toString());
	    	        }
	    	        if(scarto.getPriorita() == null) {
	    	            scarto.setPriorita("-");

	    	        }
	    	    });
	        if (eliveService.findRecordsScartiReportEliveByNomeFile(request.getReport().getNomeFile()).size() == 0) {
	        	
	            eliveService.insertScartiReport(scartiToInsert);
	        } else {
	            log.info(" - records in the table SCARTIREPORTELIVE already inserted for this file");
	        }
	    }
	}

	private List<RecordFileElive> filterAndSeparateRecordsWithoutPairAssetNumWonum(
			List<RecordFileElive> recordsReportElive, List<SapIdAssetNumResult> assetNumSapIdList, List<RecordFileElive> flagOdlUpdateRecords) throws DataLayerException {
	    Set<String> pairWonumCampata = new HashSet<>();
	    
	    List<RecordFileElive> listDistinctWonumCampata = recordsReportElive.stream()
	        .filter(record -> pairWonumCampata.add(record.getCampata() + "-" + record.getWonum()))
	        .collect(Collectors.toList());
	    
	    if(Assorted.isNotEmpty(flagOdlUpdateRecords)) {
	    	listDistinctWonumCampata.addAll(flagOdlUpdateRecords.stream()
	    	        .filter(record -> pairWonumCampata.add(record.getCampata() + "-" + record.getWonum()))
	    	        .collect(Collectors.toList()));	    	
	    }
	    
	    List<RecordFileElive> listDistinctWonumCampataScarti = new ArrayList<>();
	    for (RecordFileElive record : listDistinctWonumCampata) {
	    	SapIdAssetNumResult match = assetNumSapIdList.stream()
	    	        .filter(s -> s.getX3SapId().equals(record.getCampata()))
	    	        .findFirst()
	    	        .orElse(null);
	    	
	    	 if (Assorted.notNull(match)) {
	    	        String assetNum = match.getTargetassetnum();
	    	        String wonum = record.getWonum();

	    	        if(Assorted.isEmpty(eliveService.getWorkrderByAssetNumAndWonum(assetNum, wonum))) {
	    	        	log.info(" - wonum: {} not present or not correctly associated with campata: {}", wonum, assetNum);
	    	        	listDistinctWonumCampataScarti.add(record);
	    	        }
	    	    }
	    }
	    if(Assorted.isNotEmpty(listDistinctWonumCampataScarti)) {
	    	Set<String> scartiCoppie = listDistinctWonumCampataScarti.stream()
		    	    .map(record -> record.getCampata() + "-" + record.getWonum())
		    	    .collect(Collectors.toSet());
		    List<RecordFileElive> recordScartati = new ArrayList<>();

		    recordsReportElive.removeIf(record -> {
		     String coppia = record.getCampata() + "-" + record.getWonum();
		     
		     if (scartiCoppie.contains(coppia)) {
		         recordScartati.add(record);
		         return true; 
		     }
		     return false;
			 });
		    if(Assorted.isNotEmpty(flagOdlUpdateRecords)) {
		    	flagOdlUpdateRecords.removeIf(record -> {
				     String coppia = record.getCampata() + "-" + record.getWonum();
				     
				     if (scartiCoppie.contains(coppia)) {
				         recordScartati.add(record);
				         return true; 
				     }
				     return false;
					 });
		    	
		    }	    
		    return recordScartati;
	    }
	    return null;
	}

	private void handleRecordsToInsert(List<RecordFileElive> recordsReportElive, KafkaProvvedimentiPianteMessage request, List<RecordFileElive> flagOdlUpdateRecords) throws EAMException {
		List<DatiReportElive> recordToInsert = recordsReportElive.stream()
				.map(record -> DatiReportElive.fromRecordFileElive(record, request.getReport().getNomeFile()))
				.collect(Collectors.toList());
		
		if(Assorted.isNotEmpty(flagOdlUpdateRecords)) {
			recordToInsert.addAll(flagOdlUpdateRecords.stream()
					.map(record -> DatiReportElive.fromRecordFileElive(record, request.getReport().getNomeFile()))
					.collect(Collectors.toList()));
		}
		
		// Inserimento Dati validi
		if(eliveService.findRecordsDatiReportEliveByNomeFile(request.getReport().getNomeFile()).size() == 0) {
			recordToInsert.forEach(record -> {

    	        if (record.getDistInt() == null) {
    	            record.setDistInt(-1.0);
    	        }
    	        if (record.getCampata() == null) {
    	            record.setCampata("-");
    	        }
    	        if (record.getTipologia() == null || Assorted.isEmpty(record.getTipologia())) {
    	            record.setTipologia(Collections.singletonList(-1));
    	        }
    	        if (record.getWonum() == null) {
    	            record.setWonum("-");
    	        }
    	        if(record.getPriorita() == null) {
    	            record.setPriorita("-");
    	        }
    	    });
			eliveService.insertDatiReport(recordToInsert);
		}
		else {
			log.info(" - records in the table DATIREPORTELIVE already inserted for this file");
		}

	}

	private void setPrioritaEam(List<RecordFileElive> recordsReportElive, VoltageLevelLinea voltageLineaReport, Double urgentDistance, Double deferrableDistance) {
		recordsReportElive.forEach(record -> {
			if(Assorted.notNull(record.getDist_int())) {
				record.setPrioritaEam(voltageLineaReport.determineGravita(record.getDist_int(), 
						  urgentDistance, 
						  deferrableDistance));
				log.debug("- DATI PRIORITA' CALCOLATA - Campata: {}, Wonum: {}, Priorita': {}, Distanza Int: {}, Target Urgente: {}, Target Differibile: {}",
				record.getCampata(), 
				record.getWonum(), 
				record.getPriorita(), 
				record.getDist_int(), 
				urgentDistance, 
				deferrableDistance);			
			}
			});
	}

	private void setPrioritaEamDaReport(List<RecordFileElive> recordsReportElive) {
		recordsReportElive.forEach(record -> {
			String prioritaEam = normalizzaPrioritaReport(record.getPriorita());
			record.setPrioritaEam(prioritaEam);
			log.debug("- PRIORITA' DA REPORT SPERIMENTALE - Campata: {}, Wonum: {}, Priorita ricevuta: {}, Priorita EAM: {}",
					record.getCampata(), record.getWonum(), record.getPriorita(), prioritaEam);
		});
	}

	private static String normalizzaPrioritaReport(String priorita) {
		if (!isValorizzato(priorita)) {
			return null;
		}

		switch (priorita.trim().toUpperCase(Locale.ROOT)) {
		case "U":
		case "URGENTE":
		case Constants.URGENTI:
			return Constants.URGENTI;
		case "D":
		case "DIFFERIBILE":
		case Constants.DIFFERIBILI:
			return Constants.DIFFERIBILI;
		default:
			return null;
		}
	}

	static Map<SperimentazioneAggregationKey, InterferenzaStats> aggregateSperimentazioneRecords(
			List<RecordFileElive> recordsReportElive) {
		Map<SperimentazioneAggregationKey, InterferenzaStats> result = new LinkedHashMap<>();
		if (recordsReportElive == null) {
			return result;
		}

		recordsReportElive.stream()
				.filter(Objects::nonNull)
				.filter(record -> record.getTipologia() != null
						&& record.getTipologia().contains(Constants.TIPOLOGIA_INTERFERENZA_ELIVE))
				.filter(record -> record.getPrioritaEam() != null)
				.filter(record -> isDensitaValida(record.getDensita()))
				.forEach(record -> {
					String densita = record.getDensita().trim().toUpperCase(Locale.ROOT);
					SperimentazioneAggregationKey key = new SperimentazioneAggregationKey(
							record.getCampata(), record.getPrioritaEam(), record.getWonum());
					InterferenzaStats stats = result.computeIfAbsent(key,
							ignored -> new InterferenzaStats(0, null));
					switch (densita) {
					case Constants.DENSITA_ALTA:
						stats.setCountDensitaAlta(stats.getCountDensitaAlta() + 1);
						break;
					case Constants.DENSITA_MEDIA:
						stats.setCountDensitaMedia(stats.getCountDensitaMedia() + 1);
						break;
					case Constants.DENSITA_BASSA:
						stats.setCountDensitaBassa(stats.getCountDensitaBassa() + 1);
						break;
					default:
						break;
					}
					if (record.getDist_int() != null
							&& (stats.getMinDistInt() == null || record.getDist_int() < stats.getMinDistInt())) {
						stats.setMinDistInt(record.getDist_int());
					}
				});

		return result;
	}

	private void processSperimentazioneRecords(List<RecordFileElive> recordsReportElive,
			List<SapIdAssetNumResult> assetNumSapIdList,
			List<X4InterfstatRequest> interferenzaRequests) throws EAMException {
		Map<SperimentazioneAggregationKey, InterferenzaStats> aggregazioni =
				aggregateSperimentazioneRecords(recordsReportElive);
		Map<String, X4EliveInterfConfig> configurazioni =
				loadConfigurazioniInterferenzeSperimentazione();
		Map<String, X4CatInterf> catalogoInterferenze = new HashMap<>();

		for (Map.Entry<SperimentazioneAggregationKey, InterferenzaStats> entry : aggregazioni.entrySet()) {
			SperimentazioneAggregationKey key = entry.getKey();
			InterferenzaStats stats = entry.getValue();
			Optional<SapIdAssetNumResult> optionalAssetNum = assetNumSapIdList.stream()
					.filter(item -> item.getX3SapId().equals(key.campata()))
					.findFirst();

			if (optionalAssetNum.isEmpty()) {
				continue;
			}

			X4EliveInterfConfig configurazione = configurazioni.get(key.prioritaEam());
			if (configurazione == null || !isValorizzato(configurazione.getIdInterferenza())) {
				throw new IllegalStateException("Configurazione interferenza sperimentazione non trovata per priorita "
						+ key.prioritaEam());
			}

			String interferenzaId = configurazione.getIdInterferenza();
			X4CatInterf interferenza = catalogoInterferenze.get(interferenzaId);
			if (interferenza == null) {
				interferenza = eliveService.getX4CatInterfByIdInterferenza(interferenzaId);
				if (interferenza == null) {
					throw new IllegalStateException("Interferenza non presente in X4CATINTERF: " + interferenzaId);
				}
				catalogoInterferenze.put(interferenzaId, interferenza);
			}

			X4InterfstatRequest interferenzaRequest = new X4InterfstatRequest();
			interferenzaRequest.setAssetnum(optionalAssetNum.get().getTargetassetnum());
			interferenzaRequest.setStato(Constants.VALIDATA);
			interferenzaRequest.setSiteid(Constants.SIT01);
			interferenzaRequest.setDescription(interferenza.getDescription());
			interferenzaRequest.setIdinterferenza(interferenza.getIdInterferenza());
			interferenzaRequest.setGravita(Constants.URGENTI.equals(key.prioritaEam())
					? targetUrgentiInterferenzaElive : targetDifferibiliInterferenzaElive);
			interferenzaRequest.setWonum(key.wonum());
			interferenzaRequest.setX4_distanza(stats.getMinDistInt());
			interferenzaRequest.setX4densalta(Long.valueOf(stats.getCountDensitaAlta()));
			interferenzaRequest.setX4densmedia(Long.valueOf(stats.getCountDensitaMedia()));
			interferenzaRequest.setX4densbassa(Long.valueOf(stats.getCountDensitaBassa()));
			interferenzaRequests.add(interferenzaRequest);
			log.info(" - Interferenza sperimentazione aggregata per campata: {}, priorita: {}, wonum: {}, "
					+ "densitaAlta: {}, densitaMedia: {}, densitaBassa: {}, minDistInt: {}",
					key.campata(), key.prioritaEam(), key.wonum(),
					stats.getCountDensitaAlta(), stats.getCountDensitaMedia(), stats.getCountDensitaBassa(),
					stats.getMinDistInt());
		}
	}

	private Map<String, X4EliveInterfConfig>
			loadConfigurazioniInterferenzeSperimentazione() throws EAMException {
		List<X4EliveInterfConfig> configurazioni = Optional
				.ofNullable(eliveService.findActiveEliveInterfConfig(Constants.ELIVE_FLUSSO_SPERIMENTALE))
				.orElse(Collections.emptyList());
		Map<String, X4EliveInterfConfig> result = new LinkedHashMap<>();

		for (X4EliveInterfConfig configurazione : configurazioni) {
			if (!isValorizzato(configurazione.getPriorita())) {
				throw new IllegalStateException("Configurazione interferenza sperimentazione non valida con id "
						+ configurazione.getId());
			}
			if (!isValorizzato(configurazione.getFlusso())
					|| !Constants.ELIVE_FLUSSO_SPERIMENTALE.equalsIgnoreCase(configurazione.getFlusso().trim())) {
				throw new IllegalStateException("Configurazione interferenza non valida per il flusso sperimentale con id "
						+ configurazione.getId());
			}
			String key = configurazione.getPriorita().trim().toUpperCase(Locale.ROOT);
			if (result.put(key, configurazione) != null) {
				throw new IllegalStateException("Configurazione interferenza duplicata per priorita "
						+ key + " e flusso " + Constants.ELIVE_FLUSSO_SPERIMENTALE);
			}
		}
		return result;
	}

	private void processNonGrigliaRecords(List<RecordFileElive> recordsReportElive, List<SapIdAssetNumResult> assetNumSapIdList, List<X4InterfstatRequest> interferenzaRequests) throws RuntimeException, EAMException {
		Map<String, Map<String, Map<Integer, Map<String, InterferenzaStats>>>> result = recordsReportElive.stream()
		        .flatMap(record -> record.getTipologia().stream()
		                .filter(tipologia -> tipologia == 1) // Filtra solo per catenaria verticale
		                .filter(tipologia -> Assorted.notNull(record.getPrioritaEam()))
		                .map(tipologia -> new AbstractMap.SimpleEntry<>(tipologia, record)))
		        .collect(Collectors.groupingBy(
		                entry -> entry.getValue().getCampata(), // Raggruppa per campata
		                Collectors.groupingBy(
		                        entry -> entry.getValue().getWonum(), // Raggruppa per wonum
		                        Collectors.groupingBy(
		                                Map.Entry::getKey, // Raggruppa per tipologia
		                                Collectors.groupingBy(
		                                        entry -> entry.getValue().getPrioritaEam(), // Raggruppa per priorità
		                                        Collectors.collectingAndThen(
		                                                Collectors.toList(),
		                                                list -> new InterferenzaStats(
		                                                        list.size(),
		                                                        list.stream()
		                                                                .map(entry -> entry.getValue().getDist_int())
		                                                                .filter(Objects::nonNull)
		                                                                .min(Double::compareTo)
		                                                                .orElse(null)
		                                                )
		                                        )
		                                )
		                        )
		                )
		        ));
		
		Double target = eliveService.findRecordsByModello(Constants.DEFINIZIONE_AREE_BOSCHIVE).stream()
			    .findFirst()
			    .orElseThrow(() -> new RuntimeException("No target for Definzione aree boschive found"))
			    .getN1();
		log.info(" - target = {}", target);

		result.forEach((campata, prioritàMap) -> {
			log.info(" - Campata: {}", campata);

			Optional<SapIdAssetNumResult> optionalAssetNum = assetNumSapIdList.stream()
					.filter(item -> item.getX3SapId().equals(campata))
					.findFirst();

			optionalAssetNum.ifPresent(assetNum -> {
				prioritàMap.forEach((wonum, mapTipologiaPrioritàCount) -> {
					mapTipologiaPrioritàCount.forEach((tipologia, mapPrioritàCount) -> {
						mapPrioritàCount.forEach((priorità, stats) -> {
							X4InterfstatRequest interferenzaRequest = new X4InterfstatRequest();
							interferenzaRequest.setAssetnum(assetNum.getTargetassetnum());
							interferenzaRequest.setStato(Constants.VALIDATA);
							interferenzaRequest.setSiteid(Constants.SIT01);
						    int count = stats.getCount();
						    Double minDistInt = stats.getMinDistInt();
							log.info(" - tipologia: {}, Priorita: {}, CountPriorita: {}, minDistInt: {}", tipologia, priorità, count, minDistInt);
							switch(tipologia) {
							case 1:
								try {
									setInterferenceDetailsVerticale(interferenzaRequest, priorità, count, target, wonum, minDistInt);
								} catch (Exception e) {
									throw new IllegalArgumentException("Unable to retrieve idinterferenza from x4catinterf");
								}
								break;
							case 2:
								try {
									setInterferenceDetailsSbandata(interferenzaRequest, priorità, count, target, wonum, minDistInt);
								} catch (Exception e) {
									throw new IllegalArgumentException("Unable to retrieve idinterferenza from x4catinterf");
								}
								break;
							case 3:
								try {
									setInterferenceDetailsRibaltamento(interferenzaRequest, priorità, count, target, wonum, minDistInt);
								} catch (Exception e) {
									throw new IllegalArgumentException("Unable to retrieve idinterferenza from x4catinterf");
								}
								break;
							default:
								throw new IllegalArgumentException("tipologia number does not exist");
							}
							log.info(" - created the following 'interferenzaRequest' object: {}", interferenzaRequest);
							interferenzaRequests.add(interferenzaRequest);
						});	                    
					});
				});
			});
		});
	}


	private void setInterferenceDetailsVerticale(X4InterfstatRequest interferenzaRequest, String prioritàEam, long count, Double target, String wonum, Double minDistInt) throws EAMException {
	    String interferenzaId = (count < target) ? eliveInterferenzeProperties.getInterferenzaVerticaleBasso() : eliveInterferenzeProperties.getInterferenzaVerticaleAlto();

	    X4CatInterf interferenza = eliveService.getX4CatInterfByIdInterferenza(interferenzaId);

	    interferenzaRequest.setDescription(interferenza.getDescription());
	    interferenzaRequest.setIdinterferenza(interferenza.getIdInterferenza());
	    String gravita = prioritàEam.equals(Constants.URGENTI) ? targetUrgentiInterferenzaElive : targetDifferibiliInterferenzaElive;
	    interferenzaRequest.setGravita(gravita);
	    interferenzaRequest.setWonum(wonum);
	    interferenzaRequest.setNumero_collisioni(count);
	    interferenzaRequest.setX4_distanza(minDistInt);
	}

	private void setInterferenceDetailsSbandata(X4InterfstatRequest interferenzaRequest, String prioritàEam, long count, Double target, String wonum, Double minDistInt) throws EAMException {
	    String interferenzaId = (count < target) ? eliveInterferenzeProperties.getInterferenzaSbandataBasso() : eliveInterferenzeProperties.getInterferenzaSbandataAlto();

	    X4CatInterf interferenza = eliveService.getX4CatInterfByIdInterferenza(interferenzaId);

	    interferenzaRequest.setDescription(interferenza.getDescription());
	    interferenzaRequest.setIdinterferenza(interferenza.getIdInterferenza());
	    String gravita = prioritàEam.equals(Constants.URGENTI) ? targetUrgentiInterferenzaElive : targetDifferibiliInterferenzaElive;
	    interferenzaRequest.setGravita(gravita);
	    interferenzaRequest.setWonum(wonum);
	    interferenzaRequest.setNumero_collisioni(count);
	    interferenzaRequest.setX4_distanza(minDistInt);
	}

	private void setInterferenceDetailsRibaltamento(X4InterfstatRequest interferenzaRequest, String prioritàEam, long count, Double target, String wonum, Double minDistInt) throws EAMException {
	    String interferenzaId = (count < target) ? eliveInterferenzeProperties.getInterferenzaRibaltamentoBasso() : eliveInterferenzeProperties.getInterferenzaRibaltamentoAlto();

	    X4CatInterf interferenza = eliveService.getX4CatInterfByIdInterferenza(interferenzaId);

	    interferenzaRequest.setDescription(interferenza.getDescription());
	    interferenzaRequest.setIdinterferenza(interferenza.getIdInterferenza());
	    String gravita = prioritàEam.equals(Constants.URGENTI) ? targetUrgentiInterferenzaElive : targetDifferibiliInterferenzaElive;
	    interferenzaRequest.setGravita(gravita);
	    interferenzaRequest.setWonum(wonum);
	    interferenzaRequest.setNumero_collisioni(count);
	    interferenzaRequest.setX4_distanza(minDistInt);
	}
	
	private MaximoResponseArray<X4InterfstatResponse> sendInterferenzaRequest(List<X4InterfstatRequest> interferenzaRequest) {
		try {
			log.info(" - creating the following interference: {}", interferenzaRequest);
			return x4InterfstatApiService.insertAndUpdateX4InterfstatBulk(interferenzaRequest, X4InterfstatResponse[].class);
		} catch (Exception e) {
			log.error("Rest Error when executing Maximo call to write X4INTERFSTAT result using this request: {}, occured exception {}", 
					interferenzaRequest != null ? interferenzaRequest.toString() : null,
							e.getMessage());
			return null;
		}
	}
	
	private boolean isSperimentazioneReport(String nomeFile) {
		return Optional.ofNullable(nomeFile)
				.map(file -> file.toLowerCase(Locale.ROOT)
						.contains(Constants.REPORT_TAGLIO_PIANTE_GRIGLIA.toLowerCase(Locale.ROOT)))
				.orElse(false);
	}

	private static boolean isDensitaValida(String densita) {
		if (densita == null) {
			return false;
		}
		String normalized = densita.trim().toUpperCase(Locale.ROOT);
		return Constants.DENSITA_BASSA.equals(normalized)
				|| Constants.DENSITA_MEDIA.equals(normalized)
				|| Constants.DENSITA_ALTA.equals(normalized);
	}

	public WorkorderGetResponse recoveryHrefForUpdate(List<String> listWonum) throws RestException, EAMException {
		WorkorderGetResponse responseGetApi;
		log.info("Calling API to retrieve existing workorders.");
		responseGetApi = x4WorkorderApiService.retrieveWorkorders(listWonum);
		log.info("API GET call completed with HTTP code: {}", responseGetApi.getHttpCode());

		if (responseGetApi.getHttpCode() >= 300) {
		    log.error("Error in retrieving workorders. HTTP code: {}", responseGetApi.getHttpCode());
		    return null;
		}
		return responseGetApi;
	}
	
    private static boolean isValorizzato(String s) {
        return s != null && !s.trim().isEmpty();
    }
    
    private KafkaProvvedimentiPianteMessage deepCopy(KafkaProvvedimentiPianteMessage source) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(source, KafkaProvvedimentiPianteMessage.class);
    }

}
