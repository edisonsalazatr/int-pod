package it.terna.nas.service.dal.service.repository;

import java.util.List;
import java.util.stream.Stream;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.material.utility.Constants;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String>
{
	static String QUERY_ASSETNUM = """
			select c.assetNum, c.parent from Asset c where c.classStructureId in :classStructureIds and c.siteId = :siteId and VERIFICA_ASSET_PKG.VERIFICA_ASSET(c.assetNum, c.classStructureId) = 'OK'
			""";
	
	@Query(value = QUERY_ASSETNUM , nativeQuery = true )
	@QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
	public Stream<IAssetMinimal> findAllByClassStructureId(@Param("classStructureIds") List<String> classStructureId, 
													@Param("siteId") String siteId);
	
	@Query("select count(c.assetNum) from Asset c where c.classStructureId in (:classStructureId) and c.siteId = :siteId")
	public Integer findCountByClassStructureId(@Param("classStructureId") List<String> classStructureId, @Param("siteId") String siteId);
	
	@Query(value = """
		    SELECT COUNT(*) 
		    FROM (
		        SELECT a.assetnum, VERIFICA_ASSET_PKG.VERIFICA_ASSET(a.assetnum, a.classstructureid) AS esito
		        FROM asset a
		        WHERE a.classstructureid IN (:classStructureId)
		          AND a.siteid = :siteId
		    ) p
		    WHERE p.esito = 'OK'
		    """, nativeQuery = true)
	public Integer findCountByClassStructureIdAndEsitoOk(
		    @Param("classStructureId") List<String> classStructureId,
		    @Param("siteId") String siteId
		);

	
	
	 static final String QUERY_STALLO = """
	    		select c.ASSETNUM, c.PARENT
	from ASSET c
	where c.CLASSSTRUCTUREID = :tipologiaAsset
		and	 c.ASSETNUM  in (
	        select a.PARENT
	        from ASSET a
	        WHERE a.CLASSSTRUCTUREID in :classStructureIds
	        AND a.parent = c.assetnum)
	    		""";
	 
	@Query(value = QUERY_STALLO, nativeQuery = true)
	@QueryHints(@QueryHint(name = Constants.HINT_FETCH_SIZE, value = Constants.HINT_FETCH_SIZE_VALUE))
	public Stream<IAssetMinimal> findStalli(@Param("classStructureIds") List<String> classStructureId, 
										   @Param("tipologiaAsset") String tipologiaAssetStallo);
	
	
	static final String QUERY_STAZIONE = """
			SELECT b.parent as asset, count(b.parent) as children
FROM (
	select c.ASSETNUM, c.PARENT
	from ASSET c
	where c.CLASSSTRUCTUREID = :tipologiaAsset
		and	 c.ASSETNUM  in (
	        select a.PARENT
	        from ASSET A
	        WHERE a.CLASSSTRUCTUREID in :classStructureIds
	        AND a.parent = c.assetnum)
	group by c.ASSETNUM, c.PARENT) B
	GROUP BY b.PARENT 
			""";
	@Query(value = QUERY_STAZIONE, nativeQuery = true)
	public List<IAssetCount> findStazioniWithNumeroStalli(@Param("classStructureIds") List<String> classStructureId, 
			   @Param("tipologiaAsset") String tipologiaAssetStallo);
	
	

	public static final String QUERY_TRONCO = """
            SELECT A.ASSETNUM, A.PARENT
            FROM ASSET A, ASSET B, ASSETSPEC A2, X3TRONCHI XT, ASSETLOCRELATION A3  
            WHERE XT.TRONCONUM = :tronconum
                AND A3.SOURCEASSETNUM = XT.ASSETNUM
                AND A3.TARGETASSETNUM = A.ASSETNUM 
                AND A.ASSETNUM = B.PARENT                 
                AND B.ASSETNUM = A2.ASSETNUM                  
                AND A2.ASSETATTRID  = 'LIN_TRONCO_ISPEZIONE'
                AND A2.CLASSSTRUCTUREID = B.CLASSSTRUCTUREID 
                AND A2.TABLEVALUE = :tronconum
                AND VERIFICA_ASSET_PKG.VERIFICA_ASSET(A.ASSETNUM, A.CLASSSTRUCTUREID) = 'OK'
                AND A.CLASSSTRUCTUREID IN (:classStructureIds) """;
	
    public static final String QUERY_COUNT_TRONCO = "SELECT COUNT(*) FROM (" + QUERY_TRONCO + ") p ";
	
    @Query(value = QUERY_TRONCO, nativeQuery = true)
    public Stream<IAssetMinimal> findAssetsTronco(@Param("tronconum") String tronconum, @Param("classStructureIds") List<String> classStructureId);

    @Query(value = QUERY_COUNT_TRONCO, nativeQuery = true)
    public Integer findCountByAssetsTronco(@Param("tronconum") String tronconum, @Param("classStructureIds") List<String> classStructureId);
}
