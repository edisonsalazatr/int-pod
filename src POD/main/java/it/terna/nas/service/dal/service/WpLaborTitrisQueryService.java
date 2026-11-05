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
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.LabTransId;
import it.terna.nas.service.dal.entity.eam.WpLaborTitris;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WpLaborTitrisQueryService {

    @Autowired
    private DataAccessService dataAccessService;

    private final String QUERY_01_A = "SELECT w.WPLABORUID, CASE WHEN w.CRAFT = 'OPERAIO' THEN 'O' WHEN w.CRAFT = 'IMPIEGAT' THEN 'I' WHEN w.CRAFT = 'QUADRO' THEN 'Q' ELSE w.CRAFT END AS CRAFT, w.QUANTITY, p.X3PARENTPERSONGROUP AS X4TITRIS FROM WPLABOR w LEFT JOIN PERSONGROUP p ON p.PERSONGROUP = w.X4TITRIS AND p.X4TIPUDB = 'UI' WHERE w.WONUM = :wonum AND w.X4TITRIS IS NOT NULL";
    private final String QUERY_01_B = "SELECT w.WPLABORUID, CASE WHEN w.CRAFT = 'OPERAIO' THEN 'O' WHEN w.CRAFT = 'IMPIEGAT' THEN 'I' WHEN w.CRAFT = 'QUADRO' THEN 'Q' ELSE w.CRAFT END AS CRAFT, w.QUANTITY, p.X3PARENTPERSONGROUP AS X4TITRIS FROM WORKORDER wo JOIN WPLABOR w ON w.WONUM = wo.WONUM LEFT JOIN PERSONGROUP p ON p.PERSONGROUP = w.X4TITRIS AND p.X4TIPUDB = 'UI' WHERE wo.PARENT = :wonum AND w.X4TITRIS IS NOT NULL";
    private final String QUERY_02 = "SELECT LABTRANSID FROM LABTRANS WHERE EXTERNALREFID = :idconsuntivo AND LABORCODE = :matricola AND REFWO = :refwo";
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<WpLaborTitris> getTitrisListByWonum(String wonum, String haschildren) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getTitrisListByWonum(wonum={}, haschildren={}) |{}|", wonum, haschildren, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
		List<WpLaborTitris> result = null;
		if(haschildren.equals("0"))
			result = dataAccessService.search(WpLaborTitris.class, QUERY_01_A, parameters, true, true);
		else 
			result = dataAccessService.search(WpLaborTitris.class, QUERY_01_B, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getTitrisListByWonum (wonum={}, haschildren={}) found [{}] |{}|", wonum, haschildren, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return result;
	}

    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public Boolean checkConsuntivo(String idconsuntivo, String matricola, String refwo) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call checkConsuntivo(idconsuntivo={}, matricola={}, refwo={}) |{}|", idconsuntivo, matricola, refwo, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.IDCONSUNTIVO.getName(), idconsuntivo);
		parameters.put(ESQLMappingParameter.MATRICOLA.getName(), matricola);
		parameters.put(ESQLMappingParameter.REFWO.getName(), refwo);
		List<LabTransId> result = null;
		result = dataAccessService.search(LabTransId.class, QUERY_02, parameters, true, true);
		long endTime = System.currentTimeMillis();
		log.info("end call checkConsuntivo (idconsuntivo={}, matricola={}, refwo={}) found [{}] |{}|", idconsuntivo, matricola, refwo, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		return Assorted.isEmpty(result) ? true : false;
	}
}
