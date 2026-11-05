package it.terna.nas.service.dal.service.sap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import it.terna.kernel.basement.utility.Monitor;
import it.terna.nas.service.dal.entity.eam.sap.X4NTKjdbc;
import it.terna.nas.service.dal.entity.eam.sap.X4OPNTK;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.material.utility.Catalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.sap.X4NTK;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;

@Service
@Slf4j
public class X4NtkQueryService {

    private static final String S_REQUEST = "SELECT  MAX(X4NTKID) AS X4NTKID, TO_CHAR(MAX(CREATEDATE),'yyyy-MM-dd') AS CREATEDATE, TO_CHAR(MAX(CHANGEDDATE),'yyyy-MM-dd') AS CHANGEDDATE, null AS STATOSISTEMASAP FROM X4NTK HAVING MAX(X4NTKID) IS NOT NULL";
    private static final String S_REQUEST2 = "SELECT * FROM X4NTK WHERE IDNTK = :idntk";
    private static final String S_REQUEST3 = "SELECT * FROM X4OPNTK WHERE IDNTK = :idntk AND IDOPERAZIONE = :idoperazione";

    @Autowired
    private DataAccessService dataAccessService;

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public List<X4NTK> getRequests()
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getRequests() |{}|", startTime);
        List<X4NTK> result;
        try {
            result = dataAccessService.search(X4NTK.class, S_REQUEST, null, true, true);
        } catch (Exception e) {
            log.error("Error in getRequests(): {}", e.getMessage());
            throw new EAMException("Error in getRequests() for NTK", e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        log.info("  - Search getRequests Network found [{}] record - Time: |{}| ms", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return Assorted.isNotEmpty(result) ? result : null;
    }

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public X4NTKjdbc getById(String id)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getById() |{}|", startTime);
        List<X4NTKjdbc> result;
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put(Catalog.ESQLMappingParameter.IDNTK.getName(), id);
            result = dataAccessService.search(X4NTKjdbc.class, S_REQUEST2, parameters, true, true);
        } catch (DataLayerException e) {
            log.error("Error in getById() - idntk: {}", id, e);
            throw new EAMException("Error in getById() - idntk: " + id, e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getById Network found [{}] record - Time: |{}| ms", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return Objects.nonNull(result) && !result.isEmpty() ? result.get(0) : null;
    }

    @Transactional
    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public void saveOrUpdate(X4NTKjdbc x4Ntk)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call saveOrUpdate() X4NTK |{}|", startTime);
        try {
            dataAccessService.update(x4Ntk, X4NTKjdbc.class.getName(), "Error to insert X4NTK");
        } catch (DataLayerException e) {
            log.error("Error to saveOrUpdate X4NTK with idntk {}", x4Ntk.getIdntk(), e);
            throw new EAMException(Monitor.EErrorCategory.GENERIC.getSpecificErrorCode(), Monitor.EErrorCategory.GENERIC.getErrorDescription(), e);
        }
        long endTime = System.currentTimeMillis();
        log.debug(" - saveOrUpdate X4NTK |{}|", endTime - startTime);
    }

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public X4OPNTK getByIdOp(String id, String nOperazione)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getByIdOp() |{}|", startTime);
        List<X4OPNTK> result;
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put(Catalog.ESQLMappingParameter.IDNTK.getName(), id);
            parameters.put(Catalog.ESQLMappingParameter.IDOPERAZIONE.getName(), nOperazione);
            result = dataAccessService.search(X4OPNTK.class, S_REQUEST3, parameters, true, true);
        } catch (DataLayerException e) {
            log.error("Error in getByIdOp() - idntk: {} and nOperazione: {}", id, nOperazione, e);
            throw new EAMException(Monitor.EErrorCategory.GENERIC.getSpecificErrorCode(), Monitor.EErrorCategory.GENERIC.getErrorDescription(), e);
        }
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getByIdOp found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return Objects.nonNull(result) && !result.isEmpty() ? result.get(0) : null;
    }

    @Transactional
    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public void saveOrUpdateOp(X4OPNTK x4Ntk)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call saveOrUpdate() X4OPNTK |{}|", startTime);
        try {
            dataAccessService.update(x4Ntk, X4OPNTK.class.getName(), "Error to insert X4OPNTK");
        } catch (DataLayerException e) {
            log.error("Error to saveOrUpdate X4OPNTK with idntk {}", x4Ntk.getIdntk(), e);
            throw new EAMException(Monitor.EErrorCategory.GENERIC.getSpecificErrorCode(), Monitor.EErrorCategory.GENERIC.getErrorDescription(), e);
        }
        long endTime = System.currentTimeMillis();
        log.debug(" - saveOrUpdate X4OPNTK |{}|", endTime - startTime);
    }

}
