package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import org.hibernate.Session;
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
import it.terna.nas.service.dal.entity.eam.RootAsset;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyRunner;
import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.exposure.events.WorkedPolicyRequestEvent;
import it.terna.nas.service.material.dto.Pair;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class X4DssWorkedHistoryQueryService
{
	private static final String	ENTITY_UPDATE_MESSAGE		= "Unable to save/update X4DssWorkedHistory for policy '%s'";
	private static final String	PARAM_I_CORRELATION			= "I_CORRELATION";
	private static final String	PARAM_I_CREATE_BY			= "I_CREATE_BY";
    private static final String PARAM_I_RUNNER_ID           = "I_RUNNER_ID";
    private static final String PARAM_I_RECOVERY_COUNT      = "I_RECOVERY_COUNT";
	private static final String	PARAM_I_LIST_ASSETNUM		= "I_LIST_ASSETNUM";
	private static final String	PARAM_I_LIST_ID_DSS_WORKED	= "I_LIST_ID_DSS_WORKED";
	private static final String	PARAM_I_POLICY_ID			= "I_POLICY_ID";
	private static final String PARAM_I_TOTAL_MESSAGE		= "I_TOTAL_MESSAGE";
	private static final String PARAM_I_TRONCO              = "I_TRONCO";
	private static final String PARAM_I_VERSION             = "I_VERSION";
	private static final String PARAM_O_ESITO               = "O_ESITO";
	private static final String	PARAM_O_LIST_ASSETNUM		= "O_LIST_ASSETNUM";
	private static final String	PARAM_O_LIST_ID_DSS_WORKED	= "O_LIST_ID_DSS_WORKED";
	private static final String	QUERY_01					= "select c from " + X4DssWorkedHistory.class.getName() + " c where c.policyId = :x4PolicyId and c.correlationId = :correlationKey";
	private static final String	QUERY_02					= "SELECT PKG_DSS_MANAGER_UTILITY.FIND_ROOT_ASSET(:assetNum) AS ROOT_ASSET FROM DUAL";
	private static final String QUERY_03                    = "select c from " + X4DssWorkedHistory.class.getName() + " c where c.policyId = :x4PolicyId and c.correlationId = :correlationKey and c.assetNum = :assetNum";
	private static final String QUERY_BY_POLICY_AND_ASSET   = "select c from " + X4DssWorkedHistory.class.getName() + " c where c.policyId = :x4PolicyId and c.assetNum = :assetNum";

	private static final String QUERY_BY_RUNNERID		    = "select c from " + X4DssWorkedHistory.class.getName() + " c where c.x4DssPolicyRunnerId = :runnerId";
	
	private static final String	STORED_PROCEDURE_01			= "PKG_DSS_MANAGER_UTILITY.SAVE_DSS_WORKER_HISTORY";
	private static final String	STORED_PROCEDURE_02			= "PKG_DSS_MANAGER_UTILITY.UPDATE_PROGRESSIVE_MESSAGE";
    private static final String STORED_PROCEDURE_03         = "PKG_DSS_MANAGER_UTILITY.CHECK_TRONCONUM";	
	private static final String	STORED_PROCEDURE_04			= "PKG_DSS_MANAGER_UTILITY.MARK_DSS_WORKED_AS_SENT";
	
	private static final String QUERY_04 					= "UPDATE x4_dss_worked_history SET sent = ? WHERE x4_dss_worked_historyid = ?";
	private static final String QUERY_05 =
		    "SELECT DISTINCT " +
		    "       PKG_DSS_MANAGER_UTILITY.FIND_ROOT_ASSET(a3.ASSETNUM) AS ROOT_ASSET " +
		    "FROM ASSET A " +
		    "JOIN ASSETLOCRELATION A2 ON A2.SOURCEASSETNUM = A.ASSETNUM " +
		    "JOIN ASSET A3            ON A2.TARGETASSETNUM = A3.ANCESTOR " +
		    "JOIN CLASSSTRUCTURE P    ON A3.CLASSSTRUCTUREID = P.CLASSSTRUCTUREID " +
		    "JOIN X4TABIDONEITA T     ON T.TIPOLOGIAASSET = P.X4ELEMENTO " +
		    "                        AND T.STATOIDONEITA = A3.STATUS " +
		    "WHERE A.ASSETNUM = :assetNum " +
		    "  AND P.CLASSIFICATIONID = 'FUNE DI GUARDIA'";


	
	
	@Autowired
    private DataAccessService   dataAccessService;
	
    @Autowired
    private EntityManager entityManager;
    
	
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
        log.info("end checkTroncoNum({}, {}, {}) result obtained [{}] ==> value [{}] |{}|", correlationId, troncoNum, x4policyId, result, esito, endTime - startTime);
        
        return esito;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
  	public String getDssAssetNum(String assetNum) 
  			throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call getDssAssetNum({}) |{}|", assetNum, startTime);		
  		
  	    Map <String, String> parameters = new HashMap<>();
  		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
  		List<RootAsset> result = dataAccessService.search(RootAsset.class, QUERY_02, parameters, true, true);
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Search getDssAssetNum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
  		
  		return Assorted.isNotEmpty(result) ? result.get(0).getRootAsset() : null;
  	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
  	public X4DssWorkedHistory getDssWorkedHistoryByCorrelationAndPolicy(String correlation, String policy) 
  			throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call getDssWorkedHistoryByCorrelationAndPolicy({}, {}) |{}|", correlation, policy, startTime);		
  		
  	    Map <String, String> parameters = new HashMap<>();
  		parameters.put(ESQLMappingParameter.CORRELATIONKEY.getName(), correlation);
  		parameters.put(ESQLMappingParameter.POLICYID.getName(), policy);
  		
  		List<X4DssWorkedHistory> result = dataAccessService.search(X4DssWorkedHistory.class, QUERY_01, parameters, false, true);
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Search getDssWorkedHistoryByCorrelationAndPolicy found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
  		
  		return Assorted.isNotEmpty(result) ? result.get(0) : null;
  	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public X4DssWorkedHistory getDssWorkedHistoryByCorrelationAndPolicyAndAsset(String correlation, String policy, String asset) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getDssWorkedHistoryByCorrelationAndPolicyAndAsset({}, {}, {}) |{}|", correlation, policy, asset, startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.CORRELATIONKEY.getName(), correlation);
        parameters.put(ESQLMappingParameter.POLICYID.getName(), policy);
        parameters.put(ESQLMappingParameter.ASSETNUM.getName(), asset);
        
        List<X4DssWorkedHistory> result = dataAccessService.search(X4DssWorkedHistory.class, QUERY_03, parameters, false, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getDssWorkedHistoryByCorrelationAndPolicyAndAsset found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return Assorted.isNotEmpty(result) ? result.get(0) : null;
    }
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4DssWorkedHistory> getDssWorkedHistoryByPolicyAndAsset(String policy, String asset) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getDssWorkedHistoryByPolicyAndAsset({}, {}) |{}|", policy, asset, startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.POLICYID.getName(), policy);
        parameters.put(ESQLMappingParameter.ASSETNUM.getName(), asset);
        
        List<X4DssWorkedHistory> result = dataAccessService.search(X4DssWorkedHistory.class, QUERY_BY_POLICY_AND_ASSET, parameters, false, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getDssWorkedHistoryByPolicyAndAsset found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public Pair<String, String> saveDssWorkedHistory(String correlation, Integer totalMessage, String policyId, String user, String listAssetNum, X4DssPolicyRunner runner, Integer version) 
			throws DataLayerException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call saveDssWorkedHistory({}, {}, {}, {}, {}, {}, {}) |{}|", correlation, policyId, user, listAssetNum, runner.getX4DssPolicyRunnerId(), totalMessage, version, startTime);		
		 
		Map<String, ParameterData> parameters = new LinkedHashMap<>();
		parameters.put(PARAM_I_CORRELATION, 		new ParameterData(correlation, ParameterMode.IN));
		parameters.put(PARAM_I_TOTAL_MESSAGE, 		new ParameterData(totalMessage, ParameterMode.IN));
		parameters.put(PARAM_I_POLICY_ID, 			new ParameterData(policyId, ParameterMode.IN));
		parameters.put(PARAM_I_LIST_ASSETNUM, 		new ParameterData(listAssetNum, ParameterMode.IN));
		parameters.put(PARAM_I_CREATE_BY, 			new ParameterData(user, ParameterMode.IN));
        parameters.put(PARAM_I_RUNNER_ID,           new ParameterData(runner.getX4DssPolicyRunnerId(), ParameterMode.IN));
        parameters.put(PARAM_I_VERSION,             new ParameterData(version, ParameterMode.IN));
		parameters.put(PARAM_O_LIST_ASSETNUM,  		new ParameterData("", ParameterMode.INOUT));
		parameters.put(PARAM_O_LIST_ID_DSS_WORKED,  new ParameterData("", ParameterMode.INOUT));
		boolean result = dataAccessService.executeStoredProcedure(STORED_PROCEDURE_01, parameters);
		long endTime = System.currentTimeMillis();
		
		String assetNumList = null;
		String workListDSS 	= null;
		ParameterData parameterData = parameters.get(PARAM_O_LIST_ASSETNUM);
		if (Assorted.notNull(parameterData))
			assetNumList = (String) parameterData.getValue();

		parameterData = parameters.get(PARAM_O_LIST_ID_DSS_WORKED);
		if (Assorted.notNull(parameterData))
			workListDSS = (String) parameterData.getValue();

		log.info("  - Executing saveDssWorkedHistory result obtained [{}] |{}|", result, endTime - startTime);
		log.debug("  - Executing saveDssWorkedHistory asset result obtained {}", Assorted.isNotEmpty(assetNumList) ? assetNumList : "NO VALUE");
		log.debug("  - Executing saveDssWorkedHistory workerd result obtained {}", Assorted.isNotEmpty(workListDSS) ? workListDSS : "NO VALUE");
	
		return Pair.makePair(assetNumList, workListDSS);
	}
    
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(propagation = Propagation.REQUIRED)
    public X4DssWorkedHistory saveOrUpdate(X4DssWorkedHistory row) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call saveOrUpdate({}) |{}|", row, startTime);
		X4DssWorkedHistory result = null; 
		if (Assorted.notNull(row.getIdDssWorked()))
		    result = dataAccessService.update(row, X4DssWorkedHistory.class.getName(), ENTITY_UPDATE_MESSAGE, row.getPolicyId());
		else
		    result = dataAccessService.save(row, X4DssWorkedHistory.class.getName(), ENTITY_UPDATE_MESSAGE, row.getPolicyId());
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Saved X5DssWorkedHistory |{}|", endTime - startTime);
		return result;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public X4DssWorkedHistory saveOrUpdateRequiresNew(X4DssWorkedHistory row) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call saveOrUpdate({}) |{}|", row, startTime);
		X4DssWorkedHistory result = null; 
		if (Assorted.notNull(row.getIdDssWorked()))
		    result = dataAccessService.update(row, X4DssWorkedHistory.class.getName(), ENTITY_UPDATE_MESSAGE, row.getPolicyId());
		else
		    result = dataAccessService.save(row, X4DssWorkedHistory.class.getName(), ENTITY_UPDATE_MESSAGE, row.getPolicyId());
		
		long endTime = System.currentTimeMillis();
		log.debug("  - saveOrUpdateRequiresNew |{}|", endTime - startTime);
		return result;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void updateSent(Long idDssWorked, String sentValue)
            throws DataLayerException {

        long startTime = System.currentTimeMillis();
        log.debug("call updateSent(id={}, sent={}) |{}|",
                idDssWorked, sentValue, startTime);


        int updated = entityManager.createNativeQuery(QUERY_04)
                .setParameter(1, sentValue)
                .setParameter(2, idDssWorked)
                .executeUpdate();
        
		entityManager.flush();

        long endTime = System.currentTimeMillis();
        log.debug("  - updateSent UPDATED={} |{} ms|",
                updated, (endTime - startTime));
    }


   
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public void updateProgressiveMessage(Long runnerId, Integer recoveryCount) 
			throws DataLayerException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call updateProgressiveMessage({}, {}) |{}|", runnerId, recoveryCount, startTime);		
		 
		Map<String, ParameterData> parameters = new LinkedHashMap<>();
		parameters.put(PARAM_I_RUNNER_ID, 		new ParameterData(runnerId, ParameterMode.IN));
        parameters.put(PARAM_I_RECOVERY_COUNT,  new ParameterData(recoveryCount, ParameterMode.IN));		
		boolean result = dataAccessService.executeStoredProcedure(STORED_PROCEDURE_02, parameters);
		long endTime = System.currentTimeMillis();
		log.info("  - Executing saveDssWorkedHistory result obtained [{}] |{}|", result, endTime - startTime);
	}

    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public void markAsSent(String workListDSS) throws DataLayerException {
		long startTime = System.currentTimeMillis();
		log.debug("call markAsSent({}) |{}|", workListDSS , startTime);		
		 
		Map<String, ParameterData> parameters = new LinkedHashMap<>();
		parameters.put(PARAM_I_LIST_ID_DSS_WORKED, new ParameterData(workListDSS, ParameterMode.IN));
		boolean result = dataAccessService.executeStoredProcedure(STORED_PROCEDURE_04, parameters);
		long endTime = System.currentTimeMillis();
		log.info("  - Executing markAsSent result obtained [{}] |{}|", result, endTime - startTime);	
	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
  	public List<RootAsset> getDoppiaLineaAggrFuni(String assetNum) 
  			throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call getDoppiAssetNumFuni({}) |{}|", assetNum, startTime);		
  		
  	    Map <String, String> parameters = new HashMap<>();
  		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
  		List<RootAsset> result = dataAccessService.search(RootAsset.class, QUERY_05, parameters, true, true);
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Search getDoppiAssetNumFuni found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
  		
  		return Assorted.isNotEmpty(result) ? result : null;
  	}

    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
  	public X4DssWorkedHistory getDssWorkedHistoryByRunnerId(Long runnerId) 
  			throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call getDssWorkedHistoryByRunnerId({}) |{}|", runnerId, startTime);		
  		
  	    Map <String, String> parameters = new HashMap<>();
  		parameters.put(ESQLMappingParameter.RUNNER_ID.getName(), String.valueOf(runnerId));
  		List<X4DssWorkedHistory> result = dataAccessService.search(X4DssWorkedHistory.class, QUERY_BY_RUNNERID, parameters, false, true);
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Search getDssWorkedHistoryByRunnerId found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
  		
  		return Assorted.isNotEmpty(result) ? result.get(0) : null;
  	}



    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public void saveAllDssBulkWorkedHistory(List<Pair<WorkedPolicyRequestEvent, X4DssWorkedHistory>> bulkHistory)
    {
        long startTime = System.currentTimeMillis();
        log.debug("call saveAllDssBulkWorkedHistory() |{}|", startTime);
        
        Session session = entityManager.unwrap(Session.class);
        session.setJdbcBatchSize(Constants.BULK_SIZE); // batch size solo per questa Session/tx
        
        List<List<Pair<WorkedPolicyRequestEvent, X4DssWorkedHistory>>> partitions = EamEtlUtils.partition(bulkHistory, Constants.BULK_SIZE);
        for (List<Pair<WorkedPolicyRequestEvent, X4DssWorkedHistory>> partition: partitions)
        {
            
            for (Pair<WorkedPolicyRequestEvent, X4DssWorkedHistory> pair: partition)
            {
                entityManager.persist(pair.getB());
            }
            
            entityManager.flush();
            partition.forEach(x -> {
                x.getA().setDssWorkerId(x.getB().getIdDssWorked().toString());
            });
            
            entityManager.clear();
        }


         long endTime = System.currentTimeMillis();
         log.info("end saveAllDssBulkWorkedHistory() |{}|", endTime - startTime);    
    }

}
