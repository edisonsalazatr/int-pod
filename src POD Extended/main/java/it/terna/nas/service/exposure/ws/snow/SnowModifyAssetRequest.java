package it.terna.nas.service.exposure.ws.snow;

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
public class SnowModifyAssetRequest extends ExposureRequest{
	
	private static final long serialVersionUID = 3100609668903619321L;
	
	private String assetNum;
	private String changeDate;
	private String gewrk;
	private String ingrp;
	private String installDate;
	private String sequence1;
	private String sequence2;
	private String status;
	private String statusDate;
	private String swerk;
	private String swerkDescription;
	private String classificationId;
	private String rootSapId1;
	private String rootSapId2;
	private String description;
	private String costruttoreComponente;
	private String costruttoreComponenteDescr;
	private String funzioneElemento;
	private String riferimentoFase;
	private String tipoComponente;
	private String sapId;
	private String sapId2;
	private String codicePadre;
	private String codiceRadice;
	private String codicePadre2;
	private String codiceRadice2;
	private List<SnowAttribute> attributes;

	
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
