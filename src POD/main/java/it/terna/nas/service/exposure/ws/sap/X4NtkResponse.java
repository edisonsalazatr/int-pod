package it.terna.nas.service.exposure.ws.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class X4NtkResponse extends MaximoResponse {

    private static final long serialVersionUID = -1850871167904751200L;

    private List<X4NtkResponseItem> member;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class X4NtkResponseItem {

        private static final long serialVersionUID = 6366021656430662498L;

        private Long   x4ntkid;
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
        private String href;

    }

}
