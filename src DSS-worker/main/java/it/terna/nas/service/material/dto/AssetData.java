package it.terna.nas.service.material.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import static it.terna.kernel.basement.utility.Assorted.*;
import it.terna.nas.service.dal.entity.eam.Asset;
import it.terna.nas.service.dal.entity.eam.AttivitaAsset;
import it.terna.nas.service.dal.entity.eam.AttributiAsset;
import it.terna.nas.service.dal.entity.eam.DifformitaAsset;
import it.terna.nas.service.dal.entity.eam.HealthIndexAsset;
import it.terna.nas.service.dal.entity.eam.InterferenzeAsset;
import it.terna.nas.service.dal.entity.eam.VariabileAsset;
import it.terna.nas.service.material.utility.Catalog.VariableType;
import it.terna.nas.service.material.utility.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssetData implements Serializable{
	
	private static final long serialVersionUID = 4272400058549151076L;
	
	private Asset 											asset;
    private Map<String, HealthIndexAsset>	 				hi;
    private Map<String, HealthIndexAsset>	 				tassiGA;
    private Map<String, List<InterferenzeAsset>> 			interferenze;
    private Map<String, List<DifformitaAsset>>				difformita;
    private List<AttivitaAsset> 							attivita;
    private Map<String, AttributiAsset>						attributi;
    // variabili e misure;
    private Map<String, VariabileAsset>						misure;
    
    private Integer level;
    
    public Object search(String searchString, VariableType type) {
    	return search(searchString, type, null);
    }
    
    public Object search(String searchString, VariableType type, Object target) {
        if (searchString == null) {
            return null;
        }

        switch (type) {
            case HEALTH_INDEX:
                return hi != null ? hi.get(searchString) : null;
                
            case TASSO_GUASTO:
                return tassiGA != null ? tassiGA.get(searchString) : null;
                
            case TASSO_ANOMALIA:
                return tassiGA != null ? tassiGA.get(searchString) : null;

            case ATTIVITA:
                return attivita != null ? attivita.stream()
                        .filter(attivita -> attivita.getJpnum().contains(searchString) 
                        		            && !Constants.CHIUSO.equals(attivita.getStatus()))
                        .findFirst()
                        .orElse(null) : null;

            case CATALOGO_INTERFERENZA:
                return searchInListWithTarget(interferenze, searchString, target, InterferenzeAsset::getTarget);

            case CATALOGO_DIFFORMITA:
                return searchInListWithTarget(difformita, searchString, target, DifformitaAsset::getTarget);

            case VARIABILE:
            case MISURA:
                return misure != null ? misure.get(searchString) : null;

            case ASSETSPEC:
                return getAttribute(searchString);

            default:
                return null;
        }
    }

    
    private AttributiAsset getAttribute(String attributeName)
    {
        if (attributi != null)
        {
            AttributiAsset attributiAsset = attributi.get(attributeName);
            
            if (notNull(attributiAsset))
            {
                boolean exist = isNotEmpty(attributiAsset.getAlnValue()) || notNull(attributiAsset.getDateValue()) || notNull(attributiAsset.getNumValue()) || isNotEmpty(attributiAsset.getTableValue());
                if (exist)
                    return attributiAsset;
            }
        }
        
        return null;
    }
    
    
    private <T> T searchInListWithTarget(Map<String, List<T>> map, String searchString, Object target, Function<T, Object> functionExtractor) 
    {
        if (map == null || map.get(searchString) == null) {
            return null;
        }

        List<T> list = map.get(searchString);

        if (target != null) {
            return list.stream()
		                .filter(item -> {
		                    Object value = functionExtractor.apply(item);
		                    return value != null && value.equals(target); 
		                })
                       .findFirst()
                       .orElse(null);
        }

        return list.stream()
                   .findFirst()
                   .orElse(null);
    }


}
