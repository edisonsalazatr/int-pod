package it.terna.nas.service.dal.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import it.terna.nas.service.dal.entity.eam.X4HiCalcStaging;

@Repository
public interface X4HiCalcStagingRepository extends JpaRepository<X4HiCalcStaging, Long>
{
    @Query(value = "select count(c) from X4HiCalcStaging c where c.siteId = :siteId and c.assetnum = :assetnum and c.hiCalcId is null")
    public Integer counterHiCalcByAsset(@Param("siteId") String siteId, @Param("assetnum") String assetNum);
    
    @Query(value = "SELECT count(X4HICALCSTAGINGID) FROM X4HICALCSTAGING WHERE ASSETNUM = :assetnum AND SITEID = :siteId AND NVL(LASTUPDATE, CREATEDATE) >= (SYSDATE - :minutes/1440) AND ROWNUM = 1", nativeQuery = true)
    public Integer counterHiElabByAsset(@Param("siteId") String siteId, @Param("assetnum") String assetNum, @Param("minutes") Integer minutes);
}
