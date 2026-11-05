package it.terna.nas.service.dal.service.sap;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.sap.X4WORKORDER;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.material.utility.Catalog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

@Service
@Slf4j
public class X4WorkorderQueryService {

    private static final String S_REQUEST = "SELECT WORKORDERID, WONUM, X4ODM, X4NTK, CHANGEBY, X4TIPOSPESA, X4BURKS, X4NOPERAZIONE FROM WORKORDER WHERE WONUM = :refwo AND SITEID = :siteid";

    @Autowired
    private DataAccessService dataAccessService;

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public List<X4WORKORDER> getWorkorder(String refwo, String siteid)
            throws EAMException {
        long startTime = System.currentTimeMillis();
        log.debug("call getWorkorder({}) |{}|", refwo, startTime);
        List<X4WORKORDER> result = null;
        try {
            Map<String, String> parameters = new HashMap<>();
            parameters.put(Catalog.ESQLMappingParameter.REFWO.getName(), refwo);
            parameters.put(Catalog.ESQLMappingParameter.SITEID.getName(), siteid);
            result = dataAccessService.search(X4WORKORDER.class, S_REQUEST, parameters, true, true);
        } catch (Exception e) {
            log.error("Error in getWorkorder({})", refwo, e);
            throw new EAMException("Error in getWorkorder(" + refwo + ")", e.getMessage(), e);
        }
        long endTime = System.currentTimeMillis();
        log.info(" - Search getWorkorder  found [{}] record - Time: |{}| ms", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return Assorted.isNotEmpty(result) ? result : null;
    }


}
