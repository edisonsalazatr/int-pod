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
	MAXIMO_GET_X3ESITOINT_SCRIPT	("retrieveX3Esito", 		"HTTP call to fetch X3ESITOINT Business Object", 			"Business Object Script");


	
	private String	entityKey;
	private String	description;
	private String	scriptName;
}