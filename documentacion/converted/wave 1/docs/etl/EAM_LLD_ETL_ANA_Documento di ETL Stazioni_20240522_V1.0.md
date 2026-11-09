# EAM_LLD_ETL_ANA_Documento di ETL Stazioni_20240522_V1.0

|  | ETL Stazioni – Documento di Low Level Design |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Relazione: | LOW LEVEL DESIGN DOCUMENT (LLDD) |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 1.0 | Primo rilascio RTI | 22/05/2024 |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | RTI | 22/05/2024 |  |
| Riesaminato da: | RTI |  |  |
| Approvato da: |  |  |  |

# Glossario

| Abbreviazione | Descrizione |
| --- | --- |
| HLD | High Level Design – Progettazione di alto livello |
| LLD | Low Level Design – Progettazione di basso livello |
| EAM | Enterprise Asset Management - Usato per indicare l’applicativo IBM Maximo |
| ETL | Extract-Transform-Load, processo di estrazione, trasformazione e caricamento dati |
| REST API | API di prodotto o custom esposta tramite interfaccia REST |
| Crontask | componente di Maximo che permette di eseguire e automatizzare attività a intervalli regolari |

# Introduzione

Il presente documento ha l’obiettivo di dettagliare la progettazione di basso livello (LLD) del processo di ETL per il caricamento degli oggetti in EAM in ambito Stazioni.

Questo documento riprende gli argomenti affrontati in fase di progettazione di alto livello (HLD) in cui viene specificata la procedura di importazione SAP-EAM sulla base di una serie di assunzioni, regole e criteri di trasformazione discussi nella prima fase (wave-1) relativi al caricamento degli asset di tipo STAZIONE ed è stato redatto sulla base delle versioni dei documenti indicati nella sezione Documenti correlati con particolare enfasi alle specifiche di mapping fornite dal Business per asset (Documento #1) e caratteristiche (Documento #2). Potrebbero essere necessari aggiornamenti a questo documento qualora siano generate nuove versioni di quelli di riferimento.

Potrà inoltre essere aggiornato durante lo sviluppo del progetto al fine di registrare eventuali modifiche necessarie, rilevate in fasi successive rispetto al disegno.

# Processo ETL

Il processo di ETL consiste in una prima fase di estrazione (o data transfer) che prevede l’estrazione dei dati SAP (sedi tecniche, equipment, caratteristiche e anagrafiche accessorie) oggetto di elaborazione da parte della procedura. Tale fase è realizzata mediante estrattori che partendo dalla base dati SAP esporteranno dati grezzi (raw data) in tabelle di interfaccia accessibili dallo schema del database EAM. I dati esportati saranno oggetto di un secondo step di trasformazione (o preimport) verso tabelle custom definite nello schema del database EAM. L’obiettivo di questa fase è di normalizzare e arricchire i dati con informazioni specifiche del data model EAM. Ad esempio, verrà identificata la classificazione EAM della sede tecnica/equipment in base a STRNO (ID univoco SAP) e RBNR (Profilo Catalogo). Infine, il processo prevede l’ultimo step di caricamento (o import) degli asset EAM.

Ciascuna fase è realizzata mediante un flusso dedicato in grado di operare su un gruppo di stazioni fornito come input. Di seguito viene mostrato un diagramma di flusso che rappresenta graficamente le fasi del processo di ETL appena descritte.

Il funzionamento generale del processo di ETL è stato implementato sfruttando gli Automation Script di EAM, ovvero una delle possibilità di customizzazione messe a disposizione da Maximo: nello specifico sono degli script in linguaggio python, alcuni dei quali vengono richiamati tramite REST API o Crontask per avviare il processo, altri vengono richiamati dai primi per gestire le varie fasi. Il popolamento dei dati in Maximo viene effettuato agendo direttamente sulle tabelle del database mediante query SQL anziché usare le API del prodotto: questo approccio ha il vantaggio di ridurre il tempo totale della procedura, da una parte evitando ulteriori query di controlli applicativi che avrebbe eseguito il framework (alcuni non necessari, altri gestiti direttamente dal processo di ETL) e dall’altra consentendo di eseguire operazioni massive.

Il processo di import è stato pensato per lavorare a gruppi di stazioni, il cui numero è un parametro richiesto al lancio e che viene stabilito per ottenere il miglior compromesso in termini di prestazioni, ad esempio un numero troppo alto porterebbe a tempi elevati di esecuzione delle query (con possibili errori di time-out), mentre un numero troppo basso (all’estremo 1) farebbe perdere il vantaggio di eseguire le operazioni in maniera massiva.

A ogni processo di import quindi vengono eseguite tutte le fasi, limitate a quel gruppo di stazioni: questo significa che in termini di caricamento di stazioni complete su EAM, l’ETL non sarà né orizzontale, inteso come all’inizio non ci sarà nessuna stazione e solo alla fine ci saranno tutte, né verticale, ossia verrà caricata interamente una stazione per volta, ma sarà un approccio ibrido, per cui si avranno N nuove stazioni complete per volta.

Di seguito una rappresentazione grafica esplicativa della modalità di caricamento dei dati.

Un singolo processo di import è gestito dall’automation script X3ETLSTZ_LAUNCHER, il quale si occupa di avviare in ordine gli script responsabili delle singole fasi, ossia X3ETLSTZ_DATATRANSFER per la fase di Estrazione, X3ETLSTZ_PREIMPORT per quella di Trasformazione e X3ETLSTZ_IMPORT per la fase di Caricamento. Esistono 2 modalità per avviare l’import:

una diretta ed estemporanea, richiamando il Launcher tramite REST API

automaticamente da un crontask schedulato, guidato da una tabella di pianificazione

Nei prossimi paragrafi verrà prima illustrato il funzionamento del singolo processo di import, in seguito verrà indicato come questo si inserisce in un contesto di import massivo o puntuale (modalità di avvio). Temi comuni a più componenti e/o fasi, come la tabella di pianificazione e monitoraggio, verranno accennati durante la descrizione del processo di import per poi essere ripresi, riepilogati e dettagliati ulteriormente in paragrafi dedicati.

# Processo di import

In questo paragrafo viene illustrato nel dettaglio il singolo processo di import che inizia con il componente Launcher e prosegue con l’esecuzione delle 3 fasi. L’ETL è pensato per eseguire a ogni esecuzione un import totale, ossia non è previsto che lavori per differenze (in modalità a delta); pertanto, ogni fase inizia con un’attività di cancellazione dei dati di import precedenti legati alle stazioni oggetto del caricamento corrente.

## Launcher

Il Launcher è il componente che si occupa di gestire un singolo processo di import di un gruppo di Stazioni in tutte le sue fasi ed è implementato con lo script X3ETLSTZ_LAUNCHER.

I parametri accettati sono:

etlnum: id del record di pianificazione, se passato i parametri rootstrno, importid ed extractionid vengono ignorati

rootstrno (obbligatorio senza etlnum): elenco degli strno delle stazioni separati da virgola (es. 31202C,31204B,31301A)

importid: id univoco dell'import, usato per associare diverse chiamate dello script allo stesso processo di import. In caso di API se non passato viene impostato al timestamp corrente

extractionid (obbligatorio per step data transfer senza etlnum): id univoco dell'estrazione SAP, usato per copiare i dati dal DB on premise

step_datatransfer (solo API): indica se eseguire lo step di DATATRANSFER (0 = non attivo, altrimenti attivo) (default: attivo)

step_preimport (solo API): indica se eseguire lo step di PREIMPORT (0 = non attivo, altrimenti attivo) (default: attivo)

step_import (solo API): indica se eseguire lo step di IMPORT (0 = non attivo, altrimenti attivo) (default: attivo)

A seconda della modalità di avvio cambiano i parametri necessari e anche le possibilità di configurazione: se richiamato direttamente come REST API è necessario passare l’elenco degli STRNO (identificativo SAP) delle Stazioni da importare ed è possibile specificare quali fasi eseguire, ad esempio perché si è verificato un errore in fase di preimport o import, per cui rilanciando il processo non è necessario riportare i dati dal db on premise. Se non espressamente indicato vengono eseguite tutte le fasi.

L’importid è un identificativo usato per raggruppare diverse esecuzioni del Launcher appartenenti ad uno stesso processo di import globale. Se ad esempio si vogliono importare 50 Stazioni a gruppi di 10, il Launcher verrà eseguito 5 volte e le 5 esecuzioni avranno in comune lo stesso importid. Questo consente di facilitare l’analisi degli scarti e dei tempi di esecuzione legati all’intero processo di import e non solo a quello legato al singolo gruppo di Stazioni.

L’etlnum, descritto più in dettaglio nel paragrafo Planner, rappresenta l’identificativo di un record di pianificazione presente nella tabella X3ETLSTZLOG. Nel caso in cui il processo venga avviato direttamente questo parametro è opzionale, se non viene passato il Launcher si occupa anche di creare la riga nella tabella di pianificazione.

Una volta raccolti e verificati tutti i parametri lo script avvia in ordine le 3 fasi di Estrazione (Data Transfer), Trasformazione (Preimport) e Caricamento (Import), creando per ognuna di esse una riga nella tabella X3ETLSTZLOG per tracciare principalmente ora di inizio e fine e l’esito dell’attività.

## Estrazione

La migrazione dei dati da SAP S/4 a Maximo inizia con l’estrazione dei dati da SAP e la copia in tabelle di frontiera in un db on premise, accessibili da entrambi i sistemi. Questa parte è in carico al team di SAP e non è oggetto del documento.

La fase di Estrazione o di Data Transfer è la prima ad essere avviata dal Launcher e si occupa di riportare i dati estratti da SAP dalle tabelle di frontiera a quelle di interfaccia nel db di EAM.

La fase è gestita dallo script X3ETLSTZ_DATATRANSFER e i parametri di cui ha bisogno sono:

stazStrno: elenco degli strno delle stazioni separati da virgola (es. 31202C,31204B,31301A)

extractionId: id univoco dell'estrazione SAP comune a tutte le tabelle di interfaccia a parità di flusso

A seguire verranno descritte le attività eseguite durante la fase, nell’ordine in cui sono riportate.

Pulizia dati import precedenti

La prima attività è quella di eliminare i dati dalle tabelle di interfaccia, caricati eventualmente durante import precedenti: in particolare verranno cancellate le caratteristiche di Sedi Tecniche ed Equipment dalla tabella X3CARATSAP_STZ, i dati di testata degli Equipment da X3EQUIPMENTSAP_STZ e quelli delle Sedi Tecniche da X3SEDITECHSAP_STZ.

Copia delle ST

Copia i dati di testata delle Sedi Tecniche dalla tabella di frontiera X3SEDITECHSAP_STZ_IF alla tabella di EAM X3SEDITECHSAP_STZ filtrando per extractionId e riportando i record per cui i primi 6 caratteri dell’STRNO corrispondono a uno dei valori passati col parametro stazStrno

Copia degli EQ

Copia i dati di testata degli Equipment dalla tabella di frontiera X3EQUIPMENTSAP_STZ_IF alla tabella di EAM X3EQUIPMENTSAP_STZ filtrando per extractionId e riportando i record per cui i primi 6 caratteri del TPLNR corrispondono a uno dei valori passati col parametro stazStrno

Copia delle caratteristiche delle ST

Copia i dati delle caratteristiche delle Sedi Tecniche dalla tabella di frontiera X3CARATSAP_STZ_IF alla tabella di EAM X3CARATSAP_STZ filtrando per extractionId e riportando i record per cui l’OBJNR corrisponde a una ST che fa parte delle Stazioni da importare

Copia delle caratteristiche degli EQ

Copia i dati delle caratteristiche degli Equipment dalla tabella di frontiera X3CARATSAP_STZ_IF alla tabella di EAM X3CARATSAP_STZ filtrando per extractionId e riportando i record per cui l’OBJECT_ID corrisponde a un EQ che fa parte delle Stazioni da importare

Segue la definizione delle tabelle di interfaccia sul database di EAM, quelle presenti nel database on premise hanno una struttura simile.

X3SEDITECHSAP_STZ

| Colonna | Descrizione | Tipo Dati | Lunghezza |
| --- | --- | --- | --- |
| BUKRS | Società | VARCHAR2 | 200 |
| BUTXT | Definizione della società o della ditta | VARCHAR2 | 200 |
| CITY | Località | VARCHAR2 | 200 |
| COUNTRY | Chiave paese/regione | VARCHAR2 | 200 |
| EQFNR | Campo di classificazione | VARCHAR2 | 200 |
| EXTRACTION_ID | ID Estrazione SAP | VARCHAR2 | 160 |
| FLTYP | Categoria della sede tecnica | VARCHAR2 | 200 |
| GEWRK | Centro di lavoro responsabile delle misure di manutenzione | VARCHAR2 | 200 |
| INBDT | Data in funzione dal | DATE | 7 |
| INGRP | Gruppo resp. pian. per servizio clienti e manutenzione | VARCHAR2 | 200 |
| INNAM | Nome gruppo responsabili pianificazione manutenzione | VARCHAR2 | 200 |
| IWERK | Div. di pian. della manutenzione | VARCHAR2 | 200 |
| IWERKNAME | Descrizione div. di pian. della manutenzione | VARCHAR2 | 200 |
| KOKRS | Controlling area | VARCHAR2 | 200 |
| KOSTL | Centro di costo | VARCHAR2 | 200 |
| KTEXT | Definizione generale | VARCHAR2 | 200 |
| LIVELLO | Livello sede tecnica | NUMBER | 22 |
| MSGRP | Impianto | VARCHAR2 | 200 |
| NAME1 | Descrizione 1 indirizzo | VARCHAR2 | 200 |
| NAME2 | Descrizione 2 indirizzo | VARCHAR2 | 200 |
| OBJNR | Numero oggetto | VARCHAR2 | 200 |
| PLTXT | Definizione della sede tecnica | VARCHAR2 | 200 |
| POSNR | Posizione | VARCHAR2 | 200 |
| POST_CODE1 | Codice di avviamento postale della città | VARCHAR2 | 200 |
| RBNR | Profilo catalogo | VARCHAR2 | 200 |
| RBNRX | Testo per catalogo di manutenzione | VARCHAR2 | 200 |
| REGION | Regione (stato federale, stato federato, provincia, contea) | VARCHAR2 | 200 |
| STREET | Via | VARCHAR2 | 240 |
| STRNO | Identificazione della sede tecnica | VARCHAR2 | 200 |
| STTXT | Stato Sistema | VARCHAR2 | 200 |
| STTXU | Stato Utente | VARCHAR2 | 200 |
| SUBMT | Materiale per tipo di costruzione dell'oggetto tecnico | VARCHAR2 | 200 |
| SWERK | Divisione di manutenzione | VARCHAR2 | 200 |
| TPLKZ | Codice struttura della sede tecnica | VARCHAR2 | 200 |
| TPLMA | Sede tecnica superiore | VARCHAR2 | 200 |
| TPLNR | Sede tecnica | VARCHAR2 | 200 |
| TXT_GEWRK | Descrizione breve | VARCHAR2 | 200 |
| TXT_TPLKZ | Testo del codice struttura per sedi tecniche | VARCHAR2 | 200 |
| TYPTX | Definizione di tipo tecnico | VARCHAR2 | 200 |
| WERGW | Divisione del centro di lavoro responsabile | VARCHAR2 | 200 |
| X3SEDITECHSAP_STZID | Identificativo univoco Maximo | NUMBER | 22 |
| ZZSOCP | Società proprietaria | VARCHAR2 | 200 |

.

X3EQUIPMENTSAP_STZ

| Colonna | Descrizione | Tipo Dati | Lunghezza |
| --- | --- | --- | --- |
| ANSDT | Data del cambio di indirizzo | DATE | 7 |
| ANSWT | Valore d'acquisto | NUMBER | 22 |
| BAUJJ | Anno di costruzione | VARCHAR2 | 16 |
| BAUMM | Mese di costruzione | VARCHAR2 | 8 |
| BRGEW | Peso lordo | NUMBER | 22 |
| BUKRS | Società | VARCHAR2 | 16 |
| BUTXT | Definizione della società o della ditta | VARCHAR2 | 200 |
| CITY | Località | VARCHAR2 | 160 |
| COUNTRY | Chiave paese/regione | VARCHAR2 | 12 |
| DATAB | Data inizio validità | DATE | 7 |
| DATBI | Data di fine validità | DATE | 7 |
| EARTX | Testo per il tipo di oggetto | VARCHAR2 | 80 |
| EQART | Tipo di oggetto tecnico | VARCHAR2 | 40 |
| EQFNR | Campo di classificazione | VARCHAR2 | 120 |
| EQKTX | Definizione oggetto tecnico | VARCHAR2 | 160 |
| EQTYP | Categoria equipment | VARCHAR2 | 4 |
| EQUNR | Numero attrezzatura | VARCHAR2 | 72 |
| EXTRACTION_ID | ID Estrazione SAP | VARCHAR2 | 160 |
| GEWEI | Unità di peso | VARCHAR2 | 12 |
| GEWRK | Centro di lavoro responsabile delle misure di manutenzione | VARCHAR2 | 32 |
| HEQUI | Equipment superiore | VARCHAR2 | 72 |
| HERLD | Paese/Regione di produzione | VARCHAR2 | 12 |
| HERST | Produttore del cespite | VARCHAR2 | 120 |
| INBDT | Prima data dell'entrata in funzione | DATE | 7 |
| INGRP | Gruppo resp. pian. per servizio clienti e manutenzione | VARCHAR2 | 12 |
| INNAM | Nome gruppo responsabili pianificazione manutenzione | VARCHAR2 | 200 |
| INVNR | Numero dell'inventario | VARCHAR2 | 100 |
| IWERK | Div. di pian. della manutenzione | VARCHAR2 | 16 |
| IWERK_DESC | Descrizione div. di pian. della manutenzione | VARCHAR2 | 200 |
| KOKRS | Controlling area | VARCHAR2 | 16 |
| KOSTL | Centro di costo | VARCHAR2 | 40 |
| KTEXT | Definizione generale | VARCHAR2 | 80 |
| MAPAR | Cod. comp. costrutt. | VARCHAR2 | 120 |
| NAME1 | Descrizione 1 indirizzo | VARCHAR2 | 204 |
| NAME2 | Descrizione 2 indirizzo | VARCHAR2 | 160 |
| POSNR | Posizione | VARCHAR2 | 16 |
| POST_COD | Codice di avviamento postale della città | VARCHAR2 | 40 |
| RBNR | Profilo catalogo | VARCHAR2 | 36 |
| RBNRX | Testo per catalogo di manutenzione | VARCHAR2 | 120 |
| REGION | Regione (stato federale, stato federato, provincia, contea) | VARCHAR2 | 12 |
| SERGE | Numero di serie del produttore | VARCHAR2 | 120 |
| SPART | Settore merceologico | VARCHAR2 | 8 |
| STREET | Via | VARCHAR2 | 240 |
| STTXT | Stato Sistema | VARCHAR2 | 16 |
| STTXU | Stato Utente | VARCHAR2 | 16 |
| SUBMT | Materiale per tipo di costruzione dell'oggetto tecnico | VARCHAR2 | 72 |
| SWERK | Divisione di manutenzione | VARCHAR2 | 16 |
| SWERK_DESC | Descrizione divisione di manutenzione | VARCHAR2 | 200 |
| TIDNR | Numero identificazione tecnico | VARCHAR2 | 100 |
| TPLNR | Sede tecnica | VARCHAR2 | 120 |
| TXT_GEWRK | Descrizione breve | VARCHAR2 | 160 |
| TYPBZ | Numero modello costruttore | VARCHAR2 | 80 |
| TYPTX | Definizione di tipo tecnico | VARCHAR2 | 120 |
| WERGW | Divisione del centro di lavoro responsabile | VARCHAR2 | 16 |
| X3EQUIPMENTSAP_STZID | Identificativo univoco Maximo | NUMBER | 22 |
| ZSOCPR | Società proprietaria | VARCHAR2 | 16 |

X3CARATSAP_STZ

| Colonna | Descrizione | Tipo Dati | Lunghezza |
| --- | --- | --- | --- |
| ATNAM | Caratteristica | VARCHAR2 | 160 |
| ATNAMTX | Descrizione caratteristica | VARCHAR2 | 160 |
| BOO_TYP | Valore booleano | NUMBER | 22 |
| CHAR_TYP | Valore alfanumerico | VARCHAR2 | 160 |
| DAT_TYP | Valore formato data | DATE | 7 |
| DEC_TYP | Valore numerico decimale | NUMBER | 22 |
| EXTRACTION_ID | ID Estrazione SAP | VARCHAR2 | 160 |
| KLASSE | Classe SAP | VARCHAR2 | 160 |
| NUM_TYP | Valore numerico intero | NUMBER | 22 |
| OBJECT_ID | Identificativo oggetto | VARCHAR2 | 160 |
| OBJNR | Numero oggetto | VARCHAR2 | 160 |
| UDM | Unità di misura | VARCHAR2 | 64 |
| UDM_TYP | Tipo di dato | VARCHAR2 | 40 |
| X3CARATSAP_STZID | Identificativo univoco Maximo | NUMBER | 22 |

## Trasformazione

La fase di Trasformazione o Preimport è la seconda ad essere avviata dal Launcher e si occupa di riportare i dati dalle tabelle di interfaccia ad altre tabelle (chiamate di interfaccia o staging step 2) che poi saranno la base per la fase di Caricamento. Nel fare quest’attività vengono eseguiti dei controlli di validità dei dati che possono portare alla produzione di scarti, sia a livello di Sedi Tecniche ed Equipment, sia a livello di singole caratteristiche. Vengono inoltre effettuate delle trasformazioni e integrazioni delle informazioni, come ad esempio l’aggiunta della colonna ROOTSTRNO (Identificativo della stazione) per identificare tutti i record legati ad una determinata stazione.

La fase è gestita dagli script X3ETLSTZ_PREIMPORT e X3ETLSTZ_PREIMPORT_PT2 a cui vengono passati i parametri:

stazStrno: elenco degli strno delle stazioni separati da virgola (es. 31202C,31204B,31301A)

importId: id univoco dell'import condiviso tra N chiamate legate allo stesso processo (es. 20240322161017)

A seguire verranno descritte le attività eseguite durante la fase, nell’ordine in cui sono riportate.

Pulizia dati import precedenti

Anche in questo caso la fase inizia eliminando gli eventuali dati caricati con import precedenti per ripartire da una situazione pulita. Le tabelle impattate da questo step sono quelle di staging step 2, ossia X3ETLSTSAPS2 per le Sedi Tecniche, X3ETLEQSAPS2 per gli Equipment e X3ETLCARATSAPS2 per le caratteristiche

Copia Sedi Tecniche da tabella di staging step 1 a quella di step 2

Con questa attività vengono riportate le righe delle Sedi Tecniche delle Stazioni da importare dalla tabella X3SEDITECHSAP_STZ alla tabella X3ETLSTSAPS2 e allo stesso tempo vengono arricchite con altre informazioni.

Viene aggiunto il ROOTSTRNO, ossia il codice identificativo della stazione a cui appartengono, i valori degli stati vengono convertiti seguendo il mapping

| Stato SAP | Stato Maximo |
| --- | --- |
| PROG | INAT |
| CANT | INAT |
| DATT | INAT |
| ESER | DATT |
| ATTV | ATTV |
| NADI | NADI |
| NAND | NAND |
| CEAL | CEAL |
| DEMO | DEMO |
| Altri valori | UNIDENTIFIED |

Inoltre, poiché a differenza delle Sedi Tecniche delle Linee l’informazione del Profilo Catalogo non è identificativa, ossia non è sufficiente a capire il tipo di elemento (STAZIONE, SEZIONE, SBARRA…), in questa fase vengono determinati sia il tipo che il codice di classificazione Maximo da applicare all’asset corrispondente alla ST, confrontando l’STRNO con delle regular expression ricavate dal file di mapping.

Ne vengono riportate alcune a titolo di esempio evidenziando in verde le regular expression, in giallo il tipo elemento e in turchese il percorso gerarchico della classificazione:

Tipo elemento

WHEN REGEXP_LIKE(STRNO, '^3[0-6][0-9]{3}[A-Z]$') THEN 'STAZIONE'

WHEN REGEXP_LIKE(STRNO, '^3[0-6][0-9]{3}[A-Z]-93-14[0-9\|A-Z\|-]{2}$') THEN 'IMPIANTI TECNOLOGICI'

Classificazione

WHEN REGEXP_LIKE(STRNO, '^3[0-6][0-9]{3}[A-Z]$') THEN (SELECT CLASSSTRUCTUREID FROM CLASSHIERARCHY ch WHERE ch.HIERARCHYPATH = 'STAZIONE')

WHEN REGEXP_LIKE(STRNO, '^3[0-6][0-9]{3}[A-Z]-93-14[0-9\|A-Z\|-]{2}$') THEN (SELECT CLASSSTRUCTUREID FROM CLASSHIERARCHY ch WHERE ch.HIERARCHYPATH = 'STAZIONE \ STRUTTURE E IMPIANTI TECNOLOGICI \ IMPIANTI TECNOLOGICI')

Copia Equipment da tabella di staging step 1 a quella di step 2

In maniera analoga vengono riportate e arricchite le righe degli Equipment dalla tabella X3EQUIPMENTSAP_STZ alla X3ETLEQSAPS2.

In questo caso non c’è trascodifica degli stati poiché sono gli stessi e il ROOTSTRNO viene calcolato prendendo i primi 6 caratteri della colonna TPLNR che contiene il codice della Sede Tecnica a cui è collegato l’equipment; sempre a partire dal TPLNR viene ricavato il PARENTSTRNO, ovvero il codice SAP dell’asset padre a cui deve essere collegato l’asset corrispondente all’equipment, prendendo al più i primi 14 caratteri, questo perché nel data model EAM non viene riportato il quarto livello SAP, pertanto gli equipment che su SAP generalmente si trovano al quinto livello vengono riportato sul quarto di EAM. Sarà poi il Connettore in fase di sincronizzazione EAM-SAP ad eseguire l’operazione inversa riportandoli al quinto.

Vengono infine determinati tipo elemento e codice di classificazione Maximo in maniera simile a quella per le Sedi Tecniche, con la differenza che in più viene preso in considerazione l’RBNR (Profilo Catalogo) e che le regular expression vengono validate sul TPLNR poiché il codice identificativo degli equipment (EQUNR) non ha contenuto informativo.

Update classstructureid delle ST di tipo TRASFORMATORE

Determinare i classstructureid (codici della classificazione Maximo) delle ST di tipo TRASFORMATORE segue una logica particolare, ossia in base al tipo di Equipment presenti al quinto livello SAP vengono mappati su classificazioni diverse, seguendo la regola:

| Profilo Catalogo Equipment | Classificazione |
| --- | --- |
| PMTEQT3 PMTEQTA PMTEQTD PMTEQTG PMTEQTI | TRASFORMATORE |
| PMTEQTB | BOOSTER |
| PMTEQTO | TRASFORMATORE AT/BT |
| PMTEQTS | PHASE SHIFTING TRANSFORMER |

Check ST non identificate come SAPCLASS

Durante la fase di Preimport e in alcuni casi anche in fase di Import vengono fatti dei controlli sui dati per verificare che rispettino le regole di mapping e il data model EAM, quando il controllo non è superato viene tracciato uno scarto per poter essere analizzato al termine dell’import e il dato non viene caricato in Maximo. Ulteriori dettagli e un riepilogo della gestione degli scarti sono presenti nel paragrafo dedicato Scarti.

L’azione di ricavare la SAPCLASS (tipo di elemento SAP) fatta in uno dei passaggi precedenti, è fondamentale per sapere quali regole di mapping applicare in fase di Import; pertanto, le Sedi Tecniche che non soddisfano alcuna regular expression e che quindi non sono state identificate, non sono accettate e vengono scartate.

Check ST non identificate come classificazione

Allo stesso modo se non è stato possibile determinare come classificare l’asset corrispondente alla Sede Tecnica, questa viene tracciata come scarto.

Check EQ non identificati a livello di SAPCLASS e classificazione

Gli stessi controlli fatti per le Sedi Tecniche vengono eseguiti sugli Equipment.

Check Scarti per multivalore ST

In SAP c’è la possibilità di inserire molteplici valori per la stessa caratteristica di una Sede Tecnica, cosa non prevista nel Data Model di Maximo. Non esistono regole per ricondurre più valori SAP in un unico valore EAM per cui se per una caratteristica è presente più di un valore nessuno di questi viene riportato in Maximo e vengono tracciati come scarti. L’attività prevista è la segnalazione di questi casi al Business che potrà bonificare i dati in SAP prima dell’estrazione finale oppure inserire i valori in Maximo post go live (in questo caso sarà il Connettore poi a riallineare i sistemi).

Copia caratteristiche delle ST da tabella di staging step 1 a quella di step 2

Con questo passaggio vengono riportate e arricchite del ROOTSTRNO le righe delle caratteristiche delle Sedi Teniche dalla tabella di interfaccia X3CARATSAP_STZ alla X3ETLCARATSAPS2, escludendo i casi di caratteristica multivalore.

Check Scarti per multivalore equipment e copia caratteristiche da tabella di staging step 1 a quella di step 2

Vengono eseguite per le caratteristiche degli Equipment le stesse attività di controllo, copia e arricchimento descritte per le Sedi Tecniche.

Rimuove le caratteristiche duplicate su più classi

Su SAP è possibile associare a ST ed EQ più classi, ognuna delle quali porta con sé un elenco di caratteristiche. Nel caso in cui la stessa caratteristica sia presente in più classi, l’utente da interfaccia ne vede e gestisce una sola, ma in fase di estrazione vengono prodotte più righe che hanno stesso nome della caratteristica, stesso valore, ma classe diversa. In questo passaggio viene effettuata un’azione di deduplicazione, lasciando per questi casi una sola riga.

Check valori ST indisponibilità

Alcune caratteristiche contengono come valore il riferimento a una Sede Tecnica di indisponibilità: queste sono riportate all’interno di una tabella di anagrafica dedicata (X3SEDIINDISP) come step del workflow dell’ETL Linee (i dati sono in comune) e a regime allineati tramite l’invocazione da parte di SAP in un web service esposto da Maximo. Nel caso in cui i valori fanno riferimento a un codice di ST di indisponibilità non presente in quella tabella, vengono tracciati come scarti e svuotati per non essere riportati in Maximo.

Check formato caratteristiche CHAR che sono mappate su attributi NUMERIC

Ci sono casi in cui le regole di mapping fanno confluire diverse caratteristiche SAP nello stesso attributo di classificazione EAM: quando il tipo di dato Maximo è numerico e quello di una o più delle caratteristiche SAP è alfanumerico è necessario effettuare un controllo sui valori per essere sicuri che possano essere convertiti in numeri. Quando la verifica fallisce il valore viene tracciato come scarto e svuotato.

Trascodifica dei valori di alcune caratteristiche da A,B,C a 1,2,3

Per alcune caratteristiche come la PMT_T329 “AVV. DI INSERIM. CFT (A/B/C)” dell’equipment TRASFORMATORE A 3 AVVOLGIMENTI è richiesta una trascodifica dei valori

| Valore SAP | Valore Maximo |
| --- | --- |
| A | 1 |
| B | 2 |
| C | 3 |

Il connettore effettuerà l’operazione di trascodifica inversa.

Check coerenza saplevel con parent

Questo controllo verifica che il livello SAP della Sede Tecnica sia coerente con quello della Sede Tecnica padre, ossia che il SAPLEVEL della ST sia uguale a quello del padre + 1, altrimenti viene scartata.

Ad esempio, la ST con STRNO 31304A-54-1024-B e TPLMA 31304A-54 non sarebbe valida perché il livello della ST è 4 e quello del padre è 2.

Check coerenza strno con parent

Il controllo verifica che l’STRNO della Sete Tecnica sia coerente con quello della Sede Tecnica padre (TPLMA), ovvero che la parte iniziale coincida con quello del padre, altrimenti viene scartata.

Ad esempio, la ST con STRNO 31208A-54-17D1-B e TPLMA 31208A-54-17D2 non sarebbe valida perché la parte del terzo livello non corrisponde.

Check nuovo parent per IMPIANTO DI TERRA

L’elemento IMPIANTO DI TERRA da specifica deve essere collocato su EAM come quarto livello sotto al percorso “STAZIONE \ STRUTTURE E IMPIANTI TECNOLOGICI \ TERRENI E INFRASTRUTTURE”, mentre su SAP si trova come terzo livello sotto a “STAZIONE \ CONTROLLO GENERALE IMPIANTO”.

In questo passaggio viene fatto un doppio controllo:

viene verificato che esista almeno una ST di tipo TERRENI E INFRASTRUTTURE valida per essere usata come nuovo asset padre dell’asset IMPIANTO DI TERRA

viene verificato che ne esista al più una sola, poiché altrimenti non sarebbe possibile scegliere automaticamente sotto quale asset posizionarlo

I casi che non superano entrambi i controlli vengono tracciati come scartati e non riportati in EAM, mentre per quelli validi viene aggiornato il valore della colonna TPLMA (Codice Sede Tecnica superiore) con il valore del nuovo padre per essere usato nella fase di Import.

Check valori BAUJJ, BAUMM

BAUJJ e BAUMM sono due attributi di testata degli Equipment che rappresentano rispettivamente anno e mese di costruzione che devono essere uniti su Maximo in un attributo di classificazione di tipo data. In questo passaggio vengono validati i valori degli attributi SAP per essere sicuri di poter fare la conversione senza errori. I casi non validi vengono tracciati come scarti e non riportati in EAM.

Check valori TYPBZ

TYPBZ è un attributo di testata degli Equipment e rappresenta il “Numero modello costruttore” per cui su Maximo è stata definita una tabella di anagrafica che servirà come dominio di selezione dei valori. In fase di ETL deve essere verificato che i valori estratti da SAP siano presenti in questa tabella, altrimenti vengono tracciati come scarti e non riportati in Maximo.

Check POSNR senza mapping

POSNR è un attributo di testata degli Equipment che rappresenta la Posizione e che in fase di ETL deve essere trascodificato in 2 diversi attributi di testata degli asset che sono X3RIFERIMENTOFASE e X3FUNZIONEELEMENTO.

La trascodifica viene effettuata facendo una ricerca del valore del POSNR SAP nella tabella di Maximo X3TRASCODIFICAPSNR per il percorso di classificazione dell’asset corrispondente all’equipment.

I casi in cui non c’è una corrispondenza nella tabella per questi 2 valori vengono tracciati come scarti e l’informazione non viene riportata in Maximo.

Applica i valori alternativi

Per alcune caratteristiche è stato fornito un criterio di mapping alternativo, ossia in un attributo di classificazione di Maximo va riportato il valore di una caratteristica SAP o in alternativa se questo dovesse essere vuoto, il valore di un’altra caratteristica SAP.

Ad esempio, per gli equipment di tipo TA AT ISOLATO SF6 va riportato nell’attributo EAM STZ_CLASSE_NUCLEO_1 il valore della caratteristica PMT_AE12, ma se vuoto va riportato quello di PMT_AE14.

In questo passaggio per semplificare l’attività di caricamento vengono riportati i valori delle caratteristiche alternative in quelle principali che sono vuote, così da ricondurre questi casi alle regole lineari di corrispondenza valori usate per le altre caratteristiche.

Un caso a parte è costituito dalle regole di mapping per l’attributo EAM STZ_TIPO_TRASFORMATORE, poiché le caratteristiche principali e alternative su SAP hanno help valori diversi, per cui oltre ad applicare il criterio descritto va effettuata anche una trascodifica dei valori.

| Valore alternativo | Valore principale |
| --- | --- |
| A | AUTOTRASFORMATORE |
| T | TRASFORMATORE |

## Caricamento

La fase di Caricamento o Import è l’ultima del processo di import e a partire dai dati preparati dalle fasi precedenti si occupa di creare gli asset, valorizzare gli attributi delle classificazioni secondo le regole di mapping e popolare le tabelle accessorie di Maximo, come ad esempio quella dello storico degli stati o dei legami gerarchici tra asset.

La fase è gestita dallo script di automazione X3ETLSTZ_IMPORT al quale vengono passati i parametri:

stazStrno: elenco degli strno delle stazioni separati da virgola (es. 31202C,31204B,31301A)

importId: id univoco dell'import condiviso tra N chiamate legate allo stesso processo (es. 20240322161017)

A seguire verranno descritte le attività eseguite durante la fase, nell’ordine in cui sono riportate.

Pulizia dati import precedenti

Come le altre fasi anche questa inizia ripulendo eventuali dati del gruppo di stazioni corrente caricati da import precedenti, in questo caso le tabelle impattate sono:

ASSETSPECHIST: contiene lo storico degli attributi delle classificazioni

ASSETSPEC: tabella degli attributi delle classificazioni

ASSETTRANS: contiene lo storico degli spostamenti degli asset

PLUSCASSETSTATUS: è lo storico degli stati degli asset

ASSETANCESTOR: tabella dei rapporti gerarchici tra asset

A_ASSET: è dove vengono tracciate le modifiche ai valori degli attributi soggetti a monitoraggio (tabella di audit)

LONGDESCRIPTION: contiene i valori delle descrizioni estese, usate in questo caso per le note

X3SAPTOMAXOBJECT: tabella usata dal Connettore come legame tra l’anagrafica Maximo e l’anagrafica SAP

X3OBJECTCHANGE: traccia gli eventi generati a partire dalle azioni degli utenti sull’anagrafica EAM che il Connettore elabora per riflettere le modifiche su quella SAP

SERVICEADDRESS: contiene gli indirizzi delle Stazioni

ASSET: è la tabella principale degli asset

X3SAPASSETDATA: contiene le righe di dettaglio delle Sedi Tecniche e degli Equipment (caratteristiche e attributi di testata) che il Connettore usa per sincronizzare i dati di su SAP

X3SAPASSET: tabella principale dell’anagrafica SAP usata dal Connettore per tenere allineato il sistema integrato con Maximo

Creazione asset

Come fase di creazione vera e propria per prima cosa vengono inserite le righe nella tabella ASSET a partire dalle 2 tabelle di interfaccia di step 2 X3ETLSTSAPS2 e X3ETLEQSAPS2, sfruttando le informazioni riportate e ricavate dalle fasi precedenti, come la classificazione per determinare il tipo di asset; vengono presi in considerazione solo i record che non sono stati tracciati come scarti.

La creazione viene effettuata eseguendo diverse query, una per ogni livello delle Sedi Tecniche, una per gli Equipment ed altre per casi speciali descritti più avanti; gli asset creati in fase di ETL sono riconoscibili da quelli che saranno creati direttamente in Maximo perché hanno il prefisso “S” (che sta per SAP).

Le principali informazioni riportate dalle tabelle di staging e dalle creazioni dei livelli superiori sono:

| Attributo Asset | 1 Livello ST | 2 Livello ST | 3 Livello ST | Equipment |
| --- | --- | --- | --- | --- |
| ASSETNUM | Generato automaticamente concatenando la lettera S e un numero progressivo preso dalla sequenza x3etlassetnumseq | = (si indicherà con il simbolo = quando vale quanto riportato per il livello superiore) | = | = |
| PARENT | Vuoto | Assetnum del livello superiore | = | = |
| DESCRIPTION | PLTXT | = | = | EQKTX |
| CLASSSTRUCTUREID | Codice di classificazione ricavato in fase di Preimport | = | = | = |
| STATUS | STTXU (Stato Utente) rimappato durante la fase di Preimport | = | = | STTXU |
| ANCESTOR | Vuoto | Assetnum della stazione | = | = |
| X3SAPID | STRNO (Codice ST) | = | = | EQUNR (Codice EQ) |
| X3SWERK | SWERK (Divisione di manutenzione) | Vuoto | = | = |
| X3INGRP | INGRP (Gruppo resp. pian. per servizio clienti e manutenzione) | Vuoto | = | = |
| X3ZZSOCP | ZZSOCP (Società proprietaria) | Vuoto | = | = |
| X3MSGRP | MSGRP (Impianto) | Vuoto | = | = |
| X3EQFNR | EQFNR (Campo di classificazione) | = | = | Vuoto |
| X3BUKRS | BUKRS (Società) | Vuoto | = | = |
| X3KOSTL | KOSTL (Centro di costo) | Vuoto | = | = |
| X3GEWRK | GEWRK (Centro di lavoro responsabile delle misure di manutenzione) | Vuoto | = | = |
| X3KOKRS | KOKRS (Controlling area) | Vuoto | = | = |
| LOADED | Viene impostato a 1 al termine dell’import se non sono presenti scarti | Vuoto | = | = |
| EQ1 | STRNO della stazione, serve a individuare facilmente tutti gli asset appartenenti alla stessa stazione | = | = | = |

Le ST di tipo IMPIANTO DI TERRA fanno eccezione e vengono create come asset di quarto livello, anche in questo caso sfruttando le trasformazioni fatte durante lo step di Preimport.

Creazione e associazione Indirizzi di Servizio

La creazione dei record per gli indirizzi delle Stazioni è l’unico caso gestito con le API di Maximo anziché con query dirette al database per sfruttare per comodità la generazione automatica dei codici identificativi ed è un’attività fatta esclusivamente per i primi livelli, quindi per gli asset di tipo STAZIONE.

Di seguito il mapping delle informazioni tra Maximo e SAP:

| Attributo Maximo | Attributo ST SAP |
| --- | --- |
| DESCRIPTION | NAME1 |
| ADDRESSLINE2 | NAME2 |
| CITY | CITY |
| COUNTRY | COUNTRY |
| REGIONDISTRICT | REGION |
| STREETADDRESS | STREET |
| POSTALCODE | POST_CODE1 |

Creazione asset VARIATORE e SISTEMA DI RAFFREDDAMENTO

In ambito Stazioni c’è una corrispondenza tra oggetti (ST ed EQ) SAP e Asset Maximo, ad eccezione di alcune tipologie di Equipment che sono mappati con un numero variabile di asset che va da 1 a 3. Gli Equipment in questione sono BOOSTER, REATTORE, TRASFORMATORE A 3 AVVOLGIMENTI, TRASFORMATORE AT/MT, TRASFORMATORE DI GRUPPO, TRASFORMATORE DI INTERCONNESSIONE e TRASFORMATORE MT/MT, per i quali viene creato sempre un asset di tipo MACCHINA (con classificazione specifica) e in aggiunta quando a seconda dei casi determinate caratteristiche su SAP sono valorizzate o riportano il valore SI vengono creati anche asset di tipo VARIATORE e/o SISTEMA DI RAFFREDDAMENTO, sempre con classificazioni specifiche in base al tipo di equipment.

Aggiornamento flag CHILDREN

Completate le creazioni degli asset viene aggiornato il valore del flag CHILDREN che su Maximo viene impostato a 1 quando esiste almeno un asset figlio

Gestione campo Note

Le Note di SAP vengono riportate in una tabella dedicata di Maximo chiamata LONGDESCRIPTION usata per l’appunto quando si vogliono inserire descrizioni estese. Contestualmente per gli asset che contengono note viene impostato il flag HASLD a 1 come da standard Maximo.

Popolamento tabelle accessorie Maximo

Avendo scelto di implementare l’ETL delle Stazioni con query dirette al database per i motivi descritti in questo documento, è necessario gestire puntualmente anche i dati in tutte quelle tabelle che ruotano intorno agli Asset. Viene quindi eseguita una serie di query che hanno la finalità di emulare il comportamento che il sistema avrebbe avuto sfruttando le API di Maximo e vanno a popolare alcune tabelle che vengono riportate solo come elenco a titolo di completezza, poiché non hanno una grande rilevanza per la comprensione del funzionamento dell’ETL:

A_ASSET

ASSETTRANS

ASSETANCESTOR

PLUSCASSETSTATUS

Creazione attributi di classificazione

In questo passaggio vengono create massivamente tutte le righe degli attributi delle classificazioni per ogni asset nella tabella ASSETSPEC: i valori verranno popolati con le query successive che applicano le regole di mapping.

Applicazione regole di mapping per valori attributi

In fase di analisi del file di mapping delle caratteristiche/attributi SAP con attributi EAM sono state identificate alcune logiche comuni, le quali sono state poi trasformate in regole e implementate nella forma di query di update.

Le query vengono alimentate dalla tabella X3ETLSTZRULES che ha la seguente struttura:

| Colonna | Descrizione | Tipo Dati | Lunghezza |
| --- | --- | --- | --- |
| CLASSIFICATIONID | Nome della classificazione dell’asset su cui si deve mappare la caratteristica SAP nei casi in cui può esserci ambiguità (es. equipment che si mappano su 1-3 asset come TRASFORMATORE A 3 AVVOLGIMENTI) | VARCHAR2 | 768 |
| MAXNAME | Nome attributo Maximo | VARCHAR2 | 200 |
| RULE | Tipo di regola da applicare | VARCHAR2 | 200 |
| SAPCLASS | Tipo elemento SAP per cui si applica la regola (es. STAZIONE, SEZIONE, SBARRA…) | VARCHAR2 | 200 |
| SAPNAME | Nome caratteristica SAP | VARCHAR2 | 200 |
| SAPTYPE | Tipo di dati SAP, serve a capire da quale colonna della tabella delle caratteristiche vanno letti i valori | VARCHAR2 | 40 |
| STRNOIDX | Numero massimo di caratteri dell’STRNO della ST da usare come collegamento all’X3SAPID dell’asset da usare per il mapping. Viene usato ad esempio per riportare i valori di una ST di 4 livello su un asset di 3 livello dato che i quarti non fanno parte del Data Model EAM | NUMBER | 22 |
| X3ETLSTZRULESID | Identificativo univoco Maximo | NUMBER | 22 |

Tra i documenti correlati è presente anche il file che contiene l’elenco di tutte le regole di mapping applicate durante il processo di ETL Stazioni (Documento #3).

Gestione regole SameValue

La regola è usata per riportare i valori delle caratteristiche SAP delle Sedi Tecniche negli attributi di classificazione corrispondente. Ogni regola si applica a una specifica SAPCLASS (tipo elemento SAP) e il valore riportato segue queste logiche:

Se l’attributo EAM è di tipo ALN o TABLE

Se la caratteristica SAP è di tipo ALN allora riporta lo stesso valore presente nella colonna CHAR_TYP

Se è di tipo INT allora riporta il valore di NUM_TYP convertito in stringa

Se è di tipo DEC allora riporta il valore di DEC_TYP convertito in stringa

Se è di tipo NUMERIC

Se l’attributo SAP è INT allora corrisponde a NUM_TYP

Se è DEC corrisponde a DEC_TYP

Se l’attributo EAM è di tipo DATE allora corrisponde al valore SAP DAT_TYP

Gestione regole SameValueChildren

In questo caso i valori delle caratteristiche delle Sedi Tecniche sono riportati negli attributi degli asset figli a quello corrispondente alla ST e che hanno la classificazione indicata nella regola. Sono tutte caratteristiche mappate dalla ST SEZIONE agli asset di tipo SBARRA.

Le logiche con cui i valori sono riportati in Maximo sono le stesse della regola SameValue.

Gestione regole SameValueEq

È la regola per gli Equipment equivalente alla SameValue delle Sedi Tecniche, per cui i valori SAP sono riportati tali e quali negli attributi corrispondenti degli asset EAM e ogni regola è valida solo per una determinata SAPCLASS. Le logiche per riportare i valori sono le stesse della regola SameValue, ad eccezione di alcuni casi in cui l’attributo Maximo è di tipo NUMERIC, mentre su SAP è ALN per cui è necessario convertire il testo in numeri: la certezza della conversione è garantita dal controllo effettuato in fase di Preimport, per cui i valori non validi sono stati tracciati come scarti e svuotati.

Gestione regole SameValueClassEq

È simile alla regola SameValueEq, l’unica differenza è che la regola contiene anche l’informazione sulla classificazione poiché all’equipment possono corrispondere più asset e il valore va riportato solo su uno di questi. Generalmente è usata per quegli equipment che si dividono in asset di tipo MACCHINA, VARIATORE e SISTEMA DI RAFFREDDAMENTO.

Gestione regole SameValueAllEq

Anche questa regola si comporta in maniera analoga alla SameValueEq, la differenza è che queste regole si applicano a tutti gli equipment, non solo a quelli di una SAPCLASS specifica. È usata per le caratteristiche PMT_EQUIPMENTxx.

Gestione regole SameValueAttrToSpecEq

È una regola speciale per gestire i 2 casi in cui i dati contenuti negli attributi di testata SERGE (Numero di serie del produttore) e MAPAR (Codice componente costruttore) devono essere riportati sugli attributi di classificazione rispettivamente STZ_SERIE_COMPONENTE e STZ_CODICE_COMPONENTE e si applica a tutti i tipi di equipment.

Check riferimenti non validi per regole RifAssetEq

I valori di alcune caratteristiche contengono riferimenti ad equipment, su Maximo il valore deve essere convertito nell’ASSETNUM corrispondente all’equipment riferito. Questo controllo si assicura che esista un asset con l’X3SAPID uguale al codice EQUNR contenuto nella caratteristica, nel caso in cui non esiste viene tracciato come scarto.

Gestione regole RifAssetEq

La regola si occupa di convertire i codici degli equipment contenuti nelle caratteristiche nei corrispondenti ASSETNUM e di riportare i valori nei rispettivi attributi nella colonna TABLE_VALUE.

Gestione regole SameValueCaratToAttrEq

Questa regola si occupa di valorizzare i 2 attributi di testata degli asset X3TIPOCOMPONENTE e X3COSTRUTTORECOMPONENTE riportando i dati contenuti in caratteristiche degli equipment. Sono valori che finiscono in asset di tipo VARIATORE e si applicano ad equipment di tipo BOOSTER, REATTORE e cinque tipi di trasformatori.

Gestione regole eccezionali

Vengono poi eseguite delle query per gestire regole eccezionali, ossia che non sono guidate dalla tabella delle regole X3ETLSTZRULES.

La prima converte in data i valori contenuti nei 2 attributi di testata degli Equipment BAUJJ (Anno) e BAUMM (Mese) e poi la riporta nell’attributo di classificazione STZ_DATA_COSTRUZIONE.

La seconda riporta i valori degli attributi di testata HERST (Produttore del cespite) e TYPBZ (Numero modello costruttore) nei rispettivi attributi dell’MBO ASSET X3COSTRUTTORECOMPONENTE e X3TIPOCOMPONENTE. La differenza con la regola SameValueCaratToAttrEq sta nel fatto che per gli stessi tipi di equipment i valori si mappano su asset di tipo MACCHINA e in più è valida per tutti gli altri tipi di EQ.

La terza si occupa di convertire i valori POSNR (Posizione) degli Equipment nei 2 attributi degli asset X3FUNZIONEELEMENTO e X3RIFERIMENTOFASE in base alla tabella di trascodifica X3TRASCODIFICAPSNR, da cui si ricavano i 2 valori in corrispondenza del valore del POSNR e della classificazione dell’asset in cui vanno riportati.

Popolamento tabelle connettore

Le ultime query della fase di caricamento servono a popolare le tabelle che serviranno poi al Connettore per allineare SAP: nello specifico vengono popolate le anagrafiche delle Sedi Tecniche e degli Equipment nella tabella X3SAPASSET e quella dei binding (legami tra l’anagrafica EAM e quella SAP) X3SAPTOMAXOBJECT.

# Modalità di avvio

In questa sezione vengono descritte le 2 modalità con cui è possibile avviare l’import delle Stazioni, mettendo in evidenza anche in quale occasione è preferibile usare l’una o l’altra.

Modalità di avvio ETL

## Planner

La prima modalità è indicata per l’import massivo, quindi è da usare come primo avvio ogniqualvolta è necessario caricare un numero elevato di stazioni.

Viene lanciata richiamando una REST API di tipo GET raggiungibile all’indirizzo

https://<hostname del manage>/maximo/oslc/script/X3ETLSTZ_PLANNER

L’API è gestita dall’Automation Script X3ETLSTZ_PLANNER che accetta i seguenti parametri:

extractionid (obbligatorio): id univoco dell'estrazione SAP, usato per ricavare le stazioni da importare

maxstations (obbligatorio): numero massimo di stazioni per singolo import

Il primo è uno dei parametri usati nelle varie fasi del processo di import per capire quali sono i dati da importare, il secondo serve a limitare il numero di stazioni che ogni singolo processo caricherà e può essere usato per ottimizzare tempi e prestazioni.

Il Planner inizia eseguendo una query nel db on premise per estrarre l’elenco delle Stazioni incluse nell’estrazione SAP e il numero di Sedi Tecniche ed Equipment per ognuna.

Crea poi dei gruppi di massimo maxstations stazioni pescando man mano in ordine quella con più oggetti (ST + EQ) e quella con meno oggetti per cercare di bilanciare quanto più possibile la numerosità dei record da importare per ogni gruppo.

Per ognuno di questi crea poi una riga nella tabella di Pianificazione, con le seguenti informazioni:

etlnum: generato automaticamente, è l’identificativo del singolo processo di import

phase: ETL

status: WSTART (in attesa di avvio)

numrecords: numero di oggetti del gruppo

importid: generato automaticamente come AnnoMeseGiornoOreMinutiSecondi del momento in cui viene avviato il Planner ed è comune a tutti i gruppi

extractionid: il valore passato come parametro dell’id dell’estrazione SAP

rootstrno: elenco degli STRNO delle stazioni che fanno parte del gruppo

Al termine dell’attività saranno presenti N righe in attesa di essere processate che rappresentano il piano di import.

A gestire l’import vero e proprio se ne occupa come visto nei paragrafi precedenti il Launcher, che in questo caso viene richiamato grazie a un crontask programmato (X3ETLSTZ) associato all’Automation Script X3ETLSTZ_CRON, che a ogni iterazione carica una riga della tabella di pianificazione in stato WSTART e avvia il processo.

## REST API

Con questa modalità il Launcher viene richiamato direttamente, mediante l’invocazione di una REST API di tipo GET raggiungibile all’indirizzo

https://<hostname del manage>/maximo/oslc/script/X3ETLSTZ_LAUNCHER

i cui parametri accettati sono quelli già descritti nel paragrafo Launcher.

Questa modalità viene usata principalmente per due finalità:

quando si vuole importare un numero limitato di stazioni, per lo più in fase di test

quando uno o più record di pianificazione falliscono per errori e si vogliono riavviare dopo aver sistemato il problema

## Pianificazione e monitoraggio

In questo paragrafo verrà ripreso e approfondito il tema della pianificazione e del monitoraggio del processo di ETL Stazioni.

Per comprendere il funzionamento della tabella X3ETLSTZLOG se ne riporta la struttura:

| Colonna | Descrizione | Tipo Dati | Lunghezza |
| --- | --- | --- | --- |
| ELAPSED | Tempo impiegato a concludere l’attività in millisecondi | FLOAT | 22 |
| ELAPSEDALN | Tempo impiegato a concludere l’attività in rappresentazione ore:minuti:secondi.millisecondi | VARCHAR2 | 80 |
| ERROR | Eventuale errore che ha fatto fallire un’attività | CLOB | 4.000 |
| ETLNUM | Identificativo del singolo import, comune a tutte le fasi e sottofasi appartenenti allo stesso processo | VARCHAR2 | 200 |
| EXTRACTIONID | Id dell’estrazione SAP | VARCHAR2 | 160 |
| FINISHDATE | Data e ora del completamento dell’attività | DATE | 7 |
| IMPORTID | Id di import comune a tutti i singoli processi di caricamento in cui è stato diviso l’import totale | VARCHAR2 | 120 |
| NUMRECORDS | Numero di record processati dall’attività, ST + EQ per la fase ETL e numero di righe del db create/modificate/cancellate in caso di sottofasi | NUMBER | 22 |
| PHASE | Fase principale del processo ETL | VARCHAR2 | 200 |
| ROOTSTRNO | Elenco degli STRNO delle stazioni che fanno parte del gruppo | VARCHAR2 | 1.200 |
| STARTDATE | Data e ora dell’inizio dell’attività | DATE | 7 |
| STATUS | Stato dell’attività | VARCHAR2 | 80 |
| SUBPHASE | Sottofase del processo ETL | VARCHAR2 | 800 |
| X3ETLSTZLOGID | Identificativo univoco Maximo | NUMBER | 22 |

Come più volte espresso in questo documento, l’approccio al caricamento è quello di dividerlo in una serie di import indipendenti, ognuno dei quali gestisce un numero massimo e ristretto di stazioni. Questa suddivisione è rappresentata dalla presenza di righe nella tabella X3ETLSTZLOG caratterizzate dalla fase ETL, nelle quali vengono riportati i parametri da usare durante le varie fasi e che alla fine conterranno l’esito e i tempi totali. Per spiegare meglio le differenze tra le possibili righe verranno riportati di volta in volta degli esempi.

Table 1 Riga fase ETL

| Colonna | Valore |
| --- | --- |
| ETLNUM | 1064 |
| PHASE | ETL |
| SUBPHASE |  |
| STARTDATE | 2024-05-20 17:50:11.000 |
| FINISHDATE | 2024-05-20 17:54:46.000 |
| STATUS | COMPLETED |
| NUMRECORDS | 4.445 |
| IMPORTID | 20240520174324 |
| EXTRACTIONID | 29S_0905_1 |
| ROOTSTRNO | 31102A,31299A,31203B,31302A,31210B,32133A,32215A,31604A,31203C,35233B |
| ELAPSED | 274.843 |
| ELAPSEDALN | 00:04:34.843 |

Durante l’esecuzione del processo di import vengono create ulteriori righe che dettagliano l’andamento dell’esecuzione stessa: vengono aggiunte quelle relative alle 3 fasi principali, caratterizzate quindi dai valori DATATRANSFER, PREIMPORT e IMPORT nella colonna PHASE.

Table 2 Riga fase DATATRANSFER

| Colonna | Valore |
| --- | --- |
| ETLNUM | 1064 |
| PHASE | DATATRANSFER |
| SUBPHASE |  |
| STARTDATE | 2024-05-20 17:50:11.000 |
| FINISHDATE | 2024-05-20 17:50:32.000 |
| STATUS | COMPLETED |
| NUMRECORDS |  |
| IMPORTID |  |
| EXTRACTIONID |  |
| ROOTSTRNO |  |
| ELAPSED | 20.732 |
| ELAPSEDALN | 00:00:20.732 |

Per ognuna delle fasi vengono poi riportate come sottofasi tutte le attività, ovvero le query eseguite, descritte in questo documento, indicando tempi di esecuzione e numero di record impattati.

Table 3 Riga sottofase copia caratteristiche ST

| Colonna | Valore |
| --- | --- |
| ETLNUM | 1064 |
| PHASE | PREIMPORT |
| SUBPHASE | Copia le righe delle caratteristiche delle ST da X3CARATSAP_STZ a X3ETLCARATSAPS2 |
| STARTDATE | 2024-05-20 17:50:39.000 |
| FINISHDATE | 2024-05-20 17:50:42.000 |
| STATUS | COMPLETED |
| NUMRECORDS | 21.762 |
| IMPORTID |  |
| EXTRACTIONID |  |
| ROOTSTRNO |  |
| ELAPSED | 3.208 |
| ELAPSEDALN | 00:00:03.208 |

Durante l’esecuzione dell’import sarà quindi possibile monitorarne l’andamento, lo stato di un’attività passerà ad EXECUTING mentre è in corso, per poi passare a COMPLETED se completata con successo o FAILED se annullata per errori e alla fine sarà possibile fare delle query per avere un riepilogo delle fasi principali

o il dettaglio di una singola fase

# Scarti

In questo paragrafo viene ripreso il tema degli scarti, ovvero di quei dati, che siano intere Sedi Tecniche o Equipment o solo valori di singole caratteristiche, che non rispettano il Data Model o le regole di mapping fornite dal Business.

Nelle fasi che costituiscono il processo ETL di import Stazioni e, in particolare durante quella di Preimport, vengono effettuati dei check al fine di controllare la validità e la conformità dei dati estratti da SAP: quelli non idonei vengono per l’appunto scartati, quindi non caricati in EAM, e vengono tracciati nella tabella dedicata X3ETLSTZSCARTI, di cui si riporta la struttura:

| Colonna | Descrizione | Tipo Dati | Lunghezza |
| --- | --- | --- | --- |
| CARATTERISTICA | Caratteristica che ha prodotto lo scarto | VARCHAR2 | 160 |
| CHAR_TYP | Valore alfanumerico | VARCHAR2 | 160 |
| CLASS | Classe SAP della caratteristica | VARCHAR2 | 160 |
| CREATEDATE | Data e ora di creazione dello scarto | DATE | 7 |
| DAT_TYP | Valore di tipo data | DATE | 7 |
| DEC_TYP | Valore numerico di tipo decimale | NUMBER | 22 |
| ETLNUM | Id del singolo processo di import | VARCHAR2 | 200 |
| IMPORTID | Id di import comune ai singoli processi appartenenti a uno stesso import totale | VARCHAR2 | 80 |
| MESSAGE | Tipo di scarto | VARCHAR2 | 2.000 |
| NUM_TYP | Valore numerico di tipo intero | NUMBER | 22 |
| OBJNR | Codice identificativo della ST (TPLNR) o dell’EQ (EQUNR) | VARCHAR2 | 120 |
| RBNR | Profilo Catalogo SAP | VARCHAR2 | 80 |
| ROOTSTRNO | STRNO della stazione | VARCHAR2 | 200 |
| SAPCLASS | Tipo di elemento SAP | VARCHAR2 | 200 |
| SAPOBJECT | Tipo di oggetto (EQ o ST) | VARCHAR2 | 8 |
| STRNO | STRNO della ST stessa o della ST superiore in caso di EQ | VARCHAR2 | 200 |
| X3ETLSTZSCARTIID | Identificativo univoco Maximo | NUMBER | 22 |

L’idea dietro la tabella è quella di avere un modo per estrarre facilmente i dati non caricati su Maximo con il motivo per cui sono stati scartati, al fine di semplificare l’attività di analisi del Business per capire cosa effettivamente è corretto che venga scartato e cosa invece va bonificato preventivamente all’estrazione su SAP o ancora (ci si augura come eventualità remota) se c’è bisogno di rivedere qualche logica dell’ETL.

Per comodità viene riportato l’elenco dei controlli già visti in dettaglio nella descrizione delle singole fasi:

Check ST non identificate come SAPCLASS

Check ST non identificate come classificazione

Check EQ non identificati come SAPCLASS

Check EQ non identificati come classificazione

Check scarti per caratteristica multivalore ST

Check scarti per caratteristica multivalore EQ

Check valori ST indisponibilità

Check formato caratteristiche CHAR che sono mappate su attributi NUMERIC

Check coerenza saplevel con parent

Check coerenza strno con parent

Check esistenza nuovo parent per IMPIANTO DI TERRA

Check molteplici nuovi parent per IMPIANTO DI TERRA

Check valori BAUJJ, BAUMM

Check valori TYPBZ

Check POSNR senza mapping

Check riferimenti non validi per regole RifAssetEq

# Documenti correlati

I documenti sotto menzionati sono consultabili alla seguente repository condivisa: Documenti correlati.

| ID | Nome documento | Descrizione |
| --- | --- | --- |
| 1 | EAM Stazioni - Classificazione asset integrata con HVDC e ICS_20240326.xlsx | Mapping sede tecniche ed equipment SAP verso asset EAM |
| 2 | EAM Stazioni - Elenco attributi integrato con HVDC e ICS_PRELIMINARE BSN 20240430.xlsx | Mapping caratteristiche sedi tecniche ed equipment SAP verso specifiche tecniche asset EAM |
| 3 | EAM Stazioni - Regole ETL.xlsx | Elenco delle regole di mapping applicate dal processo di ETL Stazioni |
| 4 | EAM_HLD_ETL_ANA_Documento di ETL Stazioni_20240408_V2.6.docx | Documento di HLD di ETL Stazioni |
