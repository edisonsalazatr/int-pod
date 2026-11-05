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
import it.terna.nas.service.dal.entity.eam.X4InterfStat;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4InterStatQueryService
{
    private static final String QUERY_01 = "select c from " + X4InterfStat.class.getName() + " c where c.x4interfstatid = :x4interfstatid";
    private static final String QUERY_02 = "select c from " + X4InterfStat.class.getName() + " c where c.idinterferenza = :x4catinterfid and c.assetnum is not null";
    
    @Autowired
    private DataAccessService   dataAccessService;

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public X4InterfStat getX4InterStatById(String x4interfstatid) throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getX4InterStatById({}) |{}|", x4interfstatid, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.X4INTERFSTATID.getName(), x4interfstatid);

        List<X4InterfStat> result = dataAccessService.search(X4InterfStat.class, QUERY_01, parameters, false, true);

        long endTime = System.currentTimeMillis();
        log.debug("  - Search getX4InterStatById found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return Assorted.isNotEmpty(result) ? result.get(0) : null;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4InterfStat> getX4InterStatByIdInterferenza(String idInterferenza) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getX4InterStatByIdInterferenza({}) |{}|", idInterferenza, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.X4CATINTERFID.getName(), idInterferenza);

        List<X4InterfStat> result = dataAccessService.search(X4InterfStat.class, QUERY_02, parameters, false, true);

        long endTime = System.currentTimeMillis();
        log.debug("  - Search getX4DifforStatByIdDifformita found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return result;
    }
}
