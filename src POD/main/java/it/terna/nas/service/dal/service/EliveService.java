package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Monitor.EErrorCategory;
import it.terna.kernel.basement.utility.Monitor.EErrorMapping;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.AlnvalueResult;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.DatiReportElive;
import it.terna.nas.service.dal.entity.eam.SapIdAssetNumResult;
import it.terna.nas.service.dal.entity.eam.ScartiReportElive;
import it.terna.nas.service.dal.entity.eam.Workorder;
import it.terna.nas.service.dal.entity.eam.X4CatInterf;
import it.terna.nas.service.dal.entity.eam.X4EliveInterfConfig;
import it.terna.nas.service.dal.entity.eam.X4CatModelloAsset;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EliveService {
	
	@Autowired
    private DataAccessService dataAccessService;	
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<SapIdAssetNumResult> getSapIdAndAssetNum(List<String> sapIds) {

		log.info(" - find AssetNum by SapId");
		List<SapIdAssetNumResult> listResult = null;
        StringBuilder sapIdListBuilder = new StringBuilder();
        for (String sapId : sapIds) {
            if (sapIdListBuilder.length() > 0) {
                sapIdListBuilder.append(" UNION ALL ");
            }
            sapIdListBuilder.append("SELECT '").append(sapId).append("' AS x3sapid FROM DUAL");
        }
        
        String nativeQuery = String.format(
                "SELECT sapid_list.x3sapid, a.targetassetnum " +
                "FROM (%s) sapid_list " +
                "LEFT JOIN ASSETLOCRELATION a ON sapid_list.x3sapid = a.x3sapid " +
                "ORDER BY sapid_list.x3sapid", sapIdListBuilder
            );
        
        try {
        	
        	listResult = dataAccessService.search(SapIdAssetNumResult.class,
        			nativeQuery.toString(),
					null,
					true,
					true);
        	
        	log.info(" Found {} result", listResult != null
        											? listResult.size()
        											: 0);
        	
        } catch (DataLayerException dle) {
			log.error("An error occurred during the query", dle);
		}

        return listResult;
    }
	
	
	@Transactional
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<ScartiReportElive> insertScartiReport(List<ScartiReportElive> scartiToInsert) throws EAMException {
		Assert.notNull(scartiToInsert, "launchId is null");
		
		log.info("  - insert scarti ");
        
		try {
			
			return dataAccessService.update(scartiToInsert,
					ScartiReportElive.class.getSimpleName(),
	                EErrorMapping.GENERIC_QUERY_ERROR.getCode(),
	                new Object[]{"create", "ScartiReportElive"});
			
		} catch (DataLayerException dle) {
			log.error("An error occurred during the query", dle);
			throw new EAMException(EErrorCategory.GENERIC.getSpecificErrorCode(), EErrorCategory.GENERIC.getErrorDescription(), dle);
		}
	}
	
	@Transactional
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<DatiReportElive> insertDatiReport(List<DatiReportElive> datiReportElive) throws EAMException {
		Assert.notNull(datiReportElive, "launchId is null");
		
		log.info("  - insert dati report ");
        
		try {
			
			return dataAccessService.update(datiReportElive,
					DatiReportElive.class.getSimpleName(),
	                EErrorMapping.GENERIC_QUERY_ERROR.getCode(),
	                new Object[]{"create", "DatiReportElive"});
			
		} catch (DataLayerException dle) {
			log.error("An error occurred during the query", dle);
			throw new EAMException(EErrorCategory.GENERIC.getSpecificErrorCode(), EErrorCategory.GENERIC.getErrorDescription(), dle);
		}
	}
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<AlnvalueResult> findAlnvalueByLinea(String linea) throws EAMException {

		log.info(" - Find livello tensione by linea");
		List<AlnvalueResult> assetspec = null;

		String nativeQuery = new StringBuilder()
			    .append("SELECT alnvalue ")
			    .append("FROM assetspec ")
			    .append("WHERE assetnum = ( ")
			    .append("    SELECT assetnum ")
			    .append("    FROM asset ")
			    .append("    WHERE x3sapid = '").append(linea).append("' ")
			    .append(") ")
			    .append("AND assetattrid = 'LIN_LIVELLO_TENSIONE_ESERCIZIO'")
			    .toString();
        
        try {
        	
        	assetspec = dataAccessService.search(AlnvalueResult.class,
        			nativeQuery.toString(),
					null,
					true,
					true);
        	
        	
        } catch (DataLayerException dle) {
			log.error("An error occurred during the query", dle);
			throw new EAMException(EErrorCategory.GENERIC.getSpecificErrorCode(), EErrorCategory.GENERIC.getErrorDescription(), dle);
		}

        return assetspec;
    }
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<DatiReportElive> findRecordsDatiReportEliveByNomeFile(String nomeFile) throws EAMException {

		log.info(" - Find record in DATIREPORTELIVE by nome file");
		List<DatiReportElive> result = null;
        String nativeQuery = String.format("SELECT * FROM DATIREPORTELIVE WHERE NOME_FILE = '%s'", nomeFile);        
        try {
        	
        	result = dataAccessService.search(DatiReportElive.class,
        			nativeQuery.toString(),
					null,
					true,
					true);
        	
        	
        } catch (DataLayerException dle) {
			log.error("An error occurred during the query", dle);
			throw new EAMException(EErrorCategory.GENERIC.getSpecificErrorCode(), EErrorCategory.GENERIC.getErrorDescription(), dle);
		}

        return result;
    }
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<ScartiReportElive> findRecordsScartiReportEliveByNomeFile(String nomeFile) throws EAMException {

		log.info(" - Find record in SCARTIREPORTELIVE by nome file");
		List<ScartiReportElive> result = null;
        String nativeQuery = String.format("SELECT * FROM SCARTIREPORTELIVE WHERE NOME_FILE = '%s'",nomeFile);        
        try {
        	
        	result = dataAccessService.search(ScartiReportElive.class,
        			nativeQuery.toString(),
					null,
					true,
					true);
        	
        	
        } catch (DataLayerException dle) {
			log.error("An error occurred during the query", dle);
			throw new EAMException(EErrorCategory.GENERIC.getSpecificErrorCode(), EErrorCategory.GENERIC.getErrorDescription(), dle);
		}

        return result;
    }
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public X4CatInterf getX4CatInterfByIdInterferenza(String idInterferenza) throws EAMException {
	    log.info(" - start getX4CatInterfByIdInterferenza");

	    String nativeQuery = String.format("SELECT * FROM x4catinterf WHERE idinterferenza = '%s'", idInterferenza);

	    try {
	        List<X4CatInterf> results = dataAccessService.search(X4CatInterf.class, nativeQuery, null, true, true);

	        return results.stream().findFirst().orElse(null);
	        
	    } catch (DataLayerException dle) {
	        log.error("An error occurred during the query", dle);
	        throw new EAMException(EErrorCategory.GENERIC.getSpecificErrorCode(), EErrorCategory.GENERIC.getErrorDescription(), dle);
	    }
	}
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<X4EliveInterfConfig> findActiveEliveInterfConfig(String flusso) throws EAMException {
		log.info(" - Find active ELive interference configurations for flow: {}", flusso);

		String nativeQuery = "SELECT * FROM X4ELIVEINTERFCONFIG "
				+ "WHERE ATTIVO = 1 AND UPPER(TRIM(FLUSSO)) = :flusso";
		Map<String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.FLUSSO.getName(), flusso.trim().toUpperCase(Locale.ROOT));
		try {
			return dataAccessService.search(
					X4EliveInterfConfig.class,
					nativeQuery,
					parameters,
					true,
					true);
		} catch (DataLayerException dle) {
			log.error("An error occurred while reading X4ELIVEINTERFCONFIG", dle);
			throw new EAMException(
					EErrorCategory.GENERIC.getSpecificErrorCode(),
					EErrorCategory.GENERIC.getErrorDescription(),
					dle);
		}
	}


	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<X4CatModelloAsset> findRecordsByModello(String modello) throws EAMException {

	    log.info(" - Find records in X4CATMODELLOASSET by modello");
	    List<X4CatModelloAsset> result = null;

	    String nativeQuery = "SELECT * " +
	            "FROM X4CATMODELLOASSET xc " +
	            "WHERE xc.X4CATMODELLOID = ( " +
	            "SELECT X4CATMODELLOID FROM X4CATMODELLO " +
	            "WHERE IDMODELLO = :description)";

	    Map<String, String> params = new HashMap<>();
	    params.put("description", modello);


	    try {
	        result = dataAccessService.search(
	            X4CatModelloAsset.class,
	            nativeQuery,
	            params,
	            true,
	            true
	        );
	    } catch (DataLayerException dle) {
	        log.error("An error occurred during the query", dle);
	        throw new EAMException(EErrorCategory.GENERIC.getSpecificErrorCode(), EErrorCategory.GENERIC.getErrorDescription(), dle);
	    }

	    return result;
	}
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
   	public List<Asset> getAssetByAssetNum(String assetNum) 
   			throws DataLayerException
   	{
        String query_asset = """
                SELECT 
        		           a
        		FROM Asset a
        		WHERE a.assetNum = :assetNum AND a.siteId = 'SIT01'
        		""";
   		long startTime = System.currentTimeMillis();
   		log.debug("call getAssetByAssetNum({}) |{}|", assetNum, startTime);		

   		Map <String, String> parameters = new HashMap<>();
   		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
   		List<Asset> result = dataAccessService.search(Asset.class, query_asset, parameters, false, true);
   		
   		long endTime = System.currentTimeMillis();
   		log.debug("  - Search getAssetByAssetNum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
   	
   		return result;
   	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
   	public List<Workorder> getWorkrderByAssetNumAndWonum(String assetNum, String wonum) 
   			throws DataLayerException
   	{
        String query_workorder = """
                SELECT 
        			*
        		FROM workorder w
        		WHERE w.assetnum = :assetNum AND w.wonum = :wonum
        		""";
   		long startTime = System.currentTimeMillis();
   		log.debug("call getWorkrderByAssetNum({}) |{}|", assetNum, startTime);		

   		Map <String, String> parameters = new HashMap<>();
   		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
   		parameters.put("wonum", wonum);
   		List<Workorder> result = dataAccessService.search(Workorder.class, query_workorder, parameters, true, true);
   		
   		long endTime = System.currentTimeMillis();
   		log.debug("  - Search getWorkrderByAssetNumAndWonum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
   	
   		return result;
   	}


}
