package it.terna.nas.service.material.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import it.terna.kernel.basement.utility.Assorted;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
@Slf4j
public class KafkaOdlToEliveRequest implements Serializable{
	
	private static final long serialVersionUID = 7678544795271246719L;
	
	private String wonum;
	private String status;
	private String jpnum;
	private String targstartdate;
	private Long assetnum;
	private List<AssetKafka> asset;
	private List<AssetLocKafka> assetlocrelation;
	
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AssetKafka {
        private String x3sapid;
    }
    
    @Data
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AssetLocKafka {
        private String x3sapid;
    }
    
    public String toJson()
	{
		try {
			return Assorted.toJson(this);
		} catch (Exception ex) {
			// log occurred error
			log.error("- KafkaOdlToEliveRequest - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}
	

}
