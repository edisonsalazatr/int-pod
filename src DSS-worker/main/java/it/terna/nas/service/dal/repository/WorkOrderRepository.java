package it.terna.nas.service.dal.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import it.terna.nas.service.dal.entity.eam.WorkOrder;
import it.terna.nas.service.material.utility.SqlQueryConstants;

@Repository
public interface WorkOrderRepository extends JpaRepository<WorkOrder, String>
{
    @Query(value = SqlQueryConstants.QUERY_WONUM_BY_ASSET_01, nativeQuery = true)
    public List<String> findWonumByStatus(@Param(value = "ASSETNUM") String assetNum);
    
    @Query(value = "SELECT * FROM WORKORDER WHERE STATUS IN (:listStatus) AND TRUNC(X4DATAMODELLO) = TRUNC(:x4DataModello) AND JPNUM IN (:jpnums) AND X4POLICYID= :x4policyid AND ASSETNUM IN (:listAssets) AND (X4TRONCONUM = :tronconum OR X4TRONCONUM IS NULL)", nativeQuery = true)    
    public List<WorkOrder> findWorkOrderForTronco(@Param(value = "listStatus")       List<String> listStatus,
                                                  @Param(value = "listAssets")       List<String> listAssets, 
                                                  @Param(value = "jpnums")           List<String> jpnums, 
                                                  @Param(value = "x4policyid")       Long x4policyid, 
                                                  @Param(value = "x4DataModello")    Date x4DataModello, 
                                                  @Param(value = "tronconum")        String tronconum);
    
    String RESET_MICROCONTROLLI_DELETE_WOSTATUS = """
    		DELETE FROM WOSTATUS wo
    			WHERE wo.wonum IN (
    			    SELECT wo2.wonum
    				FROM workorder wo2
    				    JOIN x4_microcontrolli_mig_batch m
    				        ON wo2.parent = m.wonum
    				    WHERE m.flag_tobe_deleted = 1
    		)
    		""";
    
    @Modifying
    @Query(value = RESET_MICROCONTROLLI_DELETE_WOSTATUS, nativeQuery = true)
    public int resetMicrocontrolliWoStatus();
    
    String RESET_MICROCONTROLLI_DELETE_WOANCESTOR = """
    		DELETE FROM WOANCESTOR wo
		    WHERE wo.wonum IN (
		        SELECT wo2.wonum
		        FROM workorder wo2
		        JOIN x4_microcontrolli_mig_batch m
		            ON wo2.parent = m.wonum
		        WHERE m.flag_tobe_deleted = 1
		    )
    		""";
    @Modifying
    @Query(value = RESET_MICROCONTROLLI_DELETE_WOANCESTOR, nativeQuery = true)
    public int resetMicrocontrolliWoAncestor();
    
    String RESET_MICROCONTROLLI_DELETE_WOOWNERHISTORY = """
    		DELETE FROM WOOWNERHISTORY wo
		    WHERE wo.wonum IN (
		        SELECT wo2.wonum
		        FROM workorder wo2
		        JOIN x4_microcontrolli_mig_batch m
		            ON wo2.parent = m.wonum
		        WHERE m.flag_tobe_deleted = 1
		    )
    		""";
    
    @Modifying
    @Query(value = RESET_MICROCONTROLLI_DELETE_WOOWNERHISTORY, nativeQuery = true)
    public int resetMicrocontrolliWoOwnerHistory();
    
    String RESET_MICROCONTROLLI_DELETE_A_WORKORDER = """
    		DELETE FROM A_WORKORDER wo
		    WHERE wo.wonum IN (
		        SELECT wo2.wonum
		        FROM workorder wo2
		        JOIN x4_microcontrolli_mig_batch m
		            ON wo2.parent = m.wonum
		        WHERE m.flag_tobe_deleted = 1
		    )
    		""";
    @Modifying
    @Query(value = RESET_MICROCONTROLLI_DELETE_A_WORKORDER, nativeQuery = true)
    public int resetMicrocontrolliAWorkOrder();
    
    String RESET_MICROCONTROLLI_DELETE_WORKORDER = """
    		DELETE FROM WORKORDER wo
		    WHERE wo.wonum IN (
		        SELECT wo2.wonum
		        FROM workorder wo2
		        JOIN x4_microcontrolli_mig_batch m
		            ON wo2.parent = m.wonum
		        WHERE m.flag_tobe_deleted = 1
		    )
    		""";
    @Modifying
    @Query(value = RESET_MICROCONTROLLI_DELETE_WORKORDER, nativeQuery = true)
    public int resetMicrocontrolliWorkOrder();
    
    public static String EXISTS_WORKORDER_BY_POLICY_ASSET_JP = 
                	    "SELECT T.WONUM FROM WORKORDER T " +
                	    "WHERE T.X4POLICYID = :x4PolicyId " +
                	    "AND T.JPNUM = :jpNum " +
                	    "AND T.ASSETNUM = :assetNum " +
                	    "AND T.STATUS IN ('APERTO', 'AVVIATO', 'DA VALIDARE', 'ESEGUIBILE', 'ESEGUITO PARZ.', 'INOLTRATO', 'INVIATO', 'SCHEDULABILE', 'SCHEDULATO') " +
                	    "AND ROWNUM = 1";
    @Query(value = EXISTS_WORKORDER_BY_POLICY_ASSET_JP, nativeQuery = true)
    Optional<String> findWorkOrderNumByPolicyAssetAndJpnum(
            @Param("x4PolicyId") Long x4PolicyId,
            @Param("jpNum") String jpNum,
            @Param("assetNum") String assetNum
    );

    @Query(value = "SELECT * FROM WORKORDER WHERE X4POLICYID = :x4policyid and JPNUM = :jpnum AND STATUS IN (:listStatus) AND ASSETNUM = :assetnum AND X4TRONCONUM = :tronconum AND X4TECMIG = 1", nativeQuery = true)    
    public List<WorkOrder> findOdlMigratiByLineaAndStatusBeyondSchedulato(@Param(value = "listStatus")       List<String> listStatus,
    											  @Param(value = "assetnum")           String linea,
    											  @Param(value = "jpnum")           String jpnum, 
                                                  @Param(value = "x4policyid")       Long x4policyid, 
                                                  @Param(value = "tronconum")        String tronconum);

}
