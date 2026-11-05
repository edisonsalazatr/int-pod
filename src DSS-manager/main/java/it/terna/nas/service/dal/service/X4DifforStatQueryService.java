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
import it.terna.nas.service.dal.entity.eam.X4DifforStat;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4DifforStatQueryService
{
    private static final String QUERY_01 = "select c from " + X4DifforStat.class.getName() + " c where c.x4difforstatid = :x4difforstatid";
    private static final String QUERY_02 = "select c from " + X4DifforStat.class.getName() + " c where c.idDifformita = :x4catdifforid and c.assetnum is not null";

    @Autowired
    private DataAccessService   dataAccessService;

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public X4DifforStat getX4DifforStatById(String x4difforstatid) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getX4DifforStatById({}) |{}|", x4difforstatid, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.X4DIFFORSTATID.getName(), x4difforstatid);

        List<X4DifforStat> result = dataAccessService.search(X4DifforStat.class, QUERY_01, parameters, false, true);

        long endTime = System.currentTimeMillis();
        log.debug("  - Search getX4DifforStatById found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return Assorted.isNotEmpty(result) ? result.get(0) : null;
    }

    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4DifforStat> getX4DifforStatByIdDifformita(String idDifformita) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getX4DifforStatByIdDifformita({}) |{}|", idDifformita, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.X4CATDIFFORID.getName(), idDifformita);

        List<X4DifforStat> result = dataAccessService.search(X4DifforStat.class, QUERY_02, parameters, false, true);

        long endTime = System.currentTimeMillis();
        log.debug("  - Search getX4DifforStatByIdDifformita found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return result;
    }
}
