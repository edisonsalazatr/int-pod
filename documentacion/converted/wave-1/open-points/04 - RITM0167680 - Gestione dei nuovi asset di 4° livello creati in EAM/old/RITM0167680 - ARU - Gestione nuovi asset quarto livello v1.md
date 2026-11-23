| ANALISI REQUISITI UTENTE<br> Gestione creazioni Asset di 4° livello |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Marco Lerro | 09/02/2026 | Prima emissione |
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

3.	Descrizione della soluzione	6

3.1.	Req_U_001: Creazione asset relazionati tramite funzione dedicata	6

3.2.	Req_U_002: Valorizzazioni automatiche degli attributi nel modello Linee	7

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

Il periodo temporale di riferimento va indicativamente da Febbraio 2026 e Maggio 2026. Eventuali ripianificazioni andranno concordate col PM di progetto.

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

Nell’ambito del progetto di realizzazione della piattaforma di Asset Management basata su IBM Maximo Application Suite (MAS) per Terna S.p.A., l’utente richiede di rafforzare la qualità, la tracciabilità e l’efficienza dei processi anagrafici e operativi e nel dettaglio la realizzazione dei seguenti requisiti:

Req_U_001: Creazione asset relazionati tramite funzione dedicata

Req_U_002: Valorizzazioni automatiche degli attributi nel modello Linee

# Descrizione della soluzione

## Req_U_001: Creazione asset relazionati tramite funzione dedicata

Il requisito Req_U_001 prevede la possibilità di creare asset relazionati (ad esempio ANTIROTAZIONALI CONDUTTORE) a partire da un asset di riferimento (ad esempio CONDUTTORE). Pertanto, dal menu accessibile da un asset di quarto livello sarà disponibile l’azione “Crea asset relazionato”. Selezionando questa azione sarà presentata un dialogo che elencherà i possibili asset relazionati all’asset di riferimento.

In questo dialogo saranno mostrati per ogni asset relazionato la sua classificazione, la cardinalità prevista, il numero di asset già presenti a sistema. Sarà infine possibile indicare il numero di asset da creare per ogni classificazione compatibilmente con la cardinalità ed il numero di asset presenti. Inizialmente il numero sarà impostato a zero e sarà compito dell’utente valorizzare il numero di asset da creare desiderato. In caso di numero incompatibile verrà mostrato un messaggio esplicativo. Alla pressione del tasto Crea verranno creati gli asset relazionati impostando la caratteristica di collegamento (nell’esempio LIN_RIF_ASSET_COND) automaticamente.

La lista dei possibili asset relazionati per asset di riferimento sono riportati nella tabella seguente.

| ASSET DI RIFERIMENTO | ASSET RELAZIONATI | CARATTERISTICA DI COLLEGAMENTO |
| --- | --- | --- |
| CONDUTTORE | ANTIROTAZIONALI CONDUTTORE<br>ANTIVIBRANTI CONDUTTORE<br>BARRETTA PREFORMATA CONDUTTORE<br>DISSUASORI AVIFAUNA CONDUTTORE<br>DISTANZIATORI<br>DISTANZIATORI INTERFASE<br>WRAPPED CONDUTTORE<br>GIUNTO CONDUTTORE<br>SFERE DI SEGNALAZIONE CONDUTTORE | LIN_RIF_ASSET_COND |
| FUNE DI GUARDIA | ANTIROTAZIONALI FUNE DI GUARDIA<br>ANTIVIBRANTI FUNE DI GUARDIA<br>BARRETTA PREFORMATA FUNE DI GUARDIA<br>DISSUASORI AVIFAUNA FUNE DI GUARDIA<br>WRAPPED FDG<br>GIUNTO FUNE DI GUARDIA<br>SFERE DI SEGNALAZIONE FUNE DI GUARDIA | LIN_RIF_ASSET_FDG |
| ARMAMENTO CONDUTTORE | ISOLATORE CONDUTTORE | LIN_RIF_ASSET_ARM_COND |
| ARMAMENTO FUNE DI GUARDIA | ISOLATORE FUNE DI GUARDIA | LIN_RIF_ASSET_ARM_FDG |

## Req_U_002: Valorizzazioni automatiche degli attributi nel modello Linee

Il requisito Req_U_002 indirizza la valorizzazione automatica di alcune caratteristiche per asset di quarto livello creati mediante l’operazione Inserisci tratta. La seguente tabella riassume le valorizzazioni che verranno applicate.

| ASSET DI RIFERIMENTO | NUMEROSITA | CARATTERISTICA | VALORIZZAZIONE |
| --- | --- | --- | --- |
| CONDUTTORE | 3 | LIN_RIF_POSIZIONE | 1, 2, 3 per ognuno dei 3 asset creati |
| ARMAMENTO CONDUTTORE | 3 | LIN_RIF_POSIZIONE | 1, 2, 3 per ognuno dei 3 asset creati |
| FUNE DI GUARDIA | 1 | LIN_RIF_FUNE | DX |
| ARMAMENTO FUNE DI GUARDIA | 1 | LIN_RIF_FUNE | DX |
| CAVO DI POTENZA | 3 | RIF_FASE | R, S, T per ognuno dei 3 asset creati |
| GIUNTO CAVO | 3 | RIF_FASE | R, S, T per ognuno dei 3 asset creati |
| TERMINALE | 3 | RIF_FASE | R, S, T per ognuno dei 3 asset creati |