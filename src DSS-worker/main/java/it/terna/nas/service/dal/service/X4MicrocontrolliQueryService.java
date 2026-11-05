package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4MicrocontrolloMigBatch;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class X4MicrocontrolliQueryService
{
	private static final String	ENTITY_UPDATE_MESSAGE		= "Unable to save/update X4MicrocontrolloMigBatch for wonum '%s'";
	
	@Autowired
    private DataAccessService   dataAccessService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
  	public boolean save(X4MicrocontrolloMigBatch row) 
  			throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call save() |{}|", startTime);		
  		
  		dataAccessService.save(row, X4MicrocontrolloMigBatch.class.getName(), ENTITY_UPDATE_MESSAGE, Assorted.notNull(row) ? row.getWonum(): "");
  				
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Executing save |{}|", endTime - startTime);
  		
  		return true;
  	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public X4MicrocontrolloMigBatch getById(String batchId) {
		Assert.hasLength(batchId, "batchId is null or empty");
		
		log.debug("  list of items not found in cache, attempt to load them from db");
		X4MicrocontrolloMigBatch result = null;
		try {
			log.debug(" - Starting to retrieve data from X4MicrocontrolloMigBatch by id='{}'", batchId);
			 
		    long startTime = System.currentTimeMillis();
		    
			Map <String, String> parameters = new HashMap<>();
			parameters.put(ESQLMappingParameter.ID.getName(), batchId);
			result = dataAccessService.find(X4MicrocontrolloMigBatch.class, parameters);
			long endTime = System.currentTimeMillis();
			log.debug("  - Search X4TabEccezioni: found [{}] |{}|", Assorted.notNull(result) 
																			? 1 
																			: 0, 
																	endTime - startTime);
		} catch (DataLayerException dle) {
			log.error("  - Error executing query to read data from X4MicrocontrolloMigBatch by id {} \n {}", batchId, dle);
		}
	    
		return result;
	}
    
  
}
