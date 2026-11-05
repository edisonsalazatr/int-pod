package it.terna.nas.service.dal.service.sap;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.sap.X4OrdInt;
import it.terna.nas.service.exceptions.EAMException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class X4OrdIntQueryService {

    private static final String S_REQUEST = "SELECT MAX(X4ORDINTID) AS X4ORDINTID, TO_CHAR(MAX(CREATEDDATE),'yyyy-MM-dd') AS CREATEDDATE, TO_CHAR(MAX(CHANGEDDATE),'yyyy-MM-dd') AS CHANGEDDATE, NULL AS STATOSISTEMA, TIPOOI FROM X4ORDINT WHERE TIPOOI IS NOT NULL GROUP BY TIPOOI";

    @Autowired
    private DataAccessService dataAccessService;


    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public List<X4OrdInt> getRequests()
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getRequests() |{}|", startTime);
        List<X4OrdInt> result;
        try {
            Map<String, String> parameters = new HashMap<>();
            result = dataAccessService.search(X4OrdInt.class, S_REQUEST, parameters, true, true);
        } catch (Exception e) {
            log.error("Error in getRequests()", e);
            throw new EAMException("Error in getRequests()", e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        log.info("  - Search getRequests Ordini Interni found [{}] record - Time: |{}| ms", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return Assorted.isNotEmpty(result) ? result : null;
    }

}
