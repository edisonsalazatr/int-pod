package it.terna.nas.service.exposure.ws.sap;

import com.fasterxml.jackson.annotation.JsonInclude;
import it.terna.nas.service.exposure.ws.maximo.MaximoResponse;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class X4LabtransResponse extends MaximoResponse {

    private static final long serialVersionUID = -5665589214062446835L;

    private List<X4LabtransResponseItem> member;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @ToString
    public static class X4LabtransResponseItem implements Serializable {

        private static final long serialVersionUID = 3039670447514394757L;

        private String href;
        private String x4storno;
        private String labtransid;
        private String refwo;
        private String x4oreuff;
        private Double regularhrs;
        private String x4oretrasf;

    }

}
