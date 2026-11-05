package it.terna.nas.service.material.dto.worker;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class WorkerFeedbackRequestEvent extends ExposureRequest
{

    private static final long serialVersionUID = 5054206847975062665L;

    private String            asset;
    private String            correlationId;
    private String            esito;
    private String            event;
    private Long              numberMessagePolicy;
    private Long              policy;
    private String            tipology;
    private String            type;
    private Boolean           addTronco;
    
    @Override
    public String toJson()
    {
        try {
            return Assorted.toJson(this);
        } catch (Exception ex) {
            // log occurred error
            log.error("- Base WS Data - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
            // return empty JSON representation
            return null;
        }
    }

}
