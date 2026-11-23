| ANALISI REQUISITI UTENTE<br>Gestione Campi calcolati connettore |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Marco Lerro | 10/03/2026 | Prima emissione |
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

2.1.	Requisito Utente Req_U_001	5

2.2.	Requisito Utente Req_U_002	6

3.	Deliverable	7

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

Il periodo temporale di riferimento va indicativamente da Aprile 2026 e Luglio 2026. Eventuali ripianificazioni andranno concordate col PM di progetto.

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

### Requisito Utente Req_U_001

Il requisito prevede di impiantare la gestione delle caratteristiche relative alla topologia della linea direttamente in anagrafica. Ad oggi la struttura topologica della anagrafica è basata sul numero di sequenza (X3SEQUENCE) e le caratteristiche sono calcolate dal connettore EAM/SAP quando è necessaria la sincronizzazione delle sedi tecniche e non sono esposte in anagrafica.  Nel dettaglio si richiede di gestire le seguenti caratteristiche:

|  | ASSET | CARATTERISTICA |
| --- | --- | --- |
| 1 | CAMPATA AEREA | LIN_SOSTEGNO_INIZIO |
| 2 | CAMPATA AEREA | LIN_SOSTEGNO_FINE |
| 3 | CAMPATA AEREA | LIN_CAMPATA_ASSOCIATA |
| 4 | TRATTA CAVO | LIN_ESTREMO_INIZIO |
| 5 | TRATTA CAVO | LIN_ESTREMO_FINE |

La gestione del dato direttamente in anagrafica implica l’aggiunta delle logiche di calcolo a seguito di qualsiasi modifica strutturale della linea. Il calcolo avverrà contestualmente alla modifica strutturale permettendo la visualizzazione immediata delle caratteristiche ricalcolate. Le caratteristiche saranno disponibili tramite Smart View. Il connettore dovrà sincronizzare i dati calcolati in anagrafica. Le funzionalità di modifica strutturale linea sono elencate di seguito:

Genera asset singolo di secondo livello

Inserisci tratta (aerea/cavo)

Associazione in terna doppia

Ammazzettamento

Crea derivazioni

Ricodifica SAPID

Avvia ricalcolo sequenza

Crea Split Linea

Disammazzettamento

Riepilogo dati

Riassocia tratta (aerea/cavo) (Riassociazione asset di secondo livello)

Bonifica campata doppia

Bonifica sostegni singoli

Cambio stato secondo livello

Le caratteristiche saranno visualizzabili in sola lettura e valorizzate laddove abbia senso. In particolare, la caratteristica LIN_CAMPATA_ASSOCIATA sarà valorizzata solo in caso di campata in doppia terna. La caratteristica LIN_SOSTEGNO_INIZIO sarà comunque valorizzabile tramite la funzione Crea legame altra linea.

In seguito allo spostamento delle caratteristiche in anagrafica, sarà necessario realizzare una procedura di popolamento iniziale delle caratteristiche che non dovrà innescare la sincronizzazione verso SAP tramite il connettore. I valori calcolati dovranno essere verificati rispetto all’attuale valorizzazione in SAP per garantire la bontà di produzione del dato. Nel caso di bonifiche i dati saranno soggetti a sincronizzazione.

### Requisito Utente Req_U_002

Nella scheda tronchi dovranno essere aggiunte le seguenti due colonne alla tabella Tronchi:

Sostegno/Estremo Inizio

Sostegno/Estremo Fine

In queste due colonne, per ciascun tronco di ispezione, verranno mostrati i dati del primo e dell’ultimo sostegno /estremo.

Verranno adottate le seguenti regole di calcolo:

sostegno/estremo di inizio: sostegno/estremo di inizio della campata avente sequenza minore tra quelle associate al tronco di ispezione

sostegno/estremo di fine: sostegno/estremo di fine della campata avente sequenza maggiore tra quelle associate al tronco

i campi saranno in sola lettura

In seguito alla introduzione di questi attributi, sarà necessario realizzare una procedura di popolamento iniziale che non dovrà innescare la sincronizzazione verso SAP tramite il connettore. I valori calcolati dovranno essere verificati rispetto all’attuale valorizzazione in SAP per garantire la bontà di produzione del dato. Nel caso di bonifiche i dati saranno soggetti a sincronizzazione.

# Deliverable

Di seguito i deliverable previsti:

Specifica funzionale (HLD)

Specifica tecnica (LLD)

Test-book per la verifica di tutti gli scenari previsti

Nota di rilascio corredata dei pacchetti di rilascio basati su migration manager.

Piano attività in formato Gantt