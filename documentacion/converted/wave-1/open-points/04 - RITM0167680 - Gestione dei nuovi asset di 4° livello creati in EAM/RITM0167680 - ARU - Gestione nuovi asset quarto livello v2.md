| ANALISI REQUISITI UTENTE<br> Gestione creazioni Asset di 4° livello |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Marco Lerro | 09/02/2026 | Prima emissione |
| 2 | Raffaele Vaiano | 17/02/2026 |  |
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

Req_U_003: Adeguamento creazione asset relazionati per i comparti blindati/MCM

# Descrizione della soluzione

## Req_U_001: Creazione asset relazionati tramite funzione dedicata

Il requisito Req_U_001 prevede la possibilità di creare asset relazionati (ad esempio ANTIROTAZIONALI CONDUTTORE) a partire da un asset di riferimento (ad esempio CONDUTTORE) tramite l’azione già disponibile a sistema “Crea asset relazionato” (l’azione è disponibile dal menu contestuale degli asset di quarto livello per cui è possibile creare asset relazionati). Selezionando questa azione sarà presentata un dialogo che elencherà i possibili asset relazionati all’asset di riferimento, da cui è possibile selezionare solo una classificazione per volta relativa ai possibili asset relazionabili.

Gli asset relazionati non potranno più essere aggiunti dalla funzionalità “Crea asset di quarto livello”.

In questo dialogo sarà possibile selezionare la classificazione dell’asset relazionato compatibile con la classificazione dell’asset di partenza. Saranno escluse eventuali classificazioni per le quali già esiste l’asset relazionato e la cardinalità prevista è 1 escludendo dal conteggio eventuali asset in stato DIST o DELE. In questo modo l’utente non potrà violare la cardinalità. Selezionata la classificazione, per i soli asset con cardinalità superiore ad 1, verranno mostrati gli asset già presenti come indicato in figura. La data di installazione partirà vuota e deve essere obbligatoriamente immessa per poter creare l’asset relazionato. Alla pressione del tasto Crea verranno creati gli asset relazionati impostando la caratteristica di collegamento (nell’esempio LIN_RIF_ASSET_COND) automaticamente.

La lista dei possibili asset relazionati per asset di riferimento sono riportati nella tabella seguente.

| ASSET DI RIFERIMENTO | ASSET RELAZIONATI | CARATTERISTICA DI COLLEGAMENTO |
| --- | --- | --- |
| CONDUTTORE | ANTIROTAZIONALI CONDUTTORE<br>ANTIVIBRANTI CONDUTTORE<br>BARRETTA PREFORMATA CONDUTTORE<br>DISSUASORI AVIFAUNA CONDUTTORE<br>DISTANZIATORI<br>DISTANZIATORI INTERFASE<br>WRAPPED CONDUTTORE<br>GIUNTO CONDUTTORE<br>SFERE DI SEGNALAZIONE CONDUTTORE | LIN_RIF_ASSET_COND |
| FUNE DI GUARDIA | ANTIROTAZIONALI FUNE DI GUARDIA<br>ANTIVIBRANTI FUNE DI GUARDIA<br>BARRETTA PREFORMATA FUNE DI GUARDIA<br>DISSUASORI AVIFAUNA FUNE DI GUARDIA<br>WRAPPED FDG<br>GIUNTO FUNE DI GUARDIA<br>SFERE DI SEGNALAZIONE FUNE DI GUARDIA | LIN_RIF_ASSET_FDG |
| ARMAMENTO FUNE DI GUARDIA | ISOLATORE FUNE DI GUARDIA | LIN_RIF_ASSET_ARM_FDG |
| TERMINALE | CASSETTA DI SEZIONAMENTO | LIN_RIF_ASSET_TERM<br>(da creare) |
| INTERRUTTORI BLINDATI | COMANDO PER INTERRUTTORE AT BLINDATO | STZ_RIF_ASSET |
| SEZIONATORI BLINDATI | COMANDO PER SEZIONATORE AT BLINDATO | STZ_RIF_ASSET |
| MACCHINARIO DI POTENZA | ISOLATORE PASSANTE | STZ_RIF_ASSET |

## Req_U_002: Valorizzazioni automatiche degli attributi nel modello Linee

Il requisito Req_U_002 indirizza la valorizzazione automatica di alcune caratteristiche per asset di quarto livello creati mediante l’operazione Inserisci tratta o altra funzione di creazione asset (ad esempio Crea derivazione). La seguente tabella riassume le valorizzazioni che verranno applicate.

| ASSET DI RIFERIMENTO | NUMEROSITA | CARATTERISTICA | VALORIZZAZIONE |
| --- | --- | --- | --- |
| CONDUTTORE | 3 | LIN_RIF_POSIZIONE | 1, 2, 3 per ognuno dei 3 asset creati |
| ARMAMENTO CONDUTTORE | 3 | LIN_RIF_POSIZIONE | 1, 2, 3 per ognuno dei 3 asset creati |
| FUNE DI GUARDIA | 1 | LIN_RIF_FUNE | DX |
| ARMAMENTO FUNE DI GUARDIA | 1 | LIN_RIF_FUNE | DX |
| ISOLATORE CONDUTTORE | 3 | LIN_RIF_ASSET_ARM_COND | Con codice asset dell’armamento conduttore |
| CAVO DI POTENZA | 3 | RIF_FASE | R, S, T per ognuno dei 3 asset creati |
| GIUNTO CAVO | 3 | RIF_FASE | R, S, T per ognuno dei 3 asset creati |
| TERMINALE | 3 | RIF_FASE | R, S, T per ognuno dei 3 asset creati |

Per quanto riguarda i casi di inserimento puntuale mediante la funzione “Crea asset di quarto livello” per la FUNE DI GUARDIA e “Crea asset relazionato” per l’ARMAMENTO FUNE DI GUARDIA verranno gestiti indicati nella tabella seguente.

| ASSET DI RIFERIMENTO | CARATTERISTICA | VALORIZZAZIONE |
| --- | --- | --- |
| FUNE DI GUARDIA | LIN_RIF_FUNE | SX se si crea la seconda fune di guardia<br>3 se si crea la terza fune di guardia |
| ARMAMENTO FUNE DI GUARDIA | LIN_RIF_FUNE | SX se si crea il terzo armamento<br>3 se si crea il terzo armamento |

Se il valore risultasse incompatibile con quanto presente a sistema la caratteristica sarà valorizzata con il valore disponibile seguendo l’ordine DX, SX , 3.

Inoltre per la caratteristica tecnica LIN_RIF_FUNE (relativa alle classificazioni FUNE DI GUARDIA e ARMAMENTO DI GUARDIA), LIN_RIF_POSIZIONE (relativa alle classificazioni CONDUTTORE e ARMAMENTO CONDUTTORE) e RIF_FASE (relativa alle classificazioni CAVO DI POTENZA, GIUNTO CAVO e TERMINALE) sarà inserito un controllo di validazione che assicurerà la compilazione consisente sia tramite applicazione che caricamenti massivi che garantisce l’uso esclusivo dei possibili valori secondo la logica indicata nella tabella seguente.

| ASSET DI RIFERIMENTO | CARATTERISTICA | VALORIZZAZIONE |
| --- | --- | --- |
| FUNE DI GUARDIA | LIN_RIF_FUNE | La caratteristica può essere sempre impostata a valore nullo.<br>Se è presente un solo asset l’unico valore non nullo possibile sarà DX.<br>Se sono presenti due asset il valore per un asset può essere DX o SX in base al valore dell’altro.<br>Se sono presenti tre asset il valore per un asset può essere DX, SX o 3 in base al valore degli altri due. |
| ARMAMENTO FUNE DI GUARDIA | LIN_RIF_FUNE | La caratteristica può essere sempre impostata a valore nullo.<br>Se è presente un solo asset l’unico valore non nullo possibile sarà DX.<br>Se sono presenti due asset il valore per un asset può essere DX o SX in base al valore dell’altro.<br>Se sono presenti tre asset il valore per un asset può essere DX, SX o 3 in base al valore degli altri due. |
| CONDUTTORE | LIN_RIF_POSIZIONE | La caratteristica può essere sempre impostata a valore nullo.<br>Se è presente un solo asset l’unico valore non nullo possibile sarà 1.<br>Se sono presenti due asset il valore per un asset può essere 1 o 2 in base al valore dell’altro.<br>Se sono presenti tre asset il valore per un asset può essere 1, 2 o 3 in base al valore degli altri due. |
| ARMAMENTO CONDUTTORE | LIN_RIF_POSIZIONE | La caratteristica può essere sempre impostata a valore nullo.<br>Se è presente un solo asset l’unico valore non nullo possibile sarà 1.<br>Se sono presenti due asset il valore per un asset può essere 1 o 2 in base al valore dell’altro.<br>Se sono presenti tre asset il valore per un asset può essere 1, 2 o 3 in base al valore degli altri due. |
| CAVO DI POTENZA | RIF_FASE | La caratteristica può essere sempre impostata a valore nullo.<br>Se è presente un solo asset l’unico valore non nullo possibile sarà R.<br>Se sono presenti due asset il valore per un asset può essere R o S in base al valore dell’altro.<br>Se sono presenti tre asset il valore per un asset può essere R, S o T in base al valore degli altri due. |
| GIUNTO CAVO | RIF_FASE | La caratteristica può essere sempre impostata a valore nullo.<br>Se è presente un solo asset l’unico valore non nullo possibile sarà R.<br>Se sono presenti due asset il valore per un asset può essere R o S in base al valore dell’altro.<br>Se sono presenti tre asset il valore per un asset può essere R, S o T in base al valore degli altri due. |
| TERMINALE | RIF_FASE | La caratteristica può essere sempre impostata a valore nullo.<br>Se è presente un solo asset l’unico valore non nullo possibile sarà R.<br>Se sono presenti due asset il valore per un asset può essere R o S in base al valore dell’altro.<br>Se sono presenti tre asset il valore per un asset può essere R, S o T in base al valore degli altri due. |

## Req_U_003: Adeguamento creazione asset relazionati per i comparti blindati/MCM

Con questo requisito di chiede di consentire la creazione dei seguenti asset di quarto livello stazioni mediante la funzione “Crea asset relazionato” che dovrà essere disponibile per gli asset COMPARTO BLINDATO e COMPARTO MCM.

INTERRUTTORE AT BLINDATO

PASSANTE AT BLINDATO SF6/ARIA

PASSANTE AT BLINDATO SF6/CAVO

SCARICATORE AT BLINDATO

SEZIONATORE AT AD ANGOLO SEMPLICE BLIND.

SEZIONATORE AT DI LINEA BLINDATO

SEZIONATORE AT DI TERRA BLINDATO

SEZIONATORE AT DI TERRA RAPIDO BLINDATO

TA AT BLINDATO

TV AT BLINDATO

Gli asset relazionati non potranno più essere aggiunti dalla funzionalità “Crea asset di quarto livello”.

La creazione dell’asset relazionato dovrà impostare l’attributo STZ_COMPARTO_RIFERIMENTO con il numero dell’asset di partenza (COMPARTO BLINDATO o COMPARTO MCM). Inoltre l’attributo STZ_TIPO_COMPARTO dovrà essere valorizzato con il valore corrispondente del’asset di partenza.