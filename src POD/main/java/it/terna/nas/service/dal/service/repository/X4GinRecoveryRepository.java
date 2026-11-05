package it.terna.nas.service.dal.service.repository;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.QueryHint;
import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4OdlIndispRecovery;
import it.terna.nas.service.material.utility.Constants;

@Repository
public interface X4GinRecoveryRepository extends JpaRepository<X4OdlIndispRecovery, Long> {

	@Query(value = """
		    SELECT rc.WONUM AS WONUM
		    FROM X4_ODL_INDISP_RECOVERY rc
		    WHERE rc.PROCESSED = 0
		    ORDER BY rc.ID
		    """, nativeQuery = true)
	@QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
	public Stream<String> getListWonumToRecover();

	String ODL_INDISP_REQUEST = """
		    SELECT WONUM,
		           STATUS,
		           DESCRIPTION,
		           ASSETNUM,
		           SITEID,
		           JPNUM,
		           CASE
		               WHEN TARGSTARTDATE IS NOT NULL
		               THEN TO_CHAR(TARGSTARTDATE, 'YYYY-MM-DD"T"HH24:MI:SS') || '+02:00'
		           END AS TARGSTARTDATE,
		           CASE
		               WHEN TARGCOMPDATE IS NOT NULL
		               THEN TO_CHAR(TARGCOMPDATE, 'YYYY-MM-DD"T"HH24:MI:SS') || '+02:00'
		           END AS TARGCOMPDATE,
		           CASE
		               WHEN SCHEDSTART IS NOT NULL
		               THEN TO_CHAR(SCHEDSTART, 'YYYY-MM-DD"T"HH24:MI:SS') || '+02:00'
		           END AS SCHEDSTART,
		           CASE
		               WHEN SCHEDFINISH IS NOT NULL
		               THEN TO_CHAR(SCHEDFINISH, 'YYYY-MM-DD"T"HH24:MI:SS') || '+02:00'
		           END AS SCHEDFINISH,
		           X4WBS,
		           X4TIPOSPESA,
		           PLUSCJPREVNUM
		    FROM WORKORDER
		    WHERE WONUM = :wonum
		    """;

    @Query(value = ODL_INDISP_REQUEST, nativeQuery = true)
    Tuple loadWorkOrder(@Param("wonum") String wonum);

    @Modifying
    @Query(value = """
        UPDATE X4_ODL_INDISP_RECOVERY
        SET PROCESSED = 1,
            ELABDATE = SYSDATE
        WHERE PROCESSED = 0
          AND WONUM IN (:wonums)
        """, nativeQuery = true)
    int markAsProcessedBatch(@Param("wonums") List<String> wonums);
}