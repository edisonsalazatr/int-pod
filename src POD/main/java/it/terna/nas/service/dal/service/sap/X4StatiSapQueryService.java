package it.terna.nas.service.dal.service.sap;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.sap.X4StatiSap;
import it.terna.nas.service.exceptions.EAMException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class X4StatiSapQueryService {

    @Autowired
    private DataAccessService dataAccessService;

    private static final String S_REQUEST = "SELECT STATO, CODICE, DESCRIZIONE FROM X4_STATI_SAP";

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public List<X4StatiSap> getAll()
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getAll() stati SAP |{}|", startTime);
        List<X4StatiSap> result;
        try {
            result = dataAccessService.search(X4StatiSap.class, S_REQUEST, null, true, true);
        } catch (Exception e) {
            log.error("Error in getAll() stati SAP", e);
            throw new EAMException("Error in getAll() stati SAP ", e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        log.info("  - Search getAll Stati SAP found [{}] record - Time: |{}| ms", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return Assorted.isNotEmpty(result) ? result : null;
    }

}
