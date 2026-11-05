package it.terna.nas.service.exposure.ws.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class X4NtkRequest extends ExposureRequest {

    private String x4ntkid;
    private String description;
    private String idntk;
    private String hasld;
    private String wbe;
    private String noperazione;
    private String statosistemasap;
    private String statoutentesap;
    private String progetto;
    private String societa;
    private String profitcenter;
    private String tipo;
    private String createdate;
    private String changeddate;
    private String cdcresp;
    private String changedby;
    private String statosistema;

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
