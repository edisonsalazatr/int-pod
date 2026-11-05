package it.terna.nas.service.dal.service.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.lettuce.core.dynamic.annotation.Param;
import it.terna.nas.service.dal.entity.eam.X4MicrocontrolloMigBatch;

@Repository
public interface X4MicrocontrolloMigBatchRepository extends JpaRepository<X4MicrocontrolloMigBatch, Long>
{
	
	@Query("SELECT e FROM X4MicrocontrolloMigBatch e WHERE e.status='READY' and e.id > :lastId ORDER BY e.id ASC")
    public List<X4MicrocontrolloMigBatch> findByNextBatch(@Param("lastId") Long lastId, Pageable pageable);
	
	
	@Query(value = """
			SELECT * 
				FROM X4_MICROCONTROLLI_MIG_BATCH bat 
			WHERE bat.status='READY' 
			AND
			 bat.X4_MICROCONTROLLI_MIG_BATCHID > :lastId 
			ORDER BY bat.X4_MICROCONTROLLI_MIG_BATCHID ASC
			FETCH FIRST :limit ROWS ONLY
			""", nativeQuery = true)
    public List<X4MicrocontrolloMigBatch> findByNextBatchNative(@Param("lastId") Long lastId, @Param("limit") int limit);
	
	@Modifying
	@Transactional
	@Query("UPDATE X4MicrocontrolloMigBatch batch SET batch.status = :status WHERE batch.id = :id")
	public int updateStatusById(@Param("id") Long id, @Param("status") String status);
}
