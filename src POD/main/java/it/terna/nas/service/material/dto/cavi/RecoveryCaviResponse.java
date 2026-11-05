package it.terna.nas.service.material.dto.cavi;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureResponse;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Getter
@Setter
@Slf4j
@ToString
public class RecoveryCaviResponse extends ExposureResponse {
	
	private static final long serialVersionUID = 1L;
	
	private String esito;
	private String message;
	private String codiceErrore;
	private String descrizioneErrore;
	
	@Override
	public String toJson()
	{
		try
		{
			return Assorted.toJson(this);
		}
		catch (Exception ex)
		{
			//log occurred error
			log.error("- Base WS Data - Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}

}
