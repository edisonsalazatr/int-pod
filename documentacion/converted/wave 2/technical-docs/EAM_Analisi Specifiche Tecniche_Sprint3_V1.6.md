# EAM_Analisi Specifiche Tecniche_Sprint3_V1.6

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Specifica Tecnica – Sprint 3 |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data | Owner |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | ACN |  |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

# Glossario

| Termine | Definizione |
| --- | --- |
| Piano di Lavoro (PdL) | Oggetto Maximo utilizzato per modellare l’attività del catalogo |
| Ordine di Lavoro (OdL) | Oggetto Maximo utilizzato per modellare le richieste di lavoro da eseguire |
| Job Task (JT) | Metodi/micro-controlli associati a un PdL |
| Configurazione | Implementazione Standard sull’applicativo IBM Maximo |
| Sviluppo | Implementazione Custom sull’applicativo IBM Maximo |
| Configurazione Database | Applicazione che consente di creare, modificare e gestire tabelle, attributi, indici e relazioni all'interno del database di Maximo. Qui è possibile definire la struttura del database e configurare i dati in modo da adattarsi alle specifiche esigenze dell'organizzazione |
| Application Designer | Applicazione che consente di creare e personalizzare l'interfaccia grafica delle applicazioni presenti in Maximo. Gli utenti possono aggiungere tabelle, sezioni e campi per adattare l'aspetto e il comportamento delle applicazioni in base alle specifiche esigenze aziendali |
| Conditional Expression Manager | Applicazione che permette di creare e modificare espressioni in linguaggio SQL utili per gestire le regole di visibilità e/o editabilità di determinati oggetti all'interno di Maximo |
| Escalation | Le escalation in IBM Maximo vengono utilizzate per automatizzare attività e monitorare record sensibili al tempo. Quest’ultime possono essere attivate quando un record raggiunge un punto di escalation definito. I punti di escalation rappresentano le condizioni che devono essere soddisfatte |
| Script di Automazione | Applicazione che permette di creare script in diversi linguaggi di programmazione al fine di automatizzare processi, eseguire calcoli complessi e personalizzare il comportamento delle applicazioni |
| Relazione | Le relazioni consentono di stabilire collegamenti tra più tabelle al fine di recuperare specifici dati mediante l'utilizzo di clausole "WHERE" all'interno di un'istruzione SQL. Questa funzionalità permette di definire una tabella di destinazione, chiamata "Figlio", sulla quale viene eseguita la clausola "WHERE", mentre la tabella di partenza è chiamata "Padre" |
| Relazione.Attributo | Convenzione utilizzata in Application Designer per fare riferimento a un attributo specifico di una tabella nel database di Maximo relazionata a quella corrente |
| Sigoption | Le sigoption (opzione di firma) permettono di specificare i privilegi per l’utilizzo di applicazioni, opzioni di menu ed elementi della barra degli strumenti. Inoltre, esistono sigoption condizionali che consentono di concedere o revocare dinamicamente i privilegi di un utente in base a espressioni condizionali. |
| MX Loader | Foglio excel che consente di: Caricare dati in Maximo Scaricare determinati dati presenti nel DB Maximo, tramite l’impostazione di opportune query all’interno del file |
|  |  |

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche, già descritte ad alto livello nel documento “EAM_Analisi Funzionale_Sprint_3_v2.0.docx”, verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software.

Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni implementate sulla piattaforma EAM di Terna (ambiente Dev) e gli sviluppi custom da implementare. Ciascun paragrafo (dedicato a una specifica funzionalità nell’applicativo) presenta due sotto-paragrafi: nel primo sono illustrate le configurazioni eseguite sul software (e.g., estensione del modello dati, configurazione dell’applicazione, gestione della condizionalità, etc.), nel secondo gli sviluppi necessari per soddisfare le funzionalità richieste durante lo sprint.

In particolare, i temi inclusi nello Sprint 3 sono i seguenti:

Processo di esecuzione OdL

Processo di chiusura OdL

Recupero/alimentazione da fonti esterne (Geocall, Tagetik e SAP)

Esecuzione su Maximo Mobile

Gestione difformità/interferenze

Visualizzazione OdL nel Centro di Controllo

Reportistica.

# Sprint 3: Esecuzione OdL

## 4.1 Processo di esecuzione OdL

Il processo di esecuzione degli ordini di lavoro si differenzia in base al Titris associato all’OdL in fase di pianificazione; in particolare, è possibile distinguere le seguenti tre casistiche: Titris 100% UI, Titris 100% non UI e Titris misti.

### Configurazioni

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

Per permettere la gestione degli ordini di lavoro e i loro automatismi relativi ai cambi di stato, si prevedono le seguenti configurazioni a sistema:

Estensione del dominio degli stati dell’ordine di lavoro

Modifica ed estensione del dominio degli stati dell’ordine di lavoro (WOSTATUS) per l’inserimento dei seguenti stati: DA VALIDARE (OdL UI, non-UI e misto)

NON VALIDATO (OdL UI, non-UI e misto)

APERTO (OdL UI, non-UI e misto)

NON ESEGUITO (OdL UI, non-UI e misto)

SCHEDULABILE (OdL UI, e misto)

SCHEDULATO (OdL UI)

INOLTRATO (OdL UI)

ESEGUITO PAR (OdL UI, non-UI e misto)

AVVIATO (OdL UI)

ESEGUITO (OdL UI, non-UI e misto)

CHIUSO (OdL UI, non-UI e misto)

### Sviluppi

Per permettere il processo di esecuzione e chiusura degli OdL (Titris UI, non-UI e misti), si prevedono i seguenti sviluppi a sistema che permetteranno l’indirizzamento dell’ordine di lavoro all’interno del flusso di lavoro EAM:

Funzionalità di esecuzione e chiusura OdL sottostanti l’aggregatore (Titris UI)

DA VALIDARE: stato assunto quando l’OdL viene generato da DSS in caso di policy “on-condition”. Il passaggio dallo stato “DA VALIDARE” allo stato “APERTO” avviene o tramite l’area di validazione oppure tramite passaggio automatico di stato (allo scadere del tempo di max validazione).

APERTO: stato assunto quando l’OdL viene generato manualmente o validato dall’utente (passaggio di stato DA VALIDARE – APERTO); in questo stato è possibile procedere con la pianificazione economica e con quella tecnica (tramite ottimizzatore o manualmente)

SCHEDULABILE: stato assunto dagli OdL figli quando il padre passa in stato SCHEDULABILE; in caso di mancata creazione OdL in Geocall, tutti gli OdL in EAM torneranno in stato APERTO. In questo stato – mediante restrizione dei dati – l’ordine di lavoro non sarà modificabile dall’utente.

SCHEDULATO: stato assunto dagli OdL su input di Geocall; in questa fase si ricevono anche le date di schedulazione e le informazioni delle risorse.

INOLTRATO: stato assunto dagli OdL su input di Geocall; in questa fase gli ordini saranno visibili in EAM Mobile e sarà possibile esitare micro-controlli e rilevare difformità

AVVIATO: stato assunto dagli OdL su input di Geocall; da questa fase saranno ricevuti i consuntivi da Geocall e, quindi, si potranno inviare verso SAP

ESEGUITO PAR: stato assunto dagli OdL eseun micro-controllo ouna difformità (passaggio di stato INOLTRATO / AVVIATO – ESEGUITO PAR)

ESEGUITO: stato assunto dagli OdL a seguito di passaggio di stato manuale INOLTRATO / AVVIATO / ESEGUITO PAR – ESEGUITO

CHIUSO: stato assunto dagli OdL su input di Geocall

NON ESEGUITO: stato assunto dagli OdL su input di Geocall o quando la SYSDATE supera la data di massimo posticipo, ovvero FNLCONSTRAINT di WORKORDER (passaggio di stato APERTO – NON ESEGUITO)

NON VALIDATO: stato assunto dagli OdL a seguito di passaggio di stato manuale DA VALIDARE – NON VALIDATO.

Funzionalità di esecuzione e chiusura OdL aggregati e singoli (Titris UI)

DA VALIDARE: stato assunto quando l’OdL viene generato da DSS in caso di policy “on-condition” (solo OdL singoli)

APERTO: stato assunto quando l’OdL viene generato manualmente o validato dall’utente (passaggio di stato DA VALIDARE – APERTO); In questo stato è possibile procedere con la pianificazione economica e con quella tecnica (tramite ottimizzatore o manualmente)

SCHEDULABILE: stato assunto dagli OdL a seguito di passaggio di stato manuale APERTO – SCHEDULABILE. In questo stato – mediante restrizione dei dati – l’ordine di lavoro non sarà modificabile dall’utente. Per ripristinare lo stato in “APERTO” sarà possibile eseguire l’azione di “Ripianificazione ODL” dettagliata nei paragrafi successivi.

SCHEDULATO: stato assunto dagli OdL aggregatori al passaggio di stato di almeno un figlio in SCHEDULATO; stato assunto dagli OdL singoli su input di Geocall

: stato assunto dagli OdL aggregatori al passaggio di stato di almeno un figlio in INOLTRATO; stato assunto dagli OdL singoli su input di Geocall. In questa fase gli ordini saranno visibili in EAM Mobile e sarà possibile esitare micro-controlli e rilevare difformità

AVVIATO: stato assunto dagli OdL aggregatori al passaggio di stato di almeno un figlio in AVVIATO; stato assunto dagli OdL singoli su input di Geocall. Da questa fase saranno ricevuti i consuntivi da Geocall e, quindi, si potranno inviare verso SAP

ESEGUITO PAR: stato assunto dagli OdL aggregatori al passaggio di stato di almeno un figlio in ESEGUITO PAR, ESEGUITO o NON ESEGUITO; da questo stato sarà possibile registrare i fisici parziali degli OdL sottostanti. Stato assunto dagli OdL singoli quando viene eseguito un micro-controllo o inserita una difformità (passaggio di stato INOLTRATO / AVVIATO – ESEGUITO PAR); da questo stato sarà possibile validare le difformità rilevate

ESEGUITO: stato assunto dagli OdL aggregatori se tutti i figli presentano lo stato ESEGUITO NON ESEGUITO; stato OdL singoli se l’operazione corrispondente è in stato CHIUSO con esito positivo. In questo stato si potrà registrare il fisico totale dell’OdL.

CHIUSO: stato assunto dagli OdL su input di Geocall, se sono stati inviati a SAP tutti i consuntivi orari ele difformità registrate Inoltre, nel casosia necessario il processo di chiusura a tre punte, dovranno essere presenti anche la conferma dell’aggiornamento dell’anagrafica, il check patrimoniale EE e la conferma del caricamento degli allegati

NON ESEGUITO: stato assunto dagli OdL nel caso venga generato un ordine prevalente al corrente o quando la SYSDATE supera la data di massimo posticipo, ovvero FNLCONSTRAINT di WORKORDER (passaggio di stato APERTO – NON ESEGUITO); inoltre, per gli OdL singoli sarà possibile anche il passaggio di stato manuale

NON VALIDATO: stato assunto dagli OdL a seguito di passaggio di stato manuale DA VALIDARE – NON VALIDATO.

Funzionalità di esecuzione e chiusura OdL (Titris non-UI)

DA VALIDARE: stato assunto quando l’OdL viene generato da DSS in caso di policy “on-condition”

APERTO: stato assunto quando l’OdL viene generato manualmente o validato dall’utente (passaggio di stato DA VALIDARE – APERTO); in questo stato è possibile procedere con la pianificazione economica e con quella tecnica (tramite ottimizzatore o manualmente)

stato assunto dagli OdL a seguito di passaggio di stato manuale APERTO – . In questo stato è possibile procedere con la pianificazione economica e con quella tecnica (tramite ottimizzatore o manualmente)

ESEGUITO PAR: stato assunto dagli OdL aggregatori al passaggio di stato di almeno un figlio in ESEGUITO PAR o ESEGUITO; da questo stato sarà possibile registrare i fisici parziali degli OdL sottostanti. Stato assunto dagli OdL singoli quando viene inserito almeno un consuntivo (passaggio di stato – ESEGUITO PAR); Il passaggio di stato del figlio sarà completabile tramite interfaccia grafica selezionando l’OdL figlio. Da questo stato sarà possibile inviare a SAP i consuntivi parziali

ESEGUITO: stato assunto dagli OdL aggregatori quando tutti i figli presentano lo stato ESEGUITO; stato assunto dagli OdL singoli a seguito di passaggio di stato manuale ESEGUITO PAR – ESEGUITO. Da questo momento viene conteggiato il fisico totale

CHIUSO: stato assunto dagli OdL a seguito di passaggio di stato manuale ESEGUITO – CHIUSO, dopo l’invio a SAP di tutti i consuntivi orari, la conferma dell’aggiornamento anagrafica e il caricamento degli allegati (per il processo a 3 punte)

NON ESEGUITO: stato assunto dagli OdL quando la SYSDATE supera la data di massimo posticipo, ovvero FNLCONSTRAINT di WORKORDER (passaggio di stato APERTO – NON ESEGUITO)

NON VALIDATO: stato assunto dagli OdL a seguito di passaggio di stato manuale DA VALIDARE – NON VALIDATO.

Funzionalità di esecuzione e chiusura OdL (Titris misto)

DA VALIDARE: stato assunto quando l’OdL viene generato da DSS in caso di policy “on-condition”

APERTO: stato assunto quando l’OdL viene generato manualmente o validato dall’utente (passaggio di stato DA VALIDARE – APERTO); in questo stato è possibile procedere con la pianificazione economica e con quella tecnica (tramite ottimizzatore o manualmente)

SCHEDULABILE stato assunto dagli OdL a seguito di passaggio di stato manuale APERTO – SCHEDULABILE; da questo momento l’OdL viene inviato a Geocall con le informazioni dei Titris UI.

ESEGUITO PAR: stato assunto dagli OdL all’inserimento del primo consuntivo relativo ai Titris non-UI di EAM o su input di Geocall; da questo stato sarà possibile inviare a SAP i consuntivi dei Titris UI

ESEGUITO: stato assunto dagli OdL a seguito di passaggio di stato manuale ESEGUITO PAR – ESEGUITO

CHIUSO: stato assunto dagli OdL dopo l’invio a SAP di tutti i consuntivi orari, la ricezione del cambio stato da Geocall, la conferma dell’aggiornamento anagrafica e il caricamento degli allegati (per il processo a 3 punte)

NON ESEGUITO: stato assunto dagli OdL quando la SYSDATE supera la data di massimo posticipo, ovvero FNLCONSTRAINT di WORKORDER (passaggio di stato APERTO – NON ESEGUITO)

NON VALIDATO: stato assunto dagli OdL a seguito di passaggio di stato manuale DA VALIDARE – NON VALIDATO.

Le funzionalità sopra descritte saranno riportate all’interno di entità workflow dedicate che inoltreranno l’entità di WORKORDER all’interno del flusso che governerà le assegnazioni agli utenti e le possibili azioni determinate ed esplicate all’interno dei singoli step precedentemente discussi.

L

Per permettere la funzionalità di ripianificazione OdL (Titris UI), si prevede il seguente sviluppo a sistema:

Funzionalità di ripianificazione OdL aggregati e singoli (Titris UI)

Per poter permettere di abilitare la funzione di ripianificazione, viene inserita un’apposita funzione nel menù delle azioni aggiuntive dell’OdL denominata “Ripianifica OdL”. Questa funzionalità, eseguibile fino allo stato SCHEDULATO, cambierà lo stato dell’OdL in APERTO. Verrà, parallelamente, anche evocato Geocall (flusso di integrazione “Annulla OdL”) per sincronizzare i due sistemi.

Funzionalità di ripianificazione OdL (Titris non-UI)

Per poter permettere di abilitare la funzione di ripianificazione, viene inserita un’apposita funzione nel menù delle azioni aggiuntive dell’OdL denominata “Ripianifica OdL”. Questa funzionalità, eseguibile fino allo stato SCHEDULABILE, cambierà lo stato dell’OdL in APERTO.

…

## Processo di chiusura OdL

Il processo di chiusura “a tre punte” è caratterizzato dai seguenti passaggi chiave: il caricamento dei moduli FVI, M04 e M06, l’aggiornamento dei dati anagrafici e l’invio completo dei consuntivi verso SAP.

### Configurazioni

Per la gestione dei dati inerenti alla chiusura del processo “a tre punte” è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito viene riportato in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| WORKORDER | WONUM | Ordine di lavoro | UPPER | 25 |
| WORKORDER | CHANGEBY | Matricola | UPPER | 100 |
| WORKORDER | X4MODULICAR | Moduli caricati | YORN |  |
| WORKORDER | X4SAPCONSOK | Conferma Ore da SAP | YORN |  |
| WORKORDER | X4ANAGRAFICAUPDATE | Anagrafica Aggiornata | YORN |  |

Per permettere la visione dei campi necessari alla conferma del processo “a tre punte”, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “WOTRACK_D” come duplicato dell’applicazione standard “WOTRACK”, si dovranno configurare i campi del tab “Conferme per chiusura”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Ordine di lavoro | WONUM |  |  |  |
| Matricola | CHANGEBY |  |  |  |
| Moduli caricati? | X4MODULICAR |  |  |  |
| Conferma ore da SAP | X4SAPCONSOK |  |  |  |
| Anagrafica aggiornata? | X4ANAGRAFICAUPDATE |  |  |  |
| Allegati |  |  |  |  |

### Sviluppi

Per permettere la gestione del processo “a tre punte”, si prevede il seguente sviluppo a sistema:

Flag “Conferma ore da SAP” nel processo “a tre punte”

Il sistema imposterà automaticamente il flag X4SAPCONSOK ad 1 nel caso in cui siano stati inoltrati a SAP i consuntivi del WONUM corrente. Nel dettaglio un automation script verrà triggerato ogni qualvolta verrà impostato ad 1 il flag “X4CHECKSAP” della tabella LABTRANS (che viene impostato automaticamente dal sistema al seguito della ricezione di OK di invio consuntivo). Lo script si occuperà di verificare che tutti i consuntivi caricati sull’ordine di lavoro collegato al consuntivo validato (tutte le righe di LABTRANS dove REFWO=:REFWO and SITEID=:SITEID) abbiano il flag impostato ad 1; se tale condizione risulterà vera allora imposterà il flag X4SAPCONSOK ad uno anche per l’ordine di lavoro (dove l’ordine di lavoro verrà cercato come record della tabella WORKORDER con WORKORDER.WONUM=LABTRANS.REFWO and SITEID=SITEID)

## Recupero/alimentazione da fonti esterne

### Configurazioni Maximo

Per la gestione dei dati inerenti al recupero/alimentazione da fonti esterne è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito viene riportato in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| LABTRANS | LABORCODE | Risorse | UPPER | 8 |
| LABTRANS | WONUM | Ordine di lavoro | UPPER | 10 |
| LABTRANS | STARTDATE | Data di inizio | DATE |  |
| LABTRANS | REGULARHRS | Ore totali | DURATION |  |
|  |  |  |  |  |
| LABTRANS | GENAPPRSERVRECEIPT | Inviato a SAP? | YORN |  |
| LABTRANS | X4CHECKSAP | Esito SAP | UPPER | 10 |
| LABTRANS | X4ERRSAP | Errore SAP | ALN | 200 |
| LABTRANS | X4ESITOSAPDESC | Descrizione Errore SAP | ALN | 2000 |
| LABTRANS | STARTTIME | Ora di inizio | TIME |  |
| LABTRANS | FINISHTIME | Ora di fine | TIME |  |
| LABTRANS | X4ORETRASF | Ore trasferimento | DURATION |  |
| LABTRANS | X4OREUFF | Ore in ufficio | DURATION |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| X4ESITOINT | DESC_ESITO | Descrizione Esito | ALN | 2000 |
| X4ESITOINT | DEST | Destinazione | ALN | 50 |
| X4ESITOINT | END_ELAB | Fine Elaborazione | DATETIME |  |
| X4ESITOINT | ESITO | Esito | ALN | 10 |
| X4ESITOINT | IDINTERFACCIA | ID Interfaccia | ALN | 50 |
| X4ESITOINT | PARENT | Parent | INT |  |
| X4ESITOINT | SOURCE | Partenza | ALN | 50 |
| X4ESITOINT | START_ELAB | Inizio Elaborazione | DATETIME |  |
| X4ESITOINT | UNIQUEID | ID Univoco | ALN | 50 |
| X4ESITOINT | X4ESITOINTID | X4esitointid | BIGINT |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

Per permettere la visione dei campi necessari all’invio massivo OdL a schedulazione, si dovrà prevedere la seguente configurazione a sistema. In particolare, è stata creata la dialog “OdL da inviare a Geocall” nell’applicazione “WOTRACK_D”, riportante i seguenti campi in formato tabellare:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Ordine di lavoro | X4ODLGEO.WONUM |  |  |  |
| Descrizione | X4ODLGEO.DESCRIPTION |  |  |  |

Modifica dei campi inviati a Geocall

Se l’OdL si trova in stato SCHEDULABILE o SCHEDULATO risultano modificabili solo i campi relativi al Tipo spesa (X4TIPOSPESA) . I campi modificati saranno inviati automaticamente a Geocall. Per tutti gli stati dell’OdL successivi allo stato SCHEDULATO non sono ammesse modifiche ai campi.

Per permettere la visione dei campi necessari alla consuntivazione delle risorse, si dovrà prevedere la seguente configurazione a sistema. In particolare, si è configurata la tabella a vista “Consuntivazione risorse” dell’applicazione Standard “LABREP” aggiungendo i seguenti attributi:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
|  | LABORCODE |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Ordine di lavoro |  |  |  |  |
| Data di inizio | STARTDATE |  |  |  |
| Ore totali |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Inviato a SAP? | GENAPPRSERVRECEIPT |  |  |  |
| Esito SAP | X4CHECKSAP |  |  |  |
| Errore SAP | X4ERRSAP |  |  |  |
|  |  |  |  |  |

Configurazione “Consuntivazione risorse”

L’applicazione “Consuntivazione risorse” vengono configurate per permettere la visualizzazione rapida delle risorse appartenenti alla propria UdB la cui consuntivazione deve essere ancora inviata a SAP (GENAPPRSERVRECEIPT della tabella LABTRANS pari a 0). La vista generale su tutte le risorse di competenza si rende disponibile cliccando su “Query disponibili” e successivamente su “Tutte le risorse”.

Per permettere di visualizzare soltanto i record sopra descritti viene impostata una query di default: GENAPPRSERVRECEIPT=0 and refwo in (select wonum from workorder where siteid=:siteid and ownergroup in (select persongroup from persongroupteam where respparty=:&PERSONID&))

Per permettere la visione dei campi necessari alla consuntivazione delle risorse a seguito dell’invio verso SAP, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “WOTRACK_D” come duplicato dell’applicazione standard “WOTRACK”, si è configurata la tabella “Risorse” del tab “Avanzamento” aggiungendo i seguenti attributi:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
|  | LABORCODE |  | LABORCRAFTRATE |  |
| Nome | LABOR.PERSON.DISPLAYNAME |  |  |  |
| Inviato a SAP? | GENAPPRSERVRECEIPT |  |  |  |
| Esito SAP | X4CHECKSAP |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Data inizio | STARTDATE |  |  |  |
| Ora di inizio | STARTTIME |  |  |  |
| Ora di fine | FINISHTIME |  |  |  |
| Ore esecuzione | REGULARHRS |  |  |  |
| Ore trasferimento | X4ORETRASF |  |  |  |
| Ore in ufficio | X4OREUFF |  |  |  |
|  |  |  |  |  |

Per permettere la visione dei campi necessari alla intersezione tecnica patrimoniale pianificata, si dovrà prevedere la seguente configurazione a sistema. In particolare, si è configurata la tabella a vista X4TECPATR, aggiungendo i seguenti attributi nel tab “Pianificata”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| CBS |  |  |  |  |
| Indicatore |  |  |  |  |
| Unità di misura |  |  |  |  |
| Importo |  |  |  |  |
| Mese |  |  |  |  |
| Anno |  |  |  |  |

Per permettere la visione dei campi necessari alla intersezione tecnica patrimoniale consuntivata, si dovrà prevedere la seguente configurazione a sistema. In particolare, si è configurata la tabella a vista X4TECPATR, aggiungendo i seguenti attributi nel tab “Consuntivata”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| CBS |  |  |  |  |
| Indicatore |  |  |  |  |
| Unità di misura |  |  |  |  |
| Importo |  |  |  |  |
| Mese |  |  |  |  |
| Anno |  |  |  |  |

### Sviluppi Maximo embedded

Per permettere l’invio di OdL a schedulazione verso Geocall, si prevedono i seguenti sviluppi a sistema:

Funzionalità di invio puntuale OdL a schedulazione verso Geocall

L’invio puntuale dell’OdL a schedulazione verso Geocall avviene automaticamente a seguito del passaggio di stato manuale dello stesso in STATUS uguale a SCHEDULABILE. Il passaggio di stato dell’OdL in SCHEDULABILE è possibile solo se i seguenti campi risultano valorizzati: Codice OdL (WONUM), Codice PdL associato (JPNUM), ), Durata attività (ESTDUR), Descrizione OdL (DESCRIPTION), Data inizio pianificazione (TARGSTARTDATE), Data fine pianificazione (TARGCOMPDATE), Codice asset (ASSETNUM), Tipologia assistenza/sorveglianza(X4NATURALAVORO), Titris Risorse solo se Titris è UI ed Effort risorse (Tabella WPLABOR).

Funzionalità di invio massivo OdL a schedulazione verso Geocall

Viene creata una nuova dialog richiamabile dalle azioni laterali dell’applicazione “Monitoraggio OdL” denominata “Invio a schedulazione”. All’interno della dialog viene mostrato una tabella - mediante relazione X4ODLGEO – contenente tutti gli ordini di lavoro che possono assumere lo stato schedulabile, che non sono ancora stati inviati a Geocall e che hanno come PdL un record (da catalogo) gestito da UI. .

Sarà possibile flaggare gli ordini di lavoro che si dovranno inviare verso Geocall e, tramite apposito pulsante “Invia”, spedirli verso Geocall per la schedulazione. Di conseguenza, il sistema modifica automaticamente lo stato degli OdL in EAM in SCHEDULABILE.

Per permettere la funzionalità di invio consuntivi risorse verso SAP, si prevedono i seguenti sviluppi a sistema:

Funzionalità di invio consuntivi risorse verso SAP

Nell’applicazione di “Consuntivazione risorse” viene aggiunta la funzionalità nelle azioni laterali “Invio consuntivi a SAP” che predisporrà le righe di consuntivo selezionate per essere inviate verso SAP. Nel dettaglio, in prima battuta l’azione imposterà il flag di approvato della consuntivazione pari ad 1. Contestualmente, evocherà il servizio esposto da SAP di check periodo contabile e, in caso quest’ultimo abbia esito positivo, procederà con l’invio della consuntivazione verso SAP; in caso contrario, il sistema mostrerà un messaggio di errore all’utente “Invio consuntivazione non riuscito”.

Esito SAP nel processo di consuntivazione risorse

Il sistema, a seguito dell’invio del consuntivo verso SAP, mostrerà all’utente – nell’applicazione di monitoraggio OdL – lo stato della consuntivazione in SAP mediante apposito campo X4CHECKSAP della tabella LABTRANS. Questo sarà pari ad OK oppure KO. Nel caso in cui l’esito sia KO, il campo X4ESITOSAPDESC riporterà anche la descrizione dell’errore ricevuto da SAP.

Per permettere l’auto-compilazione del campo “Or” nel tab “Avanzamento”, si prevede il seguente sviluppo a sistema:

Auto-compilazione campo “”

Il campo della tabella LABTRANS del tab “Avanzamento” deve essere calcolato automaticamente dal sistema come segue: REGULARHRS + X4ORETRASF + X4OREUFF. Di conseguenza viene reso necessario l’avvio automatico di uno script di automazione che, al momento in cui si effettua il salvataggio, va a sommare le durate registrate nei campi di tipo durata prima citati e il risultante lo imposta nel campo ().

Caricamento massivo dei consuntivi

Al fine di semplificare l’attività di caricamento dei consuntivi per gli ordini di lavoro del processo “non-UI” o “misto”, viene previsto un inserimento massivo in EAM attraverso MX Loader, un foglio di lavoro Excel preconfigurato per permettere il caricamento massivo delle consuntivazioni in EAM. I dati necessari all’interno dell’Excel che l’utente dovrà popolare per effettuare il caricamento sono: identificativo dell’ordine di lavoro, matricola della risorsa, data di inizio consuntivo, durate dell’intervento.

Per permettere la funzionalità di popolamento della tabella di intersezione tecnico-patrimoniale verso SAP, si prevede il seguente sviluppo a sistema:

Popolamento tabella di intersezione tecnico-patrimoniale pianificata

Popolamento tabella di intersezione tecnico-patrimoniale consuntivata

Per permettere la funzionalità di storno OdL parziale e totale (Titris UI, non-UI e misti), si prevedono i seguenti sviluppi a sistema:

Funzionalità di storno OdL parziale (Titris UI, non-UI e misti)

Per eseguire lo storno OdL parziale devono essere rispettate le seguenti condizioni: consuntivo già validato e X4TIPOSPESA = OPEX; in caso contrario, il sistema mostrerà il messaggio di errore “Impossibile effettuare storno parziale, verificare che siano presenti consuntivi già validati e che la tipologia spesa sia OPEX”.

La funzionalità di storno parziale è attivabile dall’utente tramite l’omonimo pulsante nella tabella Labor del tab “Avanzamento” di “Monitoraggio OdL”; di conseguenza, mediante script di automazione, le ore della risorsa considerata vengono . Inoltre, nel caso in cui si dovesse stornare più del 70% dell’effort totale dell’OdL e il campo X4FISICO fosse diverso da NULL, il sistema mostrerà il warning “Attenzione, si sta stornando più del 70% del totale”.

Nel caso di storno l’automation script si occuperà quindi, di evocare il servizio esposto da SAP per la ricezione della richiesta di storno secondo le logiche descritte nel paragrafo dedicato; dopo la ricezione dell’OK allo storno procederà i consuntiv registrat dell (rig di LABTRANS dove REFWO=WONUM and SITEID=SITEID)

Funzionalità di storno OdL totale (Titris UI, non-UI e misti)

Per eseguire lo storno OdL totale devono essere rispettate le seguenti condizioni: lo STATUS dell’OdL deve essere diverso da e X4TIPOSPESA = OPEX; in caso contrario, il sistema mostrerà il messaggio di errore “Impossibile effettuare storno totale, verificare che la tipologia spesa sia OPEX ”.

La funzionalità di storno totale è attivabile dall’utente tramite l’omonimo pulsante nella sezione “More actions” di “Monitoraggio OdL”; di conseguenza, mediante script di automazione, le ore di tutte le risorse e il campo X4FISICO dell’OdL vengono azzerati e lo stato dell’OdL viene posto in APERTO. Nel caso di OdL aggregati (X4AGGREGATI = 1), lo storno totale dell’OdL padre avrà effetto anche sugli OdL figli se presenti (righe di WORKORDER dove PARENT=:WONUM and siteid=:SITEID)…

## Recupero/alimentazione da fonti esterne - GeoCall

### Sviluppi Maximo not embedded

Configurazioni Maximo

Per poter leggere e scrivere oggetti di Maximo, verranno elencati i Object Structure:

|  |  |
| --- | --- |

#### Flussi d’integrazioni

In questa sezione si prefigge di descrivere il flusso che intercorre tra EAM e il sistema GeoCall e le relative interazioni.

Di seguito diagramma di chiamate tra EAM, GeoCall, SAP ed Sisnet:

#### Logica d’attivazione e tipologia d’interazione

Il diagramma riportato in precedenza descrive le interazioni tra EAM e i vari sistemi.

EAM attraverso il connettore (linea rossa) invia a SAP gli elementi di rete adeguando le informazioni in funzione della gerarchia di SAP.

Su EAM gli Asset vengono identificati attraverso una chiave univoca chiamata AssetNum. Su Sap gli stessi elementi vengono identificati attraverso un codice univoco che si chiama SAP Id.

EAM detiene il master dell’associazione tra le 2 chiavi. Ogni volta che viene creato un elemento gerarchico di primo livello una linea (intesa come sede tecnica di primo livello) o una stazione (intesa come sede tecnica di primo livello) il connettore si fa carico di creare il correspettivo elemento su EAM.

Viceversa, ogni volta che su piattaforma EAM viene creato o modifica un elemento di rete, il connettore salva l’elemento su SAP ed eventualmente aggiorna il master delle associazioni.

Gli elementi di rete creati o modificati devono essere inviati al sistema GeoCall. A tale scopo GeoCall mette a disposizione due API di allineamento:

InvioTipoImpianto

ModificaAnagrafica

Il servizio InvioTipoImpianto ha lo scopo di inserire sulla piattaforma Geocall l’anagrafica dei tipi impianti

Il secondo servizio ModificaAnagrafica verrà invocata da EAM ogni qual volta un elemento di rete/Asset verrà creato o modificato sulla piattaforma EAM IBM Maximo attraverso la tecnologia di “Publish Channels” pubblicherà le informazioni necessarie dell’Asset su coda kafka usata dal pod eam-integration. Il listener in ascolto su coda kafka dovrà acquisire tutte le informazioni ed inviarle a GeoCall. È a cura di Sisnet veicolare i dati di geolocalizzazione ad EAM. EAM li invierà a GeoCall.

Il colloquio tra EAM e GeoCall non si limita solo allo scambio degli elementi di rete, ma anche alla gestione degli ordini di lavoro (OdL). EAM permette di collezionare in un unico punto centrale tutte le esigenze di manutenzione sugli impianti esistenti o relative modifiche e/o costruzione di nuovi impianti che coinvolgono il personale della Unità impiantistiche.

L’entità principale di Maximo è il workorder. Quest’ultimo verrà alimentato o dall’operatore attraverso Gui di Maximo o attraverso flussi automatici. Attraverso i Publish Channels, Maximo invierà ad eam-integration un OdL, il quale raccoglierà tutti i dati e verranno inviati GeoCall attraverso un API SOAP (InvioOdLVsGeocall)

L’ultima fase del processo di integrazione tra EAM e GeoCall consiste nella funzione di “e Bilanciamento” che sulla base della stima di durata, dell’impegno delle risorse assegnate a ciascuna attività del piano, si occuperà della delle stesse sulla base delle capacità produttive erogabili da ciascuna unità impiantistica.

Questa attività si traduce nella realizzazione di due nuove API SOAP di EAM:

Ricezione Schedulazione OdL (RicezioneSchedulazioneOdL)

Ricezione consuntivazione OdL tecnica/ore (notifyChiusuraOdL)

La prima API permette a GeoCall di notificare ad EAM i dati di schedulazione degli OdL e le risorse da impegnare. La seconda API permette a GeoCall di notificare ad EAM per ogni singolo OdL e risorse impegnate e le date di effettiva lavorazione e/o durata.

Di seguito lista dei flussi:

| Nome flusso | Tipologia d’integrazione | Tecnologia | Autenticazione |
| --- | --- | --- | --- |
| Invio Tipo Impianto | APIM | Soap/Xml | BasicAuth |
| Modifica Anagrafica | OSB | Soap/Xml | BasicAuth |
| Acquisizione geolocalizzazione SISNET | OSB | Soap/Xml | OAuth2 |
| ModificaAnagraficaMassiva | N.A. | CSV | N.A. |
| Flusso Invio OdL a Geocall | OSB | Soap/Xml | BasicAuth |
| Annullamento/Blocco Ordini Di Lavoro | OSB | Soap/Xml | BasicAuth |
| Annulla Operazione | OSB | Soap/Xml | BasicAuth |
| RicezioneSchedulazioneOdL | OSB | Soap/Xml | OAuth2 |
| Ricezione consuntivazioni OdL tecnica/ore | OSB | Soap/Xml | OAuth2 |
| Notifica Cambio Stato | APIM | Soap/Xml | OAuth2 |
| Storno | APIM | Soap/Xml | OAuth2 |

Per ogni flusso verranno dettagliate le logiche di business implementate.

#### Invio Tipo Impianto

Ogni qualvolta un elemento di tipo impianto viene aggiunto o rimosso da EAM bisogna allineare il sistema GeoCall. A tale scopo è stata definita una coda kafka che si chiama “eam-send-type-asset”. Come primo passo bisogna individuare tutti gli elementi di tipo linea o stazione da inviare a GeoCall. Tali informazioni sono salvate all’interno delle tabelle CLASSIFICATION e CLASSANCESTOR.

L’Object Structure dedicato per questa API è X4APICLASSSTRUCTURE.

Il formato del payload dell’oggetto ottenuto in input sulla coda kafka conterrà i seguenti campi:

{

" classstructureid": “1032”,

"description": " Componenti strutturali del terminale.",

"href": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/x4apiclassstructure/_MTAzMg--"

}

Una volta ottenuto il campo “classstructureid” dovremmo andare a verificare se l’elemento è di tipo Linea o Stazione sulla tabella CLASSANCESTOR. La query da eseguire è la seguente:

SELECT DECODE(C.ANCESTORCLASSID, 'LINEA', 'L', 'STAZIONE', 'S') AS TYPE, D a.CLASSSTRUCTUREID, A.CLASSIFICATIONID, A.DESCRIPTION DFROM CLASSSTRUCTURE A,

(SELECT max(HIERARCHYLEVELS) MAX_HIERARCHYLEVELS, CLASSSTRUCTUREID

FROM CLASSANCESTOR c GROUP BY CLASSSTRUCTUREID) B,

CLASSANCESTOR C

WHERE A.CLASSSTRUCTUREID = B.CLASSSTRUCTUREID

AND C.CLASSSTRUCTUREID = B.CLASSSTRUCTUREID

AND C.ANCESTORCLASSID IN ('STAZIONE', 'LINEA')

AND A.CLASSSTRUCTUREID = :classstructureid

Se la query da esito positivo è necessario individuare tutti gli elementi appartenenti alle linee e stazioni ed inviarle a GeoCall. La query da eseguire è la seguente:

SELECT DECODE(C.ANCESTORCLASSID, 'LINEA', 'L', 'STAZIONE', 'S') AS TYPE,

a.CLASSSTRUCTUREID, A.CLASSIFICATIONID, A.DESCRIPTION

FROM CLASSSTRUCTURE A,

(SELECT max(HIERARCHYLEVELS) MAX_HIERARCHYLEVELS, CLASSSTRUCTUREID

FROM CLASSANCESTOR c GROUP BY CLASSSTRUCTUREID) B,

CLASSANCESTOR C

WHERE A.CLASSSTRUCTUREID = B.CLASSSTRUCTUREID

AND C.CLASSSTRUCTUREID = B.CLASSSTRUCTUREID

AND C.ANCESTORCLASSID IN ('STAZIONE', 'LINEA')

La struttura SOAP messa a disposizione da GeoCall è la seguente:

| Campo interfaccia | Descrizione funzionale | Tipo | Obbligatorio | Note |
| --- | --- | --- | --- | --- |
| CODICE | Codice tipologia asset | String (40) | Y | Campo CLASSSTRUCTUREID della query precedente |
| DESCRIZIONE | Descrizione tipologia asset | String (80) | Y | Campo della query precedente. |
| CODICE_GRUPPO | Codice Gruppo | Long | Y | Campo univoco: usare la sequence SEQ_CODICE_GRUPPO |
| LAST_RECORD | Ultimo record | String | Y | Valori ammessi: - 0: per tutte le n-1 chiamate della medesima esportazione - 1: esclusivamente nell’ultima chiamata dell’esportazione. |
| TIPO_ANAGRAFICA_CLIENTE | Tipo Anagrafica | Long | Y | Viene valorizzato con ‘3’ per gli asset con descrizione “STAZIONI (F)" e "LINEE (F)”, tutti gli altri asset avranno come valore ‘2’. |
| FAMIGLIA | famiglia | String (20) | Y | Campo TYPE della query precedente |

Per ogni elemento in CLASSIFICATION bisogna fare una chiamata SOAP/Basic Authentification.

GeoCall per ogni chiamata può risponde con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore GeoCall fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In caso di KO, in funzione del CODICE_ERRORE si proverà a riconciliare l’operazione su GeoCall (rilanciare request).

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo verrà usato il business Object Structure X4ESITOINT.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "SEND_ASSET_TYPE_TO_GEOCALL",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "GEOCALL",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

#### Acquisizione geolocalizzazione SISNET

Ogni qualvolta cambiano le coordinate di geolocalizzazione su SISNET, bisogna allineare il sistema EAM. SISNET invocherà un servizio di EAM per allineare la sua banca dati.

L’API è esposta da EAM ha il seguente URI:

POST https://<host:port>/eam/ws/oauth2/ricezioneImpianti.wsdl

con SOAPAction = modificaImpianto/2.0.

Il formato della request è la seguente:

<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/" xmlns:SOAP-ENC="http://schemas.xmlsoap.org/soap/encoding/" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:xsd="http://www.w3.org/2001/XMLSchema">

<SOAP-ENV:Body>

<m:modificaImpianto xmlns:m="http://modificaimpianto.inbound.cxf.ternawfm.geocallapp.overit/2.0">

<modificaImpiantoRequest>

<tipoOggetto>L3COR</tipoOggetto>

<statoOggetto>A</statoOggetto>

<codiceOggetto>23863C1-357------G</codiceOggetto>

<codicePadre>23863C1-357-----</codicePadre>

<descrizione>CORDE DI GUARDIA</descrizione>

<areaLavorazione>BVL</areaLavorazione>

<centroDiLavoro>BVTT4</centroDiLavoro>

<filiale>BVTT4</filiale>

<indirizzo>

<toponimo>v.</toponimo>

<via>VIA ARECHI</via>

<civico>11</civico>

<localita>MILANO</localita>

<provincia>MI</provincia>

<cap>20120</cap>

<comune>MILANO</comune>

<istat>015146</istat>

<IDStradario xsi:nil="true"/>

<latitudine1>45.6612796755387</latitudine1>

<longitudine1>9.50001811147929</longitudine1>

<latitudine2>47</latitudine2>

<longitudine2>9.6</longitudine2>

</indirizzo>

</modificaImpiantoRequest>

</m:modificaImpianto>

</SOAP-ENV:Body>

</SOAP-ENV:Envelope>

Per prima cosa bisogna salvare in cache la gerarchia della Linea e della Stazione:

SELECT B.*

FROM(

SELECT C.CLASSSTRUCTUREID, C.DESCRIPTION, PARENT, CLASSIFICATIONID, CLASSSTRUCTUREUID, LEVEL LIVELLO

FROM CLASSSTRUCTURE C

START WITH CLASSIFICATIONID = 'STAZIONE'

CONNECT BY PRIOR CLASSSTRUCTUREID = PARENT) B

ORDER BY B.LIVELLO

SELECT B.*

FROM(

SELECT C.CLASSSTRUCTUREID, C.DESCRIPTION, PARENT, CLASSIFICATIONID, CLASSSTRUCTUREUID, LEVEL LIVELLO

FROM CLASSSTRUCTURE C

START WITH CLASSIFICATIONID = 'LINEA'

CONNECT BY PRIOR CLASSSTRUCTUREID = PARENT) B

ORDER BY B.LIVELLO

Nell’output della query sono presenti delle informazioni molto importanti:

CLASSSTRUCTUREID

LIVELLO

Queste due informazioni saranno molto utili nella parte successiva dell’algoritmo.

Tutte le informazioni inviate da SISNET verranno acquisite su una tabella che si chiama X4SISNETADDRESS. Il formato di questa tabella è riportato nella sezione di modello relazionale.

Usando il campo “codiceOggetto” bisogna controllare se l’elemento è presente nella tabella X4SISNETADDRESS. Se è stato individuato un record vengono aggiornate tutte le informazioni dello stradario e delle coordinate geografiche. In caso negativo viene inserito un nuovo record a DB. Impostare il campo DA_NOTIFICARE_GEOCALL = ‘Y’ della tabella X4SISNETADDRESS.

Le informazioni da salvare sono le seguenti:

codiceOggetto

descrizione

codicePadre

latitudine1

longitudine1

latitudine2

longitudine2

toponimo

via

civico

localita

provincia

cap

comune

istat

statoOggetto

tipoOggetto

areaLavorazione

centroDiLavoro

filiale

Successivamente si determinano le informazioni di gerarchia da popolare ed inviare a GeoCall:

SAPID

SAPID2

codicePadre

codicePadre2

radice

radice2

Come primo passo è determinare il tipo dell’elemento SAP inviato:

Se il valore di codiceOggetto inizia con 2 è Linea

Se il valore del codiceOggetto inizia con 3 è Stazione

Se il valore del codiceOggetto corrisponde ad un tronco (codiceOggetto con formato [7 digit alfanumerico]-T[2 digit numeriche]) è valorizzare DA_NOTIFICARE_GEOCALL = NULL e terminare esecuzione

Se il valore del codiceOggetto non inizia né con 2 e né con 3 è Errore

Si presentano diversi casi a seconda se sono Linea o Stazione.

Di seguito si analizzano i vari casi per linee. Prima di tutto bisogna controllare se ci sono problemi con il connettore e per tale scopo si esegue la seguente query:

SELECT A.STRNO, B.OBJECTNUM, B.HIERARCHYPATH FROM X3SAPASSET A, X3SAPTOMAXOBJECT B

WHERE A.STRNO = '< codiceOggetto >' AND A.X3SAPASSETID = B.SAPASSETID

AND B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

AND B.SITEID = 'SIT01'

Se la precedente query non fornisce risultato, impostare il campo DA_NOTIFICARE_GEOCALL = ‘Y’ nella tabella X4SISNETADDRESS e terminare l’elaborazione (incongruenza sul connettore, salvare informazione su X4ESITOINT).

Per determinare i dati dell’Asset è necessario analizzare 3 casistiche:

Elemento di 1° livello (lunghezza 7 del campo codiceOggetto). Eseguire la seguente logica:

SELECT ASS.ASSETNUM, ASS.PARENT, ASS.DESCRIPTION, ASS.X3ROOTASSETNUM,

ASS.CLASSSTRUCTUREID, ASS.ANCESTOR, ASS.X3SAPID,

ASS.X3GEWRK, ASS.X3INGRP, ASS.STATUS

FROM ASSET ASS

WHERE ASS.ASSETNUM IN (

SELECT B.OBJECTNUM FROM X3SAPASSET A, X3SAPTOMAXOBJECT B

WHERE A.STRNO = '< codiceOggetto >' AND A.X3SAPASSETID = B.SAPASSETID

AND B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

AND B.OBJECTNUM = ASS.ASSETNUM

AND B.SITEID = 'SIT01')

Se la precedente query non fornisce risultato, impostare il campo DA_NOTIFICARE_GEOCALL = ‘Y’ nella tabella X4SISNETADDRESS e terminare l’elaborazione (incongruenza sui dati, salvare informazione su X4ESITOINT).

Elemento di 2° livello (lunghezza 16 del campo codiceOggetto), di tipo CAMPATA o TRATTA CAVO. Eseguire la seguente logica:

Dalla precedente query controllare se nell’output del campo HIERARCHYPATH è presente una riga valorizzato o con 'LINEA\CAMPATA AEREA' o con 'LINEA\TRATTA CAVO'. In caso di esito positivo recuperiamo le informazioni dell’asset nel seguente modo:

SELECT ASS.ASSETNUM, ASS.PARENT, ASS.DESCRIPTION, ASS.X3ROOTASSETNUM,

ASS.CLASSSTRUCTUREID, ASS.ANCESTOR, ASS.X3SAPID,

ASS.X3GEWRK, ASS.X3INGRP, ASS.STATUS

FROM ASSET ASS

WHERE ASS.ASSETNUM IN (

SELECT B.OBJECTNUM FROM X3SAPASSET A, X3SAPTOMAXOBJECT B

WHERE A.STRNO = '< codiceOggetto >' AND A.X3SAPASSETID = B.SAPASSETID

AND B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

AND B.OBJECTNUM = ASS.ASSETNUM

AND (b.HIERARCHYPATH = 'LINEA\CAMPATA AEREA' OR b.HIERARCHYPATH = 'LINEA\TRATTA CAVO')

AND B.SITEID = 'SIT01')

Se la precedente query non fornisce risultato, impostare il campo DA_NOTIFICARE_GEOCALL = ‘Y’ nella tabella X4SISNETADDRESS e terminare l’elaborazione (incongruenza sui dati, salvare su X4ESITOINT).

Per tutte le altre tipologie eseguire la seguente query:

SELECT DISTINCT B.*

FROM (

SELECT ASS.ASSETNUM,

ASS.PARENT,

ASS.DESCRIPTION,

ASS.X3ROOTASSETNUM,

ASS.CLASSSTRUCTUREID,

ASS.ANCESTOR,

ASS.X3SAPID,

ASS.X3GEWRK,

COALESCE(ASS.X3INGRP, ANCESTOR_ASSET.X3INGRP) AS X3INGRP,

ASS.STATUS

FROM ASSET ASS

LEFT JOIN ASSET ANCESTOR_ASSET

ON ASS.ANCESTOR = ANCESTOR_ASSET.ASSETNUM

AND ASS.SITEID = ANCESTOR_ASSET.SITEID

WHERE ASS.SITEID = 'SIT01'

AND (

ASS.ASSETNUM IN (

SELECT OBJECTNUM

FROM X3SAPTOMAXOBJECT B, X3SAPASSET A

WHERE B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

AND A.STRNO = '< codiceOggetto >'

AND B.OBJECTNUM = ASS.ASSETNUM

AND B.SITEID = ASS.SITEID

AND A.X3SAPASSETID = B.SAPASSETID

)

OR ASS.ASSETNUM NOT IN (

SELECT OBJECTNUM

FROM X3SAPTOMAXOBJECT B, X3SAPASSET A

WHERE B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

AND A.X3SAPASSETID = B.SAPASSETID

AND B.SITEID = ASS.SITEID

AND B.OBJECTNUM = ASS.ASSETNUM

)

)

START WITH ASS.ASSETNUM IN (

SELECT OBJECTNUM

FROM X3SAPASSET A, X3SAPTOMAXOBJECT B

WHERE A.STRNO = '< codiceOggetto >'

AND A.X3SAPASSETID = B.SAPASSETID

AND B.SITEID = 'SIT01'

AND B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

)

CONNECT BY PRIOR ASS.ASSETNUM = ASS.PARENT

) B;

Se la precedente query non fornisce risultato, impostare il campo DA_NOTIFICARE_ GEOCALL = ‘Y’ nella tabella X4SISNETADDRESS e terminare l’elaborazione (incongruenza sui dati, salvare su X4ESITOINT).

Per le stazioni la logica d’applicare è la seguente:

SELECT

ASS.ASSETNUM,

ASS.PARENT,

ASS.DESCRIPTION,

ASS.X3ROOTASSETNUM,

ASS.CLASSSTRUCTUREID,

ASS.ANCESTOR,

NVL(ASS.X3SAPID, ASS.X3SAPIDEQ) AS X3SAPID,

ASS.X3GEWRK,

COALESCE(ASS.X3INGRP, ANCESTOR_ASSET.X3INGRP) AS X3INGRP,

ASS.STATUS

FROM ASSET ASS

LEFT JOIN ASSET ANCESTOR_ASSET

ON ASS.ANCESTOR = ANCESTOR_ASSET.ASSETNUM

AND ASS.SITEID = ANCESTOR_ASSET.SITEID

WHERE ASS.SITEID = 'SIT01'

AND NVL(ASS.X3SAPID, ASS.X3SAPIDEQ) = '< codiceOggetto >';

Di seguito verranno esposte le varie logiche per determinare i campi necessari a GeoCall in funzione della tipologia (Linee, Stazioni).

Linee:

Per tutti gli elementi della gerarchia possiamo valorizzare i seguenti elementi a prescindere:

codiceOggetto = < ASSETNUM >

descrizione = < DESCRIPTION >

codicePadre = < PARENT >

Se il campo PARENT è NULL allora l’elemento è un 1° o un 2° livello gerarchico. Pertanto, si applicano le seguenti logiche:

Se CLASSSTRUCTUREID è un 2° livello allora bisogna eseguire la seguente query:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE AL.TARGETASSETNUM='< ASSETNUM DELLA QUERY PRECEDENTE >'

A valle di questa query si esegue la seguente assegnazione:

codicePadre = < SOURCEASSETNUM prima riga>

radice = < SOURCEASSETNUM prima riga>

SAPID = < X3SAPID prima riga>

codicePadre2 = < SOURCEASSETNUM seconda riga, se esiste>

radice2 = < SOURCEASSETNUM seconda riga, se esiste>

SAPID2 = < X3SAPID seconda riga, se esiste>

Altrimenti (CLASSSTRUCTUREID è un 1° livello) allora

radice = NULL

radice2 = NULL

codicePadre2 = NULL

SAPID = < X3SAPID della query iniziale>

Se il campo PARENT è valorizzato e X3ROOTASSETNUM è valorizzato allora siamo in presenza di 3° livello. La logica da applicare è la seguente:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE TARGETASSETNUM='< PARENT >'

AND SOURCEASSETNUM = '< X3ROOTASSETNUM >'

e si valorizza i seguenti campi:

radice = X3ROOTASSETNUM

radice2 = NULL

codicePadre2 = NULL

SAPID = X3SAPID

SAPID2 = NULL

In tutte le altre casistiche si applicano le seguenti logiche:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE AL.TARGETASSETNUM='< ANCESTOR >'

e si valorizzano i campi nel seguente modo:

radice = < SOURCEASSETNUM prima riga>

SAPID = < X3SAPID >

radice2 = < SOURCEASSETNUM seconda riga, se presente>

SAPID2 = < X3SAPID seconda riga, se presente>

codicePadre2 = NULL

Stazioni:

Per tutti gli elementi della gerarchia si valorizzano i campi nel seguente modo:

codiceOggetto = < ASSETNUM>

SAPID = < X3SAPID/X3SAPIDEQ della query iniziale o prima SAPID in gerarchia>

SAPID2 = NULL

codicePadre = < PARENT >

radice = < ANCESTOR >

radice2 = NULL

Il campo X3SAPID può essere NULL, pertanto è necessario iterare sulla tabella ASSET usando il campo PARENT fino ad individuare un SAPID.

Tutte le informazioni individuate devono essere inviate a Geocall.

| Campo interfaccia | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| tipoOggetto | Classificazione dell'asset | Y | Campo CLASSSTRUCTUREID determinato. |
| statoOggetto | Stato dell'asset | N | Campo STATUS determinato |
| codiceOggetto | Codice identificativo dell'asset | Y | Campo ASSETNUM determinato |
| SAPID | Precedente codice identificativo dell'asset | Y | Valorizzare con <SAPID> |
| SAPID2 | Codice identificativo SAP per elementi in doppia terna | N | Valorizzare con <SAPID2> |
| descrizione | Descrizione dell’oggetto tecnico (sede tecnica o equipment) | N | Campo DESCRIPTION determinato |
| codicePadre | Codice identificativo del padre dell'asset Informazione relativa al padre dell’oggetto trasmesso, tale informazione è necessaria per la ricostruzione della struttura della consistenza impiantistica. | N | Valorizzare con <codicePadre> |
| radice | Informazione relativa alla radice dell’oggetto trasmesso. Necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | N | Valorizzare con <radice> |
| codicePadre2 | Informazione relativa al secondo padre dell’oggetto trasmesso, tale informazione è necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | N | Valorizzare con <codicePadre2> |
| codiceRadice2 | Informazione relativa al secondo padre dell’oggetto trasmesso, tale informazione è necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | N | Valorizzare con <radice2> |
| areaLavorazione | Unità impianti dell'asset. Informazione trasmessa da EAM ma che non verrà considerata da Geocall in fase di importazione. | N | Campo x3ingrp dell’oggetto ottenuto in input. |
| centroDiLavoro | Unità impianti dell'asset. Informazione trasmessa da EAM ma che non verrà considerata da Geocall in fase di importazione. | N | Campo x3gewrk dell’oggetto ottenuto in input. |
| filiale | Codice dell’Unità Impianti di riferimento (Centro Operativo in Geocall). Es. GVTT1. | Y | Campo x3ingrp dell’oggetto ottenuto in input. |
| Indirizzo | Salvato ma non utilizzato | N |  |
| toponimo | Salvato ma non utilizzato | N | Valorizzare da X4SISNETADDRESS |
| via | Salvato ma non utilizzato | N | Valorizzare da X4SISNETADDRESS |
| localita | Salvato ma non utilizzato | N | Valorizzare da X4SISNETADDRESS |
| provincia | Salvato ma non utilizzato | N | Valorizzare da X4SISNETADDRESS |
| cap | Salvato ma non utilizzato | N | Valorizzare da X4SISNETADDRESS |
| comune | Salvato ma non utilizzato | N | Valorizzare da X4SISNETADDRESS |
| istat | Salvato ma non utilizzato | N | Valorizzare da X4SISNETADDRESS |
| latitudine1 | Coordinata X dell’impianto. Utilizzato per la schedulazione e visualizzazione su mappa. Es. 37.443419931193 | N | Valorizzare da X4SISNETADDRESS |
| longitudine1 | Coordinata Y dell’impianto. Utilizzato per la schedulazione e visualizzazione su mappa. Es. 37.443419931193 | N | Valorizzare da X4SISNETADDRESS |
| latitudine2 | Solo significato informativo. Presenti nel caricamento da csv. | N | Valorizzare da X4SISNETADDRESS |
| longitudine2 | Solo significato informativo. Presenti nel caricamento da csv. | N | Valorizzare da X4SISNETADDRESS |

In caso di errore impostare DA_NOTIFICARE_GEOCALL = ‘Y’ nella tabella X4SISNETADDRESS.

Per tutti gli impianti non inviati a GeoCall (X4SISNETADDRESS.DA_NOTIFICARE_GEOCALL = Y) è necessario prevedere uno schedulatore che ogni 10 minuti verifica le condizioni sugli ASSET\Geocall e provvedere ad inviare a Geocall le sedi tecniche con le eventuali coordinate geografiche. Per ogni elemento inviato a Geocall impostare il DA_NOTIFICARE_GEOCALL = NULL.

Inoltre, è necessario prevedere la rinomina del SapId da GUI Maximo. Gli script jython salveranno le informazioni del vecchio e nuovo SapId nella tabella X4RENAMESAPIDGEO. L’allineamento da parte del connettore non sarà immediato. È necessario uno Schedulatore che prevede a controllare i dati all’interno delle tabelle X3SAPASSET, X3SAPTOMAXOBJECT per il nuovo SapId.

#### Modifica Anagrafica

Ogni qualvolta che l’anagrafica subisce una modifica o si ha una variazione del set informativo viene inviato un nuovo messaggio su coda kafka “eam-asset-modify”; Il listener in ascolto sulla stessa allineerà il sistema Geocall. La logica di allineamento dell’anagrafica a Geocall è manuale poiché attivata tramite pulsante dall’operatore su Maximo dopo aver concluso le attività su EAM.

Per l’elemento ottenuto bisogna effettuare una chiamata SOAP/Basic Authentication.

L’Object Structure dedicato per questa API è X4APIASSETADDRESS.

Il formato del payload dell’oggetto ottenuto in input sulla coda kafka conterrà i seguenti campi:

{

"parent": "1723257",

"x3ingrp": "UPM",

"status": "DATT",

"assetnum": "1723262",

"x3gewrk": "BVTT3_1",

"x3ingrp": "bvtt3",

"classstructureid": "1039",

}

Prima di poter inviare i dati a GeoCall è necessario individuare i dati dello stradario e le coordinate geografiche. Queste informazioni sono memorizzate nella tabella X4SISNETADDRESS. Le logiche per poter acquisire le informazioni delle coordinate si differenziano a seconda se sono Linea o Stazione. Per determinare la tipologia eseguire la seguente query:

SELECT DECODE(C.ANCESTORCLASSID, 'LINEA', 'L', 'STAZIONE', 'S') AS TYPE,

a.CLASSSTRUCTUREID, A.CLASSIFICATIONID, A.DESCRIPTION

FROM CLASSSTRUCTURE A, (SELECT max(HIERARCHYLEVELS) MAX_HIERARCHYLEVELS, CLASSSTRUCTUREID

FROM CLASSANCESTOR c GROUP BY CLASSSTRUCTUREID) B,

CLASSANCESTOR C

WHERE A.CLASSSTRUCTUREID = B.CLASSSTRUCTUREID

AND C.CLASSSTRUCTUREID = B.CLASSSTRUCTUREID

AND C.ANCESTORCLASSID IN ('STAZIONE', 'LINEA')

AND A.CLASSSTRUCTUREID = <classstructureid>

Di seguito si analizzano le logiche per determinare il SAPID con cui accedere alla tabella X4SISNETADDRESS per gli elementi di tipo Linea:

Se i-CLASSSTRUCTUREID è 1° livello eseguire la

SELECT B.OBJECTNUM, A.STRNO AS SAP_FIND

FROM X3SAPASSET A, X3SAPTOMAXOBJECT B

WHERE B.OBJECTNUM = '<assetnum>'

AND A.X3SAPASSETID = B.SAPASSETID

AND B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

AND B.SITEID = 'SIT01'

Se i-CLASSSTRUCTUREID è 2° livello e di tipo CAMPATA, TRATTA CAVO eseguire le seguenti logiche:

SELECT B.OBJECTNUM, A.STRNO AS SAP_FIND

FROM X3SAPASSET A, X3SAPTOMAXOBJECT B

WHERE B.OBJECTNUM = '<assetnum>'

AND A.X3SAPASSETID = B.SAPASSETID

AND B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

AND B.SITEID = 'SIT01'

AND (b.HIERARCHYPATH = 'LINEA\CAMPATA AEREA' OR b.HIERARCHYPATH = 'LINEA\TRATTA CAVO')

AND ROWNUM = 1

Per tutte le altre tipologie eseguire la seguente query:

SELECT B.OBJECTNUM, A.STRNO AS SAP_FIND

FROM X3SAPASSET A, X3SAPTOMAXOBJECT B

WHERE B.OBJECTNUM = '<assetnum>'

AND A.X3SAPASSETID = B.SAPASSETID

AND B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

AND B.SITEID = 'SIT01'

AND ROWNUM = 1

Di seguito si analizzano le logiche per determinare il SAPID con cui accedere alla tabella X4SISNETADDRESS per gli elementi di tipo Stazioni.

È necessario eseguire la seguente query:

SELECT NVL(X3SAPID, X3SAPIDEQ) AS SAP_FIND, PARENT

FROM ASSET

WHERE ASSETNUM = '<assetnum>'

Nel caso in cui il campo SAP_FIND è NULL si itera sulla gerarchia usando il campo PARENT.

Successivamente si determinano gli elementi di gerarchia per GeoCall:

SAPID

SAPID2

codicePadre

codicePadre2

radice

radice2

Di seguito verranno esposte le varie logiche in funzione della tipologia (Linee, Stazioni).

Linee:

Per tutti gli elementi della gerarchia possiamo valorizzare i seguenti elementi a prescindere:

codiceOggetto = < ASSETNUM >

descrizione = < DESCRIPTION >

codicePadre = < PARENT >

Se il campo PARENT è NULL allora l’elemento è un 1° o un 2° livello gerarchico. Pertanto, si applicano le seguenti logiche:

Se CLASSSTRUCTUREID è un 2° livello allora bisogna eseguire la seguente query:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE AL.TARGETASSETNUM='< ASSETNUM >'

A valle di questa query si esegue la seguente assegnazione:

codicePadre = < SOURCEASSETNUM prima riga>

radice = < SOURCEASSETNUM prima riga>

SAPID = < X3SAPID prima riga>

codicePadre2 = < SOURCEASSETNUM seconda riga, se esiste>

radice2 = < SOURCEASSETNUM seconda riga, se esiste>

SAPID2 = < X3SAPID seconda riga, se esiste>

Altrimenti (CLASSSTRUCTUREID è un 1° livello) allora

radice = NULL

radice2 = NULL

codicePadre2 = NULL

SAPID = < X3SAPID >

Se il campo PARENT è valorizzato e X3ROOTASSETNUM è valorizzato allora siamo in presenza di un 3° livello. La logica da applicare è la seguente:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE TARGETASSETNUM='< PARENT >'

AND SOURCEASSETNUM = '< X3ROOTASSETNUM >'

e si valorizza i seguenti campi:

radice = i-X3ROOTASSETNUM

radice2 = NULL

codicePadre2 = NULL

SAPID = X3SAPID

SAPID2 = NULL

In tutte le altre casistiche si applicano le seguenti logiche:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE AL.TARGETASSETNUM='< ANCESTOR >'

e si valorizzano i campi nel seguente modo:

radice = < SOURCEASSETNUM prima riga>

SAPID = < X3SAPID >

radice2 = < SOURCEASSETNUM seconda riga, se presente>

SAPID2 = < X3SAPID seconda riga, se presente>

codicePadre2 = NULL

Stazioni:

Per tutti gli elementi della gerarchia si valorizzano i campi nel seguente modo:

codiceOggetto = < ASSETNUM>

SAPID = < X3SAPID/X3SAPIDEQ della query iniziale o prima SAPID in gerarchia>

SAPID2 = NULL

codicePadre = < PARENT >

radice = < ANCESTOR >

radice2 = NULL

codicePadre2 = NULL

Il campo X3SAPID può essere NULL, pertanto è necessario iterare sulla tabella ASSET usando il campo PARENT fino ad individuare un SAPID.

Il campo X3INGRP può essere NULL, pertanto è necessario eseguire la seguente query per poterlo recuperare dalla tabella ASSET attraverso il campo ANCESTOR.

SELECT X3INGRP FROM ASSET

WHERE ASSETNUM = ‘< ANCESTOR >’

La struttura SOAP messa a disposizione da GeoCall è la seguente:

| Campo interfaccia | Descrizione funzionale | Tipo | Obbligatorio | Note |
| --- | --- | --- | --- | --- |
| tipoOggetto | Classificazione dell'asset | String(20) | Y | Campo classstructureid dell’oggetto ottenuto in input. |
| statoOggetto | Stato dell'asset | String(20) | N | Campo status dell’oggetto ottenuto in input. |
| codiceOggetto | Codice identificativo dell'asset | String(25) | Y | Campo assetnum dell’oggetto ottenuto in input. |
| SAPID | Precedente codice identificativo dell'asset | String(30) | Y | Valorizzare con <SAPID> |
| SAPID2 | Codice identificativo SAP per elementi in doppia terna | String(30) | N | Valorizzare con <SAPID2> |
| descrizione | Descrizione dell’oggetto tecnico (sede tecnica o equipment) | String(40) | N | Campo description dell’oggetto ottenuto in input. |
| codicePadre | Codice identificativo del padre dell'asset Informazione relativa al padre dell’oggetto trasmesso, tale informazione è necessaria per la ricostruzione della struttura della consistenza impiantistica. | String(25) | N | Valorizzare con <codicePadre> |
| radice | Informazione relativa alla radice dell’oggetto trasmesso. Necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | String(25) | N | Valorizzare con <radice> |
| codicePadre2 | Informazione relativa al secondo padre dell’oggetto trasmesso, tale informazione è necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | String(25) | N | Valorizzare con <codicePadre2> |
| codiceRadice2 | Informazione relativa al secondo padre dell’oggetto trasmesso, tale informazione è necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | String(25) | N | Valorizzare con <radice2> |
| areaLavorazione | Unità impianti dell'asset. Informazione trasmessa da EAM ma che non verrà considerata da Geocall in fase di importazione. | String(3) | N | Campo x3ingrp dell’oggetto ottenuto in input. |
| centroDiLavoro | Unità impianti dell'asset. Informazione trasmessa da EAM ma che non verrà considerata da Geocall in fase di importazione. | String(8) | N | Campo x3gewrk dell’oggetto ottenuto in input. |
| filiale | Codice dell’Unità Impianti di riferimento (Centro Operativo in Geocall). Es. GVTT1. | String(3) | Y | Campo x3ingrp dell’oggetto ottenuto in input. |
| Indirizzo | Salvato ma non utilizzato |  | N |  |
| toponimo | Salvato ma non utilizzato | String(20) | N | Valorizzare da X4SISNETADDRESS |
| via | Salvato ma non utilizzato | String(60) | N | Valorizzare da X4SISNETADDRESS |
| localita | Salvato ma non utilizzato | String(3) | N | Valorizzare da X4SISNETADDRESS |
| provincia | Salvato ma non utilizzato | String(25) | N | Valorizzare da X4SISNETADDRESS |
| cap | Salvato ma non utilizzato | String(10) | N | Valorizzare da X4SISNETADDRESS |
| comune | Salvato ma non utilizzato | String(40) | N | Valorizzare da X4SISNETADDRESS |
| istat | Salvato ma non utilizzato | String(20) | N | Valorizzare da X4SISNETADDRESS |
| latitudine1 | Coordinata X dell’impianto. Utilizzato per la schedulazione e visualizzazione su mappa. Es. 37.443419931193 | Double | N | Valorizzare da X4SISNETADDRESS |
| longitudine1 | Coordinata Y dell’impianto. Utilizzato per la schedulazione e visualizzazione su mappa. Es. 37.443419931193 | Double | N | Valorizzare da X4SISNETADDRESS |
| latitudine2 | Solo significato informativo. Presenti nel caricamento da csv. | Double | N | Valorizzare da X4SISNETADDRESS |
| longitudine2 | Solo significato informativo. Presenti nel caricamento da csv. | Double | N | Valorizzare da X4SISNETADDRESS |

GeoCall per ogni chiamata può risponde con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore GeoCall fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo useremo il business Object Structure X4ESITOINT.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "SEND_ASSET_TO_GEOCALL",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "GEOCALL",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su GeoCall rilanciando la request verso GeoCall. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Inserimento Anagrafica Massiva

Il flusso serve ad allineare le sedi tecniche di GeoCall con i dati di EAM. Il flusso è one-shot, verrà eseguito una volta sola.

dati di SISNET caricati all’interno della tabella X4SISNETADDRESS attraverso un CSV. La struttura della tabella è riportata nel paragrafo del modello relazionale.

Usando la consistenza di EAM verrà creata una tabella dove verrà riportata la gerarchia di EAM più lo stradario e coordinate geografiche di SISNET. La tabella si chiamerà X4GEOCALLADDRESS (vedere modello relazionale).

Per ogni riga della tabella ASSET è necessario recuperare le seguenti informazioni:

SAPID

SAPID2

codicePadre

codicePadre2

radice

radice2

descrizionePadre

tipoImpiantoPadre

stradario

coordinate geografiche

Di seguito verranno esposte le varie logiche in funzione della tipologia (Linee, Stazioni).

Linee:

Per tutti gli elementi della gerarchia possiamo valorizzare i seguenti elementi a prescindere:

codiceOggetto = < i-ASSETNUM >

descrizione = < i-DESCRIPTION >

codicePadre = < i-PARENT >

Se il campo PARENT è NULL allora l’elemento è un 1° o un 2° livello gerarchico. Pertanto, si applicano le seguenti logiche:

Se CLASSSTRUCTUREID è un 2° livello allora bisogna eseguire la seguente query:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID,

FROM ASSETLOCRELATION AL

WHERE AL.TARGETASSETNUM='< i-ASSETNUM >'

A valle di questa query si esegue la seguente assegnazione:

codicePadre = < SOURCEASSETNUM prima riga>

radice = < SOURCEASSETNUM prima riga>

SAPID = < X3SAPID prima riga>

codicePadre2 = < SOURCEASSETNUM seconda riga, se esiste>

radice2 = < SOURCEASSETNUM seconda riga, se esiste>

SAPID2 = < X3SAPID seconda riga, se esiste>

Altrimenti (CLASSSTRUCTUREID è un 1° livello) allora

radice = < i-ASSETNUM >

radice2 = NULL

codicePadre2 = NULL

SAPID = < i-X3SAPID >

Se il campo i-PARENT è valorizzato e i-X3ROOTASSETNUM è valorizzato allora siamo in presenza di 3° livello. La logica da applicare è la seguente:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE TARGETASSETNUM='< i-PARENT >'

AND SOURCEASSETNUM = '< i-X3ROOTASSETNUM >'

e si valorizza i seguenti campi:

radice = i-X3ROOTASSETNUM

radice2 = NULL

codicePadre2 = NULL

SAPID = X3SAPID

SAPID2 = NULL

In tutte le altre casistiche si applicano le seguenti logiche:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE AL.TARGETASSETNUM='< i-ANCESTOR >'

e si valorizzano i campi nel seguente modo:

radice = < SOURCEASSETNUM prima riga>

SAPID = < X3SAPID >

radice2 = < SOURCEASSETNUM seconda riga, se presente>

SAPID2 = < X3SAPID seconda riga, se presente>

codicePadre2 = NULL

Stazioni:

Per tutti gli elementi della gerarchia si valorizzano i campi nel seguente modo:

codiceOggetto = < i-ASSETNUM>

SAPID = < X3SAPID/X3SAPIDEQ della query iniziale o primo SAPID in gerarchia>

SAPID2 = NULL

codicePadre = < PARENT >

codicePadre2 = NULL

radice = < ANCESTOR >

radice2 = NULL

codicePadre2 = NULL

Il campo X3SAPID può essere NULL, pertanto è necessario iterare sulla tabella ASSET usando il campo PARENT fino ad individuare un SAPID.

Per determinare le coordinate bisogna accedere alla tabella X4SISNETADDRESS con logiche diverse a seconda se sono Linea o Stazione. Per determinare la tipologia eseguire seguente query:

SELECT DECODE(C.ANCESTORCLASSID, 'LINEA', 'L', 'STAZIONE', 'S') AS TYPE,

a.CLASSSTRUCTUREID, A.CLASSIFICATIONID, A.DESCRIPTION

FROM CLASSSTRUCTURE A, (SELECT max(HIERARCHYLEVELS) MAX_HIERARCHYLEVELS, CLASSSTRUCTUREID

FROM CLASSANCESTOR c GROUP BY CLASSSTRUCTUREID) B,

CLASSANCESTOR C

WHERE A.CLASSSTRUCTUREID = B.CLASSSTRUCTUREID

AND C.CLASSSTRUCTUREID = B.CLASSSTRUCTUREID

AND C.ANCESTORCLASSID IN ('STAZIONE', 'LINEA')

AND A.CLASSSTRUCTUREID = <i-CLASSSTRUCTUREID>

Di seguito si analizzano le logiche per determinare il SAPID con cui accedere alla tabella X4SISNETADDRESS per gli elementi di tipo Linea:

Se i-CLASSSTRUCTUREID è 1° livello eseguire la

SELECT B.OBJECTNUM, A.STRNO AS SAP_FIND

FROM X3SAPASSET A, X3SAPTOMAXOBJECT B

WHERE B.OBJECTNUM = '< i-ASSETNUM >'

AND A.X3SAPASSETID = B.SAPASSETID

AND B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

AND B.SITEID = 'SIT01'

Se i-CLASSSTRUCTUREID è 2° livello e di tipo CAMPATA, TRATTA CAVO eseguire le seguenti logiche:

SELECT B.OBJECTNUM, A.STRNO AS SAP_FIND

FROM X3SAPASSET A, X3SAPTOMAXOBJECT B

WHERE B.OBJECTNUM = '< i-ASSETNUM >'

AND A.X3SAPASSETID = B.SAPASSETID

AND B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

AND B.SITEID = 'SIT01'

AND (b.HIERARCHYPATH = 'LINEA\CAMPATA AEREA' OR b.HIERARCHYPATH = 'LINEA\TRATTA CAVO')

AND ROWNUM = 1

Per tutte le altre tipologie eseguire la seguente query:

SELECT B.OBJECTNUM, A.STRNO AS SAP_FIND

FROM X3SAPASSET A, X3SAPTOMAXOBJECT B

WHERE B.OBJECTNUM = '< i-ASSETNUM >'

AND A.X3SAPASSETID = B.SAPASSETID

AND B.MAXOBJECT = 'ASSET'

AND A.HISTORYFLAG = 0

AND B.SITEID = 'SIT01'

AND ROWNUM = 1

Di seguito si analizzano le logiche per determinare il SAPID con cui accedere alla tabella X4SISNETADDRESS per gli elementi di tipo Stazioni:

È necessario eseguire la seguente query:

SELECT NVL(X3SAPID, X3SAPIDEQ) AS SAP_FIND, PARENT

FROM ASSET

WHERE ASSETNUM = '< i-ASSETNUM >'

Nel caso in cui il campo SAP_FIND è NULL si itera sulla gerarchia usando il campo PARENT.

Con il campo SAP_FIND si accede alla tabella X4SISNETADDRESS per individuare lo stradario e le coordinate geografiche.

Di seguito si analizzano le logiche per interrogare la tabella X4GEOCALLADDRESS per poter determinare i restanti campi da inserire nel CSV per gli elementi di tipo Linea:

SELECT CODICE_OGGETTO

FROM X4GEOCALLADDRESS

WHERE TIPO_OGGETTO = (SELECT CLASSSTRUCTUREID

FROM CLASSSTRUCTURE

WHERE CLASSIFICATIONID = 'LINEA');

SELECT DISTINCT CODICE_RADICE, CODICE_RADICE2

FROM X4GEOCALLADDRESS

WHERE CODICE_RADICE = <CODICE_OGGETTO>

OR CODICE_RADICE2 = <CODICE_OGGETTO>;

Di seguito si analizzano le logiche per interrogare la tabella X4GEOCALLADDRESS per poter determinare i restanti campi da inserire nel CSV per gli elementi di tipo Stazione:

SELECT CODICE_OGGETTO

FROM X4GEOCALLADDRESS

WHERE TIPO_OGGETTO = (SELECT CLASSSTRUCTUREID

FROM CLASSSTRUCTURE

WHERE CLASSIFICATIONID = 'STAZIONE');

SELECT DISTINCT CODICE_RADICE, CODICE_RADICE2

FROM X4GEOCALLADDRESS

WHERE CODICE_RADICE = <CODICE_OGGETTO>

OR CODICE_RADICE2 = <CODICE_OGGETTO>;

In entrambi i casi grazie al numero di <CODICE_RADICE> ottenuti dalla seconda query riusciamo a capire se ci troviamo in un caso di Doppia Terna e determinare la query da eseguire.

Query caso non Doppia Terna:

SELECT K.* FROM

(SELECT C.*, LEVEL AS LIVELLO

FROM X4GEOCALLADDRESS C

START WITH CODICE_OGGETTO = <CODICE_RADICE>

CONNECT BY PRIOR CODICE_OGGETTO = CODICE_PADRE) K

ORDER BY LIVELLO

Query caso Doppia Terna:

SELECT K.* FROM

(SELECT C.*, LEVEL AS LIVELLO

FROM X4GEOCALLADDRESS C

START WITH CODICE_OGGETTO = <CODICE_RADICE>

CONNECT BY PRIOR CODICE_OGGETTO = CODICE_PADRE

UNION

SELECT C.*, LEVEL AS LIVELLO

FROM X4GEOCALLADDRESS C

START WITH CODICE_OGGETTO = <CODICE_RADICE_2>

CONNECT BY PRIOR CODICE_OGGETTO = CODICE_PADRE) K

ORDER BY LIVELLO

EAM con le informazioni individuate produrrà un CSV che avrà la seguente struttura:

| Campo interfaccia | Descrizione funzionale | Tipo | Obbligatorio | Note |
| --- | --- | --- | --- | --- |
| tipoOggetto | Classificazione dell'asset | String(20) | Y | Valorizzare con i-CLASSSTRUCTUREID. |
| statoOggetto | Stato dell'asset | String(20) | Y | Valorizzare con i-STATUS. |
| codiceOggetto | Codice identificativo dell'asset | String(25) | Y | Valorizzare con i-ASSETNUM |
| SAPID | Identificativo SAP | String(30) | Y | Valorizzare con <SAPID> |
| SAPID2 | Identificativo SAP2 | String(30) | N | Valorizzare con <SAPID2> |
| descrizione | Descrizione dell’asset | String(40) | N | Valorizzare con i-DESCRIPTION |
| codicePadre | Codice identificativo del padre dell'asset Informazione relativa al padre dell’oggetto trasmesso, tale informazione è necessaria per la ricostruzione della struttura della consistenza impiantistica. | String(25) | N | Valorizzare con <codicePadre> |
| codiceRadice | Radice dell’asset | String(25) | N | Se < codiceOggetto> = <radice> allora valorizzare con NULL altrimenti valorizzare con <radice> |
| codicePadre2 | Codice Padre2 dell’asset in caso di doppia terna | String(25) | N | Valorizzare con <codicePadre2> |
| codiceRadice2 | Radice2 dell’asset in caso di doppia terna | String(25) | N | Valorizzare con <radice2> |
| filiale | Identificativo filiale | String(3) | Y | Campo x3ingrp |
| toponimo | Toponimo | String(20) | N | Valore presente X4SISNETADDRESS |
| via | Valore della Via | String(60) | N | Valore presente X4SISNETADDRESS |
| civico | Valore del Civico | String(3) | N | Valore presente X4SISNETADDRESS |
| localita | Valore della località | String(3) | N | Valore presente X4SISNETADDRESS |
| provincia | Provincia | String(25) | N | Valore presente X4SISNETADDRESS |
| cap | Campo CAP | String(10) | N | Valore presente X4SISNETADDRESS |
| comune | Campo Comune | String(40) | N | Valore presente X4SISNETADDRESS |
| istat | Codice ISTAT | String(20) | N | Valore presente X4SISNETADDRESS |
| latitudine1 | Coordinata X dell’impianto. Utilizzato per la schedulazione e visualizzazione su mappa. Es. 37.443419931193 | Double | N | Valore presente X4SISNETADDRESS |
| longitudine1 | Coordinata Y dell’impianto. Utilizzato per la schedulazione e visualizzazione su mappa. Es. 37.443419931193 | Double | N | Valore presente X4SISNETADDRESS |
| latitudine2 | Solo significato informativo. Presenti nel caricamento da csv. | Double | N | Valore presente X4SISNETADDRESS |
| longitudine2 | Solo significato informativo. Presenti nel caricamento da csv. | Double | N | Valore presente X4SISNETADDRESS |

Per poter creare questi n CSV sarà messa a disposizione da EAM un’API REST che una volta chiamata produrrà questi ultimi nel pacchetto “PKG_X4GEOCALLADDRESS”, dove l’elaborazione sarà in modalità Asincrona. Di seguito verranno descritti i dettagli sul comportamento dell’API.

L’API è esposta al seguente URI:

GET https://<host:port>/eam/geocall/modificaAnagraficaMassiva

Quest’ultima non richiede parametri in input poiché alla sua chiamata eseguirà la seguente logica:

Viene chiamata la procedura PKG_X4GEOCALLADDRESS.MAIN per popolare la tabella X4GEOCALLADDRESS.

Vengono eseguite le query descritte nel paragrafo superiore per recuperare le informazioni sugli elementi da inserire nel CSV.

In base alla quantità di informazioni recuperate dalle query viene deciso se continuare a compilare il CSV corrente oppure se passare al successivo.

L’API si comporta in modo asincrono, quindi restituirà la response in questo modo:

{

"esito": "OK"

}

I CSV prodotti seguiranno questa naming convention: GEOCALL_YYYYMMGG_<ora_elaborazione>_<progressivo>.csv

Dove:

<ora_elaborazione> = HHMMSS che rappresenta l’orario di inizio elaborazione del flusso.

<progressivo> = progressivo numero da 1 a N

La creazione dei CSV dovrà seguire i seguenti vincoli:

Devono avere un numero massimo di record pari a 500.000.

All'interno di uno stesso file deve essere presente l'intera struttura di un albero di consistenza.

Le radici e i nodi padre dovranno essere riportati nel file in modo ordinato, poiché Geocall elaborerà il file seguendo l'ordine indicato. Ciò garantirà che, nel momento in cui verrà elaborato un nodo figlio, i relativi nodi padre e le radici saranno già stati precedentemente elaborati e importati sul DB di Geocall.

Gli elementi di doppia campata devono essere presenti all’interno dello stesso file.

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

#### Flusso Invio OdL a GeoCall

Ogni qualvolta che l’OdL subisce una modifica viene inviato un nuovo messaggio su coda kafka “eam-send-odl-geocall”; Il listener in ascolto sulla stessa allineerà il sistema Geocall. La logica di allineamento degli OdL a Geocall avverrà tramite selezione manuale/automatica di uno o più OdL che sono in stato “Schedulabile”. Da quel momento l’OdL verrà gestito su Geocall, eccezion fatta per alcuni campi (es indisponibilità) che potranno aggiornare le informazioni sugli OdL inviate a Geocall precedentemente, se non già presenti al primo invio.

Per bisogn effettuare una chiamata SOAP/Basic Authentication.

L’Object Structure dedicato per questa API è X4INTODLGEOCALL.

Il formato del payload dell’oggetto ottenuto in input sulla coda kafka conterrà i seguenti campi:

{

"assetnum": "1146323",

"description": "OdL Aggregatore test",

"fnlconstraint": "2024-05-31T16:54:39+00:00",

"sneconstraint": "2024-05-31T17:54:39+00:00",

"href": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/oslc/os/x4intodlgeocall/_U0lUMDEvMTU4NQ--",

"jpnum": "AT.L.CO.1",

"parent": "1002",

"wonum": "1585",

"wopriority": 1,

"workorder": [

{

"href": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/oslc/os/x4intodlgeocall/_U0lUMDEvMTU4Ng--"

}

],

"x3coddelindisp": "PROVA DELIBERA",

"x3dinizioindisp": "2024-05-31T20:54:39+00:00",

"x3dfineindisp": "2024-05-31T21:54:39+00:00",

"x3coin": "123",

"x3dataottinizio": "2024-05-31T16:54:39+00:00",

"x3nordineinterno": "Prova Ordine Interno",

"x3ntw": "Prova NTW",

"x3odm": "Prova ODM",

"x3tipoattivita": "Prova Tipo Attivita",

"x3tipoordine": "Prova Tipo Ordine",

"x3tipospesa": "OPEX",

"x3wbe": "Prova WBE",

"x3wbepian": "Prova WBE Pian",

"x4titris": "Risorsa1;Risorsa2",

"x4statusindisp": "StatoIndisp",

"x4sapidasset": "2393235233---S"

}

Questo ci permetterà di recuperare l’OdL padre con i relativi figli, riportati nell’array “workorder”. Bisognerà effettuare N chiamate con gli “href” recuperati dall’array. Di seguito la struttura dell’OdL figlio:

{

"href": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/x4intodlgeocall/_U0lUMDEvMTU4Ng--",

"estdur": 0.0,

"wonum": "1586",

"sneconstraint": "2024-05-31T17:54:39+00:00",

"fnlconstraint": "2024-05-31T16:54:39+00:00",

"targstartdate": "2024-05-31T18:54:39+00:00",

"targcompdate": "2024-05-31T19:54:39+00:00",

"jpnum": "AT.L.CO.1",

"wopriority": 0,

"x3dataottinizio": "2024-05-31T16:54:39+00:00",

"x4sapidasset": "3233543223---S"

}

La struttura SOAP messa a disposizione da GeoCall è la seguente:

| Campo Interfaccia | Descrizione funzionale | Tipo | Obbligatorio | Note |
| --- | --- | --- | --- | --- |
| CODICE_ORDINE_ESTERNO | Codice identificativo dell'ordine di lavoro? | String(10) | Y | Campo wonum dell’oggetto ottenuto in input. |
| CODICE_CATALOGO | Codice identificativo dell'attività | String(12) | Y | Campo jpnum dell’oggetto ottenuto in input. |
| DESCRIZIONE_ATTIVITA | Descrizione attività | String(100) | N | Campo description dell’oggetto ottenuto in input. |
| <TITRIS> | Titolari risorse dell'OdL |  | N | Nodo contenente tutti i Titris relativi all’OdL |
| <UNITA_IMPIANTI> | Unità Impianti |  | Y | Nodo contenente tutte le Unità Impianti relative all’OdL |
| COD_UNITA_IMPIANTI | Codice Identificativo Unità Impianti | String(150) | Y | Campo x4titris dell’oggetto ottenuto in input. |
| <RISORSE> | Risorse |  | N | Nodo contenente tutte le risorse relative al Titris |
| COD_INQUADRAMENTO | Codice Identificativo Inquadramento | String(8) | N | Campo craft dell’oggetto ottenuto in input |
| NUMERO_RISORSE | Numero Risorse | String | N | Campo quantity dell’oggetto ottenuto in input |
| </RISORSE> |  |  |  |  |
| </UNITA_IMPIANTI> |  |  |  |  |
| </TITRIS> |  |  |  |  |
| DATA_TOLL_SX | Data minima di schedulazione | DateTime | N | Campo sneconstraint dell’oggetto ottenuto in input. |
| DATA_TOLL_DX | Data massima di schedulazione | DateTime | N | Campo fnlconstraint dell’oggetto ottenuto in input. |
| DATA_INIZIO_ATT | Data inizio pianificata | DateTime | N | Campo workorder.targstartdate dell’oggetto ottenuto in input. |
| DATA_FINE_ATT | Data fine pianificata | DateTime | N | Campo workorder.targcompdate dell’oggetto ottenuto in input. |
| PRIORITA | Priorità OdL | Number | N | Campo wopriority dell’oggetto ottenuto in input. |
| COD_SEDE_TECNICA | Codice Asset su cui effettuare manutenzione | String(25) | Y | Campo assetnum dell’oggetto ottenuto in input. |
| CODICE_CLUSTER | Solo per gli ordini aggregati, viene valorizzato con il wonum dell’ordine padre | String(10) | N | Corrisponde al campo wonum se la struttura workorder non è vuota (ODL Aggregato), altrimenti valorizzare con NULL |
| TIPO_SPESA | Tipo spesa | String(5) | Y | WORKORDER.X3TIPOSPESA |
| CODICE DELIBERA INDISPONIBILITA | Codice delibera indisponibilità | String(25) | N | Campo x4coddelindisp dell’oggetto ottenuto in input. |
| FLAG_INDISPONIBILITA | Flag Indisponibilità | String | N | Campo x4indisponibilita dell’oggetto in input |
| STATO_DELIBERA | Stato Delibera | String(20) | N | Campo x4statusindisp dell’oggetto ottenuto in input |
| DATA INIZIO INDISPONIBILITA | Data di inizio indisponibilità | DateTime | N | Campo x4dinizioindisp dell’oggetto ottenuto in input. |
| DATA FINE INDISPONIBILITA | Data di fine indisponibilità | DateTime | N | Campo x4dfineindisp dell’oggetto ottenuto in input. |
| <OPERAZIONE> | Nodo contenente tutti gli ODL figli legati all'odl di testata |  | Y | I seguenti campi sono recuperati dalla struttura figlio (secondo esempio). |
| COD_POSIZIONE | Codice dell'ordine di lavoro. Se non è un OdL aggregato, sarà pari al codice ordine esterno. Se il codice ordine esterno è un odl aggregato, sarà presente il singolo OdL figlio in codice posizione. | String(10) | Y | Campo workorder.wonum dell’oggetto ottenuto in input. |
| DATA_TOLL_SX | Data minima di schedulazione | DateTime | N | Campo workorder.sneconstraint dell’oggetto ottenuto in input. |
| DATA_TOLL_DX | Data massima di schedulazione | DateTime | N | Campo workorder.fnlconstraint dell’oggetto ottenuto in input. |
| DATA_INIZIO_ATT | Data inizio pianificata | DateTime | N | Campo workorder.shedstart dell’oggetto ottenuto in input. |
| DATA_FINE_ATT | Data fine pianificata | DateTime | N | Campo workorder.schedfinish dell’oggetto ottenuto in input. |
| PROPEDEUTICITA | Propedeuticità | String(50) | N | Verrà sempre valorizzato con NULL |
| DURATA_OP | Durata dell'attività | Float | N | Campo workorder.estdur dell’oggetto ottenuto in input. |
| COD_SEDE_TECNICA | Codice Asset su cui effettuare manutenzione | String(25) | Y | Campo workorder.assetnum dell’oggetto ottenuto in input. |
| SAPID | Codice SAPID (chiave Sisnet) | String(30) | Y | Campo workorder.x4sapidasset dell’oggetto ottenuto in input. |
| </OPERAZIONE> |  |  |  |  |

GeoCall per ogni chiamata può risponde con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore GeoCall fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo useremo il business Object Structure X4ESITOINT. L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "SEND_ODL_TO_GEOCALL",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "GEOCALL",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su GeoCall rilanciando la request verso GeoCall. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Annullamento/Blocco Ordini di Lavoro

Ogni qualvolta l’utente attiva l’operazione di “” da Maximo ponendo l’intero OdL in stato , viene inviato sulla coda kafka “eam-annull-block-odl” un messaggio contenete il workorder padre. Un sottoinsieme di queste informazioni servirà per costruire il messaggio da inviare a GeoCall. La chiamata è di tipo SOAP con Basic Authentication.

La valorizzazione dei campi è la seguente:

| Campo Interfaccia | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| CODICE_ORDINE_ESTERNO | Identificativo Istanza | Y | Campo wonum d’input |
| CODICE_CLUSTER | Identificativo Cluster | N | Se l’ordine è di tipo aggregato (campo workorder d’input contenente i figli non vuoto) allora valorizzare con campo wonum d’input. |
| TIPO_OPERAZIONE | Tipo di operazione | Y | Valorizzato sempre con A |

GeoCall per ogni chiamata può risponde con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore GeoCall fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo useremo il business Object Structure X4ESITOINT. L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "SEND_ANN_TO_GEOCALL oppure SEND_BLOCC_TO_GEOCALL",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "GEOCALL",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su GeoCall rilanciando la request verso GeoCall. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Annulla Operazione

Ogni qualvolta , viene inviato sulla coda kafka “eam-annull-odl” un messaggio contenete il workorder da annullare. Un sottoinsieme di queste informazioni servirà per costruire il messaggio da inviare a GeoCall. La chiamata è di tipo SOAP con Basic Authentication.

La valorizzazione dei campi è la seguente:

| Campo Interfaccia | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| CODICE_ORDINE_ESTERNO | Identificativo Istanza | Y | Campo parent d’input |
| CODICE_POSIZIONE | Identificativo codice posizione | Y | Campo wonum d’input |
| TIPO_OPERAZIONE | Tipo di operazione | Y | A |

GeoCall per ogni chiamata può risponde con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore GeoCall fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo useremo il business Object Structure X4ESITOINT. L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "SEND_ANN_TO_GEOCALL",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "GEOCALL",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su GeoCall rilanciando la request verso GeoCall. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Ricezione Schedulazione OdL

Ogni qualvolta l’attività di schedulazione ed assegnazione delle risorse è terminato su GeoCall bisogna allineare il sistema EAM. A tale scopo GeoCall invoca l’API di schedulazione esposta su EAM.

L’API è esposta al seguente URI:

POST https://<host:port>/eam/ws/oauth2/notifyChiusuraOdl.wsdl

con SOAPAction = notifySchedulazioneOdl/4.0.

La request è la seguente:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://soa.terna.it/GEOCALL/Odl_Notify/4.0">

<soapenv:Header/>

<soapenv:Body>

<ns:notifySchedulazioneOdl_Input>

<CODICE_ORDINE_ESTERNO>1059</CODICE_ORDINE_ESTERNO>

<PREPOSTO>LABOR1</PREPOSTO>

<!--Optional:-->

<SQUADRA>

<!--1 or more repetitions:-->

<MATRICOLA>LABOR</MATRICOLA>

<MATRICOLA>LABOR</MATRICOLA>

</SQUADRA>

<!--1 or more repetitions:-->

<OPERAZIONI>

<CODICE_POSIZIONE>1059</CODICE_POSIZIONE>

<DATA_INI_SCHEDULATA>2024-06-18T09:30:47Z</DATA_INI_SCHEDULATA>

<DATA_FINE_SCHEDULATA>2024-06-18T20:30:47Z</DATA_FINE_SCHEDULATA>

</OPERAZIONI>

</ns:notifySchedulazioneOdl_Input>

</soapenv:Body>

</soapenv:Envelope>

Le info inviate vanno salvate su Maximo usando l’Object Structure X4APIWOASS.

Come prima cosa bisogna individuare il link/record d’aggiornare. A tale bisogna invocare X4APIWOASS con un’operazione di GET:

GET

https://<HOST:PORT>/maximo/api/os/X4APIWOASS?lean=1&oslc.where=WONUM%3D%221107%22

passando i seguenti parametri:

lean = 1 è Rimozione dei namespace

oslc.where = < CODICE_ORDINE_ESTERNO d’input > è record della tabella workorder da individuare.

oslc.select è jpnum,istask,siteid,orgid,woclass,status,schedstart,schedfinish,assignment,assignment.laborcode,lead,statusdate,wonum

L’output della risposta rest è la seguente:

{

"member": [

{

"href": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/X4APIWOASS/_U0lUMDEvMTEwNw--"

}

],

"href": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/X3WORKORDER",

"responseInfo": {

"href": "http://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/X4APIWOASS?lean=1&oslc.where=WONUM%3D%221107%22"

}

}

Nella struttura member è presente il link associato alla risorsa X4APIWOASS d’aggiornare.

Una volta individuato il workorder va controllato il campo “stato” della risorsa per poter verificare se ci troviamo nella situazione di prima schedulazione o rischedulazione. Le regole utilizzate per questa verifica sono le seguenti:

Stato = Schedulabile è Prima Schedulazione

Stato = Schedulato/Inoltrato/Eseg. Parz./Avviato è Rischedulazione

Una volta verificata la condizione verrà eseguito uno tra due tipi di aggiornamento, nel caso della prima schedulazione vanno inseriti tutti i dati riportati nella request e per ogni codice posizione (Operazioni) va modificato lo stato in “Schedulato”; nel caso della rischedulazione vanno cancellati tutti i dati precedenti relativi alle matricole e preposto, per poi successivamente inserire i nuovi riportati nella request senza effettuare cambio di stato. La struttura generica della POST è la seguente:

{

"jpnum": "L.1.1.1.3",

"istask": false,

"siteid": "SIT01",

"href": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/x4apiwoass/_U0lUMDEvMTUyOQ--",

"orgid": "ORG01",

"woclass": "WORKORDER",

"SCHEDSTART": "2024-05-31T16:54:39+00:00",

"SCHEDFINISH": "2024-05-31T16:54:39+00:00",

"assignment_collectionref": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/x3apiwoass2/_U0lUMDEvMTUyOQ--/showassignment",

"assignment": [

{

"localref": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/x3apiwoass2/_U0lUMDEvMTUyOQ--/showassignment/0-577",

"laborcode": "<matricola>",

"href": "http://childkey#V09SS09SREVSL0FTU0lHTk1FTlQvNTc3"

}

],

"lead": "<PREPOSTO>",

"statusdate": "<data corrente nel seguente formato 2024-05-32T14:21:27+00:00>",

"wonum": "<CODICE_ORDINE_ESTERNO>"

}

L’esito dell’elaborazione verrà salvato l’esito nella tabella X4ESITOINT. A tale scopo si userà il business Object Structure X4ESITOINT.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "EAM_SCHEDULE ",

"desc_esito": "XYZ",

"source": "GEOCALL",

"dest": "EAM",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

La risposta da fornire a GeoCall è la seguente:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://soa.terna.it/GEOCALL/Odl_Notify/4.0">

<soapenv:Header/>

<soapenv:Body>

<ns:notifySchedulazioneOdl_Output>

<ESITO>KO</ESITO>

<!--Optional:-->

<CODICE_ERRORE>EAM_0002</CODICE_ERRORE>

<!--Optional:-->

<DESCRIZIONE_ERRORE>Campi in input non valorizzati correttamente</DESCRIZIONE_ERRORE>

</ns:notifySchedulazioneOdl_Output>

</soapenv:Body>

</soapenv:Envelope>

I possibili codici d’errore che prevediamo sono i seguenti:

| Azione | Valore Esito | Valore Codice Errore | Valore Descrizione Errore | Http Code Response |
| --- | --- | --- | --- | --- |
| Errore generico di EAM | KO | EAM_0000 | Errore generico di EAM | 503 |
| Errore di validazione | KO | EAM_0001 | Campi in input non valorizzati correttamente | 400 |
| Stato non coincidente con nessuno stato predisposto in EAM | KO | EAM_0002 | Campo ‘stato’ non valorizzato correttamente | 400 |
| Codice ordine di lavoro non presente in EAM | KO | EAM_0004 | Codice ordine di lavoro non presente in EAM | 404 |
| Codice matricola non presente in EAM | KO | EAM_0005 | Codice matricola non presente in EAM | 404 |

#### Ricezione consuntivazioni Ore

Ogni qualvolta è necessario aggiornare l’OdL di EAM. A tale scopo GeoCall invocherà l’API di notifica esposta da EAM.

L’API è esposta al seguente URI:

POST https://<host:port>/eam/ws/oauth2/notifyChiusuraOdl.wsdl

con SOAPAction = notifyChiusuraOdl/4.0.

La request è la seguente:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://soa.terna.it/GEOCALL/Odl_Notify/4.0">

<soapenv:Header/>

<soapenv:Body>

<ns:notifyChiusuraOdl_Input>

<CODICE_ORDINE_ESTERNO>1560</CODICE_ORDINE_ESTERNO>

<!--1 or more repetitions:-->

<LABTRANS>

<IDCONSUNTIVO>563224</IDCONSUNTIVO>

<MATRICOLA>LABOR1</MATRICOLA>

<DATA_INI_EFFETTIVA>2024-12-20T09:30:47Z</DATA_INI_EFFETTIVA>

<DATA_FINE_EFFETTIVA>2024-12-20T18:30:47Z</DATA_FINE_EFFETTIVA>

<ORE>9.0</ORE>

<!--Optional:-->

<ORE_UFFICIO>10.0</ORE_UFFICIO>

<!--Optional:-->

<TEMPO_DI_TRASFERIMENTO>2.0</TEMPO_DI_TRASFERIMENTO>

</LABTRANS>

</ns:notifyChiusuraOdl_Input>

</soapenv:Body>

</soapenv:Envelope>

Le info inviate vanno salvate su Maximo usando l’Object Structure X4APIWOLABTRANS.

Come prima cosa bisogna individuare il link/record d’aggiornare. A tale bisogna invocare X4APIWOLABTRANS con un’operazione di GET:

GET

https://<HOST:PORT>/maximo/api/os/X4APIWOLABTRANS?lean=1&oslc.where=WONUM%3D%221560%22

passando i seguenti parametri:

lean = 1 è Rimozione dei namaspace

oslc.where = < CODICE_ORDINE_ESTERNO d’input > è record della tabella workorder da individuare.

oslc.select è wonum,status,labtrans,labtrans.startdatetime, abtrans.finishdatetime,labtrans.regularhrs,labtrans.x3ttrasferimento,labtrans.laborcode,labtrans.lavoro_effettivo_pre, labtrans.externalrefid

L’output della risposta rest è la seguente:

{

"member": [

{

"href": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/x4apiwolabtrans/_U0lUMDEvMTU2MA--"

}

],

"href": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/X4APIWOLABTRANS",

"responseInfo": {

"href": "http://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/X4APIWOLABTRANS?lean=1&oslc.where=WONUM=1560"

}

}

Nella struttura member è presente il link associato alla risorsa X4APIWOLABTRANS d’aggiornare.

Con il link individuato è necessario aggiornare i dati della workorder e delle matricole associate. La struttura della POST è la seguente:

{

"wonum": "1560",

"labtrans": [

{

"externalrefid": "000242",

"startdatetime": "2024-06-18T00:00:00+00:00",

"finishdatetime": "2024-06-18T10:00:00+00:00",

"href": "http://childkey#V09SS09SREVSL0xBQlRSQU5TL01ST1NTSS81MDQvU0lUMDE-",

"regularhrs": 10.0,

"x3ttrasferimento": 2.0,

"lavoro_effettivo_pre": 3.0,

"laborcode": "MROSSI"

}, {

"externalrefid": "000243",

"startdatetime": "2024-06-19T00:00:00+00:00",

"finishdatetime": "2024-06-19T10:00:00+00:00",

"href": "http://childkey#V09SS09SREVSL0xBQlRSQU5TL01ST1NTSS81MDUvU0lUMDE-",

"regularhrs": 10.0,

"x3ttrasferimento": 2.0,

"lavoro_effettivo_pre": 3.0,

"laborcode": "MROSSI"

}

]

}

L’esito dell’elaborazione verrà salvato l’esito nella tabella X4ESITOINT. A tale scopo si userà il business Object Structure X4ESITOINT.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "EAM_BALANCE ",

"desc_esito": "XYZ",

"source": "GEOCALL",

"dest": "EAM",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

La risposta da fornire a GeoCall è la seguente:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://soa.terna.it/GEOCALL/Odl_Notify/4.0">

<soapenv:Header/>

<soapenv:Body>

<ns:notifyChiusuraOdl_Output>

<ESITO>KO</ESITO>

<!--Optional:-->

<CODICE_ERRORE>EAM-0004</CODICE_ERRORE>

<!--Optional:-->

<DESCRIZIONE_ERRORE>Codice ordine di lavoro non presente in EAM</DESCRIZIONE_ERRORE>

</ns:notifyChiusuraOdl_Output>

</soapenv:Body>

</soapenv:Envelope>

I possibili codici d’errore che prevediamo sono i seguenti:

| Azione | Valore Esito | Valore Codice Errore | Valore Descrizione Errore | Http Code Response |
| --- | --- | --- | --- | --- |
| Errore generico di EAM | KO | EAM_0000 | Errore generico di EAM | 503 |
| Errore di validazione | KO | EAM_0001 | Campi in input non valorizzati correttamente | 400 |
| Codice ordine di lavoro non presente in EAM | KO | EAM_0004 | Codice ordine di lavoro non presente in EAM | 404 |
| Codice matricola non presente in EAM | KO | EAM_0005 | Codice matricola non presente in EAM | 404 |
| Date di consuntivazione comunicate nel messaggio nel futuro rispetto alla data di sistema | KO | EAM_0014 | Date di consuntivazione comunicate nel messaggio nel futuro rispetto alla data di sistema | 400 |

#### Notifica Cambio Stato

Ogni qualvolta l’OdL o le sue operazioni sottese subiscono un cambio di stato è necessario allineare il sistema EAM. A tale scopo GeoCall invocherà l’api di notifica cambio stato esposta da EAM.

L’API è esposta al seguente URI:

POST https://<host:port>/eam/ws/oauth2/changeStatusOdl.wsdl

La request è la seguente:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://soa.terna.it/GEOCALL/Odl_Status/1.0">

<soapenv:Header/>

<soapenv:Body>

<ns:changeStatusOdl_Input>

<!--Optional:-->

<CODICE_CLUSTER>20863</CODICE_CLUSTER>

<CODICE_POSIZIONE>20863</CODICE_POSIZIONE>

<STATO_OPERAZIONE>In Carico</STATO_OPERAZIONE>

<!--Optional:-->

<DATA_CAMBIO_STATO>2024-12-20T09:30:47Z</DATA_CAMBIO_STATO>

<STATO_ORDINE>Chiuso</STATO_ORDINE>

<!--Optional:-->

<NOTA_CHIUSURA>Prova Nota</NOTA_CHIUSURA>

</ns:changeStatusOdl_Input>

</soapenv:Body>

</soapenv:Envelope>

Le info inviate vanno salvate su Maximo usando l’Object Structure X4WORKORDER.

Come prima cosa bisogna individuare il link/record d’aggiornare. A tale bisogna invocare X4WORKORDER con un’operazione di GET:

GET

https://<HOST:PORT>/maximo/api/os/X4WORKORDER?lean=1&oslc.where=WONUM%3D%221560%22

passando i seguenti parametri:

lean = 1 è Rimozione dei namespace

oslc.where = < CODICE_POSIZIONE d’input > è record della tabella workorder da individuare.

oslc.select è wonum,status,changedate

L’output della risposta rest è la seguente:

{

"member": [

{

"href": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/x4workorder/_U0lUMDEvMTU2MA--"

}

],

"href": "https://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/X4WORKORDER ",

"responseInfo": {

"href": "http://masdev.manage.dev.apps.dev.azopencs.servizi.prv/maximo/api/os/X4WORKORDER?lean=1&oslc.where=WONUM=1560"

}

}

Nella struttura member è presente il link associato alla risorsa X4WORKORDER d’aggiornare.

Con il link individuato è necessario aggiornare i dati della workorder. Dovrà essere aggiornata la singola operazione con lo stato presente nel campo “STATO_OPERAZIONE” (In particolare nel caso in cui lo stato dovesse essere “In Carico” va trascodificato in “Avviato”). Il campo “STATO_ORDINE” va considerato solo quando valorizzato a “Chiuso”, questo comporta l’aggiornamento del flag “X4GEOCALLCLOSED” per indicare che l’OdL è Chiuso in GeoCall. La struttura della POST è la seguente:

{

"wonum": "1560",

"status": "APERTO",

"changedate": "2024-06-21T15:30:08+00:00"

}

Nella request il campo “STATO_ORDINE” viene popolato con gli stati utilizzati da GeoCall, piuttosto che quello usato da EAM; sarà necessario trascodificare lo stato con quello appropriato seguendo questa tabella:

| Stato GeoCall | Stato EAM |
| --- | --- |
| Chiuso | STATO_OPERAZIONE èCHIUSO e viene eseguita la logica sul “CAUSALE_ESITO” STATO_ORDINE è Non viene modificato lo stato ma va popolato il flag “X4GEOCALLCLOSED” per indicare che l’OdL è Chiuso in GeoCall |
| Eseguito | ESEGUITO |
| Fissato | SCHEDULATO |
| Inoltrato | INOLTRATO |
| In Carico | AVVIATO |
| Schedulabile | SCHEDULABILE |
| Inviato | AVVIATO |
| Chiuso Tecnicamente | CHIUSO |
| Parzialmente Consuntivato | AVVIATO |

Qualora il campo “STATO_OPERAZIONE” sia valorizzato a “CHIUSO” e il campo “CAUSALE_ESITO” non sia vuoto, in base al valore di quest’ultimo viene ulteriormente modificato lo stato seguendo queste regole:

CAUSALE_ESITO = “ATTIVITA’ ESEGUITA” è Campo “STATO_ORDINE” passa in “ESEGUITO”.

CAUSALE_ESITO = “NON ESEGUITA” è Campo “STATO_ORDINE” passa in “NON ESEGUITO”.

Inoltre andremo a salvare per ogni OdL sul quale è stato modificato lo stato il relativo stato inviato da GeoCall, in particolare verrà salvato nel campo “X4STATOGEOCALL” presente sulla tabella WorkOrder (Ad esempio nel caso dello stato GeoCall “In Carico” andremo a modificare l’OdL in “Avviato” ed andremo a salvare nel campo “X4STATOGEOCALL” la stringa “In Carico”).

L’esito dell’elaborazione verrà salvato l’esito nella tabella X4ESITOINT. A tale scopo si userà il business Object Structure X4ESITOINT.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "EAM_BALANCE ",

"desc_esito": "XYZ",

"source": "GEOCALL",

"dest": "EAM",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

La risposta da fornire a GeoCall è la seguente:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://soa.terna.it/EAM/Odl_ChangeStatus/1.0">

<soapenv:Header/>

<soapenv:Body>

<ns:changeStatusOdl_Output>

<ESITO>KO</ESITO>

<!--Optional:-->

<CODICE_ERRORE>EAM_0000</CODICE_ERRORE>

<!--Optional:-->

<DESCRIZIONE_ERRORE>Errore generico di EAM.</DESCRIZIONE_ERRORE>

</ns:changeStatusOdl_Output>

</soapenv:Body>

</soapenv:Envelope>

I possibili codici d’errore che prevediamo sono i seguenti:

| Azione | Valore Esito | Valore Codice Errore | Valore Descrizione Errore | Http Code Response |
| --- | --- | --- | --- | --- |
| Errore generico di EAM | KO | EAM_0000 | Errore generico di EAM | 503 |
| Field 'stato' not valorized correctly. | KO | EAM_0002 | Stato non coincidente con nessuno stato predisposto in EAM | 400 |
| Work Order code not present in EAM. | KO | EAM_0004 | Codice ordine di lavoro non presente in EAM | 404 |

#### Storno

Ogni qualvolta che l’utente seleziona uno specifico consuntivo ore di una specifica risorsa da interfaccia Maximo richiama la funzionalità di storno; successivamente sarà compito di SAP soddisfare la richiesta. Dopo aver effettuato lo storno, SAP invierà sulla coda “eam-send-sap-storno-geocall” un messaggio contenente le informazioni relative allo storno. Un sottoinsieme di queste informazioni servirà per costruire il messaggio da inviare a GeoCall. La chiamata è di tipo SOAP con OAuth2.

Il formato del payload dell’oggetto ottenuto in input sulla coda kafka conterrà i seguenti campi:

{

"idConsuntivo": "320329310",

"matricola": "xt4360",

"tipoStorno": "T",

"instance": "1560"

}

Una volta ottenuto il messaggio in entrambi i casi di Storno (Parziale o Totale) va notificato GeoCall attraverso la seguente interfaccia. La valorizzazione dei campi è la seguente:

| Campo Interfaccia | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| IdConsuntivo | ID univoco Geocall del consuntivo | N | Campo “idConsuntivo”. Questo campo diventa obbligatorio quando si parla di Storno Parziale. |
| Matricola | Matricola dell’utente riferita al consuntivo | N | Campo “matricola”. Questo campo diventa obbligatorio quando si parla di Storno Parziale. |
| ITpStorno | Tipologia storno | Y | Campo “tipoStorno”. Può assumere valori T, P (Totale, Parziale) |
| IZzinstance | Ordine di lavoro | Y | Campo “instance” (corrisponde al Wonum dell’OdL) |

GeoCall per ogni chiamata può risponde con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore GeoCall fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo useremo il business Object Structure X4ESITOINT. L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "SEND_STORNO_TO_GEOCALL",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "GEOCALL",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su GeoCall rilanciando la request verso GeoCall. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Schedulatore per Rielaborare i messaggi in errore

In qualsiasi flusso di Geocall, per il quale si è riscontrato un errore applicativo/comunicazione si provvede a realizzare un motore che rilancerà per n-volte il messaggio a Geocall nel tentativo di ottenere un esito positivo nella comunicazione. Il numero di tentativi dev’essere configurabile attraverso variabili d’ambiente di Open Shift.

Il motore sfrutterà i dati presenti sulla tabella X4ESITOINT cercando i record che hanno ESITO = KO, PARENT = null e SOURCE = EAM.

Per ogni record individuato si provvederà ad acquisire il dato presente in MESSAGE ed inserirlo nell’opportuna coda kafka in modo da rilanciare l’elaborazione (campo KAFKA_TOPIC).

#### Modello Relazione

Di seguito verranno riportate le tabelle usate all’interno dei flussi di sprint 3 con i relativi dettagli:

X4RENAMESAPIDGEO:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4RENAMESAPIDGEOID | NUMBER | Y | PK |
| ASSETNUM | VARCHAR(30) | Y | AssetNum modificato |
| SITEID | VARCHAR(30) | Y | SiteId Associato |
| SAPID_ORIG | VARCHAR(30) | Y | Vecchio SapId |
| SAPID_DEST | VARCHAR(30) | Y | Nuovo SapId |
| UPDATETIME | DATE | N | Data creazione/modifica |
| DA_NOTIFICARE_GEOCALL | VARCHAR(1) | N | Record da elaborare e notificare a Geocall. Può assumere valore Y o N |

X4GEOCALLADDRESS:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| TIPO_OGGETTO | VARCHAR(20) | N | Campo Tipo Oggetto |
| STATO_OGGETTO | VARCHAR(20) | N | Campo Stato Oggetto |
| CODICE_OGGETTO | VARCHAR(30) | N | Codice Oggetto – SAPID |
| CODICE_PADRE | VARCHAR(30) | N | Codice Padre – SAPID |
| CODICE_RADICE | VARCHAR(30) | N | Codice Radice della gerarchia |
| CODICE_SAP | VARCHAR(30) | N | Codice SAP |
| CODICE_PADRE2 | VARCHAR(30) | N | Codice Padre – SAPID |
| CODICE_RADICE2 | VARCHAR(30) | N | Codice Radice per gli elementi in doppia terna |
| CODICE_SAP2 | VARCHAR(30) | N | Codice SAP2 per gli elementi in doppia terna |
| DESCRIZIONE | VARCHAR(40) | N | Campo Descrizione |
| FILIALE | VARCHAR(20) | N | Campo Filiale |
| TOPONIMO | VARCHAR(20) | N | Campo Toponimo |
| VIA | VARCHAR(60) | N | Campo Via |
| CIVICO | VARCHAR(15) | N | Campo Civico |
| LOCALITA | VARCHAR(5) | N | Campo Località |
| PROVINCIA | VARCHAR(25) | N | Campo Provincia |
| CAP | VARCHAR(10) | N | CAP |
| COMUNE | VARCHAR(40) | N | Comune |
| ISTAT | VARCHAR(20) | N | Campo ISTAT |
| LATITUDINE1 | NUMBER | N | Campo Latitudine |
| LONGITUDINE1 | NUMBER | N | Campo Longitudine |
| LATITUDINE2 | NUMBER | N | Campo Latitudine2 Opzionale |
| LONGITUDINE2 | NUMBER | N | Campo Longitudine Opzionale |

X4SISNETADDRESS:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4SISNETADDRESSID | NUMBER | Y | PK |
| TIPO_OGGETTO | VARCHAR(20) | N | Campo Tipo Oggetto |
| STATO_OGGETTO | VARCHAR(20) | N | Campo Stato Oggetto |
| CODICE_OGGETTO | VARCHAR(30) | N | Codice Oggetto – SAPID |
| CODICE_PADRE | VARCHAR(30) | N | Codice Padre – SAPID |
| CODICE_RADICE | VARCHAR(30) | N | Codice Radice - SAPID |
| LIVELLO | VARCHAR(1) | N | Livello (1, 2, 3) |
| DESCRIZIONE | VARCHAR(40) | N | Campo Descrizione |
| AREA_LAVORAZIONE | VARCHAR(20) | N | Campo Area Di Lavorazione |
| CENTRO_DI_LAVORO | VARCHAR(20) | N | Campo Centro di Lavoro |
| FILIALE | VARCHAR(20) | N | Campo Filiale |
| TOPONIMO | VARCHAR(20) | N | Campo Toponimo |
| VIA | VARCHAR(60) | N | Campo Via |
| CIVICO | VARCHAR(15) | N | Campo Civico |
| LOCALITA | VARCHAR(5) | N | Campo Località |
| PROVINCIA | VARCHAR(25) | N | Campo Provincia |
| CAP | VARCHAR(10) | N | CAP |
| COMUNE | VARCHAR(40) | N | Comune |
| ISTAT | VARCHAR(20) | N | Campo ISTAT |
| LATITUDINE1 | NUMBER | N | Campo Latitudine |
| LONGITUDINE1 | NUMBER | N | Campo Longitudine |
| LATITUDINE2 | NUMBER | N | Campo Latitudine2 Opzionale |
| LONGITUDINE2 | NUMBER | N | Campo Longitudine Opzionale |
| CREATION_DATE | DATE | Y | Data Creazione record |
| UPDATE_DATE | DATE | N | Data Aggiornamento record |
| DA_NOTIFICARE_GEOCALL | VARCHAR(1) | N | Indica se il record è da notificare a GeoCall |

WORKORDER:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| WONUM | VARCHAR2(10) | Y | Numero identificativo dell'ordine di lavoro |
| SITEID | VARCHAR2(8) | Y | Sede dell'ordine di lavoro |
| DESCRIPTION | VARCHAR2(100) | N | Descrizione dell'ordine di lavoro |
| STATUS | VARCHAR2(16) | Y | Stato OdL |
| JPNUM | VARCHAR2(12) | N | Identificativo univoco dell'ID del PdL associato all'OdL |
| ASSETNUM | VARCHAR2(25) | N | Identificativo univoco della sede tecnica su cui effettuare l'ordine di lavoro |
| X3NATURALAVORO | VARCHAR2(20) | N | Assistenza/Sorveglianza |
| X3DATAMODELLO | DATE | N | Data modello OdL |
| TARGSTARTDATE | DATE | N | Data di inizio lavoro PIANIFICATA |
| TARGFINISHDATE | DATE | N | Data di fine lavoro PIANIFICATA |
| X3DATAOTTINIZIO | DATE | N | Data ottimizzata inizio, stampata dall'ottimizzatore |
| X3DATAOTTFINE | DATE | N | Data ottimizzata fine, stampata dall'ottimizzatore |
| SNECONSTRAINT | DATE | N | Inizia non prima di |
| FNLCONSTRAINT | DATE | N | Data massima esecuzione |
| ESTDUR | FLOAT | N | Durata OdL |
| X3TPREP | FLOAT | N | Tempo di preparazione totale |
| X3TOPER | FLOAT | N | Tempo operazione totale |
| X3TTRASF | FLOAT | N | Tempo spostamento totale |
| ACTSTART | DATE | N | Data di inizio lavoro effettiva |
| ACTFINISH | DATE | N | Data di fine lavoro effettiva |
| X4TIPOSPESA | VARCHAR2(5) | N | Tipo di spesa (CAPEX, OPEX, ANR) |
| X4COIN | VARCHAR2(15) | N | ID Coin |
| X4WBE | VARCHAR2(20) | N | WBE OdL |
| X4WBEPIAN | VARCHAR2(20) | N | WBE Pianificata OdL |
| X4WBS | VARCHAR2(20) | N | WBS OdL |
| X4TIPOATTIVITA | VARCHAR2(15) | N | Tipo attività contabile |
| X4TIPOORDINE | VARCHAR2(15) | N | Tipo ordine contabile |
| X4ODM | VARCHAR2(20) | N | ID ODM SAP |
| X4NTK | VARCHAR2(20) | N | ID NETWORK SAP |
| X4NORDINEINTERNO | VARCHAR2(20) | N | ID ORDINE INTERNO SAP |
| WOPRIORITY | NUMBER | N | Priorità OdL |
| EXTERNALREFID | VARCHAR2(10) | N | Chiave sistema esterno |
| SCHEDSTART | DATE | N | Data inizio schedulazione |
| SCHEDFINISH | DATE | N | Data fine schedulazione |
| PREDECESSORWOS | VARCHAR2(256) | N | Negli OdL aggregati, indica la sequenza degli OdL |
| LEAD | VARCHAR2(100) | N | Preposto |
| X4GEOCALLCLOSED | BOOLEAN | N | Flag per indicare ordine “Chiuso” in GeoCall |

ASSET:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| CLASSSTRUCTUREID | VARCHAR2(20) | Y | Classificazione dell'asset |
| STATUS | VARCHAR2(20) | Y | Stato dell'asset |
| ASSETNUM | VARCHAR2(25) | Y | Codice identificativo dell'asset |
| PARENT | VARCHAR2(25) | N | Codice identificativo del padre dell'asset |
| DESCRIPTION | VARCHAR2(40) | N | Descrizione dell’oggetto tecnico (sede tecnica o equipment) |
| X3INGRP | VARCHAR2(3) | N | Unità impianti dell'asset. |
| X3GEWRK | VARCHAR2(8) | N | Unità impianti dell'asset. |
| X3SAPID | VARCHAR2(30) | N | SapId |
| SITEID | VARCHAR2(8) | Y | Sede dell'asset |

ASSIGNMENT

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ASSIGNMENTID | NUMBER | Y | PK |
| STATUS | VARCHAR2(12) | Y | Stato dell'assignment |
| WONUM | VARCHAR2(10) | Y | FK ODL |
| LABORCODE | VARCHAR2(8) | N | Matricola |
| SITEID | VARCHAR2(8) | Y | PK |
| ORGID | VARCHAR2(8) | N | Organizzazione |

X4ESITOINT

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4ESITOINTID | NUMBER | Y | PK |
| IDINTERFACCIA | VARCHAR2(50) | N | Nome interfaccia |
| ESITO | VARCHAR2(10) | N | ESITO (Es. OK o KO) |
| COD_ESITO | VARCHAR2(30) | N | Codice d’errore |
| DESC_ESITO | VARCHAR2(2000) | N | Descrizione d’errore |
| UNIQUEID | VARCHAR2(50) | Y | Chiave d’integrazione per flusso |
| ROWSTAMP | VARCHAR2(40) | Y |  |
| DEST | VARCHAR2(50) | N | Sistema destinatario |
| SOURCE | VARCHAR2(50) | N | Sistema d’origine |
| PARENT | NUMBER | N | Parent è FK al campo X4ESITOINTID |
| START_ELAB | DATE | N | Data inizio elaborazione |
| END_ELAB | DATE | N | Data fine elaborazione |
| KAFKA_TOPIC | VARCHAR2(50) | N | Coda Kafka di elaborazione |
| MESSAGE | CLOB | N | Messaggio Json d’input. Viene salvato se necessario |

## Recupero/alimentazione da fonti esterne – Tagetik

Configurazioni Maximo

#### Flusso Import Capacity-CMO [perimetro 1]

Per la gestione del flusso di integrazione EAM-Tagetik relativo all’import dei dati di Capacity e Tariffe, sono state realizzate, tramite Maximo, due configurazioni DB (Oracle Sql) i cui modelli dati rispettano la struttura che segue:

X4CAPACITYDETAILS

| Campo |  | Tipo | Obbligatorio |
| --- | --- | --- | --- |
| X4CAPACITYDETAILSID |  | NUMBER | Y |
| DESCRIPTION |  | VARCHAR2(50 CHAR) | Y |
| X4CAPACITYID |  | NUMBER | Y |
| MESE |  | VARCHAR2(15 CHAR) | Y |
| ORE |  | FLOAT (126) | Y |
| HASLD |  | NUMBER | Y |
| CRAFT |  | VARCHAR2(8 CHAR) | Y |
| ROWSTAMP |  | VARCHAR2(40 CHAR) | Y |
|  |  |  |  |

X4CRAFTPERYEAR

| Campo |  | Tipo | Obbligatorio |
| --- | --- | --- | --- |
| X4CRAFTPERYEARID |  | NUMBER | Y |
| DESCRIPTION |  | VARCHAR2 (50 CHAR) | Y |
| CRAFT |  | VARCHAR2 (8 CHAR) | Y |
| HASLD |  | NUMBER | Y |
| UDB |  | VARCHAR2 (8 CHAR) | Y |
| PAYRATE |  | NUMBER (10,2) | Y |
| ANNO |  | NUMBER | Y |
| SCENARIO |  | VARCHAR2 (25 CHAR) | Y |
| ROWSTAMP |  | VARCHAR2 (40 CHAR) | Y |

Le tabelle appena descritte sono impiegate per il salvataggio a DB dei dati provenienti dall’endpoint Tagetik.

Tale endpoint viene configurato come oggetto endpoint Maximo. Esso verrà utilizzato all’interno dello script di automazione al fine di richiamare il sistema esterno per mezzo di Maximo. Per consentire l’invio di un modulo di request è inoltre necessaria la configurazione di due oggetti dialog titolati “Import Capacity” e “Import CMO”.

L’oggetto richiamato all’interno delle dialog che verranno mostrate in EAM per predisporre la finestra di inserimento dati di request (input) viene realizzato come oggetto non persistente – denominato “X4REQUESTTAGETIK” - all’interno del database di cui si riportano di seguito le configurazioni:

| Campo | Tipo | Obbligatorio |
| --- | --- | --- |
| CONTO | ALN(8) | Y |
| SCENARIO | ALN (20) | Y |
| TITRIS | ALN (8) | N |

Il campo “Conto” verrà automaticamente impostato dal sistema, sulla base dell’applicazione da cui l’utente evocherà l’integrazione: verrà popolato con il valore “Capacity” se l’import dei dati verrà richiesto mediante l’applicazione di gestione Capacity; verrà popolato con il valore “CMO” se l’import dei dati verrà richiamato mediante l’applicazione di gestione Tariffe.

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Sviluppi Maximo

#### Flusso Import Capacity-CMO [perimetro 1]

L’intera attività di importazione e ricalcolo dei dati viene eseguita mediante uno script di automazione Maximo titolato “X4TAGETIKTESTSCRIPT”. Si procede con lo stabilire una funzione di ricezione user input. L’utente finale dovrà, infatti, poter popolare i campi di request dinamica necessari all’estrazione dei dati da endpoint esterno esposto da Tagetik.

I camp che l’utente dovrà obbligatoriamente popolare sar: Scenario.

L’utente potrà scegliere di selezionare opzionalmente il campo Titris.

Il campo “Titris” rappresenta il campo di esemplificazione dell’Unità di Budget. Nella tabella Saldi Lordi di Tagetik, corrisponde al campo “Titolare Risorsa”.

Il campo “Scenario” sarà composto da una tipologia di dato alfanumerico che:

con i primi 4 digit esprimerà l’anno di riferimento, ossia l’anno in cui è stata realizzata l’analisi di scenario;

con i successivi 4 digit esprimerà la tipologia di scenario (OBDG, F10_, OPCL);

con gli ultimi 2 digit esprimerà l’anno l’anno di fase ossia l’anno per il quale è stata realizzata l’analisi di scenario.

Il campo (nella sua forma concatenata) corrisponde al campo “Scenario” nella tabella Saldi Lordi di Tagetik.

Il campo “Conto” servirà a filtrare la tipologia di dati di cui si vorrà produrre un’estrazione: Tariffe o Capacity. Nella tabella Saldi Lordi di Tagetik, corrisponde al campo “Conto”.

Una volta dichiarato l’indirizzo endpoint mediante oggetto Endpoint Maximo, ottenuta l’istanza MXServer ed aperta la connessione all’url indicato, si procede con la creazione del payload di richiesta dinamico che viene inviato, mediante OutputStreamWriter all’url di destinazione.

Ricevuta la risposta dal sistema esterno,si costruisca l’oggetto di response in formato JSON.

Si proceda con la selezione delle informazioni utili alla nostra integrazione costituendo, per ognuno dei dati interessanti, una singola variabile da inserire all’interno delle nostre due tabelle Oracle SQL (X4CAPACITYDETAILS e X4CRAFTPERYEAR)

Per i formati di request e di response, si considera lo swagger recepito in data 23/09/2024 e di cui riportiamo di seguito un esempio:

Request

{ "Scenario":"'2024ATC'", "Conto":"'Capacity'", "Titris":"'^$_W~cjU\\8D[p6Vww4.j{.^3ak4&4Y!OJ[q{$>Db3;#o:2`%a?C\"&qG71r7]&Q'" }

## Recupero/alimentazione da fonti esterne – SAP

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |

### Sviluppi Maximo not embedded

#### Configurazioni Maximo

Per poter leggere e scrivere oggetti di Maximo, verranno elencati i Object Structure:

| Object Structure | Descrizione |
| --- | --- |
| X4ESITOINT | Struct Object per scrivere esito elaborazione sulla tabella X4ESITOINT |
| X4ODM | Struct Object che definisce per accedere alla tabella delle strutture degli ordini di manutenzione. |
| X4NTK | Struct Object che definisce per accedere alla tabella delle strutture delle network. |
| X4WBE | Struct Object che definisce per accedere alla tabella delle strutture delle wbe. |
| X4WORKORDER | Struct Object che definisce per accedere alla tabella delle strutture degli ordini di lavoro. |
| X4ORDINT | Struct Object che definisce per accedere alla tabella delle strutture degli ordini di interni. |
| X4LABTRANS | Struct Object che definisce per accedere alla tabella LABTRANS. |

#### Flussi d’integrazioni

In questa sezione si prefigge di descrivere il flusso che intercorre tra EAM e il sistema SAP e le relative interazioni.

Di seguito diagramma di chiamate tra EAM e SAP:

#### Logica d’attivazione e tipologia d’interazione

Il diagramma illustrato precedentemente descrive le interazioni tra il sistema EAM e vari altri sistemi. EAM, mediante il connettore (rappresentato dalla linea rossa), invia a SAP gli elementi di rete, aggiornando le informazioni in base alla gerarchia di SAP.

In EAM, gli asset sono identificati tramite una chiave univoca chiamata AssetNum, mentre in SAP gli stessi elementi sono identificati attraverso un codice univoco denominato SAP Id. EAM mantiene il master dell'associazione tra le due chiavi. Ogni volta che viene creato un elemento gerarchico primo livello, come una o una (entrambe intese come sedi tecniche di livello), il connettore provvede a creare l'elemento corrispondente in SAP. Al contrario, ogni volta che un elemento di rete viene creato o modificato sulla piattaforma EAM, il connettore aggiorna l'elemento in SAP e, se necessario, aggiorna il master delle associazioni.

Per la creazione di ordini di manutenzione e network e la loro associazione agli ordini di lavoro, SAP mette a disposizione due API:

RichiestaCreazioneOdM

RichiestaCreazioneNTW

Queste API hanno lo scopo di generare un codice per gli ordini di manutenzione (OdM) o network (NTW) e inviarlo in risposta, permettendo così a EAM di salvarli nel proprio sistema e associarli agli ordini di lavoro per cui è stata fatta la richiesta.

Per poter invece disassociare un ordine di manutenzione o una network è possibile farlo attraverso questi flussi:

DisassociaOdM:

DisassociaNTK:

Entrambe queste funzionalità sono utilizzabili solo per OdL non ancora consuntivati.

Il flusso di InvioConsuntivi gestisce l'invio delle consuntivazioni delle risorse a SAP, sia parziali che totali, attraverso l'interfaccia dedicata "consuntivazione risorse". Per inviare le consuntivazioni a SAP (ControlloPeriodoContabile), è necessario che il periodo sia apert su SAP.

La funzione di Storno delle ore consente di annullare parzialmente o totalmente le ore di consuntivo registrate su SAP. Se l’utente effettua uno storno parziale, in background verrà prima effettuata una chiamata all’API SAP "CounterOdM" per recuperare i campi necessari alla successiva chiamata di storno.

Tramite procedure e routine periodiche (eseguite ogni ora) vengono effettuate:

Le acquisizioni dei dati contabili CBS/CBE (AcquisizioneDatiContabili)

Gli ordini interni da SAP (AcquisizioneOl)

Gli ordini di manutenzione da SAP (AcquisizioneOdM)

Le network da SAP (AcquisizioneNTW)

Le prime acquisizioni sono massive; le successive, mediante filtri, acquisiranno solo le modifiche avvenute su SAP dopo la richiesta precedente.

Di seguito lista dei flussi:

| Nome flusso | Tipologia d’integrazione | Tecnologia | Autenticazione |
| --- | --- | --- | --- |
| RichiestaCreazioneOdM | OSB | SOAP/XML | BasicAuth |
| RichiestaCreazioneNTW | OSB | SOAP/XML | BasicAuth |
| InvioConsuntivi/InvioConsuntiviParziali | OSB | SOAP/XML | BasicAuth |
| Storno | OSB | SOAP/XML | BasicAuth |
| CounterOdM | OSB | SOAP/XML | BasicAuth |
| DisassociazioneOdM | BWCE | SOAP/XML | OAuth2 |
| DisassociazioneNTW | BWCE | SOAP/XML | OAuth2 |
| ControlloPeriodoContabile | OSB | SOAP/XML | BasicAuth |
|  |  |  |  |
| AcquisizioneDatiContabili (CBS/CBE) | BWCE | SOAP/XML | OAuth2 |
| AcquisizioneOI | BWCE | SOAP/XML | OAuth2 |
| AcquisizioneOdM | BWCE | SOAP/XML | OAuth2 |
| AcquisizioneNTW | BWCE | SOAP/XML | OAuth2 |

Per ogni flusso verranno dettagliate le logiche di business implementate.

#### Flusso RichiestaCreazioneOdM

Ogni qualvolta sia necessario associare un Ordine di Manutenzione (OdM) a un Ordine di Lavoro (OdL) non precedentemente definito in SAP, l'utente può avviare la creazione dell'OdM tramite il pulsante "Richiesta creazione in SAP". A tale scopo, verrà implementato uno script di automazione denominato "X4CREATEODMSAP".

Questo script si occuperà di recuperare i campi pertinenti dell'ordine di lavoro corrente dalla tabella WORKORDER. Dopo aver ottenuto i dati necessari e averli convertiti utilizzando le chiavi coerenti al WSDL, lo script invierà una richiesta SOAP a SAP. In caso di esito positivo, SAP risponderà con il codice OdM generato per l'OdL.

Nel dettaglio lo script si occuperà di prelevare i dati dalla tabella WORKORDER e dalla tabella WPLABOR (nel caso di creazione dell’OdM, relazionata secondo la seguente join WORKORDER.WONUM=WPLABOR.WONUM AND SITEID=SITEID) o dalla tabella LABTRANS (nel caso di update del record di OdM) e rielaborarli per costruire il messaggio sotto esplicato.

Inoltre, nel caso di primo invio di richiesta creazione, lo script popolerà il nodo “Operazione” con la seguente logica: il CDL dell’OdM verrà posto uguale al record OWNERGROUP con la desinenza “_O” nel caso di Responsabile attività (OWNERGROUP) di tipo UI; verrà popolato invece con la desinenza “_I” nel caso di Responsabile attività (OWNERGROUP) di tipo non UI. Il tipo del responsabile attività verrà verificato nella tabella PERSONGROUP mediante il campo X4TIPOUDB (dove ci si sposterà per relazione PERSONGROUP.PERSONGROUP=WORKORDER.OWNERGROUP). Infine, il campo Trtyp verrà popolato con il valore “H”.

Nel caso degli invii successivi alla creazione (prima dell’invio del consuntivo), lo script popolerà il nodo dell’operazione con gli effettivi TITRIS delle risorse consuntivate. Nel dettaglio il CDL verrà calcolato sulla base della matricola inserita nel campo LABORCODE della riga di LABTRANS (la cui relazione con l’ordine è WORKORDER.WONUM=LABTRANS.REFWO and SITEID=SITEID). Le matricole saranno legate al TITRIS ma anche alla competenza (nella tabella LABORCRAFTRATE, verrà prelevato il campo CRAFT dove LABORCRAFTRATE.LABORCODE=LABTRANS.LABORCODE). Le righe delle operazioni create tramite il flusso di “UPDATE” preleveranno i campi direttamente dalla LABTRANS per fornire a SAP i dati corretti delle operazioni. Inoltre il campo Trtyp verrà popolato con “V”.

Una volta ricevuta la risposta da SAP, lo script aggiornerà i campi dell'ordine di lavoro nella tabella WORKORDER con il nuovo codice OdM.

La struttura SOAP messa a disposizione da SAP è la seguente:

| Campo interfaccia | Descrizione funzionale | Tipo | Campo DB | Obbligatorio | Note |
| --- | --- | --- | --- | --- | --- |
| <OdMMaintain> |  |  |  |  |  |
| Istanza | Id riguardante l'attività del centro operativo Id riguardante l'attività del centro operativo (nel TO-BE sarà popolato con l’ID EAM) | String 20 | WORKORDER.WONUM | Y |  |
| OdM | Identificativo univoco creato da SAP dell'oggetto contabile dell'Odm | String 12 | WORKORDER.X4ODM | N |  |
| Descrizione | Descrizione dell’OdM. Generata da |  | JOBPLAN.DESCRIPTION |  | SELECT DESCRIPTION FROM JOBPLAN WHERE JPNUM=:JPNUM and STATUS='ACTIVE' |
| InizioCardine | Date pianificate di Inizio attività | date yyyy/mm/dd | WORKORDER.TARGSTARTDATE | Y |  |
| FineCardine | Date pianificate di fine attività | date yyyy/mm/dd | WORKORDER.TARGCOMPDATE | Y |  |
| priorita | Priorità impostata | String 1 | WORKORDER.WOPRIORITY | Y |  |
| TipoAttivitaPM | Tipo attività inviata da sistema chiamate | String 3 | WORKORDER.X4TIPOATTIVITA | Y |  |
| TipoOrdine | Tipo ordine inviato da sistema chiamante | String 4 | WORKORDER.X4TIPOORDINE | Y |  |
| SedeTecnica | SAP ID dell’asset | String 30 | X4SAPIDASSET |  |  |
| Wbs | Campo inviato per le operazioni di network | String 24 | WORKORDER.X4WBE | Y |  |
| CdlResponsabile | Viene trascodificato in SAP e impostato nella testata ordine | String 8 | WORKORDER.OWNERGROUP | Y |  |
| <ScaricoCosti> |  |  |  |  |  |
| OrdineInterno | N. Ordine interno per cui creare OdM |  | WORKORDER.X4NORDINEINTERNO |  |  |
| WBS | WBS legata all'OI |  | ? |  |  |
| </ScaricoCosti> |  |  |  |  |  |
| <Operazioni> |  |  |  | Y |  |
| DataTarget | IN SAP non viene utilizzata | date yyyy/mm/dd |  | Y | Non popolata |
| Durata | Durata pianificata/prevista per l'operazione | double 5,1 |  | Y |  |
| <Personale> |  |  |  |  |  |
| CdL | es: operaio, impiegato, quadro |  | WPLABOR.X4TITRIS |  | (verrà popolato staticamente al primo invio con _O se odl tipo UI; con _I se odl tipo NON UI) WPLABOR.WONUM=WORKORDER.WONUM and WPLABOR.SITEID=WORKORDER.SITEID |
| OreLavoro |  | double 8,2 | WPLABOR.LABORHRS | Y | WPLABOR.WONUM=WORKORDER.WONUM and WPLABOR.SITEID=WORKORDER.SITEID |
| OreTrasferimento |  | double 8,2 | WPLABOR.X4ORETRASF | Y | WPLABOR.WONUM=WORKORDER.WONUM and WPLABOR.SITEID=WORKORDER.SITEID |
| </Personale> |  |  |  |  |  |
| </Operazioni> |  |  |  |  |  |
| </OdMMaintain> |  |  |  |  |  |
| Trtyp |  |  |  |  | Costante del messaggio. Pari ad H in caso di creazione, V in caso di modifica di un record esistente. |

per ogni chiamata può risponde con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore EAM fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

#### Flusso RichiestaCreazioneNTW

Ogni qualvolta sia necessario associare una Network (NTW) a un Ordine di Lavoro (OdL) non precedentemente definito in SAP, l'utente può avviare la creazione della NTW tramite il pulsante "Richiesta creazione in SAP". A tale scopo, verrà implementato uno script di automazione denominato "X4CREATENTWSAP".

Questo script si occuperà di recuperare i campi pertinenti dell'ordine di lavoro corrente dalla tabella WORKORDER. Dopo aver ottenuto i dati necessari e averli convertiti utilizzando le chiavi coerenti al WSDL, lo script invierà una richiesta SOAP a SAP. In caso di esito positivo, SAP risponderà con il codice NTW generato per l'OdL.

Nel dettaglio lo script si occuperà di prelevare i dati dalla tabella WORKORDER e dalla tabella WPLABOR (nel caso di creazione dell’OdM, relazionata secondo la seguente join WORKORDER.WONUM=WPLABOR.WONUM AND SITEID=SITEID) o dalla tabella LABTRANS (nel caso di update del record di NTK) e rielaborarli per costruire il messaggio sotto esplicato.

Inoltre, nel caso di primo invio di richiesta creazione, lo script popolerà il nodo “Operazione” con la seguente logica: il CDL della NTK verrà posto uguale al record OWNERGROUP con la desinenza “_O” nel caso di Responsabile attività (OWNERGROUP) di tipo UI; verrà popolato invece con la desinenza “_I” nel caso di Responsabile attività (OWNERGROUP) di tipo non UI. Il tipo del responsabile attività verrà verificato nella tabella PERSONGROUP mediante il campo X4TIPOUDB (dove ci si sposterà per relazione PERSONGROUP.PERSONGROUP=WORKORDER.OWNERGROUP). Infine, il campo Trtyp verrà popolato con il valore “H”.

Nel caso degli invii successivi alla creazione (prima dell’invio del consuntivo), lo script popolerà il nodo dell’operazione con gli effettivi TITRIS delle risorse consuntivate. Nel dettaglio il CDL verrà calcolato sulla base della matricola inserita nel campo LABORCODE della riga di LABTRANS (la cui relazione con l’ordine è WORKORDER.WONUM=LABTRANS.REFWO and SITEID=SITEID). Le matricole saranno legate al TITRIS ma anche alla competenza (nella tabella LABORCRAFTRATE, verrà prelevato il campo CRAFT dove LABORCRAFTRATE.LABORCODE=LABTRANS.LABORCODE). Le righe delle operazioni create tramite il flusso di “UPDATE” preleveranno i campi direttamente dalla LABTRANS per fornire a SAP i dati corretti delle operazioni. Inoltre il campo Trtyp verrà popolato con “V”.

Una volta ricevuta la risposta da SAP, lo script aggiornerà i campi dell'ordine di lavoro nella tabella WORKORDER con il nuovo codice NTW.

La struttura SOAP messa a disposizione da SAP è la seguente:

| Campo interfaccia | Descrizione funzionale | Tipo | Campo DB | Obbligatorio | Note |
| --- | --- | --- | --- | --- | --- |
| <NtwMaintain> |  |  |  |  |  |
| TipoNTW | Chiave univoca al fine di identificare il tipo network di EAM rispetto ad altri | String 4 |  | Y |  |
| Network | Codice numerico creato da SAP relativo al network utilizzato solo in fase di update | String 12 | WORKORDER. X4NTK | N |  |
| Wbs | Attributo fondamentale per le operazioni di network | String 24 | WORKORDER.X4WBE | Y |  |
| Descrizione | Descrizione di tipo statico stabilita da EAM | String 40 | JOBPLAN.DESCRIPTION | Y | SELECT DESCRIPTION FROM JOBPLAN WHERE JPNUM=:JPNUM and STATUS='ACTIVE' |
| CentroLavoro | Codice centro di lavoro | String 8 | WORKORDER. OWNERGROUP | Y |  |
| <Operazioni> | Oggetto che comprende infromazioni riguardo il centro di lavoro e il ciclo di lavoro |  |  |  |  |
| Istanza | Id riguardante l'attività del centro operativo | String 20 | WORKORDER.WONUM | Y |  |
| Operazione | Campo inviato per le operazioni di network | String 4 | WORKORDER.X4OPNTK | Y |  |
| IdAttivita | Id riguardante l'attività del centro di lavoro | String 20 | WORKORDER.JPNUM | Y |  |
|  |  |  |  |  |  |
| InizioCardine | Date pianificate di Inizio attività | date yyyy/mm/dd | WORKORDER.TARGSTARTDATE |  |  |
| FineCardine | Date pianificate di fine attività | Date yyyy/mm/dd | WORKORDER.TARGCOMPDATE |  |  |
| DataTarget | IN SAP non viene utilizzata | Date yyyy/mm/dd |  |  | Non popolato |
| Durata | Durata pianificata/prevista per l'operazione | Double 5,1 |  |  | calcolato = OreLavoro + OreTrasferimento |
| <Personale> |  |  |  |  |  |
| CdL | Centro di lavoro responsabile dell’operazione |  | WPLABOR.X4TITRIS |  | WPLABOR.WONUM=WORKORDER.WONUM and WPLABOR.SITEID=WORKORDER.SITEID |
| OreLavoro | Ore di lavoro pianificate per l'intervento | Double 8,2 | WPLABOR.LABORHRS |  | WPLABOR.WONUM=WORKORDER.WONUM and WPLABOR.SITEID=WORKORDER.SITEID |
| OreTrasferimento | Tempo di trasferimento dell'ordine | Double 8,2 | WPLABOR.X4ORETRASF |  | WPLABOR.WONUM=WORKORDER.WONUM and WPLABOR.SITEID=WORKORDER.SITEID |
| </Personale> |  |  |  |  |  |
| </Operazioni> |  |  |  |  |  |
| <NtwMaintain> |  |  |  |  |  |
| Trtyp |  |  |  |  | Per creazione sarà pari a H; V per modifica |

per ogni chiamata può risponde con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore EAM fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

#### Flusso InvioConsuntivi

Ogni volta che è necessario inviare le consuntivazioni a SAP, l'utente potrà farlo attraverso l'interfaccia “Consuntivazione Risorse”, selezionando le righe desiderate e cliccando sul pulsante “Invia a SAP” situato sulla sinistra.

Il click su questo pulsante attiverà il canale di pubblicazione “X4LABTRANS”, che invierà il contenuto della riga selezionata in EAM a una coda KAFKA dedicata, per la successiva elaborazione e invio del messaggio verso SAP.

A tale scopo è stata definita una coda kafka “send-labor-sap".

L’object structure per questa api sarà X4LABTRANS.

Il formato ottenuto in input sulla coda kafka sarà il seguente:

{

“laborcode”: “MROSSI”,

“regularhrs”: 5.0,

“startdate”: “2024-02-14T05:00:00+00:00”,

“labtransid”: “145”,

“refwo”: “1173”,

“siteid”: “SIT01”

}

Dopo aver ottenuto il payload, prima di procedere all'elaborazione dei campi, è necessario eseguire un passaggio nel flusso denominato “ControlloPeriodoContabile”. Se il controllo ha esito positivo, il modulo eam-integration procederà recuperando la riga corrispondente nella tabella WORKORDER utilizzando i campi “REFWO” e “SITEID” recuperati nel payload, con la seguente query:

SELECT * FROM WORKORDER

WHERE WORKORDER.WONUM = LABTRANS.REFWO

AND LABTRANS.SITEID = WORKORDER.SITEID;

Una volta ottenuti i dati, è possibile recuperare i campi necessari per generare la richiesta da inviare a SAP.

La struttura SOAP messa a disposizione da SAP è la seguente:

| Campo interfaccia | Descrizione funzionale | Tipo | Campo DB | Obbligatorio | Note |
| --- | --- | --- | --- | --- | --- |
| <ConfermaOre> | Nodo contentente... | ZmosSConfirm |  | Y |  |
| Istanza | Id riguardante l'attività del centro operativo | String 20 | WORKORDER.WONUM | Y |  |
| Network | Indentificativo Univoco Ntw di Sap se presente | String 12 | WORKORDER.NTK | Y | Obbligatorio nel caso in cui l’OdM non sia popolato |
| Operazione | Valorizzato nel caso passato il tipo network | String 4 | WORKORDER.XNTKOP | Y | Obbligatorio nel caso in cui l’OdM non sia popolato |
| Odm | Indentificativo Univoco ODM di Sap se presente | String 12 | WORKORDER.X4ODM | Y | Obbligatorio nel caso in cui la NTK non sia popolata |
| Approvatore | Matricola Terna Approvatore (che effettua l’invio) | String 12 | WORKORDER.CHANGEDBY | Y |  |
| <Wrktime> | Inizio nodo | ZmosSConfirm |  |  |  |
| Matricola | Codice Matricola consuntivata | String 12 | LABTRANS.LABORCODE | Y |  |
| OreLavoro | Ore di lavoro | Double 8,2 | LABTRANS.REGULARHRS | Y |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
| DataRegistrazione | Data del consuntivo | String 1 | LABTRANS.STARTDATE | Y |  |
| EAMKey | Chiave EAM | Double 8,2 | LABTRANS.LABTRANSID | Y |  |
| </Wrktime> |  | Date yyyy/MM/dd |  |  |  |
| </ConfermaOre> |  | Double 19,0 |  |  |  |
| Processo | OPEX, CAPEX, ANR | String 20 | WORKORDER.X4TIPOSPESA | Y |  |

SAP per ogni chiamata può rispondere con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore SAP fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo useremo il business Object Structure X4ESITOINT.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "SEND_LABOR_TO_SAP",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "SAP",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su SAP rilanciando la request verso SAP. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Flusso Storno

Per eseguire lo storno delle ore su SAP, l'utente può scegliere tra due modalità operative:

Storno parziale: Attraverso l'interfaccia utente (UI), l'utente può attivare manualmente un trigger per richiamare la funzione di storno da applicare a una singola riga di consuntivo.

Storno totale: L'utente può avviare una funzione laterale per stornare tutte le righe di consuntivo registrate e inviate verso SAP per un determinato ordine di lavoro.

Le due funzioni andranno a marcare l’oggetto EAM (rispettivamente la riga di LABTRANS per lo storno parziale o la riga di WORKORDER per lo storno totale) per essere evocati.

A supporto di questo processo è stata creata una coda Kafka chiamata "eam-hours-transfer-sap_t" (per lo storno totale) collegata al canale di pubblicazione "X4STORNO_T". Analogamente, la coda "eam-hours-transfer-sap_p" (per lo storno parziale) è collegata al canale di pubblicazione "X4STORNO_P".

Nel caso di storno totale di un ordine, l'Object Structure dedicata a questa API è X4WORKORDER.

Il payload inviato alla coda Kafka includerà i seguenti campi: (segue la descrizione dei campi nel dettaglio).

{

"IAufnr": " 23",

"IModEsecuzione": "E", //’E’ se effettivo ‘T’ se simulazione

"ITpStorno": "T", // P per parziale e T per totale

"Izzinstance": " 10012" //campo ‘WONUM’ da tabella WORKORDER se quindi storno TOTALE

}

Nella casistica di unparziale Object Structure dedicata per questa API è X4LABTRANS

Il formato del payload dell’oggetto ottenuto in input sulla coda kafka conterrà i seguenti campi:

{

"IAufnr": "11",

"IDatiConferme": {

"Rueck": "",

"Rmzhl": ""

},

"IModEsecuzione": "T", //’E’ se effettivo ‘T’ se simulazione

"ITpStorno": "P", // P per parziale e T per totale

"Izzinstance": " 10042" //Campo ’REFWO’ da tabella LABTRANS se torno parziale

}

La struttura SOAP messa a disposizione da SAP per entrambe le casistiche è la seguente:

| Dato | Descrizione funzionale | Tipo | Campo DB | Obbligatorio | Note |
| --- | --- | --- | --- | --- | --- |
| IAufnr | Numero Ordine SAP |  | Query se parte da LABTRANS (parziale)-> SELECT X4ODM FROM WORKORDER WHERE WONUM=IZzinstance AND SITEID='SIT01' X4ODM se parte da WORKORDER (totale) | Y |  |
| <IDatiConferme> | Inizio nodo... |  |  |  |  |
| Rueck | Numero di conferma |  | NA |  |  |
| Rmzhl | Contatore progressivo |  | NA |  |  |
| </IDatiConferme> |  |  |  |  |  |
| IModEsecuzione | Modalità esecuzione storno (se simulazione o reale) |  |  | Y |  |
| ITpStorno | Tipologia storno (totale o parziale) |  |  | Y |  |
| Izzinstance | Identificativo ordine di lavoro (istanza MAKE) |  | REFWO (se parte dalla LABTRANS - quindi storno parziale) WONUM (se parte da WORKORDER - quindi storno TOTALE) | Y |  |

SAP per ogni chiamata può rispondere con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore SAP fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In caso di KO, in funzione del CODICE_ERRORE si proverà a riconciliare l’operazione su EAM (rilanciare request).

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo verrà usato il business Object Structure X4ESITOINT.

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "SEND_HOURS_SAP",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "SAP",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su SAP rilanciando la request verso SAP. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Flusso AcquisizioneCBSCBE

Con cadenza oraria viene eseguita una procedura di acquisizione dei dati contabili (CBS/CBE). A tal fine, è stata creata una coda Kafka denominata 'eam-countable-data-acquisition-sap', collegata al canale di pubblicazione 'X4CBSCBE'. Questa coda viene attivata tramite un'escalation, anch'essa denominata 'X4CBSCBE', che è programmata per essere eseguita una volta ogni ora.

Una volta ricevuto il messaggio sulla coda invieremo una request di tipo SOAP verso SAP.

La struttura SOAP messa a disposizione da SAP è la seguente:

| Dato | Descrizione funzionale | Tipo | Campo DB | Obbligatorio | Note |
| --- | --- | --- | --- | --- | --- |
| statoOggetto | Stato dell’oggetto su SAP, sarà valorizzato solo nel primo infasamento | String | X4WBESTATOSISTEMASAP | Y | può essere APERTO, RIL o AUTO |
| tipoSpesa | CAPEX e filtra WBS/WBE "valide" che hanno "profit center" che termina con i caratteri "LIN", oppure ANR che filtra quelle "contabili valide" che hanno "profit center" che termina con "EPC". |  |  |  |  |
| dataCreazione | Data inizio del range dal quale recuperare i dati |  |  |  |  |
| dataModifica | Data fine del range dal quale recuperare i dati |  |  |  |  |

SAP in risposta ci invierà tutte le CBS/CBE modificate rispetto all’ultima data, e il sistema eam-integration salverà i dati all’interno del DB richiamando l’api della struct object X4WBE.

Di seguito la struttura SOAP di risposta SAP:

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| ID Elemento CBS (=CBE) | ID univoco della CBE | Y |  |
| ID CBS | Elemento WBS (identificatore all'interno della gerarchia) | N |  |
| Descrizione CBE | Descrizione testuale della CBE | Y |  |
| Descrizione CBS | Descrizione testuale della CBS | N |  |
| Stato sistema | Visualizza lo stato sistema | Y |  |
| Stato utente | Visualizza lo stato utente | Y |  |
| CDC Responsabile | Codice del centro di costo dell'elemento cbs responsabile dell'attività richiesta | Y |  |
| CdC richiedente | Codice del centro di costo dell'elemento cbs richiedente dell'attività richiesta | Y |  |
| Soc.richiedente | Società dell'elemento cbs richiedente dell'attività richiesta | N |  |
| Data creazione | Data di creazione della cbs | Y |  |
| Data ultima modifica | Data dell'ultima modifica della cbs | Y |  |
| Tipo progetto | Tipologia di progetto a cui appartiene la cbs | Y |  |
| Profit Center | Profit Center cbs | Y |  |
| Sede tecnica | Impianto a cui è collegata la cbs | Y |  |
| Motivo investimento | Indica un motivo di investimento | N |  |
| Cluster | Codice cluster a sostituzione del codice incentivazione | N |  |
| Cod. intervento | Codice intervento | N |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

SAP per ogni chiamata può rispondere con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore SAP fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo useremo il business Object Structure X4ESITOINT. L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "GET_COUNTABLE_DATA_FROM_SAP",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "SAP",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su SAP rilanciando la request verso SAP. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Flusso AcquisizioneOI

Con cadenza oraria viene eseguita una procedura di acquisizione degli ordini interni (OI). A tal fine, è stata creata una coda Kafka denominata eam-oi-acquisition-sap ', collegata al canale di pubblicazione X4ORDINT. Questa coda viene attivata tramite un'escalation, anch'essa denominata X4ORDINT, che è programmata per essere eseguita una volta ogni ora.

Una volta ricevuto il messaggio sulla coda invieremo una request di tipo SOAP verso SAP.

La struttura SOAP messa a disposizione da SAP è la seguente:

| Dato | Descrizione funzionale | Tipo | Campo DB | Obbligatorio | Note |
| --- | --- | --- | --- | --- | --- |
| statoOggetto | Stato dell’oggetto su SAP, sarà valorizzato solo nel primo infasamento | String | X4WBESTATOSISTEMASAP | Y | stato RIL |
| tipoOrdine | può essere T06 o T09 |  |  |  |  |
| dataCreazione | Data inizio del range dal quale recuperare i dati |  |  |  |  |
| dataModifica | Data fine del range dal quale recuperare i dati |  |  |  |  |

SAP in risposta ci invierà tutti gli OI modificati rispetto all’ultima data, e il sistema eam-integration salverà i dati all’interno del DB richiamando l’api della struct object X4ORDINT.

Di seguito la struttura SOAP di risposta SAP:

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| ID | Identificativo univoco dell'OI | Y |  |
| Descrizione | Descrizione OI | Y |  |
| Tipo ordine | Tipo OI | Y |  |
| Cat. Ordine | categoria ordine | Y |  |
|  |  |  |  |
|  |  |  |  |
| Stato sistema | Visualizza lo stato sistema | Y |  |
| Stato utente | Visualizza lo stato utente | Y |  |
| Società | Società | N |  |
| CDC Responsabile | Codice del centro di costo dell'elemento cbs responsabile dell'attività richiesta | Y |  |
|  |  |  |  |
| Codice Cliente | Codice Cliente | N |  |
| Centro di costo | Centro di costo per scarico costi | N |  |
| Data creazione | Data di creazione | Y |  |
| Data modifica | Data di ultima modifica | Y |  |
| Profit center | Profit center dell'OI | Y |  |
| Classe ogg. | Identifica la classe oggetto | N |  |

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo useremo il business Object Structure X4ESITOINT. L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "GET_OI_FROM_SAP",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "SAP",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su SAP rilanciando la request verso SAP. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Flusso AcquisizioneOdM

Con cadenza oraria viene eseguita una procedura di acquisizione degli ordini di manutenzione (OdM). A tal fine, è stata creata una coda Kafka denominata “eam-odm-acquisition-sap”, collegata al canale di pubblicazione X4ODM. Questa coda viene attivata tramite un'escalation, anch'essa denominata X4ODM, che è programmata per essere eseguita una volta ogni ora.

Una volta ricevuto il messaggio sulla coda invieremo una request di tipo SOAP verso SAP.

La struttura SOAP messa a disposizione da SAP è la seguente:

| Dato | Descrizione funzionale | Tipo | Campo DB | Obbligatorio | Note |
| --- | --- | --- | --- | --- | --- |
| tipoAttivitaPM | la tipologia di attività svolta | String | X4WBESTATOSISTEMASAP | Y |  |
| tipoOrdine | Tipo OdM |  |  |  |  |
| statoOggetto | Stato dell’oggetto su SAP, sarà valorizzato solo nel primo infasamento |  |  |  |  |
| dataCreazione | Data inizio del range dal quale recuperare i dati |  |  |  |  |
| dataModifica | Data fine del range dal quale recuperare i dati |  |  |  |  |

SAP in risposta ci invierà tutti gli OdM modificati rispetto all’ultima data, e il sistema eam-integration salverà i dati all’interno del DB richiamando l’api della struct object X4ODM.

Di seguito la struttura SOAP di risposta SAP:

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| ID | ID univoco dell'OdM | Y |  |
| Tipo ordine | Tipo OdM | Y |  |
| Tipo attività PM | Tipo attività | Y |  |
| Testo breve | descrizione ID Odm | Y |  |
| Stato sistema | Visualizza lo stato sistema | Y |  |
| Stato utente | Visualizza lo stato utente | Y |  |
| CLAvResp | Centro di lavoro responsabile dell'attività | Y |  |
| Impianto/sede Tecnica | Codice Impianto/sede tecnica e descrizione | N |  |
| Sede tecnica | Sede tecnica a cui è collegato l'OdM | Y |  |
| Operazione | Numero Operazione | Y |  |
| Desc.Operazione | Desc.Operazione | Y |  |
| Centro di costo responsabile | Centro di costo responsabile | Y |  |
| Centro di costo | Centro di costo per scarico costi | Y |  |
| Profit Center | Profit Center | Y |  |
| Autore/autore modifica | Autore creazione/modifica | Y |  |
| Data creazione | Data creazione ODM | Y |  |
| Data modifica | Data ultima modifica OdM | Y |  |

SAP per ogni chiamata può rispondere con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore SAP fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo useremo il business Object Structure X4ESITOINT. L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "GET_ODM_FROM_SAP",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "SAP",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su SAP rilanciando la request verso SAP. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Flusso AcquisizioneNTW

Con cadenza oraria viene eseguita una procedura di acquisizione delle network (NTW/NTK). A tal fine, è stata creata una coda Kafka denominata “eam-ntw-acquisition-sap”, collegata al canale di pubblicazione X4NTK. Questa coda viene attivata tramite un'escalation, anch'essa denominata X4NTK, che è programmata per essere eseguita una volta ogni ora.

Una volta ricevuto il messaggio sulla coda invieremo una request di tipo SOAP verso SAP.

La struttura SOAP messa a disposizione da SAP è la seguente:

| Dato | Descrizione funzionale | Tipo | Campo DB | Obbligatorio | Note |
| --- | --- | --- | --- | --- | --- |
| statoOggetto | Stato dell’oggetto su SAP, sarà valorizzato solo nel primo infasamento | String | X4WBESTATOSISTEMASAP | Y | status diverso da: bloccata \| chiusa |
| dataCreazione | Data inizio del range dal quale recuperare i dati |  |  |  |  |
| dataModifica | Data fine del range dal quale recuperare i dati |  |  |  |  |

SAP in risposta ci invierà tutte le network modificate rispetto all’ultima data, e il sistema eam-integration salverà i dati all’interno del DB richiamando l’api della struct object X4NTK.

Di seguito la struttura SOAP di risposta SAP:

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| ID | ID Univoco NTW | Y |  |
| Descrizione | Descrizione NTW | Y |  |
| Stato sistema | Visualizza lo stato sistema | Y |  |
| Stato utente | Visualizza lo stato utente | Y |  |
| Progetto | Progetto a cui è legata NTW | Y |  |
| Elemento CBS | Codice CBE a cui è legata | Y |  |
| Società | Società | N |  |
| Profit Center | Profit Center | Y |  |
| CdC Responsabile | = al centro responsabile presente sulla CBE | Y |  |
| Tipo di network | Tipo NTW | Y |  |
| Data creazione | Data creazione NTW | Y |  |
| Data modifica | Data ultima modifica NTW | Y |  |
| Autore creazione/modifica | Autore creazione/modifica | Y |  |
| Operazioni della NTW |  | NA |  |
| ID Operazione | N. Operazione della network | Y |  |
| Descrizione operazione | Descrizione Operazione | Y |  |
| Stato sistema | Visualizza lo stato sistema | Y |  |
| CDL resp | Centro di lavoro Responsabile | Y |  |
| Data creazione | Data creazione NTW | Y |  |
| Data modifica | Data ultima modifica NTW | Y |  |
| Autore creazione/modifica | Autore creazione/modifica | Y |  |

SAP per ogni chiamata può rispondere con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore SAP fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo useremo il business Object Structure X4ESITOINT. L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "GET_NTW_FROM_SAP",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "SAP",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su SAP rilanciando la request verso SAP. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Flusso DisassociazioneOdM

La funzionalità "Disassocia OdM" consente di modificare l'OdM associato all'OdL tramite il pulsante dedicato. Questa azione de-valorizza il campo "Identificativo OdM". Al ricevimento della notifica, l'OdM . La funzione è utilizzabile solo per gli OdL non ancora consuntivati in SAP. Come primo passo bisogna recuperare le informazioni da inviare a SAP per la . A tale scopo, verrà implementato uno script di automazione denominato "X4DISASSOCIAZIONEODM" che potrà essere avviato dall’utente cliccando manualmente sul pulsante “Disassocia OdM”.

Questo script recupererà i campi pertinenti dell'ordine di manutenzione dalla tabella ODM. Dopo aver ottenuto i dati necessari e averli convertiti, lo script invierà una richiesta SOAP a SAP.

La struttura SOAP messa a disposizione da SAP è la seguente:

| Dato | Descrizione funzionale | Tipo | Obbligatorio | Note |
| --- | --- | --- | --- | --- |
| Istanza/OdL | ID dell'ordine EAM da disassociare | String | Y |  |
| OdM | ID dell'OdM per cui si chiede la disassociazione | String | Y |  |

#### Flusso DisassociazioneNTW

La funzionalità "Disassocia NTK" permette di modificare la NTK associata all'OdL tramite il pulsante dedicato “Disassocia NTK”. Questa azione de-valorizza il campo "Identificativo NTK". Al ricevimento della notifica, la NTK in SAP. Questa funzione è utilizzabile solo per gli OdL non ancora consuntivati in SAP.A tale scopo, verrà implementato uno script di automazione denominato "X4DISASSOCIAZIONENTW" che potrà essere avviato dall’utente cliccando manualmente sul pulsante “Disassocia NTW”.

Questo script recupererà i campi pertinenti alla network dalla tabella NTW. Dopo aver ottenuto i dati necessari e averli convertiti, lo script invierà una richiesta SOAP a SAP.

La struttura SOAP messa a disposizione da SAP è la seguente:

| Dato | Descrizione funzionale | Tipo | Obbligatorio | Note |
| --- | --- | --- | --- | --- |
| Istanza/OdL | ID dell'ordine EAM da disassociare | String | Y |  |
| NTK | ID della NTK per cui si chiede la disassociazione | String | Y |  |
| Operazione | Numero ordine dell'operazione per cui si chiede la disassociazione | String | Y |  |

#### Flusso CounterOdM

Per comunicare uno storno parziale in SAP, è necessario identificare ID univoco della riga di consuntivo caricata sull’OdM. Il servizio esposto da SAP consente di ricavare questo identificativo, noto come counter, associato a un consuntivo, permettendo a EAM di procedere con la richiesta di storno effettiva.

Il flusso di integrazione CounterOdM viene invocato esclusivamente durante uno storno parziale, quando viene avviato il relativo flusso di Storno. Questo flusso di integrazione è necessario per abilitare il successivo flusso dedicato allo storno.

A tale scopo è stata definita un'API REST esposta su EAM che verrà invocata dal Flusso Storno.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/eam/...

Il Body della request sarà il seguente:

{

"ZmosWsResearchCounterOdm": {

"Aufnr": "",

"Zzinstance": "",

"Matricola": "",

"Isdd": "",

"Iedd": ""

}

}

Una volta ottenuto il payload, trasformeremo i campi per il corretto invio verso SAP con una request di tipo SOAP.

La struttura SOAP messa a disposizione da SAP è la seguente:

| Dato | Dato | Dato | Descrizione funzionale | Tipo | Capo DB | Obbligatorio | Note |
| --- | --- | --- | --- | --- | --- | --- | --- |
| <ZmosWsResearchCounterOdm> | <ZmosWsResearchCounterOdm> | <ZmosWsResearchCounterOdm> | Inizio request | NA |  | NA |  |
| Aufnr | Aufnr | Aufnr | Numero Ordine di manutenzione SAP | String | WORKORDER.X4ODM | Y |  |
| Zzinstance | Zzinstance | Zzinstance | ID Istanza (ODL) | Date | LABTRANS.STARTDATETIME | Y |  |
| Matricola | Matricola | Matricola | Codice Matricola consuntivata | Date | LABTRANS.FINISHDATETIME | Y |  |
| Isdd | Isdd | Isdd | Data inizio effettiva (formato YYYYMMDD) | String | LABTRANS.LABORCODE | Y |  |
| Iedd | Iedd | Iedd | Data fine effettiva (formato YYYYMMDD) | String | LABTRANS.REFWO | Y |  |
| </ ZmosWsResearchCounterOdm > | </ ZmosWsResearchCounterOdm > | </ ZmosWsResearchCounterOdm > | Fine request | NA |  |  |  |
|  |  | Response | Response | Response | Response | Response | Response |
| <Bapiret2> | <Bapiret2> | <Bapiret2> | Contiene l’esito della richiesta | NA |  |  |  |
| Type | Type | Type | Esito | String 1 |  | Y |  |
| Id | Id | Id | NA | String 20 |  |  |  |
| Number | Number | Number | NA | Int 3 |  |  |  |
| Message | Message | Message | Messaggio contenente l’esito | String 20 |  | Y |  |
| LogNo | LogNo | LogNo |  | String 20 |  | N |  |
| LogMsgNo | LogMsgNo | LogMsgNo | Contiene in risposta l’odm inviato in request se esito positivo | Int 6 |  | Y |  |
| MessageV1 | MessageV1 | MessageV1 | Contiene in risposta l’instanza inviata in request se esito positivo | String 50 |  | Y |  |
| MessageV2 | MessageV2 | MessageV2 | NA | String 50 |  |  |  |
| MessageV3 | MessageV3 | MessageV3 | NA | String 50 |  |  |  |
| Messagev4 | Messagev4 | Messagev4 | NA | String 50 |  |  |  |
| Parameter | Parameter | Parameter | NA | String 32 |  |  |  |
| Row | Row | Row | NA | Int |  |  |  |
| Field | Field | Field | NA | String 30 |  |  |  |
| System | System | System | NA | String 10 |  |  |  |
| </Bapiret2> | </Bapiret2> | </Bapiret2> |  |  |  |  |  |
| OData | OData | OData | Contiene i contatori delle conferme |  |  |  |  |
| IAufnr | IAufnr | IAufnr | Numero Ordine di manutenzione SAP | String 12 |  | N |  |
| IIedd | IIedd | IIedd | Data inizio effettiva (formato YYYYMMDD) | Date |  | N |  |
| IIsdd | IIsdd | IIsdd | Data fine effettiva (formato YYYYMMDD) | Date |  | N |  |
| IMatricola | IMatricola | IMatricola | Codice Matricola consuntivata | String 40 |  | N |  |
| IZzinstance | IZzinstance | IZzinstance | ID Istanza (ODL) | String 20 |  | N |  |
| Rueck | Rueck | Rueck | Numero di conferma | Int 10 |  | N |  |
| Rmzhl | Rmzhl | Rmzhl | Contatore progressivo | Int 8 |  | N |  |

SAP per ogni chiamata può rispondere con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore SAP fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In caso di KO eam-integration propagherà l’errore al chiamante (Storno). In modo da permettere allo schedulatore della rielaborazione di gestire l’intero processo dal punto di inizio.

#### Flusso ControlloPeriodoContabile

Per inviare le consuntivazioni a SAP, devono essere soddisfatte due condizioni essenziali:

Periodo contabile aperto su SAP.

Quando EAM elabora il flusso di "InvioConsuntivi", esegue un controllo sul periodo contabile per verificare . I campi IEsercizio e IPeriodo vengono estratti dal campo "startdate", presente nel payload della coda Kafka “eam-send-labor-sap”. Nello specifico:

IEsercizio corrisponde all'anno della "startdate".

IPeriodo corrisponde al mese della "startdate".

Il campo Isocietà, invece, viene recuperato tramite una query SQL dal campo WORKORDER.X4BURKS utilizzando i seguenti parametri:

SELECT X4BURKS FROM WORKORDER W

WHERE W.WONUM = <LABTRANS.REFWO>

AND W.SITEID = <LABTRANS.SITEID>

Questa query associa l'ordine di lavoro al contesto aziendale corretto. Infine, per completare il processo, EAM utilizza una struttura SOAP fornita da SAP. (Segue la descrizione della struttura SOAP).

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| IEsercizio | Anno esercizio | Y | YEAR(LABTRANS.STARTDATE) |
| Iperiodo | Periodo dell'esercizio | Y | MONTH(LABTRANS.STARTDATE) |
| Isocieta | Società | Y | SELECT X4BURKS FROM WORKORDER W WHERE W.WONUM=<LABTRANS.REFWO> AND W.SITEID=<LABTRANS.SITEID> |
| ITpConto | TBD |  |  |

SAP per ogni chiamata può rispondere con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore SAP fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In caso di KO per errore di comunicazione con SAP, il modulo eam-integration proverà a rieffettuare la chiamata per un numero prestabilito di volte, dopo il quale in caso non si riceva esito positivo, il check restituirà al chiamante (InvioConsuntivi) l’esito negativo.

|  |  |  |  |  |  |
| --- | --- | --- | --- | --- | --- |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |

#### Schedulatore per Rielaborare i messaggi in errore

Nei flussi SAP, per il quale si è riscontrato un errore applicativo/comunicazione si provvede a realizzare un motore che rilancerà per n-volte il messaggio a SAP nel tentativo di ottenere un esito positivo nella comunicazione. Il numero di tentativi dev’essere configurabile attraverso variabili d’ambiente di Open Shift.

Il motore sfrutterà i dati presenti sulla tabella X4ESITOINT cercando i record che hanno ESITO = KO, PARENT = null e SOURCE = EAM.

Per ogni record individuato si provvederà ad acquisire il dato presente in MESSAGE ed inserirlo nell’opportuna coda kafka in modo da rilanciare l’elaborazione (campo KAFKA_TOPIC).

#### Modello Relazionale

Di seguito verranno riportate le tabelle usate all’interno dei flussi di sprint 3 con i relativi dettagli:

WORKORDER:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| WONUM | VARCHAR2(10) | Y | Numero identificativo dell'ordine di lavoro |
| SITEID | VARCHAR2(8) | Y | Sede dell'ordine di lavoro |
| DESCRIPTION | VARCHAR2(100) | N | Descrizione dell'ordine di lavoro |
| STATUS | VARCHAR2(16) | Y | Stato OdL |
| JPNUM | VARCHAR2(12) | N | Identificativo univoco dell'ID del PdL associato all'OdL |
| ASSETNUM | VARCHAR2(25) | N | Identificativo univoco della sede tecnica su cui effettuare l'ordine di lavoro |
|  |  |  |  |
|  |  |  |  |
| TARGSTARTDATE | DATE | N | Data di inizio lavoro PIANIFICATA |
| TARGFINISHDATE | DATE | N | Data di fine lavoro PIANIFICATA |
|  |  |  |  |
|  |  |  |  |
| SNECONSTRAINT | DATE | N | Inizia non prima di |
| FNLCONSTRAINT | DATE | N | Data massima esecuzione |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
| ESTDUR | FLOAT | N | Durata OdL |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
| ACTSTART | DATE | N | Data di inizio lavoro effettiva |
| ACTFINISH | DATE | N | Data di fine lavoro effettiva |
| X4TIPOSPESA | VARCHAR2(5) | N | Tipo di spesa (CAPEX, OPEX, ANR) |
| X4COIN | VARCHAR2(15) | N | ID Coin |
| X4WBE | VARCHAR2(20) | N | WBE OdL |
| X4WBEPIAN | VARCHAR2(20) | N | WBE Pianificata OdL |
| X4WBS | VARCHAR2(20) | N | WBS OdL |
| X4TIPOATTIVITA | VARCHAR2(15) | N | Tipo attività contabile |
| X4TIPOORDINE | VARCHAR2(15) | N | Tipo ordine contabile |
| X4ODM | VARCHAR2(20) | N | ID ODM SAP |
| X4NTK | VARCHAR2(20) | N | ID NETWORK SAP |
|  |  |  |  |
| X4NORDINEINTERNO | VARCHAR2(20) | N | ID ORDINE INTERNO SAP |
| WOPRIORITY | NUMBER | N | Priorità OdL |
| EXTERNALREFID | VARCHAR2(10) | N | Chiave sistema esterno |
| SCHEDSTART | DATE | N | Data inizio schedulazione |
| SCHEDFINISH | DATE | N | Data fine schedulazione |
| PREDECESSORWOS | VARCHAR2(256) | N | Negli OdL aggregati, indica la sequenza degli OdL |
| LEAD | VARCHAR2(100) | N | Preposto |
| X4SAPIDASSET | VARCHAR2(30 CHAR) |  | SapId dell’asset |

X4ESITOINT

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4ESITOINTID | NUMBER | Y | PK |
| IDINTERFACCIA | VARCHAR2(50) | N | Nome interfaccia |
| ESITO | VARCHAR2(10) | N | ESITO (Es. OK o KO) |
| COD_ESITO | VARCHAR2(30) | N | Codice d’errore |
| DESC_ESITO | VARCHAR2(2000) | N | Descrizione d’errore |
| UNIQUEID | VARCHAR2(50) | Y | Chiave d’integrazione per flusso |
| ROWSTAMP | VARCHAR2(40) | Y |  |
| DEST | VARCHAR2(50) | N | Sistema destinatario |
| SOURCE | VARCHAR2(50) | N | Sistema d’origine |
| PARENT | NUMBER | N | Parent è FK al campo X4ESITOINTID |
| START_ELAB | DATE | N | Data inizio elaborazione |
| END_ELAB | DATE | N | Data fine elaborazione |
| KAFKA_TOPIC | VARCHAR2(50) | N | Coda Kafka di elaborazione |
| MESSAGE | CLOB | N | Messaggio Json d’input. Viene salvato se necessario |

LABTRANS

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| STARTDATETIME | DATE | N | Data fine effettiva (formato YYYYMMDD) |
| REGULARHRS | FLOAT | S | Ore di lavoro pianificate per l'intervento |
| FINISHDATETIME | DATE | N | Data inizio effettiva (formato YYYYMMDD) |
| LABORCODE | VARCHAR2(8 CHAR) | S | Codice Matricola consuntivata |
| REFWO | VARCHAR2(25 CHAR) | N | ID Istanza (ODL) |
| LABTRANSID | NUMBER | S | Chiave EAM |

X4ODM

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4ODMID | NUMBER | S | ID univoco dell'OdM |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | descrizione ID Odm |
| HASLD | NUMBER | S |  |
| TIPOATTIVITA | VARCHAR2(20 CHAR) | N | Tipo attività |
| TIPOORDINE | VARCHAR2(20 CHAR) | N | Tipo OdM |
| STATOSISTEMA | VARCHAR2(8 CHAR) | N | Visualizza lo stato sistema |
| STATOUTENTE | VARCHAR2(8 CHAR) | N | Visualizza lo stato utente |
| SEDETECNICA | VARCHAR2(30 CHAR) | N | Codice Impianto/sede tecnica e descrizione |
| CDCRESP | VARCHAR2(15 CHAR) | N | Centro di costo responsabile |
| CDCCOSTI | VARCHAR2(15 CHAR) | N | Centro di costo per scarico costi |
| PROFITCENTER | VARCHAR2(15 CHAR) | N | Profit Center |
| CREATEDBY | VARCHAR2(20 CHAR) | N | Autore creazione/modifica |
| CHANGEDDATE | DATE | N | Data ultima modifica OdM |
| CDLRESP | VARCHAR2(15 CHAR) | N | Centro di lavoro responsabile dell'attività |
| CREATEDDATE | DATE | N | Data creazione ODM |

X4NTK

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| IDNTK | NUMBER | S | ID Univoco NTW |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione NTW |
| IDNTK | VARCHAR2(20 CHAR) | N | ID Univoco NTW |
| WBE | VARCHAR2(20 CHAR) | N | Codice CBE a cui è legata |
| NOPERAZIONE | VARCHAR2(20 CHAR) | N | N. Operazione della network |
| STATOSISTEMASAP | VARCHAR2(8 CHAR) | N | Visualizza lo stato sistema |
| STATOUTENTESAP | VARCHAR2(8 CHAR) | N | Visualizza lo stato utente |
| PROGETTO | VARCHAR2(20 CHAR) | N | Progetto a cui è legata NTW |
| SOCIETA | VARCHAR2(15 CHAR) | N | Società |
| PROFITCENTER | VARCHAR2(15 CHAR) | N | Profit Center |
| TIPO | VARCHAR2(10 CHAR) | N | Tipo NTW |
| CREATEDATE | DATE | N | Data creazione NTW |
| CHANGEDDATE | DATE | N | Data ultima modifica NTW |
| CHANGEDBY |  |  | Autore creazione/modifica |

X4ODMOP

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| NOOPERAZIONE / IDOPERAZIONE | VARCHAR2(5 CHAR) | N | Numero Operazione |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Desc.Operazione |

X4OPNTK

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| IDOPERAZIONE | VARCHAR2(5 CHAR) | N | N. Operazione della network |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione Operazione |
| STATO | VARCHAR2(8 CHAR) | N | Visualizza lo stato sistema |
| CDLRESP | VARCHAR2(15 CHAR) | N | Centro di lavoro Responsabile |
| CREATEDATE | DATE | N | Data creazione NTW |
| CHANGEDDATE | DATE | N | Data ultima modifica NTW |
| CHANGEDBY | VARCHAR2(20 CHAR) | N | Autore creazione/modifica |

X4WBE

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| IDWBE | VARCHAR2(20 CHAR) | N | ID univoco della CBE |
| IDWBS | VARCHAR2(20 CHAR) | N | Elemento WBS (identificatore all'interno della gerarchia) |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione testuale della CBE |
| STATOSISTEMASAP | VARCHAR2(8 CHAR) | N | Visualizza lo stato sistema |
| STATOUTENTESAP | VARCHAR2(8 CHAR) | N | Visualizza lo stato utente |
| CDCREP | VARCHAR2(15 CHAR) | N | Codice del centro di costo dell'elemento cbs responsabile dell'attività richiesta |
| CDCRICH | VARCHAR2(15 CHAR) | N | Codice del centro di costo dell'elemento cbs richiedente dell'attività richiesta |
| SOCRICH | VARCHAR2(15 CHAR) | N | Società dell'elemento cbs richiedente dell'attività richiesta |
| CREATEDATE | DATE | N | Data di creazione della cbs |
| CHANGEDATE | DATE | N | Data dell'ultima modifica della cbs |
| TIPOPROG | VARCHAR2(15 CHAR) | N | Tipologia di progetto a cui appartiene la cbs |
| PROFITCENTER | VARCHAR2(15 CHAR) | N | Profit Center cbs |
| SEDETECNICA | VARCHAR2(30 CHAR) | N | Impianto a cui è collegata la cbs |
| MOTINVESTIMENTO | VARCHAR2(50 CHAR) | N | Indica un motivo di investimento |
| CODINCENTIVAZIONE | VARCHAR2(50 CHAR) | N | Codice incentivazione (non direttamente indicato in SAP, Trascodifica di motivo investimento) |
| CODINTERVENTO | VARCHAR2(50 CHAR) | N | Codice intervento |

WPLABOR

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4TITRIS | VARCHAR2(10 CHAR) |  |  |
| LABORHRS | FLOAT |  |  |
| X4ORETRASF | FLOAT |  |  |

JOBPLAN

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| DESCRIPTION | VARCHAR2(150 CHAR) |  |  |

## Esecuzione su Maximo Mobile

La gestione degli ordini di lavoro funzionalmente prevede alcuni step da effettuare mediante applicazione mobile, in particolare per il tracciamento delle rilevazioni su campo effettuate durante l’intervento. Per permettere la gestione di queste entità ed il loro tracciamento è dunque necessario personalizzare l’applicazione “Maximo Mobile” in modo da poterne permettere la gestione.

### Configurazioni

Per permettere la gestione di asset, ordini di lavoro, difformità/interferenze e micro-controlli in Maximo Mobile, si prevedono le seguenti configurazioni a sistema:

Operatività Maximo Mobile

L’utente ha la possibilità di visualizzare gli OdL dal momento in cui sono in stato “INOLTRATO” in “MySchedules” e può visualizzare tutti gli OdL (in qualsiasi stato e assegnati a qualsiasi operatore) associati a uno specifico asset in “Asset”. In questo stato, l’utente può, tramite la piattaforma, registrare nuove difformità o confermare l’esecuzione di microcontrolli.

Per permettere la visualizzazione mirata tramite app mobile degli ordini di lavoro è stato necessario configurare la struttura oggetto responsabile della visualizzazione dei dati nell’app (X4MXAPIWODETAIL) modificandone le query rese disponibili al proprio interno. Nel dettaglio la query standard è stata impostata per rilevare tutti gli OdL in stato “INOLTRATO”.

Modifica datasources asset e ordini di lavoro

Per permettere la visualizzazione degli attributi custom delle entità ASSET e WORKORDER (e le tabelle a loro associate), i datasources denominati ASSETS e WODETAILS sono stati espansi in modo da poter recepire anche le informazioni degli attributi custom.

È stato inoltre modificato il datasource ASSET permettendo di poter effettuare i filtri di ricerca sugli asset sul campo X4SAPID.

Creazione nuovi datasources per gestione difformità e interferenze

Per permettere la visualizzazione delle difformità/interferenze già aperte sugli asset oggetto dell’ordine di lavoro e per permettere all’utente di poter creare delle nuove difformità/interferenze a sistema, è stato necessario creare dei nuovi datasources (cAnomalieDs e cNewAnomalieDs).

|  |  |
| --- | --- |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |

|  |  |
| --- | --- |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |

Il primo relazionato a partire dalla tabella ASSET, che preleva tutte le interferenze e difformità aperte sull’asset selezionato (o sui figli dell’asset selezionato), mentre il secondo che, invece, permette di generare una nuova difformità/interferenza a partire dall’ordine di lavoro (a partire dalla tabella WORKORDER, permette di creare un nuovo record nella tabella X4DIFFOR_WO)

Modifica app “Assets” per visualizzazione difformità

L’app “Assets” (che permette la gestione degli asset) è stata configurata in modo tale da ramite opportuno pulsante, aprire la lista di difformità che insistono sullo stesso. Per far ciò è stato necessario estendere il datasource dell’ASSET inserendo anche le tabelle “X4DIFFORSTAT” e “X4INTERFSTAT” (con entrambi relazioni uguali ad :ASSETNUM=ASSETNUM and SITEID=:SITEID). Includendo una “data-list” all’interno dell’applicazione ASSETS che come data-source ha le datasources sopra citate, è stato possibile estendere graficamente l’applicazione per contenere anche queste liste.

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

Modifica app “My schedules” per gestione micro-controlli

L’app “My schedules” (che permette la gestione degli ordini di lavoro) è stata configurata in modo tale da, tramite opportuno pulsante, aprire la lista dei micro-controlli all’interno dell’ordine di lavoro. La vista dei micro-controlli è stata revisionata e modificata opportunamente in modo tale da avere:

una vista ordinata dei microcontrolli per tipologia di asset;

le informazioni dell’asset su cui insiste ogni microcontrollo (codice dell’asset, classificazione dell’asset, SAPID se presente e codice dell’asset padre);

la possibilità di filtrare la lista dei micro-controlli per tipologia di micro-controllo oppure per classificazione dell’asset a cui si applica il micro-controllo tramite la textbar di ricerca

la possibilità di registrare l’esecuzione dei microcontrolli utilizzando l’apposito pulsante di “check” che cambierà lo stato del micro-controllo in completato.

### Sviluppi

Per permettere la gestione delle difformità/interferenze in Maximo Mobile, si prevedono i seguenti sviluppi a sistema:

Modifica app “My schedules” per vista anomalie già registrate

All’interno dell’app “My schedules” deve essere riportata una vista tabellare delle difformità e interferenze già registrate sull’asset (o sui figli dell’asset), creata a partire dal datasource cAnomalieDs, che mostri all’utente quali delle difformità e interferenze sono attualmente presenti. Tramite opportuno sarà inoltre possibile permettere la conferma della presenza dell’anomalia registrata in passato.

Modifica app “My schedules” per generazione nuova difformità/interferenza

All’interno dell’app “My schedules” - mediante opportuno pulsante - sarà possibile aprire una dialog che permetterà la registrazione di una nuova difformità. Per registrare la nuova difformità sarà necessario, come per la versione web, specificare l’asset per cui si registra l’anomalia, il codice dell’anomalia e la gravità se presente. Questo inserimento viene effettuato utilizzando il datasources cNewAnomalieDs.

Alla chiusura della dialog, il sistema genererà una chiamata all’API esposta da EAM (se presente la connessione), contenente le informazioni selezionate a schermo dall’utente ed il record verrà creato all’interno del sistema.

Nel caso in cui l’operatore inserisca una difformità già presente sull’asset, il sistema mostrerà il campo di conferma “Difformità già presente su asset, procedere comunque con ?”; se l’operatore prosegue, il campo “Note” di entrambe le difformità verrà reso disponibile all’utente.

…

Modifica app “My schedules” per generazione nuova difformità/interferenza per microcontrolli

All’interno dell’app “My schedules” nella sezione dei microcontrolli - mediante opportuno pulsante - sarà possibile aprire una dialog che permetterà la registrazione di una nuova difformità. Per registrare la nuova difformità sarà necessario, come per la versione web, specificare l’asset per cui si registra l’anomalia, il codice dell’anomalia e la gravità se presente. Questo inserimento viene effettuato utilizzando il datasources cNewAnomalieDs. Alla chiusura della dialog, il sistema generarà una chiamata all’API esposta da EAM (se presente la connessione), contenente le informazioni selezionate a schermo dall’utente ed il record verrà creato all’interno del sistema.

difformità Maximo Mobile

La tabella di difformità già presenti sull’asset, visionabile all’interno dell’ordine di lavoro, è stata configurata per contenere al suo interno un pulsante denominato ?”.

Questo pulsante - quando viene posto cliccato - permetterà .

Per permettere la gestione della piccola manutenzione in Maximo mobile, si prevede il seguente sviluppo a sistema:

Gestione della piccola manutenzione Maximo Mobile

Contestualmente al momento di una nuova rilevazione di difformità sarà possibile porre un flag al campo “Risolto contestualmente” del record. Quando l’OdL in cui è stata effettuata questa rilevazione passa in stato CHIUSO, verrà creato automaticamente dal sistema un ordine di lavoro sull’asset su cui è stato effettuato l’ordine di lavoro e verrà impostato il JPNUM dell’ordine con il piano di lavoro che permette di risolvere la difformità selezionata. Questo OdL, una volta creato, verrà inoltre chiuso contestualmente. Le date di pianificazione saranno lasciate vuote ma sarà popolata la data di esecuzione con la data di generazione dell’ordine stesso.

## Gestione difformità/interferenze

La gestione difformità/interferenze prevede un’operatività dell’utente sia sull’applicativo IBM MAS Web, sia sulla sua parte Mobile (come visto al paragrafo precedente). Nel dettaglio, in questo capitolo sono riportati gli stati assunti dalle difformità/interferenze e le loro logiche di visualizzazione e conferma/non conferma.

### Configurazioni

Per la gestione dei dati inerenti alle difformità rilevate è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito viene riportato in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4DIFFOR_WO | ASSETNUM | Asset | UPPER | 25 |
| X4DIFFOR_WO | DIFFORMITA | Difformità | ALN | 0 |
| X4DIFFOR_WO | GRAVITA | Gravita | ALN | 50 |
| X4DIFFOR_WO | STAT | Stato | ALN | 50 |
|  |  |  |  |  |
| X4DIFFOR_WO | WONUM | Ordine di lavoro | UPPER | 10 |
| X4DIFFOR_WO |  |  | ALN | 0 |
| X4DIFFOR_WO | VALMISURA | Valore misura |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| X4CATDIFFOR | TIPOLOGIAASSET | Tipologia Asset | UPPER | 192 |
| X4CATDIFFOR | DESCRIPTION | Descrizione difformità | ALN | 250 |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

Per permettere la visione dei campi inerenti alle difformità rilevate e agli OdL associati, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione Custom “”, si dovranno configurare:

I campi della sezione “Difformità rilevate”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Difformità | DIFFORMITA |  | X4IDDIFFORMITA |  |
| Descrizione difformità | X4CATDIFFOR.DESCRIPTION |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Asset | ASSET |  |  |  |
| Descrizione asset | ASSET.DESCRIPTION |  |  |  |
| Gravità | GRAVITA |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Descrizione misura |  |  |  |  |
| Valore misura | VALMISURA |  |  |  |
|  |  |  |  |  |
| OdL risolutore | ODLRISOLUTORE |  |  |  |
| Stato | STAT |  |  |  |
| Note | DESCRIPTION_LONGDESCRIPTION |  |  |  |
| Causale | CAUSALE |  |  |  |
| Matricola | CHANGEBY |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

er

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |

Per permettere la visione dei campi necessari alla registrazione di nuove difformità e misure, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “WOTRACK_D” come duplicato dell’applicazione standard “WOTRACK”, si dovranno configurare:

I campi di testata del tab “Difformità e Interferenze”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Ordine di lavoro | WONUM | DESCRIPTION |  |  |

I campi della sezione “Difformità” caratterizzata dalla relazione “X4DIFFORMITAWO”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Asset | ASSETNUM |  | ASSET |  |
|  |  |  |  |  |
| Difformità | DIFFORMITA |  | X4DIFFORMITAWO |  |
|  |  |  |  |  |
| Gravità | GRAVITA |  | X4GRAVITADIFFWO |  |
| Stato | STAT |  |  |  |
| Fase | FASE |  | VALUELIST |  |
| Asset di quarto livello | X4CATDIFFORMITA.TIPOLOGIAASSET |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

INTERFERENZE

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

La lookup ASSET permetterà la selezione manuale solamente dell’asset associato all’OdL, di quelli gerarchicamente sottostanti e quelli adiacenti (quest’ultimo filtro è valido solo per JOBPLAN relativi ad asset classificati come CAMPATA AEREA, dove quelli adiacenti saranno di classificazione SOSTEGNO AEREO).

La lookup X4DIFFORMITAWO permetterà la selezione manuale solamente delle difformità dell’asset e dell’attività in oggetto

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

I campi della sezione “Misure” caratterizzata dalla relazione “X4MISUREWO”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
| Misura | IDMISURA |  |  |  |
| Valore misura | VALUE |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

Tutte le configurazioni presentate saranno replicate anche per le interferenze.

### Sviluppi

Per permettere la funzionalità di cambio stato delle difformità/interferenze, si prevede il seguente sviluppo a sistema:

Funzionalità di cambio stato difformità

L'oggetto custom delle difformità rilevate è stato configurato in modo da gestire al suo interno anche lo stato. Nel dettaglio, una difformità può assumere i seguenti stati: DA VALIDARE: stato assunto di default al momento della registrazione di una difformità; gli stati successivi possono essere NON VALIDATA o VALIDATA.

VALIDATA: stato impostato dall’utente mediante la funzionalità “cambia stato” (puntualmente o massivamente tramite checkbox) o automaticamente dal sistema al superamento della data di massima validazione (TEMPOVALID) presente nella tabella X4CATDIFFOR; gli stati successivi possono essere SUPERATA o RISOLTA.

NON VALIDATA: stato impostato dall’utente mediante la funzionalità “cambia stato” (puntualmente o massivamente tramite checkbox); inoltre, l’utente dovrà obbligatoriamente inserire la motivazione nel campo note “DESCRIPTION_LONGDESCRIPTION”

RISOLTA: stato impostato automaticamente dal sistema quando viene chiuso un OdL risolutore di una data difformità, oppure un OdL con attività prevalente rispetto a quella risolutrice

SUPERATA: stato impostato automaticamente dal sistema nel caso in cui non venga più rilevata la difformità sul campo (flag CONFERMATO passa da 1 a 0 nel tab Difformità e Interferenze) o manualmente se viene eliminata la difformità dal catalogo o l’attività risolutiva non viene eseguita.

Validazione automatica

Un’escalation di sistema verificherà tutte le difformità (tabelle X4DIFFOR_WO) in status “DA VALIDARE” una volta al giorno. Se la data di registrazione della difformità è superiore alla sysdate sommata al valore di TEMPOVALID della difformità (dove il campo viene recuperato con la seguente relazione IDDIFFORMITA=IDDIFFORMITA), allora tramite action X4VALDIFF viene cambiato lo stato in “VALIDATA”.

Gestione del campo CAUSALE

Il campo CAUSALE della tabella X4DIFFOR_WO dovrà essere reso obbligatorio dal sistema e valorizzabile dall’utente tramite VALUELIST (“Non esecuzione del controllo”, “Eliminazione a catalogo”) a seguito del passaggio di stato manuale di una difformità in SUPERATA. Inoltre, tale campo verrà popolato automaticamente mediante automation script (“Non conferma”) nel caso il flag CONFERMATO di una specifica difformità passi da 1 a 0.

Per permettere la visualizzazione delle difformità prefiltrate in base allo stato e alla UdB di competenza, si prede il seguente sviluppo a sistema:

Filtraggio Difformità rilevate

I record della applicazione X4DIFFOR_WO saranno prefiltrati mediante script di automazione in base alle seguenti logiche:

Il campo STATO della tabella X4DIFFOR_WO dovrà essere uguale a “DA VALIDARE”

Il campo OWNERGROUP (della tabella WORKORDER) degli OdL che hanno rilevato le difformità dovrà essere uguale all’UdB di competenza dell’utente.

Per permettere l’auto-compilazione dei campi inerenti alle difformità rilevate, si prevede il seguente sviluppo a sistema:

Auto-compilazione campi difformità rilevate

A seguito di un ordine di lavoro contenente al suo interno un PdL definito risolutore di una determinata difformità, il campo “OdL risolutore” (ODLRISOLUTORE della difformità di un asset – tabella X4DIFFORSTAT) dell’applicazione “Difformità rilevate” verrà automaticamente popolato con il campo WONUM di tale OdL. Questo automatismo verrà triggerato alla chiusura degli ordini, verrà effettuato un controllo sul PdL impostato nell’ordine di lavoro e se ne verificherà che l’attività risolva difformità. Successivamente verranno verificate le difformità aperte sull’asset oggetto dell’ordine di lavoro e tutte le difformità aperte sull’asset che corrispondono all’elenco delle difformità risolte dall’attività verranno automaticamente impostate in stato RISOLTA.

Inoltre, il campo “Matricola” dell’applicazione Difformità Rilevate verrà automaticamente gestito dal sistema come segue: al cambio stato manuale triggerato dall’operatività utente, il sistema ne registrerà il codice matricola responsabile della modifica all’interno del campo.

Infine, il campo “Descrizione misura” verrà popolato automaticamente dal sistema in base alla relazione tra la difformità e la misura del catalogo delle difformità (nel caso non sia presente, il campo risulterà vuoto e read-only); invece, il campo “Valore misura” risulterà editabile manualmente solo se il campo “Descrizione misura” è precedentemente valorizzato.

Logica di compilazione campo “Note”

Il campo “Note” dell’applicazione “Difformità rilevate”, ovvero l’attributo DESCRIPTION_LONGDESCRIPTION di X4DIFFOR, risulterà a compilazione obbligatoria solo se la difformità ha STAT uguale a “NON VALIDATA”. In tal caso, se l’utente non valorizza il campo, allora il sistema non permetterà il salvataggio e restituirà un messaggio “Compilare le note per le difformità in stato NON VALIDATA”.

Per permettere la registrazione di nuove difformità nell’ordine di lavoro, si prevede il seguente sviluppo a sistema:

Tutte gli sviluppi presentati saranno replicati anche per le interferenze.

## Visualizzazione OdL nel Centro di Controllo

Il Centro di Controllo di IBM Maximo è una dashboard personalizzabile che consente all’utente di avere una vista riassuntiva e un rapido accesso agli strumenti chiave dell’applicativo.

### Configurazioni

### Sviluppi

Per permettere la visualizzazione degli ordini di lavoro nel centro di controllo, si prevede il seguente sviluppo a sistema:

Centro di controllo monitoraggio OdL

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista di sintesi contenente gli OdL il cui stato è incluso tra APERTO e CHIUSO (descritti al paragrafo del workflow dell’OdL) e che hanno come OWNERGROUP il valore dell’UdB di appartenenza dell’utente che ha effettuato il login. Il result set “OdL di competenza” mostrerà le seguenti colonne: OdL (WONUM), Descrizione (DESCRIPTION) e Stato (STATUS).

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista di monitoraggio dell’avanzamento degli OdL il cui stato è APERTO e che hanno come OWNERGROUP il valore dell’UdB di appartenenza dell’utente che ha effettuato il login. Viene inoltre posta un’ulteriore clausola where che evidenzia in arancione i record con un valore di TARGSTARTDATE inferiore alla somma della SYSDATE (data del sistema) più la soglia definita a sistema (7 giorni). Il result set “OdL in scadenza” mostrerà le seguenti colonne: OdL (WONUM), Descrizione (DESCRIPTION), Asset (ASSETNUM), Data pianificata inizio (TARGSTARTDATE).

## Reportistica

### Reportistica operativa

La visualizzazione live di reportistica operativa verrà gestita direttamente in EAM con la realizzazione di alcuni report eseguibili dal centro di controllo dell’utente.

Nel dettaglio, i report eseguibili saranno: consuntivazione avanzamento OdL.

All’interno del report “Consuntivazione verranno riportati i seguenti campi:

Matricola risorsa (PERSONID di PERSON)

Inquadramento risorsa (CRAFT della risorsa definita in WPLABOR)

responsabile risorsa (padre di X4TITRIS della risorsa definita in WPLABOR)

UdB responsabile risorsa (X4TITRIS della risorsa definita in WPLABOR)

Codice OdL (WONUM dell’OdL)

Cliente OdL

Tipologia attività PdL (HIERARCHYPATH di CLASSSTRUCTURE relativo ai PdL)

Classificazione asset (CLASSIFICATIONID di CLASSSTRUCTURE relativo agli asset)

Gerarchia asset (HIERARCHYPATH di CLASSSTRUCTURE relativo agli asset)

Mese

Anno

Tipo spesa (X4TIPOSPESA dell’OdL)

Coin (X4COIN dell’OdL)

CBS (X4WBS dell’OdL)

CBE (X4WBE dell’OdL)

OI (X4NORDINEINTERNO dell’OdL)

NTK (X4NTK dell’OdL)

Operazione NTK (X4NOPERAZIONE dell’OdL)

OdM (X4ODM dell’OdL)

Tipo ordine OdM (X4TIPOORDINE dell’OdL)

Tipo ordine OI (X4TIPOOI dell’OdL)

Tipo attività PM (X4TIPOATTIVITA dell’OdL)

Flag invio a SAP (GENAPPRSERVRECEIPT di LABTRANS)

Ore esecuzione (REGULARHRS della risorsa definita in LABTRANS)

Ore trasferimento (X4ORETRASF della risorsa definita in LABTRANS)

Ore ufficio (X4OREUFF della risorsa definita in LABTRANS)

Data inizio pianificata (TARGSTARTDATE dell’OdL)

Data fine pianificata (TARGCOMPDATE dell’OdL)

All’interno del report “Avanzamento OdL” verranno riportati i seguenti campi:

Codice OdL (WONUM dell’OdL)

Descrizione OdL (DESCRIPTION dell’OdL)

Tipo ordine (X4TIPOO dell’OdL)

ID PdL associato (JPNUM dell’OdL)

Descrizione PdL (DESCRIPTION di JOBPLAN)

Tipologia attività PdL (HIERARCHYPATH di CLASSSTRUCTURE relativo ai PdL)

Classificazione asset (CLASSIFICATIONID di CLASSSTRUCTURE relativo agli asset)

Gerarchia asset (HIERARCHYPATH di CLASSSTRUCTURE relativo agli asset)

Tipo spesa (X4TIPOSPESA dell’OdL)

Informazione COIN (X4COIN dell’OdL)

D responsabile attività (padre dell’OWNERGROUP dell’OdL)

UdB responsabile attività (OWNERGROUP dell’OdL)

UI responsabile asset (X3INGRP di ASSET)

Data inizio pianificata (TARGSTARTDATE dell’OdL)

Data fine pianificata (TARGCOMPDATE dell’OdL)

Data esecuzione (ACTSTART dell’OdL)

Data chiusura (ACTFINISH dell’OdL)

Data modello (X4DATAMODELLO dell’OdL)

Stato OdL (STATUS dell’OdL)

Fisico pianificato (X4FISICOPLAN dell’OdL)

Fisico consuntivato (X4FISICO dell’OdL)

Unità di misura (X4UM di JOBPLAN)

Consuntivo ORE (somma di REGULARHRS, X4ORETRASF e X4OREUFF della risorsa consuntivata definita in LABTRANS)

ODM (X4ODM dell’OdL)

OI (X4NORDINEINTERNO dell’OdL)

NTK (X4NTK dell’OdL)

CBS (X4WBS dell’OdL)

CBE (X4WBE dell’OdL)

Cliente ().

Inoltre, per l’esecuzione dei report sarà necessario, tramite opportuna schermata di lancio, riportare dei parametri di input per l’esecuzione dell’estrazione. I parametri di input saranno utilizzati per effettuare la query eseguita dal report all’interno della tabella WORKORDER in modo da mostrare solo ed esclusivamente i record di ordini di lavoro che soddisfano i parametri definiti in fase di lancio del report. Nel dettaglio, i parametri di lancio da impostare dipendono dal tipo di report che si esegue e vengono riportati di seguito:

Consuntivazione :

responsabile attività (padre dell’OWNERGROUP dell’OdL, la query estrarrà tutti i figli associati al DT inserito)

UdB responsabile attività (OWNERGROUP dell’OdL)

Inquadramento risorsa (CRAFT della risorsa definita in WPLABOR)

Codice OdL (WONUM dell’OdL)

responsabile risorsa (padre di X4TITRIS della risorsa definita in WPLABOR, la query estrarrà tutti i figli associati al DT inserito)

UdB responsabile risorsa (X4TITRIS della risorsa definita in WPLABOR)

Tipologia attività PdL (JPNUM dell’OdL)

Coin (X4COIN dell’OdL)

Classificazione asset (CLASSIFICATIONID di CLASSSTRUCTURE)

Gerarchia asset (HIERARCHYPATH di CLASSSTRUCTURE)

Mese (il mese di estrazione è un parametro che viene passato all’interno della query di estrazione degli OdL in modo tale che vengano estratti tutti gli OdL che hanno una data di inizio pianificazione (TARGSTARTDATE) e una di fine (TARGCOMPDATE) compresa nel mese specificato nel parametro)

Anno (l’anno di estrazione è un parametro che viene passato all’interno della query di estrazione degli OdL in modo tale che vengano estratti tutti gli OdL che hanno una data di fine pianificazione (TARGCOMPDATE) compresa tra il 01/01 e il 31/12 dell’anno specificato nel parametro)

Tipo spesa (X4TIPOSPESA dell’OdL)

Stato OdL (STATUS dell’OdL)

Avanzamento OdL:

D responsabile attività (padre dell’OWNERGROUP dell’OdL, la query estrarrà tutti i figli associati al DT inserito)

UdB responsabile attività (OWNERGROUP dell’OdL)

UI responsabile asset (X3INGRP di ASSET)

D responsabile risorsa (padre di X4TITRIS della risorsa definita in WPLABOR, la query estrarrà tutti i figli associati al DT inserito)

UdB responsabile risorsa (X4TITRIS della risorsa definita in WPLABOR)

Tipologia attività PdL (JPNUM dell’OdL)

Coin (X4COIN dell’OdL)

Classificazione asset (CLASSIFICATIONID di CLASSSTRUCTURE)

Gerarchia asset (HIERARCHYPATH di CLASSSTRUCTURE)

Intervallo di tempo data pianificata inizio (la query estrarrà tutti i record che hanno una TARGSTARTDATE compresa nell’intervallo definito)

Intervallo di tempo data pianificata fine (la query estrarrà tutti i record che hanno una TARGCOMPDATE compresa nell’intervallo definito)

Tipo spesa (X4TIPOSPESA dell’OdL)

Stato OdL (STATUS dell’OdL).
