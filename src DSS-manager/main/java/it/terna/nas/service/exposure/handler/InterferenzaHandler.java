package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.dal.entity.eam.X4InterfStat;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.AssetQueryService;
import it.terna.nas.service.dal.service.CacheService;
import it.terna.nas.service.dal.service.ClassificationQueryService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.dal.service.X4DssWorkedHistoryQueryService;
import it.terna.nas.service.dal.service.X4InterStatQueryService;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.Shoot;
import it.terna.nas.service.material.dto.interferenza.InterferenzaDTO;
import it.terna.nas.service.material.dto.interferenza.InterferenzaStatDTO;
import it.terna.nas.service.material.dto.policy.EventDTO;
import it.terna.nas.service.material.utility.Catalog.CatalogTipologyType;
import it.terna.nas.service.material.utility.Catalog.DssKafka;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Catalog.EventAttributes;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.KafkaUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InterferenzaHandler extends AbstractHandler
{
    private final CacheService cacheService;
    private final ClassificationQueryService classificationQueryService;
    private final PolicyQueryService policyQueryService;
    private final X4DssPolicyRunnerQueryService x4DssPolicyRunnerQueryService;
    private final X4DssWorkedHistoryQueryService x4DssWorkedHistoryQueryService;
    private final X4InterStatQueryService x4InterStatQueryService;
    private final AssetQueryService assetQueryService;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void manageInterferenza(EventDTO request) 
            throws EAMRetryException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call manageInterferenza({}, {})  |{}|", request.getAttributes(), request.getAsset(), startTime);
        
        Map<String, String> attributes = request.getAttributes();
        if (Assorted.isNotEmpty(attributes))
        {
            String x4difforstatid   = attributes.get(EventAttributes.X4INTERFSTATID.getValue());
            String x4catdifforid    = attributes.get(EventAttributes.X4CATINTERFID.getValue());
            Shoot<WorkedPolicyRequestEvent> result = transactionBlocks.requiresNew(() -> {
            if (notNull(x4difforstatid))
                return manageInterferenzaForAsset(request);
            else if (notNull(x4catdifforid))
                manageInterferenzaChange(request);
                
                return null;
            });           
            shootMessages(result);
        }
        
        long endTime = System.currentTimeMillis();
        log.debug("end manageInterferenza({}, {})  |{}|", request.getAttributes(), request.getAsset(), endTime - startTime);
    }

    
    private void manageInterferenzaChange(EventDTO request)
        throws EAMRetryException
    {
        try {
            Map<String, String> attributes  = request.getAttributes();
            String x4CatInterId             = attributes.get(EventAttributes.X4CATINTERFID.getValue());
            List<X4Policy> policies         = policyQueryService.getPolicyByInput(EventType.INTERFERENZA.getValue(), x4CatInterId);
            List<X4InterfStat> listInter    = x4InterStatQueryService.getX4InterStatByIdInterferenza(x4CatInterId);
            
            if (isNotEmpty(policies))
            {
                for (X4Policy policy: policies)
                    cacheService.evictCache(CACHE_POLICY, String.valueOf(policy.getX4PolicyId()), CatalogTipologyType.POLICY);
            }
            
            if (isNotEmpty(listInter))
            {
                for (X4InterfStat interStat: listInter)
                {    
                    cacheService.evictCache(CACHE_ASSET, interStat.getAssetnum(), CatalogTipologyType.ASSET);
                    cacheService.evictCache(CACHE_ASSET, interStat.getAssetnum() + SLIM_VERSION, CatalogTipologyType.ASSET);
                }
            }
            
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
        }
    }


    private Shoot<WorkedPolicyRequestEvent> manageInterferenzaForAsset(EventDTO request) 
            throws EAMRetryException 
    {
        Shoot<WorkedPolicyRequestEvent> results = new Shoot<>();
        try {
            Map<String, String> attributes  = request.getAttributes();
            String x4interfstatid           = attributes.get(EventAttributes.X4INTERFSTATID.getValue());
            X4InterfStat x4InterStat        = x4InterStatQueryService.getX4InterStatById(x4interfstatid);
            if (notNull(x4InterStat) && notNull(x4InterStat.getIdinterferenza()) && notNull(x4InterStat.getAssetnum()))
            {
                Asset asset = assetQueryService.getAssetByAssetnum(request.getAsset());
                if (notNull(asset))
                {
                    List<X4Policy> policies = policyQueryService.getPolicyByClassStructureIdAndInterferenza(asset.getClassStructureId(), 
                    																						x4InterStat.getIdinterferenza());
                    if (isNotEmpty(policies))
                    {
                        BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);
                        results.setEntity(topicKafka);
                        for (X4Policy policy: policies)
                        {
                            String eventType                            = EventActionType.MODIFY.getValue();
                            List<ClassStructureTipology> typeJpnumList  = classificationQueryService.getClassStructureTipologyByMacroAttivita(policy.getIdMacroAttivita());
                            ClassStructureTipology classSostit          = typeJpnumList.stream()
                                                                                       .filter(x -> CLASS_SOSTITUZIONE.equals(x.getClassificationId()))
                                                                                       .findFirst()
                                                                                       .orElse(null);
                                                    
                            if (notNull(classSostit))
                                eventType = classSostit.getClassificationId();
                            else if (notNull(policy.getLogicaTronco()) && policy.getLogicaTronco() == 1)
                                eventType = CLASS_TRONCO_ISPEZIONE;

                            String policyStr        = policy.getX4PolicyId().toString();
                            String correlationKey   = policyStr + SEP4 + KafkaUtility.getTimeStamp(request.getKafkaKey());
                            String correlationKey2  = correlationKey + SEP4 + 1;
                            X4DssWorkedHistory rowHistory = x4DssWorkedHistoryQueryService.getDssWorkedHistoryByCorrelationAndPolicy(correlationKey2, policyStr);
                              
                            if (Assorted.isNull(rowHistory))
                            {                
                                X4DssPolicyRunner row = new X4DssPolicyRunner();
                                row.setIdMacroAttivita(policy.getIdMacroAttivita());
                                row.setX4PolicyId(policy.getX4PolicyId());
                                row.setIdPolicy(policy.getIdPolicy());
                                row.setCorrelationId(correlationKey);
                                row.setDataElaborazione(new Date());
                                row.setAssetnum(request.getAsset());
                                row.setNumElaborati(1L);
                                row.setTipoEvento(EventType.INTERFERENZA.getValue());
                                row.setTopic(KafkaUtility.getTopicName(DssKafka.INTERFERENZA_ASSET, brokerEntities.getEndPoints()));
                                row.setHasLd(0L);
                                row = x4DssPolicyRunnerQueryService.saveOrUpdate(row);
                                
                                rowHistory = new X4DssWorkedHistory();
                                rowHistory.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));
                                rowHistory.setPolicyId(policyStr);
                                rowHistory.setCorrelationId(correlationKey2);
                                rowHistory.setStatus(OPEN_STATUS);
                                rowHistory.setTotalRelatedMessage(1);
                                rowHistory.setProgressiveRelatedMessage(1);
                                rowHistory.setAssetNum(request.getAsset());
                                rowHistory.setHasLd(0);
                                rowHistory.setRecoveryCount(1);
                                rowHistory.setX4DssPolicyRunnerId(row.getX4DssPolicyRunnerId());
                                rowHistory = x4DssWorkedHistoryQueryService.saveOrUpdate(rowHistory);
                              
                                WorkedPolicyRequestEvent event     = new WorkedPolicyRequestEvent();
                                event.setPolicy(policy.getX4PolicyId());
                                event.setEvent(eventType);
                                event.setCorrelationId(correlationKey2);
                                event.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(request.getAsset()));
                                event.setAsset(request.getAsset());
                                event.setDssWorkerId(rowHistory.getIdDssWorked().toString());
                                event.setNumberMessagePolicy(1L);
                                event.setTipology(policy.getTipoPolicy());
                                event.setType(EventType.POLICY.getValue());
                                results.getItems().add(event);
                            }
                        }
                    }
                }     
            }
            
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
        }
        return results;
    }


    public void sendCreateInterferenza(InterferenzaDTO request)
    {
        log.debug("call sendCreateInterferenza({}, {})", request.getX4catinterfid(), request.getStatus());
        
        if (ENTITY_ATTIVO_STATUS.equals(request.getStatus()) && notNull(request.getIdinterferenza()))
        {
            Date currentDate = new Date();
            String key = EventType.INTERFERENZA.getValue() + SEP4 + request.getX4catinterfid() + SEP4 + currentDate.getTime();

            EventDTO event = new EventDTO();
            event.setType(EventType.INTERFERENZA.getValue());
            event.setEvent(EventActionType.MODIFY.getValue());
            event.setKafkaKey(key);
            event.setAttributes(new HashMap<>());
            event.getAttributes().put(EventAttributes.X4CATINTERFID.getValue(), request.getX4catinterfid().toString());

            log.debug("sendCreateInterferenza ==> {}", event);
            
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
            
            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
        }    
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void sendCreateInterferenzaForAsset(InterferenzaStatDTO request) 
        throws DataLayerException
    {
        log.debug("call sendCreateInterferenzaForAsset({}, {}, {})", request.getX4interfstatid(), request.getStatus(), request.getAssetnum());

        cacheService.evictCache(CACHE_ASSET, request.getAssetnum(), CatalogTipologyType.ASSET);
        cacheService.evictCache(CACHE_ASSET, request.getAssetnum() + SLIM_VERSION, CatalogTipologyType.ASSET);
        if (ENTITY_VALIDATA_STATUS.equals(request.getStatus()) && isNotEmpty(request.getAssetnum()) && notNull(request.getIdinterferenza()))
        {
            X4InterfStat x4InterfStat = x4InterStatQueryService.getX4InterStatById(request.getX4interfstatid().toString());            
            if (!notNull(x4InterfStat) || !ENTITY_VALIDATA_STATUS.equals(x4InterfStat.getStatus()))
            {
                log.warn("sendCreateInterferenzaForAsset: x4interfstat {} not found or not VALIDATA in DB, retry", request.getX4interfstatid());
                throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "X4InterfStat not found or not VALIDATA for id: " + request.getX4interfstatid());
            }

            Date currentDate = new Date();
            String key = EventType.INTERFERENZA.getValue() + SEP4 + request.getX4interfstatid() + SEP4 + currentDate.getTime();

            EventDTO event = new EventDTO();
            event.setType(EventType.INTERFERENZA.getValue());
            event.setAsset(request.getAssetnum());
            event.setEvent(EventActionType.MODIFY.getValue());
            event.setKafkaKey(key);
            event.setAttributes(new HashMap<>());
            event.getAttributes().put(EventAttributes.X4INTERFSTATID.getValue(), request.getX4interfstatid().toString());

            log.debug("sendCreateInterferenzaForAsset ==> {}", event);
            
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
            
            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
        }
    }
}
