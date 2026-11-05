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
	MAXIMO_X4OPNTK					("X4OPNTK",		"HTTP call to get/save X4NTKOP on Maximo Database", 	"Business Object Script"),
	MAXIMO_X4LABTRANS				("X4LABTRANS",		"HTTP call to get/save X4LABTRANS on Maximo Database", 	"Business Object Script"),
	MAXIMO_X4NTK					("X4NTK",		"HTTP call to get/save X4NTK on Maximo Database", 	"Business Object Script"),
	MAXIMO_X4ORDINT					("X4ORDINT",		"HTTP call to get/save X4ORDINT on Maximo Database", 	"Business Object Script"),
	MAXIMO_X4ODM					("X4ODM",		"HTTP call to get/save X4ODM on Maximo Database", 	"Business Object Script"),
	MAXIMO_X4ODMOP					("X4ODMOP",		"HTTP call to get/save X4ODM on Maximo Database", 	"Business Object Script"),
	MAXIMO_X4WBE					("X4WBE",		"HTTP call to get/save X4WBE on Maximo Database", 	"Business Object Script"),
	MAXIMO_X4APIWOLABTRANS			("X4APIWOLABTRANS",  		"HTTP call to get/save X4APIWOLABTRANS on Maximo Database", 	"Business Object Script"),
	MAXIMO_X4INTODLGEOCALL			("X4INTODLGEOCALL",  		"HTTP call to get/save X4INTODLGEOCALL on Maximo Database", 	"Business Object Script"),
	MAXIMO_X4APIWOASS				("X4APIWOASS", 				"HTTP call to get/save X4APIWOASS on Maximo Database", 			"Business Object Script"),
	MAXIMO_POST_X4ESITOINT			("saveX4Esito", 			"HTTP call to save X4ESITOINT on Maximo Database", 				"Business Object Script"),
	MAXIMO_X4WORKORDER				("X4WORKORDER",  			"HTTP call to get/save X4WORKORDER on Maximo Database", 		"Business Object Script"),
	OAUTH2							("callOAuth2", 				"HTTP call to fetch OAuth2", 									"da cancellare---"),
	AGILE_ADD_RICHIESTA_INDISPONIBILITA		("agileAddInvioOdlApim",		"HTTP POST to call Agile  SOAP for send unavailability request",""),
	AGILE_UPDATE_RICHIESTA_INDISPONIBILITA	("agileUpdateInvioOdlApim",		"HTTP POST to call Agile  SOAP for send unavailability request",""),
	GEOCALL_INVIO_TIPO_IMPIANTO		("sendTypeAsset",			"HTTP POST to call Geocall SOAP for send type asset", 			""),
	GEOCALL_ANNULLA_BLOCCO			("cancelOrBlock",			"HTTP POST to call Geocall SOAP for cancel or block", 			""),
	GEOCALL_ANNULLA_OPERAZIONE		("cancelOperation",			"HTTP POST to call Geocall SOAP for cancel operation", 			""),
	GEOCALL_STORNO					("sendSapStornoGeocall",	"HTTP POST to call Geocall SOAP for storno results", 			""),
	GEOCALL_MODIFICA_IMPIANTO		("modifyAsset", 			"HTTP POST to call Geocall SOAP for modify asset", 				""),
	GEOCALL_INVIO_ODL				("sendOdL",					"HTTP POST to call Geocall SOAP for send OdL", 					""),
	GEOCALL_INVIO_STORNO			("sendStorno",				"HTTP POST to call Geocall SOAP for send Storno", 				""),
	CAVI_INVIO_ODL				    ("sendOdlToCavi",			"HTTP POST to send OdL to CAVI",			 					""),
	CAVI_DELETE_ODL				    ("sendOdLDeleteToCavi",		"HTTP POST to send delete OdL to CAVI",			 				""),
	ELIVE_INVIO_ODL_APIM			("eliveInvioOdlApim",		"HTTP POST to call ELIVE POST for invio odl", 		    		""),
	MAXIMO_X4INDISPWO				("saveX4IndispWO",			"HTTP call to save X4INDISPWO on Maximo Database",				"Business Object Script"),
	MAXIMO_POST_X4DIFFORSTAT		("saveX4Difforstat", 		"HTTP call to save X4DIFFORSTAT on Maximo Database", 			"Business Object Script"),
	MAXIMO_POST_X4INTERFSTAT		("saveX4Interfstat", 		"HTTP call to save X4INTERFSTAT on Maximo Database", 			"Business Object Script"),
	ACQUISIZIONE_OI					("acquisitionOISAP", 			"HTTP POST to call acquisition OI from SAP", 			""),
	ACQUISIZIONE_ODM				("acquisitionOdMSAP", 			"HTTP POST to call acquisition OdM from SAP",			""),
	ACQUISIZIONE_NTK				("acquisitionNtkSAP", 			"HTTP POST to call acquisition Ntw from SAP",			""),
	ACQUISIZIONE_DC					("acquisitionCountableDataSAP", "HTTP POST to call SAP SOAP for acquisition countable data", ""),
	SAP_STORNO						("sapStorno", 			"HTTP POST to call storno to SAP",			""),
	SAP_COUNTERODM					("sapCounterOdM", 			"HTTP POST to call counter OdM to SAP",			""),
	SAP_CHECK_PERIODO				("sapCheckPeriodo", 			"HTTP POST to call check periodo contabile on SAP",			""),
	SAP_CONSUNTIVAZIONE				("sapConsuntivazione", 			"HTTP POST to call consuntivazione to SAP",			""),
	SAP_CLOSE_ODM					("closureOdMSAP",					"HTTP POST to close OdM to SAP", 							""),
	SAP_CLOSE_NTK					("closureNtkSAP",					"HTTP POST to close Ntw to SAP", 							""),
	X4NOTIFYSCHEDULEODL				("X4NOTIFYSCHEDULEODL",  	"HTTP call to launch X4NOTIFYSCHEDULEODL script on Maximo Database", "Automation Script")
	;






	private String	entityKey;
	private String	description;
	private String	scriptName;
}