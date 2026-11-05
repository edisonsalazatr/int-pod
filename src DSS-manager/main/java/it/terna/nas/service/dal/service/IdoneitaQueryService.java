package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4TabIdoneita;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class IdoneitaQueryService
{
	
	@Autowired
    private DataAccessService   dataAccessService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public X4TabIdoneita getIdoneitaById(String id) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getIdoneitaById({}) |{}|", id, System.currentTimeMillis());		
		
	    Map <String, String> parameters = new HashMap<>();
	    parameters.put(ESQLMappingParameter.ID.getName(), id);
		
		X4TabIdoneita result = dataAccessService.find(X4TabIdoneita.class, parameters, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getIdoneitaById found [{}] |{}|", Assorted.notNull(result) 
																			? 1 
																			: 0, 
																		  endTime - startTime);
		return result;
	}
}