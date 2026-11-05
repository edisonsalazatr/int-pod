package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.ClassStructure;
import it.terna.nas.service.dal.entity.eam.ClassStructureLevel;
import it.terna.nas.service.dal.entity.eam.ClassStructureLevel2;
import it.terna.nas.service.dal.repository.ClassStructureRepository;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ClassStructureService 
{   
    private static final String QUERY_01 = """
                SELECT LEVEL, c.classstructureid FROM CLASSSTRUCTURE c 
                START WITH classstructureid  = :classstructureid 
                CONNECT BY PRIOR  classstructureid = parent """;

    private static final String QUERY_02 = """
                SELECT LEVEL, A.CLASSSTRUCTUREID, A.CLASSIFICATIONID, 
                            (SELECT DECODE(C.ANCESTORCLASSID, 'LINEA', 'L', 'STAZIONE', 'S')  
                             FROM CLASSANCESTOR C 
                             WHERE C.ANCESTORCLASSID IN ('STAZIONE', 'LINEA') 
                                AND A.CLASSSTRUCTUREID =   c.CLASSSTRUCTUREID 
                                AND ROWNUM = 1) AS "TYPE" 
                FROM CLASSSTRUCTURE A  
                START WITH A.classificationid  in ('STAZIONE', 'LINEA')
                CONNECT BY PRIOR  A.CLASSSTRUCTUREID = A.PARENT """;
    
    @Autowired
    private DataAccessService   dataAccessService;

    @Autowired 
    private ClassStructureRepository repository;
    
    /**
     * @param policyId
     * @return the policy associated to policyId
     */
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Cacheable(cacheManager = Constants.CACHE_MANAGER, value = Constants.CACHE_CLASSSTRUCTURE_LEVEL, key = "#root.methodName+#classification", unless = "#result==null")
	public ClassStructure getByClassification(String classification) 
    {
        log.debug(" - Searching ClassStructure Table for item with classification={}", classification);
        Assert.notNull(classification, "classification is null");
        return repository.findByClassification(classification);
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Cacheable(cacheManager = Constants.CACHE_MANAGER, value = Constants.CACHE_CLASSSTRUCTURE_LEVEL, key = "#root.methodName+#classification", unless = "#result==null OR #result.isEmpty()")
    public List<ClassStructure> getByClassificationList(String classification) 
    {
        log.debug(" - Searching ClassStructure Table for item with classification={}", classification);
        Assert.notNull(classification, "classification is null");
        return repository.findByClassificationList(classification);
    }
    
    
    @Cacheable(cacheManager = Constants.CACHE_MANAGER, 
           value = Constants.CACHE_CLASSSTRUCTURE_LEVEL, 
           key = "#root.methodName", 
           unless = "#result==null")
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public Map<String, Integer> buildLevelMap() {
        
        Map<String, Integer> map = new HashMap<>();
        
        try {
            log.debug(" - Starting to retrieve levels");
            ClassStructure classStazione = getByClassification(Constants.CLASS_STAZIONE);
            ClassStructure classLinea = getByClassification(Constants.CLASS_LINEA);
            long startTime = System.currentTimeMillis();

            Map <String, String> parameters = new HashMap<>();
            parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classLinea.getClassStructureId());
            List<ClassStructureLevel> levelLinee = dataAccessService.search(ClassStructureLevel.class, QUERY_01, parameters, true, true);
            
            if(Assorted.isNotEmpty(levelLinee)) {
                levelLinee.forEach(struct -> map.put(struct.getClassStructureId(), struct.getLevel()));
            }
            
            parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classStazione.getClassStructureId());
            List<ClassStructureLevel> levelStazioni = dataAccessService.search(ClassStructureLevel.class, QUERY_01, parameters, true, true);
            
            if(Assorted.isNotEmpty(levelStazioni)) {
                levelStazioni.forEach(struct -> map.put(struct.getClassStructureId(), struct.getLevel()));
            }
            
            long endTime = System.currentTimeMillis();
            log.debug("  - Loading level for lines and stations took [{}]",  
                                                                    endTime - startTime);
        } catch (DataLayerException dle) {
            log.error("  - Error executing query to read data from CLASSSTRUCTURE  \n {}", dle);
        }
        
        return map;
    }


    @Cacheable(cacheManager = Constants.CACHE_MANAGER, value = Constants.CACHE_CLASSSTRUCTURE_LEVEL, key = "#root.methodName", unless = "#result==null OR #result.isEmpty()")
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public Map<String, ClassStructureLevel2> classStructureMap() 
    {
        Map<String, ClassStructureLevel2> map = new HashMap<>();
        
        try {
            log.debug(" - Starting to retrieve levels");
            long startTime = System.currentTimeMillis();
    
            List<ClassStructureLevel2> levelLineeStazioni = dataAccessService.search(ClassStructureLevel2.class, QUERY_02, null, true, true);
            
            if (Assorted.isNotEmpty(levelLineeStazioni))
                levelLineeStazioni.forEach(struct -> map.put(struct.getClassStructureId(), struct));
            
            long endTime = System.currentTimeMillis();
            log.debug("  - Loading level for lines and stations took [{}]",  endTime - startTime);
        } catch (DataLayerException dle) {
            log.error("  - Error executing query to read data from CLASSSTRUCTURE  \n {}", dle);
        }
        
        return map;
    }
}
