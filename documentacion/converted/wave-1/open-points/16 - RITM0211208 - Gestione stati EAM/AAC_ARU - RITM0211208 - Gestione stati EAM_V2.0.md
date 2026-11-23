| ANALISI REQUISITI UTENTE<br>Gestione Stati EAM |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Nicola Riccio | 05/06/2026 | Prima emissione |
| 2 | Nicola Riccio | 09/06/2026 | Seconda Emissione |
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

Non potrà essere possibile applicare ad un asset linea di un determinato livello uno stato non coerente con quello dei propri “figli”. Dovrà essere segnalata questa incoerenza con un messaggio di errore (ad es. “Non è possibile inserire lo stato DEMO a tale asset in quanto i seguenti figli non sono in stato DEMO: indicare elenco con assetnum, SAPID, descrizione e stato”), in particolare al cambio di stato del 1° livello dovrà essere fatto un controllo di coerenza per verificare le seguenti condizioni:

DEMO: verificare che tutti i 2° livelli siano in DEMO o CEAL;

CEAL: verificare che tutti i 2° livelli siano in CEAL o DEMO;

NADI: verificare che non siano presenti 2° livelli in stato ATTV e che ce ne sia almeno uno in stato NADI. È possibile passare da stato NADI a stato ATTV o NAND;

NAND: verificare che non siano presenti 2° livelli in stato ATTV e che ce ne sia almeno uno in stato NAND. È possibile passare da stato NAND a ATTV o NADI;

Non potrà essere possibile applicare ad un asset stazione di un determinato livello uno stato non coerente con quello dei propri “figli”. Dovrà essere segnalata questa incoerenza con un messaggio di errore (ad es. “Non è possibile inserire lo stato DEMO a tale asset in quanto i seguenti figli non sono in stato DEMO: indicare elenco con assetnum, SAPID, descrizione e stato”), in particolare al cambio stato del 3° livello dovrà essere fatto un controllo di coerenza per verificare le seguenti condizioni:

DEMO: verificare che tutti i 4° livelli siano in DIST;

CEAL: verificare che tutti i 4° livelli siano in DIST;

NADI: verificare che non siano presenti 4° livelli in stato ESER. È possibile passare da stato NADI a stato ATTV o NAND;

NAND: verificare che non siano presenti 4° livelli in stato ESER. È possibile passare da stato NAND a ATTV o NADI;

Al cambio di stato del 1° e del 2° livello stazioni dovrà essere fatto un controllo di coerenza per verificare le seguenti condizioni:

DEMO: verificare che tutti i 2° livelli siano in DEMO o CEAL;

CEAL: verificare che tutti i 2° livelli siano in CEAL o DEMO;

NADI: verificare che non siano presenti 2° livelli in stato ATTV e che ce ne sia almeno uno in stato NADI. È possibile passare da stato NADI a stato ATTV o NAND;

NAND: verificare che non siano presenti 2° livelli in stato ATTV e che ce ne sia almeno uno in stato NAND. È possibile passare da stato NAND a ATTV o NADI;

Per gli asset di secondo e terzo livello Stazioni devono essere introdotti i nuovi stati DEMO e CEAL, per consentire la corretta disattivazione degli asset.

Revisione e ordinamento dell’elenco del menù a discesa del cambio stato degli asset indicando anche il relativo acronimo (es. ATTV, NAND, DELE, DEMO, ecc.)

# Descrizione della soluzione

## Req_U_001 - LINEE

Gli Asset di terzo livello Linee, anche se non direttamente gestiti dall’utente, assumeranno lo stesso stato del relativo secondo livello contestualmente al cambiamento di stato.

Verrà quindi introdotta la logica di propagazione automatica dello stato da un asset di secondo livello ai terzi corrispondenti. In particolare, verrà intercettato l’evento di cambiamento di stato del secondo livello che farà partire una procedura (script di automazione) che recupererà i terzi livelli figli del secondo ed effettuerà il cambio di stato coerentemente. Il cambio di stato sarà effettuato in modo transazionale garantendo l’atomicità del passaggio di stato su tutti gli asset coinvolti. Lo stato degli asset di terzo livello non sarà modificabile manualmente e pertanto sarà sempre indotto dal cambio di stato dell’asset di livello superiore. La visibilità dello stato dell’asset dipenderà dalla configurazione di visibilità a sistema.

La funzionalità non prevede attività di bonifica, che verranno indirizzate da una RITM successiva.

Non sarà consentita l'applicazione di uno stato ad un asset qualora tale stato risulti incoerente rispetto allo stato dei relativi asset figli. In caso di anomalia il sistema bloccherà l'operazione di cambio stato e restituirà un messaggio contenente:

Numero dell’asset;

Id SAP;

Descrizione;

Stato corrente.

Verrà implementato un controllo sul cambio stato del 1° livello Linea, nel dettaglio:

Stato DEMO

Il passaggio a DEMO sarà consentito esclusivamente se tutti i 2° livelli associati risultano in uno dei seguenti stati:

DEMO

CEAL

Presenza di altri stati ⇒ operazione non consentita.

Stato CEAL

Il passaggio a CEAL sarà consentito esclusivamente se tutti i 2° livelli associati risultano in:

CEAL

DEMO

Presenza di altri stati ⇒ operazione non consentita.

Stato NADI

Il passaggio a NADI sarà consentito se:

nessun 2° livello risulta in stato ATTV;

almeno un 2° livello risulta in stato NADI.

In caso contrario l'operazione verrà bloccata.

Stato NAND

Il passaggio a NAND sarà consentito se:

nessun 2° livello risulta in stato ATTV;

almeno un 2° livello risulta in stato NAND.

In caso contrario l'operazione verrà bloccata.

## Req_U_001 - STAZIONI

Per gli asset di secondo e terzo livello Stazioni saranno introdotti i seguenti stati:

DEMO: asset dismesso fisicamente

CEAL: asset cessato logicamente

Il modello stati per gli asset di 2° e 3° livello Stazioni sarà esteso in conformità a quanto definito nell’Excel allegato al requisito che si allega anche in questo documento per comodità.

La realizzazione di questo requisito prevede l’adeguamento delle logiche di applicabilità degli stati mediante l’aggiornamento delle conditional expression relative a ciascun stato coinvolto. Inoltre, verrà aggiornato come richiesto il flag GESTAT relativo alle classificazioni al fine di rendere visibili gli stati per le classificazioni richieste.

Per gli asset appartenenti alle STAZIONI verrà verificata la congruenza con i relativi asset figli di 4° livello.

Nel dettaglio:

Stato DEMO

Consentito esclusivamente se tutti i 4° livelli risultano in stato DIST.

Stato CEAL

Consentito esclusivamente se tutti i 4° livelli risultano in stato DIST.

Stato NADI

Consentito se:

nessun 4° livello risulta in stato ESER.

Sono consentite successive variazioni:

NADI → ATTV

NADI → NAND

Stato NAND

Consentito se:

nessun 4° livello risulta in stato ESER.

Sono consentite successive variazioni:

NAND → ATTV

NAND → NADI

Per gli asset appartenenti alla struttura STAZIONI dovrà essere verificata la congruenza con i relativi asset figli di 4° livello.

Stato DEMO

Consentito esclusivamente se tutti i 4° livelli risultano in stato DIST.

Stato CEAL

Consentito esclusivamente se tutti i 4° livelli risultano in stato DIST.

Stato NADI

Consentito se:

nessun 4° livello risulta in stato ESER.

Sono consentite successive variazioni:

NADI → ATTV

NADI → NAND

Stato NAND

Consentito se:

nessun 4° livello risulta in stato ESER.

Sono consentite successive variazioni:

NAND → ATTV

NAND → NADI

Per gli asset di 1° e 2° livello STAZIONI verrà essere eseguito un controllo sui relativi asset di 2° livello.

Stato DEMO

Consentito esclusivamente se tutti i 2° livelli risultano:

DEMO

CEAL

Stato CEAL

Consentito esclusivamente se tutti i 2° livelli risultano:

CEAL

DEMO

Stato NADI

Consentito se:

nessun 2° livello è in stato ATTV;

almeno un 2° livello è in stato NADI.

Stato NAND

Consentito se:

nessun 2° livello è in stato ATTV;

almeno un 2° livello è in stato NAND.

Sia per Linee che per Stazioni, verrà rivisto il menù a discesa del cambio stato, indicando oltra alla descrizione dello stato, già presente, il relativo acronimo.

Inoltre, gli stati verranno ordinati secondo il seguente ordine: (verificare)

LINEE:

| ATTV | NADI | NAND | DEMO | CEAL | ESER | EFFE | GUAS | RIPR | DIST | DELE |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |

STAZIONI:

| ATTV | NADI | NAND | DEMO | CEAL |
| --- | --- | --- | --- | --- |

| EFFE | ESER | EFDI | EFPR | EFSC | GUAS | NIED | NIEP | NIES | NIGU | RIPR | NIEO | DIST | DELE |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |

## Aggiornamento documento funzionale e tecnica

Vista la natura dei requisiti, il documento di analisi funzionale (high level design) e di low level design verranno aggiornati di conseguenza. È previsto inoltre la produzione di una nota di rilascio per l’aggiornamento della funzionalità, configurazione della gestione di visibilità degli stati (flag GESTAT) e delle procedure per la bonifica degli stati dei terzi livelli Linea.