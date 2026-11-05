package it.terna.nas.service.material.dto;

import javax.persistence.Id;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ConditionTaskDTO {

    @Id
    private String assetNum;

    private String assetDescription;

    private String siteId;

    private String classStructureId;

    private String idMicroControllo;

    private String parent;

    private String attribute;

    private String controllValue;

    private String anlValue;

    private Double numValue;

    private String tableValue;

    private String dateValue;

    private Boolean areEquals;
}
