| ANALISI REQUISITI UTENTE<br>Gestione Centro di Lavoro Responsabile |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Mario Luise | 13/02/2026 | Prima emissione |
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

4.	Gestione dei dati (bonifiche e aggiornamenti)	8

5.	file allegati	9

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

Nell’ambito del progetto di realizzazione della piattaforma di Asset Management basata su IBM Maximo Application Suite (MAS) per Terna S.p.A., l’utente richiede la ristrutturazione della funzionalità di selezione del centro di lavoro responsabile con i seguenti obiettivi:

Guidare la compilazione del centro di lavoro responsabile in base al valore inserito nella unità impianti

Offrire il valore già filtrato, con la facoltà di cambiarlo, sempre nell’ambito di quelli possibili nel perimetro della unità impianti precedentemente inserita.

Il dialogo di selezione per popolare il centro di lavoro responsabile è presente nei dati di testata delle linee e delle stazioni e su tutte le righe di asset di secondo livello delle sole linee.

Si riassume per sinteticità gli oggetti dello sviluppo:

Applicazione delle linee.

Applicazione delle stazioni.

La funzione di selezione del centro di lavoro responsabile dovrà essere adattata anche nei seguenti casi di creazione di nuovi asset per l’applicazione delle linee:

Genera asset singolo di secondo livello

Inserisci tratta (aerea/cavo)

Crea derivazioni

Crea Split Linea

Di seguito i deliverable previsti:

Specifica funzionale.

Specifica tecnica.

Aggiornamento della documentazione di disegno (HLD/LLD).

Per gli UAT sarà fornito il test-book per la verifica di tutti gli scenari previsti.

Predisposizione della nota di rilascio corredata dei pacchetti di rilascio basati su migration manager.

Piano attività in formato Gantt.

# Descrizione della soluzione

## Operatività sul sistema

I possibili valori per il centro di lavoro responsabile sono legati alla unità impianti secondo la seguente convenzione: i valori dei centri di lavoro responsabile iniziano con il valore della unità impianti e termina con il valore _O. Ad esempio se l’unità impianti è UBO i centri di lavoro corrispondenti sono:

UBORE_Q

UBOTP_I

UBOTT_O

UBOTT_Q

UBOTT_I

I possibili valori dei centri responsabili possono essere determinati applicando il filtro di ricerca Unita_impianti% sulla tabella X3GEWRK (nell’esempio UBO%). Il requisito richiede, all’atto del popolamento da parte dell’utente dell’unità impianti, l’impostazione automatica del centro di lavoro responsabile che termina per _O (nell’esempio UBOTT_O). Questo automatismo sarà realizzato per le applicazioni Linee e Stazioni e relative funzionalità in cui è prevista l’immissione di unità impianti e centro responsabile (Genera asset singolo di secondo livello, Inserisci tratta (aerea/cavo), Crea derivazioni e Crea Split Linea).

Le dialog di ricerca per i centri di lavoro responsabili saranno ristrette agli effettivi centri di lavoro responsabili corrispondenti alle unità impianti. Se è impostata la unità impianti i centri di lavoro saranno ulteriormente ristretti in base a questa. Quindi nel caso ad esempi si imposti come unità impianti UBR automaticamente verrà impostato UBRTT_O come centro di lavoro responsabile.

Aprendo la dialog di ricerca per il centro di lavoro responsabile saranno mostrati tutti i valori corrispondenti alla unità impianti impostata.

Il filtro potrà essere rimosso per ottenere la lista di tutti i centri di lavoro responsabile a prescindere dall unità impianti impostata.

# Gestione dei dati (bonifiche e aggiornamenti)

Da una ricerca eseguita sul sistema in produzione sono stati individuati solo 61 riferiti ad asset non demoliti in cui l’unità impianti non è uguale alle prime tre lettere del centro di lavoro responsabile. Per questi non verrà eseguita nessuna operazione, rimandando l’eventuale azione alla valutazione ed esecuzione da parte del business di Terna. Si allega il documento con queste evidenze.

# file allegati

File delle discrepanze tra unità impianti e centro di lavoro responsabile.