package it.terna.nas.service.dal.service.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4DssAggregate;
import it.terna.nas.service.material.utility.SqlQueryConstants;

@Repository
public interface X4DssAggregateRepository extends JpaRepository<X4DssAggregate, Long>
{
    @Query(value = "SELECT  c.* FROM X4_DSS_AGGREGATE c WHERE c.DSS_PC_CORRELATION_ID = :dssPcCorrelationId AND c.LINEA = :linea AND c.KELIGIBILITA >= :keligibilita AND ROWNUM = 1", nativeQuery = true)
    public List<X4DssAggregate> findTrattaOne(@Param(value = "dssPcCorrelationId") String correlationId, 
                                              @Param(value = "linea") String linea,
                                              @Param(value = "keligibilita") Double keligibilita);


    @Query(value = "SELECT DISTINCT LINEA FROM X4_DSS_AGGREGATE xda WHERE xda.DSS_PC_CORRELATION_ID = :dssPcCorrelationId", nativeQuery = true)
    public List<String> findLineaAggregate(@Param(value = "dssPcCorrelationId") String correlationId);
    
    
    @Query(value = SqlQueryConstants.QUERY_AGG_COND_FUNE, nativeQuery = true)
    public List<String> findLineaCandidateCondFune( @Param(value = "CORRELATIONKEY") String correlationId,
                                                    @Param(value = "LINEA") String linea,
                                                    @Param(value = "SOGLIA_KELEG") double sogliaKEleg, 
                                                    @Param(value = "SOGLIA_ELIGIBILITA") double sogliaElegibilita);


    @Query(value = SqlQueryConstants.QUERY_AGG_TELAIO, nativeQuery = true)
    public List<String> findLineaCandidateTelaio( @Param(value = "CORRELATIONKEY") String correlationId,
                                                  @Param(value = "LINEA") String linea,
                                                  @Param(value = "SOGLIA_KELEG") double sogliaKEleg, 
                                                  @Param(value = "SOGLIA_ELIGIBILITA") double sogliaElegibilita);
}



