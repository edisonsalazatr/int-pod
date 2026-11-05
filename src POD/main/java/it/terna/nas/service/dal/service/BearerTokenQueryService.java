package it.terna.nas.service.dal.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.source.cache.manager.CacheManagerHandler;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.BearerToken;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@SuppressWarnings("unchecked")
public class BearerTokenQueryService
{
	private static final String QUERY_01 = "SELECT * FROM X4BEARERTOKEN";
	
    @Autowired
    private CacheManagerHandler cacheManagerHandler;
    
	@Autowired
    private DataAccessService   dataAccessService;
    
    private void findBearerTokenUrisQuery() 
 			throws DataLayerException
 	{
 		long startTime = System.currentTimeMillis();
 		log.debug("call findBearerTokenUrisQuery() |{}|", System.currentTimeMillis());		
		
		List<BearerToken> result = dataAccessService.search(BearerToken.class, QUERY_01, null, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search findBearerTokenUrisQuery found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		if (Assorted.isNotEmpty(result))
		{
			Map<String, BearerToken> mapStructure 			= result.stream().collect(Collectors.toMap(BearerToken::getUri, Function.identity()));			
			cacheManagerHandler.put(Constants.CACHE_BEARER_TOKEN, Constants.BEARER_TOKEN_URIS, 	mapStructure);
		}		
 	}

	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public Map<String, BearerToken> getBearerTokenUris() 
			throws DataLayerException
	{
		Cache cache = cacheManagerHandler.cacheManager().getCache(Constants.CACHE_BEARER_TOKEN);
		ValueWrapper valueWrapper = cache.get(Constants.BEARER_TOKEN_URIS);
		if (Assorted.isNull(valueWrapper) || Assorted.isNull(valueWrapper.get()))
		{	
			findBearerTokenUrisQuery();
			valueWrapper = cache.get(Constants.BEARER_TOKEN_URIS);
		}
		
		if (Assorted.notNull(valueWrapper) && Assorted.notNull(valueWrapper.get()))
			return (Map<String, BearerToken>) valueWrapper.get();
		
		return null;
	}
}
