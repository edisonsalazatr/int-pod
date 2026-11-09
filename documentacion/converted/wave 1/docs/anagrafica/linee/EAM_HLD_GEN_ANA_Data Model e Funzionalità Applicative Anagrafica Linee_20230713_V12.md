# EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V12

|  | Disegno di alto livello (HLD) del Modello Dati dell’Anagrafica Linee e delle Funzionalità Applicative dell’Anagrafica Linee |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Relazione: | DISEGNO DI ALTO LIVELLO DEL MODELLO DATI DELL’ANAGRAFICA E DELLE FUNZIONALITÀ APPLICATIVE DELL’ANAGRAFICA LINEE |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 1.0 | Primo rilascio | 13/07/2023 |
| 1.2 | Revisione per recepimento commenti | 18/07/2023 |
| 1.5 | Revisione per recepimento commenti | 28/07/2023 |
| 1.8 | Revisione per recepimento commenti | 04/08/2023 |
| 1.9 | Revisione per recepimento commenti |  |
| 1.10 | Revisione per risoluzione di punti aperti | 25/08/2023 |
| V4 | Aggiornamenti per § 3.19 | 11/10/2023 |
| V7.7 | Aggiornamenti per OP prioritari | 18/04/2025 |
| V10.1 | Integrazione Split |  |
| V12 | Riassociazione Linee | 09/01/2026 |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | RTC | 13/07/2023 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

Documenti citati

| # | File | Descrizione | Data |
| --- | --- | --- | --- |
| 1 | TERNA_EAM_RRQ_REQUT_ANA_CasiDiUso_20230414_V5.1 | Documento relativo ai casi d’uso dell’Anagrafica |  |
| 2 | REV 20230710 of attributi-EAM-SAP.xlsx | File relativo ai campi di testata |  |
| 3 | EAM_LLD_GEN_ANA_Documento Anagrafica di LLD_20230703_V1.0.docx | Documento di LLD Anagrafica |  |
| 4 | Domini.xlsx | Elenco Domini |  |
| 5 | ApprofondimentoRiassociazione.pptx | È il documento per la comprensione business – RTI degli scenari da indirizzare. | 09/01/2026 |

Indice delle figure

Figura 1 - Linea in singola terna 10

Figura 2 - Linee in terna doppia 10

Figura 3 - Rappresentazione linea in singola terna 11

Figura 4 - Rappresentazione linea in doppia terna 12

Figura 5 - I quattro livelli per le linee 15

Figura 6 - Organigramma Terna 29

Figura 7 - Legame DT – UI 31

Figura 8 - Codici DT 32

Figura 9 - Codici UI 32

Figura 10 - Funzione di generazione di elementi di linea (2°- 3°- 4° livello) 35

Figura 11 - Interazione del dispacciamento, extra sistema, con la UI d’interesse 35

Figura 12 - Esempio di linea aerea risultante dalla precedente azione 36

Figura 13 - La linea (9101) che deve essere in doppia terna con la precedente (8836) di Figura 12 36

Figura 14 - Finestra di dialogo per scegliere gli elementi di 2° livello da rendere comuni 37

Figura 15 - Elementi rappresentanti la doppia terna, per le due linee 37

Figura 16 - Elementi di una linea in cavo 38

Figura 17 - Tronchi di ispezione associati alla linea 39

Figura 18 - Tronco di ispezione associato alla campata tramite il conduttore 40

Figura 19 - crea asset singolo in cavo Situazione prima della modifica 41

Figura 20 - crea asset singolo in cavo Situazione dopo la modifica 41

Figura 21 - Informazioni per creazione asset di linea di 2° livello 42

Figura 22 - Inserimento asset contestualizzato su una linea aerea 42

Figura 23 - Terna Singola 44

Figura 24 - Doppia Terna (1/3) 45

Figura 25 - Doppia Terna (2/3) 45

Figura 26 - Doppia Terna (3/3) 46

Figura 27 - Casi linee in cavo 46

Figura 28 - Tutti i casi misti non devono essere permessi 46

Figura 29- Dialog “Crea Split” 49

Figura 30 Posnr split Monofase 49

Figura 31 Posnr Split Bifase 50

Figura 32 Esempio Linea con Split Bifase 50

Figura 33 - Rappresentazione in Mandi di terna doppia “ammazzettata” 51

Figura 34 - incrocio di due linee su sostegno singolo 52

Figura 35 - Stati gestiti 54

Figura 36 - Linee in relazione 57

Figura 37 - Ricerca storico delle variazioni (1) 58

Figura 38 - Ricerca storico delle variazioni (2) 58

Figura 39 - Storico spostamenti di un asset 59

Figura 40 - Sostegni e distribuzione fasi 62

Figura 41 - Correlazione tra modelli 67

Figura 42 - Modello di sostegno 68

Figura 43 - Sostegno, aggiunte per doppia terna 69

Figura 44 - Elementi di campata per terna singola 69

Figura 45 - Modello di campata per la doppia terna 70

Figura 46 - Modello di tratta cavo 70

Figura 47 - Modello di estremo cavo terminale 70

Figura 48 - Modello di estremo cavo giunto 71

Figura 49 - Posizione fasi su sostegno di terna doppia 72

Figura 50 - Elementi di una linea aerea con i rispettivi numeri di sequenza 74

Figura 51 - Dorsale aerea con una derivazione aerea 74

Figura 52 - Derivazione di derivazione aerea 75

Figura 53 - Derivazione aerea con rientro sulla dorsale 75

Figura 54 - Rappresentazione della sequenza con derivazioni 76

Figura 55 - Esempio derivazione multipla stesso sostegno – POSNR 76

Figura 56 - Derivazione doppia dallo stesso sostegno - X3SEQUENCE 77

Figura 57 - Derivazione tripla dallo stesso sostegno – X3SEQUENCE 77

Figura 58 - Derivazione mista, prima in aereo e poi in cavo 78

Figura 59 - Derivazione diretta in cavo dalla dorsale 78

Figura 60 - La derivazione in cavo da dorsale in cavo non è supportata 78

Figura 61 - Derivazione in cavo da derivazione in aereo 79

Figura 62 – Modifica di tracciato con rimozione 80

Figura 63 – Dati rilevanti per la riassociazione 81

Figura 64 - Modifica del calcolo del POSNR 82

Figura 65 - Legenda forme & colori 83

Figura 66 - Riassociazione di una porzione di linea in una linea vuota 84

Figura 67 - Riassociazione di una porzione di linea a inizio altra linea 84

Figura 68 - Riassociazione di una porzione di linea a una linea in cavo 85

Figura 69 - La riassociazione di una linea con degli ammazzettamenti 86

Figura 70 - Gestione della doppia terna caso a-1 86

Figura 71 - Gestione della doppia terna caso a-2 87

Figura 72 - Gestione della doppia terna caso b 87

Figura 73 - Gestione della doppia terna caso c 88

Figura 74 - La riassociazione totale 88

Figura 75 - La riassociazione di una porzione di derivazione 89

Figura 76 - La riassociazione di una porzione di dorsale 89

Figura 77 - Scenari esclusi a 90

Figura 78 - Scenari esclusi b 91

Figura 63 - Gerarchia opere interferenti 92

Figura 64 - Tabella parametrica opere interferenti 92

Figura 65 - Tabella dell'asset di quarto livello opera interferente con evidenza all’icona di accesso all’elenco di attributi 94

Figura 66 - Attributi dell’asset di quarto livello “opera interferente” 94

Figura 67 - Funzionalità di creazione Opera Interferente 3° livello 95

Figura 68 - Funzionalità di creazione Opera Interferente 4° livello 95

Figura 69 - Dialog di creazione dell'opera interferente 96

Figura 70 - Tabella contenente l'asset di quarto livello opera interferente, all'interno dell'applicazione “Linee” 96

Figura 71 - Schermata OdL da mobile 97

Figura 72 - Schermata di compilazione della segnalazione 97

Figura 73 - Lotti tecnici linea 98

Figura 74 - Confluenza di due campate singole nel medesimo sostegno in doppia terna 101

Figura 75 - Due linee, entrambe con un sostegno che deve essere messo in doppia terna 102

Figura 76 - Sostegno linea rossa 102

Figura 77 - Sostegno linea verde 102

Figura 78 - Linee con sostegno duplicato 103

Figura 79 - Linee dopo l'accorpamento del sostegno duplicato 103

Figura 80 - Campata in doppia terna da dividere 104

Figura 81 - effetto della divisione: due campate distinte 105

# Introduzione

Il presente documento ha l’obiettivo di presentare la struttura del modello dati e le funzionalità applicative dell’anagrafica delle linee (derivate dal documento dei casi d’uso). Tutti i documenti citati all’interno del presente documento sono disponibili e consultabili all’interno della seguente cartella Teams Documenti correlati.

In particolare, la macro-fase di High-Level Design formalizza gli scenari operativi identificati dagli use-case secondo i meccanismi di modellazione offerti dal prodotto. A seguire, questo documento verrà ulteriormente dettagliato nella attività di Low-Level Design in cui verranno identificate le attività realizzate a basso livello che rappresenteranno l’input per la macro-fase di Implementazione.

Di seguito le immagini rappresentative del data model oggetto del presente documento:

Figura 1 - Linea in singola terna

Figura 2 - Linee in terna doppia

Figura 3 - Rappresentazione linea in singola terna

Figura 4 - Rappresentazione linea in doppia terna

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
| ITEMSTRUCT | Tabella della struttura di modelli, che una volta istanziati, daranno origine agli asset in gerarchia (ad esempio, applicando la struttura SOSTEGNO-S ad un asset questo assumerà la classificazione, con relativi attributi, del sostegno e creerà tutti gli altri asset, sempre con relative classificazioni e attributi, di 3° e 4° livello, figli e “nipoti” del sostegno. |  |
|  |  |  |

# Il modello dati delle linee

In questo capitolo viene spiegato il modello dati impiegato per la gestione delle linee elettriche. La struttura dati di riferimento per gestirle è quella degli Asset, coadiuvata dalle classificazioni, per poter classificare i componenti distribuiti sui quattro livelli e collegarli agli attributi di pertinenza.

Il modello dati, oltre a permettere la caratterizzazione dei singoli asset deve essere anche in grado di gestire i legami logici tra i differenti livelli.

## Elementi, livelli classificazioni e attributi della linea

In questo paragrafo si ripercorre la struttura degli asset di linea, come si qualificano e come si agganciano le rispettive caratteristiche tecniche.

Una linea elettrica è divisa in quattro livelli, come mostrato nell’immagine seguente:

Figura 5 - I quattro livelli per le linee

La linea elettrica, per ciascun livello, si compone dei seguenti elementi, censiti nel Manage come asset:

Livello uno:

Linea

Livello due:

Campata Aerea

Sostegno Aereo

Tratta Cavo

Estremo Cavo

Livello tre per

Campata Aerea

Conduttori

Funi di guardia

Sostegno Aereo

Attacchi conduttore

Telaio

Tratta Cavo

Cavi

Estremo Cavo

Giunti

Terminali

Livello quattro per:

Conduttori:

Antirotazionale conduttore

Antivibrante conduttore

Barretta preformata conduttore

Dissuasore avifauna conduttore

Fibra ottica conduttore

Conduttore

Giunto conduttore

Sfere di segnalazione conduttore

Distanziatori

Distanziatori interfase

Funi di guardia:

Antirotazionale fune di guardia

Antivibrante fune di guardia

Barretta preformata fune di guardia

Dissuasore avifauna fune di guardia

Fibra ottica fune di guardia

Fune di guardia

Giunto fune di guardia

Sfere di segnalazione fune di guardia

Attacchi conduttore:

Isolatori conduttore

Bobina di sbarramento OCV

Armamento conduttore

Telaio:

Isolatori fune di guardia

Armamento fune di guardia

Struttura

Segnalazione luminosa ostacolo volo

Giunzione fibra ottica

Dispositivo TLC

Sezionatore

Nido avifauna

Monitoraggio IOT (DIGIL)

Cavi:

Fibra ottica

Cavo di potenza

Giunto di riparazione

Segnalazione cavi

Giunti:

Camera ispezionabile

Cassetta di sezionamento giunti

Giunto

Monitoraggio IOT (DIGIC) Giunti

Terminali:

Cassetta di sezionamento

Local server

Monitoraggio IOT (DIGIC) Terminali

Scaricatore

Terminale

A ciascuno degli asset di ciascun livello sono pertanto associate delle classificazioni, impiegate proprio per classificare l’asset stesso, oggetto di associazione.

Per la mappatura completa delle classificazioni che verranno associate agli asset, riferirsi al documento 001-mxloader-linee-classificazioni.xlsm incluso nel seguito.

A ciascuna classificazione sono stati legati degli attributi, per mezzo dei quali sono gestite le informazioni specifiche di ogni asset al quale la classificazione è associata.

Per fare un esempio, sulla classificazione delle linee, alcuni degli attributi gestiti sono i seguenti:

LIN_ELEMENTO_INDISP ELEMENTO INDISPONIBILITÀ

LIN_LIVELLO_TENSIONE_ESERC LIVELLI TENSIONE DI ESERCIZIO

LIN_CTI CODICE CTI DI APPARTENENZA

LIN_LUNGHEZZA_T LUNGHEZZA TERNA

LIN_NUM_SOSTEGNI NUMERO SOSTEGNI

LIN_KM_EQUIVALENTI KM EQUIVALENTI

LIN_LUNGHEZZA_L LUNGHEZZA LINEA

LIN_NUM_SOSTEGNI_PP NUMERO SOSTEGNI SU PALIFICAZIONE PROPRIA

LIN_UIM1_ID U.I. 1: UNITÀ IMPIANTI

LIN_UIM1_LUNG_TERNA U.I. 1: LUNGHEZZA TERNA

LIN_UIM1_NUM_SOSTEGNI U.I. 1: NUMERO SOSTEGNI

LIN_UIM1_KM_EQUIVALENTI U.I. 1: KM EQUIVALENTI

LIN_UIM1_SOSTEGNO_ESTREMO_FIN U.I. 1: SOSTEGNO O ESTREMO FINE COMPETENZA

LIN_UIM1_SOSTEGNO_ESTREMO_INI U.I. 1: SOSTEGNO O ESTREMO INIZIO COMPETENZA

LIN_UIM2_ID U.I. 2: UNITÀ IMPIANTI

LIN_UIM2_LUNG_TERNA U.I. 2: LUNGHEZZA TERNA

LIN_UIM2_NUM_SOSTEGNI U.I. 2: NUMERO SOSTEGNI

LIN_UIM2_KM_EQUIVALENTI U.I. 2: KM EQUIVALENTI

LIN_UIM2_SOSTEGNO_ESTREMO_FIN U.I. 2: SOSTEGNO O ESTREMO FINE COMPETENZA

LIN_UIM2_SOSTEGNO_ESTREMO_INI U.I. 2: SOSTEGNO O ESTREMO INIZIO COMPETENZA

LIN_UIM3_ID U.I. 3: UNITÀ IMPIANTI

LIN_UIM3_LUNG_TERNA U.I. 3: LUNGHEZZA TERNA

LIN_UIM3_NUM_SOSTEGNI U.I. 3: NUMERO SOSTEGNI

LIN_UIM3_KM_EQUIVALENTI U.I. 3: KM EQUIVALENTI

LIN_UIM3_SOSTEGNO_ESTREMO_FIN U.I. 3: SOSTEGNO O ESTREMO FINE COMPETENZA

LIN_UIM3_SOSTEGNO_ESTREMO_INI U.I. 3: SOSTEGNO O ESTREMO INIZIO COMPETENZA

LIN_UIM4_ID U.I. 4: UNITÀ IMPIANTI

LIN_UIM4_LUNG_TERNA U.I. 4: LUNGHEZZA TERNA

LIN_UIM4_NUM_SOSTEGNI U.I. 4: NUMERO SOSTEGNI

LIN_UIM4_KM_EQUIVALENTI U.I. 4: KM EQUIVALENTI

LIN_UIM4_SOSTEGNO_ESTREMO_FIN U.I. 4: SOSTEGNO O ESTREMO FINE COMPETENZA

LIN_UIM4_SOSTEGNO_ESTREMO_INI U.I. 4: SOSTEGNO O ESTREMO INIZIO COMPETENZA

LIN_LUNGHEZZA_CT LUNGHEZZA IN CAVO TERRESTRE

LIN_LUNGHEZZA_CM LUNGHEZZA IN CAVO MARINO

LIN_COLLEGAMENTO_ESTERO COLLEGAMENTO CON L'ESTERO

LIN_COMPROPRIETA ELEMENTI IN COMPROPRIETÀ

Sempre a titolo di esempio, per la classificazione dei conduttori, alcuni degli attributi gestiti sono i seguenti:

LIN_ANNO_INSTALLAZIONE ANNO DI INSTALLAZIONE

LIN_FUSO_RIFERIMENTO_LIDAR FUSO DI RIFERIMENTO (DA LIDAR)

LIN_RIF_FASE FASE CONDUTTORE

LIN_NUM_CONDUTTORI NUMERO CONDUTTORI ELEMENTARI

LIN_CONDUTTORE_AEREO MATERIALE CONDUTTORE

LIN_TIPO_CONDUTTORE_AEREO TIPO CONDUTTORE

LIN_RIF_ASSET_COND RIFERIMENTO CONDUTTORE

LIN_ANNO_INSTALLAZIONE ANNO DI INSTALLAZIONE

L’elenco completo degli attributi utilizzati per tutte le classificazioni relative ad asset delle linee è contenuto, diviso per tipologia di asset, nei file inclusi di seguito (più tipologie nello stesso file).

## Il modello dati dell’asset

Tutti gli elementi costituenti una linea, dal primo al quarto livello, vengono gestiti per mezzo della struttura dati dell’asset. L’oggetto principale nel quale sono salvate le informazioni si chiama ASSET, è costituito da una tabella nel database dell’applicativo e ogni record in questa tabella rappresenta uno specifico asset.

Le informazioni gestite in questa tabella vengono definite “di testata”, ossia hanno cardinalità 1:1 rispetto all’asset stesso. I due campi ASSETNUM e SITEID sono utilizzati in un indice a chiave univoca; poiché nel progetto Terna è configurato un unico sito, nella tabella ASSET il campo ASSETNUM può essere considerato univoco. Viene popolato dal sistema con una chiave autogenerata. Il campo X3SAPID (o SAPID nelle tabelle custom come la X3TRONCHI) è utilizzato per salvare il codice parlante identificativo della sede tecnica SAP.

In questa tabella (ASSET) sono state aggiunte delle colonne specifiche per la gestione di dati propri di Terna, da migrare (ETL) e/o allineare (connettore) da e verso SAP:

| Colonna DB Manage | Descrizione / Etichetta SAP -> Manage | Nota |
| --- | --- | --- |
| X3SAPID | Sede tecnica -> Sede tecnica SAP | Questo campo è popolato solo per gli asset di primo livello, ossia le linee |
| X3SWERK | Divisione di ubicazione -> Distretto Territoriale | Legato a un dominio costruito sui gruppi di persone, definiti in Doc.1 paragrafo 2.2.1.1 Domini legati a campi di testata |
| X3KOSTL | Centro di costo | Campo a sola lettura |
| X3INGRP | Divisione di pianificazione della manutenzione -> Unità Impianti | Legato a un dominio costruito sui gruppi di persone, definiti in Doc.1 |
| X3ZZSOCP | Società Proprietaria | Campo a sola lettura |
| X3MSGRP | Impianto | Campo a sola lettura |
| X3EQFNR | Campo sort | Testo libero |
| X3BUKRS | Società | Campo a sola lettura, legato a un dominio (X3BUKRS) per gestirne la descrizione |
| X3KOKRS | Controlling area | Campo a sola lettura |
| X3GEWRK | Centro di lavoro responsabile | Legato a un dominio costruito sulla tabella X3GEWRK |

Nella tabella seguente sono invece presenti i campi standard della tabella ASSET, con l’informazione che permette il legame con i dati di provenienza SAP:

| Dato SAP | Colonna DB Manage | Descrizione / Etichetta SAP -> Manage | Nota |
| --- | --- | --- | --- |
| STTXU | STATUS | Stato utente -> Stato |  |
| INDBT | INSTALLDATE | In funzione dal -> Data di installazione |  |
| PLTXT | DESCRIPTION | Definizione sede tecnica -> Descrizione |  |

Altre strutture dati, con cardinalità 1:1 nei confronti dell’asset, che saranno utilizzate nel progetto Terna per indirizzare le seguenti informazioni di provenienza SAP:

Informazioni di toponomastica, quali Città, Paese, Provincia, Via, etc. Per queste tipologie di informazioni esiste una tabella specifica, la SERVICEADDRESS, verso la quale ogni asset realizza un legame per trattare le sue informazioni di toponomastica. Nell’ambito delle Linee la valorizzazione di queste informazioni non è prevista.

Un asset, censito nella tabella ASSET, per diventare una linea, un sostegno, un conduttore, un giunto o qualsiasi altro elemento previsto nel § 2.1 deve essere associato ad una delle classificazioni previste nel file 001-mxloader-linee-classificazioni.xlsm.

Questa operazione di classificazione provocherà l’inserimento nella tabella ASSETSPEC di tanti record quanti sono gli attributi previsti per la classificazione associata, come riportato in uno dei file Excel, da 002xx a 012xx.

Ad esempio, quando un asset è classificato come Conduttore, nella tabella ASSETSPEC verranno inseriti 42 record, uno per ciascuna delle 42 caratteristiche (attributi nel linguaggio del Manage), come contenuti nel file: 004-mxloader-linee-campata-conduttori-attributi.xlsm.

### La gestione dei domini

Nel Manage con il termine domini si intendono gli elenchi valori sulla base dei quali possono essere valorizzati i campi presenti nelle differenti applicazioni. In questo documento vengono elencati i domini utili ai fini dell’anagrafica presenti nei campi di testata e in determinati attributi di classificazione come la disposizione delle funi di guardia o il dominio dei codici di indisponibilità; tutti i domini presenti a sistema, tra cui quelli ricavati in fase di ETL (identificati dal prefisso X3DOM%) sono consultabili nel documento di LLD Anagrafica (EAM_LLD_GEN_ANA_Documento Anagrafica di LLD_20230703_V1.0.docx) e dettagliati nel file dei Domini (Domini.xlsx).

I domini utilizzati a livello di anagrafica sono divisi nelle seguenti 2 tipologie, alfanumerico e tabella:

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
| X3ZZSOCP | Elenco società proprietarie | ASSET. X3ZZSOCP | Il contenuto del dominio è allineato manualmente tra SAP ed EAM. Sono 2 valori, ovvero TR01, TR26 |
| X3KOKRS | Elenco contolling Area | ASSET. X3KOKRS | Il contenuto del dominio è allineato manualmente tra SAP ed EAM. È un solo valore, ovvero TR01 |
| X3BUKRS | Elenco società SAP | ASSET. X3BUKRS | Il contenuto del dominio è allineato manualmente tra SAP ed EAM. Contiene 10 valori (REV 20230710 of attributi-EAM-SAP.xlsx). |

Domini di tipo tabella:

| Nome | Descrizione | Dove è usato | Nota |
| --- | --- | --- | --- |
| X3SWERK | Elenco dei dipartimenti trasmissione | ASSET.X3SWERK | Il contenuto del dominio è allineato manualmente tra SAP ed EAM. Sono 8 valori. Il dominio si basa su una query della tabella PERSONGROUP |
| X3INGRP | Elenco delle unità impianti | ASSET.X3INGRP | Il dominio mostra i valori in base a quanto popolato nel campo ASSET.X3SWERK. Il contenuto del dominio è allineato manualmente tra SAP ed EAM. Sono 33 valori (REV 20230710 of attributi-EAM-SAP.xlsx). Il dominio si basa su una query della tabella PERSONGROUP. I valori sono selezionabili in base a quanto inserito nel campo X3SWERK quando viene popolato il dato sui secondi livelli, mentre sul primo livello (campo Unità impianti prevalente) permette di scegliere solo tra i valori già inseriti sui secondi livelli; in mancanza di secondi livelli si comporta come quello relativo ai secondi livelli. |
| X3GEWRK | Elenco dei centri di lavoro | ASSET.X3GEWRK | Il dominio mostra i valori in base a quanto popolato nel campo ASSET.X3SWERK. I valori del dominio sono mantenuti allineati con quelli presenti in SAP per mezzo del connettore SAP – EAM. Sono circa 8K valori (REV 20230710 of attributi-EAM-SAP.xlsx), selezionabili in base a quanto inserito nel campo X3SWERK |

#### Domini legati ad attributi di classificazioni

In questo paragrafo, divisi sulle rispettive tabelle, sono riportati tutti i domini coinvolti nel progetto Manage per Terna e utilizzati per popolare attributi di classificazioni; se è indicata anche la classificazione significa che il dominio, a parità di attributo, cambia da classificazione a classificazione.

Domini alfanumerici:

| Nome | Descrizione | Attributo / Attributo - classificazione | Nota |
| --- | --- | --- | --- |
| X3FASI01 | Elenco fasi | LIN_RIF_FASE | Il dominio contiene 3 valori: R, S, T |
| X3POSIZIONEFDG | Posizione della FdG | LIN_RIF_FUNE | Il dominio contiene 3 valori: DS, SX, 3 |

Domini di tipo tabella:

| Nome | Descrizione | Attributo / Attributo - classificazione | Nota |
| --- | --- | --- | --- |
| X3LIN_RIF_ASSET_CO | Conduttore di riferimento | LIN_RIF_ASSET_COND | Dominio per indicare su quale asset di quarto livello è montato l’asset, sempre di quarto livello, per cui deve valere questo legame (esempio del giunto in relazione al conduttore su cui è stato montato). I valori vengono presi dalla tabella asset, in base ad una regola SQL |
| X3LIN_RIF_ASSET_FD | FdG di riferimento | LIN_RIF_ASSET_FDG | Come sopra, ma per gli asset montati sulle funi di guardia. I valori vengono presi dalla tabella asset, in base ad una regola SQL |
| X3TRONCHI | Tronchi di ispezione | LIN_TRONCO_ISPEZIONE | Dominio per associare i tronchi di ispezione presi della tabella X3TRONCHI |
| X3SEDIINDISP | Codici indisponibilità | LIN_ELEMENTO_INDISP | Dominio per associare i codici indisponibilità alla linea o ai suoi elementi, presi dalla tabella X3SEDIINDISP |

## Il modello dati per i legami tra asset

Precedentemente abbiamo visto come si costruiscono i singoli componenti di una linea (compresa la linea stessa quale componente di primo livello), qualificandoli per mezzo dell’associazione con una determinata classificazione. Vediamo ora come i differenti componenti devono essere messi in relazione tra loro per permettere la modellazione di una linea.

Il modo con cui il Manage permette di gestire le gerarchie multilivello è quello del legame padre – figlio. La struttura dati impiegata è contenuta sempre nella tabella ASSET e opera per mezzo dell’informazione, su ciascun’asset, di quale è suo padre. Il limite di questo metodo risiede nell’impossibilità di gestire elementi che hanno più di un padre.

Gli asset passibili di avere più di un padre (massimo due) sono quelli che possono operare per due terne, ossia due linee distinte; questi asset sono i sostegni e le campate. I padri di questi asset chiaramente sono le linee, unico tipo di oggetto presente al primo livello (nella Figura 2 viene ben rappresentato dalle righe piene blu e rosse che collegano il primo con il secondo livello).

Per permettere una gestione flessibile, in grado di rappresentare sia la terna singola che la terna doppia, senza la necessità di creare asset “gemelli”, uno per ciascuna linea elettrica, il legame tra questi (in effetti, per omogeneità di gestione, non solo il legame sostegno e campata, ma anche tratta cavo ed estremo cavo, ossia tra tutti e 4 gli asset di secondo livello) e la linea viene gestito per mezzo di una differente struttura dati, gestita nella tabella ASSETLOCRELATION e chiamata relazione. Nella tabella che segue sono riportati i campi custom di questa tabella che sono specifici del progetto Terna:

| Colonna DB Manage | Descrizione / Etichetta SAP -> Manage | Nota |
| --- | --- | --- |
| X3SAPID | Codice parlante univoco della sede tecnica gestita in SAP | Viene gestito per gli elementi di secondo livello delle linee, in accordo a Doc.1, per permettere valori per le rispettive linee di appartenenza della doppia terna. |
| X3POSNR | Dato utilizzato per gestire la sequenza di campate e tratte cavo. Serve al connettore. L’ordinamento dei secondi livelli di Manage sarà gestito in un campo differente, non integrato con SAP. | Viene gestito per gli elementi di secondo livello delle linee (solo campate e tratte cavo), in accordo a Doc.1, per permettere valori per le rispettive linee di appartenenza della doppia terna. |
| X3SEQUENCE | Dato utilizzato per gestire la sequenza, nell’ambito di una linea, con cui si susseguono gli elementi di secondo livello. | Questo dato è presente solo nel Manage |

Gestiamo quindi, nell’ambito delle linee, sia relazioni che gerarchie, le prime tra il primo e il secondo livello, mentre le seconde tra secondo, terzo e quarto livello.

Ritornando alla Figura 2, resta però da risolvere la gestione dell’informazione di legame tra asset che rimangono comunque specifici di una singola line e non di entrambe e si trovano al terzo livello, come ad esempio i conduttori o gli attacchi conduttore (rispettivamente definiti come asset di terzo livello di campate e sostegni). Questi asset, che hanno la funzione di aggregatori logici degli asset fisici gestiti al quarto livello, devono necessariamente essere ricondotti alla linea elettrica di appartenenza.

Il risultato è stato ottenuto inserendo un nuovo campo nella tabella ASSET, denominato X3ROOTASSETNUM, gestito in automatico dalle differenti funzioni applicative che sono spiegate in questo documento, in §3 - Funzionalità applicative dell’anagrafica Linee. In questo campo, solo per gli asset di terzo livello Conduttori e Attacchi conduttore è inserito il codice univoco della linea di appartenenza.

Una particolarità riguardante il criterio per popolare X3ROOTASSETNUM si ha quando si rimuove una delle due linee che costituivano una terna doppia: in questo caso il campo X3ROOTASSETNUM per i due aggregatori Conduttori e Attacchi conduttore riferiti alla linea che viene rimossa, è automaticamente popolato con il codice univoco della linea che rimane e contestualmente è popolato a 1 il flag X3DADOPPIATERNA.

### Legami particolari gestiti a livello di attributi

Per permettere una rappresentazione e una gestione degli asset il più fedele possibile rispetto alla realtà e allo stesso tempo mantenere la prevista struttura a quattro livelli, sono implementate sia la possibilità di costituire legami tra asset di quarto livello e sia la possibilità di gestire informazioni, su asset potenzialmente comuni a due linee (terna doppia), in modo specifico per ciascuna linea.

#### Legami tra asset di quarto livello

Nella realtà operativa di Terna alcuni asset definiti al quarto livello sono in realtà funzionalmente dipendenti da altri asset, anch’essi di quarto livello (conduttori e FdG).

Esempi di questo si hanno con i giunti di riparazione delle strefolature, i distanziatori, le barrette preformate e tutti quegli altri asset che esistono solo al servizio dei conduttori o delle funi di guardia; seguendo sempre l’esempio proposto dello specifico conduttore, sostituendo il conduttore si rimuovono anche gli eventuali giunti o distanziatori che su questo sono montati.

Per permettere la gestione a sistema di questa informazione si utilizzano gli attributi LIN_RIF_ASSET_xxx (con xxx che può assumere il valore di COND oppure FDG). La scelta dell’asset da inserire sarà consentita solo tra quelli figli del medesimo aggregatore di terzo livello a cui appartiene l’asset sul quale si sta compilando l’attributo LIN_RIF_ASSET_xxx.

#### Gestione su asset comuni, specifiche per ciascuna linea

Nella soluzione sviluppata per Terna l’unico caso in cui è stato necessario seguire la strada della differenziazione degli attributi su asset comuni a due linee è rappresentato dalle funi di guardia ed è relativo alla posizione delle funi di guardia rispetto a ciascuna delle due linee a cui sono associate per mezzo dell’aggregatore di terzo livello Funi di guardia.

Su ciascuno degli asset di quarto livello rappresentativo di ogni fune di guardia (classificato quindi come LINEA \ CAMPATA AEREA \ FUNI DI GUARDIA \ FUNE DI GUARDIA) è gestito l’attributo LIN_RIF_FUNE che potrà assumere i valori DX, SX oppure 3. In caso di doppia terna ogni FdG ha due attributi, ciascuno riferito a una delle due linee della doppia terna; in questo modo, in caso le due linee abbiano versi opposti (il verso della linea è la convenzione con la quale si possono individuare gli asset in campo), la stessa FdG può essere DX per una linea e SX per l’altra.

Per fare questo è stata sfruttata la possibilità di avere attributi uguali nella tabella ASSETSPEC, a fronte del medesimo asset (nel nostro caso la specifica fune di guardia), inserendo nel campo SECTION il codice univoco di ogni linea della doppia terna. Nella casistica di inversione del verso di una linea, la gestione di questo campo è eseguita da una procedura che provocherà lo scambio dei valori DX e SX tra i reciproci (dal punto di vista dei valori SX e DX) due asset di tipo fune di guardia, per i soli attributi che hanno il campo SECTION popolato con il codice univoco della linea sottoposta ad inversione. Nel caso di doppia terna con una sola FdG non verrà modificata nessuna informazione, parimenti alla FdG con LIN_RIF_FUNE = 3.

## Genesi e ownership dei dati delle linee

In questo paragrafo si affronta il tema della creazione degli asset nel Manage e dell’ownership del dato.

Il primo livello di una nuova linea viene creato per mezzo dell’applicativo SAP.

Si prevede di creare il primo livello della linea in EAM al momento della creazione dell’anagrafica in SAP. Si avrà uno stato INAT (Inattivo) sull’Anagrafica EAM, che corrisponde ai tre stati utente SAP:

PROG: In progettazione

CANT: In cantierizzazione

DATT: Da attivare

Sarà presente un servizio di integrazione per la creazione e l’aggiornamento della Sede Tecnica di Primo Livello EAM per qualsiasi cambiamento della Sede Tecnica SAP in questi tre stati. La Sede Tecnica su EAM sarà Read Only per l’utente. Il connettore, invece, procederà al suo aggiornamento. Tramite il medesimo servizio di integrazione arriverà lo stato SAP ESER che innescherà il cambio di stato EAM da INAT a DATT. A questo punto, EAM diventerà owner della Sede Tecnica di Primo Livello fatta eccezione per i dati contabili di testata, la cui ownership rimarrà di SAP:

ZZSOCP: Società Proprietaria

MSGRP: Impianto

KOSTL: Centro di Costo

KOKRS: Controlling Area

Questo implica che qualsiasi modifica della Sede Tecnica di Primo Livello SAP, fatta eccezione per i campi contabili e per lo stato, verrà ignorata dal servizio. In virtù della potenziale cancellazione logica della Sede Tecnica di Primo Livello SAP negli stati PROG, CANT, DATT, sarà invocato il servizio di integrazione che innescherà il passaggio di stato della Sede Tecnica di Primo Livello EAM allo stato DELE. Ai fini dell’operatività SAP – connettore deve essere comunicato dal business quale operatività in SAP verrà utilizzata per attivare la cancellazione logica.

Alcune informazioni della linea rimarranno comunque di pertinenza SAP, non imputabili né modificabili dagli utenti del Manage, come ad esempio le informazioni contabili (vedi § 2.2).

Altre informazioni, come ad esempio la valorizzazione degli attributi o i cambi di stato verranno invece gestiti dal Manage verso SAP e di conseguenza dovranno essere non modificabili in SAP.

Gli asset di secondo, terzo e quarto livello sono invece creati e modificati unicamente nel Manage, e per mezzo del connettore inviati a SAP, dove saranno solo consultabili ma non modificabili dagli utenti.

## Strutture dati accessorie alla gestione delle linee

Nei paragrafi che seguono vengono spiegate le strutture dati per permettere la gestione dei tronchi di ispezione e la selezione dei codici di indisponibilità da collegare alla linea e ad alcuni dei suoi asset componenti.

### Tronchi di ispezione

I tronchi di ispezione nel Manage vengono gestiti per mezzo della tabella X3TRONCHI e nascono direttamente collegati alla linea. Successivamente dovranno essere associati agli aggregatori conduttori e cavi (LINEA / CAMPATA AEREA / CONDUTTORI e LINEA / TRATTA CAVO / CAVI) ispezionabili nell’arco della stessa giornata, per mezzo dell’attributo LIN_TRONCO_ISPEZIONE, con tutte le regole previste in questo documento nella sezione dedicata (3). Nella tabella X3TRONCHI sono gestite le seguenti colonne:

ASSETNUM: codice univoco della linea per la quale il tronco è stato creato;

SITEID & ORGID: popolati automaticamente con l’organizzazione e li sito di riferimento della linea;

SAPID: codifica della sede tecnica di tronco d’ispezione gestita in SAP;

TRONCONUM: codice univoco del tronco popolato con la funzione di auto numerazione gestita a livello di organizzazione;

X3TRONCHIID: chiave univoca di tabella, gestita da una sequence a livello di database;

ZONATRONCO: zonizzazione del territorio su cui insiste la campata in base alla condizione ambientale. Il campo è legato a un dominio alfanumerico, presente nella figura

LUNGHEZZATRONCO: campo calcolato, in base alla somma delle lunghezze delle campate o tratte cavo a cui il tronco è stato associato.

FREQUENZAANNUALE: campo numerico, usualmente popolato con 1,2,3,4. Non verrà però impostato un dominio.

NSOSTEGNIV: campo calcolato, indica il numero di sostegno/estremi cavo adiacenti alle campate/tratte cavo (campo non visibile a GUI)

SOSTFINE: campo calcolato, indica il sostegno di fine adiacente alla campata (campo non visibile a GUI)

SOSTINIZIO: campo calcolato, indica il sostegno di inizio adiacente alla campata (campo non visibile a GUI)X3GEWRK: Campo centro di lavoro responsabile del 1° livello della Linea (campo non visibile a GUI)

X3INGRP: Campo Unità impianti prevalente del 1° livello della Linea (campo non visibile a GUI)

### Codici di indisponibilità

I codici di indisponibilità sono gestiti per mezzo della tabella X3SEDIINDISP. Non possono essere creati nel Manage, ma solo recepiti, per mezzo del connettore, da SAP, dove sono gestiti come delle sedi tecniche e associati alle sedi tecniche di linea col la valorizzazione di un’apposita caratteristica. Tale flusso verrò dettagliato nel documento di LLD del Connettore.

Nel Manage, sulle linee, l’associazione è gestita a livello di asset di 1° e 3° livello (quindi LINEA per il 1° mentre LINEA / CAMPATA AEREA / CONDUTTORI e LINEA / TRATTA CAVO / CAVI per il 3°) per mezzo dell’attributo LIN_ELEMENTO_INDISP. Per compilare tale attributo, l’utente può selezionare uno qualsiasi dei codici presenti nella tabella X3SEDIINDISP, che iniziano con la lettera L (es. L20001A). La tabella è composta dalle seguenti colonne:

STRNO: è il codice parlante SAP della sede tecnica;

TPLNR: è il codice interno SAP della sede tecnica;

PLTXT: è la descrizione della sede tecnica, ossia del codice indisponibilità;

STTXT: è lo stato della sede tecnica di indisponibilità;

TIPOELEMENTO: tipologia di elemento di indisponibilità;

## Navigazione dei dati delle linee in Manage

Lo scopo di questo paragrafo è spiegare il modo con cui gli utenti del Manage interrogheranno e visualizzeranno le informazioni delle linee.

La filosofia generale seguita è quella di rendere immediatamente evidente all’utente la composizione di una linea, mostrando in una tabella tutti gli asset di secondo livello che la compongono.

La nuova applicazione sviluppata a questo scopo si chiama Linee, la quale è impostata per mostrare l’elenco di tutti gli asset associati alla classificazione LINEA; selezionando una linea, questa viene caricata e l’applicazione mostra le differenti schede che la compongono. Nei paragrafi seguenti sono spiegate le informazioni presenti in ciascuna scheda dell’applicazione.

### Linea

Questa è la scheda principale dell’applicazione, la prima che si apre nel momento in cui si seleziona una linea dall’elenco.

Nella parte superiore dell’applicazione vengono mostrate le informazioni direttamente legate alla linea, come ad esempio:

Descrizione della linea, ossia il suo nome, ad esempio SAVONA – MORIGALLO;

Codice SAP (SAP ID) della linea, esempio 22268D2;

Stato della linea, esempio NADI;

Divisione di Ubicazione, 2602 - Dipartimento Trasmissione Nord

Unità impianti prevalente

Centro di lavoro responsabile

Centro di costo

Società proprietaria

Società, con relativa descrizione

Impianto

Controlling area

Al di sotto dei campi sopra indicati è inserita una tabella che contiene tutti gli asset di secondo livello della linea e mostra le seguenti colonne (elenco non esaustivo):

Sequenza degli asset di secondo livello appartenenti alla linea selezionata in testata (scelta iniziale dall’elenco), in base al quale sono ordinate le righe degli asset della tabella;

Codice SAP (SAP ID);

POSNR;

Codice univoco dell’asset nel Manage;

Sapid da ricodificare;

Descrizione dell’asset di secondo livello;

Classificazione;

In funzione dal;

Stato dell’asset di secondo livello;

Unità impianti di appartenenza dell’asset di secondo livello

Centro di lavoro responsabile

Selezionando e aprendo il dettaglio su una qualsiasi delle righe detta tabella si attiva la possibilità di visualizzare i dati degli elementi che compongono gli asset di secondo livello, oltre alle caratteristiche tecniche degli stessi. Per semplificare la comprensione, i seguenti paragrafi spiegano, divisi per livello di profondità, quali dati vengono resi disponibili e con quali caratteristiche (sola lettura o modifica).

#### Secondo livello linea

Di default il secondo livello è mostrato chiuso; aprendolo viene mostrata tutta la gerarchia degli asset correlati (e.g. CAMPATA AEREA>ATTACCO CONDUTTORE>ARMAMENTO CONDUTTORE). Cliccando sull’icona vicino al singolo assetnum è possibile visualizzare le specifiche tecniche, con i seguenti campi:Codice identificato univoco dell’attributo di classificazione (e.g. LIN_LUNGHEZZA);

Descrizione dell’attributo di classificazione (e.g. LUNGHEZZA);

Data effettiva modifica, campo compilabile e modificabile solo se pertinente al relativo codice attributo di classificazione e in conformità alle regole di protezione. È consentito inserire la data corrente o una data passata. Sebbene il campo a video risulti sempre vuoto, il valore eventualmente impostato sarà registrato e avrà effetto sullo storico delle specifiche tecniche, determinando la validità effettiva dell’attributo a partire dalla data indicata.

Valore data, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Valore alfanumerico, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Valore numerico, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Unità di misura, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Valore da tabella, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione

Section, gestito nei casi indicati nel paragrafo § 2.3.2.1

#### Terzo livello linea

Il terzo livello mostra, coerentemente con il secondo livello selezionato in § 2.6.1, gli aggregatori comuni e gli aggregatori specifici della linea selezionata all’inizio. In caso di terna doppia sono visualizzati i soli aggregatori di pertinenza. Cambiando linea si visualizzano gli altri aggregatori dell’altra linea oltre, chiaramente, a quelli comuni. Ad esempio, selezionando un sostegno, il medesimo aggregatore telaio verrà visualizzato dalle due linee a cui appartiene il sostegno, mentre, da ciascuna linea, viene mostrato lo specifico attacco conduttore di pertinenza.

Cliccando sull’icona vicino al singolo assetnum è possibile visualizzare le specifiche tecniche, con i seguenti campi:Codice identificato univoco dell’attributo di classificazione (e.g. LIN_DISPOSIZIONE_FASI, attributo presente sul 3° livello ATTACCHI CONDUTTORE);

Descrizione dell’attributo di classificazione (e.g. SCHEMA DISPOSIZIONE FASI);

Data effettiva modifica, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione. È consentito inserire la data corrente o una data passata. Il valore impostato influenzerà lo storico delle specifiche tecniche.

Valore data, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Valore alfanumerico, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Valore numerico, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Unità di misura, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Valore da tabella, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Section, gestito nei casi indicati nel paragrafo § 2.3.2.1

#### Quarto livello linea

Il quarto livello mostra, coerentemente con il terzo livello selezionato in § 2.6.1.2, i relativi asset.

Cliccando sull’icona vicino al singolo assetnum è possibile visualizzare le specifiche tecniche, con i seguenti campi:Codice identificato univoco dell’attributo di classificazione (e.g. LIN_DISPOSIZIONE_FASI, attributo presente sul 3° livello ATTACCHI CONDUTTORE);

Descrizione dell’attributo di classificazione (e.g. SCHEMA DISPOSIZIONE FASI);

Data effettiva modifica, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione. È consentito inserire la data corrente o una data passata. Il valore impostato influenzerà lo storico delle specifiche tecniche.

Valore data, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Valore alfanumerico, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Valore numerico, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Unità di misura, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Valore da tabella, compilabile e modificabile se pertinente al relativo codice attributo di classificazione e coerentemente alle regole di protezione;

Section, gestito nei casi indicati nel paragrafo § 2.3.2.1

### Riepilogo dati

In questa scheda sono riportate le sommatorie, raggruppate per ciascuna unità impianti, dei seguenti dati della linea selezionata:

Il totale dei sostegni (conteggio asset di classificazione Sostegno);

Il totale dei Km Linea (sommatoria dell’attributo Lunghezza Linea presente su campate e tratte cavo, moltiplicato per un fattore K definito in funzione del livello di tensione);

La lunghezza della terna (sommatoria dell’attributo Km di circuito).

Sostegno/Estremo inizio di competenza;Sostegno/Estremo fine di competenza.

I seguenti sono invece calcolati sull’intera linea, indipendentemente dall’unità impianti:

Il totale dei Km di cavo terrestre (sommatoria dell’attributo Km per le tratte di cavo con l’attributo Tipo posa = Terrestre);

Il totale dei Km di cavo marino (sommatoria dell’attributo Km per le tratte di cavo con l’attributo Tipo posa = Marino)

### Tronchi

In questa scheda sono gestiti (creati e visualizzati) i tronchi di ispezione associabili agli asset della linea a cui devono essere riferiti. I tronchi sono visualizzati in formato tabellare, con le seguenti colonne (elenco indicativo);

Descrizione tronco

Identificativo SAP del tronco

L’associazione viene fatta per mezzo dell’attributo LIN_TRONCO_ISPEZIONE - Tronco di ispezione, sui seguenti elementi:

Linea / Campata Aerea / Conduttori

Linea / Tratta Cavo / Cavi

Trattandosi di compilazione di un attributo di specifica di un asset di terzo livello, si agisce come spiegato in § 2.6.1.2.

### Dati linea

In questa scheda, in formato tabellare, sono mostrati tutti gli attributi della classificazione LINEA della linea selezionata. I dati mostrati sono i seguenti:

Codice dell’attributo;

Descrizione dell’attributo;

Valore data, compilabile e modificabile se pertinente al relativo codice attributo e coerentemente alle regole di protezione;

Valore alfanumerico, compilabile e modificabile se pertinente al relativo codice attributo e coerentemente alle regole di protezione;

Valore numerico, compilabile e modificabile se pertinente al relativo codice attributo e coerentemente alle regole di protezione;

Unità di misura, compilabile e modificabile se pertinente al relativo codice attributo e coerentemente alle regole di protezione;

Valore da tabella, compilabile e modificabile se pertinente al relativo codice attributo e coerentemente alle regole di protezione

### Stazioni in relazione

In questa scheda sono mostrate tutte le stazioni e i relativi stalli linea che hanno una relazione con la linea selezionata. I dati sono visualizzati in formato tabellare, con le seguenti colonne (elenco indicativo);

SAP ID della stazione

Codice Manage Assetnum della stazione

Descrizione della stazione

SAP DI Stallo Linea

Codice Manage Assetnum dello Stallo Linea

Descrizione dell’asset Stallo Linea

### Linee in relazione

In questa scheda sono mostrate tutte le linee che hanno una relazione con la linea selezionata, ossia condividono almeno un asset di secondo livello (sostegno o campata) in terna doppia. I dati sono visualizzati in formato tabellare, con le seguenti colonne (elenco indicativo):

SAP ID della linea

Codice Manage Assetnum della linea;

Descrizione della lineaStato della Linea

# Funzionalità applicative dell’anagrafica Linee

Nel seguente capitolo verranno illustrate le funzionalità applicative dell’anagrafica Linee ad alto livello derivate dal file dei casi d’uso.

Le schermate riportate da IBM MAS Manage sono state ricavate dall’ambiente di PoC e non sono da considerarsi definitive, alla stregua dei nomi delle funzioni e delle applicazioni, che potranno variare in base a vincoli o esigenze implementative.

## Attori dei processi

In questo paragrafo si riportano gli attori dei differenti scenari d’uso analizzati.

Gli attori individuati appartengono ai seguenti gruppi:

AM: gestori centrali dell’asset management;

UI: unità impianti. Sono i gruppi di specialisti della manutenzione, distribuiti sul territorio italiano;

Dispacciamento

La gestione centralizzata di AM serve a garantire che tutti i fruitori dei dati di anagrafica (al momento DT e UI) si possano basare su informazioni univocamente riconosciute e validate, come ad esempio le classificazioni per qualificare un asset o le specifiche tecniche per definirlo.

## Organigramma di riferimento

Per permettere la gestione di visibilità e azioni su elementi appartenenti ad unità impianti differenti del medesimo distretto / dipartimento territoriale, dovranno essere gestiti dei rapporti di parentela tra UI e DT. Di seguito l’organigramma di riferimento per eventuali, ulteriori relazioni configurabili in IBM MAS Manage.

Figura 6 - Organigramma Terna

## Gruppi di protezione e gruppi di persone

I gruppi di protezione (gestiti per mezzo dell’applicazione Gruppi di protezione) servono ad attribuire l’utilizzo delle applicazioni di IBM MAS Manage e delle specifiche funzionalità connesse. In dettaglio, i gruppi di protezione sono utilizzati per definire e regolare l'accesso e le autorizzazioni degli utenti alle applicazioni, alle azioni e ai dati in Maximo.

Un utente può appartenere a più gruppi di protezione.

I gruppi di persone, invece, servono a raggruppare le persone che devono condividere criteri e caratteristiche. Le 33 UI (Unità Impianti) e gli 8 DT (Dipartimenti o Distretti Trasmissione) saranno gestiti con i gruppi di persone (applicazione Gruppi di persone); a questi gruppi vengono legati, ad esempio, i criteri di filtro che in base all’UI di appartenenza, permettono di stabilire la modificabilità piuttosto che la sola lettura degli asset e delle relative specifiche tecniche (attributi di classificazione).

Analogamente ai gruppi di protezione, un utente può appartenere a più gruppi di persone.

## Gestori centrali dell’asset management

Per la gestione degli attori di AM (Asset Management) verranno creati i seguenti gruppi di protezione.

Gruppo Centro 1:

Gestisce tutte quelle informazioni che possono essere definite comuni, ossia quelle che influenzano e supportano il lavoro di tutte le DT. Le tipologie di oggetti attualmente individuati sono:

Modelli a supporto della creazione di linee e stazioni come, ad esempio, sostegni e campate con tutti i rispettivi asset di 3° e 4° livello;

Classificazioni;

Definizione delle regole sugli attributi calcolati (vedi § 3.12.4);

Aggiunta e cancellazione di attributi direttamente nella tabella delle specifiche tecniche sugli asset;

Elenchi valori;

Associazione dei differenti domini all’attributo POSNR di stazione, in base alla classificazione a cui è associato, per permettere di mostrare il sotto insieme corretto di valori tra cui scegliere;

Ha visibilità e possibilità di azione su tutta l'anagrafica, esclusi i dati di specifici attributi (vedi § 3.12.2), per i quali sarà necessaria l’appartenenza a determinati gruppi di persone per poterli editare

Gruppo Centro 2:

Ha visibilità e possibilità di azione su tutta l'anagrafica, esclusi i dati di specifici attributi (vedi § 3.12.2), per i quali sarà necessaria l’appartenenza a determinati gruppi di persone per poterli editare

Gruppo Centro 3:

Ha visibilità su tutta l'anagrafica, ma in sola lettura

## Personale delle unità impianti

Per la gestione degli attori delle Unità Impianti verrà creato il gruppo di protezione UI (X3UI01 - Gruppo unità impianti 1), con i seguenti privilegi:

Creazione e aggiornamento di tutti i dati di anagrafica relativi a Linee e Stazioni di pertinenza, esclusi i dati di specifici attributi (vedi § 3.12.2), per i quali sarà necessaria l’appartenenza a determinati gruppi di persone per poterli editare.

Visibilità di tutta l'anagrafica.

Il legame DT – UI è invece implementato per mezzo dell’applicazione Gruppi di persone, con la quale viene gestita la relazione 1:n tra DT e UI. Le informazioni di riferimento sono state prese dal documento 20221222_DT.pdf, di cui si riporta lo schema utilizzato, ossia 8 gruppi di persone per gli otto distretti / dipartimenti e 33 gruppi, ciascuno figlio di un DT, per le differenti unità impianti.

L’operatività su IBM MAS Manage del personale delle UI sarà il medesimo al di sotto della stessa DT. Per quanto riguarda invece la sola visibilità delle informazioni di anagrafica degli asset non vi saranno vincoli.

Figura 7 - Legame DT – UI

Ogni colonna rappresenta la composizione di una DT (parte blu, in alto) mentre le righe azzurre UNITA’ IMPIANTI, nell’incrocio con la rispettiva colonna, definiscono il nome e la composizione della specifica unità impianti. Ad esempio, la DT NORD-OVEST è composta da 4 UI, Pont S. Martin, Novara, Torino e Genova.

I codici utilizzati per gestire DT e UI sono i medesimi impiegati in SAP e riportati nelle due tabelle seguenti.

Figura 8 - Codici DT

Figura 9 - Codici UI

Per l’associazione DT – UI riferirsi al contenuto di Figura 7.

## Personale del dispacciamento

Per la gestione degli attori del Dispacciamento verrà creato il gruppo di protezione Dispacciamento, con i privilegi:

Visibilità su tutta l'anagrafica, ma in sola lettura

Il personale del dispacciamento interagisce nel processo per la decisione di una parte del codice del primo livello di linee e stazioni: digit da 3 a 5 dell’ID descrittivo SAP. Dialoga con le unità impianti, ma non ha un ruolo funzionale in IBM MAS Manage.

## Attività generali che coinvolgono i dati

Questo capitolo ha l’obiettivo di riportare il disegno di alto livello dei requisiti funzionali, specifici per ciascuno dei casi d’uso raccolti durante l’analisi degli scenari dell’anagrafica, divisi tra creazione, aggiornamento e interrogazione.

In generale, salvo non differentemente indicato (ad esempio in § 3.12.5), i dati presenti su elementi di livello superiore non sono fisicamente replicati verso i livelli inferiori; da esempio, l’informazione del DT di una linea non viene replicata sugli asset dei livelli sottostanti, ma da ciascun livello sottostante sarà sempre possibile, grazie all’appartenenza alla linea stessa, conoscere il rispettivo DT.

### Creazione

In questo ambito ricadono tutte le attività per le quali viene creata una o più nuove entità in IBM MAS Manage. Con il termine entità non si intendono semplicemente dei nuovi record, ma la creazione e popolamento di un insieme di oggetti che nel loro complesso rappresentano oggetti di business complessi, nello specifico gli asset, per mezzo dei quali saranno gestiti tutti gli elementi degli anagrafici impianti (ossia gli equipment e le sedi tecniche di SAP), con le eventuali informazioni a corredo, come le relazioni e le classificazioni. La creazione e la gestione di linee e stazioni verrà fatta per mezzo di due nuove applicazioni, Linea e Stazione, il cui design sarà ottimizzato sulla base delle due famiglie di oggetti che verranno gestite.

### Creazione di una nuova linea (terna singola, doppia e ripresa da “ammazzettamento”)

Per lo svolgimento delle funzioni relative alle linee si utilizzerà la nuova applicazione Linee. La creazione di una nuova linea può nascere da una nuova palificazione oppure, ad esempio, dalla segmentazione di una linea esistente (vedi § 3.15), in seguito alla realizzazione di una stazione in cui la linea entra e poi esce.

Sia che si tratti di una nuova linea oppure della segmentazione di una linea esistente, il nuovo primo livello dovrà essere creato in SAP; l’integrazione SAP <-> IBM MAS Manage si occuperà di renderlo disponibile anche in IBM MAS Manage.

Nel caso di nuovi asset di secondo livello, sia aereo che cavo, verrà impiegata la funzione:

Inserisci Tratta (aerea/cavo)

Questa funzione è richiamabile una volta selezionata una linea e gestirà in input le seguenti informazioni:

Tipo tratta (Aerea/Cavo)

Numero di elementi da generare;

Elemento di inizio (Facoltativo): Punto esistente della linea a cui agganciarsi per la successiva generazione;

Elemento di fine (Facoltativo): In caso di aggancio a elemento esistente, indica se generare prima o dopo. Da compilare solo nella casistica del cambio tracciato (vedi §3.19, caso 4)

Unità Impianti: obbligatorio con selezione possibile solo tra le UI della divisione di ubicazione del primo livello della linea;

Centro di lavoro responsabile: obbligatorio con selezione possibile solo tra i valori compatibili con la divisione di ubicazione del primo livello della linea;

Data di installazione: (Facoltativo): se lasciato vuoto, la data sugli asset creati è quella di sistema al momento dell’esecuzione;

Figura 10 - Funzione di generazione di elementi di linea (2°- 3°- 4° livello)

In fase di creazione, il campo UI sugli asset creati verrà popolato con quanto scelto sopra; tale valore potrà essere successivamente modificato manualmente dall’utente direttamente sui singoli asset di 2° livello, ma sempre in ottemperanza della regola di appartenenza alla divisione di ubicazione della linea.

Con le informazioni precedenti lo strumento di creazione massiva delle linee creerà tanti sostegni e campate o tratte cavo ed estremi cavo (con tutti i rispettivi asset sottostanti come previsti in §3.17) e gestirà le informazioni di relazione, tra il primo livello e i secondi livelli.

L’interazione con il dispacciamento è necessaria per accordarsi su parte della codifica costituente l’ID SAP e sulla realizzazione, sempre in SAP, dei codici indisponibilità che dovranno essere associati alla linea e ai suoi componenti di 2° livello.

Figura 11 - Interazione del dispacciamento, extra sistema, con la UI d’interesse

#### Elementi della linea aerea e gestione della doppia terna

La codifica descrittiva ID SAP per la parte aerea di una linea (ad esempio NNXXXCN-NNNXXXXX-S per il sostegno e NNXXXCN-NNNXXXXX per la campata) verrà generata (vedere § 3.16.1) e associata alla relazione tra il primo livello con i secondi livelli. La parte facoltativa a disposizione dell’utente, che può essere successivamente ricodificata, (ossia NNNXXXXX) verrà popolata automaticamente con un progressivo nei primi tre digit (NNN) e con il simbolo del meno (-) per i successivi 5 digit (ad esempio, 001-----).

Il risultato della generazione eseguita per mezzo della box di dialogo (Figura 10) produce l’aggiunta, alla nuova linea di provenienza SAP, di tutti gli elementi che la compongono.

Figura 12 - Esempio di linea aerea risultante dalla precedente azione

La linea appartiene al dipartimento trasmissione nord ovest, in parte manutenuta dalla unità impianti Torino (UTO) e in parte dall’unità impianti Novara (UNO).

Per la gestione della creazione ex novo di una doppia terna (linee 22123A1 e 22123A2) si utilizzerà il seguente approccio:

Esecuzione di tutte le fasi precedenti volte alla creazione di una linea (22123A1) in terna singola

Richiamo nell’applicazione Linee della nuova linea (22123A2) ed esecuzione della funzione di Associazione in terna doppia di sostegni e campate selezionati dalla linea precedente (22123A1)

Nel caso in cui solo alcuni sostegni e campate saranno gestite in doppia terna, la scelta al punto precedente degli elementi della linea (22123A1) dovrà comprendere solo le campate e sostegni che andranno effettivamente condivisi con la nuova linea (22123A2).

Nella seguente serie di immagini è rappresentato il caso sopra esposto.

Figura 13 - La linea (9101) che deve essere in doppia terna con la precedente (8836) di Figura 12

Con la funzione di Associazione in terna doppia scelgo gli elementi della linea precedente da associare alla nuova; nell’esempio li scelgo tutti, ossia le due linee, in doppia terna, partono e arrivano nel medesimo punto, su palificazione condivisa. Nell’immagine seguente il filtro è stato attivato per la linea 8836.

Figura 14 - Finestra di dialogo per scegliere gli elementi di 2° livello da rendere comuni

Il risultato dell’azione di associazione di elementi di una linea ad un'altra linea è quello di ottenere la seconda linea (9101) popolata con i medesimi elementi della linea gestita precedentemente

Figura 15 - Elementi rappresentanti la doppia terna, per le due linee

Se la nuova linea (nell’esempio la 18406249, ossia 22266B1) avesse già degli elementi di secondo livello, successivamente alla schermata di dialogo di Figura 14, sarà richiesto all’utente di inserire anche la sequenza linea e la posizione.

Anche nel caso di ripresa di sostegni e campate che sono state oggetto di “ammazzettamento” (vedi § 3.8.2) si utilizzerà la medesima funzione Associazione in terna doppia; in questo caso, chiaramente, esistendo già gli aggregatori di terzo livello, la funzione non andrà a crearli nuovamente, ma semplicemente li assocerà alla nuova linea. Il discrimine per capire quale aggregatore dovrà essere riassociato alla nuova linea si basa sul fatto che è popolato a 1 il flag X3DADOPPIATERNA (vedi ultimo capoverso di § 2.3). Tutti gli elementi in doppia terna saranno evidenziati in blu, come in figura 15.

#### Elementi della linea in cavo

Nelle linee in cavo, come asset di secondo livello, sono gestiti Estremo Cavo e Tratta Cavo, con una particolarità per quanto riguarda l’estremo cavo, il cui modello non è univoco, ma deve rappresentare due strutture differenti, quando al 4° livello ha dei terminali oppure dei giunti. Classificazioni per l’estremo cavo:

LINEA \ ESTREMO CAVO \ TERMINALI \ TERMINALE

LINEA \ ESTREMO CAVO \ GIUNTI \ GIUNTO CAVO

Nella struttura dei SAPID un estremo cavo con terminali deve gestire la lettera H mentre un estremo cavo con giunti deve gestire la lettera R.

Figura 16 - Elementi di una linea in cavo

Successivamente alla creazione automatica, che gestisce la congruità tra H ed R per estremo cavo con terminale ed estremo cavo con giunto, la successiva gestione sarà a carico degli utenti.

L’operatività implementata, analogamente a quanto riportato in §3.7.2, usando la funzione Inserisci Tratta (Aerea/Cavo) sarà la seguente:

Tipo tratta (Aerea/Cavo): compilato con Cavo

Numero di elementi da generare;

Elemento di inizio (Facoltativo): Punto esistente della linea a cui agganciarsi per la successiva generazione;

Elemento di fine (Facoltativo): In caso di aggancio a elemento esistente, indica se generare prima o dopo. Da compilare solo nella casistica del cambio tracciato (vedi §3.19, caso 4)

Unità Impianti: obbligatorio con selezione possibile solo tra le UI della divisione di ubicazione del primo livello della linea;

Centro di lavoro responsabile: obbligatorio con selezione possibile solo tra i valori compatibili con la divisione di ubicazione del primo livello della linea;

Data di installazione: (Facoltativo): se lasciato vuoto, la data sugli asset creati è quella di sistema al momento dell’esecuzione;

Questo tipo di funzione, per la tratta in cavo, creerà sempre una sequenza alternata di tratte ed estremi, iniziando e finendo con un estremo di tipo terminale, ossia con un asset di tipo Terminale al quarto livello. I terminali intermedi, invece, avranno al quarto livello un asset di tipologia Giunto Cavo.

#### Linee miste

Nel caso di linee miste (aeree e in cavo) la procedura di generazione verrà ripetuta più volte, inserendo dalla seconda generazione in poi, il punto di aggancio da cui proseguire la linea (Elemento di inizio).

#### Creazione e gestione dei tronchi d’ispezione

Il concetto di tronco di ispezione è impiegato per raggruppare elementi omogenei (aereo o in terra) e contigui di una stessa linea, utilizzando come parametro l’ispezionabilità degli stessi nell’arco di una giornata.

I tronchi di ispezione saranno gestiti come elementi specifici (non saranno quindi gestiti come asset), relazionati alla linea dove dovranno essere utilizzati. La creazione verrà fatta in una scheda (Tronchi d’Ispezione) dell’applicazione di gestione delle linee, creando in una tabella tutti i tronchi necessari per completare l’ispezione dell’intera linea.

Nell’utilizzo i tronchi di ispezione creati nell’ambito della linea verranno poi associati, per mezzo di un attributo (LIN_TRONCO_ISPEZIONE), a tutti gli elementi di terzo livello, figli di quelli contigui di secondo livello, che potranno essere ispezionati nello stesso giorno (la coerenza di queste informazioni è a cura dell’utente che associa il tronco). Per chiarire, ciò significa che i tronchi di ispezione vengono associati ai seguenti aggregatori di 3° livello:

Linea / Campata Aerea / Conduttori

Linea / Tratta Cavo / Cavi

Nella tabella della scheda di gestione tronchi della linea verranno anche mostrate le seguenti informazioni calcolate:

Lunghezza totale degli elementi a cui ogni tronco d’ispezione farà riferimento; nello specifico, la lunghezza totale delle campate e delle tratte cavo;

Numero totale dei sostegni per ogni tronco (ricavato algoritmicamente, eseguendo la sommatoria di tutti i sostegni collegati alle campate legate ad un medesimo tronco)

Le informazioni della zona tronco e della frequenza annuale saranno invece popolati manualmente dagli utenti, per ogni tronco d’ispezione.

Quando il tronco viene associato alla specifica campata / tratta cavo, il valore dell’attributo Zona sulla campata verrà popolato automaticamente, in base a quanto inserito sul tronco stesso, ma il dato riportato potrà essere modificato dall’utente.

L’omogeneità dei tronchi d’ispezione (tra aereo e cavo) si otterrà in base al primo utilizzo di ogni tronco: quando verrà associato ad una campata, potrà successivamente essere associato solo ad altre campate. Viceversa, quando sarà associato ad una tratta cavo, potrà essere associato solo ad altre tratte cavo.

Nell’immagine seguente è riportato, dall’applicazione delle Linee, la scheda per la creazione dei tronchi utilizzabili in una linea (nello specifico, la linea 13775886).

Figura 17 - Tronchi di ispezione associati alla linea

Nell’immagine seguente si riporta un esempio di associazione ad un elemento della linea (in questo caso l’aggregatore di terzo livello 14275200- CONDUTTORI, che gestisce l’attributo LIN_TRONCO_ISPEZIONE - Tronco di ispezione).

Figura 18 - Tronco di ispezione associato alla campata tramite il conduttore

### Creazione massiva asset di 4° livello (linee)

In questo scenario viene indirizzata la necessità di creare in modo massivo un numero, definito dall’utente, di asset singoli, tutti uguali tra loro.

L’utente sceglie il modello in base al quale gli asset di quarto livello verranno creati, il numero di oggetti che devono essere creati e il codice dell’asset di terzo livello al quale dovranno essere associati.

L’applicazione di riferimento per fare questa operazione si chiama Modelli asset (attenzione, non confondere questi modelli, che permettono di creare n asset, non strutturati in gerarchia, con i modelli generati da IAS che vengono spiegati nel § 3.17).

### Creazione asset singolo (linee)

Per la creazione di un asset singolo di linea di secondo livello (campata-sostegno oppure tratta cavo-estremo cavo, con tutti i rispettivi asset di 3° e 4° livello), si agisce dall’applicazione di gestione delle linee, richiamando la linea alla quale dovrà appartenere il nuovo asset e utilizzando la funzione:

Genera asset singolo di secondo livello

Poiché, nelle tratte cavo, aggiungendo prima o dopo di un estremo cavo di tipo terminale e non potendo, un estremo cavo terminale essere contenuto tra due tratte cavo, la procedura elimina l’asset estremo cavo esistente, lo sostituisce con un estremo cavo di tipo giunto e solo dopo (oppure prima, in caso ci si sia collegati all’inizio della linea) creerà l’estremo cavo di tipo terminale. In questo caso, quindi, non verrà creata una coppia di asset ma tre asset, uno (l’estremo cavo di tipo giunto) in sostituzione di quello demolito (l’estremo cavo di tipo terminale) e poi una tratta e infine un nuovo l’estremo cavo di tipo terminale.

Nelle immagini seguenti si vede le situazione prima e dolo la creazione di una coppia tratta cavo-estremo cavo aggiunta all’inizio della linea, quindi prima dell’estremo cavo 23210C1-000------H e riferito all’asset 25462073.

Figura 19 - crea asset singolo in cavo Situazione prima della modifica

Nella situazione dopo la modifica si hanno 3 nuovi asset, riquadrati in rosso, come da regola precedentemente descritta.

Figura 20 - crea asset singolo in cavo Situazione dopo la modifica

Questa funzione gestisce in input:

Tipo di tratta: obbligatorio, impiegato per definire se creare una coppia campata-sostegno oppure una coppia tratta cavo-estremo cavo, inserendo, rispettivamente, Aereo oppure Cavo;

Sequenza Linea: è il numero di sequenza lungo la linea alla quale il nuovo asset deve essere collegato;

Unita Impianti: obbligatorio, con valori selezionabili compatibili con il distretto di appartenenza della linea

Centro di lavoro responsabile: obbligatorio, con valori selezionabili compatibili con il distretto di appartenenza della linea

Data di installazione: facoltativo, se non popolato, sull’asset verrà inserita la data di sistema del momento della creazione

L’asset creato verrà automaticamente messo in relazione alla linea e avrà la struttura (asset figli) derivante all’applicazione del modello.

Figura 21 - Informazioni per creazione asset di linea di 2° livello

Per la parte aerea di una linea la situazione è più semplice, in quanto il sostegno, a differenza dell’estremo cavo, non si differenzia se messo all’estremo di una linea oppure al suo interno.

Figura 22 - Inserimento asset contestualizzato su una linea aerea

Il risultato dell’azione di Figura 22 sarà la creazione di una nuova campata precedente alla campata.

Se l’asset creato dovrà essere in relazione con due linee (doppia terna), lo creo sempre all’interno di una delle due linee (ad esempio linea A), e successivamente lo metterò in relazione con la seconda linea (ad esempio linea B), come spiegato in § 3.7.2 per la doppia terna.

La creazione di un asset di secondo livello comporterà la creazione automatica di tutti gli asset figli, di terzo e quarto livello, in base al modello collegato (item a rotazione per campata, sostegno, ecc.). L’asset creato, analogamente a quanto indicato sempre in § 3.7.2 avrà il campo dell’unità impianti già popolato con quello inserito nel primo livello, ma comunque modificabile.

Per la creazione di asset singoli di quarto livello devono essere gestiti i seguenti tre casi:

Sostituzione di un asset esistente: questa funzione è utilizzata, ad esempio, quando viene sostituito un conduttore, una fune di guardia, gli isolatori, ecc.

Aggiunta di un asset: serve per montare un nuovo asset, che si aggiunge agli altri di 4° livello, ma non serve gestire una relazione funzionale con nessuno degli esistenti. Ad esempio, viene montata una bobina di sbarramento o un dispositivo TLC.

Aggiunta di un asset in relazione con un altro: il caso tipico è la riparazione di una strefolatura per mezzo del montaggio di un giunto

Caso 1

Si agisce nella tabella del quarto livello linea, per mezzo del menù di dettaglio, contestuale all’asset che deve essere sostituito, scegliendo la voce di menu “Sostituisci asset”. L’asset che viene sostituito viene messo nello stato DIST (Distrutto/Alienato alias di Decommissioned) e quindi, di default, non più visualizzato.

Caso 2

Si agisce nella tabella del terzo livello linea (quella che contiene gli aggregatori, ad esempio Funi di Guardia, Conduttori, Telaio e Attacchi Conduttore) per mezzo del menù di dettaglio, contestuale all’asset aggregatore sotto il quale deve essere montato, scegliendo la voce di menu “Crea asset figlio”.

Caso 3

Nel caso sia necessario creare asset di quarto livello, come ad esempio i giunti, si agirà sempre dall’interno di una linea, evidenziando nella tabella dei quarti livelli con quale asset dovrà legarsi (conduttore, fune di guardia, attacco conduttore o telaio). Dal menu di dettaglio dell’asset di relazione, si sceglie la funzione “Crea asset relazionato”.

Tutti gli asset di quarto livello che dal punto di vista tecnico sono funzionali ad un altro asset (ad esempio il giunto è funzionale all’asset conduttore su cui è montato), sarà obbligatorio popolare sulla classificazione l’attributo LIN_RIF_ASSET_COND oppure LIN_RIF_ASSET_FDG (per gli asset di 4° livello relativi ai sostegni, gli attributi sono LIN_RIF_ASSET_ARM_COND e LIN_RIF_ASSET_ARM_FDG) per poter salvare l’asset (quando questo è aggiunto manualmente, ad esempio in seguito alla riparazione di una strefolatura, e non quando è creato massivamente, ad esempio in fase di ETL). Gli asset di quarto livello che avranno questo attributo obbligatorio, quando aggiunti manualmente, sono tutti quelli che non possono esistere, se non montati su un altro asset di pari livello. Nello specifico, considerando i seguenti quattro elementi di terzo livello, seguono quelli di quarto:

LINEA / CAMPATA AEREA / CONDUTTORI /

SFERE DI SEGNALAZIONE CONDUTTORE

DISTANZIATORI

ANTIROTAZIONALI CONDUTTORE

DISTANZIATORI INTERFASE

GIUNTO CONDUTTORE

BARRETTA PREFORMATA CONDUTTORE

ANTIVIBRANTI CONDUTTORE

DISSUASORI AVIFAUNA CONDUTTORE

LINEA / CAMPATA AEREA / FUNI DI GUARDIA

FIBRA OTTICA FUNE DI GUARDIA

SFERE DI SEGNALAZIONE FUNE DI GUARDIA

DISSUASORI AVIFAUNA FUNE DI GUARDIA

ANTIROTAZIONALI FUNE DI GUARDIA

GIUNTO FUNE DI GUARDIA

BARRETTA PREFORMATA FUNE DI GUARDIA

ANTIVIBRANTI FUNE DI GUARDIA

LINEA \ SOSTEGNO AEREO \ ATTACCHI CONDUTTORE

ISOLATORE CONDUTTORE

LINEA \ SOSTEGNO AEREO \ TELAIO

ISOLATORE FUNE DI GUARDIA

Nel momento in cui il nuovo asset è creato, di qualunque tipologia sia, IBM MAS Manage inizia a tracciarne la storia; la prima transazione con la quale inizia la vita dell’asset è quella di CREATED e lo stato standard interno è quello di NOT READY, ossia DATT (da attivare).

### Creazione e manutenzione di classi e attributi

Le classificazioni permettono di qualificare gli asset a cui sono associate, popolando automaticamente la tabella delle specifiche tecniche utilizzate per definire dal punto di vista tecnico l’asset. Le classificazioni, con le relative specifiche associate, sono gestibili direttamente dagli utenti centrali di AM, come riportato nel § 3.4.

A livello di attributo, per classificazioni differenti, possono essere gestiti differenti domini; ad esempio, l’attributo Posizione, per un TA potrà essere legato al domino con valori da TA-1 a TA-4 mentre su una batteria di UPS potrà contenere i valori UPS-1 e UPS-2.

L’aggiunta o la rimozione di un attributo su una classificazione si propagherà su tutte le specifiche tecniche generate dalla classificazione oggetto della modifica.

### Cambio tracciato / variante con creazione di nuovi elementi

Nel caso debba essere gestito un cambio di tracciato che comporti la creazione di nuove campate e sostegni, si dovrà agire nell’applicazione di gestione linee:

Richiamare la linea che deve essere modificata;

Richiamare la funzione Inserisci Tratta (aerea/cavo) (vedi § 3.7.1);

Nel caso in cui la modifica comporti un mix di campate e tratte cavo verrà richiamata più volte la funzione di creazione, scegliendo di volta in volta l’elemento di aggancio e i modelli da utilizzare.

### Gestione del legame tra linee elettriche differenti

La necessità è quella di permettere il legame tra linee elettriche differenti. La richiesta copre sia le linee in cavo che le linee aere; per le aeree è gestito sia il caso della terna singola che quello della terna doppia.

Nelle immagini seguenti sono stati rappresentati tutti e soli i casi che verranno gestiti nello sviluppo di questa funzionalità.

Figura 23 - Terna Singola

Figura 24 - Doppia Terna (1/3)

Figura 25 - Doppia Terna (2/3)

Figura 26 - Doppia Terna (3/3)

Figura 27 - Casi linee in cavo

Figura 28 - Tutti i casi misti non devono essere permessi

La gestione di questo legame tra linee elettriche differenti sarà eseguita per mezzo di una specifica funzionalità del menu dell’applicazione delle linee, denominata "Crea legame altra linea", con il seguente comportamento:

La funzionalità andrà utilizzata dalla linea (quella verde) che diventerà la prosecuzione della linea verso cui si costruirà il legame (quella rossa)

Il legame verrà costituito sempre e soltanto verso il primo sostegno o estremo cavo della linea (quello verde con il numero di sequenza minore)

Nella box di dialogo che si apre l’utente visualizzerà tutti i sostegni o tutti i terminali definiti in EAM (coerentemente con la linea dalla quale si sta agendo, quindi sostegni se inizia con un sostegno o l’ultimo terminale se inizia con un terminale), tranne quelli della linea da cui si sta agendo, filtrabili per ASSETNUM, Descrizione e SAPID; chiaramente per le situazioni di doppia terna, l’ ASSETNUM del sostegno sarà ripetuto 2 volte, in relazione ai due SAPID differenti.

La nuova funzionalità creerà, a seconda del caso, una nuova campata o una nuova tratta per la linea verde, con l’attributo LIN_SOSTEGNO_INIZIO oppure LIN_ESTREMO_INIZIO popolato con il sostegno o il terminale della linea rossa selezionato nella precedente box di dialogo. Per LIN_SOSTEGNO_INIZIO deve essere gestita anche la SECTION, popolata con il SAPID della linea da cui si sta agendo. (ATTENZIONE: usare il SAPID per creare i legami obbliga a rivedere tutte le procedura che coinvolgono la rinomina del SAPID)

Di seguito vengono trattati i casi in cui, per le linee aeree, si stia agendo in presenza di doppie terne. La doppia terna potrebbe essere presente sia nella linea di partenza che in quella di arrivo oppure soltanto su una delle due linee, ossia linee singole che confluiscono in doppia terna oppure linee che dalla doppia terna si scindono in due terne singole.

In caso di sostegno in doppia terna, (per la linea verde), verso un sostegno in doppia terna (linea rossa), l’operazione "Crea legame altra linea" dovrà essere eseguita 2 volte, sia dalla linea verde C che dalla linea verde D (casi 1 e 4, rispettivamente, di Figura 25 - Doppia Terna (2/3)), con le seguenti regole:

La prima esecuzione creerà la campata di 2°L, il conduttori di 3°L e i 3 conduttore di 4°L, con LIN_ESTREMO_INIZIO sulla campata popolato con il SAPID del sostegno dell’altra linea (rossa A) e con la SECTION coerentemente popolata (ossia con il SAPID della verde C)

La seconda esecuzione creerà solo il conduttori di 3°L e i 3 conduttore di 4°L e aggiungerà il secondo attributo LIN_ESTREMO_INIZIO sulla campata creata al punto precedente, popolato con il SAPID del sostegno dell’altra linea (rossa B) e con la SECTION coerentemente popolata (ossia con il SAPID della verde D)

Una variante di quando si agisce da una linea in doppia terna è presentata nel caso 2 di Figura 24 - Doppia Terna (1/3), in cui due linee in terna singola confluiscono entrambe sulla medesima linea in doppia terna, per mezzo di due campate in singola terna.

La prima esecuzione creerà la campata di 2°L, il conduttori di 3°L e i 3 conduttore di 4°L, con LIN_ESTREMO_INIZIO sulla campata popolato con il SAPID del sostegno dell’altra linea (azzurra A) e con la SECTION coerentemente popolata (ossia con il SAPID della verde da cui si sta agendo, ad esempio la C)

La seconda esecuzione creerà la campata di 2°L, il conduttori di 3°L e i 3 conduttore di 4°L, con LIN_ESTREMO_INIZIO sulla campata popolato con il SAPID del sostegno dell’altra linea (rossa B) e con la SECTION coerentemente popolata (ossia con il SAPID della verde da cui si sta agendo, ad esempio la D)

Nel caso da una doppia terna si debba unire la linea verde o azzurra, Figura 26 - Doppia Terna (3/3), in terna singola, la funzione "Crea legame altra linea" verrà usata una volta sola e agirà così:

creazione della campata di 2°L, il conduttori di 3°L e i 3 conduttore di 4°L, con LIN_ESTREMO_INIZIO sulla campata popolato con il SAPID del sostegno dell’altra linea (rossa A oppure B) e con la SECTION coerentemente popolata (ossia con il SAPID della verde C o della linea azzurra D)

Chiaramente, nel caso debba essere creato solo il legame per la verde o l’azzurra (esempio 5), si agirà una volta sola, dalla linea interessata.

In tutti i casi gestiti, la sequenza della linea che prolunga, verde o azzurra, verrà automaticamente aggiornata (X3SEQUENCE) per assorbire la nuova campata o la nuovo tratta di unione (nel caso in cui non sia “già libero” il numero da utilizzare); è escluso invece che la linea verso cui si costituisce il legame vengo aggiornata in base alle sequenza della line che la precede.

Si evidenzia come, per la gestione di questi casi, verranno aggiunti i necessari attributi, come di seguito riportato:

Aggiunta di un nuovo attributo sulla classificazione CAMPATA AEREA, denominato LIN_SOSTEGNO_INIZIO. Per la doppia terna dovranno essercene 2, con la SECTION popolata con il SAPID della linea da cui è richiamata la funzione "Crea legame altra linea"

Aggiunta di un nuovo attributo sulla classificazione TRATTA CAVO, denominato LIN_ESTREMO_INIZIO.

Implementazione di una nuova funzionalità nel menu della linea, denominata "Crea legame altra linea", con il seguente comportamento:

BONIFICHE:

Per gli eventuali asset di congiunzione, già presenti a sistema, con un elenco fornito dal business, si agirà popolando massivamente l’attributo LIN_SOSTEGNO_INIZIO o LIN_ ESTREMO _INIZIO per tutti i casi di legame interlinea (e in coerenza, dovranno essere popolate anche le rispettive SECTION).

CONNETTORE:

Se il connettore trova l'attributo LIN_SOSTEGNO_INIZIO o LIN_ ESTREMO _INIZIO valorizzato lo riporta a SAP, in caso contrario, esegue il suo calcolo.

### Creazione Split su linea aerea

Lo Split consente di frazionare una linea aerea trifase (composta da tre conduttori) su più sostegni equipaggiati da 1 o da due conduttori

Nella pratica operativa, una campata può essere rappresentata e gestita in diversi modi:

Trifase: Campata con tutti e tre i conduttori.

Monofase: Campata con un solo conduttore;

Bifase: Campata composta da due conduttori;

La funzionalità di split, presente in EAM con il nome di “Crea split linea”, consente di frazionare una tratta aerea trifase in tratte aeree monofase e/o bifase; ad esempio, è possibile dividere una linea trifase in due tratte (una monofase e una bifase) oppure in tre tratte monofase, ciascuna con un singolo conduttore.

La tipologia di split da creare sulla linea dipende dal popolamento del campo “Numerosità sostegni di split”, come di seguito spiegato:

con numerosità 1 è possibile selezionare soltanto asset già splittati;

con numerosità 2 Viene generata una campata monofase e una campata bifase, afferenti ad altrettanti sostegni;

con numerosità 3 vengono generate 3 campate monofase, afferenti ad altrettanti sostegni.

Figura 29- Dialog “Crea Split”

In base alla numerosità scelta, il sistema aggiorna automaticamente i valori di sequenza e posnr, come di seguito:

Sequenza

Con split bifase, i suffissi della sequenza diventano -01 per la tratta monofase e -23 per la tratta bifase.

Con split monofase suffissi sono -01, -02 e -03, corrispondenti alle tre tratte monofase.

POSNR

Il posnr viene incrementato progressivamente per ogni nuovo asset creato (campata o sostegno).

Nel caso di split monofase, al campo POSNR viene aggiunto un suffisso numerico per distinguere le campate derivate:

il suffisso “1” identifica la prima campata monofase generata;

il suffisso “2” identifica la seconda campata monofase generata;

La campata prima campata mantiene invece il POSNR senza suffisso.

Figura 30 Posnr split Monofase

Nel caso di split Bifase, al campo POSNR viene aggiunto come suffisso numerico “1” per indicare la campata Bifase o rimane senza suffisso nel caso di campata Monofase.

Figura 31 Posnr Split Bifase

Se dopo lo split sono presenti o vengono aggiunti ulteriori asset nella linea, il sistema ricalcola automaticamente sia le sequenze sia i POSNR di tutti gli elementi a valle, così da preservare la continuità e garantire un ordinamento coerente.

Figura 32 Esempio Linea con Split Bifase

#### Split su derivazione

Lo split deve consentire la selezione non solo di asset appartenenti alla dorsale, ma anche di asset di derivazione (aerea), che devono rispettare le stesse logiche di generazione descritte in precedenza tramite l’apposita funzionalità di “Crea split”. In questo caso, però, cambiano le regole di aggiornamento di SEQUENCE e POSNR:

La SEQUENCE deve mantenere come prefisso la stessa lettera utilizzata nella sequenza della derivazione di origine. Esempio: A00025-01 per la tratta monofase, A00025-23 per la bifase.

Il POSNR deve conservare la lettera della derivazione e aggiungere come ultima cifra 1 o 2, a seconda della tipologia di split. Esempio: se il POSNR della derivazione è A024, in caso di split diventerà A241, tenendo in considerazioni le attuali logiche previste in EAM per la derivazione.

## Aggiornamento

### Popolamento e modifica di attributi

Nel corso della vita operativa di un asset potrà essere necessario modificare il valore degli attributi che lo qualificano. Lo scenario d’uso più semplice è quello in cui si agisce modificando direttamente il valore presente a sistema. Ad esempio, in seguito ad una attività manutentiva, il numero delle sfere di segnalazione sulla fune di guardia di una determinata campata, viene portato da 3 a 9; di seguito gli step operativi da effettuare:

Richiamare l’asset rappresentativo dell’insieme delle sfere di segnalazione, associato alla fune di guardia oggetto dell’attività (il codice della fune di guardia sarà gestito nell’attributo LIN_RIF_ASSET_FDG della classificazione SFERE DI SEGNALAZIONE FUNE DI GUARDIA);

Modificare il valore dell’attributo LIN_NUM_OGGETTI, con l’effettivo numero di sfere installate

Nel momento in cui viene inserito un nuovo asset, a meno degli attributi che presentano dei valori di default o di eventuali regole di compilazione automatica (vedi § 3.12.1), i valori dovranno essere imputati manualmente, a rappresentazione dell’effettiva realtà operativa dell’oggetto creato.

### Doppia terna “ammazzettata”

Con il termine ammazzettamento, impiegato nel lessico operativo di Terna, si indica la procedura che porta ad avere tutti e sei i conduttori di una campata in doppia terna elettricamente collegati ad un'unica linea, ossia accoppiati a due a due. Lo si fa per avere sempre tutti i conduttori attraversati dalla corrente: questo permette un maggiore controllo dello stato fisico della linea.

Nell’immagine seguente, al portale CMM sono collegati tre conduttori (rosso alla fase 8, verde alla fase 12 e giallo alla fase 4). Sul sostegno P.001/1-P.004/1, utilizzando il formalismo di distribuzione delle fasi (vedi § 3.13), l’attributo preposto a contenere il dato (LIN_DISPOSIZIONE_FASI) sarà così popolato: H-1,3,2,2,3,1. La sequenza numerica precedente presuppone che il verso della linea sia CMM à P.001/1 – P.004/1.

Figura 33 - Rappresentazione in Mandi di terna doppia “ammazzettata”

La medesima informazione, partendo dal “palo gatto” BAT à P.001-P.015 è H-2,3,1,1,3,2.

Quindi, dal punto di vista informatico, il dato della distribuzione delle fasi avrà tutte e sei le posizioni compilate per la medesima linea solo quando questa risulta composta da due conduttori per campata (attenzione, conduttori singoli di fase, indipendentemente al fatto che siano binati o trinati).

Poiché l’ammazzettamento nasce sempre in seno ad una doppia terna (ad esempio, linea A e linea B, con linea B che viene dismessa), a sistema, le fasi operative saranno le seguenti:

Disassociare campate e sostegni della linea che viene dismessa (nell’esempio linea B, in relazione alla quale si perderanno le informazioni, impostate a valore nullo, dagli asset di 2° e 3° livello, di ID SAP, LIN_DISPOSIZIONE_FASI, X3POSNR e X3SEQUENCE) tramite la funzionalità Ammazzettamento funzione spiegata in § 3.9.1.2;

Sui sostegni associati alla linea A andrà cambiata la sequenza numerica della distribuzione fasi, coerentemente a quanto spiegato in questo paragrafo nel secondo capoverso (tutte e sei le posizioni compilate)

### Incrocio – utilizzo di un sostegno a sei mensole

L’incrocio è una condizione della linea e si ha quando il tracciato di una linea incrocia il tracciato di un'altra linea su un solo sostegno comune a entrambe (è un caso abbastanza raro, ma comunque reale, quindi da gestire) e poi proseguono tutte e due in singola terna.

Per impostare in doppia terna un singolo sostegno si agirà come previsto in § 3.7.2, relativamente alla doppia terna, indicando il solo sostegno interessato.

Lo scenario dell’unico sostegno in doppia terna ha dato origine alla necessità di una bonifica manuale (vedi § 3.8.4) in IBM MAS Manage, per evitare la gestione di sostegni gemelli (presenti in SAP) quando nella realtà ne esiste soltanto uno. La necessità di bonifica lato EAM nasce dall’assenza di un attributo che lato SAP possa consentire l’identificazione di queste casistiche per una corretta migrazione lato EAM; pertanto, in fase di migrazione anche in IBM saranno creati due sostegni. Per le nuove anagrafiche e quindi create ex-novo su IBM si procederà con la creazione di un unico sostegno comune, il connettore invece dovrà creare il sostegno gemello in SAP.

### Bonifica dei doppi sostegni irrisolti dalla fase di ETL

È il caso del sostegno che supporta le terne di due linee differenti (cosiddetta rappresentazione a X), vedi immagine seguente.

Figura 34 - incrocio di due linee su sostegno singolo

Non possono essere bonificati in fase di ETL da SAP per la mancanza di informazioni utili all’individuazione.

In pratica non si riescono ad individuare i sostegni che non hanno il dato campata successiva popolato; quindi, non è identificabile la terna associata. Lo stesso caso si verifica anche per il passaggio aereo – cavo, in cui in SAP esisteranno due sostegni gemelli, quando nella realtà ne esiste uno solo.

Per risolverlo si deve agire direttamente in IBM MAS Manage, in base a un elenco di casi individuati dal territorio, per aggiornare i dati, cancellando il sostegno fittizio e associando il sostegno rimanente anche all'altra linea, per mezzo della funziona già vista in § 3.7.2.1 relativamente alla gestione in doppia terna. In seguito a questa modifica in IBM MAS Manage, il connettore dovrà trattare questo caso in modo specifico.

### Gestione degli stati

Ai fini dei ragionamenti formulati in questo documento, è stato utile distinguere gli stati in due categorie: quelli di tipologia DATT e quelli di tipologia ESER. I primi sono indicati nella tabella seguente con la lettera D e i secondi con la lettera E.

In generale, indipendentemente dalla tabella sottostante, EAM gestisce obbligatoriamente lo stato sugli asset e necessita di una stato di default alla creazione. Questo stato è il DATT, indipendentemente dal fatto che venga visualizzato o nascosto con gli xxxx.

In grassetto e in inglese sono anche indicati gli stati base di IBM MAS Manage ai quali verranno associati, creandoli come valori alias degli stessi.

Per tutti gli asset di 4° livello di Linee e Stazioni è possibile abilitare la gestione in GUI dello stato per ciascuna classificazione tramite l’attributo GESTSTAT (booleano). In particolare, valorizzando l’attributo a “SI” vengono abilitate la visualizzazione e la modifica dello stato dell’asset tramite GUI, se invece viene valorizzato a “NO” vengono disabilitate la visualizzazione e la modifica dello stato (tranne per il passaggio di stato a DIST e DELE che rimangono sempre disponibili). L’attributo GESTSTAT sarà impostato di default a “SI” per tutti gli asset di quarto livello di Linee e stazioni; Inoltre, gli utenti CENTRO 1 potranno modificare liberamente il valore di GESTSTAT per ogni classificazione all’interno della relativa applicazione “Classificazione”.

|  |  |  |  |  | LINEE | LINEE | LINEE | LINEE | STAZIONI | STAZIONI | STAZIONI | STAZIONI |  |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Stato Base | Gruppo stati | Sequenza applica | Stato | Descrizione | Liv.1 | Liv.2 | Liv.3 | Liv.4 | Liv.1 | Liv.2 | Liv.3 | Liv.4 | Note per il terzo e quarto livello delle STAZIONI |
| Not ready | D | 4 | DATT | Da Attivare | X | X |  |  | X |  | X |  | Solo per alcuni, dipendenti dalla classificazione del Secondo livello |
| Not ready | E |  | INAT | Inattivo | X |  |  |  | X |  |  |  | In questo stato l’asset è in sola lettura. Non è uno stato impostabile dal Manage |
| Operating | D | 1 | ATTV | Attiva | X | X |  |  | X |  | X |  | Solo per alcuni, dipendenti dalla classificazione5 del Secondo livello |
| Operating | D | 2 | NADI | Non Attiva, Disponibile | X | X |  |  | X |  | X |  | Solo per alcuni, dipendenti dalla classificazione5 del Secondo livello |
| Operating | D | 3 | NAND | Non Attiva, Non Disponibile | X | X |  |  | X |  | X |  | Solo per alcuni, dipendenti dalla classificazione5 del Secondo livello |
| Operating | E |  | ESER | Installato In Esercizio |  |  |  | X |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Operating | E |  | EFFE | Installato Efficiente Futuro Esercizio |  |  |  | X |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Operating | E |  | EFDI | Installato Efficiente Disponibile |  |  |  |  |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Operating | E |  | EFPR | Installato Efficiente Prenotato |  |  |  |  |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Operating | E |  | EFSC | Installato Efficiente Scorta |  |  |  |  |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Operating | E |  | GUAS | Installato Guasto |  |  |  | X |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Operating | E |  | NIED | Non Installato Efficiente Disponibile |  |  |  |  |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Operating | E |  | NIEP | Non Installato Efficiente Prenotato |  |  |  |  |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Operating | E |  | NIES | Non Installato Efficiente Scorta |  |  |  |  |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Operating | E |  | NIEO | Non Installato Efficiente Obsoleto |  |  |  |  |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Operating | E |  | NIGU | Non Installato Guasto |  |  |  |  |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Operating | E |  | RIPR | In Riparazione/Revisione |  |  |  | X |  |  |  | X | Solo per gli asset con l'attributo GESTSTAT |
| Decommissioned | D |  | CEAL | Ceduta / Alienata | X | X |  |  | X |  |  |  |  |
| Decommissioned | D |  | DEMO | Demolita / Dismessa | X | X |  |  | X |  |  |  |  |
| Decommissioned | E |  | DIST | Distrutto/Alienato |  |  |  | X |  |  |  | X | Sempre previsto sul 4° livello, indipendentemente dal GESTSTAT |
| Decommissioned | E |  | DELE | Cancellato | X |  |  | X | X |  |  | X | Sempre previsto sul 4° livello, indipendentemente dal GESTSTAT |

Figura 35 - Stati gestiti

Per le linee, al quarto livello, gli stati di tipo ESER da utilizzare sono solo quelli segnati con X nella rispettiva colonna, mentre per le stazioni, sempre relativamente agli asset di quarto livello, sono utilizzabili tutti quelli presenti in tabella (marcati con X nella colonna del quarto livello).

Lo scopo dello stato INAT, Inattivo, è quello di permettere la visualizzazione in IBM MAS Manage, dei primi livelli di asset, quando questi sono creati in SAP e hanno uno stato per il quale la gestione rimane in carico a SAP. Questi stati SAP sono PROGE, CANTI e DATT (questo DATT è da non confondere con quello gestito in IBM MAS Manage e che indica il momento di passaggio dalla gestione SAP a quella di IBM MAS Manage). L’asset di livello uno, fino a quando rimane nello stato INAT è in sola lettura, quindi non modificabile da IBM MAS Manage.

Le regole base a cui saranno sottoposti gli stati alias sono mutuate dalle regole dei tre stati standard da cui gli alias stessi derivano, ossia:

NOT READY: è lo stato di default per i nuovi asset. Le eventuali regole di manutenzione preventiva (standard IBM MAS Manage, non relativo al DSS) non faranno generare ordini di lavoro sino a quando lo stato non passa a operating. In ogni caso un asset in questo stato può essere oggetto di ordini di lavoro;

OPERATING: indica che l’asset può essere utilizzato e tutte le funzionalità standard di gestione della manutenzione possono essere applicate;

DECOMMISSIONED: è utilizzato per rappresentare asset non più in uso. Gli asset in questo stato non vengono più mostrati nelle liste di selezione e quindi non possono essere utilizzati come oggetto di ordini di lavoro, richieste e manutenzioni preventive. Rimane comunque attiva la possibilità di spostarli (associarli a differenti asset, modificandone il padre);

In IBM MAS Manage i nuovi valori di default per ciascuno stato saranno i seguenti:

Not Ready, alla creazione, verrà sostituito da DATT – Da attivare;

Operating avrà come default ATTIV – Attiva;

Decommissioned avrà come default DEMO – Demolita / Dismessa;

Come rappresentato nella precedente tabella, sugli asset di terzo livello delle linee (che hanno mera funzione di aggregatori logici) e sugli asset di secondo livello delle stazioni (che hanno lo scopo di raggruppatori funzionali, come le Conversioni, i Rifasamenti, le Sezioni, ecc.), non è prevista la gestione puntuale dello stato; poiché lo stato non potrà essere vuoto, il campo conterrà comunque un valore, ma non sarà modificabile dagli utenti (in fase di Low Level Design potrà essere valutato se nascondere il campo).

Gli asset di quarto livello, che sono demoliti, vengono messi nello stato di DIST Distrutto/Alienato e di default non visualizzati, a meno che non attivo un filtro apposito.

OP_258_W1 Cambio stato massivo

Al fine di consentire l’aggiornamento massivo dello stato degli asset di Linee e Stazione, oggi possibile solo a livello singolo asset tramite l’apposita funzionalità “Cambio Stato”, è stata richiesta l’implementazione di una nuova funzione di cambio stato massivo per l’anagrafica. In particolare, essa consentirà di attivare più asset attualmente in stato DATT, stato che impedisce la corretta generazione di policy e attività sui nuovi asset.

La nuova funzionalità sarà disponibile all’interno delle applicazioni “Linee” e “Stazioni” nella sezione “Altre azioni” e dovrà fare quanto segue:

Visualizzare una finestra di dialogo con l’elenco degli asset in stato DATT per la sede tecnica selezionata, consentendo la selezione multipla tramite checkbox;

In base alla tipologia degli asset selezionati lo stato verrà modificato rispettando il controllo di coerenza definiti in Anagrafica W1:

2° livelli (Linee) 3° livelli (Stazione) lo stato verrà modificato da DATT ad ATTV

4° livelli (Linee/Stazioni) lo stato verrà modificato da DATT a ESER

### Funzioni di aggiornamento massivo per la gestione delle linee

In questo paragrafo sono descritte le funzioni di manipolazione massiva dei dati per permettere la modifica delle linee, senza la necessità di agire sui singoli asset impattati. Si suppone perciò di agire sempre su asset già associati a linee esistenti e non su asset “solitari”. Le funzioni riguardano solo gli asset di secondo livello delle linee, ossia sostegni, campate, tratte cavo e terminali cavo. In ogni caso l’azione su asset singoli rimane sempre possibile e sarà cura dell’utente mantenere la coerenza delle specifiche informazioni.

Una delle informazioni fondamentali nella gestione di una linea è la sequenza con la quale si susseguono le quattro tipologie di asset di secondo livello che la costituiscono (la sequenza è rappresentativa dei legami per ricavare i collegamenti di continuità elettrica); persa questa informazione la linea di fatto non esiste più. Poiché la sequenza di cui sopra è gestita solo nella relazione tra la linea (asset di primo livello) e gli asset di secondo livello, eliminando la relazione, viene persa la sequenza.

#### Associazione massiva ad una linea di asset di secondo livello (palificazione esistente)

La necessità potrebbe aversi quando, in seguito alla dismissione di una linea, parte dei suoi sostegni e campate sono utilizzati per prolungarne un’altra. A sistema il caso d’uso si comporrà dei seguenti step:

Dall’applicazione di gestione delle linee richiamo la linea alla quale “conferire” gli asset esistenti di secondo livello;

Richiamo la funzione di Riassociazione Asset di Secondo livello

Nella maschera di dialogo, analoga a quella della doppia terna vista in § 3.7.2, filtro e scelgo gli asset da associare che appartengono a una linea esistente;

Se nella linea selezionata al punto iniziale esistono già elementi di secondo livello, sarà possibile indicare la sequenza del sostegno di aggancio della nuova tratta, secondo una delle seguenti due modalità:

Scelgo il primo o l’ultimo elemento tra quelli esistenti e il primo o l’ultimo tra quelli da aggiungere: i nuovi asset sono agganciati, rispettivamente, prima o dopo e la sequenza viene interamente ricalcolata, rispettando comunque l’andamento precedente.

Scelgo un elemento intermedio tra quelli esistenti e il primo o l’ultimo tra quelli da aggiungere: i nuovi asset sono agganciati interponendosi tra quello scelto esistente e il suo successivo. L’intera sequenza successiva all’asset intermedio viene ricalcolata, rispettando comunque l’andamento precedente.

Questa funzione, contestualmente all’associazione con la nuova linea, eseguirà l’aggiornamento dei codici descrittivi ID SAP nella porzione relativa al primo livello e la cancellazione dell’associazione dalla linea precedente degli elementi associati con la nuova linea. Chiaramente, non trattandosi di doppia terna, non eseguirà la creazione dei nuovi aggregatori di terzo livello e rispettivi figli.

#### Rimozione massiva da una linea di asset di secondo livello in doppia Terna

Questo caso d’uso si applica, ad esempio, in presenza di una doppia terna, quando una delle due linee viene disattivata (vedi § 3.8.2).Il flusso operativo si articola nei seguenti passaggi:

Dall’applicazione di gestione delle linee richiamo la linea dalla quale rimuovere gli asset di secondo livello;

Se la rimozione è stata eseguita su campate e sostegni in doppia terna, in modo automatico, i dati interni, utilizzati per gestire la corretta visibilità degli aggregatori di terzo livello, conduttore e attacco conduttore, verranno aggiornati coerentemente alla linea di doppia terna rimanente, ossia:

Sulle campate che precedentemente erano associate alla linea B (quella rimossa), l’aggregatore conduttori, con tutto il suo contenuto, deve entrare a fare parte della linea A;

Sui sostegni che precedentemente erano associate alla linea B (quella rimossa), l’aggregatore attacchi conduttore, con tutto il suo contenuto, deve entrare a fare parte della linea A e il suo attributo LIN_DISPOSIZIONE_FASI viene impostato a NULL;

#### Gestione della sequenza di una linea

Tutte le operazioni di aggiunta, incorporazione, eliminazione e segmentazione relative a elementi di secondo livello di una linea comportano l’aggiornamento, più o meno esteso (sia in funzione degli elementi coinvolti che di quelli precedenti o successivi al punto di innesto della modifica) della sequenza. L’informazione di sequenza è gestita per mezzo dei campi X3POSNR e X3SEQUENCE, entrambe gestiti sull’oggetto (tabella fisica di database) ASSETLOCRELATION.

Con il termine POSNR si indica il campo utilizzato in SAP sulla base del quale viene ordinato l’elenco degli asset (sedi tecniche figlie, sempre in SAP) di secondo livello che costituiscono una linea. Poiché il campo è di soli 4 caratteri, ed in SAP è utilizzato per ordinare solo campate e tratte cavo, non è stato possibile estenderne l’utilizzo anche a IBM MAS Manage, dove gli asset da gestire al secondo livello raddoppiano, con l’aggiunta di sostegni e terminali cavo. Per questo motivo, in IBM MAS Manage, sugli elementi di linea campate e tratte cavo verranno gestiti sia il campo X3POSNR che il campo X3SEQUENCE, mentre sugli elementi sostegni e terminali cavo verrà gestito solo il campo X3SEQUENCE.

I casi d’uso per i quali la ricodifica (da non confondere con la rinomina, trattata in § 3.19 e relativa al codice X3SAPID) si rende necessaria sono quello dell’inversione di una linea (vedi § 3.18.1) e la necessità di “farsi spazio” tra una campata e un sostegno (vedi § 3.18.2), per poter inserire altri asset di secondo livello. In entrambe i casi, per il dettaglio e l’operatività si rimanda ai due paragrafi indicati.

### Aggiornamento massivo generico

La strategia per l’aggiornamento massivo generico delle specifiche tecniche, tramite lo strumento MxLoader, file NoKEY_MxLoaderAggiornaValoriAttributiClassificazione_v3.xlsm, (attualmente abilitato agli utenti del COAM e Centro 1) si compone delle seguenti 3 fasi:

estrazione in Excel dei dati di interesse, tramite filtro sul campo ASSETNUM

modifica manuale dei valori sul file excel

upload dei dati modificati;

Tale strategia di aggiornamento massivo dei dati garantisce il rispetto delle regole interne del sistema, (es. validità dei dati nel formato e nella tipologia previsti da Maximo, coerenza dei valori inseriti in un dominio, valori duplicati).

Attenzione, questa modalità può essere utilizzata per i casi in cui debbano essere gestite qualche migliaio di righe; per le decine di migliaia e oltre ogni caso deve essere analizzato nello specifico.

## Interrogazione

### Visualizzazione tratte di tesatura

Nell’applicazione per la gestione delle linee elettriche dovrà essere possibile rintracciare (visualizzare) tutti i sostegni e tutte le campate coinvolte dalla medesima tratta di tesatura.

La regola per individuare la tratta di tesatura si basa sulla posizione degli amarri (quello immediatamente precedente e quello successivo) rispetto al conduttore o alla fune di guardia a questi ormeggiata.

Si ha la presenza di un amarro quando è popolato l’attributo LIN_TIPO_ATTACCO con il valore O ossia Ormeggio, appartenente alla gerarchia di asset:

LINEA / SOSTEGNO AEREO / ATTACCHI CONDUTTORE / ARMAMAENTO CONDUTTORE

L’armamento conduttore è un asset con l’indicazione della fase, quindi ne esisteranno tre per ogni linea.

Un ragionamento analogo viene fatto per le tratte di tesatura delle funi di guardia, per le quali cambia il paradigma di relazioni per individuare la presenza dell’attributo amarro, come segue:

LINEA / SOSTEGNO AEREO / TELAIO / ARMAMAENTO FUNE DI GUARDIA

L’amarro è gestito nell’attributo LIN_TIPO_ATTACCO dell’armamento della fune di guardia, quando valorizzato con la lettera O; gli armamenti saranno tanti quante sono le funi di guardia (da 1 a 3, identificate, quando 2, con destra e sinistra, quando 3, si aggiunge centrale. Vedi in proposito l’ultimo punto di § 3.14).

L’individuazione della tratta di tesatura è basilare per il corretto dimensionamento di un’attività di sostituzione di un cavo aereo, comprendendo in quanti asset distinti (differenziati uno per campata), è stato segmentato il medesimo cavo.

L’operatività a sistema sarà la seguente:

Richiamo nell’applicazione di gestione delle linee della linea d’interesse;

Selezione, nella tabella degli elementi di quarto livello, dell’asset conduttore o fune di guardia per la quale deve essere eseguita la ricerca;

Cliccando sul menu accanto all’asset conduttore o fune di guardia, sarà disponibile la funzionalità Visualizza Tratta di Tesatura. Selezionandola, si aprirà una finestra di dialogo in cui verranno mostrati gli altri asset dello stesso tipo (e della stessa fase o posizione, nel caso delle FdG) appartenenti al medesimo tratto di tesatura.

### Visualizzazione di tutte le linee relazionate con la palificazione di una linea specifica

Nell’applicazione di gestione delle linee elettriche verrà realizzata una scheda (Linee in relazione) per mezzo della quale, in una tabella, sarà visualizzato, non modificabile, l'elenco di tutte le linee che hanno campate associate alla linea richiamata o condividono anche un unico sostegno (incrocio).

L’immagine seguente fornisce un esempio di quanto spiegato; la linea 9101 – linea da A a B condivide parte della palificazione della linea 8836 anch’essa congiungente A con B (chiaramente si tratta di due linee fittizie, costruite e intersecate a scopo dimostrativo nell’ambient di POC).

Figura 36 - Linee in relazione

### Gestione e visualizzazione dei dati storici

In IBM MAS Manage l’inserimento e la modifica di alcuni dati deve automaticamente originare una stratificazione storica di informazioni, interrogabile in modo regolato. L’interrogabilità dei dati storici deve permettere una visualizzazione organizzata delle informazioni, garantendo all’utente che le visualizza la comprensione dell’evoluzione del dato nel tempo.

Le categorie di informazioni che devono essere sottoposte a storicizzazione sono le seguenti:

Classificazioni, attributi associati e valori degli stessi, tutto in relazione a una data di riferimento;

Modifiche di stato degli asset;

Modifiche relative al rapporto di gerarchia verso il padre. Nelle stazioni è utilizzato per tracciare gli spostamenti all’interno della medesima stazione e tra stazioni differenti;

Modifiche relative alle relazioni. Nelle linee è utilizzato per verificare la composizione di una linea ad una determinata data o l’appartenenza di un asset di secondo livello ad una linea;

Modifiche relative all’ID descrittivo SAP (vedi § 3.16 e § 3.19), dove questo è gestito;

Modifiche riguardanti la distribuzione delle fasi sul sostegno;

Per le linee, modifiche relative al POSNR (sia X3POSNR che X3SEQUENCE)

Di seguito sono riportati dettagli e specificazioni delle quali si dovrà tenere conto in fase di low level design e di implementazione:

Le modifiche degli attributi devono poter essere eseguite indicando anche la data dalla quale il dato dell'attributo è da considerarsi presente, con valenza sullo storico delle modifiche dei valori degli attributi;

Possibilità di creare asset con la data di creazione antecedente rispetto a quella di sistema;

Possibilità di indicare una data di spostamento antecedente rispetto alla data di sistema (solo per gli asset di Stazione)

Per avere un’idea della possibilità di indagine nella variazione degli attributi si presentano le due seguenti immagini, nelle quali la data inserita come criterio di ricerca (campo Al) è prima il 15/06/23 e poi il 16/06/23,

Figura 37 - Ricerca storico delle variazioni (1)

Figura 38 - Ricerca storico delle variazioni (2)

Nell’immagine seguente si vede la storia degli spostamenti eseguiti sull’asset 3926 – SEZIONATORE AIS.

Figura 39 - Storico spostamenti di un asset

## Dati non modificabili da IBM MAS Manage

In questo capitolo vengono censiti tutti i dati, siano campi fisici di database, strutture complesse o attributi di classificazione, che non dovranno poter’ essere creati o modificati dall’applicativo IBM MAS Manage.

Gli asset di primo livello rappresentativi delle linee o delle stazioni non possono essere creati in IBM MAS Manage dagli utenti, ma solo per mezzo del connettore con SAP. Lo stato iniziale con cui sono creati è il DATT (per gli stati, vedi § 3.8.5); da questo momento in poi lo stato verrà aggiornato da IBM MAS Manage.

I seguenti campi non devono poter essere né inseriti né modificati dagli utenti del sistema:

I dati contabili riferiti agli asset (Centro di Costo);

Il campo X3SWERK utilizzato per gestire l’informazione della DT sugli asset di primo livello;

Il campo X3SAPID che conterrà il dato SAP STRNO (solo con apposita funzione);

Il campo X3POSNR (solo come conseguenza di azione con apposita funzione);

Il campo X3SEQUENCE (solo come conseguenza di azione con apposita funzione);

## Casi d’uso relativi a requisiti puntuali

Con il termine requisiti puntuali si intendono l'insieme di tutte le richieste / specificità viste e analizzate con lo scopo di assolvere ad una necessità specifica; chiaramente la risposta nel suo complesso ha indirizzato un disegno organico della soluzione. In questo capitolo sono riportati, partendo dal caso d'uso specifico, tutti i dettagli di design di alto livello necessari e funzionali alla fase di implementazione.

### Gestione e calcolo degli attributi delle specifiche tecniche

La gestione di alcuni attributi delle specifiche tecniche degli asset (gestiti per mezzo delle classificazioni), necessitano di regole di funzionamento più estese rispetto a quanto previsto dallo standard di sistema. In questo capitolo e nei paragrafi contenuti sono riportate, raggruppate per tipologia, tutte le necessità evidenziate analizzando i relativi casi d’uso.

Per permettere il calcolo del totale chilometri di cavo posati (vedi § 3.12.3), distinto per LIN_TIPO_POSA (terrestre o marino) dovrà essere gestito questo nuovo attributo, sia sugli asset di secondo che quelli di terzo livello.

Tutte le formule utilizzate per i calcoli o le visualizzazioni aggregate devono considerare sempre lo stato dell’asset: quelli in stati alias di decommissioned (CEAL, DEMO, DIST), non devono più essere considerati per i conteggi.

### Gestione di regole di editabilità degli attributi, indipendenti dalla classificazione

Per alcuni attributi, indipendentemente dalla classificazione nella quale sono utilizzati, deve essere possibile indicare il gruppo di persone che potrà gestirne (inserire, cancellare o modificare) la valorizzazione (vedi anche § 3.4).

Questo caso d’uso deriva dalla necessità di rendere disponibili a tutti le informazioni tecniche relative agli asset, ma per garantirne veridicità e coerenza, solo determinati gruppi, rappresentativi di specifiche competenze, avranno la possibilità di gestirli.

L’operatività a sistema prevede che sugli attributi dove questo sia necessario, dovrà essere popolato il campo gruppo responsabile; se il campo sarà vuoto, il dato potrà essere modificato da chiunque (fatte salve le regole generali di editabilità, costruite sulla base della UI di appartenenza, vedi § 3.5).

### Visualizzazione aggregata sul 1° livello di attributi relativi al 2° livello

La necessità espressa è quella di mostrare sulla linea un set di informazioni ottenute aggregando il contenuto di differenti attributi presenti sugli elementi di secondo livello oppure contando il numero degli elementi di secondo livello stessi. Di seguito il dettaglio di fonti e regole:

Tabella con tutte le unità impianti presenti su una linea, e per ciascuna UI:

Il totale dei sostegni (conteggio asset di classificazione Sostegno);

Il totale dei Km Linea (sommatoria dell’attributo LIN_LUNGHEZZA presente su CAMPATA e TRATTA CAVO (non demolite/cedute), escludendo le campate in doppia terna delle campate ospitate (per campate ospitate si intendono la campata con codice SAP di 1°livello maggiore). Inoltre, nel caso di campate split va conteggiata la sola lunghezza delle campate con attributo LIN_CAMPATA_RIFERIMENTO vuoto);

La lunghezza della terna (sommatoria dell’attributo LIN_LUNGHEZZA di CAMPATE e TRATTE CAVO non demolite/cedute della linea in questione);

Il totale dei Km di cavo terrestre (sommatoria dell’attributo Km per le tratte di cavo con l’attributo Tipo posa = Terrestre);

Il totale dei Km di cavo marino (sommatoria dell’attributo Km per le tratte di cavo con l’attributo Tipo posa = Marino);

Informazione dell’unità impianti prevalente di una linea (il dato viene ricavato inserendo l’unità impianti con la maggiore quantità di chilometri di tracciato)

### Strumento generico per la gestione di roll up basati su gerarchia o relazione

In IBM MAS Manage deve essere possibile gestire un sistema di regole per permettere la visualizzazione di sommatorie di attributi, calcolate aggregando valori dai livelli inferiori rispetto ai livelli superiori, come ad esempio la somma della lunghezza delle campate (asset di secondo livello) il cui valore deve essere riportata sulla linea (asset di primo livello) a cui sono relazionate (vedi § 3.12.3).

Per permettere agli utenti di IBM MAS Manage di agire in autonomia nella realizzazione di nuovi, ulteriori aggregati rispetto a quelli precedentemente riportati, verrà implementato il seguente scenario:

Possibilità di indicare su un attributo la costituzione di un legame, specificando il livello (L1°ß L2° oppure L1° ß L4°). L’attributo sul primo livello sarà sempre la destinazione del calcolo, sui livelli secondo o quarto il contenuto dell’attributo sarà sempre la fonte del dato da calcolare;

Tipologia di calcolo, da applicare sull’attributo inserito nell’asset di primo livello, selezionato tra:

Somma;

Minimo;

Massimo;

Valore medio;

Per il conteggio della numerosità di elementi di un determinato tipo, si utilizza un attributo con valore di default a 1 sui livelli di fonte del dato (livelli 2° o 4°);

Eventuale criterio di aggregazione in base al quale operare la tipologia di calcolo. Un esempio è rappresentato dall’unità impianti, gestita su secondo livello della struttura delle linee. Il criterio di aggregazione sarà indicato in un apposto campo dell’attributo gestito sul modello dati delle specifiche tecniche (applicazione Classificazioni), indicando il campo o l’attributo contenente il criterio in base al quale aggregare:

Il campo potrà essere scelto solo tra i campi della tabella ASSET; nello specifico, per l’unità impianti verrà indicato ASSET.X3INGRP;

Nella scelta dell’attributo di classificazione dovrà essere indicata anche la classificazione alla quale fare riferimento

In fase di low level design si affronterà il tema relativo a quando eseguire il ricalcolo (basato su specifici eventi oppure con schedulazione programmata).

Gli attributi con regola, definiti sulle linee è sulle stazioni (primi livelli) non saranno editabili manualmente, ma verranno popolati solo dalla procedura di calcolo. Il valore mostrato nell’attributo sul primo livello sarà anche scritto nel database.

### Propagazione automatica del dato

Per alcuni attributi utilizzati su più livelli deve essere prevista la propagazione fisica (scritta nel database) del dato, dal livello superiore a quello inferiore (iterabile), sia rispetto alla relazione (1° à 2° livello) che rispetto alla gerarchia (2° à 3° à 4° livello). L’attributo contenente l’unità impianti, presente sui 2°, 3° e 4° livelli delle linee, deve automaticamente essere propagato, sia quando creo che, quando sposto, associando l’asset oggetto dello spostamento a un padre differente. Nella tabella seguente sono indicati i dati al momento individuati, con possibilità di essere aggiornati sino alla fase di disegno di dettaglio.

| Campo / Attributo | Spiegazione | Nota |
| --- | --- | --- |
| X3INGRP | È il campo della tabella ASSET sul quale viene gestita l’informazione della unità impianti |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |

## Gestione della disposizione fasi

Per permettere l’individuazione in campo della fase viene mantenuto l’attuale sistema che si basa sulla tipologia di sostegno e la distribuzione delle fasi stesse, osservando il sostegno in base al verso convenzionale della linea (alle spalle dell’osservatore la provenienza e di fronte la destinazione). Nell’immagine seguente sono riportate le 5 tipologie di sostegni con la distribuzione delle posizioni convenzionali rispetto alle mensole.

Figura 40 - Sostegni e distribuzione fasi

L’informazione è gestita con l’attributo LIN_DISPOSIZIONE_FASI associato all’asset di terzo livello classificato LINEA / SOSTEGNO AEREO / ATTACCHI CONDUTTORE; il contenuto dell’attributo avrà il seguente formato, garantito da una regola di editing:

A-n,n,n,n,n,n

La lettera A rappresenta il tipo di sostegno (E, F, G, H, I) mentre n può assumere i valori 1, 2, 3, rappresentativi delle differenti fasi, in base alle relazioni:

1 = fase 4

2 = fase 8

3 = fase 12

La posizione nella quale scrivo 1, 2 oppure 3 (le 6 n,n,n,n,n,n) serve a indicare la specifica mensola del sostegno. Nell’immagine precedente, per il sostegno di tipo G, il campo Disposizione fasi sarà:

G-2,3,1,,,

Al salvataggio del campo verranno applicate le seguenti regole di verifica:

Posso salvare anche con il campo non popolato

La prima posizione (A) dovrà avere uno dei 5 valori previsti (E, F, G, H, I)

La seconda posizione potrà essere solo il meno (-)

Le n potranno essere solo 1, 2, 3 o nulle

Dovranno essere sempre presenti tre valori numerici, oppure 6, nel solo caso di “ammazzettamento” (vedi § 3.8.2)

## Gestione delle informazioni sensibili alla doppia terna

Per permettere la gestione di asset singoli anche quando appartenenti a linee differenti, (nello specifico il sostegno e la campata), si è reso necessario spostare una serie di informazioni a livello di relazione, per permettere l’uso di valori differenti, in base alla linea che si sta considerando. Nell’elenco seguente sono riportati tutti i campi che necessiteranno di essere gestiti per specifica linea:

Codice descrittivo ID SAP (NNXXXCN-NNNXXXXX-S e NNXXXCN-NNNXXXXX), sia per campata che per sostegno (per omogeneità anche tratta e terminale cavo, seppur non gestiti in doppia terna). Qui ci si riferisce ai soli elementi di 2° livello delle linee. Gli elementi di primo livello (sia linee che stazioni) gestiranno l’ID SAP nella tabella ASSET;

Informazione della sequenza di campate e tratte cavo (X3POSNR), indipendentemente dalla presenza di una doppia terna o meno;

Informazione della sequenza di campate, sostegni, tratte e terminali cavo (X3SEQUENCE), indipendentemente dalla presenza di una doppia terna o meno

Informazioni gestite in attributi di asset aggregatori di terzo livello, specifici per ciascuna linea:

La disposizione fasi (attributo LIN_DISPOSIZIONE_FASI) è gestita a livello di aggregatori di terzo livello, sugli attacchi conduttore per i sostegni (impatto doppia terna)

L’informazione dei tronchi di ispezione (attributo LIN_TRONCO_ISPEZIONE) è gestita a livello di aggregatori di terzo livello, sui conduttori per le campate (impatto doppia terna) e sui cavi per le tratte cavo (mai in doppia terna).

L’informazione dei codici di indisponibilità (attributo LIN_ELEMENTO_INDISP) è gestita a livello di aggregatori di terzo livello, sui conduttori per le campate (impatto doppia terna) e sui cavi per le tratte cavo (mai in doppia terna).

Informazioni gestite in attributi di asset che rimangono comuni anche in doppia terna, come ad esempio le funi di guardia, ma sono soggette a potenziale ambiguità in caso di verso discorde tra le due linee. In questo caso è necessario gestire due attributi uguali, distinti solo per mezzo del campo Sezione, che verrà popolato, per ciascuno, con il codice di ciascuna delle due linee che condividono le stesse funi di guardia:

La disposizione delle funi di guardia, per mezzo di specifici valori (DX, SX, 3) è gestita in un attributo apposito (LIN_RIF_FUNE), associato all’asset con classificazione LINEA / CAMPATA AEREA / FUNI DI GUARDIA / FUNE DI GUARDIA. Al cambio di verso di una linea (una delle due, nel caso della doppia terna), il valore contenuto nell’attributo LIN_RIF_FUNE andrà scambiato (DX ßà SX); il valore 3 rimarrà invariato.

## Segmentazione linea per entra-esci

3.21 .

## Gestione della codifica descrittiva ID SAP

Nell’ambito di questo progetto, con il termine codifica descrittiva ID SAP si identifica una codifica univoca, generata dall’utente, i cui componenti sono validati da un algoritmo SAP. Questo ID SAP è sempre modificabile da parte dell’utente, fatti salvi i criteri di univocità e le regole di composizione. Sono previsti, infatti, dei controlli a sistema che permettono di evitare la duplicazione del dato e garantire, quindi, l’univocità del codice, mentre la sequenza dell’ID SAP viene definita tramite il campo custom X3SEQUENCE.

SAP valida anche la coerenza tra l’ID SAP di un elemento padre e l’ID SAP di un elemento figlio, avvisando l’utente dell’eventuale violazione di coerenza (poiché i livelli successivi al primo devono avere un ID SAP composto dagli ID SAP dei livelli precedenti + l’ID SAP del livello specifico, se manca la corrispondenza, l’utente viene avvisato della violazione).

Nei paragrafi seguenti, la regola, applicata dalla maschera di editing di SAP, si basa sulla seguente verifica di coerenza tra tipologia di dato e valore inserito:

N: solo numerico;

X: alfanumerico;

C: solo alfabetico;

Per tutti i casi, la separazione tra un livello e l’altro della codifica viene gestita con il simbolo del trattino (-).

Per i temi relativi alla ricodifica, vedere il § 3.19.

### ID SAP per le linee

La maschera di editing che SAP applica per le linee e che dovrà essere implementata in IBM MAS Manage dovrà adottare le seguenti regole:

Livello 1 SAP – NNXXXCN (7 digit)

Digit 1: indica la tipologia di asset, per le linee l’unico valore ammesso “2”;

Digit 2: indica il livello di tensione;

Digit 3÷5: usati per l’identificativo di esercizio della terna (la linea), in caso di lettere solo maiuscole;

Digit 6: carattere alfabetico solo maiuscolo, utilizzato per differenziare quei casi in cui esiste già una linea con i precedenti 5 caratteri uguali a quella che deve essere creata;

Digit 7: carattere numerico utilizzato per indicare un differente titolare o DT (distretto o Dipartimento territoriale) per ciascuna linea. In pratica, per ogni DT o titolare viene creato un nuovo primo livello, con i precedenti 6 caratteri identici;

Livello 2 SAP - NNNXXXXX (8 digit)

Digit 1÷3: caratteri numerici;

Digit 4÷8: caratteri alfanumerici, facoltativi;

In ogni caso ciascuno degli 8 digit del 2° livello, se non imputati dall’utente, verranno popolati con il trattino (-);

Livello 3 SAP - XN (2 digit)

Digit 1: carattere alfabetico solo maiuscolo, indica il tipo di oggetto. In creazione da IBM MAS Manage dovranno essere gestiti solo i seguenti:

S (sostegno);

H (Terminale cavo – in EAM gestito sull’Estremo cavo);

R (Giunti - in EAM gestito sull’Estremo cavo);

Digit 2: possibilità facoltativa, mai impiegata in SAP

Gli elementi per i quali l’ID SAP deve essere esposto anche in IBM MAS Manage e sottoposto a una possibile creazione o modifica, per cui necessita di essere validato, sono:

Linea (L1° IBM MAS Manage), per la possibilità di ricodifica (per ricodifica, vedere il § 3.19);

Campata (L2° IBM MAS Manage), sia per creazione che ricodifica (per ricodifica, vedere il § 3.19);

Sostegno (L2° IBM MAS Manage), sia per creazione che ricodifica (per ricodifica, vedere il § 3.19);

Tratta cavo (L2° IBM MAS Manage), sia per creazione che ricodifica (per ricodifica, vedere il § 3.19);

Estremo cavo inizio e fine (L2° IBM MAS Manage), sia per creazione che ricodifica (per ricodifica, vedere il § 3.19);

Gli scenari di impiego in IBM MAS Manage dell’ID SAP in relazione alle linee possono riguardare sia la ricodifica (L1° e L2°) che la creazione (per il solo L2°); nel caso di creazione massiva di elementi di secondo livello (che per mezzo dei rispettivi modelli creeranno in IBM MAS Manage anche gli L3° e L4°), degli 8 digit possibili del livello 2 SAP, solo i primi tre numerici potrebbero essere popolati con un progressivo numerico, mentre gli altri 5 rimarrebbero vuoti (popolato con il trattino), perché privi di regola.

## Gestione dei modelli

I modelli oggetto di questi paragrafi (§ 3.16.x) si basano tutti sul concetto di IAS (Item Assembly Structure). Applicando una IAS ad un asset, il sistema genera automaticamente una gerarchia di asset, con la medesima struttura e le stesse classificazioni definite nel modello.

Questa funzionalità di base permettere, ad esempio, la creazione di nuove linee elettriche, la realizzazione di derivazioni o la creazione di nuove stazioni, evitando la gestione manuale di ogni singolo componente; applicando i modelli, in automatico, tutta una serie di informazioni verranno quindi generate nel sistema, senza la necessità di specificarle ogni volta.

Un modello verrà definito come un insieme di asset, in gerarchia tra loro, per mezzo di relazioni padre – figlio; ad esempio, un sostegno potrà essere rappresentato come un modello, costruito in base a quanto mostrato nel seguente paragrafo. Potranno essere creati tanti modelli, rappresentativi di specifici sostegni, come ad esempio un modello per i sostegni delle tratte di tesatura che devono superare elevati dislivelli, quindi con un solo attacco conduttore, un altro modello con due attacchi conduttore e un modello con tre attacchi conduttore; un'altra strategia può essere quella di avere un modello più completo e dopo la creazione degli asset specifici, procedere rimuovendo quelli che non ci sono. Analogo discorso può essere fatto per asset non sempre presenti, come il nido avifauna o i sezionatori: possono essere definiti (unitariamente per sostegno) e successivamente rimossi, oppure non presenti nel modello e successivamente aggiunti.

La strategia migliore dovrebbe essere cercata nella gestione di modelli, rappresentativi della media degli asset che devono essere creati e che quindi richiedano, in post creazione, un numero ridotto di modifiche per ottenere asset informaticamente definiti uguali agli asset realmente installati.

Per la gestione della doppia terna vengono gestiti dei modelli costituiti con i soli asset specifici della seconda terna, senza quelli comuni, che saranno già stati creati applicando il modello iniziale per la singola terna (vedi l’esempio di creazione riportato in § 3.7.2 sia per la singola che la doppia terna).

Nel momento in cui un asset di secondo livello, sensibile alla doppia terna (sostegno o campata) viene associato ad una seconda linea, sempre in base alla procedura vista in § 3.7.2, Figura 14, ultima colonna, è mostrato il Parent Item: è proprio in base a questa informazione che il sistema è in grado di capire quale modello applicare per far progredire l’asset da singola a doppia terna (ossia quale aggregatore di terzo livello creare).

Per fare questo, sempre in fase di gestione dei modelli, nell’applicazione Item principale dovrà essere inserita la relazione tra il modello della singola e il modello della doppia terna.

Figura 41 - Correlazione tra modelli

Nell’immagine precedente si vede la relazione tra il sostegno per la doppia terna che ha come Parent Item quello a singola terna; se a fronte di un modello di terna singola sono stati messi in relazione più modelli per la terna doppia, la schermata di Figura 14 permetterà di eseguire la scelta, per riga, tra gli n relazionati (ossia tra tutti quelli che avranno il medesimo Parent Item).

### Modello per il sostegno

Ogni sostegno da aggiungere ad una linea dovrà essere creato con una funzione specifica da richiamare all’interno della linea stessa.

Nell’immagine seguente è rappresentata la struttura di asset che verrà creata scegliendo lo specifico modello; come spiegato a inizio del capitolo potranno essere gestiti e quindi scelti per la creazione, differenti modelli, che genereranno differenti strutture di asset, in base alle necessità contingenti.

Figura 42 - Modello di sostegno

Nel modello gli elementi che devono creare asset dovranno essere ripetuti per il numero di asset che devono essere creati. Per una terna singola dovranno essere presenti 3 armamenti conduttore, uno per fase; la fase sarà gestita per mezzo di un attributo dell’asset.

#### Modello per il sostegno a terna doppia

L’utilizzo di questo modello andrà fatto selezionando il sostegno, dopo che è stato messo in relazione alla seconda terna, richiamato dall’interno della seconda linea. L’applicazione del modello produrrà un nuovo asset di tipo Attacco conduttore, con tutti gli asset figli, specifici per ciascuna fase.

Figura 43 - Sostegno, aggiunte per doppia terna

### Modello per la campata

Ogni campata da aggiungere ad una linea dovrà essere creata con una funzione specifica da richiamare all’interno della linea stessa.

Nell’immagine seguente è rappresentata la struttura di asset che verrà creata scegliendo lo specifico modello; come spiegato a inizio del capitolo potranno essere gestiti e quindi scelti per la creazione, differenti modelli, che genereranno differenti strutture di asset, in base alle necessità contingenti.

Figura 44 - Elementi di campata per terna singola

Come si vede dal modello, non sono presenti ne giunti, né barrette preformate o distanziatori interfase, in quanto giunti e barrette sono asset di riparazione, montati in seguito alla risoluzione di un problema e quindi non consoni a entrare a far parte di un modello pensato per la creazione di elementi nuovi. Nel caso dei distanziatori, il modello è per una terna mono cavo. Il modello per una terna binata o trinata chiaramente includerà anche i distanziatori.

#### Modello per la campata a terna doppia

Per aggiungere i conduttori di una seconda terna ad una terna esistente si utilizzerà il seguente modello, applicando la IAS dopo aver selezionato la campata alla quale aggiungere i conduttori.

Figura 45 - Modello di campata per la doppia terna

### Modello per la tratta cavo

Di seguito il modello per creare gli asset rappresentativi della tratta cavo.

Figura 46 - Modello di tratta cavo

### Modello per l’estremo cavo

Di seguito i modelli per creare gli asset rappresentativi delle due tipologie di estremo cavo.

Figura 47 - Modello di estremo cavo terminale

Figura 48 - Modello di estremo cavo giunto

## Gestione del verso della linea

In questo paragrafo sono spiegati i punti di attenzione relativi alla gestione del verso della linea. La criticità riguarda la trasposizione tra i dati informatici e l’orientamento in campo, considerando che quasi tutti gli elementi (tranne i sostegni, per mezzo delle targhe monitorie) non hanno una taggatura per mezzo della quale mantenere il legame asset fisico – asset informatico.

Per supplire a questa mancanza viene utilizzata una convenzione attraverso la quale “leggere” la linea quando si è in campo e ricondurvi i corretti asset gestiti informaticamente.

Il principio basilare è quello del verso della linea: collocandosi davanti a un sostegno, in base al verso, alle mie spalle avrò il sostegno che precede quello che sto guardando. Da questo punto di osservazione normato sono in grado di identificare, rispetto ad un asse verticale, la destra e la sinistra.

Nel momento in cui viene invertito il verso di una linea, il punto di vista dell’osservatore deve essere ruotato di 180°: nel caso precedente, il sostegno che precede diventa il sostegno che osservo e il precedente sostegno che osservavo diventa il sostegno che precede. Analogamente, rispetto al precedente asse verticale, tutto ciò che si trovava a destra ora sarà a sinistra e viceversa.

Nel caso di due linee che condividono i medesimi sostegni e campate (linee in doppia terna) e viaggiano con il medesimo verso, devono essere analizzati i seguenti casi: inversione simmetrica (per entrambe le linee) del verso e inversione del verso di una sola delle due linee.

In caso di inversione simmetrica saranno aggiornate le sequenze di entrambe le linee e dovranno essere invertiti i contenuti di tutti gli attributi che gestiscono l’informazione di destra e sinistra (al momento la disposizione fasi).

In caso di inversione asimmetrica (solo una delle due linee cambia il verso) andrà aggiornata la sequenza di una sola linea e dovranno essere invertiti i contenuti di tutti gli attributi che gestiscono l’informazione di destra e sinistra della sola linea che ha subito l’inversione. In questo caso si verifica una situazione interessante: i due osservatori in campo, orientati ciascuno per il verso della propria linea, guardando il medesimo sostegno, si troveranno l’uno di fronte all’altro, con in mezzo il sostegno sul quale la destra di uno corrisponderà alla sinistra dell’altro e viceversa. Questo fa sì che nella sequenza a 6 numeri, utilizzata per posizionare le fasi, verranno riempite le stesse caselle (intendendo per casella, la posizione della stringa alfanumerica di disposizione fasi). Ad esempio, per un traliccio di tipo H, che sostiene linee, ossia due terne (vedi figura seguente), che hanno versi opposti, saranno rappresentate come segue:

Figura 49 - Posizione fasi su sostegno di terna doppia

Linea A, disposizione fasi: H-1,2,3,0,0,0

Linea B, disposizione fasi: H-1,2,3,0,0,0

Se la linea A fosse stata modificata rispetto alla disposizione canonica, con due fasi sulle due mensole più alte, sarebbe: H-1,2,0,3,0,0. In questo caso la linea B, quando orientata come la A, avrebbe la disposizione fasi così rappresentata: H-0,0,1,0,2,3 ma al cambio di verso diventerebbe H-0,2,3,0,0,1 in pratica si corrompe la regola di complementarità per la quale la somma della disposizione fasi di due linee sul medesimo sostegno di doppia terna produce la compilazione di tutte e 6 le caselle della stringa alfanumerica di disposizione fasi.

In IBM MAS Manage questo non rappresenta un problema, in quanto, nonostante venga gestito un solo sostegno, l’informazione delle fasi è posta sull’aggregatore di 3° livello (LINEA / SOSTEGNO AEREO / ATTACCHI CONDUTTORE), specifico di ciascuna linea nell’attributo LIN_DISPOSIZIONE_FASI.

### Modifica della sequenza per invertire il verso di una linea

L’inversione del verso in cui una linea deve essere letta si esegue invertendo la sequenza associata ad ogni elemento della linea. L’inversione di una dorsale non ha impatto sul verso delle derivazioni e l’inversione di una derivazione (ove avesse senso) non ha impatti sulla dorsale. Poiché la sequenza, in IBM MAS Manage verrà gestita per mezzo del campo X3SEQUENCE, gli esempi che seguono si riferiscono a questo campo; l’inversione provocherà, in ogni caso, anche la modifica del campo X3POSNR, che per mezzo del connettore, riverbererà la modifica in SAP.

Il caso d’uso per l’utilizzo di questa funzione è il seguente:

Richiamare a video, nell’applicazione di gestione delle linee, la linea coinvolta nella modifica del verso;

Richiamare la funzionalità <<Invertire linea>>;

Nella maschera di dialogo che si apre sono presenti i campi elemento di inizio ed elemento di fine, con le seguenti regole:

l’elemento da cui si vuole iniziare l’inversione permette di inserire solo elementi della linea sulla quale ci si trova (nell’esempio seguente 100);

Inserire un elemento della dorsale vincola la scelta dell’elemento di fine ai soli elementi della dorsale, successivi all’elemento di inizio (nell’esempio seguente 1500);

Inserire un elemento di una derivazione vincola la scelta dell’elemento di fine ai soli elementi della medesima derivazione, successivi all’elemento di inizio;

Poiché l’aggancio logico di una derivazione è fatto per mezzo di una lettera, che non cambia invertendo la linea, il legame con il sostegno di inizio derivazione non viene alterato;

Di seguito l’esempio di inversione di una linea con derivazione.

Anche nel caso di un’inversione della linea con una derivazione che si ricollega alla dorsale, l’inversione della linea non provocherà nessuna propagazione sulla derivazione, come nel caso seguente.

Per ottenere una modifica del verso della derivazione sarà necessario inserire come elemento di inizio 3600 e come elemento di fine 4200. Il risultato sarà il seguente.

Come si può notare, il sostegno 300DZ che era quello di ritorno della derivazione sulla dorsale, andrà aggiornato come segue: 300DZ à 300D e analogamente il 700D à 700DZ. Poiché anche la sequenza tra uscita per derivazione e ingresso da derivazione è essa stessa indicativa del verso, in fase di disegno di dettaglio verrà indagata la possibilità di tralasciare l’uso della lettera Z.

Invertendo il verso della linea, una procedura automatica applicherà un algoritmo per aggiornare automaticamente la disposizione fasi, in base al seguente modello:

Per i sostegni di tipo I e H i numeri inseriti nella sestupletta dovranno essere ricollocati con le seguenti regole:

Posizioni 1 ßà 4;

Posizioni 2 ßà 5;

Posizioni 3 ßà 6;

Per i sostegni di tipo G andranno invertiti solo i contenuti delle posizioni 1 ßà 3;

Per i sostegni E ed F l’inversione non provoca una necessità di scambio di posizioni;

### Modifica della sequenza (POSNR) per fare spazio a nuove campate e sostegni

Nella normale operatività di Terna si può rendere necessario inserire nuove campate e sostegni tra due sostegni esistenti, ad esempio per aggirare un’area di nuova urbanizzazione.

Poiché tutte le funzioni di variazione di una linea incorporano già la funzione di rinumerazione della sequenza, non sarà più necessario gestire una funzione specifica; si rimanda alle differenti funzioni per il dettaglio operativo di modifica dei valori di X3POSNR e X3SEQUENCE.

### Gestione delle derivazioni

In questo paragrafo e nei successivi sotto paragrafi si affronta il tema della gestione delle derivazioni da una linea principale (dorsale) e della derivazione da una derivazione (derivazione di derivazione). Le derivazioni possono essere sia in cavo che in aereo che miste, ossia cavo e aereo per la medesima derivazione. Non deve essere possibile creare, da una derivazione in cavo o da una dorsale in cavo una derivazione incavo. Per le sole dorsali in aereo devono essere supportate le derivazioni multiple da uno stesso sostegno, sino ad un numero massimo di tre derivazioni, sempre in aereo. Quando da un sostegno parte una derivazione in cavo, da quel sostegno non deve essere possibile creare altre derivazioni, né in cavo né in aereo.

Nell’immagine seguente i cerchi rappresentano i sostegni e le frecce le campate; il verso della freccia rappresenta il verso della linea. I numeri su sostegni e campate rappresentano la numerazione in base alla quale è creato il legame logico di sequenzialità tra sostegni e campate.

Figura 50 - Elementi di una linea aerea con i rispettivi numeri di sequenza

Per creare una derivazione (la quale parte sempre da un sostegno), si inserisce una lettera nel numero della sequenza del sostegno coinvolto (potrebbe anche essere un campo apposito), che deve essere conservata lungo tutta la derivazione, come da immagine seguente (700 à 700A).

Figura 51 - Dorsale aerea con una derivazione aerea

Alla creazione di un nuovo elemento il campo della sequenza è popolato automaticamente, con il valore massimo raggiunto; per gestire la derivazione, la lettera da anteporre andrà imputata manualmente.

Per creare altre derivazioni si opera in modo analogo, utilizzando la successiva lettera dell’alfabeto.

Per creare derivazioni di derivazioni si gestisce sempre l’informazione per mezzo della lettera successiva, come da figura seguente (A2100 à A2100B).

Figura 52 - Derivazione di derivazione aerea

Nel caso in cui una derivazione deve congiungersi nuovamente alla dorsale, la sequenza del sostegno di ritorno deve contenere, a destra del numero, la lettera identificativa della derivazione, seguita dalla lettera Z, come nel seguente esempio (900 à 900D e 1300 à 1300DZ).

Figura 53 - Derivazione aerea con rientro sulla dorsale

I criteri di gestione sopra esposti della sequenza associata a sostegni e campate devono garantire una visualizzazione in tabella ordinata come segue:

Prima i sostegni e campate della linea principale, ordinati per sequenza numerica, senza influenze da parte della componente letterale gestita a destra del numero

A seguire le campate e sostegni delle derivazioni, raggruppate per lettera a sinistra del numero e in sequenza basata sul numero, non influenzato da eventuali lettere alla sua destra (come per la linea principale.

Utilizzando i precedenti esempi, la rappresentazione in tabella è la seguente:

Figura 54 - Rappresentazione della sequenza con derivazioni

#### Derivazioni multiple dallo stesso sostegno della dorsale

Una particolare necessità di rappresentazione della derivazione si ha quando dallo stesso sostegno parte più di una derivazione. In questo caso si parla di derivazioni multiple, per le quali deve essere supportato fino ad un massimo di tre derivazioni dal medesimo sostegno (Figura 57 - Derivazione tripla ).

La derivazione multipla (nello specifico, il punto di derivazione) coinvolge solo la parte aerea. Dopo la derivazione aerea, possono esserci anche tratte in cavo, tanto per la dorsale quanto per la derivazione stessa.

Poiché questa soluzione dovrà permettere l’integrazione con i dati gestiti da SAP, la gestione del POSNR dovrà utilizzare l’asterisco quando verrà creata una doppia derivazione (attuale comportamento SAP).

Figura 55 - Esempio derivazione multipla stesso sostegno – POSNR

Per i dati gestiti solo in EAM, invece, l’individuazione del sostegno e il legame con le derivazioni di pertinenza sono gestiti per mezzo di lettere (alla fine a destra della parte numerica della sequenza per il sostegno, con tutte le lettere delle derivazioni che da lì partono e all’inizio, a sinistra della sequenza, per ciascun elemento di ogni derivazione).

Figura 56 - Derivazione doppia dallo stesso sostegno - X3SEQUENCE

Figura 57 - Derivazione tripla dallo stesso sostegno – X3SEQUENCE

Di seguito i nuovi comportamenti da integrare nella funzionalità Crea derivazione:

Impostazione di un asterisco sul POSNR del sostegno da cui parte la seconda derivazione.

Per quanto attiene alla sequenza (X3SEQUENCE) gestita in EAM, verrà implementata la logica della doppia lettera per le derivazioni multiple. Es. 0009AB per il sostegno di derivazione e A000001 per l’inizio di una derivazione e B000001 per l’inizio dell’altra derivazione.

Nel caso di tre derivazioni (massimo gestito) il sostegno di derivazione sarà, Es. 0009ABC. Il POSNR rimarrà sempre identificato con l’asterisco (*).

#### Derivazioni miste aereo e cavo

La funzione di Crea derivazioni deve supportare anche la possibilità di creare derivazioni in cavo, derivazioni miste, aereo – cavo e derivazioni di derivazioni, che da aereo passano in cavo e viceversa. L’unico caso che deve essere espressamente escluso è quello di creare derivazioni da linee in cavo. Nelle immagini che seguono sono rappresentate differenti casistiche.

Figura 58 - Derivazione mista, prima in aereo e poi in cavo

Figura 59 - Derivazione diretta in cavo dalla dorsale

Figura 60 - La derivazione in cavo da dorsale in cavo non è supportata

Figura 61 - Derivazione in cavo da derivazione in aereo

I casi che non sono stati qui rappresentati devono essere considerati esclusi dalle possibilità della funzione di creazione delle derivazioni.

## Gestione della ricodifica (rinomina) del codice descrittivo SAP (ID)

Nelle linee, la ricodifica interesserà il secondo livello di IBM MAS Manage, con i seguenti casi d’uso, di seguito riportati:

Rinomina del secondo livello;

Rinomina come conseguenza dell’associazione a un nuovo primo livello (ad esempio § 3.15);

Rinomina come conseguenza dell’inversione di una linea;

Modifica del tracciato di una linea e conseguente rinomina (riuso) della campata in ingresso.

Il dato è gestito nel campo X3SAPID. La ricodifica deve garantire l’aggiornamento sia per gli asset di secondo livello (tabella ASSETLOCRELATION) che per gli eventuali dati gestiti negli attributi delle specifiche tecniche (tabella ASSETSPEC).

1 - Caso d’uso modifica secondo livello linea

Modifica dell’identificativo parlante SAP sui secondi livelli, della sola componente facoltativa (quindi solo campate e tratte cavo. Sono esclusi dalla ricodifica sostegni e terminali, in quanto non gestiscono una parte facoltativa propria e sono in SAP al terzo livello).

OPERATIVITA':

La ricodifica avviene per mezzo di una specifica funzione nell'applicazione Linee. Questa funzione richiama l’apertura di una finestra di dialogo che visualizza il valore attuale e fornisce un campo vuoto in cui inserire la porzione facoltativa. Al salvataggio la procedura verifica che la modifica non crei un duplicato del valore.

In caso di duplicato, il salvataggio viene bloccato ed è mostrato un messaggio di errore con la motivazione.

2 - Caso d’uso associazione di secondi livelli a un nuovo primo livello (esempio entra /esci)

In questo caso, gli elementi di secondo livello che precedentemente erano associati a una linea ora vengono associati a una nuova linea e quindi ne deve essere aggiornato il primo componente del campo X3SAPID.

La ricodifica verrà gestita in automatico dalla funzionalità EAM dedicata (Riassociazione asset di secondo livello) per il relativo caso d'uso, senza necessità di dover utilizzare la funzionalità di rinomina (vedi 1 - Caso d’uso modifica secondo livello linea).

In caso di eventuali duplicazioni provocate dall’uso della funzione di inversione, l’operazione viene bloccata ed è mostrato un messaggio di errore con la motivazione.

3 - Caso d’uso modifica secondo livello linea in seguito a inversione

In caso di inversione di una linea (§ 3.18.1) l’operatività in IBM MAS Manage coinvolge unicamente le informazioni di sequenza (X3SEQUENCE e X3POSNR) ma poiché in SAP sostegni e terminali cavo sono figli, rispettivamente, di campate e tratte cavo e che per convenzione si trovano sempre alla fine di campata o tratta, in seguito all’inversione sarà necessario rinominare gli X3SAPID degli elementi di secondo livello campate e tratte cavo; di conseguenza, i valori di X3SAPID per i sostegni e i terminali saranno resi coerenti con i codici di campata e di tratta dei quali diventeranno figli.

La ricodifica verrà gestita in automatico dalla funzionalità EAM dedicata per il relativo caso d'uso, senza necessità di dover utilizzare la funzionalità di rinomina (vedi 1 - Caso d’uso modifica secondo livello linea).

In caso di eventuali duplicazioni provocate dall’uso della funzione di inversione, l’operazione viene bloccata ed è mostrato un messaggio di errore con la motivazione.

4 - Caso d’uso della modifica del tracciato

L’unico caso di modifica del tracciato che ha un impatto con codici esistenti di X3SAPID è quello in cui sostegni e campate aggiuntive rientrano su un sostegno esistente, come mostrato nella successiva Figura 62: il sostegno 30 e le due campate 20 e 40 vengono rimossi e i nuovi elementi da campata 20 a campata 120 vengono inseriti nel tracciato. In tutta questa operazione, la campata 120 che si lega al sostegno 130 dovrà avere il medesimo X3SAPID della dismessa campata 40, in quanto non può essere sostituita l’esistente targa monitoria del preesistente sostegno 130.

Figura 62 – Modifica di tracciato con rimozione

Nel caso in cui la modifica del tracciato venga inserita prima del sostegno 5 oppure a prolungare, successivamente al sostegno 140, non si avranno impatti su X3SAPID, ma solo variazioni delle sequenze, come già analizzato in § 3.9.1.3.

La ricodifica verrà gestita in automatico dalla funzionalità EAM dedicata per il relativo caso d'uso, senza necessità di dover utilizzare la funzionalità di rinomina (vedi 1 - Caso d’uso modifica secondo livello linea).

## Gestione del codice di indisponibilità

Il codice di indisponibilità è utilizzato per avere una codifica condivisa tra chi gestisce l’anagrafica e chi gestisce il dispacciamento, allo scopo di comunicare quali asset dovranno essere tolti dalla produzione (ossia non più percorsi dalla corrente elettrica).

Il codice indisponibilità, in SAP, è gestito per mezzo di una struttura di sedi tecniche specifiche. La funzione aziendale che si occupa di crearle è il dispacciamento, la funzione aziendale che si occupa di associarle alle sedi tecniche rappresentative di linee e stazione è la funzione aziendale di asset management.

Il codice indisponibilità (menzionato come codice INDISP in SAP) in IBM MAS Manage dovrà poter essere associato agli asset per mezzo di appositi attributi di classificazione, con le seguenti regole:

per le linee è associato agli asset di 1° e 3° livello (quindi LINEA per il 1° mentre LINEA / CAMPATA AEREA / CONDUTTORI e LINEA / TRATTA CAVO / CAVI per il 3°) per mezzo dell’attributo LIN_ELEMENTO_INDISP;

Gli attributi degli asset (xxx_ELEMENTO_INDISP) che dovranno recepire le sedi tecniche di indisponibilità saranno configurati per permettere solo l'inserimento di sedi tecniche di indisponibilità.

La coerenza della codifica delle sedi tecniche di indisponibilità è del dispacciamento, che per le linee agisce nel rispetto del quadro nazionale (deve tenere conto di quanto gestito da altri operatori) mentre per le stazioni genera il codice su indicazione di AM. In IBM MAS Manage verrà gestito per mezzo della struttura dati, appositamente creata, X3SEDIINDISP.

Di seguito una serie di punti, fissati con il business, relativi all’argomento dei codici di indisponibilità:

L’eventuale ricodifica non impatta le sedi di indisponibilità associate.

Impiego del prefisso Z per le stazioni ed L per le linee.

In fase di ETL questi dati popoleranno la tabella X3SEDIINDISP.

Nell’immagine seguente l’ipotesi di gestione dei dati richiesti in input per eseguire la funzionalità. Il simbolo di colore nero rappresenta l’opzione selezionata e di colore grigio quella non selezionata; una scelta è obbligatoria e il default è quella di Includi asset successivi.

63

64

65

3

66 - Riassociazione di una porzione di linea in una linea vuota

67 - Riassociazione di una porzione di linea a inizio altra linea

68 - Riassociazione di una porzione di linea a una linea in cavo

69 - La riassociazione di una linea con degli ammazzettamenti

In 70 e 71 sono mostrate due operazioni di riassociazione che insistono sulla medesima campata in doppia terna; la prima operazione non cambia lo stato della campata 4, ma provoca la sola disassociazione dalla linea C, mentre la seconda operazione, oltre a provocare la disassociazione della campata 4 dalla linea A ne modifica anche lo stato in demolito. Lo stato non può essere variato per gli elementi in doppia terna, in quanto è un’informazione condivisa tra le due linee di appartenenza.

70 - Gestione della doppia terna caso a-1

71 - Gestione della doppia terna caso a-2

72 - Gestione della doppia terna caso b

73 - Gestione della doppia terna caso c

74 - La riassociazione totale

75 - La riassociazione di una porzione di derivazione

76 - La riassociazione di una porzione di dorsale

77

77

77 - Scenari esclusi a

78 - Scenari esclusi b

# Elementi anagrafici

A seguito delle analisi condotte sui requisiti affrontati in ambito Wave 2, sono state implementate nuove logiche per la gestione degli elementi anagrafici di Terna presenti in EAM. Nel dettaglio, in questo capitolo, verranno trattate le seguenti tematiche:

Anagrafiche opere interferenti;

Lotti tecnici omogenei;

Propagazione degli attributi.

Si segnala, inoltre, che il paragrafo 4.1, relativo all’anagrafica delle opere interferenti, è stato aggiornato in base alle osservazioni emerse durante la DEMO 18.

## Anagrafiche opere interferenti

Le opere interferenti verranno gestite in anagrafica come asset di 4° livello (Figura 79) sottostanti a un 3° livello aggregatore “Opere interferenti” a sua volta figlio del 2° livello “Campata” (nel caso di linee aeree) o del 2° livello “Tratta cavo” (nel caso di linee in cavo). Vengono definite due classificazioni distinte di 4° livello:

Opera interferente aerea, per le linee aeree

Opera interferente cavo, per le linee in cavo

Figura 79 - Gerarchia opere interferenti

Di seguito si riportano gli attributi presenti all’interno di ogni opera interferente:

ID opera (LIN_ID_OPERA_INT): Generato automaticamente dal sistema come concatenazione di Codice regione, Tipologia, Progressivo numerico a 6 cifre che corrisponderà al numero progressivo appena successivo rispetto ad altre opere interferenti della stessa tipologia; Identifica univocamente l’opera interferente con l’asset. In fase di migrazione sarà fornito dal BSN mediante il file Excel che sarà usato per il primo popolamento.

Codice regione (LIN_REGIONE_INT): L’attributo codice istat della regione dove si trova l’opera interferente. L’attributo viene valorizzato manualmente scegliendo il valore desiderato da una lista elenco predefinita, contenente i codici istat ammissibili.

Tipologia (LIN_TIPO_INT): campo valorizzato automaticamente in fase di creazione dell’asset di 4° livello e potrà essere, successivamente, modificato manualmente dall’utente tramite una lista valori.

Sotto – tipologia (LIN_SOTTOTIPO_INT): campo valorizzato manualmente dall’utente tramite una lista valori.

Criticità (LIN_CRITICITA_LIV4_INT / LIN_CRITICITA_LIV3_INT): L’attributo “criticità” viene calcolato esclusivamente per opere interferenti in stato “DATT”. Può essere 1 (“critico”) o 0 (“non critico”), deve essere valorizzato automaticamente dal sistema in base a un calcolo eseguito in base a “Tipologia”, “Sotto tipologia” e “Distanza orizzontale”. L’attributo deve essere valorizzato anche sul terzo livello in base alla criticità dei quarti livelli sottostanti (il 3° livello è critico se almeno un 4° livello sottostante è critico). La criticità viene calcolata automaticamente dal sistema sulla base della seguente logica basata sulla tabella parametrica riportata in Figura 80 (con due righe di esempio), tramite la quale l’utente centrale può specificare per ciascuna tipologia di opera la criticità e la soglia di distanza minima orizzontale utilizzati dall’algoritmo (la tabella parametrica è un dominio modificabile liberamente dall’utente ed è accessibile tramite azione laterale nell’applicazione “Linee”).

Figura 80 - Tabella parametrica opere interferenti

Sulla base della tabella parametrica, il sistema applica la seguente logica in base alla tipologia di opera interferente inserita (dove “criticità opera” e “distanza orizzontale” = criticità opera e distanza orizzontale che si sta inserendo; mentre “criticità tabella parametrica” e “soglia distanza orizzontale” = criticità e soglia distanza orizzontale inseriti nella tabella parametrica):

Se “criticità tabella parametrica” = 0 allora “criticità opera” = 0;

Se “criticità tabella parametrica” = 1 e “distanza orizzontale” < “soglia distanza orizzontale” allora “criticità opera” = 1;

Se “criticità tabella parametrica” = 1 e “distanza orizzontale” >= soglia distanza orizzontale” allora “criticità opera” = 0;

Per le tipologie che non sono descritte nella tabella parametrica, la criticità deve essere impostata di default come “criticità opera” = 0.

Denominazione (LIN_DENOMINAZIONE_INT): campo libero valorizzato manualmente dall’utente;

Ente gestore (LIN_ENTE_GESTORE_INT): campo valorizzato manualmente dall’utente tramite una lista valori che sarà popolata da un catalogo implementabile a sistema;

Data rilievo (LIN_DATA_RILIEVO_INT): campo in formato data valorizzato manualmente dall’utente;

Progressivo da estremo di rif (LIN_PROG_ESTREMO_RIF_INT): campo libero in formato numerico (espresso con unità di misura “metri”) che rappresenta la distanza dell’interferenza dal sostegno di riferimento riportato nel campo sottostante.

Rif. estremo progressivo (LIN_RIF_ESTREMO_PROG_IN): campo popolato manualmente dall’utente tramite una lista contenente i sostegni della campata. Nella box di dialogo per la scelta verrà mostrato sia il numero univoco dell’asset (che poi verrà effettivamente salvato nell’attributo) che il SAPID del sostegno, entrambe filtrabili. Per gli asset in doppia terna, saranno mostrate due righe, ciascuna con il proprio SAPID;

Distanza orizzontale (LIN_DISTANZA_ORIZZONTALE_INT): campo libero in formato numerico (espresso con unità di misura “metri”) popolato manualmente dall’utente. In caso di inversione della linea questo valore cambia segno (viene moltiplicato per “-1”);

SAP-ID linea interferente (LIN_SAPID_LINEA_INT): campo valorizzato manualmente dall’utente tramite una lista valori contenente i SAP-ID delle linee contenute in EAM. Tale lista valori viene filtrata di default su tutte le linee dell’UI della stessa campata oggetto dell’interferenza. È un campo obbligatorio utilizzato quando a interferire è una linea e l’ente gestore è uguale a “Terna” oppure quando l’ente gestore è uguale a “Rete” e la tipologia è uguale a “altre linee”. Selezionando la lente di ingrandimento, il sistema apre una dialog che permette di scegliere il SAPID della linea interferente. La lista di SAPID selezionabile è filtrabile per i seguenti campi:

SAPID;

Descrizione;

DT;

UI;

Asset;

SAP-ID campata interferente (LIN_SAPID_CAMPATA_INT): popolato dall’utente inserendo il SAP-ID della campata incrociata dall’interferenza, è possibile selezionare qualsiasi campata, esclusi gli stati DEMO e DEL, appartenente alla linea selezionata in precedenza. In caso di ricodifica del SAPID della campata, il valore si aggiornerà automaticamente anche nelle specifiche tecniche.

Oltre alla casistica più semplice e lineare in cui un’interferenza incrocia la linea in un solo punto, possono presentarsi anche altre situazioni:

Opera interferente che incrocia più volte la stessa linea: ogni intersezione verrà rappresentata da uno specifico 4° livello “opera interferente”, ognuno con il proprio AssetID;

Opera interferente che incrocia due linee parallele: ogni intersezione verrà rappresentata da uno specifico 4° livello “opera interferente”, ognuno con il proprio AssetID e afferente a una gerarchia anagrafica differente (questo perché sono presenti 2 differenti linee);

Opera interferente che incrocia due linee in doppia terna: in questo caso l’intersezione avviene sulla campata comune; pertanto, sarà presente un unico 4° livello “opera interferente” afferente alla gerarchia della campata comune.

L’elenco degli attributi delle opere interferenti, accessibile tramite l’icona evidenziata in Figura 81, sarà gestito in EAM mediante la vista rappresentata in Figura 82.

Figura 81 - Tabella dell'asset di quarto livello opera interferente con evidenza all’icona di accesso all’elenco di attributi

Figura 82 - Attributi dell’asset di quarto livello “opera interferente”

### Modalità di gestione

Di seguito si riportano le logiche di gestione, visualizzazione e utilizzo delle opere interferenti:

Gestione:

Gli utenti anagrafici possono inserire e modificare le opere interferenti riferite agli asset di propria competenza (DT);

Gli utenti del Centro 1 possono inserire e modificare le opere interferenti associate a tutti gli asset della rete;

Visualizzazione:

Tutti gli utenti possono visualizzare le opere interferenti definite a sistema per tutti gli asset (cross DT);

Utilizzo:

Il DSS legge l’attributo “Criticità” in input alle policy per generare attività di manutenzione.

### Modalità di creazione

La creazione delle opere interferenti avviene all’interno dell’applicazione “Linee”. Come riportato in Figura 79, l’anagrafica delle opere interferenti prevede un terzo livello aggregatore (sottostante a campata o tratta cavo) e un quarto livello che rappresenta la specifica interferenza.

Di seguito si riportano gli step da seguire per la creazione di un’opera interferente:

Accedere all’applicazione “Linee”;

Selezionare una linea;

Cliccare la funzionalità “Crea opere interferenti” per creare il terzo livello aggregatore. Si specifica che tale funzionalità è disponibile solo per asset di 2° livello Campata e Tratta cavo.

Figura 83 - Funzionalità di creazione Opera Interferente 3° livello

Una volta creato l’aggregatore “Opere interferenti” di 3° livello è necessario utilizzare la funzionalità “Crea asset di quarto livello”, rappresentata in Figura 84 ,per creare l’opera interferente di 4° livello sottostante.

Figura 84 - Funzionalità di creazione Opera Interferente 4° livello

Una volta selezionata l’opzione “Crea asset di quarto livello”, il sistema apre la dialog rappresentata in Figura 85, dove l’utente può selezionare da un elenco predefinito la classificazione (Opera interferente), la tipologia di interferenza (ad es. Strada, Linea elettrica, ecc) e la data di rilievo dell’interferenza, selezionabile dal calendario. Il campo della data deve essere vuoto e obbligatorio.

Figura 85 - Dialog di creazione dell'opera interferente

Accedere alle specifiche tecniche dell’asset di 4° livello appena creato e compilare gli attributi come descritto all’inizio del capitolo. L’accesso alle specifiche tecniche dell’opera interferente segue le stesse modalità implementate per l’anagrafica in Wave 1.

Dopo aver eseguito i passaggi appena descritti, l’opera interferente appena creata viene resa visibile nella vista evidenziata in Figura 86, all’interno dell’applicazione “Linee”. La vista è composta dalle seguenti colonne:

Asset;

Descrizione;

Classificazione;

Unità impianti;

Data rilievo;

Stato (DATT, DIST e DELE); NO

Figura 86 - Tabella contenente l'asset di quarto livello opera interferente, all'interno dell'applicazione “Linee”

### Segnalazioni dal campo

Nel caso in cui l’operatore durante l’esecuzione di un OdL rilevi una nuova opera interferente o una differenza tra quanto presente sul campo e quanto riportato in anagrafica, può procedere all’invio di una segnalazione verso gli utenti abilitati alla modifica dell’anagrafica appartenente all’UdB responsabile asset (da Web il responsabile asset può visualizzare le notifiche di segnalazioni dal campo e tramite le notifiche accedere direttamente agli OdL ai quali sono associate).

Per inviare una notifica da app mobile, una volta all’interno dell’OdL, l’operatore clicca il pulsante riportato in Figura 87 per accedere alla schermata di compilazione della segnalazione (Figura 88); successivamente inserisce il motivo della segnalazione (in forma di testo libero) e invia la notifica al responsabile asset. Quest’ultimo può visualizzare le notifiche ricevute dal campo nel CdC e procedere con le modifiche anagrafiche riferite dall’operatore se le ritiene valide (con la possibilità di consultare l’OdL associato alla notifica per avere maggiori dettagli sull’attività che ha portato alla rilevazione della segnalazione e di visualizzare eventuali foto allegate all’ordine). A seguito della modifica il responsabile asset, da Web, spunta il flag “gestito” sulla notifica. Nell’elenco delle notifiche, saranno sempre visualizzabili quelle che sono state gestite e quelle ancora da gestire, identificate dal relativo flag.

Figura 87 - Schermata OdL da mobile

Figura 88 - Schermata di compilazione della segnalazione

## Lotti tecnici omogenei

EAM permette il raggruppamento di un insieme di campate (asset di secondo livello) appartenenti alla stessa linea tramite l’attributo “Lotto tecnico omogeneo”. Nello specifico, le campate vengono assegnate automaticamente dal sistema ad un lotto tecnico omogeneo in base alle seguenti caratteristiche tecniche dei conduttori sottesi:

Materiale del conduttore: materiale del conduttore sotteso alla campata, ad esempio “Alluminio”, “Rame”, ecc. Il materiale della campata viene dedotto dall’attributo “Tipo di conduttore”, che riporta sia il materiale che il diametro del conduttore;

Diametro del conduttore: diametro del conduttore sotteso alla campata. Il campo viene dedotto dall’attributo “Tipo di conduttore”, che riporta sia il materiale che il diametro del conduttore;

Anno di installazione;

Criticità: valutata in base alla criticità relativa alle opere interferenti associate al conduttore, dove in caso di multiple opere interferenti viene utilizzata la criticità più grave.

Essendo tale attributo non presente attualmente a sistema, viene definito un algoritmo che effettua un calcolo di inizializzazione per la prima valorizzazione, basandosi sulla valutazione degli attributi indicati sopra e partendo dalla prima campata della linea in ordine di posizione. La

Figura 89 e la tabella sottostante riportano un esempio di una assegnazione iniziale dei lotti tecnici in base agli attributi e la posizione delle campate.

Figura 89 - Lotti tecnici linea

| Campata | Materiale | Diametro (mm) | Criticità | Anno di installazione | Lotto tecnico omogeneo |
| --- | --- | --- | --- | --- | --- |
| 1 | Alluminio | 15 | 1 | xxx | 1 |
| 2 | Alluminio | 15 | 1 | xxx | 1 |
| 3 | Alluminio | 15 | 1 | xxx | 1 |
| 4 | Rame | 15 | 2 | xxx | 2 |
| 5 | Rame | 15 | 2 | xxx | 2 |
| 6 | Rame | 18 | 3 | xxx | 3 |
| 7 | Rame | 18 | 3 | xxx | 3 |
| 8 | Rame | 18 | 3 | xxx | 3 |
| 9 | Alluminio | 18 | 2 | xxx | 4 |
| 10 | Alluminio | 18 | 2 | xxx | 4 |
| 11 | Alluminio | 15 | 1 | xxx | 1 |
| 12 | Alluminio | 15 | 1 | xxx | 1 |

In seguito alla determinazione automatica iniziale prevista per i lotti, il calcolo per l’assegnazione di una campata ad un lotto tecnico omogeneo viene eseguito al momento della creazione di una campata o dell’aggiornamento degli attributi target, in generale in caso di modifica dell’assetto sull’intera linea. Durante la creazione di una nuova campata, il lotto tecnico viene assegnato automaticamente dal sistema alla definizione degli attributi necessari sull’asset, rimanendo non valorizzato fino all’inserimento di tutti i campi necessari. Se non esiste un lotto tecnico corrispondente agli attributi inseriti, viene creato un nuovo lotto tecnico.

I lotti tecnici omogenei servono alla seguente funzione:

Valutazione della composizione della linea mediante report dedicato: tramite l’apposita funzionalità di reportistica presente in EAM, è possibile richiedere la generazione di report lotti tecnici-linea specificando i seguenti parametri di lancio:

Asset di primo livello;

UI responsabile asset;

DT responsabile asset;

Livello di tensione.

Una volta impostati i parametri di lancio, il sistema genera il report contenente i seguenti campi:

DT responsabile asset;

UI responsabile asset;

Codice lotto tecnico omogeneo;

Gerarchia: struttura gerarchica associata alle campate;

ASSETNUM: codice identificativo univoco EAM delle campate comprese nel perimetro delineato dai parametri di lancio e associate ad un lotto tecnico omogeneo;

SAPID asset 1° livello: codice univoco identificativo SAP della linea di riferimento della campata del lotto omogeneo;

SAPID: codice univoco identificativo SAP delle campate comprese nel perimetro delineato dai parametri di lancio e associate ad un lotto tecnico omogeneo;

Criticità: valore della criticità (recuperata delle opere interferenti) con cui è stato valutato il lotto tecnico;

Stato: stato delle campate comprese nel perimetro delineato dai parametri di lancio e associate ad un lotto tecnico omogeneo

Lotto tecnico omogeneo: lotti tecnici omogenei comprese nel perimetro delineato dai parametri di lancio;

Materiale conduttore: materiale del conduttore delle campate comprese nel perimetro del lotto tecnico omogeneo;

Diametro conduttore: diametro del conduttore delle campate comprese nel perimetro del lotto tecnico omogeneo;

Posizione campata: Posizione delle campate comprese nel perimetro del lotto tecnico omogeneo.

HI conduttore;

Anno installazione conduttore: anno di installazione delle campate comprese nel perimetro del lotto tecnico omogeneo;

Materiale fune di guardia: materiale delle funi di guardia associate alle campate comprese nel perimetro del lotto tecnico omogeneo;

Diametro fune di guardia: diametro delle funi di guardia associate alle campate comprese nel perimetro del lotto tecnico omogeneo;

Anno installazione fune di guardia: anno di installazione delle funi di guardia associate alle campate comprese nel perimetro del lotto tecnico omogeneo;

# Recepimento open point W1/ W2

In questo capitolo sono trattati, dal punto di vista delle eventuali bonifiche sui dati, gli open point che verranno lavorati contestualmente all’attuale livello di revisione del documento. Tutte le modifiche alle funzioni esistenti e l’eventuale sviluppo di nuove funzioni sono state integrate nel documento.

## OP_252 – Gestione del legame tra linee elettriche differenti

BONIFICHE:

Per gli eventuali asset di congiunzione, già presenti a sistema, con un elenco fornito dal business, si agirà popolando massivamente l’attributo LIN_SOSTEGNO_INIZIO o LIN_ ESTREMO _INIZIO per tutti i casi di legame interlinea (e in coerenza, dovranno essere popolate anche le rispettive SECTION).

CONNETTORE:

Se il connettore trova l'attributo LIN_SOSTEGNO_INIZIO o LIN_ ESTREMO _INIZIO valorizzato lo riporta a SAP, in caso contrario, esegue il suo calcolo.

## OP_305 - Rimozione della gestione della lettera K per il terminale di fine

BONIFICHE:

Per i soli asset di secondo livello classificati come Estremo Cavo, che hanno come figlio di terzo livello un asset classificato come Terminale, il SAPID deve essere aggiornato, sostituendo la lettera K con la lettera H. Prestare attenzione, poiché in casi come il seguente:

23082D1-999-KFET-K

Ad essere sostituita dovrà essere solo l’ultima lettera a destra, ottenendo:

23082D1-999-KFET-H

All’attuale ricerca sono circa 1.000 gli asset con K dell’ultima porzione del SAPID.

CONNETTORE:

L’aggiornamento in EAM dovrà comportare il relativo allineamento di SAP, che dovrà essere coinvolto SAP per avere l’OK sulla bontà dell’aggiornamento.

ATTENZIONE: le verifiche delle sincronizzazioni eseguite dal connettore a seguito delle bonifiche sull’anagrafica EAM potranno essere eseguite solo in produzione, non essendo disponibile un ambiente SAP per la verifica con i dati aggiornati.

## OP_253 – derivazioni multiple dallo stesso sostegno della dorsale

BONIFICHE:

Caricare in EAM le 9 linee che sono state individuate come detentrici di derivazioni multiple (esistenza dell’asterisco nel dato del POSNR);

Aggiornare i dati in EAM, riportandoli alla rappresentazione corretta (dovrà essere coinvolto SAP per avere l’OK sulla bontà dell’aggiornamento) agendo sulla sequenza (X3SEQUENCE) e sul POSNR per adeguarli alla nuova rappresentazione (lettere multiple per il primo e asterisco per il secondo).

Non essendo disponibile un ambiente SAP di test con i dati allineati, la verifica delle bonifiche potrà essere eseguita solo in produzione.

## OP_256 – derivazioni miste aereo e cavo

Trattandosi dell’estensione di una funzionalità esistente e assumendo che tutte le derivazioni in parte o completamente in cavo siano state migrate, questo open point non comporta bonifiche sui dati. Verranno comunque eseguiti dei controlli a campione per verificare la coerenza dei dati migrati rispetto a quanto la nuova funzionalità potrà produrre.

## OP_254– Asset distinti da unire in doppia terna (sostegni) e asset da scindere in singola terna (campate)

Questo open point è stato necessario dividerlo in due, in quanto le due casistiche che devono essere affrontate non sono assimilabili alla medesima strategia, bensì devono essere sviluppate due procedure distinte. La seconda casistica si è chiarita soltanto a seguito dell’approfondimento con il business avvenuto il 25 marzo.

### OP_254 A– Confluenza di 2 campate non in doppia terna sullo stesso sostegno

Questo open point è già gestito nelle funzionalità dell’anagrafica, pertanto non sarà oggetto di nuovi sviluppi, ma comporterà una bonifica dei dati che sono stati caricati dall’ETL, al fine di rappresentare la confluenza di due campate singole verso un unico sostegno in doppia terna, attualmente migrato in EAM come due sostegni distinti.

Figura 90 - Confluenza di due campate singole nel medesimo sostegno in doppia terna

La bonifica dovrà ricondurre i due sostegni in un unico sostegno in doppia terna. L’individuazione delle linee e degli asset di tipo sostegno per i quali è necessario intervenire devono essere indicati dal business.

Nelle immagini seguenti si riporta un caso effettivo, individuato in produzione, relativo alle linee 22236B1e 21302A1.

Figura 91 - Due linee, entrambe con un sostegno che deve essere messo in doppia terna

Figura 92 - Sostegno linea rossa

Figura 93 - Sostegno linea verde

I due sostegni evidenziati nelle immagini precedenti, nella realtà sono un solo sostegno in doppia terna; quindi, gli asset 18530564 e 18580381 devono diventare un unico sostegno in doppia terna, le cui campate precedenti rimangono invece in singola terna.

Nelle immagini seguenti, riferite sempre al caso preso in considerazione, si mostra da un punto di vista grafico l’evoluzione delle informazioni per raggiungere il risultato atteso.

Figura 94 - Linee con sostegno duplicato

Figura 95 - Linee dopo l'accorpamento del sostegno duplicato

La procedura di accorpamento elaborerà e modificherà le informazioni (in base della regola della prevalenza, per cui nella doppia terna ha prevalenza la linea con il SAPID minore), in base seguente flusso:

Spostamento degli asset di 3° e 4° livello (ATTACCHI CONDUTTORE e ISOLATORE CONDUTTORE) dal sostegno della linea con SAPID maggiore al sostegno con SAPID minore (su quali coppie di sostegni agire è un informazione che deve essere fornita dal business).

Cancellazione del sostegno del punto precedente, compresi il 3° livello TELAIO e il 4° livello STRUTTURA, da cui è stato spostato il 3° e 4° livello ATTACCHI CONDUTTORE e ISOLATORE CONDUTTORE.

Quanto sopra dovrà essere eseguito con una procedura specifica, da sviluppare in EAM, che individuerà gli asset sui quali agire per mezzo di un’apposita tabella, alimentata dalle informazioni che dovrà fornire il business.

ATTENZIONE: le verifiche delle sincronizzazioni eseguite dal connettore a seguito delle bonifiche sull’anagrafica EAM potranno essere eseguite solo in produzione, non essendo disponibile un ambiente SAP per la verifica con i dati aggiornati.

### OP_254 B– Divisione di una campata in doppia terna in due campate in singola terna

In questo caso sono state inserite in EAM delle campate in doppia terna perché così erano state definite in SAP, in quanto gli utenti hanno scelto questa strategia: per evitare la creazioni di sostegni fittizi hanno usato una campata in doppia terna, unico modo per gestire in SAP anche il rispettivo sostegno in doppia terna. Però, nella realtà, la campata non è in doppia terna, ma sono due distinte in singola terna.

Nelle immagini della pagina seguente sono rappresentate la situazione attuale e il risultato che deve essere raggiunto.

Figura 96 - Campata in doppia terna da dividere

Figura 97 - effetto della divisione: due campate distinte

La procedura di divisione elaborerà e modificherà le informazioni in base seguente flusso:

Spostamento degli asset di 3° e 4° livello (CONDUTTORI e CONDUTTORE) dalla campata di una linea alla nuova campata che deve essere creata.

Creazione sotto la nuova campata dei nuovi asset FUNI DI GUARDIA e FUNE DI GUARDIA.

Quanto sopra dovrà essere eseguito con una procedura specifica, da sviluppare in EAM, che individuerà gli asset sui quali agire per mezzo di un’apposita tabella, alimentata dalle informazioni che dovrà fornire il business. Non avendo il business ancora fornito un caso effettivo sul quale approfondire lo studio, quanto esposto potrebbe richiedere degli aggiustamenti sulla base della consistenza dei dati reali.

ATTENZIONE: anche in questo caso le verifiche delle sincronizzazioni eseguite dal connettore a seguito delle bonifiche sull’anagrafica EAM potranno essere eseguite solo in produzione, non essendo disponibile un ambiente SAP per la verifica con i dati aggiornati.

## OP_77(W2) Gestione campi obbligatori EAM

Per garantire il monitoraggio degli attributi obbligatori, fondamentali per il corretto funzionamento dei sistemi integrati, verrà implementata una query ad hoc, gestita tramite AMS. Tale query estrarrà i campi di testata relativi a Linee e Stazioni e le specifiche tecniche soggette a controllo, applicando le regole di validazione ricavate sia dalle logiche MBI sia da quelle introdotte con il nuovo Data Model di anagrafica in EAM. Eventuali richieste di inserimento di nuovi campi saranno precedute da un’analisi degli impatti sulla logica di estrazione.

La query verrà gestita tramite AMS e verrà aperto un ticket con cadenza settimanale, al fine di avere il dettaglio di eventuali scarti e bonifiche.

Di seguito il file contenente l’elenco dettagliato delle specifiche tecniche e degli attributi da estrarre

# Gestione nuove società RETE 2 e Terna Gora

Nel contesto evolutivo dei sistemi EAM e SISNET è prevista l’introduzione di due nuove società:

RETE 2, che entrerà a far parte del Gruppo Terna nel corso del 2H 2025

Terna CRNA Gora, già appartenente al Gruppo Terna.

L’obiettivo è garantire che entrambe le società dispongano di un impianto anagrafico coerente con gli standard Terna e che i relativi utenti possano operare in modo corretto e sicuro all’interno dei sistemi.

## Società RETE 2

Le sedi tecniche della società RETE 2 saranno create ex novo, seguendo le regole e gli strumenti attualmente disponibili per la creazione di Linee.

Il primo livello verrà generato in SAP con stato ESER, contenente tutti i valori relativi alla nuova società, e successivamente trasmesso a EAM in stato DATT.

Da questo punto, l’utente potrà completare la struttura gerarchica e procedere con la valorizzazione delle specifiche tecniche.

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

In particolare, in SAP sono attualmente censite:

4 Sedi Tecniche Linee

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
