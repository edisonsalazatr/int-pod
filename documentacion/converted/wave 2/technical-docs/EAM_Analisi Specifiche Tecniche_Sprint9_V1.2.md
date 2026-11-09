# EAM_Analisi Specifiche Tecniche_Sprint9_V1.2

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Specifica Tecnica – Sprint 9 |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data | Owner |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | ACN | 22/11/2024 |  |
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
| Dominio crossover | Un dominio di crossover in IBM Maximo è un meccanismo che consente di specificare condizioni per il trasferimento di valori tra campi di oggetti correlati. Quando si crea un dominio di crossover, è possibile definire regole che determinano quando i valori di un campo di origine verranno copiati in un campo di destinazione |

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche, già descritte ad alto livello nel documento “EAM_Analisi Funzionale_Sprint_.docx”, verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software.

Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni implementate sulla piattaforma EAM di Terna (ambiente Dev) e gli sviluppi custom da implementare. Ciascun paragrafo (dedicato a una specifica funzionalità nell’applicativo) presenta due sotto-paragrafi: nel primo sono illustrate le configurazioni eseguite sul software (e.g., estensione del modello dati, configurazione dell’applicazione, gestione della condizionalità, etc.), nel secondo gli sviluppi necessari per soddisfare le funzionalità richieste durante lo sprint.

In particolare, i temi inclusi nello Sprint 9 sono i seguenti:

Gestione degli OdL manuali;

Gestione attività di pronto intervento

Gestione OdL serbatoio.

# Sprint 9: Gestione ordini manuali

## Gestione OdL manuali

### Configurazioni

Per la gestione degli OdL manuali è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| WORKORDER | JPNUM | Identificativo PdL | ALN | 16 |
| WORKORDER | X4ASSIEME1 | Assieme del PdL associato | ALN | 35 |
| WORKORDER | X4SOTTOASSIEME1 | Sottoassieme del PdL associato | ALN | 50 |
| WORKORDER | OWNERGROUP1 | Identificativo dell’owner della pianificazione dell’OdL | UPPER | 10 |
| WORKORDER | WONUM | Identificativo dell’OdL | UPPER | 25 |
| WORKORDER | DESCRIPTION1 | Descrizione dell’OdL | ALN | 100 |
| WORKORDER | ASSETNUM1 | Identificativo dell’Asset | UPPER | 25 |
| WORKORDER | STATUS1 | Stato dell’OdL | UPPER | 16 |
| WORKORDER | X4COIN1 | Identificativo della contabilità industriale | ALN | 15 |
| WORKORDER | X4TIPOSPESA1 | Tipo spesa | ALN | 5 |
| WORKORDER | ESTDUR1 | Durata pianificata | DURATION |  |
| WORKORDER | TARGSTARTDATE1 | Data inizio pianificata | DATETIME |  |
| WORKORDER | TARGCOMPDATE1 | Data fine pianificata | DATETIME |  |
| WORKORDER | X4DATAMODELLO1 | Data modello | DATE |  |
| WORKORDER | X4DINIZIOINDISP1 | Data inizio indisponibilità | DATETIME |  |
| WORKORDER | X4DFINEINDISP1 | Data fine indisponibilità | DATETIME |  |
| WORKORDER | REPORTDATE1 | Data di creazione dell’OdL | DATETIME |  |
| WORKORDER | FNLCONSTRAINT1 | Data massimo posticipo | DATETIME |  |
| WORKORDER | SNECONSTRAINT1 | Data non prima di | DATETIME |  |
| WORKORDER | X4NOTE1 | Note | ALN | 150 |
| WORKORDER | X4PERIMETROINT1 | Perimetro di intervento | ALN | 25 |
| WORKORDER | X4TIPOODL1 | Tipologia OdL | ALN | 15 |
| WORKORDER | X4SERBATOIO1 | OdL serbatoio | YORN | 1 |
| WORKORDER | X4NATURALAVORO1 | Assistenza/sorveglianza | ALN | 15 |
| WORKORDER | X4AGGREGATI | OdL aggregatore | YORN | 1 |
| WORKORDER | X4POLICY1 | Identificativo della policy associata | ALN | 20 |
| WORKORDER | X4SOTTOPROGRAMMA | Cluster di caratterizzazione su alcuni asset specifici | ALN | 50 |
|  |  |  |  |  |
| ASSET | DESCRIPTION1 | Descrizione dell’asset | ALN | 50 |
| ASSET | X3SAPID1 | Codice SAP dell’asset | UPPER | 30 |
| ASSET | X3INGRP1 | Responsabile asset | ALN | 3 |
|  |  |  |  |  |
| JOBPLAN | X4PROGRAMMA1 | Cluster di attività su alcuni asset specifici | ALN | 20 |
| JOBPLAN | DESCRIPTION1 | Descrizione del PdL | ALN | 150 |
| JOBPLAN | X4MACROATTIVITA1 | Codice identificativo della macro-attività associata all’OdL | UPPER | 16 |
| JOBPLAN | X4ATTALL1 | ID attività standard | ALN | 15 |
| JOBPLAN | X4INDISPONIBILITA1 | Indisponibilità necessaria sull’OdL | YORN | 1 |
| JOBPLAN | X4ATTCAR1 | Attività caricabile manualmente | YORN | 1 |
| JOBPLAN | X4TIPOLOGIAASSET1 | Classificazione asset | UPPER | 192 |
| JOBPLAN | JPDURATION1 | Durata pianificata | DURATION |  |
|  |  |  |  |  |
| JOBPLAN | X4OUTSOURCING | Modalità di gestione IN/OUT | YORN |  |
| X4JOBPLANLASSIFICATIONS | CLASSIFICATIONID1 | Classificazione asset ammissibile | UPPER | 192 |
| WPLABOR | X4TITRIS1 | Titolare risorsa | UPPER | 10 |
| WPLABOR | LABORHRS1 | Ore esecuzione | DURATION |  |
| WPLABOR | X4ORETRASF1 | Ore trasferimento | DURATION |  |
| X4CATMOS | COINPREVALENTE | Coin prevalente | ALN | 15 |
| X4CATMOS | SPESAPREVALENTE1 | Tipo spesa prevalente | ALN | 15 |
| X4CATMOS | IDATTSTANDARD1 | ID attività standard | ALN | 15 |
| X4MATRICEDEST | TTRASF1 | Tempo di trasferimento | DURATION |  |
| X4MATRICEDEST | ORIGINE1 | Origine | ALN | 20 |
| X4MATRICEDEST | DESTINAZIONE1 | Destinazione | UPPER | 30 |
| X4INDISPWO | INIZIOINDISPONIBILITA1 | Data inizio indisponibilità | DATETIME |  |
| X4INDISPWO | FINEINDISPONIBILITA1 | Data fine indisponibilità | DATETIME |  |
| CLASSSTRUCTURE | HIERARCHYPATH1 | Tipologia di PdL associato all’OdL | ALN | 254 |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

WORKORDER: Tabella standard di Maximo che permette la gestione degli ordini di lavoro

ASSET: Tabella standard di Maximo che permette la gestione dell’anagrafica delle linee e stazioni

JOBPLAN: Tabella standard di Maximo che permette la gestione dei piani di lavoro

X4JOBPLANLASSIFICATIONS: Tabella custom di Maximo che permette la gestione del legame tra piani di lavoro e classificazioni asset ammissibili

X4POLICY: Tabella custom di Maximo che permette la gestione delle policy

WPLABOR: Tabella standard di Maximo che permette la gestione della pianificazione delle risorse interne

X4MATRICEDEST: Tabella custom di Maximo che permette la gestione del tempo di trasferimento dell’ordine di lavoro

X4INDISPWO: Tabella custom di Maximo che permette la gestione dei campi relativi alle richieste di indisponibilità

CLASSSTRUCTURE: Tabella standard di Maximo che permette la gestione delle classificazioni degli asset

X4CATMOS: Tabella custom di Maximo che permette la gestione del catalogo delle attività standard

Richiesta di servizio per pronto intervento

La gestione delle richieste di servizio funzionalmente prevede alcuni step da effettuare mediante applicazione mobile. Per permettere la gestione di queste entità ed il loro tracciamento è dunque necessario personalizzare l’applicazione “Maximo Mobile” in modo da poterne permettere la gestione.

Modifica app per Service Request

L’utente ha la possibilità di utilizzare l’applicazione “Service Request” per creare nuove richieste di servizio all’interno di EAM. L’applicazione sfrutta le componenti standard mobile, come il datasource “MXAPISR”. L’oggetto interagisce con la vista “SR” dell’applicativo EAM.

Il datasource MXAPISR viene determinato con i seguenti attributi: TICKETID (ID univoco della SR), DESCRIPTION (Campo note della SR), X4CLASSIFICAZIONEASSET (classificazione dell’asset), X4DIFFORMITA (Tipologia difformità).

Il campo X4DIFFORMITA, inoltre, richiama una lookup definita da un altro datasource denominato “X4CATDIFF”, al cui interno sono presenti soltanto i campi: IDDIFFORMITA (ID Univoco difformità), DESCRIPTION (Descrizione difformità), TIPOLOGIAASSET (Asset a cui applicare la difformità).

Il datasource viene filtrato sulla base del seguente criterio X4CATDIFF.TIPOLOGIAASSET=SR. X4CLASSIFICAZIONEASSET.

In schermata, inoltre, è presente un tasto “Salva” che evocherà la sincronizzazione del datasource MXAPISR con EAM e quindi la creazione della nuova service request in EAM.

### Sviluppi

Per permettere il popolamento dei campi degli OdL manuali, si prevedono i seguenti sviluppi a sistema:

Valorizzazione campi obbligatori degli OdL manuali

Di seguito si riportano i campi obbligatori degli OdL manuali (X4TIPOODL = ‘MANUALE’) e le loro logiche di valorizzazione:

Identificativo PdL–– (JPNUM), campo ad inserimento manuale mediante il domino crossover X4JP2WO che mostrerà la lista dei PdL che presentano il flag X4ATTCAR uguale a 1 (attività caricabile manualmente) Inoltre, tale dominio di crossover viene configurato inserendo nel campo CROSSOVERDOMAIN.SOURCEFIELD i campi X4ASSIEME e X4SOTTOASSIEME della tabella JOBPLAN e nel campo CROSSOVERDOMAIN.DESTFIELD gli omonimi campi della tabella WORKORDER; di conseguenza, il sistema preleverà i valori presenti in X4ASSIEME e X4SOTTOASSIEME della tabella JOBPLAN del JPNUM di interesse e li scriverà negli stessi attributi della tabella WORKORDER.

Tipologia PdL (CLASSSTRUCTURE.HIERARCHYPATH), campo valorizzato automaticamente dal sistema mediante la relazione JOBPLAN (:JPNUM=JPNUM) in base al JPNUM selezionato

Descrizione PdL (DESCRIPTION), campo valorizzato automaticamente dal sistema mediante la relazione JOBPLAN (:JPNUM=JPNUM) in base al JPNUM selezionato

ID Macro-attività (X4MACROATTIVITA), campo valorizzato automaticamente dal sistema mediante la relazione JOBPLAN (:JPNUM=JPNUM) in base al JPNUM selezionato

Responsabile attività (OWNERGROUP), campo valorizzato automaticamente dal sistema mediante automation script, il cui valore definito viene popolato come segue: sulla base del JPNUM inserito nell’ordine, viene verificato il tipo di UDB a cui assegnarlo (X4TIPUDB della tabella JOBPLAN dove il JPNUM=WORKORDER.JPNUM); una volta determinato il tipo di UDB viene definito l’UDB come segue: se l’UDB è UI allora viene prelevata l’UI dell’asset (X3INGRP dell’asset ASSETNUM del WORKORDER) e si effettua una ricerca sulla tabella PERSONGROUP dove x3parentpersongroup è uguale all’UI dell’asset e X4UDB è pari a True; in caso il X4TIPUDB non sia pari a UI, allora in quel caso viene prelevato il DT dell’asset (X3SWERK dell’asset) e viene effettuata la stessa ricerca prima descritta, ma con X4TIPUDB pari a quello prelevato dal PDL.

ID OdL (WONUM), campo valorizzato automaticamente dal sistema con codice univoco interno

Descrizione OdL (DESCRIPTION), campo valorizzato automaticamente dal sistema mediante automation script prelevando il campo JOBPLAN.DESCRIPTION relativo al JPNUM associato all’OdL. Tale campo potrà essere modificato dall’utente

ID Asset (ASSETNUM), campo ad inserimento manuale, mediante il domino crossover ASSETCROSSCI mostrerà la lista degli asset presenti a sistema

Descrizione asset (DESCRIPTION), campo valorizzato automaticamente dal sistema mediante la relazione ASSET (:ASSETNUM=ASSETNUM) in base all’ASSETNUM selezionato

Gerarchia asset (CLASSSTRUCTURE.HIERARCHYPATH), campo valorizzato automaticamente dal sistema mediante la relazione ASSET (:ASSETNUM=ASSETNUM) in base all’ASSETNUM selezionato

Stato (STATUS), campo valorizzato automaticamente dal sistema mediante automation script con il valore “Aperto” al momento della creazione dell’OdL

Responsabile asset (X3INGRP), campo valorizzato automaticamente dal sistema mediante la relazione ASSET (:ASSETNUM=ASSETNUM) in base all’ASSETNUM selezionato

Codice CoIn (X4COIN), campo valorizzato automaticamente dal sistema mediante automation script non appena selezionato il PdL. Nel dettaglio, il campo COINPREVALENTE verrà prelevato dalla tabella X4CATMOS dove IDATTSTANDARD è uguale al campo X4ATTALL del piano di lavoro usato.

Tipo Spesa (X4TIPOSPESA), campo valorizzato automaticamente dal sistema mediante automation script non appena selezionato il PdL. Nel dettaglio, il campo SPESAPREVALENTE verrà prelevato dalla tabella X4CATMOS dove IDATTSTANDARD è uguale al campo X4ATTALL del piano di lavoro usato

Attività standard (X4ATTALL), campo valorizzato automaticamente dal sistema mediante la relazione JOBPLAN (:JPNUM=JPNUM) in base al JPNUM selezionato

Titris (X4TITRIS), campo valorizzato automaticamente dal sistema mediante automation script che verifica se il campo OWNERGROUP dell’ordine di lavoro collegato alla riga di WPLABOR (wonum=:wonum and siteid=:siteid) è presente, in caso positivo viene posto lo stesso valore sul campo X4TITRIS.

Durata pianificata (ESTDUR), campo valorizzato automaticamente dal sistema non appena selezionato il PdL. Nel dettaglio, il campo JPDURATION verrà prelevato della tabella JOBPLAN dove JPNUM della tabella WORKORDER è uguale al campo JPNUM del piano di lavoro usato

Ore esecuzione (LABORHRS), campo valorizzato automaticamente dal sistema non appena selezionato il PdL ed il sistema creerà le righe di pianificazione risorse (con il stesso valore definito nel JOBPLAN).

Data pianificata inizio (TARGSTARTDATE), campo valorizzato

Data pianificata fine (TARGCOMPDATE), campo valorizzato automaticamente dal sistema mediante automation script. Nel dettaglio, il campo sarà posto uguale al campo data modello se quest’ultimo è valorizzato; ovvero se X4DATAMODELLO is not null. Altrimenti, il campo sarà popolato manualmente dall’utente

Perimetro intervento (X4PERIMETROINT), campo valorizzato automaticamente dal sistema mediante automation script. Nel dettaglio, il campo sarà valorizzato (con uno dei seguenti valori: “Sostituzione”, “Nuove installazione”, “Manutenzione”, “Monitoraggio” e “Altro”) in base al campo tipologia attività del PdL (CLASSSTRUCTURE.HIERARCHYPATH) dove JPNUM della tabella WORKORDER è uguale al campo JPNUM del piano di lavoro usato

Tipo OdL (X4TIPOODL), campo valorizzato automaticamente dal sistema mediante automation script con il valore “Manuale”

(X4OUTSOURCING), campo valorizzato automaticamente dal sistema mediante la relazione JOBPLAN (:JPNUM=JPNUM) in base al JPNUM selezionato

OdL serbatoio (X4SERBATOIO), flag impostato di default dal sistema in “NO”; ovvero, X4SERBATOIO = 0

Indisponibilità necessaria (X4INDISPONIBILITA), flag valorizzato automaticamente dal sistema mediante la relazione JOBPLAN (:JPNUM=JPNUM) in base al JPNUM selezionato

OdL aggregatore (X4AGGREGATI), flag impostato di default dal sistema in “NO”; ovvero, X4AGGREGATI = 0

Data massimo posticipo (FNLCONSTRAINT), campo ad inserimento manuale

Data non prima di (SNECONSTRAINT), campo valorizzato automaticamente dal sistema mediante automation script. Nel dettaglio, il campo sarà posto uguale alla data di creazione dell’OdL; ovvero REPORTDATE della tabella WORKORDER

Valorizzazione campi facoltativi degli OdL manuali

Di seguito si riportano i campi facoltativi degli OdL manuali (X4TIPOODL = ‘MANUALE’) e le loro logiche di valorizzazione:

Assieme (X4ASSIEME), campo valorizzato automaticamente mediante il dominio crossover X4JP2WOcampo identificativo PdL (JPNUM)

Sottoasseime (X4SOTTOASSIEME), campo valorizzato automaticamente mediante il dominio crossover X4JP2WOcampo identificativo PdL (JPNUM)

Programma (X4PROGRAMMA), campo valorizzato automaticamente dal sistema mediante la relazione JOBPLAN (:JPNUM=JPNUM) in base al JPNUM selezionato

Sotto-programma (X4SOTTOPROGRAMMA), campo ad inserimento manuale, questo sarà visibile all’utente solo se l’OdL è manuale; ovvero, viene mostrato mediante la sigoption X4ODLMANUALE quando la condizione :X4TIPOODL= ‘MANUALE’ è verificata. L’utente potrà selezionare i valori presenti nel dominio X4SOTTOPROGRAMMA.

SAP ID Asset (X3SAPID), campo valorizzato automaticamente dal sistema mediante la relazione X4SAPIDASSET ((((targetassetnum=:ASSET.ancestor) or (:ASSET.ancestor is null and targetassetnum=:ASSET.assetnum))and siteid=:siteid AND :ASSET.classstructureid in (select classstructureid FROM classancestor WHERE ancestorclassid='LINEA'))or((assetnum=:ASSET.ANCESTOR or (:ASSET.ASSETNUM=assetnum and :ASSET.ANCESTOR is null)) and :ASSET.CLASSSTRUCTUREID in (select classstructureid FROM classancestor WHERE ancestorclassid='STAZIONE'))) in base al ASSETNUM selezionato

Ore trasferimento (X4ORETRASF), campo valorizzato automaticamente dal sistema mediante automation script sulla base del TITRIS di appartenenza. Nel dettaglio, verrà creata una tabella X4MATRICEDEST che per ogni UdB possibile all’interno del sistema, abbia associato un tempo di trasferimento verso ogni specifica sede tecnica di primo livello. Nel dettaglio, il campo ORIGINE della tabella X4MATRICEDEST sarà caratterizzato dal dominio tabellare “X4UDB” che preleverà i record di PERSONGROUP con X4UDB = 1, mentre il campo DESTINAZIONE della medesima tabella sarà caratterizzato dal dominio “X4SAPID” che preleva i record di ASSET con CLASSSTRUCTUREID della tabella CLASSSTRUCTURE dove CLASSIFICATIONID è pari a LINEA o STAZIONE. Inoltre, la tabella sarà modificabile dall’utente (sia in termini di creazione di nuove righe che di modifica/eliminazione di righe esistenti) mediante applicazione dedicata X4MATRICEDEST.

Data modello (X4DATAMODELLO), campo ad inserimento manuale dagli utenti centrali.

Data inizio indisponibilità (X4DINIZIOINDISP), campo valorizzato automaticamente dal sistema mediante automation script alla ricezione della delibera di indisponibilità nell’apposita tabella X4INDISPWO. Quest’ultimo copierà il contenuto dell’ultima delibera ricevuta nel campo X4DINIZIOINDISP, prelevando il contenuto dei campi INIZIOINDISPONIBILITA di X4INDISPWO.

Data fine indisponibilità (X4DFINEINDISP), campo valorizzato automaticamente dal sistema mediante automation script alla ricezione della delibera di indisponibilità nell’apposita tabella X4INDISPWO. Quest’ultimo copierà il contenuto dell’ultima delibera ricevuta nel campo X4DFINEINDISP, prelevando il contenuto dei campi FINEINDISPONIBILITA di X4INDISPWO.

Note (X4NOTE), campo ad inserimento manuale

Gestione stati OdL manuali

La gestione degli stati degli OdL manuali segue il workflow presentato nel documento “EAM_Analisi Specifiche Tecniche_Sprint3_V1.0” al par. 3.1.2., ad eccezione degli stati DA VALIDARE e NON ESEGUITO. In particolare, per gli OdL manuali non è previsto lo stato DA VALIDARE e il passaggio di stato da APERTO a NON ESEGUITO non sarà automatico quando la SYSDATE supera la data di massimo posticipo (FNLCONSTRAINT), bensì manuale. Tutti gli OdL creati direttamente da utente tramite GUI nascono dunque in stato APERTO. Tale impostazione viene definita mediante un DEFAULTVALUE impostato a livello di applicazione, che viene richiamato solo ed esclusivamente quando l’OdL viene creato mediante GUI (applicazione di monitoraggio OdL)

Per permettere la creazione massiva di OdL manuali, si prevedono i seguenti sviluppi a sistema:

Creazione massiva OdL manuali tramite MxLoader

Per permettere la creazione massiva degli OdL manuali si prevede la creazione di un API (che insiste sull’entità WORKORDER) dedicata richiamata all’interno di un file MXLoader con le seguenti colonne: Identificativo PdL (JPNUM), ID asset (ASSETNUM), Data fine pianificata (TARGCOMPDATE), Data massimo posticipo (FNLCONSTRAINT). L’API sarà prodotta per poter interagire solo ed esclusivamente con i campi sopra descritti.

Creazione massiva OdL manuali tramite la funzionalità “Creazione massiva OdL”

Per permettere la creazione massiva degli OdL manuali si prevede la creazione della dialog “Creazione massiva OdL” (Control ID “creaodlman”) nell’applicazione “Monitoraggio OdL” (WOTRACK_D). Nel dettaglio, questa sarà caratterizzata dai campi di testata: Sede tecnica di 1° livello (X4PRIMOLVL), Classificazione asset (X4CLASSMASS), Identificativo PdL (JPNUM), Assistenza/sorveglianza (X4NATURALAVORO), (X4OUTSOURCING); Data modello (X4DATAMODELLO), Data pianificata fine (TARGCOMPDATE), Data di massimo posticipo (FNLCONSTRAINT); e dalla tabella caratterizzata dalla relazione X4CHILDASSET (:ASSETNUM=PARENT OR ASSETNUM=ANCESTOR OR (assetnum in (select targetassetnum from assetlocrelation where sourceassetnum=:assetnum and assetrelationnum=’INCLUDES’))) che mostrerà gli asset gerarchicamente inferiori a quello inserito nel campo sede tecnica di 1° livello e che abbiano classificazione come quella indicata nei campi di testata della dialog: Asset (ASSETNUM), Descrizione (DESCRIPTION), SAP ID (X3SAPID), Stato (STATUS).

Per permettere il popolamento dei campi dei PdL di pronto intervento, si prevedono i seguenti sviluppi a sistema:

Valorizzazione campi dei PdL di pronto intervento

Di seguito si riportano i campi dei PdL di pronto intervento (CLASSSTRUCTURE.HIERARCHYPATH = ‘PRONTO INTERVENTO’) e le loro logiche di valorizzazione:

Attività caricabile manualmente (X4ATTCAR), flag valorizzato automaticamente dal sistema mediante automation script. Nel dettaglio, il flag X4ATTCAR viene posto pari a 1 quando il campo CLASSSTRUCTURE.HIERARCHYPATH del JPNUM corrente è uguale a ‘PRONTO INTERVENTO’.

Classificazione asset, campo valorizzato automaticamente dal sistema per relazione in base al campo X4TIPOLOGIAASSET selezionato, prelevando i record ammissibili (CLASSIFICATIONID) nella tabella X4JOBPLANLASSIFICATIONS.

Gerarchia asset associato, campo valorizzato automaticamente dal sistema sulla base del livello asset associato; viene definito mediante relazione, prelevando il campo HIERARCHYPATH della classificazione asset.

Durata pianificata (JPDURATION), campo valorizzato automaticamente dal sistema mediante automation script. Nel dettaglio, il campo JPDURATION viene posto pari a 24:00 quando il campo CLASSSTRUCTURE.HIERARCHYPATH del JPNUM corrente è uguale a ‘PRONTO INTERVENTO’.

Gestione OdL serbatoio

### Configurazioni

Per il monitoraggio della capacity erosa dagli OdL serbatoio è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| WORKORDER | ASSETNUM | Identificativo dell’asset | UPPER | 25 |
| WORKORDER | X4CAPACITYEROSAO | Capacity erosa operai da OdL a serbatoio | INT |  |
| WORKORDER | X4CAPACITYRESIDUAO | Capacity residua operai del serbatoio | INT |  |
| WORKORDER | X4CAPACITYEROSAI | Capacity erosa impiegati da OdL a serbatoio | INT |  |
| WORKORDER | X4CAPACITYRESIDUAI | Capacity residua impiegati del serbatoio | INT |  |
| WORKORDER | X4CAPACITYEROSAQ | Capacity erosa quadri da OdL a serbatoio | INT |  |
| WORKORDER | X4CAPACITYRESIDUAQ | Capacity residua quadri del serbatoio | INT |  |
| WORKORDER | OWNERGROUP1 | Responsabile attività | UPPER | 10 |
| WORKORDER | X4COIN1 | Codice Coin | ALN | 15 |
| WORKORDER | WONUM1 | Identificativo dell’OdL | UPPER | 15 |
| WORKORDER | DESCRIPTION1 | Descrizione dell’OdL | ALN | 100 |
| WORKORDER | X4EFFORTO1 | Effort operai | DURATION |  |
| WORKORDER | X4EFFORTI1 | Effort impiegati | DURATION |  |
| WORKORDER | X4EFFORTQ1 | Effort quadri | DURATION |  |
| WORKORDER | TARGSTARTDATE1 | Data inizio pianificata | DATETIME |  |
| WORKORDER | TARGCOMPDATE1 | Data fine pianificata | DATETIME |  |
| WORKORDER | JPNUM1 | Identificativo del PdL associato all’OdL | ALN | 16 |
| WORKORDER | REPORTDATE1 | Data di creazione dell’OdL | DATETIME |  |
| WPLABOR | CRAFT1 | Qualifica | UPPER | 8 |
| WPLABOR | X4DATAINIZIORISORSA1 | Data inizio risorsa | DATETIME |  |
| WPLABOR | X4DATAFINERISORSA1 | Data fine risorsa | DATETIME |  |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

WORKORDER: Tabella standard di Maximo che permette la gestione degli ordini di lavoro

JOBPLAN: Tabella standard di Maximo che permette la gestione dei piani di lavoro

WPLABOR: Tabella standard di Maximo che permette la gestione della pianificazione delle risorse

Per permettere la visione dei campi necessari al monitoraggio della capacity erosa dagli OdL serbatoio, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “WOTRACK_D” come duplicato dell’applicazione standard “WOTRACK”, si dovranno configurare i seguenti campo di testata del tab “Capacity erosa Operai”, caratterizzato dalla sigoption “X4SERBATOIO”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Capacity totale | X4EFFORTO |  |  | RO |
| Capacity erosa | X4CAPACITYEROSAO |  |  | RO |
| Capacity residua | X4CAPACITYRESIDUAO |  |  | RO |
| Responsabile attività | OWNERGROUP |  |  | RO |
| Coin | X4COIN |  |  | RO |

La sigoption “RO” rende i campi su cui è applicata non modificabili dall’utente. La sigoption “X4SERBATOIO” rende visibile l’intero tab solo se il flag X4SERBATOIO della tabella WORKORDER è pari a 1, ovvero se l’ordine è un OdL a serbatoio.

I campi della tabella “Lista OdL”, caratterizzata dalla relazione “X4WOSERBOP”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID OdL | WONUM |  |  | RO |
| Descrizione | DESCRIPTION |  |  | RO |
| Asset | ASSETNUM |  |  | RO |
| Effort | X4EFFORTO |  |  | RO |
| Data pianificata inizio | TARGSTARTDATE |  |  | RO |
| Data pianificata fine | TARGCOMPDATE |  |  | RO |
| PdL | JPNUM |  |  | RO |

La sigoption “RO” rende i campi su cui è applicata non modificabili dall’utente. La relazione “X4WOSERBOP” lega le tabelle WORKORDER e WORKORDER in modo da selezionare la lista di OdL che presentano :OWNERGROUP=OWNERGROUP and :X4COIN=X4COIN and WPLABOR.CRAFT=’OPERAIO’ and WPLABOR.X4DATAFINERISORSA<:TARGCOMPDATE and WPLABOR.X4DATAFINERISORSA>:TARGSTARTDATE and REPORTDATE>:REPORTDATE; nel dettaglio, la relazione preleva gli OdL che presentano stesso responsabile attività, stesso codice Coin, stessa qualifica e stesso mese dell’OdL serbatoio corrente e generati dopo quest’ultimo.

Il medesimo tab viene aggiunto anche per impiegati “Capacity erosa Impiegati” e per quadri “Capacity erosa Quadri”. Le relazioni adottate per ricavare i record della tabella “Lista OdL” saranno, rispettivamente, “X4WOSERBIMP” (:OWNERGROUP=OWNERGROUP and :X4COIN=X4COIN and WPLABOR.CRAFT=’IMPIEGAT’ and WPLABOR.X4DATAFINERISORSA<:TARGCOMPDATE and WPLABOR.X4DATAFINERISORSA>:TARGSTARTDATE and REPORTDATE>:REPORTDATE) e “X4WOSERBQUA” (:OWNERGROUP=OWNERGROUP and :X4COIN=X4COIN and WPLABOR.CRAFT=’QUADRO’ and WPLABOR.X4DATAFINERISORSA<:TARGCOMPDATE and WPLABOR.X4DATAFINERISORSA>:TARGSTARTDATE and REPORTDATE>:REPORTDATE); nel dettaglio, le relazioni prelevano gli OdL che presentano stesso responsabile attività, stesso codice Coin, stessa qualifica e stesso mese dell’OdL serbatoio corrente e generati dopo quest’ultimo.

### Sviluppi

Per permettere la gestione degli stati degli OdL serbatoio, si prevede il seguente sviluppo a sistema:

Workflow stati OdL serbatoio

Di seguito si riportano le logiche del workflow degli stati ammissibili (BOZZA, APERTO, CHIUSO) dell’OdL serbatoio (X4SERBATOIO della tabella WORKORDER pari a 1):

BOZZA, stato impostato di default dal sistema alla creazione dell’OdL serbatoio

APERTO, stato impostato automaticamente dal sistema tramite escalation al verificarsi della condizione: anno(TARGSTARTDATE)=anno(SYSDATE); ovvero, quando l’anno associato alla data pianificata inizio dell’OdL serbatoio è pari all’anno corrente

CHIUSO, stato impostato automaticamente dal sistema tramite escalation al verificarsi della condizione: anno(TARGSTARTDATE)=anno(SYSDATE)-1; ovvero, quando l’anno associato alla data pianificata inizio dell’OdL serbatoio è all’anno corrente. Inoltre, tramite il pulsante del cambio stato, l’utente può effettuare manualmente il passaggio di stato da BOZZA / APERTO a CHIUSO.

Per permettere la valorizzazione dei campi inerenti al monitoraggio della capacity erosa, si prevedono i seguenti sviluppi a sistema:

Valorizzazione campi del Tab “Capacity erosa operai”

Di seguito si riportano le loro logiche di valorizzazione dei campi del tab “Capacity erosa operai”:

Capacity totale (X4EFFORTO), campo valorizzato automaticamente dal sistema mediante automation script con il prodotto dei campi QUANTITY e LABORHRS prelevati dalla riga di WPLABOR caratterizzata da :WONUM=WONUM and CRAFT=’OPERAIO’

Capacity erosa (X4CAPACITYEROSAO), campo valorizzato automaticamente dal sistema mediante automation script con la somma dei campi X4EFFORTO della lista di OdL che presentano :OWNERGROUP=OWNERGROUP and :X4COIN=X4COIN and WPLABOR.CRAFT=’OPERAIO’ and WPLABOR.X4DATAFINERISORSA <:TARGCOMPDATE and WPLABOR.X4DATAFINERISORSA>:TARGSTARTDATE and REPORTDATE>:REPORTDATE; ovvero, gli OdL che presentano stesso responsabile attività, stesso codice Coin, stessa qualifica e stesso mese dell’OdL serbatoio corrente e generati dopo quest’ultimo.

Capacity residua (X4CAPACITYRESIDUAO), campo valorizzato automaticamente dal sistema mediante automation script con la differenza dei campi X4EFFORTO e X4CAPACITYEROSAO dell’OdL serbatoio.

Le medesime logiche di valorizzazione sono presenti anche per il tab “Capacity erosa impiegati” e “Capacity erosa quadri”.

Per permettere il controllo del conto base residuo, si prevede il seguente sviluppo a sistema:

Controllo valore residuo conto base

Un automation script verificherà che al variare dell’ammontare di ore pianificate negli OdL serbatoio, questi non facciano diventare negativo il valore del conto base residuo.

Affinché sia possibile, lo script verrà triggerato solo per quelle modifiche di WPLABOR dove : WORKORDER.X4SERBATOIOID is not null (garantisce che gli odl siano odl a serbatoio); una volta triggerato lo script, l’algoritmo verificherà il valore di conto base residuo impattato dalla modifica con la seguente logica: verrà verificata l’esistenza dell’mboSet generato con la seguente relazione “x4serbatoioid=:WORKORDER.X4SERBATOIOID and CRAFT=:CRAFT and MESE=EXTRACT( MONTH FROM :X4DATAINIZIORISORSA)”, una volta verificata l’esistenza verrà prelevato il valore di conto base residuo della riga definita e a questi ne verrà sottratto il delta della riga di WPLABOR modificata (previous_value – current_value). Se la differenza è negativa, allora il sistema mostrerà un errore e non permetterà il salvataggio.
