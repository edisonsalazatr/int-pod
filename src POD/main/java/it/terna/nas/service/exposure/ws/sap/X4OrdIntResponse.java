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
public class X4OrdIntResponse extends MaximoResponse {

    private static final long serialVersionUID = -745376854272980737L;

    private List<X4OrdIntResponseItem> member;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class X4OrdIntResponseItem implements Serializable {

        private static final long serialVersionUID = -483127816427242896L;

        private String  href;
        private Long    x4ordintid;
        private String  idoi;
        private String  description;
        private Long    hasld;
        private String  statosistema;
        private String  statoutente;
        private String  tipooi;
        private String  catord;
        private String  createddate;
        private String  changeddate;
        private String  societa;
        private String  cdcresp;
        private String  sedetecnica;
        private String  codcliente;
        private String  cdccosti;
        private String  profitcenter;
        private String  classeoggetto;
        private String  wbe;
        private String  idodm;
    }
}
