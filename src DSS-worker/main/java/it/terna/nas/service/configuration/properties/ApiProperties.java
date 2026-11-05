package it.terna.nas.service.configuration.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import lombok.Getter;
import lombok.*;

@Configuration
@ConfigurationProperties(prefix = "api")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class ApiProperties
{
    private MicrocontrolliProperties microcontrolli;
    private WorkorderRetryProperties retry;
    private TransactionProperties disableTransaction;
}
