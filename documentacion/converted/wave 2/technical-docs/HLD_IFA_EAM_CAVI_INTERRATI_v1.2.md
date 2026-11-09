# HLD_IFA_EAM_CAVI_INTERRATI_v1.2

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento | HLD– Interface Agreement EAM vs CAVI INTERRATI |

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
| Riesaminato da: | ACN | 17/06/2024 |  |
| Revisionato da: | TERNA | 19/07/2024 |  |

# Glossario

| Termine | Definizione | Definizione |
| --- | --- | --- |
| Odl | Ordine di lavoro |  |
| DSS | Decision Support System |  |
|  |  |  |

# Obiettivi del documento

Nell’ambito programma di evolutive previste per la wave 2 del progetto EAM il documento ha lo scopo di descrivere funzionalmente i flussi d’integrazione e di riportarne per ogni flusso:

Descrizione funzionale

logiche d’attivazione

dati scambiati

tipologia d’interazione

# Flussi d’integrazione e logica d’attivazione

Questa sezione si prefigge di descrivere i flussi che intercorrono tra EAM e il sistema CAVI INTERRATI e le relative interazioni.

Di seguito diagramma di chiamate tra i sistemi EAM, CAVI INTERRATI:

Allineamento anagrafica SAP à SISNET. Sisnet a valle arricchisce i dati con dati di geolocalizzazione

EAM invia ordini di lavoro con la richiesta di esecuzione ispezioni per rilevare difformità sui cavi di rete. Invia in particolare un ordine aggregato. L’ordine aggregato contiene più ordini singoli ognuno associato alla singola tratta cavo. Nella richiesta d’invocazione EAM passa Ordini di lavoro con chiave EAM, assetnum EAM per usi futuri, sapId chiave SAP della linea e tronco. Nel flusso vengono inviati anche i sapId delle tratte cavo relative ai singoli ordini di lavoro.

Il catalogo difformità sarà allineato manualmente tra i due sistemi.

L’operatore di cavi interrati una volta completata l’ispezione e rilevate le difformità potrà procedere manualmente su EAM al caricamento delle difformità rilevate mappandole sulla nuova anagrafica EAM. Anche la chiusura dell’Odl verrà gestita manualmente.

L’api di cancellazione ordine triggerata da EAM permetterà di cancellare l’ordine su CI e quindi interrompere il processo indicato ai punti 1,2,3

| Id | Flusso | Descrizione |
| --- | --- | --- |
| 1 | Invio ispezioni verso cavi | Interfaccia di CAVI INTERRATI per la ricezione dell’ordine di lavoro relativo agli interventi di manutenzione dei cavi. |
| 5 | INVIO CANCELLAZIONE CONTROLLI ELIMINATI IN EAM | Interfaccia di CAVI INTERRATI per recepire gli interventi eliminati |
| 4 | Invio della chiusura dell’ispezione da CAVI INTERRATI AD EAM | L’invio e la chiusura sulle difformità di un Ordine di Lavoro vengono gestite manualmente dall’utente. |

## Flusso INVIO ISPEZIONI VERSO CAVI

### Descrizione funzionale interfaccia

Il sistema EAM, attraverso le logiche del DSS, genera gli Ordini di Lavoro e invia la quota parte delle ispezioni relative alle Linee in Cavo che deve essere gestita in outsourcing, tramite il flusso “Invio ispezioni verso cavi” alla suite CAVI INTERRATI, a cui è demandata la gestione dell’Ordine di Lavoro.

### Logiche d’attivazione

L’ordine di Lavoro su EAM è generato in stato “APERTO” e inviato alla SUITE CAVI INTERRATI.

Nel caso vi fosse una modifica della pianificazione di un ordine aggregato e degli ordini figli innescato dalla modifica delle frequenza di una policy. EAM invierà un aggiornamento dell’ordine adeguando.

Solo se lo stato dell’ordine in EAM è Aperto. Nel caso in cui l’ordine IN EAM fosse in stato Schedulabile o schedulato non verrà invito aggiornamento dell’ordine da EAM a CAVI.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | REST |
| --- | --- |
| BUS | Nessun bus |
| Tipo Esposizione | API |
| Frequenza Invocazione | < 5000 al mese |
| Timing (indica l'arco temporale in cui viene effettuata l'invocazione (Giornaliera, Mensile, ecc) | In automatico |
| Modalità Comunicazione | Sincrona |
| Rete (su cui è esposto il flusso) | Intranet / Internet |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | HTTPS |
| Formato Dati Supportati | JSON |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati in formato lista:

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| wonum / codice ordine di lavoro | Codice ordine di lavoro aggregato | Y |  |
| date | Data target | Y | Formato 2024-06-05 |
| lineCode | sapId della linea | Y |  |
| lineName | Descrizione della linea | Y |  |
| trunkCode | SapIDdel tronco | Y |  |
| listaOrdini { | Lista di tutte le coppie tratte cavo/odl singolo associate all'ordine aggregato |  | Array contenete gli attributi “Singolo ordine” e “Tratta cavo”; all’interno dell’array sono presenti le coppie di informazioni (OdL e SAPID tratta) sottostanti all’odl aggregato. |
| Singolo ordine | Singolo ordine associato alla tratta Cavo | Y |  |
| trattaCavo | idSAP della singola tratta CAVO | Y |  |
| listaOrdini } |  |  |  |
| aotCode | Unità impianti | Y |  |
| uiCode | Centro di lavoro | Y |  |
| planType | Una costante valorizzata con la stringa “REGULAR” | Y |  |
| codiceAttivita | Identificativo univoco attività dell’ordine di lavoro | Y | Formato Stringa |

Un JSON esemplificativo è riportato di seguito:

[

{

"date": "2025-10-01",

"lineCode": "LC123",

"lineName": "Linea 1",

"trunkCode": "TC456",

"aotCode": "FVL",

"uiCode": "FVTT2",

"wonum": "WO345",

"orderList": [

{

"singleOrder": "ORD001",

"trattaCavo": "TC123"

},

{

"singleOrder": "ORD002",

"trattaCavo": "TC456"

}

],

"planType": "REGULAR",

"codiceAttivita": "CA123"

}

]

Response in formato lista dell’oggetto descritto di seguito:

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
| wonum / codice ordine di lavoro | Codice ordine di lavoro aggregato | Y |
| esito | Esito della chiamata (Dominio OK/KO) | Y |
| codiceErrore | Codice identificativo dell’errore | Y |
| messaggioErrore | Descrizione del messaggio di errore | Y |

## Flusso INVIO CANCELLAZIONE CONTROLLI ELIMINATI IN EAM

### Descrizione funzionale interfaccia

La necessità di cancellare un ordine di lavoro già trasmesso a CAVI INTERRATI può scaturire dalla modifica all’anagrafica dell’asset su cui insiste l’ordine di lavoro principale emesso sul tronco:

In caso di eliminazione della tratta o di cambio della topologia del tronco, EAM non invierà la cancellazione dell’ordine nel caso in cui rimarrà associato al tronco almeno una tratta cavo. Nel caso in cui il cambiamento della topologia del tronco prevedesse l’eliminazione di tutte le tratte, solo in questo caso EAM invierà la cancellazione dell’ordine associato.

### Logiche d’attivazione

La logica di attivazione del flusso Invio cancellazione controlli eliminati è il passaggio di stato di un Ordine di lavoro su “Annullato”. che è stato precedentemente inviato alla suite Cavi interrati

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | REST |
| --- | --- |
| BUS | Nessun bus |
| Tipo Esposizione | API |
| Frequenza Invocazione | < 5000 al mese |
| Timing (indica l'arco temporale in cui viene effettuata l'invocazione (Giornaliera, Mensile, ecc) | In automatico |
| Modalità Comunicazione | Sincrona |
| Rete (su cui è esposto il flusso) | Intranet / Internet |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | HTTPS |
| Formato Dati Supportati | JSON |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| Array di codici MBI/EAM | Un array di identificativi degli ordini di lavoro da eliminare | Y | Identificativo degli ordini di lavoro emessi sul tronco. |

Un JSON esemplificativo è riportato di seguito:

[

"WO345",

"WO346"

]

Response in formato lista dell’oggetto descritto di seguito:

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
| wonum / codice ordine di lavoro | Codice ordine di lavoro aggregato | Y |
| esito | Esito della chiamata (Dominio OK/KO) | Y |
| codiceErrore | Codice identificativo dell’errore | Y |
| messaggioErrore | Descrizione del messaggio di errore | Y |

### Gestione degli errori

Il sistema potrebbe rilevare un errore nel caso:

Cancellazione di un ordine non inviato/presente;

Cancellazione di un ordine già cancellato.
