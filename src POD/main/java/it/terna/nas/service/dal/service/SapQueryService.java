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
import it.terna.nas.service.dal.entity.eam.ConnectorSapInfo;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class SapQueryService
{
	private static final String QUERY_01 = "SELECT RAWTOHEX(SYS_GUID()) AS ID, A.STRNO, B.OBJECTNUM, B.HIERARCHYPATH, A.SAPLEVEL FROM X3SAPASSET A, X3SAPTOMAXOBJECT B WHERE A.STRNO = :strno AND A.X3SAPASSETID = B.SAPASSETID AND B.MAXOBJECT = 'ASSET' AND A.HISTORYFLAG = 0 AND B.SITEID = 'SIT01'";
	private static final String QUERY_02 = "SELECT RAWTOHEX(SYS_GUID()) AS ID, A.STRNO, B.OBJECTNUM, B.HIERARCHYPATH, A.SAPLEVEL FROM X3SAPASSET A, X3SAPTOMAXOBJECT B WHERE B.OBJECTNUM = :codiceOggetto AND A.X3SAPASSETID = B.SAPASSETID AND B.MAXOBJECT = 'ASSET' AND A.HISTORYFLAG = 0 AND B.SITEID = 'SIT01'";
	private static final String QUERY_03 = QUERY_02 + " AND (B.HIERARCHYPATH = 'LINEA\\CAMPATA AEREA' OR B.HIERARCHYPATH = 'LINEA\\TRATTA CAVO') ORDER BY B.ROWSTAMP DESC";
	
	@Autowired
    private DataAccessService   dataAccessService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<ConnectorSapInfo> getSapListByAssetNum(String assetNum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getSapListByAssetNum(assetNum={}) |{}|", assetNum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName(), assetNum);
		List<ConnectorSapInfo> result = dataAccessService.search(ConnectorSapInfo.class, QUERY_02, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getSapListByAssetNum (assetNum={}) found [{}] |{}|", assetNum, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return result;
	}

    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<ConnectorSapInfo> getSapListByStrno(String strno) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getSapListByStrno({}) |{}|", strno, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.STRNO.getName(), strno);
		List<ConnectorSapInfo> result = dataAccessService.search(ConnectorSapInfo.class, QUERY_01, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getSapListByStrno found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return result;
	}

    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<ConnectorSapInfo> getSapListL2ByAssetNum(String assetNum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getSapListL2ByAssetNum(assetNum={}) |{}|", assetNum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName(), assetNum);
		List<ConnectorSapInfo> result = dataAccessService.search(ConnectorSapInfo.class, QUERY_03, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getSapListL2ByAssetNum (assetNum={}) found [{}] |{}|", assetNum, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return result;
	}
}
