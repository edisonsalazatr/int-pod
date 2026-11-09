# EAM_Analisi Specifiche Tecniche_Sprint7_V1.4

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Specifica Tecnica – Sprint 7 |

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

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche, già descritte ad alto livello nel documento “EAM_Analisi unzionale_Sprint__v.docx”, verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software.

Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni implementate sulla piattaforma EAM di Terna (ambiente Dev) e gli sviluppi custom da implementare. Ciascun paragrafo (dedicato a una specifica funzionalità nell’applicativo) presenta due sotto-paragrafi: nel primo sono illustrate le configurazioni eseguite sul software (e.g., estensione del modello dati, configurazione dell’applicazione, gestione della condizionalità, etc.), nel secondo gli sviluppi necessari per soddisfare le funzionalità richieste durante lo sprint.

In particolare, i temi inclusi nello Sprint 7 sono i seguenti:

Processo di gestione cataloghi

Layout cataloghi

Anagrafica opere interferenti.

# Sprint 7: Layout cataloghi

## Processo di gestione cataloghi

### Configurazioni

### Sviluppi

Per permettere il monitoraggio nel centro di controllo delle incongruenze derivate dalla gestione/modifica dei cataloghi, si prevedono i seguenti sviluppi a sistema:

Difformità non rilevate da alcuna attività/micro-controllo

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista riepilogativa delle difformità non rilevate da alcuna attività/micro-controllo; nel dettaglio, una difformità viene mostrata nel result set se il PdL che la rileva viene disattivato (stato “NON ATTIVO”) nel catalogo delle attività oppure qualora il micro-controllo che la rileva venga eliminato nel relativo catalogo. Il result set “Difformità non rilevate da alcuna attività/micro-controllo” mostrerà le seguenti colonne: ID Difformità (IDDIFFORMITA), Descrizione della difformità (DESCRIPTION), Classificazione asset associato (TIPOLOGIAASSET), Stato (STATUS).

Difformità non risolte da alcuna attività

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista riepilogativa delle difformità non risolte da alcuna attività; nel dettaglio, una difformità viene mostrata nel result set se il PdL che la risolve viene disattivato (stato “NON ATTIVO” nel catalogo delle attività. Il result set “Difformità non risolte da alcuna attività” mostrerà le seguenti colonne: ID Difformità (IDDIFFORMITA), Descrizione della difformità (DESCRIPTION), Classificazione asset associato (TIPOLOGIAASSET), Stato (STATUS).

Eliminazione di Misure associate a Difformità

In Maximo sarà possibile la visualizzazione nel centro di controllo di una vista riepilogativa delle difformità dalle quali risulta eliminato l’ID misura utilizzato per la generazione; nel dettaglio, una difformità viene mostrata nel result set se la misura associata viene eliminata dal relativo catalogo. Il result set “Difformità non generabili da misure” mostrerà le seguenti colonne: ID Difformità (IDDIFFORMITA), Descrizione della difformità (DESCRIPTION), Classificazione asset associato (TIPOLOGIAASSET), Stato (STATUS).

I medesimi centri di controllo verranno configurati anche per le interferenze, adottando le stesse logiche e mostrando le seguenti colonne: ID Interferenza (IDINTERFERENZA), Descrizione dell’interferenza (DESCRIPTION), Classificazione asset associato (TIPOLOGIAASSET), Stato (STATUS).

Layout cataloghi

Configurazioni

#### Catalogo difformità

Per la gestione del catalogo difformità è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4CATDIFFOR | ASSIEME | Assieme | ALN | 50 |
| X4CATDIFFOR | DESCRIPTION | Descrizione difformità | ALN | 250 |
| X4CATDIFFOR | IDDIFFORMITA | ID difformità | ALN | 30 |
| X4CATDIFFOR | IDMISURA | ID misura | ALN | 100 |
|  |  |  |  |  |
| X4CATDIFFOR | SOTTOASSIEME | Sottoassieme | ALN | 60 |
| X4CATDIFFOR | STATUS | Stato difformità | ALN | 15 |
| X4CATDIFFOR | TEMPOVALID | Massimo tempo di validazione | INT |  |
| X4CATDIFFOR | TIPOLOGIA | Tipologia difformità | ALN | 30 |
| X4CATDIFFOR | TIPOLOGIAASSET | Tipologia asset | UPPER | 192 |
| X4CATDIFFOR | CHANGEBY | Modificato da | UPPER | 100 |
| X4CATDIFFOR | CHANGEDATE | Data modifica | DATETIME |  |
| X4CATDIFFOR | GENMISURA | Generazione da misura | YORN |  |
|  |  |  |  |  |
|  |  |  |  |  |
| X4CATDIFFOR | FASE | Fase | YORN |  |
| X4TARGETDIFFOR | VALUE | Target/Gravità | ALN | 50 |
|  |  |  |  |  |
|  |  |  |  |  |
| X4CARDIFFOR | ATTRIBUTE | Attributo di classificazione asset | UPPER | 50 |
| X4CARDIFFOR | VALUE | Valore | ALN | 254 |
|  |  |  |  |  |
| X4DIFFATT |  | ID attività | ALN |  |
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

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

X4CATDIFFOR: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione del catalogo difformità.

X4TARGETDIFFOR: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione dei target/gravità inerenti alle difformità.

X4CARDIFFOR: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione delle caratteristiche asset inerenti alle difformità.

X4DIFFATT: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione delle attività che risolvono le difformità.

Per permettere la visione dei campi necessari all’interno del Catalogo difformità, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4CATDIFFOR”, si dovranno configurare i campi di testata:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID difformità | IDDIFFORMITA | DESCRIPTION |  |  |
| Stato | STATUS |  |  |  |
| Max tempo di validazione difformità (gg) | TEMPOVALID |  |  |  |
| Elemento | TIPOLOGIAASSET |  | CLASSIFICATION |  |
| Classificazione | X4DIFFORCLASSIFICATIONS.CLASSIFICATIONID |  |  |  |
| Gerarchia asset associato | X4DIFFORCLASSIFICATIONS.CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Generazione | GENMISURA |  |  |  |
| Tipologia difformità | TIPOLOGIA |  | VALUELIST |  |
| Assieme | ASSIEME |  | VALUELIST |  |
| Sotto-assieme | SOTTOASSIEME |  | VALUELIST |  |
| Fase | FASE |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

La relazione “X4DIFFORCLASSIFICATIONS” lega le tabelle X4CATDIFFOR e X4DIFFORLASSIFICATIONS (la tabella di relazione tra difformità e classificazioni) tramite :X4CATDIFFORID=X4CATDIFFORID.

I campi della tabella “Gravità difformità”, caratterizzata dalla relazione X4TARGETDIFFOR:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Gravità | VALUE |  |  |  |
|  |  |  |  |  |

La relazione “X4TARGETDIFFOR” lega le tabelle X4CATDIFFOR e X4TARGETDIFFOR (la tabella di relazione tra difformità e target/gravità) tramite :X4CATDIFFORID=X4CATDIFFORID.

I campi della tabella “Attività che risolvono la difformità”, caratterizzata dalla relazione X4DIFFATT:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività |  | X4JOBPLAN.DESCRIPTION |  |  |

La relazione “X4DIFFATT” lega le tabelle X4CATDIFFOR e X4DIFFATT (la tabella di relazione tra difformità e attività che la risolvono) tramite :IDDIFFORMITA=DIFFORMITA.

I campi della tabella “Caratteristiche asset”, caratterizzata dalla relazione X4CARDIFFOR:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Caratteristica | ATTRIBUTE |  | VALUELIST |  |
| Valore | VALUE |  |  |  |

La relazione “X4CARDIFFOR” lega le tabelle X4CATDIFFOR e X4CARDIFFOR (la tabella di relazione tra difformità e caratteristiche/attributo dell’asset) tramite :X4CATDIFFORID=X4CATDIFFORID.

I campi della tabella “Misure”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID misure | IDMISURA | X4CATMISURE.DESCRIPTION |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

#### Catalogo interferenze

Per la gestione del catalogo interferenze è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4CATINTERF | ASSIEME | Assieme | ALN | 30 |
| X4CATINTERF | DESCRIPTION | Descrizione interferenza | ALN | 250 |
| X4CATINTERF |  | ID interferenza | ALN | 50 |
| X4CATINTERF | IDMISURA | ID misura | ALN | 50 |
|  |  |  |  |  |
| X4CATINTERF | SOTTOASSIEME | Sottoassieme | ALN | 50 |
| X4CATINTERF | STATUS | Stato interferenza | ALN | 20 |
| X4CATINTERF | TEMPOVAL | Massimo tempo di validazione | INT |  |
| X4CATINTERF | TIPOLOGIAASSET | Tipologia asset | UPPER | 192 |
| X4CATINTERF | CHANGEBY | Modificato da | UPPER | 100 |
| X4CATINTERF | CHANGEDATE | Data modifica | DATETIME |  |
| X4CATINTERF | GENMISURA | Generazione da misura | YORN |  |
|  |  |  |  |  |
| X4CATINTERF | FASE | Fase | YORN |  |
|  |  |  |  |  |
| X4TARGETINTERF | VALUE | Target/Gravità | ALN | 50 |
|  |  |  |  |  |
|  |  |  |  |  |
| X4CARDINTERF | ATTRIBUTE | Attributo di classificazione asset | UPPER | 50 |
| X4CARDINTERF | VALUE | Valore | ALN | 254 |
|  |  |  |  |  |
| X4INTERFATT | ATTIVITA | ID attività | ALN | 15 |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

X4CATINTERF: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione del catalogo interferenze.

X4TARGETINTERF: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione dei target/gravità inerenti alle interferenze.

X4CARDINTERF: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione delle caratteristiche asset inerenti alle interferenze.

X4INTERFATT: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione delle attività che risolvono le interferenze.

Per permettere la visione dei campi necessari all’interno del Catalogo interferenze, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4CATINTERF”, si dovranno configurare i campi di testata:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID interferenza | IDINTERFERENZA | DESCRIPTION |  |  |
| Stato | STATUS |  |  |  |
| Max tempo di validazione (gg) | TEMPOVAL |  |  |  |
| Elemento | TIPOLOGIAASSET |  | CLASSIFICATION |  |
| Classificazione | X4INTERFCLASSIFICATIONS.CLASSIFICATIONID |  |  |  |
| Gerarchia asset associato | X4INTERFCLASSIFICATIONS.CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Generazione da misura | GENMISURA |  |  |  |
| Assieme | ASSIEME |  | VALUELIST |  |
| Sotto-assieme | SOTTOASSIEME |  | VALUELIST |  |
| Fase | FASE |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

La relazione “X4INTERFCLASSIFICATIONS” lega le tabelle X4CATINTERF e X4INTERFCLASSIFICATIONS (la tabella di relazione tra interferenza e classificazioni) tramite :X4CATINTERFID=X4CATINTERFID.

I campi della tabella “Target interferenza”, caratterizzata dalla relazione X4TARGETINTERF:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Target/Gravità | VALUE |  |  |  |
|  |  |  |  |  |

La relazione “X4TARGETINTERF” lega le tabelle X4CATINTERF e X4TARGETINTERF (la tabella di relazione tra interferenza e target/gravità) tramite :X4CATINTERFID=X4CATINTERFID.

I campi della tabella “Attività che risolvono l’interferenza”, caratterizzata dalla relazione X4INTERFATT:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | ATTIVITA | X4JOBPLAN.DESCRIPTION |  |  |

La relazione “X4INTERFATT” lega le tabelle X4CATINTERF e X4INTERFATT (la tabella di relazione tra interferenza e attività che la risolvono) tramite :IDINTERFERENZA=INTERFERENZA.

I campi della tabella “Caratteristiche asset”, caratterizzata dalla relazione X4CARDINTERF:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Caratteristica | ATTRIBUTE |  | VALUELIST |  |
| Valore | VALUE |  |  |  |

La relazione “X4CARDINTERF” lega le tabelle X4CATINTERF e X4CARDINTERF (la tabella di relazione tra interferenza e caratteristiche/attributo dell’asset) tramite :X4CATINTERFID=X4CATINTERFID.

I campi della tabella “Misure”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID misure | IDMISURA | X4CATMISURE.DESCRIPTION | X4MISUR |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

Dominio stati catalogo difformità/interferenze

È stato creato un nuovo dominio denominato X4STATUSANOMALIE contenente al suo interno i tre stati caratterizzanti il record nel catalogo difformità/interferenze: ATTIVA, NON ATTIVA, BOZZA.

#### Catalogo modelli-target

Per la gestione del catalogo modelli-target è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4CATMODELLO | IDTARGET | ID Target | ALN | 50 |
| X4CATMODELLO | DESCRIPTION | Descrizione Target | ALN | 50 |
| X4CATMODELLO | TIPOLOGIAASSET | Tipologia asset | UPPER | 192 |
| X4CATMODELLO | VALUE | Valore target massivo | FLOAT |  |
| X4CATMODELLO | X4CATMODELLOID | ID univoco / chiave della tabella | BIGINT |  |
| X4CATMODELLOASSET | X4CATMODELLOID | ID univoco / chiave della tabella | BIGINT |  |
| X4CATMODELLOASSET | VALUE | Valore attributo | ALN | 50 |
| X4CATMODELLOASSET | ATTRIBUTO | Attributo | UPPER | 50 |
| X4CATMODELLOASSET | IDCLUSTER | Modello | INT |  |
| X4CATMODELLOASSET | N1 | Valore target | FLOAT |  |
| X3EQTYPBZ | ZHERST | Codice costruttore componente | ALN | 30 |
| X3EQTYPBZ | ZTYPBZ | Descrizione modello componente | ALN | 20 |
| X3EQTYPBZ | CLASSIFICATIONID | Classificazione equipment | UPPER | 192 |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

X4CATMODELLO: Tabella custom di Maximo; al suo interno sono definiti i campi di testata necessari per la gestione del catalogo modelli-target.

X4CATMODELLOASSET: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione del catalogo modelli-target.

X3EQTYPBZ: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione della tabella di mapping “Costruttore componente / Tipo”.

Per permettere la visione dei campi necessari all’interno del Catalogo modelli-target, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4CATMODELLO”, si dovranno configurare i campi di testata:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Target | IDTARGET | DESCRIPTION |  |  |
| Tipologia asset | TIPOLOGIAASSET |  | CLASSIFICATION |  |
| Gerarchia anagrafica associata | CLASSIFICATION.HIERARCHYPATH |  |  |  |
| Valore | VALUE |  |  |  |

I campi della tabella “Modelli-target”, caratterizzata dalla relazione X4CATMODELLOASSET:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attributo | ATTRIBUTO |  | VALUELIST |  |
| Valore attributo | VALUE |  | X4VALATTRIBUTO |  |
| Modello | IDCLUSTER |  |  |  |
| N | N1 |  |  |  |

La relazione “X4CATMODELLOASSET” lega le tabelle X4CATMODELLOASSET e X4CATMODELLO tramite :X4CATMODELLOID=X4CATMODELLOID.

Inoltre, di seguito riportiamo il dominio associato al campo ATTRIBUTO e i suoi valori ammissibili:

Attributo (X4ATTRIBUTO): LIN_TIPO_CONDUTTORE_AEREO, LIN_TIPO_FUNE_GUARDIA, COSTRUTTORE, MODELLO.

#### Catalogo misure e variabili di stato

Per la gestione del catalogo misure e variabili di stato è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4CATMISURE | IDMISURA | ID misura/variabile | ALN | 20 |
| X4CATMISURE | DESCRIPTION | Descrizione misura/variabile | ALN | 200 |
|  |  |  |  |  |
|  |  |  |  |  |
| X4CATMISURE | CALCOLO | Calcolo | ALN | 150 |
| X4CATMISURE | TIPOLOGIAASSET | Tipologia asset | UPPER | 192 |
| X4CATMISURE | RESET | Reset | YORN |  |
|  |  |  |  |  |
| X4CATMISURE | TIPOPARAMETRO | Tipo parametro | ALN | 20 |
|  |  |  |  |  |
| X4CATMISURE | PERIODOCALCOLO | Periodo di calcolo (gg) | INT |  |
| X4CATMISURE | UM | Unità di misura | ALN | 40 |
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

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

X4CATMISURE: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione del catalogo misure e variabili di stato.

Per permettere la visione dei campi necessari all’interno del Catalogo misure e variabili di stato, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4CATMISURE”, si dovranno configurare i campi:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Misura / variabile | IDMISURA | DESCRIPTION |  |  |
|  |  |  |  |  |
| Calcolo | CALCOLO |  |  |  |
| Asset | TIPOLOGIAASSET |  | CLASSIFICATION |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Reset? | RESET |  |  |  |
|  |  |  |  |  |
| Tipo parametro | TIPOPARAMETRO |  | VALUELIST |  |
|  |  |  |  |  |
| Periodo di calcolo | PERIODOCALCOLO |  |  |  |
| Unità di misura | UM |  | VALUELIST |  |
|  |  |  |  |  |

La sigoption “X4RESET” rende visibili i campi quando il campo RESET è pari a 1Inoltre, di seguito riportiamo i domini associati ai campi e i loro valori ammissibili:

Tipo parametro (X4TIPOPARAMETRO): Variabile di stato, Misura.

Unità di misura (X4UMISURA): millisec, secondi, ore, giorni, anni, A, I/s, avv/giorno, n°, 0:Falso, 1:Vero (solo per variabili statiche), kA, Nm, V, °C, mA, uA, ppm, data, % in peso, %.

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |

Catalogo micro-controlli

Per la gestione del catalogo micro-controlli è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4MICROCONTROLLI | IDMICROCONTROLLO | ID microcontrollo | ALN | 15 |
| X4MICROCONTROLLI | DESCRIPTION | Descrizione microcontrollo | ALN | 270 |
|  |  |  |  |  |
| X4MICROCONTROLLI | TIPOLOGIAASSET | Tipologia asset | UPPER | 192 |
| X4MICROCONTROLLI | ASSIEME | Assieme | ALN | 20 |
| X4MICROCONTROLLI | SOTTOASSIEME | Sottoassieme | ALN | 20 |
| X4MICROCONTROLLI | CHANGEDBY | Modificato da | UPPER | 100 |
| X4MICROCONTROLLI | CHANGEDATE | Data modifica | DATETIME |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| X4CATASSOCIAZIONE | IDDIFFORMITA | ID Difformità | ALN | 20 |
| X4CATASSOCIAZIONE | IDORIGINE | ID origine | ALN | 20 |
| X4CATASSOCIAZIONE | IDINTERFERENZA | ID Interferenza | ALN | 20 |
| X4CATDIFFOR | IDDIFFORMITA | ID Difformità | ALN | 20 |
| X4CATINTERF | IDINTERFERENZA | ID Interferenza | ALN | 20 |
| X4CARDMIC | IDMICROCONTROLLO | ID Microcontrollo | ALN | 15 |
| X4CARDMIC | ATTRIBUTE | Attributo di classificazione asset | UPPER | 50 |
| X4CARDMIC | VALUE | Valore | ALN | 254 |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

X4MICROCONTROLLI: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione del catalogo microcontrolli.

X4CATASSOCIAZIONE: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione del catalogo associazione microcontrolli-difformità/interferenze.

X4CARDMIC: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione delle caratteristiche asset inerenti ai microcontrolli.

X4CATDIFFOR: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione del catalogo difformità.

X4CATINTERF: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione del catalogo interferenze.

Per permettere la visione dei campi necessari all’interno del Catalogo micro-controllo, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4MICROCONTROLLI”, si dovranno configurare i campi di testata:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Microcontrollo | IDMICROCONTROLLO | DESCRIPTION |  |  |
|  |  |  |  |  |
| asset | TIPOLOGIAASSET |  | CLASSIFICATION |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Assieme | ASSIEME |  |  |  |
| Sottoassieme | SOTTOASSIEME |  |  |  |

I campi della tabella “Difformità rilevabili”, caratterizzata dalla relazione X4ASSOCDIFFORMITA:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Difformità | IDDIFFORMITA |  | X4IDDIFFORMITA |  |
| Descrizione | DIFFORMITA.DESCRIPTION |  |  |  |

La relazione “X4ASSOCDIFFORMITA” lega le tabelle X4MICROCONTROLLI e X4CATASSOCIAZIONE (la tabella di relazione tra microcontrolli e il catalogo associazione microcontrolli / difformità) tramite la query :IDMICROCONTROLLO=IDORIGINE and IDDIFFORMITA is not null.

La relazione “DIFFORMITA” lega le tabelle X4CATASSOCIAZIONE e X4CATDIFFOR (la tabella di relazione tra catalogo associazione microcontrolli / difformità e difformità) tramite la query IDDIFFORMITA=:IDDIFFORMITA.

I campi della tabella “Interferenze rilevabili”, caratterizzata dalla relazione X4ASSOCINTERFERENZA:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Interferenza | IDINTERFERENZA |  | X4IDINTERFERENZA |  |
| Descrizione | INTERFERENZA.DESCRIPTION |  |  |  |

La relazione “X4ASSOCINTERFERENZA” lega le tabelle X4MICROCONTROLLI e X4CATASSOCIAZIONE (la tabella di relazione tra microcontrolli e il catalogo associazione microcontrolli / interferenza) tramite la query :IDMICROCONTROLLO=IDORIGINE and IDINTERFERENZA is not null.

La relazione “INTERFERENZA” lega le tabelle X4CATASSOCIAZIONE e X4CATINTERF (la tabella di relazione tra catalogo associazione microcontrolli / interferenza e interferenza) tramite la query IDINTERFERENZA=:IDINTERFERENZA.

I campi della tabella “Caratteristiche asset”, caratterizzata dalla relazione X4CARDMIC:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Caratteristica | ATTRIBUTE |  | X4ATTRIBUTE |  |
| Valore | VALUE |  |  |  |

La relazione “X4CARDMIC” lega le tabelle X4MICROCONTROLLI e X4CARDMIC (la tabella di relazione tra microcontrollo e caratteristiche/attributo dell’asset) tramite la query :IDMICROCONTROLLO= IDMICROCONTROLLO.

condizione tecnica

Per la gestione della scheda di condizione tecnica è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| ASSET | ASSETNUM | Identificativo asset | UPPER | 25 |
|  |  |  |  |  |
| ASSET | DESCRIPTION | Descrizione | ALN | 50 |
| ASSET | STATUS | Stato | ALN | 20 |
| ASSET | PARENT | Padre dell’asset | UPPER | 25 |
| ASSET | INSTALLDATE | Data di installazione | DATE |  |
|  |  |  |  |  |
|  |  |  |  |  |
| CLASSSTRUCTURE | HIERARCHYPATH | Gerarchia asset associata | ALN | 254 |
| ASSETSPEC | ASSETATTRID | ID attributo | UPPER | 50 |
| ASSETSPEC | DATEVALUE | Valore data | DATE |  |
| ASSETSPEC | ALNVALUE | Valore alfanumerico | ALN | 254 |
| ASSETSPEC | NUMVALUE | Valore numerico | DECIMAL |  |
| ASSETSPEC | MEASUREUNITID | Unità di misura | UPPER | 16 |
| ASSETSPEC | TABLEVALUE | Valore tabella | ALN | 254 |
| ASSETATTRIBUTE | DATATYPE | Tipo di valore | UPPER | 8 |
| ASSETATTRIBUTE | DESCRIPTION | Descrizione | ALN | 50 |
| WORKORDER | WONUM | Identificativo OdL | UPPER | 25 |
| WORKORDER | JPNUM | Identificativo PdL | ALN | 15 |
| WORKORDER | DESCRIPTION | Descrizione OdL | ALN | 50 |
| WORKORDER | STATUS | Stato | UPPER | 16 |
| X4DIFFORSTAT | IDDIFFORMITA | ID difformità |  |  |
| X4DIFFORSTAT | GRAVITA | Gravità | ALN | 50 |
| X4DIFFORSTAT | STATUS | Stato | ALN | 15 |
| X4DIFFORSTAT | PRIMARILEVAZIONE | Data prima rilevazione | DATETIME |  |
| X4CATDIFFOR | TIPOLOGIA | Tipologia | ALN | 15 |
| X4DIFFOR_WO | WONUM | Identificativo OdL | UPPER | 25 |
| X4INTERFSTAT | IDINTERFERENZA | ID interferenza | BIGINT |  |
| X4INTERFSTAT | GRAVITA | Gravità | ALN | 50 |
| X4INTERFSTAT | STATUS | Stato | ALN | 15 |
| X4INTERFSTAT | PRIMARILEVAZIONE | Prima rilevazione | DATETIME |  |
| X4INTERF_WO | WONUM | Identificativo OdL | UPPER | 25 |
| AHSCORE | ATTRIBUTENAME | Attributo | UPPER | 50 |
| AHSCORE | VALUE | Valore | DECIMAL |  |
| AHSCORE | OBJECTNAME | Nome oggetto | UPPER | 30 |
| X4MISUREASSET | IDMISURA | ID misura | ALN | 50 |
| X4MISUREASSET | DESCRIPTION | Descrizione | ALN | 50 |
| X4MISUREASSET | VALORE | Valore | FLOAT | 8 |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

ASSET: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione dell’anagrafica linee e stazioni.

ASSETSPEC: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione delle specifiche tecniche dell’asset.

ASSETATTRIBUTE: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione degli attributi dell’asset.

WORKORDER: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione degli OdL.

CLASSSTRUCTURE: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione delle classificazioni degli asset.

JOBPLAN: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione dei PdL.

AHSCORE: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione degli score degli asset.

X4DIFFORSTAT: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione delle difformità relazionate all’anagrafica.

X4DIFFOR_WO: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione delle rilevazioni di difformità.

X4CATDIFFOR: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione del catalogo delle difformità.

X4INTERFSTAT: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione delle interferenze relazionate all’anagrafica.

X4INTERF_WO: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione delle rilevazioni di interferenze.

X4MISUREASSET: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione delle misure relazionate all’anagrafica.

Per permettere la visione dei campi necessari alla gestione della scheda di condizione tecnica dell’asset, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione Custom X4SCHEDATECNICA, clone dell’applicazione standard ASSET, si dovranno configurare:

I campi di testata del tab “Attributi di anagrafica”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Asset | ASSETNUM | DESCRIPTION |  |  |
|  |  |  |  |  |
| Stato | STATUS |  |  |  |
| Classificazione | CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Padre | PARENT |  |  |  |
| Data installazione | INSTALLDATE |  |  |  |
| Allegati |  |  |  |  |

I campi della sezione “Specifiche tecniche” del tab “Attributi di anagrafica”, caratterizzata dalla relazione ASSETSPEC:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attributo | ASSETATTRID | ASSETATTRIBUTE.DESCRIPTION |  |  |
| Tipo | ASSETATTRIBUTE.DATATYPE |  |  |  |
| Valore data | DATEVALUE |  |  |  |
| Valore alfanumerico | ALNVALUE |  |  |  |
| Valore numerico | NUMVALUE |  |  |  |
| Unità di misura | MEASUREUNITID |  |  |  |
| Valore di tabella | TABLEVALUE |  |  |  |

La relazione ASSETSPEC lega le tabelle ASSET e ASSETSPEC mediante :ASSETNUM=ASSETNUM. La relazione ASSETATTRIBUTE lega le tabelle ASSETSPEC e ASSETATTRIBUTE mediante :ASSETATTRID=ASSETATTRID.

I campi di testata del tab “Attività associate”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Asset | ASSETNUM | DESCRIPTION |  |  |
|  |  |  |  |  |
| Stato | STATUS |  |  |  |
| Classificazione | CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Padre | PARENT |  |  |  |
| Data installazione | INSTALLDATE |  |  |  |

I campi della sezione “Attività” del tab “attività associate”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID OdL | WORKORDER.WONUM |  |  |  |
| ID PdL | WORKORDER.JPNUM |  |  |  |
| Descrizione PdL | WORKORDER.DESCRIPTION |  |  |  |
| Status OdL | WORKORDER.STATUS |  |  |  |

La relazione WORKORDER lega le tabelle ASSET e WORKORDER mediante :ASSETNUM=ASSETNUM

I campi della sotto-sezione “Micro-controlli” della sezione “Attività”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Micro-controllo | X4WOMICRO.WONUM |  |  |  |
| Descrizione Micro-controllo | X4WOMICRO.DESCRIPTION |  |  |  |

La relazione X4WOMICRO lega le tabelle WORKORDER al record di asset corrente, mediante il collegamento logico :ASSETNUM= ASSETNUM and x4microcontrollo is not null

I campi di testata del tab “Difformità/Interferenze associate”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Asset | ASSETNUM | DESCRIPTION |  |  |
|  |  |  |  |  |
| Stato | STATUS |  |  |  |
| Classificazione | CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Padre | PARENT |  |  |  |
| Data installazione | INSTALLDATE |  |  |  |

I campi della sezione “Difformità” del tab “Difformità/Interferenze associate”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Difformità | X4DIFFORSTAT.IDDIFFORMITA | X4DIFFORSTAT.DESCRIPTION |  |  |
| Tipologia difformità | X4DIFFORSTAT.X4CATDIFFORMITA.TIPOLOGIA |  |  |  |
| Gravità | X4DIFFORSTAT.GRAVITA |  |  |  |
| Stato difformità | X4DIFFORSTAT.STATUS |  |  |  |
| Data rilevazione | X4DIFFORSTAT.PRIMARILEVAZIONE |  |  |  |

La relazione X4DIFFORSTAT lega le tabelle ASSET e X4DIFFORSTAT mediante :ASSETNUM = ASSETNUM.

La relazione X4CATDIFFORMITA lega le tabelle X4DIFFORSTAT e X4CATDIFFOR mediante IDDIFFORMITA=:IDDIFFORMITA_EXT. Nella tabella sarà presente una colonna contenente un pulsante cliccabile che aprirà una dialog. Tale dialog sarà relazionata attraverso la relazione X4DIFFORWO che mette in relazione il record di X4DIFFORSTAT con tutte le righe di X4DIFFOR_WO che l’hanno alimentato (così da poter vedere tutti gli OdL relazionati alla singola difformità). La dialog al suo interno conterrà una tabella riportante le seguenti informazioni:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID OdL | WONUM |  |  |  |
| ID PdL | WORKORDER.JPNUM | WORKORDER.DESCRIPTION |  |  |
| Tipologia PdL | WORKORDER.JOBPLAN.CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |

La relazione WORKORDER lega le tabelle X4DIFFOR_WO e WORKORDER mediante :WONUM=WONUM. La relazione JOBPLAN lega le tabelle WORKORDER e JOBPLAN mediante :JPNUM=JPNUM.

I campi della sezione “Interferenze” del tab “Difformità/Interferenze associate”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Interferenza | X4INTERFSTAT.IDDIFFORMITA | X4INTERFSTAT.DESCRIPTION |  |  |
| Gravità | X4INTERFSTAT.GRAVITA |  |  |  |
| Stato interferenza | X4INTERFSTAT.STATUS |  |  |  |
| Data rilevazione | X4INTERFSTAT.PRIMARILEVAZIONE |  |  |  |

La relazione X4INTERFSTAT lega le tabelle ASSET e X4INTERFSTAT mediante :ASSETNUM = ASSETNUM. La relazione X4CATINTERFERENZA lega le tabelle X4INTERFSTAT e X4CATINTERF mediante IDINTERFERENZA = :IDINTERFERENZA_EXT. Nella tabella sarà presente una colonna contenente un pulsante cliccabile che aprirà una dialog. Tale dialog sarà relazionata attraverso la relazione X4INTERFWO che mette in relazione il record di X4INTERFSTAT con tutte le righe di X4INTERF_WO che l’hanno alimentato (così da poter vedere tutti gli OdL relazionati alla singola interferenza). La dialog al suo interno conterrà una tabella riportante le seguenti informazioni:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID OdL | WONUM |  |  |  |
| ID PdL | WORKORDER.JPNUM | WORKORDER.DESCRIPTION |  |  |
| Tipologia PdL | WORKORDER.JOBPLAN. CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |

La relazione WORKORDER lega le tabelle X4INTERF_WO e WORKORDER mediante :WONUM=WONUM. La relazione JOBPLAN lega le tabelle WORKORDER e JOBPLAN mediante :JPNUM=JPNUM.

I campi di testata del tab “Opere interferenti associate”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Asset | ASSETNUM | DESCRIPTION |  |  |
|  |  |  |  |  |
| Stato | STATUS |  |  |  |
| Classificazione | CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Padre | PARENT |  |  |  |
| Data installazione | INSTALLDATE |  |  |  |

I campi della sezione “Opere interferenti” del tab “Opere interferenti associate”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Asset | ASSETNUM |  |  |  |
| ID intersezione opera | X4INTOPERA.ALNVALUE |  |  |  |
| ID opera interferente | X4IDOPERA.ALNVALUE |  |  |  |
| Tipologia | X4TIPOLOGIAOI.ALNVALUE |  |  |  |
| Criticità | X4CRITICITAOI.ALNVALUE |  |  |  |
| Stato | X4STATUSOI.ALNVALUE |  |  |  |
| SAP-ID linea interferente | X4SAPIDOI.ALNVALUE |  |  |  |

Le relazioni riportate nella tabella servono per collegare il singolo record di Asset di tipo Opera interferente con i valori della tabella “ASSETSPEC” (le rispettive specifiche tecniche), dove l’assetattrid (il nome dell’attributo tecnico), è pari rispettivamente a: LIN_ID_INTERSEZIONE_OPERA, LIN_ID_OPERA_INT, LIN_TIPO_INT, LIN_CRITICITA_LIV4_INT, LIN_STATO_INT LIN_SAPID_LINEA_INT.

I campi di testata del tab “Indicatori di condizione tecnica”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Asset | ASSETNUM | DESCRIPTION |  |  |
|  |  |  |  |  |
| Stato | STATUS |  |  |  |
| Classificazione | CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Padre | PARENT |  |  |  |
| Data installazione | INSTALLDATE |  |  |  |

I campi della sezione “Indicatori di condizione tecnica” del tab “Indicatori di condizione tecnica”, caratterizzata dalla relazione X4AHSCORE:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Nome HI | ATTRIBUTENAME |  |  |  |
| Valore HI | VALUE |  |  |  |

Gli indicatori di condizione tecnica vengono mostrati per relazione X4AHSCORE, prelevando dalla tabella AHSCORE tutti i record dove OWNERRECORDID è pari all’asset e il campo OBJECTNAME=ASSET.

I campi di testata del tab “Misure e variabili di stato”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Asset | ASSETNUM | DESCRIPTION |  |  |
|  |  |  |  |  |
| Stato | STATUS |  |  |  |
| Classificazione | CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Padre | PARENT |  |  |  |
| Data installazione | INSTALLDATE |  |  |  |

I campi della sezione “Misure e variabili di stato” del tab “Misure e variabili di stato”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID OdL | X4MISUREWO.WONUM |  |  |  |
| ID Misura/Variabile | IDMISURA | DESCRIPTION |  |  |
| Valore | VALORE |  |  |  |

La relazione X4MISUREWO lega le tabelle ASSET e A_X4MISUREASSET mediante :ASSETNUM= ASSETNUM.

Per permettere la visualizzazione dello storico delle variabili di stato associate all’asset, si prevede la seguente configurazione:

Storico variabili di stato

Per poter garantire il mantenimento dello storico dei valori assegnati alle variabili di stato, viene attivata la funzione di e-audit sulla tabella X4MISUREASSET (impostato l’EAUDITTIMESTAMP). Attivando lo storico sulla tabella, ciò permette di poter generare automaticamente sulla tabella A_X4MISUREASSET una nuova riga ogni qualvolta viene variato l’attributo “VALORE” presente all’interno della tabella. La relazione sopra-citata permetterà dunque di mostrare in applicazione N righe per la stessa variabile di stato (per lo stesso asset), specificandone il valore che quella variabile possedeva ed a quale momento storico.

Sviluppi

#### Catalogo difformità

Per permettere la gestione del catalogo difformità, si prevedono i seguenti sviluppi a sistema:

Valorizzazione campi

La valorizzazione dei campi automatici o necessari di una particolare customizzazione del catalogo difformità è definita come segue:

Classificazione, automatico in base al campo TIPOLOGIAASSET selezionato, prelevando i record ammissibili nella tabella X4DIFFORCLASSIFICATIONS

Gerarchia asset associato, automaticamente sulla base del livello asset associato; viene definito mediante relazione, prelevando il campo “HIERARCHYPATH” della classificazione asset

Tipologia difformità, inserimento manuale tramite dominio: Anomalia, Guasto, Anomalia + Guasto

ID misura, inserimento manuale tramite dominio X4MISURche preleva le misure di X4CATMISURE

Caratteristiche asset,

Attività, X4DIFFATT.

Visualizzazione storico

Viene abilitato l’audit su tutti i campi del catalogo difformità; la funzionalità “Visualizza storico” mostrerà una vista contenente le seguenti colonne per ciascuna modifica: Stato (STATUS), Modificato da (CHANGEBY) e Data (CHANGEDATE). Inoltre, espandendo la singola riga sarà possibile visualizzare tutti i dati del catalogo difformità relativi alla CHANGEDATE selezionata. La tabella sarà scaricabile in formato Excel in qualsiasi momento.

Propagazione modifica descrizione

Il campo DESCRIPTION di X4CATDIFFOR deve essere modificabile dall’utente e il sistema deve prevedere uno script di automazione che permetta la propagazione di tale modifica su tutte le difformità (in qualsiasi stato della lista definita in X4DIFFSTATUS).

Gestione tipologia “Anomalia + Guasto”

Nel caso il campo TIPOLOGIA di X4CATDIFFOR sia pari a “Anomalia + Guasto”, il sistema definirà tramite script di automazione l’appartenenza ad una delle due tipologie in base al campo CLASSIFICATIONID di CLASSSTRUCTURE (in fase di creazione dell’OdL); nel dettaglio, se quest’ultimo è pari a ‘CONTROLLO TECNICO’ o ‘CONTROLLO PERIODICO’ allora TIPOLOGIA = ‘Anomalia’, se CLASSIFICATIONID = ‘CONTROLLO OCCASIONALE’ allora TIPOLOGIA = ‘Guasto’.

Gestione generazione da misura

Nel caso in cui il campo GENMISURA di X4CATDIFFOR sia pari a 1, il sistema renderl’inserimento di un nellatabella.

Compilazione campo LOGICA

Il campo della tabella è un ALN, in cui l’utente può inserire delle formule elementari (ad esempio). In seguito, il sistema mediante script di automazione verifica che nel campo siano presenti solo i seguenti caratteri speciali: ; in caso contrario, sarà mostrato un messaggio di errore.

Passaggio manuale in stato ATTIVO

Il sistema permetterà il passaggio di stato di una difformità da BOZZA ad ATTIVO, mediante funzionalità laterale “Cambia stato”, solo se campi obbligatori risultano compilati: Tipologia asset (TIPOLOGIAASSET), Gerarchia asset associato (X4DIFFORCLASSIFICATIONS.CLASSSTRUCTURE.HIERARCHYPATH), ID difformità (IDDIFFORMITA), Descrizione (DESCRIPTION), Massimo tempo di validazione (TEMPOVALID), Tipologia (TIPOLOGIA).

#### Catalogo interferenze

Per permettere la gestione del catalogo interferenze, si prevedono i seguenti sviluppi a sistema:

Valorizzazione campi

La valorizzazione dei campi automatici o necessari di una particolare customizzazione del catalogo interferenze è definita come segue:

Classificazione, automatico in base al campo TIPOLOGIAASSET selezionato, prelevando i record ammissibili nella tabella X4INTERFCLASSIFICATIONS

Gerarchia asset associato, automaticamente sulla base del livello asset associato; viene definito mediante relazione, prelevando il campo “HIERARCHYPATH” della classificazione asset

ID misura, inserimento manuale tramite dominio X4MISUR che preleva le misure di X4CATMISURE

Caratteristiche asset,

Attività,

Visualizzazione storico

Viene abilitato l’audit su tutti i campi del catalogo interferenze; la funzionalità “Visualizza storico” mostrerà una vista contenente le seguenti colonne per ciascuna modifica: Stato (STATUS), Modificato da (CHANGEBY) e Data (CHANGEDATE). Inoltre, espandendo la singola riga sarà possibile visualizzare tutti i dati del catalogo interferenze relativi alla CHANGEDATE selezionata. La tabella sarà scaricabile in formato Excel in qualsiasi momento.

Propagazione modifica descrizione

Il campo DESCRIPTION di X4CATINTERF deve risultare modificabile dall’utente e il sistema deve prevedere uno script di automazione che permetta la propagazione di tale modifica su tutte le interferenze (in qualsiasi stato della lista definita in X4DIFFSTATUS).

Gestione generazione da misura

Nel caso in cui il campo GENMISURA di X4CATINTERF sia pari a 1, il sistema render l’inserimento di un nella tabella

Gestione logica di generazione dell’interferenza

Nella fase di di una qualsiasi misura (aggiunta di un record nel), il sistema verifica che tale misura sia collegata ad una interferenza; ovvero, nel catalogo X4CATINTERF esista una riga con IDMISURA pari alla nuova misura . Conseguentemente, il sistema effettua un controllo mediante script di automazione della condizione elementare inserita nelcampo LOGICA: se la condizione viene soddisfatta, il sistema procede con la creazione della interferenza/assegnazione gravità.

Compilazione campo LOGICA

Il campo LOGICA della tabella è un ALN, in cui l’utente può inserire delle formule elementari (ad esempio, X=1, X>TRG.INTSF6.5). In seguito, il sistema mediante script di automazione verifica che nel campo siano presenti solo i seguenti caratteri speciali: ^ * / + - , ( ) > < = , in caso contrario, sarà mostrato un messaggio di errore..

Passaggio manuale in stato ATTIVO

Il sistema permetterà il passaggio di stato di una interferenza da BOZZA ad ATTIVO, mediante funzionalità laterale “Cambia stato”, solo se campi obbligatori risultano compilati: Tipologia asset (TIPOLOGIAASSET), Gerarchia asset associato (X4INTERFCLASSIFICATIONS.CLASSSTRUCTURE.HIERARCHYPATH), ID interferenza (IDINTERFERENZA), Descrizione (DESCRIPTION), Massimo tempo di validazione (TEMPOVALID).

#### Catalogo modelli-target

Per permettere la gestione del catalogo modelli-target, si prevedono i seguenti sviluppi a sistema:

Funzionalità “Genera lista modelli”

Per aggiungere massivamente valori nella tabella “Modelli-target”, recuperati dall’anagrafica in base al campo TIPOLOGIAASSET di X4CATMODELLO, si prevede la funzionalità custom “Genera lista modelli” nelle azioni laterali dell’applicazione X4CATMODELLOASSET. Nel dettaglio, mediante uno script di automazione, i campi ATTRIBUTO e VALUE della tabella X4CATMODELLOASSET verranno popolati come segue:

Se TIPOLOGIAASSET = ‘CONDUTTORE’ (o ‘FUNE DI GUARDIA’), allora si avranno n-righe con ATTRIBUTO = ‘LIN_TIPO_CONDUTTORE_AEREO’ (o ‘LIN_TIPO_FUNE_GUARDIA’) e VALUE uguale agli n-valori presenti nel dominio X3DOMALN15 (o X3DOMALN16)

Se TIPOLOGIAASSET è un equipment di stazione, allora si preleveranno i valori dalla tabella di mapping “Costruttore componente / Tipo” (X3EQTYPBZ); nel dettaglio, il sistema creerà due righe per ogni record della tabella X3EQTYPBZ come segue: riga 1 ATTRIBUTO = ‘COSTRUTTORE’, IDCLUSTER = n, VALATTRIBUTO pari al campo ZHERST della tabella X3EQTYPBZ; riga 2 ATTRIBUTO = ‘MODELLO’, IDCLUSTER = n, VALATTRIBUTO pari al campo ZTYPBZ della tabella X3EQTYPBZ, dove n = numero intero che incrementerà di 1 per ogni record della tabella di mapping “Costruttore componente / Tipo”.

Controllo valorizzazione campo “IDCLUSTER”

Nel caso in cui il campo ATTRIBUTO sia pari a “COSTRUTTORE” o “MODELLO”, il "Modello" (IDCLUSTER della tabella X4CATMODELLOASSET) dovrà essere obbligatoriamente valorizzato e il sistema dovrà verificare, mediante automation script, che il campo "Valore" (N1 della tabella X4CATMODELLOASSET) delle righe associate al medesimo IDCLUSTER sia uguale (e.g., se record 1: ATTRIBUTO = 'COSTRUTTORE' e IDCLUSTER = 1; record 2: ATTRIBUTO = 'MODELLO' e IDCLUSTER = 1, allora il campo N1 delle due righe dovrà assumere lo stesso valore). Contrariamente, il sistema bloccherà il salvataggio mostrando un messaggio di errore.

Eliminazione modello

Nel caso di eliminazione di un attributo di stazione nella tabella “Modelli-target” lo script dovrà eliminare contestualmente le righe che presentano il medesimo IDCLUSTER a parità di IDTARGET. Inoltre, il sistema dovrà controllare che a seguito dell’eliminazione di un modello, lo stesso non sia presente in anagrafica; in particolare:

Linee: nel caso dell’attributo LIN_TIPO_CONDUTTORE_AEREO il valore non deve essere elencato nel dominio X3DOMALN15, mentre nel caso dell’attributo LIN_TIPO_FUNE_GUARDIA il valore non deve essere presente nel dominio X3DOMALN16

Stazioni: il valore non deve essere presente nella tabella di mapping “Costruttore componente / Tipo” (X3EQTYPBZ).

In caso contrario, il sistema dovrà restituire il warning “Rimosso modello presente in anagrafica”.

Valorizzazione campi

La valorizzazione dei campi automatici io necessari di una particolare customizzazione del catalogo è definita come segue:

Tipologia asset, manuale da lista valori; il campo sarà reso in sola lettura a seguito dell’inserimento di un record nella tabella X4CATMODELLOASSET associato all’IDTARGET corrente di X4CATMODELLO

Gerarchia asset associato, automaticamente sulla base del livello asset associato; viene definito mediante relazione, prelevando il campo “HIERARCHYPATH” della tipologia asset e non è editabile

Valore attributo, manuale da lista valori (X4VALATTRIBUTO); il dominio associato al campo mostrerà i valori in base all’attributo selezionato nel campo ATTRIBUTO della tabella X4CATMODELLOASSET (ad esempio, selezionando LIN_TIPO_CONDUTTORE_AEREO, saranno mostrati solo i valori presenti nel dominio associato a quest’ultimo, ovvero X3DOMALN15).

Modifica massiva del valore dei modelli associati al target

Per modificare massivamente tutti i valori dei modelli associati al target, si prevede l’implementazione del pulsante custom “Aggiorna” che, mediante un automation script, aggiornerà tutti i campi VALUE della tabella X4CATMODELLOASSET con il valore presente nel campo VALUE della tabella X4CATMODELLO.

Funzionalità “Modifica valore massivo”

Per modificare selettivamente i valori dei modelli associati al target, si prevede l’implementazione della funzionalità custom “Modifica valore massiva”; nel dettaglio, mediante un automation script il sistema aggiornerà i campi VALUE della tabella X4CATMODELLOASSET delle righe selezionate mediante la checkbox “selectrow” con il valore presente nel campo VALUE della tabella X4CATMODELLO.

Visualizzazione storico

Viene abilitato l’audit su tutti i campi del catalogo modelli-target; la funzionalità “Visualizza storico” è accessibile per ogni riga della tabella X4CATMODELLOASSET e mostrerà una vista contenente le seguenti colonne per ciascuna modifica: Modificato da (CHANGEBY) e Data (CHANGEDATE). Inoltre, espandendo la singola riga sarà possibile visualizzare tutti i dati del catalogo modelli-target relativi alla CHANGEDATE selezionata. La tabella sarà scaricabile in formato Excel in qualsiasi momento.

#### Catalogo misure e variabili di stato

Visualizzazione storico

Viene abilitato l’audit su tutti i campi del catalogo misure e variabili di stato; la funzionalità “Visualizza storico” è accessibile per ogni riga di IDMISURA della tabella X4CATMISURE e mostrerà una vista contenente le seguenti colonne per ciascuna modifica: Modificato da (CHANGEBY) e Data (CHANGEDATE). Inoltre, espandendo la singola riga sarà possibile visualizzare tutti i dati del catalogo misure e variabili di stato relativi alla CHANGEDATE selezionata. La tabella sarà scaricabile in formato Excel in qualsiasi momento.

Sintassi del campo CALCOLO

Il campo CALCOLO della tabella X4CATMISURE viene predisposto con alcune formule codificate che permettono di calcolare direttamente l'output di una variabile, nel dettaglio:

DIFFANNI, calcola la differenza in anni tra la data di installazione

Calcolata in anni (numero intero) sottraendo l'anno della sysdate all'anno della data contenuta nell'attributo STZ_DATA_IN_SERVIZIO dell'asset per le stazioni e LIN_ANNO_INSTALLAZIONE per le linee. Viene calcolata il 01 Gennaio di ogni anno su trigger temporale per tutti gli asset che sono in stato diverso da dismesso.

COUNT, calcola l'ammontare di entità presenti con l'ID specificato nel campo

Calcolato per il singolo asset (in qualsiasi stato) contando quanti ordini (in qualsiasi stato) con il PdL in input (indipendentemente dallo stato del PdL) sono associati all'asset nel periodo di tempo specificato nel campo "Periodo di calcolo" (considerandolo come giorni nel passato). La data dell'ordine considerata è la data modello. Se non sono presenti attività sarà valorizzato con zero.

Il calcolo deve essere eseguito tutti i giorni attivato da schedulazione temporale

DATAACT, calcola la differenza tra la SYSDATE e la data di ultima

Calcolata in giorni sottraendo alla data attuale del sistema (sysdate) il valore contenuto nel campo "data esecuzione" (data in cui l'ordine è passato in stato "eseguito") dell'ordine più recente associato (con data esecuzione più vicina nel tempo) all'asset e riferito al PdL in input. Se non ci sono ordini che soddisfano le condizioni, la varabile non viene calcolata.

DATAOPT, calcola la differenza tra la prossima data e la SYSDATE.

Calcolata in giorni sottraendo alla data riportata nel campo "data pianificata fine" (TARGCOMPDATE) dell'ordine più recente associato all'asset e riferito ai PdL in input, la data attuale del sistema (sysdate). Vengono considerati tutti gli ordini sull'asset in uno dei seguenti stati: 'APERTO', 'DA VALIDARE','SCHEDULABILE','SCHEDULATO',

'INVIATO','AVVIATO','ESEGUIBILE'. Tra quelli individuati si sceglie quello con data modello più prossima alla data attuale. Viene conteggiato sia su base temporale ogni giorno sia su base evento in corrispondenza di una modifica o inserimento di un ordine con un qualsiasi Pdl presente nel campo input. Se non ci sono ordini che soddisfano le condizioni, la varabile non viene calcolata.

E' calcolato esclusivamente per asset di tipo conduttori (terzo livello) come il massimo tra i valori di HI conduttore (quarto livello) sotto al conduttori terzo livello. Viene calcolato ogni giorno, dopo il ricalcolo degli HI sull'Health.

Non sono presenti logiche di esclusione sulla base degli stati dell'asset

Conteggia il numero di ordini associati ad una qualsiasi attività (lista PdL riportato nel campo input) associate all'asset in un qualsiasi stato configurabile da GUI riportato nel campo "Stato ordine associato a Pdl", in una finestra temporale indicata nel campo "Periodo di calcolo" (considerandolo come giorni nel passato). Il ricalcolo è innescato ogni volta che viene inserito o modificato un record sulla tabella WORKORDER (tabella che contiene i record degli OdL associati agli asset a sistema). La data dell'ordine considerata è la data modello.

Calcola la differenza tra il valore che la variabile assume alla data attuale e quello che ha assunto un numero di giorni prima indicato nel campo "periodo di calcolo" (considerandolo come giorni nel passato). Nel caso il sistema non disponesse del valore calcolato nel passato, verrà presa il valore della variabile alla data più prossima a quella richiesta partendo da quella data. Per esempio, se la formula richiede il calcolo della variabile calcolata il 30/11/2023 e la variabile non è stata calcolata inquella data ma il 14/11/2023 ed il 12/12/2023, sarà individuato come termine di raffronto la data al 12/12/2023. La variabile viene ricalcolata ogni giorno tramite trigger temporale.

Le condizioni di applicabilità della formula di calcolo prevedono che in input devono essere:

- inseriti una lista di PdL

- un'unica variabile/misura.

Al passaggio in stato "Eseguito" di ordini associati ad una qualsiasi delle attività presenti nel campo input, il sistema associa il valore dell'unica variabile in input alla variabile che si sta calcolando

Calcola il numero di opere interferenti (se passato in input la gravità con criticità elevata in caso contrario tutte) presenti sotto la campata aerea gerarchicamente superiore all'asset a cui fa riferimento la variabile e su cui viene calcolata. Viene ricalcolata a seguito dell'inserimento a sistema di una nuova opera interferente sotto la campata. Nel caso di modifica dei parametri che determinano la gravità dell'opera interferente non sarà effettuato un nuovo ricalcolo. In questo scenario solo l'inserimento di una nuova interferenza genera il ricalcolo per quell'asset.

Gestione reset della variabile

Nel caso in cui il campo RESET della tabella X4CATMISURE sia pari a True, il sistema deve effettuare, mediante automation script, il reset del valore della variabile

Trigger calcolo della variabile

Il calcolo del campo CALCOLO della tabella X4CATMISURE viene innescato automaticamente mediante script di automazione in base alla tipologia

anno

ogni giorno mediante escalation

mediante:

Aggiornamento periodico ogni giorno viene rivalutato il calcolo

Aggiornamento ad evento in caso di inserimento/update sulla tabella WORKORDER di un’attività tra quelle nella lista della attività in input

mediante:

Aggiornamento periodico ogni giorno viene rivalutato il calcolo

Aggiornamento ad evento in caso di inserimento/update sulla tabella WORKORDER di un’attività tra quelle nella lista della attività in input

…

#### Catalogo micro-controlli

Visualizzazione storico

Viene abilitato l’audit su tutti i campi del catalogo microcontrolli; la funzionalità “Visualizza storico” mostrerà una vista contenente le seguenti colonne per ciascuna modifica: Modificato da (CHANGEBY) e Data (CHANGEDATE). Inoltre, espandendo la singola riga sarà possibile visualizzare tutti i dati del catalogo microcontrolli relativi alla CHANGEDATE selezionata. La tabella sarà scaricabile in formato Excel in qualsiasi momento.

Valorizzazione campi

La valorizzazione dei campi che necessitano di una particolare customizzazione del catalogo microcontrolli è definita come segue:

Assieme, manuale da lista valori prefiltrata in base al valore presente nel campo TIPOLOGIAASSET

Sottoassieme, manuale da lista valori prefiltrata in base al valore presente nel campo TIPOLOGIAASSET e quello presente nel campo ASSIEME

ID difformità, manuale da lista valori prefiltrata in base al valore presente nel campo TIPOLOGIAASSET

ID interferenza, manuale da lista valori prefiltrata in base al valore presente nel campo TIPOLOGIAASSET

#### Catalogo indicatori di condizione tecnica

Gli indicatori di condizione tecnica – divisi fra tassi di guasto/anomalia e Health Index – ed il loro catalogo sono censiti all’interno dell’applicativo Health del MAS. Per il calcolo delle loro componenti vengono utilizzate le formule censibili all’interno dell’applicazione.

##### Tassi di guasto e tassi di anomalia

Per la realizzazione dei tassi di guasto vengono create a sistema le seguenti voci:

Score group, score type e contributors: uno per ogni tipologia di elemento su cui applicare il tasso di guasto/anomalia (es. Uno score type, un contributor e uno score group denominata “TdG Conduttore”).

Parallelamente, vengono anche create altrettante “Locations” EAM, una per tipologia di elemento a cui applicare il TdG o TdA. Gli score group vengono definiti a livello di location e quindi applicati a questi elementi.

La formula dei TdG viene definita come di seguito:

TdG = COUNT$X4GUASTICONDUTTORI(2000)/COUNT$X4ASSETCONDUTTORI * 100

Oppure

TdG = COUNT$X4GUASTICONDUTTORI(2000)/SUM$X4ASSETLUNCONDUTTORI * 100

La logica definita all’interno di questa formula viene replicata per tutte le tipologie di asset a cui applicare il TdG/TdA. Nel dettaglio la prima count estrarra la sommatoria di tutte le difformità aperte sugli asset di tipo conduttore con tipologia di difformità eguale a “Guasto”; la seconda count estrarrà invece la sommatoria di tutti gli asset con classificazione di tipo “CONDUTTORE” con data di installazione (INSTALLDATE) maggiore del 2000, oppure nel secondo caso andrà a sommare la lunghezza delle “CAMPATE” (asset ancestor) degli asset specificati con classificazione di tipo “CONDUTTORE”.

Il tasso di anomalia viene costruito in modo speculare:

TdA = COUNT$X4ANOMALIECONDUTTORI(2000)/COUNT$X4ASSETCONDUTTORI * 100

La differenza registrata in questo caso avviene nella formula del numeratore, dove piuttosto che la tipologia di difformità essere posta a “Guasto”, sarà posto eguale ad “Anomalia”.

Le formule X4GUASTICONDUTTORI e X4ANOMALIECONDUTTORI evocano un automation script che effettua il calcolo delle difformità con tipologia “GUASTO” o “ANOMALIA” aperte a partire dall’anno espresso nel parametro passato all’interno delle parantesi (dataprimarilevazione > TO_DATE(’01-01-2000’,’DD-MM-YYYY’).

##### Health Index

Gli HI a sistema vengono censiti sulla tipologia di asset su cui applicarli (ad es. verrà creato un HI applicabile ai conduttori, uno alle funi di guardia); la struttura della formula applicata ai singoli HI segue lalogica

…

Anagrafica opere interferenti

Le opere interferenti verranno gestite in anagrafica come elementi di 4° livello sottostanti a un 3° livello aggregatore OPERE INTERFERENTI, a sua volta figlio del 2° livello CAMPATA (linee aeree) o del 2° livello TRATTA CAVO (linee in cavo).

Configurazioni

Per la gestione delle segnalazioni in campo degli operatori, è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| WORKLOG | DESCRIPTION_LONGDESCRIPTION | Nota segnalazione | LONGALN | 32000 |
| WORKLOG | X4GESTITO | Stato della segnalazione | YORN |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

Per permettere la gestione delle opere interferenti, si prevedono le seguenti configurazioni a sistema:

Applicazione “Classificazioni Terna”

L’applicazione “Classificazioni Terna” (X3ASSETCAT, clone dell’applicazione standard di gestione delle classificazioni e delle specifiche tecniche degli asset ASSETCAT) viene estesa per permettere la gestione delle seguenti classificazioni:

Opere interferenti, il cui percorso gerarchico di classificazione è LINEA \ CAMPATA AEREA \ OPERE INTERFERENTI

Opere interferenti, il cui percorso gerarchico di classificazione è LINEA \ TRATTA CAVO \ OPERE INTERFERENTI

Opera interferente, il cui percorso gerarchico di classificazione è LINEA \ CAMPATA AEREA \ OPERE INTERFERENTI \ OPERA INTERFERENTE

Opera interferente, il cui percorso gerarchico di classificazione è LINEA \ TRATTA CAVO \ OPERE INTERFERENTI \ OPERA INTERFERENTE.

Inoltre, si prevede l’aggiunta degli attributi relativi all’asset di 4° livello OPERA INTERFERENTE, i quali vengono descritti nel paragrafo 3.3.2.

Creazione opere interferenti

L’applicazione custom di gestione dell’anagrafica delle linee X3LINEE, clone dell’applicazione standard ASSET, viene estesa in modo da permettere la creazione delle opere interferenti:

Funzionalità “Crea ”, attivabile solamente da un secondo livello CAMPATA o TRATTA CAVO, permette la creazione di un aggregatore di terzo livello OPERE INTERFERENTI

Funzionalità “Crea ”, attivabile solamente da un terzo livello aggregatore, permette la creazione di un elemento di quarto livello OPERA INTERFERENTE.

Gestione e visualizzazione opere interferenti

Le logiche di gestione e visualizzazione delle opere interferenti sono le seguenti:

Visualizzazione, tutti gli utenti, a prescindere dal gruppo di protezione associato, possono visualizzare l’anagrafica delle opere interferenti

Gestione, gli utenti appartenenti al gruppo di protezione “trn-eam-rtn-cntr-1-xxx” (con xxx l’ambiente di MAS target, ad es. Dev) possono inserire e modificare opere interferenti su tutti gli asset in anagrafica; mentre, quelli appartenenti a “trn-eam-rtn-ui-xxx” possono farlo solo se l’asset è di propria competenza.

Segnalazioni dal campo

In Maximo sarà possibile la visualizzazione nel centro di controllo del responsabile asset di competenza di una vista riepilogativa delle segnalazioni dal campo registrati sugli asset. Il result set “Segnalazioni dal campo opere interferenti” mostrerà le seguenti colonne: ID OdL (WONUM), Nota dell’operatore in merito alla segnalazione (WORKLOG. DESCRIPTION_LONGDESCRIPTION), flag GESTITO (WORKLOG.X4GESTITO). Gli ordini di lavoro mostrati saranno quelli con OWNERGROUP eguale all’UDB di appartenenza dell’utente che ha effettuato il login.

Sviluppi

Per permettere la gestione delle opere interferenti, si prevedono i seguenti sviluppi a sistema:

Attributi di classificazione dell’elemento di quarto livello “OPERA INTERFERENTE”

Gli attributi di classificazione dell’asset di 4° livello OPERA INTERFERENTE e le loro logiche di popolamento sono le seguenti:

ID intersezione opera (LIN_ID_OPERA), caratteristica ALN valorizzata automaticamente dal sistema con la concatenazione del codice (), Tipologia (LIN_TIPO_INT), Progressivo numerico a 6 cifre tramite automation script

Tipologia (LIN_TIPO_INT), caratterista ALN tramite lista valori (X4TIPOINT)

Sotto-tipologia (LIN_SOTTOTIPO_INT), caratterista ALN ad inserimento manuale tramite lista valori (X4SOTTOTIPOINT)

Criticità (LIN_CRITICITA_LIV4_INT), caratteristica ALN valorizzata automaticamente dal sistema con “critico” o “non critico” in base

Denominazione (LIN_DENOMINAZIONE_INT), caratteristica ALN ad inserimento manuale libero

Ente gestore (LIN_ENTE_GESTORE_INT), caratteristica ALN ad inserimento manuale libero

Data rilievo (LIN_DATA_RILIEVO_ INT), caratteristica DATE ad inserimento manuale libero

Progressivo da estremo di rif (LIN_PROG_ESTREMO_RIF_INT), caratteristica NUM ad inserimento manuale libero

Rif. estremo progressivo (LIN_RIF_ESTREMO_PROG_INT), caratteristica ALN ad inserimento manuale tramite lista valori (X4ESTREMOPROG); quest’ultima permetterà la selezione del SAP ID (ASSETLOCRELATION.X3SAPID) dei sostegni (o estremi cavo) associati alla campata (o tratta cavo) sotto la quale è rilevata l’opera interferente

Distanza orizzontale (LIN_DISTANZA_ORIZZONTALE_INT), caratteristica NUM ad inserimento manuale libero; nel caso di attivazione della funzionalità “Inverti linea”, il sistema, tramite script di automazione, moltiplicherà il valore presente nel campo per -1

SAP-ID linea interferente (LIN_SAPID_LINEA_INT), caratteristica ad inserimento manuale tramite lista valori (X4SAPIDINT); quest’ultima permetterà la selezione del SAP ID (ASSET.X3SAPID) delle linee che hanno come UI la stessa della campata su cui insiste l’opera interferente. Inoltre, l’attributo di classificazione LIN_SAPID_LINEA_INT risulta valorizzabile e obbligatorio solo se LIN_ENTE_GESTORE_INT = ‘Terna’.

Attributi di classificazione dell’elemento di terzo livello “OPERE INTERFERENTI”

L’attributo di classificazione dell’asset di 3° livello OPERE INTERFERENTI e la sua logica di popolamento è la seguente:

Criticità (LIN_CRITICITA_LIV3_INT), caratteristica ALN valorizzata automaticamente dal sistema con “critico” o “non critico” in base all’attributo LIN_CRITICITA_LIV4_INT dell’elemento del livello inferiore (se almeno un elemento di 4° livello è critico allora anche quello di 3° livello sarà critico).
