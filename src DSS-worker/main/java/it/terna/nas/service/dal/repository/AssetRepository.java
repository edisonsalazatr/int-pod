package it.terna.nas.service.dal.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.material.dto.ConditionTaskProjection;
import it.terna.nas.service.material.dto.NoConditionTaskProjection;
import it.terna.nas.service.material.utility.SqlQueryConstants;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String>
{

    @Query(value = SqlQueryConstants.QUERY_ASSET_TESATURA_CONDUTTORE, nativeQuery = true)
    public List<String> findAssetForTesaturaForConduttore(@Param(value = "ANCESTOR") String ancestor,
                                                          @Param(value = "ATTRVALUE") BigDecimal numValue,
                                                          @Param(value = "X3ROOTASSET") String x3RootAsset);

    @Query(value = SqlQueryConstants.QUERY_ASSET_TESATURA_FUNE, nativeQuery = true)
    public List<String> findAssetForTesaturaForFune( @Param(value = "ASSETNUM") String assetNum, 
                                                     @Param(value = "SITEID") String siteId, 
                                                     @Param(value = "CLASSSTRUCTUREID") String classstructureId,
                                                     @Param(value = "ANCESTOR") String ancestor);
    
    @Query(value = SqlQueryConstants.QUERY_CONDUTTORE, nativeQuery = true)
    public List<Asset> findAssetToConduttori(@Param(value = "ASSETNUM") String assetNum);

    @Query(value = SqlQueryConstants.QUERY_STRUTTURA, nativeQuery = true)
    public List<Asset> findAssetToStruttura(@Param(value = "ASSETNUM") String assetNum);

    @Query(value = SqlQueryConstants.QUERY_CONDUTTORI, nativeQuery = true)
    public List<Asset> findAssetToConduttoriFromLinea(@Param(value = "ASSETNUM") String assetNum);

    @Query(value = SqlQueryConstants.QUERY_FUNE_DI_GUARDIA, nativeQuery = true)
    public List<Asset> findAssetToFuneDiGuardia(@Param(value = "ASSETNUM") String assetNum);
   
    @Query(value = SqlQueryConstants.QUERY_LINEE_FOR_ASSETLOCRELATION, nativeQuery = true)
    public List<String> findLineeForAssetLocRelation(@Param(value = "ASSETNUM") String assetNum);
    
    @Query(value = SqlQueryConstants.QUERY_FUNE_DI_GUARDIA2, nativeQuery = true)
    public List<Asset> findAssetToFuneDiGuardiaFromLinea(@Param(value = "ASSETNUM") String assetNum);
    
    @Query(value = SqlQueryConstants.QUERY_STRUTTURA_SAVE, nativeQuery = true)
    public List<Asset> findAssetToStrutturaFromLinea(@Param(value = "ASSETNUM") String assetNum);  
    
    @Query(value = SqlQueryConstants.QUERY_RETRIVE_CONDITION_MICROCONTROL, nativeQuery = true)
    List<ConditionTaskProjection> findTaskConditions(@Param("assetNum") String assetNum, @Param("jpnum") String jpnum);
    
    @Query(value = SqlQueryConstants.QUERY_RETRIVE_NO_CONDITION_MICROCONTROL, nativeQuery = true)
    List<NoConditionTaskProjection> findTaskNoConditions(@Param("assetNum") String assetNum, @Param("jpnum") String jpnum);
    
    
    @Query(value = """
    		SELECT
    			a.assetnum FROM ASSET a
    		WHERE a.parent = :assetNum
    		AND a.CLASSSTRUCTUREID in :classStructureIds
    		""", nativeQuery = true)
    List<String> getAssetnumByParentAndClassStructureIds(@Param("assetNum") String assetnum, 
    								  					 @Param("classStructureIds") List<String> classStructureIds);

}
