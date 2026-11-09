# EAM_LLD_GEN_ANA_Connettore_20230731_V1.4

|  | Connettore anagrafica – Low-Level Design |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Relazione: | Connettore anagrafica – Low-level design |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 1.0 | Primo rilascio | 04/12/2023 |
| 1.1 | Feedback ICT | 11/12/2023 |
| 1.2 | Aggiornamento in base ai feedback ICT | 13/12/2023 |
| 1.3 | Aggiornamento stazioni | 12/01/2024 |
| 1.3 | Feedback ICT | 30/01/2024 |
| 1.4 | Integrazione Allegati | 05/02/2024 |
| 1.5 | Integrazione Open Point | 27/06/2025 |
|  |  |  |
|  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | RTI | 04/12/2023 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

# Introduzione

Il presente documento descrive il disegno di basso livello (low-level design) del connettore EAM. Il documento si basa sui concetti espressi nel documento di HLD (high-level-design) del connettore. Dal punto di vista funzionale il connettore è responsabile della gestione dei flussi di integrazione in-bound e out-bound per la piattaforma EAM di seguito elencati:

un flusso in-bound per la creazione in EAM di un primo livello linea/stazione esistente in SAP

flussi out-bound per la sincronizzazione di linee/stazioni EAM verso SAP

flussi di sincronizzazione di anagrafiche accessorie (non tecniche); parliamo nel dettaglio di sedi tecniche di indisponibilità e centri di lavoro da SAP verso EAM

Dal punto di vista tecnico il connettore sarà basato sul layer di integrazione MIF (Manage Integration Framework) di IBM MAS Manage. Il MIF consente la realizzazione di flussi di integrazione basati su MBO definiti a sistema, supporta interazioni sincrone e asincrone e aderisce a standard di integrazione quali SOAP e REST API.

I flussi di integrazione indicati al punto 1 e 3 non sono caratterizzati da complesse logiche di business. Del tutto differente è la natura del flusso di integrazione indicato al punto 2 il quale richiede la realizzazione di una logica applicativa necessaria per la trasformazione del data-model FDD in EAM e viceversa. Ovviamente questo tipo di trasformazione è specifica della realtà del cliente Terna e pertanto non è indirizzata nativamente dal prodotto. Per questo motivo è stato disegnato un componente applicativo secondo le logiche di trasformazione raccolte durante la fase di design logico funzionale che si avvarrà degli strumenti di integrazione del MIF.

Si riporta di seguito il diagramma di flusso dello scenario di integrazione al punto 2. Come si evince dal diagramma il flusso di integrazione si articola secondo i seguenti passi (steps):

Registrazione modifiche anagrafica: il connettore è in ascolto delle modifiche apportate agli MBO che rappresentano la anagrafica. Queste modifiche saranno raccolte e memorizzate in strutture dati accessorie in modo rapido ed efficiente per essere elaborate successivamente (secondo le policy di avvio della sincronizzazione) senza impattare le prestazioni dell’operatività della anagrafica. L’anagrafica è agnostica rispetto a questo passo e sarà possibile disattivare, mediante configurazione, questo aggancio ottenendo di fatto lo spegnimento del connettore.

Identificazione operazioni di sincronizzazione: questo passo e successivi saranno avviati mediante un CRON-task secondo una programmazione definita a sistema. L’esecuzione avverrà in background e non impatterà le attività degli utenti. Dal punto di vista architetturale sarà possibile configurare MAS Manage ad eseguire i CRON-task su un server bundle dedicato. Questo passo sarà responsabile di elaborare le modifiche della anagrafica registrate nel passo 1 e riflettere le modifiche e le azioni di sincronizzazione sulla struttura dati della anagrafica SAP (sedi tecniche ed equipement) memorizzata in EAM.

Predisposizione messaggio di integrazione: questo passo elaborerà le sedi tecniche ed equipement su EAM in attesa di sincronizzazione e collezionerà i dati necessari alla sincronizzazione (dati di testata e caratteristiche) applicando le regole di mapping fornite da Terna.

Sincronizzazione: questo ultimo passo effettuerà l’effettiva chiamata verso SAP dei servizi di sincronizzazione (avvalendosi del MIF) per allineare l’anagrafica tecnica su SAP.

Figura 1. Diagramma di flusso scenario di sincronizzazione anagrafica linee/stazioni

# Prerequisiti tecnici per la comprensione del documento

Il presente documento indirizza aspetti di low-level design; pertanto, i concetti espressi nel documento saranno estremamente tecnici e calati su aspetti funzionali del prodotto che occorre padroneggiare per una corretta interpretazione. Per questo motivo verrà fornito un elenco di conoscenze tecniche necessarie fornendone un rapido inquadramento senza l’obiettivo di essere esaustivo.

Object Oriented Programming: metodologia di programmazione orientata agli oggetti nello specifico utilizzata mediante il linguaggio di programmazione Java. Il connettore farà ampio uso di tecnologia Java per la realizzazione della sincronizzazione di linee/stazioni EAM verso SAP.

Data design per database relazionali: MAS Manage persiste i dati in un database relazionale (per Terna il database adottato è Oracle). Pertanto, è necessario avere nozioni di design di un modello dati basato su entità/relazioni e conoscenze relative a tabelle, viste, indici, chiavi primarie ed esterne, sequenze, linguaggio SQL.

MAS Manage API: il MAS Manage fornisce una API per la realizzazione delle funzionalità out-of-box e può essere utilizzata per la realizzazione di funzionalità aggiuntive.

MAS Manage automation script: il MAS Manage fornisce la possibilità di definire script di automazione eseguiti automaticamente dal prodotto in base ad eventi (launch points) configurati dal programmatore. Questi script possono essere realizzati con un linguaggio di scripting supportato dal motore Oracle Nashorn (ad oggi JavaScript, Python e PHP). Come indicato nella documentazione di prodotto un ulteriore linguaggio supportato è il Jython. Il connettore farà uso di automation script per la realizzazione di regole di mapping che richiedono elaborazioni estremamene specifiche.

MAS Manage MIF: essendo il connettore un componente dedicato alla integrazione con i sistemi esterni, si avvarrà ampiamente del layer di integrazione fornito dal prodotto. Per la corretta comprensione di questo strumento è necessario avere competenze in merito a tematiche di sicurezza, standard di integrazione quali SOAP e REST API, sistemi di gestione messaggi quali JMS e Apache Kafka, XML (in particolare fogli di trasformazione XSLT e WSDL)

MAS Deployment per applicazioni custom: il MAS fornisce una procedura per il rilascio di classi custom (nella fattispecie le classi Java del connettore) che verranno integrate nelle classi di prodotto e rilasciate durante il deployment su tutti i server bundle.

DevOps: metodologia per il rilascio strutturato di progetti di sviluppo software.

È necessaria, infine, una completa comprensione dei documenti di high-level design relativi al connettore, alla anagrafica e al data model EAM.

# Architettura connettore

## Struttura del progetto

Questo componente è realizzato prevalentemente con tecnologia Java e si fonda sulle API di IBM MAS Manage. Il progetto è realizzato mediante IDE (Integrated Development Environment) Eclipse for Java Developers. Sono previsti due progetti come mostrato nella figura seguente.

Figura 2. Progetti Eclipse

Il progetto connettore-bo è un progetto Java (soggetto quindi alla compilazione di classi Java) che contiene le classi per la realizzazione dei passi citati nel paragrafo di introduzione. Il progetto library-bo è un progetto di tipo generale (non soggetto a compilazione) che contiene le librerie necessarie per la compilazione del progetto connettore-bo. Al momento l’unica libreria necessaria è il businessobjects.jar che fornisce le classi di prodotto che realizzano il TPAE (MAS Manage API). Entrambi i progetti sono caricati sull’ambiente DevOps Terna e configurati per l’avvio di uno stream di build automatizzata mediante tecnologia Ant. Il processo di build produce un file compresso che contiene le classi compilate del progetto connettore-bo organizzate secondo l’alberatura di directories richiesta dal MAS Manage per il deployment. Questo file compresso rappresenta l’unità di deployment applicativa del connettore che può essere utilizzata per il rilascio secondo le modalità di deployment previste dal MAS Manage. Il progetto connettore-bo è compilato con la versione di JDK (Java Development Kit ) 1.8.

Figura 3. Versione JDK

Come si evince dalla figura seguente il progetto connettore-bo utilizza le librerie del Java run-time e la libreria del TPAE.

Figura 4. Librerie progetto connettore-bo

Il progetto Java connettore-bo è strutturato in package ovvero una alberatura (che si riflette in directory su file-system) che permette di organizzare in modo strutturato le classi del progetto.

Figura 5. Struttura package progetto connettore-bo

Il package principale che contiene l’intero progetto è com.terna avendo seguito la convenzione indicata dal Java software designer. Al di sotto del package com.terna sono presenti i seguenti sotto package:

common: package per implementazioni di utilities generiche non dipendenti dal connettore e potenzialmente riusabili da altri componenti. Si notino ad esempio gli ulteriori package di quarto livello che raggruppano le utilities per funzionalità (ad esempio il package date contiene classi per la manipolazione avanzata delle date).

integration: package che contiene le classi del connettore

asset: package che contiene classi invocate dalle applicazioni di anagrafica

testing: package che contiene le classi per le suite di test automation

Il codice del connettore è interamente scritto in lingua inglese sia perché di fatto è la lingua standard per la codifica sia perché lascia la porta aperta alla gestione del codice a team stranieri. Pertanto, fatto salvo specifiche nomenclature non traducibili (ad esempio gli oggetti di una linea e/o stazione), il codice è scritto e documentato in inglese. Il codice è ampiamente corredato di documentazione in linea secondo la codifica JavaDoc il che permette una rapida comprensione del codice durante la consultazione, nonché l’estrazione della documentazione stessa mediante strumenti forniti dal JDK e creazione di una wiki consultabile via web-browser.

Figura 6. Esempio codice Java con documentazione in formato JavaDoc

Figura 7. Esempio JavaDoc per una classe

## Servizio di integrazione

Il connettore definisce il servizio MAS Manage X3TERNAINT per la gestione delle cache necessarie e di funzionalità generiche e potenzialmente esposte come web services. Un servizio MAS Manage è una classe Java che viene inizializzata dal prodotto al suo avvio su ogni istanza di esecuzione e distrutta al fermo dell’istanza stessa. Un servizio MAS Manage segue il pattern singleton per l’inizializzazione di strutture dati quali cache e configurazioni e permette di esporre web services mediante la notazione Java @WebMethod. Il servizio di integrazione è definito a sistema nella applicazione Database Configuration come illustrato nella figura seguente

Figura 8. Configurazione servizio di integrazione connettore

Il servizio nel dettaglio gestisce:

La configurazione del connettore

La cache delle classificazioni usate dal connettore

La cache della mappatura elementi Maximo/SAP (X3MAXIMOSAPMAP)

L’esecuzione di unit-test (invocabili anche via web service)

L’esecuzione della sincronizzazione del connettore (invocabile anche via web service)

## Proprietà di configurazione

Il connettore offre alcune proprietà di configurazione memorizzate nella tabella X3CONNECTORPROP. Nella tabella seguente sono riportate le proprietà disponibili.

| PROPRIETA’ | DESCRIZIONE |
| --- | --- |
| connector.enabled | Se impostato a 1 il connettore SAP è abilitato. Questa proprietà è relativa agli step 2, 3 e 4 e non allo step 1. |
| connector.step2.enabled | Se impostato a 1 lo step 2 è abilitato. |
| connector.step3.enabled | Se impostato a 1 lo step 3 è abilitato. |
| connector.step4.enabled | Se impostato a 1 lo step 4 è abilitato. |
| objectchange.cleanup.enabled | Se impostato a 1 il connettore effettuerà la pulizia dei record in X3OBJECTCHANGE non in errore |
| objectchange.cleanup.retention | Saranno cancellati solo i record in X3OBJECTCHANGE non in errore con data di processamento più vecchia dei giorni specificati |
| connector.trans.cleanup.enabled | Se impostato a 1 il connettore effettuerà la pulizia dei record in X3CONNECTORTRANS |
| connector.trans.cleanup.retention | Saranno cancellati solo i record in X3CONNECTORTRANS con data di processamento più vecchia dei giorni specificati |
| connector.step4.maxthreads | Numero massimo di Thread per la sincronizzazione degli Asset SAP |
| connector.stats.sql.enabled | Se impostato a 1 il connettore traccerà informazioni statistiche in merito alla esecuzione di query SQL |
| connector.stats.sql.statements | Se impostato a 1 il connettore traccerà informazioni di dettaglio per tutte le query eseguite |
| connector.stats.sql.threshold | Soglia oltre la quale il connettore traccerà query che hanno richiesto un tempo di esecuzione superiore alla soglia |
| connector.stats.sql.timethreshold | Soglia oltre la quale il connettore traccerà query che hanno richiesto un tempo di esecuzione superiore alla soglia |
| connector.scheduler.enabled | Se impostato ad 1 verrà utilizzata la pianificazione presente in X3CONNECTORPLAN |
| connector.step3.pendingonly | Se impostato ad 1 lo step 3 elaborerà solo gli asset SAP con flag STEP3PENDING impostato ad 1 |

Tabella 1. Proprietà di configurazione connettore

## Statistiche di esecuzione

Il connettore può essere abilitato per il tracciamento di alcune statistiche di esecuzione che permettono di avere dettagli sui tempi e volumi di esecuzione. Le statistiche sono memorizzate nella tabella X3CONNECTORTRANS.

Per ogni esecuzione del CRON task viene registrata una statistica per ogni step (l’attributo PHASE può essere STEP 2, STEP 3 o STEP 4) e le statistiche complessive dell’esecuzione (attributo PHASE uguale a CONNETTORE). Ogni record di statistica indica il totale di record processati (TOTAL), i record processati con successo (PROCESSED), i record falliti (FAILED), lo stato dell’esecuzione (STATUS), l’orario di inizio (STARTDATE) e di fine (FINISHDATE), il tempo totale in millisecondi impiegato (ELAPSED) e relativa rappresentazione in formato orario (ELAPSEDALN), il tempo totale per operazioni su database in millisecondi impiegato (DBELAPSED) e relativa rappresentazione in formato orario (DBELAPSEDALN), ), il tempo totale per operazioni di rete in millisecondi impiegato (NETWORKELAPSED) e relativa rappresentazione in formato orario (NETWORKELAPSEDALN). Ovviamente i tempi di rete sono relativi solo allo step 4.

Inoltre, per ogni record di statistica è riportato anche un log con informazioni di dettaglio quali ad esempio query eseguite, query critiche dal punto di vista delle prestazioni, numero di MBO (in memoria) creati. Si riporta un log di esempio.

Figura 9. Dati prestazioni esecuzione del connettore

Il tuning delle informazioni prestazionali può essere effettuato mediante le proprietà indicate nel paragrafo 3.3.

## Step 1: registrazione modifiche anagrafica

Lo step 1 ha l’obiettivo di registrare nella tabella X3OBJECTCHANGE in modo implicito (laddove possibile) o esplicito (mediante chiamate ad API fornite dal connettore) le modifiche apportate alla anagrafica. Il meccanismo implicito è basato sul modello publish/subscriber fornito da MAS Manage che permette ad una applicazione di registrarsi per eventi di modifica (ADD/UPDATE/DELETE) verso i MBO designati senza che il produttore delle modifiche sia impattato. Nella tabella seguente sono riportati gli MBO su cui il connettore è registrato. Si noti che per il MBO ASSET non sono previsti eventi di DELETE in quanto non è prevista l’effettiva cancellazione degli asset ma in caso di dismissione sarà previsto un opportuno cambiamento di stato che verrà intercettato tramite l’evento di UPDATE.

| MBO | Eventi |
| --- | --- |
| ASSET | ADD, UPDATE |
| ASSETLOCRELATION | ADD, UPDATE, DELETE |
| ASSETSPEC | ADD, UPDATE, DELETE |
| X3TRONCHI | ADD, UPDATE, DELETE |

Tabella 1. Eventi su cui il connettore è in ascolto

Per quanto riguarda la gestione di scenari complessi (ad esempio quelli legati alla ricodifica) le applicazioni della anagrafica invocano opportune API del connettore al fine di inviare (sempre tramite il meccanismo publish/subscriber) eventi ad hoc. Si riportano nei paragrafi successivi i dettagli delle API esposte dal connettore per ogni scenario supportato.

La gestione delle transazioni è demandata al chiamante il quale ha due possibilità.

Condividere la transazione di modifica della anagrafica con il connettore

Eseguire le chiamate del connettore in una transazione dedicata (T2) che avviene in seguito al commit della transazione (T1) usata dello script

Nel secondo caso è necessario innanzi tutto disabilitare il connettore affinché non elabori le modifiche apportate durante la transazione T1. La chiamata seguente disabilita il connettore limitatamente al thread di esecuzione del chiamante (pertanto non interferisce con altri scenari utente).

// imposta il connettore ad ignorare eventi di modifica fino alla successiva commit IntegrationService.getService().setScenario(SapConnectorScenario.SKIP);

Nel caso in cui lo script utilizzi più transazioni sarà necessario ripetere la chiamata prima di ogni commit in quanto il connettore a seguito di ogni commit ritorna automaticamente allo scenario base.

Successivamente, al termine delle chiamate al connettore sarà necessario indicare il commit della transazione T2 mediante la seguente chiamata.

// indica al connettore il completamento della sua transazione IntegrationService.getService().commitScenario();

In entrambi i casi, al fine di evitare impatti in termini di tempo sulla esecuzione dello script, il connettore delegherà l’elaborazione degli eventi prodotti dalle chiamate ad un consumer thread in carico dell’arricchimento degli eventi con le informazioni necessarie e memorizzazione nella tabella X3OBJECTCHANGE.

### Scenario 3: ammazzettamento

L’ammazzettamento è una operazione fornita dalla gestione della anagrafica che si applica ai conduttori e attacchi conduttore di una campata e relativo sostegno in doppia in terna (supponiamo di identificare le due linee in doppia terna come linea A e linea B). L’operazione prevede la dismissione di una campata e relativo sostegno su una linea (ad esempio la linea A) previo spostamento dei suoi conduttori e attacchi conduttore sull’altra linea in doppia terna (linea B). Si rammenta che gli asset di terzo livello delle linee afferenti ad una specifica linea (in questo scenario i terzi livelli CONDUTTORI e ATTACCHI CONDUTTORE) hanno l’attributo X3ROOTASSETNUM impostato con il numero dell’asset (ASSETNUM) della linea. Si riporta nella figura seguente la procedura di ammazzettamento dell’aggregato CONDUTTORI.

Figura 10. Ammazzettamento aggregato CONDUTTORI

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API da richiamare esplicitamente dallo script di ammazzettamento per comunicare le modifiche.

import com.terna.integration.service.IntegrationService; import com.terna.integration.sapconnector.SapConnectorScenario;

import com.terna.integration.sapconnector.SapConnectorScenarioOperation;

import com.terna.integration.sapconnector.step1.handler.ammazzettamento.DeleteCampataArg; import com.terna.integration.sapconnector.step1.handler.ammazzettamento. DeleteSostegnoArg;

import com.terna.integration.sapconnector.step1.handler.ammazzettamento.MoveConduttoriArg;

import com.terna.integration.sapconnector.step1.handler.ammazzettamento. MoveAttacchiConduttoreArg;

// impostazione scenario IntegrationService.getService().setScenario(SapConnectorScenario.AMMAZZETTAMENTO);

// spostamento conduttori MoveConduttoriArg moveConduttoriArgs = new MoveConduttoriArg(); moveConduttoriArgs.asset = …; // MBO che identifica l’asset CONDUTTORI

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.AMMAZZETTAMENTO_MOVE_CONDUTTORI,

moveConduttoriArgs);

// spostamento attacchi conduttore MoveAttacchiConduttoreArg moveAttacchiConduttoreArgs = new MoveAttacchiConduttoreArg();

moveAttacchiConduttoreArgs.asset = …; // MBO che identifica l’asset ATTACCHI CONDUTTORE

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.AMMAZZETTAMENTO_MOVE_ATTACCHI_CONDUTTORE,

moveAttacchiConduttoreArgs);

// cancellazione campata DeleteCampataArg deleteCampataArgs = new DeleteCampataArg();

deleteCampataArgs.relation = …; // MBO che identifica la relazione cancellata

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.AMMAZZETTAMENTO_DELETE_CAMPATA,

deleteCampataArgs);

// cancellazione sostegno

DeleteSostegnoArg deleteSostegnoArgs = new DeleteSostegnoArg();

deleteSostegnoArgs.relation = …; // MBO che identifica la relazione cancellata

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.AMMAZZETTAMENTO_DELETE_SOSTEGNO,

deleteSostegnoArgs); // salvataggio scenario (necessario nel caso si fosse utilizzata una transazione dedicata)

IntegrationService.getService().commitScenario();

Gli aggregati CONDUTTORI e ATTACCHI CONDUTTORE precedentemente ammazzettati possono essere riusati su una nuova linea in doppia terna (linea C). Le operazioni da chiamare saranno di natura diversa rispetto al flusso di ammazzettamento. Le operazioni prevedono l’aggiunta della campata e sostegno di destinazione e la successivamente lo spostamento dei conduttori e attacchi conduttori precedentemente ammazzettati verso la nuova campata/sostegno:

import com.terna.integration.service.IntegrationService; import com.terna.integration.sapconnector.SapConnectorScenario;

import com.terna.integration.sapconnector.SapConnectorScenarioOperation;

import com.terna.integration.sapconnector.step1.handler.ammazzettamento.AddCampataArg;

import com.terna.integration.sapconnector.step1.handler.ammazzettamento. AddSostegnoArg;

import com.terna.integration.sapconnector.step1.handler.ammazzettamento.ReuseConduttoriArg; import com.terna.integration.sapconnector.step1.handler.ammazzettamento. ReuseAttacchiConduttoreArg; // impostazione scenario IntegrationService.getService().setScenario(SapConnectorScenario.AMMAZZETTAMENTO);

// aggiunta campata AddCampataArg addCampataArgs = new AddCampataArg();

addCampataArgs.relation = …; // MBO che identifica la relazione creata

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.AMMAZZETTAMENTO_ADD_CAMPATA,

addCampataArgs);

// aggiunta sostegno AddSostegnoArg addSostegnoArgs = new AddSostegnoArg(); addSostegnoArgs.relation = …; // MBO che identifica la relazione creata

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation. AMMAZZETTAMENTO_ADD_SOSTEGNO,

addArgs);

// spostamento conduttori ReuseConduttoriArg reuseConduttoriArgs = new ReuseConduttoriArg();

reuseConduttoriArgs.prevRootAsset = …; // MBO che identifica l’asset LINEA da cui // si attingono i conduttori

reuseConduttoriArgs.prevSiblingAsset = …; // MBO che identifica l’asset CONDUTTORI non spostato

reuseConduttoriArgs.asset = …; // MBO che identifica l’asset CONDUTTORI oggetto dello // spostamento

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.AMMAZZETTAMENTO_REUSE_CONDUTTORI,

reuseConduttoriArgs); // spostamento attacchi conduttore ReuseAttacchiConduttoreArg reuseAttacchiConduttoreArgs = new ReuseAttacchiConduttoreArg();

reuseAttacchiConduttoreArgs.prevRootAsset = …; // MBO che identifica l’asset LINEA da cui // si attingono i conduttori

reuseAttacchiConduttoreArgs.prevSiblingAsset = …; // MBO che identifica l’asset CONDUTTORI non spostato

reuseAttacchiConduttoreArgs.asset = …; // MBO che identifica l’asset CONDUTTORI // oggetto dello spostamento

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation. AMMAZZETTAMENTO_REUSE_ATTACCHI_CONDUTTORE,

reuseAttacchiConduttoreArgs); // salvataggio scenario (necessario nel caso si fosse utilizzata una transazione dedicata)

IntegrationService.getService().commitScenario();

### Scenario 4: ricodifica secondo livello linea

La ricodifica di un secondo livello EAM si applica solo per gli elementi di una linea ed in particolare alla sola CAMPATA AEREA e TRATTA CAVO (secondi livelli anagrafica SAP). Di fatto, in fase di rinomina l’applicazione della anagrafica allineerà la codifica dei rispettivi secondi livelli (SOSTEGNO AEREO ed ESTREMO CAVO/CAMERA GIUNTI rispettivamente). Il connettore avrà in carico la gestione della ricodifica dei corrispondenti elementi SAP (CAMPATE e TRATTA CAVO) e dei rispettivi figli.

Si presuppone che il nuovo SAP id non sia già in uso.

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche.

import com.terna.integration.service.IntegrationService; import com.terna.integration.sapconnector.SapConnectorScenario;

import com.terna.integration.sapconnector.SapConnectorScenarioOperation;

import com.terna.integration.sapconnector.step1.handler.rename.RenameArg; // impostazione scenario IntegrationService.getService().setScenario(SapConnectorScenario. RENAME_LINE_SECOND_LEVEL);

// ricodifica secondo livello RenameArg renameArgs = new RenameArg();

renameArgs.relation = …; // MBO che identifica la relazione da ricodificare

renameArgs.prevSapId = …; // SAP id precedente

renameArgs.newSapId = …; // nuovo SAP id

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.RENAME_LINE_SECOND_LEVEL,

renameArgs);

### Scenario 5: entra/esci

L’azione entra/esci consiste nello spostamento di asset CAMPATA AEREA e TRATTA CAVO di una linea (secondi livelli anagrafica SAP) unitamente a secondi livelli associati (SOSTEGNO AEREO ed ESTREMO CAVO/CAMERA GIUNTI rispettivamente) verso un’altra linea. Il connettore avrà in carico la gestione dell’aggiornamento dei corrispondenti elementi SAP (CAMPATE e TRATTA CAVO) e dei rispettivi figli.

Si presuppone che i SAP id associati agli asset spostati non siano già in uso.

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche.

import com.terna.integration.service.IntegrationService; import com.terna.integration.sapconnector.SapConnectorScenario;

import com.terna.integration.sapconnector.SapConnectorScenarioOperation;

import com.terna.integration.sapconnector.step1.handler.move.MoveArg; // impostazionescenario IntegrationService.getService().setScenario(SapConnectorScenario.MOVE_LINE_SECOND_LEVEL);

// spostamento asset secondo livello su nuova linea MoveArg moveArgs = new MoveArg();

moveArgs.relation = …; // MBO che identifica la relazione del secondo livello da spostare

moveArgs.newRootAsset = …; // MBO che identifica l’asset LINEA destinazione

moveArgs.newSapId = …; // SAP id da associare al secondo livello spostato IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.MOVE_LINE_SECOND_LEVEL,

moveArgs);

### Scenario 6: inversione linea

L’azione inversione linea consiste nel riordinamento dei secondi livelli EAM in ordine inverso con conseguente rinomina di campate/tratte cavo per conciliare la convenzione che l’X3SAPID dei seguenti sostegni/estremi cavo deve corrispondere. Inoltre, ogni estremo cavo di fine/inizio diventerà estremo cavo di inizio/fine (per cui la codifica dovrà cambiare da -H/-K in -K/-H). Questo procedimento preserva gli identificativi di sostegni, camera giunti ed estremi cavo (ad eccezione del suffisso) e non richiede quindi la riproduzione delle targhe di segnalazione.

Si consideri ad esempio la linea mista (tratta aerea e tratta cavo) riportata di seguito (l’ordinamento è da sinistra a destra).

L’operazione di inversione della applicazione di Anagrafica riordinerà i secondi livelli in ordine inverso (l’ordinamento è sempre da sinistra a destra).

Infine, applicherà la rinomina dei secondi livelli campata e tratta cavo e dei suffissi degli estremi cavo:

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche.

import com.terna.integration.service.IntegrationService; import com.terna.integration.sapconnector.SapConnectorScenario;

import com.terna.integration.sapconnector.SapConnectorScenarioOperation;

import com.terna.integration.sapconnector.step1.handler.inversion.RenameArg;

import com.terna.integration.sapconnector.step1.handler.inversion.MoveArg;

// impostazione scenario IntegrationService.getService().setScenario(SapConnectorScenario.LINE_INVERSION);

// rinomina asset secondo livello RenameArg renameArgs = new RenameArg ();

renameArgs.relation = …; // MBO che identifica la relazione del secondo livello

renameArgs.sapId = …; // SAP id precedente

renameArgs.newSapId = …; // nuovo SAP id

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.INVERSION_RENAME_SECOND_LEVEL,

renameArgs);

// spostamento asset secondo livello MoveArg moveArgs = new MoveArg();

moveArgs.relation = …; // MBO che identifica la relazione del secondo livello moveArgs.prevRelation = …; // MBO che identifica la relazione del secondo livello precedente

moveArgs.sapId = …; // SAP id precedente

moveArgs.newSapId = …; // nuovo SAP id

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.INVERSION_MOVE_SECOND_LEVEL,

moveArgs);

### Scenario 7: aggiunta secondo livello linee

L’aggiunta di secondi livelli per le linee viene tipicamente gestita mediante il meccanismo implicito. Si fornisce comunque anche il meccanismo esplicito a favore della anagrafica nel caso in cui avesse necessità di operare sulla creazione dei secondi livelli senza stimolare il connettore, per poi notificare le creazioni in un secondo momento.

import com.terna.integration.sapconnector.SapConnectorScenario;

import com.terna.integration.sapconnector.SapConnectorScenarioOperation;

import com.terna.integration.sapconnector.step1.handler.touch.AssetRelationAddArg;

// connettore disabilitato per la transazione corrente IntegrationService.getService().setScenario(SapConnectorScenario.SKIP);

// creazione relazione // … // salvataggio relazione (il connettore non registrerà alcun evento) // creazione argomenti AssetRelationAddArg args = new AssetRelationAddArg();

args.relation = …; // MBO relazione

// crezione secondo livello

IntegrationService.getService().setScenario(SapConnectorScenario.TOUCH);

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.TOUCH_ADD_ASSET_RELATION,

args);

// commit scenario IntegrationService.getService().commitScenario();

### Scenario 8: movimentazione equipment stazione

Sarà possibile movimentare gli elementi di quarto livello di una stazione (noti come equipment) all’interno della stessa stazione o verso un’altra stazione. Un elemento di quarto livello potrà essere spostato verso un terzo livello appartenente alla stazione di partenza oppure ad un terzo livello appartenente ad una differente stazione. Inoltre, un elemento di quarto livello può essere spostato sotto la stazione o sotto ad una differente stazione. Data la natura gerarchica (padre/figlio) degli elementi di una stazione la movimentazione consisterà (dal punto di vista della anagrafica) nel cambio dell’asset padre e relativo aggiornamento degli ID SAP.

Figura 11. Possibili spostamenti equipment stazione

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche. Come primo passo è necessario impostare lo scenario mediante la chiamata:

import com.terna.integration.service.IntegrationService; IntegrationService.getService().setScenario(SapConnectorScenario.MOVE_STATION_EQUIPMENT);

Successivamente occorre segnalare la movimentazione dell’asset EAM mediante la chiamata:

import com.terna.integration.sapconnector.SapConnectorScenario;

import com.terna.integration.sapconnector.SapConnectorScenarioOperation;

import com.terna.integration.sapconnector.step1.handler.stazione.move.MoveArg;

// movimentazione equipment stazione MoveArg moveArgs = new MoveArg();

moveArgs.asset = …; // MBO che identifica l’asset da movimentare

moveArgs.fromAsset = …; // MBO che identifica l’asset da cui l’asset verrà movimentato

moveArgs.toAsset = …; // MBO che identifica l’asset verso cui l’asset verrà movimentato

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.MOVE_STATION_EQUIPMENT,

moveArgs);

### Scenario 9: ricodifica terzo livello stazione

Sarà possibile ricodificare i terzi livelli una stazione. La ricodifica del X3SAPID in questo caso riguarda la sola parte a scelta dell’utente quindi i soli ultimi due caratteri del terzo segmento (in quanto i primi due sono associati alla classificazione dell’asset). Ad esempio, dato un trasformatore con codifica 32525C-77-1200, è possibile ricodificarlo in 32525C-77-1210.

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche.

import com.terna.integration.service.IntegrationService; import com.terna.integration.sapconnector.SapConnectorScenario;

import com.terna.integration.sapconnector.SapConnectorScenarioOperation;

import com.terna.integration.sapconnector.step1.handler.stazione.techsite.rename.RenameArg;

// impostazione dello scenario IntegrationService.getService().setScenario(SapConnectorScenario. RENAME_STATION_THIRD_LEVEL); // preparazione parametri

RenameArg renameArgs = new RenameArg();

renameArgs.asset = …; // asset di terzo livello

renameArgs.prevSapId = …; // SAP id precedente

renameArgs.newSapId = …; // nuovo SAP ID

// invocazione rinomina

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.RENAME_STATION_THIRD_LEVEL,

renameArgs); // commit scenario IntegrationService.getService().commitScenario();

### Scenario 10: movimentazione secondo/terzo livello stazione

Sarà possibile movimentare gli elementi di una stazione di secondo/terzo livello verso un valido primo/secondo livello di un’altra stazione. Data la natura gerarchica (padre/figlio) degli elementi di una stazione la movimentazione consisterà (dal punto di vista della anagrafica) nel cambio dell’asset padre e relativo aggiornamento degli ID SAP.

Questo scenario è basato sul meccanismo esplicito, pertanto, il connettore esporrà una API per indicare le modifiche.

import com.terna.integration.service.IntegrationService; import com.terna.integration.sapconnector.SapConnectorScenario;

import com.terna.integration.sapconnector.SapConnectorScenarioOperation;

import com.terna.integration.sapconnector.step1.handler.stazione.techsite.move. MoveAssetArg; // impostazione scenario IntegrationService.getService().setScenario(SapConnectorScenario.MOVE_STATION_ASSET); // preparazione parametri MoveAssetArg moveArgs = new MoveAssetArg();

moveArgs.asset = …; // MBO asset da movimentare

moveArgs.parentAsset = …; // MBO asset padre precedente

moveArgs.newParentAsset = …; // MBO asset padre // invocazione movimentazione

IntegrationService.getService().callScenarioOperation(

SapConnectorScenarioOperation.MOVE_STATION_ASSET,

moveArgs);

// commit scenario IntegrationService.getService().commitScenario();

## Avviamento step 2/3/4

L’avviamento degli step successivi al primo è demandato al CRON task X3SAPCONNECTOR programmato per l’esecuzione periodica come mostrato nella figura seguente.

Figura 12. CRON task per l'avvio degli step 2, 3 e 4

Si tenga presente che l’esecuzione dei CRON tasks è eseguita da una istanza MAS Manage del cluster CRON, pertanto, la sua esecuzione non interferisce dal punto di vista del consumo di risorse con le istanze dedicata alla user interface. Inoltre, nel caso di più istanze di MAS Manage nel cluster CRON, MAS Manage assicura l’esecuzione del CRON task su di una sola istanza. In sostanza la configurazione assicura che il CRON task non sarà eseguito in parallelo.

Il CRON task durante l’esecuzione segue le configurazioni impostate nelle proprietà del connettore. Prima di tutto se la proprietà connector.enabled è impostata a 0 di fatto il CRON task termina immediatamente. A seguire il CRON task eseguirà in ordine gli step 2, 3 e 4 compatibilmente con le configurazioni delle proprietà connector.stepx.enabled. Ad esempio, se la proprietà connector.step4.enabled è impostata a 0 il CRON task eseguirà solo gli step 2 e 3.

### Pianificazione step

È possibile definire una pianificazione in base a fasce orarie su quali step eseguire. Innanzi tutto, per abilitare la pianificazione è necessario impostare ad 1 la proprietà connector.scheduler.enabled. Si tenga presente che, con pianificazione abilitata o meno, l’esecuzione di uno step è condizionata dalla sua abilitazione mediante la proprietà connector.stepx.enabled.

Ad ogni esecuzione del CRON task viene valutata la configurazione di esecuzione degli step in specifiche fasce orarie contenuta nella tabella X3CONNECTORPLAN. Si riporta nella figura seguente un esempio di pianificazione.

Figura 13. Pianificazione step 2, 3 e 4

In questa tabella di pianificazione è possibile indicare nel campo DAY il giorno della settimana a cui si applica la pianificazione. Possibili valori sono:

WEEKDAY: giorno della settimana (dal lunedì alla domenica)

WORKDAY: giorno lavorativo (dal lunedì al venerdì)

WEEKEND: giorno del finesettimana (sabato e domenica)

Nel campo RANGE è possibile definire la finestra temporale nell’arco della giornata, espressa nel formato HH24:MM. A parità di valore DAY non è possibile definire RANGE sovrapposti.

Nel campo STEPS è possibile definire gli step da eseguire nella particolare fascia oraria. Possibili valori sono:

2: solo step 2

3: solo step 3

4: solo step 4

2-3: step 2 e 3

3-4: step 3 e 4

2-3-4: step 2, 3 e 4

Il campo ACTIVE indica se la programmazione è attiva (1) o meno (0).

Infine, il campo LASTRUN riporta l’orario di inizio dell’ultima esecuzione avviata per la programmazione.

La necessità di avere questo particolare livello di pianificazione nasce dalla esigenza di attivare in specifiche fasce orarie (notturne) gli step di sincronizzazione verso SAP (step 4) dando comunque la possibilità di avviare gli step precedenti periodicamente al fine di anticipare quanto più possibile le fasi preparatorie alla effettiva sincronizzazione.

Oltre alla programmazione appena vista, il CRON task privilegia l’esecuzione di eventuali sincronizzazioni richieste mediante l’applicazione di monitoraggio degli errori di sincronizzazione. Quando viene richiesta la sincronizzazione di una linea/stazione in errore, la richiesta viene registrata nella tabella X3CONNECTORQUEUE. Prima di iniziare la sincronizzazione canonica, le linee/stazioni in attesa di risincronizzazione verranno sincronizzate.

Figura 14. Applicazione monitoraggio errori

## Step 2: Identificazione operazioni di sincronizzazione

Lo step 2 è responsabile della elaborazione dei record prodotti dallo step 1 e registrati nella tabella X3OBJECTCHAGE. Lo step 2 è stato concepito per essere aperto alla gestione separata (se necessario) di scenari differenti mediante handler dedicati. Ad esempio, lo scenario di inversione di una linea ha un handler dedicato vista la sua peculiarità.

Lo step 2 viene governato dallo step manager il quale invoca a rotazione gli handler disponibili che tentano di gestire i record in attesa di elaborazione nella X3OBJECTCHAGE. Quando tutti gli handler non sono più in grado di gestire record (perché terminati o per eventuali situazioni di errore) lo step manager terminerà la sua esecuzione. Durante la sua esecuzione, lo step manager memorizza in una black list eventuali linee/stazioni per cui è stato riscontrato un errore di elaborazione. In questo modo gli handler salteranno record non processati per le linee/stazioni appartenenti alla black list.

Lo step 2 ottimizza l’esecuzione dello step 3 marcando i record aggiornati dallo step 1 impostando il flag STEP3PENDING a 1. Questo permetterà allo step 3 di ricalcolare solo i dati effettivamente aggiornati rispetto all’ultimo calcolo. Questo comportamento può essere abilitato impostando la proprietà connector.step3.pendingonly a 1. Impostando tale proprietà a 0 lo step 3 ricalcolerà i dati per tutte le sedi tecniche da sincronizzare.

## Step 3: Predisposizione messaggio di integrazione

Questo passo elaborerà le sedi tecniche ed equipement su EAM in attesa di sincronizzazione e collezionerà i dati SAP (dati di testata e caratteristiche) necessari alla sincronizzazione applicando le regole di mapping fornite da Terna.

L’elaborazione dei dati di testata e delle caratteristiche è programmabile mediante regole definite nel MBO X3SAPASSETSPECRULE. Per ogni regola è necessario specificare

SAPTYPE: la natura del dato (ATTRIBUTE per un attributo di testata e SPEC per una caratteristica)

SAPNAME: il nome dell’attributo/caratteristica SAP (ad esempio PMT_ANNO_INSTALLAZIONE)

SAPCLASS: tipologia della sede tecnica o equipment (ad esempio CAMERA GIUNTI)

MAXNAME: il nome dell’attributo o della specifica tecnica corrispondente in EAM (ad esempio LIN_ANNO_INSTALLAZIONE)

RULE: la regola di mapping da utilizzare (ad esempio GetMostRecentSpec)

MAXRELATIONSHIP: la relazione (clausola where SQL) per ottenere il MBO EAM da cui ricavare il dato

DEFAULTVALUE: valore di default

Le regole applicabili sono prevalentemente regole generiche identificate durante la fase di design e sono realizzate nativamente al fine di massimizzarne le prestazioni. Nel caso in cui fosse necessaria una implementazione specifica è possibile definire una regola di tipo Custom o MultiCustom che delega il calcolo del dato ad uno script di automazione inserito a sistema.

Si riporta l’elenco completo delle regole:

Static: ritorna il valore di default indicato nella regola

Count: ritorna il numero di asset corrispondenti alla relazione

GetMboAttribute: ritorna il valore dell’attributo del MBO corrispondente alla relazione

GetMostRecentSpec: ritorna il valore della specifica tecnica più recente fra gli MBO identificati dalla relazione

SumSpecValues: ritorna la somma dei valori delle specifiche tecniche corrispondenti agli MBO identificati dalla relazione

MaxSpecValues: ritorna il massimo dei valori delle specifiche tecniche corrispondenti agli MBO identificati dalla relazione

GetSpec: ritorna il valore della specifica tecnica corrispondente al MBO identificati dalla relazione

HasAssets: ritorna 1 se esiste almeno un asset corrispondente agli MBO identificati dalla relazione

Custom: esegue uno script di automazione che calcola il valore

MultiCustom: esegue uno script di automazione che calcola il valore sotto forma di lista multi-valore

## Step 4: Sincronizzazione

Questo ultimo passo effettua le chiamate verso SAP dei servizi di sincronizzazione (avvalendosi del MIF) per allineare l’anagrafica tecnica su SAP.

Al fine di parallelizzare, laddove possibile, i flussi di integrazione è possibile configurare il livello di parallelismo tramite la proprietà connector.step4.maxthreads. Al di là del livello di parallelismo configurato, lo step 4 garantisce la sincronizzazione partendo dai livelli gerarchici più bassi (1, 2, 3, 4, e 5).

Si riporta un esempio di template di chiamata di sincronizzazione di una sede tecnica.

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:urn="urn:sap-com:document:sap:soap:functions:mc-style">

<soapenv:Header/>

<soapenv:Body>

<urn:Zfmpms4FunclocManage>

<ICity></ICity>

<ICountry></ICountry>

<IEqfnr></IEqfnr>

<IFltyp></IFltyp>

<IGewrk></IGewrk>

<IInbdt></IInbdt>

<IIngrp></IIngrp>

<IIwerk></IIwerk>

<IName1></IName1>

<IName2></IName2>

<IPltxt></IPltxt>

<IPosnr></IPosnr>

<IPostCode1></IPostCode1>

<IRbnr></IRbnr>

<IRegion></IRegion>

<IStreet></IStreet>

<IStrno></IStrno>

<ISttxu></ISttxu>

<ISwerk></ISwerk>

<ITplkz></ITplkz>

<ITplma></ITplma>

<ITplnr></ITplnr>

<IWergw></IWergw>

<ItCharacteristics>

<!--Zero or more repetitions:-->

<item>

<CharacteristicName></CharacteristicName>

<CharacteristicValue></CharacteristicValue>

</item>

</ItCharacteristics>

</urn:Zfmpms4FunclocManage>

</soapenv:Body>

</soapenv:Envelope>

## Sincronizzazione di anagrafiche accessorie (non tecniche)

### Centri di Lavoro

Questo flusso servirà ad alimentare la tabella X3GEWRK che conterrà l’anagrafica dei Centri di Lavoro. I valori di questa tabella verranno usati come dominio per l’attributo X3GEWRK della tabella ASSET.

Di seguito viene indicato l’elenco dei campi che fanno parte del servizio e il relativo mapping con i corrispondenti attributi SAP.

| CAMPO SERVIZIO | DESCRIZIONE CAMPO | ATTRIBUTO SAP |
| --- | --- | --- |
| GEWRK | Centro di lavoro | GEWRK |
| SWERK | Divisione | SWERK, WERGW o IWERK |
| TXT_GEWRK | Descrizione Centro di Lavoro | TXT_GEWRK |

Il servizio lavora in modalità Sync, ossia può essere usato sia per creare nuovi record che per aggiornarli: la chiave che identifica un record e che viene usata per determinare se l’azione deve essere Add o Update è costituita dai campi GEWRK e SWERK.

Il WSDL è recuperabile all’indirizzo

https://masdev-mea.manage.dev.apps.dev.azopencs.servizi.prv/meaweb/wsdl/X3GEWRK.wsdl

e per comodità viene allegato in questo documento

Si riporta un esempio di chiamata al servizio

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:max="http://www.ibm.com/maximo">

<soapenv:Header/>

<soapenv:Body>

<max:SyncX3GEWRK>

<max:X3GEWRKSet>

<max:X3GEWRK>

<max:GEWRK>EVITT3</max:GEWRK>

<max:SWERK>EVL</max:SWERK>

<max:TXT_GEWRK>upp. Tecn. Gest. Linee Imp. Firenze</max:TXT_GEWRK>

</max:X3GEWRK>

</max:X3GEWRKSet>

</max:SyncX3GEWRK>

</soapenv:Body>

</soapenv:Envelope>

Quando una chiamata va a buon fine la risposta ha 200 come http status code e ha una struttura di questo tipo:

<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">

<soap:Body>

<SyncX3GEWRKResponse creationDateTime="2023-07-13T10:05:32+00:00" transLanguage="EN" baseLanguage="EN" messageID="7207016892355335353407" xmlns="http://www.ibm.com/maximo" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>

</soap:Body>

</soap:Envelope>

Quando una chiamata fallisce la risposta ha 500 come http status code e ha una struttura di questo tipo:

<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">

<soap:Body>

<soap:Fault>

<faultcode>soap:Server</faultcode>

<faultstring>BMXAA4049E - The value specified exceeds the maximum field length that is allowed for this attribute: Value = EVITT3ABCDEF, Object Name=X3GEWRK, Attribute Name=GEWRK, Maximum Length=8. Specify a value that is less than the maximum length of 8.</faultstring>

</soap:Fault>

</soap:Body>

</soap:Envelope>

### Sedi Tecniche di indisponibilità

Questo flusso servirà ad alimentare la tabella X3SEDIINDISP che conterrà l’anagrafica delle sedi tecniche di indisponibilità. I valori di questa tabella verranno usati come dominio per l’attributo LIN_ELEMENTO_INDISP degli asset di primo e terzo livello classificati come LINEA, LINEA \ CAMPATA AEREA \ CONDUTTORI o LINEA \ TRATTA CAVO \ CAVI.

Di seguito viene indicato l’elenco dei campi che fanno parte del servizio e il relativo mapping con i corrispondenti attributi SAP.

| CAMPO SERVIZIO | DESCRIZIONE CAMPO | ATTRIBUTO SAP |
| --- | --- | --- |
| STRNO | Sede Tecnica | STRNO |
| TPLNR | Sede Tecnica | TPLNR |
| STTXT | Stato Sistema | STTXT |
| PLTXT | Definizione Sede Tecnica | PLTXT |
| TIPOELEMENTO | Tipo di Sede Tecnica | ATNAM |

Il servizio lavora in modalità Sync, ossia può essere usato sia per creare nuovi record che per aggiornarli: la chiave che identifica un record e che viene usata per determinare se l’azione deve essere Add o Update è costituita dal campo TPLNR.

Il WSDL è recuperabile all’indirizzo

https://masdev-mea.manage.dev.apps.dev.azopencs.servizi.prv/meaweb/wsdl/X3SEDIINDISP.wsdl

e per comodità viene allegato in questo documento

Esempio di chiamata al servizio

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:max="http://www.ibm.com/maximo">

<soapenv:Header/>

<soapenv:Body>

<max:SyncX3SEDIINDISP>

<max:X3SEDIINDISPSet>

<max:X3SEDIINDISP>

<max:PLTXT>Cagliari Sud - Rumianca</max:PLTXT>

<max:STRNO>L21006A</max:STRNO>

<max:STTXT>CRT</max:STTXT>

<max:TIPOELEMENTO>LIN</max:TIPOELEMENTO>

<max:TPLNR>?0100000000000000081</max:TPLNR>

</max:X3SEDIINDISP>

</max:X3SEDIINDISPSet>

</max:SyncX3SEDIINDISP>

</soapenv:Body>

</soapenv:Envelope>

Quando una chiamata va a buon fine la risposta ha 200 come http status code e ha una struttura di questo tipo:

<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">

<soap:Body>

<SyncX3SEDIINDISPResponse creationDateTime="2023-07-13T08:48:53+00:00" transLanguage="EN" baseLanguage="EN" messageID="328951689230933778990" xmlns="http://www.ibm.com/maximo" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"/>

</soap:Body>

</soap:Envelope>

Quando una chiamata fallisce la risposta ha 500 come http status code e ha una struttura di questo tipo:

<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">

<soap:Body>

<soap:Fault>

<faultcode>soap:Server</faultcode>

<faultstring>BMXAA4049E - The value specified exceeds the maximum field length that is allowed for this attribute: Value = CRTABCD, Object Name=X3SEDIINDISP, Attribute Name=STTXT, Maximum Length=5. Specify a value that is less than the maximum length of 5.</faultstring>

</soap:Fault>

</soap:Body>

</soap:Envelope>

### Sedi Tecniche di 1° livello

Questo flusso servirà a creare e aggiornare gli ASSET che corrispondono alle Sedi Tecniche SAP di 1° livello (Linee e Stazioni). Si occuperà anche di popolare la tabella SERVICEADDRESS che contiene le informazioni relative all’indirizzo della ST e le strutture a supporto del Connettore, nello specifico le tabelle X3SAPASSET che contiene l’anagrafica delle Sedi Tecniche SAP e X3SAPTOMAXOBJECT che contiene i link tra le ST SAP e gli oggetti Maximo.

Di seguito viene indicato l’elenco dei campi che fanno parte del servizio e il relativo mapping con i corrispondenti attributi SAP ed EAM.

| CAMPO SERVIZIO | DESCRIZIONE CAMPO | ATTRIBUTO EAM | ATTRIBUTO SAP |
| --- | --- | --- | --- |
| STRNO | Codice Sede Tecnica | ASSET.X3SAPID, X3SAPASSET. ROOTSTRNO, X3SAPASSET. STRNO | STRNO |
| TPLNR | Sede Tecnica | X3SAPASSET.SAPUID | TPLNR |
| STTXU | Stato Utente | ASSET.STATUS | STTXU |
| INBDT | In funzione dal | ASSET.INSTALLDATE | INBDT |
| SWERK | Divisione di Ubicazione | ASSET.X3SWERK | SWERK |
| MSGRP | Impianto | ASSET.X3MSGRP | MSGRP |
| EQFNR | Campo sort | ASSET.X3EQFNR | EQFNR |
| BUKRS | Società | ASSET.X3BUKRS | BUKRS |
| KOSTL | Centro di Costo | ASSET.X3KOSTL | KOSTL |
| NAME1 | NOME | SERVICEADDRESS.DESCRIPTION | NAME1 |
| NAME2 | NOME2 | SERVICEADDRESS. ADDRESSLINE2 | NAME2 |
| CITY | CITTA | SERVICEADDRESS.CITY | CITY |
| COUNTRY | PAESE | SERVICEADDRESS.COUNTRY | COUNTRY |
| REGION | PROVINCIA | SERVICEADDRESS.REGIONDISTRICT | REGION |
| STREET | VIA | SERVICEADDRESS.STREETADDRESS | STREET |
| POSTCODE1 | CAP | SERVICEADDRESS.POSTALCODE | POSTCODE1 |
| INGRP | Gruppo Responsabile Pianificazione della Manutenzione | ASSET.X3INGRP | INGRP |
| PLTXT | Definizione Sede Tecnica | ASSET.DESCRIPTION | PLTXT |
| GEWRK | Centro di Lavoro Responsabile | ASSET.X3GEWRK | GEWRK |
| ZZSOCP | Società Proprietaria | ASSET.X3ZZSOCP | ZZSOCP |
| FLTYP | Categoria Sede Tecnica | Usato per determinare la classificazione dell’Asset | FLTYP |

Il servizio lavora in modalità Sync, ossia può essere usato sia per creare nuovi record che per aggiornarli: la chiave che identifica un record e che viene usata per determinare se l’azione deve essere Add o Update è costituita dal campo TPLNR.

Solo alcune informazioni si possono aggiornare in caso di Update:

Società Proprietaria (ZZSOCP)

Impianto (MSGRP)

Centro di Costo (KOSTL)

Definizione Sede Tecnica (PLTXT)

Stato Utente (STTXU)

Non è prevista un’attività di rinomina dell’STRNO lato SAP.

Il WSDL è recuperabile all’indirizzo

https://masdev-mea.manage.dev.apps.dev.azopencs.servizi.prv/meaweb/wsdl/X3TERNA_X3SRVPRIMILVL.wsdl

e per comodità viene allegato in questo documento

Esempio di chiamata al servizio

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:max="http://www.ibm.com/maximo">

<soapenv:Header/>

<soapenv:Body>

<max:SyncX3SRVPRIMILVL>

<max:X3SRVPRIMILVLSet>

<max:X3SRVPRIMILVL>

<max:BUKRS>TR26</max:BUKRS>

<max:CITY>PIETRACAMELA</max:CITY>

<max:COUNTRY>IT</max:COUNTRY>

<max:EQFNR>BOH</max:EQFNR>

<max:GEWRK>EVITT3</max:GEWRK>

<max:INBDT>2023-07-10T10:31:09.465Z</max:INBDT>

<max:INGRP>l03</max:INGRP>

<max:KOSTL>AXL021388</max:KOSTL>

<max:MSGRP>2110388</max:MSGRP>

<max:NAME1>TERNA SPA - AOT ROMA

STAZIONE ELETTRICA S. GIACOMO 380 KV</max:NAME1>

<max:NAME2>Fabbricato Sede GOS</max:NAME2>

<max:PLTXT>S.GIACOMO 380 KV</max:PLTXT>

<max:POSTCODE1>64047</max:POSTCODE1>

<max:REGION>TE</max:REGION>

<max:STREET>LOCALITA COLLEPIANO</max:STREET>

<max:STRNO>23511D1</max:STRNO>

<max:STTXU>DATT</max:STTXU>

<max:SWERK>EVL</max:SWERK>

<max:TPLNR>?0100000000000597991</max:TPLNR>

<max:ZZSOCP>TR26</max:ZZSOCP>

<max:FLTYP>L</max:FLTYP>

</max:X3SRVPRIMILVL>

</max:X3SRVPRIMILVLSet>

</max:SyncX3SRVPRIMILVL>

</soapenv:Body>

</soapenv:Envelope>

Quando una chiamata va a buon fine la risposta ha 200 come http status code e ha una struttura di questo tipo:

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">

<soapenv:Body>

<SyncX3SRVPRIMILVLResponse creationDateTime="2023-07-20T16:29:28+02:00" transLanguage="EN" baseLanguage="EN" messageID="8173016898633694172560" xmlns="http://www.ibm.com/maximo" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

<X3SRVPRIMILVLSet>

<X3SRVPRIMILVL/>

</X3SRVPRIMILVLSet>

</SyncX3SRVPRIMILVLResponse>

</soapenv:Body>

</soapenv:Envelope>

Quando una chiamata fallisce la risposta ha 500 come http status code e ha una struttura di questo tipo:

<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">

<soap:Body>

<soap:Fault>

<faultcode>soap:Server</faultcode>

<faultstring>BMXAA4049E - The value specified exceeds the maximum field length that is allowed for this attribute: Value = TERNA SPA - AOT ROMA

STAZIONE ELETTRICA S. GIACOMO 380 KV, Object Name=X3SRVPRIMILVL, Attribute Name=NAME1, Maximum Length=40. Specify a value that is less than the maximum length of 40.</faultstring>

</soap:Fault>

</soap:Body>

</soap:Envelope>

# Integrazione documento per OP 252, OP 253 e OP 305

Modifica agli script X3CONSOSTINIZIO, X3CONTERMINALEINIZIO e X3CONTERMINALEFINE che calcolano le caratteristiche SAP PMT_SOSTEGNO_INIZIO, PMT_TERMINALE_INIZIO, PMT_TERMINALE_FINE.

## OP 252, OP 253

Gestione dei nuovi attributi EAM “LIN_SOSTEGNO_INIZIO” e “LIN_ESTREMO_INIZIO” mediante l’aggiunta di un if statement per verificare la presenza dell’attributo all’interno della tabella ASSETSPEC per il record legato alla campata o alla tratta cavo a cui stiamo facendo riferimento. Il set ASSETSPEC viene recuperato mediante la relazione esistente tra l’mbo CAMPATA/TRATTA CAVO e la suddetta tabella.

Una volta verificata la presenza dell’attributo e ottenuto l’mbo, viene verificato che questo non sia vuoto (assetSpec != None).

Dopo questa ulteriore verifica viene recuperato il valore presente all’interno del campo “ALNVALUE” dell’mbo e successivamente scritto all’interno della tabella X3SAPASSETDATA contenente, per l’mbo campata o tratta cavo in questione, le sue caratteristiche.

Codice aggiunto allo script X3CONSOSTINIZIO:

linSostegnoInizio = None assetSpecSet = campataMbo.getMboSet("ASSETSPEC") assetSpecSet.setWhere("assetattrid = 'LIN_SOSTEGNO_INIZIO'") assetSpecSet.reset() assetSpec = assetSpecSet.getMbo(0) if assetSpec != None: linSostegnoInizio = assetSpec.getString("ALNVALUE") if linSostegnoInizio != "": # l'attributo LIN_SOSTEGNO_INIZIO è valorizzato mappedRawValue = linSostegnoInizio

Codice aggiunto allo script X3CONTERMINALEINIZIO:

linTermInizio = None

assetSpecSet = trattaCavoMbo.getMboSet("ASSETSPEC")

assetSpecSet.setWhere("ASSETATTRID='LIN_ESTREMO_INIZIO'")

assetSpecSet.reset()

assetSpec = assetSpecSet.getMbo(0)

if assetSpec != None:

linTermInizio = assetSpec.getString("ALNVALUE")

if linTermInizio != "":

# L'attributo LIN_TERMINALE_INIZIO è valorizzato

mappedRawValue = linTermInizio

## OP 305

Gestione del nuovo requisito per il quale gli asset di tipo ESTREMO CAVO di inizio e fine tratta avranno un SAPID uniformato (-H), sostituendo di conseguenza il precedente codice –K.

Le modifiche apportate agli script X3CONTERMINALEINIZIO e X3CONTERMINALEFINE riguardano la regex utilizzata per recuperare l’mboSet della tratta cavo fittizia e la modifica alla query che recupera la relazione tra l’mbo tratta cavo e l’estremo cavo collegato ad esso.

La prima modifica, come detto, prevede la rimozione della lettera –K dalla regex per recuperare l’mboSet della tratta cavo sulla tabella ASSETLOCRELATION:

if sapAsset.getBoolean("dummy"):

strno = sapAsset.getString("strno")

childSet = sapAsset.getMboSet("$child$", "ASSETLOCRELATION", "regexp_like(x3sapid, '"+ strno + "-[RH]$')")

childSet.reset()

childMbo = childSet.getMbo(0)

La seconda prevede la rimozione della lettera –K quando si effettua la query per cercare gli estremi cavo in relazione con la tratta cavo in caso l’attributo LIN_ESTREMO_INIZIO non sia valorizzato:

if assetSpec != None:

linTermInizio = assetSpec.getString("ALNVALUE")

if linTermInizio != "":

# L'attributo LIN_ESTREMO_INIZIO è valorizzato

mappedRawValue = linTermInizio

else:

# L'attributo LIN_ESTREMO_INIZIO non è valorizzato

sqlWhere = "sourceassetnum = :sourceassetnum and siteid = :siteid and (x3sapid like '%-R' or x3sapid like '%-H') and x3sequence < :x3sequence"

# Indice tabelle

Tabella 1. Proprietà di configurazione connettore 14

Tabella 1. Eventi su cui il connettore è in ascolto 15

# Indice figure

Figura 1. Diagramma di flusso scenario di sincronizzazione anagrafica linee/stazioni 5

Figura 2. Progetti Eclipse 7

Figura 3. Versione JDK 8

Figura 4. Librerie progetto connettore-bo 9

Figura 5. Struttura package progetto connettore-bo 10

Figura 6. Esempio codice Java con documentazione in formato JavaDoc 11

Figura 7. Esempio JavaDoc per una classe 12

Figura 8. Configurazione servizio di integrazione connettore 13

Figura 9. Dati prestazioni esecuzione del connettore 14

Figura 10. Ammazzettamento aggregato CONDUTTORI 16

Figura 11. Possibili spostamenti equipment stazione 22

Figura 12. CRON task per l'avvio degli step 2, 3 e 4 24

Figura 13. Pianificazione step 2, 3 e 4 24

Figura 14. Applicazione monitoraggio errori 25

# Allegati

## Documentazione integrazione EAM-SAP
