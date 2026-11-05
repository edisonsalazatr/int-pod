package it.terna.nas.service.exposure.ws.agile;

import java.util.List;

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
public class AgileInvioOdlRequest extends ExposureRequest{

	private static final long serialVersionUID = -5286135243451523851L;
	
	private String codiceOdL;
	private String descrizioneODL;
	private String statoOdL; //HLD V20
	private String titolareUnitaImpianti;
	private String pianoLavoro;
	private boolean richiestaIndisponibilita;
	private String tipologiaAttivita;
	private List<String> listaOdlFigli;
	private long dataInizioPianificata;
	private long dataFinePianificata;
	private String sedeTecnicaIndispPadre;
	private String codiceSedeTecnicaIndisp;
	private List<String> sediTecnicheInterferenti;
	private String wbsCbs;
	private String tipoSpesa;
	
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
