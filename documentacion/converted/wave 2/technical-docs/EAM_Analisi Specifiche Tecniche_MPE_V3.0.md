# EAM_Analisi Specifiche Tecniche_MPE_V3.0

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Specifica Tecnica – Motore di Pianificazione Euristico |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data | Owner |
| --- | --- | --- | --- |
| 1.0 | Prima stesura del documento | 17/01/2025 | Marco Chiappelli |
| 2.0 | Seconda stesura del documento | 14/02/2025 | Marco Chiappelli |
|  |  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | Chiara Brunelli Fabio Leonardi Marco Chiappelli Nino Tiziano Scalbi Riccardo Capecchi | 17/01/2025 |  |
| Riesaminato da: | Luca Martino | 22/02/2025 |  |
| Approvato da: |  |  |  |

# Glossario

| Termine | Definizione |
| --- | --- |
| API | Application Program Interface |
| EAM | Enterprise Asset Management |
| MPE | Motore di Pianificazione Euristico |

# Obiettivo del documento

Il presente documento ha l’obiettivo di descrivere il funzionamento del Motore di Pianificazione Euristico (MPE) all’interno della soluzione EAM adottata per l’implementazione dell’ambito trattato nel corso dello “sprint 6” e di dettagliarne le specifiche tecniche ed i software utilizzati.

Nei paragrafi successivi è riportato il dettaglio degli elementi facenti parte l’elaborazione e costituisce la linea guida per l’implementazione dell’MPE in EAM.

# Motore di Pianificazione Euristico

In EAM è possibile effettuare la pianificazione tecnica degli OdL tramite la funzionalità dedicata “Motore di Pianificazione Euristico”. Tale funzionalità consente di effettuare una pianificazione temporale di massima degli ordini all’interno di un determinato intervallo temporale selezionabile dall’utente, coerentemente con i vincoli di esecuzione delle singole attività e di capacity disponibile.

La componente di calcolo dell’MPE non viene gestita internamente al MAS, ma il MAS si collega alla componente di calcolo inviando gli ordini di lavoro da ottimizzare. Una volta terminato il calcolo, i dati sono acquisiti all’interno del MAS per permettere all’utente di accettare/rifiutare l’output dell’MPE.

# Scopo e funzione del Motore di Pianificazione Euristico

Lo scopo del MPE è quello di fornire un supporto agli utenti delle varie Unità di Business (UdB), relativamente alla pianificazione degli OdL che sono presenti a sistema e nel loro perimetro, affinché possano eseguire simulazioni di pianificazione delle attività da svolgere e, una volta ricevuto il risultato dal modello di pianificazione, valutarne o meno la possibilità di rendere tali aggiornamenti definitivi sul sistema Maximo.

# Assunzioni principali

Il Motore di Pianificazione Euristico viene invocato al fine di fornire una pianificazione degli OdL selezionati dall’utente nel periodo temporale di interesse.

Un file contenente il contenuto informativo di base, ovvero il body (JSON) di una richiesta HTTP/HTTPS necessario per il MPE, viene inviato ad una coda di elaborazione delle richieste al fine di essere processato da un servizio di Backend che ha il compito di validare la richiesta e posizionarla nella coda di elaborazione che sarà consumata poi dal modello.

MPE è in grado di prendere in esame una richiesta di elaborazione per UdB alla volta, senza consentire che ne siano accodate altre fino a quando non è terminata la fase di valutazione utente della richiesta proveniente dalla medesima Unità di Business.

La richiesta validata è successivamente messa nella coda del servizio di Worker che esegue il modello.

Importante è la gestione degli stati, al fine di poter tracciare l’avanzamento delle elaborazioni delle singole richieste e di fornire al Frontend utente la visibilità della situazione, se necessario.

Il risultato del modello è quindi riversato all’interno del database, in strutture, in modo da poter essere reso disponibile nelle modalità note all’utente, e per fare in modo che il contenuto informativo possa essere persistente fino al termine della fase di valutazione.

La valutazione svolta dall’utente può portare alla conferma, e conseguente aggiornamento della base dati originale nei sistemi dell’utente, oppure a scartare i risultati del modello, senza intaccare la base dati iniziale.

# Descrizione ed introduzione all’architettura applicativa

Dal sistema EAM l’utente ha modo di specificare i parametri di ottimizzazione che l’algoritmo deve utilizzare ed il perimetro di OdL da elaborare, ovvero le condizioni che li identificano.

Questi dati vengono messi a disposizione del Motore di Pianificazione Euristico attraverso l’invio di un file JSON ad una API dedicata, questo JSON deve contenere tutto il set informativo necessario per lo scambio comunicativo e l’individuazione dei dati per il modello.

Il file JSON deve rispettare un contenuto standard, ovvero avere:

Utente

UdB

Identificativo univoco della richiesta

Variabili di interesse per il modello

Elemento molto importante è l’UdB a cui afferisce l’utente, in quanto è stato definito da disegno della soluzione che è possibile mandare in esecuzione una sola pianificazione per UdB, al fine di evitare che utenti della medesima UdB si sovrappongano nelle valutazioni delle panificazioni degli OdL.

Al fine di poter garantire questo, sono presenti controlli che valutano se sia ammissibile o meno una richiesta di elaborazione all’interno del modello, per scongiurare il caso di cui sopra.

Altre informazioni, importanti per il modello, sono le date di inizio e fine della finestra di pianificazione di interesse, mentre estremamente a valore per la comunicazione GUI-MPE l’utente ed il numero della richiesta.

Il servizio di Backend espone un’API al fine di ricevere il body di una richiesta e si occupa di interrogare la base dati propria dell’MPE in modo da conoscere se ci sia già in esecuzione una pianificazione per la stessa UdB (o questa sia terminata e l’utente non ha ancora operato la scelta di confermarla).

Nel caso in cui non ci sia nessuna pianificazione in corso per quella UdB, allora il Backend si occupa di accodare su Redis il JSON in modo tale che il POD del Worker, che esegue il codice del modello, attraverso l’uso della libreria python Celery, possa consumare la coda per estrarre le informazioni che saranno di input al modello.

L’API del Backend da invocare per inviare la richiesta è: POST <backend_url>/api/tasks/

Di seguito viene riportato un esempio di contenuto del payload JSON:

{

udb: “<codice UDB>”,

x4mpeid: “<identificativo univoco>”

user: “<utenza>”,

json_richiesta: {

“inizioperott”: “2024-01-10”,

“fineperott”: “2024-01-31”

}

}

L’elemento “json_richiesta” è a sua volta un JSON che contiene 2 informazioni fondamentali, ovvero gli estremi del periodo di pianificazione di interesse per l’utente che ha inviato la richiesta.

I parametri per il modello relativi alla quota percentuale di Capacity da assegnare ad ogni priorità a livello mensile sono presenti nella struttura MAXPROPVALUE e sono reperibili con la seguente query:

SELECT PROPNAME, PROPVALUE

FROM MAXPROPVALUE

WHERE PROPNAME IN ('MPE.WO.PRIORITY1','MPE.WO.PRIORITY2','MPE.WO.PRIORITY3')

I dati degli OdL sono presenti sulla struttura WORKORDER, contenente l’identificativo dell’ordine e tutti i relativi attributi, parte dei quali di interesse per le elaborazioni del modello.

Al fine di non dover scambiare un payload di dimensioni corpose con la lista degli OdL è stata predisposta una struttura apposita nella quale saranno riportate le 3 informazioni basi per lo scambio informativo Maximo-MPE: tale struttura avrà a disposizione il numero della richiesta, il sito e i singoli OdL che devono entrare nel MPE. Il nome della struttura è X4MPEINPUT, dettagliata nel paragrafo 3.4.3.

Di seguito viene riportato un esempio di query da eseguire al fine di recuperare i vari attributi degli OdL da pianificare, tenendo conto che i WONUM a perimetro saranno definiti dal join con la X4MPEINPUT:

SELECT WONUM, OWNERGROUP, STATUS, WOPRIORITY, X4EFFORTO, X4EFFORTI, X4EFFORTQ, SNECONSTRAINT, FNLCONSTRAINT, ESTDUR

FROM WORKORDER

WHERE

STATUS = ‘APERTO’ AND

OWNERGROUP = {payload[“udb”]} AND

(TARGSTARTDATE BETWEEN {payload[‘inizioperott’]} AND {payload[‘fineperott’]}) AND

(TARGCOMPDATE BETWEEN {payload[‘inizioperott’]} AND {payload[‘fineperott’]})

…

Di seguito viene riportata un esempio di query da eseguire al fine di recuperare i dati della Capacity:

SELECT CRAFT, CAPACITYHTEO, UDB, MESE, ANNO, CRAFT, CAPACITYHTEO, SCENARIO

FROM X4CAPACITY X4C

JOIN X4CAPACITYPM X4CPM ON X4C.X4CAPACITYID = X4CPM.X4CAPACITYID

WHERE UDB = {payload[“udb”]}

Oltre alla struttura volta a recepire gli OdL di input per il modello, è presente anche una struttura dati che per ogni file JSON, ovvero per ogni richiesta ricevuta, mantiene le informazioni dello stato in cui queste si trovano; tale struttura ha come elemento chiave la singola run, ovvero il riferimento univoco di ogni invocazione, che è definito come elemento fondamentale di comunicazione anche con il Frontend.

La chiave di ogni run è identificata dal campo X4MPEID, presente nelle varie strutture del modello dati.

La comunicazione tra il Backend, che si occupa di gestire le richieste, ed il Worker (modello), che si occupa di calcolare la nuova pianificazione degli OdL, avviene tramite un’altra singola coda Redis, dove le varie richieste sono tra loro accodate e consumate dal POD del modello.

Questi i possibili stati in cui può trovarsi una richiesta una volta che è stata validata dal Backend:

in coda -> la richiesta è arrivata al Backend e deve ancora essere elaborata dal Worker

in elaborazione -> il Worker sta elaborando la richiesta

elaborata con successo -> l’elaborazione del Worker è terminata con successo

fallita elaborazione -> l’elaborazione del Worker è fallita

approvata -> l’utente ha approvato i risultati del modello, che andranno quindi ad aggiornare la sua base dati originale

scartata -> l’utente non ha approvato i risultati del modello, che non andranno quindi ad aggiornare la sua base dati originale

invio non previsto -> arrivo di una richiesta non prevista, in quanto è ancora presente una richiesta di una stessa UdB, che viene scartata dal Backend

Il POD Worker del modello è sempre attivo, in modo da essere il più reattivo possibile alle richieste.

Al termine dell’elaborazione dell’MPE, il risultato dell’elaborazione dati relativa agli OdL viene riportato su una tabella dedicata del database Oracle e viene aggiornato lo stato all’interno della struttura che censisce le singole richieste, in modo tale che la GUI possa conoscere quando questo è disponibile e mostrare il dato all’utente al fine di fare le sue valutazioni.

Nella struttura del modello dati, viene mantenuto lo storico delle elaborazioni nel caso sia di interesse per analisi postume.

Nel caso l’utente approvi l’elaborazione effettuata da MPE, questa viene accettata e applicata a tutti gli OdL elaborati, attraverso una elaborazione che risiede su Maximo e che si occupa anche di aggiornare lo stato al termine.

In alternativa, l’utente può rifiutare la proposta e non impattare la base dati iniziale di partenza, per cui l’elaborazione Maximo si limiterà ad aggiornare lo stato.

# Architettura applicativa

Questo il disegno tecnico della soluzione proposta dell’MPE.

Di seguito la spiegazione dei vari passaggi in immagine:

GUI invia il JSON con la richiesta all’API esposta dal Backend del MPE

Backend interroga il db, per sapere se ci sono run in corso della stessa UdB

Backend aggiunge la richiesta alla tabella delle richieste e ne aggiorna lo stato

Backend invia alla coda Redis la richiesta, perché possa essere presa in esame

Worker del modello consuma la coda Redis

Worker del modello interroga il db per recuperare i dati di OdL e Capacity

Appena terminata l’elaborazione del pianificatore, il Worker scrive i risultati nel db ed aggiorna lo stato della richiesta

# Descrizione dei moduli applicativi e delle funzionalità che assolvono

La soluzione proposta dell’MPE contiene i seguenti moduli applicativi.

Git

Sono stati predisposti 3 repository Git con i relativi permessi di lettura e scrittura sugli stessi, nel dettaglio sono:

eam_mpe_worker -> Motore di Pianificazione Euristico (POD: 2 CPU 8GB RAM)

​eam_mpe_backend -> Backend di movimentazione dato (POD: 1 CPU 2GB RAM)

eam_mpe_broker -> Gestione Code (POD: 1 CPU 1GB RAM)

​

Docker

Queste le Immagini docker per la containerizzazione degli elementi base del MPE, i cui dettagli sono riportati in tabella: ​

| Tipo immagine | Versione | Servizio | Componente | Repository |
| --- | --- | --- | --- | --- |
| python | 3.11 | Backend | Backend di comunicazione e smistamento | eam_mpe_backend |
| python | 3.11 | Motore di Pianificazione Euristico | Worker | eam_mpe_worker |
| redis | 7 | Gestione Code | Broker di messaggi | eam_mpe_broker |

​

​

Database

MPE ha accesso al database Oracle del MAS al fine di poter interrogare sia la struttura WORKORDER, relativa agli OdL, sia le due strutture X4CAPACITY e X4CAPACITYPM, relative alla Capacity.

Sempre sulla medesima istanza, sono presenti le strutture necessarie per le interazioni con la GUI e per le elaborazioni del modello; tutte hanno prefisso X4MPE*.

Storage

Al fine di garantire la persistenza del dato, è stato predisposto un servizio di Persistent Volume​ in uso su Redis

​

Archivio Segreti

Con l’obiettivo di essere aderenti agli standard di sicurezza, è in uso un servizio di registrazione sicura di segreti, in modo da non dover porre in chiaro le credenziali usate dalla soluzione.

# Descrizione del modello dati

Il modello dati presenterà una serie di strutture, volte a poter gestire l’intero flusso logico del dato e a dare disponibilità informativa per comunicazioni esterne al Motore di Pianificazione Euristico.

# Tabella WORKORDER

Questa tabella già esiste, per dettagli su questa tabella consultare il documento EAM_Analisi Specifiche Tecniche_Sprint6_V1.4.docx.

Tale struttura è così fruita da MPE e Maximo:

Ms MPE legge

Maximo scrive

# Tabella X4CAPACITY e X4CAPACITYPM

I valori per le capacità di riferimento nel periodo da considerare si possono ottenere unendo le tabelle X4CAPACITY e X4CAPACITYPM rispetto al campo X4CAPACITYID.

Per calcolare le capacità nette facciamo inoltre rifermento a quanto indicato nel seguente documento: EAM_Analisi funzionale_Sprint 6_v3.0.docx

Tale struttura è così fruita da MPE e Maximo:

MPE legge

Maximo scrive

# Tabella X4MPEINPUT

Questa risulta essere la tabella di interscambio che permette a Maximo di condividere con MPE quelli che sono gli OdL nel perimetro di pianificazione per ogni richeista. Essa è alimentata dalla GUI e letta da MPE e contiene 3 info (ordine, x4mpeid, siteid). Per un x4mpeid corrispondono N ordini disposti in N record.

Tale struttura è così fruita da MPE e Maximo:

MPE legge

Maximo scrive

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4MPEINPUT | X4MPEINPUTID | ID univoco record |  |  |
| X4MPEINPUT | X4MPEID | Chiave Richiesta |  |  |
| X4MPEINPUT | IDRUN | Chiave tecnica della run univoca | NUMBER |  |
| X4MPEINPUT | WONUM | ID dell’OdL | VARCHAR2 | 25 |
| X4MPEINPUT | SITEID | Sito |  |  |
| X4MPEINPUT | DESCRIPTION | Descrizione ODL |  |  |

# Tabella X4MPEODL

In questa tabella sono riversati i dati degli ordini che escono dal modello a seguito della riorganizzazione algoritmica, in modo tale da avere la base dati disponibile per le valutazioni utente.

Tale struttura è così fruita da MPE e Maximo:

MPE scrive

Maximo legge

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4MPEODL | X4MPEID | Chiave Richiesta |  |  |
| X4MEODL | IDRUN | Chiave tecnica della run univoca | NUMBER |  |
| X4MPEODL | WONUM | ID dell’OdL | VARCHAR2 | 25 |
| X4MPEODL | X4DATAULTIMAPIAN | Data di ultima pianificazione | DATA |  |
| X4MPEODL | X4PIANMPE | OdL pianificato dall’MPE precedentemente | YORN |  |
| X4MPEODL | TARGSTARTDATE | Data di inizio lavoro pianificata definita dall’utente | DATE |  |
| X4MPEODL | TARGCOMPDATE | Data di fine lavoro pianificata definita dall’utente | DATE |  |
| X4MPEODL | DATAINIZIOPROP | Data di inizio proposta dal MPE | DATA |  |
| X4MPEODL | DATAFINEPROP | Data di fine proposta dal MPE | DATA |  |
| X4MPEODL | SITEID | Sito |  |  |
| X4MPEODL | DESCRIPTION | Descrizione ODL |  |  |

# Tabella X4MPE_STATI

Leggere MPE, MAXIMO e scrive MAXIMO, MPE

Questa tabella assolve il compito di tracciare le richieste e dare evidenza dello stato in cui si trovano.

La struttura conserva lo storico dei passaggi di stato.

Tale struttura è così fruita da MPE e Maximo:

MPE legge e scrive

Maximo legge e scrive

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4MPE_STATI | X4MPEID | Chiave Richiesta, id sequence incrementale | NUMBER |  |
| X4MPE_STATI | IDRUN | Chiave tecnica della run univoca | NUMBER |  |
| X4MPE_STATI | ID_CELERY | UUID4 identificativo del task, autogenerato da celery in caso di corretto incodamento del task | VARCHAR2 | 36 |
| X4MPE_STATI | STATO | Stato del task ( Un valore fra ‘IN CODA’, ‘IN ELABORAZIONE’, ELABORATA CON SUCCESSO’, ‘ELABORAZIONE FALLITA’, ‘INVIO NON PREVISTO’, ‘APPROVATA’, ‘SCARTATA’) | VARCHAR2 | 32 |
| X4MPE_STATI | USERID | Utente | VARCHAR2 | 155 |
| X4MPE_STATI | TIMESTAMP_INSERIMENTO | Timestamp della richiesta | DATE |  |
| X4MPE_STATI | TIMESTAMP_MODIFICA | Timestamp della modifica | DATE |  |
| X4MPE_STATI | ACTUAL | Flag Y/N ad indicare quale sia l’ultima versione per X4MPEID | VARCHAR2 |  |
| X4MPE_STATI | JSON_RICHIESTA | JSON contenente i filtri applicati dall’utente per la ricerca | CLOB (struttura binaria per JSON) |  |
| X4MPE_STATI | UDB | Unità di Business | VARCHAR2 | 155 |
| X4MPE_STATI | DESCRIZIONE_ERRORE | Descrizione dell’errore occorso in caso di ELABORAZIONE FALLITA | VARCHAR2 | 155 |
| X4MPE_STATI | TIMESTAMP_PARTENZA_TASK | Timestamp della partenza effetiva del task | DATE |  |
| X4MPE_STATI | TIMESTAMP_FINE_TASK | Timestamp della fine effettiva del task | DATE |  |
|  |  |  |  |  |
| X4MPE_STATI | METRICHE_PIANF | JSON contenente le metriche di pianificazione | CLOB (struttura binaria per JSON) |  |

# Tabella X4MPE

Questa tabella rappresenta l’entità centrale nella gestione delle esecuzioni del Motore di Pianificazione Euristico (MPE) all’interno della piattaforma Maximo. Ogni record identifica una contesto per il motore, costruita su una configurazione specifica e su un intervallo temporale definito.

La tabella funge da fonte dati principale per la costruzione del payload JSON che viene inviato al servizio del motore MPE esterno per effettuare l’elaborazione.

Tale struttura è così fruita da MPE e Maximo:

MPE non interagisce direttamente

Maximo legge e scrive

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4MPE | X4MPEID | Chiave Richiesta | NUMBER |  |
| X4MPE | DESCRIPTION | Descrizione di esecuzione del motore | VARCHAR2 | 155 |
| X4MPE | UDB | Unità di Business | VARCHAR2 | 155 |
| X4MPE | INIZIOPEROTT | Inizio del periodo da pianificare | DATE |  |
| X4MPE | FINEPEROTT | Fine del periodo d pianificare | DATE |  |
| X4MPEODL | IDRUN | Chiave tecnica della run univoca | NUMBER |  |

# Api Maximo usate

Il Motore di Pianificazione Euristico non fa uso di API di Maximo, ma è presente una stretta interconnessione tra i due sistemi, per cui la loro necessità di comunicazione in questa fase direzionale è basata sull’aggiornamento dei campi delle tabelle, in particolare il campo dello stato delle richieste su cui si basano azioni in seguito al raggiungimento di certe valorizzazioni (tabella X4MPE_STATI).

# Api esposte da MPE ed invocate da Maximo

Come anticipato nei paragrafi precedenti, la comunicazione tra Maximo e MPE non avviene solo attraverso l’ausilio di tabelle, ma anche grazie allo scambio informativo via API.

Nello specifico, MPE espone una API a Maximo:

Api esposta da ms mpe-beckend: questa API permette a Maximo di inviare il payload con le informazioni base relativamente ad una nuova richiesta di elaborazione del modello

POST <backend_endpoint>/api/tasks/

Payload esempio:

{

"udb": "AM-PC",

"user": "userString",

"x4mpeid": 1,

“idrun”:0

"json_richiesta": {

"month_year_start": "2025-02-01",

"month_year_end": "2025-03-01",

"site_id": "siteString",

"scenario": "9581OPPL63"

}

}

Nel digramma di sotto si tratta del flusso1

Se vengono specificati due o tre attributi nei query params, ci sarà una condizione di AND fra

gli N attributi (es. id_task = X e user_time_concat = Y).

L’api provvederà ad innescare le logiche di business del MPE descritte negli paragrafi.

Prima di effettuare l’invocazione la gui di Maximo invocherà l’automation script X4PLANODL. La logica di questo script è dettagliata nel paragrafo 3.7

# Automation script Maximo usati

Gli automation script usati in ambito MPE sono 2:

X4PLANODL: ha lo scopo di inserire all’interno della tabella X4MPE_INPUT gli ordini selezionati dall’utente per la pianificazione e di comunicare la richiesta di elaborazione effettuata dall’utente tramite l’invocazione del api POST <backend_endpoint>/api/tasks/ [descritto nel paragrafo 3.6]

X4MPE_CONFERMA_DATE_PIANO_ODL: per validare la simulazione calcolata dal MPE Maximo (flusso 2 dell’immagine paragrafo 3.6) invocherà questo l’automation script. La business logic dello script provvederà a modificare la data di inizio e fine pianificata sulla tabella WORKORDER per gli ordini presenti nella tabella X4MPEODL aventi lo stesso x4mpeid selezionato sulla gui dall’utente e modificherà anche la tabella X4MPE_STATI.

# Algoritmi

L’algoritmo condiviso è strutturato in tre parti fondamentali:

Parte 1 – Input e Validazione

Parte 2 – Pianificazione

Parte 3 - Output

# 4.1 Parte 1 - Input e Validazione

### 4.1.1 Input e validazione – Vincoli

Per definire i vincoli della pianificazione il pianificatore riceve in input i seguenti valori:

Il periodo temporale INIZIOPEROTT e FINEPEROTT;

I tre valori per le percentuali di ordini di priorità 1, 2 e 3 che verranno lette dalla tabella MAXPROPVALUE;

Validazione dell’input, per esempio INIZIOPEROTT < FINEPEROTT;

### 4.1.2 Input e validazione – Ordini di Lavoro

Per quanto riguarda gli OdL, il pianificatore riceve in input i seguenti valori:

Gli ordini da pianificare, i.e. un elenco di valori che rappresentano le WONUM da selezionare e che sono contenuti nella tabella X4MPEINPUT.

Basandosi su questi valori, si selezionano i rispettivi Odl dalla tabella WORKORDER, considerando per la pianificazione le informazioni necessarie, in particolare: WONUM, PARENT, WOPRIORITY, JPNUM, STATUS, X4EFFORTO, X4EFFORTI, X4EFFORTQ, TARGSTARTDATE, TARGCOMPDATE, X4INIZIOESCLUSIONE, X4FINEESCLUSIONE, SNECONSTRAINT, FNLCONSTRAINT;

Validazione dell’input degli Odl da pianificare:

Per ogni ordine: Il periodo da escludere deve sovrapposto in tutto o in parte al periodo temporale di pianificazione;

Per ogni ordine: L’intervallo tra SNECONSTRAINT e la data FNLCONSTRAINT deve essere superiore a 30 giorni;

Lo stato dell’Odl deve essere “APERTO”;

Per ogni mese controlliamo che gli ordini divisi per priorità siano sufficienti per raggiungere il target di percentuale desiderata.

### 4.1.3 Input e validazione – Capacity

Per definire le capacità giornaliere per inquadramento (impiegati, operai, quadri) il pianificatore riceve in input i seguenti valori:

Una lista che contiene la capacità totale per inquadramento e mese per mese nel periodo di tempo da considerare (INIZIOPEROTT, FINEPEROTT). Ogni oggetto in questa lista contiene le seguenti informazioni: UDB, ANNO, MESE, SCENARIO, CRAFT, CAPACITYHTEO, SERBATOIO. Per gli anni successivi, quindi escluso l’anno N (anno N = anno in corso), il mese di dicembre conterrà la capacità annuale del relativo anno e gli altri mesi saranno a zero. Il sistema provvederà a ripartire uniformemente il valore annuale su base mensile;

Per gli Odl di tipo serbatoio, andremo a scorporare la relativa capacità mensile, indicata nella colonna SERBATOIO della tabella X4CAPACITYPM, dalla capacità totale mensile del mese di riferimento. Nel caso degli anni successivi (da N+1 a N+5) se il campo serbatoio è popolato verrà scorporato dal totale mensile, altrimenti si riterrà pari a zero.

Dalla tabella WORKORDER cerchiamo mese per mese nel periodo di tempo da considerare (INIZIOPEROTT, FINEPEROTT) gli ordini non considerati nella pianificazione che erodono la capacità. Di questo tipo verranno considerati tutti gli Odl trovati, ad eccezione dei seguenti:

Odl da ottimizzare, ovvero quelli indicati dall’utente;

Odl di tipo serbatoio, gestiti seguendo la logica al punto 2);

Odl con status (ESEGUTO, CHIUSO);

Dall’insieme di tutti gli ordini trovati, al netto di quelli indicati nei punti (a, b, c), calcoliamo l’effort eroso totale per inquadramento giorno per giorno (giorno lavorativo). In seguito, verrà calcolata la capacità netta giornaliera. Il calcolo verrà eseguito mese per mese, dividendo la capacità netta per il numero di giorni lavorativi e sottraendo l’effort eroso totale relativo di ogni giorno. Questi valori vengono quindi salvati in una lista che chiamiamo capacità giornaliera aggiornata;

Validazione della capacità:

Controlliamo che la capacità netta disponibile sia sufficiente per contenere almeno uno degli OdL da ordinare. Se non c’è abbastanza disponibilità per contenere almeno un ordine, il pianificatore si interrompe fornendo un messaggio di errore;

# 4.2 Parte 2 – Pianificazione

Il modello divide l’elenco degli ordini in tre liste (ordini_da_pianificare_p1, ordini_da_pianificare_p2, ordini_da_pianificare_p3) in base al valore della priorità (WOPRIORITY);

Per ogni lista, ordina gli Odl in base al campo FNLCONSTRAINT, in ordine crescente;

Iteriamo gli Odl della tabella ordini_da_pianificare_p1, mese per mese, nel seguente modo:

Iteriamo per tutti i giorni nel periodo da considerare:

Quando l’ordine viene piazzato, aggiorniamo i campi DATAINIZIOPROP e DATAFINEPROP dell’Odl e ricalcoliamo la capacità giornaliera disponibile;

Raggiunta la capacità massima disponibile agli ordini di priorità 1 al netto delle priorità 2 e 3, ci fermiamo tenendo traccia degli ordini non ancora piazzati;

Iteriamo gli OdL della tabella ordini_da_pianificare_p2, mese per mese, nel seguente modo:

Iteriamo per tutti i giorni nel periodo da considerare:

Proviamo a inserire gli ordini negli spazi rimasti liberi come nel punto 3) e ci fermiamo quando superiamo la percentuale minima richiesta dall’utente, tenendo traccia degli ordini non ancora piazzati;

Iteriamo gli OdL della tabella ordini_da_pianificare_p3, mese per mese, nel seguente modo:

Iteriamo per tutti i giorni nel periodo da considerare:

Proviamo a inserire gli ordini negli spazi rimasti liberi come nel punto 3) e ci fermiamo quando superiamo la percentuale minima richiesta dall’utente, tenendo traccia degli ordini non ancora piazzati;

Se non abbiamo raggiunto la percentuale minima per gli ordini di priorità 2:

Partendo dalla fine del periodo di riferimento, rimuoviamo gli ordini di priorità 1 necessari e sufficienti ad avere capacità utile per inserire gli ordini di priorità 2 mancanti;

Aggiungiamo gli ordini di priorità 2

Ripetiamo il procedimento al punto 6 anche per gli ordini di priorità 3;

Se rimane capacità disponibile cerchiamo di aggiungere gli ordini rimanenti, partendo da quelli con priorità 1, successivamente quelli con priorità 2 ed infine con priorità 3.

# 4.3 Parte 3 – Output

Al termine, salviamo per ogni odl i valori calcolati dal pianificatore, ovvero DATAINIZIOPROP e DATAFINEPROP, oltre alle informazioni necessarie ad aggiornare il relativo Odl nella tabella di origine, tra cui X4DATAULTIMAPIAN e X4PIANMPE.

Se ci sono ordini non piazzati dal pianificatore, verrà fornito un messaggio di warning per notificare l’utente. Gli ordini verranno comunque ritornati, ma a differenza degli ordini piazzati, non avranno i campi DATAINIZIOPROP e DATAFINEPROP popolati. Il MPE calcola le metriche della pianificazione eseguita (numero totale di ordini da pianificare, numero totale di ordini pianificati, percentuale degli ordini pianificati, percentuale degli ordini pianificati di priorità 1, percentuale degli ordini pianificati di priorità 2, percentuale degli ordini pianificati di priorità 3) e le scrive nella tabella X4MPE_STATI nel campo METRICHE_PIANF in formato json.
