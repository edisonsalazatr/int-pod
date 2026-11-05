package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4TabEccezione;
import it.terna.nas.service.dal.entity.eam.X4TabIdoneita;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class IdoneitaService 
{

    @Autowired
    private DataAccessService   dataAccessService;
	
	private static final String QUERY_IDONEITA = """
	            SELECT IDO.X4TABIDONEITAID, IDO.STATOIDONEITA, C.CLASSIFICATIONID AS TIPOLOGIAASSET 
	            FROM X4TABIDONEITA IDO, CLASSSTRUCTURE C 
	            WHERE C.CLASSIFICATIONID = :assetType AND C.X4ELEMENTO = IDO.TIPOLOGIAASSET""";
	
	private static final String QUERY_EXCEPTIONS = """
	            SELECT 
                    ecc.X4TABECCEZIONIID,
                    ecc.STATOIDONEO,
                    ecc.IDATTIVITA_EXT,
                    ecc.IDATTIVITA,
                    ecc.TIPOLOGIAASSET,
                    ecc.DESCRIPTION,
                    ecc.HASLD,
                    ecc.ROWSTAMP
                FROM 
                    x4tabeccezioni ecc
                WHERE 
                    ecc.IDATTIVITA_EXT = :macroActivityId
        """;
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	@Cacheable(cacheManager = Constants.CACHE_MANAGER, 
	   value = Constants.CACHE_IDONEITA, 
	   key = "#classificationId", 
	   unless = "#result==null")
	public List<X4TabIdoneita> loadIdoneitaByClassificationId(String classificationId) {
		Assert.hasLength(classificationId, "classificationId is null or empty");
		
		log.debug("  list of items not found in cache, attempt to load them from db");
		List<X4TabIdoneita> result = null;
		try {
			log.debug(" - Starting to retrieve data from X4TabIdoneita by type='{}'", classificationId);
			 
		    long startTime = System.currentTimeMillis();

			Map <String, String> parameters = new HashMap<>();
			parameters.put(ESQLMappingParameter.ASSET_TYPE.getName(), classificationId);
			result = dataAccessService.search(X4TabIdoneita.class, QUERY_IDONEITA, parameters, true, true);
			long endTime = System.currentTimeMillis();
			log.debug("  - Search idoneita: found [{}] |{}|", Assorted.isNotEmpty(result) 
																		? result.size() 
																		: 0, 
															  endTime - startTime);
		} catch (DataLayerException dle) {
			log.error("  - Error executing query to retrieve idoneita associations by type of asset {} \n {}", classificationId, dle);
		}
	    
		return result != null
				? result
				: new ArrayList<>();
	}
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	@Cacheable(cacheManager = Constants.CACHE_MANAGER, 
	           value = Constants.CACHE_ECCEZIONI, 
	           key = "#jpnum", 
	           unless = "#result==null")
	public List<X4TabEccezione> loadEccezioniByActivity(String jpnum) 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call loadEccezioni({}) |{}|", jpnum, startTime);
		Assert.hasLength(jpnum, "jpnum is null or empty");
		
		log.debug("  list of items not found in cache, attempt to load them from db");
		List<X4TabEccezione> result = null;
		try {
			Map <String, String> parameters = new HashMap<>();
			parameters.put(ESQLMappingParameter.MACROATTIVITA.getName(), jpnum);
			result = dataAccessService.search(X4TabEccezione.class, QUERY_EXCEPTIONS, parameters, true, true);
			long endTime = System.currentTimeMillis();
			log.debug("  - Search X4TabEccezioni: found [{}] |{}|", Assorted.isNotEmpty(result) 
																			? result.size() 
																			: 0, 
																	endTime - startTime);
		} catch (DataLayerException dle) {
			log.error("  - Error executing query to read data from X4TabEccezioni by jpnum {} \n {}", jpnum, dle);
		}

        return result != null ? result : new ArrayList<>();
	}
}
