package it.terna.nas.service.dal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.material.utility.Catalog.CatalogTipologyType;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CacheService {

	@Autowired 
	private CacheManager cacheManager;
	
	public void evictCache(String cacheName, String key, CatalogTipologyType type) 
	{
	    log.debug("call evictCache({}, {}, {})", cacheName, key, type);		
	    
	    if (Assorted.isEmpty(cacheName) || Assorted.isEmpty(key) || Assorted.isNull(type))
			return;
		
		Cache cache = cacheManager.getCache(cacheName);

		if (Assorted.notNull(cache))
		{
			switch (type) 
			{
				case ASSET: cache.evict(key);
					break;
				case ATTIVITA: cache.clear();
					break;
				case ECCEZIONE: cache.clear();
        			break;
				case IDONEITA: cache.clear();
            		break;
				case lIVELLO_PERIODICHE: cache.clear();
        			break;
				case MODELLO: cache.evict(key);
					break;	
                case POLICY: cache.evict(Constants.CACHE_POLICY_KEY_PREFIX + key);
                	break;
                
				default: log.error("Unhandled cache evict: {}", type);
			}
		
		} else
		    log.error("evictCache: cache is null");
	}
}
