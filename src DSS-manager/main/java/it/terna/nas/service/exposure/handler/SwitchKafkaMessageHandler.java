package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.nas.service.material.utility.Constants.*;
import com.google.gson.Gson;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.AssetLocRelation;
import it.terna.nas.service.dal.entity.eam.JobPlan;
import it.terna.nas.service.dal.entity.eam.X4EsitoInt;
import it.terna.nas.service.dal.service.ActivityService;
import it.terna.nas.service.dal.service.AssetQueryService;
import it.terna.nas.service.dal.service.LabTransQueryService;
import it.terna.nas.service.dal.service.WorkOrderQueryService;
import it.terna.nas.service.dal.service.X4EsitoIntQueryService;
import it.terna.nas.service.material.dto.workorder.WorkOrderDTO;
import it.terna.nas.service.material.utility.Catalog.DssKafka;
import it.terna.nas.service.material.utility.Catalog.Esito;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SwitchKafkaMessageHandler
{
    private static final Set<String> SCENARI45_STATUS = Set.of(STATUS_CHIUSO_ODL, STATUS_NON_ESEGUITO_ODL, STATUS_CHIUSO_TEC_ODL, STATUS_NON_VALIDATO_ODL);
    private static final String KEY_OP_GEOCALL              = "OPERATION";
    private static final String INVIO_ODL_GEOCALL           = "INVIO_ODL";
    private static final String ANNULLA_BLOCCO_ODL_GEOCALL  = "ANNULLA_BLOCCO_ODL";
    
    @Autowired
    private ActivityService activityService;
 
    @Autowired
    private AssetQueryService assetQueryService;
    
    @Autowired
    private BrokerEntityConfiguration brokerEntities;
    
    @Value("${disableIntegration: false}")
    private Boolean disableIntegration;
    
    private static final Gson gson = KafkaUtility.createGson();
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private KafkaProducerHandler kafkaHandler;
    
    @Autowired
    private LabTransQueryService labTransQueryService;

    @Autowired
    private WorkOrderQueryService workOrderQueryService;
    
    @Autowired
    private X4EsitoIntQueryService x4EsitoIntQueryService;
    
    @Transactional(readOnly = true)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void handleMessage(String payload) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        WorkOrderDTO request = gson.fromJson(payload, WorkOrderDTO.class);
        log.debug("call handleMessage({}) |{}|", request.getWonum(), startTime);
        
        try {
            validateWorkOrderForForward(request);
            sendMessageDssOdLChange(request);
            sendMessageDssOdLClosed(request);
            
            if (Assorted.isFalse(disableIntegration))
            {
                //sendMessageAgile(request); //TODO MICRUS DA RIVEDERE
                sendMessageChiusuraX4NTK(request);
                //sendMessageChiusuraOdm(request); //TODO MICRUS DA RIVEDERE
                sendMessageElive(request); 
                //sendMessageCavi(request); //TODO MICRUS DA RIVEDERE
                sendMessageCaviDelete(request);
                sendMessageCaviInvio(request);
                sendMessageGeocall(request);
                sendMessageSapStornoTotale(request);
                //sendMessageStornoGeocall(request); //TODO MICRUS DA RIVEDERE
            }            
        } finally {
            entityManager.clear();
            long endTime = System.currentTimeMillis();
            log.info("end handleMessage({}) |{}|", request.getWonum(), endTime - startTime);            
        }        
    }


    private void validateWorkOrderForForward(WorkOrderDTO request)
            throws DataLayerException
    {
        if (isEmpty(request.getWonum()))
            throw new IllegalArgumentException("Input payload does not contain wonum");

        if (isEmpty(request.getChangedate()))
            throw new IllegalArgumentException("Input payload does not contain changedate");

        Date inputChangedate;
        try {
            inputChangedate = Assorted.stringToDate(request.getChangedate(), Constants.MAXIMO_DATE_AND_TIME);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Input payload changedate format is invalid. Expected format: " + Constants.MAXIMO_DATE_AND_TIME, e);
        }

        boolean workOrderFound = workOrderQueryService.existsByWonumAndChangedate(request.getWonum(), inputChangedate);
        if (Assorted.isFalse(workOrderFound))
            throw new IllegalStateException("No WORKORDER found for wonum=" + request.getWonum() + " with changedate >= " + request.getChangedate());
    }
    
    
    /**
     * Send message for X4ODLAGILE
     * 
     * @param w
     * @throws DataLayerException 
     */
    private void sendMessageAgile(WorkOrderDTO w) //TODO MICRUS DA RIVEDERE
            throws DataLayerException
    {
        if ((!STATUS_DA_VALIDARE_ODL.equals(w.getStatus())) && isNotEmpty(w.getJpnum()) && isNotEmpty(w.getTargstartdate()) && isNotEmpty(w.getX4tipospesa()) && isEmpty(w.getParent()) && isEmpty(w.getX4policy()))
        {
            List<JobPlan> activityList = activityService.getActivity(w.getJpnum(), w.getPluscjprevnum());
            if (isEmpty(activityList))
                return;

            JobPlan jobPlan             = activityList.get(0);
            Integer x4Indisponibilita   = jobPlan.getX4Indisponibilita();
            String status               = jobPlan.getStatus();
            if (isNull(x4Indisponibilita) || x4Indisponibilita != 1 || (!ACTIVITY_STATUS_ACTIVE.equals(status))) 
                return;

            List<Asset> assetByAncestor = assetQueryService.getAssetByAncestor(w.getAssetnum(), w.getSiteid());
            if (isEmpty(assetByAncestor) || isEmpty(assetByAncestor.get(0).getX3Ingrp()))
                return;
            
            log.debug("Send Message {} to Agile", w.getWonum());
            
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.AGILE_ODL.getName());
            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));
        }
    }


    /**
     * Send message to X4WOINVIOCAVICHANNEL
     * @param w - workorder
     */
    private void sendMessageCavi(WorkOrderDTO w) //TODO MICRUS DA RIVEDERE
    {
        if (isEmpty(w.getParent()) && Boolean.TRUE.equals(w.getHaschildren()) && STATUS_APERTO_ODL.equals(w.getStatus()))
        {
            log.debug("Send Message {} to Cavi", w.getWonum());
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.CAVI_ODL.getName());
            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));
        }
    }


    private void sendMessageCaviDelete(WorkOrderDTO w)
            throws DataLayerException
    {
        if ((STATUS_NON_ESEGUITO_ODL.equals(w.getStatus()) || STATUS_ANNULLATO_ODL.equals(w.getStatus())) 
                && Boolean.FALSE.equals(w.getIstask())
                && isNotEmpty(w.getX4tronconum()))
        {
            List<String> cavi = workOrderQueryService.getCaviChild(w.getWonum());
            if (isNotEmpty(cavi))
            {
                log.info("Send Message {} to Cavi", w.getWonum());
                BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.CAVI_DELETE_ODL.getName());
                kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));                
            } else {
                
                boolean isCaviCandidate = workOrderQueryService.isCavi45(w.getWonum());
                if (isCaviCandidate)
                {
                    log.info("Send Message {} to Cavi", w.getWonum());
                    BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.CAVI_DELETE_ODL.getName());
                    kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));
                }
            }
        }
    }


    private void sendMessageCaviInvio(WorkOrderDTO w)
            throws DataLayerException
    {
        if (Boolean.FALSE.equals(w.getIstask()) && isNotEmpty(w.getX4tronconum())) 
        {
            if (STATUS_APERTO_ODL.equals(w.getStatus()))
            {               
                List<String> cavi = workOrderQueryService.getCaviChild(w.getWonum());
                if (isNotEmpty(cavi))
                {
                    w.setWorkorder(new ArrayList<>());
                    String uriPrefix = w.getHref().substring(0, w.getHref().lastIndexOf(SEP8));
                    
                    for (String wonum: cavi)
                    {
                        WorkOrderDTO row = new WorkOrderDTO();
                        row.setHref(uriPrefix + SEP8 + SEP1 + wonum);
                        w.getWorkorder().add(row);
                    }
    
                    log.info("Send Message {} to Cavi", w.getWonum());
                    BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.CAVI_ODL.getName());
                    kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));
                    return;
                }
            }  
                
                
            if (!SCENARI45_STATUS.contains(w.getStatus())) 
            {
                boolean isCaviCandidate = workOrderQueryService.isCavi45(w.getWonum());
                
                if (isCaviCandidate)
                {
                    log.info("Send Message {} to Cavi", w.getWonum());
                    BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.CAVI_ODL.getName());
                    kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));
                }
            }
        }
    }


    /**
     * Send message to X4PCCHIUSURAODM
     * @param w - workorder
     */
    private void sendMessageChiusuraOdm(WorkOrderDTO w) //TODO MICRUS DA RIVEDERE
    {
        if (STATUS_CHIUSO_ODL.equals(w.getStatus()) && isNotEmpty(w.getX4odm()))
        {
            log.debug("Send Message {} to SAP for ODM", w.getWonum());
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.CLOSE_ODM_ODL.getName());
            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));
        }
    }


    /**
     * Send message to X4PCCHIUSURANTK
     * @param w - workorder
     */
    private void sendMessageChiusuraX4NTK(WorkOrderDTO w)
    {
        if (STATUS_CHIUSO_ODL.equals(w.getStatus()) && isNotEmpty(w.getX4ntk()) && isNotEmpty(w.getX4noperazione()))
        {
            log.debug("Send Message {} to SAP for X4NTK", w.getWonum());
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.CLOSURE_NTK_ODL.getName());
            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));
        }
    }
    
    /**
     * Send message for X4DSSODLCHANGE to DSS 
     */
    private void sendMessageDssOdLChange(WorkOrderDTO w)
    {
        if (isNotEmpty(w.getAssetnum()) && isNotEmpty(w.getJpnum()))
        {
            log.debug("Send Message {} to DSS", w.getWonum());
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.CREATE_ODL.getName());
            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));
        }
    }
    
    
    /**
     * Send message for X4DSSODLCLOSED to DSS 
     * 
     * @param w - workorder
     */
    private void sendMessageDssOdLClosed(WorkOrderDTO w)
    {
        if (STATUS_CHIUSO_ODL.equals(w.getStatus()))
        {
            log.debug("Send Message {} to DSS with status CHIUSO", w.getWonum());
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.CLOSE_ODL.getName());
            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));
        }
    }
    
    
    /**
     * Send message for X4PUBLISHELIVEODL
     * 
     * @param w
     * @throws DataLayerException 
     */
    private void sendMessageElive(WorkOrderDTO w) 
            throws DataLayerException
    {
        if (STATUS_APERTO_ODL.equals(w.getStatus()))
        {
            List<JobPlan> activityList = activityService.getActivity(w.getJpnum(), w.getPluscjprevnum());
            
            if (isEmpty(activityList))
                return;
            
            JobPlan jobPlan = activityList.get(0);
            Integer x4Elive = jobPlan.getX4Elive();

            //Check se l'attività ha x4elive = 1
            if (isNull(x4Elive) || x4Elive != 1)
                return;
            
            //Check se la chiamata è stata inviata
            List<X4EsitoInt> x4EsitoList = x4EsitoIntQueryService.getX4Esito(w.getWonum(), Esito.OK.name(), ELIVE, SEND_ODL_TO_ELIVE);
            if (isEmpty(x4EsitoList))
            {
                List<AssetLocRelation> assetLocRelation = assetQueryService.getAssetLocRelationByAsset(w.getAssetnum(), w.getSiteid());
                if (isNotEmpty(assetLocRelation))
                {
                    w.setAssetlocrelation(new ArrayList<>());
                    assetLocRelation.stream().forEach(x -> {
                        WorkOrderDTO.AssetLocKafka row = new WorkOrderDTO.AssetLocKafka();
                        row.setX3sapid(x.getX3SapId());
                        w.getAssetlocrelation().add(row);
                    });
                }
                
                log.debug("Send Message {} to Elive", w.getWonum());
                BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.ELIVE_ODL.getName());
                kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));                        
            }
        }
    }

    
    /**
     * Send message for X4INTODLGEOCALL
     * 
     * @param w
     * @throws DataLayerException 
     */
    private void sendMessageGeocall(WorkOrderDTO w)
        throws DataLayerException
    {
        if (STATUS_SCHEDULABILE_ODL.equals(w.getStatus()) && isEmpty(w.getParent()))
        {
            log.debug("Send Create OdL Message {} to Geocall", w.getWonum());
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.GEOCALL_ODL.getName());
            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w), Map.of(KEY_OP_GEOCALL, INVIO_ODL_GEOCALL));
        } else if (STATUS_APERTO_ODL.equals(w.getStatus()) && isEmpty(w.getParent()) && ANNULLA_GEOCALL_ODL.equals(w.getWolo1())) {
            
            boolean isAnnullaBloccoGeocall = workOrderQueryService.isAnnullaBloccoGeocall(w.getWonum());
            if (isAnnullaBloccoGeocall)
            {
                log.debug("Send Annulla Message {} to Geocall", w.getWonum());
                BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.GEOCALL_ODL.getName());
                kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w), Map.of(KEY_OP_GEOCALL, ANNULLA_BLOCCO_ODL_GEOCALL));
            }
        }
    }
    
    
    /**
     * Send message for X4STORNO_T
     * 
     * @param w
     * @throws DataLayerException 
     */
    private void sendMessageSapStornoTotale(WorkOrderDTO w) 
            throws DataLayerException
    {
        if (STORNO_TOTALE.equals(w.getX4stornosap()) && isEmpty(w.getParent())) 
        {
            List<Asset> labTransByWonum = labTransQueryService.getLabTransByWonum(w.getWonum(), w.getSiteid());
            if (isNotEmpty(labTransByWonum))
            {
                log.debug("Send Message {} to Sap for Storno Totale", w.getWonum());
                
                BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(DssKafka.SAP_STORNO_ODL.getName());
                kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(w));
            }
        }
    }
    
    
    /**
     * Send message for X4STORNOGEOCALL
     * 
     * @param w
     * @throws DataLayerException 
     */
    private void sendMessageStornoGeocall(WorkOrderDTO w) //TODO MICRUS DA RIVEDERE
    {
        //TODO MICRUS NON Implementabile
    }
}
