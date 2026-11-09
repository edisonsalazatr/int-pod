# EAM_Analisi Specifiche Tecniche_DSS_V2_1

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Specifica Tecnica – DSS |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data | Owner |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | ACN | 15/12/2025 |  |
| Riesaminato da: | ACN | 15/01/2026 |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

# Glossario

| Termine | Definizione |
| --- | --- |
| Piano di Lavoro (PdL) | Oggetto Maximo utilizzato per modellare l’attività del catalogo |
| Ordine di Lavoro (OdL) | Oggetto Maximo utilizzato per modellare le richieste di lavoro da eseguire |
| Configurazione | Implementazione Standard sull’applicativo IBM Maximo |
| Sviluppo | Implementazione Custom sull’applicativo IBM Maximo |
| Configurazione Database | Applicazione che consente di creare, modificare e gestire tabelle, attributi, indici e relazioni all'interno del database di Maximo. Qui è possibile definire la struttura del database e configurare i dati in modo da adattarsi alle specifiche esigenze dell'organizzazione |
| Application Designer | Applicazione che consente di creare e personalizzare l'interfaccia grafica delle applicazioni presenti in Maximo. Gli utenti possono aggiungere tabelle, sezioni e campi per adattare l'aspetto e il comportamento delle applicazioni in base alle specifiche esigenze aziendali |
| Conditional Expression Manager | Applicazione che permette di creare e modificare espressioni in linguaggio SQL utili per gestire le regole di visibilità e/o editabilità di determinati oggetti all'interno di Maximo |
| Escalation | Le escalation in IBM Maximo vengono utilizzate per automatizzare attività e monitorare record sensibili al tempo. Quest’ultime possono essere attivate quando un record raggiunge un punto di escalation definito. I punti di escalation rappresentano le condizioni che devono essere soddisfatte |
| Script di Automazione | Applicazione che permette di creare script in diversi linguaggi di programmazione al fine di automatizzare processi, eseguire calcoli complessi e personalizzare il comportamento delle applicazioni |
| Relazione | Le relazioni consentono di stabilire collegamenti tra più tabelle al fine di recuperare specifici dati mediante l'utilizzo di clausole "WHERE" all'interno di un'istruzione SQL. Questa funzionalità permette di definire una tabella di destinazione, chiamata "Figlio", sulla quale viene eseguita la clausola "WHERE", mentre la tabella di partenza è chiamata "Padre" |
| Relazione Attributo | Convenzione utilizzata in Application Designer per fare riferimento a un attributo specifico di una tabella nel database di Maximo relazionata a quella corrente |

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche, già descritte ad alto livello nel documento “EAM_Documento funzionale W2_1docx”, verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software.

All’interno del documento funzionale sono riportate due macro-feature:

Gestione delle policy non isoelemento.

Gestione della rilevabilità delle interferenze e difformità basate su logiche non isoelemento.

# Premessa: RELAZIONI ANAGRAFICHE vs REGOLE

Per meglio comprendere l’ambito e le soluzioni tecniche disegnate per soddisfare le funzionalità oggetto di questo documento è necessario chiarire il concetto di relazione anagrafica e regole

La relazione anagrafica identifica il rapporto gerarchico che sussiste tra due asset e dipende dalla tipologia di asset a cui ci si sta riferendo. La finalità della relazione anagrafica è quella di fornire agli algoritmi che usano le relazioni anagrafiche la lista degli asset su cui applicare tali logiche.

La regola di applicabilità è l’algoritmo / la logica specifica che va adottata una volta aver ricevuto in input la lista degli asset su applicare tale logica.

A partire dalla specifica funzionale nella quale erano riportate in modo accorpato le regole di navigazione / identificazione degli asset su cui dovevano essere applicate gli algoritmi sono stati suddivise le regole in relazione anagrafica e regola di applicabilità.

Questa gestione permette di poter progettare un componente dedicato alla gestione delle relazioni anagrafiche con l’obiettivo di fornire un set di asset. Questo attraverso l’implementazione di algoritmi di navigazione nell’anagrafica EAM. Il modulo che gestisce le relazioni anagrafiche è l’assetsLookup, che sarà descritto nel corpo del documento.

Le regole di applicabilità saranno applicate su entrambe le feature (Gestione delle policy non isoelemento e Gestione della rilevabilità delle interferenze e difformità basate su logiche non isoelemento) secondo il caso d’uso.

Le regole generali che valgono per tutte le tipologie di asset sono:

| Regola della specifica funzionale | estrapolazione della regola dalla relazione gerarchica | Regola vera e propria |
| --- | --- | --- |
| Regola 1: Almeno 1, la cardinalità è 1 a N quindi il sistema per poter applicare la logica deve assicurarsi che almeno 1 degli asset presi in considerazione rispettino la condizione indicata nel catalogo; | da 'CAMPATA AEREA' a CONDUTTORI da 'CAMPATA AEREA' a STRUTTURA da CONDUTTORI a 'CONDUTTORE f' da TELAIO a 'ARMAMENTO CONDUTTORE f' da 'ARMAMENTO CONDUTTORE f' a CONDUTTORI da CAVI a 'CAVO DI POTENZA f' da CAVI a 'CAVO DI POTENZA f' da GIUNTI a 'CAVO DI POTENZA f' da GIUNTI a CAVI | Almeno 1 |
| Regola 2: Per gli asset che hanno l’attributo LIN_RIF_ASSET_COND di tipo GIUNTO CONDUTTORE, ANTIVIBRANTI CONDUTTORE, SFERE DI SEGNALAZIONE CONDUTTORE, BARRETTA PREFORMATA relazionati con asset di tipo CONDUTTORE va considerato il conduttore specifico il cui codice è riportato nel campo LIN_RIF_ASSET_COND. Nel caso in cui l’attributo fosse vuoto, va utilizzata la regola 1. | 'Nel caso di BARRETTA PREFORMATA CONDUTTORE, SFERE DI SEGNALAZIONE CONDUTTORE, ANTIVIBRANTI CONDUTTORE, CONDUTTORE, GIUNTO CONDUTTORE, DISTANZIATORI sarà l'asset CONDUTTORE di riferimento o tutti e 3 i CONDUTTORI fratelli | Almeno 1 |
| Regola 3: Per gli asset che hanno l’attributo LIN_RIF_ASSET_FDG di tipo GIUNTO FUNE DI GUARDIA, ANTIVIBRANTI FUNE DI GUARDIA, ANTIROTAZIONALI FUNE DI GUARDIA relazionati con asset di tipo FUNE DI GUARDIA va considerata la fune di guardia specifica il cui codice è riportato nel campo LIN_RIF_ASSET_FDG. Nel caso in cui l’attributo fosse vuoto, va utilizzata la regola 1. | Nel caso di GIUNTO FUNE DI GUARDIA, ANTIROTAZIONALI FUNE DI GUARDIA, ANTIVIBRANTI FUNE DI GUARDIA sarà il valore dell'attributo oppure gli asset | Almeno 1 |
| Regola 4: L’asset gerarchicamente superiore (a uno o più livelli di gerarchia sopra), la cardinalità è sempre 1 a 1 quindi la selezione è inequivocabile. | da ‘CONDUTTORE f’ a ‘CAMPATA AEREA’ da 'FUNE DI GUARDIA g' a ‘CAMPATA AEREA’ da 'ISOLATORE CONDUTTORE f' a LINEA da STRUTTURA a LINEA da SEZIONATORE a LINEA da ‘FIBRA OTTICA CAVO’ a CAVI da ‘CAVO DI POTENZA f’ a CAVI da 'SEGNALAZIONE CAVO x' a CAVI da 'CAVO DI POTENZA f' a TRATTA CAVO da 'TERMINALE f' a LINEA da 'INTERRUTTORE AIS x' a SEZIONE da 'INTERRUTTORE GIS x' a SEZIONE da 'SEZIONATORE AIS x' SEZIONE da STALLO - STAZIONE da STALLO - SEZIONE | Identità |
| Regola 5: Per gli asset che hanno l’attributo LIN_RIF_ASSET_ARM_COND di tipo ISOLATORE CONDUTTORE relazionati con asset di tipo ARMAMENTO CONDUTTORE va considerato l’armamento conduttore il cui codice è riportato nel campo LIN_RIF_ASSET_ARM_COND. | ISOLATORE CONDUTTORE ad ARMAMENTO CONDUTTORE: sarà restituito l'armamento conduttore indicato dall'attributo | Almeno 1 |
| Regola 6: per asset ISOLATORE CONDUTTORE 4° livello relazionati a CAMPATA AEREA, dato che la campata aerea è un 2° livello di gerarchia differente, si risale dall’isolatore conduttore al sostegno 2° livello gerarchicamente superiore (relazione di cardinalità 1 a 1 quindi inequivocabile) poi dal sostegno si individua la campata per la quale il sostegno è quello di fine campata (regola inequivocabile). | A partire dall'isolatore conduttore restituire l’unica campata aerea | Identità |
| Regola 7: identifica la relazione tra un quarto livello e un terzo livello di altra gerarchia. In particolare, per le relazioni tra ISOLATORE CONDUTTORE con CONDUTTORI (terzo livello), ARMAMENTO FUNE DI GUARDIA con CONDUTTORI e FUNI DI GUARDIA (terzi livelli) la regola è quella di risalire al secondo livello sostegno e individuare la campata il cui sostegno è quello di fine, da lì scendere al terzo livello e applicare la regola 1 (almeno 1 dei terzi presenti sotto la campata deve rispettare la condizione). | da 'ISOLATORE CONDUTTORE f' a CONDUTTORI da 'ARMAMENTO FUNE DI GUARDIA g' a CONDUTTORI da 'ARMAMENTO FUNE DI GUARDIA g' a 'FUNI DI GUARDIA' | Almeno 1 |
| Regola 8: per asset di quarto livello sotto la gerarchia cavi del secondo livello ESTREMO CAVO relazionati ad asset di terzo e quarto livello sotto la gerarchia di secondo livello TRATTA CAVO la regola da applicare è quella di risalire dal quarto livello al secondo livello tratta cavo (cardinalità 1 a 1 quindi inequivocabile), la tratta cavo ha una relazione inequivocabile con l’estremo di fine tratta quindi è possibile scendere sull’estremo della tratta fino al terzo o al quarto livello indicato nella relazione e applicare la regola dell’almeno 1 (almeno 1 degli asset deve rispettare la condizione). | da 'CAMERA ISPEZIONABILE' a 'CAVO DI POTENZA f' da 'CASSETTA DI SEZIONAMENTO GIUNTI' a 'CAVO DI POTENZA f' da 'CASSETTA DI SEZIONAMENTO GIUNTI' a CAVI da 'GIUNTO CAVO f' a CAVI da 'CAMERA ISPEZIONABILE' a CAVI | Almeno 1 |
| Regola 9: per l’asset GIUNTO CAVO relazionato al CAVO DI POTENZA che si trovano sotto rispettivamente al secondo ESTREMO CAVO e TRATTA CAVO che sono relazionati dalla regola precedentemente descritta (estremo di fine tratta) si risale ai cavi di potenza e si identifica quello che ha lo stesso valore del GIUNTO CAVO nel campo LIN_RIF_FASE. | da 'GIUNTO CAVO f' a 'CAVO DI POTENZA f' | ? Identità ? Se LIN_RIF_FASE non è valorizzato |
| Regola 10: per l’asset FUNE DI GUARDIA relazionato al CONDUTTORI (terzo livello), si individuano tutti i conduttori sotto la stessa campata della fune e si applica la regola 1 (almeno uno dei conduttori deve rispettare la condizione). | da 'FUNE DI GUARDIA g' a CONDUTTORI | Almeno 1 |
| Regola 11: per le relazioni tra quarti livelli sotto lo stesso padre, si applica la regola 1 (almeno uno sotto lo stesso padre deve rispettare la condizione). | da 'SEGNALAZIONE CAVO x' a 'CAVO DI POTENZA f' da 'FIBRA OTTICA CAVO' a 'CAVO DI POTENZA f' da 'CASSETTA DI SEZIONAMENTO TERMINALI' a 'TERMINALE f' | Almeno 1 |
| Regola 12: per asset Macchina relazionati con sistema di raffreddamento, visto che sono collegati anagraficamente, si considerano i sistemi di raffreddamento sotto lo stesso padre della macchina in valutazione e tra questi (che possono essere massimo 3) si prende in considerazione quello specifico che ha lo stesso SAPID nel campo X3SAPID della macchina | da 'MACCHINA x' a 'SISTEMA DI RAFFREDDAMENTO x' | Identità |
| Regola 13: asset gerarchicamente superiori all’asset in esame (uno o più livelli gerarchici sopra), si abbina alla regola 4 perché la relazione è sempre univoca. | da CAMPATA AEREA a LINEA da CONDUTTORE a CAMPATA AEREA da FUNE DI GUARDIA a CAMPATA AEREA da ISOLATORE CONDUTTORE a LINEA da STRUTTURA a LINEA da SEZIONATORE a LINEA da FIBRA OTTICA CAVO a CAVI da CAVO DI POTENZA a CAVI da SEGNALAZIONE CAVO a CAVI da CAVO DI POTENZA a TRATTA CAVO da TERMINALE a LINEA da INTERRUTTORE AIS a SEZIONE da INTERRUTTORE GIS a SEZIONE da SEZIONATORE AIS a SEZIONE da STALLO a STAZIONE da STALLO a SEZIONE | Identità |
| Regola 14: identifica la relazione tra due quarti livelli sotto secondi livelli diversi. In particolare, tra due quarti livelli uno sotto gerarchia SOSTEGNO AEREO e uno sotto CAMPATA AEREA. Si risale dal quarto livello in esame fino al secondo livello SOSTEGNO AEREO, si ricerca la CAMPATA AEREA di cui il sostengo è di fine dopo di che si riscende al terzo (che è uno solo) e dal terzo si applica la regola 1 perché i quarti livelli sono 3. | da 'ARMAMENTO FUNE DI GUARDIA g' a 'FUNI DI GUARDIA' da 'ARMAMENTO FUNE DI GUARDIA g' a 'CONDUTTORE' | Almeno 1 |

# GESTIONE POLICY NON ISOELEMENTO

## Descrizione del modello dati

Di seguito verranno riportate le tabelle utilizzate in ambito DSS per le tematiche relative alla W2.1.

Alcune tabelle saranno completamente nuove, mentre altre sono state modificate per adeguarci alla specifica.

X4_RULES (NUOVA TABELLA)

Questa tabella contiene la configurazione delle regole da associare alle policyinput per forzare il calcolo della policy su asset correlati.

| Campo | Tipo | Tipo Maximo | Obbligatorio | Descrizione |
| --- | --- | --- | --- | --- |
| X4_RULESID | NUMBER | BIGINT | Y | PK della tabella |
| NAME | VARCHAR2(64) | ALN (64) | Y | Nome della regola, es Regola1 |
| DESCRIPTION | VARCHAR2(512) | ALN (512) | Y | Descrizione della regola |
| X4_TIPOLOGIE_CORRELATEID | NUMBER | BIGINT | Y | FK per associare la tipologia correlata |
| X4_QUANTIFICATION_RULESID | NUMBER | BIGINT | Y | FK per associare la regola di applicabilità |
| X4_ALGORITMIID | NUMBER | BIGINT | Y | FK per associare l’algoritmo per trovare gli asset correlati |

X4_TIPOLOGIE_CORRELATE (NUOVA TABELLA)

Questa tabella contiene le coppie di tipologie di elementi (asset) tra loro connessi secondo una certa gerarchia.

| Campo | Tipo | Tipo Maximo | Obbligatorio | Descrizione |
| --- | --- | --- | --- | --- |
| X4_TIPOLOGIE_CORRELATEID | NUMBER | BIGINT | Y | PK della tabella |
| TIPOLOGIA | VARCHAR2(64) | UPPER (64) | N | Tipo di primo livello |
| ELEMENTO_RIFERIMENTO | VARCHAR2(65) | ALN (65) | Y | Elemento asset di riferimento |
| ELEMENTO_TARGET | VARCHAR2(65) | ALN (65) | Y | Elemento asset target |
| LIVELLO_RIFERIMENTO | NUMBER | INTEGER | N | Livello elemento asset di riferimento |
| LIVELLO_TARGET | NUMBER | INTEGER | N | Livello elemento asset target |
| CARDINALITA | VARCHAR2(8) | ALN (8) | N | Cardinalità |
| ECCEZIONE | VARCHAR2(512) | ALN (512) | N | Eccezione |
| CATALOGO | VARCHAR2(64) | UPPER (64) | N | Catalogo (es. POLICY) |
| CREATED_AT | Timestamp | DATETIME | Y | Data creazione record |

X4_QUANTIFICATION_RULES (NUOVA TABELLA)

Questa tabella contiene le regole di applicabilità.

| Campo | Tipo | Tipo Maximo | Obbligatorio | Descrizione |
| --- | --- | --- | --- | --- |
| X4_QUANTIFICATION_RULESID | NUMBER | BIGINT | Y | ID univoco della tabella |
| RULE | VARCHAR2(32) | UPPER (32) | Y | Regola di applicabilità: es: “almeno 1”, “1”, “tutti”, “nessuno” |
| DESCRIPTION | VARCHAR2(512) | ALN (512) | N | Descrizione |
| CREATED_AT | Timestamp | DATETIME | Y | Data creazione record |

X4_ALGORITMI (NUOVA TABELLA)

Questa tabella contiene le regole per passare da un elemento ad un altro.

| Campo | Tipo | Tipo Maximo | Obbligatorio | Descrizione |
| --- | --- | --- | --- | --- |
| X4_ALGORITMID | NUMBER | BIGINT | Y | ID univoco della tabella |
| NAME | VARCHAR2(64) | ALN (64) | Y | Nome sintetico dell’algoritmo da eseguire |
| ALGORITMO | VARCHAR2(512) | ALN (512) | Y | Algoritmo per ottenere elementi connessi dato un asset di partenza |
| DESCRIPTION | VARCHAR2(4000) | ALN (4000) | N | Descrizione |
| CREATED_AT | Timestamp | DATETIME | Y | Data creazione record |

X4POLICYINPUT

Questa tabella contiene le informazioni relative agli input delle Policy.

CAMPI VECCHI:

| Campo | Tipo | Tipo Maximo | Obbligatorio | Descrizione |  |
| --- | --- | --- | --- | --- | --- |
| DESCRIPTION | VARCHAR2(50) | ALN (50) | N | Descrizione funzionale | Descrizione funzionale |
| IDINPUT | VARCHAR2(50) | ALN (50) | N | ID tabellare Input | ID tabellare Input |
| IDINPUT_EXT | VARCHAR2(50) | ALN (50) | N | ID descrittivo input | ID descrittivo input |
| IDMISURA | VARCHAR2(25) | ALN (25) | N | ID tabellare Misura | ID tabellare Misura |
| IDMISURA_EXT | VARCHAR2(50) | ALN (50) | N | ID descrittivo Misura | ID descrittivo Misura |
| LIVINPUT | NUMBER | INTEGER | N | Livello input | Livello input |
| NINPUT | NUMBER | INTEGER | N | Numero input | Numero input |
| OPERATORE | VARCHAR2(15) | ALN (15) | N | Operatore di confronto | Operatore di confronto |
| TARGETNUM | FLOAT | FLOAT | N | Valore numerico di confronto | Valore numerico di confronto |
| TARGETRAFFRONTO | VARCHAR2(50) | ALN (50) | N | ID target di raffronto | ID target di raffronto |
| TARGETRAFFRONTO_EXT | VARCHAR2(50) | ALN (50) | N | ID descrittivo target di raffronto | ID descrittivo target di raffronto |
| TIPOLOGIAINPUT | VARCHAR2(20) | ALN (20) | N | Tipologia dell'input (ATTRIBUTO, VARIABILE, etc) | Tipologia dell'input (ATTRIBUTO, VARIABILE, etc) |
| X4AND | VARCHAR2(50) | ALN (20) | N | N input in AND | N input in AND |
| X4EQUAL | VARCHAR2(20) | ALN (20) | N | N input in equal | N input in equal |
| X4NOTEQUAL | VARCHAR2(20) | ALN (20) | N | N input in NOT equal | N input in NOT equal |
| X4OR | VARCHAR2(50) | ALN (50) | N | N input in OR | N input in OR |
| X4POLICYID | NUMBER | BIGINT | N | Riferimento FK tabellare x4policy | Riferimento FK tabellare x4policy |
| X4POLICYINPUTID | NUMBER | BIGINT | Y | PK tabella | PK tabella |
| VERSION | NUMBER | INTEGER | N | Versione della policy | Versione della policy |

CAMPI NUOVI:

| Campo | Tipo | Tipo Maximo | Obbligatorio | Descrizione |
| --- | --- | --- | --- | --- |
| X4_RULESID | NUMBER | BIGINT | N | ID della tabella delle regole |
| X4_RULESID_EXT | VARCHAR2(64) | ALN (64) | N | Campo descrittivo per la regola |
| ELEMENTO_ASSET | VARCHAR2(65) | ALN (65) | N | Elemento asset |
| CLASSIFICAZIONE_ASSET | VARCHAR2(192) | UPPER (192) | N | Classificazione asset |
| CLASSSTRUCTUREID | VARCHAR2(26) | UPPER (26) | N | ID univoco di CLASSSTRUCTURE |

X4POLICY

Questa tabella contiene le informazioni di testata del Catalogo Policy.

CAMPI NUOVI:

| Campo | Tipo | Tipo Maximo | Obbligatorio | Descrizione |
| --- | --- | --- | --- | --- |
| ISOELEMENTO | NUMBER | YORN | Y | Indica se la Policy ha tutti input isoelemento |

Esempio:

X4_QUANTIFICATION_RULES

| X4_QUANTIFICATION _RULESID | RULE | DESCRIPTION | CREATED_AT |
| --- | --- | --- | --- |
| 1 | ALMENO 1 | Almeno 1 | 2026-01-15 17:00:00 |
| 2 | IDENTITA | Identità | 2026-01-15-17:00:00 |

X4_ALGORITMI

| X4_ALGORITMIID | NAME | ALGORITMO | DESCRIPTION | CREATED_AT |
| --- | --- | --- | --- | --- |
| 1 | Algoritmo Regola 1 | ALGREG1 | Descrizione del percorso seguito dall’algoritmo per passare da una tipologia di partenza ad una di arrivo | 2026-01-15 17:00:00 |
| 2 | Algoritmo Regola 2 | ALGREG2 | Descrizione del percorso seguito dall’algoritmo per passare da una tipologia di partenza ad una di arrivo | 2026-01-15 17:00:00 |

X4_TIPOLOGIE_CORRELATE

| X4_TIPOLOGIE_CORRELATEID | CATALOGO | ELEMENTO_RIFERIMENTO | ELEMENTO_TARGET |
| --- | --- | --- | --- |
| 1 | POLICY | CAMPATA AEREA | CONDUTTORI |
| 2 | ATTIVITA | CAMPATA AEREA | STRUTTURA |
| 3 | DIFFORMITA | CONDUTTORI | CONDUTTORE |

Esempio di configurazione di una regola

| X4_RULESID | NAME | DESCRIPTION | X4_TIPOLOGIE _CORRELATEID | X4_QUANTIFICATION_RULESID | X4_RULESID | X4_ALGORITMIID |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | Regola1 | REGOLA 1 da CAMPATA AEREA a CONDUTTORI Algoritmo 1 – Almeno 1 | 1 | 1 | 1 | 1 |
| 2 | Regola1 |  |  |  |  |  |

Esempio di policy non isolivello

| X4POLICYID | TIPOLOGIAASSET | ISOLIVELLO | TIPOPOLICY |
| --- | --- | --- | --- |
| 1 | CAMPATA AEREA | 0 | ON-CONDITION |

Esempio di policyinput non isolivello

| X4POLICYID | X4POLICYINPUTID | ELEMENTO_ASSET | Classification Asset | X4_RULESID | X4_RULES_ID_EXT |
| --- | --- | --- | --- | --- | --- |
| 1 | 1 | CONDUTTORI | null | 1 | Regola 1 |

## AssetsLookup: gestione relazioni anagrafiche

L’assetsLookup è un api API Rest di un nuovo microservizio (eam-assets-lookup) che prenderà in input i seguenti parametri:

assetnum (stringa, obbligatorio): identificativo dell’asset di partenza;

siteId (stringa, obbligatorio): Dedicato al tenant

ruleId (numero, obbligatorio): ID di X4_RULES;

classStructureIdTarget (stringa, opzionale): rappresenta il classStructureId della tipologia target, parametro opzionale, se assente verranno restituiti tutti gli asset con lo stesso X4Elemento specificato dalla regola.

URL: http://<eam-assets-lookup_host>/eam/assets/lookup

E restituirà un json di questo tipo:

{

"esito": "OK\|KO",

"descrizione": "descrizione in caso di esito ko",

"assets": [

{"asset1" ; ,

"asset2",

"asset3",

"assetN"

]

}

Step 1) Si carica il record di X4_Rules usando il metodo findById del repository jpa relativo alla tabella X4_RULES.

Nel caso in cui non ci sia un record configurato per l’input fornito verrà restituito il seguente messaggio d’errore:

{

"esito": "KO",

"descrizione": "Nessun record trovato con X4_RULESID = [ruleId]"

}

Del record estratto ci interessa il campo X4_TIPOLOGIE_CORRELATEID (con il quale recuperare la tipologia correlata applicata) e il campo X4_ALGORITMIID per sapere quale algoritmo usare per trovare gli asset correlati.

Eseguire la seguente query (1):

SELECT rule.*, alg.*, tc.*

FROM X4_RULES rule

JOIN X4_TIPOLOGIE_CORRELATE tc

ON rule.X4_TIPOLOGIE_CORRELATEID = tc.TIPOLOGIE_CORRELATEID

JOIN X4_ALGORITMI alg

ON alg.X4_ALGORITMIID = rule.X4_ALGORITMIID

WHERE rule.X4_RULESID = <ruleId>

Dalla query precedente estrapolo il contenuto della colonna ALGORITMO, ELEMENTO_RIFERIMENTO e ELEMENTO_TARGET;

Il campo QUERY_ ALGORITMO conterrà una stringa alfanumerica che sta ad indicare un algoritmo da eseguire. Ad ogni algoritmo sarà associata una classe java che dovrà implementare la seguente interfaccia:

public interface InterfAlgAssetLookup

{

public AssetLookupResponse assetLookupSearch(AssetLookupRequest request, X4RuleExt ruleExt

);

}

Di seguito descrizione dei vari algoritmi:

ALGREG1 (classe AlgRegola1):

Per questo algoritmo è necessario considerare diversi casi:

Se l’ELEMENTO_RIFERIMENTO e l’ELEMENTO_TARGET rientrano nei casi seguenti:

Da CAMPATA AEREA a CONDUTTORI

Da CONDUTTORI a CONDUTTORE

Da CAVI a CAVO DI POTENZA

allora l’algoritmo esegue le seguente query:

SELECT c.assetnum AS results FROM asset p JOIN asset c ON c.parent = p.assetnum JOIN classstructure cs ON cs.classstructureid = c.classstructureid WHERE p.assetnum = '<REQUEST.ASSETNUM>' AND cs.x4elemento = '<X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET>'

Se l’ELEMENTO_RIFERIMENTO e l’ELEMENTO_TARGET rientrano nei casi seguenti:

Da TELAIO a ARMAMENTO CONDUTTORE

allora l’algoritmo esegue la seguente query:

SELECT c.assetnum FROM asset b join asset c ON b.ancestor = c.ancestor join classstructure cs ON cs.classstructureid = c.classstructureid WHERE b.assetnum = '<REQUEST.ASSETNUM>' AND cs.x4elemento = '<X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET>';

Se l’ELEMENTO_RIFERIMENTO e l’ELEMENTO_TARGET rientrano nei casi seguenti:

Da CAMPATA AEREA a STRUTTURA

allora l’algoritmo esegue le seguenti query:

SELECT g.assetnum FROM ( SELECT r1.x3sapid \|\| '-S' AS x3sapid_s FROM assetlocrelation r1 WHERE r1.targetassetnum = '<REQUEST.ASSETNUM>' UNION SELECT value AS x3sapid_s FROM x3sapassetdata WHERE sapassetid IN ( SELECT sapassetid FROM x3saptomaxobject WHERE objectnum = '<REQUEST.ASSETNUM>' ) AND name = 'PMT_SOST_FINE' ) x join assetlocrelation r2 ON r2.x3sapid = x.x3sapid_s join asset p ON p.assetnum = r2.targetassetnum join asset c ON c.parent = p.assetnum join asset g ON g.parent = c.assetnum join classstructure cs ON cs.classstructureid = g.classstructureid WHERE cs.x4elemento = 'STRUTTURA';

Se l’ELEMENTO_RIFERIMENTO e l’ELEMENTO_TARGET rientrano nei casi seguenti:

Da ARMAMENTO CONDUTTORE a CONDUTTORI

Da GIUNTI a CAVI

Da GIUNTI a CAVO DI POTENZA

SELECT c.assetnum FROM asset p join asset c ON c.ancestor = p.assetnum join classstructure cs ON cs.classstructureid = c.classstructureid WHERE p.assetnum IN ( SELECT targetassetnum FROM assetlocrelation WHERE x3sapid IN ( SELECT Substr(x3sapid, 1, 16) FROM assetlocrelation WHERE targetassetnum = ( SELECT ancestor FROM asset WHERE assetnum = '<REQUEST.ASSETNUM>' ) ) ) AND cs.x4elemento = '<X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET>';

Per tutti i casi, il valore di results, se è presente, verrà usato per popolare il campo assets dell’oggetto di risposta AssetLookupResponse.

ALGREG2 (classe AlgRegola2):

Come prima azione verrà eseguita la seguente query:

SELECT a.ASSETNUM AS results

FROM assetspec c, asset a

WHERE c.ASSETATTRID = 'LIN_RIF_ASSET_COND'

AND c.TABLEVALUE IS NOT NULL

AND c.assetnum = '<request.assetnum>'

AND c.TABLEVALUE = a.assetnum;

Se la query fornisce un esito verrà popolato il campo assets dell’oggetto di risposta AssetLookupResponse con il valore di results.

Altrimenti verrà eseguita la seguente query:

SELECT c.assetnum AS results

FROM ASSET a, asset b, asset c

WHERE a.assetnum = '<request.assetnum>'

AND a.PARENT = b.ASSETNUM

AND b.ASSETNUM = c.parent

AND c.CLASSSTRUCTUREID = '1009'; --1009=CONDUTTORE

Il valore di results verrà usato per popolare il campo assets di dell’oggetto di risposta AssetLookupResponse.

ALGREG3 (classe AlgRegola3):

Come prima azione verrà eseguita la seguente query:

SELECT a.ASSETNUM AS results

FROM assetspec c, asset a

WHERE c.ASSETATTRID = 'LIN_RIF_ASSET_FDG'

AND c.TABLEVALUE IS NOT NULL

AND c.assetnum = '<request.assetnum>'

AND c.TABLEVALUE = a.assetnum;

Se la query fornisce un esito verrà popolato il campo assets dell’oggetto di risposta AssetLookupResponse con il valore di results.

Altrimenti verrà eseguita la seguente query:

SELECT c.assetnum AS results

FROM ASSET a, asset b, asset c

WHERE a.assetnum = '<request.assetnum>'

AND a.PARENT = b.ASSETNUM

AND b.ASSETNUM = c.parent

AND c.CLASSSTRUCTUREID = '1022'; --1022= FUNE DI GUARDIA

Se la query fornisce un esito verrà popolato il campo assets dell’oggetto di risposta AssetLookupResponse con il valore di results.

ALGREG4 (classe AlgRegola4):

Per questo algoritmo è necessario considerare diversi casi:

Se X4_TIPOLOGIE_CORRELATE.TIPOLOGIA = 'STAZIONE' allora le query da eseguire sono le seguenti:

SELECT c.CLASSSTRUCTUREID AS target

FROM CLASSSTRUCTURE c

WHERE c.CLASSIFICATIONID = '<X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET>'

SELECT b.assetnum AS results

FROM (

SELECT a.assetnum, a.CLASSSTRUCTUREID

FROM ASSET a

START WITH a.assetnum = '<request.assetnum>'

CONNECT BY PRIOR parent = assetnum) B

WHERE b.CLASSSTRUCTUREID = '<target>'

AND rownum < 2;

Nel caso sia stato passato all’API il parametro opzionale classStructureIdTarget verrà effettuata solo la seconda query con il classstrctureid = <request. classStructureIdTarget >

SELECT b.assetnum AS results

FROM (

SELECT a.assetnum, a.CLASSSTRUCTUREID

FROM ASSET a

START WITH a.assetnum = '<request.assetnum>'

CONNECT BY PRIOR parent = assetnum) B

WHERE b.CLASSSTRUCTUREID = '<request.classStructureIdTarget>'

AND rownum < 2;

Se la query fornisce un esito verrà popolato il campo assets dell’oggetto di risposta AssetLookupResponse con il valore di results.

Le altre casistiche sono legate alla linea. Bisogna fare una distinzione in funzione del campo X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET = 'LINEA' o meno.

Se X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET != ‘LINEA’ le query da eseguire sono le seguenti:

SELECT c.CLASSSTRUCTUREID AS target

FROM CLASSSTRUCTURE c

WHERE c.CLASSIFICATIONID = '<X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET>'

SELECT b.assetnum AS results

FROM (

SELECT a.assetnum, a.CLASSSTRUCTUREID

FROM ASSET a

START WITH a.assetnum = '<request.assetnum>'

CONNECT BY PRIOR parent = assetnum) B

WHERE b.CLASSSTRUCTUREID = '<target>'

AND rownum < 2;

Nel caso sia stato passato all’API il parametro opzionale classStructureIdTarget verrà effettuata solo la seconda query con il classstrctureid = <request. classStructureIdTarget >

SELECT b.assetnum AS results

FROM (

SELECT a.assetnum, a.CLASSSTRUCTUREID

FROM ASSET a

START WITH a.assetnum = '<request.assetnum>'

CONNECT BY PRIOR parent = assetnum) B

WHERE b.CLASSSTRUCTUREID = '<request.classStructureIdTarget>'

AND rownum < 2;

Il valore di results servirà a popolare il campo assets del dell’oggetto di risposta AssetLookupResponse.

Se X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET = ‘LINEA’ la query da eseguire sono le seguenti:

SELECT c.CLASSSTRUCTUREID AS target

FROM CLASSSTRUCTURE c

WHERE c.CLASSIFICATIONID = 'LINEA'

SELECT a.assetnum AS results

FROM (

SELECT a.assetnum

FROM ASSET a

WHERE a.parent IS null

START WITH a.assetnum = '<request.assetnum>'

CONNECT BY PRIOR parent = assetnum) B, ASSETLOCRELATION C, ASSET A

WHERE b.assetnum = c.targetAssetnum AND c.sourceassetnum = a.assetnum

AND a.CLASSSTRUCTUREID = '<target>';

Il valore di results servirà a popolare il campo assets del dell’oggetto di risposta AssetLookupResponse.

ALGREG5 (classe AlgRegola5):

Come prima azione verrà eseguita la seguente query:

SELECT a.ASSETNUM AS results

FROM assetspec c, asset a

WHERE c.ASSETATTRID = 'LIN_RIF_ASSET_ARM_COND'

AND c.TABLEVALUE IS NOT NULL

AND c.assetnum = '<request.assetnum>'

AND c.TABLEVALUE = a.assetnum;

Se la query fornisce un esito verrà popolato il campo assets dell’oggetto di risposta AssetLookupResponse con il valore di results.

Altrimenti verrà eseguita la seguente query:

SELECT c.assetnum AS results

FROM ASSET a, asset b, asset c

WHERE a.assetnum = '<request.assetnum>'

AND a.PARENT = b.ASSETNUM

AND b.ASSETNUM = c.parent

AND c.CLASSSTRUCTUREID = '1038'; --1038 = ARMAMENTO CONDUTTORE

Il valore di results verrà usato per popolare il campo assets dell’oggetto di risposta AssetLookupResponse.

ALGREG6 (classe AlgRegola6):

Di seguito, la query da eseguire per determinare gli asset di output:

SELECT alr_t.targetassetnum AS results FROM assetlocrelation alr_t WHERE alr_t.x3sapid IN ( SELECT Substr(alr_s.x3sapid, 1, 16) FROM assetlocrelation alr_s WHERE alr_s.targetassetnum = ( SELECT a.ancestor FROM asset a WHERE a.assetnum = '<REQUEST.ASSETNUM>' ) );

Il valore di results verrà usato per popolare il campo assets dell’oggetto di risposta AssetLookupResponse.

ALGREG7 (classe AlgRegola7):

Di seguito, la query da eseguire per determinare gli asset di output:

SELECT c.assetnum FROM asset p join asset c ON c.ancestor = p.assetnum join classstructure cs ON cs.classstructureid = c.classstructureid WHERE p.assetnum IN ( SELECT targetassetnum FROM assetlocrelation WHERE x3sapid IN ( SELECT Substr(x3sapid, 1, 16) FROM assetlocrelation WHERE targetassetnum = ( SELECT ancestor FROM asset WHERE assetnum = '<REQUEST.ASSETNUM>' ) ) ) AND cs.x4elemento = '<X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET>';

Il valore di results verrà usato per popolare il campo assets dell’oggetto di risposta AssetLookupResponse.

ALGREG8 (classe AlgRegola8):

Di seguito, la query da eseguire per determinare gli asset di output:

SELECT c.assetnum FROM asset p join asset c ON c.ancestor = p.assetnum join classstructure cs ON cs.classstructureid = c.classstructureid WHERE p.assetnum IN ( SELECT targetassetnum FROM assetlocrelation WHERE x3sapid IN ( SELECT Substr(x3sapid, 1, 16) FROM assetlocrelation WHERE targetassetnum = ( SELECT ancestor FROM asset WHERE assetnum = '<REQUEST.ASSETNUM>' ) ) ) AND cs.x4elemento = '<X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET>';

il valore di results, se è presente, verrà usato per popolare il campo assets di dell’oggetto di risposta AssetLookupResponse.

ALGREG9 (classe AlgRegola9):

Come prima azione verrà eseguita la seguente query:

SELECT a.alnvalue AS target

FROM assetspec a

WHERE a.ASSETATTRID = 'LIN_RIN_FASE’

AND a.assetnum = '<request.assetnum>'

Se è presente il valore dell’attributo, eseguiamo la query successiva:

SELECT c.assetnum FROM asset p join asset c ON c.ancestor = p.assetnum join classstructure cs ON cs.classstructureid = c.classstructureid WHERE p.assetnum IN ( SELECT alr_t.targetassetnum FROM assetlocrelation alr_t WHERE alr_t.x3sapid IN ( SELECT Substr(alr_s.x3sapid, 1, 16) FROM assetlocrelation alr_s WHERE alr_s.targetassetnum = ( SELECT a.ancestor FROM asset a WHERE a.assetnum = '<REQUEST.ASSETNUM>' ) ) ) AND cs.x4elemento = 'CAVO DI POTENZA' AND s.assetattrid = 'LIN_RIF_FASE' AND s.alnvalue = '<target>';

Se una delle due query non ha prodotto risultato, eseguire la seguente query:

SELECT c.assetnum FROM asset p join asset c ON c.ancestor = p.assetnum join classstructure cs ON cs.classstructureid = c.classstructureid WHERE p.assetnum IN ( SELECT targetassetnum FROM assetlocrelation WHERE x3sapid IN ( SELECT Substr(x3sapid, 1, 16) FROM assetlocrelation WHERE targetassetnum = ( SELECT ancestor FROM asset WHERE assetnum = '<REQUEST.ASSETNUM>' ) ) ) AND cs.x4elemento = 'CAVO DI POTENZA';

Il valore di results, se è presente, verrà usato per popolare il campo assets di dell’oggetto di risposta AssetLookupResponse.

ALGREG10 (classe AlgRegola10):

Di seguito, la query da eseguire per determinare gli asset di output:

SELECT b.assetnum AS results

FROM ASSET a, asset b

WHERE a.assetnum = '<requet.assetnum>'

AND a.ANCESTOR = b.ANCESTOR

AND b.CLASSSTRUCTUREID = '1005'; -- 1005 = ‘CONDUTTORI’

Il valore di results verrà usato per popolare il campo assets dell’oggetto di risposta AssetLookupResponse.

ALGREG11(classe AlgRegola11):

Di seguito, la query da eseguire per determinare gli asset di output:

SELECT c.assetnum AS results

FROM ASSET a, asset b, asset c

JOIN CLASSSTRUCTURE cls on c.classstructureid = cls.classstructureid

WHERE a.assetnum = '<request.assetnum>'

AND a.PARENT = b.ASSETNUM

AND b.ASSETNUM = c.parent

AND cls.X4ELEMENTO = '<elementoTarget>';

Nel caso sia stato passato all’API il parametro opzionale classStructureIdTarget verrà effettuata solo la query con il classstructureid = <request. classStructureIdTarget >

SELECT c.assetnum AS results

FROM ASSET a, asset b, asset c

WHERE a.assetnum = '<request.assetnum>'

AND a.PARENT = b.ASSETNUM

AND b.ASSETNUM = c.parent

AND c.CLASSSTRUCTUREID = '<request.classStructureIdTarget>';

Il valore di results verrà usato per popolare il campo assets dell’oggetto di risposta AssetLookupResponse.

ALGREG12 (classe AlgRegola12):

Di seguito, la query da eseguire per determinare gli asset di output:

SELECT B.ASSETNUM AS results

FROM ASSET A, ASSET B, CLASSSTRUCTURE C

WHERE A.ASSETNUM = '<request.assetnum>'

AND A.PARENT = B.PARENT

AND B.CLASSSTRUCTUREID = C.CLASSSTRUCTUREID

AND C.X4ELEMENTO = '<X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET>'

AND a.assetnum != b.assetnum;

Nel caso sia stato passato all’API il parametro opzionale classStructureIdTarget verrà effettuata la query con il classstructureid = <request. classStructureIdTarget >

SELECT b.assetnum AS results

FROM ASSET a, asset b

WHERE a.assetnum = '<request.assetnum>'

AND a.PARENT = b.ASSETNUM

AND b.ASSETNUM = c.parent

AND a.assetnum != b.assetnum

AND b.CLASSSTRUCTUREID = '<request.classStructureIdTarget>';

Il valore di results verrà usato per popolare il campo assets dell’oggetto di risposta AssetLookupResponse.

ALGREG13 (classe AlgRegola13):

Per questo algoritmo è necessario considerare diversi casi:

Se l’ELEMENTO_RIFERIMENTO e l’ELEMENTO_TARGET rientrano nei casi seguenti:

da ISOLATORE CONDUTTORE a LINEA

da STRUTTURA a LINEA

da SEZIONATORE a LINEA

da TERMINALE a LINEA

allora l’algoritmo esegue le seguente query:

SELECT alr.sourceassetnum FROM asset a JOIN assetlocrelation alr ON alr.targetassetnum = COALESCE(a.ancestor, a.assetnum) WHERE a.assetnum = <request.assetnum>;

Se l’ELEMENTO_RIFERIMENTO e l’ELEMENTO_TARGET rientrano nei casi seguenti:

da CONDUTTORE f a CAMPATA AEREA

da FUNE DI GUARDIA a CAMPATA AEREA

da CAVO DI POTENZA a TRATTA CAVO

da SEGNALAZIONE CAVO a TRATTA CAVO

da FIBRA OTTICA CAVO a TRATTA CAVO

da CAVI a TRATTA CAVO

da STALLO a STAZIONE

allora l’algoritmo esegue le seguente query:

SELECT b.assetnum FROM asset a JOIN asset b ON b.assetnum = a.ancestor WHERE a.assetnum = <request.assetnum>

Se l’ELEMENTO_RIFERIMENTO e l’ELEMENTO_TARGET rientrano nei casi seguenti:

da INTERRUTTORE AIS a SEZIONE

da INTERRUTTORE GIS a SEZIONE

da SEZIONATORE AIS a SEZIONE

allora l’algoritmo esegue le seguente query:

SELECT c.assetnum FROM asset a JOIN asset c ON c.parent = a.ancestor WHERE a.assetnum = '<request.assetnum>' AND c.classstructureid = 1554; --DA VERIFICARE

Se l’ELEMENTO_RIFERIMENTO e l’ELEMENTO_TARGET rientrano nei casi seguenti:

da STALLO a SEZIONE

allora l’algoritmo esegue le seguente query:

SELECT b.assetnum FROM asset a JOIN asset b ON b.assetnum = a.parent WHERE a.assetnum = <request.assetnum>

Se l’ELEMENTO_RIFERIMENTO e l’ELEMENTO_TARGET rientrano nei casi seguenti:

da CAMPATA AEREA a LINEA

allora l’algoritmo esegue le seguente query:

SELECT alr.sourceassetnum FROM assetlocrelation alr WHERE alr.targetassetnum = <request.assetnum>;

ALGREG14 (classe AlgRegola14):

Di seguito, la query da eseguire per determinare gli asset di output:

SELECT c.assetnum FROM asset p join asset c ON c.ancestor = p.assetnum join classstructure cs ON cs.classstructureid = c.classstructureid WHERE p.assetnum IN ( SELECT targetassetnum FROM assetlocrelation WHERE x3sapid IN ( SELECT Substr(x3sapid, 1, 16) FROM assetlocrelation WHERE targetassetnum = ( SELECT ancestor FROM asset WHERE assetnum = '<REQUEST.ASSETNUM>' ) ) ) AND cs.x4elemento = '<X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET>';

Il valore di results verrà usato per popolare il campo assets dell’oggetto di risposta AssetLookupResponse.

Al termine, il servizio costruirà il json di risposta, come riportato in alto.

Nuovi Oggetti utilizzati da AssetsLookup

l nuovi oggetti introdotti che userà l’AssetsLookup sono i seguenti:

AssetLookupRequest: oggetto per invocare il microservizio assetsLookup, contiene i parametri per estrarre gli asset correlati.

public AssetLookupRequest

{

private String assetnum;

private String siteId;

private Long ruleId;

private String classStructureIdTarget;

}

AssetLookupResponse: Oggetto di response del servizio AssetsLookup, contiene un campo esito che può valere “OK” oppure “KO”, un campo descrizione per restituire un eventuale messaggio di errore o di informazioni e la lista di asset correlati.

public AssetLookupResponse

{

private String esito;

private String descrizione;

private List<String> assets;

}

X4RuleExt: Oggetto che rappresenta le configurazioni di una regola, recuperata dal database dalle diverse tabelle e utilizzata dagli algoritmi per determinare il comportamento della ricerca degli asset correlati.

public class X4RuleExt

{

private Long ruleId;

private String algoritmo;

private String elementoRiferimento;

private String elementoTarget;

}

Modifiche DSS: algoritmi da modificare/aggiungere per applicare le regole

Algoritmo verifica policy soddisfatta

Per consentire l’implementazione delle modifiche previste dalla Wave 2.1 è necessario adeguare l’algoritmo di verifica policy soddisfatta, al fine di gestire correttamente gli input non isolivello.

Il primo intervento consiste nell’allineamento degli oggetti al nuovo modello dati.

L’oggetto Policy, utilizzato dal DSS-Worker, dovrà essere esteso con un metodo che consenta di determinare, in maniera agevole, se la policy ha condizioni riferite ad asset della stessa tipologia/livello oppure ad asset correlati tramite gerarchia, rendendo quindi possibile differenziare le logiche di valutazione.

L’algoritmo dovrà essere rivisto nella propria logica interna (Figura 1 Evaluate policy as is). Attualmente, il processo di valutazione si articola in due fasi principali: il calcolo delle singole espressioni definite dagli input e la successiva elaborazione del risultato finale in base agli esiti parziali.

L’evoluzione del calcolo policy richiede l’introduzione di una fase intermedia, in cui un metodo dedicato valuterà l’espressione sugli asset correlati.

Tale metodo, con la tripletta assetnum – rulesId (X4PolicyInput.X4_RULESID) – classStructureIdTarget (X4POLICYINPUT.CLASSSTRUCTUREID), invocherà il microservizio assetsLookup, responsabile della restituzione della lista degli asset di interesse.

In funzione della regola quantificatrice associata alla policy input e degli asset recuperati, verrà verificato se il requisito previsto dalla policy risulta soddisfatto.

Interventi richiesti:

Trova gli input su asset correlati

Per ogni input su asset correlato, chiama assetsLookup come di seguito:

List<String> connectedAssets = assetLookupService.lookupAssets(assetnum, ruleId, classificationIdTarget);

Per ogni assetnum ricevuto -> carica la consistenza, e calcola la condizione fino a che la regola quantificatrice non sia soddisfatta.

Questo adeguamento consentirà di garantire la corretta valutazione delle policy che prevedono condizioni su asset non isolivello.

Figura 1 Evaluate policy as is

Figura 2 Evaluate policy tobe 2.1

Adeguamento GUI

Configurazione

Per permettere la visione dei campi necessari al mapping regole-relazioni asset per la gestione di componenti non isoelemento, si prevede la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom X4REGOLEREL “Regole relazione asset” con oggetto principale la vista X4REGOLEREL, si dovranno configurare:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Tipologia | TIPOLOGIA |  |  |  |
| Livello | LIVELLO_RIFERIMENTO |  |  |  |
| Asset di riferimento | ELEMENTO_RIFERIMENTO |  |  |  |
| Cardinalità | CARDINALITA |  |  |  |
| Asset da cui leggere | ELEMENTO_TARGET |  |  |  |
| Livello | LIVELLO_TARGET |  |  |  |
| Regola | NAME |  |  |  |
| Descrizione regola | DESCRIPTION |  |  |  |
| Eccezione | ECCEZIONE |  |  |  |
| Catalogo | CATALOGO |  |  |  |

La vista X4REGOLEREL rappresenta una join tra le tabelle X4_TIPOLOGIE_CORRELATE e X4_RULES, effettuata sulla chiave X4_TIPOLOGIE_CORRELATEID.

La tabella “Regole relazione asset” viene posta in sola lettura tramite funzionalità standard e resa visibile solo al gruppo di protezione “trn-eam-gestionecataloghi-ese".

Per permettere la visione dei campi necessari alla gestione di policy non isoelemento si prevede il seguente adeguamento ai campi di testata del “Catalogo Policy”, inserendo il seguente flag:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Isoelemento? | ISOELEMENTO |  |  |  |

Il flag “Isoelemento?” viene posto di default in attivo (X4POLICY.ISOELEMENTO=1) tramite funzionalità standard, identificando che la policy è caratterizzata da tutti input isoelemento.

Per permettere la visione dei campi necessari alla gestione di input policy non isoelemento si prevede il seguente adeguamento alla tabella “Input Policy” del “Catalogo Policy”, inserendo i seguenti campi dopo “N° input” e prima di “Tipologia input”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Elemento asset | ELEMENTO_ASSET |  | X4ELEMENTOASSOC | X4ISOELEMENTO |
| Classificazione asset | CLASSIFICAZIONE_ASSET |  | X4CLASSASSOC | X4ISOELEMENTO |
| Regola | X4_RULESID_EXT |  | X4RULES | X4ISOELEMENTO |

La sigoption ”X4ISOELEMENTO” nasconde i campi a cui è applicata se il campo X4POLICY.X4ISOELEMENTO=1, ovvero quando tutti gli input della Policy sono isoelemento.

Valorizzazione dell’elemento asset dell’input della Policy

Nel caso di Policy con input non isoelemento (X4POLICY.ISOELEMENTO=0), il dominio tabellare dedicato alla logica di selezione dell’elemento asset dell’input della Policy deve prevedere che il campo ELEMENTO_ASSET della tabella X4POLICYINPUT sia valorizzabile solo con gli elementi asset dell’applicazione “Regole relazione asset” (ELEMENTO_TARGET di X4_TIPOLOGIE_CORRELATE) che hanno come elemento di riferimento quello definito sulla Policy (X4_TIPOLOGIE_CORRELATE.ELEMENTO_RIFERIMENTO=X4POLICY.TIPOLOGIAASSET) e valide per il Catalogo Policy (X4_TIPOLOGIE_CORRELATE.CATALOGO=’POLICY’).

La lookup custom “X4ELEMENTOASSOC” prevede la seguente colonna: Elemento asset (ELEMENTO_TARGET di X4_TIPOLOGIE_CORRELATE).

Valorizzazione della classificazione asset dell’input della Policy

Nel caso di Policy con input non isoelemento (X4POLICY.ISOELEMENTO=0), il dominio crossover dedicato alla logica di selezione della classificazione asset dell’input della Policy deve prevedere che il campo CLASSIFICAZIONE_ASSET della tabella X4POLICYINPUT sia valorizzabile solo con le classificazioni (CLASSIFICATIONID della tabella CLASSSTRUCTURE) che hanno come elemento quello definito in input alla Policy (CLASSSTRUCTURE.X4ELEMENTO=X4POLICYINPUT.ELEMENTO_ASSET).

La lookup custom “X4CLASSASSOC” prevede le seguenti colonne: Classificazione asset (CLASSIFICATIONID di CLASSSTRUCTURE), Gerarchia anagrafica associata (HIERARCHYPATH di CLASSSTRUCTURE).

Inoltre, in fase di salvataggio del record, il dominio provvede a popolare automaticamente il campo CLASSSTRUCTUREID della tabella X4POLICYINPUT con l’ID interno del record selezionato (CLASSSTRUCTUREID di CLASSSTRUCTURE). Tuttavia, tale campo non è visualizzato in maschera, in quanto destinato esclusivamente alle logiche applicative del DSS.

Valorizzazione della regola in input alla Policy

Nel caso di Policy con input non isoelemento (X4POLICY.ISOELEMENTO=0), il dominio crossover dedicato alla logica di selezione della regola in input alla Policy deve prevedere che il campo X4_RULESID_EXT della tabella X4POLICYINPUT sia valorizzabile con le regole censite nella tabella “Regole relazione asset” (NAME di X4_RULES) che hanno come elemento target quello definito in input alla Policy (X4_TIPOLOGIE_CORRELATE.ELEMENTO_TARGET=X4POLICYINPUT.ELEMENTO_ASSET), come elemento di riferimento quello definito sulla Policy (X4_TIPOLOGIE_CORRELATE.ELEMENTO_RIFERIMENTO=X4POLICY.TIPOLOGIAASSET) e valide per il Catalogo Policy (X4_TIPOLOGIE_CORRELATE.CATALOGO=’POLICY’).

La lookup custom “X4RULES” prevede le seguenti colonne: Nome della regola (NAME di X4_RULES), Descrizione della regola (DESCRIPTION di X4_RULES), Regola di applicabilità (DESCRIPTION di X4_QUANTIFICATION_RULES), Algoritmo (DESCRIPTION di X4_ALGORITMI).

Inoltre, in fase di salvataggio del record, il dominio provvede a popolare automaticamente il campo X4_RULESID della tabella X4POLICYINPUT con l’ID interno del record selezionato nell’applicazione “Regole relazione asset” (X4_RULESID di X4_RULES). Tuttavia, tale campo non è visualizzato in maschera, in quanto destinato esclusivamente alle logiche applicative del DSS.

Adeguamento della selezione della misura in input alla Policy

Nel caso di Policy con input non isoelemento (X4POLICY.ISOELEMENTO=0), il dominio tabellare dedicato alla logica di selezione della misura in input alla Policy deve prevedere che il campo IDMISURA_EXT della tabella X4POLICYINPUT sia valorizzabile con gli ID Misura del Catalogo Misure (IDMISURA di X4CATMISURE con TIPOPARAMETRO=’Misura’) associati all’elemento asset definito a livello di input della Policy (X4CATMISURE.TIPOLOGIAASSET=X4POLICYINPUT.ELEMENTO_ASSET).

Sviluppo

Per permettere la gestione di input policy non isoelemento nel Catalogo Policy, si prevedono i seguenti sviluppi a sistema:

Valorizzazione del flag “Isoelemento?”

Nel caso di modifica del flag ISOELEMENTO di X4POLICY (impostato di default pari a 1), il sistema effettua un controllo mediante automation script sulla presenza di input nella tabella dedicata (X4POLICYINPUT); se presenti, il sistema genera un messaggio di errore bloccante.

Adeguamento della modificabilità dei campi della tabella “Input Policy”

Nel caso di Policy con input non isoelemento (X4POLICY.ISOELEMENTO=0), lo script di automazione dedicato ad impostare i campi della tabella “Input Policy” in sola lettura per Policy aggregate (X4POLICY.POLICYAGGR=1) e per Policy in stato “ATTIVA” deve prevedere lo stesso controllo per i nuovi campi (ELEMENTO_ASSET, CLASSIFICAZIONE_ASSET e X4_RULESID_EXT di X4POLICYINPUT).

Adeguamento della selezione/validazione dell’input della Policy

Nel caso di Policy con input non isoelemento (X4POLICY.ISOELEMENTO=0), gli script di automazione dedicati alle logiche di valorizzazione e validazione dell’input della Policy devono prevedere che il campo IDINPUT_EXT della tabella X4POLICYINPUT, in base alla tipologia inserita, sia valorizzabile e validabile con input legati all’elemento asset definito nella tabella “Input Policy” (ELEMENTO_ASSET di X4POLICYINPUT).

Adeguamento del popolamento della descrizione dell’input della Policy

Nel caso di Policy con input non isoelemento (X4POLICY.ISOELEMENTO=0), lo script di automazione dedicato al popolamento automatico della descrizione dell’input della Policy deve prevedere che il campo DESCINPUT della tabella X4POLICYINPUT sia valorizzato con le descrizioni degli input legati all’elemento asset definito nella tabella “Input Policy” (ELEMENTO_ASSET di X4POLICYINPUT).

Adeguamento della selezione del target di raffronto dell’input della Policy

Nel caso di Policy con input non isoelemento (X4POLICY.ISOELEMENTO=0), lo script di automazione dedicato alle logiche di selezione del target di raffronto dell’input della Policy deve prevedere che il campo TARGETRAFFRONTO_EXT della tabella X4POLICYINPUT, in base all’input inserito, sia valorizzabile con target di raffronto legati all’elemento asset definito nella tabella “Input Policy” (ELEMENTO_ASSET di X4POLICYINPUT).

# RILEVABILITA’ DIFFORMITA’ E INTERFERENZE

La gestione delle difformità e interferenze non iso-elemento estende lo spettro di applicabilità delle stesse a tipologie di asset in relazione alla tipologia dell’asset su cui sono definite. L’ottica di questa estensione è quella di poter definire e rilevare difformità e interferenze su un insieme di asset costituito da asset appartenente ad una tipologia principale (ad esempio CASSETTA DI SEZIONAMENTO GIUNTI) ed asset appartenenti a tipologie correlate (ad esempio CAVO DI POTENZA e CAVI). Il sistema permetterà quindi di poter definire e rilevare difformità e interferenze definite su:

una singola tipologia di asset (come realizzato in wave 2)

una singola tipologia di asset e una o più tipologie correlate (oggetto della realizzazione della wave 2.1)

Le possibili tipologie correlate e le regole di identificazione degli asset correlati costituiscono una parte comune alle feature di progetto pertanto sono trattate in una sezione dedicata.

## Cataloghi difformità e interferenze

Descrizione del modello dati

Ad oggi i cataloghi di difformità e interferenze definiscono le regole di applicabilità e rilevabilità a livello di tipologia asset (elemento asset in schermata). La tipologia asset funge da potenziale aggregatore di più classificazioni utile per poter definire una singola configurazione di catalogo per diverse classificazioni assimilabili. Ad esempio, in anagrafica asset sono state definite diverse classificazioni di interruttori AIS (isolato in aria) che sono aggregate mediante la tipologia asset INTERRUTTORE AIS.

Figura 3. Esempio tipologia asset aggregatore

Nel caso in cui non servisse aggregare più classificazioni la tipologia asset corrisponde alla classificazione stessa.

Figura 4. Esempio tipologia asset non aggregatore

Per difformità e interferenze non iso-elemento sarà presente una tipologia principale (già presente) ed una o più tipologie correlate. Le tipologie correlate alla tipologia principale sono elencate nel MBO X4_TIPOLOGIE_CORRELATE come riportato nel paragrafo 3.3.

Sulla base di queste considerazioni i cataloghi difformità e interferenze saranno estesi secondo le logiche di seguito descritte. Innanzi tutto, gli oggetti di testata per difformità e interferenze (X4CATDIFFOR e X4CATINTERF rispettivamente) avranno l’attributo MULTILEMENTO (tipo YORN) necessario per identificare se la difformità/interferenza è iso-elemento o meno:

0 la voce di catalogo è iso-elemento (voce di catalogo realizzata in wave 2),

1 la voce di catalogo non è iso-elemento (oggetto requisito wave 2.1).

Verrà introdotto il MBO X4CATCHILD (usato sia per le difformità che per le interferenze) per legare una difformità o interferenza non iso-elemento alle tipologie asset correlate. I possibili legami saranno automaticamente popolati secondo quanto configurato nel MBO X4_TIPOLOGIE_CORRELATE e X4_RULES nel momento in cui per la difformità o interferenze impostato a viene ad 1 l’attributo MULTIELEMENTO.

Gli MBO X4DIFFORLASSIFICATIONS e X4INTERFCLASSIFICATIONS contenenti le classificazioni della tipologia principale saranno estesi con l’aggiunta dell’attributo SECONDARY al fine di contenere anche le classificazioni delle tipologie correlate:

0 la classificazione è relativa alla tipologia principale,

1 la classificazione è relativa ad una tipologia correlata.

Al popolamento della X4CATCHILD verranno popolate le tabelle delle classificazioni X4DIFFORLASSIFICATIONS e X4INTERFCLASSIFICATIONS con le classificazioni corrispondenti alle tipologie correlate. Le classificazioni saranno determinate filtrando dalla tabella CLASSSTRUCTURE secondo l’attributo X4ELEMENTO (tipologia asset).

Infine, gli MBO X4CARDIFFOR e X4CARDINTERF contenenti gli attributi di classificazione e attributi della difformità/interferenza saranno estesi con l’attributo TIPOLOGIAASSET al fine di associare l’attributo alla rispettiva tipologia asset (proveniente dalla tipologia principale o dalle tipologie correlate) e l’attributo ATTRIBUTETYPE che indica se la riga si riferisce ad un attributo di classificazione (C) oppure ad un attributo dell’asset (A).

Si riporta di seguito il nuovo data model.

Figura 5. Data model difformità/interferenze non iso-elemento

Di seguito si riporta il dettaglio del MBO X4CATCHILD

| ATTRIBUTO | DESCRIZIONE | TIPO | LUNGHEZZA | OBBLIGATORIO | VALORI |
| --- | --- | --- | --- | --- | --- |
| X4CATCHILDID | Id record gestito da Maximo | BIGINT | 19 | SI |  |
| TYPE | Identifica se il legame è per una difformità oppure una interferenza | UPPER | 20 | SI | DIFFORMITA INTERFERENZA |
| PARENTID | Id della difformità/interferenza | BIGINT | 19 | SI |  |
| TIPOLOGIAASSET | Tipologia asset | UPPER | 192 | SI |  |
| CLASSSTRUCTUREID | Id classificazione per la restrizione degli asset appartenenti alla tipologia | UPPER | 26 | NO |  |
| RULEID | Id regola | BIGINT | 19 | SI |  |

Si riporta ora un esempio di difformità non iso-elemento definita per la tipologia asset CASSETTA DI SEZIONAMENTO GIUNTI.

| X4CATDIFFOR | X4CATDIFFOR | X4CATDIFFOR | X4CATDIFFOR | X4CATDIFFOR |
| --- | --- | --- | --- | --- |
| X4CATDIFFORID | DESCRIPTION | IDDIFFORMITA | TIPOLOGIAASSET | MULTIELEMENTO |
| 413 | Malfunzionamento scaricatori sez. guaine | Diff.LC.4.2.3.10 | CASSETTA DI SEZIONAMENTO GIUNTI | 1 |

La difformità è non iso-elemento poiché l’attributo MULTIELEMENTO è uguale a 1.

Le classificazioni della difformità sono le seguenti.

| X4DIFFORLASSIFICATIONS | X4DIFFORLASSIFICATIONS | X4DIFFORLASSIFICATIONS | X4DIFFORLASSIFICATIONS |
| --- | --- | --- | --- |
| X4CATDIFFORID | CLASSIFICATIONID | CLASSSTRUCTURE.HIERARCHYPATH | SECONDARY |
| 413 | CASSETTA DI SEZIONAMENTO GIUNTI | LINEA \ ESTREMO CAVO \ GIUNTI \ CASSETTA DI SEZIONAMENTO GIUNTI | 0 |

Le regole non iso-elemento per la tipologia asset CASSETTA DI SEZIONAMENTO GIUNTI definite nella tabella delle tipologie correlate X4_TIPOLOGIE_CORRELATE sono:

| X4_TIPOLOGIE_CORRELATE | X4_TIPOLOGIE_CORRELATE | X4_TIPOLOGIE_CORRELATE | X4_TIPOLOGIE_CORRELATE | X4_TIPOLOGIE_CORRELATE | X4_TIPOLOGIE_CORRELATE |
| --- | --- | --- | --- | --- | --- |
| X4_TIPOLOGIE_CORRELATEID | X4_NAVIGATION_PATH_ID | TIPOLOGIA | X4ELEMENTO_RIFERIMENTO | X4ELEMENTO_TARGET | CATALOGO |
| 1 | 8 | LINEA IN CAVO | CASSETTA DI SEZIONAMENTO GIUNTI | CAVO DI POTENZA | DIFFORMITA |
| 2 | 8 | LINEA IN CAVO | CASSETTA DI SEZIONAMENTO GIUNTI | CAVI | DIFFORMITA |

La difformità avrà automaticamente popolati i record di collegamento per la tabella X4CATCHILD come segue:

| X4CATCHILD | X4CATCHILD | X4CATCHILD | X4CATCHILD | X4CATCHILD | X4CATCHILD |
| --- | --- | --- | --- | --- | --- |
| X4CATCHILDID | TYPE | PARENTID | TIPOLOGIAASSET | CLASSSTRUCTUREID | RULEID |
| 1 | DIFFORMITA | 413 | CAVO DI POTENZA |  | 1 |
| 2 | DIFFORMITA | 413 | CAVI |  | 2 |

Il popolamento della colonna RULEID avviene navigando dal record della tabella X4_TIPOLOGIE_CORRELATE (tramite la tipologia asset principale e la tipologia asset correlata) verso la tabella X4_RULES da cui è possibile ricavare l’id della regola (X4_RULESID). Sebbene la cardinalità fra la tabella X4_TIPOLOGIE_CORRELATE e X4_RULES è 1 ad N, per la wave 2.1 sarà di fatto presente una ed una sola corrispondenza.

Le classificazioni complessive saranno:

| X4DIFFORLASSIFICATIONS | X4DIFFORLASSIFICATIONS | X4DIFFORLASSIFICATIONS | X4DIFFORLASSIFICATIONS |
| --- | --- | --- | --- |
| X4CATDIFFORID | CLASSIFICATIONID | CLASSSTRUCTURE.HIERARCHYPATH | SECONDARY |
| 413 | CASSETTA DI SEZIONAMENTO GIUNTI | LINEA \ ESTREMO CAVO \ GIUNTI \ CASSETTA DI SEZIONAMENTO GIUNTI | 0 |
| 413 | CAVI | LINEA \ TRATTA CAVO \ CAVI | 1 |
| 413 | CAVO DI POTENZA | LINEA \ TRATTA CAVO \ CAVI \ CAVO DI POTENZA | 1 |

Le caratteristiche relative alla difformità saranno.

| X4CARDIFFOR | X4CARDIFFOR | X4CARDIFFOR | X4CARDIFFOR | X4CARDIFFOR |
| --- | --- | --- | --- | --- |
| X4CATDIFFORID | TIPOLOGIAASSET | ATTRIBUTETYPE | ATTRIBUTE | VALUE |
| 413 | CASSETTA DI SEZIONAMENTO GIUNTI | C | LIN_TIPO_SEZIONAMENTO | N |
| 413 | CASSETTA DI SEZIONAMENTO GIUNTI | C | LIN_TIPO_SEZIONAMENTO | SNS |
| 413 | CASSETTA DI SEZIONAMENTO GIUNTI | C | LIN_TIPO_SEZIONAMENTO | SS |
| 413 | CAVI | C | LIN_TIPO_POSA | C |
| 413 | CAVI | C | LIN_TIPO_POSA | G |
| 413 | CAVI | C | LIN_TIPO_POSA | I |
| 413 | CAVO DI POTENZA | C | LIN_TIPO_POSA | S |
| 413 | CAVO DI POTENZA | C | LIN_GUAINA_CAVO | A |
| 413 | CAVO DI POTENZA | C | LIN_GUAINA_CAVO | P |

Estensione supporto attributi MBO

L’elenco delle caratteristiche per difformità ed interferenze sarà esteso al fine di poter includere anche gli attributi di MBO di tipo ASSET. Pertanto, la struttura delle tabelle X4CARDIFFOR e X4CARDINTERF contenenti gli attributi di classificazione e attributi della difformità/interferenza saranno estese con l’attributo ATTRIBUTETYPE che indicherà se la riga si riferisce ad un attributo di classificazione (C) oppure ad un attributo dell’asset (A). In base al valore di ATTRIBUTETYPE, l’attributo ATTRIBUTE conterrà il nome della caratteristica (ATTRIBUTETYPE uguale a C) o il nome dell’attributo (ATTRIBUTETYPE uguale a A). Il dominio associato a ATTRIBUTE imposterà la query di selezione e la tabella sorgente in base al valore di ATTRIBUTETYPE. Allo stesso modo anche il dominio associato all’attributo VALUE imposterà la query di selezione e la tabella sorgente in base al valore di ATTRIBUTETYPE.

Nel caso in cui la riga corrisponde ad una caratteristica, le possibili caratteristiche tecniche valide per il campo ATTRIBUTE saranno ricavate dalla tabella CLASSSPEC in base alle classificazioni corrispondenti alla tipologia asset associata alla riga. Selezionata la caratteristica, i possibili valori selezionabili per il campo VALUE saranno impostati tramite un automation script di tipo Retrieve List impostando il dominio, la corrispondenza dell’attributo e la look-up in base a quanto configurato nella definizione in CLASSSPEC.

Nel caso in cui la riga corrisponde ad un attributo, i possibili attributi validi per il campo ATTRIBUTE saranno ricavati dalla tabella X4CARMBOATTR che contiene l’elenco dei possibili attributi ASSET come mostrato di seguito. Selezionato l’attributo, i possibili valori selezionabili per il campo VALUE saranno impostati tramite un automation script di tipo Retrieve List impostando il dominio, la corrispondenza dell’attributo e la look-up in base a quanto configurato nella definizione in X4CARMBOATTR.

Di seguito si riporta il dettaglio del MBO X4CARMBOATTR.

| ATTRIBUTO | DESCRIZIONE | TIPO | LUNGHEZZA | OBBLIGATORIO |
| --- | --- | --- | --- | --- |
| ATTRIBUTENAME | Nome attributo | Same as MAXATTRIBUTE.ATTRIBUTENAME | Same as MAXATTRIBUTE.ATTRIBUTENAME | SI |
| DOMAINID | Possibile dominio | Same as MAXDOMAIN.DOMAINID | Same as MAXDOMAIN.DOMAINID | SI |
| SOURCEATTRIBUTE | Nome attributo tabella di look-up | Same as MAXATTRIBUTE.ATTRIBUTENAME | Same as MAXATTRIBUTE.ATTRIBUTENAME | SI |
| LOOKUPNAME | Nome look-up da utilizzare | ALN | 192 | SI |

Adeguamento GUI

Si riportano di seguito le modifiche da apportare all’applicazione per il catalogo difformità (X4CATDIFFORMITA) e analogamente all’applicazione per il catalogo interferenze (X4CATINTERF).

Sarà esposto un flag di testata (attributo MULTILIVELLO) per marcare la difformità non iso-elemento.

Se la difformità sarà marcata non iso-elemento sarà mostrata la sezione relativa alle tipologie correlate e relative classificazioni. Nella tabella Tipologie correlate sarà disponibile la Regola che sarà selezionabile tramite look-up.

Nella tabella Caratteristiche asset sarà esposta la tipologia asset al fine di evidenziare a quale tipologia è riferita la caratteristica e il tipo dell’attributo (C indica una caratteristica e A indica un attributo).

Sviluppo

#### Introduzione contesto applicativo

In questo paragrafo saranno analizzati i flussi per la rilevabilità delle difformità al fine di evidenziare le modifiche necessarie per la valutazione della applicabilità delle difformità/interferenze (iso-elemento e non iso-elemento) ad un asset.

Il calcolo della rilevabilità di una difformità è innescato da una modifica di una misura relativa ad un asset (X4MISUREASSET) e/o un health index (AHSCORE). Gli script che analizzano le modifiche sono X4VAREVALDIFFORMITA per le misure e X4HIEVALDIFFORMITA per gli health index. Questi script, data una modifica, identificano tutte le difformità legate alla misura/health index, e avviano il calcolo della rilevabilità della difformità. Il calcolo della rilevabilità per ogni singola difformità e asset è eseguito dallo script X4DIFFOR_CALC. In particolare, vengono valutati variabili, soglie ed health index al fine di valutare le formule della difformità. Nel caso in cui almeno una formula sia vera, la difformità per lo specifico asset verrà rilevata e inserita nella tabella X4DIFFORSTAT.

Le difformità/interferenze sono oggetto di ulteriore elaborazione al fine di popolare la tabella X4RILEVAZIONIODL che contiene tutte le potenziali difformità/interferenze rilevabili dato un ordine di lavoro e un asset. Questo popolamento avviene periodicamente per mezzo della escalation X4RILEVAZIONIODL che opera sugli ordini di lavoro (WORKORDER) che rispecchiano queste condizioni:

L’asset dell’ordine di lavoro deve avere una classificazione

Lo stato dell’ordine di lavoro deve essere uno fra i seguenti stati: SCHEDULATO, ESEGUIBILE, AVVIATO, INOLTRATO, ESEGUITO PARZ, INVIATO

Nel caso l’ordine di lavoro fosse un micro-controllo lo stato dell’ordine di lavoro padre deve essere uno fra i seguenti stati: SCHEDULATO, ESEGUIBILE, ESEGUITO PARZ.

Non esistono già rilevazioni (in X4RILEVAZIONIODL) per l’ordine di lavoro e relativo asset

Il piano di lavoro associato non deve essere del tipo PRONTO INTERVENTO, ALTRE ATTIVITA.

Per ogni ordine di lavoro viene avviato lo script X4RILEVAZIONIODL che valuta tutte le possibili difformità/interferenze rilevabili per l’asset in oggetto e i suoi discendenti e le inserisce nella tabella X4RILEVAZIONIODL.

Per quanto visto, gli script che saranno impattati per l’aggiunta della valutazione della applicabilità di difformità/interferenze in base alle caratteristiche saranno:

X4DIFFOR_CALC: script per la valutazione della rilevabilità di una difformità per un asset

X4RILEVAZIONIODL: script per la valutazione delle difformità/interferenze rilevabili per un ordine di lavoro

#### Funzionalità match caratteristiche

Le funzionalità che verranno descritte in questo paragrafo permetteranno, data una difformità/interferenza (iso-elemento e non iso-elemento) e un asset, di poter valutare le caratteristiche definite rispettivamente in X4CARDDIFFOR e X4CARDINTERF e determinare se la difformità/interferenza è rilevabile. Saranno rese disponibili due funzioni così strutturate:

checkDifformita(asset, difformita) -> True \| False

checkInterferenza(asset, interferenza) -> True \| False

Nel caso di difformità/interferenza non iso-elemento le funzioni sfrutteranno il servizio di asset look-up al fine di determinare gli asset non iso-elemento necessari per la valutazione delle caratteristiche.

Di seguito si dettaglia il flusso per il check di una difformità (per le interferenze vale lo stesso flusso con le dovute sostituzioni.

#### Caricamento caratteristiche difformità

La funzione riceve in input la difformità (record tabella X4CATDIFFOR) e l’asset in esame. A partire dalla difformità, si procede al caricamento delle caratteristiche (da tabella X4CARDIFFOR) in una struttura dati così organizzata (da ora riferita come caratteristiche difformità). Il primo livello di aggregazione sarà la tipologia asset (TIPOLOGIASSET). Per ogni tipologia asset, saranno elencate le possibili caratteristiche (ATTRIBUTE). Per ogni caratteristica saranno elencati i valori possibili (VALUE).

Si riporta di seguito un esempio della struttura caratteristiche difformità che verrà caricata per la difformità.

| TIPOLOGIAASSET | RULEID | ATTRIBUTETYPE | ATTRIBUTE | VALUE |
| --- | --- | --- | --- | --- |
| CASSETTA DI SEZIONAMENTO GIUNTI |  | C | LIN_TIPO_SEZIONAMENTO | N |
| CASSETTA DI SEZIONAMENTO GIUNTI |  | C | LIN_TIPO_SEZIONAMENTO | SNS |
| CASSETTA DI SEZIONAMENTO GIUNTI |  | C | LIN_TIPO_SEZIONAMENTO | SS |
| CAVI | 1 | C | LIN_SCHEMA_POSA | P |
| CAVI | 1 | C | LIN_SCHEMA_POSA | T |
| CAVI | 1 | C | LIN_TIPO_POSA | C |
| CAVI | 1 | C | LIN_TIPO_POSA | G |
| CAVI | 1 | C | LIN_TIPO_POSA | I |
| CAVI | 1 | C | LIN_TIPO_POSA | S |
| CAVO DI POTENZA | 2 | C | LIN_GUAINA_CAVO | A |
| CAVO DI POTENZA | 2 | C | LIN_GUAINA_CAVO | P |

#### Verifica rilevabilità per asset principale

L’algoritmo esegue una prima valutazione per determinare la rilevabilità della difformità sull’asset principale. Dall’asset principale è possibile ricavare il CLASSSTRUCTUREID e di conseguenza la tipologia asset corrispondente (attraverso la tabella CLASSSTRUCTURE si fa una ricerca per CLASSSTRUCTUREID e si recupera X4ELEMENTO). A questo punto è possibile recuperare dalle caratteristiche della difformità l’elenco di caratteristiche da verificare (TIPOLOGIAASSET uguale a X4ELEMENTO). Date queste caratteristiche verranno recuperati i valori delle corrispondenti caratteristiche e/o attributi dell’asset (mediante accesso alla ASSETSPEC e/o ASSET) e verificato che tali valori rientrino nei possibili valori ammessi dalle caratteristiche della difformità. Se la verifica ha esito positivo si procede ai passi successivi, altrimenti la difformità non risulta rilevabile.

#### Verifica rilevabilità per asset correlati

Il primo step consiste nel recuperare gli asset correlati mediante il servizio di asset look-up. L’API di asset look-up richiede i seguenti parametri:

siteId: id del sito per l’asset principale

assetNum: numero dell’asset principale

ruleId: id della regola per il recupero degli asset corrispondenti alla tipologia asset correlata

classStructureIdTarget (opzionale): id classificazione relativa agli asset corrispondenti alla tipologia asset correlata

Partendo dalle tipologie asset (eccetto quella principale) presenti nella struttura contente le caratteristiche della difformità è possibile ottenere gli asset correlati tramite il servizio di asset look-up.

Ad esempio, si supponga che l’asset avente tipologia asset CASSETTA SEZIONAMENTO GIUNTI per cui si sta valutando la rilevabilità abbia:

siteId: ‘SIT01’

assetNum: ‘1003’

e che si debbano recuperare gli asset per la tipologia correlata CAVI. Sarà possibile recuperare l’id della regola dalla tabella X4CATCHILD, quindi ad esempio:

ruleId: 1

A questo punto è possibile verificare se sulle tipologie correlate è presente una classificazione associata accedendo al record corrispondente in X4CATCHILD. Si supponga ad esempio che:

classStructureIdTarget: NULL

A questo punto si dispone di tutti i parametri per invocare il servizio di asset look-up:

siteId: ‘SIT01’

assetNum: ‘1003’

ruleId: 1

classStructureIdTarget: NULL

La chiamata al servizio ritornerà la lista degli asset correlati secondo quanto descritto nel paragrafo 4.2.

Per ciascun asset correlato si procederà alla verifica delle caratteristiche come descritto nello step precedente. Per gli asset correlati sarà necessario applicare la regola di applicabilità (che sarà possibile ricavare dalla X4_RULES) come segue:

ALMENO 1: almeno un asset deve soddisfare la verifica delle caratteristiche

1: un solo asset deve soddisfare la verifica delle caratteristiche

TUTTI: tutti gli asset devono soddisfare la verifica delle caratteristiche

NESSUNO: nessun asset deve soddisfare la verifica delle caratteristiche

#### Esempio funzionalità match caratteristiche

Si ripercorre un esempio di match caratteristiche prendendo ad esempio la seguente difformità non iso-elemento Diff.LC.4.2.3.10 definita per la tipologia asset CASSETTA DI SEZIONAMENTO GIUNTI.

| X4CATDIFFOR | X4CATDIFFOR | X4CATDIFFOR | X4CATDIFFOR | X4CATDIFFOR |
| --- | --- | --- | --- | --- |
| X4CATDIFFORID | DESCRIPTION | IDDIFFORMITA | TIPOLOGIAASSET | MULTIELEMENTO |
| 413 | Malfunzionamento scaricatori sez. guaine | Diff.LC.4.2.3.10 | CASSETTA DI SEZIONAMENTO GIUNTI | 1 |

Le tipologie associate sono.

| X4CATCHILD | X4CATCHILD | X4CATCHILD | X4CATCHILD | X4CATCHILD | X4CATCHILD |
| --- | --- | --- | --- | --- | --- |
| X4CATCHILDID | PARENTID | TYPE | TIPOLOGIAASSET | CLASSSTRUCTURE | RULEID |
| 1 | 413 | DIFFORMITA | CAVI |  | 1 |
| 1 | 413 | DIFFORMITA | CAVO DI POTENZA |  | 2 |

Le classificazioni della difformità sono:

| X4DIFFORLASSIFICATION | X4DIFFORLASSIFICATION | X4DIFFORLASSIFICATION | X4DIFFORLASSIFICATION |
| --- | --- | --- | --- |
| X4CATDIFFORID | CLASSIFICATIONID | CLASSSTRUCTURE.HIERARCHYPATH | SECONDARY |
| 413 | CASSETTA DI SEZIONAMENTO GIUNTI | LINEA \ ESTREMO CAVO \ GIUNTI \ CASSETTA DI SEZIONAMENTO GIUNTI | 0 |
| 413 | CAVI | LINEA \ TRATTA CAVO \ CAVI | 1 |
| 413 | CAVO DI POTENZA | LINEA \ TRATTA CAVO \ CAVI \ CAVO DI POTENZA | 1 |

Le caratteristiche relative alla difformità sono:

| X4CARDIFFOR | X4CARDIFFOR | X4CARDIFFOR | X4CARDIFFOR | X4CARDIFFOR |
| --- | --- | --- | --- | --- |
| X4CATDIFFORID | TIPOLOGIAASSET | ATTRIBUTETYPE | ATTRIBUTE | VALUE |
| 413 | CASSETTA DI SEZIONAMENTO GIUNTI | C | LIN_TIPO_SEZIONAMENTO | N |
| 413 | CASSETTA DI SEZIONAMENTO GIUNTI | C | LIN_TIPO_SEZIONAMENTO | SNS |
| 413 | CASSETTA DI SEZIONAMENTO GIUNTI | C | LIN_TIPO_SEZIONAMENTO | SS |
| 413 | CAVI | C | LIN_SCHEMA_POSA | P |
| 413 | CAVI | C | LIN_SCHEMA_POSA | T |
| 413 | CAVI | C | LIN_TIPO_POSA | C |
| 413 | CAVI | C | LIN_TIPO_POSA | G |
| 413 | CAVI | C | LIN_TIPO_POSA | I |
| 413 | CAVI | C | LIN_TIPO_POSA | S |
| 413 | CAVO DI POTENZA | C | LIN_GUAINA_CAVO | A |
| 413 | CAVO DI POTENZA | C | LIN_GUAINA_CAVO | P |

Struttura asset

Si riporta la seguente struttura asset che verrà esercitata nell’esempio.

| ASSETNUM | PARENT | CLASSSTRUCTURE.HIERARCHYPATH | CLASSSTRUCTURE. CLASSSTRUCTUREID | CLASSSTRUCTURE.X4ELEMENTO |
| --- | --- | --- | --- | --- |
| 1000 |  | LINEA | 1003 | LINEA |
| 1001 | 1000 | LINEA \ ESTREMO CAVO | 1026 | ESTREMO CAVO |
| 1002 | 1001 | LINEA \ ESTREMO CAVO \ GIUNTI | 1027 | GIUNTI |
| 1003 | 1002 | LINEA \ ESTREMO CAVO \ GIUNTI \ CASSETTA DI SEZIONAMENTO GIUNTI | 1029 | CASSETTA DI SEZIONAMENTO GIUNTI |
| 1004 | 1000 | LINEA \ TRATTA CAVO | 1050 | TRATTA CAVO |
| 1005 | 1004 | LINEA \ TRATTA CAVO \ CAVI | 1051 | CAVI |
| 1006 | 1005 | LINEA \ TRATTA CAVO \ CAVI \ CAVO DI POTENZA | 1052 | CAVO DI POTENZA |
| 1007 | 1005 | LINEA \ TRATTA CAVO \ CAVI \ CAVO DI POTENZA | 1052 | CAVO DI POTENZA |
| 1008 | 1005 | LINEA \ TRATTA CAVO \ CAVI \ CAVO DI POTENZA | 1052 | CAVO DI POTENZA |

Caricamento dati di contesto della difformità

| TIPOLOGIAASSET | RULEID | ATTRIBUTETYPE | ATTRIBUTE | VALUE |
| --- | --- | --- | --- | --- |
| CASSETTA DI SEZIONAMENTO GIUNTI |  | C | LIN_TIPO_SEZIONAMENTO | N |
| CASSETTA DI SEZIONAMENTO GIUNTI |  | C | LIN_TIPO_SEZIONAMENTO | SNS |
| CASSETTA DI SEZIONAMENTO GIUNTI |  | C | LIN_TIPO_SEZIONAMENTO | SS |
| CAVI | 1 | C | LIN_SCHEMA_POSA | P |
| CAVI | 1 | C | LIN_SCHEMA_POSA | T |
| CAVI | 1 | C | LIN_TIPO_POSA | C |
| CAVI | 1 | C | LIN_TIPO_POSA | G |
| CAVI | 1 | C | LIN_TIPO_POSA | I |
| CAVI | 1 | C | LIN_TIPO_POSA | S |
| CAVO DI POTENZA | 2 | C | LIN_GUAINA_CAVO | A |
| CAVO DI POTENZA | 2 | C | LIN_GUAINA_CAVO | P |

Verifica rilevabilità per asset principale

L’algoritmo esegue una prima valutazione volta a determinare la rilevabilità della difformità Diff.LC.4.2.3.10 sull’asset principale 1003.

boolean checkDifformita(“1003”, “Diff.LC.4.2.3.10”)

Dall’asset principale 1003 è possibile determinare il CLASSSTRUCTUREID 1029, risalire alla tipologia asset CASSETTA SEZIONAMENTO GIUNTI tramite tabella CLASSSTRUCTURE e ricavare dalle caratteristiche della difformità tutte le caratteristiche per cui effettuare la verifica (LIN_TIPO_SEZIONAMENTO).

| TIPOLOGIAASSET | RULEID | ATTRIBUTETYPE | ATTRIBUTE | VALUE |
| --- | --- | --- | --- | --- |
| CASSETTA DI SEZIONAMENTO GIUNTI |  | C | LIN_TIPO_SEZIONAMENTO | N |
| CASSETTA DI SEZIONAMENTO GIUNTI |  | C | LIN_TIPO_SEZIONAMENTO | SNS |
| CASSETTA DI SEZIONAMENTO GIUNTI |  | C | LIN_TIPO_SEZIONAMENTO | SS |

Date queste caratteristiche verranno recuperati i valori delle stesse per l’asset (mediante accesso alla ASSETSPEC).

| ASSETNUM | ASSETATTRID | TABLEVALUE, NUMVALUE, ALNVALUE, DATEVALUE |
| --- | --- | --- |
| 1003 | LIN_TIPO_SEZIONAMENTO | N |
| 1003 | LIN_FUSO_RIFERIMENTO |  |
| 1003 | LIN_COORDINATA_EST |  |
| 1003 | LIN_COORDINATA_NORD |  |

Verrà eseguita la verifica che tali valori (N) rientrano nei valori definiti (N, SS, SNS) nelle caratteristiche della difformità. Se la verifica ha esito positivo si procede ai passi successivi, altrimenti la difformità non risulta rilevabile.

Verifica rilevabilità per asset correlati

Il recupero degli asset correlati avviene recuperando le tipologie asset correlate dalle caratteristiche della difformità. A questo punto sarà possibile ottenere tutti gli asset correlati tramite il servizio di asset look-up.

assetLookUp(“SIT01”, “1003”, 1, NULL) -> [“1005”]

assetLookUp(“SIT01”, “1003”, 2, NULL) -> [“1006”, “1007”, “1008”]

Per ciascun asset correlato (1005 e 1006, 1007 e 1008) si procederà alla verifica delle caratteristiche come descritto nello step precedente. Per gli asset correlati sarà inoltre necessario risolvere la regola di applicabilità impostata in X4_CATCHILD. Nella ipotesi che le regole di applicabilità per le tipologie correlate sia ALMENO 1, per la CASSETTA DI SEZIONAMENTO avendo un unico asset (1005) si verificherà che questo asset soddisfi le caratteristiche. Per i CAVI DI POTENZA sono disponibili tre asset (1006, 1007 e 1008) pertanto si verificherà che almeno 1 di questi asset soddisfi le caratteristiche della difformità.

## Catalogo attività

Introduzione

Il catalogo delle attività è l’applicazione che associa, fra l’altro, ad ogni attività tutte le difformità ed interferenze che possono essere potenzialmente rilevate in un ordine di lavoro. Attualmente le difformità/interferenze identificate e caricate in Maximo sono legate alla tipologia dell’asset specificata nella attività.

Ad esempio, un’attività la cui tipologia asset è CAMPATA AEREA può essere configurata con difformità e interferenze definite per le seguenti tipologie asset:

CAMPATA AEREA

CONDUTTORE

FUNE DI GUARDIA

ISOLATORE CONDUTTORE

SEZIONATORE

STRUTTURA

Le interferenze e difformità rilevabili possono essere aggiunte nella applicazione Catalogo attività nel tab Rilevazioni nelle tabelle Interferenze Rilevabili e Difformità Rilevabili. L'oggetto Maximo su cui si appoggiano entrambe le tabelle è l’MBO X3CATASSOCIAZIONE. L’attributo TIPO determina se l’associazione è per una difformità (TIPO = ‘D’) o una interferenza (TIPO = ‘I’).

Le possibili interferenze che si possono impostare nell’attributo ID Interferenza (IDINTERFERENZA) della tabella Interferenze Rilevabili sono vincolate dal dominio X4INTERFASSOC. Analogamente il campo ID Difformità (IDDIFFORMITA) della tabella Difformità Rilevabili utilizza il dominio X4DIFFERASSOC.

I due domini utilizzano clausole molto articolate, che a seconda della tipologia asset dell’attività (JOBPLAN.X4TIPOLOGIAASSET) o della stessa tabella (X3CATASSOCIAZIONE.TIPOLOGIAASSET) filtra i possibili record in maniera differente. Ad esempio, nel caso di CAMPATA AEREA mostra solo le Interferenze (Difformità) relative alla campata e a tutti i figli gerarchici di CAMPATA AEREA o SOSTEGNO AEREO, mentre nel caso di TRATTA CAVO espone solo i dati di TRATTA CAVO e dei figli di TRATTA CAVO ed ESTREMO CAVO.

Nell’ottica di semplificare le possibili tipologie asset correlabili verrà introdotta una tabella di verità. Le possibili tipologie asset saranno derivate da questa tabella in funzione della tipologia asset della attività

La tabella di verità sarà caricata ed eventualmente aggiornata in futuro mediante file di caricamento. Con l'adozione della tabella si prevedono circa 5000 associazioni.

Il nuovo Catalogo Attività introduce inoltre il concetto di Gravità della rilevazione, ossia che sia possibile generare un’Interferenza/Difformità da una Attività solo in caso di determinate situazioni di gravità.

Ad esempio, la difformità Diff.L.1.1.1.3 - Isolatori Sporchi può avere gravità Lieve, Medio, Grave ed è possibile che una certa attività possa rilevare questa interferenza solo per alcuni di questi casi.

Il progetto introdurrà quindi il concetto di Gravità come valore selezionabile all’atto dell’inserimento di una nuova Rilevabilità nell’applicazione Catalogo Attività.

Come conseguenza si rende necessario intervenire anche sull’applicazione OdL: l’operatore che intende rilevare un’Interferenza/Difformità potrà farlo solo per le Gravità consentite, per cui la Gravità sarà resa obbligatoria nei casi in cui essa sia stata specificata nel Catalogo Attività.

Modello dati

Verrà introdotta la tabella X4CATASSOCVERITA (sia per le difformità che per le interferenze) che assocerà le tipologie asset a tipologie correlate. Questo oggetto utilizzerà come campi chiave per il mapping il concetto di elemento, presente sia nelle Attività che nei cataloghi di Interferenze/Difformità come X4TIPOLOGIAASSET, inteso come aggregatore di classificazioni asset.

Seguono gli elementi chiave identificati:

Identificativo univoco della tabella

L’indicazione che il target della relazione sia una interferenza (I) o una difformità (D)

La tipologia asset dell’attività

La tipologia asset della interferenza/difformità

Di seguito si riporta il dettaglio del MBO X4CATASSOCVERITA.

| ATTRIBUTO | DESCRIZIONE | TIPO | LUNGHEZZA | OBBLIGATORIO | VALORI | CHIAVE PRIMARIA |
| --- | --- | --- | --- | --- | --- | --- |
| X4CATASSOCVERITAID | Id record gestito da Maximo | BIGINT | 19 | SI |  |  |
| TIPOLOGIAORIGINE | Tipologia asset origine | UPPER | 65 | SI |  | 1 |
| TIPOLOGIACORRELATA | Tipologia asset correlata | UPPER | 65 | SI |  | 2 |
| TIPO | Identifica se il legame è per una difformità oppure una interferenza | UPPER | 1 | SI | D, I | 3 |

Si riporta ora un esempio di tabella definita per alcune tipologie.

| X4CATASSOCVERITA | X4CATASSOCVERITA | X4CATASSOCVERITA | X4CATASSOCVERITA |  |
| --- | --- | --- | --- | --- |
| X4CATASSOCVERITAID | TIPO | TIPOLOGIAORIGINE | TIPOLOGIACORRELATA | TIPOLOGIACORRELATA |
| 1001 | I | CAMPATA AEREA | SOSTEGNO AEREO | SOSTEGNO AEREO |
| 1002 | I | CAMPATA AEREA | CONDUTTORI | CONDUTTORI |
| 1003 | I | CAMPATA AEREA | TELAIO | TELAIO |
| 1004 | I | CONDUTTORI | ISOLAMENTO CONDUTTORE | ISOLAMENTO CONDUTTORE |

Al dominio viene aggiunto nativamente la tipologia asset dell’attività, in quanto si dà per inteso che per un’attività si possano rilevare interferenze/difformità sulla sua tipologia asset. Ad esempio, per un’attività avente come tipologia asset CAMPATA AEREA, le possibili interferenze selezionabili avranno come tipologia asset CAMPATA AEREA, SOSTEGNO AEREO, CONDUTTORI e TELAIO.

Il popolamento iniziale della tabella X4CATASSOCVERITA si baserà sui seguenti punti:

Estrazione delle tipologie asset correlate utilizzando la clausola dei domini X4INTERFASSOC e X4DIFFORASSOC

Caricamento delle tipologie asset correlate introdotte dalla gestione non iso-elemento presenti nella tabella X4_TIPOLOGIE_CORRELATE filtrando per CATALOGO = ‘ATTIVITA’.

Per l’introduzione della Gravità si provvederà a modificare le sezioni Interferenze Rilevabili e Difformità Rilevabili come da figura, inserendo il nuovo campo e corredandolo di menu a tendina che mostrerà le possibili gravità dell’Interferenza/Difformità come da definizione nel Catalogo relativo:

** inserire controllo errori:

Non si può sbiancare una gravità se esiste un’altra riga con lo stesso oggetto rilevabile

Se si crea una riga che completa tutte le gravità possibili, avvisare: si deve eliminare tutte le righe tranne una, e togliervi la gravità

Valutare una flag “Tutte le gravità” perchè l’utente confermi l’intenzione

Sviluppo

Tabella di associazione

Si prevede la creazione del MBO X4CATASSOCVERITA con gli attributi indicati in precedenza, rispettando la tipologia e la dimensione suggerita e impostandoli tutti di tipo PERSISTENT.

Per gli attributi TIPOLOGIAORIGINE e TIPOLOGIACORRELATA si imposteranno:

SAMEASOBJECT: CLASSSTRUCTURE

SAMEASATTRIBUTE: X4ELEMENTO

Saranno impostati i seguenti domini dei valori:

Per il campo TIPO, il dominio X4CATASSTIPO di tipo ALNVALUE, contenente i valori I per le interferenze e D per le difformità.

Per il campo TIPOLOGIAORIGINE, il dominio X4CATASSTIPOLOGIA di tipo TABLE contenente tutte i possibili valori di X4ELEMENTO presenti nella tabella CLASSSTRUCTURE

Per il campo TIPOLOGIACORRELATA, il dominio X4CATASSTIPOLOGIA

Catalogo Attività

Si prevede di modificare il dominio X4INTERFASSOC per la selezione delle interferenze con tipologia asset compatibile alla tipologia asset della attività in base a quanto definito nella tabella X4CATASSOCVERITA.

La nuova clausola sarà:

TIPOLOGIAASSET = :JOBPLAN.X4TIPOLOGIAASSET or TIPOLOGIAASSET in (select TIPOLOGIACORRELATA from X4CATASSOCVERITA where TIPO = 'I' and TIPOLOGIAORIGINE = :JOBPLAN.X4TIPOLOGIAASSET)

Analogamente si prevede di modificare il dominio X4DIFFORFASSOC per la selezione delle difformità con tipologia asset compatibile alla tipologia asset della attività in base a quanto definito nella tabella X4CATASSOCVERITA.

La nuova clausola sarà:

TIPOLOGIAASSET = :JOBPLAN.X4TIPOLOGIAASSET or TIPOLOGIAASSET in ( select TIPOLOGIACORRELATA from X4CATASSOCVERITA where TIPO = 'D' and TIPOLOGIAORIGINE = :JOBPLAN.X4TIPOLOGIAASSET)

Per l’introduzione della Gravità sarà necessario creare un nuovo attributo GRAVITA nel mbo X3CATASSOCIAZIONE. L’attributo sarà creato con sameasobject X4TARGETDIFFOR e sameasattribute VALUE.

Si appoggerà ad un dominio di tipo TABLE con la seguente clausola:

TIPO = ’D’ and

VALUE in (

select td.value

from X4TARGETDIFFOR td

inner join x4catdiffor cd

on td.x4catdifforid = cd.x4catdifforid

where iddifformita = :IDORIGINE

) or

TIPO = ’I’ and

VALUE in (

select ti.value

from X4TARGETINTERF ti

inner join x4catinterf ci

on ti.x4catinterfid = ci.x4catinterfid

where idinterferenza = :IDORIGINE

)

Le tabelle dell’applicazione Catalogo Attività saranno modificate come da figura nel paragrafo precedente, introducendo il nuovo campo ed il relativo menu di scelta.

## Ordini di lavoro

In questo paragrafo verranno trattate le modifiche necessarie all’applicazione Ordini di Lavoro in merito alla rilevabilità di difformità e interferenze.

Introduzione

Un ordine di lavoro è caratterizzato dalla attività (JOBPLAN) che contiene le possibili difformità e interferenze rilevabili per la tipologia asset principale della attività. Inoltre, l’ordine di lavoro è associato ad un asset principale su cui si sta eseguendo l’attività. Nell’ordine di lavoro deve essere possibile rilevare le difformità/interferenze relative all’asset dell’ordine di lavoro e potenzialmente ad asset correlati. Le difformità sono censite nella tabella X4DIFFOR_WO e le interferenze nella tabella X4INTERF_WO. Entrambe le tabelle identificano l’asset mediante gli attributi SITEID e ASSETNUM. È possibile selezionare l’asset mediante una look-up che propone gli asset possibili per la rilevabilità. A seguito della selezione dell’asset occorre selezionare la difformità/interferenza che verrà registrata nell’attributo IDDIFFORMITA/IDINTERFERENZA. La selezione della difformità/interferenza avviene mediante look-up che propone le possibili difformità/interferenze rilevabili per l’asset impostato.

#### Difformità

Per quanto riguarda le difformità rilevabili, la selezione dell’asset avviene tramite l’automation script X4DOMAINDIFF (con opportuno launch point) che in base alla tipologia dell’ordine di lavoro determina il dominio da applicare:

X4PERIMETROINT = ‘PRONTO INTERVENTO’: vengono recuperati gli asset discendenti dell’ordine di lavoro unitamente all’asset dell’ordine di lavoro.

X4PERIMETROINT != ‘PRONTO INTERVENTO’: vengono recuperati gli asset elencati nella tabella X4RILEVAZIONIODL per cui è possibile applicare una difformità.

La selezione della difformità avviene tramite l’automation script X4DOMAINDIFF (con opportuno launch point) che recupera le possibili difformità secondo la seguente logica:

X4PERIMETROINT = ‘PRONTO INTERVENTO’: vengono recuperate tutte le difformità in stato ATTIVO per la tipologia dell’asset.

X4PERIMETROINT != ‘PRONTO INTERVENTO’: viene utilizzato il dominio X4DIFFOR_WO che seleziona tutte le difformità elencate nella tabella X4RILEVAZIONIODL per l’asset in questione.

#### Interferenze

Per quanto riguarda le interferenze rilevabili, la selezione dell’asset avviene tramite il dominio X4ASSETINTWO che recupera gli asset elencati nella tabella X4RILEVAZIONIODL per cui è possibile applicare una interferenza.

La selezione della interferenza avviene tramite il dominio X4INTERF_WO che recupera le possibili interferenze elencate nella tabella X4RILEVAZIONIODL per l’asset in questione.

#### Gravità

Nell’applicazione Ordini di Lavoro, la selezione della gravità per Interferenze/Difformità avverrà rispettivamente tramite i domini X4TARGETINTERF_WO e X4TARGETDIF_WO che verranno modificati per recuperare da X3CATASSOCIAZIONE tutte le gravità relative alla rilevazione che si sta creando.

In caso la gravità non è definita, i domini restituiranno tutti i valori possibili per quell’Interferenza/Difformità.

Sarà necessario intervenire inoltre sul comportamento del campo Gravità, che sarà reso obbligatorio in modo condizionale:

Sarà obbligatorio se il campo Difformità (o Interferenza) è compilato ed è presente una distinzione di gravità nel Catalogo Attività per quell’oggetto,

Sarà non obbligatorio in tutti gli altri casi.

** l’operatore deve tornare indietro se non trova la gravità corretta

Sviluppo

Le logiche di recupero degli asset su cui è possibile rilevare una difformità/interferenza non devono essere alterate in quanto l’elenco dei possibili asset (nel caso ordine di lavoro non PRONTO INTERVENTO) sono prodotti dallo script X4RILEVAZIONIODL che terrà conto delle logiche di rilevabilità sia iso-elemento che non iso-elemento.

Per quanto riguarda la selezione delle difformità nel caso ordine di lavoro PRONTO INTERVENTO occorre estendere la logica di estrazione per verificare la rilevabilità delle difformità estratte. Il dominio che estrae le possibili difformità è X4CATDIFFOR così definito

status='ATTIVO' and tipologiaasset in (select x4elemento from classstructure where classstructureid=:ASSET.classstructureid)

Per le difformità selezionate tramite questa query occorre verificarne la rilevabilità sia nel caso iso-elemento che non iso-elemento. Pertanto, il dominio verrà trasformato in un automation script del tipo retrieveList che itera sulle difformità prodotte dalla query, ne verifica la rilevabilità mediante la funzione checkDifformita e produce la lista delle difformità rilevabili.

Per quanto riguarda la selezione delle interferenze occorre estendere la logica di estrazione per verificare la rilevabilità delle interferenze estratte. Il dominio che estrae le possibili difformità è X4INTERF_WO così definito

WORKORDERID IN (SELECT WORKORDERID FROM WORKORDER WHERE WONUM =:WONUM) AND ASSETNUM=:ASSETNUM AND TIPO='INT'

Per le interferenze selezionate tramite questa query occorre verificarne la rilevabilità sia nel caso iso-elemento che non iso-elemento. Pertanto, il dominio verrà trasformato in un automation script del tipo retrieveList che itera sulle interferenze prodotte dalla query, ne verifica la rilevabilità mediante la funzione checkInterferenze e produce la lista delle interferenze rilevabili.

Relativamente alla Gravità, la Where Clause del dominio X4TARGETDIF_WO verrà sostituita con la seguente:

X4CATDIFFORID = :X4CATDIFFORMITA.X4CATDIFFORID and

GRAVITA in (

select GRAVITA from X3CATASSOCIAZIONE

where IDDIFFORMITA=:IDDIFFORMITA

and IDORIGINE=:WORKORDER.JPNUM

)

Analogamente la Where Clause del dominio X4TARGETINTERF_WO sarà la seguente:

X4CATINTERFID = :X4CATINTERFERENZA.X4CATINTERFID and

GRAVITA in (

select GRAVITA from X3CATASSOCIAZIONE

where IDINTERFERENZA=:IDINTERFERENZA

and IDORIGINE=:WORKORDER.JPNUM

)

** modifica gui per introdurre gravità obbligatoria **

## Caricamento cataloghi

Difformità e interferenze

#### Introduzione

Il caricamento dei cataloghi delle caratteristiche delle difformità e interferenze segue il processo descritto nella figura seguente.

Cataloghi difformità e interferenze: documenti in formato MS Excel con struttura predefinita concordata con il business per la configurazione delle caratteristiche di difformità e interferenze.

Fogli di caricamento MXLoader: questi fogli MS Excel (con funzionalità MXLoader) saranno prodotti da RTI attingendo dai cataloghi prodotti dal business. La struttura dei due formati è similare per consentire un ribaltamento semplificato

Tabelle di staging: tabelle su schema Maximo per il caricamento dei dati dai fogli di caricamento MXLoader

Script di caricamento: procedono alla elaborazione dei dati delle tabelle di staging per riportare le configurazioni nei cataloghi

La struttura dei cataloghi difformità e interferenze è illustrata nell’esempio seguente (ovviamente in base alle varie difformità la struttura sarà adeguata e fornita in un foglio dedicato).

|  | CAVO DI POTENZA | CAVO DI POTENZA | CAVO DI POTENZA | CAVO DI POTENZA | CAVI | CAVI | CAVI | CAVI |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
|  | LIN_TIPO_SEZIONAMENTO | LIN_TIPO_SEZIONAMENTO | LIN_TIPO_SEZIONAMENTO | LIN_TIPO_SEZIONAMENTO | LIN_TIPO_POSA | LIN_TIPO_POSA | LIN_TIPO_POSA | LIN_TIPO_POSA |
| ID DIFFORMITA | O | C | X | E | I | C | G | S |
| Diff.LC.4.2.3.10 | x | x | x | x | x | x | x | x |
| Diff.LC.4.2.3.11 | x | x | x | x | x | x | x |  |
| Diff.LC.4.2.3.12 | x | x | x | x | x | x | x | x |

L’area bianca verrà popolata dall’utente. L’ID della difformità è indicato nella prima colonna (in corrispondenza della cella azzurra). A seguire sono presenti le caratteristiche (celle arancioni) e tutti i possibili valori corrispondenti alla caratteristica (celle verdi). L’utente potrà indicare con la lettera X se il valore della caratteristica deve inserito, altrimenti potrà lasciare la cella non valorizzata (o in alternativa valorizzata con NP). Per comodità sono indicate anche le tipologie asset corrispondenti alla caratteristica (celle rosa). Le celle colorate rappresentano la struttura del foglio e verrà concordata in modo definitivo. L’utente potrà durante la fase di compilazione variare la parte bianca.

Le celle valorizzate con X costituiscono informazioni rilevanti ai fini applicativi e verranno trasformate in caratteristiche da caricare rispettivamente nelle tabelle X4CARDIFFOR o X4CARINTERFER, in funzione della tipologia di catalogo.

Questa rappresentazione consente al business di gestire i cataloghi in forma compatta e leggibile, demandando al processo di caricamento la normalizzazione e la trasformazione dei dati nel modello relazionale applicativo.

Questi file saranno copiati nel file di caricamento MXLoader (area azzurra). Gli attributi corrispondenti sono già presenti nel file di caricamento. L’area rosa dovrà essere compilata manualmente in base al catalogo.

| IDCATALOGO | ATTR1 | ATTR2 | ATTR3 | ATTR4 | ATTR5 | ATTR6 | ATTR7 | ATTR8 | CATALOGO | SHEET |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Diff.LC.4.2.3.10 | x | x | x | x | x | x | x | x | D | Giunto |
| Diff.LC.4.2.3.11 | x | x | x | x | x | x | x |  | D | Giunto |
| Diff.LC.4.2.3.12 | x | x | x | x | x | x | x | x | D | Giunto |

#### Metodo di compilazione cataloghi

I cataloghi che verranno forniti al business per la compilazione avranno la struttura già definita in wave 2 allo scopo di mantenere la baseline aggiornata. Poiché lo scopo della wave 2.1 è il caricamento delle caratteristiche relative a difformità/interferenze già presenti a sistema, si richiede la compilazione dei cataloghi limitata a particolari colonne, mentre le altre devono restare invariate.

Le colonne oggetto della compilazione saranno quelle relative alle caratteristiche per le quali la struttura prevede una o più righe per difformità/interferenza. Solo la prima riga verrà considerata (colore rosa). Eventuali altre righe (colore rosso) saranno ignorate. Pertanto, in caso di discrepanza fra le righe, la prima riga sarà il riferimento. Per le altre colonne ci si aspetta in generale una cella unica o celle raggruppate. Fra queste riveste importanza fondamentale la colonna ID Difformità/ID interferenza (colore azzurro) che deve essere raggruppata. Le altre celle potranno essere raggruppate (ad esempio Gravità, ID Misura, Logica generazione difformità, Tipologia) o meno (ad esempio Descrizione difformità) ma non sono rilevanti per il caricamento (colore grigio).

#### Tabella di staging e caricamento iniziale

I fogli Excel utilizzati come sorgente possono differire sia per tipologia sia per numerosità delle caratteristiche gestite. Al fine di gestire tale variabilità dei cataloghi, verrà usata la tabella di staging X4STAGINGCATCARAT, contenente colonne generiche (ATTR1, ATTR2, …, ATTRn) che ospiteranno i dati provenienti dai diversi cataloghi.

Oltre alle colonne dedicate aggi attributi, la tabella avrà le seguenti colonne:

SHEET: nome del foglio Excel di origine

IDCATALOGO: identificativo della difformità o interferenza

CATALOGO: identificativo del catalogo che può assumere i valori (D o I)

ENTERDATE: data aggiornamento dato

PROCESSDATE: data processamento data

| IDCATALOGO | CATALOGO | ATTR1 | ATTR2 | ATTR3 | ATTR4 | ATTR5 | ATTR6 | ATTR7 | ATTR8 | SHEET |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Diff.LC.4.2.3.10 | D | x | x | x | x | x | x | x | x | Giunto |
| Diff.LC.4.2.3.11 | D | x | x | x | x | x | x | x |  | Giunto |
| Diff.LC.4.2.3.12 | D | x | x | x | x | x | x | x | x | Giunto |

Il caricamento dei dati nella tabella di staging avviene tramite MXLoader. Durante tale fase, i campi ATTRn vengono popolati con i valori presenti nei file sorgente (ad esempio “X”, “NP” o valore nullo), mantenendo la codifica originale. Il nome del foglio Excel di provenienza (campo SHEET) e l’identificativo del catalogo (CATALOGO) dovranno essere esplicitamente configurati nell’MXLoader.

Sulla base del nome del foglio Excel SHEET e della tipologia di catalogo CATALOGO, vengono definite specifiche regole di mapping nella tabella X4STAGINGCATCARATRULE, le quali consentono di determinare, per ciascun campo ATTR:

la caratteristica applicativa corrispondente;

il valore associato;

l’elemento asset di riferimento.

| SHEET | CATALOGO | COLONNA | TIPOLOGIAASSET | CARATTERISTICA | VALORE |
| --- | --- | --- | --- | --- | --- |
| Giunto | D | ATTR1 | CAVO DI POTENZA | LIN_TIPO_SEZIONAMENTO | O |
| Giunto | D | ATTR2 | CAVO DI POTENZA | LIN_TIPO_SEZIONAMENTO | C |
| Giunto | D | ATTR3 | CAVO DI POTENZA | LIN_TIPO_SEZIONAMENTO | X |
| Giunto | D | ATTR4 | CAVO DI POTENZA | LIN_TIPO_SEZIONAMENTO | E |
| Giunto | D | ATTR5 | CAVI | LIN_TIPO_POSA | I |
| Giunto | D | ATTR6 | CAVI | LIN_TIPO_POSA | C |
| Giunto | D | ATTR7 | CAVI | LIN_TIPO_POSA | G |
| Giunto | D | ATTR8 | CAVI | LIN_TIPO_POSA | S |

#### Aggiornamento cataloghi

Il caricamento nelle tabelle applicative X4CARDIFFOR e X4CARINTERFER è eseguito dall’automation script X4CATCARATUPLOAD. Lo script seleziona dalla tabella di staging X4STAGINGCATCARAT esclusivamente i record con PROCESSDATE nullo, assicurando un’elaborazione incrementale ed evitando duplicazioni di caricamento.

I dati vengono quindi elaborati raggruppandoli per SHEET e IDCATALOGO, così da ricostruire in modo completo l’insieme delle caratteristiche associate a ciascuna difformità o interferenza. Per ogni difformità/interferenza individuata, prima di procedere con i nuovi inserimenti, vengono eliminate tutte le caratteristiche già presenti a sistema, garantendo l’allineamento completo del catalogo allo stato corrente dei file sorgente.

Per ogni record di staging:

viene determinata la tabella di destinazione in base al valore del campo CATALOGO (a D corrisponde la tabella X4CARDIFFOR e ad i corrisponde la tabella X4CARINTERFER)

per ciascun campo ATTRn valorizzato con “X”:

viene ricercata la regola corrispondente nella tabella X4STAGINGCATCARATRULE, utilizzando come chiave (SHEET, CATALOGO, (ATTRn)). Da questa vengono ricavati le tipologie di asset, il nome della caratteristica e il valore della caratteristica.

viene inserita una riga nella tabella di destinazione contenente:

la tipologia asset

il nome della caratteristica;

il valore assunto dalla caratteristica.

| IDDIFFORMITA | TIPOLOGIA ASSET | ASSETATTRID | VALUE |
| --- | --- | --- | --- |
| Diff.LC.4.2.3.10 | CASSETTA DI SEZIONAMENTO | LIN_TIPO_SEZIONAMENTO | O |
| Diff.LC.4.2.3.10 | CASSETTA DI SEZIONAMENTO | LIN_TIPO_SEZIONAMENTO | C |
| Diff.LC.4.2.3.10 | CASSETTA DI SEZIONAMENTO | LIN_TIPO_SEZIONAMENTO | X |
| Diff.LC.4.2.3.10 | CASSETTA DI SEZIONAMENTO | LIN_TIPO_SEZIONAMENTO | E |
| Diff.LC.4.2.3.10 | CAVI | LIN_TIPO_POSA | I |
| Diff.LC.4.2.3.10 | CAVI | LIN_TIPO_POSA | C |
| Diff.LC.4.2.3.10 | CAVI | LIN_TIPO_POSA | G |
| Diff.LC.4.2.3.10 | CAVI | LIN_TIPO_POSA | S |

I campi ATTR con valore diverso da “X” (ad esempio “NP” o NULL) non producono alcun inserimento, poiché non rappresentano un’associazione valida tra caratteristica e catalogo.

Durante il processo se viene trovata una riga in X4STAGINGCATCARATRULE con TIPOLOGIAASSET differente da quella della difformità o interferenza viene inoltre settato il flag MULTIELEMENTO a 1.

Al termine dell’elaborazione di ogni record, il campo PROCESSDATE della tabella di staging viene aggiornato con la data/ora corrente, marcando il dato come correttamente processato.

Controlli

#### Introduzione

Il caricamento dei cataloghi delle caratteristiche di Controlli segue un processo del tutto similare a quello adottato per Difformità ed Interferenze, che comprende quindi:

Catalogo Controlli: documenti in formato MS Excel con struttura predefinita concordata con il business per la configurazione delle caratteristiche di difformità e interferenze.

Fogli di caricamento MXLoader: questi fogli MS Excel (con funzionalità MXLoader) saranno prodotti da RTI attingendo dai cataloghi prodotti dal business. La struttura dei due formati è similare per consentire un ribaltamento semplificato

Tabelle di staging: tabelle su schema Maximo per il caricamento dei dati dai fogli di caricamento MXLoader

Script di caricamento: procedono alla elaborazione dei dati delle tabelle di staging per riportare le configurazioni nei cataloghi

La struttura del catalogo Controlli è illustrata nell’esempio seguente (ovviamente in base ai vari Controlli la struttura sarà adeguata e fornita in un foglio dedicato).

|  |  | Tipo di Controllo | Tipo di Controllo | Tipo di Controllo | Tipo di Controllo | Tipo di Controllo |
| --- | --- | --- | --- | --- | --- | --- |
|  |  | Vista da terra | Visiva eliportata | IR da elicottero | Convenzionale | … |
| ID DIFFORMITA | Gravità | AT.L.1.0.0.1.1, AT.L.1.0.0.1.2 | AT.L.1.0.0.3.1 | AT.L.1.0.0.4.1 | AT.L.1.0.0.6.1, AT.L.1.0.0.6.2, AT.L.1.0.0.6.3, AT.L.1.0.0.6.4, AT.L.1.0.0.6.5, AT.L.1.0.0.6.6, AT.L.1.0.0.6.7 | … |
| Int.L.2.1.2.1 |  | x | x |  | x | x |
| Int.L.2.1.2.2 |  | x | x |  | x | x |
| Diff.L.1.1.1.3 | lieve | x | x |  | x |  |
| Diff.L.1.1.1.3 | medio | x | x |  | x |  |
| Diff.L.1.1.1.3 | grave | x | x |  | x |  |

L’area bianca verrà popolata dall’utente. L’ID della difformità è indicato nella prima colonna (in corrispondenza della cella azzurra). A seguire sono presenti la descrizione del tipo di controllo e tutti i possibili Controlli (Attività) corrispondenti. L’utente potrà indicare con la lettera X se il valore del Tipo di Controllo deve essere inserito fra quelli che possono rilevare la Difformità/Interferenza, altrimenti potrà lasciare la cella non valorizzata (o in alternativa valorizzata con NP). Le celle colorate rappresentano la struttura del foglio e verranno concordate in modo definitivo. L’utente potrà durante la fase di compilazione variare la parte bianca.

Le celle valorizzate con X costituiscono informazioni rilevanti ai fini applicativi e verranno trasformate in voci da caricare nella tabella X3CATASSOCIAZIONE.

Questi file saranno copiati nel file di caricamento MXLoader (area azzurra). Gli attributi corrispondenti sono già presenti nel file di caricamento. L’area rosa dovrà essere compilata manualmente in base al catalogo.

| IDCATALOGO | GRAVITA | ATTR1 | ATTR2 | ATTR3 | ATTR4 | ATTRn | CATALOGO | SHEET |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Int.L.2.1.2.1 |  | x | x |  | x | x | I | Conduttore |
| Int.L.2.1.2.2 |  | x | x |  | x | x | I | Conduttore |
| Diff.L.1.1.1.3 | lieve |  | x |  |  |  | D | Conduttore |
| Diff.L.1.1.1.3 | medio | x |  |  |  |  | D | Conduttore |
| Diff.L.1.1.1.3 | grave | x | x |  | x | x | D | Conduttore |

#### Tabella di staging e caricamento iniziale

I fogli Excel utilizzati come sorgente possono differire sia per tipologia sia per numerosità delle caratteristiche gestite.

In totale simmetria con quanto previsto per i cataloghi Difformità ed Interferenze, al fine di gestire tale variabilità verrà usata la tabella di staging X4STAGINGCATRILEV contenente colonne generiche (ATTR1, ATTR2, …, ATTRn) che ospiteranno i dati provenienti dai diversi cataloghi.

Oltre alle colonne dedicate aggi attributi, la tabella avrà le seguenti colonne:

SHEET: nome del foglio Excel di origine

IDCATALOGO: identificativo della difformità o interferenza

CATALOGO: identificativo del catalogo che può assumere i valori (D o I)

GRAVITA: identificativo della gravità per cui la Difformità/Interferenza può essere rilevata

ENTERDATE: data aggiornamento dato

PROCESSDATE: data processamento data

Il caricamento dei dati nella tabella di staging avviene tramite MXLoader. Durante tale fase, i campi ATTRn vengono popolati con i valori presenti nei file sorgente (ad esempio “X”, “NP” o valore nullo), mantenendo la codifica originale. Il nome del foglio Excel di provenienza (campo SHEET) e l’identificativo del catalogo (CATALOGO) dovranno essere esplicitamente configurati nell’MXLoader.

Sulla base del nome del foglio Excel SHEET e della tipologia di catalogo CATALOGO, vengono definite specifiche regole di mapping nella tabella X4STAGINGCATRILEVRULE, le quali consentono di determinare, per ciascun campo ATTR:

Il Tipo di Controllo corrispondente;

L'elenco delle Attività relativo;

| SHEET | CATALOGO | COLONNA | TIPOCONTROLLO | ELENCOATTIVITA |
| --- | --- | --- | --- | --- |
| Conduttore | D | ATTR1 | Vista da Terra | AT.L.1.0.0.1.1, AT.L.1.0.0.1.2 |
| Conduttore | D | ATTR2 | Visiva eliportata | AT.L.1.0.0.3.1 |
| Conduttore | D | ATTR3 | IR da elicottero | AT.L.1.0.0.4.1 |
| Conduttore | D | ATTR4 | Convenzionale | AT.L.1.0.0.6.1, AT.L.1.0.0.6.2, AT.L.1.0.0.6.3, AT.L.1.0.0.6.4, AT.L.1.0.0.6.5, AT.L.1.0.0.6.6, AT.L.1.0.0.6.7 |

#### Aggiornamento cataloghi

Il caricamento nella tabella applicativa X3CATASSOCIAZIONE è eseguito dall’automation script X4CATASSOCUPLOAD. Lo script seleziona dalla tabella di staging X4STAGINGCATRILEV esclusivamente i record con PROCESSDATE nullo, assicurando un’elaborazione incrementale ed evitando duplicazioni di caricamento.

I dati vengono quindi elaborati raggruppandoli per SHEET e IDCATALOGO, così da ricostruire in modo completo l’insieme delle caratteristiche associate a ciascuna difformità o interferenza.

** Per ogni controllo individuato, prima di procedere con i nuovi inserimenti, vengono eliminate tutte le rilevabilità già presenti a sistema, garantendo l’allineamento completo del catalogo allo stato corrente dei file sorgente.

in caso siano presenti tutte le gravità possibili, si manterrà un solo record senza gravità esplicitata

Per ogni record di staging:

Dal catalogo Interferenza o Difformità (chiave IDCATALOGO) si ricava la Tipologia Asset

per ciascun campo ATTRn valorizzato con “X” viene ricercato l’elenco delle Attività corrispondenti nella tabella X4STAGINGCATRILEVRULE (campo ELENCOATTIVITA), utilizzando come chiave (SHEET, CATALOGO, (ATTRn)).

dall’elenco si ricavano le singole Attività con cui le Difformità/Interferenze devono essere correlate

Per ogni coppia Attività - Interferenza o Difformità viene inserita una riga nella tabella X3CATASSOCIAZIONE contenente:

L'identificativo dell’Attività;

La Descrizione dell’Attività, presa dal Catalogo Attività

Il Tipo, a seconda che si tratti di Difformità o Interferenza;

L’identificativo della Difformità o in alternativa quello della Interferenza;

La gravità

La Tipologia Asset.

| IDORIGINE | DESCRIPTION | TIPO | IDDIFFORMITA | IDINTERFERENZA | GRAVITA | TIPOLOGIA ASSET |
| --- | --- | --- | --- | --- | --- | --- |
| AT.L.1.0.0.1.1 | Ispezione a vista da terra (SA) | I |  | Int.L.2.1.2.1 |  | ISOLATORE CONDUTTORE |
| AT.L.1.0.0.1.1 | Ispezione a vista da terra (SA) | I |  | Int.L.2.1.2.2 |  | ISOLATORE CONDUTTORE |
| AT.L.1.0.0.1.2 | Ispezione a vista da terra (SA) - monoperatore | I |  | Int.L.2.1.2.1 |  | ISOLATORE CONDUTTORE |
| AT.L.1.0.0.1.2 | Ispezione a vista da terra (SA) - monoperatore | I |  | Int.L.2.1.2.2 |  | ISOLATORE CONDUTTORE |
| AT.L.1.0.0.1.1 | Ispezione a vista da terra (SA) | D | Diff.L.2.1.2.1 |  | Medio | ISOLATORE CONDUTTORE |
| AT.L.1.0.0.1.1 | Ispezione a vista da terra (SA) | D | Diff.L.2.1.2.1 |  | Grave | ISOLATORE CONDUTTORE |
| AT.L.1.0.0.1.2 | Ispezione a vista da terra (SA) - monoperatore | D | Diff.L.2.1.2.1 |  | Medio | ISOLATORE CONDUTTORE |
| AT.L.1.0.0.1.2 | Ispezione a vista da terra (SA) - monoperatore | D | Diff.L.2.1.2.1 |  | Grave | ISOLATORE CONDUTTORE |

I campi ATTR con valore diverso da “X” (ad esempio “NP” o NULL) non producono alcun inserimento, poiché non rappresentano un’associazione valida tra caratteristica e catalogo.

Al termine dell’elaborazione di ogni record, il campo PROCESSDATE della tabella di staging viene aggiornato con la data/ora corrente, marcando il dato come correttamente processato.

## Mobile

L’applicazione mobile recupera le possibili difformità/interferenze rilevabili per un ordine di lavoro e asset mediante un servizio di integrazione già presente che espone le possibili difformità/interferenze precaricate nella tabella X4RILEVAZIONIODL. Le difformità/interferenze rilevabili (incluse quelle non iso-elemento) sono prodotte in questa tabella secondo quanto indicato nel paragrafo 5.1.3 pertanto non è necessario nessun ulteriore sviluppo.

TODO: in attesa da disegno mobile se è necessario esporre ulteriori campi.
