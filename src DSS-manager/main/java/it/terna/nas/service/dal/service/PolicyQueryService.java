package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.AX4Policy;
import it.terna.nas.service.dal.entity.eam.X4LivelloTriggerPeriodica;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class PolicyQueryService
{
	private static final String SELECT_01	= "SELECT X4POLICYID, CAMPAGNA, DESCRIPTION, CHANGEDBY, CHANGEDDATE, DATAFINEVAL, DATAINIZIOVAL, DIFFERIBILITA, ELEGGIBILITA, FREQUENZA, IDATTIVITA, IDPOLICY, IDTIMING, LASTREVIEWDATE, MAXANTICIPO, MAXPOSTICIPO, TIPOLOGIAASSET, HASLD, ROWSTAMP, LIVPOLICY, LIVCOMB, UM, TIPOPOLICY, POLICYAGGR, LIVAGGR, COMBINPUT, STATUS, X4RIFTEMPORALE, X4SOTTOPROGRAMMA, X4CLUSTER, SITEID, APARTIREDA, VERSION, IDMACROATTIVITA, LOGICATRONCO, X4CARICAMENTOMXLOADER "; 
	private static final String QUERY_01	= "select c from " + X4Policy.class.getName() + " c where c.x4PolicyId = :x4PolicyId AND c.x4CaricamentoMXLoader is null";
	private static final String QUERY_03	= "select c from " + X4Policy.class.getName() + " c where c.tipoPolicy = :tipoPolicy AND c.status = 'ATTIVA' AND c.x4CaricamentoMXLoader is null";
	private static final String QUERY_04	= SELECT_01 + ", EAUDITTRANSID FROM A_X4POLICY xap WHERE xap.X4POLICYID = :x4PolicyId AND xap.VERSION = ( SELECT max(axp.VERSION) FROM A_X4POLICY axp WHERE axp.X4POLICYID = :x4PolicyId AND axp.VERSION != :version AND axp.STATUS = :status) AND ROWNUM = 1 AND STATUS = :status"; 
	private static final String QUERY_05	= SELECT_01 + """
												FROM X4POLICY A 
												WHERE A.POLICYAGGR = 0 
												AND A.TIPOPOLICY = 'ON-CONDITION' 
												AND A.STATUS = 'ATTIVA' 
												AND A.X4CARICAMENTOMXLOADER is null
												AND A.X4POLICYID IN (SELECT B.X4POLICYID	
													 				 FROM X4POLICYCLASSIFICATIONS B 
													 				 WHERE B.CLASSSTRUCTUREID = :classStructureId 
													 				 AND B.X4POLICYID = A.X4POLICYID
													 				)
												AND EXISTS (SELECT 1 FROM X4POLICYINPUT R 
															WHERE R.TIPOLOGIAINPUT = 'ATTIVITA' 
															AND R.X4POLICYID = A.X4POLICYID 
															AND R.IDINPUT = :jpnum)"""; 
	
	private static final String QUERY_06    = SELECT_01 + """ 
												FROM X4POLICY A 
												WHERE A.POLICYAGGR = 0 
												AND A.TIPOPOLICY = 'ON-CONDITION' 
												AND A.STATUS = 'ATTIVA' 
												AND A.X4CARICAMENTOMXLOADER is null
												AND  A.X4POLICYID IN (SELECT B.X4POLICYID 
														FROM X4POLICYCLASSIFICATIONS B 
														WHERE B.CLASSSTRUCTUREID = :classStructureId 
														AND B.X4POLICYID = A.X4POLICYID)
												AND EXISTS (SELECT 1 FROM X4POLICYINPUT R 
															WHERE R.TIPOLOGIAINPUT = 'DIFFORMITA' 
															AND R.X4POLICYID = A.X4POLICYID 
															AND R.IDINPUT = :x4catdifforid)""";
	
	private static final String QUERY_07    = SELECT_01 + """
												FROM X4POLICY A 
												WHERE A.POLICYAGGR = 0 
												AND A.TIPOPOLICY = 'ON-CONDITION' 
												AND A.STATUS = 'ATTIVA' 
												AND A.X4CARICAMENTOMXLOADER is null
												AND A.X4POLICYID IN (SELECT B.X4POLICYID 
																	 FROM X4POLICYCLASSIFICATIONS B 
																	 WHERE B.CLASSSTRUCTUREID = :classStructureId 
																	 AND B.X4POLICYID = A.X4POLICYID)
												AND EXISTS (SELECT 1 FROM X4POLICYINPUT R 
															WHERE R.TIPOLOGIAINPUT = 'INTERFERENZA' 
															AND R.X4POLICYID = A.X4POLICYID 
															AND R.IDINPUT = :x4catinterfid)""";

	
	private static final String QUERY_POLICY_BY_CLASSSTRUCTUREID_AND_INPUT = SELECT_01 + """
			  FROM X4POLICY A 
			    WHERE A.POLICYAGGR = 0 AND A.TIPOPOLICY = 'ON-CONDITION' AND STATUS = 'ATTIVA'
			       AND A.X4CARICAMENTOMXLOADER is null
			       AND A.X4POLICYID IN 
			       			(
								SELECT B.X4POLICYID
								FROM X4POLICYCLASSIFICATIONS B
								WHERE B.CLASSSTRUCTUREID = :classStructureId
								AND B.X4POLICYID = A.X4POLICYID
							)
			            AND EXISTS (
						    SELECT 1 FROM X4POLICYINPUT R 
						    WHERE R.TIPOLOGIAINPUT = :tipologiaInput
								AND R.X4POLICYID = A.X4POLICYID 
								AND R.IDINPUT = :idInput) 
				""";
	
	private static final String QUERY_POLICY_BY_MODELLO_TARGET = SELECT_01 + """ 
			 FROM X4POLICY A 
			    WHERE A.POLICYAGGR = 0 AND A.TIPOPOLICY = 'ON-CONDITION' AND STATUS = 'ATTIVA'
			       AND A.X4CARICAMENTOMXLOADER is null
			       AND EXISTS (
			           SELECT 1 FROM X4POLICYINPUT R 
			               WHERE R.TIPOLOGIAINPUT = 'DIFFORMITA' 
				           AND R.X4POLICYID = A.X4POLICYID 
				           AND R.IDMISURA IS NOT NULL
				           AND R.TARGETRAFFRONTO = :modelloId
				           AND R.TARGETRAFFRONTO_EXT = :modelloTarget) 
			""";
	
	private static final String QUERY_POLICY_BY_JPNUM = SELECT_01 + " FROM X4POLICY a WHERE A.POLICYAGGR = 0 AND A.TIPOPOLICY = 'ON-CONDITION' AND STATUS = 'ATTIVA' AND A.X4CARICAMENTOMXLOADER is null AND X4POLICYID IN (SELECT XP.X4POLICYID FROM X4POLICYINPUT XP WHERE XP.IDINPUT = :idInput AND XP.TIPOLOGIAINPUT = 'ATTIVITA' AND A.X4POLICYID = XP.X4POLICYID)";

	private static final String QUERY_08    = "SELECT xp2.* FROM X4POLICYINPUT XP, X4POLICY XP2 WHERE XP2.POLICYAGGR = 0 AND XP2.TIPOPOLICY = 'ON-CONDITION' AND XP.IDINPUT = :idInput AND XP.TIPOLOGIAINPUT = :tipoInput AND XP2.X4POLICYID = XP.X4POLICYID AND XP2.STATUS = 'ATTIVA'";
	private static final String QUERY_09    = SELECT_01 + """ 
												FROM X4POLICY A 
												WHERE A.POLICYAGGR = 0 
												AND A.TIPOPOLICY = 'ON-CONDITION' 
												AND A.STATUS = 'ATTIVA' 
												AND A.X4CARICAMENTOMXLOADER is null
												AND A.X4POLICYID IN (
																	 SELECT B.X4POLICYID 
																	 FROM X4POLICYCLASSIFICATIONS B 
																	 WHERE B.CLASSSTRUCTUREID = :classStructureId 
																	 AND B.X4POLICYID = A.X4POLICYID
																	)
																											
												AND EXISTS (SELECT 1 FROM X4POLICYINPUT R 
															   WHERE R.TIPOLOGIAINPUT = 'HI' 
					 										   AND R.X4POLICYID = A.X4POLICYID 
															   AND R.IDINPUT = :hiattributename
															) """;
	private static final String QUERY_10    = SELECT_01 + """
			 									FROM X4POLICY A 
			 									WHERE A.POLICYAGGR = 0 
			 									AND A.STATUS = 'ATTIVA' 
			 									AND A.X4CARICAMENTOMXLOADER is null
			 									AND A.X4POLICYID IN (
			 										 		SELECT A.X4POLICYID 
			 										 		FROM X4POLICYCLASSIFICATIONS B 
			 										 		WHERE B.CLASSSTRUCTUREID = :classStructureId 
			 										 		AND B.X4POLICYID = A.X4POLICYID
			 										 ) """;

	private static final String QUERY_11    = SELECT_01 + " FROM X4POLICY A WHERE A.POLICYAGGR = 0 AND A.TIPOPOLICY = 'ON-CONDITION' AND A.STATUS = 'ATTIVA' AND A.X4CARICAMENTOMXLOADER is null AND (TIPOLOGIAASSET = :classificationid OR A.X4POLICYID IN (SELECT A.X4POLICYID FROM X4POLICYCLASSIFICATIONS B WHERE CLASSIFICATIONID = :classificationid AND B.X4POLICYID = A.X4POLICYID)) AND EXISTS (SELECT 1 FROM X4POLICYINPUT R WHERE R.TIPOLOGIAINPUT = 'ATTRIBUTO' AND R.X4POLICYID = A.X4POLICYID AND R.IDINPUT = :assetattrid)";
	private static final String QUERY_POLICY_BY_ECCEZIONI = SELECT_01 + " FROM X4POLICY a WHERE A.POLICYAGGR = 0 AND A.TIPOPOLICY = 'ON-CONDITION' AND a.STATUS = 'ATTIVA' AND A.X4CARICAMENTOMXLOADER is null AND EXISTS (SELECT 1 FROM X4TABECCEZIONI r where r.TIPOLOGIAASSET = a.TIPOLOGIAASSET AND r.TIPOLOGIAASSET = :classificationid)";
	private static final String QUERY_POLICY_BY_IDONEITA = SELECT_01 + " FROM X4POLICY a WHERE A.POLICYAGGR = 0 AND A.TIPOPOLICY = 'ON-CONDITION' AND a.STATUS = 'ATTIVA' AND A.X4CARICAMENTOMXLOADER is null AND EXISTS (SELECT 1 FROM X4TABIDONEITA r where r.TIPOLOGIAASSET = a.TIPOLOGIAASSET AND r.TIPOLOGIAASSET = :classificationid)";
			
	
	private static final String QUERY_12 = String.format(
		    "SELECT c FROM %s c " +
		    "WHERE c.x4PolicyId = :x4PolicyId " +
		    "AND c.tipoPolicy = 'PERIODICA' " +
		    "AND c.x4RifTemporale = 'A' " +
		    "AND c.x4CaricamentoMXLoader is null " +
		    "AND c.status = 'ATTIVA' " +
		    "AND c.frequenza > 1",
		    X4Policy.class.getSimpleName()
		);
	
	private static final String QUERY_13    = SELECT_01 + """
			FROM X4POLICY A 
			WHERE A.STATUS = 'ATTIVA' 
			  AND A.TIPOPOLICY = 'PERIODICA' 
			  AND A.X4CARICAMENTOMXLOADER is null
			  AND A.X4POLICYID IN (
			 						SELECT B.X4POLICYID 
			 						FROM X4POLICYCLASSIFICATIONS B 
			 						WHERE B.CLASSSTRUCTUREID = :classStructureId 
			 						AND B.X4POLICYID = A.X4POLICYID
			 					  )
			  AND EXISTS (SELECT 1 
			              FROM X4POLICYINPUT R 
			              WHERE R.TIPOLOGIAINPUT = 'ATTRIBUTO' 
			                AND R.X4POLICYID = A.X4POLICYID 
			                AND R.IDINPUT = :assetattrid
			                AND R.TARGETRAFFRONTO = :alnvalue)
			
			""";

		
	private static final String QUERY_LIVELLI_ABILITATI	= "select c from " + X4LivelloTriggerPeriodica.class.getName() + " c where c.attivo = 1";
	
	@Autowired
    private DataAccessService   dataAccessService;
	
 
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public X4Policy getPolicy(String policyId) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getPolicy({}) |{}|", policyId, startTime);		
		
	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.POLICYID.getName(), policyId);
		
		List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_01, parameters, false, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getPolicy found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByClassStructureIdAndLogicaTronco(String classStructureId, Integer logicaTronco) 
        throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByClassStructureId({}, {}) |{}|", classStructureId, logicaTronco, startTime);       
        
        StringBuilder sql = new StringBuilder(QUERY_10);
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classStructureId);

        if (Assorted.notNull(logicaTronco))
        {
            sql.append(" AND A.LOGICATRONCO = :logicatronco");
            parameters.put(ESQLMappingParameter.LOGICATRONCO.getName(), logicaTronco.toString());
        }

        List<X4Policy> result = dataAccessService.search(X4Policy.class, sql.toString(), parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByClassStructureId found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }

    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByClassifAttrib(String classificationId, String attribute) 
        throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByClassifAttrib({}, {}) |{}|", classificationId, attribute, startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.CLASSIFICATIONID.getName(), classificationId);
        parameters.put(ESQLMappingParameter.ASSETATTRID.getName(), attribute);
        List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_11, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByClassifAttrib found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByClassStructureIdAndDifformita(String classStructuredId, Long difformita) 
        throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByClassStructureIdAndDiffor({}, {}) |{}|", classStructuredId, difformita, startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classStructuredId);
        parameters.put(ESQLMappingParameter.X4CATDIFFORID.getName(), difformita.toString());
        List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_06, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByClassStructureIdAndDiffor found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByClassStructureIdAndHi(String classStructureId, String attribureId) 
        throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByClassStructureIdAndHi({}, {}) |{}|", classStructureId, attribureId, startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classStructureId);
        parameters.put(ESQLMappingParameter.HIATTRIBUTENAME.getName(), attribureId);
        List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_09, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByClassStructureIdAndHi found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByClassStructureIdAndInterferenza(String classStructureId, Long interferenza) 
        throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByClassStructureIdAndInterferenza({}, {}) |{}|", classStructureId, interferenza, startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classStructureId);
        parameters.put(ESQLMappingParameter.X4CATINTERFID.getName(), interferenza.toString());
        List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_07, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByClassStructureIdAndInterferenza found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }

    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<X4Policy> getPolicyByClassStructureIdAndJpnum(String classStructureId, String jpnum, String policyId) 
		throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getPolicyByClassStructureIdAndJpnum({}, {}, {}) |{}|", classStructureId, jpnum, policyId, startTime);		
		
		StringBuilder sql = new StringBuilder(QUERY_05);
	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classStructureId);
		parameters.put(ESQLMappingParameter.JPNUM.getName(), jpnum);

		if (Assorted.isNotEmpty(policyId))
		{
			sql.append(" AND A.X4POLICYID != :x4PolicyId");
			parameters.put(ESQLMappingParameter.POLICYID.getName(), policyId);
		}
			
		List<X4Policy> result = dataAccessService.search(X4Policy.class, sql.toString(), parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getPolicyByClassStructureIdAndJpnum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		return result;
	}

    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByInput(String tipoInput, String idInput) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByInput({}, {}) |{}|", tipoInput, idInput, startTime);      
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.TIPOINPUT.getName(), tipoInput);
        parameters.put(ESQLMappingParameter.IDINPUT.getName(), idInput);
        
        List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_08, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByInput found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByJpnum(String idInput) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByInput({}) |{}|", idInput, startTime);      
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.IDINPUT.getName(), idInput);
        
        List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_POLICY_BY_JPNUM, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByInput found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }

    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<X4Policy> getPolicyByTipologia(String tipologia, String idPolicy) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getPolicyByTipologia({}, {}) |{}|", tipologia, startTime);		
		
		StringBuilder sql = new StringBuilder();
		sql.append(QUERY_03);
		
	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.TIPOPOLICY.getName(), tipologia);
		
		if (Assorted.isNotEmpty(idPolicy))
		{
		    sql.append(" and c.x4PolicyId = :x4PolicyId");
		    parameters.put(ESQLMappingParameter.POLICYID.getName(), idPolicy);
		}
		
		List<X4Policy> result = dataAccessService.search(X4Policy.class, sql.toString(), parameters, false, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getPolicyByTipologia found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		return result;
	}
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public AX4Policy getPolicyHistory(String policyId, Integer version) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getPolicyHistory({}, {}) |{}|", policyId, version, startTime);		
		
	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.POLICYID.getName(), policyId);
		parameters.put(ESQLMappingParameter.VERSION.getName(), 	Assorted.notNull(version) ? version.toString() : "1");
		parameters.put(ESQLMappingParameter.STATUS.getName(), 	Constants.POLICY_ATTIVA_STATUS);
		List<AX4Policy> result = dataAccessService.search(AX4Policy.class, QUERY_04, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getPolicyHistory found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
		
		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}
    
    
	@Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByClassStructureIdAndTipologiaInput(String classStructureId, String inputType, String idInput) 
        throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByClassificationIdAndTipologiaInput({}, {}, {}) |{}|", classStructureId, inputType, idInput, startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classStructureId);
        parameters.put(ESQLMappingParameter.TIPOLOGIA_INPUT.getName(), inputType);
        parameters.put(ESQLMappingParameter.IDINPUT.getName(), idInput);
        List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_POLICY_BY_CLASSSTRUCTUREID_AND_INPUT, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByClassificationIdAndTipologiaInput found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByModello(String modelloId, String modelloTarget) 
        throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByModello({}, {}) |{}|", modelloId, modelloTarget, startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.MODELLO_ID.getName(), modelloId);
        parameters.put(ESQLMappingParameter.MODELLO_TARGET.getName(), modelloTarget);
        List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_POLICY_BY_MODELLO_TARGET, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByModello found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByClassificationFromEccezioni(String classificationId) 
        throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByClassificationFromEccezioni({}) |{}|", classificationId, startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.CLASSIFICATIONID.getName(), classificationId);
        List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_POLICY_BY_ECCEZIONI, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByClassificationFromEccezioni found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }
	
	@Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByClassificationFromIdoneita(String classificationId) 
        throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByClassificationFromIdoneita({}) |{}|", classificationId, startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.CLASSIFICATIONID.getName(), classificationId);
      
        List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_POLICY_BY_IDONEITA, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByClassificationFromIdoneita found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }
	
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public X4Policy findActivePeriodicPolicyById(String policyId) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
	    log.debug("Fetching active periodic policy for ID: {} | Start time: {}", policyId, startTime);
		
	    Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.POLICYID.getName(), policyId);
		
		List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_12, parameters, false, true);
		
		long duration = System.currentTimeMillis() - startTime;
	    log.debug("Search completed. Found {} policies | Duration: {}ms", result.size(), duration);
		
		return Assorted.isNotEmpty(result) ? result.get(0) : null;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4Policy> getPolicyByClassStructureIdAttribForZonizzazione(String classStructureId, String attribute, String alnvalue) 
        throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getPolicyByClassStructureIdAttribForZonizzazione({}, {}) |{}|", classStructureId, attribute, startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classStructureId);
        parameters.put(ESQLMappingParameter.ASSETATTRID.getName(), attribute);
        parameters.put(ESQLMappingParameter.ALNVALUE.getName(), alnvalue);

        List<X4Policy> result = dataAccessService.search(X4Policy.class, QUERY_13, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getPolicyByClassStructureIdAttribForZonizzazione found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }
    
    @Cacheable(cacheManager = Constants.CACHE_MANAGER, value = Constants.CACHE_LIVELLO_PERIODICHE, key = "#root.methodName", unless = "#result==null OR #result.isEmpty()")
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<Integer> getLivelliAbilitati() 
        throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getLivelliAbilitati() |{}|", startTime);       
        
        Map <String, String> parameters = new HashMap<>();
        
        List<Integer> result = new ArrayList<Integer>();
 
        List<X4LivelloTriggerPeriodica> livelliAbilitati = dataAccessService.search(X4LivelloTriggerPeriodica.class, QUERY_LIVELLI_ABILITATI, parameters, false, true);
        if(Assorted.isNotEmpty(livelliAbilitati)) {
        	log.debug(" - Livelli abilitati per POLICY PERIODICHE={}", livelliAbilitati.stream()
        																	 .map(x -> String.valueOf(x.getLevel()))
        																	 .collect(Collectors.joining(",")));
        	livelliAbilitati.forEach(x -> result.add(x.getLevel()));
        }
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getLivelliAbilitati found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        return result;
    }
}
