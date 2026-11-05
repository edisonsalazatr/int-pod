package it.terna.nas.service.dal.service.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import it.terna.nas.service.dal.entity.eam.X4PolicyClassifications;

@Repository
public interface X4PolicyClassificationsRepository extends JpaRepository<X4PolicyClassifications, Long>
{
	@Query("select c from X4PolicyClassifications c where c.x4policyId = :x4policyId")
	public List<X4PolicyClassifications> findClassificationByPolicy(@Param("x4policyId") Long x4policyId);
}
