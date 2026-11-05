package it.terna.nas.service.material.dto.cavi;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import it.terna.kernel.basement.utility.Assorted;
import it.terna.kernel.service.exposure.ExposureResponse;
import it.terna.nas.service.material.utility.EDataIntegrationEntity;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class InvioOdlDeletionToCaviResponse extends ExposureResponse {
	
	private static final long serialVersionUID = -5887807973382721846L;
	
	private String esito;
	
	private String codiceErrore;
	
	private String messaggioErrore;
	
	private String wonum;
	
	// this variables are not related to the API interface agreement but they are used for our internal logic only
	private EDataIntegrationEntity					integrationEntity;
		
	private String									errorCode;
		
	private InvioOdlDeletionToCaviRequest			request;
	
	public String toJson ()
	{
		try
		{
			return Assorted.toJson(this);
		}
		catch (Exception ex)
		{
			// log occurred error
			log.error("Unable to build [{}] JSON representation. An invalid object representation will be returned. Below the exception thrown \n", 
					this.getClass().getCanonicalName(), ex);
			// return empty JSON representation
			return null;
		}
	}
	
}
