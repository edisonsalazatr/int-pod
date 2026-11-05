package it.terna.nas.service.dal.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.terna.kernel.basement.exception.AbstractException;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Monitor.EErrorMapping;
import it.terna.nas.service.dal.documents.PolicyEvaluatorContextDocument;
import it.terna.nas.service.dal.repository.PolicyEvaluatorContextRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PolicyEvaluatorContextService {

	@Autowired
	private  PolicyEvaluatorContextRepository repository;
		
	public PolicyEvaluatorContextDocument findById(String id) throws AbstractException {
		
		log.info(" - Mongo - Repository Service - Finding PolicyEvaluatorContextDocument by id - ");
		// return retrieved documents from underlying storage
		return repository.findById(id)
						 .orElseThrow(() -> new DataLayerException(EErrorMapping.GENERIC_QUERY_ERROR,  
							new Object[] { "PolicyEvaluatorContextDocument not found searching by id", id }));
	}
	
	public PolicyEvaluatorContextDocument save(PolicyEvaluatorContextDocument document)  throws DataLayerException {
		
		log.info(" - Mongo - Repository Service - Saving document - ");
		try
		{
			// return saved documents from underlying storage
			return repository.save(document);
		}
		catch ( Exception ex )
		{
			//log occurred error
			log.error("  - Mongo - Repository Service - Saving PolicyEvaluatorContextDocument - Following error has occurred\n", ex);

			//throw custom exception 
			throw new DataLayerException(EErrorMapping.GENERIC_QUERY_ERROR, 
				                         ex,
				                         new Object[] { "Mongo collection is policy_evaluator_context" });
		}
	}
	
	public List<PolicyEvaluatorContextDocument> saveAll(List<PolicyEvaluatorContextDocument> documents)  throws DataLayerException {
		
		log.info(" - Mongo - Repository Service - Saving all documents - ");
		try
		{
			// return saved documents from underlying storage
			return repository.saveAll(documents);
		}
		catch ( Exception ex )
		{
			//log occurred error
			log.error("  - Mongo - Repository Service - Saving PolicyEvaluatorContextDocument - Following error has occurred\n", ex);

			//throw custom exception 
			throw new DataLayerException(EErrorMapping.GENERIC_QUERY_ERROR, 
				                         ex,
				                         new Object[] { "Mongo collection is policy_evaluator_context" });
		}
	}
		
}
