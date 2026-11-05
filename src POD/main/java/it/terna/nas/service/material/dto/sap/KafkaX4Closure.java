package it.terna.nas.service.material.dto.sap;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.terna.kernel.basement.utility.Assorted;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(Include.NON_NULL)
public class KafkaX4Closure implements Serializable{

	private static final long serialVersionUID = 5657838766229711436L;
	
	//OdM Closure
	@JsonProperty("x4odm")
	private String x4odm;
	
	//NTK Closure
	private String wonum;
	private String x4ntk;
	private String x4noperazione;
	
    public String toJson()
	{
		try {
			return Assorted.toJson(this);
		} catch (Exception ex) {
			// log occurred error
			log.error("- KafkaX4Closure - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}
}
