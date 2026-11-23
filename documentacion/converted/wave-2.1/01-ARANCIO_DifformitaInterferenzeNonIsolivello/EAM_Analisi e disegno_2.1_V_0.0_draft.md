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

# Obiettivi

Analisi e disegno delle soluzioni implementative in ambito wave 2.1. In particolare, nel seguito del documento saranno descritte funzionalmente e tecnicamente le funzionalità riferite alla gestione di oggetti di catalogo KO non inclusi nel rilascio 2.0.

# Definizione di relazioni tra asset

Per gestire alcuni degli oggetti di catalogo non inclusi nel rilascio 2.0 è necessario prevedere una funzionalità che permetta al sistema di leggere informazioni da asset differenti da quello associato all’oggetto di catalogo.

In questo capitolo sono quindi riportate le regole che definiscono relazioni univoche tra asset (esclusivamente per le relazioni attualmente individuate nei cataloghi per gli oggetti di questo cluster KO) necessarie al sistema per identificare univocamente gli asset su cui applicare le logiche in funzione dell’asset associato all’oggetto di catalogo.

Di seguito sono elencati i cluster KO specifici risolti con l’applicazione della funzionalità descritta in questo capitolo:

Rilevabilità Difformità/Interferenza dipendente da attributi su altri asset

Policy che hanno in input un parametro che riferisce ad un asset differente da quello su cui è definita la policy;

Policy che sono definite su un asset differente da quello di output (cluster ricondotto a quello precedente tramite bonifica catalogo);

L'attributo per determinare la prevalenza per metodo è definito su asset differente da quello dell'attività;

Rilevabilità Difformità/Interferenza dipendente da attività definita su altri asset;

Attività la cui esecuzione risolve difformità su livelli gerarchici diversi da quelli previsti in rilascio #1.

Nel seguito del documento, con il termine “oggetto” si identifica il singolo record di catalogo (Difformità, Attività, Policy, ecc) mentre con il termine “Elemento” si identifica l’elemento anagrafico che raccoglie le classificazioni asset.

Per tutti i casi elencati sopra e necessario definire delle regole di relazione inequivocabili tra gli asset. Nel file in allegato al capitolo 8 sono riportate regole di associazione per tutte le combinazioni attualmente presenti nei cataloghi e che saranno implementate in 2.1. Il file è compilato come segue:

Nella colonna “Asset di riferimento” è riportato l’asset a cui fa riferimento l’oggetto di catalogo:

Figura : esempio maschera catalogo Difformità, campo in cui viene associato l’elemento EAM all’oggetto di catalogo con le relative classificazioni nella tabella sottostante

Nella colonna “Relazione” è riportata la cardinalità della relazione anagrafica tra gli asset;

Nella colonna “Asset da cui leggere” è riportato l’asset su cui si richiede di verificare la condizione indicata nel catalogo per la specifica casistica;

Nella colonna “Regola” è definita la regola che permette di selezionare inequivocabilmente l’asset da cui leggere l’informazione per determinare l’azione sull’asset di riferimento dell’oggetto di catalogo;

Nella colonna “Eccezioni” sono riportate eventuali regole aggiuntive relative a casi particolari per i quali non è sufficiente la regola generale definita nella colonna precedente.

Le regole sono definite sulla base di associazioni anagrafiche (quando presenti) combinate con regole specifiche che permettono di relazionare anche asset che non sono direttamente collegati tra loro.

Si specifica che per l’eccezione delle linee in doppia terna, viene considerato sempre l’asset con assetnum minore.

Per meglio comprendere come sono state impostate le regole che relazionano gli asset è utile una rappresentazione schematica dei rapporti gerarchici che sussistono tra gli asset in anagrafica riportata nei capitoli seguenti.

È necessario distinguere correttamente il concetto di relazione con quello di regola. La relazione identifica il rapporto gerarchico che sussiste tra due asset, la regola invece è la logica specifica che va implementata per permettere al sistema di identificare inequivocabilmente l’asset da cui leggere partendo dall’asset a cui l’oggetto a catalogo fa riferimento. Di seguito è riportata una generalizzazione delle possibili relazioni che intercorrono tra gli asset:

L’asset su cui leggere è ad un livello superiore sullo stesso ramo gerarchico: non sono necessarie regole perché la cardinalità è sempre 1 a 1 (Regola “L'asset gerarchicamente superiore”);

L’asset su cui leggere è ad un livello superiore ma su un ramo gerarchico differente: la cardinalità è 1 a N quindi sono definite regole specifiche in base alla casistica;

L’asset su cui leggere è ad un livello inferiore sullo stesso ramo: la cardinalità è 1 a N quindi sono definite regole specifiche in base alla casistica;

L’asset su cui leggere è ad un livello inferiore ma su un ramo gerarchico differente: la cardinalità è 1 a N quindi sono definite regole specifiche in base alla casistica;

L’asset su cui leggere è allo stesso livello ma su un ramo gerarchico differente: la cardinalità è 1 a N quindi sono definite regole specifiche in base alla casistica.

Sono state individuate due macro-casistiche di regole utilizzabili:

Regola dell’almeno 1, utilizzata principalmente nei casi in cui non è possibile identificare alcuna relazione specifica tra gli asset;

Regole basate su relazioni gerarchiche precise e inequivocabili. Utilizzata nel caso in cui due asset siano inequivocabilmente relazionati attraverso, ad esempio, il SAP ID o un attributo specifico.

Come specificato in precedenza, le regole indicate nel file fanno riferimento esclusivamente alle combinazioni presenti sugli oggetti di catalogo KO che saranno implementate in 2.1. Le ulteriori regole potranno essere gestite come CR in rilasci successivi su richiesta.

## Linee

Di seguito la schematizzazione dei rapporti gerarchici tra asset di linee.

Figura : Gerarchie Linee Aeree

Figura 3: Gerarchie Linee in Cavo

## Stazioni

Di seguito la schematizzazione dei rapporti gerarchici tra asset di stazione

## Combinazione tra regole e requisiti

Per meglio comprendere come le regole descritte fino a questo punto permettono di gestire i requisiti espressi, di seguito viene riportato il dettaglio per i singoli requisiti:

Rilevabilità Difformità/Interferenza dipendente da attributi su altri asset: sul catalogo difformità/interferenze viene indicato come parametro di rilevabilità un attributo che non fa riferimento all’asset a cui è associato l’oggetto a catalogo. Per leggere il valore dell’attributo in fase di rilevazione difformità/interferenza, il sistema individua l’asset su cui leggere tramite la regola definita. Sulla base del valore che rileva, permette la selezione o meno della difformità.

Policy che hanno in input un parametro che riferisce ad un asset differente da quello su cui è definita la policy: gli input alla policy potranno essere riferiti ad elementi differenti da quello indicato nella policy e a cui fa riferimento l’attività in output (sempre univoco). A seconda dell’input selezionato e sulla base degli asset associati alla policy, il sistema dovrà leggere sull’asset relativo all’input l’informazione da utilizzare nell’elaborazione della policy stessa per determinare la generazione dell’ordine sull’asset output. L’asset da cui leggere è individuabile tramite le regole già descritte partendo dall’asset su cui la policy sta elaborando cioè quello sul quale va generato l’ordine (asset output).

Policy che sono definite su un asset differente da quello di output: questa casistica dovrà essere bonificata a catalogo (come concordato) perché le policy dovranno fare sempre riferimento a un solo elemento in output e 1 o n in input. L’asset associato alla policy è quindi l’asset a cui fa riferimento la macro-attività scelta come output, gli input potranno fare riferimento ad uno o più asset diversi da quello output (di riferimento della policy) come descritto nel punto precedente. Il sistema non gestisce policy che si riferiscono ad un asset diverso da quello della macro-attività in output.

L'attributo per determinare la prevalenza per metodo è definito su asset differente da quello dell'attività: in fase di generazione ordine da parte del DSS, partendo dall’asset sul quale si sta aprendo l’ordine conoscendo le relazioni tra questo e gli altri asset è possibile identificare univocamente l’asset da cui va letto l’attributo per la scelta del metodo. L’attributo selezionabile è sempre solo uno.

Rilevabilità Difformità/Interferenza dipendente da attività definita su altri asset: sulla base delle regole di associazione univoca tra gli asset, in fase di esecuzione di un ordine è possibile associare difformità anche ad altri asset relazionati univocamente all’asset a cui fa riferimento l’ordine.

Attività la cui esecuzione risolve difformità su livelli gerarchici diversi da quelli previsti in rilascio #1: in fase di esecuzione, sulla base delle relazioni tra gli asset si individuano univocamente gli asset associati all’asset relativo all’ordine in chiusura sui quali risolvere le difformità (indicate a catalogo come risolvibili).

## Adeguamenti UI e software

Per gestire le regole descritte in precedenza, dovrà essere adeguata la maschera e il software per permettere agli utenti di configurare le relazioni (e quindi le regole) che il sistema deve applicare per recuperare i dati necessari alle logiche. Il dettaglio di seguito è suddiviso per singola applicazione di catalogo.

### Modifiche catalogo Policy

Nella maschera del catalogo policy si aggiunge una tabella indicata con il nome “Elementi asset Input policy”. Nella tabella possono essere aggiunti uno o più elementi a cui faranno riferimento gli input alla policy. I campi sono:

Elemento asset: selezionato dall’utente rappresenta l’elemento da cui leggere l’informazione input durante l’elaborazione della policy. La lista mostrata dipende dall’asset a cui fa riferimento la policy; infatti, possono essere selezionati asset per i quali è implementata a sistema la regola di relazione con l’asset di riferimento della policy (definite nel file allegato al capitolo 8);

Descrizione: descrizione dell’asset popolato automaticamente dal sistema;

Regola: popolato automaticamente dal sistema sulla base dell’elemento scelto, non modificabile.

Figura : Tabella di inserimento elementi per input

All’interno della tabella di selezione dell’input alla policy deve essere associato l’asset a cui fa riferimento l’input della riga. L’elemento è il primo campo ad essere selezionato e si sceglie dalla lista di elementi inseriti nella tabella di elementi per input. Il sistema può quindi pre-filtrare gli altri campi sulla base dell’elemento selezionato. Di seguito una mock della schermata adeguata.

Figura : Tabella input policy adeguata

### Catalogo difformità

Nella maschera del catalogo difformità si aggiunge una tabella indicata con il nome “Elemento caratteristiche asset”. Nella tabella possono essere aggiunti uno o più elementi a cui faranno riferimento gli attributi per i quali il sistema deve valutare la rilevabilità della difformità. La tabella contiene i seguenti campi:

Elemento asset: selezionato dall’utente rappresenta l’elemento da cui leggere il valore dell’attributo in fase di rilevazione difformità. La lista mostrata dipende dall’asset a cui fa riferimento la difformità; infatti, possono essere selezionati asset per i quali è implementata a sistema la regola di relazione con l’asset di riferimento della difformità (definite nel file allegato al capitolo 8);

Descrizione: descrizione dell’asset popolato automaticamente dal sistema;

Regola: popolato automaticamente dal sistema sulla base dell’elemento scelto, non modificabile.

Figura : Tabella elemento caratteristiche asset

All’interno della tabella caratteristiche asset deve essere aggiunto un campo che permette all’utente di selezionare l’asset (tra quelli inseriti nella tabella “Elemento caratteristiche asset”) da cui il sistema deve leggere il valore dell’attributo per determinare la rilevabilità della difformità. L’asset deve essere il primo campo popolato per permettere al sistema di pre-filtrare gli altri campi in funzione dell’asset scelto. Una volta selezionato l’asset, la caratteristica può essere scelta tra gli attributi dell’asset indicato (compresi dei campi di testata, esclusivamente quelli riportati nell’elenco già implementato per R2.0) e il relativo valore (tramite il campo “valore”).

Figura : Tabella caratteristiche asset adeguata

A livello software, il sistema in fase di rilevazione dovrà proporre all’utente una lista di difformità rilevabili che sarà filtrata sulla base di:

asset a cui si sta associando;

ordine che si sta eseguendo;

caratteristiche degli asset indicate nella tabella. Per determinare da quale asset va letta la caratteristica il sistema si baserà sulle regole definite in precedenza nel documento e scelte in fase di configurazione del catalogo.

### Catalogo interferenze

Nella maschera del catalogo interferenze si aggiunge una tabella indicata con il nome “Elemento caratteristiche asset”. Nella tabella possono essere aggiunti uno o più elementi a cui faranno riferimento gli attributi per i quali il sistema deve valutare la rilevabilità dell’interferenza. La tabella contiene i seguenti campi:

Elemento asset: selezionato dall’utente rappresenta l’elemento da cui leggere il valore dell’attributo in fase di rilevazione interferenza. La lista mostrata dipende dall’asset a cui fa riferimento l’interferenza; infatti, possono essere selezionati asset per i quali è implementata a sistema la regola di relazione con l’asset di riferimento dell’interferenza (definite nel file allegato al capitolo 8);

Descrizione: descrizione dell’asset popolato automaticamente dal sistema;

Regola: popolato automaticamente dal sistema sulla base dell’elemento scelto, non modificabile.

Figura : Tabella elemento caratteristiche asset

All’interno della tabella caratteristiche asset deve essere aggiunto un campo che permette all’utente di selezionare l’asset (tra quelli inseriti nella tabella “Elemento caratteristiche asset”) da cui il sistema deve leggere il valore dell’attributo per determinare la rilevabilità dell’interferenza. L’asset deve essere il primo campo popolato per permettere al sistema di pre-filtrare gli altri campi in funzione dell’asset scelto. Una volta selezionato l’asset, la caratteristica può essere scelta tra gli attributi dell’asset indicato

(comprensivi dei campi di testata) e il relativo valore (tramite il campo “valore”).

Figura : Tabella caratteristiche asset adeguata

A livello software, il sistema in fase di rilevazione dovrà proporre all’utente una lista di interferenze rilevabili che sarà filtrata sulla base di:

asset a cui si sta associando

ordine che si sta eseguendo

caratteristiche degli asset indicate nella tabella. Per determinare da quale asset va letta la caratteristica il sistema si baserà sulle regole definite in precedenza nel documento e scelte in fase di configurazione del catalogo.

### Modifiche catalogo Attività

#### Selezione metodo da attributo su altro asset

Nella maschera del catalogo attività va aggiunta una tabella denominata “Elemento asset relazionato per metodo” che riporta gli asset da cui possono essere scelti gli attributi per scelta metodo. Nella tabella possono essere inseriti uno o più elementi asset. Sulla base dell’asset associato all’attività, la lista degli elementi selezionabili nella tabella è pre-filtrata in funzione delle relazioni implementate (come descritto nel capitolo 3, presenti nel file allegato al capitolo 8). La tabella riporta i seguenti campi:

Elemento asset: popolato tramite lista valori selezionandolo tra gli asset la cui relazione con l’asset associato all’attività è implementata;

Descrizione: popolato automaticamente riporta la descrizione dell’elemento asset;

Regola: popolato automaticamente dal sistema sulla base dell’elemento scelto, non modificabile.

Figura : Tabella asset relazionato per metodo

Nella tabella relativa alla selezione dell’attributo per selezione metodo è aggiunto un campo che fa riferimento all’elemento asset su cui risiede l’attributo che il DSS deve leggere per determinare quale PdL va associato all’ordine in fase di creazione. L’asset può essere scelto dalla lista degli elementi inseriti in precedenza nella tabella “Elemento asset relazionato” e va selezionato prima degli altri campi. In questo modo il sistema può pre-filtrare l’attributo e il relativo valore in funzione dell’asset scelto. È possibile inserire più di un attributo sul quale il sistema deve ragionare per scegliere il PdL. È possibile selezionare un solo attributo per la scelta del metodo.

Figura 13: Tabella attributi per selezione metodo adeguata

#### Rilevabilità/risolvibilità difformità/interferenze su altro asset

È anche presente una tabella per la selezione dell’asset a cui fanno riferimento le difformità/interferenze rilevabili/risolvibili con il PdL. La tabella è denominata “Elemento asset relazionato difformità/interferenza” e riporta le seguenti colonne:

Elemento asset: popolato tramite lista valori selezionandolo tra gli asset la cui relazione con l’asset associato all’attività è implementata;

Descrizione: popolato automaticamente riporta la descrizione dell’elemento asset;

Regola: popolato automaticamente dal sistema sulla base dell’elemento scelto, non modificabile

Figura : Tabella asset relazionato per difformità/interferenze

Nella tabella relativa alla selezione delle difformità/interferenze risolte deve essere aggiunto il campo che fa riferimento all’elemento asset a cui la difformità/interferenza è riferita. Questo campo deve essere il primo selezionato in fase di inserimento per permettere al sistema di pre-filtrare gli altri campi sulla base dell’elemento scelto. La lista valori dell’elemento corrisponde a quelli indicati nella tabella elemento asse relazionato per difformità/interferenze.

Figura : Tabella associazione difformità e interferenze adeguata

In termini di software, il sistema in fase di chiusura ordini associati al PdL in oggetto, verificare tutte le difformità associate al PdL come risolvibili e identificare gli asset a cui sono associate sulla base dell’asset su cui insiste il PdL e applicando le regole definite a catalogo (esplicitate nel documento).

Nella tabella per la selezione delle difformità/interferenze rilevabili, deve essere aggiunto il campo che fa riferimento all’elemento asset a cui la difformità/interferenza è riferita. Questo campo deve essere il primo selezionato in fase di inserimento per permettere al sistema di pre-filtrare gli altri campi sulla base dell’elemento scelto. La lista valori dell’elemento corrisponde a quelli indicati nella tabella elemento asse relazionato.

Figura : Tabella difformità interferenze rilevabili adeguata

In termini di software, il sistema in fase di esecuzione ordine dovrà permettere la rilevazione di difformità non solo sull’asset oggetto dell’ordine ma anche su altri asset (a cui sono associate le difformità rilevabili dall’ordine) relazionati tramite le regole identificate all’asset oggetto dell’ordine. Tramite le regole è possibile individuare l’asset specifico su cui è possibile rilevare le difformità durante l’esecuzione dell’ordine partendo dall’asset su cui l’ordine insiste.

# Gestione prevalenza non iso-asset

Non sono previsti ulteriori sviluppi per gestire questo requisito. Infatti, attualmente è già prevista la gestione di prevalenza non iso-livello per specifiche casistiche:

Su asset linea, per ordini/attività associate ad asset di livello gerarchico terzo e quarto livello. In dettaglio sarà valutata per ordini/attività associate ad asset padre (terzo livello) se si sta creando un ordine su asset di quarto livello. Per ordini/attività associate ad asset figli (quarto livello) se si sta creando un ordine su asset di terzo livello;

Su asset stazioni, per ordini/attività associate ad asset di quarto livello su altri asset di quarto livello con lo stesso asset parent.

Il requisito fa riferimento in particolare alla casistica KO relativa ad asset quarto livello MACCHINA di stazione. Attualmente a sistema è già prevista l’applicazione delle logiche di prevalenza tra asset di quarto livello fratelli sotto lo stesso terzo livello stazione come descritto al secondo punto della lista sopra. La casistica di sostituzione MACCHINA X è quindi già coperta tramite l’applicazione di logiche di prevalenza su quarti livelli sotto lo stesso terzo livello. Si specifica che, in tutti i casi, per permettere l’applicazione corretta delle regole di prevalenza devono essere definite le associazioni di PdL prevalenti sul catalogo attività.

# Gestione variabili KO

Le variabili KO negli attuali cataloghi, input a policy KO devono essere gestite con formule di calcolo specifiche implementate ad hoc per la gestione della singola variabile. Di seguito il dettaglio.

VAR.1.1.0.2: La logica permette di calcolare su CONDUTTORI terzo livello il numero di GIUNTI CONDUTTORE sotto al terzo CONDUTTORI;

VAR.2.1.0.1: La logica permette di calcolare su ATTACCHI CONDUTTORE terzo livello il numero di AMARRI per campata come numero di ARMAMENTO CONDUTTORE (4° livello) che hanno attributo LIN_TIPO_ATTACCO = O sotto al terzo ATTACCHI CONDUTTORE;

VAR.L.1.2.1.7: la variabile calcola il numero di difformità aperte sulla FUNE DI GUARDIA contenute in questo elenco: Diff.L.1.2.1.1; Diff.L.1.2.1.2; Diff.L.1.2.1.3; Diff.L.1.2.1.4; Diff.L.1.2.1.5; Diff.L.1.2.1.6; Diff.L.1.2.1.7; Diff.L.1.2.1.8; Diff.L.1.2.1.9; Diff.L.1.2.1.10 (con possibilità di modificare la lista delle difformità riportata);

VAR.S.49.29: alla variabile viene associato il valore dell’attributo STZ_CORRENTE_GUASTO_CTOCTO_NODO della sezione gerarchicamente superiore all’asset SEZIONATORE AIS.

VAR.S.56.12: calcolata come il rapporto percentuale tra due attributi anagrafici STZ_CORRENTE_GUASTO_CTOCTO_NODO e STZ_POTERE_INTERRUZIONE_CTOCTO

VAR.S.85.9: Il calcolo della variabile avviene tramite la formula  dove X corrisponde al valore della misura VAR.S.85.8 con data minore tra quelle rilevate sull’asset (andando indietro nel tempo).

VAR.S.50.2: il sistema assegna valore 1 alla variabile se viene rilevato che la gerarchia del TV AIS è Stallo di parallelo/Sbarra. Assegna 0 in caso contrario.

VAR.S.49.56: la variabile viene calcolata come numero di fasi diverse sulle quali sono aperti ordini con PdL AT.S.49.13 sul SEZIONATORE AIS. Le fasi si contano verificando direttamente l’attributo fase associato all’ordine sul SEZIONATORE AIS.

La formula di calcolo FORMULATESTOLIBERO continuerà ad ammettere come tipi parametri in input esclusivamente variabili e soglie.

# Associazione diretta tra ordine e difformità che lo ha generato con eventuale dettaglio per fase

(in attesa di requisito)

# Policy KO non incluse nei cluster elencati

Alcune policy KO non sono risolte attraverso le soluzioni nei capitoli precedenti e necessitano di azioni specifiche sulla policy per renderle gestibili in 2.1.

P.S.48.2 – La policy ha in input il TdG che non è in scope per 2.1 quindi la policy deve essere rivista con un input gestibile;

P.S.51.1 – La policy ha in input il TdG che non è in scope per 2.1 quindi la policy deve essere rivista con un input gestibile;

P.S.50.37 – Una delle variabili (VAR.S.50.11) input alla policy è stata cancellata, va corretta l’impostazione della policy;

P.S.50.38 – Una delle variabili (VAR.S.50.11) input alla policy è stata cancellata, va corretta l’impostazione della policy;

# Tabella di associazione cluster KO soluzione

| Cluster | Catalogo | # oggetti KO | Soluzione |
| --- | --- | --- | --- |
| Policy che hanno in input un parametro che riferisce ad un asset differente da quello su cui è definita la policy | Policy | 67 | Definizione di relazioni tra asset |
| Policy che sono definite su un asset differente da quello di output | Policy | 8 | Bonifica catalogo da ricondurre a: Definizione di relazioni tra asset |
| Attributo in input alla variabile | Misure e variabili di stato | 2 | Gestione variabili KO |
| Conteggio di oggetti anagrafici | Misure e variabili di stato | 2 | Gestione variabili KO |
| HI in input alla variabile | Misure e variabili di stato | 1 | Bonifica cataloghi |
| No input/no logica | Misure e variabili di stato | 1 | Bonifica cataloghi |
| Variabile in input su altro asset | Misure e variabili di stato | 8 | Gestione variabili KO |
| Rilevabilità Difformità/Interferenza dipendente da attributi su altri asset | Difformità/ Interferenze | 134 | Definizione di relazioni tra asset |
| Misura in input alla Difformità definita su altro asset | Difformità/ Interferenze | 180 | Bonifica cataloghi |
| Gestione prevalenza per attività definita su livelli gerarchici diversi da quelli previsti in rilascio #1 | Attività | 76 | Gestione prevalenza non iso-asset |
| L'attributo per determinare la prevalenza per metodo è definito su asset differente da quello dell'attività | Attività | 66 | Definizione di relazioni tra asset |
| Rilevabilità Difformità/Interferenza dipendente da attività definita su altri asset | Attività | 34 | Definizione di relazioni tra asset |
| Attività la cui esecuzione risolve difformità su livelli gerarchici diversi da quelli previsti in rilascio #1 | Attività | 15 | Definizione di relazioni tra asset |

# Allegati