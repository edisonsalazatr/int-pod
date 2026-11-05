package it.terna.nas.service.dal.service.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.stream.Stream;

import javax.persistence.QueryHint;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4EsitoInt;
import it.terna.nas.service.material.utility.Constants;

@Repository
public interface X4EsitoIntRepository extends JpaRepository<X4EsitoInt, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM X4EsitoInt x WHERE x.endElab < :thresholdDate")
    int deleteOlderThan(@Param("thresholdDate") Date thresholdDate);
    
    @Query(value="SELECT X4ESITOINTID FROM X4ESITOINT P WHERE CODESITO != 'EAM_0012' AND UNIQUEID IS NOT NULL AND KAFKATOPIC IS NOT NULL AND ESITO = 'KO' AND PARENT IS NULL AND SOURCE = 'EAM' AND RETRY < :resubmit AND NOT EXISTS (SELECT 1 FROM X4ESITOINT C WHERE C.SOURCE = P.SOURCE AND C.PARENT = P.X4ESITOINTID AND C.UNIQUEID = P.UNIQUEID AND ESITO = 'OK' AND ROWNUM = 1)", nativeQuery = true)
	@QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
	public Stream<BigDecimal> getListServiceToRestart(@Param("resubmit") Integer resubmit);

    @Query(value="SELECT X4ESITOINTID FROM X4ESITOINT P WHERE CODESITO = 'EAM_0012' AND UNIQUEID IS NOT NULL AND KAFKATOPIC IS NOT NULL AND ESITO = 'KO' AND PARENT IS NULL AND SOURCE = 'EAM' AND RETRY < :resubmit AND NOT EXISTS (SELECT 1 FROM X4ESITOINT C WHERE C.SOURCE = P.SOURCE AND C.PARENT = P.X4ESITOINTID AND C.UNIQUEID = P.UNIQUEID AND ESITO = 'OK' AND ROWNUM = 1)", nativeQuery = true)
	@QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
	public Stream<BigDecimal> getListSapIdServiceToRestart(@Param("resubmit") Integer resubmit);
}
