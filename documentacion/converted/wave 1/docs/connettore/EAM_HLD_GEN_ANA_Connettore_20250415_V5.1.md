# EAM_HLD_GEN_ANA_Connettore_20250415_V5.1

|  | Connettore anagrafica – High-Level Design |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Relazione: | Connettore anagrafica – high-level design |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 1.0 | Primo rilascio | 08/06/2023 |
| 1.1 | Feedback revisione ICT Terna | 15/06/2023 |
| 1.2 | Secondo rilascio in base alla revisione ICT Terna | 23/06/2023 |
| 1.3 | Seconda revisione ICT Terna | 7/07/2023 |
| 1.4 | Terzo rilascio in base alla revisione ICT Terna | 18/07/2023 |
| 1.5 | Terza revisione ICT Terna | 24/07/2023 |
| 1.6 | Correzione in base a revisione ICT Terna | 25/07/2023 |
| 1.7 | Aggiunta scenari legati ad operazioni di ricodifica | 06/10/2023 |
| 1.8 | Aggiunta gestione campate fittizie e inversione linea | 09/10/2023 |
| 1.9 | Revisione in base a feedback ICT Terna | 26/10/2023 |
| 1.10 | Revisione in base a feedback ICT Terna | 17/11/2023 |
| 1.11 | Aggiunta stazioni | 22/11/2023 |
| 1.12 | Revisione in base a feedback ICT Terna. I file di mapping stazioni saranno allegati nel paragrafo 5 e 7 al loro completamento | 7/12/2023 |
| 1.13 | Revisione in base a feedback ICT Terna | 8/01/2024 |
| 1.14 | Aggiunta scenari speciali stazioni e aggiornamento file di mapping per linee e stazioni | 11/01/2024 |
| 3.9 | Revisione in base a feedback ICT Terna | 31/01/2024 |
| 4.0 | Evidenziazione degli elementi per cui si richiede nuova approvazione da parte del BSN | 09/02/2024 |
| 4.1 | Aggiunta gestione classi SAP e documenti di riferimento per attributi di testata | 14/02/2024 |
| 5.0 | Realizzazione open point 252, 305, 253, 256, 254 | 25/03/2025 |
| 5.1 |  |  |
|  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | RTI | 08/06/2023 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

# Introduzione

Il presente documento descrive il disegno ad alto livello (high-level design) del connettore EAM dedicato alla sincronizzazione della anagrafica di linee e stazioni in formato to-be gestita sulla piattaforma EAM (IBM MAS Manage). La sincronizzazione prevede

un flusso in-bound per la creazione in EAM di un primo livello linea/stazione esistente in SAP

flussi out-bound per la sincronizzazione della linea/stazione EAM verso SAP

flussi di sincronizzazione di anagrafiche accessorie (non tecniche); parliamo nel dettaglio di sedi tecniche di indisponibilità e centri di lavoro da SAP verso EAM

I formati delle due anagrafiche differiscono sia per la struttura gerarchico-topologica, sia per il livello di dettaglio. In particolare, l’anagrafica to-be modella linee e stazioni mediante 4 livelli di profondità a differenza del formato FDD che modella le linee mediante 3 livelli e le stazioni mediante 5 livelli. Inoltre, per quanto riguarda le linee, l’anagrafica to-be rappresenta i secondi livelli comuni (sostegni e campate) come singoli asset collegati al primo livello mediante relazioni e non attraverso la singola gerarchia padre-figlio. Questo tipo di approccio permette di rappresentare un asset fisico condiviso da più linee mediante una rappresentazione unica a sistema, a differenza del modello FDD in cui la sede tecnica è duplicata.

Figura 1. Struttura data model to-be linee

Si riporta per completezza il data model to-be stazioni per il quale la gerarchia è esclusivamente basata su relazioni padre-figlio.

Figura 2. Struttura data model to-be stazioni

Inoltre, la nuova struttura della anagrafica to-be ricolloca la posizione di alcune tipologie di asset (ad esempio i sostegni vengono promossi dal terzo livello al secondo) e dettaglia gli asset aggregatori (ad esempio l’aggregatore per i conduttori di terzo livello viene dettagliato mediante singoli conduttori di quarto livello).

Il connettore oggetto del presente documento è un componente applicativo di integrazione fra EAM e SAP che ha lo scopo di sincronizzare da e verso SAP l’anagrafica di linee e stazioni gestite su EAM trasformando l’anagrafica dal formato to-be al formato FDD e viceversa.

Figura 3. Mappa applicativa Terna

Questa trasformazione si applicherà alla gerarchia, agli attributi e alle specifiche tecniche degli asset.

Il documento fornirà la specifica funzionale ad alto livello del connettore dettagliando:

data-model usato dal connettore

flussi di trasformazione della anagrafica

flussi di integrazione

gestione casistiche di errore

Inoltre, poiché il connettore opererà temporalmente a valle dei processi di caricamento della anagrafica SAP (ETL) e della gestione della stessa su EAM (anagrafica asset), il suo design all’interno del presente documento sarà armonizzato con il design di queste altre due componenti dettagliando i punti di raccordo e i prerequisiti.

## IBM MAS Manage

IBM MAS Manage è l’applicazione per la gestione degli asset (Enterprise Asset Management) fornita dalla suite MAS (Maximo Application Suite). La suite fornisce anche altre applicazioni orientate ad aspetti di gestione avanzata degli asset come, ad esempio, l’applicazione Health che permette di definire e controllare KPI (key performance indicator) indicativi dello stato di salute degli asset. La suite inoltre gestisce centralmente aspetti configurativi comuni alle applicazioni quali:

Integrazione con lo user registry di un Identity Provider

Configurazione del licensing

Deployment delle applicazioni su container IBM RedHat Open Shift.

IBM MAS nasce dalla versione 8. Il motivo storico è dovuto al fatto che prima della nascita della suite il prodotto era prettamente una piattaforma di asset management nota come Maximo Enterprise Asset Management o amichevolmente come Maximo. La versione precedente la suite è la versione 7. La versione 8 ha prodotto una significativa trasformazione del prodotto introducendo la suite che include di fatto il Maximo Enterprise Asset Management rinominato in IBM MAS Manage.

IBM MAS Manage è quindi l’applicazione per la gestione degli asset e di tutti i processi di manutenzione degli asset aziendali quali ad esempio interventi, piani di lavoro, manutenzioni programmate, gestione magazzini, movimentazione, approvvigionamento e via discorrendo. Per maggiori dettagli si rimanda alla documentazione di prodotto.

In questo paragrafo si descriveranno alcuni argomenti base relativi alle funzionalità di prodotto che aiuteranno la comprensione del documento.

IBM MAS Manage è una applicazione J2EE (Java 2 Enterprise Edition) pertanto viene eseguita nel contesto di un application server (dalla versione 8 l’unico application server supportato è IBM WAS Liberty). Inoltre, IBM MAS Manage è una applicazione che sfrutta un database relazionale per la memorizzazione dei dati (i database supportati sono Oracle, Db2 e SQL Server).

Un primo concetto fondamentale per la conoscenza del prodotto è il meccanismo di astrazione del dato realizzato da IBM MAS Manage noto come MBO (Maximo Business Object). Il MBO si riferisce tipicamente ad una tabella o vista definita sul database, ma in taluni casi ad un dato non persistente o un flusso di integrazione. Pertanto, il MBO persiste e legge il dato per mezzo di un record di una tabella del database.

Inoltre, il MBO correda il dato di logiche di business mediante codice applicativo scritto per mezzo del linguaggio di programmazione Java (essendo appunto IBM MAS Manage una applicazione J2EE). Inoltre, è possibile agganciare ulteriori logiche di business mediante automation script (script di automazione) ovvero codice applicativo scritto in linguaggio Javascript, Python, Jython e connesso ad eventi del ciclo di vita del MBO (launch point). Il prodotto offre out-of-the-box una moltitudine di MBO pensati per la gestione degli asset.

La gestione delle configurazioni degli MBO è possibile mediante l’applicazione amministrativa nota come Configurazione Database. Tale applicazione permette la modifica di MBO di prodotto nonché l’aggiunta di nuovi MBO (custom) necessari per plasmare il prodotto in base ai casi d’uso cliente. Un MBO è caratterizzato da una notevole quantità di configurazioni. Si elencano i principali aspetti configurativi necessari per una buona comprensione del documento.

Segmentazione dati: IBM MAS Manage fornisce una struttura articolata per la segmentazione dei dati rispetto a unità organizzative permettendo di confinare facilmente la visibilità dei dati. Tipicamente la segmentazione si basa su tre possibili livelli: SYSTEM, ORG e SITE. La segmentazione SYSTEM di fatto indica che il MBO è un dato condiviso a livello di sistema ovvero non è soggetto ad alcuna segmentazione. La segmentazione ORG fissa un primo livello di segmentazione basato sulle organizzazioni definite a sistema. La segmentazione SITE definisce un secondo livello di segmentazione (interno alla organizzazione) basato sui siti definiti a sistema. Si noti che ogni installazione prevede la creazione di almeno una organizzazione e di un sito come prima fase di configurazione. Inoltre, i siti sono relativi alle organizzazioni. Un MBO a livello di organizzazione deve avere obbligatoriamente un attributo ORGID (id della organizzazione). Un MBO a livello di sito deve avere obbligatoriamente un attributo ORGID (id della organizzazione) e un attributo SITEID (id del sito).

Chiave interna: tipicamente ogni MBO ha un attributo di tipo numerico (BIGINT) che identifica univocamente il record all’interno della tabella. Il campo è gestito automaticamente ed è alimentato da una sequenza definita nel database creata dal prodotto per ogni specifica tabella. Pertanto, all’interno dello schema ogni record è univocamente identificabile mediante il nome della tabella e il valore della chiave interna. Tipicamente la chiave interna non costituisce la chiave primaria.

Chiave primaria: ogni MBO permette la definizione di una chiave primaria (insieme di colonne) che identifica univocamente il record all’interno della tabella. Tipicamente tale chiave contiene le colonne relative alla segmentazione (ORGID o SITEID) più altre colonne di dettaglio. Si tenga presente che, sebbene i siti siano definiti all’interno delle organizzazioni, non è possibile avere lo stesso sito all’interno di più organizzazioni. Pertanto, per MBO a livello di sito il SITEID è sufficiente come colonna per la chiave primaria (cioè l’ORGID in questo caso non è necessario). Ad esempio, la tabella ASSET (tabella principale per la memorizzazione degli asset aziendali) essendo un MBO a livello di sito ha come chiave primaria la coppia SITEID e ASSETNUM, dove SITEID indica il sito di appartenenza dell’asset e ASSETNUM un codice alfanumerico (eventualmente autogenerato progressivamente) identificativo dell’asset all’interno del sito. Eventuali riferimenti ad un MBO da altre tabelle devono contenere l’intera chiave primaria (in questo caso riferita come foreign key). Ad esempio, la tabella ASSETSPEC che contiene le specifiche tecniche di un asset ha le colonne SITEID e ASSETNUM che costituiscono la foreign key verso la tabella ASSET.

Attributi: ogni MBO definisce un insieme di attributi che tipicamente sono mappati sulle rispettive colonne di una tabella. Ogni attributo ha un nome che tipicamente corrisponde al nome della colonna. Per ogni attributo è necessario definire il tipo che caratterizza il tipo della colonna. Si tenga presente che i tipi disponibili possono non corrispondere direttamente al tipo definito per la colonna della tabella. Ad esempio, IBM MAS Manage permette di definire il tipo UPPER che di fatto è un tipo alfanumerico ma di soli caratteri maiuscoli. In alternativa alla definizione del tipo è possibile indicare un attributo di riferimento (anche di un altro MBO) da cui verrà ereditata la configurazione del tipo (configurazione nota come same-as). Questa configurazione rende consistente il tipo all’interno del database. Una modifica al tipo base comporterà l’automatica riconfigurazione dei referenti. Per ciascun attributo, fra le altre cose, è possibile indicare il titolo da mostrare nelle applicazioni, la descrizione completa, l’obbligatorietà, il valore di default e l’eventuale sequenza da cui generare il valore.

Domini: i domini sono insiemi di valori statici o dinamici. I domini statici sono costituiti da liste valori configurate a sistema. I domini dinamici (ad esempio il dominio di tipo tabella) selezionano all’occorrenza i record di una tabella mediante una query. I domini sono utilizzati per circoscrivere i possibili valori che può assumere un attributo. È possibile configurare il dominio all’interno della configurazione di un attributo del MBO. Il dominio, se opportunamente generico, può essere utilizzato per più di un attributo.

Relazioni: è possibile definire per ogni MBO (parent) relazioni con altri MBO (children) per mezzo di query SQL che appunto esprimono la logica per recuperare i record del MBO figlio in funzione dei valori del MBO padre.

Alcuni MBO (tra cui gli asset) possono essere soggetti a classificazione. Una classificazione mira a definire la natura del MBO (ad esempio per gli asset di tipo campata si può definire la classificazione CAMPATA AEREA). Inoltre, la classificazione permette di definire un insieme di specifiche tecniche costituite da un nome e un possibile valore (numerico, data, alfanumerico). Una classificazione può avere una classificazione padre e più classificazione figli (ad esempio essendo la campata figlia di una linea la classificazione completa potrebbe essere LINEA \ CAMPATA AEREA). È possibile definire se le specifiche tecniche devono essere propagate alle classificazioni figlie. Una classificazione è identificata univocamente dall’attributo CLASSIFICATIONID. La gerarchia delle classificazioni (tecnicamente chiamata class structure) non deve necessariamente ricalcare il percorso gerarchico dei MBO. Una buona strutturazione della gerarchia delle classificazioni deve essere orientata alla classificazione della natura del MBO (ad esempio per asset generici come gli interruttori si può immaginare una classificazione STRUMENTAZIONE \ INTERRUTTORI anche se di fatto l’interruttore può essere montato in un percorso gerarchico diverso, ad esempio, al di sotto di un trasformatore).

Un altro componente di prodotto degno di nota è il MIF (Maximo Integration Framework) componente necessario per realizzare scenari di integrazione. Il MIF permette di definire oggetti di integrazione (object structures) che rappresentano la composizione di MBO consumati da flussi di integrazione in-bound e out-bound. I flussi in-bound sono definiti mediante canali di richiamo mentre i flussi out-bound sono definiti mediante canali di pubblicazione. I protocolli di comunicazione utilizzabili sono SOAP e REST API. Infine, i flussi possono essere sincroni o asincroni (i messaggi fluiscono mediante code JMS o topic Kafka). Infine, una ulteriore configurazione di prodotto citata nel documento è il CRON-task. IBM MAS Manage offre la possibilità di programmare attività background (per l’appunto task) secondo una programmazione desiderata. Creata una definizione di un CRON-task è possibile definire quante istanze di esecuzione si vogliono. Ogni istanza può essere eventualmente parametrizzata mediante l’impostazione di parametri specifici per l’istanza. Le istanze sono caratterizzate da una propria programmazione e possono essere singolarmente attivate o disattivate.

## Asset Data Model

A puro titolo introduttivo si riporta la parte significativa del data model out-of-the-box per la parte relativa agli asset. I dati principali dell’asset sono memorizzati tramite il MBO ASSET la cui chiave è costituita da SITEID (identificativo del sito in cui è collocato l’asset) e ASSETNUM (codice alfanumerico identificativo dell’asset). La tabella riporta le principali informazioni relative all’asset. Un asset può essere figlio di un altro asset. Pertanto, gli asset possono essere strutturati gerarchicamente secondo la relazione padre-figlio. L’asset è basato su un MBO di tipo stateful, cioè corredato di un ciclo di vita per cui è prevista la storicizzazione dei cambi di stato. L’asset può essere soggetto a classificazione. In tal caso, all’applicazione della classificazione, le specifiche tecniche definite verranno prodotte per lo specifico asset nella tabella ASSETSPEC. Questi record conterranno i valori delle specifiche tecniche per l’asset in questione.

Gli asset possono essere relazionati. È possibile, cioè, indicare una specifica relazione da un asset (source) ad un altro asset (target). Le relazioni sono memorizzate nella tabella ASSETLOCRELATION.

Figura 4. Modello entità-relazioni per gli asset

### Data model linee

La struttura della anagrafica delle linee è calata sul model appena descritto con l’aggiunta di alcune entità (MBO e attributi) che permetteranno la strutturazione del model così come dettato dai requisiti della anagrafica to-be.

Il primo punto chiave consiste nella rappresentazione delle sedi tecniche SAP mediante ASSET EAM. Pertanto, in base ai criteri di mapping analizzati, potremo assumere in linea di massima che una sede tecnica SAP corrisponde ad un ASSET EAM. C’è comunque da tener presente che il data model to-be delle linee ha un livello di dettaglio maggiore rispetto a quello SAP; pertanto, alcuni ASSET EAM (ad esempio i giunti) non avranno una corrispondenza con una sede tecnica SAP. Infine, in alcuni casi (ad esempio la morsetteria) la sede tecnica SAP non avrà una corrispondenza verso un ASSET EAM. Gli ASSET EAM saranno strutturati gerarchicamente secondo una relazione di tipo padre-figlio (ad esempio i giunti saranno asset di quarto livello figli dell’asset aggregatore conduttori di terzo livello, che a sua volta sarà figlio della campata di secondo livello).

Il secondo punto chiave consiste nella gestione della doppia terna per cui un asset di secondo livello della linea (campata e sostegno) può essere condiviso da due linee. Pertanto, in questo caso la relazione padre-figlio non è più congeniale (ad esempio una campata di secondo livello potrebbe essere legata a due linee di primo livello) e verrà utilizzato il concetto di relazione (ASSETLOCRELATION) che permette di legare liberamente un asset a molteplici asset. Quindi i secondi livelli della linea (campata, sostegno, estremo cavo e tratta cavo) saranno legati alla linea mediante relazione. Dal secondo livello in poi si utilizzerà la relazione gerarchica padre-figlio.

Per gli asset di primo livello in EAM sarà memorizzato ed esposto all’utente l’attributo X3SAPID a livello di ASSET che conterrà il codice utente della sede tecnica (STRNO di sede tecnica).

Per i secondi livelli il codice utente della sede tecnica X3SAPID sarà memorizzato a livello di ASSETLOCRELATION (e non di ASSET) per poter differenziare il codice in base alla linea di riferimento.

Per gli asset di terzo livello (aggregatori) che necessitano di essere ricondotti ad una precisa linea, poiché il secondo livello padre potrebbe essere in doppia terna, verrà valorizzato l’attributo X3ROOTASSETNUM con il numero dell’asset di primo livello (la linea appunto). Ad esempio, l’asset di terzo livello CONDUTTORI è figlio dell’asset di secondo livello CAMPATA che può essere associato (se in doppia terna) a due linee diverse. La figura seguente illustra il caso. Si tenga presente che alcuni asset di terzo livello (funi di guardia e telaio) sono condivisi dalle due linee in doppia terna per cui esisterà un solo asset aggregatore con nessuna valorizzazione dell’attributo X3ROOTASSETNUM.

Figura 5. Esempio pratico -model asset

### Data model stazioni

La struttura del data model per le stazioni si basa in modo analogo alle linee sugli oggetti base del prodotto come mostrato in Figura 3. Modello entità-relazioni per gli asset. A differenza delle linee, non vi è la necessità di modellare asset comuni a più stazioni (come, ad esempio, le campate aeree ed i sostegni per le linee) pertanto la gerarchia verrà esclusivamente rappresentata mediante relazioni gerarchiche di tipo padre-figlio (in sostanza non verranno utilizzate relazioni mediante la tabella ASSETLOCRELATION). Il data model EAM per le stazioni prevede 4 livelli dove i primi tre livelli rappresentano sedi tecniche SAP e il quarto livello rappresenta gli equipment SAP. Si rammenta che il data model SAP per le stazioni è basato su 5 livelli e il quarto livello non verrà rappresentato in EAM. Ovviamente il connettore effettuerà le dovute traslazioni fra i due data model. Nella Figura 5. Mappatura asset EAM e sedi tecniche equipment SAP si riporta una casistica a titolo di esempio.

Figura 6. Mappatura asset EAM e sedi tecniche equipment SAP

A differenza del data model delle linee, per il data model delle stazioni è previsto il popolamento del campo X3SAPID per tutti i livelli. In particolare, per i primi tre livelli EAM (sedi tecniche SAP) il valore impostato corrisponderà al valore del STRNO della corrispondente sede tecnica. Per quanto riguarda il quarto livello EAM (equipment SAP) sarà necessaria una gestione specifica (descritta di seguito) in quanto l’asset EAM si mapperà ad un equipment piuttosto che ad una sede tecnica. La tabella degli equipment SAP (EQUZ) non prevede la doppia codifica degli ID (STRNO e TPLNR) come avviene per le sedi tecniche SAP, bensì ha un unico identificativo (EQUNR). Questo identificativo dovrà essere memorizzato nel campo X3SAPID degli asset EAM dalle procedure di caricamento ETL e verrà utilizzato dal connettore come foreign key verso il record corrispondente nella X3SAPASSET. Per gli asset EAM di quarto livello generati in EAM, sarà EAM stesso a generare un valore (non in conflitto con i precedenti caricati) e memorizzarlo nel campo X3SAPID. Il connettore inoltre gestirà la memorizzazione del EQUNR (sia per gli equipment caricati da ETL sia per quelli generati da EAM e creati di conseguenza su SAP) nel campo SAPUID.

Figura 7. Puntamenti X3SAPASSET per equipment

Si riportano gli step di valorizzazione previsti nello scenario di creazione di un equipment EAM (rappresentato anche graficamente in Figura 6. Flusso di creazione di un equipment EAM di quarto livello).

L’anagrafica aggiunge un asset di quarto livello di tipo equipment (per definizione il data model delle stazioni prevede che i quarti livelli siano necessariamente equipment).

L’anagrafica genera il valore X3SAPID per l’asset creato che identifica univocamente l’equipment all’interno di EAM. Il valore X3SAPID permetterà l’identificazione del corrispondente equipment nella tabella X3SAPASSET.

Il connettore dovrà eseguire una operazione di sincronizzazione di tipo ADD verso SAP che ritornerà l’identificativo univoco (EQUNR) dell’equipment SAP.

Il connettore memorizzerà l’identificativo univoco nel campo SAPUID della tabella X3SAPASSET. In questo modo il connettore potrà indirizzare future richieste verso SAP identificando l’equipment SAP usando il valore EQUNR memorizzato in SAPUID.

Figura 8. Flusso di creazione di un equipment EAM di quarto livello

# Flusso di sincronizzazione anagrafica

In questo paragrafo verrà descritto il flusso di sincronizzazione della anagrafica EAM/SAP. Questo flusso è di tipo out-bound dal punto di vista di IBM MAS Manage, cioè, viene interamente guidato e innescato da EAM in quanto master della anagrafica.

Il connettore gestirà in sequenza i seguenti steps elaborativi:

Step 1: registrazione modifiche anagrafica EAM

Step 2: identificazione operazioni di sincronizzazione

Step 3: predisposizione messaggio di integrazione

Step 4: sincronizzazione (con conseguente gestione di errori)

La figura seguente mostra le strutture dati (di prodotto e custom) su cui ogni step elaborativo insiste.

Figura 9. Utilizzo strutture dati durante il flusso di elaborazione

Nei paragrafi seguenti verranno dettagliati i singoli step e la struttura e finalità dei dati custom. Per facilitarne la comprensione, si riporta una sintesi delle strutture dati nella tabella seguente.

| MBO | Custom | Funzionalità | Descrizione |
| --- | --- | --- | --- |
| ASSET | No | Tabella asset | Tabella principale per la memorizzazione di un asset EAM |
| ASSETLOCRELATION | No | Tabella relazioni asset | Tabella per memorizzare una relazione fra due asset (ad esempio fra una linea e una campata aerea) |
| ASSETSPEC | No | Tabella specifiche tecniche | Tabella per la memorizzazione delle specifiche tecniche degli asset |
| X3MAXIMOSAPMAP | Si | Mapping percorso oggetti anagrafica EAM e tipologia sedi tecniche SAP | Questa tabella definisce le logiche di mappatura degli elementi dell’anagrafica EAM mediante il percorso gerarchico (ad esempio LINEA/CAMPATA AEREA/CONDUTTORI/CONDUTTORE) verso le sedi tecniche ed equipment SAP identificate dalla classe SAP (ad esempio CONDUTTORE). |
| X3OBJECTCHANGE | Si | Registro modifiche anagrafica EAM | Questa tabella registra gli eventi di cambiamento (aggiunta, modifica, cancellazione) avvenuti per gli asset della anagrafica EAM. Gli eventi rappresentano la base su cui periodicamente verranno determinate le necessarie azioni di sincronizzazione da avviare verso SAP |
| X3SAPASSET | Si | Immagine della struttura delle sedi tecniche/equipment SAP e della loro gerarchia | Questa tabella rappresenta lo specchio delle sedi tecniche/equipement SAP in termini di campi chiave (id interno ed esterno) nonché delle relazioni gerarchiche fra gli stessi. La tabella registra le eventuali azioni di sincronizzazione pendenti ed eventuale esito negativo del flusso di sincronizzazione. |
| X3SAPTOMAXOBJECT | Si | Tabella di collegamento fra gli oggetti di anagrafica EAM e anagrafica SAP | Questa tabella registra i collegamenti fra un record dell’anagrafica EAM (asset e tronchi di ispezione) e un record della anagrafica SAP (presenti in X3SAPASSET). Questi collegamenti sono necessari per: capire come costruire la gerarchia delle sedi tecniche ed equipment SAP dalla anagrafica EAM ricondurre le informazioni della anagrafica EAM (attributi e specifiche tecniche) alle sedi tecniche ed equipment SAP |
| X3SAPASSETDATA | Si | Tabella per la raccolta temporanea di attributi e specifiche tecniche per una sede tecnica SAP per la quale è prevista una sincronizzazione. | Questa tabella viene utilizzata per registrare temporaneamente i dati relativi ad una sede tecnica/equipment (attributi e specifiche tecniche). La tabella sarà popolata prima dell’avvio di una richiesta di creazione o aggiornamento verso SAP. Al termine della richiesta i record relativi alla sede tecnica/equipment sincronizzata saranno cancellati. |
| X3SAPASSETSPECRULE | Si | Tabella per la descrizione delle regole da applicare al fine di ottenere il valore di una caratteristica SAP | Questa tabella fornisce per ogni caratteristica SAP di una sede tecnica/equipment, la regola da applicare per ricavare il valore della caratteristica dalla anagrafica EAM. |

## Mappatura elementi EAM/SAP

Gli elementi dell’anagrafica EAM saranno soggetti a classificazione. Il percorso di classificazione di ciascun elemento non coincide necessariamente con il percorso dell’asset all’interno della gerarchia. Questo accade sicuramente per le stazioni dove alcune tipologie di asset identificate da una specifica classificazione possono essere innestati in vari punti della gerarchia. Pertanto, è importante assumere che il livello dell’asset non sarà desumibile dal percorso della classificazione. Al fine di poter istruire il connettore sulle possibili mappature fra oggetti EAM e SAP per asset di linee e stazioni, verrà configurato l’MBO X3MAXIMOSAPMAP definito come segue.

| Attributo | Tipo | Descrizione | Valori |
| --- | --- | --- | --- |
| MAXOBJECT | MAXOBJECT.OBJECTNAME | Oggetto EAM | ASSET, X3TRONCHI |
| HIERARCHYPATH | UPPER 250 | Percorso gerarchico asset |  |
| MAXLEVEL | INTEGER | Livello asset EAM | 1, 2, 3, 4 |
| SAPCLASS | UPPER 50 | Classe SAP |  |
| SAPLEVEL | INTEGER | Livello SAP | Linee: 1, 2, 3 Stazioni: 1, 2, 3, 4, 5 |
| ROOTCLASS | UPPER 192 | Indica se il primo livello è una linea o stazione | LINEA, STAZIONE |
| STEP1 | YORN | Se 1 indica che la riga di mapping deve essere elaborata nella prima fase dello step 1, A parità di HIERARCHYPATH dovrà esserci una sola riga con il flag a 1. Eventuali altre righe avranno il flag a 0 |  |
| STEP1STRNO | ALN 200 | Regola per la generazione del STRNO durante lo step1. Dovrà essere valorizzato solo per i record con STEP uguale a 1 |  |
| STEP1REGEX | ALN 200 | Espressione regolare per la validazione del STRNO prodotto durante lo step 1 |  |
| STEP2STRNO | ALN 200 | Regola per la generazione del STRNO durante lo step 2. Dovrà essere valorizzato per tutti i record |  |
| STEP2REGEX | ALN 200 | Espressione regolare per la validazione del STRNO prodotto durante lo step 2 |  |
| STEP2FILTER | ALN 200 | Espressione regolare che verrà valutata rispetto a STRNO calcolato dello step 1. Se falsa allora il record sarà scartato. Se vera o se nessuna espressione regolare è impostata il record verrà considerato |  |
| STEP2TARGET | UPPER 50 | Indica se la regola di mapping si applicherà durante lo step 2 ai soli binding (BINDING), ai soli asset SAP (ASSET) o a entrambi (ASSET_AND_BINDING) | BINDING, ASSET, ASSET_AND_BINDING |

Tabella 1. MBO X3MAXIMOSAPMAP

Questo mapping sarà funzionale in due casi:

Identificazione di uno o più elementi SAP verso cui l’elemento EAM sarà mappato

Identificazione di uno o più elementi SAP verso cui le specifiche tecniche dell’elemento EAM saranno mappate

L’attributo HIERARCHYPATH rappresenta il percorso gerarchico dell’asset che può differire dal percorso della classificazione. Questo percorso avrà una profondità pari al livello definito in MAXLEVEL. Ogni elemento del percorso corrisponde al CLASSIFICATIONID del corrispondente elemento della gerarchia. I vari livelli saranno separati dal carattere back-word slash (\). Quindi ad esempio un asset conduttore di quarto livello avrà il seguente percorso.

LINEA/CAMPATA/CONDUTTORI/CONDUTTORE

Si riporta a titolo di esempio il mapping degli elementi LINEA, CAMPATA AEREA, CONDUTTORI e CONDUTTORE.

MAXOBJECT HIERARCHYPATH MAXLEVEL SAPCLASS SAPLEVEL -------------------------------------------------------------------------------------------------------------------------------------------------

ASSET LINEA 1 LINEA 1

ASSET LINEA\CAMPATA AEREA 2 CAMPATA 2

ASSET LINEA\CAMPATA AEREA\CONDUTTORI 3 CAMPATA 2

ASSET LINEA\CAMPATA AEREA\CONDUTTORI 3 CONDUTTORI 3

ASSET LINEA\CAMPATA AEREA\CONDUTTORI\CONDUTTORE 4 CONDUTTORI 3

L’aggregato CONDUTTORI è un terzo livello sull’anagrafica EAM e corrisponde all’elemento di terzo livello sull’anagrafica SAP. Inoltre, le specifiche tecniche dell’aggregato CONDUTTORI EAM devono essere mappate sia sulla CAMPATA che sull’aggregato CONDUTTORI SAP, pertanto, sono presenti due righe di mapping (riga 3 e 4).

L’attributo STEP1 indica se la regola di mapping è applicata durante lo step 1 (STEP1 uguale ad 1) o successivamente nello step 2 (STEP1 uguale a 0). Per ogni HIEARCHYPATH è prevista la presenza di almeno una regola di mapping con l’attributo impostato ad 1. Questa regola sarà utilizzata (a parità di HIERARCHYPATH) per determinare la logica di valorizzazione della X3OBJECTCHANGE. In generale è presente per ogni HIERARCHYPATH una sola regola con l’attributo STEP1 valorizzato ad 1.

Durante lo step 1 la regola di mapping fornisce il criterio (attributo STEP1STRNO) per la registrazione nella X3OBJECTCHANGE dell’id SAP di riferimento. Ad esempio, per un conduttore EAM (che di fatto corrisponde alla sede tecnica conduttore SAP) viene registrato l’id SAP della campata EAM a cui afferisce. Questo id SAP costituirà la base per generare l’effettivo id SAP durante lo step 2. Si ricordi che EAM non necessariamente memorizza tutti gli id SAP (ad esempio per le linee sono per i primi e secondi livelli) e che inoltre non tutte le sedi tecniche SAP sono rappresentate in EAM (ad esempio la morsetteria SAP). Pertanto, la strategia è quella di memorizzare l’id SAP più prossimo a cui afferisce l’asset EAM per poi generare durante lo step 2 l’effettivo id SAP mediante una ulteriore regola (espressa nell’attributo STEP2STRNO) a partire dall’id SAP registrato nella X3OBJECTCHANGE.

Figura 10. Popolamento id SAP

Il processo di valorizzazione è validato mediante espressioni regolari (STEP1REGEX e STEP2REGEX) che assicurano la bontà della codifica dell’id SAP.

La regola di mapping fornisce indicazione (mediante l’attributo STEP2TARGET) sul tipo di gestione da effettuare durante lo step 2. Le possibili gestioni sono:

ASSET: la modifica registrata in X3OBJECTCHANGE si riflette con la sola creazione / cancellazione della sede tecnica corrispondente in X3SAPASSET

BINDING: la modifica registrata in X3OBJECTCHANGE si riflette con l’aggiornamento dei collegamenti asset / sede tecnica nella tabella X3SAPTOMAXOBJECT. Questi collegamenti sono funzionali alla produzione delle caratteristiche durante lo step 3

ASSET_AND_BINDING: la modifica registrata in X3OBJECTCHANGE si riflette con l’aggiornamento della sede tecnica in X3SAPASSET e dei collegamenti asset / sede tecnica nella tabella X3SAPTOMAXOBJECT.

Infine, l’ultimo attributo da menzionare è STEP2FILTER il quale, se definito, fornisce una espressione regolare da applicare al valore dell’id SAP registrato nella X3OBJECTCHANGE. Se l’espressione regolare è vera allora la regola di mapping verrà presa in considerazione, se falsa sarà ignorata. Questo tipo di configurazione si applica per l’estremo cavo EAM che possono mapparsi nell’anagrafica SAP a due tipologie di sedi tecniche (estremo cavo e camera giunti).

Per quanto riguarda la semantica degli altri attributi si rimanda ai paragrafi Registrazione modifiche anagrafica EAM e Identificazione operazioni di sincronizzazione.

Per completezza si fornisce un esempio anche per le stazioni.

MAXOBJECT HIERARCHYPATH MAXLEVEL SAPCLASS SAPLEVEL ---------------------------------------------------------------------------------------------------------------------------------------------------------------

ASSET STAZIONE 1 STAZIONE 1

ASSET STAZIONE\SEZIONE 2 SEZIONE 2

ASSET STAZIONE\SEZIONE\STALLO 3 STALLO 3

ASSET STAZIONE\SEZIONE\STALLO 3 APPARECCHIATURE 4

ASSET STAZIONE\SEZIONE\STALLO 3 SPCC 4

ASSET STAZIONE\SEZIONE\STALLO 3 COMPLESSI DI MISURA 4

ASSET STAZIONE\SEZIONE\STALLO 3 OPERE CIVILI 4

ASSET STAZIONE\SEZIONE\STALLO\BOBINA DI SBARRAMENTO OCV 4 BOBINA ONDE CONVOGLIATE 5

## Struttura anagrafica SAP su EAM

Per la realizzazione delle attività di sincronizzazione verso SAP sarà necessario mantenere traccia in EAM dell’ultima versione della struttura della anagrafica SAP sincronizzata al fine di poter determinare le azioni necessarie per allineare il delta di modifiche avvenute in EAM rispetto all’ultima sincronizzazione. Questa struttura rifletterà l’intera anagrafica SAP e sarà circoscritta alle sole relazioni gerarchiche. I dati effettivi, quali attributi degli oggetti e specifiche tecniche, saranno attinti dagli oggetti EAM all’atto della sincronizzazione. Per la rappresentazione in EAM della anagrafica SAP sarà creato il seguente MBO custom X3SAPASSET.

| Attributo | Tipo | Descrizione | Valori | Esempio |
| --- | --- | --- | --- | --- |
| X3SAPASSETID | LONG | Id record | Auto generati da sequenza | 1 |
| STRNO | UPPER 50 | Id elemento (STRNO) | Ricavato da anagrafica per i primi e secondi livelli, prodotti secondo algoritmo di codifica per i livelli successivi | 22F22A1-001----- |
| SAPCLASS | X3MAXIMOSAPMAP. SAPCLASS | Classe SAP |  | CAMPATA |
| SAPLEVEL | INTEGER | Livello SAP | Linee: 1, 2, 3 Stazioni: 1, 2, 3, 4, 5 | 2 |
| PARENTSTRNO | UPPER 50 | Id (STRNO) oggetto padre (NULL per elemento di primo livello) |  | 22F22A1 |
| SAPUID | ALN 30 | Id interno elemento SAP linee: TPLNR sede tecniche stazioni: TPLNR equipment stazioni: EQUNR |  | ?0100000000000124318 |
| SAPPARENTUID | ALN 30 | Id interno elemento SAP padre linee: TPLMA sede tecniche stazioni: TPLMA equipment stazioni: TPLNR |  | ?0100000000000124317 |
| SYNCHACTION | UPPER 50 | Azione di sincronizzazione | ADD, UPDATE, DELETE, RENAME, MOVE, REBIND | UPDATE |
| ERRORFLAG | YORN | 1 se il record è stato elaborato con errore da step 3 e 4, 0 altrimenti | 0, 1 | 0 |
| ERROR | CLOB | Vuoto se il record non è stato elaborato Vuoto se il record è stato elaborato con successo (SYNCHACTION non valorizzata) compilato se il record è stato elaborato con errore (SYNCHACTION valorizzata). |  |  |
| MAPPINGERROR | YORN | 1 se si sono verificati errori durante lo step 3 | 0, 1 | 0 |
| WARNING | CLOB | Warning raccolti durante lo step 3 |  |  |
| ROOTSTRNO | UPPER 50 | Id (STRNO) oggetto elemento di primo livello |  | 22F22A1 |
| ROOTASSETTYPE | UPPER 1 | Identificativo tipo asset principale (linea o stazione) | L: linea Z: stazione | L |
| SAPOBJECT | UPPER 1 | Identificativo tipo oggetto SAP (sede tecnica o equipment) | S: sede tecnica E: equipment | S |
| HISTORYFLAG | YORN | 1 se l’ultima azione di sincronizzazione eseguita con successo è stata una DELETE | 0, 1 | 0 |
| DUMMY | YORN | Identifica una sede tecnica fittizia | 0, 1 | 1 |

Tabella 2. MBO X3SAPASSET

L’id X3SAPASSETID rappresenterà la chiave primaria del MBO X3SAPASSET. Come si evince dai campi di questo MBO si memorizzerà la struttura gerarchica dei record SAP utilizzando il SAP id dell’elemento (STRNO), del padre (PARENTSTRNO) e dell’elemento di primo livello (ROOTSTRNO). Inoltre, ad ogni sincronizzazione, la gerarchia sarà tracciata anche mediante l’id interno dell’elemento (SAPUID) e l’id interno del padre (SAPPARENTUID). Per quanto riguarda gli id SAP interni la sorgente SAP è chiarita nella tabella seguente.

| Anagrafica tecnica | Oggetto SAP | SAPUID | SAPPARENTUID |
| --- | --- | --- | --- |
| Linea | Sede tecnica | TPLNR | TPLMA |
| Stazione | Sede tecnica | TPLNR | TPLMA |
| Stazione | Equipment | EQUNR | TPLNR |

Tabella 3. Corrispondenza SAPUI e SAPPARENTUID con attributi SAP

Si noti che il MBO X3SAPASSET non ha riferimenti verso la anagrafica tecnica rappresentata in IBM MAS Manage. Il collegamento della anagrafica SAP verso l’anagrafica EAM verrà realizzata mediante il MBO custom X3SAPTOMAXOBJECT che avrà la seguente struttura.

| Attributo | Tipo | Descrizione | Valori | Esempio |
| --- | --- | --- | --- | --- |
| SAPASSETID | X3SAPASSET.X3SAPASSETID | Id record X3SAPASSET |  | 5 |
| MAXOBJECT | MAXOBJECT.OBJECTNAME | Nome MBO (ASSET, X3TRONCHI) |  | ASSET |
| MAXUID | LONG | Id MBO (ASSET, X3TRONCHI) |  | 19 |
| SITEID | SITE.SITEID | Sito MBO |  | TERNA |
| OBJECTNUM | UPPER di dimensione tale da contenere gli identificativi di tutti gli oggetti da sincronizzare | Numero MBO (ASSET.ASSETNUM, X3TRONCHI.TRONCONUM) |  | 1005 |
| HIERARCHYPATH | UPPER 250 | Percorso gerarchico |  | LINEA/CAMPATA AREA |

Tabella 4. MBO X3SAPTOMAXOBJECT

Il MBO X3SAPTOMAXOBJECT collega un asset EAM con un asset SAP. Il collegamento potrebbe essere realizzato esclusivamente mediante i primi due campi (SAPASSETID e MAXUID), ma per motivi di leggibilità verranno riportati anche i campi della chiave primaria del MBO (SITEID e OBJECTNUM).

Si presuppone che a seguito della procedura di ETL i dati necessari al connettore saranno correttamente popolati.

In dettaglio, i MBO X3SAPASSET e X3SAPTOMAXOBJECT dovranno contenere la struttura gerarchica SAP e i mapping con i MBO ASSET.

Inoltre, per quanto riguarda le linee, l’attributo X3SAPID presente sugli asset di primo livello nel MBO ASSET e sulle relazioni primo-secondo livello per le linee nel MBO ASSETLOCRELATION dovrà contenere l’identificativo della sede tecnica SAP corrispondente (STRNO). Per quanto riguarda le stazioni il campo X3SAPID dovrà essere popolato per tutti gli asset. In particolare per i primi tre livelli coinciderà con il valore STRNO della sede tecnica corrispondente, per il quarto livello coinciderà con il valore EQUNR dell’equipment corrispondente.

Infine, l’attributo X3ROOTASSETNUM presente sugli asset di terzo livello nel MBO ASSET dovrà contenere il numero di asset della linea laddove l’asset di terzo livello è collocato su una specifica linea (ad esempio l’asset di tipo CONDUTTORI).

## Step 1: registrazione modifiche anagrafica EAM

Lo step 1 consiste nell’intercettamento delle modifiche apportate alla anagrafica EAM sia per linee che per le stazioni e alla susseguente registrazione di tali modifiche in un registro dedicato (X3OBJECTCHANGE). Questo registro rappresenterà la base di elaborazione per gli step successivi. L’aggiornamento di questo registro avverrà contestualmente al salvataggio degli oggetti modificati. L’intercettazione delle modifiche potrà avvenire mediante due possibili meccanismi rispetto alla operatività della anagrafica:

Implicito: questo meccanismo prevede che le applicazioni della anagrafica effettuino le operazioni di ADD, UPDATE e DELETE sui MBO della anagrafica senza notificarle al connettore. Il connettore riceverà notifiche relative a queste operazioni mediante un meccanismo publish/subscriber offerto dalla piattaforma.

Esplicito: questo meccanismo prevede che le applicazioni della anagrafica effettuino delle chiamate esplicite ad API del connettore per notificare specifiche operazioni di modifica difficilmente o impossibili da dedurre dalle operazioni di ADD, UPDATE e DELETE sui MBO. Le notifiche ricevute mediate il meccanismo publish/subscriber verranno ignorate.

Lo step 1 si articola in due fasi. La prima fase viene eseguita in sincrono con il salvataggio eseguito dalla applicazione della anagrafica. Durante questa fase lo step 1 disporrà di tutti gli MBO modificati e potrà raccogliere un primo insieme di informazioni dai MBO modificati senza dover accedere alla base dati rendendo la fase estremamente efficiente.

### Configurazione step 1

La seconda fase dello step 1 utilizza le configurazioni riportate nella tabella X3MAXIMOSAPMAP (attributi STEP1, STEP1STRNO e STEP1REGEX). In prima battuta viene determinato il percorso gerarchico dell’oggetto (HIERARCHYPATH) mediante il quale sarà possibile ricavare il record della X3MAXIMOSAPMAP da utilizzare (filtrando per HIERARCHYPATH calcolato e per STEP1 uguale a 1).

A questo punto verranno utilizzate le seguenti configurazioni:

STEP1STRNO: stringa di formattazione di STRNO.

STEP1REGEX: espressione regolare per la validazione del STRNO prodotto

Successivamente lo step 2 elaborerà il STRNO definitivo (si rimanda al paragrafo Applicazione mapping).

### Scenari di esecuzione

Le registrazioni delle modifiche durante lo step 1 possono essere generate mediante

meccanismo implicito: elaborazione di eventi standard di MBO (ADD, UPDATE e DELETE)

meccanismo esplicito: mediante API esposta dal connettore.

Il primo caso si applica ad operazioni canoniche generate dalle applicazioni della anagrafica. Operazioni più complesse (quali entra/esci, vedi paragrafo Scenario 5: entra/esci) sono realizzate mediante script di automazione che utilizzeranno API esposte dal connettore per notificare la natura delle modifiche.

Gli scenari di aggiornamento identificati sono i seguenti:

0: cambiamenti mediante applicazione

1: disabilita step 1

2: caricamento anagrafica via ETL

3: scenario di ammazzettamento

4: ricodifica secondo livello linea

5: entra/esci

6: inversione linea

#### Scenario 0: cambiamenti mediante applicazione

Questo scenario è basato sul meccanismo implicito, pertanto, il connettore sarà in ascolto degli eventi di modifica sugli oggetti dell’anagrafica EAM riportati nella tabella seguente.

| Oggetto | Descrizione | MBO standard |
| --- | --- | --- |
| ASSET | MBO per la memorizzazione degli asset di livello 1, 2, 3, 4 | Si |
| ASSETLOCRELATION | MBO per la memorizzazione delle relazioni dei secondi livelli linee (sostegni e campata) verso le linee (primo livello) | Si |
| ASSETSPEC | MBO per la memorizzazione delle specifiche tecniche degli asset in base alla classificazione applicata all’asset | Si |
| X3TRONCHI | MBO per la memorizzazione dei tronchi di ispezione | No |

Tabella 5. MBO anagrafica EAM

Si tenga presente che le modifiche effettuate agli MBO ASSET, ASSETLOCRELATION e ASSETSPEC saranno registrate come modifiche dell’ASSET corrispondente.

All’atto del salvataggio di ogni singola tipologia di oggetto saranno determinati i seguenti valori relativi al registro modifiche X3OBJECTCHANGE.

ROOTSITEID: sito dell’oggetto anagrafica memorizzato nell’attributo SITEID

ROOTASSETNUM: numero asset della linea/stazione. Per quando riguarda le linee il valore sarà ottenuto secondo lo schema riportato di seguito

| Oggetto | Livello EAM | Sorgente |
| --- | --- | --- |
| ASSET | 1 | ASSETNUM |
| ASSET | 2 | SOURCEASSETNUM della relazione (ASSETLOCRELATION) fra primo e secondo. Poiché l’asset di secondo livello può avere più di una linea verranno creati tanti record di modifiche (uno per linea) quante sono le relazioni trovate verso l’asset sorgente (linea). |
| ASSETLOCRELATION | 2 | SOURCEASSETNUM |
| ASSET | 3 | X3ROOTASSETNUM |
| ASSET | 4 | PARENT. X3ROOTASSETNUM |
| ASSETSPEC |  | Dall’oggetto ASSETSPEC si recupera l’asset padre (che potrà essere di livello 1, 2, 3, 4) e si adotteranno le regole indicate nelle righe precedenti. |
| X3TRONCHI | 2 | ASSETNUM |

Tabella 6. Regole per recupero asset principale per le linee

Per quanto riguarda le stazioni sarà possibile determinare il valore risalendo la gerarchia padre/figlio sino all’elemento di primo livello.

OWNERTABLE: nome dell’oggetto anagrafica (determinato applicativamente). Per gli MBO ASSET, ASSETLOCRELATION e ASSETSPEC verrà memorizzato l’oggetto di anagrafica ASSET corrispondente. Per il MBO X3TRONCHI sarà X3TRONCHI.

OWNERNUM: numero dell’oggetto anagrafica (determinato applicativamente). Per gli MBO ASSET, ASSETLOCRELATION e ASSETSPEC verrà memorizzato l’ASSETNUM dell’oggetto di anagrafica ASSET corrispondente. Per il MBO X3TRONCHI sarà TRONCONUM. Lo schema seguente riassume le regole su come ricavare il numero dell’oggetto.

| Oggetto | Livello EAM | Sorgente |
| --- | --- | --- |
| ASSET | 1 | ASSETNUM |
| ASSET | 2 | ASSETNUM |
| ASSETLOCRELATION | 2 | TARGETASSETNUM |
| ASSET | 3 | ASSETNUM |
| ASSET | 4 | ASSETNUM |
| ASSETSPEC |  | ASSETNUM |
| X3TRONCHI | 2 | TRONCONUM |

Tabella 7. Regole per recupero numero asset oggetto della modifica da MBO anagrafica EAM

MAXLEVEL / SAPLEVEL: i livelli della gerarchia EAM e SAP saranno recuperati dal mapping indicato in X3MAXIMOSAPMAP filtrando per STEP1 uguale a 1.

OPERATION: l’operazione sarà ADDCHANGE se l’oggetto di anagrafica EAM è stato creato o aggiornato. Nel caso vi sia il cambiamento di stato dell’asset in DISMISSED l’operazione sarà DELETE. Nel caso di cancellazione di una ASSETLOCRELATION l’operazione sarà DELETE. Inoltre, è prevista l’azione RENAME in caso di rinomina di un id SAP e MOVE in caso di spostamento.

NEWSTRNO: valorizzato con il nuovo id SAP assegnato dalla anagrafica. Verrà valorizzato solo in caso di operazione RENAME e MOVE.

#### Scenario 1: disabilitazione step 1

Questo scenario è basato sul meccanismo implicito. Le notifiche di aggiornamento sugli oggetti della anagrafica EAM saranno ignorate e non verranno pertanto registrate le modifiche nel registro modifiche. Di fatto consiste nel disabilitare gli effetti dello step 1.

#### Scenario 2: caricamento anagrafica via ETL

Questo scenario è basato sul meccanismo implicito. Le modifiche verranno registrate allo stesso modo dello scenario 0 con l’unica differenza che lo step 2 eseguirà l’aggiornamento della struttura degli asset SAP ma non produrrà alcuna azione di sincronizzazione. Questo scenario è usato dalle procedure di ETL al fine di popolare la tabella X3MAXIMOSAPMAP.

#### Scenario 3: ammazzettamento

L’ammazzettamento è una operazione fornita dalla gestione della anagrafica nell’ambito linee che si applica ai conduttori di una campata in doppia in terna (supponiamo di identificare le due linee in doppia terna come LINEA A e LINEA B). L’operazione prevede la dismissione di una campata su una linea (ad esempio la LINEA A) previo spostamento dei suoi conduttori sull’altra linea (LINEA B).

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API da richiamare esplicitamente per comunicare le modifiche.

#### Scenario 4: ricodifica secondo livello linea

La ricodifica di un secondo livello EAM si applica solo per gli elementi di una linea ed in particolare alla sola CAMPATA AEREA e TRATTA CAVO (secondi livelli anagrafica SAP). Di fatto, in fase di rinomina l’applicazione della anagrafica allineerà la codifica dei rispettivi secondi livelli (SOSTEGNO AEREO ed ESTREMO CAVO/CAMERA GIUNTI rispettivamente). Il connettore avrà in carico la gestione della ricodifica dei corrispondenti elementi SAP (CAMPATE e TRATTA CAVO) e dei rispettivi figli.

Si presuppone che il nuovo SAP id non sia già in uso.

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche.

#### Scenario 5: entra/esci

L’azione entra/esci consiste nello spostamento di asset CAMPATA AEREA e TRATTA CAVO di una linea (secondi livelli anagrafica SAP) unitamente a secondi livelli associati (SOSTEGNO AEREO ed ESTREMO CAVO/CAMERA GIUNTI rispettivamente) verso un’altra linea. Il connettore avrà in carico la gestione dell’aggiornamento dei corrispondenti elementi SAP (CAMPATE e TRATTA CAVO) e dei rispettivi figli.

Si presuppone che i SAP id associati agli asset spostati non siano già in uso.

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche.

#### Scenario 6: inversione linea

L’azione inversione linea consiste nel riordinamento dei secondi livelli EAM in ordine inverso con conseguente rinomina di campate/tratte cavo per conciliare la convenzione che l’X3SAPID dei seguenti sostegni/estremi cavo deve corrispondere. Questo procedimento preserva gli identificativi di sostegni, camera giunti ed estremi cavo (ad eccezione del suffisso) e non richiede quindi la riproduzione delle targhe di segnalazione.

Si consideri ad esempio la linea mista (tratta aerea e tratta cavo) riportata di seguito (l’ordinamento è da sinistra a destra).

L’operazione di inversione della applicazione di Anagrafica riordinerà i secondi livelli in ordine inverso (l’ordinamento è sempre da sinistra a destra).

Infine, applicherà la rinomina dei secondi livelli campata e tratta cavo e dei suffissi degli estremi cavo:

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche.

#### Scenario 7: aggiunta secondo livello linee

L’aggiunta di secondi livelli per le linee viene tipicamente gestita mediante il meccanismo implicito. Si fornisce comunque anche il meccanismo esplicito a favore della anagrafica nel caso in cui avesse necessità di operare sulla creazione dei secondi livelli senza stimolare il connettore, per poi notificare le creazioni in un secondo momento.

#### Scenario 8: movimentazione equipment stazione

Sarà possibile movimentare gli elementi di quarto livello di una stazione (noti come equipment) all’interno della stessa stazione o verso un’altra stazione. Un elemento di quarto livello potrà essere spostato verso un terzo livello appartenente alla stazione di partenza oppure ad un terzo livello appartenente ad una differente stazione. Inoltre, un elemento di quarto livello può essere spostato sotto la stazione o sotto ad una differente stazione. Data la natura gerarchica (padre/figlio) degli elementi di una stazione la movimentazione consisterà (dal punto di vista della anagrafica) nel cambio dell’asset padre e relativo aggiornamento degli ID SAP.

Figura 11. Possibili spostamenti equipment stazione

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche.

#### Scenario 9: ricodifica terzo livello stazione

Sarà possibile ricodificare i terzi livelli di una stazione. La ricodifica del X3SAPID in questo caso riguarda la sola parte a scelta dell’utente quindi i soli ultimi due caratteri del terzo segmento (in quanto i primi due sono associati alla classificazione dell’asset). Ad esempio, dato un trasformatore con codifica 32525C-77-1200, è possibile ricodificarlo in 32525C-77-1210.

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche.

#### Scenario 10: movimentazione secondo/terzo livello stazione

Sarà possibile movimentare gli elementi di una stazione di secondo/terzo livello verso un valido primo/secondo livello di un’altra stazione. Data la natura gerarchica (padre/figlio) degli elementi di una stazione la movimentazione consisterà (dal punto di vista della anagrafica) nel cambio dell’asset padre e relativo aggiornamento degli ID SAP.

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche.

### Registro modifiche

Il registro delle modifiche sarà rappresentato mediante il MBO X3OBJECTCHANGE definito all’interno di IBM MAS Manage. Si riporta di seguito il data-model di tale entità.

| Attributo | Tipo | Descrizione | Valori |
| --- | --- | --- | --- |
| TXID | BIGINT | Id transazione MAS Manage che ha generato il cambiamento. Permette di identificare tutti i cambiamenti verificatesi in una operazione di salvataggio della anagrafica. |  |
| ROOTSITEID | SITE.SITEID | Sito linea/stazione |  |
| ROOTASSETNUM | ASSET.ASSETNUM | Numero asset linea/stazione |  |
| OWNERTABLE | MAXOBJECT.OBJECTNAME | Nome MBO Maximo oggetto del cambiamento. ASSETLOCRELATION e ASSETSPEC saranno ricondotti all’ASSET corrispondente. | ASSET, X3TRONCHI |
| OWNERNUM | UPPER di dimensione tale da contenere gli identificativi di tutti gli oggetti da sincronizzare | Numero oggetto da sincronizzare (ad esempio ASSET.ASSETNUM) |  |
| HIERARCHYPATH | UPPER 250 | Percorso gerarchico asset |  |
| SCENARIO | INTEGER | Identifica lo scenario di anagrafica che ha alimentato i cambiamenti |  |
| OPERATION | UPPER 20 | Operazione di sincronizzazione | ADDCHANGE, DELETE, RENAME, MOVE |
| RENAME | YORN | 1 se l’oggetto è soggetto a ricodifica |  |
| SAPLEVEL | INTEGER | Livello elemento nel modello FDD | Linee: 1, 2, 3 Stazioni: 1, 2, 3, 4, 5 |
| MAXLEVEL | INTEGER | Livello elemento nel modello to-be | 1, 2, 3, 4 |
| STRNO | UPPER 50 | STRNO calcolato, utilizzato successivamente dallo step 2 per produrre il STRNO corrispondente alla sede tecnica o equipment |  |
| NEWSTRNO | UPPER 50 | Nuovo STRNO in caso di operazione RENAME |  |
| CREATEDATE | DATETIME | Orario di creazione della transazione |  |
| PROCESSDATE | DATETIME | Orario di processamento del cambiamento. Non impostato se il cambiamento non è stato ancora elaborato dallo step successivo |  |
| ERROFLAG | YORN | Se 1 indica che il record è stato elaborato con successo, se 0 il record è stato elaborato con errore. |  |
| ERROR | CLOB | Causa dell’errore. |  |
| SEQUENCENUM | INTEGER | Ordine con cui devono essere elaborati i record appartenenti alla stessa transazione. Il campo è opzionale e valorizzato solo per specifici scenari (ad esempio per l’inversione della linea). |  |

Tabella 8. MBO X3OBJECTCHANGE

## Step 2: identificazione operazioni di sincronizzazione

L’obiettivo di questo step è l’analisi del registro delle modifiche per ogni linea/stazione al fine di preparare le operazioni di sincronizzazione necessarie. Questo processo sarà asincrono rispetto alle modifiche effettive della linea/stazione e sarà avviato secondo una programmazione ad hoc mediante la configurazione di un CRON-task IBM MAS Manage.

Il flusso di elaborazione è descritto dalla figura seguente.

Figura . Flusso di sincronizzazione

### Recupero oggetti modificati

Saranno valutati tutti i cambiamenti non processati (PROCESSDATE non valorizzata) registrati nella X3OBJECTCHANGE per ogni singola transazione (più cambiamenti appartenenti alla stessa transazione hanno la stessa data di creazione CREATEDATE) partendo dai livelli SAP più bassi (1, 2, 3, …). Pertanto, verranno selezionati i cambiamenti non processati (PROCESSDATE non valorizzata).

select * from x3maximosapmap

where processdate is null

order by createdate, saplevel

### Applicazione mapping

Durante l’elaborazione di una modifica (sia per elementi di linea che di stazioni) registrata nella X3OBJECTCHANGE vengono elaborati i mapping corrispondenti nella tabella X3MAXIMOSAPMAP. I mapping da elaborare verranno ricavati usando lo HIERARCHYPATH presente nella X3OBJECTCHANGE.

select * from x3maximosapmap

where hierarchypath = ‘{1}’

dove {1} è l’attributo HIERARCHYPATH del MBO X3OBJECTCHANGE.

La prima osservazione importante è che alcuni mapping (a parità di percorso gerarchico) potrebbero essere validi in funzione del SAP id recuperato durante lo step 1 e memorizzato nell’attributo STRNO della X3OBJECTCHANGE. È questo il caso dei mapping relativi all’estremo cavo che nella anagrafica EAM può essere un estremo cavo di inizio (-H) della tratta cavo o un estremo cavo intermedio (-R) della tratta cavo. Come riportato nell’estratto che segue lo stesso percorso gerarchico può corrispondere a due elementi SAP di natura diversa (TERMINALE o CAMERA GIUNTI)

MAXOBJECT HIERARCHYPATH MAXLEVEL SAPCLASS SAPLEVEL STEP2FILTER -------------------------------------------------------------------------------------------------------------------------------------------------

ASSET LINEA\ESTREMO CAVO 2 TERMINALE 3 ^2[0-5][0-9\|A-Z\|-]{3}[A-Z][0-9]-[0-9]{3}[0-9\|A-Z\|-]{5}-R$

ASSET LINEA\ESTREMO CAVO 2 CAMERA GIUNTI 3 ^2[0-5][0-9\|A-Z\|-]{3}[A-Z][0-9]-[0-9]{3}[0-9\|A-Z\|-]{5}-[H]$

Per poter determinare quale è il mapping da applicare viene usata una espressione regolare definita nell’attributo STEP2FILTER che verrà applicata al valore dell’attributo STRNO della X3OBJECTCHANGE. Se la valutazione è vera allora la regola di mapping verrà presa in considerazione, se falsa la regola di mapping verrà scartata. Se l’attributo STEP2FILTER non è definito allora la regola di mapping verrà sempre presa in considerazione.

I mapping applicabili verranno processati per il singolo record della X3OBJECTCHANGE. Per ogni mapping verrà identificato un record nella X3SAPASSET producendo un STRNO secondo quanto indicato nell’attributo STEP2STRNO della X3MAXIMOSAPMAP. Questo attributo, obbligatorio per tutti i record, indica la logica per produrre il STRNO finale per ricercare l’asset nella X3SAPASSET. IL STRNO prodotto sarà validato con l’espressione regolare definita nell’attributo STEP2REGEX della X3MAXIMOSAPMAP.

Riassumendo:

STEP2STRNO: stringa di formattazione per STRNO finale partendo da STRNO riportato in X3OBJECTCHANGE. Può essere parametrizzata rispetto alle variabili

{strno}: valore dell’attributo STRNO presente nel record X3OBJECTCHANGE

{strno-2}: secondo livello dell’attributo STRNO presente nel record X3OBJECTCHANGE

STEP2REGEX: espressione regolare per la validazione del STRNO prodotto

A titolo di esempio si riporta la configurazione per il percorso gerarchico LINEA\CAMPATA AEREA\CONDUTTORI per un asset SAP di tipo CONDUTTORE

STEP2STRNO: {strno}-C

STEP2REGEX: ^2[0-5][0-9\|A-Z\|-]{3}[A-Z][0-9]-[0-9]{3}[0-9\|A-Z\|-]{5}-C$

L’ultimo punto chiave in merito alla configurazione del mapping è il tipo di aggiornamento da effettuare alle tabelle della anagrafica SAP su EAM (X3SAPASSET e X3SAPTOMAXOBJECT). Questa informazione è riportata nell’attributo STEP2TARGET della X3MAXIMOSAPMAP e può assumere i seguenti valori:

BINDING: per l’oggetto SAP occorre creare/cancellare solo i collegamenti alla anagrafica EAM nella X3SAPTOMAXOBJECT. Se il record nella X3SAPASSET non è presente viene creato (ma non viene cancellato).

ASSET: per l’oggetto SAP occorre creare/cancellare solo l’asset SAP nella X3SAPASSET. I collegamenti non devono essere creati/cancellati.

ASSET_AND_BINDING: per l’oggetto SAP occorre creare/cancellare sia l’asset SAP nella X3SAPASSET che i collegamenti nella X3SAPTOMAXOBJECT.

### Gestione sedi fittizie linee

Il data model EAM e data model SAP differiscono, fra le altre cose, negli elementi di secondo livello come riportato nella tabella seguente.

| Elemento EAM | Livello EAM | Elemento SAP | Livello SAP |
| --- | --- | --- | --- |
| LINEA\CAMPATA AEREA | 2 | LINEA\CAMPATA | 2 |
| LINEA\SOSTEGNO AEREO | 2 | LINEA\CAMPATA\SOSTEGNO | 3 |
| LINEA\TRATTA CAVO | 2 | LINEA\TRATTA CAVO | 2 |
| LINEA\ESTREMO CAVO | 2 | LINEA\TRATTA CAVO\TERMINALE | 3 |
| LINEA\ESTREMO CAVO | 2 | LINEA\TRATTA CAVO\CAMERA GIUNTI | 3 |

Tabella 9 - Corrispondenza secondi livelli EAM / SAP

Come si evince il sostegno aereo e l’estremo cavo sono elementi di secondo livello nel data model EAM mentre i corrispondenti elementi nel data model SAP sono terzi livelli rispettivamente figli di campata e tratta cavo. Pertanto, in SAP un sostegno di inizio di una tratta aerea deve avere una campata padre che non corrisponde ad una campata reale (denominata campata fittizia) ma funge esclusivamente in qualità di contenitore. Allo stesso modo un terminale deve avere una tratta cavo padre che non corrisponde ad una tratta cavo reale (denominata tratta cavo fittizia) ma funge esclusivamente in qualità di contenitore.

Questa differenza nei due data model induce il connettore alla creazione di sedi tecniche SAP fittizie non presenti su EAM. In EAM, un sostegno/estremo cavo che dà inizio ad una tratta aerea/in cavo necessita della creazione di una campata/tratta cavo SAP fittizia in quanto non vi è corrispondenza con l’omologo EAM.

Una sede tecnica fittizia è identificata dal flag DUMMY impostato a 1 nella tabella X3SAPASSET. Di seguito verrà descritta la gestione delle sedi tecniche fittizie per il sostegno, assumendo un comportamento analogo per l’estremo cavo. Si supponga di avere un sostegno in EAM (asset numero 1000) che costituisce l’inizio di una tratta aerea. Pertanto, questo sostegno non sarà preceduto da una campata (potrebbe essere il primo elemento della linea oppure essere preceduto dall’estremo cavo di fine di una precedente tratta in cavo). Al momento della creazione di questo sostegno nella X3SAPASSET verranno applicate le regole di mapping per il percorso gerarchico LINEA /SOSTEGNO AEREO, riportate di seguito.

MAXOBJECT HIERARCHYPATH MAXLEVEL SAPCLASS SAPLEVEL STEP2TARGET ------------------------------------------------------------------------------------------------------------------------------------------------------------

ASSET LINEA\SOSTEGNO AEREO 2 SOSTEGNO 3 ASSET_AND_BINDING

ASSET LINEA\SOSTEGNO AEREO 2 CAMPATA 2 ASSET

ASSET LINEA\SOSTEGNO AEREO 2 CONDUTTORE 3 ASSET

ASSET LINEA\SOSTEGNO AEREO 2 MORSETTERIA 3 ASSET ASSET LINEA\SOSTEGNO AEREO 2 CORDA DI GUARDIA 3 ASSET ASSET LINEA\SOSTEGNO AEREO 2 ISOLATORE 3 ASSET

Queste regole di mapping fanno sì che, alla creazione di un sostegno EAM, l’intera struttura delle sedi tecniche SAP a partire dal secondo livello campata fino ai terzi livelli sostegno, conduttore, morsetteria, corda di guardia e isolatore laddove una sede tecnica corrispondente non esista. Nell’eventualità in cui la sede tecnica padre (la campata) non esistesse, allora la stessa verrebbe creata con il flag DUMMY impostato a 1 (sede tecnica fittizia necessaria per contenere la sede tecnica sostegno).

Figura - Creazione campata fittizia

Se successivamente la campata (fino ad allora non presente in EAM) verrà creata allora il flag DUMMY della campata SAP verrà impostato a 0 in quanto corrisponde ad una effettiva campata aerea EAM.

Figura - Trasformazione di una campata fittizia in una campata reale

Una cancellazione di una campata aerea EAM imposterà il flag DUMMY a 1 in quanto la campata SAP non potrà essere cancellata fino a quando il sostegno EAM corrispondente non sarà cancellato.

Infine, la cancellazione di un sostegno EAM comporterà la cancellazione della relativa campata SAP se avente il flag DUMMY impostato ad 1.

### Allineamento struttura SAP

Le modifiche apportate alla anagrafica EAM possono produrre la creazione di nuovi asset non ancora creati in SAP. Per questi casi occorre creare i corrispondenti record nella X3SAPASSET. Quando viene creato un record nella X3SAPASSET è possibile valorizzare tutti i campi eccetto il SAPUID in quanto quest’ultimo dovrà essere generato e ritornato da SAP in seguito alla creazione del corrisponde record. Inoltre, l’id interno del padre (PARENTSAPUID) potrebbe non essere valorizzabile nel caso in cui il padre è in attesa di creazione.

L’azione di sincronizzazione (SYNCHACTION) sarà valorizzata secondo il seguente schema e potrà assumere i valori di ADD, UPDATE e DELETE. Lo schema seguente indica l’azione di sincronizzazione per ogni azione di aggiornamento (ACTION) registrata nel MBO X3OBJECTCHANGE da realizzare (SYNCHACTION nuova) in funzione dell’azione di sincronizzazione in attesa (SYNCHACTION corrente) e alla valorizzazione dell’attributo SAPUID. Nello schema sono riportate anche le eventuali combinazioni che non dovrebbero mai verificarsi.

| X3OBJECTCHANGE | X3SAPASSET | X3SAPASSET | X3SAPASSET |  |
| --- | --- | --- | --- | --- |
| ACTION | SYNCHACTION corrente | SAPUID | SYNCHACTION nuova | Note |
| ADDCHANGE |  | Non valorizzato | Errore | Il record SAP non è stato ancora creato pertanto la SYNCHACTION corrente dovrebbe essere ADD. |
| ADDCHANGE |  | Valorizzato | UPDATE | Il record SAP esiste ed è sincronizzato per cui occorre effettuare un UPDATE. |
| ADDCHANGE | ADD | Non valorizzato | ADD | Il record SAP esiste e non è stato ancora creato pertanto occorre effettuare una ADD. |
| ADDCHANGE | ADD | Valorizzato | Errore | Il record SAP esiste e ha il SAPUID per cui la SYNCHACTION corrente non può essere ADD. |
| ADDCHANGE | UPDATE | Non valorizzato | Errore | Il record SAP non è stato ancora creato per cui la SYNCHACTION corrente non può essere UPDATE. |
| ADDCHANGE | UPDATE | Valorizzato | UPDATE | Il record SAP esiste ed è già in attesa UPDATE. |
| ADDCHANGE | DELETE | Non valorizzato | Errore | Il record SAP non è stato creato per cui la SYNCHACTION corrente non può essere DELETE. |
| ADDCHANGE | DELETE | Valorizzato | UPDATE | Il record SAP esiste ed era in attesa di cancellazione. L’azione sarà trasformata in UPDATE. |
| DELETE |  | Non valorizzato | Errore | Il record SAP non è stato creato pertanto la SYNCHACTION corrente dovrebbe essere ADD. |
| DELETE |  | Valorizzato | DELETE | Il record SAP esiste ed è sincronizzato per cui occorre effettuare una DELETE. L’azione di DELETE sarà propagata a tutti i figli. |
| DELETE | ADD | Non valorizzato |  | Il record SAP non è stato ancora sincronizzato in quanto il SAPUID non è valorizzato pertanto verrà rimosso dalla X3SAPASSET insieme a tutti i figli. |
| DELETE | ADD | Valorizzato | Errore | Il record SAP esiste e ha il SAPUID per cui la SYNCHACTION corrente non può essere ADD. |
| DELETE | UPDATE | Non valorizzato | Errore | Il record SAP non è stato ancora creato per cui la SYNCHACTION corrente non può essere UPDATE. |
| DELETE | UPDATE | Valorizzato | DELETE | Il record SAP esiste ed è già in attesa UPDATE per cui l’azione verrà imposta a DELETE. L’operazione di cancellazione sarà propagata a tutti i figli. |
| DELETE | DELETE | Non valorizzato | Errore | Il record SAP non è stato creato per cui la SYNCHACTION corrente non può essere DELETE |
| DELETE | DELETE | Valorizzato | DELETE | Il record SAP esiste ed era in attesa di aggiornamento. L’azione sarà trasformata in UPDATE. Non sarà necessario propagare la cancellazione ai record figli perché già fatta in precedenza. |
| RENAME |  | Valorizzato | UPDATE | Il record è sincronizzato pertanto l’azione è UPDATE |
| RENAME |  | Non valorizzato | Errore | Il record SAP non è stato creato per cui la SYNCHACTION corrente dovrebbe essere ADD. |
| RENAME | ADD | Valorizzato | Errore | Il record SAP è stato già creato per cui la SYNCHACTION corrente non dovrebbe essere ADD. |
| RENAME | ADD | Non valorizzato | ADD | Il record SAP è in attesa di creazione e verrà creato con il nuovo SAP id |
| RENAME | UPDATE | Valorizzato | UPDATE | Il record SAP è in attesa di sincronizzazione e verrà aggiornato con il nuovo SAP id |
| RENAME | UPDATE | Non valorizzato | Errore | Il record SAP non è stato creato per cui la SYNCHACTION corrente dovrebbe essere ADD. |
| RENAME | DELETE | Non valorizzato | Errore | Il record SAP non è stato creato per cui la SYNCHACTION corrente non può essere DELETE. |
| RENAME | DELETE | Valorizzato | Errore | Il record SAP è in attesa di cancellazione pertanto non può essere rinominato. |
| MOVE |  | Valorizzato | UPDATE | Il record SAP è sincronizzato pertanto deve essere aggiornato con il nuovo SAP id e gerarchia aggiornata |
| MOVE |  | Non valorizzato | Errore | Il record SAP non è stato creato per cui la SYNCHACTION corrente dovrebbe essere ADD. |
| MOVE | ADD | Valorizzato | Errore | Il record SAP è stato già creato per cui la SYNCHACTION corrente non dovrebbe essere ADD. |
| MOVE | ADD | Non valorizzato | ADD | Il record SAP è in attesa di creazione e verrà creato con il nuovo SAP id e gerarchia aggiornata |
| MOVE | UPDATE | Valorizzato | UPDATE | Il record SAP è in attesa di sincronizzazione e verrà aggiornato con il nuovo SAP id e gerarchia aggiornata |
| MOVE | UPDATE | Non valorizzato | Errore | Il record SAP non è stato creato per cui la SYNCHACTION corrente dovrebbe essere ADD. |
| MOVE | DELETE | Non valorizzato | Errore | Il record SAP non è stato creato per cui la SYNCHACTION corrente non può essere DELETE. |
| MOVE | DELETE | Valorizzato | Errore | Il record SAP è in attesa di cancellazione pertanto non può essere spostato. |

Tabella 9. Regole per identificazione operazioni di sincronizzazione

A seguito di ogni aggiornamento l’eventuale errore di sincronizzazione verrà azzerato in quanto una nuova azione di sincronizzazione è stata impostata. Infine, la data di processamento (PROCESSDATE) del MBO X3OBJECTCHANGE verrà impostata all’orario di sistema per segnare l’avvenuto processamento del record e un eventuale errore (ERRORFLAG e ERROR) registrato verrà annullato. In caso di errore verrà impostato sullo stesso record l’errore (ERRORFLAG e ERROR). Nel caso in cui l’azione fosse DELETE ma non esiste il record corrispondente nella X3SAPASSET l’azione produrrà esclusivamente l’aggiornamento della data di processamento (PROCESSDATE) del MBO X3OBJECTCHANGE.

L’azione di RENAME effettuerà una ricodifica del STRNO del SAP asset corrispondente e dei relativi figli (con aggiornamento per questi del PARENTSTRNO). L’azione di RENAME non prevede un cambio del ROOTSTRNO (a differenza della MOVE).

L’azione di MOVE effettuerà una ricodifica del STRNO del SAP asset corrispondente e dei relativi figli (con aggiornamento per questi del PARENTSTRNO e del ROOTSTRNO se necessario). Inoltre, l’azione di MOVE aggiornerà gli ID SAP (SAPUID e SAPPARENTUID) per riflettere la nuova gerarchia.

### Esempio

Si riporta di seguito un esempio di creazione di una campata con aggregato conduttori e relativi conduttori. La struttura dell’ASSET sarà la seguente:

MAXUID SITEID ASSETNUM CLASSSTRUCTUREID PARENT X3SAPID --------------------------------------------------------------------------------------------------------------------------------------------------

1 TERNA 1000 LINEA 22F22A1

2 TERNA 1001 LINEA/CAMPATA AEREA

3 TERNA 1002 LINEA/CAMPATA AEREA/CONDUTTORI 1001 4 TERNA 1003 LINEA/CAMPATA AEREA/CONDUTTORI/CONDUTTORE 1002

5 TERNA 1004 LINEA/CAMPATA AEREA/CONDUTTORI/CONDUTTORE 1002

6 TERNA 1005 LINEA/CAMPATA AEREA/CONDUTTORI/CONDUTTORE 1002

La struttura della ASSETLOCRELATION che lega la campata di secondo livello alla linea sarà:

SITEID SOURCEASSETNUM TARGETASSETNUM X3SAPID -----------------------------------------------------------------------------------------------

TERNA 1000 1001 22F22A1-001-----

La creazione contestuale della campata, dell’aggregato conduttori e dei conduttori registrerebbe le seguenti modifiche nella X3OBJECTCHANGE:

ROOTSITEID ROOTASSETNUM OWNERTABLE OWNERNUM MAXLEVEL SAPLEVEL OPERATION -----------------------------------------------------------------------------------------------------------------------------------------------

TERNA 1000 ASSET 1001 2 2 ADDCHANGE

TERNA 1000 ASSET 1002 3 3 ADDCHANGE

TERNA 1000 ASSET 1003 4 3 ADDCHANGE TERNA 1000 ASSET 1004 4 3 ADDCHANGE

TERNA 1000 ASSET 1005 4 3 ADDCHANGE

L’aggiornamento della X3SAPASSET sarà

ID STRNO SAPCLASS SAPLEVEL PARENTSTRNO ROOTSTRNO SAPUID PARENTSAPUID SYNCHACTION ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

1 21345A1 LINEA 1 21345A1 0?0000

2 21345A1-001----- CAMPATA 2 21345A1 21345A1 0?0000 ADD

3 21345A1-001------C CONDUTTORI 3 21345A1-001----- 21345A1 ADD

Si noti che in fase di aggiornamento della X3SAPASSET sarà possibile ricostruire la gerarchia mediante l’id SAP (STRNO, PARENTSTRNO e ROOTSTRNO), ma la gerarchia mediante id interni SAP (SAPUID e PARENTSAPUID) è parzialmente valorizzata in quanto in attesa della sincronizzazione. Le operazioni di sincronizzazione in questo caso saranno di ADD e una volta eseguite forniranno il SAPUID di campata e conduttori che andranno a completare la gerarchia basata su id interni.

ID STRNO SAPCLASS SAPLEVEL PARENTSTRNO ROOTSTRNO SAPUID PARENTSAPUID SYNCHACTION

----------------------------------------------------------------------------------------------------------------------------------------------------------------

1 21345A1 LINEA 1 21345A1 0?0000

2 21345A1-001----- CAMPATA 2 21345A1 21345A1 0?0001 0?0000

3 21345A1-001------C CONDUTTORI 3 21345A1-001----- 21345A1 0?0002 0?0001

Infine, si riporta la valorizzazione della tabella X3SAPTOMAXOBJECT che registra i collegamenti dei record SAP ed EAM:

SAPASSETID MAXOBJECT MAXUID SITEID OBJECTNUM HIERARCHYPATH

-----------------------------------------------------------------------------------------------------------------------------------------------------------------------

1 ASSET 1 TERNA 1000 LINEA

2 ASSET 2 TERNA 1001 LINEA/CAMPATA AEREA

2 ASSET 3 TERNA 1002 LINEA/CAMPATA AEREA/CONDUTTORI

3 ASSET 3 TERNA 1002 LINEA/CAMPATA AEREA/CONDUTTORI

3 ASSET 4 TERNA 1003 LINEA/CAMPATA AEREA/CONDUTTORI/CONDUTTORE

3 ASSET 5 TERNA 1004 LINEA/CAMPATA AEREA/CONDUTTORI/CONDUTTORE

3 ASSET 6 TERNA 1005 LINEA/CAMPATA AEREA/CONDUTTORI/CONDUTTORE

Si noti che:

l’asset SAP di tipo LINEA (SAPASSETID=1) ha una sola corrispondenza con l’asset EAM di tipo LINEA (MAXOBJECT=ASSET e OBJECTNUM=1000)

l’asset SAP di tipo CAMPATA (SAPASSETID=2) ha una corrispondenza con l’asset EAM di tipo CAMPATA (MAXOBJECT=ASSET e OBJECTNUM=1001) e una corrispondenza con l’asset EAM di tipo CONDUTTORI (ASSETNUM=1002)

l’asset SAP di tipo CONDUTTORI (SAPASSETID=3) ha una corrispondenza con l’asset EAM di tipo CONDUTTORI (MAXOBJECT=ASSET e OBJECTNUM=1002) e tre corrispondenze con gli asset EAM di tipo CONDUTTORE (MAXOBJECT=ASSET e OBJECTNUM=1003, 1004, 1005)

Le 4 relazioni al punto 3 saranno prodotte per effetto della configurazione in X3MAXIMOSAPMAP riportata nuovamente di seguito.

MAXOBJECT HIERARCHYPATH MAXLEVEL SAPCLASS SAPLEVEL HIERARCHY -------------------------------------------------------------------------------------------------------------------------------------------------------------------

ASSET LINEA 1 LINEA 1 1

ASSET LINEA/CAMPATA AEREA 2 CAMPATA 2 1

ASSET LINEA/CAMPATA AEREA/CONDUTTORI 3 CAMPATA 2 0

ASSET LINEA/CAMPATA AEREA/CONDUTTORI 3 CONDUTTORI 3 1

ASSET LINEA/CAMPATA AEREA/CONDUTTORI/CONDUTTORE 4 CONDUTTORI 3 1

### Inversione linea

L’inversione della linea richiede una gestione dedicata in quanto richiede una gestione speciale delle campata/tratte cavo fittizie, lo swap dei SAP id e lo spostamento di sostegni, terminali e camere giunti. Inoltre, tutte le modifiche non potranno essere gestite in modo isolato ma devono essere gestite in modo atomico. Per questa sua specificità lo step 2 avrà un handler dedicato allo scenario di inversione della linea. L’handler verrà impiegato nel caso in cui si elabori un primo record X3OBJECTCHANGE legato alla inversione della linea. L’handler collezionerà tutti i record relativi alla stessa transazione nell’ordine indicato nel campo SEQUENCE.

Si prenda come esempio la seguente linea di partenza:

La cui rappresentazione in X3SAPASSET è la seguente.

ID STRNO SAPCLASS SAPLEVEL PARENTSTRNO SAPUID PARENTSAPUID DUMMY ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

1 22F22A1 LINEA 1 0?0000

2 22F22A1-100----- CAMPATA 2 22F22A1 0?0001 0?0000 1

3 22F22A1-100------C CONDUTTORI 3 22F22A1-100----- 0?0002 0?0001 4 22F22A1-100------G CORDE DI GUARDIA 3 22F22A1-100----- 0?0003 0?0001

5 22F22A1-100------S SOSTEGNO 3 22F22A1-100----- 0?0004 0?0001

6 22F22A1-100------I ISOLATORI 3 22F22A1-100----- 0?0005 0?0001 7 22F22A1-100------M MORSETTERIA 3 22F22A1-100----- 0?0006 0?0001 8 22F22A1-101----- CAMPATA 2 22F22A1 0?0007 0?0000

9 22F22A1-101------C CONDUTTORI 3 22F22A1-101----- 0?0008 0?0007 10 22F22A1-101------G CORDE DI GUARDIA 3 22F22A1-101----- 0?0009 0?0007

11 22F22A1-101------S SOSTEGNO 3 22F22A1-101----- 0?0010 0?0007

12 22F22A1-101------I ISOLATORI 3 22F22A1-101----- 0?0011 0?0007 13 22F22A1-101------M MORSETTERIA 3 22F22A1-101----- 0?0012 0?0007 14 22F22A1-102----- TRATTA CAVO 2 22F22A1 0?0013 0?0000 1

15 22F22A1-102------H TERMINALE 3 22F22A1-102----- 0?0014 0?0013 16 22F22A1-103----- TRATTA CAVO 2 22F22A1 0?0015 0?0000

17 22F22A1-103------R CAMERA GIUNTI 3 22F22A1-103----- 0?0016 0?0015 18 22F22A1-104----- TRATTA CAVO 2 22F22A1 0?0017 0?0000

19 22F22A1-104----- TERMINALE 3 22F22A1-104----- 0?0018 0?0017

A seguito della inversione la linea diventerà la seguente.

La prima azione eseguita sarà di rinomina del STRNO in un valore temporaneo di tutti i secondi livelli della linea e relativi figli al netto dei terzi livelli SAP di tipo sostegno, terminale e camera giunto. Il valore temporaneo sarà ottenuto aggiungendo il prefisso (!) agli STRNO. Questa prima azione permetterà di eseguire agevolmente la rinomina dei secondi livelli. Si riportano in rosso le righe oggetto di modifica ed in grassetto il campo modificato.

ID STRNO SAPCLASS SAPLEVEL PARENTSTRNO SAPUID PARENTSAPUID DUMMY ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

1 22F22A1 LINEA 1 0?0000

2 !22F22A1-100----- CAMPATA 2 22F22A1 0?0001 0?0000 1

3 !22F22A1-100------C CONDUTTORI 3 22F22A1-100----- 0?0002 0?0001 4 !22F22A1-100------G CORDE DI GUARDIA 3 22F22A1-100----- 0?0003 0?0001

5 22F22A1-100------S SOSTEGNO 3 22F22A1-100----- 0?0004 0?0001

6 !22F22A1-100------I ISOLATORI 3 22F22A1-100----- 0?0005 0?0001 7 !22F22A1-100------M MORSETTERIA 3 22F22A1-100----- 0?0006 0?0001 8 !22F22A1-101----- CAMPATA 2 22F22A1 0?0007 0?0000

9 !22F22A1-101------C CONDUTTORI 3 22F22A1-101----- 0?0008 0?0007 10 !22F22A1-101------G CORDE DI GUARDIA 3 22F22A1-101----- 0?0009 0?0007

11 22F22A1-101------S SOSTEGNO 3 22F22A1-101----- 0?0010 0?0007

12 !22F22A1-101------I ISOLATORI 3 22F22A1-101----- 0?0011 0?0007 13 !22F22A1-101------M MORSETTERIA 3 22F22A1-101----- 0?0012 0?0007 14 !22F22A1-102----- TRATTA CAVO 2 22F22A1 0?0013 0?0000 1

15 22F22A1-102------H TERMINALE 3 22F22A1-102----- 0?0014 0?0013 16 !22F22A1-103----- TRATTA CAVO 2 22F22A1 0?0015 0?0000

17 22F22A1-103------R CAMERA GIUNTI 3 22F22A1-103----- 0?0016 0?0015 18 !22F22A1-104----- TRATTA CAVO 2 22F22A1 0?0017 0?0000

19 22F22A1-104------ TERMINALE 3 22F22A1-104----- 0?0018 0?0017

A questo punto si potrà applicare la rinomina dei secondi livelli (in rosso) che grazie all’azione precedente non creerà sedi tecniche duplicate.

ID STRNO SAPCLASS SAPLEVEL PARENTSTRNO SAPUID PARENTSAPUID DUMMY ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

1 22F22A1 LINEA 1 0?0000

2 !22F22A1-100----- CAMPATA 2 22F22A1 0?0001 0?0000 1

3 !22F22A1-100------C CONDUTTORI 3 22F22A1-100----- 0?0002 0?0001 4 !22F22A1-100------G CORDE DI GUARDIA 3 22F22A1-100----- 0?0003 0?0001

5 22F22A1-100------S SOSTEGNO 3 22F22A1-100----- 0?0004 0?0001

6 !22F22A1-100------I ISOLATORI 3 22F22A1-100----- 0?0005 0?0001 7 !22F22A1-100------M MORSETTERIA 3 22F22A1-100----- 0?0006 0?0001 8 22F22A1-100----- CAMPATA 2 22F22A1 0?0007 0?0000

9 22F22A1-100------C CONDUTTORI 3 22F22A1-100----- 0?0008 0?0007 10 22F22A1-100------G CORDE DI GUARDIA 3 22F22A1-100----- 0?0009 0?0007

11 22F22A1-101------S SOSTEGNO 3 22F22A1-101----- 0?0010 0?0007

12 22F22A1-100------I ISOLATORI 3 22F22A1-100----- 0?0011 0?0007 13 22F22A1-100------M MORSETTERIA 3 22F22A1-100----- 0?0012 0?0007 14 !22F22A1-102----- TRATTA CAVO 2 22F22A1 0?0013 0?0000 1

15 22F22A1-102------H TERMINALE 3 22F22A1-102----- 0?0014 0?0013 16 22F22A1-102----- TRATTA CAVO 2 22F22A1 0?0015 0?0000

17 22F22A1-103------R CAMERA GIUNTI 3 22F22A1-103----- 0?0016 0?0015 18 22F22A1-103----- TRATTA CAVO 2 22F22A1 0?0017 0?0000

19 22F22A1-104------ TERMINALE 3 22F22A1-104----- 0?0018 0?0017

Si noti che la rinomina dei secondi livelli è parziale in quanto i secondi livelli fittizi (campata 22F22A1-100----- e tratta cavo 22F22A1-102-----) non presenti in EAM non hanno una ricodifica diretta, ma verranno comunque riutilizzati per i nuovi rispettivi terzi livelli di inizio tratta (sostegno 22F22A1-101------S e tratta cavo 22F22A1-104------). Pertanto, si eseguirà la loro rinomina come segue (in verde):

ID STRNO SAPCLASS SAPLEVEL PARENTSTRNO SAPUID PARENTSAPUID DUMMY ------------------------------------------------------------------------------------------------------------------------------------------------------------------------

1 22F22A1 LINEA 1 0?0000

2 22F22A1-101----- CAMPATA 2 22F22A1 0?0001 0?0000 1

3 22F22A1-101------C CONDUTTORI 3 22F22A1-101----- 0?0002 0?0001 4 22F22A1-101------G CORDE DI GUARDIA 3 22F22A1-101----- 0?0003 0?0001

5 22F22A1-100------S SOSTEGNO 3 22F22A1-100----- 0?0004 0?0001

6 22F22A1-101------I ISOLATORI 3 22F22A1-101----- 0?0005 0?0001 7 22F22A1-101------M MORSETTERIA 3 22F22A1-101----- 0?0006 0?0001 8 22F22A1-100----- CAMPATA 2 22F22A1 0?0007 0?0000

9 22F22A1-100------C CONDUTTORI 3 22F22A1-100----- 0?0008 0?0007 10 22F22A1-100------G CORDE DI GUARDIA 3 22F22A1-100----- 0?0009 0?0007

11 22F22A1-101------S SOSTEGNO 3 22F22A1-101----- 0?0010 0?0007

12 22F22A1-100------I ISOLATORI 3 22F22A1-100----- 0?0011 0?0007 13 22F22A1-100------M MORSETTERIA 3 22F22A1-100----- 0?0012 0?0007 14 22F22A1-104----- TRATTA CAVO 2 22F22A1 0?0013 0?0000 1

15 22F22A1-102------H TERMINALE 3 22F22A1-102----- 0?0014 0?0013 16 22F22A1-102----- TRATTA CAVO 2 22F22A1 0?0015 0?0000

17 22F22A1-103------R CAMERA GIUNTI 3 22F22A1-103----- 0?0016 0?0015 18 22F22A1-103----- TRATTA CAVO 2 22F22A1 0?0017 0?0000

19 22F22A1-104------ TERMINALE 3 22F22A1-104----- 0?0018 0?0017

A questo punto sarà possibile spostare i terzi livelli (in blue) sotto i rispettivi secondi livelli

ID STRNO SAPCLASS SAPLEVEL PARENTSTRNO SAPUID PARENTSAPUID DUMMY ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ 1 22F22A1 LINEA 1 0?0000 2 22F22A1-101----- CAMPATA 2 22F22A1 0?0001 0?0000 1 3 22F22A1-101------C CONDUTTORI 3 22F22A1-101----- 0?0002 0?0001 4 22F22A1-101------G CORDE DI GUARDIA 3 22F22A1-101----- 0?0003 0?0001 5 22F22A1-100------S SOSTEGNO 3 22F22A1-100----- 0?0004 0?0007 6 22F22A1-101------I ISOLATORI 3 22F22A1-101----- 0?0005 0?0001 7 22F22A1-101------M MORSETTERIA 3 22F22A1-101----- 0?0006 0?0001 8 22F22A1-100----- CAMPATA 2 22F22A1 0?0007 0?0000 9 22F22A1-100------C CONDUTTORI 3 22F22A1-100----- 0?0008 0?0007 10 22F22A1-100------G CORDE DI GUARDIA 3 22F22A1-100----- 0?0009 0?0007 11 22F22A1-101------S SOSTEGNO 3 22F22A1-101----- 0?0010 0?0001 12 22F22A1-100------I ISOLATORI 3 22F22A1-100----- 0?0011 0?0007 13 22F22A1-100------M MORSETTERIA 3 22F22A1-100----- 0?0012 0?0007 14 22F22A1-104----- TRATTA CAVO 2 22F22A1 0?0013 0?0000 1 15 22F22A1-102------H TERMINALE 3 22F22A1-102----- 0?0014 0?0015 16 22F22A1-102----- TRATTA CAVO 2 22F22A1 0?0015 0?0000 17 22F22A1-103------R CAMERA GIUNTI 3 22F22A1-103----- 0?0016 0?0017 18 22F22A1-103----- TRATTA CAVO 2 22F22A1 0?0017 0?0000 19 22F22A1-104------ TERMINALE 3 22F22A1-104----- 0?0018 0?0013

A pure titolo di verifica si riordina la X3SAPASSET secondo la sequenza dei secondi livelli EAM al fine di visualizzare la struttura in linea con la linea rappresentata in EAM.

ID STRNO SAPCLASS SAPLEVEL PARENTSTRNO SAPUID PARENTSAPUID DUMMY ------------------------------------------------------------------------------------------------------------------------------------------------------------------------ 1 22F22A1 LINEA 1 0?0000 14 22F22A1-104----- TRATTA CAVO 2 22F22A1 0?0013 0?0000 1 19 22F22A1-104------ TERMINALE 3 22F22A1-104----- 0?0018 0?0013

18 22F22A1-103----- TRATTA CAVO 2 22F22A1 0?0017 0?0000

17 22F22A1-103------R CAMERA GIUNTI 3 22F22A1-103----- 0?0016 0?0017 16 22F22A1-102----- TRATTA CAVO 2 22F22A1 0?0015 0?0000 15 22F22A1-102------H TERMINALE 3 22F22A1-102----- 0?0014 0?0015

2 22F22A1-101----- CAMPATA 2 22F22A1 0?0001 0?0000 1 3 22F22A1-101------C CONDUTTORI 3 22F22A1-101----- 0?0002 0?0001 4 22F22A1-101------G CORDE DI GUARDIA 3 22F22A1-101----- 0?0003 0?0001 11 22F22A1-101------S SOSTEGNO 3 22F22A1-101----- 0?0010 0?0001 6 22F22A1-101------I ISOLATORI 3 22F22A1-101----- 0?0005 0?0001 7 22F22A1-101------M MORSETTERIA 3 22F22A1-101----- 0?0006 0?0001 8 22F22A1-100----- CAMPATA 2 22F22A1 0?0007 0?0000 9 22F22A1-100------C CONDUTTORI 3 22F22A1-100----- 0?0008 0?0007 10 22F22A1-100------G CORDE DI GUARDIA 3 22F22A1-100----- 0?0009 0?0007

5 22F22A1-100------S SOSTEGNO 3 22F22A1-100----- 0?0004 0?0007 12 22F22A1-100------I ISOLATORI 3 22F22A1-100----- 0?0011 0?0007 13 22F22A1-100------M MORSETTERIA 3 22F22A1-100----- 0?0012 0?0007

## Step 3: predisposizione messaggio di integrazione

Al termine dello step operativo precedente il MBO X3SAPASSET conterrà l’intera struttura SAP per linee e stazioni aggiornata con le operazioni di sincronizzazione necessarie (SYNCHACTION). Inoltre, il MBO X3SAPTOMAXOBJECT conterrà i legami fra gli ASSET e i X3SAPASSET.

Sulla base delle operazioni di sincronizzazione richieste, in questo step saranno predisposti i messaggi di sincronizzazione recuperati dagli asset EAM. I dati oggetto dei messaggi di sincronizzazione (attributi e specifiche tecniche) saranno memorizzati nel MBO X3SAPASSETDATA che avrà la seguente struttura.

| Attributo | Tipo | Descrizione | Valori |
| --- | --- | --- | --- |
| SAPASSETID | X3SAPASSET.X3SAPASSETID | Id SAP asset |  |
| TYPE | UPPER 10 | Tipo dato (attributo o specifica tecnica) | ATTRIBUTE, SPEC |
| NAME | UPPER 50 | Nome campo di testata SAP (se TYPE = ATTRIBUTE) o nome caratteristica SAP (se TYPE = SPEC) |  |
| VALUE | ALN 250 | Valore in formato stringa dell’attributo o specifica |  |

Tabella 10. MBO X3SAPASSETDATA

I valori ricavati dagli attributi MBO (ASSET) e/o delle specifiche tecniche (ASSETSPEC) saranno rappresentati come alfanumerici secondo la rappresentazione dei tipi primitivi indicata nella raccomandazione del W3C consultabile alla pagina https://www.w3.org/TR/xmlschema-2/. Questa raccomandazione definisce la rappresentazione dei tipi primitivi (interi, decimali, booleani, date, stringhe, orari ed altri) adottata sia nella codifica XML per web services che nella codifica JSON per REST API. Pertanto, i valori raccolti saranno già nel formato di codifica adottato dai flussi di integrazione.

Il recupero dei valori avverrà per tutti i record SAP in attesa di sincronizzazione.

select * from x3sapasset

where synchaction is not null

order by rootstrno, saplevel

Per ogni record in attesa di sincronizzazione saranno recuperati i campi di testata e le caratteristiche.

Figura . Flusso di recupero dati di sincronizzazione

### Attributi di testata

Gli attributi di testata saranno scritti da una funzionalità applicativa del connettore che seguirà le regole di mapping fra gli attributi di testata delle sedi tecniche/equipment SAP e gli attributi degli MBO EAM.

### Caratteristiche

Il recupero delle caratteristiche è soggetto a regole di trasposizione (o anche chiamate regole di mapping) che indicano come ricavare una caratteristica SAP per uno specifico record SAP dal data-model to-be.

In linea di massima il mapping è costituito dal legame ad una specifica tecnica EAM, ma in alcuni casi il recupero della caratteristica è caratterizzato da altri procedimenti basati su navigazione della gerarchia, calcoli e formattazioni. Ad esempio, la caratteristica che indica il numero di giunti di una MORSETTERIA SAP è ottenuta contando il numero di asset di tipo GIUNTO CONDUTTORE al di sotto dell’asset CONDUTTORI relativo alla stessa campata della morsetteria.

Inoltre, la sorgente del dato EAM potrebbe non coincidere con l’asset corrispondente al record SAP. Ad esempio, per una CAMPATA SAP le caratteristiche tecniche in alcuni casi sono attinte dall’asset EAM di tipo CAMPATA, in altri casi dall’asset CONDUTTORI che soggiace la campata.

L’alto numero di caratteristiche tecniche da mappare (circa 400 per le linee e 2000 per le stazioni) ha richiesto una fase di analisi per valutare se i criteri di mapping fossero ricorrenti. Tale analisi ha confermato (ad oggi per le sole linee) che la totalità delle caratteristiche richiede all’incirca una decina di differenti criteri di mapping codificabili in operazioni di mapping generiche. Questo risultato permetterà di circoscrivere le attività implementative ad un numero ristretto di operazioni di mapping e permetterà la configurazione delle regole a caldo sul sistema.

| Operazione | Descrizione | Numero caratteristiche |
| --- | --- | --- |
| GetSpec | Ritorna la specifica tecnica EAM configurata | 145 |
| GetSapIdBySpec | Recupera il numero dell’asset (ASSET) dalla specifica tecnica EAM configurata e ne ritorna il valore X3SAPID | 8 |
| GetMostRecentSpec | Ritorna la specifica tecnica EAM aggiornata più recentemente fra un insieme di valori della specifica recuperati da più MBO | 39 |
| Count | Ritorna il numero di MBO ottenuti tramite la relazione | 6 |
| HasAssets | Ritorna “S” se il numero di MBO ottenuti tramite la relazione è maggiore di 0 altrimenti ritorna “” | 8 |
| SumSpecValues | Ritorna la somma dei valori della specifica tecnica per i MBO recuperati | 12 |
| MaxSpecValues | Ritorna il massimo dei valori della specifica tecnica per i MBO recuperati | 1 |
| Custom | Esegue l’automation script configurato e ne ritorna il valore prodotto | 9 |

Tabella 12. Regole di mapping caratteristiche

Il MBO destinato alla codifica delle regole di mapping è X3SAPASSETSPECRULE che avrà la seguente struttura.

| Attributo | Tipo | Descrizione | Valori |
| --- | --- | --- | --- |
| SAPCLASS | X3MAXIMOSAPMAP.SAPCLASS | Tipo elemento SAP | Ad esempio: LINEA, CAMPATA, CONDUTTORE |
| SAPNAME | UPPER 50 | Nome caratteristica SAP | Ad esempio: PMT_ELEM_INDISP, PMT_TIPO_CONDUT |
| RULE | ALN 50 | Nome operazione di mapping |  |
| DESCRIPTION | ALN 250 | Descrizione regola di mapping |  |
| MAXNAME | ASSETATTRIBUTE.ASSETATTRID | Nome specifica tecnica EAM | Ad esempio: LIN_ELEMENTO_INDISP, LIN_CONDUTTORE_AEREO |
| MAXRELATIONSHIP | CONDITION.CONDITIONNUM | Condizione contenente la clausola SQL where per la selezione degli MBO che saranno valutati dalla operazione di mapping |  |
| DEFAULTVALUE | UPPER 20 | Valore default da applicare |  |
| FORMAT | ALN 250 | Regola di formattazione del valore |  |
| SCRIPTNAME | AUTOSCRIPT.AUTOSCRIPT | Nome automation script da eseguire nel caso di operazione custom |  |
| IFEXPR | CONDITION.CONDITIONNUM | Condizione contenente la clausola SQL where in aggiunta alla clausola espressa nell’attributo MAXRELATIONSHIP |  |

Tabella 11. MBO X3SAPASSETSPECRULE

Gli attributi SAPCLASS e SAPNAME permettono di selezionare la regola di mapping per un record SAP e una specifica caratteristica. Pertanto, dato un MBO X3SAPASSET le regole per la generazione di tutte le caratteristiche potranno essere recuperate tramite la SAPCLASS.

select * from x3sapassetspecrule

where sapclass = {1}

dove {1} è l’attributo SAPCLASS del MBO X3SAPASSET.

L’attributo RULE identifica l’operazione di mapping per calcolare il valore della caratteristica. In corrispondenza di ogni operazione sarà avviata una logica applicativa che applicherà la regola di mapping. L’operazione opererà su un insieme di MBO che saranno filtrati mediante la clausola contenuta nell’attributo MAXRELATIONSHIP. La clausola sarà parametrizzata per mezzo del MBO X3SAPASSET.

Si consideri, ad esempio, la caratteristica PMT_DISL_ATT_CDG per una sede tecnica di tipo CAMPATA. Il valore della caratteristica risiede nella specifica tecnica LIN_DISLIVELLO_ATTACCO_FDG della campata EAM corrispondente come indicato nella figura seguente.

Figura . Esempio di regola di mapping caratteristiche

La configurazione della X3SAPASSETSPECRULE è la seguente.

SAPCLASS SAPNAME RULE MAXNAME MAXRELATIONSHIP -------------------------------------------------------------------------------------------------------------------------------

CAMPATA PMT_DISL_ATT_CDG GetSpec LIN_DISLIVELLO_ATTACCO_FDG CAMPATA

L’operazione di mapping GetSpec ritorna il valore della specifica tecnica LIN_DISLIVELLO_ATTACCO_FDG indicata nell’attributo MAXNAME della campata EAM corrispondente. Il MBO ASSETSPEC relativo alla specifica tecnica della campata EAM è ottenuto tramite la clausola CAMPATA indicata nell’attributo MAXRELATIONSHIP.

La clausola fornita dalla condizione CAMPATA sarà applicata come filtro al MBO ASSETSPEC

select * from assetspec where

(siteid, assetnum) in

(select siteid, assetnum from X3SAPTOMAXOBJECT

where

sapassetid = :id and

hierarchypath = ‘LINEA/CAMPATA AEREA’

)

Si noti che la clausola è parametrizzata rispetto al MBO X3SAPASSET (sede tecnica SAP di tipo CAMPATA). Pertanto, il parametro :id sarà sostituto con l’ID del MBO X3SAPASSET corrispondente alla campata SAP.

L’attributo DEFAULTVALUE è un valore opzionale che l’operazione di mapping ritorna nel caso in cui il valore calcolato è una stringa vuota o nulla.

L’attributo FORMAT è un eventuale stringa di formattazione da applicare al dato recuperato. La sintassi della formattazione è quella documentata alla pagina Java Formatter.

L’attributo SCRIPTNAME è il nome dell’automation script che verrà eseguito per l’operazione di mapping Custom. Tale operazione di mapping fornisce la possibilità di determinare valori di caratteristiche il cui calcolo è specifico e non può essere realizzato mediante una operazione generica.

Infine, l’attributo IFEXPR è una condizione addizionale opzionale che viene applicata in aggiunta alla clausola indicata in MAXRELATIONSHIP. Tale condizione può essere sfruttata per casi specifici in cui la clausola della relazione richiede un ulteriore affinamento.

### Classi SAP

Le Sedi Tecniche relative alle stazioni condividono lo stesso profilo catalogo (RBNR), pertanto per le nuove ST non sarà sufficiente a determinare quali classi SAP devono essere associate. Il connettore si occuperà di inviare l’elenco delle classi seguendo una tabella di mapping basata su regole (regular expression) da applicare agli STRNO delle ST stesse.

## Step 4: sincronizzazione

Gli step precedenti avranno predisposto la struttura dei record SAP nei MBO X3SAPASSET e X3SAPTOMAXOBJECT con le relative operazioni di sincronizzazione necessarie (ADD, UPDATE e DELETE) e i dati di attributi di testata e caratteristiche nel MBO X3SAPASSETDATA. Pertanto, sono disponibili tutte le informazioni utili all’integrazione con SAP.

La sincronizzazione di una linea/stazione procederà eseguendo le azioni nell’ordine DELETE, ADD e UPDATE. Per ogni operazione saranno sincronizzati i record SAP a partire dal livello più basso: 1, 2, 3, 4, 5. L’ordine scelto mira a risolvere prima i puntamenti gerarchici basati sul SAPUID e PARENTSAPUID (ADD) e poi ad aggiornare i dati (attributi di testata e caratteristiche) di ogni singolo record (UPDATE). Per quanto riguarda gli attributi di testata e le caratteristiche saranno sincronizzati tutti i valori a prescindere da quelli effettivamente modificati.

In caso di fallimento di un flusso di sincronizzazione verrà impostato il messaggio di errore nell’attributo ERROR del MBO X3SAPASSET, sia in caso di fallimento che di successo verrà settata la PROCESSDATE. L’esecuzione dei flussi di sincronizzazione della linea/stazione non verrà interrotta con l’obiettivo di raggiungere il massimo livello di sincronizzazione. Evidentemente il fallimento di un flusso potrebbe causare la mancata creazione di precondizioni necessarie ai flussi successivi (ad esempio la creazione di una sede tecnica poi riferita in una caratteristica). In tale situazione sia il connettore EAM che la controparte SAP dovranno prevedere la corretta gestione del caso di errore.

Nei paragrafi seguenti verranno trattati i flussi di sincronizzazione fornendo una struttura logica del servizio in termini di richiesta e risposta. Questa struttura logica mira a focalizzare i dati scambiati.

I flussi generici di cancellazione, aggiunta e aggiornamento saranno mappati su sedi tecniche ed equipment SAP in virtù dell’oggetto di sincronizzazione.

### Cancellazione

Il flusso di cancellazione (azione DELETE) effettua una richiesta di passaggio della sede tecnica/equipment SAP in stato interno FCAN (cancellazione oggetto SAP) con relative operazioni di pulizia previste in SAP (ad esempio riciclo dei codici di linea o stazione, spostamento in sede bidone).

La struttura della richiesta sarà la seguente

<Request>

<Object>ST</Object> <!-- ST per sedi tecniche, EQ per equipment -->

<Action>DELETE</Action>

<Sapuid></Sapuid> <!-- ID interno SAP -->

</Request>

La struttura della risposta sarà la seguente.

<Response>

<Result></Result> <!-- >= 0 successo, < 0 fallimento -->

<Error>

<Code></Code> <!-- codice di errore SAP -->

<Message></Message> <!-- messaggio di errore SAP -->

</Error>

</Response>

La cancellazione di un record SAP di livello n sarà seguita dalla cancellazione dei record SAP in gerarchia con tale record (livello maggiore di n). L’avvenuta cancellazione del record SAP innescherà la cancellazione del relativo record SAP nei MBO X3SAPASSET e X3SAPTOMAXOBJECT.

### Aggiunta

Il flusso di aggiunta (azione ADD) effettua una richiesta di creazione di una sede tecnica/equipment SAP. Tale operazione imposterà solo gli attributi di testata obbligatori per la creazione. Ad ogni flusso di aggiunta seguirà automaticamente un flusso di aggiornamento (azione UPDATE) per la valorizzazione di attributi di testata e caratteristiche.

La struttura della richiesta sarà la seguente

<Request>

<Object>ST</Object> <!-- ST per sedi tecniche, EQ per equipment -->

<Action>ADD</Action>

<Parentsapuid></ Parentsapuid > <!-- ID interno SAP del padre in cui collocare il record -->

<Attributes>

<Attribute> <!-- 1..N -->

<Name>STRNO</Name>

<Value></Value>

</Attribute>

</Attributes>

</Request>

La struttura della risposta sarà la seguente.

<Response>

<Result></Result> <!-- >= 0 successo, < 0 fallimento -->

<Error>

<Code></Code> <!-- codice di errore SAP -->

<Message></Message> <!-- messaggio di errore SAP -->

</Error>

<Sapuid></ Sapuid > <!-- ID interno SAP -->

</Response>

L’avvenuta aggiunta del record SAP innescherà l’aggiornamento di SAPUID e PARENTSAPUID dei corrispondenti record SAP nel MBO X3SAPASSET. Inoltre, l’azione SYNCHACTION sarà impostata ad UPDATE per il record SAP aggiunto.

### Aggiornamento

Il flusso di aggiornamento (azione UPDATE) effettua una richiesta di aggiornamento di una sede tecnica/equipment SAP in termini di attributi di testata e caratteristiche.

La struttura della richiesta sarà la seguente

<Request>

<Object>ST</Object> <!-- ST per sedi tecniche, EQ per equipment -->

<Action>UPDATE</Action>

<Sapuid></ Sapuid > <!-- ID interno SAP -->

<Attributes>

<Attribute> <!-- 1..N -->

<Name></Name> <-- nome attributo di testata -->

<Value></Value> <-- valore attributo di testata -->

</Attribute>

</Attributes> <Specifications>

<Specification> <!-- 1..N -->

<Name></Name> <!-- nome caratteristica -->

<Value></Value> <!-- valore caratteristica -->

</Specification>

</Specifications>

</Request>

La struttura della risposta sarà la seguente.

<Response>

<Result></Result> <!-- >= 0 successo, < 0 fallimento -->

<Error>

<Code></Code> <!-- codice di errore SAP -->

<Message></Message> <!-- messaggio di errore SAP -->

</Error> </Response>

L’avvenuto aggiornamento del record SAP innescherà l’azzeramento della SYNCHACTION nel MBO X3SAPASSET per il record SAP.

### Inversione linea

L’inversione della linea (opportunamente ricalcolata e marcata in X3SAPASSET) sarà oggetto di un flusso unico verso SAP che eseguirà le operazioni indicate nella sequenza fornita.

<Inversione>

<Moves> <!-- azioni di spostamento dei terzi livelli -->

<Move> <!-- 1..N -->

<Tplnr></Tplnr> <!-- ID interno SAP del della sede tecnica -->

<Tplma></Tplma> <!-- ID interno SAP del padre in cui collocare il record -->

</Move>

</Moves>

<Renames> <!-- azioni di rinomina dei secondi livelli -->

<Rename> <!-- 1..N -->

<Tplnr></Tplnr> <!-- ID interno SAP del della sede tecnica -->

<Vecchio_Strno></Vecchio_Strno> <-- STRNO corrente -->

<Nuovo_Strno></Nuovo_Strno> <-- nuovo STRNO -->

</Rename>

</Renames>

</Inversione>

### Parallelismo di esecuzione dei flussi

Come precedentemente indicato, la sincronizzazione avverrà per ogni singola linea/stazione in attesa di sincronizzazione.

select distinct rootstrno from x3sapasset

where synchaction is not null

order by rootstrno

Durante la sincronizzazione di una linea/stazione si procederà eseguendo le azioni di sincronizzazione (SYNCHACTION) nell’ordine DELETE, ADD, UPDATE e per ogni operazione saranno sincronizzati i livelli SAP (SAPLEVEL) nell’ordine 1, 2, 3, 4, 5.

select * from x3sapasset

where rootstrno = {1} and synchaction = ’DELETE’

order by saplevel desc select * from x3sapasset

where rootstrno = {1} and synchaction = ’ADD’

order by saplevel

select * from x3sapasset

where rootstrno = {1} and synchaction = ’UPDATE’

order by saplevel

dove {1} è il ROOTSTRNO della linea/stazione in fase di sincronizzazione.

All’interno di ogni ciclo di sincronizzazione (linea, azione, livello SAP) sarà possibile avviare i flussi in parallelo per ottimizzare i tempi di sincronizzazione. Il livello di parallelismo sarà un parametro di configurazione che verrà determinato in fase di test. Si assume inizialmente che il livello di parallelismo sarà 1 (pertanto un flusso per volta).

## Gestione errori

IBM MAS Manage esporrà una applicazione custom per il monitoraggio degli errori di sincronizzazione per linee e stazioni. La pagina di lista mostrerà linee e stazioni in errore di sincronizzazione. Gli asset in errore sono recuperati mediante la query seguente.

select * from asset where

classstructureid in (select classstructureid from classification where classificationid in (‘LINEA’, ‘STAZIONE’)) and x3sapid in (select rootstrno from x3sapasset where error is not null)

Alla selezione di un asset linea/stazione sarà aperta una schermata di dettaglio che mostrerà una tabella relativa ai record del MBO X3SAPASSET della linea/stazione. In particolare, saranno mostrati gli attributi SYNCHACTION ed ERROR per ogni record SAP. In tal modo l’amministratore potrà avere visibilità degli errori di sincronizzazione e delle azioni di sincronizzazione fallite.

Sarà esposta una azione di risincronizzazione della linea/stazione che avvierà la procedura a partire dallo step indicato nel paragrafo Predisposizione messaggio di integrazione.

# Flusso creazione linee/stazioni

IBM MAS Manage esporrà un servizio di integrazione in-bound per la creazione dei primi livelli di linee e stazioni. La struttura logica della richiesta sarà la seguente

<Request>

<Action>ADD</Action>

<Attributes>

<Attribute> <!-- 1..N -->

<Name></Name> <-- nome attributo di testata -->

<Value></Value> <-- valore attributo di testata -->

</Attribute>

</Attributes> <Specifications>

<Specification> <!-- 1..N -->

<Name></Name> <!-- nome caratteristica -->

<Value></Value> <!-- valore caratteristica -->

</Specification>

</Specifications>

</Request>

La struttura della risposta sarà la seguente.

<Response>

<Result></Result> <!-- >= 0 successo, < 0 fallimento -->

<Error>

<Code></Code> <!-- codice di errore EAM -->

<Message></Message> <!-- messaggio di errore EAM -->

</Error> </Response>

La ricezione di questo flusso di integrazione causerà la creazione della linea in EAM e dei dati necessari al connettore (MBO X3SAPASSET e X3SAPTOMAXOBJECT) senza specificare alcuna SYNCHACTION.

# Sincronizzazione anagrafiche base

In fase di low-level design saranno identificate le anagrafiche di base di attributi e specifiche tecniche che saranno oggetto di sincronizzazione (real-time o batch) da SAP.

# Lista completa mappatura elementi EAM/SAP

# Glossario

IBM MAS (Maximo Application Suite): la suite di prodotti IBM per la gestione degli asset, il tracciamento dello stato di salute degli asset, la predizione dei fallimenti degli asset in base ad algoritmi di failure prediction, la gestione della sensoristica di campo. Per una completa descrizione della suite si rimanda alla documentazione di prodotto.

IBM MAS Manage: la applicazione di IBM MAS per la gestione degli asset (Enterprise Asset Management) fornita dalla suite MAS.

KPI (key performance indicator): indicatore numerico per la misura delle prestazioni di una entità (ad esempio un asset).

Identity provider: sistema per la gestione delle utenze e autenticazione

J2EE (Java 2 Enterprise Edition): insieme di specifiche per la realizzazione di applicazioni web aziendali sviluppate principalmente con tecnologia Java.

IBM RedHat Open Shift: infrastruttura per la creazione di applicazioni basate su container che eseguono su infrastruttura cloud ibrida.

MBO (Maximo Business Object): elemento di IBM MAS Manage che rappresenta una entità di dato che tipicamente viene memorizzata su una tabella di database associata. Il MBO è corredato di logica applicativa che ne caratterizza le logiche di business nonché quelle amministrative (come ad esempio persistenza, consistenza e sicurezza).

Primary key (chiave primaria): insieme di colonne di una tabella che identifica univocamente il record all’interno della tabella stessa.

Foreign key (chiave esterna): insieme di colonne di una tabella che identifica univocamente il record di un’altra tabella.

Classificazione: definizione della natura di un MBO (ad esempio asset) con relative specifiche tecniche. La classificazione può essere inserita in un percorso gerarchico di classificazioni.

MIF (Maximo Integration Framework): componente di IBM MAS Manage deputato allo sviluppo di integrazioni con sistemi esterni al prodotto.

Object structure (struttura oggetto): definizione di un dato oggetto di integrazione realizzato mediante la composizione di MBO.

JMS (Java Message Service): specifica facente parte di J2EE che indirizza le tematiche di scambi di messaggi fra applicazioni mediante l’uso di code.

Apache Kafka: piattaforma distribuita per l’integrazione dati fra applicazioni.

SOAP (Simple Object Access Protocol): protocollo per lo scambio strutturato di informazioni mediante web service basati su codifica XML.

REST API (Representational state transfer API): architettura per l’integrazione di sistemi distribuiti basata su protocollo http.

# Appendice

Si allegano le regole di mapping raccolte durante la fase di disegno logico per caratteristiche

e attributi di testata

# Indice tabelle

# Indice figure
