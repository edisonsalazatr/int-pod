| Requisiti Utente<br>Gestione Doppie Terne con verso opposto |
| --- |

Controllo del documento

Identificazione del documento

| Nome progetto: | Gestione Doppie Terne con verso opposto |
| --- | --- |
| Funzione: |  |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 0.0 | Prima stesura | 07/08/2026 |
| 0.1 |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | Raffaele Vaiano | 07/08/2026 |  |
| Riesaminato da: |  |  |  |
| Approvato da: |  |  |  |

Archiviazione

Fornire indicazioni sul relativo archivio di progetto

INDICE

1.	Generalità	6

1.1	Committente	8

1.2	Contesto di riferimento	8

1.3	Timing	8

2.	Descrizione dei requisiti UTENTE	9

2.1	Requisito Utente Req_U_001	9

2.2	Requisito Utente Req_U_002	10

2.3	Attori e ruoli coinvolti	11

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

Tabella 3 - Generalità	6

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

ORD – Asset Management

### Contesto di riferimento

Storicamente, nel sistema SAP la gestione delle doppie terne era basata esclusivamente sull'associazione tra campate appartenenti a linee differenti mediante l'attributo Campata Associata. Tale modello consentiva di identificare la corrispondenza tra due campate in doppia terna, senza tuttavia prevedere alcuna informazione o relazione relativa ai sostegni associati alle stesse.

Con l'introduzione del sistema EAM, il modello informativo è stato esteso consentendo non solo l'associazione delle campate, ma anche l'associazione dei relativi sostegni condivisi tra le linee in doppia terna. Questa evoluzione ha migliorato la rappresentazione anagrafica degli asset, permettendo di modellare in maniera più completa le componenti fisiche effettivamente condivise.

Tuttavia, il requisito originario implementato in EAM presenta un limite nei casi in cui le due linee associate in doppia terna abbiano una sequenza delle campate con verso opposto. In tali configurazioni, infatti, a una sequenza crescente su una linea corrisponde una sequenza decrescente sull'altra linea, pur riferendosi alle medesime campate fisiche.

Durante le attività di migrazione da SAP a EAM, le associazioni tra le campate sono state correttamente riportate sulla base delle informazioni presenti nel sistema sorgente. Diversamente, non essendo disponibile in SAP alcuna relazione esplicita tra i sostegni delle due linee, l'associazione dei sostegni è stata effettuata automaticamente assumendo che le due linee condividessero il medesimo sostegno iniziale e il medesimo sostegno finale per ciascuna campata associata.

Tale assunzione risulta corretta nei casi in cui le due linee presentano la stessa progressione delle sequenze, ma non nei casi di doppia terna con verso opposto. In queste configurazioni l'associazione automatica dei sostegni ha comportato la creazione di relazioni non corrette tra gli asset, determinando uno sfasamento delle associazioni e una rappresentazione anagrafica non coerente con la configurazione fisica effettiva delle linee.

Alla luce di quanto sopra, si rende necessario introdurre una specifica gestione applicativa delle doppie terne con verso opposto, sia per consentire la corretta modellazione delle nuove associazioni, sia per procedere alla bonifica delle configurazioni già presenti in EAM che risultano affette da tale anomalia.

### Timing

Il prima possibile, in considerazione degli impatti sulla qualità del dato anagrafico e sulla corretta rappresentazione delle campate in doppia terna all’interno di EAM, con conseguenti impatti sul DSS.

# Descrizione dei requisiti UTENTE

“Per ciascun requisito utente, compilare una riga della tabella sottostante, e riportare nel paragrafo successivo la relativa descrizione.”

| Id Requisito Utente | Titolo del requisito | Business owner | Eventuali impatti di business | Priorità | Dipendenza da altri requisiti (ID) |
| --- | --- | --- | --- | --- | --- |
| Req_U_001 | Gestione delle doppie terne con verso opposto in fase di creazione/associazione | ORD-AM | Impatto sui processi di gestione anagrafica linee con conseguenze sul corretto funzionamento del DSS | Alta |  |
| Req_U_002 | Bonifica delle campate in doppia terna con verso incoerente | ORD-AM | Impatto sui processi di gestione anagrafica linee con conseguenze sul corretto funzionamento del DSS | Alta |  |

Tabella 4 – Descrizione dei requisiti utente

### Requisito Utente Req_U_001

Il presente requisito ha l’obiettivo di introdurre in EAM una funzionalità che consenta, già in fase di creazione o associazione delle doppie terne, di gestire correttamente i casi in cui le due linee interessate presentino un verso di percorrenza discorde, come nel seguente esempio:

In particolare, nell’ambito della procedura di associazione degli asset in doppia terna, dovrà essere prevista la possibilità per l’utente di indicare che la terna associata presenta un verso opposto rispetto alla linea di riferimento. Tale informazione dovrà essere utilizzata dal sistema per determinare correttamente l’ordine di associazione delle campate e dei sostegni condivisi tra le due linee, evitando che la sequenza degli asset venga interpretata come coerente quando, invece, le stesse campate risultano disposte secondo una sequenza discorde.

La soluzione dovrà quindi consentire di distinguere almeno le seguenti casistiche:

doppia terna con verso coerente, in cui le campate condivise tra le due linee seguono una sequenza concorde;

doppia terna con verso discorde, in cui a una sequenza crescente su una linea corrisponde una sequenza decrescente sull’altra linea, a parità di asset condiviso;

eventuali casistiche anomale, in cui il sistema non sia in grado di determinare una corrispondenza univoca tra le campate delle due linee.

Ai fini della corretta gestione della funzionalità, il sistema dovrà considerare come elemento di confronto l’ASSETNUM della campata condivisa tra le due linee e dovrà verificare la coerenza della relativa SEQUENCE sui due ROOTSAPID interessati. In presenza di verso opposto, l’associazione dovrà essere gestita in modo tale da mantenere l’allineamento tra i medesimi asset campate e sostegni, anche quando la sequenza risulta crescente su una linea e decrescente sull’altra.

La funzionalità dovrà inoltre prevedere opportuni controlli di coerenza e messaggi informativi o bloccanti, al fine di evitare la creazione di associazioni non corrette. In particolare, qualora il sistema rilevi disallineamenti tra le campate da associare, asset non corrispondenti, sequenze non concordi o casi non riconducibili alle configurazioni previste, dovrà essere restituita all’utente una segnalazione chiara, riportando almeno l’elenco degli asset interessati, il SAPID, la linea di appartenenza e la relativa SEQUENCE.

La funzionalità dovrà essere integrata nel processo standard di creazione e associazione delle doppie terne, in modo da prevenire la generazione di anomalie anagrafiche e ridurre la necessità di interventi correttivi successivi sui dati già presenti in EAM.

### Requisito Utente Req_U_002

Il presente requisito ha l’obiettivo di richiedere la bonifica dei sostegni già presenti in EAM che risultano affetti da anomalie riconducibili alla gestione del verso opposto nelle doppie terne.

In particolare, dovranno essere analizzate e corrette tutte le casistiche in cui, a parità di ASSETNUM della campata condivisa tra due linee, la SEQUENCE risulti crescente su una linea e decrescente sull’altra, senza che tale condizione sia stata gestita correttamente a sistema. Tali anomalie possono determinare un disallineamento nella rappresentazione anagrafica dei sostegni in doppia terna, con potenziali impatti sulla corretta lettura della struttura della linea, e sui processi che utilizzano le informazioni anagrafiche presenti in EAM.

La bonifica dovrà essere effettuata sulla base dell’elenco allegato per i sostegni afferenti alle campate che presentano la colonna VERSO = “INVERTITA”.

A valle della bonifica dovrà essere garantita la coerenza delle associazioni tra le campate e i sostegni in doppia terna, assicurando che i medesimi ASSETNUM risultino correttamente associati tra le due linee anche in presenza di verso opposto.

La bonifica dovrà essere considerata propedeutica alla piena efficacia della nuova funzionalità, in quanto consente di riallineare i dati pregressi già presenti in EAM e di assicurare omogeneità tra le nuove associazioni gestite a sistema e le associazioni già censite.

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