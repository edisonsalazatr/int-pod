package it.terna.nas.service.dal.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import it.terna.nas.service.dal.entity.eam.AhMethodology;

@Repository
public interface AhMethodologyRepository extends JpaRepository<AhMethodology, Long>
{
    @Query(value = "select c from AhMethodology c where c.active = 1 and c.description = :description")
    public List<AhMethodology> findHiDefByDescription(@Param(value = "description") String description);
}
