package it.terna.nas.service.exposure.ws.maximo;

import com.fasterxml.jackson.annotation.JsonProperty;

import it.terna.kernel.basement.utility.Assorted;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class X4InterfstatResponse extends MaximoResponse{

	private static final long serialVersionUID = 2938086939715533325L;


	private int idinterferenza;

	@JsonProperty("status_description")
	private String statusDescription;

	@JsonProperty("idinterferenza_ext")
	private String idinterferenzaExt;

	private String description;

	@JsonProperty("_rowstamp")
	private String rowstamp;

	@JsonProperty("primarilevazione")
	private String primaRilevazione;

	private boolean hasld;

	private boolean nonconfermato;

	private String assetnum;

	private String siteid;

	private String href;

	@JsonProperty("x4interfstatid")
	private int x4InterfstatId;

	private String status;
	@JsonProperty("Error")
	private ErrorData errorData;



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
