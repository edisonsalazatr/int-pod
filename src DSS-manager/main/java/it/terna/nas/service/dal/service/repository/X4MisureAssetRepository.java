package it.terna.nas.service.dal.service.repository;

import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import it.terna.nas.service.dal.entity.eam.X4MisureAsset;

public interface X4MisureAssetRepository extends JpaRepository<X4MisureAsset, Long>
{
    @Query(value = "SELECT COUNT(*) "
            + "FROM X4MISUREASSET m "
            + "WHERE m.X4MISUREASSETID = :x4MisureAssetId "
            + "AND m.ULTIMARILEVAZIONE >= (:ultimaRilevazione - NUMTODSINTERVAL(5, 'SECOND'))", nativeQuery = true)
    public Integer countByIdAndUltimaRilevazioneGreaterThanEqual(@Param("x4MisureAssetId") Long x4MisureAssetId,
                                                                 @Param("ultimaRilevazione") Date ultimaRilevazione);
}
