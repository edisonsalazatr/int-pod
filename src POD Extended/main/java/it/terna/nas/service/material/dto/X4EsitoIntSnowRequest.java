package it.terna.nas.service.material.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;

import it.terna.kernel.basement.utility.Assorted;

import java.io.Serializable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;


@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
@Slf4j
public class X4EsitoIntSnowRequest implements Serializable
{
	private static final long serialVersionUID = -5887807973382721846L;
	
	private String  start_elab;
	private String  esito;
	private String  idinterfaccia;
	@SerializedName("cod_esito")
	@JsonProperty("cod_esito")
	private String 	codesito;
	private String  desc_esito;
	private String  uniqueid;
	private String  end_elab;
	@SerializedName("kafka_topic")
	@JsonProperty("kafka_topic")
	private String  kafkatopic;
	private String 	message;
	private String  assetnum;
	private Long    parent;
	private Long    retry;
	
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
