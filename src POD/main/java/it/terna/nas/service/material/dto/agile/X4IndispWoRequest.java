package it.terna.nas.service.material.dto.agile;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.terna.kernel.basement.utility.Assorted;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import java.io.Serializable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class X4IndispWoRequest implements Serializable {

    private final static long serialVersionUID = 7037819209656301890L;

    private String  wonum;
    private String  x4cluster;
    private String  description;
    private String  inizioindisponibilita;
    private String  fineindisponibilita;
    private String  status;
    private String  motivoann;
    private String  esito;
    private String  iderrore;
    private String  descerrore;

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
