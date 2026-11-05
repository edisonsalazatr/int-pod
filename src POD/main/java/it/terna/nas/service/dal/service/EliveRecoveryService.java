package it.terna.nas.service.dal.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.terna.kernel.aspect.datasource.annotation.Datasource;
import it.terna.kernel.basement.exception.DataLayerException;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.basement.utility.Common.EDatasourceKey;
import it.terna.kernel.storage.service.DataAccessService;
import it.terna.nas.service.dal.entity.eam.AssetLocRelation;
import it.terna.nas.service.material.dto.KafkaOdlToEliveRequest;
import it.terna.nas.service.material.dto.KafkaOdlToEliveRequest.AssetLocKafka;
import it.terna.nas.service.material.utility.EamEtlUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EliveRecoveryService {

    @Autowired
    private DataAccessService dataAccessService;
    
    private static final String QUERY_ASSET_LOC_RELATION = """
            SELECT ASSETLOCRELATIONID AS ID,
            	   SOURCEASSETNUM,
                   TARGETASSETNUM,
                   X3SAPID
            FROM ASSETLOCRELATION
            WHERE TARGETASSETNUM = :assetNum
              AND SITEID = :source
            ORDER BY X3SAPID ASC
            FETCH FIRST 1 ROW ONLY
            """;

    public KafkaOdlToEliveRequest buildKafkaOdlToEliveRequest(Tuple tuple) throws DataLayerException {
        if (tuple == null) {
            throw new IllegalArgumentException("Cannot build KafkaOdlToEliveRequest from null Tuple");
        }

        KafkaOdlToEliveRequest request = new KafkaOdlToEliveRequest();

        String wonum = getString(tuple, "WONUM");
        String status = getString(tuple, "STATUS");
        String jpnum = getString(tuple, "JPNUM");
        String assetnum = getString(tuple, "ASSETNUM");
        String siteid = getString(tuple, "SITEID");
        String targstartdate = getString(tuple, "TARGSTARTDATE");

        if (!StringUtils.hasText(wonum)) {
            throw new IllegalArgumentException("Cannot build KafkaOdlToEliveRequest: WONUM is null or blank");
        }

        if (!StringUtils.hasText(jpnum)) {
            throw new IllegalArgumentException("Cannot build KafkaOdlToEliveRequest: JPNUM is null or blank");
        }

        if (!StringUtils.hasText(assetnum)) {
            throw new IllegalArgumentException("Cannot build KafkaOdlToEliveRequest: ASSETNUM is null or blank");
        }

        if (!StringUtils.hasText(siteid)) {
            throw new IllegalArgumentException("Cannot build KafkaOdlToEliveRequest: SITEID is null or blank");
        }

        if (!StringUtils.hasText(targstartdate)) {
            throw new IllegalArgumentException("Cannot build KafkaOdlToEliveRequest: TARGSTARTDATE is null or blank");
        }

        request.setWonum(wonum);
        request.setStatus(status);
        request.setJpnum(jpnum);
        request.setTargstartdate(EamEtlUtils.formatTargstartdateForElive(targstartdate));
        request.setAssetnum(Long.valueOf(assetnum));

        AssetLocRelation relation = getFirstAssetLocRelationByAsset(assetnum, siteid);

        if (relation == null || !StringUtils.hasText(relation.getX3SapId())) {
            throw new IllegalArgumentException(
                    "Cannot build KafkaOdlToEliveRequest: X3SAPID not found for assetnum " + assetnum + " and siteid " + siteid
            );
        }

        AssetLocKafka assetLocKafka = new AssetLocKafka();
        assetLocKafka.setX3sapid(relation.getX3SapId());

        request.setAssetlocrelation(List.of(assetLocKafka));

        return request;
    }

    @Datasource(datasourceKey = EDatasourceKey.EAM)
    public AssetLocRelation getFirstAssetLocRelationByAsset(String assetNum, String siteId) throws DataLayerException {
        long startTime = System.currentTimeMillis();

        log.debug("call getFirstAssetLocRelationByAsset({}, {}) |{}|",
                assetNum,
                siteId,
                startTime);

        Map<String, String> parameters = new HashMap<>();
        parameters.put("assetNum", assetNum);
        parameters.put("source", siteId);

        List<AssetLocRelation> result = dataAccessService.search(
                AssetLocRelation.class,
                QUERY_ASSET_LOC_RELATION,
                parameters,
                true,
                true
        );

        long endTime = System.currentTimeMillis();

        log.debug("Search getFirstAssetLocRelationByAsset found [{}] |{}|",
                Assorted.isNotEmpty(result) ? result.size() : 0,
                endTime - startTime);

        if (Assorted.isEmpty(result)) {
            return null;
        }

        return result.get(0);
    }

    private String getString(Tuple tuple, String alias) {
        Object value = tuple.get(alias);
        return value != null ? value.toString() : null;
    }


}
