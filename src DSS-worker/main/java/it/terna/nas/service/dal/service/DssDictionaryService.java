package it.terna.nas.service.dal.service;

import javax.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.DssDictionary;
import it.terna.nas.service.dal.repository.DssDictionaryRepository;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class DssDictionaryService 
{

    private final DssDictionaryRepository repository;
	
	@Cacheable(cacheManager = Constants.CACHE_MANAGER, 
			   value = Constants.CACHE_DSS_DICTIONARY, 
			   key = "#key", 
			   unless = "#result==null")
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public String getDictionary(String key) {
		
	    log.info(" - Retrieving entry for key='{}'", key);
	    DssDictionary dictionary = null;
	    try {
		    dictionary = repository.findById(key).orElse(null);

			if(dictionary == null) {
				log.warn("  - No value configured with key={}", key);
			} else {
				log.info("  - Found dictionary entry with key='{}' [{}]", key, dictionary.getValue());
			}
			
		} catch (EntityNotFoundException nfe) {
			log.error("  - Error executing query to read data from {}  \n {}", EamEtlUtils.getTableName(DssDictionary.class), nfe);
		}
	    
		return dictionary != null
				? dictionary.getValue()
				: null;
	}

}