package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.ElementoIndisponibileAgile;
import it.terna.nas.service.dal.entity.eam.FlagRichiestaIndisponibilita;
import it.terna.nas.service.dal.entity.eam.RichiestaIndisponibilitaEsistente;
import it.terna.nas.service.dal.entity.eam.SediTecnicheInterferenti;
import it.terna.nas.service.dal.entity.eam.SediTecnicheInterferentiLinee;
import it.terna.nas.service.dal.entity.eam.SediTecnicheInterferentiStazioni;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AgileQueryService {
	
	@Autowired
    private DataAccessService   dataAccessService;
	
	//Elemento di Indisponibilita
	private static final String QUERY_01 = "WITH start_depth AS ( SELECT MAX(hierarchylevels) AS max_depth FROM assetancestor WHERE assetnum = :assetNum ) SELECT TABLEVALUE, ASSETNUM FROM ( SELECT a.ASSETNUM, asp.TABLEVALUE, ROW_NUMBER() OVER ( ORDER BY aa.hierarchylevels ASC ) AS rank FROM assetancestor aa CROSS JOIN start_depth sd JOIN asset a ON aa.ancestor = a.assetnum OR aa.assetnum = a.ancestor JOIN classstructure cs ON a.classstructureid = cs.classstructureid LEFT JOIN ASSETSPEC asp ON asp.ASSETNUM = a.assetnum AND ( asp.assetattrid = 'LIN_ELEMENTO_INDISP' OR ( asp.assetattrid = 'STZ_ELEMENTO_INDISP' AND (sd.max_depth - aa.hierarchylevels) IN (2, 3) ) ) WHERE (aa.assetnum = :assetNum  OR aa.ancestor = :assetNum ) AND cs.classificationid IN ('CAVI','REATTORE','STALLO','LINEA','SBARRA','PHASE SHIFTING TRANSFORMER','TRASFORMATORE','CONDUTTORI','STAZIONE','BATTERIA CONDENSATORI','BOOSTER','TRASFORMATORE AT/BT') ) combined_results WHERE TABLEVALUE IS NOT NULL ORDER BY rank FETCH FIRST 1 ROWS ONLY";
	private static final String ALT_QUERY_01 = "SELECT TABLEVALUE, ASSETNUM FROM assetspec WHERE ASSETATTRID IN ('LIN_ELEMENTO_INDISP', 'STZ_ELEMENTO_INDISP') AND tablevalue IS NOT NULL AND assetnum IN (SELECT assetnum FROM assetancestor WHERE ancestor IN (SELECT targetassetnum FROM assetlocrelation WHERE x3sapid IN (SELECT SUBSTR(x3sapid, 1, 16) FROM assetlocrelation WHERE targetassetnum IN (SELECT targetassetnum FROM assetlocrelation WHERE assetrelationnum = 'INCLUDES' AND targetassetnum IN (SELECT ancestor FROM assetancestor WHERE assetnum = :assetNum OR ancestor = :assetNum )))))";
	private static final String ALT_QUERY_02 = "SELECT TABLEVALUE, ASSETNUM FROM assetspec WHERE ASSETATTRID IN ('LIN_ELEMENTO_INDISP', 'STZ_ELEMENTO_INDISP') AND tablevalue IS NOT NULL AND assetnum IN (SELECT assetnum FROM assetancestor WHERE assetnum IN (SELECT sourceassetnum FROM assetlocrelation WHERE x3sapid IN (SELECT SUBSTR(x3sapid, 1, 16) FROM assetlocrelation WHERE targetassetnum IN (SELECT targetassetnum FROM assetlocrelation WHERE assetrelationnum = 'INCLUDES' AND targetassetnum IN (SELECT ancestor FROM assetancestor WHERE assetnum = :assetNum OR ancestor = :assetNum )))))";
	
	//Sede Tecnica di Indisponibilità padre
	private static final String QUERY_02 = "SELECT TABLEVALUE, ASSETNUM FROM assetspec WHERE ( assetnum IN ( SELECT sourceassetnum FROM assetlocrelation WHERE targetassetnum = :ancestor OR targetassetnum = :assetNum ) AND assetattrid = 'LIN_ELEMENTO_INDISP' ) OR (( assetnum = :ancestor OR ( assetnum = :assetNum AND :ancestor IS NULL )) AND assetattrid = 'STZ_ELEMENTO_INDISP' )";
	
	//Asset Interferenti - Query 1
	private static final String QUERY_03 = "SELECT TABLEVALUE, ASSETNUM FROM assetspec WHERE ASSETATTRID = 'LIN_ELEMENTO_INDISP' AND assetnum IN (SELECT assetnum FROM asset WHERE x3sapid IN (SELECT tablevalue FROM assetspec WHERE assetattrid = 'LIN_SAPID_LINEA_INT' AND assetnum IN (SELECT assetnum FROM assetancestor WHERE assetnum = :assetNum OR ancestor IN ( :assetNum , :ancestor ) UNION SELECT assetnum FROM assetancestor WHERE ancestor IN (SELECT assetnum FROM asset WHERE assetnum IN (SELECT targetassetnum FROM assetlocrelation WHERE x3sapid IN (SELECT SUBSTR(x3sapid, 1, 16) FROM assetlocrelation WHERE targetassetnum IN ( :ancestor , :assetNum )))) UNION SELECT assetnum FROM assetancestor WHERE ancestor IN (SELECT targetassetnum FROM assetlocrelation WHERE assetrelationnum='INCLUDES' AND sourceassetnum = :assetNum ) UNION SELECT assetnum FROM assetancestor WHERE ancestor IN (SELECT targetassetnum FROM assetlocrelation WHERE x3sapid IN (SELECT SUBSTR(x3sapid, 1, 16) FROM assetlocrelation WHERE targetassetnum IN (SELECT targetassetnum FROM assetlocrelation WHERE assetrelationnum!='INCLUDES' AND sourceassetnum = :assetNum OR sourceassetnum = (SELECT parent FROM asset WHERE assetnum = :assetNum )))))) UNION SELECT assetnum FROM asset WHERE ancestor IN (SELECT targetassetnum FROM assetlocrelation WHERE sourceassetnum = :assetNum ))";
	
	//Asset Interferenti STAZIONI - Query 2
	private static final String QUERY_04 = "SELECT TABLEVALUE, ASSETNUM FROM assetspec WHERE ASSETATTRID IN ('LIN_ELEMENTO_INDISP', 'STZ_ELEMENTO_INDISP') AND assetnum IN (SELECT assetnum FROM assetancestor WHERE ancestor IN (SELECT targetassetnum FROM assetlocrelation WHERE x3sapid IN (SELECT SUBSTR(x3sapid, 1, 16) FROM assetlocrelation WHERE targetassetnum IN (SELECT targetassetnum FROM assetlocrelation WHERE assetrelationnum!='INCLUDES' AND sourceassetnum IN (SELECT ancestor FROM assetancestor WHERE assetnum = :assetNum OR ancestor = :assetNum )))))";

	//Asset Interferenti LINEE - Query 3
	private static final String QUERY_05 = "SELECT TABLEVALUE, ASSETNUM FROM assetspec WHERE ASSETATTRID IN ('LIN_ELEMENTO_INDISP', 'STZ_ELEMENTO_INDISP') AND (assetnum IN (SELECT sourceassetnum FROM assetlocrelation WHERE assetrelationnum != 'INCLUDES' AND targetassetnum IN (SELECT targetassetnum FROM assetlocrelation WHERE (targetassetnum = :assetNum OR targetassetnum = :ancestor ) OR x3sapid IN (SELECT CONCAT(X3SAPID, '-S') FROM assetlocrelation WHERE assetrelationnum = 'INCLUDES' AND (targetassetnum = :assetNum OR targetassetnum = :ancestor )))) OR assetnum IN (SELECT targetassetnum FROM assetlocrelation WHERE assetrelationnum != 'INCLUDES' AND sourceassetnum = :assetNum ))";

	//Flag Richiesta Indisponibilita
	private static final String QUERY_06 = "SELECT JOBPLANID, X4INDISPONIBILITA FROM jobplan WHERE jpnum = :jpnum AND pluscrevnum = :pluscjprevnum";
	
	//Check per precedenti richieste di Indisponibilita(ADD/UPDATE)
	private static final String QUERY_07 = "SELECT wonum FROM x4indispwo WHERE wonum = :wonum";

	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public boolean findPreviousUnavailabilityRequest(String wonum) throws DataLayerException {
    	
		long startTime = System.currentTimeMillis();
    	log.debug("call findPreviousUnavailabilityRequest({}) |{}|", wonum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.WONUM.getName(), wonum);
		log.debug("Executing QUERY_07 with parameters: {}", parameters);
		List<RichiestaIndisponibilitaEsistente> result = dataAccessService.search(RichiestaIndisponibilitaEsistente.class, QUERY_07, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search findPreviousUnavailabilityRequest found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		if(null == result || result.isEmpty()) {
			
			return true;
		}
    	
    	return false;
    }
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public Integer findRequestUnavailabilityFlag(String jpnum, Integer pluscjprevnum) throws DataLayerException {
    	
		long startTime = System.currentTimeMillis();
    	log.info("call findRequestUnavailabilityFlag[jpnum: {}, pluscjprevnum: {}, wonum: {}, status: {}] - |{}|",
    			jpnum, pluscjprevnum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.JPNUM.getName(), jpnum);
		parameters.put(ESQLMappingParameter.PLUSCJPREVNUM.getName(), pluscjprevnum.toString());
		List<FlagRichiestaIndisponibilita> result = dataAccessService.search(FlagRichiestaIndisponibilita.class, QUERY_06, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search findRequestUnavailabilityFlag found [{}] value: [{}] - |{}|", 
				Assorted.isNotEmpty(result) ? result.size() : 0,
				Assorted.isNotEmpty(result) ? result.get(0).getX4Indisponibilita() : 0,
				endTime - startTime);
	
		if(Assorted.isNotEmpty(result)) {
			
			return result.get(0).getX4Indisponibilita();
		
		}else {
			log.info("no results extracted from query, unavailability flag = 0");
			return 0;
		}
    	
    }
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<String> findInterferingAssets(String assetnum, String ancestor) throws DataLayerException {
    	
		long startTime = System.currentTimeMillis();
    	log.debug("call findInterferingAssets({}) |{}|", assetnum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetnum);
		parameters.put(ESQLMappingParameter.ANCESTOR.getName(), ancestor);
		List<SediTecnicheInterferenti> result = dataAccessService.search(SediTecnicheInterferenti.class, QUERY_03, parameters, true, true);
		log.info("InterferingAssets found ({})", Assorted.isNotEmpty(result) ? result : "NO_RESULT");
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search findInterferingAssets found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		if(result != null && !result.isEmpty()) {
			
			return extractTableValues(result);
		}
    	
    	return null;
    }
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<String> findInterferingStationAssets(String assetnum) throws DataLayerException {
    	
		long startTime = System.currentTimeMillis();
    	log.debug("call findInterferingStationAssets({}) |{}|", assetnum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetnum);
		List<SediTecnicheInterferentiStazioni> result = dataAccessService.search(SediTecnicheInterferentiStazioni.class, QUERY_04, parameters, true, true);
		log.info("InterferingStationAssets found ({})", Assorted.isNotEmpty(result) ? result : "NO_RESULT");
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search findInterferingStationAssets found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		if(result != null && !result.isEmpty()) {
			
			return extractTableValuesStazioni(result);
		}
    	
    	return null;
    }
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<String> findInterferingLineAssets(String assetnum, String ancestor) throws DataLayerException {
    	
		long startTime = System.currentTimeMillis();
    	log.debug("call findInterferingLineAssets({}) |{}|", assetnum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetnum);
		parameters.put(ESQLMappingParameter.ANCESTOR.getName(), ancestor);
		List<SediTecnicheInterferentiLinee> result = dataAccessService.search(SediTecnicheInterferentiLinee.class, QUERY_05, parameters, true, true);
		log.info("InterferingLineAssets found ({})", Assorted.isNotEmpty(result) ? result : "NO_RESULT");
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search findInterferingLineAssets found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		if(result != null && !result.isEmpty()) {
			
			return extractTableValuesLinee(result);
		}
    	
    	return null;
    }
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public String findUnavailabilityElement(String assetnum) throws DataLayerException {
    	
    	long startTime = System.currentTimeMillis();
    	log.debug("call findUnavailabilityElement({}) |{}|", assetnum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetnum);
		List<ElementoIndisponibileAgile> result = dataAccessService.search(ElementoIndisponibileAgile.class, QUERY_01, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search findUnavailabilityElement found [{}] results. |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		if(Assorted.isEmpty(result)) {
			
			log.debug("  - Executing alterantive query 1 for unavailability element.");

			result = dataAccessService.search(ElementoIndisponibileAgile.class, ALT_QUERY_01, parameters, true, true);
			
			endTime = System.currentTimeMillis();
			log.info("  - Search findUnavailabilityElement with alternative query 1, found [{}] results. |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		}
		
		if(Assorted.isEmpty(result)) {
			
			log.debug("  - Executing alterantive query 2 for unavailability element.");

			result = dataAccessService.search(ElementoIndisponibileAgile.class, ALT_QUERY_02, parameters, true, true);
			
			endTime = System.currentTimeMillis();
			log.info("  - Search findUnavailabilityElement with alternative query 2, found [{}] results. |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		}	

		if(!Assorted.isEmpty(result)) {			
			return result.get(0).getTableValue();
		}
    	
    	return null;
    }
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public String findUnavailabilityFatherElement(String assetnum, String ancestor) throws DataLayerException {
    	
    	long startTime = System.currentTimeMillis();
    	log.debug("call findUnavailabilityFatherElement({}) |{}|", assetnum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetnum);
		parameters.put(ESQLMappingParameter.ANCESTOR.getName(), ancestor);
		List<ElementoIndisponibileAgile> result = dataAccessService.search(ElementoIndisponibileAgile.class, QUERY_02, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search findUnavailabilityFatherElement found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		if(result != null && !result.isEmpty()) {
			
			return result.get(0).getTableValue();
		}
    	
    	return null;
    }
    
    public List<String> extractTableValues(List<SediTecnicheInterferenti> assetValues) {
        
    	Set<String> tableValuesSet = new LinkedHashSet<>();
        for (SediTecnicheInterferenti assetValue : assetValues) {
            tableValuesSet.add(assetValue.getTableValue());
        }
        
        return new ArrayList<>(tableValuesSet);
    }

    
    public List<String> extractTableValuesStazioni(List<SediTecnicheInterferentiStazioni> assetValues) {
        
    	Set<String> tableValuesSet = new LinkedHashSet<>();
        for (SediTecnicheInterferentiStazioni assetValue : assetValues) {
        	tableValuesSet.add(assetValue.getTableValue());
        }
        
        return new ArrayList<>(tableValuesSet);
    }
    
    public List<String> extractTableValuesLinee(List<SediTecnicheInterferentiLinee> assetValues) {
        
    	Set<String> tableValuesSet = new LinkedHashSet<>();
        for (SediTecnicheInterferentiLinee assetValue : assetValues) {
        	tableValuesSet.add(assetValue.getTableValue());
        }
        
        return new ArrayList<>(tableValuesSet);
    }
	
}