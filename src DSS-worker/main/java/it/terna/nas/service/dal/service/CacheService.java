package it.terna.nas.service.dal.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import it.terna.nas.service.dal.entity.eam.CatalogTipologyModify;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CacheService {

	@Autowired 
	private CacheManager cacheManager;
	
	@Autowired 
	private X4PolicyService x4PolicyService;

	private static final String PREFIX = "policy-";

	
	/**
	 * @param typologyElement
	 * @return will take care of removing an object present from the Redis 
	 * cache using the key passed in input. If the key passed as input is not present, 
	 * the method will in any case behave idempotently. 
	 * The method will be invoked following adjustments to the catalogues.
	 */
	public Boolean evictCache(CatalogTipologyModify typologyElement) {

		Boolean response = false;

		try {

			if (typologyElement == null) {
				return response;
			}

			String typology = typologyElement.getType().getValue();
			String redisKey = typologyElement.getKey();
			Cache cache = cacheManager.getCache(Constants.CACHE_POLICY);

			switch(typology) {
			
				case Constants.POLICY:
					cache.evict(PREFIX + redisKey);
					response = true;
					break;
					
				case Constants.INTERFERENZA:
					log.info("CacheService - Evict cache for typology = {}", typology);
					response = idsByKey(redisKey, typology, cache);
					break;
					
				case Constants.DIFFORMITA:
					log.info("CacheService - Evict cache for typology = {}", typology);
					response = idsByKey(redisKey, typology, cache);
					break;
					
				case Constants.ATTIVITA:
					log.info("CacheService - Evict cache for typology = {}", typology);
					response = idsByKey(redisKey, typology, cache);
					break;
					
				default:
					log.warn("CacheService - Evict cache typology = {} not found", typology);
					break;
			}

		} catch (Exception e) {
			log.error("CacheService - Error during evictCache, reason: {}", e);
		}
		return response;
	}
	
	
	/**
	 * @param redisKey
	 * @param typology
	 * @param cache
	 * @return Retrieve a list of policy ids and evict chache redis typology passed on input
	 */
	private Boolean idsByKey(String redisKey, String typology, Cache cache) {
		
		List<Long> policyIds = x4PolicyService.getIdsByKey(redisKey, typology);
		
		log.info("CacheService - Searching for policies with key = {} and typology = {}", redisKey, typology);
		
        if (policyIds.isEmpty()) {
            log.warn("CacheService - No policies found for key = {} and typology = {}", redisKey, typology);
        } else {
        	for (Long id : policyIds) {
        		cache.evict(PREFIX + id);
        		log.debug("CacheService - Evicted key: {}", PREFIX + id);
			}
        	return true;
        }
		return false;
	}

}
