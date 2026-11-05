package it.terna.nas.service.dal.service.sap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import it.terna.kernel.basement.utility.Monitor;
import it.terna.nas.service.dal.entity.eam.sap.X4WBE;
import it.terna.nas.service.dal.entity.eam.sap.X4WBENTk;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.material.utility.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.sap.X4WBERestricted;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

@Service
@Slf4j
public class X4WBEQueryService {

    private static final String S_REQUEST = "SELECT MAX(X4WBEID) AS X4WBEID, TO_CHAR(MAX(CHANGEDATE), 'yyyy-MM-dd') AS CHANGEDATE FROM X4WBE HAVING MAX(X4WBEID) IS NOT NULL";
    private static final String S_REQUEST_2 = "SELECT DISTINCT IDWBE FROM X4WBE WHERE IDWBS IS NULL AND IDWBE IS NOT NULL";
    private static final String S_REQUEST_3 = "SELECT * FROM X4WBE WHERE IDWBE = :idwbe";

    @Autowired
    private DataAccessService dataAccessService;

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public List<X4WBERestricted> getRequests()
            throws EAMException {
        long startTime = System.currentTimeMillis();
        List<X4WBERestricted> result;
        log.debug("call getRequests() |{}|", startTime);
        try {
            result = dataAccessService.search(X4WBERestricted.class, S_REQUEST, null, true, true);
        } catch(DataLayerException e) {
            log.error("Error in getRequests()", e);
            throw new EAMException("Error in getRequests()", e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        log.info("  - Search getRequests found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return result;
    }

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public List<X4WBENTk> getWbeNtk()
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getWbeNtk() |{}|", startTime);
        List<X4WBENTk> result;
        try {
            result = dataAccessService.search(X4WBENTk.class, S_REQUEST_2, null, true, true);
        } catch (DataLayerException e) {
            log.error("Error in getWbeNtk()", e);
            throw new EAMException("Error in getWbeNtk()", e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        log.info("  - Search getWbeNtk found [{}] record - Time: |{}| ms", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return result;
    }

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public X4WBE getById(String idwbe)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getById() |{}|", startTime);
        List<X4WBE> result;
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put(Catalog.ESQLMappingParameter.IDWBE.getName(), idwbe);
            result = dataAccessService.search(X4WBE.class, S_REQUEST_3, parameters, true, true);
        } catch (DataLayerException e) {
            log.error("Error in getById() - idwbe: {}", idwbe, e);
            throw new EAMException("Error in getById() - idwbe: " + idwbe, e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        log.info("  - Search getById WBE found [{}] record - Time: |{}| ms", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return Objects.nonNull(result) && !result.isEmpty()? result.get(0) : null;
    }

    @Transactional
    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public void saveOrUpdate(X4WBE x4WBE)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call saveOrUpdate() X4WBE |{}|", startTime);
        try {
            dataAccessService.update(x4WBE, X4WBE.class.getName(), "Error to insert X4WBE");
        } catch (DataLayerException e) {
            log.error("Error to saveOrUpdate X4WBE with idwbe {} and idwbs {}", x4WBE.getIdwbe(), x4WBE.getIdwbs(), e);
            throw new EAMException(Monitor.EErrorCategory.GENERIC.getSpecificErrorCode(), Monitor.EErrorCategory.GENERIC.getErrorDescription(), e);
        }
        long endTime = System.currentTimeMillis();
        log.debug(" - saveOrUpdate X4WBE |{}|", endTime - startTime);
    }

}
