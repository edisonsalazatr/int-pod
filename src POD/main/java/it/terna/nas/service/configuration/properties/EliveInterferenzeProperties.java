package it.terna.nas.service.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "interferenze.elive")
@Getter
@Setter
@Data
public class EliveInterferenzeProperties {
	
    private String interferenzaVerticaleBasso;
    private String interferenzaVerticaleAlto;
    private String interferenzaVerticaleSingolo;
    private String interferenzaVerticaleAreale;

    private String interferenzaSbandataBasso;
    private String interferenzaSbandataAlto;
    private String interferenzaSbandataSingolo;
    private String interferenzaSbandataAreale;

    private String interferenzaRibaltamentoBasso;
    private String interferenzaRibaltamentoAlto;
    private String interferenzaRibaltamentoSingolo;
    private String interferenzaRibaltamentoAreale;
}
