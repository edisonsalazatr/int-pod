package it.terna.nas.service.dal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.terna.nas.service.dal.entity.eam.X4PolicyInput;

public interface X4PolicyInputRepository extends JpaRepository<X4PolicyInput, Long>{
	
	List<X4PolicyInput> findByIdPolicy(Long idPolicy);

}
