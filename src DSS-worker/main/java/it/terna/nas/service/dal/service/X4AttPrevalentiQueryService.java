package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4AttPrevalenti;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class X4AttPrevalentiQueryService
{
	private static final String QUERY_01 = """
	        SELECT xa.* FROM X4ATTPREVALENTI xa, JOBPLAN j  
	        WHERE xa.JPNUM = j.JPNUM 
	            AND j.PLUSCREVNUM = xa.PLUSCREVNUM 
	            AND j.STATUS = 'ACTIVE'
	            AND XA.ATTIVITAPREV IS NOT NULL
	            AND XA.JPNUM IN 
	        """;

	private final DataAccessService dataAccessService;
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<X4AttPrevalenti> findAttPrevalentiByJpNum(List<String> jpnum) 
			throws DataLayerException
	{
		if (Assorted.isEmpty(jpnum))
			return new ArrayList<>();
		
		long startTime = System.currentTimeMillis();
		log.debug("call findAttPrevalentiByJpNum({}) |{}|", jpnum, startTime);
		
		Map <String, String> parameters = new HashMap<>();
		StringBuilder sql = new StringBuilder();
		sql.append(QUERY_01);
		sql.append(" (");

		for (int i = 0; i < jpnum.size(); i++)
		{
			parameters.put(ESQLMappingParameter.ACTIVITY_ID.getName() + "_" + i, jpnum.get(i));
			sql.append(":" + ESQLMappingParameter.ACTIVITY_ID.getName() + "_" + i);
			if ((i + 1) < jpnum.size())
				sql.append(", ");
		}
				
		sql.append(" )");
		List<X4AttPrevalenti> result = dataAccessService.search(X4AttPrevalenti.class, sql.toString(), parameters, true, true);

		long endTime = System.currentTimeMillis();
		log.debug("  - Executing findAttPrevalentiByJpNum |{}|", endTime - startTime);
		
		return result;
	}
}