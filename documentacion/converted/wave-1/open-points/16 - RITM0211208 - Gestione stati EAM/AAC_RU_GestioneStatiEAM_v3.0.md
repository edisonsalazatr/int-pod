| Requisiti Utente<br>Gestione stati EAM |
| --- |

Controllo del documento

Identificazione del documento

| Nome progetto: | Gestione stati EAM |
| --- | --- |
| Funzione: |  |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 0.0 | Prima stesura | 12/05/2026 |
| 0.1 |  | gg/mm/aaaa |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | Raffaele Vaiano | 12/05/2026 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
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

2.2	Attori e ruoli coinvolti	11

3.	Descrizione di ulteriori requisiti	13

3.1	Requisiti di Performance	14

3.2	Requisiti sulla gestione dei dati/archivi	14

3.3	Requisiti Normativi	14

3.4	Requisiti Formativi	14

4.	REQUISITI DI UTILIZZO DELL’APPLICATIVO	15

4.1	Contesto Applicativo	15

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

Si richiede la gestione degli stati in coerenza con il corretto funzionamento dei processi di Asset Management (es DSS, gestione scorte, gestione campagne, ecc)

### Timing

Il prima possibile

# Descrizione dei requisiti UTENTE

“Per ciascun requisito utente, compilare una riga della tabella sottostante, e riportare nel paragrafo successivo la relativa descrizione.”

| Id Requisito Utente | Titolo del requisito | Business owner | Eventuali impatti di business | Priorità | Dipendenza da altri requisiti (ID) |
| --- | --- | --- | --- | --- | --- |
| Req_U_001 | Visibilità stati EAM | RTN-AM | Impatto su O&M | Alta |  |

Tabella 4 – Descrizione dei requisiti utente

### Requisito Utente Req_U_001

Il presente requisito definisce gli stati visibili per ogni classificazione presente in EAM in base al seguente file Excel:

Altri punti relativi al requisito:

La funzione GESTAT, così come è pensata oggi, perde di significato in quanto viene richiesto di rendere visibili tutti gli stati a prescindere se gestiti o meno. Pertanto, potrà essere eliminata insieme a tutte le sue logiche attualmente contemplate, a meno che non si possa sfruttare per qualche funzionalità suggerita da RTI.

LINEE

I 3° livelli linee, anche se non gestiti dall’utente, devono però assumere lo stesso stato del relativo 2° livello, altrimenti non sarà possibile discriminarli tra asset che appartengono a campate ad esempio demolite o attive;

Andrebbe adeguato il comportamento della propagazione degli stati per i 4° livelli delle opere interferenti, in quanto abbiamo verificato che se viene inserita una campata in DEMO le opere interferenti non si mettono in DIST come invece avviene per gli altri 4° livelli linee.

STAZIONI

Per tutti i 2° e 3° livelli stazioni sono stati aggiunti anche gli stati DEMO e CEAL in quanto per ogni asset attivo deve esistere la possibilità di “disattivare” quell’asset e pertanto;

Su Macchine, Trasformatori SA e Batterie di condensatori, sono stati ridimensionati gli stati visibili per andare incontro ad alcune novità di processo in asset management (questa logica nei prossimi mesi potrebbe essere estesa anche ad altri componenti in una più ampia logica di razionalizzazione degli attuali stati presenti in EAM);

Inoltre, occorre introdurre dei controlli di coerenza ogni qualvolta si intende modificare lo stato di livelli 1°, 2° e 3° di LINEE e STAZIONI, evitando si applicare stati non coerenti agli stati dei propri “figli”.

Per le LINEE:

Ad ogni cambio stato del 2° livello deve avvenire un cambio stato automatico per tutti i relativi 3° livelli con lo stesso stato del 2°.

Restano invariate le regole di propagazione sui 4° livelli (deve funzionare correttamente la propagazione dello stato DEMO e CEAL dal 2° livello verso tutti i 4° livelli impostandolo con DIST),

Non potrà essere possibile applicare ad un asset di un determinato livello uno stato non coerente con quello dei propri “figli”. Dovrà essere segnalata questa incoerenza con un messaggio di errore (ad es. “Non è possibile inserire lo stato DEMO a tale asset in quanto i seguenti figli non sono in stato DEMO: indicare elenco con assetnum, SAPID, descrizione e stato”), in particolare al cambio di stato del 1° livello dovrà essere fatto un controllo di coerenza per verificare le seguenti condizioni:

DEMO: verificare che tutti i 2° livelli siano in DEMO o CEAL;

CEAL: verificare che tutti i 2° livelli siano in CEAL o DEMO;

NADI: verificare che non siano presenti 2° livelli in stato ATTV e che ce ne sia almeno uno in stato NADI. È possibile passare da stato NADI a stato ATTV o NAND;

NAND: verificare che non siano presenti 2° livelli in stato ATTV e che ce ne sia almeno uno in stato NAND. È possibile passare da stato NAND a ATTV o NADI;

Per le STAZIONI

Non potrà essere possibile applicare ad un asset di un determinato livello uno stato non coerente con quello dei propri “figli”. Dovrà essere segnalata questa incoerenza con un messaggio di errore (ad es. “Non è possibile inserire lo stato DEMO a tale asset in quanto i seguenti figli non sono in stato DEMO: indicare elenco con assetnum, SAPID, descrizione e stato”), in particolare al cambio stato del 3° livello dovrà essere fatto un controllo di coerenza per verificare le seguenti condizioni:

DEMO: verificare che tutti i 4° livelli siano in DIST;

CEAL: verificare che tutti i 4° livelli siano in DIST;

NADI: verificare che non siano presenti 4° livelli in stato ESER. È possibile passare da stato NADI a stato ATTV o NAND;

NAND: verificare che non siano presenti 4° livelli in stato ESER. È possibile passare da stato NAND a ATTV o NADI;

Al cambio di stato del 1° e del 2° livello dovrà essere fatto un controllo di coerenza per verificare le seguenti condizioni:

DEMO: verificare che tutti i 2° livelli siano in DEMO o CEAL;

CEAL: verificare che tutti i 2° livelli siano in CEAL o DEMO;

NADI: verificare che non siano presenti 2° livelli in stato ATTV e che ce ne sia almeno uno in stato NADI. È possibile passare da stato NADI a stato ATTV o NAND;

NAND: verificare che non siano presenti 2° livelli in stato ATTV e che ce ne sia almeno uno in stato NAND. È possibile passare da stato NAND a ATTV o NADI;

La gestione degli stati, almeno per i 4° livelli di LINEE e STAZIONI, dovrà essere configurabile e gestita all’occorrenza con un semplice ticket di richiesta di configurazione, senza necessitare di sviluppi particolarmente complessi.

Si chiede, infine, una revisione l’elenco del menù a descesa del cambio stato degli asset indicando anche il relativo acronimo (es. ATTV, NAND, DELE, DEMO, ecc.), in quanto oggi non presente:

E ordinarli nel seguente modo:

LINEE:

| ATTV | NADI | NAND | DEMO | CEAL | ESER | EFFE | GUAS | RIPR | DIST | DELE |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |

STAZIONI:

| ATTV | NADI | NAND | DEMO | CEAL |
| --- | --- | --- | --- | --- |

| EFFE | ESER | EFDI | EFPR | EFSC | GUAS | NIED | NIEP | NIES | NIGU | RIPR | NIEO | DIST | DELE |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |

Contestualmente occorre aggiornare il documento HLD con lo schema delle visibilità stati riportato in tale tabella.

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