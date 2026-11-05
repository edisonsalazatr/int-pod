package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.AssetLocRelation;
import it.terna.nas.service.material.utility.Constants;
import it.terna.nas.service.material.utility.Catalog.ESQLMappingParameter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AssetQueryService
{
    private static final String QUERY_01 = """
                                        	SELECT DISTINCT 
                                            	asset.assetnum,
                                            	asset.ANCESTOR,
                                            	asset.classStructureId,
                                            	asset.description,
                                            	asset.parent,
                                            	asset.status,
                                            	asset.x3Gewrk,
                                            	asset.x3Ingrp,
                                            	asset.x3RootAssetNum,
                                            	asset.x3SapId,
                                            	asset.siteId,
                                            	asset.x4oldstatus
                                            FROM
                                            	WORKORDER w
                                            JOIN JOBPLAN j  ON w.jpnum = j.jpnum
                                            JOIN ASSET asset ON asset.assetnum = w.assetnum
                                            	AND ((j.orgid = w.orgid
                                            		AND j.siteid = w.siteid)
                                            	OR (j.orgid = w.orgid
                                            		AND j.siteid IS NULL)
                                            	OR (j.orgid IS NULL
                                            		AND j.siteid IS NULL))
                                            	AND j.status = 'ACTIVE'
                                            WHERE
                                            	w.jpnum = :jpnum
                                        		""";
    
    private static final String QUERY_02 = """
                                        	SELECT a3.assetnum,
                                        		a3.ANCESTOR,
                                        		a3.classStructureId,
                                        		a3.description,
                                        		a3.parent,
                                        		a3.status,
                                        		a3.x3Gewrk,
                                        		a3.x3Ingrp,
                                        		a3.x3RootAssetNum,
                                        		a3.x3SapId,
                                        		a3.siteId,
                                            	a3.x4oldstatus
                                        	FROM ASSETSPEC a, ASSET a2, ASSET a3 
                                        	WHERE TABLEVALUE = :tronconum
                                        		AND a.ASSETATTRID  = 'LIN_TRONCO_ISPEZIONE'
                                        		AND a.ASSETNUM = a2.ASSETNUM 
                                        		AND a2.PARENT  = a3.ASSETNUM
                                        		""";
    		
    
    private static final String QUERY_03 = """
                                    SELECT a3.assetnum,
                                        a3.ANCESTOR,
                                        a3.classStructureId,
                                        a3.description,
                                        a3.parent,
                                        a3.status,
                                        a3.x3Gewrk,
                                        a3.x3Ingrp,
                                        a3.x3RootAssetNum,
                                        a3.x3SapId,
                                        a3.siteId,
                                        a3.x4oldstatus
                                    FROM ASSET a3 
                                    WHERE a3.ASSETID = :assetid  """;

    private static final String QUERY_04 = """
                                    SELECT SOURCEASSETNUM , TARGETASSETNUM, SITEID, ORGID,  ASSETLOCRELATIONID, X3SAPID  
                                    FROM ASSETLOCRELATION
                                    WHERE TARGETASSETNUM  = :assetNum 
                                        AND SITEID = :source
                                    ORDER BY X3SAPID ASC
                                    FETCH FIRST 1 ROW ONLY """;

    private static final String QUERY_07 = """
                                    SELECT a3.assetnum,
                                            a3.ANCESTOR,
                                            a3.classStructureId,
                                            a3.description,
                                            a3.parent,
                                            a3.status,
                                            a3.x3Gewrk,
                                            a3.x3Ingrp,
                                            a3.x3RootAssetNum,
                                            a3.x3SapId,
                                            a3.siteId,
                                            a3.x4oldstatus
                                    FROM ASSET a3 
                                    WHERE a3.assetnum in (select ancestor from asset where assetnum=:assetNum 
                                        and siteid=:source) """;

    private static final String QUERY_05 = """
                                    SELECT  D.ASSETNUM,
                                            D.ANCESTOR,
                                            D.CLASSSTRUCTUREID,
                                            D.DESCRIPTION,
                                            D.PARENT,
                                            D.STATUS,
                                            D.X3GEWRK,
                                            D.X3INGRP,
                                            D.X3ROOTASSETNUM,
                                            D.X3SAPID,
                                            D.SITEID,
                                            D.x4oldstatus
                                    FROM ASSETSPEC A, ASSET C, ASSET D, CLASSSTRUCTURE E 
                                    WHERE A.ASSETATTRID  = 'LIN_TRONCO_ISPEZIONE' 
                                        AND A.TABLEVALUE IS NOT NULL
                                        AND A.ASSETNUM = C.ASSETNUM
                                        AND C.PARENT = D.ASSETNUM
                                        AND D.CLASSSTRUCTUREID = E.CLASSSTRUCTUREID 
                                        AND E.CLASSIFICATIONID IN ( 'CAMPATA AEREA', 'TRATTA CAVO')
                                        AND D.ASSETNUM = :assetNum
                                    UNION 
                                    SELECT  D.ASSETNUM,
                                            D.ANCESTOR,
                                            D.CLASSSTRUCTUREID,
                                            D.DESCRIPTION,
                                            D.PARENT,
                                            D.STATUS,
                                            D.X3GEWRK,
                                            D.X3INGRP,
                                            D.X3ROOTASSETNUM,
                                            D.X3SAPID,
                                            D.SITEID,
                                            D.x4oldstatus
                                    FROM ASSETSPEC A, ASSET C, ASSET D, CLASSSTRUCTURE E 
                                    WHERE A.ASSETATTRID  = 'LIN_TRONCO_ISPEZIONE' 
                                        AND A.TABLEVALUE IS NOT NULL
                                        AND A.ASSETNUM = C.ASSETNUM
                                        AND C.PARENT = D.ASSETNUM
                                        AND D.CLASSSTRUCTUREID = E.CLASSSTRUCTUREID 
                                        AND E.CLASSIFICATIONID IN ( 'CAMPATA AEREA', 'TRATTA CAVO')
                                        AND C.ASSETNUM = :assetNum
                                    """;
    
    
    @Autowired
    private DataAccessService   dataAccessService;

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public Asset getAssetByAssetId(String assetId) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getAssetByAssetId({}) |{}|", assetId, startTime);
    
        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETID.getName(), assetId);
    
        List<Asset> result = dataAccessService.search(Asset.class, QUERY_03, parameters, true, true);
    
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getAssetByAssetId found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);
    
        return Assorted.isNotEmpty(result) ? result.get(0) : null;
    }
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public Asset getAssetByAssetnum(String assetnum) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getAssetByAssetnum({}) |{}|", assetnum, startTime);
    
        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetnum);
    
        Asset asset = dataAccessService.find(Asset.class, parameters, true);
    
        long endTime = System.currentTimeMillis();
        log.debug("  - Search getAssetByAssetnum found [{}] |{}|", Assorted.isNull(asset) ? 0 : 1, endTime - startTime);
    
        return asset;
    }

    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<Asset> getAssetByJpnum(String jpnum) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getAssetnumByJpnum({}) |{}|", jpnum, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.JPNUM.getName(), jpnum);

        List<Asset> result = dataAccessService.search(Asset.class, QUERY_01, parameters, true, true);

        long endTime = System.currentTimeMillis();
        log.debug("  - Search getAssetnumByJpnum found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<Asset> getAssetByTronco(String tronconum) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getAssetByTronco({}) |{}|", tronconum, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.TRONCO.getName(), tronconum);

        List<Asset> result = dataAccessService.search(Asset.class, QUERY_02, parameters, true, true);

        long endTime = System.currentTimeMillis();
        log.debug("  - Search getAssetByTronco found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<Asset> getAssetAssociatedTronco(String assetNum) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getAssetAssociatedTronco({}) |{}|", assetNum, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum.toString());

        List<Asset> result = dataAccessService.search(Asset.class, QUERY_05, parameters, true, true);

        long endTime = System.currentTimeMillis();
        log.debug("  - Search getAssetAssociatedTronco found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return result;
    }
    
    
    @Cacheable(cacheManager = Constants.CACHE_MANAGER, value = Constants.CACHE_ASSET, key = "{#root.methodName, #assetNum, #siteId}", unless = "#result==null OR #result.isEmpty()")
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<Asset> getAssetByAncestor(String assetNum, String siteId) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getAssetByAncestor({}, {}) |{}|", assetNum, siteId, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
        parameters.put(ESQLMappingParameter.SOURCE.getName(), siteId);

        List<Asset> result = dataAccessService.search(Asset.class, QUERY_07, parameters, true, true);

        long endTime = System.currentTimeMillis();
        log.debug("  - Search getAssetByAncestor found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return result;
    }
    
    
    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public List<AssetLocRelation> getAssetLocRelationByAsset(String assetNum, String siteId) 
            throws DataLayerException
    {
        long startTime = System.currentTimeMillis();
        log.debug("call getAssetLocRelationByAsset({}, {}) |{}|", assetNum, siteId, startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put(ESQLMappingParameter.ASSETNUM.getName(), assetNum);
        parameters.put(ESQLMappingParameter.SOURCE.getName(), siteId);

        List<AssetLocRelation> result = dataAccessService.search(AssetLocRelation.class, QUERY_04, parameters, true, true);

        long endTime = System.currentTimeMillis();
        log.debug("  - Search getAssetLocRelationByAsset found [{}] |{}|", Assorted.isNotEmpty(result) ? result.size() : 0, endTime - startTime);

        return result;
    }    
}
