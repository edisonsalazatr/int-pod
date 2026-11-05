package it.terna.nas.service.material.dto.difformita;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DifformitaStatDTO implements Serializable
{
	private static final long	serialVersionUID	= -5887807973382721846L;

	private Long				x4difforstatid;
	private String				description;
	private String				assetnum;
	private String				siteid;
	private Long				iddifformita;
	private String				status;
	private String				ultimarilevazione;
	private String				primarilevazione;
	private String				gravita;
	private Boolean				confermato;
	private Boolean				nonconfermato;
	private String				odlrisolutore;

	@SerializedName("iddifformita_ext")
	@JsonProperty("iddifformita_ext")
	private String				iddifformitaExt;
	private String				causale;
	private String				fase;
	private String				changeby;
	private String				idmisura;
	private Double				valmisura;

	@SerializedName("_action")
	@JsonProperty("_action")
	private String				action;
}
