package it.terna.nas.service.material.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumeration used to map HTTP Call entity keys
 * 
 * @author Michele Russo
 */
@AllArgsConstructor
@Getter
public enum EDataIntegrationEntity 
{
	// filter API
	MAXIMO_POST_X4ESITOINTSNOW		("saveX4EsitoSnow", 			"HTTP call to save X4ESITOINT on Maximo Database", 				"Business Object Script"),
	OAUTH2							("callOAuth2", 				"HTTP call to fetch OAuth2", 									""),
	SERVICENOW_MODIFICA_IMPIANTO	("modifyAsset", 			"HTTP POST to call ServiceNow REST for modify asset", 			"");

	private String	entityKey;
	private String	description;
	private String	scriptName;
}