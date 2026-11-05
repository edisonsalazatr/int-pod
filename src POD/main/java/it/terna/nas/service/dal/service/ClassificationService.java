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
import it.terna.nas.service.dal.entity.eam.SequenceCodiceGruppo;
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
	private static final String	SEQUENCE = "SELECT SEQ_CODICE_GRUPPO.NEXTVAL FROM dual";
	
    @Autowired
    private CacheManagerHandler cacheManagerHandler;
    
	@Autowired
    private DataAccessService   dataAccessService;
    
    private void findListClassStructureHierarchyQuery(ClassificationType type) 
 			throws DataLayerException
 	{
 		long startTime = System.currentTimeMillis();
 		log.debug("call findListClassStructureHierarchyQuery(type={}) |{}|", type, startTime);		

	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CLASSIFICATIONID.getName(), type.getName());
		
		List<ClassStructureHierarchy> result = dataAccessService.search(ClassStructureHierarchy.class, QUERY_03, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call findListClassStructureHierarchyQuery(type={}) found [{}] |{}|", type, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
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
		log.debug("call getClassStructureTipology(classStructureId={}) |{}|", classStructureId, startTime);		
		
	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classStructureId);
		
		List<ClassStructureTipology> result = dataAccessService.search(ClassStructureTipology.class, QUERY_02, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getClassStructureTipology(classStructureId={}) found [{}] |{}|", classStructureId, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<ClassStructureTipology> getListClassStructureTypology(ClassificationType... types) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getListClassStructureTypology(types={}) |{}|", types, System.currentTimeMillis());		

		Map <String, String> parameters = new HashMap<>();
		ClassificationType [] typesSearch = types;
		if (Assorted.isEmpty(types))
			typesSearch = ClassificationType.values();
		
		StringBuilder sql = new StringBuilder(QUERY_01);
		sql.append(" AND C.ANCESTORCLASSID IN (");
		for (int i = 0; i < typesSearch.length; i++)
		{
			parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName() + "_" + i, typesSearch[i].getName());
			sql.append(":" + ESQLMappingParameter.CLASSSTRUCTUREID.getName() + "_" + i);
			if ((i + 1) < typesSearch.length)
				sql.append(", ");
		}
		
		sql.append(" )");
		
		List<ClassStructureTipology> result = dataAccessService.search(ClassStructureTipology.class, sql.toString(), parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getListClassStructureTypology(types={}) found [{}] |{}|", types, Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		return result;
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


	@Datasource(datasourceKey = EDatasourceKey.EAM)
    public Map<String, ClassStructureHierarchy> getMapClassStructureHierarchy(ClassificationType type) 
 			throws DataLayerException
 	{
    	Cache cache = cacheManagerHandler.cacheManager().getCache(Constants.CACHE_CLASSIFICATION);
    	ValueWrapper valueWrapper = cache.get(Constants.CLASSIFICATION_LEVEL + type.getName());
    	if (Assorted.isNull(valueWrapper) || Assorted.isNull(valueWrapper.get()))
    		findListClassStructureHierarchyQuery(type);
    	
    	valueWrapper = cache.get(Constants.CLASSIFICATION_LEVEL + type.getName());
    	
    	if (Assorted.notNull(valueWrapper) && Assorted.notNull(valueWrapper.get()))
    		return (Map<String, ClassStructureHierarchy>) valueWrapper.get();
    	
    	return null;
 	}
    
    
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public SequenceCodiceGruppo getSequenceCodiceGruppo() 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getSequenceCodiceGruppo() |{}|", System.currentTimeMillis());		
		
		List<SequenceCodiceGruppo> result = dataAccessService.search(SequenceCodiceGruppo.class, SEQUENCE, null, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("end call getSequenceCodiceGruppo () found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}
}
