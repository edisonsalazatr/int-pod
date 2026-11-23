|  | Strumento di quadratura connettore – High-Level Design |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Relazione: | Strumento di quadratura connettore – High-level design |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 1.0 | Primo rilascio | 18/06/2026 |
| 2.0 | Aggiunta funzionalità di recupero quadratura | 26/06/2026 |
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
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | RTI | 18/06/2026 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

# Introduzione

Il presente documento descrive il disegno ad alto livello (high-level design) dello strumento di quadratura del connettore EAM/SAP. L’obiettivo di questo strumento è dare la visione del potenziale disallineamento fra l’anagrafica asset EAM e l’anagrafica SAP e gli step intermedi della sincronizzazione eseguiti dal connettore. I disallineamenti potrebbero essere dovuti alle seguenti possibili cause:

Anagrafica EAM inconsistente (step 1): tipicamente dovuto a dati immessi non consistenti. Il connettore potrebbe intercettare queste situazioni e non essere in grado di calcolare correttamente i dati SAP.

Connettore non sollecitato correttamente (step 1): le modifiche fatta alla anagrafica EAM sono intercettate dal connettore in due modi in base allo scenario di modifica esercitato: implicito (il connettore intercetta un cambiamento ad insaputa della anagrafica) oppure espliciti (l’anagrafica informa il connettore delle modifiche). Se in entrambi i casi il sollecito non avviene o avviene nel modo sbagliato il connettore potrebbe perdere eventi (nel primo caso) oppure avere una sequenza incorretta di aggiornamenti portando la linea/stazione in black-list (secondo caso).

Errore nel calcolo delle caratteristiche SAP (step 3): questo scenario potrebbe accadere per inconsistenza della anagrafica o regole di calcolo del connettore non corrette.

Fallimento nella sincronizzazione SAP (step 4): la sincronizzazione potrebbe fallire per problemi infrastrutturali (mancanza di connettività ad esempio) oppure errore segnalato da SAP.

Figura 1. Flusso connettore EAM

Al fine di avere un quadro chiaro del disallineamento della anagrafica EAM rispetto alla anagrafica SAP, lo strumento di quadratura, oggetto di questo HLD, provvederà alla estrazione di anagrafica EAM, connettore e SAP (via Sisnet) e alla successiva comparazione (a coppia) delle estrazioni con l’intento di evidenziare eventuali disallineamenti nel processo di sincronizzazione.

# Architettura

Lo strumento di quadratura sarà avviato in background secondo una opportuna programmazione su base giornaliera con l’obiettivo di analizzare una parte di linee e stazioni. L’obiettivo è di raggiungere l’analisi di circa 200 primi livelli al giorno garantendo l’analisi dell’intera anagrafica in circa un mese. Finita l’analisi, la quadratura ripartirà automaticamente eseguendo un nuovo ciclo di analisi aggiornando gli esiti delle comparazioni in virtù delle nuove estrazioni.

Figura 2. Architettura strumento di quadratura

Le sorgenti dati sono:

Anagrafica EAM

Anagrafica connettore

Anagrafica SAP (veicolata tramite integrazione Sisnet)

Poiché l’anagrafica EAM è basata su un data model diverso da quello delle anagrafiche connettore e Sisnet, l’estrazione richiederà una trasformazione della stessa secondo le regole del connettore. Inoltre, l’anagrafica estratta da Sisnet, richiederà una prima estrazione della intera anagrafica e successivo allineamento delle variazioni sempre estratte da Sisnet.

Le estrazioni prevederanno l’elaborazione della intera linea/stazione oggetto della comparazione. Terminate le estrazioni avverranno le seguenti comparazioni:

Anagrafica EAM / anagrafica connettore: evidenzierà disallineamenti fra EAM e connettore e quindi porterà alla luce eventuali eventi di innesco verso il connettore

Anagrafica connettore / anagrafica Sisnet: evidenzierà disallineamenti fra connettore e Sisnet, quindi, porterà alla luce eventuali sincronizzazioni fallite o in attesa di essere eseguite

Anagrafica EAM / anagrafica Sisnet: evidenzierà disallineamenti fra EAM e Sisnet

Poiché il connettore non esegue sincronizzazioni in real-time ma secondo una pianificazione appropriata degli step, alcuni scostamenti potrebbero essere temporanei e non indicativi di un errore.

I risultati delle comparazioni saranno memorizzati in tabelle dedicate. In particolare, le comparazioni EAM / SAP saranno fruibili tramite reportistica Power BI, mentre le altre (più tecniche ed a favore della squadra di AMS) tramite accesso al database.

Infine, i risultati della comparazione potranno innescare azioni di recupero automatiche verso il connettore (ad esempio se la comparazione EAM / connettore indica una squadratura sui bindings, sarà avviata una azione di rebind per ricuperare il disallineamento).

Figura 3. Utilizzo dati di comparazione

## Pianificazione

L’avvio dello strumento di quadratura avviene tramite il CRON-task XQDIFFRUNNER. IBM MAS Manage avvia il task di quadratura secondo la schedulazione definita a sistema.

Figura 4. CRON-task Strumento di quadratura

Il CRON-task si avvale di una piano di esecuzione definito nel MBO XQDIFFPLAN che verrà configurato mediante caricamento via MXLoader secondo le esigenze. Ogni riga di questa tabella oggetto contiene le istruzioni di quadratura (piani di quadratura) ed è possibile potenzialmente configurare più piani di quadratura che verranno avviati ad ogni esecuzione del CRON-task.

Figura 5. Tabella piani di quadratura

Per ogni piano di quadratura (il cui identificativo è nella colonna chiave XQDIFFPLANID) è possibile specificare le seguenti configurazioni:

| ATTRIBUTO | DESCRIZIONE |
| --- | --- |
| SEQUENCE | Ordine di esecuzione del piano. |
| MAXRUN | Numero massimo di primi livelli da eseguire in una esecuzione del CRON-task per il piano. |
| WHERECLAUSE | Clausola di filtro su tabella ASSET per selezionare i primi livelli associati al piano. |
| ORDERBY | Clausola di ordinamento su tabella ASSET per ordinare i primi livelli associati al piano. |

Il campo CURRENTRUNID identifica il ciclo di esecuzione del piano. Quando il piano avrà completato l’analisi di tutti i primi livelli associati (potenzialmente su più esecuzioni del CRON-task), il ciclo di esecuzione del piano sarà terminato e partirà un nuovo ciclo e di conseguenza il CURRENTRUNID verrà incrementato di una unità.

Ogni esecuzione del CRON-task verrà registrata nel MBO XQDIFFRUNTRANS e sarà riconducibile al piano mediante il campo PLANID. Questa tabella contiene lo storico delle esecuzioni dei piani.

Figura 6. Tabella transazioni CRON-task di quadratura

Di seguito la descrizione degli attributi.

| ATTRIBUTO | DESCRIZIONE |
| --- | --- |
| PLANID | ID del piano. |
| RUNID | L’ID del ciclo di esecuzione del piano. Il valore più alto corrisponde al CURRENTRUNID impostato per il XQDIFFPLAN. |
| RUN | Identifica l’esecuzione del CRON-task all’interno di un ciclo di un piano. Pertanto, se un piano prevede la quadratura di 1000 linee e un limite di esecuzione di 200 linee, si avranno in totale 5 esecuzioni del CRON-task per il completamento del ciclo di quadratura. Il primo avrà RUN pari a 1, il secondo 2 e l’ultimo 5. A questo punto essendo il ciclo terminato, il RUNID verrà incrementato (e di conseguenza il CURRENTRUNID) e il RUN ripartirà da 1. |
| STARTDATE | La data di inizio della esecuzione del CRON-task. |
| ENDDATE | La data di fine della esecuzione del CRON-task. |
| RUNTIME | Il tempo di esecuzione del CRON-task. |
| STATUS | Lo stato della esecuzione, può essere RUNNING se il CRON-task è in esecuzione, COMPLETED se è stata portata a termine. |
| PROCESSED | Il numero di primi livelli processati (inclusi eventuali fallimenti) nell’esecuzione del CRON-task. |
| FAILED | Il numero di primi livelli processati con fallimenti nell’esecuzione del CRON-task. |

All’interno di una esecuzione del CRON-task il MBO XQDIFFRUNLOG registra l’esito della quadratura di ogni linea/stazione elaborata.

Figura 7. Tabella di registrazione quadratura linee/stazioni

Di seguito la descrizione degli attributi.

| ATTRIBUTO | DESCRIZIONE |
| --- | --- |
| PLANID | ID del piano. |
| RUNID | L’ID del ciclo di esecuzione del piano. Il valore più alto corrisponde al CURRENTRUNID impostato per il XQDIFFPLAN. |
| RUN | Identifica l’esecuzione del CRON-task all’interno di un ciclo di un piano. |
| ASSETNUM | Numero asset EAM del primo livello. |
| SAPID | SAP ID del primo livello. |
| STARTDATE | La data di inizio della esecuzione del CRON-task. |
| ENDDATE | La data di fine della esecuzione del CRON-task. |
| RUNTIME | Il tempo di esecuzione del CRON-task. |
| FAILED | 1 se l’esecuzione della quadratura non è andata a buon fine |
| ERROR | Causa dell’errore nel caso l’esecuzione della quadratura non fosse andata a buon fine. |

## Allineamento Sisnet

L’allineamento Sisnet avviene tramite interrogazione via JDBC del servizio di esportazione dati esposto da Sisnet. Il processo di allineamento prevede una prima estrazione dell’intera anagrafica Sisnet di interesse e successivi allineamenti tramite lo scarico delle modifiche. L’allineamento delle modifiche viene avviato ad ogni esecuzione del CRON-task e le modifiche verranno integrate nella anagrafica Sisnet corrente. In particolare, eventuali sedi tecniche, equipment, attributi e caratteristiche non esistenti verranno aggiunte, se presenti verranno aggiornate.

L’anagrafica Sisnet viene memorizzata nelle seguenti tabelle.

| TABELLA | DESCRIZIONE |
| --- | --- |
| XQSISNETOBJECT | Tabella delle sedi tecniche ed equipment. |
| XQSISNETDATA | Tabella degli attributi e caratteristiche tecniche delle sedi tecniche ed equipment. |

## Estrazione

Gli estrattori sono avviati dal CRON-task per ogni piano secondo la sequenza:

Estrattore EAM

Estrattore connettore

Estrattore Sisnet

L’estrattore EAM analizza l’intera gerarchia di un primo livello EAM e la trasforma secondo le regole del connettore nel corrispondente data-model SAP. L’estrattore produrrà le sedi tecniche, equipment, attributi e caratteristiche, nonché i binding di collegamento verso l’anagrafica. L’estrazione viene memorizzata nelle seguenti tabelle.

| TABELLA | DESCRIZIONE |
| --- | --- |
| XQEAMSAPASSET | Tabella delle sedi tecniche ed equipment. |
| XQEAMSAPASSETDATA | Tabella degli attributi e caratteristiche tecniche delle sedi tecniche ed equipment. |
| XQEAMSAPTOMAXOBJECT | Tabella dei binding anagrafica SAP verso anagrafica EAM. |

L’estrattore connettore attinge l’anagrafica presente nei dati del connettore già rappresentata secondo il formato del data model SAP. L’estrazione viene memorizzata nelle seguenti tabelle.

| TABELLA | DESCRIZIONE |
| --- | --- |
| XQCONNSAPASSET | Tabella delle sedi tecniche ed equipment. |
| XQCONNSAPASSETDATA | Tabella degli attributi e caratteristiche tecniche delle sedi tecniche ed equipment. |
| XQCONNSAPTOMAXOBJECT | Tabella dei binding anagrafica SAP verso anagrafica EAM. |

L’estrattore Sisnet attinge dall’anagrafica Sisnet scaricata al passo precedente ed è già rappresentata secondo il formato del data model SAP. L’estrazione viene memorizzata nelle seguenti tabelle.

| TABELLA | DESCRIZIONE |
| --- | --- |
| XQSISNETOBJECT | Tabella delle sedi tecniche ed equipment. |
| XQSISNETDATA | Tabella degli attributi e caratteristiche tecniche delle sedi tecniche ed equipment. |

## Comparazione

I comparatori sono avviati dal CRON-task per ogni piano al termine dell'estrazione di un oggetto di primo livello secondo la sequenza:

Comparatore EAM/connettore

Comparatore EAM/Sisnet

Comparatore connettore/Sisnet

I comparatori condividono le stesse tabelle per memorizzare i risultati di comparazione.

| TABELLA | DESCRIZIONE |
| --- | --- |
| XQDIFF | Tabella riassuntiva della comparazione di una linea/stazione. La tabella riporta i dati che identificano il primo livello (ASSETNUM e SAPID), la data della comparazione e il numero di squadrature rilevate per ogni comparatore. |
| XQOBJECTDIFF | Tabella riassuntiva di tutte le sedi tecniche ed equipment comparati. Ogni record ha una colonna (KEY) che identifica la sede tecnica tramite il suo SAP id (STRNO) o l’equipment tramite il suo EQNR. Una stessa sede tecnica ed equipment appare tre volte in base alla comparazione. Il tipo comparazione è indicato nella colonna TYPE dove:<br>Comparatore EAM/connettore<br>Comparatore EAM/Sisnet<br>Comparatore connettore/Sisnet<br>La colonna RESULT indica l’esito della comparazione della sede tecniche o equipment secondo la seguente convenzione:<br><br>% La sede tecnica o equipment esiste in entrambe le anagrafiche comparate ma differiscono.<br>+ La sede tecnica o equipment esiste solo sulla prima delle due anagrafiche comparate.<br>- La sede tecnica o equipment esiste solo sulla seconda delle due anagrafiche comparate. |
| XQVALUEDIFF | Tabella riassuntiva della comparazione degli attributi e caratteristiche per tutte le sedi tecniche ed equipment. Il tipo comparazione è indicato nella colonna TYPE dove:<br>Comparatore EAM/connettore<br>Comparatore EAM/Sisnet<br>Comparatore connettore/Sisnet<br>I valori delle due anagrafiche sono riportati nelle colonne VALUESOURCE e VALUETARGET. |
| XQBINDINGDIFF | Tabella riassuntiva della comparazione dei binding per tutte le sedi tecniche ed equipment verso gli asset EAM. Il popolamento avviene solo per il comparatore EAM/connettore.<br>La colonna RESULT indica l’esito della comparazione della sede tecniche o equipment secondo la seguente convenzione:<br><br>% La sede tecnica o equipment esiste in entrambe le anagrafiche comparate ma differiscono.<br>+ La sede tecnica o equipment esiste solo sulla prima delle due anagrafiche comparate.<br>- La sede tecnica o equipment esiste solo sulla seconda delle due anagrafiche comparate. |

## Recupero quadratura

Lo strumento di quadratura, in base ai risultati della comparazione, avvierà azioni di recupero del connettore al fine di ripristinare in modo automatico, laddove fattibile, dati del connettore e recuperare potenzialmente le squadrature. In questa versione l’azione di recupero realizzata mira al recupero di eventuali bindings (collegamenti fra anagrafica SAP locale al connettore e anagrafica EAM) incorretti. Le squadrature relative ai bindings possono essere rilevate mediante i risultati della comparazione prodotti nella tabella XQBINDINGDIFF. Nel caso vi fossero delle squadrature, lo strumento di quadratura aggiungerà una richiesta di ricreazione dei bindings nella coda del connettore. Il connettore, secondo la sua pianificazione (riportata nella figura seguente), analizzerà le richieste pendenti nella coda.

Figura 8. Pianificazione connettore EAM/SAP in ambiente di produzione

L’attività di recupero bindings può essere abilitata impostando ad 1 la proprietà xq.recovery.rebind.enabled.

## Esclusione attributi e caratteristiche

Nel caso in cui Sisnet non esportasse alcuni attributi e/o caratteristiche prodotte dall’estrattore EAM, la comparazione verrà inibita per questi. Allo stesso modo, nel caso in cui Sisnet esportasse attributi e/o caratteristiche con un valore codificato in modo differente da quanto prodotto dal connettore, per questi la comparazione potrà essere inibita o in alternativa potrà essere mostrata risultando come squadratura.

Al momento la lista di attributi e caratteristiche configurate per la esclusione sono quelle riportate nella tabella seguente. A seguito di SIT/UAT/utilizzo in produzione potrebbero essere identificati ulteriori attributi e/o caratteristiche non comparabili: in tal caso sarà necessario segnalare l’attributo/caratteristica da escludere e lo strumento di quadratura verrà istruito ad ignorarlo mediante opportuna evolutiva.

| Attributo/Caratteristica | Sede tecnica/Equipment | Nome | Classe | Motivazione |
| --- | --- | --- | --- | --- |
| ATTRIBUTO | SEDE TECNICA | COUNTRY |  | Non presente in Sisnet |
| ATTRIBUTO | SEDE TECNICA | STTXU |  | Non presente in Sisnet |
| ATTRIBUTO | SEDE TECNICA | REGION |  | Non presente in Sisnet |
| ATTRIBUTO | SEDE TECNICA | TPLKZ |  | Non presente in Sisnet |
| ATTRIBUTO | SEDE TECNICA | CITY |  | Non presente in Sisnet |
| ATTRIBUTO | SEDE TECNICA | STREET |  | Non presente in Sisnet |
| ATTRIBUTO | SEDE TECNICA | INBDT |  | Non presente in Sisnet |
| ATTRIBUTO | SEDE TECNICA | NAME1 |  | Non presente in Sisnet |
| ATTRIBUTO | SEDE TECNICA | EQFNR |  | Non presente in Sisnet |
| ATTRIBUTO | SEDE TECNICA | INGRP |  | Non presente in Sisnet |
| ATTRIBUTO | SEDE TECNICA | RBNR |  | Non presente in Sisnet |

## Parallelismo

Al fine di seguire un numero elevato di quadrature per ogni esecuzione del CRON-task, è stato realizzato un meccanismo di esecuzione parallela di analisi dei primi livelli (estrazione e comparazione) che potrà esser configurato mediante la proprietà xq.diff.maxthread dove è possibile indicare il numero di quadrature concorrenti.

## Reportistica

I risultati della comparazione EAM/Sisnet saranno consultabili tramite reportistica Power BI. La soluzione definisce le seguenti viste che esportano il dato a favore della reportistica.

| TABELLA | DESCRIZIONE |
| --- | --- |
| XQBIDIFF | Tabella riassuntiva della comparazione di una linea/stazione. La tabella riporta i dati che identificano il primo livello (ASSETNUM e SAPID), la data della comparazione e il numero di squadrature. |
| XQBIDIFFOBJECT | Tabella riassuntiva di tutte le sedi tecniche ed equipment comparati. |
| XQBIDIFFVALUE | Tabella riassuntiva della comparazione degli attributi e caratteristiche per tutte le sedi tecniche ed equipment. |

La reportistica potrà essere consultata partendo dalla pagina iniziale mostrata nella figura seguente.

Figura 9. Pagina iniziale reportistica

La vista sui primi livelli mostrerà le linee e stazioni (solo i primi livelli) elaborate dallo strumento di quadratura fornendo alcune informazioni riassuntive.

Figura 10. Tabella reportistica primi livelli

Nel dettaglio le colonne mostrano:

SAP ID primo livello

Numero asset primo livello

Descrizione del primo livello

Divisione di ubicazione

Data di ultima quadratura (pertanto se la quadratura verrà rieseguita sarà mostrata la data dell’ultima quadratura)

Fallimento della quadratura (dovuta ad errori, ad esempio, di inconsistenza dei dati di anagrafica EAM, problemi di connettività a Sisnet, bug strumento di quadratura)

Numero di squadrature rilevate

Presenza di errori di sincronizzazione del connettore EAM/SAP

Se il connettore non ha ancora sincronizzato elementi della linea/stazione perché in attesa di elaborazione e/o in errore.

Il secondo livello di dettaglio è relativo alle sedi tecniche ed equipment di qualsiasi livello. Sarà possibile applicare filtri, che ad esempio, permetteranno la visualizzazione delle sedi tecniche ed equipment relativi ad una linea/stazione.

Figura 11. Tabella reportistica sedi tecniche

Nel dettaglio le colonne mostrano:

SAP ID primo livello

SAP ID del livello superiore della sede tecnica/equipment.

SAP ID della sede tecnica/equipment. Nel caso di equipment (5 livello stazione) l’ID sarà la concatenazione del SAP ID padre e del EQNR separati al solito dal simbolo – (ad esempio 32102A-77-1220-A-1000092829)

Il tipo di oggetto (S per sede tecnica, E per equipment)

Classe della sede tecnica/equipment.

L’esito della quadratura. I possibili valori sono:

Sede tecnica non presente in SISNET

Equipment non presente in SISNET

Sede tecnica non presente in EAM

Equipment non presente in EAM

Errori connettore EAM/SAP

Warning connettore EAM/SAP

Il terzo livello di dettaglio mostra gli attributi e caratteristiche delle sedi tecniche/equipment non allineati fra EAM e Sisnet.

Figura 12. Tabella reportistica attributi e caratteristiche

Nel dettaglio le colonne mostrano:

SAP ID primo livello

SAP ID della sede tecnica/equipment. Nel caso di equipment (5 livello stazione) l’ID sarà la concatenazione del SAP ID padre e del EQNR separati al solito dal simbolo – (ad esempio 32102A-77-1220-A-1000092829)

Nome dell’attributo/caratteristica tecnica

Valore calcolato in EAM dell’attributo/caratteristica tecnica

Valore presente in Sisnet dell’attributo/caratteristica tecnica

# Indice Figure

Figura 1. Flusso connettore EAM	4

Figura 2. Architettura strumento di quadratura	5

Figura 3. Utilizzo dati di comparazione	6

Figura 4. CRON-task Strumento di quadratura	6

Figura 5. Tabella piani di quadratura	7

Figura 6. Tabella transazioni CRON-task di quadratura	7

Figura 7. Tabella di registrazione quadratura linee/stazioni	8

Figura 8. Pianificazione connettore EAM/SAP in ambiente di produzione	11

Figura 9. Pagina iniziale reportistica	13

Figura 10. Tabella reportistica primi livelli	13

Figura 11. Tabella reportistica sedi tecniche	14

Figura 12. Tabella reportistica attributi e caratteristiche	15