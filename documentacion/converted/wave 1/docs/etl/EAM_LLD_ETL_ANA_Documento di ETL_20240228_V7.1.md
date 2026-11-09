# EAM_LLD_ETL_ANA_Documento di ETL_20240228_V7.1

|  |  |
| --- | --- |

ETL Linee Low Level Design

TCDD_Wave_1

Low Level Design Document (LLDD):

# Storia del documento

| Data | Versione | Descrizione | Autori |
| --- | --- | --- | --- |
| Data | # Versione | Dettagli del cambiamento | Nome autori |
| 27/07/2023 | 1.0 | Documento iniziale | Francisco Javier Ulla Pedrera, Pablo Cano Llorente |
| 03/08/2023 | 2.0 | Visto e commentato da ICT | Emily Coniglio |
| 08/09/2023 | 2.1 | Revisione dopo i commenti di ICT | Francisco Javier Ulla Pedrera, Pablo Cano Llorente |
| 25/09/2023 | 2.2 | Visto e commentato da ICT | Emily Coniglio |
| 16/10/2023 | 2.3 | Revisione dopo i commenti di ICT | Francisco Javier Ulla Pedrera, Pablo Cano Llorente |
| 19/10/2023 | 2.4 | Commenti di ICT | Emily Coniglio |
| 24/11/2023 | 2.5 | Revisione dopo la call con ICT | Francisco Javier Ulla Pedrera, Pablo Cano Llorente |
| 12/12/2023 | 2.6 | Commenti di ICT | Emily Coniglio |
| 20/12/2023 | 2.7 | Revisione dopo i commenti di ICT | Pablo Cano Llorente |
| 12/01/2024 | 2.9 | Commenti di ICT | E. Coniglio, A. Carcione |
| 31/01/2024 | 3.0 | Revisione RTI dopo i commenti di ICT | Pablo Cano Llorente |
| 01/03/2024 | 4.0 | Revisione documento | Alin Catalin David |
| 05/04/2024 | 5.3 | Revisione dopo i commenti di ICT | Francisco Javier Ulla Pedrera |
| 18/04/2024 | 6.0 | Revisione RTI dopo commenti ICT | Francisco Javier Ulla Pedrera |
| 24/04/2024 | 6.0 | Commenti di ICT | Emily Coniglio / Antonino Carcione |
| 29/04/2024 | 7.0 | Revisione dopo i commenti di ICT | Luca Martino / Marco Martini |
| 06/05/2024 | 7.1 | Ultima revisione | Luca Martino / Marco Martini |

# Approvazione del documento

Solo approvatori – Firma richiesta

| Data | Versione | Approvatori | Ruolo |
| --- | --- | --- | --- |
| Data di approvazione | # Versione | Approvatori del documento | Ruolo nella matrice di approvazione |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Solo revisori – Firma richiesta

| Data | Versione | Revisori | Ruolo |
| --- | --- | --- | --- |
| Data di revisione | # Versione | Revisori del documento | Ruolo nella matrice di revisione |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Solo destinatari – Firma non richiesta

| Data | Versione | Destinatari | Ruolo |
| --- | --- | --- | --- |
| Data di invio | # Versione | Destinatari del documento | Ruolo nel progetto o nell’organizzazione |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

# Index

1 Storia del documento 2

2 Approvazione del documento 3

3 Index 4

4 Glossario 6

5 Assunzioni 7

6 Informazioni generali 7

7 Dettaglio architettura applicativa 8

7.1 Set di step iniziali: 9

7.2 Step cleaning 11

7.3 Step di migrazione 11

8 Tabelle di Frontiera On Premise e tabelle di staging 12

8.1 Tabelle di Staging EAM 12

8.1.1 Nota 34

8.2 Tabelle on premise 35

9 Conversione della struttura oggetto 40

9.1 Oggetti e Relazioni gerarchiche 40

10 DEscRizione Step Workflow 40

11 Tabelle per il connettore 53

12 Documenti correlati 53

Indice figure 54

# Glossario

| Abbreviazione | Descrizione |
| --- | --- |
| ALN | Caratteri alfanumerici: maiuscole, minuscole e numeri. |
| CSV | E’ un file di testo delimitato che utilizza una virgola per separare i valori. Ogni riga del file è un record di dati. Ogni record è costituito da uno o più campi, separati da virgole. |
| Data-load | Modelli o strumenti di Excel utilizzati per importare i dati in Maximo. |
| Dati grezzi (raw data) | Dati SAP non elaborati. |
| EAM | Enterprise asset management (EAM) è un sistema di gestione del ciclo di vita degli asset. |
| HLD | “High Level Design”, Progettazione di alto livello. |
| LLD | “Low Level Design”, Progettazione di basso livello. |
| Record | Piccola unità di archiviazione destinata a contenere un certo tipo di dati. |
| SAP | SAP stands for System Applications and Products in Data Processing.Sistema di Enterprise Resource Planning utilizzato in Terna. |
| SITE | Un sito è una suddivisione di un'organizzazione che può tenere traccia dell'inventario e di altri dati separatamente dagli altri siti. Alcunei tipologiei di informazioni sono univochei per un sito e non sono visibili ad altri siti all'interno dell'organizzazione. |
| Struttura oggetto | Una struttura oggetto è il livello di dati comune utilizzato dai componenti del framework di integrazione per inviare e ricevere dati in Maximo® Asset Management. Una struttura oggetto consiste in uno o più oggetti di business correlati che definiscono il contenuto di un messaggio di integrazione. |
| Oggetto | Un oggetto Maximo è un'entità utilizzata per archiviare i dati. Questi dati sono memorizzati in una tabella in cui ogni colonna è un attributo del record da memorizzare. |
| UPPER | Caratteri maiuscoli. |
| YORN | Campo Maximo che può essere popolato con 1 o 0. |

# Assunzioni

Il documento di LLD segue le stesse assunzioni riportate nel documento di HLD ID 3 “EAM_HLD_ETL_ANA_Documento di ETL_20231025_V2.6” e potrebbe subire modifiche qualora una delle assunzioni riportate non fosse rispettata.

È possibile consultare la documentazione citata in questo LLD nella seguente folder condivisa: Documenti correlati.

# Informazioni generali

L’obiettivo di questo documento è descrivere la configurazione del tool ETL, sviluppato per il caricamento massivo degli Asset da SAP verso Maximo. L’import in EAM verrà eseguito per livelli in accordo con le regole di Mapping concordate e condivise con il Business (ID 1 Documenti Correlati: (Mapping Anagrafica EAM AM_Dati Linee rev.7 250723.LLD ).

L’architettura applicativa del flusso ETL linee prevede l’esecuzione dei seguenti flussi:

Flusso 1a: estrae le linee e gli attributi da SAP S/4 e le copia sulle tabelle di staging di SAP S/4.

Flusso 1b: estrae le linee e gli attributi dalle tabelle di staging SAP S/4 e le copia sulle tabelle DB Oracle On Premise tramite client Orale JDBC. Il LLD di questo flusso è contenuto nel documento “Terna - SAP DB Connection to Oracle”.

Flusso 2: estrae le linee e gli attributi dalle tabelle del DB Oracle on Premise e le inserisce in Maximo.

In particolare, il Flusso 2 esegue le seguenti azioni (vedi figura1):

Copia dal DB Oracle on premise al DB oracle staging EAM

Trasforma i dati dal DB oracle staging EAM ed inserisce linee ed attributi su EAM tramite l’invocazione di api native Maximo

Figure 1 - Processo di caricamento degli asset SAP su EAM

Il documento, nelle sezioni seguenti, descrive in dettaglio il flusso 2. In particolare:

nel capitolo 7 è descritta l’architettura applicativa

nel capitolo 8 sono elencate e descritte le tabelle:

del DB On Premise (paragrafo 8.2).

del DB Oracle staging EAM (paragrafo 8.1).

nel capitolo 9 sono elencate e descritte le tabelle Maximo su cui ETL andrà a scrivere.

nel capitolo 10 sono descritte le tabelle del workflow ed i vari step.

nel capitolo 11 sono riportate i riferimenti alle tabelle popolate per il connettore.

# Dettaglio architettura applicativa

Componenti che costituiscono l’architettura applicativa del flusso 2:

script di automazione nativi IBM Maximo: script custom scritti in linguaggio Jhthon che sono deployati su pod mas-ui (puntoC). Contengono la logica di business del flusso 2. Invocano api native IBM per la creazione delle linee e caratteristiche su EAM Maximo.

pod EAM-ETL: il pod è deployato su project Openshift dedicato. Il pod contiene un’applicazione springbootbased che ha il compito di lanciare il motore di workflow.

Pod mas-ui: il pod è deployato su project mas-*-manage Openshift. Il pod nativo Maximo ha l’obiettivo di eseguire gli script di automazione. Il pod espone in modalità rest la logica degli script di automazione.

Tabelle DB Staging EAM: contiene le tabelle di staging che hanno la doppia funzione: performance e di tenere traccia dell’esito della migrazione oltre che delle tabelle dedicate al workflow.

Tabelle workflow. Tabelle utilizzate dal pod eam-etl (punto B).

DB On premise: contiene le tabelle di frontiera alimentate da SAP tramite il client oracle.

DB EAM il DB Maximo. Il DB su cui è stato installato Maximo.

Le tabelle al punto D, E, G risiedono sulla stessa istanza Oracle che corrisponde al DB EAM. Le tabelle al punto F risiedono su DB On premise dedicato e visibile sia da Cloud SAP che da Cloud EAM.

Il pod ETL-EAM (B) è un motore di workflow che esegue step configurati sulle tabelle di workflow (punto E). Ogni step rappresenta una singola unità di lavoro che ha la funzione o di preparare dati sulle tabelle di staging (D) oppure di invocare la creazione di linee o caratteristiche tramite l’invocazione di script di automazione (A) esposti tramite api rest dal pod mas-ui (punto C).

Il workflow prevede 3 macro-gruppi di step.

Step iniziali che copiano le tabelle on premise su tabelle di staging

Step dedicati a identificare gli asset spazzatura, errati, senza riferimento gerarchico, fittizi, appartenenti all’elenco di esclusione, doppioni, aggiornare lo stato della sede ricevuta da SAP secondo il mapping (es. Stato PROG à INAT).

Step dedicati alla migrazione (sono gli step restanti).

Ogni lancio di un workflow è identificato da un identificativo chiamato LOADED che permetterà di discriminare tra lanci successivi.

## Set di step iniziali

Gli step iniziali hanno l’obiettivo di importare tramite copia i dati dal DB On Premise alle tabelle del DB di staging. In particolare, dalle tabelle X3SEDITECHSAP_IF, X3CARATSAP_IF, X3SEDIINDISP_IF e X3GEWRK_IF alle tabelle X3SEDITECHSAP, X3CARATSAP, X3SEDIINDISP e X3GEWRK.

Questi step prevedono di lanciare le seguenti procedure PLSQL:

PROCEDURE CREATE_CHUNKS_INTO_X3GEWRK_IF

PROCEDURE COPY_DATA_FROM_X3GEWRK_IF

PROCEDURE CREATE_CHUNKS_INTO_X3CARATSAP_IF

PROCEDURE COPY_DATA_FROM_X3CARATSAP_IF

PROCEDURE CREATE_CHUNKS_INTO_X3SEDITECHSAP_IF

PROCEDURE COPY_DATA_FROM_X3SEDITECHSAP_IF

PROCEDURE CREATE_CHUNKS_INTO_X3SEDIINDISP_IF

PROCEDURE COPY_DATA_FROM_X3SEDIINDISP_IF

Ogni tabella on premise verrà interessata da una coppia di stored procedure:

CREATE_CHUNKS_INTO_<TABLE_NAME> ha la funzione di suddividere i record individuati dallo stesso EXTRACTION_ID che devono essere processati (CHUNK_ID = NULL) in più sottogruppi assegnando un valore diverso alla colonna CHUNK_ID in base al valore di CHUNK_SIZE configurato (default = 500.000).

La procedura del tipo COPY_DATA_FROM_<NOME_TABELLA>_IF si occupa di fare la copia del dato dalla tabella di origine <NOME_TABELLA_IF> alla rispettiva tabella di destinazione <NOME_TABELLA>, ad esempio la procedura COPY_DATA_FROM_X3SEDITECHSAP_IF leggerà i dati dalla tabella X3SEDITECHSAP_IF e li scriverà nella tabella X3SEDITECHSAP.

Le due procedure sono invocate dal workflow di copia tramite uno step dedicato. Questo step lancerà prima la creazione dei chunk e successivamente lancerà tante volte la procedura di copia quanti sono i chunk da creare.

Ad esempio, lo step 1 – Copiare i dati da X3CaratSAP effettuerà le seguenti operazioni:

Invoca la procedura CREATE_CHUNKS_ON_X3CARATSAP ottenendo come valore di ritorno il numero chunk creati

Invoca la procedura di copia contemporaneamente per il valore di PARALLEL_LEVEL configurato nello step fino a che tutti i chunk saranno processati.

Questo accorgimento di suddivisione in più lavori da eseguire parallelamente si è rivelato necessario per ridurre i tempi di copia; infatti, le tabelle _IF e quelle di staging risiedono su due database differenti e la copia da una tabella all’altra avviene tramite collegamento DBLink. Il collegamento risente della velocità della rete LAN/INTERNET e non può usare i meccanismi ottimizzati dai database per questo tipo di operazione. Ogni invocazione in parallelo riesce a scalare abbastanza linearmente fino alla saturazione della capacità della rete o del numero massimo di connessioni consentite.

Per maggiori dettagli consultare il capitolo 10.

## Step cleaning

Gli step di pulizia sono dedicati a identificare gli asset spazzatura, errati, senza riferimento gerarchico, fittizi, appartenenti all’elenco di esclusione, doppioni e ad aggiornare lo stato della sede ricevurta da SAP secondo il mapping (es. Stato PROG à INAT).

Per maggiori dettagli consultare il capitolo 10.

## Step di migrazione

Gli step di migrazione prevedono una fase di preparazione delle tabelle di staging mediante la suddivisione in gruppi di asset e successivamente l’invocazione di script di automazione.

Tipicamente è previsto uno step per ogni tipologia di asset da migrare. Fatta eccezione per quelle tipologie di asset dove è previsto uno step di popolamento preventivo di una tabella di staging dedicata cui segue l’esecuzione dello step di migrazione vera e propria.

In particolare, lo step di migrazione prevede di:

Conteggiare il numero di asset da migrare

Dividere il gruppo di asset identificati in gruppi tramite un identificativo chiamato chunk_id

Invocazione in parallelo tramite numero di thread (configurabile) di script maximo esposti tramite api rest. Ogni invocazione allo script maximo prevederà almeno il riferimento al chunk_id da elaborare.

Ogni script automatico al suo interno provvederà per ogni singolo asset contenuto nel chunk_id ad effettuare l’invocazione di api IBM Maximo che si occuperanno della creazione dell’asset su Maximo.

I log del run sono sul pod ETL-EAM; è previsto inoltre su ogni record delle tabelle di staging il tracciamento dell’esito tramite campo tecnico.

Per maggiori dettagli consultare il capitolo 10.

# Tabelle di Frontiera On Premise e tabelle di staging

Il presente capitolo descrive le tabelle di staging e le tabelle on premise

## Tabelle di Staging EAM

Nella tabella sottostante sono riportate tutte le tabelle di staging EAM necessarie all’esecuzione del workflow. Nelle tabelle successive di questo paragrafo sono riportate le DDL con la descrizione di ogni singolo campo.

Sulle tabelle di Staging creati sul DB di EAM ci sono dei vincoli minimali sulle colonne non legate al workflow, mentre ci sono vincoli di obbligatorietà sulle colonne legate al workflow (processo di migrazione), eventuali dati che non rispecchiano quanto condiviso sull’interfaccia saranno scartati in fase di migrazione. Le tabelle di interfaccia sul DB on premise presento dei vicoli fisici ed il controllo è intrinseco nel client Oracle utilizzato per l’estrazione lato SAP.

Le tabelle di staging, di seguito riportate vengono popolate durante il workflow. L’esigenza di queste tabelle nasce con il duplice obiettivo: rispondere a esigenze di performance e tenere traccia degli asset creati negli step dedicati agli asset per tipologia.

| Tabella | Utilità |
| --- | --- |
| X3SEDITECHSAP | Memorizza tutte le informazioni ricevute sulla sede tecnica SAP. Da questa tabella vengono creati i seguenti asset, senza bisogno di più tabelle intermedie: LINEA CAMPATA SOSTEGNO TRATTA IN CAVO CONDUTTORI FUNI DI GUARDIA CAVI CAMERA GIUNTI TERMINALI |
| X3CARATSAP | Memorizza tutte le informazioni ricevute sulle caratteristiche SAP. |
| X3ESTREMOCAVO | Tabella dove vengono immagazzinati gli estremi cavo che devono essere caricati in EAM: ESTREMO CAVO |
| X3GENLVL3NOSAP | Tabella dove vengono immagazzinati gli asset di livello 3 che non esistono in SAP: ATTACCHI CONDUTTORE TELAIO |
| X3GENLVL4SAPISOLATORE | Tabella dove vengono immagazzinati gli isolatori conduttore che devono essere caricati in EAM: ISOLATORE CONDUTTORE |
| X3GENLVL4NOSAP | Tabella dove vengono immagazzinati gli asset di livello 4 che non esistono in SAP e la cui regola di creazione non dipende da alcuna caratteristica: CONDUTTORE CAVO DI POTENZA GIUNTO CAVO TERMINALE ARMAMENTO CONDUTTORE STRUTTURA |
| X3GENLVL4FROMATT | Tabella dove vengono immagazzinati gli asset di livello 4 che non esistono in SAP e la cui regola di creazione dipende da una caratteristica: FIBRA OTTICA CONDUTTORE FIBRA OTTICA FUNE DI GUARDIA DESTRA FIBRA OTTICA FUNE DI GUARDIA SINISTRA FIBRA OTTICA FUNE DI GUARDIA TERZA FUNE DI GUARDIA DESTRA FUNE DI GUARDIA SINISTRA FUNE DI GUARDIA TERZA NIDO AVIFAUNA SEGNALAZIONE LUMINOSA OSTACOLO VOLO ANTIROTAZIONALI CONDUTTORE ANTIVIBRANTI CONDUTTORE DISSUASORI AVIFAUNA CONDUTTORE DISTANZIATORI DISTANZIATORI INTERFASE SFERE DI SEGNALAZION ANTIROTAZIONALI FUNE DI GUARDIA DISSUASORI AVIFAUNA FUNE DI GUARDIA SFERE DI SEGNALAZIONE FUNE DI GUARDIA GIUNTO CONDUTTORE BARRETTA PREFORMATA CONDUTTORE GIUNTO FUNE DI GUARDIA BARRETTA PREFORMATA FUNE DI GUARDIA DESTRA BARRETTA PREFORMATA FUNE DI GUARDIA SINISTRA BARRETTA PREFORMATA FUNE DI GUARDIA TERZA ARMAMENTO FUNE DI GUARDIA DESTRA ARMAMENTO FUNE DI GUARDIA SINISTRA ARMAMENTO FUNE DI GUARDIA TERZA MONITORAGGIO IOT (DIGIC) CAVI GIUNTO DI RIPARAZIONE CAMERA ISPEZIONABILE CASSETTA DI SEZIONAMENTO GIUNTI MONITORAGGIO IOT (DIGIC) GIUNTI MONITORAGGIO IOT (DIGIC) TERMINALI ISOLATORE FUNE DI GUARDIA ANTIVIBRANTI FUNE DI GUARDIA DESTRA ANTIVIBRANTI FUNE DI GUARDIA SINISTRA ANTIVIBRANTI FUNE DI GUARDIA TERZA DISPOSITIVO TLC GIUNZIONE FIBRA OTTICA SEZIONATORE |
| X3TESTX3ETL | Tabella dove sono memorizzati gli attributi che devono essere valorizzati in EAM e la cui sede tecnica di provenienza corrisponde alla sede tecnica che genera l’asset in EAM. |
| X3HEREDADOS | Tabella dove sono memorizzati gli attributi che devono essere valorizzati in EAM e la cui sede tecnica di provenienza non corrisponde alla sede tecnica che genera l’asset in EAM. |

Tabella X3SEDITECHSAP:

| Attribute | Description | Chiave | Type | Length | Reqd?(1) | Same as object | Same as attribute | Search type |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| BUKRS | Società |  | ALN | 50 | OPZ |  |  | Wildcard |
| BUTXT | Descrizione della società |  | ALN | 50 | OPZ |  |  | Wildcard |
| CITY | Città |  | ALN | 50 | OPZ |  |  | Wildcard |
| COUNTRY | Paese |  | ALN | 50 | OPZ |  |  | Wildcard |
| EQFNR | Campo sort |  | ALN | 50 | OPZ |  |  | Wildcard |
| FLTYP | Categoria della sede tecnica |  | ALN | 50 | OPZ |  |  | Wildcard |
| GEWRK | Centro di lavoro responsabile |  | ALN | 50 | OPZ |  |  | Wildcard |
| INBDT | In funzione dal |  | DATE | 4 | OPZ |  |  | Exact |
| INGRP | Gruppo responsabile della pianificazione della manutenzione |  | ALN | 50 | OPZ |  |  | Wildcard |
| INNAM | Descrizione del gruppo responsabile della pianificazione della manutenzione |  | ALN | 50 | OPZ |  |  | Wildcard |
| IWERK | Divisione di pianificazione della manutenzione |  | ALN | 50 | OPZ |  |  | Wildcard |
| IWERK.NAME (NAME1) | Descrizione della divisione di pianificazione della manutenzione |  | ALN | 50 | OPZ |  |  | Wildcard |
| KOKRS | Area di controllo |  | ALN | 50 | OPZ |  |  | Wildcard |
| KOSTL | Centro di costo |  | ALN | 50 | OPZ |  |  | Wildcard |
| KTEXT | Descrizione dell’area di controllo |  | ALN | 50 | OPZ |  |  | Wildcard |
| LIVELLO SEDE TECNICA | Livello della sede tecnica |  | ALN | 50 | OPZ |  |  | Wildcard |
| MSGRP | Impianto |  | ALN | 50 | OPZ |  |  | Wildcard |
| NAME 1 | Nome 1 |  | ALN | 50 | OPZ |  |  | Wildcard |
| NAME 2 | Nome 2 |  | ALN | 50 | OPZ |  |  | Wildcard |
| OBJNR | ID SAP univoco | X | ALN | 50 | OPZ |  |  | Wildcard |
| PLTXT | Definizione della sede tecnica |  | ALN | 50 | OPZ |  |  | Wildcard |
| POSNR | Posizione |  | ALN | 50 | OPZ |  |  | Wildcard |
| POST_CODE1 | CAP |  | ALN | 50 | OPZ |  |  | Wildcard |
| RBNR | Profilo catalogo |  | ALN | 50 | OPZ |  |  | Wildcard |
| RBNRX | Test per profilo catalogo |  | ALN | 50 | OPZ |  |  | Wildcard |
| REGION | Provincia |  | ALN | 50 | OPZ |  |  | Wildcard |
| STREET | Via |  | ALN | 50 | OPZ |  |  | Wildcard |
| STRNO | Codice della sede tecnica | X | ALN | 50 | OBB |  |  | Wildcard |
| STTXT | Stato del sistema |  | ALN | 50 | OPZ |  |  | Wildcard |
| STTXU | Stato utente |  | ALN | 50 | OPZ |  |  | Wildcard |
| SUBMT | Tipo di costruzione |  | ALN | 50 | OPZ |  |  | Wildcard |
| SWERK | Divisione di ubicazione |  | ALN | 50 | OPZ |  |  | Wildcard |
| TPLKZ | Codice della struttura |  | ALN | 50 | OPZ |  |  | Wildcard |
| TPLMA | Sede tecnica superiore |  | ALN | 50 | OPZ |  |  | Wildcard |
| TPLNR | Codice TPLNR |  | ALN | 50 | OPZ |  |  | Wildcard |
| TXT_GEWRK | Descrizione del centro di lavoro responsabile |  | ALN | 50 | OPZ |  |  | Wildcard |
| TXT_TPLKZ | Testo del codice della struttura |  | ALN | 50 | OPZ |  |  | Wildcard |
| TYPTX | Descrizione della categoria della sede tecnica |  | ALN | 50 | OPZ |  |  | Wildcard |
| WERGW | Divisione del centro di lavoro responsabile |  | ALN | 50 | OPZ |  |  | Wildcard |
| ZZITO_ORD41 | Tipologia |  | ALN | 50 | OPZ |  |  | Wildcard |
| ZZITO_UMWKZ | Regione |  | ALN | 50 | OPZ |  |  | Wildcard |
| ZZSOCP | Società proprietaria |  | ALN | 50 | OPZ |  |  | Wildcard |
| CREATDATE | Data di creazione |  | DATE | 4 | OPZ |  |  | Exact |
| DESCRIPTION | Descrizione |  | ALN | 50 | OPZ |  |  | Wildcard |
| DESCRIPTION_LONGDESCRIPTION | Descrizione lunga |  | ALN | 32000 | OPZ |  |  | Text |
| DUPLICATE | Campo contrassegnato con un “1” quando la sede tecnica è contrassegnata come duplicata in doppia terna |  | YORN | 1 | OBB |  |  | Exact |
| HASLD | Campo contrassegnato con un “1” quando la sede tecnica ha una descrizione lunga |  | YORN | 1 | OBB |  |  | Exact |
| INPUTFILE | File da cui proviene il record |  | ALN | 100 | OPZ |  |  | Wildcard |
| LOADED | Sottoinsieme di caricamento |  | INTEGER | 12 | OPZ |  |  | Exact |
| X3FICT | Campo contrassegnato con un “1” quando la sede tecnica e’fittizia |  | ALN | 50 | OPZ |  |  | Exact |
| X3SEDITECHSAPID | Numero di identificazione interno di questa tabella | X | BIGINT | 19 | OBB |  |  | Exact |
| X3ELENCOEX | Cardinalità specifica |  | INTEGER | 12 | OPZ |  |  | Exact |
| CHUNK_ID | Pezzo di carico parallelo a cui appartiene questo asset |  | INTEGER | 12 | OPZ |  |  | Exact |
| ETL_RESULT | Risultato del caricamento |  | ALN | 50 | OPZ |  |  | Wildcard |
| ETL_RESULT_DESCRIPTION | Descrizione del risultato del caricamento |  | ALN | 100 | OPZ |  |  | Wildcard |
| TIMESTAMP_EXECUTION_START | Ora di inizio del processo di caricamento |  | DATETIME | 10 | OPZ |  |  | Exact |
| TIMESTAMP_EXECUTION_END | Ora di fine del processo di caricamento |  | DATETIME | 10 | OPZ |  |  | Exact |
| EAM_ASSET_ID | ASSETNUM dell’asset generato in EAM(2) |  | UPPER | 25 | OPZ |  |  | Wildcard |
| WORKFLOW_RUN_ID | ID dell’esecuzione del processo di migrazione |  | INTEGER | 12 | OPZ |  |  | Exact |
| STEP_TIMEOUT_FLAG | Campo contrassegnato con una “Y” quando il processo è scaduto |  | UPPER | 1 | OPZ |  |  | Wildcard |
| CHUNCK_ID_RECUPERO | CHUNK_ID di riesecuzione per le righe con STEP_TIMEOUT_FLAG = Y |  | INTEGER | 12 | OPZ |  |  | Exact |
| BLACKLIST | Popolato quando l’esecuzione presenta qualche errore. Quando una sede tecnica viene inserita nella blacklist, vengono inserite nella blacklist anche tutte le sue caratteristiche e children |  | ALN | 50 | OPZ |  |  | Wildcard |
| AUTOMATIC_SCRIPT_RESULT | Risultato del lancio dello script |  | ALN | 1000 | OPZ |  |  | Wildcard |

Dominio campo:

OPZ -> Opzionale. IBM Máximo permette che il campo abbia valore “Null”.

OBB -> Obbligatorio. IBM Máximo non permette che il campo abbia valore “Null”.

(2) Il campo ASSETNUM è inizialmente non valorizzato. Viene valorizzato con il valore assegnato all’asset da Máximo durante la migrazione. Per cui, prima dell’esecuzione del singolo step del workflow (che ha l’obiettivo di migrare un determinato tipo di asset, ad esempio le campate) non sarà valorizzato. Solo dopo l’esecuzione dello step dedicato alle campate verrà popolato con il valore dell’asset num staccato su Máximo.

Tabella X3CARATSAP:

| Atttribute | Description | Chiave | Type | Lenght | Reqd? |
| --- | --- | --- | --- | --- | --- |
| OBJECT_ID | ID oggetto | X | ALN | 40 | OBB |
| OBJNR | ID interno SAP | X | ALN | 40 | OBB |
| KLASSE | Classe |  | ALN | 40 | OBB |
| ATNAM | Caratteristica |  | ALN | 40 | OBB |
| ATNAMTX | Descrizione della caratteristica |  | ALN | 40 | OPZ |
| UDM | UDM |  | ALN | 16 | OPZ |
| UDM_TYP | Tipo di dati |  | ALN | 10 | OPZ |
| DAT_TYP | Valore data |  | DATE | 4 | OPZ |
| CHAR_TYP | Valore alfanumerico |  | ALN | 40 | OPZ |
| NUM_TYP | Valore numerico |  | INTEGER | 12 | OPZ |
| BOO_TYP | Valore booleano |  | YORN | 1 | OBB |
| DEC_TYP | Valore decimale |  | DECIMAL | 24 | OPZ |
| LOADED | Sottoinsieme di caricamento |  | INTEGER | 12 | OPZ |
| CREATDATE | Data di creazione |  | DATE | 10 | OPZ |
| X3CARATSAPID | Numero di identificazione interno di questa tabella |  | BIGINT | 19 | OBB |
| INPUTFILE | File da cui proviene il record |  | ALN | 100 | OPZ |
| BLACKLIST | Popolato quando l’esecuzione presenta qualche errore. Quando una sede tecnica viene inserita nella blacklist, vengono inserite nella blacklist anche tutte le sue caratteristiche e children |  | ALN | 50 | OPZ |
| DESCRIPTION | Descrizione |  | ALN | 50 | OPZ |
| DESCRIPTION_LONGDESCRIPTION | Descrizione lunga |  | LONGALN | 32000 | OPZ |
| HASLD | Campo contrassegnato con un “1” quando la sede tecnica ha una descrizione lunga |  | YORN | 1 | OBB |

Tabella X3ESTREMOCAVO:

| Atttribute | Description | Chiave | Type | Lenght | Reqd? |
| --- | --- | --- | --- | --- | --- |
| X3ESTREMOCAVOID | Numero di identificazione interno di questa tabella | X | BIGINT | 19 | OBB |
| DESCRIPTION | Descrizione |  | ALN | 50 | OPZ |
| DESCRIPTION_LONGDESCRIPTION | Descrizione lunga |  | LONGALN | 32000 | OPZ |
| HASLD | Campo contrassegnato con un “1” quando la sede tecnica ha una descrizione lunga |  | YORN | 1 | OBB |
| CLASSSTRUCTUREID | Tipo di asset |  | UPPER | 20 | OBB |
| GEWRK | Centro di lavoro responsabile |  | ALN | 50 | OPZ |
| INGRP | Gruppo responsabile della pianificazione della manutenzione |  | ALN | 50 | OPZ |
| LOADEDASSET | LOADED dell’asset generato in EAM |  | YORN | 1 | OPZ |
| RBNR | Profilo catalogo |  | ALN | 50 | OPZ |
| R_ASSETRELATIONNUM | Valore da inserire nell’omonimo campo MAXIMO standard della tabella ASSETLOCRELATION |  | UPPER | 12 | OPZ |
| R_ISLINEARREF | Valore da inserire nell’omonimo campo MAXIMO standard della tabella ASSETLOCRELATION |  | YORN | 1 | OPZ |
| R_LINEARASSETLOCRELATIONID | Valore da inserire nell’omonimo campo MAXIMO standard della tabella ASSETLOCRELATION |  | BIGINT | 19 | OPZ |
| R_LOADED | Valore da inserire nell’omonimo campo MAXIMO della tabella ASSETLOCRELATION |  | YORN | 1 | OPZ |
| R_ORGID | Valore da inserire nell’omonimo campo MAXIMO standard della tabella ASSETLOCRELATION |  | UPPER | 8 | OPZ |
| R_SITEID | Valore da inserire nell’omonimo campo MAXIMO standard della tabella ASSETLOCRELATION |  | UPPER | 8 | OPZ |
| R_SOURCEASSETNUM | Valore da inserire nell’omonimo campo MAXIMO standard della tabella ASSETLOCRELATION |  | UPPER | 25 | OBB |
| STRNO | Codice della sede tecnica |  | ALN | 50 | OPZ |
| STTXU | Stato utente |  | ALN | 50 | OPZ |
| LOADED | Sottoinsieme di caricamento |  | INTEGER | 12 | OPZ |
| CHUNK_ID | Pezzo di carico parallelo a cui appartiene questo asset |  | INTEGER | 12 | OPZ |
| ETL_RESULT | Risultato del caricamento |  | ALN | 50 | OPZ |
| ETL_RESULT_DESCRIPTION | Descrizione del risultato del caricamento |  | ALN | 100 | OPZ |
| TIMESTAMP_EXECUTION_START | Ora di inizio del processo di caricamento |  | DATETIME | 10 | OPZ |
| TIMESTAMP_EXECUTION_END | Ora di fine del processo di caricamento |  | DATETIME | 10 | OPZ |
| EAM_ASSET_ID | ASSETNUM dell’asset generato in EAM |  | UPPER | 25 | OPZ |
| WORKFLOW_RUN_ID | ID dell’esecuzione del processo di migrazione |  | INTEGER | 12 | OPZ |
| STEP_TIMEOUT_FLAG | Campo contrassegnato con una “Y” quando il processo è scaduto |  | UPPER | 1 | OPZ |
| CHUNCK_ID_RECUPERO | CHUNK_ID di riesecuzione per le righe con STEP_TIMEOUT_FLAG = Y |  | INTEGER | 12 | OPZ |
| BLACKLIST | Popolato quando l’esecuzione presenta qualche errore. Quando una sede tecnica viene inserita nella blacklist, vengono inserite nella blacklist anche tutte le sue caratteristiche e children |  | ALN | 50 | OPZ |
| AUTOMATIC_SCRIPT_RESULT | Risultato del lancio dello script |  | ALN | 1000 | OPZ |

Tabella X3GENLVL3NOSAP:

| Atttribute | Description | Chiave | Type | Lenght | Reqd? |
| --- | --- | --- | --- | --- | --- |
| X3GENLVL3NOSAPID | Numero di identificazione interno di questa tabella | X | BIGINT | 19 | OBB |
| DESCRIPTION | Descrizione |  | ALN | 50 | OPZ |
| DESCRIPTION_LONGDESCRIPTION | Descrizione lunga |  | LONGALN | 32000 | OPZ |
| HASLD | Campo contrassegnato con un “1” quando la sede tecnica ha una descrizione lunga |  | YORN | 1 | OBB |
| CLASSSTRUCTUREID | ID di tipo di asset |  | UPPER | 20 | OPZ |
| LOADEDASSET | LOADED dell’asset generato in EAM |  | YORN | 1 | OBB |
| PARENT | ASSETNUM del padre |  | UPPER | 25 | OPZ |
| STATUS | Stato dell’asset |  | ALN | 20 | OPZ |
| X3SAPIDETL | STRNO dell’asset |  | ALN | 30 | OPZ |
| LOADED | LOADED della sede tecnica |  | INTEGER | 12 | OPZ |
| CHUNK_ID | Pezzo di carico parallelo a cui appartiene questo asset |  | INTEGER | 12 | OPZ |
| ETL_RESULT | Risultato del caricamento |  | ALN | 50 | OPZ |
| ETL_RESULT_DESCRIPTION | Descrizione del risultato del caricamento |  | ALN | 100 | OPZ |
| TIMESTAMP_EXECUTION_START | Ora di inizio del processo di caricamento |  | DATETIME | 10 | OPZ |
| TIMESTAMP_EXECUTION_END | Ora di fine del processo di caricamento |  | DATETIME | 10 | OPZ |
| EAM_ASSET_ID | ASSETNUM dell’asset generato in EAM |  | UPPER | 25 | OPZ |
| WORKFLOW_RUN_ID | ID dell’esecuzione del processo di migrazione |  | INTEGER | 12 | OPZ |
| STEP_TIMEOUT_FLAG | Campo contrassegnato con una “Y” quando il processo è scaduto |  | UPPER | 1 | OPZ |
| CHUNCK_ID_RECUPERO | CHUNK_ID di riesecuzione per le righe con STEP_TIMEOUT_FLAG = Y |  | INTEGER | 12 | OPZ |
| BLACKLIST | Popolato quando l’esecuzione presenta qualche errore. Quando una sede tecnica viene inserita nella blacklist, vengono inserite nella blacklist anche tutte le sue caratteristiche e children |  | ALN | 50 | OPZ |
| AUTOMATIC_SCRIPT_RESULT | Risultato del lancio dello script |  | ALN | 1000 | OPZ |

Tabella X3GENLVL4SAPISOLATORE:

| Atttribute | Description | Chiave | Type | Lenght | Reqd? |
| --- | --- | --- | --- | --- | --- |
| X3GENLVL4SAPISOLATOREID | Numero di identificazione interno di questa tabella | X | BIGINT | 19 | OBB |
| DESCRIPTION | Descrizione |  | ALN | 50 | OPZ |
| DESCRIPTION_LONGDESCRIPTION | Descrizione lunga |  | LONGALN | 32000 | OPZ |
| HASLD | Campo contrassegnato con un “1” quando la sede tecnica ha una descrizione lunga |  | YORN | 1 | OBB |
| BURKS | Società |  | ALN | 50 | OPZ |
| EQFNR | Campo sort |  | ALN | 50 | OPZ |
| GEWRK | Centro di lavoro responsabile |  | ALN | 50 | OPZ |
| INBDT | In funzione dal |  | DATE | 4 | OPZ |
| INGRP | Gruppo responsabile della pianificazione della manutenzione |  | ALN | 50 | OPZ |
| KOKRS | Area di controllo |  | ALN | 50 | OPZ |
| PLTXT | Definizione della sede tecnica |  | ALN | 50 | OPZ |
| STRNO | Codice della sede tecnica |  | ALN | 50 | OPZ |
| STTXU | Stato utente |  | ALN | 50 | OPZ |
| SWERK | Divisione di ubicazione |  | ALN | 50 | OPZ |
| TPLMA | Sede tecnica superiore |  | ALN | 50 | OPZ |
| TPLNR | Codice TPLNR |  | ALN | 50 | OPZ |
| ZZSOCP | Società proprietaria |  | ALN | 50 | OPZ |
| X3ELENCOEX | Cardinalità specifica |  | INTEGER | 12 | OPZ |
| ASSETNUM1037 | ASSETNUM dell’asset generato in EAM |  | UPPER | 25 | OPZ |
| SAPPARENTUID | SAPUID della sede tecnica superiore |  | ALN | 50 | OPZ |
| LOADEDASSET | LOADED dell’asset generato in EAM |  | YORN | 1 | OBB |
| STATUS | Stato dell’asset |  | ALN | 20 | OPZ |
| LOCATION | Ubicazione dell’asset |  | ALN | 50 | OPZ |
| CLASSSTRUCTUREID | Tipo di asset |  | UPPER | 20 | OBB |
| SAPOBJECT | Valore da inserire nell’omonimo campo MAXIMO della tabella X3SAPASSET |  | UPPER | 1 | OPZ |
| ROOTASSETTYPE | Valore da inserire nell’omonimo campo MAXIMO della tabella X3SAPASSET |  | UPPER | 1 | OPZ |
| MAPPINGERROR | Valore da inserire nell’omonimo campo MAXIMO della tabella X3SAPASSET |  | YORN | 1 | OBB |
| SAPCLASS | Classe di asset in sap |  | UPPER | 50 | OPZ |
| SAPLEVEL | Livello dell’asset in sap |  | INTEGER | 12 | OPZ |
| LOADED | Batch di caricamento |  | INTEGER | 12 | OPZ |
| CHUNK_ID | Pezzo di carico parallelo a cui appartiene questo asset |  | INTEGER | 12 | OPZ |
| ETL_RESULT | Risultato del caricamento |  | ALN | 50 | OPZ |
| ETL_RESULT_DESCRIPTION | Descrizione del risultato del caricamento |  | ALN | 100 | OPZ |
| TIMESTAMP_EXECUTION_START | Ora di inizio del processo di caricamento |  | DATETIME | 10 | OPZ |
| TIMESTAMP_EXECUTION_END | Ora di fine del processo di caricamento |  | DATETIME | 10 | OPZ |
| EAM_ASSET_ID | ASSETNUM dell’asset generato in EAM |  | UPPER | 25 | OPZ |
| WORKFLOW_RUN_ID | ID dell’esecuzione del processo di migrazione |  | INTEGER | 12 | OPZ |
| STEP_TIMEOUT_FLAG | Campo contrassegnato con una “Y” quando il processo è scaduto |  | UPPER | 1 | OPZ |
| CHUNCK_ID_RECUPERO | CHUNK_ID di riesecuzione per le righe con STEP_TIMEOUT_FLAG = Y |  | INTEGER | 12 | OPZ |
| BLACKLIST | Popolato quando l’esecuzione presenta qualche errore. Quando una sede tecnica viene inserita nella blacklist, vengono inserite nella blacklist anche tutte le sue caratteristiche e children |  | ALN | 50 | OPZ |
| AUTOMATIC_SCRIPT_RESULT | Risultato del lancio dello script |  | ALN | 1000 | OPZ |

Tabella X3GENLVL4NOSAP:

| Atttribute | Description | Chiave | Type | Lenght | Reqd? |
| --- | --- | --- | --- | --- | --- |
| X3GENLVL4NOSAPID | Numero di identificazione interno di questa tabella | X | BIGINT | 19 | OBB |
| DESCRIPTION | Descrizione |  | ALN | 50 | OPZ |
| DESCRIPTION_LONGDESCRIPTION | Descrizione lunga |  | LONGALN | 32000 | OPZ |
| HASLD | Campo contrassegnato con un “1” quando la sede tecnica ha una descrizione lunga |  | YORN | 1 | OBB |
| STATUS | Stato dell’asset |  | ALN | 20 | OPZ |
| LOADEDASSET | LOADED dell’asset generato in EAM |  | YORN | 1 | OBB |
| PARENT | ASSETNUM del asset parent |  | UPPER | 25 | OPZ |
| CLASSSTRUCTUREID | Tipo di asset |  | UPPER | 20 | OPZ |
| X3SAPID | STRNO dell’asset in SAP |  | ALN | 30 | OPZ |
| LOADED | Batch di caricamento |  | INTEGER | 12 | OPZ |
| CHUNK_ID | Pezzo di carico parallelo a cui appartiene questo asset |  | INTEGER | 12 | OPZ |
| ETL_RESULT | Risultato del caricamento |  | ALN | 50 | OPZ |
| ETL_RESULT_DESCRIPTION | Descrizione del risultato del caricamento |  | ALN | 100 | OPZ |
| TIMESTAMP_EXECUTION_START | Ora di inizio del processo di caricamento |  | DATETIME | 10 | OPZ |
| TIMESTAMP_EXECUTION_END | Ora di fine del processo di caricamento |  | DATETIME | 10 | OPZ |
| EAM_ASSET_ID | ASSETNUM dell’asset generato in EAM |  | UPPER | 25 | OPZ |
| WORKFLOW_RUN_ID | ID dell’esecuzione del processo di migrazione |  | INTEGER | 12 | OPZ |
| STEP_TIMEOUT_FLAG | Campo contrassegnato con una “Y” quando il processo è scaduto |  | UPPER | 1 | OPZ |
| CHUNCK_ID_RECUPERO | CHUNK_ID di riesecuzione per le righe con STEP_TIMEOUT_FLAG = Y |  | INTEGER | 12 | OPZ |
| BLACKLIST | Popolato quando l’esecuzione presenta qualche errore. Quando una sede tecnica viene inserita nella blacklist, vengono inserite nella blacklist anche tutte le sue caratteristiche e children |  | ALN | 50 | OPZ |
| AUTOMATIC_SCRIPT_RESULT | Risultato del lancio dello script |  | ALN | 1000 | OPZ |

Tabella X3GENLVL4FROMATT:

| Atttribute | Description | Chiave | Type | Lenght | Reqd? |
| --- | --- | --- | --- | --- | --- |
| X3GENLVL4FROMATTID | Numero di identificazione interno di questa tabella | X | BIGINT | 19 | OBB |
| DESCRIPTION | Descrizione |  | ALN | 50 | OPZ |
| DESCRIPTION_LONGDESCRIPTION | Descrizione lunga |  | LONGALN | 32000 | OPZ |
| HASLD | Campo contrassegnato con un “1” quando la sede tecnica ha una descrizione lunga |  | YORN | 1 | OBB |
| X3CARATSAPID | Numero di identificazione della caratteristica che genera l’asset |  | BIGINT | 19 | OPZ |
| KLASSESOURCE | Classe SAP dell’asset generato da la caratteristica |  | ALN | 40 | OPZ |
| ATNAMSOURCE | Nome della caratteristica che genera l’asset |  | ALN | 40 | OPZ |
| CONDITIONFIELD | Campo in cui si applica la condizione |  | ALN | 40 | OPZ |
| CONDITIONCOMPARATOR | Comparatore della condizione |  | ALN | 10 | OPZ |
| CONDITIONVALUE | Valore della condizione |  | ALN | 40 | OPZ |
| STATUS | Stato dell’asset |  | ALN | 20 | OPZ |
| LOADEDASSET | LOADED dell’asset generato in EAM |  | YORN | 1 | OBB |
| PARENT | ASSETNUM del padre |  | UPPER | 25 | OPZ |
| CLASSSTRUCTUREID | Tipo di asset |  | UPPER | 20 | OPZ |
| X3SAPID | STRNO dell’asset in SAP |  | ALN | 30 | OPZ |
| LOADED | Batch di caricamento |  | INTEGER | 12 | OPZ |
| CHUNK_ID | Pezzo di carico parallelo a cui appartiene questo asset |  | INTEGER | 12 | OPZ |
| ETL_RESULT | Risultato del caricamento |  | ALN | 50 | OPZ |
| ETL_RESULT_DESCRIPTION | Descrizione del risultato del caricamento |  | ALN | 100 | OPZ |
| TIMESTAMP_EXECUTION_START | Ora di inizio del processo di caricamento |  | DATETIME | 10 | OPZ |
| TIMESTAMP_EXECUTION_END | Ora di fine del processo di caricamento |  | DATETIME | 10 | OPZ |
| EAM_ASSET_ID | ASSETNUM dell’asset generato in EAM |  | UPPER | 25 | OPZ |
| WORKFLOW_RUN_ID | ID dell’esecuzione del processo di migrazione |  | INTEGER | 12 | OPZ |
| STEP_TIMEOUT_FLAG | Campo contrassegnato con una “Y” quando il processo è scaduto |  | UPPER | 1 | OPZ |
| CHUNCK_ID_RECUPERO | CHUNK_ID di riesecuzione per le righe con STEP_TIMEOUT_FLAG = Y |  | INTEGER | 12 | OPZ |
| BLACKLIST | Popolato quando l’esecuzione presenta qualche errore. Quando una sede tecnica viene inserita nella blacklist, vengono inserite nella blacklist anche tutte le sue caratteristiche e children |  | ALN | 50 | OPZ |
| AUTOMATIC_SCRIPT_RESULT | Risultato del lancio dello script |  | ALN | 1000 | OPZ |

Tabella X3HEREDADOS:

| Atttribute | Description | Chiave | Type | Lenght | Reqd? |
| --- | --- | --- | --- | --- | --- |
| X3HEREDADOSID | Numero di identificazione interno di questa tabella | X | BIGINT | 19 | OBB |
| DESCRIPTION | Descrizione |  | ALN | 50 | OPZ |
| DESCRIPTION_LONGDESCRIPTION | Descrizione lunga |  | LONGALN | 32000 | OPZ |
| HASLD | Campo contrassegnato con un “1” quando la sede tecnica ha una descrizione lunga |  | YORN | 1 | OBB |
| ASSETSPECID | ID dell’attributo della tabella ASSETSPECT |  | BIGINT | 19 | OBB |
| CAMPODESTINAZIONE | Campo ASSETSPEC da popolare |  | ALN | 100 | OPZ |
| CHUNCK_ID_RECUPERO | CHUNK_ID di riesecuzione per le righe con STEP_TIMEOUT_FLAG = Y |  | INTEGER | 12 | OPZ |
| CHUNK_ID | Pezzo di carico parallelo a cui appartiene questo asset |  | INTEGER | 12 | OPZ |
| EAM_ASSET_ID | ASSETNUM dell’asset popolato |  | UPPER | 25 | OPZ |
| ETL_RESULT | Risultato del caricamento |  | ALN | 50 | OPZ |
| ETL_RESULT_DESCRIPTION | Descrizione del risultato del caricamento |  | ALN | 100 | OPZ |
| LOADED | Batch di caricamento |  | INTEGER | 12 | OPZ |
| QUERY_TIPOLOGY | Numero identificativo della query all’interno dello script |  | ALN | 200 | OPZ |
| STEP_TIMEOUT_FLAG | Campo contrassegnato con una “Y” quando il processo è scaduto |  | UPPER | 1 | OPZ |
| TIMESTAMP_EXECUTION_START | Ora di inizio del processo di caricamento |  | DATETIME | 10 | OPZ |
| TIMESTAMP_EXECUTION_END | Ora di fine del processo di caricamento |  | DATETIME | 10 | OPZ |
| VALOREDESTINAZIONE | Valore da inserire nel campo destinazione |  | ALN | 1000 | OPZ |
| WORKFLOW_RUN_ID | ID dell’esecuzione del processo di migrazione |  | INTEGER | 12 | OPZ |
| AUTOMATIC_SCRIPT_RESULT | Risultato del lancio dello script |  | ALN | 1000 | OPZ |

Tabella X3TESTX3ETL:

| Atttribute | Description | Chiave | Type | Lenght | Reqd? |
| --- | --- | --- | --- | --- | --- |
| X3GENLVL4NOSAPID | Numero di identificazione interno di questa tabella | X | BIGINT | 19 | OBB |
| DESCRIPTION | Descrizione |  | ALN | 50 | OPZ |
| DESCRIPTION_LONGDESCRIPTION | Descrizione lunga |  | LONGALN | 32000 | OPZ |
| HASLD | Il record ha una descrizione lunga |  | YORN | 1 | OBB |
| ASSETSPECID | ID dell’attributo della tabella ASSETSPECT |  | BIGINT | 19 | OPZ |
| ASSETSPECIDDES | ASSETSPEC dell’asset destro |  | BIGINT | 19 | OPZ |
| ASSETSPECIDSIN | ASSETSPEC dell’asset sinistro |  | BIGINT | 19 | OPZ |
| ASSETSPECIDTER | ASSETSPEC dell’asset terzo |  | BIGINT | 19 | OPZ |
| CAMPODESTINAZIONE | Campo ASSETSPEC da popolare |  | ALN | 100 | OPZ |
| CAMPODESTINAZIONEDES | Campo destinazione dell’asset destro |  | ALN | 100 | OPZ |
| CAMPODESTINAZIONESIN | Campo destinazione dell’asset sinistro |  | ALN | 100 | OPZ |
| CAMPODESTINAZIONETER | Campo destinazione dell’asset terzo |  | ALN | 100 | OPZ |
| CHUNCK_ID_RECUPERO | CHUNK_ID di riesecuzione per le righe con STEP_TIMEOUT_FLAG = Y |  | INTEGER | 12 | OPZ |
| CHUNK_ID | Pezzo di carico parallelo a cui appartiene questo asset |  | INTEGER | 12 | OPZ |
| EAM_ASSET_DES_ID | ASSETNUM dell’asset destro |  | UPPER | 25 | OPZ |
| EAM_ASSET_ID | ASSETNUM dell’asset popolato |  | UPPER | 25 | OPZ |
| EAM_ASSET_SIN_ID | ASSETNUM dell’asset sinistro |  | UPPER | 25 | OPZ |
| EAM_ASSET_TER_ID | ASSETNUM dell’asset terzo |  | UPPER | 25 | OPZ |
| ETL_RESULT | Risultato del caricamento |  | ALN | 50 | OPZ |
| ETL_RESULT_DESCRIPTION | Descrizione del risultato del caricamento |  | ALN | 100 | OPZ |
| LOADED | Batch di caricamento |  | INTEGER | 12 | OPZ |
| QUERY_TIPOLOGY | Numero identificativo della query all’interno dello script |  | ALN | 200 | OPZ |
| STEP_TIMEOUT_FLAG | Campo contrassegnato con una “Y” quando il processo è scaduto |  | UPPER | 1 | OPZ |
| TIMESTAMP_EXECUTION_END | Ora di fine del processo di caricamento |  | DATETIME | 10 | OPZ |
| TIMESTAMP_EXECUTION_START | Ora di inizio del processo di caricamento |  | DATETIME | 10 | OPZ |
| VALOREDESTINAZIONE | Valore da inserire nel campo destinazione |  | ALN | 1000 | OPZ |
| VALOREDESTINZIONEDES | Valore da inserire nel campo destinazione dell’asset destro |  | ALN | 1000 | OPZ |
| VALOREDESTINZIONESIN | Valore da inserire nel campo destinazione dell’asset sinistro |  | ALN | 1000 | OPZ |
| VALOREDESTINZIONETER | Valore da inserire nel campo destinazione dell’asset terzo |  | ALN | 1000 | OPZ |
| WORKFLOW_RUN_ID | ID dell’esecuzione del processo di migrazione |  | INTEGER | 12 | OPZ |
| AUTOMATIC_SCRIPT_RESULT | Risultato del lancio dello script |  | ALN | 1000 | OPZ |

### Nota

Non è previsto l’utilizzo di domini all’interno delle tabelle di staging; tutti i valori presenti nelle tabelle di staging verranno importati da SAP senza apportare alcuna modifica.

Non è necessario creare manualmente i record in Maximo su queste tabelle, o gestirle in alcun modo, poiché si assume che le informazioni estratte da SAP siano corrette e quindi non è necessario associare i domini a queste tabelle.

## Tabelle on premise

Nella tabella sottostante sono riportate tutte le tabelle del DB Oracle on Premise caricate dal Client Oracle SAP.

Nelle tabelle successive di questo paragrafo sono riportate le DDL con la descrizione di ogni singolo campo.

| Tabella | Utilità |
| --- | --- |
| X3SEDITECHSAP_IF | Tabella di pre-staging usata per popolare la tabella di staging X3SEDITECHSAP in IBM Maximo database. |
| X3CARATSAP_IF | Tabella di pre-staging usata per popolare la tabella di staging X3CARATSAP in IBM Maximo database. |
| X3SEDIINDISP_IF | Tabella di pre-staging usata per popolare la tabella X3SEDIINDISP (*) in the IBM Maximo database. |
| X3GEWRK_IF | Tabella di pre-staging usata per popolare la tabella X3GEWRK (*) in IBM áximo database. |

(*) Non é una tabella di staging EAM ma un dominio tipo tabella di anagrafica.

Tabella X3SEDITECHSAP_IF

| Attribute | Description | Chiave | Type | Length |
| --- | --- | --- | --- | --- |
| BUKRS | Società |  | ALN | 50 |
| BUTXT | Descrizione della società |  | ALN | 50 |
| CITY1 | Città |  | ALN | 50 |
| COUNTRY | Paese |  | ALN | 50 |
| EQFNR | Campo sort |  | ALN | 50 |
| EXTRACTION_DATE | Data di estrazione |  | DATE | 4 |
| EXTRACTION_ID | ID di estrazione |  | ALN | 40 |
| FLTYP | Categoria della sede tecnica |  | ALN | 50 |
| GEWRK | Centro di lavoro responsabile |  | ALN | 50 |
| INBDT | In funzione dal |  | DATE | 4 |
| INGRP | Gruppo responsabile della pianificazione della manutenzione |  | ALN | 50 |
| INNAM | Descrizione del gruppo responsabile della pianificazione della manutenzione |  | ALN | 50 |
| IWERK | Divisione di pianificazione della manutenzione |  | ALN | 50 |
| KOKRS | Area di controllo |  | ALN | 50 |
| KOSTL | Centro di costo |  | ALN | 50 |
| KTEXT | Descrizione dell’area di controllo |  | ALN | 50 |
| LIVELLO | Livello della sede tecnica |  | ALN | 50 |
| MSGRP | Impianto |  | ALN | 50 |
| NAME_SWERK | Descrizione della divisione di ubicazione |  | ALN | 50 |
| NAME1 | Nome 1 |  | ALN | 50 |
| NAME1_MAN | Descrizione della divisione di pianificazione della manutenzione |  | ALN | 50 |
| NAME2 | Nome 2 |  | ALN | 50 |
| OBJNR | ID SAP univoco | X | ALN | 50 |
| PLTXT | Definizione della sede tecnica |  | ALN | 50 |
| POSNR_CSV | Posizione |  | ALN | 50 |
| POST_CODE1 | CAP |  | ALN | 50 |
| RBNR | Profilo catalogo |  | ALN | 50 |
| RBNRX | Test per profilo catalogo |  | ALN | 50 |
| REGION | Provincia |  | ALN | 50 |
| STREET | Via |  | ALN | 50 |
| STRNO | Codice della sede tecnica | X | ALN | 50 |
| SUBMT | Tipo di costruzione |  | ALN | 50 |
| SWERK | Divisione di ubicazione |  | ALN | 50 |
| TPLKZ | Codice della struttura |  | ALN | 50 |
| TPLMA | Sede tecnica superiore |  | ALN | 50 |
| TPLNR | Codice TPLNR |  | ALN | 50 |
| TXT_GEWRK | Descrizione del centro di lavoro responsabile |  | ALN | 50 |
| TXT_TPLKZ | Testo del codice della struttura |  | ALN | 50 |
| TYPTX | Descrizione della categoria della sede tecnica |  | ALN | 50 |
| WERGW | Divisione del centro di lavoro responsabile |  | ALN | 50 |
| ZSOCPR | Società proprietaria |  | ALN | 50 |
| ZSTATO_SISTEMA | Stato del sistema |  | ALN | 50 |
| ZSTATO_UTENTE | Stato utente |  | ALN | 50 |
| ZZITO_ORD41_CSV | Tipologia |  | ALN | 50 |
| ZZITO_UMWKZ_CSV | Regione |  | ALN | 50 |

Tabella X3CARATSAP_IF

| Atttribute | Description | Chiave | Type | Lenght |
| --- | --- | --- | --- | --- |
| ATNAM | Caratteristica |  | ALN | 40 |
| ATNAMTX | Descrizione della caratteristica |  | ALN | 40 |
| BOO_TYP | Valore booleano |  | YORN | 1 |
| CHAR_TYP | Valore alfanumerico |  | ALN | 40 |
| DAT_TYP | Valore data |  | DATE | 4 |
| DEC_TYP | Valore decimale |  | DECIMAL | 24 |
| EXTRACTION_DATE | Data di estrazione |  | DATE | 4 |
| EXTRACTION_ID | ID di estrazione |  | ALN | 40 |
| KLASSE | Classe |  | ALN | 40 |
| NUM_TYP | Valore numerico |  | INTEGER | 12 |
| OBJECT_ID | ID oggetto | X | ALN | 40 |
| OBJNR | ID interno SAP | X | ALN | 40 |
| UDM | UDM |  | ALN | 16 |
| UDM_TYP | Tipo di dati |  | ALN | 10 |

Tabella X3SEDIINDISP_IF

| Attribute | Description | Chiave | Type | Length |
| --- | --- | --- | --- | --- |
| BUKRS | Società |  | ALN | 50 |
| BUTXT | Descrizione della società |  | ALN | 50 |
| CITY1 | Città |  | ALN | 50 |
| COUNTRY | Paese |  | ALN | 50 |
| EQFNR | Campo sort |  | ALN | 50 |
| EXTRACTION_DATE | Data di estrazione |  | DATE | 4 |
| EXTRACTION_ID | ID di estrazione |  | ALN | 40 |
| FLTYP | Categoria della sede tecnica |  | ALN | 50 |
| GEWRK | Centro di lavoro responsabile |  | ALN | 50 |
| INBDT | In funzione dal |  | DATE | 4 |
| INGRP | Gruppo responsabile della pianificazione della manutenzione |  | ALN | 50 |
| INNAM | Descrizione del gruppo responsabile della pianificazione della manutenzione |  | ALN | 50 |
| IWERK | Divisione di pianificazione della manutenzione |  | ALN | 50 |
| KOKRS | Area di controllo |  | ALN | 50 |
| KOSTL | Centro di costo |  | ALN | 50 |
| KTEXT | Descrizione dell’area di controllo |  | ALN | 50 |
| LIVELLO | Livello della sede tecnica |  | ALN | 50 |
| MSGRP | Impianto |  | ALN | 50 |
| NAME_SWERK | Descrizione della divisione di ubicazione |  | ALN | 50 |
| NAME1 | Nome 1 |  | ALN | 50 |
| NAME1_MAN | Descrizione della divisione di pianificazione della manutenzione |  | ALN | 50 |
| NAME2 | Nome 2 |  | ALN | 50 |
| OBJNR | ID SAP univoco | X | ALN | 50 |
| PLTXT | Definizione della sede tecnica |  | ALN | 50 |
| POSNR_CSV | Posizione |  | ALN | 50 |
| POST_CODE1 | CAP |  | ALN | 50 |
| RBNR | Profilo catalogo |  | ALN | 50 |
| RBNRX | Test per profilo catalogo |  | ALN | 50 |
| REGION | Provincia |  | ALN | 50 |
| STREET | Via |  | ALN | 50 |
| STRNO | Codice della sede tecnica | X | ALN | 50 |
| SUBMT | Tipo di costruzione |  | ALN | 50 |
| SWERK | Divisione di ubicazione |  | ALN | 50 |
| TPLKZ | Codice della struttura |  | ALN | 50 |
| TPLMA | Sede tecnica superiore |  | ALN | 50 |
| TPLNR | Codice TPLNR |  | ALN | 50 |
| TXT_GEWRK | Descrizione del centro di lavoro responsabile |  | ALN | 50 |
| TXT_TPLKZ | Testo del codice della struttura |  | ALN | 50 |
| TYPTX | Descrizione della categoria della sede tecnica |  | ALN | 50 |
| WERGW | Divisione del centro di lavoro responsabile |  | ALN | 50 |
| ZSOCPR | Società proprietaria |  | ALN | 50 |
| ZSTATO_SISTEMA | Stato del sistema |  | ALN | 50 |
| ZSTATO_UTENTE | Stato utente |  | ALN | 50 |
| ZZITO_ORD41_CSV | Tipologia |  | ALN | 50 |
| ZZITO_UMWKZ_CSV | Regione |  | ALN | 50 |

Tabella X3GEWRK_IF

| Attribute | Description | Chiave | Type | Length |
| --- | --- | --- | --- | --- |
| ARBPL | Centro di lavoro responsabile |  | ALN | 8 |
| EXTRACTION_ID | ID di estrazione |  | ALN | 40 |
| TXT_GEWRK | Descrizione del centro di lavoro responsabile |  | ALN | 40 |
| WERKS | Divisione di ubicazione |  | ALN | 4 |

Queste tabelle avranno tutti i campi necessari delle nostre tabelle di staging più campi specifici che servono per identificare l’estrazione (EXTRACTION_ID, EXTRACTION_DATE). Per ulteriori informazioni sul flusso Staging Sap à Oracle on premise consultare il documento ID 2 “Terna – SAP DB Connection to Oracle”.

.

# Conversione della struttura oggetto

In questo capitolo vengono descritte:

Le tabelle IBM Maximo su cui ETL andrà a scrivere e le relazioni gerarchiche;

Le regole del processo di conversione (riportato il riferimento al documento di mapping)

## Oggetti e Relazioni gerarchiche

Configurazione in Maximo della tabella relativa agli oggetti e relazioni gerarchiche da gestire:

| Object | Parent | Field relationship |
| --- | --- | --- |
| ASSET | ASSET |  |
| ASSETSPEC | ASSET | ASSET.ASSETNUM = ASSETSPEC.ASSETNUM AND ASSET.SITEID = ASSETSPEC.SITEID |
| ASSETATTRIBUTE | ASSET | ASSET.ASSETNUM = ASSETATTRIBUTE.ASSETNUM AND ASSET.SITEID = ASSETATTRIBUTE.SITEID |
| ASSETLOCRELATION | ASSET | ASSET.ASSETNUM = ASSETLOCRELATION.SOURCEASSETNUM AND ASSET.SITEID = ASSETLOCRELATION.SITEID |

Tabella 1: Oggetti e Relazioni gerarchiche

La mappatura finale tra i campi SAP e i campi MAXIMO è definita nel file ID 4 Documenti correlati “REV 20230710 of attributi-EAM-SAP”.

La mappatura finale tra gli attributi di classificazione SAP e Maximo è disponibile nel documento ID 1 documenti correlati “Mapping Anagrafica EAM AM_Dati Linee rev.7 250723.LLD”.

# DEscRizione Step Workflow

Il workflow prevede 3 macro-gruppi di step.

Step iniziali che copiano le tabelle on premise su tabelle di staging (#1, #2, #3, #4)

Step dedicati ad identificare gli asset spazzatura, errati, orfani, fittizi, appartenenti all’elenco di esclusione, doppioni, aggiornare lo stato della sede ricevurta da SAP secondo il mapping (es. Stato PROG à INAT). (#5, #6, #7, #8, #9, #10, #11, #12)

Step dedicati alla migrazione (gli step restanti).

La configurazione della tabella relativa all’ordine sequenziale da seguire nel caricamento dei dati al fine di creare tutti i record in modo consistente si trova nel documento ID 3 “EAM_HLD_ETL_ANA_Documento di ETL_20231025_V2.5”.

| Ordine di caricamento | Nome dello script | Descrizione (Gli asset vengono tutti caricati nella tabella ASSET di Maximo. All’interno del workflow é stata esplicitata la tabella solo quando diversa dalla tabella ASSET di Maximo.) |
| --- | --- | --- |
| # | Dettagli dello script | Dettagli della descrizione |
| 1 | NA | Copiare i dati in X3CARATSAP |
| 2 | NA | Copiare i dati in X3SEDITECHSAP |
| 3 | NA | Copiare i dati in X3SEDIINDISP |
| 4 | NA | Copiare i dati in X3GEWRK |
| 5 | NA | Contrassegnare gli asset spazzatura |
| 6 | NA | Contrassegnare gli asset non corretti |
| 7 | NA | Marcatura degli asset orfani |
| 8 | NA | Aggiornare lo stato degli asset |
| 9 | NA | Segnare doppia Asset |
| 10 | NA | Contrassegnare gli Asset fittizi |
| 11 | NA | Marcatura dell’elenco di esclusione (elenco fornito dal business per creare gli elementi in base alle quantitá previste nell’elenco, es: conduttore se 1, conduttore 1 e conduttore 2 se 2, etc...) |
| 12 | NA | Aggiornare i valori delle caratteristiche secondo le descrizioni dei domini da SAP |
| 13 | X3ETLPROCRESTFIXEDX3SAPASSETEXTRAN | Caricamento di asset di livello 1 -> Linea |
| 14 | X3ETLPROCRESTFIXEDX3SAPASSETEXTRAN | Caricamento di asset di livello 2 -> Campata |
| 15 | X3ETLPROCRESTSAPASSETMDFEXTRAN | Caricamento livello 2 -> Campate Fittizie in X3SAPASSET |
| 16 | X3ETLPROCRESTFIXEDX3SAPASSETEXTRAN | Caricamento di asset di livello 2 -> Sostegno |
| 17 | X3ETLPROCRESTSAPASSETMDFEXTRAN | Caricamento livello 2 -> Sostegni Fittizi in X3SAPASSET |
| 18 | X3ETLPROCRESTFIXEDX3SAPASSETEXTRAN | Caricamento di asset di livello 2 -> Tratte in cavo |
| 19 | X3ETLPROCRESTSAPASSETMDFEXTRAN | Caricamento di livello 2 -> Tratte in cavo Fittizie in X3SAPASSET |
| 20 | NA | Popolare tabella X3ESTREMOCAVO |
| 21 | X3ETLPROCRESTESTREMOCAVOEXTRAN | Caricamento di asset di livello 2 -> Estremocavo a partire da Camera Giunti |
| 22 | X3ETLPROCRESTESTREMOCAVOEXTRAN | Caricamento di asset di livello 2 -> Estremocavo a partire da Terminali |
| 23 | X3ETLPROCRESTFIXEDX3SAPASSETEXTRAN | Caricamento di asset di livello 3 -> Conduttori |
| 24 | X3ETLPROCRESTSAPASSETMDFEXTRAN | Caricamento livello 3 -> Conduttori Fittizi in X3SAPASSET |
| 25 | X3ETLPROCRESTFIXEDX3SAPASSETEXTRAN | Caricamento di asset di livello 3 -> Funi di Guardia |
| 26 | X3ETLPROCRESTSAPASSETMDFEXTRAN | Caricamento livello 3 -> Funi di Guardia Fittizie in X3SAPASSET |
| 27 | NA | Popolamento della tabella X3GENLVL3NOSAP |
| 28 | X3ETLPROCRESTGENLEV3EXTRAN | Caricamento di asset di livello 3 -> Attachi conduttore |
| 29 | X3ETLPROCRESTGENLEV3EXTRAN | Caricamento di asset di livello 3 -> Telaio |
| 30 | X3ETLPROCRESTFIXEDX3SAPASSETEXTRAN | Caricamento di asset di livello 3 -> Cavi |
| 31 | X3ETLPROCRESTSAPASSETMDFEXTRAN | Caricamento livello 3 -> Cavi Fittizi in X3SAPASSET |
| 32 | X3ETLPROCRESTFIXEDX3SAPASSETEXTRAN | Caricamento di asset di livello 3 -> Giunti |
| 33 | X3ETLPROCRESTFIXEDX3SAPASSETEXTRAN | Caricamento di asset di livello 3 -> Terminale |
| 34 | NA | Popolamento della tabella X3GENLVL4NOSAPISOLATORI |
| 35 | X3ETLPROCRESTSAPLEV4ISOLATOREEXTRAN | Caricamento di asset di livello 4 -> Isolatori |
| 36 | X3ETLPROCRESTSAPASSETMDFEXTRAN | Caricamento Morseteria in X3SAPASSET |
| 37 | NA | Popolamento della tabella X3GENLVL4NOSAP |
| 38 | X3ETLPROCRESTGENLEV4EXTRAN | Caricamento di asset di livello 4 -> Conduttore |
| 39 | X3ETLPROCRESTGENLEV4EXTRAN | Caricamento di asset di livello 4 -> Cavo di potenza |
| 40 | X3ETLPROCRESTGENLEV4EXTRAN | Caricamento di asset di livello 4 -> Giunto cavo |
| 41 | X3ETLPROCRESTGENLEV4EXTRAN | Caricamento di asset di livello 4 -> Terminale |
| 42 | X3ETLPROCRESTGENLEV4EXTRAN | Caricamento di asset di livello 4 -> Armamento conduttore |
| 43 | X3ETLPROCRESTGENLEV4EXTRAN | Caricamento di asset di livello 4 -> Struttura |
| 44 | NA | Popolamento della tabella X3GENLVL4FROMATT |
| 45 | X3ETLPROCRESTLEV4ATTEXTRAN | Caricamento degli attributi provenienti da SAP di livello 4: FIBRA OTTICA CONDUTTORE FIBRA OTTICA FUNE DI GUARDIA DESTRA FIBRA OTTICA FUNE DI GUARDIA SINISTRA FIBRA OTTICA FUNE DI GUARDIA TERZA FUNE DI GUARDIA DESTRA FUNE DI GUARDIA SINISTRA FUNE DI GUARDIA TERZA NIDO AVIFAUNA SEGNALAZIONE LUMINOSA OSTACOLO VOLO ANTIROTAZIONALI CONDUTTORE ANTIVIBRANTI CONDUTTORE DISSUASORI AVIFAUNA CONDUTTORE DISTANZIATORI DISTANZIATORI INTERFASE SFERE DI SEGNALAZION ANTIROTAZIONALI FUNE DI GUARDIA DISSUASORI AVIFAUNA FUNE DI GUARDIA SFERE DI SEGNALAZIONE FUNE DI GUARDIA GIUNTO CONDUTTORE BARRETTA PREFORMATA CONDUTTORE GIUNTO FUNE DI GUARDIA BARRETTA PREFORMATA FUNE DI GUARDIA DESTRA BARRETTA PREFORMATA FUNE DI GUARDIA SINISTRA BARRETTA PREFORMATA FUNE DI GUARDIA TERZA ARMAMENTO FUNE DI GUARDIA DESTRA ARMAMENTO FUNE DI GUARDIA SINISTRA ARMAMENTO FUNE DI GUARDIA TERZA MONITORAGGIO IOT (DIGIC) CAVI GIUNTO DI RIPARAZIONE CAMERA ISPEZIONABILE CASSETTA DI SEZIONAMENTO GIUNTI MONITORAGGIO IOT (DIGIC) GIUNTI MONITORAGGIO IOT (DIGIC) TERMINALI ISOLATORE FUNE DI GUARDIA ANTIVIBRANTI FUNE DI GUARDIA DESTRA ANTIVIBRANTI FUNE DI GUARDIA SINISTRA ANTIVIBRANTI FUNE DI GUARDIA TERZA DISPOSITIVO TLC GIUNZIONE FIBRA OTTICA SEZIONATORE |
| 46 | X3ETLPROCTRONCHIEXTRAN | Caricamento di Tronchi |
| 47 | X3ETLPROCSERVICEADDRESSEXTRAN | Generazione di Service Address |
| 48 | X3ETLPROCDOPPIATERNAEXTRAN | Generazione di ASSETLOCRELATION per i sostegni in doppia terna |
| 49 | X3ETLPROCDOPPIATERNAEXTRAN | Generazione di ASSETLOCRELATION per le campate in doppia terna |
| 50 | NA | Popolamento della tabella X3TESTX3ETL - Caricamento degli attributi sugli asset da SAP. Popolare - Caricamento degli attributi specifici di SAP: LIN_NUM_CATENE per 1039 Attributi provenienti dal padre. LIN_TIPO_COLLEGAMENTO per 1050 LIN_NUM_OGGETTI per 1018 |
| 51 | TESTX3ETL_REST_EXTRAN | Caricamento degli attributi sugli asset da SAP. Caricamento degli attributi specifici di SAP: LIN_NUM_CATENE per 1039 Attributi provenienti dal padre. LIN_TIPO_COLLEGAMENTO per 1050 LIN_NUM_OGGETTI per 1018 |

| 52 | NA | Popolamento della tabella X3HEREDADOS - Caricamento di attributi ereditati su asset non-SAP. Popolamento - Caricamento degli attributi del sito tecnico che non genera l'asset - ALN18 a Morsetteria. Popolamento - Caricamento di attributi specifici: LIN_RIF_ASSET_FDG per 1018, 1019 e 1021 LIN_RIF_FASE per 1009 e 1038 LIN_RIF_FUNE per 1022 e 1041 LIN_TRONCO_ISPEZIONE per 1005 e 1051 LIN_ELEMENTO_INDISP per 1003, 1005 e 1051 LIN_NUM_OGGETTI per 1018 DESTRA LIN_NUM_OGGETTI per 1018 SINISTRA LIN_NUM_OGGETTI per 1018 TERZA LIN_ANNO_INSTALLAZIONE per 1022 DESTRA LIN_ANNO_INSTALLAZIONE per 1022 SINISTRA LIN_ANNO_INSTALLAZIONE per 1022 TERZA LIN_TIPO_FUNE_GUARDIA per 1022 DESTRA LIN_TIPO_FUNE_GUARDIA per 1022 SINISTRA LIN_TIPO_FUNE_GUARDIA per 1022 TERZA LIN_ANNO_INSTALLAZIONE per 1041 DESTRA LIN_ANNO_INSTALLAZIONE per 1041 SINISTRA LIN_ANNO_INSTALLAZIONE per 1041 TERZA LIN_TIPO_ATTACCO per 1041 DESTRA LIN_TIPO_ATTACCO per 1041 SINISTRA LIN_TIPO_ATTACCO per 1041 TERZA LIN_TIPO_ATTACCO per 1038 DESTRA LIN_TIPO_ATTACCO per 1038 SINISTRA LIN_TIPO_ATTACCO per 1038 TERZA LIN_SIGLA_UNIFICAZIONE_FDG per 1041 DESTRA LIN_SIGLA_UNIFICAZIONE_FDG per 1041 SINISTRA LIN_SIGLA_UNIFICAZIONE_FDG per 1041 TERZA LIN_UNIFICAZIONE_FDG per 1041 DESTRA LIN_UNIFICAZIONE_FDG per 1041 SINISTRA LIN_UNIFICAZIONE_FDG per 1041 TERZA |
| --- | --- | --- |

| 53 | X3ETLHEREDADOS_REST_EXTRAN | Caricamento di attributi ereditati su asset non-SAP. Caricamento degli attributi del sito tecnico che non generano l'asset - ALN18 a Morsetteria. Caricamento di attributi specifici: LIN_RIF_ASSET_FDG per 1018, 1019 e 1021 LIN_RIF_FASE per 1009 e 1038 LIN_RIF_FUNE per 1022 e 1041 LIN_TRONCO_ISPEZIONE per 1005 e 1051 LIN_ELEMENTO_INDISP per 1003, 1005 e 1051 LIN_NUM_OGGETTI per 1018 DESTRA LIN_NUM_OGGETTI per 1018 SINISTRA LIN_NUM_OGGETTI per 1018 TERZA LIN_ANNO_INSTALLAZIONE per 1022 DESTRA LIN_ANNO_INSTALLAZIONE per 1022 SINISTRA LIN_ANNO_INSTALLAZIONE per 1022 TERZA LIN_TIPO_FUNE_GUARDIA per 1022 DESTRA LIN_TIPO_FUNE_GUARDIA per 1022 SINISTRA LIN_TIPO_FUNE_GUARDIA per 1022 TERZA LIN_ANNO_INSTALLAZIONE per 1041 DESTRA LIN_ANNO_INSTALLAZIONE per 1041 SINISTRA LIN_ANNO_INSTALLAZIONE per 1041 TERZA LIN_TIPO_ATTACCO per 1041 DESTRA LIN_TIPO_ATTACCO per 1041 SINISTRA LIN_TIPO_ATTACCO per 1041 TERZA LIN_TIPO_ATTACCO per 1038 DESTRA LIN_TIPO_ATTACCO per 1038 SINISTRA LIN_TIPO_ATTACCO per 1038 TERZA LIN_SIGLA_UNIFICAZIONE_FDG per 1041 DESTRA LIN_SIGLA_UNIFICAZIONE_FDG per 1041 SINISTRA LIN_SIGLA_UNIFICAZIONE_FDG per 1041 TERZA LIN_UNIFICAZIONE_FDG per 1041 DESTRA LIN_UNIFICAZIONE_FDG per 1041 SINISTRA LIN_UNIFICAZIONE_FDG per 1041 TERZA |
| --- | --- | --- |

Tabella 2: step workflow

Nella tabella 2 sono riportati gli script di automazione invocati nei differenti step. Lo stesso script di automazione sulla base dei parametri passati in input segue una logica di business differente. Ogni script ammette fino a 2 parametri input (parametro 1 e paramentro2). Nella tabella 3 sono riportati i differenti valori ammessi dal parametro 1, nella tabella 4 sono riportati i differenti valori ammessi dal parametro 2. Nella tabella 5 è riportata la sequenza d’invocazione degli script di automazione ed il valore dei parametri ammessi.

Il workflow ha una sequenza di lancio e dei parametri di lancio da rispettare.

Per caricare altri tipi di asset, è necessario utilizzare una diversa correlazione di script e parametri.

Parametro 1:

| Valori | Descrizione |
| --- | --- |
| X3CARATSAP_SOURCE | Copiare i dati in X3CARATSAP |
| X3SEDITECHSAP_SOURCE | Copiare i dati in X3SEDITECHSAP |
| X3SEDIINDISP_SOURCE | Copia dei dati in X3SEDIINDISP |
| X3GEWRK_SOURCE | Copiare i dati in X3GEWRK |
| PMTSTLIN | Linee |
| PMTSTLIL | Campate o Campate fittizi |
| PMTSTLIS | Sostegni o Sostegni fittizi |
| PMTSTLIZ | Tratta in cavo o Tratta in cavo fittizi |
| PMTSTLIT | Terminale cavo o Estremocavo |
| PMTSTLICG | Camera giunti o Estremocavo |
| PMTSTLIC | Conduttori o Conduttori fittizi |
| PMTSTLIG | Funi di guardia o Funi di guardia fittizi |
| ATTACHI | Attachi conduttore |
| TELAIO | Telaio |
| PMTSTLIV | Cavo o Cavo fittizi |
| PMTSTLII | Isolatori |
| PMTSTLIM | Morsetteria |
| CONDUTTORE | Conduttore |
| CAVODIPOTENZA | Cavo di Potenza |
| GIUNTOCAVO | Giunto cavo |
| TERMINALE | Terminale |
| ARMAMENTOCONDUTTORE | Armamento conduttore |
| STRUTTURA | Struttura |
| FIBRAOTTICACONDUTTORE | Fibra ottica conduttore |
| FIBRAOTTICAFUNEDIGUARDIADESTRA | Fibra ottica fune di guardia destra |
| FIBRAOTTICAFUNEDIGUARDIASINISTRA | Fibra ottica fune di guardia sinistra |
| FIBRAOTTICAFUNEDIGUARDIATERZA | Fibra ottica fune di guardia terza |
| FUNEDIGUARDIADESTRA | Fune di guardia destra |
| FUNEDIGUARDIASINISTRA | Fune di guardia sinistra |
| FUNEDIGUARDIATERZA | Fune di guardia terza |
| NIDOAVIFAUNA | Nido avifauna |
| SEGNALAZIONELUMINOSAOSTACOLOVOLO | Segnalazione luminosa ostacolo volo |
| ANTIROTAZIONALICONDUTTORE | Antirotazionali conduttore |
| ANTIVIBRANTICONDUTTORE | Antivibrati conduttore |
| DISSUASORIAVIFAUNACONDUTTORE | Dissuasori avifauna conduttore |
| DISTANZIATORI | Distanziatori |
| DISTANZIATORIINTERFASE | Distanziatori interfase |
| SFEREDISEGNALAZIONE | Sfere di segnalazione |
| ANTIROTAZIONALIFUNEDIGUARDIA | Antirotazionali fune di guardia |
| DISSUASORIAVIFAUNAFUNEDIGUARDIA | Dissuasori avifauna fune di guardia |
| SFEREDISEGNALAZIONEFUNEDIGUARDIA | Sfere di segnalazione fune di guardia |
| GIUNTOCONDUTTORE | Giunto conduttore |
| BARRETTAPREFORMATACONDUTTORE | Barretta preformata conduttore |
| GIUNTOFUNEDIGUARDIA | Giunto fune di guardia |
| BARRETTAPREFORMATAFGDESTRA | Barretta preformata fune di guardia destra |
| BARRETTAPREFORMATAFGSINISTRA | Barretta preformata fune di guardia sinistra |
| BARRETTAPREFORMATAFGTERZA | Barretta preformata fune di guardia terza |
| ARMAMENTOFUNEDIGUARDIADESTRA | Armamento fune di guardia destra |
| ARMAMENTOFUNEDIGUARDIASINISTRA | Armamento fune di guardia sinistra |
| ARMAMENTOFUNEDIGUARDIATERZA | Armamento fune di guardia terza |
| MONITORAGGIOIOTDIGICCAVI | Monitoraggio iot (digic) cavi |
| GIUNTODIRIPARAZIONE | Giunto di riparazione |
| CAMERAISPEZIONABILE | Camera ispezionabile |
| CASSETTADISEZIONAMENTOGIUNTI | Cassetta di sezionamento giunti |
| MONITORAGGIOIOTDIGICGIUNTI | Monitoraggio iot (digic) giunti |
| MONITORAGGIOIOTDIGICTERMINALI | Monitoraggio iot (digic) terminali |
| ISOLATOREFUNEDIGUARDIA | Isolatore fune di guardia |
| ANTIVIBRANTIFUNEDIGUARDIADESTRA | Antivibranti fune di guardia destra |
| ANTIVIBRANTIFUNEDIGUARDIASINISTRA | Antivibranti fune di guardia sinistra |
| ANTIVIBRANTIFUNEDIGUARDIATERZA | Antivibranti fune di guardia terza |
| DISPOSITIVOTLC | Dispositivo tlc |
| GIUNZIONEFIBRAOTTICA | Giunzione fibra ottica |
| SEZIONATORE | Sezionatore |
| PMTSTLIQ | Tronchi |
| PMTSTLIS | Generazione di ASSETLOCRELATION per i duplicati Sostegnos |
| PMTSTLIL | Generazione di ASSETLOCRELATION per i duplicati Campatas |

Tabella 3: Parametro 1

Parametro 2:

| Valori | Descrizione |
| --- | --- |
| X3CARATSAP_DEST | Copiare i dati in X3CARATSAP |
| X3SEDITECHSAP_DEST | Copiare i dati in X3SEDITECHSAP |
| X3SEDIINDISP_DEST | Copiare i dati in X3SEDIINDISP |
| X3GEWRK_DEST | Copiare i dati in X3GEWRK |
| ASSET | ASSET |
| SAPASSETMDF | SAPASSETMDF (Fittizi) |
| ESTREMOCAVO | Generating Estremocavo |
| LEVEL3 | LEVEL3 |
| LEVEL4 | LEVEL4 |
| L4ATTRIBUTES | Caricamento asset di livello 4 da attributi |
| TRONCHI | TRONCHI |
| SERVICEADDRESS | SERVICEADDRESS |
| DUPIA TERNA | Relazioni relative alla Doppia Terna |

Tabella 4: Parametro 2

Correlazione tra PARAMETRO 1 e PARAMETRO 2 con la loro relazione con la "Tabella 2: Esecuzione del processo di conversione" per lanciare il processo con il suo script corrispondente.

| PARAMETRO 1 | PARAMETRO 2 | PROCESS |
| --- | --- | --- |
| X3CARATSAP_SOURCE | X3CARATSAP_DEST | #1 |
| X3SEDITECHSAP_SOURCE | X3SEDITECHSAP_DEST | #2 |
| X3SEDIINDISP_SOURCE | X3SEDIINDISP_DEST | #3 |
| X3GEWRK_SOURCE | X3GEWRK_DEST | #4 |
| PMTSTLIN | ASSET | #13 |
| PMTSTLIL | ASSET | #14 |
| PMTSTLIL | SAPASSETMDF | #15 |
| PMTSTLIS | ASSET | #16 |
| PMTSTLIS | SAPASSETMDF | #17 |
| PMTSTLIZ | ASSET | #18 |
| PMTSTLIZ | SAPASSETMDF | #19 |
| PMTSTLIT | ESTREMOCAVO | #21 |
| PMTSTLICG | ESTREMOCAVO | #22 |
| PMTSTLIC | ASSET | #23 |
| PMTSTLIC | SAPASSETMDF | #24 |
| PMTSTLIG | ASSET | #25 |
| PMTSTLIG | SAPASSETMDF | #26 |
| ATTACHI | LEVEL3 | #28 |
| TELAIO | LEVEL3 | #29 |
| PMTSTLIV | ASSET | #30 |
| PMTSTLIV | SAPASSETMDF | #31 |
| PMTSTLICG | ASSET | #32 |
| PMTSTLIT | ASSET | #33 |
| PMTSTLII |  | #35 |
| PMTSTLIM | SAPASSETMDF | #36 |
| CONDUTTORE | LEVEL4 | #38 |
| CAVODIPOTENZA | LEVEL4 | #39 |
| GIUNTOCAVO | LEVEL4 | #40 |
| TERMINALE | LEVEL4 | #41 |
| ARMAMENTOCONDUTTORE | LEVEL4 | #42 |
| STRUTTURA | LEVEL4 | #43 |
| FIBRAOTTICACONDUTTORE | L4ATTRIBUTES | #45 |
| FIBRAOTTICAFUNEDIGUARDIADESTRA | L4ATTRIBUTES | #45 |
| FIBRAOTTICAFUNEDIGUARDIASINISTRA | L4ATTRIBUTES | #45 |
| FIBRAOTTICAFUNEDIGUARDIATERZA | L4ATTRIBUTES | #45 |
| FUNEDIGUARDIADESTRA | L4ATTRIBUTES | #45 |
| FUNEDIGUARDIASINISTRA | L4ATTRIBUTES | #45 |
| FUNEDIGUARDIATERZA | L4ATTRIBUTES | #45 |
| NIDOAVIFAUNA | L4ATTRIBUTES | #45 |
| SEGNALAZIONELUMINOSAOSTACOLOVOLO | L4ATTRIBUTES | #45 |
| ANTIROTAZIONALICONDUTTORE | L4ATTRIBUTES | #45 |
| ANTIVIBRANTICONDUTTORE | L4ATTRIBUTES | #45 |
| DISSUASORIAVIFAUNACONDUTTORE | L4ATTRIBUTES | #45 |
| DISTANZIATORI | L4ATTRIBUTES | #45 |
| DISTANZIATORIINTERFASE | L4ATTRIBUTES | #45 |
| SFEREDISEGNALAZIONE | L4ATTRIBUTES | #45 |
| ANTIROTAZIONALIFUNEDIGUARDIA | L4ATTRIBUTES | #45 |
| DISSUASORIAVIFAUNAFUNEDIGUARDIA | L4ATTRIBUTES | #45 |
| SFEREDISEGNALAZIONEFUNEDIGUARDIA | L4ATTRIBUTES | #45 |
| GIUNTOCONDUTTORE | L4ATTRIBUTES | #45 |
| BARRETTAPREFORMATACONDUTTORE | L4ATTRIBUTES | #45 |
| GIUNTOFUNEDIGUARDIA | L4ATTRIBUTES | #45 |
| BARRETTAPREFORMATAFGDESTRA | L4ATTRIBUTES | #45 |
| BARRETTAPREFORMATAFGSINISTRA | L4ATTRIBUTES | #45 |
| BARRETTAPREFORMATAFGTERZA | L4ATTRIBUTES | #45 |
| ARMAMENTOFUNEDIGUARDIADESTRA | L4ATTRIBUTES | #45 |
| ARMAMENTOFUNEDIGUARDIASINISTRA | L4ATTRIBUTES | #45 |
| ARMAMENTOFUNEDIGUARDIATERZA | L4ATTRIBUTES | #45 |
| MONITORAGGIOIOTDIGICCAVI | L4ATTRIBUTES | #45 |
| GIUNTODIRIPARAZIONE | L4ATTRIBUTES | #45 |
| CAMERAISPEZIONABILE | L4ATTRIBUTES | #45 |
| CASSETTADISEZIONAMENTOGIUNTI | L4ATTRIBUTES | #45 |
| MONITORAGGIOIOTDIGICGIUNTI | L4ATTRIBUTES | #45 |
| MONITORAGGIOIOTDIGICTERMINALI | L4ATTRIBUTES | #45 |
| ISOLATOREFUNEDIGUARDIA | L4ATTRIBUTES | #45 |
| ANTIVIBRANTIFUNEDIGUARDIADESTRA | L4ATTRIBUTES | #45 |
| ANTIVIBRANTIFUNEDIGUARDIASINISTRA | L4ATTRIBUTES | #45 |
| ANTIVIBRANTIFUNEDIGUARDIATERZA | L4ATTRIBUTES | #45 |
| DISPOSITIVOTLC | L4ATTRIBUTES | #45 |
| GIUNZIONEFIBRAOTTICA | L4ATTRIBUTES | #45 |
| SEZIONATORE | L4ATTRIBUTES | #45 |
| PMTSTLIQ | TRONCHI | #46 |
|  | SERVICEADDRESS | #47 |
| PMTSTLIS | DOPPIATERNA | #48 |
| PMTSTLIL | DOPPIATERNA | #49 |

Tabella 5: Elenco dei parametri con i rispettivi script

# Tabelle per il connettore

La configurazione delle Tabelle per tracciare la corrispondenza tra ID SAP ed ID Maximo è contenuta all’interno del documento ID 5 “EAM_LLD_GEN_ANA_Connettore_20230731_V1.4”.

# Documenti correlati

| ID | Descrizione |
| --- | --- |
| Numero di identificazione del documento | Descrizione del documento |
| 1 | Mapping Anagrafica EAM AM_Dati Linee rev.7 250723.LLD |
| 2 | Terna - SAP DB Connection to Oracle |
| 3 | EAM_HLD_ETL_ANA_Documento di ETL_20231025_V2.6 |
| 4 | REV 20230710 of attributi-EAM-SAP |
| 5 | EAM_LLD_GEN_ANA_Connettore_20230731_V1.4 |

# Indice figure

Figure 1 - Processo di caricamento degli asset SAP su EAM 6
