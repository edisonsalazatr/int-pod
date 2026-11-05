package it.terna.nas.service.exposure.ws.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class X4OrdIntRequest extends ExposureRequest {

    private Long   x4ordintid;
    private String idoi;
    private String description;
    private Long   hasld;
    private String statosistema;
    private String statoutente;
    private String tipooi;
    private String catord;
    private String createddate;
    private String changeddate;
    private String societa;
    private String cdcresp;
    private String sedetecnica;
    private String codcliente;
    private String cdccosti;
    private String profitcenter;
    private String classeoggetto;
    private String wbe;
    private String idodm;


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
