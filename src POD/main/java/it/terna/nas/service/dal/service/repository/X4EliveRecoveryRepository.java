package it.terna.nas.service.dal.service.repository;

import java.util.stream.Stream;

import javax.persistence.QueryHint;
import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4OdlEliveRecovery;
import it.terna.nas.service.material.utility.Constants;

@Repository
public interface X4EliveRecoveryRepository extends JpaRepository<X4OdlEliveRecovery, Long> {

    @Query(value = """
        SELECT rc.WONUM
        FROM MAXIMO_RO.X4_ODL_ELIVE_RECOVERY rc
        WHERE rc.PROCESSED = 0
          AND rc.BATCH_CODE = :batchCode
        ORDER BY rc.ID
        """, nativeQuery = true)
    @QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
    Stream<String> getListWonumToRecover(@Param("batchCode") String batchCode);
    
    String ODL_ELIVE_REQUEST = """
        SELECT WONUM,
               STATUS,
               JPNUM,
               ASSETNUM,
               SITEID,
               TARGSTARTDATE
        FROM WORKORDER
        WHERE WONUM = :wonum
        """;

    @Query(value = ODL_ELIVE_REQUEST, nativeQuery = true)
    Tuple loadWorkOrderForElive(@Param("wonum") String wonum);

    @Modifying
    @Query(value = """
        UPDATE MAXIMO_RO.X4_ODL_ELIVE_RECOVERY
        SET PROCESSED = 1,
            ELABDATE = SYSDATE,
            ERROR_MESSAGE = NULL
        WHERE PROCESSED = 0
          AND BATCH_CODE = :batchCode
          AND WONUM = :wonum
        """, nativeQuery = true)
    int markAsProcessed(@Param("batchCode") String batchCode,
                        @Param("wonum") String wonum);

    @Modifying
    @Query(value = """
        UPDATE MAXIMO_RO.X4_ODL_ELIVE_RECOVERY
        SET ELABDATE = SYSDATE,
            ERROR_MESSAGE = SUBSTR(:errorMessage, 1, 4000)
        WHERE PROCESSED = 0
          AND BATCH_CODE = :batchCode
          AND WONUM = :wonum
        """, nativeQuery = true)
    int markAsKo(@Param("batchCode") String batchCode,
                 @Param("wonum") String wonum,
                 @Param("errorMessage") String errorMessage);
}
