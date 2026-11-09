package it.terna.nas.service.dal.service.repository;

import java.util.List;
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
public interface X4SnowRepository extends JpaRepository<X4Snow, String>
{
	@Query(value="SELECT K.* FROM (SELECT C.*, LEVEL AS LIVELLO FROM X4SNOW C START WITH CODICE_OGGETTO IN (:assets) CONNECT BY PRIOR CODICE_OGGETTO = CODICE_PADRE) K ORDER BY LIVELLO", nativeQuery = true)
	@QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
	public Stream<X4Snow> getHierarchy(@Param("assets") List<String> assets);
}
