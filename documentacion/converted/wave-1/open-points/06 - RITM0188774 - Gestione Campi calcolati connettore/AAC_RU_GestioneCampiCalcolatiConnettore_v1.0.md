| Requisiti Utente<br>Gestione Campi calcolati connettore |
| --- |

Controllo del documento

Identificazione del documento

| Nome progetto: | Gestione campi calcolati connettore |
| --- | --- |
| Funzione: |  |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 0.0 | Prima stesura | 08/02/2026 |
|  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | Raffaele Vaiano | 08/02/2026 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Raffaele Vaiano | 08/02/2026 |  |

Archiviazione

Fornire indicazioni sul relativo archivio di progetto

INDICE

1.	Generalità	6

1.1	Committente	8

1.2	Contesto di riferimento	8

1.3	Timing	8

2.	Descrizione dei requisiti UTENTE	9

2.1	Requisito Utente Req_U_001	9

2.2	Attori e ruoli coinvolti	10

3.	Descrizione di ulteriori requisiti	11

3.1	Requisiti di Performance	12

3.2	Requisiti sulla gestione dei dati/archivi	12

3.3	Requisiti Normativi	12

3.4	Requisiti Formativi	12

4.	REQUISITI DI UTILIZZO DELL’APPLICATIVO	13

4.1	Contesto Applicativo	13

INDICE DELLE TABELLE

Tabella 1 – Riferimenti documentali	5

Tabella 2 – Definizioni, acronimi, abbreviazioni	5

Tabella 3 - Generalità	7

Tabella 4 – Descrizione dei requisiti utente	9

Tabella 5 – Controlli: Attori e ruoli coinvolti	10

Tabella 6 – Descrizione ulteriori requisiti utente	11

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

Gli attributi strutturali relativi agli estremi di linea — sia per le campate aeree sia per le linee in cavo — sono attualmente calcolati dal connettore al momento dell’invio verso SAP, ma non vengono riportati in EAM.
Analogamente, la gestione delle doppie terne richiede la presenza del campo Campata Associata anche in EAM.

La mancata visibilità di tali informazioni in EAM impedisce:

verifiche operative a livello territoriale,

allineamenti topologici tra sistemi,

controlli di qualità sui dati prima e dopo gli invii verso SAP,

estrazioni strutturate da Smart View.

Si richiede pertanto l’introduzione di tali campi nel modello dati EAM, in sola visualizzazione, con aggiornamento tramite connettore.

### Timing

Entro il 30 Aprile 2026

# Descrizione dei requisiti UTENTE

“Per ciascun requisito utente, compilare una riga della tabella sottostante, e riportare nel paragrafo successivo la relativa descrizione.”

| Id Requisito Utente | Titolo del requisito | Business owner | Eventuali impatti di business | Priorità | Dipendenza da altri requisiti (ID) |
| --- | --- | --- | --- | --- | --- |
| Req_U_001 | Estensione campi Sostegno/Estremo Inizio/Fine e Campata Associata | RTN-AM | Impatto su O&M | Alta |  |
| Req_U_001 | Visibilità sostegno inizio e sostegno fine dei Tronchi di Ispezione | RTN-AM | Impatto su O&M | Alta |  |

Tabella 4 – Descrizione dei requisiti utente

### Requisito Utente Req_U_001

Campi da aggiungere (non modificabili)

Sostegno Inizio / Estremo inizio

Sostegno Fine / Estremo fine

Campata Associata (solo nei casi di doppie terne o compresenza di più circuiti)

Regole di gestione

i valori dovranno essere calcolati direttamente in EAM ad ogni variazione di sequenza;

i valori dovranno continuare ad essere trasferiti a SAP;

i campi devono essere solamente visualizzabili, non modificabili dall’utente;

la loro visualizzazione deve essere disponibile:

nella scheda della Campata (conduttori) / Tratta Cavo (cavi),

in eventuali SmartView o altre estrazioni.

### Requisito Utente Req_U_002

Si richiede di estendere la visibilità degli estremi anche ai Tronchi di Ispezione, affinché tali oggetti ereditino:

Sostegno/Estremo Inizio

Sostegno/Estremo Fine

come informazioni derivate dalla o dalle campate/tratte collegate.

Tali valori devono essere associati a specifici nuovi attributi del tronco di ispezione:

Sostegno/Estremo cavo di inizio

Sostegno/Estremo cavo di fine

Tali attributi dovranno essere presenti anche nella maschera dei tronchi.

Regole di calcolo

il tronco deve mostrare come sostegno/estremo di inizio quello relativo al sostegno/estremo di inizio della campata avente sequenza minore tra quelle associate al tronco di ispezione stesso;

il tronco deve mostrare come sostegno/estremo di fine quello relativo al sostegno/estremo di fine della campata avente sequenza maggiore tra quelle associate al tronco di ispezione stesso;

i campi devono essere non modificabili e aggiornati automaticamente tramite connettore EAM-SAP.

### Attori e ruoli coinvolti

“Indicare le tipologie di ruolo/profilo da prevedere/modificare all’interno dell’applicativo”

Specificare la tipologia di utenti ai quali deve essere reso disponibile l’applicativo cioè se gli utenti sono tutti interni oppure esterni dotati oppure no di utenza di dominio.”

| Tipologia utente | Ruolo | Descrizione | Attività |
| --- | --- | --- | --- |
| Indicare la tipologia di utente a cui associare il ruolo/profilo: <br>interno<br>esterno con utenza di dominio<br>esterno senza utenza di dominio | Indicare il ruolo/profilo utente | Descrivere il ruolo/profilo utente | Descrivere le attività condotte dall’utente con il ruolo/profilo indicato all’interno dell’applicativo |

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