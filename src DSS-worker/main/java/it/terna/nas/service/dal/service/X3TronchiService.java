package it.terna.nas.service.dal.service;

import java.util.ArrayList;
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
import it.terna.nas.service.dal.entity.eam.X3Tronchi;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X3TronchiService 
{
	@Autowired
    private DataAccessService   dataAccessService;
	
	private static final String QUERY_01 = """
			SELECT ASSETNUM, SAPID, TRONCONUM, X3TRONCHIID
			FROM X3TRONCHI  
	        WHERE TRONCONUM = :tronconum
	""";
	
	private static final String QUERY_02 = """
	        SELECT x.ASSETNUM, x.SAPID, x.TRONCONUM, x.X3TRONCHIID
	        FROM X3TRONCHI x, WORKORDER w  
	        WHERE x.TRONCONUM = w.X4TRONCONUM 
	              AND w.WONUM = :wonum
	              AND w.X4TRONCONUM IS NOT NULL
	""";
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<X3Tronchi> getX3TronchiByTronconum(String tronconum) 
	{
		List<X3Tronchi> result = null;
		try {
		    long startTime = System.currentTimeMillis();
		    log.debug("call getX3TronchiByTronconum({}) |{}|", tronconum, startTime);
			Map <String, String> parameters = new HashMap<>();
			parameters.put(ESQLMappingParameter.TRONCONUM.getName(), tronconum);
			result = dataAccessService.search(X3Tronchi.class, QUERY_01, parameters, true, true);
			long endTime = System.currentTimeMillis();
			log.info("end getX3TronchiByTronconum({}) ==> found [{}] |{}|", tronconum, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		} catch (DataLayerException dle) {
			log.error("  - Error executing query to retrieve x3tronchi by tronconum {} \n {}", tronconum, dle);
		}
	    
		return result != null ? result : new ArrayList<>();
	}
	
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X3Tronchi> getX3TronchiByWonumRoot(String wonum)
    {
        List<X3Tronchi> result = null;
        try {
            long startTime = System.currentTimeMillis();
            log.debug("call getX3TronchiByWonumRoot({}) |{}|", wonum, startTime);
            Map<String, String> parameters = new HashMap<>();
            parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
            result = dataAccessService.search(X3Tronchi.class, QUERY_02, parameters, true, true);
            long endTime = System.currentTimeMillis();
            log.info("end getX3TronchiByWonumRoot({}) ==> found [{}] |{}|", wonum, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        } catch (DataLayerException dle) {
            log.error("  - Error executing query to retrieve x3tronchi by wonum {} \n {}", wonum, dle);
        }

        return result != null ? result : new ArrayList<>();
    }
}
