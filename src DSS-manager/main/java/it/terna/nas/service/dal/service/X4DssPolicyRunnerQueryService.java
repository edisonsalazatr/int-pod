package it.terna.nas.service.dal.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import javax.persistence.ParameterMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.dto.ParameterData;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class X4DssPolicyRunnerQueryService
{
	private static final String ENTITY2_UPDATE_MESSAGE      = "Unable to save/update X4DssPolicyRunner for policy '%s'";
	private static final String	QUERY_01					= "select c from " + X4DssPolicyRunner.class.getName() + " c where c.correlationId = :correlationKey";
	private static final String QUERY_02                    = "select c from " + X4DssPolicyRunner.class.getName() + " c where c.x4DssPolicyRunnerId = :id";
	private static final String	QUERY_03					= "select c from " + X4DssPolicyRunner.class.getName() + " c where c.correlationId = :correlationKey and c.x4PolicyId = :x4PolicyId";
	
	private static final String PURGE_RUNNER_HISTORY		= "PURGE_X4_DSS_POLICY_RUNNER_HISTORY";
    private static final String	PARAM_I_BATCH_SIZE			= "P_BATCH_SIZE";
    private static final String	PARAM_I_RETENTION			= "P_RETENTION_DAYS";
	
	@Autowired
    private DataAccessService   dataAccessService;
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
  	public X4DssPolicyRunner findRunnerByCorrelationId(String correlationId) 
  			throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call findRunnerByCorrelationId({}) |{}|", correlationId, startTime);		
  		
  	    Map <String, String> parameters = new HashMap<>();
  		parameters.put(ESQLMappingParameter.CORRELATIONKEY.getName(), correlationId);
  		List<X4DssPolicyRunner> result = dataAccessService.search(X4DssPolicyRunner.class, QUERY_01, parameters, false, true);
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Search findRunnerByCorrelationId found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
  		
  		return Assorted.isNotEmpty(result) ? result.get(0) : null;
  	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public X4DssPolicyRunner findRunnerById(Long id) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call findRunnerById({}) |{}|", id, startTime);     
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ID.getName(), id.toString());
        List<X4DssPolicyRunner> result = dataAccessService.search(X4DssPolicyRunner.class, QUERY_02, parameters, false, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search findRunnerById found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return Assorted.isNotEmpty(result) ? result.get(0) : null;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(propagation = Propagation.REQUIRED)    
    public X4DssPolicyRunner saveOrUpdate(X4DssPolicyRunner row) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call saveOrUpdate({}) |{}|", row, startTime);        
        X4DssPolicyRunner result = dataAccessService.save(row, X4DssPolicyRunner.class.getName(), ENTITY2_UPDATE_MESSAGE, row.getIdPolicy());
        long endTime = System.currentTimeMillis();
        log.debug("  - Saved X4DssPolicyRunner |{}|", endTime - startTime);
        return result;
    }
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public X4DssPolicyRunner saveOrUpdateRequiresNew(X4DssPolicyRunner row) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call saveOrUpdate({}) |{}|", row, startTime);        
        X4DssPolicyRunner result = dataAccessService.save(row, X4DssPolicyRunner.class.getName(), ENTITY2_UPDATE_MESSAGE, row.getIdPolicy());
        long endTime = System.currentTimeMillis();
        log.debug("  - Saved X4DssPolicyRunner |{}|", endTime - startTime);
        return result;
    }

    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public X4DssPolicyRunner findByPolicyAndCorrelation(String policyStr, String correlationId) throws DataLayerException {
  		long startTime = System.currentTimeMillis();
  		log.debug("call findByPolicyAndCorrelation({}, {}) |{}|", correlationId, policyStr, startTime);		
  	    Map <String, String> parameters = new HashMap<>();
  		parameters.put(ESQLMappingParameter.CORRELATIONKEY.getName(), correlationId);
  		parameters.put(ESQLMappingParameter.POLICYID.getName(), policyStr);
  		List<X4DssPolicyRunner> result = dataAccessService.search(X4DssPolicyRunner.class, QUERY_03, parameters, false, true);
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Search findByPolicyAndCorrelation found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
  		
  		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}

    @Transactional
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public Boolean purgeOldRunner(Integer retention, Integer batchSize) 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call purgeRunner({}, {}) |{}|", retention, batchSize, startTime);        
         
        boolean result = false;
        
        try {
            Map<String, ParameterData> parameters = new LinkedHashMap<>();
            parameters.put(PARAM_I_BATCH_SIZE, 		new ParameterData(batchSize,	ParameterMode.IN));
            parameters.put(PARAM_I_RETENTION,       new ParameterData(retention, ParameterMode.IN));
        
            result = dataAccessService.executeStoredProcedure(PURGE_RUNNER_HISTORY, parameters);
           
        } catch(Exception p) {
            log.error(p.getMessage(), p);
        }
        
        long endTime = System.currentTimeMillis();
        log.info("  - Executing purgeRunner obtained [{}] |{}|", result, endTime - startTime);
        
        return result;
    }
}
