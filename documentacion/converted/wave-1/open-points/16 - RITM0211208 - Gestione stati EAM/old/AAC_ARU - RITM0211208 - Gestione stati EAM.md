| ANALISI REQUISITI UTENTE<br>Gestione Stati EAM |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Nicola Riccio | 05/06/2026 | Prima emissione |
|  |  |  |  |
|  |  |  |  |

Indice

1.	Generalità	3

Contesto di riferimento	3

Committente	3

Vincoli temporali e di pianificazione	3

Trattamento dei dati personali	3

Classificazione del sistema/applicazione	4

2.	Requisiti Funzionali	5

3.	Descrizione della soluzione	6

Req_U_001 - LINEE	6

Req_U_001 - STAZIONI	6

Aggiornamento documento funzionale e tecnica	6

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

Il periodo temporale di riferimento va indicativamente da Giungo 2026 a Luglio 2026. Eventuali ripianificazioni andranno concordate col PM di progetto.

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

Nell’ambito del sistema di Asset Management (EAM), l’utente richiede l’introduzione di nuove logiche di gestione e allineamento degli stati per asset appartenenti a Linee e Stazioni, al fine di garantire coerenza con il corretto funzionamento dei processi di Asset Management (es DSS, gestione scorte, gestione campagne, ecc.)

In particolare, vengono definiti i seguenti requisiti:

Req_U_001:

Gli asset di terzo livello Linee devono assumere automaticamente lo stato del relativo secondo livello.

Per gli asset di secondo e terzo livello Stazioni devono essere introdotti i nuovi stati DEMO e CEAL, per consentire la corretta disattivazione degli asset.

# Descrizione della soluzione

## Req_U_001 - LINEE

Gli Asset di terzo livello Linee, anche se non direttamente gestiti dall’utente, assumeranno lo stesso stato del relativo secondo livello contestualmente al cambiamento di stato.

Verrà quindi introdotta la logica di propagazione automatica dello stato da un asset di secondo livello ai terzi corrispondenti. In particolare, verrà intercettato l’evento di cambiamento di stato del secondo livello che farà partire una procedura (script di automazione) che recupererà i terzi livelli figli del secondo ed effettuerà il cambio di stato coerentemente. Il cambio di stato sarà effettuato in modo transazionale garantendo l’atomicità del passaggio di stato su tutti gli asset coinvolti. Lo stato degli asset di terzo livello non sarà modificabile manualmente e pertanto sarà sempre indotto dal cambio di stato dell’asset di livello superiore. La visibilità dello stato dell’asset dipenderà dalla configurazione di visibilità a sistema.

## Req_U_001 - STAZIONI

Per gli asset di secondo e terzo livello Stazioni saranno introdotti i seguenti stati:

DEMO: asset dismesso fisicamente

CEAL: asset cessato logicamente

Il modello stati per gli asset di 2° e 3° livello Stazioni sarà esteso in conformità a quanto definito nell’Excel allegato al requisito che si allega anche in questo documento per comodità.

La realizzazione di questo requisito prevede l’adeguamento delle logiche di applicabilità degli stati mediante l’aggiornamento delle conditional expression relative a ciascun stato coinvolto. Inoltre, verrà aggiornato come richiesto il flag GESTAT relativo alle classificazioni al fine di rendere visibili gli stati per le classificazioni richieste.

## Aggiornamento documento funzionale e tecnica

Vista la natura dei requisiti, il documento di analisi funzionale (high level design) e di low level design verranno aggiornati di conseguenza. È previsto inoltre la produzione di una nota di rilascio per l’aggiornamento della funzionalità, configurazione della gestione di visibilità degli stati (flag GESTAT) e delle procedure per la bonifica degli stati dei terzi livelli Linea.