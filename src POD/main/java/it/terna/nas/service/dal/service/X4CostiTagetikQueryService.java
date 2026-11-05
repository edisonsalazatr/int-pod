package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.ParameterMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.storage.dto.ParameterData;
import it.terna.kernel.storage.service.DataAccessService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4CostiTagetikQueryService {
    
    @Autowired
    private DataAccessService dataAccessService;

    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public void processAllTagetikCosts(String runDate) throws DataLayerException {
        log.info("Executing procedure MOVE_COSTI_TAGETIK for run date: {}", runDate);
        Map<String, ParameterData> parameters = new HashMap<>();
        parameters.put("p_run_date", new ParameterData(runDate, ParameterMode.IN));
        
        dataAccessService.executeStoredProcedure("MOVE_COSTI_TAGETIK", parameters);
        log.info("Procedure MOVE_COSTI_TAGETIK completed.");
    }
}