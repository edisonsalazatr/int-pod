package it.terna.nas.service.dal.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.X4Policy;
import it.terna.nas.service.dal.entity.eam.X4PolicyClassifications;
import it.terna.nas.service.dal.entity.eam.X4PolicyInput;
import it.terna.nas.service.dal.repository.X4PolicyClassificationsRepository;
import it.terna.nas.service.dal.repository.X4PolicyRepository;
import it.terna.nas.service.exceptions.EAMException;
import it.terna.nas.service.material.dto.policy.Policy;
import it.terna.nas.service.material.transformer.PolicyTransformer;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4PolicyService 
{

	@Autowired 
	private X4PolicyRepository policyRepository;
	
	@Autowired 
	private X4PolicyInputService policyInputService;
	
	@Autowired
	private X4PolicyClassificationsRepository classificationRepository;
	
	
	/**
	 * @param policyId
	 * @return the policy associated to policyId
	 */
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public X4Policy getById(Long policyId) 
    {
    	   log.info(" - Searching X4Policy Table for policy with ID={}", policyId);
    	    Assert.notNull(policyId, "policyId is null");
    	    return policyRepository.findByX4PolicyId(policyId).orElse(null);
	}
    
	/**
	 * @param key
	 * @return identify related policies by CatalogTipology key
	 */
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<Long> getIdsByKey(String key, String typology) 
    {
        log.info(" - Searching X4Policy Table for related policies by key ={}", key);
        Assert.notNull(key, "key is null");

        List<Long> policyIds = policyRepository.findPolicyIdsByKey(key, typology);
        return policyIds;
    }
    
    
    @Cacheable(cacheManager = Constants.CACHE_MANAGER, 
    		   value = Constants.CACHE_POLICY, 
    		   key = "'" + Constants.CACHE_POLICY_KEY_PREFIX + "'+#policyId", 
    		   unless = "#result==null")
    public Policy loadPolicy(Long policyId) 
	{
		log.info("  policy not found in cache, attempt to load it from db");
		
		X4Policy x4policy = getById(policyId);
		Assert.notNull(x4policy, String.format("policy non found by id=%d", policyId));
		
		List<X4PolicyInput> listInput = policyInputService.getByPolicyId(policyId);
		
		// trasform data
		return PolicyTransformer.toDto(x4policy, listInput);
	}
    
    public List<String> getClassStructureIds(Policy policy) 
			throws EAMException
	{
		log.debug("call getClassStructureIds({})", policy.getId());
		
		List<String> output 	= new ArrayList<>();
		Set<String> setType		= new HashSet<>();
		String tipologiaAsset 	= policy.getAssetType();
		if (Assorted.isEmpty(tipologiaAsset))
			throw new EAMException(EAMErrorMapping.EAM_0003.getEamCode(), 
								   EAMErrorMapping.EAM_0003.getErrorDescription());
		
		setType.add(tipologiaAsset);
		List<X4PolicyClassifications> classifications = classificationRepository.findClassificationByPolicy(policy.getId());
		if (Assorted.isNotEmpty(classifications))
		{
			classifications.stream().forEach(x -> setType.add(x.getClassStructureId()));
		}
		
		output.addAll(setType);
		log.debug("getClassStructureIds({}) ==> {}", policy.getId(), output);
		return output;
	}
}
