package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.source.connector.provider.DataSourceContextHolder;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.*;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.utility.Catalog;
import it.terna.nas.service.material.utility.Catalog.AttributeAssetType;
import it.terna.nas.service.material.utility.Catalog.AttributeType;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.SqlQueryConstants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetDataCacheService 
{		
	@Autowired
    private DataAccessService   dataAccessService;
	
	@Autowired
    private ClassStructureService classStructureService;
	
	@Cacheable(cacheManager = Constants.CACHE_MANAGER, 
	 		   value = Constants.CACHE_ASSET, 
	 		   key = "#assetNum", 
	 		   unless = "#result==null")
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public AssetData createAssetData(String assetNum, long startTime) throws EAMException {
		log.info("Starting to retrieve asset...");
	    Map<String, String> contextMap = MDC.getCopyOfContextMap();
		    CompletableFuture<List<Asset>> assetFuture = CompletableFuture.supplyAsync(() -> {
		        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
		        if (Assorted.notNull(contextMap)) {
		            MDC.setContextMap(contextMap);
		        }
		        try {
		            List<Asset> result = getAssetByAssetNum(assetNum);
		            log.debug("Asset retrieved. Number of elements: {}", result.size());
		            return result;
		        } catch (Exception e) {
		            log.error("Error while retrieving asset for assetNum: {}", assetNum);
		            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving interferenze"));
		        } finally {
		        	MDC.clear();
		            DataSourceContextHolder.clear();
		        }
		    });
		    
		 
		    
		    log.debug("Starting to retrieve tassiGA...");
		    CompletableFuture<List<HealthIndexAsset>> tassiFuture = assetFuture.thenCompose(assetList -> {
		        if (Assorted.notNull(contextMap)) {
		            MDC.setContextMap(contextMap);
		        }
		        try {
		            if (!assetList.isEmpty()) {
		                String classStructureId = assetList.stream().findFirst().get().getClassStructureId(); 
		                return CompletableFuture.supplyAsync(() -> {
		    		        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
		                    List<HealthIndexAsset> result;
							try {
								result = getTassiGAByClassstructureId(classStructureId);
			                    log.debug("TassiGA retrieved. Number of elements: {}", Assorted.isEmpty(result) 
			                    																		? 0 
			                    																		: result.size());
			                    return result;
							} catch (DataLayerException e) {
					            log.error("Error while retrieving TassiGA for classstructureid: {}", classStructureId);
					            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving TassiGA"));
							}
		                });
		            } else {
		            	log.warn("There are no assets with this assetNum: {}", assetNum);
		                throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving TassiGA"));
		            }
		        } catch (Exception e) {
		            log.error("Error while retrieving TassiGA for assetNum: {}", assetNum);
		            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving TassiGA"));
		        } finally {
		            MDC.clear();
		            DataSourceContextHolder.clear();
		        }
		    });
		    
		    log.debug("Starting to retrieve interferenze...");
		    CompletableFuture<List<InterferenzeAsset>> interferenzeFuture = CompletableFuture.supplyAsync(() -> {
		        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
		        if (Assorted.notNull(contextMap)) {
		            MDC.setContextMap(contextMap);
		        }
		        try {
		            List<InterferenzeAsset> result = getInterferenzeByAssetNum(assetNum);
		            log.debug("Interferenze retrieved. Number of elements: {}", Assorted.isEmpty(result) 
																					? 0 
																					: result.size());
		            return result;
		        } catch (Exception e) {
		            log.error("Error while retrieving interferenze for assetNum: {}", assetNum);
		            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving interferenze"));
		        } finally {
		        	MDC.clear();
		            DataSourceContextHolder.clear();
		        }
		    });

		    log.debug("Starting to retrieve difformita...");
		    CompletableFuture<List<DifformitaAsset>> difformitaFuture = CompletableFuture.supplyAsync(() -> {
		        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
		        if (Assorted.notNull(contextMap)) {
		            MDC.setContextMap(contextMap);
		        }
		        try {
		            List<DifformitaAsset> result = getDifformitaByAssetNum(assetNum);
		            log.debug("Difformita retrieved. Number of elements: {}", Assorted.isEmpty(result) 
																					? 0 
																					: result.size());
		            return result;
		        } catch (Exception e) {
		            log.error("Error while retrieving difformita for assetNum: {}", assetNum);
		            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving difformita"));
		        } finally {
		        	MDC.clear();
		            DataSourceContextHolder.clear();
		        }
		    });

		    log.debug("Starting to retrieve attivita...");
		    CompletableFuture<List<AttivitaAsset>> attivitaFuture = CompletableFuture.supplyAsync(() -> {
		        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
		        if (Assorted.notNull(contextMap)) {
		            MDC.setContextMap(contextMap);
		        }
		        try {
		            List<AttivitaAsset> result = getAttivitaByAssetNum(assetNum);
		            log.debug("Attivita retrieved. Number of elements: {}", Assorted.isEmpty(result) 
																				? 0 
																				: result.size());
		            return result;
		        } catch (Exception e) {
		            log.error("Error while retrieving attivita for assetNum: {}", assetNum);
		            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving attivita"));
		        } finally {
		        	MDC.clear();
		            DataSourceContextHolder.clear();
		        }
		    });

		    log.debug("Starting to retrieve variabili...");
		    CompletableFuture<List<VariabileAsset>> variabiliFuture = CompletableFuture.supplyAsync(() -> {
		        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
		        if (Assorted.notNull(contextMap)) {
		            MDC.setContextMap(contextMap);
		        }
		        try {
		            List<VariabileAsset> result = getVariabileByAssetNum(assetNum);
		            log.debug("Variabili retrieved. Number of elements: {}", Assorted.isEmpty(result) 
																						? 0 
																						: result.size());
		            return result;
		        } catch (Exception e) {
		            log.error("Error while retrieving variabili for assetNum: {}", assetNum);
		            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving variabili"));
		        } finally {
		        	MDC.clear();
		            DataSourceContextHolder.clear();
		        }
		    });

		    log.debug("Starting to retrieve attributi...");
		    CompletableFuture<List<AttributiAsset>> attributiFuture = CompletableFuture.supplyAsync(() -> {
		        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
		        if (Assorted.notNull(contextMap)) {
		            MDC.setContextMap(contextMap);
		        }
		        try {
		            List<AttributiAsset> result = getAttributiByAssetNum(assetNum);
		            log.debug("Attributi retrieved. Number of elements: {}", Assorted.isEmpty(result) 
																						? 0 
																						: result.size());
		            return result;
		        } catch (Exception e) {
		            log.error("Error while retrieving attributi for assetNum: {}", assetNum);
		            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving attributi"));
		        } finally {
		        	MDC.clear();
		            DataSourceContextHolder.clear();
		        }
		    });
		    
		    log.debug("Starting to retrieve health index...");
		    CompletableFuture<List<HealthIndexAsset>> hiFuture = CompletableFuture.supplyAsync(() -> {
		        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
		        if (Assorted.notNull(contextMap)) {
		            MDC.setContextMap(contextMap);
		        }
		        try {
		            List<HealthIndexAsset> result = getHealthIndexByAssetNum(assetNum);
		            log.debug("Health Index retrieved. Number of elements: {}", Assorted.isEmpty(result) 
		            																		? 0 
		            																		: result.size());
		            return result;
		        } catch (Exception e) {
		            log.error("Error while retrieving health index for assetNum: {}", assetNum);
		            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving Health Index"));
		        } finally {
		            DataSourceContextHolder.clear();
		            MDC.clear();
		        }
		    });
		    
		    log.debug("Starting to retrieve misure...");
		    CompletableFuture<List<VariabileAsset>> misureFuture = CompletableFuture.supplyAsync(() -> {
		        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
		        if (Assorted.notNull(contextMap)) {
		            MDC.setContextMap(contextMap);
		        }
		        try {
		            List<VariabileAsset> result = getMisureByAssetNum(assetNum);
		            log.debug("Misure retrieved. Number of elements: {}", Assorted.isEmpty(result) 
																				? 0 
																				: result.size());
		            return result;
		        } catch (Exception e) {
		            log.error("Error while retrieving misure for assetNum: {}", assetNum);
		            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving misure"));
		        } finally {
		        	MDC.clear();
		            DataSourceContextHolder.clear();
		        }
		    });

		    log.debug("Waiting for all operations to complete...");
		    CompletableFuture<Void> allFutures = CompletableFuture.allOf(
		        interferenzeFuture,
		        difformitaFuture,
		        attivitaFuture,
		        variabiliFuture,
		        attributiFuture,
		        hiFuture,
		        assetFuture,
		        misureFuture,
		        tassiFuture
		    );
		    allFutures.join();

		    long endTime = System.currentTimeMillis();
		    log.info("All operations completed in {} ms", endTime - startTime);

		    Optional<Asset> assetOptional = assetFuture.join().stream().findFirst();
		    Asset asset = assetOptional.orElseThrow(() -> new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Asset not found!"));
		    List<InterferenzeAsset> interferenzeList = interferenzeFuture.join();
		    List<DifformitaAsset> difformitaList = difformitaFuture.join();
		    List<AttivitaAsset> attivitaList = attivitaFuture.join();
		    List<VariabileAsset> variabiliList = variabiliFuture.join();
		    List<AttributiAsset> attributiList = attributiFuture.join();
		    List<HealthIndexAsset> healthIndexList = hiFuture.join();
		    List<VariabileAsset> misureList = misureFuture.join();
		    List<HealthIndexAsset> tassiGAList = tassiFuture.join();

		    Map<String, List<InterferenzeAsset>> interferenzeMap = interferenzeList.stream()
		            .collect(Collectors.groupingBy(interferenza -> String.valueOf(interferenza.getX4CatInterfId())));

		    Map<String, List<DifformitaAsset>> difformitaMap = difformitaList.stream()
		            .collect(Collectors.groupingBy(difformita -> String.valueOf(difformita.getX4CatDifforId())));
		    
		    

		    Map<String, AttributiAsset> attributiMap = attributiList.stream()
		            .collect(Collectors.toMap(
		                AttributiAsset::getAssetAttrId, 
		                Function.identity(), 
		                (existing, duplicate) -> {
		                    log.warn("Duplicate key found for AttributiAsset: {}. Keeping the existing value: {}, ignoring the duplicate: {}",
		                             existing.getAssetAttrId(), existing, duplicate);
		                    return existing; 
		                }
		            ));

		    addOtherAttributeForAsset(attributiMap, asset);
		    
		    // Variabili e misure
		    Map<String, VariabileAsset> mapAllMeasures = new HashMap<>();
		    
		    Map<String, VariabileAsset> variabiliMap = variabiliList.stream()
		            .collect(Collectors.toMap(
		                variabile -> String.valueOf(variabile.getX4CatMisureId()), 
		                Function.identity(), 
		                (existing, current) -> {
		                	VariabileAsset newest = misuraComparator.compare(existing, current) <= 0
																		                    		? existing
																		                    		: current;
		                    log.warn("Duplicate key found for VariabileAsset: {}. Keeping the newest value: {}, ignoring the old: {}",
		                    															newest.getX4CatMisureId(), 
		                    															newest, 
		                    															existing);
		                    return newest; 
		                }
		            ));
		    if(Assorted.isNotEmpty(variabiliMap)) {
		    	mapAllMeasures.putAll(variabiliMap);
		    }
		    
		    Map<String, VariabileAsset> misureMap = misureList.stream()
		            .collect(Collectors.toMap(
		                misura -> String.valueOf(misura.getX4CatMisureId()), 
		                Function.identity(), 
		                (existing, current) -> {
		                	VariabileAsset newest = misuraComparator.compare(existing, current) <= 0
																		                    		? existing
																		                    		: current;
		                    log.warn("Duplicate key found for VariabileAsset: {}. Keeping the newest value: {}, ignoring the old: {}",
		                    															newest.getX4CatMisureId(), 
		                    															newest, 
		                    															existing);
		                    return newest; 
		                }
		            ));
		    
		    if(Assorted.isNotEmpty(misureMap)) {
		    	mapAllMeasures.putAll(misureMap);
		    }
		    
		    Map<String, HealthIndexAsset> hiMap = healthIndexList.stream()
		            .collect(Collectors.toMap(
		                HealthIndexAsset::getAttributeName, 
		                Function.identity(), 
		                (existing, duplicate) -> {
		                    log.warn("Duplicate key found for HealthIndexAsset: {}. Keeping the existing value: {}, ignoring the duplicate: {}",
		                             existing.getAttributeName(), existing, duplicate);
		                    return existing;
		                }
		            ));

		        

		        Map<String, HealthIndexAsset> tassiGAMap = tassiGAList.stream()
		            .collect(Collectors.toMap(
		                HealthIndexAsset::getAttributeName, 
		                Function.identity(), 
		                (existing, duplicate) -> {
		                    log.warn("Duplicate key found for tassiGA: {}. Keeping the existing value: {}, ignoring the duplicate: {}",
		                             existing.getAttributeName(), existing, duplicate);
		                    return existing;
		                }
		            ));


		    // recupera classstructureid
		    Integer level = getLevel(asset.getClassStructureId());

		    AssetData assetData = AssetData.builder()
		    		.asset(asset)
		            .interferenze(interferenzeMap)
		            .difformita(difformitaMap)
		            .attributi(attributiMap)
		            .misure(mapAllMeasures)
		            .attivita(attivitaList)
		            .hi(hiMap)
		            .tassiGA(tassiGAMap)
		            .level(level)
		            .build();

		  
		    log.info("FULL: AssetData successfully created for assetNum: {} in cache", assetNum);	
		    
		    if (log.isDebugEnabled())
		        log.debug("FULL: assetData : {} ", assetData);
		    
		    return assetData;
	}
	
	@Cacheable(cacheManager = Constants.CACHE_MANAGER, 
 		   value = Constants.CACHE_ASSET, 
 		   key = "#assetNum + '" + Constants.SLIM_VERSION + "'", 
 		   unless = "#result==null")
	@Datasource(datasourceKey = EDatasourceKey.EAM)
	public AssetData createSlimAssetData(String assetNum, long startTime) throws EAMException {
	    log.info("Starting to retrieve slim data for assetNum...");

	    Map<String, String> contextMap = MDC.getCopyOfContextMap();

	    CompletableFuture<List<Asset>> assetFuture = CompletableFuture.supplyAsync(() -> {
	        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
	        if (Assorted.notNull(contextMap)) {
	            MDC.setContextMap(contextMap);
	        }
	        try {
	            List<Asset> result = getAssetByAssetNum(assetNum);
	            log.debug("Asset retrieved. Number of elements: {}", result.size());
	            return result;
	        } catch (Exception e) {
	            log.error("Error while retrieving asset for assetNum: {}", assetNum);
	            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving asset"));
	        } finally {
	            MDC.clear();
	            DataSourceContextHolder.clear();
	        }
	    });

	    log.debug("Starting to retrieve health index...");
	    CompletableFuture<List<HealthIndexAsset>> hiFuture = CompletableFuture.supplyAsync(() -> {
	        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
	        if (Assorted.notNull(contextMap)) {
	            MDC.setContextMap(contextMap);
	        }
	        try {
	            List<HealthIndexAsset> result = getHealthIndexByAssetNum(assetNum);
	            log.debug("Attributi retrieved. Number of elements: {}", result.size());
	            return result;
	        } catch (Exception e) {
	            log.error("Error while retrieving health index for assetNum: {}", assetNum);
	            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving attributi"));
	        } finally {
	            DataSourceContextHolder.clear();
	            MDC.clear();
	        }
	    });
	    
	    log.debug("Starting to retrieve tassiGA...");
	    CompletableFuture<List<HealthIndexAsset>> tassiFuture = assetFuture.thenCompose(assetList -> {
	        if (Assorted.notNull(contextMap)) {
	            MDC.setContextMap(contextMap);
	        }
	        try {
	            if (!assetList.isEmpty()) {
	                String classStructureId = assetList.stream().findFirst().get().getClassStructureId(); 
	                return CompletableFuture.supplyAsync(() -> {
	    		        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
	                    List<HealthIndexAsset> result;
						try {
							result = getTassiGAByClassstructureId(classStructureId);
		                    log.debug("TassiGA retrieved. Number of elements: {}", Assorted.isEmpty(result) 
		                    																		? 0 
		                    																		: result.size());
		                    return result;
						} catch (DataLayerException e) {
				            log.error("Error while retrieving TassiGA for classstructureid: {}", classStructureId);
				            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving TassiGA"));
						}
	                });
	            } else {
	            	log.warn("There are no assets with this assetNum: {}", assetNum);
	                throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving TassiGA"));
	            }
	        } catch (Exception e) {
	            log.error("Error while retrieving TassiGA for assetNum: {}", assetNum);
	            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving TassiGA"));
	        } finally {
	            MDC.clear();
	            DataSourceContextHolder.clear();
	        }
	    });

	    log.debug("Starting to retrieve attributi...");
	    CompletableFuture<List<AttributiAsset>> attributiFuture = CompletableFuture.supplyAsync(() -> {
	        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
	        if (Assorted.notNull(contextMap)) {
	            MDC.setContextMap(contextMap);
	        }
	        try {
	            List<AttributiAsset> result = getAttributiByAssetNum(assetNum);
	            log.debug("Attributi retrieved. Number of elements: {}", result.size());
	            return result;
	        } catch (Exception e) {
	            log.error("Error while retrieving attributi for assetNum: {}", assetNum);
	            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving attributi"));
	        } finally {
	        	MDC.clear();
	            DataSourceContextHolder.clear();
	        }
	    });
	    
	    log.debug("Starting to retrieve variabili...");
	    CompletableFuture<List<VariabileAsset>> variabiliFuture = CompletableFuture.supplyAsync(() -> {
	        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
	        if (Assorted.notNull(contextMap)) {
	            MDC.setContextMap(contextMap);
	        }
	        try {
	            List<VariabileAsset> result = getVariabileByAssetNum(assetNum);
	            log.debug("Variabili retrieved. Number of elements: {}", Assorted.isEmpty(result) 
						? 0 
						: result.size());
	            return result;
	        } catch (Exception e) {
	            log.error("Error while retrieving variabili for assetNum: {}", assetNum);
	            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving variabili"));
	        } finally {
	        	MDC.clear();
	            DataSourceContextHolder.clear();
	        }
	    });
	    
	    log.debug("Starting to retrieve misure...");
	    CompletableFuture<List<VariabileAsset>> misureFuture = CompletableFuture.supplyAsync(() -> {
	        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
	        if (Assorted.notNull(contextMap)) {
	            MDC.setContextMap(contextMap);
	        }
	        try {
	            List<VariabileAsset> result = getMisureByAssetNum(assetNum);
	            log.debug("Misure retrieved. Number of elements: {}", Assorted.isEmpty(result) 
																			? 0 
																			: result.size());
	            return result;
	        } catch (Exception e) {
	            log.error("Error while retrieving misure for assetNum: {}", assetNum);
	            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving misure"));
	        } finally {
	        	MDC.clear();
	            DataSourceContextHolder.clear();
	        }
	    });
	    
	    log.debug("Starting to retrieve attivita...");
	    CompletableFuture<List<AttivitaAsset>> attivitaFuture = CompletableFuture.supplyAsync(() -> {
	        DataSourceContextHolder.setDatasourceKey(EDatasourceKey.EAM.getKey());
	        if (Assorted.notNull(contextMap)) {
	            MDC.setContextMap(contextMap);
	        }
	        try {
	            List<AttivitaAsset> result = getAttivitaByAssetNum(assetNum);
	            log.debug("Attivita retrieved. Number of elements: {}", Assorted.isEmpty(result) 
																			? 0 
																			: result.size());
	            return result;
	        } catch (Exception e) {
	            log.error("Error while retrieving attivita for assetNum: {}", assetNum);
	            throw new CompletionException(new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Error retrieving attivita"));
	        } finally {
	        	MDC.clear();
	            DataSourceContextHolder.clear();
	        }
	    });

	    CompletableFuture<Void> allFutures = CompletableFuture.allOf(
	        assetFuture,
	        hiFuture,
	        attributiFuture,
	        variabiliFuture,
	        misureFuture,
	        tassiFuture,
	        attivitaFuture
	    );
	    
	    allFutures.join();

	    long endTime = System.currentTimeMillis();
	    log.info("Slim version operations completed in {} ms", endTime - startTime);

	    Optional<Asset> assetOptional = assetFuture.join().stream().findFirst();
	    Asset asset = assetOptional.orElseThrow(() -> new EAMException(Catalog.EAMErrorMapping.EAM_9999.getEamCode(), "Asset not found!"));

	    List<AttributiAsset> attributiList = attributiFuture.join();
	    List<HealthIndexAsset> healthIndexList = hiFuture.join();
	    List<HealthIndexAsset> tassiGAList = tassiFuture.join();
	    List<AttivitaAsset> attivitaList = attivitaFuture.join();
	    
	    List<VariabileAsset> variabiliList = variabiliFuture.join();
	    List<VariabileAsset> misureList = misureFuture.join();

	    Map<String, AttributiAsset> attributiMap = attributiList.stream()
	            .collect(Collectors.toMap(
	                AttributiAsset::getAssetAttrId, 
	                Function.identity(), 
	                (existing, duplicate) -> {
	                    log.warn("Duplicate key found for AttributiAsset: {}. Keeping the existing value: {}, ignoring the duplicate: {}",
	                             existing.getAssetAttrId(), existing, duplicate);
	                    return existing; 
	                }
	            ));
	    
	    addOtherAttributeForAsset(attributiMap, asset);
	    
	    // Variabili e misure
	    Map<String, VariabileAsset> mapAllMeasures = new HashMap<>();
	    
	    Map<String, VariabileAsset> variabiliMap = variabiliList.stream()
	            .collect(Collectors.toMap(
	                variabile -> String.valueOf(variabile.getX4CatMisureId()), 
	                Function.identity(), 
	                (existing, current) -> {
	                	VariabileAsset newest = misuraComparator.compare(existing, current) <= 0
																	                    		? existing
																	                    		: current;
	                    log.warn("Duplicate key found for VariabileAsset: {}. Keeping the newest value: {}, ignoring the old: {}",
	                    															newest.getX4CatMisureId(), 
	                    															newest, 
	                    															existing);
	                    return newest; 
	                }
	            ));
	    
	    if(Assorted.isNotEmpty(variabiliMap)) {
	    	mapAllMeasures.putAll(variabiliMap);
	    }
	    
	    Map<String, VariabileAsset> misureMap = misureList.stream()
	            .collect(Collectors.toMap(
	                misura -> String.valueOf(misura.getX4CatMisureId()), 
	                Function.identity(), 
	                (existing, current) -> {
	                	VariabileAsset newest = misuraComparator.compare(existing, current) <= 0
	                    		? existing
	                    		: current;
	                	log.warn("Duplicate key found for VariabileAsset: {}. Keeping the newest value: {}, ignoring the old: {}",
	                																					newest.getX4CatMisureId(), 
	                																					newest, 
	                																					existing);
	                	return newest; 
	                }
	            ));
	    
	    if(Assorted.isNotEmpty(misureMap)) {
	    	mapAllMeasures.putAll(misureMap);
	    }
	    
        Map<String, HealthIndexAsset> hiMap = healthIndexList.stream()
            .collect(Collectors.toMap(
                HealthIndexAsset::getAttributeName, 
                Function.identity(), 
                (existing, duplicate) -> {
                    log.warn("Duplicate key found for HealthIndexAsset: {}. Keeping the existing value: {}, ignoring the duplicate: {}",
                             existing.getAttributeName(), existing, duplicate);
                    return existing;
                }
            ));
	       
        Map<String, HealthIndexAsset> tassiGAMap = tassiGAList.stream()
		            .collect(Collectors.toMap(
		                HealthIndexAsset::getAttributeName, 
		                Function.identity(), 
		                (existing, duplicate) -> {
		                    log.warn("Duplicate key found for tassiGA: {}. Keeping the existing value: {}, ignoring the duplicate: {}",
		                             existing.getAttributeName(), existing, duplicate);
		                    return existing;
		                }
		            ));
	        
	        
	    // recupera classstructureid
        Integer level = getLevel(asset.getClassStructureId());

        AssetData assetData = AssetData.builder()
                                       .asset(asset)
                                       .attributi(attributiMap)
                                       .misure(mapAllMeasures)
                                       .level(level)
                                       .hi(hiMap)
                                       .tassiGA(tassiGAMap)
                                       .attivita(attivitaList).build();	    
	    
        log.info("SLIM: AssetData successfully created for assetNum: {} in cache", assetNum); 
        
        if (log.isDebugEnabled())
            log.debug("SLIM: assetData : {} ", assetData);
        
        return assetData;
	}
	
	
    @CachePut(cacheManager = Constants.CACHE_MANAGER, value = Constants.CACHE_ASSET, key = "#assetNum")
    public AssetData saveAssetCache(String assetNum, AssetData assetData)
    {
        log.debug("call saveAssetCache({})", assetData.getAsset().getAssetNum());
        return assetData;
    }

    
    private void addOtherAttributeForAsset(Map<String, AttributiAsset> attributiMap, Asset asset)
    {
	    addAttribute(attributiMap, asset, AttributeAssetType.SOCIETA.getName(), asset.getSocieta());
	    addAttribute(attributiMap, asset, AttributeAssetType.SORT.getName(), asset.getSort());
	    addAttribute(attributiMap, asset, AttributeAssetType.CENTRO_LAVORO_RESPONSABILE.getName(), asset.getCentroLavoroResponsabile());
	    addAttribute(attributiMap, asset, AttributeAssetType.IN_FUNZIONE_DA.getName(), asset.getInstallDate());
	    addAttribute(attributiMap, asset, AttributeAssetType.GRUPPO_RESPONSABILE_PIANIF_MANUTEN.getName(), asset.getGruppoResponsabilePianifManuten());
	    addAttribute(attributiMap, asset, AttributeAssetType.CONTROLLING_AREA.getName(), asset.getControllingArea());
        addAttribute(attributiMap, asset, AttributeAssetType.CENTRO_DI_COSTO.getName(), asset.getCentroDiCosto());
        addAttribute(attributiMap, asset, AttributeAssetType.SEDE_TECNICA_DESCRIZIONE.getName(), asset.getSedeTecnicaDescrizione());
        addAttribute(attributiMap, asset, AttributeAssetType.SEDE_TECNICA_CODICE.getName(), asset.getX3SapId());
        addAttribute(attributiMap, asset, AttributeAssetType.STATO.getName(), asset.getStatus());
        addAttribute(attributiMap, asset, AttributeAssetType.DIVISIONE_UBICAZIONE.getName(), asset.getDivisioneUbicazione());
        addAttribute(attributiMap, asset, AttributeAssetType.SOCIETA_PROPRIETARIA.getName(), asset.getSocietaProprietaria());
        addAttribute(attributiMap, asset, AttributeAssetType.CODICE_COSTRUTTORE.getName(), asset.getCodiceCostruttore());
        addAttribute(attributiMap, asset, AttributeAssetType.MODELLO.getName(), asset.getModello());
        addAttribute(attributiMap, asset, AttributeAssetType.RIFERIMENTO_FASE.getName(), asset.getRiferimentoFase());
        addAttribute(attributiMap, asset, AttributeAssetType.TIPO_COMPONENTE.getName(), asset.getTipoComponente());
        addAttribute(attributiMap, asset, AttributeAssetType.IMPIANTO.getName(), asset.getImpianto());
    }

	
    private void addAttribute(Map<String, AttributiAsset> attributiMap, Asset asset, String name, Object obj)
    {
        if (Assorted.notNull(obj))
        {
            AttributiAsset attributo = new AttributiAsset();
            attributo.setAssetAttrId(name);
            attributo.setAssetNum(asset.getAssetNum());
            attributo.setClassStructureId(asset.getClassStructureId());
            attributo.setSiteId(asset.getSiteId());
            if (obj instanceof String)
            {
                attributo.setDataType(AttributeType.ALN.getCode());
                attributo.setAlnValue((String) obj);
            } else if (obj instanceof Date) {
                attributo.setDataType(AttributeType.DATE.getCode());
                attributo.setDateValue((Date) obj);                
            }
            
            attributiMap.put(name, attributo);
        }
    }

    public Integer getLevel(String classStructureId) {
		Map<String, Integer> mapClassStructureId_Level = classStructureService.buildLevelMap();
		Integer level = null;
		if(mapClassStructureId_Level != null) {
			if(mapClassStructureId_Level.containsKey(classStructureId)) {
				level = mapClassStructureId_Level.get(classStructureId);
			}
		}
		return level;
	}

    private List<InterferenzeAsset> getInterferenzeByAssetNum(String assetNum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getInterferenzeByAssetNum({}) |{}|", assetNum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
		List<InterferenzeAsset> result = dataAccessService.search(InterferenzeAsset.class, SqlQueryConstants.QUERY_INTERFERENZE_ASSETDATA, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getInterferenzeByAssetNum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return Assorted.notNull(result) ? result : new ArrayList<>();
	}
    
    private List<DifformitaAsset> getDifformitaByAssetNum(String assetNum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getDifformitaByAssetNum({}) |{}|", assetNum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
		List<DifformitaAsset> result = dataAccessService.search(DifformitaAsset.class, SqlQueryConstants.QUERY_DIFFORMITA_ASSETDATA, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getDifformitaByAssetNum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return Assorted.notNull(result) ? result : new ArrayList<>();
	}
    
    private List<AttivitaAsset> getAttivitaByAssetNum(String assetNum) 
			throws DataLayerException
	{
		long startTime = System.currentTimeMillis();
		log.debug("call getAttivitaByAssetNum({}) |{}|", assetNum, startTime);		

		Map <String, String> parameters = new HashMap<>();
		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
		List<AttivitaAsset> result = dataAccessService.search(AttivitaAsset.class, SqlQueryConstants.QUERY_ATTIVITA_ASSETDATA, parameters, true, true);
		
		long endTime = System.currentTimeMillis();
		log.debug("  - Search getAttivitaByAssetNum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
	
		return Assorted.notNull(result) ? result : new ArrayList<>();
	}
    
    private List<VariabileAsset> getVariabileByAssetNum(String assetNum) 
  			throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call getVariabileByAssetNum({}) |{}|", assetNum, startTime);		

  		Map <String, String> parameters = new HashMap<>();
  		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
  		List<VariabileAsset> result = dataAccessService.search(VariabileAsset.class, SqlQueryConstants.QUERY_VARIABILI_ASSETDATA, parameters, true, true);
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Search getVariabileByAssetNum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
  	
  		return Assorted.notNull(result) ? result : new ArrayList<>();
  	}
    
    private List<AttributiAsset> getAttributiByAssetNum(String assetNum) 
   			throws DataLayerException
   	{
   		long startTime = System.currentTimeMillis();
   		log.debug("call getAttributiByAssetNum({}) |{}|", assetNum, startTime);		

   		Map <String, String> parameters = new HashMap<>();
   		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
   		List<AttributiAsset> result = dataAccessService.search(AttributiAsset.class, SqlQueryConstants.QUERY_ATTRIBUTI_ASSETDATA, parameters, true, true);
   		
   		long endTime = System.currentTimeMillis();
   		log.debug("  - Search getAttributiByAssetNum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
   	
   		return Assorted.notNull(result) ? result : new ArrayList<>();
   	}
    
    private List<HealthIndexAsset> getHealthIndexByAssetNum(String assetNum) 
   			throws DataLayerException
   	{
   		long startTime = System.currentTimeMillis();
   		log.debug("call getHealthIndexByAssetNum({}) |{}|", assetNum, startTime);		

   		Map <String, String> parameters = new HashMap<>();
   		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
   		List<HealthIndexAsset> result = dataAccessService.search(HealthIndexAsset.class, SqlQueryConstants.QUERY_HEALTH_INDEX_ASSETDATA, parameters, true, true);
   		
   		long endTime = System.currentTimeMillis();
   		log.debug("  - Search getHealthIndexByAssetNum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
   	
   		return Assorted.notNull(result) ? result : new ArrayList<>();
   	}
    
    
    private List<Asset> getAssetByAssetNum(String assetNum) 
   			throws DataLayerException
   	{
   		long startTime = System.currentTimeMillis();
   		log.debug("call getAssetByAssetNum({}) |{}|", assetNum, startTime);		

   		Map <String, String> parameters = new HashMap<>();
   		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
   		List<Asset> result = dataAccessService.search(Asset.class, SqlQueryConstants.QUERY_ASSET_ASSETDATA, parameters, true, true);
   		
   		long endTime = System.currentTimeMillis();
   		log.debug("  - Search getAssetByAssetNum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
   	
   		return Assorted.notNull(result) ? result : new ArrayList<>();
   	}
    
    
    private List<VariabileAsset> getMisureByAssetNum(String assetNum) 
  			throws DataLayerException
  	{
  		long startTime = System.currentTimeMillis();
  		log.debug("call getMisureByAssetNum({}) |{}|", assetNum, startTime);		

  		Map <String, String> parameters = new HashMap<>();
  		parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
  		List<VariabileAsset> result = dataAccessService.search(VariabileAsset.class, SqlQueryConstants.QUERY_MISURE_ASSETDATA, parameters, true, true);
  		
  		long endTime = System.currentTimeMillis();
  		log.debug("  - Search getMisureByAssetNum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
  	
  		return Assorted.notNull(result) ? result : new ArrayList<>();
  	}

    
    private List<HealthIndexAsset> getTassiGAByClassstructureId(String classstructureId) 
   			throws DataLayerException
   	{
   		long startTime = System.currentTimeMillis();
   		log.debug("call getTassiGAByClassstructureId({}) |{}|", classstructureId, startTime);		

   		Map <String, String> parameters = new HashMap<>();
   		parameters.put(ESQLMappingParameter.CLASSSTRUCTUREID.getName(), classstructureId);
   		List<HealthIndexAsset> result = dataAccessService.search(HealthIndexAsset.class, SqlQueryConstants.QUERY_TASSI_GA_ASSETDATA, parameters, true, true);
   		
   		long endTime = System.currentTimeMillis();
   		log.debug("  - Search getTassiGAByClassstructureId found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
   	
   		return Assorted.notNull(result) ? result : new ArrayList<>();
   	}

    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    @Cacheable(cacheManager = Constants.CACHE_MANAGER, 
    		   value = Constants.CACHE_ASSET_STAZIONE, 
    		   key = "#assetnum", 
    		   unless = "#result==null")
    public Boolean checkIfIsStazione(String assetnum) 
            throws DataLayerException 
    {
        long startTime = System.currentTimeMillis();
        log.debug("call checkIfIsStazione({}) |{}|", assetnum, startTime);
        
        Map <String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetnum);

        List<ClassAncestor> ancestors = dataAccessService.search(ClassAncestor.class, SqlQueryConstants.QUERY_FIND_ANCESTORCLASSID, parameters, true, true);
        String assetType              = null;
        
        if (Assorted.isNotEmpty(ancestors)) 
            assetType = ancestors.stream().map(ClassAncestor::getAncestorClassId).findFirst().orElse(null);            

        long endTime = System.currentTimeMillis();
        log.info("end checkIfIsStazione({}) ==> {} |{}|", assetnum, assetType, endTime - startTime);
        return Assorted.isNotEmpty(assetType) ? Constants.CLASS_STAZIONE.equals(assetType) : Boolean.FALSE;
    }
    
    /**
     * Comparator per avere le ultime misure per data rilevazione, assumo che il record con dataUltimaRilevazione a null sia
     * la piu recente.
     */   
    Comparator<VariabileAsset> misuraComparator = (m1, m2) -> {
    	boolean m1HasValue = m1.getValore() != null;
    	boolean m2HasValue = m2.getValore() != null;
    	
    	if(m1HasValue && !m2HasValue) return -1;
    	if(!m1HasValue && m2HasValue) return 1;
    	
    	Date d1 = m1.getUltimaRilevazione();
    	Date d2 = m2.getUltimaRilevazione();
    	
    	if(d1 == null && d2 == null) return 0;
    	if(d1 == null) return 1;
    	if(d2 == null) return -1;
    	
    	return d2.compareTo(d1);
    };
}