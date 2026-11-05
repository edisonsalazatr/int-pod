package it.terna.nas.service.dal.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import it.terna.nas.service.dal.entity.eam.ClassStructure;
import it.terna.nas.service.material.utility.SqlQueryConstants;

@Repository
public interface ClassStructureRepository extends JpaRepository<ClassStructure, String> 
{
	public ClassStructure findByClassification(String classification);

	@Query(value = SqlQueryConstants.QUERY_CLASSIFICATION_01, nativeQuery = false)
	public List<ClassStructure> findByClassificationList(@Param("classification") String classification);
}
