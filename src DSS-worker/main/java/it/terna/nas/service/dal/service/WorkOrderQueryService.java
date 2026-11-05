package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.AssetWithYearTronchi;
import it.terna.nas.service.dal.entity.eam.SequenceValue;
import it.terna.nas.service.dal.entity.eam.WorkOrder;
import it.terna.nas.service.dal.repository.WorkOrderRepository;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkOrderQueryService
{
    private static final String SELECT      = "WONUM,PARENT,STATUS,ASSETNUM,JPNUM,SNECONSTRAINT,FNLCONSTRAINT,TARGSTARTDATE,TARGCOMPDATE,SITEID,X4AGGREGATI,X4POLICYID,X4POLICY,X4TRAINANTE,X4MICROCONTROLLO,X4TRONCONUM,X4NOTE,X4DATAMODELLO,X4ORDINEPREVALENTE,X4TIPOODL,WOCLASS,X4OUTSOURCING,X4NATURALAVORO";
    private static final String QUERY_01    = "select c from " + WorkOrder.class.getName() + " c where c.woclass = 'WORKORDER' and c.assetNum = :assetNum and c.x4DataModello is not null ";
    private static final String QUERY_02    = "SELECT WORKORDERSEQ.NEXTVAL FROM DUAL";
    private static final String QUERY_03    = "SELECT WORKORDERSEQ.NEXTVAL FROM DUAL CONNECT BY LEVEL <= :numberOfValues";
    private static final String QUERY_04    = "SELECT " + SELECT + " FROM WORKORDER T WHERE T.WOCLASS = 'WORKORDER' AND T.ASSETNUM IN (SELECT A.ASSETNUM FROM asset A WHERE (A.assetnum = :assetNum OR A.parent = :assetNum) AND A.ASSETNUM = T.ASSETNUM) AND T.STATUS";
    private static final String QUERY_05    = "SELECT " + SELECT + " FROM WORKORDER T WHERE T.WOCLASS = 'WORKORDER' AND (T.ASSETNUM = :assetNum OR T.ASSETNUM IN (SELECT A.PARENT FROM ASSET A WHERE assetnum = :assetNum)) AND T.STATUS";
    private static final String QUERY_06    = "SELECT " + SELECT + " FROM WORKORDER T WHERE T.WOCLASS = 'WORKORDER' AND (T.ASSETNUM IN (SELECT C.ASSETNUM FROM ASSET A, ASSET B, ASSET C, CLASSSTRUCTURE CA, CLASSSTRUCTURE CC WHERE A.ASSETNUM  = :assetNum AND A.PARENT = B.ASSETNUM AND B.ASSETNUM = C.PARENT AND C.ASSETNUM  = T.ASSETNUM AND CA.CLASSSTRUCTUREID = A.CLASSSTRUCTUREID AND CC.CLASSSTRUCTUREID = C.CLASSSTRUCTUREID AND CA.X4ELEMENTO != CC.X4ELEMENTO) OR T.ASSETNUM = :assetNum) AND T.STATUS";
    private static final String QUERY_07    = """
                                                SELECT ANNO, LISTAGG(B.TRONCO, '|') WITHIN GROUP (ORDER BY B.TRONCO) AS TRONCHI
                                                FROM (
                                                	SELECT DISTINCT TO_NUMBER(TO_CHAR(W.X4DATAMODELLO, 'YYYY')) AS ANNO, P.X4TRONCONUM AS TRONCO
                                                FROM WORKORDER W, WORKORDER P
                                                WHERE W.ASSETNUM = :assetNum
                                                	AND W.STATUS IN ('APERTO', 'SCHEDULABILE', 'SCHEDULATO', 'INOLTRATO', 'AVVIATO', 'ESEGUITO PAR', 'ESEGUIBILE', 'DA VALIDARE')
                                                	AND W.X4DATAMODELLO IS NOT NULL
                                                	AND W.X4POLICY = :id
                                                	AND W.JPNUM = :jpnum
                                                	AND W.PARENT = P.WONUM 
                                                	AND P.X4TRONCONUM IS NOT NULL) B
                                                GROUP BY B.ANNO"""; 

	@Autowired
    private DataAccessService   dataAccessService;
	
	@Autowired
    private WorkOrderRepository   workOrderRepository;
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<AssetWithYearTronchi> findAssetWithYearTronchi(String assetNum, String x4policy, String jpnum) 
    		throws DataLayerException
    {
    	long startTime = System.currentTimeMillis();
    	log.debug("call findAssetWithYearTronchi({}, {}, {}) |{}|", assetNum, x4policy, jpnum, startTime);
    	Map <String, String> parameters = new HashMap<>();
    	parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
    	parameters.put(ESQLMappingParameter.ID.getName(), x4policy);
    	parameters.put(ESQLMappingParameter.ACTIVITY_ID.getName(), jpnum);
    	List<AssetWithYearTronchi> result = dataAccessService.search(AssetWithYearTronchi.class, QUERY_07, parameters, true, true);
    	long endTime = System.currentTimeMillis();
    	log.debug(" end AssetWithYearTronchi |{}|", endTime - startTime);
    	
    	return result;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<WorkOrder> findWorkOrderByAssetAndStatus(String assetNum, List<String> status) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call findWorkOrderByAssetAndStatus({}, {}) |{}|", assetNum, status, startTime);
		
		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
		
		StringBuilder sql = new StringBuilder();
		sql.append(QUERY_01);
		if (Assorted.isNotEmpty(status))
		{	
			sql.append(" and c.status ");

			if (status.size() > 1)
			{
				sql.append(" in (");
				for (int i = 0; i < status.size(); i++)
				{
					parameters.put(ESQLMappingParameter.STATUS.getName() + "_" + i, status.get(i));
					sql.append(":" + ESQLMappingParameter.STATUS.getName() + "_" + i);
					if ((i + 1) < status.size())
						sql.append(", ");
				}
				
				sql.append(" )");
				
			} else {
				sql.append(" = :").append(ESQLMappingParameter.STATUS.getName());
				parameters.put(ESQLMappingParameter.STATUS.getName(), status.get(0));
			}
		}

		List<WorkOrder> result = dataAccessService.search(WorkOrder.class, sql.toString(), parameters, false, true);

		long endTime = System.currentTimeMillis();
		log.debug("  - Executing findWorkOrderByAssetAndStatus |{}|", endTime - startTime);
		
		return result;
	}
	
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<WorkOrder> findWorkOrderByAssetLin3AndStatus(String assetNum, List<String> status) 
    		throws DataLayerException
    {
    	long startTime = System.currentTimeMillis();
    	log.debug("call findWorkOrderByAssetLin3AndStatus({}, {}) |{}|", assetNum, status, startTime);
    	
    	Map <String, String> parameters = new HashMap<>();
    	parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
    	
    	StringBuilder sql = new StringBuilder();
    	sql.append(QUERY_04);
    	if (status.size() > 1)
    	{
    		sql.append(" IN (");
    		for (int i = 0; i < status.size(); i++)
    		{
    			parameters.put(ESQLMappingParameter.STATUS.getName() + "_" + i, status.get(i));
    			sql.append(":" + ESQLMappingParameter.STATUS.getName() + "_" + i);
    			if ((i + 1) < status.size())
    				sql.append(", ");
    		}
    		
    		sql.append(" )");
    		
    	} else {
    		sql.append(" = :").append(ESQLMappingParameter.STATUS.getName());
    		parameters.put(ESQLMappingParameter.STATUS.getName(), status.get(0));
    	}
    
    	List<WorkOrder> result = dataAccessService.search(WorkOrder.class, sql.toString(), parameters, true, true);
    
    	long endTime = System.currentTimeMillis();
    	log.debug("  - Executing findWorkOrderByAssetLin3AndStatus |{}|", endTime - startTime);
    	
    	return result;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<WorkOrder> findWorkOrderByAssetLin4AndStatus(String assetNum, List<String> status) 
    		throws DataLayerException
    {
    	long startTime = System.currentTimeMillis();
    	log.debug("call findWorkOrderByAssetLin4AndStatus({}, {}) |{}|", assetNum, status, startTime);
    	
    	Map <String, String> parameters = new HashMap<>();
    	parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
    	
    	StringBuilder sql = new StringBuilder();
    	sql.append(QUERY_05);
    	if (status.size() > 1)
    	{
    		sql.append(" in (");
    		for (int i = 0; i < status.size(); i++)
    		{
    			parameters.put(ESQLMappingParameter.STATUS.getName() + "_" + i, status.get(i));
    			sql.append(":" + ESQLMappingParameter.STATUS.getName() + "_" + i);
    			if ((i + 1) < status.size())
    				sql.append(", ");
    		}
    		
    		sql.append(" )");
    		
    	} else {
    		sql.append(" = :").append(ESQLMappingParameter.STATUS.getName());
    		parameters.put(ESQLMappingParameter.STATUS.getName(), status.get(0));
    	}
    
    	List<WorkOrder> result = dataAccessService.search(WorkOrder.class, sql.toString(), parameters, true, true);
    
    	long endTime = System.currentTimeMillis();
    	log.debug("  - Executing findWorkOrderByAssetLin4AndStatus |{}|", endTime - startTime);
    	
    	return result;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<WorkOrder> findWorkOrderByAsseStaz4AndStatus(String assetNum, List<String> status) 
    		throws DataLayerException
    {
    	long startTime = System.currentTimeMillis();
    	log.debug("call findWorkOrderByAsseStaz4AndStatus({}, {}) |{}|", assetNum, status, startTime);
    	
    	Map <String, String> parameters = new HashMap<>();
    	parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
    	
    	StringBuilder sql = new StringBuilder();
    	sql.append(QUERY_06);
    	if (status.size() > 1)
    	{
    		sql.append(" in (");
    		for (int i = 0; i < status.size(); i++)
    		{
    			parameters.put(ESQLMappingParameter.STATUS.getName() + "_" + i, status.get(i));
    			sql.append(":" + ESQLMappingParameter.STATUS.getName() + "_" + i);
    			if ((i + 1) < status.size())
    				sql.append(", ");
    		}
    		
    		sql.append(" )");
    		
    	} else {
    		sql.append(" = :").append(ESQLMappingParameter.STATUS.getName());
    		parameters.put(ESQLMappingParameter.STATUS.getName(), status.get(0));
    	}
    
    	List<WorkOrder> result = dataAccessService.search(WorkOrder.class, sql.toString(), parameters, true, true);
    
    	long endTime = System.currentTimeMillis();
    	log.debug("  - Executing findWorkOrderByAsseStaz4AndStatus |{}|", endTime - startTime);
    	
    	return result;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<SequenceValue> getSequenceForAggregate() 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getSequence() |{}|", startTime);      

        List<SequenceValue> result = dataAccessService.search(SequenceValue.class, QUERY_02, null, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getSequence found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
    
        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<SequenceValue> getSequenceForAggregate(String numberOfValues) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getSequence({}) |{}|", numberOfValues, startTime);      

        Map <String, String> parameters = new HashMap<>();
        parameters.put("numberOfValues", numberOfValues);
        List<SequenceValue> result = dataAccessService.search(SequenceValue.class, QUERY_03, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getSequence found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
    
        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public WorkOrder getWorkOrderById(String wonum) throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getWorkOrderById({})|{}|", wonum, startTime);
		
		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
		
		WorkOrder workOrder = dataAccessService.find(WorkOrder.class, parameters, true);
		
		long endTime = System.currentTimeMillis();
        log.debug("  - Search workOrder found [{}] took |{}|", Assorted.notNull(workOrder) ? 1 : 0, endTime - startTime);
        
        return workOrder;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public String getWonumByPolicyAssetAndJpnum(Long x4PolicyId, String jpNum, String assetNum)
            throws DataLayerException
    {
    	long startTime = System.currentTimeMillis();
        log.debug("call getWorkOrderNum({}, {}, {}) |{}|", x4PolicyId, jpNum, assetNum, startTime);

        String wonum = workOrderRepository
                        .findWorkOrderNumByPolicyAssetAndJpnum(x4PolicyId, jpNum, assetNum)
                        .orElse(null);

        long endTime = System.currentTimeMillis();
        log.debug("end search getWorkOrderNum found [{}] |{}|", wonum, endTime - startTime);

        return wonum;
    }
}
