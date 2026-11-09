# EAM_Documento DSS_v6

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento | Logiche di elaborazione DSS |

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

| Termine | Definizione | Definizione |
| --- | --- | --- |
| TdA | Tasso di Anomalia |  |
| TdG | Tasso di Guasto |  |
| PdL | Piano di Lavoro |  |
| OdL | Ordine di Lavoro |  |
| DSS | Decision Support System |  |

# Obiettivi del documento

L’obiettivo del presente documento è quello di descrivere nel dettaglio il funzionamento della componente DSS di EAM.

# Decision support system (DSS)

Il DSS è il motore di elaborazione delle policy che genera, modifica e cambia stato agli Ordini sul sistema (solo per la gestione della prevalenza per attività, su ordini con stato precedente a schedulabile). Le modalità di elaborazione del DSS sono suddivise in 2 categorie per le quali sono previste diverse logiche di elaborazione sulla base della tipologia specifica di policy e di asset su cui insiste:

On condition:

Standard – elaborazione di policy di tipo on condition che non hanno ulteriori logiche particolari per la generazione degli ordini.

Trainante/trainato – elaborazione di policy di tipo on condition con output attività di sostituzione, che insistono su asset di tipo conduttore o fune di guardia (quarto livello).

Tronco di ispezione – elaborazione di policy di tipo on condition che insistono su campate aeree o tratte cavo (secondo livello) e per le quali è abilitato il flag “tronco di ispezione”.

Aggregate – elaborazione di policy di tipo aggregato per le quali la logica di generazione si basa su coefficienti di aggregazione calcolati tramite formule cablate a sistema.

Periodiche:

Standard – elaborazione di policy di tipo periodico inter o intra annuale che non hanno ulteriori logiche particolari di generazione di ordini.

Tronco di ispezione – elaborazione di policy di tipo periodico che insistono su campate aeree o tratte cavo (secondo livello) e per le quali è abilitato il flag “tronco di ispezione”.

Trainante-trainato – elaborazione di policy di tipo periodico che insistono su asset di quarto livello conduttore o fune di guardia e che generano ordini di sostituzione.

### Configurazione delle policy

Gli asset, in anagrafica, sono caratterizzati da un’unica classificazione e sono associati tramite attributo all’ “elemento EAM”. L’attributo “elemento EAM” identifica un raggruppamento di classificazioni diverse.

Le policy sono configurate per elemento/classificazione e vengono calcolate per singolo asset. Per recuperare tutti gli asset su cui la policy agisce, a partire dall’elemento, si ricavano le classificazioni e quindi i singoli asset. Il calcolo di ciascuna policy avverrà per singolo asset, attraverso elaborazioni parallele e indipendenti tra loro, salvo nei casi in cui la funzionalità non imponga dei vincoli diversi.

Questo implica che anche tramite GUI tutti gli input selezionabili sulla policy devono fare esclusivamente riferimento all’elemento associato alla policy. Non è possibile, in nessun caso, selezionare come input un qualsiasi oggetto che non sia associato nel proprio catalogo allo stesso elemento scelto per la policy o attributi che non siano direttamente associati ad una delle classificazioni che si riferiscono all’elemento della policy.

Nel caso in cui nella policy fosse specificata una condizione che si può verificare solo per una parte degli asset (attributo presente solo per una particolare classificazione) la condizione espressa risulta non rispettata.

Gli input alle policy possono essere singoli o multipli e possono essere combinati logicamente tra loro attraverso operatori semplici (&, OR, diverso e uguale).

Ogni policy sarà costituita da un insieme di formule identificate da:

Variabile

Tipologia variabile

Funzione

Valore di comparazione

A. Una variabile di una formula che costituisce una policy, potrà essere scelta solo all’interno di questa lista di entità

On condition

Indicatori di condizione tecnica calcolati sull’asset su cui si calcola la policy;

Attributi anagrafici (specifiche tecniche e campi di testata) calcolati sull’asset su cui si calcola la policy;

Attività PdL (letti come ordini in stato APERTO, SCHEDULABILE, SCHEDULATO, INOLTRATO, AVVIATO, DA VALIDARE sull’asset associato alla policy) aperti sull’asset su cui si calcola la policy;

Difformità rilevate in stato VALIDATA sull’asset su cui si calcola la policy;

Interferenze rilevate in stato VALIDATA sull’asset su cui si calcola la policy

Misure/variabili di stato/ calcolate sull’asset su cui si calcola la policy.

Periodiche

Indicatori di condizione tecnica calcolati sull’asset su cui si calcola la policy;

Attributi anagrafici (specifiche tecniche e campi di testata) aperti sull’asset su cui si calcola la policy;

Variabili di stato calcolate sull’asset su cui si calcola la policy.

Attività PdL (letti come ordini in stato APERTO, SCHEDULABILE, SCHEDULATO, INOLTRATO, AVVIATO, DA VALIDARE sull’asset associato alla policy)

Per le policy di tipo periodico, inoltre, a sistema è presente un controllo che non permette all’utente di associare come output alla policy macro-attività che fanno riferimento ad attività di tipo “Sostituzione”.

B. Le tipologie di variabile saranno compresi in questa lista:

DIFFORMITA: tipologia che indica che la condizione è espressa su difformità

INTERFERENZA: tipologia che indica che la condizione è espressa su una interferenza

HI: tipologia che indica che la condizione è espressa su un health index

TASSOGUASTO: tipologia che indica che la condizione è espressa su un tasso di guasto

TASSOANOMALIA: tipologia che indica che la condizione è espressa su un tasso di anomalia

ATTIVITA: tipologia che indica che la condizione è espressa su una attività

ATTRIBUTO: tipologia che indica che la condizione è espressa su un attributo dell’asset.

VARIABILE: tipologia che indica che la condizione è espressa su una varabile da comparare con valori numerici espressi nella condizione oppure da una soglia modello il cui valore sarà da ricercare nel catalogo modelli-target.

C. Le funzioni saranno strettamente le seguenti:

| Lista C, funzioni | Dettaglio |
| --- | --- |
| DISPARI | Restituisce true se il valore numerico indicato dalla variabile è dispari altrimenti false |
| PARI | Restituisce true se il valore numerico indicato dalla variabile è pari altrimenti false |
| NOT EXIST | Funzione “non esiste”, ritorna true se l’operando su cui è applicato è null |
| EXIST | Funzione “esiste”, ritorna true se l’operando su cui è applicato è diverso da null |
| < | Minore |
| <= | Minore uguale |
| > | Maggiore |
| >= | Maggiore uguale |
| = | Uguale |
| != | Diverso |

Valore di comparazione: il valore di comparazione potrà essere:

un valore numerico

un valore alfanumerico

un modello soglia: ossia una soglia che è definita sulla base del modello dell’asset a cui è applicato. Ogni modello avrà un cluster che lo contraddistinguerà e permetterà di aggregare le caratteristiche del modello. Quindi il valore della soglia sarà infatti attraverso il match esatto del valore degli attributi del particolare asset. La gestione delle soglie non prevede un valore soglia di default. Il DSS non gestirà il caso in cui i valori degli attributi dell’asset che permettono di scegliere la soglia non siano compresi nel dominio configurato. In questo caso il DSS non proseguirà con il calcolo andando in errore per il singolo asset.

Nell’ambito della stessa policy ogni formula sarà correlabile con le altre formule che costituiscono la policy tramite strettamente i seguenti operatori:

Operatore uguale (=)

Operatore diverso (!=)

Operatore booleano AND

Operatore booleano OR

### Consistenza Asset su cui si basa il calcolo della policy

Il DSS calcolerà nell’algoritmo standard, in modo indipendente e in parallelo, ogni singola policy sul singolo asset. Per farlo, il motore estrarrà tutte le info utili alla valutazione della policy strettamente sull’asset oggetto di calcolo. Nel caso particolare di algoritmi come trainante/trainato, tronco di ispezione e policy aggregate, il calcolo prenderà in considerazione, durante parte dell’algoritmo, più asset dello stesso tipo.

### Sequenza di calcolo delle policy

Il DSS segue una logica di calcolo asincrona. In questo modo il sistema garantisce l’elaborazione del calcolo rispettando la sequenza temporale di innesco di calcolo. In particolare, richieste di calcolo sugli stessi asset vengono accodati sulla base dell’ordine temporale e in base alla chiave identificata come l’asset padre della gerarchia. Nel caso stazioni corrisponderà all’asset padre stazione, nel caso delle linee la chiave corrisponderà all’asset linea o alla concatenazione linee nel caso della doppia terna.

Questo meccanismo mitiga la non concorrenza di eventi di modifica che potrebbero innescarsi sullo stesso asset.

# Assunzioni principali

Il calcolo dell’HI, Tasso di Guasto, Tasso di difettosità avverrà su base periodica. Solo al completamento del calcolo degli HI sarà possibile innescare eventi di calcolo/verifica policy interessati da HI.

Il DSS dovrà considerare nel run solamente le difformità in stato VALIDATA e le attività in stato “Aperto”, “Avviato”, “Da Validare”, “Schedulabile”, “Schedulato”, “Inoltrato”.

Nel caso fosse stato creato, in run precedenti, un ordine associato allo stesso asset e caratterizzato dalla stessa attività e policy nel caso in cui l’ordine già creato si trovasse in stato funzionalmente precedente a “Schedulato” il sistema non creerà un nuovo ordine ma provvederà a modificare quello esistente (nel caso di riattivazione dopo una modifica della policy stessa, come dettagliato nel seguito del documento). Nel caso l’ordine già associato all’asset fosse in stato uguale o successivo a “Schedulato” il DSS provvederà a creare un nuovo ordine associato all’asset.

Una policy periodica non può essere di tipo Sostituzione.

In fase di creazione OdL verranno associati per ogni ordine strettamente i riferimenti all’asset su cui è stato aperto. Non saranno riportati riferimenti anagrafici relativi ad asset correlati. In particolare, in fase di creazione ordine per un asset campata/tratta cavo non saranno riportati i riferimenti dei sostegni o dell’estremo cavo. L’utente per associare una difformità ai sostegni associati alla campata dovrà ricercare da GUI Maximo.

Sono descritti ed elencati tutti gli use case di innesco del DSS, tutti gli algoritmi e feature supportate. il documento ha anche lo scopo di descrivere casi in cui modifiche ad EAM non innescano direttamente calcoli e creazioni di ordini di lavoro creati dal DSS.

L’operatore da GUI potrà validare e modificare gli ordini di lavoro creati dal DSS ma non triggerare la scelta dell’algoritmo da usare per la realizzazione degli ordini che rimane una feature interna del sistema.

Le policy dovranno essere configurate da GUI o tramite strumenti di caricamento iniziale tipo MXloader che verrà rilasciato con il setup del DSS. Il DSS permetterà di valutare le policy solo se soddisferanno le condizioni riportate al paragrafo 3.1.1

# Elaborazioni delle policy

Le policy, una volta triggerate, vengono valutate attraverso sette principali casistiche di elaborazione, ognuna delle quali prevede una serie di passaggi logici, per arrivare alla generazione dell’ordine/degli ordini su uno specifico asset presente a sistema. Nel seguito del capitolo sono descritti nel dettaglio i sette schemi di elaborazione utilizzati dal DSS per valutare le policy di diversa tipologia.

Le elaborazioni possono essere scatenate da diversi eventi che si possono presentare a sistema e che sono descritti nel dettaglio nel capitolo 6.

È possibile che alcune elaborazioni delle policy non siano portate a termine perché il DSS rileva degli errori di configurazione durante l’esecuzione degli algoritmi. Sulla base del tipo di errore che il sistema intercetta sarà possibile che l’elaborazione/calcolo della singola policy venga interrotta in fase di preparazione dei calcoli (in questo caso non vengono effettuati calcoli per nessun asset e quindi non creato nessun ordine) oppure che l’elaborazione rilevi per singolo asset un’anomalia (in questo caso il calcolo per il singolo asset non sarà completato e quindi non generati ordini per i soli asset affetti dall’errore). Per monitorare le elaborazioni del DSS

, per il quale viene generato un codice univoco

Gli errori che si possono presentare sono contenuti in una lista chiusa e sono elencati di seguito:

Errore durante l’inserimento dell’ordine;

Errore durante il check della policy (esempio calcolo di policy in cui il target di raffronto è una soglia non configurata correttamente);

Errore durante la selezione dell’attività a partire dalla macro-attività;

Errore durante la gestione della prevalenza per attività;

Altro tipo di errore;

Non è prevista una ri-elaborazione parziale degli asset andati in errore associati al run della policy.

Di seguito è riportato a fini di esempio un elenco di alcune cause di fallimento (la lista è esaustiva e prevede errori generici applicativi e sistemistici) delle elaborazioni del DSS relative a incongruenze di dati:

La policy non è configurata correttamente o la combinazione logica degli input non ne permette la valutazione;

I target di raffronto definiti per la policy non corrispondono ai valori attesi per l’entità indicata in input;

L’attributo definito in input alla policy non è popolato per l’asset in elaborazione;

La combinazione tra stato e tipologia dell’asset rilevata in anagrafica non è codificata nella tabella “Tabella di corrispondenza Asset – Stato idoneo” o nella tabella di eccezioni;

Per le policy che insistono sul terzo livello, se l’attributo LIN_IDONEITA non è popolato sull’asset in elaborazione.

Il DSS dopo aver effettuato le verifiche delle policy e prodotti gli ordini non prevede un meccanismo di cancellazione ordini creati e salvati a database. Rimane inteso che è possibile provvedere alla correzione degli ordini prodotti tramite GUI (laddove possibile). È inoltre possibile provvedere alla correzione delle condizioni di elaborazione della policy tramite GUI

## Elaborazione policy on condition – standard

Le policy on condition – standard sono policy caratterizzate dalla tipologia corrispondente a “on condition” e dall’assenza di altre logiche di elaborazione. In particolare:

non devono essere riferite a tronchi di ispezione;

non devono essere associate ad asset di tipo conduttore o fune di guardia (quarto livello) con attività di sostituzione in output.

non sono etichettate come policy aggregate

Di seguito la schematizzazione della sequenza di step logici dell’algoritmo eseguiti dal DSS quando si avvia l’elaborazione:

Il DSS, per singolo asset in modo indipendente, valuta gli input della policy singolarmente confrontandoli con gli eventuali target di raffronto. Dopo aver recuperato la consistenza strettamente dell’asset su cui si calcola la policy. Viene quindi valutata la policy per ogni singolo asset in modo indipendente dalle comparazioni condotte su altri asset.

Per essere soddisfatti gli input alla policy devono essere confrontati con il target di raffronto indicato per ciascuno e devono essere soddisfatte le condizioni di generazione determinate dall’operatore di confronto scelto (cfr. paragrafo 3.1.1). Ad esempio, se in input è presente una difformità in stato validata con target di raffronto gravità “Lieve” e operatore “uguale” l’input è soddisfatto solo se sull’asset in elaborazione è registrata una difformità con gravità uguale a “Lieve”.

Se gli input indicati nella policy sono soddisfatti, il DSS sulla base della macro-attività indicata nella policy, seleziona la relativa attività (PdL) da associare all’ordine di lavoro (OdL) in funzione delle specifiche dell’asset che sono espresse su base prevalenza per metodo. L’informazione viene rilevata dal catalogo attività, dove è presente l’attributo e il relativo valore che il DSS deve valutare sull’asset per scegliere quale PdL selezionare tra quelli che fanno riferimento alla macro-attività in output alla policy.

In particolare, il DSS verifica se vi sono delle regole di prevalenza per metodo espresse mediante attributi che dovranno essere presenti strettamente allo stesso livello dell’asset su cui sta valutando la policy. Nel caso siano presenti, l’algoritmo verifica le condizioni comparando il valore indicato nel catalogo attività per la scelta metodo con i valori dell’attributo relativo all’asset oggetto di calcolo. Nel caso in cui le condizioni siano espresse mediante attributi su un livello gerarchico differente il DSS non potrà effettuare valutazione.

Nel caso in cui non sono espresse logiche di prevalenza per metodo per attributo il DSS sceglierà la prima attività di default. Si assume che vi sia sempre una sola attività di default associata alla macro-attività, nel caso in cui ve ne fossero più di una, viene scelta la prima per soddisfare il requisito.

Affinché avvenga la generazione dell’ordine, il DSS deve valutare l’idoneità ed eccezioni dello stato dell’asset rispetto al PdL scelto. Per capire se un asset ha uno stato idoneo per l’attività che si intende effettuare bisogna distinguere tra 2 casistiche, in funzione del livello della gerarchia dell’asset. Oltre a valutare l’idoneità dell’asset sulla base del proprio stato o delle logiche spiegate nel seguito, il sistema per i secondi e i primi livelli deve anche valutare lo stato degli asset sottostanti. In particolare, se sotto un primo o un secondo livello non è presente almeno un asset di livello inferiore in stato idoneo, il DSS lo escluderà dalle elaborazioni.

Nel caso in cui la policy punti ad un terzo livello, allora l’algoritmo dovrà verificare la idoneità attraverso un check delle caratteristiche dell’asset.

Qualora si tratti di una tipologia asset che punta ad un terzo livello di Linee (aeree\cavo) allora il valore da cercare sarà la caratteristica LIN_IDONEITA

Qualora si tratti di una tipologia asset che punta ad un terzo livello di Stazioni, allora il valore da cercare sarà la caratteristica LIN_IDONEITA_SPCC o LIN_IDONEITA_UF.

Nota: queste 3 caratteristiche vengono valorizzate da EAM in automatico:

Infatti, nel caso particolare di asset di un terzo livello (asset per il quale non è definito uno stato), per la generazione dello stato il sistema effettua un check sullo stato di anagrafica dei quarti livelli sottesi. Se almeno uno di questi si trova in uno stato idoneo (informazione ricavata dalla “tabella di corrispondenza Asset – Stato idoneo”). La valorizzazione dell’idoneità o non idoneità dei terzi livelli (usata poi dal DSS per la generazione di OdL) sugli stessi verrà gestita come di seguito descritto:

Linee: sia per le linee aeree che per quelle in cavo sarà presente un attributo “LIN_IDONEITA”, per ogni terzo livello riportato nelle tabelle sottostanti, che potrà assumere valore “IDONEO” o “NON IDONEO” secondo regole descritte successivamente nel corso del paragrafo.

Stazioni: saranno presenti gli attributi “LIN_IDONEITA_UF” e “LIN_IDONEITA_SPCC”, per ogni terzo livello riportato nelle tabelle sottostanti, che potranno assumere i valori “IDONEO” o “NON IDONEO” secondo regole descritte successivamente nel corso del paragrafo.

Il DSS se la policy è stata soddisfatta genererà OdL sul terzo livello solo se l’attributo

risulta valorizzato con “IDONEO”.

Le regole per la generazione di OdL su asset di terzo livello saranno gestite all’interno dell’applicazione “Gestione stati - attività” nel tab “Regole terzi livelli”. Di seguito il dettaglio delle tabelle:

Regole terzi livelli linee: all’interno di questa tabella sono presenti le seguenti colonne:

Terzo livello: campo popolabile manualmente dall’utente tramite una lista valori contenente le classificazioni di terzo livello linee aeree e in cavo

Quarto livello: campo popolabile manualmente dall’utente tramite una lista valori contenente le classificazioni di quarto livello sottostanti al terzo livello precedentemente selezionato

| Terzo livello | Quarto livello |
| --- | --- |
| CONDUTTORI | CONDUTTORE |
| TELAIO | STRUTTURA |
| ATTACCO CONDUTTORE | ARMAMENTO CONDUTTORE |
| TERMINALI | TERMINALE |
| FUNI DI GUARDIA | FUNE DI GUARDIA |
| CAVI | CAVO DI POTENZA |
| GIUNTI | GIUNTO CAVO |

La logica di lettura della tabella è la seguente: se almeno un quarto livello si trova in uno stato idoneo (ricavato dalla “tabella di corrispondenza Asset – Stato idoneo”) allora l’attributo “LIN_IDONEITA” assume il valore “IDONEO”. In caso contrario assumerà un valore “NON IDONEO”.

Esempio sul CONDUTTORI di terzo livello: se esiste almeno un CONDUTTORE di 4° livello in uno stato idoneo, allora l’attributo “LIN_IDONEITA” del CONDUTTORI (3° livello) viene valorizzato con “IDONEO”. A questo punto il DSS potrà generare l’attività riportata nella policy.

Regole terzi livelli stazioni: sono presenti due tabelle (una per gestire la casistica SPCC e l’altra per la casistica UF) con le seguenti colonne:

Terzo livello: campo compilabile manualmente dall’utente tramite una lista valori contenente le classificazioni di terzo livello stazioni

Quarto livello: campo compilabile manualmente dall’utente tramite una lista valori contenente le classificazioni di quarto livello sottostanti al terzo livello precedentemente selezionato

| SPCC | SPCC |
| --- | --- |
| Terzo Livello | Quarto Livello |
| STALLO | PROTEZIONE DI MONTANTE |
| STALLO | SPCC DI MONTANTE |
| SBARRA | PROTEZIONE DI SEZIONE |
| SBARRA | SPCC DI SEZIONE |
| TRASFORMATORE | PROTEZIONE DI MACCHINA |
| TRASFORMATORE | SPCC DI MACCHINA |
| BOOSTER | PROTEZIONE DI MACCHINA |
| BOOSTER | SPCC DI MACCHINA |
| BATTERIA CONDENSATORI | PROTEZIONE DI CONDENSATORE |
| BATTERIA CONDENSATORI | SPCC DI CONDENSATORE |
| REATTORE | PROTEZIONE DI MACCHINA |
| REATTORE | SPCC DI MACCHINA |
| SISTEMA DI CONTROLLO E AUTOMAZIONE | SPCC DI IMPIANTO |

| UF | UF |
| --- | --- |
| Terzo livello | Quarto livello |
| STALLO | INTERRUTTORE AIS |
| STALLO | SEZIONATORE AIS |
| STALLO | TA AIS |
| STALLO | TV AIS |
| STALLO | SCARICATORE AIS |
| STALLO | CALATE E COLLEGAMENTI |
| STALLO | PASSANTE AIS |
| STALLO | INTERRUTTORE GIS |
| STALLO | SEZIONATORE GIS |
| STALLO | TA GIS |
| STALLO | TV GIS |
| STALLO | SCARICATORE GIS |
| STALLO | PASSANTE GIS |
| STALLO | RESISTORE |
| STALLO | INDUTTORE |
| STALLO | CAPACITORE |
| SBARRA | SBARRA AIS |
| SBARRA | SBARRA GIS |
| SBARRA | SEZIONATORE AIS |
| SBARRA | TV AIS |
| SBARRA | SCARICATORE AIS |
| SBARRA | SEZIONATORE GIS |
| SBARRA | TV GIS |
| SBARRA | SCARICATORE GIS |
| TRASFORMATORE | MACCHINA |
| TRASFORMATORE | VARIATORE |
| TRASFORMATORE | SISTEMA DI RAFFREDDAMENTO |
| TRASFORMATORE | ISOLATORE PASSANTE |
| BOOSTER | MACCHINA |
| BOOSTER | VARIATORE |
| BOOSTER | SISTEMA DI RAFFREDDAMENTO |
| BOOSTER | ISOLATORE PASSANTE |
| BATTERIA CONDENSATORI | CONDENSATORE |
| BATTERIA CONDENSATORI | TA AIS |
| REATTORE | MACCHINA |
| REATTORE | VARIATORE |
| REATTORE | SISTEMA DI RAFFREDDAMENTO |
| REATTORE | ISOLATORE PASSANTE |

La logica di lettura delle tabelle è la seguente:

Caso SPCC: se almeno un quarto livello si trova in uno stato idoneo (ricavato dalla “tabella di corrispondenza Asset – Stato idoneo”) allora la caratteristica “LIN_IDONEITA_SPCC” assume il valore “IDONEO”. In caso contrario assumerà un valore “NON IDONEO”.

Caso UF: se almeno un quarto livello si trova in uno stato idoneo (ricavato dalla “tabella di corrispondenza Asset – Stato idoneo”) allora la caratteristica “LIN_IDONEITA_UF” assume il valore “IDONEO”. In caso contrario assumerà un valore “NON IDONEO”.

Esempio sullo STALLO (caso SPCC) di terzo livello: se esiste almeno una PROTEZIONE DI MONTANTE o un SPCC MONTANTE in uno stato idoneo, allora l’attributo “LIN_IDONEITA_SPCC” dello STALLO viene valorizzato con “IDONEO”. A questo punto il DSS potrà generare l’attività riportata nella policy.

Esempio sullo STALLO (caso UF) di terzo livello: se esiste almeno un TV AIS o uno SCARICATORE AIS o un CALATE E COLLEGAMENTI (e così via per ogni quarto livello riportato in tabella) in uno stato idoneo, allora l’attributo “LIN_IDONEITA_UF” dello STALLO viene valorizzato con “IDONEO”. A questo punto il DSS potrà generare l’attività riportata nella policy.

Per identificare l’attributo da leggere (tra LIN_IDONEITA_UF e LIN_IDONEITA_SPCC) il DSS verifica sul PdL scelto per l’ordine da creare il valore del campo UF/SPCC (flag) che indica quale dei due attributi di idoneità vale per quello specifico PdL.

Si specifica che, la logica sopra descritta applicata dal DSS per ordini che puntano ad un terzo livello, non è applicata dal sistema alla creazione manuale tramite GUI (o tramite altri strumenti diversi dal DSS) di ordini di lavoro associati a terzi livelli.

Nel caso in cui la policy non punti ad un terzo livello, allora l’algoritmo dovrà verificare nell’ordine le eccezioni e l’idoneità attraverso un check delle caratteristiche dell’asset.

Il check sulla presenza di eccezioni viene effettuato interrogando la tabella eccezioni per attività legate all’ordine e tipologia di asset su cui si sta effettuando il calcolo.

Dalla tabella eccezioni viene recuperato l’informazione STATO_IDONEO se presente la combinazione passata in input confrontiamo il valore dello stato dell’asset con il valore trovato nella tabella:

Se corrispondono l’ordine potrà essere generato.

Se non corrispondono (lo stato dell’asset con lo stato restituito dalla tabella eccezione) oppure se non saranno presenti combinazioni sulla tabella eccezioni sarà necessario effettuare un ulteriore check controllare la tabella delle idoneità.

| Tabella eccezioni | Tabella eccezioni | Tabella eccezioni |  |
| --- | --- | --- | --- |
| ID PdL | Descrizione PdL | Stato idoneo | Tipologia |
| AT.L.1.0.0.4.1 | Ispezione eliportata IR (ST) | ATTV | CAMPATA AEREA |
| AT.L.1.0.0.9.1 | Controllo strumentale IR da terra (OA1) | ATTV | CAMPATA AEREA |
| AT.L.1.0.0.10.1 | Controllo con telecamera a raggi UV (OA2) | ATTV | CAMPATA AEREA |
| AT.L.4.1.0.2.1 | Ispezione strumentale IR terminali cavo (SD) | ATTV | TERMINALI |
| AT.L.4.1.0.3.1 | Controllo strumentale sui terminali cavo (OD) | ATTV | TERMINALI |
| AT.S.1.0.0.4.1 | Controlli tecnici di tipo A - Thermovision | ATTV |  |
| AT.S.2.0.0.5.1 | Controlli tecnici di tipo A - Thermovision | ATTV |  |
| AT.S.3.0.0.5.1 | Controlli tecnici di tipo A - Thermovision | ATTV |  |
| AT.S.7.0.0.7.1 | Controlli tecnici di tipo A - Thermovision | ATTV |  |

Dalla tabella delle idoneità sarà recuperato lo stato confrontato con quello dell’asset. La configurazione della tabella delle idoneità è riportata in allegato.

Solo se lo stato corrisponde potrà essere creato l’ordine, il flusso logico potrà continuare, alternativamente ODL non sarà creato.

Nel caso di idoneità dell’asset, il DSS procede con la valutazione della presenza di altri ordini (OdL) con stessa policy e PdL in stato:

Da validare

Aperto

Schedulabile

Schedulato

Avviato

In caso fosse già presente un ordine in uno qualsiasi degli stati elencati prima che è stato generato dalla stessa policy, il DSS non genera l’ordine in output alla policy.

Si valuta la prevalenza per l’ordine generato. All’interno del catalogo delle Attività, per ogni PdL, possono essere definite n attività prevalenti. Sulla base delle associazioni di prevalenza presenti nel catalogo attività per il PdL scelto sull’ordine, il DSS verificherà se a sistema sono già presenti ordini con PdL prevalente o prevalso rispetto a quello da generare sull’asset in questione. Oltre alla verifica di ordini con PdL prevalenti, viene verificata la presenza di altri ordini con stesso PdL di quello associato all’ordine in ingresso sul sistema. Di seguito sono esplicitati i meccanismi di elaborazione della logica di prevalenza applicati esclusivamente se è censita a catalogo almeno una relazione di prevalenza sull’attività associata all’ordine in ingresso sul sistema.

Il DSS per ogni ordine che è candidato ad essere inserito sulla piattaforma, a valle delle logiche applicate ai punti precedenti, valuta se prevalente o prevalso rispetto ad ordini già associati all’asset e presenti in consistenza. Per farlo considera nella sequenza riportata i casi in cui:

Il singolo ordine in ingresso è prevalso da ordini in consistenza;

Il singolo ordine in ingresso prevale su ordini in consistenza;

Il caso particolare in cui il singolo ordine in ingresso prevale o è prevalso da ordini associati alla stessa attività in consistenza.

Per ogni ordine candidato ad essere inserito e per gli ordini relazionati ai punti 1, 2 e 3, il DSS, assegna progressivamente un flag prevalso/prevalente. L’assegnazione del flag per l’applicazione delle logiche al punto 1, 2 e 3 è legata alla presenza delle regole di prevalenza indicate nel catalogo attività e alla consistenza degli ordini associati all’asset sul sistema.

Questa modalità di elaborazione permette di prendere in considerazione sempre tutti gli ordini presenti a sistema (sia prevalsi che prevalenti) e applicare le logiche per le due casistiche (prevalente o prevalso) separatamente in modo sequenziale.

La valutazione viene effettuata nella sequenza di seguito indicata:

Ricercando tutti gli ordini prevalenti rispetto a quello candidato alla generazione ed assegnando il flag “Prevalso” all’ordine nuovo (“Prevalente” agli ordini già a sistema). Applicando, successivamente, le logiche di aggiornamento ordini riportate nella tabella (*)

Ricercando tutti gli ordini prevalsi rispetto a quello candidato alla generazione assegnando il flag “Prevalente” all’ordine nuovo (“Prevalso” agli ordini già a sistema). Applicando, successivamente, le logiche di aggiornamento ordini riportate nella tabella (*)

Ricercando gli ordini che hanno la stessa attività associata all’ordine candidato alla generazione e sulla base della data modello più bassa associa rispettivamente l’etichetta prevalente e prevalso. Applicando, successivamente, le logiche di aggiornamento ordini riportate nella tabella (**).

(*) Di seguito una rappresentazione grafica delle logiche aggiornamento ordini per prevalenza:

(**) Di seguito una rappresentazione grafica delle logiche di aggiornamento ordini per auto-prevalenza:

Riportiamo alcuni scenari che possono presentarsi a sistema per rappresentare concretamente come vengono applicate le logiche sopra descritte:

Assumiamo che l’ordine in fase di creazione sia l’ordine 4 ed associato allo stesso asset siano già presenti in consistenza gli ordini: Ordine1, Ordine 2, Ordine 3, Ordine 5.

Esempio scenario 1: A sistema vengono rilevati esclusivamente ordini prevalsi (uno o più di uno) dall’ordine in inserimento. All’ordine in fase di creazione viene assegnato il flag “Prevalente”, all’ordine o agli ordini già aperti viene assegnato il flag “Prevalso”.

Sulla base della data modello si presentano due casistiche:

Se la data modello dell’ordine 4 (prevalente) è minore o uguale della data modello dell’ordine 1 prevalso, lo stato dell’ordine 1 viene impostato a “Non eseguito”, gli viene associato nel campo ordine prevalente il codice del nuovo ordine, viene indicata la dicitura “OdL prevalso” nel campo note.

Se la data modello dell’ordine 4 (prevalente) è maggiore della data modello dell’ordine 1 prevalso, lo stato dell’ordine 1 viene mantenuto com’è, gli viene associato nel campo ordine prevalente il codice del nuovo ordine, viene indicata la dicitura “OdL prevalso” nel campo note.

Esempio scenario 2: a sistema vengono rilevati sia ordini prevalsi che prevalenti (uno o più di uno). In questo caso è importante l’ordine con cui viene assegnato il flag:

Dopo la 1° assegnazione, l’ordine 4 secondo le logiche sulla data modello dovrebbe essere posto in stato “Non eseguito”, l’ordine 2 rimane nello stato in cui si trova. La seconda assegnazione però è quella decisiva quindi, in conclusione si ha:

Ordine 4 prevalente rispetto all’1 quindi viene generato in stato Aperto o Da validare, con l’indicazione nel campo “OdL Prevalente” del codice dell’ordine 2 e nel campo note la dicitura “OdL prevalso”.

Ordine 2 rimane nello stato in cui si trovava.

Ordine 1 viene portato in stato non eseguito se la data modello dell’ordine 4 è minore altrimenti rimane nello stato in cui si trovava. Gli viene comunque associato nel campo OdL prevalente il codice dell’ordine 4 e nel campo note la dicitura “OdL prevalso”.

Esempio scenario 3: a sistema vengono rilevati ordini prevalsi, ordini prevalenti e un ordine con stesso PdL a cui si applica l’auto-prevalenza, con data modello maggiore di quello generato (che risulta quindi prevalente rispetto a quello a sistema).

Anche in questo caso, come nello scenario 2, dopo aver effettuato tutte le valutazioni l’assegnazione definitiva è l’ultima (3°):

Ordine 4 prevalente rispetto al 3 quindi viene generato in stato Aperto o Da validare, con l’indicazione nel campo “OdL Prevalente” del codice dell’ordine 2 e nel campo note la dicitura “OdL prevalso”.

Ordine 2 rimane nello stato in cui si trovava.

Ordine 1 viene portato in stato non eseguito se la data modello dell’ordine 4 è minore altrimenti rimane nello stato in cui si trovava. Gli viene comunque associato nel campo OdL prevalente il codice dell’ordine 4 e nel campo note la dicitura “OdL prevalso”.

Ordine 3 (con stesso PdL del 4) è prevalso dall’ordine 4 avendo una data modello maggiore. Viene quindi posto in stato non eseguito, gli viene associato nel campo OdL prevalente il codice dell’ordine 4 e nel campo note la dicitura “OdL prevalso”.

Esempio scenario 4: a sistema vengono rilevati ordini prevalsi, ordini prevalenti e un ordine con stesso PdL a cui si applica l’auto-prevalenza, con data modello minore o uguale di quello generato (che risulta quindi prevalso rispetto a quello a sistema).

In questo caso l’ultima assegnazione rappresenta quella definitiva:

Ordine 4 prevalso dall’ordine 3 quindi viene generato in stato. Non eseguito, con l’indicazione nel campo “OdL Prevalente” del codice dell’ordine 3 e nel campo note la dicitura “OdL prevalso”.

Ordine 2 rimane nello stato in cui si trovava.

Ordine 1 viene portato in stato non eseguito se la data modello dell’ordine 4 è minore altrimenti rimane nello stato in cui si trovava. Gli viene comunque associato nel campo OdL prevalente il codice dell’ordine 4 e nel campo note la dicitura “OdL prevalso”.

Ordine 3 (con stesso PdL del 4) è prevalente sull’ordine 4 avendo una data modello minore o uguale. Rimane quindi nello stato in cui si trovava.

In questo caso, anche se l’ordine 4 risulta in stato non eseguito, la prevalenza sull’ordine 1 è coperta dall’ordine 3 che ha stesso PdL del 4 e rimane nello stato in cui si trova.

Si specifica che le logiche di prevalenza per attività sono applicate sulla base del tipo asset:

Per gli asset di tipologia linee sarà gestita la prevalenza per attività:

per ordini/attività associate allo stesso asset

per ordini/attività associate ad asset di livello gerarchico terzo e quarto livello. In dettaglio sarà valutata per ordini/attività associate ad asset padre (terzo livello) se si stà creando un ordine su asset di quarto livello. Per ordini/attività associate ad asset figli (quarto livello) se si stà creando un ordine su asset di terzo livello.

Non sarà prevista la gestione della prevalenza tra asset di livelli gerarchici differenti.

Per gli asset di tipologia stazioni sarà gestita la prevalenza per attività:

per ordini/attività associate allo stesso asset

per ordini/attività associate ad asset di livello gerarchico quarto livello su altri asset di quarto livello con lo stesso asset parent.

Non sarà prevista la gestione della prevalenza tra asset di livelli gerarchici differenti.

Il meccanismo di prevalenza è applicato anche dal sistema all’apertura di attività manuali da parte dell’utente tramite GUI (o da altri strumenti diversi dal DSS) esclusivamente per attività che fanno riferimento allo stesso asset. Questa logica applicata dalla GUI è slegata da quella applicata sul DSS.

Nel caso delle stazioni l’ordine prima dell’inserimento viene arricchito con i task associati all’ordine / micro-controlli. La funzionalità ha lo scopo di associare all’ordine:

Tutti i micro-controlli per i quali non sono espresse delle condizioni di applicabilità. In questo caso saranno associati all’ordine senza effettuando esclusivamente una verifica sullo stato dell’asset a cui devono essere associati. In particolare, il sistema verifica che lo stato dell’asset a cui fa riferimento il micro-controllo sia idoneo (esclusivamente secondo la corrispondenza indicata nella “tabella di corrispondenza asset-stato idoneo” senza applicare altre logiche) per la generazione.

Tutti i micro-controlli per i quali sono espresse delle condizioni di applicabilità e che sulla base di attributi dell’asset in fase di calcolo e relativi valori (indicati sul catalogo micro-controlli) sono soddisfatte e lo stato dell’asset è idoneo alla generazione. In particolare, il sistema verifica che lo stato dell’asset a cui fa riferimento il micro-controllo sia idoneo (esclusivamente secondo la corrispondenza indicata nella “tabella di corrispondenza asset-stato idoneo” senza applicare altre logiche) per la generazione.

Per verificare il punto b sarà necessario fare riferimento al catalogo micro-controlli. In

questo caso per ogni task sarà verificato se l’asset associato soddisfa le condizioni

definite per il singolo task. Solo se soddisfatte le condizioni sarà effettuato l’inserimento

sull’ordine.

L’associazione automatica di micro-controlli a ordini generati manualmente tramite GUI (o tramite altri strumenti diversi dal DSS) non è prevista.

Inserimento dell’ordine sulla base dati e di tutti gli ordini correlati in modo da garantire la transazionalità delle operazioni. Per ordini correlati si intendono tutti gli ordini che sono aggiunti o modificati durante l’implementazione dell’algoritmo e legati all’ordine in output alla policy.

Di seguito la schematizzazione dei campi valorizzati sull’ordine dal DSS:

L’ordine generato sarà creato in stato “Da Validare”. I campi calcolati dal DSS al momento della generazione dell’OdL on condition seguiranno queste logiche:

Data modello DSS: legata all’eleggibilità della policy e descritta successivamente nel paragrafo.

Data pianificata fine: stesso valore della Data modello.

Data pianificata inizio: Data pianificata fine – durata pianificata.

Data di max posticipo: Data modello + Massimo posticipo attività.

Data non prima di: Data modello – Massimo anticipo attività.

Premessa: un asset è eleggibile quando è soggetto ad un intervento di sostituzione prevedibile e pianificabile triggerato da una policy di eleggibilità. Le policy di eleggibilità, presenti nel catalogo delle policy, fanno riferimento a delle soglie degli indicatori di condizione tecnica (HI) oltre le quali l’asset è identificato eleggibile e per cui viene prescritta una determinata data di sostituzione.

Per identificare una policy come eleggibile deve essere stata configurata a GUI con il flag eleggibilità abilitato oppure devono essere soddisfatte 2 condizioni:

la policy contiene almeno un parametro in input di tipo HI.

la macro-attività innescata dalla policy è di tipo “sostituzione” (caratterizzata dalla tipologia attività “sostituzione”).

In base alla condizione

Se l’asset non è eleggibile e non sono presenti difformità/interferenza in input alla policy, la data modello = differibilità + data generazione ODL (sysdate)

Se l’asset non è eleggibile e sono presenti difformità/interferenza in input alla policy allora vengono ricercati nella consistenza asset nella mappa difformità con stato VALIDATO o interferenze con stato VALIDATO presenti nella formula della policy e tra queste (e non tra tutte) quella più recente (il valore corrisponde alla valorizzazione della data di prima rilevazione dell’interferenza e della difformità) ed una volta trovata viene sommata la differibilità alla data di rilevamento per valorizzare la data modello. Se la data modello calcolata è nel passato, viene ricalcolata sommando la differibilità alla data attuale.

Se l’asset è eleggibile (secondo la definizione riportata sopra) nella consistenza dell’asset, viene verificato se sono già presenti altri ordini (in particolare tra quelli che hanno fino allo stato “Schedulato” escluso) con la stessa attività associata (sulla base delle constraint precedentemente espressa, dovrebbe essere inserito un solo ordine di questo tipo).

Se non presenti ordini con stessa attività associati all’asset allora sarà impostata la data modello = data ordine + differibilità

Se presenti ordini con stessa attività associati all’asset è necessario comparare il valore della differibilità della policy associato all’ordine già creato in precedenza con la differibilità della policy corrente secondo le seguenti regole:

Se minore (differibilità ordine corrente < differibilità ordine precedente) sarà impostata la data modello = data modello minore tra la data modello corrente (calcolata come data corrente + differibilità corrente) e data modello precedente. In altre parole, viene associata la data modello più prossima.

se maggiore (differibilità ordine corrente > differibilità ordine precedente) sarà impostata la data modello = data generazione (data in cui sto calcolando l’ordine) + differibilità nuova (differibilità della policy in fase di elaborazione) – [anno di calcolo ordine corrente – anno in cui l’asset è diventato eleggibile la prima volta].

Nota importante:

Le differibilità: è espressa secondo l’unità di misura caratteristica della policy e non sono previste conversioni.

Max posticipo e max anticipo sono sempre espressi in giorni.

La durata pianificata campo espresso in ore.

## Elaborazione policy on condition – trainante/trainato

Le policy on condition – trainante/trainato sono policy caratterizzate dalla tipologia corrispondente a “on condition”, associate a tipologia asset conduttore o fune di guardia con attività di sostituzione in output.

L’algoritmo trainante/trainato eredita dall’algoritmo on condition – standard tutte le fasi: valutazione policy, idoneità, calcolo dell’attività, calcolo data modello, prevalenza. La sua peculiarità si espleta dopo l’esecuzione del passo D) e prima del passo E) della prevalenza.

In sostanza il DSS, per ogni asset validato e per il quale non esiste un OdL a parità di asset, attività e policy, inizia a determinare la tratta di tesatura. Se per l’asset analizzato non esiste la tratta di tesatura, il DSS creerà direttamente l’OdL, in pratica si comporta esattamente come l’algoritmo standard.

Se la tratta di tesatura esiste, il DSS provvederà a sincronizzare le esecuzioni degli altri asset che appartengono alla stessa tratta di tesatura. A parità di tratta di tesatura ci possono essere asset che soddisfano la policy ed asset che non soddisfano la policy. In questa circostanza il DSS creerà tanti OdL quanti sono gli asset che appartengono alla tratta di tesatura ed assegnerà un attributo che assume valori “Trainante” o “Trainato”. Tutti gli OdL che soddisfano la policy saranno marcati come trainanti. Possono quindi esistere più OdLTrainante sulla stessa tratta di tesatura.

Di seguito un esempio di creazione OdL per una tratta di tesatura e relativa valorizzazione dell’attributo “Trainante/Trainato”:

Per questo insieme di OdL viene eseguito il passo E) della prevalenza e il passo G) di inserimento OdL in un’unica transazione.

## Elaborazione policy on condition – tronco di ispezione

Le policy on condition – tronco di ispezione sono policy caratterizzate dal flag “Tronco di ispezione” attivo e che insistono su campate aeree o tratte cavo (secondo livello).

L’algoritmo tronco d’ispezione eredita dall’algoritmo on condition – standard tutte le fasi: valutazione policy, idoneità, calcolo dell’attività, calcolo data modello, prevalenza. La sua peculiarità si espleta dopo l’esecuzione del passo D).

In sostanza il DSS, per ogni asset che soddisfa la policy e per il quale non esiste un OdL a parità di asset, attività e policy inizia a determinare il tronco d’ispezione. Se l’asset considerato non ha un tronco di ispezione associato, la sua elaborazione termina con scarto.

Per tutti gli asset che hanno un tronco d’ispezione, il DSS sincronizzerà tutte le elaborazioni e le aggregherà per tronco. Per ogni tronco verrà generato un ordine aggregatore associato alla linea relative alle campate aeree/tratte cavo sulla quale è scattata la policy in cui è indicato anche il SAP-ID del tronco di ispezione. Sotto all’ordine aggregatore vengono creati N ordini associati a tutte le campate aeree/tratte cavo riferite al singolo tronco di ispezione a cui fa riferimento l’OdL aggregatore (padre).

Di seguito un esempio di elaborazione di una policy tronco di ispezione:

Per ogni OdL aggregatore e tutti ordini “figli” viene eseguito il passo E) della prevalenza e al termine il passo G) di inserimento OdL in un’unica transazione.

## Elaborazione policy on condition – aggregate

Le policy aggregate sono definite a sistema e non sono modificabili tramite GUI. In output generano ordini di sostituzione che possono essere aggregati sul primo, terzo livello, quarto livello per le linee, sul terzo e quarto livello per le stazioni. L’elaborazione si basa anche sulla valutazione dei K di eleggibilità di determinati asset, che vengono messi a confronto con i parametri di eleggibilità riferiti allo stesso asset. A seconda del livello su cui sono aggregate, in caso di esito positivo delle valutazioni sui K, il DSS genera N ordini uno per ogni asset della stessa linea riferito alla tipologia elemento associata alla policy.

Le policy aggregate sono identificate attraverso le seguenti condizioni che devono essere rispettate:

Flag policy aggregata impostata a true

Livello aggregazione della policy impostata ad un livello uguale ad 1,2,3

La macro-attività associata alla policy sarà di tipo SOSTITUZIONE.

Le formule dei K di eleggibilità sono cablate a sistema mentre, i parametri di eleggibilità sono gestibili tramite due apposite maschere nell’applicazione del catalogo policy. Le maschere sono accessibili tramite due pulsanti nelle azioni di sinistra dell’applicazione:

S eleggibilità linee;

S eleggibilità stazioni.

Tramite le due maschere è possibile selezionare i target di raffronto delle policy aggregate con cui il DSS deve confrontare il valore calcolato tramite le formule dei K di eleggibilità per ogni asset.

Di seguito i componenti su cui sono previste le policy aggregate:

Linee aeree:

Conduttore (quarto livello)

Fune di guardia (quarto livello)

Strutture (quarto livello)

Stazioni (tutti equipment) + stallo:

Interruttori AIS

Sezionatori AIS

TV AIS

TA AIS

Scaricatore AIS

Stallo

Le logiche di elaborazione delle policy aggregate dipendono dall’asset a cui puntano e dal livello di aggregazione previsto.

Per le policy aggregate su asset di linea è prevista una logica che alla creazione di un nuovo asset di tipo Conduttore, Fune di guardia e struttura se scatta una policy che genera un ordine di sostituzione viene controllato se a sistema già esiste un ordine aggregatore di sostituzione su linea al quale associare come figlio il nuovo ordine di sostituzione.

Per le policy aggregate linee su conduttore, il DSS per ogni linea esegue i seguenti passaggi logici:

Identificati tutti i conduttori per ogni linea. Per ogni asset “conduttori” (terzo livello) si passa ad identificare tutti gli asset “conduttore” associati ai “conduttori”.

Viene calcolato il K eleggibilità conduttore per ogni conduttore (quarto livello) della linea. Il calcolo deve avvenire per il singolo conduttore perché le formule sono parametriche e dipendono da alcune caratteristiche del conduttore stesso.

Quindi per ogni elemento conduttore individuato è necessario calcolare il coefficiente di eleggibilità del conduttore Keleg_cond. Per ogni singolo conduttore è necessario reperire HI di vetustà e l’HI di degrado e calcolare HIvet.+deg..

Di seguito l’algoritmo per il calcolo del coefficiente di eleggibilità del conduttore:

Se HIvet.+deg. >= S1cond. oppure esiste un OdL con attività di sostituzione conduttore con stato "Aperto", "Schedulabile", “Eseguibile”, “inviato”, "Schedulato", "Inoltrato", "Avviato", "Eseguito parzialmente" è Keleg_cond = 1

Se HIvet.+deg. < S1cond. è Keleg_cond = HIvet.+deg. / S1cond.

Il coefficiente S1cond rappresenta il parametro soglia del conduttore, è legato alle caratteristiche dell’asset ed è riportato nel catalogo dei modelli target.

Una volta calcolati i K per il conduttore (quarto livello), il DSS valuterà il coefficiente di eleggibilità del 3° livello “conduttori” come il valore massimo dei coefficienti dei 4° livelli sottostanti:

Keleg_cond = max(Keleg_cond del 4° livello conduttore)

Successivamente viene calcolato il valore del coefficiente di eleggibilità dell’intera tratta di tesatura in cui è compresa ogni singola campata. Per ogni conduttore viene recuperata la tratta di tesatura. Se non si riesce ad individuare la tratta di tesatura il conduttore non darà contributo ai calcoli successivi. Se si individua la tratta di tesatura per ogni conduttore si determina la campata.

il DSS calcola il K per le tratte di tesatura della linea (dove presenti). Per farlo dovrà aspettare la fine del calcolo di tutti i conduttori associati alla linea al fine di avere tutti i dati necessari al calcolo. Per ogni tratta di tesatura, si determina Keleg_tratta_cond nel seguente modo:

Keleg_tratta_cond = (max(Keleg_cond)*LunghTratta)/(LunghLinea)

Dove

Keleg_cond = k eleggibilità del singolo conduttore.

LunghTratta = è la lunghezza della tratta definita come somma delle lunghezze delle campate che la compongono.

LunghLinea = corrisponde alla lunghezza della Linea.

Si passa poi al calcolo del K di eleggibilità per l’intera linea come somma dei K delle tratte. Questo sarà dato dalla somma dei coefficienti di eleggibilità delle singole tratte di tutta la linea:

Keleg_linea_cond = Sommatoria dei Keleg_tratta_cond

Una volta determinato il Keleg_linea_cond è possibile procedere alla definizione delle attività di sostituzione confrontando il coefficiente con un valore soglia Seleg_linea_cond. Il valore Seleg_linea_cond è riportato come valore unico indipendentemente dalle caratteristiche dell’asset.

In base ai valori assunti dal coefficiente di eleggibilità della linea è prevista la seguente logica:

Se Keleg_linea_cond >= Seleg_linea_cond & almeno un Keleg_cond = 1 è generazione di n OdL “Sostituzione conduttore” (3° livello) tanti quanti sono i conduttori di terzo livello sottesi alla linea.

Il coefficiente della linea calcolato viene confrontato con il parametro soglia S eleggibilità della linea conduttore.

Se il K supera la soglia e almeno uno dei K eleggibilità degli asset di quarto livello conduttore sottesi alla linea è uguale a 1, vengono creati N ordini di “sostituzione conduttore” (terzo livello) tanti quanti sono i conduttori di terzo livello sottesi alla linea per la quale sono soddisfatte le condizioni sopra citate. Sarà creato anche un ordine aggregato per singola linea.

Per le policy aggregate linee su fune di guardia, il DSS per ogni linea esegue i seguenti passaggi logici:

Le formule di calcolo delle policy aggregate sono cablate a sistema.

Per prima cosa si calcola il K eleggibilità fune di guardia per ogni fune di guardia (quarto livello) della linea. In particolare, si definisce per il 4° livello Fune di Guardia un parametro, definibile coefficiente di eleggibilità della fune di guardia, di valore variabile tra 0 e 1. Tale coefficiente assume valore pari a 1 nel caso in cui la somma dell’HI di vetustà e l’HI di degrado del 4° livello fdg (fune di guardia) supera il valore della soglia di eleggibilità (i valori delle soglie di eleggibilità sono riportati nel catalogo degli indicatori di condizione tecnica) oppure nel caso sia già presente sul 4° livello fune di guardia un’attività di sostituzione fune di guardia trainante (in stato “Aperto”, “Schedulabile”, “Eseguibile”, “Schedulato”, “Inoltrato”, “Avviato”, “Eseguito parzialmente”). Il valore del coefficiente di eleggibilità della fune di guardia assumerà invece un valore proporzionale all’attuale valore della somma dell’HI vetustà e dell’HI degrado (di seguito si farà riferimento al parametro HIvet.+deg.) rapportata alla soglia di eleggibilità nel caso in cui il valore non superi la soglia di eleggibilità. Definito il parametro della soglia di eleggibilità il coefficiente di eleggibilità può essere valutato nel seguente modo keleg_fdg

Se OR Attività di sostituzione fdg -> ;

Se -> .

Dove rappresenta il parametro soglia della fune di guardia, è legato alle caratteristiche dell’asset ed è riportato nel catalogo dei modelli target.

Una volta calcolati i K per la fune di guardia (quarto livello), viene calcolato il K per l’asset di tipo funi di guardia (terzo livello) come il valore massimo dei coefficienti dei 4° livelli sottostanti

Successivamente il DSS calcola il K per le tratte di tesatura della linea (dove presenti). Questo valore sarà pari al valore del massimo dei coefficienti di eleggibilità dei 3° livelli fune di guardia sottostanti. Questo valore deve essere poi moltiplicato per il rapporto tra la lunghezza della tratta in esame e la lunghezza dell’intera linea.

Calcolo Keleg_tratta_fdg

Dove:

è il Keleg_fdg maggiore tra le campate della stessa tratta;

è la lunghezza della tratta definita come somma delle lunghezze delle campate che la compongono;

è la lunghezza totale della linea definita come somma delle lunghezze delle tratte che la compongono.

Si passa poi al calcolo del K di eleggibilità per l’intera linea come somma dei K delle tratte. Questo sarà dato dalla somma dei coefficienti di eleggibilità delle singole tratte di tutta la linea.

Calcolo Keleg_linea_fdg

Il coefficiente della linea calcolato viene confrontato con il parametro soglia S eleggibilità della linea fune di guardia.

Se il K supera la soglia e almeno uno dei K eleggibilità degli asset di quarto livello fune di guardia sottesi alla linea è uguale a 1, vengono creati N ordini di “sostituzione fune di guardia” (quarto livello) tanti quante sono le funi di guardia di terzo livello sottese alla linea per la quale sono soddisfatte le condizioni sopra citate. Sarà inoltre calcolato un ordine aggregato associato alla linea.

Per le policy aggregate linee su struttura. il DSS per ogni linea esegue i seguenti passaggi logici:

Per prima cosa si calcola il K eleggibilità struttura per ogni struttura (quarto livello) della linea. In particolare, si considerano tutti gli asset struttura figli di Telaio a loro volta figli di Sostegno sulla linea (in relazione 1:1:1 se escludiamo i DIST, DAT). Per ogni Struttura viene calcolato il sostegno di riferimento e la somma di e della struttura associata. L’indice di eleggibilità così calcolato (K eleggibilità struttura) per la singola struttura viene confrontato con un valore soglia chiamato S1 struttura (indipendente dal modello). Se il K supera la soglia di eleggibilità (S1) definita per le strutture o se è presente una attività di sostituzione della struttura (in stato “Aperto”, “Schedulabile”, “Schedulato”, “Inoltrato”, “Avviato”, “Eseguito parzialmente”). l’indice assume valore pari a 1, altrimenti assume un valore proporzionale all’HIvet.+tdg come di seguito riportato:

Se OR Attività Sostituzione struttura -> ;

Se ->.

Dove rappresenta la soglia di eleggibilità ed è riportato nell’applicazione “S eleggibilità linee” sul catalogo policy.

Una volta calcolati i K per le strutture (quarto livello), viene calcolato il K per l’intera linea come somma dei K delle singole strutture. Il coefficiente della linea calcolato viene confrontato con il parametro soglia S eleggibilità della linea struttura.

Calcolo Keleg_linea_struttura

Dove rappresenta il numero delle struttura della linea.

Se il Keleg_linea_struttura supera la soglia e almeno uno dei K eleggibilità degli asset di quarto livello struttura sottesi alla linea è uguale a 1, vengono creati N ordini di “sostituzione struttura” (quarto livello) tante quante sono le strutture di quarto livello sottese alla linea per la quale sono soddisfatte le condizioni sopra citate.

Per le policy aggregate stazioni. Le policy aggregate di sostituzione per le stazioni hanno come riferimento principale le singole unità funzionali rappresentate dai singoli stalli. Possono essere aggregate solo su terzo livello.

Prima di definire gli indici di eleggibilità in maniera analoga a quanto fatto per le linee aeree è necessario quindi definire dei coefficienti che tengano conto della rilevanza dei IV e III livelli:

KpIV livello: coefficiente attribuito a ogni singolo equipment di 4° livello in funzione all’importanza;

Kpstallo: coefficiente attributo a ogni singolo stallo dato dalla sommatoria dei KpIV livello dei 4° livelli sottesi.

I coefficienti KpIV livello saranno contenuti all’interno del catalogo degli indicatori di condizione tecnica (definiti direttamente nelle formule) e varieranno in funzione dell’equipment di 4° livello. Si riporta un esempio di seguito.

| IV livello | KpIV livello |
| --- | --- |
| Interruttore AIS | 61 |
| Sezionatore AIS | 16 |
| TV AIS | 7 |
| TA AIS | 12 |
| Scaricatore AIS | 3 |

Si definisce quindi il coefficiente di eleggibilità dei singoli 4° livelli. Per i 4° livelli eleggibili, ovvero quelli per cui sia stata generata un’attività di sostituzione (in stato “Aperto”, “Schedulabile”, “Eseguibile”, “Schedulato”, “Inoltrato”, “Avviato”, “Eseguito parzialmente”), questo sarà pari al rapporto tra il KpIV livello e il Kpstallo. Per i 4° livelli non eleggibili l’indice di eleggibilità sarà pari al rapporto tra il KpIV livello e il Kpstallo moltiplicato per il rapporto tra il parametro responsabile della determinazione dell’eleggibilità () e la soglia di eleggibilità del 4° livello (). Per i 4° livelli di stazione il è un parametro non calcolato a sistema ma configurato. Il parametro corrisponde alla soglia di eleggibilità dell’equipment.

Calcolo coefficiente di eleggibilità KIV livello:

Se equipment eleggibile ->

Se equipment non eleggibile ->

Il calcolo del coefficiente di eleggibilità dello stallo è dato dalla sommatoria dei coefficienti di eleggibilità dei 4° livelli sottostanti.

Coefficiente di eleggibilità Kstallo:

Sulla base del coefficiente di eleggibilità dello stallo e definendo una soglia di eleggibilità per le policy aggregate di sostituzione dello stallo ( parametro riportato nel catalogo dei modelli target) si definiscono le seguenti logiche di generazione:

Policy di sostituzione interruttori sottesi allo stallo: Se -> Generazione n OdL “Sostituzione Interruttore (4° livello)” tanti quanti sono gli interruttori sottesi allo stallo;

Policy di sostituzione sezionatori sottesi allo stallo: Se -> Generazione n OdL “Sostituzione Sezionatore (4° livello)” tanti quanti sono i sezionatori sottesi allo stallo;

Policy di sostituzione TV sottesi allo stallo: Se -> Generazione n OdL “Sostituzione TV (4° livello)” tanti quanti sono i TV sezionatori sottesi allo stallo;

Policy di sostituzione TA sottesi allo stallo: Se -> Generazione n OdL “Sostituzione TA (4° livello)” tanti quanti sono i TA sottesi allo stallo;

Policy di sostituzione scaricatori sottesi allo stallo: Se -> Generazione n OdL “Sostituzione scaricatore (4° livello)” tanti quanti sono i scaricatori sottesi allo stallo.

Saranno configurate a sistema una policy aggregata per ognuno dei quarti livelli sottesi allo stallo per cui si vogliono applicare le logiche.

il DSS valuta la policy per tutti gli stalli presenti a sistema eseguendo i passaggi logici di seguito:

Per prima cosa viene calcolato il K di quarto livello per ognuno degli asset di quarto livello sottostanti al singolo stallo. I K di quarto livello si calcolano utilizzando una formula cablata a sistema che prende in input anche il Kp, consultabile e modificabile tramite l’azione sull’applicazione catalogo policy “Kp equipment stazione”.

Successivamente il DSS calcola il K dello stallo come sommatoria dei K di tutti gli equipment ad esso sottesi;

Se il K dello stallo supera la soglia definita dal S eleggibilità dello stallo, il DSS crea un ordine di sostituzione per ognuno degli equipment sottesi allo stallo e per lo stallo stesso.

## Elaborazione policy periodiche – standard

Le policy periodiche – standard sono policy caratterizzate dalla tipologia corrispondente a “periodiche” e dall’assenza di altre logiche di elaborazione. In particolare:

non devono essere riferite a policy di sostituzione su conduttore e fune di guardia.

non devono essere riferite a tronchi di ispezione.

non devono avere il flag policy aggregate.

Il DSS genererà N ordini periodici in relazione alla frequenza definita nella policy come dettagliato più avanti nel paragrafo.

Di seguito la sequenza degli step logici eseguiti dal DSS quando si avvia l’elaborazione di questo algoritmo:

Stessa logica descritta nel capitolo 5.1 bullet A

Stessa logica descritta nel capitolo 5.1 bullet B

Stessa logica descritta nel capitolo 5.1 bullet C

Stessa logica descritta nel capitolo 5.1 bullet D si applica alle policy inter-annuali, Per le policy intra-annuali, invece, se la policy viene rilanciata in un anno successivo, il sistema verifica la presenza di OdL già generati e procede a creare solo gli ordini per gli anni successivi fino a N+5. Ad esempio, se la policy viene eseguita per la prima volta nel 2025, gli OdL saranno generati fino al 2029. Se la policy viene rilanciata nel 2027, il sistema creerà nuovi OdL solo per gli anni 2030 e 2031, escludendo gli anni già coperti dalla precedente esecuzione.

Stessa logica descritta nel capitolo 5.1 bullet E ad eccezione della logica di autoprevalenza che non viene applicata alle policy periodiche

Stessa logica descritta nel capitolo 5.1 bullet F

Stessa logica descritta nel capitolo 5.1 bullet G

Le Policy periodiche determinano la generazione di Ordini di Lavoro (OdL) con una frequenza prestabilita. Gli OdL periodici verranno generati con stato iniziale "Aperto".

Le policy possono essere classificate in due categorie principali, in base alla loro frequenza:

inter-annuali: con frequenza superiore a un anno.

intra-annuali: con frequenza inferiore o uguale a un anno.

Policy Inter-annuali

Per le policy con frequenza superiore a un anno, viene generato un unico OdL. I campi calcolati in fase di generazione sono:

Data Modello DSS: calcolata come “data di chiusura dell’OdL precedente + frequenza”.

Data Pianificata Fine: assume lo stesso valore della Data Modello.

Data Pianificata Inizio: calcolata come “Data Pianificata Fine - durata pianificata”.

Data di Massimo Posticipo: calcolata come “Data Modello + massimo posticipo attività”.

Data Non Prima di: calcolata come “Data Modello – massimo anticipo attività”.

Policy Intra-annuali

Per le policy con frequenza inferiore o uguale a un anno, vengono generati OdL fino all’anno N+5. I campi calcolati in fase di generazione sono:

Data Modello: determinata in base alla frequenza (descritta successivamente).

Data Pianificata Fine: assume lo stesso valore della Data Modello.

Data Pianificata Inizio: calcolata come “Data Pianificata Fine - durata pianificata”.

Data di Massimo Posticipo: calcolata come “Data Modello + massimo posticipo attività”.

Data Non Prima di: corrisponde alla data di inizio della periodicità.

La Data Modello è calcolata come: “Primo giorno dell’anno di competenza + frequenza”

Di conseguenza, la Data Modello di ciascun OdL coincide con l’ultimo giorno del periodo definito dalla frequenza. Di seguito alcuni esempi:

Frequenza 1 mese → 12 controlli con Data Modello: 31 gennaio, 28 febbraio, 31 marzo, 30 aprile, 31 maggio, 30 giugno, 31 luglio, 31 agosto, 30 settembre, 31 ottobre, 30 novembre, 31 dicembre.

Frequenza 2 mesi → 6 controlli con Data Modello: 28 febbraio, 30 aprile, 30 giugno, 31 agosto, 31 ottobre, 31 dicembre.

Frequenza 3 mesi → 4 controlli con Data Modello: 31 marzo, 30 giugno, 30 settembre, 31 dicembre.

Frequenza 4 mesi → 3 controlli con Data Modello: 30 aprile, 31 agosto, 31 dicembre.

Frequenza 6 mesi → 2 controlli con Data Modello: 30 giugno, 31 dicembre.

Frequenza 1 anno → 1 controllo con Data Modello: 31 dicembre.

Esempio di Odl generato per frequenza mensile:

Le frequenze gestibili per le policy intra-annuali sono:

Giornaliera ("G") → da 1 a 6 giorni

Settimanale ("S") → da 1 a 3 settimane

Mensile ("M") → 1, 2, 3, 4, 6, 12 mesi

Per le policy periodiche, è possibile impostare una data "A partire da", che indica il momento a partire dal quale l’OdL generato dalla policy può essere posizionato temporalmente.

Si specifica che sarà possibile inserire solamente una data “a partire da” > “data attuale”.

Di seguito si riportano le logiche relative alla generazione di OdL quando risulta valorizzato il campo “a partire da”:

Se “a partire da” <= 1° Gennaio + frequenza -> gli OdL vengono generati come una normale policy periodica. Es: Policy periodica con frequenza uguale a 3 mesi e data “a partire da” uguale al 15/03. In questo caso verranno generati 4 OdL per anno (31/03, 30/06, 30/09, 31/12).

Se “a partire da”> 1° Gennaio + frequenza -> vengono generati solo gli OdL successivi alla data “a partire da”. Es: Policy periodica con frequenza uguale a 3 mesi e data “a partire da” uguale al 15/04. In questo caso verranno generati 3 OdL per anno (30/06, 30/09, 31/12).

Nota importante:

Max posticipo e max anticipo sono sempre espressi in giorni

La durata pianificata campo espresso in ore.

## Elaborazione policy periodiche – trainante/trainato

Le policy periodiche – trainante/trainato sono policy caratterizzate dalla tipologia corrispondente a “periodiche”. In particolare, devono essere associate ad asset di IV livello di tipo conduttore o fune di guardia con attività di sostituzione in output.

L’algoritmo trainante/trainato eredita dall’algoritmo standard delle policy periodiche 4.5 tutte le fasi: valutazione policy, idoneità, calcolo dell’attività, calcolo data modello, prevalenza. La sua peculiarità si espleta dopo l’esecuzione del passo D) e prima del passo E) della prevalenza.

In sostanza il DSS, per ogni asset validato e per il quale non esiste un OdL a parità di asset ed attività, inizia a determinare la tratta di tesatura. Se per l’asset analizzato non esiste la tratta di tesatura, il DSS creerà direttamente l’OdL e si comporta esattamente come l’algoritmo standard.

Se la tratta di tesatura esiste, il DSS provvederà a sincronizzare le esecuzioni degli altri asset che appartengono alla stessa tratta di tesatura. A parità di tratta di tesatura ci possono essere asset che soddisfano la policy ed asset non soddisfano la policy. In questa circostanza il DSS creerà tanti OdL quanti sono gli asset che appartengono alla tratta di tesatura ed assegnerà un attributo che assume valori “Trainante” o “Trainato”. Può esistere un solo OdL Trainante e verrà eletto tra gli asset che soddisfano la policy e scelto in maniera arbitraria (in genere il primo validato a parità di tratta di tesatura).

Di seguito un esempio di elaborazione di una policy tronco di ispezione:

Per questo insieme di OdL viene eseguito il passo E) della prevalenza e il passo G) di inserimento OdL in un’unica transazione.

## Elaborazione policy periodiche – tronco di ispezione

Le policy periodiche – tronco di ispezione sono policy caratterizzate dal flag “Tronco di ispezione” attivo e che insistono su campate aeree o tratte cavo (secondo livello) di tipologia “Periodica”.

L’algoritmo tronco d’ispezione eredita dall’algoritmo standard delle policy periodiche 4.5 tutte le fasi: valutazione policy, idoneità, calcolo dell’attività, calcolo data modello, prevalenza. La sua peculiarità si espleta dopo l’esecuzione del passo D).

In sostanza il DSS, per ogni asset che soddisfa la policy e per il quale non esiste un OdL a parità di policy, asset ed attività, inizia a determinare il tronco d’ispezione. Per le policy su tronco (sia con frequenza inter che intra annuale), oltre agli ordini con stessa tripletta policy, asset ed attività verifica anche il codice del tronco e genera un nuovo ordine solo se non ne esiste già un altro con stessa policy, asset, attività e tronco di ispezione. Se l’asset considerato non ha un tronco di ispezione associato, la sua elaborazione termina con scarto.

Come descritto nel paragrafo periodico standard 5.5, la generazione degli Ordini di Lavoro (OdL) segue le regole stabilite in base alla frequenza della policy. Successivamente, in questa casistica, viene generato un OdL aggregatore associato alla linea relativa alla campata aerea/tratta cavo sulla quale è scattata la policy, riportando anche il SAP-ID del tronco di ispezione.

Sotto all’OdL aggregatore vengono creati N ordini associati a tutte le campate aeree/tratte cavo che fanno parte del singolo tronco di ispezione a cui l'OdL aggregatore (padre) fa riferimento.

Per le policy inter-annuali, viene generato un unico OdL aggregatore, a cui saranno associati N ordini, uno per ciascuna campata del tronco di ispezione.

Per le policy intra-annuali, vengono invece generati N OdL aggregatori, uno per ogni scadenza prevista dalla frequenza. A ciascun aggregatore saranno poi associati N ordini, in base al tronco di ispezione e alla data modello.

Per ogni OdL aggregatore e tutti gli ordini “figli” viene eseguito il passo E) della prevalenza e al termine il passo G) di inserimento OdL in un’unica transazione.

# Eventi che attivano le azioni da parte del DSS

Le policy si dividono in due categorie: policy periodiche e policy on condition. La principale differenza tra queste due categorie di policy è il trigger che ne determina la valutazione da parte del DSS. La valutazione della policy determina la generazione di ordini sugli asset solo se sono soddisfatte le condizioni in input alla policy stessa (nel caso siano presenti).

Gli eventi trigger che scatenano il calcolo delle policy da parte del DSS sono di due tipologie: temporale (per le policy periodiche), on demand (per le policy on condition).

Per le policy periodiche il trigger temporale viene scatenato una volta all’anno in una data modificabile previa modifica di una variabile d’ambiente. In corrispondenza di questa data, il DSS valuta l’intero set di policy periodiche attive sul sistema e genera ordini per le policy che soddisfano le condizioni in input alla policy e i vincoli di idoneità degli asset già discussi nei precedenti capitoli.

Per quanto riguarda le policy periodiche inter-annuali (con frequenza maggiore a 1 anno) oltre al trigger temporale, viene considerato come trigger per l’elaborazione la chiusura dell’ordine precedente oppure la creazione di un nuovo asset.

Per quanto riguarda le policy periodiche infra-annuali (con frequenza minore uguale ad 1 anno) oltre al trigger temporale esiste quello dello scenario di creazione di un nuovo asset.

Per le policy on condition i trigger possono riguardare eventi di processo o anagrafici e sono discussi nel dettaglio nel seguito del capitolo.

Gli eventi scatenano l’elaborazione delle policy come descritto nel dettaglio nel capitolo 4.

## Eventi anagrafica asset

Nel capitolo sono riportati e descritti tutti gli eventi riguardanti l’anagrafica degli asset che determinano la valutazione delle policy attive a sistema.

### Modifica attributo di zonizzazione su asset

L’aggiornamento degli attributi che determinano la zonizzazione di un asset sarà gestito come modulo tramite script che avranno il compito di aggiornare attributi dedicati dell’asset.

Gli script permetteranno di aggiornare i seguenti attributi:

LIN_ZONA_AMBIENTALE → per le linee in cavo

LIN_ZONA_ANNO_SA → per le linee aeree

LIN_ZONA_ANNO_SB → per le linee aeree

Nel caso delle linee in cavo è necessario modificare la frequenza di zonizzazione degli OdL già creati dal DSS.

Per individuare gli ordini da modificare, il DSS si basa sul PdL associato agli ordini. Infatti, le policy di zonizzazione periodiche, pur avendo frequenze diverse, generano sempre la stessa macro-attività. Di conseguenza, per uno stesso asset, il PdL scelto dal DSS rimane invariato, garantendo che tutti gli ordini generati da policy di zonizzazione su un medesimo asset abbiano lo stesso PdL associato. Questo permette al DSS di identificare con precisione gli ordini da modificare.

Gli ordini individuati sono sempre quelli in stato APERTO, quelli in stato successivo ad aperto non vanno mai toccati.

Le regole di modifica degli ordini generati da un’altra policy di zonizzazione si applicano specificamente all’evento di modifica dell’attributo di zonizzazionesull’asset

. Contestualmente viene aggiornato il codice ID policy con quello della policy corrispondente al nuovo attributo

, associando l’ID policy relativo alla quella scattata

Per le policy di zonizzazione applicate a un tronco, la modifica dell’attributo su un singolo asset (campata/tratta cavo) non è sufficiente a scatenare l’elaborazione della policy.

In questi casi, il sistema valuta la coerenza degli attributi di zonizzazione su tutti gli asset appartenenti allo stesso tronco:

Se tutti gli attributi risultano coerenti, viene attivata l’elaborazione della policy su tutti gli asset.

In caso di discrepanze tra gli attributi degli asset del tronco, l’elaborazione non viene avviata.

Esempio di zonizzazione:

### Inserimento in anagrafica di un nuovo asset

L’aggiunta di un nuovo asset in anagrafica scatena l’elaborazione di tutte le policy (sia periodiche che on condition, escluse le policy aggregate) che sono associate alla stessa tipologia elemento dell’asset appena inserito. Le policy individuate saranno quindi valutate senza un ordine predefinito sull’asset inserito secondo le modalità di elaborazione descritte nel dettaglio nel capitolo 5.

### Modifica dello stato di un asset esistente

La modifica dello stato di un asset già presente in anagrafica scatena la valutazione delle policy su quell’asset. In particolare, vengono valutate le policy che hanno associata una tipologia elemento uguale a quella dell’asset per cui è avvenuta la modifica di stato. Il valore assunto dallo stato modificato potrebbe rientrare tra quelli idonei alla generazione degli ordini.

L’evento descritto si può applicare ad una qualsiasi delle elaborazioni descritte nel dettaglio nel capitolo 4.

### Modifica di un asset esistente

La modifica di un asset esistente in anagrafica scatena la valutazione da parte del DSS delle policy su quell’asset. In particolare, vengono valutate tutte le policy che hanno associata una tipologia elemento uguale a quella dell’asset per il quale è avvenuta la modifica.

L’evento descritto si può applicare ad una qualsiasi delle elaborazioni descritte nel dettaglio nel capitolo 4.

### Modifica idoneità ed eccezione stati asset

La modifica della tabella di idoneità o di eccezione gestite all’interno dell’applicazione “Catalogo policy” scatena la valutazione da parte del DSS delle policy per valutare se asset presenti a sistema risultano idonei alla generazione di ordini successivamente alla modifica effettuata.

L’evento descritto si può applicare ad una qualsiasi delle elaborazioni descritte nel dettaglio nel capitolo 4.

## Eventi catalogo policy

Nel seguito del capitolo sono riportati gli eventi relativi al catalogo delle policy che determinano la valutazione delle policy attive a sistema.

### Modifica a catalogo di policy non tronco di ispezione

L’evento di modifica di una policy a catalogo determina da parte del DSS l’elaborazione della stessa sugli asset di tipologia elemento uguale a quella associata alla policy. Le policy possono essere modificate una volta poste in stato “PENDING REVIEW”. Le modifiche alle policy che vengono propagate agli ordini (in stato “Aperto” o “Da validare”) già generati sono esclusivamente quelle di natura temporale. In particolare, per i campi riportati di seguito:

Frequenza (per policy periodiche);

Differibilità (per policy on condition);

Max anticipo;

Max posticipo.

In caso di modifica di una policy è necessario prevedere le seguenti casistiche per ogni asset su cui è applicato il calcolo:

È associato all’asset un ordine che si trova in stato Aperto o Da validare e l’attività collegata all’ordine è la stessa attività associata alla policy in modifica (passando per la scelta da macro-attività ad attività). Si provvederà a modificare l’ordine oppure gli ordini esistenti;

È associato all’asset un ordine che non si trova in stato Aperto e Da validare e l’attività collegata all’ordine è la stessa attività associata alla policy in modifica (passando per la scelta da macro-attività ad attività). Non ci sarà creazione o modifica ordini;

Non è associato all’asset un ordine associato alla stessa attività associata alla policy. l’algoritmo segue la stessa logica che avrebbe seguito per il caso standard.

Per quanto riguarda le policy periodiche, il DSS individua gli ordini già generati dalla policy ed effettua la modifica delle date. Si specifica che non è possibile modificare la frequenza di una policy portandolo da un valore > di 1 anno ad uno <= si un 1 anno e viceversa.

Nel caso ci sia un cambio di frequenza, è necessario discriminare le azioni da dover eseguire sulla base del fatto che questa:

Aumenta: (nuova frequenza < vecchia frequenza) significa che sugli OdL esistenti sarà modificata la data modello degli ordini esistenti e creati nuovi ordini sulla base dell’ordine. Vengono recuperati tutti gli ordini in stato “Attivo” non ancora temporalmente partiti legati a quella policy ed asset. si identificano il numero di ordini necessari a coprire lo stesso periodo temporale. Viene aggiornata la data modello degli ordini più prossimi e creati degli ordini nuovi per coprire le date nuove.

Diminuisce (nuova frequenza > vecchia frequenza) significa che non saranno previste creazioni di nuovi ordini ma solo la modifica delle date modello di quelli esistenti e ne porta in stato “Non eseguito” quelli di troppo. In questo scenario vengono estratti tutti gli ordini associati all’asset in input creati con la policy passata in input.

Esempio di modifica data modello / creazione nuovi ordini nel caso di cambio frequenza da mensile a trimestrali.

Per quanto riguarda le policy on condition, in caso di cambio di differibilità, il DSS individua gli ordini già generati dalla policy modificata e modifica la data modello con il nuovo valore individuato sulla base della differibilità indicata nella policy. Per questa casistica si possono presentare diversi scenari dipendentemente dal nuovo valore scelto per il campo “differibilità”, descritti nel dettaglio di seguito:

Caso policy on condition senza input difformità/interferenza: La data modello è calcolata come somma della data di generazione originale dell’ordine e la nuova differibilità della policy. Se la nuova data modello calcolata è minore della data attuale viene comunque riportata sull’ordine.

Caso policy on condition con input difformità/interferenza: La data modello è calcolata come somma della data di rilevazione della difformità e la differibilità della policy.

Se la nuova differibilità è minore della differibilità vecchia, la data modello viene ricalcolata e risulta antecedente alla data attuale.

Se la nuova differibilità è maggiore della differibilità vecchia, la data modello viene ricalcolata e risulta successiva alla data attuale.

Se si effettua una modifica di Max anticipo, Se la policy è periodica infra-annuale (frequenza < 1 anno): non viene eseguita modifica poiché questo caso non rientra tra quelle modificabili dall’utente.

Se la policy è periodica inter-annuale (frequenza > 1 anno) o on-condition: la data “non prima di” è calcolata come data modello – nuovo valore passato nel campo massimo anticipo attività.

Se si effettua una modifica del Max posticipo, viene sommata alla data modello dell’ordine il massimo posticipo attività.

Le logiche indicate sono sovrapponibili. Nel caso in cui pervenisse al DSS sia la modifica della frequenza e di uno o entrambi i valori del massimo anticipo e posticipo si partirà sempre prima dal ricalcolo del cambio frequenza e differibilità e poi sul calcolo della data al più presto ed al più tardi

L’evento descritto si applica esclusivamente alle elaborazioni (descritte nel dettaglio nel capitolo 4) elencate di seguito:

Elaborazione policy on condition – standard.

Elaborazione policy periodiche – standard;

### Inserimento a catalogo di policy non tronco di ispezione

L’inserimento di una nuova policy a catalogo scatena l’elaborazione della stessa su tutti gli asset presenti a sistema della tipologia elemento associata alla policy. L’evento descritto si applica esclusivamente alle elaborazioni (descritte nel dettaglio nel capitolo 4) elencate di seguito:

Elaborazione policy on condition – standard;

Elaborazione policy periodiche – standard;

### Modifica/inserimento policy trainante trainato

Nel caso di modifica o inserimento di una policy con output attività di sostituzione che insiste su conduttore o fune di guardia, il DSS scatena l’elaborazione della stessa su tutti gli asset presenti a catalogo di tipologia elemento conduttore o fune di guardia. L’evento descritto si applica esclusivamente all’elaborazione (descritte nel dettaglio nel capitolo 4) policy on condition – trainante/trainato.

### Inserimento a catalogo di policy su tronco di ispezione

L’inserimento di una nuova policy su tronco di ispezione a catalogo determina l’elaborazione da parte del DSS della stessa su tutte le linee presenti a sistema. L’evento descritto si applica esclusivamente alle elaborazioni (descritte nel dettaglio nel capitolo 4) elencate di seguito:

Elaborazione policy on condition – tronco di ispezione;

Elaborazione policy periodiche – tronco di ispezione;

### Modifica a catalogo di policy su tronco di ispezione

Nel caso di modifica di una policy che insiste su un tronco di ispezione, per quanto riguarda i campi differibilità, frequenza, max anticipo o max posticipo vengono applicate le logiche di rielaborazione degli ordini già creati dal DSS sullo stesso asset.

L’evento descritto nel seguito del capitolo si applica esclusivamente alle elaborazioni (descritte nel dettaglio nel capitolo 4) elencate di seguito:

Elaborazione policy on condition – tronco di ispezione;

Elaborazione policy periodiche – tronco di ispezione;

In particolare, le logiche previste differiscono nel caso la policy sia di tipo periodico o on condition.

#### Caso policy on condition

Il DSS, dopo la modifica, verifica quali sono gli ordini sulle specifiche linee che sono stati creati dalla policy e che si trovano in stato “Da validare” o “Aperto”. Per questi ordini effettua l’aggiornamento della data modello coerentemente con le logiche descritte nel capitolo 5.2.1. Di seguito è riportato un esempio:

#### Caso policy periodiche

Per le policy periodiche sono applicate due logiche diverse a seconda che si tratti di una policy con frequenza inter-annuale (> 1 anno) o intra-annuale (<= 1 anno).

Frequenza intra-annuale:

Il DSS dopo la modifica, verifica quali sono gli ordini sulle specifiche linee che sono stati creati dalla policy e che si trovano in stato “Da validare” o “Aperto”. Per questi ordini effettua l’aggiornamento della data modello coerentemente con le logiche descritte nel capitolo 5.2.1. Di seguito è riportato un esempio:

Frequenza inter-annuale:

Il DSS dopo la modifica, verifica quali sono gli ordini sulle specifiche linee che sono stati creati dalla policy e che si trovano in stato “Da validare” o “Aperto”. Per questi ordini effettua l’aggiornamento della data modello coerentemente con le logiche descritte nel capitolo 5.2.1. Di seguito è riportato un esempio:

## Eventi altri cataloghi

### Modifica catalogo modelli target

La modifica di un record nel catalogo modelli target innesca la valutazione delle policy nelle quali viene utilizzato il modello modificato. In particolare, i modelli target possono essere inseriti come target di raffronto di input alle policy.

L’evento descritto è applicabile ad una qualsiasi delle elaborazioni riportate nel dettaglio nel capitolo 4.

## Eventi di processo

### Generazione di un Ordine

Quando nel sistema viene creato un nuovo ordine, il DSS controlla se ci sono delle policy che potrebbero attivarsi per la presenza del PdL associato all’ordine appena creato. Le policy potenziali sono tutte quelle che hanno come input il PdL. Il DSS innescherà la valutazione delle policy individuate sull’asset delle rispettive tipologie.

L’evento descritto è applicabile ad una qualsiasi delle elaborazioni riportate nel dettaglio nel capitolo 4.

### Registrazione difformità

La registrazione di una difformità a sistema associata ad un asset scatena l’elaborazione da parte del DSS delle policy che hanno in input quella difformità. La policy deve essere associata alla stessa tipologia elemento dell’asset a cui è stata associata la difformità, condizione necessaria anche per poter inserire la difformità come input alla policy. Le policy che hanno la difformità in input vengono valutate sull’asset dove è stata registrata la difformità.

L’evento descritto nel seguito del capitolo si applica esclusivamente alle elaborazioni (descritte nel dettaglio nel capitolo 4) elencate di seguito:

Elaborazione policy on condition – standard;

Elaborazione policy on condition – trainante/trainato;

Elaborazione policy on condition – tronco di ispezione;data

### Registrazione interferenza

La registrazione di un’interferenza a sistema associata ad un asset scatena l’elaborazione da parte del DSS delle policy che hanno in input quell’interferenza. La policy deve essere associata alla stessa tipologia elemento dell’asset a cui è stata associata l’interferenza, condizione necessaria anche per poter inserire l’interferenza come input alla policy. Le policy che hanno l’interferenza in input vengono valutate sull’asset dove è stata registrata l’interferenza.

L’evento descritto nel seguito del capitolo si applica esclusivamente alle elaborazioni (descritte nel dettaglio nel capitolo 4) elencate di seguito:

Elaborazione policy on condition – standard;

Elaborazione policy on condition – trainante/trainato;

Elaborazione policy on condition – tronco di ispezione;

### Rilevamento misura

La rivelazione di una misura ed il calcolo della variabile associata farà in modo che il DSS richieda per l’asset interessato, la valutazione di tutte le policy, che hanno questa misura come input. La policy deve essere associata alla stessa tipologia elemento dell’asset a cui è stata associata la misura, condizione necessaria anche per poter inserire la variabile come input alla policy. Le policy che hanno una variabile in input vengono valutate sull’asset dove è stata registrata la variabile.

L’evento descritto è applicabile ad una qualsiasi delle elaborazioni riportate nel dettaglio nel capitolo 4.

### Modifica del valore di un HI ricalcolato

Dopo il ricalcolo giornaliero degli HI, il sistema effettua un confronto tra il valore salvato prima del ricalcolo e quello dell’ultimo ricalcolo effettuato sull’asset. Per gli HI il cui valore sull’asset è cambiato, si individuano le policy associate (per le quali l’HI è in input e sono associate all’asset su cui è variato il valore) e ne viene triggerata l’elaborazione.

L’evento descritto è applicabile ad una qualsiasi delle elaborazioni riportate nel dettaglio nel capitolo 4.

Dopo il ricalcolo giornaliero degli HI il sistema innesca la verifica del calcolo difformità basate su formule con HI.

### Modifica del valore di un TdA ricalcolato

Dopo il ricalcolo annuale dei TdA il sistema effettua un confronto tra il valore prima del ricalcolo e quello dell’ultimo ricalcolo effettuato sull’asset. Per i tassi di anomalia il cui valore sull’asset è cambiato, si individuano le policy associate (per le quali il tasso di anomalia è in input e sono associate all’asset su cui è variato il valore) e ne viene triggerata l’elaborazione.

L’evento descritto è applicabile ad una qualsiasi delle elaborazioni riportate nel dettaglio nel capitolo 4.

### Modifica del valore di un TdG ricalcolato

Dopo il ricalcolo annuale dei TdG il sistema effettua un confronto tra il valore prima del ricalcolo e quello dell’ultimo ricalcolo effettuato sull’asset. Per i tassi di guasto il cui valore sull’asset è cambiato, si individuano le policy associate (per le quali il tasso di guasto è in input e sono associate all’asset su cui è variato il valore) e ne viene triggerata l’elaborazione.

L’evento descritto è applicabile ad una qualsiasi delle elaborazioni riportate nel dettaglio nel capitolo 4

### Modifica formula TdA

Dato che il tasso di anomalia è ricalcolato dal sistema una sola volta l’anno, in caso di modifica della formula viene innescato il calcolo sulla base della nuova formula. Successivamente al ricalcolo viene effettuata la valutazione descritta nel capitolo 5.4.6 per determinare se vanno innescate le policy o meno.

### Modifica formula TdG

Dato che il tasso di guasto è ricalcolato dal sistema una sola volta l’anno, in caso di modifica della formula viene innescato il calcolo sulla base della nuova formula. Successivamente al ricalcolo viene effettuata la valutazione descritta nel capitolo 5.4.7 per determinare se vanno innescate le policy o meno.

### Aggiornamento variabile di stato

All’aggiornamento di una variabile di stato calcolata dal sistema, si innesca la stessa valutazione descritta nel capitolo 5.4.4.

### Chiusura ordine periodico inter-annuale

La chiusura di un ordine periodico inter-annuale innesca il rilancio della policy che lo ha generato per il solo asset dell’OdL chiuso (Algoritmo paragrafo 5.5). Il sistema notifica il DSS della chiusura dell’ordine, il quale verifica la presenza della policy associata all’ordine nel catalogo, accertandosi che sia attiva e con una frequenza superiore a un anno.

Se l’ordine chiuso è un ordine padre generato da una policy periodica con frequenza inter-annuale e logica di tronco, il sistema rilancerà la stessa policy per tutti gli asset appartenenti al medesimo tronco. (Algoritmo paragrafo 5.7).

Al contrario, la chiusura di un ordine figlio non comporta il rilancio della policy. In questo caso, il sistema non attiverà alcuna nuova elaborazione, poiché il processo di generazione degli ordini segue la logica stabilita solo per l’ordine padre.

Le elaborazioni innescate da questo evento rientrano nelle logiche delle policy periodiche descritte nel capitolo 4.

## Evento temporale di innesco policy periodiche

L’evento di innesco delle policy periodiche è di tipo temporale e scatta una volta l’anno in corrispondenza di una data definita a sistema (modificabile tramite ticket previa modifica della variabile d’ambiente). Questo evento porta all’elaborazione massiva da parte del DSS di tutte le policy attive a sistema di tipologia “Periodica” senza un ordine stabilito.

Nel caso particolare in cui l’attività associata alla policy sia di tipo “Sostituzione” l’algoritmo non eseguirà la policy poiché per assunzione una policy periodica non prevede la Sostituzione.

Le policy periodiche inter-annuali possono scattare anche in corrispondenza di altri eventi oltre a quello di innesco temporale periodico. Come, ad esempio, il caso di chiusura di un ordine inter-annlue periodico.

Le policy periodiche attualmente prevedono le seguenti frequenze inter e intra annuali:

Settimanale;

Bisettimanale;

Mensile;

Bimestrale;

Trimestrale;

Quadrimestrale;

Semestrale;

Annuale;

Triennale;

Quinquennale;

Esennale;

Decennale;

Dodicennale.

L’evento temporale può innescare esclusivamente le elaborazioni di seguito riportate (dettagliate nel capitolo 4):

Elaborazione policy periodiche – standard;

Elaborazione policy periodiche – zonizzazione.

Elaborazione policy periodiche – tronco di ispezione.

Le elaborazioni portate a termine con esito positivo produrranno un numero di ordini sul singolo asset corrispondente a quelli determinati sulla base dell’algoritmo innescato.

# Eventi che non scatenano l’elaborazione delle policy

Di seguito l’elenco degli eventi che non scatenano la valutazione delle policy da parte del DSS

Aggiunta a catalogo nuova attività

Modifica catalogo attività

Aggiunta a catalogo nuova difformità

Modifica catalogo difformità

Aggiunta a catalogo nuova interferenza

Modifica catalogo interferenze

Disattivazione a catalogo di una policy esistente

Disattivazione a catalogo di un’attività esistente

Disattivazione a catalogo di una difformità esistente

Disattivazione a catalogo di un’interferenza esistente

Modifica della formula di un HI

Modifica formula del tasso di guasto

Modifica formula del tasso di anomali

Modifica record catalogo misure e variabili di stato

Risoluzione difformità

Risoluzione interferenza
