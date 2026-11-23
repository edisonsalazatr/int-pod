| Requisiti Utente<br>Gestione anagrafica Tronchi di Ispezione |
| --- |

Controllo del documento

Identificazione del documento

| Nome progetto: | Gestione anagrafica Tronchi di Ispezione |
| --- | --- |
| Funzione: |  |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 0.0 | Prima stesura | 18/07/2025 |
| 0.2 | Accorpamento altro requisito | 08//02/2026 |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | Michela Iannone | 18/07/2025 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Raffaele Vaiano | 24/07/2025 |  |

Archiviazione

Fornire indicazioni sul relativo archivio di progetto

INDICE

1.	Generalità	6

1.1	Committente	8

1.2	Contesto di riferimento	8

1.3	Timing	8

2.	Descrizione dei requisiti UTENTE	9

2.1	Requisito Utente Req_U_001	9

2.2	Attori e ruoli coinvolti	9

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

Gli asset “Tronchi di ispezione”, su EAM, sono collegati direttamente alla linea e vengono gestiti (creati e visualizzati) nella scheda “Tronchi”.

I tronchi sono visualizzati in formato tabellare, con le seguenti colonne:

Identificativo SAP del tronco

Descrizione

Lunghezza tronco

Zona e relativa Descrizione

N°Sostegni/Estremi Cavo

L’associazione viene fatta per mezzo dell’attributo LIN_TRONCO_ISPEZIONE - Tronco di ispezione, sui seguenti elementi, se trattasi, rispettivamente di Campata o Tratta Cavo:

Linea / Campata Aerea / Conduttori

Linea / Tratta Cavo / Cavi

La mancata visualizzazione dei campi Unità Impianti e Centro di Lavoro Responsabile causa disallineamenti con i sistemi afferenti (SISNET) segnalati dal territorio. I campi, nonostante presenti nel database EAM, non vengono aggiornati in fase di modifica delle campate o delle tratte cavo ad essi collegati.

Si richiede la visualizzazione dei campi Unità Impianti e Centro di Lavoro Responsabile nella scheda “Tronchi” (calcolati come da requisito riportato nel cap. 2.1 del presente documento) e la conseguente attivazione del connettore sia in fase di creazione dei tronchi che di modifica, ai fini dell’allineamento di tutti i sistemi afferenti.

Inoltre, si richiede un ricalcolo massivo di tutti i tronchi ad oggi presenti in EAM verso il connettore ai fini di una corretta migrazione dei dati verso SAP.

Altri due requisiti riportati nel presente documento riguardano la:

Possibilità di visualizzare il SAPID del tronco di ispezione come valore visualizzato sia nella maschera delle Specifiche Tecniche, sia nella Smart View;

Rinomina del SAPID del tronco di ispezione in modalità guidata;

### Timing

Entro il 30 Aprile 2026

# Descrizione dei requisiti UTENTE

“Per ciascun requisito utente, compilare una riga della tabella sottostante, e riportare nel paragrafo successivo la relativa descrizione.”

| Id Requisito Utente | Titolo del requisito | Business owner | Eventuali impatti di business | Priorità | Dipendenza da altri requisiti (ID) |
| --- | --- | --- | --- | --- | --- |
| Req_U_001 | Gestione campi UI e C. Lav. Resp. per TRONCHI DI ISPEZIONE | RTN-AM | Impatto su O&M | Alta |  |
| Req_U_002 | Visualizzazione ed esportazione SAPID dei TRONCHI DI ISPEZIONE | RTN-AM | Impatto su O&M | Alta |  |
| Req_U_003 | Rinomina guidata del SAPID del TRONCO DI ISPEZIONE | RTN-AM | Impatto su O&M | Alta |  |
| Req_U_004 | Gestione del TRONCHI DI ISPEZIONE in caso di riassociazione asset | RTN-AM | Impatto su O&M | Alta |  |

Tabella 4 – Descrizione dei requisiti utente

### Requisito Utente Req_U_001

Il presente requisito si limiterà a definire la gestione delle anagrafiche tecniche in EAM degli asset “Tronchi di ispezione”.

Attualmente nella sezione “Tronchi” di EAM, non risultano visibili i seguenti campi:

Unità Impianti

Centro di Lavoro Responsabile

Si richiede di rendere visibili tali campi nella sezione “Tronchi”, non modificabili e calcolati secondo gli asset di riferimento (campata o tratta cavo).

Nel caso i tronchi facciano riferimento a campate o tratte cavo con unità impianti differenti, va considerata l’unità impianti prevalente da attribuire al tronco stesso.

Il connettore deve essere attivato sia in fase di creazione che di modifica degli asset collegati (campate o tratte cavo) in modo da allineare SAP e, di conseguenza, gli altri sistemi afferenti (es. SISNET).

Inoltre, i tronchi ad oggi presenti in EAM devono essere oggetto di ricalcolo massivo verso il connettore per migrare i dati correttamente verso SAP.

### Requisito Utente Req_U_002

Dovrà essere possibile visualizzare nella maschera delle Specifiche Tecniche il SAPID del tronco di ispezione anziché l’assetnum che si vede attualmente

Lo stesso SAPID dovrà comparire anche quando si eseguono esportazioni da Smart View.

Inoltre, dovrà essere possibile esportare i Tronchi d’Ispezione da Smart View come se fosse una Sede Tecnica.

### Requisito Utente Req_U_003

La rinomina del SAPID del tronco di ispezione dovrà essere guidata in maniera da impedire creazioni errate, in base alle seguenti regole:

Il SAPID di 1° livello dovrà essere sempre invariato

Il progressivo del tronco di ispezione dovrà sempre essere garantito partendo da T01 e proseguendo con T02, T03, ecc ogni volta che si crea un tronco nuovo.

Se viene eliminato un tronco, il suo SAPID sarà preso dal successivo, sempre garantire la continuità dei codici del tronco, senza che vi siano buchi di numerazione.

Tutti i cambi di SAPID dovranno essere garantiti anche all’interno dei 3° livelli.

Dovrà, inoltre, essere garantito l’allineamento con SAP di tutti i SAPID, o meglio, di tutti gli attributi dei tronchi di ispezioni corrispondenti ai propri SAPID.

### Requisito Utente Req_U_004

Quando una campata viene riassociata ad altra linea questa dovrà svuotarsi del vecchio Tronco d’Ispezione e, contestualmente, il Tronco stesso dovrà perdere i riferimenti di quella campata.

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