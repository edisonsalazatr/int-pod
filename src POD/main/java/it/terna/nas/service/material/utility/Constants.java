package it.terna.nas.service.material.utility;

import java.util.Arrays;
import java.util.List;

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
	public static final String 	EAM_SAP						= "EAM_SAP";
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
	public static final String  UFFICIO						= "U";
	public static final String  FOTOVOLTAICO				= "I";
	public static final String  SELECT_ALL					= "*";
	public static final String  BLANK_STRING 				= ""; 
	public static final String  COLONS						= ":";
	public static final String  NOT_SUPPORTED_URI			= "NOT_SUPPORTED";
	public static final String  FORMAT_DATE_YYYYMMDD        = "yyyy-MM-dd";
	public static final String 	UUID_RESEND_PARAM			= "UUID_RESEND";
	public static final String 	OPERATION_PARAM				= "OPERATION";
	public static final String 	OSLC_URI_REGEX 				= "(?i)/oslc/";
	public static final String  API_URI						= "/api/";
	public static final String	DOUBLE_QUOTES				= "\"";
	public static final String  PIPE_REGEX				    = "^\\|+|\\|+$";
	public static final String  PIPE_CHARACTER				= "\\|";
	public static final String  MAXIMO_FUTURE_DATES			= "BMXAA4589E";
	public static final String  MAXIMO_UNIQUE_CONSTRAINT	= "BMXAA4211E";
	public static final String  MAXIMO_SAME_USER_CONSTRAINT	= "BMXAA8229W";

	public static final String  CACHE_CLASSIFICATION		= "CACHE_CLASSIFICATION";
	public static final String  CLASSIFICATION_LEVEL		= "CLASSIFICATION_LEVEL";
	public static final String  CLASSIFICATION_MAPPING		= "CLASSIFICATION_MAPPING";
	public static final String  CACHE_BEARER_TOKEN			= "CACHE_BEARER_TOKEN";
	public static final String  BEARER_TOKEN_URIS			= "BEARER_TOKEN_URIS";
    public static final String  MAXIMO_DATE_AND_TIME        = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String  UPDATE                      = "Update";
    
    public static final double 	ELIVE_DIST_INT_VALORE 		= 1_000_000_000D;
    public static final String 	ELIVE_PRIORITA_VALORE 		= "NA";
	public static final String 	ELIVE_FLUSSO_SPERIMENTALE 	= "SPERIMENTALE";
    


	// AGILE
	public static final String	ESITO_OK								= "OK";
	public static final String	ESITO_KO								= "KO";
	public static final String	AGILE									= "AGILE";

	
	// CAVI INTERRATI
	public static final String  CAVI_INTERRATI 				     	  = "CAVI_INTERRATI";

	
	//GEOCALL
	public static final Long 	 TIPO_ANAGRAFICA_CLIENTE_PRIMO_LIVELLO = 3L;
	public static final Long	 TIPO_ANAGRAFICA_CLIENTE			  = 2L;
	public static final String   GEOCALL 				     		  = "GEOCALL";
	public static final String   SISNET								  = "SISNET";
	public static final int 	 CSV_BUFFER_SIZE					  = 500000;
	public static final char     CSV_SEPARATOR 						  = ';';
	public static final String   PKG_X4GEOCALLADDRESS_FILEPATH		  = "/dumps/PKG_X4GEOCALLADDRESS/";
	public static final String   CSV_EXTENSION 						  = ".csv";
	public static final String 	 USER_DIR 							  = "user.dir";
	public static final String[] HEADERS_ANAGRAFICA_MASSIVA			  =	{"tipoOggetto", "statoOggetto", "codiceOggetto", 
																		 "SAPID", "SAPID2", "descrizione", "codicePadre", 
																		 "codiceRadice", "codicePadre2", "codiceRadice2",
																		 "areaLavorazione", "centroDiLavoro", "filiale", 
																		 "toponimo", "via",	"civico", "localita", "provincia", 
																		 "cap", "comune",	"istat", "latitudine1",	
																		 "longitudine1", "latitudine2",	"longitudine2"};
	public static final String   ASSET_MODIFY_ENTITY				  = "sendAsset";
	public static final List<String> CHIUSO_GEOCALL					  = Arrays.asList(Catalog.EAMStatusMapping.CHIUSO.getGeoCallStatus(), Catalog.EAMStatusMapping.CHIUSOTEC.getGeoCallStatus());
	public static final List<String> PRIMO_LIVELLO					  = Arrays.asList("STAZIONI (F)", "LINEE (F)", "UFFICIO (F)", "IMPIANTO FOTOVOLTAICO (F)");  
	
	public static final String		MSG_001							= "No data on X3SAPASSET or X3SAPTOMAXOBJECT for sapCode %s.";
	public static final String		MSG_002							= "No ASSET data find, check table X3SAPASSET or X3SAPTOMAXOBJECT for sapCode %s.";
	public static final String		MSG_003							= "Generic error for sapCode %s. Error %s";
	public static final String		MSG_004							= "Parent not valorized for asset %s. Incorrect classstructure %s";
	public static final String		MSG_005							= "Asset %s doesn't have a valid classstructureId because it's null";
	public static final String 		SAPID_NOT_FOUND_MESSAGE 		= "Asset %s non presenta SAPID.";
	public static final String		INVIO_ODL						= "INVIO_ODL";
	public static final String		ANNULLA_BLOCCO_ODL				= "ANNULLA_BLOCCO_ODL";

	//ELIVE
	public static final String  ELIVE 				     		  	  = "ELIVE";
	public static final String  REPORT_TAGLIO_PIANTE_GRIGLIA              = "Report_per_taglio_piante_griglia";
	public static final String  APERTA								  = "APERTA";
	public static final String  URGENTI								  = "URGENTI";
	public static final String  SIT01								  = "SIT01";
	public static final String  VALIDATA							  = "VALIDATA";
	public static final String  DEFINIZIONE_AREE_BOSCHIVE			  = "TRG.AREA.BOSCH";
	public static final String  DIFFERIBILI							  = "DIFFERIBILI";
	public static final String  DENSITA_BASSA                             = "B";
	public static final String  DENSITA_MEDIA                             = "M";
	public static final String  DENSITA_ALTA                              = "A";
	public static final Integer TIPOLOGIA_INTERFERENZA_ELIVE              = 1;
	
	public static final String  HEADER_X_API_KEY					  = "x-api-key";
	public static final String  HEADER_HOST					  		  = "Host";
	public static final String 	CLIENT_ID_CLAIM						  = "appid";
    public static final String HINT_FETCH_SIZE                        = "org.hibernate.fetchSize";
    public static final String HINT_FETCH_SIZE_VALUE                  = "1000";
	
	//SAP
	public static final String SAP = "SAP";
	public static final String IDODM = "IDODM";
	public static final String IDOI = "IDOI";
	public static final String IDNTK = "IDNTK";
	public static final String X4ODMID = "X4ODMID";
	public static final String IDWBE = "IDWBE";
	public static final String NOPERAZIONE = "NOPERAZIONE";
	public static final String IDOPERAZIONE = "IDOPERAZIONE";
	public static final String PARZIALE = "P";
	public static final String TOTALE = "T";
	public static final String REFWO = "REFWO";
	public static final String WONUM = "WONUM";
	public static final List<String> PROFIT_CENTERS = Arrays.asList("EB101B4EPC", "IANR000LIN", "IORD000LIN", "IPDD000LIN", "IPDS000LIN");
	public static final List<String> STATI_WBE = Arrays.asList("I0001", "I0002");
	public static final String STATO_AUTO = "E0011";
	public static final Integer ONE = 1;
	public static final Integer FOUR = 4;
	public static final List<Integer> LIVELLI_WBE = Arrays.asList(ONE, FOUR);
	public static final List<Integer> CHECK_WBX_LEVEL = Arrays.asList(ONE, FOUR);
	public static final String SUCCESS = "S";
	public static final String SAP_ERROR = "E";
	public static final String LABTRANSID = "LABTRANSID";
	public static final String YEAR = "YEAR";
	public static final String MONTH = "MONTH";
	public static final String PERIODO_CONTABILE_APERTO = "SI";
	public static final String PERIODO_CHIUSO = "CHIUSO";
	public static final String CONSUNTIVAZIONE_KO = "Invio consuntivazione non riuscito";
	public static final String CONSUNTIVAZIONE_OK = "Invio consuntivazione riuscito";
	public static final String EQ = "EQ";
	public static final String NE = "NE";
	public static final String TP_CONTO = "+";
	public static final String WORKORDERID = "WORKORDERID";
	public static final String BLOC = "I0043";
	public static final String TECO = "I0045";
	public static final String OPEX = "OPEX";
	public static final String ODM = "ODM";
	public static final String OI = "OI";
	public static final String NTK = "NTK";
	public static final String WBE = "WBE";
	public static final String NO = "N";
	public static final String PERIODO_CONTABILE_CHIUSO = "Invio consuntivazione non riuscito per periodo contabile chiuso";
	public static final String WARNING = "W";
	public static final String CORRELATIONID = "X4CORRELATIONID";

	//TAGETIK
	public static final String TAGETIK = "TAGETIK";
	
	//CAVI
	public static final String INSPECTION_NOT_FOUND = "Inspection not found";
}
