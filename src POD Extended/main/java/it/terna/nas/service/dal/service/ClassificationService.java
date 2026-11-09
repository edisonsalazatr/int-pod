package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.source.cache.manager.CacheManagerHandler;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.ClassStructureHierarchy;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.material.utility.Catalog.ClassificationType;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
@SuppressWarnings("unchecked")
public class ClassificationService
{
	private static final String QUERY_01 = "SELECT DECODE(C.ANCESTORCLASSID, 'LINEA', 'L', 'STAZIONE', 'S', 'UFFICIO', 'U', 'IMPIANTO FOTOVOLTAICO', 'I') AS TYPE, a.CLASSSTRUCTUREID, A.CLASSIFICATIONID, A.DESCRIPTION FROM CLASSSTRUCTURE A, (SELECT max(HIERARCHYLEVELS) MAX_HIERARCHYLEVELS, CLASSSTRUCTUREID FROM CLASSANCESTOR c GROUP BY CLASSSTRUCTUREID) B, CLASSANCESTOR C WHERE A.CLASSSTRUCTUREID = B.CLASSSTRUCTUREID AND C.CLASSSTRUCTUREID  = B.CLASSSTRUCTUREID ";
	private static final String QUERY_02 = QUERY_01 + " AND C.ANCESTORCLASSID IN ('STAZIONE', 'LINEA', 'IMPIANTO FOTOVOLTAICO', 'UFFICIO') AND A.CLASSSTRUCTUREID = :classstructureid";
	private static final String QUERY_03 = "SELECT C.CLASSSTRUCTUREID, C.DESCRIPTION, C.PARENT, C.CLASSIFICATIONID, C.CLASSSTRUCTUREUID, LEVEL LIVELLO FROM CLASSSTRUCTURE C START WITH CLASSIFICATIONID = :classificationid CONNECT BY PRIOR CLASSSTRUCTUREID = PARENT";
	
    @Autowired
    private CacheManagerHandler cacheManagerHandler;
    
	@Autowired
    private DataAccessService   dataAccessService;
    
    private void findListClassStructureHierarchyQuery(ClassificationType type) 
 			throws DataLayerException
 	{
 		long startTime = System.currentTimeMillis();
 		log.debug("call findListClassStructureHierarchyQuery({}) |{}|", type, System.currentTimeMillis());		

	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CLASSIFICATIONID.getName(), type.getName());
		
		List<ClassStructureHierarchy> result = dataAccessService.search(ClassStructureHierarchy.class, QUERY_03, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search findListClassStructureHierarchyQuery found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		if (Assorted.isNotEmpty(result))
		{
			Map<String, ClassStructureHierarchy> mapStructure 			= result.stream().collect(Collectors.toMap(ClassStructureHierarchy::getClassStructureId, Function.identity()));
			Map<String, List<ClassStructureHierarchy>> mapStructureType = result.stream().collect(Collectors.groupingBy(ClassStructureHierarchy::getClassificationId));
			
			cacheManagerHandler.put(Constants.CACHE_CLASSIFICATION, Constants.CLASSIFICATION_LEVEL + type.getName(), 	mapStructure);
			cacheManagerHandler.put(Constants.CACHE_CLASSIFICATION, Constants.CLASSIFICATION_MAPPING + type.getName(), 	mapStructureType);			
		}		
 	}
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public ClassStructureTipology getClassStructureTipology(String classStructureId) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getClassStructureTipology({}) |{}|", classStructureId, System.currentTimeMillis());		
		
	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classStructureId);
		
		List<ClassStructureTipology> result = dataAccessService.search(ClassStructureTipology.class, QUERY_02, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getClassStructureTipology found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}

	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public Map<String, List<ClassStructureHierarchy>> getMapClassStructure(ClassificationType type) 
			throws DataLayerException
	{
		Cache cache = cacheManagerHandler.cacheManager().getCache(Constants.CACHE_CLASSIFICATION);
		ValueWrapper valueWrapper = cache.get(Constants.CLASSIFICATION_MAPPING + type.getName());
		if (Assorted.isNull(valueWrapper) || Assorted.isNull(valueWrapper.get()))
		{	
			findListClassStructureHierarchyQuery(type);
			valueWrapper = cache.get(Constants.CLASSIFICATION_MAPPING + type.getName());
		}
		
		if (Assorted.notNull(valueWrapper) && Assorted.notNull(valueWrapper.get()))
			return (Map<String, List<ClassStructureHierarchy>>) valueWrapper.get();
		
		return null;
	}
}
