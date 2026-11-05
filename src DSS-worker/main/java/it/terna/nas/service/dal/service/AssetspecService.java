package it.terna.nas.service.dal.service;

import java.util.ArrayList;
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
import it.terna.nas.service.dal.entity.eam.Assetspec;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetspecService {
	
	@Autowired
    private DataAccessService   dataAccessService;
	
	private static final String QUERY_TRONCO_ASSETNUM = """
	SELECT A2.TABLEVALUE, A2.ASSETNUM, A2.ASSETSPECID
	FROM ASSET A, ASSET B, ASSETSPEC A2 
	WHERE A.ASSETNUM  = :assetNum
		AND A.ASSETNUM = B.PARENT
		AND B.ASSETNUM = A2.ASSETNUM 
		AND A2.ASSETATTRID  = 'LIN_TRONCO_ISPEZIONE' 
	    AND A2.TABLEVALUE IS NOT NULL
	""";
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<Assetspec> getTroncoByAssetnum(String assetnum, String troncoNum) 
	{
		List<Assetspec> result = null;
		try {
            long startTime = System.currentTimeMillis();
		    log.debug("call getTroncoByAssetnum({}, {}) |{}|", assetnum, troncoNum, startTime);

		    StringBuilder sql = new StringBuilder(QUERY_TRONCO_ASSETNUM);
		    
			Map <String, String> parameters = new HashMap<>();
			parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetnum);
			
			if (Assorted.isNotEmpty(troncoNum))
			{
			    parameters.put(ESQLMappingParameter.TRONCONUM.getName(), troncoNum);
			    sql.append(" AND A2.TABLEVALUE = :tronconum");
			}
			
			result = dataAccessService.search(Assetspec.class, sql.toString(), parameters, true, true);
			long endTime = System.currentTimeMillis();
			log.info("end getTroncoByAssetnum({}, {}) [{}] |{}|", assetnum, troncoNum, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
			
		} catch (DataLayerException dle) {
			log.error("  - Error executing query to retrieve tronco by assetnum {} \n {}", assetnum, dle);
		}
	    
		return result != null
				? result
				: new ArrayList<>();
	}
}
