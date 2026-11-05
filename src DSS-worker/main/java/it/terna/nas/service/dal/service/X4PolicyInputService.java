package it.terna.nas.service.dal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.nas.service.dal.entity.eam.X4PolicyInput;
import it.terna.nas.service.dal.repository.X4PolicyInputRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class X4PolicyInputService {

	@Autowired 
	private X4PolicyInputRepository repository;
	
	/**
	 * @param policyId
	 * @return the inputs record for the policy
	 */
    @Datasource(datasourceKey = EDatasourceKey.EAM)
	public List<X4PolicyInput> getByPolicyId(Long policyId) {
		Assert.notNull(policyId, "policyId is null");
		log.info(" - Retrieving inputs for policy with ID={}", policyId);
		return repository.findByIdPolicy(policyId);
	}
}
