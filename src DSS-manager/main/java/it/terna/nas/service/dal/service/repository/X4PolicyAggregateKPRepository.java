package it.terna.nas.service.dal.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4PolicyAggregateKP;

@Repository
public interface X4PolicyAggregateKPRepository extends JpaRepository<X4PolicyAggregateKP, Long>{
	
}
