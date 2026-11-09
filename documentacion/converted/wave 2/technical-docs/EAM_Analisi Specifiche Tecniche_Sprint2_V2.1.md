# EAM_Analisi Specifiche Tecniche_Sprint2_V2.1

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Specifica Tecnica – Sprint 2 |

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
| Job Task (JT) | Metodi/micro-controlli associati a un PdL |
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
| Dominio Crossover | Un dominio di crossover in IBM Maximo è un meccanismo che consente di specificare condizioni per il trasferimento di valori tra campi di oggetti correlati. Quando si crea un dominio di crossover, è possibile definire regole che determinano quando i valori di un campo di origine verranno copiati in un campo di destinazione |

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche, già descritte ad alto livello nel documento “EAM_Analisi , verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software.

Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni implementate sulla piattaforma EAM di Terna (ambiente Dev) e gli sviluppi custom da implementare.

Nel dettaglio, ciascun paragrafo (dedicato a una specifica funzionalità nell’applicativo) presenta due sotto-paragrafi: nel primo sono illustrate le configurazioni eseguite sul software (e.g., estensione del modello dati, configurazione dell’applicazione, gestione della condizionalità, etc.), nel secondo gli sviluppi necessari per soddisfare le funzionalità richieste durante lo sprint.

# Sprint 2: Pianificazione economica e preparazione dati per consuntivazione

Nel modello Terna, la pianificazione economica è il processo con cui si definisce la valorizzazione economica delle risorse interne ed esterne allocate per lo svolgimento delle attività.

Nello specifico, i temi inclusi nello sprint sono i seguenti:

Caratterizzazione OdL con informazioni CoIn

Recupero/alimentazione da fonti esterne (SAP)

Verifica e gestione capacity

Pianificazione costi risorse interne ed esterne

Gestione OdL serbatoio

Gestione tariffe

Cataloghi

Reportistica su pianificazione economica.

## Caratterizzazione OdL con informazioni CoIn

Le Informazioni di Contabilità Industriale sono rappresentate all’interno dell’applicazione Monitoraggio degli OdL nel tab “CoIn”, dove i dati sono organizzati in base al tipo di spesa.

### Configurazioni

Per la gestione dei dati inerenti alla contabilità industriale è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito viene riportato in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| WORKORDER | X4TIPOSPESA | Tipo spesa | ALN | 5 |
| WORKORDER | X4COIN | Codice Coin | ALN | 15 |
| WORKORDER | X4NTK | Identificativo NTK | ALN | 20 |
| WORKORDER | X4TIPOOI | Tipo ordine Interno | ALN | 15 |
| WORKORDER | X4NORDINEINTERNO | Ordine Interno | ALN | 20 |
| WORKORDER | X4TIPOATTIVITA | Tipo attività | ALN | 20 |
| WORKORDER | X4TIPOORDINE | Tipo ordine | ALN | 20 |
| WORKORDER | X4WBS | WBS Contabile | ALN | 20 |
| WORKORDER | X4WBSPIAN | WBS Pianificata | ALN | 20 |
| WORKORDER | X4WBE | WBE Contabile | ALN | 20 |
| WORKORDER | X4WBEPIAN | WBE Pianificata | ALN | 20 |
| WORKORDER | X4ODM | Identificativo OdM | ALN | 20 |
| WORKORDER | X3NOPERAZIONE | Numero Operazione Network | ALN | 20 |
| X4CATMOS | IDATTSTANDARD | ID attività standard | ALN | 15 |
| X4CATMOS | SPESAPREVALENTE | Tipo spesa prevalente | ALN | 15 |
| X4CATMOS | COINPREVALENTE | Coin Prevalente | ALN | 15 |
| X4CATMOS | X4CATMOSID | ID univoco/chiave della tabella X4CATMOS | BIGINT |  |
| X4CATMOSCOIN | X4CATMOSID | ID univoco/chiave della tabella X4CATMOS | BIGINT |  |
| X4CATMOSCOIN | TIPOSPESA | Tipo spesa | ALN | 15 |
| X4CATMOSCOIN | COIN | Codice Coin | ALN | 15 |
| X4COIN | IDCOIN | Codice Coin | ALN | 15 |
| X4COIN | TIPOATTIVITA | Tipo Attività | ALN | 15 |
| X4COIN | TIPOORDINE | Tipo Ordine | ALN | 15 |
| X4COIN | TIPOSPESA | Tipo Spesa | ALN | 15 |
| X4WBE | IDWBE | ID CBE | ALN | 20 |
| X4WBE | IDWBS | ID CBS di appartenenza | ALN | 20 |
| X4WBE | DESCRIPTION | Descrizione CBE | ALN | 50 |
| X4WBE | PIANIFICAZIONE | Elemento di tipo pianificazione | YORN | 1 |
| X4WBE | STATOSISTEMASAP | Stato del sistema elemento in SAP | UPPER | 8 |
| X4WBE | STATOUTENTESAP | Stato utente elemento in SAP | UPPER | 8 |
| X4WBE | CDCRESP | Codice del centro di costo dell'elemento cbs responsabile dell'attività richiesta | UPPER | 15 |
| X4WBE | CDCRICH | Codice del centro di costo dell'elemento cbs richiedente dell'attività richiesta | UPPER | 15 |
| X4WBE | SOCRICH | Società dell'elemento cbs richiedente dell'attività richiesta | UPPER | 15 |
| X4WBE | CREATEDATE | Data di creazione della cbs | DATETIME |  |
| X4WBE | CHANGEDATE | Data dell'ultima modifica della cbs | DATETIME |  |
| X4WBE | TIPOPROG | Tipologia di progetto a cui appartiene la cbs | ALN | 15 |
| X4WBE | PROFITCENTER | Profit Center cbs | UPPER | 15 |
| X4WBE | SEDETECNICA | Sede tecnica a cui è collegata la cbs | UPPER | 30 |
| X4WBE | MOTINVESTIMENTO | Indica un motivo di investimento | ALN | 50 |
| X4WBE | CODINCENTIVAZIONE | Cluster | ALN | 50 |
| X4WBE | CODINTERVENTO | Codice intervento | ALN | 50 |
| X4WBE | TIPOSPESA | Tipo spesa (popolato per WBE di pianificazione) | ALN | 5 |
| X4NTK | IDNTK | ID Network | ALN | 20 |
| X4NTK | DESCRIPTION | Descrizione NTK | ALN | 50 |
| X4NTK | WBE | ID CBE | ALN | 20 |
| X4NTK | STATOSISTEMASAP | Stato del sistema elemento in SAP | UPPER | 8 |
| X4NTK | STATOUTENTESAP | Stato utente elemento in SAP | UPPER | 8 |
| X4NTK | PROGETTO | Progetto a cui è legata NTW | ALN | 20 |
| X4NTK | SOCIETA | Società | UPPER | 15 |
| X4NTK | PROFITCENTER | Profit Center | UPPER | 15 |
| X4NTK | TIPO | Tipo NTW | UPPER | 10 |
| X4NTK | CREATEDATE | Data creazione | DATETIME |  |
| X4NTK | CHANGEDDATE | Data ultima modifica | DATETIME |  |
| X4NTK | CHANGEDBY | Autore creazione/modifica | UPPER | 20 |
| X4OPNTK | IDNTK | ID Network | ALN | 20 |
| X4OPNTK | IDOPERAZIONE | ID Operazione | ALN | 5 |
| X4OPNTK | DESCRIPTION | Descrizione Operazione | ALN | 50 |
| X4OPNTK | STATO | Stato Operazione | UPPER | 8 |
| X4OPNTK | CDLRESP | CdL responsabile dell’operazione | UPPER | 15 |
| X4OPNTK | CREATEDATE | Data creazione | DATETIME |  |
| X4OPNTK | CHANGEDDATE | Data ultima modifica | DATETIME |  |
| X4OPNTK | CHANGEDBY | Autore creazione/modifica | UPPER | 20 |
| X4ODM | IDODM | ID OdM | ALN | 20 |
| X4ODM | DESCRIPTION | Descrizione ODM | ALN | 50 |
| X4ODM | TIPOATTIVITA | Tipo attività | ALN | 20 |
| X4ODM | TIPOORDINE | Tipo ordine | ALN | 20 |
| X4ODM | STATOSISTEMA | Stato del sistema elemento in SAP | UPPER | 8 |
| X4ODM | STATOUTENTE | Stato utente elemento in SAP | UPPER | 8 |
| X4ODM | IMPIANTO | Impianto/Sede Tecnica | ALN | 20 |
| X4ODM | SEDETECNICA | Sede Tecnica | UPPER | 30 |
| X4ODM | CDCRESP | Centro di costo responsabile | UPPER | 15 |
| X4ODM | CDCCOSTI | Centro di costo per scarico costi | UPPER | 15 |
| X4ODM | PROFITCENTER | Profit Center | UPPER | 15 |
| X4ODM | CREATEDBY | Autore creazione/modifica | UPPER | 20 |
| X4ODM | CREATEDATE | Data creazione ODM | DATETIME |  |
| X4ODM | CHANGEDDATE | Data ultima modifica OdM | DATETIME |  |
| X4ODM | CDLRESP | Centro di lavoro responsabile dell'attività | UPPER | 15 |
|  |  |  |  |  |
|  |  |  |  |  |
| X4ODMOP | IDODM | ID ODM | ALN | 20 |
| X4ODMOP | NOPERAZIONE | Numero operazione ODM | UPPER | 20 |
| X4ODMOP | DESCRIPTION | Descrizione operazione | ALN | 50 |
| X4ORDINT | IDOI | ID Ordine Interno | ALN | 20 |
| X4ORDINT | DESCRIPTION | Descrizione | ALN | 50 |
| X4ORDINT | STATOSISTEMA | Stato del sistema elemento in SAP | UPPER | 8 |
| X4ORDINT | STATOUTENTE | Stato utente elemento in SAP | UPPER | 8 |
| X4ORDINT | TIPOOI | Tipo OI | UPPER | 5 |
| X4ORDINT | CATORD | Categoria Ordine | UPPER | 5 |
| X4ORDINT | CREATEDDATE | Data creazione | DATETIME |  |
| X4ORDINT | CHANGEDDATE | Data modifica | DATETIME |  |
| X4ORDINT | SOCIETA | Società | UPPER | 15 |
| X4ORDINT | CDCRESP | Codice del centro di costo responsabile | UPPER | 15 |
| X4ORDINT | CODCLIENTE | Codice cliente | UPPER | 15 |
| X4ORDINT | CDCCOSTI | Centro di costo per scarico costi | UPPER | 15 |
| X4ORDINT | PROFITCENTER | Profit Center | UPPER | 15 |
| X4ORDINT | CLASSEOGGETTO | Classe Oggetto | UPPER | 15 |
| LABTRANS | SITEID | Identificativo del sito | UPPER | 8 |
| LABTRANS | REFWO | Ordine di lavoro associato | UPPER | 10 |
| LABTRANS | GENAPPRSERVRECEIPT | Inviato a SAP | YORN |  |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

X4CATMOS: Catalogo delle attività standard; al suo interno viene definito l’intero set di informazioni quali codici CoIn e Tipi spesa ammissibili per ogni attività standard. Inoltre, viene anche definito il tipo spesa ed il rispettivo codice CoIn prevalente dell’attività standard (informazione utilizzata per il primo popolamento dei campi “Tipo spesa” e “Codice CoIn” dell’ordine di lavoro.

X4CATMOSCOIN: Tabella custom di Maximo; al suo interno vengono definiti i campi per la gestione delle Coin multi-valore del catalogo delle attività standard (X4CATMOS).

X4COIN: Catalogo dei codici CoIn correlato dalle informazioni di tipo attività PM e tipo ordine per ogni rispettivo codice CoIn. Utilizzato per collegare il tipo attività PM e il tipo ordine ad un codice CoIn.

X4WBE: Anagrafica delle WBE e WBS. Nel caso di WBS il campo IDWBS sarà vuoto. Le righe presenti all’interno della tabella saranno quelle mostrate all’utente nella fase di selezione WBS/WBE.

X4ORDINT: Anagrafica degli ordini interni. Le righe presenti all’interno della tabella saranno quelle mostrate all’utente nella fase di selezione OI.

X4ODM Anagrafica degli ordini di manutenzione. Le righe presenti all’interno della tabella saranno quelle mostrate all’utente nella fase di selezione ODM.

X4ODMOP: Tabella custom di Maximo che permette la gestione dei campi relativi alle operazioni ODM.

X4NTK: Anagrafica dei codici network. Le righe presenti all’interno della tabella saranno quelle mostrate all’utente nella fase di selezione NTK.

X4OPNTK: Tabella custom di Maximo che permette la gestione dei campi relativi alle operazioni NTK.

LABTRANS: Tabella standard di Maximo che permette la gestione e la registrazione dei consuntivi delle risorse.

Per permettere la visione dei campi necessari all’interno del tab “CoIn”, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “WOTRACK_D” come duplicato dell’applicazione standard “WOTRACK”, si dovranno configurare:

I campi di testata del tab “CoIn”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Tipo spesa | X4TIPOSPESA |  | VALUELIST |  |
| ID Attività Standard | JOBPLAN.X4ATTALL |  |  |  |

I campi della sezione “ANR”, caratterizzata dalla sigoption “X4ANR”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Codice Coin | X4COIN |  | X4COIN |  |
| Tipo attività PM | X4TIPOATTIVITA |  | X4TIPOATT |  |
| Tipo ordine OdM | X4TIPOORDINE |  | X4TIPOORD |  |
| Tipo ordine OI | X4TIPOOI |  | X4TIPOOI |  |
| CBS Contabile | X4WBS |  | X4WBS | X4EPC |
| CBS Pianificata | X4WBSPIAN |  |  | X3HIDE |
| CBE Contabile | X4WBE |  | X4WBE | X4EPC |
| CBE Pianificata | X4WBEPIAN |  |  | X3HIDE |
| Numero ordine Interno | X4NORDINEINTERNO |  | X4OI |  |
| Identificativo OdM | X4ODM |  | X4ODM |  |

La sigoption “X4EPC” rende visibili i campi CBS e CBE Contabile solo se l’attributo X4TIPOATTIVITA = EPC; La sigoption “X3HIDE” nasconde i campi ai quali è associata a tutti gli utenti.

I campi della sezione “CAPEX”, caratterizzata dalla sigoption “X4CAPEX”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Codice Coin | X4COIN |  | X4COIN |  |
| CBS Contabile | X4WBS |  | X4WBS |  |
| CBE Contabile | X4WBE |  | X4WBE |  |
| Tipo network | X4TIPOORDINE |  |  | X3HIDE |
| Identificativo NTK | X4NTK |  | X4NTK |  |
| Operazione | X4NOPERAZIONE |  |  | X4RO |

La sigoption “X4RO” rende il campo in sola lettura.

I campi della sezione “OPEX”, caratterizzata dalla sigoption “X4OPEX”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Codice Coin | X4COIN |  | X4COIN |  |
| Tipo attività PM | X4TIPOATTIVITA |  | X4TIPOATT |  |
| Tipo ordine OdM | X4TIPOORDINE |  | X4TIPOORD |  |
| Tipo ordine OI | X4TIPOOI |  |  | X3HIDE |

| CBS Pianificata | X4WBSPIAN |  |  | X3HIDE |
| --- | --- | --- | --- | --- |

| CBE Pianificata | X4WBEPIAN |  |  | X3HIDE |
| --- | --- | --- | --- | --- |
| Identificativo OdM | X4ODM |  | X4ODM | X4ODMOPEX |
| Numero ordine interno | X4NORDINEINTERNO |  | X4OI | X4OIOPEX |

La sigoption “X4ODMOPEX” nasconde il campo Identificativo OdM solo se X4TIPOORDINE = PMAC e X4TIPOATTIVITA = SFO.

La sigoption “X4OIOPEX” rende visibile il campo Numero ordine interno solo se X4TIPOORDINE = PMAC e X4TIPOATTIVITA = SFO.

Dominio “X4TIPOSPESA”

Creazione di un dominio associato a X4TIPOSPESA (WORKORDER), contenente al suo interno 3 voci OPEX, CAPEX, ANR. Sulle voci del dominio viene imposta una condizione che mostra i valori solo se per l'id attività standard esiste almeno una riga con tipospesa=:X4TIPOSPESA. Nel dettaglio la condizione applicata ai tre possibili valori del dominio prevede il seguente controllo per determinare se mostrare o meno il valore corrispondente: viene verificato che esista almeno una riga all’interno della tabella X4CATMOS dove IDATTSTANDARD è uguale al valore di X4ATTALL del piano di lavoro (valore JPNUM del workorder che viene valutato) impiegato sull’OdL (IDATTSTANDARD=:JOBPLAN.X4ATTALL) e che abbia come valore di TIPOSPESA il valore valutato nel dominio (TIPOSPESA rispettivamente uguale a OPEX, CAPEX o ANR). In caso contrario viene nascosta.

Dominio CROSSOVER “X4COINGEN”

Quando viene generato un OdL, qualunque sia la sua natura, al momento in cui viene assegnato un PdL, automaticamente avrà popolato il campo CoIn. Il popolamento automatico verrà effettuato mediante l'implementazione di un dominio crossover che andrà a popolare il campo X4COIN con l'attributo COINPREVALENTE della tabella X4CATMOS, dove IDATTSTANDARD è uguale al campo X4ATTALL del piano di lavoro usato. Nel dettaglio, a seguito della definizione del JPNUM su un record di WORKORDER, il dominio preleverà l’attributo COINPREVALENTE della tabella X4CATMOS della riga della tabella che soddisferà la seguente condizione: l’attributo IDATTSTANDARD sarà uguale all’attributo X4ATTALL del record della tabella JOBPLAN con JPNUM uguale al JPNUM del WORKORDER.

Visualizzazione campi economici

Creazione di 3 sigoption: X4CAPEX, X4OPEX, X4ANR. A queste sono associale tre condizioni omonime che verificano se il tipo spesa (X4TIPOSPESA) è uguale rispettivamente a CAPEX, OPEX o ANR.

Queste 3 sigoption mostrano condizionalmente 3 sezioni dell'applicazioni, in queste sezioni, vengono mostrati i campi economici da compilare per la caratterizzazione economica dell'intervento.

### Sviluppi

Per permettere la funzionalità di pre-valorizzazione dei dati economici, si prevede il seguente sviluppo a sistema:

Auto-compilazione dati economici

Sulla base del tipo spesa e sulla base dei dati inseriti da parte dell'utente, le voci economiche vengono popolate automaticamente da un automation script come segue:

CAPEX:

Codice Coin e Tipo Spesa: Gli attributi X4COIN e X4TIPOSPESA vengono popolati automaticamente – se possibile – non appena selezionato (o impostato automaticamente da parte del DSS) il PdL. Tali valori (rispettivamente COINPREVALENTE e SPESAPREVALENTE) verranno prelevati nel dettaglio dalla tabella X4CATMOS dove IDATTSTANDARD è uguale al campo X4ATTALL del piano di lavoro usato.

Tipo network: Valorizzato automaticamente sempre con PST2 (non visibile)

Se oggetto (NTK) esistente:

Il sistema propone una lista di NTK, le NTK della tabella X4NTK associate a CBE con profit center , sede tecnica di 1° livello compatibile con l'OdL (impianto della CBE e rispettiva CBS prelevati dalla tabella X4WBE) e centro di costo responsabile (CDCRESP di X4WBE) afferente al responsabile attività dell’OdL.

A seguito della selezione della NTK, il sistema popola automaticamente il campo “X4NOPERAZIONE” selezionato dalla tabella X4NTK data una NTK. Il sistema valorizza automaticamente la CBE sulla base della NTK selezionata sulla base del valore X4WBE della NTK selezionata dalla tabella X4NTK. Per il popolamento, invece, del campo X4WBS sarà necessario prelevare il valore dell’attributo IDWBS della tabella X4WBE dove l’attributo IDWBE sarà pari a quello definito nel campo CBE del codice network scelto.

Se oggetto (NTK) non esistente:

L'utente inserisce una CBE/CBS contabile selezionandola fra quelle proposte dal sistema (filtrate dalla tabella X4WBE per profit center , sede tecnica di 1 primo livello compatibile con l’OdL e centro di costo responsabile (CDCRESP) afferente al responsabile attività dell’OdL) e, in seguito, utilizza l’interfaccia di creazione per generare la NTK e la relativa Operazione associata sulla base della CBE.

Inoltre, in merito alle CBS multi-impianto (utili per gestire le campagne), quest’ultime non avranno il campo SEDETECNICA popolato e saranno visibili su tutti gli OdL.

OPEX:

Codice Coin e Tipo Spesa: Gli attributi X4COIN e X4TIPOSPESA vengono popolati automaticamente – se possibile – non appena selezionato (o impostato automaticamente da parte del DSS) il PdL. Tali valori verranno prelevati nel dettaglio dalla tabella X4CATMOS (rispettivamente COINPREVALENTE e SPESAPREVALENTE) dove IDATTSTANDARD è uguale al campo X4ATTALL del piano di lavoro usato.

Tipo attività PM: Valorizzato automaticamente dal sistema in base alla relazione tra Tipo attività PM e codice Coin della tabella X4COIN.

Tipo ordine OdM: Valorizzato automaticamente dal sistema in base alla relazione tra Tipo ordine OdM e codice Coin della tabella X4COIN.

CBS/CBE di pianificazione: Valorizzati automaticamente in base all’UI e a X4TIPOSPESA. Nel dettaglio verrà prelevata dalla tabella X4WBE il record con CDCRICH eguale al responsabile attività dell’OdL (OWNERGROUP), con il flag PIANIFICAZIONE eguale ad 1 e con tipo spesa pari ad OPEX.

Se oggetto (OdM) esistente:

L’utente seleziona manualmente l’OdM selezionandolo da una lista (filtrata per i record di X4ODM dove TIPOATTIVITA, TIPOORDINE, SEDETECNICA e CDCRESP coincidono con i rispettivi valori dell’OdL).

Se oggetto (OdM) non esistente:

L’utente utilizza l’interfaccia di creazione per generare l’OdM sulla base del Tipo attività PM e del Tipo ordine OdM.

ANR:

Codice CoIn e Tipo spesa: Gli attributi X4COIN e X4TIPOSPESA vengono popolati automaticamente – se possibile – non appena selezionato (o impostato automaticamente da parte del DSS) il PdL. Tali valori verranno prelevati nel dettaglio dalla tabella X4CATMOS (rispettivamente COINPREVALENTE e SPESAPREVALENTE) dove IDATTSTANDARD è uguale al campo X4ATTALL del piano di lavoro usato.

Tipo attività PM: Valorizzato automaticamente dal sistema in base alla relazione tra Tipo attività PM e codice Coin della tabella X4COIN.

CBS/CBE pianificazione: Valorizzati automaticamente in base all’UI e a X4TIPOSPESA. Nel dettaglio verrà prelevata dalla tabella X4WBE il record con CDCRICH eguale al responsabile attività dell’OdL (OWNERGROUP), con attributo COIN pari a quello dell’OdL (X4COIN), con il flag PIANIFICAZIONE eguale ad 1 e con tipo spesa pari ad ANR.

Se oggetto (OdM) esistente:

L’utente può selezionare manualmente l’OdM da una lista (filtrata per i record di X4ODM dove TIPOATTIVITA, TIPOORDINEe CDCRESP coincidono con i rispettivi valori dell’OdL). Dopo aver inserito l’OdM i campi restanti vengono valorizzati automaticamente, come descritto di seguito:

I campi Tipo ordine OdM, CBS/CBE contabile e Numero ordine interno vengono popolati automaticamente in base ai campi del record di OdM selezionato dalla tabella X4ODM.

Il campo X4TIPOOI dell’OdL viene popolato con il valore “T06” se X4TIPOORDINE=PMT9, con T09 se X4TIPOORDINE=PMTG.

Altrimenti, l’utente può selezionare manualmente il X4NORDINEINTERNO da una lista contente tutti gli ordini interni con CDCRESP di X4ORDINT afferente al responsabile attività dell’OdL. In base al numero ordine interno selezionato il sistema popola automaticamente X4TIPOOI con T06 o T09 (sulla base dell’attributo TIPOOI della tabella X4ORDINT). Successivamente, l’utente seleziona manualmente l’OdM selezionandolo da una lista (filtrata per i record X4ODM dove TIPOATTIVITA, TIPOORDINE, CDCRESP coincidono con i rispettivi valori dell’OdL). Dopo aver inserito l’OdM i campi restanti vengono valorizzati automaticamente, come descritto di seguito:

l campo X4TIPOORDINE dell’OdL viene popolato con il valore “PMT9” se X4TIPOOI=T06, con “PMTG” se X4TIPOOI=T09.

Se oggetto (OdM) non esistente:

Se X4TIPOATTIVITA = EPC:

L’utente seleziona una CBE/CBS contabile da una lista prefiltrata sulla base del CDCRESP di X4WBE afferente al responsabile attività, con PROFITCENTER che termina con EPCe con il flag “PIANIFICAZIONE” pari a 0. Quindi, X4NORDINEINTERNO viene valorizzato automaticamente in base al rapporto 1:1 con CBE contabile (sulla base del campo WBE della tabella X4ORDINT). Viene inoltre popolato anche il campo X4TIPOOI con la seguente logica: viene prelevato il tipo di ordine interno dalla tabella X4ORDINT (TIPOOI), dove l’attributo IDOI di questa tabella coincide con l’OI dell’ordine di lavoro.

Nel caso in cui l’utente selezioni prima X4NORDINEINTERNO (tramite una lista prefiltrata in base a PROFITCENTER che termina con EPCe CDCRESP di X4ORDINT afferente al responsabile attività dell’OdL), allora la selezione di X4WBE dovrà avvenire manualmente.

Una volta creato l’OdM viene popolato automaticamente il campo X4TIPOORDINE=PMT9 se X4TIPOOI dell’OI è pari a T06; PMTG se X4TIPOOI dell’OI è pari a T09.

Se X4TIPOATTIVITA ≠ EPC:

L’utente seleziona un Numero ordine interno da una lista (filtrata per Tipo ordine OI e CDCRESP afferente al responsabile attività). Una volta inserito il Numero ordine interno, il campo X4TIPOOI viene valorizzato con T09 o T06 sulla base del record di X4NORDINEINTERNO selezionato;

Una volta creato l’OdM viene popolato automaticamente il campo X4TIPOORDINE sulla base del tipo ordine associato all’OdM ricevuto/creato (i valori ammissibili sono PMT9 e PMTG).

## Recupero/alimentazione da fonti esterne (SAP)

Per permettere la corretta gestione delle informazioni gestite nel tab “CoIn” dell’ordine di lavoro, si deve prevedere l’implementazione di un’interfaccia di creazione NTK / OdM fra i sistemi EAM e SAP.

Nel caso in cui NTK / OdM non fossero già presenti in SAP, si richiede la generazione on-demand con allineamento sincrono fra i due sistemi.

### Configurazioni

Dati economici in sola lettura

Le informazioni economiche dell’OdL saranno impostate in sola lettura quando si richiede la creazione del codice NTK o del codice OdM.

Quando si richiede la creazione del codice NTK o del codice OdM, le informazioni economiche dell'OdL saranno impostate in sola lettura.

### Sviluppi

Per permettere la creazione del codice NTK o del codice OdM, è previsto il seguente sviluppo:

Pulsante di creazione NTK / OdM da SAP

Creazione di un pulsante che evoca il flusso di integrazione per la creazione del codice NTK o OdM. Questo sarà disponibile solo se tutte le informazioni obbligatorie dell'OdL sono state popolate. Le informazioni minime per richiedere la creazione variano a seconda del tipo di richiesta che viene effettuata:

Nel caso di OdM è necessario aver definito una data di inizio e fine pianificata dell’ordine di lavoro, il responsabile dell’attività (OWNERGROUP), la WBS (X4WBS), l’asset (ASSETNUM), il piano di lavoro (JPNUM), tipo attività PM (X4TIPOATTIVITA) e il tipo ordine (X4TIPOORDINE);

Nel caso di NTK necessario aver definito una data di inizio e fine pianificata dell’ordine di lavoro, il responsabile dell’attività (OWNERGROUP), la WBS (X4WBS), il piano di lavoro (JPNUM) ed un asset (ASSETNUM).

Nel caso in cui la richiesta di creazione non vada a buon fine viene generato un warning per informare l’utente circa il KO. Inoltre, quando il codice NTK o il codice OdM è diverso da Null, il pulsante non sarà più disponibile..

Per permettere la modifica del codice NTK o del codice OdM, è previsto il seguente sviluppo:

Pulsante di dissociazione NTK / OdM

Creazione di un pulsante che attivi la funzionalità “Disassocia NTK / OdM”, la quale permette la disassociazione dell’OdM / NTK con l’OdL (non comporta un annullamento dell’oggetto contabile in SAP) e contestualmente de-valorizza il campo “Identificativo NTK / OdM”. Questo pulsante che evoca la funzionalità di “disassociazione” sarà cliccabile dall’utente soltanto negli OdL dove le consuntivazioni ricevute per l’ordine non sono ancora state inviate a SAP. Tale controllo si trasforma dunque in una verifica che non esistano delle righe di LABTRANS - dove le chiavi di relazione REFWO e SITEID coincidono con l’OdL aperto dall’utente - che abbiano attributo GENAPPRSERVRECEIPT pari ad 1.

## Verifica e gestione capacity

La capacity è lo strumento impiegato per indicare le ore uomo effettivamente disponibili in una singola UdB; quest’ultima è un’informazione necessaria ai fini della quadratura dell’amministrato.

### Configurazioni

Per la gestione dei dati inerenti alla Capacity è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| PERSONGROUP | X4UDB | Flag per specificare se persongroup è una UDB | YORN |  |
| X4CAPACITY | X4CAPACITYID | ID univoco/chiave della tabella X4CAPACITY | ALN | 25 |
| X4CAPACITY | UDB | UdB | UPPER | 8 |
| X4CAPACITY | ANNO | Anno | INT |  |
| X4CAPACITY | SCENARIO | Scenario | ALN | 25 |
| X4CAPACITY | STATUS | Stato | ALN | 20 |
| X4CAPACITY | CHANGEDATE | Data modifica | DATETIME |  |
| X4CAPACITY | CHANGEDBY | Modificato da | ALN | 100 |
| X4CAPACITYDETAILS | X4CAPACITYID | ID capacity | ALN | 25 |
| X4CAPACITYDETAILS | X4CAPACITYDETAILSID | ID univoco/chiave della tabella X4CAPACITYDETAILS | ALN | 25 |
| X4CAPACITYDETAILS | MESE | Mese | ALN | 10 |
| X4CAPACITYDETAILS | ORE | Ore | ALN | 10 |
| X4CAPACITYDETAILS | CRAFT | Competenza (Operaio, Impiegato, Quadro) | UPPER | 8 |
| X4CAPACITYPM | X4CAPACITYID | ID capacity | ALN | 25 |
| X4CAPACITYPM | CAPACITYHTEO | Capacity teorica | INT |  |
| X4CAPACITYPM | HPROPRIETEO | Ore proprie teoriche | INT |  |
| X4CAPACITYPM | CEDUTOTEO | Ore cedute teoriche | INT |  |
| X4CAPACITYPM | RICEVUTOTEO | Ore ricevute teoriche | INT |  |
| X4CAPACITYPM | FLAVOROTEO | Ore forza lavoro teorica | INT |  |
| X4CAPACITYPM | HPROPRIEPLAN | Ore proprie pianificate puntuali | INT |  |
| X4CAPACITYPM | CEDUTOPLAN | Ore cedute pianificate | INT |  |
| X4CAPACITYPM | SATURAZIONEPLAN | Percentuale saturazione pianificata | INT |  |
| X4CAPACITYPM | RICEVUTOPLAN | Ore ricevute pianificate | INT |  |
| X4CAPACITYPM | CONTOBASEPLAN | Ore conto base pianificato | ALN | 15 |
| X4CAPACITYPM | FLAVOROPLAN | Ore forza lavoro pianificata | INT |  |
| X4CAPACITYPM | DELTACEDUTO | Ore cedute (da pianificare) | ALN | 15 |
| X4CAPACITYPM | DELTARICEVUTO | Ore ricevute (da pianificare) | ALN | 15 |
| X4CAPACITYPM | MESE | Mese | ALN | 10 |
| X4CAPACITYPM | CRAFT | Competenza (Operaio, Impiegato, Quadro) | UPPER | 8 |
| X4CAPACITYPM | SERBATOIO | Ore proprie pianificate serbatoio | INT |  |
| X4CAPACITYEXC | X4CAPACITYID | ID capacity | ALN | 25 |
| X4CAPACITYEXC | CRAFT | Competenza (Operaio, Impiegato, Quadro) | UPPER | 8 |
| X4CAPACITYEXC | QTYINV | Quantità proposta | INT |  |
| X4CAPACITYEXC | QTYACC | Quantità accettata | INT |  |
| X4CAPACITYEXC | MESE | Mese | ALN | 10 |
| X4CAPACITYEXC | DESTINATARIO | Destinatario | ALN | 10 |
| X4CAPACITYEXC | STATUS | Stato | ALN | 15 |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

PERSONGROUP: Tabella standard di Maximo; al suo interno sono definiti i campi necessari alla gestione dei gruppi di persone.

X4CAPACITY: Tabella custom di Maximo; al suo interno sono definiti i campi di testata dell’applicazione di gestione capacity.

X4CAPACITYDETAILS: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per l’inserimento della capacity nel tab “Capacity iniziale”.

X4CAPACITYPM: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per il monitoraggio della capacity nel tab “Monitoraggio capacity”.

X4CAPACITYEXC: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione dello scambio della capacity nel tab “Scambio ore”.

Al fine di poter guidare l’utente nel popolamento del campo “UdB” della tabella X4CAPACITY nell’applicazione dedicata, è stato configurato un dominio che permetterà di seleziona un record della tabella PERSONGROUP (già utilizzata per contenere al proprio interno tutti i DT e UI) dove il flag “X4UDB” è stato impostato a “True”.

Per permettere la visione dei campi necessari all’inserimento della Capacity, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione Custom “X4CAPACITY”, si dovranno configurare:

I campi di testata del tab “Capacity iniziale”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Titris | UDB |  |  | X4RO |
| Ultima modifica | CHANGEDATE |  |  | X4RO |
| Modificato da | CHANGEDBY |  |  | X4RO |
| Anno | ANNO |  |  | X4RO |
| Scenario | SCENARIO |  |  | X4RO |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

La sigoption “X4RO” rende i campi in sola lettura.

I campi della sezione “Capacity”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Mese | MESE |  |  |  |
| Competenza | CRAFT |  |  |  |
| Ore | ORE |  |  |  |

Per permettere la visione dei campi necessari allo scambio ore tra UdB, si dovrà prevedere la seguente configurazione a sistema per il tab “Scambio ore”:

I campi della sezione “Ore in uscita”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Natura | CRAFT |  | CRAFT |  |
| Qtà proposta | QTYINV |  |  |  |
| Qtà accettata | QTYACC |  |  |  |
| Mese | MESE |  |  |  |
| Resp. Attività destinatario | DESTINATARIO |  | UDB |  |
| Stato | STATUS |  |  |  |

I campi della sezione “Ore in entrata”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Natura | CRAFT |  |  |  |
| Qtà proposta | QTYINV |  |  |  |
| Qtà accettata | QTYACC |  |  |  |
| Mese | MESE |  | DATELOOKUP |  |
| Titris mittente | X4CAPACITY.UDB |  |  |  |
| Stato | STATUS |  |  |  |

Per permettere la visione dei campi necessari alla verifica della saturazione capacity, si dovrà prevedere la seguente configurazione a sistema (suddivisa per qualifica):

I campi del tab “Monitoraggio capacity” della sezione “Teorico”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Mese | MESE |  |  |  |
| Capacity teorica (H) | CAPACITYHTEO |  |  |  |
| Proprio teorico (H) | HPROPRIETEO |  |  |  |
| Ceduto teorico (H) | CEDUTOTEO |  |  |  |
| Ricevuto teorico (H) | RICEVUTOTEO |  |  |  |
| Forza lavoro teorica (H) | FLAVOROTEO |  |  |  |

I campi della sezione “Pianificato”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Proprio pianificato puntuale (H) | HPROPRIEPLAN |  |  |  |
| Ceduto pianificato (H) | CEDUTOPLAN |  |  |  |
| Saturazione pianificata (%) | SATURAZIONEPLAN |  |  |  |
| Conto base pianificato (H) | CONTOBASEPLAN |  |  |  |
| Ricevuto pianificato (H) | RICEVUTOPLAN |  |  |  |
| Forza lavoro pianificata (H) | FLAVOROPLAN |  |  |  |
| Proprio pianificato serbatoio (H) | SERBATOIO |  |  |  |

I campi della sezione “Check”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Ceduto teorico (H) | CEDUTOTEO |  |  |  |

| Ceduto pianificato (H) | CEDUTOPLAN |  |  |  |
| --- | --- | --- | --- | --- |

| Ceduto (da pianificare, H) | DELTACEDUTO |  |  |  |
| --- | --- | --- | --- | --- |
| Ricevuto teorico (H) | RICEVUTOTEO |  |  |  |
| Ricevuto pianificato (H) | RICEVUTOPLAN |  |  |  |
| Ricevuto (da pianificare, H) | DELTARICEVUTO |  |  |  |

Gestione del mittente dello scambio Quando il Titris crea una nuova riga per offrire una nuova quantità definita di ore di una specifica competenza, al momento in cui viene generata la nuova riga, non sarà necessario definire l’UdB “mittente” in quanto verrà automaticamente definito sulla base del record di capacity aperto dall’utente. Nel dettaglio quando viene effettuata la creazione della nuova riga tramite UI, un campo invisibile a schermo (il campo X4CAPACITYID) viene popolato automaticamente di default con il valore X4CAPACITYID del record aperto dall’utente. Il popolamento automatico viene effettuato mediante l’inserimento di un nodo di “Default Value” inserito nell’xml dell’applicazione che dal MAINRECORD (il record di X4CAPACITY) preleva l’attributo X4CAPACITYID e lo imposta nel campo X4CAPACITYID della tabella X4CAPACITYEXC.

Gestione delle capacity in base allo stato

Gli utenti appartenenti al gruppo “superuser” possono modificare lo stato dei record della capacity. Se quest’ultimo viene impostato in “MODIFICABILE DA UDB”, allora gli utenti responsabili UdB potranno modificare il record. Al contrario, se lo stato è “NON MOD. DA UDB”, allora l’operatività sui record sarà bloccata.

Tale configurazione si ottiene applicando una restrizione dei dati all'oggetto X4CAPACITY agli utenti che non fanno parte del gruppo dei superuser.

Centro di controllo capacity

Per gli utenti UdB, il centro di controllo deve essere configurato in modo tale da permettere la visualizzazione di un portlet dedicato alle proposte di scambio capacity. Nel dettaglio è stato creato un nuovo centro di controllo contenente al proprio interno un portlet di tipo “Result Set”. Il result set è basato sull’applicazione creata X4CAPACITY per la gestione della capacity. Al centro di controllo viene associata una query definita nell’applicazione che preleverà, sulla base dell’utente che ha effettuato il login, le righe di X4CAPACITY le cui UdB sono segnalate come “DESTINATARIO” della tabella X4CAPACITYEXC e lo stato della richiesta è ancora in “PROPOSTA”.

Per gli utenti centrali (AM)e i responsabili UdB, il centro di controllo deve essere configurato in modo tale da permettere la visualizzazione di un portlet dedicato alle sovrasaturazioni delle varie UdB. Nel dettaglio è stato creato un nuovo centro di controllo contenente al proprio interno un portlet di tipo “Result Set”. Il result set è basato sull’applicazione creata X4CAPACITY per la gestione della capacity. Al centro di controllo viene associata una query definita nell’applicazione che preleverà le righe di X4CAPACITY per cui esiste almeno una riga di X4CAPACITYDETAILS dove il valore di SATURAZIONEPLAN è maggiore del 100%.

### Sviluppi

Per permettere la funzionalità di inserimento della capacity, si prevede il seguente sviluppo a sistema:

Pulsante “Import” da Tagetik

La richiesta della capacity deve avvenire da opportuna dialog in cui viene specificato lo scenario Tagetike le UdB per cui importare le informazioni relative alle capacity. Tale funzionalità è disponibile solo per gli utenti centrali. A seguito dell’import, il campo “Modificato da” (CHANGEDBY della tabella X4CAPACITY) sarà popolato con il valore “Tagetik”.

Modifica manuale capacity iniziale

Nel caso di modifica manuale della capacity iniziale, tramite automation script il campo “Modificato da” (CHANGEDBY della tabella X4CAPACITY) sarà popolato con la matricola dell’utente e al campo “SCENARIO” della tabella X4CAPACITY sarà associato il valore “NULL”.

Per permettere la gestione dello scambio ore tra UdB, si prevedono i seguenti sviluppi a sistema:

Valorizzazione di default quantità accettata e stato

Il campo QTYACC viene popolato, di default, con lo stesso valore posto in QTYINV quando viene creata la nuova riga nella tabella X4CAPACITYEXC; mentre, il campo STATUS viene popolato automaticamente con PROPOSTA.

Conferma/rifiuto offerta scambio capacity Lo stato della richiesta varierà a seguito dell’operatività dell’utente dell’UdB destinataria. Nel dettaglio, fin tanto che il destinatario non opererà sulla quantità proposta dall’UdB mittente, la richiesta rimarrà in stato “PROPOSTA”. Non appena l’UdB destinataria della proposta accetterà lo scambio – variandone lo stato della richiesta di scambio -, lo stato varierà come segue: lo stato potrà essere posto pari ad “ACCETTATA” se la QTYACC è maggiore di 0 ma comunque minore o uguale dell’attributo QTYINV; lo stato potrà essere posto in “RIFIUTATA” e automaticamente il campo QTYACC sarà posto uguale a 0. Successivamente, in base alla scelta del destinatario, si aggiornerà automaticamente il campo status del mittente.

Calcoli automatici scambio capacity

Al popolamento della tabella X4CAPACITYEXC devono essere modificate opportunamente i record della tabella X4CAPACITYPM con uno script. Nel dettaglio devono essere aggiornate le CEDUTOTEO e RICEVUTOTEO.

Per permettere il monitoraggio della capacity e la verifica della sua saturazione, si prevedono i seguenti sviluppi a sistema:

Calcoli automatici monitoraggio capacity

Gli attributi della tabella contenuti nel monitoraggio capacity devono essere calcolati dinamicamente tramite automation script:

CAPACITYHTEO: valore definito nel tab di inserimento della capacity

HPROPRIETEO: valore calcolato come CAPACITYHTEO – CEDUTOTEO

CEDUTOTEO: valore calcolato come la somma di tutte le ore di QTYACC della tabella X4CAPACITYEXC dove il mese della stessa tabella è pari al mese specificato nella riga della tabella X4CAPACITYDETAILS relazionata alla X4CAPACITYPM di cui si sta valutando il ceduto teorico; inoltre, la X4CAPACITYID dei record di X4CAPACITYEXC deve essere uguale alle X4CAPACITYID dei record di X4CAPACITY della propria UdB.

RICEVUTOTEO: valore calcolato prelevando le righe di X4CAPACITYEXC dove il mese della tabella X4CAPACITYEXC è uguale a quello che si sta valutando nella X4CAPACITYPM, ma l'UdB non sarà pari all'UdB specificata nel record di X4CAPACITY che ha generato lo scambio, bensì uguale al campo "DESTINATARIO".

FLAVOROTEO: valore calcolato come HPROPRIETEO + RICEVUTOTEO

HPROPRIEPLAN: valore calcolato come somma delle ore assorbite negli OdL (in stato APERTO, SCHEDULABILE, SCHEDULATO, INOLTRATO, AVVIATO, ESEGUITO PAR, ESEGUITO e CHIUSO) che insistono sull'UdB; non può essere maggiore di HPROPRIETEO. Nel caso gli OdL siano a cavallo tra più mesi, l’effort orario viene distribuito coerentemente nei mesi.

CEDUTOPLAN: valore calcolato come somma delle ore assorbite negli OdL che insistono sull'UdB da altre UdB; non può essere maggiore di CEDUTOTEO

SATURAZIONEPLAN: valore calcolato come (HPROPRIEPLAN + CEDUTOPLAN) / CAPACITYHTEO * 100

CONTOBASEPLAN: valore calcolato come CAPACITYHTEO – HPROPRIEPLAN – CEDUTOPLAN – SERBATOIO

RICEVUTOPLAN: valore calcolato come somma delle ore assorbite negli OdL dell'UdB ricevute da altre UdB; non può essere maggiore di CEDUTOTEO dell'UdB Cedente

FLAVOROPLAN: valore calcolato come HPROPRIEPLAN + RICEVUTOPLAN

DELTACEDUTO: valore calcolato come CEDUTOTEO – CEDUTOPLAN

DELTARICEVUTO: valore calcolato come RICEVUTOTEO – RICEVUTOPLAN.

Controllo di coerenza sul campo “Proprio pianificato serbatoio”

In fase di compilazione del campo SERBATOIO di X4CAPACITYPM, il sistema mostrerà un messaggio di errore all’utente se il valore inserito rende il campo CONTOBASEPLAN minore di zero (a seguito dell’operazione: CAPACITYHTEO – HPROPRIEPLAN – CEDUTOPLAN – SERBATOIO).

## Pianificazione costi risorse interne ed esterne

Le Informazioni inerenti alla pianificazione dei costi delle risorse interne ed esterne sono rappresentate all’interno dell’applicazione Monitoraggio degli OdL nel tab “Piani”.

### Configurazioni

Per la gestione dei dati inerenti alla pianificazione dei costi delle risorse esterne e interne, è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| WPLABOR | CRAFT | Qualifica | UPPER | 8 |
| WPLABOR | QUANTITY | Quantità | INT |  |
| WPLABOR | RATE | Tariffa oraria | AMOUNT |  |
| WPLABOR | X4TITRIS | Titris della risorsa | UPPER | 8 |
| WPLABOR | X4DATAINIZIORISORSA | Data di inizio | DATETIME |  |
| WPLABOR | X4DATAFINERISORSA | Data di fine | DATETIME |  |
| WPLABOR | X4OREESE | Ore esecuzione | DURATION |  |
| WPLABOR | X4ORETRASF | Ore trasferimento | DURATION |  |
| ITEM | ITEMNUM | Resource ID | UPPER | 30 |
| ITEM | DESCRIPTION | Descrizione | ALN | 40 |
| ITEM | COMMODITYGROUP | Gruppo Merce | UPPER | 8 |
| ITEM | X4VDC | VdC | UPPER | 20 |
| ITEM | X4TRISORSA | Tipo Risorsa | ALN | 10 |
| ITEM | ITEMSETID | ID Item set | UPPER | 8 |
|  |  |  |  |  |
|  |  |  |  |  |
| WPSERVICE | ITEMNUM | Codice risorsa esterna / ricavo | UPPER | 30 |
| WPSERVICE | LINECOST | Costo linea | AMOUNT |  |
| WPSERVICE | X4DATAINIZIORISORSA | Data inizio | DATETIME |  |
| WPSERVICE | X4DATAFINERISORSA | Data fine | DATETIME |  |
| WPSERVICE | ITEMSETID | ID Item set | UPPER | 8 |
|  |  |  |  |  |
|  | X4KPI | KPI economico risorse esterne | AMOUNT |  |
| JOBSERVICE | ITEMNUM | Codice risorsa esterna | UPPER | 30 |
|  | X4TOLLERANZA | Range di tolleranza | FLOAT |  |
| X4CRAFTPERYEAR | PAYRATE | Tariffa oraria | AMOUNT |  |
| JOBLABOR | LABORHRS | Ore figura | DURATION |  |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

WPLABOR: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione delle risorse interne in fase di pianificazione nell’ordine di lavoro (applicazione WOTRACK_D clone dell’applicazione standard WOTRACK).

JOBLABOR: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione delle risorse interne nel piano di lavoro (applicazione X4CATATTIVITA clone dell’applicazione standard JOBPLAN).

ITEM: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione del catalogo risorse esterne – ricavi.

WPSERVICE: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione delle risorse esterne/ricavi in fase di pianificazione nell’ordine di lavoro (applicazione WOTRACK_D clone dell’applicazione standard WOTRACK).

JOBSERVICE: Tabella standard di Maximo; al suo interno sono definiti i campi necessari per la gestione delle risorse esterne nel piano di lavoro (applicazione X4CATATTIVITA clone dell’applicazione standard JOBPLAN).

X4CRAFTPERYEAR: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione del tariffario risorse.

Per permettere la visione dei campi necessari alla pianificazione dei costi delle risorse esterne e interne, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “WOTRACK_D” come duplicato dell’applicazione standard “WOTRACK”, si è configurata la tabella a vista delle risorse interne ed esterne pianificate aggiungendo i seguenti attributi:

I campi del tab “Piani” della tabella “Labor”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Qualifica | CRAFT |  | CRAFTRATE |  |
| Quantità | QUANTITY |  |  |  |
| Ore esecuzione | LABORHRS |  |  |  |
| Ore trasferimento | X4ORETRASF |  |  |  |
| Titris | X4TITRIS |  | UDB |  |
|  | RATE |  |  |  |
| Data inizio risorsa | X4DATAINIZIORISORSA |  | DATELOOKUP |  |
| Data fine risorsa | X4DATAFINERISORSA |  | DATELOOKUP |  |

I campi della tabella “Risorse esterne”, caratterizzata dalla relazione “X4REODL”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Tipo risorsa | ITEM.X4TRISORSA |  | VALUELIST |  |
|  | ITEMNUM | ITEM.DESCRIPTION | X4VENDORCRAFT |  |
| Gruppo merce | ITEM.COMMODITYGROUP |  |  |  |
|  | ITEM.X4VDC |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| Importo | LINECOST |  |  |  |
| Data inizio | X4DATAINIZIORISORSA |  | DATELOOKUP |  |
| Data fine | X4DATAFINERISORSA |  | DATELOOKUP |  |

I campi della tabella “Ricavi”, caratterizzata dalla relazione “X4RICAVIODL” e dalla sigoption “X4RICAVI”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Tipo risorsa | ITEM.X4TRISORSA |  | VALUELIST |  |
| ID | ITEMNUM | ITEM.DESCRIPTION | X4RICAVICODICI |  |
|  |  |  |  |  |
|  | ITEM.X4VDC |  |  |  |
| Importo | LINECOST |  |  |  |
| Data inizio | X4DATAINIZIORISORSA |  | DATELOOKUP |  |
| Data fine | X4DATAFINERISORSA |  | DATELOOKUP |  |

La relazione “ITEM” mostra le righe della tabella “ITEM” dove i campi ITEMNUM e ITEMSETID coincidono con quelli di “WPSERVICE”.

La relazione “X4REODL” mostra le righe della tabella “WPSERVICE” dove i campi WONUM e SITEID coincidono con quelli dell’ordine di lavoro corrente e ovvero X4TRISORSA sia uguale a “Prestazione” o “Materiale”.

La relazione “X4RICAVIODL” mostra le righe della tabella “WPSERVICE” dove i campi WONUM e SITEID coincidono con quelli dell’ordine di lavoro corrente e dove X4TRISORSA = ‘Ricavo’; mentre, la sigoption “X4RICAVI” permette la visualizzazione del tab “Ricavi” solo ed esclusivamente se l’attributo X4TIPOSPESA è uguale a “ANR”.

Gestione risorse esterne nel piano di lavoro

Inoltre, per permettere la pianificazione delle risorse esterne, all’applicazione “Catalogo Attività”, nel tab “Date ed Effort”, viene aggiunto un tab alla tabella delle risorse pianificate denominato “Risorse esterne” (la cui tabella di riferimento è la JOBSERVICE) i cui campi vengono riportati di seguito:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID | ITEMNUM | DESCRIPTION | X4VENDORCRAFT |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

### Sviluppi

Per permettere la gestione della pianificazione costi delle risorse interne ed esterne, si prevedono i seguenti sviluppi a sistema:

Valorizzazione di default effort risorse interne

Alla generazione di un OdL (DSS o manuale), i campi della tabella “Labor” dovranno essere pre-valorizzati come segue:

Qualifica: il campo CRAFT viene popolato in automatico dal sistema alla selezione del PdL.

Quantità: il campo QUANTITY viene popolato in automatico dal sistema alla selezione del PdL.

Ore di esecuzione: il campo LABORHRS viene popolato in automatico dal sistema alla selezione del PdL.

Titris: il campo X4TITRIS verrà popolato automaticamente dal sistema in modo tale che coincida con il campo OWNERGROUP dell’Ordine di lavoro.

Tariffa: il campo RATE verrà popolato automaticamente seguendo la seguente logica: dato il TITRIS calcolato della riga, la RATE di quella qualifica verrà prelevata dall’attributo PAYRATE della tabella X4CRAFTPERYEAR dove il valore di “UDB” sarà pari al campo X4TITRIS della pianificazione, i campi CRAFT delle due tabelle coincidono e, infine, dove l’attributo “ANNO” di X4CRAFTPERYEAR sarà eguale all’anno in cui viene pianificato l’utilizzo di quella risorsa (determinato sulla base dell’anno in cui ricade la data di inizio pianificata della riga di WPLABOR). Nel caso in cui la risorsa venga pianificata con anno di inizio pianificazione diverso dall’anno di fine pianificazione, il quantitativo di ore verrà partizionato dall’utente in modo proporzionale fra i due anni e le righe verranno divise in due.

Data inizio risorsa: il campo X4DATAINIZIORISORSA è calcolato come la differenza tra il campo X4DATAFINERISORSA della tabella WPLABOR e LABORHRS della tabella JOBLABOR.

Data fine risorsa: il campo X4DATAFINERISORSA è ereditato dalla data di fine pianificazione dell’OdL, ovvero TARGCOMPDATE della tabella WORKORDER.

Modifica risorse interne pianificate

L’utente può modificare le informazioni pre-valorizzate della tabella “Labor” come segue:

Titris: modificabile selezionando da una lista (i record di PERSONGROUP) un TITRIS diverso dal responsabile attività. La lista permetterà di scegliere all’utente le sole UdB che hanno ceduto delle ore di personale all’UdB responsabile dell’attività dell’OdL. Tale informazione verrà definita all’interno del dominio del campo X4TITRIS della WPLABOR, imponendo che le UdB selezionabili siano quelle presenti nelle righe della tabella X4CAPACITYEXC che, come valore del campo DESTINATARIO, abbiano valore uguale al resp. Attività dell’OdL.

Data inizio e fine risorsa: verrà effettuato un controllo sulla data di inizio e fine risorsa in modo che non venga mai inserita una data di inizio risorsa antecedente al campo TARGSTARTDATE dell’OdL di cui si sta pianificando e che almeno una di queste sia coincidente con quella pianificata (TARGSTARTDATE): nel caso ciò avvenga verrà mostrato un errore all’utente che blocchi la sua operatività. Analogamente verrà effettuato un controllo sulla data di fine risorsa, in modo che non venga mai inserita una data di fine risorsa successiva al campo TARGCOMPDATE dell’OdL di cui si sta pianificando e che almeno una di queste sia coincidente con quella pianificata (TARGCOMPDATE).

Inserimento nuove risorse interne

Durante l’inserimento di nuove risorse interne il campo X4TITRIS verrà popolato automaticamente dal sistema in modo tale che coincida con il campo OWNERGROUP dell’Ordine di lavoro. Successivamente, sarà possibile modificare il campo X4TITRIS della tabella WPLABOR, il quale ammetterà come valore soltanto le UdB (i record di GROUPUSER).

Inoltre, verrà effettuato un controllo sulla data di inizio e fine risorsa in modo che non venga mai inserita una data di inizio risorsa antecedente al campo TARGSTARTDATE dell’OdL di cui si sta pianificando: nel caso ciò avvenga verrà mostrato un errore all’utente che blocchi la sua operatività. Analogamente verrà effettuato un controllo sulla data di fine risorsa, in modo che non venga mai inserita una data di fine risorsa successiva al campo TARGCOMPDATE dell’OdL di cui si sta pianificando.

Valorizzazione di default effort risorse esterne

Alla generazione di un OdL (DSS o manuale), i campi della tabella “Risorse Esterne” dovranno essere pre-valorizzati come segue:

Tipo risorsa: il campo X4TRISORSA verrà popolato automaticamente dal sistema con lo stesso identificativo inserito nel PdL mediante relazione “ITEM”.

Resource ID: il campo ITEMNUM verrà popolato automaticamente dal sistema con lo stesso identificativo inserito nel PdL.

Descrizione : il campo X4DESCRISORSA verrà popolato automaticamente dal sistema con lo stesso identificativo inserito nel PdL mediante relazione “ITEM”.

Gruppo merce: il campo X4GMERCE verrà popolato automaticamente dal sistema con lo stesso identificativo inserito nel PdL mediante relazione “ITEM”.

VdC: il campo X4VDC verrà popolato automaticamente dal sistema con lo stesso identificativo inserito nel PdL mediante relazione “ITEM”.

Importo: Alla selezione del jobplan dell’ordine di lavoro, se nel Job Plan è stato definito un valore di associato al Resource ID della risorsa esterna pianificata, allora il valore del costo di quest’ultima sarà ottenuto effettuando la moltiplicazione del e inserito nel campo LINECOST della riga di pianificazione risorsa esterna.

Data inizio e fine: ereditati rispettivamente dalla data inizio e fine pianificazione dell’OdL.

Modifica risorse esterne pianificate

L’utente può modificare le informazioni pre-valorizzate della tabella “Risorse esterne” come segue:

Resource ID: modificabile selezionando da una lista (i record di ITEM) di risorse esterne censite a sistema.

Data inizio e fine: verrà effettuato un controllo sulla data di inizio e fine in modo che non venga mai inserita una data di inizio antecedente al campo TARGSTARTDATE dell’OdL di cui si sta pianificando: nel caso ciò avvenga verrà mostrato un errore all’utente che blocchi la sua operatività. Analogamente verrà effettuato un controllo sulla data di fine, in modo che non venga mai inserita u una data di fine successiva al campo TARGCOMPDATE dell’OdL di cui si sta pianificando.

Inserimento nuove risorse esterne

Durante l’inserimento di nuove risorse esterne sarà necessario definire il campo ITEMNUM della tabella WPSERVICE. Questo campo ammetterà come valore soltanto le risorse censite a sistema come esterne. (Tipologia risorsa dell’ITEMNUM posto come “Prestazione” o “Materiale”)

Inoltre, verrà effettuato un controllo sulla data di inizio e fine in modo che non venga mai inserita una data di inizio antecedente al campo TARGSTARTDATE dell’OdL di cui si sta pianificando: nel caso ciò avvenga verrà mostrato un errore all’utente che blocchi la sua operatività. Analogamente verrà effettuato un controllo sulla data di fine, in modo che non venga mai inserita una data di fine successiva al campo TARGCOMPDATE dell’OdL di cui si sta pianificando.

Inserimento/modifica ricavi

Durante l’inserimento di ricavi sarà necessario definire il campo ITEMNUM della tabella WPSERVICE. Questo campo ammetterà come valore soltanto le risorse censite a sistema come ricavi. (Tipologia risorsa dell’ITEMNUM posto come “Ricavo”)

Inoltre, verrà effettuato un controllo sulla data di inizio e fine in modo che non venga mai inserita una data di inizio antecedente al campo TARGSTARTDATE dell’OdL di cui si sta pianificando: nel caso ciò avvenga verrà mostrato un errore all’utente che blocchi la sua operatività. Analogamente verrà effettuato un controllo sulla data di fine, in modo che non venga mai inserita una data di fine successiva al campo TARGCOMPDATE dell’OdL di cui si sta pianificando.

## Generazione OdL serbatoio

Il serbatoio è un’entità logica finalizzata alla pianificazione delle attività per le quali non è definito un asset specifico. Quest’ultimo rappresenta un centro di imputazione di risorse economiche, ore uomo e driver tecnici legato ad un asset fittizio, a cui attingere in fase di consuntivazione. Inoltre, ogni serbatoio deve presentare la corrispondenza con un OdL.

### Configurazioni

Per la generazione degli OdL a serbatoio è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito viene riportato in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4SERBATOIO | IDSERBATOIO | Codice serbatoio Univoco | ALN | 50 |
| X4SERBATOIO | ANNO | Anno | INT |  |
| X4SERBATOIO | UDB | UdB di ripartizione del serbatoio | UPPER | 8 |
| X4SERBATOIODETAILS | FISICO | Quantità di fisico del serbatoio | YORN |  |
| X4SERBATOIODETAILS | UM | Unità di misura | UPPER | 5 |
| X4SERBATOIODETAILS | COIN | Codice COIN | ALN | 15 |
| X4SERBATOIODETAILS | TERNA | Percentuale TERNA | INT |  |
| X4SERBATOIODETAILS | CRAFT | Competenza (Operaio, Impiegato, Quadro) | UPPER | 8 |
| X4SERBATOIODETAILS | TERNARETE | Terna o Rete? | UPPER |  |
| X4SERBATOIODETAILS | RETE | Percentuale RETE | INT |  |
| X4SERBATOIODETAILS | GENNAIO |  | ALN | 10 |
| X4SERBATOIODETAILS | FEBBRAIO |  | ALN | 10 |
| X4SERBATOIODETAILS | MARZO |  | ALN | 10 |
| X4SERBATOIODETAILS | APRILE |  | ALN | 10 |
| X4SERBATOIODETAILS | MAGGIO |  | ALN | 10 |
| X4SERBATOIODETAILS | GIUGNO |  | ALN | 10 |
| X4SERBATOIODETAILS | LUGLIO |  | ALN | 10 |
| X4SERBATOIODETAILS | AGOSTO |  | ALN | 10 |
| X4SERBATOIODETAILS | SETTEMBRE |  | ALN | 10 |
| X4SERBATOIODETAILS | OTTOBRE |  | ALN | 10 |
| X4SERBATOIODETAILS | NOVEMBRE |  | ALN | 10 |
| X4SERBATOIODETAILS | DICEMBRE |  | ALN | 10 |
| X4SERBATOIODETAILS |  | Totale | ALN | 15 |
| WORKORDER | X4SERBATOIOID | ID Univoco serbatoio | BIGINT | 19 |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

X4SERBATOIO: Tabella custom di Maximo; al suo interno sono definiti i campi di testata necessari per la gestione dei serbatoi.

X4SERBATOIODETAILS: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione delle tabelle di ripartizione dell’effort e del fisico.

Il campo COIN, nel dettaglio, viene collegato ad un dominio di tipo tabellare che permette di definire il valore del campo sulla base dei valori presenti nel campo “IDCOIN” della tabella X4COIN.

Il campo TERNARETE è necessario per far gestire all’utente se la riga di serbatoio è da imputare alla società RETE o società TERNA. Gli unici valori ammissibili per questo attributo sono TERNA o RETE, selezionabili tramite opportuna lookup.

Il flag FISICO permette di distinguere le righe di X4SERBATOIODETAILS che si applicano alle qualifiche (QUADRO, IMPIEGATO o OPERAIO) da quelle del fisico pianificato.

Per permettere la visione dei campi necessari alla gestione degli OdL a serbatoio, si dovrà prevedere la seguente configurazione a sistema. In particolare, è stata creata l’applicazione “Gestione serbatoi” denominata a sistema “X4SERBATOI”, riportante i seguenti campi in formato tabellare del tab “Serbatoio”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Codice Coin | COIN |  | X4COIN |  |
| Unità di misura | UM |  |  | X4FISICO |
|  |  |  |  |  |
|  |  |  |  |  |
| ? | TERNARETE |  | VALUELIST |  |
| Gennaio | GENNAIO |  |  |  |
| Febbraio | FEBBRAIO |  |  |  |
| Marzo | MARZO |  |  |  |
| Aprile | APRILE |  |  |  |
| Maggio | MAGGIO |  |  |  |
| Giugno | GIUGNO |  |  |  |
| Luglio | LUGLIO |  |  |  |
| Agosto | AGOSTO |  |  |  |
| Settembre | SETTEMBRE |  |  |  |
| Ottobre | OTTOBRE |  |  |  |
| Novembre | NOVEMBRE |  |  |  |
| Dicembre | DICEMEBRE |  |  |  |
| Totale |  |  |  |  |

Questa tabella sarà mostrata mediante relazione per e fisico. In particolare, permentre per il fisico, la relazione X4SERBATOIODETAILSFISICO avrà la condizione (X4SERBATOIOID=:X4SERBATOIOID and FISICO='1').

La sigoption X4FISICO mostra i campi ai quali è applicata se il flag FISICO della tabella X4SERBATOIODETAILS è pari a 1.

È inoltre presente un altro tab in applicazione denominato OdL Serbatoio

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Ordine di lavoro | WONUM |  |  |  |
| Codice Coin | X4COIN |  |  |  |
| Società | X4BURKS |  |  |  |
| Data inizio | TARGSTARTDATE |  |  |  |
| Data fine | TARGCOMPDATE |  |  |  |

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Ordine di lavoro | WONUM |  |  |  |
| ID risorsa esterna | ITEMNUM |  |  |  |
| Costo | LINECOST |  |  |  |

La prima tabella ha lo scopo di riportare tutti gli OdL creati tramite la funzione di generazione massiva. La relazione utilizzata per popolare la tabella è definita come X4SERBATOIOODL (parent X4SERBATOIO e figlio WORKORDER) ed è la seguente: :X4SERBATOIOID=X4SERBATOIOID.

La seconda tabella, invece, riporta tutte le righe di risorse esterne pianificate per gli ordini generati da funzione massiva di generazione OdL serbatoio. La relazione utilizzata per popolare la tabella è definita come X4WORISEST (parent X4SERBATOIO e figlio WPSERVICE) ed è la seguente: wonum in (select wonum from workorder where :X4SERBATOIOID=X4SERBATOIOID.)

### Sviluppi

Per permettere la gestione degli OdL a serbatoio, si prevedono i seguenti sviluppi a sistema:

Impostazione automatica delle percentuali di ripartizione

Un automation script, alla validazione del campo TERNARETE, imposterà automaticamente i valori di ripartizione percentuali “TERNA” e “RETE” secondo il seguente principio:

se l’utente seleziona la voce “TERNA” nell’attributo TERNARETE, allora verrà impostato a 100 l’attributo TERNA ed a 0 l’attributo RETE; se l’utente seleziona la voce “RETE” nell’attributo TERNARETE, allora verrà impostato a 100 l’attributo RETE ed a 0 l’attributo TERNA.

Generazione massiva OdL serbatoio

Creazione di un pulsante che innesca la generazione massiva di OdL serbatoio, ripartendo le ore di capacity da assegnare al serbatoio per l’UdB e per l’anno del record aperto. L’attivazione di questa funzionalità sarà possibile solo se tutte le tabelle presenti sono valorizzate.

Nel dettaglio viene eseguito un automation script che genererà N record nella tabella WORKORDER in stato “”, dove N è il prodotto del mese, delle COIN su cui ripartire le ore di capacity e potrebbe inoltre essere moltiplicato per due se per ciascuna UdB. Per ognuno di questi record, le date di pianificazione (TARGSTARTDATE e TARGCOMPDATE del WORKORDER) verranno settate con la data di inizio e fine del mese per cui sono stati creati e l’attributo X4COIN verrà posto uguale alla CoIn di riferimento.

L’attributo JPNUM dell’ordine di lavoro verrà automaticamente popolato con il valore del PdL che caratterizza i serbatoi.

Questo sarà determinato all’interno dello script inerente alla generazione massiva degli OdL serbatoio; in particolare, sarà presente un array contenente la tupla di COIN e JPNUM dell’OdL serbatoio associato.

Inoltre, verranno anche create delle relative righe nelle tabelle WPLABOR (con attributo WONUM pari all’odl generato) in cui verranno computate le ore di impiegati, quadri e operai da ripartire a quel mese per la specifica CoIn. Tale numero di ore verrà prelevato dalla tabella X4CAPACITYDETAILS, nel dettaglio dall’attributo “SERBATOIO” del mese per cui è pianificato il serbatoio.

Inoltre, negli OdL generati, verrà impostato il campo X4FISICOSERBATOIO (della tabella workorder) con l’ammontare di fisico del corrispettivo mese. Nel dettaglio, al momento di generazione degli OdL, quando verrà generato l’OdL di ogni mese per la coin, verrà verificata la presenza di una riga con lo stesso X4SERBATOIOID nella tabella X4SERBATOIODETAILS con il flag FISICO posto a True ed il valore del corrispettivo mese verrà posto nel campo X4FISICOSERBATOIO della tabella WORKORDER.

Tutti gli ordini di lavoro generati attraverso questa funzionalità inoltre avranno il campo X4SERBATOIOID (tabella WORKORDER) con l’ID Univoco della riga di X4SERBATOIO da cui sono stati generati.

Tabelle di ripartizione del fisico

Nella tabella di ripartizione del fisico (X4SERBATOIODETAILS con FISICO =’1’), il campo COIN viene pre-valorizzato con i valori presenti nelle tabelle di ripartizione dell’effort (X4SERBATOIODETAILS delle varie CRAFT) che presentano il medesimo IDSERBATOIO.

Ciò significa che al salvataggio di una qualsiasi altra riga di serbatoio (ad es. Il serbatoio delle ore impiegato di una specifica CoIn) verrà creata automaticamente dal sistema una nuova riga di pianificazione serbatoio fisico con la coIn pianificata (nel caso in cui questa non esista già).

Controllo valori imputati nelle ripartizioni mensili

A seguito di un salvataggio sulla tabella X4SERBATOIODETAILS, un automation script verrà triggerato per verificare che la somma di tutte le righe di X4SERBATOIODETAILS, appartenenti all’UdB di cui si sta modificando il contenuto, non superino il valore designato all’interno dell’applicazione di gestione capacity.

Quindi, un automation script effettuerà le somme imputate per ogni mese (attributo GENNAIO, FEBBRAIO, etc) per una specifica competenza (es. CRAFT=OPERAIO) e ne comparerà il risultato con il valore dell’attributo SERBATOIO di X4CAPACITYPM dove “X4CAPACITYID in (select X4CAPACITYID FROM X4CAPACITY WHERE UDB=:UDB and ANNO=:ANNO) and CRAFT=:CRAFT” e, se risulta maggiore, verrà mostrato un errore di sistema che non permetterà l’utente di poter procedere con il salvataggio.

## Gestione delle tariffe

Le tariffe sono lo strumento impiegato per permettere il calcolo del valore economico dell’effort orario degli ordini di lavoro in fase di pianificazione. L’applicativo IBM Maximo consentirà agli utenti abilitati la gestione differenziata delle tariffe specifiche per anno e per unità di budget, adottando una struttura dati custom.

### Configurazioni

Per la gestione dei dati inerenti al tariffario delle risorse, è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito viene riportato in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4CRAFTPERYEAR | CRAFT | Competenza (Operaio, Impiegato, Quadro) | UPPER | 8 |
| X4CRAFTPERYEAR | UDB | UdB di riferimento | UPPER | 8 |
| X4CRAFTPERYEAR | PAYRATE | Tariffa oraria | AMOUNT |  |
| X4CRAFTPERYEAR | ANNO | Anno di riferimento | INT |  |
| X4CRAFTPERYEAR | SCENARIO | Scenario importato | ALN | 25 |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

X4CRAFTPERYEAR: Tabella custom di Maximo; al suo interno sono definiti i campi necessari per la gestione del tariffario risorse.

Per permettere la visione dei campi necessari alla gestione delle tariffe, si dovrà prevedere la seguente configurazione a sistema. In particolare, è stata modificata l’applicazione Standard denominata “Crafts”, ed al suo interno è stato creato un nuovo Tab contenente le informazioni aggiuntive:

I campi del tab “Tariffario risorse”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| UdB | UDB |  |  |  |
| Anno | ANNO |  |  |  |
| Figura professionale | CRAFT |  |  |  |
| Costo medio orario (€/H) | PAYRATE |  |  |  |
| Scenario | SCENARIO |  |  |  |

### Sviluppi

Per permettere la funzionalità di inserimento del costo medio orario, si prevede il seguente sviluppo a sistema:

Pulsante “Import massivo” da Tagetik

La richiesta della tariffa oraria deve avvenire da opportuna dialog in cui viene specificato lo scenario Tagetik e l'anno di riferimento. Tale funzionalità è disponibile solo per gli utenti centrali.

Le competenze (OPERAIO, QUADRO, IMPIEGATO) dovranno avere una gestione del tariffario che si differenzia per UdB di appartenenza e per anno. Per l'anno n+1, per singola UdB, si deve avere un'univoca voce di tariffa oraria:

Gestione tariffe competenza

Al popolamento della tabella di WPLABOR, la tariffa oraria deve essere prelevata dinamicamente da uno script. Lo script agirà all'add o all’update di un record della tabella WPLABOR, nel dettaglio la modifica di un attributo quale CRAFT o X4TITRIS della medesima tabella.

Per relazione, sulla base della competenza e dell'UdB inserita nella tabella e sulla base dell'anno specificato nella riga di WPLABOR, viene preso il valore della tariffa oraria e viene impostato nell’attributo RATE della WPLABOR. Nel dettaglio, sulla base del valore X4TITRIS presente nella riga di WPLABOR modificata/inserita e sulla base dell’anno in cui è stata pianificata (attributo X4DATAFINERISORSA della riga di WPLABOR), viene prelevato il valore della tariffa oraria di quella competenza, per quell’UdB di appartenenza e per l’anno.

Alla modifica di una tariffa oraria, quest’ultima dovrà propagarsi su tutti gli OdL che non presentano consuntivi inviati a SAP:

Propagazione modifica tariffa oraria

Script che parte alla modifica dell'attributo della tariffa oraria di un qualsiasi anno/UdB. Per relazione, per tutti gli OdL che appartengono a quella UDB modificata, tutte le righe di WPLABOR che sono incluse nell'anno modificato devono essere aggiornate (nel dettaglio viene modificato l’attributo RATE con il costo annuo della tabella custom appena modificato).

Lo script modificherà tutte le righe di WPLABOR che soddisfano le seguenti condizioni:

Le righe di WPLABOR che hanno X4TITRIS pari all’UdB della X4CRAFTPERYEAR;

Le righe di WPLABOR che hanno come anno della data di fine pianificazione eguale a quello della riga di X4CRAFTPERYEAR modificata.

Le righe di WPLABOR associate a un OdL che non presenta consuntivi inviati a SAP, ovvero: WONUM IN(SELECT REFWO FROM LABTRANS WHERE GENAPPRSERVRECEIPT = '0'). Inoltre, tale dato viene aggiornato solo in pianificazione e non in consuntivazione (tabella LABTRANS) in quanto i costi effettivi vengono recuperati da Tagetik

## Cataloghi

### Configurazioni

Per la gestione dei dati inerenti ai cataloghi è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4CATMOS | IDATTSTANDARD | ID attività standard | ALN | 15 |
| X4CATMOS | DESCRIPTION | Descrizione | ALN | 50 |
| X4CATMOS | SPESAPREVALENTE | Tipo spesa prevalente | ALN | 15 |
| X4CATMOS | COINPREVALENTE | Coin prevalente | ALN | 15 |
| X4COIN | IDCOIN | Codice Coin | ALN | 15 |
| X4COIN | TIPOORDINE | Tipo ordine | ALN | 15 |
| X4COIN | TIPOATTIVITA | Tipo attività PM | ALN | 15 |
| X4COIN | TIPOSPESA | Tipo spesa | ALN | 15 |
| X4COIN | DESCRIPTION_LONGDESCRIPTION | Note | LONGALN | 32000 |
| X4WBE | UDB | Responsabile attività | ALN | 15 |
| X4WBE | TIPOSPESA | Tipo spesa | ALN | 15 |
| X4WBE | COIN | Codice Coin | ALN | 15 |
| X4WBE | IDWBS | ID CBS di appartenenza | ALN | 20 |
| X4WBE | IDWBE | ID CBE | ALN | 20 |
| X4MATRICEDEST | ORIGINE | Origine | ALN | 20 |
| X4MATRICEDEST | DESTINAZIONE | Destinazione | UPPER | 30 |
| X4MATRICEDEST | TTRASF | Tempo di trasferimento | DURATION |  |
| X4CATMOSCOIN | X4CATMOSID | ID univoco/chiave della tabella X4CATMOS | BIGINT |  |
| X4CATMOSCOIN | TIPOSPESA | Tipo spesa | ALN | 15 |
| X4CATMOSCOIN | COIN | Codice Coin | ALN | 15 |

Per permettere la visione dei campi necessari all’interno del Catalogo Standard, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4CATMOS”, si dovranno configurare:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Attivtà standard | IDATTSTANDARD | DESCRIPTION |  |  |
| Tipo spesa prevalente | SPESAPREVALENTE |  | VALUELIST |  |
| Codice Coin prevalente | COINPREVALENTE |  | VALUELIST |  |

I campi della tabella “Codice Coin OPEX”, caratterizzata dalla relazione X4OPEX:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Coin | COIN |  | VALUELIST |  |

La relazione “X4OPEX” lega le tabelle X4CATMOS e X4CATMOSCOIN mediante la condizione :X4CATMOSID = X4CATMOSID and TIPOSPESA = ‘OPEX’ e permette la gestione di codici Coin multi-valore di tipo spesa OPEX.

I campi della tabella “Codice Coin CAPEX”, caratterizzata dalla relazione X4CAPEX:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Coin | COIN |  | VALUELIST |  |

La relazione “X4CAPEX” lega le tabelle X4CATMOS e X4CATMOSCOIN mediante la condizione :X4CATMOSID = X4CATMOSID and TIPOSPESA = ‘CAPEX’ e permette la gestione di codici Coin multi-valore di tipo spesa CAPEX.

I campi della tabella “Codice Coin ANR”, caratterizzata dalla relazione X4ANR:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Coin | COIN |  | VALUELIST |  |

La relazione “X4ANR” lega le tabelle X4CATMOS e X4CATMOSCOIN mediante la condizione :X4CATMOSID = X4CATMOSID and TIPOSPESA = ‘ANR’ e permette la gestione di codici Coin multi-valore di tipo spesa ANR.

Per permettere la visione dei campi necessari all’interno del Catalogo Coin, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4COIN”, si dovranno configurare:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Codice Coin | IDCOIN |  |  |  |
| Tipo ordine | TIPOORDINE |  |  |  |
| Tipo attività PM | TIPOATTIVITA |  |  |  |
| Tipo spesa | TIPOSPESA |  | VALUELIST |  |
| Note | DESCRIPTION_LONGDESCRIPTION |  |  |  |

Per permettere la visione dei campi necessari all’interno del Catalogo CBS-CBE pianificate, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4WBE”, si dovranno configurare:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Responsabile attività (UI) | UDB |  |  |  |
| Tipo spesa | TIPOSPESA |  |  |  |
| CBS Pianificata | IDWBS |  |  |  |
| CBE Pianificata | IDWBE |  |  |  |
| Codice Coin | COIN |  |  |  |

Il catalogo CBS-CBE pianificate sarà caratterizzato dai record della tabella X4WBE che presentano il flag PIANIFICAZIONE della medesima tabella pari ad 1.

Per permettere la visione dei campi necessari all’interno del Catalogo Risorse Esterne - Ricavi, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “X4SITEM”, clone dell’applicazione “SRVITEM”, si dovranno configurare:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Resource ID | ITEMNUM |  |  |  |
| Descrizione | DESCRIPTION |  |  |  |
| Gruppo Merce | COMMODITYGROUP |  |  |  |
| VdC/VdR | X4VDC |  |  |  |
| Tipo Risorsa | X4TRISORSA |  | VALUELIST |  |

Il tipo risorsa è collegato ad un dominio alfanumerico (denominato anch’esso X4TRISORSA) che rende ammissibili i seguenti valori: Prestazione, Materiale, Ricavo.

Le lookup associate ai campi denominati “Resource ID” delle tabelle di pianificazione delle risorse esterne e ricavi permetteranno di selezionare rispettivamente gli elementi censiti in anagrafica che corrispondono ad un tipo risorsa eguale a “Prestazione” o “Materiale” nel primo caso, “Ricavo” nel secondo.

Per permettere la visione dei campi necessari all’interno del Matrice Origine - Destinazione, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4MATRICEDEST”, si dovranno configurare:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Origine | ORIGINE |  | VALUELIST |  |
| Destinazione | DESTINAZIONE |  | VALUELIST |  |
| Tempo di trasferimento | TTRASF |  |  |  |

### Sviluppi

Per permettere la gestione dei cataloghi sopra descritti, si prevedono i seguenti sviluppi:

Eliminazione record dal catalogo codice Coin

Eliminando un codice Coin dal catalogo Coin che è utilizzato anche all’interno del Catalogo Standard, il sistema, al momento del salvataggio, mostrerà un messaggio di warning “Codice Coin eliminato anche all’interno del Catalogo Standard”.

Popolamento automatico del tempo di trasferimento OdL

Alla generazione dell’ordine di lavoro e sulla base del TITRIS di appartenenza, il dato del tempo di trasferimento viene calcolato automaticamente. Nel dettaglio, verrà creata una tabella X4MATRICEDEST che per ogni UdB possibile all’interno del sistema, abbia associato un tempo di trasferimento verso ogni specifica sede tecnica di primo livello. Nel dettaglio, il campo ORIGINE della tabella X4MATRICEDEST sarà caratterizzato dal dominio tabellare “X4UDB” che preleverà i record di PERSONGROUP con X4UDB = 1, mentre il campo DESTINAZIONE della medesima tabella sarà caratterizzato dal dominio “X4SAPID” che preleva i record di ASSET con CLASSSTRUCTUREID della tabella CLASSSTRUCTURE dove CLASSIFICATIONID è pari a LINEA o STAZIONE. Inoltre, la tabella sarà modificabile dall’utente (sia in termini di creazione di nuove righe che di modifica/eliminazione di righe esistenti) mediante applicazione dedicata X4MATRICEDEST.

|  |  |
| --- | --- |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |
