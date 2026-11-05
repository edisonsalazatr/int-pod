package it.terna.nas.service.exposure.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Values;
import it.terna.nas.service.dal.entity.eam.X4CatModello;
import it.terna.nas.service.dal.service.X4CatModelloAssetService;
import it.terna.nas.service.dal.service.X4CatModelloService;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ModelloResolver {
	
	@Autowired
	private X4CatModelloService modelloService;
	
	@Autowired
	private X4CatModelloAssetService modelloAssetService;
	
	public Long getIdByCode(String modelloId) {
		X4CatModello x4Catmodello = modelloService.getByModelloId(Constants.SELEG);
		if(x4Catmodello == null) {
			log.warn(" --- No model can be found by '{}'", Constants.SELEG);
		}
		
		return x4Catmodello != null
				? x4Catmodello.getId()
				: null;
	}
	
	
	/**
	 * 
	 * @param modelloIdAsString
	 * @param assetData
	 * @return
	 */
	public ClusterDto searchModelloAsset(Long modelloId, AssetData assetData) {
		
		Assert.notNull(modelloId, "modelloId is null or empty");
		
		List<ClusterDto> listClusterByModello = modelloAssetService.getById(modelloId);
		
		if(listClusterByModello == null || listClusterByModello.size() == 0) return null;
		
		ClusterDto clusterDto = lookupForModel(listClusterByModello, assetData);
		
		return clusterDto;
	}
	
	
	private ClusterDto lookupForModel(List<ClusterDto> clusters,
									  AssetData assetData) {
		
		log.info("  - lookupForModel matching asset {} ", assetData.getAsset().getAssetNum());
		
		if(Assorted.isEmpty(clusters)) {
			log.info("  clusters is empty");
			return null;
		}
		
		Map<String, String> mapAttributiAsset = assetData.getAttributi()
														 .entrySet()
														 .stream()
														 .collect(HashMap::new, 
																 (m,v)-> m.put(v.getKey(), v.getValue().getAlnValue()), 
																 HashMap::putAll);
		
		
		
		List<ClusterDto> matchingClusters = clusters.stream()
										.filter(cluster -> mapAttributiAsset.entrySet()
														  					.containsAll(cluster.getAttributes().entrySet()))
												  							.toList();
		log.debug("  - Expected 1 cluster matching: actually found {}", matchingClusters != null
																			? matchingClusters.size()
																			: 0);
		
		return Assorted.isNotEmpty(matchingClusters) ? matchingClusters.get(Values.FIRST_ELEMENT) : null;
	}
}
