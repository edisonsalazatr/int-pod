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
import it.terna.nas.service.dal.entity.eam.ClassAncestor;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class ClassificationQueryService
{
	private static final String QUERY_01 = "SELECT a.CLASSSTRUCTUREID, A.CLASSIFICATIONID, A.DESCRIPTION FROM CLASSSTRUCTURE A WHERE A.CLASSIFICATIONID = :classificationid";
	//private static final String QUERY_02 = "SELECT a.CLASSSTRUCTUREID, A.CLASSIFICATIONID, A.DESCRIPTION FROM CLASSSTRUCTURE A WHERE A.CLASSIFICATIONID IN ";
	private static final String QUERY_03 = "SELECT a.CLASSSTRUCTUREID, A.CLASSIFICATIONID, A.DESCRIPTION FROM CLASSSTRUCTURE A, ASSET B WHERE b.ASSETNUM = :assetNum AND b.CLASSSTRUCTUREID = a.CLASSSTRUCTUREID"; 
	private static final String QUERY_04 = "SELECT a.CLASSSTRUCTUREID, A.CLASSIFICATIONID, A.DESCRIPTION FROM CLASSSTRUCTURE A WHERE A.CLASSSTRUCTUREID IN (SELECT B.CLASSSTRUCTUREID FROM JOBPLAN B WHERE B.JPNUM = :jpnum)"; 
	private static final String QUERY_05 = "SELECT a.CLASSSTRUCTUREID, A.CLASSIFICATIONID, A.DESCRIPTION FROM CLASSSTRUCTURE A WHERE A.CLASSSTRUCTUREID IN (SELECT B.CLASSSTRUCTUREID FROM JOBPLAN B WHERE B.X4MACROATTIVITA = :jpnum)";
	

	
	@Autowired
    private DataAccessService   dataAccessService;
    
	
//    @Datasource(datasourceKey = EDatasourceKey.EAM)
//	public List<String> getClassStructureTipology(List<String> classifications) 
//			throws DataLayerException
//	{
//		long startTime = System.currentTimeMillis();
//		log.debug("call getClassStructureTipology({}) |{}|", classifications, System.currentTimeMillis());		
//		
//		Map <String, String> parameters = new HashMap<>();
//		StringBuilder sql 	= new StringBuilder(QUERY_02);
//		int i     			= 0;
//		int size			= classifications.size();
//		List<String> output	= new ArrayList<>();
//		
//		for (String classification: classifications)
//        {
//            if (i == 0)
//                sql.append(" (");
//            else
//                sql.append(", ");
//            
//            String paramName = ESQLMappingParameter.CLASSIFICATIONID.getName() + "_" + i;
//            parameters.put(paramName, classification);
//            sql.append(":" + paramName);
//            if ((i + 1) >= size)
//                sql.append(")");
//            
//            i++;
//        }
//		
//		List<ClassStructureTipology> result = dataAccessService.search(ClassStructureTipology.class, sql.toString(), parameters, true, true);
//		
//		long endTime = System.currentTimeMillis();
//		log.debug("  - Search getClassStructureTipology found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
//		
//		if (Assorted.isNotEmpty(result))
//		{
//			result.stream().forEach(x -> output.add(x.getClassStructureId()));
//		}
//		
//		return output;
//	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public ClassStructureTipology getClassStructureTipology(String classificationId) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getClassStructureTipology({}) |{}|", classificationId, System.currentTimeMillis());		
		
	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CLASSIFICATIONID.getName(), classificationId);
		
		List<ClassStructureTipology> result = dataAccessService.search(ClassStructureTipology.class, QUERY_01, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getClassStructureTipology found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public ClassStructureTipology getClassStructureTipologyByAsset(String assetNum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getClassStructureTipologyByAsset({}) |{}|", assetNum, System.currentTimeMillis());		
		
	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
		
		List<ClassStructureTipology> result = dataAccessService.search(ClassStructureTipology.class, QUERY_03, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getClassStructureTipologyByAsset found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public ClassStructureTipology getClassStructureTipologyByAttivita(String attivita) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getClassStructureTipologyByAttivita({}) |{}|", attivita, System.currentTimeMillis());		
		
	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.JPNUM.getName(), attivita);
		
		List<ClassStructureTipology> result = dataAccessService.search(ClassStructureTipology.class, QUERY_04, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getClassStructureTipologyByAttivita found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<ClassStructureTipology> getClassStructureTipologyByMacroAttivita(String macroAttivita) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getClassStructureTipologyByMacroAttivita({}) |{}|", macroAttivita, System.currentTimeMillis());       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.JPNUM.getName(), macroAttivita);
        
        List<ClassStructureTipology> result = dataAccessService.search(ClassStructureTipology.class, QUERY_05, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getClassStructureTipologyByMacroAttivita found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }
    
    
    public static final String QUERY_FIND_ANCESTORCLASSID = """
			SELECT * FROM classancestor 
			WHERE classstructureid IN (
				SELECT classstructureid
				FROM asset
				WHERE assetnum = :assetNum)
			AND hierarchylevels = (
				SELECT MAX(hierarchylevels)
				FROM classancestor
				WHERE classstructureid IN (
					SELECT classstructureid
					FROM asset
					WHERE assetnum = :assetNum)
				)
	        """;
    /**
	 * Controlla se l'asset è di tipo stazione
	 *
	 * @param  String identificativo dell'asset
	 * @return Rritorna un {@code Boolean} <br>
	 * True = è stazione <br>
	 * False = non è stazione
	 */
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public Boolean checkIfIsStazione(String assetnum) {

		List<ClassAncestor> ancestors = new ArrayList<>();
		String assetType = "";

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetnum);

		log.info("MicroControlService - checkIfIsStazione - START");
		try {
			ancestors = dataAccessService.search(ClassAncestor.class, QUERY_FIND_ANCESTORCLASSID, parameters, true, true);
		} catch (DataLayerException e) {
			log.error("MicroControlService - checkIfIsStazione - Error during retriving ancestorclassid {}", e);
		}
		if(Assorted.notNull(ancestors)) {
			assetType = ancestors.stream().findFirst().orElse(new ClassAncestor()).getAncestorClassId();			
		}

		log.info("MicroControlService - checkIfIsStazione - Retrived ancestor: {}", assetType);
		log.info("MicroControlService - checkIfIsStazione - END");
		return (Assorted.notNull(assetType)) ? assetType.equals(Constants.CLASS_STAZIONE) : false;

	}

}
