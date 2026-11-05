package it.terna.nas.service.dal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.X4CatModelloAsset;
import it.terna.nas.service.material.utility.SqlQueryConstants;

@Repository
public interface X4CatModelloAssetRepository extends JpaRepository<X4CatModelloAsset, Long> 
{
	public List<X4CatModelloAsset> findByX4CatModelloId(Long modelloAssetId);
	
	@Query(value = SqlQueryConstants.QUERY_X4CATMODELLOASSET_BY_IDMODELLO, nativeQuery = true)
	public List<X4CatModelloAsset> findByIdModello(@Param(value = "IDMODELLO") String idModello);
}
