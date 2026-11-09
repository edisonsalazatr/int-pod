# EAM_Analisi Specifiche Tecniche_Sprint1_V1.2

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Analisi Tecnica |

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
| Escalation | Meccanismo di automazione che permette di gestire il tempo di risposta di determinate azioni e attività |
| Script di Automazione | Applicazione che permette di creare script in diversi linguaggi di programmazione al fine di automatizzare processi, eseguire calcoli complessi e personalizzare il comportamento delle applicazioni |
| Relazione | Le relazioni consentono di stabilire collegamenti tra più tabelle al fine di recuperare specifici dati mediante l'utilizzo di clausole "WHERE" all'interno di un'istruzione SQL. Questa funzionalità permette di definire una tabella di destinazione, chiamata "Figlio", sulla quale viene eseguita la clausola "WHERE", mentre la tabella di partenza è chiamata "Padre" |
| Relazione.Attributo | Convenzione utilizzata in Application Designer per fare riferimento a un attributo specifico di una tabella nel database di Maximo relazionata a quella corrente |
| Sigoption |  |

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche, già descritte ad alto livello nel documento “EAM_Analisi Funzionale_Sprint_1_v.0.docx”, verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software.

Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni implementate sulla piattaforma EAM di Terna (ambiente Dev) e gli sviluppi custom da implementare.

# Sprint 1: Catalogo e modellizzazione Attività

Nel modello Terna, le macro-attività/attività rappresentano l’entità centrale del processo di manutenzione che sarà gestito in EAM (attività di controllo, sostituzione, etc.). Le attività sono rappresentate dall’associazione di un workflow (sequenza di metodi) ad una macro-attività. I metodi sono caratterizzati da attributi che ne definiscono i dettagli in termini di risorse, attrezzature e durate necessarie per espletarli.

In generale, i temi di basso livello inclusi nello Sprint 1 sono i seguenti:

Modellizzazione delle attività (struttura relazionale, tipologia, anagrafica, prevalenza).

Gestione catalogo attività (struttura catalogo, aggiunta/modifica/disattivazione attività nel catalogo).

Anagrafica OdL.

Aggregazione OdL.

- le

## Informazioni Attività

Il Catalogo Attività sarà organizzato in diversi tab, ognuno dei quali contenente un determinato set di informazioni, nel dettaglio:

Tab “Attività”, dove verranno riportate le informazioni anagrafiche relative alla tipologia di attività presa in considerazione.

Tab “Rilevazioni”, dove verranno riportate le interferenze e le difformità rilevabili dall’attività.

Tab “Workflow”, dove verrà riportato il workflow dei metodi con le relative risorse e attrezzature associate.

Tab “Micro-controlli”, dove verranno associati i micro-controlli necessari.

D’altro canto, il Catalogo Metodi e il Catalogo Micro-controlli sono caratterizzati da un singolo tab principale contenente tutte le informazioni di interesse.

### Configurazioni

Per la gestione dei dati inerenti al catalogo delle attività e ai cataloghi satellite è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito viene riportato in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| JOBPLAN | X3ASSIEME | Assieme | ALN | 20 |
| JOBPLAN | X3ATTALL | ID Attività | UPPER | 12 |
| JOBPLAN | X3ATTCAR | Attività caricabile manualmente | YORN |  |
| JOBPLAN | X3CLASSECOMP | Classe Componente | ALN | 20 |
| JOBPLAN | X3DATAFINE | Data fine validità | DATE |  |
| JOBPLAN | X3DATAINIZIO | Data inizio validità | DATE |  |
| JOBPLAN | X3GENATTFITT | Generazione attività fittizia | YORN |  |
|  |  |  |  |  |
| JOBPLAN | X3MACROATTIVITA | ID Macro-Attività | UPPER |  |
| JOBPLAN | X3NORMDIRIF | Normativa di riferimento | ALN | 15 |
| JOBPLAN | X3PROGRAMMA | Programma | ALN | 20 |
| JOBPLAN | X3SOTTOASSIEME | Sottoassieme | ALN | 50 |
| JOBPLAN | X3TEMPOVAL | Max. tempo di validazione attività | INT | 12 |
| JOBPLAN | X3TIPOCOMP | Tipo componente | ALN | 20 |
| JOBPLAN | X3TIPOLOGIAASSET | Tipologia Asset | UPPER | 192 |
| JOBPLAN | X3ZONIZZAZIONE | Attività soggetta a zonizzazione | YORN |  |
|  |  |  |  |  |
|  |  |  |  |  |
| X3ATTPREVALENTI | ATTIVITAPREV | Attività Prevalente | ALN | 12 |
| X3ATTPREVALENTI | JPNUM | PdL | ALN | 12 |
| X3METODI | IDMETODO | ID Metodo | ALN | 20 |
| X3METODI | DESCRIPTION | Descrizione | ALN | 50 |
| X3METODI | DURATA | Durata | DURATION |  |
|  |  |  |  |  |
| X3MICROCONTROLLI | ASSIEME | Assieme | ALN | 20 |
| X3MICROCONTROLLI | CONTROLLO | Controllo | ALN | 3 |
| X3MICROCONTROLLI | DESCRIPTION | Descrizione | ALN | 50 |
| X3MICROCONTROLLI | FREQUENZA | Frequenza | INT |  |
| X3MICROCONTROLLI | IDMICROCONTROLLO | ID Microcontrollo | ALN | 15 |
| X3MICROCONTROLLI | SOTTOASSIEME | Sottoassieme | ALN | 50 |
| X3MICROCONTROLLI | TIPOLOGIAASSET | Tipologia Asset | UPPER | 192 |
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
| WORKORDER | X3AGGREGATI | OdL aggregante? | YORN | 1 |
| WORKORDER | X3NATURALAVORO | Assistenza/Sorveglianza | ALN | 20 |
| WORKORDER | X3DATAMODELLO | Data Modello | DATE |  |
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

Per permettere la visione dei campi necessari all’interno del Catalogo Attività, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “X3CATATTIVITA” come duplicato dell’applicazione standard “JOBPLAN”, si dovranno configurare:

I campi di testata del tab “Attività”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ttività | JPNUM | DESCRIPTION |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Revisione | PLUSCREVNUM | PLUSCREVCOM |  |  |
| Macro-Attività | X3MACROATTIVITA |  |  |  |
| Allegati |  |  |  |  |

I campi della sezione “Dettagli”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Tipologia Asset | X3TIPOLOGIAASSET |  | CLASSIFICATION |  |
| Assieme | X3ASSIEME |  | VALUELIST |  |
| Programma | X3PROGRAMMA |  | VALUELIST |  |
| Stato | STATUS |  |  |  |
| Sottoassieme | X3SOTTOASSIEME |  | VALUELIST |  |
| Massimo tempo di validazione (giorni)​ | X3TEMPOVAL |  |  |  |
| Durata | JPDURATION |  |  |  |
|  |  |  |  |  |
| Attività caricabile manualmente? | X3ATTCAR |  |  |  |
| ID Attività | X3ATTALL |  | VALUELIST |  |
| Tipologia attività | CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Generazione attività fittizia? | X3GENATTFITT |  |  |  |
|  |  |  |  |  |
| Attività soggetta a zonizzazione? | X3ZONIZZAZIONE |  |  |  |
| Data inizio validità | X3DATAINIZIO |  | DATELOOKUP |  |
| Data fine validità | X3DATAFINE |  | DATELOOKUP |  |
| omponente | X3TIPOCOMP |  |  | X3CONTRLEGGE |
| Normativa di riferimento | X3NORMDIRIF |  |  | X3CONTRLEGGE |
| Classe componente | X3CLASSECOMP |  |  | X3CONTRLEGGE |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

La sigoption “X3CONTRLEGGE” verifica se il tipo di controllo è “CONTROLLO DI LEGGE”; quando si verifica questa condizione, i campi che hanno assegnata tale sigoption verranno mostrati in schermata.

I campi della tabella “Difformità risolte”, caratterizzata dalla relazione “X3DIFFATT”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Difformità | DIFFORMITA |  | X3IDDIFFORMITA |  |
| Descrizione | DIFFORMITA.DESCRIPTION |  |  |  |
|  |  |  |  |  |

I campi della tabella “Attività Prevalenti”, caratterizzata dalla relazione “X3ATTPREVALENTI”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Attività | ATTIVITAPREV |  | JOBPLAN |  |
| Descrizione | ATTIVITA.DESCRIPTION |  |  |  |
|  |  |  |  |  |

I campi della tabella “Risorse pianificate”, caratterizzata dalla relazione “JOBLABOR”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPTASK |  |  |  |
| Tipo di squadra | AMCREWTYPE |  | AMCREWTYPE |  |
| Qualifica | CRAFT |  | CRAFTRATE |  |
| Livello di competenza | SKILLLEVEL |  |  |  |
| Risorse | LABORCODE |  | LABORONLY |  |
| Squadra | AMCREW |  | AMCREWS |  |
| Quantità | QUANTITY |  |  |  |
| Ore | LABORHRS |  |  |  |
| Tariffa | PAYRATE |  |  |  |
| Costo riga | LINECOST |  |  |  |
| Appuntamento richiesto? | APPTREQUIRED |  |  |  |
| Tipo | CALCAPPLYTO |  |  |  |
| Calcolo | CALCMETHOD |  |  |  |

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPTASK |  |  |  |
| Attrezzatura | ITEMNUM |  | TOOLS |  |
| Descrizione | ITEM.DESCRIPTION |  | LONGDESC |  |
| Quantità attrezzature | ITEMQTY |  |  |  |
| Ore attrezzature | HOURS |  |  |  |
| Tariffa | RATE |  |  |  |
| Costo riga | LINECOST |  |  |  |
| Tipo | CALCAPPLYTO |  |  |  |
| Calcolo | CALCMETHOD |  |  |  |

I campi di testata del tab “Rilevazioni”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPNUM | DESCRIPTION |  |  |
| Revisione | PLUSCREVNUM | PLUSCREVCOM |  |  |

I campi della tabella “Interferenze Rilevabili”, caratterizzata dalla relazione “X3ASSOCIAZIONEINT”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Interferenza | IDINTERFERENZA |  | X3IDINTERFERENZA |  |
| Descrizione | INTERFERENZA.DESCRIPTION |  |  |  |
|  |  |  |  |  |

I campi della tabella “Difformità Rilevabili”, caratterizzata dalla relazione “X3ASSOCIAZIONEDIFF”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Difformità | IDDIFFORMITA |  | X3IDDIFFORMITA |  |
| Descrizione | DIFFORMITA.DESCRIPTION |  |  |  |
|  |  |  |  |  |

I campi della tabella “Difformità Rilevabili da Microcontrolli”, caratterizzata dalla relazione “X3ASSOCIAZIONEDIFFMICR”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Difformità | IDDIFFORMITA |  |  |  |
| Descrizione | DIFFORMITA.DESCRIPTION |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

I campi di testata del tab “Workflow”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPNUM | DESCRIPTION |  |  |
| Revisione | PLUSCREVNUM | PLUSCREVCOM |  |  |

I campi della tabella “Workflow”, caratterizzata dalla relazione “X3WORKFLOW”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPTASK |  |  |  |
| ID Metodo | X3WORKFLOW |  | X3METODI |  |
| Descrizione | X3METODO.DESCRIPTION |  | LONGDESC |  |
|  |  |  |  |  |
| Durata | X3METODO.DURATA |  |  |  |

I campi di testata del tab “Micro-controlli”, caratterizzato dalla sigoption “X3CONTRPERIODICO”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPNUM | DESCRIPTION |  |  |
| Revisione | PLUSCREVNUM | PLUSCREVCOM |  |  |

I campi della tabella “Micro-controllo”, caratterizzata dalla relazione “X3MICROCONTR”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPTASK |  |  |  |
| ID Microcontrollo | X3MICROCONTROLLO |  | X3MICROCONTROLLO |  |
| Descrizione | X3MICROCONTROLLO.DESCRIPTION |  | LONGDESC |  |
|  |  |  |  |  |

Inoltre, per permettere la visione dei campi necessari all’interno del Catalogo Metodi si ha la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione “X3CATMETODI” con oggetto principale “X3METODI”, si dovranno configurare:

I campi di testata del tab “Metodo”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Metodo | IDMETODO | DESCRIPTION |  |  |
| Durata | DURATA |  |  |  |

I campi della tabella “Risorse”, caratterizzata dalla relazione “LABOR”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Qualifica | CRAFT |  | CRAFTRATE |  |
| Quantità | QUANTITY |  |  |  |

I campi della tabella “Mezzi”, caratterizzata dalla relazione “MEZZI”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Mezzi | ITEMNUM |  | TOOLS |  |
| Quantità | ITEMQTY |  |  |  |

I campi della tabella “Strumenti”, caratterizzata dalla relazione “STRUMENTI”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Strumenti | ITEMNUM |  | TOOLS |  |
| Quantità | ITEMQTY |  |  |  |

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attrezzature | ITEMNUM |  | TOOLS |  |
| Quantità | ITEMQTY |  |  |  |

Infine, per permettere la visione dei campi necessari all’interno del Catalogo Micro-controlli si ha la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione “X3MICROCONTROLLI” con oggetto principale “X3MICROCONTROLLI”, si configurano:

I campi di testata del tab “Microcontrollo”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Microcontrollo | IDMICROCONTROLLO | DESCRIPTION |  |  |
| Frequenza (M) | FREQUENZA |  |  |  |
| Controllo | CONTROLLO |  | VALUELIST |  |
| Assieme | ASSIEME |  | VALUELIST |  |
| Tipologia Asset | TIPOLOGIAASSET |  | CLASSIFICATION |  |
| Sottoassieme | SOTTOASSIEME |  | VALUELIST |  |
|  |  |  |  |  |
|  |  |  |  |  |

I campi della tabella “Difformità Rilevabili”, caratterizzata dalla relazione “X3ASSOCDIFFORMITA”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Difformità | IDDIFFORMITA |  | X3IDDIFFORMITA |  |
| Descrizione | DIFFORMITA.DESCRIPTION |  |  |  |

I campi della tabella “Interferenze Rilevabili”, caratterizzata dalla relazione “X3ASSOCINTERFERENZA”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Interferenza | IDINTERFERENZA |  | X3IDINTERFERENZA |  |
| Descrizione | INTERFERENZA.DESCRIPTION |  |  |  |

Catalogo Metodi

Creazione della tabella “X3METODI” per contenere al proprio interno le informazioni del catalogo dei Metodi. Inoltre, viene creata la tabella “X3ATTRMETODI”, in relazione con la tabella “X3METODI”, per contenere al proprio interno tutti gli attributi di interesse (i.e., mezzi, qualifiche, attrezzature, strumenti).

Difformità risolte

Creazione della tabella “X3DIFFRISOLTE” per contenere al proprio interno le difformità risolte per singola attività. Tale tabella relazionata a “JOBPLAN” e conto anche della revisione del PdL a cui è legata la difformità.

Attività prevalenti

Creazione della tabella “X3ATTPREV” per contenere al proprio interno le attività prevalenti del PdL. Questa tabella contiene i riferimenti agli identificativi univoci dei PdL che dovranno, al momento della generazione dell’OdL, essere presi in considerazione per poter chiudere tutti gli OdL ancora aperti.

In aggiunta, per garantire l’associazione di un micro-controllo/PdL a una difformità/interferenza si integra la seguente configurazione a sistema:

Associazione Micro-controllo/PdL – Difformità/Interferenza

Creazione della tabella “X3CATASSOCIAZIONE” per mettere in relazione un “IDORIGINE” (contenente l'ID del Micro-controllo o del PdL) con un “IDDIFFORMITA” o un “IDINTERFERENZA”. Risulta necessaria la definizione di un attributo per specificare il tipo di relazione (i.e., se l'origine è MC o PdL), in modo da creare tre indici univoci: “IDORIGINE – IDINTERFERENZA”, “IDORIGINE – IDDIFFORMITA”, “IDORIGINE – TIPO”.

### Sviluppi

Per permettere la funzionalità di disattivazione di un micro-controllo si dovrà prevedere il seguente sviluppo a sistema:

Disattivazione Micro-controllo

Creazione di un warning di avviso alla disattivazione di un Micro-controllo (MC). Il sistema verifica che nella tabella delle associazioni MC/PdL - Difformità (X3CATASSOCIAZIONE) se il record ID univoco di Difformità (IDDIFFORMITA) o Interferenza (IDINTERFERENZA) sia presente o meno ma con altro IDORIGINE rispetto a quello del PdL/MC corrente. Se ciò non avviene, viene generato un warning per l’utente che ha effettuato l’eliminazione dell’associazione.

## Struttura Attività

La struttura dell’attività è modellata in IBM Maximo tramite il Piano di Lavoro (PdL), il quale contiene al suo interno tutte le informazioni legate all’attività (cluster attività, difformità risolte, livello anagrafico associato, etc.), il workflow dei metodi e i micro-controlli associati all’attività.

Sia i metodi che i micro-controlli sono modellizzati attraverso il Job Task, il quale permette di gestire entrambi, separatamente, come oggetti strettamente connessi al PdL a cui sono associati.

### Configurazioni

Per permettere la gestione / visibilità del catalogo delle attività, di quello dei metodi e del catalogo dei micro-controlli si configura il sistema come segue:

Permessi utenti

La visibilità e la navigazione dell’applicazione è modulata tra i vari utenti tramite la creazione di opportune opzioni di firma (sigoption). I grant necessari per avere la visualizzazione dell’applicazione verranno forniti soltanto ai gruppi utenti di Asset Management.

Pre-filtraggio catalogo attività

Sono fornite a sistema un totale di 3 query predefinite dell’applicazione che permettono di poter prendere visione di tutte le policy dedicate rispettivamente a: Linee in Cavo, Linee Aeree e Stazioni.

### Sviluppi

Per garantire la corretta generazione di OdL in presenza di metodi e/o micro-controlli si dovranno prevedere i seguenti sviluppi a sistema:

Dominio crossover X3JOBTASK

Creazione di un dominio di tipo crossover assegnato all’attributo standard JOBTASKID della vista WOACTIVITY per migrare automaticamente le informazioni inserite nel JOBTASK verso la tabella WORKORDER al momento dell’assegnazione del PdL all’OdL.

Attributi del catalogo attività condizionali

Sulla base della tipologia di attività, i campi del Piano di Lavoro sono mostrati in modo condizionale. Per ottenere questa funzionalità è stata creata una SIGOPTION con nome uguale alla tipologia di attività e ne è stata assegnata una condizione. Al verificarsi della condizione (che verifica sempre che l’attributo CLASSSTRUCTURE sia uguale ad una specifica tipologia di attività), il campo viene mostrato. Se ciò non accade, il campo viene nascosto.

Si fa rifermento alla “Tabella 1” presente nel paragrafo “3.3 Informazioni che caratterizzano le attività” del documento “EAM_Analisi funzionale.docx” per una rappresentazione esaustiva delle casistiche.

## Processo di aggiunta e modifica di attività

L’applicativo IBM Maximo consente agli utenti abilitati di aggiungere attività al Catalogo Attività, qualora sia necessario. Al fine di prevenire possibili disallineamenti tra i cataloghi e garantire che il processo di aggiunta di una nuova attività avvenga efficacemente, si propone che il processo venga eseguito rispettando una sequenza logica di azioni prestabilite (Par. 3.4 - “EAM_Analisi Funzionale.docx”).

Oltre ad aggiungere una nuova attività, Maximo consente all’utente la possibilità di apportare modifiche alla maggior parte delle informazioni presenti nei PdL già esistenti.

### Configurazioni

Per permettere la propagazione delle modifiche apportate ai campi del Catalogo Attività su Ordini di Lavoro si ha la seguente configurazione a sistema:

Modifica Tipologia attività

Implementazione di una sigoption con condizione aggregata tale da rendere il campo di tipologia attività READ-ONLY dopo la sua compilazione.

### Sviluppi

Per permettere la funzionalità di modifica delle informazioni del Catalogo Attività si dovranno prevedere i seguenti sviluppi a sistema:

Modifica / eliminazione difformità risolte

Implementazione di uno script relativo alla modifica / eliminazione di una riga della tabella delle difformità risolte. Nel caso in cui l'ID della difformità che si cerca di cancellare dalla tabella di associazione attività-difformità non è presente in altre righe della tabella, il sistema dovrà restituire un warning per informare l'utente che nessun'altra attività potrà risolvere la difformità eliminata.

Salvataggio difformità risolte

Creazione di una condizione associata allo stato “ATTIVO” del jobplan (nel dominio degli status dei jobplan). Nel caso in cui l'attività e non è presente nessun record relazionato al jobplan corrente che si cerca di salvare nella tabella delle difformità risolte, il sistema dovrà inibire il passaggio del PdL allo stato “ATTIVO".

Modifica “Attività Fittizia”

Nel caso in cui l’utente modifichi il campo impostandolo da “sì” a “no”, viene eseguito dal sistema uno script di automazione che verifica l’esistenza di un ordine di lavoro con stato diverso da “CHIUSO” nella tabella dei WORKORDER avente JPNUM uguale all’id dell’attività fittizia. Se la verifica restituisce più di un record viene mostrato un warning all’utente che lo avvisa di tale evento.

Modifica campo “Massima validazione”

Nel caso in cui il tempo di massima validazione venga aggiornato, uno script di automazione verificherà il cambio effettuato. Se il nuovo tempo di validazione inserito dall’utente è minore di quello precedente, allora il sistema verifica la presenza di OdL nella tabella WORKORDER aventi JPNUM uguale a quello dell’attività corrente e, nel caso in cui la data corrente sia maggiore della data di generazione dell’OdL sommata al nuovo tempo di massima validità, allora ne imposta lo stato in aperto.

Creazione centro di controllo per revisione incongruenze

Creazione di un centro di controllo nel quale vengono rilevate le possibili incongruenze tra i cataloghi per estrarre le difformità non più risolvibili ma rilevabili e le difformità non più rilevabili da attività. Viene dunque creato un centro di controllo denominato “X3RILEVAZIONI” al cui interno viene definito un result set basato sull’applicazione X3CATDIFF con query denominata “INCDIFF” che rileva le due casistiche.

Disattivazione attività

Nel momento in cui viene effettuato il cambio di stato di un’attività in stato “Non attivo”, il sistema effettua diverse operazioni inerenti il catalogo delle policy contestualmente al cambio di stato avvenuto nel catalogo delle attività.

Nel dettaglio, verrà effettuata una query nella tabella X3POLICY (contenente le policy censite al sistema) per estrarre quelle policy in cui l’ID univoco dell’attività appena disattivata è presente nel campo X3ATTOUTPUT. Parallelamente viene effettuata un’altra query nella tabella X3INPUTPOLICY per estrarre quelle righe della tabella in cui l’attributo X3IDINPUT è pari all’id dell’attività appena disattivata e non sono presenti altre righe in tabella per stesso record di X3IDPOLICY. In entrambi i casi verranno disattivate le policy presenti nell’attributo “X3IDPOLICY” di entrambe le tabelle. Tale disattivazione avverrà con un cambio stato della policy.

Inoltre, per permettere la propagazione delle modifiche apportate al PdL, ovvero la sua nuova revisione, si dovrà prevedere il seguente sviluppo a sistema:

Propagazione PdL revisionato

Implementazione di un pulsante con action nel PdL che consentirà di selezionare tutti gli OdL caratterizzati dal medesimo PdL e inserire la nuova revisione. Inoltre, se è presente una richiesta di indisponibilità per l’OdL a cui si sta modificando il PdL associato, allora il sistema dovrà mostrare un service warning per fornire un avvertimento all'utente (gli OdL soggetti a questo tipo di propagazione dovranno essere tracciati a sistema).

## Informazioni associate al Monitoraggio degli OdL

Il Monitoraggio degli OdL sarà organizzato in diversi tab, ognuno dei quali contenente un determinato set di informazioni, nel dettaglio:

Tab “Ordine di lavoro”, dove verranno riportate le informazioni relative all’ordine di lavoro.

Tab “Date”, dove verranno riportate le informazioni relative alla programmazione.

### Configurazioni

Per permettere la visione dei campi necessari all’interno dell’Ordine di lavoro, si implementa la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “WOTRACK_D” come duplicato dell’applicazione standard “WOTRACK”, si configurano:

I campi di testata del tab “Ordine di lavoro”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
|  | WONUM | DESCRIPTION |  |  |
|  |  |  |  |  |
| Allegati |  |  |  |  |
| OdL padre | PARENT |  | WORKORDERISTASK |  |
| Classe | WOCLASS |  |  |  |
| Stato | STATUS |  |  |  |
| Perimetro intervento | CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
|  | GLACCOUNT |  | GLNAVIGATOR |  |
| Data stato | STATUSDATE |  |  |  |
|  |  |  |  |  |
| Descrizione classe | CLASSSTRUCTURE.DESCCRIPTION_CLASS |  | CLASSIFICATION |  |
| OdL ? | X3AGGREGATI |  |  |  |
| OdL Serbatoio? | APPTREQUIRED |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

I campi della sezione “Dettagli lavoro”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
|  | JPNUM | JOBPLAN.DESCRIPTION | WO_JOBPLAN |  |
| N. revisione piano di lavoro | PLUSCJPREVNUM |  |  |  |
|  |  |  |  |  |
| Sottoassieme | JOBPLAN.X3SOTTOASSIEME |  |  |  |
| Macro-attività | JOBPLAN.X3MACROATTIVITA |  |  |  |
| Assieme | JOBPLAN.X3ASSIEME |  |  |  |
| Tipologia PdL | JOBPLAN.CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Programma | JOBPLAN.X3PROGRAMMA |  |  |  |
| Assistenza/Sorveglianza | X3NATURALAVORO |  | X3NATURALAVORO |  |
|  |  |  |  |  |

I campi della sezione “Dettagli asset”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Asset | ASSETNUM | ASSET.DESCRIPTION | ASSET |  |
| SAP ID | ASSET.X3SAPID |  |  |  |
| Gerarchia Asset | ASSET.CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Priorità asset/collocazione | ASSETLOCPRIORITY |  |  |  |
| Priorità | WOPRIORITY |  |  |  |
| Giustificazione priorità | JUSTIFYPRIORITY |  | LONGDESC |  |

I campi della sezione “Informazioni di indirizzo”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Indirizzo di servizio | SERVICEADDRESS.SADDRESSCODE | SERVICEADDRESS.DESCRIPTION | WO_SELECTSA |  |
| Città | CITY |  |  |  |
| Indirizzo formattato | FORMATTEDADDRESS |  |  |  |
| Stato/Provincia | STATEPROVINCE | STATEPROVINCE.DESCRIPTION | VALUELIST |  |
| Indirizzo | STREETADDRESS |  |  |  |

I campi della tabella “Asset, collocazioni e IC multipli”, caratterizzata dalla relazione “MULTIASSETLOCCI”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Asset | ASSETNUM |  | ASSET |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Sequenza | SEQUENCE |  |  |  |
| Contrassegna avanzamento? | PROGRESS |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

I campi della sezione “Responsabilità”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Riportato da | REPORTEDBY |  | PERSONNAME |  |
|  |  |  |  |  |
| Responsabile Attività |  |  |  |  |
| Data di report | REPORTDATE |  | DATELOOKUP |  |
| Responsabile Asset |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Telefono | PHONE |  |  |  |

I campi di testata del tab “Date”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Data Modello | X3DATAMODELLO |  |  |  |
|  | ACTSTART |  | DATELOOKUP |  |
| Data pianificata inizio | TARGSTARTDATE |  | DATELOOKUP |  |
|  | ACTFINISH |  | DATELOOKUP |  |
| Data pianificata fine | TARGCOMPDATE |  | DATELOOKUP |  |
| Durata | ESTDUR |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Data non prima di | SNECONSTRAINT |  | DATELOOKUP |  |
|  | FNLCONSTRAINT |  | DATELOOKUP |  |
| Tempo Preparazione | X3TPREP |  |  |  |
| Tempo Operazione | X3TOPER |  |  |  |
| Tempo Trasferimento | X3TTRASF |  |  |  |
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
| --- | --- | --- | --- | --- |
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
| --- | --- | --- | --- | --- |
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
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

### Sviluppi

## Modalità di accorpamento e logiche di gestione OdL aggregatori

L’applicativo IBM Maximo consente agli utenti abilitati di accorpare più Ordini di Lavoro in un singolo OdL aggregatore. Quest’ultimo dovrà essere creato manualmente e presenterà lo stesso corredo informativo di un normale Ordine di Lavoro, al netto del flag “OdL aggregatore” attivato.

### Configurazioni

Campi obbligatori per gestione OdL Aggregati

Inserimento di una sigoption e di una condizione associata per rendere obbligatori i campi di “PdL”, “COIN”, “Programma”, “Assistenza/sorveglianza”,”Asset” dopo la selezione del flag ”OdL aggregante?”.

Configurazione applicazione gestione OdL

Per garantire una corretta visualizzazione delle pianificazioni e consuntivazioni degli OdL aggregati all’ordine di lavoro aggregante è stata configurata l’applicazione “Monitoraggio OdL” come segue: sono duplicate le tabelle di gestione di pianificazioni e consuntivazioni. A queste tabelle viene modificata la relazione che mostra i record sottostanti in modo tale che il sistema estragga tutte le pianificazioni e tutte le consuntivazioni presenti negli OdL aggregati. Viene inoltre applicata un’apposita sigoption denominata “X3ODLAGG” che mostra le due tabelle duplicate nel caso in cui l’OdL sia un OdL aggregante, se ciò non accade invece le nasconde. Per ottenere questa funzionalità viene applicata una condizione omonima alla sigoption che verifica se il valore dell’attributo X3AGGREGATI è pari a 1.

### Sviluppi

Per permettere la funzionalità di aggregazione degli OdL, si prevedono i seguenti sviluppi a sistema:

Attributi dell’ordine di lavoro condizionali

Sulla base della tipologia di ordine di lavoro, i campi dell’OdL sono mostrati in modo condizionale. Per ottenere questa funzionalità è stata creata una SIGOPTION con nome uguale alla tipologia di ordine di lavoro e ne è stata assegnata una condizione. Al verificarsi della condizione (che verifica se l’ordine è di tipo serbatoio e se l’odl è stato creato o meno dal DSS), il campo viene mostrato. Se ciò non accade, il campo viene nascosto.

Si fa rifermento alla “Tabella 1” presente nel paragrafo “4.2 Informazioni associate agli OdL” del documento “EAM_Analisi funzionale.docx” per una rappresentazione esaustiva delle casistiche.

Associazione OdL aggregati

Dopo la selezione del flag ”OdL aggregante?”, nella lookup di selezione degli OdL aggregati si dovrà poter filtrare per gli OdL che siano riconducibili alla stessa sede tecnica di primo livello di riferimento (necessario selezionare assetnum dell'OdL aggregante), OdL con stesso programma e OdL con stesso PdL.

La Clausola where dovrà essere tale che gli OdL mostrati siano: OdL con JPNUM, programma uguale all'OdL, asset di riferimento degli OdL aggregabili figli dell’asset selezionato nell’OdL aggregatore.

Gestione CoIn

Quando viene aggregato un nuovo OdL all'OdL aggregante, se l'OdL aggregato ha CoIn diversa da quella dell'OdL aggregante, l'OdL aggregato eredita la CoIn dell'aggregatore. Pertanto, per un nuovo record sulla relatedrecord, il wonum nell'attributo relatedreckey deve ereditare il “GLACCOUNT” dell'OdL presente sul relatedkey.

Gestione Assistenza/Sorveglianza

Quando viene aggregato un nuovo OdL all'OdL aggregante, se l'OdL aggregato ha valore di Assistenza/Sorveglianza diversa da quella dell'OdL aggregante, viene mostrato un warning di avviso. Pertanto, per un nuovo record sulla relatedrecord, se il wonum nell'attributo relatedreckey ha valore differente per l'attributo “X3NATURALAVORO” rispetto al wonum impostato su recordkey, allora viene evocato un service warning che avvisa l'utente.
