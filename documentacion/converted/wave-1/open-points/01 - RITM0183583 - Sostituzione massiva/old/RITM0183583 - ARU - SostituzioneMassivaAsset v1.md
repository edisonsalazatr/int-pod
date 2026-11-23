| ANALISI REQUISITI UTENTE<br>Funzionalità di sostituzione massiva asset linea |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Mario Luise | 21/01/2026 | Stesura |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
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

3.2.	Gestione degli asset di secondo livello coinvolti	7

3.3.	Gestione attributi specifici	7

3.4.	Gestione degli asset relazionati	7

3.5.	Gestione del report di fine sostituzione	8

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

Filtri su attributi della classificazione: utilizzati per impostare un filtro su uno o più attributi appartenenti alla classificazione precedentemente impostata. In presenza di filtri impostati, il campo dell’elenco asset da sostituire sarà vuoto e non scrivibile.

Elenco puntuale degli asset da sostituire: permette l’inserimento tramite copia – incolla di numeri di asset. La compilazione è possibile solo quando non sono stati impostati filtri sugli attributi della classificazione.

Tabella degli asset di secondo livello coinvolti: le sostituzioni saranno eseguite solo per quegli asset di quarto livello sottostanti agli asset di secondo presenti in questa tabella. Valgono i criteri di filtri standard di Maximo.

Data di installazione: Questa data è usata come data di In funzione dal per tutti gli asset creati. Per gli asset sostituti vale la data di esecuzione. Se il campo è lasciato vuoto, varrà la data di sistema al momento dell’esecuzione.

Tabella degli asset relazionati da creare: la tabella compare vuota, popolabile solo con le classificazioni degli asset che sono relazionabili alla classificazione degli asset sostituti.

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

In questo paragrafo si descrive l’operatività a sistema con la quale l’utente eseguirà la funzione di sostituzione massiva degli asset di quarto livello.

Nell’immagine seguente l’ipotesi di gestione dei dati richiesti in input per eseguire la funzionalità.

Figura 1 – Dati rilevanti per il dialogo di sostituzione massiva

Richiamando la funzionalità di sostituzione massiva dal menù azioni si apre una box di dialogo per la gestione delle informazioni qui riportate.

Per la compilazione del campo [Classificazione degli asset da sostituire] viene mostrato l’elenco delle classificazioni degli asset di quarto livello delle linee.

La compilazione della tabella [Attributi da filtrare] è in concorrenza rispetto alla compilazione del campo multi-riga [Elenco asset da sostituire]. La tabella è vuota all’apertura del dialogo, compilabile per mezzo del pulsante + per aggiungere attributi, selezionabili solo tra quelli della classificazione precedentemente impostata. Se sono inseriti valori di filtro su più attributi, questi verranno messi in and tra loro. I valori inseriti sono applicati come filtro con l’operatore di uguaglianza.

Il campo multi-riga [Elenco asset da sostituire] è pensato per incollare il contenuto di una colonna di Excel con gli ASSETNUM degli asset di quarto livello; eventuali valori riferiti ad asset non appartenenti alla linea e alla classificazione impostata precedentemente saranno ignorati dalla funzione di sostituzione.

La tabella [Asset di secondo livello coinvolti] serve ad indirizzare l’attività di sostituzione, come approfondito nel successivo paragrafo §3.2.

Il campo [Data di installazione] è utilizzato per popolare massivamente la data [In funzione dal] (INSTALLDATE), per tutti gli asset di nuova creazione, ossia i sostituti e gli eventuali relazionati.

La tabella [Asset relazionati da creare] serve a gestire la creazione dei relazionati, in base alle regole esposte nel paragrafo §3.4.

## Gestione degli asset di secondo livello coinvolti

Questa tabella serve ad indicare quali asset di quarto livello verranno sostituiti, ossia solo quelli i cui rispettivi secondi livelli sono presenti in questa tabella. In pratica, dopo aver compilato almeno la classificazione, questa tabella sarà popolata con tutti gli asset di secondo livello della linea su cui si sta lavorando, che al loro quarto livello li contengono. Se viene impostato un filtro su questa tabella, per cui sono mostrati solo alcuni asset di secondo livello, la sostituzione degli asset di quarto livello verrà fatta per i soli asset appartenenti ai secondi livelli selezionati. Un possibile caso d’uso potrebbe essere la sostituzione di una tratta di tesatura, che coinvolge solo un numero limitato delle campate di una linea.

## Gestione attributi specifici

Nell’operazione di sostituzione il contenuto dei seguenti attributi, quando presenti, deve essere riportato dagli asset sostituiti agli asset sostituti:

RIF_POSIZIONE

RIF_FASE

## Gestione degli asset relazionati

Relativamente agli asset sostituti, i rispettivi asset relazionati verranno gestiti per mezzo dalla tabella [Asset relazionati da creare], che può essere popolata dall’utente con le sole classificazioni relative a quegli asset che possono essere relazionati agli asset in sostituzione; a destra di ogni classificazione potrà essere inserita una quantità. Le regole di funzionamento sono le seguenti:

Con la tabella vuota tutti gli asset sostituti (ossia quelli nuovi), verranno creati senza asset relazionati. In pratica non vengono creati gli asset relazionati di una determinata classificazione, se la stessa classificazione non è presente nella tabella (ad esempio gli asset classificati come GIUNTO CONDUTTORE, per i quali non si desidera che siano abbinati ai conduttori sostituti): non inserendo una determinata classificazione nella tabella, non verranno creati gli specifici asset relazionati.

Per ogni classificazione inserita, senza l’indicazione della quantità, verranno creati gli specifici asset relazionati, solo se esistenti sugli asset sostituiti e ne saranno creati tanti quanti ve ne erano per ogni asset sostituito. Questo comportamento serve, ad esempio, quando si sostituisce la fune di guardia di una linea e si vuole che per le stesse campate vi sia lo stesso numero di nuove sfere di segnalazione rispetto alle attuali.

Per ogni classificazione inserita con l’indicazione della quantità, verranno creati per tutti i nuovi asset, tanti asset relazionati in base al numero inserito. Ad esempio, la tratta di tesatura di una fune di guardia, in prossimità di un nuovo sedime aeroportuale, necessita per ogni campata di cinque sfere di segnalazione.

La funzione di sostituzione massiva si occupa anche di portare in DIST tutti gli asset relazionati a quelli che vengono sostituiti.

## Gestione del report di fine sostituzione

Al termine della fase di applicazione delle modifiche (cambi di stato in DIST dei sostituiti e creazione in DATT dei sostituti, più la gestione degli eventuali relazionati) sarà mostrato a video un dialogo simile al seguente, allo scopo di dare visibilità all’utente dell’esito finale dall’attività eseguita.

Figura 2 - Esempio di rapporto di esecuzione