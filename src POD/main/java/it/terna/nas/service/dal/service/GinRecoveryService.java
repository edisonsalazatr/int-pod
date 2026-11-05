package it.terna.nas.service.dal.service;

import javax.persistence.Tuple;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import it.terna.nas.service.material.dto.KafkaOdlToAgileRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GinRecoveryService {

    public KafkaOdlToAgileRequest buildKafkaOdlToAgileRequest(Tuple tuple) {
        if (tuple == null) {
            throw new IllegalArgumentException("Cannot build KafkaOdlToAgileRequest from null Tuple");
        }

        KafkaOdlToAgileRequest request = new KafkaOdlToAgileRequest();

        String wonum = getString(tuple, "WONUM");

        if (!StringUtils.hasText(wonum)) {
            throw new IllegalArgumentException("Cannot build KafkaOdlToAgileRequest: WONUM is null or blank");
        }

        request.setWonum(wonum);
        request.setStatus(getString(tuple, "STATUS"));
        request.setDescription(getString(tuple, "DESCRIPTION"));
        request.setAssetnum(getString(tuple, "ASSETNUM"));
        request.setSiteid(getString(tuple, "SITEID"));
        request.setJpnum(getString(tuple, "JPNUM"));

        request.setTargstartdate(getString(tuple, "TARGSTARTDATE"));
        request.setTargcompdate(getString(tuple, "TARGCOMPDATE"));
        request.setSchedstart(getString(tuple, "SCHEDSTART"));
        request.setSchedfinish(getString(tuple, "SCHEDFINISH"));

        request.setX4wbs(getString(tuple, "X4WBS"));
        request.setX4tipospesa(getString(tuple, "X4TIPOSPESA"));
        request.setPluscjprevnum(getInteger(tuple, "PLUSCJPREVNUM"));

        return request;
    }

    private String getString(Tuple tuple, String alias) {
        Object value = tuple.get(alias);
        return value != null ? value.toString() : null;
    }

    private Integer getInteger(Tuple tuple, String alias) {
        Object value = tuple.get(alias);

        if (value == null) {
            return null;
        }

        if (value instanceof Number) {
            return ((Number) value).intValue();
        }

        String stringValue = value.toString();

        if (!StringUtils.hasText(stringValue)) {
            return null;
        }

        return Integer.valueOf(stringValue);
    }
}
