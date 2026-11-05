package it.terna.nas.service.dal.service;

import static it.terna.kernel.basement.utility.Assorted.isEmpty;
import static it.terna.kernel.basement.utility.Assorted.isNotEmpty;
import static it.terna.kernel.basement.utility.Assorted.notNull;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.ParameterMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.dto.ParameterData;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class X4DssWorkedHistoryQueryService
{
	private static final String	ENTITY_UPDATE_MESSAGE		= "'Unable to save/update X4DssWorkedHistory '%s";
	private static final String	QUERY_01					= "select c from " + X4DssWorkedHistory.class.getName() + " c where c.idDssWorked = :idDssWorked";
	private static final String PARAM_I_ID_DSS_WORKER       = "I_ID_DSS_WORKER";
	private static final String PARAM_I_CORRELATION         = "I_CORRELATION";
    private static final String PARAM_I_LIST_PAIR           = "I_LIST_PAIR";
    private static final String PARAM_I_TRONCO              = "I_TRONCO";
    private static final String PARAM_I_POLICY_ID           = "I_POLICY_ID";
    private static final String PARAM_O_ESITO               = "O_ESITO";
    private static final String STORED_PROCEDURE_01         = "PKG_DSS_MANAGER_UTILITY.SAVE_TRONCONUM";
    private static final String STORED_PROCEDURE_02         = "PKG_DSS_MANAGER_UTILITY.UPDATE_DSS_HISTORY";
    private static final String STORED_PROCEDURE_03         = "PKG_DSS_MANAGER_UTILITY.CHECK_TRONCONUM";
    
	@Autowired
    private DataAccessService   dataAccessService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public String checkTroncoNum(String correlationId, String troncoNum, String x4policyId) 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call checkTroncoNum({}, {}, {}) |{}|", correlationId, troncoNum, x4policyId, startTime);        
         
        boolean result = false;
        String esito   = null;
        
        try {
            Map<String, ParameterData> parameters = new LinkedHashMap<>();
            parameters.put(PARAM_I_CORRELATION, new ParameterData(correlationId, ParameterMode.IN));
            parameters.put(PARAM_I_TRONCO,      new ParameterData(troncoNum, ParameterMode.IN));
            parameters.put(PARAM_I_POLICY_ID,   new ParameterData(x4policyId, ParameterMode.IN));
            parameters.put(PARAM_O_ESITO,       new ParameterData("", ParameterMode.INOUT));
            result = dataAccessService.executeStoredProcedure(STORED_PROCEDURE_03, parameters);
            
            ParameterData parameterData = parameters.get(PARAM_O_ESITO);
            if (Assorted.notNull(parameterData))
                esito = (String) parameterData.getValue();
            
        } catch(Exception p) {
            log.error(p.getMessage(), p);
        }
        
        long endTime = System.currentTimeMillis();
        log.info("  - Executing checkTroncoNum result obtained [{}] ==> value [{}] |{}|", result, esito, endTime - startTime);
        
        return esito;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
  	public X4DssWorkedHistory getDssWorkedHistoryById(String key) 
  			throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call getDssWorkedHistoryById({}) |{}|", key, startTime);		
  		
  	    Map <String, String> parameters = new HashMap<>();
  		parameters.put(ESQLMappingParameter.ID_DSS_WORKED.getName(), key);
  		
  		List<X4DssWorkedHistory> result = dataAccessService.search(X4DssWorkedHistory.class, QUERY_01, parameters, false, true);
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Search getDssWorkedHistoryById found [{}] |{}|", isNotEmpty(result) ? result.size() : 0, endTime - startTime);
  		
  		return isNotEmpty(result) ? result.get(0) : null;
  	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
  	public void saveDssWorkedHistory(X4DssWorkedHistory row) 
  			throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call saveDssWorkedHistory() |{}|", startTime);		
  		
  		dataAccessService.save(row, X4DssWorkedHistory.class.getName(), ENTITY_UPDATE_MESSAGE, notNull(row) ? row.toString(): "");
  				
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Executing saveDssWorkedHistory |{}|", endTime - startTime);
  	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void saveTroncoNum(Long idDssWorker) 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call saveTroncoNum({}) |{}|", idDssWorker, startTime);        
         
        boolean result = false;
        
        try {
            Map<String, ParameterData> parameters = new LinkedHashMap<>();
            parameters.put(PARAM_I_ID_DSS_WORKER,       new ParameterData(idDssWorker, ParameterMode.IN));
            result = dataAccessService.executeStoredProcedure(STORED_PROCEDURE_01, parameters);
        } catch(Exception p) {
            log.error(p.getMessage(), p);
        }
        
        long endTime = System.currentTimeMillis();
        log.info("  - Executing saveTroncoNum result obtained [{}] |{}|", result, endTime - startTime);
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void updateHistoryDss(String correlationId, String listPairAssetWo, String troncoNum) 
            throws DataLayerException 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call updateHistoryDss({}, {}, {}) |{}|", correlationId, listPairAssetWo, troncoNum, startTime);        
         
        Map<String, ParameterData> parameters = new LinkedHashMap<>();
        parameters.put(PARAM_I_CORRELATION, new ParameterData(correlationId, ParameterMode.IN));
        parameters.put(PARAM_I_LIST_PAIR,   new ParameterData(listPairAssetWo, ParameterMode.IN));
        parameters.put(PARAM_I_TRONCO,      new ParameterData(isEmpty(troncoNum) ? "" : troncoNum, ParameterMode.IN));
        
        boolean result = dataAccessService.executeStoredProcedure(STORED_PROCEDURE_02, parameters);
        long endTime = System.currentTimeMillis();
        log.info("  - Executing updateHistoryDss result obtained [{}] |{}|", result, endTime - startTime);
    }
}
