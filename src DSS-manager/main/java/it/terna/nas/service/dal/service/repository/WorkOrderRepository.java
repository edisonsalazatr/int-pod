package it.terna.nas.service.dal.service.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import it.terna.nas.service.dal.entity.eam.WorkOrder;

public interface WorkOrderRepository extends JpaRepository<WorkOrder, String>
{
    @Query("SELECT od FROM WorkOrder od WHERE od.wonum IN (:wonums)")
    public List<WorkOrder> findWorkOderByWonum(List<String> wonums);
    
    @Query(value = "SELECT wonum FROM WorkOrder od WHERE od.jpnum = :jpnum and od.assetNum = :linea and od.status in ( 'APERTO', 'AVVIATO', 'DA VALIDARE', 'ESEGUIBILE', 'ESEGUITO PARZ.', 'INVIATO', 'INOLTRATO', 'SCHEDULABILE', 'SCHEDULATO' ) and od.x4tronconum = :troncoNum and od.x4policyid = :policyId", nativeQuery = true)
    public List<String> findWorkOderByLinea(@Param(value = "jpnum") String attivita,  @Param(value = "linea") String linea, @Param(value = "troncoNum") String troncoNum, @Param(value = "policyId") Long policyId);

    @Query(value = "SELECT * FROM WORKORDER P WHERE P.JPNUM IN (SELECT JPNUM FROM JOBPLAN J WHERE X4OUTSOURCING = 1 AND J.JPNUM = P.JPNUM) AND P.ASSETNUM IN (SELECT K.ASSETNUM FROM ASSETSPEC K WHERE ASSETATTRID = 'LIN_OUTSOURCING' AND ALNVALUE = 'SI' AND K.ASSETNUM = P.ASSETNUM) AND P.PARENT = :wonum AND P.ISTASK = 0", nativeQuery = true)
    public List<WorkOrder> findCaviChildCandidate(@Param(value = "wonum") String wonum);
    
    @Query(value = "SELECT * FROM WORKORDER P WHERE P.JPNUM IN (SELECT JPNUM FROM JOBPLAN J WHERE X4OUTSOURCING = 1 AND J.JPNUM = P.JPNUM) AND P.X4SCENARIOTRONCHIMIG in (4,5) AND P.WONUM = :wonum AND P.ISTASK = 0", nativeQuery = true)
    public List<WorkOrder> findCaviCandidate45(@Param(value = "wonum") String wonum);
    
    @Query(value = "SELECT * FROM WORKORDER P WHERE P.WONUM = :wonum AND P.ISTASK = 0 AND WOLO1 IS NOT NULL", nativeQuery = true)
    public List<WorkOrder> findAnnullaGeocall(@Param(value = "wonum") String wonum);    

    @Query(value = "SELECT * FROM WORKORDER P WHERE P.WONUM = :wonum AND P.CHANGEDATE >= (CAST(:changedate AS TIMESTAMP) - NUMTODSINTERVAL(5, 'SECOND'))", nativeQuery = true)
    public List<WorkOrder> findByWonumAndChangedate(@Param(value = "wonum") String wonum, @Param(value = "changedate") Date changedate);
}
