package it.terna.nas.service.exposure.handler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.dal.entity.eam.ClassStructureTipology;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.service.ClassificationQueryService;
import it.terna.nas.service.dal.service.repository.AssetRepository;
import it.terna.nas.service.dal.service.repository.IAssetCount;
import it.terna.nas.service.dal.service.repository.IAssetMinimal;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AssetDiscoverer 
{
	@Autowired 
	private AssetRepository assetRepository;
	
	@Autowired
	private ClassificationQueryService classificationService;
	
	
	public StreamHolder<IAssetMinimal> getAssetsTronco(String troncoNum, List<String> classStructureIds)
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getAssetsTronco({}, {}) |{}|", troncoNum, classStructureIds, startTime);
        
        Integer numAsset                     = assetRepository.findCountByAssetsTronco(troncoNum, classStructureIds);
        Stream<IAssetMinimal> assetNumStream = assetRepository.findAssetsTronco(troncoNum, classStructureIds);
        
        long endTime   = System.currentTimeMillis();
        log.info("end getAssetsTronco({}, {}) |{}|", troncoNum, classStructureIds, endTime - startTime);
        return new StreamHolder<>(assetNumStream, numAsset != null ? numAsset : 0);
    }

	/**
	 * default behaviour
	 * @param policy
	 * @param classStructureIds
	 * @return
	 */
	public StreamHolder<IAssetMinimal> getDefaultAssetStream(X4Policy policy, List<String> classStructureIds) 
	{
	    long startCount = System.currentTimeMillis();
		Integer numAsset = assetRepository.findCountByClassStructureIdAndEsitoOk(classStructureIds, Constants.SITE);
		log.debug("Asset count completed (classStructureIds: {}, site: {}, result: {}) in {} ms",
	              classStructureIds, Constants.SITE, numAsset, System.currentTimeMillis() - startCount);
	    long startStream = System.currentTimeMillis();
		Stream<IAssetMinimal> assetNumStream = assetRepository.findAllByClassStructureId(classStructureIds, Constants.SITE);

	    log.debug("Asset stream loaded (classStructureIds: {}, site: {}) in {} ms",
	              classStructureIds, Constants.SITE, System.currentTimeMillis() - startStream);
	    return new StreamHolder<>(assetNumStream, numAsset != null
															? numAsset
															: 0);
	}
	
	/**
	 * 
	 * @param policy
	 * @param classStructureIds
	 * @return
	 * @throws DataLayerException 
	 */
	public StreamHolder<IAssetMinimal> getStazioniAssetStream(X4Policy policy, List<String> classStructureIds) 
	        throws DataLayerException 
	{
		log.info("  --- Starting retrieving data for stazioni");
		ClassStructureTipology classStructureStallo = getClassStructureByClassificationId(Constants.CLASS_STALLO);
		
		log.debug("  --- Retrieved classStructureid for 'STALLO' [{}]", classStructureStallo != null 
																			? classStructureStallo.getClassificationId() 
																			: "NULL");
		Stream<IAssetMinimal> assetNumStream = assetRepository.findStalli(classStructureIds, classStructureStallo.getClassStructureId());
		
		log.debug(" --- Created stream for STALLO ... ");
		
		List<IAssetCount> listAssetCount = assetRepository.findStazioniWithNumeroStalli(classStructureIds, 
																						   classStructureStallo.getClassStructureId());
		log.debug(" --- Retrieved all assets of type 'STALLO', count = {} ... ", Assorted.isNotEmpty(listAssetCount) 
																					? listAssetCount.size()
																					: 0);
		Map<String, Integer> mapParentCount = Assorted.isNotEmpty(listAssetCount)
												? listAssetCount.stream()
															    .collect(Collectors.toMap(x -> x.getAsset(), 
															    						  x -> x.getChildren()))
												: new HashMap<>();
		
		
		return new StreamHolder<>(assetNumStream, mapParentCount);
	}
	
	
	
	private ClassStructureTipology getClassStructureByClassificationId(String classificationId) 
	        throws DataLayerException 
	{
	    log.debug("call getClassStructureByClassificationId({})", classificationId);
	    return classificationService.getClassStructureTipology(classificationId);
	}
}
