package it.terna.nas.service.dal.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.DssDictionary;

@Repository
public interface DssDictionaryRepository extends JpaRepository<DssDictionary, String>{
	
	
}
