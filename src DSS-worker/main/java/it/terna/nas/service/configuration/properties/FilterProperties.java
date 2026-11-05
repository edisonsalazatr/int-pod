package it.terna.nas.service.configuration.properties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FilterProperties
{
    private Boolean aggregate;
    private Boolean tronco;
    private String policy;
    private String tipologiaAsset;
}
