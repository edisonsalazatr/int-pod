package it.terna.nas.service.material.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import it.terna.kernel.basement.utility.Assorted;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KafkaOdlToAgileRequest implements Serializable{
	
	private static final long serialVersionUID = 7678544795271246719L;
	
	@JsonProperty("wonum")
	private String wonum;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("assetnum")
    private String assetnum; // Necessario per il recupero di “Unità Impianti responsabile dell’attività” dalla tabella ASSET
    
    @JsonProperty("siteid")
    private String siteid;   // Necessario per il recupero di “Unità Impianti responsabile dell’attività” dalla tabella ASSET
    
    @JsonProperty("jpnum")
    private String jpnum;
    
//    @JsonProperty("tipologia_attivita")
//    private String tipologiaAttivita;
    
    @JsonProperty("targstartdate")
    private String targstartdate;
    
    @JsonProperty("targcompdate")
    private String targcompdate;
    
    @JsonProperty("schedstart")
    private String schedstart;
    
    @JsonProperty("schedfinish")
    private String schedfinish;    
    
    @JsonProperty("x4wbs")
    private String x4wbs;
    
    @JsonProperty("x4tipospesa")
    private String x4tipospesa;
    
    @JsonProperty("ownergroup")
    private String ownerGroup; // Nuova Responsabile uità impianti relativo all'OdL
    
    @JsonProperty("pluscjprevnum")
    private Integer pluscjprevnum; // Necessario per il recupero di “Richiesta Indisponibilità” dalla tabella WORKORDER
    
    public String toJson()
	{
		try {
			return Assorted.toJson(this);
		} catch (Exception ex) {
			// log occurred error
			log.error("- KafkaOdlToAgileRequest - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}

}
