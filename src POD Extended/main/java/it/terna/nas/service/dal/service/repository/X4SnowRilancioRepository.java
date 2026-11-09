package it.terna.nas.service.dal.service.repository;

import java.util.stream.Stream;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import it.terna.nas.service.dal.entity.eam.X4SnowRilancio;
import it.terna.nas.service.material.utility.Constants;

public interface X4SnowRilancioRepository extends JpaRepository<X4SnowRilancio, Long>
{
    @Query("select x from X4SnowRilancio x where x.daElaborare = :daElaborare order by x.id asc")
    @QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
    public Stream<X4SnowRilancio> findByDaElaborare(@Param("daElaborare") String daElaborare);

    @Modifying
    @Query("update X4SnowRilancio x set x.daElaborare = :daElaborare where x.id = :id")
    public int updateDaElaborare(@Param("id") Long id, @Param("daElaborare") String daElaborare);
}
