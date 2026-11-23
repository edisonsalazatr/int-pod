| ANALISI REQUISITI UTENTE<br>Funzionalità di sostituzione massiva asset linea |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Mario Luise | 21/01/2026 | Stesura |
| 2 | Raffaele Vaiano | 17/02/2026 | Revisione |
| 3 | Marco Lerro | 19/02/2026 | Seconda stesura |
| 4 | Marco Lerro | 23/02/2026 | Terza stesura |
|  |  |  |  |

Indice

1.	Generalità	3

1.1.	Contesto di riferimento	3

1.1.1.	Committente	3

1.1.2.	Vincoli temporali e di pianificazione	3

1.1.3.	Trattamento dei dati personali	3

1.1.4.	Classificazione del sistema/applicazione	4

2.	Requisiti Funzionali	5

2.1.	Lista Requisiti Funzionali	5

3.	Descrizione della soluzione	6

3.1.	Operatività sul sistema	6

3.2.	Gestione del report di fine sostituzione	9

# Generalità

## Contesto di riferimento

Terna è il principale proprietario della Rete elettrica di Trasmissione Nazionale (RTN), una delle più moderne e tecnologiche in Europa ed è uno tra gli operatori europei più importanti di reti per la trasmissione dell’energia in alta e altissima tensione (Transmission System Operator - TSO) per chilometri di linee gestite.

In qualità di Transmission System Operator (TSO), Terna ha, tra gli altri compiti, il mandato istituzionale e la responsabilità sulle seguenti quattro aree:

Assicurare la sicurezza operativa del sistema elettrico nazionale;

Mantenere costantemente l’equilibrio tra domanda ed offerta di energia;

Assicurare e mantenere l’adeguatezza del sistema di trasmissione nel medio e lungo termine;

Migliorare l’efficienza del mercato elettrico.

In questo contesto, l’implementazione di un sistema EAM (Enterprise Asset Management) come IBM Maximo è parte di un percorso di digitalizzazione e innovazione dei processi. Il sistema mira – fra le altre cose - a integrare la pianificazione e la consuntivazione delle attività tecnico-economiche, garantendo una gestione avanzata dei processi di manutenzione e investimento. La presente fornitura si inserisce in questo percorso, con un focus specifico sui collaudi funzionali e contraddittori delle soluzioni sviluppate.

### Committente

SDS-ITD-Soluzioni per RTN IRP

### Vincoli temporali e di pianificazione

Il periodo temporale di riferimento va indicativamente da Marzo 2026 e Giugno 2026. Eventuali ripianificazioni andranno concordate col PM di progetto.

### Trattamento dei dati personali

| Trattamento dati personali | Trattamento dati personali | Trattamento dati personali |
| --- | --- | --- |
| Richiesta | Check | Note |
| Il sistema tratta dati personali? | __ Si |  |
| Il sistema tratta dati personali? | __ No |  |
| Il sistema tratta dati particolari? | __ Si |  |
| Il sistema tratta dati particolari? | __ No |  |
| Il sistema tratta dati giudiziari? | __ Si |  |
| Il sistema tratta dati giudiziari? | __ No |  |

### Classificazione del sistema/applicazione

| Trattamento dati personali | Trattamento dati personali | Trattamento dati personali | Trattamento dati personali |
| --- | --- | --- | --- |
| Richiesta | Impatto Alto | Impatto Medio | Impatto Basso |
| Com’è stata classificata l’applicazione/il sistema | __ | __ | __ |

# Requisiti Funzionali

## Lista Requisiti Funzionali

Nell’ambito del progetto di realizzazione della piattaforma di Asset Management basata su IBM Maximo Application Suite (MAS) per Terna S.p.A., l’utente richiede la creazione di una nuova funzionalità, Sostituzione massiva asset, per la sostituzione massiva degli asset di quarto livello di una specifica linea, con i seguenti obiettivi:

realizzazione di una maschera operativa che permetta di indicare facilmente gli asset oggetto della sostituzione, impostare i parametri operativi che guideranno il processo di sostituzione, tracciamento dell’esito della sostituzione mediante report di esecuzione

vista la natura massiva della funzionalità si richiedono prestazioni adeguate

La funzionalità sarà attivabile da una linea (linea per la quale devono essere sostituiti gli asset) e basata su una maschera di input che gestirà le seguenti informazioni:

Classificazione: utilizzata per indicare la classificazione dell’asset oggetto della sostituzione.

Selezione degli asset di quarto livello: sarà possibile selezionare gi asset di quarto livello in tre modi. La prima modalità prevede la selezione di tutti gli asset della linea corrispondenti alla classificazione selezionata. La seconda permetterà di filtrare gli asset di quarto livello in base a un criterio di ricerca basato sugli attributi relativi alla classificazione dell’asset di quarto livello. La terza modalità permetterà l’immissione degli identificativi degli asset di quarto livello (ASSETNUM).

Tabella degli asset di secondo livello coinvolti: verranno mostrati gli asset di secondo livello coinvolti nella sostituzione

Data di installazione: Questa data è usata come data di In funzione dal per tutti gli asset creati. Il campo sarà obbligatorio e inizialmente non valorizzato.

Tabella degli asset relazionati da creare: la tabella sarà popolata automaticamente e permetterà la selezione degli asset relazionati da creare per gli asset di quarto livello oggetto della sostituzione.

Valori attributi da inserire: sarà possibile indicare i valori per gli attributi per gli asset che verranno creati

Si riassume per sinteticità gli oggetti dello sviluppo:

Dialog di selezione degli asset oggetto di sostituzione.

Dialog per visualizzazione per report di esecuzione.

Gestione transazionalità della dismissione e creazione al fine di non modificare in modo inconsistente l’anagrafica nel caso di situazioni di errore, preservando la forma originale della linea oggetto dalla richiesta di modifica.

Prestazioni adeguate rispetto ai volumi di modifiche generati dalla riassociazione.

Attività di test (unit-test, SIT e UAT) comprensivi di scenari end-to-end Maximo/SAP.

Di seguito i deliverable previsti:

Specifica funzionale.

Specifica tecnica.

Aggiornamento della documentazione di disegno (HLD/LLD).

Per gli UAT sarà fornito il test-book per la verifica di tutti gli scenari previsti.

Predisposizione della nota di rilascio corredata dei pacchetti di rilascio basati su migration manager.

Piano attività in formato Gantt.

# Descrizione della soluzione

## Operatività sul sistema

In questo paragrafo si descrive l’operatività a sistema con la quale l’utente eseguirà la funzione di sostituzione massiva degli asset di quarto livello. La funzionalità di sostituzione massiva sarà disponibile nel menu azioni e sarà relativa alla linea correntemente visualizzata. La selezione dell’azione porterà alla apertura di una dialog per l’immissione dei parametri inerenti alla sostituzione.

Nell’immagine seguente l’ipotesi di gestione dei dati richiesti in input per eseguire la funzionalità.

Figura 1 – Dialogo di sostituzione massiva

Il campo Classificazione asset da sostituire viene utilizzato per indicare la classificazione degli asset di quarto livello oggetto della sostituzione (unitamente ad eventuali asset relazionati che saranno mostrati a seguito della richiesta di preview nella tabella Asset relazionati). Indicata la classificazione, sarà poi necessario indicare quali asset di quarto livello della linea dovranno essere oggetto della sostituzione. La selezione dei quarti livelli può essere fatta secondo le seguenti modalità alternative fra loro come indicato nel box informativo:

Includi tutti gli asset della linea
Tutti gli asset di quarto livello della linea corrispondenti alla classificazione selezionata saranno inclusi nella sostituzione.

Criteri di selezione asset
Apparirà una tabella per l’inserimento degli attributi relativi alla classificazione precedentemente impostata.

Figura . Impostazione criteri di selezione basata sugli attributi

Per ogni attributo sarà necessario inserire un valore. L’asset di quarto livello sarà selezionato se il corrispondente attributo ha il valore immesso nel filtro di ricerca. Nel caso venissero specificati più attributi nella ricerca, l’asset di quarto livello sarà selezionato se tutti gli attributi dell’asset corrispondenti avranno i valori corrispondenti ai criteri di selezione.

Elenco asset da sostituire. 
Apparirà un campo testuale multi-riga nel quale sarà possibile inserire un elenco di asset identificati mediante il loro numero (ASSETNUM).

Figura . Impostazione criteri di selezione basata su elenco asset

Ogni numero dovrà essere disposto su una riga. Eventuali righe vuote verranno ignorate. Questo tipo di immissione facilita l’inserimento di elenchi precedentemente estratti in un foglio di calcolo. Nel caso il numero degli asset inseriti superi il limite supportato dal sistema verrà presentato un messaggio esplicativo.

Al termine della impostazione di uno dei tre criteri di ricerca l’utente dovrà premere il pulsante Preview al fine di visualizzare gli asset di secondo livello (tabella Asset di 2° livello) corrispondenti agli asset di quarto livello selezionati mediante uno dei due metodi possibili. Questa tabella verrà popolata automaticamente dalla funzionalità e non ha l’obiettivo di raffinare la ricerca.

Verrà inoltre popolata la lista di eventuali classificazioni di asset di quarto livello relazionati alla classificazione selezionata. La tabella (Asset relazionati) mostrerà solo le classificazioni relazionate effettivamente presenti sulla linea. Queste classificazioni saranno selezionabili al fine di indicare se oltre la distruzione dell’asset relazionato occorre anche effettuare la creazione. Automaticamente le classificazioni saranno selezionate alla preview eccetto le eventuali seguenti classificazioni:

BARRETTA PREFORMATA CONDUTTORE

BARRETTA PREFORMATA FUNE DI GUARDIA

GIUNTO CAVO

GIUNTO CONDUTTORE

GIUNTO DI RIPARAZIONE

GIUNTO FUNE DI GUARDIA

Il campo Data di installazione rappresenta la data che verrà impostata per i nuovi asset (INSTALLDATE). La compilazione del campo sarà obbligatoria e la sostituzione massiva potrà essere avviata solo previa compilazione.

Il pulsante Seleziona valori da inserire permetterà di indicare valori di attributi da impostare all’atto della sostituzione. Cliccando il pulsante si aprirà un nuovo dialog che mostrerà le classificazioni oggetto della sostituzione (includendo la classificazione specificata nonché le classificazioni degli asset relazionati selezionati) e per ognuna di queste verranno mostrati gli attributi e verrà data la possibilità di impostare il valore eventualmente selezionato tramite una ricerca basata sul dominio.

Figura . Impostazione valori attributi da assegnare ai nuovi asset

Al termine della funzionalità, gli asset oggetto della sostituzione saranno apposti automaticamente in stato DIST, compresi gli asset a loro relazionati. Gli asset distrutti saranno rimpiazzati da nuovi asset che avranno lo stato DATT. Gli asset relazionati per cui non è stata selezionata la classificazione non verranno creati ma solo distrutti. Gli asset avranno come data installazione la data obbligatoria inserita dall’utente. Gli asset creati avranno le caratteristiche impostate secondo i valori immessi tramite la funzione Seleziona valori da inserire. Per gli asset relazionati saranno impostate le caratteristiche tecniche secondo le logiche della RITM0167680.

## Gestione del report di fine sostituzione

Al termine della fase di applicazione delle modifiche (cambi di stato in DIST dei sostituiti e creazione in DATT dei sostituti, più la gestione degli eventuali relazionati) sarà mostrato a video un dialogo simile al seguente, allo scopo di dare visibilità all’utente dell’esito finale dall’attività eseguita.

Figura  - Esempio di rapporto di esecuzione