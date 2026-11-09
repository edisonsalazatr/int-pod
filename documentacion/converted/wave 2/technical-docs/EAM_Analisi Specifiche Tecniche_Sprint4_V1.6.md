# EAM_Analisi Specifiche Tecniche_Sprint4_V1.6

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Specifica Tecnica – Sprint 4 |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data | Owner |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | ACN | 07/10/2024 |  |
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

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche, già descritte ad alto livello nel documento “EAM_Analisi Funzionale_Sprint_4_V.docx”, verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software.

Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni implementate sulla piattaforma EAM di Terna (ambiente Dev) e gli sviluppi custom da implementare. Ciascun paragrafo (dedicato a una specifica funzionalità nell’applicativo) presenta due sotto-paragrafi: nel primo sono illustrate le configurazioni eseguite sul software (e.g., estensione del modello dati, configurazione dell’applicazione, gestione della condizionalità, etc.), nel secondo gli sviluppi necessari per soddisfare le funzionalità richieste durante lo sprint.

In particolare, i temi inclusi nello Sprint 4 sono i seguenti:

Catalogo delle policy

Centro di controllo

Generazione input tramite eventi fuori processo

Area di validazione

Gestione indicatori di condizione

Recupero/alimentazione da fonti esterne: ELIVE

Recupero/alimentazione da fonti esterne: CAVI INTERRATI.

L’analisi tecnica DSS (come di tutti i moduli not embedded) sarà contenuta in un documento dedicato.

# Sprint 4: Policy e DSS

## Catalogo delle policy

Le Policy sono le regole utilizzate dal DSS per l’elaborazione (generazione/modifica/cambio stato) degli OdL. Quest’ultime sono caratterizzate da Input e da Output, che determinano rispettivamente l’esecuzione della policy e il risultato da generare; nel dettaglio, si distinguono due macrocategorie:

Policy elementari, che presentano un singolo input

Policy combinate, che presentano molteplici input.

### 5.1.1 Configurazioni

Per la gestione dei dati inerenti al catalogo delle policy è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito viene riportato in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4POLICY | DATAFINEVAL | Data fine validità | DATETIME |  |
| X4POLICY | DATAINIZIOVAL | Data inizio validità | DATETIME |  |
| X4POLICY | X4POLICYID |  | BIGINT |  |
| X4POLICY | CHANGEDBY | Modificato da | UPPER | 100 |
| X4POLICY | CHANGEDDATE | Data modifica | DATETIME |  |
|  |  |  |  |  |
|  |  |  |  |  |
| X4POLICY | DIFFERIBILITA | Differibilità | INT |  |
| X4POLICY | FREQUENZA | Frequenza | INT |  |
| X4POLICY |  | IDttività Output |  |  |
| X4POLICY | IDPOLICY | ID Policy | ALN | 20 |
| X4POLICY | IDTIMING | ID Timing | ALN | 3 |
| X4POLICY | LASTREVIEWDATE | Data ultima revisione | DATETIME |  |
| X4POLICY | MAXANTICIPO | Massimo anticipo | INT |  |
| X4POLICY | MAXPOSTICIPO | Massimo posticipo | INT |  |
| X4POLICY | STAT | Stato | ALN | 12 |
| X4POLICY | TIPOLOGIAASSET | Asset | UPPER | 192 |
| X4POLICY | LIVPOLICY | Livello Policy | INT |  |
| X4POLICY | LIVCOMB | Livello combinazione input | INT |  |
| X4POLICY | X4RIFTEMPORALE | Riferimento temporale | ALN |  |
| X4POLICY | TIPOPOLICY | Tipo Policy | ALN | 15 |
| X4POLICY | POLICYAGGR | Policy aggregata | YORN |  |
| X4POLICY | LIVAGGR | Livello aggregazione policy | INT |  |
| X4POLICY | COMBINPUT | Combinazione input (formula) | UPPER | 1000 |
| X4POLICY | APARTIREDA | A partire da | DATETIME |  |
| X4POLICY | X4CLUSTER | Cluster | ALN | 50 |
| X4POLICY | X4SOTTOPROGRAMMA | Sotto-programma | ALN | 50 |
| X4POLICY | ELEGGIBILITA | Eleggibilità | YORN |  |
| X4POLICYINPUT | DESCRIPTION | Descrizione | ALN | 50 |
| X4POLICYINPUT | IDINPUT | ID input | ALN | 25 |
|  |  |  |  |  |
| X4POLICYINPUT | IDPOLICY | ID policy | ALN | 20 |
| X4POLICYINPUT | NINPUT | N Input | ALN | 20 |
| X4POLICYINPUT | TARGETRAFFRONTO | Target raffronto | ALN | 15 |
| X4POLICYINPUT | TIPOLOGIAINPUT | Tipologia input | ALN | 20 |
| X4POLICYINPUT | IDMISURA | ID misura | ALN | 25 |
| X4POLICYINPUT | OPERATORE | Operatore logico | ALN |  |
| X4POLICYINPUT | X4AND | AND | ALN | 20 |
| X4POLICYINPUT | X4OR | OR | ALN | 20 |
| X4POLICYINPUT | X4EQUAL | Uguale | ALN | 20 |
| X4POLICYINPUT | X4NOTEQUAL | Diverso | ALN | 20 |
| X4POLICYINPUT | TARGETNUM | Target di raffronto numerico | FLOAT |  |
| X4POLICYINPUT | LIVINPUT | Livello input | INT |  |
|  |  |  |  |  |
| X4POLICYCLASSIFICATIONS | CLASSIFICATIONID | Classificazione | UPPER | 192 |
| X4POLICYCLASSIFICATIONS | X4POLICYID | X4policyid | BIGINT |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

Per permettere la visione dei campi necessari alla gestione delle policy si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4POLICY”, si dovranno configurare:

I campi di testata del tab “Policy”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Policy | IDPOLICY |  |  |  |
| Elemento asset | TIPOLOGIAASSET |  | CLASSIFICATION |  |
| Classificazioni asset | X4POLICYCLASSIFICATIONS.CLASSIFICATIONID |  |  |  |
| Gerarchia asset associato | CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| ID Timing | IDTIMING |  |  |  |
| Livello policy | LIVPOLICY |  |  |  |
| A partire da | APARTIREDA |  |  | X4PPERIODICA |
| Differibilità | DIFFERIBILITA |  |  | X4PONCONDITION |
| Max anticipo | MAXANTICIPO |  |  |  |
| Max posticipo | MAXPOSTICIPO |  |  |  |
| Stato | STATO |  |  |  |
| Livello combinazione input | LIVCOMB |  |  |  |
| ID PdL in output |  |  |  |  |
| Frequenza | FREQUENZA |  |  | X4PPERIODICA |
| Riferimento temporale | X4RIFTEMPORALE |  | VALUELIST |  |
| Data inizio validità | DATAINIZIOVAL |  | DATELOOKUP |  |
| Data fine validità | DATAFINEVAL |  | DATELOOKUP |  |
| Tipo policy | TIPOPOLICY |  | X4TIPOPOLICY |  |
| Policy aggregata | POLICYAGGR |  |  |  |
| Livello di aggregazione | LIVAGGR |  | X4LIVAGGR |  |
| Combinazione input | COMBINPUT |  |  |  |
| Sotto-programma | X4SOTTOPROGRAMMA |  | … |  |
| Cluster | X4CLUSTER |  | VALUELIST |  |
| Eleggibilità | ELEGGIBILITA |  |  |  |

La sigoption “X4PONCONDITION” rende visibili i campi relativi alle policy on-condition (TIPOPOLICY = On-condition); la sigoption “X4PPERIODICA” rende visibili i campi relativi a quelle periodiche (TIPOPOLICY = Periodica). La relazione “X4POLICYCLASSIFICATIONS” lega le tabelle X4POLICY e X4POLICYCLASSIFICATIONS (la tabella di relazione tra policy e classificazioni) tramite :X4POLICYID=X4POLICYID. Inoltre, di seguito riportiamo i domini associati ai campi e i loro valori ammissibili:

Livello combinazione input (X4LIVCOMB): 0, 1, 2, 3

Riferimento temporale (X4RIFTEMPORALE): G, S, M, A

Tipo di policy (X4TIPOPOLICY): On-condition, Periodica

Livello di aggregazione (X4LIVAGGR): 1, 2, 3

Operatore (X4OPLOGICO): >, >=, <, <=, =, ≠, PariOrDispari, EXIST, NOT EXIST

I campi della tabella “Input”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| N° input | NINPUT |  |  |  |
| Tipologia | TIPOLOGIAINPUT |  | X4TIPOINPUT |  |
| ID Input | IDINPUT |  |  |  |
|  |  |  |  |  |
| Misura | IDMISURA |  |  |  |
| Operatore/funzione | OPERATORE |  | X4OPLOGICO |  |
|  |  |  |  |  |
| Target raffronto | TARGETRAFFRONTO |  | VALUELIST |  |
| Target di raffronto numerico | TARGETNUM |  |  |  |

I campi della tabella “Combinazione input 1° livello”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| N° input | NINPUT |  |  |  |
| & | X4AND |  | X4INPUT |  |
| OR | X4OR |  | X4INPUT |  |
| = | X4EQUAL |  | X4INPUT |  |
| <> | X4NOTEQUAL |  | X4INPUT |  |

I campi della tabella “Combinazione input 2° livello”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |

| N° input | NINPUT |  |  |  |
| --- | --- | --- | --- | --- |
| & | X4AND |  | X4INPUT |  |
| OR | X4OR |  | X4INPUT |  |
| = | X4EQUAL |  | X4INPUT |  |
| <> | X4NOTEQUAL |  | X4INPUT |  |

I campi della tabella “Combinazione input 3° livello”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |

| N° input | NINPUT |  |  |  |
| --- | --- | --- | --- | --- |
| & | X4AND |  | X4INPUT |  |
| OR | X4OR |  | X4INPUT |  |
| = | X4EQUAL |  | X4INPUT |  |
| <> | X4NOTEQUAL |  | X4INPUT |  |

Queste tabelle saranno mostrate mediante relazione per i vari livelli di combinazione input. In particolare, per il livello zero la relazione X4INPUTPOLICY avrà la condizione (IDPOLICY=:X4POLICYID and LIVINPUT is null), per il primo livello la relazione X4INPUTPOLICYLIV1 avrà la condizione (IDPOLICY=:X4POLICYID and LIVINPUT='1’), per il secondo livello la relazione X4INPUTPOLICYLIV2 avrà la condizione (IDPOLICY=:X4POLICYID and LIVINPUT='2’) e per il livello terzo livello la relazione X4INPUTPOLICYLIV3 avrà la condizione (IDPOLICY=:X4POLICYID and LIVINPUT='3’).

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

Pre-filtraggio catalogo policy

L’applicazione di gestione delle policy (X4POLICY) viene fornita di un totale di 3 query predefinite necessarie per filtrare il contenuto della tabella secondo il criterio di tipologia asset a cui viene applicata la policy. Nel dettaglio le query filtreranno sull’attributo TIPOLOGIAASSET per estrarre tutti i record di policy applicabili rispettivamente a Linee aeree, in cavo e stazioni. Nel dettaglio verrà impiegata la tabella standard di sistema CLASSANCESTOR – tabella nella quale viene stabilito per ogni classificazione l’intero albero gerarchico - per definire a quale classificazione padre (nel caso di policy per i quarti livelli) la policy è stata definita.

Dominio stati policy

È stato creato un nuovo dominio denominato X4POLICYSTATUS contenente al suo interno i quattro stati caratterizzanti la policy: BOZZA, PENDING REVIEW, ATTIVA, NON ATTIVA.

Inserimento nuovo attributo zonizzazione degli asset

Nelle classificazioni ove presente l’attributo di zonizzazione sar inserit “” per permettere la zonizzazione

Tale informazione sarà presa in input dal DSS: nel caso di generazione di OdL che ha come anno di riferimento quello in corso, l’attributo da cui prendere il valore della zonizzazione sarà “LIN_ZONA_AMBIENTALE”;

### 5.1.2 Sviluppi

Per permettere la visualizzazione/valorizzazione dei campi delle policy, si prevedono i seguenti sviluppi a sistema:

Valorizzazione campi catalogo policy

La valorizzazione dei campi automatici o necessari di una particolare customizzazione del catalogo policy è definita come segue:

IDPOLICY, automatico al primo salvataggio; il valore verrà generato da un automation script sulla base di una regola fornita dal BSN ()

Classificazioni asset, automatico in base al campo TIPOLOGIAASSET selezionato

Gerarchia asset associato, automaticamente sulla base del livello asset associato; viene definito mediante relazione, prelevando il campo “HIERARCHYPATH” della classificazione asset inserita per la policy

LIVPOLICY, automatico in base al livello anagrafico associato

IDTIMING, automatico concatenando il campo FREQUENZA o DIFFERIBILITA con il campo X4RIFTEMPORALE

MAXPOSTICIPO, manuale e determina la valorizzazione del campo “Data di massimo posticipo” dell’OdL, ovvero FNLCONSTRAINT di WORKORDER dovrà essere uguale alla somma di X4DATAMODELLO (WORKORDER) e MAXPOSTICIPO (X4POLICY)

MAXANTICIPO, non modificabile e vuoto nel caso di policy periodiche con FREQUENZA <= 1 anno (la data “Non prima di”, ovvero SNECONSTRAINT di WORKORDER, assumerà il valore del primo giorno del periodo); modificabile manualmente e obbligatorio nel caso di policy periodiche con FREQUENZA > 1 anno e on-condition (la data “Non prima di”, ovvero SNECONSTRAINT di WORKORDER, verrà calcolata come la differenza di X4DATAMODELLO (WORKORDER) e MAXANTICIPO (X4POLICY)

APARTIREDA, modificabile manualmente dall’utente con date successive alla data del sistema SYSDATE

, il campo accetta dei valori di PdL sulla base del tipo di asset inserito nella policy; la dialog di selezione

NINPUT, automatico con valore crescente (nella tabella “Input”)

COMBINPUT, formula generata automaticamente dal sistema a seguito della compilazione dell’utente dell’ultima tabella di combinazione input della policy. I codici di input definiti all’interno della tabella degli input policy verranno riportati in un formato stringa e concatenati tra loro utilizzando i classici operatori logici (AND, OR; =; !=, e simili). Un automation script si occuperà, una volta completata la policy e le relazioni tra gli input della policy, di relazionare i codici input tra loro utilizzando la sintassi sopra descritta. Nel dettaglio gli input elementari (quelli con LIVINPUT nullo) vengono aggiunti ad una lista degli elementi base.

Le combinazioni (quelle con LIVINPUT non nullo) verranno aggiunte alla lista delle combinazioni, con priorità a quelle con livelli più alti (LIVINPUT maggiore). L’automation script combinazione del livello più alto e costruirà una stringa che rappresenta l'intera combinazione degli input.

Viene gestita la gerarchia delle combinazioni usando parentesi per raggruppare correttamente i livelli (es. (1 AND 2) OR (3 AND 4)).

Se ci sono combinazioni a più livelli, queste vengono concatenate nel campo COMBINPUT della policy.

Visualizzazione e vincoli tabelle combinazioni input

In base al campo LIVCOMB di X4POLICY saranno mostrate le tabelle relative alle combinazioni degli Input; nel dettaglio, il numero di tabelle sarà pari al numero inserito nel campo (da 0 a 3). La modifica del campo LIVCOMB sarà inibita all’utente se le tabelle risultano valorizzate (e.g., se LIVCOMB = 2, l’utente dovrà svuotare il contenuto della tabella “Combinazione input 2° livello” e poi modificare LIVCOMB in 1). Nel caso in cui LIVCOMB = 0, il sistema deve consentire l’inserimento di un solo NINPUT nella tabella “Input”. I check verranno effettuati da un automation script che verificherà che dato un IDPOLICY della tabella X4POLICYINPUT vi sia un solo input nel caso di policy con LIVCOMB=0; nel caso di modifica del livello LIVCOMB invece verificherà la presenza di righe di X4POLICYINPUT con LIVINPUT maggiori del valore LIVCOMB: in caso ne esistano mostrerà un errore all’utente.

Un automation script inoltre verificherà che solo uno dei campi X4AND, X4OR, X4EQUAL, X4NOTEQUAL venga popolato per singola riga di X4INPUTPOLICY.

Liste valori tabelle combinazioni input

I campi contrassegnati con gli operatori logici (i.e., X4AND, X4OR, X4EQUAL, X4NOTEQUAL) saranno a inserimento manuale tramite lista valori (X4INPUT). Quest’ultima dovrà variare di livello in livello al fine di non garantire la possibilità di inserire un input (della tabella “Input”) già selezionato in precedenza. I domini nelle tabelle di combinazione input sono stati configurati come segue:

Il dominio richiamato nella tabella di combinazione di primo livello mostrerà come input ammissibili i “N. Input" di tutti gli input definiti all’interno della tabella “Input policy”

Il dominio richiamato nella tabella di combinazione di secondo livello mostrerà come input ammissibili gli ID univoci delle righe popolate nella tabella di input di primo livello;

Il dominio richiamato nella tabella di combinazione di terzo livello mostrerà come input ammissibili gli ID univoci delle righe popolate nella tabella di input di secondo livello;

Gestione valori ammissibili input policy

Il campo TIPOLOGIAINPUT definito nella tabella di Input della policy ammetterà come possibili valori soltanto i seguenti: Health Index, Attributi anagrafici, Difformità, Misure, Variabili di stato, Attività (PdL), Interferenze, Opere interferenti (definiti nel dominio X4TIPOINPUT).

Sulla base di questo campo e sulla base del(classificazione) di asset inserito in testata nella policy, il sistema pre-filtrerà le possibili voci selezionabili del campo IDINPUT, mostrando le rispettive voci ammissibili (verrà filtrata la rispettiva tabella di catalogo sulla base della classificazione dell’asset selezionato nella testata della policy).

Nel caso in cui si selezioni una difformità come input e questa difformità prevede anche una registrazione di una misura sarà modificabile la colonna IDMISURA della tabella (pre-filtrata in base a IDINPUT). Sarà dunque possibile selezionare una misura relazionata alla difformità selezionata e nei successivi campi OPERATORE e TARGETRAFFRONTO (pre-filtrato in base a TIPOLOGIAINPUT) specificarne come questa misura dovrà essere valutata dal DSS.

Selezione multipla elementi in input

Affinché sia possibile selezionare N. input nella sezione dedicata alle combinazioni di primo, secondo e terzo livello delle policy, si rende necessario la creazione di un automation script che, una volta premuto il tasto “OK” della dialog di selezione input, ciclando per tutti gli item selezionati, concateni l’attributo NINPUT degli elementi selezionati utilizzando il carattere “;” come separatore.

Per permettere la gestione delle policy aggregate, si prevede il seguente sviluppo a sistema:

Flag “Policy aggregata?”

Per definire una policy aggregata il sistema viene configurato con la presenza di un flag apposto in testata alla policy denominato “Policy aggregata”. Inoltre, per la gestione della regola che dovrà far triggerare una policy aggregata, sarà necessario che l’utente selezioni come input il codice dell’HI in oggetto della valutazione da fare sull’aggregato e imposti come target il valore di soglia. Si rimanda al capitolo del DSS per il funzionamento e la gestione della policy aggregata al momento della valutazione e generazione dell’ordine.

Per permettere l’attivazione e la modifica di una policy, si prevedono i seguenti sviluppi a sistema:

Campi necessari per attivazione policy

Per l’attivazione di una Policy devono essere popolati i seguenti campi obbligatori:

Riferimento asset (TIPOLOGIAASSET)

Tipo policy (TIPOPOLICY)

Riferimento temporale (X4RIFTEMPORALE)

Frequenza/Differibilità (FREQUENZA/DIFFERIBILITA)

Massimo posticipo attività (MAXPOSTICIPO)

Massimo anticipo attività (MAXANTICIPO)

ID in output (IDATTIVITA)

Livello combinazione input (LIVCOMB)

La tabella “Input”

Tabelle di combinazione input, se LIVCOMB > 0.

…

Inoltre, nel caso di IDINPUT caratterizzati da TIPOLOGIAINPUT “Health Index”, "Difformità", "Interferenza" o "Attività", quest’ultime dovranno essere in stato ATTIVA nei rispettivi cataloghi; mentre, nel caso di "Opere interferenti" lo stato (dell’asset) dovrà essere ESISTENTE.

Attivazione automatica policy

Quando la data di sistema (SYSDATE) raggiunge il giorno definito all’interno del campo DATAINIZIOVAL, il sistema prevederà un cambio automatico della Policy da “BOZZA” ad “ATTIVA” mediante la configurazione di un automation script. Ciò avverrà solo ed esclusivamente se i campi definiti necessari per la policy sono stati compilati dall’utente.

Attivazione manuale policy

Quando l’utente effettua manualmente il passaggio di stato di una policy da “BOZZA” a “ATTIVA” prima che la SYSDATE sia uguale alla DATAINIZIOVAL, quest'ultima dovrà essere sovrascritta mediante automation script dalla data corrente di attivazione. Ciò avverrà solo ed esclusivamente se i campi definiti necessari per la policy sono stati compilati dall’utente.

Modifica policy: passaggio manuale allo stato “Pending Review”

L’utente può effettuare il passaggio allo stato “Pending Review” manualmente tramite la funzionalità “Revisione policy” nelle azioni laterali. Conseguentemente, dalla maschera di dettaglio della Policy sarà possibile modificare tutti i campi, ad eccezione di IDPOLICY.

Attraverso il cambia stato, una volta modificati i campi, sarà possibile riportare lo stato della policy in ATTIVA. Dopo l’attivazione, le modifiche relative alle seguenti date della Policy vengono automaticamente propagate agli OdL esistenti in stato “APERTO” (ODL con X4POLICY=IDPOLICY del record di X4POLICY):

Frequenza (FREQUENZA)

Differibilità (DIFFERIBILITA)

Max anticipo (MAXANTICIPO)

Max posticipo (MAXPOSTICIPO)

Modifica policy: passaggio automatico allo stato “Pending Review”

Nel caso in cui uno dei possibili input di una policy venga modificato (es. Un elemento del catalogo “Difformità”), il sistema verificherà tutte le policy che hanno quell’elemento come input (nella tabella X4POLICYINPUT verificherà tutti gli elementi che hanno come TIPOLOGIAINPUT la tipologia che ha subito una modifica e come IDINPUT il singolo elemento che ha subito la modifica) e, per tutti i record di policy che hanno come input quell’elemento verrà impostato lo stato a PENDING REVIEW.

Visualizzazione storico policy

Viene abilitato l’audit su tutti i campi di input e di testata della policy; “Visualizza storico” mostrerà una vista contenente le seguenti colonne per ciascuna modifica: Stato (STAT), Modificato da (CHANGEBY) e Data (CHANGEDATE). Inoltre, espandendo la singola riga sarà possibile visualizzare tutti i dati della policy relativi alla CHANGEDATE selezionata. La tabella sarà scaricabile in formato Excel in qualsiasi momento.

## entro di controllo

Il Centro di Controllo di IBM Maximo è una dashboard personalizzabile che consente all’utente di avere una vista riassuntiva e un rapido accesso agli strumenti chiave dell’applicativo.

### 5.2.1 Configurazioni

Per la gestione dei dati inerenti alla corrispondenza tra asset e stati idonei è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito viene riportato in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4TABIDONEITA | TIPOLOGIAASSET | Classificazione asset | UPPER | 192 |
| X4TABIDONEITA | STATOIDONEO | Stato idoneo | ALN | 20 |
| X4TABECCEZIONI | IDATTIVITA | ID PdL | ALN | 15 |
| X4TABECCEZIONI | SITEID | Site | ALN | 8 |
| X4TABECCEZIONI | TIPOLOGIAASSET | Classificazione asset | UPPER | 192 |
| X4TABECCEZIONI | STATOIDONEO | Stato idoneo | ALN | 20 |
| X4TABTERZOLIVELLO | TIPO | Tipo | ALN | 10 |
| X4TABTERZOLIVELLO |  | Classificazione asset | UPPER | 192 |
|  |  |  |  |  |

Per permettere la visione dei campi necessari alla “Tabella di corrispondenza Asset – Stato idoneo” si dovrà prevedere la seguente configurazione a sistema. In particolare, :

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Classificazione asset | TIPOLOGIAASSET |  | CLASSIFICATION |  |
| Stato idoneo | STATOIDONEO |  | VALUELIST |  |

Il campo STATOIDONEO mostrerà solamente gli stati associati alla classificazione asset precedentemente selezionata (TIPOLOGIAASSET).

Per permettere la visione dei campi necessari alla “Tabella eccezioni” si dovrà prevedere la seguente configurazione a sistema. In particolare,

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| PdL | IDATTIVITA |  |  |  |

| Classificazione asset | TIPOLOGIAASSET |  | CLASSIFICATION |  |
| --- | --- | --- | --- | --- |

| Stato idoneo | STATOIDONEO |  | VALUELIST |  |
| --- | --- | --- | --- | --- |

Il campo TIPOLOGIAASSET mostrerà solamente gli stati associati al PdL precedentemente selezionato (IDATTIVITA); mentre, il campo STATOIDONEO risulterà filtrato solamente in base agli stati associati alla classificazione asset precedentemente selezionata (TIPOLOGIAASSET).

Per permettere la visione dei campi necessari alla “Tabella Asset di Terzo Livello” si dovrà prevedere la seguente configurazione a sistema. In particolare,

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Tipo | TIPO |  | X4TIPO |  |

| Classificazione 3° livello |  |  | CLASSIFICATION |  |
| --- | --- | --- | --- | --- |

| Classificazione 4° livello |  |  | CLASSIFICATION |  |
| --- | --- | --- | --- | --- |

Il campo TIPO sarà caratterizzato dal dominio “X4TIPO” (Linee, SPCC e UF); mentre, i campi “Classificazione 3° livello” e “Classificazione 4° livello” risulteranno filtrati in base al TIPO selezionato e, rispettivamente, in base al campo HIERARCHYLEVELS della tabella ASSETANCESTOR pari a 2 e 3.

### 5.2.2 Sviluppi

Per permettere la visualizzazione delle Policy e degli OdL a loro associati nel centro di controllo, si prevedono i seguenti sviluppi a sistema:

Policy in scadenza/scadute

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista riepilogativa delle Policy in scadenza/scadute; nel dettaglio, il testo dell’ID policy del result set in oggetto sarà colorato in arancione per i record relativi alle Policy in stato (STATUS) “Attiva” per le quali il periodo che intercorre fra la data fine validità e la data attuale è pari o inferiore a dieci giorni (DATAFINEVAL della policy <= SYSDATE +10), mentre in rosso sono riportati i record relativi alle Policy scadute per le quali è stata superata la data fine validità (DATAFINEVAL > SYSDATE). Il result set “Policy in scadenza/scadute” mostrerà le seguenti colonne: ID Policy (IDPOLICY), Data fine validità (DATAFINEVAL), ID PdL in output (), Descrizione PdL in output (JOBPLAN.DESCRIPTION), Tipo Policy (TIPOPOLICY), asset (TIPOLOGIAASSET).

Policy in stato “Pending Review”

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista riepilogativa delle Policy in stato “Pending Review”; nel dettaglio, vengono evidenziati in arancione i record relativi alle Policy che si trovano in stato “Pending Review” da più di cinque giorni (STATUS=’ e SYSDATE +5 > LASTREVIEWDATE), mentre in verde sono riportati i record relativi a Policy in stato “Pending Review” da meno di cinque giorni (STATUS=’ e SYSDATE +5 < LASTREVIEWDATE). Il result set “Policy in stato Pending Review” mostrerà le seguenti colonne: ID Policy (IDPOLICY), Modificato da (CHANGEDBY), ID PdL in output (), Descrizione PdL in output (JOBPLAN.DESCRIPTION), Tipo Policy (TIPOPOLICY), Riferimento asset (TIPOLOGIAASSET), Data entrata in Pending Review (LASTREVIEWDATE).

Policy in stato “Bozza” (DATAINIZIOVAL <= SYSDATE)

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista riepilogativa delle Policy in stato “Bozza” con DATAINIZIOVAL <= SYSDATE, ovvero le policy che pur avendo raggiunto la data di inizio validità non sono passate in stato “Attiva” a causa della mancata valorizzazione dei campi obbligatori. Il result set “Policy in stato Bozza (Data inizio validità <= Data corrente)” mostrerà le seguenti colonne: ID Policy (IDPOLICY), Tipo policy (TIPOPOLICY), Riferimento asset (TIPOLOGIAASSET), Data inizio validità (DATAINIZIOVAL).

OdL relazionati a policy in stato “Pending Review”

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista riepilogativa degli OdL che sarebbero impattati dall’attivazione di una Policy in stato “Pending Review” (workorder con uno stato diverso da CHIUSO il cui valore di X4POLICY è un POLICYID della tabella X4POLICY con status=’’); nel dettaglio, l’attivazione di una Policy in stato “Pending Review” può comportare l’esecuzione automatica di modifiche agli OdL esistenti a essa relazionati, oltre che la generazione di nuovi OdL. Inoltre, il result set “OdL relazionati a policy in Pending Review” mostrerà tutti gli ordini di lavoro (tabella WORKORDER) con le seguenti colonne: ID OdL (WONUM), ID Policy (X4POLICY), Riferimento asset (ASSETNUM), Responsabile attività (OWNERGROUP), ID PdL (JPNUM), Data inizio (TARGSTARTDATE), Data fine (TARGCOMPDATE), Stato OdL (STATUS).

OdL impattati da disattivazione policy

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista riepilogativa degli OdL relazionati ad una Policy disattivata (posta in stato “Non Attiva”); nel dettaglio, i record relativi agli OdL (tabella WORKORDER) relazionati a Policy disattivate (dove X4POLICY è un POLICYID della tabella X4POLICY con status NON ATTIVA) sono visibili fino a dieci giorni dopo la disattivazione della Policy (SYSDATE <= LASTREVIEWDATE +10). Inoltre, il result set “OdL impattati da disattivazione policy” mostrerà le seguenti colonne: ID OdL (WONUM), ID Policy disattivata (X4POLICY), Riferimento asset (ASSETNUM), Responsabile attività (OWNERGROUP), ID PdL (JPNUM), Data pianificata inizio (TARGSTARTDATE), Data pianificata fine (TARGCOMPDATE), Data disattivazione policy (X4POLICY.LASTREVIEWDATE), Stato OdL (STATUS).

OdL impattati da attivazione policy

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista riepilogativa degli OdL generati o modificati a seguito dell’attivazione di una Policy in stato “Pending Review” (workorder con un valore di X4POLICY che è adesso in stato ATTIVA ma che ha una LASTREVIEWDATE popolata). Tali record saranno visibili nel centro di controllo per 10 giorni a partire dalla data di attivazione della Policy (SYSDATE <= LASTREVIEWDATE +10). Gli ordini di lavoro mostrati all’interno del portlet saranno solo ed esclusivamente quelli generati prima della modifica alla policy (REPORTDATE dell’ordine di lavoro <= LASTREVIEWDATE della policy). Inoltre, il result set “OdL impattati da attivazione policy” mostrerà le seguenti colonne: ID OdL (WONUM), ID Policy (X4POLICY), Riferimento asset (ASSETNUM), Responsabile attività (OWNERGROUP), ID PdL (JPNUM), Data pianificata inizio (TARGSTARTDATE), Data pianificata fine (TARGCOMPDATE), Stato OdL (STATUS).

OdL relazionati ad asset in stati non idonei

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista riepilogativa degli OdL che risultano associati ad asset con stato non più idoneo per quel tipo attività. Nel dettaglio, la query di estrazione verificherà lo stato dell’asset per tutti gli OdL con uno stato diverso da “CHIUSO”; lo stato dell’asset verrà valutato come segue: se l’asset non è di terzo livello (deducibile dalla classificazione/classstructureid dell’asset) e per il pdl non è presente nessuna eccezione (non esiste un record nella tabella X4TABECCEZIONI con IDATTIVITA uguale al JPNUM dell’ordine), allora verifica soltanto che lo stato dell’asset sia congruo con quello definito nella tabella X4TABIDONEITA per l’asset; nel caso in cui invece sia presente un’eccezione (exists almeno un record nella tabella X4TABECCEZIONI con IDATTIVITA uguale al JPNUM dell’ordine), allora verificherà lo stato con quanto presente nel X4TABECCEZIONI; nel caso in cui l’asset sia di terzo livello, invece, la verifica avviene Inoltre, il result set “OdL relazionati ad asset in stati non idonei” mostrerà le seguenti colonne: ID OdL (WONUM), ID PdL (JPNUM), ID Asset (ASSETNUM), Descrizione Asset (ASSET.DESCRIPTION), Stato Asset (ASSET.STATUS).

Elaborazioni DSS avviate e non portate a termine

In Maximo sarà possibile la visualizzazione di una vista riepilogativa delle elaborazioni effettuate dal DSS Inoltre, “Elaborazioni DSS avviate e non portate a termine” mostrerà le seguenti colonne: ID Policy (X4POLICY), ID Asset (ASSETNUM), Descrizione Asset (DESCRIPTION), Data elaborazione (CREATED_TIMESTAMP della tabella X_DSS_WORKED_HISTORY

Elaborazioni DSS portate a termine

In Maximo sarà possibile la visualizzazione di una vista riepilogativa delle elaborazioni effettuate dal DSS concluse, ovvero che hanno generato OdL sugli asset di riferimento. Inoltre, “Elaborazioni DSS portate a termine” mostrerà le seguenti colonne: ID Policy (X4POLICY), ID (WONUM), ID Asset (ASSETNUM), Descrizione Asset (DESCRIPTION), Data elaborazione (CREATED_TIMESTAMP della tabella X_DSS_WORKED_HISTORY).

## Generazione input tramite eventi fuori processo

### 5.3.1 Configurazioni

Per permettere l’inserimento di misure derivanti dai report di laboratorio e da report Ariel, si prevede la seguente configurazione a sistema:

Inserimento misure macchinari e Ariel

Per permettere il caricamento a sistema di misure macchinari e Ariel si prevede la configurazione di un file MXLoader con le seguenti colonne: Asset (ASSETNUM), ID misura (IDMISURA), Valore misure (VALORE), Unità di misura (UM).

Per permettere l’inserimento di nuove difformità a sistema, si prevede la seguente configurazione:

Configurazione dell’applicazione X4DIFFOR

Per permettere l’inserimento di difformità (senza registrarle da un ordine di lavoro), l’applicazione già utilizzata per la rilevazione delle difformità inserite a sistema viene configurata per permettere l’inserimento di nuove difformità mediante il “+” in alto a destra. Creando una nuova riga, sarà necessario popolare i campi: Asset, Difformità, Gravità (se richiesto dalla difformità) e Note (descritti nel documento Analisi Specifiche Tecniche Sprint 3). Lo stato impostato automaticamente dal sistema sarà “DA VALIDARE” (ottenuto mediante controllo di tipo “DEFAULTVALUE” inserito in applicazione che in modo statico genera lo stato con la stringa sopra citata). Sulla base dell’asset selezionato, il campo difformità verrà pre-filtrato da parte del sistema – grazie all’utilizzo d dominio – con le difformità applicabili alla classificazione dell’asset selezionato

### 5.3.2 Sviluppi

Per permettere l’inserimento di difformità non associate a OdL, si prevede il seguente sviluppo a sistema:

Inserimento difformità non associate a OdL

L’utente può inserire manualmente una nuova difformità associata ad un asset nell’applicazione “Difformità Rilevate”. In tal caso, i nuovi OdL associati all’asset mostreranno la nuova difformità aggiunta. Quando questa sarà presente all’interno dell’ordine di lavoro

## Area di validazione

L’area di validazione è un’applicazione custom di IBM Maximo in cui l’utente può controllare gli OdL on-condition generati dal DSS (stato “Da Validare”) prima di inviarli alla fase di pianificazione (stato “Aperto”).

### 5.4.1 Configurazioni

Per permettere la visione dei campi necessari all’area di validazione degli OdL, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4AREAVAL”, clone dell’applicazione WOTRACK, si configurano i campi dell’unico tab “Area di validazione OdL”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID OdL | WONUM |  |  |  |
| Riferimento asset | ASSETNUM |  |  |  |
| Gerarchia asset associato | ASSET.CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| ID PdL | JPNUM |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Data pianificata inizio | TARGSTARTDATE |  |  |  |
| Data pianificata fine | TARGCOMPDATE |  |  |  |
| Stato | STATUS |  |  |  |
| Programma | X4PROGRAMMA |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

L’applicazione sopra citata è definita come un’applicazione mono-pagina, in cui le informazioni consultabili sono quelle sopra esposte.

Per permettere il passaggio di stato ad APERTO degli ordini di lavoro che transitano nell’area di validazione è inoltre necessario la seguente configurazione:

Modifica stato OdL da area di validazione

Per permettere il passaggio di stato in “Aperto” degli OdL che si trovano in area di validazione è necessario configurare l’applicazione in modo tale che la schermata preveda una prima colonna che permetta di “flaggare” gli ordini di lavoro. Tale configurazione avviene mediante la personalizzazione dell’applicazione inserendo un controllo di tipo “tablecolumn” il cui evento collegato è “selectrows”. Inoltre, viene introdotta l’azione laterale “Cambia stato” che permette di cambiare lo stato in “APERTO" o in “NON VALIDATO” degli OdL selezionati.

### 5.4.2 Sviluppi

Inerente alla gestione dello stato di validazione/aperto degli ordini di lavoro, si prevedono i seguenti sviluppi a sistema:

Passaggio manuale allo stato “Non Validato”

Il passaggio allo stato “Non Validato” è sempre manuale attraverso l’area di validazione. In caso di non validazione, l’utente deve inserire una motivazione nella dialog di cambio stato scegliendola tra quelle definite nella lista X4MOTIVAZIONE.

Nel caso in cui l’utente scelga di non validare un OdL al quale sono associate delle difformità (il JPNUM dell’OdL che si sta andando a non validare ha una difformità risolvibile che è aperta su quell’asset dell’ordine - esiste dunque una riga su X4DIFFORSTAT dove l’assetnum coincide con quello dell’OdL, lo stato è aperto e l’ID difformità coincida con una di quelle risolvibili dall’attività imposta nel JPNUM che si sta non validando), il sistema produrrà un warning segnalando che le difformità associate all’OdL dovranno essere gestite con una nuova attività oppure chiuse manualmente.

Passaggio automatico allo stato “Aperto”

Mediante l’implementazione di un’escalation che valuta tutte le righe di workorder in stato “DA VALIDARE”, e la cui data di massima validazione (calcolato come REPORTDATE dell’ordine di lavoro + il valore di tempo di massima validazione del piano di lavoro collegato all’odl) sia maggiore della SYSDATE del sistema.

Il sistema deve garantire il passaggio automatico allo stato “Aperto” dell’OdL nell’area di validazione quando la SYSDATE (al momento della generazione dell’OdL in stato “Da validare”) supera il massimo tempo di validazione (X4TEMPOVAL di JOBPLAN).

## Gestione indicatori di condizione

Il processo di calcolo degli indicatori di condizione tecnica (HI, Health Index) sarà eseguito tramite il modulo dell’applicativo IBM Maximo denominato “Health”.

### 5.5.1 Configurazioni

Per permettere la gestione della frequenza di calcolo degli HI e dei tassi di guasto/anomalia, si prevede la seguente configurazione a sistema:

Crontask HI e Tassi di guasto/anomalia

Per permettere il ricalcolo automatico degli HI a frequenza fissa dovrà essere abilitata la crontask “AHSCORINGGROUP” e le relative istanze della cron legate agli indicatori di condizione tecnica da calcolare con frequenza fissa (la creazione delle istanze della crontask è automatica e contestuale al momento della creazione dello scoring group).

Customizzazione Health (Tassi di guasto/anomalie)

Vista la natura dei calcoli da effettuare per i tassi di guasto e di anomalia, che non vengono concettualmente applicati al singolo asset ma piuttosto alla “classificazione” in esame da parte dell’Health, si è reso necessario configurare il sistema come segue: Per ogni classificazione verrà creata un’oggetto di tipo “LOCATION” fittizio, con nome eguale alla classificazione; per questa location verranno calcolati gli score di guasto e di anomalia. Gli score verranno calcolati basandosi sulla consistenza impiantistica degli asset aventi classificazione eguale a quella impostata alla location

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

### 5.5.2 Sviluppi

Per permettere la gestione di HI e Tassi di guasto/anomalia, si prevedono i seguenti sviluppi a sistema:

Visualizzazione storico HI e Tassi di guasto/anomalia

Gli HI sono storicizzati mensilmente, mentre i Tassi di guasto/anomalie annualmente. Lo storico degli indicatori di condizione risulta consultabile sia tramite l’Applicazione dedicata “Indicatori di condizione tecnica” del “Manage” (nel menù delle azioni laterali “Visualizza storico HI” e “Visualizza storico Tassi di guasti/anomalie”) sia tramite l’applicazione “Asset” del modulo “Health”. Questo storico verrà gestito automaticamente da uno script di automazione: il primo giorno del mese (o dell’anno, per i tassi di guasto/anomalia), lo script verrà eseguito su tutti gli asset e creerà una nuova riga nella tabella di storico, prelevando il valore corrente dell’HI e lo riporta in una riga della tabella di storico degli HI.

Connessione Manage-Health (API)

Al fine di poter creare un ricalcolo ad evento degli indicatori di condizione tecnica di uno specifico asset, piuttosto che dell’health index generico (tramite crontask), verrà creata un’API specifica che accetterà come parametro in ingresso l’identificativo REST di uno specifico asset e – tramite l’opportuna action “CALCULATEHEALTH” passata come parametro della chiamata REST - renderà possibile il ricalcolo puntuale degli indicatori di condizione tecnica di un solo asset.

Creazione degli HI e calcolo

Al fine di poter effettuare i calcoli dovuti alla ricostruzione dell’HI verranno creati nella componente Health del MAS tanti Scoring group quanti sono gli indicatori di condizione tecnica designati (HI conduttori, HI macchinari, HI funi di guardia, etc.); al loro interno verrà configurata una formula utilizzata per comunicare ad EAM come dovrà essere effettuato il calcolo di ogni singolo HI applicato all’asset. Di seguito vengono riportati i principali operatori (e formule) utilizzati per la configurazione dell’HI:

| Operatore | Descrizione | Esempio |
| --- | --- | --- |
| + | Somma | A + B |
| - | Sottrazione | A - B |
| = | Uguale a | IF(a=10 \|\| b==c,x,y) |
| != | Diverso da | IF(a<>10 && b!=c,x,y) |
| / | Divisione | A/B |
| * | Moltiplicazione | A*B |
| && | Operatore logico AND | IF(a<>10 && b!=c,x,y) |
| \|\| | Operatore logico OR | IF(a=10 \|\| b==c,x,y) |

| Funzione | Descrizione | Esempio |
| --- | --- | --- |
| IF |  |  |
| MAX, MIN |  | MAX(a,b) o MAX(a,MAX(b,c)), MIN(a,b), MIN(a,MIN(b,c)) |
| FLOOR,CEILING |  | FLOOR(3.2) restituisce 3 |
| REL$ATTRIBUTO | La sintassi è utilizzata per spostarsi tra un MBO e l’altro secondo una relazione (REL) configurata a sistema | X4DIFF1123$MAXVALIDAZIONE |
| FUNC$REL | La sintassi è utilizzata per applicare una funzione specifica ad un set di MBO secondo una relazione (REL) configurata a sistema | COUNT$X4DIFF1123OPEN |

La formula individua le attività associate a ciascun asset di tipo CONDUTTORE calcola il contributo la moltiplicazione peso.

HI conduttore

Formula:

ASSETLOCSCORE("1010")+ASSETLOCSCORE("1027")+ASSETLOCSCORE("1031")

La formula prevede la somma dei punteggi degli Health Index relativi a vetustà, anomalie, , calcolati per ciascun asset di tipo conduttore.

A tal fine, viene utilizzata la funzione ASSETLOCSCORE, la quale prende in input il codice identificativo dell’HI da considerare e recupera, dalla tabella AHSCORE, l'ultimo valore corrispondente per l’asset in esame.

Il processo viene eseguito per ciascuno dei tre HI sopra menzionati. Una volta recuperati tutti i punteggi associati all’asset, questi vengono sommati per ottenere il valore finale.

La formula seleziona il valore massimo tra gli Health Index di degrado relativi agli asset figli dell’asset corrente. Tale calcolo viene eseguito per ciascun asset di tipo CONDUTTORI.

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

## Recupero/alimentazione da fonti esterne: ELIVE

### 5.6.1 Configurazioni Maximo

Per poter leggere e scrivere oggetti di Maximo, verranno utilizzati i seguenti Object Structure:

| Per veicolare l’object structure X4ELIVEODL viene definito il seguente Publish Channel: Sono state definite tre regole per il publish channel: SKIP_RULE_ELIVE: Regola di filtro per gli OdL da inviare ad ELIVE, che skippa tutti gli ordini di lavoro che hanno Un JOBPLAN con il campo X4ELIVE diverso da 1 SKIP_STATUS: Regola di filtro per gli OdL che hanno il campo STATUS diverso da aperto SKIP_GIAINVIATO: Regola di filtro per gli OdL che skippano se hanno già un record nella tabella X4ESITOINT. Viene definito il seguente TARGET: Il topic sul quale verrà inviato l’object structure X4ELIVEODL è il seguente: eam-send-odl-elive-<ambiente dev/col/tra> |  |
| --- | --- |

### 5.6.3 Sviluppo Maximo not embedded

#### Flussi d’integrazioni

In questa sezione si prefigge di descrivere brevemente il flusso che intercorre tra EAM e il sistema ELIVE e le relative iterazioni.

Di seguito diagramma delle chiamate tra EAM e ELIVE:

Per

#### Logica d’attivazione e tipologia d’interazione

Di seguito descriviamo le interazioni tra EAM e i vari sistemi come rappresentato nel diagramma precedente.

ELIVE effettua periodicamente delle verifiche basate sulle ispezioni per recuperare da SISNET i dati geografici e geometrici. Dai rilevamenti andranno a rilevare quali sono le posizioni delle piante.

Successivamente Il sistema ELIVE trasmetterà i riferimenti dell'ispezione, fornendo ad EAM i riferimenti delle CAMPATE relative al sistema SAP e il codice dell’ordine di lavoro.

Saranno presenti quindi due flussi d’integrazioni che sono i seguenti:

| Nome flusso | Descrizione |  |
| --- | --- | --- |
| Invio OdL | Flusso di invio di ordini di lavoro relativo alle ispezioni Lidar verso ELIVE |  |
| Invio provvedimenti taglio piante | Flusso per l’inserimento a sistema dei dati relativi alle interferenze |  |

Il flusso Invio OdL verrà quindi innescato da EAM, ogni volta che un OdL passerà in stato aperto, e quindi richiamerà un API REST esposta da ELIVE dove invierà i dati successivamente dettagliati.

#### Invio OdL

L’interfaccia di Invio OdL scatta ogni qual volta un Ordine di Lavoro relativo alle attività di Taglio Piante passa dallo stato “In Validazione” allo stato “Aperto”.

Per fare in modo che si identifichi univocamente il subset di Ordini di lavoro da inviare ad Elive, è necessario individuare a priori tutti i piani di lavoro che afferiscono alle attività di Elive e che serviranno come criteri di filtro nell’interfaccia.

Gli ordini di lavoro che verranno inviati ad elive sono quelli che hanno un PdL (attributo JPNUM della WORKORDER) che ha lo stato = ‘ACTIVE’ e il campo X4ELIVE uguale a 1.

Ogni qualvolta che l’OdL cambia stato e rispetta i criteri che afferiscono alle attività di Elive, viene inviato un nuovo messaggio su coda kafka “eam-send-odl-to-elive-<ambiente>”;

esempio di messaggio kafka:

{

"_action": "Replace",

"_event": true,

"_id": "SIT01-1680",

"_translangcode": "EN",

"asset": [

{

"_id": "1146323-SIT01",

"assetnum": "1146323",

"href": "http:\/\/childkey#V09SS09SREVSL0FTU0VULzExNDYzMjMvU0lUMDE-",

"localref": "https:\/\/masdev.manage.dev.apps.dev.azopencs.servizi.prv\/maximo\/oslc\/os\/x4eliveodl\/_U0lUMDEvMTY4MA--\/asset\/0-1145954",

"x3sapid": "23034H1-073-----",

"x3zzsocp": "TR36",

"x3zzsocp_description": "Rete S.r.l.",

"ytdcost": 0.0

}

],

"assetnum": "1146323",

"href": "https:\/\/masdev.manage.dev.apps.dev.azopencs.servizi.prv\/maximo\/oslc\/os\/x4eliveodl\/_U0lUMDEvMTY4MA--",

"jpnum": "test2_0627",

"siteid": "SIT01",

"status": "APERTO",

"status_description": "Aperto",

"targstartdate": null,

"wonum": "1680",

"x4nodm": null,

"x4preownergroup": null

}

Questo messaggio viene letto da un kafka listener, che procederà poi a recuperare solo i campi necessari, e successivamente chiamare un API esposta da Elive.

Verrà chiamata l’API REST esposta da ELIVE al seguente url:

<URL API ELIVE> (vedi IFA Elive)

Verranno passati i seguenti parametri da EAM:

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

|  |  |  |  |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

ELIVE per ogni chiamata può rispondere con un esito positivo o con un errore.

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINT. A tale scopo verrà usato il business Object Structure X4ESITOINT.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "O",

"idinterfaccia": "SEND_ODL_TO_ELIVE",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "ELIVE",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

#### Invio provvedimenti taglio piante

Il flusso si attiva quando viene recuperato un file da BWCE, che successivamente richiama l’API POST esposta da EAM con il contenuto del file. L’esito dell’elaborazione sarà salvato su EAM. Elive non riceverà da EAM esito dell’elaborazione.

L’API è esposta al seguente URI

POST https://<host:port>/eam/elive/provvedimenti

La request da passare all’API è la seguente:

Esempio file 1:

{

"nomefile": " 22108A1-Report_per_taglio_piante-13.12.2022.csv",

"record":[

{

"_int ": 1,

"campata": "22108A1-012-----",

"dist_int": 4,6,

"progres": 285,

"priorità": "DIFFERIBILE",

"temp_amb": 0

"c_est_int": 4868,7,

"c_nord_int": 44444,

"wonum": "001",

"cod_griglia": null,

"t_s": null,

"t_a": null,

"est_cen": null,

"nord_cen": null,

"x_cen": null,

"y_cen": null,

"anno_comp": null,

"periodo_comp": null,

"in_fascia": null,

"cat_vert": null,

"cat_sband": null,

"ribalt": null,

“tipologia”: [

1,

2,

3

]

}

]

}

Esempio file 2:

{

"nomefile": " 22108A1-Report_per_taglio_piante_griglia-13.12.2022.csv",

"record":[

{

"_int ": null,

"campata": "22108A1-012-----",

"dist_int": 4,6,

"progres": null,

"priorità": "DIFFERIBILE",

"temp_amb": null

"c_est_int": null,

"c_nord_int":null,

"wonum": "001",

"cod_griglia": null,

"t_s": 1,

"t_a": 0,

"est_cen": 4868,7,

"nord_cen":44444,

"x_cen": 44444,

"y_cen":44444,

"anno_comp": 2024,

"periodo_comp": “periodo”,

"in_fascia":0,

"cat_vert": 1,

"cat_sband": 0,

"ribalt": 0,

“tipologia”: [

1,

2,

3

]

}

]

}

Per gestire le casistiche in cui, all’interno del file Excel, un record contenga unicamente la coppia Campata / Numero ODL EAM (ovvero nei casi di report vuoto per quella campata), è stato concordato di prevedere comunque l’invio di un oggetto nell’array "record" del payload, configurato nel seguente modo:

"record": [ { "campata": "22108A1-012-----", "dist_int": 1000000000, "priorita": "NA", "tipologia": [], "wonum": "10001" } ]

In questo caso, non verrà generata alcuna interferenza per il record in questione, ma verrà semplicemente popolato il flag “Acquisizione misure Elive” per il wonum corrispondente.

Una volta ricevuto il body, risponderemo con 201 al sistema che ci chiama (un accepted HTTP status 201)

Successivamente, l’applicazione continua a lavorare in modo asincrono.

Per garantire il disaccoppiamento risposta elaborazione sarà veicolato su topic kafka la richiesta di elaborazione. Il body della request verrà inviato al topic kafka eam-receive-provvedimenti-piante-<ambiente>, su cui sarà in ascolto un kafkalistner che consumerà il messaggio e procederà con le logiche.

Il messaggio kafka è il seguente:

{

“uuid” : "0300cc56-a105-4143-ad3a-e2b612dae244",

“retry” : false

“report” : {

"nomefile": " 22108A1-Report_per_taglio_piante-13.12.2022.csv",

"record":[

{

"_int ": 1,

"campata": "22108A1-012-----",

"dist_int": 4,6,

"progres": 285,

"priorità": "DIFFERIBILE",

"temp_amb": 0

"c_est_int": 4868,7,

"c_nord_int": 44444,

"wonum": "001",

"cod_griglia": null,

"t_s": null,

"t_a": null,

"est_cen": null,

"nord_cen": null,

"x_cen": null,

"y_cen": null,

"anno_comp": null,

"periodo_comp": null,

"in_fascia": null,

"cat_vert": null,

"cat_sband": null,

"ribalt": null,

“tipologia”: 1

}

]

}

}

Il campo “retry” serve a gestire eventuali errori durante il processo. Nel caso in cui si verifichino errori, il messaggio verrà reinviato dallo stesso listener alla coda Kafka, in modo da rieseguire il processo. Previsto un numero max di retry.

Ogni record viene validato. Come controllo, confrontiamo il SAP ID della campata presente in ogni record con le nostre tabelle interne per verificarne la presenza. Verrà eseguita la seguente query:

SELECT

sapid_list.x3sapid,

a.assetnum

FROM

(

SELECT ‘<SAP ID campata 1>' AS x3sapid FROM DUAL

UNION ALL

SELECT ‘<SAP ID campata 2>' FROM DUAL

-- Si aggiungono gli altri SAP ID

) sapid_list

LEFT JOIN

Asset a

ON

sapid_list.x3sapid = a.x3sapid

ORDER BY

sapid_list.x3sapid;

Questa query ci permette di recuperare l'assetnum relativo al SAP ID della campata, e nel caso in cui l'assetnum non sia presente, tutte le righe con quel SAP ID saranno scartate. In questo caso, le righe saranno scartate e inserite in una tabella chiamata SCARTIREPORTELIVE, in cui ci saranno tutti gli scarti delle varie chiamate.

Una volta validate, tutte le righe non scartate saranno inserite nella tabella DATIREPORTELIVE, che conterrà tutti i campi dei due file di report.

Successivamente, Verrà recuperato il livello di tensione della LINEA del report con la seguente query:

SELECT

alnvalue

FROM

assetspec

WHERE

assetnum (select assetnum from asset where x3sapid=<x3sapid della linea delle campate>)

AND assetattrid='LIN_LIVELLO_TENSIONE_ESERCIZIO'

La query fornirà un valore che, in base alla legenda sottostante, equivale a una determinata tensione.

La gravità delle viene determinata sulla base delle distanze ricevute in input dal report e del livello di tensione della linea. I calcoli sono effettuati in base alla tabella sottostante:

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |  |  |  |  |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
|  |  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |  |

Successivamente, in base al file ricevuto si eseguono due differenti logiche:

Per il primo file, una volta calcolate le gravità della campata, si effettua un confronto tra la numerosità delle interferenze di ciascun livello di gravità con un valore denominato “target di definizione delle piante interferenti”. Questo target definisce il limite sopra il quale le piante sono considerate di alto volume.

catenaria verticale:

“piante interferenti (volume alto)” con due livelli di gravità: differibile o urgente

“piante interferenti (volume basso)” con due livelli di gravità: differibile o urgente

catenaria sbandata

“piante interferenti (volume alto) – catenaria sbandata” con due livelli di gravità: differibile o urgente

“piante interferenti (volume basso) – catenaria sbandata” con due livelli di gravità: differibile o urgente

ribaltamento

“piante interferenti (volume alto) – ribaltamento” con due livelli di gravità: differibile o urgente

“piante interferenti (volume basso) – ribaltamento” con due livelli di gravità: differibile o urgente

Per il secondo file, si richiede al sistema di generare per ogni riga del json, un’interferenza in funzione delle informazioni presenti nelle colonne t_s, t_a e priorità.

| Criticità | # interferenze | Interferenza |
| --- | --- | --- |
| Urgente | Taglio singolo (t_s = 1) | Piante interferenti (taglio singolo) |
| Urgente | Taglio areale (t_a = 1) | Piante interferenti (taglio areale) |
| Differibile | Taglio singolo (t_s = 1) | Piante interferenti (taglio singolo) |
| Differibile | Taglio areale (t_a = 1) | Piante interferenti (taglio areale) |

Se ad esempio, Sulla campata 22209E1-189----- sono presenti nel report tre tagli singoli (colonna t_s), di cui due urgenti e uno differibile, e due tagli areali (colonna t_a) differibili. Il sistema deve quindi generare due interferenze “Piante interferenti (taglio singolo)” con target “Urgente”, una interferenza “Piante interferenti (taglio singolo)” con target “differibile” e due interferenze “Piante interferenti (taglio areale)” con target “Differibile”.

si possono generare, per ogni riga del report, le seguenti interferenze in base alle tipologie:

catenaria verticale:

“piante interferenti taglio singolo” con due livelli di gravità: differibile e urgente

“piante interferenti taglio areale” con due livelli di gravità: differibile e urgente

catenaria sbandata

“piante interferenti taglio singolo – catenaria sbandata” con due livelli di gravità: differibile e urgente

“piante interferenti taglio areale – catenaria sbandata” con due livelli di gravità: differibile e urgente

ribaltamento

“piante interferenti taglio singolo – ribaltamento” con due livelli di gravità: differibile e urgente

“piante interferenti taglio areale – ribaltamento” con due livelli di gravità: differibile e urgente

Le interferenze verranno poi inserite sulla tabella . A tale scopo si userà il business Object Structure X4INTERFSTATche ci permetterà di effettuare l’INSERT tramite una chiamata REST verso Maximo.

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

POST: https://<HOST:PORT>/maximo/api/os/X4INTERFSTAT?lean=1

Un esempio di body il seguente:

Una volta inserite le interferenze, per ciascun wonum presente, verrà popolato il flag “Acquisizione misure Elive”. A seguito di ciò, verrà eseguito un aggiornamento sulla tabella workorder, impostando il campo X4FLAGFISICO a 1 per ogni ordine coinvolto.

verrà salvata sulla tabella X4ESITOINT sia in caso di errore, che in caso positivo. A tale scopo verrà usato il business Object Structure X4ESITOINT.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

Un esempio di request da passare all’API è la seguente:

{

"esito": "OK",

"idinterfaccia": "INVIO_PROVVEDIMENTI_TO_EAM”

"desc_esito": "XYZ",

"source": "ELIVE",

"dest": "EAM",

"uniqueid": "HUDISAIU383KO9",

}

##### Modello Relazionale

Di seguito verranno riportate le tabelle usate all’interno dei flussi di sprint 4 con i relativi dettagli.

WORKORDER:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| WONUM | VARCHAR2(10) | Y | Numero identificativo dell'ordine di lavoro |
| ASSETNUM | VARCHAR2(25) | Y | Identificativo univoco della sede tecnica su cui effettuare l'ordine di lavoro |

DATIREPORTELIVE:

| Campo | Tipo | Tipo | Obbligatorio | Obbligatorio | Descrizione | Descrizione |
| --- | --- | --- | --- | --- | --- | --- |
| ID | NUMBER | Y | Y | Identificato dei record della tabella | Identificato dei record della tabella |  |
| NOME_FILE | VARCHAR2(100) | Y | Y | Nome del file a cui appartengono i record | Nome del file a cui appartengono i record |  |
| ID_INT | NUMBER | N | N | Progressivo numerico delle campata | Progressivo numerico delle campata |  |
| CAMPATA | VARCHAR2(100) | Y | Y | SAP id della campata | SAP id della campata |  |
| DIST_INT | NUMBER | Y | Y | Distanza in m dalla catenaria | Distanza in m dalla catenaria |  |
| PROGRES | NUMBER | N | N | Distanza in m dell’interferenza dal 1° sostegno di campata | Distanza in m dell’interferenza dal 1° sostegno di campata |  |
| PRIORITA | VARCHAR2(50) | Y | Y | Gravità dell’interferenza | Gravità dell’interferenza |  |
| TEMP_AMB | NUMBER | N | N | Temperatura ambientale | Temperatura ambientale |  |
| C_EST_INT | NUMBER | N | N | Coordinate Est | Coordinate Est |  |
| C_NORD_INT | NUMBER | N | N | Coordinate Nord | Coordinate Nord |  |
| WONUM | VARCHAR2(10) | Y | Y | Numero identificativo dell'ordine di lavoro | Numero identificativo dell'ordine di lavoro |  |
| COD_GRIGLIA | VARCHAR2(40) | N | N | è il codice della macro-cella di appartenenza del taglio | è il codice della macro-cella di appartenenza del taglio |  |
| T_S | NUMBER | N | N | indica se il taglio è singolo (1 sì, 0 no); | indica se il taglio è singolo (1 sì, 0 no); |  |
| T_A | NUMBER | N | N | indica se il taglio è areale (1 sì, 0 no); | indica se il taglio è areale (1 sì, 0 no); |  |
| EST_CEN | NUMBER | N | N | longitudine, nel sistema di riferimento della linea, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | longitudine, nel sistema di riferimento della linea, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale |  |
| NORD_CEN | NUMBER | N | N | latitudine, nel sistema di riferimento della linea, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | latitudine, nel sistema di riferimento della linea, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale |  |
| X_CEN | NUMBER | N | N | coordinata x, nel sistema di riferimento locale che ha per origine il sostegno di inizio, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | coordinata x, nel sistema di riferimento locale che ha per origine il sostegno di inizio, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale |  |
| Y_CEN | NUMBER | N | N | coordinata y, nel sistema di riferimento locale che ha per origine il sostegno di inizio, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | coordinata y, nel sistema di riferimento locale che ha per origine il sostegno di inizio, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale |  |
| ANNO_COMP | NUMBER | N | N | anno di riferimento | anno di riferimento |  |
| PERIODO_COMP | VARCHAR2(40) | N | N | Periodo di riferimento | Periodo di riferimento |  |
| IN_FASCIA | NUMBER | N | N | indica se il taglio è in fascia oppure no (1 sì, 0 no). I tagli fuori fascia sono individuati con l'algoritmo del ribaltamento | indica se il taglio è in fascia oppure no (1 sì, 0 no). I tagli fuori fascia sono individuati con l'algoritmo del ribaltamento |  |
| CAT_VERT | NUMBER | N | N | indica se il taglio è individuato con riferimento alla catenaria verticale oppure no (1 sì, 0 no | indica se il taglio è individuato con riferimento alla catenaria verticale oppure no (1 sì, 0 no |  |
| CAT_SBAND | NUMBER | N | N | indica se il taglio è individuato con riferimento alla catenaria sbandata oppure no (1 sì, 0 no); | indica se il taglio è individuato con riferimento alla catenaria sbandata oppure no (1 sì, 0 no); |  |
| RIBALT | NUMBER | N | N | indica se il taglio è individuato con l'algoritmo del ribaltamento oppure no (1 sì, 0 no); | indica se il taglio è individuato con l'algoritmo del ribaltamento oppure no (1 sì, 0 no); |  |
| TIPOLOGIA | NUMBER | Y | Y | Indica la tipologia del report | Indica la tipologia del report |  |

SCARTIREPORTELIVE:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| ID | NUMBER | Y | Identificato dei record della tabella |
| NOME_FILE | VARCHAR2(100) | Y | Nome del file a cui appartengono i record |
| DESC_SCARTO | VARCHAR2(500) | Y | Motivo dello scarto della campata/wonum |
| ID_INT | NUMBER | N | Progressivo numerico delle campata |
| CAMPATA | VARCHAR2(100) | Y | SAP id della campata |
| DIST_INT | NUMBER | Y | Distanza in m dalla catenaria |
| PROGRES | NUMBER | N | Distanza in m dell’interferenza dal 1° sostegno di campata |
| PRIORITA | VARCHAR2(50) | Y | Gravità dell’interferenza |
| TEMP_AMB | NUMBER | N | Temperatura ambientale |
| C_EST_INT | NUMBER | N | Coordinate Est |
| C_NORD_INT | NUMBER | N | Coordinate Nord |
| WONUM | VARCHAR2(10) | Y | Numero identificativo dell'ordine di lavoro |
| COD_GRIGLIA | VARCHAR2(40) | N | è il codice della macro-cella di appartenenza del taglio |
| T_S | NUMBER | N | indica se il taglio è singolo (1 sì, 0 no); |
| T_A | NUMBER | N | indica se il taglio è areale (1 sì, 0 no); |
| EST_CEN | NUMBER | N | longitudine, nel sistema di riferimento della linea, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale |
| NORD_CEN | NUMBER | N | latitudine, nel sistema di riferimento della linea, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale |
| X_CEN | NUMBER | N | coordinata x, nel sistema di riferimento locale che ha per origine il sostegno di inizio, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale |
| Y_CEN | NUMBER | N | coordinata y, nel sistema di riferimento locale che ha per origine il sostegno di inizio, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale |
| ANNO_COMP | NUMBER | N | anno di riferimento |
| PERIODO_COMP | VARCHAR2(40) | N | Periodo di riferimento |
| IN_FASCIA | NUMBER | N | indica se il taglio è in fascia oppure no (1 sì, 0 no). I tagli fuori fascia sono individuati con l'algoritmo del ribaltamento |
| CAT_VERT | NUMBER | N | indica se il taglio è individuato con riferimento alla catenaria verticale oppure no (1 sì, 0 no |
| CAT_SBAND | NUMBER | N | indica se il taglio è individuato con riferimento alla catenaria sbandata oppure no (1 sì, 0 no); |
| RIBALT | NUMBER | N | indica se il taglio è individuato con l'algoritmo del ribaltamento oppure no (1 sì, 0 no); |
| TIPOLOGIA | STRING | Y | Tipologia del report. un stringa che contiene numeri da 1 a 3 concatenati con questo dominio: 1)Catenaria Verticale 2)Catenaria Sbandata 3)Ribaltamento |

X4INTERF

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4INTERFSTATID | NUMBER | Y | ID del record |
| DESCRIPTION | VARCHAR2(50 CHAR) | N | Descrizione del |
| ASSETNUM | VARCHAR2(25 CHAR) | Y | Asset su cui |
|  |  |  |  |
| SITEID | VARCHAR2(8 CHAR) | Y | Sede dell'asset |
| IDINTERFERENZA | VARCHAR2(15 CHAR) | Y |  |
|  |  |  |  |
| STATUS | VARCHAR2(15 CHAR) | Y | Stato della difformità/Interferenza |
| PRIMARILEVAZIONE | DATE | Y | Data della prima rilevazione |
|  |  |  |  |
| HASLD | NUMBER | Y |  |
| ROWSTAMP | VARCHAR2(40 CHAR) | Y |  |

X4ESITOINT

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4ESITOINTID | NUMBER | Y | PK |
| IDINTERFACCIA | VARCHAR2(50) | N | Nome interfaccia |
| ESITO | VARCHAR2(10) | N | ESITO (Es. OK o KO) |
| COD_ESITO | VARCHAR2(30) | N | Codice d’errore |
| DESC_ESITO | VARCHAR2(2000) | N | Descrizione d’errore |
| UNIQUEID | VARCHAR2(50) | Y | Chiave d’integrazione per flusso |
| ROWSTAMP | VARCHAR2(40) | Y |  |
| DEST | VARCHAR2(50) | N | Sistema destinatario |
| SOURCE | VARCHAR2(50) | N | Sistema d’origine |
| PARENT | NUMBER | N | Parent è FK al campo X4ESITOINTID |
| START_ELAB | DATE | N | Data inizio elaborazione |
| END_ELAB | DATE | N | Data fine elaborazione |
| KAFKA_TOPIC | VARCHAR2(50) | N | Coda Kafka di elaborazione |
| MESSAGE | CLOB | N | Messaggio Json d’input. Viene salvato se necessario |

##### Interface agreement flusso ELIVE Invio Provvedimenti Taglio Piante

La specifica d’interfaccia del flusso è riportata nel documento InterfaceAgreement_EAM_ELIVE_v1.1.docx.

### 5.7.1 Configurazioni Maximo

Per poter gestire i dati su Maximo, verranno creati i seguenti Object Structure:

|  |  |
| --- | --- |

| Publication channel | Descrizione |
| --- | --- |
| X4WOINVIOCAVICHANNEL | Canale per inviare gli ordini di lavoro su CAVI INTERRATI |
| X4WOCAVIDELETECHANNEL | Canale per inviare le cancellazioni degli ordini di lavoro a CAVI INTERRATI |

In EAM bisogna aggiungere un meccanismo di pubblicazione ad eventi, grazie al quale,venga inviato un messaggio kafka sul topic x4-cavi-invio-ispezioni-<ambiente>; il meccanismo si dovrà attivare solo per determinati codici attività che dovranno essere forniti dal BSN con una pianificazione da concordare.

| Proprietà | Valore |
| --- | --- |
| Nome del nodo | eamCaviInterratiInvioIspezioni |
| entityName |  |
| entityType | TOPIC |
| entityDescription | Kafka topic per inviare gli ordini di lavoro a cavi interrati |
| entityObjectName | Invio degli ordini verso cavi interrati |
| entityObjectClass | it.terna.eam.integration.kafka.messages.WorkOrderCaviInterrati. |
| entityUuidMethodName | getUUID |

Per l’invio della cancellazione degli ordini invece è necessario configurare le variabili di ambiente con i valori della prossima tabella:

| Proprietà | Valore |
| --- | --- |
| Nome del nodo | eamCaviInterratiInvioCancellazione |
| entityName |  |
| entityType | TOPIC |
| entityDescription | Kafka topic per inviare la cancellazione degli ordini di lavoro |
| entityObjectName | Invio degli ordini verso cavi interrati da cancellare |
| entityObjectClass | it.terna.eam.integration.kafka.messages.WorkOrderCancellazioneCaviInterrati. |
| entityUuidMethodName | getUUID |

### 5.7.2 Sviluppo Maximo embedded

Non è previsto nessuno script di automazione per questo sistema

### 5.7.3 Sviluppo Maximo not embedded

Di seguito diagramma di chiamate tra i sistemi EAM, CAVI INTERRATI:

Allineamento anagrafica SAP à SISNET. Sisnet a valle arricchisce i dati con dati di geolocalizzazione

EAM invia ordini di lavoro con la richiesta di esecuzione ispezioni per rilevare difformità sui cavi di rete. Invia in particolare un ordine aggregato. L’ordine aggregato contiene più ordini singoli ognuno associato alla singola tratta cavo. Nella richiesta d’invocazione EAM passa Ordini di lavoro con chiave EAM, assetnum EAM per usi futuri, sapId chiave SAP della linea e tronco.

Il

### Logging dei messaggi

I messaggi da/verso EAM saranno memorizzati tramite la struttura X4ESITOINT mediante chiamata REST

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINT

I valori da usare per popolare le richieste sono riportati nei rispettivi paragrafi.

Esempio di chiamata

{

"idinterfaccia": "SEND_ODL_TO_CAVI",

"kafkatopic": "topic-kafka",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "CAVI_INTERRATI",

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"end_elab": "2024-06-21T15:30:08+00:00"

}

#### Flusso INVIO ISPEZIONI VERSO CAVI

Questa funzionalità consente ad EAM di propagare gli ordini di lavoro generati tramite il motore DSS verso il sistema CAVI INTERRATI e di conseguenza per richiedere la manutenzione degli asset.

##### Logica di attivazione e tipologia d’interazione

Alla generazione programmata degli ordini di manutenzione sui cavi, è associato un meccanismo event base che genererà un messaggio da inviare tramite kafka al modulo di integrazione, il quale provvederà ad inoltrarlo al sistema CAVI INTERATI, dopo averlo convertito nel formato proprio della destinazione.

##### TOPIC KAFKA

Di seguito la configurazione del nodo per il topic KAFKA :

##### L’ordine di lavoro nel messaggio di kafka

Nella tabella seguente sono riportati i campi che contengono le informazioni necessarie all’invio dell’OdL al sistema CAVI INTERRATI. Nella colonna “CAMPO EAM” sono riportati il nome della colonna e il nome della tabella in cui l’informazione è contenuto nel database di EAM, la notazione è “nome tabella”.”nome colonna”.

Nella colonna CAMPO JSON viene riportato il nome del campo che il sistema di integrazione troverà nel messaggio kafka ricevuto.

Nella colonna CAMPO IFA è riportato il nome del campo corrispondente nel sistema CAVI INTERRATI.

| CAMPO EAM | CAMPO JSON | CAMPO IFA | Descrizione |
| --- | --- | --- | --- |
| WORKORDER.WONUM | wonum / codice ordine di lavoro | wonum / codice ordine di lavoro | Codice ordine di lavoro corrisponde all'id dell'ordine di lavoro aggregato |
| WORKORDER.CHANGEDDATE | CHANGEDDATE | date | data intervento |
| WORKORDER.ASSETNUM | ASSETNUM | lineCode | Codice della linea EAM |
| ASSET.DESCRIPTION |  | lineName | Descrizione della linea |
| WORKORDER.X4TRONCONUM | X4TRONCONUM | trunkCode | Codice del tronco |
| ASSET.X3SEWRK | X3SEWRK | aotCode |  |
| ASSET.X3INGRP | X3INGRP | uiCode |  |
|  |  | planType | Costante valorizzata con "REGULAR" |
| WORKORDER.JPNUM | JPNUM | codiceAttivita | Identificativo univoco dell’attività dell’ordine di lavoro |
|  |  | listaOrdini | Lista di tutte le coppie tratte cavo associate all'ordine aggregato e singolo ordine |

I campi che non hanno una corrispondenza nella colonna CAMPO JSON devono essere risolti dal corrente strato applicativo. Al momento, il messaggio ricevuto contiene per gli oggetti complessi, il link alla risorsa, per cui è compito del consumatore del messaggio, o di un delegato, invocare EAM per recuperare la risorsa da usare per compilare il messaggio da inoltrare a CAVI INTERRATI.

Esempio del messaggio:

{

"_action": "Replace",

"_event": true,

"_id": "SIT01-1589",

"actfinish": null,

"assetnum": "1461224",

"changedate": "2024-07-03T16:27:39+00:00",

"classstructureid": "1686",

"description": "Controllo di legge NON UI",

"href": "https:\/\/masdev.manage.dev.apps.dev.azopencs.servizi.prv\/maximo\/oslc\/os\/x4wo_invio_cavi\/_U0lUMDEvMTU4OQ--",

"jpnum": "TESTWF02",

"parent": null,

"siteid": "SIT01",

"status": "APERTO",

"statusdate": "2024-06-20T19:57:02+00:00",

"wogroup": "1589",

"wonum": "1589",

"x4tipoattivita": null

}

##### CONSUMATORE su EAM INTEGRATION

Il consumatore del topic al punto 3.4.3 si occuperà di leggere le informazioni contenute nel messaggio e trasformarle nel messaggio ricevibile da CAVI INTERRATI.

La conversione sarà fatta in accordo alla

I campi non direttamente valorizzati, saranno recuperati attraverso la chiamata rest alle url contenute nel rispettivo campo del messaggio oppure tramite query sulle tabelle.

Le informazioni relative agli asset possono essere recuperate con la query:

SELECT DESCRIPTION, X3SWERK, X3INGRP FROM ASSET WHERE ASSETNUM = <ASSETNUMVAL>;

##### END POINT su CAVI INTERRATI per il flusso invio ispezioni.

Il messaggio creato dal consumatore deve essere inviato all’endpoint di CAVI INTERRATI <<ENDPOINT>>

PUT https://<hostcavi>:<port>/cavi-interrati/v1.0/uci/mbi/createInspections

{[

{

"date": "2024-06-05",

"lineCode": "string",

"lineName": "string",

"trunkCode": "string",

"aotCode": "string",

"uiCode": "string",

"wonum": "string",

"planType": "REGULAR"

"codiceAttivita": "codice",

"orderList": [

{ singleOrder: “ord1”, trattaCavo: “cavo1”},

{ singleOrder: “ord2”, trattaCavo: “cavo2”},

...

]

}

]}

Una volta effettuata la chiamata a CAVI INTERRATI, registrarne l’esito con la chiamata REST verso la tabella di logging.

I parametri specifici per questo flusso sono:

IDINTERFACCIA= SEND_ODL_TO_CAVI

DEST= CAVI_INTERRATI

SOURCE=EAM

In caso di errore durante la chiamata, il modulo di integrazione proverà a ripetere la chiamata un numero di volte configurato nell’applicazione.

#### Flusso Invio cancellazione controlli

Questa funzionalità consente ad EAM di chiedere la cancellazione di alcuni ordini di lavoro già inoltrati a CAVI INTERRATI che non devono più essere lavorati.

##### Logica di attivazione e tipologia d’interazione

Alla cancellazione di un ordine di lavoro, per qualsiasi motivo, che riguarda un asset inviato tramite un precedente ordine di lavoro, è associato un canale di pubblicazione, che al verificarsi della condizione, genererà un messaggio da inviare tramite kafka ad eam-integration, il quale provvederà ad inoltrarlo alla sua destinazione naturale, il sistema CAVI INTERATI, dopo averlo convertito nel formato proprio della destinazione.

##### TOPIC KAFKA

Il topic kafka da configurare si chiamerà eam-invio-odl-delete-cavi-interrati<-env> (env indica l’ambiente di esecuzione, valori possibili dev, col, training), di seguito tutti i campi necessari per la configurazione:

| Proprietà | Valore |
| --- | --- |
| Nome del nodo | eamCaviInterratiInvioIspezioni |
| entityName | eam-invio-odl-delete-cavi-interrati-dev |
| entityType | TOPIC |
| entityDescription | Kafka topic per inviare le cancellazioni a CAVI INTERRATI |
| entityObjectName | Invio delle cancellazioni a CAVI INTERATI |
| entityObjectClass | it.terna.eam.integration.kafka.messages.WODeleteCAVI |
| entityUuidMethodName | getUUID |

JSON di configurazione di esempio

Esempio di un messaggio:

{

"_action": "Replace",

"_event": true,

"_id": "SIT01-1589",

"assetnum": "1461224",

"changedate": "2024-07-03T16:27:39+00:00",

"href": "https:\/\/masdev.manage.dev.apps.dev.azopencs.servizi.prv\/maximo\/oslc\/os\/x4wo_invio_cavi\/_U0lUMDEvMTU4OQ--",

"jpnum": "TESTWF02",

"parent": null,

"siteid": "SIT01",

"status": "APERTO",

"wonum": "1589",

}

##### Il messaggio di kafka

Nella tabella seguente sono riportati i campi che contengono le informazioni necessarie all’invio dell’OdL al sistema CAVI INTERRATI. Nella colonna “CAMPO EAM” sono riportati il nome della colonna e il nome della tabella in cui l’informazione è contenuto nel database di EAM, la notazione è nometabella.nomecolonna.

Nella colonna CAMPO JSON viene riportato il nome del campo che il sistema di integrazione troverà nel messaggio kafka ricevuto.

Nella colonna CAMPO IFA è riportato il nome del campo corrispondente nel sistema CAVI INTERRATI.

| CAMPO EAM | CAMPO JSON | CAMPO IFA | Descrizione |
| --- | --- | --- | --- |
| Array di codici MBI/EAM | wonum / codice ordine di lavoro | wonum / codice ordine di lavoro | Lista di codici relativi ad asset da eliminare. ES. ["CL_8038745", "CL_8038746" ] |

Nel messaggio potranno arrivare o un array di stringhe con gli identificativi degli ordini di lavoro in chiaro, oppure degli URI, in questo caso, per ogni elemento invocare la GET per recuperare i valori in chiaro.

##### CONSUMATORE su EAM INTEGRATION

Il consumatore del topic al punto 5.3.2.4 si occuperà di leggere le informazioni contenute nel messaggio e trasformarle nel messaggio ricevibile da CAVI INTERRATI.

La conversione sarà fatta in accordo alla tabella 5.3.2.4 I campi non direttamente valorizzati, saranno recuperati attraverso la chiamata rest alle url contenute nel rispettivo campo del messaggio.

##### END POINT su CAVI INTERRATI per il flusso cancella ispezioni.

Il messaggio creato dal consumatore deve essere inviato all’endpoint di CAVI INTERRATI

DELETE /cavi-interrati/v1.0/uci/mbi/deleteInspections

Una volta effettuata la chiamata a CAVI INTERRATI, il modulo di integrazione provvede a registrarne l’esito con la chiamata REST verso la tabella di logging.

I parametri specifici per questo flusso sono:

IDINTERFACCIA= SEND_ODL_DELETE_TO_CAVI

DEST= CAVI_INTERRATI

SOURCE=EAM

In caso di errore durante la chiamata, il modulo di integrazione proverà a ripetere la chiamata un numero di volte
