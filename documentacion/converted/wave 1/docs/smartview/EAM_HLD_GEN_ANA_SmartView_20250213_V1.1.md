# EAM_HLD_GEN_ANA_SmartView_20250213_V1.1

|  | Smart View – High-Level Design |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Relazione: | Smart View – high-level design |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 1.0 | Primo rilascio | 13/02/2025 |
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
| Redatto da: | RTI | 13/02/2025 |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

# Introduzione

Il presente documento descrive il disegno ad alto livello (high-level design) del componente custom per la definizione di viste (smart view) delle anagrafiche di linee e stazioni. L’applicazione permette la definizione di molteplici viste configurate per visualizzare in anteprima ed estrarre successivamente i dati relativi a linee o stazioni mediante una vista tabellare nella quale possono essere presenti attributi degli asset (attributi di testata) e specifiche tecniche.

L’utente può:

configurare logiche di selezione sui campi di testata più significativi e sulle classificazioni di interesse

definire la struttura tabellare dei dati estratti

definire l’ordinamento desiderato

visualizzare l’anteprima paginata e navigarla

scaricare localmente i dati in formato MS Excel.

# Struttura della applicazione

L’applicazione sarà disponibile dal menù del MAS Manage all’interno del modulo Asset.

Figura . Menu MAS Manage per l'apertura della applicazione Smart View

L’applicazione ha la tipica struttura delle applicazioni MAS Manage, presentando alla sua apertura una lista di viste disponibili e azioni di menu per crearne nuove.

Figura . Lista viste disponibili

Le viste sono assegnate ad un Proprietario. All’atto della creazione di una vista il sistema imposterà automaticamente il proprietario con l’id dell’utente. Il proprietario di una vista può decidere di cedere la proprietà della vista ad un altro utente mediante l’azione di menu Assegna ad altro utente.

Figura . Finestra per l'assegnazione della vista ad un altro utente

Il proprietario di una vista può modificare la vista ed eventualmente cancellarla. Una vista può essere resa Visibile a tutti: in tal caso gli altri utenti potranno utilizzare la vista per produrre una anteprima e/o scaricare i dati in formato MS Excel ma non potranno modificarla. Eventualmente vi fosse la necessità di variare una vista assegnata ad un altro utente, è possibile duplicare la vista mediante l’azione di menu Duplica ottenendo una seconda vista identica assegnata all’autore della duplicazione.

Le viste sono specifiche per impianti (LINEA o STAZIONE). Pertanto, non è possibile avere una vista promiscua. Ciascuna Vista è identificata mediante un codice numerico (ad esempio 1020) generato automaticamente dal sistema all’atto della creazione. Il proprietario può inserire una Descrizione esplicativa.

Una vista per impianto di tipo LINEA può essere creata selezionando l’azione di menu Nuova vista linea, allo stesso modo una vista per impianto di tipo STAZIONE può essere creata selezionando l’azione di menu Nuova vista stazione. L’azione creerà una vista assegnando il codice numerico e impostando l’Impianto: entrambi i valori non saranno modificabili in quanto assegnati dal sistema.

L’applicazione è strutturata in tab in ognuno dei quali è possibile fornire specifiche configurazioni della vista.

Figura . Dettaglio vista

Vista: in questo tab risiedono gli attributi principali della vista quali numero della vista, descrizione, proprietario, impianto e visibilità. Inoltre, è possibile, ma non obbligatorio, fornire la lista di classificazioni degli asset di interesse.

Visualizzazione: in questo tab si può definire la struttura della vista mediante l’elenco delle colonne (attributi e/o specifiche tecniche)

Anteprima: in questo tab si possono definire i criteri di ricerca, visualizzare una anteprima paginata dei dati e scaricare un foglio MS Excel con la totalità dei dati.

## Tab Vista

Figura . Tab Vista

In questo tab è possibile inserire una descrizione della vista ed eventualmente rendere visibile (o non visibile) la vista ad altri utenti mediante il selettore Visibile a tutti.

Inoltre, è possibile elencare le classificazioni degli asset che si desidera estrarre (tabella Classificazioni). Le classificazioni disponibili dipendono dal tipo di Impianto: pertanto per un impianto di tipo LINEA sarà possibile selezionare le classificazioni relative ad una linea, mentre per un impianto di tipo STAZIONE sarà possibile selezionare le classificazioni relative ad una stazione.

L’aggiunta delle classificazioni può essere fatta mediante il pulsante Aggiungi sedi tecniche (disponibile per le viste di linee e stazioni) e il pulsante Aggiungi equipments (disponibile per le viste di stazioni). Selezionando il pulsante si aprirà una finestra di ricerca che presenterà le classificazioni disponibili non ancora inserite.

Figura . Finestra per la ricerca e aggiunta delle classificazioni

È possibile selezionare una o più classificazioni che verranno aggiunte alla vista premendo il pulsante Aggiungi classificazioni. Alla pressione di questo pulsante le classificazioni aggiunte spariranno dalla lista di classificazioni disponibili (in quanto aggiunte alla vista) e sarà possibile procedere ad una nuova selezione ed aggiunta. Al termine della selezione si potrà tornare alla vista premendo il tasto Chiudi.

Infine, è possibile rimuovere una classificazione mediante il pulsante o rimuovere tutte le classificazioni inserite mediante il pulsante Rimuovi classificazioni. Poiché la rimozione è istantanea (immediatamente registrata a sistema), la rimozione totale richiede una conferma.

Figura . Pop-up per la conferma rimozione classificazioni

### Gruppi di classificazioni

In alternativa alla selezione puntuale descritta nel paragrafo Tab Vista, è possibile aggiungere gruppi di classificazioni predefiniti premendo il tasto Aggiungi gruppi classificazioni. Alla pressione di questo pulsante verrà mostrata una finestra di selezione di uno o più gruppi di classificazioni predefiniti.

Figura . Finestra per l'aggiunta di gruppi di classificazioni

Sarà possibile selezionare uno o più gruppi e aggiungerli mediante il pulsante Aggiungi gruppi. Eventuali classificazioni già presenti nella vista non saranno duplicate. Per chiudere la finestra e tornare alla vista occorre premere il pulsante Chiudi. I gruppi di classificazione sono accessibili da tutti gli utenti abilitati all’applicazione ma sono configurabili solo da opportuni utenti abilitati. Gli utenti abilitati dispongono della azione di menu Gruppi di classificazioni.

Figura . Azione di menu per la gestione dei gruppi di classificazioni

Selezionando questa voce di menu si accederà alla finestra di Gestione gruppi classificazioni.

Figura . Finestra per la gestione dei gruppi di classificazioni

È possibile aggiungere gruppi specifici per impianti di tipo LINEA (tasto Aggiungi gruppo linea) e/o STAZIONE (tasto Aggiungi gruppo stazione). Per ogni gruppo è possibile aggiungere (mediante il pulsante Aggiungi classificazioni) e/o rimuovere (mediante il pulsante ) le classificazioni appartenenti al gruppo.

Figura . Dettaglio classificazioni di un gruppo

Le modifiche ai gruppi devono essere confermate premendo il tasto Salva alla cui pressione verranno registrate a sistema e la finestra verrà chiusa. Se si desidera non salvare le modifiche pendenti si prema il tasto Annulla.

Si tenga presente che le modifiche ai gruppi non incideranno sulle classificazioni già aggiunte alle viste.

## Tab Visualizzazione

Nel tab Visualizzazione è possibile indicare le colonne da visualizzare (attributi di testata e specifiche tecniche). Alla creazione di una vista alcuni attributi di testata verranno aggiunti by default. Gli attributi disponibili possono variare in funzione dell’impianto su cui è stata costruita la vista (LINEA o STAZIONE).

Figura . Tab Visualizzazione

Le colonne possono essere rimosse puntualmente tramite il pulsante o nella loro totalità mediante il pulsante Rimuovi colonne. In questo ultimo caso verrà richiesta conferma.

Figura . Pop-up di conferma cancellazione colonne

È possibile aggiungere gli attributi di testata mediante il pulsante Aggiungi attributi che aprirà una finestra di selezione. La finestra presenterà gli attributi non ancora aggiunti alla vista e potrebbero variare in base all’impianto (LINEA o STAZIONE) della vista. Si tenga presente che gli attributi non corrispondono necessariamente a campi presenti nella base dati del sistema ma potrebbero essere oggetto di calcolo (quali ad esempio il ROOTSAPID e il MAXLEVEL).

Figura . Finestra per l'aggiunta di attributi di testata

È possibile aggiungere specifiche tecniche mediante il pulsante Aggiungi specifiche che aprirà una finestra di selezione. La finestra presenterà solo le specifiche non ancora aggiunte alla vista e relative alle eventuali classificazioni impostate nella vista.

Figura . Finestra per l'aggiunta di specifiche tecniche

L’ordine delle colonne è determinato dal campo Posizione con la convezione che le colonne saranno posizionate da sinistra a destra seguendo l’ordine crescente. L’aggiunta di una colonna avviene in coda tramite la generazione automatica della posizione. Si noti che il sistema automaticamente genera posizioni con passo 10 e al salvataggio ricalcola i valori assicurando il posizionamento impostato e preservando il passo 10.

Quindi ad esempio se l’utente riordina la posizione delle colonne secondo la figura seguente

Figura . Posizionamento colonne vista

al salvataggio l’applicazione ricalcolerà la posizione per tutti i record preservando l’ordinamento impostato e il passo 10 come mostrato in figura seguente

Figura . Ricalcolo automatico posizione colonne

Il Tipo identifica se la colonna è relativa ad un attributo (ATTRIBUTO) o specifica tecnica (SPECIFICA). Il Nome e la Descrizione non sono modificabili. In particolare, il nome verrà esposto come intestazione della colonna. In alternativa è possibile specificare una etichetta personalizzata nel campo Etichetta. Se si desidera riutilizzare la descrizione si può premere il tasto che causerà la copia della descrizione nel campo etichetta (che eventualmente può essere successivamente raffinato).

Infine, Il campo Ordinamento permette di indicare se la colonna è oggetto di ordinamento (relativo alle righe). Anche in questo caso, in modo similare alla posizione, occorre indicare un numero. L’ordinamento verrà applicato partendo dalle colonne aventi il numero più basso. Pertanto, se si vuole ordinare per ROOTSAPID, MAXLEVEL, CLASSIFICATIONID l’impostazione sarà la seguente:

Figura . Criteri di ordinamento

La sequenza di ordinamento non deve necessariamente coincidere con la sequenza di posizione. L’ordinamento sarà di tipo ascendente (pertanto verranno mostrati prima i valori numericamente o lessicograficamente minori).

## Tab Anteprima

Il tab Anteprima consente di specificare i filtri di ricerca, visualizzare l’anteprima dei risultati e scaricare un file in formato MS Excel dei risultati.

Figura . Tab Anteprima

Sono presenti due sezioni: Filtri ricerca e Anteprima.

### Filtri ricerca

Nella sezione Filtri ricerca è possibile specificare un criterio di ricerca per alcuni attributi di testata che possono dipendere dal tipo di impianto. Nella tabella successiva vengono elencati i filtri di ricerca supportati.

| Filtro | Descrizione | Tipo | Applicabile a | Operatori |
| --- | --- | --- | --- | --- |
| Linea | SAP id del primo livello linea | Alfanumerico | LINEA | >, <, >=, <=, <>, =, % |
| Stazione | SAP id del primo livello stazione | Alfanumerico | STAZIONE | >, <, >=, <=, <>, =, % |
| Id SAP | SAP id | Alfanumerico |  | >, <, >=, <=, <>, =, % |
| Stato | Stato asset | Alfanumerico |  | >, <, >=, <=, <>, =, % |
| Livello | Livello asset (1,2,3,4) | Numerico |  | >, <, >=, <=, <>, = |
| Divisione di ubicazione | Divisione di ubicazione | Alfanumerico |  | >, <, >=, <=, <>, =, % |
| Unità impianti | Unità impianti | Alfanumerico |  | >, <, >=, <=, <>, =, % |
| Ultimo cambio stato (da) | Data ultimo cambio stato asset | Data |  | >= |
| Ultimo cambio stato (a) | Data ultimo cambio stato asset | Data |  | <= |
| In funzione dal (da) | Data installazione asset | Data |  | >= |
| In funzione dal (a) | Data installazione asset | Data |  | <= |
| Ultima modifica (da) | Data ultima modifica asset | Data |  | >= |
| Ultima modifica (a) | Data ultima modifica asset | Data |  | <= |

I filtri supportano una finestra di selezione accessibile mediante il tasto che aiuta la compilazione del filtro. A seconda del filtro verrà proposta una schermata corrispondente. A titolo di esempio viene mostrata la schermata di selezione del SAP id per il filtro Stazione.

Figura . Finestra per la selezione dei SAP id di stazioni

La finestra presenterà solo i valori non ottenuti mediante l’applicazione del filtro correntemente impostato. È possibile filtrare le stazioni di interesse mediante i campi di ricerca dopodiché è possibile selezionare le stazioni di interesse mediante il tasto . È possibile, inoltre, selezionare tutti i record mostrati nella pagina mediante il tasto : si tenga presente che questo tasto seleziona solo i record mostrati nella pagina e non tutti i record presenti in altre pagine (comportamento standard del MAS Manage). Ad ogni modo è possibile selezionare contemporaneamente record mostrati in pagine differenti. Una volta selezionati i record, la selezione deve essere confermata mediante uno dei due tasti Imposta selezione e/o Aggiungi selezione. Il tasto Imposta selezione imposta il filtro con i soli record selezionati, pertanto, una precedente valorizzazione del filtro viene rimpiazzata. Il tasto Aggiungi selezione aggiunge al filtro con i record selezionati, pertanto, una precedente valorizzazione del filtro viene preservata. In entrambi casi una preview del filtro viene mostrata nel campo posizionato sopra la tabella dei record selezionabili. Al termine delle aggiunte occorre confermare il nuovo filtro premendo il tasto Ok. Premendo invece il tasto Annulla il filtro resta invariato.

Il filtro Id SAP ha una finestra di selezione dedicata che è possibile aprire mediante il tasto . La schermata proposta è riportata di seguito.

Figura . Schermata per l'inserimento di Id SAP

Nel campo Testo è possibile inserire una lista di SAP Id copiati, ad esempio, da una lista di valori di una colonna di un foglio MS Excel.

Figura . Inserimento SAP id da MS Excel

Premendo il tasto Imposta il contenuto immesso verrà analizzato e prodotto il relativo filtro. Nel caso in cui il filtro prodotto superi la lunghezza ammissibile (2000 caratteri) verrà mostrato un warning e varranno impostati il numero massimo di valori possibili.

Figura . Warning impostazione SAP id

Se invece viene premuto il tasto Chiudi il filtro non verrà aggiornato.

L’ultimo tipo di filtri è quello relativo alle date. Ogni campo di tipo data presente due campi per l’impostazione del filtro che hanno come suffisso nelle label rispettivamente (da) e (a).

Figura . Filtri di tipo data

In questo caso è possibile indicare una data di partenza (da) e/o una data di arrivo (a). La data può essere selezionata mediante il calendario premendo il tasto . La data inserita sarà inclusa nella ricerca. A titolo di esempio, volendo selezionare gli asset la cui la data di ultimo cambio stato sia compresa fra il 1/1/2024 e 31/12/2024 i filtri vanno impostati come segue.

Figura . Esempio valorizzazione filtri tipo data

Ovviamente specificando solo il campo di partenza (da) verranno selezionati tutti gli asset con ultimo cambio stato maggiore uguale alla data fornita. Allo stesso modo specificando solo il campo di arrivo (a) verranno selezionati tutti gli asset con ultimo cambio stato minore uguale alla data fornita. La data di partenza deve essere minore della data di arrivo se entrambe specificate.

### Sintassi filtri

I filtri supportano una sintassi avanzata per permettere l’immissione di uno o più criteri di ricerca generici. La sintassi supportata è del tipo

[operatore] valore, [operatore] valore, ..., [operatore] valore

Gli operatori supportati dipendono dal tipo del filtro: alfanumerico, numerico, data.

I filtri alfanumerici e numerici supportano gli operatori (facilmente intuibili) >, <, >=, <=, <>, =. Pertanto, se si desidera selezionare un valore esatto si può usare l’operatore = seguito dal valore (ad esempio per il livello = 2). Se si desidera selezionare tutti i valori maggiori uguali ad un valore si può usare l’operatore >= seguito dal valore (ad esempio per il livello >= 2).

Se l’operatore non viene indicato, il filtro indica che il valore fornito deve essere presente (e non necessariamente uguale) al valore del record. Pertanto, se per il filtro Stazione viene specificato il filtro 31, saranno restituite tutti gli asset il cui SAP id di primo livello contiene il valore 31.

Nel caso si volessero filtrare tutti i valori che iniziano per un particolare valore si può usare l’operatore jolly %. Pertanto, se per il filtro Stazione viene specificato il filtro 31%, saranno restituiti tutti gli asset il cui SAP id di primo livello inizia per 31. In generale l’operatore può essere usato in più posizioni. Pertanto, se per il filtro Stazione viene specificato il filtro 31%2%, saranno restituiti tutti gli asset il cui SAP id di primo livello inizia per 31 e contiene il valore 2. L’operatore jolly è supportato per i soli filtri alfanumerici.

Infine, è possibile specificare più criteri separandoli tramite virgola (,). Tutti i criteri saranno usati in modalità alternativa. Ad esempio, volendo selezionare tutti gli asset il cui SAP id di primo livello inizia per 31 oppure 32 il filtro da usare sarà 31%, 32%.

### Anteprima

La sezione di Anteprima permette di vedere in anteprima il risultato della vista in base a come configurata (classificazioni selezionate, struttura della tabella, filtri di ricerca).

Figura . Anteprima visualizzazione

Per aggiornare il contenuto della tabella occorre premere il pulsante Ricerca.

Figura . Anteprima visualizzazione popolata

L’anteprima è una tabella che permette di navigare i dati recuperati mediante paginazione utilizzando i tasti di navigazione (pagina precedente) e (pagina successiva). La dimensione della pagina è impostata by default a 20 righe. Sulla sinistra è mostrato il dettaglio della pagina (ad esempio 1 – 20 indica che sono mostrate le righe da 1 a 20) e il numero totale di righe (ad esempio 378).

È possibile infine scaricare in locale il contenuto della tabella nel formato MS Excel premendo il tasto . Quando è richiesto il download del file verrà mostrato un pop-up di conferma:

Figura . Finestra di conferma per il download dei dati

Per confermare la generazione e download del file occorre premere il pulsante Si. A questo punto inizierà la procedura di estrazione e invio del file. L’avanzamento della procedura di estrazione sarà mostrato nella seguente finestra di avanzamento che indicherà sia la percentuale di avanzamento che la stima del tempo restante. Sarà comunque possibile annullare la procedura premendo il tasto Cancella.

Figura . Finestra di avanzamento di creazione del file MS Excel

Al termine della produzione del file inizierà il download del file da parte del browser. L’avanzamento e il salvataggio di questo file dipenderanno dal tipo di browser e dalla sua configurazione. Il nome del file avrà la seguente struttura vista_YYYYMMDDhhmmss.xslx dove vista rappresenta il numero della vista, YYYY l’anno, MM il mese, DD il giorno, hh l’ora, mm i minuti, ss i secondi del momento in cui è stato richiesto il file.

Il file prodotto avrà una riga di intestazione corrispondente ai nomi delle colonne selezionate (in alternativa delle corrispondenti etichette se impostate). Le colonne avranno i filtri già abilitati.

Figura . Esempio vista in formato MS Excel

# Configurazione applicazione

È possibile configurare alcune proprietà di tuning dalla applicazione standard Proprietà del sistema. Le proprietà sono indicate nella tabella seguente.

| Proprietà | Descrizione | Valore base |
| --- | --- | --- |
| xr.reporting.maxcols | Numero massimo di colonne per viste Smart View | 30 |
| xr.reporting.maxrows | Numero massimo di righe che è possibile mostrare ed estrarre con le viste Smart View | 250000 |
|  |  |  |

# Indice figure

Figura 1. Menu MAS Manage per l'apertura della applicazione Smart View 6

Figura 2. Lista viste disponibili 7

Figura 3. Finestra per l'assegnazione della vista ad un altro utente 7

Figura 4. Dettaglio vista 8

Figura 5. Tab Vista 8

Figura 6. Finestra per la ricerca e aggiunta delle classificazioni 9

Figura 7. Pop-up per la conferma rimozione classificazioni 10

Figura 8. Finestra per l'aggiunta di gruppi di classificazioni 10

Figura 9. Azione di menu per la gestione dei gruppi di classificazioni 11

Figura 10. Finestra per la gestione dei gruppi di classificazioni 11

Figura 11. Dettaglio classificazioni di un gruppo 12

Figura 12. Tab Visualizzazione 12

Figura 13. Pop-up di conferma cancellazione colonne 13

Figura 14. Finestra per l'aggiunta di attributi di testata 13

Figura 15. Finestra per l'aggiunta di specifiche tecniche 14

Figura 16. Posizionamento colonne vista 14

Figura 17. Ricalcolo automatico posizione colonne 15

Figura 18. Criteri di ordinamento 15

Figura 19. Tab Anteprima 16

Figura 20. Finestra per la selezione dei SAP id di stazioni 17

Figura 21. Schermata per l'inserimento di Id SAP 18

Figura 22. Inserimento SAP id da MS Excel 19

Figura 23. Warning impostazione SAP id 19

Figura 24. Filtri di tipo data 20

Figura 25. Esempio valorizzazione filtri tipo data 20

Figura 26. Anteprima visualizzazione 21

Figura 27. Anteprima visualizzazione popolata 21

Figura 28. Finestra di conferma per il download dei dati 22

Figura 29. Finestra di avanzamento di creazione del file MS Excel 22

Figura 30. Esempio vista in formato MS Excel 23
