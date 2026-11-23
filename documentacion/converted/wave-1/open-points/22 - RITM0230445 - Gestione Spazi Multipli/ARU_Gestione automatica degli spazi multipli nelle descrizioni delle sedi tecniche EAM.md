| Requisiti Utente<br>Gestione automatica degli spazi multipli nelle descrizioni delle sedi tecniche EAM |
| --- |

Controllo del documento

Identificazione del documento

| Nome progetto: | Gestione automatica degli spazi multipli nelle descrizioni delle sedi tecniche EAM |
| --- | --- |
| Funzione: |  |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 1.0 | Prima stesura | 31/07/2026 |
|  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | Insrerire funzione |  |  |
| Riesaminato da: | inserire funzione |  |  |
| Approvato da: | inserire funzione |  |  |

Archiviazione

Da definire

INDICE

1.	Generalità	5

1.1	Committente	6

1.2	Contesto di riferimento	6

1.3	Timing	6

1.4	Requisito	6

1.1	Attori e ruoli coinvolti	7

1.2	Integrazione tra sistemi	7

1.3	Use case	7

INDICE DELLE TABELLE

Tabella 1 - Generalità	5

Tabella 2 – Descrizione dei requisiti utente	6

Generalità

| Informazioni Iniziativa | Informazioni Iniziativa | Informazioni Iniziativa |
| --- | --- | --- |
| Codice Iniziativa |  |  |
| Data Rilascio Iniziativa desiderata | xx/xx/xxxx | xx/xx/xxxx |
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

Tabella 1 - Generalità

Note:

Per conservazione sostitutiva si intende una procedura informatica che conferisce valore legale nel tempo ad un documento in formato elettronico. Questa procedura permette di equiparare la validità di un documento in formato elettronico a quella dell’originale cartaceo. La sua applicazione è per tutti quei documenti che necessitano, per motivi pratici o di legge, di essere conservati per lungo tempo come ad esempio le fatture.

Posta Elettronica Certificata sistema che consente di inviare e-mail con valore legale equiparato ad una raccomandata con ricevuta di ritorno.

La Firma Digitale è un particolare tipo di firma elettronica che consente al titolare tramite una chiave privata e al destinatario tramite una chiave pubblica, rispettivamente, di rendere manifesta e di verificare la provenienza e l'integrità di un documento informatico o di un insieme di documenti informatici.
La firma digitale è l'equivalente informatico di una firma autografa apposta su carta ed ha il suo stesso valore legale. La sua funzione è quella di garantire autenticità, integrità e validità di un documento: tramite l’apposizione della firma digitale, infatti, è possibile sottoscriverne il contenuto, assicurarne la provenienza e garantire l'inalterabilità delle informazioni in esso contenute

### Committente

SDS-ITD-ESP-SOL-Application Operations & Cloud

### Contesto di riferimento

Nell’ambito del flusso di integrazione delle sedi tecniche tra EAM e i sistemi a valle, è stata riscontrata un’anomalia causata dalla presenza di spazi multipli nel campo descrizione. Durante il trasferimento dei dati, tali spazi vengono rappresentati con il carattere “#”, provocando errori nell’elaborazione della catena Pro-CPM di BW.

A seguito della bonifica puntuale delle sedi tecniche interessate e dell’incontro tecnico del 31 luglio 2026, è stata condivisa la necessità di estendere l’analisi all’intero perimetro delle descrizioni presenti in EAM e di introdurre un’azione automatica in fase di validazione del campo. Il controllo dovrà ricondurre eventuali sequenze di spazi multipli a un unico spazio, prevenendo il ripetersi dell’anomalia. È stata inoltre prevista l’esecuzione di test lungo la catena applicativa per verificare il corretto funzionamento della modifica prima del rilascio

### Timing

Da definire

Requisiti

| Id Requisito Utente | Titolo del requisito | Business owner | Eventuali impatti di business | Priorità | Dipendenza da altri requisiti (ID) |
| --- | --- | --- | --- | --- | --- |
| Req_U_001 | Gestione automatica degli spazi multipli nelle descrizioni delle sedi tecniche EAM | N/A | N/A | N/A | N/A |

Tabella 2 – Descrizione dei requisiti utente

### Requisito

L’intervento prevede:

l’analisi e la bonifica delle descrizioni delle sedi tecniche presenti in EAM che contengono spazi multipli;

l’implementazione lato EAM di un’azione automatica che, in fase di validazione del campo descrizione, ricondurrà eventuali spazi multipli a uno spazio singolo;

l’esecuzione dei test necessari a verificare il corretto funzionamento della modifica lungo la catena applicativa.