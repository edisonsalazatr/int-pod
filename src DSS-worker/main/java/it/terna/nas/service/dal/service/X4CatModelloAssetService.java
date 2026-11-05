package it.terna.nas.service.dal.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.basement.utility.Values;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.X4CatModelloAsset;
import it.terna.nas.service.dal.repository.X4CatModelloAssetRepository;
import it.terna.nas.service.exposure.service.ClusterDto;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4CatModelloAssetService 
{
	@Autowired 
	private X4CatModelloAssetRepository repository;
	
	@Autowired
	private DataAccessService dataAccessService;
	
	/**
	 * @param modelloId
	 * @return the policy associated to modelloId
	 */
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	@Cacheable(cacheManager = Constants.CACHE_MANAGER, 
	           value = Constants.CACHE_MODELLO_ASSET, 
	           key = "#modelloId", 
	           unless = "#result==null")
	public List<ClusterDto> getById(Long modelloId) 
    {
    	log.info(" - Searching X4CatModelloAsset Table for asset model with ID={}", modelloId);
    	Assert.notNull(modelloId, "modelloAssetId is null");
    	
    	List<X4CatModelloAsset> listModelloAsset = repository.findByX4CatModelloId(modelloId);
    	
    	List<ClusterDto> clusters = Assorted.isNotEmpty(listModelloAsset)
    									? listModelloAsset.stream()
    											.collect(Collectors.groupingBy(X4CatModelloAsset:: getIdCluster))
    											.entrySet()
    											.stream()
    											.map(entry -> createCluster(entry))
    											.collect(Collectors.toList())
    									: new ArrayList<>();
    	return clusters;
	}
    
    private ClusterDto createCluster(Entry<Integer, List<X4CatModelloAsset>> entry) {
    	
		Map<String, String> attributes = entry.getValue()
											  .stream()
											  .filter(x -> x.getAttributo() != null && x.getValue() != null)
											  .collect(Collectors.toMap(X4CatModelloAsset::getAttributo, 
													  					X4CatModelloAsset::getValue));
		Double n1 = entry.getValue()
						 .stream()
						 .map(x -> x.getN1())
						 .findFirst()
						 .orElse(null);
		return new ClusterDto(entry.getKey(), new HashMap<>(attributes), n1 != null
												? BigDecimal.valueOf(n1) 
												: null);
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Cacheable(cacheManager = Constants.CACHE_MANAGER, 
               value = Constants.CACHE_MODELLO_ASSET, 
               key = "#idModello", 
               unless = "#result==null")
    public Map<String, Double> getByIdModello(String idModello) 
    {
        long startTime = System.currentTimeMillis();
        log.info("call getByIdModello({}) |{}|", idModello, startTime);
        Assert.notNull(idModello, "modelloAssetId is null");
        List<X4CatModelloAsset> catModelloAssetList = repository.findByIdModello(idModello);
        Map<String, Double> catValue = null;
        
        if (Assorted.isNotEmpty(catModelloAssetList))
            catValue = catModelloAssetList.stream().collect(Collectors.toMap(X4CatModelloAsset::getValue, X4CatModelloAsset::getN1));
        
        return catValue;
    }
    
    
    /**
     * Search X4CATMODELLOASSET to match the required model by value. We need to retrieve column N1
     * Query to execute is:
     * SELECT * FROM X4CATMODELLOASSET WHERE X4CATMODELLO = :X4CATMODELLO AND VALUE = :VALUE
     */
    public X4CatModelloAsset getByModelloAndValue(Long x4CatModelloId, String value) {
    	log.info(" - Searching X4CatModelloAsset Table for asset witch x4CatModelloId='{}' AND VALUE='{}'", x4CatModelloId, 
    																										value);
    	// create model to use as example 
    	X4CatModelloAsset modelloAsset = new X4CatModelloAsset();
    	modelloAsset.setX4CatModelloId(x4CatModelloId);
    	modelloAsset.setValue(value);
    	
    	// matcher
    	ExampleMatcher matcher = ExampleMatcher.matching()
    										   .withIgnoreNullValues();
    	
    	X4CatModelloAsset modelloTarget = repository.findOne(Example.of(modelloAsset, matcher))
    												.orElse(null);
    	
    	log.info(" - {} record from X4MODELLOASSET Table by x4CatModelloId='{}' AND VALUE='{}'", 
    																			modelloTarget!= null 
    																				? "FOUND" 
    																				: "NOT FOUND",
    																x4CatModelloId, 
    																value);
    	
    	return modelloTarget;
    }
    
    /**
     * Search model value N1 for asset of type STATIONS (all type except CONDUTTORE AND FUNE DI GUARDIA
     * @param costruttore
     * @param tipoComponente
     * @return
     */
    public X4CatModelloAsset getByCostruttoreAndTipo(String costruttore, String tipoComponente) {
    	
    	String query = """
			    SELECT * 
			    FROM X4CATMODELLOASSET 
			    WHERE 
			       ATTRIBUTO = 'X3COSTRUTTORECOMPONENTE' 
			       AND VALUE = :costruttore
			       AND IDCLUSTER = ( SELECT IDCLUSTER
			                           FROM X4CATMODELLOASSET
			                           WHERE 
			                               ATTRIBUTO = 'X3TIPOCOMPONENTE'
			                               AND VALUE = :value)
			    	""";
    	X4CatModelloAsset result = null;
    	try {
    		Map<String, String> queryParam = Map.of("costruttore", costruttore, 
    			    								"value", 	   tipoComponente);
    		List<X4CatModelloAsset> listResult = dataAccessService.search(X4CatModelloAsset.class, 
    																	  query,
    																	  queryParam,
    																	  true,
    																	  true);
    		result = Assorted.isNotEmpty(listResult)
    					? listResult.get(Values.FIRST_ELEMENT)
    					: null;
 
    	}catch (DataLayerException dle) {
			log.error("  Error executing query {} with params value ['{}', '{}']", query, costruttore, tipoComponente);
		}
    	
    	return result;
    }
}
