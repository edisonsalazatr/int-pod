package it.terna.nas.service.material.utility;

import java.util.Set;

public interface Constants
{
    public static final String SSL                              = "SSL";
    public static final String TZ_ITALY                         = "Europe/Rome";
    public static final String DATE_JSON_FORMAT                 = "yyyy-MM-dd'T'HH:mm:ss.SSS";
    public static final String MAXIMO_DATE_AND_TIME             = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String API_KEY                          = "apikey";
    public static final String AUTHORIZATION                    = "Authorization";
    public static final String BEARER                           = "Bearer ";
    public static final String DATE_FORMAT                      = "yyyyMMdd HH:mm:ss";
    public static final String DSS                              = "DSS";
    public static final String END_POINT_MANAGER_EVENTS_HANDLER = "dssEventHandlerManager";
    public static final String END_POINT_HI_CALC_EVENTS_HANDLER = "dssEventHiCalcHandler";
    public static final String END_POINT_EVALUATE_WORKER_POLICY = "workerEvaluatePolicy";
    public static final String END_POINT_EVALUATE_WORKER_PERIODICHE_POLICY = "workerEvaluatePeriodichePolicy";
    public static final String END_POINT_WORKER_MICROCONTROLLI  = "workerHandleMicrocontrolli";
    public static final String END_POINT_BATCH_MICROCONTROLLI   = "dssMicrocontrolliStart";
    public static final String HINT_FETCH_SIZE                  = "org.hibernate.fetchSize";
    public static final String BON_DSS                          = "BON_DSS_RICALCOLO";
    public static final String HINT_FETCH_SIZE_VALUE            = "1000";
    public static final String OPEN_STATUS                      = "OPEN";
    public static final String CLOSED_STATUS                    = "CLOSED";
    public static final String SEP1                             = "_";
    public static final String SEP2                             = "|";
    public static final String SEP3                             = " - ";
    public static final String SEP4                             = "-";
    public static final String SEP5                             = ", ";
    public static final String SEP6                             = "_NO_TES";
    public static final String SEP7                             = "_NO_TRO";
    public static final String SEP8                             = "/";    
    public static final String SITE                             = "SIT01";
    public static final String TIPOLOGY_PERIODICA               = "PERIODICA";
    public static final String TIPOLOGY_ON_CONDITION            = "ON-CONDITION";
    public static final String CALCULATE_PERIODIC               = "CALCULATE-PERIODIC";
    public static final String POLICY                           = "POLICY";
    public static final String DOPPIA_TERNA                     = "DOPPIATERNA";
    public static final String POLICY_STATUS_ATTIVA             = "ATTIVA";
    public static final String ATTIVITA                         = "ATTIVITA";
    public static final String DIFFORMITA                       = "DIFFORMITA";
    public static final String INTERFERENZA                     = "INTERFERENZA";
    public static final String POLICY_ATTIVA_STATUS             = "ATTIVA";
    public static final String ACTION_KAKFA_CREATE              = "Add";
    public static final String ACTION_KAKFA_REPLACE             = "Replace";
    public static final String STATUS_CHIUSO_ODL         		= "CHIUSO";
    public static final String STATUS_APERTO_ODL                = "APERTO";
    public static final String STATUS_DA_VALIDARE_ODL           = "DA VALIDARE";
    public static final String STATUS_SCHEDULABILE_ODL          = "SCHEDULABILE";
    public static final String STATUS_ESEGUITO_ODL              = "ESEGUITO";
    public static final String STATUS_NON_ESEGUITO_ODL          = "NON ESEGUITO";
    public static final String STATUS_ANNULLATO_ODL          	= "ANNULLATO";
    public static final String STATUS_CHIUSO_TEC_ODL            = "CHIUSO TEC.";
    public static final String STATUS_NON_VALIDATO_ODL          = "NON VALIDATO";
    public static final String CLASS_SOSTITUZIONE               = "SOSTITUZIONE";
    public static final String CLASS_TRONCO_ISPEZIONE           = "TRONCO DI ISPEZIONE";
    public static final String STORNO_TOTALE                    = "T";
    public static final String PREFIX_DSS_WONUM                 = "D";
    public static final Integer IS_POLICY_AGGREGATE             = 1;
    public static final String SLIM_VERSION                     = "_slim";
    public static final String ENTITY_VALIDATA_STATUS           = "VALIDATA";
    public static final String ENTITY_ATTIVO_STATUS             = "ATTIVO";
    public static final Set<String> STATUS_DECOM_ASSET          = Set.of("DELE", "DEMO", "CEAL", "DIST");
    public static final String ATTRIBUTO_ZONIZZAZIONE           = "LIN_ZONA_AMBIENTALE";
    public static final String ACTIVITY_STATUS_ACTIVE           = "ACTIVE";
    public static final String EAM                              = "EAM";
    public static final String ELIVE                            = "ELIVE";
    public static final String SEND_ODL_TO_ELIVE                = "SEND_ODL_TO_ELIVE";
    public static final String ATTR_LIN_TRONCO_ISPEZIONE        = "LIN_TRONCO_ISPEZIONE";
    public static final String ANNULLA_GEOCALL_ODL              = "CANCEL";
    
    // cache
    public static final String	CACHE_MANAGER				= "cacheManagerGeneric";
	public static final String	CACHE_ASSET					= "CACHE_ASSET";
	public static final String	CACHE_POLICY				= "CACHE_POLICY";
	public static final String	CACHE_POLICY_KEY_PREFIX		= "policy-";
	public static final String	CACHE_MODELLO_ASSET			= "CACHE_MODELLO_ASSET";
	public static final String  CACHE_MACROATTIVITA 		= "CACHE_MACROATTIVITA";
	public static final String  CACHE_ATTIVITA              = "CACHE_ATTIVITA";
	public static final String  CACHE_IDONEITA				= "CACHE_IDONEITA";
	public static final String  CACHE_ECCEZIONI				= "CACHE_ECCEZIONI";
	public static final String  CACHE_CLASSSTRUCTURE_LEVEL	= "CACHE_CLASSSTRUCTURE_LEVEL";
	public static final String  CACHE_LIVELLO_PERIODICHE    = "CACHE_LIVELLO_PERIODICHE";
	
    public static final String  CLASS_CONDUTTORI            = "CONDUTTORI";
    public static final String  CLASS_FUNI_GUARDIA          = "FUNI DI GUARDIA";
    public static final String  CLASS_SOSTEGNO_AEREO        = "SOSTEGNO AEREO";
    public static final String  CLASS_STAZIONE        		= "STAZIONE";
    public static final String  CLASS_STALLO        		= "STALLO";
    public static final String  CLASS_TELAIO                = "TELAIO";
    public static final int     BULK_SIZE                   = 100;

}
