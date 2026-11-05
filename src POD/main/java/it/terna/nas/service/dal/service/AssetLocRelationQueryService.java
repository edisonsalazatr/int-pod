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
import it.terna.nas.service.dal.entity.eam.AssetLocRelation;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class AssetLocRelationQueryService
{
	private static final String QUERY_01 = "SELECT RAWTOHEX(SYS_GUID()) AS ID, AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID FROM ASSETLOCRELATION AL WHERE AL.TARGETASSETNUM = :codiceOggetto AND AL.SITEID = 'SIT01'";
	private static final String QUERY_02 = "SELECT RAWTOHEX(SYS_GUID()) AS ID, AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID FROM ASSETLOCRELATION AL WHERE AL.TARGETASSETNUM = :codiceOggetto AND AL.SITEID = 'SIT01' AND AL.SOURCEASSETNUM = :codiceOggetto2";
	
    @Autowired
    private DataAccessService   dataAccessService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<AssetLocRelation> getLineaByLevel2(String assetNumLevel2) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getLineaByLevel2(assetNumLevel2={}) |{}|", assetNumLevel2, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName(), assetNumLevel2);
		List<AssetLocRelation> result = dataAccessService.search(AssetLocRelation.class, QUERY_01, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getLineaByLevel2(assetNumLevel2={}) found [{}] |{}|", assetNumLevel2, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return result;
	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<AssetLocRelation> getLineaByLevel2AndRoot(String assetNumLevel2, String assetNumLevel1) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getLineaByLevel2AndRoot(assetNumLevel2={}, assetNumLevel1={}) |{}|", assetNumLevel2, assetNumLevel1, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName(), assetNumLevel2);
		parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName() + "2", assetNumLevel1);
		List<AssetLocRelation> result = dataAccessService.search(AssetLocRelation.class, QUERY_02, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getLineaByLevel2AndRoot(assetNumLevel2={}, assetNumLevel1={}) found [{}] |{}|", assetNumLevel2, assetNumLevel1, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return result;
	}
}
