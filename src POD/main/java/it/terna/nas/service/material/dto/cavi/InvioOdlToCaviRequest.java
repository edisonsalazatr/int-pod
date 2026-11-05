package it.terna.nas.service.material.dto.cavi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;

import it.terna.kernel.basement.utility.Assorted;

import java.io.Serializable;
import java.util.List;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InvioOdlToCaviRequest implements Serializable
{
	private static final long serialVersionUID = -5887807973382721846L;
	
	/**
	 * codice ordine di lavoro
	 */
	private String wonum;
	private String date;
	private String lineName;
	private String lineCode;
	private String trunkCode;
	private String aotCode;
	private String uiCode;	
	private String planType;
	private String codiceAttivita;
	
	@SerializedName("orderList")
	@JsonProperty("orderList")
	private List<OrderChild> listaOrdini;
	
	public String toJson()
	{
		try
		{
			return Assorted.toJson(this);
		}
		catch (Exception ex)
		{
			//log occurred error
			log.error("Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", 
					this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}


}

