package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Values;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.Modello;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ModelloQueryService
{
	private static final String QUERY_MODELLO = """
			SELECT  XC2.X4CATMODELLOID AS MODELLO, xc2.IDMODELLO AS MODELLO_TARGET
				FROM X4CATMODELLOASSET xc, X4CATMODELLO xc2 
				WHERE xc.x4catmodelloassetid  = :x4catmodelloassetid
				AND XC.X4CATMODELLOID  = XC2.X4CATMODELLOID
			""";

	
	@Autowired
    private DataAccessService   dataAccessService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public Modello getModelloByModelloAssetId(String x4catmodelloassetid) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getModelloByModelloAssetId({}) |{}|", x4catmodelloassetid, System.currentTimeMillis());		
		
	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.MODELLO_ASSET_ID.getName(), x4catmodelloassetid);
		
		List<Modello> result = dataAccessService.search(Modello.class, QUERY_MODELLO, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getModelloByModelloAssetId found [{}] |{}|", Assorted.isNotEmpty(result) 
																			? result.size() 
																			: 0, 
																		  endTime - startTime);
		
		return Assorted.isNotEmpty(result)
				? result.get(Values.FIRST_ELEMENT)
				: null;
	}
}