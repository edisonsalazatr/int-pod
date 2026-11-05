package it.terna.nas.service.dal.service.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4DssWorkedHistory;
import it.terna.nas.service.material.utility.Constants;

@Repository
public interface X4DssWorkedHistoryRepository extends JpaRepository<X4DssWorkedHistory, Long>
{
	@Query(value = "SELECT A.ASSETNUM "
	        + "FROM X4_DSS_WORKED_HISTORY A, ASSET B "
	        + "WHERE A.X4_DSS_POLICY_RUNNERID = :runnerId "
	        + "    AND A.ERROR_FLAG = 'KO' "
	        + "    AND A.RECOVERY_COUNT = :recoveryCount"
	        + "    AND A.ASSETNUM = B.ASSETNUM"
	        + "    AND B.CLASSSTRUCTUREID IN (SELECT c.CLASSSTRUCTUREID "
	        + "                               FROM X4POLICYCLASSIFICATIONS xp, CLASSSTRUCTURE c  "
	        + "                               WHERE xp.CLASSIFICATIONID = c.CLASSIFICATIONID "
	        + "                                    AND xp.X4POLICYID = :x4PolicyId)", nativeQuery = true)
	@QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
	public List<String> findAllRecordToRecovery(@Param("runnerId") Long runnerId, @Param("recoveryCount") Integer recoveryCount, @Param("x4PolicyId") Long x4PolicyId);


	
	@Query(value = "SELECT count(A.X4_DSS_WORKED_HISTORYID) "
                + "FROM X4_DSS_WORKED_HISTORY A, ASSET B "
                + "WHERE A.X4_DSS_POLICY_RUNNERID = :runnerId "
                + "    AND A.ERROR_FLAG = 'KO' "
                + "    AND A.RECOVERY_COUNT = :recoveryCount"
                + "    AND A.ASSETNUM = B.ASSETNUM"
                + "    AND B.CLASSSTRUCTUREID IN (SELECT c.CLASSSTRUCTUREID "
                + "                               FROM X4POLICYCLASSIFICATIONS xp, CLASSSTRUCTURE c  "
                + "                               WHERE xp.CLASSIFICATIONID = c.CLASSIFICATIONID "
                + "                                 AND xp.X4POLICYID = :x4PolicyId)", nativeQuery = true)
    @QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
    public Integer countAllRecordToRecovery(@Param("runnerId") Long runnerId, @Param("recoveryCount") Integer recoveryCount, @Param("x4PolicyId") Long x4PolicyId);

	
    @Query(value = "SELECT A.ASSETNUM "
                 + "FROM X4_DSS_WORKED_HISTORY A"
                 + "WHERE A.X4_DSS_POLICY_RUNNERID = :runnerId "
                 + "    AND A.ERROR_FLAG = 'KO' "
                 + "    AND A.RECOVERY_COUNT = :recoveryCount", nativeQuery = true)
    @QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
    public List<String> findAllRecordToRecoveryStation(@Param("runnerId") Long runnerId, @Param("recoveryCount") Integer recoveryCount);
	

    @Query(value = "SELECT count(A.X4_DSS_WORKED_HISTORYID) "
                 + "FROM X4_DSS_WORKED_HISTORY A "
                 + "WHERE A.X4_DSS_POLICY_RUNNERID = :runnerId "
                 + "    AND A.ERROR_FLAG = 'KO' "
                 + "    AND A.RECOVERY_COUNT = :recoveryCount", nativeQuery = true)
    @QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
    public Integer countAllRecordToRecoveryStation(@Param("runnerId") Long runnerId, @Param("recoveryCount") Integer recoveryCount);
    
    
	@Query(value="SELECT nvl(max(xdwh.RECOVERY_COUNT), 0) FROM X4_DSS_WORKED_HISTORY xdwh WHERE xdwh.X4_DSS_POLICY_RUNNERID = :runnerId", nativeQuery = true)
	public Integer findMaxRecovery(@Param("runnerId") Long runnerId);

	
	@Query(value="SELECT nvl(max(xdwh.RECOVERY_COUNT), 1) FROM X4_DSS_WORKED_HISTORY xdwh WHERE xdwh.POLICY_ID = :policyId and xdwh.DSS_PC_CORRELATION_ID = :correlationId", nativeQuery = true)
	public Integer findRecoveryCountByCorrelationId(@Param("correlationId") String correlationId, @Param("policyId") String policyId);
	
}
