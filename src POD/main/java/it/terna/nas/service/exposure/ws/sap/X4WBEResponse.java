package it.terna.nas.service.exposure.ws.sap;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class X4WBEResponse extends MaximoResponse {

    private static final long serialVersionUID = -745376854272980737L;

    private List<X4WBEResponseItem> member;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class X4WBEResponseItem implements Serializable {

        private static final long serialVersionUID = -483127816427242896L;

        private String href;
        private String idwbe;
        private String idwbs;
        private String description;
        private String statosistemasap;
        private String statoutentesap;
        private String cdcresp;
        private String cdcrich;
        private String socrich;
        private String createdate;
        private String changedate;
        private String tipoprog;
        private String profitcenter;
        private String sedetecnica;
        private String motinvestimento;
        private String codincentivazione;
        private String codintervento;
    }
}