# EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20260730_V6

|  | Disegno di alto livello (HLD) del Modello Dati dell’Anagrafica Stazioni e delle Funzionalità Applicative dell’Anagrafica Stazioni |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Relazione: | DISEGNO DI ALTO LIVELLO DEL MODELLO DATI DELL’ANAGRAFICA E DELLE FUNZIONALITÀ APPLICATIVE DELL’ANAGRAFICA STAZIONI |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 1.0 | Primo rilascio | 19/12/2023 |
|  |  |  |
|  |  |  |
|  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | RTC |  |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

Documenti citati

| # | File | Descrizione | Data |
| --- | --- | --- | --- |
| 1 | TERNA_EAM_RRQ_REQUT_ANA_CasiDiUso_20230414_V6.1 | Documento relativo ai casi d’uso dell’Anagrafica |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Indice delle figure

Figura 1 - Esempio di stazione elettrica (smistamento e conversione) 1

Figura 2 - Esempio distribuzione asset di 4° tra gli elementi di 3° livello 1

Figura 3 - Relazioni tra linee aeree, in cavo e stazioni 1

Figura 4 – Struttura a 4 livelli delle stazioni (porzione) 1

Figura 5 - Organigramma Terna 1

Figura 6 - Legame DT – UI 1

Figura 7 - Codici DT 1

Figura 8 - Codici UI 1

Figura 9 - Trasformazioni, livelli 2°, 3° e 4° 1

Figura 10 - Stati gestiti 1

Figura 11 - Maschera di spostamento asset 1

Figura 12 - Focus sulla gestione utente della data effettiva di spostamento 1

Figura 13 - Ricerca storico delle variazioni (1) 1

Figura 14 - Ricerca storico delle variazioni (2) 1

Figura 15 - Storico spostamenti di un asset 1

Figura 16 - Asset di stazione 1

Figura 17 - Dominio POSNR per asset di stazione 1

Figura 18 - Relazione stazione - linea 1

Figura 29 – Rappresentazione gerarchica del caso 1: Asset di terzi sotteso ad un asset di Terna 1

Figura 30 - Rappresentazione gerarchica del caso 2: Asset di Terna sotteso ad asset di terzi 1

Figura 31 - Gerarchia degli elementi di terzo livello Stallo e Sbarra nel modello To-be 1

Figura 32 - Asset di quarto livello di una Stazione 1

Figura 33 - Attributo "Asset di riferimento" dell'asset di quarto livello 1

Figura 34 - Asset associabili all'asset di quarto livello selezionato 1

# Introduzione

Questo documento ha l’obiettivo di presentare la struttura del modello dati e le funzionalità applicative dell’anagrafica delle stazioni (derivate dal documento dei casi d’uso TERNA_EAM_RRQ_REQUT_ANA_CasiDiUso_20230414_V6.1.docx).

In particolare, la macro-fase di High-Level Design formalizza gli scenari operativi identificati dagli use-case secondo i meccanismi di modellazione offerti dal prodotto. A seguire, questo documento verrà ulteriormente dettagliato nella attività di Low-Level Design in cui verranno identificate le attività realizzate a basso livello che rappresenteranno l’input per la macro-fase di Implementazione.

Di seguito le immagini rappresentative del data model oggetto del documento.

La figura seguente rappresenta una stazione elettrica di smistamento e trasformazione; le parti in grigio indicano che quelle componenti, facenti comunque parte del modello dati, non ha senso siano presenti per questo tipo di stazione.

Figura 1 - Esempio di stazione elettrica (smistamento e conversione)

Figura 2 - Esempio distribuzione asset di 4° tra gli elementi di 3° livello

Figura 3 - Relazioni tra linee aeree, in cavo e stazioni

Figura 4 – Struttura a 4 livelli delle stazioni (porzione)

## Glossario

| Termine | Spiegazione | Nota |
| --- | --- | --- |
| PoC | Acronimo di proof of concept. Il PoC è l'allestimento (nell'ambiente software effettivo, non una presentazione "fittizia" in stile PowerPoint o eseguita mediante uno strumento di modellazione visiva di schermate) di una demo prototipale del sistema o applicazione in sviluppo o in corso di valutazione. |  |
| IBM MAX Manage | È il nome dell'applicativo Maximo, ora inserito come componente della più estesa suite di prodotti denominata MAS: Maximo Application Suite. Quindi, poiché il prefisso Maximo è stato impiegato per tutti i componenti, non potendo riferisti a IBM Maximo Application Suite Maximo (ossia Maximo ripetuto due volte) al vecchio Maximo è stato dato il nome di Manage (ossia IBM Maximo Application Suite Manage, che diventa IBM MAS Manage). | Nel documento, per brevità verrà utilizzato anche il solo termine Manage. |
| TS | Terna singola |  |
| TD | Terna doppia |  |
| UI | Unità impianti |  |
| DT | Distretto di trasmissione |  |
| MBO | Acronimo di Maximo Business Object. Definisce un insieme di campi e regole di business ed agisce su una o più tabelle del database del Manage. |  |
| FdG | Acronimo per indicare la fune di guardia. |  |

## Tabelle del DB del Manage

| Tabella | Spiegazione | Nota |
| --- | --- | --- |
| ASSET | Tabella principale dell’MBO Asset. |  |
| ASSETLOCRELATION | Tabella nella quale sono gestite le relazioni che permettono ad un asset di legarsi a molteplici asset di livello superiore. |  |
| ASSETSTATUS | Tabella con cardinalità 1:n rispetto ad ogni asset, dove sono registrati i cambi stato di ogni asset. |  |
| ASSETSPEC | Tabella delle specifiche tecniche degli asset. Viene popolata, con cardinalità 1:n rispetto all’asset, nel momento in cui l’asset è associato ad una classificazione. In questa tabella sono anche salvati i valori di ogni singolo attributo, in relazione a ciascun’asset. |  |
| SERVICEADDRESS | Tabella dove sono registrati tutti gli indirizzi associabili a vari elementi gestiti in Manage, tra cui gli asset. |  |
| X3TRONCHI | Tronchi di ispezione |  |
| X3COIN | Codici di indisponibilità |  |
| CLASSSTRUCTURE | Tabella principale di gestione delle gerarchie di modelli di classificazioni, associabili a varie entità del Manage, tra cui gli asset e gli item. |  |
| ASSETATTRIBUTE | Tabella dell’anagrafica dei modelli di attributi utilizzati nelle classificazioni. |  |
| ITEM | Tabella degli item, utilizzati nel progetto Terna per gestire i componenti, che diventeranno asset di 2°, 3° e 4° livello. |  |
| ITEMSPEC | Tabella delle specifiche tecniche degli item. Viene popolata, con cardinalità 1:n rispetto all’item, nel momento in cui l’item è associato ad una classificazione. |  |
| ITEMSTRUCT | Tabella della struttura di modelli, che una volta istanziati, daranno origine agli asset in gerarchia (ad esempio, applicando la struttura SOSTEGNO-S ad un asset questo assumerà la classificazione, con relativi attributi, del sostegno e creerà tutti gli altri asset, sempre con relative classificazioni e attributi, di 3° e stat4° livello, figli e “nipoti” del sostegno. |  |
|  |  |  |

# Il modello dati delle stazioni

In questo capitolo viene spiegato il modello dati impiegato per la gestione delle stazioni elettriche. La struttura dati di riferimento per gestirle è quella degli Asset, coadiuvata dalle classificazioni, per poter classificare i componenti distribuiti sui 4 livelli e collegarli agli attributi di pertinenza.

Il modello dati, oltre a permettere la caratterizzazione dei singoli asset deve essere anche in grado di gestire i legami logici tra i differenti livelli.

## Elementi, livelli classificazioni e attributi della stazione

In questo paragrafo si ripercorre la struttura degli asset delle stazioni, come si qualificano e come si agganciano le rispettive caratteristiche tecniche.

Una stazione elettrica è divisa in 4 livelli, come mostrato nell’immagine di Figura 1. La stazione elettrica, per ciascun livello, si compone dei seguenti elementi, censiti nel Manage come asset:

Livello uno:

Stazione

Livello due:

Accumuli

Rifasamenti

Servizi Ausiliari

Sezione

Strutture e impianti tecnologici

Trasformazioni

Controllo generale di impianto

Conversioni (elemento HVDC)

Compensatore sincrono (elemento dei Sincroni)

Livello tre per i rispettivi secondi livelli:

Accumuli

Setup

UAC

Rifasamenti

Batteria condensatori

…

Servizi Ausiliari

Servizi ausiliari BT corrente continua

…

Sezione

Sbarra

Stallo

Strutture e impianti tecnologici

Edifici logistici

…

Trasformazioni

Booster

…

Controllo generale di impianto

Acquisizione eventi e perturbazioni

Circuiti generali d’impianto

Compensatore sincrono (elemento dei Sincroni)

Alternatore (elemento dei Sincroni)

Volano (elemento dei Sincroni)

HVAC cabina sincrono (elemento dei Sincroni)

Conversioni (elemento HVDC)

Filtro corrente continua

Montante AT lato conversione (elemento degli HVDC )

Sala valvole (elemento degli HVDC)

…

Livello quattro per i rispettivi terzi livelli:

Setup:

N/A

Batteria condensatori:

STAZIONE \ DISPOSITIVI E COMPONENTI \ MACCHINARIO DI POTENZA \ BATTERIA CONDENSATORI DI RIFASAMENTO

STAZIONE \ DISPOSITIVI E COMPONENTI \ RIDUTTORI DI CORRENTE AT \ TA AT ISOLATO ARIA

…

Servizi ausiliari BT corrente continua:

STAZIONE \ DISPOSITIVI E COMPONENTI \ ALIMENTAZIONE SERVIZI AUSILIARI \ RADDRIZZATORE CARICA BATTERIE

Sbarra:

STAZIONE \ DISPOSITIVI E COMPONENTI \ SBARRA \ SBARRA AIS

STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ SEZIONATORI AT \ SEZIONATORE AT DI LINEA BLINDATO

Stallo:

STAZIONE \ DISPOSITIVI E COMPONENTI \ CONDOTTO BUSDUCT (elemento dei Sincroni)

STAZIONE \ DISPOSITIVI E COMPONENTI \ SEZIONATORE GCB (elemento dei Sincroni)

Alternatore

STAZIONE\DISPOSITIVI E COMPONENTI\IMPIANTO COMPENSATORE SINCRONO\STATORE (elemento dei Sincroni)

Volano

STAZIONE\DISPOSITIVI E COMPONENTI\IMPIANTO COMPENSATORE SINCRONO\CORPO VOLANO (elemento dei Sincroni)

HVAC cabina sincrono

STAZIONE\DISPOSITIVI E COMPONENTI\IMPIANTO COMPENSATORE SINCRONO\FILTRO HVAC (elemento dei Sincroni)

Edifici logistici:

N/A

Booster:

STAZIONE \ DISPOSITIVI E COMPONENTI \ MACCHINARIO DI POTENZA \ ISOLATORE PASSANTE \ ISOLATORE PASSANTE OLIO/SF6

STAZIONE \ DISPOSITIVI E COMPONENTI \ RIDUTTORI DI CORRENTE AT \ TA TOROIDALE

Acquisizione eventi e perturbazioni:

STAZIONE \ DISPOSITIVI E COMPONENTI \ DISPOSITIVI DI AUTOMAZIONE E SEGNALAZIONE \ REGISTRATORE CRONOLOGICO DI EVENTI

STAZIONE \ DISPOSITIVI E COMPONENTI \ DISPOSITIVI DI AUTOMAZIONE E SEGNALAZIONE \ SISTEMA DI REGISTRAZIONE DELLE PERTURBAZIONI (UNITA CENTRALE)

Circuiti generali d’impianto:

Carpenteria e caveteria

Filtro corrente continua

N/A

Montante AT lato conversione

STAZIONE\DISPOSITIVI E COMPONENTI\ALTRI DISPOSITIVI E COMPONENTI\CAPACITORE (elemento di HVDC)

Sala valvole

STAZIONE\DISPOSITIVI E COMPONENTI\IMPIANTI DI CONVERSIONE\PONTE DI CONVERSIONE LCC (elemento HVDC)

A ciascuno degli asset di ciascun livello sono, pertanto, associate delle classificazioni, impiegate proprio per classificare l’asset stesso, oggetto di associazione.

La mappatura completa delle classificazioni che verranno associate agli asset è stata ricavata dal documento: Mapping Anagrafica EAM Stazioni_Asset 20230912.xlsx, condiviso all’interno della cartella “Documenti Correlati”.

A ciascuna classificazione sono stati legati degli attributi, per mezzo dei quali sono gestite le informazioni specifiche di ogni asset al quale la classificazione è associata.

Per fare un esempio, sulla classificazione delle stazioni, alcuni degli attributi gestiti sono i seguenti:

STZ_DATA_IN_SERVIZIO DATA DI ENTRATA IN SERVIZIO

STZ_TENSIONE_ESERCIZIO TENSIONE NOMINALE DI ESERCIZIO

STZ_TIPO_COSTRUZIONE_IMPIANTO TIPO DI COSTRUZIONE DELLA STAZIONE

STZ_TIPO_CONDUZIONE_IMPIANTO TIPO DI CONDUZIONE DELLA STAZIONE

STZ_DATA_PRIMA_AUTOMAZIONE DATA DI PRIMA AUTOMAZIONE

STZ_CENTRO_TELECONDUZIONE CENTRO DI TELECONDUZIONE

STZ_STALLI_EQUIVALENTI STALLI EQUIVALENTI

STZ_TIPO_SISTEMA_CONTROLLO TIPO DI SISTEMA DI CONTROLLO

STZ_DATA_COSTRUZIONE DATA DI COSTRUZIONE

STZ_DATA_IN_CONSEGNA DATA DI PRESA IN CONSEGNA

STZ_DATA_ASSETTO_ATTUALE DATA DI ASSETTO ATTUALE

STZ_LIVELLO_TENSIONE_ESERCIZIO LIVELLO TENSIONE DI ESERCIZIO

STZ_POTENZA_TRASFORMAZIONE POTENZA DI TRASFORMAZIONE INSTALLATA

STZ_POTENZA_RIFASAMENTO_STATICO POTENZA DI RIFASAMENTO STATICO INSTALLATA

STZ_POTENZA_RIFASAMENTO_ROTANTE POTENZA DI RIFASAMENTO ROTANTE INSTALLATA

STZ_POTENZA_CONVERSIONE POTENZA DI CONVERSIONE INSTALLATA

STZ_NUM_SEZIONI_380 NUMERO DI SEZIONI 380 kV

STZ_NUM_SEZIONI_220 NUMERO DI SEZIONI 220 kV

STZ_NUM_SEZIONI_110_150 NUMERO DI SEZIONI 110-150 kV

STZ_NUM_SEZIONI_40_90 NUMERO DI SEZIONI 40-90 kV

STZ_NUM_SEZIONI_MT NUMERO DI SEZIONI MT

STZ_NUM_UNITA_HVDC NUMERO DI UNITÀ DI CONVERSIONE

STZ_NUM_TRASF_P380_S220 NUMERO DI TRASFORMAZIONI 380 / 220 kV

STZ_NUM_TRASF_P380_S110_150 NUMERO DI TRASFORMAZIONI 380 / 110-150 kV

STZ_NUM_TRASF_P380_S40_90 NUMERO DI TRASFORMAZIONI 380 / 40-90 kV

STZ_NUM_TRASF_P220_S110_150 NUMERO DI TRASFORMAZIONI 220 / 110-150 kV

STZ_NUM_TRASF_P220_S40_90 NUMERO DI TRASFORMAZIONI 220 / 40-90 kV

STZ_NUM_TRASF_P220_SMT NUMERO DI TRASFORMAZIONI 220 kV / MT

STZ_NUM_TRASF_P110_150_SMT NUMERO DI TRASFORMAZIONI 110-150 kV / MT

STZ_NUM_TRASF_P40_90_SMT NUMERO DI TRASFORMAZIONI 40-90 kV / MT

STZ_NUM_TRASF_ALTRO NUMERO DI TRASFORMAZIONI ALTRI RAPPORTI

STZ_REGIONE REGIONE

STZ_TIPO_IMPIANTO_CONSISTENZA TIPO DI IMPIANTO (CONSISTENZA)

STZ_NOTA ANNOTAZIONI

STZ_FUSO_RIFERIMENTO FUSO DI RIFERIMENTO

STZ_COORDINATA_EST COORDINATA EST

STZ_COORDINATA_NORD COORDINATA NORD

STZ_QUOTA_ORTOMETRICA QUOTA ORTOMETRICA

STZ_NUM_COORDINATE_1 NUMERO DI VERTICI AREA 1

STZ_NUM_COORDINATE_2 NUMERO DI VERTICI AREA 2

Sempre a titolo di esempio, per la classificazione del registratore cronologico di eventi, gli attributi gestiti sono i seguenti:

STZ_TENSIONE_AUSILIARIA TENSIONE AUSILIARIA DI ALIMENTAZIONE

STZ_NUM_INGRESSI_MAX NUMERO MASSIMO DI INGRESSI

STZ_PRESENZA_STAMPANTE_LOCALE PRESENZA STAMPANTE LOCALE

STZ_COSTRUTTORE_STAMPANTE_LOCALE COSTRUTTORE DELLA STAMPANTE LOCALE

STZ_TIPO_COMPONENTE_STAMPANTE_LOCALE DEFINIZIONE TIPO COSTRUTTORE DELLA STAMPANTE LOCALE

Con la medesima modalità di mappatura verranno integrate le informazioni relative agli asset delle stazioni HVDC e quelle degli asset SINCORNI. Di seguito, i relativi file di Mapping presenti all’interno della cartella “Documenti Correlati”:

Mapping asset HVDC: Mapping Anagrafica EAM Stazioni_HVDC_Asset 20231205.xlsx;

Mapping attributi HVDC: Mapping Anagrafica EAM Stazioni_HVDC_Dati 20231205.xlsx ;

Mapping attributi Sincroni: Mapping Anagrafica EAM Stazioni_Dati 20230912_rev.02.xlsx.

.

## Il modello dati dell’asset

Tutti gli elementi costituenti una stazione, dal primo al quarto livello, vengono gestiti per mezzo della struttura dati dell’asset. Quelli che in SAP erano gestiti come equipment, in IBM Manage sono sempre asset, ma la cui classificazione inizia con STAZIONE \ DISPOSITIVI E COMPONENTI \ … (per ulteriori dettagli vedere § 3.2.4.1).

L’oggetto principale nel quale sono salvate le informazioni si chiama ASSET, è costituito da una tabella nel database dell’applicativo e ogni record in questa tabella rappresenta uno specifico asset.

Le informazioni gestite in questa tabella vengono definite “di testata”, ossia hanno cardinalità 1:1 rispetto all’asset stesso. I due campi ASSETNUM e SITEID sono utilizzati in un indice a chiave univoca; poiché nel progetto Terna non saranno utilizzati altri siti, oltre a quello definito in § 1.3, nella tabella ASSET il campo ASSETNUM può essere considerato univoco. Viene popolato dal sistema con una chiave autogenerata. Il campo X3SAPID è utilizzato per salvare il codice di legame tra EAM e SAP (codice parlante per la sede tecnica SAP e non parlante, ma basato su sequenza, per gli equipment SAP).

In questa tabella (ASSET) sono state aggiunte delle colonne specifiche per la gestione di dati propri di Terna, da migrare (ETL) e/o allineare (connettore) da e verso SAP:

| Colonna DB Manage | Descrizione / Etichetta SAP -> Manage | Nota |
| --- | --- | --- |
| X3SAPID (§ 3.7) | Sede tecnica -> ID SAP | Questo campo è popolato sugli asset di ogni livello, con la seguente modalità: Livelli 1°, 2°, 3° basato su regular expression; Livello 4° basato su un contatore specifico; Provenienza: STRNO tranne per Dispositivi e componenti |
| X3SWERK | Divisione di ubicazione -> Distretto Territoriale | Legato a un dominio costruito sui gruppi di persone, definiti in Doc.1 Non presente su Dispositivi e componenti |
| X3INGRP | Divisione di pianificazione della manutenzione -> Unità Impianti | Legato a un dominio costruito sui gruppi di persone, definiti in Doc.1 Non presente su Dispositivi e componenti |
| X3ZZSOCP | Società Proprietaria | Campo a sola lettura Non presente su Dispositivi e componenti |
| X3BUKRS | Società | Campo a sola lettura, legato a un dominio (X3BUKRS) per gestirne la descrizione Non presente su Dispositivi e componenti |
| X3KOKRS | Controlling area | Campo a sola lettura Non presente su Dispositivi e componenti |
| X3GEWRK | Centro di lavoro responsabile | Legato a un dominio costruito sulla tabella X3GEWRK |
| X3EQFNR | Campo sort | Non presente su Dispositivi e componenti |

Nella tabella seguente sono invece presenti i campi standard della tabella ASSET, con l’informazione che permette il legame con i dati di provenienza SAP:

| Dato SAP | Colonna DB Manage | Descrizione / Etichetta SAP -> Manage | Nota |
| --- | --- | --- | --- |
| STTXU | STATUS | Stato utente -> Stato |  |
| INDBT | INSTALLDATE | In funzione dal -> Data di installazione |  |
| PLTXT o EQKTX | DESCRIPTION | Definizione sede tecnica -> Descrizione |  |

Altre strutture dati, con cardinalità 1:1 nei confronti dell’asset, che saranno utilizzate nel progetto Terna per indirizzare le seguenti informazioni di provenienza SAP:

Informazioni di toponomastica, quali Città, Paese, Provincia, Via, etc. Per queste tipologie di informazioni esiste una tabella specifica, la SERVICEADDRESS, verso la quale ogni asset realizza un legame per trattare le sue informazioni di toponomastica.

Un asset, censito nella tabella ASSET, per diventare una stazione, una sezione, un booster, un interruttore AT o qualsiasi altro elemento previsto nel § 2.1 deve essere associato ad una delle classificazioni previste nel file 000-stazioni-classificazioni e attributi.xlsm.

Questa operazione di classificazione provocherà l’inserimento nella tabella ASSETSPEC di tanti record quanti sono gli attributi previsti per la classificazione associata.

Ad esempio, quando un asset è classificato come Stallo, nella tabella ASSETSPEC verranno inseriti 13 record, uno per ciascuna delle 13 caratteristiche (attributi nel linguaggio del Manage) di seguito riportate:

STZ_TIPO_COSTRUZIONE

STZ_CORRENTE_MAX

STZ_PESO_SF6

STZ_CONFIGURAZIONE_DBS

STZ_DATA_COSTRUZIONE

STZ_DATA_IN_SERVIZIO

STZ_DATA_IN_CONSEGNA

STZ_POSIZIONE_STALLO

STZ_DATA_ASSETTO_ATTUALE

STZ_TIPO_SPCC_STALLO

STZ_DATA_IN_SERVIZIO_SPCC

STZ_ELEMENTO_INDISP

STZ_STALLO_AFFACCIATO_BLINDATO

come indicato nel file di mapping “Mapping Anagrafica EAM Stazioni_Dati 20230912.xlsx”.

### La gestione dei domini

Nel Manage con il termine domini si intendono gli elenchi valori sulla base dei quali possono essere valorizzati i campi presenti nelle differenti applicazioni.

I domini sono divisi nelle seguenti 2 tipologie, alfanumerico e tabella:

Dominio alfanumerico: permette di gestire un codice con la sua descrizione. La creazione di un nuovo dominio di questo tipo provoca il popolamento di una struttura dati già predisposta nel Manage, chiamata ALNDOMAIN che gestisce:

Il nome del dominio

Il valore del dominio

La descrizione del valore del dominio

Dominio di tipo tabella: permette di prendere dati da qualsiasi MBO del Manage, sia standard, come ad esempio PERSONGROUP che custom, come X3GEWRK. Quali dati dell’MBO mostrare per la scelta viene definito per mezzo di una condizione SQL, così come la validazione del dato inserito nel campo di destinazione del dominio.

#### Domini legati a campi di testata

In questo paragrafo, divisi sulle rispettive tabelle, sono riportati i domini coinvolti nel progetto Manage per Terna e utilizzati per popolare campi di testata della tabella ASSET.

Domini alfanumerici:

| Nome | Descrizione | Dove è usato | Nota |
| --- | --- | --- | --- |
| X3ZZSOCP | Elenco società proprietarie | ASSET. X3ZZSOCP | Il contenuto del dominio è allineato manualmente tra SAP ed EAM. Sono 2 valori. |
| X3KOKRS | Elenco controlling Area | ASSET. X3KOKRS | Il contenuto del dominio è allineato manualmente tra SAP ed EAM. È un solo valore. |
| X3BURKS | Elenco società SAP | ASSET. X3BUKRS | Il contenuto del dominio è allineato manualmente tra SAP ed EAM. Contiene 10 valori. |
| X3COSTRCOMPONENTE | Produttori del cespite | ASSET.X3COSTRUTTORECOMPONENTE | Dominio gestito in Maximo e aggiornato manualmente, inizialmente popolato con i valori forniti da SAP (circa 125 coppie di codice e descrizione) |

Domini di tipo tabella:

| Nome | Descrizione | Dove è usato | Nota |
| --- | --- | --- | --- |
| X3SWERK | Elenco dei dipartimenti trasmissione | ASSET.X3SWERK | Il contenuto del dominio è allineato manualmente tra SAP ed EAM. Sono 8 valori. Il dominio si basa su una query della tabella PERSONGROUP |
| X3INGRP | Elenco delle unità impianti | ASSET.X3INGRP | Il dominio mostra i valori in base a quanto popolato nel campo ASSET.X3SWERK. Il contenuto del dominio è allineato manualmente tra SAP ed EAM. Sono 33 valori. Il dominio si basa su una query della tabella PERSONGROUP. I valori sono selezionabili in base a quanto inserito nel campo X3SWERK |
| X3GEWRK | Elenco dei centri di lavoro | ASSET.X3GEWRK | Il dominio mostra i valori in base a quanto popolato nel campo ASSET.X3SWERK. I valori del dominio sono mantenuti allineati con quelli presenti in SAP per mezzo del connettore SAP – EAM. Sono circa 8K valori, selezionabili in base a quanto inserito nel campo X3SWERK |
| X3TIPOCOMPONENTE | Definizione tipo | ASSET.X3TIPOCOMPONENTE | Dominio gestito in Maximo e aggiornato manualmente, inizialmente popolato con i valori forniti da SAP (circa 1600) e i dati di transcodifica forniti dal business per popolare la colonna della classificazione. In fase di valorizzazione del campo su ASSET l’elenco dei valori possibili verrà filtrato in base alla scelta fatta in ASSET.X3COSTRUTTORECOMPONENTE e alla classificazione dell’asset stesso |
| X3STZ_FUNZ_ELE | Funzione dell’elemento | ASSET.X3FUNZIONEELEMENTO | Il dominio verifica la classificazione dell’asset per mostrare solo i valori possibili e gestisce la scelta con cui popolare X3RIFERIMENTOFASE |
| X3STZ_RIF_FASE_TAB | Elenco fasi | ASSET.X3RIFERIMENTOFASE | Il dominio contiene 3 valori: R, S, T. La scelta dei valori possibili verrà gestita in base a quanto inserito in ASSET.X£FUNZIONEELEMENTO. |

#### Domini legati ad attributi di classificazioni

In questo paragrafo, divisi sulle rispettive tabelle, sono riportati tutti i domini coinvolti nel progetto Manage per Terna e utilizzati per popolare attributi di classificazioni; se è indicata anche la classificazione significa che il dominio, a parità di attributo, cambia da classificazione a classificazione.

Domini alfanumerici:

| Nome | Descrizione | Attributo / Attributo - classificazione | Nota |
| --- | --- | --- | --- |
| X3CODICECOMPONENTE | Codice Componente | STZ_CODICE_COMPONENTE | Deve essere possibile inserire nell’attributo anche valori non presenti nel dominio. Campo in SAP: MAPAR, per il quale deve essere rimossa la validazione a help valori. |
| X3COSTRCOMPONENTE | Produttori del cespite | Utilizzato su tutti gli attributi STZ_COSTRUTTORE_*, ossia qualunque valori sia al posto dell’asterisco. | È il medesimo dominio legato a ASSET.X3COSTRUTTORECOMPONENTE. È utilizzato per gestire i costruttori di elementi che sono all’interno di un asset complesso e che non sono creati come asset. Se ad esempio, l’asset gruppo di continuità monta un motore Fiat o Mercedes, questa informazione viene salvata nell’attributo STZ_COSTRUTTORE_MOTORE definito sull’asset gruppo di continuità. |

Domini di tipo tabella:

| Nome | Descrizione | Attributo / Attributo - classificazione | Nota |
| --- | --- | --- | --- |
| X3STZRIFASS | Asset di riferimento | STZ_RIF_ASSET | Dominio per indicare su quale asset di quarto livello è montato l’asset, sempre di quarto livello, per cui deve valere questo legame (esempio del comando interruttore in relazione all’interruttore su cui agisce) |
| X3SEDIINDISP | Codici indisponibilità | STZ_ELEMENTO_INDISP | Dominio per associare i codici indisponibilità alla linea o alla stazione e suoi elementi, presi dalla tabella X3SEDIINDISP |

## Il modello dati per i legami tra asset

Precedentemente abbiamo visto come si costruiscono i singoli componenti di una stazione (compresa la stazione stessa quale componente di primo livello), qualificandoli per mezzo dell’associazione con una determinata classificazione. Vediamo ora come i differenti componenti devono essere messi in relazione tra loro per permettere la modellazione di una specifica stazione.

Il modo con cui il Manage permette di gestire le gerarchie multilivello è quello del legame padre – figlio. La struttura dati impiegata è contenuta sempre nella tabella ASSET e opera per mezzo dell’informazione, su ciascun’asset, di quale è suo padre.

Nell’ambito delle stazioni verranno gestite gerarchie e relazioni, le prime tra i quattro livelli di una stazione, mentre le seconde per collegare tra loro asset differenti, qualsiasi sia il livello di appartenenza alla gerarchia, della medesima stazione, di stazioni differenti o appartenenti a linee (par. § 3.9).

### Legami particolari tra asset

Per permettere una rappresentazione e una gestione degli asset il più fedele possibile rispetto alla realtà e allo stesso tempo mantenere la prevista struttura a 4 livelli, è implementata la possibilità di costituire legami tra asset di quarto livello, per mezzo di uno specifico attributo o tramite una relazione.

#### Legami tra asset di quarto livello gestito per mezzo di un attributo

Nella realtà operativa di Terna alcuni asset definiti al quarto livello sono in realtà funzionalmente dipendenti o al servizio di altri asset, anch’essi di quarto livello. Un esempio di questo è l’asset di comando per l’interruttore o il sezionatore che manovrano (ossia sono al servizio di…) uno specifico interruttore o sezionatore.

Le classificazioni che hanno l’attributo di interesse sono le seguenti:

STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ COMANDO PER INTERRUTTORE AT BLINDATO

STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ COMANDO PER SEZIONATORE AT BLINDATO

STAZIONE \ DISPOSITIVI E COMPONENTI \ MACCHINARIO DI POTENZA \ ISOLATORE PASSANTE

Per le regole connesse allo spostamento di questi asset, riferirsi al § 3.3.3 e § 3.3.4

#### Asset aggregati

Con questo termine si indicano gli asset di quarto livello che per motivi funzionali devono essere considerati tra lori inscindibili; per ottenere questo, la creazione (qui descritta) e lo spostamento (§ 3.3.5) verranno fatti per mezzo di funzioni specifiche.

Il caso in esame riguarda la creazione di asset nella cui classificazione, oltre a …\ DISPOSITIVI E COMPONENTI \... sia presente, in uno dei livelli:

…\ MACCHINA \...

…\ VARIATORE \...

…\ SISTEMA DI RAFFREDDAMENTO \...

Tra queste 3 tipologie di asset deve essere gestita una interdipendenza, da costituirsi al momento della creazione degli asset stessi.

La precedente tripletta (MACCHINA, VARIATORE , RAFFREDDAMENTO) costituisce 3 asset di 4° livello, interdipendenti dal punto di vista della movimentazione, figli dei seguenti asset di 3° livello e che hanno stesso SAPID.

Figli del 2°L TRASFORMAZIONI \

TRASFORMATORE 3°L (5 tipologie: a 3 avvolgimenti, AT/MT, di gruppo, di interconnessione, MT/MT)

BOOSTER 3°L

Nell’ambito delle trasformazioni devono essere gestiti anche i seguenti controlli aggiuntivi:

decidere se creare anche il VARIATORE e/o il RAFFREDDAMENTO;

figli del medesimo trasformatore possono esistere al massimo 3 asset MACCHINA, con o senza VARIATORE e/o RAFFREDDAMENTO;

Figli del 2°L RIFASAMENTI \

REATTORE 3°L

Nell’ambito dei rifasamenti devono essere gestiti anche i seguenti controlli aggiuntivi:

decidere se creare anche il VARIATORE e/o il R0041FFREDDAMENTO;

figli del medesimo reattore possono esistere uno oppure tre asset MACCHINA, con o senza VARIATORE e/o RAFFREDDAMENTO;

Per tutti i casi precedenti il legame tra una MACCHINA ed il suo specifico VARIATORE e RAFFREDDAMENTO è gestito riportando nel campo X3SAPID del VARIATORE e del RAFFREDDAMENTO il medesimo X3SAPID della MACCHINA a cui si riferiscono.

#### Legami tra asset di stazione per mezzo di relazione

Questa opzione permette di realizzare delle relazioni tra asset, superando il limite imposto dalle strutture padre figlio; per farlo si utilizza la relazione STAZSTAL. I casi trattati in questo paragrafo non includono il caso specifico di relazione stazione – linea (STAZLIN), affrontato nel § 3.9.

Nell’elenco seguente sono indicati gli asset di 3° livello della stazione, tra i quali è necessario poter costituire delle relazioni:

STALLO ßà TRASFORMATORE

STALLO ßà BOOSTER

STALLO ßà REATTORE

STALLO ßà BATTERIA CONDENSATORI

STALLO ßà PHASE SHIFTING TRANSFORMER

STALLO ßà COMPENSATORE SINCRONO

L’operatività a sistema prevede di scegliere tra gli asset classificati come STALLO, da mettere in relazione a un asset della medesima stazione e appartenente a una delle 6 classificazioni dell’elenco precedente.

## Genesi e ownership dei dati delle stazioni

In questo paragrafo si affronta il tema della creazione degli asset nel Manage e dell’ownership del dato.

Il primo livello di una nuova stazione viene creato nell’applicativo SAP negli stati di PROGE, CANTI o DATT. In questo caso il connettore si occuperà di crearlo nel Manage nello stato di INAT (per gli stati, vedere § 3.3.2); nel momento in cui in SAP viene messo in esercizio, il connettore SAP – Manage si occuperà del suo cambio di stato, da INAT a DATT.

Alcune informazioni della stazione rimarranno comunque di pertinenza SAP, non imputabili né modificabili dagli utenti del Manage, come ad esempio le informazioni contabili.

Altre informazioni, come ad esempio la valorizzazione degli attributi o i cambi di stato verranno invece gestiti dal Manage verso SAP e di conseguenza dovranno essere non modificabili in SAP.

Gli asset di secondo, terzo e quarto livello sono invece creati e modificati unicamente nel Manage, e per mezzo del connettore inviati a SAP, dove saranno solo consultabili ma non modificabili dagli utenti.

## Strutture dati accessorie alla gestione delle stazioni

Nei paragrafi che seguono vengono spiegate le strutture dati per permettere la selezione dei codici di indisponibilità da collegare alla stazione e ad alcuni dei suoi asset componenti.

### Codici di indisponibilità

I codici di indisponibilità sono gestiti per mezzo della tabella X3SEDIINDISP. Non possono essere creati nel Manage, ma solo recepiti, per mezzo del connettore, da SAP, dove sono gestiti come delle sedi tecniche, e associati agli asset di una stazione con la valorizzazione di un apposito attributo.

La tabella X3SEDIINDISP è composta dalle seguenti colonne:

STRNO: è il codice parlante SAP della sede tecnica;

TPLNR: è il codice interno SAP della sede tecnica;

PLTXT: è la descrizione della sede tecnica, ossia del codice indisponibilità;

STTXT: è lo stato della sede tecnica di indisponibilità;

TIPOELEMENTO: tipologia di elemento di indisponibilità;

L’associazione delle sedi tecniche di indisponibilità con gli asset di stazione è fatta per mezzo dell’attributo STZ_ELEMENTO_INDISP; l’utente deve essere libero nella scelta del codice da inserire (quindi sia tra quelli che iniziano con L che quelli che iniziano con Z), aiutandosi filtrando sulla colonna PLTXT (Descrizione) o sulla colonna STRNO (Codice SAPID).

## Navigazione dei dati delle stazioni in Manage

Lo scopo di questo paragrafo è spiegare il modo con cui gli utenti del Manage interrogheranno e visualizzeranno le informazioni delle stazioni.

La filosofia generale seguita è quella di rendere immediatamente evidente all’utente la composizione di una stazione, mostrando in una tabella tutti gli asset di secondo livello che la compongono.

La nuova applicazione sviluppata a questo scopo si chiama Stazioni, la quale è impostata per mostrare l’elenco di tutti gli asset associati alla classificazione STAZIONE; selezionando una stazione, questa viene caricata e l’applicazione mostra le differenti schede che la compongono. Nei paragrafi seguenti sono spiegate le informazioni presenti in ciascuna scheda dell’applicazione.

### Stazione

Questa è la scheda principale dell’applicazione, la prima che si apre nel momento in cui si seleziona una stazione dall’elenco.

Nella parte superiore dell’applicazione vengono mostrate le informazioni direttamente legate alla stazione, come ad esempio:

Descrizione della stazione, ossia il suo nome, ad esempio ROMA NORD;

Codice SAP della stazione, esempio 31401B;

Stato della stazione, esempio ATTV;

Dipartimento trasmissione di appartenenza, 2605 - Dipartimento Trasmis. Centro

Unità impianti di appartenenza, ULN – Unità Impianti Lazio Nord

Centro di lavoro responsabile

Al di sotto dei campi sopra indicati è inserita una tabella che contiene tutti gli asset di secondo livello della stazione e mostra le seguenti colonne (elenco non esaustivo):

Codice SAP;

Codice univoco dell’asset nel Manage;

Descrizione dell’asset di secondo livello;

Stato dell’asset di secondo livello;

Classificazione di appartenenza;

#### Secondo livello stazione

Nella tabella del secondo livello sono mostrati tutti gli asset direttamente figli della stazione visualizzata in testata della scheda, fatta esclusione di quegli asset, normalmente al quarto livello, ossia quelli con …\ DISPOSITIVI E COMPONENTI \... nella classificazione, ma che sono stati smontati e quindi ora figli della stazione: questi asset sono visualizzati nella scheda successiva, quella appunto degli asset smontati (§ 2.6.2).

Di default le righe dei secondi livelli sono mostrate chiuse; aprendole, ossia aprendo la riga della tabella degli asset di secondo livello, sono mostrate le tabelle con gli asset di terzo e quarto livello, relativi all’asset di secondo livello, oggetto della riga che è stata aperta.

#### Terzo livello stazione

Il terzo livello mostra, coerentemente con il secondo livello selezionato, i rispettivi asset figli.

In questa tabella i dati mostrati sono i seguenti:

Codice univoco dell’asset;

Descrizione dell’asset;

Classificazione di appartenenza dell’asset;

#### Quarto livello stazione

Il quarto livello mostra, coerentemente con il terzo livello selezionato, i relativi asset. Per semplificare la leggibilità delle informazioni, nella testata della tabella è riportato l’asset di terzo livello a cui gli asset di quarto livello si riferiscono. Nella tabella dei quarti livelli sono mostrate le seguenti colonne:

Codice univoco dell’asset;

Descrizione;

Classificazione di appartenenza dell’asset;

Data di installazione;

Stato;

### Smontati

In questa scheda sono mostrati, in formato tabellare, tutti gli asset nella cui classificazione è presente il livello …\ DISPOSITIVI E COMPONENTI \... e che sono figli diretti della stazione.

### Dati stazione

In questa scheda, in formato tabellare, sono mostrati tutti gli attributi della classificazione STAZIONE della stazione selezionata. I dati mostrati sono i seguenti:

Codice dell’attributo;

Descrizione dell’attributo;

Valore data, compilabile e modificabile se pertinente al relativo codice attributo e coerentemente alle regole di protezione;

Valore alfanumerico, compilabile e modificabile se pertinente al relativo codice attributo e coerentemente alle regole di protezione;

Valore numerico, compilabile e modificabile se pertinente al relativo codice attributo e coerentemente alle regole di protezione;

Unità di misura, compilabile e modificabile se pertinente al relativo codice attributo e coerentemente alle regole di protezione;

Valore da tabella, compilabile e modificabile se pertinente al relativo codice attributo e coerentemente alle regole di protezione

### Asset di stazione in relazione

In questa scheda sono mostrati tutti gli asset della stazione, tra i quali è stata costituita una relazione per mezzo di STAZSTAL, come visto nel § 2.3.1.3 .

### Linee in relazione

In questa scheda sono mostrate tutte le linee che hanno una relazione con la stazione selezionata, ossia è stata realizzata una relazione di tipo STAZLIN tra un asset di stazione e un asset di linea, come spiegato nel par. § 3.9 (usualmente un asset figlio della sezione della stazione e il sostegno della linea adiacente alla stazione):

Codice SAPID della linea;

Codice Manage della linea;

Descrizione della linea;

# Utenti e funzionalità applicative dell’anagrafica Stazioni

Nel seguente capitolo sono trattati gli attori del processo di gestione delle stazioni (dal § 3.1) e verranno illustrate, ad alto livello, le funzionalità applicative dell’anagrafica delle stazioni (dal § 3.2) tratte dagli incontri relativi ai casi d’uso (TERNA_EAM_RRQ_REQUT_ANA_CasiDiUso_20230414_V6.1.docx)

Le schermate riportate da IBM MAS Manage sono state ricavate dall’ambiente di PoC e non sono da considerarsi definitive, alla stregua dei nomi delle funzioni e delle applicazioni, che potranno variare in base a vincoli o esigenze implementative.

## Attori dei processi

In questo paragrafo si riportano gli attori dei differenti scenari d’uso analizzati.

Gli attori individuati appartengono ai seguenti gruppi:

AM: gestori centrali dell’asset management;

UI: unità impianti. Sono i gruppi di specialisti della manutenzione, distribuiti sul territorio italiano;

Dispacciamento

La gestione centralizzata di AM serve a garantire che tutti i fruitori dei dati di anagrafica (al momento DT e UI) si possano basare su informazioni univocamente riconosciute e validate, come ad esempio le classificazioni per qualificare un asset o le specifiche tecniche per definirlo.

### Organigramma di riferimento

Per permettere la gestione di visibilità e azioni su elementi appartenenti ad unità impianti differenti del medesimo distretto / dipartimento territoriale, dovranno essere gestiti dei rapporti di parentela tra UI e DT. Di seguito l’organigramma di riferimento per eventuali, ulteriori relazioni configurabili in IBM MAS Manage.

Figura 5 - Organigramma Terna

### Gruppi di protezione e gruppi di persone

I gruppi di protezione (gestiti per mezzo dell’applicazione Gruppi di protezione) servono ad attribuire l’utilizzo delle applicazioni di IBM MAS Manage e delle specifiche funzionalità connesse. Un utente può appartenere a più gruppi di protezione.

I gruppi di persone servono a raggruppare le persone che devono condividere criteri e caratteristiche. Le 33 UI (Unità Impianti) e gli 8 DT (Dipartimenti o Distretti Trasmissione) saranno gestiti con i gruppi di persone (applicazione Gruppi di persone); a questi gruppi vengono legati, ad esempio, i criteri di filtro per stabilire la modificabilità piuttosto che la sola lettura degli asset.

Analogamente ai gruppi di protezione, un utente può appartenere a più gruppi di persone.

### Gestori centrali dell’asset management

Per la gestione degli attori di AM (Asset Management) verranno creati i seguenti gruppi di protezione.

Gruppo Centro 1:

Gestisce tutte quelle informazioni che possono essere definite comuni, ossia quelle che influenzano e supportano il lavoro di tutte le DT. Le tipologie di oggetti attualmente individuati sono:

Classificazioni;

Aggiunta e cancellazione di attributi direttamente nella tabella delle specifiche tecniche sugli asset;

Elenchi valori;

Gestione dei valori relativi alla funzione elemento e alla definizione tipo.

Aggiornamento della tabella di verità (§ 3.2.3) per la gestione di nuovi asset;

Ha visibilità e possibilità di azione su tutta l'anagrafica, esclusi i dati di specifici attributi (vedi § 3.6.1), per i quali sarà necessaria l’appartenenza a determinati gruppi di persone per poterli editare

Gruppo Centro 2:

Ha visibilità e possibilità di azione su tutta l'anagrafica, esclusi i dati di specifici attributi (vedi § 3.6.1), per i quali sarà necessaria l’appartenenza a determinati gruppi di persone per poterli editare

Gruppo Centro 3:

Ha visibilità su tutta l'anagrafica, ma in sola lettura

### Personale delle unità impianti

Per la gestione degli attori delle Unità Impianti verrà creato il gruppo di protezione UI (X3UI01 - Gruppo unità impianti 1), con i seguenti privilegi:

Creazione e aggiornamento di tutti i dati di anagrafica relativi a Linee e Stazioni di pertinenza, esclusi i dati di specifici attributi (vedi § 3.6.1), per i quali sarà necessaria l’appartenenza a determinati gruppi di persone per poterli editare.

Visibilità di tutta l'anagrafica.

Il legame DT – UI è invece implementato per mezzo dell’applicazione Gruppi di persone, con la quale viene gestita la relazione 1:n tra DT e UI. Le informazioni di riferimento sono state prese dal documento 20221222_DT.pdf, di cui si riporta lo schema utilizzato, ossia 8 gruppi di persone per gli otto distretti / dipartimenti e 33 gruppi, ciascuno figlio di un DT, per le differenti unità impianti.

L’operatività su IBM MAS Manage del personale delle UI sarà il medesimo al di sotto della stessa DT. Per quanto riguarda invece la sola visibilità delle informazioni di anagrafica degli asset non vi saranno vincoli.

Figura 6 - Legame DT – UI

Ogni colonna rappresenta la composizione di una DT (parte blu, in alto) mentre le righe azzurre UNITA’ IMPIANTI, nell’incrocio con la rispettiva colonna, definiscono il nome e la composizione della specifica unità impianti. Ad esempio, la DT NORD-OVEST è composta da 4 UI, Pont S. Martin, Novara, Torino e Genova.

I codici utilizzati per gestire DT e UI sono i medesimi impiegati in SAP e riportati nelle due tabelle seguenti.

Figura 7 - Codici DT

Figura 8 - Codici UI

Per l’associazione DT – UI riferirsi al contenuto di Figura 6

### Personale del dispacciamento

Per la gestione degli attori del Dispacciamento verrà creato il gruppo di protezione Dispacciamento, con i privilegi:

Visibilità su tutta l'anagrafica, ma in sola lettura

Il personale del dispacciamento interagisce nel processo per la decisione di una parte del codice del primo livello di linee e stazioni: digit da 3 a 5 dell’ID descrittivo SAP. Dialoga con le unità impianti, ma non ha un ruolo funzionale in IBM MAS Manage.

## Attività generali che coinvolgono i dati

Questo capitolo ha l’obiettivo di riportare il disegno di alto livello dei requisiti funzionali, specifici per ciascuno dei casi d’uso raccolti durante l’analisi degli scenari dell’anagrafica, divisi tra creazione, aggiornamento e interrogazione.

### Creazione

In questo ambito ricadono tutte le attività per le quali viene creata una o più nuove entità in IBM MAS Manage. Con il termine entità non si intendono semplicemente dei nuovi record, ma la creazione e popolamento di un insieme di oggetti che nel loro insieme rappresentano oggetti di business complessi, nello specifico gli asset, per mezzo dei quali saranno gestiti tutti gli elementi dell’anagrafica degli impianti (ossia gli equipment e le sedi tecniche di SAP), con le eventuali informazioni a corredo, come le relazioni e le classificazioni. La creazione e la gestione di linee e stazioni verrà fatta per mezzo di due nuove applicazioni, Linea e Stazione, il cui design sarà ottimizzato sulla base delle due famiglie di oggetti che verranno gestite.

In generale, i dati presenti su elementi di livello superiore non sono fisicamente replicati verso i livelli inferiori; da esempio, l’informazione dell’unità impianti (U.I.) di una stazione non viene replicata sugli asset dei livelli sottostanti, ma da ciascun livello sottostante sarà sempre possibile, grazie all’appartenenza alla stazione stessa, conoscere il rispettivo U.I.

### Creazione di una nuova stazione

Per lo svolgimento delle funzioni relative alle stazioni si utilizzerà la nuova applicazione Stazioni. La creazione di una nuova stazione può nascere solo conseguentemente alla creazione di un nuovo primo livello in SAP; l’integrazione SAP <-> IBM MAS Manage si occuperà di renderlo disponibile anche in IBM MAS Manage.

Per la creazione di nuovi asset di secondo, terzo o quarto livello, si agirà sempre nell’applicazione Stazioni, dove è gestita anche la codifica descrittiva ID SAP in base al tipo di elemento che viene creato.

Asset di 2° livello

Per mezzo della funzione di menu “Crea Asset di 2° livello” viene mostrata una finestra di dialogo, in cui l’utente sceglie la classificazione dell’asset da creare; sono visualizzate solo classificazioni con due livelli, figlie della classificazione STAZIONE, come ad esempio STAZIONE \ ACCUMULI oppure STAZIONE \ TRASFORMAZIONI (vedi § 2.6.1.1). Conseguentemente alla scelta della classificazione, potrebbe essere necessario inserire un ulteriore dettaglio (vedi § 3.7.1) per qualificare l’ID SAP da generare. Deve essere anche possibile la creazione degli asset di 4° livello, ossia quelli classificati come …\ DISPOSITIVI E COMPONENTI \..., in questo caso non verificati per mezzo della tabella di verità.

Asset di 3° livello

Dall’asset di 2° livello di cui l’asset che si sta creando dovrà essere figlio, usando il menu contestuale, si seleziona la voce “Crea asset di terzo livello”. Come conseguenza, viene mostrata una finestra di dialogo, in cui l’utente sceglie la classificazione dell’asset da creare; sono visualizzate solo classificazioni con tre livelli, figlie della classificazione dell’asset di 2° livello dal quale è stato richiamato il menu contestuale. Ad esempio, riprendendo il precedente esempio, STAZIONE \ ACCUMULI \ SETUP oppure STAZIONE \ TRASFORMAZIONI \ TRASFORMATORE (vedi § 2.6.1.2). Conseguentemente alla scelta della classificazione, potrebbe essere necessario inserire un ulteriore dettaglio (vedi § 3.7.1) per qualificare l’ID SAP da generare.

Asset di 4° livello

Dall’asset di 3° livello di cui l’asset che si sta creando dovrà essere figlio, usando il menu contestuale, si seleziona la voce “Crea asset di quarto livello”. Come conseguenza, viene mostrata una finestra di dialogo, in cui l’utente sceglie la classificazione dell’asset da creare; sono visualizzate solo le classificazioni compatibili in base ai valori della tabella di verità (§ 3.2.3). Riprendendo il precedente esempio:

STAZIONE \ DISPOSITIVI E COMPONENTI \ MACCHINARIO DI POTENZA \ TRASFORMATORE DI POTENZA \ MACCHINA \ TRASFORMATORE A 3 AVVOLGIMENTI

(vedi § 2.6.1.3).

Da quanto sopra si ricava che la creazione di elementi di livello successivo richiederà sempre la precedente creazione dell’elemento padre, ossia quello del corrispondente livello precedente. Ad esempio, la creazione di un asset trasformatore richiederà la precedente creazione di un asset trasformazioni, a sua volta figlio della stazione creata come primo livello. Successivamente, figli del trasformatore, potranno essere creati tutti gli asset di categoria … \ DISPOSITIVI E COMPONENTI \ … (§ 2.2) a questo associati, ottenendo il risultato seguente:

Figura 9 - Trasformazioni, livelli 2°, 3° e 4°

La codifica descrittiva ID SAP, seguendo il caso della figura precedente, viene inserita da una procedura automatica, richiamata dall’azione di creazione dell’utente in ciascuno dei tre step precedenti e validata come segue (applicando le regular expression dei criteri sisnet per i livelli 2° e 3°):

STAZIONE: (^3[0-6][0-9]{3}[A-Z]$) ad esempio 31401B

TRASFORMAZIONI: (^3[0-6][0-9]{3}[A-Z]-77$), ad esempio 31401B-77

TRASFORMATORE: (^3[0-6][0-9]{3}[A-Z]-77-10[0-9\|A-Z\|-]{2}$), esempio 31401B-77-10--

TRASFORMATORE AT/MT: nnnnnnnnnn, ad esempio 2123123123

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

### Gestione della tabella di verità

Lo scopo dell’applicazione e quindi della tabella di verità è quello di controllare la coerenza degli asset di 4° livello rispetto a quelli di 3° di cui sono figli, impedendo, ad esempio di installare un interruttore MT a vuoto come figlio di un trasformatore.

Le colonne gestite nella tabella di verità sono le seguenti:

CLASSE_PADRE: Classificazione di 3° livello. In questo campo possono essere inserite solo le classificazioni che arrivano al terzo livello, come da esempio STAZIONE \ SEZIONE \ STALLO o la classificazione STAZIONE \ TRASFORMAZIONI \ TRASFORMATORE, ossia quelle che possono contenere gli asset fisici ossia quelli classificati come … \ DISPOSITIVI E COMPONENTI \ …

CLASSE_FIGLIO: Classificazione di tipologia … \ DISPOSITIVI E COMPONENTI \ …

Tutti i dispositivi e componenti che deve essere possibile creare in associazione con uno specifico terzo livello dovranno essere presenti in questa tabella, associando in uno stesso record CLASSE_PADRE e CLASSE_FIGLIO, quindi, se deve essere possibile creare un interruttore MT a vuoto, nella tabella di verità sarà stato inserito un record con i seguenti valori:

CLASSE_PADRE: STAZIONE \ SEZIONE \ STALLO

CLASSE_FIGLIO: STAZIONE \ DISPOSITIVI E COMPONENTI \ APPARECCHIATURE MT \ INTERRUTTORE MT A VUOTO

Per il rispetto della coerenza degli elementi che non sono classificati come …\DISPOSITIVI E COMPONENTI\... (siano essi di secondo, terzo o quarto livello) non è invece necessario l’impiego della tabella di verità ma sarà sufficiente quanto già strutturato nella classificazione, come nel seguente esempio:

Un asset classificato come STAZIONE \ TRASFORMAZIONI \ BOOSTER può essere solo figlio di un altro asset, classificato come STAZIONE \ TRASFORMAZIONI;

Un asset classificato come STAZIONE \ STRUTTURE E IMPIANTI TECNOLOGICI \ FABBRICATI INDUSTRIALI può essere solo figlio di un altro asset, classificato come STAZIONE \ STRUTTURE E IMPIANTI TECNOLOGICI;

L’asset la cui classificazione, al quarto livello è STAZIONE \ CONTROLLO GENERALE DI IMPIANTO \ CIRCUITI GENERALI DI IMPIANTO \ CARPENTERIA E CAVETTERIA, può essere solo figlio di STAZIONE \ CONTROLLO GENERALE DI IMPIANTO \ CIRCUITI GENERALI DI IMPIANTO

Questo significa che la struttura gerarchica delle classificazioni, escludendo dispositivi e componenti, è direttamente utilizzabile per fornire direttamente l’elenco dei possibili asset figli, guidando le possibilità di scelta dell’utente.

### Creazione e manutenzione di classificazioni e attributi

Le classificazioni permettono di qualificare gli asset a cui sono associate, popolando automaticamente la tabella delle specifiche tecniche utilizzate per definire dal punto di vista tecnico l’asset. Le classificazioni, con le relative specifiche associate, sono gestibili direttamente dagli utenti centrali di AM, come riportato nel § 3.1.3.

A livello di attributo, per classificazioni differenti, possono essere gestiti differenti domini e l’aggiunta o la rimozione di un attributo su una classificazione si propagherà su tutte le specifiche tecniche generate dalla classificazione oggetto della modifica.

#### La classificazione “STAZIONE \ DISPOSITIVI E COMPONENTI \ …”

Le classificazioni che iniziano con STAZIONE \ DISPOSITIVI E COMPONENTI \ … sono utilizzate per classificare gli asset di 4° livello. Deve essere possibile associare questa categoria di classificazioni agli asset solo utilizzando l’ultimo livello della classificazione stessa; ad esempio, la seguente classificazione:

STAZIONE \ DISPOSITIVI E COMPONENTI \ MACCHINARIO DI POTENZA \ ISOLATORE PASSANTE \ ISOLATORE PASSANTE OLIO/SF6

potrà essere associata solo arrivando all’ultimo livello (ISOLATORE PASSANTE OLIO/SF6) ossia quello che non ha figli.

Non sarà possibile quindi classificare un asset come ISOLATORE PASSANTE usando la classificazione:

STAZIONE \ DISPOSITIVI E COMPONENTI \ MACCHINARIO DI POTENZA \ ISOLATORE PASSANTE

Oppure, ancora, come \ MACCHINARIO DI POTENZA utilizzando:

STAZIONE \ DISPOSITIVI E COMPONENTI \ MACCHINARIO DI POTENZA

## Aggiornamento

### Popolamento e modifica di attributi

Nel corso della vita operativa di un asset potrà essere necessario modificare il valore degli attributi che lo qualificano. Lo scenario d’uso più semplice è quello in cui si agisce modificando direttamente il valore presente a sistema.

Nel momento in cui viene inserito un nuovo asset, a meno degli attributi che presentano dei valori di default o di eventuali regole di compilazione automatica (§ 3.6.2), i valori dovranno essere imputati manualmente, a rappresentazione dell’effettiva realtà operativa dell’oggetto creato.

Se molteplici oggetti creati dovranno assumere il medesimo valore per un determinato attributo (ad esempio il diametro o il materiale, ossia il valore di una o più caratteristiche tecniche che si ripetono uguali per tutti i conduttori di una linea elettrica), potrà essere utilizzata una funzione di aggiornamento massivo di asset, con la seguente operatività:

Impostazione di un criterio di filtro che selezioni tutti gli asset sui quali si deve agire;

Richiamo della funzione di Modifica/Sposta asset;

Eventuale affinamento della selezione, con nuovo, ulteriore filtro sui valori ottenuti dal primo punto o rimozione puntuale di asset dalla selezione;

Indicazione degli attributi sui quali agire e relativi valori da impostare

Poiché IBM MAS Manage registrerà il momento dell’esecuzione di ogni modifica, dovrà essere cura dell’utente indicare da quale data il nuovo valore dovrà essere considerato presente a sistema (di default popolato con la data di sistema del momento in cui si esegue l’aggiornamento).

### Gestione degli stati

Nel file Excel allegato sono riportati, per ciascuna classificazione EAM, tutti gli stati disponibili, le relative regole di gestione e visibilità per gli asset di Linee e Stazioni. Tale file costituisce il riferimento funzionale per la verifica della gestione degli stati configurata a sistema.

Nel seguito vengono descritte le principali logiche di gestione degli stati in EAM, con particolare riferimento alla corrispondenza con gli stati standard IBM MAS Manage, alle regole di visibilità da GUI e ai comportamenti applicativi previsti per i diversi livelli e tipologie di asset.

Nello sheet dedicato “Stati EAM” del file “Visibilità stati_EAM_” viene riportata la corrispondenza tra gli stati base di IBM MAS Manage e gli alias creati in EAM. Le regole applicative associate agli stati alias derivano dai tre stati standard IBM MAS Manage:

NOT READY: stato iniziale dei nuovi asset;

OPERATING: stato operativo che consente l’utilizzo completo delle funzionalità manutentive;

DECOMMISSIONED: stato associato ad asset non più in uso e non selezionabili per attività manutentive.

In IBM MAS Manage i relativi alias saranno:

DATT – Da attivare, associato a NOT READY;

ATTIV – Attiva, associato a OPERATING;

DEMO – Demolita/Dismessa, associato a DECOMMISSIONED.

EAM prevede obbligatoriamente la gestione dello stato sugli asset e richiede la presenza di uno stato di default in fase di creazione dell’asset, identificato con lo stato DATT.

Lo stato INAT – Inattivo è stato introdotto per consentire la visualizzazione in IBM MAS Manage degli asset di primo livello creati in SAP e ancora gestiti lato SAP tramite gli stati PROG, CANT e DATT SAP. Finché l’asset permane nello stato INAT, esso risulta in sola lettura e non modificabile da IBM MAS Manage.

Il sistema prevede inoltre una logica basata sull’attributo booleano GESTSTAT, che consente di definire, a livello di singola classificazione, se lo stato dell’asset debba essere visibile e modificabile tramite GUI:

GESTSTAT = “SI”: lo stato risulta visibile e modificabile;

GESTSTAT = “NO”: lo stato non risulta visibile né modificabile da interfaccia grafica.

Gli stati DIST (Distrutto/Alienato) e DELE (Cancellato) costituiscono un’eccezione a tale logica, in quanto rimangono sempre disponibili e selezionabili indipendentemente dal valore assunto da GESTSTAT. Una volta che un asset viene posto in uno dei suddetti stati, esso non risulta più visibile nelle applicazioni “Stazioni”, poiché tali applicazioni prevedono un filtro predefinito che nasconde automaticamente gli asset in stato DIST e DELE. La visualizzazione rimane comunque possibile mediante rimozione del filtro sul campo “STATO”.

|  |  |  |  |  | STAZIONI |  |
| --- | --- | --- | --- | --- | --- | --- |

OP_258_W1 Cambio stato massivo

Al fine di consentire l’aggiornamento massivo dello stato degli asset di Linee e Stazione, oggi possibile solo a livello singolo asset tramite l’apposita funzionalità “Cambio Stato”, è stata richiesta l’implementazione di una nuova funzione di cambio stato massivo per l’anagrafica. In particolare, essa consentirà di attivare più asset attualmente in stato DATT, stato che impedisce la corretta generazione di policy e attività sui nuovi asset.

La nuova funzionalità sarà disponibile all’interno delle applicazioni “Linee” e “Stazioni” nella sezione “Altre azioni” e dovrà fare quanto segue:

Visualizzare una finestra di dialogo con l’elenco degli asset in stato DATT per la sede tecnica selezionata, consentendo la selezione multipla tramite checkbox;

In base alla tipologia degli asset selezionati lo stato verrà modificato rispettando il controllo di coerenza definiti in Anagrafica W1:

2° livelli (Linee) 3° livelli (Stazione) lo stato verrà modificato da DATT ad ATTV

4° livelli (Linee/Stazioni) lo stato verrà modificato da DATT a ESER

### Gestione dello spostamento di asset nella stazione e tra stazioni dello stesso Dipartimento di Trasmissione

All’interno di una stazione o tra stazioni del medesimo DT, gli asset (intendendo per asset solo quelli associati a una classificazione contenente … \ DISPOSITIVI E COMPONENTI \...) possono essere spostati solo in base alla regola della tabella di verità (§ 3.2.3) oppure possono diventare sempre figli del primo livello Stazione, senza subire nessuna validazione.

Nel momento in cui si sposta un asset (modificandone il padre) la cui classificazione gestisce l’attributo STZ_RIF_ASSET, il contenuto dell’attributo in questione deve essere resettato. Lo stesso reset deve essere fatto quando si sposta l’asset il cui codice univoco è contenuto nell’attributo STZ_RIF_ASSET: in pratica, modificando il padre di un asset, deve essere verificato che il suo codice non sia presente nell’attributo STZ_RIF_ASSET di tutti gli asset figli del medesimo padre precedente allo spostamento.

### Gestione dello spostamento di asset di stazione tra DT differenti

Nell’ambito delle operazioni che gli utenti delle UI devono poter eseguire in autonomia c’è lo spostamento degli asset (intendendo per asset solo quelli associati a una classificazione contenente … \ DISPOSITIVI E COMPONENTI \...) tra stazioni appartenenti a DT differenti. Per eseguire questo spostamento (ad esempio un trasformatore a livello di “macchina”), l'operatività prevede la gestione dello stato (per gli stati, fare riferimento al § 3.3.2) e attraverserà i seguenti step:

Un utente della UI di appartenenza dell'asset lo mette nello stato di NIED (non installato, efficiente, disponibile);

Un utente della UI di destinazione esegue lo spostamento, modificando il padre in base alle regole della tabella di verità (§ 3.2.3);

Lo stato NIED è l'unico che permette agli utenti di UI differenti (gli utenti di Centro 1 e Centro 2 possono sempre) la movimentazione di asset che non appartengono alla loro DT.

Nel momento in cui viene eseguito lo spostamento, deve essere possibile la gestione di una data, indicata dall’utente, alla quale far riferire lo spostamento, differente dalla data di sistema in cui lo si esegue.

Nelle immagini seguenti viene data evidenza di come le informazioni saranno presentate.

Figura 11 - Maschera di spostamento asset

Nel momento in cui l’asset 3926 – SEZIONATORE AIS sarà stato messo nello stato di NIED diventerà possibile, per gli utenti di UI appartenenti a DT differenti rispetto a quella di attuale appartenenza, inserire nel campo Conto A padre la nuova destinazione.

Come mostrato nella figura seguente, aprendo il dettaglio della riga di spostamento, sarà possibile indicare puntualmente la data di effettiva modifica.

Figura 12 - Focus sulla gestione utente della data effettiva di spostamento

### Gestione dello spostamento di asset aggregati

Lo spostamento degli asset aggregati (per la cui definizione § 2.3.1.2) deve essere possibile solo a livello di macchina (ossia asset nella cui classificazione, oltre a … \ DISPOSITIVI E COMPONENTI \... sia presente anche … \ MACCHINA \...). In pratica la funzione di spostamento deve essere inibita per tutti gli asset che hanno il campo X3SAPID uguale ad un altro X3SAPID di un altro asset di tipo macchina, identificato con la regola precedente.

Attivando la funzione di spostamento sulla macchina (modificando il padre e rispettando le regole della tabella di verità), verranno automaticamente spostati gli altri asset che condividono il medesimo X3SAPID.

## Interrogazione

### Gestione e visualizzazione dei dati storici

In IBM MAS Manage l’inserimento e la modifica di alcuni dati deve automaticamente originare una stratificazione storica di informazioni, interrogabile in modo regolato. L’interrogabilità dei dati storici deve permettere una visualizzazione organizzata delle informazioni, garantendo all’utente che le visualizza la comprensione dell’evoluzione del dato nel tempo.

Le categorie di informazioni che devono essere sottoposte a storicizzazione sono le seguenti:

Classificazioni, attributi associati e valori degli stessi, tutto in relazione a una data di riferimento;

Modifiche di stato degli asset;

Modifiche relative al rapporto di gerarchia verso il padre. Nelle stazioni è utilizzato per tracciare gli spostamenti all’interno della medesima stazione e tra stazioni differenti;

Modifiche relative alle relazioni;

Modifiche relative all’ID descrittivo SAP (vedi § 3.7 e § 3.7.2), dove questo è gestito;

Modifiche riguardanti l’associazione delle fasi;

Di seguito sono riportati dettagli e specificazioni delle quali si dovrà tenere conto in fase di low level design e di implementazione:

Le modifiche degli attributi devono poter essere eseguite indicando anche la data dalla quale il dato dell'attributo è da considerarsi presente, con valenza sullo storico delle modifiche dei valori degli attributi;

Possibilità di creare asset con la data di creazione antecedente rispetto a quella di sistema;

Possibilità di indicare una data di spostamento antecedente rispetto alla data di sistema

Per avere un’idea della possibilità di indagine nella variazione degli attributi si presentano le due seguenti immagini, nelle quali la data inserita come criterio di ricerca (campo Al) è prima il 15/06/23 e poi il 16/06/23,

Figura 13 - Ricerca storico delle variazioni (1)

Figura 14 - Ricerca storico delle variazioni (2)

Nell’immagine seguente si vede la storia degli spostamenti eseguiti sull’asset 3926 – SEZIONATORE AIS.

Figura 15 - Storico spostamenti di un asset

## Dati non modificabili da IBM MAS Manage

In questo capitolo vengono censiti tutti i dati, siano campi fisici di database, strutture complesse o attributi di classificazione, che non dovranno poter’ essere creati o modificati dall’applicativo IBM MAS Manage.

Gli asset di primo livello rappresentativi delle stazioni non possono essere creati in IBM MAS Manage dagli utenti, ma verranno resi disponibili per mezzo del connettore con SAP. Lo stato iniziale con cui sono creati è il DATT oppure l’INAT (per gli stati, vedi § 3.3.2); da questo momento in poi lo stato verrà aggiornato da IBM MAS Manage (fatta eccezione per lo stato di INAT).

I seguenti campi non devono poter essere né inseriti né modificati dagli utenti del sistema:

I dati contabili riferiti agli asset (X3KOSTL);

Il campo X3SWERK utilizzato per gestire l’informazione della DT;

Il campo X3SAPID potrà essere aggiornato solo nell’ambito delle attività di ricodifica, descritte in § 3.7.2;

Analogamente, il contenuto di alcuni domini, popolati e aggiornati tramite il connettore, non dovrà essere modificabile in IBM MAS Manage; tra questi al momento sono stati individuati:

| Nome dominio | Descrizione | Nota |
| --- | --- | --- |
| X3SEDIINDISP | Codici indisponibilità | Collegato all’attributo STZ_ELEMENTO_INDISP |
| X3GEWRK | Centro di lavoro responsabile | Collegato a ASSET.X3GEWRK Campo SAP: GEWRK |
| X3SUBMT | Tipo costruzione | Campo SAP: SUBMT N.B. Sull’utilizzo del dominio vi è un punto aperto in lavorazione, in cui si attende riscontro dal BNS per conferma sulla gestione o meno dell’attributo in EAM. |

Nell’elenco seguente invece riportati i domini, non aggiornati dal connettore, il cui contenuto deve essere manualmente aggiornato in IBM MAS Manage, mantenendolo identico a quanto presente in SAP:

| Nome dominio | Descrizione | Nota |
| --- | --- | --- |
| X3BUKRS | Società | Collegato a ASSET.X3BURKS |
| X3ZZSOCP | Società proprietaria | Collegato a ASSET.X3ZZSOCP |

## Casi d’uso relativi a requisiti puntuali

Con il termine requisiti puntuali si intendono l'insieme di tutte le richieste / specificità viste e analizzate con lo scopo di assolvere ad una necessità specifica; chiaramente la risposta nel suo complesso ha indirizzato un disegno organico della soluzione. In questo capitolo sono riportati, partendo dal caso d'uso specifico, tutti i dettagli di design di alto livello necessari e funzionali alla fase di implementazione.

### Gestione di regole di editabilità degli attributi, indipendenti dalla classificazione

Per alcuni attributi, indipendentemente dalla classificazione nella quale sono utilizzati, deve essere possibile indicare il gruppo di persone che potrà gestirne (inserire, cancellare o modificare) la valorizzazione (vedi anche § 3.1.3).

Questo caso d’uso deriva dalla necessità di rendere disponibili a tutti le informazioni tecniche relative agli asset, ma per garantirne veridicità e coerenza, solo determinati gruppi, rappresentativi di specifiche competenze, avranno la possibilità di gestirli.

L’operatività a sistema prevede che sugli attributi dove questo sia necessario, dovrà essere popolato il campo gruppo responsabile; se il campo sarà vuoto, il dato potrà essere modificato da chiunque (fatte salve le regole generali di editabilità, costruite sulla base della UI di appartenenza, vedi § 3.1.4).

### Visualizzazione aggregata sul 1° livello della numerosità di asset di 2° e 3° livello

La necessità espressa è quella di mostrare sulla stazione un set di informazioni ottenute contando quanti asset, di specifiche tipologie, di secondo o terzo livello, esistono in una stazione. Di seguito il dettaglio di fonti e regole:

Le seguenti contando gli asset di 2° livello SEZIONE, raggruppati in base al valore dell’attributo STZ_TENSIONE_ESERCIZIO:

STZ_NUM_SEZIONI_380: NUMERO DI SEZIONI 380 kV

STZ_NUM_SEZIONI_220: NUMERO DI SEZIONI 220 kV

STZ_NUM_SEZIONI_110_150: NUMERO DI SEZIONI 110-150 kV

STZ_NUM_SEZIONI_40_90: NUMERO DI SEZIONI 40-90 kV

STZ_NUM_SEZIONI_MT: NUMERO DI SEZIONI MT

Le seguenti contando gli asset di 3° livello TRASFORMATORE, raggruppati in base ai valori degli attributi STZ_TENSIONE_NOMINALE_AVV_1 e STZ_TENSIONE_NOMINALE_AVV_2:

STZ_NUM_TRASF_P380_S220: NUMERO DI TRASFORMAZIONI 380 / 220 kV;

STZ_NUM_TRASF_P380_S110_150: NUMERO DI TRASFORMAZIONI 380 / 110-150 kV;

STZ_NUM_TRASF_P380_S40_90: NUMERO DI TRASFORMAZIONI 380 / 40-90 kV;

STZ_NUM_TRASF_P220_S110_150: NUMERO DI TRASFORMAZIONI 220 / 110-150 kV;

STZ_NUM_TRASF_P220_S40_90: NUMERO DI TRASFORMAZIONI 220 / 40-90 kV;

STZ_NUM_TRASF_P220_SMT: NUMERO DI TRASFORMAZIONI 220 kV / MT;

STZ_NUM_TRASF_P110_150_SMT: NUMERO DI TRASFORMAZIONI 110-150 kV / MT;

STZ_NUM_TRASF_P40_90_SMT: NUMERO DI TRASFORMAZIONI 40-90 kV / MT;

STZ_NUM_TRASF_ALTRO: NUMERO DI TRASFORMAZIONI ALTRI RAPPORTI;

Per i 5 valori del gruppo che usa l’attributo STZ_TENSIONE_ESERCIZIO il calcolo sarà:

num1 <= STZ_TENSIONE_ESERCIZIO <= num2;

Il business deve indicare i valori num1 e num2 per le 5 righe precedenti.

Per i 9 valori del gruppo che usa gli attributi STZ_TENSIONE_NOMINALE_AVV_1 e …2 il calcolo sarà:

num1 <= STZ_TENSIONE_NOMINALE_AVV_1 <= num2;

num3 <= STZ_TENSIONE_NOMINALE_AVV_2 <= num4;

Il business deve indicare i valori di num1 … num4 per le 9 righe precedenti.

Tutte le formule utilizzate per i calcoli o le visualizzazioni aggregate devono considerare sempre lo stato dell’asset: quelli in stati alias di decommissioned (CEAL, DEMO, DIST), non devono più essere considerati per i conteggi.

Gli attributi con regola, definiti sulle linee e sulle stazioni (primi livelli) non saranno editabili manualmente, ma verranno popolati solo dalla procedura di calcolo. Il valore mostrato nell’attributo sul primo livello sarà anche scritto nel database.

## Gestione della codifica descrittiva ID SAP

Nell’ambito di questo progetto, con il termine codifica descrittiva ID SAP si identifica una codifica univoca, i cui componenti sono validati da un algoritmo basato su regular expression (valido solo per i primi tre livelli di IBM MAS Manage). Nel database dell’EAM l’informazione è gestita nel campo X3SAPID oppure SAPID.

Questo codice è alla base dell’allineamento delle sedi tecniche e gli equipment SAP con gli asset di IBM Manage; per questo tutti gli asset del Manage che devono avere un corrispettivo in SAP lo devono gestire.

### ID SAP per le stazioni

Per le stazioni, a differenza delle linee, la struttura dalla parte di codice d’interesse anche per IBM MAS Manage la gestione dell’ID SAP è su tutti e quattro i livelli; nel quarto livello del Manage l’ID SAP è però popolato con un progressivo numerico generato da una sequenza.

Esempi di gestione dell’ID SAP:

Primo livello: arriva da SAP

Espressione regolare: ^3[0-6][0-9]{3}[A-Z] Es: 36123A

Secondo livello: generato in MX per mezzo della tabella di gestione usata dall’integrazione (X3MAXIMOSAPMAP), che mette in relazione le classificazioni (HIERARCHYPATH) con le regular expression (STEP1REGEX).

Il contenuto di X3SAPID del secondo livello è concatenato con il precedente.

Espressioni regolari 1:1 con le classificazioni, tranne

-- STAZIONE \ SEZIONE

che richiederà di scegliere il secondo carattere della 2° sezione, dal valore 2 al valore 6:

Espressione regolare: -5[2-6]$ Es: -52

Terzo livello: generato in MX analogamente a quanto fatto per il secondo livello.

Il contenuto di X3SAPID del terzo livello è concatenato con i precedenti.

Espressioni regolari 1:1 con le classificazioni, tranne

-- STAZIONE \ SEZIONE \ STALLO

che richiederà di scegliere il 2° carattere della terza sezione da 0-4 e 6, 8

1[0-4\|6\|8][0-9\|A-Z\|-]{2}$

-- STAZIONE \ TRASFORMAZIONI \ TRASFORMATORE

che richiederà di scegliere il 2° carattere della terza sezione da 0-5

1[0-5][0-9\|A-Z\|-]{2}$

Quarto livello: generato in MX da apposito contatore, 10 caratteri numerici, nnnnnnnnnn

Il contenuto di X3SAPID del quarto livello NON è concatenato ai livelli precedenti.

Particolarità per la gestione del SAPID: dal punto di vista dell’utente, quanto riportato sopra comporta la necessità, in fase di creazione, solo per il secondo livello SEZIONE e per i due terzi livelli STALLO e TRASFORMATORE, di arricchire il dato, chiedendo all’utente di specificare come segue la rispettiva informazione di 2° o 3° livello:

SEZIONE (2°L)

52 – 380Kv;

53 – 220Kv;

54 – 132-150Kv;

55 – 60-70Kv;

56 – MT;

STALLO (3°L)

10 – Linea;

11 – Macchina;

12 – Parallelo;

13 – Rifasamento;

14 – Conversione;

16 – Arrivo gruppo;

18 – Congiuntore;

TRASFORMATORE (3°L)

10 – CC;

11 – 380Kv;

12 – 220Kv;

13 – 132-150Kv;

14 – 60-70Kv;

15 – MT;

Nell’operatività a sistema, dopo la scelta della classificazione, se si ricade in uno dei precedenti tre casi, prima di dare l’OK nella maschera di dialogo di creazione, sarà necessario fornire l’ulteriore indicazione.

Nel caso degli asset di terzo livello, indipendentemente dalla tipologia, sempre prima del salvataggio, verrà data la possibilità di inserire gli ultimi due componenti alfanumerici o di lasciare il doppio trattino, presentato come default [0-9\|A-Z\|-]{2}$. Nel caso in cui esista già un elemento con ID SAP identico, di default verrà presentata la prima posizione libera, partendo da 01, 02, 03, ecc.

### Gestione della ricodifica del codice descrittivo ID SAP

La ricodifica del codice descrittivo ID SAP è una funzione che permette di aggiornare in modo massivo la codifica SAP ed è utilizzata per mantenere coerenti le codifiche tra il primo livello e quelli sottostanti.

Nell’ambito delle stazioni si può rendere necessaria un’operazione di ricodifica a causa della modifica radicale di una stazione, mantenendo parte degli elementi già esistenti; questo può avvenire perché viene estesa la capacità di una stazione oppure viene modificata la sua funzione.

In tutti questi casi la prima azione da fare è quella di creare in SAP la nuova stazione (il solo primo livello), per renderla disponibile in IBM MAS Manage; a questo punto gli asset dal secondo al quarto livello, sempre nel rispetto della coerenza della struttura (gerarchia delle classificazioni per livelli 2° e 3°, tabella di verità per il quarto livello), potranno diventare componenti della nuova stazione. In automatico, come conseguenza di questo spostamento, la prima porzione dell’ ID SAP verrà aggiornata con l’ID SAP della nuova stazione (spostamento di un secondo livello + figli). L’altro caso si può avere quando, sotto un secondo livello della nuova stazione viene spostato un terzo livello + figli: in questo caso l’ID SAP del terzo livello deve essere aggiornato nella porzione di codice del primo e del secondo componente.

Oltre a quanto sopra, solo per gli asset di terzo livello, dovrà essere possibile, sempre rispettando le regular expression, aggiornare la porzione di ID SAP di pertinenza, ossia la terza porzione. In questo caso la modifica non dovrà essere propagata, in quanto nell’ID SAP del quarto livello non si gestisce la concatenazione (porzioni) con i livelli superiori.

Per riassumere, i casi di aggiornamento saranno i seguenti:

Spostamento di un asset di secondo livello: si aggiorna il primo componente dell’ID SAP del secondo livello e di tutti i terzi livelli figli;

Spostamento di un asset di terzo livello: si aggiorna il primo e il secondo componente dell’ID SAP dell’asset spostato;

Ricodifica puntuale di un asset di terzo livello: premesso che gli asset non possono essere riclassificati e che quindi, ad esempio, uno stallo rimane uno stallo e un trasformatore un trasformatore, potrà essere modificata la porzione finale, alfanumerica, di due caratteri.

L’operatività a sistema per i primi due casi è automatica e consequenziale allo spostamento, mentre per il terzo caso (ricodifica), si attua con una funzione di menu contestuale alla riga dell’asset nella tabella dei terzi livelli sulla quale si intende agire.

## Gestione dell’informazioni di funzione elemento e fase

Nell’attuale installazione di SAP il campo POSNR non viene utilizzato sulle sedi tecniche delle stazioni; in questo caso l'ordinamento di visualizzazione si basa sul codice descrittivo ID SAP associato a ciascuna sede tecnica di stazione.

Discorso differente per gli equipment di SAP, definiti nelle stazioni, dove il POSNR è gestito, ma con una funzione differente rispetto a quella che assume sulle linee (ossia la gestione della progressiva sequenza di campate, sostegni e tratte cavo); infatti nelle stazioni è utilizzato per specificare la funzione dell’asset all’interno della stazione. Un elemento, attualmente critico in SAP, relativo al POSNR è rappresentato dalla gestione di un unico dominio con oltre 200 valori, tra cui gli utenti devono scegliere; la richiesta è quella di mostrare solo i valori pertinenti con il tipo di asset per il quale si sta inserendo il valore.

Nell’immagine seguente, presa da SAP, viene mostrato l’equipment 1000086269, associato alla sede tecnica degli Apparecchi/collegamenti di potenza di U.F. codificata 31401B-52-1011-A.

Figura 16 - Asset di stazione

L’equipment ha come posizione (POSNR) il valore A24. La selezione di A24 è stata fatta scegliendo tra i 226 valori possibili del seguente elenco:

Figura 17 - Dominio POSNR per asset di stazione

Per il caso specifico, gli unici valori che potevano avere senso per l’equipment erano solo quelli evidenziati in nero nell’immagine precedente.

Avere inserito A24 nel campo posizione indica che l’equipment è un TA di fase R, ovvero TA-R.

Per rendere possibile la selezione dei soli valori evidenziati verrà gestito un dominio condizionato alla classificazione associata all’asset che si sta gestendo.

Quindi il POSNR usato per le stazioni viene suddiviso nei due attributi:

X3FUNZIONEELEMENTO: per l’esempio in oggetto, popolato con TA1;

X3RIFERIMENTOFASE: per l’esempio in oggetto, popolato con R;

Il connettore dovrà verificare il valore di questi due attributi con una tabella di trascodifica, che permetterà di capire quale valore unico (tra i 226 gestiti) dovrà essere mandato a SAP; importante che sia identico, perché è con questo valore che SISNET e il DSS capiscono quale tipo di asset stanno considerando.

Riprendendo sempre l’esempio precedente, il connettore dovrà inviare, per il campo POSNR il valore A24.

Il dominio associato all’attributo deve prevedere il valore che esprime la singola fase (R, S, T) .

In IBM Maximo Manage il popolamento e la visualizzazione di questi campi dell’asset verrà eseguita all’interno della box di dialogo deve viene mostrata anche la tabella degli attributi di classificazione, sopra la tabella stessa.

Gli asset che non gestiscono la fase o entrambe i campi, avranno l’elenco valori vuoto; quindi gli utenti non potranno forzare l’inserimento di valori in X3FUNZIONEELEMENTO e/o X3RIFERIMENTOFASE.

## Gestione del legame di relazione tra stazioni e linee

Il caso d’uso espresso è quello di poter gestire un legame di relazione tra una stazione e le linee che da questa escono.

Per permettere questo legame viene gestita la possibilità di creare una relazione (relazione STAZLIN) tra un elemento della stazione classificato come STALLO e il primo o l’ultimo sostegno della linea, sia di dorsale che di derivazione.

La visualizzazione della fase effettiva di un conduttore, rispetto alla stazione di partenza, viene gestita solo sulla linea.

Nell’immagine seguente, tra la stazione ROMA NORD e la linea S.LUCIA – ROMA NORD il legame di relazione STAZLIN deve essere costruito tra l’elemento 31401B-52-1011 della stazione e l’elemento 21304-999-RNO--S della linea.

Figura 18 - Relazione stazione - linea

Nell’operatività a sistema, l’utente esegue i seguenti passi:

Aprire l’applicazione di gestione delle stazioni e nella tabella dei terzi livelli selezionare l’elemento di stazione da mettere in relazione (la funzione di menu contestuale sarà disponibile solo per gli asset classificati come …\ STALLO \...);

Dal menu contestuale scegliere la funzione Linea in uscita;

Si apre una maschera di dialogo che mostra tutti i sostegni di inizio e fine (sia di dorsale che di derivazioni), associati alla medesima UI della stazione, filtrabili per linea;

L’utente seleziona il sostegno (solo uno) e poi clicca su OK;

Il sistema salva una nuova riga nella tabella delle relazioni, che associa l’asset stallo da cui la funzione è stata richiamata con l’asset sostegno selezionato, usando la relazione STAZLIN

Con le operazioni precedenti IBM MAS Manage ha costituito un legame interrogabile sia dalla stazione che dalla linea.

Richiamando a video il primo livello della stazione, nella scheda Relazioni con le linee, in un’apposita tabella, verranno visualizzati tutti i primi livelli delle linee che partono, nell’esempio, dalla stazione Roma Nord.

Richiamando a video il primo livello della linea, nella scheda Relazioni con le stazioni, in un’apposita tabella, verranno visualizzati tutti i primi livelli delle stazioni collegate, nell’esempio, dalla linea S.Lucia- Roma Nord.

## Gestione del codice di indisponibilità

Il codice di indisponibilità è utilizzato per avere una codifica condivisa tra chi gestione l’anagrafica e chi gestisce il dispacciamento, allo scopo di comunicare quali asset dovranno essere tolti dalla produzione (ossia non più percorsi dalla corrente elettrica).

Il codice indisponibilità, in SAP, è gestito per mezzo di una struttura di sedi tecniche specifiche. La funzione aziendale che si occupa di crearle è il dispacciamento, la funzione aziendale che si occupa di associarle alle sedi tecniche rappresentative di linee e stazione è la funzione aziendale di asset management.

In IBM MAS Manage, sulle stazioni, l’associazione è gestita a livello di asset di 1° e 3° livello, quindi direttamente sulla stazione (1° livello) e sui seguenti terzi livelli:

Stallo

Sbarra

Trasformatore

Booster

Trasformatore AT/BT

Phase shifting trasformer

Batteria condensatori

Reattore

Su tutti l’attributo di gestione del dato è STZ_ELEMENTO_INDISP. L’utente per compilare l’attributo potrà selezionare uno qualsiasi dei codici presenti nella tabella delle sedi di indisponibilità, ossia tutti quelli delle linee (iniziale del valore STRNO L) che quelli delle stazioni (iniziale del valore STRNO Z).

La selezione possibile verrà filtrata sui soli valori di STRNO della tabella che iniziano con “Z”.

La coerenza della codifica delle sedi tecniche di indisponibilità è del dispacciamento. In IBM MAS Manage l’elenco dei codici di indisponibilità verrà registrato per mezzo della struttura dati X3SEDIINDISP. L’eventuale ricodifica del valore dell’ID SAP degli asset non avrà impatti sulla codifica delle sedi di indisponibilità a questi associate.

# Elementi Anagrafici

A seguito delle analisi condotte sui requisiti affrontati in ambito Wave 2, sono state implementate nuove logiche per la gestione degli elementi anagrafici di Terna presenti in EAM.

Nel dettaglio, in questo capitolo, verranno trattate le seguenti tematiche:

Sedi Tecniche integrative;

MCM e Blindati;

Tipologia "Unità funzionale".

## Sedi tecniche integrative

In questo capitolo viene descritta la gestione delle Sedi tecniche integrative utilizzate per gestire particolari casistiche di processo.

Le sedi tecniche saranno create in anagrafica EAM sulla base del seguente file di input:

In totale, saranno creati 188 Asset fittizi associati alle classificazioni “Ufficio” (associati alle relative UI) e “impianto fotovoltaico”, non appartenenti né a gerarchie di linee né di stazioni Le sedi tecniche integrative consentiranno la gestione, tramite OdL, di particolari casistiche di processo (ad esempio la formazione), con la possibilità di consuntivare ore in SAP. Una volta inizializzati a sistema, tali sedi tecniche non saranno modificabili/creabili direttamente dagli utenti. Le sedi tecniche integrative saranno caratterizzate dalle seguenti informazioni:

Classificazione: Indica la natura dell’asset generico. Le sedi tecniche integrative saranno create come asset di 1° Livello con classificazione “Ufficio” o “Impianto fotovoltaico” come indicato nel file di input

ASSETNUM: identificativo generato in automatico dal sistema;

Descrizione: descrizione asset;

DT e UI: distretto territoriale e unità impianto;

Stato: default “ATTIVO”;

Centro di lavoro responsabile.

Il DSS non generà alcuna attività per questa tipologia di asset; tuttavia, sarà possibile generare OdL manuali associandoli direttamente all’asset generico censito in anagrafica utilizzando i PdL associabili alla relativa classificazione.

Per gli OdL su STI non verrà inviata a SAP l’informazione della sede tecnica nel flusso delle consuntivazioni.

Come anticipato, non è prevista una gestione in anagrafica degli asset di tipo “Ufficio” e “Impianto fotovoltaico”, pertanto l’inserimento o la modifica delle sedi tecniche integrative a valle dell’inizializzazione del sistema dovrà essere effettuato tramite CR.

## MCM e Blindati

Questo capitolo illustra in dettaglio le caratteristiche e la gestione degli asset “Comparti MCM”, “Comparti AT blindato” e “Armadio GIS”. Nello specifico, questi elementi saranno gestiti in EAM come asset di quarto livello associati agli asset di terzo livello Stallo e Sbarra. Gli elementi di quarto livello Comparto MCM, Comparto AT Blindato e Armadio GIS sottesi alle Sbarre e Stalli avranno poi i seguenti percorsi gerarchici all’interno dell’applicazione “Classificazione Terna”.

Di seguito vengono riportati il percorso di classificazione e le regole di creazione di MCM e Blindati:

Colonna "Classificazione Padre": indica il terzo livello della classificazione sotto cui possono essere creati gli MCM e i Blindati.

Colonna "Classificazione Figlio": specifica il percorso di classificazione degli MCM e dei Blindati.

Colonne "Numerosità" e "Cardinalità": definiscono il numero massimo di MCM e Blindati che possono essere creati sotto ciascun livello padre.

| Classificazione padre | Descrizione | Classificazione figlio | Descrizione | Numerosità | Cardinalità |
| --- | --- | --- | --- | --- | --- |
| STAZIONE \ SEZIONE \ STALLO | Stallo | STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ COMPARTO AT BLINDATO | Comparto AT Blindato | x = 0 ÷ n | 0 ÷ n |
| STAZIONE \ SEZIONE \ SBARRA | Sbarra | STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ COMPARTO AT BLINDATO | Comparto AT Blindato | x = 0 ÷ n | 0 ÷ n |
| STAZIONE \ SEZIONE \ STALLO | Stallo | STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ COMPARTO MCM | Comparto MCM | x = 0 ÷ n | 0 ÷ n |
| STAZIONE \ SEZIONE \ STALLO | Stallo | STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ ARMADIO GIS | Armadio GIS | x = 0 ÷ 1 | 0, 1 |
| STAZIONE \ SEZIONE \ SBARRA | Sbarra | STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ ARMADIO GIS | Armadio GIS | x = 0 ÷ n | 0, 1 |
| STAZIONE \ CONVERSIONI \ MONTANTE AT LATO CONVERSIONE | Montante At Lato Conversione | STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ COMPARTO AT BLINDATO | Comparto AT Blindato | x = 0 ÷ n | 0 ÷ n |

Per il calcolo dell’attributo “Numero MCM” (attributo 1° livello di Stazione) che serve ad indicare il numero di MCM e blindati presenti nella stazione verrà utilizzato il campo di testata “Funzione elemento” (ASSET.X3FUNZIONEELEMENTO) presente su tutti gli equipment.

Il popolamento di “Funzione elemento” è vincolato a un elenco di valori determinato in base alla classificazione e modificabile dagli utenti all’interno dell’applicazione “Funzione elemento/fase”. Di seguito la lista valori per popolare il campo nel caso di MCM e Blindati:

• B01, B02: Nel caso di Blindati (Comparto AT blindato, Armadio GIS)

• M01, M02: Nel caso di MCM (Comparto MCM)

Tramite il progressivo 01,02 e il primo carattere del valore è possibile indicare il numero di MCM o Blindati per Stallo. Il popolamento dell’attributo sarà manuale e non saranno presenti automatismi di valorizzazione, mentre l’attributo “Numero MCM” del primo livello stazione verrà valorizzato con la seguente logica:

Conteggio per stallo di MCM e Blindati (count della distinct di “funzione elemento” per stallo), e successivamente saranno sommati i conteggi di tutti gli stalli a livello di stazione (prendendo solo i valori di “Funzione elemento” che iniziano per B (blindati) e M per (MCM)).

Figura - Gerarchia degli elementi di terzo livello Stallo e Sbarra nel modello To-be

Nel contesto dell’asset di terzo livello Stallo/Sbarra, è prevista in EAM la presenza di un attributo identificativo del tipo di isolamento. Questo attributo, denominato “Tipo isolamento” viene popolato automaticamente dal sistema (scegliendolo da una lista) in base alla tipologia di isolamento degli asset sottesi. Il campo “Tipo isolamento” viene ricalcolato ad ogni inserimento / modifica di asset sottostanti lo stallo che presentano la caratteristica dell’isolamento. I valori assumibili dal “Tipo Isolamento” sono:

Aria: le apparecchiature contenute nello stallo sono isolate in aria (asset AIS);

Gas: le apparecchiature contenute nello stallo sono isolate in gas (asset GIS);

Misto: lo stallo contiene sia apparecchiature isolate in aria che con gas (asset AIS/GIS).

Inoltre, viene definito un nuovo attributo specifico per gli asset di quarto livello fisicamente contenuti all'interno del comparto chiamato "Comparto di Riferimento". Questo attributo viene valorizzato con il comparto in cui si trova fisicamente l'asset e sarà presente all’interno delle seguenti classificazioni:

INTERRUTTORE AT BLINDATO

PASSANTE AT BLINDATO SF6/ARIA

PASSANTE AT BLINDATO SF6/CAVO

TA AT BLINDATO

TV AT BLINDATO

SCARICATORE AT BLINDATO

SEZIONATORE AT AD ANGOLO SEMPLICE BLIND.

SEZIONATORE AT DI LINEA BLINDATO

SEZIONATORE AT DI TERRA BLINDATO

SEZIONATORE AT DI TERRA RAPIDO BLINDATO

## Tipologia unità funzionale

Sugli asset di terzo livello stallo si aggiunge una specifica tecnica denominata STZ_TIPOLOGIA_UF, serve da input alla policy per la generazione automatica di OdL ed utile per la scelta del metodo da impiegare. La specifica è popolata in automatico alla creazione di un nuovo stallo sulla base del mapping tra il codice SAP e il tipo definito nella tabella sotto:

| Elemento SAP | Codifica SAP | STZ_TIPOLOGIA_UF |
| --- | --- | --- |
| STALLO (LINEA) | nnnnnx-5n-10xx | Linea |
| STALLO (MACCHINA) | nnnnnx-5n-11xx | Macchina |
| STALLO (PARALLELO) | nnnnnx-5n-12xx | Parallelo |
| STALLO (RIFASAMENTO) | nnnnnx-5n-13xx | Rifasamento |
| STALLO (FILTRO DI ARMONICA) | nnnnnx-5n-14xx | Filtro di armonica |
| STALLO (ARRIVO GRUPPO) | nnnnnx-5n-16xx | Arrivo gruppo |
| STALLO (CONGIUNTORE) | nnnnnx-5n-18xx | Congiuntore |

# Gestione nuove società RETE 2 e Terna Gora

Nel contesto evolutivo dei sistemi EAM e SISNET è prevista l’introduzione di due nuove società:

RETE 2, che entrerà a far parte del Gruppo Terna nel corso del 2H 2025

Terna CRNA Gora, già appartenente al Gruppo Terna.

L’obiettivo è garantire che entrambe le società dispongano di un impianto anagrafico coerente con gli standard Terna e che i relativi utenti possano operare in modo corretto e sicuro all’interno dei sistemi.

## Società RETE 2

Le sedi tecniche della società RETE 2 saranno create ex novo, seguendo le regole e gli strumenti attualmente disponibili per la creazione di Stazioni.

Il primo livello verrà generato in SAP con stato ESER, contenente tutti i valori relativi alla nuova società, e successivamente trasmesso a EAM in stato DATT.

Da questo punto, l’utente potrà completare la struttura gerarchica tramite le funzionalità di creazione singola di asset e procedere con la valorizzazione delle specifiche tecniche.

I valori dei campi di testata specifici per la società RETE 2, mappati su EAM e SAP, sono i seguenti:

Società proprietaria: TR64

Società: TR64

Divisione di Ubicazione: 6400

Unità Impianti: RT2

Centro di Lavoro Responsabile: RT2TT_O

Le sedi tecniche associate alla società TR64 potranno essere modificate e gestite dagli utenti abilitati come “gestori anagrafica DT”, appartenenti alla Divisione di Ubicazione 6400 e da tutti gli utenti del Dipartimento Centro.

Inoltre, gli utenti di RETE 2, secondo le regole già implementate, disporranno anche dell’accesso in sola lettura a tutta l’anagrafica EAM.

## Società TernaGora

Per la società TernaGora risultano già presenti alcuni asset su SAP. Tuttavia, tali asset non verranno caricati tramite ETL, ma saranno ricreati da zero su EAM, in coerenza con le regole applicative definite.

In particolare, in SAP sono attualmente censite:1 Sede Tecnica Stazione

Le sedi tecniche della società TernaGora saranno quindi create ex novo, seguendo le regole e gli strumenti attualmente disponibili per la creazione di Stazioni.

Il primo livello è già disponibile in SAP con stato ATTV e contiene tutti i valori relativi alla nuova società. Per consentire il corretto invio verso EAM, sarà, però, necessario impostare la sede tecnica in ESER, in modo da sollecitare il connettore SAP-EAM e trasmettere la sede tecnica verso EAM in stato DATT;

L ’attivazione (ATTV) avverrà direttamente in EAM e l’utente potrà completare la struttura gerarchica utilizzando le funzionalità di creazione singola degli asset e potrà procedere con la valorizzazione delle specifiche tecniche.

I valori dei campi di testata specifici per la società TernaGora, mappati su EAM e SAP, sono i seguenti:

Società proprietaria: TR30

Società: TR30

Divisione di Ubicazione: 3000

Unità Impianti: GTG

Centro di Lavoro Responsabile: TCG_O

Le sedi tecniche associate alla società TR30 potranno essere modificate e gestite dagli utenti abilitati come “gestori anagrafica DT”, appartenenti alla Divisione di Ubicazione 3000 e da tutti gli utenti del Dipartimento Centro.

Inoltre, gli utenti di TernaGora, secondo le regole già implementate, avranno anche accesso in sola lettura a tutta l’anagrafica EAM.
