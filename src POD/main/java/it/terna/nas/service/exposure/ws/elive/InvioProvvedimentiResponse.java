package it.terna.nas.service.exposure.ws.elive;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Getter
@Setter
@Slf4j
@ToString
public class InvioProvvedimentiResponse extends ExposureResponse {
	
	private static final long serialVersionUID = -2667179787108420337L;
	
	private String esito;
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
