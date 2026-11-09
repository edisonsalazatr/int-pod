package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.dto.ParameterData;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.AssetClassStructure;
import it.terna.nas.service.dal.entity.eam.AssetRestricted;
import it.terna.nas.service.dal.entity.eam.RadiceList;
import it.terna.nas.service.dal.entity.eam.X4RenameSapIdSnow;
import it.terna.nas.service.dal.entity.eam.X4Snow;
import it.terna.nas.service.dal.entity.eam.X4SnowAsset;
import it.terna.nas.service.dal.entity.eam.X4SnowAttributes;
import it.terna.nas.service.dal.entity.eam.X4SnowRilancio;
import it.terna.nas.service.dal.service.repository.X4SnowRepository;
import it.terna.nas.service.dal.service.repository.X4SnowRilancioRepository;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SnowAssetQueryService {

    private static final String QUERY_01_LINEE      = "SELECT ASSETNUM,CHANGEDATE,INGRP,GEWRK,INSTALLDATE,SEQUENCE,STATUS,STATUSDATE,SWERK,SWERKDESCRIPTION,CLASSIFICATIONID,ROOTSAPID,DESCRIPTION,COSTRUTTORECOMPONENTE,COSTRUTTORECOMPONENTEDESCR,FUNZIONEELEMENTO,RIFERIMENTOFASE,TIPOCOMPONENTE,SAPID,PARENT,ROOT_LEVEL FROM X4SNOWLINEE WHERE ASSETNUM = :assetnum AND SITEID = 'SIT01'";
    private static final String QUERY_01_STAZIONI   = "SELECT ASSETNUM,CHANGEDATE,INGRP,GEWRK,INSTALLDATE,SEQUENCE,STATUS,STATUSDATE,SWERK,SWERKDESCRIPTION,CLASSIFICATIONID,ROOTSAPID,DESCRIPTION,COSTRUTTORECOMPONENTE,COSTRUTTORECOMPONENTEDESCR,FUNZIONEELEMENTO,RIFERIMENTOFASE,TIPOCOMPONENTE,NVL(SAPID, PARENTSAPID) AS SAPID,PARENT,ROOT_LEVEL FROM X4SNOWSTAZIONI WHERE ASSETNUM = :assetnum AND SITEID = 'SIT01'";    
    private static final String QUERY_02_LINEE      = "SELECT s.ASSETATTRID,aa.DATATYPE,s.ALNVALUE,s.NUMVALUE,s.TABLEVALUE,s.DATEVALUE FROM ASSETSPEC s JOIN ASSETATTRIBUTE aa ON aa.ASSETATTRID = s.ASSETATTRID WHERE s.ASSETNUM = :assetnum AND s.SITEID = 'SIT01' AND s.ASSETATTRID IN ('LIN_LUNGHEZZA','LIN_CONDUTTORE_AEREO','LIN_ANNO_INSTALLAZIONE','LIN_TIPO_FUNE_GUARDIA','LIN_COSTRUTTORE_GIUNTO','LIN_TIPO_GIUNTO','LIN_COSTRUTTORE_TERMINALE','LIN_TIPO_TERMINALE','LIN_RIF_ASSET_ARM_COND','LIN_TIPO_ISOLATORI_LIN','LIN_NUM_CATENE','LIN_TIPO_ISOLATORI_DER','LIN_RIF_ASSET_ARM_FDG','LIN_TIPO_ISOLATORI','LIN_TIPO_PALIFICAZIONE','LIN_TIPO_SOSTEGNO','LIN_STRUTTURA_SOSTEGNO','LIN_ANNO_COSTRUZIONE','LIN_COSTRUTTORE_CAVO','LIN_CONDUTTORE_CAVO','LIN_ISOLAMENTO_CAVO','LIN_TIPO_CONDUTTORE_CAVO','LIN_SEZIONE_CONDUTTORE_CAVO','LIN_RIF_FASE','LIN_RIF_POSIZIONE','LIN_ELEMENTO_INDISP') AND (s.ALNVALUE IS NOT NULL OR s.NUMVALUE IS NOT NULL OR s.TABLEVALUE IS NOT NULL OR s.DATEVALUE IS NOT NULL)";
    private static final String QUERY_03_LINEE      = "SELECT s.ASSETATTRID,aa.DATATYPE,s.ALNVALUE,s.NUMVALUE,s.TABLEVALUE,s.DATEVALUE FROM ASSETSPEC s JOIN ASSETATTRIBUTE aa ON aa.ASSETATTRID = s.ASSETATTRID WHERE s.ASSETNUM = :assetnum AND s.SECTION = :rootSapId AND s.SITEID = 'SIT01' AND s.ASSETATTRID = 'LIN_RIF_FUNE' AND s.ALNVALUE IS NOT NULL";
    private static final String QUERY_02_STAZIONI   = "SELECT s.ASSETATTRID,aa.DATATYPE,s.ALNVALUE,s.NUMVALUE,s.TABLEVALUE,s.DATEVALUE FROM ASSETSPEC s JOIN ASSETATTRIBUTE aa ON aa.ASSETATTRID = s.ASSETATTRID WHERE s.ASSETNUM = :assetnum AND s.SITEID = 'SIT01' AND s.ASSETATTRID IN ('STZ_DATA_COSTRUZIONE','STZ_TENSIONE_NOMINALE','STZ_FUNZIONAMENTO_COMANDI','STZ_TIPO_COMPARTO','STZ_POTENZA_NOMINALE','STZ_ELEMENTO_INDISP') AND (s.ALNVALUE IS NOT NULL OR s.NUMVALUE IS NOT NULL OR s.TABLEVALUE IS NOT NULL OR s.DATEVALUE IS NOT NULL)";
    private static final String QUERY_03            = "SELECT CODICE_OGGETTO FROM X4SNOW WHERE CLASSSTRUCTUREID = :tipoOggetto";
    private static final String QUERY_04            = "SELECT DISTINCT CODICE_RADICE AS RADICE FROM X4SNOW xg WHERE (CODICE_RADICE = :codiceOggetto) UNION SELECT DISTINCT CODICE_RADICE2 AS RADICE FROM X4SNOW xg WHERE (CODICE_RADICE = :codiceOggetto AND CODICE_RADICE2 IS NOT null)";
    private static final String QUERY_05			= "SELECT * FROM X4RENAMESAPIDSNOW WHERE DA_NOTIFICARE_SNOW = 'Y'";
    private static final String QUERY_06			= "select c from " + X4RenameSapIdSnow.class.getName() + " c where c.x4RenameSapIdSnowId = :x4RenameSapIdSnowId";
	private static final String QUERY_07			= "SELECT CLASSSTRUCTUREID, ASSETNUM FROM ASSET WHERE ASSETNUM = :assetnum";
	private static final String QUERY_08   			= "SELECT ASSETNUM,CHANGEDATE,INGRP,GEWRK,INSTALLDATE,SEQUENCE,STATUS,STATUSDATE,SWERK,SWERKDESCRIPTION,CLASSIFICATIONID,ROOTSAPID,DESCRIPTION,COSTRUTTORECOMPONENTE,COSTRUTTORECOMPONENTEDESCR,FUNZIONEELEMENTO,RIFERIMENTOFASE,TIPOCOMPONENTE,SAPID,PARENT,ROOT_LEVEL FROM X4SNOWUFFICIOVOLTAICO WHERE ASSETNUM = :assetnum AND SITEID = 'SIT01'";
	private static final String ENTITY_UPDATE_MESSAGE 	= "Unable to update X4RenameSapIdSnow with x4renamesapidsnowid '%s'";
    private static final String STORED_PROCEDURE_01 = "PKG_X4SNOW.MAIN";
    
    @Autowired
    private DataAccessService dataAccessService;
    
    @Autowired
    private EntityManager entityManager;
    
    @Autowired
    private X4SnowRepository x4SnowRepository;
    
    @Autowired
    private X4SnowRilancioRepository x4SnowRilancioRepository;
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public Boolean executeSnowStoredProcedure() 
            throws DataLayerException 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call executeSnowStoredProcedure() |{}|", System.currentTimeMillis());        
        
        Boolean result = false;
        
        Map<String, ParameterData> parameters = new HashMap<>();

        result = dataAccessService.executeStoredProcedure(STORED_PROCEDURE_01, parameters);
        
        long endTime = System.currentTimeMillis();
        log.info("End executeSnowStoredProcedure result obtained [{}] |{}|", result, endTime - startTime);
    
        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<AssetRestricted> getAssetObjectsByClassStructure(String tipoOggetto) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getAssetObjectsByClassStructure({}) |{}|", tipoOggetto, startTime);
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.TIPOOGGETTO.getName(), tipoOggetto);

        List<AssetRestricted> result = dataAccessService.search(AssetRestricted.class, QUERY_03, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.info("end getAssetObjectsByClassStructure found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<String> getDirectAssetRoots(String codiceOggetto) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getDirectAssetRoots({}) |{}|", codiceOggetto, System.currentTimeMillis());     
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.CODICEOGGETTO.getName(), codiceOggetto);

        List<RadiceList> result = dataAccessService.search(RadiceList.class, QUERY_04, parameters, true, true);
        
        long endTime = System.currentTimeMillis();
        log.info("end getDirectAssetRoots found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
        
        if(Assorted.isNotEmpty(result)) 
        {
            List<String> returningRoots = new ArrayList<>();
            for(RadiceList root : result) 
            {
                returningRoots.add(root.getRadice());
            }
            
            return returningRoots;
        }
        else 
            return null;
    }
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(readOnly = true)
    public List<X4Snow> getHierarchy(List<String> rootCodes) 
            throws DataLayerException 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getHierarchy({}) |{}|", rootCodes, System.currentTimeMillis());     
        List<X4Snow> result = new ArrayList<>();
        try (Stream<X4Snow> items = x4SnowRepository.getHierarchy(rootCodes))
        {
            Iterator<X4Snow> iterator = items.iterator();
            while (iterator.hasNext()) 
            {
                X4Snow row = iterator.next();
                entityManager.detach(row);
                result.add(row);
            }
        }
    
        long endTime = System.currentTimeMillis();
        log.info("end getHierarchy found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
    
        return Assorted.isNotEmpty(result) ? result : null;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4SnowAsset> getX4SnowLinee(String assetnum) 
            throws DataLayerException 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getX4SnowLinee({}) |{}|", assetnum, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM_A.getName(), assetnum);

        List<X4SnowAsset> result = dataAccessService.search(X4SnowAsset.class, QUERY_01_LINEE, parameters, true, true);

        long endTime = System.currentTimeMillis();
        log.info("  - Search getX4SnowLinee found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return Assorted.isNotEmpty(result) ? result : null;
    }

    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4SnowAttributes> getX4SnowLineeAttributes(String assetnum, String rootSapId) 
            throws DataLayerException 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getX4SnowLineeAttributes({}, {}) |{}|", assetnum, rootSapId, startTime);
        List<X4SnowAttributes> finalResults = new ArrayList<>();

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM_A.getName(), assetnum);

        List<X4SnowAttributes> result = dataAccessService.search(X4SnowAttributes.class, QUERY_02_LINEE, parameters, true, true);
        
        parameters.put(ESQLMappingParameter.ROOTSAPID.getName(), rootSapId);
        
        List<X4SnowAttributes> result2 = dataAccessService.search(X4SnowAttributes.class, QUERY_03_LINEE, parameters, true, true);
        
        if(Assorted.isNotEmpty(result))
        	finalResults.addAll(result);
        
        if(Assorted.isNotEmpty(result2))
        	finalResults.addAll(result2);

        long endTime = System.currentTimeMillis();
        log.info("  - Search getX4SnowLineeAttributes found [{}] |{}|", Assorted.isNotEmpty(finalResults) ? finalResults.size() : 0, endTime - startTime);

        return Assorted.isNotEmpty(finalResults) ? finalResults : null;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4SnowAsset> getX4SnowStazioni(String assetnum) 
            throws DataLayerException 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getX4SnowStazioni({}) |{}|", assetnum, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM_A.getName(), assetnum);

        List<X4SnowAsset> result = dataAccessService.search(X4SnowAsset.class, QUERY_01_STAZIONI, parameters, true, true);

        long endTime = System.currentTimeMillis();
        log.info("  - Search getX4SnowStazioni found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return Assorted.isNotEmpty(result) ? result : null;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4SnowAttributes> getX4SnowStazioniAttributes(String assetnum) 
            throws DataLayerException 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getX4SnowStazioniAttributes({}) |{}|", assetnum, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM_A.getName(), assetnum);

        List<X4SnowAttributes> result = dataAccessService.search(X4SnowAttributes.class, QUERY_02_STAZIONI, parameters, true, true);

        long endTime = System.currentTimeMillis();
        log.info("  - Search getX4SnowStazioniAttributes found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return Assorted.isNotEmpty(result) ? result : null;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<X4SnowAsset> getX4SnowUfficioVoltaico(String assetnum) 
            throws DataLayerException 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getX4SnowUfficioVoltaico({}) |{}|", assetnum, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM_A.getName(), assetnum);

        List<X4SnowAsset> result = dataAccessService.search(X4SnowAsset.class, QUERY_08, parameters, true, true);

        long endTime = System.currentTimeMillis();
        log.info("  - Search getX4SnowUfficioVoltaico found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return Assorted.isNotEmpty(result) ? result : null;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
	@Transactional
	public List<X4RenameSapIdSnow> getObjectsToNotifyRename() 
			throws DataLayerException 
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getObjectsToNotifyRename() |{}|", startTime);		
		
		List<X4RenameSapIdSnow> result = dataAccessService.search(X4RenameSapIdSnow.class, QUERY_05, null, true, true);
		
		long endTime = System.currentTimeMillis();
		log.info("  - Search getObjectsToNotifyRename found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return Assorted.isNotEmpty(result) ? result : null;
	}    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
	public void updateNotiDetails(Long x4RenameSapIdSnowId, String flagDaNotificare) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call updateNotiDetails({}, {}) |{}|", x4RenameSapIdSnowId, flagDaNotificare, startTime);		
		
		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.X4RENAMESAPIDSNOWID.getName(), String.valueOf(x4RenameSapIdSnowId));
		
		List<X4RenameSapIdSnow> result = dataAccessService.search(X4RenameSapIdSnow.class, QUERY_06, parameters, false, true);
		if (Assorted.isNotEmpty(result)) 
		{
			X4RenameSapIdSnow rowUpdate = result.get(0);
			rowUpdate.setDaNotificareSnow(flagDaNotificare);
			dataAccessService.save(rowUpdate, X4RenameSapIdSnow.class.getName(), ENTITY_UPDATE_MESSAGE, x4RenameSapIdSnowId);
		}
		
		long endTime = System.currentTimeMillis();
		log.debug("  - End updateNotiDetails |{}|", endTime - startTime);
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<AssetClassStructure> getClassstructureId(String assetnum) 
            throws DataLayerException 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getClassstructureId({}) |{}|", assetnum, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM_A.getName(), assetnum);

        List<AssetClassStructure> result = dataAccessService.search(AssetClassStructure.class, QUERY_07, parameters, true, true);

        long endTime = System.currentTimeMillis();
        log.info("  - Search getClassstructureId found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return result;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(readOnly = true)
    public long recuperoAssetDaElaborare(Consumer<X4SnowRilancio> consumer)
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call recuperoAssetDaElaborare() |{}|", startTime);

        long count = 0;
        try (Stream<X4SnowRilancio> items = x4SnowRilancioRepository.findByDaElaborare(Constants.DA_ELABORARE_YES))
        {
            Iterator<X4SnowRilancio> iterator = items.iterator();
            while (iterator.hasNext())
            {
                X4SnowRilancio row = iterator.next();
                // detach immediately so the persistence context does not keep references in memory
                entityManager.detach(row);
                consumer.accept(row);
                count++;
            }
        }

        long endTime = System.currentTimeMillis();
        log.info("end recuperoAssetDaElaborare processed [{}] |{}|", count, endTime - startTime);

        return count;
    }


    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markAsElaborato(Long id)
            throws DataLayerException
    {
        log.debug("call markAsElaborato({}) |{}|", id, System.currentTimeMillis());
        x4SnowRilancioRepository.updateDaElaborare(id, Constants.DA_ELABORARE_NO);
    }
}