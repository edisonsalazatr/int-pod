package it.terna.nas.service.dal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4Policy;

@Repository
public interface X4PolicyRepository extends JpaRepository<X4Policy, Long>{
	
	Optional<X4Policy> findByX4PolicyId(Long x4PolicyId);
	
	@Query(value = "SELECT XP.X4POLICYID " +
            "FROM X4POLICYINPUT XP, X4POLICY XP2 " +
            "WHERE IDINPUT = :key " +
            "AND TIPOLOGIAINPUT = :typology " +
            "AND XP2.X4POLICYID = XP.X4POLICYID " +
            "AND XP2.STATUS = 'ATTIVA'", 
    nativeQuery = true)
	List<Long> findPolicyIdsByKey(
			@Param(value = "key") String key, 
			@Param(value = "typology") String typology);
	
}
