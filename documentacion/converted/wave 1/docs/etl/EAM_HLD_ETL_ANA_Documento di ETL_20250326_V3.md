# EAM_HLD_ETL_ANA_Documento di ETL_20250326_V3

|  | Functional Conversion Design Document |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Relazione: | FUNCTIONAL CONVERSION DESIGN DOCUMENT (FCDD) |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 1.0 | Primo rilascio. | 26/05/2023 |
| 1.1 | Piccole precisazioni e correzione di errori ortografici. | 31/05/2023 |
| 1.2 | Allineamento al documento TERNA_TOP Program_F2_ ATFSC_SAPS4-EAM_Specifiche_20230221_v.0.9 | 05/06/2023 |
| 1.3 | Revisione dei punti aperti con il team Connettore. | 06/06/2023 |
| 1.4 | Documento restituito con suggerimenti per il miglioramento da parte dell'ICT. | 15/06/2023 |
| 1.5 | Revisione a seguito dei commenti dell'ICT. | 26/06/2023 |
| 1.6 | Revisione eseguita da ICT | 13/07/2023 |
| 1.7 | Revisione a seguito dei commenti dell’ICT. | 18/07/2023 |
| 1.8 | Risposta ai commenti | 24/07/2023 |
| 1.9 | Modifiche finali | 01/08/2023 |
| 2.0 | Revisione eseguita da ICT | 02/08/2023 |
| 2.1 | Revisione a seguito dei commenti dell’ICT. | 02/08/2023 |
| 2.2 | Final ICT aproved version | 03/08/2023 |
| 2.3 | Revisione a seguito dei commenti dell’ICT. | 29/09/2023 |
| 2.4 | Revisione a seguito dei commenti del Business. | 19/10/2023 |
| 2.5 | Revisione degli oggetti creati nel capitolo 2 a seguito dei commenti del Business. | 26/10/2023 |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | RTI | 26/05/2023 |  |
| Riesaminato da: | RTI | 06/06/2023 |  |
| Approvato da: |  |  |  |

# Introduzione

Il presente documento ha l’obiettivo di dettagliare il progetto di conversione funzionale (Functional Conversion Design, FCD). La definizione del progetto di conversione funzionale è uno degli step della progettualità per l’implementazione della soluzione di asset management, come di seguito riportato.

Questo documento è una prima fase di progettazione di alto livello (high-level) in cui viene specificata la procedura di importazione SAP-EAM sulla base di una serie di assunzioni, regole e criteri di trasformazione discussi nella prima fase (wave) relativi al caricamento degli asset di tipo “LINEA”. Questo documento è stato redatto sulla base delle versioni dei documenti indicati nella sezione Documenti correlati. Potrebbero essere necessari aggiornamenti a questo documento qualora siano generate nuove versioni dei documenti di riferimento.

Tale documento potrà essere modificato durante lo sviluppo del progetto al fine di registrare eventuali modifiche necessarie, rilevate in fasi successive rispetto al disegno. Ulteriori dettagli tecnici saranno forniti successivamente tramite documento di low level design.

Tutti i documenti citati all’interno del presente documento sono disponibili e consultabili all’interno della seguente cartella Teams Documenti correlati.

## Definizione Functional Conversion Design

L'obiettivo principale di questa fase è realizzare un primo schema di alto livello (high level) in cui viene progettato il processo di conversione funzionale (Functional Conversion Design) SAP – EAM con riferimento alla prima fase (wave 1) relativa al caricamento degli asset di tipo “LINEA”. Viene specificata la procedura di import SAP-EAM partendo da una serie di presupposti, regole e criteri di trasformazione concordati.

L'importazione in EAM sarà effettuata per livelli, in modo che i livelli inferiori possano essere correttamente correlati ai livelli gerarchicamente superiori (tramite il campo parent o campi di relazione), nonché con altri elementi dello stesso livello, secondo le regole di attribuzione concordate nel documento (ID 1 Documento correlati, Mapping Anagrafica EAM AM_Dati Linee (BOZZA rev.7 250723).LLD).

## Descrizione Funzionale

Per creare i record in EAM sarà effettuata inizialmente un'importazione dei dati grezzi (raw data) da SAP in EAM in tabelle custom di staging (vedere la sezione 2). Questi dati includono i dati di linea descritti al punto 2 del presente documento. Successivamente, utilizzando questi dati grezzi SAP, verranno generati, tramite un processo prestabilito (vedere Figura 1 e 2), i seguenti dati finali in EAM:

I principali dati generici di un asset;

Valori della classificazione;

Valori degli attributi;

Relazioni gerarchiche.

Figura 1 - Processo di caricamento degli asset SAP su EAM.

Figura 2 - Schema di importazione dei raw data in EAM ed esecuzione del processo.

Al fine di garantire che la configurazione sia stata eseguita correttamente e che gli elementi da caricare siano conformi a quanto previsto e progettato, prima di iniziare a caricare le classificazioni e gli altri oggetti necessari, si effettuerà un primo controllo dell'ambiente, rivedendo gli oggetti, gli attributi, i domini, ecc. necessari per avviare il processo di caricamento. Tale verifica sarà effettuata in via generale selezionando a campione gli elementi da revisionare tra quelli sopra indicati. Nel diagramma seguente, l’illustrazione del processo:

Figura 3 - Rappresentazione Functional Conversion Design diagram preliminare alla creazione degli asset.

A questa prima fase segue il caricamento degli oggetti anagrafici. Per il caricamento della struttura completa, la procedura sarà svolta per livelli, in modo da garantire che per qualsiasi livello oggetto di caricamento, siano presenti tutti i livelli superiori da cui dipende.

Dopo aver caricato ciascuno dei livelli, verrà effettuato un controllo manuale sulla creazione, verificando che il numero di record creati in EAM corrisponda a quelli attesi sulla base dei dati SAP ed effettuando una revisione a campione delle informazioni di alcuni record. In questo modo, verrà costruita iterativamente la struttura completa.

Questo processo comporterà più lavoro manuale, ma consentirà di identificare rapidamente eventuali problemi durante il caricamento. In presenza di errori sarà possibile bloccare il caricamento al livello in cui si presenta l’errore e interrompere quindi i caricamenti successivi. Il processo finale di creazione degli asset, la loro posizione, le relazioni gerarchiche, la classificazione e il caricamento dei loro attributi, possono essere visualizzati nel diagramma seguente riportato in Figura 4:

Figura 4 - Rappresentazione Functional Conversion Design diagram che rappresenta il processo di creazione dell'asset.

Viene di seguito specificato l'ordine sequenziale di caricamento dei dati che deve essere seguito per creare tutti i record in modo coerente:

| Ordine di caricamento | Tipo di oggetto |
| --- | --- |
| # | Nome della classificazione |
| 1 | Linea |
| 2 | Assets di livello 2 |
| 3 | Assets di livello 3 |
| 4 | Caricamento degli attributi provenienti da SAP |
| 5 | Creazione dell'asset associato agli aggregatori |
| 6 | Caricamento delle informazioni sugli attributi che non provengono da SAP (non in questa wave) (per oggetti di livello 4) |

## Assunzioni

Prima di iniziare la trattazione della fase di costruzione, è necessario definire alcune assunzioni di base.

Il documento HLD potrebbe subire un impatto qualora una delle seguenti assunzioni venga meno (non è possibile determinare un impatto a priori, quindi dovrà essere analizzato per ogni punto eventualmente non soddisfatto).

Le assunzioni sono le seguenti:

Il processo di ETL è disegnato sulla base del Data Model Maximo, definito ad oggi (ID 1 Documento correlati, Mapping Anagrafica EAM Mapping Anagrafica EAM AM_Dati Linee (BOZZA rev.7 250723).LLD). Eventuali modifiche al Data Model potrebbero avere un impatto su questo documento.

Il processo di ETL è disegnato sulla base delle regole di trasformazione di asset e attributi tra SAP ed EAM definite dal BSN (ID 1 Documento correlati, Mapping Anagrafica EAM Mapping Anagrafica EAM AM_Dati Linee (BOZZA rev.7 250723).LLD). Eventuali modifiche potrebbero avere un impatto su questo documento.

Questo documento si basa sull'estrazione del campione dati (ID 8 ST_20230726_150000 e ID 9 CRT_20230721_130000), qualora emergano da ulteriori estrazioni SAP casistiche non previste in questo file, andrà valutato l'impatto sulla procedura di ETL.

La finalizzazione del disegno relativo al ETL è vincolata alla chiusura dei punti aperti tracciati su Data Model e regole finali di trasformazione.

I file di esportazione dati SAP non subiranno modifiche significative (formato, struttura, ecc.) rispetto a quelli utilizzati per la redazione del presente documento. (ID 6 TERNA_TOP Program_F2_ ATFSC_SAPS4-EAM_Specifiche_20230221_v.0.9; ID 8 ST_20230726_150000 e ID 9 CRT_20230721_130000).

Gli oggetti SAP che verranno caricati in EAM, così come la loro gerarchia, non subiranno modifiche significative rispetto a quanto utilizzato per la redazione di questo documento. Si vedano le gerarchie indicate nel presente documento (punti 1 e 3).

La struttura delle classificazioni e degli attributi non subirà modifiche significative rispetto a quella utilizzata per la redazione del presente documento (ID 1 Documento correlati, Mapping Anagrafica EAM AM_Dati Linee (BOZZA rev.7 250723).LLD).

Le regole di calcolo degli attributi verranno implementate in EAM prima del caricamento dei dati. In caso contrario, il valore di questi campi corrisponderà al valore ricevuto dall'estrazione SAP (e verrà fissato fino a quando il calcolo non sarà implementato in EAM).

Come prerequisito per la fase di costruzione, al momento dell'inizio del lavoro relativo alle informazioni contenute in questo documento, l'ambiente EAM sarà disponibile e configurato per essere utilizzato correttamente. Se non è disponibile, i lavori relativi a questo documento saranno oggetto di ritardo e potrebbe non essere possibile eseguirli.

Tutti gli asset caricati nella wave 1 verranno caricati nella posizione del sito (ID 7 documenti correlati, EAM_HLD_GEN_ANA_Documento Anagrafica di HLD_20230616_V2.1).

Dato che le linee in SAP hanno solo tre livelli gerarchici, le risorse e gli attributi di livello quattro in EAM saranno generati a partire dalle informazioni ricevute dagli aggregatori di livello tre. Ciò comporterà la presenza di alcuni attributi delle risorse di livello quattro che non potranno essere popolati nella prima ondata poiché non esistono in SAP, così come gli attributi calcolati dei livelli superiori che dipendono da essi. Questi attributi saranno caricati in EAM in ondate successive.

L'oggetto "sedi fittizie" esportato da SAP non verrà importato in EAM. La funzionalità delle sedi fittizie in SAP sarà sostituita con la funzionalità di gestione degli stati in EAM.

Le campate duplicate, così come tutti i loro elementi gerarchicamente inferiori in SAP duplicati, dovranno essere identificabili tramite le informazioni ricevute da SAP. In caso contrario, dovrà essere fornito un elenco di tutti i record duplicati da parte del BSN. Se le condizioni sopra non saranno soddisfatte, il caricamento di asset duplicati su EAM non potrà essere evitato.

Le informazioni necessarie per la corretta compilazione delle tabelle relative al connettore saranno presenti nei file dati estratti da SAP (ID 5 documenti correlati, EAM_HLD_GEN_ANA_Connettore_20230608_V1.8EAM_HLD_GEN_ANA_Connettore_20230608_V1.6 Punti 2.1 e 2.2).

# Caricamento Raw Data SAP

Il primo step del processo sarà il caricamento dei raw data SAP nelle tabelle di staging Maximo.

Queste tabelle verranno utilizzate per caricare direttamente le informazioni SAP nel sistema ed essere in grado di creare i record corrispondenti in Maximo.

Questi oggetti riporteranno le informazioni ricevute da SAP in formato SAP e non subiranno conversioni specifiche. Questi oggetti verranno utilizzati solo durante il processo di caricamento dei record in Maximo, memorizzandoli nel database come backup dei dati SAP originali. Nella tabella sono riportati gli Oggetti Maximo che saranno oggetto di creazione e che corrispondono alle anagrafiche SAP riportate nella seconda colonna:

Configurazione Oggetti:

| Oggetti Maximo | Scheda Dati SAP | Classificazione associata in Maximo | Azione |
| --- | --- | --- | --- |
| X3SEDITECHSAP | Sedi Tecniche | Questa tabella conterrà le informazioni sulle sedi tecniche inviate da SAP. | Creare |
| X3CARATSAP | Caratteristiche | Questa tabella conterrà le informazioni sulle caratteristiche inviate da SAP. | Creare |
| X3SAPMAXCLASS | Mapping | Memorizza il mapping esistente tra le caratteristiche di SAP e Maximo. | Creare |

Questi nuovi oggetti saranno definiti come attributi tecnici riportati nella colonna ‘Attributi’ della tabella in basso. Il dettaglio funzionale di questi attributi è contenuto nel file Documenti correlati ID 2 TERNA_Data Model_Oggetti Tecnici PM-EAM_11.

| Oggetti Maximo: | Attributi: |
| --- | --- |
| X3SEDITECHSAP | I dati verranno caricati nel sistema così come provengono da SAP e le regole di mappatura verranno definite come codice nel sistema. Tutti i campi sono presenti nei documenti correlati- ID 2 TERNA_Data Model_Oggetti Tecnici PM-EAM_11 Mapping Anagrafica EAM AM_Dati Linee (BOZZA rev.7 250723).LLD |
| X3CARATSAP |  |
| X3SAPMAXCLASS |  |

Configurazione Struttura Oggetto:

La struttura Oggetto viene utilizzata per caricare ed elaborare i dati nel sistema. Definisce la struttura e gli attributi dei dati, facilitando l'importazione e il caricamento massivo di informazioni relative agli asset. Questo oggetto è particolarmente utile quando è necessario migrare i dati da altri sistemi o eseguire aggiornamenti massivi del database. La struttura dell'oggetto fornisce il formato e la struttura necessari per garantire che i dati vengano caricati correttamente e siano disponibili per la gestione e l'analisi all'interno di IBM Maximo.

| Struttura dell'oggetto | Azione |
| --- | --- |
| X3SEDITECHSAP | Creare |
| X3CARATSAP | Creare |
| X3SAPMAXCLASS | Creare |

Configurazioni enterprise services:

L’enterprise services dispone di livelli di elaborazione dati che consentono di trasformare i dati ed applicare le regole dei processi aziendali ai dati prima che siano trasferiti negli oggetti di destinazione. Quando il messaggio di input è trasferito nella struttura oggetto, il messaggio XML deve essere nel formato dello schema oggetto. In questo modo, il framework di integrazione può quindi elaborare correttamente il messaggio.

| Enterprise services | Azione |
| --- | --- |
| X3SEDITECHSAP | Creare |
| X3CARATSAP | Creare |
| X3SAPMAXCLASS | Creare |

Sistema esterno:

| Sistema esterno | Azione |
| --- | --- |
| X3LOADINGRAWDATA | Creare |

Tutti i Servizi Enterprise della tabella precedente verranno associati ed attivati nel Sistema Esterno X3LOADINGRAWDATA.

2.1 Formato CSV:

Per il processo di caricamento dei dati raw provenienti da SAP, verrà gestita la modalità di caricamento tramite file .csv, che saranno importati tramite il tool MIF di Maximo.

Questo file sarà costituito da un'intestazione che indica il sistema esterno, il servizio aziendale, l'azione e la lingua. Nella seconda riga saranno riportati gli attributi (quelli estratti da SAP) e nelle righe successive i dati da caricare.

Di seguito viene riportato un esempio di file CSV per dati RAW SAP. (Assunzione 3):

| INTESTAZIONE | ACC_RAWDATA / C_TERNA_LOADING_CARATT_LINE / AddChange / IT |
| --- | --- |
| SECONDA LINEA | Nomi dei campi Maximo separati dal “TAB” (/). |
| LINEE SEGUENTI | Valori per ogni campo. |

Figura 5 – Template file di esempio

Si assume l'esistenza di un file separato per ogni tipo di dato proveniente da SAP, secondo la specifica (ID 6 Documento correlati, “TERNA_TOP Program_F2_ ATFSC_SAPS4-EAM_Specifiche_20230925_v.0.2”).

Esisterà in Maximo un record per ogni ID univoco relativo a ciascun file. Questo record verrà classificato con la classificazione Maximo, tracciata nel file di mapping (ID 1 documenti correlati, Mapping Anagrafica EAM AM_Dati Linee (BOZZA rev.7 250723).LLD). Alcuni di questi asset avranno un padre nel campo Parent standard e alcuni avranno una relazione. Tutto questo sarà definito nella sezione Regole di Processo.

Punto di Attenzione: eventuali soluzioni alternative di caricamento dei dati da SAP verso Maximo saranno dettagliate nel documento di analisi ‘Low Level Design’.

# Regole di Processo

Le regole di processo sono i criteri che devono essere soddisfatti per la corretta creazione di record SAP in Maximo. Queste regole di processo sono elencate di seguito:

Tutti gli asset creati saranno associati nel SITE, in coerenza con la struttura dei dati standard di IBM Maximo.

Il campo ID SAP verrà archiviato in un campo del Maximo Datamodel (ID 1 Documento correlati, Mapping Anagrafica EAM AM_Dati Linee (BOZZA rev.7 250723).LLD).

Tutti i dati verranno caricati in più passaggi sequenziali, permettendo di creare i record che verranno configurati come parents per i livelli successivi.

Saranno presenti record di secondo livello che risultano duplicati nei dati SAP. Esisterà quindi un campo SAP che metterà in relazione questi due record tra loro. In questo modo ne verrà caricato solo uno.

Sarà presente un campo SAP che consentirà di mettere in relazione ciascuno di questi record duplicati con il suo record di primo livello. In questo modo convertiremo i record duplicati di secondo livello in SAP in un unico record con più parent in EAM.

Durante questo caricamento dei dati (in wave 1), nessun asset di quarto livello sarà incluso nei dati. Questi verranno creati in base ai campi ricevuti da SAP con i relativi attributi. Verrà effettuato un secondo caricamento dei dati per riportare i valori di tutti questi attributi.

Se un asset di secondo livello, classificato come CAMPATA, ha il campo SAP STRNO_RIF non valorizzato, verranno effettuati i seguenti controlli: se ha l'attributo di lunghezza pari a 0 ed il sostegno di inizio è diverso al sostegno di fine, non verrà caricato in Maximo; quando invece il campo SAP STRNO_RIF è valorizzato verrà caricato bypassando il controllo.(CR controlli campate)

Se un asset di secondo livello, classificato come CAMPATA, ha il campo SAP STRNO_RIF non valorizzato, verranno effettuati i seguenti controlli: se ha l'attributo di lunghezza maggiore di 0 ed il sostegno di inizio è uguale al sostegno di fine, non verrà caricato in Maximo; quando invece il campo SAP STRNO_RIF è valorizzato verrà caricato bypassando il controllo (CR controlli campate)

Se un asset di secondo livello, classificato come TRATTA CAVO, ha l'attributo di lunghezza pari a 0 ed il terminale di inizio è uguale al terminale di fine, non verrà caricato in Maximo.

Nella tabella seguente sono elencate tutte le regole estratte dal file Mapping (ID 1 Documento correlati, Mapping Anagrafica EAM AM_Dati Linee (BOZZA rev.7 250723).LLD) che implicano la creazione di record (asset) per le linee e tutta la loro struttura gerarchica. Nella colonna “Rule” viene indicata: la cardinalità (X:X) e il formato dell’oggetto anagrafico (nncccxn-nnnxxxxx-C).

Le regole funzionali sono state censite, riviste e convalidate da Terna nel documento di mapping ID 1 Documento correlati, Mapping Anagrafica EAM AM_Dati Linee (BOZZA rev.7 250723).LLD.

| Asset type | Level | Parent | Rule |
| --- | --- | --- | --- |
| LINEA | 1 | - | 1:1 STRNO LINEA (nncccxn) |
| CAMPATA | 2 | LINEA (ASSETLOCRELATION) | 2:1* STRNO CAMPATA (nncccxn-nnnxxxxx) |
| CONDUTTORI | 3 | CAMPATA (ASSET) | 1:1 STRNO CONDUTTORI (nncccxn-nnnxxxxx-C) |
| CONDUTTORE | 4 | CONDUTTORI (ASSET) | 1:3X STRNO CONDUTTORI (nncccxn-nnnxxxxx-C) |
| FIBRA OTTICA (CONDUTTORI) | 4 | CONDUTTORI (ASSET) | 1:X STRNO CONDUTTORI (nncccxn-nnnxxxxx-C) Dove X=1 se CAMPATA attributo PMT_CAV_FO_AVV = S E X=0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| DISTANZIATORI | 4 | CONDUTTORI (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = 1 se attributo PMT_NUM_DISTANZIATORI_COND > 0 E X = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| DISTANZIATORI INTERFASE | 4 | CONDUTTORI (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = 1 se attributo PMT_NUM_INTERFASE_COND > 0 E X = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| ANTIROTAZIONALI | 4 | CONDUTTORI (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = 1 se attributo PMT_NUM_ANTIROTAZIONALI_COND > 0 E X = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| GIUNTO AEREO (CONDUTTORI) | 4 | CONDUTTORI (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = PMT_NUM_GIUNTI_COND |
| BARRETA PREFORMATA (CONDUTTORI) | 4 | CONDUTTORI (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = PMT_NUM_PREFORMATI_COND |
| ANTIVIBRANTI (CONDUTTORI) | 4 | CONDUTTORI (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = 1 se attributo PMT_NUM_ANTIVIBR_FASE > 0 E X = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| SFERE DI SEGNALAZIONE (CONDUTTORI) | 4 | CONDUTTORI (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = 1 se attributo PMT_NUM_SFERE_COND > 0 E X = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| DISSUASORI AVIFAUNA (CONDUTTORI) | 4 | CONDUTTORI (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = 1 se attributo PMT_NUM_DISSUASORI_COND > 0 E X = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| FUNI DI GUARDIA | 3 | CAMPATA (ASSET) | X:1 STRNO FUNE DI GUARDIA (nncccxn-nnnxxxxx-G) Dove X dipende dal parent CAMPATA attributo PMT_CAMP_TER_ASS come segue: X=1 se PMT_CAMP_TER_ASS compilata. X=2 se PMT_CAMP_TER_ASS non compilata. |
| FUNE DI GUARDIA | 4 | FUNI DI GUARDIA (ASSET) | X:Y STRNO FUNE DI GUARDIA (nncccxn-nnnxxxxx-G) Dove X dipende dal parent CAMPATA attributo PMT_CAMP_TER_ASS come segue: X=1 se PMT_CAMP_TER_ASS compilata. X=2 se PMT_CAMP_TER_ASS non compilata. e Y= PMT_NUM_CDG L'identificazione di ogni fune come "fune destra", "fune sinistra" o "terza fune" può essere effettuata tramite gli attributi SAP PMT_TIPO_CDG_DX, PMT_TIPO_CDG_SX e PMT_TIPO_CDG_3. |
| FIBRA OTTICA (CDG) | 4 | FUNI DI GUARDIA (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X ∈ [0,3] perché: Verrà creato un asset attribuito a fune DX se PMT_WRAP_CDG_DX = S Verrà creato un asset attribuito a fune SX se PMT_WRAP_CDG_SX = S Verrà creato un asset attribuito alla terza fune se PMT_WRAP_CDG_3 = S |
| GIUNTO AEREO (CDG) | 4 | FUNI DI GUARDIA (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = PMT_NUM_GIUNTI_CDG |
| BARRETTA PREFORMATA (CDG) | 4 | FUNI DI GUARDIA (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = A + B e A = PMT_NUM_PREFORMATI_CDG_DX B = PMT_NUM_PREFORMATI_CDG_SX Verrà creato un asset attribuito alla fune DX e un asset attribuito alla fune SX. |
| ANTIVIBRANTI (CDG) | 4 | FUNI DI GUARDIA (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = A + B e A = PMT_NUM_ANTIVIBR_CDG_DX B = PMT_NUM_ANTIVIBR_CDG_SX Verrà creato un asset attribuito alla fune DX e un asset attribuito alla fune SX. |
| SFERE DI SEGNALAZIONE (CDG) | 4 | FUNI DI GUARDIA (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = 1 se attributo PMT_NUM_SFERE_CDG > 0 E X = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| DISSUASORI AVIFAUNA (CDG) | 4 | FUNI DI GUARDIA (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = 1 se attributo PMT_NUM_DISSUASORI_CDG > 0 E X = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| ANTIROTAZIONALI FUNE DI GUARDIA | 4 | FUNI DI GUARDIA (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X = 1 se attributo PMT_NUM_ANTIROTAZIONALI_CDG > 0 E X = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| SOSTEGNO | 2 | LINEA (ASSETLOCRELATION) | X:1 STRNO SOSTEGNO (nncccxn-nnnxxxxx-S) Dove X dipende dal parent CAMPATA attributo PMT_CAMP_TER_ASS come segue: X=1 se PMT_CAMP_TER_ASS = blank oppure = STRNO della stessa Sede Tecnica campata. X=2 se PMT_CAMP_TER_ASS = STRNO di altra Sede Tecnica campata. |
| ATTACCHI CONDUTTORE | 3 | SOSTEGNO (ASSET) | X:1 STRNO SOSTEGNO (nncccxn-nnnxxxxx-S) Dove X dipende dal parent CAMPATA attributo PMT_CAMP_TER_ASS come segue: X=1 se PMT_CAMP_TER_ASS = blank oppure = STRNO della stessa Sede Tecnica campata. X=2 se PMT_CAMP_TER_ASS = STRNO di altra Sede Tecnica campata. |
| ARMAMENTO CONDUTTORE | 4 | ATTACCHI CONDUTTORE (ASSET) | X:3 STRNO SOSTEGNO (nncccxn-nnnxxxxx-S) Dove X dipende dal parent CAMPATA attributo PMT_CAMP_TER_ASS come segue: X=1 se PMT_CAMP_TER_ASS = blank oppure = STRNO della stessa Sede Tecnica campata. X=2 se PMT_CAMP_TER_ASS = STRNO di altra Sede Tecnica campata. Prima del caricamento dei dati è necessario fornire un elenco di eccezioni in cui X non è mappato in 3 elementi. |
| ISOLATORI CONDUTTORE | 4 | ATTACCHI CONDUTTORE (ASSET) | 1:3 STRNO ISOLATORI (nncccxn-nnnxxxxx-I) Prima del caricamento dei dati è necessario fornire un elenco di eccezioni in cui X non è mappato in 3 elementi. |
| TELAIO | 3 | SOSTEGNO (ASSET) | X:1 STRNO SOSTEGNO (nncccxn-nnnxxxxx-S) Dove X dipende parent CAMPATA attributo PMT_CAMP_TER_ASS come segue: X=1 se PMT_CAMP_TER_ASS = blank oppure = STRNO della stessa Sede Tecnica campata. X=2 se PMT_CAMP_TER_ASS = STRNO di altra Sede Tecnica campata. |
| STRUTTURA | 4 | TELAIO (ASSET) | X:1 STRNO SOSTEGNO (nncccxn-nnnxxxxx-S) Dove X dipende dal parent CAMPATA attributo PMT_CAMP_TER_ASS come segue: X=1 se PMT_CAMP_TER_ASS = blank oppure = STRNO della stessa Sede Tecnica campata. X=2 se PMT_CAMP_TER_ASS = STRNO di altra Sede Tecnica campata. |
| SEZIONATORI | 4 | TELAIO (ASSET) | X:Y STRNO SOSTEGNO (nncccxn-nnnxxxxx-S) Dove X dipende dal parent CAMPATA attributo PMT_CAMP_TER_ASS come segue: X=1 se PMT_CAMP_TER_ASS = blank oppure = STRNO della stessa Sede Tecnica campata. X=2 se PMT_CAMP_TER_ASS = STRNO di altra Sede Tecnica campata. Y= 1 se attributo PMT_NUM_SEZIONAT > 0 E Y= 0 altrimenti, in questo caso l’asset non verrà creato. L'asset avrà un attributo che specifica quanti SEZIONATORI ci sono. |
| NIDO AVIFAUNA | 4 | TELAIO (ASSET) | X:Y STRNO SOSTEGNO (nncccxn-nnnxxxxx-S) Dove X dipende dal parent CAMPATA attributo PMT_CAMP_TER_ASS come segue: X=1 se PMT_CAMP_TER_ASS = blank oppure = STRNO della stessa Sede Tecnica campata X=2 se PMT_CAMP_TER_ASS = STRNO di altra Sede Tecnica campata E Y = 1 se attributo PMT_PRESENZA_NIDO = A o PMT_PRESENZA_NIDO = N e Y = 2 se attributo PMT_PRESENZA_NIDO = AN |
| SEGNALAZIONE LUMINOSA OSTACOLO VOLO | 4 | TELAIO (ASSET) | X:Y STRNO SOSTEGNO (nncccxn-nnnxxxxx-S) dove X dipende dal parent CAMPATA attributo PMT_CAMP_TER_ASS come segue: X=1 se PMT_CAMP_TER_ASS = blank oppure = STRNO della stessa Sede Tecnica campata X=2 se PMT_CAMP_TER_ASS = STRNO di altra Sede Tecnica campata e Y = 1 se attributo PMT_SEGN_LUMIN = S E Y = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| DISPOSITIVO TLC | 4 | TELAIO (ASSET) | X:Y STRNO SOSTEGNO (nncccxn-nnnxxxxx-S) Dove X dipende dal parent CAMPATA attributo PMT_CAMP_TER_ASS come segue: X=1 se PMT_CAMP_TER_ASS = blank oppure = STRNO della stessa Sede Tecnica campata X=2 se PMT_CAMP_TER_ASS = STRNO di altra Sede Tecnica campata e Y = 1 se attributo PMT_IMP_TELECOM = S e Y = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| ISOLATORI FUNE DI GUARDIA | 4 | TELAIO (ASSET) | 1:X STRNO ISOLATORI (nncccxn-nnnxxxxx-I) Se PMT_TIPO_ISOL_CDG non è nullo X = PMT_NUM_CDG della FUNE DI GUARDIA (nncccxn-nnnxxxxx-G) con lo stesso parent campata come gli ISOLATORI. Se PMT_TIPO_ISOL_CDG è nullo X=0 in questo caso l’asset non verrà creato in EAM. |
| GIUNZIONE FIBRA OTTICA | 4 | TELAIO (ASSET) | 1:X STRNO SOSTEGNO (nncccxn-nnnxxxxx-S) Dove X = 1 se attributo PMT_GIUNTO_FO = S e X = 0 altrimenti, in questo caso l’asset non verrà creato in EAM. |
| ARMAMENTO FUNE DI GUARDIA | 4 | TELAIO (ASSET) | 1:X STRNO MORSETTERIA (nncccxn-nnnxxxxx-M) Dove X ∈ [0,3] perché: Verrà creato un asset attribuito alla fune DX se PMT_SIGLA_ARM_CDG_DX non è nullo Verrà creato un asset attribuito alla fune SX se PMT_SIGLA_ARM_CDG_SX non è nullo Verrà creato un asset attribuito alla terza 3 se PMT_SIGLA_ARM_CDG_3 non è nullo |
| ESTREMO CAVO | 2 | LINEA (ASSETLOCRELATION) | 1:1 STRNO TERMINALE (nncccxn-nnnxxxxx-H) (nncccxn-nnnxxxxx-K) e / o 1:1 STRNO CAMERA GIUNTI (nncccxn-nnnxxxxx-R) Con lo stesso parent TRATTA CAVO in SAP (Un ESTREMO CAVO in EAM per un TRATTA CAVO in SAP che è parent di un TERMINALE e/o di una CAMERA GIUNTI). |
| TERMINALI | 3 | ESTREMO CAVO (ASSET) | 1:1 STRNO TERMINALE (nncccxn-nnnxxxxx-H) (nncccxn-nnnxxxxx-K) |
| GIUNTI | 3 | ESTREMO CAVO (ASSET) | 1:1 STRNO CAMERA GIUNTI (nncccxn-nnnxxxxx-R) |
| GIUNTO CAVO | 4 | GIUNTI (ASSET) | 1:3 STRNO CAMERA GIUNTI (nncccxn-nnnxxxxx-R) Prima del caricamento dei dati è necessario fornire un elenco di eccezioni in cui X non è mappato in 3 elementi. |
| CAMERA ISPEZIONABILE | 4 | GIUNTI (ASSET) | L’esistenza dell’asset in EAM dipende dall’attributo PMT_TIPO_CAM_GIUNTO della CAMERA GIUNTI. La regola deve essere definita. |
| TRATTA CAVO | 2 | LINEA (ASSETLOCRELATION) | 1:1 STRNO TRATTA CAVO (nncccxn-nnnxxxxx) |
| CAVI | 3 | TRATTA CAVO (ASSET) | 1:1 STRNO CAVO (nncccxn-nnnxxxxx-V) |
| CAVO | 4 | CAVI (ASSET) | 1:3 STRNO CAVO (nncccxn-nnnxxxxx-V) Prima del caricamento dei dati è necessario fornire un elenco di eccezioni in cui X non è mappato in 3 elementi. |
| GIUNTO DI RIPARAZIONE | 4 | CAVI (ASSET) | 1:X STRNO CAVO (nncccxn-nnnxxxxx-V) Dove X = PMT_NUM_GIUNTI_RIP |

# Applicazione per l'esecuzione del processo

Una volta caricati i raw data SAP (capitolo 2), il processo proseguirà con le regole precedentemente definite attraverso l’utilizzo di un'applicazione personalizzata in EAM.

L'applicazione consentirà il caricamento successivo per livelli, il livello viene selezionato manualmente dall'utente ICT che gestisce il caricamento.

Il caricamento deve essere effettuato seguendo la struttura dei livelli. Viene fatto in questo modo per evitare di caricare livelli successivi senza aver effettuato una verifica del carico del livello precedente. Spetterà all'utente scegliere manualmente il livello che desidera caricare in ogni momento.

Le esecuzioni di questo processo verranno memorizzate nell'applicazione in formato elenco, con informazioni quali livello (o classificazione) caricato, utente, ora, record creati, ecc. In questo modo sarà quindi possibile tenere traccia dell'intero processo di caricamento.

# Open points

## OP valorizzazione attributi (OP 306)

A seguito di alcune verifiche, è stato richiesto di valorizzare gli attributi (LIN_RIF_ASSET_ARM_COND) degli isolatori conduttori (classificazione 1039) nei rispettivi armamenti conduttori collegati (classificazione 1038). Per garantire una corretta valorizzazione di tutti gli attributi, verrà introdotto un nuovo step nel processo ETL, che sarà eseguito separatamente.

## OP controlli campate (OP 255)

È stato richiesto di modificare il vincolo che escludeva gli asset di secondo livello (campate) in presenza di determinate condizioni (vedi par. “Regole di Processo”). Con l'aggiornamento, questi asset verranno ora caricati correttamente, a condizione che abbiano il campo SAP STRNO_RIF valorizzato.

# Dati da popolare per il connettore

Per il corretto funzionamento del connettore tra EAM e SAP, le tabelle X3SAPASSET e X3SAPTOMAXIMOASSET devono essere popolate con le informazioni elencate in questa sezione.

Queste tabelle e le relative informazioni sono definite nel documento (ID 5 Documenti correlati, EAM_HLD_GEN_ANA_Connettore_20230608_V1.8EAM_HLD_GEN_ANA_Connettore_20230608_V1.6). Di seguito è riportato un esempio delle informazioni che saranno presenti in queste tabelle:

X3SAPASSET:

X3SAPTOMAXIMOASSET:

Questo step sarà incluso come parte del processo di creazione dei record delineati nel documento. La descrizione dettagliata di questo processo sarà indicata nel documento di progettazione di basso livello. I dati creati dai processi indicati in questo documento saranno contrassegnati da un campo di tipo YORN (questo campo sarà definito nel modello di dati Maximo). I record saranno caratterizzati dal valore 1 nel processo di caricamento.

# Documenti correlati

| ID | DOCUMENTO |
| --- | --- |
| 1 | Mapping Anagrafica EAM AM_Dati Linee (BOZZA rev.7 250723).LLD |
| 2 | TERNA_Data Model_Oggetti Tecnici PM-EAM_11 |
| 3 | EAM_HLD_GEN_ANA_Connettore_20230608_V1.6 |
| 4 | TERNA_TOP Program_F2_ ATFSC_SAPS4-EAM_Specifiche_20230925_v.0.2 |
| 5 | EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V4 |
| 6 | ST_20230726_150000 |
| 7 | CRT_20230721_130000 |

# Glossario

| ID | Termini | Descrizione |
| --- | --- | --- |
| 1 | Record | È una piccola unità di archiviazione destinata a contenere un certo tipo di dati. |
| 2 | SITE | Un sito è una suddivisione di un'organizzazione che può tenere traccia dell'inventario e di altri dati separatamente dagli altri siti. Alcuni tipi di informazioni sono univoci per un sito e non sono visibili ad altri siti all'interno dell'organizzazione. |
| 4 | EAM | Enterprise asset management (EAM) è un processo di gestione del ciclo di vita delle risorse fisiche per massimizzarne l'utilizzo. |
| 5 | Dati grezzi (raw data) | Dati non elaborati (di SAP) |
| 6 | Struttura oggetti | Una struttura oggetto è il livello di dati comune utilizzato dai componenti del framework di integrazione per inviare e ricevere dati in Maximo® Asset Management. Una struttura oggetto consiste in uno o più oggetti di business correlati che definiscono il contenuto di un messaggio di integrazione. |
| 7 | Enterprise services | Un enterprise service è una pipeline per eseguire query su dati esterni e importare dati da un sistema esterno. |
| 8 | Sistema esterno | Un sistema esterno è un sistema interno de Maximo® Manage che interagisce con Maximo® Manage, sia come endpoint (posizione) a cui Maximo Manage invia i dati in uscita, sia come fonte da cui il software di asset management riceve i dati in entrata. Un sistema esterno può elaborare enterprise services in entrata, canali di pubblicazione in uscita o una combinazione di entrambi. |
| 9 | YORN | Campo Maximo che può essere popolato con 1 o 0. |
| 10 | Oggetto | Un oggetto Maximum è un'entità utilizzata per archiviare i dati. Questi dati sono memorizzati in una tabella in cui ogni colonna è un attributo del record da memorizzare. |

# Indice figure

Figura 1 - Processo di caricamento degli asset SAP su EAM. 4

Figura 2 - Schema di importazione dei raw data in EAM ed esecuzione del processo. 5

Figura 3 - Rappresentazione Functional Conversion Design diagram preliminare alla creazione degli asset. 5

Figura 4 - Rappresentazione Functional Conversion Design diagram che rappresenta il processo di creazione dell'asset. 6

Figura 5 – Carica file di esempio 11
