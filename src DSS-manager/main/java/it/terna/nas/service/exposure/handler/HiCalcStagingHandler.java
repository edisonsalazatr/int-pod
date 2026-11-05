package it.terna.nas.service.exposure.handler;

import java.util.Date;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.X4HiCalcStaging;
import it.terna.nas.service.dal.service.repository.X4HiCalcStagingRepository;
import it.terna.nas.service.exceptions.EAMRetryException;
import it.terna.nas.service.material.dto.hi.HiCalcDTO;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class HiCalcStagingHandler
{
    @Autowired
    private EntityManager entityManager;
    
    @Value("${hiCheckAssetInMin:10}")
    private Integer hiCheckAssetInMin;
    
    @Autowired
    private X4HiCalcStagingRepository x4HiCalcStagingRepository;
    
    @Transactional
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void saveHiCalcStaging(HiCalcDTO request) 
            throws EAMRetryException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call saveHiCalcStaging({}) |{}|", request, startTime);
        
        try {
            Integer counterHiCheckAsset = x4HiCalcStagingRepository.counterHiElabByAsset(request.getSiteId(), request.getAssetNum(), hiCheckAssetInMin);
            if (counterHiCheckAsset > 0)
                return;
            
            Integer countHi = x4HiCalcStagingRepository.counterHiCalcByAsset(request.getSiteId(), request.getAssetNum());
            log.debug("countHi found: {}", countHi);
        
            if (countHi < 1)
            {
                X4HiCalcStaging row = new X4HiCalcStaging();
                row.setAssetnum(request.getAssetNum());
                row.setSiteId(request.getSiteId());
                row.setCreateDate(new Date());
                X4HiCalcStaging saved = x4HiCalcStagingRepository.save(row);
                EamEtlUtils.detach(entityManager, Boolean.TRUE, saved);
                log.debug("saveHiCalcStaging ==> {}", saved.getX4HiCalcStagingId());
            }
        
        } catch(Exception e) {
            log.error(e.getMessage(), e);
            throw new EAMRetryException(EAMErrorMapping.EAM_9999.getEamCode(), e.getMessage(), e); 
        }
        
        long endTime = System.currentTimeMillis();
        log.info("end saveHiCalcStaging({}) |{}|", request, endTime - startTime);
    }
}
