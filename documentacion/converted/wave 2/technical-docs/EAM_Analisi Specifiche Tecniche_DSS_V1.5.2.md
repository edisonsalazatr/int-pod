# EAM_Analisi Specifiche Tecniche_DSS_V1.5.2

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
| Redatto da: | ACN | 24/07/2024 |  |
| Riesaminato da: | ACN | 24/09/2024 |  |
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
| Sigoption | Le sigoption (opzione di firma) permettono di specificare i privilegi per l’utilizzo di applicazioni, opzioni di menu ed elementi della barra degli strumenti. Inoltre, esistono signature option condizionali che consentono di concedere o revocare dinamicamente i privilegi di un utente in base a espressioni condizionali. |

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche, già descritte ad alto livello nel documento “EAM_Analisi Funzionale_Sprint_4_V5_.docx”, verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software in particolare per il nuovo componente Decision Support System che sarà implementato.

Pertanto, verranno riportate e descritte all’interno del presente documento gli algoritmi di calcolo che sono state disegnati per permettere al DSS di gestire la creazione/modifica/cessazione degli ordini di lavoro sulla base di eventi e condizioni che si verificano nel sistema EAM.

Nel documento sono dettagliati i seguenti argomenti:

Scopo e funzione del DSS

Descrizione ed introduzione all’architettura applicativa

Descrizione dei moduli applicativi e delle funzionalità che gestiscono

Descrizione del modello dati

Descrizione delle strutture principali del modulo: topic, cache, oggetti principali

Elenco di tutti gli use case che gestirà il DSS

Per ogni use case è descritto l’algoritmo che sarà implementato

Algoritmi utilizzati

# DSS

# Scopo e funzione del DSS

Il Decisione Support System ha lo scopo di governare il processo di O&M. Il processo di O&M è basato sulle policy, ovvero i criteri che indirizzano le attività da effettuare su un determinato asset.

Il processo di O&M in Terna, ad oggi, è caratterizzato da un monitoraggio effettuato quasi esclusivamente su base periodica e da attività di manutenzione effettuate esclusivamente su condizione.

Il punto di ingresso del processo di O&M sono quindi le policy di monitoraggio periodiche, i cui input sono solamente dati di anagrafica, che generano attività di monitoraggio su base periodica. Attraverso tali attività di monitoraggio periodiche vengono rilevate le difformità che affliggono gli asset. Le policy delle attività su condizione ricevono in input le difformità rilevate tramite i controlli periodici (ma anche dati di anagrafica e altri indicatori di condizione tecnica) e generano le attività di controllo / manutenzione / sostituzione su condizione.

Le caratteristiche principali quindi che deve soddisfare il DSS sono:

Gestione ad eventi: permettere di elaborare su base eventi delle logiche di business riportate nelle policy. In particolare, un singolo evento può scatenare più azioni all’interno del sistema ed avere un effetto tanto in ambito calcolo DSS quanto in ambito non DSS. Quindi permettere gestione 1 evento n-azioni eterogenee e non per forza sulle stesse entità.

Resilienza e robustezza: permettere di sopportare carichi massivi di logiche di business concentrate in un particolare periodo dell’anno a ridosso del periodo di calcolo massivo di tutte le policy. Esternamente a questo intervallo ridotto temporalmente parlando non si registrano questi picchi.

Modalità eterogenee di innesco calcolo: Soddisfare le logiche di business peculiari delle policy…

# Assunzioni principali

Il calcolo dell’HI, Tasso di Guasto, Tasso di difettosità sarà effettuato tramite il modulo Health Maximo. Nel documento EAM Specifiche Tecniche_Sprint4V1.0 capitolo 5.5 e EAM_Analisi Specifiche Tecniche_HI_V1.1.docx sono dettagliate le configurazioni e gli sviluppi implementati. Il DSS non innesca il calcolo degli HI, TdA, TdG.

Il DSS dovrà considerare nelsolamente le difformità in stato “”le attività in stato “Aperto”, “Avviato”, “” e “”.

Nel caso fosse stato creato, in run precedenti, un ordine associato allo stesso asset caratterizzato dalla stessa attività nel caso in cui l’ordine già creato si trovasse in stato funzionalmente precedente a “Schedulato” il sistema non creerà un nuovo ordine ma provvederà a modificare quello esistente. Nel caso l’ordine già associato all’asset fosse in stato uguale o successivo a “Schedulato” il DSS provvederà a creare un nuovo ordine associato all’asset.

Una policy periodica non può essere di tipo Sostituzione.

In fase di creazione OdL verrà associato per ogni ordine strettamente i riferimenti all’asset su cui è stato aperto. Non saranno riportati riferimenti anagrafici relativi ad asset correlati. Per essere più chiaro in fase di creazione ordine per un asset campata/estremo cavo non saranno riportati i riferimenti dei sostegni o all’estremo cavo. L’utente per associare una difformità ai sostegni associati alla campata dovrà ricercare da GUI Maximo.

Sono descritti ed elencati tutti gli use case di innesco del DSS, tutti gli algoritmi e feature supportate..

L’operatore da gui potrà validare e modificare gli ordini di lavoro crearti dal DSS ma non triggerare la scelta dell’algoritmo da usare per la realizzazione degli ordini che rimane una feature interna del sistema.

Le policy dovranno essere configurate da GUI o tramite strumenti di caricamento iniziale tipo MXloader che verrà rilasciato con il setup del DSS. Il DSS permetterà di valutare le policy solo se soddisferanno le condizioni riportate al paragrafo 3.5.2

# Descrizione ed introduzione all’architettura applicativa

Il Decision Support System è un modulo che in ambito progetto EAM è stato identificato come non-embedded. Per not-embedded si indica che non è stato disegnato utilizzando pod e funzionalità IBM Maximo native ma funzionalità custom ospitate da pod non Maximo native.

È stato pensato per garantire l’utilizzo, dove possibile di componenti e funzionalità IBM Maximo, ma nel caso in cui le funzionalità necessarie non fossero disponibili la piattaforma è stata customizzata aggiungendo dei pod che permettono quindi di assolvere alle funzionalità e farsi carico come nel caso del DSS di logica di business.

Per garantire la comunicazione tra pod IBM native e pod che estendono la soluzione è stato scelto di usare Kafka per soddisfare esigenze sia funzionali che tecniche.

La scelta di Kafka è legata dal punti di vista funzionale all’esigenza di poter gestire una logica topic based: ad un evento pubblicato da un produttore possono corrispondere più azioni intraprese da consumatori che con tempistiche e logiche dedicate permettono di soddisfare requisiti funzionali come la sequenzialità temporale delle richieste di calcolo (garantendo contestualmente un calcolo near-real time e l’ordine temporale) grazie all’uso di più partizioni sullo stesso topic ed alla scelta della chiave del messaggio pubblicato kafka nativamente reindirizza sulla stessa partizione messaggi che hanno la stessa chiave, più consumatori in parallelo elaborano questi messaggi. Garantite anche la gestione della prevalenza, delle logiche di calcolo policy elementari, combinate, aggregate nonché l’eleggibilità

Dal punto di vista tecnico l’uso di un meccanismo asincrono ad eventi permette di venire incontro all’esigenza di resilienza della soluzione: nel caso uno dei pod per eccesso di carico dovesse crashare e restartare il calcolo sarà interrotto e ripreso nel punto da cui è partito. Tale funzionalità è garantita dalla gestione della commit su offset della partizione kafka. Se il consumatore, ad esempio, viene killato da OCP l’offset rimarrà nella posizione corrente ed al restart del pod crashato il calcolo riprenderà da dove interrotto.

Gli elevati carichi saranno gestiti tramite l’uso dello stesso pod (che si occuperà dei calcolo) scalato orizzontalmente all’occorrenza, tramite meccanismo di horizontal autoscaling pod (HPA) Openshift based. In questo modo sarà gestito il carico impulsivo richiesto nelle fasi di calcolo più onerose come ad esempio quello delle policy periodiche con l’uso del max dei pod disponibili [in parte rilasciati nel momento in cui il carico si riduca] e nello stesso tempo grazie alla possibilità di tuning del carico sarà possibile in modo parametrico definire il numero di consumatori.

# Architettura applicativa

Nella figura sottostante è riportata l’architettura applicativa del DSS

# Descrizione dei moduli applicativi e delle funzionalità che assolvono

Nella figura sottostante sono rappresentate le principali componenti dell’architettura applicativa e le funzioni che assolvono. È dedicato per ogni componente applicativa un sotto paragrafo dedicato.

### dss-manager

Il pod ha lo scopo di analizzare l’evento che innesca il calcolo delle policy ed indirizzare il calcolo. I possibili eventi che possono innescare il calcolo delle policy sono gli use case riportati nel capitolo 4.

### dss-worker

il pod assolve al compito di eseguire l’estrazione dei dati su cui si deve basare il calcolo ed eseguire il calcolo della policy su singolo asset o gruppo di asset su cui è basata la chiave di calcolo.

### dss-cache

Il pod è la cache del engine di calcolo, permette di conservare con TTL (time to live) configurabile per entità i dati relativi all’asset evitando di effettuare di calcoli oppure delle estrazioni di dati che non sono cambiati. Il pod esporrà delle api per gestire l’evict della cache. La cache permetterà di persiste anche i cataloghi. Saranno previste delle api di cache evict innescate nei casi in cui sono state effettuate delle modifiche sui cataloghi oppure sulla consistenza che rendono vetusto quanto in cache. Le annotation spring cache based permetteranno basandosi su chiave definita di restituire il valore conservato in cache oppure procedere al recupero del dato.

Il pod è costituito da un microservizio che ospita Redis.

### kafka topic

Sono previsti i seguenti topic dedicati al motore di calcolo opportunamente partizionati, nella slide identificati con (B):

topic-mas-handle-events: topic su cui vengono indirizzati i messaggi che innescano almeno un calcolo

topic-mas-dss-workers: topic su cui vengono indirizzate le richieste di calcolo per policy ed asset.

topic-mas-handle-periodic: topic su cui vengono indirizzati i messaggi relativi alle policy periodiche.

topic-mas-handle-worker-feedback: topic su cui vengono indirizzati i messaggi di feedback del worker usato solo per i macro-algoritmi strettamente di sostituzione e tronco di ispezione

Sono previsti i seguenti topic di raccolta eventi sulla piattaforma opportunamente partizionati, nella slide identificati con (A):

topic-mas-handle-attivita-change-event: topic su cui vengono indirizzati i messaggi relativi alle attività aggiunte o modificate a sistema relative al catalogo attività.

topic-mas-handle-diffor-change-event: topic su cui vengono indirizzati i messaggi relative alle difformità aggiunte o modificate a sistema relative al catalogo difformità.

topic-mas-handle-diffor-asset-change-event: topic su cui vengono indirizzati i messaggi relative alle difformità aggiunte o modificate a sistema per un singolo asset relative alle difformità rilevate.

topic-mas-handle-inter-change-event: topic su cui vengono indirizzati i messaggi relativi alle interferenze aggiunte o modificate a sistema relative al catalogo interferenze.

topic-mas-handle-inter-asset-change-event: topic su cui vengono indirizzati i messaggi relativi alle interferenze aggiunte o modificate a sistema per un singolo asset relative alle interferenze rilevate.

topic-mas-handle-asset-change-event: topic su cui vengono indirizzati i messaggi relative agli asset aggiunti o modificati a sistema

topic-mas-handle-hi-asset-change-event: topic su cui vengono indirizzati i messaggi relativi agli health index aggiunti o modificati a sistema per un singolo asset

topic-mas-handle-misure-asset-change-event: topic su cui vengono indirizzati i messaggi relativi alle misure aggiunte o modificate a sistema per un singolo asset

topic-mas-handle-modelli-target-change-event: topic su cui vengono indirizzati i messaggi relativi ai modelli aggiunti o modificati a sistema

topic-mas-handle-odl-create-event: topic su cui vengono indirizzati i messaggi relativi alla creazione di un nuovo OdL a sistema

topic-mas-handle-assetspec-change-event: topic su cui vengono indirizzati i messaggi relativi ad una modifica di un assetspec per un dato asset.

topic-mas-handle-policy-change-event: topic su cui vengono indirizzati i messaggi relativi ad una creazione e modifica di una policy.

topic-mas-handle-idoneita-change-event: topic su cui vengono indirizzati i messaggi relativi ad una creazione e modifica di una idoneità.

topic-mas-handle-eccezioni-change-event: topic su cui vengono indirizzati i messaggi relativi ad una creazione e modifica di una eccezione.

# Descrizione del modello dati

Di seguito verranno riportate le tabelle usate in ambito DSS. Il E/R complessivo sarà riportato nel documento di AT complessiva EAM_Analisi_Tecnica_Complessiva.v1.0.doc

# Tabella cataloghi

Le tabelle X4POLICY, X4POLICYINPUT, X4POLICYCLASSIFICATION persistono il catalogo policy e le regole di applicabilità. Nel caso venga effettuata da Gui una modifica ad una policy oppure ad una regola di applicabilità vengono aggiornate queste tabelle.

X4POLICY tabella principale delle policy.

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4POLICYID | NUMBER | Y | PK |
| DATAFINEVAL | DATE | N | Data fine validità |
| DATAINIZIOVAL | DATE | N | Data inizio validità |
| CHANGEDBY | VARCHAR2(100 CHAR) | N | Modificato da |
| CHANGEDDATE | DATE | N | Data modifica |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione |
| DIFFERIBILITA | NUMBER | N | Differibilità |
| FREQUENZA | NUMBER | N | Frequenza |
| IDATTIVITA | VARCHAR2(12 CHAR) | N | ID Attività Output |
| X4IDPOLICY | VARCHAR2(20 CHAR) | N | ID Policy |
| IDTIMING | VARCHAR2(3 CHAR) | N | ID Timing |
| LASTREVIEWDATE | DATE | N | Data ultima revisione |
| MAXANTICIPO | NUMBER | N | Massimo anticipo |
| MAXPOSTICIPO | NUMBER | N | Massimo posticipo |
| TIPOLOGIAASSET | VARCHAR2(192 CHAR) | N | Tipologia Asset |
| LIVPOLICY | NUMBER | N | Livello Policy |
| LIVCOMB | NUMBER | N | Livello combinazione input |
| UM | VARCHAR2(1 CHAR) | N | Unità di misura |
| TIPOPOLICY | VARCHAR2(15 CHAR) | N | Tipo Policy |
| POLICYAGGR | NUMBER | y | Policy aggregata |
| LIVAGGR | NUMBER | N | Livello aggregazione policy |
| CAMPAGNA | NUMBER | Y | Campagna |
| COMBINPUT | VARCHAR2(1000 CHAR) | N | Combinazione input (formula) |
| ELEGGIBILITA | NUMBER | N | Campo eleggibilità della policy |
| STATUS | VARCHAR2(20) | N | Stato della policy |
| X4RIFTEMPORALE | VARCHAR2(50) | Y | Riferimento temporale inserito |
| X4SOTTOPROGRAMMA | VARCHAR2(50) | Y | sottoprogramma associato |
| X4CLUSTER | VARCHAR2(50) | Y | cluster di policy |
| SITEID |  | N | SiteId associato alla tipologia di asset |
| APARTIREDA | DATE | Y | Data a partire dalla quale vale la policy |
| VERSION | NUMBER | N | Versione della policy |
| IDMACROATTIVITA | VARCHAR2(16) | N | ID Macro Attività associata alla policy |

Per questa tabelle sarà abilitato il meccanismo di audit di IBM Maximo al fine di conservare la storia delle modifiche e riuscire a ricostruire lo storico della configurazione policy nel tempo. Di sotto un esempio che permette di spiegare come sarà gestito il meccanismo di audit

X4POLICYCLASSIFICATIONS tabella associativa per gli ASSET.

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4POLICYCLASSIFICATIONSID | NUMBER | Y | PK della tabella |
| CLASSIFICATIONID | VARCHAR2(190 CHAR) | N | Tipo di classificazione |
| X4POLICYID | NUMBER | N | FK alla X4POLICY. X4POLICYID |
| ROWSTAMP | VARCHAR | Y | Campo di sistema |

X4POLICYINPUT tabella associativa di configurazione delle policy.

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| DESCRIPTION | VARCHAR2(50) | N | Descrizione funzionale |
| IDINPUT | VARCHAR2(50) | N | Id tabellare Input |
| IDINPUT_EXT | VARCHAR2(50) | N | Id descrittivo input |
| IDMISURA | VARCHAR2(25) | N | Id tabellare Misura |
| IDMISURA_EXT | VARCHAR2(50) | N | Id descrittivo Misura |
| LIVINPUT | NUMBER | N | livello input |
| NINPUT | NUMBER | N | Numero input |
| OPERATORE | VARCHAR2(15) | N | operatore di confronto |
| TARGETNUM | FLOAT | N | Valore numerico di confronto |
| TARGETRAFFRONTO | VARCHAR2(50) | N | Id target di raffronto |
| TARGETRAFFRONTO_EXT | VARCHAR2(50) | N | Id descrittivo target di raffronto |
| TIPOLOGIAINPUT | VARCHAR2(20) | N | tipologia dell'input (Att, Var,COMB) |
| X4AND | VARCHAR2(50) | N | N input in AND |
| X4EQUAL | VARCHAR2(20) | N | N input in equal |
| X4NOTEQUAL | VARCHAR2(20) | N | N input in NOT equal |
| X4OR | VARCHAR2(50) | N | N input in OR |
| X4POLICYID | NUMBER | N | Riferimento FK tabellare x4policy |
| X4POLICYINPUTID | NUMBER | Y | PK tabella |
| VERSION | NUMBER | N | Versione della policy |
| X4IDPOLICY | NUMBER | N | ID policy |

La tabella X4CATDIFFOR persiste il catalogo delle difformità

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4CATDIFFORID | NUMBER | Y | Campo Sistema PK |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione funzionale |
| ASSIEME | VARCHAR2(20 CHAR) | N | Valore di assieme |
| IDDIFFORMITA | VARCHAR2(15 CHAR) | N | Id Della difformità |
| IDMISURA | VARCHAR2(20 CHAR) | N | valore di misura |
| SOTTOASSIEME | VARCHAR2(20 CHAR) | N | sottoassieme |
| TEMPOVALID | VARCHAR2(50 CHAR) | N | Tempo di validità espresso in GG |
| TIPOLOGIA | VARCHAR2(20 CHAR) | N | Tipologia della difformità |
| TIPOLOGIAASSET | VARCHAR2(192 CHAR) | N | Tipologia dell'asset |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo di sistema |
| STATUS | VARCHAR2(15 CHAR) | Y | Stato Difformità |
| PESOAPERTE | FLOAT | Y | Peso della difformità per HI in apertura |
| PESOCHIUSE | FLOAT | Y | Peso della difformità per HI in chiusura |
| LOGICADIFF | VARCHAR2(150) | N | Logica generazione della difformità |
| FASE | NUMBER | Y | Codice della Fase |
| DOCUFOTOOBBLIGA | NUMBER | Y | Flag di presenza obbligatorietà fotografia documentale |
| GENMISURA | NUMBER | N | Flag di sussistenza logiche di difformità |

La tabella X4CATINTERF persiste il catalogo delle interferenze.

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione funzionale |
| X4CATINTERFID | NUMBER | Y | PK univoca |
| HASLD | NUMBER | Y | Campo Sistema Maximo |
| ASSIEME | VARCHAR2(20 CHAR) | N | Valore dell'assieme |
| IDINTERFERENZA | VARCHAR2(50 CHAR) | N | ID della interferenza |
| IDMISURA | VARCHAR2(50 CHAR) | N | ID della misura |
| LOGICAINT | VARCHAR2(150 CHAR) | N | Logica di calcolo interferenza |
| MISURA | NUMBER | T | Valore della Misura |
| SOTTOASSIEME | VARCHAR2(20 CHAR) | N | Sotto assieme |
| TEMPOVAL | NUMBER | N | Tempo di validità espresso in GG |
| TIPOLOGIAASSET | VARCHAR2(192 CHAR) | N | Tipologia dell'asset |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo di sistema |
| STATUS | VARCHAR2(20) | N | Stato dell’interferenza |
| DOCUFOTOOBBLIGA | NUMBER | N | Flag di presenza obbligatorietà fotografia documentale |
| FASE | NUMBER | N | Codice della fase |
| GENMISURA | NUMBER | N | Flag di sussistenza logica di interferenza |

X4ATTIVITAASSOCIAZIONE persiste il catalogo di associazione delle attività sulle macro-attività, utilizzato in fase di calcolo OdL dal DSS.

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4ATTASSOCIAZIONEID | NUMBER | Y | ID TABELLA |
| MACROATTIVITA_EXT | VARCHAR2(15) | Y | Macro-attività id descrittivo |
| ATTIVITA | NUMBER | Y | Id attività Jobplan |
| ATTIVITA_EXT | VARCHAR2(15) | Y | ID attività Descrittivo |
| HASLD | NUMBER | Y | ID Revisione |
| ROWSTAMP | NUMBER | Y | Campo progressivo SQL |

X4CONDIZIONEASSOCIAZIONE persiste il catalogo delle logiche utilizzate dalla X4ATTIVITAASSOCIAZIONE.

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4CONDIZIONEASSOCIAZIONEID | NUMBER | Y | ID tabellare univoco |
| X4ATTASSOCIAZIONEID | NUMBER | Y | Riferimento X4 ATTASSOCIAZIONE |
| TARGET | VARCHAR2(100) | Y | Target di raffronto |
| VALUE | NUMBER | Y | Valore da Raffrontare |
| HASLD | NUMBER | Y | ID Revisione |
| ROWSTAMP | NUMBER | Y | Campo progressivo SQL |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Le tabelle persistono il catalogo Health index (HI), tasso di guasto (TdG), tasso di anomalia. AHMETHODOLOGY

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ACTIVE | NUMBER | Y | Flag se metodologia è attiva\spenta |
| AHMETHODOLOGYID | NUMBER | Y | Unique Id |
| AUTOCONVERSION | NUMBER | N | indica se la conversione del punteggio (score) è automatica. |
| BESTSCORE | NUMBER(10,2) | N | Il miglior punteggio possibile per la metodologia, |
| CALCTYPE | VARCHAR2(100 CHAR) | Y | Metodo di calcolo |
| CONVERSIONFORMULA | VARCHAR2(1000 CHAR) | N | Formula per punteggio grezzo in un valore standardizzato per analisi comparativa. |
| CONVERSIONFORMULANAME | VARCHAR2(50 CHAR) | N | Nome della formula matematica |
| DESCRIPTION | VARCHAR2(200 CHAR) | N | Descrizione della metodologia |
| EXTFUNCTIONNAME | VARCHAR2(200 CHAR) | N |  |
| EXTMODELNAME | VARCHAR2(200 CHAR) | N |  |
| FACTORCARDINALITY | VARCHAR2(20 CHAR) | Y | N fattori utilizzati dalla metodologia (N° indicatori per calcolo stato salute) |
| ISATTRIBUTE | NUMBER | Y | Flag per indicare se metodo è legato a singolo attributo |
| METHODDISPLAYNAME | VARCHAR2(200 CHAR) | N | Nome descrittivo metodologia |
| METHODFORMULA | VARCHAR2(1000 CHAR) | Y | Formula principale che descrive il calcolo della salute dell’asset |
| METHODNAME | VARCHAR2(50 CHAR) | Y | Nome tecnico della metodologia |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Timestamp del record |
| SCORETYPE | VARCHAR2(50 CHAR) | N | Tipo di score (Rischio, salute etc etc) |
| USEWITH | VARCHAR2(100 CHAR) | Y | Ambito di utilizzo (esempio ASSET) |
| WORSTSCORE | NUMBER(10,2) | N | Peggior punteggio possibile calcolato |

AHSCORE

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| AHSCOREID | NUMBER | Y | Identificativo univoco punteggio registrato |
| ASSETNUM | VARCHAR2(25 CHAR) | N | Identificativo univoco dell’asset associato |
| ATTRIBUTENAME | VARCHAR2(50 CHAR) | N | Nome dell’attributo dell’asset |
| CHANGEBY | VARCHAR2(100 CHAR) | N | Utente modificatore |
| CHANGEDATE | DATE | N | Data modifica |
| ENTERDATE | DATE | N | Data inserimento punteggio calcolato |
| EXPVALINPUTTYPE | VARCHAR2(10 CHAR) | N | Input atteso |
| FORMULA | VARCHAR2(1000 CHAR) | N | Formula utilizzata dalla metodologia |
| FUTURESCOREBATCHID | NUMBER | N | Campo per calcoli previsionali futuri |
| LASTSUCCESSDATE | DATE | N | Data ultimo calcolo compiuto con successo |
| LOCATION | VARCHAR2(25 CHAR) | N | Location id associata la punteggio |
| MEMO | VARCHAR2(1000 CHAR) | N | Note aggiuntive |
| METHODNAME | VARCHAR2(50 CHAR) | N | Nome della Metodologia associata |
| MSGID | VARCHAR2(100 CHAR) | N | ID messaggio associato al calcolo |
| OBJECTNAME | VARCHAR2(30 CHAR) | N | Tipo di oggetto associato “ASSET” |
| ORGID | VARCHAR2(8 CHAR) | N | Id organizzazione |
| ORIGINALVALUE | NUMBER | N | Valore precedente alla misurazione\calcolo |
| OWNERRECORDID | NUMBER | N | ID del record proprietario |
| PREVIOUSVALUE | NUMBER(12,2) | N | Valore registrato prima dell’aggiornamento |
| ROWSTAMP | VARCHAR2(40 CHAR) | N | Campo SQL timestamp |
| SITEID | VARCHAR2(8 CHAR) | N | Identificativo del sito specifico |
| SUCCESS | NUMBER | Y | Flag se calcolo è andato a buon fine |
| TODATE | DATE | N | Data del punteggio registrato |
| VALUE | NUMBER(12,2) | N | Valore finale per calcolo asset |

La tabella ASSET persiste

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ASSETNUM | VARCHAR2(25) | Y | Codice identificativo dell'asset |
| SITEID | VARCHAR2(8) | Y | Sede dell'asset |
| DESCRIPTION | VARCHAR2(50) | N | Descrizione dell'asset |
| X3INGRP | VARCHAR2(3) | N | Unità impianti |
| X3SWERK | VARCHAR2(4) | N | Distretto territoriale |
| X3GEWRK | VARCHAR2(8) | N | Gruppo di lavoro responsabile |
| X3SAPID | VARCHAR2(30) | N | Sapid dell'asset |

Le tabelle X4CATMODELLO e X4CATMODELLOASSET persistono il catalogo modelli target.

X4CATMODELLO

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4CATMODELLOID | NUMBER | Y | IUD univoco tabellare |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione del modello |
| TIPOLOGIAASSET | VARCHAR2(192 CHAR) | N | Tipo del modello |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo SQL |
| IDMODELLO | VARCHAR(20) | N |  |

X4CATMODELLOASSET

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4CATMODELLOASSETID | NUMBER | Y | ID Univoco |
| X4CATMODELLOID | NUMBER | N | Riferimento alla X4CATMODELLO |
| VALUE | FLOAT | N | Valore Soglia |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo SQL |
| IDCLUSTER | NUMBER | N | Numero di cluster dell’asset |
| HASLD | NUMBER | Y | Campo Maximo |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrittivo della Misura |
| ATTRIBUTO | VARCHAR2(50 CHAR) | N | Definisce la classificazione della Misura |
| N1 | FLOAT | N |  |
|  |  |  |  |

Le tabelle X4MICROCONTROLLI persistono il catalogo piano dei controlli.

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ASSIEME | Varchar2(20) | N | Valore dell’assieme |
| CONTROLLO | Varchar2(3) | N | Valore del controllo, (Occasionale, Bimensile) |
| DESCRIPTION | Varchar2(50) | N | Descrizione del controllo |
| FREQUENZA | Number | N | Valore numerico se controllo valorizzato |
| IDMICROCONTROLLO | Varchar2(15) | N | Valore Alfa numerico del Micro-controllo |
| SOTTOASSIEME | Varchar2(20) | N | Valore Sotto-Assieme |
| TIPOLOGIAASSET | Varchar2(192) | N | Tipo di asset |
| TCOMPONENTE | Varchar2(20) | N | Tipologia del componente |
| TCOMANDO | Varchar2(20) | N | Tipologia del comando |
| STATUS | VARCHAR2(15) | N | Campo status |
| X4MICROCONTROLLIID | NUMBER | Y | PK |

Le tabelle X4CATMISURE persistono il catalogo misure e variabili di stato.

| Campo | Tipo | Obbligatorio | Descrizione |  |
| --- | --- | --- | --- | --- |
| X4CATMISUREID | NUMBER | Y | Campo univoco PK di tabella |  |
| DESCRIPTION | VARCHAR2(200 CHAR) | N | Descrizione |  |
| CALCOLO | VARCHAR2(50 CHAR) | N | Descrizione letterale del calcolo |  |
|  |  |  |  |  |
| IDMISURA | VARCHAR2(20 CHAR) | N | Codice Della Variabile |  |
|  |  |  |  |  |
| RESET | NUMBER | Y | Flag presenza reset misura |  |
| TIPOPARAMETRO | VARCHAR2(20 CHAR) | N | VARIABILE\MISURA |  |
| UM | VARCHAR2(5 CHAR) | N | Unità di misura |  |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo SQL |  |
|  |  |  |  |  |
| TIPOLOGIAASSET | VARCHAR2(192) | N | Tipologia asset |  |
|  |  |  |  |  |
|  |  |  |  |  |
| PERIODOCALCOLO | NUMBER | N |  |  |
| TIPOLOGIAINPUT | VARCHAR(50) | N |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

La tabella X4MISUREASSET persistono le misure rilevate sull’asset e le loro revisioni.

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ASSETNUM | VARCHAR2(50 CHAR) | N | Id descrittivo dell'asset |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione della misura |
| HASLD | NUMBER | Y | Id revisione incrementale |
| IDDIFFORMITA_EXT | VARCHAR2(30 CHAR) | N | ID descrittivo difformità |
| IDMISURA | VARCHAR2(50 CHAR) | N | Id descrittivo Misura |
| PRIMARILEVAZIONE | DATE | N | data della prima rilevazione |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo SQL |
| SITEID | VARCHAR2(50 CHAR) | N | Sito fisico dell'asset |
| ULTIMARILEVAZIONE | DATE | N | Data della ultima rilevazione |
| VALORE | FLOAT | N | Valore di rilevazione |
| X4MISUREASSETID | NUMBER | Y | Campo Univoco tabellare |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

La tabella X4ATTPREVALENTI per ogni PdL (JPNUM) tiene al proprio interno le attività di cui è prevalente (che devono essere chiuse al momento della creazione).

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ATTIVITAPREV | VARCHAR2(15 CHAR) | N | Numero identificativo dell'ordine di lavoro |
| DESCRIPTION | VARCHAR2(50 CHART) | N | Descrizione |
| HASLD | NUMBER | Y | Campo gestito Maximo |
| JPNUM | VARCHAR2(15 CHAR) | N | Pdl (Piano di Lavoro) |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo gestito SQL |
| SITEID | VARCHAR2(8 CHAR) | N | Sede dell’asset |
| X4ATTPREVALENTIID | NUMBER | Y | ID Attività |

# Tabella tracing DSS

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

La tabella X4_DSS_POLICY_ERROR persiste gli errori di elaborazione in fase di innesco richiesta di policy permettendo di dare evidenza della mancata elaborazione di una policy.

X4_DSS_POLICY_ERROR:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| DSS_POLICY_ERROR_ID | NUMBER |  | ID Errore di elaborazione |
| POLICY_ID | NUMBER |  | Riferimento Id Policy |
| ERROR_TIMESTAMP | DATE | N | Data in cui si è verificato l’errore |
| ERROR_DESCRIPTION | VARCHAR2(4000) | N | Descrizione errore |

La tabella X4_DSS_WORKER_HISTORY è una tabella de-normalizzata che permette di tracciare i singoli eventi di calcolo su un asset o gruppo di asset per l’istanza di calcolo policy richiesta. Permetterà di avere se scelto il tracing evidenza anche tabella dell’esito dei calcoli condotto in parallelo sui vari asset o catene di asset oggetto di calcolo della singola policy

X4_DSS_WORKED_HISTORY:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ID_DSS_WORKED | NUMBER | Y | ID Evento |
| STATUS | VARCHAR2(20 CHAR) | Y | Stato elaborazione |
| POLICY_ID | VARCHAR2(20 CHAR) | Y | Id della policy che è stata verificata |
| ASSETNUM | VARCHAR2(20 CHAR) | Y | Assetnum su cui è stata effettuata la verifica della policy |
| ORDERNUM | VARCHAR2(20 CHAR) | N | Potrebbe non essere generato ordine da cui nullable |
| DSS_PC_CORRELATION_ID | VARCHAR2(40 CHAR) | Y | UUID - Policy calculation correlation id è la chiave di aggregazione che permette di ricondurre 2 calcoli eseguiti su 2 o più worker alla stessa richiesta di calcolo policy effettuata su più asset |
| TOTAL_RELATED_MESSAGE | VARCHAR2(20 CHAR) | Y | Il numero complessivo di messaggi inviati verso il pod worker |
| PROGRESSIVE_RELATED_MESSAGE | VARCHAR2(20 CHAR) | N | Il numero progressivo del messaggio inviati verso il pod worker |
| TRIGGED_ENTITY | VARCHAR2(20 CHAR) | N |  |
| CREATED_BY | VARCHAR2(20 CHAR) | N | Pod che ha lanciato l’elaborazione |
| CREATED_TIMESTAMP | DATE | N | timestamp di creazione |
| CHANGED_BY | VARCHAR2(20 CHAR) |  | Pod che ha eseguito l’elaborazione |
| CHANGED_TIMESPAMP | VARCHAR2(20 CHAR) | N | timestamp di modifica |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

# Tabelle Ordini di lavoro e piani di lavoro

La tabella WORKORDER persiste gli ordini di lavoro associati ad un asset.

WORKORDER

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ASSETNUM | VARCHAR2(25) | Y | Numero identificativo asset dell'ordine di lavoro |
| DESCRIPTION | VARCHAR2(100) | Y | Descrizione breve dell'ordine di lavoro |
| SCHEDFINISH | DATE | N | Data di fine pianificata dell'ordine di lavoro |
| SCHEDSTART | DATE | Y | Data di inizio pianificata dell'ordine di lavoro. |
| SNECONSTRAINT | VARCHAR2(20) | N | Vincolo di inizio anticipato. |
| FNLCONSTRAINT | DATE | N | Vincolo di fine posticipato. |
| TARGCOMPDATE | DATE | N | data di termine prevista per l'ODL |
| TARGSTARTDATE | DATE | N | data di inizio prevista per l'ODL |
| WONUM | VARCHAR2(25) | N | Numero univoco dell'ordine di lavoro |
| SITEID | VARCHAR2(8) | N | Identificativo del sito in cui viene eseguito l'ordine di lavoro. |
| PARENT | VARCHAR2(25) | N | Ordine di lavoro genitore |
| STATUS | VARCHAR2(16) | N | Stato dell'ordine di lavoro |
| STATUSDATE | DATE | N | Orario in cui è stato modificato ODL |
| ESTDUR | FLOAT | N | Durata stimata dell'ordine di lavoro in ore. |
| ACTSTART | DATE | N | Data di inizio effettiva dell'ordine di lavoro |
| ACTFINISH | DATE | N | Data di completamento effettiva dell'ordine di lavoro. |
| X3AGGREGATI | NUMBER(10,2) | N | Odl Aggregatore |
| X3NATURALAVORO | VARCHAR2(15) | N | Natura del lavoro |
| X3DATAMODELLO | DATE | N | Data di creazione del modello |
| X4POLICY | VARCHAR2(20) | N | Id descrittivo Policy legata a Odl |
| X4FISICO | FLOAT | N | Rendicontazioni consumi\produzioni |
| X4UM | VARCHAR2(5) | N | Unità di misura |
| X4TRAINANTE | VARCHAR2(10) | N | Indica se ODL trainante o Trainato |
| X3TPREP | FLOAT | N | Tempo Preparazione |
| X3TOPER | VARCHAR2(20) | N | Tempo Operazione |
| X3TTRASF | VARCHAR2(20) | N | Tempo di Trasferimento |
| X3TEMPOPAUSA | FLOAT | N | Tempo di Pausa Odl |
| OWNERGROUP | NUMBER | N | Assegnazione ad un team di un ODL |
| REPORTDATE | DATE | N | Data in cui ODL è stato generato |
| REPORTEDBY | VARCHAR2(82) | N | Utente segnalatore |
| X4ASSIEME | VARCHAR2(20) | N | Assieme |
| X4SOTTOASSIEME | VARCHAR2(50) | Y | Sotto assieme |
| X4CODDELINDISP | VARCHAR2(25) | Y | Codice Delibera della Indisponibilità |
| X4DFINEINDISP | DATE | Y | Data fine Indisponibilità |
| X4DINIZIOINDISP | DATE | Y | Data fine indisponibilità |
| X4COIN | VARCHAR2(15) | Y | Codice alfanumerico spese COIND |
| X4FLAGFISICO | NUMBER | Y | Conferma misure consultivo fisico |
| X4NORDINEINTERNO | VARCHAR2(20) | Y | Ordine Interno |
| X4TIPOOI | VARCHAR2(15) | Y | Tipo Ordine Interno |
| X4NTK | NUMBER | Y | Network |
| X4ODM | VARCHAR2(20) | Y | Ordine Di Manutenzione |
| X4PERIMETROINT | VARCHAR2(25) | Y | Perimetro Intervento |
| X4TIPOATTIVITA | VARCHAR2(20) | Y | Tipo di attività |
| X4TIPOODL | VARCHAR2(15) | Y | Tipo Ordine di Lavoro |
| X4TIPOORDINE | VARCHAR2(15) | Y | Tipo Ordine |
| X4TIPOSPESA | VARCHAR2(5) | Y | Tipo Spesa |
| X4WBE | VARCHAR2(20) | Y | Wbe (elemento) |
| X4WBEPIAN | VARCHAR2(20) | Y | Wbe pianificata |
| X4WBS | VARCHAR2(20) | Y | Wbs (struttura) |
| X4WBSPIAN | VARCHAR2(20) | Y | WBS pianificata |
| JPNUM | VARCHAR2(12) | Y | Id Attività |

La tabella JOBPLAN persiste le attività o piani di lavoro associati ad un asset. In EAM un ordine avrà sempre un unico piano di lavoro associato. Inoltre, la tabella JOBPLAN contiene anche il catalogo dei Controlli di Legge

JOBPLAN

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| JPNUM | VARCHAR2(12 CHAR) | Y | ID Attività |
| DESCRIPTION | VARCHAR2(100 CHAR) | N | Descrizione attività |
| CLASSSTRUCTUREID | VARCHAR2(25 CHAR) | N | Tipologia attività |
| PLUSCREVNUM | NUMBER | Y | Numero revisione |
| STATUS | VARCHAR2(16 CHAR) | Y | Stato |
| X4ASSIEME | VARCHAR2(20 CHAR) | N | Assieme |
| X4ATTALL | VARCHAR2(15 CHAR) | N | ID Attività Standard |
| X4ATTCAR | NUMBER | Y | Attività caricabile manualmente |
| X4CLASSECOMP | DATE | N | Classe Componente |
| X4DATAFINE | DATE | N | Data fine validità |
| X4DATAINIZIO | FLOAT | N | Data inizio validità |
| X4GENATTFITT | NUMBER | Y | Generazione attività fittizia |
| X4ATTFITTASSOCITA | NUMBER | Y | ID Attività fittizia associata |
| X4MACROATTIVITA | NUMBER(10,2) | N | ID Macro-Attività |
| X4NORMDIRIF | NUMBER | N | Normativa di riferimento |
| X4PROGRAMMA | VARCHAR2(12 CHAR) | N | Programma |
| X4SOTTOASSIEME | VARCHAR2(150 CHAR) | N | Sottoassieme |
| X4TEMPOVAL | VARCHAR2(20 CHAR) | N | Max. tempo di validazione attività |
| X4TIPOCOMP | VARCHAR2(50 CHAR) | N | Tipo componente |
| X4TIPOLOGIAASSET | NUMBER | N | Tipologia Asset |
| X4ZONIZZAZIONE | VARCHAR2(192 CHAR) | N | Attività soggetta a zonizzazione |
| X4EFFORT | VARCHAR2(10 CHAR) | N | Effort |
| X4UM | VARCHAR2(3 CHAR) | N | UM |
|  |  |  |  |

La tabella JOBTASK riporta gli step operativi necessari per completare il piano di lavoro

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| JPNUM | VARCHAR2(12 CHAR) | Y | Identificativo univoco piano di lavoro |
| SITEID | VARCHAR2(8 CHAR) | N | Sede piano di lavoro |
| PLUSCJPREVNUM | NUMBER | Y | Revisione del piano |
| DESCRIPTION | VARCHAR2(100 CHAR) | N | Descrizione del task |
| TASKDURATION | FLOAT | Y | Durata Stimata per completare attività |
| X3WORKFLOW | VARCHAR2(50 CHAR) | N |  |
| X3MICROCONTROLLO | VARCHAR2(15 CHAR) | N |  |

La tabella WOSTATUS storico dello stato dell'ordine.

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| WONUM | VARCHAR2(25 CHAR) | Y | Ordine di lavoro |
| STATUS | VARCHAR2(16 CHAR) | Y | Nuovo stato dell’ODL |
| CHANGEDATE | DATE | Y | Data del cambio |
| CHANGEBY | VARCHAR2(100 CHAR) | Y | Utente che ha effettuato cambio |
| MEMO | VARCHAR2(50 CHAR) | N | Testo facoltativo |
| GLACCOUNT | VARCHAR2(110 CHAR) | N | Codice di conto contabile |
| FINCNTRLID | VARCHAR2(8 CHAR) | N | ID controllo finanziario Maximo |
| ORGID | VARCHAR2(8 CHAR) | Y | Identificativo Sito asset |
| SITEID | VARCHAR2(8 CHAR) | Y | Identificativo Organizzazione |
| WOSTATUSID | NUMBER | Y | stato dell'ordine di lavoro |
| PARENT | VARCHAR2(25 CHAR) | Y | Stato Padre del record |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo SQL |

La vista WOACTIVITY sono contenuti i singoli task dell'ordine di lavoro.

La tabella ASSET ed ASSET_RELATION Anagrafica degli asset censiti a sistema (l'asset viene riportato sull'ordine di lavoro, nell'attributo ASSETNUM)

La tabella X4MICROCONTROLLI Anagrafica dei micro-controlli richiamati all'interno del jobtask (X4MICROCONTROLLO)

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ASSIEME | Varchar2(20) | N |  |
| CONTROLLO | Varchar2(3) | N | del controllo |
| DESCRIPTION | Varchar2(50) | N | Descrizione Controllo |
| FREQUENZA | NUMBER | N | Frequenza del controllo |
| IDMICROCONTROLLO | Varchar2(15) | N | ID descrittivo del controllo |
| SOTTOASSIEME | Varchar2(20) | N |  |
| STATUS | Varchar2(15) | N | Stato del controllo |
| TIPOLOGIAASSET | Varchar2(192) | N | Tipo Asset |
|  |  |  |  |
|  |  |  |  |

# Tabelle Difformità

In ambito difformità oltre al catalogo difformità riportato nella tabella X3CATDIFFORMITA di seguito sono riportate le tabelle in ambito difformità

La tabella X4DIFFORSTAT persiste difformità rilevate su asset

X4DIFFORSTAT:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ASSETNUM | VARCHAR2(25 CHAR) | Y | Numero identificativo dell'ordine di lavoro |
| CONFERMATO | NUMBER | Y |  |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione della difformità su asset |
| GRAVITA | VARCHAR2(50 CHAR) | Y | Valore della gravità |
| HASLD | NUMBER | N | Campo Maximo |
| IDDIFFORMITA | VARCHAR2(15 CHAR) | N | Id della difformità legata |
|  |  |  |  |
| NONCONFERMATO | NUMBER | N | Flag di conferma difformità su asset |
| ODLRISOLUTORE | VARCHAR2(25 CHAR) | N | ODL incaricato di chiudere difformità |
| PRIMARILEVAZIONE | DATE | N | Data della prima rilevazione |
| ROWSTAMP | VARCHAR2(40 CHAR) | N | Campo SQL |
| SITEID | VARCHAR2(8 CHAR) | N | Sede dell’asset |
| STATUS | VARCHAR2(15 CHAR) | N | Stato della difformità su asset |
| ULTIMARILEVAZIONE | DATE | N | Data dell’ultima rilevazione |
| X4DIFFORSTATID | NUMBER | Y | Indice tabellare |
| CAUSALE | VARCHAR(50) | N | Causale di superamento |
| FASE | VARCHAR(1) | N | Flag per indicare quale fase è in uso |
| CHANGEBY | VARCHAR(100) | N | Matricola validatore |
| IDMISURA | VARCHAR(30) | N | ID Misura |
| VALMISURA | NUMBER | N | Valore di misura |
| IDDIFFORMITA_EXT | VARCHAR(30) | N | Causale di superamento |

La tabella X4DIFFOR_WO persiste le difformità rilevate durante i controlli X4DIFFOR_WO

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ASSETNUM | VARCHAR2(25) | N | Asset |
| DESCRIPTION | VARCHAR2(50) | N | Descrizione |
|  |  |  |  |
| GRAVITA | VARCHAR2(50) | N | Gravita |
| IDDIFFORMITA | VARCHAR2(50) | N | FK alla tabella delle difformità X4CATDIFFOR |
| ROWSTAMP | VARCHAR2(40) | Y | Campo Sistema |
| STATUS | VARCHAR2(50) | N | Stato |
| SITEID | VARCHAR2(8) | N | SiteId associato all’assetnum. |
| WONUM | VARCHAR2(25) | N | Ordine di lavoro |
| X4DIFFOR_WOID | Number | Y | PK |
| X4DIFFORSTATID | NUMBER | N |  |
| DATAULTIMARIL | DATE | N | Data ultima rilevamento |
| RISOLTA | NUMBER | Y | Flag |
| FASE | VARCHAR(4) | N |  |
| IDMISURA | VARCHAR(30) | N |  |
| VALMISURA | NUMBER | N |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

La tabella X4DIFFATT in cui viene proposto per un piano di lavoro le difformità risolvibili per quel pdl.

X4DIFFATT

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ATTIVITA | VARCHAR2(12 CHAR) | N | Numero identificativo dell'ordine di lavoro |
| CHANGEDBY | VARCHAR2(100 CHAR) | N | Utente modificatore |
| CHANGEDDATE | DATE | N | Data di modifica |
| DIFFORMITA | VARCHAR2(15 CHAR) | N | Id difformità descrittivo |
| JPNUM | VARCHAR2(12 CHAR) | N | Id attività descrittivo |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Timestamp |
| SITEID | VARCHAR2(8 CHAR) | N | Id sito |
| X4DIFFATTID | NUMBER | N | Id tabellare |
| DIFFORMITAID | NUMBER | N |  |
| DESCRIPTION | VARCHAR(50) | N |  |

La tabella XCATASSOCIAZIONE tabella in cui viene proposto per un IDORIGINE (che può essere un JPNUM o un X4MICROCONTROLLO) e le difformità rilevabili per quel record

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione |
| HASLD | NUMBER | Y | Campo di sistema |
| IDDIFFORMITA | VARCHAR2(15 CHAR) | N | Id difformità descrittivo |
| IDINTERFERENZA | VARCHAR2(50 CHAR) | N | Id interferenza descrittivo |
| IDORIGINE | VARCHAR2(20 CHAR) | N | Id origine |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Timestamp |
| SITEID | VARCHAR2(8 CHAR) | N | Sito ID |
| TIPO | VARCHAR2(10 CHAR) | N | Tipologia di associazione |
| TIPOLOGIAASSET | VARCHAR2(192 CHAR) | N | Tipologia asset |
| X3CATASSOCIAZIONEID | NUMBER | Y | Id tabellare |
| IDUNIVOCOTARGET | NUMBER | N |  |
| TIPOORIGINE | VARCHAR(15) | N | Origine dell’associazione C=Controllo |

# Tabelle Interferenze

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

La tabella X4INTERFSTAT persiste le interferenze associate ad un determinato asset.

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ASSETNUM | VARCHAR2(25 CHAR) | N | Asset |
| NONCONFERMATO | NUMBER | Y | Stato se confermato o meno |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descritto |
| GRAVITA | VARCHAR2(50 CHAR) | N | Gravità |
| HASLD | NUMBER | Y | Campo di sistema |
| IDINTERFERENZA | VARCHAR2(15 CHAR) | N | Id interferenza descrittivo |
| IDINTERFERENZA_EXT | VARCHAR2(15 CHAR) | N |  |
| ODLRISOLUTORE | VARCHAR2(25 CHAR) | N | Odl risolutore |
| PRIMARILEVAZIONE | DATE | N | Data prima rivelazione |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Timestamp |
| SITEID | VARCHAR2(8 CHAR) | N | Id Sito |
| STATUS | VARCHAR2(15 CHAR) | N | Status |
| ULTIMARILEVAZIONE | DATE | N | Data ultima rilevazione |
| X4INTERFSTATID | NUMBER | Y | Id tabellare |
| CAUSALE | VARCHAR(50) | N | Causale di superamento |
| FASE | VARCHAR(1) | N | Fase dell’asset |
| ASSETNUM1LIV | VARCHAR(25) | N |  |
| CHANGEBY | VARCHAR(50) | N |  |

# Tabelle misure

La tabella X4MISUREWO Tabella contenente le misure rilevate all'interno di un'odl

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ASSETNUM | VARCHAR2(25) | N | Numero asset |
| DESCRIPTION | VARCHAR2(50) | N | Descrizione |
| DIFFORMITA | VARCHAR2(50) | N | Id Difformità Descrittivo |
| ROWSTAMP | VARCHAR2(40) | Y | Timestamp |
| SITEID | VARCHAR2(8) | N | Id Sito |
| WONUM | VARCHAR2(25) | N | Work Order number |
| X4MISUREWOID | Number | Y | Id tabellare |
| FASE | VARCHAR | N |  |
| VALUE | NUMBER | N | Valore |
| IDMISURA | VARCHAR(20) | N | Misura |

X4CATMISURE Tabella contiene il catalogo delle misure

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| CALCOLO | VARCHAR2(50 CHAR) | N | Tipologia di calcolo da eseguire |
|  |  |  |  |
| DESCRIPTION | VARCHAR2(200 CHAR) | N | Descrizione |
| IDMISURA | VARCHAR2(20 CHAR) | N | ID descrittivo della misura |
|  |  |  |  |
| RESET | NUMBER | Y | Campo reset (flag presente/assente) |
|  |  |  |  |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo di sistema |
| TIPOPARAMETRO | VARCHAR2(20 CHAR) | N | Tipologia del parametro (Misura, Variabile, Variabile di stato) |
| UM | VARCHAR2(5 CHAR) | N | Unità di misura |
| X4CATMISUREID | NUMBER | Y | ID Tabellare |
| TIPOLOGIAASSET | VARCHAR(192) | N |  |
|  |  |  |  |
|  |  |  |  |
| PERIODOCALCOLO | NUMBER | N |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

# Tabelle domini

Le tabelle delli domini ammissibili saranno alimentate tramite configurazione preventiva e da console. Le tabelle di riferimento sono:

MAXDOMAIN la tabella contiene l’anagrafica domini

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| CHANGED | VARCHAR2(1 CHAR) | N | Flag per verifica modifica record |
| DESCRIPTION | VARCHAR2(109 CHAR) | N | Descrizione del Dominio |
| DOMAINID | VARCHAR2(18 CHAR) | Y | Identificativo univoco |
| DOMAINTYPE | VARCHAR2(20 CHAR) | Y | Tipo dominio in Maximo |
| INTERNAL | NUMBER | Y |  |
| LENGTH | NUMBER | N | Lunghezza del DomainType |
| MAXDOMAINID | NUMBER | Y | ID della tabella MAXDOMAIN |
| MAXTYPE | VARCHAR2(8 CHAR) | N | Tipo dato in Maximo del dominio |
| NEVERCACHE | NUMBER | N |  |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo SQL |
| SCALE | NUMBER | N |  |

ALNDOMAIN la tabella contiene i valori dominio alfanumerico

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| DOMAINID | VARCHAR2(18 CHAR) | Y | Identificativo univoco del dominio (FK su MAXDOMAIN) |
| VALUE | VARCHAR2(254 CHAR) | Y | Valore del dominio |
| DESCRIPTION | VARCHAR2(140 CHAR) | N | Descrizione |
| SITEID | VARCHAR2(8 CHAR) | N | Sede dell’asset |
| ORGID | VARCHAR2(8 CHAR) | N | Identificativo Organizzazione |
| ALNDOMAINID | NUMBER | Y | ID univoco della tabella |
| VALUEID | VARCHAR2(300 CHAR) | Y | Concat DomainID\|Value |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo SQL |

NUMERICODOMAIN la tabella contiene i valori dominio numerico

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| DESCRIPTION | VARCHAR2(127 CHAR) | N | Descrizione del valore numerico. |
| DOMAINID | VARCHAR2(18 CHAR) | Y | Identificativo univoco del dominio (Fk con MAXDOMAIN) |
| NUMERICDOMAINID | NUMBER | Y | Univoco della tabella |
| ORGID | VARCHAR2(8 CHAR) | N | Identificativo organizzazione |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo SQL |
| SITEID | VARCHAR2(8 CHAR) | N | Sede dell’asset |
| VALUE | NUMBER(30,10) | Y | Valore numerico del dominio |
| VALUEID | VARCHAR2(300 CHAR) | Y | Concat DOMAINID\|VALUE |

La tabella CLASSSPEC permette di associare attributo e domini.

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| CLASSIFICATIONID | VARCHAR2(192 CHAR) | Y | Classificazione per ID del nodo. |
| CLASSSTRUCTUREID | VARCHAR2(26 CHAR) | Y | ID del nodo |
| CLASSSTRUCTUREUID | NUMBER | Y | Identificatore Univoco |
| DESCRIPTION | VARCHAR2(150 CHAR) | N | Descrizione |
| GENASSETDESC | NUMBER | Y | Descrizione dell’asset generata |
| HASCHILDREN | NUMBER | Y | Flag per verificare che record abbia record collegati in tabella |
| HASLD | NUMBER | Y | Campo Maximo |
| LANGCODE | VARCHAR2(4 CHAR) | Y | Codice codifica Lingua |
| ORGID | VARCHAR2(8 CHAR) | N | ID dell’organizzatore |
| PARENT | VARCHAR2(26 CHAR) | N | correlato |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y | Campo SQL |
| SHOW | NUMBER | Y | Flag per mostrare\non mostrare record |
| SHOWINASSETTOPO | NUMBER | N | Flag per mostrare\non mostrare record in assetti |
| SITEID | VARCHAR2(8 CHAR) | N | Sede dell’asset |
| SORTORDER | NUMBER | N | Ordine di sorting |
| TYPE | VARCHAR2(10 CHAR) | N | Definisce il tipo dato del nodo |
| USECLASSINDESC | NUMBER | Y | Flag per mostrare descrizione della classificazione |
| X3CLASSIDREL | VARCHAR2(30 CHAR) | N | Classificazioni relazionate |
| X3GESTSTAT | NUMBER | Y | Gestione degli Stati |
| X3MAXNUM | NUMBER | N | Massima Numerosità |
| X4ELEMENTO | VARCHAR2(20 CHAR) | N | Cluster elemento |

# Tabelle stato asset ed eccezioni

In fase di creazione ordine sarà necessario verificare la coerenza dello stato asset. Le seguenti tabelle saranno interrogate per verificare la coerenza

Per permettere la visione dei campi necessari alla “Tabella di corrispondenza Asset – Stato idoneo” si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4TABIDONEITA”, si dovranno configurare:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| TIPOLOGIAASSET | VARCHAR2(192 CHAR) | Y | Tipologia asset |
| STATOIDONEITA | VARCHAR2(50 CHAR) | Y | Stato di idoneità |
| X4TABIDONEITAID | NUMBER | N | ID tabellare univoco progressivo |

Il campo STATOIDONEO mostrerà solamente gli stati associati alla classificazione asset precedentemente selezionata (TIPOLOGIAASSET).

Per permettere la visione dei campi necessari alla “Tabella eccezioni” si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4TABECCEZIONI”, si dovranno configurare:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| TIPOLOGIAASSET | VARCHAR2(192 CHAR) | Y | Tipologia asset |
| IDATTIVITA | NUMBER | Y | ID attività tabellare |
| IDATTIVITA_EXT | VARCHAR2(25) | N | Id attività formato descrittivo |
| STATOIDONEO | VARCHAR2(26 CHAR) | Y | Stato di idoneità |
| X4TABECCEZIONID | NUMBER | N | ID tabellare univoco progressivo |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione |

Il campo TIPOLOGIAASSET mostrerà solamente gli stati associati al PdL precedentemente selezionato (IDATTIVITA); mentre, il campo STATOIDONEO risulterà filtrato solamente in base agli stati associati alla classificazione asset precedentemente selezionata (TIPOLOGIAASSET).

# Policy e funzionalità gestite dal DSS

# Tipi policy gestite

Il DSS gestirà policy periodiche e policy on condiction gestite da eventi.

Per policy periodiche si intendono le policy che su base schedulazione il DSS verificherà e sono identificate dal campo “TIPOPOLICY = ‘PERIODICA’”

Per policy on-condition o non periodiche gestite si intendono le policy che saranno innescate su base evento sulla piattaforma. Gli eventi che possono innescare il calcolo delle policy sono legati sono aggiornamenti (inserimento/cancellazione/modifica) di entità che determinano il calcolo stesso della policy. Le policy calcolate ad evento potrebbero quindi innescare il calcolo di una stessa policy su molteplici asset (come nel caso di una modifica ad una condizione di una policy esistente) oppure il calcolo di un gruppo di policy su un terminato asset (come nel caso di una nuova difformità rilevata su un asset).

Le policy possono avere diverse combinazioni di input e target di raffronto. Per questo motivo è possibile distinguere le policy in:

Policy elementari: definite utilizzando un singolo input associato a un singolo target di raffronto. In questo caso sarà valorizzata la tabella X4POLICY con la policy associata e la tabella X4POLICYINPUT con una singola riga che rappresenterà la singola condizione elementare ed un livello di combinazione pari a zero. Tali policy non valorizzeranno i campi X4AND, X4OR, X4EQUAL X4NOTEQUAL della tabella X4POLICYINPUT

Policy combinate: definite utilizzando un singolo input associato a un singolo target di raffronto, in questo caso sarà valorizzata la tabella X4POLICY con la policy associata e la tabella X4POLICYINPUT con molteplici righe che rappresenteranno le condizioni combinate ordinate anche da un livello di combinazione che varia tra 1 e 3. In questa casistica le righe della tabella della saranno combinate tramite i campi X4AND, X4OR, X4EQUAL X4NOTEQUAL della tabella X4POLICYINPUT.

Queste policy si distinguono poi in:

Policy con input unico e target di raffronto multiplo

Policy con input multiplo e target di raffronto multiplo;

Policy aggregate: gestiscono le casistiche per cui la generazione di attività di sostituzione non dipende strettamente dalle condizioni del singolo asset, ma da fattori legati alle condizioni di tutti gli asset di stessa tipologia di una intera linea aerea o stazione. Una policy aggregata sarà caratterizzata da un flag “Policy aggregata” colonna POLICYAGGR della tabella X4POLICY e “Livello aggregazione” colonna LIVAGGR della tabella X4POLICY. Il livello di aggregazione indicherà la sostituzione di un terminato tipo di asset (possibile strettamente solo per Conduttori, funi di guardia, sostegni, stazioni). Le policy di questo tipo saranno caratterizzate da K eleggibilità e target di soglia. Questo ultimo dato mantenuto nella tabella x4policyimput target raffronto

Nel capitolo 3.5.2 sono riportati i dettagli di come ogni policy è definibile.

# Parametri policy e consistenza

Il paragrafo descrive nel primo sottoparagrafo una policy come è configurabile nel secondo sottoparagrafo quali dati dell’asset sono estratti per verificare le condizioni espresse dalla policy.

# Parametri policy

Ogni policy sarà costituita da un insieme di formule identificate da:

Variabile

Tipologia variabile

Funzione

Valore di comparazione

A. Una variabile di una formula che costituisce una policy potrà essere scelta solo all’interno di questa lista di entità

Un Health Index

Un indice di tassi di guasto

Un indice di tasso di anomalia

Un attributo tra gli attributi anagrafica asset

Una attività censite nel catalogo attività

Una difformità censita del catalogo difformità

Una interferenza censita nel catalogo interferenza

Una variabile di stato

B. Le tipologie di variabile saranno compresi in questa lista:

DIFFORMITA: tipologia che indica che la condizione è espressa su difformità

INTERFERENZA: tipologia che indica che la condizione è espressa su una interferenza

HI: tipologia che indica che la condizione è espressa su un health index

TASSOGUASTO: tipologia che indica che la condizione è espressa su un tasso di guasto

TASSOANOMALIA: tipologia che indica che la condizione è espressa su un tasso di anomalia

ATTIVITA: tipologia che indica che la condizione è espressa su una attività

ATTRIBUTO: tipologia che indica che la condizione è espressa su un attributo dell’asset. Attributi presenti tabella ASSETSPEC

VARIABILE: tipologia che indica che la condizione è espressa su una varabile tipicamente una misura da comparare con valori numerici espressi nella condizione oppure da una soglia modello il cui valore sarà da ricercare nel catalogo soglie modello

C. Le funzioni saranno strettamente le seguenti:

| Lista C, funzioni | Dettaglio |
| --- | --- |
| DISPARI | Restituisce true se il valore numerico indicato dalla variabile è dispari altrimenti false |
| PARI | Restituisce true se il valore numerico indicato dalla variabile è pari altrimenti false |
| NOT EXIST | Funzione “non esiste”, ritorna true se l’operando su cui è applicato è null |
| EXIST | Funzione “esiste”, ritorna true se l’operando su cui è applicato è diverso da null |
| < | Minore |
| <= | Minore uguale |
| > | Maggiore |
| >= | Maggiore uguale |
| = | Uguale |
| != | Diverso |

Valore di comparazione: il valore di comparazione potrà essere:

un valore numerico colonna TARGETRAFFRONTONUM

un valore alfanumerico colonna TARGETRAFFRONTO

un modello soglia: ossia una soglia che è definita sulla base del modello dell’asset a cui è applicato. Il valore è presente sulla tabella X4CATMODELLOASSET nella colonna N1. Infatti, in questa tabella è possibile riconoscere il modello attraverso la valorizzazione degli attributi. Ogni modello avrà un cluster che lo contraddistinguerà e permetterà di aggregare le caratteristiche del modello tramite le colonne ATTRIBUTO e VALUE. Quindi il valore della soglia sarà infatti attraverso il match del valore degli attributi del particolare asset.

Le colonne della tabella X4POLICYINPUT che entrano in gioco per la definizione di una espressione del DSS sono:

NINPUT: usata per rimpiazzare il valore dell’espressione nella formula riportata dalla policy.

IDINPUT: la chiave o il codice di ricerca da usare per trovare un valore riferibile all’asset oggetto di verifica della policy.

TIPOLOGIAINPUT: corrispondente alla TIPOLOGIA indicata al punto b) di questo paragrafo

TARGETRAFFRONTO: il termine noto con il quale confrontare il valore individuato con i dati precedenti.

OPERATORE: funzione matematica da applicare ai due operandi. I valori sono elencati alla lettera c) di questo paragrafo.

Nell’ambito della stessa policy ogni formula sarà correlabile con le altre formule che costituiscono la policy tramite strettamente i seguenti operatori:

Operatore uguale (=)

Operatore diverso (!=)

Operatore booleano AND

Operatore booleano OR

Esempi:

Inserimento di una policy che verifichi la presenza di una difformità per un asset:

| NINPUT | TIPOLOGIAINPUT | IDINPUT | OPERATORE | TARGETRAFFRONTO |
| --- | --- | --- | --- | --- |
| 1 | DIFFORMITA | Diff.L.2.1.2.26 | EXIST |  |

Inserimento di una policy che verifichi l’assenza di una difformità per un asset:

| NINPUT | TIPOLOGIAINPUT | IDINPUT | OPERATORE | TARGETRAFFRONTO |
| --- | --- | --- | --- | --- |
| 1 | DIFFORMITA | Diff.L.2.1.2.26 | NOT_EXIST |  |

Inserimento di una policy che si attiva se la gravità di una difformità è pari ad un determinato valore (operazione di uguaglianza tra stringhe)

| NINPUT | TIPOLOGIAINPUT | IDINPUT | OPERATORE | TARGETRAFFRONTO |
| --- | --- | --- | --- | --- |
| 1 | DIFFORMITA | Diff.L.2.1.2.6 | = | Grave |

Inserimento di una policy che si attiva se il valore di un health index è maggiore, maggiore uguale, minore o minore uguale (diseguaglianza tra valori numerici):

| NINPUT | TIPOLOGIAINPUT | IDINPUT | OPERATORE | TARGETRAFFRONTO |
| --- | --- | --- | --- | --- |
| 1 | HI | Ind.HI.1 | >,>=,<,<= | 10 |

Inserimento di una policy che si attiva in presenza di una misura o di una specifica relativa al modello di un asset:

| NINPUT | TIPOLOGIAINPUT | IDINPUT | OPERATORE | TARGETRAFFRONTO | IDMISURA |
| --- | --- | --- | --- | --- | --- |
| 1 | DIFFORMITA | Diff.L.1.1.1.3 | =,>,<,!=,>=,<= | 1 |  |

Inserimento di una policy che si attiva se la gravità di una difformità è pari ad un determinato valore (operazione di uguaglianza tra stringhe)

| NINPUT | TIPOLOGIAINPUT | IDINPUT | OPERATORE | TARGETRAFFRONTO |
| --- | --- | --- | --- | --- |
| 1 | DIFFORMITA | Diff.L.2.1.2.6 | = | Grave |
|  |  |  |  |  |

# Consistenza asset su cui saranno calcolate le policy

Il DDS provvederà a calcolare le policy a partire dai seguenti attributi della consistenza asset

Health Index: L'Health Index associato al singolo asset.

Tabelle da cui recuperare le informazioni: AHSCORE

Per recuperare gli Health Index relativi all’asset useremo la seguente query:

SELECT

s.ahscoreid,

s.objectname,

s.attributename,

s.ownerrecordid,

s.value

FROM ahscore s

WHERE s.ownerrecordid = :assetnum;

Tasso di Guasto (TdG) e tasso di anomalie (TdA) : Tasso di guasto e Anomalia associato al singolo asset.

Tabelle da cui recuperare le informazioni: AHSCORE.

SELECT

s.ahscoreid,

s.objectname,

s.attributename,

s.ownerrecordid,

s.value

FROM ahscore s

WHERE s.ownerrecordid IN (SELECT locationsid FROM locations WHERE classstructureid=:classstructureid);

Attributi: Tutti gli attributi associati al singolo asset.

Tabelle da cui recuperare le informazioni: ASSETATTRIBUTE, ASSETSPEC

Per recuperare gli attributi relativi all’asset useremo la seguente query:

SELECT s.assetspecid,

s.assetnum,

s.siteid,

s.classstructureid,

a.datatype,

a.measureunitid,

s.numvalue,

s.alnvalue,

s.tablevalue,

a.assetattrid,

a.description,

a.assetattributeid

FROM assetspec s

JOIN assetattribute a

ON s.assetattrid = a.assetattrid

WHERE s.assetnum = :assetnum;

Questi sono gli attributi che compaiono nelle policy

Asset: Tutti i dettagli del singolo asset.

Tabelle da cui recuperare le informazioni: ASSET, CLASSSTRUCTURE

|  |  |  |
| --- | --- | --- |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |

Per recuperare :

SELECT

a.ANCESTOR,

a.ASSETNUM,

a.CLASSSTRUCTUREID,

cl.CLASSIFICATIONID,

a.parent,

a.status,

a.X3ROOTASSETNUM,

ASSET.X3BUKRS

,ASSET.X3EQFNR

,ASSET.X3GEWRK

,ASSET.INSTALLDATE

,ASSET.X3INGRP

,ASSET.X3KOKRS

,ASSET.X3KOSTL

,ASSET.DESCRIPTION

,ASSET.X3SAPID

,ASSET.STATUS

,ASSET.X3SWERK

,ASSET.X3ZZSOCP

,ASSET.X3COSTRUTTORECOMPONENTE

,ASSET.X3FUNZIONEELEMENTO

,ASSET.X3RIFERIMENTOFASE

,ASSET.X3TIPOCOMPONENTE

FROM Asset a

JOIN

ClassStructure cl

ON a.CLASSSTRUCTUREID = cl.CLASSSTRUCTUREID

WHERE a.assetNum = :assetNum

Attività: Le attività legate al singolo asset.

Tabelle da cui recuperare le informazioni: WORKORDER, JOBPLAN, X4POLICY

Per recuperare le attività relative all’asset useremo la seguente query:

SELECT w.wonum,

w.assetnum,

w.status,

w.jpnum,

w.reportDate,

j.description,

j.jpduration,

j.templatetype,

p.eleggibilita,

w.actfinish,

w.x4policy,

w.x4datamodello

FROM WORKORDER w

JOIN JOBPLAN j

ON w.jpnum = j.jpnum

AND ((j.orgid = w.orgid AND j.siteid = w.siteid)

OR (j.orgid = w.orgid AND j.siteid IS NULL)

OR (j.orgid IS NULL AND j.siteid IS NULL))

AND j.status IN (

SELECT value

FROM synonymdomain

WHERE domainid = 'JOBPLANSTATUS'

AND maxvalue = 'ACTIVE'

)

JOIN X4Policy p

ON w.X4POLICYID = p.X4POLICYID

WHERE w.assetnum = :assetNum

AND w.X4POLICYID is not null

AND (

(w.status IN ('APERTO', 'SCHEDULABILE', 'SCHEDULATO', 'INOLTRATO', 'AVVIATO', 'ESEGUITO PAR', 'ESEGUITO'))

OR (w.status = 'CHIUSO' AND p.tipopolicy = 'PERIODICA')

Difformità: Le difformità rilevate per il singolo asset.

Tabelle da cui recuperare le informazioni: X4DIFFORSTAT, X3CATDIFFORMITA

Per recuperare le difformità relative all’asset useremo la seguente query:

SELECT s.x4difforstatid,

s.assetnum,

s.siteid,

d.x4catdifforid,

d.description,

d.iddifformita,

s.status,

s.gravita,

d.idmisura,

d.tipologia,

s.primaRilevazione

FROM x4difforstat s

JOIN x4catdiffor d

ON s.iddifformita = d.x4catdifforid

WHERE s.assetnum = :assetnum AND

s.status = 'VALIDATA';

Tabelle su cui vengono ricavati i target di difformità:

SELECT *

FROM x4TARGETDIFFOR

Interferenze: Le interferenze create in relazione al singolo asset.

Tabelle da cui recuperare le informazioni: X4INTERFSTAT, X4CATINTERF

Per recuperare le interferenze relative all’asset useremo la seguente query:

SELECT

s.x4interfstat,

s.assetnum,

s.siteid,

i.x4catinterfid,

i.description,

i.idinterferenza,

s.status,

i.idmisura,

i.misura,

i.tipologiaasset,

s.primaRilevazione,

s.gravita

FROM x4interfstat s

JOIN x4catinterf i

ON s.idinterferenza = i.x4catinterfid

WHERE s.assetnum = :assetnum AND s.status = 'VALIDATA';

Variabili: Tutte le variabili collegate all'asset.

Tabelle da cui recuperare le informazioni: X4MISUREASSET, X4CATMISURE.

Per recuperare le variabili relative all’asset useremo la seguente query:

SELECT

c.x4misureassetid,

c.assetnum,

c.siteid,

m.description,

m.x4catmisureid,

m.idmisura,

c.valore,

m.um,

m.tipoparametro

FROM x4misureasset c

JOIN x4catmisure m

ON c.idmisura = m.x4catmisureid

AND m.tipoparametro = 'Variabile di stato'

WHERE c.assetnum = :assetnum;

# Oggetti principali utilizzati dal DSS

l principali oggetti che utilizzerà il DDS sono i seguenti:

AssetData

Oggetto in cui salviamo la consistenza dell’asset. Troveremo tutti i dati relativi all’asset che potranno essere utili per la verifica della policy e la creazione degli ordini di lavoro.

public class AssetData {

private Asset

private Map<String, HealthIndexAsset> hi;

private Map<String, HealthIndexAsset> tassiGA;

private Map<String, List<InterferenzeAsset>> interferenze;

private Map<String, List<DifformitaAsset>> difformita;

private List<AttivitaAsset> attivita;

private Map<String, AttributiAsset> attributi;

private Map<String, VariabileAsset> variabili;

private Map<String, VariabileAsset> misure;

private Map<String, List<OpereInterferentiAsset>> opereInterferenti;

private Integer level;

}

Policy

Oggetto in cui troviamo tutto quella che riguarda la policy, quindi i suoi campi della tabella X4POLICY e i record della tabella X4POLICYINPUT:

public class X4Policy {

private Long x4PolicyId;

private Integer campagna;

private String changedBy;

private Date changedDate;

private String combInput;

private Date dataFineVal;

private Date dataInizioVal;

private String description;

private Integer differibilita;

private Integer eleggibilita;

private Integer frequenza;

private Integer hasLd;

private String idPolicy;

private String idTiming;

private Date lastReviewDate;

private Integer livAggr;

private Integer livComb;

private Integer livPolicy;

private Integer maxAnticipo;

private Integer maxPosticipo;

private Integer policyAggr;

private String rowStamp;

private String status;

private String tipologiaAsset;

private String tipoPolicy;

private String um;

private String x4RifTemporale;

private String x4Sottoprogramma;

private String x4Cluster;

private String siteId;

private String idMacroAttivita;

}

public class X4PolicyInput {

private Long x4PolicyInputId;

private String description;

private Integer hasLd;

private String idInput;

private String idMisura;

private Long idPolicy;

private Integer livInput;

private Integer nInput;

private String operatore;

private String rowStamp;

private String targetRaffronto;

private String tipologiaInput;

private String x4And;

private String x4Or;

private String x4Equal;

private String x4NotEqual;

private String idInputExt;

private String idMisuraExt;

private Double targetNum;

private String targetRaffrontoExt;

}

Gli oggetti che mappano i record delle tabelle X4Policy e X4policyInput sono trasformati in oggetti di classi più consone al calcolo e messe nella cache delle policy.

Le classi Policy e BinaryExpression:

public class Policy {

private Long id;

private String assetType;

private String code;

private String description;

private int differibilita;

private int eleggibilita;

private String formula;

private String macroActivityId;

private int maxAnticipo;

private int maxPosticipo;

private String status;

private PolicyTimeUnit timeUnit;

private String tipoPolicy;

private List<Input> inputs;

}

public class BinayExpression {

private Long id;

private PolicyOperator operator;

private Integer alias;

private PolicyInputType type;

private PolicyVariable leftVar;

private PolicyVariable rightVar;

}

PolicyOperator è una enum che può assumere uno dei seguenti valori: LESS_THAN, LESS_THAN_EQUAL, EQUAL, NOT_EQUAL, GREATER_THAN, GREATER_THAN_EQUAL, EXIST, NOT_EXIST, OP_OR, OP_AND, PARI, DISPARI.

PolicyInputType è un enumeration che può assumere uno dei seguenti valori: public enum INTERFERENZA, ATTIVITA, DIFFORMITA, COMBINAZIONE, VARIABILE, HI, ATTRIBUTO,

TASSOGUASTO, TASSOANOMALIA.

public class PolicyVariable {

private String name;

private VariableType type;

private String rawValue;

}

VariableType è una enum con i seguenti valori ammessi: ASSETSPEC, CATALOGO_DIFFORMITA, CATALOGO_INTERFERENZA, MISURA, MODELLO, HEALTH_INDEX, ATTIVITA, , VARIABILE, ALPHANUMERIC, NUMERIC.

Un esempio di una policy serializzata in formato JSON:

{

"_id": 1022,

"description": "test combinazione",

"activityId": "P.L.1.2.1.35",

"assetType": "CONDUTTORE",

"status": "PNDREV",

"formula": "##1@@ AND ##2@@",

"tipoPolicy": "ON-CONDITION",

"inputs": [

{

"_id": 1936,

"operator": "GREATER_THAN_EQUAL",

"alias": 1,

"type": "VARIABILE",

"leftVar": {

"name": "149",

"type": "VARIABILE"

},

"rightVar": {

"type": "NUMERIC",

"rawValue": "2.0"

}

}, {

"_id": 1937,

"operator": "EQUAL",

"alias": 2,

"type": "INTERFERENZA",

"leftVar": {

"name": "682",

"type": "CATALOGO_INTERFERENZA"

},

"rightVar": {

"type": "ALPHANUMERIC",

"rawValue": "urgenti"

}

}

]

}

CatalogTipologyModify

Oggetto che identifica l’elemento di catalogo modificato e che potrebbe essere relazionato a qualche policy:

public class CatalogTipologyModify {

private String type;

private String key;

}

Il dominio del campo type può assumere i seguenti valori:

POLICY

ATTIVITA

DIFFORMITA

INTERFERENZA

Il campo key identifica la chiave della X4POLICY, X4CATDIFFOR, X4CATINTERF, JOBPLAN

Order

private String assetnum;

private String stato;

private String jpnum;

private Long x4PolicyId;

private String idPolicy;

private String parent;

private Date dataModello;

private Date dataPianificataInizio;

private Date dataPianificataFine;

private Date dataMaxPosticipo;

private Date dataNonPrimaDi;

private List<String> tesatura;

private String tronconum;

private String x3tronchiid;

private String x4Trainante;

private String linea;

private String sapId;

private List<String> assetRelated;

private String x4Note;

private String x4OrdinePrevalente;

private Boolean aggregatore;

private Boolean prevalent;

private String wonum;

private String x4MicroControllo;

OrderActions

Oggetto azione su ordine contiene più Order e la azioni di add o update da eseguire:

public class OrderActions {

private List<Order> orders;

}

# Cache manager

Nella tabella sottostante in modo schematico sono riportati i dati che saranno inseriti su cache gestita dal ms dss-cache Redis based.

Nella colonna id cache è riportata la chiave di caching usata per inserire e recuperare i dati dalla cache.

Nella colonna “Contenuto” è riportato il riferimento all’oggetto inserito in cache

Nella colonna “Quando viene inserita” è riportato il metodo/use case in cui è caricato il dato in cache

Nella colonna “Quando eseguito l’evict” è riportato quando l’oggetto è eliminato dalla cache per forzare il ricaricamento

Nella colonna “Time to live”: è riportato la durata in cache dell’elemento. Superato questo intervallo il framework springboot cache forzerà il ricaricamento.

| ID cache [esprimere tutta la concatenazione] | Contenuto | Quando viene inserita | Quando eseguiamo l’evict | Time to live |
| --- | --- | --- | --- | --- |
| “policy”-<chiave policy> | Policy | Caricamento del metodo Policy loadPolicy | Inserimento(Modificata la policy su Maximo | 24h |
| MACRO_ATTIVITA_< ATTIVITA> | Lista delle attività candidate in funzione di una macroattvitià | Algoritmo recupera attività da macro-attività | Paragrafo 4.1.3 | 24h |
| <assetNum> | Asset | getAssetDataByAssetNum | Paragrafo 4.1.3 Paragrafo 4.1.4 Paragrafo 4.1.5 Paragrafo 4.1.6 Paragrafo 4.1.7 Paragrafo 4.1.10 Paragrafo 4.1.11 Paragrafo 4.1.12 Paragrafo 4.1.13 Paragrafo 4.1.14 Paragrafo 4.1.15 | 24h |
| <assetNum>_slim | Asset | getAssetDataByAssetNum | Paragrafo 4.1.3 Paragrafo 4.1.4 Paragrafo 4.1.5 Paragrafo 4.1.6 Paragrafo 4.1.7 Paragrafo 4.1.10 Paragrafo 4.1.11 Paragrafo 4.1.12 Paragrafo 4.1.13 Paragrafo 4.1.14 Paragrafo 4.1.15 | 24h |
| <classstructureid> | Livello dell’asset | Agoritmo check asset status |  | 24h |
| X4tabeccezioniid | Eccezione | Algoritmo check asset status | 4.1.48 | 24h |
| X4tabidoneita | Idoneità | Algoritmo check asset status | 4.1.48 | 24h |
| CACHE_MODELLO_ASSET | ModelloAsset | Algortmo valutazione policy | Paragrafo 4.1.39 | 24h |

# Api Maximo usate

Le api che saranno implementate per scrive su tabelle gestite da Maximo sono:

API POST oslc/os/X4WORKORDER?lean=1 IBM MAXIMO API REST nativa riceve in input una lista di ordini per i quali è definita un’azione, che può essere UPDATE o CREATE.

L’azione CREATE consente la creazione di nuovi ordini.

L’azione UPDATE permette di aggiornare la data di ordini esistenti.

Questa API è una funzionalità nativa di Maximo, che utilizza le sue REST API standard con inserimento BULK. Per ulteriori dettagli su come invocare questa API, fare riferimento al paragrafo e alla documentazione ufficiale di Maximo disponibile al seguente link: IBM Maximo REST API Documentation - Bulk Operations.

L’api che effettua il ricalcolo degli HI per un determinato asset è un api IBM Maximo nativa MXAPIASSET passando nella url "os/mxapiasset/<inserire risorsa/asset> ?action=CALCULATEHEALTH". Come da documentazione IBM Api MXAPIASSET

# Topic message key/body

In questo paragrafo è ripotato lo scheletro del body di ogni messaggio postato sui differenti topic:

topic-mas-handle-events: consumato dal dss-manager

La chiave del messaggio sarà “<stringa che indica il tipo di innesco>”-“<identificativo entity>”-<timestamp unix format>

La struttura del messaggio:

{

"tipology": "< valorizzato come ON_CONDICTION / PERIODIC a seconda della tipologia>",

"event": "<un valore nel dominio ADD/MODIFY>",

"type": "<un valore nel dominio POLICY/ASSET/DIFFORMITA/INTERFERENZA/

HI/MISURA/ORDINE/VARIABILE/

ASSETSPEC/ODL/MODELLO/ATTIVITA/HI>",

specialTRONCO/SOSTITUZIONE,

"policy": "<idpolicy valorizzato se si tratta di una policy>",

"asset": "<idAsset corrisponde all’assetnum>",

"wonum": "<key dell’odl>",

"attributes": {

"frequency": true,

"deferrability": true,

"maxAdvance": true,

"maxSnooze": true,

"others": true

"jpnum" : "<In caso di ATTIVITA indica l'attività modificata>",

"x4catdifforid": "<In caso di una DIFFORMITA modificata indica la chiave della tabella

X4CATDIFFOR.X4CATDIFFORID>",

"x4difforstatid": "<In caso di una misura della DIFFORMITA per un asset, indica la chiave della tabella

X4DIFFORSTAT.X4DIFFORSTATID>",

"x4catinterfid": "<In caso di una INTERFERENZA modificata indica la chiave della tabella X4CATINTERF.X4CATINTERFID>",

"x4interfstatid": "<In caso di una misura della INTERFERENZA per un asset, indica la chiave della tabella X4INTERFSTAT.X4INTERFSTATID>",

"x4misureassetid": "<In caso di una MISURA per un asset, indica la chiave della tabella X4MISUREASSET.X4MISUREASSETID>",

"x4catmodelloassetid": "<In caso di una modifica per un modello - target, indica la chiave della tabella X4CATMODELLOASSET.X4CATMODELLOASSETID>",

"hiattributename": "<In caso di una modifica di HI rappresenta l'attributo modificato request.attributename>",

"assetattrid": "<In caso di assetSpec, rappresenta l'attributo modificato"

}

}

Gli attributi obbligatori nel messaggio sono:

type

event

topic-mas-dss-workers: consumato da dss-worker

La chiave del messaggio è l’assetnum della linea/stazione sempre e solo primo livello. Nel caso di campate in doppia terna, la chiave è costituita dalla concatenazione degli asset separato da carattere “#”

Lo schema del messaggio:

{

"tipology": "< valorizzato come ON_CONDICTION / PERIODIC a seconda della tipologia>",

"policy": "<idpolicy>",

"event": "<un valore nel dominio MODIFY/ADD/TRONCO DI ISPEZIONE/SOSTITUZIONE

> ",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare la

stessa policy ma per asset differenti. Corrisponde al campo della tabella X4_DSS_WORKED_HISTORY.

DSS_PC_CORRELATION_ID >",

“dssWorkerId”: “id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY la tupla

di richiesta calcolo prevista. Se spenta X4_DSS_WORKED_HISTORY è un UUID”,

"asset": "<assetNum da elaborare è l’asset su cui è necessario effettuare il calcolo della policy>",

"livelloAggregazione": "<Valorizzato con X4POLICY.LIVAGGR. Indica che siamo in presenza di una policy aggregata>",

"attributes": {

"frequency": {

"oldValue": "<valore precedente>",

"newValue": "<nuovo valore >"

},

"deferrability": {

"oldValue": "<valore precedente>",

"newValue": "<nuovo valore >"

},

"maxAdvance": {

"oldValue": "<valore precedente>",

"newValue": "<nuovo valore >"

},

"maxSnooze": {

"oldValue": "<valore precedente>",

"newValue": "<nuovo valore >"

}

}, "order": [

{

"assetNum": "<id Assetnum>",

"stato": "<stato>",

"dataModello": "<valore data modello>",

"tesatura": [

"<assetnum tesatura 1>",

" assetnum tesatura 2",

" assetnum tesatura 3"

],

"X4Trainante": "<trainante/trainato"

}

]

}

Gli attributi obbligatori nel messaggio sono:

policy

event

correlationId

asset

topic-mas-handle-periodic: consumato da dss-manager.

La chiave del messaggio è il “<tipology>” + “ - “ timestamp unixformat:

Lo schema del messaggio:

{

“tipology”:” PERIODIC”,

"event": "CALCULATE",

"type": "POLICY",

}

Tutti gli attributi sono obbligatori.

topic-mas-handle-worker-feedback: consumato dal dss-manager. Questo topic è usato per leggere i feedback del worker nei casi di gestione algoritmo di sostituzione e tronco di ispezione.

La chiave del messaggio è il correlation_id "_12203994855"

Lo schema del messaggio:

{

"esito": "OK",

"tipology": "ON_CONDICTION",

"event": "SOSTITUZIONE",

"type": "POLICY",

"policy": "120",

"correlationId": "SOSTITUZIONE_12203994855",

"asset": "1920349",

"order": [{

"assetNum": "1920349"

"stato": "APERTO"

"dataModello": "16/10/2024",

"tesatura": ["1920349", "1920350", "1920351"]

}]

}

Tutti gli attributi sono obbligatori.

topic-mas-handle-attivita-change-event consumato dal dss-manager. Permette di gestire la notifica di un evento di cambio dell’attività.

La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"jpnum": "L.1.1.1.15",

"description": "Ripristino corretta posizione del fascio di conduttori",

"jpduration": "2.25"

"orgid": "ORG01",

"siteid": "SIT01",

"jobplanid": "386",

"langcode": "IT",

"status": "ACTIVE",

"interruptible

"hasld": "0",

"classstructureid": "1263",

"x4tipologiaasset": "ATTACCHI CONDUTTORE",

"x4programma": "SOSTATATR"

}

Gli attributi obbligatori nel messaggio sono:

jpnum

topic-mas-handle-diffor-change-event consumato dal dss-manager. Permette di gestire la notifica di evento di cambio difformità.

La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"x4catdifforid": "1170",

"description": "Rottura manicotti, barrette e coprilesioni",

"assieme": "rottura manicotti",

"iddifformita": "Diff.L.1.1.3.4",

"idmisura": "",

"sottoassieme": "",

"tempovalid": "7",

"tipologia": "Guasto",

"tipologiaasset": "PREFORMATO",

"rowstamp": "369775118",

"logicadiff": "",

"pesoaperte": "",

"pesochiuse": "",

"status": "ELIMINATA"

}

Gli attributi obbligatori nel messaggio sono:

x4catdifforid

topic-mas-handle-diffor-asset-change-event consumato dal dss-manager. Permette di gestire una notifica di modifica di una difformità associata ad un asset.

La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"x4difforstatid": "82",

"description": "Piante interferenti differibili (volume basso)",

"assetnum": "1221664",

"siteid": "SIT01",

"iddifformita": "2024-08-05 10:33:03.000",

"status": "APERTA",

"ultimarilevazione": "",

"primarilevazione": "2024-08-05 10:33:03.000",

"hasld": "0",

"rowstamp": "352770164",

"gravita": "",

"confermato": "",

"nonconfermato": "",

"odlrisolutore": "",

"iddifformita_ext": ""

}

Gli attributi obbligatori nel messaggio sono:

x4difforstatid

assetNum

topic-mas-handle-inter-change-event consumato dal dss-manager. Permette di gestire una notifica di cambio di una interferenza. La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"x4catinterfid": "343",

"description": "cedimenti strutturali paramassi/paravalanghe/gabbionate",

"hasld": "0",

"assieme": "area circostante",

"idinterferenza": "",

"idmisura": "int.l.1.0.0.1",

"logicaint": "",

"misura": "",

"sottoassieme": "",

"tempoval": "7",

"tipologiaasset": "CAMPATA AEREA",

"rowstamp": "369443940",

"status": ""

}

Gli attributi obbligatori nel messaggio sono:

x4catinterfid

topic-mas-handle-inter-asset-change-event consumato dal dss-manager. Permette di gestire la notifica del cambio di una interferenza associata ad un asset. La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"x4interfstatid": "604",

"description": "Piante interferenti (volume basso) - Catenaria sbandata",

"assetnum": "1221637",

"status": "VALIDATA",

"ultimarilevazione": "",

"primarilevazione": "2024-10-09 10:26:51.000",

"siteid": "SIT01",

"rowstamp": "383934614",

"idinterferenza": "510",

"gravita": "differibili",

"odlrisolutore": "",

"idinterferenza_ext": ""

}

Gli attributi obbligatori nel messaggio sono:

x4interfstatid

assetNum

topic-mas-handle-asset-change-event consumato dal dss-manager. Permette di gestire la notifica del cambio di un asset. La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"installdate": "2000-07-01T00:00:00+02:00",

"x3bukrs_description": "TR26 Terna Rete Italia S.p.A. Italia",

"x3kokrs" "replacecost": 0,

"x3ingrp": "ULN",

"status": "ATTV",

"x3kokrs_description": "Terna Italia",

"x3bukrs": "TR26",

"assetnum": "1618135",

"changeby": "XT3585",

"statusdate": "2024-10-02T17:41:00+02:00",

"x3dadoppiaterna": false,

"_event": true,

"pluscismte": false,

"x3msgrp": "2150309",

"siteid": "SIT01",

"assetuid": 1935072,

"status_description": "Attiva",

"changedate": "2024-10-03T19:22:43+02:00",

"orgid": "ORG01",

"x3gewrk": "DUMMY_01",

"description": "AURELIA - S. LUCIA 1,2",

"externalrefid": null,

"_action": "Replace",

"x3sapid": "21309B1",

"x3swerk": "2605",

"pluscclass": null,

"classstructureid": "1003",

"purchaseprice": 0,

"x3sapideq": "1020721473",

"isrunning": true,

"x3riferimentofase": null,

"assetid": 1935029,

}

Gli attributi obbligatori nel messaggio sono:

assetNum

topic-mas-handle-hi-asset-change-event consumato dal dss-manager. Permette di gestire la notifica del cambio di una health index associato ad un asset. La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"objectname": "ASSET",

"attributename": "1001",

"ownerrecordid": "1734733",

"value": "100",

"ahscoreid": "120",

"description": "HI Conduttori",

}

Gli attributi obbligatori nel messaggio sono:

ownerrecordid

topic-mas-handle-misure-asset-change-event consumato dal dss-manager. Permette di gestire la notifica del cambio di una misura associata ad un asset. La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"x4misureassetid": "183",

"description": "Test API",

"assetnum": "1734733",

"siteid": "SIT01",

"idmisura": "VAR.COND.f.1",

"valore": "58",

"hasld": "0",

"rowstamp": "381592460",

"primarilevazione": "",

"ultimarilevazione": "",

"iddifformita_ext": ""

}

Gli attributi obbligatori nel messaggio sono:

x4misureassetid

assetnum

topic-mas-handle-modelli-target-change-event consumato dal dss-manager. Permette di gestire la notifica del cambio di un modello. La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"x4catmodelloassetid": "2",

"description": "alluminio 13,5 mmq 108,79",

"attributo": "fili",

"value": "19",

"idcluster": "1",

"hasld": "0",

"n1": "aa",

"rowstamp": "398428325",

"x4catmodelloid": "1"

}

Gli attributi obbligatori nel messaggio sono:

x4catmodelloassetid

x4catmodelloid

topic-mas-handle-odl-create-event

consumato dal dss-manager. Evento notificato nel caso di creazione di un nuovo OdL. La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"wonum": "1096",

"status": "INPRG",

"statusdate": "2024-01-18 14:58:17.000",

"description": "Riparazione del conduttore",

"assetnum": "186135",

"jpnum": "L.1.1.1.3",

"changeby": "XT1012",

"changedate": "2024-01-19 11:50:24.000",

"_action": "Create"

}

Gli attributi obbligatori nel messaggio sono:

wonum

jpnum

assetnum

topic-mas-handle-assetspec-change-event consumato dal dss-manager. Evento notificato ad EAM a valle di una modifica di un assetspec. La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"assetnum": "1042",

"assetattrid": "LIN_ZONA_INQUINAMENTO",

"classstructureid": "1004",

"inheritedfromitem": "1",

"itemspecvalchanged": "0",

"displaysequence": "0",

"numvalue": "4",

"measureunitid": "",

"alnvalue": "",

"changedate": "2023-07-12 16:29:00.000",

"changeby": "XT4205",

"es01": "",

"es02": "",

"es03": "",

"es04": "",

"es05": "",

"siteid": "SIT01",

"orgid": "ORG01",

"section": "",

"assetspecid": "403",

"mandatory": "0",

"tablevalue": "",

"linkedtoattribute": "",

"linkedtosection": "",

"startassetfeatureid": "",

"startmeasure": "",

"startoffset": "",

"endassetfeatureid": "",

"endmeasure": "",

"endoffset": "",

"continuous": "0",

"endyoffset": "",

"startyoffset": "",

"endzoffset": "",

"startzoffset": "",

"startyoffsetref": "",

"startzoffsetref": "",

"endyoffsetref": "",

"endzoffsetref": "",

"basemeasureunitid": "",

"startbasemeasure": "",

"endbasemeasure": "",

"startmeasureunitid": "",

"endmeasureunitid": "",

"startoffsetunitid": "",

"endoffsetunitid": "",

"linearassetspecid": "0",

"rowstamp": "3466155",

"datevalue": "",

"x3datamodifica": "",

"anywhererefid": ""

}

Gli attributi obbligatori nel messaggio sono:

assetspecid

assetattrid

assetnum

topic-mas-handle-idoneita-change-event consumato dal dss-manager. Evento notificato ad EAM a valle di una modifica d. La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

Gli attributi obbligatori nel messaggio sono:

topic-mas-handle-eccezioni-change-event consumato dal dss-manager. Evento notificato ad EAM a valle di una modifica . La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

Gli attributi obbligatori nel messaggio sono:

topic-mas-handle-odl-closed-event consumato dal dss-manager. Evento notificato nel caso di chiusura di un OdL. La chiave del messaggio è un timestamp unix format. Il formato del messaggio è il seguente:

{

"wonum": "1096",

"status": "CHIUSO",

"statusdate": "2024-01-18 14:58:17.000",

"description": "Riparazione del conduttore",

"assetnum": "186135",

"jpnum": "L.1.1.1.3",

"changeby": "XT1012",

"changedate": "2024-01-19 11:50:24.000",

"_action": "replace"

}

Gli attributi obbligatori nel messaggio sono:

wonum

jpnum

assetnum

status = ‘CHIUSO’

# Configurazione Maximo eventi

La comunicazione tra Maximo e il Pod del Manager avviene utilizzando il Maximo Integration Frame (MIF). L’architettura del MIF è riportata nella figura in basso:

L’elemento principale del MIF è il Publish Channel che è lo strumento fondamentale per la comunicazione con i sistemi esterni. Tutte le operazioni di scrittura su un oggetto Business Objects danno origine a degli eventi che vengono acquisiti attraverso un Object Structure e successivamente inviati a un sistema esterno attraverso diverse modalità di comunicazione (kafka, http, Ejb, web service, etc.).

Nello specifico nell’architettura del DSS verrà usato kafka come strumento di comunicazione e a tale scopo sarà necessario configurare dei Topic e relativi Object Structure per permettere l’acquisizione degli eventi da Maximo e l’invio dei messaggi al pod manager (dss-manager).

Nella tabella sottostante verranno esplicitate le configurazioni necessarie al DSS:

Per gestire gli eventi di inserimento e modifica policy vedere paragrafo/use case dedicato 4.1.2

Di seguito un elenco degli elementi da configurare:

| TOPIC | Struct Object | Publish Channel | External System |
| --- | --- | --- | --- |
| topic-mas-handle-attivita-change-event | MHJOBPLAN | X4DSSPCJOBPLAN | X4DSSESJOBPLAN |
| topic-mas-handle-diffor-change-event | X4CATDIFFOR | X4DSSPCCATDIFFOR | X4DSSESCATDIFFOR |
| topic-mas-handle-diffor-asset-change-event | X4DIFFORSTAT | X4DSSPCDIFFORSTAT | X4DSSESDIFFORSTAT |
| topic-mas-handle-inter-change-event | X4CATINTERF | X4DSSPCCATINTERF | X4DSSESCATINTERF |
| topic-mas-handle-inter-asset-change-event | X4INTERFSTAT | X4DSSPCINTERFSTAT | X4DSSESINTERFSTAT |
| topic-mas-handle-asset-change-event | X4ASSETCHANGE | X4DSSPCASSETCHANGE | X4DSSESASSETCHANGE |
| topic-mas-handle-hi-asset-change-event | X4HICHANGE | X4DSSPCHICHANGE | X4DSSESHICHANGE |
| topic-mas-handle-misure-asset-change-event | X4MIGMISUREASSET | X4DSSPCMISUREASSET | X4DSSESMISUREASSET |
| topic-mas-handle-modelli-target-change-event | X4DSSCATMOD | X4DSSPCCATMODASSET | X4DSSESCATMOD |
| topic-mas-handle-odl-create-event | X4ODLCHANGE | X4DSSODLCHANGE | X4DSSESODLCHANGE |
| topic-mas-handle-assetspec-change-event | X4ASSETSPECCHANGE | X4DSSPCASSETSPECCHANGE | X4DSSESASSETSPECCHANGE |
| topic-mas-handle-policy-change-event | X4POLICYCHANGE | X4DSSPOLICYCHANGE | X4DSSESPOLICYCHANGE |
| topic-mas-handle-idoneita-change-event | X4IDONEITACHANGE | X4DSSIDONEITACHANGE | X4DSSESIDONEITACHANGE |
| topic-mas-handle-eccezioni-change-event | X4ECCEZIONICHANGE | X4DSSECCEZIONICHANGE | X4DSSESECCEZIONICHANGE |
| topic-mas-handle-odl-close-event | X4ODLCHANGE | X4DSSODLCLOSED | X4DSSESODLCLOSED |

# Configurazione CronTask policy periodiche

Il DSS prevederà il calcolo delle policy periodiche che saranno innescate tramite configurato avrà l’obiettivo di inviare un messaggio su topic-mas-handle-periodic passando come chiave del messaggio la chiave la stringa “CALCULATE-PERIODIC” e concatenata con il timestamp di innesco. Nel body del messaggio sarà presente il riferimento all’evento che ha innescato il messaggio. In particolare, il seguente json:

{

“tipology”:” PERIODIC”,

"event": "CALCULATE",

"type": "POLICY",

}

# Use cases

Il DSS sarà sollecitato a partire da eventi di calcolo policy on demand o periodici, l’obiettivo di questo capitolo è riportare l’elenco degli use case che il DSS gestirà. Non saranno gestiti casi differenti dagli scenari riportati.

# Elenco di tutti gli use case che gestirà il DSS

Gli eventi di calcolo periodico policy scatteranno su base temporale e richiederanno di valutare l’intero set di policy attive. Sulla base di particolari eventi saranno sollecitati invece i calcoli on demand delle policy.

Questi sono gli use case in corrispondenza dei quali scatterà il calcolo policy:

Caso creazione nuovo ordine che ha effetto su un asset – consistenza – add - ordine

Caso modifica catalogo policy elementare e combinata non associata ad attività di sostituzione/tronco – catalogo – update - policy

Caso modifica catalogo attività – catalogo – update - attività

Caso modifica catalogo difformità – catalogo – update – difformità

Caso registrazione difformità – consistenza – add - difformità

Caso modifica catalogo interferenza – catalogo – update – interferenza

Caso registrazione interferenza– consistenza – add - interferenza

Caso aggiunta catalogo nuova policy elementari e combinate on-condiction – catalogo – add - policy non associata ad attività di sostituzione

Caso aggiunta catalogo nuova attività – catalogo – add - attività

Caso aggiunta catalogo nuova difformità – catalogo – add – difformità

Caso aggiunta catalogo nuova interferenza – catalogo – add - interferenza

Caso disattivazione /eliminazione a catalogo di una policy – catalogo – delete - policy

Caso disattivazione catalogo di una attività – catalogo – delete - attività

Caso disattivazione catalogo di una difformità – catalogo – delete – difformità

Caso disattivazione catalogo di una interferenza – catalogo – delete - interferenza

Caso modifica formula HI – catalogo – update - HI

Caso modifica valore HI – consistenza – update - HI

Caso modifica formula tasso di guasto – catalogo – update - HI

Caso modifica formula tasso di anomalia – catalogo – update - HI

Casi di modifica valore tasso di guasto – consistenza – update - HI

Casi di modifica valore tasso di anomalia – consistenza – update HI

Caso modifica altri cataloghi – catalogo – update – altri cataloghi

Caso innesco policy periodica – consistenza - frequency

Caso registrazione difformità da cavi – consistenza – add - difformità

Caso registrazione interferenza da Elive – consistenza – add - interferenze

Caso registrazione difformità da altre fonti – consistenza – add - difformità

Caso registrazione interferenza da altre fonti – consistenza – add – interferenza

Caso registrazione difformità da gui/app

Caso registrazione interferenza da gui/app

Caso rilevamento misura – consistenza – add - misura

Caso zonizzazione – consistenza – update - asset

Caso aggiunta catalogo nuova policy aggregata – catalogo – add – policy aggregata

Caso modifica policy aggregata – catalogo – update – policy aggregata

Caso eliminazione policy aggregata – catalogo - delete – policy aggregata

Caso inserimento in anagrafica di un nuovo asset per il quale sono configurate policy – consistenza – add - asset

Risoluzione difformità – consistenza – delete - difformità

Risoluzione interferenze – consistenza – delete - interferenze

Caso di cambio stato della policy – consistenza – update - policy

Aggiornamento variabili di stato: Aggiornamento delle variabili dovuto alla chiusura di un’attività – consistenza – update – variabili

Caso modifica cataloghi modello-target/modelli soglia

Caso inserimento/modifica policy associata ad attività di sostituzione – odl trainante/trainato – catalogo – add – policy

Caso inserimento policy associata a campata/tratta cavo – gestione ordine ispezione

Caso modifica policy associata a campata/tratta cavo – gestione ordine ispezione

Caso difformità inserite su input del report macchinari/Ariel - consistenza – add – difformità

Caso Interferenze inserite su input del report macchinari/Ariel - consistenza – add – interferenze

Caso modifica anagrafica di asset esistente

Caso modifica stato di un asset esistente

Per ogni use case riportato sarà descritto nel paragrafo successivo l’algoritmo usato per il calcolo.

# Caso creazione nuovo ordine che ha effetto su un asset

A valle della creazione di un OdL per un determinato Asset, EAM invia un messaggio sulla coda kafka topic-mas-handle-odl-create-event tramite le configurazione riportate nel paragrafo :

{

"wonum": "1096",

"status": "INPRG",

"statusdate": "2024-01-18 14:58:17.000",

"description": "Riparazione del conduttore",

"assetnum": "186135",

"jpnum": "L.1.1.1.3",

"changeby": "XT1012",

"changedate": "2024-01-19 11:50:24.000",

"_action": "Create"

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events inviando il seguente messaggio:

{

"event": "MODIFY",

"type": "ODL",

"asset": "request.assetNum",

"wonum": "request.wonum",

"attributes": {

"jpnum" : "request.jpnum",

}

}

La chiave del messaggio è data dalla concatenazione della stringa “ODL-<request.wonum>-” più il timestamp in millisecondi.

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type e l’attributo wonum comprende che si tratta di una creazione di un nuovo OdL.

A questo punto è necessario capire se esiste una policy che può essere eseguita che tra i suoi input ha un attributo pari al jpnum in request. Le query da eseguire sono le seguenti:

SELECT c.CLASSIFICATIONID AS TIPOLOGIA_ASSET

FROM ASSET a, CLASSSTRUCTURE c

WHERE a.ASSETNUM = '<asset d’input>' AND

a.CLASSSTRUCTUREID = c.CLASSSTRUCTUREID

SELECT *

FROM X4POLICY A

WHERE STATUS = 'ATTIVA'

AND (TIPOLOGIAASSET = '<TIPOLOGIA_ASSET >'

OR A.X4POLICYID IN (

SELECT A.X4POLICYID

FROM X4POLICYCLASSIFICATIONS B

WHERE CLASSIFICATIONID = '<TIPOLOGIA_ASSET>'

AND B.X4POLICYID = A.X4POLICYID))

AND EXISTS (

SELECT 1 FROM X4POLICYINPUT R

WHERE R.TIPOLOGIAINPUT = 'ATTIVITA'

AND R.X4IDPOLICY = A.X4POLICYID

AND R.IDINPUT = '<jpnum d'input>')

A valle dell’esecuzione delle query, per ogni policy individuata è necessario rieseguire la policy per l‘asset d’input.

Come prima cosa dobbiamo individuare la tipologia di classificazione per l’attività d’input:

select classificationID AS TIPO_POLICY

from classstructure

where classstructureID in (

select classstructureid

from jobplan

where jpnum = '<X4POLICY.IDATTIVITA per singola policy individuata>')

Successivamente, è necessario svuotare la cache di Redis invocando il metodo boolean evictCache(CatalogTipology tipologyElement) usando come chiave è “<assetNum>” oppure “<assetNum>_slim” (vedi paragrafo 5.1.9 e paragrafo)

Per ogni policy identificata invi al dss-worker un messaggio sulla coda kafka topic-mas-dss-workers

La chiave del messaggio inviato corrisponde alla linea associata all’asset per il quale è avvenuta la creazione dell’ordine e in caso di doppia terna è dato dalla concatenazione delle linee sulla campata separate dal carattere “#”.

Il body del messaggio è il seguente:

{

"event": "Se (TIPO_POLICY = 'TRONCO DI ISPEZIONE' oppure TIPO_POLICY = 'SOSTITUZIONE') allora

valorizzare con TIPO_POLICY altrimenti MODIFY",

"policy": "<X4POLICYID per ogni policy individuata>",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare

la stessa policy ma per asset differenti. Corrisponde al campo della tabella X4_DSS_WORKED_HISTORY.

DSS_PC_CORRELATION_ID >",

“dssWorkerId”: “id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY

la tupla di richiesta calcolo prevista. Se spenta X4_DSS_WORKED_HISTORY è un UUID”,

"asset": <assetnum d'input è quello per il quel è stato creato l’ordine >

}

I messaggi inviati verranno salvati sul DB nella tabella X4_DSS_WORKED_HISTORY usando la “correlationId” definita nel messaggio. La logica che sarà implementata è quella dell’algoritmo per singola policy e singolo asset riportata nel paragrafo 5.1.11 nel caso non si tratti di policy di sostituzione or campata/tratta cavi.

Solo per lo scenario di tronco d’ispezione o sostituzione il manager deve attendere le risposte del worker, valutare l’esito d’elaborazione, effettuare attività di aggregazione dei messaggi ed inviare al worker un insieme di oggetti “Order” per l’inserimento massivo sul DB.

# Caso modifica catalogo policy elementare e combinata non associata ad attività di sostituzione/tronco

Nel caso di cambio stato è necessario eseguire l’evict della policy nel dss-cache invocando il metodo boolean evictCache(CatalogTipology tipologyElement) usando come chiave “policy”-<chiave policy> (vedi paragrafo 5.1.9 e)

L’evento di modifica policy sarà intercettato in Maximo come riportato nel paragrafo . inserendo un messaggio su topic topic-mas-handle-policy-change-event. Questo evento sarà consumato dal dss-manager per verificare se si tratta di un caso di policy che innesca gli algoritmi di sostituzione o trainante trainato.

Il formato del messaggio json è il seguente:

{

'x4policyid': '6088',

'description': 'Ripristino accesso al sostegno con eventuale diffida al proprietario',

'changedby': '',

'changeddate': '',

'datafineval': '',

'datainizioval': '',

'differibilita': '6',

'eleggibilita': '0',

'frequenza': '',

'idattivita': '',

'idpolicy': 'P.L.2.2.0.5',

'idtiming': '6M',

'lastreviewdate': '',

'maxanticipo': '0',

'maxposticipo': '1800',

'tipologiaasset': 'TELAIO',

'hasld': '0',

'rowstamp': '453125997',

'livpolicy': '3',

'livcomb': '0',

'um': '',

'tipopolicy': 'ON-CONDITION',

'policyaggr': '0',

'livaggr': '',

'campagna': '',

'combinput': '',

'status': 'ATTIVA',

'x4riftemporale': 'M',

'x4sottoprogramma': '',

'x4cluster': '',

'siteid': 'SIT01',

'apartireda': '',

'version': '1',

'idmacroattivita': 'AT.L.2.2.0.6'

}

L’evento modifica policy comporta modifiche su OdL esistenti solo in specifici casi che saranno dettagliati in questo scenario, ad esclusione dei campi indicati in caso di cambio policy non saranno apportate modifiche agli OdL esistenti e non saranno applicate logiche di chiusura. Quindi è necessario che in caso di modifica policy vengano riportati i campi modificati postando sul topic topic-mas-handle-events i riferimenti.

Tutte le richieste di policy con “status” != “ATTIVA” devono essere ignorati.

A tale scopo il manager eseguirà la seguente query:

SELECT DIFFERIBILITA,FREQUENZA,MAXANTICIPO,MAXPOSTICIPO

FROM A_X4POLICY xap

WHERE xap.X4POLICYID = '<request.x4policyid>'

AND xap.VERSION = (

SELECT max(VERSION)

FROM A_X4POLICY axp

WHERE xap.X4POLICYID = '<request.x4policyid>'

AND VERSION != '<request.version>'

AND STATUS = 'ATTIVA')

Dai dati della request in input e con l’output precedente query si valorizza il seguente body:

{

"tipology": "< valorizzato come ON_CONDICTION / PERIODIC a seconda della tipologia>",

"event": "MODIFY",

"type": "POLICY",

"policy": "<requet.x4policyid>",

"attributes": {

"frequency": "<FREQUENZA valorizzata && request.frequenza != FREQUENZA>",

"deferrability": "<DIFFERIBILITA valorizzata && request.differibilita != DIFFERIBILITA>",

"maxAdvance": "<MAXANTICIPO valorizzato && request.maxanticipo != MAXANTICIPO>",

"maxSnooze": <MAXPOSTICIPO valorizzato && request.maxposticipo != MAXPOSTICIPO>,

}

}

L’algoritmo verificherà quale tipo di attività associata. Nel caso particolare in cui l’attività associata alla policy sia di tipo “Sostituzione” l’algoritmo seguirà lo use case riportato nel paragrafo. In caso contrario l’algoritmo segue le logiche riportate in questo paragrafo.

L’algoritmo verificherà che la policy a quale tipo di attività sia associata. Nel caso particolare in cui l’attività associata alla policy sia una campata/tratta cavo l’algoritmo seguirà lo use case riportato nel paragrafo. In caso contrario l’algoritmo segue le logiche riportate in questo paragrafo.

Se nel messaggio il flag frequency è impostato a true significa che l’impatto ha effetto su una policy periodica, Per questa policy periodica è necessario riorganizzare l distribuzione degli ordini creati ed in particolare della data modello. Il flag frequency è in controfase con il flag deferrability che sarà associato ad una policy on-condiction. Il dss-manager interrogherà la tabella WORKORDER filtrando per colonna Status=“APERTO” e per POLICYID = <idpolicy passato in input al topic> ed estraendo tutti gli assetnum.

Se impostato il tracing calcolo a DB verrà interrogata la tabella X4_DSS_WORKED_HISTORY per verificare se per lo stesso messaggio sono state inserite delle tuple. Questa fase serve anche per gestire il corner case in cui per qualche motivo il messaggio non è la prima volta che viene consumato da kafka (viene gestito il caso di un riprocessamento a causa di un down del pod dss-manager precedente, che non ha permesso il commit del messaggio). L’algoritmo in questo caso terrà conto di questa casistica ricercando sulla tabella X4_DSS_WORKED_HISTORY il delta asset non inseriti (nel caso standard/nice-path nessun record trovato in tabella) ed inserendo un tupla per ogni delta asset ed inviando un messaggio su topic topic-mas-dss-workers

Con chiave l’assetnum della linea/stazione sempre e solo primo livello. Nel caso di campate in doppia terna, la chiave è costituita dalla concatenazione degli asset separato da carattere “#” e

body così costituito:

{

"tipology": "<valorizzato come ON_CONDICTION / PERIODIC a seconda della tipologia>",

"policy": "<idpolicy>",

"event": "MODIFY",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati

per calcolare la stessa policy ma per asset differenti. Corrisponde al campo della

tabella X4_DSS_WORKED_HISTORY. DSS_PC_CORRELATION_ID >",

"dssWorkerId": "id univoco che permette univocamente di individuare sulla tabella

X4_DSS_WORKED_HISTORY la tupla di richiesta calcolo prevista. Se spenta

X4_DSS_WORKED_HISTORY è un UUID",

"asset": "<assetNum da elaborare è l’asset su cui è necessario effettuare il calcolo della policy>",

"attributes": {

"frequency": {

"oldValue": "<a_x4policy.FREQUENZA>",

"newValue": "<x4policy.FREQUENZA>"

},

"deferrability": {

"oldValue": "<a_x4policy.DIFFERIBILITA>",

"newValue": "<x4policy.DIFFERIBILITA>"

},

"maxAdvance": {

"oldValue": "<a_x4policy.MAXANTICIPO>",

"newValue": "<x4policy.MAXANTICIPO>"

},

"maxSnooze": {

"oldValue": "<a_x4policy.MAXPOSTICIPO>",

"newValue": "<x4policy.MAXPOSTICIPO>"

}

}

}

Il dss-worker, seguirà la logica riportata nella prossima immagine. Nel messaggio è presente una foglia event: MODIFY che contribuirà a permettere al worker di individuare la sequenza di metodo da lanciare.

In caso di modifica di una policy è necessario prevedere le seguenti casistiche per ogni asset su cui è applicato il calcolo:

E’ associato all’asset un ordine che si trova in stato Aperto o Da validare e l’attività collegata all’ordine è la stessa attività associata alla policy in modifica (passando per la scelta da macroattivtà ad attività).

E’ associato all’asset un ordine che non si trova in stato Aperto e Da validare e l’attività collegata all’ordine è la stessa attività associata alla policy in modifica (passando per la scelta da macroattivtà ad attività).

Non è associato all’asset un ordine associato alla stessa attività associata alla policy

Se nel messaggio consumato è presente la foglia valorizzata con MODIFY è necessario passare prima per il metodo getActivityByMacroActivity e successivamente per il metodo 5.1.1 checkModifyPolicy. Quest’ultimo metodo verifica in quale dei 3 scenari si trova l’asset.

Nel caso 2 sarà completata l’elaborazione e scritto sulla tabella X4_DSS_WORKED_HISTORY l’esito dell’elaborazione senza creazione o modifica ordini.

Nel caso 3 l’algoritmo segue la stessa logica che avrebbe seguito se l’event non fosse stato MODIFY, quindi procede alla creazione dell’ordine ex-novo.

Nel caso 1 l’algoritmo provvederà a modificare gli ordini già esistenti sulla base della logica del metodo copyOrModifyOrder. Il metodo provvederà infatti a modificare l’ordine oppure gli ordini esistenti.

Nel caso 1 dopo aver invocato il metodo copyOrModifyOrder verrà restituito un oggetto OrdersActions che verrà passato in input al metodo dedicato al salva ordini boolean sendOrders(OrdersActions outputOrdersActions) passando in input la lista degli ordini da ripianificare/creare (vedi algoritmo 5.1.6)

# Caso modifica catalogo attività

La modifica del catalogo delle attività comporta uno svuotamento della cache di Redis.

Lo scenario prevede l’invio ad EAM di un messaggio sul topic topic-mas-handle-attivita-change-event per notificare il cambio dell’attività tramite le configurazione riportate nel paragrafo .

Il formato del messaggio conterrà i seguenti attributi:

{

"jpnum": "L.1.1.1.15",

"description": "Ripristino corretta posizione del fascio di conduttori",

"jpduration": "2.25"

"orgid": "ORG01",

"siteid": "SIT01",

"jobplanid": "386",

"langcode": "IT",

"status": "ACTIVE",

"interruptible

"hasld": "0",

"classstructureid": "1263",

"x4tipologiaasset": "ATTACCHI CONDUTTORE",

"x4programma": "SOSTATATR"

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events del dss-manager inviando il seguente messaggio:

{

"event": "MODIFY",

"type": "ATTIVITA",

"attributes": {

"jpnum" : "<request.jpnum>"

}

}

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type comprende che si tratta di una modifica di un’attività.

Dal messaggio viene acquisito il campo jpnum. Di seguito la query per identificare le policy associate all’attività:

SELECT X4POLICYID AS RESULT_ID

FROM X4POLICYINPUT XP, X4POLICY XP2

WHERE IDINPUT = '<jpnum>'

AND TIPOLOGIAINPUT = 'ATTIVITA'

AND XP2.X4POLICYID = XP.X4IDPOLICY

AND XP2.STATUS = 'ATTIVA'

Per ogni elemento RESULT_ID individuato svuotare la cache di Redis con chiave “policy”- < RESULT_ID >.

Di seguito la query per individuare gli asset associati all’attività:

SELECT

w.assetnum AS RESULT_ASSET_ID

FROM

WORKORDER w

JOIN JOBPLAN j ON

w.jpnum = j.jpnum

AND ((j.orgid = w.orgid

AND j.siteid = w.siteid)

OR (j.orgid = w.orgid

AND j.siteid IS NULL)

OR (j.orgid IS NULL

AND j.siteid IS NULL))

AND j.status IN (

SELECT value

FROM synonymdomain

WHERE domainid = 'JOBPLANSTATUS'

AND MAXVALUE = 'ACTIVE')

WHERE

w.jpnum = '<jpnum>'

Per ogni elemento RESULT_ASSET_ID individuato bisogna rimuovere le informazioni della cache di Redis invocando il metodo boolean evictCache(CatalogTipology tipologyElement) usando come chiave chiave è proprio RESULT_ASSET_ID oppure RESULT_ASSET_ID + “_slim”. (vedi paragrafo 5.1.9 e paragrafo)

# Caso modifica catalogo difformità

La modifica del catalogo delle difformità comporta uno svuotamento della cache di Redis. Lo scenario prevede l’invio ad EAM di un messaggio sul topic kafka topic-mas-handle-diffor-change-event per notificare il cambio della difformità. La configurazione è riportata nel paragrafo .

Di seguito il formato del messaggio:

{

"x4catdifforid": "1170",

"description": "Rottura manicotti, barrette e coprilesioni",

"assieme": "rottura manicotti",

"iddifformita": "Diff.L.1.1.3.4",

"idmisura": "",

"sottoassieme": "",

"tempovalid": "7",

"tipologia": "Guasto",

"tipologiaasset": "PREFORMATO",

"rowstamp": "369775118",

"logicadiff": "",

"pesoaperte": "",

"pesochiuse": "",

"status": "ELIMINATA"

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events inviando il seguente messaggio:

{

"event": "MODIFY",

"type": "DIFFORMITA",

"attributes": {

"x4catdifforid": "<request.x4catdifforid>"

}

}

La chiave del messaggio è data dalla concatenazione della stringa “DIFFORMITA-<request. x4catdifforid>-” più il timestamp in millisecondi.

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type e l’attributo x4catdifforid comprende che si tratta di una modifica al catalogo delle difformità.

Dal messaggio viene acquisito il campo x4catdifforid. Di seguito la query per identificare le policy associate alla difformità:

SELECT X4POLICYID AS RESULT_ID

FROM X4POLICYINPUT XP, X4POLICY XP2

WHERE IDINPUT = '< x4catdifforid >'

AND TIPOLOGIAINPUT = 'DIFFORMITA'

AND XP2.X4POLICYID = XP.X4IDPOLICY

AND XP2.STATUS = 'ATTIVA'

Per ogni elemento RESULT_ID è necessario svuotare la cache di Redis con chiave “policy”- < RESULT_ID >.

Di seguito la query per individuare gli asset associati alla difformità:

SELECT ASSETNUM AS RESULT_ASSET_ID

FROM X4DIFFORSTAT

WHERE IDDIFFORMITA = '< x4catdifforid >'

Per ogni elemento RESULT_ASSET_ID individuato bisogna rimuovere le informazioni della cache di Redis. La chiave è RESULT_ASSET_ID oppure RESULT_ASSET_ID + “_slim”.

# Registrazione difformità

EAM invierà un messaggio sul topic kafka topic-mas-handle-diffor-asset-change-event per notificare una modifica della difformità associata all’asset tramite le configurazione riportate nel paragrafo 8.

Il formato del messaggio è il seguente:

{

"x4difforstatid": "82",

"description": "",

"assetnum": "1221664",

"siteid": "SIT01",

"iddifformita": "2024-08-05 10:33:03.000",

"status": "APERTA",

"ultimarilevazione": "",

"primarilevazione": "2024-08-05 10:33:03.000",

"hasld": "0",

"rowstamp": "352770164",

"gravita": "",

"confermato": "",

"nonconfermato": "",

"odlrisolutore": "",

"iddifformita_ext": ""

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events inviando il seguente messaggio:

{

"event": "MODIFY",

"type": "DIFFORMITA",

"asset": "<request.assetnum>",

"attributes": {

"x4difforstatid": "<request.x4difforstatid>"

}

}

La chiave del messaggio è data dalla concatenazione della stringa “DIFFORMITA-<request. x4difforstatid>-” più il timestamp in millisecondi.

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type ed il campo asset comprende che si tratta di una modifica alla consistenza di una difformità associata ad un asset.

Dal messaggio ricevuto, come prima operazione è necessario rimuovere il singolo asset dalla cache di Redis usando il campo asset. Successivamente bisogna individuare le policy collegate a questa difformità la cui tipologia di asset è conforme con quella dell’asset d’input. Le query da eseguire sono le seguenti:

SELECT c.CLASSIFICATIONID AS TIPOLOGIA_ASSET

FROM ASSET a, CLASSSTRUCTURE c

WHERE a.ASSETNUM = '<asset d’input>' AND

a.CLASSSTRUCTUREID = c.CLASSSTRUCTUREID

SELECT *

FROM X4POLICY A

WHERE STATUS = 'ATTIVA'

AND (TIPOLOGIAASSET = '<TIPOLOGIA_ASSET>'

OR A.X4POLICYID IN (

SELECT A.X4POLICYID

FROM X4POLICYCLASSIFICATIONS B

WHERE CLASSIFICATIONID = '<TIPOLOGIA_ASSET>'

AND B.X4POLICYID = A.X4POLICYID))

AND EXISTS (

SELECT 1 FROM X4POLICYINPUT R

WHERE R.TIPOLOGIAINPUT = 'DIFFORMITA'

AND R.X4IDPOLICY = A.X4POLICYID

AND R.IDINPUT = '<x4difforstatid>')

Per ogni policy individuata bisogna calcolare la tipologia di classificazione:

select classificationID AS TIPO_POLICY

from classstructure

where classstructureID in (

select classstructureid

from jobplan

where jpnum = '< IDATTIVITA dalla precedente query >')

A questo punto, per ogni policy individuata è necessario inviare al worker un messaggio sulla coda kafka topic-mas-dss-workers. Il formato del messaggio è il seguente:

{

"event": "Se (TIPO_POLICY = 'TRONCO DI ISPEZIONE' oppure TIPO_POLICY = 'SOSTITUZIONE') allora valorizzare con TIPO_POLICY altrimenti MODIFY",

"policy": "<X4POLICYID>",

"correlationId": "<chiave del messaggio d’input>",

"asset": <asset d'input>

}

La chiave del messaggio è la linea e in caso di doppia terna è dato dalla concatenazione della linea sulla campata separate dal carattere “#”.

I messaggi inviati verranno salvati sul DB nella tabella X4_DSS_WORKED_HISTORY usando la “correlationId” definita nel messaggio. La logica che sarà implementata è quella dell’algoritmo per singola policy e singolo asset riportata nel paragrafo 5.1.11 nel caso non si tratti di policy di sostituzione or campata/tratta cavi.

Solo per lo scenario di tronco d’ispezione o sostituzione il manager deve attendere le risposte del worker, valutare l’esito d’elaborazione, effettuare attività di aggregazione dei messaggi ed inviare al worker un insieme di oggetti “Order” per l’inserimento massivo sul DB.

# Caso modifica catalogo interferenza

La modifica del catalogo delle interferenze comporta uno svuotamento della cache di Redis. Lo scenario prevede l’invio ad EAM di un messaggio sul topic kafka topic-mas-handle-inter-change-event per notificare il cambio della interferenz. La configurazione è riportata nel paragrafo .

Di seguito il formato del messaggio:

Il formato del messaggio conterrà i seguenti attributi:

{

"x4catinterfid": "343",

"description": "cedimenti strutturali paramassi/paravalanghe/gabbionate",

"hasld": "0",

"assieme": "area circostante",

"idinterferenza": "",

"idmisura": "int.l.1.0.0.1",

"logicaint": "",

"misura": "",

"sottoassieme": "",

"tempoval": "7",

"tipologiaasset": "CAMPATA AEREA",

"rowstamp": "369443940",

"status": ""

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events inviando il seguente messaggio:

{

"event": "MODIFY",

"type": "INTERFERENZA",

"attributes": {

"x4catinterfid": "<request.x4catinterfid>"

}

}

La chiave del messaggio è data dalla concatenazione della stringa “INTERFERENZA-<request. x4catinterfid>-” più il timestamp in millisecondi.

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type e l’attributo x4catinterfid comprende che si tratta di una modifica al catalogo delle interferenze.

Dal messaggio viene acquisito il campo x4catinterfid. Di seguito la query per identificare le policy associate all’interferenza:

SELECT X4POLICYID AS RESULT_ID

FROM X4POLICYINPUT XP, X4POLICY XP2

WHERE IDINPUT = '< x4catinterfid >'

AND TIPOLOGIAINPUT = 'INTERFERENZA'

AND XP2.X4POLICYID = XP.X4IDPOLICY

AND XP2.STATUS = 'ATTIVA'

Per ogni elemento RESULT_ID è necessario svuotare la cache di Redis con chiave “policy”- < RESULT_ID >.

Di seguito la query per individuare gli asset associati all’interferenza:

SELECT ASSETNUM AS RESULT_ASSET_ID

FROM X4INTERFSTAT xi

WHERE IDINTERFERENZA = '< x4catinterfid >'

Per ogni elemento RESULT_ASSET_ID individuato bisogna rimuovere le informazioni della cache di Redis. La chiave è RESULT_ASSET_ID oppure RESULT_ASSET_ID + “_slim”.

# Registrazione interferenza

EAM invierà un messaggio sul topic kafka topic-mas-handle-inter-asset-change-event per notificare una modifica della interferenza associata all’asset tramite le configurazione riportate nel paragrafo .

Il formato del messaggio è il seguente:

{

"x4interfstatid": "604",

"description": "Piante interferenti (volume basso) - Catenaria sbandata",

"assetnum": "1221637",

"status": "VALIDATA",

"ultimarilevazione": "",

"primarilevazione": "2024-10-09 10:26:51.000",

"siteid": "SIT01",

"rowstamp": "383934614",

"idinterferenza": "510",

"gravita": "differibili",

"odlrisolutore": "",

"idinterferenza_ext": ""

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events inviando il seguente messaggio:

{

"event": "MODIFY",

"type": "INTERFERENZA",

"asset": "<request.assetnum>",

"attributes": {

"x4interfstatid": "<request.x4interfstatid>"

}

}

La chiave del messaggio è data dalla concatenazione della stringa “INTERFERENZA-<request. x4interfstatid>-” più il timestamp in millisecondi.

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type ed il campo asset comprende che si tratta di una modifica alla consistenza di una interferenza associata ad un asset.

Dal messaggio ricevuto, come prima operazione è necessario rimuovere il singolo asset dalla cache di Redis usando il campo asset. Successivamente bisogna individuare le policy collegate a questa interferenza la cui tipologia di asset è conferme con quella dell’asset d’input. Le query da eseguire sono le seguenti:

SELECT c.CLASSIFICATIONID AS TIPOLOGIA_ASSET

FROM ASSET a, CLASSSTRUCTURE c

WHERE a.ASSETNUM = '<asset d’input>' AND

a.CLASSSTRUCTUREID = c.CLASSSTRUCTUREID

SELECT *

FROM X4POLICY A

WHERE STATUS = 'ATTIVA'

AND (TIPOLOGIAASSET = '<TIPOLOGIA_ASSET>'

OR A.X4POLICYID IN (

SELECT A.X4POLICYID

FROM X4POLICYCLASSIFICATIONS B

WHERE CLASSIFICATIONID = '<TIPOLOGIA_ASSET>'

AND B.X4POLICYID = A.X4POLICYID))

AND EXISTS (

SELECT 1 FROM X4POLICYINPUT R

WHERE R.TIPOLOGIAINPUT = 'DIFFORMITA'

AND R.X4IDPOLICY = A.X4POLICYID

AND R.IDINPUT = '<x4difforstatid>')

A questo punto, per ogni policy individuata è necessario inviare al worker un messaggio sulla coda kafka topic-mas-dss-workers. Il formato del messaggio è il seguente:

{

"event": "Se (TIPO_POLICY = 'TRONCO DI ISPEZIONE' oppure TIPO_POLICY = 'SOSTITUZIONE') allora valorizzare con TIPO_POLICY altrimenti MODIFY ",

"policy": "<X4POLICYID>",

"correlationId": "<chiave del messaggio d’input>",

"asset": <asset d'input>

}

La chiave del messaggio è la linea e in caso di doppia terna è dato dalla concatenazione della linea sulla campata separate dal carattere “#”.

I messaggi inviati verranno salvati sul DB nella tabella X4_DSS_WORKED_HISTORY usando la “correlationId” definita nel messaggio. La logica che sarà implementata è quella dell’algoritmo per singola policy e singolo asset riportata nel paragrafo 5.1.11 nel caso non si tratti di policy di sostituzione or campata/tratta cavi.

Solo per lo scenario di tronco d’ispezione o sostituzione il manager deve attendere le risposte del worker, valutare l’esito d’elaborazione, effettuare attività di aggregazione dei messaggi ed inviare al worker un insieme di oggetti “Order” per l’inserimento massivo sul DB.

# Caso aggiunta catalogo nuova policy elementari e combinate non associata ad attività di sostituzione o tronco d’ispezione

L’evento di creazione di una policy sarà intercettato in Maximo come riportato nel paragrafo . inserendo un messaggio su topic topic-mas-handle-policy-change-event. Questo evento sarà consumato dal dss-manager per verificare se si tratta di un caso di policy che innesca gli algoritmi di sostituzione o trainato/trainante.

Il formato del messaggio json è il seguente:

{

'x4policyid': '6088',

'description': 'Ripristino accesso al sostegno con eventuale diffida al proprietario',

'changedby': '',

'changeddate': '',

'datafineval': '',

'datainizioval': '',

'differibilita': '6',

'eleggibilita': '0',

'frequenza': '',

'idattivita': '',

'idpolicy': 'P.L.2.2.0.5',

'idtiming': '6M',

'lastreviewdate': '',

'maxanticipo': '0',

'maxposticipo': '1800',

'tipologiaasset': 'TELAIO',

'hasld': '0',

'rowstamp': '453125997',

'livpolicy': '3',

'livcomb': '0',

'um': '',

'tipopolicy': 'ON-CONDITION',

'policyaggr': '0',

'livaggr': '',

'campagna': '',

'combinput': '',

'status': 'ATTIVA',

'x4riftemporale': 'M',

'x4sottoprogramma': '',

'x4cluster': '',

'siteid': 'SIT01',

'apartireda': '',

'version': '1',

'idmacroattivita': 'AT.L.2.2.0.6'

}

L’evento creazione policy viene sottomesso sul topic topic-mas-handle-events passando come chiave del messaggio la chiave della nuova policy creata e concatenata con il timestamp di creazione policy.

“POLICY”-“<identificativo policy id>”-<timestamp unix format>

Nel body del messaggio sarà presente il riferimento all’evento che ha innescato il messaggio. In particolare, il seguente json:

{

“tipology”:”< valorizzato come ON_CONDICTION / PERIODIC a seconda della tipologia>”,

"event": "ADD",

"type": "POLICY",

“policy”: “<idpolicy>”

}

Il messaggio viene consumato, nel messaggio vengono inviate tutte le info associate alla nuova policy estratte dalle tabelle X4POLICY e X4POLICYINPUT oltre all’info che si tratta di una creazione il ms dss-manager identifica il livello di applicabilità della policy ad esempio livello conduttore.

L’algoritmo verificherà che la policy a quale tipo di attività sia associata. Nel caso particolare in cui l’attività associata alla policy sia di tipo “Sostituzione” l’algoritmo seguirà lo use case riportato nel paragrafo 4.1.41. In caso contrario l’algoritmo segue le logiche riportate in questo paragrafo.

L’algoritmo verificherà che la policy a quale tipo di attività sia associata. Nel caso particolare in cui l’attività associata alla policy sia una campata/tratta cavo l’algoritmo seguirà lo use case riportato nel paragrafo 4.1.42. In caso contrario l’algoritmo segue le logiche riportate in questo paragrafo.

Il dss-manager identifica, ad esempio, nel caso di una policy che impatta il tipo asset conduttore, tutti i conduttore presenti sulla piattaforma circa 500000 ed invia 500000 messaggi su topic topic-mas-dss-workers.

Prima di fare questa operazione se impostato il tracing calcolo a DB verrà interrogata la tabella X4_DSS_WORKED_HISTORY per verificare se per lo stesso messaggio sono state inserite delle tuple. Questa fase serve anche per gestire il corner case in cui per qualche motivo il messaggio non è la prima volta che viene consumato da kafka (viene gestito il caso di un riprocessamento a causa di un down del pod dss-manager precedente, che non ha permesso il commit del messaggio). L’algoritmo in questo caso terrà conto di questa casistica ricercando sulla tabella X4_DSS_WORKED_HISTORY il delta asset non inseriti (nel caso standard/nice-path nessun record trovato in tabella) ed inserendo un tupla per ogni delta asset ed inviando un messaggio su topic topic-mas-dss-workers.

Nel caso di errore durante l’elaborazione del messaggio su topic topic-mas-handle-events, sarà necessario prevedere un retry del messaggio per 2 volte dopo la prima, attraverso le annotation @RetryableTopic di Spring Kafka. Nel caso il messaggio non fosse processato dopo questi tentativi è necessario avvisare l’utente che ha lanciato la richiesta che non è stato possibile per il DSS elaborare l’aggiornamento della policy. Sarà necessario prevedere l’invocazione di metodo che traccia la mancata elaborazione. Il metodo chiamato provvederà inserimento ad inserire su tabella X4_DSS_POLICY_ERROR una tupla che permetterà di tracciare che il calcolo della policy è andato in errore. Questa tabella permetterà di tracciare i mancati inneschi di calcolo policy non gli errori generati durante il calcolo che saranno tracciati eventualmente su X4_DSS_WORKED_HISTORY.

Verrà inviato quindi su topic topic-mas-dss-workers come chiave l’id dell’asset di primo livello al quale è associato l’asset su cui agisce la policy. Nel caso di campate in doppia terna, la chiave è costituita dalla concatenazione degli asset separato da carattere “#”, mentre il body del messaggio la policy con tutte le regole da soddisfare:

{

"policy": "<idpolicy>",

"event": "ADD",

“asset”: “<assetnum>”,

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare la

stessa policy ma per asset differenti. Corrisponde al campo della tabella X4_DSS_WORKED_HISTORY.

DSS_PC_CORRELATION_ID >",

“dssWorkerId”: “id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY la tupla

di richiesta calcolo prevista. Se spenta X4_DSS_WORKED_HISTORY è un UUID”,

"asset": "<assetNum da elaborare è l’asset su cui è necessario effettuare il calcolo della policy>",

}

Saranno previste 20 partizioni del topic topic-mas-dss-workers. Al quale saranno agganciati 5-10 consumatori pods dss-worker [5 fissi e 5 in autoscale].

Ogni messaggio prodotto dal dss-manager viene consumato dal singolo pod ms dss-worker. La logica implementata è riportata nel paragrafo 5.1.11

# Caso aggiunta catalogo nuova attività

La creazione o modifica un un’attività comporta la cancellazione della cache MACRO_ATTIVITA_< ATTIVITA>.

# Caso aggiunta catalogo nuova difformità

L’aggiunta a catalogo di una nuova difformità non ha impatti su DSS

# Caso aggiunta catalogo nuova interferenza

L’aggiunta a catalogo di una nuova interferenza non ha impatti su DSS

# Caso disattivazione catalogo policy

L’eliminazione a catalogo di una policy non ha impatti su DSS

# Caso disattivazione catalogo attività

L’eliminazione a catalogo di una attività non ha impatti su DSS

# Caso disattivazione catalogo difformità

L’eliminazione a catalogo di una difformità non ha impatti su DSS

# Caso disattivazione catalogo interferenza

L’eliminazione a catalogo di una difformità non ha impatti su DSS

# Caso modifica formula HI

Al cambio di una regola in EAM di una formula per l’Health Index, il sistema prevede in automatico il ricalcolo degli indicatori di condizione secondo un cron schedulato ad intervallo giornaliero (vedi AT HI). A questo punto verranno generati n-eventi che agiscono sul singolo asset. Per eventi la logica è riportata nel paragrafo 4.1.17.

# Caso modifica valore HI

EAM invierà un messaggio sul topic kafka topic-mas-handle-hi-asset-change-event per notificare una modifica del health associato all’asset tramite le configurazione riportate nel paragrafo .

Il messaggio conterrà almeno i seguenti attributi:

{

"_action": "Replace",

"_event": true,

"_id": "1001-ASSET-1470912",

"_translangcode": "EN",

"ahscoreid": 20611,

"assetnum": null,

"attributename": "1001",

"changeby": null,

"changedate": null,

"enterdate": "2024-12-28T12:15:00+01:00",

"expvalinputtype": "AUTOMATED",

"formula": null,

"futurescorebatchid": null,

"lastsuccessdate": "2024-12-28T12:15:00+01:00",

"location": null,

"memo": null,

"methodname": null,

"msgid": null,

"objectname": "ASSET",

"orgid": "ORG01",

"originalvalue": null,

"ownerrecordid": 1470912,

"previousvalue": 26.0,

"siteid": "SIT01",

"success": true,

"todate": "2024-12-28T12:15:00+01:00",

"value": 26.0

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events solo se il campo value è diverso dal valore del campo previousvalue. Il formato del messaggio è il seguente:

{

"event": "MODIFY",

"type": "HI",

"asset": "<request.e "attributes": {

"hiattributename": "<request.attributename>"

}

}

La chiave del messaggio è data dalla concatenazione della stringa “HI-<request.hiattributename>-” più il timestamp in millisecondi.

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type ed il campo asset comprende che si tratta di una modifica alla consistenza di un HI associato ad un asset.

Dal messaggio ricevuto, come prima operazione è necessario rimuovere il singolo asset dalla cache di Redis usando il campo asset. Successivamente bisogna individuare le policy collegate a questa HI la cui tipologia di asset è conforme con quella dell’asset d’input. Le query da eseguire sono le seguenti:

SELECT CLASSSTRUCTUREID AS TIPOLOGIA_ASSET

FROM ASSET a

WHERE ASSETNUM = '<asset d’input>'

SELECT *

FROM X4POLICY A

WHERE STATUS = 'ATTIVA'

AND (TIPOLOGIAASSET = '<>'

OR A.X4POLICYID IN (

SELECT A.X4POLICYID

FROM X4POLICYCLASSIFICATIONS B

WHERE CLASSIFICATIONID = '<>'

AND B.X4POLICYID = A.X4POLICYID))

AND EXISTS (

SELECT 1 FROM X4POLICYINPUT R

WHERE R.TIPOLOGIAINPUT = 'HI'

AND R.X4IDPOLICY = A.X4POLICYID

AND R.IDINPUT = '<hiattributename>')

A questo punto, per ogni policy individuata è necessario inviare al worker un messaggio sulla coda kafka topic-mas-dss-workers. Il formato del messaggio è il seguente:

{

"event": "Se (TIPO_POLICY = 'TRONCO DI ISPEZIONE' oppure TIPO_POLICY = 'SOSTITUZIONE') allora

valorizzare con TIPO_POLICY altrimenti con MODIFY ",

"policy": "<X4POLICYID>",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare

la stessa policy ma per asset differenti. Corrisponde al campo della tabella X4_DSS_WORKED_HISTORY.

DSS_PC_CORRELATION_ID >",

“dssWorkerId”: “id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY

la tupla di richiesta calcolo prevista. Se spenta X4_DSS_WORKED_HISTORY è un UUID”,

"asset": "<assetNum da elaborare è l’asset su cui è necessario effettuare il calcolo della policy>"

}

La chiave del messaggio è la stazione o la linea e in caso di doppia terna è dato dalla concatenazione della linea sulla campata separate dal carattere “#”.

Come riportato nell’algoritmo indicato i messaggi inviati verranno salvati sul DB nella tabella X4_DSS_WORKED_HISTORY usando la “correlationId” definita nel messaggio. La logica che sarà implementata è quella dell’algoritmo per singola policy e singolo asset riportata nel paragrafo 5.1.11 nel caso non si tratti di policy di sostituzione or campata/tratta cavi.

Solo per lo scenario di tronco d’ispezione o sostituzione il manager deve attendere le risposte del worker, valutare l’esito d’elaborazione, effettuare attività di aggregazione dei messaggi ed inviare al worker un insieme di oggetti “Order” per l’inserimento massivo sul DB.

# Caso modifica formula tasso di guasto

Il tasso di guasto viene calcolato ogni anno per il singolo asset. Nel caso venga cambiata la formula del tasso di guasto non sarà possibile aspettare il ricalcolo dell’anno successivo, dovrà essere innescato il calcolo del particolare tasso di guasto per ogni asset attivo della stessa tipologia per la quale il tasso di guasto è calcolato.

Sarà invocata l’api4 riportata nelle paragrafo. Una volta invocata per ogni asset sarà innescato un evento di modifica valore tasso di gusto descritto nel paragrafo 4.1.20

# Caso modifica formula tasso di anomalia

Il tasso di anomalia viene calcolato ogni anno per il singolo asset. Nel caso venga cambiata la formula del tasso di anomalia non sarà possibile aspettare il ricalcolo dell’anno successivo, dovrà essere innescato il calcolo del particolare tasso di guasto per ogni asset attivo della stessa tipologia per la quale il tasso di anomalia è calcolato.

Sarà invocata l’api4 riportata nelle paragrafo. Una volta invocata per ogni asset sarà innescato un evento di modifica valore tasso di gusto descritto nel paragrafo 4.1.21

# Casi di modifica valore tasso di guasto

La logica è speculare a quella degli HI riportata nel paragrafo 4.1.17

# Casi di modifica valore tasso di anomalia

La logica è speculare a quella degli HI riportata nel paragrafo 4.1.17

# Caso modifica cataloghi misure e variabili di stato

# Caso innesco policy periodiche

Le policy periodiche ad oggi note pr

Sulla base di questa frequenza saranno prodotti degli ordini secondo l’algoritmo riportato in questo paragrafo.

Lo use case è innescato da un meccanismo di cron, configurato di Maximo, per cui ad una data prefissata (configurabile)parte l’elaborazione massiva del calcolo delle policy periodiche.

Le policy periodiche sono identificabili tramite il campo X4POLICY.TIPOLOGY valorizzato con PERIODICA. Producono 2 tipi di ordini:

inter-annuale (con frequenza maggiore di un anno)

intra-annuali (con frequenza minore o uguale a un anno)

Il cron sarà configurato come riportato al paragrafo 3.8.1 per innescare il calcolo delle policy.

L’evento di calcolo periodico delle policy sarà sottomesso sul topic topic-mas-handle-periodic passando come chiave del messaggio la chiave la stringa “CALCULATE-PERIODIC” e concatenata con il timestamp di innesco. Nel body del messaggio sarà presente il riferimento all’evento che ha innescato il messaggio. In particolare, il seguente json:

{

“tipology”:”PERIODIC”,

"event": "CALCULATE",

"type": "POLICY",

}

L’evento di calcolo di ogni singola policy periodica viene sottomesso sul topic topic-mas-handle-events passando come chiave del messaggio la chiave della policy da calcolare e concatenata con il timestamp di creazione policy. Nel body del messaggio sarà presente il riferimento all’evento che ha innescato il messaggio. In particolare, il seguente json:

{

“tipology”:” PERIODIC”,

"event": "CALCULATE",

"type": "POLICY",

“policy”: “<idpolicy>”

}

Il messaggio viene consumato dal ms dss-manager. Il dss-manager verificherà la policy

L’algoritmo . Nel caso particolare in cui l’attività associata alla policy sia di tipo “Sostituzione” l’algoritmo non eseguirà la policy poiché per assunzione una policy periodica non prevede la Sostituzione.

L’algoritmo verificherà . l’algoritmo seguirà lo use case riportato nel paragrafo 4.1.42 . In caso contrario l’algoritmo segue le logiche riportate in questo paragrafo.

Se non si tratta di una sostituzione oppure di un tronco di ispezione identifica il livello di applicabilità della policy ad esempio livello conduttore.

Identifica, ad esempio, nel caso di una policy che impatta i conduttori, tutti i conduttori presenti sulla piattaforma circa 500000 ed invia 500000 messaggi su topic topic-mas-dss-workers. Prima di fare questa operazione se impostato il tracing calcolo a DB verrà interrogata la tabella X4_DSS_WORKED_HISTORY per verificare se per lo stesso messaggio sono state inserite delle tuple. Questa fase serve anche per gestire il corner case in cui per qualche motivo il messaggio non è la prima volta che viene consumato da kafka (viene gestito il caso di un riprocessamento a causa di un down del pod dss-manager precedente, che non ha permesso il commit del messaggio). L’algoritmo in questo caso terrà conto di questa casistica ricercando sulla tabella X4_DSS_WORKED_HISTORY il delta asset non inseriti (nel caso standard/nice-path nessun record trovato in tabella) ed inserendo un tupla per ogni delta asset ed inviando un messaggio su topic topic-mas-dss-workers.

Verrà inviato quindi su topic topic-mas-dss-workers come chiave l’id dell’asset su cui agisce la policy (nel caso della campata l’asset-num delle campata), body del messaggio la policy con tutte le regole da soddisfare:

{

"policy": "<idpolicy>",

"event": "ADD",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare la

stessa policy ma per asset differenti. Corrisponde al campo della tabella X4_DSS_WORKED_HISTORY.

DSS_PC_CORRELATION_ID >",

“dssWorkerId”: “id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY la tupla

di richiesta calcolo prevista. Se spenta X4_DSS_WORKED_HISTORY è un UUID”,

"asset": "<assetNum da elaborare è l’asset su cui è necessario effettuare il calcolo della policy>"

}

Saranno previste 20 partizioni del topic topic-mas-dss-workers. Al quale saranno agganciati 5-10 consumatori pods dss-worker [5 fissi e 5 in autoscale].

# Caso registrazione difformità da cavi

Vedi paragrafo 4.1.5

# Caso registrazione interferenza da Elive

Vedi paragrafo 4.1.7

# Caso registrazione difformità da altre fonti

Vedi paragrafo 4.1.5

# Caso registrazione interferenze da altre fonti

Vedi paragrafo 4.1.7

# Caso registrazione difformità da Gui/App

Vedi paragrafo 4.1.5

# Caso registrazione interferenze da Gui/App

Vedi paragrafo 4.1.7

# Caso rilevamento misura

EAM invierà un messaggio sul topic kafka topic-mas-handle-misure-asset-change-event per notificare una modifica della misura associata all’asset tramite le configurazione riportate nel paragrafo .

Il formato del messaggio è il seguente:

{

"x4misureassetid": "183",

"description": "Test API",

"assetnum": "1734733",

"siteid": "SIT01",

"idmisura": "VAR.COND.f.1",

"valore": "58",

"hasld": "0",

"rowstamp": "381592460",

"primarilevazione": "",

"ultimarilevazione": "",

"iddifformita_ext": ""

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events inviando il seguente messaggio:

{

"event": "MODIFY",

"type": "MISURA",

"asset": "<request.assetnum>",

"attributes": {

"x4misureassetid": "<request.x4misureassetid>"

}

}

La chiave del messaggio è data dalla concatenazione della stringa “MISURA-<request. x4misureassetid>-” più il timestamp in millisecondi.

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type ed il campo asset comprende che si tratta di una modifica alla consistenza di una misura associata ad un asset.

Dal messaggio ricevuto, come prima operazione è necessario rimuovere il singolo asset dalla cache di Redis usando il campo asset. Successivamente bisogna individuare le policy collegate a questa misura la cui tipologia di asset è conforme con quella dell’asset d’input. Le query da eseguire sono le seguenti:

SELECT c.CLASSIFICATIONID AS TIPOLOGIA_ASSET

FROM ASSET a, CLASSSTRUCTURE c

WHERE a.ASSETNUM = '<asset d’input>' AND

a.CLASSSTRUCTUREID = c.CLASSSTRUCTUREID

SELECT *

FROM X4POLICY A

WHERE STATUS = 'ATTIVA'

AND (TIPOLOGIAASSET = '<TIPOLOGIA_INPUT>'

OR A.X4POLICYID IN (

SELECT A.X4POLICYID

FROM X4POLICYCLASSIFICATIONS B

WHERE CLASSIFICATIONID = '<TIPOLOGIA_INPUT>'

AND B.X4POLICYID = A.X4POLICYID))

AND EXISTS (

SELECT 1 FROM X4POLICYINPUT R

WHERE R.TIPOLOGIAINPUT = 'VARIABILE'

AND R.X4IDPOLICY = A.X4POLICYID

AND R.IDINPUT = '<x4misureassetid>')

Per ogni policy individuata bisogna calcolare la tipologia di classificazione:

select classificationID AS TIPO_POLICY

from classstructure

where classstructureID in (

select classstructureid

from jobplan

where jpnum = '< IDATTIVITA dalla precedente query >')

A questo punto, per ogni policy individuata è necessario inviare al worker un messaggio sulla coda kafka topic-mas-dss-workers. Il formato del messaggio è il seguente:

{

"event": "Se (TIPO_POLICY = 'TRONCO DI ISPEZIONE' oppure TIPO_POLICY = 'SOSTITUZIONE') allora

valorizzare con TIPO_POLICY altrimenti MODIFY",

"policy": "<X4POLICYID>",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare

la stessa policy ma per asset differenti. Corrisponde al campo della tabella

X4_DSS_WORKED_HISTORY.DSS_PC_CORRELATION_ID >",

“dssWorkerId”: “id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY

la tupla di richiesta calcolo prevista. Se spenta X4_DSS_WORKED_HISTORY è un UUID”,

"asset": <asset d'input>

}

La chiave del messaggio è la linea e in caso di doppia terna è dato dalla concatenazione della linea sulla campata separate dal carattere “#”.

I messaggi inviati verranno salvati sul DB nella tabella X4_DSS_WORKED_HISTORY usando la “correlationId” definita nel messaggio. La logica che sarà implementata è quella dell’algoritmo per singola policy e singolo asset riportata nel paragrafo 5.1.11 nel caso non si tratti di policy di sostituzione or campata/tratta cavi.

Solo per lo scenario di tronco d’ispezione o sostituzione il manager deve attendere le risposte del worker, valutare l’esito d’elaborazione, effettuare attività di aggregazione dei messaggi ed inviare al worker un insieme di oggetti “Order” per l’inserimento massivo sul DB.

# Caso zonizzazione

LIN_ZONA_AMBIENTALE per le linee in cavo

LIN_ZONA_ANNO_SA – attributo di zonizzazione per i controlli SA che può assumere i valori PARI o DISPARI per gli asset linee aeree

LIN_ZONA_ANNO_SB – attributo di zonizzazione per i controlli SB che può assumere i valori PARI o DISPARI per gli asset linee aeree

Nel caso delle linee in cavo è necessario modificare la frequenza di zonizzazione degli OdL già creati dal DSS secondo due criteri:

modificando gli ordini già creati dal DSS sulla base della nuova frequenza;

generare ordinisecondo la nuova frequenza a partire da una data specifica.

Nel primo caso scatta lo use case 4.1.45 ed è necessario considerare il caso speciale della zonizzazione, che è identificabile se l’attributo modificato è LIN_ZONA_AMBIENTALE. In questa casistica valutiamo solo le policy in cui LIN_ZONA_AMBIENTALE appare. Nel manager si valuteranno le stesse query che eseguiamo su 4.1.45. Verranno quindi eseguite le seguenti query:

SELECT c.CLASSIFICATIONID AS TIPOLOGIA_ASSET

FROM CLASSSTRUCTURE c, ASSET a

WHERE a.ASSETNUM = <asset d’input>

AND a.CLASSSTRUCTUREID= c.CLASSSTRUCTUREID

SELECT *

FROM X4POLICY A

WHERE STATUS = 'ATTIVA'

AND (TIPOLOGIAASSET = '<TIPOLOGIA_>'

OR A.X4POLICYID IN (

SELECT A.X4POLICYID

FROM X4POLICYCLASSIFICATIONS B

WHERE CLASSIFICATIONID = '<TIPOLOGIA_>'

AND B.X4POLICYID = A.X4POLICYID))

AND EXISTS (

SELECT 1 FROM X4POLICYINPUT R

WHERE R.TIPOLOGIAINPUT = 'ATTRIBUTO'

AND R.X4IDPOLICY = A.X4POLICYID

AND R.IDINPUT = '<request. assetattrid > '

AND R.TARGETRAFFRONTO = ‘<request. alnvalue >’)

A questo punto, per la policy individuata è necessario inviare al worker un messaggio sulla coda kafka topic-mas-dss-workers.

Il formato del messaggio è il seguente:

{

"event": " ZONIZZAZIONE ",

"policy": "<X4POLICYID>",

"correlationId": "<chiave d’input>",

"asset": <asset d'input>

}

La logica da applicare sul worker è che viene valutata la policy, e se la policy viene verificata(dovrebbe essere verificata solo UNA policy, quella con ha come id input LIN_ZONA_AMBIENTALE e come target di raffronto il valore nuovo assegnato all’attributo su quell’asset) si modifica la frequenza dell’ODL già creato.

Nel caso in cui la policy sia di tipo "tronco", prima di inviare il messaggio al worker è necessario recuperare il tronco dell’asset e successivamente ottenere tutti gli asset associati al tronco, insieme ai rispettivi valori dell’attributo LIN_ZONA_AMBIENTALE, utilizzando le seguenti query:

Query per recuperare il tronco:

SELECT A2.TABLEVALUE

FROM ASSET A, ASSET B, ASSETSPEC A2

WHERE A.ASSETNUM = '<request.assetnum>'

AND A.ASSETNUM = B.PARENT

AND B.ASSETNUM = A2.ASSETNUM

AND A2.ASSETATTRID = 'LIN_TRONCO_ISPEZIONE'

AND A2.TABLEVALUE IS NOT NULL

Query per recuperare gli asset e i relativi attributi associati al tronco:

SELECT DISTINCT a1.ALNVALUE, a1.ASSETNUM, a1.ASSETSPECID, a1.TABLEVALUE

FROM ASSETSPEC a1

WHERE a1.ASSETATTRID = 'LIN_ZONA_AMBIENTALE'

AND a1.ASSETNUM IN (

SELECT DISTINCT a3.ASSETNUM

FROM ASSETSPEC a4

JOIN ASSET a2 ON a4.ASSETNUM = a2.ASSETNUM

JOIN ASSET a3 ON a2.PARENT = a3.ASSETNUM

WHERE a4.TABLEVALUE = '<tronco>'

AND a4.ASSETATTRID = 'LIN_TRONCO_ISPEZIONE'

);

Se tutti i valori dell’attributo LIN_ZONA_AMBIENTALE degli asset associati al tronco sono uguali,per ciascun asset verrà inviato un messaggio al worker e saranno modificati gli ordini corrispondenti. Se, invece, i valori non sono tutti uguali, non verrà effettuata alcuna operazione.

secondo caso

|  |  |
| --- | --- |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |

# Caso aggiunta catalogo nuova policy aggregata

Se dal topic kafka topic-mas-handle-events viene inviato un messaggio così strutturato:

{

"tipology": "<valorizzato come ON_CONDICTION / PERIODIC a seconda della tipologia>",

"event": "<un valore nel dominio ADD/MODIFY>",

"type": "POLICY",

"policy": "<idpolicy>",

}

con le seguenti caratteristiche:

Il campo X4POLICY.POLICYAGGR valorizzato con “1”

Il campo X4POLICY.LIVAGGR valorizzato con uno dei seguenti valori: “1”, “2”, “3”.

La macro – attività dev’essere associata ad una policy di sostituzione. Di seguito la query da eseguire:

select classificationID AS TIPO_POLICY

from classstructure

where classstructureID in (

select classstructureid

from jobplan

where X4MACROATTIVITA = '<POLICY.IDMACROATTIVITA>')

È necessario controllare che il valore TIPO_POLICY = “SOSTITUZIONE”.

Se le tre condizioni sono soddisfatte, vuol dire che siamo in presenza di una policy aggregata. A seconda della tipologia di Asset e del livello di aggregazione di seguito vengono presentati i vari algoritmi:

Conduttori (Campo X4POLICY.TIPOLOGIAASSET = “CONDUTTORI”)

Il manager esegue una query per determinare gli ASSET di tipo “CONDUTTORI”:

SELECT .ASSETNUM

FROM ASSET a, CLASSSTRUCTURE c

WHERE a.CLASSSTRUCTUREID = c.CLASSSTRUCTUREID

AND c.CLASSIFICATIONID = 'CONDUTTORI'

AND

Per ogni conduttore individuato il manager invia sulla coda kafka topic-mas-dss-workers il seguente messaggio:

{

"policy": "<idpolicy aggregata>",

"event": "SOSTITUZIONE",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare

la stessa policy ma per asset differenti. Corrisponde al campo della tabella

X4_DSS_WORKED_HISTORY.DSS_PC_CORRELATION_ID>",

"dssWorkerId": "id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY

la tupla di richiesta per il calcolo previsto.",

"asset": "<assetNum da elaborare, corrisponde ai conduttori>",

"livelloAggregazione": "<Valorizzato con X4POLICY.LIVAGGR. Indica che siamo in presenza

di una policy aggregata>"

}

La chiave del messaggio è la linea e in caso di doppia terna è dato dalla concatenazione della linea sulla campata separate dal carattere “#”.

Il Worker una volta ricevuto il messaggio leggerà dalla cache la policy e l’asset d’inputDavalore della soglia di eleggibilità (S1cond).

Lo scopo del worker è calcolare il coefficiente di eleggibilità del conduttore. A tale scopo, bisogna determinare gli elementi conduttore sottesi all’asset d’input:

SELECT a.ASSETNUM

FROM ASSET a, CLASSSTRUCTURE p

WHERE a.PARENT = '<request.asset>'

AND p.CLASSSTRUCTUREID = a.CLASSSTRUCTUREID

AND p.CLASSIFICATIONID = 'CONDUTTORE'

Per ogni elemento conduttore individuato è necessario calcolare il coefficiente di eleggibilità del conduttore Keleg_cond. Per ogni singolo conduttore è necessario reperire HI di vetustà e l’HI di degrado e calcolare HIvet.+deg. Usando la cache sugli asset è necessario acquisire i valori degli attribuiti HI:

HI degrado conduttore

HI vetustà conduttore

Di seguito l’algoritmo per il calcolo del coefficiente di eleggibilità del conduttore:

Se HIvet.+deg. >= S1cond. oppure esiste un OdL con attività di sostituzione conduttore con stato "Aperto", "Schedulabile", "Schedulato", "Inoltrato", "Avviato", "Eseguito parzialmente" è Keleg_cond = 1

Se HIvet.+deg. < S1cond. è Keleg_cond = HIvet.+deg. / S1cond.

La query per identificare l’esistenza di OdL associato al singolo conduttore è la seguente:

SELECT W.WONUM, W.ASSETNUM

FROM WORKORDER W, JOBPLAN J, CLASSSTRUCTURE C

WHERE W.STATUS IN ('APERTO', 'INOLTRATO', 'SCHEDULATO', 'SCHEDULABILE', 'AVVIATO', 'ESEGUITO PARZ.')

AND W.ASSETNUM = '<request.asset>'

AND W.JPNUM = J.JPNUM

AND J.CLASSSTRUCTUREID = C.CLASSSTRUCTUREID

AND C.CLASSIFICATIONID = 'SOSTITUZIONE'

Definito il coefficiente di eleggibilità del 4° livello, il worker valuterà il coefficiente di eleggibilità del 3° livello “conduttori” come il valore massimo dei coefficienti dei 4° livelli sottostanti:

Keleg_cond = max(Keleg_cond del 4° livello)

Si passa in seguito a valutare il valore del coefficiente di eleggibilità dell’intera tratta di tesatura in cui è compresa ogni singola campata. Per ogni conduttore si esegue la query riporta nel paragrafo . Se la query non fornisce risultato l’algoritmo termina con KO. Se la query ritorna risultato per ogni conduttore si determina la campata associata attraverso il campo ANCESTOR sulla tabella ASSET (indica la campata).

A questo punto il worker risponderà al manager con il seguente messaggio json al manager:

Una volta che tutti i messaggi del worker sono giunti al manager Per ogni tratta di tesatura si determina Keleg_tratta_cond nel seguente modo:

Keleg_tratta_cond = (max(Keleg_cond)*LunghTratta)/(LunghLinea)

Dove

Keleg_cond = keligibilita del messaggio ricevuto dal worker a parità di linea e campata.

LunghTratta = è la lunghezza della tratta definita come somma delle lunghezze delle campate che la compongono. Il calcolo si basa sommando il valore LIN_LUNGHEZZA (ASSETSPEC) per gli elementi che compongono la tratta.

LunghLinea = corrisponde al campo LIN_LUNGHEZZA_L dell’ASSETSPEC per la Linea.

Si procede infine al calcolo del coefficiente di eleggibilità dell’intera linea. Questo sarà dato dalla somma dei coefficienti di eleggibilità delle singole tratte di tutta la linea:

Keleg_linea_cond = Sommatoria dei Keleg_tratta_cond

Una volta determinato il Keleg_linea_cond è possibile procedere alla definizione delle attività di sostituzione confrontando il coefficiente con un valore soglia Seleg_linea_cond.

In base ai valori assunti dal coefficiente di eleggibilità della linea è prevista la seguente logica:

Se Keleg_linea_cond >= Seleg_linea_cond & almeno un Keleg_cond = 1 è generazione di n OdL “Sostituzione conduttore” (3° livello) tanti quanti sono i conduttori di terzo livello sottesi alla linea.

A tale scopo il manager invierà al worker il seguente messaggio:

{

"policy": "<idpolicy aggregata>",

"event": "",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare

la stessa policy ma per asset differenti. Corrisponde al campo della tabella

X4_DSS_WORKED_HISTORY.DSS_PC_CORRELATION_ID>",

"dssWorkerId": "id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY

la tupla di richiesta per il calcolo previsto.",

"asset": "<rappresenta la linea>",

"assetChild": "<lista di CONDUTTORI>",

"livelloAggregazione": "<Valorizzato con X4POLICY.LIVAGGR. Indica che siamo in presenza

di una policy aggregata>"

}

2. Funi di Guardia (Campo X4POLICY.TIPOLOGIAASSET = “FUNI DI GUARDIA”)

Il manager esegue una query per determinare gli ASSET di tipo “FUNI DI GUARDIA”:

SELECT c.ASSETNUM

FROM ASSET a, CLASSSTRUCTURE c

WHERE a.CLASSSTRUCTUREID = c.CLASSSTRUCTUREID

AND c.CLASSIFICATIONID = ' FUNI DI GUARDIA'

AND A.STATUS != 'DEMO'

Per ogni fune di guardia individuato il manager invia sulla coda kafka topic-mas-dss-workers il seguente messaggio:

{

"policy": "<idpolicy aggregata>",

"event": "SOSTITUZIONE",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare

la stessa policy ma per asset differenti. Corrisponde al campo della tabella

X4_DSS_WORKED_HISTORY.DSS_PC_CORRELATION_ID>",

"dssWorkerId": "id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY

la tupla di richiesta per il calcolo previsto.",

"asset": "<assetNum da elaborare, corrisponde ai conduttori>",

"livelloAggregazione": "<Valorizzato con X4POLICY.LIVAGGR. Indica che siamo in presenza

di una policy aggregata>"

}

La chiave del messaggio è la linea e in caso di doppia terna è dato dalla concatenazione della linea sulla campata separate dal carattere “#”.

(S1fdg).

Lo scopo del worker è calcolare il coefficiente di eleggibilità della fune di guardia. A tale scopo, bisogna determinare gli elementi funi di guardia sottesi all’asset d’input:

SELECT a.ASSETNUM

FROM ASSET a, CLASSSTRUCTURE p

WHERE a.PARENT = '<request.asset>'

AND p.CLASSSTRUCTUREID = a.CLASSSTRUCTUREID

AND p.CLASSIFICATIONID = 'FUNE DI GUARDIA'

AND A.STATUS != 'DEMO'

Per ogni fune di guardia individuata è necessario calcolare il relativo coefficiente di eleggibilità Keleg_fdg. Per ogni singola fune di guardia è necessario reperire HI di vetustà e l’HI di degrado e calcolare HIvet.+deg. Usando la cache sugli asset è necessario acquisire i valori degli attribuiti HI:

HI degrado fdg

HI vetustà fdg

Di seguito l’algoritmo per il calcolo del coefficiente di eleggibilità della fune di guardia:

Se HIvet.+deg. >= S1fdg. oppure esiste un OdL con attività di sostituzione conduttore con stato "Aperto", "Schedulabile", "Schedulato", "Inoltrato", "Avviato", "Eseguito parzialmente" è Keleg_fdg = 1

Se HIvet.+deg. < S1fdg. è Keleg_fdg = HIvet.+deg. / S1fdg.

La query per identificare il tipo di attività associata alla singola fune di guardia è la seguente:

SELECT W.WONUM, W.ASSETNUM

FROM WORKORDER W, JOBPLAN J, CLASSSTRUCTURE C

WHERE W.STATUS IN ('APERTO', 'INOLTRATO', 'SCHEDULATO', 'SCHEDULABILE', 'AVVIATO', 'ESEGUITO PARZ.')

AND W.ASSETNUM = '<request.asset>'

AND W.JPNUM = J.JPNUM

AND J.CLASSSTRUCTUREID = C.CLASSSTRUCTUREID

AND C.CLASSIFICATIONID = 'SOSTITUZIONE'

Definito il coefficiente di eleggibilità del 4° livello, il worker valuterà il coefficiente di eleggibilità del 3° livello come il valore massimo dei coefficienti dei 4° livelli sottostanti:

Keleg_fdg = max(Keleg_fdg del 4° livello)

Si passa in seguito a valutare il valore del coefficiente di eleggibilità dell’intera tratta di tesatura in cui è compresa ogni singola campata. Per ogni fune di guardia si esegue la query riporta nel paragrafo 5.1.8. Se la query non fornisce risultato l’algoritmo termina con KO. Se la query ritorna risultato per ogni fune di guardia si determina la campata associata attraverso il campo ANCESTOR sulla tabella ASSET (indica la campata).

A questo punto il worker risponderà al manager con il seguente messaggio json al manager:

Keleg_tratta_fdg nel seguente modo:

Keleg_tratta_fdg = (max(Keleg_fdg)*LunghTratta)/(LunghLinea)

Dove

Keleg_fdg = keligibilita del messaggio ricevuto dal worker a parità di linea e campata.

LunghTratta = è la lunghezza della tratta definita come somma delle lunghezze delle campate che la compongono. Il calcolo si basa sommando il valore LIN_LUNGHEZZA (ASSETSPEC) per gli elementi che compongono la tratta.

LunghLinea = corrisponde al campo LIN_LUNGHEZZA_L dell’ASSETSPEC per la Linea.

Si procede infine al calcolo del coefficiente di eleggibilità dell’intera linea. Questo sarà dato dalla somma dei coefficienti di eleggibilità delle singole tratte di tutta la linea:

Keleg_linea_fdg = Sommatoria dei Keleg_tratta_fdg

Una volta determinato il Keleg_linea_fdg è possibile procedere alla definizione delle attività di sostituzione confrontando il coefficiente con un valore soglia Seleg_linea_.

In base ai valori assunti dal coefficiente di eleggibilità della linea è prevista la seguente logica:

Se Keleg_linea_fdg >= Seleg_linea_fdg & almeno un Keleg_fdg = 1 è generazione di n OdL “Sostituzione fune di guardia” (4° livello) tante quante sono le fune di guardia di sottese alla linea

A tale scopo il manager invierà al worker il seguente messaggio:

{

"policy": "<idpolicy aggregata>",

"event": "",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare

la stessa policy ma per asset differenti. Corrisponde al campo della tabella

X4_DSS_WORKED_HISTORY.DSS_PC_CORRELATION_ID>",

"dssWorkerId": "id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY

la tupla di richiesta per il calcolo previsto.",

"asset": "<rappresenta la linea>",

"assetChild": "<lista di FUNE DI GUARDIA>",

"livelloAggregazione": "<Valorizzato con X4POLICY.LIVAGGR. Indica che siamo in presenza

di una policy aggregata>"

}

Telaio (Campo X4POLICY.TIPOLOGIAASSET = “”)

Il manager esegue una query per determinare gli ASSET di tipo “”:

SELECT c.ASSETNUM

FROM ASSET a, CLASSSTRUCTURE c

WHERE a.CLASSSTRUCTUREID = c.CLASSSTRUCTUREID

AND c.CLASSIFICATIONID = ''

AND A.STATUS != 'DEMO'

Per ogni sostegno individuato il manager invia sulla coda kafka topic-mas-dss-workers il seguente messaggio:

{

"policy": "<idpolicy aggregata>",

"event": "SOSTITUZIONE",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare

la stessa policy ma per asset differenti. Corrisponde al campo della tabella

X4_DSS_WORKED_HISTORY.DSS_PC_CORRELATION_ID>",

"dssWorkerId": "id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY

la tupla di richiesta per il calcolo previsto.",

"asset": "<assetNum da elaborare, corrisponde al>",

"livelloAggregazione": "<Valorizzato con X4POLICY.LIVAGGR. Indica che siamo in presenza

di una policy aggregata>"

}

La chiave del messaggio è la linea e in caso di doppia terna è dato dalla concatenazione della linea sulla campata separate dal carattere “#”.

Il Worker una volta ricevuto il messaggio leggerà dalla cache la policy e l’asset d’input(S1s).

Lo scopo del worker è calcolare il coefficiente di eleggibilità del sostegno Keleg_s. Per ogni singol s è necessario reperire HI di vetustà e l’HI di tasso di guasto e calcolare HIvet.+tdg. Usando la cache sugli asset è necessario acquisire i valori degli attribuiti HI:

TdG

HI vetustà sostegno

A tale scopo, bisogna eseguire la seguente logica:

Se HIvet.+tdg. >= S1s oppure esiste un OdL con attività di sostituzione con stato "Aperto", "Schedulabile", "Schedulato", "Inoltrato", "Avviato", "Eseguito parzialmente" è Keleg_st = 1

Se HIvet.+tdg. < S1s è Keleg_ = HIvet.+tdg. / S1s

La query per identificare il tipo di attività associata al singolo sostegno è la seguente:

SELECT W.WONUM, W.ASSETNUM

FROM WORKORDER W, JOBPLAN J, CLASSSTRUCTURE C

WHERE W.STATUS IN ('APERTO', 'INOLTRATO', 'SCHEDULATO', 'SCHEDULABILE', 'AVVIATO', 'ESEGUITO PARZ.')

AND W.ASSETNUM = '<request.asset>'

AND W.JPNUM = J.JPNUM

AND J.CLASSSTRUCTUREID = C.CLASSSTRUCTUREID

AND C.CLASSIFICATIONID = 'SOSTITUZIONE'

Dopo aver calcolato Keleg_sost il worker risponde al manager con il seguente json:

seguente modo:

Keleg_linea_s = (Somma di tutti associat alla linea)

Una volta determinato il Keleg_linea_s è possibile procedere alla definizione delle attività di sostituzione confrontando il coefficiente Keleg_linea_con un valore soglia Seleg_linea_s.

In base ai valori assunti dal coefficiente di eleggibilità della linea è prevista la seguente logica:

Se Keleg_linea_s >= Seleg_linea_s & almeno unKeleg_ = 1 è generazione di n attività “Sostituzione ” per tutt alla linea.

A tale scopo il manager invierà al worker il seguente messaggio:

{

"policy": "<idpolicy aggregata>",

"event": "",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare

la stessa policy ma per asset differenti. Corrisponde al campo della tabella

X4_DSS_WORKED_HISTORY.DSS_PC_CORRELATION_ID>",

"dssWorkerId": "id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY

la tupla di richiesta per il calcolo previsto.",

"asset": "<rappresenta la linea>",

"assetChild": "<rappresenta la linea>",

"livelloAggregazione": "<Valorizzato con X4POLICY.LIVAGGR. Indica che siamo in presenza di una policy aggregata>"

}

Stazioni (Campo X4POLICY.TIPOLOGIAASSET = “<tipo asset che rappresenta un raggruppamento di equipment figli STAZIONE\SEZIONE\STALLO i possibili valori sono “INTERRUTTORE AIS“, “SEZIONATORE AIS”, “TV AIS”, “TA AIS”, “SCARICATORE AIS” (*) >”)

(*) sarà possibile configurare nuovo ragruppatore

Prima di entrare nelle logiche di calcolo è utile riportare un riferimento al modello gerarchico asset di tipo stazioni.

La figura seguente rappresenta in basso a sinistra lo schema di una stazione elettrica di smistamento e trasformazione; nel corpo della figura è riportata la schematizzazione di una sezione secondo livello gerarchico al di sotto della linea. In particolare, le policy aggregate saranno create per equipment al di sotto della sezione ed equipment al di sotto dello stallo (terzo livello).

Figura 1 - Esempio distribuzione asset di 4° tra gli elementi di 3° livello

In particolare, i tipi asset per i quali sarà definita una policy aggregata al livello sezione ed al livello stallo saranno identificati dall’attributo X4POLICY.LIVAGGR = 3. La policy innescherà il calcolo sulla base del livello per il 3 (livello Stallo).

Il manager esegue una query per determinare gli ASSET di tipo STALLO classstructureid = 1556 padre di asset di tipologia passata in input.

select ASSET.parent, count(*) from MAXIMO.ASSET, MAXIMO.CLASSSTRUCTURE

where ASSET.parent in (

-- gli stalli

select assetnum from MAXIMO.ASSET WHERE CLASSSTRUCTUREID = 1556

)

AND ASSET.CLASSSTRUCTUREID = CLASSSTRUCTURE.CLASSSTRUCTUREID

AND X4ELEMENTO = '< valore campo X4POLICY.TIPOLOGIAASSET es. INTERRUTTORE AIS>'

group by ASSET.parent

Per ogni asset stallo individuato il manager invia sulla coda kafka topic-mas-dss-workers il seguente messaggio sul topic topic-mas-dss-workers

{

"policy": "<idpolicy aggregata>",

"event": "SOSTITUZIONE",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare

la stessa policy ma per asset differenti. Corrisponde al campo della tabella

X4_DSS_WORKED_HISTORY.DSS_PC_CORRELATION_ID>",

"dssWorkerId": "id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY

la tupla di richiesta per il calcolo previsto.",

"asset": "<assetNum da elaborare, corrisponde >",

“classificationId”:”< <X4POLICY.TIPOLOGIAASSET>>”

"livelloAggregazione": "<Valorizzato con X4POLICY.LIVAGGR. Indica che siamo in presenza di una policy aggregata>",

“countMessaggiTotali”: <il numero di item da calcolare>

}

Il valore del campo “countMessaggiTotali”: <il numero di item da calcolare> nel caso in cui il level X4POLICY.LIVAGGR = 3 sarà valorizzato con 1.

La chiave del messaggio è la .

Per ogni tipo asset sarà necessario effettuare il calcolo del . Il ms worker una volta ricevuto il messaggio leggerà dalla cache la policy e l’asset d’input. Leggerà dalla tabella X4POLICYAGGREGATEKP il valore per la tipologia asset ricevuta nel messaggio.

| Tipologia ASSET | Kp |  |  |
| --- | --- | --- | --- |
| INTERRUTTORE AIS | 61 |  |  |
| SEZIONATORE AIS | 16 |  |  |
| TV AIS | 7 |  |  |
| TA AIS | 12 |  |  |
| SCARICATORE AIS | 3 |  |  |
| STALLO | 10 |  |  |

SELECT KP

FROM X4POLICYAGGREGATEKP

WHERE TIPO_ASSET in (<X4POLICY.TIPOLOGIAASSET>, ‘STALLO’)

Se il livello di aggregazione richiesto è il 3 allora i calcoli saranno compartimentati al worker e non sarà prevista la logica su tipo asset SEZIONE. In questo caso saranno estratti tutti gli asset con valore di X4ELEMENTO pari a X4POLICY.TIPOLOGIAASSET figli dell’asset stallo passato in input attraverso la query

SELECT assetnum FROM MAXIMO.ASSET, MAXIMO.CLASSSTRUCTURE

WHERE parent = '<assetnum stallo>'

AND ASSET.CLASSSTRUCTUREID = CLASSSTRUCTURE.CLASSSTRUCTUREID

AND CLASSSTRUCTURE.X4ELEMENTO = < X4POLICY.TIPOLOGIAASSET >

Per ogni asset sulla base della consistenza asset ricavata tramite metodo getAssetDataByAssetNum (5.1.13) verifica le presenza di ordini associati.

Se è associato all’asset un ordine di sostituzione in stato “Aperto”, “Schedulabile”, “Schedulato”, “Inoltrato”, “Avviato”, “Eseguito parzialmente” in questo caso il valore

Se è associato all’asset un ordine di sostituzione in stato “Aperto”, “Schedulabile”, “Schedulato”, “Inoltrato”, “Avviato”, “Eseguito parzialmente” in questo caso il valore . Il valore del uguale a HI del componente. Il valore viene recuperato dalla stazione.

Attraverso anche la cache recupera il riferimento dell’asset padre. Una volta calcolati i vari provvede a calcolare .

Nel caso in questo caso il worker provvederà a creare un ordine di sostituzione legato alla policy ed al tipo asset per ogni asset equipment di 4 livello figli dello stallo e recuperati tramite le query (1). Il valore del si recupera dalla tabella .

# Caso modifica policy aggregata

La modifica della formula della policy aggregata può avvenire solo mediante CR.

# Caso eliminazione policy aggregata

Lo use case non innesca azioni lato DSS. La policy eliminata non sarà più oggetto di calcolo.

L’eliminazione a catalogo di una policy aggregata non ha impatti su DSS.

# Caso inserimento in anagrafica di un nuovo asset per il quale sono configurate policy

EAM invierà un messaggio sul topic kafka topic-mas-handle-asset-change-event per notificare una modifica o l’aggiunta un nuovo asset in consistenza tramite le configurazione riportate nel paragrafo .

Il formato del messaggio è il seguente:

{

"installdate": "2000-07-01T00:00:00+02:00",

"x3bukrs_description": "TR26 Terna Rete Italia S.p.A. Italia",

"x3kokrs": "TR01",

"replacecost": 0,

"x3ingrp": "ULN",

"status": "ATTV",

"x3kokrs_description": "Terna Italia",

"x3bukrs": "TR26",

"assetnum": "1618135",

"changeby": "XT3585",

"statusdate": "2024-10-02T17:41:00+02:00",

"x3dadoppiaterna": false,

"_event": true,

"pluscismte": false,

"x3msgrp": "2150309",

"siteid": "SIT01",

"assetuid": 1935072,

"status_description": "Attiva",

"changedate": "2024-10-03T19:22:43+02:00",

"orgid": "ORG01",

"x3gewrk": "DUMMY_01",

"description": "AURELIA - S. LUCIA 1,2",

"externalrefid": null,

"_action": "Replace",

"x3sapid": "21309B1",

"x3swerk": "2605",

"pluscclass": null,

"classstructureid": "1003",

"purchaseprice": 0,

"x3sapideq": "1020721473",

"isrunning": true,

"x3riferimentofase": null,

"assetid": 1935029,

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events inviando il seguente messaggio:

{

"event": "<ADD or MODIFY in funzione del campo _action>",

"type": "ASSET",

"asset": "<request.assetNum>",

}

La chiave del messaggio è data dalla concatenazione della stringa “ASSET-<request.assetnum>-” più il timestamp in millisecondi.

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type ed asset comprende che si tratta di una modifica ad un asset.

Il manager usando il campo “asset” d’input controllerà la presenza di policy in stato ATTIVA. L’asset dev’essere attivo (status != DIST).

SELECT c.CLASSIFICATIONID AS TIPOLOGIA_ASSET

FROM CLASSSTRUCTURE c, ASSET a

WHERE a.ASSETNUM = <asset d’input>

AND a.CLASSSTRUCTUREID= c.CLASSSTRUCTUREID

SELECT *

FROM X4POLICY A

WHERE STATUS = 'ATTIVA'

AND (TIPOLOGIAASSET = '< TIPOLOGIA_ASSET >'

OR A.X4POLICYID IN (

SELECT A.X4POLICYID

FROM X4POLICYCLASSIFICATIONS B

WHERE CLASSIFICATIONID = '< TIPOLOGIA_ASSET >'

AND B.X4POLICYID = A.X4POLICYID))

Per policy individuat bisogna calcolare la tipologia di classificazione:

select classificationID AS TIPO_POLICY

from classstructure

where classstructureID in (

select classstructureid

from jobplan

where jpnum = '< IDATTIVITA dalla precedente query>')

A questo punto, per ogni policy individuata è necessario inviare al worker un messaggio sulla coda kafka topic-mas-dss-workers. Il formato del messaggio è il seguente:

{

"event": "Se ( 'TRONCO DI ISPEZIONE' oppure

TIPO_POLICY = 'SOSTITUZIONE') allora

valorizzare con TIPO_POLICY

altrimenti con ADD" or " MODIFY" in funzione del campo event d'input",

"policy": "<X4POLICYID>",

"correlationId": "<chiave d’input>",

"asset": <asset d'input>

}

La chiave del messaggio è la linea e in caso di doppia terna è dato dalla concatenazione della linea sulla campata separate dal carattere “#”.

I messaggi inviati verranno salvati sul DB nella tabella X4_DSS_WORKED_HISTORY usando la “correlationId” definita nel messaggio. La logica che sarà implementata è quella dell’algoritmo per singola policy e singolo asset riportata nel paragrafo 5.1.11 nel caso non si tratti di policy di sostituzione or campata/tratta cavi.

Solo per lo scenario di tronco d’ispezione o sostituzione il manager deve attendere le risposte del worker, valutare l’esito d’elaborazione, effettuare attività di aggregazione dei messaggi ed inviare al worker un insieme di oggetti “Order” per l’inserimento massivo sul DB.

# Risoluzione difformità

Nessuna attività per DSS. Il campo X4DIFFORSTAT.ODLRISOLUTORE viene valorizzato da Maximo in fase di esecuzione/cambio stato dell’OdL.

# Risoluzione interferenze

Nessuna attività per DSS. Il campo X4INTERFSTAT.ODLRISOLUTORE viene valorizzato da Maximo in fase di esecuzione/cambio stato dell’OdL.

# Aggiornamento variabili di stato

La logica è speculare a quella della misura nel paragrafo 4.1.30

# Caso modifica catalogo modelli-target/modelli soglia

EAM invierà un messaggio sul topic kafka topic-mas-handle-modelli-target-change-event per notificare una modifica del model associata all’asset tramite le configurazione riportate nel paragrafo .

Il formato del messaggio è il seguente:

{

"x4catmodelloassetid": "2",

"description": "alluminio 13,5 mmq 108,79",

"attributo": "fili",

"value": "19",

"idcluster": "1",

"hasld": "0",

"n1": "aa",

"rowstamp": "398428325",

"x4catmodelloid": "1"

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events inviando il seguente messaggio:

{

"event": "MODIFY",

"type": "MODELLO",

"attributes": {

"x4catmodelloassetid": "request.x4catmodelloassetid"

}

}

La chiave del messaggio è data dalla concatenazione della stringa “MODELLO-<request. x4catmodelloassetid>-” più il timestamp in millisecondi.

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type comprende che si tratta di una modifica ad un Modello.

Dal messaggio ricevuto, come prima operazione è necessario individuare le policy collegate a questo modello. Le query da eseguire sono le seguenti:

SELECT XC2.X4CATMODELLOID AS MODELLO, xc2.IDMODELLO AS MODELLO_TARGET

FROM X4CATMODELLOASSET xc, X4CATMODELLO xc2

WHERE xc.x4catmodelloassetid = <request.x4catmodelloassetid>

AND XC.X4CATMODELLOID = XC2.X4CATMODELLOID

SELECT *

FROM X4POLICY A

WHERE STATUS = 'ATTIVA'

AND EXISTS (

SELECT 1 FROM X4POLICYINPUT R

WHERE R.TIPOLOGIAINPUT = 'DIFFORMITA'

AND R.X4IDPOLICY = A.X4POLICYID

AND R.IDMISURA IS NOT NULL

AND R.TARGETRAFFRONTO = '<MODELLO>'

AND R.TARGETRAFFRONTO_EXT = '<MODELLO_TARGET>')

Per ogni policy determinata, è necessario inviare sul topic topic-mas-handle-events un messaggio così formattato:

{

"tipology": "< valorizzato come ON_CONDICTION / PERIODIC a seconda della tipologia>",

"event": "MODIFY",

"type": "POLICY",

"policy": "<idpolicy>",

}

La chiave del messaggio è data dalla concatenazione dell’idpolicy e del timestamp in millisecondi. La logica per determinare gli asset impattati e per la relativa validazione è riportata nel paragrafo 4.1.8.

Con l’id del modello precedentemente recuperato provvedere ad invalidare la cache dei modelli con i seguenti parametri:

cacheName: CACHE_MODELLO_ASSET

key: idModello

type: MODELLO

# Caso inserimento/modifica policy associata ad attività di sostituzione - odl trainante/trainato

La generazione di un’attività di sostituzione di una campata di conduttore / fune di guardia (attività trainante) determina necessariamente la generazione di attività di sostituzione sull’intera tratta di tesatura (attività trainate).

Per Policy di sostituzione che insistono sui conduttori / funi di guardia, alla generazione:

Si verificherà la presenza di altri conduttori / funi di guardia sulla stessa tratta di tesatura;

Si genererà l’OdL (con attributo «trainante») per il conduttore / fune di guardia che necessita la sostituzione;

Si genereranno gli altri OdL (con attributo «trainato») per tutti i conduttori / funi di guardia presenti sulla stessa tratta di tesatura del conduttore / fune di guardia per il quale erano verificati gli input alla Policy

L’evento di inserimento o policy sarà intercettato in Maximo come riportato nel paragrafo .

Verrà sottomesso sul topic topic-mas-handle-events l’evento di elaborazione di una policy di sostituzione.

La chiave del messaggio sarà data dalla concatenazione della stringa “SOSTITUZIONE _” + timestamp formato unix. Nel body del messaggio sarà presente il riferimento all’evento che ha innescato il messaggio. Il messaggio di request sarà il seguente:

{

"tipology": "ON_CONDICTION",

"event": "SOSTITUZIONE",

"type": "POLICY",

“timestap”: “<data in millesecondi>,

"policy": "<idpolicy>"

}

A partire dal campo X4POLICY.TIPOLOGIAASSET oppure dal campo X4POLICYCLASSIFICATIONS. CLASSIFICATIONID della policy d’input si controlla che la tipologia di Asset è di tipo “CONDUTTORE” oppure “FUNE DI GUARDIA”. Inoltre, è necessario controllare che l’attività associata nel campo X4POLICY.IDATTIVITA è di tipo SOSTITUZIONE eseguendo la seguente query:

select classificationID,description,classstructureid

from classstructure

where classstructureID in (

select classstructureid

from jobplan

where jpnum = '<inputPolicy.IDATTIVITA>')

Il manager a questo punto invia al worker un messaggio json per ogni elemento conduttore o fune di guardia:

Tutti gli elementi inviati saranno salvati nella X4_DSS_WORKED_HISTORY a parità di chiave di correlazione.

Il worker eseguirà il flusso standard e calcolerà la tratta di tesatura (paragrafo 5.1.5).

eseguirà una logica di aggregazione per il campo .

# Caso inserimento policy associata a campata/tratta cavo – gestione ordine tronco ispezione

Lo user case prevede la creazione nel caso la policy sia associata ad una campata oppure una tratta cavo che . In altre parole, questo particolare use case oltre a prevedere la creazione dell’ordine deve prevedere l’associazione di questo ordine ad un ordine aggregato creato sull’asset tronco di ispezione.

Per gestire questo particolare caso è necessario che ogni messaggio inviato sul topic kafka topic-mas-handle-events del dss-manager che riguarda un elemento di tipo policy (campo type = “POLICY”) è necessario controllare che la policy d’input sia di tipologia tronco d’ispezione. A tale scopo è necessario

Se, è necessario eseguire una logica di aggregazione degli ordini secondo quanto riportato nella figura in basso.

Per elemento di tipo campata o tratta cavo (dipende dal campo X4POLICY.TIPOLOGIAASSET), il dss-manager invierà sul topic topic-mas-dss-workers del worker un messaggio nel seguente formato:

La chiave del messaggio è data dalla linea o in caso di campate in doppia terna della concatenazione delle linee divise dal carattere ‘#’.

Una volta tutti i messaggi, il manager si mette in ascolto su kafka-stream in attesa d risposte del worker.

Il worker per ogni messaggio ricevuto, capisce che si tratta di un tronco d’ispezione ed applicherà della policy.

Se la policy è soddisfatta per ogni campata o tratta di cavo, il worker determinerà il tronco d’ispezione e tutte le campate o tratte di cavo correlate il metodo updateTrunckIspection 5.1.8 ed invia il seguente messaggio di risposta al manager su topic topic-mas-handle-worker-feedback:

Una volta che tutti i messaggi del worker sono giunti al manager, il manager . topic-mas-dss-workers. Il formato di questo messaggio è il seguente:

Il campo chiave del messaggio è dato dal valore della linea X3TRONCHI.ASSETNUM.

Alla ricezione di questo nuove messaggio worker

# Caso modifica policy associata a campata/tratta cavo – gestione ordine ispezione

Lo user case prevede la gestione delle logiche associate agli ordini di lavorazione nel caso la policy sia associata ad una campata oppure una tratta cavo. In particolare, la gestione dell’ordine aggregato associato al tronco di ispezione (asset fittizio nell’anagrafica Terna) al quale siano associati ordini afferenti ad asset di tipo campata o tratta cavo.

Nel caso la policy è on-condiction

Solo se la policy ha subito una modifica ad uno o parte dei seguenti campi: differibilità, data al più presto, data al più tardi della policy sarà valutata la policy sarà valutata la policy. Se soddisfatta la policy associata alla campata o tratta cavo (tronco d’ispezione) l’algoritmo dovrà andare a verificare:

se associato alla campata/tratta cavo è già stato creato un ordine funzionalmente in stato precedente a “Schedulato”, in questo caso saranno solo modificate le date relative all’ordine.

Se associato alla campata/tratta cavo è già stato creato un ordine funzionalmente in stato successivo o uguale a “Schedulato” o non è stato creato, in questo caso sarà applicata la stessa logica prevista nel caso di creazione di un nuovo ordine associato al tronco d’ispezione. Ordine aggregato associato al tronco e ordini associate alle varie tratte cavo/campate.

Nel caso la policy è periodica

Se la policy ha subito una modifica ad uno o parte dei seguenti campi: frequenza, data al più presto, data al più tardi viene verificata altrimenti non sono previste azioni. Nel caso la policy risultasse verificata ed è associata alla campata o tratta cavo (tronco d’ispezione) l’algoritmo dovrà seguire 2 differenti logiche a seconda se si tratta di una policy:

Con frequenza interannuale (frequenza > 1)

Con frequenza intraannuale (frequenza <= 1)

Nel caso di frequenza interannuale (frequenza > 1) necessario verificare:

se associato alla campata/tratta cavo è già stato creato un ordine funzionalmente in stato precedente a “Schedulato”, in questo caso saranno solo modificate le date relative all’ordine e calcolati sulla base delle modifiche della policy.

Se associato alla campata/tratta cavo è già stato creato un ordine funzionalmente in stato successivo o uguale a “Schedulato” o non è stato creato, in questo caso sarà applicata la stessa logica prevista nel caso di creazione di un nuovo ordine associato al tronco d’ispezione 4.1.42. Ordine aggregato associato al tronco e ordini associate alle varie tratte cavo/campate.

Nel caso di frequenza intraannuale (frequenza <= 1) necessario verificare:

se associato alla campata/tratta cavo è già stato creato un ordine funzionalmente in stato precedente a “Schedulato”, in questo caso saranno solo modificate le date relative all’ordine e calcolati sulla base delle modifiche della policy.

Se associato alla campata/tratta cavo è già stato creato un ordine funzionalmente in stato successivo o uguale a “Schedulato” o non è stato creato, in questo caso sarà applicata la stessa logica prevista nel caso di creazione di un nuovo ordine associato al tronco d’ispezione 4.1.42. Ordine aggregato associato al tronco e ordini associate alle varie tratte cavo/campate.

# Caso inserimento difformità Ariel

Vedi paragrafo 4.1.5

# Caso inserimento interferenze Ariel

Vedi paragrafo 4.1.7

# Caso modifica anagrafica di un asset esistente

EAM invierà un messaggio sul topic kafka topic-mas-handle-assetspec-change-event per notificare una modifica o l’aggiunta un nuovo asset in consistenza tramite le configurazione riportate nel paragrafo .

Il formato del messaggio è il seguente:

{

"assetnum": "1042",

"assetattrid": "LIN_ZONA_INQUINAMENTO",

"classstructureid": "1004",

"inheritedfromitem": "1",

"itemspecvalchanged": "0",

"displaysequence": "0",

"numvalue": "4",

"measureunitid": "",

"alnvalue": "",

"changedate": "2023-07-12 16:29:00.000",

"changeby": "XT4205",

"es01": "",

"es02": "",

"es03": "",

"es04": "",

"es05": "",

"siteid": "SIT01",

"orgid": "ORG01",

"section": "",

"assetspecid": "403",

"mandatory": "0",

"tablevalue": "",

"linkedtoattribute": "",

"linkedtosection": "",

"startassetfeatureid": "",

"startmeasure": "",

"startoffset": "",

"endassetfeatureid": "",

"endmeasure": "",

"endoffset": "",

"continuous": "0",

"endyoffset": "",

"startyoffset": "",

"endzoffset": "",

"startzoffset": "",

"startyoffsetref": "",

"startzoffsetref": "",

"endyoffsetref": "",

"endzoffsetref": "",

"basemeasureunitid": "",

"startbasemeasure": "",

"endbasemeasure": "",

"startmeasureunitid": "",

"endmeasureunitid": "",

"startoffsetunitid": "",

"endoffsetunitid": "",

"linearassetspecid": "0",

"rowstamp": "3466155",

"datevalue": "",

"x3datamodifica": "",

"anywhererefid": ""

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta topic-mas-handle-events inviando il seguente messaggio:

{

"event": "<ADD or MODIFY in funzione del campo _action>",

"type": "ASSETSPEC",

"asset": "<request.assetnum>",

"attributes": {

"assetattrid": "<request.assetattrid>"

}

}

La chiave del messaggio è data dalla concatenazione della stringa “ASSETSPEC-<request. assetspecid>-” più il timestamp in millisecondi.

listener in ascoltosulla coda topic-mas-handle-events usando il campo type ed assetattrid comprende che si tratta di una modifica ad un assetspec.

A tale scopo verranno eseguite le seguenti query:

SELECT c.CLASSIFICATIONID AS TIPOLOGIA_ASSET

FROM CLASSSTRUCTURE c, ASSET a

WHERE a.ASSETNUM = <asset d’input>

AND a.CLASSSTRUCTUREID= c.CLASSSTRUCTUREID

SELECT *

FROM X4POLICY A

WHERE STATUS = 'ATTIVA'

AND (TIPOLOGIAASSET = '<TIPOLOGIA_INPUT>'

OR A.X4POLICYID IN (

SELECT A.X4POLICYID

FROM X4POLICYCLASSIFICATIONS B

WHERE CLASSIFICATIONID = '<TIPOLOGIA_INPUT>'

AND B.X4POLICYID = A.X4POLICYID))

AND EXISTS (

SELECT 1 FROM X4POLICYINPUT R

WHERE R.TIPOLOGIAINPUT = 'ATTRIBUTO'

AND R.X4IDPOLICY = A.X4POLICYID

AND R.IDINPUT = '<request. assetattrid > ')

Per ogni policy individuata bisogna calcolare la tipologia di classificazione:

select classificationID AS TIPO_POLICY

from classstructure

where classstructureID in (

select classstructureid

from jobplan

where jpnum = '< IDATTIVITA dalla precedente query>')

A questo punto, per ogni policy individuata è necessario inviare al worker un messaggio sulla coda kafka topic-mas-dss-workers. Il formato del messaggio è il seguente:

{

"event": "Se (TIPO_POLICY = 'TRONCO DI ISPEZIONE' oppure TIPO_POLICY = 'SOSTITUZIONE') allora

valorizzare con TIPO_POLICY altrimenti con ADD" or " MODIFY" in funzione del campo event d'input",

"policy": "<X4POLICYID>",

"correlationId": "<chiave d’input>",

"asset": <asset d'input>

}

La chiave del messaggio è la linea e in caso di doppia terna è dato dalla concatenazione della linea sulla campata separate dal carattere “#”.

I messaggi inviati verranno salvati sul DB nella tabella X4_DSS_WORKED_HISTORY usando la “correlationId” definita nel messaggio. La logica che sarà implementata è quella dell’algoritmo per singola policy e singolo asset riportata nel paragrafo 5.1.11 nel caso non si tratti di policy di sostituzione or campata/tratta cavi.

Solo per lo scenario di tronco d’ispezione o sostituzione il manager deve attendere le risposte del worker, valutare l’esito d’elaborazione, effettuare attività di aggregazione dei messaggi ed inviare al worker un insieme di oggetti “Order” per l’inserimento massivo sul DB.

# Caso modifica stato di un asset esistente

Il caso di modifica del cambio stato rientra nel caso . Poiché l’asset potrebbe rientrare tra quelli per i quali lo stato dell’asset modificato diventa adeguato ad essere valutato da una policy oppure diventa inadeguato. In questo caso il manager dovrà intercettare

# Caso modifica idoneità ed eccezioni

La modifica del catalogo delle idoneità comporta l’evict della cache di Redis. Lo scenario prevede l’invio ad EAM di un messaggio sul topic kafka topic-mas-handle-idoneita-change-event per notificare il cambio della difformità. La configurazione è riportata nel paragrafo .

Di seguito il formato del messaggio:

{

"x4tabidoneita": "1170",

"statoidoneita": "ESER",

"tipologiaasset": "GIUNTO FUNE DI GUARDIA",

"rowstamp": "369775118"

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events inviando il seguente messaggio:

{

"event": "MODIFY",

"type": "IDONEITA",

"attributes": {

"x4tabidoneitaid": "<request.x4tabidoneitaid>"

}

}

La chiave del messaggio è data dalla concatenazione della stringa “IDONEITA-<request. x4tabidoneitaid>-” più il timestamp in millisecondi.

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type e l’attributo x4tabidoneitaId comprende che si tratta di una modifica al catalogo delle idoneità.

Con il valore recuperato svuotiamo la cache con il nome CACHE_IDONEITA.

Le idoneità possono impattare sulle policy che insistono sulla stessa classificazione legata all’idoneità, per cui recupereremo il dato dalla tabella idoneità:

SELECT TIPOLOGIAASSET

FROM X4TABIDONEITA xt

WHERE X4TABIDONEITAID = <x4tabidoneitaid>

Con il valore recuperato eseguiamo la seguente query:

SELECT POLICY.X4POLICYID

FROM X4POLICY POLICY

JOIN X4POLICYCLASSIFICATIONS CLA ON POLICY.X4POLICYID = CLA.X4POLICYID

JOIN X4TABIDONEITA IDO ON IDO.TIPOLOGIAASSET = CLA.CLASSIFICATIONID

WHERE IDO.TIPOLOGIAASSET = <TIPOLOGIAASSET>

AND POLICY.STATUS = ‘ATTIVA’

Per ogni policy determinata, è necessario inviare sul topic topic-mas-handle-events un messaggio così formattato:

{

"tipology": "< valorizzato come ON_CONDICTION / PERIODIC a seconda della tipologia>",

"event": "MODIFY",

"type": "POLICY",

"policy": "<idpolicy>",

}

La chiave del messaggio è data dalla concatenazione dell’idpolicy e del timestamp in millisecondi. La logica per determinare gli asset impattati e per la relativa validazione è riportata nel paragrafo 4.1.8.

La modifica del catalogo delle eccezioni comporta l’evict della cache di Redis. Lo scenario prevede l’invio ad EAM di un messaggio sul topic kafka topic-mas-handle-eccezioni-change-event per notificare il cambio della difformità. La configurazione è riportata nel paragrafo .

Di seguito il formato del messaggio:

{

"x4tabeccezioniid": "100",

“description”: “Controllo strumentale IR da terra (OA1)”,

"statoidoneo": "ATTV",

"tipologiaasset": "GIUNTO FUNE DI GUARDIA",

“idattivita_ext”: “AT.L.1.0.0.9.1”,

“idattivita”: “121”,

"rowstamp": "369775118"

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events inviando il seguente messaggio:

{

"event": "MODIFY",

"type": "ECCEZIONI",

"attributes": {

"x4tabeccezioneid": "<request.x4tabeccezioneid>"

}

}

La chiave del messaggio è data dalla concatenazione della stringa “ECCEZIONE-<request. x4tabeccezioneid>-” più il timestamp in millisecondi.

Il listener in ascolto sulla coda topic-mas-handle-events usando il campo type e l’attributo x4tabidoneitaId comprende che si tratta di una modifica al catalogo delle idoneità.

Con il valore recuperato svuotiamo la cache con il nome CACHE_ECCEZIONI

Le eccezioni possono impattare sulle policy che insistono sulla stessa classificazione degli asset, per cui recupereremo il dato dalla tabella eccezioni:

Con il valore recuperato eseguiamo la seguente query:

Per ogni policy determinata, è necessario inviare sul topic topic-mas-handle-events un messaggio così formattato:

{

"tipology": "< valorizzato come ON_CONDICTION / PERIODIC a seconda della tipologia>",

"event": "MODIFY",

"type": "POLICY",

"policy": "<idpolicy>",

}

La chiave del messaggio è data dalla concatenazione dell’idpolicy e del timestamp in millisecondi. La logica per determinare gli asset impattati e per la relativa validazione è riportata nel paragrafo 4.1.8.

# Caso chiusura ordine periodico interannuale

Intercettato l’evento di chiusura ordine associato da un asset da Maximo. Passato al manager il messaggio dobbiamo verificare che l’ordine chiuso abbia una attività che è associata ad una macroattività a sua volta associata alla policy che è periodica ed ha una frequenza superiore ad 1 anno. Se verificata questa condizione sarà creato un ordine secondo la logica contenuta nel paragrafo 5.1.2 in particolare nel caso interannuale

EAM invierà un messaggio sul topic kafka topic-mas-handle-odl-closed-event per notificare il passaggio di un ordine allo stato chiuso con le configurazione riportate nel paragrafo .

Il formato del messaggio è il seguente:

{

"wonum": "1096",

"status": "CHIUSO",

"statusdate": "2024-01-18 14:58:17.000",

"description": "Riparazione del conduttore",

"assetnum": "186135",

"jpnum": "L.1.1.1.3",

"changeby": "XT1012",

"changedate": "2024-01-19 11:50:24.000",

“x4policyid” : 1234,

"_action": "Replace"

}

Il listener in ascolto sul messaggio inoltrerà l’informazione ricevuta sulla coda topic-mas-handle-events inviando il seguente messaggio:

"event": "ADD",

"type": "ODLCLOSED",

"asset": "request.assetNum",

"wonum": "request.wonum",

"attributes": {

"jpnum" : "request.jpnum",

“policy” : request.x4policyid,

“tronconum”: request.x4tronconum (solo in caso di ordine padre di una policy con logica tronco)

}

A questo punto è necessario capire se esiste la policy presente nell’ordine, e se è una policy periodica con frequenza > 1.

SELECT *

FROM X4POLICY A

WHERE A.STATUS = 'ATTIVA'

AND A.X4POLICYID = ‘<WORKORDER.X4POLICYID >' AND A. FREQUENZA > 1 AND A.X4RIFTEMPORALE = ‘A’

A valle dell’esecuzione delle query, se è presente la policy che soddisfa questo requisito, possiamo continuare con l’algoritmo.

Successivamente, è necessario svuotare la cache di Redis invocando il metodo boolean evictCache(CatalogTipology tipologyElement) usando come chiave è “<assetNum>” oppure “<assetNum>_slim” (vedi paragrafo 5.1.9 e paragrafo 3.6)

il dss-manager seguirà la stessa logica di innesco calcolo: invia al ms dss-worker un messaggio sulla coda kafka topic-mas-dss-workers

La chiave del messaggio inviato corrisponde alla linea associata all’asset per il quale è avvenuta la creazione dell’ordine e in caso di doppia terna è dato dalla concatenazione delle linee sulla campata separate dal carattere “#”.

Il body del messaggio è il seguente:

{

"event": " ADD",

"policy": "<X4POLICYID della policy>",

"correlationId": "<campo correlation per correlare nel caso ve ne fosse bisogno tutti i messaggi inviati per calcolare

la stessa policy ma per asset differenti. Corrisponde al campo della tabella X4_DSS_WORKED_HISTORY.

DSS_PC_CORRELATION_ID >",

“dssWorkerId”: “id univoco che permette univocamente di individuare sulla tabella X4_DSS_WORKED_HISTORY

la tupla di richiesta calcolo prevista. Se spenta X4_DSS_WORKED_HISTORY è un UUID”,

"asset": <assetnum d'input è quello per il quel è stato creato l’ordine >

}

I messaggi inviati verranno salvati sul DB nella tabella X4_DSS_WORKED_HISTORY usando la “correlationId” definita nel messaggio. La logica che sarà implementata è quella dell’algoritmo per singola policy e singolo asset riportata nel paragrafo 5.1.11

Nel caso in cui l'ordine chiuso sia un ordine padre creato da una policy periodica con una frequenza superiore a un anno e con logica di tronco, anziché inviare un unico messaggio, verranno inviati tanti messaggi quanti sono gli asset recuperati dalla seguente query:

SELECT a3.ASSETNUM

FROM ASSETSPEC a, ASSET a2, ASSET a3

WHERE TABLEVALUE = :request.x4tronconum

AND a.ASSETATTRID = 'LIN_TRONCO_ISPEZIONE'

AND a.ASSETNUM = a2.ASSETNUM

AND a2.PARENT = a3.ASSETNUM;

anche in questo caso, I messaggi inviati verranno salvati sul DB nella tabella X4_DSS_WORKED_HISTORY usando la “correlationId” definita nel messaggio. La logica che sarà implementata è quella dell’algoritmo 5.1.1

# Algoritmi comuni a più use case

# Algoritmo verifica policy soddisfatta

| On-condiction | Periodic |
| --- | --- |

Sulla base dei dati restituiti dall’api getAssetDataByAssetNum(boolean slimVersion, AssetData asset) e (5.1.13) sulla base delle formule che costituiscono la policy caricate con il metodo loadPolicy(Long policyId) (5.1.12) Per effettuare questa verifica l’algoritmo prevede in ordine:

A partire dalla policy presente sulla tabella X4POLICY viene recuperata la formula scritta nel campo COMBINPUT. Nella formula sono presenti dei parametri identificabili con un valore numerico con un suffisso (@@) ed un prefisso (##). Ogni parametro corrisponde ai distinti valori del campo NINPUT della tabella X4POLICYINPUT associata alla policy in verifica (filtrata quindi sul campo IDPOLICY con l’id policy in verifica). Ogni singolo parametro rappresenterà quindi una condizione da verificare

ogni condizione presente nella tabella X4POLICYINPUT viene verificata singolarmente applicando la formula espressa sui valori del singolo asset su cui si calcola. In particolare, in base al valore della colonna TIPOLOGIAINPUT (che può assumere i valori strettamente indicati nel paragrafo 3.4.2.1) viene recuperato in memoria il valore della variabile presente nella colonna IDINPUT.

Se TIPOLOGIAINPUT è valorizzato con ATTRIBUTO nella colonna IDINPUT sarà riportato il nome della colonna ASSETATTRID della tabella ASSETSPEC questo stesso valore sarà presente in consistenza asset come una chiave della mappa chiave valore relativi agli attributi asset. Infatti, in questa mappa la chiave sarà costituita dalla colonna se presa dalla tabella ASSETSPEC, il valore corrisponderà al valore dell’attributo per quell’asset. Nel caso l’attributo ammette un dominio di valori predefiniti per i quali è ammessa una tabella di gravità/priorità, il mapping dei possibili valori e priorità sarà recuperata dalle tabelle definite nel paragrafo 3.3.7. Saranno riportate in memoria i dati delle tabelle del suddetto paragrafo per garantire l’eventuale trascodifica e comparazione.

Se TIPOLOGIAINPUT è valorizzato con DIFFORMITA

se la colonna IDMISURA non è valorizzata (null) nella colonna IDINPUT sarà riportato l’id difformità (id tabella X4CATDIFFORMITA). Con tale chiave sarà ricercato nella mappa chiave valore relative alle difformità associate all’asset il valore della difformità. Per questo tipo di condizione l’algoritmo prevede anche di recuperare la tabella di mapping dei possibili valori di gravità associati al fine poi di comparare il livello di gravità (x4TARGETDIFFOR). Se associati all’asset non sono presenti difformità il valore della condizione identificata dal valore del campo NInput della singola riga della tabella X4POLICYINPUT corrisponderà a false.

Se la colonna IDMISURA è valorizzata nella colonna nella colonna IDINPUT sarà riportato l’id difformità (id tabella X4CATDIFFORMITA) in questo caso sarà necessario valutare il valore della variabile misura che popola appunto il campo IDMISURA. Il valore corrisponderà al X4CATMISURA. Con questa chiave sarà ricercato nella mappa chiave/valore relativa alle misure associate all’asset il valore della variabile. Se nel campo TARGETRAFFRONTONUM è valorizzato e TARGETRAFFRONTO è null il valore della variabile sarà comparata con un numerico, se invece valorizzato il campo TARGETRAFFRONTO è valorizzato la chiave corrisponderà ad una chiave della tabella X4CATMODELLO. Sulla della tabella X4CATMODELLOASSET e dei valori dell’asset sarà identificato il modello soglia da recuperare e il valore riportato nella colonna N1.

Se TIPOLOGIAINPUT è valorizzato con HI nella colonna IDINPUT sarà riportato l’id Health index (id tabella AHMETHODOLOGY). Con tale chiave sarà ricercato nella mappa chiave/valore relative al HI associate all’asset il valore del HI.

Se TIPOLOGIAINPUT è valorizzato con TASSOGUASTO o TASSOANOMALIA nella colonna IDINPUT sarà riportato l’id tasso di guasto o tasso di difettosità (id tabella AHMETHODOLOGY). Con tale chiave sarà ricercato nella mappa chiave/valore relative al Tasso di gusto/tasso di anomalia associate all’asset il valore del Tasso di guasto e tasso di anomalia.

Se TIPOLOGIAINPUT è valorizzato con INTERFERENZA nella colonna IDINPUT sarà riportato l’id interferenza (id tabella X4CATINTERF). Con tale chiave sarà ricercato nella mappa chiave valore relative alle interferenze associate all’asset il valore corrispondente. Per questo tipo di condizione l’algoritmo prevede anche di recuperare la tabella di mapping dei possibili valori di gravità associati al fine poi di comparare il livello di gravità per farlo saranno presenti in memoria le tabelle di catalogo dominio riportate nel paragrafo 3.3.7 (x4TARGETINTERF). Se associati all’asset non sono presenti interferenze il valore della condizione identificata dal valore del campo NINPUT della singola riga della tabella X4POLICYINPUT corrisponderà a false.

Se TIPOLOGIAINPUT è valorizzato con ATTIVITA nella colonna IDINPUT della tabella X4POLICYINPUT sarà riportato l’id attività (JPNUM tabella JOBPLAN). Con tale chiave sarà ricercato nella mappa chiave valore relative alle attività associate agli ordini associate all’asset. La condizione espressa in questi casi è legata alla presenza o meno di una attività associata all’asset, quindi, non sono previste transcodifiche tramite tabella di mappatura.

Se TIPOLOGIAINPUT è valorizzato con VARIABILE

se la colonna IDMISURA non è valorizzata (null) nella colonna IDINPUT sarà riportato l’id misura (id tabella X4CATMISURE). Con tale chiave sarà ricercato nella mappa chiave valore relative alle misure associate all’asset il valore della misura. Per questo tipo di condizione l’algoritmo prevede anche di recuperare la tabella di mapping dei possibili valori di gravità associati al fine poi di comparare il livello di gravità riportate nel paragrafo 3.3.7. Se associati all’asset non sono presenti misure il valore della condizione identificata dal valore del campo NInput della singola riga della tabella X4POLICYINPUT corrisponderà a false.

Se la colonna IDMISURA è valorizzata, nella colonna IDINPUT sarà riportato l’id della misura (id tabella X4CATMISURE) in questo caso sarà necessario valutare il valore della variabile misura che popola appunto il campo IDMISURA. Il valore corrisponderà al X4CATMISURAID della tabella X4CATMISURE. Con questa chiave sarà ricercato nella mappa chiave/valore (relativa alle misure associate all’asset) il valore della variabile. Se nel campo TARGETNUM è valorizzato e TARGETRAFFRONTO è null il valore della variabile sarà comparata con un numerico, se invece valorizzato il campo TARGETRAFFRONTO è valorizzato la chiave corrisponderà ad una chiave della tabella X4CATMODELLO (campo X4CATMODELLOID). Sulla base della tabella X4CATMODELLOASSET e dei valori dell’asset sarà identificato il modello soglia da recuperare e il valore riportato nella colonna N1.

Nel caso fosse necessario recuperare i valori delle soglie dai cataloghi modelli target è necessario filtrare sulla tabella X4CATMODELLOASSET con queste info. Filtrando sulla tabella per la chiave della X4CATMODELLO si identificano tutte le condizioni che sono configurate per i differenti modelli. Ogni gruppo di condizioni è identificato dallo stesso valore dalla colonna CLUSTERID. L’algoritmo per ogni clusterid verificherà che tutte le condizioni siano verificate. Al primo clusterid per il quale corrispondono tutte le condizioni configurate il modello target. Per questo sarà preso il valore della colonna N1 che rappresenta il valore della soglia.

Se TIPOLOGIAINPUT è valorizzato con COMBINAZIONE non sarà considerate come condizione da verificare.

Se il valore della condizione non è stato impostato (es. a false nel caso di assenza difformità) si procederà a valutare il valore della condizione. Per farlo identificato il valore in consistenza ed avendo la tabella di mapping in memoria, verrà applicata la funzione (valori possibili riportati nel paragrafo 3.4.2.1) il cui valore sarà presente nella colonna OPERATORE. Ogni funzione corrisponderà ad una classe che implementa l’interfaccia PolicyFunction,java. In particolare, invocando il metodo String execute(String variable, String targetRaffronto, Map<String, String> mappaLivelli) sarà valutato il valore della singola formula. Il valore restituito corrisponderà al valore della condizione corrispondente al NInput della singola riga della tabella X4POLICYINPUT.

Calcolate le singole condizioni espresse sulla tabella X4POLICYINPUT per la policyId oggetto di valutazione. L’algoritmo tramite SpringExpressionLanguage (SpEL) sostituirà i valori delle condizioni calcolati al punto 2 e verificherà se la formula:

È soddisfatta restituendo true à

Non è soddisfatta restituendo false à in questo caso non saranno previste azioni

# Algoritmo creazione attività/ordini periodici generate da policy periodiche

| On-condiction | Periodic |
| --- | --- |

Le policy periodiche creano ordini in stato Aperto. Il metodo List<Order> createPeriodicOrders(Asset asset, Policy policy) accetterà in input la policy periodica per la quale è necessario creare ordini attività periodiche.

Il metodo è avulso dalla verifica che la policy si applica o meno su un asset che è di tipo campata o tratta cavo. Poiché nel caso in cui la policy fosse applicata ad un asset per cui è necessario creare un ordine aggregato il DSS è stato disegnato nel caso di tronco di ispezione per fare in modo che il dss-manager raccolga i vari ordini periodici creati da questo metodo sui vari asset e li raccolga per creare un ordine aggregato.

Per questo algoritmo i campi calcolati in fase di generazione ODL sono:

Data modello: logica descritta nel corpo del paragrafo à inserito nella WORKORDER.X3DATAMODELLO

Data pianificata fine: stesso valore della Data modello à inserito nella WORKORDER.TARGCOMPDATE

Data pianificata inizio: Data pianificata fine – durata pianificata; à inserito nella WORKORDER. TARGSTARTDATE

Data di max posticipo: Data modello + Massimo posticipo attività; à inserito nella WORKORDER.FNLCONSTRAINT

Data non prima di: legata al fatto che si tratta di una interannuale / intrannuale, logica di valorizzazione descritta nel corpo del paragrafo à inserito nella .SNECONSTRAINT

L’oggetto Policy conterrà una variabile tipology valorizzata con “PERIODICA”, un attributo frequency che permette di individuare la frequenza, un attributo isInterAnnual. Se quest’ultimo attributo è valorizzato come:

isInterAnnual = true in questo scenario sarà necessario creare ordini interannuali con frequenza maggiore di un anno.

isInterAnnual = false in questo scenario sarà necessario creare ordini infra-annuali con frequenza minore uguale di un anno.

Nel caso di isInterAnnual = true è previsto che il campo Data Modello sia calcolata dalla data di chiusura del precedente OdL, sullo stesso asset, + frequenza della nuova policy. Esempio: un ODL concluso il 14° Gennaio 2026 con una frequenza di 2 anni, avrà la data modello impostata al 14° Gennaio 2028.

Identificata l’attività associata alla policy sarà ricercata sulla tabella WORKORDER per l’asset passato in input per verificare se presente un ordine:

Nel caso non è presente alcun ordine, significa che è la prima volta che si schedula, si provvederà alla creazione del singolo oggetto Order passando i parametri:

Data modello DSS: corrisponde

Data pianificata fine: stesso valore della Data modello;

Data pianificata inizio: Data pianificata fine – durata pianificata (valore preso dall’oggetto Policy)

Data di max posticipo: Data modello + massimo posticipo attività (valore preso dall’oggetto Policy)

Data non prima di: Data modello – Massimo anticipo attività (valore preso dall’oggetto Policy)

Nel caso sia già presente un ordine, significa che non è la prima volta che si schedula, l’algoritmo dovrà prelevare il valore dell’attributo data chiusura ODL ACTFINISH dell’ordine precedente, si provvederà alla modifica del singolo oggetto Order passando i parametri:

Data modello DSS: corrisponde “data di chiusura dell’OdL precedente + frequenza”

Data pianificata fine: stesso valore della Data modello;

Data pianificata inizio: Data pianificata fine – durata pianificata (valore preso dall’oggetto Policy)

Data di max posticipo: Data modello + massimo posticipo attività (valore preso dall’oggetto Policy)

Data non prima di: Data modello – Massimo anticipo attività (valore preso dall’oggetto Policy)

Sarà creato l’oggetto Order popolando le proprietà dell’ordine indicate nel paragrafo Oggetti principali utilizzati dal DSS sotto paragrafo Order.

Nel caso di isInterAnnual = false emessi ordini fino all’anno n+5 dove n corrisponde all’anno. Verranno quindi creati più ordini. Ogni ordine avrà una data modello che coincide con l’ultimo giorno del periodo definito dalla frequenza della policy. Il metodo leggerà il valore del campo frequency della policy ed in base alla valorizzazione creerà un numero di ordini relativi al primo anno pari a 12 / <frequency> per l’anno di budget + lo stesso numero * 5 (gli anni successivi fino al quinto). Ogni ordine ed attività associata sarà caratterizzato da:

Data modello: segue legato alla frequenza

Data pianificata fine: stesso valore della Data modello

Data pianificata inizio: Data pianificata fine – durata pianificata;

Data di max posticipo: Data modello + Massimo posticipo attività;

Data non prima di:

La data modello sarà calcolata nel seguente modo: l’algoritmo parte da mese = 0. Dividendo il numero di mesi dell’anno per la Frequenza otteniamo quanti controlli dovranno essere generati. Cicliamo la generazione mese saltando di N cicli dove N è Frequenza-1*. A questo punto abbiamo il mese di 12 da cui calcolare il giorno. Se il mese è 1, 3, 5, 7, 8,10,12 il giorno sarà 31, se il mese sarà 4,6,9,11 il giorno sarà 30, per il mese 2 dovremmo verificare che anno/4 e anno/400, in caso positivo il giorno sarà 29 altrimenti 28.

| public static List<LocalDate> calcolaDate(int frequenza) { if (12 % frequenza != 0) { //gestire controllo idoneita input return null; } List<LocalDate> dateControllo = new ArrayList<>(); int annoCorrente = Year.now().getValue(); boolean isLeap = Year.isLeap(annoCorrente); LocalDate dataInizio = LocalDate.of(annoCorrente, Month.GENNAIO, 1); while (dataInizio.getYear() == annoCorrente) { LocalDate dataModello = dataInizio.withDayOfMonth(dataInizio.getMonth().length(isLeap)); dateControllo.add(dataModello); dataInizio = dataInizio.plusMonths(frequenza); } return dateControllo; } |
| --- |

Sarà creato l’oggetto Order per ogni periodo popolando le proprietà dell’ordine indicate nel paragrafo Oggetti principali utilizzati dal DSS sotto paragrafo Order.

# Algoritmo creazione attività/ordini generate da policy on-condiction

| On-condiction | Periodic |
| --- | --- |

Le policy on-condiction creano ordini in stato “Da validare”. Il metodo Order createOrder(AssetData asset, Policy inputPolicy) accetterà in input la policy on-condiction per la quale è necessario creare il singolo ordine.

Per questo algoritmo i campi calcolati in fase di generazione ODL sono:

Data modello: legata all’eleggibilità è inserita nella WORKORDER.X3DATAMODELLO

Data pianificata fine: stesso valore della Data modello, è inserita nel campo WORKORDER.TARGCOMPDATE

Data pianificata inizio: Data pianificata fine – durata pianificata; è inserita nel campo WORKORDER. TARGSTARTDATE

Data di max posticipo: Data modello + Massimo posticipo attività; campo WORKORDER.FNLCONSTRAINT

Data non prima di: Data modello – Massimo anticipo attività; campo TARGSTARTDATE.SNECONSTRAINT

Un asset è eleggibile quando è soggetto ad un intervento di sostituzione e gli interventi di sostituzione pianificabili triggerato da una policy di eleggibilità. Le policy di eleggibilità, presenti nel catalogo delle policy, fanno riferimento a delle soglie degli indicatori di condizione tecnica (HI) oltre le quali l’asset è identificato eleggibile e per cui viene prescritta una determinata data di sostituzione.

Per identificare una policy eleggibile è usabile il campo X4POLICY.ELEGGIBILITA valorizzato ad 1 oppure devono essere soddisfatte 2 condizioni:

la policy contiene un HI come parametro à esiste almeno un

X4POLICYINPUT.TIPOLOGIAINPUT = HI

la macro-attività innescata dalla policy è di “sostituzione” à devo soddisfare questa condizione:

select * FROM JOBPLAN A, CLASSSTRUCTURE B

where B.CLASSSTRUCTUREID = A.CLASSSTRUCTUREID

and B.CLASSIFICATIONID = 'SOSTITUZIONE' AND X4MACROATTIVITAI = <id macro attività>

In base alla condizione

Se asset non eleggibile e non presente difformità/interferenza in input alla policy data_modello = differibilità (valore campo X4POLICY.DIFFERIBILITA) + data generazione ODL (sysdate)

Se asset non eleggibile e presente difformità/interferenza in input alla policy allora vengono ricercati nella consistenza asset nella mappa difformità attive o interferenza attiva presenti nella formula della policy e tra queste (e non tra tutte) quella più recente (il valore corrisponde al campo PRIMARILEVAZIONE delle tabelle X4INTERFSTAT e X4DIFFORSTAT) ed una volta trovata sommando la differibilità alla data di rilevamento.

Se eleggibile (legato ad un HI) nella consistenza asset sarà verificato negli ordini (in particolare tra quelli che hanno fino allo stato Schedulato escluso) ordini che hanno la stessa attività associata (sulla base delle constraint dovrebbe essere inserito un solo ordine di questo tipo).

Se non presenti ordini allora sarà impostata la data modello = data ordine + differibilità

Se presenti è necessario comparare il valore della differibilità della policy associato all’ordine già creato in precedenza con la differibilità della policy corrente.

Se minore (differibilità ordine corrente < differibilità ordine precedente) sarà impostata la data modello = data modello minore tra la data modello corrente (calcolata come data corrente + differibilità corrente) e data modello precedente. In altre parole, come la data modello più prossima.

se maggiore (differibilità ordine corrente > differibilità ordine precedente) sarà impostata la data modello = data generazione (data in cui sto calcolando l’ordine) + differibilità nuova (differibilità della policy che stiamo soddisfacendo) – [anno di calcolo ordine corrente – anno di calcolo ordine precedente].

Nota importante su DB EAM tabella X4POLICY

Le differibilità: colonna X4POLICY.DIFFERIBILITA è espressa secondo l’unità di misura riportata in X4POLICY.X4RIFTEMPORALE

Max posticipo e max anticipo campi X4POLICY.MAXANTICIPO e X4POLICY.MAXPOSTICIPO sono sempre espressi in giorni

La durata pianificata campo JOBPLAN.JPduration espresso in ore.

Sarà creato l’oggetto Order popolando le proprietà dell’ordine indicate nel paragrafo Oggetti principali utilizzati dal DSS sotto paragrafo Order.

# Algoritmo ordine prevalente

| On-condiction | Periodic |
| --- | --- |

Il metodo OrderActions checkPrevalentOrder(Order inputOrder) permette di verificare se prima di creare un ordine non esiste un ordine prevalente.

Nel caso in cui su uno stesso asset vengano generate e validate più attività e una di queste sia prevalente rispetto alle altre il DSS dovrà lasciare in stato “Aperto” solo l’attività prevalente e dovrà chiudere in stato “Non eseguito” le altre attività se l’ordine si trova strettamente in stato “Aperto” o “Da validare”.

L’oggetto in input Order contiene l’ordine candidato ad essere creato, per questo ordine verrà eseguito un check. Questo oggetto, quindi, sarà valorizzato con le seguenti attributi:

dataModello

assetnum l’asset su cui l’ordine è creato

jnPlan: l’attività associata all’ordine

L’oggetto restituito contiene le azioni che saranno disposte sulla base dati a seguito di questa verifica.

Gli ordini che saranno estratti assieme all’ordine passato in input dovranno essere verificati per determinare la prevalenza.

La prevalenza deve essere verificata in modo bidirezionale non solo è necessario verificare che l’attività dell’ordine che è in fase di creazione sia prevalsa da un ordine attivo estratto al punto (**), ma anche che le attività legate agli ordini al punto (**) non siano prevalse dall’ordine in ingresso. Inoltre, è necessario considerare anche lo scenario in cui l’attività associata all’ordine in ingresso è già associata ad un ordine attivo.

Per verificarlo è necessario recuperare anche le relazioni inverse di prevalenza

Tramite le query:

(*) Select ATTIVITAPREV, JPNUM from X4ATTPREVALENTI where X4ATTPREVALENTI.JPNUM in (<Id descrittivo dell’attività in apertura. jnPlan passato in >, JpNum Ordini estratti dalla query (**))

Sarà costruito una mappa <String, List<String>> mapPrevalent in cui per ogni Jpan chiave, sarà presente una lista con le attività prevalenti.

à L’ordine in ingresso prevale su ordini attivi, questi ordini saranno inseriti nella List<OrderPrevalent> assieme ad un oggetto OrderPrevalent creato a partire dal Order inputOrder passato in ingresso. L’attributo isPrevalent dell’oggetto OrderPrevalent permetterà di discriminare se un ordine è prevalente rispetto all’altro

à l’ordine in ingresso è prevalso dagli ordini attivi questi ordini saranno inseriti nella List<OrderPrevalent> assieme ad un oggetto OrderPrevalent creato a partire dal Order inputOrder passato in ingresso. L’attributo isPrevalent dell’oggetto OrderPrevalent permetterà di discriminare se un ordine è prevalente rispetto all’altro.

Se invece l’ordine in ingresso è associato ad una attività che è già associata ad un ordine attivo, per determinare la prevalenza guiderà la data modello. In questo caso quindi. Come negli altri casi questi ordini saranno inseriti nella List<OrderPrevalent> assieme ad un oggetto OrderPrevalent creato a partire dal Order inputOrder passato in ingresso. L’attributo isPrevalent dell’oggetto OrderPrevalent permetterà di discriminare se un ordine è prevalente rispetto all’altro.

Una volta popolata la lista di OrderPrevalent è necessario effettuare queste verifiche:

Se la data modello dell’ordine prevalente che ha flag isPrevalent = true <= della data modello dell’ordine prevalso, popolata una istanza di OrderActions in cui viene valorizzato

se l’ordine prevalente è un ordine attivo e l’ordine passato in input è prevalso, Non modifico l’ordine attivo ed inserisco l’ordine in creazione come non eseguito. Quindi

non viene creata istanza di OrderAction dedicata all’ordine prevalente poiché non vi sono azioni.

viene creata una istanza OrderAction dedicata all’ordine prevalso con flag toBeCreated = true (si tratta di un ordine da creare), in cui i campi saranno popolati come:

stato:“Non Eseguito”

X4Note: “OdL prevalso”

X4OrdinePrevalente: “WONUM” dell’ordine attivo prevalente

se l’ordine prevalente è in creazione ed un ordine attivo è prevalso, Modifico l’ordine(gli ordini) attivo(i) ed inserisco l’ordine in creazione con lo stato corrispondete alla tipologia (se on-condition “” se periodica “”. Quindi:

viene creata una istanza di OrderAction dedicata all’ordine prevalente in ingresso che viene trasformato in istanza di OrderAction

viene create n istanze di OrderAction relativa all’ordine(i) prevalso attivo, impostando gli attributi come:

stato: “Non Eseguito”

X4Note: “OdL prevalso”

X4OrdinePrevalente: “TOBECALCULATED” corrisponderà ORDERNUM dell’ordine che sarà inserito per l’ordine in ingresso.

Se la data modello dell’ordine prevalente (che ha flag isPrevalent = true) > della data modello dell’ordine prevalso, viene popolata una istanza di OrderActions in cui viene valorizzato:

se l’ordine prevalente è un ordine attivo e l’ordine passato in input è prevalso, Non modifico l’ordine attivo ed inserisco l’ordine in creazione come non eseguito.

non viene creata istanza di OrderAction dedicata all’ordine prevalente poiché non vi sono azioni.

viene creata una istanza OrderAction dedicata all’ordine prevalso con flag toBeCreated = true (si tratta di un ordine da creare), in cui i campi saranno popolati come:

X4Note: “OdL prevalso”

X4OrdinePrevalente: “WONUM” dell’ordine attivo prevalente

se l’ordine prevalente è in creazione e un ordine (o più ordini) attivi è (sono) prevalso (prevalsi), Modifico l’ordine attivo (solo nei campi note) ed inserisco l’ordine in creazione con lo stato corrispondete alla tipologia (se on-condiction “DaAttivare” se periodica “Attivo”. Quindi:

viene creata una istanza di OrderAction dedicata all’ordine prevalente in ingresso che viene trasformato in istanza di OrderAction

viene create una istanza di OrderAction relativa all’ordine prevalso attivo, impostando gli attributi come:

X4Note: “OdL prevalso”

X4OrdinePrevalente: “TOBECALCULATED” corrisponderà ORDERNUM dell’ordine che sarà inserito per l’ordine in ingresso.

# Algoritmo check attività trainante/trainato

| On-condiction | Periodic |
| --- | --- |

Il metodo OrdersActions checkIsTowingTowed(Asset asset, List<Order> order) permette di verificare se è necessario creare degli ordini trainati.

A tale scopo viene eseguita la seguente query:

SELECT ASSETNUM

FROM ASSET

WHERE PARENT IN (

SELECT ASSETNUM

FROM ASSET

WHERE PARENT IN (

SELECT TARGETASSETNUM

FROM (

SELECT

TARGETASSETNUM,

SOURCEASSETNUM,

ROW_NUMBER() OVER (ORDER BY X3SEQUENCE) AS SEQ_NUM

FROM

ASSETLOCRELATION

WHERE

X3SEQUENCE > (

SELECT MAX(X3SEQUENCE)

FROM ASSETLOCRELATION

WHERE (SOURCEASSETNUM IN (

SELECT

X3ROOTASSETNUM

FROM

ASSET

WHERE

ASSETNUM = :PARENT

AND :CLASSSTRUCTUREID IN (

SELECT

CLASSSTRUCTUREID

FROM

CLASSSTRUCTURE

WHERE

CLASSIFICATIONID = 'CONDUTTORE'))

OR SOURCEASSETNUM IN (

SELECT

SOURCEASSETNUM

FROM

ASSETLOCRELATION

WHERE

TARGETASSETNUM = :ANCESTOR

AND ( :CLASSSTRUCTUREID IN (

SELECT

CLASSSTRUCTUREID

FROM

CLASSSTRUCTURE

WHERE

CLASSIFICATIONID = 'FUNE DI GUARDIA'))))

AND TARGETASSETNUM IN (

SELECT

ANCESTOR

FROM

ASSETANCESTOR

WHERE

HIERARCHYLEVELS = '2'

AND ASSETNUM IN (

SELECT

ASSETNUM

FROM

ASSETSPEC

WHERE

ASSETATTRID = 'LIN_TIPO_ATTACCO'

AND ALNVALUE = 'O' ))

AND X3SEQUENCE < (

SELECT

MAX(X3SEQUENCE)

FROM

ASSETLOCRELATION

WHERE

TARGETASSETNUM = :ANCESTOR

AND ( :CLASSSTRUCTUREID IN (

SELECT CLASSSTRUCTUREID

FROM CLASSSTRUCTURE

WHERE CLASSIFICATIONID = 'CONDUTTORE')

AND SOURCEASSETNUM IN (

SELECT X3ROOTASSETNUM

FROM ASSET

WHERE ASSETNUM = :PARENT )

OR :CLASSSTRUCTUREID IN (

SELECT CLASSSTRUCTUREID

FROM CLASSSTRUCTURE

WHERE CLASSIFICATIONID = 'FUNE DI GUARDIA')) ))

AND X3SEQUENCE < (

SELECT

MIN(X3SEQUENCE)

FROM

ASSETLOCRELATION

WHERE

(SOURCEASSETNUM IN (

SELECT

X3ROOTASSETNUM

FROM

ASSET

WHERE

ASSETNUM = :PARENT

AND :CLASSSTRUCTUREID IN (

SELECT

CLASSSTRUCTUREID

FROM

CLASSSTRUCTURE

WHERE

CLASSIFICATIONID = 'CONDUTTORE'))

OR SOURCEASSETNUM IN (

SELECT

SOURCEASSETNUM

FROM

ASSETLOCRELATION

WHERE

TARGETASSETNUM = :ANCESTOR

AND ( :CLASSSTRUCTUREID IN (

SELECT

CLASSSTRUCTUREID

FROM

CLASSSTRUCTURE

WHERE

CLASSIFICATIONID = 'FUNE DI GUARDIA'))))

AND TARGETASSETNUM IN (

SELECT

ANCESTOR

FROM

ASSETANCESTOR

WHERE

HIERARCHYLEVELS = '2'

AND ASSETNUM IN (

SELECT

ASSETNUM

FROM

ASSETSPEC

WHERE

ASSETATTRID = 'LIN_TIPO_ATTACCO'

AND ALNVALUE = 'O' ) )

AND X3SEQUENCE > (

SELECT

MIN(X3SEQUENCE)

FROM

ASSETLOCRELATION

WHERE

TARGETASSETNUM = :ANCESTOR

AND (( :CLASSSTRUCTUREID IN (

SELECT

CLASSSTRUCTUREID

FROM

CLASSSTRUCTURE

WHERE

CLASSIFICATIONID = 'CONDUTTORE'))

AND SOURCEASSETNUM IN (

SELECT

X3ROOTASSETNUM

FROM

ASSET

WHERE

ASSETNUM = :PARENT )

OR ( :CLASSSTRUCTUREID IN (

SELECT

CLASSSTRUCTUREID

FROM

CLASSSTRUCTURE

WHERE

CLASSIFICATIONID = 'FUNE DI GUARDIA'))) )

AND ( SOURCEASSETNUM IN (

SELECT

X3ROOTASSETNUM

FROM

ASSET

WHERE

ASSETNUM = :PARENT )

AND :CLASSSTRUCTUREID IN (

SELECT CLASSSTRUCTUREID

FROM CLASSSTRUCTURE

WHERE CLASSIFICATIONID = 'CONDUTTORE')

OR :CLASSSTRUCTUREID IN (

SELECT CLASSSTRUCTUREID

FROM CLASSSTRUCTURE

WHERE CLASSIFICATIONID = 'FUNE DI GUARDIA'))) )

WHERE

SOURCEASSETNUM IN (

SELECT X3ROOTASSETNUM

FROM ASSET

WHERE

ASSETNUM = :PARENT

AND :CLASSSTRUCTUREID IN (

SELECT CLASSSTRUCTUREID

FROM CLASSSTRUCTURE

WHERE CLASSIFICATIONID = 'CONDUTTORE'))

OR SOURCEASSETNUM IN (

SELECT SOURCEASSETNUM

FROM ASSETLOCRELATION

WHERE

TARGETASSETNUM = :ANCESTOR

AND ( :CLASSSTRUCTUREID IN (

SELECT CLASSSTRUCTUREID

FROM CLASSSTRUCTURE

WHERE CLASSIFICATIONID = 'FUNE DI GUARDIA')))))

AND (ASSETNUM IN (

SELECT

ASSETNUM

FROM

ASSETSPEC

WHERE

(ASSETATTRID = 'LIN_RIF_FASE' OR ASSETATTRID = 'LIN_RIF_FUNE')

AND (ALNVALUE IN (

SELECT ALNVALUE

FROM ASSETSPEC

WHERE :ASSETNUM = ASSETNUM

AND :SITEID = SITEID

AND ASSETATTRID = 'LIN_RIF_FASE')

OR ALNVALUE IN (

SELECT ALNVALUE

FROM ASSETSPEC

WHERE :ASSETNUM = ASSETNUM

AND :SITEID = SITEID

AND ASSETATTRID = 'LIN_RIF_FUNE'))

AND CLASSSTRUCTUREID =:CLASSSTRUCTUREID)

AND STATUS != 'DIST')

AND CLASSSTRUCTUREID =:CLASSSTRUCTUREID

Per poter eseguire la precedente query è necessario estrarre dall’oggetto asset d’input i seguenti campi:

PARENT

CLASSSTRUCTUREID

ANCESTOR

SITEID

ASSETNUM

La query restituisce gli asset di tipo conduttore o fune di guardia appartenenti alla stessa tratta di tesatura. Gli asset vengono salvati nell’attributo chiamato tesatura dell’oggetto order. Questo campo è un campo contente di tipo lista che contiene tutti gli asset della stessa tesatura.

# Algoritmo salva ordini

| On-condiction | Periodic |
| --- | --- |

Il metodo boolean sendOrders(OrdersActions outputOrdersActions) permette di inserire/aggiornare gli ordini in Maximo invocando l’API 3 (vedi paragrafo OP - Api Maximo usate)

Verranno salvati tutti gli ordini contenuti nell’oggetto OrdersActions, inserendo gli ordini non presenti e modificando quelli già esistenti tramite l’API fornita da MAXIMO.

Gli ordini da elaborare vengono estratti dall'oggetto outputOrdersActions, che contiene una lista di ordini rappresentata dall’oggetto List<Order> orders. Ogni ordine viene mappato in un DTO specifico per garantire coerenza con i campi previsti da Maximo. Il DTO sarà la request che invieremo e sarà formato in questo modo:

public class WorkOrderRequest {

private String href;

private String _action;

private String assetnum;

private String wonum;

private String schedfinish;

private String schedstart;

private String sneconstraint;

private String fnlconstraint;

private String targcompdate;

private String targstartdate;

private String siteid;

private Integer x4aggregati;

private String x4datamodello;

private Long x4policyid;

private String x4policy;

private String x4trainante;

private Integer x3trainante;

private String jpnum;

private String parent;

private String x4microcontrollo;

private String x4tronconum;

}

Gli ordini coinvolgono esclusivamente la tabella ‘workorder’. Sarà quindi possibile utilizzare l'oggetto MXAPIWO, tramite il quale possiamo inserire i record nella tabella MAXIMO utilizzando le API.

Per gli ordini aggregati, al fine di garantire l'inserimento transazionale sia degli ordini padre che degli ordini figli, utilizzeremo la sequence SQL WORKORDERAGGREGATESEQ.

Nel caso in cui venga fornita una lista di ordini aggregati, recupereremo dalla sequence numeri che corrisponde al numero di ordini. Successivamente, a ciascun ordine da creare verrà assegnato un wonum che avrà il formato AGG_numerodellasequence. Una volta identificato l'ordine padre tramite il flag aggregatore dalla lista di oggetti Order, assegneremo come parent degli ordini figli il wonum dell'ordine padre.

Il wonum verrà assegnato solo per gli ordini aggregati, mentre per gli altri ordini il valore del wonum sarà generato automaticamente al momento dell'inserimento nella tabella.

Il campo _action indica il tipo di operazione da effettuare per quell’ordine, può quindi assumere il valore di ‘ADD’ o di ‘UPDATE’, in base a quello che serve.

Il campo href sarà valorizzato solo nel caso di un update, e sarà una stringa con un url necessario all’API che indica il record da modificare della tabella workorder.

Per valorizzare il campo href, bisognerà effettuare una chiamata alla API REST di Maximo, recuperando gli href per gli ordini che hanno il wonum valorizzato.

Avremo una lista di WorkOrderRequestDTO che verrà inviata all’API come body, mentre nell’header verrà passato ‘X-method-override’ con valore ‘BULK’ per permettere l’inserimento di più ordini insieme.

Esempio di body:

[

{

"assetnum": "12345",

"description": "Descrizione dell'ordine di lavoro",

"schedfinish": "2024-12-31",

"schedstart": "2024-12-01",

"sneconstraint": "2024-12-02",

"fnlconstraint": "2024-12-31",

"targcompdate": "2024-12-30",

"targstartdate": "2024-12-01",

"wonum": “12345",

"siteid": "SITE01",

"parent": "11111",

"status": "APERTO",

"statusdate": "2024-12-01",

"x4aggregati": 1,

"x3datamodello": "2024-11-15",

"x4policy": "1234",

"x4trainante": "WO12343",

"reportdate": "2024-11-20",

"jpnum": " AT.L.1.0.0.15.1",

"_action": "ADD"

}

]

Se l’inserimento viene effettuato correttamente, il metodo restituisce true. Altrimenti verrà restituito false.

Nel caso particolare di asset di tipo stazioni sarà necessario includere nel body del messaggio i microcontrolli

# Algoritmo check stato asset

| On-condiction | Periodic |
| --- | --- |

Il metodo boolean checkAssetStatus(Asset asset, Policy policy, Activity activity) permette di verificare se l’ordine ha la condizioni per poter essere controllato.

Per capire se un asset ha uno stato idoneo per l’attività che si intende effettuare bisogna distinguere tra 2 casistiche, in funzione del livello della gerarchia dell’asset.

Per conoscere il valore del livello della gerarchia eseguire la seguente query:

SELECT LEVEL, c.classstructureid FROM CLASSSTRUCTURE c

START WITH classstructureid = <classstructureid>

CONNECT BY PRIOR classstructureid = parent;

Il valore del parametro <classstructureid> si trova nella colonna con lo stesso nome della tabella ASSET.

Assumendo che nel sistema esistono al più 2 gerarchie, una afferenti a ClassificationID=LINEA oppure a ClassificationID=STAZIONE, è conveniente l’utilizzo di una cache che permetta di avere a disposione le coppie classstructureid-LEVEL e caricarla al primo cache miss con la seguente modalità:

Recupero delle ClassStructure per LINEA e STAZIONE

Per ognuna di esse recuperare l’intera gerarchia con la query usando il classstructureid dei record appena recuperati:

SELECT LEVEL, c.classstructureid FROM CLASSSTRUCTURE c START WITH classstructureid = :classstructureid

CONNECT BY PRIOR classstructureid = parent;

Mettere in cache tutti i livelli trovati.

Nel caso in cui la policy punti ad un terzo livello, allora l’algoritmo dovrà verificare la idoneità attraverso un check sulla tabella ASSETSPEC.

Qualora si tratti di una tipologia asset che punta ad un terzo livello di Linee (aeree\cavo) allora il valore da cercare sarà LIN_IDONEITA

SELECT alnvalue FROM assetspec WHERE ASSETATTRID = LIN_IDONEITA AND ASSETNUM=<Assetnum>

Qualora si tratti di una tipologia asset che punta ad un terzo livello di Stazioni, allora il valore da cerare sarà LIN_IDONEITA_SPCC o LIN_IDONEITA_UF

Il corretto dato da ricercare sarà esplicitato come input dall’utente centrale con il target IDONEO nella creazione della policy.

SELECT alnvalue FROM assetspec WHERE ASSETATTRID = LIN_IDONEITA_UF \ LIN_IDONEITA_SPCC AND ASSETNUM=<Assetnum>

Qualora alnvalue fosse valorizzata con IDONEO, il DSS aprirà l’ODL sui terzi livelli. Qualora la policy NON punti ad un terzo livello, allora dovremmo effettuare un check per la presenza di eccezioni, interrogando la tabella X4TABECCEZIONI per Attività e per Tipologia asset dell’anagrafica della policy appena realizzata.

SELECT STATOIDONEO FROM x4tabeccezioni WHERE IDATTIVITA_EXT=<id_attività> AND TIPOLOGIAASSET=< classificationId >

Da questa tabella possiamo intercettare lo stato idoneo SE presente; in caso di nessun risultato l’asset su attività non avrà eccezione e dovremmo controllare allora la idoneità sulla tabella X4TABIDONEITA

SELECT STATOIDONEiTA FROM x4tabidoneita WHERE TIPOLOGIAASSET=<classificationId>

Il risultato dovrà essere confrontato dal DSS su ogni stato dell’asset legato alla tipologia asset. In caso di match positivo, il flusso logico potrà continuare, alternativamente ODL non sarà creato.

# OK - Algoritmo tronco ispezione

| On-condiction | Periodic |
| --- | --- |

Il metodo OrderActions updateTrunkIspection(Asset asset, Policy inputPolicy, List<Order> listOrder) permette di determinare tutti i tronchi relati ad una stessa campata.

A partire dall’asset in input è necessario determinare i relativi tronco d’ispezione. Nel caso della campata è necessario determinare l’asset di tipo CONDUTTORI che ha associato un assetSpec con attributo ‘LIN_TRONCO_ISPEZIONE’. Nel caso di una tratta cavo l’elemento sotteso è di tipo CAVI.

Pertanto, la query per determinare i tronchi di appartenenza a partire dalla campata/tratta cavo di input è la seguente:

SELECT A2.TABLEVALUE AS TRONCO

FROM ASSET A, ASSET B, ASSETSPEC A2

WHERE A.ASSETNUM = '<asset.assetNum>'

AND A.ASSETNUM = B.PARENT

AND B.ASSETNUM = A2.ASSETNUM

AND A2.ASSETATTRID = 'LIN_TRONCO_ISPEZIONE'

AND TABLEVALUE IS NOT NULL

Per ogni TRONCO determinato si acquisisce la linea e il sapId correlati:

SELECT ASSETNUM AS LINEA, SAPID, TRONCONUM AS TRONCO, X3TRONCHIID

FROM X3TRONCHI xt

WHERE TRONCONUM = '< TRONCO >'

Per ogni oggetto Order presente nella listOrder, arricchire le informazioni determinate all’interno dei seguenti attributi:

tronconum = < TRONCO >

x3tronchiid = < X3TRONCHIID >

linea = <LINEA>

sapId = <SAPID>

# OK - Algoritmo evict cache

| On-condiction | Periodic |
| --- | --- |

Il metodo boolean evictCache(CatalogTipology tipologyElement) si occuperà di rimuovere dalla cache di Redis un oggetto presente mediante la chiave passata in input. Il metodo nel caso la chiave passata in input non fosse presente in ogni caso si comporterà in modo idempotente. Il metodo sarà invocato a seguito di adeguamenti ai cataloghi.

Le logiche da eseguire variano in funzione della tipologia d’input. Di seguito verranno presentati i vari casi:

CatalogTipologyModify[type = ‘POLICY’]

In questo scenario non è necessario eseguire nessuna logica particolare. Il campo key identifica in maniera univoca la key di Redis da rimuove. La chiave è la seguente:

“policy”-CatalogTipologyModify.key

CatalogTipologyModify[type = ‘INTERFERENZA’]

Nel caso di una modifica da un’interferenza è necessario identificare le policy collegate. La query da eseguire è la seguente:

SELECT XP.X4POLICYID AS RESULT_ID

FROM X4POLICYINPUT XP, X4POLICY XP2

WHERE IDINPUT = '<CatalogTipologyModify.key>'

AND TIPOLOGIAINPUT = 'INTERFERENZA'

AND XP2.X4POLICYID = XP.X4POLICYID

AND XP2.STATUS = 'ATTIVA'

Per ogni policy identificata è necessario rimuovere dalla cache Redis la policy con chiave “policy”- <RESULT_ID>

CatalogTipologyModify[type = ‘DIFFORMITA’]

Nel caso di una modifica di una difformità è necessario identificare le policy collegate. La query da eseguire è la seguente:

SELECT XP.X4POLICYID AS RESULT_ID

FROM X4POLICYINPUT XP, X4POLICY XP2

WHERE IDINPUT = '<CatalogTipologyModify.key>'

AND TIPOLOGIAINPUT = 'DIFFORMITA'

AND XP2.X4POLICYID = XP. X4POLICYID

AND XP2.STATUS = 'ATTIVA'

Per ogni policy identificata è necessario rimuovere dalla cache Redis la policy con chiave “policy”- <RESULT_ID>

CatalogTipologyModify[type = ‘ATTIVITA’]

Nel caso di una modifica di un’attività è necessario identificare le policy collegate. La query da eseguire è la seguente:

SELECT XP.X4POLICYID AS RESULT_ID

FROM X4POLICYINPUT XP, X4POLICY XP2

WHERE IDINPUT = '<CatalogTipologyModify.key>'

AND TIPOLOGIAINPUT = 'ATTIVITA'

AND XP2.X4POLICYID = XP. X4POLICYID

AND XP2.STATUS = 'ATTIVA'

Per ogni policy identificata è necessario rimuovere dalla cache Redis la policy con chiave “policy”- <RESULT_ID>

# Algoritmo recupera attività da macro-attività

| On-condiction | Periodic |
| --- | --- |

Il metodo Activity getActivityByMacroActivity(Asset asset, Policy inputPolicy) permette di determinare l’attività dato una macro-attività associata alla policy d’input.

L' informazioni relative all’associazione attività e macro-attività è presente nella tabella X4ATTIVITAASSOCIAZIONE. La tabella X4CONDIZIONEASSOCIAZIONE definisce la “prevalenza della scelta metodo”.

La query per determinare le attività a partire dalla macro-attività d’input è la seguente:

SELECT

ass.ATTIVITA,

cond.TARGET,

cond.VALUE,

ass.attivita_ext,

ass.X4ATTIVITAASSOCIAZIONEID,

ass.MACROATTIVITA_EXT,

cls.CLASSIFICATIONID,

jp.CLASSSTRUCTUREID,

jp.DESCRIPTION,

jp.JPDURATION,

jp.X4ATTPREVMETODO

FROM JOBPLAN jp

JOIN CLASSSTRUCTURE cls on jp.CLASSSTRUCTUREID = cls.CLASSSTRUCTUREID

JOIN X4ATTIVITAASSOCIAZIONE ass on jp.jobplanid = ass.attivita

LEFT JOIN X4CONDIZIONEASSOCIAZIONE cond

ON cond.X4ATTIVITAASSOCIAZIONEID = ass.X4ATTIVITAASSOCIAZIONEID

WHERE jp.X4MACROATTIVITA = '<inputPolicy.idmacroattivita>'

AND jp.X4ATTPREVMETODO = '1';

Dalla precedente query si seleziona solo gli elementi con target e value valorizzati e si confrontano con gli attributi (AssetSpec) associati all’asset e i relativi valori. La prima regola che viene soddisfatta verrà restituita al chiamante.

Nel caso in cui non si determina un regola soddisfatta per attributo si cerca l’attività di default: dalla lista precedente si seleziona l’unico record con target e value uguale a null. L’attività individuata verrà restituita al chiamante.

# Algoritmo gestione singola policy per singolo asset

| On-condiction | Periodic |
| --- | --- |

La logica descritta è quella di questi flussi a seconda del caso in cui si tratti in ordine di una policy on- o di una policy periodica.

Policy on

Policy periodiche

Il pod dss-worker letto il messaggio provvede ad estrarre le informazioni associate al singolo asset (dettagliate nel paragrafo 3.4.2.2): anagrafica asset (set di campi configurati nelle policy), difformità rilevate su quest’asset, interferenze rilevate su quest’asset, ordini già aperti su quest’asset, etc. Queste informazioni sono estratte tramite il metodo getAssetDataByAssetNum(boolean slimVersion, String assetNum). Queste informazioni potrebbero essere già in cache nel dss-cache se per lo stesso asset nell’arco della giornata è arrivata una richiesta di calcolo policy e non ci sono stati effetti perturbativi come la creazione di un ordine, la rilevazione di una nuova difformità, interferenza, etc.

Nel caso invece in cui fossero avvenuti questi effetti perturbativi sulla cache a seguito delle modifiche è stato inviato un segnale di cache evict che ha permesso di ripulire la cache relativa al singolo asset e forzare il caricamento. Quindi recuperata la fotografia asset utile alla gestione delle policy viene applicata la policy e verificato se è necessario creare un’attività (piano di lavoro) ed un ordine di lavoro.

Viene applicato l’algoritmo di verifica che la policy sia soddisfatta descritta nel paragrafo 5.1.1.

Nel caso l’algoritmo non identifichi per il singolo asset che le regole applicate alla policy non sono soddisfatte sarà aggiornata la tabella X4_DSS_WORKER_HISTORY con l’esito dell’elaborazione e non saranno eseguite azioni successive. L’elaborazione del worker finisce.

Nel caso l’algoritmo identifichi per il singolo asset che le regole applicate alla policy sono soddisfatte sarà necessario procedere alla creazione dell’ordine o degli ordini a seconda della tipologia di policy.

La policy soddisfatta sarà associata ad una macro-attività. L’algoritmo verificherà prima di procedere alla creazione ordine, a quale attività è associata la macro-attività sulla base delle caratteristiche dell’asset e delle condizioni impostate a catalogo per discriminare tra un’attività ed un’altra. Per farlo sarà invocato l’algoritmo riportato al paragrafo 5.1.10 ed il metodo String getActivityByMacroActivity(Asset asset, Policy inputPolicy)

Prima di generare l’OdL definito dalla Policy, il sistema controllerà in che stato si trova l’asset oggetto di OdL invocando il medodo boolean checkAssetStatus (Asset asset, Policy policy) riportato al paragrafo 5.1.7

Se si tratta di una policy on-condiction sarà seguita la logica di creazione attività/ordini generate da policy on-demand riportate al paragrafo 5.1.3 invocando il metodo Order createOrder (AssetData asset, Policy inputPolicy)

Se si tratta di una policy periodica sarà seguita la logica di creazione attività/ordini periodici generate da policy periodiche riportate al paragrafo 5.1.2 invocando il metodo List<Order> createPeriodicOrder(Asset asset, Policy policy)

Prima di completare la costruzione dell’ordine di lavoro viene verificata la prevalenza dell’ordine invocando il metodo List<OrderAction> checkPrevalentOrder (Order inputOrder) il cui algoritmo è descritto al paragrafo 5.1.4.

Indipendentemente dal risultato dell’algoritmo di prevalenza sarà creata per questa attività/policy in ogni caso almeno una istanza di OrderAction. Poiché l’ordine di lavoro viene creato comunque anche in stato se è aperto per lo stesso asset un ordine prevalente a quello in fase di creazione.

Una volta effettuati tutti i calcoli verrà invocato il metodo che permetterà di salvare la lista OrderAction in Maximo tramite il metodo boolean sendOrders(OrdersActions outputOrdersActions) dettagliato al paragrafo 5.1.6

Sarà previsto l’aggiornamento delle tabella guida X4_DSS_WORKED_HISTORY al fine di avere evidenza dell’esito del calcolo e riuscire poi a verificare quali sono le policy completamente calcolate considerando la concorrenza dei dati

Nel caso di errore durante l’elaborazione del messaggio su topic topic-mas-dss-workers, sarà necessario prevedere un retry del messaggio per 2 volte dopo la prima, attraverso le annotation @Retry di Spring Kafka. Nel caso il messaggio non fosse processato dopo questi tentativi è necessario avvisare l’utente che ha lanciato la richiesta che non è stato possibile elaborare completamente la policy ed in particolare per quale asset. Sarà necessario prevedere l’invocazione di un’api/messaggio che traccia la mancata elaborazione sulla tabella X4_DSS_WORKED_HISTORY.

# Algoritmo recupero configurazione policy

| On-condiction | Periodic |
| --- | --- |

In questo paragrafo viene descritta la logica di estrazione implementata nel metodo loadPolicy(Long policyId). Questo metodo è progettato per recuperare tutte le informazioni necessarie relative a una determinata policy identificata dal parametro policyId. La logica di recupero prevede l’utilizzo di una cache: se la policy richiesta è già stata elaborata precedentemente, l'oggetto viene prelevato direttamente dalla cache, evitando così una nuova interrogazione al database. Se, invece, la policy non è presente in cache, il metodo procede a recuperarla dal database. Inoltre, vengono recuperati anche gli input associati alla policy e, successivamente, i dati vengono trasformati in un oggetto di tipo Policy.

# Algoritmo recupero consistenza asset

| On-condiction | Periodic |
| --- | --- |

In questo paragrafo viene descritta la logica di estrazione implementata nel metodo getAssetDataByAssetNum(String assetNum, boolean isSlim). Questo metodo consente di recuperare tutte le informazioni necessarie relative a un determinato asset passato come parametro. In base al valore del booleano isSlim, è possibile ottenere un set di informazioni più o meno dettagliato sull’asset.

Il metodo, inoltre, gestisce un sistema di cache: se l’asset richiesto non è già stato elaborato, l’oggetto viene recuperato e memorizzato in cache. Al contrario, se l’asset è già presente in cache, l’oggetto viene direttamente prelevato da lì, evitando ulteriori interrogazioni.

Le query utilizzate per estrarre i dati sono descritte nel paragrafo 3.5.2.2. Nello specifico, per ciascun tipo di dato, si può fare riferimento alle seguenti sottosezioni del paragrafo:

Health Index

Tasso di Guasto (TdG) e Tasso di Anomalie (TdA)

Attributi

Asset

Attività

Difformità

Interferenze

Variabili

Tali query vengono eseguite in parallelo, migliorando l’efficienza del processo. La struttura dell’oggetto AssetData è descritta nel paragrafo 3.5.2.2.1.

# Algoritmo micro-controlli

| On-condiction | Periodic |
| --- | --- |

In questo paragrafo viene descritta la logica di implementata per gestire nel metodo List<Task> manageMicroControls (String assetnum, String jpnum). Il metodo ho lo scopo di determinare, sulla base di attributi degli asset e relativi valori indicati sul catalogo micro-controlli, quali degli n micro-controlli associati al PdL (JobPlanId) vanno riportati sull’Ordine relativo allo specifico asset sul quale il DSS sta agendo. In questo modo all’interno dell’OdL saranno presenti solo i micro-controlli specifici dell’asset che possiede una determinata caratteristica tecnica.

Se il jobPlanId è associato ad un asset di tipo stazione il DSS dovrà verificare che i task associati al jobplan siano espletabili. Per farlo sarà necessario fare riferimento al catalogo micro-controlli. Per ogni task sarà verificato se l’asset associato soddisfa le condizioni definite per il singolo task. Solo se soddisfatte le condizioni sarà effettuato l’inserimento nella tabella JOBTASK.

L’algoritmo verificherà nel caso di asset di tipo STAZIONE attraverso la query:

SELECT * FROM classancestor

WHERE classstructureid IN (

SELECT classstructureid

FROM asset

WHERE assetnum = '<assetnum identificativo asset>')

AND hierarchylevels = (

SELECT MAX(hierarchylevels)

FROM classancestor

WHERE classstructureid IN (

SELECT classstructureid

FROM asset

WHERE assetnum = '<assetnum identificativo asset>')

);

Se stiamo creando un ordine per un asset di tipo STAZIONE prima dell’inserimento dell’ordine dovremo andare a definire quelli che sono i task (alias i microcontrolli) da associare quindi è necessario effettuare le seguenti verifiche.

I microcontrolli per i quali sono espressi delle condizioni da verificare. Per questi solo se la condizione è verificata viene creato il microcontrollo. Questi microcontrolli e le condizioni correlati li ritroviamo attraverso la query:

I microcontrolli per i quali non sono espressi delle condizioni da verificare. Per questi i microcontrolli devono essere creati anche se non sono presenti condizioni:

Le query verranno eseguite in parallelo, si fa riferimento alla tabella X4CARDMICRO nella quale sono riportate le condizioni di applicabilità. In questa tabella sono riportate per ogni microcontrollo delle condizioni per le quali è verificato. In particolare saranno inserite le condizioni di applicabilità espresse tramite la coppia di colonne ATTRIBUTE e VALUE in cui sono indicate per ogni attributo dell’asset il valore che deve avere per soddisfare la singola condizione. Se soddisfatte tutte le condizioni espresse per il singolo microcontrollo, sarà ritenuto soddisfatto il microcontrollo e per questo motivo associato a PdL il singolo microcontrollo.

Una volta recuperati i dati da entrambe le query, se esistono degli asset di quarto livello con associati dei task con condizione, verranno associati per ogni assetnum univoco tutti gli N task senza condizione recuperati dalla seconda query.

Come nell’esempio riportato nella figura di questo paragrafo non esiste una dipendenza per cui tutti i microcontrolli devono essere soddisfatti per essere associato al PdL. Per cui solo i microcontrolli che soddisfano il PdL saranno associati al PdL e quindi all’ordine.

Una volta valorizzato la lista dei Task sarà restituita dal metodo. Il chiamate di questo metodo recuperati i task restituiti saranno associati all’ordine in fase di creazione nel metodo SendOrders.

# Algoritmo checkModifyPolicy

| On-condiction | Periodic |
| --- | --- |

L'algoritmo ModifyPolicyResultType checkModifyPolicy(AssetData assetData, Policy policy, Activity activity) ha lo scopo di determinare il comportamento del sistema in caso di modifica di una policy associata a un asset.

L’oggetto restituito dal metodo sarà un ENUM cosi formato:

public enum ModifyPolicyResultType {

NOT_FOUND,

MODIFIABLE,

NOT_MODIFIABLE

} Questo processo consente di valutare lo stato degli ordini esistenti e di stabilire se è necessario modificarli, crearne di nuovi o terminare l'elaborazione senza ulteriori azioni.

Il metodo esegue una ricerca all'interno della lista degli ordini (assetData.getAttivita()) per verificare se esiste almeno un ordine con lo stesso jpnum dell'attività in esame e con lo stesso x4policyid della policy. Se non viene trovato nessun ordine che corrisponde a questi criteri, il metodo restituisce ModifyPolicyResultType.NOT_FOUND, indicando che l’ordine non è modificabile e che si può proseguire con lo stesso algoritmo descritto nel paragrafo 5.1.11.

Se esiste almeno un ordine corrispondente alla policy e all’attività, il metodo controlla se questa si trova nello stato "APERTO" o "DA VALIDARE". Se la condizione è soddisfatta, il metodo restituisce ModifyPolicyResultType.MODIFIABLE, e verrà chiamato il metodo copyOrModifyOrder (5.1.16) e successivamente l’algoritmo salva ordine (5.1.6).

Se l’ordine è presente ma il suo stato non è "APERTO" o "DA VALIDARE" In questo caso, il metodo restituisce ModifyPolicyResultType.NOT_MODIFIABLE per segnalare che sarà completata l’elaborazione e scritto sulla tabella X4_DSS_WORKED_HISTORY l’esito dell’elaborazione senza creazione o modifica ordini.

# Algoritmo copyOrModifyOrder

| On-condiction | Periodic |
| --- | --- |

Il metodo boolean copyOrModifyOrder(Asset asset, Policy policy, Attributes flags) permette di applicare le modifiche all’ordine sulla base del tipo modifica alla policy

In input sarà passato un oggetto Attributes che conterrà le info relative all modifiche della policy come:

frequency

nuovo valore frequenza

vecchio valore frequenza

differibility

nuovo valore differibilità

vecchio valore differibilità

maxAdvance

nuovo valore massimo anticipo

vecchio valore massimo anticipo

maxSnooze

nuovo valore massimo posticipo

vecchio valore massimo posticipo

Si passerà a valutare gli attributi, se l’attributo frequency è presente, è necessario discriminare sulle azioni da dover eseguire sulla base del fatto che la frequenza:

Aumenta: (newValue frequenza < oldValue frequenza) significa che sugli OdL esistenti saràmodificata la data modello degli ordini esistenti e creati nuovi ordini sulla base dell’ordine.

Diminuisce (newValue frequenza > oldValue frequanza) significa che non saranno previste creazioni di nuovi ordini ma solo la modifica delle date modello di quelli esistenti.Per farlo contati il numero di ordini attivi associati a quell’asset e quella policy viene calcolata il nuovo range temporale moltiplicando il numero di ordini per la frequenza. In base alla sequenza originale saranno aggiornati gli ordini partendo dal primo ordine attivo. Costruita questa lista viene restituita dal metodo.

Se l’attributo deferrability è presente, è necessario modificare la data modello secondo le logiche riportate nel paragrafo 5.1.3. Adeguando anche data “di massimo posticipo e data no prima di calcolate sulla base della data modello.

Se l’attributo maxAdvance è presente: determina la modifica del campo “non prima di”.

Se la policy è periodica infra-annuale (frequenza < 1 anno): non viene eseguita modifica poiché questo caso non rientra tra quelle modificabili dall’utente.

Se la policy è periodica intra-annuale (frequenza > 1 anno) o on-condiction: la data “non prima di” è calcolata come data_modello – nuovo valore passato nel campo massimo anticipo attività.

Se l’attributo maxSnooze è presente: una modifica di questo campo sulla policy causa la modifica della data “di massimo posticipo”. Verrà quindi sommata alla data modello dell’ordine il massimo posticipo attività.

Le logiche indicate sono sovrapponibili. Nel caso in cui pervenisse al DSS sia la modifica della frequenza e di uno o entrambi i valori del massimo anticipo e posticipo si partirà sempre prima dal ricalcolo del cambio frequenza e differibilità e poi sul calcolo della data al più presto ed al più tardi

Sarà creato quindi per ogni ordine da creare/aggiornare una istanza dell’oggetto OrderAction. Una volta definita questa lista sarà restituita dal metodo.

Esempio di modifica data modello / creazione nuovi ordini nel caso di cambio frequenza da quadrimestrali a trimestrali.

# Centro di controllo

Per visualizzare a GUI l’esito delle elaborazioni sarà realizzato un centro di controllo Maximo, tecnologia nativa IBM Maximo. Il centro di controllo rappresenta la console da cui l’utente ha la possibilità oltre di gestore le policy di verificare l’esito delle elaborazioni del DSS. Gli ordini creati dal DSS saranno visibili dalle GUI Maximo dettagliate in analisi tecnica Sprint 4.

# APPENDICE A – IN PROGRESS – Marco - Aggiunta dei micro-controlli agli ordini migrati

Gli ordini importati in Maximo potrebbero non avere l’elenco di micro-controlli associati, per cui verrà definita la tabella X4_MICROCONTROLLI_MIG_BATCH in cui verranno inseriti gli identificativi degli ordini interessati.

X4_MICROCONTROLLI_MIG_BATCH:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ID | NUMBER | Y | Identificativo auto incrementale della tabella |
| WONUM | VARCHAR2(25 CHAR) | Y | Id dell’ordine di lavoro |
| ASSETNUM | VARCHAR2(25) | Y | Id dell’asset |
| JPNUM | VARCHAR2(20) | Y | Id dell’attività |
| STATUS | VARCHAR2(20 CHAR) | Y | Lo stato può assumere il valore tra [READY, RUNNING, DONE, ERROR] |
| CREATED_BY | VARCHAR2(20 CHAR) | N |  |
| CREATED_TIMESTAMP | DATE | Y | Data creazione |
| CHANGED_BY | VARCHAR2(20 CHAR) | N | Utente modifica |
| CHANGED_TIMESTAMP | DATE | N | Data modifica |
| MESSAGE_CODE | VARCHAR2(20 CHAR) | N | Codice restituito dall’elaborazione |
| MESSAGE_TEXT | VARCHAR2(4000 CHAR) | N | Messaggio restituito dall’elaborazione |

La tabella sarà popolata dall’attività di migrazione, in questo contesto assumiamo che sia popolata con gli ordini di lavoro per i quali si richiede l’associazione con i micro-controlli.

API MAXIMO

L’api da usare per aggiungere i micro-controlli ad un ordine di lavoro è la seguente:

URL: https://mascol.manage.col.mas.servizi.prv/maximo/api/os/X4WOACTIVITY?lean=1

METHOD: POST

REQUEST OBJECT:

[ {

"parent": <String\| wonum dell’ordine a cui aggiungere il miccontrollo>,

"assetnum": "<String \| assetnum>",

"description": "<String>",

"siteid": "SIT01",

"classstructureid": <String>,

"x4microcontrollo": <String>,

"woclass": "ACTIVITY",

"istask": "1"

}]

Step1: predisporre code kafka su DSS-MANAGER

- startBatchMicrocontrolli per avviare il processo sul manager;

- handleMicrocontrollo per effettuare il lavoro assegnazione dei microcontrolli all’ordine di lavoro

Mandando un messaggio al topic 'topic-mas-dss-batch-microcontrolli-dev' si avvierà il processo batch.

Il consumatore del messaggio leggerà dalla tabella X4_MICROCONTROLLI_MIG_BATCH gli ordini da processare, prenderà in considerazione i record con STATUS = ‘READY’

Per ogni ordine verrà inviato un messaggio al topic 'topic-mas-handle-worker-microcontrolli-dev' che effettuerà il lavoro vero e proprio. All’invio del messaggio impostare STATUS = ‘OPEN’.

Il messaggio per il worker conterrà avrà la seguente struttura:

{

"wonum": "<id ordine lavoro >",

"assetnum": "<id dell’asset >",

"jpnum": "<id dell’attività>",

"recordId": "<chiave del record della tabella X4_MICROCONTROLLI_MIG_BATCH >",

}

Il consumatore, configurato in DSS-WORKER, leggerà l'id dell'ordine di lavoro dal messaggio ricevuto e con esso potrà usare le informazioni contenute per recuperare i microcontrolli associabili invocando il metodo al 5.1.14 Microcontrolli

Se la lista restituita è non vuota, usare i dati recuperati per invocare l’api Maximo WOACTIVITY.

Se la chiamata andrà a buon fine, aggiornare lo stato del record di migrazione a ‘CLOSED’.

kafka topic

Sono previsti i seguenti topic dedicati al motore di calcolo opportunamente partizionati, nella slide identificati con (B):

topic-mas-dss-batch-microcontrolli: topic su cui vengono indirizzati i messaggi per avviare l’associazione dei micro-controlli;

topic-mas-handle-worker-microcontrolli: topic su cui vengono indirizzate le richieste di associazione dei micro-controlli.

# APPENDICE B – Dimensionamento Kafka

In allegato il dimensionamento Kafka richiesto

# APPENDICE C – Configurazione Publish Channel Kafka

In allegato manuale di configurazione di un Publish Channel di Kafka:
