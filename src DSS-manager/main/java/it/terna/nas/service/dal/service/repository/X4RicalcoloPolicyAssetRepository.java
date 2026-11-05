package it.terna.nas.service.dal.service.repository;

import java.util.stream.Stream;
import javax.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;
import it.terna.nas.service.dal.entity.eam.X4RicalcoloPolicyAsset;
import it.terna.nas.service.material.utility.Constants;

@Repository
public interface X4RicalcoloPolicyAssetRepository extends JpaRepository<X4RicalcoloPolicyAsset, Long>
{
    @Query(value = "select c from X4RicalcoloPolicyAsset c where c.assetNum is null and c.x4DssWorkedHistoryId is null and c.elaborato = 'N'", nativeQuery = false)
    @QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
    public Stream<X4RicalcoloPolicyAsset> findRicalcoloByPolicy();
    
    
    @Query(value = "select c from X4RicalcoloPolicyAsset c where c.assetNum is not null and c.x4DssWorkedHistoryId is null and c.elaborato = 'N'", nativeQuery = false)
    @QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
    public Stream<X4RicalcoloPolicyAsset> findRicalcoloByPolicyAndAsset();

    
    @Query(value = "select c from X4RicalcoloPolicyAsset c where c.assetNum is not null and c.x4DssWorkedHistoryId is not null and c.elaborato = 'N'", nativeQuery = false)
    @QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
    public Stream<X4RicalcoloPolicyAsset> findRicalcoloByPolicyAndAssetAndHistory();
}
