package it.terna.nas.service.material.utility;

import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class Catalog
{

	public enum StatusX4Esito 
	{
		OK, KO, NA
	}

	
	@AllArgsConstructor
	@ToString
	public enum ClassificationType 
	{
		LINEA  		 ("LINEA"),
		STAZIONE	 ("STAZIONE"),
		UFFICIO		 ("UFFICIO"),
		FOTOVOLTAICO ("IMPIANTO FOTOVOLTAICO");
		
		@Getter
		private String name;
	}	
	
	
	@AllArgsConstructor
	public enum ESQLMappingParameter 
	{
		CLASSSTRUCTUREID  	("classstructureid"),
		SOURCE				("source"),
		RESULT				("esito"),
		RESUBMIT			("resubmit"),
		UNIQUEID            ("uniqueid"),
		STRNO				("strno"),
		CODICEOGGETTO		("codiceOggetto"),
		CODICERADICE		("codiceRadice"),
		CODICERADICE2		("codiceRadice2"),
		CLASSIFICATIONID    ("classificationid"), 
		TIPOOGGETTO			("tipoOggetto"),
		ASSETNUM_A			("assetnum"),
		STATUS				("status"),
		JPNUM				("jpnum"),
		PLUSCJPREVNUM		("pluscjprevnum"),
		ANCESTOR			("ancestor"),
		ASSETNUM			("assetNum"),
		SITEID				("siteid"),
		X4RENAMESAPIDGEOID	("x4RenameSapIdGeoId"),
		X4SISNETADDRESSID	("x4SisnetAddressId"),
		WONUM				("wonum"),
		TIPOOI				("tipooi"),
		STATOSISTEMA		("statosistema"),
		REFWO				("refwo"),
		P_PERIODO			("p_periodo"),
		P_CO_NUM_DOC 		("p_co_num_doc"),           
		P_CDC_RESP_ORG  	("p_cdc_resp_org"),   
		P_TIPO_OGG_CONTAB  	("p_tipo_ogg_contab"), 
		P_TITOLARE_RIS  	("p_titolare_ris"),
		X4ESITOINTID		("x4esitointid"),
        IDNTK               ("idntk"),
        IDOPERAZIONE		("idoperazione"),
        IDWBE               ("idwbe"),
        IDODM				("idodm"),
        NOPERAZIONE		    ("noperazione"),
		IDCONSUNTIVO		("idconsuntivo"),
		MATRICOLA			("matricola"),
		IDINTERFACCIA		("idinterfaccia"),
		FLUSSO				("flusso"),
		START_ELAB			("startelab");


		@Getter
		private String name;
	}	
	
	@AllArgsConstructor
	public enum EAMErrorMapping
	{
		EAM_0000			("EAM_0000", "503", "EAM generic error."),
		EAM_0001			("EAM_0001", "400", "Input fields not valorized correctly."),
		EAM_0002			("EAM_0002", "400", "Field 'stato' not valorized correctly."),
		EAM_0003			("EAM_0003", "404", "Resources not found."),
		EAM_0004			("EAM_0004", "404", "Work Order code not present in EAM."),
		EAM_0005			("EAM_0005", "404", "Matricola code not present in EAM."),
		EAM_0006			("EAM_0006", "503", "Converted Kafka Message is either Null or doesn't have a valid Payload."),
		EAM_0007			("EAM_0007", "400", "Field 'codice_ordine_esterno' not valorized correctly."),
		EAM_0008			("EAM_0008", "400", "Field 'codiceOggetto' not valorized correctly."),
		EAM_0009			("EAM_0009", "400", "Incorrect data for Asset."),
		EAM_0010			("EAM_0010", "400", "Field 'codice_posizione' not valorized correctly."),
		EAM_0011			("EAM_0011", "400", "Field 'causale_esito' not valorized correctly."),
		EAM_0012			("EAM_0012", "400", "Unable to determine Sapid."),
		EAM_0013			("EAM_0013", "400", "Work Order not available for the scheduling process."),
		EAM_0014			("EAM_0014", "400", "The Asset is not related to a LINEA or STAZIONE."),
		EAM_0015			("EAM_0015", "400", "The EamKey is not valorized."),
		EAM_0016			("EAM_0016", "400", "The EamKey is not present in EAM."),
		EAM_0017			("EAM_0017", "400", "Deliberated dates not compliant with OdL dates."),
		EAM_0020			("EAM_0020", "400", "Geocall storno failed"),
		EAM_0021			("EAM_0021", "400", "Controllo periodo contabile chiuso."),
		EAM_0022			("EAM_0022", "400", "Consuntivazione SAP non riuscita."),
		EAM_0023            ("EAM_0023", "400", "Inserimento/Aggiornamento tabella maximo non riuscita."),
		EAM_0024            ("EAM_0024", "400", "JWT Token retrieval failed."),
		EAM_0025            ("EAM_0025", "400", "Errore CounterODM SAP per Storno parziale."),
		EAM_0026            ("EAM_0026", "400", "Errore Storno SAP per Storno."),
		EAM_0027            ("EAM_0027", "400", "Errore Consuntivazione Callback SAP per request ricevuta non corretta."),
		EAM_0028			("EAM_0028", "400", "List of requests for acquisition SAP is empty."),
		EAM_0029			("EAM_0029", "400", "Errore durante acquisizione dati SAP."),
		EAM_0030            ("EAM_0030", "429", "Probabile superamento limite di chiamate giornaliere."),
		EAM_0031            ("EAM_0031", "400", "Sede tecnica indisponibilità non conforme."),
		EAM_0032            ("EAM_0032", "429", "Probabile superamento limite di chiamate giornaliere."),
		EAM_0033            ("EAM_0033", "400", "Errore Counter Storno response."),
		EAM_0034            ("EAM_0034", "400", "Errore Storno Simulazione response."),
		EAM_0035            ("EAM_0035", "400", "Errore Storno Effettivo response."),
		EAM_0036            ("EAM_0036", "400", "Errore Chisura Ordine di Manutenzione."),
		EAM_0037            ("EAM_0037", "400", "Errore Chisura Network."),
		EAM_0038            ("EAM_0038", "400", "Consuntivazione Dates present in the message are expressed in the future compared to the system's date."),
		EAM_0039            ("EAM_0039", "400", "Fields 'ORE', 'ORE_UFFICIO', 'TEMPO_DI_TRASFERIMENTO' have 0 as their values."),
		EAM_0040            ("EAM_0040", "400", "Modifica non possibile. WORKORDER in stato non idoneo")
		;



		@Getter
		private String eamCode;
		
		@Getter
		private String httpCode;
		
		@Getter
		private String errorDescription;
	}
	
	@AllArgsConstructor
	public enum RetryInterfacesCatalog
	{
		WONUM (Arrays.asList(SapInterface.CONSUNTIVI.getName(), GeoCallInterface.SEND_CANCEL_BLOCK_INTERFACE.getName(), 
							 GeoCallInterface.SEND_CANCEL_OPERATION_INTERFACE.getName(), GeoCallInterface.SEND_CANCEL_OPERATION_INTERFACE.getName(),
							 GeoCallInterface.SEND_STORNO_GEOCALL_INTERFACE.getName(), CaviInterratiInterface.SEND_ODL_TO_CAVI_INTERFACE.getName(),
							 CaviInterratiInterface.SEND_ODL_DELETION_TO_CAVI_INTERFACE.getName(), AgileInterface.REQUEST_UNAVAILABILITY.getName(),
							 EliveInterface.SEND_ODL_ELIVE.getName())),
		ASSETNUM (Arrays.asList(GeoCallInterface.SEND_ASSET_INTERFACE.getName()));

		@Getter
		private List<String> interfaces;
		
		public static RetryInterfacesCatalog findRetryParameter(String idInterfaccia)
		{
			return Arrays.stream(RetryInterfacesCatalog.values())
						 .filter(x -> x.getInterfaces().contains(idInterfaccia))
						 .findFirst()
						 .orElse(null);
		}
	}

	@AllArgsConstructor
	public enum SapInterface
	{
		ACQUISITION_OI 		("ACQUISITION_SAP_OI"),
		ACQUISITION_ODM		("ACQUISITION_SAP_ODM"),
		ACQUISITION_NTK		("ACQUISITION_SAP_NTK"),
		ACQUISITION_DC		("ACQUISITION_SAP_DC"),
		RIL					("RIL"),
		STORNO_PARZIALE		("STORNO_PARZIALE"),
		STORNO_TOTALE		("STORNO_TOTALE"),
		CONSUNTIVI			("CONSUNTIVI"),
		CONSUNTIVI_EXT		("CONSUNTIVI_EXT"),
		CLOSURE_ODM			("CLOSURE_SAP_ODM"),
		CLOSURE_NTK			("CLOSURE_SAP_NTK"),
        CONSUNTIVI_CALLBACK ("CONSUNTIVI_CALLBACK")
		;


		@Getter
		private String name;
	}

	@AllArgsConstructor
	public enum GeoCallInterface 
	{
		SEND_ASSET_TYPE_INTERFACE 		("SEND_ASSET_TYPE_TO_GEOCALL"),
		SEND_CANCEL_BLOCK_INTERFACE 	("SEND_CANCEL_OR_BLOCK_ODL_TO_GEOCALL"),
		SEND_CANCEL_OPERATION_INTERFACE ("SEND_CANCEL_OPERATION_TO_GEOCALL"),
		SEND_ASSET_INTERFACE 			("SEND_ASSET_TO_GEOCALL"),
		SCHEDULE_ODL_INTERFACE 			("SCHEDULE_ODL_TO_EAM"),
		SEND_ODL_INTERFACE				("SEND_ODL_TO_GEOCALL_INTERFACE"),
		NOTIFY_CLOSURE_ODL_INTERFACE    ("NOTIFY_CLOSURE_ODL_TO_EAM"),
		NOTIFY_CHANGE_STATUS_INTERFACE	("NOTIFY_CHANGE_STATUS_TO_EAM"),
		SEND_STORNO_GEOCALL_INTERFACE	("SEND_STORNO_TO_GEOCALL");
		
		@Getter
		private String name;
	}
	
	@AllArgsConstructor
	public enum CaviInterratiInterface 
	{
		SEND_ODL_TO_CAVI_INTERFACE				("SEND_ODL_TO_CAVI_INTERFACE"),
		SEND_ODL_DELETION_TO_CAVI_INTERFACE		("SEND_ODL_DELETION_TO_CAVI_INTERFACE")
		;
		
		@Getter
		private String name;
	}

	@AllArgsConstructor
	public enum AgileInterface
	{
		REQUEST_UNAVAILABILITY	("REQUEST_UNAVAILABILITY"),
		NOTIFY_UNAVAILABILITY	("NOTIFY_UNAVAILABILITY");

		@Getter
		private String name;
	}
	
	@AllArgsConstructor
	public enum EAMStatusMapping
	{
		CHIUSO 			("Chiuso",					  "CHIUSO"),
		ESEGUITO        ("Eseguito", 				  "ESEGUITO"),
		FISSATO			("Fissato", 				  "SCHEDULATO"),
		INOLTRATO		("Inoltrato", 				  "INOLTRATO"),
		IN_CARICO		("In Carico", 				  "AVVIATO"),
		SCHEDULABILE 	("Schedulabile",			  "SCHEDULABILE"),
		SCHEDULATO		("Schedulato",   			  "SCHEDULATO"),
		AVVIATO			("NON CONSIDERATO MAPPING",   "AVVIATO"),
		ESEGUITOPARZ	("NON CONSIDERATO MAPPING",   "ESEGUITO PARZ."),
		INVIATO			("Inviato", 				  "AVVIATO"),
		CHIUSOTEC		("Chiuso Tecnicamente", 	  "CHIUSO"),
		PARZIALMENTECON	("Parzialmente Consuntivato", "AVVIATO");
		
		@Getter
		private String geoCallStatus;
				
		@Getter
		private String eamStatus;
		
		public static EAMStatusMapping findStatus(String status)
		{
			return Arrays.stream(EAMStatusMapping.values())
						 .filter(x -> x.getGeoCallStatus().equalsIgnoreCase(status))
						 .findFirst()
						 .orElse(null);
		}
	}
	
	@AllArgsConstructor
	public enum EAMStatusFatherMapping
	{
		SCHEDULATO 		(Arrays.asList("SCHEDULATO"),					  			  "SCHEDULATO"),
		INVIATO 		(Arrays.asList("INOLTRATO"),					  			  "INVIATO"),
		AVVIATO 		(Arrays.asList("AVVIATO"),					 	  			  "AVVIATO"),
		ESEGUITO_PARZ   (Arrays.asList("ESEGUITO", "ESEGUITO PARZ.", "NON ESEGUITO"), "ESEGUITO PARZ.");
		
		@Getter
		private List<String> childStatus;
				
		@Getter
		private String fatherStatus;
		
		public static EAMStatusFatherMapping findFatherStatus(List<String> statusList)
		{
			return Arrays.stream(EAMStatusFatherMapping.values())
						 .filter(x -> statusList.stream().anyMatch(y -> x.getChildStatus().contains(y)))
						 .findFirst()
						 .orElse(null);
		}
	}
	
	@AllArgsConstructor
	public enum EAMCausaleEsitoMapping
	{
		ESEGUITA 			 ("ATTIVITA' ESEGUITA", "ESEGUITO"),
		NON_ESEGUITA         ("NON ESEGUITA", 		"NON ESEGUITO");
		
		@Getter
		private String causale;
		
		@Getter
		private String status;
		
		public static EAMCausaleEsitoMapping findIfNonEseguito(String causale)
		{
			return NON_ESEGUITA.getCausale().equalsIgnoreCase(causale) ? NON_ESEGUITA : null;
		}
		
		public static EAMCausaleEsitoMapping findCausaleEsito(String causale)
		{
			return Arrays.stream(EAMCausaleEsitoMapping.values())
						 .filter(x -> x.getCausale().equalsIgnoreCase(causale))
						 .findFirst()
						 .orElse(null);
		}
	}
	
	@AllArgsConstructor
	public enum EAMSchedulazioneMapping
	{
		SCHEDULAZIONE 		(Arrays.asList(EAMStatusMapping.SCHEDULABILE.getEamStatus())),
		RISCHEDULAZIONE 	(Arrays.asList(EAMStatusMapping.SCHEDULATO.getEamStatus(), EAMStatusMapping.INOLTRATO.getEamStatus(), 
											EAMStatusMapping.ESEGUITOPARZ.getEamStatus(), EAMStatusMapping.AVVIATO.getEamStatus(), 
											EAMStatusFatherMapping.INVIATO.getFatherStatus()));
		
		@Getter
		private List<String> statusList;
		
		public static EAMSchedulazioneMapping findType(String status)
		{
			return Arrays.stream(EAMSchedulazioneMapping.values())
						 .filter(x -> x.getStatusList().stream().anyMatch(s -> s.equalsIgnoreCase(status)))
						 .findFirst()
						 .orElse(null);
		}
	}
	
	@AllArgsConstructor
	public enum EGeoTipoOperazione 
	{
		ANNULLAMENTO	("ANN", 	"A"),
		BLOCCO			("BLOC",	"B");
		
		@Getter
		private String status;
		
		@Getter
		private String type;
		
		public static EGeoTipoOperazione findOperation(String status)
		{
			return Arrays.stream(EGeoTipoOperazione.values())
						 .filter(x -> x.getStatus().equals(status))
						 .findFirst()
						 .orElse(null);
		}
	}
	
	@AllArgsConstructor
    public enum StatoCluster {
        DA_VALUTARE("IN LAVORAZIONE"),
        SOSPESO("SOSPESO"),
        RINVIATO("SOSPESO"),
        NON_PROGRAMMABILE("SOSPESO"),
        ANNULLATO("ANNULLATO"),
        IN_PROGRAMMAZIONE("IN LAVORAZIONE"),
        DELIBERATO("DELIBERATO"),
        CONSUNTIVATO("CONSUNTIVATO"),
        TRASMESSO("IN LAVORAZIONE"),
        IN_ESECUZIONE("IN ESECUZIONE");
    	
    	@Getter
    	private String statusMapping;
	}
	
	@AllArgsConstructor
	public enum EliveInterface 
	{
		SEND_ODL_ELIVE					("SEND_ODL_TO_ELIVE"),
		INVIO_PROVVEDIMENTI_TO_EAM		("INVIO_PROVVEDIMENTI_TO_EAM");
		
		@Getter
		private String name;
	}
	
	@AllArgsConstructor
	public enum TagetikInterface 
	{
		COSTS_RETRIEVAL					("COSTS_RETRIEVAL_FROM_TAGETIK");
		
		@Getter
		private String name;
	}

}
