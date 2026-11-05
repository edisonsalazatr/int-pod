package it.terna.nas.service.exposure.handler;

import static it.terna.nas.service.material.utility.Constants.DELETE;
import static it.terna.nas.service.material.utility.Constants.EAM;
import static it.terna.nas.service.material.utility.Constants.GEOCALL;
import static it.terna.nas.service.material.utility.Constants.SIT01;

import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.bind.JAXBElement;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.kafka.common.Uuid;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.ws.client.WebServiceIOException;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;

import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Values;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaBloccoOdlRequest;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaBloccoOdlResponse2;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaOperazioneOdlRequest;
import it.terna.kernel.soap.clientgen.odl.annulla.AnnullaOperazioneOdlResponse2;
import it.terna.kernel.soap.clientgen.odl.changestatusodl.ChangeStatusResponseType;
import it.terna.kernel.soap.clientgen.odl.changestatusodl.ChangeStatusType;
import it.terna.kernel.soap.clientgen.odl.consuntivazione.ChiusuraOdlResponseType;
import it.terna.kernel.soap.clientgen.odl.consuntivazione.ChiusuraOdlType;
import it.terna.kernel.soap.clientgen.odl.consuntivazione.ListaLabTransType;
import it.terna.kernel.soap.clientgen.odl.consuntivazione.ListaOperazioneSchedulazioneOdlType;
import it.terna.kernel.soap.clientgen.odl.consuntivazione.SchedulazioneOdlResponseType;
import it.terna.kernel.soap.clientgen.odl.consuntivazione.SchedulazioneOdlType;
import it.terna.kernel.soap.clientgen.odl.createodlv2.CreateOdlRequest;
import it.terna.kernel.soap.clientgen.odl.createodlv2.CreateOdlResponse;
import it.terna.kernel.soap.clientgen.odl.createodlv2.Operazione;
import it.terna.kernel.soap.clientgen.odl.createodlv2.Risorse;
import it.terna.kernel.soap.clientgen.odl.createodlv2.Titris;
import it.terna.kernel.soap.clientgen.odl.createodlv2.UnitaImpianti;
import it.terna.kernel.soap.clientgen.odl.inviotipoimpianto.DatiTipoImpiantoRequest;
import it.terna.kernel.soap.clientgen.odl.inviotipoimpianto.DatiTipoImpiantoResponse;
import it.terna.kernel.soap.clientgen.odl.storno.StornoRequest;
import it.terna.kernel.soap.clientgen.odl.storno.StornoResponse;
import it.terna.nas.service.dal.entity.eam.ClassStructureHierarchy;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.dal.entity.eam.GeoCallWorkOrder;
import it.terna.nas.service.dal.entity.eam.SequenceCodiceGruppo;
import it.terna.nas.service.dal.entity.eam.WorkorderHierarchyRestricted;
import it.terna.nas.service.dal.entity.eam.WpLaborTitris;
import it.terna.nas.service.dal.entity.eam.X4EsitoInt;
import it.terna.nas.service.dal.entity.eam.X4GeoCallAddress;
import it.terna.nas.service.dal.entity.eam.X4GeoCallAddressRestricted;
import it.terna.nas.service.dal.service.ClassificationService;
import it.terna.nas.service.dal.service.WorkorderQueryService;
import it.terna.nas.service.dal.service.WpLaborTitrisQueryService;
import it.terna.nas.service.dal.service.X4EsitoIntApiService;
import it.terna.nas.service.dal.service.X4EsitoIntQueryService;
import it.terna.nas.service.dal.service.X4GeoCallAddressQueryService;
import it.terna.nas.service.dal.service.X4NotifyScheduleOdlApiService;
import it.terna.nas.service.dal.service.X4WorkorderApiService;
import it.terna.nas.service.dal.service.sap.X4LabtransApiService;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.client.OdLClient;
import it.terna.nas.service.exposure.ws.maximo.JwtTokenResponse;
import it.terna.nas.service.exposure.ws.maximo.LabTransItem;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponseArray;
import it.terna.nas.service.exposure.ws.maximo.X4NotifyScheduleResponse;
import it.terna.nas.service.exposure.ws.maximo.X4WorkorderRequest;
import it.terna.nas.service.exposure.ws.maximo.X4WorkorderResponse;
import it.terna.nas.service.material.dto.ChangeStatusDTO;
import it.terna.nas.service.material.dto.KafkaWorkOrderDTO;
import it.terna.nas.service.material.dto.KafkaX4APIClassstructure;
import it.terna.nas.service.material.dto.StornoDTO;
import it.terna.nas.service.material.dto.WorkOrderDTO;
import it.terna.nas.service.material.dto.X4EsitoIntRequest;
import it.terna.nas.service.material.utility.Catalog.ClassificationType;
import it.terna.nas.service.material.utility.Catalog.EAMCausaleEsitoMapping;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EAMStatusFatherMapping;
import it.terna.nas.service.material.utility.Catalog.EAMStatusMapping;
import it.terna.nas.service.material.utility.Catalog.EGeoTipoOperazione;
import it.terna.nas.service.material.utility.Catalog.GeoCallInterface;
import it.terna.nas.service.material.utility.Catalog.StatusX4Esito;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import it.terna.nas.service.material.utility.EamEtlUtils;
import it.terna.nas.service.material.utility.XmlUtility;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GeoCallHandler extends AbstractHandler
{	
	@Autowired
	private ClassificationService classificationService;
	
	@Autowired
	private X4EsitoIntApiService x4EsitoIntApiService;
	
	@Autowired
	private X4EsitoIntQueryService x4EsitoIntQueryService;
	
	@Autowired
	private X4GeoCallAddressQueryService x4GeoCallAddressQueryService;
	
	@Autowired
	private X4WorkorderApiService x4WorkorderApiService;
	
	@Autowired
	private WorkorderQueryService workOrderQueryService;
	
	@Autowired
	private WpLaborTitrisQueryService wpLaborTitrisQueryService;
	
	@Autowired
	private OdLClient odlClient;

	@Autowired
	private X4LabtransApiService x4LabtransApiService;
	
	@Autowired
	private X4NotifyScheduleOdlApiService x4NotifyScheduleOdlApiService;
	
	public X4WorkorderRequest createNotifyChangeStatusRequest(ChangeStatusDTO odl, ChangeStatusType changeStatusType) 
			throws EAMException 
	{
		X4WorkorderRequest req = new X4WorkorderRequest();
		req.setHref(odl.getHref());
		req.setWonum(odl.getWonum());
		req.setX3notechiusura(changeStatusType.getNOTACHIUSURA());
		req.setChangedate(EamEtlUtils.esitoDate(changeStatusType.getDATACAMBIOSTATO()));
		req.setX4statogeocall(odl.getReqStatus());
		EAMStatusMapping status = EAMStatusMapping.findStatus(odl.getReqStatus());
		if(Assorted.isNull(status))
			throw new EAMException(EAMErrorMapping.EAM_0002.getEamCode(), EAMErrorMapping.EAM_0002.getEamCode());
		Boolean isClosed = EAMStatusMapping.CHIUSO.getEamStatus().equals(status.getEamStatus());
		if(odl.getFather() && isClosed) 
		{
			req = closureType(req, status);
		}
		else if (!odl.getFather() && isClosed)
		{
			EAMCausaleEsitoMapping esito = EAMCausaleEsitoMapping.findIfNonEseguito(changeStatusType.getCAUSALEESITO());
			req.setStatus(Objects.nonNull(esito) ? esito.getStatus() : null);
			req = closureType(req, status);
		}
		else if(!odl.getFather() && !isClosed)
			req.setStatus(status.getEamStatus());
		
		if(!odl.getFather() && Assorted.isEmpty(odl.getParent()))
		{
			req.setStatus(changeStatusType.getSTATOORDINE().equals(EAMStatusMapping.INVIATO.getGeoCallStatus()) 
					  	  ? EAMStatusFatherMapping.INVIATO.getFatherStatus() 
					  	  : req.getStatus());
		}
		
		return req;
	}
	
	public X4WorkorderRequest closureType(X4WorkorderRequest originalReq, EAMStatusMapping status) 
	{
		X4WorkorderRequest newReq = originalReq;
		if(EAMStatusMapping.CHIUSOTEC.equals(status))
			newReq.setX4geocallclosed(Boolean.TRUE);
		else if (EAMStatusMapping.CHIUSO.equals(status))
			newReq.setWoeq3(Boolean.TRUE);
		return newReq;
	}

	public SchedulazioneOdlResponseType scheduleOdl(JAXBElement<SchedulazioneOdlType> requestJaxb)
	{
		long startTime 					= System.currentTimeMillis();
		SchedulazioneOdlType request 	= requestJaxb.getValue();
		Instant startDate						= Instant.now();
		String uuid 							= Uuid.randomUuid().toString();
		
		SchedulazioneOdlResponseType response	= new SchedulazioneOdlResponseType();
		response.setESITO(StatusX4Esito.OK.name());
		
		X4EsitoIntRequest x4EsitoIntRequest 	= new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(GeoCallInterface.SCHEDULE_ODL_INTERFACE.getName());
		x4EsitoIntRequest.setSource(Constants.GEOCALL);
		x4EsitoIntRequest.setDest(Constants.EAM);
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setMessage(XmlUtility.convertToString(SchedulazioneOdlType.class.getPackageName(), requestJaxb));
		x4EsitoIntRequest.setWonum(request.getCODICEORDINEESTERNO());
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			Map<String, Object> scheduleMap = createScheduleRequestsList(request);
			String scheduleJson = mapper.writeValueAsString(scheduleMap);
			X4NotifyScheduleResponse scriptResponse = x4NotifyScheduleOdlApiService.launchNotifyScheduleOdlScript(scheduleJson);
			if(Assorted.isNull(scriptResponse))
				throw new EAMException(EAMErrorMapping.EAM_0000.getEamCode(), EAMErrorMapping.EAM_0000.getErrorDescription());
			else if (Assorted.notNull(scriptResponse) && scriptResponse.getHttpCode() != HttpStatus.OK.value()) 
				throw new EAMException(EAMErrorMapping.EAM_0000.getEamCode(), EamEtlUtils.errorGenericMessage(scriptResponse.getError()));
			else if (Assorted.notNull(scriptResponse) && StatusX4Esito.KO.name().equalsIgnoreCase(scriptResponse.getEsito()))
				throw new EAMException(scriptResponse.getCodiceErrore(), scriptResponse.getDescErrore());
		} catch(EAMException q) {
			log.error(q.getMessage());
			response.setESITO(StatusX4Esito.KO.name());
			response.setCODICEERRORE(q.getErrorCode());
			response.setDESCRIZIONEERRORE(q.getMessage());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(q.getErrorCode());
			x4EsitoIntRequest.setDesc_esito(q.getMessage());
		} catch (WebServiceIOException | HttpClientErrorException q) {
			log.error("SOAP call failed (possible timeout or unexpected response): {}", q.getMessage(), q);
			response.setESITO(StatusX4Esito.KO.name());
			response.setCODICEERRORE(EAMErrorMapping.EAM_0000.getEamCode());
			response.setDESCRIZIONEERRORE("SOAP client error: " + q.getMessage());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito("SOAP call failed: " + q.getMessage());
		} catch(java.lang.Exception q) {
			log.error("Generic Error: " + q.getMessage(), q);
			response.setESITO(StatusX4Esito.KO.name());
			response.setCODICEERRORE(EAMErrorMapping.EAM_0000.getEamCode());
			response.setDESCRIZIONEERRORE(EAMErrorMapping.EAM_0000.getErrorDescription());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + q.getMessage());
		} finally {
			
			x4EsitoIntRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
			
	    	try {
	    		x4EsitoIntApiService.sendEsitoInt(x4EsitoIntRequest);
	    	} catch(java.lang.Exception e) {
				log.error("Rest Error when executing Maximo call to write X4ESITOINT result using this request: {}, occured exception {}", 
						  Assorted.notNull(x4EsitoIntRequest)
						  ? x4EsitoIntRequest.toString() 
						  : null,
						  e.getMessage());
			}
		}
		response.setDESCRIZIONEERRORE(EamEtlUtils.trimString(response.getDESCRIZIONEERRORE(), 2000));
		long endTime = System.currentTimeMillis();
		log.info("end scheduleOdl({}) |{}|", request.getCODICEORDINEESTERNO(), endTime - startTime);
		return response;
	}

	private Map<String, Object> createScheduleRequestsList(SchedulazioneOdlType request) 
	{
	    Map<String, Object> finalMap = new HashMap<>();
	    
	    finalMap.put("preposto", request.getPREPOSTO());
	    finalMap.put("matricole",
	        Assorted.notNull(request.getSQUADRA()) && Assorted.isNotEmpty(request.getSQUADRA().getMATRICOLA())
	            ? request.getSQUADRA().getMATRICOLA()
	            : null
	    );
	    
	    List<Map<String, Object>> odl = new ArrayList<>();
	    Map<String, Object> fatherOdl = new HashMap<>();

	    ListaOperazioneSchedulazioneOdlType firstOperation = request.getOPERAZIONI().get(0);

	    String iniSched = EamEtlUtils.esitoDate(firstOperation.getDATAINISCHEDULATA());
	    String finSched = EamEtlUtils.esitoDate(firstOperation.getDATAFINESCHEDULATA());

	    fatherOdl.put("wonum", request.getCODICEORDINEESTERNO());
	    fatherOdl.put("dataIniSched", iniSched);
	    fatherOdl.put("dataFinSched", finSched);

	    odl.add(fatherOdl);

	    for (ListaOperazioneSchedulazioneOdlType op : request.getOPERAZIONI()) {
	    	Map<String, Object> operation = new HashMap<>();
	    	operation.put("wonum", op.getCODICEPOSIZIONE());
	    	operation.put("dataIniSched", EamEtlUtils.esitoDate(op.getDATAINISCHEDULATA()));
	    	operation.put("dataFinSched", EamEtlUtils.esitoDate(op.getDATAFINESCHEDULATA()));
	    	odl.add(operation);
	    }

	    finalMap.put("odlList", odl);

	    return finalMap;
	}

	public void sendTypeAsset(String topic, String uuidResend, String payload) 
	{
		Instant startDate								= Instant.now();
		String uuid 									= Uuid.randomUuid().toString();
		Long startTime									= System.currentTimeMillis();
		DatiTipoImpiantoResponse unsuccessfullResponse 	= null;
		X4EsitoIntRequest x4EsitoIntRequest 			= new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(GeoCallInterface.SEND_ASSET_TYPE_INTERFACE.getName());
		x4EsitoIntRequest.setSource(Constants.EAM);
		x4EsitoIntRequest.setDest(Constants.GEOCALL);
		x4EsitoIntRequest.setKafkatopic(topic);
		x4EsitoIntRequest.setMessage(payload);
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());
		
		try {
			
	        if(Assorted.notNull(payload)) 
	        {
	        	if(Assorted.notNull(uuidResend))
	        	{
	        		x4EsitoIntRequest.setUniqueid(uuidResend);
	        		X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
	        		x4EsitoIntRequest.setParent(Assorted.notNull(parent) ? parent.getX4EsitoIntId() : null);
	        	}
	        	else
	        		x4EsitoIntRequest.setRetry(0L);
	        	
	        	KafkaX4APIClassstructure classification = new Gson().fromJson(payload, KafkaX4APIClassstructure.class);
		        if(Assorted.notNull(classification) && Assorted.notNull(classification.getClassstructureid())) 
		        {
		        	ClassStructureTipology returningClass = classificationService.getClassStructureTipology(classification.getClassstructureid());
		        	if((Assorted.notNull(returningClass)) || (Assorted.isNull(returningClass) && DELETE.equalsIgnoreCase(classification.get_action()))) 
		        	{
		        		List<ClassStructureTipology> structureList = classificationService.getListClassStructureTypology(ClassificationType.values());
		        		SequenceCodiceGruppo seq = classificationService.getSequenceCodiceGruppo();
		        		if (Assorted.isNotEmpty(structureList) && Assorted.notNull(seq)) 
		        		{
		        			DatiTipoImpiantoRequest request = new DatiTipoImpiantoRequest();
			        		request.setCODICEGRUPPO(seq.getCodiceGruppo());
			        		JwtTokenResponse jwtToken = odlClient.getToken(EDataIntegrationEntity.GEOCALL_INVIO_TIPO_IMPIANTO);
			        		structureList = addFakeTypeAssets(structureList);
			        		for (int i = 0; i < structureList.size(); i++) 
		        			{
			        			ClassStructureTipology struct = structureList.get(i);
				        		request.setCODICE(struct.getClassStructureId());
				        		request.setDESCRIZIONE(struct.getClassificationId());
				        		request.setLASTRECORD((i + 1) >= structureList.size() ? "1" : "0");
				        		request.setFAMIGLIA(struct.getType());
				        		request.setTIPOANAGRAFICACLIENTE(Constants.PRIMO_LIVELLO.contains(struct.getClassificationId()) ? Constants.TIPO_ANAGRAFICA_CLIENTE_PRIMO_LIVELLO : Constants.TIPO_ANAGRAFICA_CLIENTE);
				        		DatiTipoImpiantoResponse response = odlClient.sendTypeAsset(request, jwtToken);
				        		if (Assorted.notNull(response) && (!StatusX4Esito.OK.name().equalsIgnoreCase(response.getESITO()))) 
				        		{
				        			unsuccessfullResponse = response;
				        			break;
				        		}
			        		}
		        		}
		        		
		        		if (Objects.nonNull(unsuccessfullResponse))
		        		{
		        			x4EsitoIntRequest.setEsito(unsuccessfullResponse.getESITO());
		        			x4EsitoIntRequest.setCodesito(unsuccessfullResponse.getCODICEERRORE());
		        			x4EsitoIntRequest.setDesc_esito(unsuccessfullResponse.getDESCRIZIONEERRORE());
		        		}
		        	}
		        }
		        else {
		        	log.error(EAMErrorMapping.EAM_0006.getErrorDescription() + " {} ", payload);
		        	x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
        			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0006.getEamCode());
        			x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0006.getErrorDescription());
		        }
	        }
	        
		} catch(java.lang.Exception e)  {
			log.error(e.getMessage(), e);
			String description = EAMErrorMapping.EAM_0000.getErrorDescription() + " " + e.getMessage();
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(description);
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
    	Long endTime = System.currentTimeMillis();
		log.info("end call sendTypeAsset(uuidResend={}, payload={}) |{}|", uuidResend, payload, endTime - startTime);
	}
	
	private List<ClassStructureTipology> addFakeTypeAssets(List<ClassStructureTipology> structureList) {
		ClassStructureTipology struct1 = new ClassStructureTipology();
		ClassStructureTipology struct2 = new ClassStructureTipology();
		ClassStructureTipology struct3 = new ClassStructureTipology();
		ClassStructureTipology struct4 = new ClassStructureTipology();
		struct1.setClassStructureId("0");
		struct1.setClassificationId("LINEE (F)");
		struct1.setType("L");
		struct2.setClassStructureId("1");
		struct2.setClassificationId("STAZIONI (F)");
		struct2.setType("S");
		struct3.setClassStructureId("2");
		struct3.setClassificationId("UFFICIO (F)");
		struct3.setType("U");
		struct4.setClassStructureId("3");
		struct4.setClassificationId("IMPIANTO FOTOVOLTAICO (F)");
		struct4.setType("I");
		structureList.add(struct1);
		structureList.add(struct2);
		structureList.add(struct3);
		structureList.add(struct4);
		return structureList;
	}

	public ChiusuraOdlResponseType notifyChiusuraOdl(JAXBElement<ChiusuraOdlType> requestJaxb)
	{
		ChiusuraOdlType chiusuraOdlType = requestJaxb.getValue();
		Instant startDate								= Instant.now();
		String uuid 									= Uuid.randomUuid().toString();
		long startTime 									= System.currentTimeMillis();
		
		ChiusuraOdlResponseType response				= new ChiusuraOdlResponseType();
		response.setESITO(StatusX4Esito.OK.name());
		X4EsitoIntRequest x4EsitoIntRequest 			= new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(GeoCallInterface.NOTIFY_CLOSURE_ODL_INTERFACE.getName());
		x4EsitoIntRequest.setSource(Constants.GEOCALL);
		x4EsitoIntRequest.setDest(Constants.EAM);
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setMessage(XmlUtility.convertToString(ChiusuraOdlType.class.getPackageName(), requestJaxb));
		try {
			if(Assorted.notNull(chiusuraOdlType.getCODICEORDINEESTERNO())) {
				x4EsitoIntRequest.setWonum(chiusuraOdlType.getCODICEORDINEESTERNO());
				if(Objects.nonNull(chiusuraOdlType.getLABTRANS()) && Assorted.isNotEmpty(chiusuraOdlType.getLABTRANS())) {
					for(ListaLabTransType item : chiusuraOdlType.getLABTRANS()) {
						if(wpLaborTitrisQueryService.checkConsuntivo(item.getIDCONSUNTIVO(), item.getMATRICOLA().toUpperCase(), chiusuraOdlType.getCODICEORDINEESTERNO().toUpperCase())) {
							LabTransItem req = populateLabtransRequest(item, chiusuraOdlType.getCODICEORDINEESTERNO());
							MaximoResponse maximoResponse = x4LabtransApiService.insertLabtrans(req);
							if (maximoResponse.getHttpCode() != HttpStatus.OK.value() && !Constants.MAXIMO_UNIQUE_CONSTRAINT.equalsIgnoreCase(maximoResponse.getErrorInfo().getReasonCode()))
								throw new EAMException(EAMErrorMapping.EAM_0000.getEamCode(), EamEtlUtils.errorGenericMessage(maximoResponse.getError()));
						}
					}
				}
			} else
				throw new EAMException(EAMErrorMapping.EAM_0007.getEamCode(), EAMErrorMapping.EAM_0007.getErrorDescription());
		} catch(EAMException q) {
			log.error(q.getMessage());
			response.setESITO(StatusX4Esito.KO.name());
			response.setCODICEERRORE(q.getErrorCode());
			response.setDESCRIZIONEERRORE(q.getMessage());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(q.getErrorCode());
			x4EsitoIntRequest.setDesc_esito(q.getMessage());
		} catch(java.lang.Exception q) {
			log.error("Generic Error: " + q.getMessage(), q);
			response.setESITO(StatusX4Esito.KO.name());
			response.setCODICEERRORE(EAMErrorMapping.EAM_0000.getEamCode());
			response.setDESCRIZIONEERRORE(EAMErrorMapping.EAM_0000.getErrorDescription());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + q.getMessage());
		}
		finally {
			x4EsitoIntRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
			try {
				x4EsitoIntApiService.sendEsitoInt(x4EsitoIntRequest);
			} catch(RestException e) {
				log.error("Rest Error when executing Maximo call to write X4ESITOINT result using this request: {}, occured exception {}", 
						  Assorted.notNull(x4EsitoIntRequest)
						  ? x4EsitoIntRequest.toString() 
						  : null,
						  e.getMessage());
			}
		}
		response.setDESCRIZIONEERRORE(EamEtlUtils.trimString(response.getDESCRIZIONEERRORE(), 2000));
		long endTime = System.currentTimeMillis();
		log.info("end notifyChiusuraOdl({}) |{}|", chiusuraOdlType.getCODICEORDINEESTERNO(), endTime - startTime);
		return response;
	}

	private LabTransItem populateLabtransRequest(ListaLabTransType item, String wonum) 
			throws EAMException 
	{
		if(Assorted.notNull(item.getORE()) && Assorted.notNull(item.getOREUFFICIO()) && Assorted.notNull(item.getTEMPODITRASFERIMENTO())
				&& Double.valueOf(item.getORE()) == 0 && Double.valueOf(item.getOREUFFICIO()) == 0 && Double.valueOf(item.getTEMPODITRASFERIMENTO()) ==0)
			throw new EAMException(EAMErrorMapping.EAM_0039.getEamCode(), EAMErrorMapping.EAM_0039.getErrorDescription());
		
		LabTransItem finalRequest = new LabTransItem();
		finalRequest.setLaborcode(item.getMATRICOLA());
		finalRequest.setRegularhrs(Assorted.notNull(item.getORE()) ? Double.valueOf(item.getORE()) : null);
		finalRequest.setX4oretrasf(Assorted.notNull(item.getTEMPODITRASFERIMENTO()) ? Double.valueOf(item.getTEMPODITRASFERIMENTO()) : null);
		finalRequest.setX4oreuff(Assorted.notNull(item.getOREUFFICIO()) ? Double.valueOf(item.getOREUFFICIO()) : null);
		finalRequest.setStartdatetime(EamEtlUtils.esitoDate(item.getDATAINIEFFETTIVA()));
		finalRequest.setFinishdatetime(EamEtlUtils.esitoDate(item.getDATAFINEEFFETTIVA()));
		finalRequest.setExternalrefid(Assorted.notNull(item.getIDCONSUNTIVO()) ? item.getIDCONSUNTIVO() : null);
		finalRequest.setSiteid(SIT01);
		finalRequest.setRefwo(wonum);

		return finalRequest;
	}
	
	public void sendCancelOrBlockOdL(String topic, String uuidResend, String operation, String payload)
	{
		Instant startDate	= Instant.now();
		String uuid 		= Uuid.randomUuid().toString();
		Long startTime 		= System.currentTimeMillis();	

		X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(GeoCallInterface.SEND_CANCEL_BLOCK_INTERFACE.getName());
		x4EsitoIntRequest.setSource(Constants.EAM);
		x4EsitoIntRequest.setDest(Constants.GEOCALL);
		x4EsitoIntRequest.setKafkatopic(topic);
		x4EsitoIntRequest.setMessage(payload);
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());
		
		try {
	        if(Assorted.notNull(payload))
	        {
	        	if(Assorted.notNull(uuidResend)) 
	        	{
	        		x4EsitoIntRequest.setUniqueid(uuidResend);
	        		X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
	        		x4EsitoIntRequest.setParent(Assorted.notNull(parent) ? parent.getX4EsitoIntId() : null);
	        	}
	        	else
	        		x4EsitoIntRequest.setRetry(0L);
	        	
	        	
	        	WorkOrderDTO workOrder = new Gson().fromJson(payload, WorkOrderDTO.class);

	        	if (Assorted.isNull(workOrder))
		        	throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription());
		        
		        if (Assorted.isEmpty(workOrder.getStatus()))
		        	throw new EAMException(EAMErrorMapping.EAM_0002.getEamCode(), EAMErrorMapping.EAM_0002.getErrorDescription());
		        
		        x4EsitoIntRequest.setWonum(workOrder.getWonum());
		        
		        //Sbiancamento campo WOLO1 = NULL
				workOrderQueryService.updateWolo1(workOrder.getWonum());
		        
		        AnnullaBloccoOdlRequest request = new AnnullaBloccoOdlRequest();
		        request.setCODICEORDINEESTERNO(workOrder.getWonum());
		        request.setTIPOOPERAZIONE(EGeoTipoOperazione.ANNULLAMENTO.getType());

		        if (Assorted.notNull(workOrder.getX4aggregati()) && workOrder.getX4aggregati())
		        	request.setCODICECLUSTER(workOrder.getWonum());
		        
		        if(log.isDebugEnabled())
					log.debug(GeoCallInterface.SEND_CANCEL_BLOCK_INTERFACE.getName() + " Request: " + EamEtlUtils.writeJaxbObjectAsString(request));
				JwtTokenResponse jwtToken = odlClient.getTokenOSB(EDataIntegrationEntity.GEOCALL_ANNULLA_BLOCCO);
				AnnullaBloccoOdlResponse2 response = odlClient.sendAnnullaBloccoOdL(request, jwtToken);
		        
				if (StatusX4Esito.KO.name().equals(response.getESITO()))
					throw new EAMException(response.getCODICEERRORE(), response.getDESCRIZIONEERRORE());
	        }
		        		
		} catch(EAMException q) {
			log.error(q.getMessage());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(q.getErrorCode());
			x4EsitoIntRequest.setDesc_esito(q.getMessage());
		} catch(java.lang.Exception q) {
			log.error("Generic Error: " + q.getMessage(), q);
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + q.getMessage());			
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
		Long endTime = System.currentTimeMillis();
		log.info("end call sendCancelOrBlockOdL(uuidResend={}, operation={}, payload={}) |{}|", uuidResend, operation, payload, endTime - startTime);
	}

	@Deprecated
	public void sendCancelOperation(String topic, String uuidResend, String payload) 
	{
		Instant startDate	= Instant.now();
		String uuid 		= Uuid.randomUuid().toString();
		Long startTime 		= System.currentTimeMillis(); 

		X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(GeoCallInterface.SEND_CANCEL_OPERATION_INTERFACE.getName());
		x4EsitoIntRequest.setSource(Constants.EAM);
		x4EsitoIntRequest.setDest(Constants.GEOCALL);
		x4EsitoIntRequest.setKafkatopic(topic);
		x4EsitoIntRequest.setMessage(payload);
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());
		
		try {
	        if(Assorted.notNull(payload))
	        {
	        	if(Assorted.notNull(uuidResend)) 
	        	{
	        		x4EsitoIntRequest.setUniqueid(uuidResend);
	        		X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
	        		x4EsitoIntRequest.setParent(Assorted.notNull(parent) ? parent.getX4EsitoIntId() : null);
	        	}
	        	else
	        		x4EsitoIntRequest.setRetry(0L);
	        	
	        	KafkaWorkOrderDTO kafkaOdLGeoCall = new Gson().fromJson(payload, KafkaWorkOrderDTO.class);
		        if (Assorted.isNull(kafkaOdLGeoCall) || Assorted.isEmpty(kafkaOdLGeoCall.getMember()))
		        	throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription());
		        
		        WorkOrderDTO odLGeoCall = kafkaOdLGeoCall.getMember().get(0);
		        if (Assorted.isEmpty(odLGeoCall.getWonum()))
		        	throw new EAMException(EAMErrorMapping.EAM_0007.getEamCode(), EAMErrorMapping.EAM_0007.getErrorDescription());
		        
		        x4EsitoIntRequest.setWonum(odLGeoCall.getWonum());
		        
		        AnnullaOperazioneOdlRequest request = new AnnullaOperazioneOdlRequest();
		        request.setCODICEORDINEESTERNO(odLGeoCall.getParent());
		        request.setCODICEPOSIZIONE(odLGeoCall.getWonum());
		        request.setTIPOOPERAZIONE(EGeoTipoOperazione.ANNULLAMENTO.getType());
		        
		        if(log.isDebugEnabled())
					log.debug(GeoCallInterface.SEND_CANCEL_OPERATION_INTERFACE.getName() + " Request: " + EamEtlUtils.writeJaxbObjectAsString(request));
				JwtTokenResponse jwtToken = odlClient.getTokenOSB(EDataIntegrationEntity.GEOCALL_ANNULLA_OPERAZIONE);
				AnnullaOperazioneOdlResponse2 response = odlClient.sendAnnullaOperazioneOdL(request, jwtToken);
		        
				if (StatusX4Esito.KO.name().equals(response.getESITO()))
					throw new EAMException(response.getCODICEERRORE(), response.getDESCRIZIONEERRORE());					
	        }
		        		
		} catch(EAMException q) {
			log.error(q.getMessage());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(q.getErrorCode());
			x4EsitoIntRequest.setDesc_esito(q.getMessage());
		} catch(java.lang.Exception q) {
			log.error("Generic Error: " + q.getMessage(), q);
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + q.getMessage());			
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
		Long endTime = System.currentTimeMillis();
		log.info("end call sendCancelOperation(uuidResend={}, payload={}) |{}|", uuidResend, payload, endTime - startTime);
	}

	public void sendOdL(String topic, String uuidResend, String operation, String payload)
	{
		Instant startDate	= Instant.now();
		String uuid 		= Uuid.randomUuid().toString();
		Long startTime		= System.currentTimeMillis();
		CreateOdlResponse response;
		X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(GeoCallInterface.SEND_ODL_INTERFACE.getName());
		x4EsitoIntRequest.setSource(Constants.EAM);
		x4EsitoIntRequest.setDest(Constants.GEOCALL);
		x4EsitoIntRequest.setKafkatopic(topic);
		x4EsitoIntRequest.setMessage(payload);
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());

		try {

			if(Assorted.notNull(payload))
			{
				if(Assorted.notNull(uuidResend))
				{
					x4EsitoIntRequest.setUniqueid(uuidResend);
					X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
					x4EsitoIntRequest.setParent(Assorted.notNull(parent) ? parent.getX4EsitoIntId() : null);
				}
				else
					x4EsitoIntRequest.setRetry(0L);

				WorkOrderDTO workOrder = new Gson().fromJson(payload, WorkOrderDTO.class);

				if (Assorted.isNull(workOrder) && (Assorted.isNull(workOrder.getHref())))
					throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription());

				String wonum = workOrder.getWonum();

				String respAsset = workOrder.getX4respasset();

				Integer haschildren = workOrder.getHaschildren() ? 1 : 0;

				String x4clusterindisp = workOrder.getX4clusterindisp();

				String x4dinizioindisp = workOrder.getX4dinizioindisp();

				String x4dfineindisp = workOrder.getX4dfineindisp();

				String x4statusindisp = workOrder.getX4statusindisp();

				x4EsitoIntRequest.setWonum(wonum);

				List<GeoCallWorkOrder> workOrderList = workOrderQueryService.getWorkOrderHierarchyByWonum(wonum);

				GeoCallWorkOrder father = workOrderList.stream().filter(x -> wonum.equalsIgnoreCase(x.getWonum())).findFirst().orElse(null);

				List<GeoCallWorkOrder> children = workOrderList.stream().filter(x -> !wonum.equalsIgnoreCase(x.getWonum())).toList();

				List<WpLaborTitris> titrisList;

				Map<String, List<WpLaborTitris>> titrisMap = null;

				if(Objects.nonNull(father) && Objects.nonNull(father.getWonum()) && Objects.nonNull(haschildren))
				{
					titrisList = wpLaborTitrisQueryService.getTitrisListByWonum(father.getWonum(), haschildren.toString());
					if(Assorted.isNotEmpty(titrisList))
					{
						List<WpLaborTitris> noNullsList = titrisList.stream().filter(titris -> Assorted.notNull(titris.getX4titris())).toList();
						titrisMap = Assorted.isNotEmpty(noNullsList)
								? noNullsList.stream().collect(Collectors.groupingBy(WpLaborTitris::getX4titris))
								: null;
					}
				}

				CreateOdlRequest request = populateCreaOdlRequest(father, children, titrisMap,
						x4clusterindisp, x4dinizioindisp,
						x4dfineindisp, x4statusindisp, respAsset);

				if(Assorted.notNull(request))
				{
					if(log.isDebugEnabled()) {
						log.debug(GeoCallInterface.SEND_ODL_INTERFACE.getName() + " Request: " + EamEtlUtils.writeJaxbObjectAsString(request));
					}
					JwtTokenResponse jwtToken = odlClient.getTokenOSB(EDataIntegrationEntity.GEOCALL_INVIO_ODL);
					response = odlClient.sendOdL(request, jwtToken);
				}
				else
					throw new EAMException(EAMErrorMapping.EAM_0004.getEamCode(), EAMErrorMapping.EAM_0004.getErrorDescription());

				if (StatusX4Esito.KO.name().equals(response.getESITO()))
					throw new EAMException(response.getCODICEERRORE(), response.getDESCRIZIONEERRORE());
			}

		} catch(EAMException q) {
			log.error(q.getMessage());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(q.getErrorCode());
			x4EsitoIntRequest.setDesc_esito(q.getMessage());
		} catch(java.lang.Exception q) {
			log.error("Generic Error: " + q.getMessage(), q);
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + q.getMessage());
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
		Long endTime = System.currentTimeMillis();
		log.info("end call sendOdL(uuidResend={}, operation={}, payload={}) |{}|", uuidResend, operation, payload, endTime - startTime);
	}

	private CreateOdlRequest populateCreaOdlRequest(GeoCallWorkOrder father, List<GeoCallWorkOrder> children,
	                                                                                               Map<String, List<WpLaborTitris>> titrisMap,
	                                                                                               String x4clusterindisp, String x4dinizioindisp,
	                                                                                               String x4dfineindisp, String x4statusindisp, String respAsset)
	{
		if(Assorted.notNull(father)) {
			CreateOdlRequest returningReq = new CreateOdlRequest();
			returningReq.setCODICEORDINEESTERNO(father.getWonum());
			returningReq.setCODICECATALOGO(father.getJpnum());
			returningReq.setDESCRIZIONEATTIVITA(father.getDescription());
			returningReq.setRESPONSABILEASSET(respAsset);
			if(Assorted.notNull(titrisMap)) {
				Titris titris = new Titris();
				for(Entry<String, List<WpLaborTitris>> titrisEntry : titrisMap.entrySet())
				{
					UnitaImpianti unitaImpianti = new UnitaImpianti();
					unitaImpianti.setCODUNITAIMPIANTI(titrisEntry.getKey());
					for(WpLaborTitris item : titrisEntry.getValue())
					{
						Risorse risorsa = new Risorse();
						risorsa.setCODINQUADRAMENTO(item.getCraft());
						risorsa.setNUMERORISORSE(Assorted.notNull(item.getQuantity()) ? item.getQuantity().toString() : null);
						unitaImpianti.getRISORSE().add(risorsa);
					}
					titris.getUNITAIMPIANTI().add(unitaImpianti);
				}
				returningReq.getTITRIS().add(titris);
			}
			returningReq.setDATATOLLSX(EamEtlUtils.esitoDateToXmlGCGeoCall(father.getSneconstraint()));
			returningReq.setDATATOLLDX(EamEtlUtils.esitoDateToXmlGCGeoCall(father.getFnlconstraint()));
			returningReq.setDATAINIZIOATT(EamEtlUtils.esitoDateToXmlGCGeoCall(father.getTargstartdate()));
			returningReq.setDATAFINEATT(EamEtlUtils.esitoDateToXmlGCGeoCall(father.getTargcompdate()));
			returningReq.setPRIORITA(Assorted.notNull(father.getWopriority()) ? Long.valueOf(father.getWopriority()) : null);
			returningReq.setCODSEDETECNICA(father.getAssetnum());
			returningReq.setCODICECLUSTER(Assorted.isNotEmpty(children) ? father.getWonum() : null);
			returningReq.setTIPOSPESA(father.getX4tipospesa());
			returningReq.setCODICEDELIBERAINDISPONIBILITA(x4clusterindisp);
			returningReq.setFLAGINDISPONIBILITA(father.getX4indisponibilita());
			returningReq.setDATAINIZIOINDISPONIBILITA(EamEtlUtils.stringDateToXmlGCGeoCall(x4dinizioindisp));
			returningReq.setDATAFINEINDISPONIBILITA(EamEtlUtils.stringDateToXmlGCGeoCall(x4dfineindisp));
			returningReq.setSTATODELIBERA(x4statusindisp);
			if(Assorted.isNotEmpty(children)) {
				for(GeoCallWorkOrder child: children) {
					Operazione childOperation = new Operazione();
					childOperation.setCODPOSIZIONE(child.getWonum());
					childOperation.setDATATOLLSX(EamEtlUtils.esitoDateToXmlGCGeoCall(child.getSneconstraint()));
					childOperation.setDATATOLLDX(EamEtlUtils.esitoDateToXmlGCGeoCall(child.getFnlconstraint()));
					childOperation.setDATAINIZIOATT(EamEtlUtils.esitoDateToXmlGCGeoCall(child.getTargstartdate()));
					childOperation.setDATAFINEATT(EamEtlUtils.esitoDateToXmlGCGeoCall(child.getTargcompdate()));
					childOperation.setPROPEDEUTICITA(null);
					childOperation.setDURATAOP(Assorted.notNull(child.getEstdur()) ? Double.valueOf(child.getEstdur() * 60).longValue()  : null);
					childOperation.setCODSEDETECNICA(child.getAssetnum());
					childOperation.setSAPID(child.getX4sapidasset());
					returningReq.getOPERAZIONE().add(childOperation);
				}
			}
			else {
				Operazione operation = new Operazione();
				operation.setCODPOSIZIONE(father.getWonum());
				operation.setDATATOLLSX(EamEtlUtils.esitoDateToXmlGCGeoCall(father.getSneconstraint()));
				operation.setDATATOLLDX(EamEtlUtils.esitoDateToXmlGCGeoCall(father.getFnlconstraint()));
				operation.setDATAINIZIOATT(EamEtlUtils.esitoDateToXmlGCGeoCall(father.getTargstartdate()));
				operation.setDATAFINEATT(EamEtlUtils.esitoDateToXmlGCGeoCall(father.getTargcompdate()));
				operation.setPROPEDEUTICITA(null);
				operation.setDURATAOP(Assorted.notNull(father.getEstdur()) ? Double.valueOf(father.getEstdur() * 60).longValue() : null);
				operation.setCODSEDETECNICA(father.getAssetnum());
				operation.setSAPID(father.getX4sapidasset());
				returningReq.getOPERAZIONE().add(operation);
			}
			return returningReq;
		}
		else {
			return null;
		}
	}

	@Async
	public void modificaAnagraficaMassiva() {
		modificaAnagraficaMassivaInternal();
	}

	
	private void modificaAnagraficaMassivaInternal() 
	{
	    long startTime = System.currentTimeMillis();
	    
		try {
			log.debug("starting modificaAnagraficaMassivaInternal() at |{}|", startTime);
			x4GeoCallAddressQueryService.executeX4GeoCallAddressStoredProcedure();
			long procDuration = System.currentTimeMillis();
			log.debug("ended executeX4GeoCallAddressStoredProcedure() at |{}|", procDuration - startTime);
			Map<String, List<ClassStructureHierarchy>> linesMap = classificationService.getMapClassStructure(ClassificationType.LINEA);
			Map<String, List<ClassStructureHierarchy>> stationMap = classificationService.getMapClassStructure(ClassificationType.STAZIONE);
			Map<String, List<ClassStructureHierarchy>> ufficioMap = classificationService.getMapClassStructure(ClassificationType.UFFICIO);
			Map<String, List<ClassStructureHierarchy>> fotovoltaicoMap = classificationService.getMapClassStructure(ClassificationType.FOTOVOLTAICO);
			String lineId = null;
			String stationId = null;
			String ufficioId = null;
			String fotovoltaicoId = null;
			if(Assorted.notNull(linesMap)) 
				lineId = linesMap.get(ClassificationType.LINEA.name()).get(0).getClassStructureId();
			if(Assorted.notNull(linesMap)) 
				stationId = stationMap.get(ClassificationType.STAZIONE.name()).get(0).getClassStructureId();
			if(Assorted.notNull(ufficioMap))
				ufficioId = ufficioMap.get(ClassificationType.UFFICIO.getName()).get(0).getClassStructureId();
			if(Assorted.notNull(fotovoltaicoMap))
				fotovoltaicoId = fotovoltaicoMap.get(ClassificationType.FOTOVOLTAICO.getName()).get(0).getClassStructureId();
			List<X4GeoCallAddressRestricted> lineObjectsList     = x4GeoCallAddressQueryService.getLineOrStationObjects(lineId);
			List<X4GeoCallAddressRestricted> stationObjectsList  = x4GeoCallAddressQueryService.getLineOrStationObjects(stationId);
			List<X4GeoCallAddressRestricted> ufficioObjectsList     = x4GeoCallAddressQueryService.getLineOrStationObjects(ufficioId);
			List<X4GeoCallAddressRestricted> fotovoltaicoObjectsList  = x4GeoCallAddressQueryService.getLineOrStationObjects(fotovoltaicoId);
			List<X4GeoCallAddressRestricted> allObjectList       = new ArrayList<>();
			
			if (Assorted.isNotEmpty(lineObjectsList)) 
			    allObjectList.addAll(lineObjectsList);
			
			if (Assorted.isNotEmpty(stationObjectsList)) 
			    allObjectList.addAll(stationObjectsList);
			
			if (Assorted.isNotEmpty(ufficioObjectsList)) 
			    allObjectList.addAll(ufficioObjectsList);
			
			if (Assorted.isNotEmpty(fotovoltaicoObjectsList)) 
			    allObjectList.addAll(fotovoltaicoObjectsList);
			
			constructCsvList(allObjectList);
		} 
		catch (Exception e) 
		{
			log.error("Generic Error: " + e.getMessage(), e);
		}
		
		long endTime = System.currentTimeMillis();
		log.info("CSV files written at |{}|", endTime - startTime);
	}

	
	private Set<String> findAllRoot(String rootLine) 
	        throws DataLayerException
	{
	    Set<String> result         = new HashSet<>();
	    Set<String> daElaborare    = new HashSet<>();
	    daElaborare.add(rootLine);	
	    
	    while (Assorted.isNotEmpty(daElaborare))
	    {
	        Set<String> daElaborareTmp = new HashSet<>();
	        result.addAll(daElaborare);
	        for (String asset: daElaborare)
	        {
	            List<String> rootCodes = x4GeoCallAddressQueryService.getRootCodes(asset);
	            
	            if(Assorted.isNotEmpty(rootCodes)) {
		            for(String assetJ: rootCodes) 
		            {
		                if (!result.contains(assetJ))
		                    daElaborareTmp.add(assetJ);	            
		            }
	            }
	        }
	        
	        daElaborare = daElaborareTmp;
	    }
	    
	    
	    return result;
	}	
	
	
	private void constructCsvList(List<X4GeoCallAddressRestricted> objectList) 
								  throws DataLayerException, IOException 
	{
		if (Assorted.isNotEmpty(objectList)) 
		{
			Set<String> lineElaborated = new HashSet<>();
			CSVWriter csv              = null;
			int numRecord              = 0;
			int numFile                = 0;
			FileWriter fileWriter      = null;
			
			try {
			    
			
        		for (X4GeoCallAddressRestricted object: objectList)
        		{
        		    if (lineElaborated.contains(object.getCodiceOggetto()))
        		        continue;
        		    
        		    List<X4GeoCallAddress> hierarchyList = new ArrayList<>();
        			List<String> rootCodes = new ArrayList<>(findAllRoot(object.getCodiceOggetto()));
        				
        			if(Assorted.isNotEmpty(rootCodes)) 
        			{
        				rootCodes = rootCodes.stream().filter(x -> !lineElaborated.contains(x)).toList();
        				if(Assorted.isNotEmpty(rootCodes))
        					hierarchyList = x4GeoCallAddressQueryService.getHierarchy(rootCodes);
        				lineElaborated.addAll(rootCodes);
        			}
        	
        			if (Assorted.isNotEmpty(hierarchyList))
        			{
        			    if (EamEtlUtils.isBiggerThanBufferSize(hierarchyList.size() + numRecord))
        			    {
        			        IOUtils.closeQuietly(fileWriter);
        	                IOUtils.closeQuietly(csv);
        			        csv = null;
        			        numRecord = 0;
        			    }
        			    
        			    if (Assorted.isNull(csv))
        			    {
        			        fileWriter  = new FileWriter(EamEtlUtils.createFileNameCustomCsv(Constants.PKG_X4GEOCALLADDRESS_FILEPATH, EamEtlUtils.constructCsvKey(++numFile))); 
        			        csv         = new CSVWriter(fileWriter, 
        			                                    Constants.CSV_SEPARATOR, 
        		                                        CSVWriter.NO_QUOTE_CHARACTER, 
        		                                        CSVWriter.DEFAULT_ESCAPE_CHARACTER, 
        		                                        CSVWriter.DEFAULT_LINE_END); 
        			        
        			        csv.writeNext(Constants.HEADERS_ANAGRAFICA_MASSIVA);
        			        csv.flush();
        			    }
        			    
        			    csv.writeAll(constructCsvX4GeoCallAddressData(hierarchyList));
        			    csv.flush();
        			    numRecord += hierarchyList.size();
        			}
        		}
        		
			} finally {
			    IOUtils.closeQuietly(fileWriter);
			    IOUtils.closeQuietly(csv);
			}
		}
	}
	
	private List<String[]> constructCsvX4GeoCallAddressData(List<X4GeoCallAddress> dataList) {
		List<String[]> returningDataList = new ArrayList<>();
		for(X4GeoCallAddress data : dataList) 
		{
			String[] returningData = {
					EamEtlUtils.blankStringIfNull(data.getTipoOggetto()),
					EamEtlUtils.blankStringIfNull(data.getStatoOggetto()),
					EamEtlUtils.blankStringIfNull(data.getCodiceOggetto()),
					EamEtlUtils.blankStringIfNull(data.getCodiceSap()),
					EamEtlUtils.blankStringIfNull(data.getCodiceSap2()),
					EamEtlUtils.blankStringIfNull(data.getDescrizione()),
					EamEtlUtils.blankStringIfNull(data.getCodicePadre()),
					EamEtlUtils.blankStringIfNull(data.getCodiceRadice()),
					EamEtlUtils.blankStringIfNull(data.getCodicePadre2()),
					EamEtlUtils.blankStringIfNull(data.getCodiceRadice2()),
					//areaLavorazione
					EamEtlUtils.blankStringIfNull(null),
					//centroDiLavoro
					EamEtlUtils.blankStringIfNull(null),
					EamEtlUtils.blankStringIfNull(data.getFiliale()),
					EamEtlUtils.blankStringIfNull(data.getToponimo()),
					EamEtlUtils.blankStringIfNull(data.getVia()),
					EamEtlUtils.blankStringIfNull(data.getCivico()),
					EamEtlUtils.blankStringIfNull(data.getLocalita()),
					EamEtlUtils.blankStringIfNull(data.getProvincia()),
					EamEtlUtils.blankStringIfNull(data.getCap()),
					EamEtlUtils.blankStringIfNull(data.getComune()),
					EamEtlUtils.blankStringIfNull(data.getIstat()),
					EamEtlUtils.blankStringIfNull(data.getLatitudine1()),
					EamEtlUtils.blankStringIfNull(data.getLongitudine1()),
					EamEtlUtils.blankStringIfNull(data.getLatitudine2()),
					EamEtlUtils.blankStringIfNull(data.getLongitudine2())
			};
			returningDataList.add(returningData);
		}
		return returningDataList;
	}
	
	public ChangeStatusResponseType changeStatusOdl(JAXBElement<ChangeStatusType> requestJaxb) 
	{
		ChangeStatusType changeStatusType = requestJaxb.getValue();
		if(log.isDebugEnabled())
			log.debug(EamEtlUtils.writeJaxbObjectAsString(requestJaxb));

		Instant startDate								= Instant.now();
		String uuid 									= Uuid.randomUuid().toString();
		Long startTime 									= System.currentTimeMillis();
		ChangeStatusResponseType response				= new ChangeStatusResponseType();
		response.setESITO(StatusX4Esito.OK.name());
		X4EsitoIntRequest x4EsitoIntRequest 			= new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(GeoCallInterface.NOTIFY_CHANGE_STATUS_INTERFACE.getName());
		x4EsitoIntRequest.setSource(Constants.GEOCALL);
		x4EsitoIntRequest.setDest(Constants.EAM);
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setMessage(XmlUtility.convertToString(ChangeStatusType.class.getPackageName(), requestJaxb));
		try 
		{
			if(Assorted.notNull(changeStatusType.getCODICEPOSIZIONE()) && Assorted.isNotEmpty(changeStatusType.getCODICEPOSIZIONE())) 
			{
				x4EsitoIntRequest.setWonum(changeStatusType.getCODICEPOSIZIONE());
				checkStatoNonEseguito(changeStatusType);
				
			    boolean changeStatus = Assorted.isEmpty(workOrderQueryService.checkIfStatusChange(changeStatusType.getCODICEPOSIZIONE(), SIT01)) ? true : false;
			    boolean codiceClusterIsEmpty = Assorted.isEmpty(changeStatusType.getCODICECLUSTER());
			    String sonStatus = codiceClusterIsEmpty && Constants.CHIUSO_GEOCALL.contains(changeStatusType.getSTATOOPERAZIONE()) ? changeStatusType.getSTATOORDINE() : changeStatusType.getSTATOOPERAZIONE();
				if(changeStatus) 
				{
					List<X4WorkorderRequest> requestList = new ArrayList<>();
					List<ChangeStatusDTO> wonumDaProcessare = new ArrayList<>();
					
					wonumDaProcessare.add(createChangeStatusDTO(changeStatusType.getCODICEPOSIZIONE(), sonStatus, false));
					
					if(!codiceClusterIsEmpty) 
						wonumDaProcessare.add(createChangeStatusDTO(changeStatusType.getCODICECLUSTER(), changeStatusType.getSTATOORDINE(), true));
					
					for(ChangeStatusDTO dto : wonumDaProcessare) 
					{
						requestList.add(createNotifyChangeStatusRequest(dto, changeStatusType));
					}
					
					String statoCodPosAttuale = requestList.stream().filter(w -> w.getWonum().equalsIgnoreCase(changeStatusType.getCODICEPOSIZIONE())).findFirst().map(X4WorkorderRequest::getStatus).orElse(null);
					EAMStatusFatherMapping statusCheck = EAMStatusFatherMapping.findFatherStatus(Arrays.asList(statoCodPosAttuale));
					if(Assorted.notNull(statusCheck))
					{
						WorkorderHierarchyRestricted topPadre = workOrderQueryService.getWorkorderTopParent(changeStatusType.getCODICEPOSIZIONE());
						if(Assorted.notNull(topPadre)) 
						{
							String topPadreWonum = topPadre.getWonum();
							boolean statoNonEseguito = codiceClusterIsEmpty && statoCodPosAttuale.equalsIgnoreCase(EAMCausaleEsitoMapping.NON_ESEGUITA.getStatus()) ? true : false;
							X4WorkorderRequest topPadreReq = getChangeStatusRequestForTopPadre(statusCheck.getFatherStatus(), topPadreWonum, 
																								changeStatusType.getDATACAMBIOSTATO(), requestList,
																									statoNonEseguito);
							requestList.stream()
						    .filter(w -> w.getWonum().equalsIgnoreCase(topPadreWonum))
						    .findFirst()
						    .ifPresentOrElse(
						    		w -> w.setStatus(topPadreReq.getStatus()),
						    		() -> requestList.add(topPadreReq)
						    );
						}
					}
						
					MaximoResponseArray<MaximoResponse> updateWorkorders = x4WorkorderApiService.insertAndUpdateX4WorkorderChangeStatusBulk(requestList, MaximoResponse[].class);
					
					if(updateWorkorders.getHttpCode() != HttpStatus.OK.value()) 
						throw new EAMException(EAMErrorMapping.EAM_0000.getEamCode(), EamEtlUtils.errorGenericMessage(updateWorkorders.getError()));
				}
				else 
				{
					X4WorkorderResponse fatherOdl = x4WorkorderApiService.retrieveWorkorder(changeStatusType.getCODICEPOSIZIONE());
					X4WorkorderRequest req = new X4WorkorderRequest();
					EAMStatusMapping status = EAMStatusMapping.findStatus(sonStatus);
					req.setHref(fatherOdl.getMember().get(Values.FIRST_ELEMENT).getHref());
					req.setX4statogeocall(sonStatus);
					req = closureType(req, status);
					MaximoResponse updateWorkorder = x4WorkorderApiService.updateWorkorder(req.getHref(), req);
					if (updateWorkorder.getHttpCode() != HttpStatus.OK.value())
						throw new EAMException(EAMErrorMapping.EAM_0000.getEamCode(), EamEtlUtils.errorGenericMessage(updateWorkorder.getError()));
				}
			} 
			else
				throw new EAMException(EAMErrorMapping.EAM_0010.getEamCode(), EAMErrorMapping.EAM_0010.getErrorDescription());
			
		} catch(EAMException q) {
			log.error(q.getMessage());
			response.setESITO(StatusX4Esito.KO.name());
			response.setCODICEERRORE(q.getErrorCode());
			response.setDESCRIZIONEERRORE(q.getMessage());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(q.getErrorCode());
			x4EsitoIntRequest.setDesc_esito(q.getMessage());
		} catch(java.lang.Exception q) {
			log.error("Generic Error: " + q.getMessage(), q);
			response.setESITO(StatusX4Esito.KO.name());
			response.setCODICEERRORE(EAMErrorMapping.EAM_0000.getEamCode());
			response.setDESCRIZIONEERRORE(EAMErrorMapping.EAM_0000.getErrorDescription());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + q.getMessage());
		}
		finally {
			x4EsitoIntRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
			try {
				x4EsitoIntApiService.sendEsitoInt(x4EsitoIntRequest);
			} catch(RestException e) {
				log.error("Rest Error when executing Maximo call to write X4ESITOINT result using this request: {}, occured exception {}", 
						  Assorted.notNull(x4EsitoIntRequest)
						  ? x4EsitoIntRequest.toString() 
						  : null,
						  e.getMessage());
			}
		}
		response.setDESCRIZIONEERRORE(EamEtlUtils.trimString(response.getDESCRIZIONEERRORE(), 2000));
		Long endTime = System.currentTimeMillis();
		log.info("end call changeStatusOdl({}) |{}|", changeStatusType.getCODICEPOSIZIONE(), endTime - startTime);
		return response;
	}
	
	/**
	 * Blocca il cambio stato quando l'OdL notificato da GeoCall (operazione e/o cluster) risulta
	 * gia' in stato NON ESEGUITO. Se il wonum non e' presente in EAM il controllo non blocca:
	 * la segnalazione resta a carico del flusso a valle.
	 */
	private void checkStatoNonEseguito(ChangeStatusType changeStatusType) 
			throws EAMException, DataLayerException
	{
		List<String> wonumDaVerificare = new ArrayList<>();
		wonumDaVerificare.add(changeStatusType.getCODICEPOSIZIONE());
		
		if(Assorted.isNotEmpty(changeStatusType.getCODICECLUSTER()))
			wonumDaVerificare.add(changeStatusType.getCODICECLUSTER());
		
		for(String wonum : wonumDaVerificare)
		{
			WorkorderHierarchyRestricted odl = workOrderQueryService.getWorkorderStatus(wonum, SIT01);
			
			if(Assorted.notNull(odl) && EAMCausaleEsitoMapping.NON_ESEGUITA.getStatus().equalsIgnoreCase(odl.getStatus()))
			{
				log.warn("changeStatusOdl - cambio stato non consentito per l'OdL {} in stato {}", wonum, odl.getStatus());
				throw new EAMException(EAMErrorMapping.EAM_0040.getEamCode(), 
									   EAMErrorMapping.EAM_0040.getErrorDescription() + " [wonum: " + wonum + "]");
			}
		}
	}
	
	private ChangeStatusDTO createChangeStatusDTO(String wonum, String status, boolean father) 
			throws EAMException, RestException 
	{
		X4WorkorderResponse odl = x4WorkorderApiService.retrieveWorkorder(wonum);
		if(Assorted.notNull(odl) && Assorted.notNull(odl.getMember())) 
		{
			ChangeStatusDTO dtoOdl = new ChangeStatusDTO();
			dtoOdl.setWonum(odl.getMember().get(Values.FIRST_ELEMENT).getWonum());
			dtoOdl.setHref(odl.getMember().get(Values.FIRST_ELEMENT).getHref());
			dtoOdl.setReqStatus(status);
			dtoOdl.setFather(father);
			dtoOdl.setParent(odl.getMember().get(Values.FIRST_ELEMENT).getParent());
			return dtoOdl;
		}
		else
			throw new EAMException(EAMErrorMapping.EAM_0004.getEamCode(), EAMErrorMapping.EAM_0004.getErrorDescription());
	}

	private X4WorkorderRequest getChangeStatusRequestForTopPadre(String statusCheckFather, 
																 String topPadreWonum, 
																 XMLGregorianCalendar changeStatusTypeDate, 
																 List<X4WorkorderRequest> requestList,
																 boolean statoNonEseguito) 
		throws RestException, EAMException, DataLayerException
	{
		X4WorkorderRequest req = new X4WorkorderRequest();
		X4WorkorderResponse odl = x4WorkorderApiService.retrieveWorkorder(topPadreWonum);
		Boolean eseguito = null;
		Boolean nonEseguito = null;
		
		List<WorkorderHierarchyRestricted> workorderList = workOrderQueryService.getWorkorderSonsHierarchyStatuses(topPadreWonum);
		for(X4WorkorderRequest reqOdl : requestList) 
		{
			workorderList.stream()
		    .filter(w -> w.getWonum().equalsIgnoreCase(reqOdl.getWonum()))
		    .findFirst()
		    .ifPresent(w -> w.setStatus(reqOdl.getStatus()));
		}
		
		List<String> statusList = workorderList.stream().filter(x -> Assorted.notNull(x.getParent())).map(WorkorderHierarchyRestricted::getStatus).toList();
		EAMStatusFatherMapping statusForFather = EAMStatusFatherMapping.findFatherStatus(statusList);
		
		if(Assorted.isNotEmpty(statusList)) 
		{
			eseguito = statusList.stream().allMatch(x -> EAMCausaleEsitoMapping.ESEGUITA.getStatus().equalsIgnoreCase(x));
			nonEseguito = statusList.stream().allMatch(x -> EAMCausaleEsitoMapping.NON_ESEGUITA.getStatus().equalsIgnoreCase(x));
		}
		
		if(Objects.nonNull(eseguito) && eseguito)
			req.setStatus(EAMCausaleEsitoMapping.ESEGUITA.getStatus());
		else if (Objects.nonNull(nonEseguito) && nonEseguito)
			req.setStatus(EAMCausaleEsitoMapping.NON_ESEGUITA.getStatus());
		else if(Assorted.notNull(statusForFather))
			req.setStatus(statusForFather.getFatherStatus());
		else 
			req.setStatus(statoNonEseguito ? EAMCausaleEsitoMapping.NON_ESEGUITA.getStatus() : statusCheckFather);
		
		req.setWonum(topPadreWonum);
		req.setChangedate(Assorted.notNull(changeStatusTypeDate) ? EamEtlUtils.esitoDate(changeStatusTypeDate) : null);
		req.setHref(odl.getMember().get(Values.FIRST_ELEMENT).getHref());
		
		return req;
	}

	public boolean sendSapStornoFromSAP(StornoDTO stornoDTO) throws EAMException {
		try {
			if(Assorted.notNull(stornoDTO))
			{
	        	StornoRequest request = new StornoRequest();
	        	request.setCodiceOrdineEsterno(stornoDTO.getWonum());
	        	request.setTpStorno(stornoDTO.getTipoStorno());
	        	request.setIdConsuntivo(stornoDTO.getIdConsuntivo());
	        	request.setMatricola(stornoDTO.getMatricola());
	        	JwtTokenResponse jwtToken = odlClient.getToken(EDataIntegrationEntity.GEOCALL_INVIO_STORNO);
	        	if(log.isDebugEnabled())
					log.debug("{} Request: {}", GeoCallInterface.SEND_STORNO_GEOCALL_INTERFACE.getName(), EamEtlUtils.writeJaxbObjectAsString(request));
	        	StornoResponse response = odlClient.sendStorno(request, jwtToken);
				if (StatusX4Esito.KO.name().equals(response.getESITO())) {
                    throw new EAMException(response.getCODICEERRORE(), response.getDESCRIZIONEERRORE());
                } else if (StatusX4Esito.OK.name().equals(response.getESITO())) {
                    return true;
                }
			}
		} catch(Exception e) {
			log.error("Generic Error: {}", e.getMessage(), e);
			throw new EAMException(EAMErrorMapping.EAM_0000.getEamCode(), e.getMessage(), e);
		}
		return false;
	}

	public void sendSapStorno(String topic, String uuidResend, String payload)
	{
		Instant startDate	= Instant.now();
		String uuid 		= Uuid.randomUuid().toString();
		Long startTime		= System.currentTimeMillis();

		X4EsitoIntRequest x4EsitoIntRequest = new X4EsitoIntRequest();
		x4EsitoIntRequest.setIdinterfaccia(GeoCallInterface.SEND_STORNO_GEOCALL_INTERFACE.getName());
		x4EsitoIntRequest.setSource(EAM);
		x4EsitoIntRequest.setDest(GEOCALL);
		x4EsitoIntRequest.setKafkatopic(topic);
		x4EsitoIntRequest.setMessage(payload);
		x4EsitoIntRequest.setUniqueid(uuid);
		x4EsitoIntRequest.setStart_elab(EamEtlUtils.esitoDate(startDate));
		x4EsitoIntRequest.setEsito(StatusX4Esito.OK.name());

		try {
			if(Assorted.notNull(payload))
			{
				if(Assorted.notNull(uuidResend))
				{
					x4EsitoIntRequest.setUniqueid(uuidResend);
					X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
					x4EsitoIntRequest.setParent(Assorted.notNull(parent) ? parent.getX4EsitoIntId() : null);
				}
				else
		    		x4EsitoIntRequest.setRetry(0L);

				ObjectMapper objectMapper = new ObjectMapper();
				objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				StornoDTO storno = objectMapper.readValue(payload, StornoDTO.class);
				if(Assorted.isNull(storno))
					throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription());
				x4EsitoIntRequest.setWonum(storno.getWonum());
				StornoRequest request = new StornoRequest();
				request.setCodiceOrdineEsterno(storno.getWonum());
				request.setTpStorno(storno.getTipoStorno());
				request.setIdConsuntivo(storno.getIdConsuntivo());
				request.setMatricola(storno.getMatricola());
				JwtTokenResponse jwtToken = odlClient.getToken(EDataIntegrationEntity.GEOCALL_INVIO_STORNO);
				if(log.isDebugEnabled())
					log.debug(GeoCallInterface.SEND_STORNO_GEOCALL_INTERFACE.getName() + " Request: " + EamEtlUtils.writeJaxbObjectAsString(request));
				StornoResponse response = odlClient.sendStorno(request, jwtToken);
				X4WorkorderResponse odl = x4WorkorderApiService.retrieveWorkorder(storno.getWonum());
				if(Assorted.notNull(odl) && Assorted.isNotEmpty(odl.getMember())) {
					X4WorkorderRequest req = new X4WorkorderRequest();
					req.setX4stornogeocall(Assorted.isNotEmpty(response.getESITO()) ? response.getESITO() : StatusX4Esito.KO.name());
					req.setX4errsap(Assorted.isNotEmpty(response.getDESCRIZIONEERRORE()) ? response.getDESCRIZIONEERRORE() : null);
					req.setHref(odl.getMember().get(0).getHref());
					MaximoResponse updateWorkorder = x4WorkorderApiService.updateWorkorder(req.getHref(), req);
					if (updateWorkorder.getHttpCode() != HttpStatus.OK.value())
						throw new EAMException(EAMErrorMapping.EAM_0000.getEamCode(), EamEtlUtils.errorGenericMessage(updateWorkorder.getError()));
				}
				if (StatusX4Esito.KO.name().equals(response.getESITO()))
					throw new EAMException(response.getCODICEERRORE(), response.getDESCRIZIONEERRORE());
			}

		} catch(EAMException q) {
			log.error(q.getMessage());
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(q.getErrorCode());
			x4EsitoIntRequest.setDesc_esito(q.getMessage());
		} catch(java.lang.Exception q) {
			log.error("Generic Error: " + q.getMessage(), q);
			x4EsitoIntRequest.setEsito(StatusX4Esito.KO.name());
			x4EsitoIntRequest.setCodesito(EAMErrorMapping.EAM_0000.getEamCode());
			x4EsitoIntRequest.setDesc_esito(EAMErrorMapping.EAM_0000.getErrorDescription() + " " + q.getMessage());
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
		Long endTime = System.currentTimeMillis();
		log.info("end call sendSapStorno(uuidResend={}, payload={}) |{}|", uuidResend, payload, endTime - startTime);
	}

}
