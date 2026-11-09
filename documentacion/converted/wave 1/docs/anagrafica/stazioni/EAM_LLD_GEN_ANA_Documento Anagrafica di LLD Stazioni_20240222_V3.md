# EAM_LLD_GEN_ANA_Documento Anagrafica di LLD Stazioni_20240222_V3

|  | Disegno di Basso Livello (LLD) dell’Anagrafica |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Relazione: | DISEGNO DI BASSO LIVELLO (LLD) DELL’ANAGRAFICA |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | RTC | 22/02/2024 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

INDICE

1Introduzione3

1.1Glossario4

1.2Tabelle del DB del Manage7

1.3Documenti citati7

2Database Configuration8

2.1Oggetti Standard e attributi Standard (senza modifiche)9

2.2Oggetti Standard e attributi Custom11

2.3Oggetti Standard e attributi Standard (con modifiche)12

2.4Oggetti Custom12

2.5Relazioni di Database12

2.6Messaggi14

3Domini15

3.1Domini NUM16

3.2Domini ALN16

3.3Domini di tipo tabella16

3.4Domini Synonym17

4Application Designer18

4.1Stazioni19

4.1.1Tab “Stazione”20

4.1.1.1Primo livello asset20

4.1.1.2Secondo livello stazione21

4.1.1.3Terzo livello Stazione23

4.1.1.4Quarto livello stazione24

4.1.2Tab “Smontati”25

4.1.3Tab “Dati Stazione”25

4.1.4Tab “Asset di stazione in relazione”26

4.1.5Tab “Linee in relazione”27

4.1.6Tab “Indirizzo di servizio”28

4.2Gruppi di persone Terna (X3PERSONGR)28

4.3Dispositivi e componenti (X3DISPECOMP)30

4.3.1Tab “Equipment”31

4.3.1.1Prima sezione Equipment31

4.3.1.2Seconda sezione Equipment33

4.3.1.3Terza sezione Equipment34

4.3.2Tab “Specifiche”35

4.3.2.1Prima sezione “Asset”36

4.3.3Tab “Collegamenti”37

4.3.3.1Prima sezione “Equipment aggregati”37

4.3.3.2Secondasezione “Equipment di riferimento”38

4.3.4Tab “Ricambi”38

4.3.5Tab “Lavoro”39

4.3.6Tab “Indirizzo di servizio”41

4.4Funzione elemento / Fase (X3TRASCODIFICAPOSNR)42

4.5Costruttore componente / Tipo (X3TYPBZ)43

5Detailed processing44

5.1Automation Scripts44

5.2Azioni46

5.3Conditional expression manager46

5.4Sigoption49

5.5Relazioni50

6Gruppi di protezione50

6.1Global Restriction51

7Gruppi di Persone52

INDICE DELLE TABELLE

Tabella 1 - Oggetti Standard con Attributi Standard (senza modifiche) 11

Tabella 2 - Oggetti Standard con Attributi Custom 12

Tabella 3 - Oggetti Standard con Attributi Standard modificati 13

Tabella 4 - Relazioni Custom 15

Tabella 5 -Messaggi 16

Tabella 6 - Domini Synonym 18

Tabella 7 - Proprietà Sezione “Asset di Secondo Livello” 21

Tabella 8 - Campi e Proprietà “Specifiche Tecniche” 23

Tabella 9 - Proprietà Sezione “Terzo Livello Stazione” 23

Tabella 10 - Proprietà Sezione “Quarto Livello Stazione” 24

Tabella 11 - Proprietà Sezione “Asset di stazione in relazione” 26

Tabella 12 - Proprietà Sezione “Linee in Relazione” 27

Tabella 13 - Automation Scripts 32

Tabella 14 - Azioni 32

Tabella 15 - Conditional Expressions 35

Tabella 16 – Sigoption 36

Tabella 17 - Relazioni 36

Tabella 18 - Gruppi di Protezione 37

Tabella 19 – Global Restriction 38

Tabella 20 - Gruppi di persone "UI" 39

Tabella 21 - Gruppi di persone "DT" 39

INDICE DELLE FIGURE

Figura 1 - Schermata “Stazioni” 20

Figura 2 - Schermata “Primo Livello Stazione” 22

Figura 3 - Sezione “Asset di Secondo Livello” 23

Figura 4 - Sezione “Specifiche Asset” 23

Figura 5 - Sezione “Asset di secondo livello” 24

Figura 6 - Sezione “Terzo livello stazione” 24

Figura 7 - Sezione “Quarto Livello Stazione” 25

Figura 8 - Schermata “Asset smontati” 26

Figura 9 - Schermata “Specifiche Stazione” 26

Figura 10 - Schermata “Asset di stazione in Relazione” 27

Figura 11 - Schermata “Linee in Relazione” 28

Figura 12 - Schermata “Informazioni di servizio” 29

Figura 13 - Schermata “Gruppi di Persone Terna” 30

Figura 14 - Dettaglio “Gruppo di Persone Terna” 31

# Introduzione

Il presente documento ha l’obiettivo di indirizzare le tematiche, già introdotte nel documento di HLD EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.verso un design più dettagliato e specifico, orientato all’implementazione effettiva del software. Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni sviluppate sulla piattaforma EAM di Terna (ambiente Dev) suddivise per applicazioni principali MAS.

Nel dettaglio:

Database Configuration, consente di creare, modificare e gestire tabelle, attributi, indici e relazioni all'interno del database di Maximo. Qui è possibile definire la struttura del database e configurare i dati in modo da adattarsi alle specifiche esigenze dell'organizzazione.

Domini, permette di creare e modificare elenchi di valori di diversi tipi, come ALN (Alfanumerico), NUM (Numerico), TABLE (Tabella) e SYNONIM (Sinonimo). Questi elenchi vengono utilizzati per definire i valori possibili per determinati campi all'interno delle applicazioni di Maximo.

Application Designer, consente di creare e personalizzare l'interfaccia grafica delle applicazioni presenti in Maximo. È possibile aggiungere tabelle, sezioni e campi per adattare l'aspetto e il comportamento delle applicazioni in base alle specifiche esigenze aziendali.

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
| X3PERSONGR | Applicazione Custom per la gestione dei Gruppi di Persona di Terna (oggetto principale Persongroup) |  |
| SIGOPTION | Configurazioni di sicurezza che definiscono privilegi di accesso e le azioni consentite agli utenti o ai gruppi di utenti all'interno del sistema. Vengono create all’interno dell’Application designer e possono essere applicate a campi o azioni |  |
| GLOBAL RESTRICTION | Regole di sicurezza utilizzate per controllare l'accesso a determinate funzionalità, dati o azioni all'interno del MAS |  |
| WORKFLOW | Insieme di processi predefiniti e sequenziali che gestiscono il flusso di lavoro e le attività aziendali all'interno dell'applicativo. Questi processi possono includere l'assegnazione di compiti, l'approvazione di richieste, etc. |  |
| ESCALATION | Meccanismo di automazione che permette di gestire il tempo di risposta di determinate azioni e attività |  |
| Dev | Ambiente Sviluppo (Development) |  |
| Data Source ID | Codice identificativo della sorgente del dato |  |
| Parent Data Source ID | Codice identificativo della sorgente del dato del livello gerarchico superiore |  |

## Tabelle del DB del Manage

| Tabella | Spiegazione | Nota |
| --- | --- | --- |
| ASSET | Tabella principale dell’MBO Asset. |  |
| ASSETSTATUS | Tabella con cardinalità 1:n rispetto ad ogni asset, dove sono registrati i cambi stato di ogni asset. |  |
| ASSETSPEC | Tabella delle specifiche tecniche degli asset. Viene popolata, con cardinalità 1:n rispetto all’asset, nel momento in cui l’asset è associato ad una classificazione. In questa tabella sono anche salvati i valori di ogni singolo attributo, in relazione a ciascun’asset. |  |
| SERVICEADDRESS | Tabella dove sono registrati tutti gli indirizzi associabili a vari elementi gestiti in Manage, tra cui gli asset. |  |
| CLASSSTRUCTURE | Tabella principale di gestione delle gerarchie di modelli di classificazioni, associabili a varie entità del Manage, tra cui gli asset e gli item. |  |
| ASSETATTRIBUTE | Tabella dell’anagrafica di attributi utilizzati nelle classificazioni. |  |
| ITEM | Tabella degli item, utilizzati nel progetto Terna per gestire i componenti, che diventeranno asset di 2°, 3° e 4° livello. |  |
| CLASSSPEC | Tabella utilizzata per memorizzare le specifiche o le proprietà delle classificazioni, ovvero permette di definire ulteriori dettagli alle classificazioni, quali attributi, domini, etc. |  |

## Documenti citati

I documenti citati sono consultabili all’interno del seguente repository Teams DOCUMENTI CORRELATI.

| DOC ID | NOME FILE |
| --- | --- |
| Doc.1 | EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2..docx |
| Doc.2 | EAM_LLD_GEN_ANA_Domini Stazioni_20240222_V1.0.xlsx |
| Doc.3 | EAM_LLD_GEN_ANA_Oggetti Custom Stazioni_20240222_V1.0.xlsx |

# Database Configuration

L’applicazione Database Configuration permette di creare e modificare oggetti, attributi e relazioni presenti all’interno del database del MAS.

Nei seguenti paragrafi vengono illustrati gli oggetti Custom creati sul database, gli oggetti Standard su cui sono state apportate delle modifiche (e.g. lunghezza attributo) e creati nuovi attributi (e.g. ASSET.X3BURKS) e gli oggetti standard utilizzati ai fini della rappresentazione dell’anagrafica.

Nel dettaglio, le colonne indicano:

Oggetto: nome univoco della tabella;

Attributo: nome univoco del campo presente in tabella;

Descrizione: Descrizione Tabella/Attributo;

Dominio: Nome del dominio associato al campo (vedi capitolo 3 Domini);

Lunghezza: Lunghezza del campo;

Tipo: Tipologia del campo (es. ALN);

SameAsAttribute e SameAsObject: SameAsAttribute indica l'attributo principale che controlla il maxtype, la lunghezza e la scala dell’attributo soggetto a modifica.SameAsObject, invece, indica la tabella in cui è presente l'attributo principale. Nel momento in cui viene modificato l'attributo principale, automaticamente le modifiche si rifletteranno su tutti gli attributi che hanno lo stesso SameAsAttribute. Ad esempio, l’attributo PARENT della tabella ASSET ha come SameAsAttribute ASSETNUM e come SameAsObject ASSET, in automatico esso eredita tutte le caratteristiche dell’attributo ASSETNUM.

Persistente: L'attributo e i dati ad esso associati sono mantenuti permanentemente all'interno del database di Maximo. Quando un attributo ha il valore "1", ciò indica che il campo è persistente e le informazioni ad esso associate vengono salvate nel database.

Tipo di ricerca: indica il tipo di ricerca prevista su questo attributo.

## Oggetti Standard e attributi Standard (senza modifiche)

Di seguito, vengono elencati gli oggetti standard e i relativi attributi standard utilizzati per la rappresentazione dell’anagrafica, senza necessità di modifiche.

| OGGETTO | DESCR. | ATTRIBUTO | DESCR. | DOMINIO | LUNGHEZZA | TIPO | PERSISTENTE | SAMEASATTRIBUTE | SAMEASOBJECT | TIPO DI RICERCA |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| ASSET | Tabella Degli Asset | STATUS | Stato | LOCASSETSTATUS | 20 | ALN | 1 |  |  | WILDCARD |
| ASSET | Tabella Degli Asset | ASSETNUM | Numero Asset |  | 25 | UPPER | 1 |  |  | WILDCARD |
| ASSET | Tabella Degli Asset | DESCRIPTION | Descrizione |  | 200 | ALN | 1 | DESCRIPTION | ITEM | TEXT |
| ASSET | Tabella Degli Asset | ORGID | Organizzazione |  | 8 | UPPER |  | ORGID | ORGANIZATION | WILDCARD |
| ASSET | Tabella Degli Asset | PARENT | Padre dell’Asset |  | 25 | UPPER |  | ASSETNUM | ASSET | WILDCARD |
| ASSET | Tabella Degli Asset | SITEID | Sito |  | 8 | UPPER | 1 | SITEID | SITE | WILDCARD |
| ASSET | Tabella Degli Asset | CLUSSSTRUCTUREID | Identificativo della classificazione |  | 20 | UPPER | 1 | CLASSSTRUCTUREID | CLASSSTRUCTURE | WILDCARD |
| ASSETLOCRELATION | Asset relazionati | SOURCEASSETNUM | Asset di origine (Padre) |  | 25 | UPPER | 1 | ASSETNUM | ASSET | EXACT |
| ASSETLOCRELATION | Asset relazionati | TARGETASSETNUM | Asset di destinazione (Figli) |  | 25 | UPPER | 1 | ASSETNUM | ASSET | EXACT |
| ASSETSPEC | Valori di un attributo specifici per un Asset | ALNVALUE | Valore Alfanumerico |  | 254 | ALN | 1 | VALUE | ALNDOMAIN | WILDCARD |
| ASSETSPEC | Valori di un attributo specifici per un Asset | ASSETATTRID | Identificativo Attributo |  | 50 | UPPER | 1 | ASSETATTRID | ASSETATTRIBUTE | WILDCARD |
| ASSETSPEC | Valori di un attributo specifici per un Asset | CLASSSTRUCTUREID | Identificativo Classificaizone |  | 20 | UPPER | 1 | CLASSSTRUCTUREID | CLASSSTRUCTURE | EXACT |
| ASSETSPEC | Valori di un attributo specifici per un Asset | TABLEVALUE | Il valore della tabella specificata nel DOMAIN in cui il tipo di dominio e il tipo di dato sono TABLE. |  | 254 | ALN | 1 |  |  | WILDCARD |
| ASSETSPEC | Valori di un attributo specifici per un Asset | NUMVALUE | Valore Numerico |  | 30 | DECIMAL | 1 | VALUE | NUMERICDOMAIN | EXACT |
| CLASSSTRUCTURE | Tabella delle classificazioni | CLASSSTRUCTUREID | Identificativo Classificazione |  | 20 | UPPER | 1 |  |  | WILDCARD |
| CLASSSTRUCTURE | Tabella delle classificazioni | HIERARCHYPATH | Tabella della gerarchia di classificazione |  | 254 | ALN |  |  |  | NONE |
| CLASSSPEC | Tabella attributi di classificazione | ASSETATTRID | Identificativo Attributo |  | 50 | UPPER | 1 | ASSETATTRIBUTE | ASSETATTRID | WILDCARD |
| CLASSSPEC | Tabella attributi di classificazione | SECTION | Sezione per un gruppo di attributi |  | 10 | UPPER |  |  |  | WILDCARD |
| CLASSSPEC | Tabella attributi di classificazione | DATATYPE | Tipo Dato |  | 50 | UPPER | 1 |  |  | WILDCARD |
| CLASSSPEC | Tabella attributi di classificazione | MEASUREUNITID | Unità di misura |  | 16 | UPPER |  | MEASUREUNIT | MEASUREUNITID | WILDCARD |
| ASSETATTRIBUTE | Tabella degli attributi | ASSETATTRID | Identificativo Attributo |  | 50 | UPPER | 1 | ASSETATTRIBUTE | ASSETATTRID | WILDCARD |
| ASSETATTRIBUTE | Tabella degli attributi | Description | Descrizione |  | 100 | ALN | 1 |  |  | WILDCARD |
| ASSETATTRIBUTE | Tabella degli attributi | DOMAINID | Id Dominio |  | 18 | UPPER | 1 | DOMAINID | MAXDOMAIN | WILDCARD |
| ASSETATTRIBUTE | Tabella degli attributi | DATATYPE | Tipo Dato |  | 8 | UPPER | 1 |  |  | WILDCARD |
| ASSETATTRIBUTE | Tabella degli attributi | MEASUREUNITID | Unità di misura |  | 16 | UPPER | 1 | MEASUREUNIT | MEASUREUNITID | WILDCARD |

Tabella 1 - Oggetti Standard con Attributi Standard (senza modifiche)

## Oggetti Standard e attributi Custom

In questo paragrafo vengono riportati gli oggetti Standard (tabelle già esistenti su MAS), i relativi attributi creati (indicati con il prefisso “X3”), le rispettive configurazioni ed i domini applicati agli attributi (per i domini si veda capitolo Domini).

| OGGETTO | ATTRIBUTO | DESCR. | DOMINIO | LUNG. | TIPO | PERSISTENTE | SAMEASATTRIBUTE | SAMEASOBJECT | TIPO DI RICERCA | TIPO DI RICERCA |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| ASSET | X3BUKRS | Società | X3BURKS | 4 | ALN | 1 |  |  | WILDCARD |  |
| ASSET | X3EQFNR | Campo sort |  | 20 | ALN | 1 |  |  | WILDCARD | WILDCARD |
| ASSET | X3GEWRK | Centro di lavoro responsabile | X3GEWRK | 8 | ALN | 1 | GEWRK | X3GEWRK | WILDCARD |  |
| ASSET | X3INGRP | Gruppo Responsabile Pianificazione della Manutenzione | X3INGRP | 3 | ALN | 1 |  |  | WILDCARD | WILDCARD |
| ASSET | X3KOKRS | Controlling area | X3KORKS | 4 | ALN | 1 |  |  | WILDCARD |  |
| ASSET | X3KOSTL | Centro di Costo |  | 10 | ALN | 1 |  |  | WILDCARD | WILDCARD |
| ASSET | X3MSGRP | Impianto |  | 8 | ALN | 1 |  |  | WILDCARD |  |
| ASSET | X3ROOTASSETNUM | X3rootassetnum |  | 25 | UPPER | 1 | ASSETNUM | ASSET | WILDCARD | WILDCARD |
| ASSET | X3SAPID | Codice sede tecnica |  | 30 | UPPER | 1 |  |  | WILDCARD |  |
| ASSET | X3SAPIDEQ | X3SAPID Equipment |  | 30 | UPPER | 1 |  |  | WILDCARD | WILDCARD |
| ASSET | X3SAPIDSTAZ | SAPID |  | 30 | UPPER | 0 | X3SAPID | ASSET | NONE |  |
| ASSET | X3SWERK | Divisione di Ubicazione | X3SWERK | 4 | ALN | 1 |  |  | WILDCARD | WILDCARD |
| ASSET | X3ZZSOCP | Società Proprietaria | X3ZZSOCP | 4 | ALN | 1 |  |  | WILDCARD |  |
| ASSETATTRIBUTE | X3GRUPPO_RESP | Gruppo responsabile | X3GRUPPO_RESP | 8 | UPPER | 1 | PERSONGROUP | PERSONGROUP | WILDCARD | WILDCARD |
| PERSONGROUP | X3PARENTPERSONGROUP | Gruppo di persone padre | X3PARENTPERSONGROU | 8 | UPPER | 1 | PERSONGROUP | PERSONGROUP | WILDCARD |  |
| PERSONGROUP |  |  |  |  |  |  |  |  |  |  |
| PERSONGROUP |  |  |  |  |  |  |  |  |  |  |

Tabella 2 - Oggetti Standard con Attributi Custom

## Oggetti Standard e attributi Standard (con modifiche)

Nella seguente tabella vengono elencati tutti gli Oggetti Standard e i relativi attributi Standard su cui sono state apportate delle modifiche, in particolare alla lunghezza, ai fini dell’integrazione SAP-EAM.

| OGGETTO (Tabella) | ATTRIBUTO | DOMINIO | LUNGHEZZA | LUNGHEZZA | TIPO | DESCRIZIONE | SAMEASATTRIBUTE | SAMEASOBJECT | TIPO DI RICERCA |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| OGGETTO (Tabella) | ATTRIBUTO | DOMINIO | Da: | A: | TIPO | DESCRIZIONE | SAMEASATTRIBUTE | SAMEASOBJECT | TIPO DI RICERCA |
| ASSET | DESCRIPTION |  | 100 | 40 | ALN | Descrizione dell’asset | DESCRIPTION | ITEM | TEXT |
| ASSETATTRIBUTE | ASSETATTRID |  | 16 | 50 | UPPER | Identificativo attributo dell’asset |  |  | WILDCARD |
| CLASSIFICATION | DESCRIPTION |  | 100 | 150 | ALN | Descrizione della classificazione |  |  | WILDCARD |
| SERVICEADDRESS | CITY |  | 36 | 40 | UPPER | Città |  |  | WILDCARD |
| SERVICEADDRESS | COUNTRY | COUNTRYCODES | 3 | 3 | UPPER | Paese |  |  | WILDCARD |
| SERVICEADDRESS | DESCRIPTION |  | 50 | 40 | ALN | Descrizione |  |  | WILDCARD |
| SERVICEADDRESS | POSTALCODE |  | 12 | 10 | ALN | Codice postale |  |  | WILDCARD |
| SERVICEADDRESS | REGIONDISTRICT |  | 36 | 3 | ALN | Regione |  |  | WILDCARD |
| SERVICEADDRESS | STREETADDRESS |  | 115 | 60 | ALN | Indirizzo |  |  | WILDCARD |

Tabella 3 - Oggetti Standard con Attributi Standard modificati

## Oggetti Custom

All’interno del file EAM_LLD_GEN_ANA_Oggetti Custom Stazioni_20240222_V1.0, consultabile nell’apposito repository Anagrafica > DOCUMENTI CORRELATI viene riportato l’elenco degli oggetti Custom (nuove tabelle, identificate con il prefisso “X3”), i relativi attributi creati (indicati con il prefisso “X3”), le rispettive configurazioni ed i domini applicati agli attributi (per i domini si veda capitolo Domini). Vista la numerosità degli Oggetti (tabelle e attributi), i valori sono stati inseriti all’interno del file Excel sopra indicato che verrà aggiornato all’occorrenza.

## Relazioni di Database

Le relazioni permettono di legare più tabelle al fine di recuperare specifici dati; si tratta di clausole “WHERE" all’interno di un’istruzione SQL. La tabella di destinazione, chiamata "Figlio", è l'oggetto su cui viene eseguita la clausola "WHERE" e contiene i dati correlati che possono essere recuperati tramite la relazione, mentre la tabella di partenza è chiamata "Padre" e detiene la chiave primaria, un identificatore univoco per ogni record, che permette di stabilire il collegamento con altre tabelle (le relazioni sono state principalmente utilizzate per la navigazione e il recupero dei dati nell’applicazione Stazioni, rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2. paragrafo 2.3).

Nel seguente paragrafo si riportano le relazioni custom create (indicate con il prefisso “X3”) all’interno del Database Configuration. Per ogni relazione, viene riportata la clausola SQL, la tabella di partenza (Padre) e la tabella di destinazione (Figlio):

| NOME | DESCRIZIONE | PADRE | FIGLIO | WHERECLAUSE | WHERECLAUSE |
| --- | --- | --- | --- | --- | --- |
| X3ALNDOMVALSOC |  | ASSET | ALNDOMAIN | domainid='X3BUKRS' and value=:X3BUKRS |  |
|  |  |  |  |  |  |
| X3ASSETSMONTATI | Relazione che permette di visualizzare nel tab “Smontati” gli asset spostati e montati | ASSET | ASSET | parent=:assetnum and siteid=:siteid and classstructureid in (select classstructureid from classancestor where ancestorclassid='DISPOSITIVI E COMPONENTI') |  |
|  |  | E |  | :cl |  |
| X3CREATEASSETSTAZ | Relazione con oggetto non persistente per la creazione di asset di Stazione | ASSET | X3CREATEASSETSTAZ |  |  |
| X3PERSONGROUP | Relazione per recuperare su asset le UI dalla tabella PERSONGROUP | ASSET | PERSONGROUP | :X3SWERK = PERSONGROUP |  |
| X3SPOSTAASSET | Relazione con oggetto non persistente per lo spostamento di asset di Stazione | ASSET | X3STZMOVEASSET | :assetnum=assetnum and :siteid=siteid | :assetnum=assetnum and :siteid=siteid |
| X3STAZCHILDREN | Relazione per recuperare gli asset diversi da DISPOSITIVI E COMPONENTI | ASSET | ASSET | parent = :assetnum and siteid = :siteid and classstructureid not in (select classstructureid from classancestor where ancestorclassid='DISPOSITIVI E COMPONENTI') |  |
| X3STAZSTAL | Relazione per visualizzare gli asset nel tab “Asset di stazione in relazione” | ASSET | ASSETLOCRELATION | sourceassetnum in (select assetnum from assetancestor where ancestor=:assetnum) or targetassetnum in (select assetnum from assetancestor where ancestor=:assetnum) and assetrelationnum='STAZSTAL' | sourceassetnum in (select assetnum from assetancestor where ancestor=:assetnum) or targetassetnum in (select assetnum from assetancestor where ancestor=:assetnum) and assetrelationnum='STAZSTAL' |
| X3STZMAXPARENT | Relazione utilizzata per visualizzare gli asset per i diversi livelli nell’azione “Sposta Asset” | ASSET | ASSET | assetnum in (select ancestor from assetancestor where assetnum=:assetnum and siteid=:siteid and hierarchylevels NOT IN('2') AND hierarchylevels=(SELECT max(HIERARCHYLEVELS) FROM assetancestor WHERE assetnum=:assetnum and siteid=:siteid GROUP BY assetnum)) |  |
| X3STZSPOSTAASSET | Relazione utilizzata per l’oggetto non persistente X3STZMOVEASSET | X3STZMOVEASSET | ASSET | :assetnum=assetnum and :siteid=siteid |  |
| X3STZ_CLASSIFICATIONDESC | Relazione utilizzata per visualizzare le classificazioni padre (fino al 3 livello) | X3TABVERITASTAZIONI | CLASSSTRUCTURE | classstructureid=:classe_padreid | classstructureid=:classe_padreid |
| X3STZ_CLASSIFICATIONDESCFIGLIO | Relazione che permette di collegare la tabella di verità con la tabella delle classificazioni, al fine di permettere la corretta visualizzazione di queste ultime | X3TABVERITASTAZIONI | CLASSSTRUCTURE | classstructureid=:classe_figlioid | classstructureid=:classe_figlioid |
|  |  |  | I |  |  |

Tabella 4 - Relazioni Custom

## Messaggi

All’interno dell’applicazione Database Configuration è possibile visualizzare e creare messaggi di sistema, vale a dire notifiche o avvisi personalizzati che vengono visualizzati dagli utenti durante l'utilizzo del sistema per fornire informazioni specifiche, avvisi o conferme su determinate azioni o eventi.

Esistono 3 tipi di messaggi:

Info, utilizzato per fornire informazioni agli utenti senza richiedere alcuna azione specifica. È un messaggio puramente informativo che mostra dettagli o notifiche importanti agli utenti senza interrompere il flusso di lavoro.

Warning, utilizzato per indicare agli utenti una situazione rilevante o critica. Può richiedere un'azione da parte dell'utente, ma non si tratta di un errore grave che impedisce l'avanzamento delle operazioni.

Error, utilizzato per segnalare un errore o una situazione errata che impedisce l'esecuzione dell'azione corrente. È necessaria un’azione da parte dell’utente per procedere.

Di seguito, i messaggi di tipo Error presenti su MAS e richiamabili dagli script indicati nella tabella sottostante (rif. 5.1 Automation Scripts); message Group indica il gruppo di messaggi, message Key è l’iD univoco del messaggio, message id indica il formato (se si tratta di un messaggio personalizzato avremo BMX[CC][NNNN][T] dove [CC] è un codice di due lettere che identifica la personalizzazione, [NNNN] è un numero di quattro cifre e [T] è il tipo di messaggio e può essere E (Errore), W (avviso) o I (Info)),il valore è testo che verrà visualizzato dall’utente.

| Message Group | Message Key | Value | Message ID | Script | Scenario |
| --- | --- | --- | --- | --- | --- |
| x3renamesapid_exists | x3renamesapid | Il SAPID inserito è già esistente, si prega di verificare. | BMXZZ7149E |  |  |
| ErrorMsg | x3terna | {0} | BMXZZ7150E |  | Messaggio generico di errore previsto su EAM |
| InfoMsg | x3terna | {0} | BMXZZ7157I |  | Messaggio generico di informazioni previsto su EAM |

Tabella 5 -Messaggi

# Domini

Nel MAS con il termine dominio si intende un elenco di valori che permette di valorizzare determinati attributi (di tabella e di classificazione) presenti nelle diverse applicazioni (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.2.1).

I domini sono divisi in 4 diverse tipologie, ovvero alfanumerico, numerico, di tipo tabella e synonym, come di seguito:

Dominio alfanumerico: Elenco di valori statico che può gestire tre tipi di dati alfanumerici: ALN, UPPER e LOWER (e.g. Dominio X3STZ_MATE_COLLE che contiene all’interno i valori A – Alluminio, R-Rame). La creazione di un nuovo dominio di questo tipo comporta il popolamento di una struttura dati già predisposta nel MAS, chiamata ALNDOMAIN che gestisce:

Il nome del dominio

Il valore del dominio

La descrizione del valore del dominio

Dominio di tipo tabella: Permette di prendere dati da qualsiasi oggetto del MAS, sia standard, come ad esempio PERSONGROUP che custom, come X3GEWRK. La lista dei valori che sarà visibile a sistema viene definita tramite una condizione SQL, così come la validazione del dato inserito nel campo di destinazione del dominio.

Dominio numerico: Elenco statico di valori numerici.

Dominio Synonym: Domini speciali di sistema utilizzati principalmente sui campi “Stato” e “Tipo”. Questi domini sono formati da valori interni che non possono essere modificati ma a cui può essere legato un sinonimo. Ad esempio, il dominio LOCASSETSTATUS (dominio applicato al campo stato Asset) presenta valore interno DECOMMISSIONED e sinonimo CEAL (stato finale che vedrà l’utente)

Nei paragrafi successivi verranno illustrate e dettagliate tutte le tipologie di Domini creati su MAS. Per le Stazioni, è stato utilizzato il prefisso “X3STZ” per identificare i domini di tipo ALN, NUMERIC e TABLE.

La lista completa dei domini (in particolare domini NUM, domini ALN e domini TABLE), vista la numerosità degli oggetti, è consultabile nel file Domini presente all’interno del repository DOCUMENTI CORRELATI. Il file è come di seguito strutturato:

Introduzione: Overview sui domini e sulla struttura del file;

Sheet Domini Num: elenco dei domini di tipo numerico;

Sheet Domini Aln: elenco dei domini di tipo alfanumerico;

Sheet Domini Table: elenco dei domini di tipo tabella;

Inoltre, all’interno del file Domini, sono presenti i seguenti sheet che non introducono nuovi domini rispetto a quelli precedentemente menzionati, ma è possibile visualizzare la loro associazione all’interno degli attributi di classificazione e attributi del Database:

Sheet Domini Attributi Class: per ogni attributo di classificazione indica il relativo dominio associato

Sheet Domini Attributi DB: indica gli attributi di database in cui sono presenti i domini.

## Domini NUM

All’interno del file Excel EAM_LLD_GEN_ANA_Domini Stazioni_20240222_V.0 (sheet Domini Num) nel repository DOCUMENTI CORRELATI, è consultabile l’elenco dei domini di tipo numerico creati in ambiente MAS DEV di Terna, con relativo codice identificativo (Dominio), descrizione, valori presenti all’interno del dominio (colonne Valori Dominio- Descrizione Dominio), tipologia del dominio e lunghezza.

Questi domini sono stati applicati agli attributi di classificazione e di database come indicato nel file excel EAM_LLD_GEN_ANA_Domini Stazioni_20240222_V.0 (sheet Domini Attributi Class e Domini Attributi DB).

## Domini ALN

All’interno del file EAM_LLD_GEN_ANA_Domini Stazioni_20240222_V.0 (sheet Domini Aln), nel repository DOCUMENTI CORRELATI, vengono elencati i domini di tipo alfanumerico creati sull’ambiente MAS DEV di Terna, con relativo codice identificativo (Dominio), descrizione, valori presenti all’interno del dominio, tipologia e lunghezza (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.2.1).

Questi domini sono stati applicati agli attributi di classificazione e di database come indicato nel file excel EAM_LLD_GEN_ANA_Domini Stazioni_20240222_V.0 (sheet Domini Attributi Class e Domini Attributi DB).

## Domini di tipo tabella

All’interno del file EAM_LLD_GEN_ANA_Domini Stazioni_20240222_V.0 (sheet Domini Table), nel repository DOCUMENTI CORRELATI, vengono elencati i domini di tipo tabella creati su MAS DEV di Terna, con relativo codice identificativo (Dominio), descrizione, validazione e clausola where.

Questi domini sono stati applicati agli attributi di classificazione e di database come indicato nel file excel EAM_LLD_GEN_ANA_Domini Stazioni_20240222_V.0 (sheet Domini Attributi Class e Domini Attributi DB).

## Domini Synonym

Nel contesto Terna si utilizza la tipologia di dominio Synonym (ad ogni valore standard dello stato possono essere aggiunti più sinonimi/alias) esclusivamente per gestire la lista degli stati disponibili per Asset. Per la gestione di questi stati viene utilizzato il dominio standard LOCASSETSTATUS.

Questo dominio viene condiviso sia per le Linee che per le Stazioni.

Nel seguente paragrafo viene riportata la lista degli stati disponibili per gli Asset secondo quanto espresso nel rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 3.3.2.

In tabella viene indicato il valore interno MAS, il sinonimo assegnato (valore), la descrizione, se si tratta di uno stato di default e la condizione associata che regola la visibilità a sistema.

| Valore Interno | Valore | Descrizione | Default | Condizione |
| --- | --- | --- | --- | --- |
| DECOMMISSIONED | CEAL | Ceduta / Alienata | Y | X3STATL12S1 |
| DECOMMISSIONED | DELE | Cancellato | N | X3STATL1S1 |
| DECOMMISSIONED | DEMO | Demolita / Dismessa | N | X3STATL12S1 |
| DECOMMISSIONED | DIST | Distrutto /Alienato | N | X3STATL4S4 |
| NOT READY | DATT | Da Attivare | Y | X3STATODATT |
| NOT READY | INAT | Inattivo | N | X3STATL1S1 |
| OPERATING | ATTV | Attiva | Y | X3GSTATS3 / X3STATL12S1 |
| OPERATING | EFDI | Installato, efficente, disponibile | N | X3STATS4 |
| OPERATING | EFFE | Installato, efficente futuro esercizio | N | X3STATL4S4 |
| OPERATING | EFPR | Installato, efficente prenotato | N | X3STATS4 |
| OPERATING | EFSC | Installato, efficente scorta | N | X3STATS4 |
| OPERATING | ESER | Installato, in esercizio | N | X3STATL4S4 |
| OPERATING | GUAS | Installato, guasto | N | X3STATL4S4 |
| OPERATING | NADI | Non attiva, disponibile | N | X3GSTATS3 / X3STATL12S1 |
| OPERATING | NAND | Non Attiva, non disponibile | N | X3GSTATS3 / X3STATL12S1 |
| OPERATING | NIED | Non Installato Efficiente Disponibile | N | X3STATS4 |
| OPERATING | NIEO | Non installato, efficente obsoleto | N | X3STATS4 |
| OPERATING | NIEP | Non installato, efficente prenotato | N | X3STATS4 |
| OPERATING | NIES | Non installato, efficente scorta | N | X3STATS4 |
| OPERATING | NIGU | Non installato, guasto | N | X3STATS4 |
| OPERATING | RIPR | In riparazione/revisione | N | X3STATL4S4 |

Tabella 6 - Domini Synonym

# Application Designer

Nel Manage, l’Application Designer è uno strumento che consente agli utenti di personalizzare e configurare l'aspetto e il comportamento delle applicazioni. È un componente chiave del framework di personalizzazione e fornisce un'interfaccia grafica che permette, ad esempio, di aggiungere nuovi campi, tabelle, liste valori, condizioni sui campi, etc.

In particolare, per soddisfare quanto espresso nel rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6, sono state create due applicazioni custom:

Applicazione “Stazioni”;

Applicazione “Gruppi di persone Terna”.

## Stazioni

L’applicazione Custom “Stazioni” è un duplicato dell’applicazione standard “Asset” (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.6).

Di seguito, la schermata principale dell’applicazione “Stazioni” in cui è possibile visualizzare ad elenco tutte le Stazioni disponibili a sistema:

Figura 1 - Schermata “Stazioni”

La schermata (Figura 1) presenta in ordine i seguenti campi relativi alla Stazione (Primo livello):

Asset: numero identificativo univoco di un asset. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET

SAP ID: codice sede tecnica SAP dell’asset. Tale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET

Descrizione: descrizione dell’asset. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET

Stato: stato dell’asset. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET

Sede: sede di appartenenza dell’asset. Tale campo richiama l’attributo Standard di tipo alfanumerico SITEID della tabella ASSET

Una volta selezionato un record da elenco, l’applicazione sarà caratterizzata da 6 tab, descritti nei paragrafi successivi.

### Tab “Stazione”

Di seguito, viene illustrato il dettaglio del tab "Stazione", suddiviso in quattro sezioni principali, ognuna dedicata a un diverso livello della Stazione di riferimento (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.6.1).

#### Primo livello asset

Nella prima sezione (Figura 2) vengono riportate le informazioni dell’asset Stazione (1° livello), di seguito descritte (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.6.1).

Stazione: numero identificativo univoco della Stazione. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET.

SAP ID: codice sede tecnica SAP della StazioneTale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET.

Stato: stato della Stazione. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET.

Centro di Costo: codice del centro di costo. Tale campo richiama l’attributo Custom X3KOSTL della tabella ASSET.

Divisione di Ubicazione: codice del Distretto/Dipartimento di appartenenza della Stazione (es: 2601). Tale campo richiama l’attributo Custom di tipo alfanumerico X3SWERK della tabella ASSET.

Unità impianti: contiene la lista delle unità impianti. L’utente può selezionare una UI dall’elenco. Tale campo richiama l’attributo Custom X3INGRP della tabella ASSET.

Controlling Area: Identificativo Controlling Area, ovvero TR01 Terna Italia. Tale campo richiama l’attributo Custom X3KOKRS della tabella ASSET.

Società Proprietaria: identificativo della società proprietaria. Tale campo richiama l’attributo Custom X3ZZSOCP della tabella ASSET.

Società: Indica la Società. Tale campo richiama l’attributo Custom X3BUKRS della tabella ASSET.

Impianto: identificativo dell’impianto. Tale campo richiama l’attributo Custom X3MSGRP della tabella ASSET.

Campo Sort: Campo a testo libero. Tale campo richiama l’attributo Custom X3EQFNR della tabella ASSET.

Centro di lavoro responsabile: Indica il Centro di lavoro responsabile. Tale campo richiama l’attributo Custom X3GEWRK della tabella ASSET.

Accanto ai campi “Stazione” e “Divisione di Ubicazione” è presente un ulteriore campo descrittivo, di seguito i dettagli:

Descrizione “Stazione”: descrizione della stazione. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET.

Descrizione “Divisione di Ubicazione”: descrizione del Dipartimento/Distretto (es: Dipartimento Trasm. Nord Ovest). Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION tramite la relazione X3PERSONGROUP (X3PERSONGROUP.DESCRIPTION).

Figura 2 - Schermata “Primo Livello Stazione”

#### Secondo livello stazione

La seconda sezione “Asset di secondo livello” permette la visualizzazione degli elementi gerarchicamente inferiori al primo livello Stazioni (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.6.1.1).

Le proprietà conferite a questa sezione sono riportate nella tabella sottostante:

| Proprietà | Dettaglio proprietà |
| --- | --- |
| Ordinato per | ASSETNUM |
| Data Source ID | 2level |
| Parent Data Source ID | MAINRECORD |
| Relazione | X3STAZCHILDREN |
| Titolo Attributi | X3SAPID |

Tabella 7 - Proprietà Sezione “Asset di Secondo Livello”

La sezione “Asset di secondo livello” è caratterizzata dalla relazione X3STAZCHILDREN e di seguito si riporta una descrizione delle informazioni presenti nella successiva Figura 3:

SAP ID: codice sede tecnica SAP dell’asset di secondo livelloTale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET.

Asset: numero identificativo univoco dell’asset di secondo livello. Tale campo richiama l’attributo Standard ASSETNUM della tabella ASSET.

Descrizione: descrizione dell’asset di secondo livello. Tale campo richiama l’attributo Standard DESCRIPTION della tabella ASSET.

Classificazione: classificazione dell’asset di secondo livello. Tale campo richiama l’attributo Standard CLASSIFICATIONID tramite la relazione CLASSSTRUCTURE (CLASSSTRUCTURE.CLASSIFICATIONID).

Figura 3 - Sezione “Asset di Secondo Livello”

Cliccando sul simbolo cerchiato in rosso in Figura 3 verrà restituita la seguente schermata, comprensiva di tutte le specifiche tecniche dell’asset di secondo livello:

Figura 4 - Sezione “Specifiche Asset”

La sezione “Specifiche asset” è caratterizzata dalla relazione di database ASSETSPEC e di seguito si riportano le proprietà dei campi presenti in Figura 4:

| CAMPO | DEFAULT LABEL | LABEL | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP |
| --- | --- | --- | --- | --- | --- |
| Asset | Asset |  | ASSETNUM | DESCRIPTION |  |
| Attributo | Attributo |  | ASSETATTRID |  |  |
| Descrizione | Descrizione |  | ASSETATTRIBUTE.DESCRIPTION |  |  |
| Data effettiva modifica | Data modifica attributo | Data effettiva modifica | X3DATAMODIFICA |  | DATELOOKUP |
| Valore ALN | Valore alfanumerico | Valore ALN | ALNVALUE |  |  |
| Valore tabella | Valore tabella |  | TABLEVALUE |  |  |
| Valore numerico | Valore numerico |  | NUMVALUE |  |  |
| Valore Data | Valore data | Valore Data | DATEVALUE |  |  |
| UM | Unità di misura | UM | ASSETATTRIBUTE.MEASUREUNITID |  |  |

Tabella 8 - Campi e Proprietà “Specifiche Tecniche”

#### Terzo livello Stazione

Dalla sezione “Asset di Secondo Livello” è possibile navigare la gerarchia degli Asset e quindi visualizzare gli elementi di terzo livello (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.6.1.2) cliccando sul simbolo cerchiato in rosso in Figura 5:

Figura 5 - Sezione “Asset di secondo livello”

In questo paragrafo viene descritta la schermata relativa al terzo livello della stazione, rappresentato in Figura 6:

Figura 6 - Sezione “Terzo livello stazione”

La sezione “Terzo livello stazione” presenta le seguenti proprietà:

| Proprietà | Dettaglio proprietà |
| --- | --- |
| Ordinato per | classstructureid |
| Data Source ID | 3levelX |
| Parent Data Source ID | 2level |
| Relazione | CHILDREN |

Tabella 9 - Proprietà Sezione “Terzo Livello Stazione”

All’interno della sezione “Terzo livello stazione”, oltre ai campi SAPID, Asset, DescrizioneClassificazione dettagliati nei livelli precedenti, sono presenti le seguenti informazioni:

Unità Impianti: unità impianti dell’asset. Tale campo richiama l’attributo custom X3INGRP della tabella ASSET.

Stato: Stato dell’asset. Tale campo richiama l’attributo standard STATUS della tabella ASSET

Cliccando sul simbolo cerchiato in rosso in Figura 6 verrà restituita la schermata comprensiva di tutte le specifiche tecniche dell’asset di terzo livello, che presenta le medesime proprietà di quella relativa agli asset di secondo livello (Figura 4, Tabella 8).

#### Quarto livello stazione

Una volta selezionato un asset di terzo livello, appaiono in schermata tutti gli asset di quarto livello gerarchicamente sottostanti (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.6.1.3) come mostrato in Figura 7.

Figura 7 - Sezione “Quarto Livello Stazione”

La sezione “Quarto livello Stazione” presenta le seguenti proprietà:

| Proprietà | Dettaglio proprietà |
| --- | --- |
| Data Source ID | 4levelZ |
| Parent Data Source ID | 3levelX |
| Relazione | CHILDREN |
| Titolo Attributi | X3SAPID |

Tabella 10 - Proprietà Sezione “Quarto Livello Stazione”

All’interno della sezione quarto livello stazione, oltre ai campi SAPID, Asset, Descrizione, ClassificazioneStato, dettagliati nei livelli precedenti, sono presenti le seguenti informazioni:

Data di installazione: data installazione dell’asset. Tale campo richiama l’attributo standard INSTALLDATE della tabella ASSET.

Cliccando sul simbolo cerchiato in rosso in Figura 7 verrà restituita la schermata comprensiva di tutte le specifiche tecniche dell’asset di terzo livello, che presenta le medesime proprietà di quella relativa agli asset di secondo livello (Figura 4, Tabella 8).

### Tab “Smontati”

In questa schermata sono mostrati, in formato tabellare, tutti gli asset nella cui classificazione è presente il livello …\ DISPOSITIVI E COMPONENTI \... e che sono figli diretti della stazione (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.6.2).

Figura 8 - Schermata “Asset smontati”

La sezione “Asset smontati” è caratterizzata dalla relazione di database X3ASSETSMONTATI e riporta i seguenti campi:

Asset: numero identificativo univoco dell’asset smontato. Tale campo richiama l’attributo Standard ASSETNUM della tabella ASSET.

Descrizione: descrizione dell’asset. Tale campo richiama l’attributo Standard DESCRIPTION della tabella ASSET.

Classificazione: Classificazione dell’asset. Tale campo richiama l’attributo Standard CLASSIFICATIONID tramite la relazione CLASSSTRUCTURE (CLASSSTRUCTURE.CLASSIFICATIONID).

Stato: Stato dell’asset. Tale campo richiama l’attributo standard STATUS della tabella ASSET.

### Tab “Dati Stazione”

In questa schermata, in formato tabellare, sono mostrati tutti gli attributi di classificazione del primo livello Stazione (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.6.3).

Figura 9 - Schermata “Specifiche Stazione”

La sezione “Specifiche Stazione” è caratterizzata dalla relazione di database ASSETSPEC e riporta i seguenti campi:

Attributo: Id dell’attributo di classificazione. Tale campo richiama l’attributo di tipo alfanumerico ASSETATTRID della tabella ASSETSPEC.

Descrizione: Descrizione attributo. Tale campo richiama l’attributo DESCRIPTION tramite la relazione ASSETATTRIBUTE (ASSETATTRIBUTE.DESCRIPTION).

Data Modifica: Valore di tipo Data dell’attributo. Si tratta di un campo in cui l’utente può inserire la data effettiva della modifica (anche antecedente). Tale campo richiama l’attributo di tipo custom X3DATAMODIFICA della tabella ASSETSPEC.

Valore Data: Valore di tipo Data dell’attributo. Tale campo richiama l’attributo DATEVALUE della tabella ASSETSPEC.

Valore Alfanumerico: Valore di tipo Alfanumerico dell’attributo. Tale campo richiama l’attributo ALNVALUE della tabella ASSETSPEC.

Valore Numerico: Valore di tipo Numerico dell’attributo. Tale campo richiama l’attributo NUMVALUE della tabella ASSETSPEC.

Unità di misura: Unità di misura dell’attributo. Tale campo richiama l’attributo MEASUREUNITID della tabella ASSETSPEC.

Valore Tabella: Valore di tipo Tabella dell’attributo. Tale campo richiama l’attributo TABLEVALUE della tabella ASSETSPEC.

### Tab “Asset di stazione in relazione”

In questa schermata sono presenti gli asset che vengono spostati sotto la stazione e, quindi, presentano la relazione unidirezionale STAZSTAL (rif. Paragrafo 5.5); questo tipo di relazione viene creata all’interno dell’applicazione Standard “Relazioni” e permette di creare una relazione tra gli asset di stazione tramite la tabella ASSETLOCRELATION (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2. paragrafo 2.6.4).

Di seguito, la schermata principale del tab:

Figura 10 - Schermata “Asset di stazione in Relazione”

Di seguito si riportano le proprietà impostate per la sezione “Asset di stazione in relazione”:

| Proprietà | Dettaglio proprietà |
| --- | --- |
| Data Source ID | 1685552670565 |
| Relationship | X3STAZSTAL |

Tabella 11 - Proprietà Sezione “Asset di stazione in relazione”

La sezione “Asset di stazione in relazione” è caratterizzata dalla relazione di database X3STAZSTAL e riporta i seguenti campi:

Asset: numero identificativo univoco della linea in relazione. Tale campo richiama l’attributo Standard SOURCEASSETNUM della tabella ASSETLOCRELATION.

Descrizione: descrizione della linea in relazione. Tale campo richiama l’attributo Standard DESCRIPTION tramite la relazione SOURCEASSET (SOURCEASSET.DESCRIPTION).

Asset: numero identificativo univoco della linea in relazione. Tale campo richiama l’attributo Standard TARGETASSETNUM della tabella ASSETLOCRELATION.

Descrizione: descrizione della linea in relazione. Tale campo richiama l’attributo Standard DESCRIPTION tramite la relazione TARGETASSET (TARGETASSET.DESCRIPTION).

### Tab “Linee in relazione”

In questa schermata sono mostrate tutte le linee che hanno una relazione con la stazione selezionata, ossia le linee che escono da quest’ultima (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2. paragrafo 2.6.5). In questo caso viene utilizzata la relazione unidirezionale STAZLIN, creata all’interno dell’applicazione “Relazioni” (rif. Paragrafo 5.5).

Di seguito, la schermata principale del tab:

Figura 11 - Schermata “Linee in Relazione”

Di seguito si riportano le proprietà impostate per la sezione “Linee in relazione”:

| Proprietà | Dettaglio proprietà |
| --- | --- |
| Data Source ID | 1685571383588 |
| Relationship | X3STAZLIN.TARGETASSET |

Tabella 12 - Proprietà Sezione “Linee in Relazione”

La sezione “Linee in Relazione” è caratterizzata dalla relazione di database X3STAZLIN.TARGETASSET e riporta i seguenti campi:

Linea: numero identificativo univoco della linea in relazione. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET.

Descrizione: descrizione della linea in relazione. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET.

Stato: stato della linea in relazione. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET.

### Tab “Indirizzo di servizio”

In questa schermata sono mostrate le informazioni di toponomastica della Stazione (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.2).

Di seguito, la schermata principale del tab:

Figura 12 - Schermata “Informazioni di servizio”

Si riporta una descrizione delle informazioni presenti in questa sezione:

Indirizzo di servizio: numero identificativo univoco dell’indirizzo della stazione. Tale campo richiama l’attributo Standard SADDRESSCODE della tabella ASSET.

Indirizzo: Tale campo richiama l’attributo Standard STREETADDRESS della tabella SERVICEADDRESS.

CAP/Codice postale: Tale campo richiama l’attributo Standard POSTALCODE della tabella SERVICEADDRESS.

Città: Tale campo richiama l’attributo Standard CITY della tabella SERVICEADDRESS.

Paese: Tale campo richiama l’attributo Standard COUNTRY della tabella SERVICEADDRESS.

Regione/Distretto: Tale campo richiama l’attributo Standard REGIONDISTRICT della tabella SERVICEADDRESS.

## Gruppi di persone Terna (X3PERSONGR)

L’applicazione Custom “Gruppi di persone Terna”1 è un duplicato dell’applicazione standard “Gruppi di Persona”.

La schermata principale dell’applicazione “Gruppi di persone Terna” contiene l’elenco delle UI e dei DT di Terna con relativa descrizione, il gruppo di persone padre ovvero campo custom che permette di creare una relazione tra UI e DT di appartenenza e il gruppo di default (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2. paragrafo 3.1.4).

La schermata principale è la seguente:

Figura 13 - Schermata “Gruppi di Persone Terna”

Si riporta una descrizione delle informazioni presenti in questa sezione:

Gruppo di persone: Codice identificativo delle Unità Impianti e dei Dipartimenti/Distretti. Tale campo richiama l’attributo Standard PERSONGROUP della tabella PERSONGROUP.

Descrizione: Descrizione delle Unità Impianti e dei Dipartimenti/Distretti. Tale campo richiama l’attributo Standard DESCRIPTION della tabella PERSONGROUP.

Gruppo di persone padre: Dipartimento/Distretto di cui fa parte l’Unità Impianti. Tale campo richiama l’attributo Custom X3PARENTPERSONGROUP della tabella PERSONGROUP.

Group Default: Utente impostato come “Persona predefinita del gruppo”. Tale campo richiama l’attributo standard DISPLAYNAME tramite la relazione ALLPERSONGROUPTEAMDEFAULTS (ALLPERSONGROUPTEAMDEFAULTS.DISPLAYNAME).

Selezionando un record, l’applicazione sarà caratterizzata da un tab, così costituito:

Figura 14 - Dettaglio “Gruppo di Persone Terna”

La sezione “Persone” è caratterizzata dalla relazione di database PERSONGROUP_PRIMARYMEMBERS e, oltre ai campi di testata Gruppo di persone e Gruppo di persone padre, sono presenti le seguenti informazioni:

Persona: Codice identificativo dell’utente. Tale campo richiama l’attributo Standard RESPPARTYGROUP della tabella PERSONGROUPTEAM.

Nome: Nome dell’utente. Tale campo richiama l’attributo Standard DISPLAYNAME tramite la relazione RESPPARTY_PERSONS (RESPPARTY_PERSONS.DISPLAYNAME).

Utilizza per organizzazione: Tale campo richiama l’attributo Standard USEFORORG della tabella PERSONGROUPTEAM.

Utilizza per sede: Tale campo richiama l’attributo Standard USEFORSITE della tabella PERSONGROUPTEAM.

Persona predefinita del gruppo: Utente che gode di proprietà particolari (es. può ricevere le notifiche a nome del gruppo). Tale campo richiama l’attributo Standard GROUPDEFAULT della tabella PERSONGROUPTEAM.

Sede predefinita: Tale campo richiama l’attributo Standard SITEDEFAULT della tabella PERSONGROUPTEAM.

Organizzazione predefinita: Tale campo richiama l’attributo Standard ORGDEFAULT della tabella PERSONGROUPTEAM.

Sequenza: Numero di sequenza dell’utente. Tale campo richiama l’attributo Standard RESPPARTYGROUPSEQ della tabella PERSONGROUPTEAM.

## Dispositivi e componenti (X3DISPECOMP)

Di seguito, la schermata principale dell’applicazione “Dispositivi e componenti” in cui è possibile visualizzare ad elenco tutti i dispositivi e componenti disponibili a sistema:

Figura 15 - Schermata “Dispositivi e componenti”

La schermata (Figura 1) presenta in ordine i seguenti campi relativi a Dispositivi e componenti:

Asset: numero identificativo univoco di un asset. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET

Descrizione: descrizione dell’asset. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET

Stazione di appartenenza: stazione a cui appartiene l’asset. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET

SAP ID Stazione: codice sede tecnica SAP dell’asset di appartenenza. Tale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET

Sede: sede di appartenenza dell’asset. Tale campo richiama l’attributo Standard di tipo alfanumerico SITEID della tabella ASSET

Descrizione classificazione: descrizione della classificazione del dispositivo/componente. Tale campo richiama l’attributo alfanumerico DESCRIPTION_CLASS della tabella CLASSSTRUCTURE

Stato: stato dell’asset. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET

Data di installazione: data in cui il dispositivo/componente è stato installato. Tale campo richiama l’attributo Standard date INSTALLDATE della tabella ASSET

Funzione elemento: attributo “Funzione elemento” dell’asset. Tale campo richiama l’attributo Custom di tipo alfanumerico X3FUNZIONEELEMENTO della tabella ASSET

Riferimento fase: attributo “Riferimento fase” dell’asset. Tale campo richiama l’attributo Custom di tipo alfanumerico X3RIFERIMENTOFASE della tabella ASSET

Costruttore componente: attributo “Costruttore componente” dell’asset. Tale campo richiama l’attributo Custom di tipo alfanumerico X3COSTRUTTORECOMPONENTE della tabella ASSET

Tipo componente: attributo “Tipo componente” dell’asset. Tale campo richiama l’attributo Custom di tipo alfanumerico X3TIPOCOMPONENTE della tabella ASSET

Una volta selezionato un record da elenco, l’applicazione sarà caratterizzata da 6 tab, descritti nei paragrafi successivi.

### Tab “Equipment”

Di seguito, viene illustrato il dettaglio del tab "Equipment", suddiviso in tre sezioni principali,

#### Prima sezione Equipment

Figura 16 – Sezione "Stazione" di “Dispositivi e componenti”

Nella prima sezione (Figura 16) vengono riportate le informazioni dell’asset Stazione (1° livello), di seguito descritte (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2.6 paragrafo 2.6.1).

Stazione: numero identificativo univoco della Stazione. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET.

SAP ID: codice sede tecnica SAP della StazioneTale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET.

Stato: stato della Stazione. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET.

Centro di Costo: codice del centro di costo. Tale campo richiama l’attributo Custom X3KOSTL della tabella ASSET.

Divisione di Ubicazione: codice del Distretto/Dipartimento di appartenenza della Stazione (es: 2601). Tale campo richiama l’attributo Custom di tipo alfanumerico X3SWERK della tabella ASSET.

Unità impianti: contiene la lista delle unità impianti. L’utente può selezionare una UI dall’elenco. Tale campo richiama l’attributo Custom X3INGRP della tabella ASSET.

Controlling Area: Identificativo Controlling Area, ovvero TR01 Terna Italia. Tale campo richiama l’attributo Custom X3KOKRS della tabella ASSET.

Società Proprietaria: identificativo della società proprietaria. Tale campo richiama l’attributo Custom X3ZZSOCP della tabella ASSET.

Società: Indica la Società. Tale campo richiama l’attributo Custom X3BUKRS della tabella ASSET.

Impianto: identificativo dell’impianto. Tale campo richiama l’attributo Custom X3MSGRP della tabella ASSET.

Campo Sort: Campo a testo libero. Tale campo richiama l’attributo Custom X3EQFNR della tabella ASSET.

Centro di lavoro responsabile: Indica il Centro di lavoro responsabile. Tale campo richiama l’attributo Custom X3GEWRK della tabella ASSET.

Accanto ai campi “Stazione” e “Divisione di Ubicazione” è presente un ulteriore campo descrittivo, di seguito i dettagli:

Descrizione “Stazione”: descrizione della stazione. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION della tabella ASSET.

Descrizione “Divisione di Ubicazione”: descrizione del Dipartimento/Distretto (es: Dipartimento Trasm. Nord Ovest). Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION tramite la relazione X3PERSONGROUP (X3PERSONGROUP.DESCRIPTION).

#### Seconda sezione Equipment

La seconda sezione “Asset di terzo secondo livello” permette la visualizzazione degli elementi gerarchicamente inferiori al quarto livello Equipment.

Le proprietà conferite a questa sezione sono riportate nella tabella sottostante:

| Proprietà | Dettaglio proprietà |
| --- | --- |
| Ordinato per | DESCRIPTION |
| Relazione | X3PARENT |

Tabella 13 - Proprietà Sezione “Asset di terzo e secondo livello”

La sezione “Asset di terzo e secondo livello” è caratterizzata dalla relazione X3PARENT e di seguito si riporta una descrizione delle informazioni presenti nella successiva Figura 17:

SAP ID: codice sede tecnica SAP dell’asset di secondo livelloTale campo richiama l’attributo Custom di tipo alfanumerico X3SAPID della tabella ASSET.

Asset: numero identificativo univoco dell’asset di terzo e secondo livello. Tale campo richiama l’attributo Standard ASSETNUM della tabella ASSET.

Descrizione: descrizione dell’asset di terso e secondo livello. Tale campo richiama l’attributo Standard DESCRIPTION della tabella ASSET.

Classificazione: classificazione dell’asset di terzo e secondo livello. Tale campo richiama l’attributo Standard CLASSIFICATIONID tramite la relazione CLASSSTRUCTURE (CLASSSTRUCTURE.CLASSIFICATIONID).

Data di installazione: data di installazione dell’asset di terzo e secondo livello. Tale campo richiama l’attributo Standart di tipo date INSTALLDATE della tabella ASSET.

Stato: stato dell’asset di terzo e secondo livello. Tale campo richiama l’attributo Standard alfanumerico STATUS della tabella ASSET.

Figura 17 - Sezione “Asset di terzo e secondo livello”

Cliccando sul simbolo cerchiato in rosso in Figura 17 verrà restituita la seguente schermata, comprensiva di tutte le specifiche tecniche dell’asset di terzo e secondo livello:

Figura 18 - Sezione “Specifiche Asset”

La sezione “Specifiche asset” è caratterizzata dalla relazione di database ASSETSPEC e di seguito si riportano le proprietà dei campi presenti in 18:

| CAMPO | DEFAULT LABEL | LABEL | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP |
| --- | --- | --- | --- | --- | --- |
| Asset | Asset |  | ASSETNUM | DESCRIPTION |  |
| Attributo | Attributo |  | ASSETATTRID |  |  |
| Descrizione | Descrizione |  | ASSETATTRIBUTE.DESCRIPTION |  |  |
| Valore alfanumerico | Valore alfanumerico |  | ALNVALUE |  |  |
| Valore tabella | Valore tabella |  | TABLEVALUE |  |  |
| Valore numerico | Valore numerico |  | NUMVALUE |  |  |
| Valore Data | Valore data | Valore Data | DATEVALUE |  |  |
| Unità di misura | Unità di misura |  | ASSETATTRIBUTE.MEASUREUNITID |  |  |

Tabella 14 - Campi e Proprietà “Specifiche Tecniche”

#### Terza sezione Equipment

Nella terza sezione (Figura 19) vengono riportate le informazioni dell’asset Equipment di quarto livello.

Figura 19 - Sezione “Informazioni equipment”

Nella terza sezione (Figura 19) vengono riportate le informazioni dell’asset equipment (4° livello), di seguito descritte.

Asset: numero identificativo univoco dell’asset di quarto livello. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET.

Sede: sede di appartenenza dell’asset. Tale campo richiama l’attributo Standard di tipo alfanumerico SITEID della tabella ASSET

Classificazione: classificazione dell’asset di quarto livello. Tale campo richiama l’attributo Standard CLASSIFICATIONID tramite la relazione CLASSSTRUCTURE (CLASSSTRUCTURE.CLASSIFICATIONID).

Stato: stato dell’equipment. Tale campo richiama l’attributo Standard di tipo alfanumerico STATUS della tabella ASSET.

Data di installazione: data di installazione dell’asset di quarto livello. Tale campo richiama l’attributo Standart di tipo date INSTALLDATE della tabella ASSET.

### Tab “Specifiche”

In questa schermata sono mostrate le informazioni relative agli attributi dell’asset equipment.

Di seguito, la schermata principale del tab:

Figura 19 - Schermata “Specifiche”

Di seguito, viene illustrato il dettaglio del tab "Specifiche", suddiviso in due sezioni principali,

#### Prima sezione “Asset”

Figura 20 – Sezione "Asset”

Nella prima sezione (Figura 20) vengono riportate le informazioni dell’asset Equipment (4° livello), di seguito descritte .

Asset: numero identificativo univoco dell’Equipment. Tale campo richiama l’attributo Standard di tipo alfanumerico ASSETNUM della tabella ASSET.

Sede: sede di appartenenza dell’Equipment. Tale campo richiama l’attributo Standard di tipo alfanumerico SITEID della tabella ASSET.

Classificazione: classificazione dell’Equipment. Tale campo richiama l’attributo Standard CLASSIFICATIONID tramite la relazione CLASSSTRUCTURE (CLASSSTRUCTURE.CLASSIFICATIONID).

Descrizione classificazione: descrizione della classificazione. Tale campo richiama l’attributo Standard di tipo alfanumerico DESCRIPTION_CLASS della tabella CLASSSTRUCTURE.

Figura 21 – Sezione "Specifiche”

La sezione “Specifiche” (Figura 21) è caratterizzata dalla relazione di database ASSETSPEC e di seguito si riportano le proprietà dei campi presenti in 21:

| CAMPO | DEFAULT LABEL | LABEL | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP |
| --- | --- | --- | --- | --- | --- |
| Attributo | Attributo |  | ASSETATTRID |  |  |
| Descrizione | Descrizione |  | ASSETATTRIBUTE.DESCRIPTION |  |  |
| Valore Data | Valore data |  | DATEVALUE |  | datelookup |
| Valore ALN | Valore alfanumerico |  | ALNVALUE |  |  |
| Valore numerico | Valore numerico |  | NUMVALUE |  |  |
| Unità di misura | Unità di misura |  | ASSETATTRIBUTE.MEASUREUNITID |  | measureunit |
| Valore tabella | Valore tabella |  | TABLEVALUE |  |  |

## Costruttore componente / Tipo (X3TYPBZ)

Di seguito, la schermata principale dell’applicazione “Costruttore componente / Tipo” in cui è possibile visualizzare ed aggiungere valori alla matrice Produttore del cespite / definizione tipo per gli equipment della stazione.

Figura 29 - Schermata “Costruttore componente / Tipo”

La schermata (Figura 29) presenta in ordine i seguenti campi relativi a Costruttore componente / Tipo:

Produttore del cespite: codiceidentificativo del produttore del cespite. Tale campo richiama l’attributo di tipo alfanumerico ZHERST della tabella custom X3EQTYPBZ.

Definizione tipo: codice identificativo del tipo. Tale campo richiama l’attributo di tipo alfanumerico ZTYPBZ della tabella custom X3EQTYPBZ.

Classe: identificativo della classe. Tale campo richiama l’attributo di tipo alfanumerico ZCLASSE della tabella custom X3EQTYPBZ.

Classificazione: classificazione per la quale è valida la coppia Produttore del cespite/Definizione tipo. Tale campo richiama l’attributo di tipo upper CLASSIFICATIONID della tabella custom X3EQTYPBZ

Classification Path: percorso della classificazione di riferimento. Tale campo richiama l’attributo alfanumerico HIERARCHYPATH della tabella custom X3EQTYPBZ

# Detailed processing

Nel seguente capitolo, vengono riportati tutti gli elementi aggiuntivi creati su MAS Dev di Terna e che permettono di personalizzare ulteriormente l’applicativo. Questi includono ad esempio conditional expressions, automation scripts, messaggi di sistema, azioni.

L'obiettivo di questa sezione è quello di fornire una panoramica completa di tutte le personalizzazioni e automatismi presenti a sistema.

## Automation Scripts

Gli script di automazione sono piccole parti di codice mirate che possono estendere il prodotto. Uno script di automazione è costituito da un punto di avvio, da variabili con valori di binding corrispondenti e dal codice sorgente.

Di seguito viene fornita una tabella riepilogativa degli script implementati:

|  |  |  |  |  |  |  |  |
| --- | --- | --- | --- | --- | --- | --- | --- |
| X3ASSETSPECDATA | X3ASSETSPECDATA2 | OBJECT | ASSETSPEC | onSave, Update, After Save |  |  |  |
| X3CREATEASSETSTAZDIALOGINIT | X3CREATEASSETSTAZDIALOGINIT | OBJECT | X3CREATEASSETSTAZ | Initialize |  |  |  |
| X3CREATEASSETSTAZDIALOGSAVE | X3CREATEASSETSTAZDIALOGSAVE | OBJECT | X3CREATEASSETSTAZ | onSave, Add, Before Save |  |  |  |
| X3GENSAPIDSTAZ | X3GENSAPIDSTAZCLASS | ATTRIBUTE | X3CREATEASSETSTAZ | Validate |  |  |  |
| X3GENSAPIDSTAZ | X3GENSAPIDSTAZTIPO | ATTRIBUTE | X3CREATEASSETSTAZ | Validate |  |  |  |
| X3GENSAPIDSTAZ | X3GENSAPIDSTADETTZ | ATTRIBUTE | X3CREATEASSETSTAZ | Validate |  |  |  |
| X3SHOWSAPIDSTAZEQ | X3SHOWSAPIDSTAZEQ | OBJECT | ASSET | Initialize |  |  |  |
| X3STZROLLUPATTRIBUTE | X3ASSETSTATUS | OBJECT | ASSET | onSave, Add Update Delete, After Commit | v_siteid = SITEID |  |  |
| X3STZROLLUPATTRIBUTE | X3ASSETSTATUS | OBJECT | ASSET | onSave, Add Update Delete, After Commit | v_assetnum = ASSETNUM |  |  |
| X3STZROLLUPATTRIBUTE | X3ASSETSPECNUMVALUE | OBJECT | ASSETSPEC | onSave, Add Update Delete, After Commit | v_assetnum = ASSETNUM |  |  |
| X3STZROLLUPATTRIBUTE | X3ASSETSPECNUMVALUE | OBJECT | ASSETSPEC | onSave, Add Update Delete, After Commit | v_siteid = SITEID |  |  |
| X3STZSPOSTAASSETDIALOG | X3STZSPOSTAASSETDIALOG | OBJECT | X3STZMOVEASSET | Initialize |  |  |  |
| X3STZSPOSTAASSETSAVE | X3STZSPOSTAASSETSAVE | OBJECT | X3STZMOVEASSET | onSave, Add Update, Before Save |  |  |  |
| X3TABVERSTAZ | X3TABVERSTAZ | OBJECT | X3TABVERITASTAZIONI | onSave, Add Update, Before Save |  |  |  |
| X3TRASCPOSNR_CLASSIFICATIONID | X3TRASCPOSNR_CLASSIFICATIONID | ATTRIBUTE | X3TRASCODIFICAPSNR | Run Action |  |  |  |
|  |  |  |  |  |  |  |  |
|  |  |  |  |  |  |  |  |

Tabella 13 - Automation Scripts

## Azioni

Le azioni in Maximo sono eventi che possono essere richiamati tramite script, escalation o workflow, e che generano una serie di operazioni o funzionalità specifiche all'interno del sistema. Queste azioni possono includere la creazione, la modifica, l'eliminazione o l'approvazione di record, l'automazione di processi e altre attività che facilitano e migliorano l'efficienza delle operazioni.

Di seguito è riportata la lista delle azioni Custom create sul sistema e indentificate con il prefisso X3. Le azioni vengono richiamate negli Script (rif. 5.1 Automation Scripts) e collegate alle sigoption al fine di rendere visibili e richiamabili tali azioni da interfaccia grafica.

| Azione | Descrizione | Nome oggetto | Parametri | Riferimento Script (par. 5.1) | Dettaglio/ rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2. |
| --- | --- | --- | --- | --- | --- |
|  |  |  |  |  |  |

Tabella 14 - Azioni

## Conditional expression manager

Il Conditional Expression Manager, all'interno di MAS (Maximo Asset Management System), è un componente essenziale per creare e gestire condizioni basate su espressioni SQL. Queste condizioni servono a controllare processi, definire comportamenti e stabilire regole di business basate su criteri specifici. Possono essere applicate dinamicamente a vari elementi dell'interfaccia utente, come campi, schede e altri controlli, personalizzando così la presentazione. Inoltre, le condizioni possono essere estese ai gruppi di sicurezza per regolare l'accesso a specifiche applicazioni, controlli e dati, garantendo una gestione precisa e sicura delle funzionalità del sistema. Ad esempio, uno stato specifico di un asset può essere nascosto in base a una condizione come X3FALSE.

Di seguito è riportata la lista delle condizioni create e le rispettive query SQL (Expression):

| Condizione | Descrizione | Espressione | Espressione | Dettaglio |
| --- | --- | --- | --- | --- |
| X3FALSE | Condizione sempre falsa | Condizione sempre falsa | 1=3 |  |
| X3GSTATS3 | Gestione per gli stati del terzo livello stazioni | Gestione per gli stati del terzo livello stazioni | EXISTS (SELECT CLASSSTRUCTUREID FROM CLASSANCESTOR WHERE ANCESTORCLASSID = 'STAZIONE' AND HIERARCHYLEVELS = 2 AND :CLASSSTRUCTUREID in( SELECT CLASSSTRUCTUREID FROM CLASSANCESTOR WHERE ANCESTORCLASSID in('SEZIONE','TRASFORMAZIONI','RIFASAMENTI','ACCUMULI') AND HIERARCHYLEVELS = 1)) |  |
| X3STATL12S1 | Visibilità stati per Liv 1 e 2 linee e Liv 1 stazioni | Visibilità stati per Liv 1 e 2 linee e Liv 1 stazioni | EXISTS (SELECT CLASSSTRUCTUREID FROM CLASSANCESTOR WHERE CLASSSTRUCTUREID = :CLASSSTRUCTUREID AND ANCESTORCLASSID IN('LINEA','STAZIONE') AND HIERARCHYLEVELS IN(0)) OR EXISTS (SELECT CLASSSTRUCTUREID FROM CLASSANCESTOR WHERE CLASSSTRUCTUREID = :CLASSSTRUCTUREID AND ANCESTORCLASSID IN('LINEA') AND HIERARCHYLEVELS IN(1)) |  |
| X3INGRP | Condizione che rende modificabile l'Asset in base all'Unità Impianti | Condizione che rende modificabile l'Asset in base all'Unità Impianti | (:assetnum in (select assetnum from asset where x3swerk is null and siteid='SIT01')) or (:&USERNAME& in (select resppartygroup from persongroupteam where persongroup in (select persongroup from persongroup where x3parentpersongroup=:x3swerk))) |  |
| X3MOVEASSET | Condizione per abilitare/disabilitare sposta asset stazioni | Condizione per abilitare/disabilitare sposta asset stazioni | (:status ='NIED') or (:status<>'NIED' and :&USERNAME& in (select resppartygroup from persongroupteam where persongroup in (select persongroup from persongroup where x3parentpersongroup=:x3swerk)) and classstructureid in (select classstructureid from classancestor where ancestorclassid in ('DISPOSITIVI E COMPONENTI'))) or (classstructureid in (select classstructureid from classancestor where classstructureid=:classstructureid and ancestorclassid in ('DISPOSITIVI E COMPONENTI') and classstructureid not in (select classstructureid from classancestor where classificationid like 'SISTEMA DI RAFFREDDAMENTO %' or classificationid like 'VARIATORE %'))) |  |
| X3RESP | Condizione che verifica se il Gruppo responsabile è presente nell'attributo | Condizione che verifica se il Gruppo responsabile è presente nell'attributo | (:assetattrid in (select assetattrid from assetattribute where x3gruppo_resp is not null) and :&USERNAME& not in (select resppartygroup from persongroupteam where persongroup=(select x3gruppo_resp from assetattribute where assetattrid=:assetattrid)) ) AND :assetattrid not in (select assetattrid from assetattribute where x3gruppo_resp is null) |  |
| X3TRUE | Condizione sempre vera | Condizione sempre vera | 3=3 |  |
| X3DATEATTRIB | Solo lettura attributi che non hanno DATATYPE DATE | Solo lettura attributi che non hanno DATATYPE DATE | :assetattrid not in(select assetattrid from assetattribute where datatype='DATE') |  |
| X3STATL1S1 | Visibilità stati per Liv 1 linee e stazioni arrivate da SAP e in R.O in MX | Visibilità stati per Liv 1 linee e stazioni arrivate da SAP e in R.O in MX | EXISTS (SELECT CLASSSTRUCTUREID FROM CLASSANCESTOR WHERE CLASSSTRUCTUREID = :CLASSSTRUCTUREID AND ANCESTORCLASSID IN('LINEA','STAZIONE') AND HIERARCHYLEVELS IN(0)) AND NOT EXISTS (SELECT SOURCEASSETNUM FROM ASSETLOCRELATION WHERE SOURCEASSETNUM = :ASSETNUM AND SITEID = :SITEID) |  |
| X3STATL4S4 | Visibilità stati per Liv 4 linee e stazioni | Visibilità stati per Liv 4 linee e stazioni | EXISTS (SELECT CLASSSTRUCTUREID FROM CLASSANCESTOR WHERE CLASSSTRUCTUREID = :CLASSSTRUCTUREID AND ANCESTORCLASSID IN('LINEA') AND HIERARCHYLEVELS IN(3)) OR EXISTS (SELECT CLASSSTRUCTUREID FROM CLASSANCESTOR WHERE CLASSSTRUCTUREID = :CLASSSTRUCTUREID AND ANCESTORCLASSID IN('DISPOSITIVI E COMPONENTI')) |  |
| X3STATS4 | Visibilità stati per Liv 4 SOLO stazioni | Visibilità stati per Liv 4 SOLO stazioni | EXISTS (SELECT CLASSSTRUCTUREID FROM CLASSANCESTOR WHERE CLASSSTRUCTUREID = :CLASSSTRUCTUREID AND ANCESTORCLASSID IN('DISPOSITIVI E COMPONENTI')) |  |
| X3STATODATT | Impedisce di andare in stato DATT dopo la creazione dell'asset | Impedisce di andare in stato DATT dopo la creazione dell'asset | NOT exists(SELECT 1 FROM PLUSCASSETSTATUS WHERE ASSETNUM=:ASSETNUM AND SITEID=:SITEID and status='DATT') |  |
| X3GESTSTAT | Nasconde lo stato asset 4 livello se Geststat = 0 | Nasconde lo stato asset 4 livello se Geststat = 0 | :classstructureid in ( select classstructureid from classstructure where x3geststat=0 and classstructureid in(select classstructureid from classancestor where classstructureid=:classstructureid and ancestorclassid in('LINEA') and hierarchylevels='3')) |  |
| X3STZSAPIDM | Rende visibile il campo SAPID mancante | Rende visibile il campo SAPID mancante | (:classstructureid is null or (:classstructureid not in (select classstructureid from classancestor where ancestorclassid='STAZIONE' and hierarchylevels='1') and :x3sapid is not null)) |  |
| X3RIF_FASE_S | Riferimento Fase S | Riferimento Fase S | exists( select RIFERIMENTOFASE from X3TRASCODIFICAPSNR where elemento = (SELECT TABLEVALUE FROM ASSETSPEC WHERE ASSETATTRID ='STZ_FUNZIONE_ELEMENTO' AND ASSETNUM =:assetnum ) AND CLASSIFICATIONID = (SELECT CLASSIFICATIONID FROM CLASSSTRUCTURE WHERE CLASSSTRUCTUREID = (select CLASSSTRUCTUREID FROM asset WHERE assetnum=:assetnum )) AND riferimentofase='S' ) |  |
| X3CREAMACCHI | Mostra dettagli aggiuntivi per creazione equipment macchine | Mostra dettagli aggiuntivi per creazione equipment macchine | :classstructureid in (select classstructureid from classstructure where X3CLASSIDREL is not null) |  |
| X3STAZCRSAPI | Nascondi il campo SAPID Mancante per creazione asset stazioni | Nascondi il campo SAPID Mancante per creazione asset stazioni | :classstructureid not in (select classstructureid from classancestor where ancestorclassid='DISPOSITIVI E COMPONENTI') |  |
| X3RIF_FASE_N | Riferimento Fase N | Riferimento Fase N | exists( select RIFERIMENTOFASE from X3TRASCODIFICAPSNR where elemento = (SELECT TABLEVALUE FROM ASSETSPEC WHERE ASSETATTRID ='STZ_FUNZIONE_ELEMENTO' AND ASSETNUM =:assetnum ) AND CLASSIFICATIONID = (SELECT CLASSIFICATIONID FROM CLASSSTRUCTURE WHERE CLASSSTRUCTUREID = (select CLASSSTRUCTUREID FROM asset WHERE assetnum=:assetnum )) AND riferimentofase='N' ) |  |
| X3GESTSTATST | Nascondo lo stato di asset di 4 livello in applicazione Stazione | Nascondo lo stato di asset di 4 livello in applicazione Stazione | :classstructureid in ( select classstructureid from classstructure where x3geststat=0 and classstructureid in(select classstructureid from classancestor where classstructureid=:classstructureid and ancestorclassid in('STAZIONE') and hierarchylevels='3' or hierarchylevels='4')) AND :classstructureid NOT IN (SELECT classstructureid FROM classancestor WHERE ancestorclassid='DISPOSITIVI E COMPONENTI') |  |
| X3STZATTREAD | condizione per mettere in read only gli attributi calcolati | condizione per mettere in read only gli attributi calcolati | (:assetattrid like 'STZ_NUM_SEZIONI_%') or (:assetattrid like 'STZ_NUM_TRASF_%') or (:assetattrid in ('STZ_ELEMENTO_INDISP', 'STZ_TIPO_COMPONENTE','STZ_COSTRUTTORE_COMPONENTE')) |  |
| X3STZ_RIF_FA | Riferimento fase | Riferimento fase | elemento in (select elemento from X3TRASCODIFICAPSNR where elemento in (SELECT TABLEVALUE FROM ASSETSPEC WHERE ASSETATTRID ='STZ_FUNZIONE_ELEMENTO'AND ASSETNUM =:assetnum ) AND CLASSIFICATIONID = (SELECT CLASSIFICATIONID FROM CLASSSTRUCTURE WHERE CLASSSTRUCTUREID = (select CLASSSTRUCTUREID FROM asset WHERE assetnum=:assetnum))) |  |
| X3RIF_FASE_R | Riferimento Fase R | Riferimento Fase R | exists( select RIFERIMENTOFASE from X3TRASCODIFICAPSNR where elemento = (SELECT TABLEVALUE FROM ASSETSPEC WHERE ASSETATTRID ='STZ_FUNZIONE_ELEMENTO' AND ASSETNUM =:assetnum ) AND CLASSIFICATIONID = (SELECT CLASSIFICATIONID FROM CLASSSTRUCTURE WHERE CLASSSTRUCTUREID = (select CLASSSTRUCTUREID FROM asset WHERE assetnum=:assetnum )) AND riferimentofase='R' ) |  |
| X3STZSAPIDE | Rende visibile il campo SAPID esteso quando non è vuoto | Rende visibile il campo SAPID esteso quando non è vuoto | :classstructureid is null or :x3sapid is not null |  |
| X3STZRICODIF | Rende visibile il campo Nuovo ID SAP per il terzo livello | Rende visibile il campo Nuovo ID SAP per il terzo livello | :X3STZSPOSTAASSET.CLASSSTRUCTUREID IN (SELECT CLASSSTRUCTUREID FROM CLASSANCESTOR WHERE ANCESTORCLASSID ='STAZIONE' AND HIERARCHYLEVELS ='2') |  |
| X3RIF_FASE_T | Riferimento Fase T | Riferimento Fase T | exists( select RIFERIMENTOFASE from X3TRASCODIFICAPSNR where elemento = (SELECT TABLEVALUE FROM ASSETSPEC WHERE ASSETATTRID ='STZ_FUNZIONE_ELEMENTO' AND ASSETNUM =:assetnum ) AND CLASSIFICATIONID = (SELECT CLASSIFICATIONID FROM CLASSSTRUCTURE WHERE CLASSSTRUCTUREID = (select CLASSSTRUCTUREID FROM asset WHERE assetnum=:assetnum )) AND riferimentofase='T' ) |  |

Tabella 15 - Conditional Expressions

## Sigoption

Le sigoption consentono una personalizzazione maggiore dei permessi e delle autorizzazioni in EAM, garantendo che solo le persone abilitate possano eseguire determinate azioni o visualizzare/modificare campi specifici. Vengono create all’interno dell’Application designer (rif.4 Application Designer) e possono essere applicate a campi o azioni. L'abilitazione viene gestita attraverso l'applicazione Gruppi di Sicurezza ed è applicabile a livello di singolo Gruppo.

Di seguito, le Sigoption create a sistema. Nella colonna Descrizione vengono descritte le azioni che verranno abilitate all’utente tramite la Sigoption nell’applicazione Stazioni:

| Applicazione | Optionname | Descrizione | Descrizione |
| --- | --- | --- | --- |
| X3STAZIONI | X3CREAMACCHINA | Mostra dettagli aggiuntivi per creazione equipment macchine |  |
| X3STAZIONI | X3STZMOVE | Sposta/Modifica Asset 4 livello |  |
| X3STAZIONI | X3STZRICODIFICA | Rende visibile il campo Nuovo ID SAP per il terzo livello Stazioni |  |
| X3STAZIONI | X3STZSAPIDESTESO | Rende visibile il campo SAP ID esteso quando non è vuoto |  |
| X3STAZIONI | X3STZSAPIDMANCANTE | Rende visibile il campo SAP ID mancante |  |

Tabella 16 – Sigoption

## Relazioni

Nel Manage, l’applicazione standard “Relazioni” è uno strumento che consente di descrivere la dipendenza e la connettività di asset fisici (non di tabelle di database, rif. Paragrafo 2.5). Nelle Stazioni tale necessità è presente per i casi di asset di stazione in relazione (rif. Paragrafo 4.1.4) e per quelli di linee in relazione (rif. Paragrafo 4.1.5).

Di seguito l’elenco delle Relazioni presenti sull’applicativo ed utilizzate nell’ambito delle Stazioni. La colonna Nome indica il nome univoco della relazione, Descrizione equivale alla descrizione della stessa, Tipologia indica la direzionalità della relazione e Usa con indica l’applicazione su cui viene usata la relazione:

| Nome | Descrizione | Tipologia | Usa con |
| --- | --- | --- | --- |
| STAZSTAL | Relazione tra elementi di stazione | UNIDIRECTIONAL | ASSET |
| STAZLIN | Relazione tra linee e stazioni | UNIDIRECTIONAL | ASSET |

Tabella 17 - Relazioni

# Gruppi di protezione

All’interno del MAS è possibile creare e gestire i gruppi di Protezione. Per ogni gruppo di protezione è possibile impostare e gestire i privilegi degli utenti e controllare, quindi, l'accesso e i permessi alle diverse funzionalità e dati all'interno del sistema. Un utente può appartenere a più gruppi di protezione (rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2. paragrafo 3.1.2).

Di seguito l’elenco dei gruppi di Protezione Standard e Custom (prefisso X3) presenti sull’applicativo. La colonna Nome Gruppo indica il nome univoco definito su EAM, Descrizione equivale alla descrizione del Gruppo indicata su EAM e Grant la tipologia di permessi:

ALL: Visualizzazione, Creazione, Modifica, Cancellazione

READ: Visualizzazione

| Nome Gruppo | Descrizione | Descrizione | Grant |
| --- | --- | --- | --- |
| MAXADMIN | Gruppo amministrativo standard | Gruppo amministrativo standard | ALL |
| MAXDEFLTREG | Gruppo standard Utenti | ALL | ALL |
| EVERYONE | Gruppo standard Utenti | ALL | ALL |
| X3CENTRO-01 | Gruppo di sicurezza centro 1 | ALL (Applicazione Linee/Stazioni, Domini, Classificazioni e Attributi) | ALL (Applicazione Linee/Stazioni, Domini, Classificazioni e Attributi) |
| X3CENTRO-02 | Gruppo di sicurezza centro 2 | ALL (Applicazione Linee/Stazioni) | ALL (Applicazione Linee/Stazioni) |
| X3CENTRO-03 | Gruppo di sicurezza centro 3 | READ (Applicazione Linee/Stazioni) | READ (Applicazione Linee/Stazioni) |
| X3DISP |  | READ (Applicazione Linee/Stazioni) | READ (Applicazione Linee/Stazioni) |
| X3UI01 | Gruppo unità impianti (test) | ALL (Applicazione Linee/Stazioni) | ALL (Applicazione Linee/Stazioni) |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Tabella 18 - Gruppi di Protezione

Il gruppo MAXADMIN è destinato agli Amministratori e dà massimo potere agli utenti presenti all’interno.

Inoltre, tutti gli utenti vengono aggiunti automaticamente ai gruppi di sicurezza predefiniti MAXDEFLTREG e EVERYONE per consentire l'accesso iniziale e la funzionalità minima del sistema

## Global Restriction

All’interno dei Gruppi di protezione è possibile definire le Global Restrictions, regole di sicurezza utilizzate per controllare l'accesso a determinate funzionalità, dati o azioni all'interno del MAS. Questo meccanismo permette agli amministratori di sistema di definire politiche di sicurezza centralizzate e valide a livello globale in tutto l'applicativo Maximo. Tutte le condizioni sono dettagliate nel paragrafo 5.3 Conditional Expression Manager del seguente documento.

Di seguito l’elenco delle Global Restriction create in ambiente Terna Dev:

| Oggetto | Attributo | Applicazione | Tipo | Condizione | Descrizione |
| --- | --- | --- | --- | --- | --- |
| ASSETSPEC |  | X3STAZIONI | READONLY | ROW | X3STZATTREAD |
| ASSET | STATUS | X3STAZIONI | HIDDEN | ATTR | X3GESTSTATST |
| ASSETSPEC | DATEVALUE |  | READONLY | ATTR | X3DATEATTRIB |

Tabella 19 – Global Restriction

# Gruppi di Persone

Nell'ambiente MAS Dev di Terna, sono stati creati specifici gruppi di persone gestiti all'interno dell'applicazione custom "Gruppi di Persona Terna"1. Questi gruppi sono stati configurati per organizzare gli utenti in base a criteri specifici e caratteristiche comuni, al fine di facilitare la gestione delle autorizzazioni e delle attività.

In particolare, sono stati creati i seguenti gruppi di persone:

33 UI (Unità Impianti)

8 DT (Dipartimenti o Distretti Trasmissione)

È importante sottolineare che un singolo utente può appartenere a più gruppi di persone. Questa struttura e l'associazione dei criteri di filtro consentono di stabilire regole specifiche per la modificabilità o la sola lettura degli asset, garantendo un'organizzazione più efficiente e una gestione accurata delle autorizzazioni.

Le informazioni sopra riportate sono dettagliate in rif. Doc.1 EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V2. paragrafo 3.1.4.

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
|  |  |  |
|  |  |  |
|  |  |  |

Tabella 20 - Gruppi di persone "UI"

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
|  |  |  |
|  |  |  |

Tabella 21 - Gruppi di persone "DT"
