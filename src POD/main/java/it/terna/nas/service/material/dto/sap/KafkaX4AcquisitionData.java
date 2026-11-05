package it.terna.nas.service.material.dto.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class KafkaX4AcquisitionData {

    private static final long serialVersionUID = -4441138328902938331L;

    private String orderType;
    private String systemStatus;
    private String activityType;
    private String creationDateFrom;
    private String creationDateTo;
    private String modificationDateFrom;
    private String modificationDateTo;

}
