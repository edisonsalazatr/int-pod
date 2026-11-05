package it.terna.nas.service.material.dto.cavi;

import javax.persistence.Tuple;

import it.terna.nas.service.material.utility.DateMapper;

public record WorkOrderRecuperoCavi(
	
	String assetnum,
	String wonum,
	String changedate,
	String targstartdate,
	String x4datamodello,
	String jpnum,
	String parent,
	String siteid,
	String status,
	String x4tronconum,
	String x4sapidasset,
	String x3ingrp,
	String x3gewrk
	) {
	public static WorkOrderRecuperoCavi from(Tuple t) {
        return new WorkOrderRecuperoCavi(
        	t.get("assetnum", String.class),
        	t.get("wonum", String.class),
        	DateMapper.toIsoOffsetString(t.get("changedate")),
        	DateMapper.toIsoOffsetString(t.get("targstartdate")),
        	DateMapper.toIsoOffsetString(t.get("x4datamodello")),
        	t.get("jpnum", String.class),
        	t.get("parent", String.class),
        	t.get("siteid", String.class),
        	t.get("status", String.class),
        	t.get("x4tronconum", String.class),
        	t.get("x4sapidasset", String.class),
        	t.get("x3ingrp", String.class),
            t.get("x3gewrk", String.class)
        );
    }
}
