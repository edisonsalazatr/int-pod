package it.terna.nas.service.exposure.events;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * @author marco.degidio
 *
 */
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class ExceptionChangedEvent extends ExposureRequest {
	
	private static final long serialVersionUID = 7932568650316591976L;
	
	private String x4tabeccezioniid;
	private String description; 
	
	@SerializedName("statoidoneo")
	@JsonProperty("istatoidoneod")
	private String statoIdoneo;
	
	@SerializedName("tipologiaasset")
	@JsonProperty("tipologiaasset")
	private String tipologiaAsset;
	
	@SerializedName("idattivita_ext")
	@JsonProperty("idattivita_ext")
	private String idAttivitaExt;
	
	@SerializedName("idattivita")
	@JsonProperty("idattivita")
	private String idAttivita;
	
	private String rowstamp; 


	@Override
	public String toJson()
	{
		try {
			return Assorted.toJson(this);
		} catch (Exception ex) {
			// log occurred error
			log.error("- Base WS Data - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}
}
