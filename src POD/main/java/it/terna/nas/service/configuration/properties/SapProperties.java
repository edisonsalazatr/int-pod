package it.terna.nas.service.configuration.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "sap-properties")
@Data
public class SapProperties {

    private String statoDummy;
    private String startDate;
    private String formatDate;
    private String noDate;
    private String modEsecuzione;
    private Integer batchSize;
    private List<FilterSap> filters;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class FilterSap {
        private String  filtro;
        private String  tipoSpesa;
        private String  tipoAttivita;
        private String  tipoOrdine;
        private Integer livello;
        private String  stato;
    }

}
