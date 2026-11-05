package it.terna.nas.service.dal.service;

import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNull;
import static it.terna.nas.service.material.utility.Constants.CLASS_CONDUTTORI;
import static it.terna.nas.service.material.utility.Constants.CLASS_FUNI_GUARDIA;
import static it.terna.nas.service.material.utility.Constants.CLASS_TELAIO;
import static it.terna.nas.service.material.utility.Constants.OPEN_STATUS;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.RootAsset;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.repository.X4DssAggregateRepository;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.Shoot;
import it.terna.nas.service.material.dto.worker.MessageStreamCounter;
import it.terna.nas.service.material.utility.Catalog.EventActionType;
import it.terna.nas.service.material.utility.Catalog.EventType;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ElaborationCondFuneTelSupport
{
    @Autowired
    private X4DssAggregateRepository x4DssAggregateRepository;
    
    @Autowired
    private X4DssWorkedHistoryQueryService x4DssWorkedHistoryQueryService;
    

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public Boolean elabCondFune(X4DssPolicyRunner runner, 
                                MessageStreamCounter request, X4Policy policy, 
                                Integer recoveryCount, List<String> linee, 
                                double sogliaElegLine, double kEleg, Long timeStampKeyLineeDoppie, 
                                Shoot<WorkedPolicyRequestEvent> events) 
        throws DataLayerException 
    {
        Boolean esitoElab = Boolean.TRUE;
        
        for (String linea: linee)
        {
            log.trace("elabCondFune {}", linea);
            List<String> lineaCandidateCondFune = x4DssAggregateRepository.findLineaCandidateCondFune(request.getKey(), linea, sogliaElegLine, kEleg);
            if (isNotEmpty(lineaCandidateCondFune))
            {
                WorkedPolicyRequestEvent saved = save(request.getKey(), policy, linea, runner, recoveryCount, timeStampKeyLineeDoppie);
                events.getItems().add(saved);
            }
        }
        
        return esitoElab;
    }
    
    
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public Boolean elabTelaio(X4DssPolicyRunner runner, 
                              MessageStreamCounter request, X4Policy policy, 
                              Integer recoveryCount, List<String> linee, 
                              double sogliaElegLine, double kEleg, 
                              Shoot<WorkedPolicyRequestEvent> events) 
        throws DataLayerException 
    {
        Boolean esitoElab = Boolean.TRUE;
        
        for (String linea: linee)
        {
            log.trace("elabTelaio {}", linea);
            List<String> lineaCandidateTelaio = x4DssAggregateRepository.findLineaCandidateTelaio(request.getKey(), linea, sogliaElegLine, kEleg);
            if (isNotEmpty(lineaCandidateTelaio))
            {
                WorkedPolicyRequestEvent saved = save(request.getKey(), policy, linea, runner, recoveryCount, null);
                events.getItems().add(saved);
            }
        }
        
        return esitoElab;
    }


    private WorkedPolicyRequestEvent save(String correlationKey, X4Policy policy, String asset, X4DssPolicyRunner runner, Integer recoveryCount, Long timeStampKeyLineeDoppie) 
            throws DataLayerException
    {
        String policyStr = policy.getX4PolicyId().toString();
        X4DssWorkedHistory rowHistory = x4DssWorkedHistoryQueryService.getDssWorkedHistoryByCorrelationAndPolicyAndAsset(correlationKey, policyStr, asset);
        if (isNull(rowHistory))
        {
            rowHistory = new X4DssWorkedHistory();
            rowHistory.setCreatedDate(new java.sql.Date(System.currentTimeMillis()));
            rowHistory.setPolicyId(policyStr);
            rowHistory.setCorrelationId(correlationKey);
            rowHistory.setStatus(OPEN_STATUS);
            rowHistory.setTotalRelatedMessage(1);
            rowHistory.setProgressiveRelatedMessage(1);
            rowHistory.setAssetNum(asset);
            rowHistory.setHasLd(0);
            rowHistory.setX4DssPolicyRunnerId(runner.getX4DssPolicyRunnerId());
            rowHistory.setRecoveryCount(recoveryCount);
            rowHistory = x4DssWorkedHistoryQueryService.saveOrUpdateRequiresNew(rowHistory);
        } else {
            rowHistory.setStatus(Constants.OPEN_STATUS);
            rowHistory.setCode(null);
            rowHistory.setDescription(null);
            rowHistory.setErrorFlag(null);
            rowHistory.setShortDescription(null);
            rowHistory = x4DssWorkedHistoryQueryService.saveOrUpdateRequiresNew(rowHistory);            
        }

        String eventType = null;
        if (CLASS_TELAIO.equals(policy.getTipologiaAsset()))
            eventType = EventActionType.AGGREGATE_STRUTTURA_SAVE.getValue();
        else if (CLASS_CONDUTTORI.endsWith(policy.getTipologiaAsset()))
            eventType = EventActionType.AGGREGATE_COND_SAVE.getValue();
        else
            eventType = EventActionType.AGGREGATE_FUNE_SAVE.getValue();
        
        WorkedPolicyRequestEvent newEvent = new WorkedPolicyRequestEvent();
        newEvent.setEvent(eventType);
        newEvent.setPolicy(policy.getX4PolicyId());
        newEvent.setCorrelationId(correlationKey);
        newEvent.setDssWorkerId(rowHistory.getIdDssWorked().toString());
        
        if (CLASS_FUNI_GUARDIA.equals(policy.getTipologiaAsset()))
            newEvent.setNotify(hasDoppiaLineaAggrFuni(asset) 
            		? String.valueOf(timeStampKeyLineeDoppie) + Constants.SEP1 + Constants.DOPPIA_TERNA
            		: asset);        
        else if (CLASS_CONDUTTORI.endsWith(policy.getTipologiaAsset()))
            newEvent.setNotify(asset);
        else
            newEvent.setNotify(x4DssWorkedHistoryQueryService.getDssAssetNum(asset));
        
        newEvent.setAsset(asset);
        newEvent.setNumberMessagePolicy(1L);
        newEvent.setTipology(policy.getTipoPolicy());
        newEvent.setType(EventType.POLICY.getValue());
        return newEvent;
    }
    
    public boolean hasDoppiaLineaAggrFuni(String assetNum) throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call hasDoppiaLineaAggrFuni({}) |{}|", assetNum, startTime);

        List<RootAsset> rootAssets = x4DssWorkedHistoryQueryService.getDoppiaLineaAggrFuni(assetNum);

        if (Assorted.isEmpty(rootAssets))
        {
            return false;
        }

        Set<String> rootAssetSet = new HashSet<>();

        for (RootAsset ra : rootAssets)
        {
            if (ra == null || ra.getRootAsset() == null || ra.getRootAsset().isBlank())
            {
                continue;
            }

            String[] values = ra.getRootAsset().split("#");
            for (String value : values)
            {
                if (value != null && !value.isBlank())
                {
                    rootAssetSet.add(value.trim());

                    // appena troviamo 2 elementi: "doppia linea"
                    if (rootAssetSet.size() > 1)
                    {
                        long endTime = System.currentTimeMillis();
                        log.debug("  - hasDoppiaLineaAggrFuni result [true] |{}|",
                                  endTime - startTime);
                        return true;
                    }
                }
            }
        }

        long endTime = System.currentTimeMillis();
        log.debug("  - hasDoppiaLineaAggrFuni result [false] |{}|",
                  endTime - startTime);

        return false;
    }

   
}
