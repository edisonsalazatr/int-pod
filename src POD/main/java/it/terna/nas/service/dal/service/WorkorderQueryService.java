package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.GeoCallWorkOrder;
import it.terna.nas.service.dal.entity.eam.Workorder;
import it.terna.nas.service.dal.entity.eam.WorkorderAnnulla;
import it.terna.nas.service.dal.entity.eam.WorkorderHierarchyRestricted;
import it.terna.nas.service.dal.entity.eam.WorkorderPlannedDate;
import it.terna.nas.service.material.utility.Catalog;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WorkorderQueryService {

    @Autowired
    private DataAccessService dataAccessService;

    private final String QUERY_01 = "SELECT wo.WONUM, wo.WORKORDERID FROM WORKORDER wo WHERE wo.WONUM = :wonum";

    private final String QUERY_02 = "WITH FATHER AS (SELECT * FROM WORKORDER WHERE wonum = :wonum) SELECT w.*, j.X4INDISPONIBILITA FROM (SELECT * FROM FATHER UNION ALL SELECT * FROM WORKORDER w WHERE w.parent = (SELECT wonum FROM FATHER) AND w.siteid = (SELECT siteid FROM FATHER) AND w.istask = 0) w LEFT JOIN JOBPLAN j ON w.jpnum = j.jpnum AND j.status = 'ACTIVE'";

    private static final String QUERY_03 = "SELECT DISTINCT WONUM, WORKORDERID FROM WORKORDER WHERE parent = :wonum AND siteid = 'SIT01' AND istask = 0";
    
    private final String QUERY_04 = "SELECT wo.TARGSTARTDATE, wo.TARGCOMPDATE, wo.WONUM, wo.WORKORDERID FROM WORKORDER wo WHERE wo.WONUM = :wonum";

    private final String QUERY_05 = "SELECT WONUM, PARENT, STATUS, LEVEL AS livello FROM WORKORDER WHERE LEVEL > 1 START WITH WONUM = :wonum CONNECT BY PRIOR WONUM = PARENT AND ISTASK = 0";
    
    private final String QUERY_06 = "WITH parent_wo AS (SELECT wonum, siteid, x4aggregati FROM workorder WHERE wonum = :wonum AND siteid = :siteid), target_wos AS (SELECT p.wonum, p.siteid FROM parent_wo p UNION ALL SELECT c.wonum, c.siteid FROM workorder c JOIN parent_wo p ON p.x4aggregati = 1 AND c.parent = p.wonum AND c.siteid = p.siteid), flags AS (SELECT MAX(CASE WHEN pg.x4tipudb IN ('UI','TP','TT') THEN 1 ELSE 0 END) AS has_in, MAX(CASE WHEN pg.x4tipudb NOT IN ('UI','TP','TT')  THEN 1 ELSE 0 END) AS has_not_in FROM wplabor wl JOIN target_wos t ON t.wonum  = wl.wonum AND t.siteid = wl.siteid JOIN persongroup pg ON pg.persongroup = wl.x4titris) SELECT wo.wonum, wo.workorderid FROM workorder wo CROSS JOIN flags f WHERE wo.wonum = :wonum AND wo.siteid = :siteid AND f.has_in = 1 AND f.has_not_in = 1";
    
    private final String QUERY_07 = "SELECT WONUM, STATUS, PARENT, NULL AS LIVELLO FROM WOSTATUS WHERE STATUS = 'SCHEDULATO' AND WONUM = :wonum";
    
    private final String QUERY_08 = "SELECT workorderid, wonum FROM WORKORDER WHERE wonum = :wonum AND x4tecmig = 1";
    
    private final String QUERY_09 = "select c from " + WorkorderAnnulla.class.getName() + " c where c.wonum = :wonum";
    
    private final String QUERY_10 = "SELECT WONUM, NULL PARENT, NULL STATUS, NULL AS LIVELLO FROM WORKORDER START WITH WONUM = :wonum CONNECT BY PRIOR PARENT = WONUM ORDER BY LEVEL DESC FETCH FIRST 1 ROW ONLY";
    
    private final String QUERY_11 = "SELECT WONUM, STATUS, PARENT, NULL AS LIVELLO FROM WORKORDER WHERE WONUM = :wonum AND SITEID = :siteid";
    
    private static final String ENTITY_UPDATE_MESSAGE 	= "Unable to update Workorder with wonum '%s'";
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(readOnly = false)
   	public void updateWolo1(String wonum) 
   			throws DataLayerException
   	{
   		long startTime = System.currentTimeMillis();
   		log.debug("call updateWolo1(wonum={}) |{}|", wonum, startTime);		

   		Map <String, String> parameters = new HashMap<>();
   		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
   		List<WorkorderAnnulla> result = dataAccessService.search(WorkorderAnnulla.class, QUERY_09, parameters, false, true);
   		
   		WorkorderAnnulla rowUpdate = null;
   		
   		if (Assorted.isNotEmpty(result)) 
		{
   			rowUpdate = result.get(0);
			rowUpdate.setWolo1(null);
			dataAccessService.save(rowUpdate, WorkorderAnnulla.class.getName(), ENTITY_UPDATE_MESSAGE, rowUpdate.getWonum());
		}
   		
   		long endTime = System.currentTimeMillis();
   		log.info("  - Search updateWolo1 found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
   	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<String> findWonumChildList(String wonum) throws DataLayerException {
    	
		long startTime = System.currentTimeMillis();
		log.debug("call findWonumChildList({}) |{}|", wonum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
		List<Workorder> result = dataAccessService.search(Workorder.class, QUERY_03, parameters, true, true);
		log.info("WonumChildList found ({})", Assorted.isNotEmpty(result) ? result : "NO_RESULT");
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search findWonumChildList found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		List<String> childList = new ArrayList<>();
		if (Assorted.isNotEmpty(result)) {
		    for (Workorder workorder : result) {
		        if (workorder.getWonum() != null) {
		            childList.add(workorder.getWonum());
		        }
		    }
		}

		return childList;
	}
    
    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public List<Workorder> getWorkorderByWonum(String wonum)
            throws DataLayerException {
        long startTime = System.currentTimeMillis();
        log.debug("call getWorkorderByWonum({}) |{}|", wonum, startTime);
        Map<String, String> parameters = new HashMap<>();
        parameters.put(Catalog.ESQLMappingParameter.WONUM.getName(), wonum);
        List<Workorder> result = dataAccessService.search(Workorder.class, QUERY_01, parameters, true, true);
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getWorkorderByWonum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return result;
    }
    
    @Datasource(datasourceKey = Common.EDatasourceKey.EAM)
    public List<WorkorderPlannedDate> getWorkorderPlannedDateByWonum(String wonum)
            throws DataLayerException {
        long startTime = System.currentTimeMillis();
        log.debug("call getWorkorderByWonum({}) |{}|", wonum, startTime);
        Map<String, String> parameters = new HashMap<>();
        parameters.put(Catalog.ESQLMappingParameter.WONUM.getName(), wonum);
        List<WorkorderPlannedDate> result = dataAccessService.search(WorkorderPlannedDate.class, QUERY_04, parameters, true, true);
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getWorkorderByWonum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        return result;
    }
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<GeoCallWorkOrder> getWorkOrderHierarchyByWonum(String wonum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getWorkOrderHierarchyByWonum(wonum={}) |{}|", wonum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
		List<GeoCallWorkOrder> result = dataAccessService.search(GeoCallWorkOrder.class, QUERY_02, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getWorkOrderHierarchyByWonum (wonum={}) found [{}] |{}|", wonum, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		return result;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<WorkorderHierarchyRestricted> getWorkorderSonsHierarchyStatuses(String wonum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getWorkorderHierarchyStatuses(wonum={}) |{}|", wonum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
		List<WorkorderHierarchyRestricted> result = dataAccessService.search(WorkorderHierarchyRestricted.class, QUERY_05, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getWorkorderHierarchyStatuses (wonum={}) found [{}] |{}|", wonum, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		return result;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public WorkorderHierarchyRestricted getWorkorderTopParent(String wonum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getWorkorderTopParent(wonum={}) |{}|", wonum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
		List<WorkorderHierarchyRestricted> result = dataAccessService.search(WorkorderHierarchyRestricted.class, QUERY_10, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getWorkorderTopParent (wonum={}) found [{}] |{}|", wonum, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		if(Assorted.isNotEmpty(result))
			return result.get(0);
		else
			return null;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<Workorder> checkIfStatusChange(String wonum, String siteid) throws DataLayerException {
    	
		long startTime = System.currentTimeMillis();
		log.debug("call checkIfStatusChange(wonum={}, siteid={}) |{}|", wonum, siteid, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
		parameters.put(ESQLMappingParameter.SITEID.getName(), siteid);
		List<Workorder> result = dataAccessService.search(Workorder.class, QUERY_06, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call checkIfStatusChange (wonum={}, siteid={}) found [{}] |{}|", wonum, siteid, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		return result;
	} 
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<WorkorderHierarchyRestricted> checkIfPreviousStatusSchedulato(String wonum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call checkIfPreviousStatusSchedulato({}) |{}|", wonum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
		List<WorkorderHierarchyRestricted> result = dataAccessService.search(WorkorderHierarchyRestricted.class, QUERY_07, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search checkIfPreviousStatusSchedulato found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return result;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public WorkorderHierarchyRestricted getWorkorderStatus(String wonum, String siteid) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getWorkorderStatus(wonum={}, siteid={}) |{}|", wonum, siteid, startTime);

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
		parameters.put(ESQLMappingParameter.SITEID.getName(), siteid);
		List<WorkorderHierarchyRestricted> result = dataAccessService.search(WorkorderHierarchyRestricted.class, QUERY_11, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getWorkorderStatus (wonum={}, siteid={}) found [{}] |{}|", wonum, siteid, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		if(Assorted.isNotEmpty(result))
			return result.get(0);
		else
			return null;
	}
}