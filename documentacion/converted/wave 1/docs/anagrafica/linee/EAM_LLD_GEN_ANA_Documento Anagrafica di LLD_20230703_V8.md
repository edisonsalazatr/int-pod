# EAM_LLD_GEN_ANA_Documento Anagrafica di LLD_20230703_V8

|  | Disegno di Basso Livello (LLD) dell’Anagrafica |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Relazione: | DISEGNO DI BASSO LIVELLO (LLD) DELL’ANAGRAFICA |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 1 | Primo rilascio | 26/07/2023 |
| 2 | Revisione e risposta ai commenti | 06/09/2023 |
| 3 | Revisione e risposta ai commenti | 02/10/2023 |
| 4 | Aggiornamento TernaGora e Rete2 | 19/09/2025 |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | RTC | 26/07/2023 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

INDICE1 Introduzione 8

1.1 Glossario 9

1.2 Tabelle del DB del Manage 11

1.3 Documenti citati 12

2 Database Configuration 13

2.1 Oggetti Standard e attributi Standard (senza modifiche) 13

2.2 Oggetti Standard e attributi Custom 16

2.3 Oggetti Standard e attributi Standard (con modifiche) 18

2.4 Oggetti Custom 19

2.5 Relazioni 19

2.6 Messaggi 25

3 Domini 29

3.1 Domini NUM 29

3.2 Domini ALN 30

3.3 Domini di tipo tabella 30

3.4 Domini Synonym 30

4 Application Designer 32

4.1 Linee 32

4.1.1 Tab “Linea” 33

4.1.1.1 Primo livello asset 33

4.1.1.2 Secondo livello linea 34

4.1.1.3 Terzo livello linea 37

4.1.1.4 Quarto livello linea 40

4.1.2 Tab “Riepilogo dati” 42

4.1.3 Tab “Tronchi” 46

4.1.4 Tab “Dati Linea” 47

4.1.5 Attributi calcolati su Tab “Dati Linea” 48

4.1.6 Tab “Linee in relazione” 50

4.2 Gruppi di persone Terna (X3PERSONGR) 53

5 Detailed processing 55

5.1 Automation Scripts 55

5.2 Azioni 60

5.3 Conditional expression manager 62

5.4 Sigoption 63

6 Gruppi di protezione 65

6.1 Global Restriction 66

7 Gruppi di persone 67

8 Recepimento OP_252 - OP_253 - OP_254 - OP_256- OP_305 70

8.1 OP_252 – Gestione del legame tra linee elettriche differenti 70

8.2 OP_253 – derivazioni multiple dallo stesso sostegno della dorsale 70

8.3 OP_305 - Rimozione della gestione della lettera K per il terminale di fine 70

8.4 OP_256 - Derivazioni miste aereo e cavo 71

8.5 OP_254 - Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate) 71

9 Anagrafica 72

10 Anagrafica TernaGora 73

INDICE DELLE TABELLETabella 1 - Oggetti Standard con Attributi Custom 19

Tabella 2 - Oggetti Standard con Attributi Standard modificati 20

Tabella 3 - Relazioni Custom 26

Tabella 4 - Relazioni Standard 26

Tabella 5 -Messaggi 28

Tabella 6 - Domini Synonym 32

Tabella 7 - Campi e Proprietà “Primo Livello Linea” 35

Tabella 8 - Proprietà Sezione “Asset di Secondo Livello” 36

Tabella 9 - Campi e Proprietà “Asset di Secondo Livello” 37

Tabella 10 - Proprietà Sottosezione “Specifiche Tecniche” (Secondo Livello) 38

Tabella 11 - Campi e Proprietà “Specifiche Tecniche” (Secondo Livello) 38

Tabella 12 - Proprietà Sezione “Terzo Livello Linea” 39

Tabella 13 - Campi e Proprietà “Terzo Livello Linea” 40

Tabella 14 - Proprietà Sottosezione “Specifiche Tecniche” (Terzo Livello) 40

Tabella 15 - Campi e Proprietà “Specifiche Tecniche” (Terzo Livello) 41

Tabella 16 - Proprietà Sezione “Quarto Livello Linea” 41

Tabella 17 - Campi e Proprietà “Quarto Livello Linea” 42

Tabella 18 - Proprietà Sottosezione “Specifiche Tecniche” (Quarto Livello) 43

Tabella 19 - Campi e Proprietà “Specifiche Tecniche” (Quarto Livello) 43

Tabella 20 – Livello di Tensione 44

Tabella 21 – Campi e proprietà “Riepilogo Dati” 45

Tabella 22 - Proprietà Sezione “Tronchi” 47

Tabella 23 - Campi e Proprietà “Tronchi” 48

Tabella 24 - Campi e Proprietà “Dati Linea” 49

Tabella 25 -Attributi Calcolati 51

Tabella 25 - Proprietà Sezione “Linee in Relazione” 52

Tabella 26 - Campi e Proprietà “Linee in Relazione” 53

Tabella 27 - Automation Scripts 61

Tabella 28 - Azioni 63

Tabella 29 - Conditional Expressions 64

Tabella 30 - Sigoption 65

Tabella 31 - Gruppi di Protezione 66

Tabella 32 – Global Restriction 67

Tabella 33 - Gruppi di persone "UI" 70

Tabella 34 - Gruppi di persone "DT" 70

INDICE DELLE FIGURE

Figura 1 - Schermata “Linee” 27

Figura 2 - Schermata “Primo Livello Linea” 29

Figura 3 - Sezione “Asset di Secondo Livello” 30

Figura 4 - Sezione “Secondo Livello Linea” 31

Figura 5 - Sottosezione “Specifiche Tecniche” (Secondo Livello) 31

Figura 6 - Sezione “Terzo Livello Linea” 33

Figura 7 - Sottosezione “Specifiche Tecniche” (Terzo Livello) 34

Figura 8 - Sezione “Quarto Livello Linea” 35

Figura 9 - Sottosezione “Specifiche Tecniche” (Quarto Livello) 36

Figura 10 - Schermata “Riepilogo Dati” 38

Figura 13 – Comando “Riepilogo Dati” 39

Figura 11 - Schermata “Tronchi” 40

Figura 12 - Schermata “Dati Linea” 41

Figura 14 - Schermata “Linee in Relazione” 43

Figura 15 - Schermata “Gruppi di Persone Terna” 45

Figura 16 - Dettaglio “Gruppo di Persone Terna” 46

# Introduzione

Il presente documento ha l’obiettivo di indirizzare le tematiche, già introdotte nel documento di HLD EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0, verso un design più dettagliato e specifico, orientato all’implementazione effettiva del software. Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni sviluppate sulla piattaforma EAM di Terna (ambiente Dev) suddivise per applicazioni principali MAS.

Nel dettaglio:

Database Configuration, consente di creare, modificare e gestire tabelle, attributi, indici e relazioni all'interno del database di Maximo. Qui è possibile definire la struttura del database e configurare i dati in modo da adattarsi alle specifiche esigenze dell'organizzazione.

Domini, permette di creare e modificare elenchi di valori di diversi tipi, come ALN (Alfanumerico), NUM (Numerico), TABLE (Tabella) e SYNONIM (Sinonimo). Questi elenchi vengono utilizzati per definire i valori possibili per determinati campi all'interno delle applicazioni di Maximo.

Application Designer, consente di creare e personalizzare l'interfaccia grafica delle applicazioni presenti in MaximoE’ possibile aggiungere tabelle, sezioni e campi per adattare l'aspetto e il comportamento delle applicazioni in base alle specifiche esigenze aziendali.

Automation Scripts, permette di creare script in diversi linguaggi di programmazione che possono essere utilizzati al fine di automatizzare processi, eseguire calcoli complessi e personalizzare il comportamento delle applicazioni.

In particolare, gli script relativi all’Anagrafica creati nell’ambiente Dev di Terna sono in linguaggio Python.

Conditional Expression, permette di creare e modificare espressioni in linguaggio SQL utili per gestire le regole di visibilità e/o editabilità di determinati oggetti all'interno di Maximo

Gruppi di Sicurezza, ogni gruppo di sicurezza consente di impostare e gestire i privilegi degli utenti e controllare, quindi, l'accesso e i permessi alle diverse funzionalità e dati all'interno del sistema.

Gruppi di Persone, permette di raggruppare più utenti al fine di semplificare e ottimizzare la gestione delle attività e delle risorse all'interno dell'applicativo. Con i Gruppi di Persone è possibile assegnare responsabilità specifiche, facilitare la comunicazione e organizzare le risorse umane in modo efficiente per una migliore gestione dei processi aziendali in Maximo.

## Glossario

| TERMINE | SPIEGAZIONE | NOTA |
| --- | --- | --- |
| TABLE.ATTRIBUTE | Convenzione utilizzata per fare riferimento a un attributo specifico di una tabella nel database di Maximo | Esempio: PERSONGROUP.X3PARENTPERSONGROUPà Fa riferimento all’attributo X3PARENTPERSONGROUP presente all’interno della tabella PERSONGROUP |
| MBO | Maximo Business Object, definisce un set di campi e di regole di Business dell’applicativo (Maximo) di una o più tabelle del database |  |
| Database Configuration | Applicazione che consente di creare, modificare e gestire tabelle, attributi, indici e relazioni all'interno del database di Maximo. Qui è possibile definire la struttura del database e configurare i dati in modo da adattarsi alle specifiche esigenze dell'organizzazione. |  |
| Domini | Applicazione che permette di creare e modificare elenchi di valori di diversi tipi, come ALN (Alfanumerico), NUM (Numerico), TABLE (Tabella) e SYNONIM (Sinonimo). Questi elenchi vengono utilizzati per definire i valori possibili per determinati campi all'interno delle applicazioni di Maximo. |  |
| Automation Scripts | Applicazione che permette di creare script in diversi linguaggi di programmazione al fine di automatizzare processi, eseguire calcoli complessi e personalizzare il comportamento delle applicazioni |  |
| Application Designer | Applicazione che consente di creare e personalizzare l'interfaccia grafica delle applicazioni presenti in Maximo. Gli utenti possono aggiungere tabelle, sezioni e campi per adattare l'aspetto e il comportamento delle applicazioni in base alle specifiche esigenze aziendali |  |
| Conditional Expression | Applicazione che permette di creare e modificare espressioni in linguaggio SQL utili per gestire le regole di visibilità e/o editabilità di determinati oggetti all'interno di Maximo |  |
| Gruppi di Protezione | Applicazione che permette di creare e gestire i Gruppi di Sicurezza. Per ogni gruppo di sicurezza è possibile impostare e gestire i privilegi degli utenti e controllare, quindi, l'accesso e i permessi alle diverse funzionalità e dati all'interno del sistema. |  |
| Gruppi di Persone | Applicazione che permette di raggruppare più utenti al fine di semplificare e ottimizzare la gestione delle attività e delle risorse all'interno dell'applicativo. Con i Gruppi di Persone è possibile assegnare responsabilità specifiche, facilitare la comunicazione e organizzare le risorse umane in modo efficiente per una migliore gestione dei processi aziendali in Maximo |  |
| IBM MAX Manage | È il nome dell'applicativo Maximo, ora inserito come componente della più estesa suite di prodotti denominata MAS: Maximo Application Suite. Quindi, poiché il prefisso Maximo è stato impiegato per tutti i componenti, non potendo riferirsi a IBM Maximo Application Suite Maximo (ossia Maximo ripetuto due volte) al vecchio Maximo è stato dato il nome di Manage (ossia IBM Maximo Application Suite Manage, che diventa IBM MAS Manage). | Nel documento, per brevità verrà utilizzato anche il solo termine Manage. |
| Messaggi | Messaggi di sistema la cui gestione avviene all’interno dell’applicazione Database Configuration. Si tratta di notifiche o avvisi personalizzati che vengono visualizzati dagli utenti durante l'utilizzo del sistema per fornire informazioni specifiche, avvisi o conferme su determinate azioni o eventi. Possono essere di tipo Info, Error e Warning. |  |
| UI | Unità Impianti |  |
| Relazione | Le relazioni consentono di stabilire collegamenti tra più tabelle al fine di recuperare specifici dati mediante l'utilizzo di clausole "WHERE" all'interno di un'istruzione SQL. Questa funzionalità permette di definire una tabella di destinazione, chiamata "Figlio", sulla quale viene eseguita la clausola "WHERE", mentre la tabella di partenza è chiamata "Padre". |  |
| Azione | Le azioni in Maximo sono eventi che possono essere richiamati tramite script, escalation o workflow, e che generano una serie di operazioni o funzionalità specifiche all'interno del sistema. Queste azioni possono includere la creazione, la modifica, l'eliminazione o l'approvazione di record, la gestione delle relazioni tra i dati, l'automazione di processi e altre attività che facilitano e migliorano l'efficienza delle operazioni. |  |
| X3LINEE | Applicazione Custom per la gestione dell’anagrafica Linee (oggetto principale ASSET) |  |
| X3PERSONGR | Applicazione Custom per la gestione dei Gruppi di Persona di Terna (oggetto principale Persongroup) |  |
| SIGOPTION | Configurazioni di sicurezza che definiscono privilegi di accesso e le azioni consentite agli utenti o ai gruppi di utenti all'interno del sistema. Vengono create all’interno dell’Application designer e possono essere applicate a campi o azioni |  |
| X3ROLLUP | Tabella Custom, i cui attributi sono visibili in applicazione Linee, tab “Riepilogo Dati” e richiamabili dallo script X3RIEPILOGO |  |
| GLOBAL RESTRICTION | Regole di sicurezza utilizzate per controllare l'accesso a determinate funzionalità, dati o azioni all'interno del MAS |  |
|  |  |  |
| WORKFLOW | Insieme di processi predefiniti e sequenziali che gestiscono il flusso di lavoro e le attività aziendali all'interno dell'applicativo. Questi processi possono includere l'assegnazione di compiti, l'approvazione di richieste, etc. | Rif. paragrafo 5.2 Azioni |
| ESCALATION | Meccanismo di automazione che permette di gestire il tempo di risposta di determinate azioni e attività | Rif. paragrafo 5.2 Azioni (e.g alla modifica dello stato dell’asset viene inviata una notifica all’utente) |
| Dev | Ambiente Sviluppo (Development) |  |

## Tabelle del DB del Manage

| Tabella | Spiegazione | Nota |
| --- | --- | --- |
| ASSET | Tabella principale dell’MBO Asset. |  |
| ASSETLOCRELATION | Tabella nella quale sono gestite le relazioni che permettono ad un asset di legarsi a molteplici asset di livello superiore. |  |
| ASSETSTATUS | Tabella con cardinalità 1:n rispetto ad ogni asset, dove sono registrati i cambi stato di ogni asset. |  |
| ASSETSPEC | Tabella delle specifiche tecniche degli asset. Viene popolata, con cardinalità 1:n rispetto all’asset, nel momento in cui l’asset è associato ad una classificazione. In questa tabella sono anche salvati i valori di ogni singolo attributo, in relazione a ciascun’asset. |  |
| SERVICEADDRESS | Tabella dove sono registrati tutti gli indirizzi associabili a vari elementi gestiti in Manage, tra cui gli asset. |  |
| X3TRONCHI | Tronchi di ispezione |  |
| X3SEDIINDISP | Codici di indisponibilità |  |
| CLASSSTRUCTURE | Tabella principale di gestione delle gerarchie di modelli di classificazioni, associabili a varie entità del Manage, tra cui gli asset e gli item. |  |
| ASSETATTRIBUTE | Tabella dell’anagrafica di attributi utilizzati nelle classificazioni. |  |
| ITEM | Tabella degli item, utilizzati nel progetto Terna per gestire i componenti, che diventeranno asset di 2°, 3° e 4° livello. |  |
| ITEMSPEC | Tabella delle specifiche tecniche degli item. Viene popolata, con cardinalità 1:n rispetto all’item, nel momento in cui l’item è associato ad una classificazione. |  |
| ITEMSTRUCT | Tabella della struttura di modelli, che una volta istanziati, daranno origine agli asset in gerarchia (ad esempio, applicando la struttura SOSTEGNO-S ad un asset questo assumerà la classificazione, con relativi attributi, del sostegno e creerà tutti gli altri asset, sempre con relative classificazioni e attributi, di 3° e 4° livello, figli e “nipoti” del sostegno. |  |
| X3ROLLUP | Tabella Custom per la gestione dei seguenti attributi: UI, Sostegni, KM equivalenti, Lunghezza Terna, Tot KM Cavo Terrestre, Tot KM Cavo Marino visibili in applicazione Linee, Tab. Riepilogo Dati |  |
| SERVICEADDRESS | Tabella degli indirizzi di servizio. |  |
| CLASSSPEC | Tabella utilizzata per memorizzare le specifiche o le proprietà delle classificazioni, ovvero permette di definire ulteriori dettagli alle classificazioni, quali attributi, domini, etc. |  |

## Documenti citati

I documenti citati sono consultabili all’interno del seguente repository Teams Documenti correlati.

| DOC ID | NOME FILE |
| --- | --- |
| Doc.1 | EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V7.docx |
| Doc.2 | EAM_LLD_GEN_ANA_Oggetti Custom_20230929_V2.0 |
| Doc.3 | xEAM_LLD_GEN_ANA_Domini_20230929_V2.0 |
|  |  |

# Database Configuration

L’applicazione Database Configuration permette di creare e modificare oggetti, attributi e relazioni presenti all’interno del database del MAS.

Nei seguenti paragrafi vengono illustrati gli oggetti Custom creati sul database, gli oggetti Standard su cui sono state apportate delle modifiche (e.g. lunghezza attributo) e creati nuovi attributi (e.g. ASSET.X3BURKS) e gli oggetti standard utilizzati ai fini della rappresentazione dell’anagrafica.

Nel dettaglio, le colonne indicano:

Oggetto: nome univoco della tabella

Attributo: nome univoco del campo presente in tabella

Descrizione: Descrizione Tabella/Attributo

Dominio: Nome del dominio associato al campo (vedi capitolo 3 Domini)

Lunghezza: Lunghezza del campo

Tipo: Tipologia del campo

SameAsAttribute e SameAsObject: SameAsAttribute indica l'attributo principale che controlla il maxtype, la lunghezza e la scala dell’attributo soggetto a modifica. In SameAsObject viene indicata la tabella in cui è presente l'attributo principale. Nel momento in cui viene modificato l'attributo principale, automaticamente le modifiche si rifletteranno su tutti gli attributi che hanno lo stesso SameAsAttribute. Ad esempio, l’attributo Parent di Asset ha come SameAsAttribute e SameAsObject rispettivamente Assetnum e Asset, in automatico eredita tutte le caratteristiche dell’attributo Assetnum.

Persistente: l'attributo e i dati associati ad esso sono mantenuti in modo permanente all'interno del database di Maximo. 1 indica che il campo è persistente e le informazioni vengono salvate a Database.Tipo di ricerca: indica il tipo di ricerca prevista su questo attributo.

## Oggetti Standard e attributi Standard (senza modifiche)

Di seguito, vengono elencati gli oggetti standard e i relativi attributi standard utilizzati per la rappresentazione dell’anagrafica senza necessità di modifiche.

| OGGETTO | DESCR. | ATTRIBUTO | DESCR. | DOMINIO | LUNGHEZZA | TIPO | PERSISTENTE | SAMEASATTRIBUTE | SAMEASOBJECT | TIPO DI RICERCA |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| ASSET | Tabella Degli Asset | STATUS | Stato | LOCASSETSTATUS | 20 | ALN | 1 |  |  | WILDCARD |
| ASSET | Tabella Degli Asset | ASSETNUM | Numero Asset |  | 25 | UPPER | 1 |  |  | WILDCARD |
| ASSET | Tabella Degli Asset | DESCRIPTION | Descrizione |  | 200 | ALN | 1 | DESCRIPTION | ITEM | TEXT |
| ASSET | Tabella Degli Asset | ORGID | Organizzazione |  | 8 | UPPER |  | ORGID | ORGANIZATION | WILDCARD |
| ASSET | Tabella Degli Asset | PARENT | Padre dell’Asset |  | 25 | UPPER |  | ASSETNUM | ASSET | WILDCARD |
| ASSET | Tabella Degli Asset | SITEID | Sito |  | 8 | UPPER | 1 | SITEID | SITE | WILDCARD |
|  |  | CLUSSSTRUCTUREID | Identificativo della classificazione |  | 20 | UPPER | 1 | CLASSSTRUCTUREID | CLASSSTRUCTURE | WILDCARD |
| ASSETLOCRELATION | Asset relazionati | SOURCEASSETNUM | Asset di origine (Padre) |  | 25 | UPPER | 1 | ASSETNUM | ASSET | EXACT |
| ASSETLOCRELATION | Asset relazionati | TARGETASSETNUM | Asset di destinazione (Figli) |  | 25 | UPPER | 1 | ASSETNUM | ASSET | EXACT |
| ASSETSPEC | Valori di un attributo specifici per un Asset | ALNVALUE | Valore Alfanumerico |  | 254 | ALN | 1 | VALUE | ALNDOMAIN | WILDCARD |
| ASSETSPEC | Valori di un attributo specifici per un Asset | ASSETATTRID | Identificativo Attributo |  | 50 | UPPER | 1 | ASSETATTRID | ASSETATTRIBUTE | WILDCARD |
| ASSETSPEC | Valori di un attributo specifici per un Asset | CLASSSTRUCTUREID | Identificativo Classificaizone |  | 20 | UPPER | 1 | CLASSSTRUCTUREID | CLASSSTRUCTURE | EXACT |
| ASSETSPEC | Valori di un attributo specifici per un Asset | TABLEVALUE | Il valore della tabella specificata nel DOMAIN in cui il tipo di dominio e il tipo di dato sono TABLE. |  | 254 | ALN | 1 |  |  | WILDCARD |
| ASSETSPEC | Valori di un attributo specifici per un Asset | NUMVALUE | Valore Numerico |  | 30 | DECIMAL | 1 | VALUE | NUMERICDOMAIN | EXACT |
| ITEM | Tabella degli Item | ITEMNUM | Identificativo Item |  | 30 | UPPER | 1 |  |  | WILDCARD |
| ITEM | Tabella degli Item | DESCRIPTION | Descrizione |  | 200 | ALN |  |  |  | TEXT |
| ITEM | Tabella degli Item | ROTATING | Item rotativo |  | 1 | YORN | 1 |  |  | EXACT |
| CLASSSTRUCTURE | Tabella delle classificazioni | CLASSSTRUCTUREID | Identificativo Classificazione |  | 20 | UPPER | 1 |  |  | WILDCARD |
|  | Tabella delle classificazioni | HIERARCHYPATH | Tabella della gerarchia di classificazione |  | 254 | ALN |  |  |  | NONE |
|  |  |  |  |  |  |  |  |  |  |  |
| CLASSSPEC | Tabella attributi di classificazione | ASSETATTRID | Identificativo Attributo |  | 50 | UPPER | 1 | ASSETATTRIBUTE | ASSETATTRID | WILDCARD |
| CLASSSPEC | Tabella attributi di classificazione | SECTION | Sezione per un gruppo di attributi |  | 10 | UPPER |  |  |  | WILDCARD |
| CLASSSPEC | Tabella attributi di classificazione | DATATYPE | Tipo Dato |  | 50 | UPPER | 1 |  |  | WILDCARD |
| CLASSSPEC | Tabella attributi di classificazione | MEASUREUNITID | Unità di misura |  | 16 | UPPER |  | MEASUREUNIT | MEASUREUNITID | WILDCARD |
| ASSETATTRIBUTE | Tabella degli attributi | ASSETATTRID | Identificativo Attributo |  | 50 | UPPER | 1 | ASSETATTRIBUTE | ASSETATTRID | WILDCARD |
| ASSETATTRIBUTE | Tabella degli attributi | Description | Descrizione |  | 100 | ALN | 1 |  |  | WILDCARD |
| ASSETATTRIBUTE | Tabella degli attributi | DOMAINID | Id Dominio |  | 18 | UPPER | 1 | DOMAINID | MAXDOMAIN | WILDCARD |
| ASSETATTRIBUTE | Tabella degli attributi | DATATYPE | Tipo Dato |  | 8 | UPPER | 1 |  |  | WILDCARD |
| ASSETATTRIBUTE | Tabella degli attributi | MEASUREUNITID | Unità di misura |  | 16 | UPPER | 1 | MEASUREUNIT | MEASUREUNITID |  |

## Oggetti Standard e attributi Custom

In questo paragrafo vengono riportati gli oggetti Standard (tabelle già esistenti su MAS), i relativi attributi creati (indicati con il prefisso “X3” o “X4”), le rispettive configurazioni ed i domini applicati agli attributi (per i domini si veda capitolo Domini).

| OGGETTO | ATTRIBUTO | DOMINIO | LUNG. | TIPO | DESCRIZIONE | SAMEASATTRIBUTE | SAMEASOBJECT | PERSISTENT | TIPO DI RICERCA |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| ASSET | X3BUKRS | X3BUKRS | 4 | ALN | Società |  |  | 1 | WILDCARD |
| ASSET | X3GEWRK | X3GEWRK | 8 | ALN | Centro di lavoro responsabile | GEWRK | X3GEWRK | 1 | WILDCARD |
| ASSET | X3INGRP | X3INGRP | 3 | ALN | Gruppo Responsabile Pianificazione della Manutenzione |  |  | 1 | WILDCARD |
| ASSET | X3KOKRS | X3KOKRS | 4 | ALN | Controlling area |  |  | 1 | WILDCARD |
| ASSET | X3KOSTL |  | 10 | ALN | Centro di Costo |  |  | 1 | WILDCARD |
| ASSET | X3MSGRP |  | 8 | ALN | Impianto |  |  | 1 | WILDCARD |
| ASSET | X3ROOTASSETNUM |  | 25 | UPPER | X3rootassetnum | ASSETNUM | ASSET | 1 | WILDCARD |
| ASSET | X3SAPID |  | 30 | ALN | Codice sede tecnica |  |  | 1 | WILDCARD |
| ASSET | X3SWERK | X3SWERK | 4 | ALN | Divisione di Ubicazione |  |  | 1 | WILDCARD |
| ASSET | X3ZZSOCP | X3ZZSOCP | 4 | ALN | Società Proprietaria |  |  | 1 |  |
| ASSET | X3DADOPPIATERNA |  | 1 | YORN | Da doppia terna |  |  | 1 | EXACT |
| ASSET | X3EQFNR |  | 20 | ALN | Campo Sort |  |  | 1 | WILDCARD |
| ASSET | X3SEQUENCELIST | X3SEQUENCE | 8 | UPPER | Sequence |  |  | 0 | NONE |
| ASSET | X3DADOPPIATERNA |  | 1 | YORN | Gestione doppia Terna |  |  | 1 | EXACT |
| ASSET | X3POSITION | X3POSITION | 4 | UPPER | Position |  |  | 0 | NONE |
| ASSET | X4LOTTOTECNICO |  | 12 | INT | Lotto tecnico omogeneo |  |  | 1 | NONE |
| ASSETATTRIBUTE | X3GRUPPO_RESP | X3GRUPPO_RESP | 8 | UPPER | Gruppo responsabile | PERSONGROUP | PERSONGROUP | 1 | WILDCARD |
| ASSETLOCRELATION | X3POSNR |  | 4 | ALN | Posizione |  |  | 1 | WILDCARD |
| ASSETLOCRELATION | X3SAPID |  | 30 | ALN | codice Sede Tecnica | X3SAPID | ASSET | 1 | WILDCARD |
| ASSETLOCRELATION | X3SEQUENCE |  | 50 | UPPER | Sequenza Linea |  |  | 1 | NONE |
| ASSETLOCRELATION | X3POSITION | X3POSITION | 4 | UPPER | Position |  |  | 0 | NONE |
| ASSETLOCRELATION | X3SEQUENCELIST | X3SEQUENCE | 8 | UPPER | Sequence |  |  | 0 | NONE |
| CLASSSPEC | X3CALCOLI | X3CALCOLI | 5 | ALN | Operazione |  |  | 1 | WILDCARD |
| ITEM | X3DEFAULTCHILD |  | 1 | YORN | Item di default |  |  | 1 | EXACT |
| ITEM | X3PARENTITEM | X3ITEM | 30 | UPPER | Parent Item | ITEMNUM | ITEM | 1 | WILDCARD |
| PERSONGROUP | X3PARENTPERSONGROUP | X3PARENTPERSONGROUP | 8 | UPPER | Gruppo di persone padre | PERSONGROUP | PERSONGROUP | 1 | WILDCARD |
| PERSONGROUP | X4TIPUDB | X4TIPUDB | 10 | UPPER | Tipologie Unità di Budget |  |  | 1 | WILDCARD |
| PERSONGROUP | X4UDB |  | 1 | YORN | Unità di Budget |  |  | 1 | EXACT |

Tabella 1 - Oggetti Standard con Attributi Custom

## Oggetti Standard e attributi Standard (con modifiche)

Nella seguente tabella vengono elencati tutti gli Oggetti Standard e i relativi attributi Standard su cui sono state apportate delle modifiche, in particolare alla lunghezza, ai fini dell’integrazione SAP-EAM.

| OGGETTO (Tabella) | ATTRIBUTO | DOMINIO | LUNGHEZZA | LUNGHEZZA | TIPO | DESCRIZIONE | SAMEASATTRIBUTE | SAMEASOBJECT | TIPO DI RICERCA |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| OGGETTO (Tabella) | ATTRIBUTO | DOMINIO | Da: | A: | TIPO | DESCRIZIONE | SAMEASATTRIBUTE | SAMEASOBJECT | TIPO DI RICERCA |
| ASSET | DESCRIPTION |  | 100 | 200 | ALN | Descrizione dell’asset | DESCRIPTION | ITEM | TEXT |
| ASSETATTRIBUTE | ASSETATTRID |  | 16 | 50 | UPPER | Identificativo attributo dell’asset |  |  | WILDCARD |
| CLASSIFICATION | DESCRIPTION |  | 100 | 150 |  | Descrizione della classificazione |  |  | WILDCARD |
| SERVICEADDRESS | CITY |  | 36 | 40 | BIGINT | Città |  |  | WILDCARD |
| SERVICEADDRESS | COUNTRY | COUNTRYCODES | 3 | 3 | BIGINT | Paese |  |  | WILDCARD |
| SERVICEADDRESS | DESCRIPTION |  | 50 | 40 | BIGINT | Descrizione |  |  | WILDCARD |
| SERVICEADDRESS | POSTALCODE |  | 12 | 10 |  | Codice postale |  |  | WILDCARD |
| SERVICEADDRESS | REGIONDISTRICT |  | 36 | 3 |  | Regione |  |  | WILDCARD |
| SERVICEADDRESS | STREETADDRESS |  | 115 | 60 |  | Indirizzo |  |  | WILDCARD |

Tabella 2 - Oggetti Standard con Attributi Standard modificati

## Oggetti Custom

All’interno del file EAM_LLD_GEN_ANA_Oggetti Custom_20230929_V2.0, consultabile nell’apposito repository Anagrafica > Documenti correlati viene riportato l’elenco degli oggetti Custom (nuove tabelle, identificate con il prefisso “X3” o “X4”), i relativi attributi creati (indicati con il prefisso “X3” o “X4”), le rispettive configurazioni ed i domini applicati agli attributi (per i domini si veda capitolo Domini). Vista la numerosità degli Oggetti (tabelle e attributi), i valori sono stati inseriti all’interno del file Excel sopra indicato che verrà aggiornato all’occorrenza.

## Relazioni

Le relazioni permettono di legare più tabelle al fine di recuperare specifici dati; si tratta di clausole “WHERE" all’interno di un’istruzione SQL. La tabella di destinazione, chiamata "Figlio", è l'oggetto su cui viene eseguita la clausola "WHERE" e contiene i dati correlati che possono essere recuperati tramite la relazione, mentre la tabella di partenza è chiamata "Padre" e detiene la chiave primaria, un identificatore univoco per ogni record, che permette di stabilire il collegamento con altre tabelle (le relazioni sono state principalmente utilizzate per la navigazione e il recupero dei dati nell’applicazione Linee, rif. 2.6 Navigazione Manage Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0).

Nel seguente paragrafo si riportano le relazioni custom create (indicate con il prefisso “X3” o “X4”) all’interno del Database Configuration. Per ogni relazione, viene riportata la clausola SQL, la tabella di partenza (Padre) e la tabella di destinazione (Figlio):

| NOME | DESCRIZIONE | PADRE | FIGLIO | WHERECLAUSE | Rif. |
| --- | --- | --- | --- | --- | --- |
| X3ALNDOMVALSOC | Relazione per estrarre I dati dal dominio X3BURKS e renderli visibili sull’applicazione Linee | ASSET | ALNDOMAIN | domainid='X3BUKRS' and value=:X3BUKRS |  |
| X3ASSETCOM | Relazione per trovare le linee in comune | ASSET | ASSET | classstructureid in (select classstructureid from classstructure where classificationid='LINEA') and siteid='SIT01' and assetnum in (select sourceassetnum from assetlocrelation where siteid=:siteid and sourceassetnum<>:assetnum and targetassetnum in (select targetassetnum from asset a join assetlocrelation on sourceassetnum=:assetnum AND a.siteid=:siteid)) |  |
| X3ASSETLINEA2 | Relazione per estrarre gli asset di 3° livello della Linea | ASSET | ASSET | siteid = :siteid and parent=:assetnum and ( x3rootassetnum is null or x3rootassetnum = :&owner&.sourceassetnum ) |  |
| X3ASSETLOCRELATION_SOURCE | Relazione utilizzata nella dialog “Remove Asset” | ASSET | ASSETLOCRELATION | sourceassetnum=:assetnum and siteid=:siteid |  |
| X3ASSETLOCRELATION_TD_LINEE | Relazione per trovare le linee in doppia terna a cui sono associati gli asset di secondo livello | ASSET | ASSETLOCRELATION | sourceassetnum in (select assetnum from asset where classstructureid in (select classstructureid from classstructure where classificationid = 'LINEA') ) AND targetassetnum IN (SELECT targetassetnum FROM assetlocrelation GROUP BY targetassetnum HAVING count(sourceassetnum)>1) and targetassetnum=:parent |  |
| X3ASSETLOCRELATION_TS | Relazione per trovare tutte le linee che hanno asset di secondo livello che non sono in doppia terna. (utilizzata per lookup di associazione linee) | ASSET | ASSETLOCRELATION | sourceassetnum in (select assetnum from asset where classstructureid in (select classstructureid from classstructure where classificationid = 'LINEA') ) AND targetassetnum IN (SELECT targetassetnum FROM assetlocrelation GROUP BY targetassetnum HAVING count(sourceassetnum)=1) and sourceassetnum != :assetnum |  |
| X3ASSETSPEC_LUN_CM | Relazione che collega la tabella ASSET con ASSETSPEC. In particolare seleziona l'attributo LIN_LUNGHEZZA_CM (Km cavo marino) | ASSET | ASSETSPEC | assetnum = :assetnum and siteid=:siteid and assetattrid='LIN_LUNGHEZZA_CM' |  |
| X3ASSETSPEC_LUN_CT | Relazione che collega la tabella ASSET con ASSETSPEC. In particolare seleziona l'attributo LIN_LUNGHEZZA_CT (Km cavo terrestre) | ASSET | ASSETSPEC | assetnum = :assetnum and siteid=:siteid and assetattrid='LIN_LUNGHEZZA_CT' |  |
| X3CHANGEASSET | Relazione all'oggetto non persistente usato per la creazione e/o sostituzione degli asset di quarto livello. Non è prevista una clausola where | ASSET | X3CHANGEASSET | \\\ |  |
| X3CLASSANCESTOR | Relazione per script X3SAPIDLIVELLO2, permette di recuperare correttamente la classificazione degli asset di 2° livello | ASSET | CLASSANCESTOR | :classstructureid=classstructureid and hierarchylevels='0' |  |
| X3CREATEASSET | Relazione per la dialog custom “Crea Palificazione” | ASSET | X3CREATEASSET |  |  |
| X3INVLINE | Relazione per la dialog custom “Inversione linea” | ASSET | X3INVLINE |  |  |
| X3PERSONGROUP | Relazione che permette di visualizzare i gruppi di persona | ASSET | PERSONGROUP | :X3SWERK = PERSONGROUP |  |
| X3RECODESAPID | Relazione per la dialog custom “Ricodifica SAPID” | ASSET | X3RECODESAPID |  |  |
| X3ROLLUP | Relazione che collega la tabella Asset a X3rollup per permettere di visualizzare correttamente i dati sul tab “Riepilogo Dati” | ASSET | X3ROLLUP | assetnum=:assetnum and siteid=:siteid |  |
| X3TRATTATESATURA | Relazione ASSET-ASSET per prendere tutti gli elementi (conduttori o fdg) presenti nella tratta di tesatura dell'asset di quarto livello corrente | ASSET | ASSET | parent IN ( SELECT assetnum FROM asset WHERE parent IN ( SELECT targetassetnum FROM ( SELECT targetassetnum, sourceassetnum, row_number() OVER (ORDER BY x3sequence) AS seq_num FROM assetlocrelation WHERE x3sequence > ( SELECT max(x3sequence) FROM assetlocrelation WHERE targetassetnum IN ( SELECT ancestor FROM assetancestor WHERE hierarchylevels = '2' AND assetnum IN ( SELECT assetnum FROM assetspec WHERE assetattrid = 'LIN_TIPO_ATTACCO' AND alnvalue='O' ) ) AND x3sequence < ( SELECT x3sequence FROM assetlocrelation WHERE targetassetnum = :parent.parent.assetnum AND ( :classstructureid IN ( SELECT classstructureid FROM classstructure WHERE classificationid='CONDUTTORE') AND sourceassetnum IN ( SELECT x3rootassetnum FROM asset WHERE assetnum = :parent.assetnum ) OR :classstructureid IN ( SELECT classstructureid FROM classstructure WHERE classificationid='FUNE DI GUARDIA')) )) AND x3sequence < ( SELECT min(x3sequence) FROM assetlocrelation WHERE targetassetnum IN ( SELECT ancestor FROM assetancestor WHERE hierarchylevels = '2' AND assetnum IN ( SELECT assetnum FROM assetspec WHERE assetattrid = 'LIN_TIPO_ATTACCO' AND alnvalue='O' ) ) AND x3sequence > ( SELECT x3sequence FROM assetlocrelation WHERE targetassetnum = :parent.parent.assetnum AND (( :classstructureid IN ( SELECT classstructureid FROM classstructure WHERE classificationid='CONDUTTORE')) AND sourceassetnum IN ( SELECT x3rootassetnum FROM asset WHERE assetnum = :parent.assetnum ) OR ( :classstructureid IN ( SELECT classstructureid FROM classstructure WHERE classificationid='FUNE DI GUARDIA'))) ) AND ( sourceassetnum IN ( SELECT x3rootassetnum FROM asset WHERE assetnum = :parent.assetnum ) AND :classstructureid IN ( SELECT classstructureid FROM classstructure WHERE classificationid='CONDUTTORE') OR :classstructureid IN ( SELECT classstructureid FROM classstructure WHERE classificationid='FUNE DI GUARDIA'))) ) WHERE sourceassetnum IN ( SELECT x3rootassetnum FROM asset WHERE assetnum = :parent.assetnum AND :classstructureid IN ( SELECT classstructureid FROM classstructure WHERE classificationid='CONDUTTORE')) OR sourceassetnum IN ( SELECT sourceassetnum FROM assetlocrelation WHERE targetassetnum = :parent.parent.assetnum AND ( :classstructureid IN ( SELECT classstructureid FROM classstructure WHERE classificationid='FUNE DI GUARDIA'))) )) AND classstructureid = :classstructureid |  |
| X3TRONCHI | Relazione che permette di visualizzare sul Tab Tronchi tutte le informazioni relative | ASSET | X3TRONCHI | assetnum = :assetnum and siteid=:siteid |  |
| X3_ASSET_AMMAZZETTATO | Relazione che permette di visualizzare gli asset in doppia terna ammazzettata | ASSET | ASSET | assetnum in (select parent from asset where x3dadoppiaterna = 1 and parent not in (select targetassetnum from assetlocrelation where sourceassetnum=:assetnum)) |  |
| X3ASSETLOCRELATION_TD | Relazione per trovare tutte le linee che hanno asset di secondo livello che sono in doppia terna. (utilizzata per dissociazione linee) | ASSETLOCRELATION | ASSETLOCRELATION | targetassetnum IN (SELECT targetassetnum FROM assetlocrelation GROUP BY targetassetnum HAVING count(sourceassetnum)>1) and targetassetnum =:targetassetnum and sourceassetnum<>:sourceassetnum |  |
| X3ASSETSPEC | Relazione utilizzata per recuperare su asset le informazioni degli attributi | X3ROLLUP | ASSETSPEC | assetnum=:assetnum and siteid=:siteid |  |
| X3DIVIDICAMPATA | Relazione per la dialog custom “Bonifica campate doppie” | ASSET | X3DIVIDICAMPATA |  | Rif. Doc. 1 5.5 OP_254 - Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate) |
| X3UNISCISOSTEGNO | Relazione per la dialog custom “Bonifica sostegni singoli” | ASSET | X3UNISCISOSTEGNO |  | Rif. Doc. 1 5.5 OP_254 - Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate) |
| X4TABPAROPINT | Relazione utilizzata per visualizzare la tabella parametrica di assegnazione criticità alle opere interferenti | ASSET | X4TABPAROPINT | 1=1 |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |
|  |  |  |  |  |  |

Tabella 3 - Relazioni Custom

Di seguito vengono riportate delle relazioni standard presenti in Maximo e utilizzate nell’Application Designer per garantire il corretto recupero dei dati:

| NOME | DESCRIZIONE | PADRE | FIGLIO | WHERECLAUSE |
| --- | --- | --- | --- | --- |
| TARGETASSET | Serve ad estrarre gli Asset di 2° sull’applicazione Linee | ASSETLOCRELATION | ASSET | assetnum=:targetassetnum and siteid=:siteid |
| CHILDREN | Serve ad estrarre gli Asset di 4° livello sull’applicazione Linee | ASSET | ASSET | parent = :assetnum and siteid=:siteid |

Tabella 4 - Relazioni Standard

## Messaggi

All’interno dell’applicazione Database Configuration è possibile visualizzare e creare messaggi di sistema, vale a dire notifiche o avvisi personalizzati che vengono visualizzati dagli utenti durante l'utilizzo del sistema per fornire informazioni specifiche, avvisi o conferme su determinate azioni o eventi.

Esistono 3 tipi di messaggi:

Info, utilizzato per fornire informazioni agli utenti senza richiedere alcuna azione specifica. È un messaggio puramente informativo che mostra dettagli o notifiche importanti agli utenti senza interrompere il flusso di lavoro.

Warning, utilizzato per indicare agli utenti una situazione rilevante o critica. Può richiedere un'azione da parte dell'utente, ma non si tratta di un errore grave che impedisce l'avanzamento delle operazioni.

Error, utilizzato per segnalare un errore o una situazione errata che impedisce l'esecuzione dell'azione corrente. È necessaria un’azione da parte dell’utente per procedere.

Di seguito, i messaggi di tipo Error presenti su MAS e richiamabili dagli script indicati nella tabella sottostante (rif. 5.1 Automation Scripts); message Group indica il gruppo di messaggi, message Key è l’iD univoco del messaggio, message id indica il formato (se si tratta di un messaggio personalizzato avremo BMX[CC][NNNN][T] dove [CC] è un codice di due lettere che identifica la personalizzazione, [NNNN] è un numero di quattro cifre e [T] è il tipo di messaggio e può essere E (Errore), W (avviso) o I (Info)),il valore è testo che verrà visualizzato dall’utente.

| Message Group | Message Key | Value | Message ID | Script | Scenario |
| --- | --- | --- | --- | --- | --- |
| x3err_format | x3err_format | Errore formato. Il campo Disposizione Fasi deve avere il seguente formato:A-n,n,n,n,n,n | BMXZZ7131E | X3DISPFASI | Gestione della disposizione Fasi- messaggio di errore sul formato dell’attributo LIN_DISPOSIZIONE_FASI |
| x3err_format_liv1 | x3err_format_liv1 | Errore formato livello 1 | BMXZZ7132E | X3SAPIDLINEA | Codifica del SAPID- messaggio di errore sul formato del SAPID di 1° Livello SAP |
| x3err_format_liv2 | x3err_format_liv2 | Errore formato livello 2 | BMXZZ7133E | X3SAPIDLIVELLO2 | Codifica del SAPID- messaggio di errore sul formato del SAPID di 2° Livello SAP |
|  |  |  |  |  |  |
| x3err_lenght | x3err_lenght | Lunghezza errata | BMXZZ7136E | X3DISPFASI | Gestione della disposizione Fasi- messaggio di errore sulla lunghezza dell’attributo LIN_DISPOSIZIONE_FASI |
| x3err_lenght_liv2 | x3err_lenght_liv2 | Errore lunghezza livello 2 | BMXZZ7137E | X3SAPIDLIVELLO2 | Codifica del SAPID- messaggio di errore sulla lunghezza del SAPID di 2° Livello SAP |
|  |  |  |  |  |  |
| x3err_length_liv1 | x3err_length_liv1 | Errore lunghezza Livello 1 | BMXZZ7140E | X3SAPIDLINEA | Codifica del SAPID- messaggio di errore sulla lunghezza del SAPID di 1° Livello SAP |
| x3err_maiuscole_liv1 | x3err_maiuscole_liv1 | Errore maiuscola | BMXZZ7141E | X3SAPIDLINEA | Codifica del SAPID- messaggio che compare quando viene inserito un carattere in Minuscolo nel SAPID di 1° livello |
| x3error_livello2 | x3error_livello2 | Per proseguire è necessario compilare il campo come segue: Sap Id livello 1 nel formato NNXXXCN (7 Digit) - Sap Id livello 2 nel formato NNNXXXXX (<8 Digit) | BMXZZ7129E | X3SAPIDLIVELLO2 | Codifica del SAPID- Messaggio di errore che informa l’utente del corretto formato del SAPID sul 2° livello, ovvero vi deve essere una concatenazione tra il 1° del SAPID e 2° Livello |
|  |  |  |  |  |  |
| x3error_tronchi | x3error_tronchi | Formato SAPID errato | BMXZZ7142E | X3SAPIDVALIDATION | Codifica del SAPID- sui tronchi. Se viene inserito un formato errato si visualizza a video il messaggio di errore |
| GenericYesNo | x3 | {0} | BMXZZ7144E |  | Messaggio generico di errore previsto su EAM |

Tabella 5 -Messaggi

# Domini

Nel MAS con il termine dominio si intende l’elenco di valori che permette di valorizzare determinati campi presenti nelle diverse applicazioni (rif. paragrafo 2.2.1 Domini legati a campi di testata Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0).

I domini sono divisi in 4 diverse tipologie, ovvero alfanumerico, numerico, di tipo tabella e synonym, come di seguito:

Dominio alfanumerico: Elenco di valori statico che può gestire tre tipi di dati alfanumerici: ALN, UPPER e LOWER (e.g. Dominio X3DOMALN12 che contiene all’interno i valori A – Alluminio, R-Rame). La creazione di un nuovo dominio di questo tipo comporta il popolamento di una struttura dati già predisposta nel MAS, chiamata ALNDOMAIN che gestisce:

Il nome del dominio

Il valore del dominio

La descrizione del valore del dominio

Dominio di tipo tabella: Permette di prendere dati da qualsiasi oggetto del MAS, sia standard, come ad esempio PERSONGROUP che custom, come X3GEWRK. La lista dei valori che sarà visibile a sistema viene definita tramite una condizione SQL, così come la validazione del dato inserito nel campo di destinazione del dominio.

Dominio numerico: Elenco statico di valori numerici.

Dominio Synonym: Domini speciali di sistema utilizzati principalmente sui campi “Stato” e “Tipo”. Questi domini sono formati da valori interni che non possono essere modificati ma a cui può essere legato un sinonimo. Ad esempio, il dominio LOCASSETSTATUS (dominio applicato al campo stato Asset) presenta valore interno DECOMMISSIONED e sinonimo CEAL (stato finale che vedrà l’utente)

Nei paragrafi successivi verranno illustrate e dettagliate tutte le tipologie di Domini creati su MAS.

La lista completa dei domini (in particolare domini NUM, domini ALN e domini TABLE), vista la numerosità degli oggetti, è consultabile nel file Domini presente all’interno del repository Documenti Correlati. Il file è come di seguito strutturato:Copertina

Introduzione: Overview sui domini e sulla struttura del file

Sheet Dominio NUM: elenco dei domini di tipo numerico

Sheet Dominio Al: elenco dei domini di tipo alfanumerico

Sheet Dominio Table: elecono dei domini di tipo tabella

Sheet Dominio_Attributo Class: per ogni attributo di classificazione indica il relativo dominio associato

Sheet Domini_Attributi DB: inidica gli attributi di database in cui sono presenti i Domini

## Domini NUM

All’interno del file Excel EAM_LLD_GEN_ANA_Domini_20230929_V2.0(sheet Dominio Num) nel repository Documenti Correlati, è consultabile l’elenco dei domini di tipo numerico creati in ambiente MAS DEV di Terna,con relativo codice identificativo (Dominio), descrizione, valori presenti all’interno del dominio (colonne Valori Dominio- Descrizione Dominio), tipologia del dominio e lunghezza.

Questi domini sono stati applicati agli attributi di classificazione come indicato nel Doc.2 Domini (sheet Dominio_Attributo Class. e Domini_Attributi DB):

## Domini ALN

All’interno del file Excel EAM_LLD_GEN_ANA_Domini_20230929_V2.0 (sheet Dominio Aln), nel repository Documenti Correlati, vengono elencati i domini di tipo alfanumerico creati sull’ambiente MAS DEV di Terna, con relativo codice identificativo (Dominio), descrizione, valori presenti all’interno del dominio, tipologia e lunghezza (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funz.ionalità Applicative Anagrafica Linee_20230713_V3.0, paragrafo 2.1.2 La gestione dei domini).

All’interno degli sheet Dominio_Attributo Class. e Domini_Attributi DB sono indicati gli attributi di classificazione o DB che utilizzano i domini elencati.

## Domini di tipo tabella

All’interno del file Excel EAM_LLD_GEN_ANA_Domini_20230929_V2.0 (sheet Dominio Table), nel repository Documenti Correlati, vengono elencati i domini di tipo tabella creati su MAS DEV di Terna, con relativo codice identificativo (Dominio), descrizione, validazione e clausola where.

All’interno degli sheet Dominio_Attributo Class. e Domini_Attributi DB sono indicati gli attributi di classificazione o DB che utilizzano i domini elencati.

## Domini Synonym

Nel contesto Terna si utilizza la tipologia di dominio Synonym (ad ogni valore standard dello stato possono essere aggiunti più sinonimi/alias) esclusivamente per gestire la lista degli stati disponibili per Asset. Per la gestione degli stati viene utilizzato il dominio standard LOCASSETSTATUS.

Nel seguente paragrafo viene riportata la lista degli stati disponibili per gli Asset secondo quanto espresso nel Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0 paragrafo 3.8.5 Gestione degli stati.

In tabella viene indicato il valore interno MAS, il sinonimo assegnato (valore), la descrizione, se si tratta di uno stato di default e la condizione associata che regola la visibilità a sistema.

| Valore Interno | Valore | Descrizione | Default | Condizione |
| --- | --- | --- | --- | --- |
| DECOMMISSIONED | CEAL | Ceduta / Alienata | Y | \ |
| DECOMMISSIONED | DELE | Cancellato | N | \ |
| DECOMMISSIONED | DEMO | Demolita / Dismessa | N | \ |
| DECOMMISSIONED | DIST | Distrutto /Alienato | N | X3GESTSTATS |
| NOT READY | DATT | Da Attivare | Y | \ |
| NOT READY | INAT | Inattivo | N | \ |
| OPERATING | ATTV | Attiva | Y | X3GSTATS3 / X3STATL12S1 |
| OPERATING | EFDI | Installato, efficente, disponibile | N | X3GESTSTATS |
| OPERATING | EFFE | Installato, efficente futuro esercizio | N | X3GESTSTATLS |
| OPERATING | EFPR | Installato, efficente prenotato | N | X3GESTSTATS |
| OPERATING | EFSC | Installato, efficente scorta | N | X3GESTSTATS |
| OPERATING | ESER | Installato, in esercizio | N | X3GESTSTATLS |
| OPERATING | GUAS | Installato, guasto | N | X3GESTSTATLS |
| OPERATING | NADI | Non attiva, disponibile | N | X3GSTATS3 / X3STATL12S1 |
| OPERATING | NAND | Non Attiva, non disponibile | N | X3GSTATS3 / X3STATL12S1 |
| OPERATING | NIED | Non Installato Efficiente Disponibile | N | X3GESTSTATS |
| OPERATING | NIEO | Non installato, efficente obsoleto | N | X3GESTSTATS |
| OPERATING | NIEP | Non installato, efficente prenotato | N | X3GESTSTATS |
| OPERATING | NIES | Non installato, efficente scorta | N | X3GESTSTATS |

Tabella 6 - Domini Synonym

# Application Designer

Nel Manage, l’Application Designer è uno strumento che consente agli utenti di personalizzare e configurare l'aspetto e il comportamento delle applicazioni. È un componente chiave del framework di personalizzazione e fornisce un'interfaccia grafica che permette, ad esempio, di aggiungere nuovi campi, tabelle, liste valori, condizioni sui campi, etc.

In particolare, per soddisfare quanto espresso nel Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0, sono state create due applicazioni custom:

Applicazione “Linee”

Applicazione “Gruppi di persone Terna”.

## Linee

L’applicazione Custom “Linee” è un duplicato dell’applicazione “Asset” (applicazione Standard per la gestione degli Asset).

Nell’applicazione Linee sarà possibile gestire l’anagrafica degli asset di Terna (rif.Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0 paragrafo 2.6 Navigazione dei dati delle linee in Manage)

Di seguito, la schermata principale dell’applicazione “Linee” in cui è possibile visualizzare ad elenco tutte le Linee disponibili a sistema:

Figura 1 - Schermata “Linee”

La schermata presenta in ordine i seguenti campi:

Asset: numero identificativo univoco di un asset. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM tabella ASSET (ASSET.ASSETNUM)

SAP ID: codice sede tecnica SAP dell’asset. Tale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET (ASSET.X3SAPID)

Description: descrizione dell’asset. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET (ASSET.DESCRIPTION)

Status: stato dell’asset. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET (STATUS.ASSET)

Site: sito di appartenenza dell’asset. Tale campo richiama l’attributo Standard di tipo alfanumerico SITEID della tabella ASSET (SITEID.ASSET)

Una volta selezionato un record da elenco, l’applicazione sarà caratterizzata da 6 tab, descritti nei paragrafi successivi.

### Tab “Linea”

Di seguito, il dettaglio del Tab “Linea” suddiviso per livelli (rif. Doc.1 Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0 paragrafo 2.6.1 Linea).

#### Primo livello asset

Il tab “Linea” è caratterizzato da 4 sezioni principali, una per ogni livello dell’asset Linea. Nella prima sezione (Figura 2) vengono riportate le informazioni dell’asset Linea (1° livello), di seguito descritte (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3

.0 paragrafo 2.6.1 Linea):

Linea: numero identificativo univoco della linea. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET (ASSET.ASSETNUM)

SAP ID: codice sede tecnica SAP della linea. Tale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET (ASSET.X3SAPID)

Status: stato della linea. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET (ASSET.STATUS)

Divisione di Ubicazione: codice del Distretto/Dipartimento di appartenenza della linea (es: 2601). Tale campo richiama l’attributo Custom di tipo alfanumerico X3SWERK della tabella ASSET (ASSET.X3SWERK).

Unità impianti Prevalente: contiene la lista delle unità impianti principali presenti sugli asset di 2° Livello. L’utente può selezionare una UI dall’elenco. Tale campo richiama l’attributo Standard ASSET.X3INGRP.

Controlling Area: Identificativo Controlling Area, ovvero TR01 Terna Italia. Tale campo richiama l’attributo Custom ASSET.X3KOKRS.

Centro di Costo: codice del centro di costo. Tale campo richiama l’attributo Custom ASSET.X3KOSTL.

Società Proprietaria: identificativo della società proprietaria. Tale campo richiama l’attributo Custom ASSET.X3ZZSOCP.

Impianto: identificativo dell’impianto. Tale campo richiama l’attributo Custom ASSET.X3MSGRP.

Campo Sort: Campo a testo libero. Tale campo richiama l’attributo Custom ASSET.X3EQFNR.

Società: Indica la Società. Tale campo richiama l’attributo Custom ASSET.X3BUKRS.

Accanto ai campi “Linea” e “Divisione di Ubicazione” è presente un ulteriore campo descrittivo, di seguito i dettagli:

Descrizione “Linea”: descrizione della linea. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET (ASSET.DESCRIPTION)

Descrizione “Divisione di Ubicazione”: descrizione del Dipartimento/Distretto (es: Dipartimento Trasm. Nord Ovest). Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella PERSONGROUP richiamabile tramite relazione X3PERSONGROUP (X3PERSONGROUP.DESCRIPTION).

Figura 2 - Schermata “Primo Livello Linea”

Di seguito una tabella riepilogativa con le properties dei vari campi:

| Campo | Default Label | Label | Attributo | Attributo parte 2 di tipo Descrizione |
| --- | --- | --- | --- | --- |
| Linea | Asset | Linea | ASSETNUM | DESCRIPTION |
| SAP ID | SAP ID |  | X3SAPID |  |
| Status | Status |  | STATUS |  |
| Divisione di Ubicazione | Divisione di Ubicazione |  | X3SWERK | X3PERSONGROUP.DESCRIPTION |
| Unità impianti Prevalente | Unità impianti Prevalente |  | X3INGRP |  |
| Controlling Area | Controlling Area |  | X3KOKRS |  |
| Centro di Costo | Centro di Costo |  | X3KOSTL |  |
| Società | Società |  | X3BUKRS |  |
| Società Proprietaria | Società Proprietaria |  | X3ZZSOCP |  |
| Impianto | Impianto |  | X3MSGRP |  |
| Campo Sort | Campo Sort |  | X3EQFNR |  |

Tabella 7 - Campi e Proprietà “Primo Livello Linea”

#### Secondo livello linea

La seconda sezione “Asset di secondo livello” presenta la relazione ASSETLOCRELATION (relazione che collega la tabella ASSET alla tabella ASSETLOCRELATION) poiché gli asset di secondo livello vengono richiamati tramite la tabella ASSETLOCRELATION e non sulla tabella ASSET. (rif. Doc. 1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0 paragrafo 2.6.1.1).

Le proprietà conferite a questa sezione sono riportate nella tabella sottostante:

| Proprietà | Dettaglio |
| --- | --- |
| Ordinato per | X3SEQUENCE |
| Data Source ID | 2level |
| Parent Data Source ID | MAINRECORD |
| Relazione | ASSETLOCRELATION |
| Titolo Attributi | X3SAPID |

Tabella 8 - Proprietà Sezione “Asset di Secondo Livello”

Di seguito una descrizione delle informazioni presenti in questa sezione, relative agli asset di secondo livello (Figura 3)

Seq. Linea: sequenza, nell’ambito di una linea, con cui si susseguono gli elementi di secondo livello. Tale campo richiama l’attributo Custom di X3SEQUENCE della tabella ASSETLOCRELATION (ASSETLOCRELATION.X3SEQUENCE)

ID SAP: codice sede tecnica SAP dell’asset di secondo livello. Tale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSETLOCRELATION (ASSETLOCRELATION.X3SAPID)

POSNR: sequenza di campate e tratte cavo (campo necessario per il connettore, vedi paragrafo 2.3 DOC.1). Tale campo richiama l’attributo Custom X3POSNR della tabella ASSETLOCRELATION (ASSETLOCRELATION.X3POSNR)

Asset: numero identificativo univoco dell’asset di secondo livello. Tale campo richiama l’attributo Standard di tipo alfanumerico TARGETASSETNUM della tabella ASSETLOCRELATION (ASSETLOCRELATION.TARGETASSETNUM)

Description: descrizione dell’asset di secondo livello. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET tramite relazione TARGETASSET che porta dalla tabella ASSETLOCRELATION alla tabella ASSET (TARGETASSET.DESCRIPTION)

Status: stato dell’asset di secondo livello. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSETL tramite la relazione TARGETASSET che porta dalla tabella ASSETLOCRELATION alla tabella ASSET (TARGETASSET.STATUS)

Unità Impianti: Unità Impianti di appartenenza dell’asset di secondo livello. Tale campo richiama l’attributo Custom X3INGRP della tabella ASSET tramite la relazione TARGETASSET che porta dalla tabella ASSETLOCRELATION alla tabella ASSET (TARGETASSET.X3INGRP)

Centro di lavoro responsabile: Centro di lavoro responsabile dell’asset di secondo livello. Tale campo richiama l’attributo Custom di tipo alfanumerico richiama l’attributo X3GEWRK della tabella ASSET tramite la relazione TARGETASSET che porta dalla tabella ASSETLOCRELATION alla tabella ASSET (TARGETASSET.X3GEWRK)

Società Proprietaria: identificativo della società proprietaria. Tale campo richiama l’attributo Custom di tipo alfanumerico X3ZZSOCP della tabella ASSET tramite la relazione TARGETASSET che porta dalla tabella ASSETLOCRELATION alla tabella ASSET (TARGETASSET.X3ZZSOCP).

Figura 3 - Sezione “Asset di Secondo Livello”

Di seguito una tabella riepilogativa con le proprietà dei diversi campi presenti in maschera:

| Campo | Default Label | Label | Attributo | Attributo parte 2 di tipo Descrizione |
| --- | --- | --- | --- | --- |
| Seq. Linea | Sequenza linea | Seq. Linea | X3SEQUENCE |  |
| ID SAP | SAP ID | ID SAP | X3SAPID |  |
| POSNR | Sequenza | POSNR | X3POSNR |  |
| Asset | Target Asset | Asset | TARGETASSETNUM |  |
| Descrizione | Descrizione |  | TARGETASSET.DESCRIPTION |  |
| Stato | Stato |  | TARGETASSET.STATUS |  |
| Unità Impianti | UI | Unità Impianti | TARGETASSET.X3INGRP |  |
| Centro di lavoro responsabile | Centro di lavoro responsabile |  | TARGETASSET.X3GEWRK |  |
| Società proprietaria | Società proprietaria |  | TARGETASSET.X3ZZSOCP |  |

Tabella 9 - Campi e Proprietà “Asset di Secondo Livello”

Una volta selezionato un asset di secondo livello, compare a video la seguente schermata:

Figura 4 - Sezione “Secondo Livello Linea”

Cliccando sulla freccia cerchiata in rosso in Figura 4 verrà restituita la seguente schermata, comprensiva di tutte le specifiche tecniche dell’asset di secondo livello:

Figura 5 - Sottosezione “Specifiche Tecniche” (Secondo Livello)

Di seguito le proprietà relative alla sottosezione “Specifiche tecniche”:

| Proprietà | Dettaglio |
| --- | --- |
| Ordinato per |  |
| Data Source ID (id univoco sezione di riferimento) | 16838986218552l |
| Relazione | ASSETSPEC |

Tabella 10 - Proprietà Sottosezione “Specifiche Tecniche” (Secondo Livello)

I campi in Figura 5 presentano le seguenti proprietà, in particolare la colonna Lookup permette all'utente di selezionare valori da un elenco predefinito quando compilano campi, rendendo quindi visibile a video il dominio associato:

| CAMPO | DEFAULT LABEL | LABEL | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP |
| --- | --- | --- | --- | --- | --- |
| Asset Info | Asset | Asset Info | ASSETNUM | DESCRIPTION |  |
| Attributo | Attributo |  | ASSETATTRID |  |  |
| Descrizione | Descrizione |  | ASSETATTRIBUTE.DESCRIPTION |  |  |
| Valore data | Valore Data |  | DATEVALUE |  | DATELOOKUP |
| Valore alfanumerico | Valore alfanumerico |  | ALNVALUE |  | ALNDOMAIN |
| Valore numerico | Valore numerico |  | NUMVALUE |  | NUMERICDOMAIN |
| Unità di misura | Unità di misura |  | MEASUREUNITID |  | MEASUREUNIT |
| Valore alfanumerico | Valore Tabella |  | TABLEVALUE |  |  |

Tabella 11 - Campi e Proprietà “Specifiche Tecniche” (Secondo Livello)

#### Terzo livello linea

Dalla sezione “Asset di Secondo Livello” è possibile navigare la gerarchia degli Asset e quindi visualizzare gli elementi di terzo livello (rif. Doc. 1 paragrafo 2.6.1.2 Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0), selezionando un asset di secondo livello (tramite la freccia cerchiata in rosso nella Figura 6). In questo paragrafo viene descritta la schermata relativa al terzo livello della linea, rappresentato in Figura 6.

Figura 6 - Sezione “Terzo Livello Linea”

La sezione “Terzo livello linea” presenta le seguenti proprietà:

| Label | Terzo livello linea |
| --- | --- |
| Ordinato per | Assetnum |
| Data Source ID | 3levelX |
| Parent Data Source ID | 2level |
| Relazione | TARGETASSET.X3ASSETLINEA2 |
| Titoli Attributi | TARGETASSET.X3SAPID |

Tabella 12 - Proprietà Sezione “Terzo Livello Linea”

Di seguito una descrizione delle informazioni presenti in questa sezione, relative agli asset aggregatori di terzo livello:

Asset: numero identificativo univoco dell’asset di terzo livello. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET (ASSET.ASSETNUM)

Descrizione: descrizione dell’asset di terzo livello. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET (ASSET.DESCRIPTION)

Classificazione: classificazione dell’asset di terzo livello. Tale campo richiama l’attributo standard CLASSSTRUCTURE.CLASSIFICATIONID

Unità Impianti: unità impianti dell’asset. Tale campo richiama l’attributo custom X3INGRP

Società Proprietaria: identificativo della società proprietaria. Tale campo richiama l’attributo Custom ASSET.X3ZZSOCP.

Di seguito una tabella riepilogativa con le properties dei vari campi:

| CAMPO | DEFAULT LABEL | LABEL | ATTRIBUTO |
| --- | --- | --- | --- |
| Asset | Asset |  | ASSETNUM |
| Classificazione | Classificazione |  | CLASSIFICATIONID |
| Unità Impianti | Unità Impianti |  | X3INGRP |
| Descrizione | Descrizione |  | DESCRIPTION |
| Società proprietaria | Società proprietaria |  | X3ZZSOCP |

Tabella 13 - Campi e Proprietà “Terzo Livello Linea”

Cliccando sulla freccia cerchiata in rosso in Figura 6 verrà restituita la seguente schermata, comprensiva di tutte le specifiche tecniche dell’asset di terzo livello:

Figura 7 - Sottosezione “Specifiche Tecniche” (Terzo Livello)

Di seguito le proprietà relative alla sottosezione “Specifiche tecniche”:

| Label | Specifiche tecniche |
| --- | --- |
| Ordinato per |  |
| Data Source ID | 1683898621855 |
| Parent Data Source ID | 3levelX |
| Relazione | ASSETSPEC |

Tabella 14 - Proprietà Sottosezione “Specifiche Tecniche” (Terzo Livello)

I campi in Figura 7 presentano le seguenti proprietà:

| CAMPO | DEFAULT LABEL | LABEL | ATTRIBUTO | ATTRIBUTO PER LA PARTE 2 | LOOKUP |
| --- | --- | --- | --- | --- | --- |
| Asset Info | Asset | Asset Info | ASSETNUM | DESCRIPTION |  |
| Attributo | Attributo |  | ASSETATTRID |  |  |
| Descrizione | Descrizione |  | ASSETATTRIBUTE.DESCRIPTION |  |  |
| Valore data | Valore Data |  | DATEVALUE |  | DATELOOKUP |
| Valore alfanumerico | Valore alfanumerico |  | ALNVALUE |  | ALNDOMAIN |
| Valore numerico | Valore numerico |  | NUMVALUE |  | NUMERICDOMAIN |
| Unità di misura | Unità di misura |  | MEASUREUNITID |  | MEASUREUNIT |
| Valore alfanumerico | Valore Tabella |  | TABLEVALUE |  |  |

Tabella 15 - Campi e Proprietà “Specifiche Tecniche” (Terzo Livello)

#### Quarto livello linea

Una volta selezionato un asset di terzo livello dalla sezione “Terzo livello linea” appaiono in schermata tutti gli asset di quarto livello gerarchicamente sottostanti al terzo livello selezionato (rif. Doc. 1 paragrafo 2.6.1.3), come mostrato in Figura 8.

Figura 8 - Sezione “Quarto Livello Linea”

La sezione “Quarto livello linea” presenta le seguenti proprietà:

| Label | Quarto livello linea |
| --- | --- |
| Ordinato per |  |
| Data Source ID | 4levelZ |
| Parent Data Source ID | 3levelX |
| Relazione | CHILDREN |
| Titolo Attributi | X3SAPID |

Tabella 16 - Proprietà Sezione “Quarto Livello Linea”

Di seguito una descrizione delle informazioni presenti in questa sezione, relative agli asset di 4° Livello:

Asset: numero identificativo univoco dell’asset di quarto livello. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET (ASSET.ASSETNUM)

Description: descrizione dell’asset di quarto livello. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET (ASSET.DESCRIPTION)

Status: stato dell’asset di quarto livello. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET (ASSET.STATUS)

Classificazione: classificazione dell’asset di terzo livello. Tale campo richiama l’attributo standard CLASSSTRUCTURE.CLASSIFICATIONID

Unità Impianti: unità impianti dell’asset. Tale campo richiama l’attributo custom X3INGRP

Società Proprietaria: identificativo della società proprietaria. Tale campo richiama l’attributo Custom ASSET.X3ZZSOCP.

Di seguito una tabella riepilogativa con le properties dei vari campi:

| CAMPO | DEFAULT LABEL | LABEL | ATTRIBUTO |
| --- | --- | --- | --- |
| Asset | Asset |  | ASSETNUM |
| ID SAP | Description |  | DESCRIPTION |
| Status | Status |  | STATUS |
| Unità Impianti | Unità Impianti |  | X3INGRP |
| Descrizione | Descrizione |  | DESCRIPTION |
| Società proprietaria | Società proprietaria |  | X3ZZSOCP |

Tabella 17 - Campi e Proprietà “Quarto Livello Linea”

Cliccando sulla freccia cerchiata in rosso in Figura 8 verrà restituita la seguente schermata, comprensiva di tutte le specifiche tecniche dell’asset di quarto livello:

Figura 9 - Sottosezione “Specifiche Tecniche” (Quarto Livello)

Di seguito le proprietà relative alla sottosezione “Specifiche tecniche”:

| Label | Specifiche tecniche |
| --- | --- |
| Ordinato per |  |
| Data Source ID | 1683898621855A |
| Parent Data Source ID | 4levelZ |
| Relazione | ASSETSPEC |

Tabella 18 - Proprietà Sottosezione “Specifiche Tecniche” (Quarto Livello)

I campi in Figura 9 presentano le seguenti proprietà:

| CAMPO | DEFAULT LABEL | LABEL | ATTRIBUTO | ATTRIBUTO PER LA PARTE 2 | LOOKUP |
| --- | --- | --- | --- | --- | --- |
| Asset Info | Asset | Asset Info | ASSETNUM | DESCRIPTION |  |
| Attributo | Attributo |  | ASSETATTRID |  |  |
| Descrizione | Descrizione |  | ASSETATTRIBUTE.DESCRIPTION |  |  |
| Valore data | Valore Data |  | DATEVALUE |  | DATELOOKUP |
| Valore alfanumerico | Valore alfanumerico |  | ALNVALUE |  | ALNDOMAIN |
| Valore numerico | Valore numerico |  | NUMVALUE |  | NUMERICDOMAIN |
| Unità di misura | Unità di misura |  | MEASUREUNITID |  | MEASUREUNIT |

Tabella 19 - Campi e Proprietà “Specifiche Tecniche” (Quarto Livello)

### Tab “Riepilogo dati”

All’interno del Tab “Riepilogo dati” sono riportate le sommatorie, raggruppate per ciascuna unità impianti, dei seguenti dati della linea selezionata (rif. Doc.1 Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0 paragrafi 2.6.2 e 3.11.4):

Il totale dei sostegni: conteggio degli asset di Tipo Sostegno per ogni UI presente sull’asset di 2° Livello;

Il totale dei Km equivalenti: Somma delle lunghezze di campate aeree e tratte cavo (attributo LIN_LUNGHEZZA) per ogni UI presente sull’asset di 2° Livello, moltiplicate per un fattore k funzione di LIN_LIVELLO_TENSIONE_ESERCIZIO. Il fattore k rappresenta un valore fisso indicato dal BSN, di seguito la tabellina contente i valori per livello di tensione:

| Livello di Tensione | Fattore k |
| --- | --- |
| 380 kV ca | 0,93 |
| 220 kV ca | 1,00 |
| ≤150 kV ca | 1,18 |
| * kV cc (HVDC) | 1,00 |

Tabella 20 – Livello di Tensione

La lunghezza della terna: Somma delle lunghezze (attributo LIN_LUNGHEZZA di campate aeree e tratte cavo (prive di una campata di riferimento, quindi con LIN_CAMPATA_RIFERIMENTO = null (attributo presente sul Conduttore)) per ogni UI presente sull’asset di 2° Livello.

I seguenti sono invece calcolati sull’intera linea, indipendentemente dall’unità impianti:

Il totale dei Km di cavo terrestre: Somma delle lunghezze di tratte cavo (attributo LIN_LUNGHEZZA) aventi LIN_TIPO_POSA <> Sottomarino (presente su 3° Livello - LINEA / TRATTA CAVO / CAVI);

Il totale dei Km di cavo marino: Somma delle lunghezze di tratte cavo (attributo LIN_LUNGHEZZA) aventi LIN_TIPO_POSA = Sottomarino (presente su 3° Livello - LINEA / TRATTA CAVO / CAVI).

Figura 10 - Schermata “Riepilogo Dati”

La schermata presenta i seguenti campi di testata:

Linea: numero identificativo univoco della linea. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET (ASSET.ASSETNUM)

SAP ID: codice sede tecnica SAP della linea. Tale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET (ASSET.X3SAPID)

Status: stato della linea. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET (ASSET.STATUS)

Tot KM Cavo Terrestre: totale km del cavo terrestre. Tale campo richiama l’attributo Standard di tipo numerico NUMVALUE della tabella ASSETSPEC (ASSETSPEC.NUMVALE) tramite relazione Custom X3ASSETSPEC_LUN_CT.

Tot KM Cavo Marino: totale km del cavo Marino. Tale campo richiama l’attributo Standard di tipo numerico NUMVALUE della tabella ASSETSPEC (ASSETSPEC.NUMVALE) tramite relazione Custom X3ASSETSPEC_LUN_CM.

Di seguito, i campi presenti nella seconda sezione Riepilogo Dati, richiamabili tramite relazione Custom X3ROLLUP:

UI: Unità impianti. Tale campo richiama l’attributo Custum di tipo alfanumerico X3INGRP della tabella X3ROLLUP (X3ROLLUP. INGRP)

Sostegni: numero sostegni della Linea. Tale campo richiama l’attributo Custum di tipo numerico TOT_SOSTEGNI della tabella X3ROLLUP (X3ROLLUP. TOT_SOSTEGNI)

Km equivalenti: sommatoria dell’attributo LIN_LUNGHEZZA presente su CAMPATA e TRATTA CAVO (non demolite/cedute), escludendo le campate in doppia terna delle campate ospitate (per campate ospitate si intendono la campata con codice SAP di 1°livello maggiore). Inoltre, nel caso di campate split va conteggiata la sola lunghezza delle campate con attributo LIN_CAMPATA_RIFERIMENTO vuotoTale campo richiama l’attributo Custum di tipo numerico KM_EQUIVALENTI della tabella X3ROLLUP (X3ROLLUP. KM_EQUIVALENTI).

Lunghezza Terna: sommatoria dell’attributo LIN_LUNGHEZZA di CAMPATE e TRATTE CAVO non demolite/cedute della linea in questione;. Tale campo richiama l’attributo Custum di tipo numerico LUNGHEZZA_TERNA della tabella X3ROLLUP (X3ROLLUP. LUNGHEZZA_TERNA)

| CAMPO | DEFAULT LABEL | ATTRIBUTO |
| --- | --- | --- |
| Sapid | Sapid | SAPID |
| Descrizione | Descrizione | DESCRIPTION |
| Linea | Linea | ASSETNUM |
| Tot KM Cavo Terrestre | Tot KM Cavo Terrestre | NUMVALUE |
| Tot KM Cavo Marino | Tot KM Cavo Marino | NUMVALUE |
| UI | UI | INGRP |
| Sostegni | Sostegni | TOT_SOSTEGNI |
| Km equivalenti | Km equivalenti | KM_EQUIVALENTI |
| Lunghezza Terna | Lunghezza Terna | LUNGHEZZA_TERNA |

Tabella 21 – Campi e proprietà “Riepilogo Dati”

La seguente sezione viene popolata e successivamente aggiornata tramite automatismo X3RIEPILOGO (rif. paragrafo di questo Doc. 5.1 Automation Scripts) – LLD) e apposita azione visibile in applicazione Linee (azione XRIEPILOGO con label “Riepilogo Dati”, rif. paragrafo di questo Doc. 5.2 Azioni) applicata alla Sigoption X3RIEPILOGO Riepilogo Dati (rif. paragrafo di questo Doc. 5.4 Sigoption). Il comando “Riepilogo Dati” viene invocato dall’utente in applicazione “Linee” e permette di aggiornare all’occorrenza la sezione “Riepilogo Dati” settando i campi sopra esplicitati secondo regole di calcolo definite all’interno del file di Mapping.

Figura 13 – Comando “Riepilogo Dati”

### Tab “Tronchi”

In questa scheda sono gestiti (creati manualmente e visualizzati) i tronchi di ispezione associabili agli asset della linea a cui devono essere riferiti (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0 paragrafo 2.6.3 Tronchi).

Di seguito, la schermata principale del tab Tronchi in cui è possibile creare/visualizzare ad elenco tutti i tronchi gestiti per una specifica linea:

Figura 11 - Schermata “Tronchi”

La schermata presenta i seguenti campi di testata:

Linea: numero identificativo univoco della linea. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET (ASSET.ASSETNUM)

SAP ID: codice sede tecnica SAP della linea. Tale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET (ASSET.X3SAPID)

Status: stato della linea. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET (ASSET.STATUS)

I tronchi di ispezione nel Manage vengono gestiti per mezzo della tabella X3TRONCHI (Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0 paragrafo 2.5.1 Tronchi di Ispezione). Di conseguenza, per la tabella “Tronchi” è stata impostata la relazione X3TRONCHI che collega la tabella ASSET alla tabella X3TRONCHI.

Di seguito si riportano le proprietà impostate per la sezione contenente la tabella “Tronchi”:

| Label | Tronchi |
| --- | --- |
| Data Source ID | 1684158524105 |
| Relationship | X3TRONCHI |

Tabella 22 - Proprietà Sezione “Tronchi”

Si riporta una descrizione delle informazioni presenti in questa sezione, cioè quelle specifiche dei tronchi:

Sapid: codice sede tecnica del tronco di ispezione gestito in SAP. Tale campo richiama l’attributo Custom SAPID, di tipo UPPER, della tabella X3TRONCHI (X3TRONCHI.SAPID)

Descrizione: descrizione del tronco di ispezione. Tale campo richiama l’attributo Custom di tipo alfanumerico DESCRIPTION della tabella X3TRONCHI (X3TRONCHI.DESCRIPTION)

Lunghezza tronco: lunghezza del tronco di ispezione. Tale campo richiama l’attributo Custom FREQUENZAANNUALE di tipo Decimal della tabella X3TRONCHI

Frequenza annuale: Frequenza di ispezione del tronco.Tale campo richiama l’attributo Custom FREQUENZAANNUALE di tipo smallint della tabella X3TRONCHI

Zona: definisce la zona in cui si trova il tronco. Tale campo richiama l’attributo Custom di tipo alfanumerico ZONATRONCO della tabella X3TRONCHI.

N. Sostegni: Numero dei sostegni. Tale campo richiama l’attributo Custom di tipo intero NSOSTEGNIV della tabella X3TRONCHI.

Di seguito una tabella riepilogativa con le properties dei vari campi:

| CAMPO | DEFAULT LABEL | ATTRIBUTO |
| --- | --- | --- |
| Sapid | Sapid | SAPID |
| Frequenza annuale | Frequenza annuale | FREQUENZAANNUALE |
| Zona | Zona | ZONATRONCO |
| N. Sostegni | N. Sostegni | NSOSTEGNIV |
| Descrizione | Descrizione | DESCRIPTION |

Tabella 23 - Campi e Proprietà “Tronchi”

### Tab “Dati Linea”

In questa scheda, in formato tabellare, sono mostrati tutti gli attributi della classificazione LINEA della linea selezionata (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0 paragrafo 2.6.4 Dati Linea).

Figura 12 - Schermata “Dati Linea”

La schermata presenta i seguenti campi di testata:

Linea: numero identificativo univoco della linea. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET (ASSET.ASSETNUM)

SAP ID: codice sede tecnica SAP della linea. Tale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET (ASSET.X3SAPID)

Status: stato della linea. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET (ASSET.STATUS)

La seconda sezione “Specifiche linea” permette di visualizzare la lista degli attributi associati alla Linea, è caratterizzata dalla relazione ASSETSPEC e riporta in ordine i seguenti attributi:

Attributo: Id dell’attributo di classificazione. Tale campo richiama l’attributo di tipo alfanumerico ASSETATTRID della tabella ASSETSPEC (ASSETSPEC.ASSETATTRID)

Descrizione: Descrizione attributo. Tale campo richiama l’attributo di tipo alfanumerico DESCRIPTION della tabella ASSETSPEC (ASSETSPEC. DESCRIPTION)

Valore Data: Valore di tipo Data dell’attributo. Tale campo richiama l’attributo di tipo Data DATEVALUE della tabella ASSETSPEC (ASSETSPEC. DATEVALUE)

Data Modifica: Valore di tipo Data dell’attributo. Si tratta di un campo custom in cui l’utente può inserire la data effettiva della modifica (anche antecedente) (ASSETSPEC.X3DATAMODIFICA)

Valore Numerico: Valore di tipo Numerico dell’attributo. Tale campo richiama l’attributo NUMVALUE della tabella ASSETSPEC (ASSETSPEC. NUMVALUE)

Valore Tabella: Valore di tipo Tabella dell’attributo. Tale campo richiama l’attributo TABLEVALUE della tabella ASSETSPEC (ASSETSPEC. TABLEVALUE)

Valore ALN: Valore di tipo Alfanumerico dell’attributo. Tale campo richiama l’attributo ALNVALUE della tabella ASSETSPEC (ASSETSPEC. ALNVALUE)

Unità di misura: Unità di misura dell’attributo. Tale campo richiama l’attributo MEASUREUNITID della tabella ASSETSPEC (ASSETSPEC. MEASUREUNITID)

Di seguito una tabella riepilogativa con le proprietà dei vari campi:

| CAMPO | DEFAULT LABEL | ATTRIBUTO |
| --- | --- | --- |
| Attributo | Attributo | ASSETATTRID |
| Descrizione | Descrizione | DESCRIPTION |
| Valore Data | Valore Data | DATEVALUE |
| Valore Numerico | Valore Numerico | NUMVALUE |
| Valore Tabella | Valore Tabella | TABLEVALUE |
| Unità di misura | Unità di misura | MEASUREUNITID |
| Valore alfanumerico | Valore alfanumerico | ALNVALUE |
| Data Modifica | Data Modifica | X3DATAMODIFICA |

Tabella 24 - Campi e Proprietà “Dati Linea”

### Attributi calcolati su Tab “Dati Linea”

All’interno dell’applicazione Linee, nella scheda "Dati Linea", è possibile visualizzare attributi calcolati automaticamente in seguito alle variazioni sulla Linea.

Questi attributi non sono modificabili manualmente dall’utente e riguardano principalmente la lunghezza della terna e il numero di sostegni presenti.

Di seguito è riportata una tabella con il dettaglio dei criteri di calcolo per ciascun attributo:

| ATTRIBUTO EAM | DESCRIZIONE | Criterio di Calcolo | Dettaglio Criterio di calcolo |
| --- | --- | --- | --- |
| LIN_LUNGHEZZA_T | LUNGHEZZA TERNA | round(totKmDoppiaTerna+ totKmSingolaTerna) |  |
| LIN_NUM_SOSTEGNI | NUMERO SOSTEGNI | totSostegni | Totale dei sostegni presenti sulla linea, esclusi i sostegni in stato ‘CEAL', 'DEMO', 'DIST' |
| LIN_KM_EQUIVALENTI | KM EQUIVALENTI | totKmDoppiaTerna+ totKmSingolaTerna)* fattoreK |  |
| LIN_LUNGHEZZA_L | LUNGHEZZA LINEA | round((totKmDoppiaTerna+ totKmSingolaTerna) |  |
| LIN_NUM_SOSTEGNI_PP | NUMERO SOSTEGNI SU PALIFICAZIONE PROPRIA | *vedi query | sostegni aerei con campate adiacenti in singola terna e con stato diverso da `CEAL`, `DEMO`, `DIST` + sostegni aerei associati ad una campata in doppia terna. Include le campate con LIN_CAMPATA_RIFERIMENTO vuoto o con LIN_CAMPATA_RIFERIMENTO uguale alla campata corrente |
| LIN_UIM1_ID | U.I. 1: UNITÀ IMPIANTI |  |  |
| LIN_UIM1_LUNG_TERNA | U.I. 1: LUNGHEZZA TERNA |  |  |
| LIN_UIM1_NUM_SOSTEGNI | U.I. 1: NUMERO SOSTEGNI |  |  |
| LIN_UIM1_KM_EQUIVALENTI | U.I. 1: KM EQUIVALENTI |  |  |
| LIN_UIM1_SOSTEGNO_ESTREMO_FIN | U.I. 1: SOSTEGNO O ESTREMO FINE COMPETENZA |  |  |
| LIN_UIM1_SOSTEGNO_ESTREMO_INI | U.I. 1: SOSTEGNO O ESTREMO INIZIO COMPETENZA |  |  |
| LIN_LUNGHEZZA_CT | LUNGHEZZA IN CAVO TERRESTRE |  |  |
| LIN_LUNGHEZZA_CM | LUNGHEZZA IN CAVO MARINO |  |  |
| LIN_DISPOSIZIONE_FASI_ESTREMO_INI | DISPOSIZIONE FASI ESTREMO INIZIO |  |  |
| LIN_DISPOSIZIONE_FASI_ESTREMO_FIN | DISPOSIZIONE FASI ESTREMO FINE |  |  |
| LIN_LUNGHEZZA_AS | LUNGHEZZA IN AEREO SEMPLICE TERNA | round(totKmCampata) |  |
| LIN_LUNGHEZZA_AD | LUNGHEZZA IN AEREO DOPPIA TERNA | round(totKmDoppiaTerna) |  |
| LIN_LUNGHEZZA_AA | LUNGHEZZA IN AEREO DOPPIA TERNA AMMAZZETTATA | round(totKmAmmazzettati) | Campate in doppia terna con stato diverso da CEAL, DEMO, DIST. Include le campate con LIN_CAMPATA_RIFERIMENTO vuoto o con LIN_CAMPATA_RIFERIMENTO uguale alla campata corrente. La lunghezza delle campate viene convertita da metri a chilometri dividendo per 1000. |

Tabella 25 -Attributi Calcolati

### Tab “Linee in relazione”

In questa scheda sono mostrate tutte le linee che hanno una relazione con la linea selezionata, ossia condividono almeno un asset di secondo livello (sostegno o campata) in terna doppia (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0 paragrafo 2.6.6).

Di seguito, la schermata principale del tab:

Figura 14 - Schermata “Linee in Relazione”

La schermata presenta i seguenti campi di testata:

Linea: numero identificativo univoco della linea. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET (ASSET.ASSETNUM) e l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET (ASSET.DESCRIPTION)

SAP ID: codice sede tecnica SAP della linea. Tale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET (ASSET.X3SAPID)

Status: stato della linea. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET (ASSET.STATUS)

Di seguito si riportano le proprietà impostate per la sezione contenente la tabella “Linee in relazione”:

| Label | Linee in relazione |
| --- | --- |
| Data Source ID | 1685571383588 |
| Relationship | X3ASSETCOM |

Tabella 25 - Proprietà Sezione “Linee in Relazione”

Si riporta una descrizione delle informazioni presenti in questa sezione, cioè quelle specifiche delle linee in relazione:

Linea: numero identificativo univoco della linea in relazione. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET (ASSET.ASSETNUM)

Descrizione: descrizione della linea in relazione. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET (ASSET.DESCRIPTION)

Stato: stato della linea in relazione. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET (ASSET.STATUS)

Di seguito una tabella riepilogativa con le properties dei vari campi:

| CAMPO | DEFAULT LABEL | ATTRIBUTO |
| --- | --- | --- |
| Linea | Asset | ASSETNUM |
| Descrizione | Descrizione | DESCRIPTION |
| Stato | Status | STATUS |

Tabella 26 - Campi e Proprietà “Linee in Relazione”

## Gruppi di persone Terna (X3PERSONGR)

L’applicazione Custom “Gruppi di persone Terna” è un duplicato dell’applicazione “Person Group”, standard in IBM Maximo. La schermata principale dell’applicazione “Gruppi di persone Terna” sarà caratterizzata della lista di record presenti in Tabella 28 e Tabella 29 presenti nel Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0 paragrafi 3.5 Personale delle Unità Impianti)., dove sarà possibile selezionare i record specifici Tale lista sarà così strutturata:

Figura 15 - Schermata “Gruppi di Persone Terna”

Selezionando un record, l’applicazione sarà caratterizzata da 1 tab, così costituito:

Figura 16 - Dettaglio “Gruppo di Persone Terna”

# Detailed processing

Nel seguente capitolo, vengono riportati tutti gli elementi aggiuntivi creati su MAS Dev di Terna e che permettono di personalizzare ulteriormente l’applicativo. Questi includono ad esempio conditional expressions, automation scripts, messaggi di sistema, azioni.

L'obiettivo di questa sezione è quello di fornire una panoramica completa di tutte le personalizzazioni e automatismi presenti a sistema.

## Automation Scripts

Gli script di automazione sono piccole parti di codice mirate che possono estendere il prodotto. Uno script di automazione è costituito da un punto di avvio, da variabili con valori di binding corrispondenti e dal codice sorgente.

Di seguito viene fornita una tabella riepilogativa degli script implementati:

| SCRIPT | LAUNCH POINT | LAUNCH POINTS TYPE | OGGETTO | EVENTO | VARIABILE | RIF. HLD | Dettaglio |
| --- | --- | --- | --- | --- | --- | --- | --- |
| X3CAMBIOSTATO | X3CAMBIOSTATO | OBJECT | ASSET | Save Add Update After Save | INPUT: vi_assetnum=RELATEDASSET.SOURCEASSETNUM vi_status=STATUS vi_siteid=RELATEDASSET.SITEID vi_classstructureid=CLASSSTRUCTURE.CLASSIFICATIONID | Rif.Doc.1 3.8.5 Gestione degli stati | Script che permette di modificare lo stato della Linea partendo da un Asset di 2° livello |
| X3CREATEASSETDIALOGINIT | X3CREATEASSET | OBJECT | X3CREATEASSET | Initialize | - |  |  |
| X3CREATEASSETDIALOGSAVE | X3CREATEASSET | OBJECT | X3CREATEASSET | Save Add Before Save | - | Rif.Doc.1 4.1 Asset di terzi e sedi tecniche integrative 5.2 OP_305 - Rimozione della gestione della lettera K per il terminale di fine |  |
| X3DISPFASI | X3DISPFASI | OBJECT | ASSETSPEC | Save Add Update After Save | INPUT: vi_attributo=ASSETATTRID vi_dispfasi=ALNVALUE | Rif.Doc.1 3.12 Gestione della disposizione fasi | Controlla il corretto formato dell’attribuito LIN_DISPOSIZIONE_FASI |
| X3RIEPILOGO | X3RIEPILOGO | ACTION | ASSET | - | INPUT: v_assetnum=ASSETNUM v_siteid=SITEID | Rif.Doc.1 3.11.4 Visualizzazione aggregata sul 1° livello di attributi relativi al 2° livello | Setta sul tab “Riepilogo Dati” e su determinati attributo del tab “Dati Linea” i seguenti calcoi: - Il totale dei sostegni dei Km equivalenti - La lunghezza della terna - Il totale dei Km di cavo terrestre - Il totale dei Km di cavo marino |
|  |  |  |  |  |  |  | Setta sul tab “Riepilogo Dati” e su determinati attributo del tab “Dati Linea” i seguenti calcoi: - Il totale dei sostegni dei Km equivalenti - La lunghezza della terna - Il totale dei Km di cavo terrestre - Il totale dei Km di cavo marino |
|  | X3RIEPILOGO_ATT | ATTRIBUTE | ASSETSPEC | Validate | INPUT: v_assetnum=assetnum v_siteid=siteid | Rif.Doc.1 3.11.4 Visualizzazione aggregata sul 1° livello di attributi relativi al 2° livello | Setta sul tab “Riepilogo Dati” e su determinati attributo del tab “Dati Linea” i seguenti calcoi: - Il totale dei sostegni dei Km equivalenti - La lunghezza della terna - Il totale dei Km di cavo terrestre - Il totale dei Km di cavo marino |
| X3ROLLUPATTRIBUTE | X3ROLLUPATTRIBUTE | ATTRIBUTE | ASSETSPEC | Validate | INPUT: v_assetnum=ASSETNUM v_siteid=SITEID | Rif.Doc.1 3.11.5 Strumento generico per la gestione di roll up basati su gerarchia o relazione | Permette di effettuare dei calcoli sugli attributi, in base all’operazione selezionata (es. MAX, SUM,MIN) |
| X3SAPIDVALIDATION | X3TRONCHI | ATTRIBUTE | X3TRONCHI | Validate | - | Rif. Doc.1 3.18 Gestione della ricodifica del codice descrittivo SAP (ID) | Script per la gestione del corretto formato del SAPID dei Tronchi |
| X3SAPIDVALIDATION | X3ASSET | ATTRIBUTE | ASSET | Validate | - | Rif. Doc.1 3.18 Gestione della ricodifica del codice descrittivo SAP (ID) | Script per la gestione del corretto formato del SAPID dei Tronchi |
| X3SAPIDVALIDATION | X3ASSETLOCRELATION | ATTRIBUTE | ASSETLOCRELATION | Validate | - | Rif. Doc.1 3.18 Gestione della ricodifica del codice descrittivo SAP (ID) | Script per la gestione del corretto formato del SAPID dei Tronchi |
| X3SAPIDLINEA | X3SAPIDLINEA | OBJECT | ASSET | Save Add Update After Save | INPUT: vi_linea=CLASSSTRUCTURE.CLASSIFICATIONID vi_sapid=X3SAPID | Rif. Doc.1 3.18 Gestione della ricodifica del codice descrittivo SAP (ID) | Script per la gestione del corretto formato del SAPID della Linea |
| X3SAPIDLIVELLO2 | X3SAPIDLIVELLO2 | OBJECT | ASSETLOCRELATION | Save Add Update After Save | INPUT: vi_classstrac=ASSET.CLASSANCESTOR.CLASSIFICATIONID vi_sapid=X3SAPID | Rif. Doc.1 3.18 Gestione della ricodifica del codice descrittivo SAP (ID) | Script per la gestione del corretto formato del SAPID degli asset di 2° e 3° Livello SAP |
| X3ASSOCIATELINE | X3ASSOCIATELINE | ACTION | ASSET | - | - |  | Script che permette l’associazione degli Asset nel caso di Doppia Terna |
| X3SETX3INGRPCHILD | X3SETX3INGRPCHILD | OBJECT | ASSET | Save Add Update Before Save | - |  | Script che copia il valore dell’Unità Impianti di 2° Livello sui livelli inferiori (3° e 4°) |
| X3CALNUMSOSTEGNI | X3CALNUMSOSTEGNI | OBJECT | ASSETLOCRELATION | Save Add Update After commit |  |  | Calcolo dei sostegni |
| X3CALCLINLUNGHEZZE | X3CALCLINLUNGHEZZE | ATTRIBUTE | ASSETSPEC | Validate |  |  | Calcoli sugli attributi LIN_LUNHEZZA |
| X3SPECTRONCO | X3SPECTRONCO | OBJECT | ASSETSPEC |  | :assetattrid='LIN_TRONCO_ISPEZIONE' |  | Script per il recupero delle seguenti informazioni per i Tronchi: - La lunghezza totale delle campate e delle tratte cavo - Numero totale dei sostegni per ogni tronco |
| X3SPECTRONCO | X3SPECTRONCOLUN | OBJECT | ASSETSPEC |  | :assetattrid='LIN_LUNGHEZZA' |  | Script per il recupero delle seguenti informazioni per i Tronchi: - La lunghezza totale delle campate e delle tratte cavo - Numero totale dei sostegni per ogni tronco |
| X3CHANGEASSETINIT | X3CHANGEASSETINIT | OBJECT | X3CHANGEASSET | Initalize Value |  |  | Gestione della dialog X3CHANGEASSET |
| X3CHANGEASSETSAVE | X3CHANGEASSETSAVE | OBJECT | X3CHANGEASSET |  |  |  | Gestione della dialog X3CHANGEASSET salvataggio |
| X3ADDEXISTINGASSET | X3ADDEXISTINGASSET | ACTION | ASSET |  |  |  | Handles "Nuova associazione asset esistenti" OK button |
| X3RECODESAPIDINIT | X3RECODESAPIDINIT | OBJECT | X3RECODESAPID |  |  |  | Gestione della dialog X3RECODESAPID |
| X3INVLINE | X3INVLINE | OBJECT | X3INVLINE |  |  | Rif.Doc.1 3.17 Gestione del verso della linea 4.1 Anagrafiche opere interferenti 3.17.3.1 Derivazioni multiple dallo stesso sostegno della dorsale (OP_253) 5.2 OP_305 - Rimozione della gestione della lettera K per il terminale di fine | Handles the inversion of the line dialog OK button |
| X3RECODESAPIDSAVE | X3RECODESAPIDSAVE | OBJECT | X3RECODESAPID |  |  |  | Ricodifica SAPID |
| X3REMOVEASSET | X3REMOVEASSET | ACTION | ASSET |  |  |  | Script per la rimozione degli asset |
| X3RENUMSEQUENCE | X3RENUMSEQUENCE |  |  |  |  |  | Gestione della sequenza |
| X3DIVIDICAMPATA | X3DIVIDICAMPATA | ACTION | ASSET |  |  | Rif. Doc. 1 5.5 OP_254 - Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate) | Script per la gestione della bonifica delle campate in doppia terna |
| X3DIVIDICAMPATAINIT | X3DIVIDICAMPATAINIT | OBJECT | X3DIVIDICAMPATA | Initialize Value |  | Rif. Doc. 1 5.5 OP_254 - Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate) | Script per la gestione della dialog X3DIVIDICAMPATA |
| X3UNISCISOSTEGNO | X3UNISCISOSTEGNO | ACTION | ASSET |  |  | Rif. Doc. 1 5.5 OP_254 - Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate) | Script per la gestione della bonifica dei sostegni in singola terna |
| X3UNISCISOSTEGNOINIT | X3UNISCISOSTEGNOINIT | OBJECT | X3UNISCISOSTEGNO | Initialize Value |  | Rif. Doc. 1 5.5 OP_254 - Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate) | Script per la gestione della dialog X3UNISCISOSTEGNO |
| X4ASSEGNAZIONECAMPATA | X4ASSEGNAZIONECAMPATA | OBJECT | ASSET | Save Add Update Before Save |  | Rif.Doc.1 4.2 Lotti tecnici omogenei | Script per la gestione dei lotti tecnici omogenei |
| X4ASSETSPEC_LIN_ID_INTERSEZION | X4ASSETSPEC_LIN_ID_INTERSEZION | OBJECT | ASSETSPEC | Save Update Before Save |  | Rif.Doc.1 4.1 Anagrafiche opere interferenti | Script per la generazione di LIN_ID_OPERA |
| X4CREATEINTERFERENCE | X4CREATEINTERFERENCE | ACTION | ASSETLOCRELATION |  |  | Rif.Doc.1 4.1.2 Modalità di creazione | Script per la creazione opera interferente per campata e cavo |
| X4ASSETSPEC_LIN_CRITICITA_LIV4 | X4ASSETSPEC_LIN_CRITICITA_LIV4 | OBJECT | ASSETSPEC | Save Update Before Save |  | Rif.Doc.1 4.1 Anagrafiche opere interferenti | Script per il calcolo di LIN_CRITICITA_LIV3_INT |
| X4TABPAROPINT | X4TABPAROPINT | OBJECT | ASSETSPEC | Save Update Before Save |  | Rif.Doc.1 4.1 Anagrafiche opere interferenti | Script per il calcolo tramite tabella parametrica di LIN_CRITICITA_LIV4_INT |
| X3CREALEGAME | X3CREALEGAME | OBJECT | X3CREALEGAME | Save Add UpdateS Before Save |  | OP 252 | Script per dialog x3crealegamedialog per unione linee differenti |
| X3CREALEGAMEINIT | X3CREALEGAMECLASSST | ATTRIBUTE | X3CREALEGAME | Initialize Value |  | OP 252 | Inizializza la classstructureid all’apertura della dialog x3crealegamedialog |
| X3CREATEDER | X3CREATEDER | OBJECT | X3CREATEASSET | Save Add Before Save |  | Rif HLD 3.17.3.1 Derivazioni multiple dallo stesso sostegno della dorsale | Script che gestisce Derivazione, Derivazione di Derivazione, Derivazione Multipla (OP_253) e Derivazione in Cavo (OP_256) |
|  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |

Tabella 27 - Automation Scripts

## Azioni

Le azioni in Maximo sono eventi che possono essere richiamati tramite script, escalation o workflow, e che generano una serie di operazioni o funzionalità specifiche all'interno del sistema. Queste azioni possono includere la creazione, la modifica, l'eliminazione o l'approvazione di record, l'automazione di processi e altre attività che facilitano e migliorano l'efficienza delle operazioni.

Di seguito è riportata la lista delle azioni Custom create sul sistema e indentificate con il prefisso X3. Le azioni vengono richiamate negli Script (rif. 5.1 Automation Scripts) e collegate alle sigoption al fine di rendere visibili e richiamabili tali azioni da interfaccia grafica.

| Azione | Descrizione | Nome oggetto | Parametri | Riferimento Script (par. 5.1) | Dettaglio/rif .HLD EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0.docx |
| --- | --- | --- | --- | --- | --- |
| X3ASSOCIATELINE | Associa linea | ASSET | X3ASSOCIATELINE,X3ASSOCIATELINE,X3ASSOCIATELINE | X3ASSOCIATELINE | Richiama il pulsante “Associazione Terna Doppia”/ Rif. HLD Anagrafica par. 3.7.2 Creazione di una nuova linea (terna singola, doppia e ripresa da “ammazzettamento”) |
| X3REMOVEASSET | Rimuovi Asset | ASSET | X3REMOVEASSET,X3REMOVEASSET,X3REMOVEASSET | X3REMOVEASSET | Richiama il pulsante “Remove Asset”/ Rif HLD Anagrafica par. 3.8.6.2 Rimozione massiva da una linea di asset di secondo livello |
| X3RIEPILOGO | Riepilogo Dati | ASSET | X3RIEPILOGO,X3RIEPILOGO,X3RIEPILOGO | X3RIEPILOGO | Richiama il pulsante “Riepilgoo Dati”/ Rif HLD Anagrafica par. 3.11.4 Visualizzazione aggregata sul 1° livello di attributi relativi al 2° livello |
| X3ADDEXISTINGASSET | Nuova associazione asset esistenti | ASSET | X3ADDEXISTINGASSET,X3ADDEXISTINGASSET,X3ADDEXISTINGASSET | X3ADDEXISTINGASSET | Richiama il pulsante ““Associazione”/ Rif. HLD Anagrafica par. 3.8.6.1 Associazione massiva ad una linea di asset di secondo livello (palificazione esistente) |
| X3DIVIDICAMPATA | Action X3DIVIDICAMPATA | ASSET | X3DIVIDICAMPATA,X3DIVIDICAMPATA,X3DIVIDICAMPATA | X3DIVIDICAMPATA | Richiama il pulsante “Bonifica campate doppie” Rif. Doc. 1 5.5 OP_254 - Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate) |
| X3UNISCISOSTEGNO |  | ASSET | X3UNISCISOSTEGNO,X3UNISCISOSTEGNO,X3UNISCISOSTEGNO | X3UNISCISOSTEGNO | Richiama il pulsante “Bonifica sostegni singoli” Rif. Doc. 1 5.5 OP_254 - Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate) |
| X4CREATEINTERFERENCE | Creazione opere interferenti per campata e cavo | ASSETLOCRELATION | X4CREATEINTERFERENCE | X4CREATEINTERFERENCE | Richiama il pulsante “Crea opera interferente” / Rif HLD Anagrafica par. 4.1.2 Modalità di creazione |
|  |  |  |  |  |  |
|  |  |  |  |  |  |

Tabella 28 - Azioni

## Conditional expression manager

Il Conditional Expression Manager, all'interno di MAS (Maximo Asset Management System), è un componente essenziale per creare e gestire condizioni basate su espressioni SQL. Queste condizioni servono a controllare processi, definire comportamenti e stabilire regole di business basate su criteri specifici. Possono essere applicate dinamicamente a vari elementi dell'interfaccia utente, come campi, schede e altri controlli, personalizzando così la presentazione. Inoltre, le condizioni possono essere estese ai gruppi di sicurezza per regolare l'accesso a specifiche applicazioni, controlli e dati, garantendo una gestione precisa e sicura delle funzionalità del sistema. Ad esempio, uno stato specifico di un asset può essere nascosto in base a una condizione come X3FALSE.

Di seguito è riportata la lista delle condizioni create e le rispettive query SQL (Expression):

| Condition | Description | Expression | Dettaglio |
| --- | --- | --- | --- |
| X3FALSE | Condizione sempre falsa | 1=3 | Condizione applicata al dominio degli Stati (LOCASSETSTATUS) per nascondere gli stati non previsti (es. stati standard presenti in EAM) |
| X3GESTSTATS | Verifica presenza attributo GESTSTAT per stazioni | EXISTS (SELECT CR.CLASSSTRUCTUREID FROM CLASSANCESTOR CR join CLASSSPEC CC ON CC.CLASSSTRUCTUREID = CR.CLASSSTRUCTUREID WHERE CR.CLASSSTRUCTUREID = :CLASSSTRUCTUREID AND CR.ANCESTORCLASSID IN('STAZIONE') AND CR.HIERARCHYLEVELS = 3 AND CC.ASSETATTRID = 'GESTSTAT') | Condizione applicata al dominio degli Stati (LOCASSETSTATUS) per rendere visibili gli stati previsti per le Stazioni |
| X3STATL12S1 | Visibilità stati per Liv 1 e 2 linee e Liv 1 stazioni | EXISTS (SELECT CLASSSTRUCTUREID FROM CLASSANCESTOR WHERE CLASSSTRUCTUREID = :CLASSSTRUCTUREID AND ANCESTORCLASSID IN('LINEA','STAZIONE') AND HIERARCHYLEVELS IN(0)) OR EXISTS (SELECT CLASSSTRUCTUREID FROM CLASSANCESTOR WHERE CLASSSTRUCTUREID = :CLASSSTRUCTUREID AND ANCESTORCLASSID IN('LINEA') AND HIERARCHYLEVELS IN(1)) | Condizione applicata al dominio degli Stati (LOCASSETSTATUS) per rendere visibili gli stati previsti per il 1 e 2 livello delle Linee |
| X3GESTSTATLS | Verifica presenza attributo GESTSTAT per linee e stazioni | EXISTS (SELECT CR.CLASSSTRUCTUREID FROM CLASSANCESTOR CR join CLASSSPEC CC ON CC.CLASSSTRUCTUREID = CR.CLASSSTRUCTUREID WHERE CR.CLASSSTRUCTUREID = :CLASSSTRUCTUREID AND CR.ANCESTORCLASSID IN('LINEA','STAZIONE') AND CR.HIERARCHYLEVELS = 3 AND CC.ASSETATTRID = 'GESTSTAT')WHERE CR.CLASSSTRUCTUREID = :CLASSSTRUCTUREID AND CR.ANCESTORCLASSID IN('LINEA','STAZIONE') AND CR.HIERARCHYLEVELS = 3 AND CC.ASSETATTRID = 'GESTSTAT') | Verifica la presenza dell’attributo GESTSTAT all’interno degli attributi di classificazione per gli asset di 3 Livello |
| X3INGRP | Condizione che rende modificabile l'Asset in base all'Unità Impianti | (:assetnum in (select assetnum from asset where x3swerk is null and siteid='SIT01')) or (:&USERNAME& in (select resppartygroup from persongroupteam where persongroup in (select persongroup from persongroup where x3parentpersongroup=:x3swerk))) | Condizione presente in applicazione Linee che rende l’applicazione modificabile o in sola lettura in base all’UI di appartenenza |
| X3MOVEASSET | Condizione per abilitare/disabilitare il Move Asset | (:assetnum in (select assetnum from asset where x3swerk is null and siteid='SIT01')) or (:status ='NIED') or (:status<>'NIED' and :&USERNAME& in (select resppartygroup from persongroupteam where persongroup in (select persongroup from persongroup where x3parentpersongroup=:x3swerk))) and :siteid='SIT01' | Condizione applicata all’azione Modifica/Sposta Asset che abilita o disabilita lo spostamento dell’Asset in base all’UI di appartenenza |
| X3RESP | Condizione che verifica se il Gruppo responsabile è presente nell'attributo | :assetattrid not in (select ASSETATTRID from ASSETATTRIBUTE where X3GRUPPO_RESP is not null and :&USERNAME& in (select PERSONGROUPTEAM.RESPPARTYGROUP from PERSONGROUPTEAM where persongroup=ASSETATTRIBUTE.X3GRUPPO_RESP)) AND (:assetattrid not in (select ASSETATTRID from ASSETATTRIBUTE where X3GRUPPO_RESP is null )) | Condizionate utilizzata all’interno delle Global Restriction (6.1) che verifica la presenza del gruppo responsabile su un determinato attributo. Se l’attributo presenta un Gruppo responsabile diverso dall’utente loggato, l’attributo diviene read only e viceversa |
| X3ATTREAD | Condizione per mettere in read only gli attributi che iniziano con "LIN_UI%" | :assetattrid like 'LIN_UIM%' | Condizionate utilizzata all’interno delle Global Restriction (6.1) che rende read only i campi di tipo NUMVALUE, ALNVALUE e TABLEVALUE per gli attributi con prefisso LIN_UI% |

Tabella 29 - Conditional Expressions

## Sigoption

Le sigoption consentono una personalizzazione maggiore dei permessi e delle autorizzazioni in EAM, garantendo che solo le persone abilitate possano eseguire determinate azioni o visualizzare/modificare campi specifici. Vengono create all’interno dell’Application designer (rif.4 Application Designer) e possono essere applicate a campi o azioni. L'abilitazione viene gestita attraverso l'applicazione Gruppi di Sicurezza ed è applicabile a livello di singolo Gruppo.

Un esempio di utilizzo delle Sigoption nell'ambiente Terna è rappresentato dalla Sigoption X3REMOVEASSET, la quale viene applicata su un'azione specifica che consente ai gruppi di sicurezza abilitati di visualizzare l'opzione di rimozione dell'asset nell'applicazione Linee.

Di seguito, le Sigoption create a sistema. Nella colonna Descrizione vengono descritte le azioni che verranno abilitate all’utente tramite la Sigoption nell’applicazione Linee:

| Applicazione | Optionname | Descrizione |
| --- | --- | --- |
| X3LINEE | X3ADDEXISTINGASSET | Nuova Associazione asset esistenti OK |
| X3LINEE | X3ADDEXISTINGASSETDIALOG | Nuova Associazione asset esistenti |
| X3LINEE | X3ASSOCIATELINE | Associa Linea |
| X3LINEE | X3ASSOCIATELINEDIALOG | Associazione in terna doppia |
| X3LINEE | X3CREATE4RELATEDASSET | Crea asset di quarto livello |
| X3LINEE | X3CREATEASSETDIALOG | Genera asset di secondo livello |
| X3LINEE | X3CREATEDERDIALOG | Crea derivazioni |
| X3LINEE | X3CREATEPALIDIALOG | Crea Palificazione |
| X3LINEE | X3CREATERELATEDASSET | Crea asset di quarto livello |
| X3LINEE | X3INVLINE | Inverti linea |
| X3LINEE | X3RECODEDIALOG | Ricodifica SAPID |
| X3LINEE | X3REMOVEASSET | Rimuovi asset (Dialog) |
| X3LINEE | X3REMOVEASSETDIALOG | Rimuovi asset (pulsante) |
| X3LINEE | X3REPLACEASSET | Sostituisci asset di quarto livello |
| X3LINEE | X3RIEPILOGO | Riepilogo Dati |
| X3LINEE | X3ADDEXISTINGASSET | Nuova Associazione asset esistenti OK |
| X3LINEE | X3ADDEXISTINGASSETDIALOG | Nuova Associazione asset esistenti |
| X3LINEE | X3ASSETPECMANAGE | Permette di Creare o Eliminare attributi di Classificazione per il centro 1 |
| X3LINEE | X3DIVIDICAMPATA | Dividi campata OK button |
| X3LINEE | X3DIVIDICAMPATADIALOG | Bonifica campate doppie |
| X3LINEE | X3UNISCISOSTEGNO | Unisci sostegno OK button |
| X3LINEE | X3UNISCISOSTEGNODIALOG | Bonifica sostegni singoli |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |

Tabella 30 - Sigoption

# Gruppi di protezione

All’interno del MAS è possibile creare e gestire i gruppi di Protezione. Per ogni gruppo di protezione è possibile impostare e gestire i privilegi degli utenti e controllare, quindi, l'accesso e i permessi alle diverse funzionalità e dati all'interno del sistema. Un utente può appartenere a più gruppi di protezione (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0 paragrafo 3.3 Gruppi di protezione e gruppi di persone)

Di seguito l’elenco dei gruppi di Protezione Standard e Custom (prefisso X3) presenti sull’applicativo. La colonna Nome Gruppo indica il nome univoco definito su EAM, Descrizione equivale alla descrizione del Gruppo indicata su EAM e Grant la tipologia di permessi:

ALL: Visualizzazione, Creazione, Modifica, Cancellazione

READ: Visualizzazione

| Nome Gruppo | Descrizione | Grant |
| --- | --- | --- |
| MAXADMIN | Maximo Administrators (Super Users) | ALL |
| MAXDEFLTREG | Nuovo utente |  |
| EVERYONE | Tutti gli utenti Maximo |  |
| trn-eam-rtn-cntr-1-% | Gruppo di sicurezza centro 1 | ALL (Applicazione Linee, Domini, Classificazioni e Attributi) |
| trn-eam-rtn-cntr-2-% | Gruppo di sicurezza centro 2 | ALL (Applicazione Linee) |
| trn-eam-rtn-cntr-3-% | Gruppo di sicurezza centro 3 | READ (Applicazione Linee) |
| X3DISP | Gruppo di sicurezza Dispacciamento | READ (Applicazione Linee) |
| trn-eam-rtn-ui-% | Gruppo unità impianti | ALL (Applicazione Linee) |

Tabella 31 - Gruppi di Protezione

Il gruppo MAXADMIN è destinato agli Amministratori e dà massimo potere agli utenti presenti all’interno.

Inoltre, tutti gli utenti vengono aggiunti automaticamente ai gruppi di sicurezza predefiniti MAXDEFLTREG e EVERYONE per consentire l'accesso iniziale e la funzionalità minima del sistema

## Global Restriction

All’interno dei Gruppi di protezione è possibile definire le Global Restrictions, regole di sicurezza utilizzate per controllare l'accesso a determinate funzionalità, dati o azioni all'interno del MAS. Questo meccanismo permette agli amministratori di sistema di definire politiche di sicurezza centralizzate e valide a livello globale in tutto l'applicativo Maximo. Tutte le condizioni sono dettagliate nel paragrafo

5.3 Conditional Expression Manager del seguente documento.

Di seguito l’elenco delle Global Restriction create in ambiente Terna Dev:

| Oggetto | Attributo | Applicazione | Tipo | Condizione | Descrizione |
| --- | --- | --- | --- | --- | --- |
| ASSETSPEC | ALNVALUE |  | READONLY | X3RESP | Rende ReadOnly l’attributo quando si verifica la condizione X3RESP |
| ASSETSPEC | MEASUREUNITID |  | READONLY | X3RESP | Rende ReadOnly l’attributo quando si verifica la condizione X3RESP |
| ASSETSPEC | NUMVALUE |  | READONLY | X3RESP | Rende ReadOnly l’attributo quando si verifica la condizione X3RESP |
| ASSETSPEC | TABLEVALUE |  | READONLY | X3RESP | Rende ReadOnly l’attributo quando si verifica la condizione X3RESP |
| ASSETSPEC | NUMVALUE | X3LINEE | READONLY | X3ATTREAD | Rende Read Only il campo NUMVALUE per gli attributi con prefisso LIN_UIM% presenti in Tab Dati Linea |
| ASSETSPEC | ALNVALUE | X3LINEE | READONLY | X3ATTREAD | Rende Read Only il campo ALNVALUE per gli attributi con prefisso LIN_UIM% presenti in Tab Dati Linea |

Tabella 32 – Global Restriction

# Gruppi di persone

Nell'ambiente MAS Dev di Terna, sono stati creati specifici gruppi di persone gestiti all'interno dell'applicazione custom "Gruppi di Persona Terna". Questi gruppi sono stati configurati per organizzare gli utenti in base a criteri specifici e caratteristiche comuni, al fine di facilitare la gestione delle autorizzazioni e delle attività.

In particolare, sono stati creati i seguenti gruppi di persone:

33 UI (Unità Impianti)

8 DT (Dipartimenti o Distretti Trasmissione)

È importante sottolineare che un singolo utente può appartenere a più gruppi di persone. Questa struttura e l'associazione dei criteri di filtro consentono di stabilire regole specifiche per la modificabilità o la sola lettura degli asset, garantendo un'organizzazione più efficiente e una gestione accurata delle autorizzazioni.

Le informazioni sopra riportate sono dettagliate nel paragrafo 3.5 Personale delle unità impianti del documento di riferimento Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V3.0.

Di seguito si riportano le Unità Impianti e i Dipartimenti/Distretti creati:

| PERSONGROUP | DESCRIPTION | X3PARENTPERSONGROUP |
| --- | --- | --- |
| UNO | Unità Impianti Novara | 2601 |
| UPM | Unità Impianti Pont St Martin | 2601 |
| UGE | Unità Impianti Genova | 2601 |
| UTO | Unità Impianti Torino | 2601 |
| UBS | Unità Impianti Brescia | 2602 |
| UGO | Unità Impianti Gorlago | 2602 |
| UBG | Unità Impianti Brugherio | 2602 |
| UCH | Unità Impianti Chiuro | 2602 |
| UCS | Unità Impianti Cislago | 2602 |
| UDU | Unità Impianti Dugale | 2603 |
| UTA | Unità Impianti Trentino Alto Adige | 2603 |
| UFV | Unità Impianti Friuli Venezia Giulia | 2603 |
| UVV | Unità Impianti Vittorio Veneto | 2603 |
| UCA | Unità Impianti Camin | 2603 |
| USU | Unità Impianti Suvereto | 2604 |
| UPR | Unità Impianti Parma | 2604 |
| UBO | Unità Impianti Bologna | 2604 |
| UFI | Unità Impianti Firenze | 2604 |
| UAM | Unità Impianti Abruzzo Molise | 2605 |
| UMA | Unità Impianti Marche | 2605 |
| ULN | Unità Impianti Lazio Nord | 2605 |
| UMB | Unità Impianti Umbria | 2605 |
| ULS | Unità Impianti Lazio Centro Sud | 2605 |
| UFE | Unità Impianti Feroleto | 2606 |
| UMD | Unità Impianti Maddaloni | 2606 |
| UFM | Unità Impianti Frattamaggiore | 2606 |
| URO | Unità Impianti Rotonda | 2606 |
| UBR | Unità Impianti Brindisi | 2606 |
| UBA | Unità Impianti Bari | 2606 |
| UCT | Unità Impianti Catania | 2607 |
| UPA | Unità Impianti Palermo | 2607 |
| UCO | Unità Impianti Codrongianos | 2608 |
| UVI | Unità Impianti Villasor | 2608 |
| GTG | TernaGora | 3000 |
| RT2 | Rete2 O&M | 6400 |
| RD2 | Rete2 Dispacciamento | 6400 |

Tabella 33 - Gruppi di persone "UI"

| PERSONGROUP | DESCRIPTION | X3PARENTPERSONGROUP |
| --- | --- | --- |
| 2601 | Dipartimento Trasm. Nord Ovest | N/A |
| 2602 | Dipartimento Trasmissione Nord | N/A |
| 2603 | Dipartimento Trasm. Nord Est | N/A |
| 2604 | Dipartimento Trasm Centro Nord | N/A |
| 2605 | Dipartimento Trasmis. Centro | N/A |
| 2606 | Dipartimento Trasmissione Sud | N/A |
| 2607 | Distretto Trasmissione Sicilia | N/A |
| 2608 | Distretto Trasmis. Sardegna | N/A |
| 3000 | Divisione Terna Gora | N/A |
| 6400 | Divisione Rete2 | N/A |

# Tabella 34 - Gruppi di persone "DT"Recepimento OP_252 - OP_253 - OP_254 - OP_256- OP_305

In questo capitolo vengono dettagliati gli oggetti sviluppati in EAM per supportare gli OP_252, OP_253, OP_254, OP_256 e OP_305. Nei paragrafi successivi saranno illustrate le modifiche tecniche apportate al sistema, sia sui componenti esistenti sia sulle nuove funzionalità.

## Inoltre, i paragrafi precedenti sono stati aggiornati con le specifiche relative a ciascun oggetto creato o modificato.OP_252 – Gestione del legame tra linee elettriche differenti

Al fine di permettere la corretta gestione del legame tra linee differenti, come dettagliato nel paragrafo dell’HLD “3.7.7Gestione del legame tra linee elettriche differenti”, sono stati creati i seguenti oggetti in EAM (eventuali dettagli sono specificati nei paragrafi precedenti in ogni tabella):

Relazione Database X3CREALEGAME per poter gestire la dialag su applicazione Linee;

Automation Script:

X3CREALEGAME, script che gestisce le varie casistiche per la creazione dei legami tra linee

X3CREALEGAMEINIT, script per inizializzare la dialog di “Crea Legame altre linee”

Inoltre, sulla Classificazione CAMPATA AEREA è stato aggiunto un nuovo attributo LIN_SOSTEGNO_INIZIO, in cui viene settato il SAPID del sostegno di inizio. Nei casi in cui è vuoto, verrà calcolato automaticamente dal connettore.

## OP_253 – derivazioni multiple dallo stesso sostegno della dorsale

Il requisito funzionale è descritto al paragrafo 3.17.3.1 dell’HLD Linee, relativo all’OP_253 – “Derivazioni multiple dallo stesso sostegno della dorsale”. Per supportare questo OP, sono stati aggiornati due script di automazione già presenti nel sistema:

X3CREATEDER: gestisce i diversi tipi di derivazione (aerea, multipla, in cavo e di derivazione).

X3INVLINE: gestisce l’inversione della linea

## OP_305 - Rimozione della gestione della lettera K per il terminale di fine

Il requisito funzionale è descritto al paragrafo dell’HLD Linee, 5.2 OP_305 - Rimozione della gestione della lettera K per il terminale di fine. Per supportare questo OP, sono stati aggiornati due script di automazione già presenti nel sistema:

X3CREATEASSETDIALOGSAVE: gestisce la creazione degli asset di 2° e 4° livello linee

X3INVLINE: gestisce l’inversione della linea

## OP_256 - Derivazioni miste aereo e cavo

Il requisito funzionale è descritto al paragrafo dell’HLD Linee, 3.17.3.2 Derivazioni miste aereo e cavo.

Per supportare questo OP, è stato aggiornato lo script di automazione già presente a sistema:

X3CREATEDER: gestisce i diversi tipi di derivazione (aerea, multipla, in cavo e di derivazione).

## OP_254 - Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate)

Il requisito funzionale è descritto al paragrafo dell’HLD Linee,5.5 Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate).

Per supportare questo OP, sono stati creati i seguenti script di automazione:

X3UNISCISOSTEGNOINIT, inizializza la dialog X3UNISCISOSTEGNO

X3UNISCISOSTEGNO, gestisce i casi di unione del sostegno

X3DIVIDICAMPATAINIT, inizializza la dialog X3DIVIDICAMPATA

X3DIVIDICAMPATA, gestisce i casi di divisione della campata.

Sono state create le seguenti relazioni DB per permettere la corretta visualizzazione e gestione della dialog:

X3DIVIDICAMPATA, Relazione per la dialog custom “Bonifica campate doppie”

X3UNISCISOSTEGNO, Relazione per la dialog custom “Bonifica sostegni singoli”

Sono state create le seguenti azioni per permettere l’avvio della funzionalità, legate all’automation script:

X3DIVIDICAMPATA

X3UNISCISOSTEGNO

Infine, sono stati creati i seguenti domini di tipo table:

X3DIVIDICAMP

X3UNISCISOST_A

X3UNISCISOST_B

# Anagrafica Rete 2

Al fine di permettere la gestione delle anagrafiche di Rete 2, sono stati modificati i seguenti oggetti:

Domini ALN:

X3ZZSOCP, dominio utilizzato per censire le Società Proprietarie

X3BUKRS, dominio utilizzato per censire la Società

Domini Table:

X3GEWRK, modificata condizione per gestire valori Centro lavoro responsabile associati alla società Rete 2

X3INGRP, modificata condizione per gestire valori Gruppo Responsabile 1° livello associati alla società Rete 2 (Unità impianti)

X3INGRP_2L, modificata condizione per gestire valori Gruppo Responsabile 2° livello Linee associati alla società Rete 2 (Unità impianti)

Al fine di gestire la profilazione e garantire le regole di visibilità per gli utenti di Rete2, è stato creato il gruppo di persona padre 6400 e il sottogruppo RT2 in cui verranno inseriti gli utenti che dovranno gestire l’anagrafica Rete 2.

# Anagrafica TernaGora

Al fine di permettere la gestione delle anagrafiche di TernaGora, sono stati modificati i seguenti oggetti:

Domini ALN:

X3ZZSOCP, dominio utilizzato per censire le Società Proprietarie. Nel caso specifico è stato inserito il valore TR30.

X3BUKRS, dominio utilizzato per censire la Società. Nel caso specifico è stato inserito il valore TR30

Domini Table:

X3GEWRK, modificata condizione per gestire valori Centro lavoro responsabile

X3INGRP, modificata condizione per gestire valori Gruppo Responsabile 1° livello (Unità impianti)

X3INGRP_2L, modificata condizione per gestire valori Gruppo Responsabile 2° livello Linee (Unità impianti)

Al fine di gestire la profilazione e garantire le regole di visibilità per gli utenti di TernaGora, è stato creato il gruppo di persona padre 3000 e il sottogruppo GTG, in cui verranno inseriti gli utenti che dovranno gestire l’anagrafica TernaGora.
