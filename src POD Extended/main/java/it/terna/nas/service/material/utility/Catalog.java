package it.terna.nas.service.material.utility;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class Catalog
{

	public enum StatusX4Esito 
	{
		OK, KO
	}

	public enum AttributesDataTypes 
	{
		DATE,
		ALN,
		NUMERIC,
		TABLE
	}
	
	@AllArgsConstructor
	@ToString
	public enum ClassificationType 
	{
		LINEA  		("LINEA"),
		STAZIONE	("STAZIONE"),
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
		X4RENAMESAPIDSNOWID	("x4RenameSapIdSnowId"),
		WONUM				("wonum"),
		HASCHILDREN			("haschildren"),
		TIPOOI				("tipooi"),
		STATOSISTEMA		("statosistema"),
		REFWO				("refwo"),
		P_PERIODO			("p_periodo"),
		P_CO_NUM_DOC 		("p_co_num_doc"),           
		P_CDC_RESP_ORG  	("p_cdc_resp_org"),   
		P_TIPO_OGG_CONTAB  	("p_tipo_ogg_contab"), 
		P_TITOLARE_RIS  	("p_titolare_ris"),
		X4ESITOINTSNOWID	("x4esitointsnowid"), 
		ROOTSAPID			("rootSapId");    


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
		EAM_0017			("EAM_0017", "400", "Deliberated dates not compliant with OdL dates.");


		@Getter
		private String eamCode;
		
		@Getter
		private String httpCode;
		
		@Getter
		private String errorDescription;
	}

	@AllArgsConstructor
	public enum ServiceNowInterface 
	{
		SEND_ASSET_INTERFACE 			("SEND_ASSET_TO_SERVICENOW");
		
		@Getter
		private String name;
	}
}
