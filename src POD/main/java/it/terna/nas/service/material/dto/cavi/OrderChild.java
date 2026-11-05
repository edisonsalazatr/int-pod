package it.terna.nas.service.material.dto.cavi;

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
public class OrderChild {
	
	@SerializedName("singleOrder")
	@JsonProperty("singleOrder")
	private String singoloOrdine;
	private String trattaCavo;
	
	public OrderChild(String wonum, String assetNum) {
		this.singoloOrdine = wonum;
		this.trattaCavo = assetNum;
	}


}
