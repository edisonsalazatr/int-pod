package it.terna.nas.service.material.dto;

import java.io.Serializable;
import java.util.List;
import it.terna.nas.service.material.utility.Catalog.EAMErrorMapping;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.util.Pair;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EsitoElab implements Serializable
{
    private static final long serialVersionUID = -114669030987256334L;
    private List<Pair<String, String>> listPairAssetWonum;
    private EAMErrorMapping esito;
    private String description;
    private String troncoNum;
    
    public EsitoElab(EAMErrorMapping esito)
    {
        this.esito = esito;
    }
}
