package it.terna.nas.service.material.utility;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class Catalog
{
    
    @AllArgsConstructor
    public enum DssKafka 
    {
        CREATE_ODL          ("dssCreateOdLEventHandlerManager"),
        CLOSE_ODL           ("dssClosedPeriodicOdLEventHandlerManager"),
        AGILE_ODL           ("integrationSendAgileMessage"),
        ELIVE_ODL           ("integrationSendEliveMessage"),
        CLOSURE_NTK_ODL     ("integrationSendClosureNtkMessage"),
        CLOSE_ODM_ODL       ("integrationSendClosureOdmMessage"),
        GEOCALL_ODL         ("integrationSendGeocallMessage"),
        SAP_STORNO_ODL      ("integrationSendSapStornoTotaleMessage"),
        CAVI_ODL            ("integrationSendCaviMessage"),
        CAVI_DELETE_ODL     ("integrationSendCaviDeleteMessage"),
        MISURA              ("dssChangeMisureAsset"),
        ASSET               ("dssChangeAssetEventHandlerManager"),
        ASSETSPEC           ("dssChangeAssetSpecForAssetEventHandlerManager"),
        DIFFORMITA_ASSET    ("dssChangeDifforForAssetEventHandlerManager"),
        INTERFERENZA_ASSET  ("dssChangeInterForAssetEventHandlerManager"),
        HI_ASSET            ("dssChangeHiForAssetEventHandlerManager"),
        POLICY              ("dssPolicyChangeEventHandlerManager");
        
        @Getter
        private String name;
    }
    
    
    @AllArgsConstructor
    @ToString
    public enum VeroOrFalso 
    {
        SI ("S"), NO ("N");
        
        @Getter
        private String code;
    }
    
	@AllArgsConstructor
	public enum DssEvent 
	{
		ADD 			("ADD"),
		CALCULATE 	  	("CALCULATE");
		
		@Getter
		private String name;
	}

	
	@ToString
	@AllArgsConstructor
	public enum PolicyType
	{
		ON_CONDITION 	("ON-CONDITION"),
		PERIODICA 		("PERIODICA");
		
		@Getter
		private String code;
	}	
	
	
	@AllArgsConstructor
	public enum EAMErrorMapping
	{
		EAM_9999			("EAM_9999", "503", "EAM generic error."),
		EAM_0001			("EAM_0001", "503", "Converted Kafka Message is either Null or doesn't have a valid Payload."),
		EAM_0002			("EAM_0002", "404", "Policy not found."),
		EAM_0003			("EAM_0003", "404", "ClassStructureId not found.");		
		
		@Getter
		private String eamCode;
		
		@Getter
		private String errorDescription;
		
		@Getter
		private String httpCode;
	}
	
	
	public enum Esito 
	{
		KO, OK
	}

	
	@AllArgsConstructor
	public enum ESQLMappingParameter 
	{
		CLASSIFICATIONID		("classificationid"), 
		CLASSSTRUCTUREID		("classStructureId"),
		ASSETNUM				("assetNum"),
		CORRELATIONKEY			("correlationKey"),
		IDINPUT                 ("idInput"),
		TIPOINPUT               ("tipoInput"),
		JPNUM					("jpnum"),
		POLICYID				("x4PolicyId"),
		STATUS					("status"),
		TIPOPOLICY				("tipoPolicy"),
		VERSION					("version"),
		ASSETATTRID             ("assetattrid"),
		X4DIFFORSTATID          ("x4difforstatid"),
		X4CATDIFFORID           ("x4catdifforid"),
	    X4INTERFSTATID          ("x4interfstatid"),
	    X4CATINTERFID           ("x4catinterfid"),
	    MODELLO_ASSET_ID     	("x4catmodelloassetid"),
	    TABECCEZIONI_ID     	("x4tabeccezioniid"),
	    TABIDONEITA_ID     		("x4tabidoneitaid"),
	    MODELLO_ID          	("modelloId"),
	    MODELLO_TARGET      	("modelloTarget"),
	    TIPOLOGIA_INPUT     	("tipologiaInput"),
	    ID          			("id"),
	    HIATTRIBUTENAME         ("hiattributename"),
	    TRONCO					("tronconum"),
	    SOURCE                  ("source"),
	    DEST                    ("dest"),
	    WONUM                   ("wonum"),
	    INTERFACCIA             ("interf"),
	    ALNVALUE				("alnvalue"),
	    ASSETID                 ("assetid"),
	    ASSETSPECID             ("assetspecid"),
	    LOGICATRONCO            ("logicatronco"),
	    DAY_DELAY               ("dayDelay"),
	    DATA_ELABORAZIONE       ("dataElaborazione"),
	    RUNNER_ID               ("runnerId");

	      
		@Getter
		private String name;
	}
	
	
	@AllArgsConstructor
	@ToString
	public enum CatalogTipologyType
	{
		POLICY 				("POLICY"),
		ATTIVITA 			("ATTIVITA"),
		ASSET				("ASSET"),
		DIFFORMITA 			("DIFFORMITA"),
		MODELLO 			("MODELLO"),
		ECCEZIONE 			("ECCEZIONE"),
		IDONEITA 			("IDONEITA"),
		INTERFERENZA 		("INTERFERENZA"),
		lIVELLO_PERIODICHE	("lIVELLO_PERIODICHE"),
		VARIABILE 			("VARIABILE")
		;
		
		@Getter
		private String value;
	}
	
	
	@AllArgsConstructor
	@ToString
	public enum EventType
	{
		POLICY 				("POLICY"),
		ATTIVITA 			("ATTIVITA"),
		DIFFORMITA 			("DIFFORMITA"),
		INTERFERENZA 		("INTERFERENZA"),
		ASSET 				("ASSET"),
		HI					("HI"),
		MISURA				("MISURA"),
		ORDINE				("ORDINE"),
		VARIABILE 			("VARIABILE"),
		ASSETSPEC			("ASSETSPEC"),
		IDONEITA			("IDONEITA"),
		ECCEZIONE			("ECCEZIONE"),
		ODL					("ODL"),
		ODLCLOSED			("ODLCLOSED"),
		MODELLO				("MODELLO"),
		RECOVERY            ("RECOVERY")
		;
		
		@Getter
		private String value;
	}
	
	
	@AllArgsConstructor
	@ToString
	public enum EventActionType
	{
		ADD 			            ("ADD"),
		MODIFY 			            ("MODIFY"),
		TRAINATO_TRAINANTE_SAVE     ("TRAINATO_TRAINANTE_SAVE"),
        TRONCO_SAVE                 ("TRONCO_SAVE"),
        ZONIZZAZIONE                ("ZONIZZAZIONE"),
        AGGREGATE_COND_SAVE         ("AGGREGATE_COND_SAVE"),
        AGGREGATE_FUNE_SAVE         ("AGGREGATE_FUNE_SAVE"),
        AGGREGATE_STRUTTURA_SAVE    ("AGGREGATE_STRUTTURA_SAVE");
        
		@Getter
		private String value;
	}
	
	
	@AllArgsConstructor
	@ToString
	public enum PolicyAttributes
	{
		FREQUENCY 			("frequency"),
		DEFERRABILITY 		("deferrability"),
		MAX_ADVANCE 		("maxAdvance"),
		MAX_SNOOZE 			("maxSnooze"),
		RIF_TEMPORALE		("rifTemporale"),
		TRONCONUM           ("troncoNum");
		
		@Getter
		private String value;
	}
	
	
	@AllArgsConstructor
	@ToString
	public enum EventAttributes
	{
		JPNUM                 ("jpnum"),
		POLICY                ("policy"),
		ASSETATTRID           ("assetattrid"),
		X4CATDIFFORID         ("x4catdifforid"),
		X4DIFFORSTATID        ("x4difforstatid"),
		X4INTERFSTATID        ("x4interfstatid"),
		X4MISUREASSETID       ("x4misureassetid"),
		IDMISURA       		  ("IDMISURA"),
		X4CATMODELLOASSETID   ("x4catmodelloassetid"),
		X4TABECCEZIONEID   	  ("x4tabeccezioneid"),
		X4TABIDONEITAID       ("x4tabidoneitaid"),
	    X4CATINTERFID         ("x4catinterfid"),
	    HIATTRIBUTENAME       ("hiattributename"),
	    X4TRONCONUM			  ("x4tronconum"),
	    ACTION			      ("action"),
	    ALNVALUE			  ("alnvalue"),
	    RUNNER                ("idrunner");
	    
		@Getter
		private String value;
	}
	
	
    @AllArgsConstructor
    @ToString
    public enum TrainatoTrainanteType 
    {
        TRAINANTE("TRAINANTE"), 
        TRAINATO("TRAINATO");

        @Getter
        private String value;
    }
    
    
    @AllArgsConstructor
    @ToString
    public enum EleggibilitaType 
    {
        S_ELEG_LINEA_COND       ("S-ELEG-LINEA-COND"),
        S_ELEG_LINEA_FDG        ("S-ELEG-LINEA-FDG"),
        S_ELEG_LINEA_STRUTTURA  ("S-ELEG-LINEA-STRUTTURA"),
        K_ELEG_COND             ("K-ELEG-COND"),
        K_ELEG_FDG              ("K-ELEG-FDG"),
        K_ELEG_STRUTTURA        ("K-ELEG-STRUTTURA"),
        S1_STRUTTURA            ("S1_STRUTTURA");

        @Getter
        private String code;
    }
    
    
    @AllArgsConstructor
    public enum PolicyTimeUnit 
    {
        YEAR        ("A", ChronoUnit.YEARS), 
        MONTH       ("M", ChronoUnit.MONTHS), 
        WEEK        ("S", ChronoUnit.WEEKS), 
        DAY         ("G", ChronoUnit.DAYS), 
        HOURS       ("H", ChronoUnit.HOURS), 
        MINUTES     ("m", ChronoUnit.MINUTES);

        @Getter
        private final String     code;

        @Getter
        private final ChronoUnit temporalUnit;

        public static PolicyTimeUnit fromCode(String code)
        {
            return Arrays.stream(PolicyTimeUnit.values()).filter(x -> x.getCode().equals(code)).findFirst().orElse(null);
        }
    }
}