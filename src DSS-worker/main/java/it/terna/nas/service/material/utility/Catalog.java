package it.terna.nas.service.material.utility;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

public class Catalog
{

    @AllArgsConstructor
    @ToString
    public enum VeroOrFalso 
    {
        SI ("S"), NO ("N");
        
        @Getter
        private String code;
    }
    
	public enum StatusX4Esito 
	{
		OK, KO
	}

	
	@AllArgsConstructor
	@ToString
	public enum ClassificationType 
	{
		LINEA  		("LINEA"),
		STAZIONE	("STAZIONE");
		
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
		ASSETNUM			("assetNum"),
		ID_DSS_WORKED		("idDssWorked"),
		BATCH_ID	        ("ID"),
		MACROATTIVITA       ("macroActivityId"),
		ASSET_TYPE	        ("assetType"),
		ACTIVITY_ID       	("jpnum"),
		JOB_PLAN_ID       	("jobplanid"),
		TRONCONUM			("tronconum"),
		STATUS				("status"),
		ID       			("id"),
		WONUM       		("wonum"),
		ANCESTOR			("ancestor");
		
		@Getter
		private String name;
	}	
	
	@AllArgsConstructor
	public enum EAMErrorMapping
	{
	    EAM_0000            ("EAM_0000", "Elaborazione eseguita correttamente.", "OK", "Elaborazione eseguita correttamente."),
		EAM_0001            ("EAM_0001", "Policy non validata.", "OK", "Elaborazione eseguita correttamente."),
		EAM_0002            ("EAM_0002", "Impossibile determinare attività.", "KO", "Impossibile determinare attività."),
        EAM_0003            ("EAM_0003", "Check status non soddisfatta.",   "OK", "Elaborazione eseguita correttamente."),
        EAM_0004            ("EAM_0004", "Notifica inviata al manager.",    "OK", "Elaborazione eseguita correttamente."),
        EAM_0005            ("EAM_0005", "Modifica Policy, Nessuna modifica o creazione dell'ordine", "OK", "Elaborazione eseguita correttamente."),   
        EAM_0006            ("EAM_0006", "Modifica Policy, Ordini modificati.", "OK", "Elaborazione eseguita correttamente."),
        EAM_0007            ("EAM_0007", "Modifica ordine zonizzazione", "OK", "Elaborazione eseguita correttamente."), 
        EAM_0008            ("EAM_0008", "Nessuna modifica ordine zonizzazione", "OK", "Elaborazione eseguita correttamente."),  
        EAM_0009            ("EAM_0009", "Nessun ordine creato. Ordini già esistenti per questa policy", "OK", "Elaborazione eseguita correttamente."),   
        EAM_0010            ("EAM_0010", "Nessun ordine creato per questo tronco. Ordini migrati già esistenti per questa attività e tronco", "OK", "Elaborazione eseguita correttamente."),        
		EAM_0015			("EAM_0015", "error while or during the call to the MAXIMO API", "KO", "Errore invocazione API Maximo"),
		EAM_0016			("EAM_0016", "Response different from 200 from MAXIMO API", "KO", "Errore invocazione API Maximo"),
        EAM_0017            ("EAM_0017", "Errore sql", "KO", "Errore sql"),
        EAM_0019            ("EAM_0019", "Asset non trovato.", "KO", "Asset non trovato."),
        EAM_0020            ("EAM_0020", "Nessun microcontrollo trovato.", "KO", "Nessun microcontrollo trovato."),
        EAM_0021            ("EAM_0021", "Soglia non superata.", "OK", "Soglia non superata."),
        EAM_0022            ("EAM_0022", "Check status non soddisfatta per l’asset dell’ordine aggregatore.",   "OK", "Elaborazione eseguita correttamente."),
        EAM_0500            ("EAM_0500", "Send orders incomplete.", "KO", "Send orders incomplete."),
		EAM_9999            ("EAM_9999", "EAM generic error.", "KO", "Errore generico")
		;

		@Getter
		private String eamCode;
		
		@Getter
		private String errorDescription;
		
		@Getter
		private String errorFlag;
		
		@Getter
		private String shortDescription;
		
		public static EAMErrorMapping findCausaleEsito(String code)
		{
		    return Arrays.stream(EAMErrorMapping.values())
	                      .filter(x -> x.getEamCode().equalsIgnoreCase(code))
	                      .findFirst()
	                      .orElse(null);
		}
	}
	
	
	@AllArgsConstructor
	public enum PolicyInputType {
		INTERFERENZA			("INTERFERENZA", "", null),
		ATTIVITA 				("ATTIVITA", "", ""),
		DIFFORMITA 				("DIFFORMITA", "", null) ,
		COMBINAZIONE 			("COMBINAZIONE", "", null),
		VARIABILE 				("VARIABILE", "", null),
		HI						("HI", "", null),
		TASSO_GUASTO			("TASSOGUASTO", "", null),
		TASSO_ANOMALIA			("TASSOANOMALIA", "", null),
		ATTRIBUTO				("ATTRIBUTO", "ASSETSPEC", "ASSETATTRID");
		
		@Getter
		private final String code;
		
		@Getter
		private final String source;
		
		@Getter
		private final String columnField;
		
		public static PolicyInputType fromCode(String code) {
			
			return Arrays.stream(PolicyInputType.values())
							 .filter(x -> x.getCode().equals(code))
							 .findFirst()
							 .orElse(null);
		}
	}
	
	@AllArgsConstructor
	public enum VariableType {
		ASSETSPEC				("ASSETSPEC", "ASSETSPEC", "ASSETATTRID"),
		CATALOGO_DIFFORMITA		("X4CATDIFFORMITA", "X4CATDIFFORMITA", "X4CATDIFFORMITAID"),
		CATALOGO_INTERFERENZA	("X4CATINTERF", "X4CATINTERF", "X4CATINTERFID"),
		MISURA					("X4CATMISURA", "X4CATMISURE", "IDMISURA"),
		MODELLO					("X4CATMODELLO", "X4CATMODELLOASSET", "X4CATMODELLOASSETID"),
		HEALTH_INDEX			("HEALTH_INDEX", "AHMETHODOLOGY", "AHMETHODOLOGYID"),
		TASSO_GUASTO			("TASSO_GUASTO", "AHMETHODOLOGY", "AHMETHODOLOGYID"),
		TASSO_ANOMALIA			("TASSO_ANOMALIA", "AHMETHODOLOGY", "AHMETHODOLOGYID"),
		ATTIVITA				("ATTIVITA", "JOBPLAN", "JPNUM"),
		VARIABILE				("VARIABILE", "X4CATMISURE", "IDMISURA"),
		ALPHANUMERIC			("ALPHANUMERIC", "X4POLICYINPUT", "TARGETRAFFRONTO"),
		NUMERIC					("NUMERIC", "X4POLICYINPUT", "TARGETNUM"),
		DATE					("DATE", "X4POLICYINPUT", "TARGETRAFFRONTO")
		;
		
		@Getter
		private final String code;
		
		@Getter
		private final String source;
		
		@Getter
		private final String columnField;
		
		public static VariableType fromCode(String code) {
			
			return Arrays.stream(VariableType.values())
							 .filter(x -> x.getCode().equals(code))
							 .findFirst()
							 .orElse(null);
		}
		
		public static final List<VariableType> literals = List.of(ALPHANUMERIC, NUMERIC);	
		
		public boolean isLiteral() {
			return literals.contains(this);
		}
	}
	
	@AllArgsConstructor
	public enum WorkOrderStatus {
		
		DA_VALIDARE		("DA VALIDARE", 10),
		APERTO			("APERTO", 20),
		SCHEDULABILE	("SCHEDULABILE", 30),
		SCHEDULATO		("SCHEDULATO", 40),
		INOLTRATO		("INOLTRATO", 50),
		AVVIATO			("AVVIATO", 60),
		ESEGUITO_PAR	("ESEGUITO PAR", 70),
		ESEGUITO		("ESEGUITO", 80),
		CHIUSO			("CHIUSO", 500),
		NON_ESEGUITO	("NON ESEGUITO", 90),
		NON_VALIDATO	("NON VALIDATO", 400);
		
		@Getter
		private final String code;
		
		@Getter
		private final int stateNumber;
		
		public static WorkOrderStatus fromCode(String code) {
			
			return Arrays.stream(WorkOrderStatus.values())
							 .filter(x -> x.getCode().equals(code))
							 .findFirst()
							 .orElse(null);
		}
		
	    public static Set<String> getCodiciStatiAttivi() {
	        return Set.of(
	            DA_VALIDARE.getCode(),
	            APERTO.getCode(),
	            SCHEDULABILE.getCode(),
	            SCHEDULATO.getCode(),
	            INOLTRATO.getCode(),
	            AVVIATO.getCode(),
	            ESEGUITO_PAR.getCode()
	        );
	    }
	}
	
	@AllArgsConstructor
	public enum PolicyTimeUnit {
		
		YEAR("A", ChronoUnit.YEARS),
		MONTH("M", ChronoUnit.MONTHS),
		WEEK("S", ChronoUnit.WEEKS),
		DAY("G", ChronoUnit.DAYS),
		HOURS("H", ChronoUnit.HOURS),
		MINUTES("m", ChronoUnit.MINUTES)
		;

		
		@Getter
		private final String code;
		
		@Getter
		private final ChronoUnit temporalUnit;
		
		public static PolicyTimeUnit fromCode(String code) {
			
			return Arrays.stream(PolicyTimeUnit.values())
							 .filter(x -> x.getCode().equals(code))
							 .findFirst()
							 .orElse(null);
		}
	}

	@ToString
	@AllArgsConstructor
	public enum CatalogTipologyType
	{
		POLICY 			("POLICY"),
		ATTIVITA 		("ATTIVITA"),
		DIFFORMITA 		("DIFFORMITA"),
		INTERFERENZA 	("INTERFERENZA");
		
		@Getter
		private String value;
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
	
	public enum ModifyPolicyResultType {
	    NOT_FOUND,         
	    MODIFIABLE,     
	    NOT_MODIFIABLE
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
    @ToString
    public enum AttributeAssetType 
    {
        SOCIETA                             ("SOCIETA"),
        SORT                                ("SORT"),
        CENTRO_LAVORO_RESPONSABILE          ("CENTRO_LAVORO_RESPONSABILE"),
        IN_FUNZIONE_DA                      ("IN_FUNZIONE_DA"),                     
        GRUPPO_RESPONSABILE_PIANIF_MANUTEN  ("GRUPPO_RESPONSABILE_PIANIF_MANUTEN"),
        CONTROLLING_AREA                    ("CONTROLLING_AREA"),
        CENTRO_DI_COSTO                     ("CENTRO_DI_COSTO"),
        SEDE_TECNICA_DESCRIZIONE            ("SEDE_TECNICA_DESCRIZIONE"),
        SEDE_TECNICA_CODICE                 ("SEDE_TECNICA_CODICE"),
        STATO                               ("STATO"),
        DIVISIONE_UBICAZIONE                ("DIVISIONE_UBICAZIONE"),
        SOCIETA_PROPRIETARIA                ("SOCIETA_PROPRIETARIA"),
        CODICE_COSTRUTTORE                  ("CODICE_COSTRUTTORE"),
        MODELLO                             ("MODELLO"),
        RIFERIMENTO_FASE                    ("RIFERIMENTO_FASE"),
        TIPO_COMPONENTE                     ("TIPO_COMPONENTE"),
        IMPIANTO                            ("IMPIANTO");
        
        @Getter
        private String name;
    }
    
    
    @AllArgsConstructor
    @ToString
    public enum AttributeType
    {
        DATE        ("DATE"),
        ALN         ("ALN"),
        NUMERIC     ("NUMERIC"),
        TABLE       ("TABLE");
        
        @Getter
        private String code;
    }
}
