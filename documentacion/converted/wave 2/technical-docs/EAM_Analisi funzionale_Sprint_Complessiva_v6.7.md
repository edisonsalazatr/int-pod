# EAM_Analisi funzionale_Sprint_Complessiva_v6.7

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento | Analisi funzionale – Fase di sprint |

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

# Obiettivi del documento

Il presente documento di analisi funzionale ha l’obiettivo di descrivere la soluzione EAM adottata per l’implementazione dell’ambito trattato nel corso della fase di sprint, iniziata il 04/04/2024 e conclusa il 26/09/2024

Nello specifico, il documento è costruito a partire dei seguenti capitoli:

Gestione cataloghi;

Decision Support System (DSS);

Gestione indicatori di condizione tecnica;

Gestione capacity;

Gestione OdL on condition e periodiche;

Gestione OdL serbatoio;

Creazione e gestione OdL manuale;

Pianificazione tecnica;

Indisponibilità;

Pianificazione economica;

Esecuzione OdL;

Assegnazione e ruoli figure OdL;

Chiusura OdL;

Reportistica;

Ogni punto sarà descritto nel dettaglio nei paragrafi successivi e costituirà la linea guida funzionale per l’implementazione in EAM.

# Gestione cataloghi

## Processo di gestione cataloghi

Per garantire una gestione ottimale delle attività di monitoraggio, manutenzione e sostituzione nei processi operativi di Terna, il sistema EAM dispone di entità logiche denominate cataloghi. Questi cataloghi raggruppano elementi univoci utilizzati per la generazione di input necessari ai fini dell’esecuzione delle attività, nonché per il monitoraggio delle condizioni degli asset.

La gestione dei cataloghi è consentita in EAM tramite le relative applicazioni (è definita un’applicazione per ciascun catalogo). Tramite la relativa applicazione, tutti gli utenti possono visualizzare i cataloghi riportati in maschera tramite vista elenco (descritte nel seguito del documento), mentre solamente gli utenti centrali possono effettuare modifiche / aggiunte / eliminazioni direttamente tramite l’interfaccia di EAM. Nel caso in cui l’utente elimini un elemento da un catalogo, il sistema mostra un warning generico non bloccante per notificarlo degli effetti che potrebbe produrre l’operazione che sta effettuando (incongruenze in altri cataloghi); nel caso in cui l’operazione effettuata dall’utente generi incongruenze in altri cataloghi, queste vengono segnalate puntualmente nel centro di controllo come descritto nel paragrafo 2.1.2. La struttura delle viste elenco di ciascun catalogo viene descritta nel capitolo 2.2.

Associazioni tra cataloghi e ordine di definizione

Lo schema riportato in Figura 1 mostra i cataloghi gestiti in EAM e le rispettive interdipendenze. Le frecce indicano le relazioni di dipendenza tra i cataloghi, partendo (da sinistra) dai cataloghi la cui definizione è propedeutica per la compilazione dei cataloghi successivi. Lo schema evidenzia anche la sequenza di definizione dei cataloghi all’interno di EAM. Data la natura delle relazioni tra i cataloghi, è fondamentale che essi vengano definiti o modificati seguendo la logica sottostante, al fine di minimizzare il rischio di incongruenze:

Inserimento dei cataloghi autonomi, che non richiedono riferimenti ad altri cataloghi per essere definiti (misure / variabili di stato, modelli-target);

Definizione dei cataloghi delle difformità e delle interferenze, i quali fanno riferimento al catalogo delle misure / variabili di stato;

Definizione del catalogo delle attività e degli indicatori di condizione tecnica, con relazioni ai cataloghi delle difformità e delle interferenze;

Definizione del catalogo dei micro-controlli.

Definizione del catalogo delle policy, che integra al suo interno le relazioni con gli oggetti presenti negli altri cataloghi.

Figura 1 – Cataloghi e loro interdipendenze

Di seguito sono elencati i cataloghi / anagrafiche descritti nel presente documento di analisi (approfonditi nel dettaglio nel successivo capitolo):

Anagrafiche opere interferenti: le opere interferenti sono gestite in EAM come entità anagrafiche di quarto livello e vengono rilevate durante le attività di monitoraggio. In particolare, l’anagrafica delle opere interferenti contiene l’insieme delle opere interferenti con le loro caratteristiche principali. Le informazioni anagrafiche relative alle opere interferenti sono utilizzate dal DSS per la generazione di nuove attività. Di conseguenza, l’anagrafica delle opere interferenti è un input utilizzato nel catalogo delle policy e dal DSS per la generazione di nuove attività.

Catalogo di misure e variabili di stato: il catalogo contiene le misure e le variabili di stato (c.f.r paragrafo 2.2.4). Il catalogo di misure e variabili di stato è strettamente correlato ai cataloghi delle difformità e delle interferenze e al catalogo delle Policy. In particolare, nelle Policy, misure e variabili di stato devono essere associate a specifici target di raffronto definiti a loro volta nel catalogo delle Policy.

Catalogo Indicatori di condizione tecnica: nel sistema EAM, per valutare lo stato di salute di un asset o di un insieme di asset, vengono utilizzati gli Indicatori di condizione tecnica. Questi si dividono in Health Index (HI), Tassi di guasto (TdG), Tassi di anomalia (TdA).Gli indicatori di condizione tecnica agiscono come trigger per la generazione di attività di manutenzione o sostituzione da parte del DSS. Di conseguenza, tali indicatori sono utilizzabili come input nel catalogo delle policy. Gli indicatori di condizione tecnica sono definiti nel modulo “Health” di EAM (le cui logiche di funzionamento sono descritte in analisi funzionale 4) tramite l’inserimento di cataloghi specifici dettagliati nel paragrafo 2.2.6.

Catalogo modelli-target: per ogni tipologia di asset, il catalogo modelli-target contiene “parametri target” che rappresentano i valori di soglia utilizzati per monitorare le prestazioni ottimali all’interno della rete. Le voci presenti nel catalogo modelli-target servono per la definizione degli input delle policy. Di conseguenza, il catalogo modelli-target è relazionato al catalogo delle policy (c.f.r paragrafo 2.2.3).

Catalogo delle interferenze: questo catalogo riporta l’insieme di interferenze rilevabili e risolvibili sugli asset oggetto degli OdL, nonché sugli asset sottostanti. Il catalogo delle interferenze fornisce input alle Policy per la generazione di nuove attività tramite il DSS ed è quindi strettamente correlato al catalogo delle policy, delle attività e delle misure / variabili di stato (c.f.r paragrafo 2.2.2).

Catalogo delle difformità: questo catalogo riporta l’insieme di difformità rilevabili e risolvibili legate agli asset oggetto degli OdL, nonché agli asset sottostanti. Il catalogo delle difformità fornisce input alle Policy per la generazione di nuove attività tramite il DSS ed è quindi strettamente correlato al catalogo delle policy, delle attività e delle misure / variabili di stato (c.f.r paragrafo 2.2.1).

Catalogo micro-controlli: per le attività di controllo periodico è prevista l’esecuzione di micro-controlli dove sono definite le diverse classificazioni dei micro-controlli, così come i relativi attributi specifici. È prevista un’associazione diretta fra il catalogo delle attività e quello dei micro-controlli. Inoltre, è possibile rilevare difformità e interferenze attraverso specifici micro-controlli. Le informazioni relative alle difformità e alle interferenze rilevabili sono riportate nel catalogo dei micro-controlli. Pertanto, è prevista un’associazione tra i cataloghi delle difformità e delle interferenze con quello dei micro-controlli (c.f.r paragrafo 2.2.5).

Gestione incongruenze nei cataloghi

Nel sistema EAM è previsto il monitoraggio di eventuali incongruenze derivanti della gestione e modifica dei cataloghi. Nello specifico, l’utente potrà verificare nel centro di controllo le seguenti incongruenze causate dalle modifiche apportate ai rispettivi cataloghi:

Difformità / interferenze non rilevate da alcuna attività / micro-controllo;

Difformità / interferenze non risolte da alcuna attività / micro-controllo;

Eliminazione di Misure associate a Difformità / Interferenze con “Generazione da misura”;

Policy per le quali è stato eliminato un input (monitoraggio descritto nel dettaglio nell’analisi dello sprint 4);

Nelle sezioni successive vengono approfondite le casistiche di monitoraggio delle incongruenze sopra elencate, con un’analisi dettagliata delle rispettive modalità di gestione.

#### Difformità / interferenze non rilevate da alcuna attività / micro-controllo

In questo paragrafo viene descritta la sezione del Centro di controllo in cui l’utente può visualizzare le difformità o interferenze non rilevate da alcuna attività o micro-controllo. In particolare, una difformità o interferenza viene indicata nel CdC qualora il PdL che la rileva venga disattivato nel catalogo delle attività oppure qualora il micro-controllo che la rileva venga eliminato nel relativo catalogo, come dettagliato nell’analisi funzionale 1.

Le caratteristiche delle difformità o interferenze non rilevate da alcuna attività o micro-controllo, visualizzabili nel Centro di controllo illustrato in Figura 2, includono:

ID Difformità / Interferenza: codice ID univoco che identifica la difformità o interferenza.

Descrizione della difformità / interferenza: descrizione dettagliata della difformità o interferenza individuata.

Classificazione Asset associato: tipologia dell’asset su cui la difformità o interferenza potrebbe essere rilevata (es. Conduttore, Fune di guardia, ecc).

Stato: stato “Attivo” della difformità o interferenza nei rispettivi cataloghi,

Figura 2 - Sezione CdC “Difformità / Interferenze non rilevate da alcuna attività”

Selezionando una voce presente in questa sezione del CdC, l’utente viene automaticamente reindirizzato alla difformità o interferenza all’interno del catalogo corrispondente, dove può impostarne lo stato su “Non attivo”.

In alternativa l’utente può, dopo aver esaminato nel CdC le difformità e/o interferenze da adeguare, associarle a un PdL esistente o ad uno nuovo, oppure ad un micro-controllo, accedendo direttamente al catalogo delle attività / micro-controlli. Una volta selezionato il PdL / micro-controllo attraverso il quale rilevare la difformità o interferenza, l’utente può aggiungerla nel tab “Rilevazioni” selezionando il tasto “+” nella rispettiva tabella, come mostrato in Figura 3 nel caso delle attività.

Figura 3 - Catalogo attività

#### Difformità / Interferenze non risolte da alcuna attività

In questo paragrafo è descritta la sezione del Centro di controllo in cui l’utente può visualizzare tutte le difformità non risolte da alcuna attività. Nello specifico, una difformità viene visualizzata in questa sezione del CdC qualora il PdL che dovrebbe risolverla venga disattivato nel catalogo delle attività, come descritto nell’analisi funzionale 1.

Le caratteristiche delle difformità non risolte da alcuna attività, riportate nel Centro di controllo rappresentato in Figura 4, includono:

ID Difformità: codice ID univoco che identifica la difformità;

Descrizione della difformità: descrizione della difformità individuata;

Classificazione asset associato: tipologia dell’asset su cui la difformità dovrebbe essere risolta (es. Conduttore, Fune di guardia, ecc);

Stato: stato “Attivo”della difformità nel rispettivo catalogo

Figura 4 – Sezione CdC “Difformità non risolte”

Selezionando una voce all’interno di questa sezione del CdC, l’utente viene automaticamente reindirizzato alla difformità nel rispettivo catalogo, dove ha la possibilità di impostare lo stato su “Non attivo”.

In alternativa, l’utente, dopo aver esaminato nel CdC le difformità da gestire, può associarle a un PdL già esistente, accedendo direttamente al catalogo delle attività. Una volta selezionato il nuovo PdL destinato alla risoluzione della difformità, l’utente può aggiungerla nel tab “Attività” selezionando il tasto “+” nella rispettiva tabella, come evidenziato nella Figura 5.

Figura 5 - Catalogo attività

#### Eliminazione di Misure associate a Difformità / Interferenze

In questo paragrafo è descritta la sezione del Centro di controllo in cui l’utente può visualizzare tutte le difformità / interferenze dalle quali risulta eliminato l ID misura utilizzata per la generazione.

In particolare, nel caso in cui l’utente elimini dal catalogo “Misure e Variabili di stato” una misura associata a una difformità / interferenza, viene riportata una notifica di incongruenza nella sezione “Difformità / Interferenze non generabili da Misure” del centro di controllo.

Le caratteristiche delle difformità non generabili a seguito di eliminazione della misura associata, riportate nel Centro di controllo rappresentato in Figura 6, includono:

ID Difformità: codice ID univoco che identifica la difformità;

Descrizione della difformità: descrizione della difformità individuata;

Classificazione asset associato: tipologia dell’asset su cui la difformità dovrebbe essere risolta (es. Conduttore, Fune di guardia, ecc);

Stato: stato “Attivo” attuale della difformità nel rispettivo catalogo

Figura 6 - Sezione CdC "Difformità / Interferenze non generabili da Misure

Selezionando una voce all’interno di questa sezione del CdC, l’utente viene automaticamente reindirizzato alla difformità nel rispettivo catalogo, dove ha la possibilità di impostare lo stato su “Non attivo”.

In alternativa, l’utente, dopo aver esaminato nel CdC le difformità da gestire, può associarle a una nuova misura valida, riabilitando la generazione automatica.

## Layout cataloghi

Tutti gli oggetti all’interno dei cataloghi sono codificati attraverso una logica che si basa sull’anagrafica degli asset a cui sono associati. Di seguito il file che riporta la transcodifica tra gli elementi anagrafici e i progressivi numerici usati nei codici catalogo.

Nei paragrafi seguenti, per ognuno dei cataloghi sono descritte le regole specifiche di costruzione.

Catalogo difformità

Il catalogo di difformità contiene, per ogni singolo asset, le difformità che possono essere rilevate dall’operatore durante i controlli di monitoraggio.

All’interno del catalogo, ogni difformità è riferita all’asset sul quale può essere rilevata. Le difformità possono essere associate a un qualsiasi livello di anagrafica.

L’utente può accedere ad una vista elenco del catalogo difformità (Figura 7) costituita dai seguenti campi (anche utilizzabili come filtri di ricerca):

Applicabile a: Nel caso di difformità definite su una singola classificazione, riporta la classificazione. Nel caso di difformità definite su un elemento, riporta l’elemento.

ID Difformità

Descrizione

Assieme

Sottoassieme

Stato

Figura 7 - Catalogo difformità, vista elenco

Per poter aggiungere una nuova difformità al catalogo l’utente clicca il tasto “+”, con il quale viene aperta la schermata rappresentata in Figura 8 con i campi vuoti da valorizzare.

Cliccando sul campo ID difformità nella vista elenco, viene aperta la schermata di dettaglio rappresentata in Figura 8.

Figura 8 - Catalogo difformità, schermata di dettaglio

In maschera vengono visualizzate le seguenti informazioni:

Elemento asset: Elemento Asset al quale è associata la difformità. Campo popolato manualmente dall’utente tramite una lista valori contenente tutti gli elementi asset presenti a sistema;

Classificazione asset: Tabella contenente le classificazioni alle quali è associata la Difformità. La tabella è popolata automaticamente in base all’Elemento precedentemente inserito. Il rapporto Elemento - Classificazione può essere sia 1:1 (in questo caso elemento e classificazione coincidono) sia 1:N (in questo caso a un elemento sono associate più classificazioni. Es: nel caso in cui si inserisse un elemento “Interruttore” nella tabella compariranno le classificazioni “Interruttore MT ad aria compressa”, “Interruttore MT Deion”, “Interruttore MT in SF6”, etc..). Si specifica che l’utente, se lo ritiene necessario, potrà solamente eliminare alcune delle classificazioni proposte dal sistema per le quali non ritiene necessario associare la Difformità;

Gerarchia asset associato: asset su cui la difformità può essere rilevata (popolata automaticamente all’inserimento dell’asset);

Assieme e sottoassieme difformità: campi per la clusterizzazione della difformità (pick-list di valori predefiniti. In schermata è presente il campo contente un codice abbreviato da cui va selezionato il valore ed è visibile anche il campo descrizione. Nella dialog di selezione è presente la colonna del codice abbreviato con la relativa descrizione.

Stato: campo in cui è possibile settare lo stato della difformità;

ID difformità: codice identificativo diverso a seconda dell’asset generato automaticamente dal sistema e non modificabile dagli utenti. All’aggiunta di una nuova difformità, l’ID difformità viene automaticamente calcolato dal sistema al termine del caricamento dei dati della difformità, in funzione dell’anagrafica a cui essa è riferita con la seguente logica in base al tipo di asset a cui è associata:

ID Linee aeree: Diff (Difformità) . L (Linea aerea) . n° progressivo associato al 2° liv anagrafica . n° progressivo associato al 3° livello di anagrafica . n° progressivo associato al 4° livello di anagrafica . Progressivo crescente da 1 a n (esempio: Diff.L.1.1.1.1);

ID Linee in cavo: Diff (Difformità) . LC (Linea cavo) . n° progressivo associato al 2° liv anagrafica . n° progressivo associato al 3° livello di anagrafica . n° progressivo associato al 4° livello di anagrafica . Progressivo crescente da 1 a n (esempio: Diff.LC.3.1.1.1);

ID Stazioni: Diff (Difformità) . S (Stazione) . n° progressivo associato all’elemento anagrafico di riferimento (diff.S.48.1, 48 transcodifica con l’interruttore ais x) . Progressivo crescente da 1 a n.

Descrizione: descrizione della difformità. Il campo risulta modificabile (inserimento libero) e la modifica si deve riflettere su tutte le difformità in qualsiasi stato presente a sistema;

Gravità difformità: valore che può assumere il campo Gravità per una difformità. In particolare, identifica la lista di valori che sono selezionabili dall’utente nel momento in cui registra la difformità e utilizzabili come target di raffronto nelle Policy per le relative difformità. L’inserimento avviene nella tabella dedicata “Target/Gravità” dove l’utente può inserire un numero di righe equivalente ai possibili valori assumibili dalla Gravità della difformità. Nella tabella “Target/Gravità” è possibile inserire liberamente valori alfanumerici (ad esempio arrugginimento “lieve”, “medio”, “grave”, “>30°C”, “15< valore < 30°”, ecc..) oppure un target del catalogo dei modelli target (ID catalogo modelli-target). Il valore presente nel campo è modificabile dall’utente. Nel caso in cui il flag “Generazione automatica” è uguale a SI obbligatorio anche indicare, per ciascun valore di gravità, un N° logica di generazione (presente nella tabella “Formula”) che indica la formula tramite la quale deve essere calcolato il valore della gravità al momento della registrazione di una misura o al calcolo dell’HI. Il sistema al momento del popolamento della tabella controlla se il valore inserito nel campo “N. logica di generazione” è univoco e se è popolato per tutte le righe;

Massimo tempo di validazione: indica l’intervallo temporale massimo (in giorni) per cui la difformità rilevata rimane in stato “In validazione”; Se necessario, l’utente centrale abilitato può modificare il massimo tempo di validazione della difformità (inserimento libero). Nel caso in cui il tempo massimo di validazione impostato sia inferiore alla data odierna, lo stato della difformità viene automaticamente modificato in “Validato”

Modificato da: Matricola dell’utente che ha effettuato l’ultima modifica sulla difformità

Data ultima modifica: Data dell’ultima modifica sulla difformità

Tipologia difformità: può assumere i valori di “Anomalia” / “Guasto” / “Anomalia+Guasto” (pick-list di valori predefiniti). Nel caso si selezioni “Anomalia+Guasto” l’appartenenza ad una delle due tipologie è guidata dalla tipologia di controllo: alla creazione dell’OdL, il sistema proporrà la tipologia “anomalia” se il controllo è periodico/tecnico, oppure la tipologia “guasto” in caso di controllo occasionale. Ci sarà poi la possibilità per l’utente di modificare il campo, all’atto della registrazione della difformità scegliendo esclusivamente tra “anomalia” o “guasto”;

Misure difformità: contiene gli ID delle misure che sono rilevabili contestualmente alla difformità in fase di rilevazione sull’ordine. Si può scegliere da una la lista che presenta solo le misure relative alle stesse classificazioni associate alla difformità.

Generazione automatica: flag che determina se la difformità viene registra automaticamente a sistema a fronte del calcolo eseguito su uno o più parametri indicati nella tabella “parametri per la generazione difformità”, attraverso le formule descritte nella tabella “Formule”. Se il campo è popolato con SI, deve esistere almeno un record nella tabella “Formule”. Se il campo “generazione da misura” è uguale a “NO” la difformità può essere registrata manualmente dagli utenti in fase di esecuzione attività.

Parametri per la generazione di difformità: La tabella viene popolata automaticamente sulla base di quanto inserito nella tabella “Formule” ed è in sola visualizzazione. La tabella riporta i seguenti campi:

Tipo: indica il tipo di parametro VARIABILE, SOGLIA e HI;

ID parametro formula: mostra l’ID del parametro inserito nella formula;

N. Logica di generazione: mostra il numero della logica di generazione assegnato alla formula nel quale è stato utilizzato il parametro.

Formule: l’inserimento di almeno una formula è obbligatorio se il flag “Generazione automatica” è a SI. L’utente può inserire tale logica manualmente tramite una formula a testo libero, aggiungendo una riga nella tabella, utilizzando esclusivamente gli operatori di seguito indicati: “=”, “>”, “<”, “!=”, AND, OR, *, +, -, /, >=, <=. Ad ogni riga deve essere assegnato un codice univoco progressivo numerico solo se è necessario richiamarla nella tabella “Gravità”. Logiche di controllo consentono di valutare la coerenza sintattica della formula e la presenza dei parametri inseriti nella sezione “parametri per la generazione difformità” e di verificare inoltre che i codici inseriti siano censiti nei rispettivi cataloghi (ad esempio che i codici di target inseriti corrispondano ad un record nel catalogo dei target, ect) e che siano associati alle stesse classificazioni della difformità, restituendo un alert in caso di mancato riscontro. L’utilizzo della formula ha le seguenti regole:

Possibile gestire, con gli operatori di cui sopra, confronto di una variabile con un valore target;

Possibile gestire, con gli operatori di cui sopra, confronto di una variabile con un valore target moltiplicato per un coefficiente;

Possibile gestire i due casi a) e b) combinati in AND o in OR con altri casi a) e b), il che vuol dire che nella stessa logica di generazione sarà possibile gestire più confronti in AND e in OR con più variabili e più target in input

Possibile gestire logica di generazione che utilizza, per fare un confronto come ai punti precedenti (a, b, c), una variabile calcolata in seguito all’inserimento di altre variabili sullo stesso asset. Il calcolo avverrà ad evento;

di seguito sono riportati i vincoli per l’inserimento delle formule a testo libero:

Disponibili nella formula come parametri solo misure, soglie ed HI;

Ogni misura in input deve avere il prefisso VAR; ogni soglia deve avere il prefisso TRG; ogni Health Index deve iniziare con HI;

Non sono gestite trasformazioni su unità di misura;

La formula testo libero scritta dall’utente sarà acquisita dal sistema solo dopo che soddisferà le regole guidate dalla GUI;

L’espressione della formula non può superare i 3500 caratteri compresi eventuali separatori es. lo spazio.

Una volta registrata la difformità a sistema sulla base dei calcoli effettuati sui parametri, questa non sarà modificata in caso di modifica dei parametri inseriti a catalogo. Inoltre, se già presente una difformità uguale sullo stesso asset in stato “Validata” o “Da validare” non ne verrà creata una nuova.

Caratteristiche asset: La tabella riporta il codice dell’attributo, la descrizione e il valore che deve assumere una certa caratteristica (Attributo) strettamente dell’asset a cui è associata la difformità in oggetto (sono esclusi i campi di testata) per poter essere rilevata sul campo. I valori presenti sulla tabella sono modificabili se necessario. Per aggiungere un ulteriore caratteristica è necessario aggiungere una riga alla tabella.

Fase: per componenti costituiti da più fasi come, ad esempio, il sezionatore o l’interruttore, il campo fase deve essere compilato dall’utente in fase di inserimento per indicare la fase a cui fa riferimento la difformità (obbligatorio solo per componenti multifase). Il campo è un flag da valorizzare nel caso in cui sia necessario applicare le logiche di fase.

Attività che risolvono la difformità: la tabella riporta le attività risolutrici della difformità ed è solo in visualizzazione.

Obbligatorietà foto: flag che indica l’obbligatorietà di allegare una foto al momento della registrazione della difformità. Il flag è prevalorizzato a NO.

Qualunque modifica effettuata viene storicizzata con la data di modifica e la matricola dell’utente che l’ha effettuata.

La Figura 9 sotto riporta i campi obbligatori (in verde) che devono essere valorizzati affinché una difformità possa essere aggiunta al catalogo, distinguendo per anomalia e guasto.

Figura 9 - Catalogo difformità, campi obbligatori (verdi) e facoltativi (giallo)

#### Stati difformità

Le difformità possono assumere i seguenti stati:

Figura 10 - Stati delle difformità

Una volta creata, alla difformità viene automaticamente associato lo stato di “Bozza”. In questo stato è possibile compilare o modificare tutti campi relativi alla difformità ed associarla a policy ed attività. Per portare la difformità in stato attivo è necessario compilare tutti i campi obbligatori. Questi sono:

Tipologia asset

Gerarchia anagrafica associato

ID difformità

Descrizione

Massimo tempo di validazione

Tipologia

Quando la difformità passa in stato attivo sarà considerata dal DSS per la generazione di attività risolutive. In questo stato è possibile modificare le associazioni con le attività risolutrici (attraverso l’apposita tabella nel catalogo attività) oltreché le logiche di generazione. La soluzione consente la modifica degli attributi della difformità in stato “Attivo”. È possibile modificare tutti i campi della difformità ad eccezione di “classificazione”.

In qualunque momento l’utente può portare lo stato della difformità in “Non attivo”. La difformità non sarà più rilevabile da alcuna attività. Una volta in questo stato non sarà possibile eseguire alcuna operazione o modifica sulla difformità a meno di riportarla nuovamente in stato attivo.

Catalogo interferenze

Il catalogo delle interferenze riporta tutte le interferenze rilevabili sugli asset durante i controlli di monitoraggio.

L’utente può accedere ad una vista elenco del catalogo interferenze (Figura 11) costituita dai seguenti campi (anche utilizzabili come filtri di ricerca):

ID Interferenza

Applicabile a

Assieme

Sottoassieme

Descrizione

Figura 11 - Catalogo interferenze, vista elenco

Per poter aggiungere una nuova interferenza al catalogo l’utente clicca il tasto “+”, con il quale viene aperta la schermata rappresentata in Figura 12

Figura 12 - Catalogo interferenze, schermata di dettaglio

In maschera vengono visualizzate le seguenti informazioni:

Elemento asset: Elemento Asset al quale è associata l’interferenza. Campo popolato manualmente dall’utente tramite una lista valori contenente tutti gli elementi asset presenti a sistema;

Classificazione asset: Tabella contenente le classificazioni alle quali è associata l’interferenza. La tabella è popolata automaticamente in base all’Elemento precedentemente inserito. Il rapporto Elemento - Classificazione può essere sia 1:1 (in questo caso elemento e classificazione coincidono) sia 1:N (in questo caso a un elemento sono associate più classificazioni. Es: nel caso in cui si inserisse un elemento “Interruttore” nella tabella compariranno le classificazioni “Interruttore MT ad aria compressa”, “Interruttore MT Deion”, “Interruttore MT in SF6”, etc..). Si specifica che l’utente, se lo ritiene necessario, potrà solamente eliminare alcune delle classificazioni proposte dal sistema per le quali non ritiene necessario associare l’interferenza;

Gerarchia anagrafica associato: asset su cui l’interferenza può essere rilevata (popolata automaticamente all’inserimento dell’asset);

Assieme e sottoassieme interferenza: campi per la clusterizzazione dell’interferenza (pick-list di valori predefiniti). In schermata è presente il campo contente un codice abbreviato da cui va selezionato il valore ed è visibile anche il campo descrizione. Nella dialog di selezione è presente la colonna del codice abbreviato con la relativa descrizione;

Stato: campo in cui è possibile settare lo stato dell’interferenza;

ID interferenza: codice identificativo diverso a seconda dell’asset generato automaticamente dal sistema e non modificabile dagli utenti. All’aggiunta di una nuova interferenza, l’ID interferenza viene automaticamente calcolato dal sistema al termine del caricamento dei dati dell’interferenza, in funzione dell’anagrafica a cui essa è riferita con la seguente logica in base al tipo di asset a cui è associata:

ID Linee aeree: Int (Interferenza) . L (Linea aerea) . n° progressivo associato al 2° liv anagrafica . n° progressivo associato al 3° livello di anagrafica . n° progressivo associato al 4° livello di anagrafica . Progressivo crescente da 1 a n (esempio: Int.L.1.1.1.1);

ID Linee in cavo: Int (Interferenza) . LC (Linea cavo) . n° progressivo associato al 2° liv anagrafica . n° progressivo associato al 3° livello di anagrafica . n° progressivo associato al 4° livello di anagrafica . Progressivo crescente da 1 a n (esempio: Int.LC.3.1.1.1);

ID Stazioni: Int (Interferenza) . S (Stazione) . n° progressivo associato all’elemento anagrafico di riferimento (Int.S.48.1, 48 transcodifica con l’elemento l’interruttore ais x) . Progressivo crescente da 1 a n.

Descrizione: descrizione dell’interferenza. Il campo risulta modificabile (inserimento libero) e la modifica si deve riflettere su tutte le interferenze in qualsiasi stato presente a sistema;

Gravità interferenza: valore che può assumere il campo Gravità per una interferenza. In particolare, identifica la lista di valori che sono selezionabili dall’utente nel momento in cui registra l’interferenza e utilizzabili come target di raffronto nelle Policy per le relative interferenze. L’inserimento avviene nella tabella dedicata “Target/Gravità” dove l’utente può inserire un numero di righe equivalente ai possibili valori assumibili dalla Gravità dell’interferenza. Nella tabella “Target/Gravità” è possibile inserire liberamente valori alfanumerici (ad esempio arrugginimento “lieve”, “medio”, “grave”, “>30°C”, “15< valore < 30°”, ecc..) oppure un target del catalogo dei modelli target (ID catalogo modelli-target). Il valore presente nel campo è modificabile dall’utente;

Massimo tempo di validazione: indica l’intervallo temporale massimo (in giorni) per cui l’interferenza rilevata rimane in stato “In validazione”; Se necessario, l’utente può modificare il massimo tempo di validazione dell’interferenza (inserimento libero); Nel caso in cui il tempo massimo di validazione impostato sia inferiore alla data odierna, lo stato della difformità viene automaticamente modificato in “Validato”;

Misure interferenza: tabella contenente l’ID della misura o delle variabili di stato associate all’interferenza (pick-list di valori predefiniti recuperati dal catalogo misure-variabili di stato);

Caratteristiche asset: La tabella riporta la descrizione e il valore che deve assumere una certa caratteristica (Attributo) di un asset per consentire l’associazione della interferenza. I valori presenti sulla tabella sono modificabili se necessario.

Fase: per componenti costituiti da più fasi come, ad esempio, il sezionatore o l’interruttore, il campo fase deve essere compilato dall’utente in fase di inserimento per indicare la fase a cui fa riferimento l’interferenza (obbligatorio solo per componenti multifase). Il campo è un flag da valorizzare nel caso in cui sia necessario applicare le logiche di fase.

Obbligatorietà foto: flag che indica l’obbligatorietà di allegare una foto al momento della registrazione della difformità. Il flag è prevalorizzato a NO.

Modificato da: Matricola dell’utente che ha effettuato l’ultima modifica sulla difformità

Data ultima modifica: Data dell’ultima modifica sulla difformità

Attività che risolvono l’interferenza: la tabella riporta le attività risolutrici dell’interferenza ed è solo in visualizzazione

Qualunque modifica effettuata viene storicizzata con la data di modifica e la matricola dell’utente che l’ha effettuata.

La Figura 13 sotto riportata riporta i campi obbligatori (in verde) che devono essere valorizzati affinché un’interferenza possa essere aggiunta al catalogo.

Figura 13 - Catalogo interferenze, campi obbligatori (verdi) e facoltativi (giallo)

#### Stati interferenze

Le interferenze possono assumere i seguenti stati:

Bozza

Attivo

Non attivo

Una volta creata, all’interferenza viene automaticamente associato lo stato di “Bozza”. In questo stato è possibile compilare o modificare tutti campi relativi all’interferenza ed associarla a policy ed attività. Per portare l’interferenza in stato attivo è necessario compilare tutti i campi obbligatori. Questi sono:

Tipologia asset

Gerarchia anagrafica associato

ID interferenza

Descrizione

Massimo tempo di validazione

Quando l’interferenza passa in stato attivo è possibile visualizzarla nel processo di registrazione attività. Si precisa che l’attività (PdL) non può essere attivata se tutte le entità ad esse collegate non sono attive. In questo stato è possibile modificare le associazioni con le attività risolutrici oltreché le logiche di generazione.

In qualunque momento l’utente può portare lo stato dell’interferenza in “Non attivo”. L’interferenza non sarà più rilevabile da alcuna attività. Una volta in questo stato non sarà possibile eseguire alcuna operazione o modifica sull’interferenza a meno di riportarla nuovamente in stato attivo.

Catalogo modelli-target

Il catalogo riporta tutti i target di raffronto per la generazione delle policy e per il corretto funzionamento del processo di O&M. I target vengono riportati per ciascun componente e si differenziano in base al modello e/o alla tipologia di componente. L’utente può accedere ad una vista elenco (Figura 14) costituita dai seguenti campi (anche utilizzabili come filtri di ricerca):

Applicabile a

ID Target

Descrizione

Figura 14 - Catalogo modelli-target, vista elenco

Cliccando il tasto “+” è possibile aggiungere un nuovo campo modello target. Viene aperta la schermata di Figura 15 con i campi da compilare. Si può aggiungere un modello cliccando sul tasto “+” nell’apposita sezione Modelli-target o massivamente aggiungere tutti i modelli, recuperati dall’anagrafica, associati alla tipologia asset selezionata, tramite la funzionalità “Genera lista modelli” accessibile dalla barra laterale in Maximo. Si specifica che non è possibile inserire due volte lo stesso modello nella tabella modelli-target.

Per eliminare un modello target dal catalogo, è necessario cliccare sull’icona “cestino” nella vista elenco del catalogo. In alternativa, l’utente accede alla vista di dettaglio del modello target che vuole eliminare tramite la vista elenco del catalogo ed esegue l’azione cliccando sull’opzione “Elimina modello target” presente nel menu a sinistra.

Cliccando l’icona “cestino” nella tabella Modello-Target, rappresentata in Figura 15, è possibile eliminare dal catalogo un modello relativo alla tipologia di asset. Il sistema verifica se il modello sia stato eliminato dall’anagrafica, restituendo in caso contrario l’alert “Rimosso modello presente in anagrafica”.

Nella vista elenco, cliccando sul campo ID Target viene aperta la schermata rappresentata in Figura 15.

Figura 15 - Catalogo modelli-target, schermata di dettaglio

La maschera presenta i seguenti campi in testa:

ID Target: codice identificativo univoco associato ad un target. A ciascun ID Target possono essere associati diversi modelli. Tale codice identificativo del target permette di confrontare la misura rilevata rispetto al valore target presente a catalogo; è generato con la seguente logica: TRG.Classificazione.Progressivo (es. Numero di trefoli rotti conduttore TRG.COND.1, TRG.COND.2)

Descrizione target: campo a inserimento libero, descrizione del target.

Elemento asset: Elemento Asset al quale è associata il modello target. Campo popolato manualmente dall’utente tramite una lista valori contenente i seguenti elementi: “conduttore”, “fune di guardia” o “equipment di stazione”

Classificazioni asset: Tabella contenente le classificazioni alle quali è associato il modello target. La tabella è popolata automaticamente in base all’Elemento precedentemente inserito. Il rapporto Elemento - Classificazione può essere sia 1:1 (in questo caso elemento e classificazione coincidono) sia 1:N (in questo caso a un elemento sono associate più classificazioni. Es: nel caso in cui si inserisse un elemento “Interruttore” nella tabella compariranno le classificazioni “Interruttore MT ad aria compressa”, “Interruttore MT Deion”, “Interruttore MT in SF6”, etc..). Si specifica che l’utente, se lo ritiene necessario, potrà solamente eliminare alcune delle classificazioni proposte dal sistema per le quali non ritiene necessario associare il modello-target;

Nuovo valore: campo in cui inserire il valore da associare ai modelli; È possibile modificare massivamente il valore di tutti i modelli associati al target valorizzando il campo “Valore” in testata e cliccando il tasto “Propaga modifica”. (Per effettuare una modifica selettiva è necessario utilizzare la funzionalità “Modifica valore massivo” descritta in seguito);

Attributo: attributo relativo all'asset necessario per determinare il modello. I valori presenti nella colonna non sono ordinabili.

Valore attributo: valore che deve assumere attributo selezionato per la determinazione del modello. I valori presenti nella colonna non sono ordinabili.

Modello: modello associato al target. La valorizzazione del campo modello per I conduttori e FdG si deve far riferimento al campo “tipo conduttore” “tipo fune di guardia”. Per le linee in cavo non esistono target. Per gli elementi asset di stazioni il campo modello deve essere definito come una combinazione di “Codice costruttore” e “Descrizione modello” selezionabili dalle relative liste elenco contenenti tutti i valori di “Codice costruttore” e “Descrizione modello” presenti a sistema; I valori dei due attributi sono abbinabili liberamente. È previsto che il sistema informi l’utente tramite un alert dedicato nel caso in cui non venga rispettata questa regola. I valori presenti nella colonna sono definiti in modo automatico dal sistema e ordinabili in modo progressivo.

Target: Valore target associato al modello. Nel caso di elementi asset di stazioni il valore target deve essere popolato con lo stesso valore sia per l’attributo “Codice costruttore” che per “descrizione modello”, in caso contrario il sistema restituisce un errore. I valori presenti nella colonna non sono ordinabili.

È possibile modificare massivamente il valore associato ai modelli tramite la funzione “Modifica valore massivo” accessibile dalla barra laterale in Maximo. Al click viene aperta una dialog contente un campo editabile “Valore” ed un elenco di tutti i modelli associati al target. È possibile modificare il valore target dei modelli selezionando le checkbox dei modelli da modificare e, dopo aver inserito un valore nell’apposito campo, cliccando il tasto “Applica”.

A seguito di modifica del target, il DSS genera automaticamente nuove attività sulla base delle policy con target modificati.

Qualunque modifica effettuata viene storicizzata con la data di modifica e la matricola dell’utente che l’ha effettuata.

Il censimento di nuovi modelli in anagrafica non genera un inserimento automatico nel catalogo modelli-target, di conseguenza nel momento della registrazione di un nuovo modello il sistema genera un alert non bloccante che informa l’utente della necessità di aggiungere manualmente (qualora necessario) il nuovo modello nel catalogo modelli-target.

Catalogo misure e variabili di stato

Il catalogo misure e variabili di stato contiene le misure e le variabili di stato caricate a sistema. L’operatore può accedere ad una vista (Figura 16) contente i seguenti campi (anche utilizzabili come filtri di ricerca):

Applicabile a

ID Misura/variabile

Descrizione

Tipo parametro

Figura 16 - Catalogo misure e variabili di stato, vista elenco

La rilevazione delle misure o il calcolo della variabile, può essere condizionata agli attributi dell’asset. è possibile discriminare la rilevazione delle misure o il calcolo della variabile in funzione degli attributi dell’asset indicando nella tabella “Caratteristiche asset” i valori delle caratteristiche degli asset per cui è rilevabile/calcolabile. Non è, inoltre, possibile rilevare misure se non attraverso un ordine di lavoro.

Possono essere eseguite le seguenti operazioni:

Aggiunta: Un utente autorizzato può aggiungere una nuova misura o variabile di stato (cliccando il tasto “+”), compilando tutti i campi necessari. Il sistema genera un ID univoco e permette di registrare la nuova misura o variabile, con la seguente logica: Var (Variabile). Abbreviativo dell'anagrafica di riferimento. Progressivo crescente da 0 a n.

Modifica: È possibile modificare la definizione, l'unità di misura e l'associazione con l'ID del target di una variabile di stato. Se si cambia il tipo (da variabile di stato a misura o viceversa), il sistema gestisce le modifiche necessarie, come la definizione dei parametri e delle combinazioni logiche.

Eliminazione: Gli utenti autorizzati possono disattivare misure o variabili di stato. Il sistema da evidenza di eventuali incongruenze rispetto all’utilizzo di tali misure o variabili da parte di policy o altre funzioni nel CdC nella sezione descritta nel paragrafo 2.1.2.3.

Qualunque modifica effettuata viene storicizzata con la data di modifica e la matricola dell’utente che l’ha effettuata.

La pagina di dettaglio (Figura 18) è costituita dai seguenti campi:

ID Misura / Variabile: calcolato in automatico dal sistema sulla base della seguente logica in base al tipo di asset a cui è associata:

ID Linee aeree: VAR . L (Linea aerea) . n° progressivo associato al 2° liv anagrafica . n° progressivo associato al 3° livello di anagrafica . n° progressivo associato al 4° livello di anagrafica . Progressivo crescente da 1 a n (ogni valore ha come separatore il punto, esempio:VAR.L.1.1.1.1);

ID Linee in cavo: VAR . LC (Linea cavo) . n° progressivo associato al 2° liv anagrafica . n° progressivo associato al 3° livello di anagrafica . n° progressivo associato al 4° livello di anagrafica . Progressivo crescente da 1 a n (esempio: VAR.LC.3.1.1.1);

ID Stazioni: VAR . S (Stazione) . n° progressivo associato all’elemento anagrafico di riferimento (VAR.S.48.1, 48 transcodifica con l’elemento l’interruttore ais x) . Progressivo crescente da 1 a n;

Descrizione: Descrizione della misura / variabile;

Tabella “Calcolo parametri misure”: visibile ed editabile solo se tipo parametro “Variabile” e campo “calcolo” è valorizzato.

Tipologia input: solo per tipo parametro “Variabile”, presente nella tabella “Calcolo parametri misure”, valorizzato manualmente da un menu a tendina. I valori selezionabili sono dipendenti dal tipo di calcolo selezionato:

ATTIVITÀ esclusivamente se il calcolo selezionato è 'DATAOPT', 'DATAACT' o 'COUNTFORLISTASTATILISTAATTIVITAFINESTRA';

VARIABILE esclusivamente se il calcolo selezionato è ‘FORMULATESTOLIBERO’ o ‘CALCULATEDAILY’;

ATTRIBUTO esclusivamente se il calcolo selezionato è ‘DIFFANNI’;

SOGLIA esclusivamente se il calcolo selezionato è ‘FORMULATESTOLIBERO’;

GRAVITAOPERAINTERFERENTE esclusivamente se il calcolo è COUNTOPERAINTERF.

Input: campo presente nella tabella “Calcolo parametri misure”, codice dell’input tramite il quale viene calcolata la variabile, scelto da una lista valori in funzione del valore inserito nel campo “Tipologia input” (deve fare riferimento all’asset specificato nel campo “Asset”). È possibile selezionare input multipli da utilizzare nel campo “Calcolo” se assume il valore ‘FORMULATESTOLIBERO’ per la definizione della logica di elaborazione della variabile;

Tipologia Asset: campo popolato tramite lista valori con l’elemento EAM associato all’oggetto di catalogo, sotto è presente la tabella auto popolata in funzione dell’elemento scelto che contiene le classificazioni (e la relativa gerarchia). È possibile eliminare dalla lista delle classificazioni associate all’elemento alcuni record;

Calcolo: visibile, modificabile e obbligatorio solo se tipo parametro uguale “Variabile”. Riporta la modalità di calcolo della variabile. Il campo è popolabile selezionando un calcolo da una lista valori predefinita. I possibili calcoli presentati nella lista del campo sono i seguenti:

COUNT: utilizzabile solo per input di tipo “Attività”, calcola l’ammontare di attività presenti a sistema con il codice ID specificato nel campo “Input” in stato;

DATAACT: calcola la differenza in giorni tra la data attuale e la data di esecuzione dell’ultimo ordine associato all’attività inserita nel campo “Input”;

DATAOPT: calcola la differenza in giorni tra la prossima data pianificata inizio dell’attività indicata nel campo “Input” e la data attuale. Vengono prese in considerazione tutte le attività in stato precedente a “Eseguito parzialmente”;

DIFFANNI: calcola la differenza in anni tra la data di installazione e l’anno in corso;

FORMULATESTOLIBERO: selezionando questa opzione si presenta un campo a maschera dove l’utente può inserire una formula a testo libero;

MAX: selezionando questa opzione si possono aggiungere come input esclusivamente variabili/misure;

MAXHICONDUTTORI: calcola il massimo tra gli HI dei conduttore (quarto livello) sotto un conduttori (terzo livello);

COUNTFORLISTSTATIATTIVITAFINESTRA: conteggia il numero di attività (della tipologia indicata nel campo “input”) in uno stato configurabile da GUI (nel campo “Stato” della tabella “Calcolo parametro misura”) in una specifica finestra temporale considerata come giorni nel passato (indicata nel campo “periodo di calcolo”);

CALCULATEDAILY: calcola la differenza tra due valori della variabile (storicizzati) in uno specifico intervallo temporale indicato nel campo “periodo di calcolo”;

GESTISCIEVENTOESEGUITO: All'esecuzione della lista di attività in input il sistema associa alla variabile in output il valore dell'unica variabile in input. Per configurare variabili che sfruttano questo calcolo, è necessario inserire in input una sola variabile e una o n attività.

COUNTOPERAINTERF: calcola il numero di opere interferenti presenti a sistema sotto la campata. Per indicare se le opere interferenti calcolate sono solo quelle con gravità “Alta”, si aggiunge nella tabella “Calcolo parametri misure” la tipologia input “GRAVITAOPERAINTERFERENTE” e si indica nel campo “Input” il valore “Alta” (unico valore selezionabile).

La formula di testo libero permette di inserire nuove formule di calcolo attraverso gli operatori e i vincoli elencati di seguito:

Ogni misura in input deve avere il prefisso VAR; ogni soglia deve avere il prefisso TRG; non possono essere selezionate altre tipologie di input;

Gli operatori previsti sono *, +, - , /, ^, min, max.

L’espressione della formula non può superare i 3500 caratteri compresi eventuali separatori es. lo spazio.

Stato: campo visibile nella tabella “Calcolo parametri misure” solo se è stato scelto nel campo “Calcolo” la funzione “countForListStatiAttivitaFinestra”. È popolabile tramite una lista valori che comprende le seguenti voci: “Eseguito”; “Aperto”; “Eseguito parzialmente”; “Da validare”; “Chiuso”; “Schedulabile”; “Schedulato”; “Eseguibile”; “Inoltrato”; “Inviato”; “Avviato”; “Non eseguito”; “Non validato”.

Periodo di calcolo: visibile e modificabile quando il tipo parametro è uguale a “Variabile”. Riporta il valore numerico che indica il periodo da considerare per il calcolo della variabile, ad esempio indicando 365 giorni verrà considerato l’anno precedente come perimetro di calcolo della variabile;

Reset?: flag (SI/NO) che definisce se è necessario eseguire o meno il reset della variabile. Il campo appare solo se il tipo parametro è uguale a “Variabile di stato”;

Reset parametri misure: tabella contente l’elenco di attività che determina il reset della variabile di stato, editabile e visibile solo se il campo “Reset?” viene valorizzato con “SI”. È possibile specificare una o più attività che devono innescare il reset della variabile aggiungendo righe nella tabella e selezionando nel campo “Reset” della singola riga il PdL di interesse (sempre riferito all’asset a cui è associata la variabile). L’evento di reset dipende dal tipo di calcolo a cui è applicato:

DATAACT l’evento è il passaggio in stato “Eseguito” di un ordine associato all’asset con PdL presente nella tabella reset parametri misure;

FORMULATESTOLIBERO l’evento è il passaggio in stato “Eseguito” di un ordine associato all’asset con PdL presente nella tabella reset parametri misure.

Non è possibile inserire il reset per altre tipologie di calcolo

Tipo Parametro: la tipologia di misura o variabile di stato (lista di valori da cui è possibile selezionare solamente “variabile di stato” o “misura”);

Attività che rilevano la misura: tabella contente i PdL sui quali è indicata come rilevabile la specifica misura. La tabella è in sola lettura, la configurazione avviene attraverso il catalogo attività sul singolo PdL. Le informazioni visualizzate sono: Codice del PdL; Descrizione del PdL. La tabella è visibile esclusivamente per Tipologia parametro “Misura”.

Micro-controlli che rilevano la misura: tabella contente i micro-controlli sui quali è indicata come rilevabile la specifica misura. La tabella è in sola lettura, la configurazione avviene attraverso il catalogo attività sul singolo micro-controllo. Le informazioni visualizzate sono: Codice del micro-controllo; Descrizione del micro-controllo. La tabella è visibile esclusivamente per Tipologia parametro “Misura”.

Unità di misura: unità di misura associata al parametro (lista di valori contenente: millisec, secondi, ore, giorni, anni, A, l/s, avv/giorno, n°, 0:Falso 1:Vero (solo per variabili statiche) , kA, Nm, V, °C, mA, uA, ppm, data, % in peso, %. L’elenco delle unità di misura è integrabile con ulteriori valori dall’utente tramite il relativo dominio.

Caratteristiche asset: La tabella riporta il codice dell’attributo, la descrizione e il valore che deve assumere una certa caratteristica (Attributo) strettamente dell’asset a cui è associata la misura/variabile in oggetto (sono inclusi i 18 campi di testata selezionabili, i medesimi già utilizzati per le policy) per poter essere rilevata sul campo (per le misure) o calcolate (per le variabili). Le variabili non vengono calcolate (quindi non viene scritto un record nella tabella delle variabili calcolate) sugli asset per cui non sono rispettate le condizioni associate nelle caratteristiche asset. Nella pick list viene mostrato sempre una sola riga per attributo anche se è presente su più classificazioni. È possibile selezionare solo attributi i cui valori sono contenuti in una lista valori tra i domini definiti a sistema. Una volta selezionato l’attributo è possibile selezionare il valore. Per aggiungere un ulteriore caratteristica è necessario aggiungere una riga alla tabella. Le righe che fanno riferimento alla stessa caratteristica ma con valori diversi vengono considerate in combinazione OR. Le righe con caratteristiche diverse vengono considerate in combinazione AND. Nel caso in cui non siano state indicate caratteristiche per cui la misura o la variabile risultino rilevabili/calcolabili, il sistema rende rilevabile/calcolabile la misura o la variabile per tutti gli asset senza applicare alcun filtro.

Figura - Catalogo misure e variabili di stato, schermata di dettaglio

Il calcolo delle variabili di stato viene eseguito automaticamente dal sistema al primo inserimento della variabile a catalogo e a seguito di determinati eventi che variano a seconda della definizione dell’Input della variabile. In particolare, se:

input attività: ad ogni creazione di un’attività del tipo in Input o al cambio di stato;

input misure: ad ogni modifica della misura inserita in Input;

input attributi: ad ogni variazione dell’attributo in Input relativamente all’asset inserito nel campo “Asset”.

La Figura 19 mostra i campi obbligatori (in verde) da compilare, affinché una nuova misura/variabile di stato sia salvata.

Figura - Catalogo misure e variabili di stato, campi obbligatori (verdi), facoltativi (giallo) e non presenti (rosso)

Catalogo micro-controlli

Il catalogo dei micro-controlli riporta tutti i micro-controlli associabili ai piani di lavoro/ordini di lavoro.

L’utente tramite l’applicazione di gestione del catalogo dei micro-controlli può accedere ad una vista elenco del catalogo micro-controlli (Figura 20) costituita dai seguenti campi (anche utilizzabili come filtri di ricerca):

Applicabile a

ID micro-controllo

Assieme

Sottoassieme

Descrizione

Figura - Catalogo micro-controlli, vista elenco

Cliccando sul campo ID microcontrollo viene aperta la pagina di dettaglio (Figura 21) costituita dai seguenti campi:

Controllo: tipologia controllo, scelto da una lista valori contente le seguenti voci: A, B, CPA, OCC.

ID Microcontrollo: codice identificativo micro-controllo (popolato automaticamente dal sistema al primo salvataggio); La logica di generazione è a struttura fissa: “valore del controllo”.”Progressivo numerico corrispondente all’asset a cui fa riferimento il micro-controllo”.”progressivo numerico” (esempio: Controllo di tipo B su sezionatore ais: CTB.49.14, CTB.49.30 ecc.., Controllo di tipo A riferito al condensatore: CPA.91.1, CPA.92.1 ecc..)

Descrizione micro-controllo: descrizione micro-controllo (campo libero);

Elemento: Elemento Asset al quale è associato il micro-controllo. Campo popolato manualmente dall’utente tramite una lista valori contenente tutti gli elementi asset presenti a sistema;

Classificazione: Tabella contenente le classificazioni alle quali è associato il micro-controllo. La tabella è popolata automaticamente in base all’Elemento precedentemente inserito. Il rapporto Elemento - Classificazione può essere sia 1:1 (in questo caso elemento e classificazione coincidono) sia 1:N (in questo caso a un elemento sono associate più classificazioni. Es: nel caso in cui si inserisse un elemento “Interruttore” nella tabella compariranno le classificazioni “Interruttore MT ad aria compressa”, “Interruttore MT Deion”, “Interruttore MT in SF6”, etc..). Si specifica che l’utente, se lo ritiene necessario, potrà solamente eliminare alcune delle classificazioni proposte dal sistema per le quali non ritiene necessario associare il micro-controllo.

Flag “Misure obbligatorie?”: flag modificabile dall’utente, che indica se tutte le misure rilevabili con il micro-controllo sono obbligatorie (SI) o se non sono obbligatorie (NO). Se il flag è a SI l’utente in fase di esecuzione deve obbligatoriamente inserire un valore per ogni misura associata al micro-controllo se non inserite il sistema avvisa l’utente con un messaggio di errore e non permette l’esecuzione del microcontrollo da mobile. Tramite web, all’atto dell’esecuzione del singolo micro-controllo (attraverso l’icona del “semaforo”) il sistema esegue il controllo bloccante per verificare che siano state rilevate le misure segnalate come obbligatorie. Se il controllo fallisce (manca una o più di una misura obbligatoria) il sistema restituisce all’utente il seguente messaggio di errore “inserire le misure obbligatorie previste per il micro-controllo prima di esitarlo” impedendo il completamento dell’azione di esitazione. Se il flag è a NO, l’utente in fase di esecuzione del micro-controllo può scegliere se rilevarle o meno le misure associate al micro-controllo a catalogo. Se il flag viene attivato ma non sono associate misure al micro-controllo, di fatto non viene valutata l’obbligatorietà dal sistema quindi il micro-controllo non ha misure obbligatorie;

Assieme: campo per la clusterizzazione. Valorizzato sulla base dell’asset di 4° livello associato al micro-controllo (tabella di relazione recuperata dai domini);

Sottoassieme: campo per la clusterizzazione. Valorizzato sulla base dell’Assieme e dell’asset di 4° livello associato al micro-controllo (tabella di relazione recuperata dai domini);

Difformità rilevabili: pick-list di difformità rilevabili dal micro-controllo, filtrate per tipologia asset;

Interferenze rilevabili: pick-list di interferenze rilevabili dal micro-controllo, filtrate per tipologia asset;

Misure rilevabili: pick-list delle misure (solo quelle di tipologia parametro MISURA nel catalogo misure/variabili di stato) rilevabili tramite quel micro-controllo, filtrate per tipologia asset (non sono selezionabili misure relativi a tipologie asset differenti da quella del micro-controllo). Tutte le misure selezionate nella tabella risultano essere obbligatorie all’atto dell’esecuzione del micro-controllo, esclusivamente se viene attivato il flag “misure obbligatorie?” sul micro-controllo a catalogo. Le misure proposte all’utente in fase di esecuzione del micro-controllo sono esclusivamente quelle che da catalogo risultano rilevabili con il micro-controllo in esecuzione sull’asset e sulla base delle sue caratteristiche rispetto a quanto indicato nel catalogo delle misure in termini di rilevabilità per caratteristiche. Se non sono state associate misure rilevabili al micro-controllo, sono rilevabili tutte le misure possibili per quell’asset (sempre in conformità con le logiche di rilevazione delle caratteristiche asset indicate a catalogo misure).

Caratteristica asset: consente di specificare eventuali caratteristiche (attributi e relativi valori) che l’asset deve possedere per poter essere associato al micro-controllo in fase di generazione ordine da parte del DSS. Se non popolata i micro-controlli vengono generati per tutti gli asset della tipologia associata al micro-controllo;

Al micro-controllo sono associate le difformità/interferenze rilevabili e anche le caratteristiche dell'asset con il relativo valore per i quali si applica quel micro-controllo. È possibile associare nuove difformità, interferenze e caratteristiche al micro-controllo cliccando sul tasto “+” nelle rispettive tabelle.

Figura - Catalogo Micro-controlli, schermata di dettaglio

Qualunque modifica effettuata viene storicizzata con la data di modifica e la matricola dell’utente che l’ha effettuata.

La Figura 22 mostra i campi obbligatori (in verde) da compilare, affinché un nuovo micro-controllo sia salvato.

Figura - Catalogo micro-controlli, campi obbligatori (verdi) e facoltativi (giallo)

#### Generazione micro-controlli

In fase di generazione dell’OdL, e quindi di associazione dei micro-controlli, oltre alla classificazione dell’asset (es: Interruttore) verranno verificate anche alcune specifiche tecniche dello stesso (se indicate nel catalogo, altrimenti vengono generati per tutti gli asset a prescindere dalle caratteritische dello stesso) (es: Isolamento). In questo modo all’interno dell’OdL saranno presenti solo i micro-controlli specifici dell’asset che possiede una determinata caratteristica tecnica. Di seguito si riporta un esempio per rendere più chiara la regola sopra descritta: nel momento in cui vengono emessi i micro-controlli per un Interruttore il sistema verificherà il tipo di interruttore in questione leggendo la specifica tecnica “isolamento”:

Se interruttore isolato in SF6: il sistema emette i micro-controlli relativi ai soli interruttori isolati in SF6

Se interruttore isolato in olio: il sistema emette i micro-controlli relativi ai soli interruttori isolati in olio

Catalogo indicatori di condizione tecnica

In questo paragrafo viene descritto il processo di inserimento a sistema degli indicatori di condizione tecnica o HI (Health Index, tasso di guasto, tasso di anomalia, HI di degrado conduttore, HI degrado fune di guardia,). Il calcolo degli indicatori viene eseguito tramite l’applicazione standard di MAS “Health” (il cui funzionamento e inizializzazione sono descritti in AF4).

La definizione degli indicatori di condizione tecnica all’interno del modulo “Health” deve essere effettuata specificando gli asset di riferimento e la formula (o le formule) per il calcolo, con la possibilità di utilizzare ogni attributo presente in MAS che sia collegabile logicamente all’asset.

A livello logico, il modulo Health si basa sull’uso dei seguenti elementi, che di fatto costituiscono i cataloghi da compilare per definire correttamente un indicatore di condizione tecnica:

Scoring groups: utilizzati per definire un insieme di Asset che deve essere oggetto del calcolo dell’indicatore di condizione. La selezione del gruppo di asset di interesse avviene attraverso l’uso di query dedicate, selezionabili nella sezione “Scored Assets” del gruppo, evidenziata in Figura 23. A sistema è presente un set di query predefinito (ad esempio “tutti i conduttori”) al quale l’utente può aggiungere ulteriori query definite a seconda delle esigenze specifiche. In particolare, è possibile definire nuove query di selezione degli Asset utilizzando l’applicazione “Asset” del modulo “Manage”: l’utente definisce il perimetro di Asset tramite i filtri nella sezione di ricerca (applicabili a tutti gli attributi degli Asset) e successivamente salva la query di ricerca definita sistema, attribuendole una denominazione specifica. Una volta creata nel modulo “Manage”, la query (identificata dalla denominazione definita dall’utente) risulterà selezionabile nella sezione “Scoring Assets” degli Scoring group nel modulo “Health”. Dopo aver selezionato l’insieme di Asset desiderato, l’utente può associarvi uno “Score”, ovvero un indicatore di condizione.

Figura - Test score group

Scores: corrisponde all’indicatore di condizione, definito utilizzando un set di “Contributors”, ossia di formule alle quali l’utente può associare un peso. All’interno di uno Score è possibile inserire una o N formule “Contributors” i cui risultati saranno combinati secondo i pesi indicati dall’utente per determinare l’indicatore di condizione complessivo (nel caso in cui non venga specificato il peso, le formule vengono combinate linearmente). Inoltre, l’utente può definire dei range di qualità dell’indice utili ad evidenziare il livello di qualità nel quale ricadono gli Asset oggetto del controllo. L’informazione del range di qualità, oltre ad altre metriche di analisi, è visualizzabile nella sezione “Test scoring group” dello scoring group (Figura 24).

Figura - Test scoring group

Contributors: formule di calcolo dell’indicatore di condizione, vengono definite utilizzando la sintassi specifica di Maximo. L’utente può utilizzare qualsiasi attributo presente nel modulo “Manage” (compresi i dati storici di OdL, difformità, anomalie) ed inserire parametri numerici per caratterizzare la formula (ad esempio pesi di ciascun addendo in una sommatoria). Le formule sono inseribili e editabili liberamente dagli utenti abilitati combinando gli attributi desiderati utilizzando parametri ed operatori logici (sarà compito dell’utente scrivere all’interno delle formule dei contributors i pesi di ciascun addendo per esteso e andarli ad aggiornare). Il sistema effettua automaticamente un controllo di sintassi della formula impedendone il salvataggio in caso di formattazione errata. Per modificare una formula, è necessario disattivare l’indicatore di condizione (Score) alla quale è associata tramite il pulsante “Active” presente nella maschera dello Score e successivamente riattivarlo una volta completata la modifica. Il calcolo dell’indicatore modificato avviene a valle della riattivazione e può essere eseguito sia manualmente tramite la funzionalità “Esegui calcolo indicatore” presente nello Score, oppure automaticamente durante i cicli di calcolo giornalieri.

Il processo di inserimento di un nuovo indicatore di condizione tecnica deve essere effettuato inserendo in quest’ordine:

Contributors: devono essere definite le formule che contribuiscono al calcolo dell’indicatore di condizione. Le formule possono a loro volta essere indicatori di condizione da combinare per definire un indicatore di condizione (Score). Le formule (Figura 25) devono essere inserite utilizzando la sintassi specifica di Maximo, per la quale verrà fornito un manuale operativo. Ad esempio, si riporta la definizione in Health dell’indicatore di condizione “Componenti cavo” con evidenza dello “Score” e dei “Contributors”, dove questi ultimi corrispondono a HI combinati a livello di “Score” per definire l’Hi “Componenti cavo”;

Figura - Formule calcolo HI

Score: viene definito l’indicatore di condizione inserendo i “Contributors” necessari e i relativi pesi nella sezione “Contributors”. È possibile attivare o disattivare in qualsiasi momento l’indicatore di condizione definito tramite l’apposito pulsante “Active”. Il calcolo di tutti gli Score definiti a sistema viene eseguito automaticamente dal sistema con una frequenza giornaliera. Inoltre, è possibile avviare on-demand il calcolo di singoli Score utilizzando il pulsante “Esegui calcolo indicatore” presente all’interno della maschera di dettaglio dello Score. In questo caso, il calcolo dello Score sarà effettuato per tutti gli Scoring Group ai quali è associato;

Scoring group: l’utente definisce lo “Scoring group” selezionando lo “Score” (indicatore di condizione) fra quelli definiti a sistema e il perimetro di Asset ai quali deve essere applicato. Si specifica che è possibile applicare gli Indicatori di condizione a qualsiasi livello di Asset presente a sistema. All’interno della maschera di dettaglio dello Scoring Group, è possibile avviare on-demand il calcolo dell’indicatore di condizione per lo Scoring group in questione utilizzando il pulsante “Esegui calcolo indicatore”. In questo caso il calcolo dell’indicatore sarà eseguito solamente per gli asset associati allo Scoring Group selezionato.

#### Calcolo degli indicatori di condizione tecnica

In questo paragrafo sono descritte le formule utilizzate nella definizione degli indicatori di condizione tecnica gestite in EAM:

Tasso di guasto: Il tasso di guasto è calcolato con riferimento all’intera famiglia di asset di una tipologia. Il sistema deve calcolare il tasso di guasto per i componenti di quarto livello delle linee e stazioni secondo la seguente regola:

Ii = La sommatoria, in un lasso di tempo determinato, dei guasti occorsi su quel tipo di componente;

Ci = La sommatoria, nello stesso lasso di tempo determinato, della consistenza (numerosità) del componente per 100 km di terne (o per 100 componenti).

TdG = [∑(i=2000-2022) Ii / ∑(i=2000-2022) Ci] * 100

Per quanto riguarda il perimetro di applicazione del tasso di guasto, la formula è applicabile per ogni gruppo di asset di quarto livello specificato negli scoring group con i seguenti attributi anagrafici a seconda della tipologia di asset:

Conduttore:

LIN_CONDUTTORE_AEREO (PMT_TIPO_CONDUT)

LIN_TIPO_CONDUTTORE_AEREO (PMT_DESCR_TIPO_CONDUT)

Fune di guardia:

LIN_TIPO_FUNE_GUARDIA (PMT_TIPO_CDG)

Isolatori conduttore:

LIN_TIPO_ISOLATORI_LIN (PMT_TIPO_ISOL_LINEA)

Isolatori Fune di guardia:

LIN_TIPO_ISOLATORI (PMT_TIPO_ISOL_CDG)

Struttura:

LIN_GEOMETRIA_SOSTEGNO (PMT_GEOM_SOST)

LIN_STRUTTURA_SOSTEGNO (PMT_TIPO_STRUTT_SOST)

LIN_TIPO_FONDAZIONI (PMT_TIPO_FOND)

Componenti di IV livello stazione:

Campo classe: (ZCLASSE)

Campo costruttore: (HERST)

Campo modello:(ZTYPBZ)

Tasso di anomalia: Il sistema deve calcolare il tasso di guasto per i componenti di quarto livello delle linee e stazione secondo la seguente regola:

Ai = La sommatoria, in un lasso di tempo determinato, delle anomalie occorse su quel tipo di componente;

Ci = La sommatoria, nello stesso lasso di tempo determinato, della consistenza (numerosità) del componente per 100 km di terne (o per 100 componenti).

TdA = (∑(i=2000-2022) Ai / ∑(i=2000-2022) Ci) * 100

Per quanto riguarda il perimetro di applicazione del tasso di anomalia, la formula è applicabile per ogni asset di quarto livello con i seguenti attributi anagrafici a seconda della tipologia di asset:

Conduttori:

LIN_COND_AEREO (PMT_TIPO_CONDUT)

LIN_TIPO_CONDUTTORE_AEREO (PMT_DESCR_TIPO_CONDUT)

Fune di guardia:

LIN_TIPO_FUNE_GUARDIA (PMT_TIPO_CDG)

Isolatori conduttore:

LIN_TIPO_ISOLATORI_LIN (PMT_TIPO_ISOL_LINEA)

Isolatori Fune di guardia:

LIN_TIPO_ISOLATORI (PMT_TIPO_ISOL_CDG)

Struttura:

LIN_GEOMETRIA_SOSTEGNO (PMT_GEOM_SOST)

LIN_STRUTTURA_SOSTEGNO (PMT_TIPO_STRUTT_SOST)

LIN_TIPO_FONDAZIONI (PMT_TIPO_FOND)

Componenti di IV livello stazione:

Campo classe: (ZCLASSE)

Campo costruttore: (HERST)

Campo modello:(ZTYPBZ)

Health index linee aeree: Il sistema deve calcolare il Health index per i seguenti componenti: Conduttori; Fune di guardia; Sostegni. Le formule che definiscono gli indicatori sono di seguito descritte:

Conduttori e fune di guardia: Il Health index è definito dalla soma dei tre seguenti termini:

HI vetustà = (vetustà (anni) / vita max.) * 30

HI degrado = ∑ (Difformità * Peso) / (cap max * conduttori del Fascio) * 30

HI attività = ∑ (Pesi HI attività aperte)

Sostegni: Il Health index è definito dalla soma dei tre seguenti termini:

HI vetustà = (vetustà (anni) / vita max.) * 60

HI attività = (∑ peso attività rilevanti / cap max) * 30

HI tasso di guasto = [TdG medio tipologia (2000÷anno n-1) / TdG medio max. (2000÷anno n-1)] * 10

Health index linee in cavo: Il sistema deve calcolare il Health index per i seguenti componenti: Cavi AT; Giunto cavo; Terminali cavo. La formula che definisce gli indicatori è di seguito descritta:

Cavi AT, Giunto cavo, Terminali cavo: La formula che definisce l’HI è data dalla somma di tre termini. I pesi sono gestiti tramite le apposite tabelle gestite in EAM, definite per ogni tipologia di componente.

HI vetustà = (vetustà (anni) / vita max.) * 60

HI tecnologico = (peso associato alla caratteristica sul tipo di isolamento / max cap) * 20

HI operativo = (peso anom.aperte + peso anom.chiuse)+(peso campagne ap. + peso campagne ch.) *20 /max cap

Health index stazioni “in blindato”: Il sistema deve calcolare l’HI per gli asset di stazioni tramite la somma dei seguenti termini:

HI vetustà = f(vetustà-k);

HI difformità = ∑ (pesi diff.chiuse)+∑(pesi diff.aperte);

HI attività= Max (peso attività aperte)

HI tipologia: uguale a 10 per i componenti dei modelli/famiglie individuati come "da attenzionare"; uguale a 0 per tutte le altre tipologie di componenti.

Health index Macchinari: Il sistema deve realizzare il calcolo dell’HI dei macchinari. La formula è definita dalla somma di tutti i termini di seguito riportati, divisi per 15.

HI dielettrico: parametro dipendente dalle analisi DGA dell'olio

HI termico: parametro dipendente dalle analisi dei furani e dell'ossido di carbonio

HI olio: parametro dipendente dalle proprietà chimico-fisiche dell'olio

HI meccanico: parametro dipendente dalle misure elettriche

#### Scheda di condizione tecnica

L’utente potrà consultare lo storico degli indicatori di condizione tecnica dell’Asset tramite l’applicazione “Asset” del modulo “Manage” di EAM. Per accedere a questa funzionalità, l’utente deve selezionare l’applicazione “Asset” dal menu della barra laterale sinistra. In questa applicazione, l’utente ha la possibilità di filtrare gli asset per caratteristiche come l’ID Asset desiderato, Tipologia di asset, Dipartimento / Unità impianti.

Selezionando l’ID dell’asset desiderato, l’utente accede alla maschera con le informazioni specifiche dell’asset di interesse. In questa maschera sono presenti i seguenti tab

Tab “Asset”: l’utente può visualizzare le seguenti informazioni:

Assetnum e descrizione dell’asset

Classificazione dell’asset

SAP ID dell’asset

Stato dell’asset

Data di installazione dell’asset

Tab “Attributi di anagrafica”: L’utente può visualizzare tutti gli attributi e caratteristiche specifiche dell’asset e il relativo SAP ID,

Tab “Attività associate”: l’utente può visualizzare una tabella contenente tutti gli ordini di lavoro associati all’asset e il relativo SAP ID. La tabella risulta composta dalle seguenti colonne:

ID OdL

Descrizione OdL

ID PdL

Stato

Data pianificata inizio

Data pianificata fine

Data chiusura

Tab “Difformità e interferenze”: L’utente può visualizzare tutte le difformità e interferenze associate all’asset e il relativo SAP ID. Sono presenti 2 tabelle (una per le difformità e una per le interferenze), contenenti le seguenti informazioni:

ID difformità/interferenze

Descrizione difformità/interferenza

Stato

Data prima rilevazione

Data ultima rilevazione

Gravità

Tab “Opere interferenti”: tab presente solo per gli asset con classificazione LINEA, CAMPATA e TRATTA CAVO. L’utente può visualizzare l’elenco di tutte le opere interferenti sottostanti all’asset selezionato e il relativo SAP ID. Tramite apposito pulsante, per ogni opera interferente è possibile vedere le relative specifiche tecniche.

Tab “Indicatori di condizione tecnica”: L’utente può visualizzare la descrizione e il valore di tutti gli indicatori associati all’asset e il relativo SAP ID. Tramite apposito tasto in prossimità dell’indicatore è possibile visualizzare lo storico degli HI in una dialog contenente la descrizione dell’indicatore, il valore e la data di aggiornamento dell’indicatore.

Tab “Misure e variabili di stato”: L’utente può visualizzare tutte le misure e variabili di stato associate all’asset e il relativo SAP ID. Sono presenti 2 tabelle (una per le misure e una per le variabili di stato) contenenti le seguenti informazioni:

ID misura/variabile

Descrizione misura/variabile

Data ultima rilevazione

Valore

Unità di misura

Tramite apposito tasto in prossimità della variabile è possibile visualizzare lo storico delle variabili in una dialog contenente la descrizione della variabile, il valore e la data di aggiornamento della variabile.

Tab “Microcontrolli”: tab visibile solo per gli asset di 4 livello stazione. Viene riportato l’elenco di tutti i micro-controlli pianificati ed eseguiti sull’asset specifico.

Tab “Documentazione”: tab all’interno del quale è possibile allegare documenti.

È possibile consultare anche nell’App mobile EAM le misure, variabili di stato e HI associati agli asset (oltre all’elenco di OdL, difformità e attributi associati all’asset già dettagliati nella descrizione dell’app mobile in AF3).

Catalogo delle attività

Nel modello Terna, le macro-attività/attività rappresentano l’entità centrale del processo di manutenzione (che include la gestione di attività di monitoraggio, sostituzione, manutenzione) che sarà gestito in EAM.

Le attività sono rappresentate dall’associazione di un workflow (sequenza di metodi) ad una macro-attività. I metodi sono caratterizzati da attributi che ne definiscono i dettagli in termini di risorse, attrezzature e durate necessarie per espletarli. Risulta importante sottolineare che nella soluzione proposta i concetti di macro-attività e attività confluiranno in un unico oggetto (Piano di Lavoro).

#### Visualizzazione e navigazione del catalogo delle attività

Il catalogo delle attività viene mostrato come un elenco di attività (PdL), con la possibilità di effettuare ricerche avanzate applicando filtri sui campi di testata (Figura 26). I filtri visibili sulla schermata possono essere configurati a seconda delle necessità specifiche in fase di implementazione.

Attualmente i filtri di testata configurati sono:

ID attività;

Descrizione;

Revisione del PdL;

Tipologia attività;

Tipologia asset;

Stato.

Oltre ai filtri di testata è anche possibile utilizzare una schermata di filtri avanzati che permette di utilizzare come filtro qualsiasi campo presente sul PdL tramite la funzionalità “Altri campi di ricerca”.

Figura - Catalogo Attività

L’accesso e l’operatività sul catalogo e sulle attività contenute in esso è definito sulla base di abilitazioni a sistema determinate dall’appartenenza dell’utente ad un determinato gruppo. In funzione del mapping degli utenti al momento disponibile, è possibile identificare alcuni utenti riconducibili al GdL2 e GdL3 come quelli abilitati in lettura e scrittura, mentre la restante parte degli utenti risulterà abilitata in sola lettura.

In funzione della gerarchia degli asset associata all’attività, la lista può essere pre-filtrata (Figura 27) per mostrare tutte le attività relative a:

Linee Aeree

Linee in Cavo

Stazioni

Sedi tecniche integrative

Figura - Filtro per mostrare le attività in base alla gerarchia degli asset associata all’attività

#### Struttura delle attività

La struttura dell’attività è modellata in Maximo tramite il Piano di Lavoro (PdL) all’interno del quale sono definiti dei Job task. Come illustrato in Figura 28, il Piano di lavoro contiene al suo interno tutte le informazioni legate all’attività (tipologia attività, difformità/interferenze risolte e rilevabili, livello anagrafico associato ecc…), il workflow di metodi e i micro-controlli associati all’attività.

Sia i metodi che i micro-controlli sono modellizzati attraverso un oggetto Maximo chiamato Job Task. Questo permette di gestire entrambi come oggetti strettamente connessi al PdL a cui sono associati e di caratterizzarlo attraverso le informazioni riportate.

Le due tipologie di Job task (metodi e micro-controlli) sono visualizzate e gestite separatamente (Figura 29). Inoltre, al fine di differenziare anche a livello di database metodi e micro-controlli, i Job task corrispondenti alle due tipologie sono caratterizzati attraverso un attributo di classificazione dedicato.

Figura - Struttura attività

Figura - Gestione differenziata Metodi e Micro-controlli

#### Informazioni che caratterizzano le attività

Le informazioni relative al PdL (attività) sono organizzate in diversi tab, ognuno dei quali contiene un determinato set di campi:

Tab “Attività” (Figura 30): vengono riportate tutte le informazioni anagrafiche relative alla tipologia di attività presa in considerazione.

Figura - Tab Attività

Ogni attività presente nel catalogo delle attività rappresenta un’azione di monitoraggio, sostituzione o manutenzione, identificata da un codice univoco e riferita a una specifica tipologia di asset di I, II, III o IV livello.

La tipologia di attività caratterizza le informazioni associate alle attività nel catalogo; quelle previste sono le seguenti:

Controlli occasionali;

Controlli periodici;

Controlli tecnici;

Controlli su condizione;

Manutenzioni su condizione;

Sostituzioni;

Controlli di legge;

Nuove costruzioni / installazioni;

Demolizione

ANR: il codice dell’attività è costruito come segue AT.X.0.0.0.n.m, dove n è il progressivo della macro-attività e m il progressivo dell’attività settato dal sistema (il codice della macro-attività AT.X.0.0.0.n è inserito dall’utente al momento dell’inserimento nel dominio macro-attività);

Altre attività: il codice dell’attività è costruito come segue AT.AT.0.0.0.n.m, dove n è il progressivo della macro-attività e m il progressivo dell’attività settato dal sistema (il codice della macro-attività AT.AT.0.0.0.n è inserito dall’utente al momento dell’inserimento nel dominio macro-attività);

Attività non operative: il codice dell’attività è costruito come segue AT.X.0.0.0.n.m, dove n è il progressivo della macro-attività e m il progressivo dell’attività settato dal sistema (il codice della macro-attività AT.X.0.0.0.n è inserito dall’utente al momento dell’inserimento nel dominio macro-attività);

Pronto intervento

In generale, le informazioni cambiano in funzione della tipologia dell’attività. Si riporta di seguito lo specchietto riepilogativo contente le informazioni obbligatorie (O), facoltative (F) e non presenti (NP) in base alla tipologia di attività trattata.

|  | Controlli occasionali | Controlli periodici | Controlli tecnici | Controlli su condiz. | Manutenz. su condiz. | Sostituzione | Controlli di legge | Nuova costr./ Install. | Demolizione | ANR | Altre attività | Attività non operative | Pronto intervento |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Elemento anagrafico associato | O | O | O | O | O | O | O | O | O | O | O | O | O |
| Tabella di associazione classificazione | O | O | O | O | O | O | O | O | O | NP | NP | NP | NP |
| Gerarchia anagrafica associata | O | O | O | O | O | O | O | O | O | NP | NP | NP | NP |
| Unità di misura | O | O | O | O | O | O | O | O | O | F | F | F | F |
| Assieme e sottoassieme | F | F | F | F | F | F | F | F | F | F | F | F | F |
| Livello attività | O | O | O | O | O | O | O | O | O | O | O | O | O |
| ID attività | O | O | O | O | O | O | O | O | O | O | O | O | O |
| Descrizione attività | O | O | O | O | O | O | O | O | O | O | O | O | O |
| ID macro-attività | O | O | O | O | O | O | O | O | O | O | O | O | O |
| Attività prevalente per il metodo | O | O | O | O | O | O | O | O | O | O | O | O | NP |
| Attributo per selezione metodo | F | F | F | F | F | F | F | F | F | F | F | F | NP |
| Valore target attributo | F | F | F | F | F | F | F | F | F | F | F | F | NP |
| Tipologia attività | O | O | O | O | O | O | O | O | O | O | O | O | O |
| Indisponibilità | O | O | O | O | O | O | O | O | O | NP | NP | NP | O |
| Durata pianificata | O | O | O | O | O | O | O | O | O | O | O | O | O |
| Effort | O | O | O | O | O | O | O | O | O | F | F | F | F |
| Difformità/interferenze risolte | NP | NP | NP | NP | O | O | NP | NP | O | NP | NP | NP | F |
| Difformità/interferenze rilevabili | O | O | O | O | NP | NP | O | NP | NP | NP | NP | NP | F |
| Misure rilevabili | F | F | F | F | NP | NP | F | NP | NP | NP | NP | NP |  |
| Max tempo validazione (giorni) | O | NP | O | O | O | O | NP | O | O | O | O | O | NP |
| Attività prevalenti | NP | NP | NP | F | F | F | NP | NP | F | NP | NP | NP | NP |
| Programma | F | F | F | F | F | F | F | F | F | F | F | F | F |
| Att. Caricabile manualmente | O | O | O | O | O | O | O | O | O | O | O | O | O |
| ID attività standard | O | O | O | O | O | O | O | NP | O | O | O | O | O |
| Descrizione attività standard | O | O | O | O | O | O | O | NP | O | O | O | O | O |
| Normativa di riferimento | NP | NP | NP | NP | NP | NP | F | NP | NP | NP | NP | NP | NP |
| Note | F | F | F | F | F | F | F | F | F | NP | NP | NP | NP |
| Micro-controlli | F | F | F | F | NP | NP | F | NP | NP | NP | NP | NP | NP |
| Metodi | F | F | F | F | F | F | NP | F | F | NP | NP | NP | NP |
| Elive? | F | F | F | F | F | F | F | F | F | NP | NP | NP | NP |
| Outsourcing? | O | O | F | F | F | F | F | F | F | F | F | F | F |
| Risorse | F | F | F | F | F | F | F | F | F | F | F | F | F |
| Risorse esterne | F | F | F | F | F | F | F | F | F | F | F | F | F |
| KPI | F | F | F | F | F | F | F | F | F | F | F | F | F |
| Tolleranza | F | F | F | F | F | F | F | F | F | F | F | F | F |
| Stato | O | O | O | O | O | O | O | O | O | O | O | O | O |
| Revisione | O | O | O | O | O | O | O | O | O | O | O | O | O |
| Idoneo UF/SPCC | O | O | O | O | O | O | O | O | O | O | O | O | O |
| Categoria responsabile attività | O | O | O | O | O | O | O | O | O | O | O | O | O |

Tabella 1 – Matrice informazioni - attività

Di seguito le logiche relative al popolamento dei campi elencati in tabella:

Elemento anagrafico associato: selezione manuale da tendina contenete l’elenco degli elementi EAM presenti in anagrafica (colonna L del mapping). (Campo presente nel tab “Attività”)

Tabella di associazione classificazione: popolamento automatico da parte del sistema sulla base della corrispondenza tra elemento e classificazione. L’utente può modificarla eliminando eventualmente le classificazioni non pertinenti. (Tabella presente nel tab “Attività”)

Gerarchia anagrafica associata: popolato automaticamente dal sistema sulla base del livello anagrafico associato. (Informazione presente nel tab “Attività”)

Unità di misura: popolato manualmente dall’utente, scelto tra “quantità” “Km” ed “ettari”. (Campo presente nel tab “Attività”)

Assieme e sottoassieme: popolamento manuale tramite menù a tendina. (Campo presente nel tab “Attività”). In schermata è presente il campo contente un codice abbreviato da cui va selezionato il valore ed è visibile anche il campo descrizione. Nella dialog di selezione è presente la colonna del codice abbreviato con la relativa descrizione;

Livello attività: popolamento automatico, corrisponde al livello anagrafico associato. (Campo presente nel tab “Attività”)

ID attività: popolamento automatico da parte del sistema. La logica di costruzione si basa sul codice della macro-attività aggiungendo dopo un punto un progressivo numerico che indica il PdL.

Descrizione attività: popolamento manuale. È possibile inserire anche una descrizione estesa, tramite il tasto a destra sulla riga del campo. (Campo presente nel tab “Attività”)

Stato: Stato del PdL, per come definito nel paragrafo 2.2.7.4.1. (Campo presente nel tab “Attività)

Revisione: Numero della revisione del PdL (Campo presente nel tab “Attività”, “Rilevazioni e “Micro-controlli”)

Categoria responsabile attività: Campo popolato a partire da un menu a tendina. Definisce la tipologia del responsabile dell’attività. (Campo presente nel tab “Dettaglio attività”)

ID macro-attività: popolamento manuale tramite menù a tendina che rimanda al catalogo definito per le macro-attività definito all’interno di un dominio valori. (Campo presente nel tab “Attività”)

Attività prevalente per metodo: flag inserito manualmente dall’utente per indicare quale attività è prevalente rispetto alla macro-attività associata. (Flag presente nel tab “Attività”). Il flag può essere attivato per più di un’attività associata alla stessa macro-attività, ma solo se sono state indicate delle caratteristiche (Attributo per scelta metodo);

Attributo per selezione metodo: l’utente seleziona l’attributo strettamente relativo all’asset dell’attività (tra quelli indicati come possibili) che il DSS deve valutare per selezionare il PdL associato alla macro-attività in output. (Informazione presente nel tab “Attività”)

Valore target attributo: inserito manualmente dall’utente, indica i possibili valori target a cui deve corrispondere l’attributo sull’asset su cui deve essere generato l’ordine. (Informazione presente nel tab “Attività”)

Tipologia attività: popolamento manuale tramite menù a tendina. (Campo presente nel tab “Attività”)

Indisponibilità?: flag (default a NO) che determina se gli OdL associati al PdL devono essere inviati verso Agile 2.0 per l’eventuale predisposizione della richiesta indisponibilità. (Flag presente nel tab “Attività”)

Durata pianificata: popolato dal sistema, calcolato come il valore massimo tra le durate associate alle singole righe della tabella “Risorse”. (Campo presente nel tab “Date ed effort”)

Effort: popolato dal sistema, calcolato come somma dell’effort delle righe nella tabella “Risorse”. (Campo presente nel tab “Date ed effort”)

Difformità/Interferenze risolte: lista delle difformità/interferenza risolvibili dall’attività. Popolato manualmente su una tabella, scegliendolo tramite menù a tendina prefiltrato sulla base del livello anagrafico associato. Le tabelle, rappresentate in Figura 31, sono presenti nel tab “Attività” ed è disponibile solo se il PdL è di tipologia “Sostituzione”, “Manutenzione su condizione” o “Demolizione”

Figura -Tabelle di difformità / interferenze risolte

Difformità/Interferenze rilevabili: valido solo per tipologia attività uguale a controlli periodici, occasionali, tecnici e di legge. Popolato manualmente su una tabella, selezionandolo da un menù a tendina prefiltrato sulla base del livello anagrafico associato.

Misure rilevabili: contiene la lista delle misure rilevabili attraverso il PdL. La tabella è popolabile aggiungendo righe attraverso l’apposito tasto “+”. Aggiungendo una riga l’utente può selezionare tramite pick list le misure rilavabili tramite il PdL tra quelle associate a stessa tipologia asset del PdL stesso. Sono mostrate solo i record del catalogo misure/variabili di stato di tipo parametro MISURA. La tabella contiene i seguenti campi: Id misura di catalogo; descrizione misura a catalogo; elemento asset di rifermento della misura; unità di misura. Esclusivamente per i PdL associati a tipologia asset CAMPATA AEREA è possibile associare anche misure rilevabili su tutti i figli della campata aerea e sui sostegni (fratelli della campata) e su tutti i figli dei sostegni. Se il PdL è associato ad un asset di terzo livello linee, è possibile associare misure rilevabili anche sui figli del terzo livello associato al PdL. Su asset di quarto livello stazioni e possibile anche rilevare su asset di quarto fratelli sotto lo stesso terzo. Se non sono state associate misure rilevabili al PdL, sono rilevabili tutte le misure possibili per quell’asset (sempre in conformità con le logiche di rilevazione delle caratteristiche asset indicate a catalogo misure).

Max. tempo di validazione: popolato manualmente, valore numerico in giorni di tempo massimo in cui un’attività generata dal DSS può rimanere in stato “da validare”. Il campo non è previsto per PdL di tipologia “Controllo periodico”. (Informazione presente nel tab “Date ed effort”)

Attività prevalenti: popolato manualmente, aggiunta delle attività prevalenti in una tabella selezionandole dalla lista di quelle presenti a catalogo. Non potranno essere selezionati delle attività in stato diverso da “Attivo”. È previsto un controllo che impedisce all’utente di associare ad attività di tipologia “Controllo periodico” attività prevalenti. L’associazione tra attività prevalsa e prevalente può essere eseguita con le seguenti modalità:

Per associazione di attività prevalenti riferite a strettamente stesso asset, la logica è gestita sia da DSS che all’apertura manuale;

Per linee aeree e linee in cavo è possibile associare come prevalenti su attività riferite a quarto livello, PdL associati a terzo livello e viceversa. Questa logica è però applicata esclusivamente dal DSS quindi non per attività create manualmente;

Per stazioni è possibile associare come prevalenti su attività riferite a quarto livello, PdL relativi ad altri asset di quarto livello sotto lo stesso terzo livello. Questa logica è però applicata esclusivamente dal DSS quindi non per attività create manualmente;

Non sono gestite associazioni tra attività che non rientrano strettamente tra i tre punti precedenti.

Programma: popolato manualmente, selezionato da una lista valori definita a sistema. (Campo presente nel tab “Dettaglio attività”)

Attività caricabile manualmente: flag popolato manualmente.

ID attività standard: popolato manualmente dall’utente scegliendo da un menù a tendina che riporta i record presenti nel catalogo standard. (Campo presente nel tab “Dettaglio attività”)

Descrizione attività standard: popolato automaticamente sulla base del campo definito sul catalogo standard sulla base dell’ID attività standard scelta nel campo “ID attività standard”. (Campo presente nel tab “Dettaglio attività”)

Normativa di riferimento: popolato manualmente e prevista solo per attività di tipologia “Controllo di legge”, campo libero.

Note: popolato manualmente in campo libero

Elive?: Flag che indica che l’OdL generato dal PdL sarà ispezioni Lidar. Di default viene impostato su “NO”. (Flag presente nel tab “Attività”)

Outsourcing?: booleano popolato dall’utente. Di default è impostato su “NO” (Flag presente nel tab “Attività”)

Micro-controlli: popolato manualmente, aggiunto alla tabella scegliendolo dal catalogo del piano dei controlli. I micro-controlli sono prefiltrati sulla base dell’asset di primo livello. (Informazione presente nel tab “Micro-controlli”)

Metodi (tab non attivo): popolato manualmente, aggiunto nella tabella del tab workflow scegliendolo dalla lista dei metodi definiti nel catalogo dei metodi.

Risorse: popolato manualmente, aggiunte nella tabella “Risorse” del tab Date ed effort inserendo l’inquadramento, il numero di risorse, durata in ore e l’effort (calcolato dal sistema come prodotto tra numero risorse e durata in ore). (Informazione presente nel tab “Date ed effort”)

Idoneo UF/SPCC: campo che indica quale attributo LIN_IDONEITA il DSS deve considerare in fase di generazione ordini su terzo livello stazione. Popolato tramite lista valori che contiene esclusivamente due valori: UF, SPCC. Se il campo è popolato con UF, il DSS valuterà l’idoneità dell’asset di terzo livello sulla base dell’attributo LIN_IDONEITA_UF se il campo è popolato con SPCC il DSS valuterà l’idoneità dell’asset sull’attributo LIN_IDNOEITA_SPCC. Il campo è visibile e popolabile solo per PdL che insistono su asset di terzo livello stazioni.

Risorse esterne: tabella popolata manualmente, all’interno della quale sono presenti le seguenti colonne:

ID Materiale: scelto da una lista valori che attinge dal catalogo delle risorse esterne

Descrizione: descrizione dell’id materiale, popolato automaticamente in base all’ID materiale inserito

Importo unitario catalogo: popolato automaticamente in base all’ID materiale inserito. Il campo è di sola lettura

Importo unitario inserito: popolato manualmente dall’utente.

Quantità: popolato manualmente dall’utente

KPI: campo numerico (solo valori positivi) popolato manualmente dall’utente

Tolleranza: campo numerico (solo valori positivi) popolato manualmente dall’utente. Rappresenta il range di tolleranza percentuale entro cui si può discostare l’importo di ogni RE dal KPI in fase di peer review

Figura - Tabella associazione risorse esterne

Di seguito una spiegazione puntuale dei tab presenti sulla schermata delle singole attività.

Tab “Dettaglio dell’attività” (Figura 33): Il tab riporta le informazioni relative alla dimensione contabile e programmi (sia il codice abbreviato che la descrizione completa).

Figura - Tab "Dettaglio attività"

Tab “Data ed effort” (Figura 34): In questo tab vengono definiti aspetti inerenti alla durata per l’esecuzione dell’attività a seconda delle risorse (interne ed esterne) che la devono eseguire. È anche definita in questo tab la finestra di validità del PdL, così come il suo massimo tempo di validazione.

Figura - Tab “Data ed effort”

Tab “Rilevazioni” (Figura 35): vengono riportate tutte le interferenze e difformità rilevabili dall’attività (sullo stesso asset a cui è associata). Per alcuni casi particolari è possibile associare ad una attività, difformità/interferenze rilevabili su asset differenti da quello a cui fa riferimento il PdL. Di seguito il dettaglio dei casi particolari:

PdL Linee:

Si possono inserire difformità rilevabili/risolvibili sull'asset oggetto di PdL;

Se il PdL è su un asset di terzo livello si possono inserire difformità rilevabili/risolvibili sullo stesso asset di 3 livello e sui figli di 4;

Se il PdL è rilevazione/controllo/monitoraggio sulla campata si possono inserire difformità rilevabili sulla campata e sui figli di questa. Inoltre, si possono inserire difformità sui sostegni (fratelli della campata) e sui figli dei sostegni;

PdL Stazione:

Si possono inserire difformità rilevabili/risolvibili sull'asset oggetto di PdL

Se il PdL è su un asset di quarto livello si possono inserire difformità rilevabili/risolvibili anche sui fratelli di quarto livello (che condividono lo stesso padre di 3° livello)

Esclusivamente per ordini che fanno riferimento ad asset di tipo campata aerea, è possibile registrare anche difformità relative ai sostegni adiacenti.

Nel caso in cui l’attività includa dei micro-controlli da eseguire, viene riportata anche la tabella contenente le difformità/interferenze rilevabili da questi ultimi. È prevista anche la possibilità di gestire controlli per i quali sono presenti dei micro-controlli ma le difformità/interferenze sono collegate a livello di PdL. All’interno di questo tab, oltre a visualizzare le difformità/interferenze, tramite apposito tasto è possibile associare difformità/interferenze all’attività (tasto “+” in alto destro, presente in ogni tabella). Una volta fatto ciò, cliccando sulla lente, l’utente può selezionare la difformità/interferenza di interesse. Da sottolineare che all’utente appariranno solo le difformità/interferenze, definite nei rispettivi cataloghi, applicabili alla tipologia di asset (classificazione) oggetto dell’attività e i sottostanti (a livello gerarchico inferiore). Per quanto riguarda la tabella relativa alle difformità rilevabili da micro-controlli, quest’ultima viene popolata in automatico in base ai micro-controlli (definiti per l’attività nel tab “Micro-controlli”) e le relative difformità/interferenze rilevabili associate a questi e non è modificabile da questa schermata.

Figura - Tab rilevazioni

Tab “Workflow” (Tab non attualmente attivo e visibile a sistema) (Figura 36): si riporta il workflow dei metodi. Per aggiungere nuovi metodi bisogna seguire la stessa procedura descritta nel tab “Rilevazioni”; al momento della selezione del nuovo metodo da inserire, l’utente sceglie uno di quelli definiti nel relativo catalogo (con i relativi competenze, mezzi e strumenti associati). Nella tabella “Workflow” per ogni metodo viene riportato l’ID, la descrizione, la durata e il totale delle risorse necessarie al suo svolgimento. Nel catalogo dei metodi è presente una tabella contenente gli attributi del metodo (competenze, mezzi, strumenti, attrezzature) necessari allo svolgimento del workflow (Figura 37 ). Nella colonna quantità della tabella “Attributi del metodo” è riportato il numero massimo di competenze, mezzi, strumenti, attrezzature presente tra i vari metodi.

Ad esempio: workflow composto da 2 metodi. Se il metodo 1 necessita di 3 competenze X e il metodo 2 necessita di 1 competenza X, allora nel tab “Competenze” si riporta una riga “Competenza X” con “Quantità” uguale a 3 (numero più grande).

Figura - Tab Workflow

Figura - catalogo Metodi

Tab “Micro-controlli” (Figura 38): se l’attività lo richiede, nel tab micro-controlli, è possibile associare tutti i micro-controlli necessari per lo svolgimento dell’attività. Le modalità di aggiunta e inserimento dei micro-controlli sono le stesse descritte per l’aggiunta delle difformità/interferenze nel tab “Rilevazioni”. Anche in questo caso, nel momento in cui si deve selezionare il micro-controllo da associare, all’utente appare una lista con i micro-controlli relativi alla tipologia di asset oggetto dell’attività. Il tab micro-controlli viene reso disponibile o viene nascosto a seconda della tipologia di attività secondo quanto definito in Tabella 1.

Figura - tab "Micro-controlli"

#### Processo di inserimento e modifica delle attività

In EAM, gli utenti dotati delle abilitazioni necessarie possono aggiungere attività al catalogo. Al fine di prevenire possibili discrepanze tra i cataloghi e garantire che il processo di aggiunta di una nuova attività avvenga in modo efficace, il processo viene eseguito rispettando una sequenza logica di azioni prestabilite, come schematizzato in Figura 39.

Figura - Processo aggiunta attività

In particolare, prima di procedere con l’aggiunta di una nuova attività occorre assicurarsi che i cataloghi delle difformità e delle interferenze (collegati al catalogo delle misure / variabili di stato) siano completati con tutte le informazioni necessarie per le associazioni con l’attività in oggetto. In seguito, dopo aver definito le attività e stabilito eventuali associazioni con le difformità o interferenze correlate, l’utente è anche in grado di determinare le policy, che racchiudono le relazioni con gli oggetti derivanti dagli altri cataloghi.

Nella schermata iniziale del MAS, chiamata “Centro di controllo” (utilizzata in MAS come dashboard personalizzabile per visualizzare e accedere alle informazioni più rilevanti) l’utente può visualizzare tutte le incongruenze presenti nei cataloghi dovute a modifiche (dettagliate nel par 2.2.7.4.2) apportate alle associazioni presenti nei cataloghi.

In particolare, potrà visualizzare:

La tabella contenente i record delle difformità/interferenze che non sono risolte da alcuna attività;

La tabella contente i record delle attività che non rilevano alcuna difformità/interferenza;

La tabella delle associazioni difformità/interferenze-controlli che non sono gestite tramite nessuna policy.

In Figura 40 si riporta l’immagine di un centro di controllo contenente le difformità, presenti a sistema, non risolte da nessuna attività come esempio. Cliccando su uno dei record presenti nella tabella “difformità non risolte da attività” l’utente viene reindirizzato sulla difformità, presente nel rispettivo catalogo, e può quindi associare quest’ultima a un’attività risolutiva.

Figura – Sezione “difformità non risolte da attività” del Centro di controllo

In generale, quando vengono effettuate modifiche ai cataloghi e alle relazioni tra gli elementi il sistema, tramite pop-up, fornisce warning in cui viene richiesto all’utente di controllare il centro di controllo per vedere se le modifiche hanno causato delle incongruenze (es: difformità non associata ad attività). Tutte le azioni per la gestione delle incongruenze devono essere effettuate manualmente dall’utente direttamente sui cataloghi.

##### Stati dei Piani di Lavoro

L’immagine riportata di seguito rappresenta gli stati che il PdL assume dalla creazione alla disattivazione dello stesso:

Figura - Stati PdL

Di seguito si descrivono i passaggi di stato che caratterizzano i PdL:

Alla creazione di un’attività il PdL è in stato bozza. In questo stato è possibile compilare e modificare tutti i campi dell’attività. In particolare, è necessario definire i campi obbligatori per poter salvare l’attività in questo stato. Una volta salvata a sistema, è possibile associarla alle policy le quali non potranno essere lette dal DSS fintanto che l’attività associata non si trova in stato attivo.

Il passaggio da bozza ad attivo del PdL è manuale e deve essere effettuato dall’utente dopo aver validato le informazioni inserite. In questo stato, l’attività può essere associata alle policy e sarà visibile al DSS per la generazione.

Per poter modificare le informazioni di un PdL in stato attivo, l’utente richiede la revisione del PdL tramite un apposito tasto (Rivedi piano di lavoro). Il sistema, quindi, genera un PdL nuovo con le stesse informazioni del PdL originale in stato “Pending review”, con il campo “Revisione” valorizzato con n+1. In questo momento il PdL originale è ancora in stato Attivo. Sul PdL nuovo possono essere effettuate alcune modifiche descritte nel dettaglio nel seguito del capitolo. La modifica del PdL non è riversata automaticamente sugli ordini già generati ai quali è associato. Per visualizzare le versioni precedenti del PdL, è possibile filtrare la vista elenco con il codice del PdL di interesse. In tal modo saranno presentate tutte le versioni del PdL per il quale si è applicato il filtro. Facendo un filtro anche sullo stato “attivo” si visualizzerà solo il PdL nella revisione attiva.

Figura - Tasto “Rivedi piano di lavoro” utilizzato per revisionare il PdL

Una volta concluse e validate le modifiche, l’utente riporta in stato attivo lo stato del PdL nuovo, che sarà di nuovo fruibile da parte del DSS. Da questo momento, lo stato del PdL originale sarà impostato su “Revisionato” e non sarà più letto dal DSS per la generazione di nuove OdL.

Nel caso in cui l’utente decida di eliminare un PdL che sta modificando (quindi in stato Pending review) deve impostare lo stato del PdL su “Cancellato”, selezionando l’opzione “Modifica stato” nel menu a sinistra e scegliendo l’opzione “Annulla” nel menu a tendina del campo “Nuovo stato”.

Il passaggio da attivo a non attivo può essere manuale, su input dell’utente, oppure automatico nel caso in cui fosse stata definita una “data di disattivazione”. Da questo stato è possibile riportare il PdL in stato attivo.

##### Logiche di modifica e disattivazione dei PdL

L’utente ha la possibilità di modificare le informazioni dei PdL presenti a catalogo, ad eccezione del codice univoco che rappresenta l’attività. Le informazioni del PdL possono essere modificate solo quando questo si trova in stato «Pending review» o in stato “bozza”. Al momento della richiesta di revisione del PdL il sistema crea un nuovo PdL da revisionare in stato «Pending review» sul quale è possibile effettuare le modifiche necessarie.

Per propagare il nuovo PdL revisionato (modificato) sugli ordini già generati dal DSS, l’utente deve effettuare un’azione manuale attraverso un tasto presente sulla schermata, denominato “propaga modifiche” dopo che il PdL è stato riportato in stato “attivo”. L’associazione del nuovo PdL revisionato è propagata dal sistema solo sugli OdL non ancora schedulati (in stato APERTO e DA VALIDARE). Per alcuni campi (specificati nella lista di seguito) le modifiche sono propagate automaticamente. Di seguito è riportato il dettaglio delle modifiche e dei controlli di coerenza effettuati dal sistema:

Modifica livello anagrafico associato: campo modificabile solo nel caso in cui il PdL si trovi in stato “bozza”. Una volta attivato non sarà più possibile modificarne il livello anagrafico associato.

Modifica unità di misura: campo modificabile solo nel caso in cui il PdL si trovi in stato “bozza”. Una volta passato in stato attivo non sarà più possibile modificare il campo.

Modifica durata pianificata: è possibile modificare il campo quando il PdL è in stato Pending review. La modifica è propagata tramite il tasto “propaga modifiche”.

Risorse interne: La modifica è propagata tramite il tasto “propaga modifiche”.

Risorse esterne: è possibile modificare il campo quando il PdL è in stato pending review. La modifica è propagata tramite il tasto “propaga modifiche”.

Difformità/interferenze rilevabili: Le possibili modifiche alle difformità/interferenze rilevabili da un’attività sono:

Eliminazione di una difformità/interferenze rilevabile;

Aggiunta di una difformità/interferenze rilevabile.

Nel caso in cui venga eliminata l’associazione con una difformità/interferenza rilevabile. Il sistema tramite warning avverte l'utente che potrebbero esserci difformità/interferenza non rilevabili tramite alcuna attività. L’utente potrà poi visualizzare nel centro di controllo tutte le difformità/interferenze non rilevabili tramite alcuna attività.

Modifica assieme e sottoassieme: è possibile modificare il campo quando il PdL è in stato “pending review”. La modifica è propagata pdl

Modifica descrizione: è possibile modificare il campo quando il PdL è in stato “pending review”. La modifica è propagata tramite il tasto “propaga modifiche”.

Modifica ID macro-attività: campo modificabile solo nel caso in cui il PdL si trovi in stato “bozza”. Una volta attivato non sarà più possibile modificarne il campo.

Modifica attività prevalenti: è possibile modificare il campo quando il PdL si trova in stato “pending review”. La modifica è propagata in automatico dopo il salvataggio e il passaggio di stato in “attivo”.

ID attività standard: campo modificabile solo nel caso in cui il PdL si trovi in stato “bozza”. Una volta attivato non sarà più possibile modificare il campo.

Modifica Normativa di riferimento: è possibile modificare il campo quando il PdL è in stato “pending review”.

Modifica Note: è possibile modificare il campo quando il PdL si trova in stato “pending review”.

Modifica micro-controlli: è possibile modificare il campo quando il PdL si trova in stato “pending review”. La modifica è propagata in automatico dopo il salvataggio e il passaggio di stato in “attivo”.

Modifica metodi: è possibile modificare il campo quando il PdL si trova in stato “pending review”. La modifica è propagata dopo il salvataggio e il passaggio di stato in “attivo”, mediante la selezione del pulsante “Propaga modifiche”.

Modifica tipologia: Per modificare la tipologia ad un’attività in stato «attivo» è necessario duplicare la stessa. In questo modo la nuova attività duplicata, in stato «bozza», può essere modificata. L’eventuale disattivazione dell’attività di partenza dovrà essere effettuata manualmente.

Modifica difformità/interferenze risolte: Le possibili modifiche alle difformità/interferenze risolte da un’attività sono:

Eliminazione di una difformità/interferenze risolta;

Aggiunta di una difformità/interferenze risolta.

Nel caso in cui venga eliminata l’associazione con una difformità/interferenze:

Se l’associazione attività – difformità/interferenze risolta è obbligatoria ed è l’unica, il sistema inibisce la riattivazione dell’attività fintanto che l’utente non effettua almeno un’altra associazione;

Il sistema avverte l’utente con un warning che potrebbero esserci delle difformità/interferenze per le quali l’associazione con l’attività era l’unica. L’utente può poi controllare sulla dashboard delle incongruenze quali difformità/interferenze sono rimaste senza attività risolutrici;

Il sistema avverte l’utente con un warning che sarà disattivata la policy che regola l’associazione eliminata. Il sistema fa partire una procedura per la ricerca e la disattivazione della policy, l’utente dovrà aspettare la conferma.

Nel caso in cui venga aggiunta l’associazione con una difformità/interferenze, il sistema da evidenza della mancanza di una policy che regoli l’associazione.

Modifica massimo tempo di validazione: La modifica, sia di rilassamento che di restringimento, è propagata in automatico dal sistema sugli ordini già generati in stato “in validazione” al salvataggio e attivazione del PdL. A seguito dell’attivazione dell’attività viene effettuato un controllo sugli ordini di lavoro già generati, determinando il passaggio di stato in «Aperto» nel caso in cui la data attuale sia superiore al nuovo tempo di validazione massimo (data di generazione DSS + max tempo di validazione).

Modifica programma: Una volta che l’utente effettua una modifica del programma associato a un’attività la modifica viene propagata automaticamente dal sistema sugli ordini già generati dal DSS, dato che il programma è un attributo ereditato sull’OdL generato.

Modifica “Attività prevalente per macroattività”: Campo fruibile dalle Policy per la generazione degli OdL. La modifica non ha impatto sugli OdL già generati

Modifica “Attributo per selezione metodo”: Campo fruibile dalle Policy per la generazione degli OdL. La modifica non ha impatto sugli OdL già generati

Modifica “Valore target”: Campo fruibile dalle Policy per la generazione degli OdL. La modifica non ha impatto sugli OdL già generati

Modifica flag Elive?: è possibile effettuare una modifica solo in stato “Bozza”.

Modifica flag Indisponibilità: è possibile effettuare una modifica solo in stato “Bozza”.

Modifica flag Outsourcing?: modificabile in stato pending review, la modifica è propagata tramite tasto “Propaga modifica”.

Modifica “Categoria responsabile attività”: è possibile effettuare una modifica solo in stato “Bozza”;

Disattivazione attività: Per disattivare un’attività, questa deve essere portata in stato «Non attiva». In questo modo l’attività non sarà più letta dal DSS. La disattivazione può essere manuale o automatica (quando si supera la data di fine validità definita a catalogo). Il sistema, quando viene disattivata un’attività:

Mostra un warning in cui avverte l’utente che saranno messe in “Pending review” tutte le policy per le quali l’attività disattivata è output. Il sistema fa partire una procedura per la ricerca e la modifica stato della policy, l’utente dovrà aspettare la conferma.

Mostra un warning in cui avverte l’utente che saranno messe in “Pending review” tutte le policy per le quali l’attività disattivata è unico input. Il sistema fa partire una procedura per la ricerca e la modifica stato della policy, l’utente dovrà aspettare la conferma.

In generale, l’utente deve verificare le incongruenze apportate alle associazioni tra difformità/interferenze, policy e attività in conseguenza della disattivazione dell’attività sul centro di controllo dedicato, all’interno del quale si riporta:

Difformità/interferenze non risolte da attività;

Attività non associate a nessuna policy;

Difformità/interferenze non rilevate da attività;

Associazione difformità/interferenze - attività risolutrice per la quale non è definita una policy.

Cataloghi informazioni contabili

Nei successivi paragrafi viene descritta la gestione dei cataloghi contenenti le informazioni contabili necessarie alla valorizzazione del tab “CoIn” dell’OdL.

#### Catalogo standard

Tramite l’applicazione “Catalogo standard” l’utente accede alla vista elenco di tutti gli “ID attività standard” presenti a sistema. Una volta cliccato sul singolo record si viene reindirizzati ad una schermata contenente le informazioni contabili riportate in Figura 43 e di seguito elencate:

ID Attività standard: campo libero valorizzato manualmente dall’utente;

Descrizione Attività standard: campo libero valorizzato manualmente dall’utente;

Codice Coin OPEX: campo valorizzato manualmente dall’utente tramite una lista valori contente i codici Coin definiti nel rispettivo catalogo (cfr. par 2.2.8.2). Una volta selezionato il codice CoIn, il sistema popola automaticamente il campo descrizione.

Codice Coin CAPEX: campo valorizzato manualmente dall’utente tramite una lista valori contente i codici Coin definiti nel rispettivo catalogo (cfr. par 2.2.8.2). Una volta selezionato il codice CoIn, il sistema popola automaticamente il campo descrizione.

Codice Coin ANR: campo valorizzato manualmente dall’utente tramite una lista valori contente i codici Coin definiti nel rispettivo catalogo (cfr. par 2.2.8.2). Una volta selezionato il codice CoIn, il sistema popola automaticamente il campo descrizione.

Tipo spesa prevalente: campo valorizzato manualmente dall’utente tramite una lista valori contenente “OPEX”, “CAPEX”, “ANR”;

Codice Coin prevalente: campo valorizzato manualmente dall’utente tramite una lista valori contente i codici Coin inseriti nelle tabelle Codice Coin OPEX (se tipo spesa prevalente è OPEX), CAPEX (se tipo spesa prevalente è CAPEX), ANR (se tipo spesa prevalente è ANR); si specifica che se il Codice Coin prevalente è valorizzato con una delle Coin inserite nelle tabelle sopra descritte, in caso di eliminazione della Coin dalla tabella Codice Coin OPEX/CAPEX/ANR, un automatismo svuota automaticamente il campo Codice Coin prevalente.

Figura - Catalogo standard

Si specifica che:

Il catalogo risulta filtrabile per ognuna delle colonne sopra riportate;

Solo gli utenti centrali possono aggiungere / modificare / eliminare record. Gli utenti non centrali avranno solo abilitazioni di visualizzazione;

La gestione del catalogo è puramente manuale. Non sono previste integrazioni con altri sistemi per il recupero delle informazioni;

#### Catalogo Coin

Tramite l’applicazione “Catalogo Coin” l’utente accede al catalogo contenente le informazioni contabili riportate in Figura 44 e di seguito elencate:

Codice Coin: campo libero valorizzato manualmente dall’utente;

Tipo ordine: campo valorizzato manualmente dall’utente tramite una lista valori;

Tipo attività PM: campo valorizzato manualmente dall’utente tramite una lista valori;

Tipo spesa: campo valorizzato manualmente dall’utente tramite una lista valori contenente “OPEX”, “CAPEX”, “ANR”;

Descrizione Coin: descrizione coin, campo libero valorizzato manualmente dall’utente;

Figura - Catalogo Coin

Si specifica che:

Il catalogo risulta filtrabile per ognuna delle colonne sopra riportate;

Solo gli utenti centrali possono aggiungere / modificare / eliminare record. Gli utenti non centrali avranno solo abilitazioni di visualizzazione;

La gestione del catalogo è puramente manuale. Non sono previste integrazioni con altri sistemi per il recupero delle informazioni;

Nel caso in cui all’interno del “Catalogo Coin” si elimini un codice Coin utilizzato all’interno del “Catalogo standard”, al momento del salvataggio il sistema restituisce un pop-up con scritto “Verifica sul centro di controllo i record del catalogo standard che contengono la Coin appena eliminata”. All’interno del centro di controllo l’utente centrale può visualizzare la tabella riportata in Figura 45. Cliccando sul singolo record di ID attività standard l’utente viene reindirizzato alla schermata del catalogo standard (Figura 43) all’interno della quale è possibile eliminare la CoIn precedentemente eliminata dal catalogo CoIn.

Figura 45 - Sezione del centro di controllo contenente i record del catalogo standard che contengono Coin non presenti nel Catalogo Coin

#### Catalogo CBS-CBE pianificate

Tramite l’applicazione “Catalogo CBS-CBE Pianificate” l’utente accede al catalogo contenente le informazioni riportate in Figura 46 e di seguito elencate:

Responsabile attività (UI): campo valorizzato manualmente dall’utente tramite una lista valori contenente le UI censite a sistema;

Tipo spesa: campo valorizzato manualmente dall’utente tramite una lista valori contenente “OPEX”, “ANR”;

CBS Pianificata: campo libero valorizzato manualmente dall’utente;

CBE Pianificata: campo libero valorizzato manualmente dall’utente;

Codice Coin: campo valorizzato manualmente dall’utente tramite una lista valori contenente i codici Coin definiti nel “Catalogo Coin”. Il campo risulta valorizzabile solo se il tipo spesa è uguale a “ANR”.

Figura 46 - Catalogo CBS-CBE Pianificate

Si specifica che:

Il catalogo risulta filtrabile per ognuna delle colonne sopra riportate;

Solo gli utenti centrali possono aggiungere / modificare / eliminare record. Gli utenti non centrali avranno solo abilitazioni di visualizzazione;

La gestione del catalogo è puramente manuale. Non sono previste integrazioni con altri sistemi per il recupero delle informazioni;

Catalogo Risorse Esterne – Ricavi

In questo paragrafo viene descritta la gestione del catalogo contenente le informazioni necessarie alla valorizzazione delle Risorse esterne e dei Ricavi all’interno del tab “Pianificazione” dell’OdL.

Tramite l’applicazione “Catalogo Risorse Esterne – Ricavi” l’utente accede al catalogo contenente le informazioni riportate in Figura 47 e di seguito elencate:

Tipo risorsa: campo valorizzato manualmente dall’utente tramite una lista valori contenente “Prestazioni terze”, “Materiali”, “Ricavi”;

ID Materiale: Campo libero valorizzato manualmente dall’utente.;

Descrizione materiale: Campo libero valorizzato manualmente dall’utente.;

Gruppo merce: campo valorizzato manualmente dall’utente tramite una lista valori.;Il campo risulta non visibile per risorse di tipo “Ricavi”

Definizione gruppo merci: campo valorizzato automaticamente in base al Gruppo merci precedentemente inserito. Il campo risulta non visibile per risorse di tipo “Ricavi”;

Conto: Campo valorizzato manualmente dall’utente tramite una lista valori

Descrizione Conto: campo valorizzato automaticamente in base al Conto precedentemente inserito.

Unità di misura base: Campo valorizzato manualmente dall’utente tramite una lista valori. Il campo risulta non visibile per risorse di tipo “Ricavi”;

Importo unitario importo del materiale. Il campo risulta non visibile per risorse di tipo “Ricavi” .

Figura 47 - Catalogo Risorse Esterne e Ricavi

Si specifica che:

Il catalogo risulta filtrabile per ognuna delle colonne sopra riportate;

Solo gli utenti centrali possono aggiungere / modificare / eliminare record. Gli utenti non centrali avranno solo abilitazioni di visualizzazione;

La gestione del catalogo è puramente manuale. Non sono previste integrazioni con altri sistemi per il recupero delle informazioni;

Matrice Origine - Destinazione

In questo paragrafo viene descritto il contenuto e la gestione della Matrice di Origine – Destinazione che include i tempi di trasferimento necessari a un Titris per raggiungere una sede tecnica di primo livello.

Tramite l’applicazione “Matrice Origine – Destinazione” l’utente accede alla tabella riportata in Figura 48, contenente le seguenti informazioni:

Origine: campo valorizzato manualmente dall’utente tramite una lista valori contente i Titris censiti a sistema;

Destinazione: campo valorizzato manualmente dall’utente tramite una lista valori contenente i SAP-ID delle sedi tecniche di primo livello censite a sistema;

Tempo di trasferimento: campo numerico valorizzato manualmente dall’utente. Rappresenta le ore impiegate dal Titris per raggiungere la sede tecnica di primo livello.

Figura 48 - Matrice Origine – Destinazione

Si specifica che:

La matrice risulta filtrabile per ognuna delle colonne sopra riportate;

Solo gli utenti centrali possono aggiungere / modificare / eliminare record. Gli utenti non centrali avranno solo abilitazioni di visualizzazione;

La gestione della matrice è puramente manuale. Non sono previste integrazioni con altri sistemi per il recupero delle informazioni. Si specifica che è possibile caricare/modificare massivamente i dati tramite MXLoader;

Eventuali modifiche apportate alla matrice saranno propagate solo sugli OdL in stato “Aperto”.

Catalogo delle policy

In questo capitolo viene descritto il catalogo relativo alle Policy. Le Policy sono delle regole utilizzate dal DSS per l’elaborazione (generazione / modifica / cambio stato) degli OdL. All’interno delle Policy sono definiti dall’utente gli Input che ne determinano l’esecuzione e gli output che deve generare.

Le Policy sono suddivise in due macrocategorie:

Policy elementari: Policy basate su un singolo input;

Policy combinate: Policy che presentano molteplici input.

Per accedere al catalogo delle Policy, l’utente deve selezionare l’applicazione “Catalogo Policy”. Successivamente, all’interno della maschera di ricerca (Figura 49), l’utente può selezionare la tipologia di sede tecnica di primo livello associata alla Policy che desidera visualizzare utilizzando le query predefinite (linee aeree, linee in cavo o stazioni). Una volta selezionata la tipologia, la maschera viene aggiornata mostrando solamente le Policy legate alla sede tecnica di primo livello selezionata. La maschera di ricerca contiene i seguenti campi (approfonditi nel seguito di questo documento):

Tipo policy

ID Policy;

Elemento asset;

Gerarchia asset;

ID Timing;

Max posticipo attività;

Stato;

Figura 49 - Maschera di ricerca Policy

Selezionando una qualsiasi riga di questo elenco, l’utente viene indirizzato alla vista di dettaglio della Policy. In particolare, verranno descritte non solo le caratteristiche che definiscono le diverse Policy e i possibili stati che possono assumere, ma anche i campi di relazione e le logiche che definiscono la generazione di attività sulla base degli input che compongono le Policy.

#### Caratterizzazione delle Policy

Dopo aver selezionato un record dalla maschera di ricerca delle Policy, l’utente accede alla vista di dettaglio della Policy di interesse,

I campi di seguito elencati (rappresentati in Figura 51) rappresentano le informazioni che definiscono le Policy in modo univoco:

ID Policy: Codice univoco che identifica la Policy generato automaticamente dal sistema al primo salvataggio. L’ID policy è costruito secondo le regole descritte di seguito in base al tipo di asset:

ID Linee aeree: P . L (Linea aerea) . n° progressivo associato al 2° liv anagrafica . n° progressivo associato al 3° livello di anagrafica . n° progressivo associato al 4° livello di anagrafica . Progressivo crescente da 1 a n (esempio: P.L.1.1.1.1);

ID Linee in cavo: P . L (Linea cavo) . n° progressivo associato al 2° liv anagrafica . n° progressivo associato al 3° livello di anagrafica . n° progressivo associato al 4° livello di anagrafica . Progressivo crescente da 1 a n (esempio: P.L.3.1.1.1);

ID Stazioni: P . S (Stazione) . n° progressivo associato all’elemento anagrafico di riferimento . Progressivo crescente da 1 a n (esempio: P.S.48.1, 48 transcodifica con l’elemento l’interruttore ais x).

Elemento: Elemento Asset al quale è associata la policy. Campo popolato manualmente dall’utente tramite una lista valori contenente tutti gli elementi asset presenti a sistema;

Classificazione Tabella contenente le classificazioni alle quali è associata la Policy. La tabella è popolata automaticamente in base all’Elemento precedentemente inserito. Il rapporto Elemento - Classificazione può essere sia 1:1 (in questo caso elemento e classificazione coincidono) sia 1:N (in questo caso a un elemento sono associate più classificazioni. Es: nel caso in cui si inserisse un elemento “Interruttore” nella tabella compariranno le classificazioni “Interruttore MT ad aria compressa”, “Interruttore MT Deion”, “Interruttore MT in SF6”, etc..). Si specifica che l’utente, se lo ritiene necessario, potrà solamente eliminare alcune delle classificazioni proposte dal sistema per le quali non ritiene necessario applicare la policy;

Gerarchia asset associato: Gerarchia dell’asset associato alla Policy. Campo valorizzato automaticamente in base all’“Elemento” inserito

Tronco d’ispezione?: campo visibile solo se l’elemento associato è una campata aerea o una tratta cavo. Questo campo permette al DSS di individuare le policy su cui applicare la logica del tronco di ispezione (descritta nel capitolo 3) per la generazione degli ordini.

Livello policy: Campo popolato automaticamente sulla base del livello anagrafico dell’Asset associato. Ad esempio, nel caso di una Policy associata a un sostegno il campo viene popolato con “2”;

Stato: Stato in cui si trova la Policy. Le logiche di gestione degli stati sono approfondite nel paragrafo 2.2.11.3 di questo capitolo.

Tipo Policy: Può assumere i valori “on-condition” o “periodica”;

Frequenza: Campo numerico popolabile solo per le Policy con Tipo Policy uguale a “periodica”. La Frequenza permette di determinare la data modello dell’OdL generato dalla Policy ed è definita in base al riferimento temporale inserito.

Riferimento temporale: codice della frequenza / differibilità. Campo popolato manualmente dall’utente selezionando il valore da una lista predefinita, all’interno della quale sono presenti 4 valori:

“G” (Giorno)

“S” (Settimana)

“M” (Mese)

“A” (Anno)

Differibilità: Campo numerico popolabile solo per le Policy con Tipo Policy uguale a “on-condition”. La Differibilità permette di determinare la data modello dell’OdL generato dalla Policy ed è definita in base al riferimento temporale inserito.

ID Timing: Campo popolato automaticamente dal sistema. Rappresenta la concatenazione tra il valore inserito in “frequenza” / “differibilità” e quello inserito nel campo “Riferimento temporale”; Ad esempio se si definisce un riferimento temporale uguale a “A” e una frequenza uguale a “2” allora il campo “ID Timing” viene popolato automaticamente con “2A”.

: campo in cui l’utente specifica il numero di giorni di massimo posticipo rispetto alla “Data Modello” dell’OdL. Inoltre, viene utilizzato per calcolare la “Data di massimo posticipo” nell’OdL uguale a “Data Modello” + “Massimo posticipo attività”;

Massimo anticipo attività: Segue logiche differenti in base alla policy:

Per le policy periodiche infra-annuali (frequenza <= 1 anno) il campo non è valorizzabile dall’utente e rimane vuoto. In questo caso la data “non prima di” viene calcolata nel seguente modo:

Per gli infra-annuali con frequenza uguale a 1 mese, la data “non prima di” è uguale al primo giorno del mese.

Per gli infra-annuali con frequenza uguale a 2 mesi, la data “non prima di” è uguale al primo giorno del bimestre

Per gli infra-annuali con frequenza uguale a 3 mesi, la data “non prima di” è uguale al primo giorno del trimestre

E così via per il resto delle frequenze infra-annuali

Per i controlli periodici inter-annuali (frequenza > 1 anno) e on condition il campo è obbligatorio e l’utente specifica il numero di giorni di massimo anticipo rispetto alla “Data modello” dell’OdL. In questo modo è possibile calcolare la data “non prima di” come “data modello – massimo anticipo attività”. Se il valore inserito è 0 la data non prima di viene posta uguale alla data di generazione dell’ordine.

Data inizio validità: Data a partire della quale la Policy passa in stato “Attiva” e viene quindi letta dal DSS per la generazione di OdL. È un campo imputabile dall’utente. Al raggiungimento di tale data la policy passa in stato “Attiva”. Se l'utente non valorizza questo campo ed effettua manualmente il passaggio della policy in “Attiva”, il campo viene automaticamente valorizzato con la data del passaggio di stato manuale. Se l’utente valorizza questo campo e, prima della data inserita, effettua manualmente il passaggio della policy in “Attiva”, il campo viene sovrascritto con la data del passaggio di stato manuale.

Data fine validità: Data fino alla quale la Policy deve essere letta dal DSS per la generazione di OdL. Si specifica che la data di fine validità non determina la disattivazione automatica della Policy. Nello specifico, una volta che la Policy si avvicina alla data fine validità (≤ 10 giorni), viene messa in evidenza nel centro di controllo nella sezione “Policy in scadenza” (vedi cap. 3.2); Successivamente, l’utente può scegliere se disattivarla o prolungare la data di fine validità. In mancanza di una di queste due azioni la Policy continua ad essere mostrata nella sezione “Policy in scadenza” del centro di controllo e rimane attiva;

A partire da: campo popolabile solo per “tipo policy” uguale a “periodica”. Rappresenta la data a partire dalla quale può essere posizionato temporalmente l’OdL generato dalla policy. La policy viene letta fin da subito dal DSS, ma gli OdL emessi potranno essere posizionati solo a partire dalla data inserita in questo campo. Di seguito si riportano le logiche relative alla generazione di OdL quando risulta valorizzato il campo “a partire da”:

Se “a partire da” <= 1° Gennaio + frequenza -> gli OdL vengono generati come una normale policy periodica. Es: Policy periodica con frequenza uguale a 3 mesi e data “a partire da” uguale al 15/03. In questo caso verranno generati 4 OdL per anno (31/03, 30/06, 30/09, 31/12).

Se “a partire da”> 1° Gennaio + frequenza -> vengono generati solo gli OdL successivi alla data “a partire da”. Es: Policy periodica con frequenza uguale a 3 mesi e data “a partire da” uguale al 15/04. In questo caso verranno generati 3 OdL per anno (30/06, 30/09, 31/12).

Si specifica che sarà possibile inserire solamente una data “a partire da” > “data attuale”. Una volta che la data attuale supera la data “a partire da”, quest’ultima diventerà obsoleta e non saranno più implementate le logiche precedentemente descritte. Sarà cura dell’utente aggiornare di volta in volta la data “a partire da”.

ID Macro-attività in output: Codice identificativo della macro-attività che deve essere strettamente associata allo stesso asset a cui fa riferimento la policy. A livello di catalogo attività (PdL), la macro-attività può essere associata a più PdL che rappresentano i metodi con cui la macro-attività può essere eseguita. Il DSS alla generazione dell’ordine associa un unico PdL all’ordine. Di seguito si riportano gli step che esegue il DSS per determinare lo specifico PdL da associare all’ordine in fase di generazione:

dal catalogo attività (PdL) nel campo “Attributo per selezione metodo” è presente l’attributo dell’asset sulla base del quale il DSS effettua la scelta del PdL;

nel campo “valore target” è indicato il valore a cui deve corrispondere l’attributo dell’asset su cui si sta generando l’ordine per selezionare il PdL;

Nel caso in cui siano presenti più attività (PdL) per lo stesso “valore target” dell’attributo è indicata tramite un apposito flag nel campo “Attività prevalente per metodo” quello da scegliere.

Nel caso in cui il valore dell’attributo sull’asset non corrisponda a nessuno di quelli elencati nella colonna “valore target”, il DSS seleziona l’attività per la quale è presente il flag “Attività prevalente per metodo” ma non è popolata la colonna “Attributo per selezione metodo” e la colonna “valore target”.

Input: Tabella dedicata alla definizione degli input associati alla Policy in oggetto. Nella tabella sono presenti le seguenti informazioni:

N° input: Identificativo progressivo dell’input valorizzato automaticamente dal sistema;

Tipologia: Campo selezionato da un elenco valori che determina la tipologia degli input presenti nella tabella. Il campo può assumere i seguenti valori;

Health Index;

Tassi di guasto;

Tassi di anomalia;

Attributi anagrafici: possono essere indicati in input attributi anagrafici riferiti a specifiche tecniche o a campi di testata (compresi nella seguente lista: Società, Campo Sort, CdL Resp, In funzione dal, Gruppo Resp. Pianificazione manutenzione, Controlling area, CdC, Definizione Sede Tecnica, Codice Sede Tecnica, Stato Utente, Divisione di Ubicazione, Società Proprietaria, Impianto, Costruttore componente, Funziona elemento, Riferimento Fase, Tipo Componente) che fanno riferimento esclusivamente all’asset associato alla policy;

Difformità: il DSS legge sullo specifico asset solo le difformità in stato “Validata”;

Variabile: per selezionare una variabile o una misura (indicato nella descrizione del valore);

Attività (PdL): necessario quando si vuole definire una policy che in input ha un certo tipo di attività. In questo caso il DSS considera solo gli OdL (che hanno come PdL quello inserito nel campo “Attività (PdL)”) in stato “Aperto”, “Schedulabile”, “Eseguibile”, “Schedulato”, “Avviato”, “Inviato”;

Interferenze;

Opere interferenti Nel caso in cui l’opera interferente sia in stato “ESISTENTE”, il DSS legge l’attributo “Criticità” in input alle policy per generare delle attività di manutenzione, La criticità viene definita in base agli attributi “Tipologia”, “Sotto-tipologia” e “Distanza orizzontale” dell’opera interferente e possono assumere i valori 0 e 1;

Figura 49: Lista valori tipologia input

ID Input: Codice identificativo dell’Input selezionato manualmente dall’utente da una lista pre-filtrata dal sistema in base alla “Tipologia” selezionata;

Descrizione Input: Descrizione dell’input inserito.

Misura: Codice identificativo della misura selezionato manualmente dall’utente da una lista contente le possibili misure da catalogo associate alla tipologia elemento della policy. Per selezionare la misura, si inserisce tipologia input “Variabile”.

Operatore/funzione: Campo selezionato manualmente dall’utente da una lista contenente gli operatori logici (>, >=, <, <=, =, ≠, Pari, Dispari, EXIST, NOT EXIST)

Target di raffronto numerico: valore numerico inserito liberamente dall’utente. Il sistema non permette il salvataggio della policy se è presente un valore sia nel campo “target di raffronto numerico” che in “target di raffronto”;

Target di raffronto: Target di raffronto associato ad ogni input. Il “Target di raffronto” è selezionabile da una lista elenco filtrata in base alla “Tipologia” di input;

Livello di combinazione input: Campo popolato manualmente che determina il numero di combinazioni logiche tra gli input. Il campo può assumere i valori 0, 1, 2 o 3 e definisce le seguenti logiche di gestione, approfondite nel paragrafo 2.2.11.2:

0: Policy con singolo input e singolo target;

1: Policy con singolo input e target multiplo o Policy con input multiplo e combinazione a singolo livello;

2: Policy con input multiplo e combinazione a doppio livello;

3: Policy con input multiplo e combinazione a terzo livello;

Policy aggregata: flag utilizzato per indicare che la policy è di tipo “aggregata”. Visibile esclusivamente per le policy aggregate in sola visualizzazione;

Livello aggregazione: È in sola visualizzazione e riporta esclusivamente per le policy aggregate il livello di aggregazione sotto il quale vengono generati gli ordini;

Sotto-programma: Campo popolato manualmente dall’utente tramite una lista valori definita a sistema. Di seguito le liste valori da implementare per il campo “Programma” e “Sotto-programma”:

Linee:

| Sotto-programmi |
| --- |
| Campagna Isolatori in porcellana (di cui Antenne) |
| Campagna Isolatori in Composito |
| Campagna Conduttori in rame |
| Campagna Cavi OF |
| Campagna Terminali Cavo Porcellana |
| Campagna Terminali Cavo NKT |

Tabella 2 - Sotto-programmi ammissibili per linee

Stazioni:

| Sotto-programmi |
| --- |
| Campagna Scaricatori AT P&V |
| Campagna Batterie SA |
| Campagna Convertitori di Misura TM/TS |
| Campagna Sezionatori Msab/P420 (NMG) |
| Campagna TA in Olio |
| Campagna TA SF6 |
| Campagna Interruttori SF6 |
| Campagna TVC e TVI in porcellana |

Tabella 3 - Sotto-programmi ammissibili per stazioni

Cluster: Campo popolato manualmente dall’utente tramite una lista valori definita a sistema. Il valore inserito in questo campo sarà ereditato dall’OdL generato dalla policy;

Eleggibilità: Flag utilizzato per identificare l’eleggibilità dell’attività. Se selezionato nella policy, tutti gli OdL generati avranno automaticamente questo flag attivato.

Combinazione input: campo che riporta la rappresentazione della combinazione degli input definita tramite maschera.

Figura 51 - Vista di dettaglio Policy

#### Logiche di combinazione input

Come già menzionato in questo capitolo, le Policy possono avere diverse combinazioni di input e target di raffronto. In particolare, è possibile distinguere le Policy in:

Policy elementari: definite utilizzando un singolo input associato a un singolo target di raffronto;

Policy combinate: definite utilizzando diverse combinazioni logiche tra gli input e i target di confronto.

Le logiche di combinazione degli input sono definite utilizzando operatori logici e livelli di combinazione. Gli operatori logici definiscono il modo in cui gli input devono essere combinati tra loro. Per le Policy combinate gestite in Maximo, sono previsti gli operatori logici “AND”, “OR”, “=” e “≠”.

Il livello di combinazione, invece, determina il numero totale di combinazioni logiche previste per una specifica Policy. I livelli di combinazione inseribili sono 0 (Policy elementari), 1, 2 e 3 (Policy combinate).

Le Policy combinate ed elementari si suddividono logicamente in tre gruppi distinti rappresentati in Tabella 4.

| Policy | Input | Target di raffronto | Livello di combinazione ammissibili | Operatori logici ammissibili |
| --- | --- | --- | --- | --- |
| Elementare | Singolo | Singolo | 0 |  |
| Combinata | Singolo | Multiplo | 1 / 2 / 3 | AND / OR / = / ≠ |
| Combinata | Multiplo | Multiplo | 1 / 2 / 3 | AND / OR / = / ≠ |

Tabella 4 - Caratteristiche delle Policy in base al numero di input e target di raffronto

Nel seguito di questo paragrafo viene descritto nel dettaglio l’inserimento degli Input nel caso delle Policy elementari e delle Policy combinate.

##### Policy elementari

Le policy elementari sono definite utilizzando un singolo input associato a un singolo target di raffronto.

Considerando che le policy elementari non prevedono combinazioni logiche tra gli input, il campo “Livello combinazione” deve essere valorizzato con “0”. In questo modo il sistema non predispone all’utente la tabella di combinazione logica degli input.

Cliccando il pulsante “+” presente nella tabella di input (Figura 52) l’utente ha la possibilità di aggiungerne uno nuovo. Per inserire un nuovo input, l’utente deve attribuire manualmente la tipologia di input desiderato nella colonna “Tipologia” e successivamente selezionare l’input specifico da un elenco di input ammissibili per la tipologia selezionata. Successivamente l’utente seleziona misura (se presente), operatore e target di raffronto.

Si specifica che nel momento in cui si inserisce un livello di combinazione pari a “0” il sistema permetterà l’inserimento di una e una sola riga di input.

In Figura 52 si riporta la maschera di creazione delle policy elementari.

Figura 52 - Vista di dettaglio di una Policy elementare

##### Policy combinate

Le policy combinate si distinguono in due gruppi:

Policy con input unico e target di raffronto multiplo;

Policy con input multiplo e target di raffronto multiplo;

Considerando che le policy combinate prevedono delle combinazioni logiche tra gli input, il campo “Livello combinazione” deve essere valorizzato con 1, 2, o 3. In tal modo, il sistema predispone all’utente il numero necessario di tabelle per definire le combinazioni logiche, a seconda del livello desiderato.

###### Input unico e target di raffronto multiplo

Questo paragrafo approfondisce il processo di combinazione logica di uno stesso input con target di raffronto diversi. Di seguito degli esempi di combinazioni che ricadono in questa tipologia:

Input 1 (target 1) & Input 1 (target 2)

Input 1 (target 1) OR Input 1 (target 2)

Il processo di inserimento e combinazione degli input avviene come segue:

L’utente clicca il pulsante “+” presente nella tabella di “Input” per aggiungere un nuovo input.

Una volta fatto ciò è necessario attribuire manualmente la tipologia di input desiderato nella colonna “Tipologia” e successivamente selezionare l’input specifico da un elenco di input ammissibili per la tipologia selezionata. Infine, l’utente seleziona il target di raffronto dall’elenco di target ammissibili.

L’utente procede con la combinazione logica tra gli input. Inserendo il valore 1 nel campo “Livello di combinazione Input”, il sistema predispone la tabella “Combinazione input di 1° livello”, rappresentata in Figura 53.

Nel campo “N° Input” della tabella “Combinazione input di 1° livello”, l’utente inserisce l’identificativo progressivo del primo input. Successivamente, sotto l’operatore logico desiderato (AND / OR), inserisce l’identificativo progressivo del secondo input. Nel campo “Combinazione input” è possibile vedere la formula che riporta la combinazione degli input effettuata tramite maschera.

Figura 53 - Vista di dettaglio di una Policy con input unico e target di raffronto multiplo

###### Input multiplo e target di raffronto multiplo

Questo paragrafo approfondisce il processo di combinazione logica di 1°, 2° e 3° livello tra input multipli con target di raffronto diversi. Di seguito degli esempi di combinazioni che ricadono in questa tipologia:

Policy con input multiplo e combinazione a un livello:

Input 1 & Input 2 & Input 3

Input 1 OR Input 2

Input 1 = Input 2

Input 1 <> Input 2

Policy con input multiplo e combinazione a 2 livelli:

(Input 1 & input 2) OR Input 3

(Input 1 OR input 2 OR Input 3) AND Input4

Policy con input multiplo e combinazione a 3 livelli:

(Input 1 & input 2 & Input 3) OR Input4 OR (Input 4 AND Input 5)

###### Input multiplo e combinazione a un livello

Il procedimento di inserimento e combinazione degli input è lo stesso descritto nel par. 2.2.11.2.2.1. L’unica differenza è che in questa casistica, nella tabella “Input”, vengono inseriti input diversi.

Figura 54 - Vista di dettaglio di una policy con input multiplo e combinazione a un livello

###### Input multiplo e combinazione a 2 livelli

Il processo di inserimento e combinazione degli input avviene come segue:

L’utente clicca il pulsante “+” presente nella tabella di “Input” per aggiungere un nuovo input.

Una volta fatto ciò è necessario attribuire manualmente la tipologia di input desiderato nella colonna “Tipologia” e successivamente selezionare l’input specifico da un elenco di input ammissibili per la tipologia selezionata. Infine, l’utente seleziona il target di raffronto dall’elenco di target ammissibili.

L’utente procede con la combinazione logica tra gli input. Inserendo il valore 2 nel campo “Livello di combinazione Input”, il sistema predispone le tabelle “Combinazione input di 1° livello” e “Combinazione input di 2° livello”;

Nel campo “N° Input” della tabella “Combinazione input di 1° livello”, l’utente inserisce l’identificativo progressivo del primo input. Successivamente, sotto l’operatore logico desiderato, inserisce l’identificativo progressivo del secondo input.

Figura 55 - Vista di dettaglio di una Policy combinata al secondo livello e compilazione della prima tabella

L’utente popola il campo “N° Input” della tabella “Combinazione input 2° livello” con gli input impostati nella tabella “Combinazione input 1° livello” (Figura 55). In questo modo il sistema recupera la combinazione di input specificata nella prima tabella. Successivamente, sotto l’operatore logico desiderato, l’utente inserisce gli identificativi progressivi degli altri input. Nel campo “Combinazione input” è possibile vedere la formula che riporta la combinazione degli input effettuata tramite maschera.

Figura 56 - Vista di dettaglio di una Policy combinata al secondo livello e compilazione della seconda tabella

Si specifica che non è possibile cambiare il livello di combinazione dopo aver valorizzato le tabelle di combinazione. Se per esempio ci sono 2 tabelle di combinazione valorizzate e si prova a cambiare il livello di combinazione inserendo “1”, il sistema non permetterà il salvataggio. Sarà necessario eliminare il contenuto della tabella di combinazione di 2° livello e poi cambiare il livello di combinazione in “1”.

###### Input multiplo e combinazione a 3 livelli

Il procedimento di inserimento e combinazione degli input è del tutto analogo a quello descritto nel precedente paragrafo (Input multiplo e combinazione a 2 livelli). Le uniche differenze sono:

Nel campo “Livello combinazione input” è necessario inserire il valore “3”;

Oltre alle tabelle “Combinazione input 1° livello” e “Combinazione input 2° livello” è necessario valorizzare anche la tabella “Combinazione input 3° livello” con le stesse logiche precedentemente descritte.

Figura 57 - Vista di dettaglio di una Policy combinata al terzo livello e compilazione della terza tabella

Si specifica che non è possibile cambiare il livello di combinazione dopo aver valorizzato le tabelle di combinazione. Se per esempio ci sono 3 tabelle di combinazione valorizzate e si prova a cambiare il livello di combinazione inserendo “2”, il sistema non permetterà il salvataggio. Sarà necessario eliminare il contenuto della tabella di combinazione di 3° livello e poi cambiare il livello di combinazione in “2”.

##### Policy aggregate

Le Policy di aggregazione gestiscono le casistiche per cui la generazione di attività di sostituzione non dipende strettamente dalle condizioni del singolo asset, ma da fattori legati alle condizioni di tutti gli asset di stessa tipologia di una intera linea aerea o stazione. Le policy aggregate sono già definite a sistema e caricate a catalogo, l’unica modifica che l’utente può effettuare riguarda il target di raffronto numerico con cui deve essere confrontato il K di eleggibilità in input alla policy, tramite una dialog dedicata. La maschera di modifica degli S eleggibilità è accessibile tramite un’azione nell’applicazione “Catalogo policy”, denominata “S eleggibilità linee” e “S eleggibilità stazione”.

I K eleggibilità e i relativi target di soglia, necessari per la definizione delle Policy aggregate, saranno gestiti come di seguito riportato:

K eleggibilità: A sistema sono cablate le regole di calcolo dei coefficienti di eleggibilità che sono utilizzati nelle policy di aggregazione per determinare le condizioni generali dei componenti di una linea/stazione;

Target di soglia: gestibili tramite l’azione nel menù di sinistra nell’applicazione policy denominato “S eleggibilità linee” e “S eleggibilità stazione”.

Nel seguito del capitolo sono descritte le formule dei K che saranno implementate a sistema.

###### Conduttori

Si definisce per il 4° livello Conduttore un parametro, denominato coefficiente di eleggibilità del conduttore, di valore variabile tra 0 e 1. Tale coefficiente assume valore pari a 1 nel caso in cui la somma dell’HI di vetustà e l’HI di degrado del 4° livello conduttore supera il valore della soglia di eleggibilità oppure nel caso sia già presente sul IV livello conduttore un’attività di sostituzione conduttore (in stato “Aperto”, “Schedulabile”, “Schedulato”, “Inoltrato”, “Avviato”, “Eseguito parzialmente”). Il valore del coefficiente di eleggibilità del conduttore assumerà invece un valore proporzionale all’attuale valore della somma dell’HI vetustà e dell’HI degrado (di seguito si farà riferimento al parametro HIvet.+deg.) rapportata alla soglia di eleggibilità nel caso in cui il valore non superi la soglia di eleggibilità. Definito il parametro della soglia di eleggibilità il coefficiente di eleggibilità può essere valutato nel seguente modo.

Calcolo keleg_cond

Se OR Attività di sostituzione conduttore -> ;

Se -> .

Dove rappresenta il valore della soglia di eleggibilità ed è riportato nel catalogo dei modelli target.

Definito il coefficiente di eleggibilità del 4° livello il sistema valuterà il coefficiente di eleggibilità del 3° livello conduttore come il valore massimo dei coefficienti dei 4° livelli sottostanti.

Si passa in seguito a valutare il valore del coefficiente di eleggibilità dell’intera tratta di tesatura in cui è compresa ogni singola campata. Questo sarà pari al valore del massimo dei coefficienti di eleggibilità dei 3° livelli conduttore sottostanti. Questo valore deve essere poi moltiplicato per il rapporto tra la lunghezza della tratta in esame e la lunghezza dell’intera linea.

Calcolo Keleg_tratta_cond

Dove:

è il Keleg_cond maggiore tra le campate della stessa tratta;

è la lunghezza della tratta definita come somma delle lunghezze delle campate che la compongono;

è la lunghezza totale della linea definita come somma delle lunghezze delle tratte che la compongono.

Si procede infine al calcolo del coefficiente di eleggibilità dell’intera linea. Questo sarà dato dalla somma dei coefficienti di eleggibilità delle singole tratte di tutta la linea.

Calcolo Keleg_linea_cond

Una volta determinato il è possibile procedere alla definizione delle attività di sostituzione confrontando il coefficiente con un valore soglia Seleg_linea_cond (sempre uguale a 0,95). Il valore Seleg_linea_cond=0,95 è riportato direttamente come target di raffronto nella dialog “S eleggibilità linee”. In base ai valori assunti dal coefficiente di eleggibilità della linea è prevista la seguente logica:

Se & almeno una campata -> generazione di n OdL “Sostituzione conduttore” (3° livello) tanti quanti sono i conduttori di terzo livello sottesi alla linea e un ordine aggregato per singola linea.

###### Funi di guardia

Si definisce per il 4° livello Fune di Guardia un parametro, definibile coefficiente di eleggibilità della fune di guardia, di valore variabile tra 0 e 1. Tale coefficiente assume valore pari a 1 nel caso in cui la somma dell’HI di vetustà e l’HI di degrado del 4° livello fdg (fune di guardia) supera il valore della soglia di eleggibilità oppure nel caso sia già presente sul 4° livello fune di guardia un’attività di sostituzione fune di guardia trainante ((in stato “Aperto”, “Schedulabile”, “Schedulato”, “Inoltrato”, “Avviato”, “Eseguito parzialmente”).. Il valore del coefficiente di eleggibilità della fune di guardia assumerà invece un valore proporzionale all’attuale valore della somma dell’HI vetustà e dell’HI degrado (di seguito si farà riferimento al parametro HIvet.+deg.) rapportata alla soglia di eleggibilità nel caso in cui il valore non superi la soglia di eleggibilità. Definito il parametro della soglia di eleggibilità il coefficiente di eleggibilità può essere valutato nel seguente modo.

Calcolo keleg_fdg

Se OR Attività di sostituzione fdg -> ;

Se -> .

Dove rappresenta il valore della soglia di eleggibilità ed è riportato nel catalogo dei modelli target.

Definito il coefficiente di eleggibilità del 4° livello il sistema valuterà il coefficiente di eleggibilità del 3° livello fune di guardia come il valore massimo dei coefficienti dei 4° livelli sottostanti.

Si passa in seguito a valutare il valore del coefficiente di eleggibilità dell’intera tratta di tesatura in cui è compresa ogni singola campata. Questo sarà pari al valore del massimo dei coefficienti di eleggibilità dei 3° livelli fune di guardia sottostanti. Questo valore deve essere poi moltiplicato per il rapporto tra la lunghezza della tratta in esame e la lunghezza dell’intera linea.

Calcolo Keleg_tratta_fdg

Dove:

è il Keleg_fdg maggiore tra le campate della stessa tratta;

è la lunghezza della tratta definita come somma delle lunghezze delle campate che la compongono;

è la lunghezza totale della linea definita come somma delle lunghezze delle tratte che la compongono.

Si procede infine al calcolo del coefficiente di eleggibilità dell’intera linea. Questo sarà dato dalla somma dei coefficienti di eleggibilità delle singole tratte di tutta la linea.

Calcolo Keleg_linea_fdg

Una volta determinato il è possibile procedere alla definizione delle attività di sostituzione confrontando il coefficiente con un valore soglia Seleg_linea_fdg (sempre uguale a 0,95). Il valore Seleg_linea_fdg=0,95 è riportato direttamente come target di raffronto nella dialog “S eleggibilità linee”. In base ai valori assunti dal coefficiente di eleggibilità della linea è prevista la seguente logica:

Se & almeno una campata -> generazione di n OdL “Sostituzione fune di guardia” (4° livello) tante quante sono le funi di guardia di quarto livello sottese alla linea e un ordine aggregato per singola linea.

###### Strutture

Il calcolo degli indici di eleggibilità delle strutture segue gli stessi step descritti per il calcolo degli indici di eleggibilità dei conduttori e delle fdg. Si calcola inizialmente l’indice di eleggibilità della singola struttara in funzione della somma dell’HI di vetustà e l’HI relativo al tasso di guasto della tipologia della struttura. Se questo supera la soglia di eleggibilità definita per e strutture o se è presente una attività di sostituzione della struttura (in stato “Aperto”, “Schedulabile”, “Schedulato”, “Inoltrato”, “Avviato”, “Eseguito parzialmente”). l’indice assume valore pari a 1, altrimenti assume un valore proporzionale all’HIvet.+tdg..

Calcolo Keleg_struttura

Se OR Attività Sostituzione struttura -> ;

Se ->.

Dove rappresenta la soglia di eleggibilità ed è modificabile attraverso l’applicazione sul catalogo policy “S eleggibilità linee”.

Calcolato l’indice di eleggibilità delle singole strutture è possibile calcolare l’indice di eleggibilità per l’intera linea. Questo sarà semplicemente dato dal rapporto tra la somma degli indici di eleggibilità delle singole strutture e la numerosità delle strutture della linea.

Calcolo Keleg_linea_struttura

Dove rappresenta il numero dei sostegni della linea.

Una volta determinato il è possibile procedere alla definizione delle attività di sostituzione confrontando il coefficiente con un valore soglia Seleg_linea_struttura (sempre uguale a 0,95) riportato direttamente come target di raffronto nella dialog “S eleggibilità linee”. In base ai valori assunti dal coefficiente di eleggibilità della linea è prevista la seguente logica:

Se & almeno un sostegno -> generazione di n attività “Sostituzione struttura” (4° livello) per tutte le strutture sottese alla linea;

###### Stazioni

Le policy aggregate di sostituzione per le stazioni hanno come riferimento principale le singole unità funzionali rappresentate dai singoli stalli. Per le policy su stazione, è possibile aggregare esclusivamente su terzo livello.

A seconda della tipologia di asset di 4° livello questo potrà avere una più o meno marcata rilevanza nel determinare l’applicazione delle policy di aggregazione. Prima di definire gli indici di eleggibilità in maniera analoga a quanto fatto per le linee aeree è necessario quindi definire dei coefficienti che tengano conto della rilevanza dei IV e III livelli:

KpIV livello: coefficiente attribuito a ogni singolo equipment di 4° livello in funzione all’importanza;

Kpstallo: coefficiente attributo a ogni singolo stallo dato dalla sommatoria dei KpIV livello dei 4° livelli sottesi.

I coefficienti KpIV livello varieranno in funzione dell’equipment di 4° livello. Si riporta un esempio di seguito.

| IV livello | KpIV livello |
| --- | --- |
| Interruttore AIS | 61 |
| Sezionatore AIS x | 16 |
| TV AIS x | 7 |
| TA AIS x | 12 |
| Scaricatore AIS x | 3 |

La tabella dei Kp è modificabile attraverso una dialog a cui si accede tramite l’azione “S eleggibilità stazioni” nel menù di sinistra nell’applicazione policy.

Si definisce quindi il coefficiente di eleggibilità dei singoli 4° livelli. Per i 4° livelli eleggibili, ovvero quelli per cui sia stata generata un’attività di sostituzione (in stato “Aperto”, “Schedulabile”, “Schedulato”, “Inoltrato”, “Avviato”, “Eseguito parzialmente”), questo sarà pari al rapporto tra il KpIV livello e il Kpstallo. Per i 4° livelli non eleggibili l’indice di eleggibilità sarà pari al rapporto tra il KpIV livello e il Kpstallo moltiplicato per il rapporto tra il parametro responsabile della determinazione dell’eleggibilità () e la soglia di eleggibilità del 4° livello (). Per i 4° livelli di stazione il parametro non calcolato a sistema ma configurato. Il parametro corrisponde alla soglia di eleggibilità dell’equipment (gestito tramite l’applicazione “S eleggibilità stazioni” accessibile dal menù azioni sul catalogo policy).

Calcolo coefficiente di eleggibilità KIV livello:

Se equipment eleggibile ->

Se equipment non eleggibile ->

Il calcolo del coefficiente di eleggibilità dello stallo è dato dalla sommatoria dei coefficienti di eleggibilità dei 4° livelli sottostanti.

Coefficiente di eleggibilità Kstallo:

Sulla base del coefficiente di eleggibilità dello stallo e definendo una soglia di eleggibilità per le policy aggregate di sostituzione dello stallo ( parametro riportato nel catalogo dei modelli target) si definiscono le seguenti logiche di generazione:

Policy di sostituzione interruttori sottesi allo stallo: Se -> Generazione n OdL “Sostituzione Interruttore (4° livello)” tanti quanti sono gli interruttori sottesi allo stallo;

Policy di sostituzione sezionatori sottesi allo stallo: Se -> Generazione n OdL “Sostituzione Sezionatore (4° livello)” tanti quanti sono i sezionatori sottesi allo stallo;

Policy di sostituzione TV sottesi allo stallo: Se -> Generazione n OdL “Sostituzione TV (4° livello)” tanti quanti sono i sezionatori sottesi allo stallo;

Policy di sostituzione TA sottesi allo stallo: Se -> Generazione n OdL “Sostituzione TA (4° livello)” tanti quanti sono i sezionatori sottesi allo stallo;

Policy di sostituzione scaricatori sottesi allo stallo: Se -> Generazione n OdL “Sostituzione scaricatore (4° livello)” tanti quanti sono i sezionatori sottesi allo stallo.

Come si evince dalle logiche sopra riportate, è necessario creare una policy aggregata per ognuno dei quarti livelli sottesi allo stallo per cui si vogliono applicare le logiche.

Infine, il coefficiente di eleggibilità della sezione, tenendo conto dei diversi pesi associati a ciascuno stallo sottostante, sarà dato dalla sommatoria dei prodotti dei coefficienti di eleggibilità degli stalli per il rispettivo peso rispetto alla sommatoria di tutti i pesi degli stalli.

Coefficiente di eleggibilità Ksezione:

Sulla base del coefficiente di eleggibilità della sezione e di una soglia di eleggibilità per la sezione ( Parametro riportato nel catalogo dei modelli target) si definiscono le seguenti logiche di generazione:

Policy di sostituzione interruttori sottesi alla sezione: Se -> Generazione n OdL “Sostituzione Interruttore (4° livello)” tanti quanti sono gli interruttori sottesi alla sezione

Policy di sostituzione sezionatori sottesi alla sezione: Se -> Generazione n OdL “Sostituzione Sezionatore (4° livello)” tanti quanti sono i sezionatori sottesi alla sezione

Policy di sostituzione TV sottesi alla sezione: Se -> Generazione n OdL “Sostituzione TV (4° livello)” tanti quanti sono i TV sottesi allo stallo;

Policy di sostituzione TA sottesi alla sezione: Se -> Generazione n OdL “Sostituzione TA (4° livello)” tanti quanti sono i TA sottesi allo stallo;

Policy di sostituzione scaricatori sottesi alla sezione: Se -> Generazione n OdL “Sostituzione scaricatore (4° livello)” tanti quanti sono gli scaricatori sottesi allo stallo.

Come si evince dalle logiche sopra riportate, è necessario creare una policy aggregata per ognuno dei quarti livelli sottesi allo stallo per cui si vogliono applicare le logiche.

#### Stati e azioni sulle Policy

Le Policy nel loro ciclo di vita sono caratterizzate da specifici stati. In Figura 59 sono rappresentati tutti gli stati associabili alle Policy dal momento in cui vengono generate fino alla loro disattivazione. Inoltre, sono rappresentati anche i passaggi di stato consentiti dal sistema.

Figura 59 - Stati Policy

La modifica di stato di una Policy può avvenire automaticamente oppure manualmente da parte dell’operatore abilitato. Come illustrato nella tabella sottostante, le azioni eseguibili sulle Policy sono strettamente legate a rispettive transizioni di stato.

| Passaggio di stato | Processo |
| --- | --- |
| Bozza à Attiva | Attivazione |
| Non attiva à Attiva | Attivazione |
| Attiva à Pending Review | Modifica |
| Pending Review à Attiva | Modifica |
| Pending Review à Non attiva | Disattivazione |
| Attiva à Non attiva | Disattivazione |

Tabella 5 - Relazione tra processo e passaggio di stato

Nei seguenti paragrafi vengono descritti i processi di attivazione, modifica e disattivazione delle Policy all’interno di Maximo.

##### Attivazione di una policy

Lo stato di Bozza è il primo che viene associato alla Policy. In questo stato non è ancora obbligatorio compilare tutti i campi che caratterizzano la Policy.

Il passaggio allo stato “Attiva” può essere effettuato manualmente dall’utente o automaticamente dal sistema al raggiungimento della data inizio validità. Lo stato “Attivo” è l’unico in cui la Policy viene letta dal DSS per la generazione degli OdL. Per l’attivazione di una Policy devono essere popolati i seguenti campi obbligatori:

Gerarchia asset associato;

Elemento;

Classificazione asset (deve essere presente almeno un record);

Tipo Policy;

Riferimento temporale;

Frequenza/Differibilità;

Massimo posticipo attività;

Massimo anticipo attività;

ID PdL in output: Solo i PdL in stato “Attivo”;

Livello combinazione input;

Input.

Tabella di combinazione input: nel caso in cui il livello di combinazione sia maggiore di zero viene effettuato un controllo per verificare che le tabelle di combinazione input non siano vuote

Inoltre, affinché la policy possa essere attivata ogni input deve trovarsi in un determinato stato nei rispettivi cataloghi, come riportato nella tabella sottostante:

| Tipologia input | Stato input necessario per attivazione policy |
| --- | --- |
| Indicatori di condizione tecnica (HI, TdG, TdA, K eleggibilità) | Attivo (flag) |
| Attributi anagrafici | N.A. |
| Difformità | ATTIVA |
| Misure | N.A. |
| Variabili di stato | N.A. |
| Interferenze | ATTIVA |
| Attività (PdL) | ATTIVA |
| Opere interferenti | ESISTENTE |

Nel caso in cui si raggiunga la data di inizio validità senza avere tutti i campi sopra elencati valorizzati, allora la policy rimarrà in stato “Bozza”. Per dare evidenza di queste policy viene predisposta una vista nel centro di controllo (cfr. par. 2.2.11.5.3). Non appena l’utente inserisce tutti i campi richiesti, la policy viene attivata con data inizio validità uguale a quella inizialmente definita.

##### Modifica di una Policy

Lo stato “Pending Review” caratterizza le policy per le quali è possibile apportare modifiche. Le policy in stato “Pending Review” non vengono lette dal DSS non producendo quindi nuovi OdL o modifiche / cambi stato su OdL esistenti. Il passaggio allo stato “Pending Review” e la modifica di una Policy possono avvenire tramite due modalità distinte, descritte nei paragrafi successivi.

###### Passaggio manuale allo stato “Pending Review”

In questo caso lo stato della Policy viene impostato a “Pending Review” su input dell’utente nel momento in cui accede alla modalità di modifica tramite la funzionalità “Revisione policy”. Il processo di modifica manuale delle Policy è illustrato nello schema sottostante (Figura 60) dove in verde sono indicate le fasi del processo in cui la Policy è in stato “Pending Review”, mentre in blu quelle in cui la Policy è in stato “Attiva”.

Figura 60 - Processo di modifica manuale delle Policy

Nello schema sono rappresentati i seguenti passaggi:

Passaggio della Policy in stato “Pending Review” tramite funzionalità di “Revisione Policy”

Modifica della Policy: Una volta in stato “Pending Review”, dalla maschera di dettaglio della Policy è possibile modificare tutti i campi presenti in schermata, eccetto il campo “ID Policy” che rappresenta il codice univoco della policy ed il campo “tipo policy”;

Dal centro di controllo, l’utente può visualizzare tutti gli OdL relazionati alle Policy in “Pending review”. Tramite questa vista è possibile identificare gli OdL che sarebbero impattati dalle modifiche nel momento in cui le Policy vengono riattivate (passaggio di stato eseguito dall’utente “Pending review” > “Attiva”).

Dopo aver apportato le modifiche necessarie, sono possibili due opzioni:

Confermare la modifica: L’utente conferma le modifiche apportate alla Policy cambiandone lo stato in “Attiva”. Si specifica che per riattivare la Policy devono essere rispettate le condizioni di attivazione descritte nel paragrafo 2.2.11.3.1. Dopo l’attivazione vengono automaticamente propagate, solo su OdL esistenti in stato “Aperto” o “Da validare”, esclusivamente modifiche di natura temporale (e quindi sulle date), di seguito riportate:

Modifica della “Frequenza”: una modifica di questo campo sulla policy causa la modifica della data modello dell’OdL solo se in stato “Aperto” e con data modello nel futuro. Vengono anche sovrascritte la “data pianificata fine” (posta uguale alla nuova data modello) e la “data pianificata inizio” (calcolata come nuova data pianificata fine – durata in giorni del PdL). Inoltre, viene resettato il campo “Data pianificata modificata” portandolo a NO (se già popolato a SI). Conseguentemente alla modifica della data modello, vengono anche aggiornate le date “a partire da” e “massimo posticipo”. Si specifica che non è possibile modificare la frequenza portandola da un valore minore o uguale a 1 anno (infra-annuale) ad un valore superiore ad un anno (inter-annuale). La modifica degli ordini già generati parte dal momento della riattivazione della policy modificata e viene ricalcolato il valore della data modello a partire dalla data modello del primo ordine. In fase di modifica il campo “a partire da” è inibito e non può essere modificato. Per maggiore dettaglio sulle regole di modifica delle date degli ordini, si rimanda al documento DSS.

Modifica “Differibilità”: una modifica di questo campo sulla policy causa la modifica della data modello dell’OdL. Le logiche di dettaglio per la modifica di questo campo sono descritte nel documento DSS;

Modifica “Max anticipo”: una modifica di questo campo sulla policy causa la modifica della data “non prima di” dell’OdL secondo le logiche descritte nel par. 2.2.11.1;

Modifica “Max posticipo”: una modifica di questo campo sulla policy causa la modifica della data “di massimo posticipo” dell’OdL secondo le logiche descritte nel par. 2.2.11.1;

Si specifica che in caso di modifica dei restanti campi della policy non saranno apportate modifiche agli OdL esistenti e non saranno applicate logiche di chiusura. Esempio: nel caso in cui si modifichi “ID PdL in output” e qualsiasi parametro all’interno di “Input”, sugli OdL esistenti (precedentemente generati da questa policy) non accadrà nulla e continueranno ad avere la struttura e il contenuto iniziale. Di conseguenza la “nuova” policy modificata genererà nuovi OdL (con il nuovo “ID PdL in output”) solo quando soddisfatti i nuovi input.

Non confermare la modifica: l’utente, dopo aver esaminato gli impatti sugli OdL dal centro di controllo, decide di non procedere con la riattivazione della Policy. In questo caso, se necessario, è possibile apportare ulteriori revisioni per poter procedere successivamente alla riattivazione.

L’utente riattiva la Policy tramite il passaggio di stato a “Attiva” oppure inserendo la data di inizio validità (data in cui la policy passa in stato “Attiva”). L’utente può visualizzare nel centro di controllo gli OdL modificati/generati dalla nuova versione della Policy.

###### Passaggio automatico allo stato “Pending Review”

Il sistema imposta automaticamente lo stato della Policy a “Pending Review” nel caso in cui un input o l’output associato alla Policy subisca la disattivazione nel rispettivo catalogo. Il processo di modifica manuale delle Policy è illustrato nello schema sottostante (Figura 61), dove in verde sono indicate le fasi del processo in cui la Policy è in stato “Pending Review”, mentre in blu quelle in cui la Policy è in stato “Attiva”.

Figura 61 - Processo di modifica innescato dal sistema

Nello schema sono rappresentati i seguenti passaggi:

Passaggio della Policy in stato “Pending Review” effettuato dal sistema a valle della disattivazione/eliminazione di un input nel catalogo o del PdL in output alla policy all’interno del catalogo attività.

Modifica della Policy: Una volta in stato “Pending Review”, dalla maschera di dettaglio della Policy è possibile modificare tutti i campi presenti in schermata, eccetto il campo “ID Policy” che rappresenta il codice univoco della policy e il “tipo policy”;

Dal centro di controllo, l’utente può visualizzare tutti gli OdL relazionati alle Policy in “Pending review”. Tramite questa vista è possibile identificare gli OdL che sarebbero impattati dalle modifiche nel momento in cui le Policy vengono riattivate (passaggio di stato eseguito dall’utente “Pending review” > “Attiva”).

Dopo aver apportato le modifiche necessarie, sono possibili due opzioni:

Confermare la modifica: L’utente conferma le modifiche apportate alla Policy cambiandone lo stato in “Attiva”. Si specifica che per riattivare la Policy devono essere rispettate le condizioni di attivazione descritte nel paragrafo 2.2.11.3.1. Dopo l’attivazione vengono automaticamente propagate, solo su OdL esistenti in stato “Aperto”, esclusivamente modifiche di natura temporale (e quindi sulle date) come riportato nel paragrafo precedente

Non confermare la modifica: l’utente, dopo aver esaminato gli impatti sugli OdL dal centro di controllo, decide di non procedere con la riattivazione della Policy. In questo caso, se necessario, è possibile apportare ulteriori revisioni per poter procedere successivamente alla riattivazione.

L’utente riattiva la Policy tramite il passaggio di stato a “Attiva”. L’utente può visualizzare nel centro di controllo gli OdL modificati, generati o portati in stato “non eseguito” (se attività prevalente) dalla nuova versione della Policy.

###### Revisione delle modifiche dopo attivazione di una Policy

Nell’eventualità in cui l’utente identifichi un’incongruenza sugli OdL dovuta alla riattivazione di una Policy modificata è possibile procedere come segue:

Dopo aver apportato le modifiche necessarie alla Policy e portato il suo stato in “Attiva”, l’utente potrà visualizzare nel centro di controllo sia gli OdL modificati a valle dalle modifiche, sia quelli nuovi generati a partire della Policy modificata.

Se l’utente individua nel centro di controllo un OdL che presenta incongruenze può selezionarlo per essere reindirizzato all’applicazione di Monitoraggio OdL, dove può effettuare le modifiche necessarie direttamente sull’OdL.

##### Disattivazione di una policy

Il passaggio della Policy allo stato “Non attiva” deve essere sempre eseguito manualmente dall’utente abilitato. Come anticipato, una volta che la Policy si avvicina alla data fine validità (≤ 10 giorni), viene messa in evidenza nel centro di controllo nella sezione “Policy in scadenza” (cfr. paragrafo 2.2.11.5.1). In nessun caso il sistema disattiva automaticamente una Policy; è quindi responsabilità dell’utente decidere se disattivarla manualmente o mantenerla attiva anche dopo la data di scadenza.

Nel momento in cui una Policy viene portata in stato “Non attiva”, non viene più letta dal DSS. L’utente può consultare nel centro di controllo tutte gli OdL impattati dalla disattivazione della Policy (cfr. paragrafo 3.2.2). A valle della disattivazione di una policy potrebbero generarsi delle inconsistenze (già descritte in Analisi funzionale sprint 1) monitorabili tramite centro di controllo e di seguito riportate:

PdL non associati a nessuna policy

Associazione difformità – attività risolutrice per la quale non è definita una policy

##### Visualizzazione storico

L’utente può visualizzare lo storico delle policy accedendo al tab “Visualizza storico” dell’applicazione di gestione delle policy, all’interno del quale sono presenti tanti record tanti quanti sono i cambi di stato della policy. Espandendo il singolo record sarà possibile visualizzare tutti i dati della policy relativi alla versione selezionata. La tabella contenente le versioni della policy è composta dalle seguenti colonne:

Stato: rappresenta lo stato assunto dalla policy (“ATTIVA”, “PENDING REVIEW” e “NON ATTIVA”)

Modificato da: rappresenta chi ha effettuato il cambio stato. Nel caso in cui il cambio stato sia effettuato dall’utente questo campo sarà valorizzato con il numero di matricola. Se invece il cambio stato è avvenuto automaticamente verrà riportato il riferimento al sistema.

Data: data di cambio stato

Figura 62 - Visualizzazione storico Policy

#### Sotto-programma

Considerando che il campo “Programma” presente all’interno del PdL non è sufficiente a caratterizzare dettagliatamente l’ordine di lavoro, all’interno della schermata delle Policy è predisposto un campo “Sotto-programma” (Figura 63). In questo modo l’OdL generato avrà l’informazione del sotto-programma ereditata dalla Policy. Si specifica che non può presentarsi il caso in cui più Policy (con stesso PdL in output, riferite allo stesso asset ma con sotto-programmi differenti) generino più OdL con sotto-programmi differenti. Difatti secondo le logiche di prevalenza definite per il DSS verrà generato solo l’OdL associato alla policy più stringente, ossia quella con differibilità minore. Inoltre, nel caso in cui n policy abbiano stessa differibilità (e quindi siano ugualmente stringenti) verrà generato solo uno degli n OdL, scelto randomicamente tra quelli proposti.

Figura 63 - Maschera di creazione policy

#### Centro di controllo

##### Policy in scadenza / scadute

In questo paragrafo viene presentato il tab “Policy in scadenza / scadute” del centro di controllo (anticipata nel paragrafo 2.2.11.3) tramite il quale l’utente può visualizzare / accedere alle Policy in scadenza / scadute. Come illustrato in Figura 64, nel tab “Policy in scadenza / scadute” vengono evidenziate in arancione i record relativi alle Policy in stato “Attiva” per le quali il periodo che intercorre fra la data fine validità e la data attuale è pari o inferiore a 10 giorni; Inoltre, in rosso sono riportati i record relativi alle Policy scadute per le quali è stata superata la data fine validità.

Il tab “Policy in scadenza / scadute” riporta in forma tabellare le seguenti informazioni per ciascun record:

ID Policy: Codice univoco che identifica la Policy in scadenza / scaduta;

Data fine validità: Data fino alla quale la Policy deve essere letta dal DSS per la generazione di OdL;

ID PdL in output: Codice identificativo del PdL associato alla Policy in scadenza / scaduta;

Descrizione PdL in output

Tipo Policy: Può assumere i valori “on-condition” o “periodica”;

Riferimento asset: Classificazione asset alla quale è associata la Policy in scadenza / scaduta.

Figura 64 - Sezione del centro di controllo contenente le Policy in scadenza / scadute

Qualora l’utente desideri impostare lo stato di una Policy in scadenza su “Non Attiva”, deve selezionare il record desiderato ed eseguire manualmente il cambio di stato nel catalogo delle Policy. Nel momento in cui viene effettuato il cambio di stato in “Non Attiva” oppure nel caso in cui l’utente modifichi la data di fine validità prolungandola, il relativo record non viene più mostrato nella sezione “Policy in scadenza / scadute” del centro di controllo. Se l’utente non esegue il cambio di stato manuale e la data attuale supera la data fine validità, la Policy viene evidenziata in rosso nel centro di controllo e rimane in stato “Attiva”, essendo letta dal DSS (il sistema non effettua disattivazioni automatiche).

##### Policy in stato “Pending Review”

In questo paragrafo viene presentato il tab “Policy in stato Pending Review” del centro di controllo (menzionata in sezione 2.2.11.3.2), tramite il quale l’utente può visualizzare / accedere alle Policy in stato “Pending Review”. Come illustrato nella Figura 65, nel tab “Policy in stato Pending Review” vengono evidenziati in arancione i record relativi alle Policy che si trovano in stato “Pending Review” da più di 5 giorni, mentre in verde sono riportati i record relativi a Policy in stato “Pending Review” da meno di 5 giorni.

Il tab “Policy in stato Pending Review” riporta in forma tabellare le seguenti informazioni per ciascun record:

ID Policy: Codice univoco della Policy in stato “Pending Review”

Modificato da: riporta la matricola dell’utente o la dicitura “MAXADMIN”, da cui si evince se la Policy è stata impostata in stato “Pending Review” da sistema o manualmente da un’utente;

ID Macro-attività in output: Codice identificativo della Macro-attività associato alla Policy in stato “Pending Review”.

Tipo Policy: Può assumere i valori “on-condition” o “periodica”;

Riferimento asset: Classificazione Asset alla quale è associata la Policy in “Pending Review”;

Data entrata in “Pending Review”: Data in cui lo stato della Policy è stato modificato in “Pending Review”.

Figura 65 - Sezione del centro di controllo contenente le Policy in stato "Pending Review"

Qualora l’utente desideri impostare lo stato di una Policy da “Pending Review ad “Attivo” o “Non Attivo”, può selezionare nel centro di controllo il record desiderato ed eseguire manualmente il cambio di stato nel catalogo delle Policy. A partire dal momento in cui viene effettuato il cambio di stato della Policy in “Attiva” o “Non Attiva”, il relativo record non viene più mostrato nella sezione “Policy in scadenza / scadute” del centro di controllo.

##### Policy in stato “Bozza” (con data inizio validità <= data corrente)

In questo paragrafo viene presentato il tab “Policy in stato Bozza con data inizio validità <= data corrente” del centro di controllo (cfr. par. 2.2.11.3.1) tramite il quale l’utente può visualizzare le policy che, nonostante abbiano raggiunto la data di inizio validità, non sono passate in stato “Attiva” causa la mancata valorizzazione di alcuni campi obbligatori a tale passaggio. Il tab riporta in formato tabellare le seguenti informazioni per ciascun record:

ID Policy: Codice univoco della Policy in stato “Bozza”

Tipo policy: può assumere i valori “on condition” o “periodica”

Riferimento asset: Classificazione Asset alla quale è associata la Policy in “Bozza”

Data inizio validità

Figura 66 - Sezione del centro di controllo contenente le policy in stato "Bozza"

Per accedere direttamente alla policy è necessario cliccare sul record desiderato. In questo modo si viene reindirizzati sulla maschera di configurazione della policy, all’interno della quale è possibile inserire le informazioni mancanti. Si specifica che sarà a cura dell’utente individuare le informazioni mancanti (necessarie all’attivazione della policy).

# Decision support system (DSS)

## Processo DSS

In questo capitolo viene descritto il DSS, ovvero il motore di elaborazione delle Policy utilizzato in EAM per l’esecuzione automatizzata di generazione / modifica / cambio stato degli OdL.

Il DSS può eseguire diversi tipi di elaborazioni in base al tipo di Input e di Policy, riassumibili come segue:

Generazione OdL: il DSS genera OdL nuovi (singolarmente o massivamente a seconda del caso) applicando le regole di generazione definite dall’utente nelle Policy;

Cambio stato OdL: il DSS può portare in stato “Non eseguito” OdL esistenti che risultano prevalsi da OdL generati dal DSS secondo le logiche descritte nel par. 3.1.2.1.2. Si precisa che il cambio stato potrà avvenire solamente per OdL che non presentano consuntivi inviati a SAP e che si trovano in stato precedente a “Schedulabile”.

Modifica OdL: Nel momento in cui una Policy viene elaborata, il sistema effettua un controllo per verificare la presenza di OdL già esistenti con stesso PdL e stesso Asset generati dalla stessa policy. Se esistono OdL già emessi, vengono modificati in ottemperanza alle regole definite nella Policy (ad esempio una differibilità diversa). La modifica di OdL tramite Policy è trattata nel dettaglio nel capitolo 2.2.11.3.2.

A seconda della modalità di innesco del DSS, è possibile identificare due macro-tipologie di esecuzione:

Esecuzione on-condition: OdL elaborati on-condition automaticamente dal sistema al verificarsi di determinate condizioni di input definite nel catalogo delle Policy.

Esecuzione periodica: OdL elaborati massivamente con cadenza annuale dal sistema.

Le due modalità di innesco sono tra loro collegate e costituiscono (insieme ad Input, output e Policy) il processo logico del DSS riportato in Figura 67.

Figura 67 - Processo di elaborazione DSS

L’utente può visualizzare e scaricare in formato excel la lista di OdL generati da Policy “on-condition” o “periodiche” tramite la vista elenco degli OdL nell’applicazione di “Monitoraggio OdL”. In particolare, dopo aver utilizzato il filtro “Tipo Policy” per evidenziare OdL derivanti da Policy “on-condition” o “periodiche”, è possibile scaricare l’elenco di OdL selezionato in formato Excel oppure applicare ulteriori filtri per raffinare ulteriormente il set da esportare (tutti i campi dell’OdL possono essere utilizzati come filtro).

Nei successivi paragrafi vengono approfonditi nel dettaglio il modello logico delle casistiche di esecuzione on-demand / periodiche, e gli use-case dipendenti dalla combinazione di input-policy definiti a sistema.

Modello logico del DSS

In questo paragrafo viene approfondito il modello logico utilizzato dal DSS nei casi di generazione di OdL on-demand e periodici.

#### Modello logico generazione on-condition OdL

Nel caso di OdL generati on-demand dal DSS, viene utilizzato un modello a eventi che attiva l’elaborazione della Policy al verificarsi delle condizioni di input definite dall’utente.

Tale modello, rappresentato in Figura 68, è costituito dai seguenti layer logici:

Layer di processo: eventi di processo eseguiti in EAM che determinano la generazione degli input per le Policy;

Layer di pubblicazione Input: In questo Layer, gli input vengono messi a disposizione per la lettura da parte dalle Policy che verificano se soddisfano i criteri di Input per la generazione degli OdL;

Layer Policy: contiene le Policy che rimangono “in ascolto” del Layer di pubblicazione Input in modo da verificare la presenza degli input necessari al loro innesco. Nel momento in cui vengono soddisfatte le condizioni di Input, le Policy si attivano automaticamente generando gli OdL secondo le regole definite dall’utente;

Layer di Generazione Ordini: contiene gli OdL generati dalle Policy.

Figura 68 - Modello on-condition a eventi

In particolare, gli eventi di processo che portano alla generazione degli input delle Policy on-condition possono essere:

Eventi di processo EAM:

Calcolo indicatori di condizione tecnica: aggiornamento di Health Index, Tassi di Guasto e Tassi di anomalia;

Anagrafica Asset: ciascun attributo anagrafico degli asset (compresi i campi di testata) può essere utilizzato come parametro di input. L’utente può visualizzare e scaricare i dati anagrafici degli asset tramite la relativa applicazione. In particolare, utilizzando i filtri della vista elenco (applicabili ad ogni dato anagrafico dell’asset), è possibile evidenziare gli asset di interesse ed eseguire il download in formato Excel della ricerca effettuata. Si specifica che l’utente centrale ha la possibilità di creare/modificare/eliminare delle liste valori (domini) collegate agli attributi di anagrafica.

Generazione attività: il DSS legge in Input solo gli OdL con stato “Aperto”

Registrazione o variazione gravità Difformità: il DSS legge in input solo le difformità validate. Inoltre, la variazione di gravità può costituire un input per il DSS;

Registrazione Interferenze: registrazione Interferenze durante l’esecuzione di un’attività;

Registrazione Misure: inserimento delle misure rilevate durante un’attività;

Aggiornamento variabili di stato: Aggiornamento delle variabili dovuto alla chiusura di un’attività o ad aggiornamenti anagrafici.

Eventi derivanti da interfacce con sistemi esterni:

Difformità/Interferenze inserite su input del report macchinari/Ariel;

Interferenze inserite su input di Elive;

Difformità inserite su input di cavi interrati.

L’acquisizione degli input da sistemi esterni è descritta in dettaglio nel capitolo 3.3.

La Figura 69 mostra una schematizzazione degli insiemi di Input gestiti dalla logica on-demand ed evidenzia il meccanismo di esecuzione delle singole Policy.

Figura 69 - Modello logico DSS on-demand

Nel momento in cui sono innescate, le Policy vengono utilizzate dal sistema come regola per l’elaborazione di operazioni sugli OdL (generazione / modifica /cambio stato) che in base alla definizione possono essere applicate a un numero variabile di OdL. A seguire, in Figura 70 è riportata una rappresentazione orientata maggiormente all’operatività utente nella gestione del catalogo delle Policy, evidenziando la dipendenza di ciascuna Policy da un differente set di Input e il conseguente impatto su un differente battente di OdL.

Figura 70 - Gestione catalogo Policy

#### Modello logico generazione periodica OdL

La generazione periodica consiste nell’esecuzione del processo massivo di elaborazione delle Policy periodiche che porta alla generazione / modifica degli OdL di controllo.

Tale processo di elaborazione è eseguito automaticamente ogni anno (in una data configurata a sistema e non modificabile dall’utente tramite GUI ma attraverso una richiesta all’AMS) e determina l’esecuzione massiva di tutte le Policy periodiche, identificate dal campo “Tipologia Policy” = periodica, con frequenza minore o uguale a un anno (infra-annuali). Per le policy periodiche di tipo inter-annuale (frequenza maggiore di un anno) la generazione avviene alla chiusura dell’ordine precedente (generato dalla stessa policy).

Una volta innescato il processo, il sistema elabora le Policy periodiche alle quali possono essere associati i seguenti Input:

Input variabili inseriti/calcolati in EAM:

Tasso di guasto;

Tasso di Anomalia;

Attributi anagrafici: specifiche tecniche e tutti i campi di testata;

Variabili di stato;

Attività.

Target di raffronto definiti in EAM.

Figura 71 - Modello logico DSS periodico

Il sistema sarà inizializzato importando gli OdL periodici dei successivi anni, già emessi in Make. Successivamente al Go-Live, il processo di generazione verrà eseguito automaticamente nella data configurata a sistema, producendo gli OdL relativi all’anno N+5 e l’eventuale aggiornamento di OdL già emessi. In questo modo, il sistema garantisce sempre la presenza di un battente di OdL periodici per i 5 anni successivi.

Logiche di funzionamento DSS

In questo capitolo vengono descritte le logiche di funzionamento del DSS. In particolare, viene fornito il dettaglio di ogni use case individuato per il DSS e le relative logiche di esecuzione.

#### Use case

##### Generazione attività on condition

Le policy «on condition» determinano la generazione di OdL on condition, i quali vengono generati in stato “da validare”. Il DSS prima di generare un nuovo ordine verifica che a sistema non esista già un ordine che soddisfa la tripletta Policy, PdL, Asset altrimenti non lo genera.

Di seguito si riportano i campi calcolati dal DSS al momento della generazione dell’OdL on condition:

Data modello DSS (logiche di valorizzazione descritte successivamente nel paragrafo);

Data pianificata fine: stesso valore della Data modello;

Data pianificata inizio: Valorizzata inizialmente dal DSS alla generazione come Data fine pianificata - Durata pianificata in giorni (pari alla durata in ore del PdL diviso per le ore lavorative di una giornata pari a 8 e 36 minuti compresa di pausa pranzo);

Data di max posticipo: Data modello + Massimo posticipo attività;

Data non prima di: Data modello – Massimo anticipo attività. Se il “massimo anticipo” sulla policy che ha generato l’ordine è uguale a 0, il sistema imposta come “data non prima di” la data di generazione dell’ordine;

Per quanto riguarda la data modello sono presenti diverse logiche di valorizzazione del campo basate sull’eleggibilità o meno dell’asset:

Policy non di eleggibilità (non ci sono HI in input alla policy):

Se in input alla policy sono presenti più difformità / interferenze, la data modello è calcolata aggiungendo la differibilità alla data di prima rilevazione della difformità / interferenza più recente (con lo stesso codice di quella indicata come input alla policy);

Se in input alla policy è presente solo una difformità/interferenza, la data modello è calcolata aggiungendo la differibilità alla data di prima rilevazione della difformità;

Se non ci sono difformità / interferenze in input alla Policy, la data modello è calcolata sommando la differibilità alla data di generazione dell’OdL.

Policy di eleggibilità (c’è almeno un HI input alla policy):

Se su un asset scatta per la prima volta una policy di eleggibilità, la data modello è calcolata come somma tra la differibilità della policy e la data di generazione dell’OdL;

Se scatta su uno stesso asset una nuova policy con lo stesso HI in input ma differibilità differente, la data modello dell’ordine viene aggiornata solo se la data calcolata sulla base della nuova differibilità è minore della data modello già presente sull’ordine (in pratica, sull’ordine viene inserita la data modello più prossima).

Si specifica che sull’OdL verrà riportata la prima data di eleggibilità dell’asset.

##### Generazione attività periodiche

Le Policy «periodiche» determinano la generazione di OdL (controlli) periodici. Gli OdL periodici saranno generati in stato “aperto”.

Di seguito si riportano i campi calcolati dal DSS al momento della generazione degli OdL periodici:

Data modello DSS (logiche di valorizzazione descritte successivamente nel paragrafo);

Data pianificata fine: stesso valore della Data modello;

Data pianificata inizio: Valorizzata inizialmente dal DSS alla generazione come Data fine pianificata - Durata pianificata in giorni (pari alla durata in ore del PdL diviso per le ore lavorative di una giornata pari a 8 e 36 minuti compresa di pausa pranzo);

Data di max posticipo: Data modello + massimo posticipo attività;

Data non prima di: per gli inter-annuali = Data modello – Massimo anticipo attività, per gli infra-annuali è definita sulla base delle logiche descritte nel capitolo 2.2.11.1;

Gli OdL periodici possono essere di tipo inter-annuale (con frequenza maggiore di un anno) e intra-annuali (con frequenza minore o uguale a un anno).

La data modello è calcolata come di seguito riportato:

OdL periodici inter – annuali: la data modello è calcolata come “data di chiusura dell’OdL precedente + frequenza” (generati alla chiusura del controllo precedente);

OdL periodici infra – annuali: per questa tipologia di OdL è previsto il run del DSS a inizio anno, a seguito del quale vengono emessi gli ordini fino all’anno n+5. In questo caso la data modello è calcolata come “primo giorno dell’anno di competenza + frequenza”. Di conseguenza la data modello dell’OdL coincide con l’ultimo giorno del periodo definito dalla frequenza della policy, come di seguito riportato:

Frequenza uguale a 1 mese: il sistema genererà 12 controlli con data modello 31 gennaio, 28 Febbraio, 31 Marzo, 30 Aprile, 31 Maggio, 30 Giugno, 31 Luglio, 31 Agosto, 30 Settembre, 31 Ottobre, 30 Novembre, 31 Dicembre

Frequenza uguale a 2 mesi: il sistema genererà 6 controlli con data modello 28 Febbraio, 30 Aprile, 30 Giugno, 31 Agosto, 31 Ottobre, 31 Dicembre;

Frequenza uguale a 3 mesi: il sistema genererà 4 controlli con data modello 31 Marzo, 30 Giugno, 30 Settembre, 31 Dicembre;

Frequenza uguale a 4 mesi: il sistema genererà 3 controlli con data modello 30 Aprile, 31 Agosto, 31 Dicembre;

Frequenza uguale a 6 mesi: il sistema genererà 2 controlli con data modello 30 Giugno, 31 Dicembre;

Frequenza uguale a 1 anno: il sistema genererà 1 controllo con data modello 31 Dicembre

Infine, per i controlli periodici generati quando un nuovo asset entra in esercizio la data modello è calcolata come “data entrata in esercizio dell’asset + frequenza”

Logica di generazione micro-controlli

Alcuni PdL di tipo controllo sono associati a catalogo ad una lista di micro-controlli come descritto nel capitolo relativo al catalogo attività (paragrafo 2.2.7). Per ordini associati a PdL che hanno il riferimento ai micro-controlli, il DSS in fase di generazione deve determinare la specifica lista sulla base delle indicazioni riportate nel catalogo dei micro-controlli come descritto nel capitolo 2.2.5.1. In particolare, sulla base di attributi degli asset e relativi valori indicati sul catalogo micro-controlli, è possibile determinare quali degli n micro-controlli associati al PdL vanno riportati sull’Ordine relativo allo specifico asset sul quale il DSS sta agendo. Non è previsto che la lista dei micro-controlli sia aggiornata automaticamente in fase di schedulazione. Se necessario eliminare o aggiungere micro-controlli alla lista sull’ordine (solo se in stato “Aperto” o “da validare”), è possibile farlo attraverso delle apposite funzionalità presenti a maschera. I micro-controlli aggiunti possono essere scelti esclusivamente dalla lista dei micro-controlli associati al PdL a catalogo.

##### Cambio stato OdL per prevalenza

La prevalenza è gestita nel catalogo dei PdL. Ad ogni PdL possono essere associati uno o più PdL prevalenti.

Di seguito si riportano le logiche relative alla prevalenza:

Se “data modello OdL prevalente” <= “data modello OdL prevalso” allora l’OdL prevalso viene portato in stato “Non eseguito”. Nel momento in cui si chiude l’OdL prevalente verranno risolte sia le difformità presenti nell’OdL prevalente sia quelle presenti nell’OdL prevalso. Sull’OdL prevalso verranno popolati automaticamente:

Una nota “OdL prevalso” per dare evidenza all’utente del motivo per il quale l’OdL si trova in stato non eseguito

Un campo all’interno del quale viene riportato l’OdL prevalente

Se “data modello OdL prevalente” > “data modello OdL prevalso” allora il sistema NON porta l’OdL prevalso in stato “Non eseguito”. In questo caso l’utente potrebbe decidere di non eseguire l’OdL prevalso più stringente (che andrà in “non eseguito” quando si supera la “data di massimo posticipo dell’OdL”) ed eseguire in futuro l’OdL prevalente. Nel momento in cui viene chiuso l’OdL prevalente, saranno risolte anche le difformità di quello prevalso

Se sono presenti più OdL on condition o manuali (con stesso PdL e che puntano allo stesso asset) con date modello differenti, prevale l’OdL con data modello più stringente. Di conseguenza gli OdL prevalsi verranno portati in stato “Non eseguito”. Nel momento in cui si chiude l’OdL prevalente verranno risolte sia le difformità presenti nell’OdL prevalente sia quelle presenti negli OdL prevalsi. Sugli OdL prevalsi verranno popolati automaticamente:

Una nota “OdL prevalso” per dare evidenza all’utente del motivo per il quale l’OdL si trova in stato non eseguito

Un campo all’interno del quale viene riportato l’OdL prevalente

Si specifica che:

Le logiche sopra descritte sono valide sia in caso di generazione di OdL da DSS sia in caso di generazione di OdL eseguita manualmente;

È possibile associare attività prevalenti esclusivamente sullo stesso livello delle attività prevalse;

Non è possibile associare attività prevalenti su PdL di tipologia “Controllo periodico”;

Se un OdL si trova già in fase di schedulazione non potrà mai essere prevalso da un altro OdL. Di conseguenza le logiche di prevalenza sopra descritte sono valide solo se gli OdL prevalsi si trovano in stato “Aperto”;

Nel caso in cui vengano innescate contemporaneamente n policy (con stesso PdL in output e riferite allo stesso asset) con stessa differibilità (e quindi ugualmente stringenti) verrà generato solo uno degli n OdL, scelto randomicamente tra quelli proposti.

Per attività su linee, alla creazione da parte del DSS di ordini su asset di quarto livello, viene valutata la prevalenza (definita a catalogo) rispetto ad ordini già emessi su asset di terzo livello gerarchicamente collegati. Allo stesso modo alla creazione da parte del DSS di ordini sul terzo livello viene valutata la prevalenza rispetto ad ordini già emessi su asset di quarto livello gerarchicamente collegati. Non è prevista la gestione della prevalenza per altre relazioni gerarchiche o livelli gerarchici. La logica sopra descritta è applicata esclusivamente dal DSS e non all’apertura di ordini manuali.

Per attività su stazioni, alla creazione da parte del DSS di ordini su asset di quarto livello, viene valutata la prevalenza (definita a catalogo) rispetto ad ordini già emessi su asset di quarto livello che hanno in comune l’asset padre (escludendo quelli di stessa tipologia elemento). Non è prevista la gestione della prevalenza per altre relazioni gerarchiche o livelli gerarchici. La logica sopra descritta è applicata esclusivamente dal DSS e non all’apertura di ordini manuali.

##### Generazione attività trainate

La generazione di un’attività di sostituzione di una campata di conduttore / fune di guardia (attività trainante) determina necessariamente la generazione di attività di sostituzione sull’intera tratta di tesatura (attività trainate). L’attributo trainante-trainato sarà gestito sull’OdL come segue:

Sull’OdL generato da Policy sarà presente l’attributo «trainante»

Sugli OdL generati perché facenti parte della stessa tratta di tesatura, sarà presente l’attributo «trainato».

In questo modo sarà possibile distinguere l’OdL trainante da quelli trainati.

Per Policy di sostituzione che insistono sui conduttori / funi di guardia, alla generazione:

Si verificherà la presenza di altri conduttori / funi di guardia sulla stessa tratta di tesatura;

Si genererà l’OdL (con attributo «trainante») per il conduttore / fune di guardia che necessita la sostituzione;

Si genereranno gli altri OdL (con attributo «trainato») per tutti i conduttori / funi di guardia presenti sulla stessa tratta di tesatura del conduttore / fune di guardia per il quale erano verificati gli input alla Policy

Se le condizioni della policy sono verificate su più di un conduttori / funi di guardia della stessa tratta di tesatura, l’attributo “trainante” viene valorizzato solo per il primo degli asset per cui il DSS valuta la policy, tutti gli altri avranno l’attributo “trainato”.

##### Generazione OdL tronco di ispezione

Essendo il tronco di ispezione un insieme di campate / tratte cavo, l’OdL relativo al tronco verrà gestito come un OdL aggregatore all’interno del quale saranno presenti tutti gli OdL aggregati relativi alle campate / tratte cavo sottese al tronco. Le policy su cui il DSS applica la logica del tronco sono definite su asset di tipo campata aerea / tratta cavo e hanno il flag “Tronco d’ispezione?” attivo. Solo per questo tipo di policy, il DSS oltre alla tripletta Policy, PdL, Asset valuta anche il tronco a cui fa riferimento l’OdL per valutare se generare o meno un nuovo ordine.

Di seguito si riportano le logiche di creazione e gestione dell’OdL aggregatore e relativi OdL aggregati:

Le Policy definite sulle campate / tratte cavo determinano la generazione di n OdL che vengono aggregati automaticamente in un OdL aggregatore “tronco di ispezione”. Si specifica che il criterio di aggregazione è basato sull’appartenenza degli asset allo stesso tronco di ispezione. Quest’ultimo non è definito come asset in Maximo (non possiede quindi un ASSETNUM). Di conseguenza gli OdL aggregatori, generati dal DSS sul tronco di ispezione, avranno i seguenti campi di testata:

“ASSETNUM” della sede tecnica di primo livello;

“SAP ID” del tronco di ispezione.

Responsabile asset:

In fase di creazione dell’ordine aggregatore vengono associati tutti i figli riferiti al tronco anche se questi sono associati ad UI responsabili asset diverse. Il responsabile asset assegnato al padre è scelto in modo casuale fra i responsabili dei figli se il padre è su linea multi-competenza. Non è possibile modificare il responsabile asset, fintantoché sono associati ordini figli.

In fase di creazione dell’ordine singolo linea multi-competenza, viene assegnato il responsabile asset prevalente in anagrafica. Il campo è successivamente modificabile dall’utente (solo in stato aperto e da validare) con uno dei valori presenti nel riepilogo dati dell’asset ma non può essere sbiancato, deve sempre contenere un valore.

Per tutti gli altri casi il responsabile asset è quello associato come UI responsabile dell’asset.

Gli OdL aggregati hanno le stesse date di pianificazione dell’OdL aggregatore;

Il dato fisico dell’OdL aggregatore avrà valore pari alla somma delle campate / tratte cavo sottese al tronco per le quali è stato eseguito il relativo OdL;

Al momento della creazione dell’OdL aggregatore il sistema recupererà la relazione tra le campate/tratte cavo e i relativi sostegni / estremi cavo (che non saranno visibili sull’ordine). In questo modo all’interno degli OdL aggregati (che hanno come asset di riferimento la campata/tratta cavo) sarà possibile tracciare le anomalie/difformità relative ai sostegni / estremi cavo (se associate da catalogo sul PdL associato all’ordine).

##### Modifiche ai cataloghi

La modifica manuale dei cataloghi (in particolare, la disattivazione/eliminazione di un record dal catalogo), il cui contenuto risulta input/output delle Policy, genera il cambio stato automatico delle Policy da “attiva” in “Pending Review”. L’utente nel centro di controllo avrà visione degli OdL impattati dalla modifica dei cataloghi la cui Policy generante risulta in “Pending Review”.

Di seguito si riporta quanto accade una volta riattivata la policy dall’utente, il quale porta lo stato da “Pending Review” a “Attiva”:

Generazione OdL: il DSS esegue il run e, se necessario, genera OdL nuovi applicando le regole definite nelle policy modificate;

Cambio stato OdL: il DSS può cambiare lo stato di OdL esistenti solamente in caso di generazione di attività prevalenti che determinano il cambio stato degli OdL prevalsi (portati in stato “Non eseguito”). Si precisa che il cambio stato potrà avvenire solamente per OdL che si trovano in stato “Aperto”.

##### Risoluzione difformità / interferenze

Al momento del passaggio in “Eseguito” di un OdL, il sistema determina la risoluzione di difformità / interferenze, il sistema valuterà tutte le difformità / interferenze (risolvibili tramite il PdL associato all’OdL o tramite OdL prevalsi da quello che sta portando in stato “Eseguito”) aperte sull’asset di riferimento dell’OdL e le porterà automaticamente in stato «risolta». Per ordini su asset di quarto livello stazione, vengono portate in stato risolto anche tutte le difformità/interferenze che hanno lo stesso padre dell’asset associato all’ordine. Per ordini su asset terzo livello linee, vengono risolte anche le difformità associate ad asset su quarto livello gerarchicamente sottostanti l’asset oggetto dell’ordine. Per visualizzare tutte le difformità / interferenze risolte con un OdL, è necessario accedere all’applicazione “Difformità rilevate” e filtrare la lista delle difformità / interferenze rappresentata in Figura 72 con il codice dell’OdL di interesse nella colonna «OdL risolutore».

Figura 72 - Lista difformità / interferenze

##### Gestione zonizzazione

La logica di generazione di attività su base zonizzazione si basa sulla lettura da parte del DSS, di attributi di zonizzazione definiti sugli asset e inseriti come input alle policy. Gli attributi di zonizzazione e la loro valorizzazione sono quindi gestiti sull’anagrafica degli asset. Per gestire nuove policy di zonizzazione è necessario definire preventivamente gli attributi di zonizzazione sugli asset (target della policy), propedeutici per la creazione della policy.

Per definire nuove policy di zonizzazione non attualmente descritte nei cataloghi, è necessario che siano totalmente conformi alle logiche di gestione e modellazione di seguito descritte..

Linee aeree

Per quanto riguarda le linee aeree i controlli generati sulla base della zonizzazione sono di due tipi:

Ispezioni a vista da terra (SA)

Ispezioni a vista da terra (SB)

Questi due controlli sono solitamente effettuati ad anni alterni. Per gestire la generazione degli ordini relativi a questi controlli sono presenti due policy per ciascuno dei controlli. Le policy avranno in input rispettivamente due attributi:

LIN_ZONA_ANNO_SA – attributo di zonizzazione per i controlli SA che può assumere i valori PARI o DISPARI;

LIN_ZONA_ANNO_SB – attributo di zonizzazione per i controlli SB che può assumere i valori PARI o DISPARI.

Dovranno quindi essere definite quattro policy come di seguito rappresentate:

Per queste policy deve essere indicato come riferimento temporale “Anno pari” o “Anno dispari” per permettere al sistema di determinare la generazione degli ordini con frequenza biennale.

Se viene modificato l’attributo di zonizzazione, il DSS ricalcola le policy solo quando tutti gli attributi relativi alle campate aeree di un tronco di ispezione assumono lo stesso valore.

Linee in cavo

Per quanto riguarda le linee in cavo, la zonizzazione è gestita tramite un attributo in anagrafica denominato LIN_ZONA_AMBIENTALE che può assumere diversi valori alfanumerici. Per ognuno dei valori alfanumerici che può assumere l’attributo è definita una policy che riporta una determinata frequenza di generazione degli ordini.

La policy ha le seguenti caratteristiche:

In input ha l’attributo LIN_ZONA_AMBIENTALE;

Come target di raffronto, il valore di zonizzazione di interesse;

Come frequenza di generazione, il valore desiderato per la zonizzazione definita come target di raffronto.

In alcuni casi, per queste attività, è necessario modificare la frequenza di zonizzazione secondo due criteri:

modificando gli ordini già creati dal DSS per l’anno in corso sulla base della nuova frequenza;

generare ordini secondo la nuova frequenza a partire da una data specifica.

Per gestire la prima fattispecie basta modificare l’attributo di zonizzazione sull’asset per il quale deve essere modificata la frequenza, con quello che corrisponde alla frequenza desiderata nelle policy definite a catalogo. In caso di policy su tronco di ispezione, il DSS elabora la policy solo se tutti gli attributi di zonizzazione delle tratte cavo associate allo stesso tronco assumono lo stesso valore. In questo modo, scatta la nuova policy e determina l’elaborazione di ordini sulla base della nuova frequenza.

Per gestire la seconda fattispecie è stata definita la seguente logica:

Viene definito un nuovo valore di zonizzazione per l’attributo LIN_ZONA_AMBIENTALE sull’asset per il quale deve essere modificata la frequenza di generazione ordine;

Viene duplicata la policy che riporta la nuova frequenza desiderata;

Si modifica nella policy duplicata il valore del target di raffronto con quello definito nell’attributo LIN_ZONA_AMBIENTALE dell’asset per il quel deve essere applicata la nuova frequenza;

Si inserisce nel campo “A partire da” la data a partire dalla quale si vuole iniziare a far generare al DSS gli ordini secondo la nuova frequenza.

##### Stati asset

Prima di generare l’OdL definito dalla Policy, il sistema controllerà in che stato si trova l’asset oggetto di OdL. Quest’ultimo sarà generato solo se lo stato in cui si trova l’asset è idoneo allo svolgimento dell’attività. Per gestire quanto appena descritto a sistema sono presenti diverse tabelle accessibili tramite le azioni nel menu a sinistra nell’applicazione “Catalogo policy”, di seguito dettagliate:

Tabella di corrispondenza Asset – Stato idoneo: All’interno di questa tabella per ogni classificazione asset presente a sistema viene riportato lo stato che si reputa idoneo affinché possano essere generati OdL dal DSS sull’asset.

La tabella è composta dai seguenti campi:

Classificazione asset: campo valorizzato manualmente dall’utente tramite una lista valori che riporta tutte le classificazioni asset presenti a sistema

Stato idoneo: campo valorizzato manualmente dall’utente tramite una lista valori contenente gli stati ammissibili per la classificazione precedentemente selezionata

Tabella eccezioni: all’interno di questa tabella sono presenti le attività che per essere generate necessitano di un particolare stato dell’asset. Dunque, solo per questo tipo di attività, il sistema non leggerà la “Tabella di corrispondenza Asset – stato idoneo”, bensì la “Tabella eccezioni”.

La tabella è composta dai seguenti campi:

PdL: campo valorizzato manualmente dall’utente tramite una lista valori contenente i PdL presenti a sistema

Classificazione Asset: campo valorizzato manualmente tramite una lista valori contenente le classificazioni associate al PdL

Stato idoneo: campo valorizzato manualmente dall’utente tramite una lista valori contenente gli stati ammissibili per la classificazione precedentemente selezionata.

Si specifica che l’operatività sulle tabelle sopra descritte è garantita solamente agli utenti centrali, i quali potranno aggiungere/modificare/eliminare record.

In allegato si riporta il dettaglio delle tabelle sopracitate.

###### Policy su asset di 3° livello

Nel caso in cui la policy punti a un terzo livello (asset per il quale non è definito uno stato), per la generazione delle attività il sistema effettua un check sullo stato di anagrafica dei quarti livelli sottesi. Se almeno uno di questi si trova in uno stato idoneo (informazione ricavata dalla “tabella di corrispondenza Asset – Stato idoneo”), allora il sistema genera OdL sugli elementi di terzo livello. L’idoneità/non idoneità dei terzi livelli per la generazione di OdL sugli stessi verrà gestita come di seguito descritto:

Linee: sia per le linee aeree che per quelle in cavo sarà presente un attributo “LIN_IDONEITA”, per ogni terzo livello riportato nelle tabelle sottostanti, che potrà assumere valore “IDONEO” o “NON IDONEO” secondo regole descritte successivamente nel corso del paragrafo. Il DSS genererà OdL sul terzo livello solo se l’attributo risulta valorizzato con “IDONEO”.

Stazioni: saranno presenti gli attributi “LIN_IDONEITA_UF” e “LIN_IDONEITA_SPCC”, per ogni terzo livello riportato nelle tabelle sottostanti, che potranno assumere i valori “IDONEO” o “NON IDONEO” secondo regole descritte successivamente nel corso del paragrafo. Il DSS genererà OdL sul terzo livello solo se l’attributo risulta valorizzato con “IDONEO”.

Le regole per la generazione di OdL su asset di terzo livello saranno gestite all’interno dell’applicazione “Gestione stati - attività” nel tab “Regole terzi livelli”. Di seguito il dettaglio delle tabelle:

Regole terzi livelli linee: all’interno di questa tabella sono presenti le seguenti colonne:

Terzo livello: campo popolabile manualmente dall’utente tramite una lista valori contenente le classificazioni di terzo livello linee aeree e in cavo

Quarto livello: campo popolabile manualmente dall’utente tramite una lista valori contenente le classificazioni di quarto livello sottostanti al terzo livello precedentemente selezionato

| Terzo livello | Quarto livello |
| --- | --- |
| CONDUTTORI | CONDUTTORE |
| TELAIO | STRUTTURA |
| ATTACCO CONDUTTORE | EQUIPAGGIAMENTI CONDUTTORE |
| TERMINALI | TERMINALE |

La logica di lettura della tabella è la seguente: se almeno un quarto livello si trova in uno stato idoneo (ricavato dalla “tabella di corrispondenza Asset – Stato idoneo”) allora l’attributo “LIN_IDONEITA” assume il valore “IDONEO”. In caso contrario assumerà un valore “NON IDONEO”.

Esempio sul CONDUTTORI di terzo livello: se esiste almeno un CONDUTTORE di 4° livello in uno stato idoneo, allora l’attributo “LIN_IDONEITA” del CONDUTTORI (3° livello) viene valorizzato con “IDONEO”. A questo punto il DSS potrà generare l’attività riportata nella policy.

Si specifica che in questo caso tra gli input della policy, che punta a uno specifico 3° livello, sarà necessario inserire l’input “LIN_IDONEITA” con target “IDONEO”

Regole terzi livelli stazioni: sono presenti due tabelle (una per gestire la casistica SPCC e l’altra per la casistica UF) con le seguenti colonne:

Terzo livello: campo popolabile manualmente dall’utente tramite una lista valori contenente le classificazioni di terzo livello stazioni

Quarto livello: campo popolabile manualmente dall’utente tramite una lista valori contenente le classificazioni di quarto livello sottostanti al terzo livello precedentemente selezionato

| SPCC | SPCC |
| --- | --- |
| Terzo Livello | Quarto Livello |
| STALLO | PROTEZIONE DI MASSIMA CORRENTE |
| STALLO | DISPOSITIVO DI TELEPROTEZIONE |
| STALLO | PROTEZIONE DI MINIMA/MASSIMA TENSIONE |
| STALLO | PROTEZIONE DIFFER. DI SBARRA/M.A.I. (UP) |
| STALLO | PROTEZIONE DIFFERENZIALE DI CAVO/LINEA |
| STALLO | PROTEZIONE DIGITALE MULTIFUNZIONE |
| STALLO | PROTEZIONE DISTANZIOMETRICA |
| STALLO | SPCC DI MONRICHIUSURA RAPIDA ESTERNATANTE x |
| SBARRA | PROTEZIONE DI MINIMA/MASSIMA TENSIONE |
| SBARRA | PROTEZIONE DIFFER. DI SBARRA/M.A.I. (UC) |
| SBARRA | PROTEZIONE DIGITALE MULTIFUNZIONE |
| TRASFORMATORE | PROTEZIONE DI MASSIMA CORRENTE V.S.C. |
| TRASFORMATORE | PROTEZIONE DI MINIMA/MASSIMA TENSIONE |
| TRASFORMATORE | PROTEZIONE DIFFERENZIALE DI MACCHINA |
| TRASFORMATORE | PROTEZIONE DIGITALE MULTIFUNZIONE |
| TRASFORMATORE | PROTEZIONE DISTANZIOMETRICA |
| BATTERIA CONDENSATORI | PROTEZIONE DI MASSIMA CORRENTE |
| BATTERIA CONDENSATORI | PROTEZIONE DIGITALE MULTIFUNZIONE |
| BATTERIA CONDENSATORI | PROTEZIONE DISTANZIOMETRICA |
| REATTORE | PROTEZIONE DI MASSIMA CORRENTE V.S.C. |
| REATTORE | PROTEZIONE DIGITALE MULTIFUNZIONE |
| SISTEMA DI CONTROLLO E AUTOMAZIONE | SPCC DI IMPIANTO |

| UF | UF |
| --- | --- |
| Terzo livello | Quarto livello |
| STALLO | INTERRUTTORE AIS x |
| STALLO | SEZIONATORE AIS x |
| STALLO | TA AIS x |
| STALLO | TV AIS x |
| STALLO | SCARICATORE AIS x |
| STALLO | CALATE E COLLEGAMENTI |
| STALLO | PASSANTE AIS x |
| STALLO | INTERRUTTORE GIS x |
| STALLO | SEZIONATORE GIS x |
| STALLO | TA GIS x |
| STALLO | TV GIS x |
| STALLO | SCARICATORE GIS x |
| STALLO | PASSANTE GIS x |
| STALLO | RESISTORE x |
| STALLO | INDUTTORE x |
| STALLO | CAPACITORE x |
| SBARRA | SBARRA AIS x |
| SBARRA | SBARRA GIS x |
| SBARRA | SEZIONATORE AIS x |
| SBARRA | TV AIS x |
| SBARRA | SCARICATORE AIS x |
| SBARRA | SEZIONATORE GIS x |
| SBARRA | TV GIS x |
| SBARRA | SCARICATORE GIS x |
| TRASFORMATORE | MACCHINA x |
| TRASFORMATORE | VARIATORE x |
| TRASFORMATORE | SISTEMA DI RAFFREDDAMENTO x |
| TRASFORMATORE | ISOLATORE PASSANTE x |
| BOOSTER | MACCHINA x |
| BOOSTER | VARIATORE x |
| BOOSTER | SISTEMA DI RAFFREDDAMENTO x |
| BOOSTER | ISOLATORE PASSANTE x |
| BATTERIA CONDENSATORI | CONDENSATORE |
| BATTERIA CONDENSATORI | TA AIS x |
| REATTORE | MACCHINA x |
| REATTORE | VARIATORE x |
| REATTORE | SISTEMA DI RAFFREDDAMENTO x |
| REATTORE | ISOLATORE PASSANTE x |

La logica di lettura delle tabelle è la seguente:

Caso SPCC: se almeno un quarto livello si trova in uno stato idoneo (ricavato dalla “tabella di corrispondenza Asset – Stato idoneo”) allora l’attributo “LIN_IDONEITA_SPCC” assume il valore “IDONEO”. In caso contrario assumerà un valore “NON IDONEO”.

Caso UF: se almeno un quarto livello si trova in uno stato idoneo (ricavato dalla “tabella di corrispondenza Asset – Stato idoneo”) allora l’attributo “LIN_IDONEITA_UF” assume il valore “IDONEO”. In caso contrario assumerà un valore “NON IDONEO”.

Per identificare l’attributo da leggere (tra LIN_IDONEITA_UF e LIN_IDONEITA_SPCC) il DSS verifica sul PdL scelto per l’ordine da creare il valore del campo “idoneo UF/SPCC” che indica quale dei due attributi di idoneità vale per quello specifico PdL.

Esempio sullo STALLO (caso SPCC) di terzo livello: se esiste almeno una PROTEZIONE DI MONTANTE o un SPCC MONTANTE in uno stato idoneo, allora l’attributo “LIN_IDONEITA_SPCC” dello STALLO viene valorizzato con “IDONEO”. A questo punto il DSS potrà generare l’attività riportata nella policy.

Esempio sullo STALLO (caso UF) di terzo livello: se esiste almeno un TV AIS o uno SCARICATORE AIS o un CALATE E COLLEGAMENTI (e così via per ogni quarto livello riportato in tabella) in uno stato idoneo, allora l’attributo “LIN_IDONEITA_UF” dello STALLO viene valorizzato con “IDONEO”. A questo punto il DSS potrà generare l’attività riportata nella policy.

Si specifica che:

Quando si crea una policy per “Controlli tecnici di tipo B - SPCC Mont./Protezioni” tra gli input sarà necessario inserire “LIN_IDONEITA_SPCC” con target IDONEO”.

Quando si crea una policy per “Controlli tecnici di tipo B - UF” tra gli input sarà necessario inserire “LIN_IDONEITA_UF” con target “IDONEO”.

L’operatività sulle tabelle sopra descritte è garantita solamente agli utenti centrali, i quali potranno aggiungere/modificare/eliminare record.

## Centro di controllo

In questo capitolo vengono descritte le funzionalità di reportistica del centro di controllo (CdC), utili per monitorare lo stato delle Policy e l’effetto delle azioni che hanno impatto su esse.

OdL relazionati a Policy in stato Pending Review

In questo paragrafo viene presentato il tab “OdL relazionati a Policy in Pending Review” del centro di controllo, tramite il quale l’utente può visualizzare / accedere agli OdL che sarebbero impattati dall’attivazione di una Policy in stato “Pending review”. In particolare, l’attivazione di una Policy in stato “Pending Review” può comportare l’esecuzione automatica di modifiche agli OdL esistenti a essa relazionati oltre che alla generazione di nuovi OdL (cfr. paragrafo 2.2.11.3.2); in quest’ottica, il tab “OdL relazionati a Policy in Pending Review” del CdC costituisce uno strumento di analisi utile all’utente per valutare tali impatti sul battente di attività precedentemente generate dal DSS. Si specifica che il sistema non produce simulazioni relative agli impatti sugli OdL determinati dall’attivazione delle Policy. Di conseguenza, è cura dell’utente verificare che l’attivazione delle Policy produca gli effetti desiderati sugli OdL.

Il tab “OdL relazionati a Policy in Pending Review” (Figura 73) riporta in forma tabellare le seguenti informazioni per ciascun record:

ID OdL: Codice identificativo dell’OdL che sarebbe impattato dall’attivazione della Policy;

ID Policy: Codice univoco della Policy in stato “Pending Review”;

Riferimento Asset: Classificazione Asset alla quale è associato l’OdL;

Responsabile attività: Responsabile dell’OdL che sarebbe impattato;

ID PdL: Codice identificativo del PdL generato dalla Policy in stato “Pending Review” e associata all’OdL;

Data inizio: Data pianificata inizio dell’OdL che sarebbe impattato;

Data fine: Data pianificata fine dell’OdL che sarebbe impattato;

Stato: Stato in cui si trova l’OdL che sarebbe impattato dalla modifica della Policy.

Figura 73 - Sezione del centro di controllo contenente gli OdL relazionati a Policy in Pending Review

Nel caso in cui l’utente sia sicuro di voler apportare modifiche alla Policy, deve modificare manualmente lo stato della stessa all’interno del catalogo delle Policy. Il cambio di stato a “Attiva” comporta la rimozione degli OdL da questa sezione del centro di controllo e la propagazione automatica delle modifiche a tutti gli OdL correlati.

OdL impattati dalla disattivazione di una Policy

In questo capitolo viene presentato il tab “OdL impattati da disattivazione Policy” del centro di controllo (menzionata nel paragrafo 2.2.11.3.3), tramite il quale l’utente può visualizzare/accedere agli OdL relazionati ad una Policy disattivata (posta in stato “Non Attiva”). I record relativi agli OdL relazionati a Policy disattivate sono visibili nel CdC fino a 10 giorni dopo la disattivazione della Policy (nel caso in cui l'utente volesse visualizzare OdL associati a policy disattivate da più di 10 giorni, può farlo andando a modificare "Available queries" nell'applicazione di gestione degli ordini di lavoro).

Il tab “OdL associati a policy disattivate negli ultimi 10 giorni” riporta in forma tabellare le seguenti informazioni per ciascun record (Figura 74).

ID OdL: Codice identificativo dell’OdL impattato;

ID Policy disattivata: Codice univoco della Policy che è stata disattivata;

Riferimento Asset: Classificazione Asset alla quale è associato l’OdL;

Responsabile attività: Responsabile dell’OdL impattato;

ID PdL: Codice identificativo del PdL associato alla Policy disattivata e all’OdL;

Data inizio: Data pianificata inizio dell’OdL impattato;

Data fine: Data pianificata inizio dell’OdL impattato;

Stato: Stato in cui si trova l’OdL impattato dalla modifica della Policy.

Figura 74 - Sezione del centro di controllo contenente gli OdL associati a policy disattivate negli ultimi 10 giorni

OdL impattati a seguito di attivazione di una Policy

In questo paragrafo viene presentato il tab “OdL impattati da attivazione Policy” del centro di controllo (menzionata in sezione 2.2.11.3.2.3) tramite il quale l’utente può visualizzare / accedere agli OdL generati o modificati a seguito dell’attivazione di una Policy in stato “Pending Review”. L’utente può verificare nel tab gli effetti prodotti dall’attivazione di una Policy (passaggio a stato “Attiva”), con la possibilità di accedere alla schermata di dettaglio degli OdL selezionando il relativo record. I record relativi agli OdL relazionati a Policy attivate sono visibili nel CdC fino a 10 giorni dopo l’attivazione della Policy.

Il tab “OdL legati a policy riattivate negli ultimi 10 giorni” riporta in forma tabellare le seguenti informazioni per ciascun record:

ID OdL: Codice identificativo dell’OdL impattato o generato dalla Policy modificata;

ID Policy: Codice univoco della Policy che è stata modificata;

Riferimento Asset: Classificazione Asset alla quale è associato l’OdL impattato o generato dalla Policy modificata;

Responsabile attività: Responsabile dell’OdL impattato o generato dalla Policy modificata;

ID PdL: Codice identificativo del PdL generato dalla Policy modificata e associata all’OdL;

Data pianificata inizio: Data pianificata inizio dell’OdL impattato o generato dalla modifica della Policy modificata;

Data pianificata fine: Data pianificata inizio dell’OdL impattato o generato dalla modifica della Policy modificata;

Stato: Stato in cui si trova l’OdL impattato o generato dalla modifica della Policy modificata.

Figura 75 - Sezione del centro di controllo contenente gli OdL legati a policy riattivate negli ultimi 10 giorni

Come delineato nel capitolo 2.2.11.3.2.3, qualora l’utente rilevi incongruenze in questa sezione del centro di controllo, ha la possibilità di selezionare direttamente il record dell'OdL. Quest’azione reindirizza l'utente all'applicazione di Monitoraggio OdL, consentendo di apportare le modifiche necessarie direttamente sull'OdL.

OdL relazionati ad asset in stati non idonei

In questo paragrafo viene descritto il tab “OdL relazionati ad asset in stati non idonei” del centro di controllo tramite il quale l’utente può visualizzare gli OdL che risultano associati ad asset con stato non più idoneo per quel tipo di attività (cfr. par 3.1.2.1.9).

Il tab “OdL relazionati ad asset in stati non idonei” riporta in forma tabellare le seguenti informazioni per ciascun record:

ID OdL: Codice identificativo dell’OdL;

ID PdL: Codice identificativo del PdL associato all’OdL;

ID Asset: Codice identificativo dell’asset associato all’OdL;

SAP-ID Asset

Descrizione Asset: Classificazione dell’asset associato all’OdL;

Stato Asset: Stato dell’asset associato all’OdL.

Figura 76 - Sezione del centro di controllo contenente gli OdL relazionati ad asset in stati non idonei

Elaborazioni DSS

Il tab nel centro di controllo che monitora sinteticamente le run delle policy da parte del DSS è denominata “Policy e Run DSS”. In questa tabella è riportato un singolo record per ogni run di policy avviato dal DSS, per il quale viene generato un codice univoco. Le colonne riportate nella tabella sono:

ID run DSS: è il codice univoco staccato dal DSS quando viene avviata la run di una policy;

ID policy: è il codice da catalogo della policy;

Data di primo run DSS: riporta la data e l’ora di avvio della prima elaborazione relativa alla run in oggetto.

Dalla tabella sopra descritta, tramite dei link sui singoli record, è possibile accedere all’applicazione “Policy e Run DSS” dove sono riportate le informazioni di dettaglio della singola run. In particolare, per ogni run è possibile visualizzare le singole elaborazioni della policy sugli asset su cui è stata valutata. L’applicazione è accessibile anche da menù laterale. Di seguito si riporta il dettaglio dell’applicazione

#### Applicazione di monitoraggio elaborazioni DSS

Nella vista elenco dell’applicazione è riportata la stessa tabella di sintesi presente nel centro di controllo. Selezionando il singolo record si accede alla vista di dettaglio.

Nella pagina di dettaglio sono presenti due tabelle:

“Elaborazioni DSS avviate e non portare a termine”;

“Elaborazioni DSS portate a termine”.

Nella tabella “Elaborazioni DSS avviate e non portare a termine” l’utente può visualizzare le elaborazioni effettuate dal DSS che sono andate in errore.

In particolare, riporta in forma tabellare le seguenti informazioni per ciascun record:

ID Policy: Codice identificativo della policy;

ID Macro-attività output alla policy: Codice identificativo della macro-attività in output alla policy;

Descrizione macro-attività: riporta la descrizione della macro-attività output alla policy;

Codice dell’Asset: Codice identificativo dell’asset per il quale non è stato generato l’OdL;

Descrizione Asset: Classificazione dell’asset per il quale non è stato generato l’OdL;

Data elaborazione della policy: Data elaborazione della policy sull’asset;

Codice errore: codice alfanumerico che identifica univocamente l’errore;

Descrizione errore: descrizione parlante dell’errore

Figura 77 – Tabella elaborazioni DSS avviate e non portate a termine

Tramite il tasto “Riprocessa Run DSS” l’utente può avviare una nuova elaborazione per tutti gli asset per i quali non è stata portata a termine correttamente. Non è prevista una ri-elaborazione parziale degli asset andati in errore associati al run ID della policy.

Nella tabella “Elaborazioni DSS portate a termine” l’utente può visualizzare le elaborazioni della run che non hanno prodotto errori e sono state correttamente portate a termine.

In particolare, riporta in forma tabellare le seguenti informazioni per ciascun record:

ID Policy: Codice identificativo della policy;

ID ordine output alla policy: Codice identificativo dell’OdL generato dal DSS. Non sempre popolato, dipende dall’outcome dell’elaborazione. Ad esempio, se l’asset non è in stato idoneo, l’ordine non viene generato;

ID PdL associato all’ordine output alla policy: Codice identificativo del PdL associato all’OdL generato dalla policy. Presente solo se è stato generato un ordine;

Codice dell’Asset: Codice identificativo dell’asset associato all’OdL generato;

Descrizione Asset: Classificazione dell’asset;

Data elaborazione della policy: Data elaborazione della policy sull’asset.

Figura 78 – Tabella elaborazioni DSS portate a termine

## Generazione input tramite eventi fuori processo

Come già accennato nel capitolo 3.1, alcuni degli input alle policy possono derivare da eventi esterni come:

L’integrazione con sistemi esterni;

Il caricamento di dati tramite file Excel.

Nel seguito del capitolo sono definite nel dettaglio tutte le casistiche relative alle integrazioni con altri sistemi e al caricamento di file Excel relativi a report di laboratorio.

Integrazione con Elive

In questo paragrafo viene descritta l’interfaccia con Elive tramite la quale vengono acquisite in modo automatico le misurazioni derivanti dalle ispezioni Lidar. Utilizzando specifiche regole di BSN, le misurazioni (distanze tra le piante e gli asset) acquisite tramite l’interfaccia, sono utilizzate dal sistema come Input per generare delle Interferenze arboree associate agli OdL che hanno permesso la rilevazione delle relative misure.

Le misurazioni Lidar acquisite dall’interfaccia EAM - Elive sono contenute nei seguenti 4 possibili report di Elive:

Catenaria verticale;

Catenaria sbandata;

Ribaltamento;

I report sopra elencati sono suddivisi in due tipologie: i primi tre fanno parte della tipologia “Report tagli”,

Nel dettaglio, il processo di acquisizione delle misure Elive e generazione delle relative interferenze si compone di diversi step, rappresentati In Figura 79.

Figura 79 - Processo di integrazione con Elive

Di seguito, vengono descritti gli step del processo di esecuzione di OdL di ispezione Lidar che richiedono l’uso dell’interfaccia con Elive:

Generazione in EAM degli OdL di ispezione Lidar (identificati tramite un attributo presente nel catalogo PdL “Elive”).

Invio degli OdL di ispezione Lidar a Elive per consentire l’associazione delle misure al relativo OdL (per gli OdL inviati a Elive deve essere aggiunto ai dati il campo booleano non modificabile “Acquisizione misure Elive”, usato dal sistema per registrare l’acquisizione delle misure da Elive).

Esecuzione in EAM degli OdL di ispezione Lidar. L’OdL rimarrà in stato “Eseguito” in EAM fino alla registrazione delle relative Interferenze. La ricezione del file da parte di Elive determina la valorizzazione automatica del flag “Acquisizione misure Elive” per il set di OdL contenuto nel file (a prescindere dal fatto che abbiamo o meno misure associate).

Parallelamente all’esecuzione degli OdL in EAM, vengono generati i report misurazioni in Elive.

I report misurazioni vengono validati in Elive e contestualmente inviati ad EAM. Il sistema calcola le interferenze unicamente per i report di tipo Catenaria verticale e Catenaria verticale 2.0.

Nel report sono riportate le distanze in metri tra le piante e le campate (con il riferimento all’asset specifico). Ogni misura corrisponde ad un’eventuale collisione. Il numero delle collisioni che hanno determinato la generazione di una interferenza (per i report non in griglia) viene riportato sull’interferenza stessa. Il numero di collisioni viene calcolato solamente per report di tipologia “Catenaria Verticale”. Per , la distanza minima riportata in metri tra quelle delle collisioni (presenti nel report Elive) che costituiscono l’interferenza deve anche essere riportata come informazione nell’interferenza generata, nel campo denominato “Distanza minima”, campo non modificabile e arrotondato alla seconda cifra decimale.

A sistema sono definiti dei valori di riferimento (target) per determinare la gravità delle interferenze registrate a sistema, sulla base delle distanze ricevute in input dal report.

|  | Livello di tensione | Livello di tensione | Livello di tensione | Livello di tensione |
| --- | --- | --- | --- | --- |
| Gravità | 380 kV | 220 kV | 150-132 kV | ≤ 70 kV |
| URGENTE | 5 m | 3,5 m | 2,5 m | 1,7 m |
| DIFFERIBILE | 7 m | 5 m | 5 m | 5 m |

Tabella 6 – Gravità della misura in base alla distanza rilevata e livello di tensione

Sulla base dei dati ricevuti vengono calcolate interferenze urgenti e differibili sulla singola campata. La tabella che riporta il valore di gravità sulla base della distanza dell’interferenza dall’asset è gestita all’interno del catalogo dei modelli-target.

Una volta calcolate le interferenze (per le misure relative al report “Catenaria verticale”) si effettua un confronto tra la numerosità delle interferenze di ciascun livello di gravità con un valore denominato “Definizione aree boschive”. Questo target definisce il limite sopra il quale le piante sono considerate di alto volume ed è gestito sempre nel catalogo dei modelli-target. Di seguito la logica di generazione delle interferenze a sistema:

Numero interferenze “URGENTE” >= “Definizione aree boschive”: si registra una singola interferenza con valore di gravità “URGENTE”. L’interferenza generata è quella definita a catalogo come “Piante interferenti (volume alto)”;

Numero interferenze “URGENTE” < “Definizione aree boschive”: si registra una singola interferenza con valore di gravità “URGENTE”. L’interferenza generata è quella definita a catalogo come “Piante interferenti (volume basso)”;

Numero interferenze “DIFFERIBILE” >= “Definizione aree boschive”: si registra una singola interferenza con valore di gravità “DIFFERIBILE”. L’interferenza generata è quella definita a catalogo come “Piante interferenti (volume alto)”;

Numero interferenze “DIFFERIBILE” < “Definizione aree boschive”: si registra una singola interferenza con valore di gravità “DIFFRIBILE”. L’interferenza generata è quella definita a catalogo come “Piante interferenti (volume basso)”;

l’utente può procedere alla chiusura dell’OdL.

Le distanze in metri indicate nel report Elive (per le due tipologie di report come descritto sopra) devono anche essere riportate all’interno dell’interferenza generata in un apposito campo denominato “Distanza minima” arrotondato alla seconda cifra decimale, campo non modificabile.

Tutte le logiche di generazione delle interferenze arboree (descritte sopra) sono configurate a sistema e gestite in fase di integrazione con Elive.

Integrazione con cavi interrati

Di seguito si riporta il processo che seguono gli OdL inviati a Cavi Interrati (Figura 80):

Invio automatico a Cavi Interrati degli OdL generati dal DSS. In particolare, verrà inviato l’OdL aggregatore (relativo al tronco) che include i singoli OdL delle tratte cavo afferenti al tronco. Non tutti gli OdL sui cavi verranno inviati a Cavi Interrati, ma solo quelli il cui asset presenta l’attributo “LIN_OUTSOURCING” valorizzato con “SI” e il cui PdL presenta il flag “Outsourcing?” valorizzato a “SI”

Esecuzione OdL su Cavi Interrati;

Registrazione esiti ispezioni e registrazione difformità su Cavi Interrati;

Registrazione manuale su EAM delle difformità precedentemente registrate su Cavi Interrati. Si specifica che la difformità una volta inserita assume lo stato “Da validare” L’OdL assumerà automaticamente lo stato “Eseguito parzialmente”.

Validazione difformità su EAM;

Da questo momento il DSS può leggere le difformità registrate sugli asset.

Considerando che gli OdL eseguiti su cavi interrati sono attività in outsourcing, per la consuntivazione delle ore spese in ufficio dalla risorsa per gestire le attività in outsourcing verrà creato un PdL apposito da associare a un OdL (creato manualmente dall’utente). Quest’ultimo sarà gestito interamente su EAM e quindi non transiterà per WFM nonostante siano presenti risorse UI (l’invio a WFM è inibito quando all’interno dell’OdL è presente il PdL creato appositamente per la consuntivazione sopra descritta).

Figura 80 - Processo OdL inviati a Cavi Interrati

Si specifica che nel caso in cui tutte le tratte cavo, sottostanti ad un ordine padre afferente a uno specifico tronco, vengano demolite (stato DEMO di anagrafica) allora il padre assumerà lo stato NON ESEGUITO (solo se prima si trovava in stato APERTO/DA VALIDARE). In questo e in tutti i casi in cui l’ordine passa in NON ESEGUITO, EAM invierà la cancellazione dell’ordine a CAVI.

Inserimento difformità non associate a OdL

Alcune difformità devono essere associate direttamente sull’asset senza che ci sia un OdL di riferimento che ne permetta la registrazione.

Per caricare questo tipo di difformità bisogna accedere all’applicazione “Difformità rilevate” all’interno della quale è presente una lista di tutte le difformità associate agli asset (Figura 81), sia quelle registrate tramite OdL che quelle caricate direttamente sugli asset. Per caricare le difformità basterà:

Premere il tasto “+” per aggiungere un nuovo record;

Nel campo “Asset” selezionare l’asset di riferimento al quale si vuole associare la difformità;

Nel campo “Difformità” selezionare il codice della difformità;

Nel campo “Gravità” inserire la gravità relativa alla difformità;

Nel campo “Note” selezionare la nota da una lista predefinita.

Figura 81 - Registrazione di difformità senza OdL di riferimento

La difformità una volta inserita si troverà di default in stato “Da validare”. Successivamente l’utente dovrà portare lo stato da “da validare” a “validata” tramite le modalità descritte nel capitolo 12.5. Da questo momento in poi nei nuovi OdL, che hanno come asset di riferimento quello a cui è stata associata la difformità direttamente nella schermata di Figura 81, verrà mostrata tale difformità.

Inserimento misure macchinari e Ariel

L’inserimento di misure derivanti dai report di laboratorio e da report Ariel potrà essere effettuato puntualmente sul singolo asset o massivamente tramite caricamento di un MXLoader all’interno del quale sono presenti le seguenti colonne:

Asset a cui associare la misura;

ID misura catalogo

Valore misura;

SITO (valore costante pari a SIT01).

# Gestione indicatori di condizione tecnica

## Gestione indicatori di condizione

In questo capitolo viene descritto il processo di calcolo degli indicatori di condizione tecnica (Health Index, dei tassi di guasto / anomalia) ai fini della lettura come input da parte del DSS. Il calcolo degli indicatori sarà eseguito nel modulo standard di MAS “Health”.

L’Health è il modulo Maximo che permette di utilizzare formule complesse per il calcolo di indicatori di condizione tecnica associati a degli insiemi di asset. Il calcolo degli indicatori è effettuato massivamente con frequenza bi-giornaliera (ogni 12 ore) per gli HI e con frequenza annuale per i tassi di guasto/anomalia (su tutti gli asset a cui sono associati).

Di seguito si riporta la struttura logica utilizzata nell’Health per la definizione degli indicatori su gruppi di asset (Figura 82):

Figura 82 - Modello logico indicatori di condizione

Calando la logica nel caso specifico dell’HI componenti cavo, si traduce come mostrato nella seguente figura:

Figura 83 - Esempio definizione HI componenti cavo

Seguendo la logica sopra descritta, sono configurati a sistema gli indicatori presenti nel seguente file:

La definizione delle formule all’interno dell’Health può essere eseguita dall’utente, specificando gli attributi e i parametri da utilizzare per il calcolo, con la possibilità di utilizzare ogni attributo presente in MAS che sia collegabile logicamente all’asset. I parametri di peso sono inseribili a livello di formula ed editabili manualmente dall’utente che può ad esempio modificare i pesi associati a determinate caratteristiche degli asset o aggiungere ulteriori caratteristiche e pesi. Le formule consentono di attingere alla base dati storica, ad esempio utilizzando lo storico delle Difformità per definire tassi di guasto o di anomalia. Una volta definite le formule, l’utente le associa ad un indicatore di condizione assegnando un peso specifico a ciascuna formula. Quando viene effettuato il calcolo dell’indicatore, il sistema esegue una somma pesata degli output delle formule per ricavare il valore dell’indicatore (se l’utente non specifica il peso, il sistema combina linearmente le formule). Di seguito si riporta la lista di oggetti utilizzati in Input per il calcolo degli Indicatori configurati a sistema e la relativa applicazione di gestione in MAS:

| Indicatore di condizione | Input | Applicazione di riferimento |
| --- | --- | --- |
| Tasso di guasto | Storico Guasti | Applicazione difformità |
| Tasso di guasto | Storico Consistenze | Anagrafica asset |
| Tasso di anomalia | Storico Anomalie | Applicazione difformità |
| Tasso di anomalia | Storico Consistenze | Anagrafica asset |
| HI conduttori | Vetustà asset | Anagrafica asset |
| HI conduttori | Difformità | Applicazione difformità |
| HI conduttori | Composizione del fascio | Anagrafica asset |
| HI conduttori | OdL in stato Aperto e Scaduto associati a PdL specifici | Applicazione Monitoraggio OdL |
| HI funi di guardia | Vetustà asset | Anagrafica asset |
| HI funi di guardia | Difformità | Applicazione difformità |
| HI funi di guardia | OdL in stato Aperto e Scaduto associato a PdL specifico | Applicazione Monitoraggio OdL |
| HI struttura | Vetustà asset | Anagrafica asset |
| HI struttura | Storico OdL associati ad attività rilevanti sul sostegno | Applicazione Monitoraggio OdL |
| HI struttura | Tasso di guasto | Modulo Health |
| HI linee in cavo | Vetustà asset | Anagrafica asset |
| HI linee in cavo | Tipo di isolamento | Anagrafica asset |
| HI linee in cavo | OdL di tipo campagna | Applicazione Monitoraggio OdL |
| HI linee in cavo | Storico Anomalie | Applicazione difformità |
| Hi equipment in blindato | Vetustà asset | Anagrafica asset |
| Hi equipment in blindato | Storico Difformità | Applicazione difformità |
| Hi equipment in blindato | OdL in stato Aperto associati a PdL specifici | Applicazione Monitoraggio OdL |
| HI macchinario | Misure | Applicazione difformità |
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

Tabella 7 - Tabella di relazione indicatori di condizione / Input / Applicazione di riferimento

Gli indicatori così definiti sono calcolati ogni giorno per ogni Asset contenuto nel set nel caso degli HI e dei K di eleggibilità, mentre nel caso dei tassi di guasto / anomalia il calcolo viene effettuato ogni anno per ogni Asset a cui sono associati.

Come anticipato, il sistema è inizializzato con lo storico dei dati (Difformità, Anomalie e OdL) utili al primo calcolo degli indicatori.

Gli indicatori di condizione calcolati nel modulo “Health” sono utilizzabili come input delle Policy definite sulle relative tipologie di Asset nel modulo “Manage” dove verranno letti seguendo la logica a eventi descritta nel paragrafo 3.1.1.1.

In particolare, la variazione degli HI dovuta al calcolo giornaliero innesca le Policy nel momento in cui vengono superati i target di accettabilità definite dall’utente negli Input, generando OdL in base alla definizione contenuta nella Policy.

Gli HI e i K di eleggibilità sono storicizzati mensilmente, mentre gli indicatori di condizione annualmente. I dati storicizzati saranno utilizzabili ai fini della reportistica. È possibile consultare lo storico degli indicatori di condizione sia tramite l’Applicazione “Asset” del modulo “Manage” sia tramite l’applicazione “Asset” del modulo “Health” (riportato un esempio di visualizzazione in Figura 84).

Figura 84 - Visualizzazione HI nell'applicazione "Asset" del modulo Health

# Gestione capacity

## Verifica e gestione capacity

In questo capitolo vengono presentate le modalità di inserimento e di verifica della saturazione capacity relativa all’unità di budget. Viene inoltre descritto lo use case inerente alla gestione dello scambio ore tra le UdB.

Inserimento capacity

L’inserimento della capacity viene effettuato prima dell’inizio della pianificazione annuale o nel corso dell’anno tramite l’applicazione «Capacity». Una volta nell’applicazione, l’utente ha accesso alla tabella di vista elenco iniziale delle capacity (Figura 85), costituita dalle seguenti colonne

Titris: unità di budget. Campo filtrabile;

Anno: anno di riferimento. Campo filtrabile;

Ultima modifica: data di ultima modifica delle capacity. Campo non filtrabile

Modificato da: indica l’utente/sistema che ha effettuato l’ultima modifica. Campo non filtrabile;

Scenario: indica lo scenario importato (cfr. 5.1.1.1). Campo filtrabile. Si specifica che il contenuto di questo campo è composto da 10 digit:

I primi 4 digit rappresentano l’anno di riferimento del Budget;

I successivi 4 digit rappresentano la fase (OPCL Preclosing - OBDG Budget - F1O_ Forecast);

Gli ultimi 2 digit rappresentano l’anno di riferimento della fase.

Esempio: 2024F1O_25 -> Forecast 2025 prodotto nel 2024

Stato: Gli utenti centrali tramite questo campo possono abilitare o inibire la modifica delle tabelle di capacity nei confronti delle altre UdB, inserendo uno dei seguenti stati:

Non modificabile da UdB: stato di default, solo gli utenti centrali possono effettuare modifiche;

Modificabile da UdB: in questo stato anche le altre UdB possono effettuare modifiche.

Il campo risulta filtrabile.

L’inserimento degli stati sopra descritti avviene cliccando sull’azione laterale “Cambio stato” di Figura 85 visibile solamente agli utenti centrali. Una volta cliccato su tale funzionalità appare una dialog all’interno della quale è possibile selezionare i record rappresentanti le capacity di una determinata UdB e di uno specifico anno (è possibile effettuare una selezione massiva tramite un “select all” oppure una selezione dei singoli record). Sempre all’interno della dialog è presente un campo “Stato” tramite il quale si seleziona lo stato (“modificabile” o “non modificabile”) che si vuole impostare per i record selezionati.

L’azione laterale in blu “Import” di Figura 85 sarà necessaria per effettuare l’import delle capacity da Tagetik (cfr. 5.1.1.1).

Ogni record presente nella vista elenco contiene la capacity di una determinata UdB per uno specifico anno. A schermo, per ogni UdB, saranno presenti i record fino all’anno n+5.

Figura 85 - Vista elenco applicazione "Capacity"

Cliccando su un record si viene reindirizzati sul tab “Capacity iniziale”, contenente la tabella di capacity (Figura 86), all’interno del quale sono presenti:

I campi di testata “Titris”, “Ultima modifica”, “Modificato da”, “Anno” e “Scenario”, “Totale capacity operaio”, “Totale capacity quadro”, “Totale capacity impiegato” in sola lettura, valorizzati automaticamente in base al record selezionato;

La tabella “Capacity” contenente la capacity mensilizzata per ogni figura professionale.

Figura 86 - Vista di capacity iniziale

Per popolare/modificare le tabelle “Capacity” nel tab “Capacity iniziale” sono previste 3 modalità alternative:

Import da Tagetik;

Modifica manuale;

Caricamento di un file Excel.

Si sottolinea che ogni modifica sovrascrive il contenuto precedente; non sono quindi presenti più versioni della stessa capacity.

La capacity delle unità Terna Gora e NewCo potranno essere gestite anche da altre unità, in particolare:

Capacity udb Terna Gora: La modifica manuale, il caricamento da mxloader e lo scambio ore potranno essere effettuati, oltre che da Terna Gora stessa, anche dal team programmazione dell’unità Abruzzo-Molise

Capacity udb NewCo: La modifica manuale, il caricamento da mxloader e lo scambio ore potranno essere effettuati, oltre che da NewCo stessa, anche dal team programmazione dell’unità Lazio Sud

Infine i COAM potranno gestire anche la capacity (modifica manuale, caricamento da mxloader, scambio ore) delle RL della propria DT e delle udb NORTN.

#### Import da Tagetik

Come anticipato nel capitolo 5.1.1, nella pagina di inserimento capacity, gli utenti centrali e gli utenti delle singole UdB (solo quelli il cui record di capacity ha stato “Modificabile da UdB”) possono importare la capacity tramite l’azione laterale “Import” che innesca il popolamento delle tabelle di capacity effettuando un import da Tagetik. Una volta cliccato sull’azione laterale viene visualizzata una dialog all’interno della quale bisogna specificare:

Titris

Scenario di riferimento.

Per gli utenti centrali tramite questa funzionalità è possibile importare sia le capacity di tutte le UdB (lasciando il campo “Titris” vuoto) che la capacity di una UdB (selezionando nella dialog l’UdB di interesse). Si specifica che sarà possibile selezionare uno e un solo scenario.

Per gli utenti delle singole UdB è possibile importare solamente la capacity della propria UdB selezionando lo scenario di interesse.

Nel caso di import da Tagetik nel campo “modificato da” appare il riferimento al sistema esterno, ossia Tagetik.

Inoltre, nel caso in cui Tagetik per una determinata UdB non disponga della capacity per alcune figure professionali, allora nelle tabelle di capacity EAM di queste figure verrà riportato/sovrascritto un valore uguale a 0.

Es: Viene effettuato l’import della capacity dell’UdBx e in Tagetik sono presenti i valori di capacity solamente per impiegato e operaio. In questo caso, una volta effettuato l’import, le tabelle di capacity su EAM vengono valorizzate nel seguente modo:

Per impiegato e operaio: le tabelle di capacity vengono valorizzate con i valori presenti in Tagetik

Per quadro: la tabella di capacity viene valorizzata con 0 in quanto non è presente un valore in Tagetik. Si precisa che, nel caso in cui la tabella di capacity quadro su EAM presentasse valori diversi da 0 prima dell’import, questi verranno sostituiti con 0 per tutti i mesi a seguito dell’import.

#### Modifica manuale

Oltre che tramite import, è possibile modificare le tabelle rappresentate in Figura 86 anche manualmente. In questo caso basta cliccare su una specifica cella della tabella (corrispondente a un determinato mese/figura professionale), inserire il valore e salvare. Nel campo “modificato da” appare il numero di matricola che ha effettuato la modifica. Nel caso in cui si modifichi manualmente una capacity precedentemente importata da Tagetik, il campo “scenario” verrà devalorizzato e quindi reso vuoto.

#### Caricamento file Excel

Per popolare/modificare le tabelle di capacity è possibile utilizzare la funzionalità di caricamento Excel tramite MXLoader.

Gestione scambio ore UdB

I Titris possono utilizzare la funzionalità di scambio ore per cedere e ricevere ore uomo (Operai, Impiegati, Quadri) da impiegare per l’esecuzione degli OdL di loro competenza.

Per accedere alla funzionalità di scambio ore l’utente deve selezionare un record, relativo a una determinata UdB e a uno specifico anno, dalla vista elenco dell’applicazione “Capacity” (Figura 85) e successivamente selezionare il tab “Scambio ore” rappresentato in Figura 87.

Figura 87 - Tab "Scambio ore"

Nel tab “Scambio ore” sono presenti le seguenti tabelle:

Ore in uscita: contiene tutte le transazioni relative alle ore cedute dall’UdB. Per ogni transazione si riporta:

Natura: La figura professionale per la quale sono cedute le ore;

Qtà proposta: Ore proposte da cedere;

Qtà accettata: Ore accettate dall’UdB ricevente;

Mese: mese per il quale si cedono le ore;

Resp. Attività destinatario: UdB responsabile attività ricevente;

Stato: Lo stato della transazione può assumere 3 valori:

Proposta: Indica che è stata proposta la cessione di ore da parte del mittente. In questo caso l’UdB ricevente non ha né accettato né rifiutato l’offerta. Il campo “Qtà accettata” sarà blank;

Accettata: indica che l’UdB ricevente ha accettato l’offerta e ha specificato il numero di ore da accettare, che deve essere <= del numero di ore offerte;

Rifiutata: indica che la proposta di cessione di ore è stata rifiutata dal destinatario. Il campo “Qtà accettata” avrà valore uguale a zero.

Ore in entrata: contiene tutte le transazioni relative alle ore in entrata, ossia le ore cedute dalle altre UdB alla propria UdB. Per ogni transazione si riporta:

Natura: La figura professionale per la quale sono cedute le ore;

Qtà proposta: Ore proposte in entrata;

Qtà accettata: L’utente visualizza la quantità accettata pre-valorizzata con quantità proposta. Successivamente l’utente, se lo ritiene necessario, può modificare questo campo inserendo un valore minore o uguale alla quantità proposta;

Mese: mese per il quale si ricevono le ore;

Titris mittente: L’UdB mittente;

Stato: Lo stato della transazione può assumere 3 valori:

Proposta: Indica che è stata proposta la cessione di ore da parte del mittente. In questo caso l’UdB ricevente non ha né accettato né rifiutato l’offerta. Il campo “Qtà accettata” sarà uguale a “Qtà proposta”;

Accettata: indica che l’UdB ricevente ha accettato l’offerta e ha specificato il numero di ore da accettare, che deve essere <= del numero di ore offerte;

Rifiutata: indica che la proposta di cessione di ore è stata rifiutata dal ricevente. Il campo “Qtà accettata” avrà valore uguale a zero.

Di seguito si riporta il processo di offerta e accettazione delle ore tra le UdB:

l Titris CEDENTE inserisce una proposta di cessione nella tabella “Ore in uscita” (Figura 87), aggiungendo un nuovo record, tramite il pulsante “+”, per il quale deve specificare:

La natura delle ore cedute (Operai, Impiegati, Quadri);

La quantità delle ore cedute;

Mese cessione;

L’UdB a cui indirizzare la cessione.

Dopo aver compilato le informazioni necessarie alla cessione di ore ad altre UdB, il responsabile dell’UdB conferma e invia la proposta salvando il record.

Il responsabile attività RICEVENTE nella schermata iniziale del MAS chiamata “Centro di controllo” (utilizzata in MAS come dashboard personalizzabile per visualizzare e accedere alle informazioni più rilevanti) visualizza le UdB mittenti che hanno proposto una quantità di ore da scambiare e cliccando sul singolo record della tabella, viene reindirizzato all'applicazione di gestione della relativa capacity, dalla quale è possibile accedere al tab "scambio ore”

L’UdB RICEVENTE può:

Accettare l’offerta: Nel centro di controllo clicca sul link e viene reindirizzato all'applicazione di gestione della relativa capacity, dalla quale è possibile accedere al tab "scambio ore”. Successivamente, all’interno della tabella “Ore in entrata” l’utente può decidere se mantenere la qtà pre-valorizzata (= alla qtà proposta), oppure modificare il campo “Qtà accettata” con un valore minore o uguale alla qtà proposta. Una volta fatto ciò cambia lo stato in “Accettata” per la proposta che intende accettare. Contestualmente, il mittente vedrà lo stato della sua transazione in uscita passare da “Proposta” a “Accettata” (il passaggio sarà effettuato automaticamente dal sistema).

Rifiutare l’offerta: Nel centro di controllo clicca sul link e viene reindirizzato all'applicazione di gestione della relativa capacity, dalla quale è possibile accedere al tab "scambio ore”. All’interno della tabella “Ore in entrata” cambia lo stato della transazione in “Rifiutata” e automaticamente viene inserito il valore “0” nel campo “Qtà accettata”. Il mittente vedrà lo stato della sua transazione in uscita passare da “Proposta” a “Rifiutata” (il passaggio sarà effettuato automaticamente dal sistema).

Verifica saturazione capacity

I Titris possono utilizzare la funzionalità di verifica saturazione della capacity per verificare gli assorbimenti della capacity pianificata nel PMI e le quadrature.

Per accedere alla funzionalità di verifica di saturazione della capacity l’utente deve selezionare un record, relativo a un determinato Titris e a uno specifico anno della vista elenco dell’applicazione “Capacity” (Figura 85) e successivamente selezionare il tab “Monitoraggio Capacity” rappresentato in Figura 88.

Dopo l’inserimento della capacity iniziale, nel tab “Monitoraggio capacity” dell’applicazione “Capacity”, l’utente monitora la capacity del Titris, relativa a un determinato anno, tramite la schermata riportata in Figura 88.

Figura 88 - Schermata di monitoraggio capacity

In questo tab per ogni figura professionale (Operai, Impiegati, Quadri) sono presenti 3 tabelle (inizialmente compresse per semplicità di navigazione):

Tabella “Teorico”, contenente i seguenti campi:

Mese;

Capacity: Capacity iniziale (inserita nel tab di inserimento della capacity);

Proprio teorico: Capacity – Ceduto teorico;

Ceduto teorico: Ore cedute ad altre UdB nel processo di condivisione degli scambi intracompany. Vengono contate solo le ore cedute che sono state accettate dall'UdB ricevente

Ricevuto teorico: Ore ricevute da altre UdB nel processo di condivisione degli scambi intracompany. Vengono contate solo le ore ricevute che sono state accettate dall'UdB ricevente;

Forza lavoro teorica: Proprio teorico + Ricevuto teorico.

Tabella “Pianificato”, contenente i seguenti campi:

Proprio pianificato puntuale: Somma delle ore assorbite negli OdL che insistono sulla propria UdB dal personale dell’UdB flaggate “PMI”. Molti OdL potrebbero essere a cavallo mese o cavallo anno. In questi casi l’effort orario viene distribuito proporzionalmente all’elapsed pianificato nei vari mesi. Es: Un OdL necessita di 100 ore Operaio con data inizio pianificata il 15 Marzo e data fine pianificata il 30 Aprile. In questo caso 34 ore (1/3 del totale) verranno assegnate al mese di Marzo mentre le restanti 66 (2/3 del totale) al mese di Aprile. Si specifica che per il calcolo di questo campo vengono considerati gli OdL in stato:

Aperto

Schedulabile

Schedulato

Inviato/Inoltrato

Avviato

Eseguito parzialmente

Eseguito

Chiuso

Da allocare a serbatoio: Campo valorizzato manualmente dall’utente. Al tempo t0 la colonna è vuota; viene valorizzata successivamente per abilitare la funzione di creazione di OdL serbatoio (cfr. cap. 7.1.1);

Ceduto pianificato: Somma delle ore assorbite negli OdL di altre UdB dal personale della propria UdB;

Saturazione pianificata: (Proprio pianificato + Ceduto pianificato) / Capacity;

Conto base residuo pianificato: Capacity – Proprio pianificato – Ceduto pianificato –;

Ricevuto pianificato: Somma delle ore, assorbite negli OdL della propria UdB, ricevute da altre UdB;

Forza lavoro pianificata: Proprio pianificato + Ricevuto pianificato.

Tabella “Check”, contenente i seguenti campi:

Ceduto teorico: stesso valore presente nella tabella “Teorico”;

Ceduto pianificato: stesso valore presente nella tabella “Pianificato”;

Ceduto da pianificare: Ceduto teorico – Ceduto pianificato. Rappresentano le ore che, seppur condivise nella maschera degli scambi intracompany, non risultano ancora assorbite dall'unità ricevente;

Ricevuto teorico: stesso valore presente nella tabella “Teorico”;

Ricevuto pianificato: stesso valore presente nella tabella “Pianificato”;

Ricevuto da pianificare: Ricevuto teorico – Ricevuto pianificato. Rappresentano le ore che, seppur condivise nella maschera degli scambi intracompany, non risultano ancora assorbite dalla propria UdB.

In questo modo ogni UdB avrà una visione complessiva della propria capacity. Si specifica che nelle tabelle di “Teorico”, “Pianificato” e “Check” sarà possibile visualizzare tutti i 12 mesi in una schermata.

Al fine di aiutare gli utenti centrali a individuare le sovrasaturazioni presenti nelle diverse UdB, nel centro di controllo di questi utenti è presente una sezione contenente una lista di record rappresentanti le UdB e relativi anni per le quali sono presenti sovra-saturazioni, come rappresentato in Figura 89.

Figura 89 - Elenco di UdB sovrasaturate

Cliccando su un record l’utente viene reindirizzato al tab “Monitoraggio capacity” di quella specifica UdB e anno.

## Interfacce/Invio dati verso Tagetik

Nei capitoli 5, 8 e 10 vengono descritte funzionalità che richiedono integrazione o caricamento dati verso il sistema Tagetik, tra cui:

Import capacity iniziale

Import tariffe

Invio dati scenari verso Tagetik

Import scenari

Import capacity iniziale

L’import della capacity iniziale viene effettuato a partire dall’applicazione di gestione della capacity.

Come descritto nel capitolo 5.1.1.1, nella pagina di inserimento capacity è presente l’azione laterale “Import” che innesca il popolamento delle tabelle di capacity effettuando un import da Tagetik. Una volta cliccato sull’azione laterale viene visualizzata una dialog all’interno della quale bisogna specificare:

Titris

Scenario di riferimento.

Tramite questa funzionalità è possibile importare sia le capacity di tutte le UdB (lasciando il campo titris vuoto ) che le capacity di specifiche UdB (selezionando nella dialog solo le UdB di interesse).

Import CMO

Le tariffe (costi medi orari) presenti in Tagetik sono definite per Titris anno, figura professionale e scenario.

Nella pagina di gestione tariffe è presente l’azione laterale “Import” che innesca il popolamento delle tabelle tariffa in import da Tagetik. Una volta cliccato sull’azione laterale viene visualizzata una dialog all’interno della quale bisogna specificare:

Titris

Scenario di riferimento.

Tramite questa funzionalità è possibile importare sia le tariffe di tutte le UdB (lasciando il campo titris vuoto) che le tariffe di specifiche UdB (selezionando nella dialog solo le UdB di interesse).

Invio dati scenari verso Tagetik

Come descritto nel par. 15.3.2.1 tramite uno strumento di BI sarà possibile consultare i report rappresentativi degli scenari di pianificazione aggregati per ogni tipo spesa (Opex, Capex e ANR). Quest’ultimi saranno successivamente esportabili tramite download dallo strumento di BI e successivamente caricabili manualmente in Tagetik.

Import scenari

Come descritto nel par. 5.1.1.1 e nel cap. 11.4, al momento dell’import della capacity e delle tariffe, è necessario specificare il codice dello scenario. Poiché il codice dello scenario è un dato che risiede in Tagetik, dovrà anch’esso essere importato. L’utente per importare la lista completa degli scenari presenti in Tagetik dovrà accedere all’applicazione di gestione della capacity e utilizzare la funzionalità “Import scenari”

Figura 90 - Funzionalità di import scenari

Una volta cliccato su “Import scenari” l’interfaccia permetterà di importare la lista completa degli scenari presenti in Tagetik. In questo modo il campo “scenario” della dialog di import capacity e della dialog di import tariffe sarà valorizzabile tramite una lista valori contenente i codici degli scenari precedentemente importati

Si specifica che:

La funzionalità di import degli scenari sarà attivata solo per gli utenti centrali;

Tramite la suddetta funzionalità verranno importati tutti i codici degli scenari presenti in Tagetik. Non sarà quindi possibile scegliere un subset di scenari da importare.

La lista importata degli scenari è comune sia per le capacity che per le tariffe

# OdL on condition e periodici

## Struttura degli Ordini di Lavoro

In questo capitolo vengono descritti:

L’organizzazione e il contenuto delle informazioni presenti all’interno dell’OdL;

Le modalità di accorpamento e le logiche di gestione dell’OdL aggregatore.

Attributi di caratterizzazione dell’OdL

In base a determinati attributi, ogni OdL segue un processo di gestione differente all’interno del sistema. Questi attributi risultano utili anche per l’estrazione di report e, inoltre, influenzano la presenza/assenza di determinate informazioni.

I principali attributi di caratterizzazione individuati sono i seguenti:

Perimetro intervento: L’attributo «Perimetro intervento» dell’OdL può assumere i valori “Sostituzione”, “Nuove installazione”, “Manutenzione”, “Monitoraggio”,“Demolizione”, “Attività non operative”, “ANR”, “Altre attività” e “Pronto intervento”. Il perimetro intervento viene determinato in base alla tipologia di attività definita per ogni PdL, seguendo le corrispondenze presenti nella seguente tabella:

| Tipologia di attività | Perimetro intervento |
| --- | --- |
| Controllo periodico | Monitoraggio |
| Controllo di legge | Monitoraggio |
| Controllo tecnico | Monitoraggio |
| Controllo occasionale | Monitoraggio |
| Controllo su condizione | Monitoraggio |
| Manutenzione su condizione | Manutenzione |
| Sostituzione | Sostituzione |
| Nuova installazione | Nuove installazioni |
| Demolizione | Demolizione |
| Pronto intervento | Pronto intervento |
| Attività non operative | Attività non operative |
| ANR | ANR |
| Altre attività | Altre attività |

Tabella 8 - Correlazione tra tipologia di attività e perimetro intervento

Tipo OdL: L’attributo «Tipo OdL», sostanzialmente, definisce la modalità di creazione dell’OdL ed ammette i valori “Manuale”, “On condition” e “Periodico”.

OdL Serbatoio: Il presente attributo viene popolato con “SI” o “NO” e definisce se l’oggetto si tratta di un OdL a serbatoio o meno.

Assistenza/Sorveglianza: Attributo specifico degli OdL. Serve a racchiudere le attività per gestire le casistiche di opportunità di cantiere. Può essere valorizzato con i seguenti valori:

Assistenza;

Sorveglianza;

Informazioni associate agli OdL

La tipologia di OdL caratterizza le informazioni associate agli OdL stessi. In generale, le informazioni cambiano in funzione della tipologia di OdL. Si riporta di seguito uno specchietto riepilogativo contente le informazioni obbligatorie (O), facoltative (F) e non presenti (NP) in base alla tipologia di OdL trattato; viene inoltre riportata la modalità di popolamento dell’informazione (può essere automatica (A) o manuale (M)) e la possibilità di modifica del campo (SI/NO). L’obbligatorietà è dettagliata nel capitolo successivo.

|  | NO SERBATOIO DA DSS | NO SERBATOIO MANUALE | SERBATOIO | Modalità di popolamento | Modificabilità (SI/NO) |
| --- | --- | --- | --- | --- | --- |
| Identificativo PdL associato | O | O | O | A | SI |
| Tipologia PdL associato | O | O | O | A | NO |
| Assieme e sottoassieme attività | F | F | NP | A | NO |
| Fisico | O | O | O | A | NO |
| Programma | F | F | NP | A | NO |
| Perimetro intervento | O | O | O | A | NO |
| Priorità | O | O | NP | A | NO |
| Tipo OdL | O | O | O | A | NO |
| Cliente | O | O | O | A o M | SI |
| Outsourcing? | O | O | NP | A | SI |
| Trainante/trainato | F | F | NP | A | NO |
| OdL aggregatore | O | O | NP | M | SI |
| Assistenza/sorveglianza | O | O | NP | A | SI |
| OdL serbatoio | O | O | O | M | SI |
| Descrizione PdL | O | O | O | A | NO |
| ID macro-attività | O | O | O | A | NO |
| Metodi | O | F | NP | A | NO |
| Responsabile attività | O | O | O | A | SI |
| ID OdL | O | O | O | A | NO |
| Descrizione OdL | O | O | O | A | NO |
| ID e descrizione asset | O | O | NP | A | NO |
| SAP ID asset | O | O | NP | A | NO |
| Gerarchia asset | O | O | NP | A | NO |
| ID e descrizione policy | O | NP | NP | A | NO |
| Stato | O | O | O | A | SI |
| Responsabile asset | O | O | NP | A | SI |
| Oggetto contabile | O | O | O | M | SI |
| Gerarchia responsabile asset | O | O | NP | A | NO |
| Valore RE | F | F | F | M | SI |
| Durata pianificata | O | O | O | A | SI |
| Tempo trasferimento totale pianificato | O | O | NP | A | SI |
| Data inizio “non prima di” | O | O | NP | A | NO |
| Data pianificata inizio | O | O | O | A | SI |
| Data pianificata fine | O | O | O | A | SI |
| Data pianificata modificata? | F | F | NP | A | NO |
| Data eleggibilità | F | NP | NP | A | NO |
| Data modello DSS | O | O | NP | A | NO |
| Data chiusura | O | O | O | A | NO |
| Data esecuzione | O | O | NP | A | NO |
| Data schedulazione | O | O | NP | A | NO |
| Data di massimo posticipo attività | O | O | NP | A | NO |
| Effort pianificato operai | O | O | O | A | NO |
| Effort pianificato quadri | O | O | O | A | NO |
| Effort pianificato impiegati | O | O | O | A | NO |
| Note | F | F | F | M | SI |
| Data inizio e fine indisponibilità | F | F | NP | A | NO |
| ID richiesta indisp. | F | F | NP | A | NO |
| Stato richiesta indisponibilità | F | F | NP | A | NO |
| Storno totale SAP | F | F | NP | A | NO |
| Flag “PMI?” | O | O | O | A | SI |
| Attività esitata da mobile | F | F | NP | A | NO |
| Matricola e nome esecutore | F | F | NP | A | NO |

Tabella 9 - Matrice informazioni - OdL

#### Informazioni caratteristiche OdL

Tra i campi presenti nella Tabella 9 si individuano alcuni campi “anagrafici” dell’OdL, ossia i principali campi caratterizzanti l’OdL. Questi sono di seguito elencati:

Tab “Ordine di lavoro”:

ID OdL: generato dal sistema;

Descrizione OdL: ereditato dalla descrizione PdL;

Perimetro intervento: preimpostato sulla base della tipologia di attività del PdL;

Priorità: ereditato dal PdL non modificabile;

Fisico: calcolato dal DSS sulla base dell’asset sul quale è stato generato l’ordine;

Cliente: Popolato automaticamente in base a quanto presente nel campo “società proprietaria” dell’asset oggetto di OdL. Nel caso in cui l’asset oggetto di OdL non abbia il campo “società proprietaria” popolato, allora il campo cliente dovrà essere popolato automaticamente con quanto riportato nell’asset di primo livello. Se anche il primo livello non dovesse avere il campo “società proprietaria” valorizzato, allora il campo “cliente” dell’odl dovrà essere popolato manualmente dall’utente tramite una picklist contenente i valori TR01, TR36, TR64, TR30.

Outsourcing?: ereditato dal PdL ma modificabile dall’utente;

Unità di misura: ereditato dal PdL;

Tipo OdL;

OdL Serbatoio?

OdL Aggregato?

ID Asset e descrizione Asset;

SAP ID Asset;

ID e descrizione policy;

Stato;

Assistenza/Sorveglianza: campo visibile e modificabile solo se il campo “Outsourcing?” impostato su “SI”. In caso contrario il campo non è visibile. Impostato di default a “Sorveglianza” (se presente il campo) per ordini generati dal DSS;

Sotto-programma: campo ereditato dalla Policy in fase di generazione (per OdL generati da DSS) e modificabile dall’utente. Inserito manualmente per ordini manuali.

Oggetto contabile;

Gerarchia responsabile asset;

Responsabile Asset (solo nel caso di ordini su asset associati a Linea multi-competenza sarà possibile modificare il “responsabile asset” selezionando una delle UI associate nella tabella di riepilogo dati dell’asset ma non sarà possibile lasciarlo blank);

Responsabile Attività;

Trainante / trainato;

Note.

Storno totale SAP

Flag “PMI” : Per gli ordini figli e singoli è sempre visibile (non ci sono restrizioni di visibilità basate sulla tipologia dell’odl e sul tipo spesa) ed editabile; risulta infatti modificabile manualmente dall’utente in ogni stato dell OdL per contrassegnare l’OdL come facente parte del PMI dell’anno di budget. Il campo è modificabile massivamente tramite MXLoader. Di default il flag viene valorizzato a FALSE ad eccezione degli ordini serbatoio per i quali invece viene valorizzato di default a TRUE. Si specifica che per gli ordini padri il flag non è presente.

Per quanto riguarda tutte le informazioni relative all’asset su cui è stato generato l’ordine, è possibile accedervi tramite un link che rimanda all’applicazione asset.

#### Informazioni del PdL associato

Rispetto alle informazioni presenti sul PdL associato, saranno visibili sull’OdL solo un sottoinsieme di queste. Le informazioni relative al PdL non sono modificabili nell’OdL. La modifica dell’anagrafica del PdL è demandata alla gestione dei cataloghi. In fase di pianificazione (stato “Da Validare” e “Aperto” dell’ordine), sull’OdL l’utente può cambiareil PdL associato tramite la funzionalità “Rimuovi pdl”. Sia per ordini manuali che da DSS, una volta rimosso il PdL, è possibile reinserire tutti i pdl appartenenti alla stessa macro-attività, a prescindere che l’attività sia caricabile manualmente o meno.L’elenco di informazioni associate al PdL è il seguente:

Tab “Ordine di lavoro”:

ID e descrizione PdL;

ID Macro-attività associata al PdL;

Assieme e sotto-assieme attività;

Tipologia PdL (cluster);

Programma;

Elive

Tab “Piani”:

Workflow dei Metodi associati.

#### Informazioni di processo

Per le varie fasi del ciclo di vita dell’OdL vengono predisposti i seguenti campi suddivisi in tab:

Tab “Pianificazione”:

Valore RE.

Tabella “micro-controlli”: presente quando nel pdl dell’odl sono riportati dei micro-controlli. La tabella è composta dalle seguenti colonne:

Attività: identificativo del micro-controllo (es: 10, 20, 30…)

Asset: assetnum dell’asset oggetto di micro-controllo

Descrizione asset

SAP ID

ID microcontrollo

Descrizione: descrizione del micro-controllo

Si specifica che nel tab «Pianificazione» sarà possibile aggiungere micro-controlli o eliminare quelli esistenti. In caso di aggiunta di un micro-controllo l’utente dovrà popolare i seguenti campi:

Asset: verranno restituiti tutti gli asset figli dell’asset oggetto di odl (es: se l’odl è sullo stallo, allora verranno restituiti tutti i figli dello specifico stallo)

ID micro-controllo: verranno restituiti tutti i micro-controlli relazionati – nel catalogo dei micro-controlli – alla classificazione dell’asset inserito nel campo «Asset».

Tab “Assegnazioni”: in questo tab vengono riportate tutte le informazioni di schedulazione ricevute da Geocall, in particolare:

Date di inizio e fine schedulazione

Date di inizio e fine pianificazione

Tabella “Schedulazione risorse”: riporta le informazioni della schedulazione più recente, in particolare:

Data ricezione: data di ricezione della matricola schedulata

Risorsa: matricola risorsa

Nome risorsa: nome e cognome risorsa

Preposto: campo booleano. Popolato con 1 quando la matricola risulta come preposto in Geocall. Popolato con 0 quando la matricola non risulta come preposto in Geocall

Data inizio schedulata

Data fine schedulata

Tabella “Storico schedulazione risorse”: riporta lo storico delle schedulazioni passate, in particolare:

Data ricezione: data di ricezione della matricola schedulata

Risorsa: matricola risorsa

Nome risorsa: nome e cognome risorsa

Preposto: campo booleano. Popolato con 1 quando la matricola risulta come preposto in Geocall. Popolato con 0 quando la matricola non risulta come preposto in Geocall

Data inizio schedulata

Data fine schedulata

Tab “Avanzamento:

Attività esitata da mobile: flag popolato automaticamente quando sul mobile risulta valorizzato il flag “Attività esitata”. Questo flag è puramente informativo, non è dunque vincolante per le logiche di cambio stato.

Matricola esecutore: numero di matricola di chi ha esitato l’attività sul mobile

Nome matricola: Nome e cognome matricola esitatore

Tabella “micro-controlli”: vengono riportati gli stessi micro-controlli presenti nel tab “Pianificazione”. La tabella è composta dai seguenti campi:

Attività: identificativo del micro-controllo (es: 10, 20, 30…)

Asset: assetnum dell’asset oggetto di micro-controllo

Descrizione asset

SAP ID

ID microcontrollo

Descrizione: descrizione del micro-controllo

Stato microcontrollo: lo stato può assumere i valori APERTO, ESEGUITO, CHIUSO, in particolare:

Stato APERTO: Stato assunto dal microcontrollo appena creato

Stato ESEGUITO: Stato assunto dal micro-controllo quando viene valorizzato il check di esitazione lato mobile o quando viene utilizzato il pulsante di cambio stato evidenziato in figura. Se l’odl si trova in uno stato diverso da ESEGUITO o CHIUSO è possibile inserire difformità, sia lato web che lato mobile, in un micro-controllo in stato ESEGUITO

Stato CHIUSO: stato assunto dal micro-controllo quando l’odl passa in CHIUSO

Tab “indisponibilità”:

Data inizio indisponibilità;

Data fine indisponibilità;

ID richiesta indisponibilità;

Stato richiesta indisponibilità.

Tab “date”:

Data non prima di: definita dal DSS sulla base del valore del campo massimo anticipo attività presente sul catalogo delle policy visualizzata sul tab date dell’OdL ma non modificabile dall’utente;

Data eleggibilità: data in cui l’asset diventa elegibile;

Data modello DSS: definita dal DSS sulla base della data di generazione più la frequenza (per le attività periodiche) o la differibilità (per le attività on condition). Visualizzata sul tab date dell’OdL ma non modificabile dall’utente;

Data di massimo posticipo attività: definito dal DSS sulla base del valore del campo massimo posticipo attività presente sul catalogo delle policy ; visualizzata nel tab date dell’OdL ma non modificabile dall’utente;

Data pianificata fine: impostata dal DSS alla generazione con lo stesso valore della data modello DSS. Visualizzata nel tab date sull’OdL e modificabile dall’utente o dall’ottimizzatore.

Data pianificata inizio: definita dal DSS come differenza tra la data pianificata fine e la durata dell’attività. Visualizzata nel tab date dell’OdL e modificabile dall’utente o dall’ottimizzatore;

Data esecuzione: popolata dal sistema una volta terminata l’esecuzione dell’attività;

Data chiusura: popolata dal sistema una volta chiusa l’attività sia in Geocall che in EAM;

Data schedulazione: data schedulata da Geocall per l’esecuzione dell’attività.

Data inizio indisponibilità: popolata dal sistema sulla base della delibera della richiesta indisponibilità inviata da Agile;

Data fine indisponibilità: popolata dal sistema sulla base della delibera della richiesta indisponibilità inviata da Agile;

Durata pianificata: definita sul catalogo Attività;

Data pianificata modificata: flag che indica se le date di pianificazione sono state modificate (manualmente o dall’ottimizzatore) almeno una volta da quando sono state generate;

Tempo trasferimento totale pianificato: Calcolato automaticamente dal sistema; rappresenta la somma delle ore di trasferimento associate a tutte le risorse assegnate all’attività, presenti nel tab “Pianificazione”. Come riportato nel capitolo 8.1.2.3.2, solo per gli OdL aggregatori il tempo di trasferimento deve essere popolato manualmente.

Effort pianificato operai: calcolato automaticamente dal sistema come prodotto delle ore di esecuzione pianificate e la qtà degli operai, riportati nel tab “Pianificazione”

Effort pianificato quadri: calcolato automaticamente dal sistema come prodotto delle ore di esecuzione pianificate e la qtà dei quadri, riportati nel tab “Pianificazione”

Effort pianificato impiegati: calcolato automaticamente dal sistema come prodotto delle ore di esecuzione pianificate e la qtà degli impiegati, riportati nel tab “Pianificazione”

Per quanto riguarda le date pianificate di inizio e fine è presente un flag nel tab date che indica se sono state modificate (manualmente dall’utente o dall’ottimizzatore).

## Area di validazione

In questo capitolo viene descritta l’area di validazione in cui l’utente può controllare gli OdL on condition generati dal DSS prima di inviarli alla fase di pianificazione (stato “Aperto”) ed effettuare il passaggio a “Non Eseguito”. A seconda del metodo di generazione dell’OdL è possibile definire le seguenti casistiche di gestione:

Gli OdL inseriti manualmente dall’utente e quelli da DSS periodici nascono direttamente in stato “Aperto”, di conseguenza non transitano dall’area di validazione.

Gli OdL on condition generati da DSS nascono in stato “Da Validare” e vengono mostrati nell’area di validazione dove non potranno permanere oltre il tempo massimo di validazione stabilito nel catalogo attività. Superato il tempo massimo di validazione passano in stato “Aperto” automaticamente.

In particolare, il processo di validazione nel caso di OdL on condition generati da DSS è strutturato come segue:

Figura 91 - Processo di validazione OdL

Dove:

Gli ordini on condition generati dal DSS nascono in stato «Da Validare».

Il passaggio allo stato “Aperto” può essere manuale tramite l’area di validazione oppure automatico quando viene superata la data calcolata tramite il campo “Massimo tempo di validazione”.

Il passaggio in “Non Validato” è sempre manuale, attraverso l’area di validazione l’utente sceglie se validare o non validare un ordine. In caso di non validazione, l’utente deve anche inserire una motivazione. Gli OdL “Non Validati” non potranno essere riattivati.

Attraverso l’area di validazione l’utente seleziona gli OdL da portare in “Non eseguito” (operazione possibile solo per gli OdL aggregatori e singoli in stato “Aperto”). Successivamente utilizza la funzionalità di cambio stato, imposta lo stato “Non eseguito” e popola il campo “motivazione” (reso obbligatorio una volta selezionato lo stato “Non Eseguito”). Infine, il sistema restituisce un ulteriore pop up di conferma con il messaggio “Si vuole confermare il passaggio in “Non eseguito?”. Gli OdL non eseguiti non potranno essere riattivati”. In caso di selezione multipla di ordini, la motivazione inserita è unica e vale per tutti gli ordini selezionati.

L’area di validazione è gestita nell’applicazione dedicata “Area di validazione” all’interno della quale l’utente può visualizzare la lista elenco (Figura 92) prefiltrata in modo da mostraretutti gli OdL in stato “Da Validare” con la possibilità di selezionarne 1 o N per eseguire il passaggio di stato ad “Aperto”.È possibile visualizzare la maschera di dettaglio di ogni OdL nella lista cliccando sul relativo record.

La vista elenco degli OdL da validare presenta i seguenti dati riassuntivi dell’OdL (tutti utilizzabili come filtro):

ID OdL;

Descrizione OdL;

Descrizione 1° livello: riporta la descrizione della linea o stazione contenente l'asset oggetto di OdL dell’elemento. In caso di doppia terna verrà restituita la linea prevalente (quella con sap-id minore)

Descrizione terzo livello relativo all’asset dell’ordine;

Chiuso tecnicamente si/no;

POS-NR (popolato solo per asset di linea);

Riferimento fase

Riferimento FdG

Riferimento conduttori

Riferimento Asset: cliccando sul record presente in questa colonna, l’utente viene direttamente indirizzato alla scheda tecnica dell’asset;

Classificazione Asset;

SAP ID;

ID Policy;

ID PdL: Cliccando sul record presente in questa colonna, l’utente viene direttamente indirizzato allo specifico PdL;

Data modello

Stato;

Programma.

Motivazione

Responsabile attività

Oltre ai filtri predisposti nella maschera, è possibile utilizzare la funzione di filtro avanzato con la quale è possibile applicare filtri su ogni attributo dell’OdL oltreché filtrare per “Data di creazione”. Oltre questi filtri sono rese disponibili le seguenti queries di filtraggio:

Query che restituisce gli ordini prossimi alla validazione automatica (sono restituiti gli ordini per i quali “data validazione automatica” – “data attuale” <= 5);

Query che restituisce gli ordini con asset appartenenti alla gerarchia della linea;

Query che restituisce gli ordini con asset appartenenti alla gerarchia della stazione;

Query che restituisce gli ordini che possono essere portati in stato Eseguito massivamente di linee;

Query che restituisce gli ordini che possono essere portati in stato Eseguito massivamente di stazione.

Query per filtrare tutti gli odl aggregatori e singoli in stato “Aperto” di linee

Query per filtrare tutti gli odl aggregatori e singoli in stato “Aperto” di stazioni

Figura 92 - Vista elenco degli OdL da validare

# Gestione OdL serbatoio

Generazione OdL serbatoio

La generazione degli OdL serbatoio è innescata da un trigger manuale che avvia il processo di generazione massiva per singola UdB. Il processo di generazione è composto dagli step rappresentati nella figura sotto.

Figura 93 - Processo di creazione degli OdL serbatoio

Verifica “conto base residuo pianificato”: L’utente verifica l’ammontare di conto base residuo pianificato (Capacity – Proprio pianificato – Ceduto pianificato con riferimento alle sole attività flaggate “PMI”) mensile dell’UdB nella colonna (cfr. 5.1.3) tramite il tab di “Monitoraggio capacity” dell’applicazione “Capacity”;

Inserimento delle ore da allocare sui serbatoi: Dopo aver verificato il conto base residuo (cfr. cap. 5.1.3) l’utente inserisce manualmente la quantità di ore da allocare sui serbatoi nel campo “Da allocare a serbatoio”. Il sistema permetterà l’inserimento di un valore minore, maggiore o uguale rispetto a quanto riportato nel “conto base residuo pianificato”

Figura 94 - Schermata di monitoraggio capacity

Figura 95 - Aggiornamento schermata di monitoraggio capacity

Apertura maschera di generazione serbatoi per UI: per creare la maschera di generazione degli OdL serbatoio, ciascuna UI deve indicare nell’applicazione “Gestione serbatoi” la combinazione UI – Anno tramite la funzione “Nuovo serbatoio” (figure successive).

Compilazione delle tabelle di ripartizione effort e fisico e creazione degli OdL serbatoio: L’inserimento delle ore da allocare è effettuabile sia tramite GUI web (inserendo puntualmente i valori nella maschera) che tramite Import da file excel. In entrambi i casi nelle tabelle di ripartizione dell’effort orario il salvataggio dei dati sarà consentito solo se la somma dei valori inseriti per ciascun mese e figura professione risulterà minore o uguale al valore indicato nella colonna “da allocare a serbatoio” (specifico per la combinazione figura professionale/mese) dell’applicazione Capacity. Nel caso di inserimento via excel sono richiesti i seguenti passaggi:

scaricare il template excel già popolato automaticamente con tutte le combinazioni presenti possibili di CoIn, società e figura professionale e compilarlo inserendo gli effort pianificati, utilizzando il campo “FISICO” per indicare se il valore inserito corrisponde a effort fisico (inserire S) o effort orario (inserire N) e cancellando tutte le combinazioni (righe nel file) che non si vuole generare. Nel caso di inserimento di effort orario è necessario popolare il campo “COMPETENZA” per indicare la figura professionale, mentre invece nel caso di effort fisico “COMPETENZA” non deve essere popolato (in caso di popolamento della competenza con effort fisico viene restituito un errore dal sistema che blocca l’inserimento della riga). Si specifica che, In fase di compilazione del file excel è anche possibile lasciare a 0 tutti gli effort e generare il serbatoio “vuoto” per poi pianificarlo in un secondo momento tramite MXLoader.

Una volta compilato il template excel, è possibile caricarlo nella maschera web tramite funzionalità di import dedicata.

Dopo il caricamento del file utilizzare il pulsante “Genera serbatoi” per completare l’acquisizione del file e generare i serbatoi per le combinazioni contenute.

Nel caso in cui sia necessario generare ulteriori OdL è possibile ripetere il processo tramite file excel (che sarà sempre scaricato con tutte le combinazioni) eliminando tutte le combinazioni tranne quelle per le quali si vuole effettuare la generazione. Nel caso in cui nel file vengano inserite combinazioni per le quali è già stato generato un OdL serbatoio, queste verranno ignorate nel processo di generazione (il processo di modifica di OdL esistenti viene gestito tramite MXLoader dedicati descritti nel paragrafo successivo). Si riporta di seguito un esempio del template per la generazione:

Nel caso di inserimento tramite GUI, è necessario inserire manualmente i dati nelle seguenti tabelle:

Tabelle di ripartizione effort: Le tabelle sono popolate manualmente dall’utente dell’UdB che inserisce le Coin ammissibili ed i relativi carichi, divisi per figura professionale, singola società e mese. Come riportato in Figura 96, è possibile inserire un elenco di CoIn. Le colonne inerenti alle competenze e al codice COIN sono ordinabili dall'utente mentre la colonna "società" e le colonne dei mesi risultano non ordinabili.

Figura 96 - Vista della tabella di Ripartizione Effort

Tabelle di ripartizione del fisico: All’interno della stessa schermata contenente le tabelle di ripartizione dell’effort è presente la tabella di ripartizione del fisico (Figura 97) popolata con le seguenti logiche:

Vengono riportate in automatico le stesse combinazioni CoIn-società inserite nelle tabelle di ripartizione dell’effort

L’utente inserisce la singola società del fisico mensile (Terna o Rete)

L’utente inserisce il fisico da allocare su ogni mese

Il sistema riporta nella colonna “Totale” la somma per CoIn dei valori inseriti

Si specifica che l’unità di misura viene ereditata automaticamente dal PdL (dedicato al Serbatoio) associato alla CoIn (relazione 1:1)

Figura 97 - Vista della tabella di ripartizione del fisico

In particolare, verranno generati N OdL serbatoi secondo la seguente formula: #Combinazioni univoche Coin-Società*12(pertanto ogni OdL serbatoio sarà relativo a un solo mese). A ciascun OdL saranno associate le quantità di ore operaio/impiegato/quadro e di fisico, definiti nelle tabelle sopra descritte. La generazione massiva degli OdL serbatoio viene innescata dal pulsante “Generazione OdL” solo se risultano valorizzate tutte le tabelle presenti in schermata (tabella di ripartizione effort e tabella di ripartizione fisico). Le CoIn dedicate alla generazione degli OdL serbatoio sono associate a dei PdL creati appositamente per i serbatoi. Per ogni CoIn sarà presente uno e un solo PdL (rapporto 1:1). Si specifica che i pdl serbatoio, in quanto strumentali alla sola creazione degli odl serbatoio, non saranno oggetti visibili/gestibili dall’utente. Per ogni pdl serbatoio viene definità una coin e un’unità di misura di riferimento. Inoltre l’odl serbatoio verrà generato con il flag PMI=true.(flag non visibile a GUI per i serbatoi).

Figura 98 - Elenco di OdL Serbatoio generati automaticamente

Pianificazione di Risorse esterne agli OdL serbatoi

Nella maschera “Gestione Serbatoi”, all’interno del tab “OdL Serbatoio” riporta gli OdL recentemente generati, è presente una tabella (Figura 99) contenente le seguenti informazioni suddivise in colonne:

Codice OdL

Codice Coin

Società (terna o rete)

Data di inizio pianificata (es. 01/01/2025)

L’utente ha la possibilità di esportare le informazioni presenti in questa tabella in formato Excel, tramite pulsante dedicato (pulsante di download evidenziato). Successivamente, i record esportati possono essere caricati su l’MXLoader.

Figura 99 - Tab "OdL serbatoio" dell'applicazione "Gestione serbatoio"

I dati relativi agli OdL sui quali si desidera pianificare risorse esterne vengono importati nell’MXLoader tramite l’esecuzione di una query. In particolare, l’utente deve eseguire i seguenti passaggi sulla tabella rappresentata in Figura 100:

Impostare il campo “C1” su “Query”

Inserire nella cella “D1” dell’Excel la query (in formato SLC) desiderata. Ad esempio, per ottenere gli OdL serbatoio associati a una specifica UdB (ad es. UdB DTNRAT), è necessario inserire la seguente query: X4SERBATOIO=1 and OWNERGROUP=”DTNRAT”. Qualora l’utente desideri applicare filtri aggiuntivi sugli OdL da visualizzare, può farlo aggiungendo ulteriori condizioni alla query.

Eseguire il Run: Selezionando il pulsante “Run” evidenziato in Figura 100, l’MXLoader popolerà con i dati relativi agli OdL che soddisfano i criteri della query le seguenti colonne:

SITEID (costante popolata con il valore SIT01);

WONUM (Codice OdL);

X4COIN (Codice CoIn);

X4BURKS (Società);

Pianificare le risorse esterne: Una volta che gli OdL serbatoio selezionati tramite la query sono disponibili, l’utente può aggiungere le informazioni sulle risorse esterne nei seguenti campi:

WPSERVICE.ITEMNUM: identifica l’id della risorsa esterna da pianificare; Questo campo viene valorizzato selezionando un record da un elenco predefinito di dominio in Excel;

WPSERVICE.ITEMCOST: indica il costo della risorsa esterna assegnata all’OdL serbatoio;

Impostare il campo C1 in “Change”: Dopo aver apportato le modifiche necessarie, l’utente deve impostare il campo C1 in “Change” ed eseguire nuovamente il “Run”. In questo modo, le modifiche effettuate nell’MXLoader saranno trasmesse agli OdL su EAM.

Figura 100 - Esempio di pianificazione di risorse esterne tramite MXLoader

Una volta inserite nel MXLoader le informazioni relative alle risorse esterne appena descritte, queste vengono riportate sulla tabella “Esito caricamento risorse esterne” evidenziata nella Figura 101 sottostanti.

Figura 101 - Tab "OdL serbatoio" dell'applicazione "Gestione serbatoio" con tabella "Esito caricamento risorse esterne" valorizzato

In generale, la gestione degli OdL Serbatoio segue un processo di gestione semplificato rispetto a quello degli OdL generati manualmente o dal DSS. Di seguito sono elencate le caratteristiche peculiari degli OdL serbatoio:

Gli stati associabili saranno solo “Bozza”, “Aperto” e “Chiuso”.

Di default, le date di pianificazione vengono settate con il primo e l’ultimo giorno del mese al quale è associato l’OdL serbatoio;

Non essendo associato ad un asset, il dato fisico del serbatoio viene definito nella tabella di ripartizione del fisico e riportato automaticamente nel tab “Ordine di lavoro” (come mostrato nella Figura 102).

La tabella “Labor” (Figura 103) del tab “Piani” sarà pre-popolata alla generazione dell’OdL serbatoio con:

Qualifica: figura professionale associata alla risorsa (Operaio, Quadro, Impiegato);

Quantità: numero delle risorse impiegate con la medesima qualifica (ad. Es. numero degli Operai impiegati);

Ore esecuzione: ore effettive di lavoro sul campo;

Titris: titolare risorsa che esegue l’attività;

CMO: costo medio orario della risorsa;

Data inizio risorsa: rappresenta la data in cui si pianifica di iniziare ad utilizzare la risorsa;

Data fine risorsa: rappresenta la data in cui si pianifica il termine di utilizzo della risorsa;

Figura 102 - Tab "Ordine di lavoro” e inserimento del dato fisico

Figura 103 - Tabella "Labor"

Gestione OdL serbatoio

L’OdL serbatoio può assumere tre stati:

Bozza;

Aperto;

Chiuso.

Alla generazione dell’OdL serbatoio il sistema imposta automaticamente lo stato “Bozza”. In questo stato è possibile modificare la capacity del personale, il fisico e le RE allocate al serbatoio (i totali di capacity allocate nel serbatoio sono visualizzabili nel tab “Date” dell’OdL). La modifica è effettuabile massivamente tramite caricamento con MX Loader. Per eseguire una modifica, l’utente deve innanzitutto scaricare le informazioni relative agli OdL serbatoio seguendo una procedura simile a quella descritta nel paragrafo 7.1.1.

I dati degli OdL su cui si intende intervenire vengono riportati nell’MXLoader tramite l’esecuzione di una query. Nello specifico, l’utente deve eseguire i seguenti passaggi sulla tabella mostrata in Figura 105:

Impostare il campo “C1” in “Query”

Inserire nella cella “D1” dell’Excel la query (in formato SLC) desiderata. Ad esempio, per selezionare gli OdL serbatoio associati a una specifica UdB, è necessario utilizzare la seguente query: X4SERBATOIO=1 and OWNERGROUP=”DTNRAT”. Qualora l’utente desideri applicare ulteriori filtri sugli OdL da visualizzare, può farlo aggiungendo delle condizioni sulla query.

Eseguire il Run: selezionando il tasto evidenziato in Figura 105, l’MXLoader popola con i dati relativi agli OdL che soddisfano i criteri della query le seguenti colonne:

SITEID (costante popolata con il valore SIT01);

WONUM (Codice OdL);

X4COIN (Codice CoIn);

X4FISICOPLAN (Fisico pianificato);

WPLAB.CRAFT (Figura professionale);

WPLABOR.LABORHOURS (Durata);

Effettuare le modifiche: Una volta ottenuti gli OdL serbatoio selezionati tramite la query, l’utente può procedere a modificare qualsiasi campo che desideri.

Impostare il campo C1 in “Change” e confermare le modifiche: Dopo aver apportato le modifiche necessarie, l’utente imposta il campo C1 su “Change” ed esegue il “Run”. In questo modo, le modifiche effettuate nell’MXLoader saranno trasferite agli OdL nel sistema EAM.

Figura 105 - Rappresentazione della modifica massiva tramite MXLoader

è da sottolineare che la modifica del totale capacity nell’OdL serbatoio fa riferimento alla “capacity totale” visualizzata nell’OdL serbatoio e non al “da allocare a serbatoio” nell’applicazione monitoraggio capacity, che rimane invariato. In fase di modifica - sia da mxloader che da gui - dei serbatoi precedentemente, creati sarà possibile inserire un valore minore, maggiore o uguale rispetto a quanto riportato nel “conto base residuo pianificato”.

Gli OdL serbatoio, passano automaticamente in stato “Aperto” quando si entra nell’anno solare (1° gennaio) in cui sono stati pianificati. Anche in questo stato rimane disponibile la funzionalità di modifica massiva tramite MXLoader anche durante l’anno N nello stato “Aperto”. Tutti gli OdL serbatoio pianificati all’anno N vengono portati in stato “Chiuso” automaticamente dal sistema una volta raggiunta la fine dell’anno (31 Dicembre) Lo stato “Chiuso” può essere settato manualmente dall’utente in qualunque momento.

# Creazione e gestione OdL manuale

Gestione OdL manuali

In EAM può essere necessaria la creazione manuale degli Ordini di lavoro, che pertanto non vengono generati automaticamente attraverso le logiche del DSS. Questa sezione è dedicata non solo all’analisi dettagliata del processo di creazione di OdL manuali in EAM, illustrando le procedure operative e la loro generazione all’interno del sistema, ma anche alla caratterizzazione specifica degli stessi.

#### Creazione OdL manuali

Per creare un OdL manuale, l’utente deve accedere all’applicazione “Monitoraggio OdL”. Selezionando il tasto “+” evidenziato in figura, viene indirizzato alla schermata dedicata alla compilazione dei dati relativi all’ordine. In alternativa, è possibile selezionare dal menu a sinistra l’opzione “Nuovo OdL”, per avviare il processo di generazione manuale di un OdL.

Figura 106 - Monitoraggio OdL

Una volta avviata la procedura di creazione, l’utente viene reindirizzato alla maschera dell’odl. Per procedere con il processo di creazione dell’odl, l’utente deve associare l’OdL a un PdL tramite il campo “Identificativo PdL”. Cliccando sulla lente adiacente, il sistema apre una dialog contenente l’elenco di PdL (Id PdL e descrizione) associabili a OdL manuali. I PdL associabili agli OdL Manuali sono definiti nel catalogo delle attività con il flag “Attività caricabile manualmente”.

Successivamente l’utente deve associare manualmente l’OdL all’asset su cui verrà eseguita l’attività. Selezionando la lente accanto al campo “Asset”, il sistema apre una dialog (figura 108) che consente all’utente di selezionare l’asset oggetto dell’OdL. Nella dialog sono presenti i seguenti campi: Asset; descrizione; UI; DT; Descrizione asset primo livello; SAP ID asset; POS-NR (solo per asset di secondo livello linea); Descrizione asset terzo livello (popolato solo per asset al di sotto di asset di terzo livello); Riferimento conduttori; Riferimento FdG; Riferimento fase.

La dialog di selezione dell’asset vengono mostrati solo gli asset che fanno riferimento alla UI (UI indicata nel campo UI dell’asset) dell’utente che sta creando l’ordine. Esclusivamente per il caso di selezione di una “Linea” (1° livello) multi-competenza, l’asset risulterà selezionabile da tutte le UI che presentano una responsabilità per un qualsiasi tratto della linea, definito da sostegno a sostegno (la responsabilità UI multi-competenza è rappresentata nella tabella di riepilogo dati nell’anagrafica asset). Una volta selezionato l’asset, sarà popolato automaticamente il campo “responsabile asset” con l’UI dell’utente che ha creato l’ordine; Solamente nel caso di ordini associati ad asset di Linea multi-competenza sarà possibile modificare il “responsabile asset” (esclusivamente in stato aperto e da validare) selezionando una delle UI associate nella tabella di riepilogo dati dell’asset, non è possibile lasciarlo blank.

Figura 107 - OdL Tracking

Figura 108 - Dialog selezione asset

Dopo aver selezionato l’asset e il PdL associati all’OdL Manuale l’utente deve valorizzare tutti campi obbligatori prima di procedere al salvataggio dell’OdL tramite l’azione “Salva OdL”, disponibile nel menu a sinistra della vista (Figura 107). Nel paragrafo successivo viene dettagliato l’elenco di attributi associati a un OdL manuale con evidenza della distinzione fra campi obbligatori e campi facoltativi.

#### Caratterizzazione OdL manuali

L’attivazione della funzione di creazione manuale dell’OdL innesca l’applicazione di logiche di popolamento automatico per determinati campi dell’ordine. Per generare questo tipo di OdL, tuttavia, è necessario garantire che il set minimo di campi obbligatori sia popolato. I campi che definiscono un l’OdL manuale, distribuiti sui diversi tab dell’applicazione “Monitoraggio OdL” sono riportate in Tabella 10, strutturata in base alle seguenti colonne:

Campo: Nome del campo;

Descrizione: Descrizione del campo;

Obbligatorio o facoltativo: Indica se il campo è obbligatorio o facoltativo per la creazione;

Modalità di valorizzazione: Indica se il campo viene popolato automaticamente o manualmente;

Logica di valorizzazione: Descrive la logica di popolamento utilizzata dal sistema per popolare il campo in caso di valorizzazione automatica oppure la procedura seguita dall’utente nel caso di valorizzazione manuale;

| Campo | Descrizione | Obbligatorio o facoltativo | Modalità di valorizzazione | Logica di valorizzazione |
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
| Responsabile Asset | Responsabile censito nelle sedi tecniche in anagrafica EAM | Obbligatorio | Automatica | Recuperato dall’anagrafica in base all’asset selezionato, modificabile fino solo in stato Aperto/Da validare per linee multi-competenza. Non è possibile lasciarlo blank. |
| Codice CoIn | Codice identificativo della contabilità industriale | Obbligatorio | Automatica | Valorizzato automaticamente sulla base del catalogo standard, tipo spesa e PdL selezionato |
| Tipo spesa | Campo che indica la tipologia di attività CoIn associata all’OdL. Può assumere i valori “Opex”; “Capex” o “ANR” | Obbligatorio | Automatico | Valorizzato automaticamente con il Tipo spesa prevalente |
| Attività standard | ID attività standard | Obbligatorio | Automatica | Valorizzato in base al PdL associato all’OdL |
| Titris | Campo indicativo del titolare della risorsa che esegue l’attività | Obbligatorio | Automatico | Corrispondente al responsabile attività |
| Durata pianificata | Durata pianificata per l’esecuzione dell’OdL | Obbligatorio | Automatico | Corrispondente alla massima durata tra quelle associate ad ogni singola risorsa. La durata pianificata viene ereditata dal catalogo delle attività in base al PdL associato all’OdL. Nel caso non sia specificata la durata all’interno del catalogo il campo viene lasciato vuoto alla creazione dell’ordine e deve essere successivamente valorizzato manualmente dall’utente. |
| Ore esecuzione | Periodo (in ore) di esecuzione dell’attività, per risorsa. | Obbligatorio (nel caso in cui ci siano dei record) | Automatico | Valore reperito dal catalogo delle attività Tuttavia, qualora sia necessario aggiungere manualmente delle risorse, il campo non deve essere valorizzato automaticamente. |
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
| OdL per piano (PMI) | Flag che indica l’appartenenza dell’OdL al PMI | Obbligatorio | Automatico | Per gli ordini manuali e da DSS valorizzato di default a “NO” dal sistema, modificabile liberamente dall’utente in ogni stato dell’OdL. Per gli ordini serbatoio valorizzato di default a SI e non visibile a GUI |

Tabella 10 - Campi OdL manuale

#### Stati OdL manuali

A livello di flusso di processo, gli OdL creati manualmente seguiranno una gestione analoga a quella prevista per gli OdL generati automaticamente dal DSS, con l’eccezione del processo di validazione (eseguibile solo per gli OdL da DSS on condition), che non è applicabile agli OdL manuali. Quindi gli stati assegnabili agli OdL manuali durante il loro ciclo di vita coincidono con quelli definiti per gli OdL generati dal DSS, ad eccezione dello stato “Da validare”, rispettando le logiche specifiche sia del caso UI che del caso non UI / misto.

Inoltre, per gli OdL manuali non viene applicata la logica di cambio di stato automatico in “Non eseguito” al superamento della data di massimo posticipo. In caso di superamento di tale data, l’utente può modificare manualmente lo stato dell’OdL in “Non eseguito”, con conseguente registrazione da parte del sistema della matricola dell’utente che ha eseguito l’operazione. La modifica in stato “Non eseguito” è comunque consentita anche prima del superamento della data di massimo posticipo

A differenza degli OdL generati dal DSS, gli OdL manuali non richiedono la valorizzazione obbligatoria del campo “data modello” per effettuare avanzamenti di stato.

#### Creazione massiva OdL manuali

Oltre alla creazione singola di OdL manuali è possibile procedere anche con la creazione massiva.

Quest’ultima è eseguibile in due modalità:

Tramite MxLoader: In questo caso l’utente compila l’MxLoader con tutti i dati necessari. Il file sarà formato da una serie di colonne all’interno delle quali è necessario inserire almeno i dati obbligatori (Asset, PdL ecc..) riportati nel paragrafo 8.1.1.2. Una volta compilate tutte le informazioni necessarie si procede con il caricamento del file, a valle del quale in Maximo saranno creati tanti OdL quanti sono i record compilati nell’MxLoader.

Tramite funzionalità di creazione massiva: in questo caso l’utente ha la possibilità di creare OdL massivamente tramite la funzionalità disponibile nella sidebar, mostrata in Figura 109.

Figura 109 - Funzionalità di creazione massiva OdL

Non appena si clicca sulla funzionalità appare una dialog con le informazioni di seguito riportate:

Sede tecnica di primo livello: campo obbligatorio popolabile manualmente dall’utente tramite una lista valori contenente le sedi tecniche di primo livello. Questo campo è necessario in quanto rappresenta un filtro per la selezione degli asset per i quali si intende creare ordini di lavoro;

Classificazione Asset: campo obbligatorio popolabile manualmente dall’utente tramite una lista valori contenente tutte le classificazioni asset presenti a sistema. Questo campo è necessario in quanto rappresenta un filtro per la selezione degli asset per i quali si intende creare ordini di lavoro;

Identificativo PdL: campo obbligatorio popolabile manualmente dall’utente tramite una lista valori contenente tutti i PdL (con flag “Attività caricabile manualmente” valorizzato con “SI”) associati alla classificazione asset precedentemente selezionata;

Data modello: campo obbligatorio popolabile manualmente solamente dall’utente centrale;

Data pianificata fine: se precedentemente popolata la data modello, allora questo campo viene popolato automaticamente con la data inserita nella data modello. Se non è stata precedentemente inserita la data modello allora è obbligatoria la valorizzazione di questo campo.

Data pianificata inizio: Campo obbligatorio popolato manualmente;

Assistenza / sorveglianza: campo obbligatorio e visibile esclusivamente se il campo “Outsourcing?” è popolato con “Si”. Se visibile è popolato manualmente dall’utente selezionando uno fra i seguenti valori: “Assistenza”, “Sorveglianza”;

Outsourcing?: campo obbligatorio, ereditato automaticamente dal PdL selezionato e modificabile dall’utente selezionando uno tra i seguenti valori: “IN” e “OUT”;

Data di massimo posticipo attività: campo obbligatorio, data massimo entro la quale è necessario svolgere l’OdL (è presente un controllo che verifica che la data di massimo posticipo sia >= della data di fine pianificata). Si specifica che per gli OdL manuali, a differenza degli OdL creati da DSS, è inibito il passaggio a “Non eseguito” al superamento della data di massimo posticipo;

Una volta inserite le informazioni sopra elencate, verrà restituita una lista filtrabile di asset che rientrano nel perimetro della sede tecnica di primo livello inserita e con classificazione uguale a quella precedentemente definita (ES: si inserisce come sede tecnica di primo livello una specifica linea e come classificazione asset “Campata”. In questo caso verrà restituito un elenco contenente tutte le campate sottostanti alla sede tecnica di primo livello inserita). A questo punto l’utente potrà selezionare tutti gli asset della lista tramite un “Select all” oppure solo gli asset per i quali si ritiene necessario creare OdL. Nella lista di asset saranno presenti le seguenti informazioni:

Codice Asset: Codice univoco dell’asset in Maximo;

Descrizione Asset;

SAP-ID: codice restituito per gli asset che possiedono un SAP-ID in anagrafica. Nel caso in cui l’asset non abbia un SAP-ID, il codice restituito sarà quello di livello immediatamente superiore a quello associato;

Stato Asset: tramite questo campo l’utente avrà la possibilità di visualizzare in che stato si trovano gli asset.

Figura 110 - Dialog di creazione massiva OdL

Si specifica che gli OdL creati massivamente seguiranno lo stesso processo di quelli creati singolarmente e quindi saranno generati in stato “Aperto”.

#### Attività di pronto intervento

In EAM è prevista la gestione di OdL associati a PdL di tipologia “pronto intervento”. Tali OdL vengono generati manualmente dall’utente e gestiti a consuntivo successivamente all’esecuzione. Gli OdL di Pronto intervento rappresentano quindi attività straordinarie non pianificate, che non vengono create dal DSS né considerate nel processo di pianificazione tramite MPE.

Data la natura di attività straordinarie che devono gestire, gli OdL di pronto intervento prevedono un processo di lavorazione semplificato disponibile a partire da App mobile e proseguito su Web che consente all’operatore di segnalare la presenza di difformità direttamente in campo tramite una Richiesta di Servizio e successivamente di inserire l’OdL di pronto intervento via Web a valle dell’esecuzione dell’attività; In questo modo, l’utente può eseguire l’attività di riparazione necessaria e contestualmente tracciarne le difformità. Successivamente può inserire a sistema via Web un OdL di pronto intervento, completandone gli attributi e consuntivando le ore lavorate. Di seguito viene riportato uno schema riepilogativo dei principali step di processo relativi al pronto intervento:

Figura 111 - Schema rappresentativo della gestione delle attività di pronto intervento

Il ciclo di vita delle attività di pronto intervento si articola nei seguenti step:

Creazione Richiesta di Servizio: Contestualmente all’esecuzione dell’attività di pronto intervento, l’utente crea una Richiesta di Servizio accedendo alla funzionalità “Richiesta di Servizio” disponibile in Maximo Mobile. La richiesta di servizio presenta un set di dati ridotto costituito dai seguenti campi:

Classificazione

Tipologia difformità

Note

L’utente seleziona la classificazione dell’asset a cui va attribuita la richiesta e successivamente seleziona la tipologia difformità scegliendo da una lista valori pre-filtrata in base alla selezione della classificazione. Nella selezione della classificazione viene reso disponibile un filtro di testo, che consente di ricercare un qualsiasi asset nella classificazione.

L’utente può valorizzare il campo note con eventuali particolarità relative all’attività eseguita o alla difformità rilevata. Al salvataggio la RdS viene sincronizzata col web.

Ricezione richiesta di servizio via Web: La richiesta di servizio creata sul mobile viene sincronizzata in EAM Web e può essere visualizzata nella sezione dedicata in stato “Aperto”. L’utente sul Web provvede a generare un OdL di pronto intervento ed a registrarvi le difformità segnalate tramite la RdS associandole all’asset specifico nelle tabelle dedicate all’inserimento delle difformità. L’OdL (di tipologia “Manuale”) viene inizialmente generato nello stato “Aperto” e non prevede il processo di validazione previsto per gli OdL generati automaticamente dal DSS. Durante la fase di creazione, è necessario compilare i campi descritti nella Tabella 10 e associare l’OdL a un PdL di pronto intervento. Le difformità vengono registrate all’interno dell’OdL e se risolte contestualmente al pronto intervento, l’utente imposta manualmente lo stato “Risolto” nell’area di validazione difformità (c.f.r. AF3). Nel catalogo delle attività è definito un PdL di tipologia pronto intervento associato all’asset di primo livello “Linea” e un PdL associato all’asset di primo livello “Stazione”, definiti in modo da poterli associare a tutti gli asset sottostanti grazie alla tabella “Classificazione Asset” (la struttura del PdL di pronto intervento è descritta nel paragrafo 8.1.1.5.1). L’utente inserisce le ore lavorate nel tab di pianificazione risorse dell’OdL e dopo aver compilato tutti i campi obbligatori è possibile procedere alla fase di consuntivazione avanzando lo stato dell’OdL in “Schedulabile”

Consuntivazione ore attività: è possibile registrare le ore consuntivate (eseguita in EAM per Titris non UI e in Geocall per Titris UI) e successivamente inviarle a SAP. Si specifica che alla ricezione del primo consuntivo da Geocall (oppure all’inserimento del primo consuntivo nel caso non UI) l’odl passa in stato ESEGUITO PARZIALMENTE (dallo SCHEDULABILE); in questo stato è possibile inserire le difformità sull’ordine. Infine, l’odl passa in ESEGUITO seguendo le logiche UI/NON UI descritte rispettivamente nei par. 12.1.1.2 e 12.1.2.

Chiusura attività: L’odl passa automaticamente in CHIUSO seguendo le stesse logiche descritte in par. 12.1.1.2 e 12.1.2.

Chiusura Richiesta di Servizio: l’utente chiude manualmente la RdS (effettuato tramite passaggio di stato a “Chiusa”), con possibilità di visualizzare lo storico di RdS chiuse in precedenza.

##### Caratterizzazione PdL di pronto intervento

Nel presente paragrafo viene mostrata la valorizzazione dei PdL di pronto intervento all’interno del catalogo delle attività di EAM. Di seguito viene descritta la valorizzazione dei campi relativi al PdL generici di pronto intervento relativi a “Linee” o “Stazione” (Figure 112, 113, 114). In particolare, la seguente valorizzazione consente di utilizzare i due PdL di pronto intervento generici (Linea/Stazione) in qualsiasi OdL di pronto intervento associato ad un qualsiasi asset al di sotto di una Linea / Stazione. Si specifica che data la natura semplificata del processo che i PdL di pronto intervento devono rappresentare, vengono valorizzati solamente i campi obbligatori per la definizione del PdL:

ID Attività PdL: Codice identificativo del PdL.

Descrizione Attività PdL: Pronto intervento linea / Pronto intervento stazione

Tipologia Attività: Campo popolato con “Pronto intervento”. Identifica la tipologia di PdL;

Elemento anagrafico associato: Indica l’asset di primo livello al quale si riferisce il PdL (Linea o Stazione);

Attività caricabile manualmente: Flag che identifica che il PdL è associabile a OdL di tipo manuali. Il Flag è attivo per PdL di pronto intervento

Tabella Classificazione asset: Tabella contenente le classificazioni alle quali è associato il PdL. La tabella è popolata in base all’asset inserito nel campo “Elemento anagrafico associato”. In questo caso, il rapporto Elemento - Classificazione è 1: n (all’elemento linea corrispondono tutte le classificazioni della linea, così come per l’elemento stazione). Tramite la valorizzazione della tabella indicata è possibile associare i PdL di pronto intervento a qualsiasi asset al di sotto di una linea / stazione negli OdL di pronto intervento;

Durata pianificata: Campo pre-popolato con “24 ore”. Indica che l’attività di pronto intervento deve essere completata al massimo nel giro di un giorno.

ID Standard:

ID Macro-attività:

Figura 112 - Tab "Attività" del piano di lavoro di pronto intervento

Figura 113 - Tab "Dettaglio attività" del piano di lavoro di pronto intervento

Figura 114 - Tab "Date ed effort" del piano di lavoro di pronto intervento

Modalità di accorpamento e logiche di gestione OdL aggregatori

L’OdL aggregatore ha lo stesso corredo informativo descritto nel par. 6.1.2; a differenza delle altre tipologie di OdL avrà il flag “OdL aggregatore” attivato.

#### Creazione OdL aggregatore

L’utente può avere la necessità di accorpare vari OdL in un unico OdL “aggregatore”. Per fare ciò il primo step è creare manualmente un OdL aggregatore, all’interno del quale vengono inseriti i vari OdL. Si riportano di seguito gli step necessari alla creazione dell’OdL aggregatore e all’associazione degli OdL:

L’utente, alla creazione di un OdL aggregatore, deve attivare il flag «OdL aggregatore?» sulla pagina dell’OdL.

Informazioni obbligatorie da associare all’OdL aggregatore:

PdL: associazione manuale. Solo per gli ordini aggregatori è possibile inserire anche dei pdl che, nel relativo catalogo attività, risultano non caricabili manualmente

COIN: associazione manuale;

Programma: ereditato automaticamente dal PdL associato;

Assistenza/sorveglianza: associazione manuale;

Asset (classificazione) di primo livello o secondo livello (per linee solo primo): ereditato automaticamente dal PdL associato. Nella dialog di selezione dell’asset vengono mostrati solo gli asset che fanno riferimento alla UI (UI indicata nel campo UI dell’asset) dell’utente che sta creando l’ordine. Esclusivamente per il caso di selezione di una “Linea” (1° livello) multi-competenza, l’asset risulterà selezionabile da tutte le UI che presentano una responsabilità per un qualsiasi tratto della linea, definito da sostegno a sostegno (la responsabilità UI multi-competenza è rappresentata nella tabella di riepilogo dati nell’anagrafica asset). Una volta selezionato l’asset, sarà popolato automaticamente il campo “responsabile asset” con l’UI dell’utente che ha creato l’ordine; Solamente nel caso di Linea multi-competenza sarà possibile modificare il “responsabile asset” selezionando una delle UI associate nella tabella di riepilogo dati dell’asset (non è possibile lasciarlo blank) fintanto che non sono stati associati OdL figli all’aggregatore. Dopo l’associazione di un OdL figlio il campo responsabile asset non sarà più modificabile (per modificare il responsabile asset di un aggregatore con figli associati sarà necessario prima disassociarli e poi proseguire con la modifica);

Dopo la creazione dell’OdL aggregatore si associano gli OdL sottostanti attraverso la funzionalità di “Relaziona ordini”.

La lista di OdL associabili all’OdL aggregatore è prefiltrata secondo i seguenti criteri:

Stesso PdL dell’OdL aggregatore

Stesso programma dell’OdL aggregatore;

Stesso asset di primo o secondo livello dell’OdL aggregatore.

Stesso “responsabile asset” dell’OdL aggregatore

Se il campo COIN degli OdL aggregati non corrisponde a quello dell’OdL aggregatore, al salvataggio appare un warning per avvisare l’utente che sarà modificata la COIN degli aggregati al fine di allinearla con quella dell’OdL aggregatore.

Se l’attributo assistenza/sorveglianza degli OdL aggregati non corrisponde a quello dell’OdL aggregatore, al salvataggio appare un warning per avvisare l’utente che sarà modificato l’attributo degli aggregati al fine di allinearlo con quello dell’OdL aggregatore.

Per ogni OdL aggregato è possibile specificare un OdL propedeutico (uno degli altri OdL aggregati) attraverso il campo “Predecessore”. In questo modo la data di inizio dell’OdL per cui si è specificato il predecessore sarà posizionata successivamente alla data di fine del predecessore indicato.

#### Logiche di gestione

Inizializzazione OdL aggregatore

Le date di pianificazione dell’OdL aggregatore sono definite sulla base dei vincoli riportati dagli OdL sottostanti e sono calcolate dal sistema al momento del salvataggio;

La durata dell’OdL aggregatore è pari alla massima durata degli OdL sottostanti;

La data fine pianificata corrisponde con l’ultima data fine pianificata tra quelle degli OdL sottostanti;

L’OdL aggregatore eredita la data di massimo posticipo più stringente tra quelle degli OdL sottostanti;

L’effort dell’OdL aggregatore è calcolato come somma degli OdL sottostanti.

#### Creazione e gestione date OdL aggregatore

In EAM è possibile associare diversi OdL in un singolo OdL padre, definito OdL aggregatore. All’interno dell’OdL aggregatore possono essere aggregati esclusivamente OdL associati allo stesso PdL e riconducibili al medesimo asset di primo livello. Una volta creato l’OdL aggregatore secondo le logiche descritte in questo capitolo, MPE non prenderà più in considerazione i singoli OdL sottostanti, ma considererà la durata, date di inizio e fine pianificate dell’OdL aggregatore.

Si specifica che la creazione dell’OdL aggregatore, oltre che manuale, potrà essere anche automatizzata e quindi da DSS (ad esempio per la gestione dei tronchi di ispezione). La generazione di OdL aggregatore in modo automatico è affrontata nell’ambito dello sprint 4.

Questa sezione esamina dettagliatamente le logiche di creazione manuale e gestione degli OdL aggregatori nel sistema EAM.

##### Creazione manuale dell’OdL aggregatore

Il processo di aggregazione manuale degli OdL inizia con la creazione dell’OdL aggregatore nell’applicazione “Monitoraggio OdL”. L’utente può aggregare manualmente gli OdL all’interno dell’OdL appena creato se soddisfatte le seguenti condizioni:

Flag “OdL Aggregatore” attivato: deve essere selezionato il flag che identifica l’OdL come di tipologia “Aggregatore” (Figura 115).

Dati per aggregazione: questo set di dati definisce le condizioni di associazione e delimita l’insieme di OdL che possono essere aggregati sotto l’OdL principale. In particolare, l’OdL aggregatore deve avere valorizzati almeno i seguenti dati:

Identificativo PdL: codice univoco che identifica il PdL associato all’aggregatore. Questo campo è presente nel tab “Work order”. Tutti gli OdL aggregati dovranno avere lo stesso PdL inserito per l’aggregatore;

Asset: asset di primo/secondo livello associato all’aggregatore. Questo campo è presente nel tab “Work order”. Gli asset presenti negli OdL aggregati dovranno essere figli dell’asset di primo livello definito sull’aggregatore;

Dati minimi OdL aggregatore: oltre ai dati elencati al punto precedente, l’OdL aggregatore deve possedere anche ii campi “tipo spesa”,“codice COIN”, assistenza/sorveglianza” (se flag “Outsourcing?” risulta valorizzato).

Una volta soddisfatte le condizioni sopra elencate, l’utente può procedere con la selezione degli OdL da associare all’OdL aggregatore utilizzando l’azione “Relazione ordini”, come illustrato in Figura 115.

Figura 115 - Tab "work order" dell’OdL aggregatore

Selezionando l’azione “Relaziona Ordini”, il sistema apre una dialog contenente i possibili OdL da aggregare (Figura 116). In questa finestra l’utente seleziona gli OdL ammissibili da un elenco predefinito. Il sistema permette di aggregare solo gli OdL compatibili con l’OdL aggregatore appena creato, basandosi sul set minimo di dati forniti in precedenza.

Figura 116 - Dialog di selezione degli OdL da aggregare

Dopo aver selezionato gli OdL da aggregare , gli OdL aggiunti vengono relazionati e riportati nella tabella rappresentata in Figura 117, presente nel tab “Pianificazione”. In questa tabella sono presenti le seguenti informazioni relative agli OdL figli:

ID OdL: codice univoco identificativo dell’OdL associato all’aggregatore;

Descrizione: descrizione dell’OdL associato all’aggregatore;

Descrizione asset: descrizione dell’asset associato all’OdL;

Predecessori: In questo campo, se necessario, l’utente inserisce l’OdL predecessore di uno specifico OdL. In questo modo l’OdL per cui è stata definita una propedeuticità potrà essere pianificato solo a valle del suo predecessore (data pianificata inizio OdL >= data fine pianificata OdL propedeutico). Si precisa che la definizione della propedeuticità non è sempre necessaria: ad esempio per la gestione dei tronchi (OdL aggregatore creato automaticamente) e del taglio piante (OdL aggregatore creato manualmente) non è presente una propedeuticità da dover rispettare.

Data inizio pianificata: data inizio pianificata dell’OdL associato all’aggregatore;

Data fine pianificata: data fine pianificata dell’OdL associato all’aggregatore;

Stato: stato dell’OdL associato all’aggregatore.

SAPID asset: SAPID dell’asset. Nel caso di assenza del SAPID viene visualizzato il SAPID dell’asset gerarchicamente superiore.

Stato operazione Geocall: riporta lo stato della relativa operazione in Geocall.

Figura 117 - Tabella contenente gli OdL figli da aggregare

Una volta associati gli OdL, l’utente clicca il pulsante “Calcola date Odl aggregatore” per procedere all’aggregazione degli OdL figli nell’OdL aggregatore. Questo pulsante attiva le logiche di ottimizzazione delle date di pianificazione, descritte nel paragrafo 8.1.2.3.2. In questo modo si definisce durata, date di pianificazione ed effort associati all’OdL aggregatore.

Per quanto riguarda la pianificazione economica dell’OdL aggregatore, quest’ultima seguirà le stesse regole e logiche previste per gli OdL non aggregatori ., la pianificazione economica verrà

##### Logica di ottimizzazione delle date dell’OdL aggregatore

Dopo aver premuto il pulsante “Aggrega OdL”, EAM procede a posizionare temporalmente gli OdL figli secondo specifiche logiche, al fine di ottimizzare i tempi di esecuzione dell’OdL aggregatore.

Per spiegare l’algoritmo di aggregazione si riporta l’esempio di un OdL aggregatore formato dagli OdL aggregati A, B, C, D dove il C può essere eseguito solo a valle del D (quindi C ha D come predecessore).

La lunghezza delle barre presenti negli schemi illustrati in questo processo rappresenta la durata di ciascun OdL aggregato, delimitata dalla data inizio pianificata (DIP) e data fine pianificata (DFP).

Step 1: il sistema determina l’intervallo di tempo entro cui l’OdL aggregatore può essere pianificato. Questa finestra temporale è compresa tra la maggiore data “non prima di” (NPD) e dalla minore data di “massimo posticipo attività” (MPA) degli OdL figli, come illustrato nella Figura 118 sottostante. In questo esempio, la finestra di flessibilità dell’OdL aggregatore è delimitata dalle date “non prima di” dell’OdL A e di “massimo posticipo” dell’OdL D.

Figura 118 - Step 1: Calcolo finestra di flessibilità dell'OdL aggregatore

Step 2: Il sistema verifica le propedeuticità presenti e posiziona gli OdL propedeutici (OdL D) in corrispondenza della data di fine pianificata minore (in questo esempio l’OdL B è quello che ha la data fine pianificata minore), come rappresentato in Figura 119.

Figura 119 - Step 2: Allineamento dell'OdL propedeutico in prossimità della minor data di fine pianificata

Step 3: Il sistema garantisce che il criterio di propedeuticità sia sodisfatto senza tempi morti tra le attività. Di conseguenza, l’OdL C (il quale, si ricorda, può essere effettuato solo dopo il completamento dell’OdL D) viene posizionato subito a valle della data di fine pianificata del predecessore (OdL D). In questo modo si avrà: data fine pianificata OdL D = data inizio pianificata OdL C.

Figura 120 - Step 3: Allineamento della data inizio pianificata dell'OdL C con la data fine pianificata dell'OdL D (propedeutico)

Step 4: Come illustrato in Figura 121, il blocco di OdL legati da un vincolo di propedeuticità (OdL C e D) viene spostato indietro in modo tale da garantire che la data di fine pianificata dell’OdL C sia allineata con la minore data di fine pianificata determinata nello step 2 (OdL B). Si specifica che non è possibile andare oltre al range definito dalla finestra di flessibilità dell’aggregatore. Dunque, se nel tentativo di allineare la data di fine pianificata dell’OdL C con la data di fine pianificata dell’OdL B si provoca lo spostamento della data inizio pianificata dell’OdL D (predecessore) prima dell’estremo minore della finestra di flessibilità, allora tutti gli OdL verranno spostati rigidamente in avanti in modo tale da rispettare il vincolo definito dall’estremo minore della finestra (quindi data di inizio pianificata dell’OdL D va a coincidere con l’estremo minore della finestra dell’aggregatore).

Figura 121 - Step 4: Spostamento rigido degli OdL legati da vincolo di propedeuticità

Step 5: Il sistema valuta la data di inizio pianificata minore ed allinea tutti gli OdL a tale data. Nell’esempio riportato la data di inizio pianificata minore è definita dall’OdL D; di conseguenza gli OdL A e B vengono traslati in modo tale da garantire che le loro date di inizio pianificate siano allineate alla prima data di inizio pianificata (ossia quella dell’OdL D). Da notare che l’unico OdL che non può essere traslato è il C in quanto eseguibile solo a valle del D. In questo modo le attività A, B e D iniziano contemporaneamente, permettendo all’utente di tenere un margine di sicurezza per l’esecuzione di A e B. La durata dell’OdL aggregatore è definita dalla differenza tra la minore data di inizio pianificata (OdL A, B e D) e la maggiore data di fine pianificata (OdL C) degli OdL sottostanti.

Figura 122 - Step 5: Allineamento della data inizio pianificata dell'OdL A e B

Definizione del tempo di trasferimento totale pianificato: Il tempo di trasferimento dell’OdL aggregatore viene inserito manualmente dall’utente con i seguenti passaggi:

Step 1: aprire l’applicazione “Monitoraggio OdL” e selezionare l’OdL aggregatore;

Step 2: selezionare il tab “Date”, rappresentato in Figura 123, dove può inserire manualmente nel campo evidenziato le ore di trasferimento necessarie per l’OdL aggregatore.

Figura 123 - Tab "Date" dell'applicazione Monitoraggio OdL, con evidenza al campo "Tempo di trasferimento"

##### Modifiche dell’OdL aggregatore

Nel seguito vengono descritte le modifiche effettuate da MPE sull’OdL aggregatore oltreché le modifiche manuali.

###### Modifiche effettuate da MPE

Come già descritto in questo capitolo, una volta generato l’OdL aggregatore, il motore di pianificazione euristico non considera più le date dei singoli OdL aggregati, ma utilizza le informazioni temporali dell’OdL aggregatore appena creato. In questo modo, qualsiasi spostamento delle date pianificate dell’OdL aggregatore comporta uno spostamento in blocco degli OdL sottostanti, in modo tale da rispettare le logiche definite dall’algoritmo di creazione dell’aggregatore.

.

Figura 124 - Schema rappresentativo dello spostamento temporale dell'OdL aggregatore e dei relativi figli

###### Modifiche manuale

EAM consente all’utente le seguenti azioni manuali di modifica dell’OdL aggregatore:

Modifica delle date di pianificazione: Qualora l’utente necessiti di spostare temporalmente l’OdL aggregatore è possibile modificare manualmente la data di fine pianificata dell’OdL aggregatore fintanto che l’OdL si trova in stato “Aperto”.

Per modificare la data di fine pianificata dell’OdL l’utente deve accedere all’applicazione “Monitoraggio OdL” e, dopo aver inserito l’ID dell’OdL aggregatore da modificare, seleziona il tab “Date”. Il sistema apre la schermata rappresentata in Figura 125, dove è possibile inserire la nuova data di fine pianificata nel campo evidenziato. Dopo aver effettuato e salvato la modifica, il sistema ricalcola la nuova data di inizio pianificata in base alla durata dell’OdL aggregatore. Si specifica che la modifica della data di fine pianificata è eseguibile solamente sull’aggregatore e non sugli OdL sottostanti.

Figura 125 - Tab "Date" dell'applicazione Monitoraggio OdL, con evidenza al campo "Data fine pianificata"

L’OdL aggregatore non può essere spostato oltre la finestra di flessibilità definita inizialmente dagli OdL aggregati. Questi ultimi vengono spostati mantenendo la loro composizione iniziale, come già illustrato in Figura 125.

Si specifica inoltre che non è possibile modificare durata ed effort direttamente sull’aggregatore, ma solo sugli OdL sottostanti. Nel caso in cui si effettui tale modifica, durata ed effort degli OdL sottostanti e dell’aggregatore verranno aggiornati automaticamente.

Aggiungere OdL: Analogamente a quanto descritto nel paragrafo 8.1.2.3.1, l’utente deve accedere all’applicazione “Monitoraggio OdL” e selezionare l’OdL aggregatore al quale si desidera aggiungere un ulteriore OdL figlio fino a che l’OdL si trova in stato “Aperto”. Successivamente, selezionando l’azione “Relaziona ordini”, presente nel menu a sinistra, l’utente sceglie gli OdL da aggiungere. Dopo aver scelto l’OdL figlio che si desidera aggregare, l’utente clicca sul tasto “Aggrega OdL” per attivare le logiche di ottimizzazione delle date già descritte nel par. 8.1.2.3.2.

Rimuovere OdL: Il sistema permette all’utente la rimozione di OdL figli dall’aggregatore solo se quest’ultimo si trova in stato “Aperto”. Per poter rimuovere un OdL figlio che da un ordine aggregatore che è già passato in stato schedulabile o schedulato, deve essere prima richiesta la ripianificazione tramite l’apposita funzionalità (alla ripianificazione l’utente viene avvisato che l’ordine sarà riportato in stato “Aperto”), per riportare l’ordine in stato “aperto”. Dopo aver aperto l’applicazione “Monitoraggio OdL” e selezionato l’OdL aggregatore dal quale si desidera rimuovere OdL figli, l’utente seleziona il tab “Pianificazione” (Figura 126). Per rimuovere un OdL figlio basta selezionare l’icona del cestino. Successivamente è necessario cliccare il pulsante “Aggrega ordini” per attivare le logiche di ottimizzazione già descritte nel par. 8.1.2.3.1. Si specifica che gli OdL figli rimossi dall’OdL aggregatore vengono automaticamente impostati in stato Aperto.

Figura 126 - Tab "Pianificazione" OdL aggregatore

#### Esecuzione OdL aggregatore

Come descritto nel cap. 12.1.1.2 l’odl aggregatore passa in stato “Eseguito” se:

- Tutti gli odl figli sono in stato “Eseguito”

- Almeno un odl figlio è in “Eseguito” e la restante parte dei figli in “Non eseguito” (si specifica che se tutti gli OdL figli sono in stato “Non eseguito” allora anche l’aggregatore assume lo stato “Non eseguito”).

Per portare in “Eseguito” lo stato dei figli di un aggregatore sono presenti 2 modalità:Cambio stato in “Eseguito” singolo: cliccando l’icona in prossimità del singolo figlio e salvando, è possibile portare in “Eseguito” il singolo figlio

Cambio stato in “Eseguito” massivo: cliccando sul pulsante “Stato eseguito massivo” e salvando, è possibile portare in “Eseguito” tutti gli odl figli.

Si evidenzia che la tabella contenente gli odl figli è composta dalle seguenti colonne filtrabili:

Odl: codice univoco dell’odl.

Descrizione: descrizione dell’odl

Asset: assetnum dell’asset

SAP-id

Stato: stato odl

Stato operazione Geocall: riporta lo stato della relativa operazione in Geocall

Inoltre, in “query disponibili” è presente una query preimpostata “OdL con operazioni Geocall in stato chiuso” che restituisce tutti gli OdL aggregatori che presentano dei figli con “Stato operazione Geocall” uguale a “Chiuso” e “Stato” diverso da “Eseguito”. In questo modo l’utente potrà ricercare tutti gli aggregatori per i quali è necessario cambiare lo stato dei figli in “Eseguito”.

# Pianificazione tecnica

## Motore di pianificazione euristico

In EAM è possibile effettuare la pianificazione tecnica degli OdL tramite la funzionalità dedicata “Motore di pianificazione euristico (MPE)”. Tale funzionalità consente di effettuare una pianificazione temporale di massima degli ordini all’interno di un determinato intervallo temporale selezionabile dall’utente, coerentemente con i vincoli di esecuzione delle singole attività e di capacity disponibile.

Nei paragrafi successivi viene illustrato il funzionamento di MPE evidenziandone le principali caratteristiche, in particolare:

Perimetro di applicabilità: perimetro di OdL processabili tramite MPE;

Parametri di Input: parametri di Input selezionabili dall’utente prima di avviare MPE;

Vincoli di pianificazione non modificabili da MPE (ad. Es. data non prima di / data massimo posticipo);

Logica di pianificazione;

Modalità di esecuzione;

Gestione proposta pianificazione MPE.

Definizione perimetro OdL ottimizzabili

Il perimetro di OdL ottimizzabili tramite MPE è definito in base a specifici attributi dell’OdL, dettagliati nel seguito del paragrafo.

MPE può agire unicamente sugli ordini in stato “Aperto” che presentano un periodo di pianificazione (differenza fra data massimo posticipo e data non prima di) superiore ad un mese. Nello specifico, il MPE considera esclusivamente le seguenti casistiche:

OdL on condition o periodici generati dal DSS;

OdL generati manualmente dall’utente in EAM.

Sono esclusi dal perimetro di applicabilità i seguenti OdL:

OdL serbatoio: in quanto definiti specificatamente sui mesi dell’anno in base alla Capacity disponibile; La capacity destinata ai serbatoi nel rispettivo mese viene sottratta alla capacity totale mensile in base a quanto definito nella tabella di ripartizione Effort per i serbatoi (OdL con tipo ordine “Serbatoio”);

OdL associato ad un’indisponibilità in stato “In Lavorazione” o successivo, poiché l’indisponibilità definisce sull’attività un vincolo temporale da rispettare una volta deliberata;

OdL non inseriti nella lista definita dall’utente all’atto del run;

OdL di Pronto intervento (OdL con PdL di tipo “Pronto intervento”);

OdL a calendario predefinito, per le quale la data di esecuzione vengono caricate massivamente (es. piani di volo);

OdL in outsourcing con frequenza fissa (ad es. ispezione di cavi interrati con orizzonte settimanale);

OdL riferiti ad attività di taglio piante;

Perché il sistema possa determinare univocamente gli ordini da escludere è gestita una tabella di configurazione in cui gli utenti centrali possono inserire le tipologie di PdL da escludere.

Tutti gli ordini non considerati nel perimetro di pianificazione del MPE saranno considerati come segue sulla base dello stato in cui si trovano:

Ordini in stato “aperti” esclusi dal perimetro MPE, erodono capacity disponibile;

Ordini in stato “schedulabile”, erodono capacity disponibile;

Ordini in stato “schedulato”, erodono capacity disponibile;

Ordini in stato “inviati”, erodono capacity disponibile;

Ordini in stato “Avviato” erodono capacity disponibile;

Ordini in stato “Eseguito parzialmente”, erodono capacity disponibile;

Ordini in stato “Eseguito”, non sono considerati nel calcolo della capacity;

Ordini in stato “Chiuso”, non sono considerati nel calcolo della capacity.

Parametri di Input

L’algoritmo di ottimizzazione di MPE considera diversi fattori:

Vincoli temporali (relative a ciascun OdL nel perimetro di MPE):

Data modello;

Data inizio pianificata;

Data fine pianificata;

Durata pianificata;

Effort per inquadramento;

Data di massimo posticipo attività;

Data non prima di;

Orizzonte temporale: indica il perimetro temporale sul quale il pianificatore posiziona gli ordini. Parametro definito dall’utente a partire dal mese successivo a quello di lancio.

Vincoli di capacity:

Capacity giornaliera: capacity teorica calcolata dividendo la capacity mensile (forza lavoro teorica al netto dei serbatoi) per i giorni lavorativi del mese per ognuno degli inquadramenti

Capacity giornaliera attività: calcolata, per inquadramento, come rapporto tra l’effort dell’attività e la durata in giorni. La durata in giorni di un’attività è il rapporto tra la durata pianificata e le ore lavorative di una giornata (8,6 h, 8 ore e 36 minuti considerando anche la pausa pranzo).

Il pianificatore evita di posizionare le attività nei giorni relativi al weekend (sabato e domenica) e alle festività nazionali.

Criterio di priorità:

Ogni attività è distinta in base alla priorità definita a catalogo PdL tramite un attributo così valorizzato: alta – valore 1, media – valore 2, bassa – valore 3.

Gli utenti centrali tramite tabella di sistema configurano le percentuali minime di capacity da dedicare mensilmente agli ordini di priorità inferiore a 1.

#### Orizzonte temporale di pianificazione

Di seguito si riportano le logiche di gestione della pianificazione in base all’orizzonte temporale:

Anno N: Per l’anno N sarà presente il dettaglio mensile della capacity per ogni inquadramento. Di conseguenza la pianificazione da parte dell’MPE avverrà considerando la capacity mensile riportata e dividendola per i giorni lavorativi del mese;

Da l’anno N+1 in poi: Dall’anno N+1 in poi non è presente a sistema il dettaglio mensile della capacity, ma solo quello annuale. In questo caso nel mese di dicembre verrà inserita la capacity dell’intero anno mentre da gennaio a novembre verrà inserito un valore pari a zero. Di conseguenza la pianificazione da parte dell’MPE avverrà considerando la capacity annuale riportata a dicembre, divisa per 12 e per i giorni lavorativi di ogni mese (esclusi quindi sabato, domenica e i giorni di festività nazionale).

Il periodo temporale selezionabile può andare dal mese successivo al mese corrente fino ad un periodo superiore all’anno N. Si specifica che da Tagetik viene importata la capacity degli anni che vanno da N a N+5. Per gli anni successivi è previsto il caricamento della capacity in EAM tramite file Excel.

Definizione vincoli di ottimizzazione

L’algoritmo di MPE pianifica gli OdL attenendosi ai seguenti vincoli di natura temporale e di capacity:

Vincolo date: per prima cosa l’MPE non potrà posizionare ordini oltre la loro data di “Data di massimo posticipo” o prima della loro “data non prima di”. Inoltre, affinché MPE prenda in considerazione l’attività, il periodo di pianificazione di quest’ultima (differenza tra la “Data di massimo posticipo” e la “Data non prima di”) deve essere superiore ad un intervallo predefinito (30 giorni).

Vincolo Capacity: deve essere rispettata la capacity disponibile per UdB mese per mese. La capacity deve essere saturata su base giornaliera inserendo gli OdL e verificando la saturazione. Nel considerare la capacity disponibile per ciascun mese MPE deve tener conto sia degli scambi intra-company, per cui la capacity mensile va saturata considerando l’impegno dei diversi TitRis, sia degli OdL serbatoio sui quali è stata pianificata una quota di capacity che MPE non deve considerare. Il sistema recupera i valori di capacity mensile dall’applicazione “Capacity”.

Di conseguenza, la capacity mensile viene calcolata come segue:

1° Step: La capacity mensile disponibile viene determinata come la differenza tra la forza lavoro teorica per mese, la capacity totale del serbatoio mensile, la capacity assorbita dagli OdL pianificati (quelli con stato successivo ad aperto ma non ancora chiusi) e la capacity degli OdL esclusi dal perimetro del run dell’MPE. La capacity è calcolata per tutte le figure professionali.

2° Step: La capacity giornaliera disponibile viene calcolata dividendo la capacity mensile disponibile per il numero di giorni lavorativi nel mese di riferimento (per inquadramento).

Logica funzionamento e operatività

Di seguito sono riportate le logiche di funzionamento e l’operatività utente per il lancio del pianificatore euristico:

L’utente indica il periodo temporale che il motore deve considerare per effettuare la pianificazione.

L’utente seleziona gli ordini sui quali deve essere effettuata la pianificazione, tra quelli di responsabilità della UdB a cui appartiene (esclusi quelli contenuti nella lista iniziale di ordini che non possono essere pianificati tramite MPE). Eventualmente, per ogni ordine incluso nella lista, può indicare un periodo temporale nel quale il pianificatore non può allocare quell’ordine. Queste date sono unicamente utilizzate dall’MPE al run per effettuare il posizionamento e non saranno salvate sull’ordine. Se necessario selezionare tutti gli ordini presenti nella lista (anche dopo averla filtrata) si utilizza il tasto azione nel menù di sinistra denominato “Seleziona tutti i record per l’invio ad MPE”. Questa funzionalità permette di selezionare ed inviare tutti i record della vista elenco compresi quelli presenti nelle pagine successive e precedenti a quella visualizzata. Il sistema, una volta cliccato sul tasto “Seleziona tutti i record per l’invio ad MPE” avvisa l’utente con un messaggio in sovraimpressione: “Attenzione, verranno inviati tutti i XXX odl presenti nella tabella di pianificazione. Procedere?”. Cliccando su OK vengono selezionati e inviati tutti gli ordini della pagina;

Il motore ordina gli OdL sulla base della “data di massimo posticipo attività”. In questo modo all’inizio della lista ordinata saranno presenti gli OdL con data di massimo posticipo collocata nel primo mese dell’intervallo;

Gli ordini sono poi posizionati nei giorni riempiendo il periodo indicato mese per mese secondo lo schema seguente:

Il motore pesca dalla lista ordinata gli ordini con priorità 1 e li posiziona partendo dal primo giorno cercando di saturare la capacity giornaliera per inquadramento. La prima figura professionale che viene saturata determina il passaggio al giorno successivo;

Una volta posizionati tutti gli ordini di priorità 1 per il mese, posiziona gli ordini di priorità 2 e 3 cercando di saturare gli sfridi lasciati dagli ordini già posizionati rispetto alla capacity giornaliera disponibile;

Raggiunta la saturazione della capacity disponibile del mese, il sistema controlla che siano state rispettate le percentuali minime di ordini con priorità 2 e 3 stabilite inizialmente dall’utente.

Se non sono state rispettate le percentuali minime, il sistema sostituisce gli ordini con priorità 1 con data di massimo posticipo più lontana (tenendo sempre in considerazione la durata degli ordini per rispettare le capacity giornaliere) con ordini di priorità 2 e 3 in modo da rispettare le percentuali minime definite.

La sequenza di passaggi descritta al punto 4 viene reiterata per tutti i mesi del periodo considerato dal pianificatore.

Modalità di esecuzione

Tramite l’applicazione “Motore di pianificazione euristico”, presente nel menu a sinistra del sistema, l’utente può specificare i parametri di ottimizzazione che l’algoritmo deve utilizzare così come il perimetro di OdL da elaborare. La maschera (Figura 127) presenta due sezioni:

Parametri di esecuzione: Campi di testata in cui è visibile l’intervallo temporale da ottimizzare (selezionato dall’utente), l’UdB (prepopolata dal sistema sulla base dell’UdB di appartenenza dell’utente che effettua la pianificazione, non modificabile).

Perimetro di pianificazione: Elenco di OdL che sono considerati dal MPE nelle logiche di pianificazioni.

La prima sezione consente all’operatore di inserire i parametri con cui lanciare l’ottimizzazione. Questa è costituita dai campi:

Inizio periodo da ottimizzare: in tale campo viene inserita la data di inizio del periodo da ottimizzare. Cliccando sul simbolo del calendario viene aperto un calendario da cui è possibile selezionare la data(saranno selezionabili solo giorni a partire dal mese successivo a quello in corso, se si seleziona un giorno nel mese in corso spunta un messaggio di errore).

Fine periodo da ottimizzare: in tale campo viene inserita la data di fine periodo da ottimizzare. Cliccando sul simbolo del calendario viene aperta una picklist costituita dai vari anni selezionabili.

La seconda sezione presenta la lista di OdL con cui è possibile procedere all’ottimizzazione. La sezione presenta i seguenti campi:

Id OdL: Codice univoco identificativo dell’OdL da pianificare;

Descrizione OdL: Descrizione dell’attività;

Classificazione Asset: Asset sul quale l’attività viene effettuata;

SAP ID

asset: Codice identificativo dell’asset di primo livello in SAP La colonna SAP ID asset è ordinabile;

Co.In;

Pianificato MPE: Campo valorizzato con “SI” o “NO”. Indica se l’OdL è mai stato pianificato dal MPE;

Data ultima pianificazione MPE: Indica l’ultima data in cui l’OdL è stato pianificato dal MPE.

Data pianificata inizio: Data pianificata per l’inizio dell’attività

Data pianificata fine: Data pianificata per l’inizio dell’attività

Data non prima di;

Data massimo posticipo;

Data proposta inizio;

Data fine proposta;

Responsabile attività;

Data Modello

Inizio periodo escluso;

Fine periodo escluso;

Effort pianificato quadri / impiegati / operai: Effort associato all’attività. È determinato dal prodotto tra ore esecuzione e quantità della figura professionale pianificata

Figura 127 - Maschera di selezione di OdL da inserire nel perimetro MPE

La tabella, per motivi di usabilità, può contenere un massimo di 100 record.

Le icone in alto a destra permettono di eseguire le seguenti azioni:

Eliminare i filtri

Filtrare per ciascuno dei campi in maschera degli OdL

Scaricare in file Excel la lista di tutti gli OdL ottimizzabili

L’operatore può, selezionando la checkbox a sinistra, decidere quali OdL inserire nel perimetro di pianificazione dell’MPE e, cliccando il tasto “Pianifica ordini selezionati”, avvia il motore.

Gestione proposta di pianificazione MPE

Dopo la valorizzazione dei parametri e l’avvio del motore di pianificazione euristico, tramite un processo di simulazione articolato in tre fasi operative l’utente può analizzare l’outcome dell’MPE:

Lancio: L’utente avvia la pianificazione degli OdL target tramite il MPE, come descritto nel paragrafo 9.1.5. Durante questa fase, l’outcome del pianificatore è provvisorio e le reali date di pianificazione degli OdL selezionati non vengono modificate;

Verifica: Il sistema permette all’utente (tramite apposito tasto) di scaricare su file excel le nuove date proposte dal MPE che sono state salvate su campi dedicati non sovrascrivendo le date di pianificazione già presenti. L’utente procede alla verifica della congruenza e fattibilità delle date suggerite fuori linea;

Conferma: Se l’utente approva l’elaborazione effettuata dal MPE, questa viene accettata e applicata a tutti gli OdL elaborati, comportando la modifica definitiva delle date di fine e inizio pianificate per le attività nonché la conseguente modifica delle date pianificate di risorse interne ed esterne. Le date di tutte le risorse devono essere traslate rigidamente insieme all’ordine. In particolare, dato che l’MPE non agisce sulla durata dell’ordine, la modifica delle date consiste in uno spostamento rigido del periodo pianificato. Lo stesso spostamento effettuato per le date di pianificazione dell’ordine deve essere applicato alle date di tutte le risorse presenti sull’ordine. Un esempio pratico per chiarire il comportamento:

Data pianificata inizio e fine dell’ordine originali: 19/01/2026 – 21/01/2026.

Date pianificata inizio e fine di risorsa 1 originale: 19/01/2026 – 20/01/2026.

Data pianificata inizio e fine di risorsa 2 originale: 20/01/2026 – 21/01/2026.

Data pianificata inizio e fine di risorsa 3 originale: 20/01/2026 – 20/01/2026.

La proposta dell’MPE riguarda esclusivamente le date pianificate inizio e fine dell’ordine che passano da 19/01/2026 – 21/01/2026 à 09/03/2026 – 11/03/2026. Quando l’utente accetta la proposta il sistema deve applicare la modifica a tutte le date dell’ordine, comprese quelle delle risorse pianificate (interne, esterne ed eventuali ricavi) traslandole rigidamente in conformità con la proposta dell’MPE. In questo caso quindi il risultato sarà:

Data pianificata inizio e fine dell’ordine dopo conferma: 09/03/2026 – 11/03/2026.

Date pianificata inizio e fine di risorsa 1 dopo conferma: 09/03/2026 – 10/03/2026.

Data pianificata inizio e fine di risorsa 2 dopo conferma: 10/03/2026 – 11/10/2026.

Data pianificata inizio e fine di risorsa 3 dopo conferma: 10/03/2026 – 10/03/2026.

In alternativa alla conferma, l’utente può rifiutare la proposta tramite apposito comando.

La vista della tabella, disponibile tramite download su file Excel, con le date proposte dal MPE è suddivisa per mese e contiene le seguenti colonne:

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

All’interno della schermata del motore di pianificazione euristico sono presenti i tasti che permettono all’utente di:

Scaricare la simulazione del lancio;

Accettare la proposta;

Rifiutare la proposta.

In caso di rifiuto le date di pianificazione rimarranno invariate mentre le date proposte verranno cancellate a sistema sia in caso di conferma che di rifiuto.

Modifica massiva OdL

In EAM l’utente ha la possibilità di eseguire modifiche massive di alcuni campi dell’OdL. Questo avviene utilizzando uno strumento standard di EAM chiamato MXLoader, che consente di caricare un file contenente le modifiche apportate agli OdL.

Per effettuare la modifica massiva, l’utente deve effettuare i seguenti passaggi:

Selezionare gli OdL da aggiornare;

Utilizzare la funzionalità di EAM per scaricare un template contenente i dati da modificare. In questo caso, il template conterrà unicamente i campi modificabili tramite MXLoader;

Aggiornare i campi, seguendo il formato già definito nel template per ognuno;

Caricare il template modificato nel sistema. Questo comporta l’aggiornamento di tutti i campi modificati.

Di seguito è riportato un file con l’elenco dei campi modificabili tramite MXLoader diviso per Tab della maschera dell’OdL.

￼

Storicizzazione modifiche data pianificata

Di seguito vengono riportate le informazioni necessarie al tracciamento delle modifiche della data di fine pianificata, le quali vengono gestite all’interno del tab “Date” dell’OdL:

Autore ultima modifica: campo popolato automaticamente con la matricola dell’utente che ha effettuato l’ultima modifica alla data di fine pianificata. Nel caso in cui l’ultima modifica sia effettuata dal motore di pianificazione euristico allora nel campo verrà riportato “MPE”;

Data ultima modifica: campo popolato automaticamente con la data dell’ultima modifica della data di fine pianificata.

Si specifica che è possibile consultare le date di pianificazione presenti nei diversi scenari tramite apposita reportistica (cfr. Analisi funzionale sprint 2).

# Indisponibilità

## Processo di gestione indisponibilità

Nell’ambito delle attività gestite tramite il sistema EAM, sono presenti alcune attività per le quali è necessaria una richiesta di indisponibilità dell’asset oggetto dell’OdL, degli asset interferenti o collegati elettricamente. La richiesta di indisponibilità, pianificata e deliberata nel sistema Agile 2.0, ha lo scopo di garantire che gli asset sui quali si intende effettuare un intervento di manutenzione / sostituzione, nonché quelli interferenti, siano fuori servizio durante il periodo programmato per l’esecuzione del lavoro.

Nella figura 128 viene riportato il flusso complessivo per processi UI, che comprende interazioni tra 3 sistemi: EAM, il quale pianifica l’OdL e invia le informazioni di questo verso Agile 2.0, Agile 2.0 che crea, lavora e delibera la richiesta di indisponibilità e Geocall che esegue la schedulazione ed esecuzione dell’attività sul campo. A seguire, in Figura 128, viene mostrato il flusso generale per processi non UI che comprende interazioni tra EAM ed Agile, con EAM che si occupa della pianificazione, schedulazione ed esecuzione dell’attività.

Figura 128 - Richiesta di indisponibilità per processi UI

Figura 129 - Richiesta di indisponibilità per processi non UI

Le richieste di indisponibilità possono essere monitorate all’interno dell’OdL nel tab dedicato “Indisponibilità” (Figura 130), composto dai seguenti campi:

Cluster titolare: Codice univoco identificativo del cluster di indisponibilità per l’ordine di lavoro;

Codice OdL: Codice dell'ordine di lavoro EAM comunicato con interfaccia RichiestaPeriodoIndisponibilità;

Data inizio indisponibilità: Campo riportante la data di inizio indisponibilità. Sarà valorizzata o meno a seconda dello stato richiesta;

Data fine indisponibilità: Campo riportante la data di fine indisponibilità. Sarà valorizzata o meno a seconda dello stato richiesta;

Stato richiesta: campo riportante lo stato della richiesta su Agile 2.0 e che andrà allineato in EAM.

Motivazione (MOD): Il campo viene sempre ricevuto in EAM e contiene le motivazioni relative ad annullamento, sospensione e altre casistiche di gestione diverse dalla delibera positiva. In EAM viene utilizzato nel caso dell’annullamento della richiesta di indisponibilità per determinare il sistema che l’ha originata a seguito delle due seguenti casistiche:

“EAM” in caso di annullamento a seguito di cambio del PdL dell’OdL in EAM con uno che non necessita l’indisponibilità;

“Dispacciamento” nel caso in cui la richiesta venga annullata dal dispacciamento.

Esito: Esito invio verso Agile 2.0 (OK/KO)

Codice errore: Codice dell’errore registrato su Agile 2.0;

Descrizione errore: descrizione esplicativa dell’errore registrato.

Tabella storico richieste: tabella che consente di visualizzare lo storico delle richieste di indisponibilità associate all’OdL, contenente le seguenti informazioni:

Data inizio indisponibilità: Campo riportante la data di inizio indisponibilità. Sarà valorizzata o meno a seconda dello stato richiesta;

Data fine indisponibilità: Campo riportante la data di fine indisponibilità. Sarà valorizzata o meno a seconda dello stato richiesta;

Stato: Stato della richiesta di indisponibilità

Motivazione: motivazione di annullamento / sospensione della richiesta.

Cluster titolare: Codice univoco identificativo del cluster di indisponibilità per l’ordine di lavoro;

Figura 130 - Tab "Indisponibilità" dell'OdL

Data l’elevata complessità dei flussi ed i possibili scenari ad essi associati, questi verranno dettagliati nel seguito del capitolo, in cui viene fornita un’analisi dettagliata di ogni fase del processo associato ai sistemi coinvolti. In particolare, nella prima parte, verrà posta attenzione sulla gestione dell’indisponibilità dal momento della creazione dell’OdL fino alla delibera della richiesta di indisponibilità. Successivamente, nel paragrafo seguente, verrà esaminata la gestione dell’indisponibilità a partire del momento in cui è deliberata in Agile 2.0.

Verrà poi posta attenzione sui casi di sospensione, ed annullamento della richiesta e sulla definizione dell’esito della delibera a seguito della ricezione delle date di indisponibilità definitive.

Creazione e delibera dell’indisponibilità

#### OdL non inviato a Geocall prima della delibera

In questo capitolo viene illustrato il processo di richiesta e delibera di indisponibilità in EAM, con evidenza delle relative integrazioni tra EAM ed Agile 2.0, a partire dalla creazione della richiesta fino al momento in cui viene deliberata in Agile 2.0. Si specifica che in questo capitolo è trattata la casistica in cui la richiesta di indisponibilità viene deliberata prima dell’invio dell’OdL a Geocall (stato OdL “Schedulabile”). Il flusso descritto in questo paragrafo si applica sia alla casistica di delibera ricevuta prima dell’invio a Geocall nel caso di OdL UI o misti, sia nel caso di OdL non UI.

La Figura 131 riporta gli step logici del processo di gestione delle richieste di indisponibilità, con evidenza delle interazioni tra i sistemi EAM e Agile 2.0. Nell’immagine sono evidenziati gli step operativi eseguiti nei sistemi coinvolti e i flussi di dati previsti tra essi. Inoltre, per ogni step operativo, sono riportati gli stati associati alla richiesta di indisponibilità in ciascun sistema (gli stati completi assumibili dalla richiesta saranno trattati nel paragrafo 10.1.2).

Si specifica che il processo in caso di annullamento o sospensione della richiesta di indisponibilità verrà trattato nel paragrafo 10.1.3.

Figura 131 - Schema logico del processo di creazione della richiesta di indisponibilità fino alla delibera (gestione prima dell’invio a Geocall)

Di seguito viene descritto il processo di richiesta indisponibilità nel caso in cui la richiesta venga approvata (deliberata) da Agile 2.0 prima dell’invio a Geocall, dando evidenza degli step procedurali su EAM. Le operazioni indicate nello schema e di seguito menzionate sono eseguite nel sistema EAM, suddivise in “Step operativi” e “Flussi dati”:

Step operativi:

Step 1E: L’OdL per il quale è necessaria l’indisponibilità (informazione dedotta dal PdL associato) viene creato manualmente o dal DSS con Policy periodiche in stato “Aperto”, oppure, nel caso di OdL generati dal DSS a partire da Policy “on-condition”, in stato “da validare” vengono successivamente portati automaticamente in stato “Aperto” a valle della validazione;

Step 2E: Al raggiungimento delle condizioni necessarie per l’invio (valorizzazione del set di dati minimo nell’OdL descritto nel capitolo 10.2) l’OdL viene inviato automaticamente ad Agile 2.0 per la creazione della richiesta di indisponibilità. Tutti gli OdL e le richieste di indisponibilità a questi associate sono visualizzabili nella sezione dedicata nel Centro di controllo. In questo step, i campi inerenti alla richiesta di indisponibilità sono ancora vuoti, e lo stato della richiesta passa a “Inviato”.

Step 3E: EAM riceve dal sistema Agile 2.0 l’esito di avvenuta creazione della richiesta di indisponibilità. In EAM vengono popolati i campi con i dati della richiesta (cluster titolare, data inizio / fine indisponibilità provvisoria e stato) nei campi del tab “Indisponibilità” rappresentati in Figura 130. Lo stato della richiesta di indisponibilità in EAM passa a “In lavorazione”;

Step 4E: L’utente potrebbe effettuare delle modifiche all’OdL in pianificazione. Nel caso in cui siano effettuate delle modifiche sui campi che hanno impatto sulla richiesta di indisponibilità creata in Agile 2.0 (stesso insieme dati necessario per l’invio automatico descritto nel capitolo 10.2), il sistema genera un Alert informativo non bloccante (segnalando che le modifiche potrebbero avere impatti sulla richiesta di indisponibilità in Agile 2.0) prima di poter confermare la modifica. Nel caso in cui l’utente confermi la modifica, gli aggiornamenti vengono trasmessi ad Agile 2.0. Nel caso in cui la modifica non riguardasse campi relazionati alla richiesta di indisponibilità, non verrà visualizzato alcun Alert prima della conferma da parte dell’utente.

Step 5E: Una volta deliberata in Agile, EAM riceve i dati definitivi della richiesta deliberata (che potrebbero sia confermare sia variare rispetto a quanto indicato in fase di creazione richiesta in Agile). Lo stato della richiesta in EAM viene modificato in “Deliberato”, e le date di indisponibilità vengono aggiornate con le date deliberate. In questa fase viene eseguita l’analisi dell’esito dell’indisponibilità deliberata.

Flusso di dati:

Flusso 1: Una volta raggiunte le condizioni necessarie per l’invio, descritte nel capitolo 10.2, un flusso dati viene automaticamente attivato verso il sistema Agile 2.0 che rende disponibili le informazioni relative all’OdL necessarie per la creazione della richiesta di indisponibilità in Agile 2.0. In questo flusso, viene inviato l’insieme di dati definiti nel capitolo 10.2. Si precisa che in questo flusso è previsto anche l’invio di eventuali modifiche nei campi inerenti alla richiesta di indisponibilità prima della sua creazione in Agile, che può avvenire in qualunque momento tra gli step 2E e 3E.

Flusso 2: Flusso di invio dei dati della richiesta dell’indisponibilità appena creata da Agile 2.0 a EAM. I dati da inviare, dettagliati nel capitolo 10.1.2, sono cluster titolare, motivazione, data inizio e fine indisponibilità provvisorie e lo stato.

Flusso 3: A seguito di un’eventuale modifica dei dati dell’OdL pianificato in EAM, è previsto un flusso di aggiornamento verso Agile 2.0. Nel flusso di dati inviati sono previsti tutti i campi oggetto di modifica definiti nel capitolo 10.2 nel tracciato dati verso Agile 2.0.

Flusso 4: Flusso di invio dei dati della richiesta deliberata. I dati da inviare sono il cluster titolare, data inizio e fine indisponibilità deliberate, lo stato e la motivazione.

La tabella sottostante riepiloga i flussi previsti tra i sistemi per questa parte del processo. In particolare, sono riportate le seguenti caratteristiche dei flussi:

Flusso: Identifica il flusso per come rappresentato in tabella 11;

Sorgente: Sistema di origine del flusso di dati;

Destinatario: Sistema al quale arriva il flusso di dati;

Modalità di invio: Indica se il flusso è attivato manualmente o automaticamente;

Evento: Indica la motivazione dell’invio del flusso dati;

Dati: Informazioni inviate/ricevute tramite il flusso.

| Flusso | Sorgente | Destinatario | Modalità di invio | Evento | Dati |
| --- | --- | --- | --- | --- | --- |
| Flusso 1 | EAM | Agile 2.0 | Automatica | Invio dati OdL per creazione richiesta indisponibilità | Per il set di dati inviato andare al capitolo 10.2 |
| Flusso 2 | Agile 2.0 | EAM | - | Ricezione informazioni richiesta indisponibilità | Per il set di dati ricevuto andare al par. 10.1.2 |
| Flusso 3 | EAM | Agile 2.0 | Automatica | Invio dati modificati per eventuale aggiornamento richiesta | Per il set di dati inviato andare al capitolo 10.2 |
| Flusso 4 | Agile 2.0 | EAM | - | Ricezione delibera richiesta di indisponibilità | Per il set di dati ricevuto andare al par. 10.1.2 |

Tabella 11 - Riepilogo dei flussi previsti tra EAM e Agile 2.0 per la casistica di OdL gestite in EAM

#### OdL inviato a Geocall prima della delibera

In questo paragrafo viene illustrato il processo di richiesta e delibera di indisponibilità nel caso di OdL inviati a Geocall prima della ricezione della delibera da Agile 2.0. In particolare, questa casistica si applica specificamente ai processi UI e misti, dove l’OdL viene pianificato in EAM e schedulato in Geocall.

La Figura 132 rappresenta gli step logici del processo di gestione delle richieste di indisponibilità, suddivisi per i sistemi coinvolti e i flussi previsti tra di essi. Nello specifico, lo schema evidenzia anche gli stati associati alla richiesta di indisponibilità, descritti nel dettaglio nel paragrafo 10.1.2.

Le casistiche di sospensione e annullamento della richiesta di indisponibilità verranno trattate nel capitolo 10.1.3.

Figura 132 - Schema logico del processo di creazione della richiesta di indisponibilità fino alla delibera (gestione dopo l’invio a Geocall)

Di seguito viene descritto il processo di richiesta indisponibilità nel caso in cui la richiesta venga approvata (deliberata) da Agile, dopo l’invio verso Geocall, dando evidenza degli step procedurali in EAM. Di seguito sono dettagliati gli “Step operativi” eseguiti nel sistema EAM e “Flussi dati” con i sistemi Agile 2.0 e Geocall:

Step operativi:

Step 1E: L’OdL per il quale è necessaria l’indisponibilità (informazione dedotta dal PdL associato) viene creato manualmente o dal DSS con Policy periodiche in stato “Aperto”, oppure, nel caso di OdL generati dal DSS a partire da Policy “on-condition”, in stato “da validare” vengono successivamente portati automaticamente in stato “Aperto” a valle della validazione;

Step 2E: Al raggiungimento delle condizioni necessarie per l’invio (valorizzazione del set di dati minimo nell’OdL descritto nel capitolo 10.2) l’OdL, le informazioni dell’asset e le informazioni collegate di asset in relazioni e opere interferenti, viene inviato automaticamente ad Agile 2. 0. Tutti gli OdL i cui dati sono stati inviati ad Agile sono visualizzabili nella sezione dedicata del centro di controllo. In questo step, i campi inerenti alla richiesta di indisponibilità sono ancora vuoti, e lo stato della richiesta si trova in “Inviato”;

Step 2.5E: L’OdL viene inviato a Geocall senza date di indisponibilità;

Step 3E: EAM riceve dal sistema Agile 2.0 l’esito di avvenuta creazione della richiesta di indisponibilità insieme al set di dati della richiesta: cluster titolare, data inizio / fine indisponibilità provvisorie e stato. Queste informazioni sono inserite nei campi del tab “Indisponibilità”, rappresentato in Figura 130. Lo stato della richiesta di indisponibilità in EAM passa a “In lavorazione” e le date di indisponibilità provvisoria vengono inviate a Geocall;

Step 4E: EAM riceve eventuali aggiornamenti delle date di pianificazione (sovrascritte con le date di schedulazione ricevute da Geocall) dell’OdL eseguiti sul sistema Geocall e li trasmette al sistema Agile 2.0.

Step 5E: Una volta deliberata in Agile, EAM riceve i dati definitivi della richiesta deliberata (che potrebbero sia confermare sia variare rispetto a quanto indicato in fase di apertura della richiesta da EAM) e invia le date deliberate verso Geocall. Lo stato della richiesta in EAM viene modificato in “Deliberato”, e le date di indisponibilità vengono aggiornate con le date deliberate. In questa fase viene eseguita l’analisi dell’esito dell’indisponibilità deliberata.

Flusso di dati:

Flusso 1: Una volta raggiunte le condizioni necessarie per l’invio, descritte nel capitolo 10.2 un flusso dati viene automaticamente attivato verso il sistema Agile 2.0 per consentire la creazione della richiesta di indisponibilità in Agile 2.0. In questo flusso, viene inviato l’insieme di dati descritti nel capitolo 10.2.

Flusso 2: L’OdL pianificato in EAM viene messo in stato “schedulabile” e le informazioni sull’OdL sono inviate a Geocall per la schedulazione.

Flusso 3: Flusso di invio dei dati della richiesta dell’indisponibilità appena creata da Agile 2.0 a EAM. I dati da ricevere, dettagliati nel capitolo 10.1.2 sono cluster titolare, motivazione, data inizio e fine indisponibilità provvisorie e lo stato.

Flusso 4: I dati della richiesta di indisponibilità creata in Agile 2.0 e inviate a EAM (cluster titolare, data inizio e fine indisponibilità provvisoria, stato della richiesta), vengono trasferiti a Geocall).

Flusso 5: Nel caso in cui vengono effettuate modifiche sull’OdL in Geocall, i dati dell’OdL aggiornati devono essere allineati con EAM. In questo flusso devono essere ricevute anche le nuove date di schedulazione impostate in Geocall.

Flusso 6: Le eventuali modifiche delle date di pianificazione dell’OdL rilevanti per la richiesta di indisponibilità, effettuate in Geocall e ricevute in EAM sono tramesse ad Agile 2.0. Nel flusso di dati inviati sono previste tutte le informazioni comprese nel tracciato descritto nel capitolo 10.2 che sono state modificate. Le date di schedulazione impostate in Geocall sovrascrivono le date di pianificazione previamente inserite in EAM, e perciò, vengono trasmesse ad Agile 2.0.

Flusso 7: Flusso di ricezione dei dati della richiesta deliberata e aggiornamento dello stato della richiesta in EAM. Le informazioni ricevute in questo flusso sono il cluster titolare, motivazione, data di inizio e fine indisponibilità deliberata e lo stato della richiesta.

Flusso 8: I dati della richiesta di indisponibilità deliberata in Agile 2.0 e ricevuti in EAM vengono trasmessi a Geocall dove vengono utilizzati per la schedulazione.

Le informazioni inviate in questo flusso sono il cluster titolare, data di inizio e fine indisponibilità deliberata e lo stato della richiesta.

La tabella sottostante riepiloga i flussi previsti tra i sistemi per questa parte del processo. In particolare, sono riportate le seguenti caratteristiche dei flussi:

Flusso: Identifica il flusso per come rappresentato in tabella 12;

Sorgente: Sistema di origine del flusso di dati;

Destinatario: Sistema al destinatario del flusso di dati;

Modalità di invio: Indica se il flusso è attivato manualmente o automaticamente;

Evento: Indica la motivazione dell’invio del flusso dati;

Dati: Informazioni inviate/ricevute tramite il flusso.

| Flusso | Sorgente | Destinatario | Modalità di invio | Evento | Dati |
| --- | --- | --- | --- | --- | --- |
| Flusso 1 | EAM | Agile 2.0 | Automatica | Invio dati OdL per creazione indisponibilità | Per il set di dati inviato vedere capitolo 10.2 |
| Flusso 2 | EAM | Geocall | Automatica | Invio dati OdL per schedulazione lavoro | Per il set di dati inviato vedere capitolo 10.2 |
| Flusso 3 | Agile 2.0 | EAM | - | Ricezione date provvisorie di indisponibilità | Per il set di dati ricevuto vedere par. 10.1.2 |
| Flusso 4 | EAM | Geocall | Automatica | Invio date provvisorie di indisponibilità | I dati inviati saranno gli stessi ricevuti da Agile, vedere par. 10.1.2 |
| Flusso 5 | Geocall | EAM | - | Ricezione modifiche OdL e date di schedulazione | L’insieme di dati relativi all’OdL che hanno subito una modifica |
| Flusso 6 | EAM | Agile 2.0 | Automatica | Invio modifiche OdL e nuove date di pianificazione | Per il set di dati inviato vedere capitolo 10.2 |
| Flusso 7 | Agile 2.0 | EAM | - | Ricezione delibera richiesta di indisponibilità | Per il set di dati ricevuto vedere par. 10.1.2 |
| Flusso 8 | EAM | Geocall | Automatica | Allineamento delibera richiesta di indisponibilità | I dati inviati saranno gli stessi ricevuti da Agile, vedere par. 10.1.2 |

Tabella 12 - Riepilogo dei flussi di dati previsti tra EAM, Agile 2.0 e Geocall per la casistica di OdL gestite in Geocall

Stati e dati ricevuti

Di seguito vengono riportati i dati che Agile 2.0 invia ad EAM in fase di lavorazione della richiesta di indisponibilità:

Cluster titolare: Codice univoco identificativo del cluster di indisponibilità per l’ordine di lavoro;

Codice OdL: Codice dell'ordine di lavoro EAM comunicato con interfaccia RichiestaPeriodoIndisponibilità;

Data inizio indisponibilità: Campo riportante la data di inizio indisponibilità. Sarà valorizzata o meno a seconda dello stato richiesta;

Data fine indisponibilità: Campo riportante la data di fine indisponibilità. Sarà valorizzata o meno a seconda dello stato richiesta;

Stato Cluster indisponibilità EAM: campo riportante lo stato della richiesta su Agile 2.0 e che andrà allineato in EAM.

Motivazione (MOD): Il campo viene sempre ricevuto in EAM e contiene le motivazioni relative ad annullamento, sospensione e altre casistiche di gestione diverse dalla delibera positiva. In EAM viene utilizzato nel caso dell’annullamento della richiesta di indisponibilità per determinare il sistema che l’ha originata a seguito delle due seguenti casistiche:

“EAM” in caso di annullamento a seguito di cambio del PdL dell’OdL in EAM con uno che non necessità l’indisponibilità;

“Dispacciamento” nel caso in cui la richiesta venga annullata dal dispacciamento.

Esito: Esito invio verso Agile 2.0 (OK/KO)

Codice errore: Codice dell’errore registrato su Agile 2.0;

Descrizione errore: descrizione esplicativa dell’errore registrato.

Nella Tabella 13 vengono riportati gli stati assunti dalla richiesta di indisponibilità in Agile 2.0 e la rispettiva trascodifica in EAM:

| Stato Cluster AGILE | Stato Cluster indisponibilità EAM |
| --- | --- |
| N.A | INVIATO |
| DA VALUTARE | IN LAVORAZIONE |
| SOSPESO | SOSPESO |
| ANNULLATO | ANNULLATO |
| IN PROGRAMMAZIONE | IN LAVORAZIONE |
| DELIBERATO | DELIBERATO |
| CONSUNTIVATO | CONSUNTIVATO |
| TRASMESSO | IN LAVORAZIONE |
| IN ESECUZIONE | IN ESECUZIONE |

Tabella 13 – Corrispondenza tra stati di EAM e di Agile 2.0

In figura 133 sono riportati gli stati che la richiesta di indisponibilità assume in EAM e il flusso logico che li lega:

Figura 133 - Rappresentazione schematica delle logiche di cambio di stato delle richieste di indisponibilità in EAM

Inviato: L’OdL è stato inviato ad Agile 2.0 e non è ancora stata creata una richiesta di indisponibilità;

In lavorazione: La richiesta di indisponibilità è in fase di valutazione e lavorazione su Agile 2.0;

Deliberato: La richiesta di indisponibilità è stata lavorata in Agile 2.0 e EAM è stato aggiornato con le date di indisponibilità. L’esito della delibera potrà essere “OK” o “KO” a seconda che le date di indisponibilità deliberate coincidano o meno con le date di pianificazione proposte. Per il dettaglio sulla determinazione dell’esito si veda il paragrafo 10.1.4.1;

In esecuzione: La richiesta di indisponibilità è in esecuzione;

Consuntivato: La richiesta di indisponibilità è consuntivata;

Sospeso: A seguito della delibera, Agile 2.0 può sospendere la richiesta poiché non è più possibile garantire l’indisponibilità nelle date definite. Il Cluster titolare in questo caso non cambia per cui EAM potrà eventualmente chiedere nuovamente l’indisponibilità sullo stesso Cluster con date di pianificazione differenti;

Annullato: La richiesta di indisponibilità viene annullata. L’annullamento può avvenire su input del dispacciamento o di EAM a seguito di una modifica del PdL associato all’OdL con un altro che non richiede l’indisponibilità su EAM. Il campo “Motivazione” verrà utilizzato per discriminare il tipo di annullamento. Questo aggiornamento di stato può arrivare anche a seguito di una richiesta già deliberata ma per cui non è più possibile garantire l’indisponibilità nelle date definite.

Sospensione / annullamento della richiesta

Come anticipato nel paragrafo 10.1.2, le logiche di gestione degli stati delle richieste di indisponibilità prevedono l’utilizzo degli stati “Sospeso” e “Annullato” in EAM.

Come rappresentato in Figura 134, da processo, le richieste di indisponibilità possono essere messe in stato “Sospeso” o “Annullato” su input di Agile in qualsiasi momento del ciclo di vita dell’OdL se l’indisponibilità è precedente a “In esecuzione”, mentre l’annullamento su input di EAM può essere richiesto eseguendo una variazione del PdL esclusivamente con OdL in stato “Aperto”. In tutti i casi sarà possibile eseguire una ripianificazione dell’OdL come descritto nel par. 10.1.4.2 Si precisa che tutti i passaggi di stato relativi alla richiesta di indisponibilità sono effettuati in Agile 2.0 ed ereditati in EAM.

Figura 134 - Schema logico del processo sospensione / annullamento della richiesta di indisponibilità in diverse fasi del ciclo di vita dell’OdL.

Di seguito sono descritte le diverse casistiche di sospensione o annullamento delle richieste di indisponibilità nel corso degli step operativi di processo (descritti nel paragrafo 10.1.1). Inoltre, sono delineati i flussi di dati previsti nel caso di sospensione o annullamento in ciascuna fase del processo.

Fase del processo:

Successivamente all’invio dei dati provvisori di indisponibilità da Agile 2.0 a EAM (Step 3e): La sospensione o annullamento della richiesta si verifica dopo che Agile 2.0 ha trasmesso i dati provvisori di indisponibilità. I campi “cluster titolare”, “data inizio indisponibilità” e “data fine indisponibilità” (provvisorie) mantengono le informazioni precedentemente inserite. Lo stato della richiesta viene aggiornato a “sospeso” o “annullato” a seconda del caso specifico (utilizzando il campo motivazione per distinguere fra i due annullamenti come descritto nel paragrafo 10.1.2). Lo stato della richiesta viene allineato su Geocall tramite flusso dedicato, qualora l’OdL sia già stato inoltrato per la schedulazione;

Successivamente a eventuali modifiche dell’OdL (Step 4e): la richiesta viene sospesa annullata a seguito di una modifica dell’OdL, effettuata in EAM / Geocall, dopo la creazione della richiesta. I campi della richiesta di indisponibilità in EAM sono popolati in modo analogo a quanto descritto nello Step 3e. Lo stato della richiesta viene allineato su Geocall tramite flusso dedicato, qualora l’OdL sia già stato inoltrato per la schedulazione;

Successivamente alla delibera della richiesta (Step 5e): Nel caso in cui la richiesta di indisponibilità venga sospesa o annullata successivamente alla delibera, i campi relativi alla richiesta di indisponibilità sono aggiornati in EAM secondo le medesime regole descritte nello Step 3e. Lo stato della richiesta viene allineato su Geocall tramite flusso dedicato, qualora l’OdL sia già stato inoltrato per la schedulazione.

Flussi:

Flusso 1, 3 e 5: Flusso dedicato all’inoltro a EAM delle informazioni relative alle richieste di indisponibilità dopo che queste sono messe in stato “sospeso” o “annullato”. Le informazioni ricevute includono lo stato (“sospeso” o “annullato” poi trascodificato nel corrispettivo stato EAM come mostrato nella tabella del paragrafo 10.1.2) e il contenuto del campo “Motivazione annullamento” nel caso di annullamento della richiesta. Le informazioni relative alle data di inizio e fine indisponibilità e cluster titolare non sono incluse in questo flusso e rimangono invariate in EAM.

Flusso 2, 4 e 6: Flusso dati verso Geocall contenente le informazioni relative alla richiesta sospesa o annullata. In particolare, viene trasmesso il campo “stato”. Non è previsto l’invio delle date di inizio e fine indisponibilità e cluster titolare, che rimangono invariate.

Nel caso di annullamento, l’utente di EAM può richiedere fuori linea la generazione di una nuova indisponibilità. In questo caso, su EAM, viene acquisita automaticamente la nuova richiesta associata all’OdL (identificata da un nuovo cluster titolare), mentre la precedente viene storicizzata. In EAM, l’utente può visualizzare lo storico delle richieste di indisponibilità per ciascun OdL.

La tabella sottostante riepiloga i flussi previsti tra i sistemi per questa parte del processo. In particolare, sono riportate le seguenti caratteristiche dei flussi:

Flusso: Identifica il flusso per come rappresentato in figura 134;

Sorgente: Sistema di origine del flusso di dati;

Destinatario: Sistema destinatario del flusso di dati;

Modalità di invio: Indica se il flusso è attivato manualmente o automaticamente;

Evento: Indica la motivazione dell’invio del flusso dati;

Dati: Informazioni inviate/ricevute tramite il flusso.

| Flusso | Sorgente | Destinatario | Modalità di invio | Evento | Informazioni inviate |
| --- | --- | --- | --- | --- | --- |
| Flusso 1, 3 e 5 | Agile 2.0 | EAM | - | Allineamento EAM dopo sospensione, annullamento della richiesta a valle della creazione su Agile 2.0 | Stato aggiornato della richiesta. Gli stati ricevuti sono “non programmabile”, “Sospeso” o “Annullato”. In caso di annullamento della richiesta, viene inviata l’informazione presente sul campo “Motivazione annullamento” |
| Flusso 2, 4 e 6 | EAM | Geocall | Automatica | Allineamento Geocall dopo sospensione, annullamento della richiesta | Stato aggiornato della richiesta. Gli stati previsti sono “Sospeso”, “Annullato”. In caso di annullamento della richiesta, viene inviata l’informazione presente sul campo “Motivazione annullamento” |

Tabella 14 - Riepilogo dei flussi tra EAM, Agile 2.0 e Geocall nel caso di sospensione o annullamento della richiesta di indisponibilità

Esito della richiesta

L’esito della richiesta di indisponibilità, visualizzabile nel centro di controllo, viene utilizzato dall’utente per verificare se la richiesta è stata deliberata e rispetta le date richieste oppure se è necessario ripianificare l’attività in base a quanto deliberato.

Questo capitolo approfondisce le logiche di calcolo dell’esito delle richieste di indisponibilità, e descrive la sezione del centro di controllo dedicata al monitoraggio delle richieste di indisponibilità associate ai relativi OdL.

#### Determinazione dell’esito della richiesta

L’esito di una richiesta di indisponibilità è determinato in base allo stato della richiesta e alle date di inizio e fine pianificate dell’OdL, nonché alle date di inizio e fine di indisponibilità della richiesta. Di seguito vengono descritte le logiche di determinazione dell’esito:

ESITO OK: Indica che la richiesta di indisponibilità è stata deliberata da Agile con esito positivo e che le date indicate sono allineate con quanto pianificato nell’OdL.

Stato della richiesta: L’esito OK può essere attribuito alle richieste di indisponibilità che si trovano nei seguenti stati in EAM.

Deliberato;

In esecuzione;

Consuntivato.

Per gli stati “In esecuzione” e “Consuntivato”, l’esito OK è impostato di default dal sistema EAM in quanto rappresentano rispettivamente le fasi del processo in cui l’attività è già in esecuzione ed è conclusa.

Nel caso dello stato “Deliberato”, oltre alla condizione sullo stato, devono essere anche rispettate le regole sulle date descritte nel seguito del capitolo per confermare l’esito OK.

Date pianificate e di indisponibilità: Dopo aver ricevuto la delibera da parte di Agile, l’analisi dell’esito viene eseguita automaticamente mediante confronto delle date di pianificazione dell’OdL inserite in EAM con le date di indisponibilità deliberata in Agile 2.0.

Una indisponibilità deliberata può ottenere l’esito OK se la finestra temporale compresa tra la data inizio pianificata (DIP) e la data fine pianificata (DFP) contiene la finestra temporale definita dalla data inizio indisponibilità (DII) e dalla data fine indisponibilità deliberata (DFI). In particolare, si individuano quattro casistiche distinte di esito OK, rappresentate nello schema in Figura 135:

Caso 1: Data inizio indisponibilità successiva alla data inizio pianificata e data fine indisponibilità precedente alla data fine pianificata;

Caso 2: Data inizio indisponibilità coincidente con la data inizio pianificata e data fine indisponibilità coincidente con la data fine pianificata;

Caso 3: Data inizio indisponibilità coincidente con la data inizio pianificata e data fine indisponibilità precedente alla data fine pianificata;

Caso 4: Data inizio indisponibilità successiva alla data inizio pianificata e data fine indisponibilità coincidente con la data fine pianificata.

Figura 135 - Possibili casistiche di esito OK in base alle date di inizio / fine pianificate e inizio / fine indisponibilità

ESITO KO: L’esito KO definito in EAM indica che la richiesta non è compatibile con le date pianificate in EAM.

Stato della richiesta: L’esito KO può essere attribuito alle richieste di indisponibilità che si trovano nei seguenti stati in EAM.

Deliberato;

Sospeso;

Annullato

Gli stati “Sospeso”, “Annullato” determinano automaticamente l’esito KO della richiesta di indisponibilità. Al contrario, nel caso dello stato “Deliberato”, l’esito può essere KO in alcune casistiche dipendenti dalle date pianificate in EAM e quelle di indisponibilità deliberate in Agile, descritte nel bullet successivo.

Se l’utente in Agile 2.0 sospende o annulla una richiesta che era in stato “in lavorazione” o “deliberato” in EAM, il sistema riceve automaticamente l’aggiornamento dello stato della richiesta e ne definisce l’esito come KO.

Date pianificate e di indisponibilità: Dopo aver trasmesso i dati della richiesta in stato “Deliberato” a EAM, l’analisi dell’esito viene eseguita mediante il confronto delle date di pianificazione dell’OdL definite in EAM con le date di indisponibilità deliberate in Agile 2.0.

Una indisponibilità deliberata può ottenere l’esito KO se la finestra temporale compresa tra la data inizio pianificata (DIP) e data fine pianificata (DFP) delle attività non contiene tutta la finestra di tempo definita dalla data inizio indisponibilità (DII) e dalla data fine indisponibilità deliberata (DFI).

In particolare, si individuano cinque casistiche distinte di esito KO, rappresentate nello schema in figura 136:

Caso 1: Data inizio indisponibilità precedente alla data inizio pianificata e data fine indisponibilità antecedente alla data fine pianificata;

Caso 2: Data inizio indisponibilità successiva alla data inizio pianificata e data fine indisponibilità successiva della data fine pianificata;

Caso 3: Data inizio indisponibilità precedente alla data inizio pianificata e data fine indisponibilità successiva alla data fine pianificata;

Caso 4: Data inizio indisponibilità coincidente con la data inizio pianificata e data fine indisponibilità successiva alla data fine pianificata.

Caso 5: Data inizio indisponibilità precedente alla data inizio pianificata e data fine indisponibilità coincidente con la data fine pianificata.

Figura 136 - Possibili casistiche di esito KO in base alle date di inizio / fine pianificate e inizio / fine indisponibilità

ESITO NON DEFINITO: L’esito della richiesta di indisponibilità non è valorizzato quando lo stato della richiesta in EAM è “in lavorazione” o “Inviato”. Nel centro di controllo, il campo relativo all’esito delle richieste in questo stato sarà popolato con “-“.

La Tabella 15 sottostante riepiloga gli esiti ammissibili per ciascuna casistica descritta in questo capitolo:

| Stato in EAM | Data inizio pianificata in relazione alla data inizio indisponibilità | Data fine pianificazione in relazione alla data fine indisponibilità | Esito ammissibile |
| --- | --- | --- | --- |
| Deliberata | Precedente | Successiva | OK |
| Deliberata | Coincidente | Coincidente | OK |
| Deliberata | Coincidente | Successiva | OK |
| Deliberata | Prec | Coincidente | OK |
| In esecuzione | N.A | N.A | OK |
| Consuntivato | N.A | N.A | OK |
| Deliberata | Successiva | Successiva | KO |
| Deliberata | Precedente | Precedente | KO |
| Deliberata | Successiva | Precedente | KO |
| Deliberata | Coincidente | Precedente | KO |
| Deliberata | Successiva | Coincidente | KO |
| Sospeso | N.A | N.A | KO |
| Annullato dal dispacciamento | N.A | N.A | KO |
| Annullato dal cambio metodologia | N.A | N.A | KO |

Tabella 15 - Riepilogo degli esiti in base allo stato della richiesta e alle date pianificate e di indisponibilità

#### Ripianificazione di OdL con esito KO

Come descritto nel paragrafo precedente, l’esito dell’indisponibilità può essere determinato confrontando le date di pianificazione dell’OdL con quelle di indisponibilità della richiesta deliberata in Agile 2.0.

Nei casi in cui l’esito della richiesta è definito come KO a causa dell’incompatibilità tra le date pianificate e quelle di indisponibilità, l’utente in EAM ha la possibilità di eseguire alcune operazioni per allineare queste date con quelle dell’indisponibilità. In particolare, accedendo all’applicazione di “Monitoraggio OdL”, l’utente può eseguire le azioni descritte nei seguenti paragrafi. Una volta effettuata la modifica sull’OdL, il sistema ricalcola l’esito basandosi sulla nuova pianificazione e aggiorna automaticamente le informazioni presenti nel centro di controllo. Se l’attività da modificare è già stata inviata a Geocall per la schedulazione, è necessario deschedularla (riportandola in stato Aperto) per poter modificare le date di pianificazione in EAM.

Si specifica che l’operatività sopra descritta può essere eseguita nell’OdL, compatibilmente con lo stato, a prescindere dalla gestione dell’esito dell’indisponibilità.

##### Modificare le date pianificate

Qualora si verifichi un’incompatibilità tra le date di inizio e fine pianificate e le date di inizio e fine di indisponibilità, comportando un esito negativo (KO) della richiesta, è possibile modificare le date di pianificazione in modo tale da renderle coerenti con quelle di indisponibilità.

Selezionando il record dell’OdL associato ad un esito KO nel centro di controllo, l’utente viene indirizzato alla schermata dell’applicazione “Monitoraggio OdL” del sistema EAM. Selezionando il tab “Date”, illustrato nella figura 137, l’utente può modificare le date di fine e inizio pianificata (entrambe devono essere modificate manualmente), coerentemente alle date associate alle risorse pianificate (se l’OdL è in stato “Aperto”, se in stato “Schedulabile” o “Schedulato” deve essere utilizzata la funzionalità di ripianificazione, che riporta l’OdL in stato aperto).

Figura 137 - Vista del tab “Date” dell’applicazione “Monitoraggio OdL” di EAM

Successivamente all’inserimento della nuova data di fine pianificata, il sistema calcola automaticamente la nuova data di inizio pianificata in base alla data fine e la durata pianificata. Se le date modificate rispettano le condizioni descritte nel paragrafo 10.1.4.1, EAM imposta automaticamente l’esito della richiesta associata all’OdL ad OK nel centro di controllo. Le nuove date di inizio e fine pianificate vengono automaticamente allineate con Agile 2.0 tramite il flusso dedicato.

##### Modifica dell’effort dell’attività

Nel caso in cui l’esito della richiesta di indisponibilità deliberata risulti negativo (KO) a causa di un’incompatibilità tra le date pianificate e quelle di indisponibilità, e la durata definita per l’attività non consenta all’utente di allinearle, è possibile modificare l’effort inizialmente associato all’attività (modificando il numero di risorse pianificate), e di conseguenza modificare manualmente la durata coerentemente. Dopo aver modificato manualmente la durata prevista per l’esecuzione dell’attività, è necessario modificare manualmente le date di inizio e fine di pianificazione, in modo tale da adeguare tali date alla finestra temporale definita dalle date di inizio e fine dell’indisponibilità deliberata.

Selezionando dal centro di controllo il record corrispondente all’OdL da modificare, l’utente viene indirizzato all’OdL di interesse nell’applicazione di “Monitoraggio OdL”. Cliccando sul tab “Piani” dell’applicazione, l’utente visualizza la tabella delle risorse pianificate per l’attività, come rappresentato in Figura 138. In questa schermata, l’utente può modificare l’effort nel seguente modo:

Dopo aver modificato le risorse necessarie, l’utente le deve salvare cliccando sull’icona evidenziata nella parte superiore destra della vista.

Figura 138 - Vista del tab “Piani” dell’applicazione “Monitoraggio OdL” di EAM

Nel tab “Date” dell’applicazione “Monitoraggio OdL”, l’utente aggiorna il campo della durata con la nuova durata determinata non automaticamente in base al nuovo effort. Anche le nuove date di inizio e fine pianificata dovranno essere inserite manualmente dall’utente.

Successivamente alla modifica delle date di inizio e fine pianificate, viene inviato ad Agile 2.0 un flusso dati di aggiornamento delle nuove date. L’esito della richiesta di indisponibilità viene ridefinito in base alle nuove date pianificate e alle date di indisponibilità precedentemente fornite da Agile 2.0. Dopo la modifica, l’esito della delibera diventa positivo (OK) nel centro di controllo.

##### Modificare i figli sottesi all’OdL aggregatore

Nel caso in cui un OdL aggregatore riceva un esito negativo (KO) a causa dell’incompatibilità delle date, è possibile variarne la durata dei OdL figli. In questo modo, EAM consente all’utente di modificare la durata prevista per l’OdL aggregatore (calcolata come differenza fra la prima data pianificata e l’ultima data pianificata fra i figli) e di allocare la finestra temporale di indisponibilità deliberata all’interno di quella pianificata.

Selezionando il record dell’OdL da modificare direttamente dal centro di controllo, l’utente viene indirizzato all’OdL di interesse nell’applicazione “Monitoraggio OdL” di EAM. Nel tab “Pianificazione”, rappresentato nella figura sottostante, l’utente può sganciare gli OdL figli selezionando l’icona evidenziata, modificare le date e durata del figlio appena sganciato, riagganciare il figlio al padre e ricalcolare le date dell’odl padre tramite apposita funzionalità.

Figura 139 - Vista del tab "Pianificazione" contenente gli OdL figli

Per ricalcolare date e durata dell’aggregatore, dopo aver effettuato la modifica, l’utente deve utilizzare la funzionalità “Calcola date aggregatore”. In questo modo il sistema ricalcola la durata dell’OdL aggregatore come differenza fra la prima data di inizio pianificata e l’ultima data fine pianificata fra quelle associate ai figli.

Se, a seguito delle modifiche effettuate, la finestra temporale definita dalle date inizio e fine indisponibilità rientra all’interno di quella definita dalle date di inizio e fine pianificate, il sistema imposta l’esito della richiesta associata all’OdL ad OK nel centro di controllo. Le nuove date di inizio e fine pianificate di EAM vengono allineate automaticamente con Agile 2.0.

##### Cambiare PdL associato all’OdL

Nel caso in cui l’esito della delibera dell’indisponibilità sia negativo (KO), l’utente in EAM ha la possibilità di modificare il PdL associato all’OdL, (per gli OdL in stato “Aperto”). Sostituendo il PdL con un altro che non richiede l’indisponibilità (l’identificazione dei PdL che non richiedono indisponibilità è descritta nel capitolo 10.2) è possibile proseguire con l’esecuzione dell’attività senza il vincolo della delibera.

È possibile accedere all’OdL da modificare dalla sezione dedicata agli esiti del centro di controllo selezionando il rispettivo record. Nel tab “Work Oder”, rappresentato nella Figura 140, l’utente modifica il PdL associato selezionandone uno nuovo che non richieda l’indisponibilità fra i PdL ammissibili per quell’OdL (proposti automaticamente dal sistema nella lista elenco). In particolare, l’utente può scegliere solo PdL che abbiano la stessa Macro-attività dal PdL da sostituire.

Figura 140 - Vista del tab “Work order” dell’applicazione” Monitoraggio OdL”

Dopo aver salvato la modifica, i dati dell’OdL modificato vengono trasmessi automaticamente ad Agile 2.0 dove viene modificato lo stato della richiesta in “Annullato”, indicando il giustificativo “EAM” nel campo “Motivazione annullamento”. Infine, lo stato dell’indisponibilità su EAM viene settato a “Annullato”.

Gestione dell’indisponibilità post-delibera

In questo capitolo viene illustrato il processo di gestione delle richieste di indisponibilità in EAM, con evidenza delle relative integrazioni tra il sistema, Agile 2.0 e Geocall a seguito della delibera da parte di Agile 2.0, sia per processi UI che processi non UI.

#### Processo post-delibera per ordini UI

La Figura 141 rappresenta gli step logici del processo di gestione delle richieste di indisponibilità per processi UI, con evidenza delle interazioni tra i sistemi EAM, Agile 2.0 e Geocall. Nell’immagine sono evidenziati gli step operativi eseguiti su EAM, così come i flussi di dati previsti tra i sistemi. Inoltre, per ogni step operativo, sono riportati gli stati associati alla richiesta di indisponibilità (gli stati completi assumibili dalla richiesta saranno trattati nel paragrafo 10.1.2).

Figura 141 - Gestione della richiesta di indisponibilità post-delibera per processi UI

Step operativi:

Step 1E: EAM riceve da Agile 2.0 la delibera dell’indisponibilità e provvede ad allineare Geocall. In questo step le date di inizio e fine indisponibilità sono quelle definitive e lo stato della richiesta è impostato in “Deliberato”;

Step 2E: EAM aggiorna le date di pianificazione con le date di schedulazione ricevute da Geocall e provvede ad allineare Agile 2.0;

Step 2.5E: EAM riceve da Agile 2.0 l’aggiornamento dello stato della richiesta di indisponibilità “In Lavorazione”, nel caso in cui le date di pianificazione aggiornate richiedono una modifica delle date di indisponibilità su Agile.

Step 3E: EAM riceve dal sistema Agile 2.0 la conferma di delibera per la nuova schedulazione proposta da Geocall e provvede ad aggiornare Geocall;

Step 4E: EAM riceve da Geocall l’aggiornamento dello stato dell’ordine di lavoro in “In carico” e riceve l’aggiornamento dello stato della richiesta di indisponibilità “In esecuzione” da Agile 2.0;

Step 5E: EAM invia verso Geocall l’aggiornamento della richiesta di indisponibilità in stato “In esecuzione”;

Step 6E: EAM riceve da Geocall l’aggiornamento dell’OdL in stato “Chiuso” e riceve l’aggiornamento della richiesta di indisponibilità da Agile 2.0 in stato “Consuntivato”.

Flusso di dati:

Flusso 1: Flusso di ricezione dei dati della richiesta deliberata da Agile 2.0. I dati ricevuti sono il cluster titolare, motivazione, data inizio e fine indisponibilità deliberate e lo stato.

Flusso 2: Flusso di invio dei dati della richiesta deliberata verso Geocall. I dati inviati sono il cluster titolare, data inizio e fine indisponibilità deliberate e lo stato.

Flusso 3: Flusso di ricezione delle date di schedulazione da Geocall.

Flusso 4: Flusso di invio delle date di schedulazione verso Agile 2.0. I dati invitati sono descritti nel capitolo 10.2. Tale flusso invia anche eventuali modifiche dell’OdL che impattano o meno la richiesta di indisponibilità.

Flusso 4.5: Flusso di ricezione dei dati della richiesta da Agile 2.0. I dati ricevuti sono il cluster titolare, motivazione e lo stato.

Flusso 5: Flusso di ricezione della conferma di delibera da Agile 2.0. I dati ricevuti sono cluster titolare, data inizio indisponibilità, data fine indisponibilità e lo stato.

Flusso 6: Flusso di invio delle date di delibera confermate verso Geocall. I dati invitati sono il cluster titolare, motivazione, data inizio e fine indisponibilità deliberate e lo stato.

Flusso 7: Flusso di ricezione dell’aggiornamento di stato da Geocall.

Flusso 8: Flusso di ricezione dell’aggiornamento della richiesta di indisponibilità da Agile 2.0. I dati ricevuti sono cluster titolare, motivazione e stato “in esecuzione”.

Flusso 9: Flusso di invio dell’aggiornamento dello stato della richiesta di indisponibilità verso Geocall. I dati inviati sono il cluster titolare e lo stato “in esecuzione”.

Flusso 10: Flusso di ricezione dell’aggiornamento dello stato dell’OdL da Geocall.

Flusso 11: Flusso di ricezione dell’aggiornamento della richiesta di indisponibilità da Agile 2.0. I dati ricevuti sono cluster titolare, motivazione e stato.

La tabella sottostante riepiloga i flussi previsti tra i sistemi per questa parte del processo. In particolare, sono riportate le seguenti caratteristiche dei flussi:

Flusso: Identifica il flusso per come rappresentato in tabella 16;

Sorgente: Sistema di origine del flusso di dati;

Destinatario: Sistema destinatario del flusso di dati;

Modalità di invio: Indica se il flusso è attivato manualmente o automaticamente;

Evento: Indica la motivazione dell’invio del flusso dati;

Dati: Informazioni inviate/ricevute tramite il flusso.

| Flusso | Sorgente | Destinatario | Modalità di invio | Evento | Dati |
| --- | --- | --- | --- | --- | --- |
| Flusso 1 | Agile 2.0 | EAM | - | Ricezione delibera richiesta di indisponibilità | Cluster titolare, Motivazione, Data inizio indisponibilità, Data fine indisponibilità, Stato |
| Flusso 2 | EAM | Geocall | Automatica | Invio dati richiesta deliberata | Cluster titolare, Data inizio indisponibilità, Data fine indisponibilità, Stato |
| Flusso 3 | Geocall | EAM | - | Ricezione date di schedulazione (vincolo full) | Date di schedulazione |
| Flusso 4 | EAM | Agile 2.0 | Automatica | Invio date di pianificazione (sovrascritte con le date di schedulazione) | Per il set di dati inviato vedere capitolo 10.2 |
| Flusso 5 | Agile 2.0 | EAM | - | Ricezione delibera richiesta di indisponibilità | Cluster titolare, Motivazione, Data inizio indisponibilità, Data fine indisponibilità, Stato |
| Flusso 6 | EAM | Geocall | Automatica | Invio dati richiesta deliberata | Cluster titolare, Data inizio indisponibilità, Data fine indisponibilità, Stato |
| Flusso 7 | Geocall | EAM | - | Ricezione aggiornamento stato OdL | Stato OdL |
| Flusso 8 | Agile 2.0 | EAM | - | Ricezione aggiornamento stato indisponibilità | Cluster titolare, Stato indisponibilità, Motivazione |
| Flusso 9 | EAM | Geocall | Automatica | Invio aggiornamento stato indisponibilità | Stato indisponibilità |
| Flusso 10 | Geocall | EAM | - | Ricezione aggiornamento stato OdL | Stato OdL |
| Flusso 11 | Agile 2.0 | EAM | - | Ricezione aggiornamento stato indisponibilità | Cluster titolare, Stato indisponibilità, Motivazione |

Tabella 16 - Riepilogo dei flussi previsti post delibera tra EAM, Agile 2.0 e Geocall per la casistica dei processi UI

#### Processo post-delibera per ordini non UI

La Figura 142 rappresenta gli step logici del processo di gestione delle richieste di indisponibilità per processi non UI, con evidenza delle interazioni tra i sistemi EAM e Agile 2.0. Nell’immagine sono evidenziati gli step operativi eseguiti su EAM e i flussi di dati previsti tra i due sistemi. Inoltre, per ogni step operativo, sono riportati gli stati associati alla richiesta di indisponibilità (gli stati completi assumibili dalla richiesta saranno trattati nel paragrafo 10.1.2).

Figura 142 - Gestione della richiesta di indisponibilità post-delibera per processi non UI

Step operativi:

Step 1E: EAM riceve da Agile 2.0 la delibera dell’indisponibilità in stato “Deliberato”.

Step 2E: EAM esegue la verifica dell’esito della richiesta;

Step 3E: Se l’esito è OK, EAM avvia l’esecuzione dell’OdL (stato “Eseguibile”) e lo stato della richiesta può essere aggiornato in “In Esecuzione” tramite flusso da Agile 2.0;

Step 4E: Al termine dell’esecuzione in EAM (stato “Eseguito”) lo stato della richiesta viene aggiornato in “Consuntivato” tramite flusso da Agile 2.0.

Flusso di dati:

Flusso 1: Flusso di ricezione dei dati della richiesta deliberata da Agile. I dati ricevuti sono il cluster titolare, motivazione, data inizio e fine indisponibilità deliberate e lo stato;

Flusso 2: Flusso di ricezione dell’aggiornamento della richiesta di indisponibilità da Agile 2.0. I dati ricevuti sono cluster titolare, motivazione e stato;

Flusso 3: Flusso di ricezione dell’aggiornamento della richiesta di indisponibilità da Agile 2.0. I dati ricevuti sono cluster titolare, motivazione e stato.

La tabella sottostante riepiloga i flussi previsti tra i sistemi per questa parte del processo. In particolare, sono riportate le seguenti caratteristiche dei flussi:

Flusso: Identifica il flusso per come rappresentato in tabella 17;

Sorgente: Sistema di origine del flusso di dati;

Destinatario: Sistema destinatario del flusso di dati;

Modalità di invio: Indica se il flusso è attivato manualmente o automaticamente;

Evento: Indica la motivazione dell’invio del flusso dati;

Dati: Informazioni inviate/ricevute tramite il flusso.

| Flusso | Sorgente | Destinatario | Modalità di invio | Evento | Dati |
| --- | --- | --- | --- | --- | --- |
| Flusso 1 | Agile 2.0 | EAM | - | Ricezione delibera richiesta di indisponibilità | Cluster titolare, Motivazione, Data inizio indisponibilità, Data fine indisponibilità, Stato |
| Flusso 2 | Agile 2.0 | EAM | - | Ricezione aggiornamento richiesta di indisponibilità | Cluster titolare, Motivazione, Stato |
| Flusso 3 | Agile 2.0 | EAM | - | Ricezione aggiornamento richiesta di indisponibilità | Cluster titolare, Motivazione, Stato |

Tabella 17 - Riepilogo dei flussi previsti post delibera tra EAM, Agile 2.0 per la casistica dei processi non UI

Centro di controllo

Come anticipato, in EAM sarà possibile visualizzare lo stato delle richieste di indisponibilità tramite la funzione di reportistica del centro di controllo. In questo paragrafo viene descritta la sezione dedicata del centro di controllo, attraverso il quale l’utente può visualizzare e accedere agli OdL per i quali è stata creata una richiesta di indisponibilità.

Come mostrato in figura 143, nel tab “richieste di indisponibilità” del centro di controllo, gli OdL con richiesta di indisponibilità con esito positivo vengono evidenziati in verde. Gli OdL associati a richieste ancora in stato “in lavorazione” / “Inviato” sono riportati inblu , mentre gli OdL con esito negativo in rosso. In questa sezione del centro di controllo sono anche riportati gli OdL per i quali potrebbe essere necessaria una richiesta di indisponibilità (flag indisponibilità nel PdL a “Si”) e per i quali non è ancora stata creata la richiesta (si trovano in stato “inviato”).

Nel tab “Esito delle richieste di indisponibilità” sono riportate le seguenti informazioni per ciascun OdL:

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

Esito della richiesta: Esito della richiesta di indisponibilità, definita come precedentemente descritto nel capitolo;

Figura 143 - Sezione del centro di controllo contenente gli OdL che richiedono indisponibilità

Selezionando uno dei record presenti in questa sezione del CdC, l’utente è indirizzato all’applicazione di “Monitoraggio OdL”. In questo modo, l’utente ha la possibilità di ripianificare gli OdL che hanno avuto esito KO secondo le regole descritte nel paragrafo seguente.

## Condizioni di invio

Le richieste di indisponibilità vengono generate nel sistema Agile 2.0 sulla base delle richieste associate agli OdL ricevuti da EAM. Questo capitolo si focalizza sulla descrizione del set di dati OdL da inviare al sistema Agile ai fini della generazione delle richieste d’indisponibilità, oltre alle condizioni necessarie per l’invio dei dati dell’OdL al sistema Agile 2.0.

In EAM, gli OdL per i quali è necessario richiedere un’indisponibilità sulla rete vengono inviati automaticamente ad Agile 2.0 al verificarsi di determinate condizioni minime (compilazione di un set di dati obbligatori nell’OdL). In particolare, è possibile identificare gli OdL che richiedono un’indisponibilità tramite il flag “indisponibilità”, ereditato dal PdL associato. Come illustrato in Figura 144, ogni PdL che richiede un’indisponibilità dell’asset viene identificato tramite il flag “Indisponibilità” presente nel catalogo delle attività.

Figura 144 - Vista di dettaglio di un PdL con evidenza al flag di indisponibilità

Per gli OdL associati a PdL che richiedono indisponibilità in stato “Aperto”, il sistema invia automaticamente le informazioni necessarie per la creazione della richiesta di indisponibilità su Agile 2.0 nel momento in cui viene valorizzato o modificato il set di dati obbligatori per l’invio (viene riportata la distinzione fra campi obbligatori per innescare l’invio e campo facoltativi che non innescano l’invio ma rientrano nel tracciato dei dati inviati ad Agile):

Codice OdL (obbligatorio): Codice univoco identificativo dell’OdL per la quale si richiede l’indisponibilità.

Descrizione OdL (obbligatorio): Descrizione dell’OdL per la quale si richiede l’indisponibilità.

CBS (facoltativo): campo non obbligatorio, informazione recuperata dal tab CoIn dell’OdL.

Tipologia attività (aggregatore per agile 2.0) (facoltativo): Mapping di aggregazione tra piano di lavoro e tipologia attività su AGILE 2.0;

Tipo spesa (obbligatorio): Campo che caratterizza il tipo di OdL (CAPEX, OPEX, ANR).

Responsabile attività (obbligatorio): Unità impianti responsabile dell’OdL.

Codice sede tecnica di indisponibilità (obbligatorio): sede tecnica d’indisponibilità associata alla sede tecnica Reale e presente in anagrafica (sede tecnica indisponibilità master). Verranno veicolate nel dettaglio i codici di sede tecnica indisponibilità strettamente connessi all’asset oggetto dell’OdL. Il dettaglio della valorizzazione di questo campo viene riportato al paragrafo 10.2.1.

Sede tecnica d’indisponibilità padre (obbligatorio): Sede tecnica d’indisponibilità legata all’Asset di tipo Linea o Stazione (gerarchicamente legato all’asset reale dell’ordine di lavoro)

Sedi tecniche interferenti (facoltativo): lista codici sedi tecniche interferenti (opere interferenti). Viene valorizzata automaticamente a valle della selezione dell’asset dell’OdL solo nel caso in cui siano presenti delle opere interferenti sull’asset per il quale viene richiesta l’indisponibilità.

Data pianificata inizio (obbligatorio): Data pianificata della singola attività.

Data pianificata fine (obbligatorio): Data pianificata della singola attività.

PdL (obbligatorio): Piano di lavoro associato all’OdL.

Stato odl (obbligatorio): stato dell’odl

Logica valorizzazione Codice sede tecnica indisponibilità

All’interno del campo Codice sede tecnica indisponibilità viene inserita la sede tecnica di indisponibilità legata all’asset su cui si fa manutenzione/ispezione utilizzando le seguenti logiche:

Nel caso di un OdL generato sul primo livello di tipo Linea, il codice di sede tecnica d’indisponibilità è uguale al valore dell’attributo LIN_ELEMENTO_INDISP della Linea;

Nel caso di un OdL generato su un asset di secondo, terzo o quarto livello della linea, il codice di sede tecnica è uguale al valore dell’attributo LIN_ELEMENTO_INDISP dell’asset di terzo livello “CONDUTTORI” o “CAVI” (rispettivamente se è elemento in aereo o in cavo) riconducibile con la seguente logica: viene navigata la gerarchia di un asset per identificare l’elemento con la classificazione “CONDUTTORI” o “CAVI”, se l’elemento viene trovato viene utilizzato il valore di LIN_ELEMENTO_INDISP; nel caso in cui l’OdL parta da un asset in gerarchia con SOSTEGNO AEREO o ESTREMO CAVO, viene recuperato l’attributo LIN_ELEMENTO_INDISP del CONDUTTORI o CAVI figli di della CAMPATA AEREA o TRATTA CAVO che ha i primi 16 caratteri di SAPID uguali al sostegno o estremo in valutazione;

Nel caso di OdL generato su asset di 4° livello stazione che non detiene l’attributo STZ_ELEMENTO_INDISP, verrà risalita la gerarchia fino al massimo al 3° livello gerarchicamente superiore. Se quest’ultimo detiene l’attributo STZ_ELEMENTO_INDISP allora l’OdL su asset di 4° livello erediterà il codice sede tecnica indisponibilità del 3° livello padre. Nel caso in cui il 3° livello non abbia l’attributo STZ_ELEMENTO_INDISP valorizzato, allora verrà restituito l’errore nel tab “Indisponibilità” dell’Odl, come riportato in figura sottostante.

Nel caso di Odl generato su asset di 3° livello stazione, il codice di sede tecnica indisponibilità verrà ereditato dal 3° livello stesso. Se quest’ultimo non dovesse avere l’attributo STZ_ELEMENTO_INDISP valorizzato allora verrà restituito l’errore nel tab “Indisponibilità” dell’Odl.

Per gli asset di 1° e 2° livello stazione non sono previste attività che richiedono indisponibilità, di conseguenza sui pdl che puntano al 1° e 2° livello stazione non dovrà mai essere valorizzato il flag “indisponibilità”.

Inoltre, in aggiunta alle sedi tecniche d’indisponibilità legate all’asset verranno veicolate anche le sedi tecniche d’indisponibilità degli asset collegati elettricamente all’asset

## OdL aggregatore

Le logiche di gestione degli OdL implementate nel sistema EAM prevedono la possibilità di aggregare diversi OdL in un OdL aggregatore. L’OdL aggregatore è un’entità prevista in EAM che permette all’utente di raggruppare vari OdL associati allo stesso PdL e riconducibili allo stesso asset di primo livello.

È possibile aggregare solamente OdL per i quali non è ancora stata creata una richiesta di indisponibilità in Agile 2.0, oppure se associati a richieste di indisponibilità in stato annullato.

La figura sottostante riporta i passaggi previsti per la gestione delle indisponibilità nel caso di OdL aggregatore (utilizzando come esempio gli OdL 1,2,3 della figura).

Invio dati ad Agile 2.0: Una volta soddisfatte le condizioni di invio descritte nel capitolo 10.1.6, i dati necessari per la creazione delle richieste di indisponibilità degli OdL figli vengono trasmessi automaticamente ad Agile 2.0 e lo stato delle relative richieste viene impostato ad “Inviato”.

Arrivo dei dati dell’OdL in Agile 2.0: I dati relativi agli OdL 1, 2 e 3 generati in EAM arrivano in Agile 2.0. L’utente di Agile 2.0, informato fuori linea della necessita di aggregare gli OdL 1, 2, 3 in un momento successivo, non procede alla creazione della richiesta di indisponibilità. Nel caso in cui sia stata creata una richiesta di indisponibilità per l’OdL figlio, la stessa deve essere annullata per permettere la creazione dell’OdL aggregatore.

Creazione dell’OdL aggregatore: In EAM viene creato l’OdL aggregatore, contenente come figli gli OdL 1, 2 e 3.

Invio dati dell’OdL aggregatore ad Agile 2.0: Una volta soddisfatte le condizioni di invio, vengono inviati ad Agile 2.0 i dati inerenti all’OdL aggregatore, al quale viene associato una richiesta di indisponibilità. Il set di dati trasmesso ad Agile 2.0 per la creazione della richiesta è il medesimo degli OdL non aggregatori, e viene valorizzato settando il campo “Asset indisponibilità” in base all’Asset inserito nell’OdL aggregatore. Per gli OdL figli, contenuti nell’OdL aggregatore, la generazione di richieste di indisponibilità viene bloccata.

Creazione della richiesta di indisponibilità: In Agile 2.0 viene creata la richiesta di indisponibilità dell’OdL aggregatore. I dati della richiesta vengono inviati ad EAM come già visto nel paragrafo 10.1.1.

Delibera della richiesta: La richiesta di indisponibilità dell’OdL aggregatore viene deliberata in Agile 2.0.

Arrivo dei dati della richiesta in EAM: Dopo la delibera della richiesta di indisponibilità dell’OdL aggregatore, i dati relativi alla stessa (“cluster titolare”, “stato”, “data inizio e fine indisponibilità”) vengono ricevuti in EAM. A partire da questo punto, il processo di gestione delle richieste di indisponibilità per l’OdL aggregatore segue in modo analogo quanto già descritto per gli OdL non aggregati.

Figura 145 - Schema logico della gestione delle richieste di indisponibilità di OdL aggregatori

Nel caso in cui l’OdL aggregatore venga creato automaticamente dal DSS (ad esempio per tronchi di ispezioni), le sue informazioni vengono inviate direttamente ad Agile 2.0 per generare la richiesta di indisponibilità. Analogamente al caso precedente, le informazioni relative agli OdL figli vengono inviate ad Agile 2.0, ma non sono utilizzate per creare nuove richieste di indisponibilità.

# Pianificazione economica

## Caratterizzazione OdL con informazioni CoIn

Il processo di popolamento dei dati di contabilità industriale viene eseguito all’interno del tab “CoIn” dell’OdL ed è rappresentato ad alto livello nella Figura 146.

In particolare, la Figura 146 rappresenta in verde le fasi di lavorazione in EAM ed in blu le azioni che coinvolgono SAP (da cui vengono acquisiti gli oggetti contabili necessari).

Figura 146 - Processo popolamento dati di contabilità industriale

Di seguito una descrizione degli step logici del processo:

Allineamento da SAP delle destinazioni contabili: Informazioni di CBS, CBE, OI, Network (e relativa operazione) e OdM sono allineate dall’apposita interfaccia massiva (descritta nel paragrafo 11.2.1);

Generazione OdL da DSS o Manuale: Pre-popolamento dati del tab «CoIn» (logiche descritte in dettaglio nel paragrafo 11.1.2);

Compilazione dati di pianificazione economica light: Valorizzazione dei campi di contabilità non pre-popolati, ed eventuale modifica dei campi pre-popolati del tab «CoIn» (cfr. 11.1.2);

Conferma dati per richiesta creazione su SAP: Una volta confermati i dati, nel caso in cui sia necessario richiedere la creazione di OdM / NTK in SAP, viene utilizzata l’apposita funzionalità descritta nel paragrafo 11.2.2 che permette di generare OdM / NTK on demand;

Creazione OdM / NTK in SAP: Viene generato l’oggetto richiesto in SAP;

Trasmissione codice OdM / NTK: Le informazioni inerenti all’OdM e Network (e relativa operazione) create in SAP sono inviate ad EAM senza la necessità di validazione;

Popolamento campo OdM / NTK: In EAM viene popolato automaticamente il campo NTK (e relativa operazione) / OdM.

Come rappresentato in Figura 147, l’invio a WFM per la schedulazione (solo OdL UI) può avvenire sia prima che dopo la creazione di OdM/NTK. Si specifica che l’unica informazione contabile necessaria per l’invio a Geocall è il Tipo Spesa

Come anticipato, le informazioni relative alla contabilità industriale sono gestite nel tab dedicato “CoIn” dell’OdL (Figura 147).

Figura 147 - Tab Coin

La struttura del tab viene automaticamente modificata dal sistema in base al “Tipo Spesa” (OPEX, CAPEX, ANR) dell’OdL, mostrando campi differenti a seconda di tale caratteristica. (cfr. par. 11.1.2).

Nei paragrafi successivi viene dettagliato il set informativo del tab “CoIn” in base al Tipo Spesa e le relative logiche di pre-valorizzazione.

Set informativo tab CoIn

Il set di dati che viene mostrato nel tab CoIn varia in base alla valorizzazione del campo “Tipo Spesa”, mostrando insieme di dati specifici a seconda che tale valore sia “CAPEX”, “OPEX” o “ANR”.

Gli attributi “Tipo Spesa” e “ID Attività Standard” saranno sempre visibili in testata del tab.

Si riporta di seguito lo specchietto riepilogativo contenente le informazioni presenti visibili (PV), presenti non visibili (PNV) e non presenti (NP) in base al “Tipo Spesa”.

|  | CAPEX | OPEX | ANR |
| --- | --- | --- | --- |
| Codice CoIn | P | P | P |
| Tipo attività PM | NP | P | P |
| Tipo network | PNV | NP | NP |
| Tipo ordine OI | NP | NP | P |
| Tipo ordine OdM | NP | P | P |
| CBS contabile | P | NP | P |
| CBE contabile | P | NP | P |
| CBS di pianificazione | NP | PNV | PNV |
| CBE di pianificazione | NP | PNV | PNV |
| Numero Ordine Interno | NP | NP | P |
| Identificativo NTK | P | NP | NP |
| Operazione | P | NP | NP |
| Identificativo OdM | NP | P | P |

Tabella 18 - Presenza dei campi in base al Tipo Spesa

Nelle figure sottostanti viene riportato il layout del tab Coin, coerentemente con quanto riportato in Tabella 18.

Figura 148 - Tab Coin per attività ANR

Figura 149 - Tab Coin per attività CAPEX

Figura 150 - Tab Coin per attività OPEX

Valorizzazione dati tab CoIn

Alla generazione dell’OdL, il sistema pre-valorizza dei sub-set di dati utilizzando logiche basate sui cataloghi censiti in EAM; altri dati invece dovranno essere inseriti manualmente. Le procedure di compilazione dei vari campi del tab CoIn cambiano a seconda del contenuto del campo “Tipo Spesa”, il quale definisce se l’attività in questione è di tipo CAPEX, OPEX o ANR. Il campo in questione rispetta le seguenti logiche:

Pre-valorizzato dal sistema sulla base del valore prevalente presente nel catalogo standard;

I soli valori ammissibili sono:

CAPEX

OPEX

ANR

Modificabile dall’utente in base all’attività standard associata al PdL.

In base al valore inserito nel campo “Tipo spesa” (CAPEX, OPEX o ANR) sono presenti diversi step di compilazione dei campi del tab CoIn, di seguito riportati.

Per quanto riguarda invece il campo “ID Attività Standard”, quest’ultimo viene pre-valorizzato con l’ID attività standard associato al PdL tramite il catalogo attività.

#### CAPEX

Valorizzazione Codice Coin: Valorizzato automaticamente sulla base del catalogo standard e modificabile selezionando tra i valori ammissibili

Tipo network: Valorizzato automaticamente con valore PST2. Il campo non è visualizzabile in maschera

3.A Se Network esistente:

4.A Il sistema propone una lista di NTK: le NTK associate a CBE con profit center uguale al codice coin, con sede tecnica di 1° livello contenente l’asset di riferimento dell’OdL e con centro di costo responsabile afferente al responsabile attività dell’OdL. Dunque, l’utente valorizza manualmente il campo Network selezionando un valore dalla lista restituita. Dopo la selezione della NTK da parte dell’utente, il sistema popola automaticamente l’Operazione (campo sempre in read only). Sarà possibile selezionare solo Network attive, gli stati non idonei (Stati sistema: BLOC, CHIU, COBL, FCAN; Stati utente: ZBLK) saranno esclusi.

5.A Il sistema valorizza automaticamente la CBE/CBS sulla base della NTK selezionata nel punto 4.A.

3.B Se Network non esistente:

4.B L'utente inserisce una CBE/CBS contabile selezionandola fra quelle proposte dal sistema (filtrate per profit center uguale al codice coin dell’odl, sede tecnica di 1° primo livello contenente l’asset di riferimento dell’OdL e centro di costo responsabile afferente al responsabile attività dell’OdL);

5.B L’utente utilizza l’interfaccia di creazione che genera la NTK (e relativa Operazione) sulla base della CBE inserita nello step precedente.

In merito alle CBS multi-impianto (utili per gestire le campagne), quest’ultime non sono associate a una sede tecnica di primo livello e concettualmente possono essere collegate a sedi tecniche diverse. Il vincolo di associazione con la sede tecnica di primo livello viene ignorato per i casi di CBS/CBE multi-impianto. Di conseguenza sarà possibile visualizzare tra le CBS/CBE selezionabili, oltre a quelle che rispettano le logiche descritte negli step precedenti, anche quelle che non hanno una sede tecnica di riferimento in quanto multi-impianto.

Infine, valorizzando il flag “Mostra CBS/CBE su qualsiasi sede tecnica” (flag presente solo quando il tipo spesa è CAPEX) l’utente può disattivare il filtro sulla sede tecnica di primo livello presente in fase di selezione della cbs/cbe/ntk. Restano comunque attivi i vincoli su profit center e centro di costo.

#### OPEX

Valorizzazione Codice CoIn: Valorizzato automaticamente in base al catalogo standard e modificabile selezionando tra i valori ammissibili;

Valorizzazione attività PM: Valorizzato automaticamente in base alla relazione tra attività PM e codice CoIn; rappresenta infatti i digit dopo l’ultimo punto del codice CoIn (es: se il codice CoIn è EA101B1LCD.CTO allora l’attività PM viene valorizzata con CTO);

Valorizzazione Tipo Ordine OdM: Valorizzato automaticamente dal sistema in base alla relazione tra Tipo ordine OdM e codice Coin

CBS/CBE di pianificazione Valorizzati automaticamente in base all’UI e al tipo spesa. Il campo non è visualizzabile in maschera.

5.A Se OdM esistente:

6.A L’utente seleziona manualmente l’OdM selezionandolo da una lista (filtrata per Tipo attività PM, Tipo ordine OdM, sede tecnica di 1°livello ,centro di costo responsabile afferente al responsabile attività dell’OdL e stato). Per quanto riguarda lo stato verranno restituiti solo gli odm in stato “RIL” e “APER”.

5.B Se OdM non esistente:

6.B L’utente utilizza l’interfaccia di creazione che genera l’OdM sulla base del Tipo attività PM e del Tipo ordine OdM.

Inoltre, solamente per ordini con tipo spesa OPEX, la creazione dell’odm può avvenire anche automaticamente al momento dell’invio del primo consuntivo verso SAP (cfr. cap. 12.3.1.2).

Si specifica che anche nel caso in cui Tipo ordine OdM = PMAC e Tipo attività PM = SFO deve essere utilizzato un OdM.

#### ANR

Valorizzazione Codice CoIn: Valorizzato automaticamente sulla base del catalogo standard e modificabile selezionando tra i valori ammissibili;

Tipo attività PM: Popolato automaticamente in base alla relazione con il Codice Coin; rappresenta infatti i digit dopo l’ultimo punto del codice CoIn (es. se il codice CoIn è AT_M.MAL.CTO allora l’attività PM viene valorizzata con CTO);

CBS/CBE pianificazione: CBS valorizzata automaticamente in base all’UI e al tipo spesa; CBE valorizzata automaticamente in base alla CBS e al codice Coin.

4.A. Se OdM esistente sono presenti 2 possibilità di valorizzazione:

4.A.1 L’utente seleziona manualmente l’OdM selezionandolo da una lista (filtrata per Tipo attività PM, Tipo ordine OdM PMT9 e PMTG, centro di costo responsabile afferente al responsabile attività e stato; Per quanto riguarda lo stato verranno restituiti solo gli odm in stato “RIL” e “APER”. Dopo aver inserito l’OdM i campi restanti vengono popolati automaticamente, come descritto di seguito:

- Il campo Tipo ordine OdM viene popolato automaticamente in base all’OdM inserito nello step 4.A.1 I valori ammissibili per questo campo sono PMT9 o PMTG. Il campo ammette solo un valore (PMT9 o PMTG), non è quindi possibile il popolamento multi-value del campo.

- I campi CBS/CBE contabile (solamente per Ordini con tipo attività EPC) e Numero ordine interno vengono popolati automaticamente in base all’OdM inserito. Si specifica che in caso di tipo attività EPC la regola di scarico costi è solo una tra CBS contabile e ordine interno. Di conseguenza quando viene inserito l’odm verrà popolata la cbs/cbe contabile quando la regola di scarico costi è la cbs contabile oppure l’ordine interno quando la regola di scarico costi è l’ordine interno.

- Il sistema popola automaticamente il campo Tipo ordine OI con i seguenti valori:

- Tipo ordine OI=T06 se Tipo ordine OdM=PMT9

- Tipo ordine OI=T09 se Tipo ordine OdM=PMTG

4.A.2 L’utente valorizza manualmente il Numero ordine interno selezionandolo da una lista contenente tutti gli ordini interni con profit center uguale al codice coin dell’ordine. In base al numero ordine interno selezionato il sistema popola automaticamente il Tipo ordine OI con T06 o T09. Successivamente l’utente seleziona manualmente l’OdM selezionandolo da una lista (filtrata per Tipo attività PM, Tipo ordine OdM PMT9 o PMTG, il numero ordine interno precedentemente inserito, centro di costo responsabile afferente al responsabile attività e stato “RIL” o “APER”). Dopo aver inserito l’OdM i campi restanti vengono popolati automaticamente, come descritto di seguito:

- Il campo Tipo ordine OdM viene popolato automaticamente in base all’OdM inserito nello step 4.A.2. I valori ammissibili per questo campo sono PMT9 o PMTG. In particolare:

- Tipo ordine OdM=PMT9 se tipo ordine OI=T06

- Tipo ordine OdM=PMTG se tipo ordine OI=T09

4.B Se OdM non esistente:

4.B.1 Se Tipo attività PM = EPC e regola di scarico costi a CBS/CBE contabile:

- L’utente seleziona una CBE/CBS contabile da una lista filtrata per profit center che termina con la stringa EPC e centro di costo responsabile afferente al responsabile attività. Una volta selezionata la cbs/cbe contabile il campo “Numero ordine interno” non risulta più editabile.

- L’utente utilizza l’interfaccia di creazione che genera l’OdM sulla base della CBE valorizzata nello step precedente.

- Una volta creato l’OdM viene popolato automaticamente il campo Tipo ordine OdM con PMT9.

4.B.2 Se Tipo attività PM = EPC e regola di scarico costi a ordine interno:

- L’utente seleziona il Numero ordine interno da una lista filtrata per profit center che termina EPC. Il campo Tipo ordine interno viene popolato automaticamente in base all’OI (i valori ammissibili per il tipo ordine interno OI sono T06, T09). Una volta selezionato l’ordine interno il campo “CBS/CBE contabile” non risulta più editabile.

- L’utente utilizza l’interfaccia di creazione che genera l’OdM sulla base dell’OI valorizzato nello step precedente

- Una volta creato l’OdM viene popolato automaticamente il campo Tipo ordine OdM. I valori ammissibili per Tipo ordine OdM sono:

- PMT9 se tipo ordine OI=T06.

- PMTG se tipo ordine OI=T09

4.B.3 Se tipo attività PM ≠ EPC:

- L’utente seleziona un Numero Ordine interno da una lista filtrata per tipo ordine OI uguale a T06 e T09 e profit center uguale al codice coin dell’odl. Una volta inserito il numero ordine interno, il campo Tipo ordine OI viene valorizzato con T09 o T06;

- L’utente utilizza l’interfaccia di creazione che genera l’OdM sulla base dell’OI valorizzato nello step precedente

- Una volta creato l’OdM viene popolato automaticamente il campo Tipo ordine OdM. I valori ammissibili per Tipo ordine OdM sono:

- PMT9 se tipo ordine OI=T06.

- PMTG se tipo ordine OI=T09

Si specifica che in caso di tipo attività PM ≠ EPC i campi CBS / CBE contabile saranno nascosti in quanto obsoleti.

## Recupero/alimentazione da fonti esterne (SAP)

Come anticipato nel paragrafo precedente, una parte delle informazioni gestite nel tab “CoIn” dell’OdL sono recuperate da SAP.

Per l’acquisizione di tali dati sono utilizzate le seguenti due interfacce fra i sistemi EAM e SAP:

Interfaccia di acquisizione massiva oggetti economici: recupera il set completo di CBS contabile, CBE contabile, OI, NTK e OdM già presenti in SAP;

Interfaccia di creazione NTK / OdM: nel caso in cui NTK / OdM non fossero già presenti in SAP ne richiede la generazione on-demand con allineamento sincrono fra i due sistemi.

Le interfacce EAM - SAP si collocano nel processo di lavorazione degli OdL come rappresentato in Figura 151. I passaggi evidenziati in verde indicano le operazioni eseguite in EAM, mentre quelli in blu rappresentano gli step logici che coinvolgono SAP.

Figura 151 - Processo di creazione NTK / OdM e interfaccia EAM - SAP

Nei paragrafi successivi sono riportati i dettagli di entrambe le interfacce.

Interfaccia di acquisizione massiva oggetti economici SAP

La valorizzazione delle informazioni relative agli oggetti contabili avviene tramite l’interfaccia di acquisizione massiva da SAP. Di seguito sono descritte le logiche di acquisizione per ciascun campo del tracciato:

CBS / CBE contabile: L'oggetto può essere acquisito su EAM se si trova in fase di apertura (stato APER), di rilascio/autorizzazione (stato RIL / AUTO) su SAP. Il filtro sullo stato verrà applicato soltanto alla prima sincronizzazione tra i due sistemi. A seguito della prima esecuzione il filtro sullo stato verrà eliminato in modo da poter registrare i passaggi di stato successivi. Le logiche principali di acquisizione sono:

CAPEX: Filtra le CBS / CBE contabili valide che hanno profit center che termina con i caratteri "LIN";

ANR: Filtra le CBS / CBE contabili valide che hanno profit center che termina con “EPC”.

Si precisa che le CBS/CBE di pianificazione saranno configurate direttamente in EAM e non acquisite tramite interfaccia

Ordine Interno (OI): L’elenco di OI attivi (in stato “Aperto”, “Rilasciato”) in SAP selezionabili è definito in base al tipo di ordine OI (T06, T09, T02. Il filtro sullo stato verrà applicato soltanto alla prima sincronizzazione tra i due sistemi. A seguito della prima esecuzione il filtro sullo stato verrà eliminato in modo da poter registrare i passaggi di stato successivi.

Ordine di manutenzione (OdM): L’interfaccia deve acquisire tutti gli OdM (in stato APER, RIL, TECO) associati a tutte le combinazioni ammissibili di:

Tipo attività PM;

Tipo ordine.

Il filtro sullo stato verrà applicato soltanto alla prima sincronizzazione tra i due sistemi. A seguito della prima esecuzione il filtro sullo stato verrà eliminato in modo da poter registrare i passaggi di stato successivi.

Network (NTK): L’interfaccia deve acquisire:

tutte le NTK in stato diverso da CONC (conclusa) e BLOC (bloccata), associate a CBE nel perimetro descritto nel primo bullet.

Le operazioni associate alle NTK del bullet precedente.

Il filtro sullo stato verrà applicato soltanto alla prima sincronizzazione tra i due sistemi. A seguito della prima esecuzione il filtro sullo stato verrà eliminato in modo da poter registrare i passaggi di stato successivi.

L’interfaccia di allineamento massiva ha una frequenza di allineamento oraria (1 volta / h).

Interfaccia di creazione NTK / OdM

Nel caso in cui la NTK o l’OdM da associare all’OdL non sia già stato definito in SAP (e quindi acquisito tramite interfaccia massiva) è possibile richiederne la generazione tramite apposita funzionalità. Tale generazione è effettuabile in qualsiasi momento precedente all’invio dei consuntivi verso SAP.

La funzionalità di creazione di OdM / NTK viene innescata manualmente tramite il tasto “Richiesta creazione in SAP”, il quale risulta cliccabile solo se il campo identificativo OdM / NTK è vuoto.

Nel caso in cui la richiesta di creazione non vada a buon fine viene generato un warning per informare l’utente circa il KO. Se invece la generazione va a buon fine, viene visualizzato automaticamente in EAM il codice dell’OdM / NTK generato nel campo “Identificativo OdM” / “Identificativo NTK”.

Nel caso in cui sia necessario modificare l’OdM / NTK associato all’OdL è necessario:

Utilizzare la funzionalità “Disassocia NTK / OdM” (innescata dal relativo pulsante) la quale rimuove il legame tra OdL e NTK/OdM e contestualmente de-valorizza il campo “Identificativo NTK / OdM”. La funzionalità è utilizzabile solo se non sono presenti consuntivi inviati verso SAP;

Utilizzare la funzionalità “Richiesta creazione in SAP” per associare un nuovo OdM / NTK.

Di seguito, in Figura 152, è presente una vista degli elementi descritti a sistema:

Figura 152 - Schermata tab Coin

## Pianificazione costi risorse esterne, interne

Una volta generati gli OdL, si procede alla pianificazione delle risorse interne ed esterne. I dati relativi alla pianificazione delle ore e dei costi delle risorse interne e dei costi delle risorse esterne si trovano nel tab “Pianificazione” dell’OdL. In particolare, i costi e le ore pianificate per ciascuna risorsa interna sono visualizzati nella tabella “Labor” (Figura 153), che presenta le seguenti informazioni:

Qualifica: figura professionale associata alla risorsa (Operaio, Quadro, Impiegato);

Quantità: numero delle risorse impiegate con la medesima qualifica (ad. Es. numero degli Operai impiegati);

Ore pre-esecuzione: ore impiegate alla preparazione dell’OdL;

Ore esecuzione: ore effettive di lavoro sul campo.

Ore trasferimento: ore necessarie al raggiungimento e al rientro dal luogo di svolgimento dell’OdL;

Ore post esecuzione: ore impiegate dopo l’esecuzione dell’OdL.

Titris: titolare risorsa che esegue l’attività;

CMO: costo medio orario della risorsa;

Data inizio risorsa: rappresenta la data in cui si pianifica di iniziare ad utilizzare la risorsa;

Data fine risorsa: rappresenta la data in cui si pianifica il termine di utilizzo della risorsa;

Figura 153 - Tabella "Labor"

Le informazioni delle risorse esterne sono visualizzate nella tabella “Risorse esterne” (Figura 154) all’interno della quale sono presenti le seguenti colonne:

ID Materiale: Campo libero valorizzato manualmente dall’utente. Sarà possibile filtrare l’elenco di valori ammissibili per “Tipo risorsa”.

Tipo risorsa: “Prestazioni terze” o “Materiale”; campo popolato automaticamente in base all’ID materiale precedentemente inserito

Descrizione ID materiale: Campo libero valorizzato manualmente dall’utente.

Gruppo merci; campo popolato automaticamente in base all’ID materiale precedentemente inserito

Descrizione gruppo merci: campo popolato automaticamente in base all’ID materiale precedentemente inserito

Conto: campo popolato automaticamente in base all’ID materiale precedentemente inserito

Descrizione conto: campo popolato automaticamente in base all’ID materiale precedentemente inserito

Unità di misura: campo popolato automaticamente in base all’ID materiale precedentemente inserito

Importo unitario: popolato automaticamente in base all’ID materiale precedentemente inserito

Quantità: campo numerico valorizzato manualmente dall’utente. Se il pdl associato all’odl presenta delle risorse esterne con una quantità definita, allora alla creazione dell’odl il campo “Quantità” risulta prepopolato con quanto riportato nel pdl; successivamente è comunque modificabile manualmente

Importo totale: campo numerico valorizzato manualmente dall’utente. Se il pdl associato all’odl presenta delle risorse esterne con una quantità e un importo definiti, allora alla creazione dell’odl il campo “Importo totale” risulta prepopolato con un valore pari a “quantità*importo”; successivamente è comunque modificabile manualmente

Data inizio;

Data fine;

Si specifica che il tab delle “Risorse esterne” è visibile solo per gli OdL con tipo spesa OPEX e ANR.

Figura 154 - Tabella "Risorse esterne"

Le informazioni dei ricavi sono visualizzate nella tabella “Ricavi” (Figura 155) all’interno della quale sono presenti le seguenti colonne:

ID ricavo: campo popolato dall’utente tramite una lista valori

Tipo risorsa: “Ricavo”; campo popolato automaticamente in base all’ID ricavo inserito

Conto: campo popolato automaticamente in base all’ID ricavo inserito

Importo: campo numerico valorizzato manualmente dall’utente

Data inizio

Data fine

Si specifica che il tab dei “Ricavi” è visibile solo per gli OdL con tipo spesa ANR.

Figura 155 - Tabella "Ricavi"

Il catalogo dove vengono censite le informazioni relative alle risorse esterne e ai ricavi viene gestito all’interno dell’applicazione “Catalogo Risorse Esterne - Ricavi” (par. 2.2.9).

Di seguito vengono descritti gli step del processo di inserimento dell’effort per le risorse interne ed esterne:

Generazione OdL da DSS o manuale: L’OdL eredita le ore standard necessarie allo svolgimento dell’attività dal PdL in termini di Quadri, Impiegati ed Operai. I dati del tab “labor” vengono inizializzati alla generazione dell’OdL come segue:

Metodo: Campo predisposto per successivo rilascio, ma al momento non popolato/utilizzato;

Qualifica: ereditato dal PdL di riferimento;

Quantità: ereditato dal PdL di riferimento (catalogo attività);

Ore pre-esecuzione: ereditate dal catalogo del PdL se disponibili;

Ore esecuzione: ereditate dal catalogo del PdL;

Ore trasferimento: pre-valorizzato sulla base della tabella di Origine/Destinazione presente a sistema;

Ore post esecuzione: ereditate dal catalogo del PdL se disponibili;

Titris: inizialmente uguale al responsabile attività;

CMO: pre-valorizzata sulla base delle tariffe già presenti a sistema nell’applicazione “Gestione tariffe”, dettagliata nel paragrafo 11.4. Vengono recuperati i CMO relativi a:

Anno (recuperato dalle date di inizio e fine risorsa)

Figura professionale e Titris della risorsa

Nel caso in cui le date di inizio e fine risorsa fossero a cavallo anno o mese si considerano le tariffe del mese/anno della data di inizio risorsa.

Data inizio: uguale a data inizio pianificata OdL

Data fine risorsa: uguale a data fine pianificata OdL.

Per quanto concerne il tab “Risorse esterne” viene anch’esso pre-valorizzato (solamente per OdL OPEX-ANR) sulla base di quanto contenuto all’interno del PdL ed in più viene applicata la seguente logica:

Pianificazione risorse interne ed esterne: L’utente procede, se necessario, con la modifica dei dati pre-valorizzati. In particolare, sarà possibile sia modificare/eliminare le informazioni presenti che inserire nuove righe all’interno della tabella per aggiungere ulteriori Titris piuttosto che nuove risorse esterne. I campi della tabella sono modificabili come segue:

Metodo: Campo predisposto per successivo rilascio, ma al momento non popolato/utilizzato;

Qualifica: non modificabile;

Quantità: modificabile inserendo il numero di risorse impiegate;

Ore pre-esecuzione: campo non valorizzabile. Impostato per successiva wave;

Ore esecuzione: modificabile manualmente inserendo il numero di ore necessarie;

Ore trasferimento: modificabile manualmente inserendo il numero di ore necessarie;

Ore post-esecuzione: campo non valorizzabile. Impostato per successiva wave;

Titris: Modificabile selezionando da una lista (contente tutte le UdB censite a sistema) un Titris diverso dal responsabile attività (impostato di default al momento della creazione dell’OdL). Al fine di agevolare la ricerca di uno specifico Titris, sopra la lista è presente un filtro di testo all’interno del quale è possibile inserire la parola o i caratteri per i quali filtrare la lista. Si specifica che l’inserimento di Titris diversi dal responsabile attività è possibile solo se definiti nella tabella “ore in entrata” della maschera di scambio intracompany (cfr. par. 5.1.2) e per un numero massimo di ore pari a quanto concordato;

CMO: non modificabile;

Data inizio e fine risorsa: campi modificabili. La data di inizio risorsa non potrà mai essere precedente alla data di inizio pianificata dell’OdL, mentre la data di fine risorsa non potrà mai essere successiva alla data di fine pianificata per l’OdL. Per questi campi valgono la seguente logiche di gestione, valide per tutti i casi di tipo spesa dell’OdL (OPEX, ANR, CAPEX):

l’utente decide se mantenere o modificare le date pre-valorizzate dal sistema;

l’utente decide di dividere le ore di impiegati, quadri e operai in diverse finestre temporali. Es: viene generato un OdL con data inizio pianificazione 01/01/2025 e data fine pianificazione 01/06/2025 e l’OdL richiede un’attività preparatoria nel mese di Gennaio e un’attività di realizzazione nel mese di Giugno. Prendendo come esempio la risorsa “impiegato”, bisogna specificare nel record pre-esistente che l’impiegato è richiesto dal 01/01/2025 al 31/01/2025; successivamente si aggiunge un altro record in cui si specifica che l’impiegato è richiesto dal 01/06/2025 al 30/06/2025. Sarà a cura dell’utente fare in modo che la minima data di inizio risorsa corrisponda alla data di inizio pianificazione dell’OdL e che la massima data di fine risorsa sia uguale alla data di fine pianificazione dell’OdL. Nel caso in cui non venga rispettato questo vincolo il sistema non permette il salvataggio e restituisce il seguente messaggio di errore:

“Controllare che ci sia:

Almeno una data di inizio risorsa che coincida con la data di inizio pianificata dell’OdL

Almeno una data di fine risorsa che coincida con la data di fine pianificata dell’OdL”

Risorse esterne: il tab “Risorse esterne” è modificabile liberamente

Ricavi: Per il tab “Ricavi” il popolamento delle informazioni sarà sempre manuale.

L’inserimento di un nuovo record nella tabella “Labor” viene effettuato utilizzando il pulsante “+”. Una volta fatto ciò l’utente compila i seguenti campi:

Metodo: Campo predisposto per successivo rilascio, ma al momento non popolato/utilizzato;

Qualifica: selezionabile dalla lista di valori: Operaio, Impiegato, Quadro;

Quantità: valore numerico intero inseribile liberamente;

Ore pre-esecuzione: valore numerico inseribile liberamente;

Ore esecuzione: valore numerico inseribile liberamente;

Ore trasferimento: pre-valorizzato sulla base della tabella di Origine/Destinazione presente a sistema;

Ore post esecuzione: valore numerico inseribile liberamente;

Titris: Pre-valorizzato con il responsabile attività e successivamente modificabile con le logiche precedentemente descritte.

CMO: Recuperata, al salvataggio, dalla tabella di gestione delle tariffe in base al Titris selezionato;

Data inizio e fine risorsa: valori in formato data. Sarà a cura dell’utente fare in modo che la minima data di inizio risorsa corrisponda alla data di inizio pianificazione dell’OdL e che la massima data di fine risorsa sia uguale alla data di fine pianificazione dell’OdL. Nel caso in cui i vincoli descritti non fossero rispettati, il sistema non permetterà il salvataggio.

Similmente a quanto descritto per la tabella “Labor”, anche nelle tabelle “Risorse esterne” e “Ricavi” è possibile inserire manualmente un nuovo record tramite il pulsante “+”.

## Gestione delle tariffe

Le tariffe (CMO) saranno gestite tramite un’applicazione dedicata chiamata «Gestione tariffe».

Come rappresentato nella Figura 156, le tariffe (Costo medio orario) sono suddivise per Unità di budget, Anno, Scenario e Figura professionale (operaio, impiegato, quadro). La tabella delle tariffe è visualizzabile e editabile solo dagli utenti centrali.

Figura 156 - Vista elenco tariffe

In modo analogo a quanto previsto per la gestione della capacity, per editare le tariffe sono previste tre modalità:

Import massivo da Tagetik: una volta cliccato su questo tasto viene aperta una dialog all’interno della quale bisogna specificare lo scenario di riferimento. In questo modo vengono popolati automaticamente i record relativi allo scenario specificato per tutte le UdB;

Caricamento di un file Excel tramite MX Loader (vedere glossario);

Inserimento manuale da parte di un utente centrale che può modificare puntualmente il CMO di qualsiasi record.

Le tariffe sono impiegate per calcolare il valore economico dell’effort orario degli ordini in fase di pianificazione. Per gli OdL generati dal DSS, le tariffe utilizzate per il calcolo dell’effort economico corrispondono a quelle dell’UdB definita dal Titris. In caso di modifica del Titris nell’OdL, il sistema aggiorna la tariffa e il costo totale al momento del salvataggio. Per gli OdL manuali, invece, una volta associato il PdL e scelto il Titris, il sistema associa la tariffa dell’UdB corrispondente al salvataggio.

Nel caso di modifica della tabella contente le tariffe, il sistema aggiorna gli OdL presenti a sistema con i nuovi costi. In particolare, l’aggiornamento avviene solo sugli OdL che non presentano consuntivi inviati a SAP e il cui stato è diverso da “NON VALIDATO”, “NON ESEGUITO”, “CHIUSO”.

## Visualizzazione costi personale interno e risorse esterne

Il sistema recupera i costi associati al personale interno e alle risorse esterne utilizzate per l’esecuzione dell’attività tramite interfaccia dedicata che li recupera dalla tabella di staging SAP (MV_TGK_STAGING_CECAPEX) senza vincoli rispetto allo stato dell'OdL (il dato può essere ricevuto anche successivamente alla chiusura dell'OdL). I suddetti costi verranno visualizzati nel tab “Avanzamento”, nella tabella “Costi risorse interne / risorse esterne”, come illustrato nella figura 157.

Si specifica che nel caso dei costi del personale interno (quadri, impiegati, operai) e risorse esterne, i campi relativi ai costi del personale interno possono essere sempre valorizzati e vengono recuperati dall’OdM o dalla NTK associata all’OdL. Nel caso sia presente un OdM, si stabilisce una corrispondenza 1:1 con l’OdL.

Nel caso di NTK, per il caso del personale interno si stabilisce una corrispondenza 1:1 con le operazioni della NTK, da cui è sempre possibile recuperare i costi delle risorse interne. Nel caso, invece, di risorse esterne, i costi relativi alle risorse vengono imputati su operazioni che non sono riconducibili all’OdL, pertanto, in questa situazione, tali costi non sono visualizzabili

Figura 157 - Tab "Avanzamento" dell'applicazione "Monitoraggio OdL"

La tabella Costi risorse interne / esterne riporta su ogni riga il valore totale di “Quantità” e “Importo” relativo alle combinazioni degli altri campi presenti in tabella.

I campi che compongono la tabella sono descritti come segue:

Esercizio: anno contabile di riferimento

Periodo: periodo contabile di riferimento

Attività: macroarea (attività) codice CoIn associato all’OdL

Centro di costo responsabile: Centro di costo responsabile

Descrizione CdC: Descrizione del centro di costo responsabile, recuperate da una tabella contenente il mapping Codice CdC – descrizione fornita fuorilinea dal BSN e caricata manualmente a sistema

Cliente: Cliente associato all’oggetto contabile

Tipo oggetto contabile: Tipo di oggetto contabile (valorizzato con “ORD” per OdM e “WBS” per NTK)

Gruppo merci: Gruppo merci

Descrizione Gruppo merci: Descrizione del rispettivo gruppo merci recuperato dal catalogo contenente le risorse esterne censite a sistema

Tipo ordine: Tipo ordine

Risorsa interna / esterna: Codice risorse interna / esterna (nel caso di Risorse interne il codice sarà troncato al “/” in modo da visualizzare solo H_OPE / H_IMP / H_QUA)

TITRIS: Codice del Titris

Descrizione Titris: Descrizione del titolare risorse

Unità: unità di misura dell’importo

Conto costi esterni Tagetik: Conto di riferimento della voce di costo

Quantità: Quantità associata al costo

Importo: Importo economico del costo

# Esecuzione OdL

## Processo di esecuzione OdL

In questo capitolo è descritto il processo di esecuzione degli OdL che si differenzia a seconda del Titris associato all’OdL in fase di pianificazione. In particolare, è possibile individuare le tre seguenti casistiche:

Titris 100% UI: flusso OdL figli, flusso OdL aggregatore o singolo;

Titris 100% non UI;

Titris misti.

A seconda della casistica fra quelle elencate, il processo prevede stati, integrazioni e azioni utente differenti.

Nei paragrafi successivi vengono descritte le peculiarità per ogni tipologia di Titris.

Processo OdL UI

#### Flusso OdL sottostanti l’aggregatore (operazioni in Geocall)

| Stato EAM | Stato Geocall | Evento OdL singolo |
| --- | --- | --- |
| Da Validare | - | OdL generato da DSS in caso di policy “on-condition”, deve essere validato dall’utente per passare a stato “Aperto”. |
| Aperto | - | OdL pianificabile in EAM. È possibile vestire con i dati di pianificazione economica ed effettuare la pianificazione tecnica (tramite ottimizzatore o manualmente). Gli OdL creati manualmente si trovano già in questo stato. |
| Schedulabile | Schedulabile | Stato impostato dal sistema quando l'OdL padre viene portato in stato "Schedulabile" (su tutti gli ordini figli contemporaneamente). In caso di errore di mancata creazione OdL da parte di Geocall, tutti gli OdL figli e l’OdL padre tornano in stato “Aperto”. In caso di deschedulazione (dopo che il piano giornaliero è stato convalidato) da parte di Geocall, l’ordine figlio in EAM regredisce in “schedulabile”. |
| Schedulato | Fissato | Stato impostato su input di Geocall, quando viene effettuata la schedulazione definitiva (Convalida del piano giornaliero, stato dell’operazione “Fissato”). Dato che lato Geocall l’ordine aggregatore verrà gestito “ordine ad ordine”, gli OdL figli passeranno tutti nello stesso momento nello stato “Schedulato”. Tramite il flusso si ricevono anche le date di schedulazione dell'OdL e l'informazione delle risorse a cui è stato assegnato. |
| Inoltrato | Inoltrato | Stato impostato su input di Geocall, quando viene effettuata la sincronizzazione sull’applicazione mobile di Geocall. Da questo momento gli OdL saranno visibili anche su EAM mobile per le risorse assegnatarie, è possibile esitare micro-controlli e rilevare difformità (se presenti). |
| Avviato | In carico | Stato impostato su input di Geocall quando l'operazione passa in stato "in carico". Lo stato “in carico” può essere ricevuto anche dopo l'avvio dell'attività su EAM. Se l’ordine è già stato avviato (passaggio in stato “Eseguito parzialmente”) anche lato EAM e sono già stati registrati dei micro-controlli/difformità la ricezione del flusso da Geocall non avrà alcun effetto sullo stato dell’Ordine EAM. È a partire da questo stato che saranno ricevuti i consuntivi da Geocall e che quindi potranno essere inviati verso SAP. I consuntivi da Geocall arrivano solo dopo la loro convalida. |
| Eseguito parzialmente | - | Impostato dal sistema se viene eseguito un micro-controllo oppure viene inserita una difformità (se previsto). Se non sono gestiti né micro-controlli né possibilità di registrare difformità, l'OdL non transiterà per questo stato. |
| Eseguito | - | Impostato manualmente dall'utente quando è conclusa l'esecuzione su EAM. |
| Chiuso | Chiuso | Impostato in automatico dal sistema: Se l’ordine è già in stato eseguito, quando arriva da Geocall l’aggiornamento dello stato in “Chiuso”; Se è già arrivato l’aggiornamento da Geocall che l’ordine è in stato “Chiuso”, quando l’utente su EAM imposta lo stato “Eseguito”. Una volta chiuso l’OdL in EAM viene inviato a SAP un flusso di chiusura dell’OdM corrispondente. |
| Non Eseguito | - | Stato impostato automaticamente dal sistema se l'OdL supera la data di massimo posticipo mentre si trova in stato "Aperto". Impostato automaticamente dal sistema se si riceve su un’operazione da Geocall lo stato "chiuso" con esito negativo. Non può essere impostato manualmente, finché l’ordine è associato ad un padre aggregatore. |
| Non Validato | - | Stato impostato manualmente dall'utente. |

#### Flusso OdL aggregati e singoli

La freccia blu che torna indietro da “schedulabile” e “schedulato” rappresenta la funzionalità di “Ripianificazione”.

Questa funzionalità è disponibile fino a che l’OdL si trova in uno stato precedente allo stato “inoltrato” e se non sono presenti consuntivazioni. Tramite questa funzionalità, l'OdL viene riportato in stato "Aperto" (contestualmente viene annullato su Geocall). Quando l’utente preme il tasto di ripianificazione, il sistema propone un messaggio di warning in cui si chiede conferma all’utente di voler proseguire con l’azione: “Attenzione, l’ordine verrà ripianificato e quindi riportato in stato “Aperto”. In stato “Aperto” è possibile:

Modificare il PdL;

Modifica delle date di pianificazione;

Sganciare/agganciare OdL sottostanti (nel caso di aggregatori).

Quando l'OdL viene riportato in stato "Schedulabile" in EAM, su Geocall viene creato un nuovo OdL con il codice esterno riferito a quello dell’OdL di EAM.

Nel caso degli OdL aggregatori, sarà visualizzato in GUI lo stato delle operazioni Geocall (colonna stato Geocall della tabella successiva) per tutti gli OdL figli (non popolato dal DSS).

| Stato EAM | Stato Geocall | Evento OdL singolo |
| --- | --- | --- |
| Da Validare | - | Stato impostato dal sistema all'atto della generazione dell'OdL se di tipo on condition (solo per OdL singoli). |
| Aperto | - | OdL pianificabile in EAM. È possibile vestire con i dati di pianificazione economica ed effettuare la pianificazione tecnica (tramite ottimizzatore o manualmente). |
| Schedulabile | Schedulabile | Impostato dall'utente, da questo momento vengono inviati i dati dell'OdL (e di tutti gli OdL sottostanti, nel caso di OdL aggregatore) verso Geocall. In conseguenza di questo cambio stato anche lo stato di tutti gli OdL sottostanti un aggregatore passa in "schedulabile". Se all’interno dell’aggregatore non sono presenti ordini figli, non è possibile portarlo in schedulabile. |
| Schedulato | - | Stato che assume l'OdL aggregatore se almeno uno degli OdL sottostanti è in stato "schedulato" lato EAM (il modello scelto lato WFM prevede che la schedulazione dell’OdL aggregatore sia ad Ordine e non ad Operazione quindi gli OdL sottostanti saranno schedulati tutti assieme). Stato che assume l'OdL singolo se l'operazione corrispondente è in stato "fissato" su Geocall. |
| Inviato | Inoltrato | Stato che assume l'OdL aggregatore se almeno uno degli OdL sottostanti è in stato "inoltrato". Su Geocall, dato che l’ordine aggregato è gestito “ordine ad ordine”, le operazioni (corrispondenti agli ordini figli) vengono inoltrate tutte simultaneamente. Stato che assume l'OdL singolo se l'operazione corrispondente è in stato "inoltrato". Da questo momento gli OdL saranno visibili su EAM mobile per le risorse assegnatarie e sarà possibile esitare micro-controlli e rilevare difformità (se presenti). |
| Avviato | In carico | Stato assunto dall'OdL aggregatore se almeno uno degli OdL sottostanti è in stato "avviato" (“in carico” lato Geocall). Stato assunto dall'OdL singolo se l'operazione corrispondente è in stato "in carico" lato Geocall. Da questo stato l’odl riceverà consuntivi orari da parte di Geocall e una volta ricevuti potranno essere inviati a SAP. |
| Eseguito parzialmente | - | Stato che assume l'OdL aggregatore se almeno uno degli OdL sottostanti è in stato "Eseguito parzialmente", "Eseguito" o “non eseguito”. In questo stato vengono registrati i fisici parziali corrispondenti agli OdL sottostanti. Stato che assume l'OdL singolo se è stato eseguito almeno un micro-controllo o è stata inserita almeno una difformità sull'OdL. Nel caso non siano previsti micro-controlli o registrazione di difformità, l'OdL non transiterà per questo stato. Da questo stato in poi è possibile validare le difformità registrate. |
| Eseguito | - | Stato che assume l'OdL aggregatore se tutti gli OdL sottostanti sono almeno in stato "Eseguito" e “non eseguito”. Se tutti gli ordini sotto l’aggregatore sono in stato “non eseguito”, l’odl passa in stato “non eseguito”. Per l’OdL singolo, stato impostato dall’utente solo se l'operazione corrispondente è in stato "chiuso" con esito positivo. In questo stato viene registrato il fisico totale per l'OdL. |
| Chiuso | Chiuso | Stato impostato automaticamente dal sistema quando sono soddisfatte le seguenti condizioni: Sono stati inviati a SAP tutti i consuntivi orari; Sono state validate tutte le eventuali difformità rilevate; Conferma dell'aggiornamento dell'anagrafica (solo per processo a 3 punte, attività di tipologia “Sostituzione”, “Nuova installazione” e “Demolizione”); Check patrimoniale EE (solo per processo a 3 punte); Conferma caricamento allegati (solo per processo a 3 punte). è stato ricevuto da Geocall l'aggiornamento sul passaggio di stato dell'OdL in stato "chiuso". |
| Non Eseguito | - | Stato impostato automaticamente dal sistema se l'OdL aggregatore supera la data di massimo posticipo mentre si trova in stato "Aperto". Vengono messi in questo stato anche tutti gli OdL sottostanti. Impostato se tutti gli ordini sottostanti l’aggregatore si trovano in stato “non eseguito” (su input di geocall). Per l’aggregatore può anche essere impostato manualmente dall’utente abilitato tramite area di validazione, in automatico anche tutti i figli passano in “non eseguito”. Per l’OdL singolo, stato impostato manualmente dall’utente abilitato nell’area di validazione o automaticamente quando l’OdL supera la data di massimo posticipo mentre si trova in stato “Aperto”. Impostato su input di Geocall nel caso in cui l’operazione sottostante sia stata chiusa con esito negativo. Impostato dal sistema nel caso in cui venga generato un ordine prevalente rispetto all’ordine in questione. |
| Non Validato | - | Impostato manualmente dall'utente. Stato finale, l'OdL non può essere riportato in stato aperto. |

Processo OdL non UI

| Stato EAM | Stato Geocall | Evento OdL singolo |
| --- | --- | --- |
| Da Validare | - | OdL generato da DSS in caso di policy “on-condition”. |
| Aperto | - | OdL pianificabile in EAM. È possibile vestire con i dati di pianificazione economica ed effettuare la pianificazione tecnica (tramite ottimizzatore o manualmente). |
| Eseguibile | - | Stato impostato dall'utente quando sono concluse le attività di pianificazione e l'OdL può essere eseguito. |
| Eseguito parzialmente | - | Per l’OdL aggregatore impostato dal sistema se almeno uno degli OdL figli è in stato “eseguito parzialmente” o “eseguito”. Per l’OdL singolo, quando viene inserito almeno un consuntivo. Per gli OdL aggregatori, in questo stato vengono conteggiati i fisici parziali corrispondenti agli OdL sottostanti che si trovano in stato “Eseguito”. Da questo momento è possibile inviare i consuntivi parziali verso SAP ed è considerato il fisico parziale degli OdL sottostanti un aggregatore. |
| Eseguito | - | Per gli OdL aggregatori è impostato dal sistema quando tutti gli OdL sottostanti sono in stato "Eseguito". Gli OdL sottostanti un aggregatore o gli OdL singoli sono portati in stato "Eseguito" dagli utenti quando non devono più essere registrati consuntivi e quando l'attività è stata eseguita. Da questo momento viene conteggiato il fisico totale dell'Ordine. |
| Chiuso | - | Stato impostato automaticamente dal sistema quando sono soddisfatte le seguenti condizioni: Sono stati inviati a SAP tutti i consuntivi orari; Conferma dell'aggiornamento dell'anagrafica (solo per processo a 3 punte); Conferma caricamento allegati (solo per processo a 3 punte). Una volta chiuso l’OdL in EAM viene inviato a SAP un flusso di chiusura dell’OdM corrispondente. |
| Non Eseguito | - | Stato impostato manualmente dall'utente abilitato, nell’area di validazione, o automaticamente dal sistema se l'OdL supera la data di massimo posticipo mentre si trova in stato "Aperto". |
| Non Validato | - | Impostato manualmente dall'utente. Stato finale, l'OdL non può essere riportato in stato aperto. |

Processo OdL Misto

| Stato EAM | Stato Geocall | Evento OdL singolo |
| --- | --- | --- |
| Da Validare | - | OdL generato da DSS in caso di policy “on-condition”. |
| Aperto | - | OdL pianificabile in EAM. È possibile vestire con i dati di pianificazione economica ed effettuare la pianificazione tecnica (tramite ottimizzatore o manualmente). |
| Schedulabile | - | Stato impostato dall'utente quando sono concluse le attività di pianificazione e l'OdL può essere eseguito. Da questo momento l'OdL viene inviato verso Geocall solo con le informazioni relative ai TitRis UI. |
| Eseguito parzialmente | - | Impostato dal sistema all'inserimento del primo consuntivo relativamente ai TitRis non UI su EAM o alla ricezione del primo consuntivo da Geocall relativamente ai TitRis UI. Da questo momento è possibile inviare i consuntivi parziali verso SAP. |
| Eseguito | - | Impostato manualmente dall'utente su EAM, quando è conclusa l'attività e non devono essere più inseriti consuntivi relativamente ai TitRis non UI. |
| Chiuso | Chiuso | Stato impostato automaticamente quando sono soddisfatte le seguenti condizioni: Sono stati inviati a SAP tutti i consuntivi orari; È stata ricevuto il cambio stato in “chiuso” da Geocall. Conferma dell'aggiornamento dell'anagrafica (solo per processo a 3 punte); Conferma caricamento allegati (solo per processo a 3 punte). Una volta chiuso l’OdL in EAM viene inviato a SAP un flusso di chiusura dell’OdM corrispondente. |
| Non Eseguito | - | Stato impostato dall'utente abilitato, nell’area di validazione, o automaticamente dal sistema se l'OdL supera la data di massimo posticipo mentre si trova in stato "Aperto". |
| Non Validato | - | Impostato manualmente dall'utente. Stato finale, l'OdL non può essere riportato in stato aperto. |

## Integrazione con Geocall

In questo capitolo viene descritta l’interfaccia fra EAM e Geocall, utilizzata per gestire l’invio degli OdL per la schedulazione e l’allineamento dei dati fra i due sistemi nelle fasi di schedulazione / esecuzione.

L’invio delle informazioni dell’OdL a Geocall per la schedulazione viene in automatico una volta che l’utente passa l’OdL in stato “schedulabile”. Per passare in stato “schedulabile”, devono essere valorizzati obbligatoriamente i seguenti campi:

Codice OdL (codice creato da EAM al momento della generazione dell’OdL);

Codice PdL associato;

Dati contabili (solo il Tipo spesa);

Durata attività;

Descrizione OdL;

Data inizio pianificazione;

Data fine pianificazione;

Codice asset;

Tipologia assistenza/sorveglianza;

Titris Risorse (solo se Titris è UI);

Effort risorse (operai, impiegati, quadri).

Di seguito sono riportati i campi inviati verso Geocall:

Codice OdL;

Codice PdL associato;

Descrizione OdL;

Titris risorse pianificate;

Data non prima di;

Data di massimo posticipo;

Data inizio pianificata;

Data fine pianificata;

Data inizio indisponibilità;

Data fine indisponibilità;

Stato richiesta indisponibilità;

Codice asset EAM;

SAP ID asset: nel caso l’asset abbia un doppio padre per le ispezioni va considerato il SAP ID specifico della campata in relazione alla linea di riferimento (sulla base del tronco di ispezione relativo all’ordine), per gli altri casi va considerato il SAP ID (radice/1° livello) risulta minore (in riferimento ai primi 7 digit);

Tipo spesa;

Codice delibera indisponibilità (se presente);

Flag indisponibilità.

Se l’ordine si trova in stato schedulabile o schedulato, possono essere modificati tutti i campi relativi all’indisponibilità e il campo Tipo spesa, tutti gli altri campi non sono modificabili. In tutti gli stati successivi allo schedulabile non sono permesse modifiche sui campi inviati verso Geocall. Le modifiche apportate ai campi (dove possibile) saranno inviate automaticamente verso Geocall.

Infine, per quanto riguarda la ricezione delle schedulazioni da Geocall, si specifica che il tab “Assegnazioni” dell’ordine di lavoro sarà aggiornato ogni qualvolta Geocall trasmetterà nuove schedulazioni. Le informazioni di schedulazione ricevute da Geocall sono: Data ricezione, Risorse, Preposto (Geocall comunica chi è il preposto tra le risorse schedulate), Data inizio schedulata, Data fine schedulata

Modalità di invio OdL verso Geocall

L’invio dell’OdL può essere effettuato in due modalità:

Invio puntuale: Utilizzando la funzionalità di inoltro del workflow all’interno dell’OdL (indicato nel riquadro rosso della Figura 158) l’utente accede alla dialog rappresentata nella Figura 158 dove saranno presenti solamente gli stati compatibili con lo stato di lavorazione dell’OdL. L’invio avviene automaticamente dal sistema nel momento in cui l’utente effettua il passaggio dell’OdL dallo stato “Aperto” allo stato “Schedulabile”.

Figura 158 - Invio con cambio stato

Figura 159 - Dialog cambio stato

Invio massivo: invio eseguito dalla pagina di “Monitoraggio OdL” tramite la funzionalità “Invio a schedulazione” che permette di selezionare tramite dialog gli OdL in stato “Aperto” per i quali si desidera effettuare l’invio a Geocall. Lo stato degli OdL inviati a Geocall viene aggiornato automaticamente dal sistema a “Schedulabile”. La dialog mostrata dalla funzionalità conterrà solamente OdL con Titris 100% UI e quelli con Titris misto che risultano schedulabili (Figura 160).

Figura 160 - Vista elenco dell'applicazione "Monitoraggio OdL" e funzionalità di invio massivo verso Geocall

Figura 161 - Invio massivo a Geocall

I campi presenti nella dialog di invio massivo verso geocall sono:

OdL;

Descrizione;

Data inizio pianificata;

Data fine pianificata;

Data Modello;

Codice Coin;

SAPID Asset;

Descrizione asset primo livello;

Descrizione asset terzo livello;

PdL.

All’apertura della dialog vengono mostrati esclusivamente tramite prefiltro gli ordini con data pianificata fine all’interno dell’anno corrente.

I dati inviati verso Geocall nel momento del passaggio di stato in “schedulabile” sono riportati di seguito:

Codice OdL;

Codice PdL associato;

Descrizione OdL;

Titris risorse pianificate;

Data non prima di;

Data di massimo posticipo;

Data inizio pianificata;

Data fine pianificata;

Codice asset EAM;

SAP ID asset: nel caso l’asset abbia un doppio padre per le ispezioni va considerato il SAP ID specifico della campata in relazione alla linea di riferimento (sulla base del tronco di ispezione relativo all’ordine), per gli altri casi va considerato il SAP ID (radice/1° livello) risulta minore (in riferimento ai primi 7 digit);

Tipo spesa;

Codice delibera indisponibilità (se presente);

Flag indisponibilità.

Responsabile asset, utilizzato da Geocall per effettuare l’assegnazione dell’ordine.

Nel caso di OdL aggregatori, prima di effettuare l’invio il sistema verifica la coerenza fra il “responsabile asset” fra gli OdL figli e dei figli rispetto al padre, bloccando l’operazione, con messaggio di errore parlante “responsabile asset incoerente tra figli o con padre su ordini aggregatori:xxx,yyy…”, nel caso in cui questi siano discordanti.. Si specifica che, in caso di invio massivo verso Geocall, il sistema elaborerà tutti gli OdL: gli ordini che rispettano le condizioni verranno regolarmente inviati, mentre quelli non conformi saranno esclusi. Al termine del processo, un messaggio riepilogativo mostrerà l'errore sopra descritto per gli OdL non inviati.

Una volta inviati i dati a Geocall per la creazione dell’ordine in caso di modifiche su EAM (possono essere modificati tutti i campi relativi all’indisponibilità e il campo Tipo spesa, tutti gli altri campi non sono modificabili) il flusso di aggiornamento scatta in automatico per allineare i dati verso Geocall.

## Integrazione con SAP

In questo capitolo vengono descritte le interfacce con SAP dedicate all’invio della consuntivazione dell’OdL, della tabella di intersezione tecnico-patrimoniale e al processo di storno.

Invio / Ricezione consuntivazione oraria

Nella Figura 162 è riportata una rappresentazione delle fasi del processo di esecuzione dell’OdL che implicano un’integrazione con SAP. In particolare, sono riportate le interfacce di scambio dati EAM-SAP relativamente all’invio dei consuntivi orari.

Figura 162 - Integrazioni con SAP

Di seguito si riportano i dati da inviare a SAP, al momento dell’invio dei consuntivi, in base al tipo spesa dell’OdL:

| Tipo spesa OdL | Oggetti contabili | Dati tecnici (per ogni matricola) |
| --- | --- | --- |
| CAPEX | Identificativo NTK Numero operazione | Matricola risorsa Data di esecuzione Ore esecuzione Ore trasferimento Ore in ufficio |
| OPEX | Identificativo OdM Operazione OdM | Matricola risorsa Data di esecuzione Ore esecuzione Ore trasferimento Ore in ufficio Centro di lavoro |
| ANR | Identificativo OdM Operazione OdM | Matricola risorsa Data di esecuzione Ore esecuzione Ore trasferimento Ore in ufficio |

#### Modifica informazioni OdL prima dell’invio verso SAP

Prima dell’invio delle consuntivazioni verso SAP potrebbe essere necessario modificare le informazioni di consuntivazione. Di seguito si riportano i processi di inserimento e modifica delle informazioni di consuntivazione, che si differenziano a seconda che l’OdL sia di tipo UI o meno:

Caso UI:

La consuntivazione viene inserita in Geocall e trasmessa a EAM. Dal momento in cui viene inviata su EAM, lato Geocall i consuntivi saranno bloccati e non potranno essere modificati;

Su EAM non è possibile modificare alcuna informazione di consuntivazione ricevuta da Geocall prima dell’invio verso SAP;

L’utente su EAM invia i consuntivi verso SAP (funzionalità descritta nel dettaglio nel paragrafo successivo);

Nel caso in cui l’invio non vada a buon fine, causa chiusura dell’oggetto contabile su SAP, l’utente in EAM modifica l’oggetto contabile e riesegue l’invio verso SAP. L’utente può cambiare l’oggetto contabile su EAM tramite la funzionalità “Disassocia NTK / OdM”.

Alla chiusura dell’ordine in EAM, viene innescato verso SAP un flusso di chiusura dell’OdM/NTK.

Caso non UI:

La consuntivazione viene effettuata in EAM;

Un’eventuale modifica dei dati di consuntivazione e degli oggetti contabili viene effettuata su EAM;

L’utente su EAM invia i consuntivi verso SAP (funzionalità descritta nel dettaglio nel paragrafo successivo);

Nel caso in cui l’invio non vada a buon fine, causa chiusura dell’oggetto contabile su SAP, l’utente in EAM modifica l’oggetto contabile e riesegue l’invio verso SAP. L’utente può cambiare l’oggetto contabile su EAM tramite la funzionalità “Disassocia NTK / OdM”.

Alla chiusura dell’ordine in EAM, viene innescato verso SAP un flusso di chiusura dell’OdM/NTK.

#### Funzionalità di invio consuntivazione

Tramite l’applicazione “Consuntivazione risorse PE” il responsabile UdB può accedere alla maschera rappresentata in Figura 163 contenente tutte le risorse appartenenti alla propria UdB la cui consuntivazione deve essere ancora inviata verso SAP. Nella schermata sono presenti i seguenti campi:

Matricola

Nome

Competenza

Ordine di lavoro

Tipo spesa

OdM

NTK

Data/ora di inizio

Ore totali

Costo risorsa

Inviato a SAP?

Esito invio

Descrizione errore invio consuntivo

Descrizione errore storno

COIN

Descrizione OdL

CBS di consuntivazione

Descrizione CBS

Nel caso in cui il responsabile UdB volesse visualizzare tutte le risorse (quindi anche quelle la cui consuntivazione è già stata inviata verso SAP) è necessario cliccare su “Query disponibili” e successivamente cliccare su “Tutte le risorse”.

Figura 163 - Applicazione consuntivazione risorse

Per inviare i consuntivi verso SAP è necessario:

Selezionare le risorse tramite checkbox:

Puntualmente, cliccando sulle singole risorse;

Massivamente, cliccando la checkbox in testata tramite la quale viene effettuato un “select all”;

Cliccare sull’azione laterale “invia a SAP”. Affinché l’invio vada a buon fine, devono essere soddisfatte due condizioni:

Periodo contabile aperto: una volta cliccato “invia a SAP” l’interfaccia verifica che il periodo contabile su SAP sia aperto;

Apertura oggetto contabile: una volta cliccato “invia a SAP” l’interfaccia verifica che l’oggetto contabile, precedentemente inserito nel tab coin, sia aperto su SAP;

Se entrambe le condizioni sopra elencate risultano soddisfatte è possibile inviare le consuntivazioni a SAP.

Invece, nel caso in cui almeno una delle due condizioni non risulti soddisfatta, non sarà possibile inviare la consuntivazione delle risorse verso SAP. L’utente riceverà su EAM un messaggio di errore “Invio consuntivazione non riuscito”. Si specifica inoltre che in caso di consuntivazione su CBE non configurate come oggetto di contabilizzazione SAP risponderà con KO.

Una volta andato a buon fine l’invio verso SAP, nel tab “Avanzamento” dell’OdL viene spuntato automaticamente il flag “Inviato a SAP”.

Come introdotto nel cap. 11.1.2.2, solamente per ordini con tipo spesa Opex, al momento dell’invio del primo consuntivo verrà creato automaticamente l’odm, in particolare: se nell’OdL prima dell’invio del consuntivo risulta già valorizzato l’odm, allora l’invio verso SAP non genererà nessun nuovo odm. Quindi il campo “odm” resterà valorizzato con quanto inserito prima dell’invio. Restano valide le condizioni descritte per l’invio OK del consuntivo (apertura periodo ed oggetto contabile).

Se nell’Odl, prima dell’invio del consuntivo non risulta valorizzato il campo odm, allora l’invio del verso SAP genererà un nuovo odm, popolando automaticamente il campo “odm” del tab coin. Anche in questo caso restano valide le condizioni per l’invio OK del consuntivo descritte precedentemente (apertura periodo ed oggetto contabile).

Per gli ordini aggregatori Capex e ANR, i consuntivi con stessa combinazione di matricola, ODL, data inizio (la data è considerata in formato dd/mm/yyyy, escludendo quindi l’orario) e tipologia (in campo/fuori campo) saranno accorpati in un unico invio verso SAP, valorizzato con la somma complessiva delle ore lavorate (es: nell’applicazione consuntivazione risorse sono presenti due consuntivi con stessa data inizio della matricola Mario Rossi sull’odl 1234. Ogni consuntivo ha 2 ore di esecuzionee 2 ore di trasferimento. In questo caso verso SAP verrà veicolato un unico consuntivo per la matricola Mario Rossi sull’odl 1234, con 4 ore di esecuzionee 4 ore di trasferimento). Si specifica che:

Non è prevista nessuna modifica alla struttura e alla cardinalità della schermata di consuntivazione risorse (saranno visibili sempre n consuntivi per la stessa combinazione di matricola-odl-data inizio-tipologia). L’accorpamento avverrà solamente in fase di invio dei consuntivi da EAM a SAP

L’accorpamento sarà valutato sia in fase di invio singolo che di invio massivo da EAM a SAP. Se viene selezionato un solo record di una data combinazione OdL-matricola-data inizio risorsa-tipologia, in fase di invio a SAP verranno accorpati (somma ore) ed inviati tutti i record con stessa combinazione del record selezionato (anche se non visualizzati nella pagina presentata) e non ancora inviati con successo verso SAP. Stesso comportamento se ne vengono selezionati solo 2 di n o anche tutti e n. In particolare:

Se l’utente seleziona 1 o più item con sole ore in campo-> verso SAP verrà veicolato 1 solo item che aggrega tutti i consuntivi con la stessa combinazione di matricola, odl, data inizio e tipologia in campo. La risposta da SAP aggiornerà solo questi item

Se l’utente seleziona 1 o più item con sole ore fuori campo-> verso SAP verrà veicolato 1 solo item che aggrega tutti i consuntivi con la stessa combinazione di matricola, odl, data inizio e tipologia fuori campo. La risposta da SAP aggiornerà solo questi item

Se l’utente seleziona sia item con ore in campo sia item con ore fuori campo-> verso SAP verrà veicolato:

1 item che aggrega tutti i consuntivi con la stessa combinazione di matricola, odl, data inizio e tipologia (una tra in campo o fuori campo). La risposta da SAP aggiornerà solo questi item

1 item che aggrega tutti i consuntivi con la stessa combinazione di matricola, odl, data inizio, e tipologia (la restante rispetto a quella veicolata nel punto precedente). La risposta da SAP sarà un KO per record già caricato a sistema ed aggiornerà solo questi consuntivi. Sarà necessaria la conferma ore in SAP prima di risottomettere l’invio da EAM

In fase di invio verso SAP, tutti i consuntivi che hanno stessa combinazione e che sono oggetto di invio, saranno marcati nel modello dati con uno stesso codice di raggruppamento non visibile a GUI

In fase di ricezione da SAP della risposta alla consuntivazione, saranno aggiornati tutti i record aventi lo stesso codice di raggruppamento del record ricevuto da SAP

Figura 164 - Tab Avanzamento

Nella tabella “Risorse” del tab “Avanzamento” (Figura 164) si riportano le seguenti colonne:

Risorse: matricola risorsa;

Nome: Nome e cognome risorsa;

Inviato a SAP: Flag spuntato automaticamente non appena va a buon fine l’invio della consuntivazione verso SAP, effettuato nell’applicazione “Consuntivazione risorse”;

Check SAP: popolato con “OK” o “KO”; rappresenta l’esito dell’invio consuntivazione

Data inizio: Data di inizio esecuzione dei lavori da parte della risorsa;

Ora di inizio: Ora di inizio esecuzione dei lavori da parte della risorsa;

Data fine: Data di fine esecuzione dei lavori da parte della risorsa

Ora di fine: Ora di fine esecuzione dei lavori da parte della risorsa. Campo popolato manualmente dall’utente

Ore esecuzione;

Ore trasferimento;

Ore in ufficio;

Ore totali: Campo calcolato automaticamente dal sistema come ore esecuzione + ore trasferimento + ore in ufficio.

Errore SAP: rappresenta la descrizione dell’errore restituito da SAP nel caso in cui lo storno parziale vada KO

Storno in corso: Valorizzato con la lettera “P” nel momento in cui la richiesta di storno parziale viene inoltrata a SAP

Titris: riporta il titolare risorsa delle risorse.

Ogni record della tabella appena descritta fa riferimento solamente al lavoro svolto dalla risorsa in una giornata lavorativa. Nel caso di OdL UI la divisione dell’effort per giornate lavorative avviene già in Geocall e quindi EAM riceve la consuntivazione delle risorse già divisa per giornata lavorativa. Invece nel caso di OdL non UI la consuntivazione viene effettuata direttamente in EAM e sarà quindi l’utente a creare un record di consuntivazione della risorsa per ogni giornata lavorativa. In caso di OdL misti (UI e non UI) i record UI che arrivano da Geocall non sono modificabili, mentre i record non UI (che vengono creati in EAM) sono modificabili. Nel caso non UI, l’utente ha la possibilità di effettuare il caricamento massivo dei consuntivi tramite file excel (utilizzando MX Loader), tale funzionalità è utilizzabile anche per caricare i consuntivi non UI nel caso di OdL misti UI / non UI.

Generazione tabella intersezione tecnico-patrimoniale

In qualsiasi fase del processo, nella dialog “Intersezione tecnica patrimoniale”, è possibile generare le tabelle di intersezione tecnico patrimoniale relativamente ai dati in fase di pianificazione e in fase di consuntivazione. Le tabelle riporteranno i valori di variazione fisica degli elementi della rete aggregati per CBS pianificati o consuntivati nell’anno.

Nella dialog saranno presenti due tab uno per la generazione della tabella con i dati di pianificazione e uno per la generazione della tabella con i dati di consuntivazione. Le tab sono rispettivamente nominate:

Tabella tecnico patrimoniale dati pianificati;

Tabella tecnico patrimoniale dati consuntivi.

In ognuna delle tab sarà presente un tasto “Genera tabella” che permette all’utente di generare la tabella tecnico patrimoniale. Tramite il tasto download è possibile scaricare la tabella generata. L’eventuale caricamento della tabella in SAP è manuale.

Di seguito sono riportati i campi della tabella tecnico patrimoniale:

CBS: parametro di aggregazione dei dati degli OdL presenti nella tabella;

Indicatore: elemento della rete per cui è calcolato l’importo, costituito dalla concatenazione dei campi asset (classificazione asset EAM), livello di tensione e tipologia (attributo dell’asset aereo, terrestre, marino);

Unità di misura: unità di misura del fisico (viene ricavata dall’asset);

Importo: valore fisico della variazione;

Mese: mese di riferimento degli interventi;

Anno: anno di riferimento degli interventi.

L’importo della variazione dovrà essere calcolato sulla base del perimetro intervento degli ordini associati all’asset. In particolare, per ogni perimetro intervento va indicato il segno della variazione che il sistema deve considerare nel calcolo dell’importo:

Monitoraggio: non vengono considerati nel calcolo;

Manutenzione: non vengono considerati nel calcolo;

Sostituzione: valutato due volte una con segno negativo una con segno positivo;

Nuove installazioni: segno positivo;

Demolizione: segno negativo;

Pronto intervento: non vengono considerati nel calcolo;

Formazione: non vengono considerati nel calcolo.

Processo di storno

In questo paragrafo si descrivono i processi di storno parziale e totale eseguibili per OdL UI, non UI e misti.

#### Storno parziale UI

Per poter effettuare lo storno parziale di singoli consuntivi orari devono essere soddisfatte le seguenti condizioni:

Consuntivo già validato

Tipologia spesa = OPEX

Nel caso in cui non fossero rispettate le condizioni sopra elencate il sistema restituirà un messaggio di errore “impossibile effettuare storno parziale, verificare che siano presenti consuntivi già validati e che la tipologia spesa sia OPEX”

Di seguito si riporta il processo di esecuzione dello storno parziale:

Figura 165 - Processo storno parziale UI

L’utente nel tab “Avanzamento” dell’OdL esegue lo storno del consuntivo orario di una risorsa tramite il pulsante rappresentato in Figura 166.

Figura 166 - Funzionalità di storno parziale

La richiesta di storno viene inoltrata a SAP

Se l’OdM in SAP si trova in uno stato che impedisce l’esecuzione dello storno allora su EAM è restituito un messaggio di errore in cui viene riportato lo stato dell’OdM in SAP che ha causato il KO della richiesta. In questo caso l’utente cambierà manualmente lo stato dell’OdM in SAP (portandolo in uno stato idoneo) per poi eseguire nuovamente lo storno in EAM. Al contrario se l’OdM si trova in uno stato idoneo si procede con lo storno delle ore in SAP e successivamente in EAM

Lo storno viene comunicato a Geocall

Si specifica che:

Non sono necessari controlli sullo stato dell’OdL e sull’esistenza dell’OdM

Lo storno parziale non comporta uno cambio stato dell’OdL

Lo storno parziale comporta l’azzeramento totale del consuntivo di una risorsa. Esempio: se per una determinata risorsa sono state consuntivate 10 ore, lo storno parziale porterà ad un azzeramento totale delle ore che quindi diventeranno pari a zero. Non è dunque possibile stornare una quantità maggiore di quella consuntivata.

Nel caso in cui si dovesse stornare più del 70% dell’effort:

Se sull’OdL è registrato un fisico allora apparirà un warning non bloccante “attenzione si sta stornando più del 70% del totale”;

Se sull’OdL non è registrato un fisico non apparirà nessun warning.

È possibile effettuare lo storno anche su consuntivazioni afferenti a periodi contabili precedenti rispetto alla data di esecuzione dello storno

In caso di esito negativo dello storno, viene popolato il campo“Errore SAP”, con relativa motivazione dell’errore.

##### Gestione consuntivi KO EAM-SAP tramite storno parziale

La funzionalità di storno parziale può essere utilizzata per la gestione dei consuntivi in KO tra EAM e SAP e per la gestione dei consuntivi non ancora inviati a SAP. Per questa casistica particolare la funzionalità di storno parziale non prevede l’invio del flusso di storno verso SAP, ma esclusivamente su Geocall, in quanto il consuntivo non è stato ancora registrato in S/4. Di conseguenza in questa casistica lo storno parziale è effettuabile per ogni tipo spesa dell’odl. Nell’immagine seguente si riporta il processo di gestione dei consuntivi sopra citati.

#### Storno totale UI

Per poter effettuare lo storno totale dei consuntivi orari e fisici devono essere soddisfatte le seguenti condizioni:

Tipologia spesa = OPEX

Nel caso in cui non fosse rispettata la condizione sopra citata il sistema restituirà un messaggio di errore “impossibile effettuare storno totale, verificare che la tipologia spesa sia OPEX”. Le difformità rilevate / validate (o potenzialmente anche già risolte) rimarranno inalterate, compreso il legame con l'OdL che le ha rilevate.

Di seguito si riporta il processo di esecuzione dello storno totale:

Figura 167 - Processo storno totale UI

L’utente esegue lo storno totale tramite il pulsante rappresentato in Figura 168. Una volta innescata la funzionalità di storno totale il sistema eseguirà un pre-check per verificare se sono state già inviate o meno delle ore a SAP.

Figura 168 - Funzionalità di storno totale

Se non sono presenti consuntivi orari già inviati a SAP il processo di storno prevede i seguenti step:

Storno ore in Geocall: può avere esito OK O KO

Se esito OK:

L’odl in Geocall viene chiuso per storno(operazioni incluse)

In EAM vengono stornate le ore e il fisico. Inoltre, l’OdLviene regredito in stato “Aperto”. Per dare evidenza dello storno totale avvenuto, viene valorizzato automaticamente il campo “Storno totale effettuato” (nel tab “Ordine di lavoro” dell’OdL) con il valore “T”.

Se esito KO: EAM acquisisce l’esito negativo dello storno in Geocall

Se sono presenti consuntivi orari inviati a SAP il processo di storno prevede i seguenti step:

Viene eseguita una simulazione di storno su SAP che può avere esito OK o KO.

Se esito simulazione SAP OK:

Storno ore in Geocall: può avere esito OK o KO

Se storno ore in Geocall OK: l’odl in Geocall viene chiuso per storno (operazioni incluse). Successivamente vengono stornate le ore in SAP e viene chiuso l’odm (portato in stato TECO). Infine in EAM vengono stornate le ore e il fisico; l’odl viene regredito in stato “Aperto”. Per dare evidenza dello storno totale avvenuto, viene valorizzato automaticamente il campo “Storno totale effettuato” (nel tab “Ordine di lavoro” dell’OdL) con il valore “T”.

Se storno ore in Geocall KO: EAM acquisisce l’esito negativo dello storno in Geocall

Se esito simulazione SAP KO:

Se l’OdM in SAP si trova in uno stato che impedisce l’esecuzione dello storno allora su EAM è restituito un messaggio di errore in cui viene riportato lo stato dell’OdM in SAP che ha causato il KO della richiesta. In questo caso l’utente cambierà manualmente lo stato dell’OdM in SAP (portandolo in uno stato idoneo) per poi eseguire nuovamente lo storno in EAM.

Si specifica che:

In caso di OdL aggregato lo storno totale viene effettuato a livello di OdL padre e implica lo storno totale ore/fisico di tutti gli OdL figli

A seguito della regressione in stato “Aperto” sarà possibile avanzare nuovamente l’OdL in “Schedulabile”

L’azione di storno totale non avrà effetti sullo stato della difformità e sul legame della stessa con l’odl

È possibile effettuare lo storno anche su consuntivazioni afferenti a periodi contabili precedenti rispetto alla data di esecuzione dello storno.

#### Storno parziale non UI

Per poter effettuare lo storno parziale di singoli consuntivi orari devono essere soddisfatte le seguenti condizioni:

Consuntivo già validato

Tipologia spesa = OPEX

Nel caso in cui non fossero rispettate le condizioni sopra elencate il sistema restituirà un messaggio di errore “impossibile effettuare storno parziale, verificare che siano presenti consuntivi già validati e che la tipologia spesa sia OPEX”

Di seguito si riporta il processo di esecuzione dello storno parziale:

Figura 169 - Processo storno parziale non UI

L’utente nel tab “Avanzamento” dell’OdL esegue lo storno del consuntivo orario di una risorsa tramite il pulsante rappresentato in Figura 170

Figura 170 - Funzionalità di storno parziale

La richiesta di storno viene inoltrata a SAP

Se l’OdM in SAP si trova in uno stato che impedisce l’esecuzione dello storno allora su EAM è restituito un messaggio di errore in cui viene riportato lo stato dell’OdM in SAP che ha causato il KO della richiesta. In questo caso l’utente cambierà manualmente lo stato dell’OdM in SAP (portandolo in uno stato idoneo) per poi eseguire nuovamente lo storno in EAM. Al contrario se l’OdM si trova in uno stato idoneo si procede con lo storno delle ore in SAP e successivamente in EAM

In caso di esito negativo dello storno, viene popolato il campo “Errore SAP” con relativa motivazione dell’errore

Si specifica che:

Non sono necessari controlli sullo stato dell’OdL e sull’esistenza dell’OdM

Lo storno parziale non comporta uno cambio stato dell’OdL

Lo storno parziale comporta l’azzeramento totale del consuntivo di una risorsa

Nel caso in cui si dovesse stornare più del 70% dell’effort:

Se sull’OdL è registrato un fisico allora apparirà un warning non bloccante “attenzione si sta stornando più del 70% del totale”;

Se sull’OdL non è registrato un fisico non apparirà nessun warning.

È possibile effettuare lo storno anche su consuntivazioni afferenti a periodi contabili precedenti rispetto alla data di esecuzione dello storno

#### Storno totale non UI

Per poter effettuare lo storno totale dei consuntivi orari e fisici devono essere soddisfatte le seguenti condizioni:

Tipologia spesa = OPEX

Nel caso in cui non fosse rispettata la condizioni sopra citata il sistema restituirà un messaggio di errore “impossibile effettuare storno totale, verificare che la tipologia spesa sia OPEX”. Le difformità rilevate / validate (o potenzialmente anche già risolte) rimarranno inalterate, compreso il legame con l'OdL che le ha rilevate.

Di seguito si riporta il processo di esecuzione dello storno totale:

Figura 171 - Processo di storno totale non UI

L’utente esegue lo storno totale tramite il pulsante rappresentato in Figura 172. Una volta innescata la funzionalità di storno totale il sistema eseguirà un pre-check per verificare se sono state già inviate o meno delle ore a SAP.

Figura 172 - Funzionalità di storno totale

Se non sono presenti consuntivi orari già inviati a SAP il sistema procede direttamente con lo storno totale del fisico in EAM.

L’OdL viene regredito in stato “Aperto”. Per dare evidenza dello storno totale avvenuto, viene valorizzato automaticamente il campo “Storno totale effettuato” (tab “Ordine di lavoro” dell’OdL) con il valore “T”.

Se sono presenti consuntivi orari inviati a SAP la richiesta di storno viene inoltrata in SAP

Se l’OdM in SAP si trova in uno stato che impedisce l’esecuzione dello storno allora su EAM è restituito un messaggio di errore in cui viene riportato lo stato dell’OdM in SAP che ha causato il KO della richiesta. In questo caso l’utente cambierà manualmente lo stato dell’OdM in SAP (portandolo in uno stato idoneo) per poi eseguire nuovamente lo storno in EAM. Al contrario se l’OdM si trova in uno stato idoneo si procede con lo storno delle ore in SAP e con la chiusura dell’OdM (portato in stato TECO).

Il sistema procede con lo storno totale delle ore e del fisico in EAM

L’OdL viene regredito in stato “Aperto”. Per dare evidenza dello storno totale avvenuto, viene valorizzato automaticamente il campo “Storno totale effettuato” (nel tab “Ordine di lavoro” dell’OdL) con il valore “T”.

Si specifica che:

In caso di OdL aggregato lo storno totale viene effettuato a livello di OdL padre e implica lo storno totale ore/fisico di tutti gli OdL figli

A seguito della regressione in stato “Aperto” sarà possibile avanzare nuovamente l’OdL in “Schedulabile”

L’azione di storno totale non avrà effetti sullo stato della difformità e sul legame della stessa con l’odl

È possibile effettuare lo storno anche su consuntivazioni afferenti a periodi contabili precedenti rispetto alla data di esecuzione dello storno

#### Storno parziale misti

Per poter effettuare lo storno parziale di singoli consuntivi orari devono essere soddisfatte le seguenti condizioni:

Consuntivo già validato

Tipologia spesa = OPEX

Nel caso in cui non fossero rispettate le condizioni sopra elencate il sistema restituirà un messaggio di errore “impossibile effettuare storno parziale, verificare che siano presenti consuntivi già validati e che la tipologia spesa sia OPEX”

Di seguito si riporta il processo di esecuzione dello storno parziale:

Figura 173 - Processo di storno parziale misti

Nel caso in cui l’utente esegua lo storno parziale su una risorsa UI il processo di storno è lo stesso descritto nel par. 12.3.3.1

Nel caso in cui l’utente esegua lo storno parziale su una risorsa non UI il processo di storno è lo stesso descritto nel par. 12.3.3.3

Si specifica che:

Non sono necessari controlli sullo stato dell’OdL e sull’esistenza dell’OdM

Lo storno parziale non comporta uno cambio stato dell’OdL

Lo storno parziale comporta l’azzeramento totale del consuntivo di una risorsa. Esempio: se per una determinata risorsa sono state consuntivate 10 ore, lo storno parziale porterà ad un azzeramento totale delle ore che quindi diventeranno pari a zero. Non è dunque possibile stornare una quantità minore di 10.

Nel caso in cui si dovesse stornare più del 70% dell’effort:

Se sull’OdL è registrato un fisico allora apparirà un warning non bloccante “attenzione si sta stornando più del 70% del totale”;

Se sull’OdL non è registrato un fisico non apparirà nessun warning.

È possibile effettuare lo storno anche su consuntivazioni afferenti a periodi contabili precedenti rispetto alla data di esecuzione dello storno

#### Storno totale misti

Per poter effettuare lo storno totale dei consuntivi orari e fisici devono essere soddisfatte le seguenti condizioni:

Tipologia spesa = OPEX

Nel caso in cui non fossero rispettate le condizioni sopra elencate il sistema restituirà un messaggio di errore “impossibile effettuare storno totale, verificare che la tipologia spesa sia OPEX”. Le difformità rilevate / validate (o potenzialmente anche già risolte) rimarranno inalterate, compreso il legame con l'OdL che le ha rilevate.

Il processo di storno totale per OdL misti segue le stesse logiche descritte nel capitolo 12.3.3.2, ossia lo storno totale UI.

Si specifica che:

In caso di OdL aggregato lo storno totale viene effettuato a livello di OdL padre e implica lo storno totale ore/fisico di tutti gli OdL figli

A seguito della regressione in stato “Aperto” sarà possibile avanzare nuovamente l’OdL in “Schedulabile”. L’azione di storno totale non avrà effetti sullo stato della difformità e sul legame della stessa con l’odl

È possibile effettuare lo storno anche su consuntivazioni afferenti a periodi contabili precedenti rispetto alla data di esecuzione dello storno

## Esecuzione su Maximo Mobile

In questo capitolo vengono descritte le funzionalità previste sull’applicazione Maximo Mobile con riferimento ai processi di esecuzione presenti nel capitolo 12.1. In particolare, verranno approfondite le funzionalità dell’app Mobile “My Schedules” e “Asset” (evidenziate nella Home Page dell’app Figura 175) che costituiscono il fulcro dell’execution degli OdL. Inoltre, sarà descritto il meccanismo di sincronizzazione fra applicativo Web e Mobile, evidenziato dall’Icona a fondo pagina che indica se la sincronizzazione è attiva o meno (per esempio a causa di assenza di rete).

Figura 175 - Home page Maximo Mobile

Nota: l’applicazione mobile utilizzata in questa presentazione è stata impostata in inglese e serve solo come esempio. La soluzione fornita a Terna sarà impostata in lingua italiana.

Come già introdotto nel capitolo 12.1, una parte dell’esecuzione degli OdL sarà eseguibile dall’utente tramite Maximo Mobile solo per OdL di competenza puramente UI.

L’operatore può visualizzare gli OdL a lui assegnati su Maximo Mobile (nell’app “My Schedules”) a partire dal momento in cui sono in stato “Inviato”. Inoltre, qualsiasi operatore può visualizzare tutti gli OdL (in qualsiasi stato e assegnati a qualsiasi operatore) associati a uno specifico asset tramite l’applicazione “Asset”. L’utente può iniziare ad effettuare la registrazione di nuove difformità, la non conferma di difformità esistenti e l’esecuzione di micro-controlli tramite la piattaforma Mobile. Una volta conclusa l’esecuzione dei microcontrollori assegnati e la registrazione delle difformità/interferenze, l’OdL può essere portato in stato “Eseguito”.

Nei successivi paragrafi vengono descritte le funzionalità dell’app Mobile, evidenziando quelle disponibili a seconda del Titris.

Applicazione “Asset”

L’applicazione è collegata ai dati anagrafici presenti a sistema e permette all’utente di consultare l’anagrafica degli asset in qualsiasi momento (difformità e caratteristiche associate all’asset).

Una volta che l’utente seleziona l’applicazione “Asset” dall’elenco presente in Home Page, si viene reindirizzati alla pagina di ricerca (Figura 176), contenente gli asset censiti all’interno del sistema.

Tramite la lente di ingrandimento, evidenziata con la freccia 1 della Figura 176, l’utente può effettuare una ricerca filtrata in base alla descrizione asset che desidera analizzare (ad esempio, asset con descrizione contenente “Sostegno”).

Selezionando un elemento dalla lista degli asset, l’utente accede alla vista di dettaglio presente in Figura 176. In questa schermata sono presenti lo stato, la classificazione, gli attributi e altre specifiche tecniche dell’asset in analisi, rappresentati nei riquadri 4, 5 e 6, rispettivamente. Inoltre, sarà possibile visualizzare gli OdL (in qualsiasi stato) collegati all’asset e, tramite pulsante dedicato, le difformità che insistono sull’asset.

Figura 176 - Vista iniziale dell'applicazione "Assets"

Figura 177 - Vista di dettaglio dell'asset selezionato

Figura 178 - Vista dell'elenco asset filtrato per denominazione

Applicazione “My schedules”

Selezionando l’applicazione “My schedules”, l’utente su Mobile viene indirizzato alla schermata rappresentata nella maschera della Figura 179, contenente tutti e soli gli OdL di tipologia controllo (con le informazioni principali dell’odl, tra cui: descrizione odl, assetnum, sap-id, descrizione asset) a lui assegnati a partire dallo stato “Inviato” con possibilità di iniziare l’esecuzione dei micro-controlli e la rilevazione di difformità/interferenze.

Figura 179 - Vista elenco degli OdL selezionabili

Tramite il pulsante evidenziato nel riquadro 2 di Figura 179 l’utente ha la possibilità di filtrare gli odl assegnati secondo 3 query predefinite e selezionabili dall’utente:

Query “odl da eseguire”-> restituisce gli odl con flag “attività eseguita” false. Questa è la query preimpostata all’apertura dell’app

Query “odl da eseguire oggi”-> restituisce gli odl con flag “attività eseguita” false e data di inizio schedulazione pari alla sysdate

Query “odl eseguiti”-> restituisce gli odl con flag “attività eseguita” true

Selezionando un OdL fra quelli proposti (evidenziato dal riquadro 1 della Figura 179), l’utente viene reindirizzato alla vista dettagliata dell’OdL scelto (Figura 180).

Figura 180 - Vista di dettaglio dell'OdL selezionato

Nella maschera di dettaglio in Figura 180, l’utente ha la possibilità di eseguire le seguenti azioni:

Verificare i dettagli dell’OdL: All’interno dell’OdL, l’utente può visualizzare la descrizione dell’OdL, le date di schedulazione e la durata (riquadro 2) e l’asset associato (riquadro 3) con il suo SAPID (o il SAPID dell’asset di livello superiore se l’asset associato non ha un suo SAPID). Tramite la freccia a destra nel riguardo degli Asset è possibile accedere all’applicazione degli asset. Si specifica che per i controlli periodici di stazione che richiedono micro-controlli, l’associazione con l’asset di quarto livello oggetto di micro-controllo è visibile tramite l’applicazione di visualizzazione dei micro-controlli associati all’OdL, indicata dalla freccia 1 in Figura 180.

Caricare un allegato: Sull’OdL, è possibile caricare foto, video o documenti nella sezione “Attachments” evidenziata nel riquadro 4. Per allegare una foto all’OdL, il sistema offre all’utente la possibilità di scattarla direttamente da Mobile e contestualmente allegarla, oppure di caricarla come allegato se già disponibile, relazionandola unicamente all’ordine di lavoro;

Verificare lo storico degli OdL associati all’asset: Utilizzando il pulsante indicato con la freccia 2 in Figura 180, l’utente può visualizzare lo storico di tutti gli OdL associati all’asset dell’attività che si sta visualizzando (Figura 181);

Figura 181 - Storico di OdL registrati sull'asset oggetto dell'attività

Visualizzare l’elenco dei Micro-controlli associati all’OdL (se presenti): Selezionando il tasto indicato con la freccia 1 in Figura 182, l’utente accede alla schermata contenente tutti i microcontrolli previsti per l’OdL in analisi (Figura 182). Nell’elenco sono disponibili tutti i microcontrolli associati all’OdL selezionato. Ogni microcontrollo presente in questa vista è associato all’asset di quarto livello a cui fa riferimento.

Visualizzare il dettaglio dei microcontrolli: È possibile visualizzare il dettaglio di ciascun microcontrollo tramite la visualizzazione a tendina riportata in Figura 182, attivata selezionando la riga di interesse.

Figura 182 - Vista dei microcontrolli associati all'OdL

All’interno della tendina di dettaglio, Il sistema riporta per l’asset oggetto di micro-controllo: Assetnum, descrizione asset, SAP-ID, SAP-ID del padre, riferimento fase dell’asset e funzione elemento. Infine, nella lista elenco dei micro-controlli sono presenti le seguenti informazioni: SAP-ID padre, descrizione micro-controllo, classificazione asset oggetto di micro-controllo, descrizione asset padre (es: se i micro-controlli puntano ad asset sottostanti lo stallo, allora la descrizione dell’asset padre rappresenta la descrizione dello stallo).

Registrare l’esecuzione dei microcontrolli: L’utente può registrare l’esecuzione dei microcontrolli direttamente all’interno della vista di microcontrolli associati all’OdL, utilizzando l’apposito pulsante di “check”. Dopo aver adeguato lo stato, l’utente seleziona l’attività eseguita tramite pulsante indicato delle frecce 1 o 3. Di default, Maximo Mobile rende disponibile il primo pulsante dell’elenco delle attività in colore Blu, che tuttavia non costituisce un vincolo dell’ordine di esecuzione delle attività. La selezione del pulsante cambia il suo colore in verde (freccia 2 nella Figura 182) e genera automaticamente una notifica sulla sincronizzazione tra Mobile e Web, visibile tramite l’icona indicata nel riquadro 2 della Figura 182. A partire di questo punto, possono verificarsi due possibilità:

Sincronizzazione con Web va a buon fine: In questo caso, il pulsante indicato conserva il colore verde e la notifica è più presente;

Sincronizzazione con Web fallisce: In questo caso, l pulsante ritorna allo stato iniziale (frecce 3) e il messaggio di errore persiste.

Registrare nuove difformità e visualizzare lo storico di difformità registrate: Tramite i pulsanti evidenziati nel riquadro 3, l’utente ha la possibilità di registrare nuove difformità rilevate sull’asset (icona superiore) e di consultare lo storico completo di tutte le difformità precedentemente registrate (icone inferiore). Queste funzionalità sono descritte in dettaglio nel paragrafo 12.5.3.2

Registrare l’esitazione dell’OdL (solamente per OdL singoli senza micro-controlli): Tramite apposito flag “Attività esitata” (riportato nella figura sottostante) l’utente ha la possibilità di registrare l’esitazione dell’OdL da mobile.

Si specifica che tale flag:

È reversibile. Può essere inserito, disinserito e successivamente reinserito;

È puramente informativo. Non è vincolante per il passaggio di stato in “Eseguito” dell’ordine; dunque non ha impatti sulle logiche di modifica/cambio stato dell’ordine

È inseribile da tutti gli assegnatari dell’ordine; non sono presenti dunque controlli autorizzativi.

Segnalare la presenza di opere interferenti: Nel caso in cui l’operatore durante l’esecuzione di un OdL rilevi una nuova opera interferente o una differenza tra quanto presente sul campo e quanto riportato in anagrafica, può procedere all’invio di una segnalazione verso gli utenti abilitati alla modifica dell’anagrafica appartenente all’UdB responsabile asset (da Web il responsabile asset può visualizzare le notifiche di segnalazioni dal campo e tramite le notifiche accedere direttamente agli OdL ai quali sono associate). Per inviare una notifica da app mobile, una volta all’interno dell’OdL, l’operatore clicca il pulsante indicato dalla freccia 3 in Figura 180 per accedere alla schermata di compilazione della segnalazione; successivamente inserisce il motivo della segnalazione (in forma di testo libero) e invia la notifica al responsabile asset. Quest’ultimo può visualizzare le notifiche ricevute dal campo nel CdC e procedere con le modifiche anagrafiche riferite dall’operatore se le ritiene valide (con la possibilità di consultare l’OdL associato alla notifica per avere maggiori dettagli sull’attività che ha portato alla rilevazione della segnalazione e di visualizzare eventuali foto allegate all’ordine). All’interno del CdC è riportata la tabella “Notifiche OdL ricevute” contenente le colonne “OdL”, “Descrizione OdL”, “Assetnum”, “SAP-ID”, “PdL”, “Stato OdL”. A seguito della modifica il responsabile asset, da Web, spunta il flag “gestito” sulla notifica. Nell’elenco delle notifiche, saranno sempre visualizzabili quelle che sono state gestite e quelle ancora da gestire, identificate dal relativo flag.

## Gestione difformità/Interferenze

In questo capitolo vengono descritti:

Stati assunti dalle difformità/interferenze (workflow);

Logiche di visualizzazione e validazione/non validazione delle difformità;

Processo di registrazione di nuove difformità.

Stati difformità/interferenze

In figura 183 si riportano gli stati assunti dalle difformità/interferenze durante il loro ciclo di vita:

Figura 183 - Stati delle difformità / interferenze

Da validare: Stato impostato dal sistema al momento della registrazione di difformità/interferenze sull’OdL. In questo stato la difformità non è visibile al DSS per la generazione degli OdL risolutivi;

Non validata: Impostato dall’utente (puntualmente o massivamente) in caso di non validazione (in questo caso l’utente deve obbligatoriamente inserire una motivazione e/o documentazione fotografica a supporto);

Validata: sono presenti due casistiche per il passaggio a questo stato:

Impostato dall’utente in caso di validazione puntuale o massiva;

Impostato automaticamente dal sistema in caso di superamento della data di massima validazione impostata a catalogo.

Superata: Questo stato è sempre accompagnato da una causale definita in una lista: non confermata; cambio gravità; eliminazione a catalogo; non esecuzione del controllo. Può essere impostato dal sistema nelle seguenti casistiche:

Quando una difformità precedentemente censita viene non confermata nel successivo controllo. È associata come causale di superamento “non conferma”;

Quando una difformità precedentemente rilevata con un certo livello di gravità deve essere registrata con nuovo livello di gravità. L’utente registra la nuova difformità con il nuovo livello di gravità, il sistema mette in stato “superato” con causale “cambio gravità” la difformità con il vecchio livello di gravità.

Impostato dall’utente nelle seguenti casistiche:

Quando l’attività risolutiva viene chiusa come “non eseguita”. La causale di superamento è “non esecuzione del controllo”.

Quando una difformità/interferenza è cancellata dal catalogo o diventa non risolvibile da alcuna attività. La causale di superamento è “eliminazione a catalogo”.

Risolta: Al passaggio in stato “Eseguito” dell’ordine verranno portate automaticamente in stato “Risolta” le difformità ed interferenze risolvibili dal PdL associato all’ordine con le seguenti specificità: ​

associate strettamente sullo stesso asset

nel caso delle stazioni su tutti gli asset di quarto livello che hanno lo stesso padre dell’asset dell’ordine risolto.​

nel caso delle linee su tutti gli asset di terzo livello che si trovano in relazione gerarchica strettamente tra terzo e quarto livello. ​​

Visualizzazione e validazione/non validazione difformità/interferenze

Tramite l’applicazione “Difformità rilevate” e “Interferenze rilevate” il responsabile attività può accedere alla maschera rappresentata nella figura sotto contenente la lista inizialmente pre-filtrata per tutte le difformità/interferenze ancora da validare. Ogni responsabile attività visualizza solo le difformità/interferenze di sua competenza. Il filtro preimpostato determina la presenza del valore “Da validare” nella testata della colonna “stato”. Nel caso in cui si volessero visualizzare le difformità in uno stato diverso da “Da Validare”, è possibile rimuovere il filtro di default dalla colonna “Stato”, e inserirvi uno degli stati descritti nel par. 12.5.1. Se invece si volessero visualizzare tutte le difformità/interferenze (in qualsiasi stato) presenti a sistema (afferenti al relativo responsabile UdB) basterà eliminare il contenuto dalla testata della colonna “stato” e premere invio. Oltre i precedenti filtri descritti, è possibile utilizzare le seguenti queries di filtraggio:

Query che mostra i record relativi a difformità associate ad asset di linea (senza filtri sullo stato)

Query che mostra i record relativi a difformità associate ad asset di stazione (senza filtri sullo stato)

Di seguito le colonne presenti in tabella:

Figura 184 - Applicazione difformità rilevate

ID Difformità/interferenza: codice difformità/interferenza da catalogo;

ID Difformità/interferenza a db: codice univoco a db della difformità/interferenza

Descrizione: descrizione difformità/interferenza;

Distanza minima (solo per interferenze): misura in metri riportata nel report Elive e arrotondata alla seconda cifra decimale utilizzata per la generazione dell’interferenza. Campo non modificabile inserito in fase di generazione dell’interferenza;

Stato difformità/interferenza: Stato in cui si trova la difformità/interferenza;

SAP ID Asset: Codice identificativo dell’asset in SAP

Descrizione asset di primo livello relativo all’asset della difformità

Descrizione asset di terzo livello relativo all’asset della difformità (solo per asset sottostanti al terzo livello)

POS-NR (popolato solo per asset di linea secondo livello)

Asset: Codice identificativo dell’asset in EAM. Selezionando un record di questa colonna l’utente accede alla scheda tecnica dell’asset. Nella pop up di selezione dell’asset sono anche presenti la descrizione asset primo livello, la descrizione asset terzo livello (solo per asset sottostanti al terzo livello), il POS-NR (popolato solo per asset di linea secondo livello) e fase (riferimento conduttore, riferimento FdG e riferimento fase), per guidare l’utente nella scelta dell’asset;

Descrizione asset: classificazione dell’asset (ad esempio CONDUTTORE, SOSTEGNO);

Gravità;

Data prima rilevazione: Data in cui la difformità/interferenza è stata rilevata per la prima volta;

Tipologia attività: Tipologia attività dell’OdL di prima rilevazione;

Data ultima rilevazione: Se la difformità/interferenza viene rilevata per la prima volta allora data ultima rilevazione è uguale a data prima rilevazione, altrimenti viene riportata l’ultima data in cui la difformità è stata confermata indirettamente. La conferma è indiretta in quanto non esiste un tasto diretto di conferma. Di seguito vengono riportate le logiche di aggiornamento del campo “data ultima rilevazione” in caso di conferma indiretta:

Solo per gli odl di tipo controllo, nel caso in cui l’utente sul mobile valorizzi il flag “attività esitata”, le difformità rilevate precedentemente sull'asset (non le difformità rilevate con l'odl su cui si sta inserendo il flag) avranno come data di ultima rilevazione la data in cui tale flag risulta inserito. Si specifica che l’inserimento del flag “attività esitata” sul controllo aggiorna esclusivamente la data di ultima rilevazione delle difformità, presenti sull’asset, rilevabili dallo specifico controllo su cui si sta inserendo il flag (le difformità rilevabili sono definite nel catalogo attività).

Considerando che il flag "attività esitata" non è obbligatorio, potrebbe accadere che l'utente non lo inserisca. Quindi nel caso in cui il flag “attività esitata” non sia valorizzato sull’ordine, sempre e solo per gli odl di tipo controllo, le difformità rilevate precedentemente sull'asset (non le difformità rilevate con l'odl su cui si sta lavorando) avranno come data di ultima rilevazione la data in cui si effettua il passaggio dell'odl in ESEGUITO. Si specifica che il passaggio dell’odl in ESEGUITO aggiorna esclusivamente la data di ultima rilevazione delle difformità, presenti sull’asset, rilevabili dallo specifico controllo su cui si effettua il cambio stato in ESEGUITO (le difformità rilevabili sono definite nel catalogo attività).

ID Misura: Codice identificativo della misura associata alla difformità rilevata

Descrizione misura: descrizione della misura da associare alla difformità. Campo popolato automaticamente in base alla relazione, definita nel catalogo delle difformità, tra la difformità e la misura. Nel caso in cui nel catalogo non fosse definita nessuna relazione tra la difformità in questione e una misura allora il campo non sarà valorizzato e non sarà editabile.

Valore Misura: Campo visualizzabile dall’utente, non modificabile.

Fase;

OdL risolutore: questo campo risulta valorizzato se è stato eseguito l’OdL risolutore della difformità/interferenza. Cliccando sul campo è possibile accedere all’OdL;

Stato OdL risolutore: Stato dell’OdL che risolve la difformità rilevata;

Note rilevazioni: viene mostrata l’ultima nota registrata attraverso un ordine sul record della difformità. Il campo è in sola lettura e per visualizzare lo storico di tutte le altre note registrate attraverso gli OdL per quella difformità è possibile accedervi tramite il tasto all’inizio della riga;

Note: Campo valorizzabile manualmente. Per questo campo sono presenti le seguenti logiche:

Se la difformità ha uno stato diverso da NON VALIDATA -> valorizzazione del campo non è obbligatoria;

Se la difformità ha stato uguale a NON VALIDATA -> valorizzazione del campo obbligatoria. Se l’utente non valorizza il campo allora il sistema non permetterà il salvataggio e restituirà un messaggio “compilare note per le difformità in stato NON VALIDATA”.

Causale di superamento:

Matricola validatore: Numero matricola utente che ha validato o non validato la difformità. Campo popolato automaticamente.

Nome validatore: Nome e cognome della matricola validatore

Matricola rilevatore: Se la difformita/interferenza è rilevata per la prima volta allora viene riportato il numero matricola utente che ha rilevato la difformità. Altrimenti riporta il numero di matricola dell’utente che ha confermato indirettamente la difformità. La conferma è indiretta in quanto non esiste un tasto diretto di conferma. Le logiche per il popolamento della matricola rilevatore in caso di conferma indiretta sono le stesse descritte precedentemente per il campo “data di ultima rilevazione”.

Nome rilevatore: Nome e cognome della matricola rilevatore

Matricola setting “SUPERATA”: Numero matricola dell’utente che ha effettuato il cambio stato in SUPERATA

Nome setting “SUPERATA”: Nome e cognome della matricola che ha effettuato il cambio stato in SUPERATA

Matricola risolutore: Numero di matricola dell’utente che ha passato l’odl risolutore in ESEGUITO. Si ricorda che una difformità assume lo stato RISOLTA non appena l’Odl passa in ESEGUITO

Nome risolutore: Nome e cognome matricola risolutore Responsabile asset:

Si specifica che ogni colonna risulta filtrabile e che i record presenti in tabella sono in read only.

Per vedere gli OdL che hanno rilevato e confermato indirettamente la difformità/interferenza, è necessario espandere il record tramite il simbolo evidenziato in Figura 185.

Figura 185 - Pulsante di espansione dettaglio OdL

Una volta cliccato sull’icona evidenziata verranno mostrati gli OdL e le relative date di esecuzione (ossia le date in cui vengono rilevate/confermate indirettamente le difformità tramite l’odl). Nel caso in cui si volessero avere maggiori informazioni sull’OdL basta cliccare sul campo “Ordine di lavoro” e si viene reindirizzati sull’applicazione di gestione dell’OdL (Figura 186). Nel campo “Note” vengono riportate le note inserite sulla difformità/interferenza al momento della registrazione della stessa all’interno dell’odl.

Figura 186 - Dettaglio OdL relazionati alla difformità

Per validare/non validare le difformità presenti nella lista è necessario:

Selezionare le difformità presenti nella schermata tramite checkbox:

Puntualmente, cliccando sulle singole difformità;

Massivamente, cliccando la checkbox in testata tramite la quale viene effettuato un “select all” (inserendo le note obbligatorie nel caso della non validazione).

Cliccare sull’azione laterale “Cambia stato”: apparirà una dialog all’interno della quale è possibile selezionare lo stato che si vuole assegnare alle difformità, ossia:

Validata

Non validata

È possibile effettuare il cambio stato solamente per le difformità che si trovano in stato “Da validare”. Da quest’ultimo stato sarà possibile passare solamente a “Validata” o “Non validata”.

#### Visualizzazione e non conferma difformità su Mobile

L’applicazione Maximo Mobile consente, tramite funzionalità custom, la visualizzazione delle difformità aperte su ciascun asset registrato nel sistema. La Figura 187 rappresenta la tabella contenente tutte le difformità aperte censite sugli asset sottesi a un particolare OdL. In questa schermata, accessibile da Mobile all’interno dell’applicazione “My Schedules”, l’operatore può visualizzare tutte le difformità aperte sull’asset relativo all’OdL e che sono rilevabili tramite l’attività che sta svolgendo in quel momento. La relazione delle difformità che possono essere rilevate tramite ciascun controllo è presente nel catalogo delle attività. Nella tabella sono previste le seguenti colonne:

Non Conferma: Tramite un pulsante presente nella colonna “non confermare”, l’utente può determinare se la difformità è ancora presente o meno. Se l’utente non clicca il pulsante del “non conferma” vuol dire che la difformità/interferenza è ancora presente; come descritto in dettaglio nel cap. 12.5.2 (per i campi “data ultima rilevazione” e “matricola ultima rilevazione”) la presenza della difformità/interferenza viene confermata indirettamente tramite l’inserimento del flag “attività esitata” sul mobile oppure tramite il passaggio dell’odl in ESEGUITO. Se invece l’utente non rileva la difformità/interferenza deve impostare su il flag su “Non conferma”. La non conferma della difformità/interferenza risulta nel cambio di stato della difformità in “superata” con causale “non confermata”.

ID Catalogo: Codice ID della difformità aperta sull’asset;

Descrizione: Descrizione della difformità aperta sull’asset;

Data prima rilevazione: data di prima rilevazione della difformità;

Data rilevazione: Data di ultima rilevazione della difformità.

Gravità: Livello di gravità associato alla difformità; Nel caso in cui sia necessario modificare il livello di gravità di una difformità già aperta, è possibile farlo direttamente dall’applicazione aggiungendo una nuova difformità/interferenza con un diverso livello di gravità. Il sistema provvede a mettere in “superata” con causale “cambio gravità” la difformità con livello vecchio e registrare la nuova difformità a sistema;

Misura: valore della misura rilevata in fase di registrazione della difformità/interferenza:

Descrizione misura: descrizione della misura rilevata durante la registrazione della difformità/interferenza;

Asset: Asset sul quale è stata rilevata la difformità. Nel caso in cui un OdL di tipologia controllo faccia riferimento a più asset, la colonna permette all’utente di distinguere gli asset su cui sono registrate le singole difformità.

Note: Campo note dedicato all’aggiunta di osservazioni relative alla difformità (ad.es posizione precisa della difformità sull’asset).

Fase a cui si riferisce: riporta la fase su cui è stata rilevata la difformità/interferenza (solo se necessario specificarla).

Matricola rilevatore: numero della matricola che ha rilevato la difformità

Figura 187 - Vista Mobile di non conferma delle difformità aperte

Registrazione nuove difformità/interferenze e misure

#### Registrazione nuove difformità/interferenze e misure tramite WEB

L’applicazione Web di Maximo consente all’utente di registrare nuove difformità/interferenze o misure rilevate su un asset (coerentemente con l’asset indicato a livello di Odl) durante una attività di controllo, così come di non confermare difformità/interferenze già registrate.

La soluzione prevede la possibilità di rilevare difformità sia sulla base del PdL (secondo le regole di seguito elencate) sia del micro-controllo, coerentemente con l'asset soggetto a micro-controllo.

Pdl Linee

Si possono inserire difformità rilevabili/risolvibili sull'asset oggetto di OdL​

Se il PdL è su un asset di terzo livello si possono inserire difformità rilevabili/risolvibili sullo stesso asset di 3 livello e sui figli di 4​

Se il PdL è rilevazione/controllo/monitoraggio sulla campata si possono inserire difformità rilevabili sulla campata e sui figli di questa. Inoltre, si possono inserire difformità sui sostegni (fratelli della campata) e sui figli dei sostegni​

Pdl Stazione

Si possono inserire difformità rilevabili/risolvibili sull'asset oggetto di PdL

Se il PdL è su un asset di quarto livello si possono inserire difformità rilevabili/risolvibili anche sui fratelli di quarto livello (che condividono lo stesso padre di 3° livello)

Esclusivamente per ordini che fanno riferimento ad asset di tipo campata aerea, è possibile registrare anche difformità relative ai sostegni adiacenti.​

Per aggiungere una difformità da Web l’utente deve accedere all’applicazione “Monitoraggio OdL”, selezionare il Tab “Difformità e interferenze” e valorizzare la tabella “Difformità rilevate con il presente odl” (per le interferenze è presente la tabella “Interferenze rilevate con il presente odl”) aprendo la vista rappresentata in Figura 188 (a scopo illustrativo è stata riportata l’immagine delle difformità che è uguale a quella per le interferenze) con i seguenti campi:

Asset: Campo dedicato all’inserimento dell’asset su cui la difformità/interferenza è stata rilevata. In questo campo sarà possibile inserire asset per i quali, all’interno del pdl, siano riportate delle difformità/interferenze rilevabili, in particolare:

L’asset associato all’OdL;

Gli asset gerarchicamente sottostanti a quello di riferimento dell’OdL;

Gli asset adiacenti a quello di riferimento dell’OdL: valido solamente per attività eseguite sulla campata, sulle quali sarà possibile inserire difformità sia della campata sia dei sostegni a essa adiacenti. (es: se su un pdl che ha come asset di riferimento una campata, si inserisce una difformità rilevabile su un sostegno, allora nella tabella di registrazione delle difformità sull’odl sarà possibile inserire i sostegni adiacenti alla campata oggetto di odl),

Nella pop up di selezione dell’asset sono anche presenti la descrizione asset primo livello, la descrizione asset terzo livello (solo per asset sottostanti al terzo livello), il POS-NR (popolato solo per asset di linea secondo livello) e fase (riferimento conduttore, riferimento FdG e riferimento fase), per guidare l’utente nella scelta dell’asset. Si specifica che nella dialog di selezione dell’asset è anche presente una colonna “riferimento posizione” popolata secondo le seguenti regole:

Per conduttori/armamenti conduttore di 4° livello: viene riportato il valore di LIN_RIF_POSIZIONE

Per fdg/armamenti fdg di 4° livello: viene riportato il valore di LIN_RIF_FUNE

Per i restanti 4° livelli linea, relazionati agli asset sopra citati, viene riportato il valore di LIN_RIF_POSIZIONE/LIN_RIF_FUNE dell’asset a cui sono relazionati

Per gli equipment di 4° livello stazione: viene riportata la fase di riferimento dell’equipment riportata in testata anagrafica

Riportata in testata di Classificazione asset

Descrizione asset

SAP ID asset

ID Difformità/interferenza: codice ID catalogo della difformità/interferenza da associare all’asset. In questo campo l’utente potrà inserire solo le difformità/interferenze che sono rilevabili per l’asset e dalla attività (pdl) in oggetto;

ID difformità/interferenza a db: codice univoco della difformità/interferenza a db. Campo popolato automaticamente al salvataggio

Descrizione difformità/interferenza: descrizione della difformità/interferenza selezionata. Popolato automaticamente dal sistema dopo la selezione della difformità/interferenza;

Distanza minima: riporta la distanza minima in metri arrotondata alla seconda cifra decimale, associata all’interferenza in fase di creazione tramite il report Elive. In nessun caso il campo è modificabile dall’utente e nel caso specifico della generazione manuale sarà sempre blank;

Fase: fase a cui si riferisce la difformità/interferenza (se necessario indicarla). Selezionabile da una lista valori che riporta R, S e T; campo obbligatorio se per la specifica difformità/interferenza, nel catalogo difformità, risulta valorizzato il flag “fase”.

ID Misura: Campo non obbligatorio. Tramite picklist vengono restituiti i codici misura associati alla difformità/interferenza nel rispettivo catalogo.

Unità di misura: campo popolato automaticamente in base all’ID misura selezionato

Descrizione misura: descrizione della misura da associare alla difformità/interferenza; popolato automaticamente in base all’ID misura selezionato.

Valore Misura: eventuale valore della misura associata alla difformità/interferenza. Obbligatorio se popolato il campo “ID misura”

Gravità: Livello di gravità della difformità/interferenza. Campo obbligatorio se per la difformità/interferenza nel rispettivo catalogo sono definite delle gravità. Campo popolabile tramite picklist contenente le gravità definite a catalogo per la difformità/interferenza

Tipologia: Anomalia o guasto. Campo presente solo per le difformità rilevabili (non per le interferenze) e popolato automaticamente in base alla difformità inserita.

Stato: In questo campo viene riportato lo stato della difformità/interferenza;

Note: campo facoltativo

Risolta contestualmente: flag che consente di identificare se l’anomalia rilevata è stata risolta contestualmente all’esecuzione dell’attività di controllo;

Obbligatorietà allegato: informa l’utente circa la necessità di inserire un allegato. Se non viene inserito un allegato dove necessario il sistema non permetterà il salvataggio. Di seguito si riportano le casistiche di inserimento di un allegato:

Odl singolo senza micro-controlli: l’allegato viene inserito nel campo “allegati” presente nella testata dell’odl e non a livello della singola difformità/interferenza

Odl singolo con micro-controlli: l’allegato viene inserito nel campo “allegati” presente nella testata del singolo micro-controllo e non a livello della singola difformità/interferenza

Odl aggregatore: l’allegato viene inserito nel campo “allegati” presente nella testata del singolo figlio e non a livello della singola difformità/interferenza

Matricola rilevatore: numero di matricola di chi ha rilevato la difformità

Numero collisioni: campo presente solo per le interferenze. Campo in read only; popolato dal sistema in base ai report inviati da Elive

Utilizzando il tasto “+” indicato in Figura 188, l’utente può aggiungere nuove difformità/interferenze. Lo stato viene associato in automatico dal sistema in “da validare”.

Figura 188 - Inserimento difformità/interferenze da Web

Nel tab “Difformità e interferenze” è presente anche la tabella per l’inserimento di Misure riportata nella Figura 189.

Figura 189 - Rilevazione misure

Di seguito si riportano i campi della tabella “Misure rilevate con il presente OdL”:

Asset: campo obbligatorio, selezionabile tramite picklist contenente l’asset oggetto di odl e gli asset gerarchicamente sottostanti a quest’ultimo. Nella pop up di selezione dell’asset sono anche presenti la descrizione asset primo livello, la descrizione asset terzo livello (solo per asset sottostanti al terzo livello), il POS-NR (popolato solo per asset di linea secondo livello) e fase (riferimento conduttore, riferimento FdG e riferimento fase), per guidare l’utente nella scelta dell’asset. Nel caso di campate e tratte cavo verranno mostrati anche gli asset adiacenti (sostegni, estremi cavo) e gli asset a essi gerarchicamente sottostanti. Verranno mostrati solo gli asset la cui classificazione risulta associata a una misura nel rispettivo catalogo.

Descrizione asset: popolato automaticamente in base all’asset precedentemente selezionato

Classificazione asset: popolato automaticamente in base all’asset precedentemente selezionato

SAP ID: popolato automaticamente in base all’asset precedentemente selezionato

ID misura: campo obbligatorio. Selezionabile tramite picklist contenente le misure che risultano rilevabili tramite il PdL dell’ordine e sulla base delle caratteristiche dell’asset (indicate nel catalogo misure). L’associazione tra PdL e misura è definita nel catalogo delle attività ed è visibile nel catalogo delle misure. Inoltre, la lista di misure rilevabili è aggiornata al momento in cui l’ordine passa in uno stato in cui le misure sono rilevabili in particolare: schedulato per gli ordini UI; eseguibile per gli ordini non UI. Tutte le modifiche di catalogo effettuate dopo che l’ordine è passato in uno di questi stati non saranno recepite perché l’ordine è già in fase di rilevazione, analogamente a quanto avviene per le difformità.

Descrizione misura: popolato automaticamente in base all’ID misura selezionato

Valore misura: campo numerico obbligatorio. Se decimale il separatore è la virgola. Per le misure non numeriche il campo è scelto attraverso una pop up che mostra il valore numerico affiancato dal valore alfanumerico che può assumere la misura. La lista è finita e viene pescata dalla tabella di transcodifica delle misure non numeriche, presente a sistema e modificabile attraverso richiesta AMS.

Valore misura non numerico: campo popolato dal sistema automaticamente in funzione del valore numerico scelto. Il campo è popolato esclusivamente se la misura e tracciata nella tabella di transcodifica delle misure non numeriche.

Unità di misura: popolato automaticamente in base all’ID misura precedentemente selezionato

Fase: campo facoltativo, popolabile tramite picklist contenente i valori “R”, “S”, “T”

All’interno del Tab Difformità e Interferenze sono presenti anche le tabelle “Difformità validate e da validare rilevate sull’asset” e “Interferenze validate e da validare rilevate sull’asset” tramite le quali l’utente può non confermare le difformità/interferenze rilevate. Di seguito si riportano le informazioni contenute nelle tabelle:

Asset: campo in sola lettura. Vengono restituiti solo gli asset per i quali è possibile non confermare la presenza di difformità/interferenze tramite il presente odl, ossia gli asset associati alle difformità/interferenze rilevabili censite all’interno del pdl.

Descrizione Asset: campo in sola lettura

Classificazione asset: campo in sola lettura

SAP ID: campo in sola lettura

Id difformità/interferenza: campo in sola lettura. Rappresenta l’ID della difformità/interferenza da catalogo

Id diffomità/interferenza a db: campo in sola lettura. Rappresenta il codice univoco della difformità/interferenza a db

Tipologia: colonna presente solo per le difformità: campo in sola lettura

Gravità: campo in sola lettura

Stato: campo in sola lettura

Matricola validatore: numero matricola di chi ha validato la difformità/interferenza

Nome validatore: Nome e cognome matricola validatore

Non confermato?: flag editabile. Se valorizzato si procede con la “non conferma” della difformità/interferenza. Una volta valorizzato questo flag la difformità/interferenza, che prima si trovava in stato DA VALIDARE o VALIDATA, passerà in stato SUPERATA con causale di superamento “non conferma”

Si specifica che:

Nel caso di controlli: verranno mostrate solo le difformità/interferenze (rilevate precedentemente sugli asset) definite come “rilevabili” all’interno del PdL.

Nel caso di manutenzioni/sostituzioni: verranno mostrate solo le difformità/interferenze (rilevate precedentemente sugli asset) definite come “risolvibili” all’interno del PdL.

In questo modo sull’OdL si ha evidenza delle difformità che verranno risolte dall’OdL una volta che quest’ultimo viene portato in ESEGUITO.

Per le interferenze arboree è visibile anche il numero di collisioni che hanno determinato la registrazione dell’interferenza.

In alternativa, è possibile registrare difformità, interferenze o misure in associazione ai micro-controlli eseguiti durante l’attività di controllo (relativamente ai soli asset a cui i micro-controlli fanno riferimento). Nello specifico, espandendo il record del singolo micro-controllo, visibile all’interno della tabella dei Micro-controlli del tab “Avanzamento”, l’utente può registrare le difformità, interferenze o misure direttamente sul micro-controllo, mediante la valorizzazione dei medesimi campi descritti per le tabelle “Difformità/interferenze rilevate con il presente odl”. Le rilevazioni possibili sono quelle riportate nel catalogo micro-controllo come difformità, interferenze o misure rilevabili. Vanno valutate anche le caratteristiche dell’asset sulla base delle specifiche di catalogo.

Dopo aver inserito le informazioni relative alle difformità/interferenze/misure da associare al micro-controllo, l’utente può procedere con il salvataggio (si evidenzia che, per difformità/interferenze che richiedono l’allegato, il salvataggio avviene soltanto dopo che lo stesso è stato aggiunto). Se le misure per il micro-controllo risultano obbligatorie da catalogo, lo stesso non può passare in stato eseguito prima che siano tutte registrate. In particolare, appare un messaggio di errore che avvisa l’utente che deve inserire le misure mancanti per la chiusura.

#### Registrazione nuove difformità/interferenze e misure tramite Mobile

Tramite una funzionalità custom è possibile su EAM Mobile registrare difformità/interferenze o misure rilevate durante controlli e micro-controlli.

Per quanto riguarda la gestione di controlli che richiedono l’esecuzione di micro-controlli, la registrazione delle difformità o delle misure relative ai micro-controlli è effettuata tramite una maschera specifica. Accedendo all’applicazione “My schedules” e selezionando l’ordine di interesse tra quelli assegnati alla propria utenza si può accedere alla lista dei micro-controlli come già descritto nel capitolo 12.4.2 e rappresentato nella Figura 190. La lista dei micro-controlli può essere filtrata, tramite le lentina presente nel riquadro 1 di Figura 190, per SAP-ID padre, descrizione micro-controllo, classificazione asset oggetto di micro-controllo.

Figura 190 - Vista elenco micro-controlli

Inoltre, tramite la lentina riportata nel riquadro 2 di Figura 190 è possibile ricercare puntualmente i micro-controlli sottostanti allo specifico stallo selezionato, tramite la picklist di seguito riportata

Successivamente alla selezione dei filtri ed entrati nel singolo micro-controllo viene restituita la schermata riportata in Figura 191.

In questa maschera è riportata la lista dei micro-controlli da eseguire e tramite il tasto evidenziato mediante il cerchio rosso della Figura 191 è possibile registrare difformità/interferenze/misure relative al singolo micro-controllo. Inoltre, cliccando sull’icona evidenziata con il riquadro rosso, l’utente può consultare lo storico delle difformità già registrate sull’asset, rappresentato in Figura 192.

Figura 191 -Lista micro-controlli

Figura 192 - Lista delle difformità registrate sull'asset

Cliccando sul tasto per la registrazione delle difformità/interferenze/misure si apre la schermata in Figura 193, dove selezionare l’anomalia da registrare, il target, la misura, il valore di misura, e l’eventuale flag di risoluzione contestuale (è anche possibile aggiungere note se necessario). Il campo difformità è selezionabile tramite una tendina che presenta le difformità/interferenze/misure rilevabili tramite quel micro-controllo e sulla base delle caratteristiche dell’asset indicate sullo specifico catalogo. I campi “Target” e “Misura” sono selezionabili tramite una tendina che riporta i valori possibili definiti a catalogo per la difformità/interferenza selezionata. Utilizzando il pulsante “+” è possibile allegare un’immagine alla difformità da registrare. Secondo quanto definito nel catalogo delle difformità e delle interferenze, per alcune di esse, l’allegato di una foto è requisito obbligatorio al momento della registrazione della stessa. In tali casi, il sistema informa l’utente, tramite il campo “Note”, l’obbligatorietà della foto. Se quest’ultima non viene inserita allora la difformità/interferenza non è visibile né validabile da WEB,

Una volta eseguito il micro-controllo, l’utente seleziona l’icona di “check” blu, che viene modificata in verde, come rappresentato nella Figura 193. L’icona verde indica che il micro-controllo è stato completato.

Figura 193 - Registrazione anomalia

Il processo di registrazione delle difformità/interferenze/misure associate direttamente all’ordine avviene nel seguente modo:

L’utente seleziona dall’applicazione “My schedules” l’OdL su cui desidera registrare una difformità/interferenza/misura. In questo esempio, l’OdL è relativo a un’attività di controllo “Ripristino corretta posizione del fascio di conduttore”, indicato nella Figura 194;

Figura 194 - Applicazione MySchedules

Dopo aver selezionato l’OdL, l’utente all’interno dell’ordine clicca sul tasto “Anomalie” rappresentato in Figura 195 per accedere alla maschera di registrazione difformità/interferenze, dove è necessario inserire le seguenti informazioni:

Selezionare l’asset sul quale si desidera registrare una difformità/interferenza. Si specifica che nella dialog di selezione dell’asset è presente una colonna “riferimento posizione” popolata secondo le seguenti regole:

Per conduttori/armamenti conduttore di 4° livello: viene riportato il valore di LIN_RIF_POSIZIONE

Per fdg/armamenti fdg di 4° livello: viene riportato il valore di LIN_RIF_FUNE

Per i restanti 4° livelli linea, relazionati agli asset sopra citati, viene riportato il valore di LIN_RIF_POSIZIONE/LIN_RIF_FUNE dell’asset a cui sono relazionati

Per gli equipment di 4° livello stazione: viene riportata la fase di riferimento dell’equipment

Scegliere la difformità/interferenza da aprire: Il sistema rende disponibile in questo campo solo le difformità/interferenze che sono rilevabili dallo specifico controllo per l’asset precedentemente inserito;

Determinare il livello di gravità: L’utente deve selezionare il livello di gravità della difformità/interferenza inserita.

Inserire eventuali note;

È possibile allegare foto nella sezione “Attachments” riportata in Figura 195 (l’allegato è logicamente associato all’ordine e non alla singola difformità; tuttavia, partendo dalla difformità è recuperabile passando attraverso l’ordine che l’ha rilevata). Per difformità/interferenze per cui è obbligatorio allegare una foto, viene mostrato in rosso un messaggio di richiesta di caricamento foto. Nel caso di mancato caricamento, la difformità/interferenza non viene salvata;

Indicare eventualmente la fase su cui deve essere associata la difformità/interferenza;

Figura 195 - Applicazione difformità/interferenze

Dopo aver selezionato l’OdL, l’utente all’interno dell’ordine clicca sul tasto “Misure” per accedere alla maschera di registrazione misure dove devono essere inserite le seguenti informazioni:

Selezionare l’asset sul quale si desidera registrare la misura;

Scegliere la misura da registrare (tra quelle rilevabili tramite il PdL e sulla base delle caratteristiche dell’asset);

Inserire il valore rilevato per la misura selezionata. Nel caso in cui la misura sia non numerica il valore può essere scelto attraverso una pop up che mostra l’elenco dei valori numerici che può assumere, affiancati dal valore alfanumerico corrispondente;

Nel caso in cui l’operatore inserisca sul medesimo asset una difformità uguale a una precedentemente rilevata, il sistema genererà un alert con scritto “difformità già presente su asset, procedere comunque con l’inserimento?”. Per distinguere le difformità l’operatore dovrà inserire nelle “note” i dettagli relativi ad ognuna.

Nel caso di registrazione di una difformità/interferenza uguale ma con differente valore del campo gravità il sistema chiederà se la difformità/interferenza con altro valore di gravità deve essere mantenuta o meno.

Casistica di gestione della piccola manutenzione

Per piccola manutenzione si fa riferimento ad attività per le quali è possibile risolvere una difformità contestualmente alla sua rilevazione.

Per risolvere una difformità contestualmente al suo inserimento, l’utente deve utilizzare il flag “risolto contestualmente”, che permette al sistema di tracciare la difformità direttamente in stato “Risolta”.

Negli OdL per i quali è presente una difformità con flag “Risolto contestualmente” viene applicata la seguente logica: nel momento in cui l’OdL passa allo stato “Eseguito” viene automaticamente generato un ulteriore OdL (portato direttamente dal sistema in stato “Chiuso”) associato a un PdL creato appositamente per il caso di risoluzione contestuale di una difformità. Tale PdL verrà applicato universalmente a ogni tipo di asset e difformità. Nell’odl generato verrà riportato il consuntivo fisico dell’asset associato alla difformità.

Il consuntivo orario sarà tracciato sull’OdL di controllo che ha rilevato la difformità.

Sull’OdL di risoluzione generato automaticamente non saranno quindi associate date di pianificazione, ma sarà direttamente riportata la data di esecuzione (coincidente con la data in cui è stato generato) e il fisico relativo all’asset. Non saranno neanche presenti record di consuntivazione oraria.

## Monitoraggio avanzamento e visualizzazione OdL

Monitoraggio dell’avanzamento dell’OdL

Maximo consente, tramite funzionalità standard, di monitorare l’avanzamento di un OdL mediante una rappresentazione grafica (Figura 196), utilizzando la funzionalità “Visualizza mappa Workflow” (Figura 196), la quale permette di vedere il flusso di lavoro associato a un odl, in particolare: i passaggi (nodi) del workflow, il percorso attuale dell’odl nel workflow, le transazioni possibili (cioè i percorsi che può prendere l’odl in base a determinate azioni o condizioni).

Figura 196 - Attivazione funzionalità di visualizzazione Workflow

Figura 197 - visualizzazione Workflow OdL

Visualizzazione grafica degli OdL nel centro di controllo

In MAS è previsto che l’utente abbia una panoramica degli OdL relativi all’UdB a cui appartiene, suddivisi per stato. Nello specifico, come illustrato nella Figura 198, sarà possibile visualizzare nel centro di controllo una vista di sintesi contenente gli OdL relativi al UdB dell’utente, con uno stato compreso fra:

Aperto;

Schedulabile;

Schedulato;

Inviato;

Avviato;

Eseguito parzialmente;

Eseguito;

Chiuso

Figura 198 - Vista di sintesi OdL nel centro di controllo

Nel centro di controllo è possibile anche il monitoraggio dell’avanzamento degli OdL basato sulle date. Il sistema evidenzia gli OdL in stato “Aperto” per i quali la data odierna è prossima alla data di inizio pianificata evidenziando in arancione quelli per i quali tale intervallo di tempo è inferiore alla soglia definita a sistema (intervallo utile per individuare il battente di OdL che si candidano, sulla base della finestra temporale, ad essere inviati a Geocall). Nell’esempio rappresentato nella Figura 199, per l’OdL colorato in arancione, l’intervallo tra la data attuale e la data di inizio pianificata è inferiore alla soglia predefinita, mentre per quello in blu, l’intervallo è superiore alla soglia definita a sistema.

La tabella di monitoraggio scadenze mostra una sintesi dei dati relativi agli OdL e alle date di interesse che comprende:

ID OdL;

Descrizione OdL;

Assetnum;

SAP ID

Data Pianificata Inizio;

Figura 199 - Monitoraggio date OdL

Visualizzazione ordini con filtri specifici

Nell’applicazione “Monitoraggio OdL” è possibile filtrare l’elenco degli OdL in base ai filtri presenti in testata. I filtri preimpostati in Figura 200 sono:

Ordine di lavoro: ID dell’OdL;

Odl padre: viene riportato l’odl padre dell’ordine di lavoro. Se l’ordine non ha un padre allora tale campo risulta null

ID PdL

ID Policy

Descrizione: Descrizione dell’OdL;

SAP ID: SAP ID dell’asset associato all’OdL;

SAP ID tronco (che sarà popolato solo per ordini che fanno riferimento a un tronco di ispezione sia aggregatori che figli, negli altri casi rimarrà vuoto);

Descrizione tronco

Descrizione asset di primo livello associato all’asset dell’ordine;

Descrizione asset terzo livello associato all’asset dell’ordine (popolato solo per asset al di sotto di asset di terzo livello);

Asset: assetnum dell’asset oggetto di odl

Descrizione Asset: Descrizione dell’asset associato all’OdL;

Perimetro intervento: Controllo, manutenzione, sostituzione;

Stato: Stato dell’OdL;

COIN;

POS-NR (popolato solo per asset di linea secondo livello);

Riferimento fase

Riferimento FdG

Riferimento conduttori;

Data inizio pianificata;

Data fine pianificata;

Data modello

Responsabile attività;

Figura 200 - Filtri di testata OdL

Oltre questi filtri tramite click su “available queries” sono rese disponibili le seguenti query:

"OdL Linea" con la quale vengono restituiti gli OdL con asset appartenenti alla gerarchia della linea;

"OdL Stazione" con la quale vengono restituiti gli OdL con asset appartenenti alla gerarchia della stazione;

“OdL Linea in stato da Aperto a Eseguito”;

“OdL Stazione in stato da Aperto a Eseguito”;

“OdL linea in stato da Aperto a Eseguito parzialmente con data pianificata fine nell’anno corrente”;

“OdL stazione in stato da Aperto a Eseguito parzialmente con data pianificata fine nell’anno corrente”;

“OdL in stato da Aperto a Eseguito parzialmente con data pianificata fine nell’anno corrente”.

Inoltre, Maximo permette tramite funzionalità standard di filtrare la ricerca per ciascun campo dell’OdL. Tramite la funzione di “Altri campi di ricerca” è possibile ricercare per campi non presenti sulla schermata. Oltre a quelli già presenti attualmente nella pop up, vanno aggiunti:

Data pianificata inizio;

Data pianificata fine;

Perimetro intervento.

Visualizzazione dello storico delle modifiche di stato dell’OdL

Maximo consente all’utente, tramite funzionalità standard, di visualizzare lo storico delle modifiche di stato dell’OdL in analisi. Selezionando l’azione “Storico OdL” nell’applicazione “Monitoraggio OdL” (Figura 201), l’utente accede alla schermata contenente lo storico relativo agli stati dell’OdL (Figura 201).

Figura 201 - Funzionalità "Storico OdL"

Di seguito si riporta la struttura della tabella di “storico stati”:

Stato: Stato dell’OdL;

Data Stato: Data nel formato GG.MM.AAAA e orario in cui è stato impostato lo stato;

Modificato da: Valorizzato con la matricola dell’utente quando il cambio stato è eseguito su EAM. Quando invece il cambio stato avviene su input di sistemi esterni verrà riportato il nome del sistema esterno.

Nota: Campo dedicato all’inserimento di un giustificativo per il cambio di stato eseguito;

Figura 202 - Storico stati

# Chiusura OdL

## Processo di chiusura OdL

Per gli OdL specifiche richiedono un processo di chiusura a tre punte (associati ad attività con tipologia attività “Sostituzioni”, “Demolizione” o “Nuove costruzioni / installazioni”), di seguito è rappresentata l’interfaccia presentata all’utente per eseguire i passaggi necessari alla chiusura. Il tab “Conferme per chiusura” appare solo se l’ordine (quando si trova in stato “eseguito”) ha un PdL associato che fa riferimento ad una delle tipologie per cui è richiesto e se il tipo spesa dell’ordine risulta CAPEX. In particolare, l’utente può:

Indicare l’avvenuto caricamento di moduli FVI, M04 e M06 a sistema;

Indicare l’avvenuto caricamento di aggiornamenti anagrafici a sistema;

Verificare l’invio completo dei consuntivi verso SAP (flag automatico impostato dal sistema una volta inoltrati a SAP tutti i consuntivi relativi all’OdL in oggetto).

Figura 203 - Schermata conferma chiusura processo a tre punte

Il campo “Matricola” viene valorizzato manualmente dall’utente che ha flaggato i punti richiesti per la chiusura del processo a “tre punte”, il quale inserisce il proprio numero di matricola.

# Assegnazione e ruoli figure OdL

## Assegnazione responsabilità e attività consentite su OdL

In questo capitolo vengono descritte le figure responsabili delle attività e le operatività che queste possono eseguire durante le varie fasi di gestione degli OdL. Nella Figura 204 viene riportata una vista strutturale dell’organizzazione definita a sistema. In particolare, sono definite a sistema due macrostrutture organizzative: il Dipartimento/Distretto di trasmissione e l’Asset Management. A queste sono sottese le seguenti unità:

Dipartimento/Distretto di trasmissione

Area tecnica

Presidio sicurezza e ambiente

Supporto gestionale

Unità impianti

Asset Management

Policy e Modelli Ingegneristici

Asset Intelligence

Governance PAM

Metodi di Lavoro

Asset Management Operation Center

Programma e Supporto AM

Figura 204 - Struttura organizzativa Terna

Nell’ambito degli OdL, sono identificabili le seguenti figure responsabili per ciascuna attività:

Responsabile asset

Responsabile attività

Titolare risorsa (TitRis)

Il responsabile asset può essere rivestito esclusivamente da un’unità impianti (l’informazione corrisponde al campo in anagrafica: UI/DT).

Il responsabile attività, che è responsabile della pianificazione dell’attività, può appartenere alle seguenti unità sottese al Dipartimento/Distretto di trasmissione e Asset management:

Dipartimenti / Distretto trasmissione

Area tecnica

Realizzazione locale

Misure e prove locali

COAM

Presidio sicurezza e ambiente

Safety e Ambiente

Monitoraggio Cantieri

Supporto Gestionale

Unità Impianti

Asset management

Policy e Modelli Ingegneristici

Asset intelligence

Governance PAM

Metodi di lavoro

Asset Management Operation Center

Programma e supporto AM

Infine, il Titolare risorsa può essere una delle seguenti unità sottese al Dipartimento/Distretto di trasmissione e Asset Management:

Dipartimento/Distretto di trasmissione

Area tecnica

Realizzazione locale

Misure e prove locali

COAM

Presidio sicurezza e ambiente

Safety e Ambiente

Monitoraggio Cantieri

Supporto Gestionale

Unità Impianti

Asset Management

Policy e modelli ingegneristici

Asset intelligence

Governance PAM

Metodi di lavoro

Asset Management Operation Center

Programma e supporto AM

Queste informazioni vengono visualizzate in EAM solo con le sigle presenti. Nelle integrazioni verso SAP e Tagetik, l’informazione viene invece trascodificata.

Assegnazione figure di responsabilità OdL

Questo capitolo descrive come vengono assegnate le figure di responsabilità dell’OdL in fase di generazione dell’attività e come queste possono essere successivamente modificate.

Responsabile asset

In fase di generazione dell’attività, sia per OdL da DSS sia per OdL manuali, il responsabile asset (che corrisponde ad un’unità impianti) viene recuperato dal relativo attributo dell’asset (campo in anagrafica: UI/DT). Questo non può essere mai modificato né in fase di pianificazione né di esecuzione. Linea multi-competenza dove sarà possibile modificare il “responsabile asset” selezionando una delle UI associate nella tabella di riepilogo dati dell’asset (non può essere lasciato blank). Per gli OdL creati da DSS con logica tronco viene applicata la seguente regola: in fase di creazione dell’ordine aggregatore sotto cui associare gli ordini delle relative campate il DSS assegna all’ordine padre il responsabile asset di un ordine casuale fra i figli;

Responsabile attività

In fase di generazione dell’attività, nel caso di attività generate dal DSS, il responsabile attività viene valorizzato in funzione degli attributi del PdL se presente, altrimenti sarà uguale al responsabile asset (es se sul PdL è segnato come responsabile COAM e l’OdL è associato ad un asset sul distretto nord allora il responsabile attività viene valorizzato: COAM distretto nord). Gli ordini relativi a UdB non UI potranno essere gestiti anche da utenti UI opportunamente profilati.

Nel caso di attività generate manualmente, il responsabile attività viene inserito dall’utente.

In fase di pianificazione dell’attività, è modificabile fino allo stato “Schedulabile” dell’OdL, sia per OdL UI che per OdL non UI

In fase di esecuzione dell’attività non è modificabile né per OdL UI né per OdL non UI

Titolare risorsa

In fase di generazione dell’attività viene automaticamente valorizzato come il responsabile attività.

In fase di pianificazione dell’attività è modificabile fino allo stato “Schedulabile” sia per OdL UI che per OdL non UI

In fase di esecuzione è possibile aggiungere TitRis nel caso di OdL UI. Nel caso in cui ci sia la necessità di schedulare la risorsa l’utente deve regredire lo stato dell’OdL ad “Aperto” ed aggiungere in EAM il TitRis, per poi inviare nuovamente l'attività in Geocall.

Il TitRis può essere aggiunto anche in caso di OdL non UI, ma questo viene effettuato direttamente in EAM in fase di esecuzione (senza regredire quindi lo stato dell'OdL).

Attività consentite per figura

Nel seguito vengono indicate le operatività consentite a ciascuna figura in fase di generazione dell’attività, pianificazione ed esecuzione.

Responsabile asset

In fase di generazione dell’attività non è eseguibile alcuna operatività

In fase di pianificazione dell’attività può visualizzare gli OdL.

In fase di esecuzione dell’attività può visualizzare gli OdL

Il responsabile asset può eventualmente gestire gli OdL nelle fasi di pianificazione ed esecuzione nel caso in cui venga definito come Responsabile attività un soggetto che non opera in EAM (es. Ingegneria).

Responsabile attività

In fase di generazione dell’attività può procedere con la creazione manuale dell’OdL

In fase di pianificazione dell’attività può effettuare le seguenti operatività:

Modificare la pianificazione

Cambiare il responsabile attività

Modificare il TitRis

Avanzare lo stato dell’OdL

In fase di esecuzione dell’attività può chiudere gli OdL e, per OdL UI, può anche validare le difformità se autorizzato. Per le attività di ispezioni lidar, solo AM è autorizzato a validare le difformità.

Titolare risorsa

In fase di generazione dell’attività non è eseguibile alcuna operatività

In fase di pianificazione dell’attività può visualizzare gli OdL

In fase di esecuzione dell’attività può effettuare le seguenti operazioni:

Stornare le ore (per tutti gli OdL)

Per OdL UI può validare e inviare verso SAP i consuntivi orari, inserire difformità ed eseguire micro-controlli

Per OdL non UI può inserire ed inviare i consuntivi verso SAP

Modifica Responsabile Attività

L’utente all’interno della pagina di monitoraggio OdL, può modificare il campo “Responsabile Attività” andando a scegliere un nuovo responsabile attività da una lista pre-filtrata contente tutte le UdB del dipartimento/distretto dell’asset oggetto di OdL.

Selezionato il responsabile attività scelto, cliccando sul tasto workflow, come mostrato in Figura 205, viene aperto un pop-up tramite il quale l’utente può selezionare “Riassegnazione Ordine” e cliccando su “OK” il sistema salva il nuovo responsabile attività.

Figura 205 - Pop up di riassegnazione di OdL

Una volta riassegnato l’ordine, solamente gli utenti appartenenti al gruppo indicato nel campo “Responsabile Attività” possono lavorare sull’ OdL e vedranno l’OdL nella propria vista elenco dell’applicazione monitoraggio OdL.

# Reportistica

In questo capitolo vengono descritte due tipi di reportistiche consultabili in EAM:

reportistica operativa: include la reportistica relativa all’assegnazione attività e alla visualizzazione degli OdL aperti per un determinato asset;

reportistica tecnico/economica: comprende i report relativi ai programmi, alla reportistica ANASCO e alla reportistica ANR che saranno fruibili fuori dal MAS in un apposito strumento di BI.

Il sistema permette di esportare su file Excel i report prodotti dagli utenti. Nel caso in cui la mole dei dati da esportare sia superiore alla capacità di un singolo foglio xls, il sistema effettua l’esportazione su più fogli.

## Reportistica operativa

Reportistica di interferenze rilevate / risolte

I report di interferenze rilevate e risolte fruibili da EAM permettono di avere una vista generale su tutte le interferenze che sono state rilevate e/o risolte su ogni singolo Asset. Per lanciare il report viene richiesto che siano forniti i seguenti parametri di lancio:

DT Responsabile asset

UI Responsabile asset

Livello di tensione

Asset di primo livello

Asset di secondo livello

Asset di terzo livello

Asset di quarto livello

Società proprietaria (BURKS)

Tipo Attività (PdL associato all’OdL di prima rilevazione)

Intervallo di tempo data prima rilevazione

Intervallo di tempo data ultima rilevazione

Stato interferenza

Dopo aver definito i parametri di lancio, l’utente avvia la generazione del report che riporterà le seguenti colonne:

Codice interferenza rilevata

Descrizione interferenza

Codice interferenza a catalogo

Gerarchia asset

Livello di tensione

Stato asset

Asset di primo livello (linea)

Classificazione Asset a cui è associata

Lunghezza campata (per le sole interferenze relative alla campata, FdG e conduttori)

Proprietario asset

DT responsabile asset

UI responsabile asset

Data prima rilevazione

Data ultima rilevazione (se diversa dalla prima)

OdL di prima rilevazione

PdL di prima rilevazione

OdL di ultima rilevazione

PdL di ultima rilevazione

Stato interferenza

Ambiente (LIN_ZONA_AMBIENTALE)

Zona inquinamento (LIN_ZONA_INQUINAMENTO)

Data chiusura

Note

Gravità rilevata (se presente)

Collisioni aggregate (numero di piante che definiscono l’interferenza)

Reportistica di difformità rilevate / risolte

I report di difformità rilevate e risolte fruibili da EAM permettono di avere una vista generale su tutte le difformità che sono state rilevate e/o risolte per su ogni singolo Asset. Per lanciare il report viene richiesto che siano forniti i seguenti parametri di lancio:

DT Responsabile asset

UI Responsabile asset

Livello di tensione

Asset di primo livello

Asset di secondo livello

Asset di terzo livello

Asset di quarto livello

Proprietario *

Intervallo di tempo data rilevazione

Stato difformità

Dopo aver definito i parametri di lancio, l’utente avvia la generazione del report che riporterà le seguenti colonne:

Codice difformità rilevata

Descrizione difformità

Codice difformità a catalogo

Gerarchia asset

Livello di tensione

Stato asset

Asset di primo livello

Classificazione Asset a cui è associata

Lunghezza campata

Proprietario asset

DT responsabile asset

UI responsabile asset

Data prima rilevazione

Data ultima rilevazione

OdL di prima rilevazione

PdL di prima rilevazione

OdL di ultima rilevazione

PdL di ultima rilevazione

Stato difformità

Data chiusura

Note difformità

Ambiente (LIN_zona tronco)

Zona inquinamento (LIN_Inquinamento)

Gravità rilevata (se presente)

Misura (se presente)

Reportistica opere interferenti

I report delle opere interferenti fruibili da EAM permettono di avere una vista generale su tutte le opere interferenti presente a sistema. Per lanciare il report viene richiesto che siano forniti i seguenti parametri di lancio:

DT Responsabile asset

UI Responsabile asset

Livello di tensione linea

Asset di primo livello

Asset di secondo livello

Dopo aver definito i parametri di lancio, l’utente avvia la generazione del report che riporterà le seguenti colonne:

Codice opera interferente

Descrizione opera interferente

Tipologia opera interferente

Sotto-tipologia

Data rilievo

Ente gestore

SAP ID

Asset di primo livello

Asset di secondo livello

Livello di tensione linea

DT Responsabile asset

UI Responsabile asset

Criticità

Reportistica delle misure

I report delle misure fruibili da EAM permettono di avere una vista generale delle misure presenti a sistema. Per generare il report viene richiesto che siano forniti i seguenti parametri di lancio:

Intervallo di tempo data rilevazione

Tipologia PdL (controllo)

Asset di primo livello

Asset di secondo livello

Asset di terzo livello

Asset di quarto livello

DT Responsabile asset

UI Responsabile asset

Dopo aver definito i parametri di lancio, l’utente avvia la generazione del report che riporterà le seguenti colonne:

Codice misura rilevata

Descrizione misura

Valore misura

Unità di misura

Codice misura a catalogo

Asset a cui è associata

DT responsabile asset

UI responsabile asset

OdL di rilevazione

Tipologia Attività di rilevazione (Controllo/Micro controllo)

Difformità (associata alla misura caricata)

Reportistica di tasso di guasto / tasso di anomalia

I report di tasso di guasto e tasso di anomalia permettono di consultare lo stato degli Asset di primo livello presenti a sistema e sono fruibili da EAM. Il report di tasso di guasto e anomalia riporta in campi distinti tutte le informazioni anagrafiche che sono state utilizzate per definire il perimetro di calcolo dei tassi (ossia DT, UI, classe componente, costruttore e modello). Nel caso in cui il TdG venga definito per gli interruttori di uno specifico DT, quindi senza specificare modello o costruttore, questi campi rimangono vuoti nel report. Per generare il report viene richiesto che siano forniti i seguenti parametri di lancio:

Intervallo di tempo

Asset di primo livello

Classe componente

DT responsabile asset

UI responsabile asset

Costruttore asset

Modello asset

Il report di tasso di guasto e tasso di anomalia sono aggregati secondo i seguenti parametri:

Modello

Costruttore

Altri attributi da definire

Dopo che sono stati forniti i parametri di lancio, il sistema genera il report che riporterà le seguenti colonne:

Classe componente

Gerarchia asset

DT

UI

Modello (alternativi)

Costruttore (alternativi)

(Altri attributi da definire) (alternativi)

TdA

TdG

Una volta inserito l’intervallo di tempo e le specifiche degli asset su cui valutarlo, viene calcolato il tasso di guasto o di anomalia tramite la formula relativa (rapporto tra la sommatoria dei guasti/anomalia rilevati sugli asset oggetto di calcolo e la consistenza degli stessi nello stesso intervallo di tempo).

Reportistica HI di linee aeree

I report di HI di linee aeree permettono di analizzare l’Health index delle linee gestite a sistema. I valori storici degli HI saranno storicizzati su EAM. Gli HI non saranno storicizzati ad ogni calcolo ma solo una volta al mese.

Per generare il report viene richiesto che siano forniti i seguenti parametri di lancio:

Asset di primo livello

Asset di secondo livello

Asset di terzo livello

Classificazione asset

Livello di tensione della linea

Proprietario

DT responsabile asset

UI responsabile asset

Dopo forniti i parametri di lancio, il sistema genererà il report con le seguenti colonne:

Codice linea

Descrizione linea

Livello di tensione

Proprietario

Codice campata

Lunghezza campata

DT

UI

Anno installazione conduttore

Conduttore

Tipo conduttore

HI conduttore totale (per ogni conduttore f)

HI massimo tra quelli dei conduttore (quarto livello) dello stesso conduttori (terzo livello)

Singoli termini dell’HI conduttore

Anno installazione FdG SX, DX

Tipo FdG SX, DX

HI totale FdG SX, DX

Singoli termini dell’HI FdG SX, DX

Anno installazione sostegno

Tipo sostegno

HI sostegno

Singoli termini dell’HI sostegno

Reportistica HI di linee in cavo

I report di HI di linee in cavo permettono di analizzare l’Health index delle linee in cavo gestite a sistema. I valori storici degli HI saranno storicizzati su EAM. Gli HI non saranno storicizzati ad ogni calcolo ma solo una volta al mese

Per generare il report viene richiesto che siano forniti i seguenti parametri di lancio:

Asset di primo livello

Asset di secondo livello

Asset di terzo livello

Classificazione asset

Proprietario

Livello di tensione della linea

DT responsabile asset

UI responsabile asset

Dopo forniti i parametri di lancio, il sistema genererà il report con le seguenti colonne Codice linea

Descrizione linea

Proprietario

Codice tratta cavo

HI totale cavo

Singoli termini HI cavo

HI totale giunti

Singoli termini HI giunti

HI totale terminali

Singoli termini HI terminali

HI totale tratta

Reportistica HI di componenti di stazione

I report di HI di componenti di stazione permettono di analizzare l’Health index dei componenti presente a sistema. I valori storici degli HI saranno storicizzati su EAM. Gli HI non saranno storicizzati ad ogni calcolo ma solo una volta al mese

Per generare il report viene richiesto che siano forniti i seguenti parametri di lancio:

Asset di primo livello (stazione)

Asset di secondo livello (sezione)

Asset di terzo livello (stallo)

Asset di quarto livello (equipment)

DT responsabile asset

UI responsabile asset

Dopo forniti i parametri di lancio, il sistema genera il report riportando le seguenti informazioni distribuite in colonne:

DT

UI

Codice stazione

Descrizione stazione

Proprietario

Sede tecnica di terzo livello

Descrizione terzo livello

Descrizione componente (Interruttore, TA, TV ecc)

Codice equipment

Descrizione equipment

Vetustà

HI equipment

HI Macchinario totale

HI dielettrico, termico, meccanico, olio

Livello di tensione

Data costruzione

Data entrata in servizio

Reportistica di lotti tecnici omogenei

I report “lotti tecnici omogenei” permettono analizzare i diversi lotti attribuiti alle campate gestite a sistema. Per generare il report, è necessario fornire i seguenti parametri di lancio:

Asset di primo livello

Livello di tensione

DT

UI

Una volta determinati i parametri di lancio, il sistema genera il report che riporterà le seguenti colonne:

DT

UI

Codice Lotto tecnico omogeneo

Descrizione

Codice linea

Codice campata

Criticità

Materiale conduttore

Diametro conduttore

Anno installazione conduttore

Materiale fune di guardia

Diametro fune di guardia

Anno installazione fune di guardia

Posizione (intesa come posizione della campata nella linea)

HI conduttore

Reportistica di consuntivazione ore e fisico

DI report “Consuntivazione ore e fisico” permettono di monitorare le ore consuntivate dalle singole risorse. Il report riporta i seguenti parametri di lancio:

Divisione responsabile attività

UdB responsabile attività

Inquadramento risorsa

Codice OdL

Divisione responsabile Risorsa

UdB Responsabile Risorsa (Titris)

Tipologia attività PdL

COIN

Classificazione asset

Gerarchia asset

Mese consuntivazione risorsa

Anno consuntivazione risorsa

Tipo spesa

Stato OdL

Dopo aver definito i parametri di lancio, l’utente avvia la generazione del report che riporterà le seguenti colonne:

Matricola risorsa

Inquadramento risorsa

Fisico consuntivato (aggregato per OdL e riportato solamente su una delle matricole riferite al medesimo OdL, sulle altre matricole sarà riportato 0)

Unità di misura fisico

Divisione responsabile Risorsa

UdB Responsabile Risorsa (Titris)

Codice OdL

Cliente OdL

Responsabile attività

Responsabile asset

Tipologia attività PdL

ID PdL

Flg_Outsourcing: popolato con 1 se odl in outsorcing; con 0 se in insourcing

Classificazione asset

Gerarchia asset

Stato asset

SAP ID

Assetnum

Descrizione asset

Stato OdL

Mese consuntivazione risorsa

Anno consuntivazione risorsa

Data inizio consuntivazione risorsa

Data fine consuntivazione risorsa

Data esecuzione Odl: data in cui l’odl passa in ESEGUITO

Tipo spesa

Co.In

CBS/CBE

OI

NTK

Operazione NTK

ODM

Tipo ordine ODM

Tipo ordine OI

Tipo attività PM

Flag invio a SAP

Ore esecuzione

Ore trasferimento

Ore ufficio

Data modello

Data inizio pianificata

Data fine pianificata

Parametro di aggregazione: Matricola risorsa

Reportistica di avanzamento OdL

I report “Avanzamento OdL” permettono di monitorare l’avanzamento degli ordini pianificati a sistema:

Divisione responsabile attività

UdB responsabile attività

UI responsabile asset

Divisione responsabile Risorsa (Titris)

UdB Responsabile Risorsa (Titris)

Tipologia attività PdL

CoIn

Classificazione asset

Gerarchia asset

Intervallo di tempo data pianificata fine

Intervallo di tempo data pianificata inizio

Tipo spesa

Stato OdL

Dopo aver definito i parametri di lancio, l’utente avvia la generazione del report che riporterà le seguenti colonne:

Codice OdL

Descrizione OdL

Tipo ordine

ID PdL associato (attività da catalogo)

Descrizione PdL

Tipologia attività PdL

SAP-ID: SAPID corrispettivo dell'asset dell'ordine di lavoro comprensivo di ultimo livello sede tecnica

Gerarchia asset

Tipo spesa

Informazione COIN

Divisione responsabile attività

UdB responsabile attività

UI responsabile asset

Data inizio pianificata

Data fine pianificata

Data esecuzione

Data chiusura

Data modello

Stato OdL

Fisico pianificato

Fisico consuntivato

Odl padre

Flag aggregato

Unità di misura

Consuntivo ORE

ODM

OI

NTK

CBE/CBS

Cliente (informazione dell’OdL, TR01 se Asset Terna, TR36 se asset ex RFI – Rete Srl)

Reportistica di avanzamento OdL con informazioni di pianificazione risorse esterne ed interne

I report di avanzamento OdL con informazioni di pianificazione risorse esterne ed interne sono fruibili da EAM. Per generare il report viene richiesto che siano forniti i seguenti parametri di lancio:

DT responsabile attività

UI responsabile attività

UI responsabile asset

DT responsabile Risorsa (Titris)

UdB Responsabile Risorsa (Titris)

Tipologia attività PdL

CoIn

Classificazione asset

Intervallo di tempo data pianificata inizio

Intervallo di tempo data pianificata fine

Tipo spesa

Stato OdL

Flag PMI

Dopo aver definito i parametri di lancio, l’utente avvia la generazione del report che riporterà le seguenti colonne:

DT responsabile attività

UdB responsabile attività

Codice ODL

Odl padre

Descrizione OdL

Fisico pianificato: Al fine di evitare double counting inerire una riga specifica solo per il dato fisico (quindi con il resto delle risorse nulle)

Stato OdL

Aggregato

Flag PMI

Tipo spesa

Informazione COIN

Tipo ordine

ID PdL associato

Descrizione PdL

Tipologia attività PdL

Cliente

Indisp. Necessaria

Data Modello

Data Pianificata inizio OdL

Data Pianificata fine OdL

ANNO

MESE (data fine pian.)

UI responsabile asset

SapID 1 LIVELLO

Descrizione SapID 1 LIVELLO

SapID

SapID 3° livello

Descrizione SapID 3 LIV

Classificazione asset

UdB responsabile risorsa (TITRIS)

Risorse interne pianificate

Data inizio pianificata risorse interne

Data fine pianificata risorse interne

Mese pianificato RISORSE INTERNE (calcolata da data fine pianificata della risorsa)

Quantità Risorse interne

ORE ESECUZIONE

ORE TRASFERIMENTO

ORE TOTALI (campo calcolato = ore esecuzione + ore trasferimento * q.tà risorse)

Tipo Risorsa (usato per discriminare PRESTAZIONI, PERSONALE, MATERIALI, FISICO)

ID Materiale

Descrizione ID Materiale

Quantità Risorse esterne

UM risorse esterne

Costo risorse esterne (in €)

Serbatoio

Esempio report finale:

Reportistica rapporto di ispezione

In EAM, all’interno del singolo odl, l’utente avrà la possibilità di accedere al report “Rapporto di ispezione” strutturato come segue:

Sezione 1:

Nome del report (“Rapporto di ispezione”)

Data esecuzione ispezione

Sap Id e descrizione del 1° livello padre dell’asset oggetto di ispezione

Livello di tensione: tensione collegamento relativo all’asset di primo livello

Matricola e nome preposto

Firma: campo blank da firmare una volta esportato il report in pdf

Sezione 2:

Macro-attività: codice macro-attività

Data esecuzione ispezione

Tronco ispezionato

Sezione 3:

SAP ID, assetnum e descrizione dell’asset oggetto di odl

Matricola e nome del primo utente che ha inserito la difformità

Sezione 4:

Assetnum e classificazione dell’asset su cui viene rilevata la difformità/interferenza

Descrizione difformità/interferenza rilevata

Gravità difformità/interferenza rilevata

Descrizione difformità/Interferenze confermata

Gravità difformità/Interferenze confermata

Descrizione difformità/Interferenze non confermata

Gravità difformità/Interferenze non confermata

Note: Note indicate in fase di rilevazione

Sezione 5:

Note Odl: in questo campo vengono riportate tutte le note segnate all’interno dell’OdL

Il report sarà esportabile in formato PDF.

## Integrazione con Data Platform Terna

Al fine di produrre reportistica legata agli scenari di pianificazione e al PMI, è previsto l’utilizzo della Data Platform Terna alimentata periodicamente con i dati presenti in MAS.

In particolare, ogni giorno tramite interfaccia dedicata viene effettuato un allineamento per delta della base dati in Data Platform leggendo il database MAS.

Le entità funzionali che vengono trasferite in Data Platform sono:

Elenco completo OdL con relativi campi di testata

Pianificazione di risorse interne ed esterne per OdL

Risorse assegnate agli OdL

Consuntivazione oraria OdL

Anagrafica PdL legati a OdL

costi personale interno e risorse esterne

anagrafica Asset legati a OdL (sia campi di testata che attributi)

Anagrafica Policy

Domini (elenco completo dei domini EAM)

Difformità rilevate dagli OdL

Il dettaglio dei campi trasferiti per ciascuna entità funzionale elencata è riportato nel documento “XXX”.

Tramite la base dati in Data Platform e uno strumento di BI è possibile produrre la reportistica relativa agli scenari e al PMI strutturata come descritto nel documento di dettaglio “EAM – UC112 Requisiti funzionali”.
