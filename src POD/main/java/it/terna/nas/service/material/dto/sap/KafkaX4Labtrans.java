package it.terna.nas.service.material.dto.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;


@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Data
@ToString
public class KafkaX4Labtrans implements Serializable
{
    private static final long serialVersionUID = 5125688819024090541L;

    private String laborcode;
    private Double regularhrs;
    private String startdatetime;
    private String labtransid;
    private String refwo;
    private String siteid;
    private String x4oretrasf;
    private String href;
    private String approver;
    private String x4oreuff;
    private Long   x4correlationid;

}
