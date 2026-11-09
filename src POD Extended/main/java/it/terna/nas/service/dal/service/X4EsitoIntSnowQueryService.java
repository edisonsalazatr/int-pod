package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4EsitoIntSnow;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class X4EsitoIntSnowQueryService
{
	private static final String QUERY_01 = "select c from " + X4EsitoIntSnow.class.getName() + " c where c.x4EsitoIntSnowId = :x4esitointsnowid";
	private static final String QUERY_02 = "SELECT * FROM X4ESITOINTSNOW WHERE UNIQUEID = :uniqueid AND PARENT IS NULL";
	private static final String ENTITY_UPDATE_MESSAGE 	= "Unable to update X4EsitoIntSnow with X4EsitoIntSnowId '%s'";
	
    @Autowired
    private DataAccessService   dataAccessService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
   	public X4EsitoIntSnow getListServiceToRestartContentAndUpdate(Long x4esitointsnowid) 
   			throws DataLayerException
   	{
   		long startTime = System.currentTimeMillis();
   		log.debug("call getListServiceToRestartContentAndUpdate() |{}|", startTime);		

   		Map <String, String> parameters = new HashMap<>();
   		parameters.put(ESQLMappingParameter.X4ESITOINTSNOWID.getName(), x4esitointsnowid.toString());
   		List<X4EsitoIntSnow> result = dataAccessService.search(X4EsitoIntSnow.class, QUERY_01, parameters, false, true);
   		
   		X4EsitoIntSnow rowUpdate = null;
   		
   		if (Assorted.isNotEmpty(result)) 
		{
   			rowUpdate = result.get(0);
			rowUpdate.setRetry(rowUpdate.getRetry() + 1);
			dataAccessService.save(rowUpdate, X4EsitoIntSnow.class.getName(), ENTITY_UPDATE_MESSAGE, rowUpdate.getX4EsitoIntSnowId());
		}
   		
   		long endTime = System.currentTimeMillis();
   		log.info("  - Search getListServiceToRestartContentAndUpdate found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
   		
   		return rowUpdate;
   	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public X4EsitoIntSnow getEsitoParent(String uniqueid) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getEsitoParent({}) |{}|", uniqueid, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.UNIQUEID.getName(), uniqueid);
		List<X4EsitoIntSnow> result = dataAccessService.search(X4EsitoIntSnow.class, QUERY_02, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search getEsitoParent found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}
}
