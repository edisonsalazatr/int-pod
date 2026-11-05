package it.terna.nas.service.exposure.ws.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class X4OdMResponse extends MaximoResponse {

    private static final long serialVersionUID = -745376854272980737L;

    private List<X4OdMResponseItem> member;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class X4OdMResponseItem implements Serializable {

        private static final long serialVersionUID = -483127816427242896L;

        private String  href;
        private long    x4odmid;
        private String  description;
        private String  hasld;
        private String  idodm;
        private String  wbe;
        private String  tipoattivita;
        private String  tipoordine;
        private String  statosistema;
        private String  statoutente;
        private String  sedetecnica;
        private String  cdcresp;
        private String  cdccosti;
        private String  profitcenter;
        private String  createdby;
        private String  changeddate;
        private String  cdlresp;
        private String  createddate;
    }
}