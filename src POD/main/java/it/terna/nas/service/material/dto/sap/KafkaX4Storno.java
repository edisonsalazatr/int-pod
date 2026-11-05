package it.terna.nas.service.material.dto.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Data
public class KafkaX4Storno {

    private static final long serialVersionUID = -1849260788499957091L;

    private String wonum;
    private String startdatetime;
    private String finishdatetime;
    private String laborcode;
    private String refwo;
    private String x4odm;
    private String siteid;
    private Long   labtransid;
    private Long   workorderid;
    private String href;
    private String externalrefid;

}
