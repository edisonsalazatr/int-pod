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
	SELECT A2.TABLEVALUE, A2.ASSETNUM, A2.ASSETSPECID, A2.ALNVALUE
	FROM ASSET A, ASSET B, ASSETSPEC A2 
	WHERE A.ASSETNUM  = :assetNum
		AND A.ASSETNUM = B.PARENT
		AND B.ASSETNUM = A2.ASSETNUM 
		AND A2.ASSETATTRID  = 'LIN_TRONCO_ISPEZIONE' 
	    AND A2.TABLEVALUE IS NOT NULL
	""";
	
	private static final String QUERY_1 = """
	SELECT DISTINCT a1.ALNVALUE, a1.ASSETNUM, a1.ASSETSPECID, a1.TABLEVALUE
	FROM ASSETSPEC a1
	WHERE a1.ASSETATTRID = 'LIN_ZONA_AMBIENTALE'
	AND a1.ASSETNUM IN (
	    SELECT DISTINCT a3.ASSETNUM
	    FROM ASSETSPEC a4
	    JOIN ASSET a2 ON a4.ASSETNUM = a2.ASSETNUM
	    JOIN ASSET a3 ON a2.PARENT = a3.ASSETNUM
	    WHERE a4.TABLEVALUE = :tronconum
	    AND a4.ASSETATTRID = 'LIN_TRONCO_ISPEZIONE'
	)
	""";
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<Assetspec> getTroncoByAssetnum(String assetnum) {
		List<Assetspec> result = null;
		try {
			log.info(" - Starting to retrieve tronco by assetnum': {}", assetnum);
			 
		    long startTime = System.currentTimeMillis();

			Map <String, String> parameters = new HashMap<>();
			parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetnum);
			result = dataAccessService.search(Assetspec.class, QUERY_TRONCO_ASSETNUM, parameters, true, true);
			long endTime = System.currentTimeMillis();
			log.debug("  - Search tronco, found [{}] |{}|", Assorted.isNotEmpty(result) 
																			? result.size() 
																			: 0, 
																  endTime - startTime);
		} catch (DataLayerException dle) {
			log.error("  - Error executing query to retrieve tronco by assetnum {} \n {}", assetnum, dle);
		}
	    
		return result != null
				? result
				: new ArrayList<>();
	}
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<Assetspec> getAssetAndZonizzazioneByTronco(String tronconum) {
		List<Assetspec> result = null;
		try {
			log.info(" - Starting getAssetAndZonizzazioneByTronco': {}", tronconum);
			 
		    long startTime = System.currentTimeMillis();

			Map <String, String> parameters = new HashMap<>();
	        parameters.put(ESQLMappingParameter.TRONCO.getName(), tronconum);
			result = dataAccessService.search(Assetspec.class, QUERY_1, parameters, true, true);
			long endTime = System.currentTimeMillis();
			log.debug("  - Search tronco, found [{}] |{}|", Assorted.isNotEmpty(result) 
																			? result.size() 
																			: 0, 
																  endTime - startTime);
		} catch (DataLayerException dle) {
			log.error("  - Error executing query to retrieve asset by tronco {} \n {}", tronconum, dle);
		}
	    
		return result != null
				? result
				: new ArrayList<>();
	}
}
