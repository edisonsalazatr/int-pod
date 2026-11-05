package it.terna.nas.service.dal.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.documents.PolicyEvaluatorContextDocument;



@Repository
public interface PolicyEvaluatorContextRepository extends MongoRepository<PolicyEvaluatorContextDocument, String> {
	

	
}
