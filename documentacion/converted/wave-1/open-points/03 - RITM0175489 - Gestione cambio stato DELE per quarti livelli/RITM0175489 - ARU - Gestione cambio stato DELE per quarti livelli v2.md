| ANALISI REQUISITI UTENTE<br> Gestione cambio stato DELE per quarti livelli |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Marco Lerro | 19/02/2026 | Prima emissione |
| 2 | Marco Lerro | 26/03/2026 | Correzione stato SAP per equipment stazioni |
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

3.	Descrizione della soluzione	6

3.1.	Req_U_002	6

3.2.	Req_U_003	6

3.3.	Aggiornamento documento funzionale e tecnica	7

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

Il periodo temporale di riferimento va indicativamente da Marzo 2026 e Maggio 2026. Eventuali ripianificazioni andranno concordate col PM di progetto.

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

Nell’ambito del progetto di realizzazione della piattaforma di Asset Management basata su IBM Maximo Application Suite (MAS) per Terna S.p.A., l’utente richiede di effettuare alcune modifiche alle logiche di sincronizzazione del connettore EAM/SAP al fine di ridurre disallineamenti fra i due applicativi. In particolare, nel requisito utente sono espresse le seguenti modifiche funzionali:

Req_U_001: Va eliminata l’azione di DELETE dai servizi di ZFMPMS4_FUNCLOC_MANAGE e ZFMPMS4_EQUIPMENT_MANAGE;

Req_U_002: Quando su EAM viene impostato lo stato utente DELE, su SAP verrà impostato DEMO o DIST, in base alla tipologia di Asset che prevede tali Stati Utente;

Req_U_003: Ogni chiamata proveniente da EAM, a prescindere dallo stato dell’asset inviato, farà UPDATE o RENAME, sia per le sedi tecniche sia per gli equipment;

Req_U_004: Verificare la corretta applicazione della transazione IL10 (in SAP) quando viene rinominato un SAPID in EAM;

Req_U_005: Eliminare la propagazione degli stati automatici in SAP sulle Sedi Tecniche sottostanti.

Data la natura dei requisiti espressi, la realizzazione può competere ad un singolo applicativo (EAM o SAP) o può richiedere una modifica congiunta. Nella tabella seguente si riporta l’ambito di intervento per ogni requisito.

| REQUISITO | EAM | SAP |
| --- | --- | --- |
| Req_U_001 | NO | SI |
| Req_U_002 | SI | NO |
| Req_U_003 | SI | SI (Bonifica) |
| Req_U_004 | NO | SI |
| Req_U_005 | NO | SI |

Pertanto, in questo documento di analisi dei requisiti verrà fornita la descrizione della soluzione per i requisiti Req_U_002 e Req_U_003. Per i requisiti Req_U_001, Req_U_004 e Req_U_005 si rimanda ad una analisi del fornitore SAP.

# Descrizione della soluzione

## Req_U_002

Requisito: Quando su EAM viene impostato lo stato utente DELE, su SAP verrà impostato DEMO o DIST, in base alla tipologia di Asset che prevede tali Stati Utente.

La gestione dello stato DELE è già presente in anagrafica EAM introdotto successivamente al go-live wave 1. Il connettore EAM/SAP non è stato adeguato di conseguenza. Pertanto, si procederà alla riconfigurazione del connettore al fine di introdurre questa gestione.

Nel dettaglio il connettore verrà configurato nella fase di produzione degli attributi e caratteristiche tecniche (step 3) nel contemplare la gestione dello stato DELE di un asset EAM. Si riporta di seguito la logica di mappatura dello stato DELE per un asset EAM rispetto alla corrispondente sede tecnica SAP.

| REGOLA | LINEA/STAZIONE | LIVELLO | EAM | SAP |
| --- | --- | --- | --- | --- |
| 1 | LINEA | 1 | DELE | DEMO |
| 2 | LINEA | 2 | DELE | DEMO |
| 3 | LINEA | 3 |  | Non mappato |
| 4 | LINEA | 4 | DELE | Non mappato |
| 5 | STAZIONE | 1 | DELE | DEMO |
| 6 | STAZIONE | 2 |  | Non mappato |
| 7 | STAZIONE | 3 | DELE | DEMO |
| 8 | STAZIONE | 4 | DELE | DIST |

Esiste il caso particolare in ambito stazioni della maglia di terra (MAGLIA DI TERRA) che in EAM è rappresentato da un asset di quarto livello mentre in SAP è una sede tecnica di terzo livello (IMPIANTO DI TERRA). Pertanto, questo caso speciale verrà trattato secondo la regola 7. Quindi nel caso in cui fosse impostato lo stato DELE su un asset EAM di quarto livello di tipo MAGLIA DI TERRA verrà inviato lo stato DEMO per la corrispondente sede tecnica SAP di tipo IMPIANTO DI TERRA. Questo caso speciale è legato al requisito Req_U_003 grazie al quale lo stato DELE non verrà più gestito come una effettiva cancellazione logica SAP (stato FCAN) bensì con un aggiornamento dello stato.

Le sincronizzazioni al momento fallite per la mancanza della gestione di questo requisito saranno recuperate automaticamente al rilascio dello stesso previa rimozione dello stato FCAN delle corrispondenti sedi tecniche SAP.

## Req_U_003

Requisito: Ogni chiamata proveniente da EAM, a prescindere dallo stato dell’asset inviato, farà UPDATE o RENAME, sia per le sedi tecniche sia per gli equipment.

La sincronizzazione di un asset EAM in stato DIST e DEMO (e stato DELE per effetto del requisito Req_U-002) al momento sono gestite dal connettore EAM/SAP con un flusso di sincronizzazione di DELETE. Pertanto, si procederà alla riconfigurazione del connettore al fine di inviare in questi casi un flusso di UPDATE che porterà l’aggiornamento dello stato della sede tecnica/equipement corrispondente senza innescare il passaggio allo stato FCAN.

Nel dettaglio il connettore verrà configurato nella fase di sincronizzazione (step 4) a inviare il flusso di UPDATE piuttosto che il flusso di DELETE.

## Aggiornamento documento funzionale e tecnica

Vista la natura dei requisiti, il documento di analisi funzionale e di low level design verranno aggiornati di conseguenza.