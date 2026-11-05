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
	MAXIMO_GET_X3ESITOINT_SCRIPT	("retrieveX3Esito", 		"HTTP call to fetch X3ESITOINT Business Object", 			"Business Object Script"),
	MAXIMO_GET_X3WORKORDER_SCRIPT	("retrieveWorkOrder", 		"HTTP call to fetch X3WORKORDER Business Object", 			"Business Object Script"),
	MAXIMO_X4APIWOLABTRANS			("X4APIWOLABTRANS",  		"HTTP call to get/save X4APIWOLABTRANS on Maximo Database", "Business Object Script"),
	MAXIMO_X4INTODLGEOCALL			("X4INTODLGEOCALL",  		"HTTP call to get/save X4INTODLGEOCALL on Maximo Database", "Business Object Script"),
	MAXIMO_POST_X3WORKORDER_SCRIPT	("saveX3Esito", 			"HTTP call to save X3ESITOINT on Maximo Database", 			"Business Object Script"),
	MAXIMO_X4APIWOASS				("X4APIWOASS", 				"HTTP call to get/save X4APIWOASS on Maximo Database", 		"Business Object Script"),
	MAXIMO_POST_X4ESITOINT			("saveX4Esito", 			"HTTP call to save X4ESITOINT on Maximo Database", 			"Business Object Script"),
	MAXIMO_MXAPIWO					("MXAPIWO", 			    "HTTP call to save MXAPIWO on Maximo Database", 			"Business Object Script"),
	MAXIMO_X4WOACTIVITY				("X4WOACTIVITY", 			"HTTP call to save X4WOACTIVITY on Maximo Database", 		"Business Object Script"),
	OAUTH2							("callOAuth2", 				"HTTP call to fetch OAuth2", 								""),
	GEOCALL_INVIO_TIPO_IMPIANTO		("sendTypeAsset",			"HTTP POST to call Geocall SOAP for send type asset", 		""),
	GEOCALL_ANNULLA_BLOCCO			("cancelOrBlock",			"HTTP POST to call Geocall SOAP for cancel or block", 		""),
	GEOCALL_ANNULLA_OPERAZIONE		("cancelOperation",			"HTTP POST to call Geocall SOAP for cancel operation", 		""),

	GEOCALL_INVIO_ODL				("sendOdL",					"HTTP POST to call Geocall SOAP for send OdL", 				""),
	CAVI_INVIO_ODL				    ("sendOdlToCavi",			"HTTP POST to send OdL to CAVI",			 				""),
	CAVI_DELETE_ODL				    ("sendOdLDeleteToCavi",		"HTTP POST to send delete OdL to CAVI",			 			""),

	ELIVE_INVIO_ODL					("eliveInvioOdl",			"HTTP POST to call ELIVE POST for invio odl", 		        ""),
	MAXIMO_POST_X4DIFFORSTAT		("saveX4Difforstat", 		"HTTP call to save X4ESITOINT on Maximo Database", 			"Business Object Script");
	
	private String	entityKey;
	private String	description;
	private String	scriptName;
}