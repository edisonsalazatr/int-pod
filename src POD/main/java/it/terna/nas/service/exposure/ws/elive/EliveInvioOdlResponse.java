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
@ToString
@Slf4j
public class EliveInvioOdlResponse extends ExposureResponse{

	private static final long serialVersionUID = -4010442977887412622L;
	
	private String esito;
	private String codiceErrore;
	private String descrizioneErrore;
	
	@Override
	public String toJson() {
		try
		{
			return Assorted.toJson(this);
		}
		catch (Exception ex)
		{
			//log occurred error
			log.error("Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}
}
