package it.terna.nas.service.exposure.ws.maximo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;

import it.terna.kernel.basement.utility.Assorted;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Data
@Slf4j
public class LabTransItem implements Serializable
{	
	private static final long serialVersionUID = -1144638071748336908L;
	private String startdatetime;
	private String finishdatetime;
	private Double regularhrs;
	private Double x4oretrasf;
	private Double x4oreuff;
	private String laborcode;
	private String externalrefid;
	private String siteid;
	private String refwo;

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
