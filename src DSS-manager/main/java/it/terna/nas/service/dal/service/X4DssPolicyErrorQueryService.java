package it.terna.nas.service.dal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4DssPolicyError;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class X4DssPolicyErrorQueryService
{
	private static final String ENTITY_UPDATE_MESSAGE 	= "Unable to update X5DssPolicyError for policy '%s'";
	
	@Autowired
    private DataAccessService   dataAccessService;
    
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public void saveDssPolicyError(X4DssPolicyError row) 
			throws DataLayerException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call X5DssPolicyError({}) |{}|", row, startTime);		
		
		dataAccessService.save(row, X4DssPolicyError.class.getName(), ENTITY_UPDATE_MESSAGE, Assorted.notNull(row.getPolicyId()) ? row.getPolicyId().toString(): "");
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Executing X5DssPolicyError |{}|", endTime - startTime);
	}
}
