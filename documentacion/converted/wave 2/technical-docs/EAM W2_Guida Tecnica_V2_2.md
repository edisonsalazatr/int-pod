# EAM W2_Guida Tecnica_V2_2

|  |  |  |
| --- | --- | --- |
| Redatto | Verificato | Approvato |
| Nominativo (acronimo struttura) Nominativo (acronimo struttura) Nominativo (acronimo struttura) Nominativo (acronimo struttura) Nominativo (acronimo struttura) | Nominativo (acronimo struttura) Nominativo (acronimo struttura) Nominativo (acronimo struttura) Nominativo (acronimo struttura) Nominativo (acronimo struttura) | Nominativo (acronimo struttura) |

Storia delle revisioni

| Rev. …. del gg/mm/aaaa | Sintesi delle principali modifiche apportate |
| --- | --- |
| Rev. 05 del gg/mm/aaaa | Sintesi delle principali modifiche apportate |
| Rev. 04 del gg/mm/aaaa | Sintesi delle principali modifiche apportate |
| Rev. 03 del gg/mm/aaaa | Sintesi delle principali modifiche apportate |
| Rev. 02 del gg/mm/aaaa | Sintesi delle principali modifiche apportate |
| Rev. 01 del gg/mm/aaaa | Sintesi delle principali modifiche apportate |
| Rev. 00 del gg/mm/aaaa | Prima emissione |

Sistemi di Gestione e/o Modelli Organizzativi di riferimento

Sistemi di Gestione certificati/accreditati Modelli Organizzativi

|  | SGQ (Qualità) |  |  | BCM (Business Continuity Model) |
| --- | --- | --- | --- | --- |
|  | SGA (Ambiente) |  |  | TCM (Tax Compliance Model) |
|  | SGSL (Sicurezza e Salute sui luoghi di Lavoro) |  |  | PRV (Modello Privacy) |
|  | SGPIR (Prev. Incidenti Rilevanti – Direttiva Seveso) |  |  | M262 (Modello 262) |
|  | SGSI (Sicurezza delle Informazioni) |  |  | M231 (Modello 231) |
|  | SGE (Energia consumata per usi propri) |  |  | MIMP (Modello Imparzialità) |
|  | SGQ LST (Laboratorio LST) |  |  | SCIIS (Sist. Controllo Informativa di Sostenibilità) |
|  | SGQ TAR (Centro di Taratura) |  |  |  |
|  | SGAC (Anticorruzione) |  |  |  |
|  | SGAM (Gestione Asset) |  |  |  |
|  | SGPCI (Prev. e Controllo Infezioni – Biosafety) |  |  |  |
|  | SGC (Compliance) |  |  |  |
|  | SGPG (Parità di Genere) |  |  |  |
|  | SGPAC (Processi Amministrativi e Contabili) |  |  |  |

(Per approfondimenti sui Sistemi di Gestione certificati/accreditati, clicca qui)

Indice

# 1. PREMESSA

## 1.1 Scopo del documento

Il presente documento ha lo scopo di descrivere il funzionamento del sistema EAM.

All’interno del documento vengono descritti gli strumenti disponibili per la gestione dei processi di Asset Management supportati dalla soluzione, e l’operatività di dettaglio per le singole azioni.

L’introduzione della piattaforma EAM si colloca nel percorso di evoluzione del processo di Asset Management in ottica ​ «Asset Lifecycle» e di digitalizzazione, con lo scopo di rendere disponibili modalità di lavoro e logiche di elaborazione in linea ​con le esigenze di business. ​

Le principali linee guida che hanno portato all’introduzione di EAM sono:

Valore del dato: modello dati strutturato basato sulle entità di business centrali ​per il processo (Asset, Cataloghi, OdL), con l’intento di garantire ampiezza ​del corredo informativo a favore di una gestione accurata delle attività sugli asset e della possibilità di effettuare analisi articolate;

Funzionalità “su misura”: business logic e funzionalità operative definite sulla base delle specifiche di Terna, prevedendo automazione dei controlli di coerenza sulle informazioni, workflow guidati, e copertura capillare delle casistiche più specifiche;

Processo integrato: razionalizzazione del processo che attraversa diverse applicazioni (EAM, WFM, GIN, SAP, …), ed integrazione delle dimensioni tecnica ed economica su EAM per un più efficace controllo dell’avanzamento delle attività.

## 1.2 Campo di applicazione

Le due principali aree coperte da EAM sono:

L’Anagrafica degli Asset, integrata con SAP, che rappresenta il master delle informazioni tecniche sugli asset di linea e stazione. Il progetto di realizzazione dell’Anagrafica Asset è noto comunemente come “EAM Wave 1”.

I processi di gestione degli Asset, sia di competenza delle unità centrali di Asset Management, sia delle unità operative territoriali (DT, UI, RL). L’ambito di processo di EAM si colloca nello spazio precedentemente coperto dalle applicazioni MBI-Linee, MBI-Stazioni, Smart MBI e Make. Il progetto di realizzazione è noto comunemente come “EAM Wave 2”.

La mappa sottostante rappresenta una scomposizione ad alto livello del processo complessivo, con l’obiettivo di mettere a fuoco le principali fasi e gli strumenti relativi.

Gestione cataloghi. I cataloghi costituiscono gli elementi cardine del modello EAM, riguardano diversi tipi di entità (ad es. Policy, Attività, Difformità, …) e racchiudono le business logic e le relazioni tra gli oggetti che guidano le attività operative. Ad es. definiscono quali policy operano su quali asset, o quali difformità sono risolvibili da una certa attività.

Calcolo indicatori. Gli indicatori di condizione tecnica degli asset definiti in apposito catalogo vengono calcolati e costantemente aggiornati in funzione di variabili o eventi di processo che ne determinano il valore.

DSS. Il Decision Support System rappresenta l’”intelligenza” di EAM che monitora gli eventi e le attività che riguardano gli asset, ed attua le policy intervenendo nella creazione, modifica o cancellazione di Ordini di Lavoro

Creazione OdL manuali. Gli Ordini di Lavoro, oltre che generati dal DSS, posson essere creati manualmente da un utente EAM e popolati per poter rispondere a specifiche esigenze operative. Più OdL possono essere aggregati tra di loro per consentire la gestione sinergica di più attività della stessa tipologia.

Gestione della capacità per UdB. In EAM è presente un meccanismo di gestione della capacità per famiglia professionale (Operaio, Impiegato, Quadro) a partire dai dati acquisiti da Tagetik, che ne consente il monitoraggio e mette a disposizione la possibilità di effettuare scambi tra UdB.

Generazione e Gestione OdL Serbatoio. A livello di Piano Manutenzione Impianti, gli Ordini Serbatoio sono una particolare tipologia di OdL che servono a pianificare sulle COIN corrette i volumi di effort orario e fisico non ancora riferibili ad asset specifici. Gli Ordini Serbatoio rimangono attivi nel corso dell’esecuzione del piano e vengono aggiornati secondo determinate logiche di erosione della capacity.

Pianificatore “Euristico”. Il Motore di pianificazione “euristico” (o MPE) rappresenta lo strumento a supporto della pianificazione di medio – lungo periodo, per collocare volumi importanti di OdL nel corso dei mesi e degli anni successivi nel rispetto delle date vincolanti di ogni OdL e delle priorità delle varie tipologie di attività, mettendo quindi ogni UdB nelle condizioni di mantenere una pianificazione costantemente aggiornata.

Pianificazione Tecnica OdL. Ogni Ordine di Lavoro conterrà l’intero set di informazioni relativamente alle date di esecuzione, alle attività da svolgere con relative durate e alle risorse da impiegare.

Gestione Indisponibilità. Per le tipologie di attività che lo richiedono, EAM gestisce in modalità integrata con GIN il processo di definizione delle date dei fuori servizio.

Pianificazione economica. Ogni OdL viene caratterizzato rispetto alle dimensioni del modello COIN di Terna, e include le destinazioni contabili grazie all’integrazione con SAP. La pianificazione economica viene denominata “light” in quanto inizialmente completa in EAM solo per la componente OPEX.

Esecuzione attività UI / non UI. EAM dispone delle funzionalità a supporto dell’esecuzione e del monitoraggio delle attività sugli asset, rendendo disponibile anche un’applicazione mobile completamente integrata a supporto degli interventi del personale operativo in campo. I processi sono distinti in termini di step operativi e responsabilità in funzione del modello di lavoro “UI”, che prevede tra l’altro l’integrazione con GeoCall, e “non UI” per le altre UdB come ad esempio Realizzazione Locale.

EAM si colloca al centro della mappa applicativa che supporta i processi di Asset Management, e rappresenta il punto di accesso al dato per monitorare le attività e il loro avanzamento; racchiude inoltre sia la dimensione tecnica, che gestisce in sinergia con GIN e WFM, sia quella economica, per la quale è integrato con SAP.

Inoltre, con l’introduzione di EAM anche le attività riconducibili ai Programmi vengono gestite in maniera strutturata e omogenea rispetto alle altre attività sugli asset (in precedenza venivano utilizzati file Excel).

## 1.3 Glossario

| TERMINE “MOSAICO” | TERMINE EAM | DEFINIZIONE |
| --- | --- | --- |
| Asset | Asset | Elemento fisico o raggruppamento di elementi fisici (impianto, componente, sistema) oggetto di monitoraggio e gestione all'interno di EAM. |
| Catalogo (Make e MBI-Make) | Catalogo (diff.,inter., attiv., etc..) | Sezione della piattaforma in cui vengono definite e configurate entità di riferimento (es. difformità, interferenze, microcontrolli, modelli-target) valide a livello generale. |
| Anomalia | Difformità | Anomalia o scostamento rispetto a condizioni di funzionamento attese o parametri standard. |
| Anomalie interferenti con linea (es. taglio piante) | Interferenza | Difformità derivante da oggetti interferenti con gli asset di rete |
| n.a. | Microcontrollo | Controllo elementare e specifico che consente di monitorare singole condizioni o comportamenti di un asset. |
| Modello-Target | Modello-Target | Configurazione predefinita che definisce soglie, regole o set di microcontrolli da applicare a una determinata classe di asset. |
| Misura | Misura | Valore rilevato su un asset (manuale o automatizzato) che rappresenta un parametro tecnico utile al monitoraggio. Le misure possono essere confrontate con soglie definite nei microcontrolli. |
| Variabile di Stato | Variabile di Stato | Indicatore logico o numerico che descrive lo stato di un asset o l’esito di un controllo. Può essere utilizzata come condizione di attivazione per controlli o regole. |
| HI (Health Index) | HI (Health Index) | Indice sintetico che esprime lo stato complessivo di salute di un asset a partire da informazioni tecniche e della storia manutentiva |
| n.a. | MXLoader | Funzionalità di IBM Maximo che consente l’aggiornamento massivo dei dati riferiti a più entità. |
| n.a. | CdC (Centro di Controllo) | Strumento per il monitoraggio dei dati in EAM, utile a rilevare la presenza di determinati record o casistiche particolari |
| Istanza Make | Ordine di Lavoro (OdL) | Attività da svolgere su di un determinato asset, entro certi limiti temporali e attraverso l’impiego di risorse interne o esterne. |
| Controllo/Provvedimento cat. MBI | Piano di Lavoro (PdL) | Elemento di catalogo che definisce un task che può essere svolto su determinati asset, in funzione delle caratteristiche degli stessi. |
| Serbatoio | OdL Serbatoio | Oggetto di pianificazione utilizzato come “contenitore” delle ore uomo disponibili ma non ancora allocate su OdL specifici |

## 1.4 Principali oggetti di business

La gestione dei processi di Asset Management si svolge attorno ad alcuni oggetti principali. Il modello nativo di prevede due entità fondamentali per la gestione delle attività: il Piano di Lavoro EAM e l’Ordine di Lavoro.

Piano di Lavoro EAM (PdL EAM):

Definisce l’attività che può essere svolta su di un asset

Contiene le informazioni per caratterizzare l’attività in termini di tempi, risorse e modalità di esecuzione (metodo)

Può contenere un ulteriore dettaglio operativo, denominato «job task», utilizzato ad es. per gestire i micro-controlli

Ordine di Lavoro (OdL):

Rappresenta l’elemento base a cui è applicato qualsiasi processo operativo, che può essere guidato da un workflow

Contiene informazioni sia di pianificazione tecnica (attività, risorse, date) sia economica (caratterizzazione di contabilità industriale, destinazione contabile)

È possibile aggregare diversi OdL sotto uno stesso OdL «aggregatore» per gestire contestualmente un gruppo di attività.

Difformità, Interferenze e Misure:

Sono entità che caratterizzano un asset o il suo stato

La difformità fa riferimento ad una difettosità intrinseca specifica legata al funzionamento dell’asset mentre l'interferenza fa riferimento a condizioni esterne che ostacolano, minacciano o alterano l’operatività e la sicurezza

Possono essere rilevate contestualmente all’esecuzione di un’attività (OdL) o associate direttamente all’asset.

Esistono legami logici tra le difformità e interferenze e le attività (PdL EAM) che le possono rilevare e risolvere

Policy:

Stabiliscono una relazione tra diversi tipi di eventi o condizioni per determinare le azioni da compiere sugli asset

Sono configurabili e consentono di gestire logiche a più livelli, utilizzando diversi tipi di operatori logici

Una volta attivate vengono applicate dal DSS per la generazione di ordini

# 2. CATALOGHI

## 2.1 Introduzione

I cataloghi contengono le informazioni anagrafiche di diversi oggetti di business utilizzati all’interno dei processi di EAM. In particolare:

Rendono disponibili i valori ammissibili per gli oggetti di business codificati, attraverso la gestione ordinata dei dati e abilitazione ad analisi strutturate e l’inserimento guidato per le diverse entità di business durante ogni step del processo;

Gestiscono il corredo informativo per ogni oggetto, attraverso la definizione delle informazioni caratterizzanti per ogni tipo di oggetto e del formato dei dati e la presenza di controlli di coerenza tra i dati

Stabiliscono le relazioni tra tipi di oggetti diversi, attraverso la determinazione delle combinazioni ammissibili tra famiglie di oggetti (ad es. difformità che possono essere applicate ad un asset) e la definizione di alcune logiche di business (ad es. difformità ed attività che la risolve)

La gestione dei cataloghi avviene attraverso apposite applicazioni dedicate, una per ciascun catalogo. Tutti gli utenti possono accedere alla visualizzazione dei cataloghi tramite l’interfaccia in modalità elenco; tuttavia, solo gli utenti centrali sono abilitati a eseguire operazioni di modifica, inserimento o eliminazione direttamente tramite il sistema EAM. In caso di eliminazione di un elemento, il sistema fornisce un messaggio di avviso non bloccante per segnalare eventuali effetti correlati, quali possibili incongruenze con altri cataloghi. Il sistema consente l’inserimento di un solo input per ciascuna Policy elementare. In caso di tentativo di aggiunta di più righe, l’utente viene bloccato e notificato dal sistema.

## 2.2 Catalogo delle difformità

Il catalogo di difformità raccoglie, per ciascun asset, le anomalie che possono essere rilevate dagli operatori durante le attività di monitoraggio. Ogni difformità è associata all’asset di riferimento e può essere collegata a qualsiasi livello dell’anagrafica.

Nell’ambito della gestione dei cataloghi è consentita all’utente la possibilità di generare, modificare o disattivare le difformità. Nello specifico, sarà l’utente centrale quello incaricato di impostare le difformità secondo le necessità operative.

Di seguito vengono descritte le procedure per l’inserimento e la valorizzazione dei campi all’interno del catalogo delle difformità:

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| Applicabile a | Definisce se la difformità è riferita a una classificazione o a un singolo elemento |  | Obbligatorio |  |
| ID Difformità | Codice univoco identificativo della difformità, generato automaticamente |  | Obbligatorio |  |
| Assieme | Raggruppano la difformità per componenti | Pick-list | Facoltativo | Facoltativo |
| Sottoassieme | Raggruppano la difformità per componenti | Pick-list | Facoltativo | Facoltativo |
| Descrizione | Descrizione libera della difformità | Testo libero | Obbligatorio | Obbligatorio |
| Elemento asset | Elemento asset su cui può essere rilevata la difformità | Pick-list | Obbligatorio | Obbligatorio |
| Classificazione asset | Classificazioni associate all’elemento selezionato |  | Obbligatorio | Obbligatorio |
| Gerarchia asset associato | Gerarchia della classificazione |  | Obbligatorio | Obbligatorio |
| Stato | Stato corrente della difformità (Bozza, Attivo, Non attivo) | Pick-list | Obbligatorio | Obbligatorio |
| Gravità difformità | Gravità associata alla difformità, usata anche per confronto con i target |  | Obbligatorio se Generazione automatica = SÌ | Obbligatorio se Generazione automatica = SÌ |
| Massimo tempo di validazione | Quantità di giorni entro cui la difformità viene validata automaticamente | Testo libero (numerico) | Obbligatorio | Obbligatorio |
| Modificato da | Matricola dell’utente che ha effettuato l’ultima modifica | Popolato automaticamente | Obbligatorio | Obbligatorio |
| Data ultima modifica | Data in cui è stata effettuata l’ultima modifica | Popolato automaticamente | Obbligatorio | Obbligatorio |
| Tipologia difformità | Tipo di difformità: Anomalia / Guasto / Anomalia+Guasto | Pick-list | Obbligatorio | Obbligatorio |
| Misure difformità | Misure associabili alla difformità, da rilevare contestualmente | Selezione da lista filtrata | Facoltativo |  |
| Generazione automatica | Indica se la difformità è generata automaticamente da misure/parametri | Flag | Attivazione facoltativa |  |
| Parametri per generazione | Parametri usati nella formula di generazione automatica della difformità | Inserimento tabellare filtrato | Obbligatorio se Generazione automatica = SÌ |  |
| Formula | Formula di calcolo della difformità da eseguire automaticamente | Testo libero con regole GUI | Obbligatorio se Generazione automatica = SÌ |  |
| Caratteristiche asset | Attributi specifici che l’asset deve avere affinché la difformità sia rilevabile | Inserimento manuale | Facoltativo |  |
| Fase | Utilizzato per le difformità la cui rilevazione necessita l’inserimento della fase | Flag | Attivazione Facoltativa |  |
| Attività che risolvono difformità | Attività collegate alla risoluzione della difformità | Non modificabile | Facoltativo |  |
| Obbligatorietà foto | Obbligo di allegare foto alla registrazione della difformità | Flag | Attivazione facoltativa |  |

### 2.2.1 Creazione di una difformità

Per aggiungere una nuova difformità nel catalogo dedicato all’interno del sistema della piattaforma è necessario eseguire gli step descritti nei paragrafi successivi.

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema.

Senza generazione automatica

Le difformità senza generazione automatica sono definite da anomalie riscontrate a seguito dell’esecuzione di attività operative su campo e registrate automaticamente dall’operatore tramite l’ordine di lavoro. Per aggiungere al catalogo una difformità senza generazione automatica, l’utente di EAM deve seguire la procedura di seguito descritta.

Accedere all’applicazione dedicata al catalogo difformità: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo difformità”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo difformità” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo difformità"

Cliccare su “Nuova Difformità”: selezionare l’opzione “Nuova difformità” evidenziato a sinistra nell’immagine, o in alternativa, cliccare sul pulsante “+” in alto a destra

Figura - Vista iniziale del catalogo delle difformità, con evidenza alle azioni di aggiunta di nuova difformità

Compilare i campi obbligatori e facoltativi in maschera: una volta effettuata l’azione descritta nello step precedente, è necessario definire le caratteristiche della difformità popolando i campi di seguito evidenziati;

Figura - Schermata di dettaglio della difformità a catalogo

Impostare lo stato della difformità appena generata: successivamente all’inserimento delle informazioni relative alla difformità, è necessario impostare lo stato della stessa. Di default, la difformità si trova in stato “Bozza” e, selezionando la lente di ricerca presente all’interno del campo “Stato”, l’utente può modificarlo in “Attivo” oppure “Non attivo”.

Figura - Dialog di modifica di stato della difformità

Salvare la difformità: selezionando l’icona in alto a destra il sistema genera automaticamente l’ID Difformità, secondo la logica di concatenazione specifica “Diff–L/S–livelli anagrafici–progressivo” e la “Classificazione asset, in base a quello inserito nel campo “Elemento asset”. Inoltre, viene popolato il campo “Gerarchia asset associato” in base all’asset precedentemente selezionato.

Figura - Schermata di dettaglio della difformità a catalogo, con evidenza all’icona di salvataggio

Con generazione automatica

La generazione automatica delle difformità si basa su regole preconfigurate nel sistema e viene attivata automaticamente in presenza di condizioni anomale definite a partire delle misure realizzate. Per aggiungere al catalogo delle difformità una difformità con generazione, l’utente di EAM deve seguire la procedura di seguito descritta.

Accedere all’applicazione dedicata al catalogo difformità: come dimostrato nellafigura sotto, posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo difformità”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo difformità” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo difformità"

Cliccare su “Nuova Difformità”: selezionare l’opzione “Nuova difformità” evidenziato a sinistra nella Figura, o in alternativa, cliccare sul pulsante “+” in alto a destra. Successivamente, l’utente deve assicurarsi che il flag “Generazione automatica” sia impostato su “Sì” per le difformità di interesse. Quando impostato su “Si”, il flag evidenziato nella figura sotto garantisce che la difformità sarà generata quando raggiunte le condizioni stabilite.

Figura - Schermata di dettaglio della difformità a catalogo, con evidenza all’icona di salvataggio, con evidenza all'azione di generazione di nuova difformità e flag "Generazione di misura"

Compilare i campi in maschera: una volta effettuata l’azione descritta nello step precedente, è necessario definire le caratteristiche della difformità popolando i campi richiesti, come rappresentato in figura. Nel caso in cui il Flag di “generazione da misure” è attivo, devono essere popolate le tabelle rappresentate nelle figure sotto:

Gravità difformità;

Parametri per la generazione di difformità;

Formule

Figura - Schermata di dettaglio della difformità a catalogo, con evidenza alle tabelle “Misure difformità”, “Formule” e “Parametri per la generazione di difformità”

Figura - Schermata di dettaglio della difformità a catalogo, con evidenza alle tabelle “Gravità difformità”, “attività

che risolvono la difformità” e “caratteristiche asset”

Impostare lo stato della difformità appena generata: come dimostrato nella figura, successivamente all’inserimento delle informazioni relative alla difformità, è necessario impostare lo stato della stessa. Di default, la difformità si trova in stato “Bozza” e, selezionando la lente di ricerca presente all’interno del campo “Stato”, l’utente può modificarlo in “Attivo” oppure “Non attivo”.

Salvare la difformità: al salvataggio, il sistema genera automaticamente il ID Difformità, secondo la logica di concatenazione specifica “Diff–L/S–livelli anagrafici–progressivo”. Inoltre, viene popolato il campo “Gerarchia asset associato” in base all’asset precedentemente selezionato.

### 2.2.2 Modifica di una difformità

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per procedere alla modifica di una difformità all’interno del catalogo delle difformità è necessario seguire i seguenti step:

Accedere all’applicazione dedicata al catalogo difformità: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo difformità”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo difformità” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo difformità"

Selezionare la difformità da modificare: dalla vista elenco l’utente può individuare la difformità di interesse e selezionarla cliccando sul record presente nella colonna ID Difformità. Selezionando l’icona di filtro evidenziata nell’immagine, l’utente può definire i filtri di ricerca sulle colonne della vista.

Figura - Vista iniziale del catalogo delle difformità, con evidenza al filtro di selezione

Apportare le modifiche: quando in stato “Attivo”, è possibile modificare qualunque informazione relativa alla difformità selezionando il campo desiderato (ad eccezione del campo “Elemento asset”, “Classificazione”, “ID Difformità” che rimane in sola lettura).

Salvare le modifiche tramite l’apposito comando: selezionando l’icona evidenziata nella figura sottostante, l’utente conferma le modifiche apportate.

Figura – Schermata di dettaglio della difformità a catalogo, con evidenza all’icona di salvataggio

### 2.2.3 Disattivazione di una difformità

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per procedere alla modifica di una interferenza all’interno della piattaforma è necessario eseguire i seguenti step:

Accedere all’applicazione dedicata al catalogo difformità: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo difformità”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo difformità” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo difformità"

Selezionare la difformità da disattivare: dalla vista elenco l’utente può individuare la difformità di interesse e selezionarla cliccando sul record presente nella colonna ID Difformità. Selezionando l’icona di filtro evidenziata nella figura, l’utente può definire i filtri di ricerca sulle colonne della vista.

Figura - Vista iniziale del catalogo delle difformità, con evidenza al filtro di selezione

Modificare lo stato della difformità: selezionando la lente di ingrandimento del campo “Stato”, l’utente può scegliere da una lista valore lo stato “Non attivo”.

Figura - Dialog di cambio stato della difformità, con la possibilità di modificare solo in "Non attivo"

Salvare la modifica: como dimostrato nella figura, selezionando l’icona evidenziata nella figura, l’utente conferma la disattivazione della difformità. In questo stato, la difformità non sarà più rilevabile da alcuna attività. e non sarà possibile eseguire alcuna operazione o modifica sulla stessa, a meno di riportarla nuovamente in stato attivo.

Nota: Si ricorda che in EAM non è possibile eliminare delle difformità dal catalogo di appartenenza, ma soltanto disattivarlo.

## 2.3 Catalogo delle interferenze

Il catalogo delle interferenze raccoglie, per ciascun asset, le interferenze che possono essere rilevate dagli operatori durante le attività di monitoraggio. Ogni interferenza è associata all’asset di riferimento e può essere collegata a qualsiasi livello dell’anagrafica.

Di seguito vengono descritte le procedure per l’inserimento e la valorizzazione dei campi all’interno del catalogo delle interferenze:

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| Applicabile a | Definisce se l’interferenza è riferita a una classificazione o a un singolo elemento |  | Obbligatorio |  |
| Elemento asset | Elemento asset su cui può essere rilevata l’interferenza | Pick-list | Obbligatorio |  |
| Classificazione asset | Classificazioni associate all’elemento asset | Popolato automaticamente in base all’elemento asset | Facoltativo |  |
| Gerarchia asset associato | Gerarchia della classificazione | Popolato automaticamente in base alla classificazione asset | Obbligatorio |  |
| Assieme | Raggruppano le interferenze per componenti | Pick-list | Facoltativo |  |
| Sottoassieme | Raggruppano le interferenze per componenti | Pick-list | Facoltativo |  |
| Stato | Stato corrente dell’interferenza | Pick-list | Obbligatorio |  |
| ID interferenza | Identificativo unico dell’interferenza | Popolato automaticamente | Obbligatorio |  |
| Descrizione | Descrizione testuale dell’interferenza | Testo libero | Obbligatorio |  |
| Gravità interferenza | Valore di gravità per confronto policy | Pick-list / Valori alfanumerici | Facoltativo |  |
| Massimo tempo di validazione | Giorni entro cui l’interferenza viene validata automaticamente | Testo libero | Obbligatorio |  |
| Misure interferenza | Consente di associare misure selezionate dal catalogo delle misure-variabili di stato. | Pick-list | Facoltativo |  |
| Caratteristiche asset | Attributi specifici dell’asset affinché l’interferenza sia rilevabile | Inserimento manuale | Facoltativo |  |
| Fase? | Per le interferenze la cui rilevazione necessita l’inserimento della fase | Flag | Valorizzazione facoltativa |  |
| Obbligatorietà allegato | Indica l’obbligatorietà di allegare una foto all’interferenza quando registrata | Flag | Valorizzazione facoltativa |  |
| Modificato da | Matricola dell’utente che ha effettuato l’ultima modifica | Popolato automaticamente | Obbligatorio |  |
| Data ultima modifica | Data dell’ultima modifica dell’interferenza | Popolato automaticamente | Obbligatorio |  |
| Attività che risolvono l’interferenza | Attività risolutive associate | Non modificabile | Facoltativo |  |
| Target / Gravità | Definisce il livello di severità dell’interferenza (es. lieve, media, grave), usato anche come parametro target per le policy. | Inserimento manuale | Facoltativo |  |

Tabella - Campi facoltativi e obbligatori del catalogo delle interferenze

### 2.3.1 Creazione interferenze

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per aggiungere una nuova interferenza nel catalogo dedicato all’interno del catalogo delle interferenze è necessario eseguire i seguenti step:

Accedere all’applicazione Catalogo Interferenze: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo interferenza”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo interferenza” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo interferenze"

Cliccare su “Nuova Interferenza”: selezionare l’opzione “Nuova interferenza” evidenziato a sinistra nella figura sotto, o in alternativa, cliccare sul pulsante “+” in alto a destra;

Figura - Vista iniziale del catalogo delle interferenze, con evidenza alle azioni di aggiunta di nuova interferenza

Compilare i campi obbligatori e facoltativi in maschera: una volta effettuata l’azione descritta nello step precedente, è necessario definire le caratteristiche delle interferenze popolando i campi di seguito rappresentati;

Figura - Schermata di dettaglio dell'interferenza a catalogo

Impostare lo stato dell’interferenza appena generata: successivamente all’inserimento delle informazioni relative all’interferenza è necessario impostare lo stato della stessa. Di default, l’interferenza si trova in stato “Bozza” e selezionando la lente di ricerca presente all’interno del campo “Stato”, l’utente può modificarlo in “Attivo” oppure “Non attivo”.

Figura - Dialog di modifica stato dell'interferenza

Salvare l’interferenza: Como dimostrato nella figura, al salvataggio, il sistema genera automaticamente l’ID Interferenza, secondo la logica di concatenazione specifica “Int–L/S–livelli anagrafici–progressivo”. Inoltre, viene popolato il campo “Gerarchia asset associato” in base all’asset precedentemente selezionato.

### 2.3.2 Modifica interferenze

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per procedere alla modifica di una interferenza all’interno della piattaforma è necessario seguire i seguenti step:

Accedere all’applicazione dedicata al catalogo delle interferenze: come dimostrato nella Figura, posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo interferenze”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo interferenze” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo interferenze"

Selezionare l’interferenza da modificare: dalla vista elenco l’utente può individuare la interferenza di interesse e selezionarla cliccando sul record presente nella colonna ID Interferenza. Selezionando l’icona di filtro evidenziata nell’immagine, l’utente può definire i filtri di ricerca sulle colonne della vista.

Figura - Vista iniziale del catalogo delle interferenze, con evidenza al filtro di selezione

Apportare le modifiche: quando in stato “Attivo”, è possibile modificare qualunque informazione relativa all’interferenza selezionando il campo desiderato, ad eccezione dei campi “Elemento Asset”, “Classificazione Asset” e “ID Interferenza”.

Salvare le modifiche: selezionando l’icona evidenziata nella Figura sottostante, l’utente conferma le modifiche apportate.

Figura - Schermata di dettaglio dell’interferenza a catalogo, con evidenza all’icona di salvataggio

### 2.3.3 Disattivazione interferenze

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per procedere alla disattivazione di una interferenza all’interno del catalogo delle interferenze, l’utente deve eseguire i seguenti passaggi:

Accedere all’applicazione dedicata alla gestione delle interferenze: come dimostrato in figura, posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo interferenze”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo interferenze” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo interferenze"

Selezionare l’interferenza da disattivare: come dimostrato in figura, dalla vista elenco l’utente può individuare l’interferenza di interesse e selezionarla cliccando sul record presente nella colonna “ID Interferenza”. Selezionando l’icona di filtro evidenziata nella Figura, l’utente può definire i filtri di ricerca sulle colonne della vista.

Figura - Vista iniziale del catalogo delle interferenze, con evidenza al filtro di selezione

Modificare lo stato dell’interferenza: selezionando la lente del campo “Stato”, l’utente può scegliere da una lista valore lo stato “Non attivo”.

Figura - Dialog di modifica stato dell'interferenza, con evidenza allo stato "Non attivo"

Salvare la modifica: selezionando l’icona, l’utente conferma la disattivazione dell’interferenza. In questo stato, l’interferenza non sarà più rilevabile da alcuna attività e non sarà possibile eseguire alcuna operazione o modifica sulla stessa a meno di riportarla nuovamente in stato attivo.

Figura - Schermata di dettaglio dell'interferenza a catalogo, con evidenza all'icona di salvataggio

Nota: Si ricorda che in EAM non è possibile eliminare le interferenze dal catalogo di appartenenza, ma soltanto disattivarle.

## 2.4 Centro di controllo per difformità e interferenze

In EAM è prevista una sezione di monitoraggio nella quale verranno gestite eventuali incongruenze dovuti della gestione dei cataloghi. La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per utilizzare questa sezione, denominata “Centro di Controllo”, l’utente deve attenersi alla seguente procedura:

Accedere all’applicazione dedicata al centro di controllo: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Centro di controllo”. In alternativa, è possibile inserire nel campo di ricerca l’opzione “Centro di controllo” per essere indirizzati all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Centro di controllo"

Selezionare la sezione desiderata del centro di controllo: dopo aver selezionato “Centro di controllo”, l’utente deve cliccare sul tab “Monitoraggio difformità/interferenze” per accedere alla sezione dedicata.

Figura - Centro di controllo di EAM, con evidenza alla sezione "Monitoraggio difformità/interferenza"

Nei paragrafi successivi sono approfondite le diverse tabelle del centro di controllo riferite alle incongruenze dei cataloghi.

### 2.4.1 Incongruenze relative a difformità nel centro di controllo

La sezione del Centro di Controllo dedicata alla gestione delle incongruenze derivanti dall’associazione tra Policy, PDL e attività risolutive presenta quattro viste distinte, ciascuna focalizzata su una specifica casistica di anomalia riscontrabile nel sistema. La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Di seguito vengono descritte nel dettaglio:

Difformità non rilevate da attività: in questa vista sono riportate tutte le difformità per le quali non risulta associato alcun Piano di Lavoro (PdL) o micro-controllo che rileva la difformità;

Difformità non risolte da attività: la tabella raccoglie le difformità per cui manca un’associazione con un PdL o micro-controllo deputato alla risoluzione della difformità;

Difformità non generabili da misure: questa vista mostra le difformità che non risultano collegate ad alcuna misura;

Difformità – attività risolutrice per la quale non è definita nessuna Policy: in questa sezione vengono elencate le difformità che, pur avendo un’attività risolutrice identificata, non risultano collegate a nessuna Policy a sistema.

Le informazioni relative alle sezioni “Difformità non rilevate da attività”, “Difformità non risolte da attività” e “Difformità – attività risolutrice per la quale non è definita nessuna Policy” sono distribuite nelle seguenti colonne precompilate:

ID Difformità

Descrizione

Tipologia

Elemento asset associato

Stato

Le informazioni relative alle sezioni “Difformità non generabili da misure” sono presenti nelle seguenti colonne:

ID Difformità

Descrizione

Tipologia asset

Stato

All’interno delle tabelle, l’utente ha la possibilità di svolgere le seguenti operazioni:

Filtrare la ricerca: selezionando l’icona del filtro evidenziata nella figura sotto, è possibile inserire il parametro desiderato in uno qualsiasi dei campi disponibili.

Selezionare il record: cliccando su un record presente in ciascuna colonna, l’utente viene automaticamente indirizzato alla corrispondente difformità all’interno del catalogo difformità.

Le figure sottostanti rappresentano le sezioni del centro di controllo appena descritte.

Figura - Sezione del centro di controllo "Difformità non rilevate da attività", con evidenza al filtro di ricerca

Figura - Sezione del centro di controllo "Difformità non risolte da attività"

Figura - Sezione del centro di controllo "Difformità - attività risolutrice per la quale non è definita una policy"

Figura - Sezione del centro di controllo "Difformità non generabili da misure"

### 2.4.2 Incongruenze relative a interferenze nel centro di controllo

La sezione del Centro di Controllo dedicata alla gestione delle interferenze derivanti dall’associazione tra Policy, PDL e attività risolutive presenta quattro viste distinte, ciascuna focalizzata su una specifica casistica di anomalia riscontrabile nel sistema. La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Di seguito vengono descritte nel dettaglio:

Interferenze non rilevate da attività: in questa vista sono riportate tutte le interferenze per le quali non risulta associato alcun Punto di Lavoro o micro-controllo che abbia svolto il ruolo di rilevatore.

Interferenze non risolte da attività: la tabella raccoglie le interferenze per cui manca un’associazione con un PDL o micro-controllo deputato alla risoluzione.

Interferenza non generabili da misure: questa vista mostra le interferenze che non risultano collegate ad alcuna misura rilevata a sistema.

Interferenza – attività risolutrice per la quale non è definita nessuna Policy: in questa sezione vengono elencate le interferenze che, pur avendo un’attività risolutrice identificata, non risultano collegate a nessuna Policy a sistema.

Le informazioni relative alle sezioni “Interferenza non rilevate da attività”, “Interferenza non risolte da attività” e “Interferenza – attività risolutrice per la quale non è definita nessuna Policy” sono distribuite nelle seguenti colonne precompilate:

ID Interferenza

Descrizione

Elemento asset associato

Stato

Le informazioni relative alle sezioni “Interferenze non generabili da misura” sono presenti nelle seguenti colonne:

ID Interferenze

Descrizione

Tipologia asset

Stato

All’interno delle tabelle, l’utente ha la possibilità di svolgere le seguenti operazioni:

Filtrare la ricerca: selezionando l’icona del filtro, è possibile inserire il parametro desiderato in uno qualsiasi dei campi disponibili.

Selezionare il record: cliccando su un record presente in ciascuna colonna, l’utente viene automaticamente indirizzato alla corrispondente interferenza all’interno del catalogo difformità.

Le figure sottostanti rappresentano le sezioni del centro di controllo appena descritte.

Figura - Sezione del centro di controllo "Interferenze non rilevate da attività", con evidenza al filtro di ricerca

Figura - Sezione del centro di controllo "Interferenze non risolte da attività"

Figura - Sezione del centro di controllo "Interferenze - attività risolutrice per la quale non è definita una policy"

Figura - Sezione del centro di controllo "Interferenze non generabili da misure"

## 2.5 Catalogo dei micro-controlli

Il catalogo dei micro-controlli contiene tutti i micro-controlli che possono essere associati ai Piani di lavoro e di conseguenza agli Ordini di lavoro associati a queste PdL.

Di seguito vengono descritte le procedure per l’inserimento e la valorizzazione dei campi all’interno del catalogo dei micro-controlli:

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| Applicabile a | Definisce se il micro-controllo è riferito a una classificazione o a un singolo elemento | Popolato automaticamente | Obbligatorio |  |
| ID Microcontrollo | Identificativo univoco del micro-controllo | Popolato automaticamente | Obbligatorio |  |
| Assieme | Clusterizzazione del micro-controllo | Pick-list | Facoltativo |  |
| Sottoassieme | Clusterizzazione del micro-controllo | Pick-list | Facoltativo |  |
| Descrizione micro-controllo | Descrizione libera del micro-controllo | Testo libero | Obbligatorio |  |
| Controllo | Tipologia del controllo | Pick-list | Obbligatorio |  |
| Elemento Asset | Elemento asset associato al micro-controllo | Pick-list | Obbligatorio |  |
| Classificazione asset | Classificazioni associate all’elemento asset | Popolato automaticamente in base all’elemento asset | Obbligatorio |  |
| Gerarchia asset associato | Asset su cui è incide il micro-controllo | Popolato automaticamente in base alla classificazione asset | Obbligatorio |  |
| Difformità rilevabili | Elenco di difformità rilevabili dal micro-controllo | Pick-list | Facoltativo |  |
| Interferenze rilevabili | Elenco di interferenze rilevabili dal micro-controllo | Pick-list | Facoltativo |  |
| Caratteristica asset | Specifiche caratteristiche tecniche dell’asset per l’associazione | Popolato manualmente | Facoltativo |  |

Tabella - Campi facoltativi e obbligatori del catalogo micro-controlli

### 2.5.1 Creazione micro-controlli

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per procedere alla creazione di un nuovo micro-controllo all’interno della piattaforma è necessario seguire i seguenti step:

Accedere all’applicazione "Catalogo Micro-Controlli": posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Catalogo Micro-Controlli” per accedere all’applicazione desiderata. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo interferenza” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'app "Catalogo micro-controlli"

Cliccare su “Nuovo Micro-controllo”: selezionare l’opzione “Nuovo Micro-controllo” evidenziato a sinistra nella figura sotto o in alternativa, cliccare sul pulsante “+” in alto a destra

Figura - Vista iniziale del catalogo dei micro-controlli, con evidenza alle azioni di aggiunta di nuovo micro-controllo

Compilare i campi in maschera: una volta effettuata l’azione descritta nello step precedente, è necessario definire le caratteristiche del micro-controllo, popolando i campi di seguito rappresentati;

Figura - Schermata di dettaglio del micro-controllo a catalogo

Associare a difformità, interferenze e caratteristiche: all’interno delle tabelle dedicate (difformità, interferenze, caratteristiche), cliccare sul tasto “+” per aggiungere nuove voci.

Figura - Schermata di dettaglio del micro-controllo a catalogo, con evidenza alle tabelle di difformità e interferenze rilevabile, e caratteristiche asset

Salvare il micro-controllo: al salvataggio (selezionando la icona sopra a destra), il sistema genera automaticamente l’ID Micro-controllo, secondo la logica di concatenazione specifica “controllo progressivo” (esempio: Controllo di tipo B : ID.CTB.1, ID.CTB.2 ecc Controllo di tipo A: ID.CPA.1, ID.CPA.2 ecc).

Figura - Schermata di dettaglio del micro-controllo a catalogo, con evidenza all'icona di salvataggio

### 2.5.2 Modifica micro-controlli

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per procedere alla modifica di un nuovo micro-controllo all’interno della piattaforma è necessario seguire i seguenti step:

Accedere all’applicazione dedicata al catalogo dei micro-controlli: come dimostrato nella figura, posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo Micro-Controlli”. In alternativa, può inserire nel campo di ricerca la voce “Catalogo Micro-Controlli” per essere indirizzato direttamente all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo micro-controlli"

Selezionare il micro-controllo da modificare: dalla vista elenco, l’utente può individuare il micro-controllo di interesse e selezionarlo cliccando sul record presente nella colonna “ID Micro-controllo”. Selezionando l’icona filtro evidenziata in Figura sotto, è possibile applicare criteri di ricerca specifici sulle colonne disponibili (ID Micro-controllo, Descrizione, Tipologia Asset, Assieme, Sottoassieme).

Figura - Vista iniziale del catalogo dei micro-controlli, con evidenza al filtro di selezione

Apportare le modifiche al micro-controllo: accedendo alla maschera di dettaglio, l’utente può modificare i seguenti campi:

Descrizione;

Assieme

Sottoassieme;

Difformità/interferenze rilevabili;

Caratteristiche asset;

Gestire i contenuti tabellare: nelle sezioni dedicate a difformità, interferenze o caratteristiche, evidenziate nella figura sotto, è possibile utilizzare il pulsante “+” per aggiungere nuove voci o l’icona “cestino” per rimuovere quelle esistenti.

Salvare le modifiche tramite l’apposito comando: selezionando l’icona “Salva”, l’utente conferma le modifiche apportate al micro-controllo.

Figura - Schermata di dettaglio del micro-controllo a catalogo, con evidenza alle tabelle di difformità e interferenze rilevate, caratteristiche asset, e alle icone di aggiunta, cancellazione e salvataggio

Nota: La modifica del campo “Elemento” comporta la variazione automatica delle classificazioni associate. In tal caso, è necessario che l’utente verifichi e, se necessario, aggiorni manualmente i valori proposti.

### 2.5.3 Disattivazione micro-controlli

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per procedere alla disattivazione di un nuovo micro-controllo all’interno del catalogo è necessario seguire i seguenti step:

Accedere all’applicazione dedicata ai micro-controlli: come dimostrato nella figura sotto, posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo Micro-controlli”. In alternativa, può inserire nel campo di ricerca la voce “Catalogo Micro-controlli” per essere indirizzato direttamente all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'app "Catalogo micro-controlli"

Selezionare il micro-controllo da eliminare: dalla vista elenco, l’utente può individuare il micro-controllo di interesse e selezionarlo cliccando sul record presente nella colonna “ID Micro-controllo”. Selezionando l’icona filtro, è possibile applicare criteri di ricerca specifici sulle colonne disponibili.

Figura - Vista iniziale del catalogo dei micro-controlli, con evidenza al filtro di selezione

Verificare eventuali associazioni attive: prima di procedere, l’utente deve verificare che il micro-controllo selezionato non risulti associato ad ordini o attività in corso. In presenza di associazioni attive, il sistema impedisce l’eliminazione del micro-controllo.

Avviare l’eliminazione del micro-controllo: selezionando l’azione “Cancella micro-controllo” disponibile nella sezione “Altre azioni” a sinistra della schermata di dettaglio, l’utente avvia il processo di eliminazione.

Figura - Schermata di dettaglio del micro-controllo con evidenza alla azione di disattivazione

Confermare l’eliminazione del micro-controllo: alla comparsa del messaggio di conferma, selezionare “Sì” per completare l’eliminazione. Il sistema registra la rimozione del micro-controllo.

Figura - Dialog di conferma per la disattivazione del micro-controllo

Visualizzare il micro-controllo eliminato: una volta eliminato, il micro-controllo apparirà sbarrato nella lista dei record dell’applicazione, a conferma dell’avvenuta eliminazione.

Figura - Vista iniziale del catalogo dei micro-controlli con evidenza al micro-controllo disattivato

## 2.6 Catalogo Modelli Target

Il catalogo contiene l’elenco completo dei target di raffronto utilizzati per la generazione delle policy e per garantire il corretto funzionamento del processo di Operation & Maintenance (O&M). I target sono specificati per ciascun componente e variano in base al modello e/o alla tipologia del componente.

Di seguito vengono descritte le procedure per l’inserimento e la valorizzazione dei campi all’interno del catalogo dei modelli-target:

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| Applicabile a | Definisce se il modello è riferito a una classificazione o a un singolo elemento | Popolato automaticamente | Obbligatorio |  |
| ID Target | Identificativo univoco del target | Popolato automaticamente | Obbligatorio |  |
| Descrizione | Descrizione libera del target | Testo libero | Obbligatorio |  |
| Elemento asset | Componente asset associato | Pick-list | Obbligatorio |  |
| Classificazioni asset | Classificazioni collegate all’elemento asset | Popolato automaticamente in base all’elemento asset | Obbligatorio |  |
| Gerarchia asset associato | Struttura gerarchica dell’asset (es. asset primario e derivati) | Popolato automaticamente in base alla classificazione asset | Obbligatorio | Obbligatorio |
| Nuovo valore |  | Testo libero (numerico) | Facoltativo |  |
| Attributo | Attributo tecnico per determinazione modello | Pick-list | Obbligatorio |  |
| Valore attributo | Valore dell’attributo selezionato | Pick-list | Obbligatorio |  |
| Modello | Modello associato al target | Testo libero (numerico) | Obbligatorio |  |
| Target | Valore target numerico | Testo libero (numerico) | Facoltativo |  |
| Modificato da | Matricola utente ultima modifica | Popolato automaticamente | Obbligatorio |  |
| Data ultima modifica | Data ultima modifica interferenza | Popolato automaticamente | Obbligatorio |  |

Tabella - Campi facoltativi e obbligatori del catalogo modelli-target

### 2.6.1 Creazione modelli-target

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per creare un nuovo modello target all’interno del catalogo dedicato, è necessario seguire i seguenti step:

Accedere all’applicazione Catalogo modelli-target: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo modelli-target”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo modelli-target” per accedere direttamente all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo modelli target"

Cliccare su “Nuovo modello target”: selezionare l’opzione “Nuovo modello target” disponibile nella sezione “Azioni comuni” o, in alternativa, cliccare sul pulsante “+” in alto a destra.

Figura - Vista iniziale del catalogo modelli target, con evidenza alle azioni di aggiunta di nuovo modello target

Compilare i campi obbligatori e facoltativi in maschera: una volta aperta la schermata di dettaglio, è necessario compilare i campi di seguito elencati:

Figura - Schermata di dettaglio del modello target a catalogo

Inserire i modelli target: nella sezione dedicata, il caricamento dei modelli può avvenire in due modalità:

Per asset di linea: per generare un modello target per un asset di linea, l’utente deve popolare il campo “Elemento asset” con un oggetto che sia compresso nella gerarchia dell’asset di primo livello “Stazione” (es. Stallo, Trasformatore, ecc).

Aggiunta manuale: cliccare sul tasto “+” e compilare una riga per volta; il campo Modello viene popolato automaticamente in base al tipo di asset selezionato;

Aggiunta massiva: cliccare su “Genera lista modelli” nella barra laterale per recuperare i modelli associati alla tipologia di asset tramite anagrafica.

Per asset di stazione: per generare un modello target per un asset di stazione, l’utente deve popolare il campo “Elemento asset” con un oggetto che sia compresso nella gerarchia dell’asset di primo livello “Stazione” (es. Stallo, Trasformatore, ecc). Per questi elementi è obbligatorio selezionare entrambi i campi Codice costruttore e Descrizione modello dalle liste predefinite. Il sistema genera il campo Modello combinando i due valori che viene poi inserito in modo progressivo all’interno della tabella. Se uno dei campi non viene compilato il sistema genera un alert bloccante ed impedisce la generazione del modello.

Figura - Schermata di dettaglio del modello a catalogo, con evidenza alle azioni di aggiunta di un nuovo modello target

Nota: il sistema impedisce l’inserimento di duplicati, bloccando il salvataggio di righe già presenti.

Inserire il valore target: nella sezione Modelli-target, è necessario valorizzare il campo Target, che rappresenta il valore tecnico di riferimento da confrontare con i dati reali dell’asset. Per asset di stazione, il valore deve essere identico per ciascuna combinazione “Codice costruttore” e “Descrizione modello”; in caso contrario, il sistema restituisce un errore.

Salvare il modello: selezionando l’icona “Salva”, l’utente conferma la creazione del modello-target. Al salvataggio, il sistema genera automaticamente l’ID Target, secondo la sintassi TRG.Classificazione.Progressivo (es. TRG.STR.1).

Figura - Schermata di dettaglio del modello target a catalogo, con evidenza all'icona di salvataggio

### 2.6.2 Modifica modelli-target

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per modificare un modello target all’interno del catalogo dedicato è necessario seguire i seguenti step:

Accedere all’applicazione Catalogo modelli-target: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo modelli-target”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo modelli-target” per accedere direttamente all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo modelli target"

Selezionare il modello target da modificare: dalla vista elenco, l’utente può individuare il modello di interesse e selezionarlo cliccando sul record presente nella colonna “ID Target”. Selezionando l’icona di filtro, è possibile applicare criteri di ricerca specifici sulle colonne disponibili (Applicabile a, ID Target, Descrizione).

Figura - Vista iniziale del catalogo modelli target, con evidenza al filtro di selezione

Modificare il valore target tramite il campo in testata: all’interno della scheda del modello target, l’utente può aggiornare il valore associato ai modelli utilizzando il campo “Nuovo valore” in testata evidenziato nella figura sottostante. Per effettuare la modifica, è necessario prima inserire il nuovo valore nel campo indicato e cliccare sul pulsante “Propaga modifica”, applicando in questo modo il nuovo valore a tutti i modelli associati a quel target.

Figura - Schermata di dettaglio del modello target, con evidenza al campo "Nuovo valore" e al pulsante "Propaga modifica"

Salvare le modifiche apportate: una volta aggiornato il valore, il sistema genera automaticamente le nuove attività previste dal DSS, secondo le policy collegate al target modificato. Tutte le modifiche effettuate vengono tracciate e storicizzate dal sistema, con registrazione della data della modifica e la matricola dell’utente che ha eseguito l’operazione.

### 2.6.3 Eliminazione modelli-target

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per eliminare un modello target all’interno del catalogo dedicato è necessario seguire i seguenti step:

Accedere all’applicazione dedicata al catalogo modelli-target: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo modelli-target”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo modelli-target” per accedere direttamente all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo micro-controlli"

Selezionare il modello target da eliminare: dalla vista elenco, l’utente può individuare il modello di interesse e selezionarlo cliccando sul record presente nella colonna “ID Target”. Selezionando l’icona di filtro, è possibile applicare criteri di ricerca specifici sulle colonne disponibili (Applicabile a, ID Target, Descrizione).

Figura - Vista iniziale del catalogo modelli target, con evidenza al filtro di selezione

Eliminare un modello target dalla vista di dettaglio: selezionando il record desiderato cliccando sull’ID Target, l’utente accede alla scheda di dettaglio del modello. Dal menu laterale è possibile cliccare sulla voce “Elimina modello target” per completare la rimozione, come rappresentato nella Figura.

Figura - Schermata di dettaglio del modello target, con evidenza all'azione di eliminazione del modello target

Eliminare un singolo modello dalla tabella Modelli-target: all’interno della vista di dettaglio, nella sezione dedicata ai modelli associati, è possibile eliminare un singolo modello cliccando sull’icona cestino accanto al modello da rimuovere, come rappresentato nella figura.

Figura - Schermata di dettaglio del modello target, con evidenza all'azione di eliminazione del singolo modello

Nota: In presenza di un modello ancora presente in anagrafica, il sistema mostra un alert bloccante con il messaggio: “Rimosso modello presente in anagrafica”.

### 2.6.4 Modifica massiva modelli

Per modificare massivamente i modelli all’interno del catalogo dedicato è necessario seguire i seguenti step:

Accedere all’applicazione dedicata al catalogo modelli-target: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo modelli-target”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo modelli-target” per accedere direttamente all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo modelli target"

Selezionare il modello target da modificare: dalla vista elenco, l’utente può individuare il modello di interesse e selezionarlo cliccando sul record presente nella colonna “ID Target”. Selezionando l’icona di filtro evidenziata in figura, è possibile applicare criteri di ricerca specifici sulle colonne disponibili.

Figura - Vista iniziale del catalogo modelli target, con evidenza al filtro di selezione

Accedere alla funzionalità di modifica valore massivo: all’interno della scheda di dettaglio del modello target, l’utente deve selezionare la voce “Modifica massiva” disponibile nel menu laterale.

Figura - Schermata di dettaglio del modello target, con evidenza all'azione di modifica massiva

Compilare la finestra di modifica: Una volta selezionata l’azione, il sistema apre la finestra di dialogo rappresentata in figura, contenente le seguenti informazioni:

Campo “Nuovo Valore” editabile, nel quale l’utente può inserire il nuovo valore da applicare;

L’elenco completo dei modelli associati al target selezionato;

Una checkbox per ogni modello, da utilizzare per effettuare una selezione puntuale dei modelli da aggiornare.

Selezionare i modelli e applicare il nuovo valore: l’utente seleziona i modelli desiderati spuntando le rispettive checkbox ed inserisce il nuovo valore nel campo “Valore”. Successivamente, clicca sul pulsante “OK” per confermare l’aggiornamento.

Figura - Dialog di selezione di modelli da modificare

Nota: La modifica viene applicata esclusivamente ai modelli selezionati. Il sistema genera automaticamente nuove attività da parte del DSS per tutte le Policy collegate ai modelli aggiornati. Tutte le modifiche effettuate tramite la funzione “Modifica valore massivo” vengono tracciate con la data della modifica e la matricola dell’utente che ha effettuato l’operazione.

## 2.7 Catalogo Misure e Variabili di stato

Il catalogo Misure e Variabili di Stato raccoglie tutte le informazioni caricate a sistema relative ai parametri rilevati e agli stati operativi dei componenti. Costituisce un riferimento essenziale per il monitoraggio, l’analisi e l’attivazione delle policy nel processo di Operation & Maintenance (O&M).

Di seguito vengono descritte le procedure per l’inserimento e la valorizzazione dei campi all’interno del catalogo misure e variabili di stato:

| Nome campo | Descrizione | Modalità di valorizzazione | Tipo parametro | Obbligatorietà |
| --- | --- | --- | --- | --- |
| Applicabile a | Determina se la misura/variabile di stato è riferito a una classificazione o a un singolo elemento | Popolato automaticamente | Misure e variabile | Obbligatorio |
| ID Misura/Variabile | ID univoco misura/variabile | Popolato automaticamente | Misure e variabile | Obbligatorio |
| Descrizione | Descrizione della misura/variabile | Testo libero | Misure e variabile | Obbligatorio |
| Tipo parametro | Tipologia del parametro | Pick-list | Misure e variabile | Obbligatorio |
| Classificazione Asset | Tabella contenente le classificazioni alle quali è associata la misura/variabile | Popolamento automatico in base all’elemento asset | Misure e variabile | Obbligatorio |
| Gerarchia asset associato | Struttura gerarchica dell’asset (es. asset primario e derivati) | Popolamento automatico in base alla classificazione asset | Misure e variabile | Obbligatorio |
| Tipologia input | Fonte da cui deriva la variabile | Pick-list | Variabile di stato | Obbligatorio |
| Input | Parametro usato come input | Pick-list | Variabile di stato | Obbligatorio |
| Tipologia asset | Asset / classificazione a cui si riferisce | Pick-list | Misure e variabile | Obbligatorio |
| Calcolo | Modalità di calcolo della variabile. Il campo può essere valorizzato con: - Valore da lista predefinita, come SUM (per input di tipo "Misura") - Formula a testo libero (max 3500 caratteri), secondo le regole: prefisso VAR per misure, TRG per soglie; nessun altro input ammesso. Nel caso di variabili di stato, può assumere i seguenti valori: COUNT DATAACT DATAOPT DIFFANNI Formula a testo libero MAXHICONDUTTORI CountForListStatiAttivitaFinestra: Per il calcolo countForListStatiAttivitaFinestra, sono visibili i campi “Stato attività” (valori: “Eseguito”, “Aperto”) e “Periodo di calcolo”. calculateDaily gestione evento eseguito INTERF: calcola il numero di opere interferenti presenti a sistema sotto la campata. È applicabile solo per variabili associate ad asset di tipo campata. | Pick-list/testo libero | Variabile di stato | Obbligatorio |
| Unità di misura | Unità associata al parametro | Pick-list | Misure e variabile | Facoltativo |
| Stato | Stato attività per il calcolo. Visualizzabile solo quando il calcolo è COUNTFORLISTASTATILISTAATTIVITAFINESTRA | Pick-list | Variabile di stato | Facoltativo |
| Periodo di calcolo | Periodo temporale di riferimento (in giorni) | Testo libero (numerico) | Variabile di stato | Facoltativo |
| Reset? | Definisce se necessario o meno eseguire il reset della variabile | Flag | Variabile di stato | Facoltativo |
| Calcolo parametri misure | Tabella contenente i parametri utilizzati nel calcolo della variabile di stato | Tabella | Variabile di stato | Obbligatorio |
| Reset parametri misure | tabella contente l’elenco di attività che determina il reset della variabile di stato, editabile e visibile solo se il campo “Reset?” viene valorizzato con “SI”. | Tabella | Variabile di stato | Obbligatorio se il flag “Reset?” attivo |
| Misure alfanumeriche | Tabella in cui è possibile inserire valori di misure che non sono puramente numerici. Nel campo valore va inserito un carattere esclusivamente numerico, nel campo descrizione può essere inserito il valore alfanumerico a cui corrisponde. Es. 1 = Alta, 2= Media, 3=Lieve | Tabella | Misure e variabile | Facoltativo |

Tabella - Campi facoltativi e obbligatori del catalogo di misure e variabili di stato

### 2.7.1 Creazione misure e variabili di stato

Misure

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per aggiungere una misura al catalogo delle misure, l’utente di EAM deve seguire la procedura di seguito descritta.

Accedere all’applicazione dedicata alla gestione delle misure: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione relativa al catalogo misure. In alternativa, può inserire nel campo di ricerca il nome dell’applicazione per essere indirizzato direttamente.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo misure"

Cliccare su “Nuova Misura/Variabile di stato”: selezionare l’opzione “Nuova Misura/variabile di stato” evidenziato a sinistra nella Figura sotto, o in alternativa, cliccare sul pulsante “+” in alto a destra;

Figura - Vista iniziale del catalogo misure e variabili di stato, con evidenza alle azioni di aggiunta di nuova misura

Compilare i campi obbligatori e facoltativi in maschera: una volta effettuata l’azione descritta nello step precedente, è necessario definire le caratteristiche della misura, popolando i campi di seguito rappresentati;

Figura - Schermata di dettaglio del catalogo misure e variabili di stato

Salvare la misura: una volta compilati i campi obbligatori, l’utente può confermare l’inserimento cliccando sull’icona in alto a destra. Il sistema registra la misura e ne abilita l’utilizzo. Le misure possono essere rilevate solo tramite un OdL. Al momento del salvataggio, il sistema crea l’ID Misura secondo la seguente logica: prefisso “Var”, abbreviativo dell’anagrafica di riferimento (S per stazioni e L per linee) e progressivo crescente da 0 a n;

Figura - Schermata di dettaglio delle misure / variabili di stato, con evidenza all'icona di salvataggio

Variabili di stato

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per aggiungere una variabile di stato al catalogo, l’utente di EAM deve seguire la procedura di seguito descritta.

Accedere all’applicazione dedicata alla gestione delle misure: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione relativa al catalogo misure. In alternativa, può inserire nel campo di ricerca il nome dell’applicazione per essere indirizzato direttamente.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo Misure"

Cliccare su “Nuova Misura/Variabile di stato”: selezionare l’opzione “Nuova Misura/variabile di stato”, o in alternativa, cliccare sul pulsante “+” in alto a destra;

Figura - Vista iniziale del catalogo modelli target, con evidenza alle azioni di aggiunta di nuova misura/variabili di stato

Compilare i campi obbligatori e facoltativi in maschera: una volta effettuata l’azione descritta nello step precedente, è necessario definire le caratteristiche della variabile di stato, popolando i campi richiesti nella maschera di dettaglio della variabile di stato;

Confermare l’operazione: dopo aver inserito tutti i dati, l’utente deve salvare la variabile. Il calcolo della variabile viene eseguito automaticamente dal sistema al primo inserimento e successivamente nei seguenti casi:

Input attività: a ogni creazione o cambio stato dell’attività.

Input misure: a ogni modifica della misura.

Input attributi: a ogni variazione dell’attributo dell’asset specificato.

Salvare la variabile di stato: una volta compilati i campi obbligatori, l’utente può confermare l’inserimento cliccando sulla icona in alto a destra, rappresentata in figura. Il sistema registra la variabile e ne abilita l’utilizzo. Al momento del salvataggio, il sistema crea l’ID Variabile secondo la regola descritta per le misure.

Figura - Schermata di dettaglio delle misure / variabili di stato, con evidenza all'icona di salvataggio

### 2.7.2 Modifica misure e variabili di stato

Misure

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema.

Per modificare una misura, l’utente di EAM deve seguire la procedura di seguito descritta.

Accedere all’applicazione dedicata alla gestione delle misure: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente selezionare l’applicazione “Catalogo Misure”. In alternativa, può inserire nel campo di ricerca il nome dell’applicazione per essere indirizzato direttamente.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo Misure"

Selezionare la misura da modificare: dalla vista elenco l’utente può individuare la misura di interesse applicando i filtri disponibili (Applicabile a, ID misura/variabile, Descrizione, Tipo parametro) e selezionarla cliccando sull’ID Misura corrispondente.

Figura - Vista elenco iniziale del catalogo Misure, con evidenza al filtro di ricerca

Apportare le modifiche: quando autorizzato, l’utente può modificare le informazioni editabile della misura selezionata, tra cui:

Descrizione

Unità di misura

Associazione con l’ID del target

Tipo parametro: in caso di modifica da “misura” a “variabile di stato” o viceversa, il sistema gestisce automaticamente le modifiche necessarie alle logiche e ai parametri associati;

Salvare le modifiche tramite l’apposito comando: selezionando l’icona dedicata in alto a destra, l’utente conferma le modifiche apportate.

Figura - Schermata di dettaglio delle misure / variabili di stato, con evidenza all'icona di salvataggio

Nota: Tutte le modifiche vengono storicizzate automaticamente con data e matricola dell’utente che ha effettuato l’operazione.

Variabili di stato

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per modificare una variabile di stato, l’utente di EAM deve seguire la procedura di seguito descritta.

Accedere all’applicazione dedicata alla gestione delle variabili di stato: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente selezionare l’applicazione “Catalogo Misure”. In alternativa, può inserire nel campo di ricerca il nome dell’applicazione per essere indirizzato direttamente.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo Misure"

Selezionare la variabile da modificare: Dalla vista elenco l’utente può individuare la variabile di interesse applicando i filtri disponibili (Applicabile a, ID misura/variabile, Descrizione, Tipo parametro) e selezionarla cliccando sul record corrispondente.

Figura - Vista iniziale del catalogo delle misure, con evidenza al filtro di selezione

Apportare le modifiche: l’utente abilitato può modificare le informazioni editabili della variabile selezionata, tra cui:

Descrizione

Unità di misura

Input

Tipo parametro: in caso di modifica da “variabile di stato” a “misura” o viceversa, il sistema gestisce automaticamente le modifiche necessarie alle logiche e ai parametri associati)

Salvare le modifiche tramite l’apposito comando: selezionando l’icona in alto a destra, l’utente conferma le modifiche apportate.

Figura - Schermata di dettaglio delle misure / variabili di stato, con evidenza all'icona di salvataggio

Nota: Tutte le modifiche vengono storicizzate automaticamente con data e matricola dell’utente che ha effettuato l’operazione

### 2.7.3 Eliminazione misure e variabili di stato

Misure

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema.

Per eliminare una misura, l’utente di EAM deve seguire la procedura di seguito descritta.

Accedere all’applicazione dedicata alla gestione delle misure: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo misure/variabili di stato”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo misure/variabili di stato” per essere indirizzato all’applicazione desiderata.

Figura 1 - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo Misure"

Selezionare la misura da eliminare: dalla vista elenco l’utente può individuare la misura di interesse e selezionarla cliccando sul record corrispondente. Selezionando l’icona di filtro evidenziata nella Figura sotto, l’utente può definire i filtri di ricerca sulle colonne della vista.

Figura - Vista iniziale del catalogo delle misure / variabili di stato, con evidenza al filtro di selezione

Eliminare la misura: per eliminare una misura, l’utente abilitato deve selezionare l’azione evidenziata nella figura sottostante e confermare l’azione.

Figura - Schermata di dettaglio della misura / variabile di stato a catalogo, con evidenza all'azione di eliminazione e la dialog di conferma di eliminazione

Nota: Il sistema fornisce evidenza di eventuali incongruenze qualora la misura risulti ancora utilizzata in policy o altre funzionalità del Centro di Controllo.

Variabili di stato

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per eliminare una variabile di stato, l’utente di EAM deve seguire la procedura di seguito descritta.

Accedere all’applicazione dedicata alla gestione delle variabili di stato: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo Misure”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo Misure” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo Misure"

Selezionare la variabile di stato da eliminare: dalla vista elenco l’utente può individuare la variabile di interesse e selezionarla cliccando sull’ID Variabili di stato. Selezionando l’icona di filtro, l’utente può definire i filtri di ricerca sulle colonne della vista.

Figura - Vista iniziale del catalogo delle misure / variabili di stato, con evidenza al filtro di selezione

Eliminare la variabile di stato: per eliminare una variabile di stato, l’utente abilitato deve selezionare l’azione evidenziata nella figura e confermare la eliminazione.

Figura - Schermata di dettaglio della misura / variabile di stato a catalogo, con evidenza all'azione di eliminazione e la dialog di conferma di eliminazione

Nota: Il sistema fornisce evidenza di eventuali incongruenze qualora la variabile risulti ancora utilizzata in policy o altre funzionalità del Centro di Controllo.

## 2.8 Catalogo indicatori di condizione tecnica

I cataloghi degli indicatori di condizione tecnica descrivono il processo di inserimento a sistema degli indicatori di condizione tecnica o HI (Health Index, tasso di guasto, tasso di anomalia, HI di degrado conduttore, HI degrado fune di guardia). Il calcolo degli indicatori è effettuato tramite l’applicazione standard di MAS “Health”.

### 2.8.1 Modulo Health (introduzione e procedura)

Il modulo Health di Maximo permette di monitorare e valutare lo stato di salute degli asset in modo dinamico, supportando strategie di manutenzione basate sulle reali condizioni operative. All’interno del modulo “Health” di EAM, è possibile assegnare a ciascun asset un punteggio di salute (health score), calcolato a partire da dati come misurazioni, eventi storici e guasti.

Queste informazioni aiutano a identificare in anticipo situazioni critiche e pianificare interventi mirati, riducendo il rischio di guasti improvvisi. Il modulo è integrato con le principali funzionalità di Maximo, e rappresenta uno strumento chiave per la manutenzione predittiva e la gestione del rischio.

La creazione dei TdA e TdG nel modulo “Health” prevede la definizione degli asset di riferimento, selezionabili tramite query dedicate. La selezione degli Asset avviene nella sezione “Scored Assets” dello Scoring group. A sistema è presente un set di query predefinito (ad esempio “tutti i conduttori”), a cui l’utente può aggiungerne di nuove, definite in base alle esigenze specifiche. Per creare una nuova query, l’utente deve, all’interno dell’applicazione “Asset” del modulo “Manage”, definire il perimetro tramite filtri nella sezione di ricerca (applicabili a tutti gli attributi degli Asset) e salvare la query, attribuendole una denominazione specifica. Una volta salvata, la query sarà selezionabile nella sezione “Scoring Assets” degli Scoring group nel modulo “Health”.

### 2.8.2 Creazione Health Index, Tasso di Guasto e Tasso di Anomalia

Query

Per avviare il processo di valutazione degli asset nel modulo Health, è necessario prima definire una query di selezione di asset. Tale query consente di individuare precisamente gli asset oggetto di analisi. Per creare la query, l’utente deve eseguire i seguenti passaggi:

Recuperare la classificazione dell’asset di interesse: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare "Amministrazione" o digitare "Classificazioni" nel campo di ricerca. In alternativa, può inserire nel campo di ricerca l’opzione “Classificazioni” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Classificazioni"

Selezionare la classificazione di interesse: dalla vista elenco rappresentata in Figura sottostante, l’utente deve selezionare la classificazione di asset sulla quale dovrà incidere l’indicatore di condizione tecnica.

Figura - Vista iniziale dell'applicazione "Classificazioni"

Selezionare il percorso di interesse: dopo aver selezionato la classificazione di asset a cui è associato l’indicatore di condizione tecnica (es. “Cavi di potenza”), l’utente deve copiare il record presente nel campo “Percorso classificazioni”, evidenziato in figura.

Figura - Vista di dettaglio della classificazione "Cavo di potenza", con evidenza al campo "Percorso classificazione"

Accedere all’applicazione Asset: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione "Asset" oppure digitare "Asset" nel campo di ricerca.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Asset"

Impostare i criteri di filtro nella maschera di ricerca avanzata: nell’applicazione "Asset", selezionare il campo "Altri campi di ricerca" cliccando sull’icona con i tre puntini in alto a destra,. Selezionare il campo "Classificazione" e cliccare su "Seleziona classificazione". Incollare il percorso precedentemente copiato e cliccare su "Trova" per ottenere l’elenco degli asset corrispondenti, come dimostrato in figura. Una volta completato questo passaggio, il sistema mostrerà l’elenco di asset con la classificazione selezionata.

Figura - Vista iniziale dell'applicazione "Asset" con evidenza all'azione "Altri campi di ricerca"

Figura - Dialog "Altri campi di ricerca" con evidenza al campo dove inserire il percorso copiato e al tasto “Trova”

Figura - Elenco di asset associati a classificazione previamente selezionata

Salvare la query: dopo aver visualizzato i risultati, cliccare su "Salva query corrente", assegnare nella sezione dedicata un nome identificativo e selezionare il flag "Pubblico" per rendere disponibile la query ad altri utenti. La query creata sarà utilizzata successivamente nel modulo Health per l’impostazione dei gruppi di punteggio e per il calcolo degli Health Index.

Figura - Vista iniziale dell'applicazione "Asset" con evidenza all'azione "Salva query corrente"

Figura - Dialog "Salva query corrente"

Nota: Le query utilizzate per il calcolo del HI si costruiscono sull’applicazione “Asset”, quelle per il calcolo del tasso di guasto (TdG) e tasso di anomalie (TdA), sull’applicazione “Collocazioni”.

Contributors

I contributors sono delle formule di calcolo dell’indicatore di condizione e vengono definite tramite le sintassi specifiche di Maximo. Queste formule vengono poi utilizzate nelle formule definite per il calcolo degli Health Index (HI). Per impostare un contributor all’interno del modulo Health è necessario seguire la procedura di seguito elencata:

Accedere al modulo Health: per accedere al modulo “Health” l’utente nella vista iniziale di EAM deve selezionare l’opzione evidenziata sulla Figura sotto.

Figura- Vista iniziale della piattaforma IBM Maximo, con evidenza al modulo Health

Accedere alla schermata di creazione: posizionando il cursore sulla barra laterale a sinistra, l’utente deve selezionare l’opzione “Impostazione di punteggio”. Successivamente deve selezionare il tab “Contributori” per essere indirizzato all’applicazione desiderata.

Figura -Lista di applicazioni di EAM, con evidenza all'applicazione "Impostazioni di punteggio" del modulo Health

Figura 2 - Vista iniziale dell'applicazione "impostazioni di punteggio", con evidenza al tab "Contributors"

Creare il contributor: una volta nella vista dei contributors, l’utente deve selezionare il tasto “Crea contributore” per aprire la scheda di generazione del contributor. In alternativa, è possibile duplicare una formula già esistente selezionando l’opzione "Duplicate".

Figura - Vista del tab "Contributors", con evidenza al tasto "Crea contributor"

Compilare i campi necessari: nella scheda rappresentata in figura sottostante, l’utente deve compilare i seguenti campi per definire il contributor:

Nome: inserire un nome identificativo per la formula;

Descrizione: fornire una breve descrizione della logica di calcolo;

Oggetto: selezionare l’opzione "Asset";

Origine dati: selezionare l’opzione "Formula";

Unità di misura: selezionare l’unità di misura associata al risultato della formula;

Formula: Inserire la formula dal catalogo indicatori di condizione tecnica per il contributor che si sta creando. Le formule devono essere strutturate utilizzando delle funzioni specifiche di Maximo.

Figura- Schermata di creazione del contributor

Definire i parametri di punteggio: inserire nei campi evidenziati nella figura sottostante il range di valori perimetrati da:

Miglior valore possibile

Possibile peggior valore

Figura - Schermata di creazione del contributor, con evidenza ai campi "Migliore valore possibile" e "Possibile peggior valore"

Salvare la formula: una volta completata la compilazione, cliccare su "Create". La formula sarà ora visibile nel tab "Contributori".

Score

Dopo aver creato il contributore, è necessario definire i tipi di punteggio (score) e i relativi gruppi (scoring group). Per creare uno Score, l’utente deve eseguire i seguenti passaggi:

Accedere al modulo Health: per accedere al modulo “Health” l’utente deve selezionare l’opzione evidenziata sulla figura sotto.

Figura - Vista iniziale della piattaforma IBM Maximo, con evidenza al modulo Health

Accedere al tab "Tipi di punteggio": posizionando il cursore sulla barra laterale a sinistra, l’utente deve selezionare l’opzione “Impostazione di punteggio”. Successivamente, deve selezionare il tab “Tipi di punteggio” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Impostazioni di punteggio" del modulo Health

Figura - Vista iniziale dell'applicazione "impostazioni di punteggio", con evidenza al tab "Tipi di punteggio"

Creare lo “score”: nella schermata di creazione dello scoring group, l’utente deve popolare i campi con le informazioni necessarie.

Figura - Schermata di creazione del tipo di punteggio

Nome

Descrizione

Valori minimo e massimo: Inserire i valori numerici coerenti con quelli utilizzati nella formula del contributore.

Nota: In caso di modifica ai valori della formula, è obbligatorio aggiornare anche quelli nel tipo di punteggio per mantenere la coerenza del calcolo.

Definire gli intervalli: nella tabella evidenziata nella figura sottostante, l’utente deve definire i limiti inferiori e superiori attribuiti allo score appena creato. Se necessario, l’utente può aggiungere una nuova riga di intervallo selezionando il tasto “Aggiungi intervallo”.

Figura -Schermata di creazione del contributor, con evidenza al tasto di creazione di intervallo

Salvare lo score: selezionando il tasto “Salva”, l’utente conferma la creazione dello score. Il nuovo tipo di punteggio verrà visualizzato nel tab "Tipi di punteggio".

Figura - Schermata di creazione del contributor, con evidenza al tasto di salvataggio e annullamento

Scoring group

Dopo la creazione dello score, è necessario procedere con la creazione del Gruppo (Scoring group) effettuando i seguenti passaggi:

Accedere al tab “Gruppi”: posizionando il cursore sulla barra laterale a sinistra, l’utente deve selezionare l’opzione “Impostazione di punteggio”, come dimostrato nella figura. Successivamente deve selezionare il tab “Gruppi” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Impostazioni di punteggio" del modulo Health

Figura - Vista iniziale dell'applicazione "impostazioni di punteggio", con evidenza al tab "Gruppi"

Creare un nuovo gruppo di punteggio: selezionando il tasto “Crea un gruppo di punteggi”, l’utente viene indirizzato alla scheda di generazione del gruppo.

Figura - Vista del tab "Gruppi", con evidenza al tasto "Gruppi di punteggio

Compilare i campi necessari: Nella vista di generazione dello scoring group, l’utente deve compilare i seguenti campi obbligatori:

Nome;

Descrizione;

Oggetto: Il campo sarà precompilato con "Asset" per il calcolo dell’HI. Per i calcoli relativi a Tasso di anomalia (TdA) e Tasso di guasto (TdG), l’utente deve impostare il campo “Oggetto” su "Ubicazione".

Figura 3 - Schermata di generazione dello "Scoring group"

Associare la query: all’interno della sezione dedicata, l’utente seleziona l’azione evidenziata in figura per inserire la query precedentemente creata secondo quanto descritto nella sezione dedicata alla generazione della query. All’interno della dialog di selezione, l’utente può utilizzare dei filtri per trovare la query di interesse.

Figura - Schermata di generazione dello scoring group, con evidenza al tasto di selezione della query

Figura - Dialog di selezione della query

Salvare lo scoring group: per salvare lo scoring group appena impostato, l’utente deve selezionare il tasto “Crea”.

Figura - Schermata di generazione dello "Scoring group", con evidenza al tasto "Crea"

Aggiungere i punteggi: una volta generato lo scoring group, è necessario procedere all’aggiunta dei punteggi al gruppo. Selezionando il gruppo appena generato, l’utente viene indirizzato alla schermata di dettaglio del gruppo dove sarà possibile associarlo ai punteggi necessari, selezionando il tasto “Aggiungi punteggio”.

Figura - Vista iniziale del tab "Scoring group", con evidenza al gruppo appena generato

Figura - Schermata di dettaglio dello scoring group

Cliccando sul tasto “Aggiungi punteggio”, il sistema apre la dialog che permette all’utente di selezionare lo score da aggiungere. Per aggiungere lo score, si deve cliccare sull’opzione desiderata e successivamente selezionare “Fine”. Una volta eseguita questa procedura, lo score diventerà visibile all’interno di questa sezione.

Figura - Dialog di aggiunta di punteggio

Figura - Schermata di dettaglio dello scoring group, con evidenza al punteggio aggiunto

Associare il contributor e assegnare i pesi: una volta aggiunto lo score allo scoring group, è necessario associare il contributor generato secondo quanto definito nel capitolo “Contributors” allo score. Selezionando la voce dello score, l’utente viene indirizzato alla schermata di dettaglio. Selezionando il tasto “+” evidenziato, EAM apre la dialog, dove sarà possibile selezionare il contributor da aggiungere.

Figura - Schermata di dettaglio del punteggio, con evidenza al flag "Attivo" e al tasto di aggiunta di contributor

Figura - Dialog di aggiunta del contributor al punteggio

Una volta visibile il contributor all’interno della schermata del punteggio, l’utente deve selezionare il tasto “Modifica” per poter assegnare il peso desiderato, tramite la dialog. In presenza di un solo contributor, assegnare il valore 100, in presenza di più contributori assicurarsi che la somma dei pesi sia pari a 100. In caso contrario il sistema non permette di concludere l’operazione.

Figura - Schermata di dettaglio del punteggio, con evidenza al flag "Attivo" e al tasto di modifica di contributor

Figura - Dialog di modifica del contributor

Attivare il gruppo: selezionare il flag "Attivo" per abilitare il gruppo al calcolo.

Avviare il calcolo: dopo l’attivazione, cliccare su "Calcola punteggi" per avviare l’elaborazione degli Health Index sulla base dei criteri configurati.

Figura - Schermata di dettaglio dello scoring group, con evidenza al tasto "Calcola punteggi"

Per ciascun indice è necessaria una formula dedicata. Dopo il calcolo dei singoli HI, è possibile procedere con l’aggregazione nell’HI Complessivo. All’interno del tab "Tipi di punteggio" saranno visibili i valori di ciascun HI calcolato.

Modifica HI / TdA / TdG

Nel modulo Health, per effettuare delle modifiche sull’HI, Tasso di guasto o tasso di anomalia, l’utente dove eseguire i seguenti passaggi.

Per modificare della query associata:

Accedere all’applicazione “Impostazioni di punteggio”: posizionando il cursore sulla barra laterale a sinistra, l’utente deve selezionare l’opzione “Impostazione di punteggio”. Successivamente deve selezionare il tab “Gruppi” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni del modulo Health d EAM, con evidenza all'applicazione "Impostazioni di punteggio"

Selezionare il gruppo di interesse: dalla vista elenco, l’utente deve selezionare il gruppo contenente lo score associato alla query da modificare. All’interno della schermata di dettaglio del gruppo, l’utente seleziona lo score desiderato.

Figura - Vista elenco dei gruppi di punteggio

Disattivare lo score: dopo aver selezionato lo score cliccando sul record evidenziato nella figura sotto, l’utente viene indirizzato alla schermata di dettaglio dello score, dove lo può disattivare selezionando il flag per disattivare lo score.

Figura 4 - Schermata di dettaglio del gruppo di punteggio, con evidenza allo score "HI Sostegno"

Figura - Schermata di dettaglio dello score, con evidenza al flag "Attivo"

Modificare la query associata: ornando alla schermata di dettaglio del gruppo, l’utente seleziona l’icona evidenziata in figura per aprire la dialog rappresentata nella figura sottostante. In questa dialog, l’utente deve scegliere la query preimpostata nell’applicazione “Asset” o nell’applicazione “Collocazione” nel caso di modifica del TdG e TdA.

Figura 5 - Schermata di dettaglio del gruppo di punteggio, con evidenza all'icona di associazione alla query

Figura - Dialog di selezione della query

Per modificare il contributor:

Accedere all’applicazione “Impostazioni di punteggio”: posizionando il cursore sulla barra laterale a sinistra, l’utente deve selezionare l’opzione “Impostazione di punteggio”, come dimostrato nella figura. Successivamente deve selezionare il tab “Contributori” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni del modulo Health d EAM, con evidenza all'applicazione "Impostazioni di punteggio"

Selezionare il contributor da modificare: dalla vista rappresentata nella figura sotto, l’utente deve selezionare il contributor che vuole modificare;

Figura - Vista elenco di selezione del contributor

Modificare il contributor: una volta selezionato il contributor come descritto nello step precedente, l’utente viene indirizzato alla schermata di dettaglio del contributor. Selezionando le icone di modifica, il sistema apre le dialog di modifica rappresentate nelle figure sotto, dove potrà realizzare le modifiche e salvarle.

Figura - Vista di dettaglio dei contributors, con evidenza alle icone di modifica

Figura - Dialog di modifica dei dettagli del contributor

Figura - Dialog di modifica della formula del contributor

Figura - Dialog di modifica normalizzazione valore del contributor

Nota: Nel caso in cui il contributor sia associato al calcolo di altri indicatori, qualsiasi modifica viene trasmessa agli stessi.

## 2.9 Cataloghi delle policy

Il catalogo contiene l’elenco completo delle Policy utilizzate per l’elaborazione (generazione, modifica, cambio stato) degli Ordini di Lavoro (OdL) da parte del DSS. Le Policy sono configurate dall’utente, che ne definisce gli input — ovvero le condizioni che ne determinano l’attivazione e gli output attesi. Le Policy sono suddivise in due categorie principali:

Elementare: policy basate su un singolo input;

Combinate: policy che prevedono molteplici input.

Nello specifico, è importante menzionare anche le Policy aggregate, che sono definite come delle Policy che gestiscono le casistiche per cui la generazione di attività di sostituzione non dipende strettamente dalle condizioni del singolo asset, ma dalle condizioni di tutti gli asset della stessa tipologia della linea o stazione.

Di seguito vengono descritte le procedure per l’inserimento e la valorizzazione dei campi all’interno del catalogo delle Policy

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà |
| --- | --- | --- | --- |
| ID Policy | Codice univoco assegnato alla Policy, costruito secondo le regole predefinite. | Popolato automaticamente | Obbligatorio |
| Elemento | Elemento asset al quale è associata la Policy | Pick-list | Obbligatorio |
| Classificazione asset | Classificazione al quale è associata la Policy | Tabella popolata automaticamente in base all’elemento inserito | Obbligatorio |
| Gerarchia asset associato | Gerarchia dell’asset associato alla Policy | Campo popolato automaticamente in base alla classificazione | Obbligatorio |
| Tronco d’ispezione | Flag che permette al DSS di individuare le Policy su quale applicare la logica del tronco di ispezione | Flag | Valorizzazione facoltativa |
| Livello policy | Campo corrispondente al livello anagrafico dell’asset associato | Campo popolato automaticamente in base all’elemento inserito | Obbligatorio |
| Stato | Stato in cui si trova la Policy | Pick-list | Obbligatorio |
| Tipo policy | Tipologia di Policy. Può assumere i valori “On-condition” o “periodica” | Pick-list | Obbligatorio |
| Frequenza | Campo numerico popolabile solo per le Policy con Tipo Policy uguale a “periodica”. La Frequenza permette di determinare la data modello dell’OdL generato dalla Policy ed è definita in base al riferimento temporale inserito. | Testo libero (Numerico) | Facoltativo |
| Riferimento temporale | Codice della frequenza / differibilità. | Pick-list | Obbligatorio |
| Differibilità | Campo numerico popolabile solo per le Policy con Tipo Policy uguale a “on-condition”. La Differibilità permette di determinare la data modello dell’OdL generato dalla Policy ed è definita in base al riferimento temporale inserito. | Popolato automaticamente | Facoltativo |
| ID Timing | Codice che identifica la configurazione temporale della Policy. Rappresenta la concatenazione tra il valore inserito nel campo “Frequenza” / “Differibilità” e quello inserito nel campo “Riferimento temporale”. | Popolato automaticamente | Obbligatorio |
| Massimo posticipo attività | Campo in cui l’utente specifica il numero di giorni di massimo posticipo rispetto alla “Data Modello” dell’OdL. | Popolato automaticamente | Facoltativo |
| Massimo anticipo attività | Segue logiche diverse a seconda della tipologia di policy applicata. Per le policy periodiche infra-annuali, ovvero quelle con frequenza minore o uguale a 1 anno, il campo non è valorizzabile manualmente dall’utente e pertanto rimane vuoto. In questi casi, è il sistema a determinare automaticamente la data “non prima di”, sulla base della frequenza impostata nella policy. Il calcolo avviene secondo queste regole: Se la frequenza è 1 mese, la data “non prima di” corrisponde al primo giorno del mese; Se la frequenza è 2 mesi, corrisponde al primo giorno del bimestre; Se la frequenza è 3 mesi, corrisponde al primo giorno del trimestre; E così via, mantenendo la logica di allineamento della data all’inizio del periodo relativo alla frequenza infra-annuale specificata. | Automatico | Facoltativo |
| Data inizio validità | Data a partire dalla quale la Policy passa in stato “Attiva” e viene letta dal DSS per la generazione degli OdL. È un campo imputabile dall’utente. Se non valorizzato, viene compilato automaticamente alla data del passaggio manuale in stato “Attiva”. Se l’utente inserisce una data futura ma attiva manualmente la policy prima, il campo viene sovrascritto con la data effettiva di attivazione. | Popolato manualmente o automaticamente | Facoltativo |
| Data fine validità | Data fino alla quale la Policy viene letta dal DSS per la generazione degli OdL. Non comporta la disattivazione automatica della Policy. Quando mancano 10 giorni o meno alla scadenza, la Policy viene evidenziata nella sezione “Policy in scadenza” del centro di controllo. L’utente può quindi disattivarla manualmente o prolungare la data di fine validità. Se non viene eseguita nessuna delle due azioni, la Policy rimane attiva e continua ad apparire nella sezione “Policy in scadenza”. | Testo libero (Numerico) | Facoltativo |
| A partire da | Campo compilabile solo per Policy di tipo “periodica”. Indica la data a partire dalla quale possono essere posizionati temporalmente gli OdL generati dalla Policy. La Policy viene letta immediatamente dal DSS, ma gli OdL saranno generati solo a partire dalla data indicata in questo campo. Logiche di generazione OdL: Se la data “a partire da” è inferiore o uguale a 1° gennaio + frequenza, gli OdL vengono generati normalmente (es. con frequenza 3 mesi e data “a partire da” = 15/03 → OdL emessi il 31/03, 30/06, 30/09, 31/12). Se la data “a partire da” è successiva a 1° gennaio + frequenza, vengono generati solo gli OdL successivi a tale data (es. con frequenza 3 mesi e data “a partire da” = 15/04 → OdL emessi il 30/06, 30/09, 31/12). È possibile inserire solo una data successiva a quella attuale. Una volta che la data “a partire da” è superata, essa diventa obsoleta e le logiche sopra descritte non si applicano più. Sarà quindi responsabilità dell’utente aggiornarla per mantenere attivo il comportamento desiderato. | Testo libero (Numerico) | Facoltativo |
| ID Macro-attività in output | Campo contenente il codice identificativo della macro-attività, che deve essere strettamente legata allo stesso asset a cui si riferisce la policy. Nel catalogo attività (PdL), una macro-attività può essere associata a più PdL (metodi esecutivi). In fase di generazione dell’ordine, il DSS seleziona un solo PdL seguendo questi step: Verifica l’attributo dell’asset indicato nel campo “Attributo per selezione metodo” del catalogo attività. Confronta il valore dell’attributo con quello indicato nel campo “valore target”. Se esistono più PdL validi per lo stesso valore target, viene scelto quello con il flag “Attività prevalente per metodo”. Se il valore dell’attributo dell’asset non corrisponde a nessun valore target, viene selezionata l’attività con il flag “Attività prevalente per metodo” e con i campi “Attributo per selezione metodo” e “valore target” non valorizzati. | Testo libero (Numerico) | Obbligatorio |
| N° input | Identificativo progressivo generato automaticamente dal sistema. | Automatico | Obbligatorio |
| Tipologia input | Selezionata da elenco valori, identifica la natura dell’input (es. Health Index, Tassi di guasto, Attributi anagrafici, Difformità, Misure, Variabili di stato, Attività PdL, Interferenze, Opere interferenti). | Pick-list | Obbligatorio |
| ID Input | Codice identificativo selezionato da lista filtrata in base alla tipologia. | Pick-list | Obbligatorio |
| Descrizione Input | Descrizione testuale dell’input. | Automatico in base all’input selezionato | Obbligatorio |
| Misura | Codice selezionabile da elenco; se non associata all’ID Input, il campo è in sola lettura. | Pick-list | Facoltativo |
| Operatore/funzione: | Operatore logico scelto da lista (>, >=, <, =, ≠, EXIST, NOT EXIST, ecc.). | Pick-list | Obbligatorio |
| Target di raffronto numerico | Valore numerico inserito liberamente; è mutuamente esclusivo con il campo “Target di raffronto”. | Pick-list | Facoltativo |
| Target di raffronto | Valore selezionabile da lista filtrata in base alla Tipologia. | Pick-list | Facoltativo |
| Livello di combinazione input | Campo popolato manualmente che determina il numero di combinazioni logiche tra gli input. Il campo può assumere i valori 0, 1, 2 o 3. | Testo libero (Numerico) | Obbligatorio |
| Policy aggregata | Flag utilizzato per indicare che la policy è di tipo “aggregata”. Visibile esclusivamente per le policy aggregate in sola visualizzazione. | Flag | Facoltativo |
| Livello di aggregazione | È in sola visualizzazione e riporta esclusivamente per le policy aggregate il livello di aggregazione sotto il quale vengono generati gli ordini. | Pick-list | Facoltativo |
| Sotto-programma | Il campo identifica le specifiche campagne di manutenzione a cui può essere associata una Policy, in base al tipo di asset (linee o stazioni). Viene valorizzato tramite picklist, selezionando uno dei sotto-programmi ammissibili. | Pick-list | Facoltativo |
| Cluster | Campo popolato manualmente dall’utente tramite una lista valori definita a sistema. | Piclk-list | Facoltativo |
| Eleggibilità | Flag utilizzato per identificare l’eleggibilità dell’attività. Se selezionato nella policy, tutti gli OdL generati avranno automaticamente questo flag attivato. | Flag | Facoltativo |
| Combinazione input | Campo che riporta la rappresentazione della combinazione degli input definita tramite maschera. | Testo libero | Facoltativo |

Tabella - Campi facoltativi e obbligatori del catalogo delle Policy

### 2.9.1 Creazione e attivazione policy

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per visualizzare il dettaglio di una Policy e configurarne i parametri, l’utente deve eseguire i passaggi di seguito descritti:

Accedere all’applicazione dedicata alla gestione delle Policy: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Catalogo Policy”. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo Policy” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo delle Policy"

Cliccare su “Nuova Policy”: selezionare l’opzione “Nuova Policy” evidenziata a sinistra nella Figura sotto, o in alternativa, cliccare sul pulsante “+” in alto a destra.

Figura - Vista iniziale del catalogo delle policy, con evidenza alle azioni di aggiunta di nuova policy

Compilare i campi presenti in maschera: l’utente deve compilare i campi obbligatori e facoltativi sulla schermata di creazione della Policy. Una volta completata la compilazione, l’utente deve cliccare sul pulsante “Salva” situato in alto a destra nella maschera. Il sistema verifica la correttezza dei dati inseriti e, in assenza di errori, procede al salvataggio. Il campo “Livello combinazione input” definisce se la policy è “Elementare” o “Combinata”, mentre la tabella “Input Policy” contiene tutti gli input che impatteranno la policy.

Figura - Vista di dettaglio della Policy a catalogo, con evidenza all'icona di salvataggio

Salvare la policy: una volta compilati i campi secondo quanto descritto nello step precedente, l’utente può procedere al salvataggio della policy. Al primo salvataggio, il sistema genera i record ID Policy ed ID Timing. Se le informazioni inserite sono coerenti e il salvataggio va a buon fine, il sistema mostra un messaggio di conferma ed effettua l’operazione. Se invece viene verificato un errore (ad esempio per la mancanza di un campo obbligatorio o per la presenza di una configurazione non valida), il sistema blocca l’operazione e segnala all’utente l’elemento da correggere.

Nota: Nei capitoli successivi verranno descritte le particolarità delle Policy elementari, combinate e aggregate durante le loro generazione.

Policy elementari

Le Policy elementari sono definite da un singolo input associato ad un singolo target di raffronto. Per impostare una Policy elementare, l’utente di EAM deve eseguire i seguenti step:

Impostare il livello di combinazione: dopo aver eseguito i primi due step descritti nella sezione 9.1, l’utente deve valorizzare il campo “Livello combinazione” con il valore “0” all’interno della schermata di dettaglio della Policy. In questo modo, viene definito che gli input della Policy non saranno associabili tra loro tramite l’uso di combinazioni logiche tra più input e viene disabilitata automaticamente la visualizzazione della tabella di combinazione logica.

Figura - Dialog di selezione del Livello di combinazione

Aggiungere un input: dopo aver popolato il campo “Livello combinazione Input” il sistema rende disponibile la tabella “Input Policy”. In questa sezione, l’utente deve cliccare sul pulsante “+”, per aggiungere un nuovo input alla Policy.

Figura - Schermata di dettaglio della Policy, con evidenza al tasto di aggiunta della riga di input

Compilare i campi richiesti per l’input: l’utente deve valorizzare i campi presente nella tabella “Input Policy”. I campi sono:

N° Input

Tipologia

ID Input

Descrizione Input

Misura

Operatore / Funzione

Data

Target di raffronto

Target di raffronto numerico

Nota: I campi “Data”, “Target di raffronto” e “Target di raffronto numerico” sono esclusivi, ovvero il sistema non permette che più di uno sia valorizzato. Inoltre, il sistema consente l’inserimento di un solo input per ciascuna Policy elementare ed in caso di tentativo di aggiunta di più righe, l’utente viene bloccato e notificato dal sistema.

Policy combinata (1, 2 o 3 livelli)

Le Policy combinate sono definite dalle combinazioni logiche tra gli input e possono essere suddivise in due categorie distinte:

Policy con input unico e target di raffronto multiplo;

Policy con input multiplo e target di raffronto multiplo;

Per impostare una Policy combinata l’utente di EAM deve eseguire i seguenti step:

Impostare il livello di combinazione: dopo aver eseguito i primi due step descritti nella sezione 9.1, l’utente deve valorizzare il campo “Livello combinazione” con uno il valore 1, 2 o 3, a seconda della complessità logica della Policy da configurare:

Livello 1: per Policy con una singola combinazione input da configurare. Gli N input vengono valutati in combinazione tra loro secondo la logica indicata nella prima tabella

Livello 2: per Policy con due combinazioni input. Gli N input possono essere combinati tramite due logiche differenti nelle due tabelle

Livello 3: per Policy con tre combinazioni input. Gli N input possono essere combinati tramite tre differenti logiche nelle tre tabelle mostrate

Inserire gli input: cliccare sul pulsante “+” nella sezione “Input” per aggiungere i parametri da utilizzare nella Policy. Il numero di input è variabile in base al livello di combinazione selezionato e per ciascun input è necessario compilare i seguenti campi:

N° Input;

Tipologia;

ID Input;

Descrizione Input;

Misura;

Operatore/Funzione;

Data;

Target di raffronto;

Target di raffronto numerico;

Definire le logiche di combinazione: dopo aver definito gli input all’interno della tabella dedicata, l’utente deve definire le combinazioni logiche degli input che definiscono le Policy combinate. In base al livello di combinazione selezionato (1, 2, o 3), il sistema genera automaticamente le tabelle necessarie per la definizione delle relative combinazioni logiche.

Livello 1: in questa casistica è possibile impostare le combinazioni logiche “OR”, “AND”, “=” e “<>”. L’utente, deve selezionare il tasto “+” evidenziato per inserire la riga dove saranno costruite delle logiche di combinazione tra gli input. Successivamente, cliccando sulla lente di ingrandimento della colonna “ID Input” deve indicare il primo input coinvolto nell’operazione. Dopodiché, selezionando la lente ingrandimento sotto la logica desiderata (colonne AND, OR, = o <>), l’utente è in grado di scegliere il secondo input considerato nella logica, tramite la dialog.

Figura - Tabella contenente gli Input di una Policy e tabella contenente la combinazione logica di primo livello, con evidenza al tasto di aggiunta di logica

Figura - Dialog di selezione dell'Input da inserire sotto l’operatore logico desiderato

Dopo aver popolato la tabella di input e di combinazione logica, l’utente deve procedere al salvataggio della Policy tramite l’icona in alto a destra nella schermata.

Livello 2: in questa casistica è possibile impostare le combinazioni logiche “OR”, “AND”, “=” e “<>”. L’utente, deve selezionare il tasto “+” evidenziato per inserire la riga dove saranno costruite delle logiche di combinazione di secondo livello tra gli input. Successivamente, deve indicare il primo Input cliccando sulla lente di ingrandimento nel campo “ID Input” e scegliere sotto quale operatore logiche inserire il secondo Input (dalla Figura 141 AND, OR, = e <>, rispettivamente), cliccando ugualmente sulla lente del campo desiderato e selezionando l’input dalla dialog. È da evidenziare che nel caso di combinazione logica a due livelli, è possibile utilizzare l’input presente nella tabella di combinazione di primo livello come input per tabella di combinazione di secondo livello.

Figura - Tabella contenente gli Input di una Policy e tabelle contenenti le combinazioni logiche di primo e secondo livello, con evidenza al tasto di aggiunta di logica

Figura - Dialog di selezione dell'Input da inserire sotto l’operatore logico desiderato

Livello 3: in questa casistica è possibile impostare le combinazioni logiche “OR”, “AND”, “=” e “<>”. L’utente, deve selezionare il tasto “+” evidenziato per inserire la riga dove saranno costruite delle logiche di combinazione tra gli input. Successivamente, deve indicare il primo Input cliccando sulla lente di ingrandimento nel campo “ID Input” e scegliere sotto quale operatore logiche inserire il secondo Input (AND, OR, = e <>, rispettivamente), cliccando ugualmente sulla lente del campo desiderato e selezionando l’input dalla dialog. È da evidenziare che nel caso di combinazione logica a tre livelli, è possibile utilizzare gli input presenti nelle tabelle di combinazione di primo e secondo livello come input per tabella di combinazione di terzo livello.

Figura - Schermata di dettaglio della Policy con visibilità della tabella contenente gli Input e le tabelle contenenti le combinazioni logiche di primo, secondo e terzo livello

Figura - Dialog di selezione dell'Input da inserire sotto l’operatore logico desiderato

Policy aggregate (linee)

Le Policy di aggregazione vengono utilizzate nei casi in cui la generazione di attività manutentive non dipende esclusivamente dalle condizioni di un singolo asset, ma da parametri calcolati sull’intero insieme di asset appartenenti a una linea aerea o a una stazione. Le Policy aggregate sono preconfigurate a sistema e già disponibili nel catalogo. In queste casistiche, l’unica operazione consentita all’utente riguarda l’aggiornamento del target numerico con cui viene confrontato il coefficiente di eleggibilità (K) in input alla Policy.

Per eseguire tale modifica, l’utente deve seguire i seguenti step:

Accedere all’applicazione “Catalogo policy”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare la sezione “Amministrazione” e successivamente l’opzione “Catalogo policy”. In alternativa, è possibile digitare “Catalogo policy” nel campo di ricerca per accedere direttamente all’applicazione.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo delle Policy"

Accedere alla dialog di modifica di target: All’interno dell’applicazione “Catalogo policy”, selezionare l’azione “S eleggibilità linee” dal menu laterale.

Figura 6 - Vista elenco delle Policy aggregate, con evidenza all’azione "S eleggibilità linee"

Effettuare le modifiche necessarie: All’interno della dialog contenente le soglie di eleggibilità, l’utente può modificare in modo libero il valore presente nella colonna “Target di soglia” per ciascuna tipologia di asset. Dopo aver modificato la soglia, è necessario cliccare sul tasto “OK” per confermare l’operazione.

Figura 7 - Dialog di modifica del campo " Target soglia" per Policy di stazione

### 2.9.2 Modifica policy

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Per modificare una Policy già presente nel catalogo, l’utente di EAM deve seguire i seguenti step:

Accedere all’applicazione “Catalogo policy”: posizionare il cursore sulla barra laterale sinistra per visualizzare le sezioni del sistema. L’utente deve selezionare “Amministrazione” e poi “Catalogo policy”. In alternativa, è possibile cercare “Catalogo policy” tramite il campo di ricerca per accedere direttamente all’applicazione.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo delle Policy"

Selezionare la Policy da modificare: nella vista di elenco delle Policy, individuare e cliccare sul record desiderato per aprire la maschera di dettaglio. In questa vista, sono selezionabili siano i record presenti nella colonna “Tipo Policy” che quelli della colonna “ID Policy”

Figura - Vista elenco delle Policy presenti nel catalogo

Impostare lo stato in “Pending review”: prima di procedere alla modifica delle informazioni associate a ciascuna Policy, è necessario portarla in uno stato idoneo alle modifiche. Selezionando l’azione laterale “Revisione policy”, il sistema modifica automaticamente lo stato della Policy in Pending review (PNDRV). Una volta che la Policy assume lo stato “Pending Review”, il relativo record viene visualizzato nel tab “Policy in stato Pending Review” del Centro di Controllo, descritto in 10.2.

Figura - Schermata di dettaglio delle Policy, con evidenza all'azione "Revisione Policy"

Effettuare le modifiche necessarie: una volta messa in stato “Pending review”, l’utente può modificare tutti i campi che desideri, a eccezione dei campi “ID Policy”, “Elemento asset”, “Tipo policy”, “Livello di aggregazione” e “Livello policy”, così come i flag “Policy aggregata?” e “Eleggibilità?”

Confermare la modifica: una volta effettuate le modifiche, cliccare sul pulsante “Salva” presente in alto a destra. Il sistema verifica la validità dei dati inseriti e aggiorna la Policy. Alla conferma, viene visualizzato un messaggio di esito positivo dell’operazione. In caso di errori o campi mancanti, l’utente riceverà una notifica con le informazioni da correggere. È da notare che il salvataggio della Policy dopo la modifica non avrà impatto sullo stato della Policy, che rimane in “Pending review” finché la Policy venga riattivata o disattivata.

Figura 8 - Schermata di dettaglio delle Policy, con evidenza all'icona di salvataggio"

### 2.9.3 Disattivazione policy

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. Il passaggio della Policy allo stato “Non attiva” deve essere sempre eseguito manualmente dall’utente abilitato. Per disattivare una Policy nel catalogo, l’utente deve seguire i seguenti step:

Accedere all’applicazione “Catalogo policy”: posizionando il cursore sulla barra laterale sinistra per visualizzare le sezioni del sistema. L’utente deve selezionare “Amministrazione” e poi “Catalogo policy”. In alternativa, può cercare “Catalogo policy” tramite il campo di ricerca per accedere direttamente.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo delle Policy"

Selezionare la Policy da disattivare: nella vista elenco delle Policy, individuare e cliccare sul record da disattivare per aprire la maschera di dettaglio. In questa vista, sono selezionabili siano i record presenti nella colonna “Tipo Policy” che quelli della colonna “ID Policy”.

Figura - Vista elenco delle Policy presenti nel catalogo

Impostare lo stato in “Non attivo”: all’interno della schermata di dettaglio, l’utente deve selezionare il pulsante “Cambia Stato” e nella dialog, selezionare lo stato “Non attiva”.

Figura - Schermata di dettaglio delle Policy, con evidenza all'azione "Cambio stato policy"

Figura - Dialog di selezione di nuovo stato

Confermare l’operazione: il sistema richiederà la conferma dell’azione. L’utente deve selezionare “OK” per confermare la disattivazione. A conferma avvenuta, la Policy passerà allo stato “Non attiva” e non sarà più considerata dal DSS per la generazione di nuovi OdL. In caso di errori (es. Policy già utilizzata in OdL attivi), verrà restituito un messaggio di errore con le istruzioni per il recupero.

## 2.10 Centro di controllo per le policy

Il Centro di controllo permette di individuare rapidamente delle incongruenze dovute all’influenza delle Policy in relazione ai diversi oggetti previsti in EAM (ad esempio difformità, interferenze, PdL, ecc). Nello specifico, questa funzionalità è pensata per supportare l’utente nelle attività di verifica, rinnovo o disattivazione delle Policy, garantendo una gestione proattiva e puntuale all’interno del catalogo.

La sezione del centro di controllo è abilitata esclusivamente agli utenti con profilo P1, secondo le autorizzazioni di sistema e, per farne uso, deve essere seguita la seguente procedura:

Accedere all’applicazione dedicata al centro di controllo: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Centro di controllo”. In alternativa, è possibile inserire nel campo di ricerca l’opzione “Centro di controllo” per essere indirizzati all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Centro di controllo"

Selezionare la sezione desiderata del centro di controllo: dopo aver selezionato “Centro di controllo”, l’utente deve cliccare sul tab “Monitoraggio Policy” per accedere alla sezione dedicata.

Figura Centro di controllo di EAM, con evidenza alla sezione "Monitoraggio difformità/interferenza"

Nei paragrafi successivi sono approfondite le diverse tabelle del centro di controllo riferite alle incongruenze dei cataloghi.

### 2.10.1 Visualizzazione Policy in stato Bozza con data validità inferiore alla data odierna

In EAM è presente una sezione nella quale è possibile monitorare le policy che, nonostante abbiano raggiunto la data di inizio validità, non sono passate in stato “Attiva”.

Policy in stato bozza con data inizio validità <= data corrente: in questa sezione sono presenti delle Policy con data di inizio validità precedente alla data odierna ma che, a causa di mancata valorizzazione di alcuni campi obbligatori al passaggio in stato “Attivo”, rimangono in stato “Bozza”.

Le informazioni relative alle “Policy in stato bozza con data inizio validità <= data corrente” sono di seguito riportate:

ID Policy

Tipo policy

Riferimento asset

Data inizio validità

All’interno delle tabelle, l’utente ha la possibilità di svolgere le seguenti operazioni:

Filtrare la ricerca: selezionando l’icona del filtro evidenziata nella Figura 158 è possibile inserire il parametro desiderato in uno qualsiasi dei campi disponibili.

Selezionare il record: cliccando su un record presente in ciascuna colonna, l’utente viene automaticamente indirizzato alla corrispondente Policy all’interno del catalogo delle Policy. All’interno del catalogo, l’utente può effettuare ulteriori modifiche sulla Policy, come ad esempio, modificare lo stato.

La Figura sottostante rappresenta la sezione del centro di controllo appena descritta.

Figura 9 - Sezione del centro di controllo "Policy in stato Bozza con data inizio validità <= data corrente", con evidenza al filtro di ricerca

### 2.10.2 Visualizzazione Policy modificate nel centro di controllo

La modifica di una Policy può avere impatto su diversi elementi a lei associate, come OdL, difformità, ecc. Per monitorare e gestire questi impatti dovuti alla modifica di una Policy, sono presente nel Centro di controllo le seguenti viste:

Policy in stato Pending review: la policy viene riportata in questa sezione qualora il suo stato sia “Pending review”.

OdL legati a policy riattivate negli ultimi 10 giorni: se l’utente decide di attivare la Policy, deve modificare manualmente il suo stato all’interno dell’applicazione “Catalogo policy”, impostandolo su “Attiva”. Questa azione comporta la loro comparsa nel tab “OdL impattati da attivazione Policy”. All’interno di questa tabella, l’utente può consultare gli effetti dell’attivazione, accedendo ai dettagli degli OdL associati alla Policy attivata. I record rimangono visibili nel Centro di Controllo fino a 10 giorni dopo l’attivazione.

OdL relazionati a Policy in stato Pending review: la sezione riporta gli OdL associati a Policy in stato “Pending review” e che potrebbero essere impattati dalle loro attivazioni.

Le informazioni relative alle “Policy in stato Pending review” sono riportate nelle seguenti colonne precompilate:

ID Policy

Modificato da

ID Macro-attività in output

Tipo Policy

Riferimento asset

Data entrata in “Pending review”

Le informazioni relative alla sezione “OdL relazionati a Policy in stato Pending review”, e “OdL legati a policy riattivate negli ultimi 10 giorni” sono riportate nelle seguenti colonne precompilate:

ID OdL

ID Policy

Riferimento Asset

Responsabile attività

ID PdL

Data inizio pianificata

Data fine pianificata

Stato

All’interno delle tabelle, l’utente ha la possibilità di svolgere le seguenti operazioni:

Filtrare la ricerca: selezionando l’icona del filtro evidenziata nella figura è possibile inserire il parametro desiderato in uno qualsiasi dei campi disponibili.

Selezionare il record: cliccando su un record presente in ciascuna colonna, l’utente viene automaticamente indirizzato al corrispondente record all’interno del catalogo di interesse. All’interno del catalogo, l’utente può effettuare ulteriori modifiche sull’oggetto selezionato.

La figure sottostanti rappresentano le sezioni del centro di controllo appena descritte.

Figura - Sezione del centro di controllo " Policy in stato Pending review ", con evidenza al filtro di ricerca

Figura - Sezione del centro di controllo "OdL relazionati a Policy in stato Pending review", con evidenza al filtro di ricerca

Figura - Sezione del centro di controllo "OdL legati a policy riattivate negli ultimi 10 giorni", con evidenza al filtro di ricerca

### 2.10.3 Visualizzazione Policy disattivate nel CdC

La disattivazione di una Policy (azione sempre eseguita manualmente) può avere impatto su diversi elementi a lei associate. Per monitorare e gestire questi impatti dovuti alla disattivazione di una Policy, sono presente nel Centro di controllo le seguenti viste:

OdL associati a policy disattivate negli ultimi 10 giorni: Dopo aver portato la Policy in stato “Non attiva”, l’utente può consultare gli OdL relazionati alla Policy disattivata accedendo a questa sezione del Centro di Controllo. Gli OdL visualizzati sono quelli impattati dalla disattivazione e rimangono disponibili per un periodo massimo di 10 giorni. Se l’utente desidera visualizzare OdL associati a Policy disattivate da più di 10 giorni, deve modificare le “Available queries” all’interno dell’applicazione di gestione degli ordini di lavoro.

Le informazioni relative agli OdL associati a policy disattivate negli ultimi 10 giorni sono riportate nelle seguenti colonne precompilate:

ID OdL

ID Policy

Riferimento Asset

Responsabile attività

ID PdL

Data inizio validità

Data fine validità

Stato

All’interno della tabella, l’utente ha la possibilità di svolgere le seguenti operazioni:

Filtrare la ricerca: selezionando l’icona del filtro evidenziata nella Figura 162 è possibile inserire il parametro desiderato in uno qualsiasi dei campi disponibili.

Selezionare il record: cliccando su un record presente in ciascuna colonna, l’utente viene automaticamente indirizzato al catalogo di interesse dove potrà risolvere l’incongruenza verificata.

La figura sotto rappresenta la sezione del centro di controllo appena descritta.

Figura - Sezione del centro di controllo "OdL associati a policy disattivate negli ultimi 10 giorni", con evidenza al filtro di ricerca

### Consultazioni CdC per policy in scadenza/scadute

Questo paragrafo è riferito alla definizione della sezione del centro di controllo dedicata a evidenziare le policy in scadenza o scadute.

Policy in scadenza / scadute: in questa sezione sono presenti le Policy per le quali la differenza tra la data di scadenza e data odierna è minore di 10 giorni o per le quali la data odierna ha superato la data di scadenza.

Le informazioni relative a Policy in scadenza o scadute sono riportate nelle seguenti colonne precompilate:

ID Policy

Data fine validità

ID PdL in output

Descrizione PdL in output

Tipo policy

Riferimento asset

All’interno della tabella, l’utente ha la possibilità di svolgere le seguenti operazioni:

Filtrare la ricerca: selezionando l’icona del filtro è possibile inserire il parametro desiderato in uno qualsiasi dei campi disponibili.

Selezionare il record: cliccando su un record presente in ciascuna colonna, l’utente viene automaticamente indirizzato alla corrispondente Policy all’interno del catalogo delle Policy. All’interno del catalogo, l’utente può effettuare ulteriori modifiche sulla Policy, come ad esempio, modificare lo stato in “Non attiva” o modificare la data fine validità.

A seguito del cambio di stato in “Non Attiva” o della modifica della data di fine validità, il relativo record non sarà più visibile nella sezione “Policy in scadenza / scadute” del Centro di Controllo. Se l’utente non interviene manualmente e la data odierna supera la data di fine validità, la Policy rimane in stato “Attiva” e continua ad essere letta dal DSS. Il sistema non effettua disattivazioni automatiche, ma evidenzia il record in rosso nel Centro di Controllo.

Nota: La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema. La figura sottostante rappresenta la sezione del centro di controllo inerente alle Policy in scadenza / scadute.

Figura - Sezione del centro di controllo "Policy in scadenza / scadute", con evidenza al filtro di ricerca

## 2.11 Gestione del catalogo attività

Nel modello Terna, il Piano di Lavoro (PdL) rappresenta l’elemento centrale del processo di manutenzione gestito in EAM. Questo elemento è definito da attività di monitoraggio, sostituzione e manutenzione. Ogni PdL è composto da una macro-attività associata a un workflow, ovvero una sequenza di metodi. I metodi sono definiti da specifici attributi che ne descrivono i dettagli operativi, come le risorse, le attrezzature necessarie e la durata prevista per la loro esecuzione. Nella soluzione adottata, i concetti di macro-attività e attività vengono unificati nel PdL, semplificando la gestione operativa all’interno della piattaforma EAM. La gestione dei PdL all’interno della piattaforma EAM è attribuita agli utenti con profilo P1, secondo le autorizzazioni di sistema ed è effettuata in modo particolare a seconda delle seguenti tipologie previste:

Controllo periodico

Attività non regolamentata (ANR)

Manutenzione su condizione

Nuove costruzioni

Controllo su condizione

Sostituzione

Controllo occasionale

Controlli di legge

Demolizione

Attività non operative

Pronto intervento

Le informazioni che caratterizzano le difformità all’interno del catalogo cambiano a seconda della tipologia che si sta considerando. Nella seguente tabella è possibile verificare, per ciascuna tipologia, se l’informazione è obbligatoria (O), facoltativa (F) o non presente (NP)

| Nome campo | Controllo periodico | ANR | Manutenzione su condizione | Nuove costruzioni | Controllo su condizione |
| --- | --- | --- | --- | --- | --- |
| Elemento anagrafico associato | O | O | O | O | O |
| Tabella di associazione classificazione | O | NP | O | O | O |
| Gerarchia anagrafica associata | O | NP | O | O | O |
| Unità di misura | O | F | O | O | O |
| Assieme e sottoassieme | F | F | F | F | F |
| Livello attività | O | O | O | O | O |
| ID attività | O | O | O | O | O |
| Descrizione attività | O | O | O | O | O |
| ID macro-attività | O | O | O | O | O |
| Attività prevalente per il metodo | F | F | F | F | F |
| Attributo per selezione metodo | F | F | F | F | F |
| Valore target attributo | F | F | F | F | F |
| Tipologia attività | O | O | O | O | O |
| Indisponibilità | F | NP | F | F | F |
| Durata pianificata | O | O | O | O | O |
| Effort | F | F | F | F | F |
| Difformità/interferenze risolte | NP | NP | F | NP | NP |
| Difformità/interferenze rilevabili | F | NP | NP | NP | F |
| Max tempo validazione (giorni) | NP | O | O | O | O |
| Attività prevalenti | NP | NP | F | NP | F |
| Programma | F | F | F | F | F |
| Att. Caricabile manualmente | F | F | F | F | F |
| ID attività standard | O | O | O | NP | O |
| Descrizione attività standard | O | O | O | NP | O |
| Normativa di riferimento | NP | NP | NP | NP | NP |
| Note | F | NP | F | F | F |
| Micro-controlli | F | NP | NP | NP | F |
| Metodi | F | NP | F | F | F |
| Elive? | F | NP | F | F | F |
| Outsourcing? | F | F | F | F | F |
| Risorse | F | F | F | F | F |
| Risorse esterne | F | F | F | F | F |
| KPI | F | F | F | F | F |
| Tolleranza | F | F | F | F | F |
| Stato | O | O | O | O | O |
| Revisione | O | O | O | O | O |
| Categoria responsabile attività | O | O | O | O | O |

Tabella - informazioni previste per ciascuna tipologia di attività, dove O = Obbligatorio, NP = Non presente, F = Facoltativo

Nell’ambito della gestione dei PdL all’interno dello specifico catalogo, gli utenti EAM hanno sia la possibilità di generare nuovi PdL, così come di modificare o disattivare quelli già esistenti. Nei paragrafi successivi vengono dimostrate in che modo le operazioni di generazione, modifica e disattivazione di PdL devono essere eseguite in EAM, suddivise in base alla tipologia di attività sulla quale si sta lavorando.

### 2.11.1 Generazione di nuovo PdL

La generazione di nuovi Ordini di Lavoro (OdL) all’interno della piattaforma consente di pianificare, eseguire e tracciare attività manutentive o gestionali in modo strutturato ed efficiente. Il processo include la creazione, eventuale modifica e, se necessario, la disattivazione delle attività associate all’OdL.

In conformità con le autorizzazioni previste dal sistema, esclusivamente gli utenti con profilo P1 sono abilitati all’esecuzione di tali operazioni.

Generazione attività di tipo controllo periodico, controllo su condizione e ANR

Le attività di controllo periodico consentono di pianificare e monitorare in modo strutturato l’esecuzione dei micro-controlli associati alle attività manutentive all’integrazione tra il catalogo delle attività e quello dei micro-controlli. Per inserire un’attività di tipo controllo periodico nel catalogo delle attività è necessario seguire i seguenti step:

Accedere all’applicazione “Catalogo Attività”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Pianificazione” e successivamente l’opzione “Catalogo attività”. In alternativa, può inserire nel campo di ricerca la voce “Catalogo attività” per essere indirizzato direttamente all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo attività"

Cliccare su “Nuovo PdL”: selezionare l’opzione “Nuovo PdL” evidenziata a sinistra nell’interfaccia oppure cliccare sul pulsante “+” in alto a destra per generare un nuovo record.

Figura - Vista iniziale del catalogo delle attività, con evidenza alle azioni di aggiunta di nuova attività

Compilare i campi obbligatori e facoltativi in maschera: una volta aperta la maschera di creazione dell’ordine, è necessario definire all’interno dei tab le caratteristiche dell’attività, popolando i campi facoltativi e obbligatori descritti nella Tabella 7, distribuiti all’interno dei seguenti tab:

Attività;

Dettaglio attività;

Date ed effort;

Rilevazioni;

Micro-controlli;

Figura - Schermata di dettaglio dell'attività in processo di creazione

Per poter generare l’attività, è necessario compilare almeno i campi obbligatori, contrassegnati da un asterisco rosso. Una volta popolato il campo “ID Macro-attività” il sistema genera in modo automatico l’ID Attività. È pure in questa fase della generazione di una attività che viene definita la sua tipologia. Selezionando la freccia all’interno del campo “Tipologia Attività” e successivamente l’opzione “Classificazione”, l’utente deve selezionare dalla dialog una tra le seguenti opzioni: Controllo periodico; ANR; Controllo su condizione.

Figura- Dialog di selezione della tipologia di attività

Associare il micro-controllo: nel caso in cui sia necessario associare dei micro-controlli al PdL che si sta generando, l’utente deve prima accedere alla sezione dedicata all’associazione dei micro-controlli, evidenziata nella Figura. Successivamente, è necessario cliccare sul pulsante “+”, evidenziato nell’immagine, per selezionare da una lista il micro-controllo desiderato tra quelli disponibili. Per accedere alla lista di micro-controlli, l’utente deve cliccare sulla lente.

Figura - Vista di dettaglio del tab "Micro controlli", con evidenza alle azioni di aggiunta di nuovo micro-controllo

Figura - Tabella di selezione del nuovo micro-controllo

Associare difformità/interferenza rilevabili: per attività di tipologia controllo periodico e controllo su condizione è obbligatoria l’associazione di almeno una difformità o interferenza rilevabile per poter impostare lo stato dell’attività in “Attivo”. Nello specifico, l’utente deve selezionare il tab “Rilevazioni”, e cliccare sul tasto “+” per aggiungere l’interferenza.

Figura - Schermata di dettaglio del Tab "Rilevazioni", con evidenza alle azioni di associazione di difformità e interferenza rilevabile dal PdL

Impostare lo stato del PdL: successivamente all’inserimento delle informazioni relative all’attività, è necessario impostare lo stato della stessa. Di default, quando generato, il PdL si trova in stato “Bozza”. Per attivare l’attività, è necessario selezionare l’azione laterale “Modifica stato”, evidenziata in figura e, successivamente, modificare lo stato tramite il campo “Nuovo stato”.

Figura - Vista di dettaglio dell'attività, con evidenza all'azione di modifica stato

Figura- Dialog di modifica stato

Alla modifica di stato, il sistema salva automaticamente la nuova attività e la stessa sarà disponibile per essere associata agli nuovi OdL.

Generazione attività di tipo manutenzione su condizione (Task 9) e Nuove costruzione/istallazione (Task 10)

Per inserire un’attività di tipo manutenzione su condizione o nuova costruzione / istallazione nel catalogo delle attività è necessario eseguire i seguenti step:

Accedere all’applicazione “Catalogo Attività”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Pianificazione” e successivamente l’opzione “Catalogo attività”. In alternativa, può inserire nel campo di ricerca la voce “Catalogo attività” per essere indirizzato direttamente all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo attività"

Cliccare su “Nuovo PdL”: selezionare l’opzione “Nuovo PdL” evidenziata a sinistra nell’interfaccia oppure cliccare sul pulsante “+” in alto a destra per generare un nuovo record.

Figura - Vista iniziale del catalogo delle attività, con evidenza alle azioni di aggiunta di nuova attività

Compilare i campi obbligatori e facoltativi in maschera: una volta aperta la maschera di creazione dell’ordine, è necessario definire all’interno dei tab le caratteristiche dell’attività, popolando i campi facoltativi e obbligatori descritti nella Tabella 7Tabella 7 - informazioni previste per ciascuna tipologia di attività, dove O = Obbligatorio, NP = Non presente, F = Facoltativo, distribuiti all’interno dei seguenti tab:

Attività

Dettaglio attività

Date ed effort

Figura - Schermata di dettaglio dell'attività in processo di creazione

Per poter generare l’attività, è necessario compilare almeno i campi obbligatori, contrassegnati da un asterisco rosso. Una volta valorizzato il campo “ID Macro-Attività” il sistema genera automaticamente l’ID Attività. È pure in questa fase della generazione di una attività che viene definita la sua tipologia. Selezionando la freccia all’interno del campo “Tipologia Attività” e successivamente l’opzione “Classificazione”, l’utente deve selezionare dalla dialog una tra le seguenti opzioni: Manutenzione su condizione; Nuova costruzione / istallazione. Una volta selezionata la tipologia, l’elenco di tab presenti nella schermata contenenti le informazioni del PdL sarà aggiustata.

Figura - Dialog di selezione della tipologia di attività

Impostare lo stato del PdL: successivamente all’inserimento delle informazioni relative all’attività, è necessario impostare lo stato della stessa. Di default, quando generata, il PdL si trova in stato “Bozza” e, selezionando l’azione laterale “Modifica stato”, evidenziata in figura e, successivamente, modificare lo stato tramite il campo “Nuovo stato”.

Figura - Vista di dettaglio dell'attività, con evidenza all'azione di modifica stato

Figura - Dialog di modifica di stato

Alla modifica di stato, il sistema salva automaticamente la nuova attività e la stessa sarà disponibile per essere associata agli nuovi OdL.

### 2.11.2 Modifica attività e propagazione su OdL

Per effettuare delle modifiche su un PdL già presente a catalogo e propagarle sugli OdL impattati, l’utente deve eseguire i seguenti passaggi:

Accedere all’applicazione “Catalogo attività”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili, suddivise per ambito. L’utente deve selezionare l’opzione “Pianificazione” e successivamente “Catalogo attività”. In alternativa, è possibile digitare “Catalogo attività” nel campo di ricerca per accedere rapidamente all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo attività"

Selezionare l’attività da modificare: dalla vista elenco, l’utente può individuare il PdL di interesse e selezionarlo cliccando sul record presente nella colonna “Attività”. Selezionando l’icona filtro, è possibile applicare criteri di ricerca specifici sulle colonne disponibili (Attività, Descrizione, Revisione del PdL, Tipologia attività, Tipologia asset, Stato).

Figura - Vista elenco iniziale del catalogo delle attività, con evidenza al filtro di ricerca

Modificare lo stato del PdL: nel caso in cui il PdL da modificare non sia in uno stato idoneo alla modifica (“Bozza” o “Pending review”), prima di procedere alla modifica dei campi di interesse, è necessario impostare lo stato dell’attività. Selezionando l’azione “Rivedi piano di lavoro”, il sistema apre la dialog rappresentata in Figura, nella quale l’utente inserisce la descrizione della revisione in corso e clicca sul tasto “OK” per impostare lo stato del PdL.

Figura - Schermata di dettaglio del PdL, con evidenza alla azione revisione del piano di lavoro

Figura - Dialog di revisione del piano di lavoro, con evidenza al campo contenente la descrizione richiesta

Effettuare le modifiche: all’interno della maschera di dettaglio dell’attività è possibile modificare tutte le informazioni associate al PdL, con eccezioni dei seguenti campi:

Tipologia attività

Elemento anagrafico associato

ID Macro-attività

ID Attività standard

Attivare il PdL: una volta apportate le modifiche desiderate, è necessario riattivare il PdL affinché lo stesso possa essere letto dal DSS per la generazione di OdL o renderlo disponibile per la generazione manuale di OdL. Cliccando sull’azione “Modifica stato”, rappresentato nella figura, il sistema apre la dialog, dove l’utente deve selezionare lo stato “Attivo” per effettuare la riattivazione.

Figura- Schermata di dettaglio del PdL, con evidenza alla azione di modifica stato

Figura - Dialog di modifica stato

Propagare le modifiche agli Ordini di Lavoro esistenti: nel caso in cui sia necessario aggiornare manualmente gli OdL già generati con la nuova versione del PdL, l’utente, all’interno della maschera di dettaglio del PdL appena modificato, deve selezionare il tasto “Propaga modifiche”. L’azione di propaga modifiche è valida solo per OdL ancora in stato “Aperto” o “Da validare”, e deve essere effettuata nel caso di modifica dei seguenti campi:

Durata pianificata

Risorse interne ed esterne

Descrizione

Metodo

Flag outsourcing?

Figura - Schermata di dettaglio del PdL, con evidenza alla azione di propagazione della modifica

### 2.11.3 Disattivazione attività

L’attività viene disattivata una volta che lo stato è impostato in “Non attiva” e in questo stato, il PdL non sarà più letto dal DSS per la generazione automatica di nuovi OdL ne sarà fruibile per la generazione manuale di OdL. Per effettuare la disattivazione di un PdL presente a catalogo, l’utente deve eseguire i seguenti passaggi:

Accedere all’applicazione “Catalogo Attività”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili, suddivise per ambito. L’utente deve selezionare l’opzione “Pianificazione” e successivamente “Catalogo attività”. In alternativa, è possibile digitare “Catalogo attività” nel campo di ricerca per accedere rapidamente all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo attività"

Selezionare l’attività da disattivare: dalla vista elenco, l’utente può individuare il PdL di interesse e selezionarlo cliccando sul record presente nella colonna “Attività”. Selezionando l’icona filtro, è possibile applicare criteri di ricerca specifici sulle colonne disponibili (Attività, Descrizione, Revisione del PdL, Tipologia attività, Tipologia asset, Stato).

Figura - Vista elenco iniziale del catalogo delle attività, con evidenza al filtro di ricerca

Impostare lo stato su “Non attiva”: all’interno della maschera di dettaglio, l’utente deve selezionare l’azione “Modifica stato” per aprire la dialog. In questa dialog, l’utente seleziona la freccia all’interno del campo “Nuovo stato” e successivamente lo stato “Non attiva” per disattivare l’attività. Se impostato lo stato “Non Attivo”, l’utente ha la possibilità di riattivarlo qualora necessario. La disattivazione di un’attività può essere eseguita manualmente dall’utente oppure avvenire in automatico al superamento della data di fine validità indicata a catalogo.

Figura - Schermata di dettaglio della attività, con evidenza alla azione "modifica stato"

Figura - Dialog di modifica di stato

Gestione dei warning di sistema: A seguito della disattivazione, il sistema mostra due messaggi di avviso:

Warning 1: informa l’utente che tutte le policy per cui l’attività disattivata rappresenta un output saranno portate in stato “Pending review”. Il sistema avvia automaticamente la procedura di aggiornamento, e l’utente dovrà attendere la conferma di avvenuto completamento.

Warning 2: informa l’utente che tutte le policy per cui l’attività disattivata è l’unico input saranno anch’esse poste in “Pending review”. Anche in questo caso, il sistema avvia una procedura automatica per la modifica dello stato delle policy collegate.

### 2.11.4 Consultazione del Centro di controllo per incongruenze legate alle attività

Questo paragrafo è riferito alla definizione della sezione del centro di controllo dedicata alla visualizzazione delle incongruenze esistenti sui PdL all’interno del catalogo delle attività. La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema.

Attività non associate a nessuna policy: Nel momento in cui una Policy viene portata in stato “Non attiva”, vengono riportate all’interno del CdC le attività ad essa associate.

Le informazioni relative alla sezione “Attività non associate a nessuna Policy” sono riportate nelle seguenti colonne precompilate:

ID PdL

ID Macro-attività

Tipologia asset

Stato

All’interno delle tabelle, l’utente ha la possibilità di svolgere le seguenti operazioni:

Filtrare la ricerca: selezionando l’icona del filtro è possibile inserire il parametro desiderato in uno qualsiasi dei campi disponibili.

Selezionare il record: cliccando su un record presente in ciascuna colonna, l’utente viene automaticamente indirizzato al catalogo di interesse dove potrà risolvere l’incongruenza verificata.

La Figura sottostante rappresenta la sezione del centro di controllo appena descritta.

Figura - Sezione del centro di controllo "Attività non associate a nessuna Policy”

## 2.12 Gestione catalogo attività standard e catalogo CoIn

In questo capitolo viene spiegata la modalità di gestione prevista per il catalogo standard e il catalogo CoIn. Entrambi i cataloghi sono essenziali per la valorizzazione delle informazioni presenti nel tab “CoIn” degli OdL.

Le tabelle di seguito riportate contengono le informazioni che caratterizzano l’attività standard e gli oggetti CoIn all’interno dei rispettivi cataloghi.

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ID Attività Standard | Codice univoco che identifica l’attività standard | campo libero valorizzato manualmente | Facoltativo |  |
| Descrizione attività standard | Descrizione dell’attività standard | campo libero valorizzato manualmente | Facoltativo |  |
| Codice CoIn OPEX | Tabella contenente una lista di codici CoIn di tipologia OPEX ammissibili per questa attività | Pick-list | Facoltativo |  |
| Codice CoIn CAPEX | Tabella contenente una lista di codici CoIn di tipologia CAPEX ammissibili per questa attività | Pick-list | Facoltativo |  |
| Codice CoIn ANR | Tabella contenente una lista di codici CoIn di tipologia ANR ammissibili per questa attività | Pick-list | Facoltativo |  |
| Tipo spesa prevalente | Campo contenente il tipo spesa prevalente dell’attività standard. Può essere valorizzato con OPEX, CAPEX o ANR | Pick-list | Facoltativo | Facoltativo |
| Codice CoIn prevalente | Campo valorizzato con il Codice CoIn prevalente per l’attività, selezionato sulla base del Tipo spesa prevalente scelto | Pick-list | Facoltativo |  |

Tabella - Campi facoltativi e obbligatori del catalogo attività standard

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| Codice CoIn | Campo contenente il codice univoco della CoIn | campo libero valorizzato manualmente dall’utente | Obbligatorio |  |
| Tipo ordine | Campo contenente il tipo ordine associato alla CoIn. | Pick-list | Facoltativo |  |
| Tipo attività PM | Campo contenente il tipo attività PM associato alla CoIn. | Pick-list | Facoltativo |  |
| Tipo spesa | Campo contenente il tipo spesa associato alla CoIn. Può assumete i valori OPEX, CAPEX o ANR | Pick-list | Facoltativo |  |
| Descrizione CoIn | Descrizione del codice CoIn | campo libero valorizzato manualmente dall’utente | Facoltativo |  |

Tabella - Campi facoltativi e obbligatori del catalogo CoIn

### 2.12.1 Inserimento attività standard nel catalogo standard

Per procedere all’inserimento di un nuovo ID attività standard all’interno della piattaforma è necessario seguire i seguenti step:

Accedere all’applicazione "Catalogo attività standard": posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Catalogo attività standard” per accedere all’ambiente dedicato alla gestione delle attività standard. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo attività standard” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo attività standard"

Cliccare su “Nuovo Record”: selezionare l’opzione “Crea nuova attività standard” per creare un nuovo ID attività standard o in alternativa selezionare l’opzione “+” evidenziata in alto a destra. L’utente verrà reindirizzato alla schermata di dettaglio della attività standard, dedicata alla compilazione dei campi contabili associati.

Figura - Vista elenco iniziale del catalogo attività standard, con evidenza alle azioni di aggiunta di un nuovo record

Compilare i campi obbligatori e facoltativi in maschera: l’utente deve compilare i campi obbligatori e facoltativi descritti nella Tabella 8 sulla schermata di creazione dell’attività standard. Una volta completata la compilazione, l’utente deve cliccare sul pulsante “Salva” situato in alto a destra nella maschera

Figura - Schermata di dettaglio dell'attività standard, con evidenza alle azioni di salvataggio

Nota: Si precisa che l’intera gestione del catalogo è manuale. Non sono previste integrazioni o automatismi con sistemi esterni per il recupero o l’aggiornamento dei dati.

### 2.12.2 Inserimento ID CoIn nel catalogo CoIn

Per procedere all’inserimento di un nuovo ID CoIn all’interno del catalogo è necessario seguire i seguenti step:

Accedere all’applicazione "Catalogo Coin": posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Catalogo Coin” per accedere all’ambiente dedicato alla gestione dei codici contabili Coin. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo CoIn” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo CoIn"

Cliccare su “Nuovo Record”: selezionare l’opzione “Crea nuova CoIn” per creare un nuovo codice CoIn, o in alternativa, selezionare il pulsante “+”. Il sistema rende disponibile una nuova riga dove è possibile inserire le informazioni relative alla CoIn.

Figura - Vista elenco del catalogo CoIn, con evidenza alle azioni di creazione di una nuova CoIn

Compilare i campi necessari: l’utente deve compilare i campi obbligatori e facoltativi descritti nella Tabella 9 sulla riga della nuova CoIn. Una volta completata la compilazione, l’utente deve cliccare sul pulsante “Salva” situato in alto a destra nella maschera.

Figura - Vista elenco del catalogo CoIn, con evidenza alla riga dove inserire le informazioni relative alla nuova CoIn

Nota: l’intera gestione del catalogo Coin è svolta manualmente. Non sono previste integrazioni con sistemi esterni per il recupero delle informazioni.

### 2.12.3 Eliminazione record dal catalogo CoIn, visualizzazione di incongruenze nel centro di controllo e adeguamento del catalogo standard

Il presente capitolo è suddiviso in due sezioni. Nella prima verrà descritto il processo di eliminazione di un codice CoIn dal relativo catalogo, mentre nella seconda è specificata la modalità di gestione di eventuali incongruenze dovute all’eliminazione di un codice CoIn.

Eliminazione di un record dal catalogo CoIn

Per eliminare un oggetto presente nel catalogo CoIn, l’utente deve eseguire la seguente procedura:

Accedere all’applicazione "Catalogo Coin": posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Catalogo Coin” per accedere all’ambiente dedicato alla gestione dei codici contabili Coin. In alternativa, può inserire nel campo di ricerca l’opzione “Catalogo CoIn” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Catalogo CoIn"

Selezionare l’elemento da eliminare: dopo aver trovare l’ID CoIn che desidera eliminare, l’utente seleziona il cestino per procedere con l’eliminazione del record.

Figura - Vista elenco del catalogo CoIn, con evidenza all'icona di eliminazione del record

Salvare l’eliminazione: dopo aver selezionato l’icona di cancellazione, l’utente seleziona l’icona di salvataggio per effettuare l’azione. In alternativa, è possibile selezionare la freccia per cancellare l’eliminazione.

Figura - Vista elenco del catalogo CoIn, con evidenza all'icona di salvataggio (alto a destra) e di torna indietro (freccia all'interno della tabella)

Visualizzazione di incongruenze nel centro di controllo e adeguamento del catalogo standard

Qualora venga eliminato un codice Coin utilizzato in uno o più ID attività standard presenti nel “Catalogo attività standard”, al momento del salvataggio il sistema mostra il seguente pop-up di avviso con il messaggio: “Verifica sul centro di controllo i record del catalogo standard che contengono la CoIn appena eliminata”. Seguente le stesse procedure già descritti in capitoli precedenti, l’utente centrale potrà accedere al “Centro di controllo” per consultare la tabella di dettaglio dei riferimenti. Selezionando un singolo record nella sezione “Attività standard con CoIn non ammissibili”, l’operatore verrà reindirizzato direttamente alla schermata del “Catalogo standard”, dalla quale sarà possibile procedere con l’eliminazione del codice CoIn appena eliminato.

Figura - Sezione del centro di controllo contenente i record del catalogo standard che contengono Coin non presenti nel Catalogo Coin

## 2.13 Gestione cataloghi risorse esterne – ricavi e Matrice origine – destinazione

### 2.13.1 Catalogo risorse esterne – ricavi

Il catalogo delle Risorse esterne – Ricavi serve a fornire delle informazioni necessarie per popolare i campi delle risorse esterne e ricavi presente nel tab “Pianificazione” degli OdL. La gestione del catalogo delle Risorse esterne - Ricavi è abilitata esclusivamente agli utenti con profilo P1, secondo le autorizzazioni di sistema.

La tabella di seguito riportata contiene le informazioni che caratterizzano le risorse esterne e ricavi all’interno del catalogo.

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ID Materiale / Ricavo | Codice univoco del record | Campo libero valorizzato manualmente | Obbligatorio |  |
| Descrizione materiale/ ricavo | Campo contenente una descrizione testuale della risorsa | Campo libero valorizzato manualmente | Facoltativo |  |
| Gruppo merce | Gruppo merce associato alla risorsa. Campo non visibile per risorsa di tipo “Ricavi” | Pick-list | Facoltativo |  |
| Definizione gruppo merce | Campo che descrive il gruppo merce precedentemente selezionato | Valorizzazione automatica in base al campo “Gruppo merce” | Facoltativo |  |
| Conto |  | Pick-list | Facoltativo |  |
| Descrizione conto | Campo che descrive il gruppo merce precedentemente selezionato | Valorizzazione automatica in base al campo “Conto” | Facoltativo | Facoltativo |
| Tipo risorsa | Determina il tipo di risorsa. Sono disponibili i valori “Prestazioni terze”, “Materiali” o “Ricavi” | Pick-list | Facoltativo |  |
| Unità di misura | Unità di misura associata alla risorsa | Pick-list | Facoltativo |  |
| Stato | Stato attuale della risorsa. Campo preimpostato con il valore “Attivo” | Valorizzazione automatica | Obbligatorio |  |
| Importo materiale fornito da AA (€/unità) | Importo, in € per unità di misura scelta, associato al record. Campo non visibile per tipo risorsa “Ricavi” | Campo libero valorizzato manualmente | Facoltativo |  |

Tabella - Campi obbligatori e facoltativi del catalogo ricavi - risorse esterne

### Inserimento ID risorsa nel catalogo risorse esterne – ricavi

Per procedere all’inserimento di nuovi record all’interno del catalogo, l’utente deve seguire la seguente procedura:

Accedere all’applicazione "Catalogo Risorse esterne – Ricavi": posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Magazzino” e successivamente l’opzione “Catalogo Risorse esterne – Ricavi” per accedere all’applicazione desiderata. In alternativa, può inserire nel campo di ricerca la voce “Catalogo Risorse esterne - Ricavi” per essere indirizzato direttamente all’applicazione.

Figura - Lista di applicazioni di EAM, con evidenza all'app "Catalogo di risorse esterne / Ricavi"

Cliccare su “Nuovo RE/Ricavi”: nella vista elenco del catalogo, selezionare l’opzione “Nuovo RE/Ricavi” per essere indirizzato alla schermata di dettaglio del record. In alternativa, l’utente può selezionare il tasto “+”.

Figura - Vista elenco iniziale del catalogo delle risorse esterne / ricavi, con evidenza alle azioni di aggiunta di nuovo record

Compilare i campi obbligatori e facoltativi in maschera: all’interno della maschera di dettaglio del nuovo record, l’utente procede al popolamento dei campi descritti. Successivamente, cliccando sull’icona di salvataggio evidenziata in alto a destra, l’utente procede al salvataggio del nuovo record.

Figura - Schermata di dettaglio del record all'interno del catalogo delle risorse esterne / ricavi

Nota: Si precisa che la gestione del catalogo è interamente manuale e non prevede integrazioni con sistemi esterni per il recupero o l’aggiornamento delle informazioni.

### Inserimento record nella matrice Origine – Destinazione

La matrice origine destinazioni contiene l’insieme dei tempi di trasferimenti necessarie ad un Titris per raggiungere una sede tecnica di primo livello. Si specifica che l’inserimento, la modifica o la cancellazione dei record è consentita esclusivamente agli utenti centrali. Gli utenti non centrali hanno accesso in sola visualizzazione. Per generare un nuovo record all’interno dell’applicazione “Matrice Origine – Destinazione”, l’utente deve seguire la seguente procedura:

Accedere all’applicazione "Matrice Origine – Destinazione": posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Amministrazione” e successivamente “Matrice Origine – Destinazione” per accedere all’ambiente dedicato alla gestione dei tempi di trasferimento associati ai Titris. In alternativa, l’utente può scrivere direttamente nel campo di ricerca il valore “Matrice Origine – Destinazione”.

Figura - Lista di applicazione di EAM, con evidenza all'app "Matrice Origine - Destinazione"

Cliccare su “Nuovo Record”: selezionare l’opzione “Nuovo Record” per inserire un nuovo collegamento Origine – Destinazione, o in alternativa, selezionare uno dei pulsanti “+”.

Figura - Lista elenco dell'applicazione "Matrice Origine - Destinazione", con evidenza alle azioni di aggiunta di record

Compilare i campi obbligatori in maschera: dopo aver selezionato l’azione di creazione, il sistema genera una nuova riga dove sarà possibile inserire le informazioni necessarie. Selezionando le lenti di ingrandimento, il sistema fornisce delle liste valori con le quale l’utente potrà popolare sia il campo “Origine” che la “Destinazione”. Il campo “Tempo di trasferimento” deve essere valorizzato manualmente con il numero di ore necessarie al Titris per raggiungere la sede tecnica selezionata.

Figura - Vista di dettaglio dell'inserimento di un nuovo record sulla Matrice Origine - Destinazione

Filtrare e consultare i record: tramite l’icona evidenziata, il sistema consente di filtrare i record inseriti sulla base di ciascuna delle colonne disponibili (Origine, Destinazione, Tempo di trasferimento), facilitando la consultazione e la ricerca puntuale delle informazioni.

Figura - Lista elenco dell'applicazione "Matrice Origine - Destinazione", con evidenza all'icona di filtro di ricerca

Nota: si precisa che la gestione della matrice è interamente manuale e non prevede integrazioni con sistemi esterni per il recupero o l’aggiornamento delle informazioni. È tuttavia possibile effettuare operazioni di caricamento o modifica massiva dei dati tramite MXLoader. Le modifiche apportate alla matrice si applicano esclusivamente agli Ordini di Lavoro in stato “Aperto”.

## 2.14 Gestione catalogo CBS – CBE pianificata

Tramite il catalogo “CBS – CBE Pianificata” l’utente ha la possibilità di consuntivare e gestire delle informazioni contabili che vengono utilizzate in EAM nell’ambito della pianificazione economica degli OdL. Le informazioni presenti nel catalogo sono descritte nel dettaglio nella tabella sottostanti. Si specifica che il catalogo è modificabile esclusivamente da utenti P1 ed è in sola visualizzazione dagli altri profili.

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| Tipo spesa | campo valorizzato manualmente dall’utente tramite una lista valori contenente “OPEX”, “ANR”; | Pick-list | Facoltativo |  |
| CBS Pianificata | Codice univoco della CBS pianificata | Campo libero valorizzato manualmente | Facoltativo |  |
| CBE Pianificata | Codice univoco della CBE pianificata | Campo libero valorizzato manualmente | Facoltativo |  |
| Codice CoIn | La valorizzazione del campo è consentita solo se il campo “Tipo Spesa” è valorizzato con “ANR” | Pick list | Facoltativo |  |
| UI | Unità impianti associata alla CBS - CBE | Pick list | Facoltativo |  |

Tabella - Campi obbligatori e facoltativi del catalogo CBS - CBE Pianificata

### 2.14.1 Creazione di un record all’interno del catalogo CBS – CBE pianificata

Per aggiungere un record nel catalogo, l’utente deve seguire la seguente procedura:

Accedere all’applicazione "Catalogo CBS – CBE Pianificate”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Amministrazione” e poi selezionare l’opzione “Catalogo CBS- CBE Pianificata” per accedere all’ambiente dedicato alla gestione e consultazione del catalogo. In alternativa, l’utente può inserire direttamente nel campo di ricerca “Catalogo CSB – CBE Pianificata” per accedere all’ambiente desiderato.

Figura - Vista elenco delle applicazioni di EAM, con evidenza all'app "Catalogo CBS - CBE Pianificata"

Cliccare su “Nuova CBE/CBS pianificata”: selezionare l’opzione “Nuova CBE/CBS pianificata” evidenziata a sinistra nell’interfaccia oppure cliccare sul pulsante “+” in alto a destra per generare un nuovo record. Entrambi le azioni sono evidenziate nella figura sottostante.

Figura - Schermata del catalogo CBS - CBE pianificata, con evidenza alle azioni di aggiunta di una nuova riga

Popolare i campi: dopo eseguita l’azione descritta nello step precedente, l’utente deve procedere al popolamento dei campi evidenziati sotto.

Figura- Schermata del catalogo CBS - CBE pianificata, con evidenza ai campi da popolare

Salvare il record: dopo aver inserito il nuovo record, è necessario effettuare il salvataggio selezionando una delle icone evidenziate sotto.

Figura - Schermata del catalogo CBS - CBE pianificata, con evidenza alle azioni di salvataggio

Nota: Si precisa che l’anagrafica degli oggetti economici è gestita centralmente e non prevede aggiornamenti manuali da parte degli utenti locali. La valorizzazione avviene in automatico in base ai criteri associati all’attività selezionata. Inoltre, si specifica che la gestione del catalogo è puramente manuale e che non sono previste integrazioni con altri sistemi per il recupero delle informazioni.

### 2.14.2 Eliminazione di un record all’interno del catalogo CBS – CBE pianificata

Per eliminare un record dal catalogo, l’utente deve seguire la seguente procedura:

Accedere all’applicazione "Catalogo CBS – CBE Pianificate”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Amministrazione” e poi selezionare l’opzione “Catalogo CBS- CBE Pianificata” per accedere all’ambiente dedicato alla gestione e consultazione del catalogo. In alternativa, l’utente può inserire direttamente nel campo di ricerca “Catalogo CSB – CBE Pianificata” per accedere all’ambiente desiderato.

Figura - Vista elenco delle applicazioni di EAM, con evidenza all'app "Catalogo CBS - CBE Pianificata"

Cliccare sull’icona di eliminazione: selezionare il cestino per procedere con l’eliminazione della riga desiderata. Qualora necessario, l’utente può seleziona la freccia per interrompere la procedura di cancellazione.

Figura - Schermata del catalogo CBS - CBE pianificata, con evidenza alle azioni di aggiunta di una nuova riga

Figura - Schermata del catalogo CBS - CBE pianificata, con evidenza all'icona di "torna indietro" per l'eliminazione di un record

Salvare l’eliminazione: dopo aver eseguito gli step precedenti, l’utente deve effettuare il salvataggio selezionando una delle icone evidenziate sotto.

Figura - Schermata del catalogo CBS - CBE pianificata, con evidenza alle azioni di salvataggio

Nota: Si precisa che l’anagrafica degli oggetti economici è gestita centralmente e non prevede aggiornamenti manuali da parte degli utenti locali. La valorizzazione avviene in automatico in base ai criteri associati all’attività selezionata. Inoltre, si specifica che la gestione del catalogo è puramente manuale e che non sono previste integrazioni con altri sistemi per il recupero delle informazioni.

## 2.15 Consultazione indicatori di condizione tecnica

All’interno della piattaforma EAM è possibile consultare gli indicatori di condizione tecnica che descrivono lo stato di salute e le prestazioni degli asset, tra cui l’Health index (HI), il Tasso di Anomalia (TdA) e il Tasso di Guasto (TdG). Tali indicatori sono accessibili attraverso diverse modalità operative, che consentono sia una visione sintetica per gruppi di asset, sia un dettaglio puntuale all’interno della Scheda di Condizione Tecnica.Di seguito vengono esclusivamente descritte le principali modalità di consultazione dell’indicatore “Health index”.

### 2.15.1 Consultazione Health index nel modulo “Health”

Esistono due modalità previste per consuntivare i valori di Health index relativi ad un gruppo di asset all’interno del modulo Health. Gli step necessarie per eseguire la consultazione sono descritti nei seguenti paragrafi.

Tramite l’applicazione “Impostazioni di punteggio”

Accedere al modulo Health: per accedere al modulo “Health” l’utente nella vista iniziale di EAM deve selezionare l’opzione evidenziata sotto.

Figura - Vista iniziale della piattaforma EAM, con evidenza al Modulo Health

Accedere all’applicazione “Impostazione di Punteggio”: all’interno del modulo Health, l’utente deve posizionare il cursore sopra la barra laterale sinistra per vedere l’elenco di applicazioni disponibili. L’utente deve selezionare l’opzione “Impostazioni di punteggio” per accedere all’ambiente dedicato.

Figura - Lista di applicazioni disponibili all'interno del modulo Health

Figura - Vista iniziale dell'applicazione Asset nel modulo Health

Selezionare l’indicatore di interesse: tramite la lentina di ingrandimento, l’utente può indicare al sistema quale indicatore di condizione tecnica vuole analizzare. Nella figura sotto, ad esempio, viene rappresentata la ricerca degli indicatori “HI” all’interno dell’applicazione. Dopo aver trovato l’indicatore di interesse, l’utente lo seleziona cliccando sul record per aprire la maschera di dettaglio dell’indicatore.

Figura - Vista iniziale dell'applicazione "Impostazioni di punteggio" all'interno del modulo Health, con evidenza all'indicatore "HI Conduttore" selezionato

Selezionare l’asset di interesse: selezionando il record evidenziato, l’utente viene indirizzato alla schermata di dettaglio dell’asset, dove potrà analizzare delle informazioni tecniche associati all’asset precedentemente selezionato, tra cui il “Health index”

Figura - Vista di dettaglio dell'indicatore selezionato

Figura- Vista di dettaglio dell'asset all'interno dell'applicazione "Asset" del modulo Health

Tramite l’applicazione “Asset”

Accedere al modulo Health: per accedere al modulo “Health” l’utente nella vista iniziale di EAM deve selezionare l’opzione evidenziata sotto.

Figura - Vista iniziale della piattaforma EAM, con evidenza al Modulo Health

Accedere all’applicazione “Asset”: all’interno del modulo Health, l’utente deve posizionare il cursore sopra la barra laterale sinistra per vedere l’elenco di applicazioni disponibili. L’utente deve selezionare l’opzione “Asset” per accedere all’ambiente dedicato.

Figura - Lista di applicazioni disponibili all'interno del modulo Health

Figura - Vista iniziale dell'applicazione Asset all’interno del modulo Health

Selezionare l’asset di interesse: una volta dentro dell’applicazione, l’utente deve selezionare l’asset sul quale desidera consuntivare gli indicatori di condizione tecnica. Per filtrare l’elenco di asset rappresentati nella vista iniziale, l’utente può o selezionare una query preimpostata, cliccando sulla freccia e selezionare l’opzione di interesse o, in alternativa, indicare direttamente al sistema tramite la lente di ingrandimento il codice dell’asset che si vuole analizzare.

Figura - Vista iniziale dell'applicazione Asset all’interno del modulo Health, con evidenza ai metodi di filtrare la ricerca

Visualizzare i risultati associati: nella vista di dettaglio sono l’utente può analizzare gli indicatori di condizione tecnica associati all’asset precedentemente selezionato.

Figura- Vista di dettaglio dell'asset all'interno dell'applicazione "Asset" del modulo Health

### Consultazione HI nella Scheda di condizione tecnica

Per consultare l’Health Index (HI) all’interno della Scheda di Condizione Tecnica nel modulo “Manage”, è necessario seguire i seguenti step:

Accedere all’applicazione "Scheda di condizione tecnica": posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Asset” e successivamente l’opzione “Scheda di condizione tecnica” per accedere all’applicazione desiderata. In alternativa, può inserire nel campo di ricerca la voce “Scheda di condizione tecnica” per essere indirizzato direttamente all’applicazione.

Figura- Lista delle applicazioni di EAM, con evidenza all'applicazione "Scheda di condizione tecnica"

Individuare l’asset di interesse: una volta aperta la vista dell’applicazione, selezionando l’icona di filtro è possibile ricercare l’asset desiderato.

Figura - Vista elenco iniziale dell'applicazione "Scheda di condizione tecnica" con evidenza all'icona di filtro

Consultare i tab informativi: dopo aver selezionato l’asset di interesse, il sistema apre la maschera la quale è suddivisa nei seguenti tab:

Asset

Attributi di anagrafica

Attività associate

Difformità e interferenze

Opere interferenti

Indicatori

Misure e variabili

Documentazione

Selezionando il tab “Indicatori”, vengono visualizzati tutti gli indicatori di condizione tecnica calcolati per l’asset selezionato, distribuiti all’interno della tabella. Nello specifico, la tabella riporta le seguenti informazioni:

Valore: Codice dell’Health Index;

Descrizione: Descrizione dell’Health Index (comprensiva del nome);

Valore: Valore dell’Health Index;

Data ultima rilevazione: Data dell’ultimo calcolo dell’indicatore;

Figura - Schermata di dettaglio dell’asset all'interno della Scheda di condizione tecnica, con evidenza al tab “Indicatori”

Figura - Schermata di dettaglio del tab "Indicatori" all'interno della Scheda di condizione tecnica, con evidenza alla tabella contenente le informazioni sugli indicatori

Consultare lo storico degli Health Index: cliccando sull’icona accanto all’ID dell’Health Index, è possibile accedere allo storico dei valori calcolati per quello indicatore specifico, rappresentata dalla dialog. La dialog contenente lo storico degli indicatori riporta la descrizione dell’indicatore, la data di ultima rilevazione e il valore calcolato nell’ultima rilevazione.

Figura - Schermata di dettaglio del tab "Indicatori" all'interno della Scheda di condizione tecnica, con evidenza alla funzione di “Visualizza storico”

Figura - Dialog contenente le informazioni storicizzate dell'indicatore di condizione tecnica di interesse

## Reporting HI

La sezione dedicata al reporting degli Health Index consente agli utenti di monitorare in modo strutturato lo stato di salute degli asset aziendali. Gli Health Index rappresentano un indicatore sintetico, calcolato sulla base di parametri tecnici e funzionali, utile a supportare le attività di manutenzione preventiva e le decisioni strategiche relative alla gestione degli impianti. Tutti i profili, dal P1 al P15, sono abilitati all’esecuzione del reporting HI.

### Reporting HI (linee aeree)

Per visualizzare il report relativo agli Health Index (HI) delle Linee Aeree, è necessario seguire i seguenti step:

Accedere all’applicazione “Linee”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Asset” e successivamente l’applicazione “Linee” per accedere all’ambiente dedicato alla gestione degli asset lineari. In alternativa, l’utente può inserire direttamente nella barra di ricerca il valore “Linee”

Figura - Lista delle applicazioni di EAM, con evidenza all'applicazione “Linee"

Accedere alla sezione “Esegui report”: all’interno dell’applicazione “Linee”, l’utente deve selezionare l’azione “Esegui report”.

Figura - Vista elenco iniziale dell'applicazione "Linee", con evidenza all'azione "Esegui report"

Selezionare il report di interesse: nella dialog, l’utente dovrà cliccare sul report dedicato agli Health Index delle Linee Aeree. Il sistema apre la pagina contenente i parametri di lancio.

Figura - Dialog di selezione del tipo di report da generare, con evidenza al report di tipo “HI Linee aeree”

Verificare e compilare i parametri di lancio: dopo aver eseguito lo step precedente, l’utente deve valorizzare i campi presenti nella dialog con i seguenti parametri di lancio:

Asset di primo livello

Asset di secondo livello

Asset di terzo livello

Classificazione asset

Livello di tensione della linea

Proprietario

DT responsabile asset

UI responsabile asset

Avviare la generazione del report: una volta impostati i parametri desiderati, è possibile cliccare sul tasto “Inoltra” per procedere con la generazione del report.

Figura Dialog di definizione dei parametri di lancio del report da generare, con evidenza al tasto di avvio “Inoltra”

Visualizzare e analizzare i risultati generati: dopo aver inserito i parametri richiesti, il sistema fornisce una tabella con la struttura del report e i valori HI restituiti per ciascuna linea.

Figu - Report di HI Linee aeree

Scaricare il report: qualora necessario, l’utente può utilizzare l’apposita funzione per esportare il report generato nel formato più adeguato. Cliccando sulla funzione evidenziata nella barra superiore del report, il sistema apre la dialog di definizione delle condizioni dell’export. L’utente deve selezionare il formato desiderato selezionando la freccia messa in evidenza e poi selezionare “Ok” per iniziare lo scarico del report.

Figura - Report HI Linee aeree, con evidenza alle azioni necessarie per l'estrazione del report

### Reporting HI (linee in cavo)

Per visualizzare il report relativo agli Health Index (HI) delle Linee, è necessario seguire i seguenti step:

Accedere all’applicazione “Linee”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Asset” e successivamente l’applicazione “Linee” per accedere all’ambiente dedicato alla gestione degli asset lineari. In alternativa, l’utente può inserire direttamente nella barra di ricerca il valore “Linee”.

Figura - Lista delle applicazioni di EAM, con evidenza all'applicazione "Linee"

Accedere alla sezione “Esegui report”: all’interno dell’applicazione “Linee”, l’utente deve selezionare l’azione “Esegui report”.

Figura - Vista elenco iniziale dell'applicazione "Linee", con evidenza all'azione "Esegui report"

Selezionare il report di interesse: nella dialog, l’utente dovrà cliccare sul report dedicato agli Health Index delle Linee in cavo.

Figura - Dialog di selezione del tipo di report da generare, con evidenza al report di tipo “HI Linee in cavo”

Verificare e compilare i parametri di lancio: dopo aver eseguito lo step precedente, l’utente deve valorizzare i campi presenti nella dialog con i seguenti parametri di lancio:

Asset di primo livello

Asset di secondo livello

Asset di terzo livello

Classificazione asset

Proprietario

Livello di tensione della linea

DT responsabile asset

UI responsabile asset

Avviare la generazione del report: una volta impostati i parametri desiderati, è possibile cliccare sul tasto “Inoltra” per procedere con la generazione del report.

Figura - Dialog di definizione dei parametri di lancio del report da generare, con evidenza al tasto di avvio “Inoltra”

Visualizzare e analizzare i risultati generati: dopo aver inserito i parametri richiesti, il sistema fornisce una tabella con la struttura del report e i valori HI restituiti per ciascuna linea in cavo.

Figura - Report di HI Linee in cavo

Scaricare il report: qualora necessario, l’utente può utilizzare l’apposita funzione per esportare il report generato nel formato più adeguato. Cliccando sulla funzione evidenziata nella barra superiore del report, il sistema apre la dialog di definizione delle condizioni dell’export. L’utente deve selezionare il formato desiderato selezionando la freccia messa in evidenza e poi selezionare “Ok” per proseguire con lo scarico del report.

Figura - Report HI Linee in cavo, con evidenza alle azioni necessarie per l'estrazione del report

### Reporting HI (componenti di stazione)

Per visualizzare il report relativo agli Health Index (HI) dei componenti di stazione, è necessario seguire i seguenti step:

Accedere all’applicazione “Stazioni”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Asset” e successivamente l’applicazione “Stazioni” per accedere all’ambiente dedicato alla gestione degli elementi di stazione. In alternativa, l’utente può inserire direttamente nella barra di ricerca il valore “Stazioni”.

Figura - Lista delle applicazioni di EAM, con evidenza all'applicazione "Stazioni"

Accedere alla sezione “Esegui report”: all’interno dell’applicazione “Stazioni”, l’utente deve selezionare l’azione “Esegui report”.

Figura - Vista elenco iniziale dell'applicazione "Stazioni", con evidenza all'azione di "Esegui report"

Selezionare il report di interesse: nella dialog, l’utente dovrà cliccare sul report dedicato agli Health Index dei componenti di stazione.

Figura - Dialog di selezione del tipo di report da generare, con evidenza al report di tipo “HI componenti di stazioni”

Verificare e compilare i parametri di lancio: dopo aver eseguito lo step precedente, l’utente deve valorizzare i campi presenti nella dialog con i seguenti parametri di lancio:

Asset di primo livello

Asset di secondo livello

Asset di terzo livello

Asset di quarto livello

DT responsabile asset

UI responsabile asset

Avviare la generazione del report: una volta impostati i parametri desiderati, è possibile cliccare sul tasto “Inoltra per procedere con la generazione del report.

Figura 10 - Dialog di definizione dei parametri di lancio del report da generare, con evidenza al tasto di avvio “Inoltra”

Visualizzare e analizzare i risultati generati: dopo aver inserito i parametri richiesti, il sistema fornisce una tabella con la struttura del report e i valori HI restituiti per ciascun componente di stazione.

Figura - Report di HI Componenti di stazioni

Scaricare il report: qualora necessario, l’utente può utilizzare l’apposita funzione per esportare il report generato nel formato più adeguato. Cliccando sulla funzione evidenziata nella barra superiore del report, il sistema apre la dialog di definizione delle condizioni dell’export. L’utente deve selezionare il formato desiderato selezionando la freccia messa in evidenza e poi selezionare “Ok” per proseguire con lo scarico del report.

Figura - Report HI Componenti di stazione, con evidenza alle azioni necessarie per l'estrazione del report

# 3. DECISION SUPPORT SYSTEM

## 3.1 Introduzione

Il Decision Support System (DSS) è il motore di elaborazione delle Policy che ha la funzione in EAM di gestire la generazione automatizzata, la modifica e il cambio stato degli Ordini di Lavoro (OdL). Il DSS può quindi:

Generare nuovi ordini in seguito all’elaborazione delle policy;

Modificare ordini generati da una determinata policy, già presenti a sistema, a seguito di una modifica della stessa;

Cambiare stato agli ordini presenti a sistema che risultano prevalsi da ordini generati dal DSS stesso. Si precisa che il cambio stato potrà avvenire solamente per OdL che non presentano consuntivi inviati a SAP e che si trovano in stato precedente a “Schedulabile”.

È possibile identificare due macro-tipologie di elaborazioni eseguite dal DSS, che dipendono dalla configurazione della policy:

Elaborazioni di policy on condition effettuate dal DSS allo scattare delle condizioni in input alle policy, caratterizzate dalla generazione automatica di un ordine ad asset.

Elaborazioni di policy periodiche che generano ordini a frequenza prestabilita, effettuate dal DSS in una data definita a sistema in cui avviene l’elaborazione massiva di tutte le policy periodiche

Gli input definiti nella policy possono essere soddisfatti a seguito di eventi di processo in EAM o in funzione dall’acquisizione di dati da sistemi esterni:

Eventi di processo EAM:

Calcolo indicatori di condizione tecnica;

Definizione anagrafica Asset;

Generazione OdL in stato “APERTO”;

Registrazione e validazione di una difformità o variazione della relativa gravità;

Registrazione e validazione di un’interferenza;

Registrazione Misure;

Aggiornamento variabili di stato.

Eventi derivanti da interfacce con sistemi esterni:

Acquisizione difformità/Interferenze dal report Macchinari/Ariel;

Acquisizione interferenze da Elive;

Acquisizione difformità da cavi interrati.

## 3.2 Policy on condition

Le policy on condition determinano la generazione, da parte del DSS, di un ordine di lavoro, in stato “DA VALIDARE”, per ciascun asset che soddisfa le condizioni in input alla policy. Il DSS genera l’ordine solo se non è già presente a sistema un ordine sull’asset in esame con medesimo PdL e relazionato alla stessa policy.

Una policy on condition può avere in input:

Indicatori di condizione tecnica;

Attributi anagrafici;

Attività (PdL): tale input risulta soddisfatto se a sistema è presente sull’asset un OdL in stato APERTO, SCHEDULABILE, SCHEDULATO, INOLTRATO, AVVIATO con quel PdL.

Difformità: tale input risulta soddisfatto solo se la difformità si trova in stato “VALIDATA”;

Interferenze: tale input risulta soddisfatto solo se l’interferenza si trova in stato “VALIDATA”;

Misure;

Variabili di stato;

Nessun input.

Tali input vengono confrontati con valori di riferimento numerici, alfanumerici o basati su modelli soglie, definiti all'interno della policy, mediante l'utilizzo di specifici operatori di comparazione:

DISPARI

PARI

NOT EXIST

EXIST

<

<=

>

>=

=

!=

Inoltre, gli input possono essere combinati tra loro strettamente tramite i seguenti operatori:

=

!=

AND

OR

Quanto precedentemente definito è valido per tutte le casistiche di policy on condition dettagliate nei paragrafi a seguire.

Policy on condition – standard

Le policy on condition – standard sono policy facenti parte della casistica “on condition” e caratterizzate dall’assenza di altre logiche di elaborazione. In particolare:

non sono riferite a tronchi di ispezione;

non sono associate ad asset di tipo conduttore o fune di guardia (quarto livello) con attività di tipo sostituzione in output;

il flag “Policy aggregata?” non è attivo;

Allo scattare di una policy on condtion standard, il DSS:

Per ogni singolo asset in modo indipendente, valuta gli input della policy singolarmente confrontandoli con gli eventuali target di raffronto.

Se gli input sono soddisfatti, seleziona l’attività (PdL) da associare all’ordine di lavoro coerentemente alla macro-attività definita nella policy.

Valuta l’idoneità dell’asset che soddisfano le condizioni in input.

Valuta la presenza a sistema di ordini per la stessa policy con medesimo PdL.

Valuta la presenza a sistema di ordini sull’asset con PdL prevalente o prevalso rispetto a quello dell’ordine in output alla policy. Valuta anche la presenza a sistema di ordini sull’asset con lo stesso PdL dell’ordine creato dal DSS (auto-prevalenza).

(Solo per le stazioni) Ricerca, qualora necessario dato il PdL, i micro-controlli da associare all’ordine.

Genera l’ordine.

Policy on condition – trainante/trainato

Le policy on condition – trainante/trainato sono policy facenti parte della tipologia “on condition” e sono associate ad asset di tipo conduttore o fune di guardia con in output un’attività di tipo sostituzione.

Allo scattare di una policy on condition – trainante/trainato, il DSS:

Per ogni singolo asset in modo indipendente, valuta gli input della policy singolarmente confrontandoli con gli eventuali target di raffronto.

Se gli input sono soddisfatti, seleziona l’attività (PdL) da associare all’ordine di lavoro coerentemente alla macro-attività definita nella policy.

Valuta l’idoneità dell’asset che soddisfano le condizioni in input.

Valuta la presenza a sistema di ordini per la stessa policy con medesimo PdL.

Determina la presenza della tratta di tesatura. In caso di esito negativo, viene eseguita un’elaborazione standard per il solo asset che soddisfa le condizioni in input. In caso di esito positivo, sincronizza le esecuzioni degli altri asset che appartengono alla stessa tratta di tesatura.

Valuta la presenza a sistema di ordini sull’asset con PdL prevalente o prevalso rispetto a quello dell’ordine in output alla policy. Valuta anche la presenza a sistema di ordini sull’asset con lo stesso PdL dell’ordine creato dal DSS (auto-prevalenza).

Genera gli ordini: vengono creati tanti OdL quanti sono gli asset che appartengono alla tratta di tesatura. Tutti gli OdL i cui asset soddisfano gli input della policy saranno caratterizzati dall’attributo “Trainante”, gli altri dall’attributo “Trainato”.

Policy on condition – tronco di ispezione

Le policy on condition – tronco di ispezione sono policy caratterizzate dal flag “Tronco di ispezione” attivo e insistono su campate aeree o tratte cavo (secondo livello).

Allo scattare di una policy on condition – tronco di ispezione, il DSS:

Per ogni singolo asset in modo indipendente, valuta gli input della policy singolarmente confrontandoli con gli eventuali target di raffronto.

Se gli input sono soddisfatti, seleziona l’attività (PdL) da associare all’ordine di lavoro coerentemente alla macro-attività definita nella policy.

Valuta l’idoneità dell’asset che soddisfano le condizioni in input.

Valuta la presenza a sistema di ordini per la stessa policy con medesimo PdL.

Determina la presenza di un tronco di ispezione associato agli asset che soddisfano le condizioni in input. In caso di esito negativo, l’elaborazione per l’asset in questione termina senza successo. Per tutti gli asset che, invece, hanno un tronco d’ispezione, il DSS sincronizza tutte le elaborazioni e le aggrega per tronco

Valuta la presenza a sistema di ordini sull’asset con PdL prevalente o prevalso rispetto a quello dell’ordine in output alla policy. Valuta anche la presenza a sistema di ordini sull’asset con lo stesso PdL dell’ordine creato dal DSS (auto-prevalenza).

Genera gli ordini: per ogni tronco viene generato un ordine aggregatore associato alla linea relativa alle campate aeree o tratte cavo sulla quale è scattata la policy. Sotto l’ordine aggregatore, vengono aggregati N ordini associati a tutte le campate aeree o tratte cavo (secondi livelli della linea) riferite al singolo tronco di ispezione.

Policy on condition – aggregate

Le policy on condition - aggregate sono caratterizzate da:

Flag “Policy aggregata” attivo;

Livello aggregazione della policy impostata ad un livello uguale ad 1,2,3;

La macro-attività associata alla policy è di tipo sostituzione.

Per le policy aggregate di linea si individuano 3 casistiche:

Policy aggregate conduttori: se superato il valore soglia date le logiche definite per il calcolo dei coefficienti di eleggibilità, vengono generati N OdL di tipo “Sostituzione conduttore” (3° livello) tanti quanti sono i conduttori di terzo livello sottesi alla linea e un ordine aggregato per singola linea.

Policy aggregate funi di guardia: se superato il valore soglia date le logiche definite per il calcolo dei coefficienti di eleggibilità, vengono generati N OdL di tipo “Sostituzione fune di guardia” (4° livello) tante quante sono le funi di guardia di quarto livello sottese alla linea e un ordine aggregato per singola linea.

Policy aggregate strutture: se superato il valore soglia date le logiche definite per il calcolo dei coefficienti di eleggibilità, vengono generati N OdL di tipo “Sostituzione struttura” (4° livello) per tutte le strutture sottese alla linea.

## 3.3 Policy periodiche

Le policy periodiche prevedono la generazione automatica di N ordini ad asset, sulla base della frequenza definita, al verificarsi di determinate condizioni di input alla stessa. Gli ordini vengono creati direttamente in stato “APERTO” e solamente se non sono già presenti a sistema ordini per la stessa policy e stesso PdL per gli asset in esame.

Una policy periodica può avere in input:

Indicatori di condizione tecnica;

Attributi anagrafici;

Attività (PdL): tale input risulta soddisfatto se a sistema è presente sull’asset un OdL in stato APERTO, SCHEDULABILE, SCHEDULATO, INOLTRATO, AVVIATO con quel PdL.

Variabili di stato;

Nessun input.

Tali input sono messi in relazione a dei valori di comparazione mediante degli operatori e possono essere anche combinati tra loro, così come definito per le policy on condition.

Le policy periodiche si dividono in due macro-categorie:

Policy periodiche inter – annuali (ovvero con frequenza maggiore di un anno): il DSS genera un solo OdL ad asset.

Policy periodiche infra – annuali (ovvero con frequenza minore di un anno): il DSS genera N ordini l’anno, data la frequenza definita, fino all’anno n+5.

Frequenza uguale a 1 mese: il sistema genererà 12 OdL all’anno;

Frequenza uguale a 2 mesi: il sistema genererà 6 OdL all’anno;

Frequenza uguale a 3 mesi: il sistema genererà 4 OdL all’anno;

Frequenza uguale a 4 mesi: il sistema genererà 3 OdL all’anno;

Frequenza uguale a 6 mesi: il sistema genererà 2 OdL all’anno;

Frequenza uguale a 1 anno: il sistema genererà 1 OdL all’anno.

Per le policy periodiche, è possibile impostare una data “A partire da”, maggiore della data attuale, che indica il momento a partire dal quale l’OdL generato dalla policy può essere posizionato temporalmente.

Se la data “a partire da” è antecedente rispetto alla data di generazione del primo ordine periodico dell’anno (1°Gennaio+frequenza policy): il DSS esegue le logiche di una normale policy periodica.

Se la data “a partire da” è collocata a posteriori rispetto alla data di generazione del primo ordine periodico dell’anno (1°Gennaio+frequenza policy): il DSS genera solo gli OdL successivi alla data “a partire da”.

Quanto appena definito è valido per tutte le tipologie di policy periodiche di seguito descritte.

Policy periodiche – standard

Le policy periodiche – standard sono policy facenti parte della tipologia “periodiche” e caratterizzate dall’assenza di altre logiche di elaborazione. In particolare:

• non hanno in output attività di sostituzione su conduttore e fune di guardia.

• non sono riferite a tronchi di ispezione.

• non hanno il flag “Policy aggregata?” attivo.

Allo scattare di una policy periodica standard, il DSS:

Per ogni singolo asset in modo indipendente, valuta gli input della policy singolarmente confrontandoli con gli eventuali target di raffronto.

Se gli input sono soddisfatti, seleziona l’attività (PdL) da associare all’ordine di lavoro coerentemente alla macro-attività definita nella policy.

Valuta l’idoneità dell’asset che soddisfano le condizioni in input.

Valuta la presenza a sistema di ordini per la stessa policy con medesimo PdL.

Valuta la presenza a sistema di ordini sull’asset con PdL prevalente o prevalso rispetto a quello dell’ordine in output alla policy.

Se la frequenza è superiore ad un anno genera un ordine ad asset. Se la frequenza è minore di un anno, genera N ordini all’anno.

Policy periodiche – trainante/trainato

Le policy periodiche – trainante/trainato sono policy facenti parte della tipologia “periodiche” e sono associate ad asset di tipo conduttore o fune di guardia con in output un’attività di tipo sostituzione.

Allo scattare di una policy periodiche – trainante/trainato, il DSS:

Per ogni singolo asset in modo indipendente, valuta gli input della policy singolarmente confrontandoli con gli eventuali target di raffronto.

Se gli input sono soddisfatti, seleziona l’attività (PdL) da associare all’ordine di lavoro coerentemente alla macro-attività definita nella policy.

Valuta l’idoneità dell’asset che soddisfano le condizioni in input.

Valuta la presenza a sistema di ordini per la stessa policy con medesimo PdL.

Determina la presenza della tratta di tesatura. In caso di esito negativo, viene eseguita un’elaborazione standard per il solo asset che soddisfa le condizioni in input. In caso di esito positivo, sincronizza le esecuzioni degli altri asset che appartengono alla stessa tratta di tesatura.

Valuta la presenza a sistema di ordini sull’asset con PdL prevalente o prevalso rispetto a quello dell’ordine in output alla policy.

Genera gli ordini: vengono creati N OdL (sulla base della frequenza) per tutti gli asset che appartengono alla tratta di tesatura. Tutti gli OdL i cui asset soddisfano gli input della policy saranno caratterizzati dall’attributo “Trainante”, gli altri dall’attributo “Trainato”.

Policy periodiche – tronco di ispezione

Le policy periodiche – tronco di ispezione sono policy caratterizzate dal flag “Tronco di ispezione” attivo e insistono su campate aeree o tratte cavo (secondo livello).

Allo scattare di una policy periodica – tronco di ispezione, il DSS:

Per ogni singolo asset in modo indipendente, valuta gli input della policy singolarmente confrontandoli con gli eventuali target di raffronto.

Se gli input sono soddisfatti, seleziona l’attività (PdL) da associare all’ordine di lavoro coerentemente alla macro-attività definita nella policy.

Valuta l’idoneità dell’asset che soddisfano le condizioni in input.

Valuta la presenza a sistema di ordini per la stessa policy con medesimo PdL.

Determina la presenza di un tronco di ispezione associato agli asset che soddisfano le condizioni in input. In caso di esito negativo, l’elaborazione per l’asset in questione termina senza successo. Per tutti gli asset che, invece, hanno un tronco d’ispezione, il DSS sincronizza tutte le elaborazioni e le aggrega per tronco.

Valuta la presenza a sistema di ordini sull’asset con PdL prevalente o prevalso rispetto a quello dell’ordine in output alla policy.

Genera gli ordini: nel caso di frequenza inferiore ad un anno, per ogni tronco vengono generati N ordini aggregatori (data frequenza) associati alla linea relativa alle campate aeree o tratte cavo sulla quali è scattata la policy. Sotto ogni ordine aggregatore, vengono aggregati gli N ordini figli associati a tutte le campate aeree o tratte cavo (secondi livelli della linea) riferite al singolo tronco di ispezione. Nel caso di frequenza superiore all’anno, viene generato un ordine aggregatore sulla linea e vengono aggregati sotto gli N OdL figli riferiti alle campate aeree o tratte cavo del tronco.

## 3.4 Eventi che generano le elaborazioni del DSS

In questa sezione verranno descritti gli eventi trigger che scatenano il calcolo delle policy da parte del DSS, evidenziando, per ciascun evento, l’eventuale intervento dell’utente.

Gli eventi possono essere suddivisi in:

Eventi riguardanti l’anagrafica degli asset

Eventi riguardanti i cataloghi

Eventi di processo

Eventi temporali

### 3.4.1 Eventi anagrafica asset

Modifica attributo di zonizzazione

La modifica di un attributo di zonizzazione (LIN_ZONA_AMBIENTALE, LIN_ZONA_ANNO_SA, LIN_ZONA_ANNO_SB) determina lo scattare delle policy di zonizzazione. Il DSS individua gli ordini che sono già stati generati da una policy di zonizzazione e che hanno come PdL la stessa attività output alla policy che scatta sulla base del nuovo valore assunto dall’attributo.

Il DSS modifica gli ordini già creati sulla base della nuova frequenza indicata nella policy di zonizzazione avente il nuovo valore dell’attributo se essa è già presente a catalogo;

Il DSS genera nuovi ordini nel caso in cui il nuovo valore dell’attributo di zonizzazione LIN_ZONA_AMBIENTALE indicato sull’asset corrisponda al target di una nuova policy che non era già presente a sistema. Questa casistica è valida solo per l’attributo di zonizzazione LIN_ZONA_AMBIENTALE per le linee in cavo. Gli ordini già presenti vengono posti tutti in stato “non eseguito” e ne vengono generati di nuovi secondo la frequenza della nuova policy.

Per le policy di zonizzazione applicate a un tronco, la modifica dell’attributo su un singolo asset del tronco non è sufficiente a scatenare l’elaborazione della policy. In questo caso, il sistema valuta la coerenza degli attributi di zonizzazione su tutti gli asset appartenenti allo stesso tronco:

Se la modifica dell’attributo è estesa a tutti gli asset del tronco, viene attivata l’elaborazione della policy su tutti gli asset.

In caso contrario, l’elaborazione non viene avviata.

Inserimento in anagrafica di un nuovo asset

L’inserimento in anagrafica di un nuovo asset innesca l’elaborazione di tutte le policy, ad eccezione delle policy aggregate, che sono associate alla stessa tipologia elemento dell’asset appena inserito. Le policy individuate saranno quindi valutate, senza un ordine predefinito, solamente sul nuovo asset inserito.

Modifica di un asset esistente o del suo stato

Alla modifica di un asset già presente in anagrafica o del suo stato, il DSS innesca la valutazione delle policy aventi quella tipologia di asset solo per l’asset in esame e, se soddisfatte le condizioni in input e il nuovo stato è idoneo alla generazione, genera l’ordine secondo le logiche descritte.

Modifica idoneità ed eccezione stati asset

All’interno dell’applicazione “Catalogo policy” è presente la tabella di idoneità ed eccezione stati asset. A seguito di una modifica della stessa, il DSS scatena l’elaborazione delle policy interessate dalla modifica per valutare l’eventuale presenza di asset ora in stato idoneo.

### 3.4.2 Eventi sui cataloghi

Modifica di una policy a catalogo

Gli utenti di profilo P1 hanno la possibilità di modificare una policy a catalogo. La modifica scatena la valutazione da parte del DSS per la propagazione delle modifiche effettuate.

L’utente ha la possibilità di modificare i seguenti campi della policy:

Frequenza (solo per le policy periodiche)

Differibilità (solo per le policy on condition)

Max anticipo (solo per le policy on condition)

Max posticipo

Figura - vista policy on condition con evidenza dei campi "Differibilità", "Max anticipo", "Max posticipo"

Figura - vista policy periodica con evidenza dei campi "Frequenza" e "Max posticipo"

Nel caso di modifica di una policy on condition il DSS valuta la presenza a sistema di ordini per la policy in esame e il relativo stato:

Se è già presente un OdL in stato “APERTO” o “DA VALIDARE” il DSS propaga le modifiche effettuate sull’ordine esistente. Nel caso di policy trainante/trainato o tronco di ispezione, il DSS apporta le modifiche a tutti gli OdL della tratta o del tronco;

Se è già presente un OdL in stato diverso da “APERTO” o “DA VALIDARE” il DSS non propaga le modifiche effettuate sull’ordine esistente e non crea nuovi ordini per quell’asset;

Se non è già presente un OdL, il DSS esegue una nuova elaborazione (standard, trainante/trainato o tronco di ispezione) e crea gli ordini per gli asset in esame se soddisfatte le condizioni in input.

Nel caso di modifica di una policy periodica, il DSS ricerca a sistema gli ordini già creati per quella policy in stato “APERTO”:

Se la frequenza aumenta, il DSS modifica le date degli ordini già presenti a sistema e genera N nuovi ordini sulla base della nuova frequenza definita. Nel caso di policy trainante/trainato o tronco di ispezione, il DSS estende tale logica a tutta la tratta di tesatura o il tronco di ispezione in questione.

Se la frequenza diminuisce, il DSS non genera nuovi ordini ma modifica le date modello di quelli esistenti e porta in stato “Non eseguito” quelli in eccesso. Nel caso di policy trainante/trainato o tronco di ispezione, la logica si estende a tutti gli ordini della tratta o del tronco.

Inserimento a catalogo di una nuova policy

Gli utenti di profilo P1 hanno la possibilità di creare una nuova policy a catalogo. La creazione di una nuova policy scatena l’elaborazione della stessa da parte del DSS su tutti gli asset presenti a sistema della tipologia elemento associata alla policy e la generazione degli ordini per gli asset che soddisfano le condizioni in input, secondo le logiche definite.

Modifica catalogo modelli target

Gli utenti di profilo P1 hanno la possibilità di apportare modifiche al catalogo dei modelli target. La modifica di un record nel catalogo modelli target innesca la valutazione delle policy da parte del DSS che hanno quel modello come target di raffronto in input.

### 3.4.3 Eventi di processo

Gli eventi di seguito descritti fanno riferimento solamente alla casistica delle policy on condition.

Generazione di un Ordine

All’interno della piattaforma, un nuovo ordine può essere creato manualmente da parte degli utenti di profilo P1, P3, P5, P6, P16, P7, P8, P17, P11, secondo le autorizzazioni di sistema, oppure automaticamente dal DSS. In entrambi casi, se l’ordine creato è in stato “APERTO” o successivi (SCHEDULABILE, SCHEDULATO, INOLTRATO, AVVIATO) ed ha come PdL un’attività che è input di una policy a sistema, il DSS valuta la stessa per l’asset in esame e genera l’ordine solo se rispettate tutte le condizioni in input e le logiche descritte.

Registrazione difformità, interferenze o misure

Le difformità, interferenze e misure possono essere inserite come input solamente per le policy on condition. Gli utenti di profilo P1, P16, P7, P8, P17 e P9 hanno la possibilità di registrare una nuova difformità, interferenza o misura sia su EAM Mobile che da web. In entrambi i casi, la registrazione e successiva validazione di una nuova difformità e interferenza o la rilevazione di una nuova misura, innesca l’elaborazione del DSS per tutte le policy che hanno quegli input specifici. Il DSS valuta le policy interessate per l’asset in esame e genera l’ordine solo se rispettate tutte le condizioni in input e le logiche descritte.

Ricalcolo valore HI

Il valore degli HI viene ricalcolato quotidianamente dal sistema. Nel caso di variazione del valore, il DSS innesca la ricerca delle policy aventi in input quell’indicatore di condizione tecnica ed effettua l’elaborazione solamente per gli asset che hanno subito la variazione. L’ordine viene creato solo se rispettate tutte le condizioni in input e le logiche descritte.

Aggiornamento variabile di stato

Ogni volta che il valore di una variabile di stato subisce una variazione, il DSS innesca la ricerca delle policy aventi in input quella variabile ed effettua l’elaborazione solamente per gli asset che hanno subito la variazione. L’ordine viene creato solo se sono rispettate tutte le condizioni in input e le logiche descritte.

Chiusura ordine periodico inter-annuale

Alla chiusura di un ordine periodico inter-annuale precedentemente creato dal DSS, si innesca il rilancio della policy che lo ha generato per il solo asset dell’OdL chiuso. Il DSS verifica che la policy in questione abbia ancora frequenza superiore all’anno e applica le logiche descritte per la rigenerazione dell’ordine. Nel caso specifico di policy periodica interannuale tronco di ispezione, il DSS rigira su tutti gli asset del tronco.

### 3.4.4 Evento temporale

Per le policy periodiche l’evento di innesco è di tipo temporale. Si definisce a sistema una data di innesco. Raggiunta tale data avviene un’elaborazione massiva da parte del DSS di tutte le policy attive a sistema di tipologia “Periodica” senza un ordine prestabilito.

L’evento temporale può innescare esclusivamente le elaborazioni di seguito riportate:

Elaborazione policy periodiche – standard;

Elaborazione policy periodiche – con attributo zonizzazione.

Elaborazione policy periodiche – tronco di ispezione

## 3.5 Monitoraggio elaborazioni DSS

Il DSS innesca le elaborazioni delle policy ed esegue le attività di generazione, modifica e cambio stato degli ordini automaticamente. All’interno della piattaforma, però, l’utente ha la possibilità di monitorare puntualmente le run avviate del DSS in due modalità:

Accedendo al centro di controllo

Dall’applicazione dedicata “Policy e run DSS”

### 3.5.1 Centro di controllo

La sezione del centro di controllo è abilitata esclusivamente agli utenti con profilo P1, secondo le autorizzazioni di sistema e per accedervi l’utente è tenuto a seguire i passaggi di seguito descritti.

Accedere al centro di controllo: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Centro di controllo”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Centro di controllo"

Accedere al tab di interesse: all’interno del centro di controllo, l’utente è tenuto ad accedere al tab “Elaborazioni DSS”.

Figura - vista sezione "Elaborazioni DSS" nel centro di controllo

Consultare la tabella “Policy e run DSS”: all’interno della tabella “Policy e run DSS” evidenziata in figura è riportato l’elenco di tutte le esecuzioni del DSS caratterizzate da un ID Run DSS univoco, con specifica dell’ID della relativa policy e la data del run. È possibile filtrare per i campi presenti in testata per ricercare l’elaborazione di interesse.

Cliccando sul singolo record, l’utente viene indirizzato alla corrispondente elaborazione all’interno dell’applicazione “Policy e run DSS” dove è possibile effettuare un controllo maggiormente approfondito.

Figura - tabella "Elaborazioni DSS" all'interno del centro di controllo

### 3.5.2 Applicazione “Policy e run DSS”

Per effettuare un monitoraggio puntale delle singole elaborazioni del DSS, l’utente è tenuto a seguire i passaggi di seguito descritti.

Accedere all’applicazione “Policy e run DSS”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Amministrazione” e successivamente l’opzione “Policy e run DSS”. In alternativa, può inserire nel campo di ricerca l’opzione “Policy e run DSS” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Policy e run DSS"

Selezionare il run di interesse: dalla vista elenco cliccare sul record corrispondente all’ID Run DSS di cui si intende monitorare l’elaborazione. È possibile filtrare per i campi presenti in testata: ID Run DSS, ID policy, Data di primo run DSS.

Figura - vista elenco applicazione "Policy e run DSS"

Consultare le tabelle: dopo aver cliccato sull’ID Run DSS di interesse, l’utente ha la possibilità di consultare le relative tabelle “Elaborazioni DSS avviate e non portate a termine” e “Elaborazioni DSS portate a termine”.

Nella tabella “Elaborazioni DSS avviate e non portate a termine” sono riportate le elaborazioni avviate ma andate in errore. Per ognuna vengono riportati: la policy, il PdL, l’asset, la data di elaborazione e l’errore riscontrato con il relativo codice. È possibile filtrare per tutti i campi presenti in testata, ad eccezione del campo “ID Macro-attività” per ricercare il singolo record di interesse.

Nella tabella “Elaborazioni DSS portate a termine” sono riportate le elaborazioni non andate in errore, il che non implica la creazione dell’ordine, ma solamente la corretta elaborazione da parte del DSS. Per ogni record sono sempre compilati i campi ID policy, codice asset, descrizione asset e data di elaborazione policy. Solo nel caso di effettiva creazione dell’ordine sono popolati i campi “ID PdL associato all'ordine output alla policy” e “ID ordine output alla policy”.

Figura - vista di un'elaborazione del DSS, con evidenza delle tabelle “Elaborazioni DSS avviate e non portate a termine” e “Elaborazioni DSS portate a termine”

Oltre alle attività di monitoraggio, all’interno dell’applicazione “Policy e run DSS” è presente la funzionalità “Riprocessa Run DSS” evidenziata in figura. Tale funzionalità, abilitata per gli utenti P1 secondo le autorizzazioni di sistema, consente all’utente di effettuare un riprocessamento esclusivamente delle operazioni andate in errore.

Figura - vista di un'elaborazione del DSS con evidenza della funzionalità "Riprocessa Run DSS"

# 4. PROCESSO DI PIANO

Nei paragrafi che seguono verranno descritti in dettaglio i processi di gestione della capacity, dei CMO, del flag PMI e della generazione e gestione degli OdL serbatoio. Si tratta di step fondamentali per la produzione del PMI: ciascuno di essi contribuisce a garantirne una corretta impostazione tecnica ed economica, oltre a un'esecuzione strutturata delle attività previste.

## 4.1 Gestione della capacity per UdB

Prima di poter assegnare delle risorse interne da pianificare all’interno degli ordini di lavoro, è fondamentale che ogni figura professionale che si vuole utilizzare abbia una capacity disponibile per l’unità di budget di interesse. Nello specifico, la gestione della capacity si effettua nell’applicazione “Capacity” di EAM, a seconda dell’anno e dell’unità di budget (UdB) di riferimento.

I paragrafi successivi descrivono le diverse casistiche associate alla gestione della capacity in EAM.

L’applicazione “Capacity” di EAM è strutturata nei seguenti due livelli:

Vista iniziale: La schermata di accesso dell’applicazione “Capacity” è composta dalle seguenti informazioni:

Titris: indica l’UdB titolare delle risorse

Anno: anno di riferimento della capacity

Scenario: codice identificativo dello scenario di riferimento

Esempio: 2024F1O_25 -> Forecast 2025 prodotto nel 2024

Stato: Impostato dagli utenti centrali e può assumere i seguenti valori:

Non modificabile da udb: solo gli utenti centrali possono effettuare modifiche

Modificabile da udb: in questo stato anche gli altri profili abilitati alla gestione della capacity possono effettuare modifiche

L’inserimento degli stati sopra descritti avviene cliccando sull’azione laterale “Cambio stato” abilitata ai soli utenti centrali. Una volta cliccato su tale funzionalità appare una dialog all’interno della quale è possibile selezionare i record rappresentanti le capacity di una determinata UdB e di uno specifico anno (è possibile effettuare una selezione massiva tramite un “select all” oppure una selezione dei singoli record). Sempre all’interno della dialog è presente un campo “Stato” tramite il quale si seleziona lo stato (“modificabile da udb” o “non modificabile da udb”) che si vuole impostare per i record selezionati.

Data modifica: l’ultima data in cui la capacity è stata modificata

Modificato da: matricola dell’utente che ha eseguito l’ultima modifica.

Figura - Vista iniziale dell'applicazione "Capacity"

Tabella - Tabella contenente i campi dell’applicazione capacity, loro modalità di valorizzazione e obbligatorietà

Selezionando un record dalla vista elenco precedentemente descritta, l’utente viene reindirizzato al tab “Capacity iniziale”, contenete le seguenti informazioni:

Titris: udb di riferimento della capacity. In fase di creazione di una nuova capacity è un campo obbligatorio popolabile manualmente

Scenario: Campo popolato automaticamente contente lo scenario di riferimento importato da Tagetik

Anno: anno di riferimento della capacity. In fase di creazione di una nuova capacity è un campo obbligatorio popolabile manualmente

Data ultima modifica: Campo popolato automaticamente con la data dell’ultima modifica effettuata sulle tabelle di Capacity operaio/impiegato/quadro

Modificato da: Campo popolato automaticamente con la matricola di chi ha effettuato l’ultima modifica sulle tabelle di Capacity operaio/impiegato/quadro

Totale capacity operaio/impiegato/quadro: campi popolati automaticamente con la somma delle ore riportate rispettivamente nelle tabelle di capacity operario, impiegato e quadro

Capacity operario/impiegato/quadro: tabelle contenenti le capacity mensilizzate delle 3 figure professionali operaio, impiegato e quadro. Queste tabelle, come descritto nel paragrafo successivo, risultano modificabili sia manualmente che tramite import.

Tab "Capacity iniziale" dell'applicazione Capacity

### 4.1.1 Inserimento capacity

Nei seguenti sottoparagrafi vengono descritti le diverse modalità di popolamento delle tabelle “Capacity operaio/impiegato/quadro” del tab “Capacity iniziale”.

###### 4.1.1.1 Import della capacity da Tagetik

L’import della capacity da Tagetik è una funzionalità riservata esclusivamente agli utenti centrali. Di seguito gli step da seguire per effettuare l’import:

Accedere all’applicazione “Capacity”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Pianificazione” e successivamente “Capacity”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Capacity”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Capacity"

Selezionare la funzione di import: all’interno della vista iniziale dell’applicazione “Capacity”, l’utente seleziona l’azione “Import”; successivamente, nella dialog restituita, è necessario inserire il titris e lo scenario di riferimento della capacity. Una volta cliccato su “OK” vengono automaticamente popolate le tabelle di capacity impiegato/quadro/operaio.

Figura - Vista iniziale dell'applicazione "Capacity", con evidenza all'azione di import da Tagetik

Figura - Dialog di definizione dei parametri di import della capacity

Nota: Tramite questa funzionalità è possibile importare sia le capacity di tutte le UdB (lasciando il campo titris vuoto) sia la capacity di una specifica udb (popolando il campo titris con l’udb di interesse). Inoltre, i valori visualizzati nel campo “Scenario” corrispondono agli scenari acquisiti da Tagetik tramite l’apposita funzionalità di Import Scenari.

Verificare l’inserimento della capacity: nell’app “Capacity”, all’interno del tab “Capacity iniziale” può verificare la presenza delle ore nelle tabelle “Capacity Operaio”, “Capacity Impiegato”, “Capacity Quadro”.

Figura - Tab "Capacity iniziale" dell'applicazione "Capacity" contenente la capacity importata da Tagetik

###### 4.1.1.2 Import tramite file Excel (MXLoader)

Per il popolamento massivo delle tabelle di capacity è possibile utilizzare anche lo strumento MXLoader, seguendo la procedura descritta di seguito.

Accedere al file MxLoader e verificare la connessione: All’interno del foglio 'Config' dell’MXLoader, inserire l’ApiKey (la chiave alfanumerica fornita all’utente abilitato) nel campo 'Password' e connettere il tool a EAM cliccando sul tasto 'Test Connection'.

Figura - Tab "Config" dello strumento "MxLoader", con evidenza al tasto "Test connection"

Figura - Tab "Config" dello strumento "MxLoader", con evidenza all’avviso di connessione andata a buon fine

Impostare il campo “C1” e cercare le UdB di interesse: selezionare il campo “C1” del tab “CAPACITY” e impostare il valore in “Query”. Prima di procedere alla modifica massiva, l’utente deve indicare nell’MxLoader le UdB con delle capacity da modificare. Nel campo “D1”, va inserita la query con le UdB da modificare, rispettando la seguente sintassi:

UDB="XXX" and ANNO=XXX

Dopo aver inserito la query nel campo “D1”, l’utente seleziona il tasto “Run” per visualizzare i record di capacity da modificare.

Figura - Tab "CAPACITY" dello strumento "MxLoader", con evidenza alla cella “D1” con l’impostazione richiesta per la ricerca delle UdB da modificare e al tasto “Run”

Modificare i campi: all’interno del tab “Capacity”, l’utente può modificare le capacity desiderate, presenti nella colonna “X4CAPACITYDETAILS.ORE”. Nello specifico, deve essere rispettata la formattazione

Figura- Tab "CAPACITY" dello strumento "MxLoader", con evidenza alle capacity modificate

Impostare il campo “C1” e fare il Run: selezionare il campo “C1” del tab “CAPACITY” ed impostarlo in “AddChange” e fare il “Run” per effettuare la modifica massiva della capacity dell’UdB

Figura - Tab "CAPACITY" dello strumento "MxLoader", con evidenza alla cella C1 da impostare su “AddChange” e al tasto “Run”

Verificare l’inserimento di nuove capacity: nell’app “Capacity”, all’interno del tab “Capacity iniziale”, l’utente può verificare la presenza delle ore nelle tabelle “Capacity Operaio”, “Capacity Impiegato”, “Capacity Quadro”.

Figura- Vista di dettaglio della capacity dell'UdB "DTNOR-UCS" dopo l'import delle capacity tramite il file MXLoader

###### 4.1.1.3 Gestione manuale

In EAM è prevista anche la possibilità di gestire manualmente la capacity attribuita ad una determinata UdB. Di seguito gli step da seguire per eseguire la modifica manuale:

Accedere all’applicazione “Capacity”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Pianificazione” e successivamente “Capacity”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Capacity”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Capacity"

Selezionare l’UdB di interesse: cliccando sull’icona di filtro, l’utente può individuare l’UdB per la quale vuole realizzare delle modifiche di capacity. Se necessario, è possibile filtrare anche per anno di riferimento e scenario.

Figura - Vista elenco iniziale dell'applicazione "Capacity", con evidenza all'icone di filtro di ricerca

Effettuare le modifiche necessarie: Inserire i valori desiderati all’interno delle tabelle di capacity operaio/impiegato/quadro

Figura - Vista di dettaglio della capacity mensile dell'UdB "DTCNR-UBO" per l'anno 2025

Salvare le modifiche: Salvare le modifiche tramite l’icona di salvataggio. Una volta salvata la modifica, il sistema aggiorna automaticamente l’informazione presente nei campi “Data ultima modifica” e “Modificato da” rispettivamente con la data e la matricola dell’utente che ha effettuato la modifica.

Figura - Vista di dettaglio della capacity mensile dell'UdB "DTCNR-UBO" per l'anno 2025, con evidenza alla capacity modificata manualmente e ai campi “Data ultima modifica” e “Modificato da”

### 4.1.2 Verifica della saturazione di capacity

Nella piattaforma EAM, l’utente abilitato ha la possibilità di analizzare il livello di saturazione della capacity, determinata esclusivamente dagli ordini PMI.

Per verificare la saturazione della capacity di una UdB, è necessario eseguire i seguenti step:

Accedere all’applicazione “Capacity”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Pianificazione” e successivamente “Capacity”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Capacity”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Capacity"

Selezionare l’UdB di interesse: cliccando sull’icona di filtro, l’utente può individuare l’UdB di interesse

Figura - Vista elenco iniziale dell'applicazione "Capacity", con evidenza all'icone di filtro di ricerca

Verificare la saturazione della capacity: all’interno del tab “Monitoraggio capacity” della vista di dettaglio della capacity, l’utente ha a disposizione le tabelle “Teorico”, “Pianificato” e “Check” per ogni figura professionale. Queste tabelle consentono all’utente di analizzare in modo esaustivo la saturazione della capacity della specifica UdB.

Di seguito il dettaglio dei campi:

Tabella “Teorico”:

Mese;

Capacity: Capacity iniziale (inserita nel tab di inserimento della capacity);

Proprio teorico: Capacity – Ceduto teorico;

Ceduto teorico: Ore cedute ad altre UdB nel processo di condivisione degli scambi intracompany. Vengono contate solo le ore cedute che sono state accettate dall'UdB ricevente

Ricevuto teorico: Ore ricevute da altre UdB nel processo di condivisione degli scambi intracompany. Vengono contate solo le ore ricevute che sono state accettate dall'UdB ricevente;

Forza lavoro teorica: Proprio teorico + Ricevuto teorico.

Figura - Tabella "Teorico Impiegati" all'interno del tab "Monitoraggio capacity"

Tabella “Pianificato”:

Mese

Proprio pianificato puntuale: Somma delle ore assorbite negli OdL che insistono sulla propria UdB dal personale dell’UdB flaggate “PMI”. Molti OdL potrebbero essere a cavallo mese o cavallo anno. In questi casi l’effort orario viene distribuito proporzionalmente all’elapsed pianificato nei vari mesi. Es: Un OdL necessita di 100 ore Operaio con data inizio pianificata il 15 Marzo e data fine pianificata il 30 Aprile. In questo caso 34 ore (1/3 del totale) verranno assegnate al mese di Marzo mentre le restanti 66 (2/3 del totale) al mese di Aprile. Si specifica che per il calcolo di questo campo vengono considerati gli OdL in stato:

Aperto

Schedulabile

Schedulato

Inviato/Inoltrato

Avviato

Eseguito parzialmente

Eseguito

Chiuso

Ceduto pianificato: Somma delle ore assorbite negli OdL PMI di altre UdB dal personale della propria UdB;

Saturazione pianificata: (Proprio pianificato + Ceduto pianificato) / Capacity;

Conto base residuo pianificato: Capacity – Proprio pianificato – Ceduto pianificato;

Ricevuto pianificato: Somma delle ore, assorbite negli OdL PMI della propria UdB, ricevute da altre UdB;

Forza lavoro pianificata: Proprio pianificato + Ricevuto pianificato.

Da allocare a serbatoio: Campo valorizzato manualmente dall’utente. Al tempo t0 la colonna è vuota; viene valorizzata successivamente per abilitare la funzione di creazione di OdL serbatoio (cfr. par. 4.5.2);

Figura - Tabella "Pianificato Impiegati" all'interno del tab "Monitoraggio capacity"

Tabella “Check”:

Ceduto teorico: stesso valore presente nella tabella “Teorico”;

Ceduto pianificato: stesso valore presente nella tabella “Pianificato”;

Ceduto da pianificare: Ceduto teorico – Ceduto pianificato. Rappresentano le ore che, seppur condivise nella maschera degli scambi intracompany, non risultano ancora assorbite dall'unità ricevente;

Ricevuto teorico: stesso valore presente nella tabella “Teorico”;

Ricevuto pianificato: stesso valore presente nella tabella “Pianificato”;

Ricevuto da pianificare: Ricevuto teorico – Ricevuto pianificato. Rappresentano le ore che, seppur condivise nella maschera degli scambi intracompany, non risultano ancora assorbite dalla propria UdB.

Figura - Tabella "Check Impiegati" all'interno del tab "Monitoraggio capacity"

### 4.1.3 Gestione scambio ore da UdB

All’interno della piattaforma EAM è prevista la possibilità di scambiare delle ore di risorse (Impiegato, quadro, operaio) relative allo stesso anno tra le diverse UdB. Nello specifico, questa possibilità è utile nel caso in cui la capacity assegnata ad un’UdB non sia sufficiente alla esecuzione delle attività pianificate per un determinato mese. Per eseguire lo scambio di capacity, è necessario seguire gli step sotto riportati:

Accedere all’applicazione “Capacity”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Pianificazione” e successivamente “Capacity”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Capacity”.

Figura- Lista di applicazioni di EAM, con evidenza all'applicazione "Capacity"

Selezionare l’UdB cedente: utilizzando il filtro di ricerca presente in alto a destra nella vista iniziale, l’utente può individuare e selezionare l’UdB che cederà delle ore lavoro ad un’altra UdB.

Figura - Vista elenco iniziale dell'applicazione "Capacity", con evidenza all'icone di filtro di ricerca

Registrare le ore cedute: all’interno del tab “Scambio ore”, l’utente deve inserire le informazioni relative alla capacity da cedere. Nella tabella “Ore in uscita” dopo la selezione del tasto “+”, è necessario valorizzare i seguenti campi:

Natura: figura professionale associata alle ore da cedere. Il campo ammette solo ed esclusivamente i valori “OPERAIO”, “QUADRO”, “IMPIEGATO”.

Quantità proposta: quantità di ore cedute proposte

Quantità accettata: quantità di ore accettate dall’UdB ricevente. Di default, il campo viene popolato con il valore inserito nel campo “Quantità proposta”. Qualora non siano accettate tutte le ore proposte, il sistema aggiorna automaticamente il campo.

Mese: mese riferito alla capacity ceduta

Responsabile attività destinatario: UdB alla quale si vuole prestare capacity

Stato: stato attuale della transazione. Al momento della creazione il sistema imposta il campo con il valore “PROPOSTA”

Figura - Tabelle "Ore in entrata" e "Ore in uscita" all'interno del Tab "Scambio ore", con evidenza all tasto di aggiunta di nuova proposta di scambio ore

Salvare la proposta: selezionando l’icona evidenziata in Figura , l’utente salva la proposta di scambio ore e le ore registrate diventano visibili dall’UdB ricevente (DTCNR-UFI).

Figura - Tabelle "Ore in entrata" e "Ore in uscita" all'interno del Tab "Scambio ore", con evidenza alla nuova proposta di scambio ore e all’icona di salvataggio della proposta

Selezionare l’UdB ricevente: successivamente alla registrazione delle ore da cedere, è necessario valutare la proposta di scambio capacity lato UdB ricevente.

Figura - Vista elenco iniziale dell'applicazione "Capacity", filtrata per l’UdB di destinazione delle ore proposte

Valutare la proposta: all’interno del tab “Scambio ore”, l’utente visualizza nella tabella “Ore in entrata” la proposta registrata nello step precedente. Per confermare lo scambio di capacity, l’utente deve selezionare la lente di ingrandimento ed impostare lo stato in “ACCETTATA”. Se accettata la proposta, è possibile definire la quantità di ore ricevute modificando il valore della colonna “Quantità accettata”. In alternativa, l’utente può anche rifiutare la proposta impostando lo stato in “RIFIUTATA”.

Figura - Tabella "Ore in entrata" all'interno del tab "Scambio ore" (vista dell'UdB di destinazione)

Salvare la proposta: dopo aver impostato lo stato della proposta in “ACCETTATA” o “RIFIUTATA”, l’operatore deve selezionare l’icona per confermare la ricezione o rifiuto dello scambio ore.

Figura - Tabelle "Ore in entrata all'interno del Tab "Scambio ore" (vista UdB di destinazione), con evidenza allo stato “Accettata” e all’icona di salvataggio della proposta

### 4.1.4 Centro di controllo per la gestione della capacity

Questo paragrafo descrive il Centro di Controllo “Monitoraggio capacity” di EAM, l'area applicativa dedicata al monitoraggio della saturazione della capacity e alla gestione degli scambi pendenti. Attraverso questa funzionalità, gli utenti abilitati possono disporre di una vista centralizzata per controllare lo stato della capacity, individuare eventuali situazioni di criticità e monitorare le richieste di scambio ancora in corso di lavorazione.

Per utilizzare la sezione “Centro di Controllo”, l'utente deve attenersi alla seguente procedura:

Accedere all’applicazione dedicata al centro di controllo: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Centro di controllo”. In alternativa, è possibile inserire nel campo di ricerca l’opzione “Centro di controllo” per essere indirizzati all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Centro di controllo"

Selezionare la sezione desiderata del centro di controllo: dopo aver selezionato “Centro di controllo”, l’utente deve cliccare sul tab “Monitoraggio capacity” per accedere alla sezione dedicata.

La sezione del Centro di Controllo dedicata alla gestione della capacity presenta due viste distinte, descritte di seguito:

Sovrasaturazione capacity: Al fine di supportare gli utenti centrali nell’individuazione delle situazioni di sovrasaturazione della capacity, il Centro di Controllo mette a disposizione una specifica sezione dedicata al monitoraggio delle UdB. In tale sezione è riportato l’elenco delle UdB che presentano un livello di saturazione superiore al 100%, consentendo agli utenti di identificare tempestivamente le situazioni di criticità e di intraprendere le opportune azioni di gestione.

Capacity con scambi pendenti: questa sezione elenca le UdB con proposte di scambio capacity ancora in attesa di approvazione.

Le informazioni relative alle sezioni “Sovrasaturazioni capacity” e “Capacity con scambi pendenti” sono distribuite nelle seguenti colonne precompilate:

UdB

Anno

Stato

All’interno delle tabelle, l’utente ha la possibilità di svolgere le seguenti operazioni:

Filtrare la ricerca: selezionando le icone di filtro, è possibile inserire il parametro desiderato in uno qualsiasi dei campi disponibili.

Selezionare il record: cliccando su un record presente in ciascuna colonna, l’utente viene automaticamente indirizzato alla vista di dettaglio dell’UdB corrispondente all’interno dell’applicazione “Capacity”.

Le figure sottostanti rappresentano le sezioni del centro di controllo sopra descritte

Figura - Sezione del centro di controllo dedicata alla verifica della sovrasaturazione della capacity, con evidenza all'icona di filtro della ricerca

Figura - Sezione del centro di controllo dedicata alla verifica della capacity con scambi pendenti, con evidenza all'icona di filtro della ricerca

## 4.2 Gestione delle tariffe (CMO)

I costi medi orari associati alle risorse interne (impiegati, quadri, operai) vengono gestite in EAM tramite l’applicazione “Gestione Tariffe”. La gestione delle tariffe è abilitata esclusivamente agli utenti centrali, secondo le autorizzazioni di sistema.

L’applicazione “Gestione tariffe” di EAM è strutturata come di seguito descritto:

Vista iniziale: la prima schermata alla quale l’utente ha accesso all’interno dell’applicazione presenta la seguenti informazioni:

Qualifica

Descrizione

Tariffa standard

Organizzazione

Figura - Vista iniziale dell'applicazione "Gestione tariffe"

### 4.2.1 Processo di gestione delle tariffe (CMO)

Il costo medio orario (CMO) viene attribuito automaticamente dal sistema alle risorse pianificate su un OdL e serve a definire il costo orario attribuito all’insieme di risorse che eseguiranno l’attività. Il CMO varia a seconda del Titris, anno di riferimento, figura professionale e scenario di pianificazione e, in EAM, viene gestito direttamente all’interno dell’applicazione “Gestione tariffe. Le tariffe possono essere gestite attraverso tre modalità diverse, ovvero da “Import massivo da Tagetik”; “Import da MxLoader” o “Gestione manuale”. Per gestire le tariffe all’interno di EAM l’utente deve eseguire i seguenti passaggi:

Accedere all’applicazione “Gestione tariffe”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve inserire direttamente nel campo di ricerca la voce “Gestione tariffe” per accedere all’applicazione di interesse.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Gestione tariffe"

###### 4.2.1.1 Import massivo da Tagetik

Di seguito vengono riportati gli step da seguire per l’import dei CMO da Tagetik:

Selezionare la funzionalità di import massivo: una volta nell’applicazione dedicata alla gestione delle tariffe, l’utente deve selezionare l’azione laterale di “Import massivo” per realizzare l’import. Cliccando sull’azione, il sistema apre la dialog dove l’utente deve definire il Titris e lo scenario per il quale vuole importare le tariffe.

Figura - Vista iniziale dell'applicazione “Gestione tariffe", con evidenza all'azione di import da Tagetik

Figura - Dialog di definizione dei parametri di import della capacity

Cliccando sul tasto “Ok”, il sistema procede ad importare le tariffe di tutte le figure professionali che fanno riferimento al Titris e allo scenario definito. L’import avviene per tutte le figure professionali e, per verificare se la procedura è andata a buon fine, l’utente deve selezionare dalla vista iniziale una qualsiasi figura professionale che vuole verificare. Se l’import è andato a buon fine, sarà possibile visualizzare la riga contenente lo scenario e l’UdB inseriti in precedenza, così come il costo medio orario associato.

Figura - Vista di dettaglio delle tariffe relative alla figura professionale “Operaio”, con evidenza alle informazioni importate da Tagetik

Nota: Tramite questa funzionalità è possibile importare sia le tariffe di tutte le UdB (lasciando il campo titris vuoto) che le tariffe di specifiche UdB (selezionando nella dialog solo le UdB di interesse).

###### 4.2.1.2 Import tramite file excel (MXLoader)

Per il popolamento massivo dei CMO è possibile utilizzare anche lo strumento MXLoader, seguendo la procedura descritta di seguito.

Accedere al file MxLoader e verificare la connessione: All’interno del foglio 'Config' dell’MXLoader, inserire l’ApiKey (la chiave alfanumerica fornita all’utente abilitato) nel campo 'Password' e connettere il tool a EAM cliccando sul tasto 'Test Connection'.

Figura - Tab "Config" dello strumento "MxLoader", con evidenza al tasto "Test connection"

Figura - Tab "Config" dello strumento "MxLoader", con evidenza all’avviso di connessione andata a buon fine

Impostare il campo “C1”: selezionare il campo “C1” del tab “CMO” ed impostare il campo in “AddChange”.

Figura - Tab "Config" dello strumento "MxLoader", con evidenza alla cella C1 da impostare su “Change”

Popolare i campi richiesti e fare il run: per realizzare l’import massivo delle tariffe usando MXLoader, l’utente deve popolare i seguenti campi:

CRAFT: figura professionale. Campo popolato con “OPERAIO”, “QUADRO”, “IMPIEGAT”

ORGID: campo deve essere sempre valorizzato con “ORG01”.

X4CRAFTPERYEAR.X4CRAFTPERYEARID: identificativo a DB

X4CRAFTPERYEAR.ANNO: anno di riferimento della tariffa

X4CRAFTPERYEAR.PAYRATE: costo medio orario della risorsa

X4CRAFTPERYEAR.UDB: unità di budget di riferimento della risorsa per la quale viene applicata la tariffa

Dopo aver popolato i campi richiesti, l’operatore deve selezionare il tasto “Run” per realizzare l’import massivo delle tariffe

Figura - Vista del tab "CMO" del file MXLoader, popolato con le informazioni da importate in EAM e con evidenza al tasto "Run"

Verificare l’inserimento di nuove tariffe: nell’app “Gestione tariffe”, all’interno del tab “Qualifica”, l’utente può verificare l’esito del caricamento.

Figura - Vista di dettaglio della tariffa associata alla figura professionale "Impiegato", con evidenza alla tariffa importata tramite MXLoader

###### 4.2.1.3 Gestione manuale

Per gestire manualmente le tariffe in EAM, bisogna accedere all’applicazione “Gestione tariffe” ed eseguire la seguente procedura:

Selezionare la figura professionale di interesse: all’interno della vista iniziale dell’applicazione, è possibile individuare la figura professionale per la quale si vuole gestire il CMO.

Figura - Vista iniziale dell'applicazione "Gestione tariffe"

Figura - Vista di dettaglio delle tariffe associate alla figura professionale "Operaio"

Effettuare delle modifiche: nella vista di dettaglio delle tariffe di una figura professionale, è possibile realizzare le seguenti azioni:

Aggiungere una tariffa: per creare una nuova tariffa, è necessario seguire i seguenti step:

Cliccare sull’icona “+”: per aggiungere una nuova riga nella tabella “Tariffario risorse”, l’operatore deve selezionare il tasto “+” evidenziato in Figura.

Compilare i seguenti campi richiesti:

Anno di riferimento: anno al quale la tariffa afferisce;

UdB di riferimento: unità impianto al quale la tariffa viene applicata;

Tariffa oraria: costo medio orario applicato attribuito alla figura professionale.

Salvare la nuova tariffa: dopo aver inserito le informazioni necessarie, l’utente clicca sull’icona di salvataggio, per confermare l’inserimento della nuova tariffa.

Figura - Vista di dettaglio delle tariffe associate alla figura professionale “Operaio”, con evidenza alla tariffa appena aggiunta manualmente e all’icona di salvataggio

Modificare una tariffa: per modificare una tariffa precedentemente registrata nell’applicazione, è necessario seguire i seguenti step:

Effettuare la modifica: per modificare un’informazione relativa alla tariffa registrata, l’utente deve semplicemente selezionare il campo che si vuole modificare ed inserire il nuovo dato. Nello specifico, EAM consente di modificare i seguenti campi:

Anno di riferimento:

UdB di riferimento:

Tariffa oraria:

Salvare la modifica: dopo aver modificato le informazioni necessarie, l’utente clicca sull’icona di salvataggio, per confermare la modifica.

Eliminare una tariffa: per eliminare una tariffa precedentemente salvata nell’applicazione, è necessario seguire i seguenti step:

Effettuare l’eliminazione: per eliminare una riga, l’utente deve cliccare sull’icona del cestino. Qualora sia necessario interrompere l’eliminazione, è possibile farlo selezionando l’icona di “torna indietro”.

Salvare l’eliminazione: dopo aver eliminato le tariffe necessarie, l’utente clicca sull’icona di salvataggio per confermare l’operazione.

Figura - Vista di dettaglio delle tariffe associate alla figura professionale “Operaio”, con evidenza dei tasti di eliminazione e salvataggio

## 4.3 Flag PMI

Tramite l’attivazione del flag PMI all’interno dell’OdL, è possibile indicare se l’attività fa parte del “Piano mantenimento impianti”. Nello specifico, vengono evidenziati i seguenti punti:

Per ordini aggregatori: è possibile riportare il flag PMI solamente sui figli e non sul padre, in quanto i figli in EAM sono il livello in cui si trovano le informazioni di pianificazione rilevanti per il PMI;

Per ordini singoli e figli: Flag PMI sempre visibile e editabile, non c’è alcuna restrizione basata sulla tipologia OdL e sul tipo spesa;

Al fine di monitorare e consultare le informazioni associate agli ordini PMI, è disponibile il report descritto nel paragrafo 11.2 “Avanzamento odl con informazioni di pianificazione risorse esterne e interne”.

Per attivare il flag PMI sono previste due seguenti possibilità, descritte nei paragrafi successivi.

### 4.3.1 Attivazione manuale

Per attivare il flag PMI in modalità manuale, l’utente deve seguire gli step riportati di seguito:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire la voce “Monitoraggio OdL” direttamente nel campo di ricerca.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare gli OdL di interesse: utilizzando il filtro di ricerca presente in alto a destra nella schermata, l’utente può individuare gli OdL per il quale vuole attivare il flag PMI. Una volta trovato, cliccare sugli OdL per accedere alla maschera di dettaglio dell’OdL.

Figura - Vista elenco iniziale dell'app "Monitoraggio OdL", con evidenza al filtro di ricerca degli OdL

Impostare il flag “PMI”: nella sezione inferiore del tab “Ordini di lavoro”, l’utente deve attivare il flag.

Figura - Schermata di dettaglio dell'OdL, con evidenza al flag "PMI"

### 4.3.2 Attivazione tramite MxLoader

Per impostare il flag PMI utilizzando lo strumento MXLoader dedicato, è necessario seguire gli step sotto riportati:

Accedere al file MxLoader e verificare la connessione: All’interno del foglio 'Config' dell’MXLoader dedicato alla modifica del flag PMI, inserire l’ApiKey (la chiave alfanumerica fornita all’utente abilitato) nel campo 'Password' e connettere il tool a EAM cliccando sul tasto 'Test Connection'.

Seguire le istruzioni riportate nello sheet “Guida”: per l’estrazione e l’aggiornamento dei dati tramite MXLoader, attenersi ai passaggi operativi descritti all’interno dello sheet “Guida”

Figura - Sheet “Guida”

## Generazione odl serbatoio

Nei paragrafi seguenti verranno descritte nel dettaglio le attività operative e le funzionalità applicative a disposizione degli utenti abilitati per la creazione e la gestione degli Ordini di Lavoro serbatoio.

### 4.4.1 Analisi del conto base residuo e inserimento delle ore da allocare sui serbatoi

Come descritto nel paragrafo 4.2.2, il conto base residuo pianificato rappresenta la quota di capacity mensile residua dell’UdB, ottenuta sottraendo alla capacity totale i valori associati al proprio pianificato e al ceduto pianificato.

Figura - Schema rappresentativo del processo di generazione degli OdL serbatoio

La verifica del conto base residuo avviene secondo quanto descritto di seguito:

Accedere all’applicazione “Capacity”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Pianificazione” e successivamente “Capacity”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Capacity”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Capacity"

Selezionare l’UdB di interesse: cliccando sull’icona di filtro l’utente può individuare l’UdB per la quale vuole realizzare la verifica del conto base residuo. Se necessario, è possibile filtrare anche per anno di riferimento e scenario.

Figura - Vista iniziale dell'applicazione "Capacity", con evidenza al filtro di ricerca

Accedere al tab “Monitoraggio capacity”: dopo aver selezionato il record di interesse, all’interno dell’applicazione “Capacity”, accedere al tab “Monitoraggio capacity” e verificare il conto base residuo mensilizzato per ogni figura professionale. Successivamente inserire manualmente la quantità di ore da allocare sui serbatoi nel campo “Da allocare a serbatoio”. Il sistema permetterà l’inserimento di un valore minore, maggiore o uguale rispetto a quanto riportato nel “conto base residuo pianificato”.

Figura - Vista di dettaglio dell'app "Capacity", con evidenza al tab "Monitoraggio capacity" e all'icona di visualizzazione delle tabelle di saturazione della capacity

Figura- Tabella "Pianificato impiegati" all'interno del Tab "Monitoraggio capacity", con evidenza alla colonna "Conto base residuo pianificato"

### 4.4.2 Ripartizione effort e creazione degli OdL serbatoio

Una volta verificato il valore del conto base residuo pianificato e definita all’interno dell’applicazione Capacity la quantità da allocare sui serbatoi, il processo di generazione degli Ordini di Lavoro serbatoio prosegue con la ripartizione dell’effort fisico e orario e con la successiva creazione degli OdL.

Figura - Schema rappresentativo del processo di generazione degli OdL serbatoio

Accedere all’applicazione “Gestione serbatoio”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Amministrazione” e successivamente “Gestione serbatoio”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Gestione serbatoio”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Gestione serbatoi"

Selezionare l’azione “Nuovo serbatoio”: all’interno dell’applicazione di gestione degli OdL serbatoio, cliccare sull’azione a sinistra “Nuovo serbatoio”, per essere indirizzato alla schermata dedicata alla generazione degli OdL serbatoio.

Figura - Vista iniziale dell'applicazione "Gestione serbatoi", con evidenza all'azione di generazione di nuovo serbatoio"

Compilare le informazioni richieste per la creazione della maschera di generazione dei serbatoi: nel tab “Serbatoio” valorizzare i seguenti campi:

Anno: anno di riferimento degli OdL serbatoio che si sta generando

UdB: UdB di riferimento degli OdL serbatoio che si sta generando

Figura - Maschera di generazione degli odl serbatoio

Valorizzare le tabelle di ripartizione effort: è possibile valorizzare le tabelle di ripartizione sia tramite GUI che tramite import di file excel

Valorizzazione manuale da GUI della tabella di ripartizione effort: selezionando il tasto “+” , il sistema crea una nuova riga nella tabella, dove inserire le seguenti informazioni:

Competenza: figura professionale da pianificare negli odl serbatoio. Selezionare un valore tra “OPERAIO”, “QUADRO” o “IMPIEGATO”;

Codice CoIn: codice CoIn da attribuire agli OdL serbatoio. Selezionare la CoIn tramite picklist

Società: società alla quale verranno attribuiti gli OdL serbatoio. Selezionare il valore da una lista contenente “TERNA”, o “RETE”.

Mese di riferimento: la tabella contiene una colonna per ogni mese dell’anno. L’utente deve inserire la quantità di ore di capacity assegnata all’OdL serbatoio per ogni mese. Il sistema permetterà il salvataggio dei dati solo se la somma dei valori inseriti per ciascun mese e figura professionale risulterà minore o uguale al valore indicato nella colonna “da allocare a serbatoio” dell’applicazione “Capacity”

Totale: colonna popolata automaticamente. Rappresenta la sommatoria delle ore inserite per ciascuna riga

Valorizzazione manuale da GUI della tabella di ripartizione del fisico: Specificare le seguenti informazioni:

Codice CoIn: codice CoIn da attribuire agli OdL serbatoio. Selezionare la CoIn tramite picklist

UM: unità di misura popolata in automatico dal sistema

Società: società alla quale verranno attribuiti gli OdL serbatoio. Selezionare il valore da una lista contenente “TERNA”, o “RETE”

Mese di riferimento: la tabella contiene una colonna per ogni mese dell’anno. L’utente deve inserire il fisico da allocare sugli OdL serbatoio per ciascun mese.

Totale: colonna popolata automaticamente. Rappresenta la sommatoria dei fisici inseriti per ciascuna riga

Figura - Maschera di generazione dei serbatoi valorizzata manualmente

Valorizzazione tramite import di file excel: In alternativa alla compilazione manuale tramite interfaccia applicativa, è possibile valorizzare massivamente le informazioni presenti nelle tabelle mediante l’import di un file Excel. Di seguito sono riportati gli step da seguire per eseguire correttamente l’operazione di import:

Scaricare il file excel contenete tutte le combinazioni ammissibili di figura professionale-codice coin-società tramite il pulsante “Scarica excel dettagli”

Popolare il file excel precedentemente scaricato. All’interno del file deve essere inserito l’effort orario per ogni combinazione di figura professionale-coin-società e l’effort fisico per ogni combinazione di coin-società.

Allegare il file tramite “Aggiungi nuovi allegati” e successivamente cliccare sul pulsante “Import excel dettagli” per popolare le maschere di generazione dei serbatoi. Analogamente a quanto avviene in fase di compilazione manuale tramite GUI, il sistema consente l’import dei dati solo se la somma delle ore inserite per ciascun mese e figura professionale risulta minore o uguale al valore indicato nella colonna “da allocare a serbatoio” dell’applicazione “Capacity”

Figura 11- Aggiunta allegato

Figura - Import excel

Figura - Tabelle di ripartizione effort e fisico popolate tramite import excel

Alla fine della compilazione (manuale a GUI o tramite import excel) delle tabelle di ripartizione effort e fisico, salvare e selezionare il tasto “Generazione OdL Serbatoio”, per avviare la generazione degli OdL. Agli OdL appena generati saranno attribuiti dei PdL specifici per i serbatoi, definiti a partire della CoIn selezionata. Inoltre, la quantità di OdL serbatoio generata è definita secondo la seguente formula:

Figura - Schermata di generazione di OdL serbatoio, con evidenza al tasto di aggiunta riga e di generazione degli OdL

Nel caso in cui sia necessario generare ulteriori OdL è possibile ripetere il processo tramite file excel (che sarà sempre scaricato con tutte le combinazioni) eliminando tutte le combinazioni tranne quelle per le quali si vuole effettuare la generazione. Se nel file vengono inserite combinazioni per le quali è già stato generato un OdL serbatoio, queste verranno ignorate nel processo di generazione (il processo di modifica di OdL esistenti viene gestito tramite MXLoader dedicati).

Verificare gli OdL generati: Gli OdL generati possono essere visualizzati all’interno del tab “OdL Serbatoio”, nella sezione “OdL generati”, che espone le seguenti informazioni:

OdL: codice univoco identificativo dell’OdL serbatoio appena generato;

Società: società proprietaria dell’OdL

Codice CoIn: codice CoIn attribuito all’OdL e determinato dall’utente in fase di ripartizione effort / fisico.

Data inizio: campo definito automaticamente dal sistema. Di default, EAM definisce come data inizio dell’OdL il primo giorno del mese di riferimento dell’OdL serbatoio.

Figura - Tab "OdL Serbatoio" all'interno dell'applicazione "Gestione serbatoio", contenente gli OdL serbatoio appena generati per la specifica UdB / Anno

### 4.4.3 Pianificazione delle risorse esterne

Una volta completata la generazione degli OdL serbatoio, è necessario procedere con la pianificazione delle risorse esterne associate. Poiché le funzionalità descritte nei paragrafi precedenti non consentono la valorizzazione di tali informazioni durante il processo di generazione degli Odl serbatoio, la pianificazione massiva delle risorse esterne deve essere effettuata tramite MXLoader. Tale attività costituisce l’ultimo passaggio del processo di creazione e configurazione degli OdL serbatoio.

Figura - Schema rappresentativo del processo di generazione degli OdL serbatoio, con evidenza allo step 4

Per procedere alla pianificazione delle risorse esterne, l’utente deve completare i seguenti step:

Accedere al file MxLoader e verificare la connessione: All’interno del foglio 'Config' dell’MXLoader dedicato alla modifica delle risorse esterne, inserire l’ApiKey (la chiave alfanumerica fornita all’utente abilitato) nel campo 'Password' e connettere il tool a EAM cliccando sul tasto 'Test Connection'.

Seguire le istruzioni riportate nello sheet “Guida”: per l’estrazione e l’aggiornamento dei dati tramite MXLoader, attenersi ai passaggi operativi descritti all’interno dello sheet “Guida”

Figura - Guida MXloader per modifica risorse esterne

### 4.4.4 Gestione odl serbatoio

Diversamente dagli stati previsti per gli OdL non serbatoio, questa tipologia di OdL ammette solo ed esclusivamente tre stati:

Bozza: stato attribuito automaticamente agli OdL serbatoio al momento della loro creazione. In questo stato è possibile effettuare delle modifiche puntuali sul singolo serbatoio (tramite l’applicazione Monitoraggio odl) o massive (tramite MXLoader). In particolare è possibile modificare il fisico pianificato, le risorse interne e le risorse esterne pianificate.

Aperto: stato attributo automaticamente all’OdL quando si entra nell’anno solare (1 Gennaio) in cui sono stati pianificati. Anche in questo stato è possibile effettuare le stesse modifiche descritte per lo stato bozza

Chiuso: viene attribuito automaticamente il 31/12 dell’anno pianificato per il serbatoio, oppure può essere impostato manualmente. In questo stato non è più possibile effettuare modifiche al serbatoio.

Si precisa che gli OdL serbatoio costituiscono un oggetto funzionale e propedeutico alla sola produzione del PMI; per tale ragione, i valori di fisico e risorse pianificate dei serbatoi non vengono erosi da parte degli odl da DSS e di quelli manuali.

## Strumenti di monitoraggio a supporto

Le informazioni relative agli Ordini di Lavoro PMI e agli OdL serbatoio possono essere analizzate attraverso il report "Avanzamento OdL con informazioni di pianificazione risorse esterne ed interne" (par. 11.2), che permette di estrarre e consultare i principali dati di pianificazione associati agli ordini durante la produzione del PMI. L'output del report, reso disponibile in formato Excel, rappresenta la base dati su cui l'utente può sviluppare analisi personalizzate, declinate secondo i parametri chiave per la produzione del PMI, come ad esempio COIN, date di pianificazione, fisico pianificato e risorse interne ed esterne pianificate.

# 5. PIANIFICAZIONE ODL

## 5.1 Introduzione

Di seguito verrà descritto in dettaglio il processo di gestione degli Ordini di Lavoro (OdL) all’interno del sistema EAM, fondamentale per garantire l’esecuzione strutturata ed efficiente delle attività manutentive previste nell’ambito della Wave 2.

Gli OdL rappresentano le entità operative che raccolgono tutte le informazioni necessarie per pianificare, eseguire, tracciare e consuntivare gli interventi sugli asset di rete. La gestione degli OdL si articola in più fasi: generazione, pianificazione tecnica ed economica, chiusura e finalmente consuntivazione.

Le attività vengono svolte attraverso applicazioni dedicate del sistema EAM, ciascuna configurata per supportare specifiche funzioni e profili autorizzati. Tutti gli utenti possono visualizzare gli OdL in modalità elenco, mentre le azioni di creazione, aggiornamento o cancellazione sono riservate a profili abilitati, secondo le regole di autorizzazione definite da Terna.

Il sistema prevede controlli formali e logici in fase di compilazione e modifica degli OdL, volti a garantire la coerenza delle informazioni inserite e l’allineamento con la struttura tecnica e operativa dell’organizzazione.

Il sistema consente di accorpare più OdL in un unico OdL “aggregatore”. Per procedere, l’utente deve creare manualmente l’OdL aggregatore, all’interno del quale vengono associati i diversi OdL figli.

Nell’ambito delle attività manutentive gestite in EAM, alcune richiedono una preventiva richiesta di indisponibilità dell’asset principale, nonché di eventuali asset interferenti o elettricamente connessi. Tale richiesta, pianificata e deliberata nel sistema GIN, assicura che gli asset interessati siano fuori servizio per l’intero periodo previsto dall’intervento.

## 5.2 Composizione e gestione degli OdL

La composizione degli OdL all’interno della piattaforma è definita da una serie di attributi che determinano il processo di gestione da seguire nel sistema. Tali attributi, valorizzabili nel tab “Ordini di lavoro” all’interno dell’app “Monitoraggio OdL” influenzano non solo il flusso operativo associato all’OdL, ma anche la presenza o assenza di informazioni utili per l’estrazione di report. I principali attributi di caratterizzazione individuati sono i seguenti:

Perimetro intervento: può assumere i valori “Sostituzione”, “Nuove installazioni”, “Manutenzione”, “Monitoraggio”, “Demolizione”, “Attività non operative”, “ANR”, “Altre attività” e “Pronto intervento”. La valorizzazione di questo attributo è determinata dalla tipologia di attività definita per ciascun PdL.

Tipo OdL: definisce la modalità di creazione dell’Ordine di Lavoro. Può assumere i seguenti valori:

Manuale: l’OdL viene creato manualmente da un operatore autorizzato.

On condition: l’OdL viene generato automaticamente dal sistema al verificarsi di una specifica condizione tecnica rilevata (ad esempio, da un controllo o una misura).

Periodico: l’OdL è generato ciclicamente dal sistema in base a una pianificazione definita (ad esempio, manutenzioni programmate).

OdL Serbatoio: specifica se l’OdL è classificato come “a serbatoio” attraverso il valore “SI” o “NO”.

Assistenza/Sorveglianza: è un attributo specifico degli OdL utilizzato per gestire le attività relative a opportunità di cantiere. I valori ammessi sono “Assistenza” e “Sorveglianza”.

La tabella riporta l’insieme di attributi che servono a caratterizzare gli OdL, così come loro obbligatorietà o meno a seconda del tipo di OdL che viene considerato. Questi attributi rappresentano elementi fondamentali per una corretta gestione, classificazione e tracciabilità degli OdL all’interno del sistema.

| CAMPO | DESCRIZIONE | OBBLIGATORIO O FACOLTATIVO | MODALITÀ DI VALORIZZAZIONE | LOGICA DI VALORIZZAZIONE |
| --- | --- | --- | --- | --- |
| Identificativo PdL | Codice univoco identificativo del PdL | Obbligatorio | Manuale | Selezione del PdL tramite dialog dedicata. |
| Tipologia PdL | Indica la tipologia di PdL associato all’OdL | Obbligatorio | Automatica | Definita nel catalogo delle attività e dedotta dal PdL selezionato. |
| Assieme e sottoassieme attività | Assieme e sottoassieme valorizzati se presenti nel PdL selezionato | Facoltativo | Automatica | Definita nel catalogo delle attività e dedotta dal PdL selezionato. |
| Programma | Cluster di attività su alcuni asset specifici | Facoltativo | Automatica | Informazione presente nel catalogo attività derivata dal PdL associato all’Odl. |
| Sotto-programma | Cluster di caratterizzazione ulteriore su alcuni asset specifici, per i quali il programma non è sufficiente | Facoltativo | Manuale | Informazione inserita manualmente dall’utente selezionando dalla lista di sotto-programmi censiti a sistema. |
| Descrizione PdL | Descrizione del PdL associato all’OdL | Obbligatorio | Automatica | Definita nel catalogo delle attività e derivata dall’ID PdL inserito. |
| ID Macro-attività | Codice identificativo della macro-attività associata all’OdL | Obbligatorio | Automatica | Definita nel catalogo delle attività e dedotta dal PdL selezionato. |
| Responsabile attività | Campo identificativo dell’owner della pianificazione dell’OdL. | Obbligatorio | Automatica | Campo modificabile dall’utente, valorizzato in base all’asset associato all’OdL e al PdL selezionato, nel caso in cui non fosse specificato nel PdL viene valorizzato con lo stesso valore del resp. Asset. |
| ID OdL | Codice identificativo dell’OdL manuale generato | Obbligatorio | Automatico | Codice generato automaticamente dal sistema al momento della creazione del nuovo OdL. |
| Descrizione OdL | Descrizione dell’OdL che si vuole creare | Obbligatorio | Automatico | Ereditato dalla descrizione del PdL Il campo è modificabile dall’utente qualora necessario. |
| ID Asset | Codice identificativo dell’Asset oggetto dell’OdL | Obbligatorio | Manuale | Selezione manuale tramite dialog dedicata. |
| Descrizione Asset | Descrizione dell’asset oggetto dell’OdL | Obbligatorio | Automatica | Determinato automaticamente in base all’asset selezionato |
| SAP ID Asset | Codice SAP dell’asset oggetto dell’OdL | Facoltativo | Automatica | Attributo anagrafico associato all’asset. Qualora all’asset non sia associato un SAP ID, Il campo viene popolato con il SAP ID del primo asset della gerarchia. |
| Gerarchia Asset | Gerarchia dell’asset oggetto dell’OdL | Obbligatorio | Automatica | Derivato dalla struttura gerarchica anagrafica dell’asset. |
| Stato | Stato dell’OdL. | Obbligatorio | Automatica | Valorizzato automaticamente con “Aperto” al momento della generazione dell’OdL |
| Responsabile Asset | Responsabile censito nelle sedi tecniche in anagrafica EAM | Obbligatorio | Automatica | Recuperato dall’anagrafica in base all’asset selezionato |
| Codice CoIn | Codice identificativo della contabilità industriale | Obbligatorio | Automatica | Valorizzato automaticamente sulla base del catalogo standard, tipo spesa e PdL selezionato |
| Tipo spesa | Campo che indica la tipologia di attività CoIn associata all’OdL. Può assumere i valori “Opex”; “Capex” o “ANR” | Obbligatorio | Automatico | Valorizzato automaticamente con il Tipo spesa prevalente |
| Attività standard | ID attività standard | Obbligatorio | Automatica | Valorizzato in base al PdL associato all’OdL |
| Titris | Campo indicativo del titolare della risorsa che esegue l’attività | Obbligatorio | Automatico | Corrispondente al responsabile attività |
| Durata pianificata | Durata pianificata per l’esecuzione dell’OdL | Obbligatorio | Automatico | Corrispondente alla massima durata tra quelle associate ad ogni singola risorsa. La durata pianificata viene ereditata dal catalogo delle attività in base al PdL associato all’OdL. Nel caso non sia specificata la durata all’interno del catalogo il campo viene lasciato vuoto alla creazione dell’ordine e deve essere successivamente valorizzato manualmente dall’utente. |
| Ore esecuzione | Periodo (in ore) di esecuzione dell’attività, per risorsa. | Obbligatorio(nel caso in cui ci siano dei record) | Automatico | Valore reperito dal catalogo delle attività Tuttavia, qualora sia necessario aggiungere manualmente delle risorse, il campo non deve essere valorizzato automaticamente. |
| Ore trasferimento | Periodo (in ore) necessario per il raggiungimento e il rientro dal luogo di svolgimento dell’OdL | Facoltativo | Automatico | Valore determinato in funzione del Titris e degli’asset associati all’OdL. |
| Data pianificata inizio / fine | Date di inizio e fine previste per l’esecuzione dell’attività | Obbligatorio | Manuale | Inserimento manuale nel campo dedicato del tab “Date” nel caso in cui non sia inserita una data modello. Se l’utente definisce una data modello (funzionalità esclusiva degli utenti centrali), il sistema popola automaticamente i campi nel seguente modo: Data pianificata fine = Data modello. La data di inizio deve invece essere popolata manualmente |
| Data modello | Data modello | Facoltativo | Manuale | Inserimento manuale nel campo dedicato del tab “Date”. L’operazione sarà consentita soltanto dagli utenti centrali |
| Perimetro intervento | Attributo dell’OdL che può assumere i valori “Sostituzione”, “Nuove installazione”, “Manutenzione”, “Monitoraggio” e “Altro”. | Obbligatorio | Automatica | Attributo valorizzato in base alla tipologia di attività |
| Tipo OdL | Campo identificativo della tipologia di OdL | Obbligatorio | Automatica | Valorizzato automaticamente con “Manuale” al momento della creazione dell’OdL |
| OdL serbatoio | Flag che definisce se l’OdL si tratta di un serbatoio o meno. Preimpostato come “NO” | Obbligatorio | Automatica | Flag preimpostato in “NO”; gestito dal sistema |
| Data inizio e fine indisponibilità | Data che definisce l’inizio e fine del periodo di indisponibilità dell’asset oggetto dell’Odl | Facoltativo | Automatica | Informazione popolata a valle della creazione della richiesta di indisponibilità in Agile, qualora sia necessaria per il PdL associato all’OdL. |
| Indisponibilità necessaria? | Flag che indica se il PdL associato all’OdL richiede indisponibilità | Obbligatorio | Automatica | Informazione ereditata dal PdL e presente all’interno del catalogo delle Attività |
| Note | Campo di inserimento di note | Facoltativo | Manuale | Inserimento manuale libero nel campo dedicato del tab “Ordini di lavoro” |
| Assistenza / Sorveglianza | Campo che indica se l’OdL si caratterizza come di assistenza o sorveglianza. Serve a racchiudere le attività per gestire le casistiche di opportunità di cantiere. Il campo può assumere i valori “Assistenza”, “Sorveglianza”. | Obbligatorio | Manuale | Inserimento nel campo dedicato tramite selezione da un elenco di valori ammissibili (“Assistenza”, “Sorveglianza”). Il campo è reso visibile solo in caso Outsourcing? = SI |
| Outsourcing? | Campo che indica se l’attività è in insourcing o outsourcing | Obbligatorio | Automatica | Campo derivato dal PdL. Se valorizzato con “SI” viene reso visibile il campo “Assistenza/Sorveglianza” |
| OdL Aggregatore | Flag che indica se l’OdL è di natura aggregatore. Preimpostato come “NO” | Obbligatorio | Automatica | Flag preimpostato in “NO” e manualmente modificabile. |
| Data massimo posticipo | Data massima entro la quale l’OdL può essere posticipata | Obbligatorio | Manuale | Inserita nel campo dedicato del tab “Date” |
| Data non prima di | Data limite entro la quale si può anticipare l’attività | Obbligatorio | Automatico | Valorizzato automaticamente dal sistema uguale alla data di creazione dell’OdL manuale |
| OdL per piano (PMI) | Flag che indica l’appartenenza dell’OdL al PMI | Obbligatorio | Automatico | Valorizzato di default a “NO” dal sistema, modificabile liberamente dall’utente in ogni stato dell’OdL |
| Fisico Pianificato | Campo che definisce il valore di fisico attribuito all’OdL | Obbligatorio | Automatico | Campo popolato dal sistema dopo aver effettuato la generazione dell’OdL. Il valore viene ereditato dall’attributo anagrafico dell’asset dell’OdL. Qualora l’unità di misura sia “Quantità”, il sistema valorizza il campo con il numero di asset associati all’OdL (1 per OdL singolo o >1 per OdL aggregatori |
| Unità di misura | Campo che definisce l’unita di misura attribuita all’OdL | Obbligatorio | Automatico | Campo popolato dal sistema dopo effettuata la generazione dell’OdL. Il valore viene ereditato da quanto definito per il PdL nel catalogo attività. |

Tabella - Campi facoltativi e obbligatori per gli OdL

## 5.3 Generazione OdL manuali

### 5.3.1 Generazione puntuale di OdL manuali

La funzione è abilitata esclusivamente per gli utenti con profilo P1, P3, P5, P6, P16, P7, P8, P17, P11, secondo le autorizzazioni di sistema.

Gli Ordini di Lavoro manuali vengono creati direttamente dall’utente tramite l’applicazione “Monitoraggio OdL”. Per effettuare il processo di generazione manuale di un ordine di lavoro, l’utente deve seguire i passaggi descritti di seguito.

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Cliccare su “Nuovo OdL: una volta aperta l’applicazione, è possibile cliccare sull’azione “Nuovo OdL” o sul pulsante “+” evidenziato nella schermata per accedere alla maschera di compilazione dell’OdL.

Figura - Vista elenco iniziale dell'app "Monitoraggio OdL", con evidenza alle azioni di creazione manuale di OdL

Compilare i campi obbligatori e facoltativi in maschera: l’operatore deve compilare i campi riportati in Tabella 1 all’interno della maschera di generazione di un OdL, distribuiti tra i diversi tab previsti nell’applicazione. I campi obbligatori da compilare per la generazione di un nuovo OdL sono riportati in Tabella 1, così come le loro modalità di valorizzazione (manuale o automatica).

Figura- Schermata di dettaglio dell'OdL all'interno dell'applicazione "Monitoraggio OdL"

Nota: quando l’utente procede alla valorizzazione del campo “Identificativo PdL”, il sistema consente di selezionare esclusivamente i PdL per il quale il flag “Attività caricabile manualmente” è attivo nel catalogo delle attività. Dopo aver selezionato il codice PdL da associare all’OdL, l’asset sul quale verrà eseguito l’OdL va definito a partire da una lista fornita dal sistema, contenente gli asset ammissibili per il PdL selezionato.

Figura - Dialog di selezione del PdL da associare all'OdL

Figura - Dialog di selezione dell'asset da associare all'OdL

Salvare l’OdL appena generato: dopo aver compilato i campi obbligatori presente in maschera e descritti in Tabella 1, l’utente deve procedere al salvataggio del nuovo OdL. Il salvataggio dell’OdL viene effettuato selezionando l’azione “Salva OdL”, disponibile nel menu laterale sinistro della vista o tramite l’icona di salvataggio evidenziata in alto a destra.

Figura - Schermata di dettaglio dell'OdL all'interno dell'applicazione "Monitoraggio OdL", con evidenza alle funzionalità di salvataggio

### 5.3.2 Generazione massiva di OdL manuali

Oltre alla creazione singola, il sistema consente la generazione massiva degli Ordini di Lavoro manuali attraverso due modalità operative, descritte di seguito:

Generazione massiva tramite MxLoader

La funzione è abilitata esclusivamente per gli utenti con profilo P1, secondo le autorizzazioni di sistema.

Per avviare il processo di generazione massiva manuale utilizzando MxLoader, l’utente deve seguire i passaggi descritti di seguito.

Accedere al file MxLoader e verificare la connessione: selezionare il file MxLoader e cliccare su “Test Connection” per controllare se il file è connesso alla piattaforma EAM. Se la connessione è stabile, il sistema mostra l’avviso.

Figura - Tab "Config" dello strumento "MxLoader", con evidenza al tasto "Test connection"

Figura - Tab "Config" dello strumento "MxLoader", con evidenza all’avviso di connessione andata a buon fine

Impostare il campo “C1”: selezionare il campo “C1” del tab “Workorder” e impostare il campo in “AddChange”.

Figura - Tab "Workorder" dello strumento "MxLoader", con evidenza alla cella “C1” con l’impostazione richiesta per la generazione massiva di OdL

Compilare i campi obbligatori: all’interno del tab “Workorder”, l’utente deve inserire i campi obbligatori per la generazione degli OdL:

ASSETNUM: codice univoco dell’asset

SITEID: costante popolata con SIT01

JPNUM: codice del PdL utilizzato

TARGSTARTDATE: data inizio pianificata

TARGCOMPDATE: data fine pianificata

SNECONSTRAINT: data non prima di

FNLCONSTRAINT: data di massimo posticipo

ESTDUR: durata dell’attività

X4BURKS: cliente

X4TIPOSPESA: tipo spesa dell’OdL

X4COIN: codice CoIn

WPLABOR.CRAFT: qualifica della risorsa interna pianificata (operaio, quadro, impiegato)

WPLABOR.QUANTITY: quantità di risorse di stessa qualifica

WPLABOR.X4TITRIS: titris al quale appartiene la risorsa pianificata

WPLABOR.LABORHRS: ore esecuzione dell’attività

WPLABOR.X4DATAINIZIORISORSA: data inizio della risorsa pianificata

WPLABOR.X4DATAFINERISORSA: data fine della risorsa pianificata

Figura - Tab "Workorder" dello strumento "MxLoader", con evidenza alle informazioni fornite per la generazione massiva di OdL

Avviare la generazione degli OdL: dopo aver valorizzato i campi richiesti, l’utente seleziona il tasto “Run” per procedere alla generazione massiva degli OdL. Qualora la procedura non sia andata a buon fine, sarà presente un messaggio di errore in colonna “Result” (AH) del file. Una volta completata la compilazione del file ed eseguito il “Run”, EAM genererà automaticamente un OdL per ciascun record presente nel file, attribuendo un codice OdL univoco ad ogni ordine di lavoro creato.

Figura - Tab "Workorder" dello strumento "MxLoader", con evidenza al tasto “Run” per la generazione massiva di Jodl

Generazione massiva tramite funzionalità dedicata

Per avviare il processo di generazione massiva manuale tramite la funzionalità dedicata di EAM, l’utente deve seguire i passaggi descritti di seguito.

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare la funzionalità di creazione massiva: l’utente deve selezionare l’opzione “Creazione massiva OdL”per accedere alla schermata operativa

Figura ìì- Vista elenco iniziale dell'app "Monitoraggio OdL", con evidenza all’azione di creazione massiva manuale di OdL

Figura - Dialog di inserimento delle informazioni per la generazione massiva di OdL

Compilare i campi obbligatori e facoltativi: nella dialog fornita dal sistema, rappresentata in Figura 14, l’utente deve inserire le informazioni rilevanti per la caratterizzazione degli OdL, rispettando l’ordine di seguito descritto:

Sede tecnica di primo livello: l’utente seleziona l’elemento di primo livello “Linea” o “Stazione” dalla lista. La selezione dell’elemento di primo livello abilita l’operatore alla successiva selezione.

Figura - Dialog di selezione dell'asset di primo livello degli OdL generati massivamente

Selezionare la classificazione asset: cliccare sulla lente di ingrandimento accanto al campo “Classificazione asset” per aprire la dialog. Tramite la dialog, sarà possibile determinare la classificazione degli asset sui quali incideranno i nuovi OdL.

Figura 13 - Dialog di selezione dell'asset di primo livello di OdL generati massivamente

Selezionare l’Identificativo PdL: popolare il campo “Identificativo PdL” selezionando un valore dalla lista fornita. Sono disponibili solo i PdL con flag “Attività caricabile manualmente” impostato su “SI” e coerenti con la classificazione asset precedentemente selezionata. Qualora necessario, l’utente può filtrare la ricerca per codice identificativo del PdL, descrizione o tipologia di attività.

Figura - Dialog di selezione del PdL degli OdL generati massivamente

Selezionare gli asset: a seguito della compilazione dei campi menzionati in precedenza, il sistema restituisce una lista filtrabile di asset ammissibili per i criteri inseriti. L’utente può selezionare gli asset desiderati in modo puntuale o massivo trami la selezione dei quadri. Per ciascun asset vengono mostrate le seguenti informazioni:

Codice Asset

Descrizione

SAP-ID (se presente)

Stato

Figura - Dialog di inserimento delle informazioni per la generazione massiva di OdL, con evidenza alle checkbox di selezione degli asset

Popolare i campi restanti: dopo aver effettuato gli step precedenti, l’utente deve procedere con la compilazione degli altri campi obbligatori e facoltativi non ancora valorizzati. Nello specifico, deve compilare i campi “Data modello” (valorizzabile solo da utente centrale), “Data pianificata inizio” e “Data pianificata fine”. Quest’ultimo viene valorizzato automaticamente se è presente una Data modello, altrimenti è necessario inserirlo manualmente. Infine, compilare il campo “Data di massimo posticipo attività”, che rappresenta la scadenza massima per l’esecuzione dell’OdL. Il sistema verifica che tale data sia successiva o uguale alla data di fine pianificata.

L’utente deve anche verificare il campo “Outsourcing?”, automaticamente ereditato dal PdL ma modificabile manualmente selezionando “IN” o “OUT”. Se il campo è impostato su “SI”, sarà visibile anche il campo “Assistenza/Sorveglianza”, da compilare selezionando uno tra i valori disponibili: “Assistenza” o “Sorveglianza”.

Qualora non sia stato valorizzato automaticamente in base alla sede tecnica di primo livello, è necessario popolare il campo “Cliente”

Confermare la generazione degli OdL: una volta completata la selezione degli asset e la valorizzazione dei campi, l’utente deve confermare l’operazione selezionando il tasto “OKper procedere con la generazione massiva degli OdL in stato “Aperto”.

Figura - Dialog di inserimento delle informazioni per la generazione massiva di OdL, con evidenza al tasto “Ok” per l’avvio della generazione massiva

Nota: l’elenco di asset selezionabili è visibile in maschera soltanto dopo aver popolato i campi “Sede tecnica di primo livello”, “Classificazione Asset” e “Identificativo PdL”. La compilazione dei campi descritti nel punto “e” non è, tuttavia, condizionata all’elenco di asset selezionato, potendo essere eseguita prima o dopo della selezione degli asset oggetto degli OdL.

## 5.4 Pianificazione tecnica degli OdL

La funzione è abilitata esclusivamente per gli utenti con profilo P1, P3, P5, P6, P7, P8, P11, P16, P17, secondo le autorizzazioni di sistema.

La pianificazione tecnica degli Ordini di Lavoro rappresenta la fase in cui vengono definite tutte le informazioni necessarie per l’esecuzione operativa delle attività. In questa fase, l’utente responsabile per la pianificazione provvede a valorizzare gli attributi tecnici indispensabili per l’esecuzione del lavoro, come ad esempio il PdL associato, l’asset target del lavoro, le date e durata di esecuzione e le risorse assegnate all’attività.

La pianificazione tecnica consente di strutturare correttamente l’OdL in funzione delle caratteristiche dell’intervento da effettuare, garantendo un’esecuzione coerente, tracciabile ed efficiente all’interno del ciclo di gestione delle attività manutentive.

Nei paragrafi successivi sono descritti degli elementi e funzionalità essenziali alla corretta gestione dei processi coinvolti nella pianificazione tecnica degli OdL in EAM.

### 5.4.1 Pianificazione delle risorse

Tramite il processo di pianificazione è possibile definire gli aspetti più importanti inerenti alle risorse che eseguiranno l’attività. La pianificazione delle risorse può essere realizzata direttamente durante la creazione di un nuovo OdL o in un momento successivo. Una volta generato l’OdL, l’utente accede al tab “Pianificazione”, dove è possibile visualizzare e gestire le informazioni relative alle risorse esterne e ai ricavi. La pianificazione di risorse esterne è prevista solo per gli OdL con tipo spesa OPEX e ANR, mentre quella di ricavi è possibile solo per gli OdL con tipo spesa ANR.

Il processo di pianificazione di risorse in EAM avviene quando vengono eseguiti i seguenti step:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire la voce “Monitoraggio OdL” direttamente nel campo di ricerca.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL di interesse: utilizzando il filtro di ricerca presente in alto a destra nella schermata, l’utente può individuare l’OdL per cui si desidera modificare il responsabile attività. Una volta trovato, cliccare sull’OdL per accedere alla maschera di dettaglio dell’OdL.

Figura - Vista elenco iniziale dell'app "Monitoraggio OdL", con evidenza al filtro di ricerca degli OdL

Verificare lo stato dell’OdL: prima di procedere alla pianificazione delle risorse, l’utente deve assicurarsi che l’OdL sia in stato “Aperto”, condizione necessaria per procedere con le attività di pianificazione. Nel caso in cui l’OdL sia in uno stato diverso da “Aperto”, procedere con la ripianificazione dell’OdL, secondo quanto descritto in 2.2.

Accedere alla tab “Pianificazione”: cliccare sul tab “Pianificazione” per visualizzare e gestire le risorse pianificate. In questa sezione, l’utente potrà realizzare tre operazioni principali, descritte nei paragrafi successivi:

Aggiunta di nuove risorse

Modifica delle risorse pianificate

Eliminazione di risorse già pianificate

Figura - Vista di dettaglio dell’OdL all'interno dell'applicazione "Monitoraggio OdL", con evidenza al tab “Pianificazione”

Nei paragrafi successivi sono descritti nel dettaglio le operazioni coinvolte nel processo di gestione della pianificazione delle risorse interne ed esterne.

Pianificazione delle risorse interne

Aggiunta delle nuove risorse: per pianificare una nuova risorsa interna sull’OdL, è necessario seguire i seguenti step:

Cliccare sull’icona “+”: per aggiungere una nuova riga nella tabella “Risorse interne”, l’operatore deve selezionare il tasto “+”

Compilare i seguenti campi richiesti:

Qualifica: figura professionale associata alla risorsa (Operaio, Quadro, Impiegato);

Quantità: numero delle risorse impiegate con la medesima qualifica;

Ore esecuzione: ore effettive di lavoro sul campo;

Ore trasferimento: campo non obbligatorio. Indica il tempo necessario alla risorsa per arrivare al luogo di lavoro;

Titris: titolare risorsa che esegue l’attività;

Data inizio risorsa: data in cui si pianifica di iniziare ad utilizzare la risorsa.

Data fine risorsa: data in cui si pianifica il termine di utilizzo della risorsa;

Salvare la nuova risorsa: dopo aver inserito le informazioni necessarie, l’utente clicca sull’icona di salvataggio, per confermare l’inserimento della nuova risorsa.

Figura – Tabella di pianificazione delle risorse interne dell’OdL all'interno dell'applicazione "Monitoraggio OdL", c on evidenza al tasto di aggiunta, alle informazioni di pianificazione da fornire e all’icona di salvataggio dell’OdL.

Nota: La minima data di inizio risorse presente non può essere minore dalla data di inizio pianificata e la massima data fine risorse presente non può essere superiore alla data di fine pianificata dell’OdL. Almeno una delle date di inizio risorsa deve coincidere con data di inizio dell’OdL e almeno una delle data di fine risorsa deve coincidere con la data di fine dell’OdL

Modifica delle risorse interne pianificate: per modificare una risorsa interna precedentemente pianificata sull’OdL, è necessario seguire i seguenti step:

Effettuare la modifica: per modificare un’informazione relativa alla risorsa pianificata, l’utente deve semplicemente selezionare il campo che si vuole modificare ed inserire il nuovo dato. Nello specifico, EAM consente di modificare i seguenti campi:

Quantità

Ore di esecuzione

Ore di trasferimento

Date di inizio risorsa

Data fine risorsa

Salvare la modifica: dopo aver modificato le informazioni necessarie, l’utente clicca sull’icona di salvataggio per confermare la modifica.

Eliminazione delle risorse interne pianificate: per rimuovere una risorsa interna precedentemente pianificata sull’OdL, è necessario seguire i seguenti step:

Effettuare l’eliminazione: per eliminare una riga, l’utente deve cliccare sull’icona del cestino. Qualora sia necessario interrompere l’eliminazione, è possibile farlo selezionando l’icona di “torna indietro”.

Salvare l’eliminazione: dopo aver eliminato le risorse necessarie, l’utente clicca sull’icona di salvataggio per confermare l’operazione.

Figura- Tabella di pianificazione delle risorse interne dell’OdL all'interno dell'applicazione "Monitoraggio OdL", con evidenza al cestino di eliminazione, all’icona di “torna indietro” e alla funzione di salvataggio.

Pianificazione delle risorse esterne

Aggiunta di nuova risorsa esterna: per pianificare una nuova risorsa esterna sull’OdL, è necessario seguire i seguenti step:

Cliccare sull’icona “+”: per aggiungere una nuova riga nella tabella “Risorse esterne”, l’operatore deve selezionare il tasto “+”.

Compilare i seguenti campi richiesti: l’utente deve compilare manualmente i campi di seguito elencati. Altri campi della tabella sono compilati automaticamente in base alle informazioni presenti nel “Catalogo risorse esterne / ricavi”.

ID Materiale: Campo libero valorizzato manualmente dall’utente. Sarà possibile filtrare l’elenco di valori ammissibili per “Tipo risorsa”.

Quantità: campo numerico valorizzato manualmente dall’utente. In alcuni casi, il campo può essere prepopolato in base al PdL associato all’ordine.

Importo totale: inserito manualmente come quantità x importo unitario.

Data inizio: data in cui si pianifica di iniziare ad utilizzare la risorsa;

Data fine: data in cui si pianifica il termine di utilizzo della risorsa;

Salvare la nuova risorsa: dopo aver inserito le informazioni necessarie, l’utente clicca sull’icona di salvataggio, per confermare l’inserimento della nuova risorsa.

Figura - Tabella di pianificazione delle risorse esterne dell’OdL all'interno dell'applicazione "Monitoraggio OdL", con evidenza al tasto di aggiunta, alle informazioni di pianificazione da fornire e all’icona di salvataggio dell’OdL.

Nota: nel caso in cui il PdL associato all’OdL contenga risorse esterne con quantità e importo definiti, i campi “Quantità” e “Importo totale” risultano pre-valorizzati alla creazione dell’OdL (informazione originata dal catalogo Risorse esterne / Ricavi). Il campo “Importo totale” viene calcolato automaticamente come quantità × importo unitario. Tali campi, pur essendo inizializzati dal sistema, restano comunque modificabili manualmente dall’utente.

Modifica di risorse esterne pianificate: per modificare una risorsa esterna precedentemente pianificata sull’OdL, è necessario seguire i seguenti step:

Effettuare la modifica: per modificare un’informazione relativa alla risorsa pianificata, l’utente deve semplicemente selezionare il campo che si vuole modificare ed inserire il nuovo dato. Nello specifico, EAM consente di modificare qualsiasi campo nella tabella.

Salvare la modifica: dopo aver modificato le informazioni necessarie, l’utente clicca sull’icona di salvataggio per confermare la modifica.

Eliminazione delle risorse interne pianificate: per rimuovere una risorsa esterna precedentemente pianificata sull’OdL, è necessario seguire i seguenti step:

Effettuare l’eliminazione: per eliminare una riga, l’utente deve cliccare sull’icona del cestino. Qualora sia necessario interrompere l’eliminazione, è possibile farlo selezionando l’icona di “torna indietro”.

Salvare l’eliminazione: dopo aver eliminato le risorse necessarie, l’utente clicca sull’icona di salvataggio per confermare l’operazione.

Figura - Tabella di pianificazione delle risorse esterne dell’OdL all'interno dell'applicazione "Monitoraggio OdL", con evidenza al cestino di eliminazione, all’icona di “torna indietro” e alla funzione di salvataggio.

Pianificazione dei ricavi

Aggiunta di nuovo ricavo: per pianificare un nuovo ricavo sull’OdL, è necessario seguire i seguenti step:

Cliccare sull’icona “+”: per aggiungere una nuova riga nella tabella “Ricavi”, l’operatore deve selezionare il tasto “+”

Compilare i seguenti campi richiesti: l’utente deve compilare manualmente i campi di seguito elencati. Altri campi della tabella sono compilati automaticamente in base alle informazioni presenti nel “Catalogo risorse esterne / ricavo”:

ID Ricavo: campo popolato dall’utente tramite una lista valori;

Importo: campo numerico valorizzato manualmente dall’utente;

Data inizio: data in cui si pianifica di iniziare ad utilizzare il ricavo;

Data fine: data in cui si pianifica di iniziare ad utilizzare il ricavo;

Salvare il nuovo ricavo: dopo aver inserito le informazioni necessarie, l’utente clicca sull’icona di salvataggio per confermare l’inserimento del nuovo ricavo.

Figura - Tabella di pianificazione dei ricavi all'interno dell'applicazione "Monitoraggio OdL", con evidenza al tasto di aggiunta e alle informazioni di pianificazione da fornire.

Modifica di risorse esterne pianificate: per modificare un ricavo precedentemente pianificato sull’OdL, è necessario seguire i seguenti step:

Effettuare la modifica: per modificare un’informazione relativa ad un ricavo precedentemente pianificato, l’utente deve semplicemente selezionare il campo che si vuole modificare ed inserire il nuovo dato. Nello specifico, EAM consente di modificare qualsiasi campo presente in tabella.

Salvare la modifica: dopo aver modificato le informazioni necessarie, l’utente clicca sull’icona di salvataggio per confermare la modifica.

Eliminazione di ricavo: per rimuovere una risorsa esterna precedentemente pianificata sull’OdL, è necessario seguire i seguenti step:

Effettuare l’eliminazione: per eliminare una riga, l’utente deve cliccare sull’icona del cestino. Qualora sia necessario interrompere l’eliminazione, è possibile farlo selezionando l’icona di “torna indietro”

Salvare l’eliminazione: dopo aver eliminato le righe necessarie, l’utente clicca sull’icona di salvataggio per confermare l’operazione.

Figura - Tabella di pianificazione dei ricavi all'interno dell'applicazione "Monitoraggio OdL", con evidenza al cestino di eliminazione, all’icona di “torna indietro” e alla funzione di salvataggio.

## 5.5 Area di validazione di OdL generati automaticamente

La funzione è abilitata esclusivamente per gli utenti con profilo P1, P3, P5, P6, P16, P7, P8, P17, P11, secondo le autorizzazioni di sistema.

Gli Ordini di Lavoro generati in modo automatico dal DSS vengono inizialmente creati in stato “Da Validare”. Questi OdL sono presenti all’interno dell’applicazione “Area di validazione” prima di essere inviati alla fase di pianificazione. Per procedere alla validazione manuale, l’utente deve seguire i passaggi descritti di seguito.

Accedere all’applicazione “Area di validazione”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente scegliere l’app “Area di validazione”. In alternativa, può inserire direttamente nel campo di ricerca la voce “Area di validazione”. Una volta aperta l’applicazione, viene visualizzata una lista contenente tutti gli OdL in stato “Da Validare”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Area di validazione OdL"

Selezionare gli OdL da validare: all’interno della vista elenco, è possibile selezionare uno o più OdL on condition in stato “Da Validare” per procedere con la validazione. La vista elenco può essere filtrata per i seguenti dati:

ID OdL

Descrizione OdL

Riferimento Asset

SAP ID Asset

Classificazione Asset

ID Policy

ID PdL

Data modello

Stato

Programma

Motivazione

Responsabile attività.

Una volta definiti gli OdL da validare, l’utente deve selezionare la checkbox corrispondente all’OdL. Qualora necessario, può effettuare la selezione massiva selezionando il quadro presente nella sezione superiore della vista elenco.

Figura - Vista iniziale dell'applicazione" Area di validazione", con evidenza alle checkbox di selezione degli OdL in stato "Da validare"

Gestire la validazione / non validazione: all’interno dell’area di validazione, è possibile eseguire le seguenti azioni:

Validazione dell’OdL: il passaggio dello stato da “Da Validare” ad “Aperto” avviene manualmente all’interno dell’applicazione “Area di validazione”. Dopo aver selezionato gli OdL da validare, l’operatore seleziona l’azione “Cambia stato” per eseguire la modifica di stato. Sulla dialog inserisce nel campo “Nuovo stato” la voce “Aperto” e conferma la modifica cliccando sul tasto blu “OK”. Il passaggio avviene automaticamente al superamento del tempo massimo di validazione definito nel catalogo attività.

Figura - Vista iniziale dell'applicazione" Area di validazione", con evidenza all’azione di cambio stato

Figura - Dialog di modifica stato, con evidenza allo stato "Aperto" e al tasto di conferma modifica

È da evidenziare che il processo di validazione può anche avvenire automaticamente nel caso in cui venga raggiunto il tempo massimo di validazione definito nel catalogo attività.

Non validazione dell’OdL: come appena descritto per il processo di validazione, la non validazione di un’OdL può essere eseguita manualmente all’interno dell’applicazione “Area di validazione”. Dopo aver selezionato gli OdL da non validare, l’operatore seleziona l’azione “Cambia stato” per eseguire la modifica di stato. Sulla dialog imposta nel campo “Nuovo stato” la voce “Non validato” e inserisce nel campo “Motivazione” una motivazione per la non validazione. Per confermare l’azione, l’utente deve cliccare sul tasto blu “OK”

Figura - Dialog di modifica stato, con evidenza allo stato "Non validato" e al tasto di conferma modifica

## 5.6 Gestione date OdL

La valorizzazione dei campi date di un OdL è un’azione obbligatoria per la sua generazione e per questo motivo, una prima definizione dei parametri temporali dell’ordine deve essere realizzata ancora in fase di apertura. Nello specifico, il sistema non ammette ordini di lavoro che non abbiano i seguenti campi compilati:

Data pianificata inizio

Data pianificata fine

Data non prima di

Data di massimo posticipo

Per definire o modificare le date associate agli OdL, è necessario eseguite gli step descritti di seguito:

Nota: il presente capitolo descrive la casistica di gestione delle date di OdL già presenti in EAM. Per la gestione delle date durante la generazione dell’OdL, considerare lo step “2” per la generazione di un nuovo OdL.

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL di interesse: utilizzando il filtro di ricerca presente in alto a destra nella schermata, l’utente può individuare l’OdL per cui si desidera modificare il responsabile attività. Una volta trovato, cliccare sull’OdL per accedere alla maschera di dettaglio dell’OdL.

Figura - Vista iniziale dell'applicazione "Monitoraggio OdL", con evidenza all'icona di filtro di ricerca

Verificare lo stato dell’OdL: prima di procedere alla modifica delle date, l’utente deve assicurarsi che l’OdL si trovi in stato “Aperto”, condizione necessaria per procedere con le attività di pianificazione. Nel caso in cui l’OdL sia in uno stato diverso da “Aperto”, procedere con la ripianificazione dell’OdL, secondo quello descritto in 2.2.4.

Accedere al tab “Date”: all’interno della scheda dell’OdL, selezionare il tab “Date” per accedere ai campi dedicati alla pianificazione temporale.

Figura - Schermata di dettaglio del tab "Date" all'interno dell'applicazione "Monitoraggio OdL"

Compilare i campi obbligatori: all’interno del tab, è necessario valorizzare i seguenti campi obbligatori:

Data pianificata inizio: indica la data prevista per l’avvio dell’attività

Data pianificata fine: rappresenta la data prevista di completamento dell’OdL

Data non prima di: indica il vincolo temporale a partire dal quale l’attività può essere avviata

Data di massimo posticipo attività: campo obbligatorio che rappresenta il limite ultimo entro cui l’OdL deve essere eseguito;

Durata pianificata: campo obbligatorio che definisce la durata complessiva dell’attività. Il campo risulta pre-popolato nei casi in cui la durata è definita a catalogo per il PdL associato all’OdL.

Salvare le modifiche: dopo aver completato la compilazione dei campi, cliccare su “Salva”, per rendere effettive le modifiche.

Figura- Schermata di dettaglio del tab "Date" all'interno dell'applicazione "Monitoraggio OdL", con evidenza ai campi obbligatori e all’icona di salvataggio

Nota: il sistema effettua un controllo automatico per verificare che la data non prima di sia uguale o precedente alla data pianificata inizio e che la data di massimo posticipo sia uguale o successiva alla data pianificata di fine. Qualora una di queste condizioni non sia rispettata, non sarà possibile salvare la pianificazione. Inoltre, per gli OdL creati manualmente, a differenza di quelli generati dal DSS, è inibito il passaggio allo stato “Non eseguito” in caso di superamento della data di massimo posticipo.

## 5.7 Processo di ripianificazione OdL

All’interno del ciclo di vita degli OdL, dal momento di creazione fino alla chiusura, può essere necessario realizzare delle modifiche sulle informazioni attribuite agli OdL in fase di pianificazione tecnica. Per realizzare delle modifiche, l’utente deve seguire la seguente procedura:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL di interesse: utilizzando il filtro di ricerca presente in alto a destra nella schermata, l’utente può individuare l’OdL per cui si desidera modificare il responsabile attività. Una volta trovato, cliccare sull’OdL per accedere alla maschera di dettaglio dell’OdL.

Figura - Vista iniziale dell'applicazione "Monitoraggio OdL", con evidenza all'icona di filtro di ricerca

Impostare lo stato in “Aperto”: dopo aver selezionato l’OdL da ripianificare, l’utente deve assicurarsi che lo stesso sia in stato “Aperto”. Dal punto di vista del ciclo di vita delle attività UI, non UI e misti, il sistema impone un limite di avanzamento all’interno del workflow oltre il quale l’attività non può essere rimessa in stato “Aperto” e quindi non può essere ripianificata. Secondo quanto rappresentato dalle frecce blu, per il processo UI, questo limite è lo stato “Schedulato”, mentre per i processi non UI e misti, questo limite è caratterizzato dallo stato “Eseguibile”.

Figura - Flusso stati OdL (caso UI e misto) con evidenza al flusso di ritorno in stato "Aperto"

Figura - Flusso stati OdL (caso non UI) con evidenza al flusso di ritorno in stato "Aperto"

La modifica di stato in “Aperto” avviene selezionando l’azione “Ripianifica OdL”, evidenziata . Selezionando l’azione, il sistema mostra un warning per indicare all’utente l’imminente modifica di stato e chiede la sua conferma per procedere con l’operazione.

Figura - Schermata di dettaglio dell'OdL, con evidenza all'azione di ripianificazione dell'OdL

Figura - Warning dell’impossibilità di ripianificazione a causa di stato non idoneo

Effettuare le modifiche necessarie: una volta che l’OdL si trova in stato “Aperto”, è possibile intervenire sui principali dati di pianificazione, come date, durata, risorse tecniche e altri campi gestionali. Le modifiche possono essere effettuate sui seguenti campi dell’OdL:

Cliente

Identificativo PdL

Data inizio pianificata

Data fine pianificata

Data massimo posticipo

Data non prima di

Durata

Risorse pianificate

Tipo spesa

Codice CoIn

Dati di contabilità

Salvare e confermare la ripianificazione: al termine della modifica, l’utente deve salvare le nuove informazioni selezionando l’icona di salvataggio presente nella schermata dell’applicazione “Monitoraggio OdL”. Se necessario, deve riportare manualmente l’OdL nello stato precedente selezionando l’azione “Inoltra workflow”.

Figura - Schermata di dettaglio dell'OdL, con evidenza alle azioni "Inoltra workflow"

Modifica massiva OdL (date)

La funzione è abilitata esclusivamente per gli utenti con profilo P1, P5, P6, P7, P8, P15, P16, P17, secondo le autorizzazioni di sistema.

In EAM è disponibile una funzionalità dedicata alla modifica massiva degli OdL utile per aggiornare contemporaneamente più record relativi a uno o più campi modificabili. Questa operazione viene eseguita tramite l’utilizzo dello strumento standard MxLoader, che consente di caricare nel sistema un file contenente le modifiche da applicare agli OdL.

Per eseguire correttamente la modifica massiva, è necessario eseguire i seguenti passaggi:

Accedere al file MxLoader e verificare la connessione: selezionare il file MxLoader e cliccare su “Test Connection” per controllare se il file è connesso alla piattaforma EAM. Se la connessione è stabile, il sistema mostra l’avviso.

Figura - Tab "Config" dello strumento "MxLoader", con evidenza al tasto "Test connection"

Figura - Tab "Config" dello strumento "MxLoader", con evidenza all’avviso di connessione andata a buon fine

Impostare il campo “C1” e cercare gli OdL di interesse: selezionare il campo “C1” del tab “Workorder” e impostare il valore in “Query”. Prima di procedere alla modifica massiva, l’utente deve indicare nell’MxLoader quali OdL vanno modificati. Nel campo “D1”, vanno inseriti i codici degli OdL da modificare, rispettando la seguente sintassi:

wonum in [“CODICEODL1”,”CODICEODL2”,”CODICEODL3”,”…”]

Dopo aver inserito la query nel campo “D1”, l’utente seleziona il tasto “Run” per visualizzare gli OdL.

Figura - Tab "Workorder" dello strumento "MxLoader", con evidenza alla cella “D1” con l’impostazione richiesta per la generazione massiva di OdL e al tasto “Run”

Impostare le celle “C1” e “D1”: dopo aver effettuato la query, è necessario impostare il campo “C1” in “AddChange” e svuotare il campo “D1”.

Modificare i campi: all’interno del tab “Workorder”, l’utente può modificare le celle desiderate. Nello specifico, deve essere rispettata la formattazione e gli OdL da modificare devono essere in stato “Aperto” (se necessario, ripianificare l’OdL prima di avviare la procedura di modifica). Dopo aver modificato i campi desiderati, selezionare il tasto “Run” per confermare la modifica. Qualora la procedura non sia andata a buon fine, sarà presente un messaggio di errore in colonna “Result” (AH) del file. Una volta completata la modifica del file ed eseguito il “Run”, EAM procederà ad allineare automaticamente a sistema le informazioni di ogni OdL modificato sul file Excel.

Figura - Tab "Workorder" dello strumento "MxLoader", contenente gli OdL da modificare

Nota: il caricamento errato del file (ad esempio per formattazione non conforme o valori non validi) può generare errori bloccanti e impedire l’aggiornamento degli OdL. Si raccomanda di verificare attentamente il contenuto del file prima dell’importazione.

Cambio responsabile attività

Per modificare il responsabile attività associato a un OdL durante la fase di pianificazione tecnica, è necessario seguire i passaggi riportati di seguito:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL di interesse: utilizzando il filtro di ricerca presente in alto a destra nella schermata, l’utente può individuare l’OdL per cui si desidera modificare il responsabile attività. Una volta trovato, cliccare sull’OdL per accedere alla maschera di dettaglio dell’OdL.

Figura - Vista iniziale dell'applicazione "Monitoraggio OdL", con evidenza all'icona di filtro di ricerca

Impostare lo stato in “Aperto”: la ripianificazione di un’OdL è prevista esclusivamente quando l’OdL è in stato “Aperto”. Nello specifico, potranno essere portati nello stato idoneo alla ripianificazione gli OdL che, al massimo, hanno raggiunto gli stati “Schedulabile”, “Schedulato” (processo UI e Misto) e “Eseguibile” (processo non UI). Una volta rispettate questa condizione, la modifica di stato a “Aperto” deve essere effettuata selezionando l’azione “Ripianifica OdL”.

Figura - Schermata di dettaglio dell'OdL, con evidenza all'azione di ripianificazione dell'OdL

Figura - Warning dell’impossibilità di ripianificazione a causa di stato non idoneo

Individuare il campo “Responsabile attività”: per localizzare il campo “Responsabile attività”, l’utente deve indirizzarsi alla sezione “Responsabilità”, presente nella parte inferiore del tab “Ordini di lavoro”. È da evidenziare che il campo “Responsabile attività”, di default, viene automaticamente popolato in base all’asset associato e al PdL selezionato ed è in read only nel caso in cui l’attività si trova in uno stato diverso da “Aperto”.

Figura - Schermata di dettaglio dell'OdL, con evidenza al campo "Responsabile attività"

Modificare il valore del campo: per modificare il campo, l’utente deve selezionare la freccia al suo all’interno e scegliere dalla dialog il nuovo responsabile attività da associare all’OdL. Qualora sia necessario, è possibile filtrare l’elenco di record visibili inserendo il valore ricercato nei campi.

Figura - Dialog di selezione del Responsabile attività

Riassegnare l’OdL: dopo aver aggiornato il campo, l’utente deve cliccare sul pulsante “Inoltra workflow” in alto a destra per aprire la finestra di riassegnazione OdL. L’utente deve selezionare l’opzione e cliccare sul tasto “Ok” per effettuare la riassegnazione dell’OdL.

Figura - Schermata di dettaglio dell'OdL, con evidenza all'azione "Inoltra workflow"

Figura - Dialog di riassegnazione di responsabile attività, con evidenza al tasto di conferma

## 5.8 Pianificazione economica degli OdL

La funzione è abilitata esclusivamente per gli utenti con profilo P1, P3, P5, P6, P7, P8, P11, P16, P17, secondo le autorizzazioni di sistema.

Nell’ambito della gestione degli Ordini di Lavoro, la pianificazione economica rappresenta una fase cruciale per l’associazione dei budget previsti alle corrette categorie di spesa. Le tipologie di spesa gestite all’interno del sistema sono:

CAPEX (Capital Expenditure): spese in conto capitale, associate ad attività che generano un incremento del valore patrimoniale.

OPEX (Operating Expenditure): spese operative, relative ad attività di manutenzione ordinaria o gestione corrente degli asset, senza incremento del valore patrimoniale.

ANR (Attività non regolamentate): spese non riconducibili direttamente a CAPEX o OPEX, tipicamente associate ad attività interne o non soggette a capitalizzazione o monitoraggio economico.

La tabella sottostante riporta l’elenco di informazioni che, combinate, rappresentano gli elementi fondamentali per una corretta gestione, classificazione e tracciabilità degli OdL in termini economici. Nello specifico, l’insieme di campi visibili in maschera è variabile a seconda del tipo spesa selezionato, come dimostrato nel corso del capitolo.

| CAMPO | CAPEX | OPEX | ANR |
| --- | --- | --- | --- |
| Codice CoIn | Presente | Presente | Presente |
| Tipo attività PM | Non presente | Presente | Presente |
| Tipo network | Presente non valorizzato | Non presente | Non presente |
| Tipo ordine OI | Non presente | Non presente | Presente |
| Tipo ordine OdM | Non presente | Presente | Presente |
| CBS contabile | Presente | Non presente | Presente |
| CBE contabile | Presente | Non presente | Presente |
| CBS di pianificazione | Non presente | Presente non valorizzato | Presente non valorizzato |
| CBE di pianificazione | Non presente | Presente non valorizzato | Presente non valorizzato |
| Numero ordine interno | Non presente | Non presente | Presente |
| Identificativo NTK | Presente | Non presente | Non presente |
| Operazione | Presente | Non presente | Non presente |
| Identificativo OdM | Non presente | Presente | Presente |

Tabella - Tabella contenente i campi presenti, non presenti e presenti non valorizzati a seconda del tipo spesa

Per consultare o impostare le informazioni relative alla pianificazione economica di un Ordine di Lavoro, è necessario seguire i seguenti passaggi:

Nota: il presente capitolo descrive la casistica di gestione delle informazioni contabili degli OdL già presenti in EAM. Per la pianificazione economica durante la generazione dell’OdL, considerare lo step “2” per la generazione di un nuovo OdL.

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL di interesse: utilizzando il filtro di ricerca presente in alto a destra nella schermata, l’utente può individuare l’OdL per cui si desidera modificare il responsabile attività. Una volta trovato, cliccare sull’OdL per accedere alla maschera di dettaglio dell’OdL.

Figura - Vista iniziale dell'applicazione "Monitoraggio OdL", con evidenza all'icona di filtro di ricerca

Accedere alla scheda “CoIn”: all’interno dell’Ordine di Lavoro, navigare tra le schede disponibili e selezionare la scheda denominata “CoIn”. In questa sezione è possibile visualizzare e compilare le informazioni contabili coinvolte nel processo di pianificazione economica dell’attività.

Figura - Schermata di dettaglio del tab "CoIn" all'interno dell'applicazione "Monitoraggio OdL"

Nota: per poter procedere al salvataggio di un nuovo OdL in stato “Aperto”, devono essere valorizzate almeno i campi “Tipo spesa” e “Codice CoIn”. Questi campi, anche se modificabili, vengono spesso popolati in modo automatico dal sistema sulla base del PdL selezionato e l’attività standard associata.

Compilare i campi: nei paragrafi successivi sono descritti i processi di consultazione e valorizzazione dei dati contabili a seconda del Tipo spesa utilizzato.

Tipo spesa CAPEX

All’interno del tab CoIn, l’utente deve verificare la presenza della voce “Capex” nel campo Tipo spesa. Se valorizzato con questa voce, il sistema imposta automaticamente la schermata con i seguenti che devono essere compilati come di seguito descritto ai fini della corretta pianificazione economica:

Figura - Schermata di dettaglio del tab "CoIn" all'interno dell'applicazione "Monitoraggio OdL", con evidenza al tipo spesa "CAPEX"

Tipo spesa: indica la categoria economica selezionata. Il campo, sebbene modificabile, è di default pre-valorizzato dal sistema sulla base del valore prevalente presente nel catalogo standard.

Codice CoIn: codice identificativo utilizzato per la registrazione contabile dell’investimento. Il campo viene valorizzato automaticamente dal sistema sulla base del catalogo standard associato all’PdL. L’utente ha comunque la possibilità di modificarne il valore selezionando un’alternativa tra quelle disponibili nel menu a tendina.

ID Attività Standard: codice associato all’attività standard predefinita collegata all’OdL. L’informazione è definita in base al PdL associato all’ordine di lavoro e non è modificabile.

Identificativo NTK: riferimento progettuale che consente l’aggregazione delle spese in ambito pianificazione. La valorizzazione del campo “Identificativo NTK” può essere definita in due modalità:

Casistica NTK esistente: l’utente deve selezionare manualmente una NTK tra quelle proposte dal sistema. Il sistema restituisce in automatico una lista filtrata di NTK che sono contemporaneamente in stato idoneo per la selezione e che rispettano le condizioni minimi richieste.

Figura - Dialog di selezione dell'identificativo NTK"

Casistica NTK non esistente: una volta selezionata la CBE, l’utente deve utilizzare l’interfaccia di creazione integrata nella maschera selezionando il tasto “Richiesta creazione in SAP”. Il sistema procederà alla generazione automatica della NTK e della relativa Operazione, sulla base della CBE selezionata nello step precedente.

Figura - Schermata di dettaglio del tab "CoIn" all'interno dell'applicazione "Monitoraggio OdL", con evidenza al tasto “Richiesta creazione in SAP”

CBS Contabile: struttura di suddivisione contabile relativa al progetto o asset. Nel caso di NTK non esistente, l’utente deve selezionare la CBS contabile tra quelle proposte dal sistema. In caso di NTK esistente, invece, il sistema valorizza automaticamente la CBS sulla base della NTK selezionata.

CBE Contabile: elemento contabile che dettaglia la voce di spesa associata all’attività. Nel caso di NTK non esistente, l’utente deve selezionare la CBE contabile tra quelle proposte dal sistema. In caso di NTK esistente, invece, il sistema valorizza automaticamente la CBE sulla base della NTK selezionata.

Operazione: campo descrittivo relativo alla specifica operazione prevista nell’ambito dell’attività CAPEX. Il campo “Operazione” viene automaticamente valorizzato dal sistema contestualmente alla valorizzazione del campo “Identificativo NTK”. Il campo è in sola lettura e non può essere modificato manualmente.

La figura sottostante rappresenta il tab “CoIn” dopo la valorizzazione di tutti i campi necessari alla caratterizzazione contabile dell’OdL pianificato per il tipo spesa Capex.

Figura - Schermata di dettaglio del tab "CoIn" all'interno dell'applicazione "Monitoraggio OdL", con tipo spesa CAPEX e campi compilati

Nota: per poter procedere al salvataggio di un nuovo OdL in stato “Aperto”, devono essere valorizzate almeno i campi “Tipo spesa” e “Codice CoIn”. Questi campi, anche se modificabili, vengono spesso popolati in modo automatico dal sistema sulla base del PdL selezionato e l’attività standard associata.

Tipo spesa OPEX

All’interno del tab CoIn, l’utente deve verificare la presenza della voce “Opex” nel campo Tipo spesa. Se valorizzato con questa voce, il sistema imposta automaticamente la schermata con i seguenti campi che devono essere compilati come di seguito descritto ai fini della corretta pianificazione economica:

Figura - Schermata di dettaglio del tab "CoIn" all'interno dell'applicazione "Monitoraggio OdL", con evidenza al tipo spesa "OPEX"

Tipo spesa: indica la categoria economica selezionata, in questo caso OPEX. Il campo viene valorizzato automaticamente dal sistema sulla base del catalogo standard associato all’OdL. L’utente ha comunque la possibilità di modificarne il valore selezionando un’alternativa tra quelle disponibili nel menu a tendina.

ID Attività Standard: codice associato all’attività standard predefinita collegata all’OdL. L’informazione è definita in base al PdL associato all’ordine di lavoro e non è modificabile.

Codice CoIn: codice identificativo utilizzato per la registrazione contabile della spesa operativa. Il campo viene valorizzato automaticamente dal sistema sulla base del catalogo standard associato all’OdL. L’utente ha comunque la possibilità di modificarne il valore selezionando un’alternativa tra quelle disponibili nel menu a tendina.

Tipo attività PM: campo descrittivo che indica la tipologia di attività prevista nel piano di manutenzione. Il campo viene valorizzato automaticamente dal sistema sulla base della relazione tra Codice COIN e attività PM. In particolare, il sistema imposta il valore corrispondente alla stringa presente dopo l’ultimo punto del Codice COIN (es. se il codice COIN è EA101B1LCD.CTO, il campo viene valorizzato con CTO).

Tipo OdM: campo che specifica la natura dell’OdM associato, utile per la classificazione degli interventi manutentivi. Il campo viene valorizzato automaticamente in base alla relazione tra il Codice COIN e la tipologia di ordine di manutenzione corrispondente. Non è richiesta alcuna azione manuale da parte dell’utente.

Identificativo OdM: riferimento all’Ordine di Manutenzione (OdM) associato all’attività. La valorizzazione del campo “Identificativo OdM” può avvenire secondo due modalità:

Casistica OdM esistente: l’utente deve selezionare manualmente un Ordine di Manutenzione (OdM) tra quelli proposti dal sistema.

Casistica OdM non esistente: qualora non sia disponibile un OdM esistente, l’utente può utilizzare l’interfaccia di creazione integrata nella maschera selezionando il tasto “Richiesta creazione in SAP”. Il sistema genera automaticamente un nuovo Ordine di Manutenzione sulla base dei valori presenti nei campi Tipo attività PM e Tipo ordine OdM.

Figura - Schermata di dettaglio del tab "CoIn" all'interno dell'applicazione "Monitoraggio OdL", con evidenza al tasto “Richiesta creazione in SAP”

La figura sottostante rappresenta il tab “CoIn” dopo la valorizzazione di tutti i campi necessari alla caratterizzazione contabile dell’OdL pianificato per il tipo spesa Opex.

Figura - Schermata di dettaglio del tab "CoIn" all'interno dell'applicazione "Monitoraggio OdL", con tipo spesa OPEX e campi compilati

Nota: per poter procedere al salvataggio di un nuovo OdL in stato “Aperto”, devono essere valorizzati almeno i campi “Tipo spesa” e “Codice CoIn”. Questi campi, anche se modificabili, vengono spesso popolati in modo automatico dal sistema sulla base del PdL selezionato e l’attività standard associata.

Tipo spesa ANR

All’atterraggio sul tab CoIn, l’utente deve verificare la presenza della voce “ANR” nel campo Tipo spesa. Se valorizzato con questa voce, il sistema imposta automaticamente la schermata con i seguenti campi che devono essere compilati come di seguito descritto ai fini della corretta pianificazione economica:

Figura - Schermata di dettaglio del tab "CoIn" all'interno dell'applicazione "Monitoraggio OdL", con evidenza al tipo spesa "ANR"

Codice COIN: codice identificativo utilizzato per la registrazione contabile dell’attività. il campo viene valorizzato automaticamente dal sistema sulla base del catalogo standard associato all’OdL. L’utente ha comunque la possibilità di modificarne il valore selezionando un’alternativa tra quelle disponibili nel menu a tendina.

Tipo attività PM: campo descrittivo che indica la tipologia di attività di manutenzione pianificata collegata all’OdL. Il campo viene popolato automaticamente dal sistema in base alla relazione con il Codice COIN. In particolare, viene assegnato il valore corrispondente alla parte finale del codice (es. se il Codice COIN è AT_M.MAL.CTO, il sistema imposta CTO).

Identificativo OdM: codice univoco di riferimento per l’Ordine di Manutenzione eventualmente associato. La valorizzazione del campo “Identificativo OdM” può avvenire secondo due modalità:

Casistica OdM esistente: la valorizzazione del campo “Identificativo OdM” in questi casi è prevista in due modalità:

Selezione diretta dell’identificativo OdM: l’utente seleziona manualmente un OdM da una lista predefinita. A seguito della selezione dell’OdM, il sistema valorizza automaticamente i seguenti campi:

Tipo ordine OdM: valorizzato in base all’OdM selezionato (PMT9 o PMTG)

CBS / CBE contabile: popolati automaticamente in base all’OdM solo per ordini con tipo attività EPC.

Numero ordine interno: popolati automaticamente in base all’OdM inserito.

Tipo ordine OI: assegnato automaticamente dal sistema. Assume il valore “T06” se l’OdM è di tipo “PMT9” e “T09” se l’OdM è di tipo “PMTG”

Selezione dell’ordine interno e successiva selezione dell’OdM: in alternativa, l’utente può valorizzare manualmente il campo “Numero ordine interno” selezionandolo da una lista contenente tutti gli ordini ammissibili. In base al “Numero ordine interno” selezionato, il sistema valorizza automaticamente i seguenti campi:

Tipo ordine OI: assegnato automaticamente dal sistema. Può assumere i valori T06 o T09. Assume il valore “T06” se il campo “Tipo OdM” è valorizzato con “PMT9” e “T09” se il campo “Tipo OdM” è valorizzato con “PMTG”.

Identificativo OdM: definito da una lista fornita dal sistema.

Tipo OdM: campo popolato automaticamente in base ai valori inseriti nei campi “Identificativo OdM” e “Tipo ordine OI”. Può assumere i valori “PMT9” o “PMTG”?

Casistica OdM non esistente: la valorizzazione del campo “Identificativo OdM” in questi casi è prevista in tre modalità:

“Tipo attività PM” = EPC e regola di scarico costi a CBS / CBE contabile: in questa casistica, l’utente deve prima popolare il campo “CBS / CBE contabile” da una lista prefiltrata. L’azione rende il campo “Numero ordine interno” non più editabile. Il campo “Identificativo OdM” va popolato in base alla CBE utilizzata nello step precedente, tramite l’azione “Richiesta NTK / OdM”. A seguito della selezione dell’OdM, il sistema valorizza automaticamente il campo “Tipo OdM” con il valore “PMT9".

“Tipo attività PM” = EPC e regola di scarico costi a ordine interno: in questa casistica, l’utente deve prima popolare il campo “Numero ordine interno” da una lista prefiltrata. Tale azione implica che il campo “Tipo OI” venga valorizzato automaticamente (il campo assume il valore T06 o T09). La compilazione automatica del campo “Tipo ordine OI” rende i campi “CBS / CBE contabile” non più modificabili. Il campo “Identificativo OdM” va popolato in base alla CBE utilizzata nello step precedente, tramite l’azione “Richiesta NTK / OdM”. A seguito della selezione dell’OdM, il sistema valorizza automaticamente il campo “Tipo OdM” con il valore “PMT9" o “PMTG”.

Tipo attività PM ≠ EPC: in questa casistica, l’utente compila il campo “Numero ordine interno”, selezionando il valore da una lista prefiltrata. Tale azione implica che il campo “Tipo OI” venga valorizzato automaticamente (il campo assume il valore T06 o T09). Il campo “Identificativo OdM” va popolato in base al Tipo ordine OI valorizzato nello step precedente, tramite l’azione “Richiesta NTK / OdM”.

La Figura sottostante rappresenta il tab “CoIn” dopo la valorizzazione di tutti i campi necessari alla caratterizzazione contabile dell’OdL pianificato per il tipo spesa ANR.

Figura - Schermata di dettaglio del tab "CoIn" all'interno dell'applicazione "Monitoraggio OdL", con tipo spesa ANR e campi compilati

Nota: per poter procedere al salvataggio di un nuovo OdL in stato “Aperto”, devono essere valorizzati almeno i campi “Tipo spesa” e “Codice CoIn”. Questi campi, anche se modificabili, vengono spesso popolati in modo automatico dal sistema sulla base del PdL selezionato e l’attività standard associata.

## 5.9 Modifica dati contabili ad esecuzione avviata Una volta che l’Ordine di Lavoro ha superato lo stato “Aperto” e l’esecuzione è stata avviata, alcuni campi contabili restano comunque modificabili per consentire l’adeguamento della pianificazione economica in corso d’opera. Nello specifico, la modifica dei dati contabili di un OdL è possibile solo se rispettate le seguenti condizioni:

Nessun consuntivo è stato inviato a SAP

I campi “Identificativo NTK” o “Identificativo OdM” non sono valorizzati.

Per modificare i dati contabili degli OdL, l’utente deve eseguire i seguenti passaggi: anche dopo la transizione dello stato OdL, l’utente può accedere al tab COIN e aggiornare i seguenti campi:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL di interesse: utilizzando il filtro di ricerca presente in alto a destra nella schermata, l’utente può individuare l’OdL per cui si desidera modificare il responsabile attività. Una volta trovato, cliccare sull’OdL per accedere alla maschera di dettaglio.

Figura - Vista inziale dell'applicazione "Monitoraggio OdL", con evidenza al filtro di ricerca

Accedere alla scheda “CoIn”: all’interno dell’OdL, navigare tra le schede disponibili e selezionare la scheda denominata “CoIn”. In questa sezione è possibile visualizzare e compilare le informazioni contabili coinvolte nel processo di pianificazione economica dell’attività.

Figura - Schermata di dettaglio del tab "CoIn" all'interno dell'applicazione "Monitoraggio OdL"

Effettuare le modifiche: all’interno del tab CoIn, l’utente può realizzare le modifiche necessarie. La modifica non è consentita se i campi “Identificativo OdM” o “Identificativo NTK” risultano valorizzati. In questo caso, è necessario cliccare sul tasto “Disassocia NTK / OdM”, evidenziato in Figura, per effettuare delle modifiche.

Figura - Schermata di dettaglio del tab "CoIn" all'interno dell'applicazione "Monitoraggio OdL", con evidenza al tasto “Disassocia NTK / OdM”

All’interno del tab “CoIn” l’utente può modificare manualmente i seguenti campi:

Tipo spesa CAPEX

Tipo spesa

Codice CoIn

CBS Contabile

CBE Contabile

Identificativo NTK (se non ancora presente)

Tipo spesa OPEX

Tipo spesa

Codice CoIn

Identificativo OdM (se non ancora presente)

Tipo spesa ANR

Tipo spesa

Codice CoIn

CBS contabile

CBE contabile

Identificativo OdM (se non ancora presente)

Numero ordine interno

## 5.10 Invio OdL a Geocall

Nell’ambito della pianificazione tecnica, gli OdL possono essere suddivisi nelle seguenti categorie a seconda del Titris associato alle risorse assegnate.

Caso OdL non UI: tutte le risorse pianificate sull’OdL appartengono a Titris non UI

Caso OdL UI: tutte le risorse pianificate sull’OdL appartengono a Titris UI

Caso OdL misto: sono pianificate sull’OdL delle risorse che appartengono sia a Titris UI che non UI.

Al termine della fase di pianificazione tecnica ed economica dell’OdL, eseguita all’interno della piattaforma EAM, l’ordine prosegue alla fase di schedulazione, passaggio cruciale del ciclo di vita dell’attività. Nello specifico, il processo di schedulazione dell’OdL viene gestito in maniera differente a seconda dei Titris associati alle risorse pianificate: su EAM per gli OdL non UI e su Geocall per le casistiche UI e Misto. Questo capitolo descrive nel dettaglio il processo di invio degli OdL UI e Misti verso Geocall ai fini della loro schedulazione.

I seguenti paragrafi approfondano i processi di invio di OdL verso Geocall sia in modo puntale che massivo.

Invio puntuale

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL di interesse: qualora l’OdL da inviare verso Geocall sia stato generato, utilizzando il filtro di ricerca presente in alto a destra nella schermata, l’utente può individuare l’OdL che vuole inviare a Geocall (l’OdL deve obbligatoriamente appartenere alla casistica “Non UI” o “Misto”). Una volta trovato, cliccare sull’OdL per accedere alla maschera di dettaglio dell’OdL.

Figura - Vista inziale dell'applicazione "Monitoraggio OdL", con evidenza al filtro di ricerca

Verificare la compilazione dei campi obbligatori: prima di inviare l’OdL a Geocall, l’utente deve assicurarsi che sono state fornite le seguenti informazioni sull’OdL:

Codice OdL

Identificativo PdL

Tipo spesa

Codice CoIn

Durata attività

Data non prima di

Data di massimo posticipo

Data inizio pianificazione

Data fine pianificazione

Asset

Tipologia assistenza / sorveglianza (se presente)

Risorse pianificate (qualifica, quantità, ore esecuzione, Titris, data inizio e fine risorsa)

Inviare l’OdL a Geocall: una volta realizzati gli step descritti in precedenza, l’utente deve procedere all’invio dell’OdL verso Geocall. Per effettuare l’invio in modo puntuale, l’utente deve selezionare l’azione “Inoltra workflow”e confermare il cambio stato in “Schedulabile” selezionando l’opzione evidenziate.

Figura - Schermata di dettaglio dell'applicazione "Monitoraggio OdL", con evidenza all'icona "Inoltra workflow"

Figura - Dialog di modifica stato, con evidenza all'opzione di modifica allo stato "Schedulabile" e al tasto di conferma

Appena passato l’OdL allo stato “Schedulabile”, il sistema invia in modo automatico a Geocall le informazioni comprese nell’elenco di seguito descritte:

Codice OdL

Codice PdL associato

Descrizione OdL

Titris risorse pianificate

Data non prima di

Data di massimo posticipo

Data inizio pianificata

Data fine pianificata

Data inizio indisponibilità

Data fine indisponibilità

Stato richiesta indisponibilità

Codice asset EAM

SAP ID asset

Tipo spesa

Codice delibera indisponibilità (se presente)

Flag indisponibilità

Figura - Schermata di dettaglio dell'applicazione "Monitoraggio OdL", con evidenza allo stato “Schedulabile”

Nota: qualora uno dei campi descritti nello step 3 non sia stato valorizzato alla selezione del tasto “Inoltra workflow”, il sistema inibisce l’invio e fornisce un warning contenente i campi ancora da compilare.

Figura - Warning dei campi mancanti alla modifica stato

Invio massivo

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare ed inviare gli OdL: nella vista elenco iniziale dell’applicazione “Monitoraggio OdL”, l’utente deve prima selezionare l’opzione “Invio OdL Massivo verso Geocall”.

Figura - Vista elenco iniziale dell'applicazione "Monitoraggio OdL", con evidenza all'azione di invio massivo verso Geocall

Il sistema fornisce una tabella contenente gli OdL da inviare verso Geocall. Per facilitare la ricerca, l’utente può filtrare gli OdL di interesse per qualsiasi colonna presente nella dialog e, dopo aver selezionato gli OdL da inviare verso Geocall, clicca su “OK” per effettuare l’invio.

Figura - Dialog di selezione degli OdL da inviare verso Geocall

L’invio degli OdL tramite la procedura di invio massivo appena descritta risulta nella modifica automatica di stato degli OdL selezionati in “Schedulabile”. Le informazioni trasmesse a Geocall sono descritte nell’elenco puntato di seguito:

Codice OdL

Codice PdL associato

Descrizione OdL

Titris risorse pianificate

Data non prima di

Data di massimo posticipo

Data inizio pianificata

Data fine pianificata

Codice asset EAM

SAP ID asset

Tipo spesa

Codice delibera indisponibilità (se presente)

Flag indisponibilità

## 5.11 Gestione indisponibilità

Nell’ambito delle attività gestite tramite il sistema EAM, sono presenti alcune attività per le quali è necessaria una richiesta di indisponibilità dell’asset oggetto dell’OdL, degli asset interferenti o collegati elettricamente. La richiesta di indisponibilità, pianificata e deliberata nel sistema GIN, ha lo scopo di garantire che gli asset sui quali si intende effettuare un intervento di manutenzione / sostituzione, nonché quelli interferenti, siano fuori servizio durante il periodo programmato per l’esecuzione del lavoro.

Le richieste di indisponibilità possono essere monitorate all’interno dell’OdL nel tab “Indisponibilità”. La Tabella III riporta le informazioni contenute nel tab:

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| Cluster titolare | Codice univoco identificativo del cluster di indisponibilità per l’ordine di lavoro | Popolato automaticamente | Obbligatorio |  |
| Codice OdL | Codice dell'ordine di lavoro EAM comunicato con interfaccia RichiestaPeriodoIndisponibilità | Popolato automaticamente | Obbligatorio |  |
| Data inizio indisponibilità | Campo riportante la data di inizio indisponibilità. Sarà valorizzata o meno a seconda dello stato richiesta | Popolato automaticamente | Obbligatorio |  |
| Data fine indisponibilità | Campo riportante la data di fine indisponibilità. Sarà valorizzata o meno a seconda dello stato richiesta | Popolato automaticamente | Obbligatorio | Obbligatorio |
| Stato richiesta | Campo riportante lo stato della richiesta su GIN e che andrà allineato in EAM | Popolato automaticamente | Obbligatorio |  |
| Motivazione (MOD) | Utilizzato nel caso dell’annullamento della richiesta di indisponibilità per determinare il sistema che l’ha originata. Può essere valorizzato con “EAM”, in caso di cambio del PdL dell’OdL in EAM con uno che non necessita l’indisponibilità, e “Dispacciamento”, nel caso in cui la richiesta venga annullata dal dispacciamento | Popolato automaticamente | Dipende dallo stato |  |
| Esito | Esito invio verso GIN (OK/KO) | Popolato automaticamente | Obbligatorio |  |
| Codice errore | Codice dell’errore registrato su GIN in caso di KO | Popolato automaticamente | Dipende dall’esito |  |
| Descrizione errore | Descrizione esplicativa dell’errore registrato | Pick-list | Dipende dall’esito |  |
| Tabella storico richieste | Tabella che consente di visualizzare lo storico delle richieste di indisponibilità associate all’OdL. Per ciascuna richiesta sono indicati i seguenti dati: data inizio indisponibilità, data fine indisponibilità, stato, motivazione, cluster titolare | Popolato automaticamente | Obbligatorio |  |

Tabella - Campi facoltativi e obbligatori del tab “Indisponibilità” dell’OdL

### 5.11.1 Creazione e delibera

La funzione è abilitata esclusivamente per gli utenti con profili P1, P3, P5, P6, P16, P7, P8, P17, P11, P15, secondo le autorizzazioni di sistema.

Una volta creata, la richiesta di indisponibilità può attraversare i seguenti stati:

Inviato: L’OdL è stato inviato a GIN e non è ancora stata creata una richiesta di indisponibilità;

In lavorazione: La richiesta di indisponibilità è in fase di valutazione e lavorazione su GIN;

Deliberato: La richiesta di indisponibilità è stata lavorata in GIN e EAM è stato aggiornato con le date di indisponibilità. L’esito della delibera potrà essere “OK” o “KO” a seconda che le date di indisponibilità deliberate coincidano o meno con le date di pianificazione proposte;

In esecuzione: La richiesta di indisponibilità è in esecuzione. Il campo esito è popolato con “OK”;

Consuntivato: La richiesta di indisponibilità è consuntivata. Il campo esito è popolato con “OK”;

Sospeso: A seguito della delibera, GIN può sospendere la richiesta poiché non è più possibile garantire l’indisponibilità nelle date definite. Il Cluster titolare in questo caso non cambia, per cui EAM potrà eventualmente chiedere nuovamente l’indisponibilità sullo stesso Cluster con date di pianificazione differenti. Il campo esito è popolato con “KO”;

Annullato: La richiesta di indisponibilità viene annullata. L’annullamento può avvenire su input del dispacciamento o di EAM a seguito di una modifica del PdL associato all’OdL con un altro che non richiede l’indisponibilità su EAM. Il campo “Motivazione” verrà utilizzato per discriminare il tipo di annullamento. Questo aggiornamento di stato può arrivare anche a seguito di una richiesta già deliberata ma per cui non è più possibile garantire l’indisponibilità nelle date definite. Il campo esito è popolato con “KO”.

Per visualizzare l’esito di una richiesta di indisponibilità, l’utente deve fare i seguenti passaggi:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Applicazione "Monitoraggio OdL"

Creare un nuovo OdL: popolare tutti i campi obbligatori, scegliendo un PdL che prevede l’indisponibilità.

Figura - Flag "Indisponibilità necessaria?"

Raggiungere le condizioni necessarie per l’invio della richiesta: le richieste di indisponibilità vengono generate nel sistema GIN sulla base delle richieste associate agli OdL ricevuti da EAM. Gli OdL vengono inviati a GIN in automatico quando sono stati valorizzati una serie di campi. Tali campi sono i seguenti:

Codice OdL (obbligatorio): Codice univoco identificativo dell’OdL per la quale si richiede l’indisponibilità.

Descrizione OdL (obbligatorio): Descrizione dell’OdL per la quale si richiede l’indisponibilità.

CBS (facoltativo): campo non obbligatorio, informazione recuperata dal tab CoIn dell’OdL.

Tipologia attività (aggregatore per GIN) (facoltativo): Mapping di aggregazione tra piano di lavoro e tipologia attività su GIN;

Tipo spesa (obbligatorio): Campo che caratterizza il tipo di OdL (CAPEX, OPEX, ANR).

Responsabile attività (obbligatorio): Unità impianti responsabile dell’OdL.

Codice sede tecnica di indisponibilità (obbligatorio): Lista di sedi tecniche d’indisponibilità associata alla sede tecnica Reale e presente in anagrafica (sede tecnica indisponibilità master). Verranno veicolate nel dettaglio i codici di sede tecnica indisponibilità strettamente connesse all’asset oggetto dell’OdL.

Sede tecnica d’indisponibilità padre (obbligatorio): Sede tecnica d’indisponibilità legata all’Asset di tipo Linea o Stazione (gerarchicamente legato all’asset reale dell’ordine di lavoro)

Sedi tecniche interferenti (facoltativo): lista codici sedi tecniche interferenti (opere interferenti). Viene valorizzata automaticamente a valle della selezione dell’asset dell’OdL solo nel caso in cui siano presenti delle opere interferenti sull’asset per il quale viene richiesta l’indisponibilità.

Data pianificata inizio (obbligatorio): Data pianificata della singola attività.

Data pianificata fine (obbligatorio): Data pianificata della singola attività.

PdL (obbligatorio): Piano di lavoro associato all’OdL.

Nota: se l’OdL viene inviato a Geocall prima della delibera, viene inviato senza le date di indisponibilità.

Selezionare il tab “Indisponibilità”: una volta avvenuto l’invio automatico dell’OdL a GIN, i campi inerenti alla richiesta di indisponibilità sono ancora vuoti, e lo stato della richiesta passa a “Inviato”.

Figura - - Richiesta indisponibilità inviata

Verificare la creazione della richiesta in GIN: quando GIN crea la richiesta, in EAM vengono popolati i campi con i dati della richiesta e lo stato cambia a “in lavorazione”.

Figura - Richiesta indisponibilità in lavorazione

Nota: l’utente potrebbe effettuare delle modifiche all’OdL in pianificazione. Nel caso in cui tali modifiche abbiano qualche impatto sulla richiesta di indisponibilità creata in GIN, il sistema genera un alert informativo non bloccante prima del salvataggio. Se confermati, gli aggiornamenti vengono trasmessi a GIN. Se l’OdL viene inviato a Geocall prima della delibera, EAM può ricevere aggiornamenti delle date di pianificazione da Geocall. In quel caso li trasmette al sistema GIN.

Figura - Warning modifiche con impatto su GIN

Verificare l’esito della richiesta: una volta deliberata in Agile, EAM riceve i dati definitivi della richiesta (che potrebbero essere diversi da quelli indicati in fase di creazione). Lo stato della richiesta in EAM viene modificato in “Deliberato”, e le date di indisponibilità vengono aggiornate con le date deliberate.

Figura - Richiesta indisponibilità deliberata

Nota: se l’OdL era stato inviato a Geocall prima della delibera, EAM invia a Geocall le date di indisponibilità deliberate non appena le riceve da GIN.

### 5.11.2 Centro di controllo per la gestione delle richieste di indisponibilità

La funzione è abilitata esclusivamente per gli utenti con profili P1, P3, P5, P6, P16, P7, P8, P17, P11, P15, secondo le autorizzazioni di sistema.

In EAM è possibile visualizzare lo stato delle richieste di indisponibilità nel centro di controllo. In particolare, quest’applicazione contiene una sezione in cui l’utente può visualizzare e accedere agli OdL per i quali è stata creata una richiesta di indisponibilità.

Il tab “richieste di indisponibilità” del centro di controllo riporta gli OdL distinti in base all’esito delle richieste di indisponibilità associate, nel modo seguente:

Gli OdL associati a richieste con esito positivo vengono evidenziati in verde.

Gli OdL associati a richieste ancora in stato “in lavorazione” / “Inviato” vengono evidenziati in blu.

Gli OdL associati a richieste con esito negativo vengono evidenziati in rosso.

Sono anche riportati gli OdL per i quali potrebbe essere necessaria una richiesta di indisponibilità (flag indisponibilità nel PdL a “Si”) e per i quali non è ancora stata creata la richiesta (stato “inviato”).

Per ciascun OdL sono indicate le informazioni seguenti:

ID OdL: Codice univoco dell’OdL per il quale è stato richiesto l’indisponibilità;

SAP ID: Codice identificativo dell’asset oggetto dell’attività;

Descrizione asset: Descrizione dell’asset di primo livello (linea o stazione) della gerarchia dell’asset oggetto dell’attività;

ASSETNUM: Codice univoco identificativo dell’asset in EAM;

ID PdL: Codice univoco dell’attività associata all’OdL, tramite la quale il sistema richiede l’indisponibilità ad Agile;

Data inizio pianificata: Data di inizio delle attività operative pianificata nell’OdL;

Data fine pianificata: Data di conclusione delle attività operative pianificata nell’OdL;

Data inizio indisponibilità: Data di inizio del periodo di indisponibilità. Per le richieste in stato “in lavorazione”, rappresenta la data di inizio indisponibilità provvisoria;

Data fine indisponibilità: Data di fine del periodo di indisponibilità. Per le richieste in stato “in lavorazione”, rappresenta la data di inizio indisponibilità provvisoria;

Stato della richiesta: Stato di EAM della richiesta di indisponibilità associata all’OdL;

Esito della richiesta: Esito della richiesta di indisponibilità, definita come precedentemente descritto nel capitolo.

Figura - Centro di controllo richieste di indisponibilità

Selezionando uno dei record presenti in questa sezione del CdC, l’utente è indirizzato all’applicazione di “Monitoraggio OdL”. In questo modo, l’utente ha la possibilità di ripianificare gli OdL che hanno avuto esito KO secondo le regole descritte nel paragrafo seguente.

### 5.11.3 Ripianificazione di OdL con esito KO

La funzione è abilitata esclusivamente per gli utenti con profili P1, P3, P5, P6, P16, P7, P8, P17, P11, P15, secondo le autorizzazioni di sistema.

L’esito dell’indisponibilità viene determinato confrontando le date di pianificazione dell’OdL con quelle di indisponibilità della richiesta deliberata in GIN. In caso di KO, l’utente ha la possibilità di eseguire alcune operazioni in EAM per allineare queste date con quelle dell’indisponibilità. Una volta effettuata la modifica sull’OdL, il sistema ricalcola l’esito basandosi sulla nuova pianificazione e aggiorna automaticamente le informazioni presenti nel centro di controllo. Se l’attività da modificare è già stata inviata a Geocall per la schedulazione, è necessario deschedularla (riportandola in stato Aperto) per poter modificare le date di pianificazione in EAM.

Modificare le date pianificate

Qualora si verifichi un’incompatibilità tra le date di inizio e fine pianificate e le date di inizio e fine di indisponibilità, comportando un esito negativo (KO) della richiesta, è possibile modificare le date di pianificazione in modo tale da renderle coerenti con quelle di indisponibilità. I passaggi sono i seguenti:

Aprire l’applicazione “Centro di controllo”.

Figura - Applicazione "Centro di controllo"

Selezionare il tab “Indisponibilità”: viene mostrata una tabella con le richieste di indisponibilità attualmente presenti a sistema. Per ogni richiesta sono indicate le seguenti informazioni: ID OdL, ID PdL, Data inizio pianificata, Data fine pianificata, Data Inizio Indisponibilità, Data Fine Indisponibilità, Stato indisponibilità, Esito richiesta indisponibilità, Asset, Classificazione Asset, SAPID Asset.

Figura - Tab "Monitoraggio indisponibilità"

Selezionare l’OdL che si intende modificare (associato ad un esito KO): l’utente viene indirizzato alla schermata dell’applicazione “Monitoraggio OdL”.

Figura - Selezione richiesta di indisponibilità

Selezionare il tab “Date”.

Figura - Tab "Date"

Modificare le date di fine e inizio pianificate.

Figura - Modifica date pianificate

Salvare le modifiche effettuate.

Figura - Funzione "Salva OdL"

Nota: se l’OdL è in stato “Schedulabile”, “Schedulato” (caso UI) o “Eseguibile” (caso non UI), deve essere utilizzata la funzionalità di ripianificazione, che riporta l’OdL in stato aperto.

Figura- Ripianificazione OdL

Successivamente all’inserimento della nuova data di fine pianificata, il sistema calcola automaticamente la nuova data di inizio pianificata in base alla data fine e la durata pianificata. Se le date modificate rispettano quelle di indisponibilità, EAM imposta automaticamente l’esito della richiesta associata all’OdL ad OK e le nuove date di inizio e fine pianificate vengono automaticamente allineate con GIN.

Modificare gli OdL figli

Nel caso in cui un OdL aggregatore riceva un esito negativo (KO) a causa dell’incompatibilità delle date, è possibile variarne la durata dei OdL figli. In questo modo, EAM consente all’utente di modificare la durata prevista per l’OdL aggregatore (calcolata come differenza fra la prima data pianificata e l’ultima data pianificata fra i figli) e di allocare la finestra temporale di indisponibilità deliberata all’interno di quella pianificata.

I passaggi sono i seguenti:

Aprire l’applicazione “Centro di controllo”.

Figura - Applicazione "Centro di controllo"

Selezionare il tab “Indisponibilità”: viene mostrata una tabella con le richieste di indisponibilità attualmente presenti a sistema. Per ogni richiesta sono indicate le seguenti informazioni: ID OdL, ID PdL, Data inizio pianificata, Data fine pianificata, Data Inizio Indisponibilità, Data Fine Indisponibilità, Stato indisponibilità, Esito richiesta indisponibilità, Asset, Classificazione Asset, SAPID Asset.

Figura - Tab "Monitoraggio indisponibilità"

Selezionare l’OdL che si intende modificare (associato ad un esito KO): l’utente viene indirizzato alla schermata dell’applicazione “Monitoraggio OdL”.

Figura – Sezione del centro di controllo di monitoraggio delle richieste di indisponibilità, con evidenza all’OdL da modificare

Sganciare l’OdL figlio: all’interno del tab “Pianificazione” dell’OdL aggregatore, l’utente deve selezionare l’icona di cestino per sganciare l’OdL figlio per il quale si desidera modificare le date di pianificazione. L’azione è necessario visto che il sistema non permette di modificare le date di pianificazione di OdL sottesi ad un aggregatore.

- Tab "Pianificazione" dell’OdL aggregatore, con evidenza all’icona di sgancio di un OdL figlio

Modificare le date di pianificazione e la durata dell’OdL figlio: per modificare le date di pianificazione e la durata dell’OdL appena sganciato, è necessario prima indirizzarsi alla schermata di dettaglio dell’OdL nell’applicazione “Monitoraggio OdL”. Nello specifico, è possibile inserire il codice dell’OdL direttamente nel campo di ricerca. Successivamente, nel tab “Date”, l’utente deve modificare le date di pianificazione e la durata e salvare le modifiche.

Figura – Vista iniziale dell’applicazione “Monitoraggio OdL”, con evidenza al codice dell’OdL da modificare inserito nel campo di filtro

Figura - Vista del tab "Date" con evidenza ai campi delle date di pianificazione e la durata

Relazionare l’OdL all’aggregatore: dopo modificare le date di pianificazione e la durata, è necessario, all’interno della maschera di dettaglio dell’OdL aggregatore nell’applicazione “Monitoraggio OdL”, relazionare l’OdL appena modificato selezionando l’azione “Relaziona ordini”.

Figura – Dialog di relazione di OdL figli all’aggregatore

Calcolare le date di pianificazione dell’aggregatore: dopo aver aggregato l’OdL, nel tab “Pianificazione” dell’OdL aggregatore, è necessario cliccare sul tasto “Calcola date OdL aggregatore” per aggiornare le date di pianificazione in base a quelle dei figli. Dopo l’operazione, è necessario salvare cliccando sull’icona di salvataggio in alto a destra.

Figura - Tab "Pianificazione" dell'OdL aggregatore, con evidenza al tasto di calcolo delle date dell'aggregatore e alla data fine pianificata modificata

Figura - Tab "Pianificazione" dell'OdL aggregatore, con evidenza alla nuova data di fine dell’OdL aggregatore e all’icona di salvataggio

Verificare la modifica: Se, a seguito delle modifiche effettuate, la finestra temporale definita dalle date inizio e fine indisponibilità rientra all’interno di quella definita dalle date di inizio e fine pianificate, EAM imposta l’esito della richiesta associata all’OdL ad OK e invia le nuove date di inizio e fine pianificate a GIN.

Figura - Sezione del centro di controllo di monitoraggio delle richieste di indisponibilità, con evidenza all’OdL aggregatore modificato

Cambiare PdL associato all’OdL

Nel caso in cui l’esito della delibera dell’indisponibilità sia negativo (KO), l’utente in EAM ha la possibilità di modificare il PdL associato all’OdL (lo stato dell’OdL dev’essere “Aperto”). Sostituendo il PdL con un altro che non richiede l’indisponibilità, è possibile proseguire con l’esecuzione dell’attività senza il vincolo della delibera.

I passaggi sono i seguenti:

Aprire l’applicazione “Centro di controllo”.

Figura - Applicazione "Centro di controllo"

Selezionare il tab “Indisponibilità”: viene mostrata una tabella con le richieste di indisponibilità attualmente presenti a sistema. Per ogni richiesta sono indicate le seguenti informazioni: ID OdL, ID PdL, Data inizio pianificata, Data fine pianificata, Data Inizio Indisponibilità, Data Fine Indisponibilità, Stato indisponibilità, Esito richiesta indisponibilità, Asset, Classificazione Asset, SAPID Asset.

Figura - Tab "Monitoraggio indisponibilità"

Selezionare l’OdL che si intende modificare (associato ad un esito KO): l’utente viene indirizzato alla schermata dell’applicazione “Monitoraggio OdL”.

Figura - Selezione richiesta di indisponibilità

Rimuovere il PdL esistente: avviare la funzione “Rimuovi PdL” dal menu laterale per svuotare il campo “Identificativo PdL”.

Figura - Funzione "Rimuovi PdL"

Associare il nuovo PdL: selezionare un PdL che ha il flag “Indisponibilità?” valorizzato con false. Possono essere scelti solo PdL che abbiano la stessa Macro-attività di quello da sostituire.

Figura - PdL senza indisponibilità

Nota: l’utente può scegliere solo PdL che abbiano la stessa Macro-attività dal PdL da sostituire.

Salvare le modifiche effettuate.

Figura - Funzione "Salva OdL"

Dopo aver salvato la modifica, i dati dell’OdL modificato vengono trasmessi automaticamente a GIN dove viene modificato lo stato della richiesta in “Annullato”, indicando il giustificativo “EAM” nel campo “Motivazione annullamento”. Infine, lo stato dell’indisponibilità su EAM viene settato a “Annullato”.

## 5.12 Gestione OdL aggregatori

L’OdL aggregatore ha lo stesso corredo informativo dell’OdL singolo, con la sola differenza che ha il flag “OdL aggregatore” attivato. La Tabella II riporta l’insieme degli attributi che servono a caratterizzare gli OdL, la loro obbligatorietà e modalità di valorizzazione. Questi attributi rappresentano elementi fondamentali per una corretta gestione, classificazione e tracciabilità degli OdL all’interno del sistema.

| CAMPO | DESCRIZIONE | OBBLIGATORIO O FACOLTATIVO | MODALITÀ DI VALORIZZAZIONE | LOGICA DI VALORIZZAZIONE |
| --- | --- | --- | --- | --- |
| Identificativo PdL | Codice univoco identificativo del PdL | Obbligatorio | Manuale | Selezione del PdL tramite dialog dedicata. |
| Tipologia PdL | Indica la tipologia di PdL associato all’OdL | Obbligatorio | Automatica | Definita nel catalogo delle attività e dedotta dal PdL selezionato. |
| Assieme e sottoassieme attività | Assieme e sottoassieme valorizzati se presenti nel PdL selezionato | Facoltativo | Automatica | Definita nel catalogo delle attività e dedotta dal PdL selezionato. |
| Programma | Cluster di attività su alcuni asset specifici | Facoltativo | Automatica | Informazione presente nel catalogo attività derivata dal PdL associato all’Odl. |
| Sotto-programma | Cluster di caratterizzazione ulteriore su alcuni asset specifici, per i quali il programma non è sufficiente | Facoltativo | Manuale | Informazione inserita manualmente dall’utente selezionando dalla lista di sotto-programmi censiti a sistema. |
| Descrizione PdL | Descrizione del PdL associato all’OdL | Obbligatorio | Automatica | Definita nel catalogo delle attività e derivata dall’ID PdL inserito. |
| ID Macro-attività | Codice identificativo della macro-attività associata all’OdL | Obbligatorio | Automatica | Definita nel catalogo delle attività e dedotta dal PdL selezionato. |
| Responsabile attività | Campo identificativo dell’owner della pianificazione dell’OdL. | Obbligatorio | Automatica | Campo modificabile dall’utente, valorizzato in base all’asset associato all’OdL e al PdL selezionato, nel caso in cui non fosse specificato nel PdL viene valorizzato con lo stesso valore del resp. Asset. |
| ID OdL | Codice identificativo dell’OdL manuale generato | Obbligatorio | Automatico | Codice generato automaticamente dal sistema al momento della creazione del nuovo OdL. |
| Descrizione OdL | Descrizione dell’OdL che si vuole creare | Obbligatorio | Automatico | Ereditato dalla descrizione del PdL Il campo è modificabile dall’utente qualora necessario. |
| ID Asset | Codice identificativo dell’Asset oggetto dell’OdL | Obbligatorio | Manuale | Selezione manuale tramite dialog dedicata. |
| Descrizione Asset | Descrizione dell’asset oggetto dell’OdL | Obbligatorio | Automatica | Determinato automaticamente in base all’asset selezionato |
| SAP ID Asset | Codice SAP dell’asset oggetto dell’OdL | Facoltativo | Automatica | Attributo anagrafico associato all’asset. Qualora all’asset non sia associato un SAP ID, Il campo viene popolato con il SAP ID del primo asset della gerarchia. |
| Gerarchia Asset | Gerarchia dell’asset oggetto dell’OdL | Obbligatorio | Automatica | Derivato dalla struttura gerarchica anagrafica dell’asset. |
| Stato | Stato dell’OdL. | Obbligatorio | Automatica | Valorizzato automaticamente con “Aperto” al momento della generazione dell’OdL |
| Responsabile Asset | Responsabile censito nelle sedi tecniche in anagrafica EAM | Obbligatorio | Automatica | Recuperato dall’anagrafica in base all’asset selezionato |
| Codice CoIn | Codice identificativo della contabilità industriale | Obbligatorio | Automatica | Valorizzato automaticamente sulla base del catalogo standard, tipo spesa e PdL selezionato |
| Tipo spesa | Campo che indica la tipologia di attività CoIn associata all’OdL. Può assumere i valori “Opex”; “Capex” o “ANR” | Obbligatorio | Automatico | Valorizzato automaticamente con il Tipo spesa prevalente |
| Attività standard | ID attività standard | Obbligatorio | Automatica | Valorizzato in base al PdL associato all’OdL |
| Titris | Campo indicativo del titolare della risorsa che esegue l’attività | Obbligatorio | Automatico | Corrispondente al responsabile attività |
| Durata pianificata | Durata pianificata per l’esecuzione dell’OdL | Obbligatorio | Automatico | Corrispondente alla massima durata tra quelle associate ad ogni singola risorsa. La durata pianificata viene ereditata dal catalogo delle attività in base al PdL associato all’OdL. Nel caso non sia specificata la durata all’interno del catalogo il campo viene lasciato vuoto alla creazione dell’ordine e deve essere successivamente valorizzato manualmente dall’utente. |
| Ore esecuzione | Periodo (in ore) di esecuzione dell’attività, per risorsa. | Obbligatorio(nel caso in cui ci siano dei record) | Automatico | Valore reperito dal catalogo delle attività Tuttavia, qualora sia necessario aggiungere manualmente delle risorse, il campo non deve essere valorizzato automaticamente. |
| Ore trasferimento | Periodo (in ore) necessario per il raggiungimento e il rientro dal luogo di svolgimento dell’OdL | Facoltativo | Automatico | Valore determinato in funzione del Titris e degli’asset associati all’OdL. |
| Data pianificata inizio / fine | Date di inizio e fine previste per l’esecuzione dell’attività | Obbligatorio | Automatico | Inserimento manuale nel campo dedicato del tab “Date” nel caso in cui non sia inserita una data modello. Se l’utente definisce una data modello (funzionalità esclusiva degli utenti centrali), il sistema popola automaticamente i campi nel seguente modo: Data pianificata fine = Data modello. La data di inizio deve invece essere popolata manualmente. Per gli OdL aggregatori vengono popolate tramite la funzione “Calcola date OdL aggregatore” |
| Data modello | Data modello | Facoltativo | Manuale | Inserimento manuale nel campo dedicato del tab “Date”. L’operazione sarà consentita soltanto dagli utenti centrali |
| Perimetro intervento | Attributo dell’OdL che può assumere i valori “Sostituzione”, “Nuove installazione”, “Manutenzione”, “Monitoraggio” e “Altro” | Obbligatorio | Automatica | Attributo valorizzato in base alla tipologia di attività |
| Tipo OdL | Campo identificativo della tipologia di OdL | Obbligatorio | Automatica | Valorizzato automaticamente con “Manuale” al momento della creazione dell’OdL |
| OdL serbatoio | Flag che definisce se l’OdL si tratta di un serbatoio o meno. Preimpostato come “NO” | Obbligatorio | Automatica | Flag preimpostato in “NO”; gestito dal sistema |
| Data inizio e fine indisponibilità | Data che definisce l’inizio e fine del periodo di indisponibilità dell’asset oggetto dell’Odl | Facoltativo | Automatica | Informazione popolata a valle della creazione della richiesta di indisponibilità in Agile, qualora sia necessaria per il PdL associato all’OdL. |
| Indisponibilità necessaria? | Flag che indica se il PdL associato all’OdL richiede indisponibilità | Obbligatorio | Automatica | Informazione ereditata dal PdL e presente all’interno del catalogo delle Attività |
| Note | Campo di inserimento di note | Facoltativo | Manuale | Inserimento manuale libero nel campo dedicato del tab “Ordini di lavoro” |
| Assistenza / Sorveglianza | Campo che indica se l’OdL si caratterizza come di assistenza o sorveglianza. Serve a racchiudere le attività per gestire le casistiche di opportunità di cantiere. Il campo può assumere i valori “Assistenza”, “Sorveglianza”. | Obbligatorio | Manuale | Inserimento nel campo dedicato tramite selezione da un elenco di valori ammissibili (“Assistenza”, “Sorveglianza”). Il campo è reso visibile solo in caso Outsourcing? = SI |
| Outsourcing? | Campo che indica se l’attività è in insourcing o outsourcing | Obbligatorio | Automatica | Campo derivato dal PdL. Se valorizzato con “SI” viene reso visibile il campo “Assistenza/Sorveglianza” |
| OdL Aggregatore | Flag che indica se l’OdL è di natura aggregatore. Preimpostato come “NO” | Obbligatorio | Automatica | Flag preimpostato in “NO” e manualmente modificabile. |
| Data massimo posticipo | Data massima entro la quale l’OdL può essere posticipata | Obbligatorio | Manuale | Inserita nel campo dedicato del tab “Date” |
| Data non prima di | Data limite entro la quale si può anticipare l’attività | Obbligatorio | Automatico | Valorizzato automaticamente dal sistema uguale alla data di creazione dell’OdL manuale |
| OdL per piano (PMI) | Flag che indica l’appartenenza dell’OdL al PMI | Obbligatorio | Automatico | Valorizzato di default a “NO” dal sistema, modificabile liberamente dall’utente in ogni stato dell’OdL |

Tabella - Campi facoltativi e obbligatori per gli OdL

## 5.13 Creazione manuale OdL aggregatore

La funzione è abilitata esclusivamente per gli utenti con profili P1, P3, P5, P6, P16, P7, P8, P17, P11, P15, secondo le autorizzazioni di sistema. Per creare un OdL aggregatore, l’utente deve seguire i passaggi descritti di seguito.

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Applicazione "Monitoraggio OdL"

Cliccare su “Nuovo OdL”: dopo aver aperto l’applicazione, è possibile cliccare sull’azione “Nuovo OdL” o sul pulsante “+” evidenziato nella schermata per accedere alla maschera di compilazione dell’OdL.

Figura - Funzione "Nuovo OdL"

Attivare il flag “OdL aggregatore?”: questo flag, presente sul tab “Ordine di lavoro”, permette di selezionare come asset un primo o secondo livello linea o stazione.

Figura - Flag "OdL aggregatore?"

Compilare i campi obbligatori: per poter salvare l’OdL aggregatore, è necessario compilare manualmente, in un primo momento, i campi “Identificativo PdL” e “Asset”.

Figura - Tab "Ordine di lavoro"

Nota: quando l’utente procede alla valorizzazione del campo “Identificativo PdL”, il sistema consente di selezionare esclusivamente dei PdL per il quale il flag “Attività caricabile manualmente” è attivo nel catalogo delle attività. Dopo aver selezionato il codice PdL da associare all’OdL, l’asset sul quale verrà eseguito l’OdL va definito a partire da una lista fornita dal sistema, contenente gli asset di primo livello o secondo livello ammissibili per il PdL selezionato.

Figura - PdL caricabili manualmente

Figura- Asset ammissibili

Nota: tra gli asset ammissibili saranno visualizzati solo asset di primo o secondo livello coerenti con l’elemento su cui insiste il pdl inserito.

Salvare l’OdL: dopo aver compilato i campi obbligatori presente in maschera e descritti nella Tabella II, l’utente deve procedere al salvataggio del nuovo OdL. Il salvataggio dell’OdL viene effettuato selezionando l’azione “Salva OdL”, disponibile nel menu laterale sinistro della vista o tramite l’icona di salvataggio presente in alto a destra.

Figura - Funzione "Salva OdL"

5.14 Aggregazione degli OdL e pianificazione dell’aggregatore

La funzione è abilitata esclusivamente per gli utenti con profili P1, P3, P5, P6, P16, P7, P8, P17, P11, P15, secondo le autorizzazioni di sistema.

Relazionare gli OdL figli: la creazione di un OdL aggregatore prevede che l’utente associ gli OdL figli al padre appena generato. A tal fine è disponibile la funzionalità “Relaziona ordini”, presente nel menu laterale a sinistra. Una volta avviata, l’utente può scegliere quali OdL verranno aggregati da una lista contenente gli OdL con stesso PdL ed incidenti su asset che hanno lo stesso asset di primo livello dell’OdL aggregatore. Alla conferma, gli OdL figli appaiono in una tabella presente nel tab “Pianificazione”.

Figura - Funzione "Relaziona ordini"

Figura - OdL relazionabili

Figura - OdL figli

Nota: Se il campo COIN degli OdL aggregati non corrisponde a quello dell’OdL aggregatore, al salvataggio appare un warning per avvisare l’utente che sarà modificata la COIN degli aggregati al fine di allinearla con quella dell’OdL aggregatore. Lo stesso vale per l’attributo assistenza/sorveglianza.

Figura - Warning aggregazione

Effettuare il calcolo delle date: per popolare date ed effort dell’OdL aggregatore occorre avviare la funzione “Calcola date OdL aggregatore”, presente nel tab “Pianificazione”. Il sistema effettua un calcolo per ottimizzare i tempi di esecuzione dell’OdL aggregatore sulla base dei vincoli degli OdL sottostanti. In particolare, la data fine pianificata dell’aggregatore sarà pari alla maggiore data di fine pianificata dei figli, la data di inizio pianificata corrisponderà alla minore data di inizio pianificata dei figli, la data di massimo posticipo verrà posta uguale alla più stringente tra quelle dei figli e la data non prima di verrà ereditata dalla maggiore tra quelle dei figli. Inoltre, per ogni OdL aggregato è possibile specificare un OdL propedeutico (uno degli altri OdL aggregati) attraverso il campo “Predecessori”. In questo modo la data di inizio dell’OdL per cui si è specificato il predecessore sarà posizionata successivamente alla data di fine del predecessore indicato. L’effort dell’OdL aggregatore è calcolato come somma degli OdL sottostanti.

Figura - Funzione "Calcola date OdL aggregatore"

Figura - Campo "Predecessori"

Figura - Date OdL aggregatore

5.15 Modifica di OdL aggregatore

La funzione è abilitata esclusivamente per gli utenti con profili P1, P3, P5, P6, P16, P7, P8, P17, P11, P15, secondo le autorizzazioni di sistema.

Una volta generato l’OdL aggregatore, il motore di pianificazione euristico (MPE) non considera più le date dei singoli OdL sottostanti, ma utilizza le informazioni temporali dell’OdL aggregatore appena creato. In questo modo, qualsiasi spostamento delle date pianificate dell’OdL aggregatore comporta uno spostamento in blocco degli OdL sottostanti, in modo tale da rispettare le logiche definite dall’algoritmo di creazione dell’aggregatore.

EAM consente all’utente le seguenti azioni manuali di modifica dell’OdL aggregatore, fintantoché quest’ultimo si trova in stato “Aperto”:

Modifica delle date di pianificazione

Aggiungere OdL

Rimuovere OdL

Modificare le date di pianificazione

L’OdL aggregatore può essere spostato temporalmente attraverso una modifica manuale della data di fine pianificata, purché sia rispettata la finestra di flessibilità definita inizialmente dagli OdL figli. Questi ultimi vengono spostati mantenendo la loro composizione iniziale. I passaggi sono i seguenti:

Accedere all’applicazione “Monitoraggio OdL”.

Figura - Applicazione "Monitoraggio OdL"

Inserire l’ID dell’OdL aggregatore da modificare.

Figura - Selezione OdL

Selezionare il tab “Date”.

Figura - Tab "Date"

Inserire la nuova data di fine pianificata nell’omonimo campo.

Figura - Campo "Data pianificata fine"

Salvare le modifiche effettuate: il sistema ricalcola la nuova data di inizio pianificata in base alla durata dell’OdL aggregatore.

Figura - Campo "Data pianificata inizio"

Nota: la modifica della data di fine pianificata è eseguibile solamente sull’aggregatore e non sugli OdL sottostanti. D’altra parte, durata ed effort vanno modificate sugli OdL sottostanti. I dati dell’aggregatore verranno aggiornati automaticamente.

Aggiungere OdL

I passaggi sono i seguenti:

Accedere all’applicazione “Monitoraggio OdL”.

Figura - Applicazione "Monitoraggio OdL"

Selezionare l’OdL aggregatore al quale si desidera aggiungere un ulteriore OdL figlio.

Figura - Selezione OdL

Avviare la funzione “Relaziona ordini”, presente nel menu a sinistra.

Figura – Funzione “Relaziona ordini”

Scegliere gli OdL da aggiungere.

Figura - Selezione OdL figlio

Verificare che il nuovo OdL figlio sia presente nel tab “Pianificazione”.

Figura - Nuovo OdL figlio

Avviare la funzione “Calcola date OdL aggregatore” per attivare le logiche di ottimizzazione delle date.

Figura - Funzione "Calcola date OdL aggregatore”

Rimuovere OdL

I passaggi sono i seguenti:

Accedere all’applicazione “Monitoraggio OdL”.

Figura - Applicazione "Monitoraggio OdL"

Selezionare l’OdL aggregatore al quale si desidera rimuovere un OdL figlio.

Figura- Selezione OdL

Selezionare il tab “Pianificazione”.

Figura - Tab "Pianificazione"

Selezionare l’icona del cestino presente sulla riga dell’OdL che si intende eliminare.

Figura - Funzione "Elimina OdL figlio"

Confermare l’operazione cliccando “sì” nel pop up che compare a schermo.

Figura - Pop up eliminazione OdL figlio

Verificare che gli OdL figli eliminati siano spariti dalla tabella e vengono automaticamente impostati in stato Aperto.

Figura - OdL figli superstiti

Avviare la funzione “Calcola date OdL aggregatore” per attivare le logiche di ottimizzazione delle date.

Figura - Funzione "Calcola date OdL aggregatore"

Nota: per poter rimuovere un OdL figlio da un ordine aggregatore che è già passato in stato schedulabile, schedulato, o eseguibile deve essere prima richiesta la ripianificazione tramite l’apposita funzionalità (alla ripianificazione l’utente viene avvisato che l’ordine sarà riportato in stato “Aperto”), per riportare l’ordine in stato “aperto”.

Figura - Funzione "Ripianificazione OdL"

5.16 Generazione OdL di pronto intervento

Gli OdL di pronto intervento sono strumenti previsti per la gestione di attività straordinarie non pianificate, da eseguire in risposta a situazioni impreviste rilevate sul campo. A differenza degli OdL generati manualmente o automaticamente dal sistema tramite il DSS e inclusi nei processi di pianificazione standard, gli OdL di pronto intervento vengono creati dall’utente e consuntivati successivamente all’esecuzione dell’attività.

Questi OdL consentono di tracciare interventi urgenti direttamente dal campo, tramite l’inserimento di una Richiesta di Servizio da mobile, e di gestirne successivamente la formalizzazione via Web.

Per realizzare il processo di generazione OdL di pronto intervento, l’utente deve seguire i passaggi descritti di seguito.

Creare la Richiesta di servizio nella piattaforma EAM Mobile: la funzione è abilitata esclusivamente per gli utenti con profilo P1, P6, P7, P8, P9, P17 secondo le autorizzazioni di sistema. Posteriormente o contestualmente all’esecuzione dell’attività di pronto intervento, l’utente deve generare la richiesta di servizio relativa all’attività realizzata in campo tramite applicazione dedicata della piattaforma Mobile. Nello specifico, è necessario eseguire i seguenti passaggi:

Accedere alla sezione dedicata in EAM Mobile: dopo aver fatto login all’ambiente mobile di EAM, l’utente deve entrare nell’applicazione di creazione delle Richieste di servizio (RdS). L’operatore deve prima selezionare l’opzione evidenziata in basso a destra per visualizzare il menu contenente le applicazioni mobile. Successivamente, clicca sull’opzione “Richiesta di servizio” .

Figura - Vista elenco delle applicazioni di EAM Mobile, con evidenza al tasto di apertura dell'elenco e all'applicazione "Richieste di servizio"

Creare la Richiesta di Servizio da mobile: una volta all’interno dell’applicazione descritta nel punto precedente, sono visibili tutte le richieste di servizio mai create in precedenza. Per procedere alla generazione di una nuova richiesta, l’utente deve selezionare il tasto “+”. Nella schermata successiva è possibile effettuare le seguenti azioni:

Figura - Vista inziale dell'applicazione "Richiesta di servizio", con evidenza al tasto di creazione di una nuova richiesta

Impostare il livello di priorità dell’attività: selezionando il tasto “Alta priorità”, è possibile definire la richiesta di servizio come essendo di priorità alta.

Definire i dettagli della richiesta: selezionando l’opzione “Dettagli” l’utente viene indirizzato alla schermata dove potrà attribuire alla richiesta di servizio le seguenti caratteristiche:

Classificazione: classificazione dell’asset a cui va attribuita la richiesta di servizio;

Tipologia difformità: codice dell’anomalia rilevata, selezionata da una lista valori pre-filtrata in base alla classificazione;

Note: campo di libera valorizzazione. Nel campo deve essere inserito il riferimento all’asset specifico sul quale l’operatore ha eseguito l’intervento.

Definire la persona di contatto: quando viene generata la nuova richiesta, è possibile definire la persona di riferimento che la gestisce. Di default, la persona di riferimento è la stessa che ha effettuato il login.

Inserire un allegato: selezionando l’icona sotto “Allegato” è possibile allegare un documento o foto alla richiesta di servizio.

Salvare la richiesta di servizio: selezionando il tasto blu, il sistema conferma la creazione della richiesta di servizio in stato “Aperta” e la rende disponibile alle ulteriori fasi del processo.

Figura - Schermata della richiesta di servizio in fase di creazione, con evidenza al tasto di salvataggio della nuova richiesta

Figura - Schermata di dettaglio della richiesta di servizio in fase di generazione

Nota: dopo la compilazione dei campi e salvataggio tramite il tasto dedicato, la richiesta di servizio diventa visibile nell’elenco di richieste generate della piattaforma Mobile e viene sincronizzata automaticamente con la piattaforma Web, diventando visibile all’interno dell’applicazione “Richieste di servizio” .

Figura - Vista inziale dell'applicazione "Richiesta di servizio", con evidenza alla nuova richiesta

Generare l’OdL di pronto intervento su Web: la funzione è abilitata esclusivamente per gli utenti con profilo P1, P5, P6, P7, P8, P11, P15, P16 e P17, secondo le autorizzazioni di sistema. La richiesta creata da mobile viene visualizzata su EAM Web nella sezione dedicata in stato “Aperto”. Prima di procedere alla creazione dell’OdL di pronto intervento è necessario verificare l’asset e l’anomalia associate alla richiesta, in modo tale da poterli attribuire all’OdL. Per generare l’OdL di pronto intervento, l’utente deve procedere nel seguente modo:

Accedere all’app Monitoraggio OdL: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Cliccare su “Nuovo OdL”: una volta aperta l’applicazione, è possibile cliccare sull’azione “Nuovo OdL” o sul pulsante “+” evidenziato nella schermata per accedere alla maschera di compilazione dell’OdL.

Figura - Vista elenco iniziale dell'app "Monitoraggio OdL", con evidenza alle azioni di creazione manuale di OdL

Popolare i campi “Identificativo PdL” e “Asset”: il PdL selezionato dovrà essere di tipologia “Pronto intervento” all’interno del catalogo delle attività e l’asset associato deve essere lo stesso riportato nel campo “Note” della richiesta di servizio.

Figura - Vista di dettaglio dell'OdL all'interno dell'applicazione "Monitoraggio OdL", con evidenza ai campi di definizione del PdL e dell'asset.

Associare e risolvere l’anomalia rilevata: deve essere registrata sull’OdL di pronto intervento la stessa anomalia riportata nella richiesta di servizio, e, qualora sia stata risolta in occasione dell’esecuzione dell’attività, è necessario impostare lo stato dell’anomalia in “Risolta”. Per eseguire le azioni appena descritte, l’operatore deve realizzare i seguenti passaggi:

Accedere al tab “Difformità e interferenze”: all’interno dell’applicazione “Monitoraggio OdL”, l’utente deve selezionare il tab “Difformità e Interferenze” per accedere all’ambiente di registrazione delle anomalie.

Registrare l’anomalia: all’interno del tab “Difformità e Interferenze”, l’utente deve cliccare sul tasto “+” per registrare l’anomalia. All’interno della rispettiva tabella, l’operatore deve fornire le seguenti informazioni inerenti all’anomalia che sta registrando:

Asset: campo dedicato all’inserimento dell’asset su cui la difformità/interferenza è stata rilevata. L’asset associato all’anomalia è lo stesso riportato nel campo “Note” della richiesta di servizio.

ID Difformità / interferenza: codice ID catalogo della difformità/interferenza da associare all’asset. Deve essere inserito lo stesso ID Difformità / interferenza presente nella RdS.

Gravità: livello di gravità della difformità/interferenza. Campo obbligatorio se per la difformità/interferenza nel rispettivo catalogo sono definite delle gravità.

ID Misura: campo non obbligatorio. Tramite picklist vengono restituiti i codici misura associati alla difformità/interferenza nel rispettivo catalogo.

Valora misura: eventuale valore della misura associata alla difformità/interferenza. Obbligatorio se popolato il campo “ID misura”

Fase: fase a cui si riferisce la difformità/interferenza (se necessario indicarla).

Risolta contestualmente: il flag deve essere selezionato qualora l’anomalia sia stata risolta in occasione dell’esecuzione dell’attività. In questo modo, l’anomalia sarà automaticamente validata al momento della registrazione e avrà lo stato “Risolto”.

Note: campo facoltativo

Figura - Vista di dettaglio del tab “Difformità e interferenze” all'interno dell'applicazione "Monitoraggio OdL", con evidenza al tasto di aggiunta di nuova difformità o interferenza.

Nota: per poter registrare sull'OdL l'anomalia indicata nella RdS, l'OdL deve trovarsi almeno in stato «Eseguito parzialmente» (caso UI e misto) e in stato «Eseguibile» (caso non UI).

Registrare ed inviare a SAP le ore per l’esecuzione dell’attività: dopo la creazione dell’OdL e la registrazione delle anomalie, è possibile effettuare la registrazione delle ore lavorate. L’operazione avviene in EAM per i Titris non UI e in Geocall per i Titris UI / Misto e può essere realizzata solo se l’OdL si trova in uno stato idoneo. Successivamente, è necessario effettuare l’invio delle ore consuntivate a SAP. Il processo di consuntivazione ed invio delle ore a SAP (processo non UI) è descritto nei seguenti passaggi.

Selezionare il tab “Avanzamento”: accedere al tab “Avanzamento” per poter registrare dei consuntivi o verificare le ore precedentemente registrate.

Registrare il nuovo consuntivo: selezionando il tasto “+”è possibile consuntivare delle ore di lavoro. Nella tabella “Risorse”, l’utente deve compilare manualmente i campi di seguito descritti, mentre gli altri sono valorizzati automaticamente dal sistema. Dopo la compilazione, è necessario effettuare il salvataggio tramite le azioni evidenziate.

Risorsa

Data di inizio

Ora di inizio

Data di fine

Ora di fine

Figura - Vista di dettaglio del tab “Avanzamento” all'interno dell'applicazione "Monitoraggio OdL", con evidenza al tasto di aggiunta di nuova risorsa, ai campi da valorizzare e alle funzioni di salvataggio

Accedere all’app “Consuntivazione risorse”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Consuntivazione risorse”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Consuntivazione risorse”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Consuntivazione risorse"

Selezionare la risorsa di interesse: all’interno della vista elenco dell’applicazione “Consuntivazione risorse”, l’utente può filtrare la ricerca della risorsa utilizzando i campi presenti nella sezione superiore della tabella. Selezionando la checkbox accanto le risorse, l’utente indica al sistema in modo puntuale o massivo quale ore consuntivate devono essere inviate a SAP.

Figura - Vista elenco dell'applicazione "Consuntivazione risorse", con evidenza ai checkbox di selezione delle ore consuntivate da inviare verso SAP

Nota: È necessario compilare il campo "Identificativo OdM / NTK" nel tab "CoIn" dell’OdL per effettuare l’invio del consuntivo a SAP.

Effettuare l’invio: selezionando l’azione laterale “Invia a SAP”, il sistema invia a SAP le ore selezionate nello step precedente.

Figura - Vista elenco dell'applicazione "Consuntivazione risorse", con evidenza all’azione di invio delle ore consuntivate verso SAP

Chiudere l’attività di pronto intervento: la chiusura degli OdL in EAM (modifica stato da “Eseguito” a “Chiuso”) avviene in modo automatico una volta raggiunte le condizioni necessarie:

Caso 100% UI: se l’ordine è già in stato “Eseguito” quando arriva da Geocall l’aggiornamento dello stato in “Chiuso”; se è già arrivato l’aggiornamento da Geocall che l’ordine è in stato “Chiuso” quando l’utente su EAM imposta lo stato “Eseguito”.

Caso misto: l’ordine è in stato “Eseguito”; sono stati inviati a SAP tutti i consuntivi orari; è stato ricevuto il cambio stato in “Chiuso” da Geocall.

Caso non UI: sono stati inviati a SAP tutti i consuntivi orari e l’ordine è in stato “Eseguito”.

Chiudere la Richiesta di Servizio: infine, l’utente chiude manualmente la RdS modificandone lo stato in “Chiusa”. È possibile visualizzare lo storico delle Richieste di Servizio chiuse nella sezione dedicata. Per effettuare la chiusura, l’operatore deve seguire i seguenti step:

Accedere all’applicazione “Richiesta di servizio”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Richieste di servizio”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Richieste di servizio”.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Richieste di servizio"

Selezionare la RdS di interesse: utilizzando il filtro di ricerca presente in alto a destra nella schermata, l’utente può individuare la RdS che desidera chiudere. Una volta trovata, cliccare sul record per accedere alla maschera di dettaglio.

Figura - Vista iniziale dell'applicazione "Richieste di servizio" in EAM Web, con evidenza alla richiesta creata in EAM Mobile

Chiudere la richiesta di servizio: all’interno della schermata di dettaglio della richiesta di servizio, l’utente deve selezionare l’azione laterale “Modifica stato”. Nella dialog di modifica stato, è possibile impostare lo stato della richiesta cliccando sulla freccia e successivamente selezionare il tasto “OK” per confermare l’operazione.

Figura - Dialog di modifica stato della richiesta di servizio

Nota: prima di inviare dei consuntivi verso SAP è importante assicurarsi che i campi “Identificativo NTK” o “Identificativo OdM” siano valorizzati. In caso contrario, EAM non effettua l’invio delle ore e segnala l’errore tramite il valore “KO” sulla colonna “Check SAP”.

Figura - Vista di dettaglio del tab “Avanzamento” all'interno dell'applicazione "Monitoraggio OdL", con evidenza al messaggio di invio verso SAP non andato a buon fine

# 6. MOTORE DI PIANIFICAZIONE EURISTICO

## Introduzione

Gli OdL rappresentano le entità operative che raccolgono tutte le informazioni necessarie per pianificare, eseguire, tracciare e consuntivare gli interventi sugli asset di rete. La gestione degli OdL si articola in più fasi: generazione, pianificazione tecnica ed economica, consuntivazione e chiusura.

La pianificazione tecnica degli Ordini di Lavoro rappresenta la fase in cui vengono definite tutte le informazioni necessarie per l’esecuzione operativa delle attività. La pianificazione tecnica consente di strutturare correttamente l’OdL in funzione delle caratteristiche dell’intervento da effettuare, garantendo un’esecuzione coerente, tracciabile ed efficiente all’interno del ciclo di gestione delle attività manutentive. In EAM è possibile effettuare la pianificazione tecnica degli OdL tramite la funzionalità dedicata “Motore di pianificazione euristico (MPE)”. Tale funzionalità consente di effettuare una pianificazione temporale di massima degli ordini all’interno di un determinato intervallo temporale definito dall’utente, coerentemente con i vincoli di esecuzione delle singole attività e di capacity disponibile.

## Configurazione parametri di esecuzione MPE

In EAM, il “Motore di pianificazione euristico (MPE)” consente di effettuare una pianificazione temporale di massima degli ordini all’interno di un determinato intervallo temporale definito dall’utente, coerentemente con i vincoli di esecuzione delle singole attività e della capacity disponibile.

### 6.2.1 Definizione perimetro OdL ottimizzabili

In EAM è definito un perimetro di applicabilità del Motore di pianificazione euristico (MPE). Nel processo di pianificazione temporale, MPE considera solamente OdL on condition o periodici generati dal Decision Support System (DSS) o OdL creati manualmente dall’utente. È necessario che gli ordini di lavoro siano in stato “APERTO” e che il periodo temporale di pianificazione, impostato sull’OdL e definito come la differenza tra la data di massimo posticipo e la data non prima di, sia maggiore di un mese.

Sono inoltre esclusi dal perimetro di applicabilità le seguenti tipologie di OdL:

OdL serbatoio;

OdL per cui lo stato della richiesta di indisponibilità è in stato “In Lavorazione” o successivo, poiché le date di indisponibilità deliberate impongono sull’attività un vincolo temporale da rispettare;

OdL esclusi dalla selezione effettuata dall’utente al momento della definizione del run;

OdL aventi PdL di tipologia Pronto intervento;

OdL a calendario predefinito;

OdL in outsourcing con frequenza fissa;

OdL aventi come attività il taglio piante.

Perché il sistema possa determinare univocamente gli ordini da escludere è gestita una tabella di configurazione in cui i soli utenti di profilo P1 possono inserire le tipologie di PdL da escludere. Per configurare la presente tabella l’utente è tenuto a seguire i passaggi di seguito descritti:

Accedere all’applicazione “Domini”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Configurazione del sistema”, poi “Configurazione piattaforma” e successivamente “Domini”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Domini”.

Figura: Lista delle applicazioni in EAM, con evidenza dell’applicazione “Domini”

Ricercare il dominio “X4PDLESCLUSI”: nel campo di ricerca della colonna “Dominio”, l’utente è tenuto a filtrare per “X4PDLESCLUSI”.

Figura - vista elenco dell'applicazione "Domini" con evidenza della ricerca " X4PDLESCLUSI”

Cliccare sull’icona

Figura: vista generale X4PDLESCLUSI con evidenza dell'icona da cliccare

Aggiungere un PdL nella lista dei PdL esclusi: cliccando il tasto +, l’utente ha la possibilità di aggiungere un PdL alla lista di quelli esclusi dalle elaborazioni dell’MPE compilando il campo obbligatorio “Valore” ed eventualmente anche i campi facoltativi

Cliccare “OK”

### 6.2.2 Configurazione parametri di Input

Nel processo di pianificazione temporale, MPE considera diversi fattori:

Vincoli temporali: MPE pianifica gli OdL considerando le informazioni di pianificazione temporale precedentemente definite all’interno dei singoli OdL, ovvero data modello, date di inizio e fine pianificata, durata pianificata, data di massimo posticipo attività, data non prima di e l’effort definito per ciascun inquadramento. Nello specifico MPE non potrà posizionare gli ordini oltre la loro “Data di massimo posticipo” o prima della loro “Data non prima di”. Inoltre, affinché MPE prenda in considerazione l’attività, il periodo di pianificazione di quest’ultima, definito come la differenza tra la “Data di massimo posticipo” e la “Data non prima di”, deve essere superiore all’intervallo predefinito di 30 giorni. Inoltre, deve collocare gli OdL all’interno dell’orizzonte temporale definito in fase di configurazione dei parametri di input da parte dell’utente stesso evitando di posizionare gli OdL durante i weekend e nei giorni di festa nazionale.

Vincoli di capacity: MPE deve rispettare la capacity disponibile per UdB mese per mese e la capacity deve essere saturata su base giornaliera. La capacity mensile disponibile per la pianificazione mediante MPE viene quindi determinata come la differenza tra la forza lavoro teorica per mese, la capacity totale del serbatoio mensile, la capacity assorbita dagli OdL pianificati (quelli con stato successivo ad aperto ma non ancora chiusi) e la capacity degli OdL esclusi dal perimetro del run dell’MPE. Si specifica che la capacity è calcolata per tutte le figure professionali.

Priorità: a catalogo ogni attività è distinta in base alla priorità tramite la valorizzazione dell’attributo “Priorità”. Se la priorità è alta tale attributo è valorizzato con il valore 1, se la priorità è media con il valore 2, se la priorità è bassa con il valore 3.

Figura - Vista di un PdL a catalogo con evidenza del campo "Priorità"

Gli utenti di profilo P1, secondo le autorizzazioni di sistema, configurano le percentuali minime di capacity da dedicare mensilmente agli ordini di priorità inferiore a 1 tramite apposita tabella. Per accedere, configurare o consultare la presente tabella l’utente è tenuto a seguire i passaggi di seguito descritti:

Accedere all’applicazione “Motore di pianificazione euristico”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Motore di pianificazione euristico”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Motore di pianificazione euristico”.

Figura: Lista delle applicazioni in EAM, con evidenza dell’applicazione “Motore di pianificazione euristico”

Dalla vista elenco cliccare la funzionalità “Definizione percentuali minime capacity per priorità

Figura Vista elenco applicazione "Motore di pianificazione euristico" con evidenza della funzionalità “Definizione percentuali minime capacity per priorità”

Valorizzare le percentuali minime di capacity per livello di priorità: l’utente è tenuto a definire le percentuali minime valorizzando la colonna “Valore”

Figura: Evidenza dei campi da compilare per la definizione delle percentuali minime di capacity

Cliccare “OK”.

Figura: Dialog di inserimento di un nuovo PdL nella lista di PdL esclusi da MPE

Cliccare “OK”

## 6.3 Esecuzione run MPE

Per effettuare il lancio dell’elaborazione da parte dell’MPE, l’utente è tenuto a seguire i passaggi di seguito descritti. Si specifica che la funzionalità è abilitata solo agli utenti di profilo P1, P5, P6, P7, P8, P16, P17, secondo le autorizzazioni di sistema.

Accedere all’applicazione “Motore di pianificazione euristico”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Motore di pianificazione euristico”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Motore di pianificazione euristico”.

Figura: Lista delle applicazioni in EAM, con evidenza dell’applicazione “Motore di pianificazione euristico”

Cliccare su “Nuova RUN MPE”: l’utente è tenuto a cliccare sull’azione “Nuovo record” presente nel menù laterale.

Figura: Vista elenco dell'applicazione "Motore di pianificazione euristico" con evidenza dell'azione "Nuova RUN MPE"

Definire i parametri di esecuzione: l’utente è tenuto ad impostare l’UdB per la quale intende effettuare la pianificazione temporale mediante MPE e l’intervallo temporale entro il quale si desidera vengano posizionati gli OdL. Dopo aver compilato i campi, si procede al salvataggio.

Figura: vista interna di una nuova elaborazione mediante MPE con evidenza dei parametri di lancio e l'azione di salvataggio

Selezionare gli OdL da pianificare: dopo aver salvato, la tabella “Perimetro di pianificazione” si popolerà con gli OdL pianificabili mediante MPE per quell’UdB secondo i vincoli e logiche precedentemente definiti. In tabella, per ogni OdL è indicato:

Id OdL: codice univoco identificativo dell’OdL da pianificare;

Descrizione OdL: descrizione dell’attività;

Classificazione Asset

SAP ID

Asset: Codice identificativo dell’asset di primo livello in SAP. La colonna SAP ID asset è ordinabile;

Coin

Pianificato MPE: Campo valorizzato con “SI” o “NO”. Indica se l’OdL è mai stato pianificato dal MPE;

Data ultima pianificazione MPE: Indica l’ultima data in cui l’OdL è stato pianificato dal MPE;

Data pianificata inizio: definita nell’OdL;

Data pianificata fine: definita nell’OdL;

Data non prima di: definita nell’OdL;

Data massimo posticipo: definita nell’OdL;

Data proposta inizio

Data fine proposta

Responsabile attività

Data Modello: definita nell’OdL;

Inizio periodo escluso

Fine periodo escluso

Effort pianificato quadri / impiegati / operai: effort associato all’attività. È determinato dal prodotto tra ore esecuzione e quantità della figura professionale pianificata assegnata all’attività.

L’utente ha la possibilità di effettuare una selezione puntuale o massiva tramite apposite funzionalità. La spunta della check box in testata consente la selezione massiva dei soli odl visualizzabili nella pagina. Se necessario selezionare tutti gli ordini presenti nella lista (anche dopo averla filtrata) si utilizza il tasto azione nel menù di sinistra denominato “Seleziona tutti i record per l’invio ad MPE”. Questa funzionalità permette di selezionare ed inviare tutti i record della vista elenco compresi quelli presenti nelle pagine successive e precedenti a quella visualizzata. Il sistema, una volta cliccato sul tasto “Seleziona tutti i record per l’invio ad MPE” avvisa l’utente con un messaggio in sovraimpressione: “Attenzione, verranno inviati tutti i XXX odl presenti nella tabella di pianificazione. Procedere?”. Cliccando su “YES” vengono selezionati e inviati tutti gli ordini della pagina.

Figura: vista della tabella “Perimetro di pianificazione” con evidenza all’azione di selezione puntuale o massiva degli OdL

Per ogni OdL, l’utente può definire l’intervallo temporale per il quale desidera che l’OdL in questione non sia pianificato compilando i campi “Inizio periodo escluso” e “Fine periodo escluso” nella tabella “Perimetro di pianificazione”

Figura: vista della tabella “Perimetro di pianificazione” con evidenza dei campi “Inizio periodo escluso” e “Fine periodo escluso”

Cliccare su “Pianifica ordini selezionati”: se l’utente ha selezionato gli OdL tramite la spunta delle check box, deve procedere al lancio cliccando sull’azione “Pianifica ordini selezionati”

Figura: Evidenza dell'azione “Pianifica ordini selezionati”

Appena avviata l’elaborazione, il campo “Stato run” sarà impostato dal sistema con “IN CODA”.

## 6.4 Gestione proposta di pianificazione MPE

Dopo aver effettuato il lancio della pianificazione mediante MPE, si procede con il monitoraggio dello stato del run e la successiva gestione della proposta. L’utente è tenuto a seguire i passaggi di seguito descritti:

Accedere all’elaborazione di interesse: dalla vista elenco delle elaborazioni mediante MPE nell’applicazione “Motore di pianificazione euristico” selezionare il run di interesse.

Figura - vista elenco applicazione "Motore di pianificazione euristico"

Aggiornare lo stato: cliccando sulla funzionalità “Aggiorna stato”, l’utente ha la possibilità di aggiornare lo stato dell’elaborazione mediante MPE. Gli stati precedenti all’accettazione o rifiuto della proposta ammissibili sono:

IN CODA: il run è stato inserito nella coda di elaborazione.

IN ELABORAZIONE: il processo è in corso.

ELABORATA CON SUCCESSO: il run è stato completato correttamente.

ELABORAZIONE FALLITA: si è verificato un errore durante l’elaborazione.

Figura: evidenza stato run

Gestire la proposta: terminata con successo l’elaborazione (stato “ELABORATA CON SUCCESSO”) viene mostrata in maschera la proposta di pianificazione. La vista della tabella, disponibile tramite download su file Excel mediante apposite funzionalità evidenziate in figura, con le date proposte dal MPE è suddivisa per mese e contiene le seguenti colonne:

ID OdL: Codice univoco identificativo dell’OdL

Asset associato all’ordine

SAP-ID asset

PdL associato

CoIn

Tipo spesa

Priorità: Livello di priorità (alta, media o bassa) associato all’OdL

Capacity giornaliera impiegati: Capacity giornaliera impiegati erosa dall’OdL

Capacity giornaliera operai: Capacity giornaliera operai erosa dall’OdL

Capacity giornaliera quadri: Capacity giornaliera quadri erosa dall’OdL

Data inizio pianificata: Data inizialmente pianificata per l’inizio dell’attività

Data fine pianificata: Data inizialmente pianificata per la fine dell’attività

Data inizio proposta: Data di avvio dell’attività proposta dal MPE

Data fine proposta: Data di fine dell’attività proposta dal MPE

Figura: Tabella contenente la proposta di pianificazione con evidenza delle colonne relative alle date proposte

Figura: Tabella contenente la proposta di pianificazione con evidenza delle funzionalità di Download

L’utente ha la possibilità di accettare o rifiutare la proposta tramite appositi pulsanti

Figura: Evidenza dei pulsanti per confermare o rifiutare la proposta

Nota: la conferma o il rifiuto della proposta riguarda l’intera proposta. Non è possibile accettare la proposta di pianificazione solo per alcuni OdL. Prima della conferma o rifiuto non è possibile lanciare una nuova elaborazione per quell’UdB.

Se l’utente approva l’elaborazione effettuata dal MPE, questa viene accettata e applicata a tutti gli OdL elaborati, comportando la modifica definitiva delle date di fine e inizio pianificate per le attività nonché la conseguente modifica delle date pianificate di risorse interne ed esterne.

L’utente ha la possibilità anche di avviare una seconda elaborazione dell’MPE, attraverso la funzionalità “Nuova run MPE” per gli OdL per i quali non è soddisfatto della proposta di pianificazione accettata/rifiutata seguendo la procedura di seguito descritta:

Selezionare gli OdL di interesse: l’utente è tenuto a selezionare gli OdL dell’UdB di interesse per i quali effettuare un secondo run.

Figura: Evidenza della funzionalità di selezione OdL

Cliccare su “Pianifica ordini selezionati”: cliccando sulla funzionalità, l’utente avvia la seconda elaborazione dell’MPE solo per determinati OdL selezionati. In alternativa, può cliccare l’azione “Seleziona tutti i record per l’invio mpe” che consente di selezionare tutti i record e inviarli così come precedentemente descritto.

Figura: Evidenza della funzionalità "Pianifica ordini selezionati"

Le attività da eseguire per il monitoraggio dello stato del run e di accettazione o rifiuto della proposta restano invariate rispetto a quanto descritto per il primo lancio.

Figura - Evidenza dello stato "IN CODA"

# PROCESSO UI: ESECUZIONE E CONSUNTIVAZIONE MOBILE

## 7.1 Introduzione

L’applicazione mobile di EAM supporta l’operatività in campo nell’esecuzione di attività riconducibili al processo UI. Le principali azioni possibili sono:

Navigazione dei dati degli Asset

Registrazione di difformità, interferenze e misure

Esecuzione di micro-controlli (per asset di stazione).

7.2 Registrazione di difformità, misure e interferenze

Come anticipato, una parte dell’esecuzione degli OdL viene effettuata tramite EAM Mobile (solo per OdL di competenza puramente UI). Durante i controlli di monitoraggio, l’operatore può rilevare difformità, interferenze o misure. Le sezioni seguenti forniscono indicazioni su come registrare correttamente tali informazioni a sistema.

La tabella riporta l’insieme di attributi che servono a caratterizzare le difformità, così come la loro obbligatorietà. Questi attributi rappresentano elementi fondamentali per una corretta gestione, delle difformità all’interno del sistema.

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| Applicabile a | Definisce se la difformità è riferita a una classificazione o a un singolo elemento |  | Obbligatorio |  |
| Elemento asset | Elemento asset associato alla difformità | Pick-list | Obbligatorio |  |
| Classificazione asset | Classificazioni associate all'asset | Popolato automaticamente in base all’elemento asset | Facoltativo |  |
| Gerarchia asset associato | Asset su cui è rilevata la difformità | Popolato automaticamente in base alla classificazione asset | Obbligatorio |  |
| Assieme | Clusterizzazione della difformità | Pick-list | Facoltativo |  |
| Sottoassieme | Ulteriore clusterizzazione | Pick-list | Facoltativo |  |
| Stato | Stato corrente della difformità | Pick-list | Obbligatorio |  |
| ID difformità | Identificativo unico della difformità | Popolato automaticamente | Obbligatorio |  |
| Descrizione | Descrizione testuale della difformità | Testo libero | Obbligatorio |  |
| Gravità difformità | Valore di gravità per confronto policy | Pick-list / Valori alfanumerici | Facoltativo |  |
| Massimo tempo di validazione | Giorni massimi per stato 'In validazione' | Testo libero | Obbligatorio |  |
| Misure difformità | Misure o variabili di stato associate | Pick-list | Facoltativo |  |
| Generazione automatica | Indica se la difformità è generata automaticamente da misure/parametri | Flag | Facoltativo |  |
| Parametri per generazione | Parametri usati nella formula di generazione automatica della difformità | Inserimento tabellare filtrato | Obbligatorio se Generazione automatica = Sì |  |
| Formula | Formula di calcolo della difformità da eseguire automaticamente | Testo libero con regole GUI | Obbligatorio se Generazione automatica = SÌ |  |
| Caratteristiche asset | Attributi specifici che l’asset deve avere affinché la difformità sia rilevabile | Inserimento manuale | Facoltativo |  |
| Fase? | Indica la fase di riferimento | Flag | Facoltativo |  |
| Obbligatorietà foto | Richiesta obbligatorietà foto | Flag | Facoltativo |  |
| Modificato da | Matricola utente ultima modifica | Popolato automaticamente | Obbligatorio |  |
| Data ultima modifica | Data ultima modifica difformità | Popolato automaticamente | Obbligatorio |  |
| Tipologia difformità | Tipo di difformità: Anomalia / Guasto / Anomalia+Guasto | Pick-list | Obbligatorio |  |
| Attività che risolvono la difformità | Attività risolutive associate | Non modificabile | Facoltativo |  |

Tabella - Campi della difformità

La tabella riporta l’insieme di attributi che servono a caratterizzare le interferenze, così come la loro obbligatorietà. Questi attributi rappresentano elementi fondamentali per una corretta gestione delle interferenze all’interno del sistema.

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| Applicabile a | Definisce se l’interferenza è riferita a una classificazione o a un singolo elemento |  | Obbligatorio |  |
| Elemento asset | Elemento asset associato all'interferenza | Pick-list | Obbligatorio |  |
| Classificazione asset | Classificazioni associate all'asset | Popolato automaticamente in base all’elemento asset | Facoltativo |  |
| Gerarchia asset associato | Asset su cui è rilevata l’interferenza | Popolato automaticamente in base alla classificazione asset | Obbligatorio |  |
| Assieme | Clusterizzazione dell’interferenza | Pick-list | Facoltativo |  |
| Sottoassieme | Ulteriore clusterizzazione | Pick-list | Facoltativo |  |
| Stato | Stato corrente dell’interferenza | Pick-list | Obbligatorio |  |
| ID interferenza | Identificativo unico dell’interferenza | Popolato automaticamente | Obbligatorio |  |
| Descrizione | Descrizione testuale dell’interferenza | Testo libero | Obbligatorio |  |
| Gravità interferenza | Valore di gravità per confronto policy | Pick-list / Valori alfanumerici | Facoltativo |  |
| Massimo tempo di validazione | Giorni massimi per stato 'In validazione' | Testo libero | Obbligatorio |  |
| Misure interferenza | Misure o variabili di stato associate | Pick-list | Facoltativo |  |
| Fase? | Indica la fase di riferimento | Flag | Facoltativo |  |
| Obbligatorietà foto | Richiesta obbligatorietà foto | Flag | Facoltativo |  |
| Modificato da | Matricola utente ultima modifica | Popolato automaticamente | Obbligatorio |  |
| Data ultima modifica | Data ultima modifica interferenza | Popolato automaticamente | Obbligatorio |  |
| Attività che risolvono l’interferenza | Attività risolutive associate | Non modificabile | Facoltativo |  |

Tabella - Campi dell'interferenza

La tabella riporta l’insieme di attributi che servono a caratterizzare le misure, così come la loro obbligatorietà. Questi attributi rappresentano elementi fondamentali per una corretta gestione delle interferenze all’interno del sistema.

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà |  |
| --- | --- | --- | --- | --- |
| Applicabile a |  | Popolato automaticamente | Obbligatorio |  |
| ID Misura/Variabile | ID univoco misura/variabile | Popolato automaticamente | Obbligatorio |  |
| Descrizione | Descrizione della misura/variabile | Testo libero | Obbligatorio |  |
| Tipo parametro | Tipologia del parametro | Pick-list | Obbligatorio |  |
| Classificazione Asset | Tabella contenente le classificazioni alle quali è associata la misura/variabile | Popolamento automatico in base all’elemento asset | Obbligatorio |  |
| Gerarchia asset associato | Struttura gerarchica dell’asset (es. asset primario e derivati) | Popolamento automatico in base alla classificazione asset | Obbligatorio |  |
| Tipologia input | Fonte da cui deriva la misura/variabile | Pick-list | Obbligatorio |  |
| Input | Parametro usato come input | Pick-list | Obbligatorio |  |
| Tipologia asset | Asset / classificazione a cui si riferisce | Pick-list | Obbligatorio |  |
| Calcolo | Modalità di calcolo della variabile. Il campo può essere valorizzato con: - Valore da lista predefinita, come SUM (per input di tipo "Misura") - Formula a testo libero (max 3500 caratteri), secondo le regole: prefisso VAR per misure, TRG per soglie; nessun altro input ammesso. Nel caso di variabili di stato, può assumere i seguenti valori: COUNT DATAACT DATAOPT DIFFANNI Formula a testo libero MAXHICONDUTTORI CountForListStatiAttivitaFinestra: Per il calcolo countForListStatiAttivitaFinestra, sono visibili i campi “Stato attività” (valori: “Eseguito”, “Aperto”) e “Periodo di calcolo”. calculateDaily gestione evento eseguito INTERF: calcola il numero di opere interferenti presenti a sistema sotto la campata. È applicabile solo per variabili associate ad asset di tipo campata. | Pick-list/testo libero | Obbligatorio |  |
| Unità di misura |  | Pick-list | Facoltativo |  |
| Stato | Stato attività per il calcolo. Visualizzabile solo quando il calcolo è COUNTFORLISTASTATILISTAATTIVITAFINESTRA | Pick-list | Obbligatorio se Tipo parametro = Variabile di stato |  |
| Periodo di calcolo | Periodo temporale di riferimento (in giorni) | Testo libero (numerico) | Obbligatorio se Tipo parametro = Variabile di stato |  |
| Reset? | Definisce se necessario o meno eseguire il reset della variabile | Flag | Obbligatorio se Tipo parametro = Variabile di stato | Obbligatorio se Tipo parametro = Variabile di stato |
| Calcolo parametri misure | Tabella contenente i parametri utilizzati nel calcolo della variabile di stato | Tabella | Obbligatorio se Tipo parametro = Variabile di stato | Obbligatorio se Tipo parametro = Variabile di stato |
| Reset parametri misure | tabella contente l’elenco di attività che determina il reset della variabile di stato, editabile e visibile solo se il campo “Reset?” viene valorizzato con “SI”. | Tabella | Obbligatorio se Tipo parametro = Variabile di stato e flag “Reset?” attivo | Obbligatorio se Tipo parametro = Variabile di stato e flag “Reset?” attivo |

Tabella - Campi della misura

Difformità, interferenze e misure possono essere registrate o direttamente sull’OdL o sui singoli micro-controlli associati. Se l’odl si trova in uno stato diverso da ESEGUITO o CHIUSO è possibile inserire la rilevazione, sia lato web che lato mobile, in un micro-controllo in stato ESEGUITO.

### 7.2.1 Rilevazione di difformità tramite EAM Mobile

Tramite una funzionalità custom, su EAM Mobile è possibile registrare le difformità rilevate durante l’esecuzione di controlli. Tale operazione può essere effettuata dagli utenti appartenenti ai profili P1, P16, P7, P8, P17 e P9.

Il processo di registrazione delle difformità associate direttamente all’OdL avviene nel seguente modo:

Accedere all’applicazione “My Schedule” dal menu: cliccando sull’icona in basso a destra, è possibile accedere all’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “My Schedules” per accedere agli OdL assegnati che sono in stato “Inviato” o successivo.

Figura - Applicazione “My Schedule”

Selezionare l’OdL su cui desidera registrare una difformità: all’interno dell’OdL, l’utente può visualizzare la descrizione dell’OdL, le date di schedulazione, la durata e l’asset associato con il suo SAPID (o il SAPID dell’asset di livello superiore se l’asset associato non ha un suo SAPID).

Figura– Lista OdL assegnati

Nota: sono accessibili solamente gli OdL schedulati per il giorno corrente.

Avviare la funzione “Nuova difformità”: questa è la schermata in cui avviene la registrazione delle difformità.

Figura - Nuova difformità

Selezionare l’asset sul quale si desidera effettuare la rilevazione: verranno mostrati solo gli asset relazionati con quello presente OdL, e per la cui classificazione esiste almeno una difformità rilevabile. I campi obbligatori sono contrassegnati dal pallino blu.

Figura - Selezione asset difformità

Selezionare la difformità: il sistema rende disponibile in questo campo solo le difformità che sono rilevabili dallo specifico controllo per l’asset precedentemente inserito.

Figura – Codice catalogo difformità

Determinare il livello di gravità: l’utente deve selezionare il livello di gravità della difformità inserita. Nel caso di registrazione di una difformità uguale ma con differente valore del campo gravità il sistema chiederà se la difformità con altro valore di gravità deve essere mantenuta o meno.

Figura - Gravità difformità

Inserire l’eventuale misura associata alla difformità: selezionare l’id misura dalla vista elenco e impostare il valore numerico rilevato. Il campo “Misura” è da popolare tramite la tendina con uno dei valori definiti a catalogo per la difformità selezionata.

Figura - Misura associata alla difformità

Indicare l’eventuale fase su cui è presente la difformità: nel caso di difformità rilevate su asset di 4° livello di stazioni, è necessario indicare anche la fase sul quale è stata rilevata la difformità.

Figura - Fase difformità

Indicare l’eventuale risoluzione contestuale: per le piccole manutenzioni, l’utente deve attivare il flag “Risolta contestualmente”, che permette al sistema di tracciare la difformità direttamente in stato “Risolta”.

Figura - Risoluzione contestuale difformità

Nota: per gli OdL con una difformità risolta contestualmente, nel momento in cui passano allo stato “Eseguito” viene automaticamente generato un ulteriore OdL in stato “Chiuso”. Tale OdL sarà associato a un PdL applicato universalmente a ogni tipo di asset e difformità. Nell’OdL generato verrà riportato il consuntivo fisico dell’asset associato alla difformità, mentre il consuntivo orario viene tracciato sull’OdL di controllo che ha rilevato la difformità. Inoltre, l’OdL di risoluzione non avrà date di pianificazione, ma solo quella di esecuzione.

Inserire eventuali note: nel caso in cui l’operatore inserisca sul medesimo asset una difformità uguale a una precedentemente rilevata, il sistema genererà un alert con scritto “difformità già presente su asset, procedere comunque con l’inserimento?”. Per distinguere le difformità l’operatore dovrà inserire nelle “note” i dettagli relativi ad ognuna.

Figura - Note difformità

Allegare eventuali allegati cliccando su “+”: l’allegato è logicamente associato all’OdL e non alla singola difformità. Tuttavia, partendo dalla difformità è recuperabile passando attraverso l’ordine che l’ha rilevata. Per le difformità per cui è obbligatorio allegare una foto, viene mostrato in rosso un messaggio di richiesta di caricamento foto. Nel caso di mancato caricamento, la difformità non viene salvata e non sarà visibile da WEB.

Figura - Allegato difformità

Salvare le modifiche effettuate: la nuova difformità è ora legata all’OdL.

Figura - Salvataggio difformità

### 7.2.2 Rilevazione di interferenze tramite EAM Mobile

Tramite una funzionalità custom, su EAM Mobile è possibile registrare le interferenze rilevate durante l’esecuzione di controlli e micro-controlli. Tale operazione può essere effettuata dagli utenti appartenenti ai profili P1, P16, P7, P8, P17 e P9.

Il processo di registrazione delle interferenze associate direttamente all’OdL avviene nel seguente modo:

Accedere all’applicazione “My Schedule” dal menu: cliccando sull’icona in basso a destra, è possibile accedere all’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “My Schedules” per accedere agli OdL assegnati all’utente che sono in stato “Inviato” o successivo.

Figura - Applicazione “My Schedule”

Selezionare l’OdL su cui desidera registrare un’interferenza: all’interno dell’OdL, l’utente può visualizzare la descrizione dell’OdL, le date di schedulazione, la durata e l’asset associato con il suo SAPID (o il SAPID dell’asset di livello superiore se l’asset associato non ha un suo SAPID).

Figura– Lista OdL assegnati

Nota: sono accessibili solamente gli OdL schedulati per il giorno corrente.

Avviare la funzione “Nuova interferenza”: questa è la schermata in cui avviene la registrazione delle interferenze.

Figura - Nuova interferenze

Selezionare l’asset sul quale si desidera effettuare la rilevazione: verranno mostrati solo gli asset relazionati con quello presente sull’OdL, e per la cui classificazione esiste almeno un’interferenza rilevabile.

Figura - Selezione asset interferenza

Selezionare l’interferenza: il sistema rende disponibile in questo campo solo le interferenze che sono rilevabili dallo specifico controllo per l’asset precedentemente inserito.

Figura - Codice catalogo interferenza

Determinare il livello di gravità: l’utente deve selezionare il livello di gravità della interferenza inserita. Nel caso di registrazione di un’interferenza uguale ma con differente valore del campo gravità il sistema chiederà se l’interferenza con altro valore di gravità deve essere mantenuta o meno.

Figura - Gravità interferenza

Inserire l’eventuale misura associata all’interferenza: selezionare l’id misura dalla vista elenco e impostare il valore numerico rilevato. Il campo “Misura” è da popolare tramite la tendina con uno dei valori definiti a catalogo per la difformità selezionata.

Figura - Misura associata all'interferenza

Indicare l’eventuale fase su cui è presente l’interferenza: nel caso di interferenze rilevate su asset di 4° livello di stazioni, è necessario indicare anche la fase sul quale è stata rilevata l’interferenza.

Figura - Fase interferenza

Indicare l’eventuale risoluzione contestuale: per le piccole manutenzioni, l’utente deve attivare il flag “Risolta contestualmente”, che permette al sistema di tracciare l’interferenza direttamente in stato “Risolta”.

Figura - Risoluzione contestuale interferenza

Nota: per gli OdL con un’interferenza risolta contestualmente, nel momento in cui passano allo stato “Eseguito” viene automaticamente generato un ulteriore OdL in stato “Chiuso”. Tale OdL sarà associato a un PdL applicato universalmente a ogni tipo di asset e interferenza. Nell’OdL generato verrà riportato il consuntivo fisico dell’asset associato all’interferenza, mentre il consuntivo orario viene tracciato sull’OdL di controllo che ha rilevato l’interferenza. Inoltre, l’OdL di risoluzione non avrà date di pianificazione, ma solo quella di esecuzione.

Inserire eventuali note: nel caso in cui l’operatore inserisca sul medesimo asset un’interferenza uguale a una precedentemente rilevata, il sistema genererà un alert con scritto “interferenza già presente su asset, procedere comunque con l’inserimento?”. Per distinguere le interferenze l’operatore dovrà inserire nelle “note” i dettagli relativi ad ognuna.

figura - Note interferenza

Allegare eventuali allegati cliccando su “+”: l’allegato è logicamente associato all’OdL e non alla singola interferenza. Tuttavia, partendo dall’interferenza è recuperabile passando attraverso l’ordine che l’ha rilevata. Per le interferenze per cui è obbligatorio allegare una foto, viene mostrato in rosso un messaggio di richiesta di caricamento foto. Nel caso di mancato caricamento, la difformità non viene salvata e non sarà visibile da WEB.

Figura - Allegato interferenza

Salvare le modifiche effettuate: la nuova interferenza è ora legata al’OdL.

Figura - Salvataggio interferenza

### 7.2.3 Rilevazione di misure tramite EAM Mobile

Tramite una funzionalità custom, su EAM Mobile è possibile registrare le misure rilevate durante l’esecuzione di controlli e micro-controlli. Tale operazione può essere effettuata dagli utenti appartenenti ai profili P1, P16, P7, P8, P17 e P9.

Il processo di registrazione delle misure associate direttamente all’OdL avviene nel seguente modo:

Accedere all’applicazione “My Schedule” dal menu: cliccando sull’icona in basso a destra, è possibile accedere all’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “My Schedules” per accedere agli OdL assegnati all’utente che sono in stato “Inviato” o successivo.

Figura - Applicazione “My Schedule”

Selezionare l’OdL su cui desidera registrare una misura: all’interno dell’OdL, l’utente può visualizzare la descrizione dell’OdL, le date di schedulazione, la durata e l’asset associato con il suo SAPID (o il SAPID dell’asset di livello superiore se l’asset associato non ha un suo SAPID).

Figura– Lista OdL assegnati

Nota: sono accessibili solamente gli OdL schedulati per il giorno corrente.

Avviare la funzione “Nuova misura”: questa è la schermata in cui avviene la registrazione delle misure.

Figura - Nuova misura

Selezionare l’asset sul quale si desidera effettuare la rilevazione: verranno mostrati solo gli asset per cui è possibile registrare una misura.

Figura - Selezione asset misura

Selezionare la misura da registrare: il sistema rende disponibile in questo campo solo le misure che sono definite a catalogo per la classificazione dell’asset selezionato.

Figura - Codice catalogo misura

Indicare il valore rilevato: inserire un valore numerico.

Figura - Valore misura

Salvare le modifiche effettuate: la nuova misura è ora legata all’OdL.

Figura - Salvataggio misura

### 7.2.4 Gestione di difformità e interferenze esistenti tramite EAM Mobile

L’applicazione EAM Mobile offre la possibilità di confermare o non confermare le difformità e le interferenze aperte su ciascun asset registrato nel sistema. Tale operazione può essere effettuata dagli utenti appartenenti ai profili P1, P16, P7, P8, P17 e P9.

I passaggi sono i seguenti:

Accedere all’applicazione “My Schedule” dal menu: cliccando sull’icona in basso a destra, è possibile accedere all’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “My Schedules” per accedere agli OdL assegnati all’utente che sono in stato “Inviato” o successivo.

Figura - Applicazione “My Schedule”

Selezionare l’OdL per cui desidera confermare una difformità o interferenza: all’interno dell’OdL, l’utente può visualizzare la descrizione dell’OdL, le date di schedulazione, la durata e l’asset associato con il suo SAPID (o il SAPID dell’asset di livello superiore se l’asset associato non ha un suo SAPID).

Figura – Lista OdL assegnati

Aprire la sezione “Rilevazioni precedenti”: viene mostrata una schermata che contiene la tabella di tutte le difformità aperte sugli asset relativi all’OdL e che sono rilevabili tramite l’attività che sta svolgendo in quel momento. Nella tabella sono previste le seguenti colonne:

Assetnum: codice asset sul quale è stata rilevata la difformità. Nel caso in cui un OdL di tipologia controllo faccia riferimento a più asset, la colonna permette all’utente di distinguere gli asset su cui sono registrate le singole difformità.

ID difformità: ID a catalogo della difformità aperta sull’asset.

Descrizione: Descrizione della difformità aperta sull’asset.

Stato: stato in cui si trova la difformità.

Gravità: livello di gravità associato alla difformità. Nel caso in cui sia necessario modificare il livello di gravità di una difformità già aperta, è possibile farlo direttamente dall’applicazione aggiungendo una nuova difformità/interferenza con un diverso livello di gravità. Il sistema provvede a mettere in “superata” con causale “cambio gravità” la difformità con livello vecchio e registrare la nuova difformità a sistema.

Matricola rilevatore: numero della matricola che ha rilevato la difformità.

Non confermare: bottone attraverso cui l’utente può determinare se la difformità è ancora presente o meno.

Figura - Rilevazioni precedenti

Avviare la funzione “Non confermare” sulla rilevazione di interesse: la non conferma della difformità/interferenza risulta nel cambio di stato della difformità in “superata” con causale “non confermata”. Ciò significa che la difformità/interferenza precedentemente rilevata non è più presente.

Figura - Difformità non confermata

Nota: se l’utente non clicca il bottone, la difformità/interferenza viene automaticamente confermata. Nel caso in cui si dovesse confermare una difformità/interferenza a cui è associata una misura, l’utente deve rilevare nuovamente la misura. Se questa non è coerente con quella già presente, deve non confermare la difformità e aggiungere una nuova difformità con la nuova misura rilevata.

## 7.3 Validazione di difformità e interferenze tramite EAM Web

⁠Tramite l’applicazione “Difformità rilevate” e “Interferenze rilevate”, il responsabile attività può accedere alla lista di tutte le difformità/interferenze di sua competenza e validarle. Tale operazione può essere effettuata dagli utenti appartenenti ai profili P1, P16, P7, P8, P17 e P9.

I prossimi step faranno riferimento alle difformità, ma valgono anche per le interferenze:

Accedere all’applicazione “Difformità rilevate”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Applicazione "Difformità rilevate"

Selezionare le difformità da validare: utilizzare la checkbox presente a inizio riga per selezionare una difformità. È anche possibile selezionare massivamente tutti i record presenti nella pagina tramite la checkbox in testata.

Figura - Selezione difformità da validare

Nota: la schermata contiene inizialmente la lista di tutte le difformità ancora da validare. Nel caso in cui si volessero visualizzare tutte le difformità/interferenze (in qualsiasi stato) o solo quelle in uno stato diverso da “Da Validare”, è sufficiente rimuovere il filtro di default dalla colonna “Stato”, ed eventualmente selezionare un altro valore. Inoltre, cliccando su “Query disponibili”, è possibile selezionare l’opzione di visualizzare solo le difformità/interferenze associate ad asset di linea o quelle associate agli asset di stazione.

Avviare la funzione “Cambia stato”: scegliere lo stato che si vuole assegnare alle difformità, cioè “Validata” o “Non validata”.

Figura - Cambio stato difformità

Nota: è possibile effettuare il cambio stato solamente per le difformità che si trovano in stato “Da validare”.

Selezionare l’esito della validazione e confermare: selezionare lo stato “Validata” o “Non validata” e cliccare “OK” per validare la difformità.

Figura - Conferma validazione

## 7.4 Registrazione di difformità senza OdL tramite EAM Web

Alcune difformità devono essere associate direttamente sull’asset senza che ci sia un OdL di riferimento che ne permetta la registrazione. La registrazione di tali difformità è effettuabile dagli utenti appartenenti ai profili P1, P16, P7, P8, P17 e P9.

I prossimi step faranno riferimento alle difformità, ma valgono anche per le interferenze:

Accedere all’applicazione “Difformità rilevate”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura - Applicazione "Difformità rilevate"

Premere il bottone “+” per aggiungere un nuovo record: viene aggiunta una nuova riga in fondo alla tabella.

Figura – Nuova difformità

Selezionare il codice catalogo della difformità: popolare la colonna “ID difformità” con uno dei valori della vista elenco.

Figura - Selezione codice catalogo difformità

Selezionare l’asset su cui si vuole registrare la difformità: popolare la colonna “Asset” con uno dei valori della vista elenco.

Figura - Selezione asset difformità

Selezionare il livello di gravità: popolare la colonna “Gravità” con uno dei valori della vista elenco.

Figura - Selezione gravità difformità

Salvare la nuova difformità.

Figura - Salvataggio difformità

Nota: la difformità appena inserita si troverà in stato “Da validare”. Per vederla sugli OdL che insistono sull’asset di riferimento, l’utente deve cambiare lo stato in “Validata”. Se l’utente la conferma, nell’applicazione “difformità rilevate” viene aggiornata la data di ultima rilevazione e associato l’OdL tramite il quale è avvenuta la conferma.

## 7.5 Registrazione di micro-controlli

Per le attività di controllo periodico è prevista l’esecuzione di micro-controlli. I micro-controlli associabili ad un OdL sono definiti sulla base del PdL e dell’asset di 1° livello, che deve essere una stazione. Inoltre, è possibile rilevare difformità e interferenze attraverso specifici micro-controlli. Le difformità e le interferenze rilevabili sono riportate nel catalogo dei micro-controlli.

La Tabella V riporta l’insieme di attributi che servono a caratterizzare i micro-controlli, così come la loro obbligatorietà. Questi attributi rappresentano elementi fondamentali per una corretta gestione dei micro-controlli all’interno del sistema.

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà | Obbligatorietà |
| --- | --- | --- | --- | --- |
| Applicabile a | Definisce se il micro-controllo è riferito a una classificazione o a un singolo elemento | Popolato automaticamente | Obbligatorio |  |
| ID Micro-controllo | Identificativo univoco del micro-controllo | Popolato automaticamente | Obbligatorio |  |
| Assieme | Clusterizzazione del micro-controllo | Pick-list | Facoltativo |  |
| Sottoassieme | Clusterizzazione del micro-controllo | Pick-list | Facoltativo |  |
| Descrizione micro-controllo | Descrizione libera del micro-controllo | Testo libero | Obbligatorio |  |
| Controllo | Tipologia del controllo | Pick-list | Obbligatorio |  |
| Elemento Asset | Elemento asset associato al micro-controllo | Pick-list | Obbligatorio |  |
| Classificazione asset | Classificazioni associate all’elemento asset | Popolato automaticamente in base all’elemento asset | Obbligatorio |  |
| Gerarchia asset associato | Asset su cui è incide il micro-controllo | Popolato automaticamente in base alla classificazione asset | Obbligatorio |  |
| Difformità rilevabili | Elenco di difformità rilevabili dal micro-controllo | Pick-list | Facoltativo |  |
| Interferenze rilevabili | Elenco di interferenze rilevabili dal micro-controllo | Pick-list | Facoltativo |  |
| Caratteristica asset | Specifiche caratteristiche tecniche dell’asset per l’associazione | Popolato manualmente | Facoltativo |  |

Tabella - Campi del micro-controllo

## 7.6 Esecuzione di micro-controlli tramite EAM Mobile

L’esecuzione dei micro-controlli è effettuata tramite una maschera specifica. Tale operazione può essere effettuata dagli utenti appartenenti ai profili P1, P16, P7, P8, P17 e P9.

I passaggi sono i seguenti:

Accedere all’applicazione “My Schedule” dal menu: cliccando sull’icona in basso a destra, è possibile accedere all’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “My Schedules” per accedere agli OdL assegnati all’utente che sono in stato “Inviato” o successivo.

Figura - Applicazione “My Schedule”

Selezionare l’OdL di cui si desidera eseguire un micro-controllo: all’interno dell’OdL, l’utente può visualizzare la descrizione dell’OdL, le date di schedulazione, la durata e l’asset associato con il suo SAPID (o il SAPID dell’asset di livello superiore se l’asset associato non ha un suo SAPID).

Figura - Applicazione "My Schedule"

Accedere alla lista dei micro-controlli: nell’elenco sono disponibili tutti i micro-controlli associati all’OdL selezionato. Ogni micro-controllo è associato all’asset di quarto livello a cui fa riferimento.

Figura – Dettagli OdL

Selezionare il micro-controllo che si vuole eseguire: è possibile espandere il micro-controllo per visualizzarne i dettagli. Il sistema riporta l’asset associato (eventuale SAPID, classificazione, stato e descrizione) e lo stato di avanzamento dell’attività.

Figura - Lista micro-controlli

Nota: La lista dei micro-controlli può essere filtrata per l’elemento di livello superiore all’oggetto del controllo (ad es. stallo o sezione).

Avviare la funzione “Esegui micro-controllo”: l’esecuzione dei micro-controlli avviene direttamente all’interno della vista dei micro-controlli associati all’OdL, utilizzando l’apposito bottone con la spunta. Una volta cliccato, il bottone diventa verde e genera automaticamente una notifica sulla sincronizzazione tra Mobile e Web. Se la sincronizzazione con EAM Web fallisce, il pulsante ritorna allo stato iniziale e il messaggio di errore persiste.

Figura - Esecuzione micro-controllo

Nota: una volta conclusa l’esecuzione dei micro-controlli assegnati e la registrazione delle difformità/interferenze, l’OdL può essere portato in stato “Eseguito”.

## 7.7 Esecuzione micro-controlli con registrazione di Difformità / Interferenze

Tramite una funzionalità custom, su EAM Mobile è possibile registrare le difformità, interferenze e misure rilevate durante l’esecuzione di micro-controlli. Tale operazione può essere effettuata dagli utenti appartenenti ai profili P1, P16, P7, P8, P17 e P9.

### 7.7.1 Rilevazione difformità

La registrazione delle difformità relative ai micro-controlli è effettuata tramite una maschera specifica. I passaggi sono i seguenti:

Accedere all’applicazione “My Schedule” dal menu: cliccando sull’icona in basso a destra, è possibile accedere all’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “My Schedules” per accedere agli OdL assegnati all’utente che sono in stato “Inviato” o successivo.

Figura - Applicazione “My Schedule”

Selezionare l’OdL su cui desidera registrare una difformità: all’interno dell’OdL, l’utente può visualizzare la descrizione dell’OdL, le date di schedulazione, la durata e l’asset associato con il suo SAPID (o il SAPID dell’asset di livello superiore se l’asset associato non ha un suo SAPID).

Figura - Applicazione "My Schedule”

Accedere alla lista dei micro-controlli: nell’elenco sono disponibili tutti i micro-controlli associati all’OdL selezionato. Ogni micro-controllo è associato all’asset di quarto livello a cui fa riferimento.

Figura – Dettagli OdL

Selezionare il micro-controllo su cui si vuole effettuare la rilevazione: è possibile registrare difformità relative al singolo micro-controllo.

Figura - Lista micro-controlli

Nota: La lista dei micro-controlli può essere filtrata per l’elemento di livello superiore all’oggetto del controllo (ad es. stallo o sezione).

Avviare la funzione “Nuova difformità”: si viene portati sulla schermata in cui è possibile inserire i dati della nuova difformità.

Figura - Dettagli micro-controllo

Popolare i campi presenti in schermata: è possibile selezionare solo difformità rilevabili tramite quel micro-controllo. Gli altri campi seguono le stesse logiche delle rilevazioni sui controlli.

Figura - Campi nuova difformit

Salvare le modifiche effettuate: la nuova difformità è ora legata al micro-controllo.

Figura - Salvataggio difformità

### 7.7.2 Rilevazione interferenze

La registrazione delle interferenze relative ai micro-controlli è effettuata tramite una maschera specifica. I passaggi sono i seguenti:

Accedere all’applicazione “My Schedule” dal menu: cliccando sull’icona in basso a destra, è possibile accedere all’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “My Schedules” per accedere agli OdL assegnati all’utente che sono in stato “Inviato” o successivo.

Figura - Applicazione “My Schedule”

Selezionare l’OdL su cui desidera registrare un’interferenza: all’interno dell’OdL, l’utente può visualizzare la descrizione dell’OdL, le date di schedulazione, la durata e l’asset associato con il suo SAPID (o il SAPID dell’asset di livello superiore se l’asset associato non ha un suo SAPID).

Figura - Applicazione "My Schedule"

Accedere alla lista dei micro-controlli: nell’elenco sono disponibili tutti i micro-controlli associati all’OdL selezionato. Ogni micro-controllo è associato all’asset di quarto livello a cui fa riferimento.

Figura – Dettagli OdL

Selezionare il micro-controllo su cui si vuole effettuare la rilevazione: è possibile registrare interferenze relative al singolo micro-controllo.

Figura - Lista micro-controll

Nota: La lista dei micro-controlli può essere filtrata per l’elemento di livello superiore all’oggetto del controllo (ad es. stallo o sezione).

Avviare la funzione “Nuova interferenza”: si viene portati sulla schermata in cui è possibile inserire i dati della nuova interferenza.

Figura - Dettagli micro-controllo

Popolare i campi presenti in schermata: è possibile selezionare solo interferenze rilevabili tramite quel micro-controllo. Gli altri campi seguono le stesse logiche delle rilevazioni sui controlli.

Figura – Campi nuova interferenza

Salvare le modifiche effettuate: la nuova interferenza è ora legata al micro-controllo.

Figura - Salvataggio interferenza

### 7.7.3 Rilevazione misure

La registrazione delle misure relative ai micro-controlli è effettuata tramite una maschera specifica. I passaggi sono i seguenti:

Accedere all’applicazione “My Schedule” dal menu: cliccando sull’icona in basso a destra, è possibile accedere all’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “My Schedules” per accedere agli OdL assegnati all’utente che sono in stato “Inviato” o successivo.

Figura - Applicazione “My Schedule”

Selezionare l’OdL su cui desidera registrare una misura: all’interno dell’OdL, l’utente può visualizzare la descrizione dell’OdL, le date di schedulazione, la durata e l’asset associato con il suo SAPID (o il SAPID dell’asset di livello superiore se l’asset associato non ha un suo SAPID).

Figura - Applicazione "My Schedule"

Accedere alla lista dei micro-controlli: nell’elenco sono disponibili tutti i micro-controlli associati all’OdL selezionato. Ogni micro-controllo è associato all’asset di quarto livello a cui fa riferimento.

Figura – Dettagli OdL

Selezionare il micro-controllo su cui si vuole effettuare la rilevazione: è possibile registrare misure relative al singolo micro-controllo.

Figura - Lista micro-controlli

Nota: La lista dei micro-controlli può essere filtrata per l’elemento di livello superiore all’oggetto del controllo (ad es. stallo o sezione).

Avviare la funzione “Nuova misura”: si viene portati sulla schermata in cui è possibile inserire i dati della nuova misura.

Figura - Dettagli micro-controllo

Selezionare la misura da registrare: il sistema rende disponibile in questo campo solo le misure che sono definite a catalogo per la classificazione dell’asset selezionato.

Figura - Selezione misura da micro-controllo

Indicare il valore rilevato: inserire un valore numerico.

Figura - Valore numerico misura

Salvare le modifiche effettuate: la nuova misura è ora legata al micro-controllo.

Figura - Salvataggio misura

# PROCESSO UI: ESECUZIONE E CONSUNTIVAZIONE WEB

## 8.1 Introduzione

Le attività di monitoraggio di difformità, interferenze e misure sono svolte massivamente sulla piattaforma attraverso la creazione e analisi della relativa reportistica. Tale funzionalità è abilitata a tutti gli utenti, secondo le autorizzazioni di sistema. Allo scopo di effettuare un monitoraggio puntuale delle difformità, interferenze, misure e micro-controlli, l’utente ha due possibilità:

Consultare la scheda di condizione tecnica di un asset;

Consultare gli appositi tab all’interno dell’OdL nell’applicazione “Monitoraggio OdL”.

La funzionalità di consultazione della Scheda di condizione tecnica e del singolo OdL è abilitata a tutti gli utenti, secondo le autorizzazioni di sistema.

## 8.2 Monitoraggio difformità, interferenze, misure mediante scheda di condizione tecnica

All’interno della scheda di condizione tecnica, l’utente può visionare e monitorare le difformità, interferenze e misure associate all’asset seguendo i passaggi di seguito descritti.

Accedere all’applicazione "Scheda di condizione tecnica": posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni disponibili. L’utente deve selezionare l’opzione “Asset” e successivamente l’opzione “Scheda di condizione tecnica” per accedere all’applicazione desiderata. In alternativa, può inserire nel campo di ricerca la voce “Scheda di condizione tecnica” per essere indirizzato direttamente all’applicazione.

Figura: Lista di applicazioni di EAM, con evidenza all'applicazione "Scheda di condizione tecnica"

Selezionare l’asset di interesse: dalla vista elenco, selezionare l’asset per il quale si intende monitorare le difformità, interferenze, misure associati. È possibile ricercare l’asset di interesse filtrando per i seguenti campi:

Asset

Descrizione

SAP ID

Classificazione

UI

DT

Figura: vista elenco applicazione "Scheda di condizione tecnica" con evidenza del filtro applicato per il campo "Classificazione"

Accedere ai tab di interesse: dopo aver selezionato l’asset di interesse, il sistema apre la maschera, la quale è suddivisa nei seguenti tab:

Asset

Attributi di anagrafica

Attività associate

Difformità e interferenze

Opere interferenti

Indicatori

Misure e variabili

Documentazione

Figura: maschera della scheda di condizione tecnica di un asset con evidenza dei tab "Difformità e interferenze" e "Misure e variabili" di interesse

Accedere al tab “Difformità e interferenze”: all’interno del presente tab sono riportate le difformità e interferenze rilevate sull’asset, rispettivamente nelle tabelle “Difformità rilevate” e “Interferenze rilevate” . In entrambe viene riportato l’ID della difformità/interferenza, la descrizione, lo stato, la data di prima e ultima rilevazione e la gravità.

Figura: vista generale del tab "Difformità e Interferenze" con evidenza delle tabelle "Difformità rilevate" e "Interferenze rilevate"

Accedere al tab “Misure e variabili”: all’interno del tab sono riportate le tabelle “Misure” e “Variabili di stato” all’interno delle quali sono indicate, rispettivamente, le misure e variabili calcolate sull’asset. In entrambi i casi, oltre all’ID della misura/variabile, viene mostrata la descrizione, la data di ultima rilevazione, il valore e la relativa unità di misura.

Figura: vista generale del tab "Misure e variabili" con evidenza delle tabelle "Misure" e "Variabili di stato"

Nota: le informazioni relative agli asset, riportate nella scheda di condizione tecnica, sono accessibili unicamente per la consultazione da parte dell'utente.

## 8.3 Gestione dell’esecuzione di ordini di lavoro singoli ed aggregati (caso UI e misto)

Il processo di esecuzione di un ordine di lavoro si differenzia sulla base del Titris scelto in fase di pianificazione. La gestione dell’esecuzione di OdL singoli e aggregati per i casi UI e misto, inizia con il processo di invio verso Geocall degli OdL ai fini della loro schedulazione e termina con il passaggio di stato in “Eseguito” effettuato dall’utente. Gli utenti con profilo P1, P3, P5, P6, P7, P8, P11, P16, P15, P17 sono abilitati alla gestione dell’OdL.

Nota: si rimanda al modulo 2 per un’analisi dettagliata del processo di invio degli OdL verso Geocall.

Caso UI

Figura: Flusso OdL aggregati e singoli con evidenza della fase di esecuzione

Durante la fase di esecuzione l’OdL assume i seguenti stati:

Inviato: un OdL singolo assume lo stato “inviato” se l’operazione corrispondente in Geocall è in stato “Inoltrato”. Un OdL aggregatore assume tale stato se almeno uno degli OdL sottostanti è in stato “inoltrato” in Geocall. In questo stato è possibile registrare delle anomalie ed esitare dei micro-controlli, se presenti;

Avviato: un OdL singolo assume lo stato “Avviato” se l'operazione corrispondente in Geocall è in stato “in carico”. Un Odl aggregatore assume tale stato se almeno uno degli OdL sottostanti è in stato “avviato” in EAM o “in carico” in Geocall.

Eseguito parzialmente:

OdL singolo: un OdL singolo assume tale stato se è stato eseguito almeno un micro-controllo o è stata inserita almeno un’anomalia sull'OdL. Nel caso non siano previsti micro-controlli o registrazione di un’anomalia, l'OdL non transiterà per questo stato.

Per registrare un’anomalia o eseguire un micro-controllo (OdL su un elemento di stazione) su un OdL in stato “Inviato”, l’utente deve seguire i seguenti passaggi:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Monitoraggio OdL”. Altrimenti, può inserire nel campo di ricerca la dicitura “Monitoraggio OdL” per essere indirizzato all’applicazione desiderata.

Figura: Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL: dalla vista elenco, selezionare l’ordine di lavoro per il quale si intende registrare una difformità. È possibile ricercare l’OdL di interesse filtrando per i seguenti campi:

Ordine di lavoro

Descrizione OdL

SAP ID

Asset

Descrizione asset

Perimetro di intervento

Stato

Data inizio pianificata

Data fine pianificata

Data modello

Responsabile attività

Accedere al tab di interesse: l’utente può accedere al tab “Difformità e Interferenze” per inserire una difformità sull’OdL e al tab “Avanzamento” per esitare un microcontrollo.

Figura: evidenza sui tab "Difformità e Interferenze" e “Avanzamento”

Registrare una nuova difformità / interferenza: aggiungere una difformità / interferenza nella tabella “Difformità rilevate con il presente OdL” o “Interferenza rilevate con il presente OdL” utilizzando il pulsante. Fornire le seguenti informazioni sulla difformità rilevata:

Difformità

Asset

ID difformità

Gravità

ID misura

Valore misura

Fase

Allegati

Figura: vista generale tab "Difformità e interferenze" con evidenza del pulsante per creare un record nella tabella “Difformità rilevate con il presente OdL”

Salvare tramite apposito pulsante:

Figura vista del tab “Difformità e Interferenze” con evidenza dell’icona per il salvataggio

Esitare un microcontrollo: per esitare un microcontrollo, l’utente deve accedere al tab “Avanzamento” e cliccare l’icona evidenziata per il microcontrollo che intende portare in “Eseguito” e salvare.

Figura: vista tab avanzamento con evidenza dell'icona per il passaggio in Eseguito di un OdL figlio

OdL Aggregatore: un OdL aggregatore è in stato “Eseguito parzialmente” se almeno uno degli OdL sottostanti è in stato "Eseguito parzialmente", "Eseguito" o “Non eseguito”.

Eseguito: per gli OdL singoli tale stato viene impostato manualmente e il cambio stato è possibile solo se il corrispondente stato in Geocall è “chiuso”. Sulla piattaforma, per eseguire il cambio stato in “Eseguito”, l’utente deve seguire i passaggi descritti di seguito:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Monitoraggio OdL”. Altrimenti, può inserire nel campo di ricerca la dicitura “Monitoraggio OdL” per essere indirizzato all’applicazione desiderata.

Figura: Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL: dalla vista elenco, selezionare l’ordine di lavoro per il quale si intende effettuare l’operazione. È possibile ricercare l’OdL di interesse filtrando per i seguenti campi:

-Ordine di lavoro

-Descrizione OdL

-SAP ID

-Asset

-Descrizione asset

-Perimetro di intervento

-Stato

- Data inizio pianificata

- Data fine pianificata

-Data modello

-Responsabile attività

Utilizzare la funzione di inoltro workflow: cliccare sull’icona evidenziata

Figur: vista generale di un OdL con evidenza dell'icona di inoltro workflow

Cliccare “OK”: dopo lo step precedente si aprirà la dialog “Completa assegnazione workflow”. L’utente deve cliccare “OK” per ultimare il cambio stato in “Eseguito”.

Figura: Dialog “Completa assegnazione workflow”

Un OdL aggregatore assume lo stato “Eseguito” se tutti gli OdL sottostanti sono almeno in stato “Eseguito” e “Non eseguito” ma se tutti gli ordini sotto l’aggregatore sono in stato “non eseguito”, l’OdL passa in stato “non eseguito”. Dalla piattaforma l’utente ha la possibilità di portare in “Eseguito” gli OdL figli seguendo i passaggi di seguito descritti all’interno dell’applicazione “Monitoraggio OdL”.

Selezionare dalla vista elenco l’OdL aggregatore.

Accedere al tab “Avanzamento”.

Eseguire il passaggio di stato: l’utente può effettuare un cambio di stato singolo cliccando l’icona in prossimità del singolo figlio e salvando o un cambio stato massivo cliccando sul pulsante “Stato eseguito massivo” e salvando.

Figura: vista tab avanzamento OdL aggregatore con evidenza dei pulsanti per il cambio stato "Eseguito" puntuale e massivo

Caso misto

Figura14: flusso OdL caso misto

Nel caso di Titris misto, un OdL in fase di esecuzione può assumere i seguenti stati:

Eseguito parzialmente: tale stato viene impostato dal sistema all'inserimento del primo consuntivo relativamente ai Titris non UI su EAM o alla ricezione del primo consuntivo da Geocall relativamente ai Titris UI.

Eseguito: tale stato viene impostato manualmente dall'utente su EAM, seguendo i passaggi già descritti per il caso UI, quando è conclusa l'attività e non devono essere più inseriti consuntivi relativamente ai Titris non UI.

## 8.4 Consuntivazione ore in EAM e processo di storno

Nel presente capitolo, si descriverà il processo di consuntivazione ore e storno delle stesse. Nel caso di OdL con Titris 100% UI, le attività di consuntivazione vengono eseguite in Geocall e trasmesse poi in EAM prima dell’invio verso SAP. Nel caso di OdL con Titris misto le attività di consuntivazione delle risorse con Titris UI vengono ricevute da Geocall e quelle delle risorse non UI sono registrate direttamente dall’utente su EAM. Le attività di storno, parziale e totale, sono eseguite in EAM mediante un’integrazione con gli altri sistemi.

### 8.4.1 Monitoraggio dei consuntivi orari ricevuti da Geocall

L’utente ha la possibilità di monitorare sulla piattaforma i consuntivi orari ricevuti da Geocall in due modi:

a livello di singolo OdL nel tab “Avanzamento” dell’OdL;

accedendo all’applicazione “Consuntivazione risorse”.

Per monitorare i consuntivi orari in modo puntuale per un OdL l’utente deve seguire i passaggi di seguito descritti:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Monitoraggio OdL”. Altrimenti, può inserire nel campo di ricerca la dicitura “Monitoraggio OdL” per essere indirizzato all’applicazione desiderata.

Figura 15: Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL: dalla vista elenco, selezionare l’ordine di lavoro per il quale si intende effettuare l’operazione. È possibile ricercare l’OdL di interesse filtrando per i seguenti campi:

-Ordine di lavoro

-Descrizione OdL

-SAP ID

-Asset

-Descrizione asset

-Perimetro di intervento

-Stato

-Data inizio pianificata

-Data fine pianificata

-Data modello

-Responsabile attività

Accedere al tab avanzamento: accedendo al tab, l’utente può monitorare le ore consuntivate nella tabella “Risorse”. Per ogni risorsa viene indicato l’ID, il nome, data e orario di inizio e fine, le ore di esecuzione, trasferimento, ufficio e totali, il flag “Inviato a SAP?” ed eventuali storni in corso.

Figura16: vista tab "Avanzamento" con evidenza della tabella "Risorse"

L’utente ha la possibilità di monitorare i consuntivi orari anche mediante l’applicazione “Consuntivazione risorse” seguendo i passaggi di seguito descritti.

Accedere all’applicazione “Consuntivazione risorse”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Consuntivazione risorse”. Altrimenti, può inserire nel campo di ricerca la dicitura “Consuntivazione risorse” per essere indirizzato all’applicazione desiderata.

Figura 17: Lista di applicazioni di EAM, con evidenza all'applicazione "Consuntivazione risorse"

Eseguire le attività di monitoraggio: nella vista elenco sono riportate tutte le matricole di competenza dell’utente per le quali è stata ricevuta la consuntivazione oraria. L’utente ha la possibilità di filtrare per: -matricola -noma -competenza -ordine di lavoro -data di inizio -ore totali -costo risorsa -invio a SAP -esito invio -descrizione errore storno.

Figura18: vista elenco applicazione "Consuntivazione risorse"

## 8.5 Invio consuntivi orari a SAP (caso UI e misto)

Dopo la ricezione dei consuntivi da Geocall, si procede all’invio degli stessi verso SAP. La funzionalità di convalida dei consuntivi e invio verso SAP è abilitata agli utenti con profilo P1, P5, P6, P7, P8, P11, P15, P16, P17, secondo le autorizzazioni di sistema.

Figura 19: integrazioni con SAP

Per l’invio dei consuntivi orari verso SAP, l’utente è tenuto a seguire i passaggi di seguito descritti:

Accedere all’applicazione “Consuntivazione risorse”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Consuntivazione risorse”. Altrimenti, può inserire nel campo di ricerca la dicitura “Consuntivazione risorse” per essere indirizzato all’applicazione desiderata.

Figura:20: Lista di applicazioni di EAM, con evidenza all'applicazione "Consuntivazione risorse"

Selezionare le risorse: dalla vista elenco l’utente può selezionare puntualmente le singole risorse per le quali intende eseguire l’operazione di invio consuntivi verso SAP o spuntare la check box presente in testata per la selezione massiva delle stesse.

Figura21: vista elenco applicazione "Consuntivazione risorse" con evidenza delle check box per una selezione massiva o puntuale

Nota: è possibile filtrare per i campi presenti nella testata della tabella.

Effettuare l’invio a SAP: tramite apposita funzionalità presente nel menù laterale, si effettua l’invio verso SAP delle risorse selezionate.

Figura 22: vista elenco dell'applicazione "Consuntivazione risorse" con evidenza della funzionalità di invio verso SAP

Effettuando tale operazione, i consuntivi orari delle risorse selezionate saranno inviati verso SAP e nel tab “Avanzamento” dell’OdL verrà spuntato automaticamente il flag “Inviato a SAP”.

## 8.6 Processo di storno (caso UI e misto)

A seguito della consuntivazione delle ore, l’utente ha la possibilità di eseguire, se necessario, lo storno parziale o totale delle stesse. Lo storno può essere eseguito sia prima che dopo l’invio verso SAP dei consuntivi orari: nel primo caso il sistema interagirà solamente con Geocall, nel secondo caso anche con SAP. La funzionalità di storno è abilitata agli utenti con profilo P1, P5, P6, P7, P8, P,15, P16, P17, secondo le autorizzazioni di sistema.

### 8.6.1 Storno parziale (caso UI e misto)

Sia nel caso di OdL con Titris 100% UI e misto, per effettuare le operazioni di storno parziale di singoli consuntivi orari è necessario che il consuntivo sia stato già validato e che il tipo spesa sia OPEX.

In entrambi i casi, l’utente è tenuto a seguire le operazioni descritte di seguito per effettuare lo storno parziale di singoli consuntivi orari.

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Monitoraggio OdL”. Altrimenti, può inserire nel campo di ricerca la dicitura “Monitoraggio OdL” per essere indirizzato all’applicazione desiderata.

Figura23: Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL: dalla vista elenco, selezionare l’ordine di lavoro per il quale si intende effettuare l’operazione. È possibile ricercare l’OdL di interesse filtrando per i campi presenti in testata.

Accedere al tab “Avanzamento”

Effettuare l’operazione di storno parziale: all’interno della tabella “Risorse”, l’utente deve cliccare sul comando “Storno parziale” in corrispondenza della risorsa di interesse e salvare tramite apposita icona.

Figura24: vista del tab “Avanzamento” con evidenza della funzionalità di storno parziale

In caso di esito negativo dello storno, viene popolato il campo “Errore SAP”.

### 8.6.2 Storno totale (caso UI e misto)

Sia per il caso UI e misto, per poter effettuare lo storno totale dei consuntivi orari e fisici è necessario che il tipo spesa sia OPEX. L’utente è tenuto a seguire i passaggi di seguito descritti:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Monitoraggio OdL”. Altrimenti, può inserire nel campo di ricerca la dicitura “Monitoraggio OdL” per essere indirizzato all’applicazione desiderata.

Selezionare l’OdL: dalla vista elenco, selezionare l’ordine di lavoro per il quale si intende effettuare l’operazione. È possibile ricercare l’OdL di interesse filtrando per i campi presenti in testata.

Effettuare l’operazione di storno totale: tramite apposita funzionalità presente nel menù laterale, l’utente effettua lo storno totale.

Figura25: vista OdL con evidenza della funzionalità di storno totale

Per dare evidenza dello storno totale avvenuto, viene valorizzato automaticamente il campo “Storno totale effettuato” del tab “Ordine di lavoro” con il valore “T”.

Figura 26: vista tab “Ordine di Lavoro” con evidenza del campo “Storno totale” valorizzato con “T”

## 8.7 Processo di chiusura

### 8.7.1 Processo di chiusura OdL (semplice)

Il processo di chiusura di un OdL prevede il passaggio di stato in “Chiuso”. Tale cambio stato avviene automaticamente quando sono soddisfatte le seguenti condizioni:

Sono stati inviati a SAP tutti i consuntivi orari;

Sono state validate tutte le eventuali difformità rilevate con l’OdL;

È stato ricevuto il cambio stato in “chiuso” da Geocall e l’ordine è già in stato eseguito, oppure se è arrivato prima l’aggiornamento da Geocall relativo alla chiusura dell’ordine e l’utente imposta su EAM lo stato “Eseguito”.

### 8.7.2 Processo di chiusura a tre punte

Il processo di chiusura a tre punte riguarda solamente gli OdL con attività di tipo “Sostituzione”, “Demolizione” o “Nuova costruzione / installazione” e tipo spesa CAPEX ed è abilitato solo agli utenti di profilo P1, P5, P6, P7, P8, P,15, P16, P17, secondo le autorizzazioni di sistema.

L’utente è tenuto a seguire i passaggi descritti di seguito:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Monitoraggio OdL”. Altrimenti, può inserire nel campo di ricerca la dicitura “Monitoraggio OdL” per essere indirizzato all’applicazione desiderata.

Figura: Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL: dalla vista elenco, selezionare l’ordine di lavoro per il quale si intende effettuare l’operazione. È possibile ricercare l’OdL di interesse filtrando per i campi presenti in testata.

Accedere al tab “Conferme per chiusura”: il presente tab appare solo per gli OdL che rispettando le condizioni precedentemente descritte.

Figura: vista OdL con evidenza del tab "Conferme per chiusura"

Completare la chiusura: all’interno del tab “Conferme per chiusura” l’utente è tenuto a: -caricare e indicare l’avvenuto caricamento di moduli FVI, M04 e M06 a sistema ponendo il flag moduli caricati a “SI”; -indicare l’avvenuto caricamento di aggiornamenti anagrafici a sistema impostando il flag “Anagrafica aggiornata?” a “SI”; -verificare che il flag automatico “Conferma Ore da SAP?” sia su “SI”.

Il campo “matricola” viene valorizzato automaticamente dal sistema ma è possibile modificarlo manualmente dall’utente.

Figura: vista tab "Conferme per chiusura"

Salvare: l’utente è tenuto ad effettuare il salvataggio tramite apposita icona. L’OdL passerà automaticamente in stato “CHIUSO”.

Se non sono presenti consuntivi orari già inviati a SAP il processo di storno prosegue con lo storno orario in Geocall che può avere esito positivo o negativo. In caso di esito positivo su EAM l’ordine sarà portato in stato “aperto”, il campo “Storno totale” nel tab “Ordine di lavoro” sarà valorizzato con "S" (Success) e il campo “Storno totale Geocall” sarà valorizzato con “OK”. In caso invece di esito KO su Geocall, EAM acquisisce l’esito negativo dello storno in Geocall riportando “KO” nel campo “Storno totale Geocall”.

Se sono presenti consuntivi orari inviati a SAP, a seguito degli step precedentemente descritti, viene eseguita una simulazione di storno su SAP che può avere esito positivo o negativo. In caso di esito positivo della simulazione su SAP si procede con lo storno su Geocall. Se quest’ultimo ha esito “OK”, l’ordine sarà portato in stato “aperto”, il campo “Storno totale” sarà valorizzato con "S" e il campo “Storno totale Geocall” sarà valorizzato con “OK” altrimenti con “KO”. Se l’esito della simulazione SAP è KO su EAM viene restituito un messaggio di errore in cui viene riportato lo stato dell’OdM in SAP che ha causato il KO della richiesta. Sarà necessario effettuare un cambio di stato manuale dell’OdM in SAP per poi eseguire nuovamente lo storno in EAM seguendo i passaggi precedentemente descritti.

# 9. PROCESSO UI: ESECUZIONE E CONSUNTIVAZIONE RL

## 9.1 Introduzione

Gli OdL rappresentano le entità operative che raccolgono tutte le informazioni necessarie per pianificare, eseguire, tracciare e consuntivare gli interventi sugli asset di rete. La gestione degli OdL si articola in più fasi: generazione, pianificazione tecnica ed economica, esecuzione, consuntivazione e chiusura.

Di seguito verrà descritto nel dettaglio il processo di esecuzione degli OdL non UI all’interno del sistema EAM Web, fondamentale per garantire la gestione strutturata ed efficiente delle attività manutentive previste nell’ambito della Wave 2.

## 9.2 Esecuzione attività non UI (EAM)

Il processo di esecuzione degli OdL che si differenzia a seconda dei Titris associati in fase di pianificazione. In particolare, è possibile individuare le tre seguenti casistiche:

Titris 100% UI;

Titris 100% non UI;

Titris misti.

A seconda della casistica, gli stati, le integrazioni e le azioni utente previste sono differenti. Nei paragrafi successivi vengono descritti i dettagli per la tipologia Titris 100% non UI.

La gestione dei consuntivi avviene nel tab “Avanzamento” dell’OdL. Tale tab contiene la tabella “Risorse”, con le seguenti colonne:

| Nome campo | Descrizione | Modalità di valorizzazione | Obbligatorietà |  |
| --- | --- | --- | --- | --- |
| Risorse | Matricola risorsa | Pick-list | Obbligatorio |  |
| Nome | Nome e cognome risorsa | Popolato automaticamente | Obbligatorio |  |
| Inviato a SAP | Flag che indica se il consuntivo è stato inviato verso SAP | Popolato automaticamente | Obbligatorio |  |
| Check SAP | Esito dell’invio consuntivazione (OK/KO) | Popolato automaticamente | Facoltativo |  |
| Data inizio | Data di inizio esecuzione dei lavori da parte della risorsa | Popolato manualmente | Obbligatorio |  |
| Ora di inizio | Ora di inizio esecuzione dei lavori da parte della risorsa | Popolato manualmente | Obbligatorio |  |
| Data fine | Data di fine esecuzione dei lavori da parte della risorsa | Popolato manualmente | Obbligatorio |  |
| Ora di fine | Ora di fine esecuzione dei lavori da parte della risorsa | Popolato manualmente | Obbligatorio |  |
| Ore esecuzione | Tempo di esecuzione dei lavori in ore | Popolato automaticamente, ma sovrascrivibile | Obbligatorio |  |
| Ore trasferimento | Tempo per raggiungere il campo in ore | Popolato manualmente | Facoltativo |  |
| Ore in ufficio | Tempo passato in ufficio in ore | Popolato manualmente | Facoltativo |  |
| Ore totali | Ore esecuzione + ore trasferimento + ore in ufficio | Popolato automaticamente | Obbligatorio |  |
| Errore SAP | Descrizione dell’errore restituito da SAP nel caso in cui lo storno parziale vada KO | Popolato automaticamente | Facoltativo |  |
| Storno in corso | Valorizzato con la lettera “P” nel momento in cui la richiesta di storno parziale viene inoltrata a SAP | Popolato automaticamente | Facoltativo |  |
| Titris | Titolare della risorsa | Popolato automaticamente | Obbligatorio |  |

La registrazione puntuale e la convalida dei consuntivi possono essere effettuate dagli utenti appartenenti ai profili P1, P3, P5, P6, P16, P7, P8, P17, P11 e P14, seppur con alcune differenze basate sul profilo specifico:

P1, P3, P5, P11: possono gestire i consuntivi di tutte le risorse.

P6: può visualizzare e inviare a SAP, ma non modificare, solo i consuntivi delle risorse che hanno come Titris la propria UdB.

P16, P7, P8, P17: possono registrare e inviare i consuntivi di:

tutti gli OdL il cui responsabile attività è NORTN_IRP, NORTN_TSP o NORTN_SVP;

OdL della propria DT il cui responsabile attività è di tipo AT o RL;

risorse UI della propria UdB.

Il caricamento massivo dei consuntivi può essere effettuato dagli utenti appartenenti ai profili P1, P5, P6, P16, P7, P8, P17 e P15.

Lo storno può essere effettuato dagli utenti appartenenti ai profili P1, P5, P6, P16, P7, P8, P17 e P15, seppur con alcune differenze basate sul profilo specifico:

P1, P5, P15: possono gestire i consuntivi di tutte le risorse.

P6, P16, P7, P8, P17: possono gestire i consuntivi solo delle risorse che hanno come Titris la propria UdB.

### 9.2.1 Consuntivazione ore sul singolo OdL

Tramite una funzionalità custom, su EAM è possibile registrare i consuntivi sugli OdL. Per gli OdL non UI ciò è possibile senza il passaggio per Geocall; quindi, tutto il processo è gestito in EAM.

Il processo di registrazione dei consuntivi orari su un OdL non UI avviene nel seguente modo:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura -: Applicazione “Monitoraggio OdL”

Selezionare l’OdL che si vuole consuntivare: utilizzare i filtri di ricerca della vista elenco per trovare il record e cliccare sul codice OdL.

Figura: Selezione OdL

Nota: l’OdL dev’essere in stato “Eseguibile” o “Eseguito parzialmente” per poter ricevere consuntivi.

Accedere al tab “Avanzamento”: il tab contiene la tabella “Risorse” in cui è possibile aggiungere righe di consuntivo.

Figura: Selezione OdL

Aggiungere un record: cliccando il bottone “+” posizionato nell’angolo in alto a destra della tabella appare una nuova riga.

Figura: Tab "Avanzamento"

Nota: i record della tabella fanno riferimento solamente al lavoro svolto dalla risorsa in una giornata lavorativa. Sarà quindi l’utente a creare un record di consuntivazione della risorsa per ogni giornata lavorativa.

Popolare le colonne presenti: per poter salvare il consuntivo è necessario inserire informazioni come la matricola della risorsa e data e ora di inizio/fine.

Figura: Nuovo consuntivo

Salvare le modifiche effettuate: cliccare il bottone “salva” in alto a destra per salvare la consuntivazione.

Figura: Salvataggio consuntivo

### 9.2.2 Consuntivazione ore massiva

Nel caso non UI, l’utente ha la possibilità di effettuare il caricamento massivo dei consuntivi tramite file excel MxLoader.

Per caricare massivamente dei consuntivi è necessario seguire questi passaggi:

Accedere all’MxLoader: posizionarsi sul foglio “Config”, inserire la password e cliccare “Test connection”. In caso di successo appare un pop up che avvisa che l’accesso è andato a buon fine.

Figura: Configurazione Mx Loader

Posizionarsi sul foglio “X4WF-WO_LABTRANS”: il foglio contiene una colonna per ogni attributo del consuntivo.

Figura: Foglio "X4WF-WO_LABTRANS"

Nota: se non è presente, crearlo avviando la funzione “MxSheet Wizard” dal tab “MxLoader”. Una volta nel dialog, selezionare la struttura oggetto “X4WF-WO_LABTRANS”, aggiungere le colonne desiderate (es. SITEID, WONUM, LABTRANS.LABORCODE, LABTRANS.FINISHDATEENTERED, LABTRANS.FINISHTIMEENTERED, LABTRANS.STARTDATEENTERED, LABTRANS.STARTTIMEENTERED, LABTRANS.X3TTRASFERIMENTO, LABTRANS.X4ORETRASF, LABTRANS.X4OREUFF, LABTRANS.X4ORETOT) e confermare.

Impostare la modalità “AddChange”: questo permette di inserire nuovi consuntivi a sistema, su uno o più OdL.

Figura: Modalità "AddChange"

Nota: tutte le righe presenti nel foglio verranno inviate a EAM, quindi assicurarsi che esse comprendano solo i consuntivi che si intende caricare massivamente.

Popolare le colonne presenti: inserire le righe desiderate, specificando l’OdL, la matricola, le date e gli orari di inizio e fine e la ripartizione delle ore in ufficio e trasferimento.

Figura: Nuovi consuntivi

Avviare la funzione “Run”: cliccare il bottone presente nel tab “Mx Loader” in alto per eseguire il caricamento.

Figura - Avvio caricamento massivo

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura: Applicazione "Monitoraggio OdL"

Selezionare l’OdL su cui sono stati caricati i consuntivi: utilizzare i filtri di ricerca della vista elenco per trovare il record e cliccare sul codice OdL.

Figura Selezione OdL

Accedere al tab “Avanzamento”: il tab contiene la lista dei consuntivi registrati sull’OdL.

Figura Tab "Ordine di lavoro"

Verificare la presenza dei nuovi consuntivi: la tabella “Risorse” contiene una riga per ogni consuntivo inserito nell’MxLoader. Il caricamento è avvenuto in modo massivo.

Figura Tab "Avanzamento"

### 9.2.3 Invio consuntivi orari a SAP

Il processo di esecuzione di un OdL in EAM prevede l’invio dei consuntivi a SAP per la convalida delle ore lavorate.

Prima dell’invio delle consuntivazioni verso SAP potrebbe essere necessario modificare le informazioni di consuntivazione. Per gli OdL non UI, l’eventuale modifica dei dati di consuntivazione e degli oggetti contabili viene effettuata su EAM.

Per inviare i consuntivi verso SAP occorre eseguire i seguenti passaggi:

Accedere all’applicazione “Consuntivazione risorse”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Consuntivazione risorse”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Consuntivazione risorse”.

Figura: Applicazione "Consuntivazione risorse

Nota: il responsabile UdB può accedere alla maschera contenente tutte le risorse appartenenti alla propria UdB la cui consuntivazione deve essere ancora inviata verso SAP. Nel caso in cui il responsabile UdB volesse visualizzare tutte le risorse (quindi anche quelle la cui consuntivazione è già stata inviata verso SAP) è necessario cliccare su “Query disponibili” e successivamente cliccare su “Tutte le risorse”.

Selezionare i consuntivi da inviare a SAP: cliccare la checkbox in corrispondenza dei singoli record, o quella in testata per selezionare tutti quelli presenti in schermata ed effettuare un invio massivo.

Figura: Selezione consuntivi

Avviare la funzione “Invia a SAP”: il bottone si trova nel menu di sinistra. Affinché l’invio vada a buon fine, devono essere soddisfatte due condizioni:

Periodo contabile aperto: una volta cliccato “invia a SAP”, l’interfaccia verifica che il periodo contabile su SAP sia aperto;

Oggetto contabile aperto: una volta cliccato “invia a SAP”, l’interfaccia verifica che l’oggetto contabile, precedentemente inserito nel tab “Coin”, su SAP sia aperto.

Figura Invio a SAP

Nota: nel caso in cui almeno una delle due condizioni non sia soddisfatta, non sarà possibile inviare la consuntivazione delle risorse verso SAP. L’utente riceverà su EAM un messaggio di errore “Invio consuntivazione non riuscito”. Si specifica inoltre che in caso di consuntivazione su CBE non configurate come oggetto di contabilizzazione SAP risponderà con KO. Se invece l’invio va a buon fine, le righe scompaiono dalla tabella, perché quest’ultima è filtrata di default per i consuntivi non ancora inviati.

Cambiare il filtro della colonna “Inviato a SAP”: selezionare il valore “S” e premere “Invio” dalla tastiera per visualizzare i consuntivi inviati. Tra di essi compaiono anche quelli appena inviati.

Figura: Risultato invio

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura: Applicazione "Monitoraggio OdL"

Selezionare l’OdL di cui sono inviati i consuntivi: utilizzare i filtri di ricerca della vista elenco per trovare il record e cliccare sul codice OdL.

Figura: Selezione OdL

Accedere al tab “Avanzamento”: il tab contiene la tabella “Risorse” con i consuntivi registrati.

Figura: Tab "Ordine di lavoro"

Verificare il flag “Inviato a SAP”: una volta andato a buon fine l’invio verso SAP, nel tab “Avanzamento” dell’OdL viene spuntato automaticamente il flag “Inviato a SAP” in corrispondenza del consuntivo in questione.

Figura: Consuntivi inviati

Nota: per OdL con tipo spesa Opex, se prima dell’invio del consuntivo il campo “Identificativo OdM” del tab “Coin” non è valorizzato, allora l’invio del verso SAP genererà un nuovo OdM, popolando automaticamente il campo.

### 9.2.4 Storno ore parziale

L’applicazione EAM offre la possibilità di stornare i consuntivi registrati su un OdL. Se viene stornata solo una parte dell’effort dell’OdL, il processo prende il nome di storno parziale. È possibile effettuare lo storno anche su consuntivazioni afferenti a periodi contabili precedenti rispetto alla data di esecuzione dello storno.

Per stornare un consuntivo è necessario seguire questi passaggi:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura: Applicazione "Monitoraggio OdL"

Selezionare l’OdL su cui si vuole effettuare lo storno: utilizzare i filtri di ricerca della vista elenco per trovare il record e cliccare sul codice OdL.

Figura Selezione OdL

Accedere al tab “Avanzamento”: il tab contiene la lista dei consuntivi registrati sull’OdL.

Figura: Tab "Ordine di lavoro"

Avviare la funzione “Storno parziale” e salvare: il bottone si trova alla fine del record corrispondente alla risorsa che si vuole stornare, nella tabella “Risorse”.

Figura: Funzione "Storno parziale"

Nota: se il consuntivo non è ancora stato validato o il tipo spesa dell’OdL è diverso da OPEX, il sistema restituisce un messaggio di errore: “impossibile effettuare storno parziale, verificare che siano presenti consuntivi già validati e che la tipologia spesa sia OPEX”. D’altra parte, se l’OdM in SAP si trova in uno stato non idoneo allo storno, EAM restituisce un messaggio di errore in cui viene riportato lo stato dell’OdM in SAP. In questo caso l’utente deve manualmente lo stato dell’OdM in SAP ed eseguire nuovamente lo storno in EAM.

Verificare l’esito dello storno: una volta avviata la funzione, la colonna “Storno in corso” viene popolata dal sistema con “P”. A questo punto è necessario aggiornare la pagina per visualizzare l’esito. Se lo storno è andato a buon fine, la riga di consuntivo non è più presente in tabella; altrimenti la colonna “Storno in corso” si popola con “E” e la colonna “Errore SAP” mostra la motivazione dell’errore.

Figura: Esito storno

Nota: se sull’OdL è registrato un fisico e si prova a stornare più del 70% dell’effort, il sistema mostra un messaggio non bloccante: “attenzione si sta stornando più del 70% del totale”.

### 9.2.5 Passaggio in stato eseguito

Una volta che l’attività è stata completata e tutti i consuntivi sono stati registrati, l’OdL può essere portato in “Eseguito”.

Per avanzare un OdL allo stato “Eseguito”, occorre seguire i seguenti passaggi:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire direttamente nel campo di ricerca la voce “Monitoraggio OdL”.

Figura: Applicazione "Monitoraggio OdL"

Selezionare un OdL in stato “Eseguibile”: utilizzare i filtri di ricerca della vista elenco per trovare il record e cliccare sul codice OdL.

Figura: Selezione OdL

Accedere al tab “Avanzamento”: il tab contiene la tabella “Risorse” in cui è possibile aggiungere righe di consuntivo.

Figura: Tab “Ordine di lavoro”

Aggiungere un consuntivo: creare una nuova riga, popolare le colonne obbligatorie e salvare.

Figura: Registrazione consuntivo

Verificare lo stato dell’OdL: all’inserimento del primo consuntivo l’OdL passa in stato “Eseguito parzialmente”.

Figura Cambio stato "Eseguito parz."

Nota: da questo momento è possibile inviare i consuntivi parziali verso SAP ed è considerato il fisico parziale degli OdL sottostanti un aggregatore.

Avviare la funzione “Inoltra workflow”: cliccare il bottone in alto a destra quando tutti i consuntivi sono stati registrati e l'attività è stata eseguita.

Figura: Funzione "Inoltra a workflow"

Confermare il cambio stato: cliccare “OK” per confermare. Da questo momento l’OdL è in stato “Eseguito” e viene conteggiato il fisico totale.

Figura: Cambio stato "Eseguito”

# 10 INTERFACCIA CON TAGETIK E SAP

In alcuni casi, ai fini della corretta gestione dei processi coinvolti nel ciclo di vita degli OdL (ad esempio pianificazione tecnica ed economica, schedulazione, gestione dei consuntivi e registrazione di anomalie) è necessario stabilire dei collegamenti tra la piattaforma EAM e altri sistemi esterni. Nello specifico, il presente capitolo è dedicato a descrivere l’interfaccia tra il sistema di gestione di Asset EAM e i sistemi Tagetik e SAP, con focus sui seguenti processi necessari per la gestione ed esecuzione degli OdL.

Generazione delle tabelle di intersezione tecnica-patrimoniale (EAM / SAP)

Export scenari per caricamento in Tagetik (EAM / Tagetik)

Import dei costi di personale interno e risorse esterne in EAM (EAM / SAP)

## 10.1 Generazione tabella di intersezione tecnica-patrimoniale

Le tabelle di intersezione tecnica-patrimoniale servono a riportare i valori di variazione fisica degli elementi della rete, aggregati per CBS pianificati o consuntivati nell’anno. Per procedere alla generazione della tabella di intersezione tecnica-patrimoniale, l’utente deve attenersi ai seguenti passaggi:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire la voce “Monitoraggio OdL” direttamente nel campo di ricerca.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’azione “Intersezione tecnico patrimoniale”: dal menu laterale a sinistra, l’operatore deve selezionare l’azione “Intersezione tecnico patrimoniale”, per visualizzare le informazioni relative ai dati ancora in fase di pianificazione e i dati già consuntivati. Le tabelle sono scaricabili tramite l’icona evidenziata e sono composte dalle seguenti colonne:

Pianificazione

CBS Pianificata: parametro di aggregazione dei dati degli OdL presenti nella tabella

Indicatore pianificato: elemento della rete per cui è calcolato l’importo, costituito dalla concatenazione dei campi asset (classificazione asset EAM), livello di tensione e tipologia (attributo dell’asset aereo, terrestre, marino);

Unità di misura: unità di misura del fisico (viene ricavata dall’asset);

Import pianificato: valore fisico della variazione;

Mese pianificato: mese di riferimento degli interventi;

Anno pianificato: anno di riferimento degli interventi.

Consuntivazione

CBS Consuntivata: parametro di aggregazione dei dati degli OdL presenti nella tabella

Indicatore consuntivata: elemento della rete per cui è calcolato l’importo, costituito dalla concatenazione dei campi asset (classificazione asset EAM), livello di tensione e tipologia (attributo dell’asset aereo, terrestre, marino);

Unità di misura consuntivata: unità di misura del fisico (viene ricavata dall’asset);

Import consuntivato: valore fisico della variazione;

Mese consuntivato: mese di riferimento degli interventi;

Anno consuntivato: anno di riferimento degli interventi.

Figura - Vista iniziale dell'applicazione "Monitoraggio OdL", con evidenza all'azione "Intersezione tecnico patrimoniale"

Figura - Tabella di intersezione tecnico patrimoniale della pianificazione

Figura- Tabella di intersezione tecnico patrimoniale dei consuntivi

Tramite il tasto download è possibile scaricare la tabella generata. L’eventuale caricamento della tabella in SAP è manuale.

## 10.2 Import costi interni ed esterni in EAM

Al fine di verificare i costi relativi all’utilizzo di personale interno e di risorse esterne per la realizzazione dell’attività, la piattaforma EAM, tramite integrazione con SAP, è in grado di recuperare i costi delle risorse presenti nella tabella di staging di SAP. Nello specifico, la chiamata verso SAP può essere fatta indipendentemente dallo stato dell’OdL (possibile anche dopo la chiusura dell’ordine) e i costi vengono recuperati in base all’OdM o NTK associati all’OdL rispettando la seguente logica:

Nel caso di oggetto contabile OdM: i costi di personale esterno e interno sono sempre recuperabili, in quanto viene stabilita una corrispondenza 1:1 tra OdM e OdL

Nel caso di oggetto contabile NTK: i costi del personale interno sono sempre recuperabili, mentre quelli delle risorse esterne vengono imputati su operazioni che non sono riconducibili agli OdL e, pertanto, non sono visualizzabili.

Per accedere alla tabella dei costi del personale esterno ed interno a EAM, l’utente deve realizzare la seguente operazione:

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente “Monitoraggio OdL”. In alternativa, è possibile inserire la voce “Monitoraggio OdL” direttamente nel campo di ricerca.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL di interesse: nella vista elenco iniziale dell’applicazione “Monitoraggio OdL”, l’utente deve selezionare l’OdL per il quale vuole verificare i costi interni ed esterni associati. Se necessario, la vista è filtrabile selezionando l’icona

Figura - Vista elenco iniziale dell'applicazione "Monitoraggio OdL", con evidenza al filtro di ricerca

Accedere alla tabella: selezionando il tab “Avanzamento”, l’utente viene indirizzato alla sezione dell’OdL dove può verificare la tabella dei costi interni / esterni. La tabella è composta dalle seguenti colonne:

Esercizio: anno contabile di riferimento

Periodo: periodo contabile di riferimento

Attività: macroarea (attività) codice CoIn associato all’OdL

Centro di costo responsabile: Centro di costo responsabile

Descrizione CdC: Descrizione del centro di costo responsabile, recuperate da una tabella contenente il mapping Codice CdC – descrizione fornita fuori linea dal BSN e caricata manualmente a sistema

Cliente: Cliente associato all’oggetto contabile

Tipo oggetto contabile: Tipo di oggetto contabile (valorizzato con “ORD” per OdM e “WBS” per NTK)

Gruppo merci: Gruppo merci

Descrizione Gruppo merci: Descrizione del rispettivo gruppo merci recuperato dal catalogo contenente le risorse esterne censite a sistema

Tipo ordine: Tipo ordine

Risorsa interna / esterna: Codice risorse interna / esterna (nel caso di Risorse interne il codice sarà essere troncato al “/” in modo da visualizzare solo H_OPE / H_IMP / H_QUA)

TITRIS: Codice del Titris (da sostituire con descrizione Titris se disponibile, stiamo verificando per esserne certi)

Descrizione Titris: Descrizione del titolare risorse

Unità: unità di misura dell’importo

Conto costi esterni Tagetik: Conto di riferimento della voce di costo

Quantità: Quantità associata al costo

Importo: Importo economico del costo

Figura - Vista di dettaglio del Tab "Avanzamento", con evidenza al tab di costi interni / esterni

# 11 REPORTISTICA

## 11.1 Reportistica avanzamento Odl

La visualizzazione del report di avanzamento OdL, avviene attraverso i passaggi descritti di seguito:

Accedere all’applicazione “Monitoraggio OdL”

Accedere alla sezione “Esegui report”: all’interno dell’applicazione “Monitoraggio OdL” selezionare l’azione “Esegui report”

Figura 27 - Vista elenco iniziale dell'applicazione "Monitoraggio OdL", con evidenza all'azione "Esegui report"

Selezionare il report di interesse: nella dialog rappresentata, l’utente dovrà cliccare sul report dedicato all’avanzamento di OdL

Figura - Dialog di selezione del tipo di report da generare, con evidenza al report di interesse

Verificare e compilare i parametri di lancio del report

Figura - Parametri di lancio

Avviare la generazione del report: una volta impostati i parametri desiderati, è possibile cliccare sul tasto “Inoltra” per procedere con la generazione del report.

Figura – Dialog di definizione dei parametri di lancio del report da generare, con evidenza al tasto di avvio “Inoltra”

Visualizzare e analizzare i risultati generati: dopo aver inserito i parametri richiesti, il sistema fornisce una tabella con la struttura del report e le informazioni associate.

Figura– Report di avanzamento di OdL con informazioni di pianificazione risorse esterne ed interne

Scaricare il report: qualora necessario, l’utente può utilizzare l’apposita funzione evidenziata per esportare il report generato nel formato più adeguato. Cliccando sulla funzione evidenziata nella barra superiore del report, il sistema apre la dialog di definizione delle condizioni dell’export. L’utente deve selezionare il formato desiderato selezionando la freccia messa in evidenza e poi selezionare “Ok” per eseguire il download del report.

Figura – Report di avanzamento di OdL con informazioni di pianificazione risorse esterne ed interne, con evidenza alle azioni necessarie per l’estrazione del report

## 11.2 Reportistica avanzamento OdL con informazioni di pianificazione risorse esterne ed interne

La visualizzazione del report di avanzamento degli OdL pianificati con informazioni delle risorse esterne e interne, avviene attraverso i passaggi descritti di seguito:

Accedere all’applicazione “Monitoraggio OdL”

Accedere alla sezione “Esegui report”: all’interno dell’applicazione “Monitoraggio OdL” selezionare l’azione “Esegui report”

Figura 28 - Vista elenco iniziale dell'applicazione "Monitoraggio OdL", con evidenza all'azione "Esegui report"

Selezionare il report di interesse: nella dialog rappresentata, l’utente dovrà cliccare sul report dedicato all’avanzamento di OdL con informazioni di pianificazione risorse esterne ed interne.

Figura - Dialog di selezione del tipo di report da generare, con evidenza al report di interesse

Verificare e compilare i parametri di lancio del report

Figura - Parametri di lancio

Avviare la generazione del report: una volta impostati i parametri desiderati, è possibile cliccare sul tasto “Inoltra” per procedere con la generazione del report.

Figura – Dialog di definizione dei parametri di lancio del report da generare, con evidenza al tasto di avvio “Inoltra”

Visualizzare e analizzare i risultati generati: dopo aver inserito i parametri richiesti, il sistema fornisce una tabella con la struttura del report e le informazioni associate.

Figura– Report di avanzamento di OdL con informazioni di pianificazione risorse esterne ed interne

Scaricare il report: qualora necessario, l’utente può utilizzare l’apposita funzione evidenziata per esportare il report generato nel formato più adeguato. Cliccando sulla funzione evidenziata nella barra superiore del report, il sistema apre la dialog di definizione delle condizioni dell’export. L’utente deve selezionare il formato desiderato selezionando la freccia messa in evidenza e poi selezionare “Ok” per eseguire il download del report.

Figura – Report di avanzamento di OdL con informazioni di pianificazione risorse esterne ed interne, con evidenza alle azioni necessarie per l’estrazione del report

## 11.3 Report consuntivazione risorse

Selezionare il report di interesse: nella dialog rappresentata, l’utente dovrà cliccare sul report dedicato alla consuntivazione delle ore e fisico delle risorse. Il sistema apre la pagina contenente i parametri di lancio.

Figura– Dialog di selezione del tipo di report da generare, con evidenza al report di interesse

Verificare e compilare i parametri di lancio: dopo aver eseguito lo step precedente, l’utente deve valorizzare i campi presenti nella dialog rappresentata con i seguenti parametri di lancio:

Divisione responsabile attività

UdB responsabile attività

Inquadramento risorsa

Codice OdL

Divisione responsabile risorsa (Titris)

UdB responsabile risorsa (Titris)

Tipologia attività PdL

CoIn

Classificazione asset

Mese consuntivazione risorsa

Anno di consuntivazione risorsa

Tipo spesa

Stato OdL

Flag PMI

Avviare la generazione del report: una volta impostati i parametri desiderati, è possibile cliccare sul tasto “Inoltra” per procedere con la generazione del report.

Figura 29 – Dialog di definizione dei parametri di lancio del report da generare, con evidenza al tasto di avvio “Inoltra”

Visualizzare e analizzare i risultati generati: dopo aver inserito i parametri richiesti, il sistema fornisce una tabella con la struttura del report e le informazioni richieste

Figura - Report di avanzamento di OdL con informazioni di pianificazione risorse esterne ed interne

Scaricare il report: qualora necessario, l’utente può utilizzare l’apposita funzione per esportare il report generato nel formato più adeguato. Cliccando sulla funzione evidenziata nella barra superiore del report, il sistema apre la dialog di definizione delle condizioni dell’export. L’utente deve selezionare il formato desiderato selezionando la freccia messa in evidenza e poi selezionare “Ok” per iniziare lo scarico del report.

Figura– Report di avanzamento di OdL con informazioni di pianificazione risorse esterne ed interne, con evidenza alle azioni necessarie per l’estrazione del report

## 11.4 Reportistica di difformità rilevate / risolte

Il report di difformità rilevate/risolte, fruibile da web, offre una vista sulle difformità attualmente rilevate e/o risolte sugli asset presenti a sistema consentendo le attività di monitoraggio delle stesse. Per poter eseguire tale report, l’utente di EAM deve seguire la procedura descritta di seguito.

Accedere all’applicazione “Difformità rilevate”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Difformità rilevate”. Altrimenti, può inserire nel campo di ricerca la dicitura “Difformità rilevate” per essere indirizzato all’applicazione desiderata.

Figura: Lista di applicazioni di EAM, con evidenza all'applicazione "Difformità rilevate"

Cliccare su “Esegui report”: l’utente deve cliccare sull’opzione “Esegui report” presente sul menù di sinistra

Figura - Vista iniziale dell’applicazione “Difformità rilevate”, con evidenza della funzionalità “Esegui report”.

Cliccare su “Difformità rilevate/risolte”: eseguita l’operazione descritta nello step precedente, si aprirà la dialog “Report e Programmazioni”. L’utente deve cliccare sull’opzione “Difformità rilevate/risolte” presente nel tab “Report” della dialog.

Figura - Dialog per la selezione del report da eseguire.

Compilare i campi obbligatori e facoltativi: eseguito il precedente step, si aprirà la pagina di richiesta suddivisa in 4 sezioni: parametri, programmazione, e-mail, opzioni di formato di programmazione. L’utente è tenuto a compilare, per ogni sezione, i campi obbligatori contrassegnati da. È a discrezione dell’utente la compilazione dei restanti campi facoltativi.

Figura: Dialog relativa ai parametri di lancio del report Difformità rilevate/risolte con evidenza dei campi obbligatori

Figura: Dialog relativa ai parametri di lancio del report Difformità rilevate/risolte con evidenza dei campi obbligatori

Nota: per tutti gli attributi della sezione “Parametri” della dialog relativa ai parametri di lancio è possibile effettuare una selezione multipla.

Cliccare “Inoltra”: cliccando “Inoltra”, l’utente verrà indirizzato su una nuova pagina web dove avrà la possibilità di visionare il report appena creato.

Figura: esempio report Difformità rilevate/risolte

(Step facoltativo) Download dati del report: l’utente ha la possibilità di esportare i dati del report o il report stesso cliccando, rispettivamente, sulle due icone evidenziate in rosso. È possibile anche stampare il report cliccando sull’icona evidenziata in verde.

Figura - esempio report Difformità rilevate/risolte con dettaglio sulle icone di stampa, esportazione dati ed esportazione report

Nota: durante le operazioni di esportazione dei dati è possibile scegliere se effettuare un’esportazione massiva dei dati o selezionare solo le colonne di interesse attraverso l’apposita dialog.

## 11.5 Reportistica di interferenze rilevate / risolte

Il report di interferenze rilevate/risolte, fruibile da web, offre una vista sulle interferenze attualmente rilevate e/o risolte sugli asset presenti a sistema consentendo le attività di monitoraggio delle stesse. Per poter eseguire tale report, l’utente di EAM deve seguire la procedura descritta di seguito:

Accedere all’applicazione “Interferenze rilevate”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema, suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Interferenze rilevate”. Altrimenti, può inserire nel campo di ricerca la dicitura “Interferenze rilevate” per essere indirizzato all’applicazione desiderata.

Figura - Lista di applicazioni di EAM, con evidenza all'applicazione "Interferenze rilevate"

Cliccare su “Esegui report”: l’utente deve cliccare sull’opzione “Esegui report” presente sul menù di sinistra

Figura: Vista iniziale dell’applicazione “Interferenze rilevate”, con evidenza della funzionalità “Esegui report”

Cliccare su “Interferenze rilevate/risolte”: eseguita l’operazione descritta nello step precedente, si aprirà la dialog “Report e Programmazioni”. L’utente deve cliccare sull’opzione “Interferenze rilevate/risolte” presente nel tab “Report” della dialog.

Figura: Dialog per la selezione del report da eseguire

Compilare i campi obbligatori e facoltativi: eseguito il precedente step, si aprirà la pagina di richiesta suddivisa in 4 sezioni: parametri, programmazione, e-mail, opzioni di formato di programmazione. L’utente è tenuto a compilare, per ogni sezione, i campi obbligatori contrassegnati da “*”. È a discrezione dell’utente la compilazione dei restanti campi facoltativi.

Figura: Dialog relativa ai parametri di lancio del report Interferenze rilevate/risolte con evidenza dei campi obbligatori

Figura: Dialog relativa ai parametri di lancio del report Interferenze rilevate/risolte con evidenza dei campi obbligatori

Nota: per tutti gli attributi della sezione “Parametri” della dialog relativa ai parametri di lancio è possibile effettuare una selezione multipla.

Figura -: esempio selezione multipla dialog DT responsabile asset

Cliccare “Inoltra”: cliccando “Inoltra”, l’utente verrà indirizzato su una nuova pagina web dove avrà la possibilità di visionare il report appena creato.

Figura -: esempio report interferenze rilevate/risolte

(Step facoltativo) Download dati del report: l’utente ha la possibilità di esportare i dati del report o il report stesso cliccando, rispettivamente, sulle due icone evidenziate in rosso. È possibile anche stampare il report cliccando sull’icona evidenziata in verde.

Figura: esempio report Interferenze rilevate/risolte con dettaglio sulle icone di stampa, esportazione dati ed esportazione report

Nota: durante le operazioni di esportazione dei dati è possibile scegliere se effettuare un’esportazione massiva dei dati o selezionare solo le colonne di interesse attraverso apposita dialog.

## 11.6 Reportistica delle misure

Per monitorare le misure attualmente presenti a sistema associate ad asset, l’utente ha la possibilità di generare il relativo report seguendo i passaggi descritti di seguito.

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Monitoraggio OdL”. Altrimenti, può inserire nel campo di ricerca la dicitura “Monitoraggio OdL” per essere indirizzato all’applicazione desiderata.

Figura: Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Cliccare su “Esegui report”: l’utente deve cliccare sull’opzione “Esegui report” presente sul menù di sinistra

Figura: Vista iniziale dell’applicazione “Monitoraggio OdL”, con evidenza della funzionalità “Esegui report”

Cliccare su “Misure”: eseguita l’operazione descritta nello step precedente, si aprirà la dialog “Report e Programmazioni”. L’utente deve cliccare sull’opzione “Misure” presente nel tab “Report” della dialog.

Figura: Dialog per la selezione del report da eseguire

Compilare i campi obbligatori e facoltativi: eseguito il precedente step, si aprirà la pagina di richiesta suddivisa in 4 sezioni: parametri, programmazione, e-mail, opzioni di formato di programmazione. L’utente è tenuto a compilare, per ogni sezione, i campi obbligatori contrassegnati da “*”. È a discrezione dell’utente la compilazione dei restanti campi facoltativi.

Figura: Dialog relativa ai parametri di lancio del report Misure con evidenza dei campi obbligatori

Figura: Dialog relativa ai parametri di lancio del report Misure con evidenza dei campi obbligatori

Nota: per tutti gli attributi della sezione “Parametri” della dialog relativa ai parametri di lancio è possibile effettuare una selezione multipla.

Cliccare “Inoltra”: cliccando “Inoltra”, l’utente verrà indirizzato su una nuova pagina web dove avrà la possibilità di visionare il report appena creato.

Figura: esempio report misure

(Step facoltativo) Download dati del report: l’utente ha la possibilità di esportare i dati del report o il report stesso cliccando, rispettivamente, sulle due icone evidenziate in rosso. È possibile anche stampare il report cliccando sull’icona evidenziata in verde..

Figura: esempio report misure con dettaglio sulle icone di stampa, esportazione dati ed esportazione report

Nota: durante le operazioni di esportazione dei dati è possibile scegliere se effettuare un’esportazione massiva dei dati o selezionare solo le colonne di interesse attraverso apposita dialog.

## 11.7 Monitoraggio difformità, interferenze, misure e micro-controlli sul singolo OdL

L’utente ha la possibilità di monitorare le difformità e interferenze validate e da validare, le difformità, interferenze e misure rilevate con un OdL e i micro-controlli associati a livello di singolo OdL seguendo i passaggi di seguito descritti.

Accedere all’applicazione “Monitoraggio OdL”: posizionando il cursore sopra la barra laterale sinistra, EAM mostra l’elenco delle applicazioni presenti a sistema suddivise tra diverse tipologie. L’utente deve selezionare l’opzione “Ordini di lavoro” e successivamente l’opzione “Monitoraggio OdL”. Altrimenti, può inserire nel campo di ricerca la dicitura “Monitoraggio OdL” per essere indirizzato all’applicazione desiderata.

Figura: Lista di applicazioni di EAM, con evidenza all'applicazione "Monitoraggio OdL"

Selezionare l’OdL: dalla vista elenco, selezionare l’ordine di lavoro per il quale si intende monitorare le difformità, interferenze, misure e micro-controlli associati. È possibile ricercare l’OdL di interesse filtrando per i seguenti campi:

Ordine di lavoro

Descrizione OdL

SAP ID

Asset

Descrizione asset

Perimetro di intervento

Stato

Data inizio pianificata

Data fine pianificata

Data modello

Responsabile attività

Figura: vista elenco applicazione "Monitoraggio OdL" con evidenza dell’azione per la selezione di un OdL

Accedere ai tab di interesse: all’interno del singolo ordine di lavoro l’utente, oltre a consultare gli attributi di caratterizzazione dell’OdL in esame e le informazioni di processo, ha la possibilità di monitorare lo stato delle difformità, interferenze, misure e micro-controlli associati accedendo agli appositi tab “Avanzamento” e “Difformità e interferenze”

Figura - scheda di un OdL con evidenza dei tab "Avanzamento" e "Difformità e interferenze"

Accedere al tab “Avanzamento”: all’interno del tab “Avanzamento” l’utente può consultare la tabella “Micro-controlli” e monitorare lo stato attuale dei micro-controlli associati all’OdL. All’interno della tabella, per ogni micro-controllo, è indicato l’asset oggetto di micro-controllo con la sua descrizione e SAP ID, l’ID micro-controllo con relativa descrizione e lo stato attuale del micro-controllo.

Figura - vista generale del tab "Avanzamento" con evidenza della tabella "Microcontrolli"

Accedere al tab “Difformità e interferenze”: accedendo al tab, l’utente ha la possibilità di monitorare le difformità e interferenze validate e da validare e le difformità, interferenze e misure rilevate con l’OdL attraverso le relative tabelle

Figura - vista del tab "Difformità e interferenze" con evidenza delle tabelle: - Difformità rilevate con il presente OdL; - Interferenze rilevate con il presente OdL; - Misure rilevate con il presente OdL.

Figura: vista del tab "Difformità e interferenze" con evidenza delle tabelle: -Difformità validate e da validare rilevate sull'asset; -Interferenze validate e da validare rilevate sull'asset

# 12 MXLOADER

## 12.1 Configurazione MXLoader

Per poter utilizzare Mxloader è necessario accedere al file e verificare la connessione. In particolare, all’interno del foglio 'Config' di ogni MXLoader, inserire l’ApiKey (la chiave alfanumerica fornita all’utente abilitato) nel campo 'Password' e connettere il tool a EAM cliccando sul tasto 'Test Connection'.

Figura: inserimento password MXLoader

## 12.2 MXLoader modifica date di pianificazione, dati contabili, flag pmi, dati odl

Lo strumento MXLoader consente di effettuare modifiche massive sugli Ordini di Lavoro (OdL), intervenendo sui principali dati dell’ordine di lavoro, in particolare: date di pianificazione dell’ordine, dati contabili (coin, cbs, cbe), flag pmi e i principali dati dell’odl (flag outsoutcing, assistenza/sorveglianza, cliente, pdl, responsabile attività, durata dell’ordine di lavoro). Di seguito la procedura da seguire per l’utilizzo:

Creazione query: Nel tab "CreazioneQuery" si può scegliere se effettuare l'estrazione utilizzando una lista di OdL, da inserire in colonna A, oppure i parametri (tutti obbligatori tranne il flag PMI) da valorizzare in colonna F. In funzione di questi input, la query di estrazione verrà generata automaticamente nello sheet "Estrazione", nella cella D1. Se sono presenti gli OdL in colonna A, la query sarà generata utilizzando gli OdL. Se si cancella la colonna A ovvero non saranno inseriti OdL (oppure se ne saranno inseriti un numero non consentito dai limiti della formula), la query sarà generata con i parametri valorizzati in colonna F

Estrazione: Nello sheet "Estrazione+modifica", cella D1, controllare che la formula sia presente. Se riporta invece il valore #N/A si possono essere verificare le seguenti circostanze:

- Sono stati inseriti troppi OdL in colonna A: diminuire il numero di OdL oppure utilizzare i parametri alternativi di colonna F

- Si stanno utilizzando i parametri di colonna F ma almeno uno dei parametri (al di fuori del flag PMI che è opzionale) non è stato valorizzato: valorizzare tutti i parametri con un solo valore per campo

- Si stanno utilizzando i parametri di colonna F, sono tutti valorizzati, ma la differenza tra le date TARGSTART_max e TARGSTART_min eccede 1 anno: riportare l'intervallo di estrazione al massimo ad 1 anno.

Infine eseguire nel TAB "Estrazione+modifica" il run dell'interrogazione per estrarre i valori degli OdL da aggiornare, attraverso la funzione RUN contenuta nel menù MxLoader.

Modifica: Una volta estratti i dati, apportare - nel tab "Estrazione+modifica" - le modifiche desiderate

Copiare le informazioni nel tab “Caricamento”: Una volta apportate le modifiche desiderate nel tab "Estrazione+modifica", copiare le informazioni nel TAB "Caricamento", facendo attenzione a inserire le informazioni nelle colonne corrette necessarie

Caricare i dati modificati: Dopo aver copiato i dati nel TAB "Caricamento", eseguire il run attraverso la funzione RUN contenuta nel menù MxLoader (vedi punto 2)

Modifica date di pianificazione dell’ordine padre: Se sono state modificate le date di pianificazione degli ordini figli, è necessario allineare le date degli ordini padri tramite il tab "CARICA_PADRI", come di seguito riportato:

Accedere al tab "CARICA_PADRI": Il tab risulterà già precompilato con gli ordini padri estratti nel tab "Estrazione+caricamento". La data di inizio sarà impostata pari alla data di inizio minima dei figli, mentre la data di fine pari alla data di fine massima dei figli

Eseguire la funzione RUN presente nel menu MXLoader (fare riferimento al punto 2).

In fase di RUN delle righe compilate nella tab “Caricamento”, si segnala che la presenza di una riga vuota tra righe valorizzate comporta l’interruzione del caricamento: l’Excel si fermerà alla prima riga vuota individuata, senza proseguire con le successive righe compilate.

I campi presenti in questo file hanno il seguente significato:

IDCLUSTER: Campo libero e facoltativo in cui poter inserire un identificativo del caricamento eseguito al fine di agevolare le verifiche da effettuare a GUI sull'applicazione "Caricamenti MXLoader"

WONUM: ID dell'ordine di lavoro (corrisponde al campo "Ordine di lavoro")

SITEID: Campo popolato staticamente da non modificare; sempre uguale a SIT01

ESTDUR: Durata dell'Ordine di lavoro (corrisponde al campo "Durata Pianificata" presente nel tab Date dell'OdL). Nel campo sarà possibile inserire una durata nel formato HH:MM oppure un numero intero positivo

DESCRIPTION: Presente nel tab "Estrazione+modifica", Il campo riporta la Descrizione dell'OdL estratto

WOEQ9: SAP ID Asset. Presente nel tab "Estrazione+modfica", riporta il valore del SAP ID dell'asset oggetto dell'OdL estratto

ENVIRONMENT: Descrizione asset 1° lvl. Presente nel tab "Estrazione", riporta la descrizione dell'Asset di 1° lvl cui appartiene l'asset oggetto dell'OdL estratto

ASSET.DESCRIPTION: Descrizione asset 3° lvl. Presente nel tab "Estrazione", riporta la descrizione dell'Asset di 3° lvl padre dell'asset oggetto dell'OdL estratto

X4BURKS: Campo "Cliente" dell'Ordine di lavoro. Nel campo sarà possibile inserire i seguenti valori: TR01, TR26, TR30, TR36 o TR64

X4OUTSOURCING: Corrisponde al flag "Outsourcing?" presente sull'Ordine di lavoro. Può assumere solo i seguenti valori:

Per attivarlo sarà possibile inserire: 1, TRUE, true, Y, y

Per disattivarlo sarà possibile inserire: 0, FALSE, false, N, n

X4NATURALAVORO: Campo "Assistenza/Sorveglianza" dell'Ordine di lavoro. Il campo deve essere obbligatoriamente valorizzato nel caso sia attivato il campo X4OUTSOURCING. I valori ammessi sono i seguenti e devono essere inseriti in maiuscolo: ASSISTENZA/SORVEGLIANZA

X4NOTE: Campo "Note" dell'Ordine di lavoro. Qualora si intenda rimuovere il valore attualmente presente in tale campo, riportandolo a vuoto, sarà necessario inserire il valore ~NULL~

JPNUM: Identificativo PdL. Riporta l'ID del PdL cui è legato l'OdL estratto. Il campo è modificabile inserendo un OdL attivo diverso da quello di partenza, purchè sia riferito alla stessa ID Macro-attività. Si specifica che per gli ordini figli contenuti all'interno di un aggregatore non è possibile modificare il JPNUM. Pertanto per modificare il JPNUM sia dell'aggregatore sia dei relativi figli, procedere come segue:

Modificare JPNUM sugli ordini padri: Per individuare gli ordini padri, fare riferimento alla colonna PARENT del tab "Estrazione+modifica" oppure alla colonna WONUM del tab "CARICA_PADRI" (prepopolata come descritto nello step 6). Si specifica che la modifica del JPNUM sul padre comporta automaticamente lo sgancio dei figli

Modificare JPNUM sugli ordini figli: Una volta avvenuto lo sgancio automatico dal padre, è possibile aggiornare il JPNUM dei singoli ordini figli, impostando lo stesso valore inserito sul padre.

Riassociare i figli al padre tramite GUI

X4TIPOSPESA: Tipo spesa dell'ordine di lavoro. Nel campo sarà possibile inserire solo i seguenti valori da riportare sempre in maiuscolo: OPEX, CAPEX, ANR

X4COIN: ID della CoIn (corrisponde al campo "Codice Coin" presente sull'Ordine di lavoro). Sarà possibile inserire valori coerenti con il tipo spesa selezionato e che siano previsti nell'attività standard presente sull'Ordine di lavoro modificato. Si specifica che nel caso di ordini aggregatori la modifica va effettuata sull'ordine padre. La CoIn impostata sul padre verrà propagata automaticamente a tutti gli ordini figli.

Eventuali modifiche della coin sugli ordini figli non bloccheranno il caricamento, tuttavia non avranno alcun effetto in quanto il campo coin - per gli ordini figli - non è modificabile

X4WBS: ID della CBS dell'ordine di lavoro (corrisponde al campo "CBS contabile" presente sull'Ordine di lavoro). Il campo è popolabile solo nei casi CAPEX o ANR con scarico costi a CBS. Qualora si intenda rimuovere il valore attualmente presente in tale campo, riportandolo a vuoto, sarà necessario inserire il valore ~NULL~

X4WBE: ID della CBE dell'ordine di lavoro (corrisponde al campo "CBE contabile" presente sull'Ordine di lavoro). Il campo è popolabile solo nei casi CAPEX o ANR con scarico costi a CBS. Qualora si intenda rimuovere il valore attualmente presente in tale campo, riportandolo a vuoto, sarà necessario inserire il valore ~NULL~

OWNERGROUP: Responsabile attività dell'OdL. Il campo è modificabile inserendo uno dei valori possibili a sistema

X4PMI: Corrisponde al flag "PMI?" presente sull'Ordine di lavoro. Può assumere solo i seguenti valori:

Per attivarlo sarà possibile inserire: 1, TRUE, true, Y, y

Per disattivarlo sarà possibile inserire: 0, FALSE, false, N, n

TARGSTARTDATE: Campo "Data pianificata inizio" dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato GG/MM/AAAA HH:MM:SS. Nel campo sarà possibile inserire solo un valore che sia:

maggiore della data inserita in "Data non prima di" e inferiore alla data inserita in "Data pianificata fine"

TARGCOMPADATE: Campo "Data pianificata fine" dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato GG/MM/AAAA HH:MM:SS. Nel campo sarà possibile inserire solo un valore che sia: maggiore della data inserita in "Data pianificata inizio" e inferiore alla data inserita in "Data di massimo posticipo"

SNECONSTRAINT: Campo "Data non prima di" dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato GG/MM/AAAA HH:MM:SS. Nel campo sarà possibile inserire solo un valore che sia inferiore alla data inserita in "Data pianificata inizio"

FNLCONSTRAINT: Campo "Data di massimo posticipo" dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato GG/MM/AAAA HH:MM:SS. Nel campo sarà possibile inserire solo un valore che sia maggiore della data inserita in "Data pianificata fine"

PARENT: Ordine padre dell'ordine di lavoro estratto. Campo presente nel tab "Estrazione"; non modificare/eliminare questo campo

X4AGGREGATI: Campo che indica se l'ordine di lavoro è un singolo/figlio (valore uguale a 0) oppure un padre (valore uguale a 1). Campo presente nel tab "Estrazione"; non modificare/eliminare questo campo

L’MXLoader descritto integra in un’unica soluzione la modifica di quattro tipologie di informazioni: date ODL, dati ODL, flag PMI e COIN. Si precisa che i singoli MXLoader dedicati a ciascuna di queste tipologie restano comunque utilizzabili e sono soggetti alle medesime regole operative descritte in questo paragrafo.

## 12.3 MXLoader modifica Risorse esterne pianificate nell’OdL

La strumento MXLoader consente di aggiornare massivamente i dati delle risorse esterne associate agli Ordini di Lavoro (OdL), permettendo in particolare di intervenire sui parametri di pianificazione e garantendo l’allineamento delle informazioni presenti a sistema.

A seguito della modifica delle date degli Ordini di Lavoro tramite l’MXLoader descritto nel paragrafo 12.2, è necessario procedere con l’allineamento delle date delle risorse esterne utilizzando l’MXLoader illustrato nel presente paragrafo. Di seguito la procedura da seguire per l’utilizzo:

Creazione query: Nel tab "CreazioneQuery" nella colonna A va inserito l'elenco degli OdL di interesse. Nella cella B1 è presente la formula per generare la query che contenga tutti gli OdL inseriti nella colonna A. La query estratta dovrà essere copiata nella cella D1 del TAB "Estrazione", all'interno delle parentesi quadre ([...])

Inserimento query nel tab “Estrazione”: Inserire la query costruita nel tab "CreazioneQuery" nella cella D1 del TAB "Estrazione", all'interno delle parentesi quadre ([...]). Attenzione che alla fine dell'elenco non vi sia la virgola. Es: wonum IN ["931368","931384","931781",…]

Estrazione: Eseguire nel TAB "Estrazione" il run dell'interrogazione per estrarre i valori degli OdL da aggiornare, attraverso la funzione RUN contenuta nel menù MxLoader

Copiare le informazioni nel tab caricamento: Copiare le informazioni riportate nel TAB "Caricamento", facendo attenzione a inserire nelle colonne corrette

Apportare modifica ed eseguire caricamento: Dopo aver apportato le modifiche desiderate eseguire nel TAB "Caricamento" il run attraverso la funzione RUN contenuta nel menù MxLoader (vedi punto 3)

In fase di RUN delle righe compilate nella tab “Caricamento”, si segnala che la presenza di una riga vuota tra righe valorizzate comporta l’interruzione del caricamento: l’Excel si fermerà alla prima riga vuota individuata, senza proseguire con le successive righe compilate.

I campi presenti in questo file hanno il seguente significato:

IDCLUSTER: Campo libero e facoltativo in cui poter inserire un identificativo del caricamento eseguito al fine di agevolare le verifiche da effettuare a GUI sull'applicazione "Caricamenti MXLoader"

WONUM: ID dell'ordine di lavoro (corrisponde al campo "Ordine di lavoro"

SITEID: Campo popolato staticamente da non modificare; sempre uguale a SIT01

DESCRIPTION: Presente nel tab "Estrazione", Il campo riporta la Descrizione dell'OdL estratto

WOEQ9: SAP ID Asset. Presente nel tab "Estrazione", riporta il valore del SAP ID dell'asset oggetto dell'OdL estratto

ENVIRONMENT: Descrizione asset 1° lvl. Presente nel tab "Estrazione", riporta la descrizione dell'Asset di 1° lvl cui appartiene l'asset oggetto dell'OdL estratto

ASSET.DESCRIPTION: Descrizione asset 3° lvl. Presente nel tab "Estrazione", riporta la descrizione dell'Asset di 3° lvl padre dell'asset oggetto dell'OdL estratto

TARGSTARTDATE: Campo "Data pianificata inizio" dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato AAAA/MM/GG HH:MM:SS. Nel campo sarà possibile inserire solo un valore che sia:

maggiore della data inserita in "Data non prima di"

inferiore alla data inserita in "Data pianificata fine"

Si segnala che il dato è riportato a supporto della compilazione delle informazioni di pianificazione, essendo disponibile un MXLoader dedicato per la modifica delle date di pianificazione dell’Ordine di Lavoro

TARGCOMPDATE: Campo "Data pianificata fine" dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato AAAA/MM/GG HH:MM:SS. Nel campo sarà possibile inserire solo un valore che sia:

maggiore della data inserita in "Data pianificata inizio"

inferiore alla data inserita in "Data di massimo posticipo"

Si segnala che il dato è riportato a supporto della compilazione delle informazioni di pianificazione, essendo disponibile un MXLoader dedicato per la modifica delle date di pianificazione dell’Ordine di Lavoro

WPSERVICE.WPITEMID: ID identificativo della risorse esterna/ricavo estratta. Si segnala che:

se si intende andare in modifica della risorsa esterna/ricavo già presente a sistema il campo non deve essere modificato/cancellato

per procedere con l'aggiunta di una nuova riga a sistema il campo deve essere lasciato vuoto

WPSERVICE.ITEMNUM: Campo "ID Materiale" della tabella "Risorse esterne"/"Ricavi" del tab Pianificazione dell'Ordine di lavoro. In questo campo sarà possibile inserire l'ID delle risorse esterne/ricavi censiti su EAM nell'applicazione "Catalogo delle risorse esterne / ricavi"

WPSERVICE.ITEMQTY: Campo "Quantità" della tabella "Risorse esterna" del tab Pianificazione dell'Ordine di lavoro. Nel campo sarà possibile inserire un numero intero positivo

WPSERVICE.LINECOST: Campo "Importo totale (€)" della tabella "Risorse esterne" e campo "Importo" della tabella "Ricavi" del tab Pianificazione dell'Ordine di lavoro. Nel campo sarà possibile inserire un numero intero o decimale

WPSERVICE.X4DATAINIZIORISORSA: Campo "Data inizio" della tabella "Risorse esterne"/"Ricavi" del tab Pianificazione dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato AAAA/MM/GGTHH:MM:SS+02:00. Nel campo sarà possibile inserire solo un valore che sia:

maggiore della data inserita in "Data fine"

inferiore o uguale alla data inserita in "Data pianificata inizio"

WPSERVICE.X4DATAFINERISORSA: Campo "Data fine" della tabella "Risorse esterne"/"Ricavi" del tab Pianificazione dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato AAAA/MM/GGTHH:MM:SS+02:00. Nel campo sarà possibile inserire solo un valore che sia:

minore della data inserita in "Data inizio"

inferiore o uguale alla data inserita in "Data pianificata fine"

## 12.4 MXLoader modifica Risorse Interne pianificate nell’OdL

La strumento MXLoader consente di aggiornare massivamente i dati delle risorse interne associate agli Ordini di Lavoro (OdL), permettendo in particolare di intervenire sui parametri di pianificazione e garantendo l’allineamento delle informazioni presenti a sistema.

A seguito della modifica delle date degli Ordini di Lavoro tramite l’MXLoader descritto nel paragrafo 12.2, è necessario procedere con l’allineamento delle date delle risorse interne utilizzando l’MXLoader illustrato nel presente paragrafo. Di seguito la procedura da seguire per l’utilizzo:

Creazione query: Nel tab "CreazioneQuery" nella colonna A va inserito l'elenco degli OdL di interesse. Nella cella B1 è presente la formula per generare la query che contenga tutti gli OdL inseriti nella colonna A. La query estratta dovrà essere copiata nella cella D1 del TAB "Estrazione", all'interno delle parentesi quadre ([...])

Inserimento query nel tab “Estrazione”: Inserire la query costruita nel tab "CreazioneQuery" nella cella D1 del TAB "Estrazione", all'interno delle parentesi quadre ([...]). Attenzione che alla fine dell'elenco non vi sia la virgola. Es: wonum IN ["931368","931384","931781",…]

Estrazione: Eseguire nel TAB "Estrazione" il run dell'interrogazione per estrarre i valori degli OdL da aggiornare, attraverso la funzione RUN contenuta nel menù MxLoader

Copiare le informazioni nel tab caricamento: Copiare le informazioni riportate nel TAB "Caricamento", facendo attenzione a inserire nelle colonne corrette

Apportare modifica ed eseguire caricamento: Dopo aver apportato le modifiche desiderate eseguire nel TAB "Caricamento" il run attraverso la funzione RUN contenuta nel menù MxLoader (vedi punto 3)

In fase di RUN delle righe compilate nella tab “Caricamento”, si segnala che la presenza di una riga vuota tra righe valorizzate comporta l’interruzione del caricamento: l’Excel si fermerà alla prima riga vuota individuata, senza proseguire con le successive righe compilate.

I campi presenti in questo file hanno il seguente significato:

IDCLUSTER: Campo libero e facoltativo in cui poter inserire un identificativo del caricamento eseguito al fine di agevolare le verifiche da effettuare a GUI sull'applicazione "Caricamenti MXLoader"

WONUM: ID dell'ordine di lavoro (corrisponde al campo "Ordine di lavoro"

SITEID: Campo popolato staticamente da non modificare; sempre uguale a SIT01

DESCRIPTION: Presente nel tab "Estrazione", Il campo riporta la Descrizione dell'OdL estratto

WOEQ9: SAP ID Asset. Presente nel tab "Estrazione", riporta il valore del SAP ID dell'asset oggetto dell'OdL estratto

ENVIRONMENT: Descrizione asset 1° lvl. Presente nel tab "Estrazione", riporta la descrizione dell'Asset di 1° lvl cui appartiene l'asset oggetto dell'OdL estratto

ASSET.DESCRIPTION: Descrizione asset 3° lvl. Presente nel tab "Estrazione", riporta la descrizione dell'Asset di 3° lvl padre dell'asset oggetto dell'OdL estratto

TARGSTARTDATE: Campo "Data pianificata inizio" dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato AAAA/MM/GG HH:MM:SS. Nel campo sarà possibile inserire solo un valore che sia:

maggiore della data inserita in "Data non prima di"

inferiore alla data inserita in "Data pianificata fine"

Si segnala che il dato è riportato a supporto della compilazione delle informazioni di pianificazione, essendo disponibile un MXLoader dedicato per la modifica delle date di pianificazione dell’Ordine di Lavoro

TARGCOMPDATE: Campo "Data pianificata fine" dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato AAAA/MM/GG HH:MM:SS. Nel campo sarà possibile inserire solo un valore che sia:

maggiore della data inserita in "Data pianificata inizio"

inferiore alla data inserita in "Data di massimo posticipo"

Si segnala che il dato è riportato a supporto della compilazione delle informazioni di pianificazione, essendo disponibile un MXLoader dedicato per la modifica delle date di pianificazione dell’Ordine di Lavoro

ESTDUR: Durata dell'Ordine di lavoro (corrisponde al campo "Durata Pianificata" presente nel tab Date dell'OdL). Nel campo sarà possibile inserire una durata nel formato HH:MM oppure un numero intero positivo

Si segnala che il dato è riportato a supporto della compilazione delle informazioni di pianificazione, essendo disponibile un MXLoader dedicato per la modifica della durata dell’Ordine di Lavoro

WPLABOR.WPLABORUID: ID identificativo della risorse interna estratta. Si segnala che:

se si intende andare in modifica della risorsa interna già presente a sistema il campo non deve essere modificato/cancellato

per procedere con l'aggiunta di una nuova riga a sistema il campo deve essere lasciato vuoto

WPLABOR.CRAFT: Campo "Qualifica" della tabella "Risorse interne" del tab Pianificazione dell'Ordine di lavoro. I valori ammessi sono i seguenti e devono essere inseriti in maiuscolo:

IMPIEGAT

OPERAIO

QUADRO

WPLABOR.QUANTITY: Campo "Quantità" della tabella "Risorse interne" del tab Pianificazione dell'Ordine di lavoro. Nel campo sarà possibile inserire un numero intero positivo

WPLABOR.X4TITRIS: Campo "Titris" della tabella "Risorse interne" del tab Pianificazione dell'Ordine di lavoro. Nel campo sarà possibile inserire solo il Titris responsabile dell'attività oppure I Titris per i quali sono state ricevute e accettate delle ore in capacity

WPLABOR.LABORHRS: Campo "Ore esecuzione" della tabella "Risorse interne" del tab Pianificazione dell'Ordine di lavoro. Nel campo sarà possibile inserire un valore nel formato HH:MM oppure un numero intero positivo

WPLABOR.X4ORETRASF: Campo "Ore trasferimento" della tabella "Risorse interne" del tab Pianificazione dell'Ordine di lavoro. Nel campo sarà possibile inserire un valore nel formato HH:MM oppure un numero intero positivo

WPLABOR.X4DATAINIZIORISORSA: Campo "Data inizio risorsa" della tabella "Risorse interne" del tab Pianificazione dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato AAAA/MM/GGTHH:MM:SS+02:00. Nel campo sarà possibile inserire solo un valore che sia:

maggiore della data inserita in "Data fine risorsa"

inferiore o uguale alla data inserita in "Data pianificata inizio"

Si segnala che è necessario che sia presente almeno una data di inizio risorsa coincidente con la “Data pianificata di inizio” dell’OdL

WPLABOR.X4DATAFINERISORSA: Campo "Data fine risorsa" della tabella "Risorse interne" del tab Pianificazione dell'Ordine di lavoro. Sarà possibile inserire un valore nel formato AAAA/MM/GGTHH:MM:SS+02:00. Nel campo sarà possibile inserire solo un valore che sia:

minore della data inserita in "Data inizio risorsa"

inferiore o uguale alla data inserita in "Data pianificata fine"

Si segnala che è necessario che sia presente almeno una data di fine risorsa coincidente con la “Data pianificata di fine” dell’OdL

## 12.5 Processo di Analisi OdL e modifica pianificazione tramite MXLoader

ANALISI PIANIFICAZIONE ODL

Analisi OdL tramite applicazione di monitoraggio OdL: consente l’analisi degli OdL in forma estesa, offrendo una visione completa delle informazioni disponibili, con la possibilità di applicare filtri su diversi attributi dell’ordine e di esportare i risultati della ricerca in formato Excel per successive analisi,

Analisi OdL tramite report di avanzamento OdL: consente l’analisi degli OdL in forma compatta, offrendo una vista sintetica focalizzata sugli attributi di pianificazione oraria ed economica, con la possibilità di applicare filtri basati su diversi parametri di lancio ed esportare i risultati della ricerca in formato Excel per ulteriori approfondimenti.

MODIFICA MASSIVA

Download set di OdL da modificare: procedere al download del set di OdL da modificare, applicando i filtri sui parametri di interesse tramite MXLoader in modalità «Query»,

Applicazione delle modifiche in MXLoader: apportare le modifiche nel template Excel dell’MXLoader, aggiornando i dati in funzione delle esigenze operative individuate,

Upload delle modifiche: confermare le modifiche ed effettuare il caricamento in EAM utilizzando la modalità «Change» dell’MXLoader

## 12.6 Strumenti di Analisi OdL: esempio pianificazione Programmi

Analisi OdL tramite applicazione di monitoraggio OdL:

Accedere all’applicazione «Monitoraggio OdL» e applicare il filtro “PR%” nel campo “ordine di lavoro” per identificare gli Ordini di Lavoro relativi ai programmi,

Esportare i risultati della ricerca in formato Excel, considerando che verranno inclusi esclusivamente i campi visualizzati nella maschera di ricerca dell’OdL.

Figura: applicazione del filtro “PR%” nel campo ordine di lavoro e utilizzo delle funzionalità di filtro/esportazione nella schermata di Monitoraggio OdL.

Analisi OdL tramite report di avanzamento OdL

Selezionare il report di avanzamento OdL tramite la funzionalità «Esegui report» disponibile nell’applicazione di Monitoraggio OdL

Figura: selezione della funzionalità “Esegui report” dall’applicazione Monitoraggio OdL per l’accesso al report di avanzamento.

Impostare i parametri di lancio desiderati, quali ad esempio le date di pianificazione, procedere con l’esecuzione del report,

Esportare il report in formato Excel e applicare il filtro «PR%» per visualizzare esclusivamente gli Ordini di Lavoro relativi ai programmi.

Figura: visualizzazione del report di avanzamento OdL e utilizzo delle funzionalità per l’esportazione e l’applicazione dei filtri

## 12.7 Modifica pianificazione tramite MXLoader: esempio pianificazione Programmi

Download set di OdL da modificare: effettuare il download del set di Ordini di Lavoro da modificare applicando il filtro “PR%” nella cella D1 e selezionando la modalità “Query” nella cella C1. L’operazione viene avviata tramite il pulsante Run

Applicazione delle modifiche in MXLoader: apportare le modifiche nel template Excel dell’MXLoader (cfr. par. 12.2), aggiornando le date di inizio e fine pianificazione e i relativi vincoli temporali (ad esempio “non prima di” e “massimo posticipo”), in funzione delle esigenze operative, nel rispetto del formato richiesto dal sistema. Successivamente allineare le date delle risorse interne ed esterne con quelle degli ordini di lavoro, seguendo quanto descritto nei par. 12.3 e 12.4

Figura: inserimento del filtro “PR%” nel tab “Query” e aggiornamento dei campi di pianificazione nel template MXLoader.

Upload delle modifiche: confermare le modifiche apportate e procedere con il caricamento in EAM tramite il pulsante Run, utilizzando la modalità «Change» dell’MXLoader, al fine di applicare in modo massivo gli aggiornamenti agli Ordini di Lavoro selezionati e garantirne l’allineamento a sistema.

Figura: esecuzione del comando Run in modalità “Change” in MXLoader per il caricamento delle modifiche nel sistema EAM.

## 12.8 Processo di creazione OdL tramite MXLoader

EAM consente di generare massivamente set di OdL utilizzando la funzionalità MXLoader. In particolare, per i PdL con attributo Generabile manualmente «SI», è possibile utilizzare il template MXLoader per generare massivamente ordini di lavoro inserendo i dati obbligatori richiesti.

RICERCA PDL GENERABILI MANUALMENTE

Analisi PdL tramite applicazione di monitoraggio PdL: consente l’analisi dei PdL generabili manualmente con possibilità di scaricare il risultato della ricerca in formato excel. Passaggio necessario per assicurarsi che gli OdL da generare siano associati a PdL idonei e che i relativi asset siano compatibili con l’attività

CREAZIONE MASSIVA

Inserimento OdL nel template MXLoader: popolare il template MXLoader inserendo i campi richiesti per la generazione in base al tipo di OdL (aggregato o singolo)

Generazione degli OdL: confermare i dati inseriti ed effettuare la creazione in EAM utilizzando la modalità «AddChange» dell’MXLoader. Gli OdL saranno generati in stato «APERTO».

Di seguito viene condivisa la descrizione dei campi da compilare:

| Campo | Obbligatorietà | Descrizione / Valore |
| --- | --- | --- |
| ID CLUSTER | Facoltativo | Se popolato con un ID numerico sempre uguale per tutti i record, facilita la ricostruzione di eventuali errori di elaborazione. |
| WONUM | Facoltativo | Popolare per creare relazione padre-figlio tramite il campo parent nel caso degli aggregatori. Se lasciato blank viene popolato dal sistema. |
| ASSETNUM | Obbligatorio | Selezionare compatibilmente con PdL. |
| SITEID | Obbligatorio | Costante SIT01. |
| PARENT* | Obbligatorio* | Popolare solo su OdL figli per indicare il WONUM del padre. |
| X4POLICY | Facoltativo | Popolare solo se l'ordine deve essere elaborato dal DSS. |
| JPNUM | Obbligatorio | PdL con attributo CARICABILE MANUALMENTE = SI. |
| X4SAPIDASSET | Facoltativo | Se non popolato viene recuperato dall'ASSETNUM. |
| X4AGGREGATI | Obbligatorio | Per indicare se l'OdL è padre (1) o figlio/singolo (0). |
| X4TRONCONUM | Facoltativo | Da popolare solo se si sta creando un OdL padre su tronco di ispezione. |
| X4DATAMODELLO | Obbligatorio | Data modello. |
| TARGCOMPDATE | Obbligatorio | Data pianificata fine. |
| TARGSTARTDATE | Obbligatorio | Data pianificata inizio. |
| SNECONSTRAINT | Obbligatorio | Data non prima di. |
| FNLCONSTRAINT | Obbligatorio | Data di massimo posticipo. |

*Manuale per scrittura query: https://bportaluri.com/2021/04/mxloader-rest-query-where.html

12.11 Ricerca PdL generabili manualmente

La seguente procedura consente di identificare e analizzare i PdL generabili manualmente tramite l’applicazione di Monitoraggio PdL.

Analisi PdL tramite applicazione di monitoraggio PdL

Accedere all’applicazione «Monitoraggio PdL» e utilizzare la query predefinita per identificare i PdL generabili manualmente, così da visualizzare in modo immediato l’insieme dei record rilevanti per le successive attività di generazione degli OdL

Esportare i risultati della ricerca in formato Excel, tenendo conto che verranno inclusi esclusivamente i campi visualizzati nella maschera di ricerca del PdL, così da consentire ulteriori analisi e verifiche sui dati estratti.

Figura: utilizzo della query predefinita nell’applicazione Monitoraggio PdL e accesso alle funzionalità di filtro ed esportazione dei risultati.

## 12.9 Generazione massiva OdL : MXLoader aggregati

La seguente procedura descrive le attività necessarie per la generazione massiva di Ordini di Lavoro aggregati tramite MXLoader.

Popolamento del template in MXLoader: popolare il template MXLoader inserendo i campi richiesti per la generazione degli OdL aggregati, assicurando la corretta compilazione delle informazioni necessarie. In particolare, utilizzare il campo PARENT per indicare il riferimento dell’OdL padre e il campo X4AGGREGATI per specificare se l’OdL è padre (1) o figlio (0), così da garantire la corretta strutturazione delle relazioni tra gli ordini.

Generazione degli OdL: confermare i dati inseriti e procedere con la creazione degli OdL in EAM utilizzando la modalità «AddChange» dell’MXLoader tramite il pulsante Run, al fine di generare in modo massivo gli ordini e garantirne la corretta registrazione e disponibilità a sistema

Figura: esecuzione del comando Run in modalità “AddChange” in MXLoader per la generazione massiva degli OdL.

## 12.10 Generazione massiva OdL: MXLoader singoli

La seguente procedura descrive le attività necessarie per la generazione massiva di Ordini di Lavoro singoli tramite MXLoader.

Popolamento del template in MXLoader: popolare il template MXLoader inserendo i campi richiesti per la generazione degli OdL singoli, assicurando la corretta compilazione delle informazioni necessarie. In particolare, lasciare vuoto il campo PARENT e valorizzare il campo X4AGGREGATI con 0, così da identificare l’OdL come singolo e garantirne la corretta gestione a sistema.

Generazione degli OdL: confermare i dati inseriti e procedere con la creazione degli OdL in EAM utilizzando la modalità «AddChange» dell’MXLoader tramite il pulsante Run, al fine di generare in modo massivo gli ordini singoli e garantirne la corretta registrazione e disponibilità a sistema.

Figura: popolamento del template MXLoader per la generazione degli OdL singoli ed esecuzione del comando Run in modalità “AddChange”
