package it.terna.nas.service.dal.service;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.source.cache.manager.CacheManagerHandler;
import it.terna.nas.service.dal.entity.eam.Activity;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.AttributiAsset;
import it.terna.nas.service.dal.entity.eam.X4TabEccezione;
import it.terna.nas.service.dal.entity.eam.X4TabIdoneita;
import it.terna.nas.service.dal.repository.AssetRepository;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.exposure.handler.ActivityHandler;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.ConditionTaskProjection;
import it.terna.nas.service.material.dto.NoConditionTaskProjection;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetDataService 
{		
    @Autowired
    private AssetDataCacheService assetDataCacheService;

    @Autowired
    private AssetRepository       assetRepository;

    @Autowired
    private IdoneitaService       idoneitaService;
    
    @Autowired
    private ActivityHandler activityHandler;
	
    @Autowired
    private CacheManagerHandler   cacheManagerHandler;
    
	@Datasource(datasourceKey = EDatasourceKey.EAM)
    public AssetData getAssetDataByAssetNum(String assetNum, boolean isSlim) 
            throws EAMException
    {
        log.info(" - Starting to retrieve data for assetNum: {}", assetNum);
        long startTime = System.currentTimeMillis();

        AssetData assetData = null;

        if (isSlim)
            assetData = assetDataCacheService.createSlimAssetData(assetNum, startTime);
        else 
            assetData = assetDataCacheService.createAssetData(assetNum, startTime);
                
        return assetData;
    }
	
	/**
     * @param assetData
     * @param jpnum
     * @return true if status is suitable for asset
	 * @throws DataLayerException 
     */
	public boolean checkAssetStatus(AssetData assetData, String jpnum) throws DataLayerException {
    	
    	
    	boolean result = Objects.equals(Constants.ASSET_LEVEL_3, assetData.getLevel())
    			? checkStatusAssetLevel3(assetData, jpnum)
    			: checkStatusAssetAllLevel(assetData.getAsset().getStatus(), 
						   assetData.getAsset().getClassificationId(),
						   jpnum);
    	
    	return result;
        
    }
	
	/**
     * @param aggregationLevel
     * @param assetData
     * @param activity
     * @return true if status is suitable for asset
	 * @throws DataLayerException 
     */
	public boolean checkAssetStatus(AssetData assetData,
									Activity activity) throws DataLayerException {
    	    	
    	boolean result = Objects.equals(Constants.ASSET_LEVEL_3, assetData.getLevel())
    			? checkStatusAssetLevel3(assetData, activity)
    			: checkStatusAssetAllLevel(assetData.getAsset().getStatus(), 
    									   assetData.getAsset().getClassificationId(),
    									   activity.getJpnum());
    	
    	return result;
    }
	
	private boolean checkStatusAssetLevel3(AssetData assetData, String jpnum) throws DataLayerException {
    	
    	Assert.notNull(assetData, "assetData is null");
    	Assert.hasText(jpnum, "activity is null");
    	
    	Activity activity = activityHandler.getActivityByJpnum(jpnum);
    	if(activity == null) {
    		log.error(" - Activity not found by jpnum='{}'", jpnum);
    		return false;
    	}
    	
    	return checkStatusAssetLevel3(assetData, activity);
    }
	
	private boolean checkStatusAssetLevel3(AssetData assetData, Activity activity) throws DataLayerException {
    	
    	Assert.notNull(assetData, "assetData is null");
    	Assert.notNull(activity, "activity is null");
    	
    	boolean stazione = checkIfIsStazione(assetData.getAsset().getAssetNum());
    	
    	
    	String attributeToChek = stazione 
    								? String.format("%s_%s", Constants.ATTR_IDONEITA_BASE, activity.getX4atridoneita() != null
    																						? activity.getX4atridoneita()
    																						: "NOT_FOUND")
    								: Constants.ATTR_IDONEITA_LINEA;
    	
    	if(assetData.getAttributi().containsKey(attributeToChek)) {
			AttributiAsset attrIdoneita = assetData.getAttributi().get(attributeToChek);
			log.debug("attribute to check is: {}, value={}", attributeToChek, attrIdoneita != null ? attrIdoneita.getAlnValue() : "null");
			return Assorted.equals(Constants.STATO_IDONEO, attrIdoneita != null 
																? attrIdoneita.getAlnValue()
																: null);
		}
    	log.debug("No attribute {} for asset {}", attributeToChek, assetData.getAsset().getAssetNum());
    	
    	return false;
    }

	
	public boolean checkStatusAssetAllLevel(String status, 
											 String classificationId,
											 String jpnum) 
    {
    	
    	Assert.hasText(status, "status is null or empty");
    	Assert.hasText(jpnum, "jpnum is null or empty");
    	
    	String statusToCheck = null;
    	
    	// check idoneita tra le eccezioni 
    	List<X4TabEccezione> listEccezioni = idoneitaService.loadEccezioniByActivity(jpnum);

		if (Assorted.isNotEmpty(listEccezioni)) 
		{
			X4TabEccezione eccezione = listEccezioni.stream()
													.filter(x -> Assorted.equals(x.getValidState(), status))
													.findFirst()
													.orElse(null);
			statusToCheck = eccezione != null
							? eccezione.getValidState()
							: null;
		} else {
			// check in idoneita
    		List<X4TabIdoneita> listIdoneita = idoneitaService.loadIdoneitaByClassificationId(classificationId);
    		X4TabIdoneita idoneita = listIdoneita.stream()
    											  .filter(x -> Assorted.equals(x.getValidState(), 
    													  					   status))
    											  .findFirst()
    											  .orElse(null);
			statusToCheck = idoneita != null
							? idoneita.getValidState()
							: null;
		}

    	log.debug("statusToCheck: {}", statusToCheck);
    	
    	return statusToCheck != null;
    }



    @SuppressWarnings("unchecked")
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public List<String> findAssetForTesatura(AssetData assetData, AssetData parent)
    {
        List<String> result = null;
        long startTime      = System.currentTimeMillis();
        Asset asset         = assetData.getAsset();
        log.debug("call findAssetForTesatura({}, {}, {}, {}, {}) |{}|", asset.getAssetNum(), asset.getParent(), asset.getSiteId(), asset.getClassStructureId(), asset.getAncestor(), startTime);
        
        Cache cache = cacheManagerHandler.cacheManager().getCache(Constants.CACHE_TRAINANTE_TRAINATO);
        ValueWrapper valueWrapper = cache.get(asset.getAssetNum());
        if (Assorted.notNull(valueWrapper) && Assorted.notNull(valueWrapper.get())) 
        {
            return (List<String>) valueWrapper.get();
        }
        
        if (Constants.CLASS_CONDUTTORE.equals(asset.getClassificationId()))
        {
            AttributiAsset attrLinRifPos = assetData.getAttributi().get(Constants.LIN_RIF_POSIZIONE);
            if (Assorted.notNull(attrLinRifPos) && Assorted.notNull(attrLinRifPos.getNumValue()))
                result = assetRepository.findAssetForTesaturaForConduttore(asset.getAncestor(), attrLinRifPos.getNumValue(), parent.getAsset().getX3RootAssetNum());
        } else if (Constants.CLASS_FUNE_GUARDIA.equals(asset.getClassificationId()))
            result = assetRepository.findAssetForTesaturaForFune(asset.getAssetNum(), asset.getSiteId(), asset.getClassStructureId(), asset.getAncestor());

        if (Assorted.notNull(result))
        {
            cacheManagerHandler.put(Constants.CACHE_TRAINANTE_TRAINATO, asset.getAssetNum(), result);
            for (String assetNum: result)
            {
                cacheManagerHandler.put(Constants.CACHE_TRAINANTE_TRAINATO, assetNum, result);
            }
        }
        
        long endTime = System.currentTimeMillis();
        log.info("end findAssetForTesatura({}, {}, {}, {}, {}) ==> {} |{}|", asset.getAssetNum(), asset.getParent(), asset.getSiteId(), asset.getClassStructureId(), asset.getAncestor(), result, endTime - startTime);
        return result;
    }
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public List<String> getAssetnumByParentAndClassStructureIds(String assetnum, List<String> classStructureIds){
    	
    	List<String> result = null;
    	long startTime      = System.currentTimeMillis();
        log.debug("call getAssetnumByParentAndClassStructureIds({}, {}) |{}|", assetnum, classStructureIds, startTime);
    
    	result = assetRepository.getAssetnumByParentAndClassStructureIds(assetnum, classStructureIds );
    	long endTime = System.currentTimeMillis();
    	log.info("end getAssetnumByParentAndClassStructureIds({}, {]) ==> {} |{}|", assetnum, classStructureIds, result, endTime - startTime);
    	
    	return result;
	}
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public List<ConditionTaskProjection> findTaskConditions(String assetnum, String jpnum)
    {
        long startTime = System.currentTimeMillis();
        log.debug("call findTaskConditions({}, {}) |{}|", assetnum, jpnum, startTime);
        List<ConditionTaskProjection> retrivedConditionTask = assetRepository.findTaskConditions(assetnum, jpnum);
        long endTime = System.currentTimeMillis();
        log.info("end findTaskConditions({}, {}) |{}|", assetnum, jpnum, endTime - startTime);
        return retrivedConditionTask;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Transactional
    public List<NoConditionTaskProjection> findTaskNoConditions(String jpnum, String classStructureIdAsset)
    {
        long startTime = System.currentTimeMillis();
        log.debug("call findTaskNoConditions({}-{}) |{}|", jpnum, classStructureIdAsset, startTime);
        List<NoConditionTaskProjection> retrivedNoConditionTask = assetRepository.findTaskNoConditions(jpnum, classStructureIdAsset);
        long endTime = System.currentTimeMillis();
        log.info("end findTaskNoConditions({}-{}) |{}|", jpnum, classStructureIdAsset, endTime - startTime);
        return retrivedNoConditionTask;

    }
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public Boolean checkIfIsStazione(String assetnum) throws DataLayerException 
    {
    	log.debug(" - Checking if asset is station {}", assetnum);
        return assetDataCacheService.checkIfIsStazione(assetnum);
    }
    
    public Integer getLevel(String classStructureId) {
		return assetDataCacheService.getLevel(classStructureId);
	}
}
