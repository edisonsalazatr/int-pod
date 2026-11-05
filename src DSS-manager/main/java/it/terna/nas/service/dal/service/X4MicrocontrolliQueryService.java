package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Monitor.EErrorMapping;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4MicrocontrolloMigBatch;
import it.terna.nas.service.dal.service.repository.X4MicrocontrolloMigBatchRepository;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class X4MicrocontrolliQueryService
{
	private static final String	ENTITY_UPDATE_MESSAGE		= "Unable to save/update X4MicrocontrolloMigBatch for wonum '%s'";
	private static final String QUERY_BATCH_LOADING 		= "select c from " + X4MicrocontrolloMigBatch.class.getName() + " c WHERE c.status = :status";
	
	
	@Autowired
    private DataAccessService   dataAccessService;
	
	@Autowired
    private X4MicrocontrolloMigBatchRepository   batchRepository;
    
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
  	public List<X4MicrocontrolloMigBatch> loadOrdersFromBatchByStatus(String status) throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call loadOrdersFromBatch({}) |{}|", startTime);		
  		
  	    Map <String, String> parameters = new HashMap<>();
  		parameters.put(ESQLMappingParameter.STATUS.getName(), status);
  		List<X4MicrocontrolloMigBatch> result = dataAccessService.search(X4MicrocontrolloMigBatch.class, 
  																		QUERY_BATCH_LOADING, parameters, 
  																		false, true);
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Search loadOrdersFromBatch found [{}] |{}|", Assorted.isNotEmpty(result) 
  																	? result.size() : 
  																	0, 
  															   endTime - startTime);
  		
  		return result;
  	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
  	public List<X4MicrocontrolloMigBatch> loadBatchByStatus(Long lastId, int pageSize) throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call loadOrdersFromBatch({}) |{}|", startTime);		
  		
  	    
  		List<X4MicrocontrolloMigBatch> result = batchRepository.findByNextBatchNative(lastId, pageSize);
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Search loadOrdersFromBatch found [{}] records. Time elapsed |{}|", Assorted.isNotEmpty(result) 
  																	? result.size() : 
  																	0, 
  															   endTime - startTime);
  		
  		return result;
  	}
    
    @Transactional
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
    
    @Transactional
    @Datasource(datasourceKey = EDatasourceKey.EAM)
  	public boolean updateStatus(Long id, String status) 
  			throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call save() |{}|", startTime);
  		
  		Optional<X4MicrocontrolloMigBatch> record = batchRepository.findById(id);
  		
  		if(record.isPresent()) {
  			X4MicrocontrolloMigBatch row = record.get();
  			row.setStatus(status);
  			dataAccessService.save(row, X4MicrocontrolloMigBatch.class.getName(), ENTITY_UPDATE_MESSAGE, Assorted.notNull(row) ? row.getWonum(): "");
  		} else {
  			log.error(" Record not found for id {}", id);
  		}
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Executing save |{}|", endTime - startTime);
  		
  		return true;
  	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public boolean updateStatusBulk(Long id, String status) throws DataLayerException {
    	long startTime = System.currentTimeMillis();
    	int updated = batchRepository.updateStatusById(id, status);
    	
    	if(updated == 0) {
    		throw new DataLayerException(EErrorMapping.ENTITY_NOT_FOUND, 
					new Exception(String.format("Record not found in table X4_MICROCONTROLLI_MIG_BATCH for id %d", id)),
                    new Object[] { "[X4_MICROCONTROLLI_MIG_BATCH]", String.valueOf(id) });
    	}
    	long endTime = System.currentTimeMillis();
  		log.debug("  - Executing save |{}|", endTime - startTime);
    	return true;
    }
    
  
}
