package it.terna.nas.service.dal.service.sap;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.basement.utility.Monitor;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.sap.X4ODM;
import it.terna.nas.service.dal.entity.eam.sap.X4ODMOP;
import it.terna.nas.service.dal.entity.eam.sap.X4ODMjdbc;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.material.utility.Catalog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class X4OdMQueryService {

    private static final String S_REQUEST = "SELECT  MAX(X4ODMID) AS X4ODMID, TO_CHAR(MAX(CREATEDDATE),'yyyy-MM-dd') AS CREATEDDATE, TO_CHAR(MAX(CHANGEDDATE),'yyyy-MM-dd') AS CHANGEDDATE, NULL AS STATOSISTEMA, TIPOORDINE, TIPOATTIVITA FROM X4ODM X4 WHERE TIPOORDINE IS NOT NULL AND TIPOATTIVITA IS NOT NULL GROUP BY TIPOORDINE, TIPOATTIVITA";
    private static final String S_REQUEST2 = "SELECT * FROM X4ODM WHERE IDODM = :idodm";
    private static final String S_REQUEST3 = "SELECT * FROM X4ODMOP WHERE NOPERAZIONE = :noperazione AND IDODM = :idodm";
    @Autowired
    private DataAccessService dataAccessService;

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public List<X4ODM> getRequests()
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getRequests() |{}|", startTime);
        List<X4ODM> result;
        try {
            result = dataAccessService.search(X4ODM.class, S_REQUEST, null, true, true);
        } catch (Exception e) {
            log.error("Error in getRequests()", e);
            throw new EAMException("Error in getRequests()", e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getRequests Ordini di Manutenzione found [{}] record - Time: |{}| ms", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return Assorted.isNotEmpty(result) ? result : null;
    }

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public X4ODMjdbc getById(String id)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getById() |{}|", startTime);
        List<X4ODMjdbc> result;
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put(Catalog.ESQLMappingParameter.IDODM.getName(), id);
            result = dataAccessService.search(X4ODMjdbc.class, S_REQUEST2, parameters, true, true);
        } catch (DataLayerException e) {
            log.error("Error in getById() - idodm: {}", id, e);
            throw new EAMException("Error in getById() - idodm: " + id, e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getById Ordini di Manutenzione found [{}] record - Time: |{}| ms", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return Objects.nonNull(result) && !result.isEmpty() ? result.get(0) : null;
    }


    @Transactional
    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public void saveOrUpdate(X4ODMjdbc x4ODM)
            throws  EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call saveOrUpdate() - X4Odm |{}|", startTime);
        try {
            dataAccessService.update(x4ODM, X4ODM.class.getName(), "Error to insert X4ODM ['%s']", x4ODM.getIdodm());
        } catch (DataLayerException e) {
            log.error("Error to saveOrUpdate X4ODM with idodm {}", x4ODM.getIdodm(), e);
            throw new EAMException(Monitor.EErrorCategory.GENERIC.getSpecificErrorCode(), Monitor.EErrorCategory.GENERIC.getErrorDescription(), e);
        }
        long endTime = System.currentTimeMillis();
        log.debug(" - saveOrUpdate X4ODM |{}|", endTime - startTime);
    }

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public X4ODMOP getByIdOp(String id, String nOperazione)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getByIdOp() |{}|", startTime);
        List<X4ODMOP> result;
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put(Catalog.ESQLMappingParameter.IDODM.getName(), id);
            parameters.put(Catalog.ESQLMappingParameter.NOPERAZIONE.getName(), nOperazione);
            result = dataAccessService.search(X4ODMOP.class, S_REQUEST3, parameters, true, true);
        } catch (DataLayerException e) {
            log.error("Error in getByIdOp() - idodm: {} and nOperazione: {}", id, nOperazione, e);
            throw new EAMException(Monitor.EErrorCategory.GENERIC.getSpecificErrorCode(), Monitor.EErrorCategory.GENERIC.getErrorDescription(), e);
        }
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getByIdOp found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return Objects.nonNull(result) && !result.isEmpty() ? result.get(0) : null;
    }

    @Transactional
    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public void saveOrUpdateOp(X4ODMOP x4OdmOp)
            throws  EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call saveOrUpdateOp() |{}|", startTime);
        try {
            dataAccessService.update(x4OdmOp, X4ODMOP.class.getName(), "Error to insert X4ODMOP ['%s']", x4OdmOp.getIdodm());
        } catch (DataLayerException e) {
            log.error("Error to saveOrUpdate X4ODMOP with idodm {}", x4OdmOp.getIdodm(), e);
            throw new EAMException(Monitor.EErrorCategory.GENERIC.getSpecificErrorCode(), Monitor.EErrorCategory.GENERIC.getErrorDescription(), e);
        }
        long endTime = System.currentTimeMillis();
        log.debug(" - saveOrUpdateOp X4ODMOP |{}|", endTime - startTime);
    }

}
