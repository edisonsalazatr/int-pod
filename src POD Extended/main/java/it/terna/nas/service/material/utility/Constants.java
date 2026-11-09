package it.terna.nas.service.material.utility;

import java.util.Set;

public interface Constants
{
	public static final String	API_KEY						= "apikey";
	public static final String	AUTHORIZATION				= "Authorization";
	public static final String	BEARER						= "Bearer ";
	public static final String	DATE_FORMAT					= "yyyyMMdd HH:mm:ss";
	public static final String	DATE_WITHOUT_TIME_FORMAT	= "yyyyMMdd";
	public static final String	LOCAL_DATE_FORMAT			= "dd/MM/yyyy";
	public static final String  DATE_SQL_FORMAT				= "yyyy-MM-dd";
	public static final String	PARAM_LEAN					= "lean";
	public static final String	PARAM_VALUE_LEAN			= "1";
	public static final String	POD_NAME					= "podname";
	public static final String	SCRIPT_NAME					= "scriptName";
	public static final String	SOAPACTION					= "SOAPAction";
	public static final String	TIME_FORMAT					= "HH:mm";
	public static final String	TIME_FORMAT_2				= "HH:mm:ss";
	public static final String  ERROR_SEPARATOR             = " | ";
	public static final String 	EAM							= "EAM";
	public static final String  ESITO_DATE_FORMAT           = "yyyy-MM-dd'T'HH:mm:ssXXX";
	public static final String  ESITO_DATE_FORMAT_2 		= "yyyy-MM-dd HH:mm:ss";
	public static final String  X_METHOD_OVERRIDE			= "x-method-override";
	public static final String  PATCHTYPE					= "patchtype";
	public static final String 	PATCH						= "PATCH";
	public static final String  MERGE						= "MERGE";
	public static final String  DELETE						= "DELETE";
	public static final String  ITALY_TIMEZONE				= "Europe/Paris";
	public static final String  UTC_TIMEZONE				= "UTC";
	public static final String 	OSLC_WHERE					= "oslc.where";
	public static final String	OSLC_SELECT					= "oslc.select";
	public static final String 	OSLC						= "oslc";
	public static final String	API							= "api";
	public static final String  BULK   					    = "BULK";
	public static final String	OSLC_PAGE_SIZE				= "oslc.pageSize";
	public static final String	OSLC_PAGING					= "oslc.paging";
	public static final String  WILDCARD 					= "*";
	public static final String	PAGE_SIZE					= "200";
	public static final String  CONTENT_TYPE				= "Content-Type";
	public static final String  APPLICATION_JSON			= "application/json";
	public static final String  ACCEPT						= "accept";
	public static final String  PROPERTIES					= "properties";
	public static final String  ALL_OR_NOTHING				= "allornothing";
	public static final String  ALL_OR_NOTHING_VALUE		= "1";
	public static final String	AND							= " AND ";
	public static final String  EQUAL						= "=";
	public static final String  COMMA_SEPARATOR 			= ",";
	public static final String  LINEA 						= "L";
	public static final String  STAZIONE 					= "S";
	public static final String  SELECT_ALL					= "*";
	public static final String  BLANK_STRING 				= ""; 
	public static final String  COLONS						= ":";
	public static final String  NOT_SUPPORTED_URI			= "NOT_SUPPORTED";
	public static final String  FORMAT_DATE_YYYYMMDD        = "yyyy-MM-dd";
	public static final String 	UUID_RESEND_PARAM			= "UUID_RESEND";
	public static final String  API_URI						= "/api/";
	public static final String	DOUBLE_QUOTES				= "\"";
	public static final String	HASH						= "#";
	public static final String  PIPE_REGEX				    = "^\\|+|\\|+$";
	public static final String  PIPE_CHARACTER				= "\\|";
	public static final String  CACHE_CLASSIFICATION		= "CACHE_CLASSIFICATION";
	public static final String  CLASSIFICATION_LEVEL		= "CLASSIFICATION_LEVEL";
	public static final String  CLASSIFICATION_MAPPING		= "CLASSIFICATION_MAPPING";
    public static final String  MAXIMO_DATE_AND_TIME        = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String  UPDATE                      = "Update";

    //SERVICENOW
    public static final String   SERVICENOW 						  = "SERVICENOW";
	public static final int 	 CSV_BUFFER_SIZE					  = 500000;
	public static final char     CSV_SEPARATOR_SNOW 				  = '#';
	public static final String   PKG_X4SNOW_FILEPATH		  		  = "/dumps/PKG_X4SNOW/";
	public static final String   CSV_EXTENSION 						  = ".csv";
	public static final String 	 USER_DIR 							  = "user.dir";
	public static final String[] HEADERS_ANAGRAFICA_MASSIVA			  =	{"assetnum", "changeDate", "gewrk", "ingrp", "installDate", 
																		 "sequence1", "sequence2", "status", "statusDate", "swerk", 
																		 "swerkDescription", "classificationId", "rootSapId1", "rootSapId2", 
																		 "description", "costruttoreComponente", "costruttoreComponenteDescr", 
																		 "funzioneElemento", "riferimentoFase", "tipoComponente", "sapId", 
																		 "sapId2", "codicePadre", "codiceRadice", "codicePadre2", "codiceRadice2", 
																		 "LIN_LUNGHEZZA", "LIN_CONDUTTORE_AEREO", "LIN_ANNO_INSTALLAZIONE", 
																		 "LIN_RIF_FUNE", "LIN_TIPO_FUNE_GUARDIA", "LIN_COSTRUTTORE_GIUNTO", 
																		 "LIN_TIPO_GIUNTO", "LIN_COSTRUTTORE_TERMINALE", "LIN_TIPO_TERMINALE", 
																		 "LIN_RIF_ASSET_ARM_COND", "LIN_TIPO_ISOLATORI_LIN", "LIN_NUM_CATENE", 
																		 "LIN_TIPO_ISOLATORI_DER", "LIN_RIF_ASSET_ARM_FDG", "LIN_TIPO_ISOLATORI", 
																		 "LIN_TIPO_PALIFICAZIONE", "LIN_TIPO_SOSTEGNO", "LIN_STRUTTURA_SOSTEGNO", 
																		 "LIN_ANNO_COSTRUZIONE", "LIN_COSTRUTTORE_CAVO", "LIN_CONDUTTORE_CAVO", 
																		 "LIN_ISOLAMENTO_CAVO", "LIN_TIPO_CONDUTTORE_CAVO", 
																		 "LIN_SEZIONE_CONDUTTORE_CAVO", "LIN_RIF_FASE", "LIN_RIF_POSIZIONE", 
																		 "COORDINATE_LONG", "COORDINATE_LAT", "LIN_ELEMENTO_INDISP", 
																		 "STZ_DATA_COSTRUZIONE", "STZ_TENSIONE_NOMINALE", "STZ_FUNZIONAMENTO_COMANDI", 
																		 "STZ_TIPO_COMPARTO", "STZ_POTENZA_NOMINALE", "STZ_ELEMENTO_INDISP"};
	public static final String		MSG_004							  = "Parent not valorized for asset %s. Incorrect classstructure %s";
	public static final String		MSG_005							  = "Asset %s doesn't have a valid classstructureId because it's null";
	public static final String HEADER_X_API_KEY					      = "x-api-key";
	public static final String HEADER_HOST					  		  = "Host";
	public static final String CLIENT_ID_CLAIM						  = "appid";
    public static final String HINT_FETCH_SIZE                        = "org.hibernate.fetchSize";
    public static final String HINT_FETCH_SIZE_VALUE                  = "1000";
    public static final String X4SNOWASSET_NOT_FOUND_MESSAGE		  = "Asset %s non trovato su vista dedicata X4SNOW.";
    public static final String X4SNOWASSET_SAPID_NOT_FOUND_MESSAGE	  = "Asset %s non presenta SAPID.";
    public static final String NOTI_SNOW							  = "Y";
    public static final String DA_ELABORARE_YES					      = "Y";
    public static final String DA_ELABORARE_NO					      = "N";
    public static final Set<String> SNOW_ATTRIBUTES = Set.of("LIN_LUNGHEZZA", "LIN_CONDUTTORE_AEREO", "LIN_ANNO_INSTALLAZIONE", 
													         "LIN_RIF_FUNE", "LIN_TIPO_FUNE_GUARDIA", "LIN_COSTRUTTORE_GIUNTO", 
													    	 "LIN_TIPO_GIUNTO", "LIN_COSTRUTTORE_TERMINALE", "LIN_TIPO_TERMINALE",
													    	 "LIN_RIF_ASSET_ARM_COND", "LIN_TIPO_ISOLATORI_LIN", "LIN_NUM_CATENE", 
													    	 "LIN_TIPO_ISOLATORI_DER", "LIN_RIF_ASSET_ARM_FDG", "LIN_TIPO_ISOLATORI",
													    	 "LIN_TIPO_PALIFICAZIONE", "LIN_TIPO_SOSTEGNO", "LIN_STRUTTURA_SOSTEGNO",
													    	 "LIN_ANNO_COSTRUZIONE", "LIN_COSTRUTTORE_CAVO", "LIN_CONDUTTORE_CAVO",
													    	 "LIN_ISOLAMENTO_CAVO", "LIN_TIPO_CONDUTTORE_CAVO", "LIN_SEZIONE_CONDUTTORE_CAVO",
													    	 "LIN_RIF_FASE", "LIN_RIF_POSIZIONE", "LIN_ELEMENTO_INDISP",
													    	 "STZ_DATA_COSTRUZIONE", "STZ_TENSIONE_NOMINALE", "STZ_FUNZIONAMENTO_COMANDI",
													    	 "STZ_TIPO_COMPARTO", "STZ_POTENZA_NOMINALE", "STZ_ELEMENTO_INDISP");
}
