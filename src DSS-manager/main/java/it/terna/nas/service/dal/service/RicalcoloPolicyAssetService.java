package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.dal.entity.eam.X4RicalcoloPolicyAsset;
import it.terna.nas.service.dal.service.repository.X4DssWorkedHistoryRepository;
import it.terna.nas.service.dal.service.repository.X4RicalcoloPolicyAssetRepository;
import it.terna.nas.service.material.utility.Constants;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RicalcoloPolicyAssetService
{
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private X4DssWorkedHistoryRepository x4DssWorkedHistoryRepository;
    
    @Autowired
    private X4RicalcoloPolicyAssetRepository x4RicalcoloPolicyAssetRepository;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public void closeElaboration(Long id)
    {
        long startTime = System.currentTimeMillis();
        log.debug("call closeElaboration({}) |{}|", id, startTime);
        
        Optional<X4RicalcoloPolicyAsset> rowOp = x4RicalcoloPolicyAssetRepository.findById(id);
        if (rowOp.isPresent())
        {        
            X4RicalcoloPolicyAsset row = rowOp.get();
            row.setElaborato("S");
            x4RicalcoloPolicyAssetRepository.save(row);
        }
        
        long endTime = System.currentTimeMillis();
        log.info("end closeElaboration({}) |{}|", id, endTime - startTime);
    }
    
    
    private List<X4RicalcoloPolicyAsset> getElements(Stream<X4RicalcoloPolicyAsset> ricalcoloPolicy)
    {
        List<X4RicalcoloPolicyAsset> result = new ArrayList<>();
        try (Stream<X4RicalcoloPolicyAsset> stream = ricalcoloPolicy;) 
        {
            Iterator<X4RicalcoloPolicyAsset> iterator = ricalcoloPolicy.iterator();
            while (iterator.hasNext()) 
            {
                X4RicalcoloPolicyAsset item = iterator.next();
                entityManager.detach(item);
                result.add(item);
            }
        }
        
        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public String getCorrelationHistory(Long idHistory) 
    {
        String correlation = null;
        long startTime = System.currentTimeMillis();
        log.debug("call getHistory({}) |{}|", idHistory, startTime);
        Optional<X4DssWorkedHistory> row = x4DssWorkedHistoryRepository.findById(idHistory);
        if (row.isPresent())
            correlation = row.get().getCorrelationId();
        long endTime = System.currentTimeMillis();
        log.info("end getHistory({}) ==> {} |{}|", idHistory, correlation, endTime - startTime);
        return correlation;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public List<X4RicalcoloPolicyAsset> getPolicyAndAssetAndHistoryElaborate() 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyAndAssetAndHistoryElaborate() |{}|", startTime);

        List<X4RicalcoloPolicyAsset> result = getElements(x4RicalcoloPolicyAssetRepository.findRicalcoloByPolicyAndAssetAndHistory());
        
        long endTime = System.currentTimeMillis();
        log.info("end getPolicyAndAssetAndHistoryElaborate() |{}|", endTime - startTime);
        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public List<X4RicalcoloPolicyAsset> getPolicyAndAssetElaborate() 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyAndAssetElaborate() |{}|", startTime);
        
        List<X4RicalcoloPolicyAsset> result = getElements(x4RicalcoloPolicyAssetRepository.findRicalcoloByPolicyAndAsset());
        
        long endTime = System.currentTimeMillis();
        log.info("end getPolicyAndAssetElaborate() |{}|", endTime - startTime);
        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public Map<Long, Long> getPolicyElaborate() 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyElaborate() |{}|", startTime);
        Map<Long, Long> result = new HashMap<>();
        try (Stream<X4RicalcoloPolicyAsset> ricalcoloPolicy = x4RicalcoloPolicyAssetRepository.findRicalcoloByPolicy();) 
        {
            Iterator<X4RicalcoloPolicyAsset> iterator = ricalcoloPolicy.iterator();
            while (iterator.hasNext()) 
            {
                X4RicalcoloPolicyAsset item = iterator.next();
                entityManager.detach(item);
                result.put(item.getId(), item.getX4PolicyId());
            }
        }
        
        long endTime = System.currentTimeMillis();
        log.info("end getPolicyElaborate() |{}|", endTime - startTime);
        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public void resetHistory(Long idHistory) 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call resetHistory({}) |{}|", idHistory, startTime);
        Optional<X4DssWorkedHistory> rowOp = x4DssWorkedHistoryRepository.findById(idHistory);
        if (rowOp.isPresent())
        {
            X4DssWorkedHistory row = rowOp.get();
            row.setChangeBy(Constants.BON_DSS);
            row.setChangeDate(new Date());
            row.setCode(null);
            row.setDescription(null);
            row.setShortDescription(null);
            row.setErrorFlag(null);
            row.setStatus(Constants.OPEN_STATUS);
            row.setOrderNum(null);
            x4DssWorkedHistoryRepository.saveAndFlush(row);
        }
        
        long endTime = System.currentTimeMillis();
        log.info("end resetHistory({}) |{}|", idHistory, endTime - startTime);
    }
}
