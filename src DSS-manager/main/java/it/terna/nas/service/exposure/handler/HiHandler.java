package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.Ahscore;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.AhscoreQueryService;
import it.terna.nas.service.dal.service.AssetQueryService;
import it.terna.nas.service.dal.service.CacheService;
import it.terna.nas.service.dal.service.ClassificationQueryService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.dal.service.X4DssWorkedHistoryQueryService;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.Shoot;
import it.terna.nas.service.material.dto.hi.HiDTO;
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
public class HiHandler extends AbstractHandler
{
    private final AhscoreQueryService ahscoreQueryService;
    private final AssetQueryService assetQueryService;
    private final CacheService cacheService;   
    private final ClassificationQueryService classificationQueryService;
    private final PolicyQueryService policyQueryService;
    private final X4DssPolicyRunnerQueryService x4DssPolicyRunnerQueryService;
    private final X4DssWorkedHistoryQueryService x4DssWorkedHistoryQueryService;

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void handleEvent(EventDTO request) 
            throws EAMRetryException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call handleHiEvent({}, {})  |{}|", request.getAttributes(), request.getAsset(), startTime);
        Shoot<WorkedPolicyRequestEvent> result = transactionBlocks.requiresNew(() -> {        
            return manageHi(request);
        });           
        shootMessages(result);
        
        long endTime = System.currentTimeMillis();
        log.debug("end handleHiEvent({}, {})  |{}|", request.getAttributes(), request.getAsset(), endTime - startTime);
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    private Shoot<WorkedPolicyRequestEvent> manageHi(EventDTO request) 
            throws EAMRetryException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call manageHi({}, {})  |{}|", request.getAttributes(), request.getAsset(), startTime);
        Shoot<WorkedPolicyRequestEvent> results = new Shoot<>();
        
        try {
            Map<String, String> attributes  = request.getAttributes();
            String hiKeyId                  = attributes.get(EventAttributes.HIATTRIBUTENAME.getValue());
            if (notNull(hiKeyId) && notNull(request.getAsset()))
            {
                Asset asset = assetQueryService.getAssetByAssetnum(request.getAsset());
                if (notNull(asset))
                {
                    List<X4Policy> policies = policyQueryService.getPolicyByClassStructureIdAndHi(asset.getClassStructureId(), 
                    																			  hiKeyId);
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
                                row.setTipoEvento(EventType.HI.getValue());
                                row.setTopic(KafkaUtility.getTopicName(DssKafka.HI_ASSET, brokerEntities.getEndPoints()));
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
        
        long endTime = System.currentTimeMillis();
        log.debug("end manageHi({}, {})  |{}|", request.getAttributes(), request.getAsset(), endTime - startTime);
		return results;
    }


    public void sendCreateHi(HiDTO request) 
            throws EAMRetryException, DataLayerException
    {
        log.debug("call sendCreateHi({}, {}, {}, {}, {})", request.getAhscoreid(), request.getOwnerrecordid(), request.getPreviousvalue(), request.getValue(), request.getAttributename());
                
        if (notNull(request.getValue()) && (!request.getValue().equals(request.getPreviousvalue())) && notNull(request.getOwnerrecordid()) && notNull(request.getAttributename()))
        {
            validateAhscoreValue(request);

            Asset asset = assetQueryService.getAssetByAssetId(request.getOwnerrecordid().toString());
            if (isNull(asset))
                return;
            
            cacheService.evictCache(CACHE_ASSET, asset.getAssetNum(), CatalogTipologyType.ASSET);
            cacheService.evictCache(CACHE_ASSET, asset.getAssetNum() + SLIM_VERSION, CatalogTipologyType.ASSET);
            
            Date currentDate = new Date();
            String key = EventType.HI.getValue() + SEP4 + request.getAhscoreid() + SEP4 + currentDate.getTime();

            EventDTO event = new EventDTO();
            event.setType(EventType.HI.getValue());
            event.setAsset(asset.getAssetNum());
            event.setEvent(EventActionType.MODIFY.getValue());
            event.setKafkaKey(key);
            event.setAttributes(new HashMap<>());
            event.getAttributes().put(EventAttributes.HIATTRIBUTENAME.getValue(), request.getAttributename());

            log.debug("sendCreateHi ==> {}", event);
            
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(END_POINT_MANAGER_EVENTS_HANDLER);
            
            kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
        }
    }


    private void validateAhscoreValue(HiDTO request) 
        throws EAMRetryException 
    {
        if (!notNull(request) || !notNull(request.getAhscoreid())) 
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Message doesn't carry a value for ahscoreid");
        
        if (!notNull(request.getValue())) 
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Message doesn't carry a value for value");
        
        Optional<Ahscore> ahscoreOpt = ahscoreQueryService.getAhscoreById(request.getAhscoreid());
        if (ahscoreOpt.isEmpty())
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "AHSCORE not found for ahscoreid " + request.getAhscoreid());
        
        BigDecimal inputValue;
        try {
            inputValue = new BigDecimal(request.getValue());
        } catch (Exception e) {
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "Invalid value for ahscoreid " + request.getAhscoreid() + ": " + request.getValue(), e);
        }

        Ahscore ahscore = ahscoreOpt.get();
        BigDecimal dbValue = ahscore.getValue();
        if (!notNull(dbValue) || dbValue.compareTo(inputValue) != 0)
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), "AHSCORE value mismatch for ahscoreid " + request.getAhscoreid() + ": expected " + dbValue + " but received " + inputValue);
    }
}
