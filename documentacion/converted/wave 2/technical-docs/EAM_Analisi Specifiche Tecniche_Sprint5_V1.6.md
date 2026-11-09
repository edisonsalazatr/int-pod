# EAM_Analisi Specifiche Tecniche_Sprint5_V1.6

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Specifica Tecnica – Sprint 5 |

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
| Configurazione | Implementazione Standard sull’applicativo IBM Maximo |
| Sviluppo | Implementazione Custom sull’applicativo IBM Maximo |
| Configurazione Database | Applicazione che consente di creare, modificare e gestire tabelle, attributi, indici e relazioni all'interno del database di Maximo. Qui è possibile definire la struttura del database e configurare i dati in modo da adattarsi alle specifiche esigenze dell'organizzazione |
| Application Designer | Applicazione che consente di creare e personalizzare l'interfaccia grafica delle applicazioni presenti in Maximo. Gli utenti possono aggiungere tabelle, sezioni e campi per adattare l'aspetto e il comportamento delle applicazioni in base alle specifiche esigenze aziendali |
| Conditional Expression Manager | Applicazione che permette di creare e modificare espressioni in linguaggio SQL utili per gestire le regole di visibilità e/o editabilità di determinati oggetti all'interno di Maximo |
| Escalation | Le escalation in IBM Maximo vengono utilizzate per automatizzare attività e monitorare record sensibili al tempo. Quest’ultime possono essere attivate quando un record raggiunge un punto di escalation definito. I punti di escalation rappresentano le condizioni che devono essere soddisfatte |
| Script di Automazione | Applicazione che permette di creare script in diversi linguaggi di programmazione al fine di automatizzare processi, eseguire calcoli complessi e personalizzare il comportamento delle applicazioni |
| Relazione | Le relazioni consentono di stabilire collegamenti tra più tabelle al fine di recuperare specifici dati mediante l'utilizzo di clausole "WHERE" all'interno di un'istruzione SQL. Questa funzionalità permette di definire una tabella di destinazione, chiamata "Figlio", sulla quale viene eseguita la clausola "WHERE", mentre la tabella di partenza è chiamata "Padre" |
| Relazione.Attributo | Convenzione utilizzata in Application Designer per fare riferimento a un attributo specifico di una tabella nel database di Maximo relazionata a quella corrente |
| Sigoption | Le sigoption (opzione di firma) permettono di specificare i privilegi per l’utilizzo di applicazioni, opzioni di menu ed elementi della barra degli strumenti. Inoltre, esistono sigoption condizionali che consentono di concedere o revocare dinamicamente i privilegi di un utente in base a espressioni condizionali. |
| MX Loader | Foglio excel che consente di: Caricare dati in Maximo Scaricare determinati dati presenti nel DB Maximo, tramite l’impostazione di opportune query all’interno del file |
| Listener | È un componente standard di Maximo che monitora eventi specifici, come la creazione o l’aggiornamento di record in una tabella, e intraprende azioni in risposta a questi eventi. |
| Object structure | È una configurazione che definisce come i dati vengono organizzati e scambiati tra Maximo e altri sistemi. Ovvero, un modello che specifica quali oggetti e campi di dati sono inclusi nei messaggi di integrazione. |
| Sistema esterno | È un'entità EAM necessaria per poter effettuare degli scambi con altri sistemi. Il sistema esterno non è che un raccoglitore di canali di pubblicazione ed eventuali sistemi esterni. |

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche, già descritte ad alto livello nel documento “EAM_Analisi Funzionale_Sprint_5_v2.1.docx”, verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software.

Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni implementate sulla piattaforma EAM di Terna (ambiente Dev) e gli sviluppi custom da implementare. Ciascun paragrafo (dedicato a una specifica funzionalità nell’applicativo) presenta due sotto-paragrafi: nel primo sono illustrate le configurazioni eseguite sul software (e.g., estensione del modello dati, configurazione dell’applicazione, gestione della condizionalità, etc.), nel secondo gli sviluppi necessari per soddisfare le funzionalità richieste durante lo sprint.

In particolare, i temi inclusi nello Sprint 5 sono i seguenti:

Creazione e delibera dell’indisponibilità

Esito della richiesta

Recupero/alimentazione da fonti esterne – Agile 2.0

# Sprint 5: Gestione indisponibilità

## Creazione e delibera dell’indisponibilità

La richiesta di indisponibilità, pianificata e deliberata in Agile 2.0, serve per garantire che gli asset sui quali si intende effettuare un intervento di manutenzione / sostituzione, nonché quelli interferenti, siano fuori servizio durante il periodo programmato per l’esecuzione del lavoro.

### Configurazioni

Per la gestione dei dati scambiati tra EAM e Agile 2.0 nel processo di creazione e delibera dell’indisponibilità è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito viene riportato in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4INDISPWO | X4CLUSTER | Cluster titolare | ALN | 20 |
| X4INDISPWO | INIZIOINDISPONIBILITA | Data Inizio Indisponibilità | DATETIME |  |
| X4INDISPWO | FINEINDISPONIBILITA | Data Fine Indisponibilità | DATETIME |  |
| X4INDISPWO | STATUS | Stato richiesta | ALN | 20 |
| X4INDISPWO | WONUM | Ordine di lavoro relazionato | UPPER | 10 |
| X4INDISPWO | SITEID | Site dell’ordine di lavoro relazionato | UPPER | 8 |
| X4INDISPWO | ESITO | Esito | ALN | 2 |
| X4INDISPWO | MOTIVOANN | Motivazione dell’annullamento | ALN | 15 |
| X4INDISPWO | IDERRORE | Codice errore | ALN | 20 |
| X4INDISPWO | DESCRERRORE | Descrizione errore | ALN | 50 |
|  |  |  |  |  |
| JOBPLAN | X4INDISPONIBILITA | Indisponibilità | YORN |  |

| WORKORDER | X4CLUSTERINDISP | Cluster titolare dell’indisponibilità | ALN | 20 |
| --- | --- | --- | --- | --- |
| WORKORDER | X4DINIZIOINDISP | Data Inizio Indisponibilità | DATETIME |  |
| WORKORDER | X4DFINEINDISP | Data Fine Indisponibilità | DATETIME |  |
| WORKORDER | X4STATUSINDISP | Stato richiesta indisponibilità | ALN | 20 |
| WORKORDER | X4SEDEINDISP | Sede di indisponibilità | ALN | 20 |
| WORKORDER | X4SEDEINDISPL1 | Sede di indisponibilità di primo livello | ALN | 20 |
| WORKORDER | WONUM | Ordine di lavoro relazionato | UPPER | 10 |
| WORKORDER | X4MOTIVOINDISP | Motivo dell’annullamento dell’indisponibilità | ALN | 15 |
| WORKORDER | X4ESITOINDISP | Esito indisponibilità | ALN | 2 |
| WORKORDER | X4ERRORINDISP | Codice errore dell’indisponibilità | ALN | 20 |
| WORKORDER | X4DESCERRORINDISP | Descrizione errore dell’indisponibilità | ALN | 50 |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

WORKORDER: Tabella standard di Maximo che permette la gestione degli ordini di lavoro

JOBPLAN: Tabella standard di Maximo che permette la gestione dei piani di lavoro

X4INDISPWO: Tabella custom di Maximo che permette la gestione dei campi relativi alle richieste di indisponibilità

Per permettere la visione dei campi necessari allo scambio dati tra EAM ed Agile 2.0, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “WOTRACK_D” come duplicato dell’applicazione standard “WOTRACK”, si dovranno configurare i campi di testata del tab “Indisponibilità”, caratterizzato dalla sigoption “X4TABINDISP”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Cluster titolare | X4CLUSTERINDISP |  |  | RO |
| Codice OdL | WONUM |  |  | RO |
| Data inizio indisponibilità | X4DINIZIOINDISP |  |  | RO |
| Data fine indisponibilità | X4DFINEINDISP |  |  | RO |
| Stato Cluster indisponibilità EAM | X4STATUSINDISP |  |  | RO |
| Motivazione (MOD) | X4MOTIVOINDISP |  |  | RO |

| Esito | X4ESITOINDISP |  |  | RO |
| --- | --- | --- | --- | --- |

| Codice errore | X4IDERRORINDISP |  |  | RO |
| --- | --- | --- | --- | --- |
| Descrizione errore | X4DESCERRORINDISP |  |  | RO |

La sigoption “RO” rende il campo su cui è applicata non editabile dall’utente

La sigoption “X4TABINDISP” mostra il tab “Indisponibilità” se il flag JOBPLAN.X4INDISPONIBILITA è pari a 1.

La tabella “Storico delle richieste”, caratterizzata dalla relazione “X4INDISP” (che relazione il WORKORDER aperto al momento con la tabella X4INDISPWO mediante where clause: :WONUM=WONUM AND SITEID=:SITEID):

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Inizio indisponibilità | X4INDISP.INIZIOINDISPONIBILITA |  |  | RO |
| Fine indisponibilità | X4INDISP.FINEINDISPONIBILITA |  |  | RO |
| Stato Cluster indisponibilità EAM | X4INDISP.STATUS |  |  | RO |
| Motivazione | X4INDISP.MOTIVOANN |  |  | RO |
| Cluster titolare | X4INDISP.CLUSTER |  |  | RO |
|  |  |  |  |  |

La relazione “X4INDISP” è una relazione che permetterà di estrarre i record dalla tabella “X4INDISPWO” a parità di WONUM e SITEID tra la tabella di origine (WORKORDER) e quella di destinazione (X4INDISPWO).

Per permettere la gestione degli stati della richiesta di indisponibilità, si prevedono le seguenti configurazioni a sistema:

Estensione del dominio del campo “Stato Cluster indisponibilità EAM”

Per la gestione degli stati della richiesta di indisponibilità, sarà esteso il dominio degli stessi con i seguenti stati:

INVIATO

IN LAVORAZIONE

SOSPESO

ANNULLATO

DELIBERATO

IN ESECUZIONE

CONSUNTIVATO.

### Sviluppi

Per permettere la determinazione della motivazione di annullamento dell’indisponibilità, si prevede il seguente sviluppo a sistema:

Motivazione annullamento

Il campo MOTIVOANN della tabella X4INDISPWO sarà utilizzato in EAM solo in caso di annullamento della richiesta di indisponibilità e può essere valorizzato con i seguenti valori:

Dispacciamento.

Un automation script permetterà la gestione di questo campo come segue:

Lo script avrà un punto di avvio: uno l’oggetto X4INDISPWO (tabella in cui scrive l’integrazione).

Sulla base del punto di avvio triggerato si comporterà come di seguito:

nel caso in cui il punto di avvio triggerato sia quello sulla X4INDISPWO, allora lo script verificherà lo stato della nuova riga inserita nella tabella X4INDISPWO dall’integrazione e, se pari ad “ANNULLATO”, imposterà l’attributo (se Null da Agile) della stessa “MOTIVOANN” della stessa riga pari a “Dispacciamento”;

Per permettere l’elaborazione dell’indisponibilità, si prevede il seguente sviluppo a sistema:

Elaborazione dell’indisponibilità

Alla ricezione della delibera di indisponibilità (mediante il flusso di integrazione denominato NotificaPeriodoIndisponibilitàOdL) nell’apposita tabella X4INDISPWO, un automation script verrà avviato in modo che il contenuto dell’ultima delibera venga copiato anche nella tabella WORKORDER. L’automation script, dunque, copierà il contenuto dell’ultima delibera ricevuta nei campi X4CLUSTERINDISP, X4DFINEINDISP, X4DINIZIOINDISP, X4ESITOINDISP, X4STATUSINDISP, X4MOTIVOINDISP, X4IDERRORINDISP e X4DESCERRORINDISP di WORKORDER, prelevando il contenuto dei campi CLUSTER, INIZIOINDISPONIBILITA, FINEINDISPONIBILITA, ESITO, STATUS, MOTIVOANN, IDERRORE e DESCERRORE di X4INDISPWO.

Per permettere la gestione della richiesta di indisponibilità, si prevedono i seguenti sviluppi a sistema:

Invio automatico dell’OdL ad Agile 2.0

A seguito della valorizzazione dei campi obbligatori dell’OdL: Codice OdL (WONUM), Descrizione OdL (DESCRIPTION), Tipo spesa (X4TIPOSPESA), Responsabile attività (OWNERGROUP), Asset (ASSETNUM),, Data pianificata inizio (TARGSTARTDATE), Data pianificata fine (TARGCOMPDATE), PdL (JPNUM) o della modifica degli stessi, l’OdL viene inviato automaticamente verso Agile 2.0. In particolare per permettere questo funzionamento, viene creato un “listener” in ascolto sulla tabella degli ordini di lavoro che segnalerà la presenza di un nuovo ordine o della modifica di un ordine già esistente nel caso in cui i campi obbligatori sopra descritti siano stati popolati e che il PdL specificato per l’ordine preveda l’indisponibilità (cioè abbia X4INDISPONIBILITA della tabella JOBPLAN posto a True). Lo STATUS della tabella X4INDISPWO passa in “INVIATO”. Si rimanda al capitolo di dettaglio dell’integrazione per maggiori dettagli.

Per permettere la visualizzazione del messaggio informativo che richiede conferma all’utente per confermare la modifica, si prevedono le seguenti configurazioni a sistema:

Modifiche dei campi obbligatori OdL a seguito della creazione dell’indisponibilità su Agile 2.0

Quando l’utente effettua delle modifiche ai campi obbligatori dell’OdL relazionati alla richiesta di indisponibilità (Codice OdL (WONUM), Descrizione OdL (DESCRIPTION), Tipo spesa (X4TIPOSPESA), Responsabile attività (OWNERGROUP), Asset indisponibilità (ASSETNUM), , Data pianificata inizio (TARGSTARTDATE), Data pianificata fine (TARGCOMPDATE), PdL (JPNUM), uno script di automazione, triggerato al salvataggio della modifica dei campi, evoca un servizio di warning che genera un Alert non bloccante “Le modifiche potrebbero avere impatti su Agile 2.0”.

## Esito della richiesta

L’esito della richiesta di indisponibilità, visualizzabile nel centro di controllo e nel tab “Indisponibilità” di Monitoraggio OdL, viene utilizzato dall’utente per verificare se la richiesta è stata deliberata e rispetta le date richieste oppure se è necessario ripianificare l’attività in base a quanto deliberato.

### Configurazioni

Per permettere la storicizzazione delle richieste di indisponibilità, si prevede la seguente configurazione a sistema:

Storicizzazione delle richieste di indisponibilità

Nel caso di annullamento/invio di una nuova delibera di indisponibilità da parte di Agile 2.0 verso EAM, le informazioni verranno veicolate mediante un messaggio inviato verso l’opportuno web service esposto da EAM(NotificaPeriodoIndisponibilitàOdL) e quindi salvateall’interno della tabella X4INDISPWO. Tutte le richieste verranno manutenute nella tabella, indipendentemente dal loro stato, per garantirne la visualizzazione storica.

Per permettere la visualizzazione delle richieste di indisponibilità nel centro di controllo, si prevede la seguente configurazione a sistema:

Centro di controllo delle richieste di indisponibilità associate agli OdL

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista riepilogativa degli ordini di lavoro caratterizzati da una richiesta d’indisponibilità; nel dettaglio, gli OdL con esito positivo vengono evidenziati in verde, mentre quelli associati a richieste in stato IN LAVORAZIONE / INVIATO e con esito negativo vengono evidenziati, rispettivamente, in arancione e rosso. Il result set “Esito delle richieste di indisponibilità” mostrerà le seguenti colonne: ID OdL (WONUM), SAP ID (ASSET.X3SAPID), Asset di 1° livello (descrizione del primo livello dell’asset corrente, utilizzando la relazione WORKORDER.X3ASSET1LVL), ID PdL (JPNUM), Data inizio pianificata (TARGSTARTDATE), Data fine pianificata (TARGCOMPDATE), Data inizio indisponibilità (X4DINIZIOINDISP), Data fine indisponibilità (X4DFINEINDISP), Stato della richiesta (X4STATUSINDISP), Esito della richiesta (X4ESITOINDISP).

Gli ordini di lavoro mostrati all’interno del result set saranno tutti quegli ordini con uno stato compreso tra l’aperto e l’eseguito.

### Sviluppi

Per permettere la determinazione dell’esito di indisponibilità, si prevede il seguente sviluppo a sistema:

Calcolo dell’esito della delibera dell’indisponibilità

Per attribuire l’esito “OK” all’indisponibilità con stato DELIBERATO, si deve verificare che la data di inizio dell’indisponibilità coincida o sia successiva alla data di inizio pianificata e che la data di fine pianificata coincida o sia successiva alla data di fine indisponibilità, ovvero che INIZIOINDISPONIBILITA (X4INDISPWO) sia maggiore o uguale a TARGSTARTDATE (WORKORDER) e TARGCOMPDATE (WORKORDER) sia maggiore o uguale a FINEINDISPONIBILITA (X4INDISPWO).

Inoltre, l’esito “OK” deve essere attribuito di default dal sistema alle richieste di indisponibilità che presentano i seguenti stati in EAM:

IN ESECUZIONE

CONSUNTIVATO.

La valutazione sopra descritta verrà effettuata da un automation script che valuterà i campi della riga di X4INDISPWO ricevuta tramite integrazione e i campi interessati della rispettiva riga di WORKORDER (X4INDISPWO.WONUM=WORKORDER.WONUM and X4INDISPWO.SITEID=WORKORDER.SITEID) e popolerà di conseguenza con la logica sopra descritta il campo ESITO della tabella X4INDISPWO.

Nel caso in cui l’indisponibilità sia in stato DELIBERATO e non siano rispettate le condizioni sopra descritte sarà assegnato l’esito “KO”.

Inoltre, l’esito “KO” deve essere attribuito di default dal sistema alle richieste di indisponibilità che presentano i seguenti stati in EAM:

ANNULLATO

SOSPESO.

Nel dettaglio, all’inserimento di una nuova riga nella tabella X4INDISPWO, viene prima valutato l’attributo STATUS: se lo status è pari a IN ESECUZIONE o CONSUNTIVATO, l’AS porrà l’esito pari ad OK senza ulteriori verifiche; se lo status è pari ad ANNULLATO o SOSPESO, l’AS porrà l’esito pari a KO senza ulteriori verifiche; per i restanti casi, l’AS valuterà le date dell’indisponibilità (INIZIOINDISPONIBILITA e FINEINDISPONIBILITA) e le confronterà con le date di pianificazione dell’ordine a cui si collega l’mbo appena inserito (mediante relazione :WONUM=WONUM and SITEID=:SITEID). Se TARGSTARTDATE (inizio pianiifcazione) è minore o uguale INIZIO INDISPONIBILITA e TARGCOMPDATE (fine pianificazione odl) è maggiore o uguale di FINEINDISPONIBILITA, allora l’esito sarà pari ad OK; in caso contrariol l’esito sarà KO.

Il ricalcolo dell’esito avviene ad ogni modifica apposta ai campi TARGSTARTDATE e TARGCOMPDATE oltre che al momento di ricezione di una nuova riga nella tabella X4INDISPWO.

Nel caso in cui l’indisponibilità sia in stato IN LAVORAZIONE o INVIATO, l’esito della richiesta (ESITO di X4INDISPWO) sarà non definito e viene mostrato dal sistema con il simbolo “-“.

## Recupero/alimentazione da fonti esterne – AGILE 2.0

### Configurazioni Maximo

Per poter leggere e scrivere oggetti di Maximo, verranno elencati i Object Structure:

| Per la gestione dei dati inerenti alle richieste di indisponibilità è necessario configurare un business object che verrà censito col seguente nome “X4ODLAGILE” per poter usufruire correttamente dei dati all’interno del database sfruttato dall’applicativo. Sarà inoltre necessario configurare: Canale di pubblicazione (x4-invio-indisponibilita-agile). Sistema esterno (X4ODLAGILE) Configurazione coda kafka (eam-send-odl-to-agile). Di seguito vengono riportati in formato tabellare gli attributi necessari per la creazione del business object: |  |
| --- | --- |

### Flussi d’integrazione

In questa sezione si prefigge di descrivere il flusso che intercorre tra EAM e il sistema AGILE 2.0 e le relative interazioni.

Di seguito diagramma di interazioni tra EAM ed AGILE:

Il sistema EAM invia ad AGILE 2.0 tutti gli Ordini di Lavoro in cui è necessario che venga richiesta una finestra di indisponibilità di un asset della rete per poter effettuare l’attività. L’informazione è specificata in un campo apposito nel Piano di Lavoro all’interno dell’Ordine di lavoro. Il flusso che si occuperà di questa operazione è “RichiestaPeriodoIndisponibilitàOdL”.

In risposta al flusso che inoltra la richiesta di indisponibilità AGILE 2.0 comunicherà al sistema EAM in maniera asincrona l’esito e/o l’aggiornamento dello stato della richiesta relativa all’ordine di lavoro. Il flusso che si occuperà di questa operazione è “NotificaPeriodoIndisponibilitàOdL”.

#### Logiche di attivazione e tipologia d’interazione

L’innesco avviene ogni volta che un OdL, che contiene un piano di lavoro che prevede indisponibilità, passa in stato “Aperto” (Gli ordini di lavoro che verranno inviati ad AGILE 2.0 sono quelli che hanno un PdL (attributo JPNUM della WORKORDER) che ha lo stato = ‘ACTIVE’). L’innesco viene percepito dal publish channel x4-invio-indisponibilita-agine

Un’ulteriore modalità di innesco del flusso si verifica quando il set di informazioni dell'OdL sul sistema EAM subisce un aggiornamento/modifica dei campi. In questo caso, entrambi i sistemi devono essere allineati/aggiornati (EAM/AGILE).

Alla generazione degli ordini di lavoro, è associato un publish channel(x4-invio-indisponibilita) che genererà un messaggio da inviare tramite coda kafka(eam-send-odl-to-agile) al microservizio eam-integration, il quale provvederà ad inoltrarlo alla sua destinazione naturale, il sistema AGILE, dopo aver effettuato le opportune trasformazioni per la request di tipo SOAP.

Conseguentemente all’innesco del flusso RichiestaPeriodoIndisponibilitàOdL sarà innescato in risposta ed in modo asincrono anche il flusso NotificaPeriodoIndisponibilitàOdL.

Di seguito la lista dei flussi:

| Nome flusso | Tipologia d’integrazione | Tecnologia | Autenticazione |
| --- | --- | --- | --- |
| RichiestaPeriodoIndisponibilitàOdL | APIM | SOAP/XML | OAuth2 |
| NotificaPeriodoIndisponibilità | APIM | SOAP/XML | OAuth2 |

#### Flusso RichiestaPeriodoIndisponibilitàOdL

Ogni qualvolta che l’OdL cambia stato/subisce un aggiornamento e rispetta i criteri di invio verso AGILE 2.0, verrà inviato un nuovo messaggio su coda kafka che sarà censita col nome “eam-send-odl-to-agile” e che conterrà le informazioni relative all’OdL;

Questo messaggio verrà acquisito da un kafka listener in ascolto sulla coda, che consumerà il messaggio ricevuto e avvierà le elaborazioni successive volte al fine di chiamare un API esposta da AGILE 2.0 .

Il messaggio kafka inviato al microservizio integration dovrà contenere le seguenti informazioni:

{

"wonum": "ODL12345",

“status”:”APERTO”,

"description": "Manutenzione ordinaria stazione elettrica",

“assetnum”: “1146323” [Necessario per il recupero di “Unità Impianti responsabile dell’attività” dalla tabella ASSET],

“siteid”: “SIT01” [Necessario per il recupero di “Unità Impianti responsabile dell’attività” dalla tabella ASSET],

"jpnum": "Piano Manutenzione Stazione 2024",

"tipologia_attivita": "Manutenzione Preventiva",

"targstartdate": "2024-09-15",

"targcomdate": "2024-09-20",

"x4wbs": "WBS001-ABC",

"x4tipospesa": "Capitale",

“ownergroup”:”DTNRAT”

“PLUSCJPREVNUM”:7

}

Il topic kafka da configurare sarà censito in questo modo “eam-send-odl-to-agile”, di seguito tutti i campi necessari per la configurazione sul microservizio “eam-integration”:

| Proprietà | Valore |
| --- | --- |
| nome del nodo | eamAgileInvioIndisponibilita |
| entityName | “__kafkaSendOdlAgile__” valorizzata con => eam-send-odl-to-agile |
| entityType | TOPIC |
| entityDescription | Kafka topic for sending OdL unavailability requests ti AGILE 2.0 |
| entityObjectName | Send OdL to AGILE 2.0 |
| entityObjectClass | It.terna.nas.service.material.dto.kafkaOdlToAgileRequest |
| entityUuidMethodName | getWonum |

Acquisito il messaggio inviato dall’applicativo MAXIMO e conseguentemente le relative informazioni sull’OdL, si procederà all’arricchimento dell’informazione per poter eseguire l’Invio verso AGILE 2.0

È previsto il recupero degli attributi riguardo:

Unità Impianti responsabile dell’attività

Codici Odl aggregati/figli(se presenti)

Sede tecnica di indisponibilita

Sedi tecniche interferenti

Effettuando un accesso diretto al DB Maximo. Dovranno essere eseguite le seguenti query:

Query 2: Recupero codici OdL aggregati/figli

Recuperati I codici OdL relativi al padre questi verranno acquisiti per l’invio verso AGILE 2.0 nel campo “Lista Codici di Lavoro”. Se la query non restituisce risultati sarà inviato soltanto il wonum ricevuto nel messaggio kafka.

Query 3: Recupero elemento di indisponibilità

Query 4: Recupero del TABLEVALUE degli asset interferenti

Query 5: Recupero della sede tecnica “padre”

Dopo aver recuperato le informazioni necessarie sarà poi possibile procedere al completamento della request da inviare verso verso AGILE 2.0, utilizzando il protocollo SOAP, attraverso l’endpoint (esposto da AGILE 2.0).

La struttura SOAP messa a disposizione da AGILE è la seguente:

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| Codice Ordine di Lavoro (OdL) | Identificativo OdL EAM | Y |  |
| Descrizione ODL | Descrizione dell'attività generante l'ordine di lavoro | Y |  |
| Stato ODL | Stato EAM dell’ordine di lavoro, ha lo scopo di comunicare l’annullamento della richiesta di indisponibilità. | Y |  |
| Unità Impianti responsabile dell’attività | Unità Impianti che è definita come responsabile dell'attività da catalogo tassonomico (es. DTNVSG) | Y |  |
| Piano di Lavoro | Codice PdL EAM | Y |  |
| Richiesta indisponibilità | Flag di richiesta d’indisponibilità, ha lo scopo di far percepire ad AGILE la casistica di annullamento della richiesta d’indisponibilità da parte di EAM | Y |  |
| Tipologia attività(aggregatore per AGILE 2.0) | Mapping di aggregazione tra piano di lavoro e tipologia attività su AGILE 2.0 | N |  |
| <Lista di codici di lavoro figli> | Lista di codici identificativi degli OdL EAM(Lista dedicata agli OdL aggregati/figli) | N |  |
| Codice Ordine di lavoro figlio | Identificativo OdL EAM | N |  |
| </Lista di codici di lavoro figli> |  |  |  |
| Data inizio pianificata | Data inizio pianificata della singola attività | Y |  |
| Data fine pianificata | Data fine pianificata della singola attività | Y |  |
| Sede tecnica d’indisponibilità padre | Sede tecnica d’indisponibilità legata all’Asset di tipo Linea o Stazione (gerarchicamente legato all’asset reale dell’ordine di lavoro) | Y |  |
| <Sedi tecniche indisponibilità> | Lista di sedi tecniche d’indisponibilità associata alla sede tecnica Reale e presente in anagrafica(sede tecnica indisponibilità master). Verranno veicolate nel dettaglio i codici di ST indisponibilità strettamente connesse all’asset oggetto dell’odl. | Y |  |
| Codice sede tecnica indisponibilità | Codice della sede tecnica di indisponibilità. | Y |  |
| </Sedi tecniche indisponibilita> | - |  |  |
| <Sedi tecniche interferenti> | Lista delle sedi tecniche reali Interferenti alla sede oggetto attività. | N |  |
| Sede tecnica interferente | Codice della sede tecnica interferente. | N |  |
| </Sedi tecniche interferenti> | - | - |  |
| WBS (CBS) | Informazione recuperata dall’OdL, in caso di intervento ad investimento si potrebbe inizialmente inserire la “WBS o WBE prevalente”. | N |  |
| Tipo Spesa | Tipo di ordine di lavoro (CAPEX, OPEX ).Tipo Spesa | Y |  |
| Esito | Esito invio vs Agile (OK, KO) | Y |  |
| Codice errore | Codice dell'errore registrato | N |  |
| Descrizione errore | Descrizione esplicativa dell'errore registrato | N |  |

AGILE 2.0 gestirà le seguenti casistiche d’errore:

Unità Impianti responsabile dell’attività non censita su Agile e quindi non riconosciuta

Sede tecnica non censita su Agile e quindi non riconosciuta

Impossibilità di gestire la richiesta da parte di Agile

In base alla risposta ricevuta da AGILE riguardo il corretto invio delle informazioni verrà tracciato l’esito dell’elaborazione sulla tabella X4ESITOINT. A tale scopo verrà usato il Business Object Structure X4ESITOINT.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00", "esito": "OK", "idinterfaccia": "SEND_ODL_TO_AGILE", "desc_esito": "XYZ", "source": "EAM", "dest": "AGILE", "uniqueid": "HUDISAIU383KO9", "end_elab": "2024-06-21T15:30:08+00:00"

}

In qualsiasi flusso di Agile, per il quale si è riscontrato un errore applicativo/comunicazione è stato realizzato un motore che rilancerà per n-volte il messaggio ad Agile nel tentativo di ottenere un esito positivo nella comunicazione. Il numero di tentativi è configurabile attraverso variabili d’ambiente di Open Shift.

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su AGILE 2.0 rilanciando la request verso AGILE 2.0. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

#### Flusso NotificaPeriodoIndisponibilitàOdL

Per la ricezione della notifica del periodo indisponibilità è necessario che EAM esponga un API SOAP, la quale verrà chiamata dal sistema AGILE.

L’api SOAP è esposta al seguente uri:

“POST https://<host:port>/eam/ws/basic/notificaIndisponibilita.wsdl”

SOAPAction = http://soa.terna.it/EAM/notificaIndisponibilita/1.0.

Esempio di Request SOAP:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ns="http://soa.terna.it/EAM/notificaIndisponibilita/1.0"> <soapenv:Header/> <soapenv:Body> <ns:notificaIndisponibilitaRequest> <codiceOdL>12000</codiceOdL> <codiceClusterTitolare>ABCD</codiceClusterTitolare> <!--Optional:--> <dataInizioProgrammazione>1725400800</dataInizioProgrammazione> <!--Optional:--> <dataFineProgrammazione>1725573600</dataFineProgrammazione> <statoIndisponibilita>ANNULLATO</statoIndisponibilita> <!--Optional:--> <MOD>INTERVENTO RIMANDATO</MOD> </ns:notificaIndisponibilitaRequest> </soapenv:Body> </soapenv:Envelope>

Una volta ricevuta la request, eam-integration provvederà ad eseguire una serie di controlli di validazione:

Controllo della valorizzazione effettiva del OdL di risposta(NOT NULL).

Controllo sull’esistenza del codice OdL su sistema EAM (Workorder)

Se tutti i controlli sono validi, provvederemo ad aggiornare inserendo un nuovo record nella tabella X4INDISPWO richiamando l’object structure X4INDISPAGILE.

L’URI per l’object structure di X4INDISPAGILE da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4INDISPAGILE

La request da passare all’API è la seguente:

{

"wonum": "12000", "description": " ABCD ", " inizioindisponibilita": "1725400800", " fineindisponibilita ": "1725573600", "status": "ANNULLATO", "motivoann": "INTERVENTO RIMANDATO"

}

Esempio di Response SOAP:

<soapenv:Envelope xmlns:soapenv=”http://schemas.xmlsoap.org/soap/envelope/” xmlns:ns=" http://soa.terna.it/EAM/notificaIndisponibilita/1.0"> <soapenv:Header/> <soapenv:Body>

<ns:notificaIndisponibilitaResponse> <ESITO>KO </ESITO> <!--Optional:--> <CODICE_ERRORE>EAM-0001</CODICE_ERRORE> <!--Optional:--> <DESCRIZIONE_ERRORE> Campi in input non valorizzati correttamente

</DESCRIZIONE_ERRORE>

</ ns:notificaIndisponibilitaResponse > </soapenv:Body> </soapenv:Envelope>

In tutti i casi verrà tracciato l’esito dell’ elaborazione sulla tabella X4ESITOINT. A tale scopo verrà usato il Business Object Structure X4ESITOINT.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00", "esito": "KO", "idinterfaccia": "SEND_ODL_TO_AGILE", "desc_esito": "XYZ", "source": "EAM", "dest": "AGILE", "uniqueid": "HUDISAIU383KO9", "end_elab": "2024-06-21T15:30:08+00:00"

}

#### Interface agreement flusso AGILE – NotificaPeriodoIndisponibilitàOdL

La specifica d’interfaccia del flusso è riportata nel documento:

“InterfaceAgreement_EAM_AGILE_V2.docx”.

#### Gestione d’errore

In qualsiasi flusso di Agile, per il quale si è riscontrato un errore applicativo/comunicazione è stato realizzato un motore che rilancerà per n-volte il messaggio ad Agile nel tentativo di ottenere un esito positivo nella comunicazione. Il numero di tentativi è configurabile attraverso variabili d’ambiente di Open Shift.

#### Modello Relazione

Di seguito verranno riportate le tabelle usate all’interno dei flussi con i relativi dettagli:

WORKORDER:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| WONUM | VARCHAR2(25 CHAR) | Y | PK |
| DESCRIPTION | VARCHAR2(100 CHAR) | Y | Descrizione dell'attività generante l'ordine di lavoro |
| JPNUM | VARCHAR2(12 CHAR) | Y | Codice PdL EAM |
| TO DO(Tipologia attività(aggregatore per AGILE 2.0)) | - | N |  |
| TARGSTARTDATE | DATE | Y | Data inizio pianificata della singola attività. |
| TARGCOMDATE | DATE | Y | Data fine pianificata della singola attività |
| X4WBS | VARCHAR2(20 CHAR) | N | Informazione recuperata dall’OdL, in caso di intervento ad investimento si potrebbe inizialmente inserire la “WBS o WBE prevalente”. |
| X4TIPOSPESA | VARCHAR2(5 CHAR) | Y | Informazione riguardo tipo spesa |

ASSET:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X3INGRP | VARCHAR2(3 CHAR) | Y | Unità Impianti che è definita come responsabilie dell'attività da catalogo tassonomico (es. DTNVSG_I) |
| ASSETNUM | VARCHAR2(25 CHAR) | Y | - |
| SITEID | VARCHAR2(8 CHAR) | Y | - |

X4INDISPWO:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| WONUM | VARCHAR2(25 CHAR) | Y | PK |
| DESCRIPTION | VARCHAR2(50 CHAR) | Y | Codice della delibera dell'indisponibilità. |
| INIZIOINDISPONIBILITA | DATE | N | Data inizio dell'indisponibilità. |
| FINEINDISPONIBILITA | DATE | N | Data fine dell'indisponibilità. |
| STATUS | VARCHAR2(20 CHAR) | Y | Stato dell'indisponibilità |
| MOTIVOANN | VARCHAR2(150 CHAR) | N | Descrive la motivazione riguardo sospensione, annullamento etc.. della richiesta di indisponibilità |

X4ESITOINT:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X3ESITOINTID | NUMBER | Y | PK |
| IDINTERFACCIA | VARCHAR2(50) | N | Nome interfaccia |
| ESITO | VARCHAR2(10) | N | ESITO (Es. OK o KO) |
| COD_ESITO | VARCHAR2(30) | N | Codice d’errore |
| DESC_ESITO | VARCHAR2(2000) | N | Descrizione d’errore |
| UNIQUEID | VARCHAR2(50) | Y | Chiave d’integrazione per flusso |
| ROWSTAMP | VARCHAR2(40) | Y |  |
| DEST | VARCHAR2(50) | N | Sistema destinatario |
| SOURCE | VARCHAR2(50) | N | Sistema d’origine |
| PARENT | NUMBER | N | Parent è FK al campo X3ESITOINTID |
| START_ELAB | DATE | N | Data inizio elaborazione |
| END_ELAB | DATE | N | Data fine elaborazione |
| KAFKA_TOPIC | VARCHAR2(50) | N | Coda Kafka di elaborazione |
| MESSAGE | CLOB | N | Messaggio Json d’input. Viene salvato se necessario |

## Schedulatore per Rielaborare i messaggi in errore

In entrambi i flussi che riguardano l’integrazione con AGILE 2.0, per il quale si è riscontrato un errore applicativo/comunicazione si provvede a realizzare un motore che rilancerà per n-volte il messaggio a AGILE nel tentativo di ottenere un esito positivo nella comunicazione. Il numero di tentativi dev’essere configurabile attraverso variabili d’ambiente di Open Shift.

Il motore sfrutterà i dati presenti sulla tabella X4ESITOINT cercando i record che hanno ESITO = KO, PARENT = null e SOURCE = EAM.

Per ogni record individuato si provvederà ad acquisire il dato presente in MESSAGE ed inserirlo nell’opportuna coda kafka in modo da rilanciare l’elaborazione (campo KAFKA_TOPIC).
