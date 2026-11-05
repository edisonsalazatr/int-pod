package it.terna.nas.service.exposure.handler;

import static it.terna.nas.service.material.utility.Catalog.EAMErrorMapping.*;
import static it.terna.nas.service.material.utility.Constants.*;
import static it.terna.nas.service.material.utility.EamEtlUtils.buildEsitoRequest;
import static it.terna.nas.service.material.utility.EamEtlUtils.currentDateAsFormattedString;
import static it.terna.nas.service.material.utility.EamEtlUtils.esitoDate;
import static it.terna.nas.service.material.utility.EamEtlUtils.getMonthAndYear;
import static it.terna.nas.service.material.utility.EamEtlUtils.logErrorAndSetFailureStatus;
import static it.terna.nas.service.material.utility.EamEtlUtils.sendEsitoIntRequest;
import static it.terna.nas.service.material.utility.EamEtlUtils.splitPipeString;
import static it.terna.nas.service.material.utility.EamEtlUtils.writeObjectAsString;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.xml.bind.JAXBElement;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.sap.*;
import it.terna.nas.service.material.dto.sap.*;
import it.terna.nas.service.material.utility.*;
import lombok.Getter;
import org.apache.kafka.common.Uuid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import it.terna.kernel.basement.exception.RestException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Values;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.http.client.RestHandler;
import it.terna.kernel.http.configuration.data.RestEntityInfo;
import it.terna.kernel.service.exposure.AbstractHandler;
import it.terna.kernel.soap.clientgen.sap.acquisition.dc.QueryWBS;
import it.terna.kernel.soap.clientgen.sap.acquisition.dc.QueryWBSResponse;
import it.terna.kernel.soap.clientgen.sap.acquisition.dc.ZfiltroArrayGenericoI;
import it.terna.kernel.soap.clientgen.sap.acquisition.dc.ZfiltroArrayLivelliI;
import it.terna.kernel.soap.clientgen.sap.acquisition.dc.ZlivelloWbxTabI;
import it.terna.kernel.soap.clientgen.sap.acquisition.dc.ZqwbsInputMessageFilteI;
import it.terna.kernel.soap.clientgen.sap.acquisition.dc.ZqwbsOutputMessageResuO;
import it.terna.kernel.soap.clientgen.sap.acquisition.dc.Zstring30TabI;
import it.terna.kernel.soap.clientgen.sap.acquisition.dc.ZwBreakdownStructureTabO;
import it.terna.kernel.soap.clientgen.sap.acquisition.dc.ZworkBreakdownStructureO;
import it.terna.kernel.soap.clientgen.sap.acquisition.ntw.ZacquisizioneNtw;
import it.terna.kernel.soap.clientgen.sap.acquisition.ntw.ZacquisizioneNtwResponse;
import it.terna.kernel.soap.clientgen.sap.acquisition.ntw.ZpsSAcquisizioneNtw;
import it.terna.kernel.soap.clientgen.sap.acquisition.ntw.ZpsSListaStati;
import it.terna.kernel.soap.clientgen.sap.acquisition.ntw.ZpsSListaWbs;
import it.terna.kernel.soap.clientgen.sap.acquisition.ntw.ZpsSOperazioniNtw;
import it.terna.kernel.soap.clientgen.sap.acquisition.ntw.ZpsTListaStati;
import it.terna.kernel.soap.clientgen.sap.acquisition.ntw.ZpsTListaWbs;
import it.terna.kernel.soap.clientgen.sap.acquisition.odm.Zfmpms4EamOrdDiMan;
import it.terna.kernel.soap.clientgen.sap.acquisition.odm.Zfmpms4EamOrdDiManResponse;
import it.terna.kernel.soap.clientgen.sap.acquisition.odm.Zstpms4InputOrdDiMan;
import it.terna.kernel.soap.clientgen.sap.acquisition.odm.Zstpms4OutputOrdDiMan;
import it.terna.kernel.soap.clientgen.sap.acquisition.odm.Zttpms4InputOrdDiMan;
import it.terna.kernel.soap.clientgen.sap.acquisition.oi.Zfmpms4EamOrdiniInterni;
import it.terna.kernel.soap.clientgen.sap.acquisition.oi.Zfmpms4EamOrdiniInterniResponse;
import it.terna.kernel.soap.clientgen.sap.acquisition.oi.Zstpms4InputOrdiniInterni;
import it.terna.kernel.soap.clientgen.sap.acquisition.oi.Zstpms4OutputOrdiniInterni;
import it.terna.kernel.soap.clientgen.sap.acquisition.oi.Zttpms4InputOrdiniInterni;
import it.terna.kernel.soap.clientgen.sap.closure.ntw.ZchiusuraNtw;
import it.terna.kernel.soap.clientgen.sap.closure.ntw.ZchiusuraNtwResponse;
import it.terna.kernel.soap.clientgen.sap.closure.odm.ChiusuraODM;
import it.terna.kernel.soap.clientgen.sap.closure.odm.ChiusuraODMResponse;
import it.terna.kernel.soap.clientgen.sap.consuntivazione.callback.RichiestaConsuntivazioneCallback;
import it.terna.kernel.soap.clientgen.sap.consuntivazione.callback.RichiestaConsuntivazioneCallbackResponse;
import it.terna.kernel.soap.clientgen.sap.consuntivazione.confirm.*;
import it.terna.kernel.soap.clientgen.sap.consuntivazione.controlloPeriodo.ZmosCheckPeriodContFi;
import it.terna.kernel.soap.clientgen.sap.consuntivazione.controlloPeriodo.ZmosCheckPeriodContFiResponse;
import it.terna.kernel.soap.clientgen.sap.storno.counter.ZmosWsResearchCounterOdm;
import it.terna.kernel.soap.clientgen.sap.storno.counter.ZmosWsResearchCounterOdmResponse;
import it.terna.kernel.soap.clientgen.sap.storno.counter.ZstCounterOdm;
import it.terna.kernel.soap.clientgen.sap.storno.tot.*;
import it.terna.nas.service.configuration.properties.OAuth2Properties;
import it.terna.nas.service.configuration.properties.SapProperties;
import it.terna.nas.service.dal.entity.eam.X4EsitoInt;
import it.terna.nas.service.dal.service.X4EsitoIntApiService;
import it.terna.nas.service.dal.service.X4EsitoIntQueryService;
import it.terna.nas.service.dal.service.sap.*;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.client.SapClient;
import it.terna.nas.service.exposure.handler.rest.JWTAwareRestClient;
import it.terna.nas.service.exposure.ws.maximo.JwtTokenResponse;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import it.terna.nas.service.exposure.ws.sap.*;
import it.terna.nas.service.material.dto.StornoDTO;
import it.terna.nas.service.material.dto.X4EsitoIntRequest;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.SapInterface;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SapHandler extends AbstractHandler {

    @Autowired
    private X4EsitoIntQueryService x4EsitoIntQueryService;

    @Autowired
    private X4EsitoIntApiService x4EsitoIntApiService;

    @Autowired
    private SapClient sapClient;

    @Autowired
    private X4OrdIntQueryService x4OrdIntQueryService;

    @Autowired
    private X4OdMQueryService x4OdMQueryService;

    @Autowired
    private X4NtkQueryService x4NtkQueryService;

    @Autowired
    private X4OrdIntApiService x4OrdIntApiService;

    @Autowired
    private X4OdMApiService x4OdMApiService;

    @Autowired
    private X4WBEApiService x4WBEApiService;

    @Autowired
    private X4OdMOpApiService x4OdMOpApiService;

    @Autowired
    private X4NtkApiService x4NtkApiService;

    @Autowired
    private X4NtkOpApiService x4NtkOpApiService;

    @Autowired
    private JWTAwareRestClient jwtAwareRestClient;

    @Autowired
    private RestHandler restHandler;

    @Autowired
    private OAuth2Properties oAuth2Sap;

    @Autowired
    private SapProperties sapProperties;

    @Autowired
    private X4SapFilterQueryService x4SapFilterQueryService;

    @Autowired
    private X4WorkOrderApiService x4WorkOrderApiService;

    @Autowired
    private X4WorkorderQueryService x4WorkorderQueryService;

    @Autowired
    private X4LabtransApiService x4LabtransApiService;

    @Autowired
    private X4WBEQueryService x4WBEQueryService;

    @Autowired
    private X4StatiSapQueryService x4StatiSapQueryService;

    @Autowired
    private KafkaProducerHandler kafkaHandler;

    @Autowired
    private BrokerEntityConfiguration brokerEntities;

    @Autowired
    private GeoCallHandler geoCallHandler;

    private static final BigDecimal zero = new BigDecimal(0);

    @Getter
    private enum WorkflowStep {
        STEP_0("STEP_0", "0000"),
        STEP_1("STEP_1", "1000"),
        STEP_2("STEP_2", "1100"),
        STEP_3("STEP_3", "1110"),
        STEP_4("STEP_4", "1111"),
        LAST_STEP_KO("STEP_KO", "0"),
        LAST_STEP_OK("STEP_OK", "1"),
        STEP_CHECK("STEP_CHECK", "11111");

        private final String code;
        private final String mask;

        WorkflowStep(String code, String mask) {
            this.code = code;
            this.mask = mask;
        }

    }

    // ----------------------------------------------------------- OI -----------------------------

    /**
     * Gestisce l'acquisizione degli ordini interni (OI) da SAP, inviando i dati acquisiti a un topic Kafka e registrando i risultati.
     * Esegue in parallelo o in sequenza l'elaborazione degli ordini interni a seconda della configurazione.
     *
     * @param topic      il topic Kafka per l'acquisizione dei dati.
     * @param uuidResend l'UUID da utilizzare per l'invio di una richiesta di riesecuzione, se presente.
     * @param payload    il payload JSON contenente i dati da elaborare.
     */
    @SuppressWarnings("null")
    public void acquisitionOrdInt(String topic, String uuidResend, String payload) {
        Instant startDate = Instant.now();
        String uuid = Uuid.randomUuid().toString();
        X4EsitoIntRequest x4EsitoIntRequest = buildEsitoRequest(topic, payload, uuid, startDate, Catalog.SapInterface.ACQUISITION_OI, Constants.EAM_SAP, Constants.SAP);
        String result = null;
        long startTime = System.currentTimeMillis();
        log.info("Starting acquisition of Ordini Interni from SAP - UUID: {}, Topic: {}", uuid, topic);
        try {
            if (Objects.isNull(payload)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), topic);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + " [" + topic + "]");
            }
            if (Objects.nonNull(uuidResend)) {
                x4EsitoIntRequest.setUniqueid(uuidResend);
                X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
                x4EsitoIntRequest.setParent(Objects.nonNull(parent) ? parent.getX4EsitoIntId() : null);
            }

            KafkaX4AcquisitionData request = new Gson().fromJson(payload, KafkaX4AcquisitionData.class);
            if (Objects.isNull(request)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), request);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + "[" + request + "]");
            }
            sapProperties.setFilters(
                    x4SapFilterQueryService.getFilter().stream()
                            .map(f -> new SapProperties.FilterSap(f.getFiltro(), f.getTipoSpesa(), f.getTipoAttivita(), f.getTipoOrdine(), f.getLivello(), f.getStato()))
                            .toList()
            );

            List<X4OrdInt> x4OrdInts = x4OrdIntQueryService.getRequests();

            List<Zfmpms4EamOrdiniInterni> interniList = buildX4OrdIntRequests(sapProperties.getFilters(), x4OrdInts);
            if (Assorted.isEmpty(interniList)) {
                log.error("{}", EAM_0028.getErrorDescription());
                throw new EAMException(EAM_0028.getEamCode(), EAM_0028.getErrorDescription());
            }
            result = processOrdiniInterniSequenziali(interniList);
        } catch (EAMException e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
        } finally {
            if(Objects.nonNull(result)) {
                x4EsitoIntRequest.setDesc_esito(result);
            }
            x4EsitoIntRequest.setEnd_elab(esitoDate(Instant.now()));
        }
        sendEsitoIntRequest(x4EsitoIntRequest, x4EsitoIntApiService);
        log.info("Completed acquisition of Ordini Interni from SAP - UUID: {}, Topic: {}, Duration: {} seconds", uuid, topic, (System.currentTimeMillis() - startTime) / 1000);
    }

    private List<Zfmpms4EamOrdiniInterni> buildX4OrdIntRequests(List<SapProperties.FilterSap> filters, List<X4OrdInt> x4OrdInts) {
        if (!Assorted.isNotEmpty(filters)) {
            return new ArrayList<>();
        }

        boolean hasX4OrdInts = Objects.nonNull(x4OrdInts) && Assorted.isNotEmpty(x4OrdInts);
        List<Zfmpms4EamOrdiniInterni> requestsList = new ArrayList<>();

        filters.stream()
                .filter(Objects::nonNull)
                .filter(f -> OI.equalsIgnoreCase(f.getFiltro()))
                .forEach(f -> {
                    try {
                        X4OrdInt oi = new X4OrdInt();

                        if (Objects.nonNull(f.getTipoOrdine())) oi.setTipooi(f.getTipoOrdine());

                        if (!hasX4OrdInts) {
                            if (Objects.nonNull(f.getStato())) oi.setStatosistema(f.getStato());
                            oi.setCreateddate(EamEtlUtils.stringToSqlDate(sapProperties.getStartDate()));
                            oi.setChangeddate(EamEtlUtils.stringToSqlDate(sapProperties.getStartDate()));
                        } else {
                            if (Objects.nonNull(sapProperties.getStatoDummy()))
                                oi.setStatosistema(sapProperties.getStatoDummy());

                            x4OrdInts.stream()
                                    .filter(x -> Objects.equals(x.getTipooi(), f.getTipoOrdine()))
                                    .findFirst()
                                    .ifPresentOrElse(
                                            match -> {
                                                oi.setCreateddate(match.getCreateddate());
                                                oi.setChangeddate(match.getChangeddate());
                                            },
                                            () -> {
                                                oi.setCreateddate(EamEtlUtils.stringToSqlDate(sapProperties.getStartDate()));
                                                oi.setChangeddate(EamEtlUtils.stringToSqlDate(sapProperties.getStartDate()));
                                            }
                                    );
                        }

                        requestsList.add(buildRequestOI(oi));
                    } catch (Exception e) {
                        throw new RuntimeException("Errore durante l'elaborazione della richiesta per Stato: " + f.getStato() +
                                ", TipoOrdine: " + f.getTipoOrdine(), e);
                    }
                });

        return new ArrayList<>(requestsList.stream()
                .filter(r -> Objects.nonNull(r.getIInputOrdini()) && Objects.nonNull(r.getIInputOrdini().getItem()))
                .collect(Collectors.toMap(
                        r -> r.getIInputOrdini().getItem().stream()
                                .map(i -> Arrays.asList(i.getTipoOrdine(), i.getStatoOggetto()))
                                .filter(Objects::nonNull)
                                .toList(),
                        r -> r,
                        (existing, replacement) -> existing
                ))
                .values());
    }

    private String processOrdiniInterniSequenziali(List<Zfmpms4EamOrdiniInterni> interniList) {
        int count = 1;
        int error = 0;
        int success = 0;
        List<String> errorMessages = new ArrayList<>();
        for (Zfmpms4EamOrdiniInterni oi : interniList) {
            log.info("Starting processing OI acquisition - Progress: {}/{}", count, interniList.size());
            try {
                JwtTokenResponse jwtTokenResponse = getToken(EDataIntegrationEntity.ACQUISIZIONE_OI);
                String jwtToken = Objects.nonNull(jwtTokenResponse) && jwtTokenResponse.getHttpCode() == 200 ? jwtTokenResponse.getAccessToken() : null;
                Zfmpms4EamOrdiniInterniResponse response = sapClient.requestToSAPOI(oi, jwtToken);
                elaborationX4OrdInt(response);
                success++;
            } catch (Exception e) {
                error++;
                String tipoOrdine = oi.getIInputOrdini() != null && oi.getIInputOrdini().getItem() != null && !oi.getIInputOrdini().getItem().isEmpty()
                        ? oi.getIInputOrdini().getItem().get(0).getTipoOrdine()
                        : "N/A";
                String statoOggetto = oi.getIInputOrdini() != null && oi.getIInputOrdini().getItem() != null && !oi.getIInputOrdini().getItem().isEmpty()
                        ? oi.getIInputOrdini().getItem().get(0).getStatoOggetto()
                        : "N/A";
                String dataCreazioneDa = oi.getIInputOrdini() != null && oi.getIInputOrdini().getItem() != null && !oi.getIInputOrdini().getItem().isEmpty()
                        ? oi.getIInputOrdini().getItem().get(0).getDataCreazioneDa()
                        : "N/A";
                String dataCreazioneA = oi.getIInputOrdini() != null && oi.getIInputOrdini().getItem() != null && !oi.getIInputOrdini().getItem().isEmpty()
                        ? oi.getIInputOrdini().getItem().get(0).getDataCreazioneA()
                        : "N/A";
                String dataModificaDa = oi.getIInputOrdini() != null && oi.getIInputOrdini().getItem() != null && !oi.getIInputOrdini().getItem().isEmpty()
                        ? oi.getIInputOrdini().getItem().get(0).getDataModificaDa()
                        : "N/A";
                String dataModificaA = oi.getIInputOrdini() != null && oi.getIInputOrdini().getItem() != null && !oi.getIInputOrdini().getItem().isEmpty()
                        ? oi.getIInputOrdini().getItem().get(0).getDataModificaA()
                        : "N/A";
                String errMsg = String.format("[%d/%d] OI=%s: %s",
                        count, interniList.size(),
                        String.format("TipoOrdine: %s, StatoOggetto: %s, DataCreazioneDa: %s, DataCreazioneA: %s, DataModificaDa: %s, DataModificaA: %s", tipoOrdine, statoOggetto, dataCreazioneDa, dataCreazioneA, dataModificaDa, dataModificaA),
                        e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
                errorMessages.add(errMsg);
                log.error("Errore durante l'elaborazione dell'ordine interno: {}", oi, e);
            } finally {
                count++;
            }
        }

        String msg = String.format("Completed processing OI acquisition - Total: %d, Success: %d, Error: %d", interniList.size(), success, error);
        log.info(msg);
        if (errorMessages.isEmpty()) {
            return msg;
        }

        String full = String.format("ERR %d/%d\n", error, interniList.size()) + String.join("\n", errorMessages);
        return full.length() > 2000 ? full.substring(0, 1997) + "..." : full;
    }

    private Zfmpms4EamOrdiniInterni buildRequestOI(X4OrdInt x4OrdInt) {
        Zfmpms4EamOrdiniInterni finalRequest = new Zfmpms4EamOrdiniInterni();
        Zstpms4InputOrdiniInterni itemOrdineInterno = new Zstpms4InputOrdiniInterni();
        List<Zstpms4InputOrdiniInterni> inputOrdiniInternis = new ArrayList<>();
        Zttpms4InputOrdiniInterni inputOrdiniInterni = new Zttpms4InputOrdiniInterni();
        itemOrdineInterno.setDataCreazioneDa(String.valueOf(x4OrdInt.getCreateddate()));
        itemOrdineInterno.setDataCreazioneA(currentDateAsFormattedString(sapProperties.getFormatDate()));
        itemOrdineInterno.setDataModificaDa(String.valueOf(x4OrdInt.getChangeddate()));
        itemOrdineInterno.setDataModificaA(currentDateAsFormattedString(sapProperties.getFormatDate()));
        itemOrdineInterno.setTipoOrdine(x4OrdInt.getTipooi());
        itemOrdineInterno.setStatoOggetto(x4OrdInt.getStatosistema());
        inputOrdiniInternis.add(itemOrdineInterno);
        inputOrdiniInterni.getItem().addAll(inputOrdiniInternis);
        finalRequest.setIInputOrdini(inputOrdiniInterni);
        return finalRequest;
    }

    private void elaborationX4OrdInt(Zfmpms4EamOrdiniInterniResponse response) throws RestException {
        if (Assorted.notNull(response, response.getOOutputOrdini(), response.getOOutputOrdini().getItem()) && Assorted.isNotEmpty(response.getOOutputOrdini().getItem())) {
            MaximoResponse maximoResponse;
            for (Zstpms4OutputOrdiniInterni item : response.getOOutputOrdini().getItem()) {
                X4OrdIntResponse x4OrdIntResponse = x4OrdIntApiService.getById(item.getId());
                if (Objects.nonNull(x4OrdIntResponse) && x4OrdIntResponse.getHttpCode() == 200) {
                    X4OrdIntRequest request = populateX4OrdInt(item);
                    if (Assorted.notNull(x4OrdIntResponse, x4OrdIntResponse.getMember()) && Assorted.isNotEmpty(x4OrdIntResponse.getMember())) {
                        X4OrdIntResponse.X4OrdIntResponseItem member = x4OrdIntResponse.getMember().get(Values.FIRST_ELEMENT);
                        maximoResponse = x4OrdIntApiService.update(member.getHref(), request);
                        if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                            log.info("X4ORDINT update eseguita: [{}]", maximoResponse.getHttpCode());
                        } else {
                            log.error("X4ORDINT update fallita: [{}]", Objects.nonNull(maximoResponse) ? maximoResponse.getHttpCode() : null);
                        }
                    } else {
                        maximoResponse = x4OrdIntApiService.insert(request);
                        if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200)
                            log.info("X4ORDINT insert eseguita: [{}]", maximoResponse.getHttpCode());
                        else
                            log.error("X4ORDINT insert fallita: [{}]", Objects.nonNull(maximoResponse) ? maximoResponse.getHttpCode() : null);
                    }
                } else {
                    log.error("Error for x4OrdIntResponse: [{}]", Objects.nonNull(x4OrdIntResponse) ? x4OrdIntResponse.getHttpCode() : null);
                }
            }
        } else {
            if (Objects.nonNull(response) && Objects.nonNull(response.getOBapiret()) && Assorted.isNotEmpty(response.getOBapiret().getItem())) {
                log.error("Error Zfmpms4EamOrdiniInterniResponse: {}", response.getOBapiret().getItem().get(Values.FIRST_ELEMENT).getMessage());
            }
        }
    }

    private X4OrdIntRequest populateX4OrdInt(Zstpms4OutputOrdiniInterni item) {
        X4OrdIntRequest request = new X4OrdIntRequest();
        request.setIdoi(item.getId());
        request.setDescription(item.getDescrizione());
        request.setTipooi(item.getTipoOrdine());
        request.setStatosistema(item.getStatoSistema());
        request.setStatoutente(item.getStatoUtente());
        request.setSocieta(item.getSocieta());
        request.setCdcresp(item.getCentroDiCostoResponsabile());
        request.setCodcliente(item.getCodiceCliente());
        request.setCdccosti(item.getCentroDiCosto());
        request.setCreateddate(item.getDataCreazione());
        request.setChangeddate(Objects.equals(item.getDataModifica(), sapProperties.getNoDate()) ? item.getDataCreazione() : item.getDataModifica());
        request.setProfitcenter(item.getProfitCenter());
        request.setCatord(item.getCatOrdine());
        request.setClasseoggetto(item.getClasseOggetto());
        return request;
    }

    // ----------------------------------------------------------- ODM -----------------------------

    /**
     * Gestisce l'acquisizione degli ordini di manutenzione (ODM) da SAP, inviando i dati acquisiti a un topic Kafka e registrando i risultati.
     * Esegue in parallelo o in sequenza l'elaborazione degli ordini di manutenzione a seconda della configurazione.
     *
     * @param topic      il topic Kafka per l'acquisizione dei dati.
     * @param uuidResend l'UUID da utilizzare per l'invio di una richiesta di riesecuzione, se presente.
     * @param payload    il payload JSON contenente i dati da elaborare.
     */
    public void acquisitionOdM(String topic, String uuidResend, String payload) {
        Instant startDate = Instant.now();
        String uuid = Uuid.randomUuid().toString();
        X4EsitoIntRequest x4EsitoIntRequest = buildEsitoRequest(topic, payload, uuid, startDate, Catalog.SapInterface.ACQUISITION_ODM, Constants.EAM_SAP, Constants.SAP);
        String result = null;
        long startTime = System.currentTimeMillis();
        log.info("Starting acquisition of Ordini di Manutenzione from SAP - UUID: {}, Topic: {}", uuid, topic);
        try {
            if (Objects.isNull(payload)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), topic);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + " [" + topic + "]");
            }
            if (Objects.nonNull(uuidResend)) {
                x4EsitoIntRequest.setUniqueid(uuidResend);
                X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
                x4EsitoIntRequest.setParent(Objects.nonNull(parent) ? parent.getX4EsitoIntId() : null);
            }

            KafkaX4AcquisitionData request = new Gson().fromJson(payload, KafkaX4AcquisitionData.class);
            if (Objects.isNull(request)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), request);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + "[" + request + "]");
            }
            sapProperties.setFilters(
                    x4SapFilterQueryService.getFilter().stream()
                            .map(f -> new SapProperties.FilterSap(f.getFiltro(), f.getTipoSpesa(), f.getTipoAttivita(), f.getTipoOrdine(), f.getLivello(), f.getStato()))
                            .toList()
            );
            List<X4ODM> x4ODMS = x4OdMQueryService.getRequests();
            boolean isFirstTime = Objects.isNull(x4ODMS) || Assorted.isEmpty(x4ODMS);
            List<Zfmpms4EamOrdDiMan> manList = buildX4OdmsRequests(sapProperties.getFilters(), x4ODMS);
            if (Assorted.isEmpty(manList)) {
                log.error("{}", EAM_0028.getErrorDescription());
                throw new EAMException(EAM_0028.getEamCode(), EAM_0028.getErrorDescription());
            }
            result = processOrdiniDiManutenzioneSequenziali(manList, isFirstTime);
        } catch (EAMException e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
        } finally {
            if(Objects.nonNull(result)) {
                x4EsitoIntRequest.setDesc_esito(result);
            }
            x4EsitoIntRequest.setEnd_elab(esitoDate(Instant.now()));
        }
        sendEsitoIntRequest(x4EsitoIntRequest, x4EsitoIntApiService);
        log.info("Completed acquisition of Ordini Interni from SAP - UUID: {}, Topic: {}, Duration: {} seconds", uuid, topic, (System.currentTimeMillis() - startTime) / 1000);
    }

    private List<Zfmpms4EamOrdDiMan> buildX4OdmsRequests(List<SapProperties.FilterSap> filters, List<X4ODM> x4odms) {
        if (!Assorted.isNotEmpty(filters)) {
            return new ArrayList<>();
        }

        boolean hasX4Odms = Objects.nonNull(x4odms) && Assorted.isNotEmpty(x4odms);
        List<Zfmpms4EamOrdDiMan> requestsList = new ArrayList<>();

        filters.stream()
                .filter(Objects::nonNull)
                .filter(f -> ODM.equalsIgnoreCase(f.getFiltro()))
                .forEach(f -> {
                    try {
                        X4ODM odm = new X4ODM();

                        if (Objects.nonNull(f.getTipoOrdine()))   odm.setTipoordine(f.getTipoOrdine());
                        if (Objects.nonNull(f.getTipoAttivita())) odm.setTipoattivita(f.getTipoAttivita());

                        if (!hasX4Odms) {
                            if (Objects.nonNull(f.getStato())) odm.setStatosistema(f.getStato());
                            odm.setCreateddate(EamEtlUtils.stringToSqlDate(sapProperties.getStartDate()));
                            odm.setChangeddate(EamEtlUtils.stringToSqlDate(sapProperties.getStartDate()));
                        } else {
                            if (Objects.nonNull(sapProperties.getStatoDummy()))
                                odm.setStatosistema(sapProperties.getStatoDummy());

                            x4odms.stream()
                                    .filter(x -> Objects.equals(x.getTipoordine(), f.getTipoOrdine()) &&
                                            Objects.equals(x.getTipoattivita(), f.getTipoAttivita()))
                                    .findFirst()
                                    .ifPresentOrElse(
                                            match -> {
                                                odm.setCreateddate(match.getCreateddate());
                                                odm.setChangeddate(match.getChangeddate());
                                            },
                                            () -> {
                                                odm.setCreateddate(EamEtlUtils.stringToSqlDate(sapProperties.getStartDate()));
                                                odm.setChangeddate(EamEtlUtils.stringToSqlDate(sapProperties.getStartDate()));
                                            }
                                    );
                        }

                        requestsList.add(buildRequestOdM(odm));
                    } catch (Exception e) {
                        throw new RuntimeException("Errore durante l'elaborazione della richiesta per Stato: " + f.getStato() +
                                ", TipoOrdine: " + f.getTipoOrdine() +
                                ", TipoAttivita: " + f.getTipoAttivita(), e);
                    }
                });

        return new ArrayList<>(requestsList.stream()
                .filter(r -> Objects.nonNull(r.getIInputOrdDiMan()) && Objects.nonNull(r.getIInputOrdDiMan().getItem()))
                .collect(Collectors.toMap(
                        r -> r.getIInputOrdDiMan().getItem().stream()
                                .map(i -> Arrays.asList(i.getTipoOrdine(), i.getStatoSistema(), i.getTipoAttivaPm()))
                                .filter(Objects::nonNull)
                                .toList(),
                        r -> r,
                        (existing, replacement) -> existing
                ))
                .values());
    }

    private String processOrdiniDiManutenzioneSequenziali(List<Zfmpms4EamOrdDiMan> manList, boolean isFirstTime) {
        int count = 1;
        int error = 0;
        int success = 0;
        List<String> errorMessages = new ArrayList<>();
        for (Zfmpms4EamOrdDiMan finalRequest : manList) {
            log.info("Request to SAP for ODM acquisition - Progress: {}/{}", count, manList.size());
            try {
                JwtTokenResponse jwtTokenResponse = getToken(EDataIntegrationEntity.ACQUISIZIONE_ODM);
                String jwtToken = Objects.nonNull(jwtTokenResponse) && jwtTokenResponse.getHttpCode() == 200 ? jwtTokenResponse.getAccessToken() : null;
                Zfmpms4EamOrdDiManResponse response = sapClient.requestToSAPOdM(finalRequest, jwtToken);
                if(isFirstTime)
                    elaborationX4ODMFirst(response);
                else
                    elaborationX4OdM(response);
                success++;
            } catch (Exception e) {
                error++;
                String tipoOrdine = finalRequest.getIInputOrdDiMan() != null && finalRequest.getIInputOrdDiMan().getItem() != null && !finalRequest.getIInputOrdDiMan().getItem().isEmpty()
                        ? finalRequest.getIInputOrdDiMan().getItem().get(0).getTipoOrdine()
                        : "N/A";
                String tipoAttivita = finalRequest.getIInputOrdDiMan() != null && finalRequest.getIInputOrdDiMan().getItem() != null && !finalRequest.getIInputOrdDiMan().getItem().isEmpty()
                        ? finalRequest.getIInputOrdDiMan().getItem().get(0).getTipoAttivaPm()
                        : "N/A";
                String statoSistema = finalRequest.getIInputOrdDiMan() != null && finalRequest.getIInputOrdDiMan().getItem() != null && !finalRequest.getIInputOrdDiMan().getItem().isEmpty()
                        ? finalRequest.getIInputOrdDiMan().getItem().get(0).getStatoSistema()
                        : "N/A";
                String dataCreazioneDa = finalRequest.getIInputOrdDiMan() != null && finalRequest.getIInputOrdDiMan().getItem() != null && !finalRequest.getIInputOrdDiMan().getItem().isEmpty()
                        ? finalRequest.getIInputOrdDiMan().getItem().get(0).getDataCreazioneDa()
                        : "N/A";
                String dataCreazioneA = finalRequest.getIInputOrdDiMan() != null && finalRequest.getIInputOrdDiMan().getItem() != null && !finalRequest.getIInputOrdDiMan().getItem().isEmpty()
                        ? finalRequest.getIInputOrdDiMan().getItem().get(0).getDataCreazioneA()
                        : "N/A";
                String dataModificaDa = finalRequest.getIInputOrdDiMan() != null && finalRequest.getIInputOrdDiMan().getItem() != null && !finalRequest.getIInputOrdDiMan().getItem().isEmpty()
                        ? finalRequest.getIInputOrdDiMan().getItem().get(0).getDataModificaDa()
                        : "N/A";
                String dataModificaA = finalRequest.getIInputOrdDiMan() != null && finalRequest.getIInputOrdDiMan().getItem() != null && !finalRequest.getIInputOrdDiMan().getItem().isEmpty()
                        ? finalRequest.getIInputOrdDiMan().getItem().get(0).getDataModificaA()
                        : "N/A";
                String errMsg = String.format("[%d/%d] ODM=%s: %s",
                        count, manList.size(),
                        String.format("TipoOrdine: %s, TipoAttivita: %s, StatoSistema: %s, DataCreazioneDa: %s, DataCreazioneA: %s, DataModificaDa: %s, DataModificaA: %s",
                                tipoOrdine, tipoAttivita, statoSistema, dataCreazioneDa, dataCreazioneA, dataModificaDa, dataModificaA),
                        e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
                errorMessages.add(errMsg);
                log.error("Errore durante l'elaborazione dell'ordine di manutenzione: {}", finalRequest, e);
            } finally {
                count++;
            }
        }
        String msg = String.format("Completed processing ODM acquisition - Total: %d, Success: %d, Error: %d", manList.size(), success, error);
        log.info(msg);
        if (errorMessages.isEmpty()) {
            return msg;
        }

        String full = String.format("ERR %d/%d\n", error, manList.size()) + String.join("\n", errorMessages);
        return full.length() > 2000 ? full.substring(0, 1997) + "..." : full;
    }

    private String processDatiContabiliSequenziali(List<QueryWBS> queryWBSList, Boolean isFirstTime, List<X4StatiSap> statiSapList) {
        int count = 1;
        int error = 0;
        int success = 0;
        List<String> errorMessages = new ArrayList<>();
        for (QueryWBS request : queryWBSList) {
            log.info("Request to SAP for WBE acquisition - Progress: {}/{}", count, queryWBSList.size());
            try {
                JwtTokenResponse jwtTokenResponse = getTokenOSB(EDataIntegrationEntity.ACQUISIZIONE_DC);
                String jwtToken = Objects.nonNull(jwtTokenResponse) && jwtTokenResponse.getHttpCode() == 200 ? jwtTokenResponse.getAccessToken() : null;
                QueryWBSResponse response = sapClient.requestToSAPDC(request, jwtToken);
                ZqwbsOutputMessageResuO result = Assorted.notNull(response) && Assorted.notNull(response.getResults()) ? response.getResults() : null;
                ZqwbsOutputMessageResuO filterResponse = checkIfWbeSkip(result, isFirstTime);
                if(Boolean.TRUE.equals(isFirstTime)) {
                    elaborationX4WBEFirst(filterResponse, statiSapList);
                } else {
                    elaborationX4WBE(filterResponse, statiSapList);
                }
                success++;
            } catch (Exception e) {
                error++;
                String dataModifica = request.getFilter() != null ? request.getFilter().getDataUltimaModifica() : "N/A";
                String errMsg = String.format("[%d/%d] WBE=%s: %s",
                        count, queryWBSList.size(),
                        String.format("DataUltimaModifica: %s", dataModifica),
                        e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
                errorMessages.add(errMsg);
                log.error("Errore durante l'elaborazione dei dati contabili: {}", request, e);
            } finally {
                count++;
            }
        }
        String msg = String.format("Completed processing WBE acquisition - Total: %d, Success: %d, Error: %d", queryWBSList.size(), success, error);
        log.info(msg);
        if (errorMessages.isEmpty()) {
            return msg;
        }
        String full = String.format("ERR %d/%d\n", error, queryWBSList.size()) + String.join("\n", errorMessages);
        return full.length() > 2000 ? full.substring(0, 1997) + "..." : full;

    }

    private void elaborationX4WBEFirst(ZqwbsOutputMessageResuO response, List<X4StatiSap> statiSapList)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.info("starting elaborationX4WBE() |{}|", startTime);
        if (Assorted.notNull(response, response.getItem()) && Assorted.isNotEmpty(response.getItem().getItem())) {
            for (ZworkBreakdownStructureO item : response.getItem().getItem()) {
                X4WBE x4wbe = x4WBEQueryService.getById(item.getCodWBx());
                x4wbe = Objects.nonNull(x4wbe) ? x4wbe : new X4WBE();
                populateX4WBEjdbc(item, statiSapList, x4wbe);
                x4WBEQueryService.saveOrUpdate(x4wbe);
                log.info("X4WBE salvataggio eseguito: [{}]", x4wbe.getIdwbe());
            }
        } else {
            log.error("Error QueryWBSResponse is null or empty");
        }
        log.info("Ending elaborationX4WBE() |{}|", System.currentTimeMillis() - startTime);
    }

    private void elaborationX4WBE(ZqwbsOutputMessageResuO response, List<X4StatiSap> statiSapList)
            throws RestException {
        log.info("Starting elaborationX4WBE() |{}|", System.currentTimeMillis());
        if (Assorted.notNull(response, response.getItem()) && Assorted.isNotEmpty(response.getItem().getItem())) {
            for (ZworkBreakdownStructureO item : response.getItem().getItem()) {
                MaximoResponse maximoResponse;
                X4WBEResponse x4WBEResponse = x4WBEApiService.getById(item.getCodWBx());
                if (Objects.nonNull(x4WBEResponse) && x4WBEResponse.getHttpCode() == 200) {
                    X4WBERequest request = populateX4WBE(item, statiSapList);
                    if (Assorted.notNull(x4WBEResponse, x4WBEResponse.getMember()) && Assorted.isNotEmpty(x4WBEResponse.getMember())) {
                        X4WBEResponse.X4WBEResponseItem member = x4WBEResponse.getMember().get(Values.FIRST_ELEMENT);
                        maximoResponse = x4WBEApiService.update(member.getHref(), request);
                        if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                            log.info("X4WBE update eseguita: [{}]", maximoResponse.getHttpCode());
                        } else {
                            log.error("Errore durante l'update di X4WBE: [{}]", Objects.nonNull(maximoResponse) ? maximoResponse.getHttpCode() : null);
                        }
                    } else {
                        maximoResponse = x4WBEApiService.insert(request);
                        if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                            log.info("X4WBE insert eseguita: [{}]", maximoResponse.getHttpCode());
                        } else {
                            log.error("Errore durante l'insert di X4WBE: [{}]", Objects.nonNull(maximoResponse) ? maximoResponse.getHttpCode() : null);
                        }
                    }
                } else {
                    log.error("Error for X4WBEResponse: [{}]", Objects.nonNull(x4WBEResponse) ? x4WBEResponse.getHttpCode() : null);
                }
            }
        } else {
            log.error("Error QueryWBSResponse is null or empty");
        }
    }

    private X4WBERequest populateX4WBE(ZworkBreakdownStructureO item, List<X4StatiSap> statiSapList) {
        X4WBERequest request = new X4WBERequest();
        request.setCdcresp(item.getCdCResp());
        request.setCdcrich(item.getCdCRic());
        request.setChangedate(Assorted.isNull(item.getData()) ? currentDateAsFormattedString(sapProperties.getFormatDate()) : EamEtlUtils.x4WBEDate(item.getData()));
        request.setCodincentivazione(item.getClustInvest());
        request.setCodintervento(item.getCodInterv());
        request.setCreatedate(null);
        request.setDescription(item.getDescrWBx());
        request.setIdwbe(item.getCodWBx());
        request.setIdwbs(Constants.ONE.equals(item.getLivello()) ? null : EamEtlUtils.getFirst12Characters(item.getCodWBx()));
        request.setMotinvestimento(null);
        request.setProfitcenter(item.getProfCent());
        request.setSedetecnica(item.getCodSedeTecnica());
        request.setSocrich(item.getCodSocRic());
        request.setStatosistemasap(item.getStatiSistema());
        request.setStatoutentesap(item.getStatiUtente());
        request.setTipoprog(item.getTipoProg());
        if (Assorted.isNotEmpty(statiSapList)) {
            Map<String, String> statoMap = statiSapList.stream()
                    .collect(Collectors.toMap(X4StatiSap::getStato, X4StatiSap::getCodice));

            request.setStatosistema(Arrays.stream(item.getStatiSistema().split("\\|"))
                    .filter(s -> !s.isEmpty())
                    .map(statoMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("|", "|", "")));
        }

        return request;
    }

    private void populateX4WBEjdbc(ZworkBreakdownStructureO item, List<X4StatiSap> statiSapList, X4WBE request) {
        request.setCdcresp(item.getCdCResp());
        request.setCdcrich(item.getCdCRic());
        LocalDate changeDate = Assorted.isEmpty(item.getData())
                ? LocalDate.now()
                : LocalDate.parse(item.getData(), DateTimeFormatter.ofPattern(DATE_WITHOUT_TIME_FORMAT));
        request.setChangedate(changeDate);
        request.setCodincentivazione(item.getClustInvest());
        request.setCodintervento(item.getCodInterv());
        request.setCreatedate(changeDate);
        request.setDescription(item.getDescrWBx());
        request.setIdwbe(item.getCodWBx());
        request.setIdwbs(ONE.equals(item.getLivello()) ? null : EamEtlUtils.getFirst12Characters(item.getCodWBx()));
        request.setMotinvestimento(null);
        request.setProfitcenter(item.getProfCent());
        request.setSedetecnica(item.getCodSedeTecnica());
        request.setSocrich(item.getCodSocRic());
        request.setStatosistemasap(item.getStatiSistema());
        request.setStatoutentesap(item.getStatiUtente());
        request.setTipoprog(item.getTipoProg());
        request.setHasld(zero);
        request.setPianificazione(zero);
        if (Assorted.isNotEmpty(statiSapList)) {
            Map<String, String> statoMap = statiSapList.stream()
                    .collect(Collectors.toMap(X4StatiSap::getStato, X4StatiSap::getCodice));

            request.setStatosistema(Arrays.stream(item.getStatiSistema().split("\\|"))
                    .filter(s -> !s.isEmpty())
                    .map(statoMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("|", "|", "")));
        }
    }


    private ZqwbsOutputMessageResuO checkIfWbeSkip(ZqwbsOutputMessageResuO response, Boolean firstTime) {
        log.info("starting checkIfWbeSkip() |{}|", System.currentTimeMillis());
        List<Integer> livelliWbe = sapProperties.getFilters().stream().filter(f -> WBE.equalsIgnoreCase(f.getFiltro())).map(SapProperties.FilterSap::getLivello).distinct().toList();
        ZqwbsOutputMessageResuO tempResponse = new ZqwbsOutputMessageResuO();
        ZwBreakdownStructureTabO tempItem = new ZwBreakdownStructureTabO();
        if (Assorted.notNull(response, response.getItem()) && Assorted.isNotEmpty(response.getItem().getItem())) {
            for (ZworkBreakdownStructureO item : response.getItem().getItem()) {
                if (Boolean.TRUE.equals(firstTime)) {
                    if (checkStatiX4WBE(item.getStatiSistema(), item.getStatiUtente()) && livelliWbe.contains(item.getLivello()))
                        tempItem.getItem().add(item);
                    else
                        log.info("X4WBEResponse has been skipped. code = {} level = {} status = {}", item.getCodWBx(), item.getLivello(), item.getStatiSistema());
                } else if (livelliWbe.contains(item.getLivello()))
                    tempItem.getItem().add(item);
                else
                    log.info("X4WBEResponse has been skipped. code = {} level = {}", item.getCodWBx(), item.getLivello());
            }
            tempResponse.setItem(tempItem);
        }
        return tempResponse;
    }

    private boolean checkStatiX4WBE(String statiSistema, String statiUtente) {
        if (Assorted.notNull(statiSistema) && Assorted.notNull(statiUtente)) {
            List<String> systemStatuses = splitPipeString(statiSistema);
            List<String> userStatuses = splitPipeString(statiUtente);
            Boolean systemCheck = systemStatuses.stream().anyMatch(STATI_WBE::contains);
            Boolean userCheck = userStatuses.stream().anyMatch(STATO_AUTO::equalsIgnoreCase);
            return systemCheck || userCheck;
        } else
            return false;
    }

    private Zfmpms4EamOrdDiMan buildRequestOdM(X4ODM x4OdM) {
        it.terna.kernel.soap.clientgen.sap.acquisition.odm.ObjectFactory objectFactory = new it.terna.kernel.soap.clientgen.sap.acquisition.odm.ObjectFactory();
        Zfmpms4EamOrdDiMan finalRequest = objectFactory.createZfmpms4EamOrdDiMan();
        Zstpms4InputOrdDiMan itemOdM = objectFactory.createZstpms4InputOrdDiMan();
        Zttpms4InputOrdDiMan inputOrdDiMan = objectFactory.createZttpms4InputOrdDiMan();
        List<Zstpms4InputOrdDiMan> inputOrdDiManList = new ArrayList<>();
        itemOdM.setDataCreazioneDa(String.valueOf(x4OdM.getCreateddate()));
        itemOdM.setDataCreazioneA(currentDateAsFormattedString(sapProperties.getFormatDate()));
        itemOdM.setDataModificaDa(String.valueOf(x4OdM.getChangeddate()));
        itemOdM.setDataModificaA(currentDateAsFormattedString(sapProperties.getFormatDate()));
        itemOdM.setTipoOrdine(x4OdM.getTipoordine());
        itemOdM.setStatoSistema(x4OdM.getStatosistema());
        itemOdM.setTipoAttivaPm(x4OdM.getTipoattivita());
        inputOrdDiManList.add(itemOdM);
        inputOrdDiMan.getItem().addAll(inputOrdDiManList);
        finalRequest.setIInputOrdDiMan(inputOrdDiMan);
        return finalRequest;
    }

    private void elaborationX4OdM(Zfmpms4EamOrdDiManResponse response)
            throws RestException {
        if (Assorted.notNull(response, response.getOOutputOrdDiMan(), response.getOOutputOrdDiMan().getItem()) && Assorted.isNotEmpty(response.getOOutputOrdDiMan().getItem())) {
            for (Zstpms4OutputOrdDiMan item : response.getOOutputOrdDiMan().getItem()) {
                MaximoResponse maximoResponse;
                X4OdMResponse x4OdMResponse = x4OdMApiService.getById(item.getId());
                if (Objects.nonNull(x4OdMResponse) && x4OdMResponse.getHttpCode() == 200) {
                    X4OdMRequest request = populateX4OdM(item);
                    if (Assorted.notNull(x4OdMResponse, x4OdMResponse.getMember()) && Assorted.isNotEmpty(x4OdMResponse.getMember())) {
                        X4OdMResponse.X4OdMResponseItem member = x4OdMResponse.getMember().get(Values.FIRST_ELEMENT);
                        maximoResponse = x4OdMApiService.update(member.getHref(), request);
                        if (Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                            log.info("X4ODM {} update eseguita: [{}]", request, maximoResponse.getHttpCode());
                        } else {
                            log.error("Error for x4OdMApiService update: [{}]", Objects.nonNull(maximoResponse) ? maximoResponse.getHttpCode() : null);
                        }
                        if (Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                            X4OdMOpResponse x4OdMOpResponse = x4OdMOpApiService.getByOpAndIdOdm(item.getOperazione(), member.getIdodm());
                            X4OdMOpRequest requestOp = populateX4OdMOp(item);
                            if (Objects.nonNull(x4OdMOpResponse) && x4OdMOpResponse.getHttpCode() == 200) {
                                if (Assorted.notNull(x4OdMOpResponse, x4OdMOpResponse.getMember()) && Assorted.isNotEmpty(x4OdMOpResponse.getMember())) {
                                    X4OdMOpResponse.X4OdMOpResponseItem memberOp = x4OdMOpResponse.getMember().get(Values.FIRST_ELEMENT);
                                    maximoResponse = x4OdMOpApiService.update(memberOp.getHref(), requestOp);
                                    if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                                        log.info("X4ODMOP update eseguita: [{}]", maximoResponse.getHttpCode());
                                    } else {
                                        log.error("Error for x4OdMOpApiService update: [{}]", Objects.nonNull(maximoResponse) ? maximoResponse.getHttpCode() : null);
                                    }
                                } else {
                                    maximoResponse = x4OdMOpApiService.insert(requestOp);
                                    if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                                        log.info("X4ODMOP insert eseguita: [{}]", maximoResponse.getHttpCode());
                                    } else {
                                        log.error("Error for x4OdMOpApiService insert: [{}]", Objects.nonNull(maximoResponse) ? maximoResponse.getHttpCode() : null);
                                    }
                                }
                            } else {
                                log.error("Error for x4OdMOpResponse: [{}]", Objects.nonNull(x4OdMOpResponse) ? x4OdMOpResponse.getHttpCode() : null);
                            }
                        }
                    } else {
                        maximoResponse = x4OdMApiService.insert(request);
                        if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                            log.info("X4ODM insert eseguita: [{}]", maximoResponse.getHttpCode());
                        } else {
                            log.error("Error for x4OdMApiService insert: [{}]", Objects.nonNull(maximoResponse) ? maximoResponse.getHttpCode() : null);
                        }
                        if (Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                            X4OdMOpRequest requestOp = populateX4OdMOp(item);
                            maximoResponse = x4OdMOpApiService.insert(requestOp);
                            if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                                log.info("X4ODMOP insert eseguita: [{}]", maximoResponse.getHttpCode());
                            } else {
                                log.error("Error for x4OdMOpApiService insert: [{}]", Objects.nonNull(maximoResponse) ? maximoResponse.getHttpCode() : null);
                            }
                        } else {
                            log.error("Error for x4OdMApiService insert: [{}]", Objects.nonNull(maximoResponse) ? maximoResponse.getHttpCode() : null);
                        }
                    }
                } else {
                    log.error("Error for x4OdMResponse: [{}]", Objects.nonNull(x4OdMResponse) ? x4OdMResponse.getHttpCode() : null);
                }
            }
        } else {
            if (Objects.nonNull(response) && Objects.nonNull(response.getOBapiret()) && Assorted.isNotEmpty(response.getOBapiret().getItem())) {
                log.error("Error Zfmpms4EamOrdDiManResponse: {}", response.getOBapiret().getItem().get(Values.FIRST_ELEMENT).getMessage());
            }
        }
    }

    private void elaborationX4ODMFirst(Zfmpms4EamOrdDiManResponse response)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.info("starting elaborationX4ODMFirst() |{}|", startTime);
        if (Assorted.notNull(response, response.getOOutputOrdDiMan(), response.getOOutputOrdDiMan().getItem()) && Assorted.isNotEmpty(response.getOOutputOrdDiMan().getItem())) {
            for (Zstpms4OutputOrdDiMan item : response.getOOutputOrdDiMan().getItem()) {
                X4ODMjdbc x4ODM = x4OdMQueryService.getById(item.getId());
                x4ODM = Objects.nonNull(x4ODM) ? x4ODM : new X4ODMjdbc();
                populateX4OdMjdbc(item, x4ODM);
                x4OdMQueryService.saveOrUpdate(x4ODM);
                log.info("ODM salvataggio eseguito: [{}]", x4ODM.getIdodm());
                X4ODMOP x4ODMOP = x4OdMQueryService.getByIdOp(item.getId(), item.getOperazione());
                x4ODMOP = Objects.nonNull(x4ODMOP) ? x4ODMOP : new X4ODMOP();
                populateX4OdMOpjdbc(item, x4ODMOP);
                x4OdMQueryService.saveOrUpdateOp(x4ODMOP);
                log.info("ODMOP salvataggio eseguito: [{}]", x4ODM.getIdodm());
            }
        } else {
            if (Objects.nonNull(response) && Objects.nonNull(response.getOBapiret()) && Assorted.isNotEmpty(response.getOBapiret().getItem())) {
                log.error("Error Zfmpms4EamOrdDiManResponse: {}", response.getOBapiret().getItem().get(Values.FIRST_ELEMENT).getMessage());
            }
        }
        log.info("ending elaborationX4ODMFirst() |{}|", System.currentTimeMillis() - startTime);
    }

    private void populateX4OdMjdbc(Zstpms4OutputOrdDiMan item, X4ODMjdbc request) {
        request.setIdodm(item.getId());
        request.setTipoordine(item.getTipoOrdine());
        request.setTipoattivita(item.getTipoAttivaPm());
        request.setDescription(item.getTestoBreve());
        request.setStatosistema(item.getStatoSistema());
        request.setStatoutente(item.getStatoUtente());
        request.setCdlresp(item.getClavresp());
        request.setSedetecnica(item.getSedeTecnica());
        request.setCdcresp(item.getCentroDiCostoResponsabile());
        request.setCdccosti(item.getCentroDiCosto());
        request.setProfitcenter(item.getProfitCenter());
        request.setHasld(zero);
        request.setCreatedby(item.getAutore());
        request.setCreateddate(LocalDate.parse(item.getDataCreazione()));
        request.setChangeddate(Objects.equals(item.getDataModifica(), sapProperties.getNoDate())
                ? LocalDate.parse(item.getDataCreazione())
                : LocalDate.parse(item.getDataModifica()));
        request.setKonty(item.getKonty());
        request.setAufnr(item.getAufnr());
        request.setWbe(item.getWbs());

    }

    private X4OdMRequest populateX4OdM(Zstpms4OutputOrdDiMan item) {
        X4OdMRequest request = new X4OdMRequest();
        request.setIdodm(item.getId());
        request.setTipoordine(item.getTipoOrdine());
        request.setTipoattivita(item.getTipoAttivaPm());
        request.setDescription(item.getTestoBreve());
        request.setStatosistema(item.getStatoSistema());
        request.setStatoutente(item.getStatoUtente());
        request.setCdlresp(item.getClavresp());
        request.setSedetecnica(item.getSedeTecnica());
        request.setCdcresp(item.getCentroDiCostoResponsabile());
        request.setCdccosti(item.getCentroDiCosto());
        request.setProfitcenter(item.getProfitCenter());
        request.setCreatedby(item.getAutore());
        request.setCreateddate(item.getDataCreazione());
        request.setChangeddate(Objects.equals(item.getDataModifica(), sapProperties.getNoDate()) ? item.getDataCreazione() : item.getDataModifica());
        request.setKonty(item.getKonty());
        request.setAufnr(item.getAufnr());
        request.setWbe(item.getWbs());

        return request;
    }

    private X4OdMOpRequest populateX4OdMOp(Zstpms4OutputOrdDiMan item) {
        X4OdMOpRequest request = new X4OdMOpRequest();
        request.setIdodm(item.getId());
        request.setNoperazione(item.getOperazione());
        request.setDescription(item.getDescOperazione());
        return request;
    }

    private void populateX4OdMOpjdbc(Zstpms4OutputOrdDiMan item, X4ODMOP request) {
        request.setIdodm(item.getId());
        request.setNoperazione(item.getOperazione());
        request.setHasld(zero);
        request.setDescription(item.getDescOperazione());
    }

    /**
     * Recupera un token JWT per accedere a un'entità di integrazione dati specificata.
     *
     * @param uri l'entità di integrazione dati {@link EDataIntegrationEntity} per cui recuperare il token.
     * @return un'istanza di {@link JwtTokenResponse} contenente il token JWT, oppure {@code null}
     * se l'entità non richiede autenticazione (indicata da {@code Constants.NOT_SUPPORTED_URI}).
     * Il recupero del token utilizza le configurazioni OAuth2, come URI di Azure, endpoint del token,
     * client ID, client secret e scope.
     */
    private JwtTokenResponse getToken(EDataIntegrationEntity uri) {
        RestEntityInfo entity = restHandler.getRestEntity(uri.getEntityKey());
        if (Objects.nonNull(entity.getUriStub()) && (!Constants.NOT_SUPPORTED_URI.equals(entity.getUriStub()))) {
            return null;
        } else {
            return jwtAwareRestClient.retrieveJwtToken(oAuth2Sap.getAzureUri(),
                    oAuth2Sap.getTokenEndpoint(),
                    oAuth2Sap.getClientId(),
                    oAuth2Sap.getClientSecret(),
                    oAuth2Sap.getClientCredential(),
                    oAuth2Sap.getScope());
        }
    }

    private JwtTokenResponse getTokenOSB(EDataIntegrationEntity uri) {
        RestEntityInfo entity = restHandler.getRestEntity(uri.getEntityKey());
        if (Objects.nonNull(entity.getUriStub()) && (!Constants.NOT_SUPPORTED_URI.equals(entity.getUriStub()))) {
            return null;
        } else {
            return jwtAwareRestClient.retrieveJwtToken(oAuth2Sap.getAzureUri(),
                    oAuth2Sap.getTokenEndpoint(),
                    oAuth2Sap.getClientIdOSB(),
                    oAuth2Sap.getClientSecretOSB(),
                    oAuth2Sap.getClientCredential(),
                    oAuth2Sap.getScopeOSB());
        }
    }

    public void storno(String topic, String uuidResend, String payload, String type) throws RestException {
        long startTime = System.currentTimeMillis();
        String uuid = Uuid.randomUuid().toString();
        SapInterface sapInterface = Objects.equals(type, TOTALE) ? SapInterface.STORNO_TOTALE : SapInterface.STORNO_PARZIALE;
        log.info("Start {} SAP - UUID: {}, Topic: {} - Payload: {}", sapInterface.getName(), uuid, topic, payload);
        Instant startDate = Instant.now();
        X4EsitoIntRequest x4EsitoIntRequest = buildEsitoRequest(topic, payload, uuid, startDate, sapInterface, EAM_SAP, Constants.SAP, WorkflowStep.STEP_0.getMask());
        X4LabtransRequest x4LabtransRequest = new X4LabtransRequest();
        X4WorkOrderRequest x4WorkOrderRequest = new X4WorkOrderRequest();
        KafkaX4Storno request = null;
        try {
            if (Objects.isNull(payload)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), topic);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + " [" + topic + "]");
            }
            if (Objects.nonNull(uuidResend)) {
                x4EsitoIntRequest.setUniqueid(uuidResend);
                X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
                x4EsitoIntRequest.setParent(Objects.nonNull(parent) ? parent.getX4EsitoIntId() : null);
            }
            request = new Gson().fromJson(payload, KafkaX4Storno.class);
            if (Objects.isNull(request)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), request);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + "[" + request + "]");
            }
            x4EsitoIntRequest.setDescription(WorkflowStep.STEP_1.getMask());
            request.setHref(request.getHref().replaceAll(OSLC, API));
            JwtTokenResponse jwtTokenResponse = getTokenOSB(EDataIntegrationEntity.SAP_STORNO);
            String jwtToken = Objects.nonNull(jwtTokenResponse) && jwtTokenResponse.getHttpCode() == 200 ? jwtTokenResponse.getAccessToken() : null;
            if (Objects.isNull(jwtToken)) {
                log.error("{} - HttpCode: [{}]", EAM_0024.getErrorDescription(), Objects.nonNull(jwtTokenResponse) ? jwtTokenResponse.getHttpCode() : null);
                throw new EAMException(EAM_0024.getEamCode(), EAM_0024.getErrorDescription() + " - HttpCode: [" + (Objects.nonNull(jwtTokenResponse) ? jwtTokenResponse.getHttpCode() : null) + "]");
            }
            processStorno(request, type, jwtToken, x4EsitoIntRequest, x4WorkOrderRequest, x4LabtransRequest);
            x4EsitoIntRequest.setEsito(ESITO_OK);
        } catch (EAMException e) {
            if (Objects.equals(type, PARZIALE)) {
                x4LabtransRequest.setX4storno(SAP_ERROR);
                x4LabtransRequest.setX4errsap(EamEtlUtils.truncate(e.getMessage(), 150));
            } else {
                x4WorkOrderRequest.setX4stornosap(SAP_ERROR);
                x4WorkOrderRequest.setX4errsap(EamEtlUtils.truncate(e.getMessage(), 150));
            }
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
        } finally {
            assert request != null;
            try {
                MaximoResponse maximoResponse;
                if (Objects.equals(type, PARZIALE)) {
                    maximoResponse = x4LabtransApiService.updateLabtrans(request.getHref(), x4LabtransRequest);
                } else {
                    maximoResponse = x4WorkOrderApiService.updateWorkorder(request.getHref(), x4WorkOrderRequest);
                }
                if(Objects.isNull(maximoResponse) || maximoResponse.getHttpCode() >= 300) {
                    x4EsitoIntRequest.setDescription(x4EsitoIntRequest.getDescription() + WorkflowStep.LAST_STEP_KO.getMask());
                    log.error("{} - Failed update Workorder or Labtrans - Request: [href: {} - {}] - Response: [{}]", EAM_0023.getErrorDescription(), request.getHref(), writeObjectAsString(x4LabtransRequest), maximoResponse);
                    throw new EAMException(EAM_0023.getEamCode(), EAM_0023.getErrorDescription() + " - [href: " + request.getHref() + " - request: " + x4LabtransRequest + "] - Response: [" + maximoResponse + "]");
                }
                x4EsitoIntRequest.setDescription(x4EsitoIntRequest.getDescription() + WorkflowStep.LAST_STEP_OK.getMask());
                if(Objects.isNull(x4EsitoIntRequest.getDescription())
                        || x4EsitoIntRequest.getDescription().contains("0")
                        || !Objects.equals(x4EsitoIntRequest.getDescription(), WorkflowStep.STEP_CHECK.getMask())) {
                    x4EsitoIntRequest.setEsito(ESITO_KO);
                } else {
                    x4EsitoIntRequest.setEsito(ESITO_OK);
                }
                log.info("Storno terminato - for Request [{}] - Response [{}]" , request.getHref() + " - " + writeObjectAsString(x4LabtransRequest), maximoResponse);

            } catch (EAMException e) {
                logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
            } catch (Exception e) {
                logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
            }

            x4EsitoIntRequest.setEnd_elab(esitoDate(Instant.now()));
        }
        sendEsitoIntRequest(x4EsitoIntRequest, x4EsitoIntApiService);
        log.info("End Storno {} SAP - UUID: {}, Topic: {}, Duration: {} seconds", type, uuid, topic, (System.currentTimeMillis() - startTime) / 1000);
    }

    private boolean sendStornoToGeocall(String tipo, KafkaX4Storno request) throws EAMException {
        log.info("Start sendStornoKafkaToGeocall [{}] [{}]", tipo, request);
        boolean esito;
        try {
            StornoDTO stornoDTO = new StornoDTO();
            stornoDTO.setTipoStorno(tipo);
            stornoDTO.setMatricola(request.getLaborcode());
            stornoDTO.setWonum(Objects.nonNull(request.getRefwo()) ? request.getRefwo() : request.getWonum());
            stornoDTO.setIdConsuntivo(request.getExternalrefid());
            esito =  geoCallHandler.sendSapStornoFromSAP(stornoDTO);
        } catch(EAMException ex) {
            log.error("Error: {}, {}", ex.getErrorCode(), ex.getMessage());
            throw new EAMException(ex.getErrorCode(), ex.getMessage(), ex);
        } catch(Exception e) {
            log.error("Error: {}, {}", EAM_0000.name(), e.getMessage());
            throw new EAMException(EAM_0000.name(), e.getMessage(), e);
        }
        return esito;
    }

    private void processStorno(KafkaX4Storno request, String tipo, String jwtToken, X4EsitoIntRequest x4EsitoIntRequest, X4WorkOrderRequest x4WorkOrderRequest, X4LabtransRequest x4LabtransRequest)
            throws EAMException {
        boolean esito;
        ZmosWsReversalHoursOdm requestSapStorno;
        String simulazione = sapProperties.getModEsecuzione().split(",")[0];
        String effettivo = sapProperties.getModEsecuzione().split(",")[1];
        if(Objects.equals(tipo, PARZIALE)) {
            x4EsitoIntRequest.setWonum(request.getRefwo());
            List<X4WORKORDER> x4WORKORDERS = x4WorkorderQueryService.getWorkorder(request.getRefwo(), request.getSiteid());
            if (Assorted.isEmpty(x4WORKORDERS)) {
                log.error("{} - Wonum: [{}] - SiteId: [{}]", EAM_0004.getErrorDescription(), request.getRefwo(), request.getSiteid());
                throw new EAMException(EAM_0004.getEamCode(), EAM_0004.getErrorDescription() + " [" + request.getRefwo() + "]");
            }
            ZmosWsResearchCounterOdm requestSapCounterOdM = createRequestCounterOdM(request, x4WORKORDERS.get(Values.FIRST_ELEMENT).getX4odm());
            ZmosWsResearchCounterOdmResponse responseSapCounterOdM = sapClient.requestToSAPCounterOdM(requestSapCounterOdM, jwtToken);
            if (Objects.isNull(responseSapCounterOdM)
                    || Objects.isNull(responseSapCounterOdM.getOData())
                    || Assorted.isEmpty(responseSapCounterOdM.getOData().getItem())
                    || Assorted.isNull(responseSapCounterOdM.getOBapiret2())
                    || Assorted.isEmpty(responseSapCounterOdM.getOBapiret2().getItem())
                    || Objects.isNull(responseSapCounterOdM.getOBapiret2().getItem().get(Values.FIRST_ELEMENT))
                    || !Objects.equals(responseSapCounterOdM.getOBapiret2().getItem().get(Values.FIRST_ELEMENT).getType(), SUCCESS)) {
                log.error("{} - [{}]", EAM_0033.getErrorDescription(), writeObjectAsString(responseSapCounterOdM));
                if (Objects.nonNull(responseSapCounterOdM)
                        && Objects.nonNull(responseSapCounterOdM.getOBapiret2())
                        && Objects.nonNull(responseSapCounterOdM.getOBapiret2().getItem().get(Values.FIRST_ELEMENT))
                        && !Objects.equals(responseSapCounterOdM.getOBapiret2().getItem().get(Values.FIRST_ELEMENT).getType(), SUCCESS)) {
                    throw new EAMException(EAM_0033.getEamCode(), EAM_0033.getErrorDescription() + " [" + responseSapCounterOdM.getOBapiret2().getItem().get(Values.FIRST_ELEMENT).getMessage() + "]");
                }
                throw new EAMException(EAM_0033.getEamCode(), EAM_0033.getErrorDescription());
            }
            requestSapStorno = createStornoRequest(requestSapCounterOdM.getIZzinstance(), requestSapCounterOdM.getIAufnr(), tipo, responseSapCounterOdM, effettivo);
        } else if (Objects.equals(tipo, TOTALE)) {
            x4EsitoIntRequest.setWonum(request.getWonum());
            requestSapStorno = createStornoRequest(request.getWonum(), request.getX4odm(), tipo, null, simulazione);
            ZmosWsReversalHoursOdmResponse responseStorno = sapClient.requestToSAPStorno(requestSapStorno, jwtToken);
            if (Objects.isNull(responseStorno)
                    || Objects.isNull(responseStorno.getOData())
                    || Assorted.isEmpty(responseStorno.getOData().getItem())
                    || Objects.isNull(responseStorno.getOBapiret2())
                    || Assorted.isEmpty(responseStorno.getOBapiret2().getItem())
                    || Objects.isNull(responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT))
                    || !Objects.equals(responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT).getType(), SUCCESS)) {
                log.error("{} - [{}]", EAM_0034.getErrorDescription(), writeObjectAsString(responseStorno));
                if (Objects.nonNull(responseStorno)
                        && Objects.nonNull(responseStorno.getOBapiret2())
                        && Assorted.isNotEmpty(responseStorno.getOBapiret2().getItem())
                        && Objects.nonNull(responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT))
                        && !Objects.equals(responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT).getType(), SUCCESS)) {
                    throw new EAMException(EAM_0034.getEamCode(), EAM_0034.getErrorDescription() + " [" + responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT).getMessage() + "]");
                }
                throw new EAMException(EAM_0034.getEamCode(), EAM_0034.getErrorDescription());
            }
        } else {
            throw new EAMException(EAM_0000.getEamCode(), EAM_0000.getErrorDescription());
        }
        x4EsitoIntRequest.setDescription(WorkflowStep.STEP_2.getMask());
        if(Assorted.isNotEmpty(request.getExternalrefid())) {
            esito = sendStornoToGeocall(tipo, request);
            if (esito) {
                x4WorkOrderRequest.setX4stornogeocall(ESITO_OK);
                x4LabtransRequest.setX4stornogeocall(ESITO_OK);
            } else {
                x4WorkOrderRequest.setX4errsap(ESITO_KO);
                x4WorkOrderRequest.setX4stornogeocall(ESITO_KO);
                x4LabtransRequest.setX4errsap(ESITO_KO);
                x4LabtransRequest.setX4stornogeocall(ESITO_KO);
                throw new EAMException(EAM_0020.getEamCode(), EAM_0020.getErrorDescription());
            }
        }
        requestSapStorno.setIModEsecuzione(effettivo);
        x4EsitoIntRequest.setDescription(WorkflowStep.STEP_3.getMask());
        ZmosWsReversalHoursOdmResponse responseStorno = sapClient.requestToSAPStorno(requestSapStorno, jwtToken);

        if (Objects.isNull(responseStorno)
                || Objects.isNull(responseStorno.getOData())
                || Assorted.isEmpty(responseStorno.getOData().getItem())
                || Objects.isNull(responseStorno.getOBapiret2())
                || Assorted.isEmpty(responseStorno.getOBapiret2().getItem())
                || Objects.isNull(responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT))
                || !Objects.equals(responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT).getType(), SUCCESS)) {
            log.error("{} - [{}]", EAM_0035.getErrorDescription(), writeObjectAsString(responseStorno));
            if (Objects.nonNull(responseStorno)
                    && Objects.nonNull(responseStorno.getOBapiret2())
                    && Assorted.isNotEmpty(responseStorno.getOBapiret2().getItem())
                    && Objects.nonNull(responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT))
                    && !Objects.equals(responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT).getType(), SUCCESS)) {
                throw new EAMException(EAM_0035.getEamCode(), EAM_0035.getErrorDescription() + " - [" + responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT).getMessage() + "]");
            }
            throw new EAMException(EAM_0035.getEamCode(), EAM_0035.getErrorDescription());
        }
        x4EsitoIntRequest.setDescription(WorkflowStep.STEP_4.getMask());
        for (ZstOreStornate itemStorno : responseStorno.getOData().getItem()) {
            if (Objects.equals(tipo, TOTALE)) {
                x4WorkOrderRequest.setX4stornosap(itemStorno.getFlgStornato());
            } else {
                x4LabtransRequest.setX4storno(itemStorno.getFlgStornato());
            }
        }
    }


    private ZmosWsReversalHoursOdm createStornoRequest(String iZzistance, String aufnr, String tipo, ZmosWsResearchCounterOdmResponse responseSapCounterOdM, String modEsecuzione) {
        ZmosWsReversalHoursOdm requestSapStorno = new ZmosWsReversalHoursOdm();
        requestSapStorno.setIZzinstance(iZzistance);
        requestSapStorno.setIAufnr(aufnr);
        requestSapStorno.setITpStorno(tipo);
        if(Objects.nonNull(responseSapCounterOdM)) {
            ZttStornoOre iDatiConferme = new ZttStornoOre();
            List<ZstStornoOre> itemDatiConferme = new ArrayList<>();
            for (ZstCounterOdm itemCount : responseSapCounterOdM.getOData().getItem()) {
                ZstStornoOre zstStornoOre = new ZstStornoOre();
                zstStornoOre.setRueck(itemCount.getRueck());
                zstStornoOre.setRmzhl(itemCount.getRmzhl());
                itemDatiConferme.add(zstStornoOre);
            }
            iDatiConferme.getItem().addAll(itemDatiConferme);
            requestSapStorno.setIDatiConferme(iDatiConferme);
        }
        requestSapStorno.setIModEsecuzione(modEsecuzione);
        return requestSapStorno;
    }

    private ZmosWsResearchCounterOdm createRequestCounterOdM(KafkaX4Storno request, String x4odm)
    {
        ZmosWsResearchCounterOdm requestSapCounterOdM;
        requestSapCounterOdM = new ZmosWsResearchCounterOdm();
        requestSapCounterOdM.setIZzinstance(request.getRefwo());
        requestSapCounterOdM.setIAufnr(x4odm);
        requestSapCounterOdM.setIIsdd(EamEtlUtils.convertToDateyyyyMMdd(request.getStartdatetime()));
        requestSapCounterOdM.setIIedd(EamEtlUtils.convertToDateyyyyMMdd(request.getFinishdatetime()));
        requestSapCounterOdM.setIMatricola(request.getLaborcode());
        return requestSapCounterOdM;
    }

    public void acquisitionCountableData(String topic, String uuidResend, String payload) {
        Instant startDate = Instant.now();
        String uuid = Uuid.randomUuid().toString();
        X4EsitoIntRequest x4EsitoIntRequest = buildEsitoRequest(topic, payload, uuid, startDate, Catalog.SapInterface.ACQUISITION_DC, Constants.EAM_SAP, Constants.SAP);
        String result = null;
        long startTime = System.currentTimeMillis();
        log.info("Starting acquisition of Wbe from SAP - UUID: {}, Topic: {}", uuid, topic);
        try {
            if (Objects.isNull(payload)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), topic);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + " [" + topic + "]");
            }
            if (Assorted.notNull(uuidResend)) {
                x4EsitoIntRequest.setUniqueid(uuidResend);
                X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
                x4EsitoIntRequest.setParent(Assorted.notNull(parent) ? parent.getX4EsitoIntId() : null);
            }
            KafkaX4AcquisitionData request = new Gson().fromJson(payload, KafkaX4AcquisitionData.class);
            if (Objects.isNull(request)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), request);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + "[" + request + "]");
            }
            sapProperties.setFilters(
                    x4SapFilterQueryService.getFilter().stream()
                            .map(f -> new SapProperties.FilterSap(f.getFiltro(), f.getTipoSpesa(), f.getTipoAttivita(), f.getTipoOrdine(), f.getLivello(), f.getStato()))
                            .toList()
            );
            List<X4WBERestricted> x4WBEs = x4WBEQueryService.getRequests();
            boolean isFirstTime = Objects.isNull(x4WBEs) || Assorted.isEmpty(x4WBEs);
            List<QueryWBS> finalRequest = buildRequestDC(sapProperties.getFilters(), x4WBEs);
            List<X4StatiSap> statiSapList = x4StatiSapQueryService.getAll();
            if (Assorted.isEmpty(finalRequest)) {
                log.error("{}", EAM_0028.getErrorDescription());
                throw new EAMException(EAM_0028.getEamCode(), EAM_0028.getErrorDescription());
            }
            result = processDatiContabiliSequenziali(finalRequest, isFirstTime, statiSapList);
        } catch (EAMException e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
        } finally {
            if(Objects.nonNull(result)) {
                x4EsitoIntRequest.setDesc_esito(result);
            }
            x4EsitoIntRequest.setEnd_elab(EamEtlUtils.esitoDate(Instant.now()));
        }
        sendEsitoIntRequest(x4EsitoIntRequest, x4EsitoIntApiService);
        log.info("Completed acquisition of Wbe from SAP - UUID: {}, Topic: {}, Duration: {} seconds", uuid, topic, (System.currentTimeMillis() - startTime) / 1000);
    }

    private List<QueryWBS> buildRequestDC(List<SapProperties.FilterSap> filterSap, List<X4WBERestricted> x4WBEs) throws EAMException {
        Date changeDate = Assorted.isNotEmpty(x4WBEs) && Assorted.notNull(x4WBEs.get(Values.FIRST_ELEMENT).getChangeDate()) ? x4WBEs.get(Values.FIRST_ELEMENT).getChangeDate() : null;
        List<Integer> livelliWbe = filterSap.stream().filter(f -> WBE.equalsIgnoreCase(f.getFiltro())).map(SapProperties.FilterSap::getLivello).distinct().toList();
        List<String> profitCenterList = filterSap.stream().filter(f -> WBE.equalsIgnoreCase(f.getFiltro())).map(SapProperties.FilterSap::getStato).distinct().toList();
        List<QueryWBS> finalRequest = new ArrayList<>();
        try {
            if (Objects.isNull(changeDate)) {
                livelliWbe.forEach(lvl -> profitCenterList.forEach(pc -> {
                    QueryWBS queryWBS = new QueryWBS();
                    ZqwbsInputMessageFilteI dc = new ZqwbsInputMessageFilteI();
                    dc.setDataUltimaModifica(sapProperties.getStartDate());
                    ZfiltroArrayGenericoI profitCenterF = new ZfiltroArrayGenericoI();
                    Zstring30TabI profitCenterS = new Zstring30TabI();
                    profitCenterS.getItem().add(pc);
                    profitCenterF.setItem(profitCenterS);
                    dc.setProfitCenter(profitCenterF);
                    ZfiltroArrayLivelliI livelliI = new ZfiltroArrayLivelliI();
                    ZlivelloWbxTabI zlivelloWbxTabI = new ZlivelloWbxTabI();
                    zlivelloWbxTabI.getItem().add(lvl);
                    livelliI.setItem(zlivelloWbxTabI);
                    dc.setLivello(livelliI);
                    queryWBS.setFilter(dc);
                    finalRequest.add(queryWBS);
                }));
            } else {
                QueryWBS queryWBS = new QueryWBS();
                ZqwbsInputMessageFilteI dc = new ZqwbsInputMessageFilteI();
                dc.setDataUltimaModifica(EamEtlUtils.dateAsSqlString(changeDate));
                ZfiltroArrayGenericoI profitCenterF = new ZfiltroArrayGenericoI();
                Zstring30TabI profitCenterS = new Zstring30TabI();
                profitCenterS.getItem().addAll(profitCenterList);
                profitCenterF.setItem(profitCenterS);
                dc.setProfitCenter(profitCenterF);
                ZfiltroArrayLivelliI livelliI = new ZfiltroArrayLivelliI();
                ZlivelloWbxTabI zlivelloWbxTabI = new ZlivelloWbxTabI();
                zlivelloWbxTabI.getItem().addAll(livelliWbe);
                livelliI.setItem(zlivelloWbxTabI);
                dc.setLivello(livelliI);
                queryWBS.setFilter(dc);
                finalRequest.add(queryWBS);
            }
        } catch (Exception e) {
            throw new EAMException("Errore durante creazione Request per CBS/CBE: ", e.getMessage());
        }
        return finalRequest;
    }

    public void invioConsuntivi(String topic, String uuidResend, String payload) {
        Instant startDate = Instant.now();
        String uuid = Uuid.randomUuid().toString();
        long startTime = System.currentTimeMillis();
        log.info("Start Invio Consuntivi SAP - UUID: {}, Topic: {} - Payload: {}", uuid, topic, payload);
        X4EsitoIntRequest x4EsitoIntRequest = buildEsitoRequest(topic, payload, uuid, startDate, Catalog.SapInterface.CONSUNTIVI, EAM_SAP, Constants.SAP, WorkflowStep.STEP_0.getMask());
        X4LabtransRequest x4LabtransRequest = new X4LabtransRequest();
        KafkaX4Labtrans request = new KafkaX4Labtrans();
        X4LabtransResponse aggregati = null;

        try {
            if (Objects.isNull(payload)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), topic);
                x4LabtransRequest.setX4esitosapdesc(CONSUNTIVAZIONE_KO);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + " [" + topic + "]");
            }
            if (Objects.nonNull(uuidResend)) {
                x4EsitoIntRequest.setUniqueid(uuidResend);
                X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
                x4EsitoIntRequest.setParent(Objects.nonNull(parent) ? parent.getX4EsitoIntId() : null);
            }
            request = new Gson().fromJson(payload, KafkaX4Labtrans.class);
            if (Objects.isNull(request) || Assorted.isNull(request.getRefwo(), request.getSiteid(), request.getStartdatetime(), request.getApprover(), request.getHref())) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), request);
                x4LabtransRequest.setX4esitosapdesc(CONSUNTIVAZIONE_KO);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + "[" + request + "]");
            }
            x4EsitoIntRequest.setDescription(WorkflowStep.STEP_1.getMask());
            x4EsitoIntRequest.setWonum(request.getRefwo());
            request.setHref(request.getHref().replace(OSLC, API));

            List<X4WORKORDER> x4WORKORDER = x4WorkorderQueryService.getWorkorder(request.getRefwo(), request.getSiteid());
            if (Assorted.isEmpty(x4WORKORDER)) {
                x4LabtransRequest.setGenapprservreceipt(0);
                log.error("{} Ordine di lavoro: [{}]", EAM_0004.getErrorDescription(), request.getRefwo());
                throw new EAMException(EAM_0004.getEamCode(), EAM_0004.getErrorDescription() + " Ordine di lavoro: [" + request.getRefwo() + "]");
            }

            x4EsitoIntRequest.setDescription(WorkflowStep.STEP_2.getMask());

            JwtTokenResponse jwtTokenResponse = getTokenOSB(EDataIntegrationEntity.SAP_CONSUNTIVAZIONE);
            String jwtToken = Objects.nonNull(jwtTokenResponse) && jwtTokenResponse.getHttpCode() == 200 ? jwtTokenResponse.getAccessToken() : null;
            if (Objects.isNull(jwtToken)) {
                x4LabtransRequest.setGenapprservreceipt(0);
                log.error("{} - HttpCode: [{}]", EAM_0024.getErrorDescription(), Objects.nonNull(jwtTokenResponse) ? jwtTokenResponse.getHttpCode() : null);
                throw new EAMException(EAM_0024.getEamCode(), EAM_0024.getErrorDescription() + " - HttpCode: [" + (Objects.nonNull(jwtTokenResponse) ? jwtTokenResponse.getHttpCode() : null) + "]");
            }

            if (!controlloPeriodoContabile(request.getStartdatetime(), x4WORKORDER.get(Values.FIRST_ELEMENT), jwtToken)) {
                x4LabtransRequest.setGenapprservreceipt(0);
                log.error("{}", EAM_0021.getErrorDescription());
                throw new EAMException(EAM_0021.getEamCode(), EAM_0021.getErrorDescription());
            }

            x4EsitoIntRequest.setDescription(WorkflowStep.STEP_3.getMask());

            if (Objects.nonNull(request.getX4correlationid())) {
                aggregati = x4LabtransApiService.retrieveLabtransByCorrelationID(request.getX4correlationid());
            }
            Zfmpms4EamConfirm requestSAP = buildRichiestaConsuntivazione(request, x4WORKORDER.get(Values.FIRST_ELEMENT), aggregati);
            Zfmpms4EamConfirmResponse responseSAP = sapClient.requestToSAPConsuntivazione(requestSAP, jwtToken);

            String sapMessage = (Objects.nonNull(responseSAP) && Objects.nonNull(responseSAP.getReturn()))
                    ? responseSAP.getReturn().getMessage()
                    : null;

            if (Objects.isNull(responseSAP) || Objects.isNull(responseSAP.getReturn()) || Objects.isNull(responseSAP.getReturn().getType()) || Objects.equals(responseSAP.getReturn().getType(), SAP_ERROR)) {
                x4LabtransRequest.setGenapprservreceipt(0);
                log.error("{} - Response: [{}]", EAM_0022.getErrorDescription(), sapMessage);
                throw new EAMException(EAM_0022.getEamCode(), EAM_0022.getErrorDescription() + " [" + sapMessage + "]");
            }
            x4EsitoIntRequest.setDescription(WorkflowStep.STEP_4.getMask());
            x4EsitoIntRequest.setDesc_esito(sapMessage);
            x4EsitoIntRequest.setEsito(ESITO_OK);
            x4LabtransRequest.setX4esitosapdesc(sapMessage);
            if(!Objects.equals(requestSAP.getActvt(), OPEX) && sapMessage.contains("Consuntivi caricati correttamente in SAP.")) {
                x4LabtransRequest.setX4checksap(ESITO_OK);
            } else if(Objects.equals(requestSAP.getActvt(), OPEX)) {
                x4LabtransRequest.setX4checksap(ESITO_OK);
            }
        } catch (EAMException e) {
            if(isTimeout(e)) {
                x4LabtransRequest.setGenapprservreceipt(1);
            } else {
                x4LabtransRequest.setGenapprservreceipt(0);
            }
            x4LabtransRequest.setX4esitosapdesc(e.getMessage());
            x4LabtransRequest.setX4checksap(ESITO_KO);
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            x4LabtransRequest.setX4esitosapdesc(e.getMessage());
            x4LabtransRequest.setX4checksap(ESITO_KO);
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
        } finally {
            try {
                List<MaximoResponse> maximoResponses = new ArrayList<>();
                if (Objects.isNull(aggregati) || Assorted.isEmpty(aggregati.getMember())) {
                    MaximoResponse maximoResponse = x4LabtransApiService.updateLabtrans(request.getHref(), x4LabtransRequest);
                    maximoResponses.add(maximoResponse);
                } else {
                    for (X4LabtransResponse.X4LabtransResponseItem item : aggregati.getMember()) {
                        MaximoResponse maximoResponse = x4LabtransApiService.updateLabtrans(item.getHref(), x4LabtransRequest);
                        maximoResponses.add(maximoResponse);
                    }
                }

                List<MaximoResponse> failures = maximoResponses.stream()
                        .filter(r -> Objects.isNull(r) || r.getHttpCode() >= 300)
                        .toList();

                if (!failures.isEmpty()) {
                    x4EsitoIntRequest.setDescription(x4EsitoIntRequest.getDescription() + WorkflowStep.LAST_STEP_KO.getMask());
                    log.error("{} - Failed update Labtrans ({}/{} KO) - Request [href: {} - {}] - Failures [{}]",
                            EAM_0023.getErrorDescription(),
                            failures.size(), maximoResponses.size(),
                            request.getHref(), writeObjectAsString(x4LabtransRequest), failures);
                    throw new EAMException(EAM_0023.getEamCode(),
                            EAM_0023.getErrorDescription() + " - [href: " + request.getHref()
                                    + " - request: " + x4LabtransRequest
                                    + "] - Failures: [" + failures + "]");
                }

                x4EsitoIntRequest.setDescription(x4EsitoIntRequest.getDescription() + WorkflowStep.LAST_STEP_OK.getMask());
                if (x4EsitoIntRequest.getDescription().contains("0")
                        || !Objects.equals(x4EsitoIntRequest.getDescription(), WorkflowStep.STEP_CHECK.getMask())) {
                    x4EsitoIntRequest.setEsito(ESITO_KO);
                } else {
                    x4EsitoIntRequest.setEsito(ESITO_OK);
                }
                log.info("Consuntivazione terminata - updated {}/{} Labtrans - Request [{}] - Responses [{}]",
                        maximoResponses.size(), maximoResponses.size(),
                        request.getHref() + " - " + writeObjectAsString(x4LabtransRequest),
                        maximoResponses);

            } catch (EAMException e) {
                logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
            } catch (Exception e) {
                logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
            }
            x4EsitoIntRequest.setEnd_elab(esitoDate(Instant.now()));
        }
        sendEsitoIntRequest(x4EsitoIntRequest, x4EsitoIntApiService);
        log.info("End Invio Consuntivi SAP - UUID: {}, Topic: {}, Duration: {} seconds", uuid, topic, (System.currentTimeMillis() - startTime) / 1000);
    }

    private boolean isTimeout(Throwable e) {
        if (e == null) return false;
        Throwable cause = e;
        while (cause != null) {
            log.error("isTimeout - Exception type: [{}] - Message: [{}]", cause.getClass().getName(), cause.getMessage());
            if (cause instanceof java.net.SocketTimeoutException
                    || cause instanceof java.util.concurrent.TimeoutException
                    || cause instanceof org.springframework.ws.client.WebServiceIOException) {
                return true;
            }
            String msg = cause.getMessage();
            if (msg != null && msg.toLowerCase().contains("timeout")
                    || msg != null && msg.toLowerCase().contains("time out")
                    || msg != null && msg.toLowerCase().contains("timed out")) {
                return true;
            }
            cause = cause.getCause();
        }
        return false;
    }

    private Zfmpms4EamConfirm buildRichiestaConsuntivazione(KafkaX4Labtrans request,
                                                            X4WORKORDER x4WORKORDER,
                                                            X4LabtransResponse aggregati) {

        Zfmpms4EamConfirm zfmpms4EamConfirm = new Zfmpms4EamConfirm();
        Zstpms4EamConfirm zstpms4EamConfirm = new Zstpms4EamConfirm();

        String wonum = x4WORKORDER.getWonum() != null && x4WORKORDER.getWonum().length() > 20
                ? x4WORKORDER.getWonum().substring(x4WORKORDER.getWonum().length() - 20)
                : x4WORKORDER.getWonum();

        zstpms4EamConfirm.setInstance(wonum);
        zstpms4EamConfirm.setNetwork(x4WORKORDER.getX4ntk());
        zstpms4EamConfirm.setOperation(x4WORKORDER.getX4noperazione());
        zstpms4EamConfirm.setOdm(x4WORKORDER.getX4odm());
        zstpms4EamConfirm.setApprover(request.getApprover());

        Zttpms4EamTConfTm zttpms4EamTConfTm = new Zttpms4EamTConfTm();
        List<Zstpms4EamSConfTm> zstpms4EamSConfTmList = new ArrayList<>();
        Zstpms4EamSConfTm zstpms4EamSConfTm = new Zstpms4EamSConfTm();

        zstpms4EamSConfTm.setZmatricola(request.getLaborcode());

        BigDecimal hrwrk;
        BigDecimal hrtra;
        String eamKey;
        if (Objects.nonNull(aggregati) && Objects.nonNull(aggregati.getMember()) && Assorted.isNotEmpty(aggregati.getMember())) {
            eamKey = String.valueOf(request.getX4correlationid());
            hrwrk = BigDecimal.ZERO;
            hrtra = BigDecimal.ZERO;

            for (X4LabtransResponse.X4LabtransResponseItem agg : aggregati.getMember()) {
                if (agg == null) continue;

                if (agg.getX4oreuff() != null && !agg.getX4oreuff().trim().isEmpty()) {
                    zstpms4EamSConfTm.setZin("X");
                    hrwrk = hrwrk.add(new BigDecimal(agg.getX4oreuff().trim()));
                } else if (agg.getRegularhrs() != null) {
                    hrwrk = hrwrk.add(BigDecimal.valueOf(agg.getRegularhrs()));
                }

                if (agg.getX4oretrasf() != null && !agg.getX4oretrasf().trim().isEmpty()) {
                    hrtra = hrtra.add(new BigDecimal(agg.getX4oretrasf().trim()));
                }
            }
        } else {
            eamKey = request.getLabtransid();
            if (Objects.nonNull(request.getX4oreuff())) {
                zstpms4EamSConfTm.setZin("X");
                hrwrk = new BigDecimal(request.getX4oreuff());
            } else {
                hrwrk = Objects.nonNull(request.getRegularhrs())
                        ? BigDecimal.valueOf(request.getRegularhrs())
                        : BigDecimal.ZERO;
            }
            hrtra = Objects.nonNull(request.getX4oretrasf())
                    ? new BigDecimal(request.getX4oretrasf())
                    : BigDecimal.ZERO;
        }

        zstpms4EamSConfTm.setHrwrk(hrwrk.setScale(2, RoundingMode.HALF_UP));
        zstpms4EamSConfTm.setHrtra(hrtra.setScale(2, RoundingMode.HALF_UP));

        zstpms4EamSConfTm.setBudat(EamEtlUtils.convertToDateyyyyMMdd(request.getStartdatetime()));
        zstpms4EamSConfTm.setEamKey(eamKey);

        zstpms4EamSConfTmList.add(zstpms4EamSConfTm);
        zttpms4EamTConfTm.getItem().addAll(zstpms4EamSConfTmList);
        zstpms4EamConfirm.setWrktime(zttpms4EamTConfTm);

        zfmpms4EamConfirm.setConfirm(zstpms4EamConfirm);
        zfmpms4EamConfirm.setActvt(x4WORKORDER.getX4tipospesa());
        return zfmpms4EamConfirm;
    }

    private boolean controlloPeriodoContabile(String startDateTime, X4WORKORDER x4WORKORDER, String jwtToken) throws EAMException {
        ZmosCheckPeriodContFi requestSAP = buildCheckRequest(startDateTime, x4WORKORDER);
        ZmosCheckPeriodContFiResponse response = sapClient.requestToSAPCheckPeriodoContabile(requestSAP, jwtToken);
        if (Objects.nonNull(response) && Objects.nonNull(response.getOPeriodoAperto())) {
            log.info("PeriodoAperto: {}", response.getOPeriodoAperto());
            return Objects.equals(response.getOPeriodoAperto(), PERIODO_CONTABILE_APERTO);
        } else {
            log.error("Error in Response: [{}]", Objects.nonNull(response) ? writeObjectAsString(response) : null);
            return false;
        }
    }

    private ZmosCheckPeriodContFi buildCheckRequest(String startDateTime, X4WORKORDER x4WORKORDER) {
        ZmosCheckPeriodContFi finalRequest = new ZmosCheckPeriodContFi();
        finalRequest.setIEsercizio(getMonthAndYear(startDateTime, YEAR));
        finalRequest.setIPeriodo(getMonthAndYear(startDateTime, MONTH));
        finalRequest.setISocieta(x4WORKORDER.getX4burks());
        finalRequest.setITpConto(TP_CONTO);
        return finalRequest;
    }

    public JAXBElement<RichiestaConsuntivazioneCallbackResponse> richiestaConsuntivazioneCallback(JAXBElement<RichiestaConsuntivazioneCallback> callbackRequest, String uuidResend) {
        long startTime = System.currentTimeMillis();
        Instant startDate = Instant.now();
        String uuid = Uuid.randomUuid().toString();
        String payload = XmlUtility.convertToString(RichiestaConsuntivazioneCallback.class.getPackageName(), callbackRequest);
        log.info("Start richiestaConsuntivazioneCallback for Request [{}]", payload);
        X4EsitoIntRequest x4EsitoIntRequest = buildEsitoRequest(null, payload, uuid, startDate, SapInterface.CONSUNTIVI_CALLBACK, SAP, EAM_SAP);
        it.terna.kernel.soap.clientgen.sap.consuntivazione.callback.ObjectFactory factory = new it.terna.kernel.soap.clientgen.sap.consuntivazione.callback.ObjectFactory();
        RichiestaConsuntivazioneCallbackResponse response = new RichiestaConsuntivazioneCallbackResponse();
        List<RichiestaConsuntivazioneCallbackResponse.Item> itemList = new ArrayList<>();
        try {
            if (Objects.isNull(payload)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), SapInterface.CONSUNTIVI_CALLBACK.getName());
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + " [" + SapInterface.CONSUNTIVI_CALLBACK.getName() + "]");
            }
            if (Objects.nonNull(uuidResend)) {
                x4EsitoIntRequest.setUniqueid(uuidResend);
                X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
                x4EsitoIntRequest.setParent(Objects.nonNull(parent) ? parent.getX4EsitoIntId() : null);
            }

            if (Objects.isNull(callbackRequest) || Objects.isNull(callbackRequest.getValue()) || Assorted.isEmpty(callbackRequest.getValue().getItem())) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0027.getErrorDescription(), SapInterface.CONSUNTIVI_CALLBACK.getName());
                throw new EAMException(EAMErrorMapping.EAM_0027.getEamCode(), EAMErrorMapping.EAM_0027.getErrorDescription() + " [" + SapInterface.CONSUNTIVI_CALLBACK.getName() + "]");
            }
            for (RichiestaConsuntivazioneCallback.Item item : callbackRequest.getValue().getItem()) {
                RichiestaConsuntivazioneCallbackResponse.Item responseItem = new RichiestaConsuntivazioneCallbackResponse.Item();
                try {
                    log.info("Start richiestaConsuntivazioneCallback for item [{}]", writeObjectAsString(item));
                    if (Objects.isNull(item) || item.getEamKey() == 0 || Assorted.isEmpty(item.getResult())) {
                        responseItem.setESITO(ESITO_KO);
                        responseItem.setCODICEERRORE(EAMErrorMapping.EAM_0015.getEamCode());
                        responseItem.setDESCRIZIONEERRORE(EAMErrorMapping.EAM_0015.getErrorDescription());
                        log.error("{} - Response[{}] - for request [{}]", EAM_0015.getErrorDescription(), writeObjectAsString(responseItem), Objects.nonNull(item) ? writeObjectAsString(item) : null);
                        throw new EAMException(EAM_0015.getEamCode(), EAM_0015.getErrorDescription() + " [" + writeObjectAsString(item) + "]");
                    }
                    responseItem.setEamKey(item.getEamKey());
                    X4LabtransResponse labtransResponse = x4LabtransApiService.retrieveLabtransByCorrelationID(item.getEamKey());
                    if (Objects.isNull(labtransResponse) || labtransResponse.getHttpCode() >= 300 || Assorted.isEmpty(labtransResponse.getMember())) {
                        labtransResponse = x4LabtransApiService.retrieveLabtransById(item.getEamKey());
                        if (Objects.isNull(labtransResponse) || labtransResponse.getHttpCode() >= 300 || Assorted.isEmpty(labtransResponse.getMember())) {
                            responseItem.setESITO(ESITO_KO);
                            responseItem.setCODICEERRORE(EAMErrorMapping.EAM_0016.getEamCode());
                            responseItem.setDESCRIZIONEERRORE(EAMErrorMapping.EAM_0016.getErrorDescription());
                            log.error("{} - Response: [{}]", EAM_0016.getErrorDescription(), labtransResponse);
                            throw new EAMException(EAM_0016.getEamCode(), EAM_0016.getErrorDescription() + " [" + labtransResponse + "]");
                        }
                    }

                    for (X4LabtransResponse.X4LabtransResponseItem member : labtransResponse.getMember()) {
                        x4EsitoIntRequest.setWonum(member.getRefwo());
                        X4LabtransRequest request = new X4LabtransRequest();
                        request.setX4checksap(Objects.equals(item.getResult(), SUCCESS) ? ESITO_OK : ESITO_KO);
                        String x4esitosapdesc = Objects.equals(item.getResult(), SUCCESS) ? CONSUNTIVAZIONE_OK : item.getErrorCode();
                        request.setX4esitosapdesc(x4esitosapdesc);
                        if(Objects.equals(request.getX4checksap(), ESITO_KO)) {
                            request.setX4correlationid(null);
                        }
                        MaximoResponse maximoResponse = x4LabtransApiService.updateLabtrans(member.getHref(), request);
                        if (Objects.isNull(maximoResponse) || maximoResponse.getHttpCode() >= 300) {
                            responseItem.setESITO(ESITO_KO);
                            responseItem.setCODICEERRORE(EAM_0023.getEamCode());
                            responseItem.setDESCRIZIONEERRORE(EAM_0023.getErrorDescription());
                            log.error("{} - ConsuntivazioneCallback KO for Request[{}] - Response [{}]", EAM_0023.getErrorDescription(), writeObjectAsString(item), writeObjectAsString(responseItem));
                            throw new EAMException(EAM_0023.getEamCode(), EAM_0023.getErrorDescription() + " [" + maximoResponse + "]");
                        }
                        responseItem.setESITO(ESITO_OK);
                        log.info("ConsuntivazioneCallback OK for Request [{}] - Response [{}] ", writeObjectAsString(item), writeObjectAsString(responseItem));
                    }
                } catch (EAMException e) {
                    responseItem.setESITO(ESITO_KO);
                    responseItem.setCODICEERRORE(e.getErrorCode());
                    responseItem.setDESCRIZIONEERRORE(e.getMessage());
                    log.error("Error: {}", e.getMessage(), e);
                } catch (Exception e) {
                    responseItem.setESITO(ESITO_KO);
                    responseItem.setCODICEERRORE(EAM_0000.getEamCode());
                    responseItem.setDESCRIZIONEERRORE(e.getMessage());
                    log.error("Error: {}", e.getMessage(), e);
                }
                itemList.add(responseItem);
                log.info("End elaborazione Consuntivazione Callback - UUID: {}, item [{}] ", uuid, writeObjectAsString(item));
            }
            response.getItem().addAll(itemList);
            String errori = response.getItem().stream()
                    .filter(i -> Objects.equals(i.getESITO(), ESITO_KO))
                    .map(RichiestaConsuntivazioneCallbackResponse.Item::getDESCRIZIONEERRORE)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("\n"));

            if (!errori.isEmpty()) {
                throw new EAMException(EAM_0000.getEamCode(), errori);
            }
            x4EsitoIntRequest.setCodesito(ESITO_OK);
        } catch (EAMException e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
        } finally {
            x4EsitoIntRequest.setEnd_elab(esitoDate(Instant.now()));
        }
        sendEsitoIntRequest(x4EsitoIntRequest, x4EsitoIntApiService);
        log.info("End elaborazione Consuntivazione Callback - UUID: {} - request: {} - Duration: {} seconds", uuid, payload, (System.currentTimeMillis() - startTime) / 1000);
        return factory.createRichiestaConsuntivazioneCallbackResponse(response);
    }

    /**
     * Gestisce l'acquisizione delle Network (NTW) da SAP, inviando i dati acquisiti a un topic Kafka e registrando i risultati.
     * Esegue in parallelo o in sequenza l'elaborazione delle Network a seconda della configurazione.
     *
     * @param topic      il topic Kafka per l'acquisizione dei dati.
     * @param uuidResend l'UUID da utilizzare per l'invio di una richiesta di riesecuzione, se presente.
     * @param payload    il payload JSON contenente i dati da elaborare.
     */
    @SuppressWarnings("null")
    public void acquisitionNtk(String topic, String uuidResend, String payload) {
        Instant startDate = Instant.now();
        String uuid = Uuid.randomUuid().toString();
        X4EsitoIntRequest x4EsitoIntRequest = buildEsitoRequest(topic, payload, uuid, startDate, Catalog.SapInterface.ACQUISITION_NTK, Constants.EAM_SAP, Constants.SAP);
        String result = null;
        long startTime = System.currentTimeMillis();
        log.info("Starting acquisition of Network from SAP - UUID: {}, Topic: {}", uuid, topic);
        try {
            if (Objects.isNull(payload)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), topic);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + " [" + topic + "]");
            }
            if (Objects.nonNull(uuidResend)) {
                x4EsitoIntRequest.setUniqueid(uuidResend);
                X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
                x4EsitoIntRequest.setParent(Objects.nonNull(parent) ? parent.getX4EsitoIntId() : null);
            }

            KafkaX4AcquisitionData request = new Gson().fromJson(payload, KafkaX4AcquisitionData.class);
            if (Objects.isNull(request)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), request);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + "[" + request + "]");
            }
            sapProperties.setFilters(
                    x4SapFilterQueryService.getFilter().stream()
                            .map(f -> new SapProperties.FilterSap(f.getFiltro(), f.getTipoSpesa(), f.getTipoAttivita(), f.getTipoOrdine(), f.getLivello(), f.getStato()))
                            .toList()
            );

            List<X4NTK> x4NTK = x4NtkQueryService.getRequests();
            List<X4WBENTk> x4WBENTks = x4WBEQueryService.getWbeNtk();
            List<X4StatiSap> statiSapList = x4StatiSapQueryService.getAll();
            boolean isFirstTime = (Objects.isNull(x4NTK) || Assorted.isEmpty(x4NTK));
            List<ZacquisizioneNtw> zacquisizioneNtws = buildX4NtkRequest(sapProperties.getFilters(), x4NTK, x4WBENTks);
            if (Assorted.isEmpty(zacquisizioneNtws)) {
                log.error("{}", EAM_0028.getErrorDescription());
                throw new EAMException(EAM_0028.getEamCode(), EAM_0028.getErrorDescription());
            }
            result = processNetworkSequenziali(zacquisizioneNtws, isFirstTime, statiSapList);
        } catch (EAMException e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
        } finally {
            if(Objects.nonNull(result)) {
                x4EsitoIntRequest.setDesc_esito(result);
            }
            x4EsitoIntRequest.setEnd_elab(esitoDate(Instant.now()));
        }
        sendEsitoIntRequest(x4EsitoIntRequest, x4EsitoIntApiService);
        log.info("Completed acquisition of Network from SAP - UUID: {}, Topic: {}, Duration: {} seconds", uuid, topic, (System.currentTimeMillis() - startTime) / 1000);
    }

    private List<ZacquisizioneNtw> buildX4NtkRequest(List<SapProperties.FilterSap> filters, List<X4NTK> x4NTKS, List<X4WBENTk> x4WBENTks) throws EAMException {
        List<ZacquisizioneNtw> requestsList = new ArrayList<>();
        List<String> statiFirstTime = filters.stream().filter(f -> NTK.equalsIgnoreCase(f.getFiltro()) && (BLOC.equalsIgnoreCase(f.getStato()) || TECO.equalsIgnoreCase(f.getStato()))).map(SapProperties.FilterSap::getStato).toList();
        List<String> statiDelta = filters.stream().filter(f -> NTK.equalsIgnoreCase(f.getFiltro())).map(SapProperties.FilterSap::getStato).toList();
        List<List<String>> wbsBatch = partition(x4WBENTks.stream().map(X4WBENTk::getIdwbe).toList(), sapProperties.getBatchSize());
        if (Assorted.isNotEmpty(x4WBENTks)) {
            try {
                if (Objects.isNull(x4NTKS) || Assorted.isEmpty(x4NTKS)) {
                    wbsBatch.forEach(wbe -> {
                        ZacquisizioneNtw request = buildRequestNTK(statiFirstTime, wbe, sapProperties.getStartDate(), sapProperties.getStartDate(), NE);
                        requestsList.add(request);
                    });
                } else {
                    wbsBatch.forEach(wbe -> {
                        ZacquisizioneNtw request = buildRequestNTK(statiDelta, wbe, String.valueOf(x4NTKS.get(Values.FIRST_ELEMENT).getCreatedate()), String.valueOf(x4NTKS.get(Values.FIRST_ELEMENT).getChangeddate()), EQ);
                        requestsList.add(request);
                    });
                }
            } catch (Exception e) {
                throw new EAMException("Errore durante l'elaborazione della richiesta per Stato: ", e.getMessage());
            }
        }

        return requestsList;
    }

    private <T> List<List<T>> partition(List<T> list, int size) {
        return IntStream.range(0, (list.size() + size - 1) / size)
                .mapToObj(i -> list.subList(i * size, Math.min((i + 1) * size, list.size())))
                .toList();
    }

    private ZacquisizioneNtw buildRequestNTK(List<String> stati, List<String> wbeList, String createDate, String changeDate, String operator) {
        ZacquisizioneNtw finalRequest = new ZacquisizioneNtw();
        ZpsTListaStati zpsTListaStati = new ZpsTListaStati();
        if (Objects.nonNull(stati) && Assorted.isNotEmpty(stati)) {
            stati.forEach(stato -> {
                ZpsSListaStati zpsSListaStati = new ZpsSListaStati();
                zpsSListaStati.setStatoSistema(stato);
                zpsTListaStati.getItem().add(zpsSListaStati);
            });
            finalRequest.setOperatoreListaStati(operator);
        }
        finalRequest.setStati(zpsTListaStati);
        ZpsTListaWbs zpsTListaWbs = new ZpsTListaWbs();
        if (Objects.nonNull(wbeList) && Assorted.isNotEmpty(wbeList)) {
            wbeList.forEach(wbe -> {
                ZpsSListaWbs zpsSListaWbs = new ZpsSListaWbs();
                zpsSListaWbs.setElementoWbs(wbe);
                zpsTListaWbs.getItem().add(zpsSListaWbs);
            });
        }
        finalRequest.setWbs(zpsTListaWbs);
        finalRequest.setDataCreazione(createDate);
        finalRequest.setDataModifica(changeDate);

        return finalRequest;
    }


    private String processNetworkSequenziali(List<ZacquisizioneNtw> ntwList, boolean isFirstTime, List<X4StatiSap> statiSapList) {
        int count = 1;
        int error = 0;
        int success = 0;
        List<String> errorMessages = new ArrayList<>();
        for (ZacquisizioneNtw finalRequest : ntwList) {
            log.info("Request to SAP for NTK acquisition - Progress: {}/{}", count, ntwList.size());
            try {
                JwtTokenResponse jwtTokenResponse = getTokenOSB(EDataIntegrationEntity.ACQUISIZIONE_NTK);
                String jwtToken = Objects.nonNull(jwtTokenResponse) && jwtTokenResponse.getHttpCode() == 200 ? jwtTokenResponse.getAccessToken() : null;
                ZacquisizioneNtwResponse response = sapClient.requestToSAPNtk(finalRequest, jwtToken);
                if(isFirstTime) {
                    elaborationX4NTKjdbc(response, statiSapList);
                } else {
                    elaborationX4Ntk(response, statiSapList);
                }
                success++;
            } catch (Exception e) {
                log.error("Errore durante l'elaborazione della network: {}", finalRequest, e);
                int wbs = Objects.nonNull(finalRequest.getWbs()) && Assorted.isNotEmpty(finalRequest.getWbs().getItem()) ? finalRequest.getWbs().getItem().size() : 0;
                String dataCreazione = Objects.nonNull(finalRequest.getDataCreazione()) ? finalRequest.getDataCreazione() : "N/A";
                String dataModifica = Objects.nonNull(finalRequest.getDataModifica()) ? finalRequest.getDataModifica() : "N/A";
                String errMsg = String.format("[%d/%d] NTK=%s: %s",
                        count, ntwList.size(),
                        String.format("WBS=%d, DataCreazione=%s, DataModifica=%s", wbs, dataCreazione, dataModifica),
                        e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
                errorMessages.add(errMsg);
                error++;
            } finally {
                count++;
            }
        }
        String msg = String.format("Completed processing NTK acquisition - Total: %d, Success: %d, Error: %d", ntwList.size(), success, error);
        log.info(msg);
        if (errorMessages.isEmpty()) {
            return msg;
        }

        String full = String.format("ERR %d/%d\n", error, ntwList.size()) + String.join("\n", errorMessages);
        return full.length() > 2000 ? full.substring(0, 1997) + "..." : full;
    }

    private void elaborationX4Ntk(ZacquisizioneNtwResponse response, List<X4StatiSap> statiSapList)
            throws RestException {
        if (Assorted.notNull(response, response.getNetwork(), response.getNetwork().getItem()) && Assorted.isNotEmpty(response.getNetwork().getItem())) {
            for (ZpsSAcquisizioneNtw item : response.getNetwork().getItem()) {
                MaximoResponse maximoResponse;
                X4NtkResponse x4NtkResponse = x4NtkApiService.getById(item.getIdnetwork());
                if (Objects.nonNull(x4NtkResponse) && x4NtkResponse.getHttpCode() == 200) {
                    X4NtkRequest request = populateX4Ntk(item, statiSapList);
                    if (Assorted.notNull(x4NtkResponse, x4NtkResponse.getMember()) && Assorted.isNotEmpty(x4NtkResponse.getMember())) {
                        X4NtkResponse.X4NtkResponseItem member = x4NtkResponse.getMember().get(Values.FIRST_ELEMENT);
                        maximoResponse = x4NtkApiService.update(member.getHref(), request);
                        if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                            log.info("X4NTK update eseguita: [{}]", maximoResponse.getHttpCode());
                        } else {
                            log.error("X4NTK update KO for id [{}]", item.getIdnetwork());
                        }
                        if (Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                            for (ZpsSOperazioniNtw itemNtkOp : item.getOperazioniNetwork().getItem()) {
                                X4NtkOpResponse x4NtkOpResponse = x4NtkOpApiService.getByOpAndIdNtk(itemNtkOp.getIdOperazione(), member.getIdntk());
                                X4NtkOpRequest requestOp = populateX4NtkOp(item, itemNtkOp, statiSapList);
                                if (Objects.nonNull(x4NtkOpResponse) && x4NtkOpResponse.getHttpCode() == 200) {
                                    if (Assorted.notNull(x4NtkOpResponse, x4NtkOpResponse.getMember()) && Assorted.isNotEmpty(x4NtkOpResponse.getMember())) {
                                        X4NtkOpResponse.X4NtkOpResponseItem memberOp = x4NtkOpResponse.getMember().get(Values.FIRST_ELEMENT);
                                        maximoResponse = x4NtkOpApiService.update(memberOp.getHref(), requestOp);
                                        if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                                            log.info("X4NTKOP update eseguita: [{}]", maximoResponse.getHttpCode());
                                        } else {
                                            log.error("X4NTKOP update KO for idNtk [{}] and idOp [{}]", member.getIdntk(), itemNtkOp.getIdOperazione());
                                        }
                                    } else {
                                        maximoResponse = x4NtkOpApiService.insert(requestOp);
                                        if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                                            log.info("X4NTKOP insert eseguita: [{}]", maximoResponse.getHttpCode());
                                        } else {
                                            log.error("X4NTKOP insert KO for idNtk [{}] and idOp [{}]", member.getIdntk(), itemNtkOp.getIdOperazione());
                                        }
                                    }
                                } else {
                                    log.error("Error for x4NtkOpResponse: [{}]", Objects.nonNull(x4NtkOpResponse) ? x4NtkOpResponse.getHttpCode() : null);
                                }
                            }
                        }
                    } else {
                        maximoResponse = x4NtkApiService.insert(request);
                        if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                            log.info("X4NTK insert eseguita: [{}]", maximoResponse.getHttpCode());
                        } else {
                            log.error("X4NTK insert KO for id [{}]", item.getIdnetwork());
                        }
                        if (Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                            for (ZpsSOperazioniNtw itemNtkOp : item.getOperazioniNetwork().getItem()) {
                                X4NtkOpRequest requestOp = populateX4NtkOp(item, itemNtkOp, statiSapList);
                                maximoResponse = x4NtkOpApiService.insert(requestOp);
                                if(Objects.nonNull(maximoResponse) && maximoResponse.getHttpCode() == 200) {
                                    log.info("X4NTKOP insert eseguita: [{}]", maximoResponse.getHttpCode());
                                } else {
                                    log.error("X4NTKOP insert KO for idNtk [{}] and idOp [{}]", item.getIdnetwork(), itemNtkOp.getIdOperazione());
                                }
                            }
                        }
                    }
                } else {
                    log.error("Error for x4NtkResponse: [{}]", Objects.nonNull(x4NtkResponse) ? x4NtkResponse.getHttpCode() : null);
                }
            }
        } else {
            if (Objects.nonNull(response) && Objects.nonNull(response.getReturn()) && Assorted.isNotEmpty(response.getReturn().getItem())) {
                log.error("Error ZacquisizioneNtwResponse: {}", response.getReturn().getItem().get(Values.FIRST_ELEMENT).getMessage());
            }
        }
    }

    private void elaborationX4NTKjdbc(ZacquisizioneNtwResponse response, List<X4StatiSap> statiSapList)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.info("Starting elaborationX4ODMFirst() |{}|", startTime);
        if (Assorted.notNull(response, response.getNetwork(), response.getNetwork().getItem()) && Assorted.isNotEmpty(response.getNetwork().getItem())) {
            for (ZpsSAcquisizioneNtw item : response.getNetwork().getItem()) {
                X4NTKjdbc x4Ntk = x4NtkQueryService.getById(item.getIdnetwork());
                x4Ntk = Objects.nonNull(x4Ntk) ? x4Ntk : new X4NTKjdbc();
                populateX4Ntkjdbc(item, statiSapList, x4Ntk);
                x4NtkQueryService.saveOrUpdate(x4Ntk);
                log.info("X4NTK salvataggio eseguito: [{}]", x4Ntk.getIdntk());
                for (ZpsSOperazioniNtw itemNtkOp : item.getOperazioniNetwork().getItem()) {
                    X4OPNTK x4OPNTK = x4NtkQueryService.getByIdOp(x4Ntk.getIdntk(), itemNtkOp.getIdOperazione());
                    x4OPNTK = Objects.nonNull(x4OPNTK) ? x4OPNTK : new X4OPNTK();
                    populateX4NtkOpjdbc(item, itemNtkOp, statiSapList, x4OPNTK);
                    x4NtkQueryService.saveOrUpdateOp(x4OPNTK);
                    log.info("OPNTK salvataggio eseguito: [{}]", x4OPNTK.getIdntk());
                }
            }
        } else {
            if (Objects.nonNull(response) && Objects.nonNull(response.getReturn()) && Assorted.isNotEmpty(response.getReturn().getItem())) {
                log.error("Error ZacquisizioneNtwResponse: {}", response.getReturn().getItem().get(Values.FIRST_ELEMENT).getMessage());
            }
        }
        log.info("Ending elaborationX4ODMFirst() |{}|", System.currentTimeMillis() - startTime);
    }

    private void populateX4Ntkjdbc(ZpsSAcquisizioneNtw item, List<X4StatiSap> statiSapList, X4NTKjdbc request) {
        request.setIdntk(item.getIdnetwork());
        request.setDescription(item.getDescrizione());
        request.setStatosistemasap(item.getStatoSistema());
        request.setStatoutentesap(item.getStatoUtente());
        request.setProgetto(item.getProgetto());
        request.setWbe(item.getElementoCbs());
        request.setSocieta(item.getSocieta());
        request.setProfitcenter(item.getProfitcenter());
        request.setCdcresp(item.getCdcResponsabile());
        request.setTipo(item.getTipoNetwork());
        request.setHasld(zero);
        request.setCreatedate(LocalDate.parse(item.getDataCreazione()));
        request.setChangeddate(Objects.equals(item.getDataModifica(), sapProperties.getNoDate())
                ? LocalDate.parse(item.getDataCreazione())
                : LocalDate.parse(item.getDataModifica()));
        request.setChangedby(item.getAutoreModifica());
        if (Assorted.isNotEmpty(statiSapList)) {
            Map<String, String> statoMap = statiSapList.stream()
                    .collect(Collectors.toMap(X4StatiSap::getStato, X4StatiSap::getCodice));

            request.setStatosistema(Arrays.stream(item.getStatoSistema().split("\\|"))
                    .filter(s -> !s.isEmpty())
                    .map(statoMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("|", "|", "")));
        }
    }

    private X4NtkRequest populateX4Ntk(ZpsSAcquisizioneNtw item, List<X4StatiSap> statiSapList) {
        X4NtkRequest request = new X4NtkRequest();
        request.setIdntk(item.getIdnetwork());
        request.setDescription(item.getDescrizione());
        request.setStatosistemasap(item.getStatoSistema());
        request.setStatoutentesap(item.getStatoUtente());
        request.setProgetto(item.getProgetto());
        request.setWbe(item.getElementoCbs());
        request.setSocieta(item.getSocieta());
        request.setProfitcenter(item.getProfitcenter());
        request.setCdcresp(item.getCdcResponsabile());
        request.setTipo(item.getTipoNetwork());
        request.setCreatedate(item.getDataCreazione());
        request.setChangeddate(Objects.equals(item.getDataModifica(), sapProperties.getNoDate()) ? item.getDataCreazione() : item.getDataModifica());
        request.setChangedby(item.getAutoreModifica());
        if (Assorted.isNotEmpty(statiSapList)) {
            Map<String, String> statoMap = statiSapList.stream()
                    .collect(Collectors.toMap(X4StatiSap::getStato, X4StatiSap::getCodice));

            request.setStatosistema(Arrays.stream(item.getStatoSistema().split("\\|"))
                    .filter(s -> !s.isEmpty())
                    .map(statoMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("|", "|", "")));
        }

        return request;
    }

    private void populateX4NtkOpjdbc(ZpsSAcquisizioneNtw item, ZpsSOperazioniNtw itemOp, List<X4StatiSap> statiSapList, X4OPNTK request) {
        request.setIdntk(item.getIdnetwork());
        request.setIdoperazione(itemOp.getIdOperazione());
        request.setDescription(itemOp.getDescOperazione());
        request.setStato(itemOp.getStatoSistema());
        request.setCdlresp(itemOp.getCdlResp());
        request.setHasld(zero);
        LocalDate createDate = Assorted.isEmpty(itemOp.getDataCreazione()) || Objects.equals(itemOp.getDataCreazione(), sapProperties.getNoDate())
                ? null
                : LocalDate.parse(itemOp.getDataCreazione());
        request.setCreatedate(createDate);
        LocalDate changeDate = Assorted.isEmpty(itemOp.getDataModifica()) || Objects.equals(itemOp.getDataModifica(), sapProperties.getNoDate())
                ? null
                : LocalDate.parse(itemOp.getDataModifica());
        request.setChangeddate(changeDate);
        request.setChangedby(itemOp.getAutoreCreazione());
        request.setWbe(itemOp.getElementoCbs());
        if (Assorted.isNotEmpty(statiSapList)) {
            Map<String, String> statoMap = statiSapList.stream()
                    .collect(Collectors.toMap(X4StatiSap::getStato, X4StatiSap::getCodice));

            request.setStatosistema(Arrays.stream(itemOp.getStatoSistema().split("\\|"))
                    .filter(s -> !s.isEmpty())
                    .map(statoMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("|", "|", "")));
        }
    }

    private X4NtkOpRequest populateX4NtkOp(ZpsSAcquisizioneNtw item, ZpsSOperazioniNtw itemOp, List<X4StatiSap> statiSapList) {
        X4NtkOpRequest request = new X4NtkOpRequest();
        request.setIdntk(item.getIdnetwork());
        request.setIdoperazione(itemOp.getIdOperazione());
        request.setDescription(itemOp.getDescOperazione());
        request.setStato(itemOp.getStatoSistema());
        request.setCdlresp(itemOp.getCdlResp());
        request.setCreatedate(itemOp.getDataCreazione());
        request.setChangeddate(Objects.equals(itemOp.getDataModifica(), sapProperties.getNoDate()) ? null : itemOp.getDataModifica());
        request.setChangedby(itemOp.getAutoreCreazione());
        request.setWbe(itemOp.getElementoCbs());
        if (Assorted.isNotEmpty(statiSapList)) {
            Map<String, String> statoMap = statiSapList.stream()
                    .collect(Collectors.toMap(X4StatiSap::getStato, X4StatiSap::getCodice));

            request.setStatosistema(Arrays.stream(itemOp.getStatoSistema().split("\\|"))
                    .filter(s -> !s.isEmpty())
                    .map(statoMap::get)
                    .filter(Objects::nonNull)
                    .collect(Collectors.joining("|", "|", "")));
        }
        return request;
    }

    @SuppressWarnings("null")
    public void closureOdM(String topic, String uuidResend, String payload) {
        long startTime = System.currentTimeMillis();
        Instant startDate = Instant.now();
        String uuid = Uuid.randomUuid().toString();
        log.info("Start Chiusura Ordine di Manutenzione - UUID: {}, Topic: {} - Payload: {}", uuid, topic, payload);
        X4EsitoIntRequest x4EsitoIntRequest = buildEsitoRequest(topic, payload, uuid, startDate, SapInterface.CLOSURE_ODM, Constants.EAM_SAP, Constants.SAP);
        try {
            if (Objects.isNull(payload)) {
                log.error("{} [{}]", EAM_0006.getErrorDescription(), topic);
                throw new EAMException(EAM_0006.getEamCode(), EAM_0006.getErrorDescription() + " [" + topic + "]");
            }
            if (Objects.nonNull(uuidResend)) {
                x4EsitoIntRequest.setUniqueid(uuidResend);
                X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
                x4EsitoIntRequest.setParent(Objects.nonNull(parent) ? parent.getX4EsitoIntId() : null);
            }

            KafkaX4Closure request = new Gson().fromJson(payload, KafkaX4Closure.class);
            if (Objects.isNull(request)) {
                log.error("{} [{}]", EAM_0006.getErrorDescription(), request);
                throw new EAMException(EAM_0006.getEamCode(), EAM_0006.getErrorDescription() + " [" + request + "]");
            }
            JwtTokenResponse jwtTokenResponse = getTokenOSB(EDataIntegrationEntity.SAP_CLOSE_ODM);
            String jwtToken = Objects.nonNull(jwtTokenResponse) && jwtTokenResponse.getHttpCode() == 200 ? jwtTokenResponse.getAccessToken() : null;
            if (Objects.isNull(jwtToken)) {
                log.error("{} - HttpCode: [{}]", EAM_0024.getErrorDescription(), Objects.nonNull(jwtTokenResponse) ? jwtTokenResponse.getHttpCode() : null);
                throw new EAMException(EAM_0024.getEamCode(), EAM_0024.getErrorDescription() + " - httpcode: [" + (Objects.nonNull(jwtTokenResponse) ? jwtTokenResponse.getHttpCode() : null) + "]");
            }
            x4EsitoIntRequest.setWonum(request.getWonum());
            closeOdM(request, jwtToken);
        } catch (EAMException e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
        } finally {
            x4EsitoIntRequest.setEnd_elab(esitoDate(Instant.now()));
        }
        sendEsitoIntRequest(x4EsitoIntRequest, x4EsitoIntApiService);
        log.info("End Chiusura Ordine di Manutenzione - UUID: {}, Topic: {}, Duration: {} seconds", uuid, topic, (System.currentTimeMillis() - startTime) / 1000);
    }

    private void closeOdM(KafkaX4Closure kafkaX4Closure, String jwtToken) throws EAMException {
        ChiusuraODM requestSap = populateOdMClosureRequest(kafkaX4Closure);
        ChiusuraODMResponse response = sapClient.requestToSAPCloseOdM(requestSap, jwtToken);
        if (Objects.isNull(response)
                || Objects.isNull(response.getReturn())
                || (!Objects.equals(response.getReturn().getType(), SUCCESS)
                && !Objects.equals(response.getReturn().getType(), WARNING))) {
            log.error("Chiusura Ordine di Manutenzione [{}] per Ordine di Lavoro [{}] non eseguita con successo. - Response: [{}].", kafkaX4Closure.getX4odm(), kafkaX4Closure.getWonum(), writeObjectAsString(response));
            throw new EAMException(EAM_0036.getEamCode(), EAM_0036.getErrorDescription() + " [" + writeObjectAsString(response) + "]");
        }
        log.info("Chiusura Ordine di Manutenzione [{}] per Ordine di Lavoro [{}] eseguita con successo. - Response: [{}].", kafkaX4Closure.getX4odm(), kafkaX4Closure.getWonum(), writeObjectAsString(response));
    }

    private ChiusuraODM populateOdMClosureRequest(KafkaX4Closure kafkaX4Closure) {
        ChiusuraODM finalRequest = new ChiusuraODM();
        finalRequest.setAufnr(kafkaX4Closure.getX4odm());
        return finalRequest;
    }

    @SuppressWarnings("null")
    public void closureNtk(String topic, String uuidResend, String payload) {
        long startTime = System.currentTimeMillis();
        log.info("Starting Chiusura Network with topic: {}, payload: {}", topic, payload);
        Instant startDate = Instant.now();
        String uuid = Uuid.randomUuid().toString();
        X4EsitoIntRequest x4EsitoIntRequest = buildEsitoRequest(topic, payload, uuid, startDate, SapInterface.CLOSURE_NTK, Constants.EAM_SAP, Constants.SAP);
        try {
            if (Objects.isNull(payload)) {
                log.error("{} [{}]", EAM_0006.getErrorDescription(), topic);
                throw new EAMException(EAM_0006.getEamCode(), EAM_0006.getErrorDescription() + " [" + topic + "]");
            }
            if (Objects.nonNull(uuidResend)) {
                x4EsitoIntRequest.setUniqueid(uuidResend);
                X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
                x4EsitoIntRequest.setParent(Objects.nonNull(parent) ? parent.getX4EsitoIntId() : null);
            }

            KafkaX4Closure request = new Gson().fromJson(payload, KafkaX4Closure.class);
            if (Objects.isNull(request)) {
                log.error("{} [{}]", EAM_0006.getErrorDescription(), request);
                throw new EAMException(EAM_0006.getEamCode(), EAM_0006.getErrorDescription() + " [" + request + "]");
            }
            JwtTokenResponse jwtTokenResponse = getTokenOSB(EDataIntegrationEntity.SAP_CLOSE_ODM);
            String jwtToken = Objects.nonNull(jwtTokenResponse) && jwtTokenResponse.getHttpCode() == 200 ? jwtTokenResponse.getAccessToken() : null;
            if (Objects.isNull(jwtToken)) {
                log.error("{} - HttpCode: [{}]", EAM_0024.getErrorDescription(), Objects.nonNull(jwtTokenResponse) ? jwtTokenResponse.getHttpCode() : null);
                throw new EAMException(EAM_0024.getEamCode(), EAM_0024.getErrorDescription() + " - httpcode: [" + (Objects.nonNull(jwtTokenResponse) ? jwtTokenResponse.getHttpCode() : null) + "]");
            }
            x4EsitoIntRequest.setWonum(request.getWonum());
            closeNtk(request, jwtToken);
        } catch (EAMException e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
        } finally {
            x4EsitoIntRequest.setEnd_elab(esitoDate(Instant.now()));
        }
        sendEsitoIntRequest(x4EsitoIntRequest, x4EsitoIntApiService);
        log.info("End Chiusura Ordine di Manutenzione - UUID: {}, Topic: {}, Duration: {} seconds", uuid, topic, (System.currentTimeMillis() - startTime) / 1000);
    }

    private void closeNtk(KafkaX4Closure kafkaX4Closure, String jwtToken) throws EAMException {
        ZchiusuraNtw requestSap = populateX4NtkClose(kafkaX4Closure);
        ZchiusuraNtwResponse response = sapClient.requestToSAPCloseNtk(requestSap, jwtToken);
        if (Objects.isNull(response)
                || Objects.isNull(response.getReturn())
                || (!Objects.equals(response.getReturn().getType(), SUCCESS)
                && !Objects.equals(response.getReturn().getType(), WARNING))) {
            log.error("Chiusura Network [{}] e Operazione [{}] per Ordine di Lavoro [{}] non eseguita con successo. - Response: [{}].", kafkaX4Closure.getX4ntk(), kafkaX4Closure.getX4noperazione(), kafkaX4Closure.getWonum(), writeObjectAsString(response));
            throw new EAMException(EAM_0037.getEamCode(), EAM_0037.getErrorDescription() + " [" + writeObjectAsString(response) + "]");
        }
        log.info("Chiusura Network [{}] e Operazione [{}] per Ordine di Lavoro [{}] eseguita con successo. - Response: [{}].", kafkaX4Closure.getX4ntk(), kafkaX4Closure.getX4noperazione(), kafkaX4Closure.getWonum(), writeObjectAsString(response));
    }

    private ZchiusuraNtw populateX4NtkClose(KafkaX4Closure kafkaX4Closure) {
        ZchiusuraNtw finalRequest = new ZchiusuraNtw();
        finalRequest.setIstanza(kafkaX4Closure.getWonum());
        finalRequest.setNetwork(kafkaX4Closure.getX4ntk());
        finalRequest.setOperazione(kafkaX4Closure.getX4noperazione());
        return finalRequest;
    }

    public void invioConsuntiviToExt(String topic, String uuidResend, String payload) {
        long startTime = System.currentTimeMillis();
        Instant startDate = Instant.now();
        String uuid = Uuid.randomUuid().toString();
        log.info("Start Invio Consuntivi Ext SAP - UUID: {}, Topic: {} - Payload: {}", uuid, topic, payload);
        X4EsitoIntRequest x4EsitoIntRequest = buildEsitoRequest(topic, payload, uuid, startDate, SapInterface.CONSUNTIVI_EXT, Constants.EAM_SAP, Constants.SAP);
        try {
            if (Objects.isNull(payload)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), topic);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + " [" + topic + "]");
            }
            if (Objects.nonNull(uuidResend)) {
                x4EsitoIntRequest.setUniqueid(uuidResend);
                X4EsitoInt parent = x4EsitoIntQueryService.getEsitoParent(uuidResend);
                x4EsitoIntRequest.setParent(Objects.nonNull(parent) ? parent.getX4EsitoIntId() : null);
            }
            KafkaX4Labtrans request = new Gson().fromJson(payload, KafkaX4Labtrans.class);
            if (Objects.isNull(request)) {
                log.error("{} [{}]", EAMErrorMapping.EAM_0006.getErrorDescription(), request);
                throw new EAMException(EAMErrorMapping.EAM_0006.getEamCode(), EAMErrorMapping.EAM_0006.getErrorDescription() + "[" + request + "]");
            }
            BrokerEntityInfo brokerEntityInfoPublicationEndpoint = brokerEntities.getEndPoints().get("laborSAPExt");
            kafkaHandler.shoot(Common.EOperation.CREATE, brokerEntityInfoPublicationEndpoint, List.of(request));
        } catch (EAMException e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
        } catch (Exception e) {
            logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, null, null);
        } finally {
            x4EsitoIntRequest.setEnd_elab(esitoDate(Instant.now()));
        }
        sendEsitoIntRequest(x4EsitoIntRequest, x4EsitoIntApiService);
        log.info("End Invio Consuntivi Ext SAP - UUID: {}, Topic: {}, Duration: {} seconds", uuid, topic, (System.currentTimeMillis() - startTime) / 1000);
    }

    public void sendStorno(String loaded) {
        long startTime = System.currentTimeMillis();
        log.info("Start send storno");
        List<X4EsitoIntRequest> x4siteIntRequests = new ArrayList<>();
        int count = 1;
        int error = 0;
        int success = 0;
        int size;
        try {
            List<X4STORNOSAP> stornosapList = x4SapFilterQueryService.getStorno(loaded);
            JwtTokenResponse jwtTokenResponse = getTokenOSB(EDataIntegrationEntity.SAP_STORNO);
            String jwtToken = Objects.nonNull(jwtTokenResponse) && jwtTokenResponse.getHttpCode() == 200 ? jwtTokenResponse.getAccessToken() : null;
            Instant instant = Instant.now();

            size = stornosapList.size();
            for(X4STORNOSAP storno : stornosapList) {
                log.info("Process storno {}/{}", count, size);
                X4EsitoIntRequest x4EsitoIntRequest = buildEsitoRequest(null, null, null, instant, "SEND_STORNO", EAM_SAP, SAP);
                try {
                    x4EsitoIntRequest.setDescription(String.valueOf(storno.getId()));
                    x4EsitoIntRequest.setMessage(storno.toString());
                    x4EsitoIntRequest.setWonum(storno.getOdl());
                    ZmosWsReversalHoursOdm requestSapStorno = new ZmosWsReversalHoursOdm();
                    requestSapStorno.setIZzinstance(storno.getOdl());
                    requestSapStorno.setIAufnr(storno.getOdm());
                    requestSapStorno.setITpStorno(storno.getTipo());
                    ZttStornoOre iDatiConferme = new ZttStornoOre();
                    List<ZstStornoOre> itemDatiConferme = new ArrayList<>();
                    ZstStornoOre zstStornoOre = new ZstStornoOre();
                    zstStornoOre.setRueck(storno.getConfermaore());
                    zstStornoOre.setRmzhl(storno.getContatoreprogressivo());
                    itemDatiConferme.add(zstStornoOre);

                    iDatiConferme.getItem().addAll(itemDatiConferme);
                    requestSapStorno.setIDatiConferme(iDatiConferme);
                    requestSapStorno.setIModEsecuzione("E");
                    if (count % 100 == 0 || isTokenExpired(jwtTokenResponse)) {
                        jwtTokenResponse = getTokenOSB(EDataIntegrationEntity.SAP_STORNO);
                        jwtToken = Objects.nonNull(jwtTokenResponse) && jwtTokenResponse.getHttpCode() == 200 ? jwtTokenResponse.getAccessToken() : null;
                    }

                    ZmosWsReversalHoursOdmResponse responseStorno = sapClient.requestToSAPStorno(requestSapStorno, jwtToken);

                    if (Objects.isNull(responseStorno)
                            || Objects.isNull(responseStorno.getOBapiret2())
                            || Assorted.isEmpty(responseStorno.getOBapiret2().getItem())
                            || Objects.isNull(responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT))
                            || !Objects.equals(responseStorno.getOBapiret2().getItem().get(Values.FIRST_ELEMENT).getType(), SUCCESS)) {
                        x4EsitoIntRequest.setEsito(ESITO_KO);
                        error++;
                    } else {
                        x4EsitoIntRequest.setEsito(ESITO_OK);
                        success++;
                    }
                    x4EsitoIntRequest.setDesc_esito(writeObjectAsString(responseStorno));
                } catch (EAMException e) {
                    error++;
                    logErrorAndSetFailureStatus(log, x4EsitoIntRequest, e, e.getMessage(), e.getErrorCode());
                } finally {
                    count++;
                    x4EsitoIntRequest.setEnd_elab(esitoDate(Instant.now()));
                    x4siteIntRequests.add(x4EsitoIntRequest);
                }
            }
        } catch (EAMException e) {
            log.error("Error send storno: {}", e.getMessage());
            throw new RuntimeException(e);
        }
        finally {
            log.info("Write esito send storno");
            for(X4EsitoIntRequest req : x4siteIntRequests) {
                sendEsitoIntRequest(req, x4EsitoIntApiService);
            }
        }
        log.info("Completed send storno - Total record: {} - Error: {} - Success {} - Duration: {} seconds.", size, error, success, (System.currentTimeMillis() - startTime) / 1000);
    }

    private boolean isTokenExpired(JwtTokenResponse token) {
        if (Objects.isNull(token) || Objects.isNull(token.getAccessToken()) || Objects.isNull(token.getExpiresIn())) {
            return true;
        }
        try {
            String[] parts = token.getAccessToken().split("\\.");
            if (parts.length < 2) return true;

            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            JsonNode claims = new ObjectMapper().readTree(payload);

            JsonNode expNode = claims.get("exp");
            if (Objects.isNull(expNode)) {
                log.warn("JWT does not contain 'exp' claim, fallback to expires_in");
                return true;
            }

            long exp = expNode.asLong();
            long now = Instant.now().getEpochSecond();
            log.debug("JWT exp: {} - now: {} - diff: {}s - expired: {}", exp, now, exp - now, now >= exp);
            return now >= exp;
        } catch (Exception e) {
            log.warn("Unable to decode JWT token: {}", e.getMessage());
            return true;
        }
    }

}
