package it.terna.nas.service.dal.service.repository;

import java.math.BigDecimal;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4Snow;
import it.terna.nas.service.material.utility.Constants;

@Repository
public interface X4EsitoIntSnowRepository extends JpaRepository<X4Snow, String>
{
	@Query(value="SELECT X4ESITOINTSNOWID FROM X4ESITOINTSNOW P WHERE COD_ESITO != 'EAM_0012' AND UNIQUEID IS NOT NULL AND KAFKA_TOPIC IS NOT NULL AND ESITO = 'KO' AND PARENT IS NULL AND RETRY < :resubmit AND NOT EXISTS (SELECT 1 FROM X4ESITOINTSNOW C WHERE C.PARENT = P.X4ESITOINTSNOWID AND C.UNIQUEID = P.UNIQUEID AND ESITO = 'OK' AND ROWNUM = 1)", nativeQuery = true)
	@QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
	public Stream<BigDecimal> getListServiceToRestart(@Param("resubmit") Integer resubmit);
	
	@Query(value="SELECT X4ESITOINTSNOWID FROM X4ESITOINTSNOW P WHERE COD_ESITO = 'EAM_0012' AND UNIQUEID IS NOT NULL AND KAFKA_TOPIC IS NOT NULL AND ESITO = 'KO' AND PARENT IS NULL AND RETRY < :resubmit AND NOT EXISTS (SELECT 1 FROM X4ESITOINTSNOW C WHERE C.PARENT = P.X4ESITOINTSNOWID AND C.UNIQUEID = P.UNIQUEID AND ESITO = 'OK' AND ROWNUM = 1)", nativeQuery = true)
	@QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
	public Stream<BigDecimal> getListSapIdServiceToRestart(@Param("resubmit") Integer resubmit);
}
