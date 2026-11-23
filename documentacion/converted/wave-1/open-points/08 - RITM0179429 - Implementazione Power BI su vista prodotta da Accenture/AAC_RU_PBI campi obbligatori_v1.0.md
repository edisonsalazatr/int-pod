| Requisiti Utente<br>Gestione anagrafiche obbligatorie mediante vista Power BI |
| --- |

Controllo del documento

Identificazione del documento

| Nome progetto: | Gestione anagrafiche obbligatorie mediante vista Power BI |
| --- | --- |
| Funzione: |  |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 0.0 | Prima stesura | 18/12/2025 |
| 0.1 |  | gg/mm/aaaa |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | Raffaele VAIANO | 18/12/2025 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Raffaele VAIANO | 18/12/2025 |  |

Archiviazione

Fornire indicazioni sul relativo archivio di progetto

INDICE

1.	Generalità	6

1.1	Committente	8

1.2	Contesto di riferimento	8

1.3	Timing	8

2.	Descrizione dei requisiti UTENTE	9

2.1	Requisito Utente Req_U_001	9

2.1.1 Aggiornamento dati (refresh)	10

2.1.2 Struttura del report (pagine)	10

2.1.3 Maschera dei filtri (slicer)	10

2.1.4 Oggetto visivo: Tabella	11

2.1.5 Oggetto visivo: Grafico a istogramma (barre verticali)	12

2.2	Attori e ruoli coinvolti	13

3.	Descrizione di ulteriori requisiti	14

3.1	Requisiti di Performance	15

3.2	Requisiti sulla gestione dei dati/archivi	15

3.3	Requisiti Normativi	15

3.4	Requisiti Formativi	15

4.	REQUISITI DI UTILIZZO DELL’APPLICATIVO	16

4.1	Contesto Applicativo	16

INDICE DELLE TABELLE

Tabella 1 – Riferimenti documentali	5

Tabella 2 – Definizioni, acronimi, abbreviazioni	5

Tabella 3 - Generalità	7

Tabella 4 – Descrizione dei requisiti utente	9

Tabella 5 – Controlli: Attori e ruoli coinvolti	13

Tabella 6 – Descrizione ulteriori requisiti utente	14

RIFERIMENTI DOCUMENTALI

“In questa tabella viene riportato un elenco di tutti i documenti utili, come:

eventuali requisiti collegati o relativi a fasi diverse del progetto;

documenti provenienti da altre funzioni (es. Rete, Architetture, Fornitori, Security, Regolatorio)”

| Rif. | Titolo Documento | Versione | Data | Codice Documento |
| --- | --- | --- | --- | --- |
| Codice identificativo del documento di riferimento | Riportare il nome del documento | Specificare la versione del documento | Riportare la data in cui è stato emesso il documento | Riportare il codice del documento di riferimento |
|  |  |  |  |  |
|  |  |  |  |  |

Tabella 1 – Riferimenti documentali

Definizioni, acronimi, abbreviazioni

| Sigla | Descrizione |
| --- | --- |
|  |  |
|  |  |

Tabella 2 – Definizioni, acronimi, abbreviazioni

# Generalità

“Riportare il codice dell’iniziativa e successivamente selezionare, per ciascuna informazione richiesta, la relativa casella”

| Informazioni Iniziativa | Informazioni Iniziativa | Informazioni Iniziativa |
| --- | --- | --- |
| Codice Iniziativa |  |  |
| Data Rilascio Iniziativa desiderata | gg/mm/aaaa | gg/mm/aaaa |
| Richiedente |  | Inserire eventuali note |
| Trattamento dati personali | Trattamento dati personali | Trattamento dati personali |
| Il Sistema tratta dati personali? |  | Inserire eventuali note |
| Il Sistema tratta dati sensibili? |  | Inserire eventuali note |
| Il Sistema tratta dati giudiziari? |  | Inserire eventuali note |
| Vincoli di pianificazione | Vincoli di pianificazione | Vincoli di pianificazione |
| Rientra nella pianificazione di budget condiviso? |  | Inserire eventuali note |
| Esistono vincoli normativi? |  | Inserire eventuali note |
| Esiste un vincolo d’urgenza derivato da esigenze di business nel corrente esercizio? |  | Inserire eventuali note |
| Vincoli di documentazione | Vincoli di documentazione | Vincoli di documentazione |
| Il sistema da sviluppare richiede archiviazione dei documenti e/o dati? |  | Se sì, specificare, laddove noti volumi, tipologia/classi documentali relativi alla documentazione/dati da gestire. |
| Il sistema da sviluppare prevede la gestione di documenti oggetto di conservazione sostitutiva a norma di legge? (1) |  | Se sì, specificare, laddove noti  volumi, durata temporale e tipologie di documenti (es contratti, fatture, documenti legali ecc), per cui si richiede la conservazione.<br>Indicare in anni il periodo temporale per il quale il documento deve essere conservato. |
| Il sistema da sviluppare include l’uso di strumenti per la Posta Elettronica Certificata (PEC)? (2) |  | Se sì, specificare le eventuali modalità di invio (es. PEC, XML, etc.) |
| Il sistema da sviluppare prevede la produzione/gestione di documenti con firma digitale?(3) |  | Se sì, fornire dettagli sul firmatario, sui formati di firma utilizzata (cades/pades) ed indicare se sono necessarie verifiche sulla validità della stessa (Certification Autority) |

Tabella 3 - Generalità

Note:

Per conservazione sostitutiva si intende una procedura informatica che conferisce valore legale nel tempo ad un documento in formato elettronico. Questa procedura permette di equiparare la validità di un documento in formato elettronico a quella dell’originale cartaceo. La sua applicazione è per tutti quei documenti che necessitano, per motivi pratici o di legge, di essere conservati per lungo tempo come ad esempio le fatture.

Posta Elettronica Certificata sistema che consente di inviare e-mail con valore legale equiparato ad una raccomandata con ricevuta di ritorno.

La Firma Digitale è un particolare tipo di firma elettronica che consente al titolare tramite una chiave privata e al destinatario tramite una chiave pubblica, rispettivamente, di rendere manifesta e di verificare la provenienza e l'integrità di un documento informatico o di un insieme di documenti informatici.
La firma digitale è l'equivalente informatico di una firma autografa apposta su carta ed ha il suo stesso valore legale. La sua funzione è quella di garantire autenticità, integrità e validità di un documento: tramite l’apposizione della firma digitale, infatti, è possibile sottoscriverne il contenuto, assicurarne la provenienza e garantire l'inalterabilità delle informazioni in esso contenute

### Committente

Rete di Trasmissione Nazionale – Asset Management

### Contesto di riferimento

In EAM esiste delle logiche che prelevano alcune caratteristiche/attributi degli asset. Tali caratteristiche devono seguire delle regole ben precise al fine di far funzionare correttamente le suddette logiche (es. DSS, indicatori statistici, ecc).

Vista l’impossibilità, al momento, gestire i campi obbligatori in EAM, è esigenza del Business avere uno strumento di monitoraggio che consenta di analizzare gli attributi anagrafici incoerenti o mancanti (blank).

La soluzione proposta da RTI è quella di utilizzare un Power BI a supporto che può essere condiviso con gli utenti centrali e territoriali interessati alle analisi.

### Timing

Tempistiche previste da programma W2 di EAM

# Descrizione dei requisiti UTENTE

“Per ciascun requisito utente, compilare una riga della tabella sottostante, e riportare nel paragrafo successivo la relativa descrizione.”

| Id Requisito Utente | Titolo del requisito | Business owner | Eventuali impatti di business | Priorità | Dipendenza da altri requisiti (ID) |
| --- | --- | --- | --- | --- | --- |
| Req_U_001 | Gestione anagrafiche obbligatorie mediante vista Power BI | RTN-AM | Impatto su O&M | Alta |  |
|  |  |  |  |  |  |

Tabella 4 – Descrizione dei requisiti utente

### Requisito Utente Req_U_001

Fermo restando la soluzione su Power BI indicata nel capitolo 1.2 e il tracciato delle regole già fornite, per comodità riportate nel file allegato “Gestione campi obbligatori EAM_v5_rev.VAIANO.xlsx”, si richiede la realizzazione di una dashboard Power BI finalizzata al monitoraggio e alla consultazione di tutti i campi obbligatori mancanti e/o non conformi ai criteri previsti per l’anagrafica EAM, relativamente agli asset di Linee e Stazioni.

Di seguito si riporta lo schema della pagina dell’interfaccia utente richiesto:

La dashboard deve consentire agli utenti abilitati di:

individuare tempestivamente le anomalie (valori null/blank e valori non validi) sugli attributi obbligatori;

filtrare e analizzare le anomalie per perimetro organizzativo e tecnico (DT, UI, Linea/Stazione, Classificazione, Attributo);

esportare i risultati per abilitare le attività di bonifica e data quality.

2.1.1 Requisiti grafici e di layout

Il template da utilizzare dovrà essere di tipo istituzionale, includendo i loghi Terna ed EAM secondo gli standard aziendali.

Deve essere sempre mostrata la data e l’ora di ultimo aggiornamento (es. in alto a destra) in modo evidente.

### 2.1.1 Aggiornamento dati (refresh)

L’aggiornamento della dashboard dovrà essere quotidiano.

Il refresh dovrà essere lanciato preferibilmente prima delle ore 07:00, al fine di garantire la disponibilità dei dati aggiornati all’avvio delle attività lavorative.

### 2.1.2 Struttura del report (pagine)

Le pagine di Power BI dovranno essere 2:

Linee

Stazioni

Entrambe dovranno avere le stesse caratteristiche in termini di filtri e oggetti visivi, variando esclusivamente l’oggetto “Linea/Stazione” in base alla pagina.

### 2.1.3 Maschera dei filtri (slicer)

La maschera dei filtri deve avere la possibilità di essere nascosta/mostrata, in modo da massimizzare l’area di visualizzazione della tabella.

I filtri applicabili dovranno essere i seguenti e nello stesso ordine in cui vengono riportati:

DT: menù a tendina che visualizzi tutti i Dipartimenti/Distretti, con relativa Sigla_DT (es. DTNOV), anche se non presenti in tabella, ordinati per relativo codice progressivo (2601, 2602, …). Possibilità di selezione multipla.

UI: menù a tendina che visualizzi tutte le Unità Impianti pertinenti ai DT selezionati, con relativa Sigla_UI (es. UPM), anche se non presenti in tabella, ordinati per relativo codice progressivo (AVTT1, AVTT2, …). Possibilità di selezione multipla.

Linea/Stazione (a seconda della pagina): menù a tendina della concatenazione tra codice di 1° livello e relativa denominazione; possibilità di ricerca testuale (sia per codice che denominazione) e selezione multipla.

Classificazioni: menù a tendina che visualizzi tutte le classificazioni presenti nella query estratta (non devono comparire classificazioni con risultato “vuoto”); le classificazioni dovranno essere mostrate come foglia finale (non intero percorso); ricerca testuale e selezione multipla abilitate.

Attributi: menù a tendina che visualizzi tutti gli attributi presenti nella query estratta (non devono comparire attributi con risultato “vuoto”); ricerca testuale e selezione multipla abilitate.

Preferenze utente (filtri)
Deve essere possibile consentire agli utenti di salvare le impostazioni dei filtri preferiti, in modo che ad ogni apertura del report sia immediatamente disponibile la vista desiderata (es. tramite funzionalità standard di Power BI: bookmark/persistent filters ove applicabile).

### 2.1.4 Oggetto visivo: Tabella

L’oggetto visivo di tipo tabellare dovrà contenere le informazioni richieste dal requisito iniziale, cioè:

LINEA / STAZIONE

DESCRIZIONE LINEA / STAZIONE

CLASSIFICAZIONE

LIVELLO

STATO

SAPID (inteso come ultimo SAPID di Sede Tecnica disponibile)

ASSETNUM

DESCRIZIONE ASSET

DT

UI

ATTRIBUTO SEGNALAZIONE (inteso sia come campo di testata che come specifica tecnica)

VALORE PRECEDENTE MODIFICA

VALORE DOPO MODIFICA

AUTORE ULTIMA MODIFICA

DATA ULTIMA MODIFICA

MOTIVI DELLA SEGNALAZIONE

Requisiti di usabilità:

dovrà essere bloccata l’intestazione per facilitare la consultazione.

dovrà essere sempre presente un indicatore (es. card) con il numero totale di record visualizzati nella tabella, dinamico rispetto ai filtri applicati.

Esportazione tabella

La tabella dovrà essere esportabile da tutti gli utenti con abilitazione al report Power BI, nel rispetto dei vincoli della piattaforma.

Dovrà comparire un avviso qualora si tenti di esportare una tabella con numero di record superiore ai limiti consentiti.

### 2.1.5 Oggetto visivo: Grafico a istogramma (barre verticali)

L’oggetto visivo di tipo istogramma a barre verticali dovrà avere:

Asse X: tutte le UI ordinate per relativo codice, utilizzando la sigla triletterale della UI (cfr. file allegato DT_UI.xlsx).

Asse Y: numero totale di record presenti in tabella.

UI e conteggi devono essere dinamici e coerenti con i filtri (stessi risultati della tabella).

Sull’asse Y non deve comparire alcuna descrizione dell’asse; i valori devono essere mostrati come etichette sulla parte superiore di ogni barra.

2.1.6 Abilitazioni e profili di accesso

Gli utenti abilitati alla lettura del report Power BI dovranno essere:

Tutti i Responsabili COAM;

Tutti i Responsabili di UI;

Tutti i Responsabili di Team Tecnico;

Tutti i Responsabili di Team Programmazione;

Gli utenti che attualmente ricevono gli scarti MBI-L e MBI-S.

Per nuove abilitazioni, ogni utente potrà richiedere l’accesso mediante le funzionalità standard del servizio; le approvazioni dovranno pervenire ai seguenti nominativi:

Vaiano Raffaele (A391516)

Iannone Michela (A396291)

2.1.7 Requisito di integrazione / riuso dati

Si richiede la possibilità di poter “sincronizzare” altri report Power BI, creati da Asset Management, con i dati estratti dalla query di cui al presente requisito, al fine di garantire consistenza e riuso del dataset

### Attori e ruoli coinvolti

“Indicare le tipologie di ruolo/profilo da prevedere/modificare all’interno dell’applicativo”

Specificare la tipologia di utenti ai quali deve essere reso disponibile l’applicativo cioè se gli utenti sono tutti interni oppure esterni dotati oppure no di utenza di dominio.”

| Tipologia utente | Ruolo | Descrizione | Attività |
| --- | --- | --- | --- |
| Utente consultatore | Tutti ruoli abilitati | esegue consultazione, applicazione filtri ed estrazioni dei dati presenti nel PBI | esegue consultazione, applicazione filtri ed estrazioni dei dati presenti nel PBI |
| Amministratore applicativo | IT / RTI | configura parametri, visibilità dati, ruoli/profili | ha la possibilità di configurare parametri, nuove regole/criteri di estrazione, visibilità dati, ruoli/profili a seguito di apposita RITM di configurazione inviata da utenti di AM-AMOC-AAC |

Tabella 5 – Controlli: Attori e ruoli coinvolti

# Descrizione di ulteriori requisiti

“Indicare nella tabella sottostante, per ciascuna tipologia di requisito, i requisiti di tipo “non funzionale” identificati ossia requisiti di carattere più generale che non saranno soddisfatti da specifiche funzionalità dell’applicativo. Per ciascuna tipologia di requisito riportare la descrizione nei paragrafi successivi.”

| Id Requisito | Tipologia | Titolo del requisito | Business owner | Eventuali impatti di business | Priorità | Dipendenza da altri requisiti (ID)* |
| --- | --- | --- | --- | --- | --- | --- |
| Identificativo univoco del requisito Req_NF_001 | Indicare tra: Performance, <br>Gestioni dati/Archivi<br>Normativo, Formativo | Descrizione d’alto livello del requisito | Chi ha richiesto l’attività | Vincolo normativo, impatto critico su un processo di business (es.: attività bloccante, no workaround, degrado delle performance, ecc.) | Alta, media, bassa | Indicare se esistono collegamenti con altri requisiti |
| Identificativo univoco del requisito Req_NF_00n | Indicare tra: Performance, <br>Gestioni dati/Archivi<br>Normativo, Formativo | Descrizione d’alto livello del requisito | Chi ha richiesto l’attività | Vincolo normativo, impatto critico su un processo di business (es.: attività bloccante, no workaround, degrado delle performance, ecc.) | Alta, media, bassa | Indicare se esistono collegamenti con altri requisiti |

Tabella 6 – Descrizione ulteriori requisiti utente

### Requisiti di Performance

Descrivere i requisiti di performance desiderati in termini di:

Volumi attesi

Tempi di risposta

Tempi di ripristino

Qualora non vi siano particolari richieste in merito, specificare che continuano a valere i requisiti di progetto (indicando necessariamente il documento in cui sono descritti) oppure scrivere esplicitamente che non sono state espresse particolari richieste.”

### Requisiti sulla gestione dei dati/archivi

“Indicare i requisiti sulla gestione dei dati/archivi, se presenti, comprensivi di profondità storica dei dati”

### Requisiti Normativi

“Descrivere i requisiti richiesti in risposta a norme, leggi e regolamenti

Qualora non vi siano particolari richieste in merito, specificare che continuano a valere i requisiti di progetto (indicando necessariamente il documento in cui sono descritti) oppure scrivere esplicitamente che non sono state espresse particolari richieste.”

### Requisiti Formativi

“Descrivere i requisiti formativi richiesti.

Qualora non vi siano particolari richieste in merito, specificare che continuano a valere i requisiti di progetto (indicando necessariamente il documento in cui sono descritti) oppure scrivere esplicitamente che non sono state espresse particolari richieste.”

# REQUISITI DI UTILIZZO DELL’APPLICATIVO

Descrivere i requisiti relativi alla modalità di utilizzo dell’applicativo: se sono da prevedere funzionalità da fruire in mobilità.

Nel caso di funzionalità in mobilità specificare se esistono specifici requisiti da prevedere esclusivamente in mobilità

### Contesto Applicativo

“Indicare il contesto applicativo in cui la richiesta si colloca”