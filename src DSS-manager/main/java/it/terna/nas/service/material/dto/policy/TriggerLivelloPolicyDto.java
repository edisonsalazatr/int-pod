package it.terna.nas.service.material.dto.policy;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TriggerLivelloPolicyDto implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("x4_lvl_trigger_periodicheid")
    @JsonProperty("x4_lvl_trigger_periodicheid")
	private String id;
	
	private Boolean attivo;
	private Integer livello;
}
