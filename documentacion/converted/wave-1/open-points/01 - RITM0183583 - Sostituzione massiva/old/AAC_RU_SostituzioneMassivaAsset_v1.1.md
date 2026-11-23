| Requisiti Utente<br>Funzionalità di sostituzione massiva degli asset Linee |
| --- |

Controllo del documento

Identificazione del documento

| Nome progetto: | Funzionalità di sostituzione massiva degli asset Linee |
| --- | --- |
| Funzione: |  |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 0.0 | Prima stesura | 17/09/2025 |
| 0.1 | Seconda stesura dopo revisione con business | 07/01/2026 |
|  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | Raffaele VAIANO | 22/09/2025 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Raffaele VAIANO | 22/09/2025 |  |

Archiviazione

Fornire indicazioni sul relativo archivio di progetto

INDICE

1.	Generalità	6

1.1	Committente	8

1.2	Contesto di riferimento	8

1.3	Timing	8

2.	Descrizione dei requisiti UTENTE	9

2.1	Requisito Utente Req_U_001	9

2.2	Attori e ruoli coinvolti	11

3.	Descrizione di ulteriori requisiti	12

3.1	Requisiti di Performance	13

3.2	Requisiti sulla gestione dei dati/archivi	13

3.3	Requisiti Normativi	13

3.4	Requisiti Formativi	13

4.	REQUISITI DI UTILIZZO DELL’APPLICATIVO	14

4.1	Contesto Applicativo	14

INDICE DELLE TABELLE

Tabella 1 – Riferimenti documentali	5

Tabella 2 – Definizioni, acronimi, abbreviazioni	5

Tabella 3 - Generalità	7

Tabella 4 – Descrizione dei requisiti utente	9

Tabella 5 – Controlli: Attori e ruoli coinvolti	11

Tabella 6 – Descrizione ulteriori requisiti utente	12

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

In EAM esiste la possibilità di sostituire un Asset con uno nuovo, attraverso un’apposita funzionalità denominata “Sostituisci asset”.

Uno dei vantaggi di questa funzionalità è quello che porta ad inserire in automatico lo stato DIST degli asset sostituiti e di quelli relazionati ad esso (come ad esempio giunti).

Tale funzionalità però va utilizzata per singolo asset, cosa che, ovviamente, la rende poco fruibile per la sostituzione degli Asset Linee (come conduttori, CdG, armamenti, isolatori, ecc.).

Per superare tale problema, la soluzione proposta è quella di creare una funzionalità “Sostituzione massiva asset” che vada a iterare per più asset contemporaneamente la sostituzione.

### Timing

Tempistiche previste dagli OP

# Descrizione dei requisiti UTENTE

“Per ciascun requisito utente, compilare una riga della tabella sottostante, e riportare nel paragrafo successivo la relativa descrizione.”

| Id Requisito Utente | Titolo del requisito | Business owner | Eventuali impatti di business | Priorità | Dipendenza da altri requisiti (ID) |
| --- | --- | --- | --- | --- | --- |
| Req_U_001 | Funzione di sostituzione massiva asset Linee | RTN-AM | Impatto su O&M | Alta |  |
|  |  |  |  |  |  |

Tabella 4 – Descrizione dei requisiti utente

### Requisito Utente Req_U_001

Obiettivo:

Abilitare in IBM Maximo (EAM) una funzionalità che permetta, a partire da una singola linea, la sostituzione contemporanea di un insieme di asset (es. conduttori, CdG, armamenti, isolatori, cavi di potenza) replicando, per ciascun asset la stessa logica realizzata nella funzione standard “Sostituisci asset” (impostazione stato DIST sull’asset sostituito e sugli asset ad esso relazionati, es. giunti), ma applicati in modalità massiva.

Ambito

Oggetti: asset di linea gestiti in EAM. Il business fornirà la lista delle classificazioni degli asset oggetto della possibile sostituzione massiva.

Processi coinvolti: Asset Management (RTN-AM) con impatti su Operations & Maintenance (O&M).

Out of scope: modifiche a processi di contabilità industriale o pianificazione lavori (salvo quanto strettamente necessario a garantire la tracciabilità della sostituzione).

Pre-condizioni

Gli asset da sostituire risultano censiti e selezionabili in EAM e non si trovano già in stato DIST. (Allineato al comportamento della sostituzione standard).

Non sussistono blocchi operativi tali da impedire la sostituzione.

Descrizione funzionale (user flow)

Il sistema deve mettere a disposizione una dialog di ricerca asset appartenenti alla linea visualizzata che consenta all’utente di individuare l’insieme di asset da sostituire secondo il seguente flusso:

Selezione della classificazione di quarto livello sostituibile
L’utente deve selezionare una classificazione di quarto livello tra quelle ammesse per la sostituzione massiva.

Visualizzazione e selezione 
A seguito della selezione del quarto livello, il sistema mostrerà una tabella dei secondi livelli aventi classificazione correlata. La tabella mostrerà per ogni asset di secondo livello l’id di sequenza, il POSNR, il SAP id e gli attributi di testata. L’utente potrà effettuare una selezione puntuale o totale secondo le funzionalità di selezione offerte dal prodotto.

Filtri di ricerca sugli asset
L’utente potrà affinare la selezione applicando uno o più filtri, combinati tra loro in AND, tra cui:

Id sequenza;

SAP id;

altri attributi di testata

Visualizzazione e filtro delle caratteristiche del quarto livello
Il sistema permetterà di impostare filtri di ricerca sulle caratteristiche del quarto livello da sostituire. Anche questi ulteriori filtri saranno combinati in AND.

Importazione elenco asset
In alternativa sarà possibile importare un elenco di asset tramite ASSETNUM. A seguito dell’import verranno mostrati i secondi livelli corrispondenti (escludendo eventuali asset non appartenenti alla linea e non compatibili con la classificazione di quarto livello selezionata). Sarà possibile successivamente affinare la ricerca come indicato sopra.

Definizione modalità di sostituzione

La selezione dei secondi livelli (es. CAMPATA AEREA) implicherà la sostituzione degli asset di quarto livello corrispondenti alla classificazione selezionata (es. CONDUTTORE). Sarà inoltre possibile indicare ulteriori classificazioni di quarto livello (es. SFERE DI SEGNALAZIONE CONDUTTORE) relazionate alla classificazione principale (es. CONDUTTORE). La selezione di classificazioni aggiuntive consentirà la creazione dei corrispondenti asset. Sarà possibile indicare la numerosità degli asset da generare o in alternativa, se non specificata, verrà riprodotta la numerosità presente a sistema per l’asset in corso di sostituzione. I nuovi asset creati a seguito della sostituzione saranno nello stato DATT.

Parametri di esecuzione

Data/ora installazione (INSTALLDATE).

Trattamento relazioni: applicazione automatica delle stesse regole della funzione “Sostituisci asset” anche in massivo, con impostazione stato DIST sull’asset sostituito e sugli asset relazionati pertinenti (es. giunti);

Logica di sostituzione
Il sistema deve effettuare la sostituzione dell’asset principale di quarto livello.

Devono essere identificate le classificazioni dei quarti livelli relazionati (es. CONDUTTORE) indicando quali devono essere reinserite a seguito della sostituzione.

Esecuzione massiva
Per ogni asset di secondo livello selezionato, il sistema:

Identificherà gli asset di quarto livello corrispondenti alla classificazione selezionata (es. CONDUTTORE) ed eventuali asset relazionati (es. SFERE DI SEGNALAZIONE CONDUTTORE)

Aggiornerà lo stato a DIST sugli asset identificati inclusi quelli relazionati

Creerà i nuovi asset inclusi quelli relazionati indicati secondo la numerosità specificata.

Registrerà lo storico della sostituzione.

Esiti e reporting
Al termine della esecuzione, verrà mostrato un report di esecuzione con le numeriche di successo e/o fallimento, errori, dettaglio delle azioni eseguite; il report sarà mostrato in forma tabellare e sarà possibile esportare in formato CSV/XLSX.

### Attori e ruoli coinvolti

“Indicare le tipologie di ruolo/profilo da prevedere/modificare all’interno dell’applicativo”

Specificare la tipologia di utenti ai quali deve essere reso disponibile l’applicativo cioè se gli utenti sono tutti interni oppure esterni dotati oppure no di utenza di dominio.”

| Tipologia utente | Ruolo | Descrizione | Attività |
| --- | --- | --- | --- |
| Utente operatore EAM | Tutti ruoli in modifica per ogni Dipartimento di competenza | esegue ricerche e lancia la sostituzione massiva |  |
| Amministratore applicativo | Utenti Centro 1 | configura parametri, visibilità dati, ruoli/profili | ha la possibilità di selezionare (da una maschera apposita o dalla maschera delle classificazioni) le classificazioni che possono essere sostituite massivamente ed eventuali asset relazionati a cui applicare stessa logica. Sarà possibile selezionare solo le classificazioni su cui è possibile eseguire già la funzione di sostituzione “singola” |

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