package it.terna.nas.service.dal.service.sap;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.sap.X4STORNOSAP;
import it.terna.nas.service.dal.entity.eam.sap.X4_SAP_FILTER;
import it.terna.nas.service.exceptions.EAMException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class X4SapFilterQueryService {

    private static final String S_REQUEST = "SELECT ID, FILTRO, TIPO_SPESA, TIPO_ORDINE, TIPO_ATTIVITA, LIVELLO, STATO FROM X4_SAP_FILTER";
    private static final String S_REQUEST_1 = "SELECT * FROM X4_STORNO_SAP WHERE LOADED = :loaded";


    @Autowired
    private DataAccessService dataAccessService;

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public List<X4_SAP_FILTER> getFilter()
            throws DataLayerException {
        long startTime = System.currentTimeMillis();
        log.debug("call getFilter() |{}|", startTime);
        List<X4_SAP_FILTER> result = dataAccessService.search(X4_SAP_FILTER.class, S_REQUEST, null, true, true);
        long endTime = System.currentTimeMillis();
        log.info("  - Search getFilter SAP found [{}] record - Time: |{}| ms", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return result;
    }

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public List<X4STORNOSAP> getStorno(String loaded)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getRequests() |{}|", startTime);
        List<X4STORNOSAP> result;
        try {
            Map<String, String> searchEntries = new HashMap<>();
            searchEntries.put("loaded", loaded);
            result = dataAccessService.search(X4STORNOSAP.class, S_REQUEST_1, searchEntries, true, true);
        } catch (Exception e) {
            log.error("Error in getRequests(): {}", e.getMessage());
            throw new EAMException("Error in getRequests() for NTK", e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        log.info("  - Search getRequests Network found [{}] record - Time: |{}| ms", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return Assorted.isNotEmpty(result) ? result : null;
    }

}
