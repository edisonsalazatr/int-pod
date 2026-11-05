package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.LabTrans;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LabTransQueryService
{
    private static final String QUERY_01 = "select c from " + LabTrans.class.getName() + " c where c.refWo = :wonum and c.siteId = :source and c.genApprServReceipt = 1";
    
    @Autowired
    private DataAccessService   dataAccessService;

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<Asset> getLabTransByWonum(String wonum, String siteId) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getLabTransByWonum({}, {}) |{}|", wonum, siteId, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
        parameters.put(ESQLMappingParameter.SOURCE.getName(), siteId);
        
        List<Asset> result = dataAccessService.search(Asset.class, QUERY_01, parameters, false, true);

        long endTime = System.currentTimeMillis();
        log.debug("  - Search getLabTransByWonum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return result;
    }
}
