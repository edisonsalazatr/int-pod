package it.terna.nas.service.dal.service.repository;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4DssOrders;
import it.terna.nas.service.material.utility.Constants;

@Repository
public interface X4DssOrdersRepository extends JpaRepository<X4DssOrders, Long>
{
    @Query(value = "SELECT x.* FROM X4_DSS_ORDERS x WHERE DSS_PC_CORRELATION_ID = :correlationId AND TRATTA = :tratta", nativeQuery = true)
    public List<X4DssOrders> findOrdersByCorrelationAndTratta(String correlationId, String tratta);

    @Query(value = "SELECT X.* FROM X4_DSS_ORDERS X WHERE X.DSS_PC_CORRELATION_ID = :correlationId AND X3TRONCHIID = :tronco", nativeQuery = true)
    public List<X4DssOrders> findOrdersByCorrelationAndTronco(String correlationId, Long tronco);

    @Query(value = "select distinct c.x3TronchiId from X4DssOrders c where c.dssPcCorrelationId = :correlationId", nativeQuery = false)
    @QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
    public Stream<Long> findTronchiByCorrelation(String correlationId);
    
    @Query(value = "SELECT x.TRATTA FROM X4_DSS_ORDERS x WHERE x.DSS_PC_CORRELATION_ID = :correlationId AND x.TRATTA IS NOT NULL GROUP BY x.TRATTA", nativeQuery = true)
    public Stream<String> findTrattaByCorrelation(String correlationId);
    
}
