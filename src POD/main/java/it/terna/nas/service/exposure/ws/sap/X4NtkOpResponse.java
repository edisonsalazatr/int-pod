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
public class X4NtkOpResponse extends MaximoResponse {

    private static final long serialVersionUID = -8330165594842765236L;

    private List<X4NtkOpResponseItem> member;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = false)
    @ToString
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class X4NtkOpResponseItem implements Serializable {

        private static final long serialVersionUID = -5385039504004394910L;

        private String  href;
        private Long    x4odmopid;
        private String  description;
        private String  noperazione;
        private String  hasld;
        private Long    x4odmid;
    }
}
