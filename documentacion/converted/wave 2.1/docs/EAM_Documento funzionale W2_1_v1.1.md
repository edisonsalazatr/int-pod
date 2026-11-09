# EAM_Documento funzionale W2_1_v1.1

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento | Documento funzionale W2.1 |

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

# Obiettivi

Il presente documento ha lo scopo di descrivere la soluzione in termini funzionali per quanto riguarda gli adeguamenti in ambito W2.1. In particolare, saranno descritte le logiche che devono essere implementate on top a quelle rilasciate nella W2.0 per abilitare i requisiti individuati per la W2.1.

Per identificare le logiche inerenti all’ambito di questo documento, già comprese nel rilascio della W2.0 è necessario fare riferimento ai documenti di seguito elencati:

Analisi funzionale Complessiva V4.1;

Documento funzionale DSS V6.0;

Analisi tecnica DSS V

# Definizione di relazioni tra asset

## Concetti generali

I cataloghi su cui si applicano le regole descritte nel resto del documento sono i seguenti:

Catalogo Policy: contente le policy che rappresentano le regole con cui vengono elaborate le informazioni di consistenza sugli asset per decidere se generare ordini di lavoro a sistema. Ogni policy può essere associata ad un unico elemento asset e a più classificazioni asset. L’elemento asset a cui è associata deve essere lo stesso a cui è associata la macro-attività che viene indicata come output alla policy nel campo “Macro-attività”. Per decidere se generare ordini di lavoro il sistema legge nella policy quali condizioni devono essere rispettate. Le condizioni nella policy sono indicate come input (uno o più di uno) che possono essere combinati logicamente tra di loro.

Catalogo Difformità/Interferenze: questi due cataloghi contengono rispettivamente tutte le possibili difformità e interferenze rilevabili sugli asset della consistenza. Le difformità rappresentano condizioni anomale proprie dell’asset che possono inficiarne le funzionalità. Le interferenze rappresentano anomalie non determinate dalle condizioni dell’asset stesso ma da agenti esterni che ne possono inficiare le funzionalità. Vengono rilevate sul campo dagli operatori tramite l’applicazione mobile oppure direttamente da WEB. Le difformità e le interferenze possono essere considerate come input alle policy. Sul catalogo la singola difformità o interferenza viene riferita ad un unico elemento asset su cui può essere rilevata ma a più classificazioni. Per le difformità e interferenze è anche possibile indicare uno o più attributi proprio delle classificazioni a cui fa riferimento l’oggetto di catalogo, per i quali, sulla base del valore assunto da questi attributi è possibile o meno rilevare la difformità. Ad esempio, una difformità può essere indicata come rilevabile sulla base del tipo di isolamento (attributo anagrafico) specificato sull’asset.

Nel seguito del documento, con il termine “oggetto” si identifica il singolo record di catalogo, mentre con il termine “Elemento” si identifica l’insieme di asset riferiti allo stesso asset fisico che sono distinti per caratteristiche specifiche diverse. Ad esempio, nelle stazioni (asset di primo livello) sotto lo stallo (asset di terzo livello) sono presenti 8 tipologie di interruttore (asset di quarto livello) denominate classificazioni (che rappresentano un asset con caratteristiche specifiche proprio) che fanno riferimento allo stesso elemento “INTERRUTTORE AIS”.

È utile, prima di entrare nel dettaglio delle soluzioni applicative descritte in questo documento, spiegare i meccanismi generali di gestione delle informazioni di sistema associate all’asset che vengono elaborate per la generazione degli ordini da parte del DSS.

Tramite l’esecuzione di ordini di lavoro di tipo controllo, possono essere registrate a sistema entità come difformità, misure e interferenze associandole ad asset specifici. Gli asset su cui possono essere rilevati questi oggetti sono indicati per il singolo oggetto nel catalogo (come descritto precedentemente). Le misure come altri parametri sono input alle variabili di stato che possono essere calcolate sugli asset specifici. Tutti questi elementi sono selezionabili come input alle policy che definiscono le regole secondo le quali il DSS genera gli ordini. Gli ordini generati a loro volta possono essere utilizzati per rilevare difformità, misure e interferenze (se di tipo controllo) oppure per risolverle (se di tipo manutenzione/sostituzione o altre tipologie).

Con il rilascio 2.0 è possibile indicare in input alle policy esclusivamente oggetti di catalogo che fanno strettamente riferimento (nel relativo catalogo) allo stesso elemento e alle stesse classificazioni asset a cui fa riferimento la policy (cioè l’asset su cui verrà generato eventualmente l’ordine in output). Questo perché il sistema deve sapere esattamente su quale asset “leggere” gli input indicati nella policy.

In questo documento quindi si specificheranno le logiche per permetteranno al sistema di individuare inequivocabilmente l’asset su cui leggere anche se questo non corrisponde esattamente all’asset di riferimento della policy ma è relazionato con quest’ultimo.

Oltre al caso degli input policy su altro asset, sono descritti anche gli adeguamenti necessari per permettere al sistema di filtrare la lista di difformità da mostrare all’utente sull’ordine in fase di rilevazione non solo in funzione di attributi presenti sull’asset stesso ma anche di attributi presenti su altri asset relazionabili e inequivocabilmente determinabili all’asset di riferimento.

Nel file allegato di seguito sono state riportate tutte le combinazioni di asset con la relativa regola di associazione possibili per il rilascio 2.1:

Il file è popolato come segue:

Nella colonna “Asset di riferimento” è riportato l’asset a cui fa riferimento l’oggetto di catalogo. Per capire meglio il concetto, la schermata successiva rappresenta un esempio di catalogo. In particolare, il catalogo delle difformità, in cui è presente il campo “Elemento asset” di associazione tra l’oggetto di catalogo difformità Diff.L.1.2.2.1 e l’elemento su cui questo oggetto può essere rilevato cioè il GIUNTO FUNE DI GUARDIA. La tabella sottostante il campo “Elemento asset” (popolata in automatico dal sistema sulla base dell’associazione anagrafica tra elemento e classificazioni asset) riporta l’elenco delle classificazioni facenti riferimento all’elemento associato nel campo “Elemento asset”. L’asset di riferimento della difformità, quindi, è l’elemento GIUNTO FUNE DI GUARDIA. Tutti i cataloghi degli oggetti possiedono il campo “Elemento asset” e la tabella “Classificazione asset”, necessari per identificare tipologie asset a cui fanno riferimento gli oggetti di catalogo.

Figura 1: esempio catalogo difformità

Nella colonna “Cardinalità” è riportata la cardinalità della relazione anagrafica tra gli asset;

Nella colonna “Asset da cui leggere” è riportato l’asset su cui il sistema deve effettuare la “lettura” delle informazioni che sono state indicate come input per abilitare la logica da applicare;

Nella colonna “Regola” è definita la regola che permette di identificare inequivocabilmente gli asset su cui applicare le logiche di lettura. N;

Nella colonna “Eccezioni” sono riportate eventuali regole aggiuntive relative a casi particolari per i quali non è sufficiente la regola generale definita nella colonna precedente per identificare inequivocabilmente l’asset.

Per meglio comprendere le regole che saranno descritte nel seguito del documento è necessario distinguere il concetto di relazione con quello di regola.

La relazione identifica il rapporto gerarchico che sussiste tra due asset e dipende dalla tipologia di asset a cui ci si sta riferendo. Le relazioni gerarchiche sono differenti nel caso in cui ci si stia riferendo ad asset di tipo Linea aerea, Linea in cavo o Stazione. Tutte le tipologie di gerarchie sono costruite su 4 livelli. Al primo livello è rappresentato l’asset più alto in gerarchia sotto il quale sono collegati gli asset sottostanti, i quali ereditano alcune informazioni dall’asset di primo livello. Scendendo nella gerarchia vengono identificati differenti rami gerarchici che possono o meno avere relazioni tra di loro. In generale, gli asset allo stesso livello su rami gerarchici differenti possono condividere l’asset gerarchicamente superiore.

La regola invece è la logica specifica che va adottata dal sistema per identificare inequivocabilmente l’asset da cui leggere partendo dall’asset a cui l’oggetto a catalogo fa riferimento.

Nei paragrafi successivi sono descritti nel dettaglio i concetti di relazione e di regola.

## Relazioni anagrafica dell’asset

Le relazioni anagrafiche rappresentano i collegamenti gerarchici che sussistono tra gli asset. Di seguito un esempio di relazioni anagrafiche per quanto riguarda gli asset di linee aeree.

Figura 2: Esempio gerarchia Asset

Come è possibile notare con l’aiuto del color coding, i rami gerarchici sono identificati dalle differenze di colore e di sfumature. Al secondo livello sono presenti solo due tipologie di asset al di sotto dei quali sono presenti due terzi livelli per ciascuno dei secondi. I terzi livelli sotto lo stesso secondo condividono il padre. Oltre alla gerarchia va anche considerata la cardinalità delle relazioni gerarchiche. Ad esempio, Ogni linea può avere sotto di sé più campate aeree e più sostegni. Ogni campa aerea ha un solo sostegno di fine campata. Ogni campata può avere sotto di sé N conduttori e N funi di guardia.

Le rappresentazioni delle relazioni per gli asset anagrafici di linee aeree, linee in cavo e stazione sono presenti nel file allegato al capitolo precedente denominato Regole di associazione asset rispettivamente nei fogli Gerarchia_Linee Aeree, Gerarchia_Linee in cavo e Gerarchia_Stazioni.

Di seguito è riportata una generalizzazione delle possibili relazioni che intercorrono tra gli asset e la loro cardinalità applicabile a tutte e tre le tipologie di asset:

L’asset su cui leggere è ad un livello superiore sullo stesso ramo gerarchico: la cardinalità è 1 a 1, in questi casi la relazione gerarchica è sufficiente per poter determinare inequivocabilmente l’asset;

L’asset su cui leggere è ad un livello superiore ma su un ramo gerarchico differente: la cardinalità è 1 a N quindi sono definite regole specifiche in base alla casistica;

L’asset su cui leggere è ad un livello inferiore sullo stesso ramo: la cardinalità è 1 a N quindi sono definite regole specifiche in base alla casistica;

L’asset su cui leggere è ad un livello inferiore ma su un ramo gerarchico differente: la cardinalità è 1 a N quindi sono definite regole specifiche in base alla casistica;

L’asset su cui leggere è allo stesso livello ma su un ramo gerarchico differente: la cardinalità è 1 a N quindi sono definite regole specifiche in base alla casistica.

Per quanto riguarda gli asset di tipo stazione, per quanto le gerarchie siano più complesse di quelle delle linee, le combinazioni possibili sono gestite tramite regole generiche definite tra quelle possibili anche per gli asset di linea. Nel paragrafo successivo è riportato il dettaglio e la definizione delle possibili regole di associazione degli asset

## Regole di associazione

Le regole di associazione, come accennato nel paragrafo 2.1 sono l’elemento centrale per abilitare la gestione delle nuove logiche implementate con il rilascio 2.1. Infatti, se non è possibile identificare univocamente o tramite regole specifiche gli asset su cui leggere, il sistema non riesce ad applicare la logica di lettura.

Per fare un esempio pratico, immaginiamo che il sistema stia elaborando una logica su uno specifico asset dell’anagrafica di tipo elemento CONDUTTORE, e che si voglia leggere un’informazione su un asset anagrafico di tipo elemento FUNE DI GUARDIA. Dato che il conduttore e la fune di guardia sono due asset di quarto livello sotto rami gerarchici differenti, perché associati a terzi livelli diversi, non è possibile determinare su quale fune di guardia di quarto livello leggere l’informazione basandosi esclusivamente sul riferimento dell’asset conduttore su cui si sta applicando la logica in quanto la loro relazione è sempre 1:3. Infatti, sotto una CAMPATA AEREA ci sono 3 asset di tipo FUNE DI GUARDIA (quarto livello) quindi la cardinalità è 1:3 e di conseguenza anche la cardinalità della relazione CONDUTTORE (specifico)-FUNE DI GUARDIA è 1:3. Le regole sostanzialmente servono per ridurre la relazione tra gli asset da 1:N a 1:1 o tuttalpiù identificare una semplificazione che valga per tutte le casistiche di quel tipo.

Sono state individuate e classificate le seguenti regole di associazione degli asset in funzione della tipologia su cui possono essere utilizzate.

Regole generali che valgono per tutte le tipologie di asset:

Regola 1: Almeno 1, la cardinalità è 1 a N quindi il sistema per poter applicare la logica deve assicurarsi che almeno 1 degli asset presi in considerazione rispettino la condizione indicata nel catalogo;

Regola 4: L’asset gerarchicamente superiore (a uno o più livelli di gerarchia sopra), la cardinalità è sempre 1 a 1 quindi la selezione è inequivocabile.

Regola 11: per le relazioni tra quarti livelli sotto lo stesso padre, si applica la regola 1 (almeno uno sotto lo stesso padre deve rispettare la condizione).

Regole che hanno valore per asset di Linea Aerea:

Regola 2: Per gli asset che hanno l’attributo LIN_RIF_ASSET_COND di tipo GIUNTO CONDUTTORE, ANTIVIBRANTI CONDUTTORE, SFERE DI SEGNALAZIONE CONDUTTORE relazionati con asset di tipo CONDUTTORE va considerato il conduttore specifico il cui codice è riportato nel campo LIN_RIF_ASSET_COND. Nel caso in cui l’attributo fosse vuoto, va utilizzata la regola 1.

Regola 3: Per gli asset che hanno l’attributo LIN_RIF_ASSET_FDG di tipo GIUNTO FUNE DI GUARDIA relazionati con asset di tipo FUNE DI GUARDIA va considerata la fune di guardia specifica il cui codice è riportato nel campo LIN_RIF_ASSET_FDG. Nel caso in cui l’attributo fosse vuoto, va utilizzata la regola 1.

Regola 5: Per gli asset che hanno l’attributo LIN_RIF_ASSET_ARM_COND di tipo ISOLATORE CONDUTTORE relazionati con asset di tipo ARMAMENTO CONDUTTORE va considerato l’armamento conduttore il cui codice è riportato nel campo LIN_RIF_ASSET_ARM_COND.

Regola 6: per asset ISOLATORE CONDUTTORE 4° livello relazionati a CAMPATA AEREA, dato che la campata aerea è un 2° livello di gerarchia differente, si risale dall’isolatore conduttore al sostegno 2° livello gerarchicamente superiore (relazione di cardinalità 1 a 1 quindi inequivocabile) poi dal sostegno si individua la campata per la quale il sostegno è quello di fine campata (regola inequivocabile).

Regola 7: identifica la relazione tra un quarto livello e un terzo livello di altra gerarchia. In particolare, per le relazioni tra ISOLATORE CONDUTTORE con CONDUTTORI (terzo livello), ARMAMENTO FUNE DI GUARDIA con CONDUTTORI e FUNI DI GUARDIA (terzi livelli) la regola è quella di risalire al secondo livello sostegno e individuare la campata il cui sostegno è quello di fine, da lì scendere al terzo livello e applicare la regola 1 (almeno 1 dei terzi presenti sotto la campata deve rispettare la condizione).

Regola 10: per l’asset FUNE DI GUARDIA relazionato al CONDUTTORI (terzo livello), si individuano tutti i conduttori sotto la stessa campata della fune e si applica la regola 1 (almeno uno dei conduttori deve rispettare la condizione).

Regole che hanno valore per asset di tipo Linea in cavo:

Regola 8: per asset di quarto livello sotto la gerarchia cavi del secondo livello ESTREMO CAVO relazionati ad asset di terzo e quarto livello sotto la gerarchia di secondo livello TRATTA CAVO la regola da applicare è quella di risalire dal quarto livello al secondo livello tratta cavo (cardinalità 1 a 1 quindi inequivocabile), la tratta cavo ha una relazione inequivocabile con l’estremo di fine tratta quindi è possibile scendere sull’estremo della tratta fino al terzo o al quarto livello indicato nella relazione e applicare la regola dell’almeno 1 (almeno 1 degli asset deve rispettare la condizione).

Regola 9: per l’asset GIUNTO CAVO relazionato al CAVO DI POTENZA che si trovano sotto rispettivamente al secondo ESTREMO CAVO e TRATTA CAVO che sono relazionati dalla regola precedentemente descritta (estremo di fine tratta) si risale ai cavi di potenza e si identifica quello che ha lo stesso valore del GIUNTO CAVO nel campo LIN_RIF_FASE.

Regole che hanno valore per asset di tipo Stazione:

Regola 12: per asset Macchina relazionati con sistema di raffreddamento, visto che sono collegati anagraficamente, si considerano i sistemi di raffreddamento sotto lo stesso padre della macchina in valutazione e tra questi (che possono essere massimo 3) si prende in considerazione quello specifico che ha lo stesso SAPID nel campo X3SAPID della macchina.

Come è possibile notare, alcune delle regole identificate sono una combinazione di regole che fanno riferimento alla gerarchia specifica e alla regola 1 (almeno 1). È chiaro che la combinazione di queste due regole serve per ridurre il cono di possibili asset su cui effettuare le letture in funzione del fatto che non sarebbe corretto verificare le logiche su asset che non hanno alcuna correlazione gerarchica con l’asset di riferimento.

Se ritorniamo al caso descritto all’inizio di questo paragrafo, relativo al CONDUTTORE e alla FUNE DI GUARDIA possiamo spiegare meglio questo concetto. Come abbiamo detto, il CONDUTTORE è figlio di un CONDUTTORI terzo livello che si trova sotto una specifica campata della linea di riferimento. Sotto la linea però sono presenti N campate quindi potenzialmente il CONDUTTORE potrebbe essere riferito a tutte le N FUNE DI GUARDIA della linea. Per ridurre il cono si usa quindi la regola anagrafica. Si risale dal CONDUTTORE sulla CAMPATA AEREA di riferimento (padre del terzo livello a cui fa riferimento il conduttore) dalla campata, dato che il terzo livello FUNI DI GUARDIA può essere uno solo si scende sul terzo FUNI e si individuano le 3 FUNE DI GUARDIA di quarto livello che fanno riferimento alla fune della stessa campata del terzo livello del conduttore. In questo modo la relazione che potenzialmente era 1:N diventa 1:3 e si applica la regola dell’almeno 1 sulle tre FUNE DI GUARDIA individuate.

Di seguito una rappresentazione grafica del processo descritto per meglio fisare il concetto.

Figura 3: Rappresentazione combinazione regole

È giusto soffermarsi un momento sulla regola dell’almeno uno per spiegare meglio come la interpreta il sistema. Abbiamo detto che le regole sono applicate per leggere informazioni su asset relazionati a quello su cui si sta effettuando una qualche logica, in particolare la generazione di un ordine da parte di una policy o la rilevazione di una difformità da parte dell’operatore sul campo.

Quando viene applicata la regola del “almeno uno” il sistema deve verificare che su almeno uno degli asset di quella tipologia relazionati all’asset di riferimento si deve verificare la condizione indicata in input alla policy o l’attributo specificato nella difformità deve assumere quel valore.

Riprendendo l’esempio più volte utilizzato del CONDUTTORE e della FUNE DI GUARDIA e facile intuire che una volta risalita la gerarchia e individuate le 3 funi di guardia sotto il terzo livello coerenti con il conduttore specifico che si sta elaborando, il sistema valuta se almeno una di queste funi di guardia individuate è coerente con la condizione definita a catalogo. Nel caso di esito positivo della verifica applica la logica.

Per quanto riguarda invece le eccezioni, ne sono state individuate due:

L’eccezione applicata a tutte le regole che sfruttano un attributo specifico dell’asset per ridurre la cardinalità a 1:1, se l’attributo non è popolato va applicata prima la regola gerarchica e poi la regola dell’almeno 1.

L’eccezione applicata esclusivamente ad asset presenti sulle gerarchie di linee aeree per le quali è stato effettuato un ammazzettamento. In questo caso, anagraficamente possono essere presenti 2 conduttori terzo livello sotto la stessa campata aerea.

Quindi per le relazioni che prendono in considerazione un qualsiasi asset di questo ramo gerarchico della campata, va considerato che le relazioni per i conduttori ammazzettati si duplicano quindi eventualmente si individuano non più 3 conduttore quarto livello ma 6 riferiti ai due conduttori terzo livello, la regola dell’almeno 1 è ancora applicabile.

L’eccezione applicata ad asset di tipo campata e sostegno che fanno riferimento a linee di derivazione. In particolare, per queste casistiche le campate di rientro non hanno lo stesso SAPID del sostegno di fine. Per individuare quindi il sostegno di fine delle campate di rientro derivazione va ricercato il sostegno sotto la stessa linea che ha la SEQUENCE che finisce con AZ.

L’eccezione applicata ad asset di tipo campata e sostegno che fanno riferimento allo split. Le linee splittate possono essere di due tipologie: bifase (sono presenti due campate, una monofase e una bifase); monofase (sono presenti tre campate ognuna monofase). In questi casi i sostegni di chiusura dello split hanno il SAPID solo di una delle due o tre campate che sono attaccate al sostegno. Le campate che non hanno lo stesso SAPID del sostegno non hanno alcuna associazione al sostegno di fine, quindi, è impossibile determinare il sostengo relazionato per leggere le informazioni relative all’altro ramo gerarchico.

Le regole indicate in questo capitolo fanno riferimento esclusivamente alle combinazioni presenti sugli oggetti che saranno previsti in W2.1.

Le regole di relazione spiegate in questo paragrafo, sono riportate a sistema in una tabella in sola visualizzazione identificata con il nome “Regole relazione asset”. La tabella contiene i seguenti campi:

Tipologia asset: se gli asset a cui si riferisce la relazione sono di linea aerea, linea in cavo o stazione;

Asset di riferimento: asset a cui fa riferimento l’oggetto di catalogo con indicazione del livello anagrafico;

Cardinalità: riporta la cardinalità della relazione tra l’asset di riferimento e l’asset da cui leggere;

Asset da cui leggere: asset su cui basare le logiche di lettura delle informazioni con indicazione del livello anagrafico;

Regola: regola specifica che governa la relazione tra i due asset della riga. La regola è codificata con i numeri indicati nel capitolo;

Descrizione regola: viene riportata una descrizione sintetica della regola indicata alla colonna precedente;

Eccezione: eventuale regola di eccezionalità se previsti per quella casistica di relazione;

Catalogo: catalogo sul quale viene utilizzata la relazione.

La tabella risulta in sola visualizzazione.

# Adeguamenti UI e algoritmi di elaborazione

Le soluzioni descritte nel capitolo precedente, in termini di concetti generali sono applicati a due cluster di seguito riportati:

Rilevabilità difformità attraverso attributi su asset diversi da quelli della difformità;

Policy con input e Output su asset differenti.

Per poter permettere al sistema di gestire queste due casistiche devono essere portati degli adeguamenti sia in termini di UI dei cataloghi di interesse che per quanto riguarda alcune logiche di elaborazione.

I cataloghi impattati dalle modifiche richieste per abilitare le funzionalità sopra elencate sono il catalogo delle policy e il catalogo delle difformità.

Nel catalogo delle policy andranno aggiunte/adeguate alcune tabelle per configurare correttamente la casistica di associazione di input che sono riferiti ad asset differenti da quello di riferimento della policy stessa.

Nel catalogo difformità, similmente a quello delle policy, devono essere adeguate e aggiunte le tabelle per configurare la selezione di caratteristiche per la rilevabilità che risiedono su asset differenti da quello di riferimento della difformità in oggetto.

Il presente capitolo, quindi, elenca le modifiche ma spiega anche come sono applicati i concetti generali descritti nel capitolo precedente ad ognuna delle casistiche individuate.

### Policy con input e Output su asset differenti

In questo paragrafo sono elencati gli adeguamenti da effettuare sul catalogo delle policy, in termini di UI e tutti gli adeguamenti a livello di elaborazione DSS che vanno gestiti per poter abilitare la gestione di policy con input e output su asset differenti.

#### Catalogo policy

Le policy all’interno del catalogo possono essere configurate associandole ad un unico elemento in output (tramite la tabella di associazione elemento asset) e a più classificazioni come mostrato nella schermata seguente.

Figura 4: Associazione asset output alla policy

In questo modo la policy viene elaborata esclusivamente su asset presenti in anagrafica che sono identificati con una delle classificazioni nella tabella “Classificazioni asset”. Per tanto gli ordini forniti in output possono essere associati esclusivamente a questo tipo di asset.

Per quanto riguarda invece la configurazione degli input alla policy (se presenti), possono essere relazionati ad altri asset differenti da quello output alla policy. In particolare, il sistema durante l’elaborazione della policy sul singolo asset, deve ricercare le informazioni relative agli input per verificarne la coerenza con i target di raffronto su asset che sono relazionati all’asset di riferimento della policy secondo le regole descritte nel paragrafo 2.3. Per sapere su quale asset va effettuata la lettura degli input, l’utente configura una tabella denominata “Asset Input policy”. La tabella deve essere popolata necessariamente dopo aver selezionato l’asset della policy. Infatti, non sarà visibile finché l’utente non avrà inserito gli altri campi obbligatori e non avrà salvato a sistema la policy.

Una volta utilizzato il tasto “+” per l’aggiunta di una nuova riga nella tabella devono essere popolati i seguenti campi:

Classificazione asset: campo di selezione della classificazione da cui leggere l’input, scelto dall’utente da una lista valori contente esclusivamente asset che sono abbinati all’asset della policy nella tabella “Regole relazione asset”. Ad esempio, se viene scelto come elemento associato alla policy il CONDUTTORE gli asset selezionabili nella tabella input sono esclusivamente quelli che sono presenti nella tabella di regole nella colonna “Asset da cui leggere” quando nella colonna “Asset di riferimento” c’è CONDUTTORE;

Descrizione: descrizione anagrafica dell’asset, popolato dal sistema e non modificabile;

Regola: scelta dall’utente da una lista contenete le possibili regole relative alla combinazione asset definita nella riga.

Figura 5: Tabella di inserimento elementi per input

È ancora possibile comunque associare input relativi all’asset della policy, in questo caso non è necessario indicare l’asset relativo all’elemento della policy nella tabella “asset input policy”.

Una volta configurata la tabella asset input policy con tutti gli asset da cui dovranno essere letti gli input, si deve configurare la tabella Input policy. In questa tabella vengono configurati gli input che il sistema deve verificare per determinare se generare o meno gli ordini relativi all’elaborazione in corso. La configurazione avviene tramite il tasto “+” nella tabella, attraverso il quale si crea una nuova riga. Di seguito i campi da popolare per configurare un input:

Asset: selezionato dall’utente da una lista valori contente esclusivamente gli asset inseriti nella tabella precedente. Questo campo va inserito per primo all’interno della riga;

N° input: popolato dal sistema in automatico e non modificabile;

Tipologia input: selezionato dall’utente da una lista valori, indica il tipo di input che si sta configurando (Difformità, Interferenza, Attributo, ecc);

ID input: sulla base dell’elemento asset scelto e della tipologia di input, il sistema propone gli ID di catalogo compatibili con queste due informazioni. Ad esempio, se è stato scelto elemento interruttore e tipologia difformità, il sistema restituisce esclusivamente gli ID difformità presenti nel catalogo che fanno riferimento all’elemento interruttore.

Descrizione input: descrizione dell’input recuperato in automatico dal sistema in funzione dell’ID scelto. Rimane non modificabile.

Misura: scelta dall’utente esclusivamente se la tipologia input è Misura al posto di inserire l’ID input. I due campi sono alternativi per distinguere gli input di tipo misura e variabili di stato che si trovano sullo stesso catalogo.

Operatore/funzione: scelto dall’utente da una lista valori che riporta gli operatori EXIST, NOT EXIST, <, >, =, ≠, Pari, Dispari. Indica l’operatore con il quale va valutato l’input.

Data: target nel caso in cui l’input sia di tipo DATEVALUE.

Target raffronto: target nel caso in cui i possibili valori dell’input siano pescati da un dominio, vengono proposti gli stessi valori, tra cui l’utente può scegliere.

Target di raffronto numerico: target nel caso in cui l’input sia di tipo NUMVALUE.

Di seguito è rappresentata la tabella con l’aggiunta del campo Elemento asset.

Figura 6: Tabella input policy adeguata

#### Adeguamenti elaborazioni DSS

Il DSS (Decision Support System) rappresenta il motore di elaborazione delle policy che sulla base della consistenza asset e degli eventi di sistema gestisce la generazione di ordini automatica. In particolare, leggendo le informazioni definite nelle policy e sulla base di quanto presente a sistema in termini di informazioni associate agli asset anagrafici, effettua delle valutazioni per decidere se generare un ordine su un determinato asset. Di seguito una rappresentazione delle modalità di elaborazione.

Figura 7: Schema di funzionamento DSS

Come già accennato nel paragrafo precedente il DSS elabora le policy sul singolo asset anagrafico effettuando una serie di step di elaborazione dettagliatamente spiegati all’interno del documento “EAM_Documento DSS_v6”.

Il DSS agisce in due modalità, on condition e periodica. La differenza sta nell’attivazione delle elaborazioni. Per la modalità on condition l’attivazione della policy e quindi dell’elaborazione sul singolo asset è determinata su base condizione tramite dei trigger definiti. L’elaborazione genera ordini esclusivamente se sono rispettate le condizioni input. Per la modalità periodica invece l’elaborazione sugli asset, di tutte le policy periodiche presenti nel catalogo è scatenata da un trigger temporale definito in un giorno specifico dell’anno (configurato e non modificabile da GUI). Una volta partite le elaborazioni, se presenti input, gli ordini vengono generati anche sulla base del rispetto delle eventuali condizioni.

Per le policy on condition le elaborazioni sono suddivise in 4 categorie sulla base della tipologia di policy. Le periodiche invece sono suddivise in 3 categorie di elaborazione.

Tutte le categorie di elaborazione sono gestite dal DSS tramite una serie di step alla conclusione dei quali (se arrivati fino all’ultimo step) si determina la generazione dell’OdL in output alla policy sull’asset oggetto di elaborazione.

Il DSS tramite le informazioni indicate sulla policy genera l’ordine associando dati specifici di pianificazione, in particolare date pianificate per l’esecuzione dell’attività.

Di seguito è riportata una schematizzazione degli step di elaborazione di una policy on condition -standard (cioè che non presenta ulteriori logiche specifiche di generazione) con l’aggiunta degli step necessari per adeguare l’algoritmo alla gestione di input riferiti ad asset diversi da quello di elaborazione.

Il DSS avvia l’elaborazione della policy sul singolo asset in modo indipendente. Nel caso in cui non ci siano input riferiti ad altri asset diversi da quello di elaborazione, il processo prosegue come descritto nel flusso standard sul file EAM_Documento DSS_v6. Se gli input della policy sono riferiti ad asset diversi da quello di elaborazione, prima di avviare le valutazioni sugli input, devono essere caricati gli asset corretti. In particolare, tramite la relazione gerarchica e le regole definite sulla tabella di associazione Elemento asset input policy, il sistema risale a tutti gli asset anagrafici che gli servono per effettuare le valutazioni. Per spiegare meglio il meccanismo facciamo un esempio pratico.

La policy ha in input una difformità che è riferita agli asset fune di guardia di quarto livello, l’asset in elaborazione è un conduttore quarto livello. Per prima cosa si individuano gli asset fune di guardia che sono determinati dalla regola definita a sistema per la combinazione indicata. Il sistema carica l’anagrafica degli asset fune di guardia che si trovano sotto lo stesso secondo livello del conduttore in elaborazione. A questo punto, sapendo che l’input è di tipo difformità, si ricerca l’ID difformità nella tabella X4DIFFORSTAT (tabella su cui vengono registrate le difformità rilevate sugli asset associate all’assetnum) associata ad una delle tre funi su cui verificare, dato che la regola è almeno 1 basta che una delle tre abbia la difformità. Se presente un target di raffronto, la difformità individuata viene anche confrontata con il target. Questo vale per tutte le tipologie di input. A seconda della tipologia input le informazioni vengono ricercate sulle varie tabelle di sistema in cui è presente il riferimento all’asset:

Attributo à ASSETSPEC, cercando il valore che assume l’attributo indicato nel campo ID attributo per quell’asset e confrontandolo con il target dell’input.

Attributo (di testata) à ASSET, cercando il valore che assume l’attributo indicato nel campo ID attributo per quell’asset e confrontandolo con il target dell’input.

Interferenza à X4INTERFSTAT, cercando interferenze con ID presente nel campo ID input associate all’assetnum e con eventuale valore di gravità se presente nel target di raffronto.

Difformità à X4DIFFORSTAT, cercando difformità con ID presente nel campo ID input associate all’assetnum e con eventuale valore di gravità se presente nel target di raffronto.

Attività à WORKORDER, cercando ordini di lavoro in stato “APERTO”, “SCHEDULABILE”, “SCHEDULATO”, “INOLTRATO”, “AVVIATO” associati all’asset con PdL indicato nel campo ID input.

Misure/variabili di stato à X4MISUREASSET, cercando misure o variabili di stato con ID presente rispettivamente nel campo Misura e ID input associate all’asset confrontandolo con il valore del target di raffronto numerico.

HI à AHSCORE, cercando HI con codice indicato nel campo ID input associati all’asset in valutazione.

Una volta valutati gli input singolarmente, è possibile effettuare le valutazioni combinate secondo quanto definito dalle combinazioni logiche sulla policy.

Se le valutazioni danno esito positivo, si passa agli step successivi del processo di elaborazione che comprendono:

Selezione del PdL specifico sulla base della macro-attività output alla policy sull’asset dell’elaborazione.

Valutazione dell’idoneità dell’asset, gli asset devono trovarsi in uno stato definito “idoneo” per la generazione dell’ordine.

Se l’asset è in stato idoneo, si verifica che non siano presenti altri ordini (in stato “APERTO”, “SCHEDULABILE”, “SCHEDULATO”, “AVVIATO”) sull’asset con stesso PdL e generati dalla stessa policy. Se non presenti si prosegue allo step successivo

Valutazione della prevalenza definita da catalogo sugli ordini già a sistema. In particolare, nel catalogo dei PdL è possibile associare altri PdL che risultano prevalenti rispetto a quello in oggetto. Il DSS applica la prevalenza verificando la presenza a sistema di ordini già aperti associati PdL prevalsi o prevalenti rispetto a quello dell’ordine in generazione.

Figura 8: Schema di valutazione Prevalenza

Generazione dell’ordine a sistema. Il DSS alla generazione dell’ordine valorizza una serie di campi sull’ordine, tra cui le date di pianificazione. Gli ordini vengono generati in stato “da validare” se generati da policy “on condition”, in stato “aperto” se generati da policy “periodiche”.

### Rilevabilità difformità/interferenze attraverso attributi su asset diversi da quelli della difformità/interferenza

Sul catalogo difformità e su quello delle interferenze viene indicato come parametro di rilevabilità un attributo che non fa riferimento all’asset a cui è associato l’oggetto a catalogo. Per leggere il valore dell’attributo in fase di rilevazione difformità/interferenza, il sistema individua l’asset su cui leggere tramite la regola definita nel capitolo 2.3. Sulla base del valore che rileva, permette la selezione o meno della difformità/interferenza.

#### Catalogo difformità

Nel catalogo delle difformità è possibile indicare una caratteristica asset e il relativo valore sulla base del quale può essere rilevabile o meno la difformità sullo specifico asset. Oltre a poter selezionare caratteristiche relative alle classificazioni dell’elemento associato alla difformità, è possibile inserire altri elementi a cui possono essere riferite le caratteristiche.

Figura : Tabella di associazione caratteristiche asset

Nella maschera del catalogo difformità si aggiunge una tabella indicata con il nome “Classificazione caratteristiche asset”. Nella tabella possono essere aggiunti uno o più elementi a cui faranno riferimento gli attributi per i quali il sistema deve valutare la rilevabilità della difformità. La tabella contiene i seguenti campi:

Classificazione asset: selezionato dall’utente rappresenta l’asset da cui leggere il valore dell’attributo in fase di rilevazione difformità. La lista mostrata dipende dall’asset a cui fa riferimento la difformità; infatti, possono essere selezionati asset per i quali è implementata a sistema la regola di relazione con l’asset di riferimento della difformità (definite nel file allegato al capitolo 2.1);

Descrizione: descrizione dell’asset popolato automaticamente dal sistema;

Regola: scelta dall’utente da una lista contenete le possibili regole relative alla combinazione asset definita nella riga.

Figura : Tabella classificazione caratteristiche asset

Si aggiunge una riga per ogni asset che andrà considerato per la lettura delle caratteristiche.

Nella tabella denominata “caratteristiche asset” è possibile inserire gli attributi e il relativo valore, sulla base dei quali la difformità deve essere rilevabile. Se viene inserito più di un attributo, saranno valutati in combinazione logica “AND”.

Nella tabella “caratteristiche asset” è presente un nuovo campo che rappresenta la classificazione asset sul quale in anagrafica si trova l’attributo che va valutato per determinare la rilevabilità. L’asset deve essere il primo campo popolato per permettere al sistema di pre-filtrare gli altri campi in funzione dell’asset scelto. Di seguito i campi della tabella:

Classificazione asset: Selezionato dall’utente da una lista valori contente gli asset inseriti nella tabella “Classificazione caratteristiche asset”;

Caratteristica: scelta dall’utente da una lista contente le possibili caratteristiche asset associate alla classificazione inserito nel campo precedente;

Valore: scelto dall’utente tra i possibili valori assunti dalla caratteristica selezionata nel campo precedente.

Figura : Tabella caratteristiche asset adeguata

#### Catalogo Interferenze

Nel catalogo delle interferenze è possibile indicare una caratteristica asset e il relativo valore sulla base del quale può essere rilevabile o meno l’interferenza sullo specifico asset. Oltre a poter selezionare caratteristiche relative alle classificazioni dell’elemento associato all’interferenza, è possibile inserire altri elementi a cui possono essere riferite le caratteristiche.

Figura : Tabella di associazione caratteristiche asset

Nella maschera del catalogo interferenza si aggiunge una tabella indicata con il nome “Classificazione caratteristiche asset”. Nella tabella possono essere aggiunti uno o più elementi a cui faranno riferimento gli attributi per i quali il sistema deve valutare la rilevabilità dell’interferenza. La tabella contiene i seguenti campi:

Classificazione asset: selezionato dall’utente rappresenta l’asset da cui leggere il valore dell’attributo in fase di rilevazione interferenza. La lista mostrata dipende dall’asset a cui fa riferimento l’interferenza; infatti, possono essere selezionati asset per i quali è implementata a sistema la regola di relazione con l’asset di riferimento dell’interferenza (definite nel file allegato al capitolo 2.1);

Descrizione: descrizione dell’asset popolato automaticamente dal sistema;

Regola: scelta dall’utente da una lista contenete le possibili regole relative alla combinazione asset definita nella riga.

Figura : Tabella classificazione caratteristiche asset

Si aggiunge una riga per ogni asset che andrà considerato per la lettura delle caratteristiche.

Nella tabella denominata “caratteristiche asset” è possibile inserire gli attributi e il relativo valore, sulla base dei quali l’interferenza deve essere rilevabile. Se viene inserito più di un attributo, saranno valutati in combinazione logica “AND”.

Nella tabella “caratteristiche asset” è presente un nuovo campo che rappresenta l’asset sul quale in anagrafica si trova l’attributo che va valutato per determinare la rilevabilità. L’asset deve essere il primo campo popolato per permettere al sistema di pre-filtrare gli altri campi in funzione dell’asset scelto. Di seguito i campi della tabella:

Classificazione asset: Selezionato dall’utente da una lista valori contente gli elementi inseriti nella tabella “Classificazione caratteristiche asset”;

Caratteristica: scelta dall’utente da una lista contente le possibili caratteristiche asset associate alla classificazione inserita nel campo precedente;

Valore: scelto dall’utente tra i possibili valori assunti dalla caratteristica selezionata nel campo precedente.

Figura : Tabella caratteristiche asset adeguata

#### Rilevazione difformità/interferenze da WEB e da Mobile

Le difformità/interferenze possono essere rilevate sugli asset attraverso un ordine sia da WEB che da applicazione mobile.

Figura : Maschera di rilevazione difformità/interferenze da WEB

Sulla maschera di rilevazione da WEB e da app mobile è possibile inserire una nuova difformità/interferenza scelta da una lista filtrata secondo i seguenti criteri:

Difformità/interferenze rilevabili tramite il PdL (di controllo) associato all’ordine in esecuzione;

Valori degli attributi indicati nella tabella “Caratteristiche asset” sul catalogo difformità/interferenze.

Quando le caratteristiche degli asset per la valutazione della rilevabilità non si trovano sull’asset su cui si sta rilevando la difformità/interferenza, il sistema per poter filtrare la lista di selezione deve prima verificare le relazioni tra gli asset. Tramite le regole definite nel capitolo 2.3 e inserite nella tabella di “Elemento caratteristiche asset” il sistema determina gli asset su cui vanno verificate le caratteristiche in base all’asset su cui si sta rilevando la difformità/interferenza.

Ad esempio, per una difformità da rilevare su GIUNTO FUNE DI GUARDIA in funzione di un attributo su FUNE DI GUARDIA si considera la fune di guardia con l’assetnum contenuto nell’attributo LIN_RIF_ASSET_FDG del Giunto fune di guardia su cui si sta rilevando (come indicato nella regola 3 descritta nel capitolo 2.3). Una volta individuati gli asset su cui fare le valutazioni, il sistema nella tabella ASSETSPEC verifica i valori degli attributi indicati nel catalogo difformità nella tabella “Caratteristiche asset” e li confronta con il valore indicato per ciascuno.

Nel caso in cui la regola sia almeno 1, il sistema verifica se almeno uno degli asset individuati ha il valore indicato nella tabella Caratteristiche asset.

Questa modalità di selezione della lista prefiltrata di difformità/interferenze rilevabili è gestita anche sul mobile. In particolare, dalla funzionalità di selezione della difformità/interferenza sull’asset in fase di esecuzione ordine, la lista fornisce le difformità/interferenze rilevabili per quell’asset anche in funzione di attributi su altri asset relazionati a quello di riferimento. Nella schermata di seguito è riportata l’applicazione di registrazione difformità/interferenze da mobile.

Figura : Maschera di registrazione difformità/interferenze da mobile

# Attivazione policy KO post go-live

Attualmente nei cataloghi polcy linee aeree, linee in cavo, stazioni da caricare in EAM sono presenti delle policy non gestibili con il rilascio 2.0. Queste policy sono quindi state caricate in uno stato non attivo e non saranno attivate al momento del rilascio 2.0.

Le logiche relative alla gestione delle policy KO descritte in questo documento ne abiliteranno l’attivazione in un momento successivo al go-live. In questo capitolo saranno descritti gli effetti che l’attivazione di queste policy determineranno sull’ambiente.

All’attivazione del DSS, nel rilascio 2.0, saranno anche caricati tramite MXLoader gli ordini relativi ad alcune policy KO, in particolare quelle periodiche. Dopo il rilascio il sistema inizierà a girare a regime e il DSS inizierà ad agire sulla consistenza anagrafica creando, chiudendo o modificando ordini coerentemente con le policy attive e con gli eventi di sistema. Il DSS non intercetterà gli eventi rilevanti per le policy non attive che quindi verranno scartati e non elaborati nell’immediatezza. Nel transitorio quindi, mentre le policy KO non sono attive, gli eventi di sistema che le avrebbero triggerate vengono persi.

All’attivazione delle policy KO, concomitante con il rilascio 2.1, gli eventi passati non potranno essere recuperati quindi per determinare le elaborazioni delle policy periodiche appena attivate e permettergli di gestire le informazioni presenti sulla consistenza deve essere forzato il ricalcolo delle stesse. L’attivazione stessa determina da parte del DSS l’elaborazione delle policy. Questo permette di intercettare le informazioni di consistenza anagrafica che sono state modificate nel transitorio e che hanno prodotto eventi non gestiti dalle policy nel lasso di tempo in cui sono state spente.

Per fare un esempio pratico, se un ordine periodico è stato eseguito mentre la policy che ne determina la generazione era spenta, la sua esecuzione non ha determinato la creazione del nuovo ordine periodico successivo perché l’evento non è stato intercettato dal DSS (in quanto la policy era spenta). L’attivazione della policy periodica però determina l’elaborazione della stessa su tutta la consistenza anagrafica e permette al DSS di generare gli ordini che reputa “mancanti” per le logiche definite nella policy.

Altro esempio, è stata rilevata e validata una difformità su un asset e la policy che genera l’ordine di risoluzione è spenta. Il trigger della validazione difformità non è stato intercettato dal DSS perché la policy non era attiva. All’attivazione della policy in KO però il DSS gira su tutta la consistenza intercettando le difformità validate per le quali ancora non esiste un ordine risolutore e lo genera secondo le logiche descritte nella policy stessa.
