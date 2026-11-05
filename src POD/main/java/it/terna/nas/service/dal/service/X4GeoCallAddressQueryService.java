package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.dto.ParameterData;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.RadiceList;
import it.terna.nas.service.dal.entity.eam.X4GeoCallAddress;
import it.terna.nas.service.dal.entity.eam.X4GeoCallAddressRestricted;
import it.terna.nas.service.dal.service.repository.X4GeoCallAddressRepository;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4GeoCallAddressQueryService
{    
	private static final String QUERY_01   = "SELECT CODICE_OGGETTO, NULL CODICE_RADICE, NULL CODICE_RADICE2 FROM X4GEOCALLADDRESS WHERE TIPO_OGGETTO = :tipoOggetto";
	private static final String QUERY_02   = "SELECT B.RADICE FROM (SELECT :codiceOggetto AS RADICE FROM DUAL UNION SELECT DECODE(CODICE_RADICE, :codiceOggetto, CODICE_RADICE2, CODICE_RADICE) FROM X4GEOCALLADDRESS xg WHERE xg.CODICE_RADICE = :codiceOggetto OR xg.CODICE_RADICE2 = :codiceOggetto) B WHERE B.RADICE IS NOT NULL";
	private static final String STORED_PROCEDURE_01 = "PKG_X4GEOCALLADDRESS.MAIN";
	
    @Autowired
    private DataAccessService   dataAccessService;

    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private X4GeoCallAddressRepository x4GeoCallAddressRepository;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<X4GeoCallAddressRestricted> getLineOrStationObjects(String tipoOggetto) 
			throws DataLayerException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getLineOrStationObjects() |{}|", System.currentTimeMillis());		
		
		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.TIPOOGGETTO.getName(), tipoOggetto);

		List<X4GeoCallAddressRestricted> result = dataAccessService.search(X4GeoCallAddressRestricted.class, QUERY_01, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search getLineOrStationObjects found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return Assorted.isNotEmpty(result) ? result : null;
	}
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<String> getRootCodes(String codiceOggetto) 
			throws DataLayerException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getRootCodes({}) |{}|", codiceOggetto, System.currentTimeMillis());		
		
		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName(), codiceOggetto);

		List<RadiceList> result = dataAccessService.search(RadiceList.class, QUERY_02, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search getRootCodes found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		if(Assorted.isNotEmpty(result)) 
		{
			List<String> returningRoots = new ArrayList<>();
			for(RadiceList root : result) 
			{
				returningRoots.add(root.getRadice());
			}
			return returningRoots;
		}
		else 
			return null;
	}
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(readOnly = true)
	public List<X4GeoCallAddress> getHierarchy(List<String> rootCodes) 
			throws DataLayerException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getHierarchy({}) |{}|", rootCodes, System.currentTimeMillis());		
		List<X4GeoCallAddress> result = new ArrayList<>();
		try (Stream<X4GeoCallAddress> items = x4GeoCallAddressRepository.getHierarchy(rootCodes))
		{
		    Iterator<X4GeoCallAddress> iterator = items.iterator();
		    while (iterator.hasNext()) 
            {
		        X4GeoCallAddress row = iterator.next();
                entityManager.detach(row);
                result.add(row);
            }
		}
	
		long endTime = System.currentTimeMillis();
		log.info("  - Search getHierarchy found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return Assorted.isNotEmpty(result) ? result : null;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public Boolean executeX4GeoCallAddressStoredProcedure() 
			throws DataLayerException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call executeX4GeoCallAddressStoredProcedure() |{}|", System.currentTimeMillis());		
		
		Boolean result = false;
		
		Map<String, ParameterData> parameters = new HashMap<>();

		result = dataAccessService.executeStoredProcedure(STORED_PROCEDURE_01, parameters);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Executing X4GeoCallAddressStoredProcedure result obtained [{}] |{}|", result, endTime - startTime);
	
		return result;
	}
}
