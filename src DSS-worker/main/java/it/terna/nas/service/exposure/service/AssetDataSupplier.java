package it.terna.nas.service.exposure.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import it.terna.kernel.basement.utility.Assorted;
import it.terna.nas.service.dal.entity.eam.AttivitaAsset;
import it.terna.nas.service.dal.entity.eam.AttributiAsset;
import it.terna.nas.service.dal.entity.eam.DifformitaAsset;
import it.terna.nas.service.dal.entity.eam.HealthIndexAsset;
import it.terna.nas.service.dal.entity.eam.InterferenzeAsset;
import it.terna.nas.service.dal.entity.eam.VariabileAsset;
import it.terna.nas.service.material.dto.AssetData;
import it.terna.nas.service.material.dto.policy.ValueHolder;
import it.terna.nas.service.material.dto.policy.types.PolicyValue;
import it.terna.nas.service.material.utility.Catalog.VariableType;
import it.terna.nas.service.material.utility.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AssetDataSupplier {
	
	@Autowired
	private ModelloResolver modelloResolver;
	
	public PolicyValue<?> getData(AssetData assetData, String key, VariableType source, Object target) {
	    Assert.hasLength(key, "key is null or empty");
	    Assert.notNull(source, "source is null or empty");
	    
	    PolicyValue<?> value = switch(source) {
	    
			case CATALOGO_DIFFORMITA, 
				 CATALOGO_INTERFERENZA -> Optional.ofNullable(assetData.search(key, source, target))
					                 .map(this::toPolicyValue)
					                 .orElse(null);
			case ASSETSPEC,
			 	MISURA,
			 	HEALTH_INDEX,
			 	TASSO_GUASTO,
			 	TASSO_ANOMALIA,
			 	VARIABILE,
			 	ATTIVITA -> Optional.ofNullable(assetData.search(key, source))
				                 .map(this::toPolicyValue)
				                 .orElse(null);
			 	
			case MODELLO -> createPolicyValue(modelloResolver.searchModelloAsset(parseLong(key), assetData));
				 
			default -> throw new IllegalArgumentException("Unexpected value: " + source);
	    };
	    
	    return value;
	}
	
	
	private Long parseLong(String numberString) 
	{
		Assert.hasLength(numberString, "numberString is null or empty");
		try {
			return Long.parseLong(numberString);
		} catch (NumberFormatException nfe) {
			log.warn(" - Error parsing {} into a number", numberString, nfe);
		}
		return null;
	}


	public PolicyValue<?> toPolicyValue(Object resource) {
	    if (resource instanceof HealthIndexAsset) {
	        return createPolicyValue((HealthIndexAsset) resource);
	    } else if (resource instanceof InterferenzeAsset) {
	        return createPolicyValue((InterferenzeAsset) resource);
	    } else if (resource instanceof DifformitaAsset) {
	        return createPolicyValue((DifformitaAsset) resource);
	    } else if (resource instanceof AttivitaAsset) {
	        return createPolicyValue((AttivitaAsset) resource);
	    } else if (resource instanceof AttributiAsset) {
	        return createPolicyValue((AttributiAsset) resource);
	    } else if (resource instanceof VariabileAsset) {
	        return createPolicyValue((VariabileAsset) resource);
	    } else {
	        throw new IllegalArgumentException("Unknown resource type: " + resource.getClass().getSimpleName());
	    }
	}

	private PolicyValue<?> createPolicyValue(HealthIndexAsset asset) {
	    return new ValueHolder<>(asset.getAttributeName(), asset.getObjectName(), asset.getValue());
	}

	private PolicyValue<?> createPolicyValue(InterferenzeAsset interferenza) {
		return new ValueHolder<>(interferenza.getIdInterferenza(),
	                                               interferenza.getDescription(), 
	                                               interferenza.getTarget());
	}
	        
	private PolicyValue<?> createPolicyValue(DifformitaAsset difformita) {
		return new ValueHolder<>(String.valueOf(difformita.getIdDifformita()), 
                difformita.getDescription(), 
                difformita.getTarget());
	}

	private PolicyValue<?> createPolicyValue(AttivitaAsset asset) {
	    return new ValueHolder<>(asset.getJpnum(), asset.getDescription(), asset.getStatus());
	}

	private PolicyValue<?> createPolicyValue(AttributiAsset asset) {
	    switch (asset.getDataType()) {
	        case "NUMERIC":
	            return new ValueHolder<>(asset.getAssetAttrId(), asset.getDescription(), asset.getNumValue());
	        case "ALN":
	            return new ValueHolder<>(asset.getAssetAttrId(), asset.getDescription(), asset.getAlnValue());
	        case "DATE":
	            return new ValueHolder<>(asset.getAssetAttrId(), asset.getDescription(), asset.getDateValue());
	        default:
	            return new ValueHolder<>(asset.getAssetAttrId(), asset.getDescription(), asset.getTableValue());
	    }
	}

	private PolicyValue<?> createPolicyValue(VariabileAsset asset) {
	    return new ValueHolder<>(asset.getIdMisura(), asset.getIdMisura(), asset.getValore());
	}
	
	
	private PolicyValue<?> createPolicyValue(ClusterDto modelloAsset) 
	{
	    if (Assorted.isNull(modelloAsset))
	        return new ValueHolder<>(Constants.NOT_DEFINED, null, 0d);
	    
	    return new ValueHolder<>(String.valueOf(modelloAsset.getIdCluster()), null, modelloAsset.getN1());
	}
}
