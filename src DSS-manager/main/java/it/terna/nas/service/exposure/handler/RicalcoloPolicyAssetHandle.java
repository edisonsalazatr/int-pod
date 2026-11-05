package it.terna.nas.service.exposure.handler;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import static it.terna.nas.service.material.utility.Constants.*;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Common.EOperation;
import it.terna.kernel.broker.client.KafkaProducerHandler;
import it.terna.kernel.broker.configuration.BrokerEntityConfiguration;
import it.terna.kernel.broker.configuration.data.BrokerEntityInfo;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.entity.eam.X4RicalcoloPolicyAsset;
import it.terna.nas.service.dal.service.CacheService;
import it.terna.nas.service.dal.service.ClassificationQueryService;
import it.terna.nas.service.dal.service.PolicyQueryService;
import it.terna.nas.service.dal.service.RicalcoloPolicyAssetService;
import it.terna.nas.service.dal.service.X4DssPolicyRunnerQueryService;
import it.terna.nas.service.dal.service.X4DssWorkedHistoryQueryService;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.policy.EventDTO;
import it.terna.nas.service.material.utility.Catalog.CatalogTipologyType;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.Catalog.PolicyType;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RicalcoloPolicyAssetHandle
{
    @Autowired
    private BrokerEntityConfiguration brokerEntities;
    
    @Autowired
    private CacheService cacheService;
    
    @Autowired
    private ClassificationQueryService classificationQueryService;
    
    @Autowired
    private KafkaProducerHandler kafkaHandler;
    
    @Autowired
    private PolicyQueryService policyQueryService;
    
    @Autowired
    private RicalcoloPolicyAssetService ricalcoloPolicyAssetService;
    
    @Autowired
    private X4DssPolicyRunnerQueryService x4DssPolicyRunnerQueryService;
    
    @Autowired
    private X4DssWorkedHistoryQueryService x4DssWorkedHistoryQueryService;
    
    @Async
    public void elabora()
    {
        elaboratePolicy();
        elaboratePolicyAndAsset();
        elaboratePolicyAndAssetAndHistory();
    }
    
    
    private void elaboratePolicy()
    {
        long startTime = System.currentTimeMillis();
        log.debug("call elaboratePolicy() |{}|", startTime);
        
        Map<Long, Long> policyToElab = ricalcoloPolicyAssetService.getPolicyElaborate();
        if (isNotEmpty(policyToElab))
        {
            BrokerEntityInfo topicKafka = brokerEntities.getEndPoints().get(Constants.END_POINT_MANAGER_EVENTS_HANDLER);
            for(Entry<Long, Long> row: policyToElab.entrySet())
            {
                Long pkId           = row.getKey();
                Long x4policyId     = row.getValue();
                X4Policy x4policy   = null;
                try {
                    x4policy = policyQueryService.getPolicy(x4policyId.toString());
                    if (isNull(x4policy))
                    {   
                        log.warn("Not find policy with id {}", x4policyId);
                        continue;
                    }
                    
                    log.info("Policy: {}, Id: {}", x4policy.getIdPolicy(), x4policy.getX4PolicyId());
                    long time = (new Date()).getTime();
                    EventDTO newEvent = new EventDTO();
                    newEvent.setEvent(EventActionType.MODIFY.getValue());
                    newEvent.setPolicy(x4policy.getX4PolicyId().toString());
                    newEvent.setTipology(x4policy.getTipoPolicy());
                    newEvent.setType(CatalogTipologyType.POLICY.getValue());
                    newEvent.setKafkaKey(x4policy.getX4PolicyId().toString() + Constants.SEP4 + time);
                    kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(newEvent));
                
                } catch (DataLayerException e) {
                    log.error(e.getMessage(), e);
                }
                
                ricalcoloPolicyAssetService.closeElaboration(pkId);
            }
        }
            
        long endTime = System.currentTimeMillis();
        log.info("end elaboratePolicy() |{}|", endTime - startTime);
    }
    
    
    
    private void elaboratePolicyAndAsset()
    {
        long startTime = System.currentTimeMillis();
        log.debug("call elaboratePolicyAndAsset() |{}|", startTime);
        
        List<X4RicalcoloPolicyAsset> policyToElab = ricalcoloPolicyAssetService.getPolicyAndAssetElaborate();
        if (isNotEmpty(policyToElab))
        {
            BrokerEntityInfo periodica      = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY);
            BrokerEntityInfo oncondition    = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);
            for(X4RicalcoloPolicyAsset ricalcolo: policyToElab)
            {
                cacheService.evictCache(CACHE_ASSET, ricalcolo.getAssetNum(), CatalogTipologyType.ASSET);
                cacheService.evictCache(CACHE_ASSET, ricalcolo.getAssetNum() + SLIM_VERSION, CatalogTipologyType.ASSET);
                
                try {
                    X4Policy x4policy = policyQueryService.getPolicy(ricalcolo.getX4PolicyId().toString());
                    if (isNull(x4policy))
                    {   
                        log.warn("Not find policy with id {}", ricalcolo.getX4PolicyId());
                        continue;
                    }
                    
                    log.info("Policy: {}, Id: {}, AssetNum: {}", x4policy.getIdPolicy(), x4policy.getX4PolicyId(), ricalcolo.getAssetNum());
                    
                    String eventType                            = EventActionType.MODIFY.getValue();
                    List<ClassStructureTipology> typeJpnumList  = classificationQueryService.getClassStructureTipologyByMacroAttivita(x4policy.getIdMacroAttivita());
                    ClassStructureTipology classSostit          = typeJpnumList.stream()
                                                                               .filter(x -> CLASS_SOSTITUZIONE.equals(x.getClassificationId()))
                                                                               .findFirst()
                                                                               .orElse(null);
    
                    if (notNull(classSostit))
                        eventType = classSostit.getClassificationId();
                    else if (notNull(x4policy.getLogicaTronco()) && x4policy.getLogicaTronco() == 1)
                        eventType = CLASS_TRONCO_ISPEZIONE;
                    
                    Date currentDate        = new Date();
                    String correlationKey   = x4policy.getX4PolicyId() + SEP4 + currentDate.getTime();
                    String correlationKey2  = correlationKey + SEP4 + 1;
                    
                    X4DssPolicyRunner row = new X4DssPolicyRunner();
                    row.setIdMacroAttivita(x4policy.getIdMacroAttivita());
                    row.setX4PolicyId(x4policy.getX4PolicyId());
                    row.setIdPolicy(x4policy.getIdPolicy());
                    row.setCorrelationId(correlationKey);
                    row.setDataElaborazione(new Date());
                    row.setHasLd(0L);
                    row = x4DssPolicyRunnerQueryService.saveOrUpdateRequiresNew(row);
                    
                    X4DssWorkedHistory rowHistory = new X4DssWorkedHistory();
                    rowHistory.setCreatedBy(BON_DSS);
                    rowHistory.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));
                    rowHistory.setPolicyId(x4policy.getX4PolicyId().toString());
                    rowHistory.setCorrelationId(correlationKey2);
                    rowHistory.setStatus(OPEN_STATUS);
                    rowHistory.setTotalRelatedMessage(1);
                    rowHistory.setProgressiveRelatedMessage(1);
                    rowHistory.setAssetNum(ricalcolo.getAssetNum());
                    rowHistory.setHasLd(0);
                    rowHistory.setRecoveryCount(1);
                    rowHistory.setX4DssPolicyRunnerId(row.getX4DssPolicyRunnerId());
                    rowHistory = x4DssWorkedHistoryQueryService.saveOrUpdate(rowHistory);
    
                    WorkedPolicyRequestEvent event     = new WorkedPolicyRequestEvent();
                    event.setPolicy(x4policy.getX4PolicyId());
                    event.setEvent(eventType);
                    event.setCorrelationId(correlationKey2);
                    event.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(ricalcolo.getAssetNum()));
                    event.setAsset(ricalcolo.getAssetNum());
                    event.setDssWorkerId(rowHistory.getIdDssWorked().toString());
                    event.setNumberMessagePolicy(1L);
                    event.setTipology(x4policy.getTipoPolicy());
                    event.setType(EventType.POLICY.getValue());
                    
                    BrokerEntityInfo topicKafka = oncondition;
                    if (PolicyType.PERIODICA.getCode().equals(x4policy.getTipoPolicy()))
                        topicKafka = periodica;
                    
                    kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
                } catch(DataLayerException q) {
                    log.error(q.getMessage(), q);
                }
                
                ricalcoloPolicyAssetService.closeElaboration(ricalcolo.getId());
            }
        }
            
        long endTime = System.currentTimeMillis();
        log.info("end elaboratePolicyAndAsset() |{}|", endTime - startTime);
    }


    private void elaboratePolicyAndAssetAndHistory()
    {
        long startTime = System.currentTimeMillis();
        log.debug("call elaboratePolicyAndAsset() |{}|", startTime);
        
        List<X4RicalcoloPolicyAsset> policyToElab = ricalcoloPolicyAssetService.getPolicyAndAssetAndHistoryElaborate();
        if (isNotEmpty(policyToElab))
        {
            BrokerEntityInfo periodica      = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY);
            BrokerEntityInfo oncondition    = brokerEntities.getEndPoints().get(END_POINT_EVALUATE_WORKER_POLICY);
            for(X4RicalcoloPolicyAsset ricalcolo: policyToElab)
            {
                cacheService.evictCache(CACHE_ASSET, ricalcolo.getAssetNum(), CatalogTipologyType.ASSET);
                cacheService.evictCache(CACHE_ASSET, ricalcolo.getAssetNum() + SLIM_VERSION, CatalogTipologyType.ASSET);
                
                try {
                    X4Policy x4policy = policyQueryService.getPolicy(ricalcolo.getX4PolicyId().toString());
                    if (isNull(x4policy))
                    {   
                        log.warn("Not find policy with id {}", ricalcolo.getX4PolicyId());
                        continue;
                    }
                    
                    log.info("Policy: {}, Id: {}, AssetNum: {}", x4policy.getIdPolicy(), x4policy.getX4PolicyId(), ricalcolo.getAssetNum());

                    String eventType                            = EventActionType.MODIFY.getValue();
                    List<ClassStructureTipology> typeJpnumList  = classificationQueryService.getClassStructureTipologyByMacroAttivita(x4policy.getIdMacroAttivita());
                    ClassStructureTipology classSostit          = typeJpnumList.stream()
                                                                               .filter(x -> CLASS_SOSTITUZIONE.equals(x.getClassificationId()))
                                                                               .findFirst()
                                                                               .orElse(null);
    
                    if (notNull(classSostit))
                        eventType = classSostit.getClassificationId();
                    else if (notNull(x4policy.getLogicaTronco()) && x4policy.getLogicaTronco() == 1)
                        eventType = CLASS_TRONCO_ISPEZIONE;
                    
                    String correlationId = ricalcoloPolicyAssetService.getCorrelationHistory(ricalcolo.getX4DssWorkedHistoryId());
                    if (isNotEmpty(correlationId))
                    {
                        ricalcoloPolicyAssetService.resetHistory(ricalcolo.getX4DssWorkedHistoryId());
                        WorkedPolicyRequestEvent event = new WorkedPolicyRequestEvent();
                        event.setPolicy(x4policy.getX4PolicyId());
                        event.setEvent(eventType);
                        event.setCorrelationId(correlationId);
                        event.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(ricalcolo.getAssetNum()));
                        event.setAsset(ricalcolo.getAssetNum());
                        event.setDssWorkerId(ricalcolo.getX4DssWorkedHistoryId().toString());
                        event.setNumberMessagePolicy(1L);
                        event.setTipology(x4policy.getTipoPolicy());
                        event.setType(EventType.POLICY.getValue());
                        
                        BrokerEntityInfo topicKafka = oncondition;
                        if (PolicyType.PERIODICA.getCode().equals(x4policy.getTipoPolicy()))
                            topicKafka = periodica;
                        
                        kafkaHandler.shoot(EOperation.UPDATE, topicKafka, List.of(event));
                    } else {
                        log.error("Impossibile determinare riga X4_DSS_WORKED_HISTORY con pkid {}", ricalcolo.getX4DssWorkedHistoryId());
                    }
                    
                } catch(DataLayerException q) {
                    log.error(q.getMessage(), q);
                }
                
                ricalcoloPolicyAssetService.closeElaboration(ricalcolo.getId());
            }
        }
            
        long endTime = System.currentTimeMillis();
        log.info("end elaboratePolicyAndAsset() |{}|", endTime - startTime);
    }
}
