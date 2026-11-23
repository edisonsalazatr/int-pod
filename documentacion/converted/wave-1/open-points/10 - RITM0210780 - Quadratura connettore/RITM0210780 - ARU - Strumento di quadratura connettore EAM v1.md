| ANALISI REQUISITI UTENTE<br>Strumento di quadratura connettore EAM/SAP |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Marco Lerro | 26/05/2026 | Prima emissione |
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

2.1.	Contesto di riferimento	5

2.2.	Descrizione funzionale	5

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

Il periodo temporale di riferimento va indicativamente da Luglio 2026. Eventuali ripianificazioni andranno concordate col PM di progetto.

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

## Contesto di riferimento

Lo strumento di sincronizzazione della anagrafica EAM/SAP (da ora riferito come connettore EAM/SAP o brevemente connettore) è la componente applicativa Maximo responsabile dell’allineamento della anagrafica EAM verso l’anagrafica SAP previa trasformazione necessaria per ricondurre il primo modello dati al secondo. L’importanza del dato e della sua corretta propagazione verso SAP e relativi sistemi a valle assume un aspetto critico. Sebbene il connettore sia dotato di diagnostica che permette di evidenziare fallimenti rilevati durante la sincronizzazione, la complessità del processo richiede uno strumento di quadratura che permetta di:

Determinare con precisione lo stato di allineamento

Identificare con precisione eventuali discrepanze

Identificare tipologia di fallimenti ricorrenti al fine di intraprendere con rapidità azioni correttive ed evolutive nonché bonifiche di recupero

Supportare efficacemente il team di supporto per la identificazione delle problematiche

## Descrizione funzionale

Poiché il connettore EAM/SAP è un elemento applicativo intermedio fra le due anagrafiche, le situazioni di disallineamento potrebbero verificarsi in diversi punti della catena applicativa mostrata nella figura seguente.

In generale le possibili cause di disallineamento sono:

Mancato innesco del connettore da parte della anagrafica EAM

Errore di gestione da parte del connettore

Errore di invio richiesta di sincronizzazione della anagrafica SAP

Incorretta gestione SAP della richiesta di sincronizzazione

Al fine di avere una evidenza di eventuali squadrature e del loro posizionamento, lo strumento di quadratura analizzerà i seguenti tre punti:

Quadratura anagrafica EAM/connettore

Quadratura connettore/SAP

Quadratura anagrafica EAM/SAP

In particolare, le prime due quadrature permetteranno di identificare dove è avvenuta la perdita della sincronizzazione e sarà funzionale al gruppo di supporto per intraprendere azioni correttive e di bonifica. La terza quadratura darà la vista completa e sarà a favore degli utenti della anagrafica per avere il quadro complessivo su eventuali squadrature. In particolare, quest’ultima sarà esposta tramite reportistica PowerBI.

Lo strumento di quadratura attingerà pertanto i dati da tre fonti dati:

Anagrafica EAM: al fine di poter comparare le anagrafiche, l’anagrafica verrà convertita secondo il data model SAP.

Anagrafica Connettore

Anagrafica SAP: questa anagrafica sarà ottenuta tramite Sisnet, pertanto potrebbe includere squadrature dovute alla sincronizzazione Sisnet/SAP.

Le tre anagrafiche citate saranno estratte periodicamente secondo una pianificazione notturna che non incida sulle prestazioni dei sistemi durante il normale orario lavorativo. Inoltre, vista la mole di dati da elaborare, si prevede una estrazione giornaliera su un sottoinsieme delle anagrafiche con l’obiettivo di analizzare mediamente circa 200 fra linee e stazioni al giorno, garantendo così la quadratura delle circa 6000 fra linee e stazioni nell’arco temporale di un mese. Le eventuali squadrature saranno disponibili giorno dopo giorno e non alla fine dell’intero ciclo. Lo strumento sarà programmato a ripetere la quadratura mese dopo mese, fornendo l’evidenza del ciclo di quadratura (identificato da un progressivo numerico) e dalla data di esecuzione. Il carico dello strumento sarà configurabile e quindi riadattabile in base ai tempi di risposta degli ambienti.

Le squadrature saranno identificate a livello di sede tecnica SAP. Le cause possibili della squadratura della sede tecnica sono le seguenti e potrebbero sovrapporsi:

Dati sede tecnica differenti

Sede tecnica presente su un solo sistema

Caratteristiche tecniche differenti

Collegamenti anagrafica incoerenti (per la sola quadratura EAM/Connettore)

Eventuali informazioni di diagnostica del connettore saranno integrate nelle quadrature. Occorre infine contemplare la possibilità di squadratura dovuta a sincronizzazioni pendenti e/o linee/stazioni marcate in black-list.