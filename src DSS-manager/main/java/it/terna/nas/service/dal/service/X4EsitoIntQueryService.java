package it.terna.nas.service.dal.service;

import static it.terna.nas.service.material.utility.Constants.EAM;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4EsitoInt;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class X4EsitoIntQueryService
{
	private static final String QUERY_01 = "SELECT * FROM X4ESITOINT XE WHERE SOURCE = :source AND DEST = :dest AND WONUM = :wonum AND IDINTERFACCIA = :interf";
	
    @Autowired
    private DataAccessService   dataAccessService;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<X4EsitoInt> getX4Esito(String wonum, String esito, String dest, String interfaccia) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getX4Esito({}, {}, {}, {}) |{}|", wonum, esito, dest, interfaccia, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.SOURCE.getName(), EAM);
		parameters.put(ESQLMappingParameter.DEST.getName(), dest);
		parameters.put(ESQLMappingParameter.INTERFACCIA.getName(), interfaccia);
		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
		List<X4EsitoInt> result = dataAccessService.search(X4EsitoInt.class, QUERY_01, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search getX4Esito found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return result;
	}
}
