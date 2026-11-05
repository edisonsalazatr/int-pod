package it.terna.nas.service.exposure.ws.elive;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
@ToString
@Slf4j
public class EliveInvioOdlRequest extends ExposureRequest{
	

	private static final long serialVersionUID = 3100609668903619321L;
	
	private String codiceOdl;
	private String codiceAttivita;
	private String codiceCampata;
	private String dataPianificazione;
	private String unitaImpianto;
	
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
