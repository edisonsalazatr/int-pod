package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4RenameSapIdGeo;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class X4RenameSapIdGeoQueryService
{
	private static final String QUERY_01				= "SELECT * FROM X4RENAMESAPIDGEO WHERE DA_NOTIFICARE_GEOCALL = 'Y'";
	private static final String QUERY_02				= "select c from " + X4RenameSapIdGeo.class.getName() + " c where c.x4RenameSapIdGeoId = :x4RenameSapIdGeoId";
	private static final String ENTITY_UPDATE_MESSAGE 	= "Unable to update X4RenameSapIdGeo with x4renamesapidgeoid '%s'";
	 
    @Autowired
    private DataAccessService   dataAccessService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	@Transactional
	public List<X4RenameSapIdGeo> getObjectsToNotify() 
			throws DataLayerException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getObjectsToNotify() |{}|", startTime);		
		
		List<X4RenameSapIdGeo> result = dataAccessService.search(X4RenameSapIdGeo.class, QUERY_01, null, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search getObjectsToNotify found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return Assorted.isNotEmpty(result) ? result : null;
	}    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
	public void updateNotiDetails(Long x4RenameSapIdGeoId, String flagDaNotificare) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call updateNotiDetails({}, {}) |{}|", x4RenameSapIdGeoId, flagDaNotificare, startTime);		
		
		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.X4RENAMESAPIDGEOID.getName(), String.valueOf(x4RenameSapIdGeoId));
		
		List<X4RenameSapIdGeo> result = dataAccessService.search(X4RenameSapIdGeo.class, QUERY_02, parameters, false, true);
		if (Assorted.isNotEmpty(result)) 
		{
			X4RenameSapIdGeo rowUpdate = result.get(0);
			rowUpdate.setDaNotificareGeocall(flagDaNotificare);
			dataAccessService.save(rowUpdate, X4RenameSapIdGeo.class.getName(), ENTITY_UPDATE_MESSAGE, x4RenameSapIdGeoId);
		}
		
		long endTime = System.currentTimeMillis();
		log.debug("  - End updateNotiDetails |{}|", endTime - startTime);
	}
}
