|  |  |
| --- | --- |

Controllo del documento

Identificazione del documento

| Nome progetto: |  |
| --- | --- |
| Funzione: |  |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 0.0 | Prima stesura | gg/mm/aaaa |
| 0.1 |  | gg/mm/aaaa |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | Nome Cognome | gg/mm/aaaa |  |
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

Archiviazione

Fornire indicazioni sul relativo archivio di progetto

INDICE

1.	GENERALITÀ	7

1.1.	Contesto di riferimento	7

1.2.	Soluzione individuata	7

1.3.	Matrice di tracciabilità requisiti	7

2.	AS-IS	9

2.1.	Processo AS-IS	9

2.1.1.	Workflow di processo AS-IS	9

2.1.2.	Matrice RACI AS-IS	9

2.2.	Sistemi applicativi a supporto AS-IS (con eventuale Flow Chart)	10

2.2.1.	Mappa applicativa AS-IS	10

2.2.2.	Flussi funzionali di integrazione AS-IS	10

3.	SOLUZIONE TO-BE	11

3.1.	Processo TO-BE	11

3.1.1.	Workflow di processo TO-BE	11

3.1.2.	Matrice RACI TO-BE	11

3.2.	Sistemi applicativi a supporto TO-BE (con eventuale Flow Chart)	12

3.2.1.	Mappa applicativa TO-BE	12

3.2.2.	Flussi integrazione TO-BE	12

4.	REQUISITI FUNZIONALI DELL’INIZIATIVA	13

4.1.	Titolo Requisito Funzionale 1 (Id Requisito funzionale)	13

4.1.1.	Gestione e formato dei dati, gestione eventuali errori	14

4.1.2.	Algoritmi di calcolo	14

4.1.3.	Profondità storica dei dati	14

4.1.4.	Conformità alla normativa	14

4.2.	Titolo Requisito Funzionale n (Id Requisito funzionale)	14

5.	ULTERIORI REQUISITI NON FUNZIONALI DELL’INIZIATIVA	14

5.1.	Identity Management, profilatura e gestione utenti	15

5.2.	Privacy	15

5.3.	Performance	15

5.4.	Formazione	15

5.5.	Requisiti sulla gestione dei dati/archivi	15

6.	INTERFACCIA UTENTE	16

6.1.	Menu e historyboard	16

6.2.	Elenco form	16

7.	EVENTUALI RISCHI	16

INDICE DELLE TABELLE

Tabella 1 - Riferimenti documentali	6

Tabella 2 - Definizioni, acronimi, abbreviazioni	6

Tabella 3 – Generalità	7

Tabella 4 - Matrice di tracciabilità dei requisiti	8

Tabella 5 - Matrice RACI AS-IS	10

Tabella 6 – Flussi funzionali di integrazione AS-IS	11

Tabella 7 - Matrice RACI TO-BE	12

Tabella 8 – Flussi funzionali di integrazione TO-BE	13

Tabella 9 – Requisiti funzionali dell’iniziativa	13

Tabella 10 – Ulteriori Requisiti non funzionali dell’iniziativa	15

Tabella 11 – Valutazione rischi/criticità	16

RIFERIMENTI DOCUMENTALI

“In questa tabella viene riportato un elenco di tutti i documenti utili, come:

Documento di Requisiti Utente – RU;

eventuali requisiti collegati o relativi a fasi diverse del progetto;

documenti provenienti da altre funzioni (es. Rete, Architetture, Fornitori, Security, Regolatorio”

| Rif. | Titolo Documento | Versione | Data | Codice Documento |
| --- | --- | --- | --- | --- |
| Codice identificativo del documento di riferimento | Riportare il nome del documento | Specificare la versione del documento | Riportare la data in cui è stato emesso il documento | Riportare il codice del documento di riferimento |
|  |  |  |  |  |

Tabella 1 - Riferimenti documentali

Definizioni, acronimi e abbreviazioni

“Definizioni di base, necessarie per una comprensione del testo completa e non ambigua.”

| Sigla | Descrizione |
| --- | --- |
|  |  |
|  |  |

Tabella 2 - Definizioni, acronimi, abbreviazioni

# GENERALITÀ

## Contesto di riferimento

“Descrivere sinteticamente ma in modo completo l’ambito di business, gli obiettivi del progetto ed i contenuti funzionali dello stesso, riprendendo quanto presente nel documento Requisiti Utente, includendo i dettagli relativi al timing.

La descrizione va agganciata alle diverse classi di utenti identificate nel documento Requisiti, ed articolata in modo schematico (utilizzando Bullet e Tabelle) per ciascuna classe.

Questo paragrafo ha un valore introduttivo e propedeutico alla comprensione dei casi d’uso, pertanto il livello di dettaglio deve essere limitato.

Vanno però identificate le diverse tipologie di casi d’uso, che saranno poi ripresi ed articolati caso per caso nel capitolo dedicato.

Inoltre, compilare la tabella 3 Generalità. Alcuni campi anagrafici sono ripresi dal documento di Requisiti Utente mentre altri sono da definire in questa sede”.

| Informazioni Iniziativa | Informazioni Iniziativa | Informazioni Iniziativa |
| --- | --- | --- |
| Codice Iniziativa |  |  |
| Data Rilascio Iniziativa ipotizzata | gg/mm/aaaa | gg/mm/aaaa |
| Richiedente |  | Inserire eventuali note |
| Tipo Iniziativa |  | Inserire eventuali note |
| Metodologia di sviluppo |  | Inserire eventuali note |
| Classificazione delle informazioni | Classificazione delle informazioni | Classificazione delle informazioni |
| Codice scheda Archer | Inserire codice | Inserire eventuali note |

Tabella 3 – Generalità

## Soluzione individuata

“Descrivere la soluzione individuata e le modalità di realizzazione/tecnologia di installazione (es. nome dell’applicativo, se già nella mappa applicativa Terna e se in cloud oppure on premises)

## Matrice di tracciabilità requisiti

“Riprendere l’elenco requisiti indicato all’interno del documento Requisiti Utente al fine di associare una corrispondenza, per ciascun Requisito Utente, con uno o più Requisiti Funzionali”

| Id Requisito Utente | Titolo del requisito | Business owner | Priorità di Business | Dipendenza da altri requisiti Utente (ID)* | Eventuali impatti di business | Tipologia | Requisito Funzionale associato |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Identificativo univoco del requisito utente | Descrizione d’alto livello del requisito utente | Chi ha richiesto l’attività | Alta, media, bassa | Indicare se esistono collegamenti con altri requisiti utente | Vincolo normativo, impatto critico su un processo di business (es.: attività bloccante, no workaround, degrado delle performance, ecc.) | Indicare la tipologia di requisito derivante dal desiderata utente (requisito funzionale; non funzionale (Sicurezza, Privacy, Performance, Normativo, Formativo) | Indicare il paragrafo del presente documento in cui si indica il requisito funzionale che soddisfa il requisito utente |
| Identificativo univoco del requisito utente | Descrizione d’alto livello del requisito utente | Chi ha richiesto l’attività | Alta, media, bassa | Indicare se esistono collegamenti con altri requisiti utente | Vincolo normativo, impatto critico su un processo di business (es.: attività bloccante, no workaround, degrado delle performance, ecc.) | Indicare la tipologia di requisito derivante dal desiderata utente (requisito funzionale; non funzionale (Sicurezza, Privacy, Performance, Normativo, Formativo) | Indicare il paragrafo del presente documento in cui si indica il requisito funzionale che soddisfa il requisito utente |

Tabella 4 - Matrice di tracciabilità dei requisiti

# AS-IS

## Processo AS-IS

“Descrizione dell’attuale processo AS-IS con identificazione delle attività e delle unità coinvolte”

### Workflow di processo AS-IS

Inserire il disegno del workflow del processo AS-IS in accordo allo Standard BPMN

Allegare il documento del workflow del processo AS-IS in formato Visio

### Matrice RACI AS-IS

Inserire la matrice RACI AS-IS per l’identificazione di ruoli e responsabilità degli attori coinvolti nelle attività del processo.

| ID | ATTIVITÀ | Attore/Unità 1 | Attore/Unità 2 | Attore/Unità 3 |
| --- | --- | --- | --- | --- |
| 1 | Descrizione attività | R | A | I |
| 2 | Descrizione attività | R | A | C |
| 3 | Descrizione attività | A | R | I |

Tabella 5 - Matrice RACI AS-IS

R=Responsible   A=Accountable   C=Consulted   I=Informed

## Sistemi applicativi a supporto AS-IS (con eventuale Flow Chart)

“Descrizione dell’attuale sistema e delle attuali funzionalità limitatamente all’oggetto progettuale”

### Mappa applicativa AS-IS

Inserire l’immagine della mappa applicativa AS-IS

Allegare eventuale documento della mappa applicativa AS-IS

### Flussi funzionali di integrazione AS-IS

Inserire elenco e descrizione dei flussi di integrazione AS-IS coinvolti dall’iniziativa dal punto di vista funzionale, come da esempio di seguito riportato.

| Nome Processo | Id task | Cod. Flusso | Da (Applicazione) | A (Applicazione) | Flusso dati (funzionale) | Tipologia di Flusso |
| --- | --- | --- | --- | --- | --- | --- |
| Nome processo | Codice identificativo task | Codice identificativo flusso | Applicazione 1 | Applicazione 2 | Indicare informazioni e dati scambiati tra gli applicativi | Indicare tipologia integrazione (eventuale) |
| Nome processo | Codice identificativo task | Codice identificativo flusso | Applicazione 1 | Applicazione 2 | Indicare informazioni e dati scambiati tra gli applicativi | Indicare tipologia integrazione (eventuale) |
| Nome processo | Codice identificativo task | Codice identificativo flusso | Applicazione 1 | Applicazione 2 | Indicare informazioni e dati scambiati tra gli applicativi | Indicare tipologia integrazione (eventuale) |

Tabella 6 – Flussi funzionali di integrazione AS-IS

# SOLUZIONE TO-BE

## Processo TO-BE

“Descrizione del processo TO-BE con identificazione delle attività e delle unità coinvolte”

### Workflow di processo TO-BE

Inserire il disegno del workflow del processo TO-BE in accordo allo Standard BPMN

Allegare il documento del workflow del processo TO-BE in formato Visio

### Matrice RACI TO-BE

Inserire la matrice RACI TO-BE per l’identificazione di ruoli e responsabilità degli attori coinvolti nelle attività del processo.

| ID | ATTIVITÀ | Attore/Unità 1 | Attore/Unità 2 | Attore/Unità 3 |
| --- | --- | --- | --- | --- |
| 1 | Descrizione attività | R | A | I |
| 2 | Descrizione attività | R | A | C |
| 3 | Descrizione attività | A | R | I |

Tabella 7 - Matrice RACI TO-BE

R=Responsible   A=Accountable   C=Consulted   I=Informed

## Sistemi applicativi a supporto TO-BE (con eventuale Flow Chart)

“Descrizione della soluzione applicativa proposta e delle funzionalità limitatamente all’oggetto progettuale”

### Mappa applicativa TO-BE

Inserire l’immagine della mappa applicativa TO-BE

Allegare eventuale documento della mappa applicativa TO-BE

### Flussi integrazione TO-BE

Inserire elenco e descrizione dei flussi di integrazione TO-BE dal punto di vista funzionale che dovranno essere previsti dall’iniziativa, come da esempio di seguito riportato.

| Nome Processo | Id task | Cod. Flusso | Da (Applicazione) | A (Applicazione) | Flusso dati (funzionale) | Tipologia di Flusso |
| --- | --- | --- | --- | --- | --- | --- |
| Nome processo | Codice identificativo task | Codice identificativo flusso | Applicazione 1 | Applicazione 2 | Indicare informazioni e dati scambiati tra gli applicativi | Indicare tipologia integrazione (eventuale) |
| Nome processo | Codice identificativo task | Codice identificativo flusso | Applicazione 1 | Applicazione 2 | Indicare informazioni e dati scambiati tra gli applicativi | Indicare tipologia integrazione (eventuale) |
| Nome processo | Codice identificativo task | Codice identificativo flusso | Applicazione 1 | Applicazione 2 | Indicare informazioni e dati scambiati tra gli applicativi | Indicare tipologia integrazione (eventuale) |

Tabella 8 – Flussi funzionali di integrazione TO-BE

# REQUISITI FUNZIONALI DELL’INIZIATIVA

“Inserire una tabella riepilogativa di alto livello di tutti i Requisiti di tipo Funzionale derivanti dalla Tabella 4 “Matrice di tracciabilità requisiti” che dovranno costituire l’iniziativa ai fini del soddisfacimento dei requisiti utente.

In questo capitolo inserire solamente i requisiti funzionali; i requisiti di altra tipologia (come Sicurezza, Accessibilità, Formazione, ecc.ecc.) devono essere inseriti nel capitolo successivo.”

| Id Requisito Funzionale | Titolo Req. Funzionale | Descrizione Req. Funzionale | Priorità | Dipendenza da altri Req. Funzionali (ID)* | Sistema applicativo coinvolto |
| --- | --- | --- | --- | --- | --- |
| Identificativo univoco del requisito funzionale | Short label per identificazione del requisito funzionale | Descrizione d’alto livello del requisito funzionale | Inserire la priorità di implementazione frutto di valutazione del desiderata di business e della capacità realizzativa lato TSP (Alta, media, bassa) | Indicare se esistono collegamenti con altri requisiti funzionali | Indicare l’applicativo o gli applicativi / moduli coinvolti |

Tabella 9 – Requisiti funzionali dell’iniziativa

## Titolo Requisito Funzionale 1 (Id Requisito funzionale)

“Paragrafo dedicato ad uno specifico requisito funzionale.

Fornire in questa prima parte del paragrafo una descrizione di dettaglio del requisito funzionale, anche come descrizione dell’interazione tra il sistema (o sue componenti) e un attore, che dovrebbe trarre “vantaggio” dall’interazione stessa.

Possibilità di utilizzare anche tecniche di esplicitazione delle funzionalità (ad esempio Use Case Diagram, Data Flow Diagram, Sequence Diagram, ecc.)

Creare e compilare un paragrafo di questo tipo per ciascuno dei requisiti funzionali esposti in tabella 9 “Requisiti funzionali dell’iniziativa”.

### Gestione e formato dei dati, gestione eventuali errori

“In questo sotto-paragrafo, se necessario e applicabile (altrimenti inserire N/A) sono da descrivere:

Quali sono i dati che devono essere gestiti, da dove vengono presi, in che forma devono essere visualizzati,

Eventuale data validation e tassonomia se necessario

Eventuali criteri per la gestione degli errori (es.se è necessario prevedere appositi pop up di errore)

“

### Algoritmi di calcolo

“In questo sotto-paragrafo, se necessario e applicabile (altrimenti inserire N/A), inserire come i dati devono essere aggregati o calcolati, fornendo formule, criteri e algoritmi di business”

### Profondità storica dei dati

“In questo sotto-paragrafo, se necessario e applicabile (altrimenti inserire N/A),  fornire indicazioni sulla profondità storica dei dati che devono essere gestiti e/o visualizzati”

### Conformità alla normativa

“In questo sotto-paragrafo, se necessario e applicabile (altrimenti inserire N/A),  fornire indicazioni su come si intendono soddisfare le condizioni derivanti dagli obblighi di conformità alla legislazione in vigore o a normative di settore, applicabili alla soluzione richiesta.”

## Titolo Requisito Funzionale n (Id Requisito funzionale)

“Creare un nuovo Paragrafo, come il 4.1 dedicato al requisito funzionale n”.

# ULTERIORI REQUISITI NON FUNZIONALI DELL’INIZIATIVA

“Inserire una tabella riepilogativa di alto livello di tutti i Requisiti di tipo NON Funzionale derivanti dalla Tabella 4 “Matrice di tracciabilità requisiti” che dovranno costituire l’iniziativa ai fini del soddisfacimento dei requisiti utente in termini di caratteristiche dell’applicazione o altri aspetti.

In questo capitolo inserire i requisiti di altra tipologia rispetto alla Funzionale (come Sicurezza, Accessibilità, Formazione, ecc.ecc.), che dovranno poi essere descritti per singola tipologia nei paragrafi successivi del presente capitolo”.

| Id Requisito (non Funzionale) | Titolo Req. | Descrizione Req. | Priorità | Dipendenza da altri Req. (Funzionali e non )(ID)* | Sistema applicativo coinvolto |
| --- | --- | --- | --- | --- | --- |
| Identificativo univoco del requisito | Short label per identificazione del requisito | Descrizione d’alto livello del requisito | Inserire la priorità di implementazione frutto di valutazione del desiderata di business e della capacità realizzativa lato TSP (Alta, media, bassa) | Indicare se esistono collegamenti con altri requisiti funzionali e non | Indicare l’applicativo o gli applicativi / moduli coinvolti |

Tabella 10 – Ulteriori Requisiti non funzionali dell’iniziativa

## Identity Management, profilatura e gestione utenti

“Descrivere nel dettaglio tutti i requisiti non funzionali legati ad aspetti di gestione degli accessi, segregation of duties in termini di profilatura.

Descrivere quindi le funzionalità che consentono di stabilire e verificare l’identità dichiarata da un utente/processo che vuole accedere al sistema/applicazione.

Descrivere le funzionalità che garantiscono che un utente/processo possa effettuare le sole operazioni per le quali è abilitato, elaborando esclusivamente i dati di propria competenza.

Descrivere le funzionalità che assicurano la riservatezza, l’integrità e la disponibilità dei dati gestiti e/o scambiati via rete attraverso le differenti interfacce.”

## Privacy

“Descrivere i controlli di privacy sui dati trattati”

## Performance

“Descrivere come si intendono traguardare i requisiti di performance e di affidabilità richiesti”

## Formazione

“Descrivere come si intendono traguardare i requisiti formativi”

## Requisiti sulla gestione dei dati/archivi

“Descrivere come si intendono traguardare i requisiti in merito alla gestione dei dati/archivi anche in termini di trattamento privacy”

# INTERFACCIA UTENTE

“Descrivere la struttura dell’interfaccia applicativa, fornendo ad esempio mockup esemplificativi dell’applicativo da realizzarsi… (se applicabile)”

## Menu e historyboard

“Descrive un’ipotesi di albero di navigazione delle form che compongono l’interfaccia.” (se applicabile)

## Elenco form

“Inserire eventuali mockup delle form ipotizzate”

# EVENTUALI RISCHI

“Identificare i principali rischi coinvolti nel completamento del progetto e descrivere brevemente come possono essere gestiti”

| Identificazione del Rischio | Identificazione del Rischio | Identificazione del Rischio | Identificazione del Rischio | Valutazione del Rischio | Valutazione del Rischio | Gestione del Rischio | Gestione del Rischio | Gestione del Rischio |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| ID | Titolo | Tipologia | Descrizione | Impatto | Probabilità | Owner | Azioni e Status | Data di Risoluzione prevista |
| Inserire il codice univoco del rischio/criticità individuata | Inserire il titolo del rischio individuato | Inserire la tipologia di rischio associata (es. funzionale o tecnico) | Descrivere il rischio/criticità individuata | Indicare la portata dell’impatto qualora il rischio si presenti effettivamente | Valutare il rischio in base all’indice di probabilità (%) | Riportare la figura competente della gestione del rischio | Descrivere l’azione individuata per il contenimento del rischio | Riportare la data in cui si prevede la risoluzione del problema |
| Inserire il codice univoco del rischio/criticità individuata | Inserire il titolo del rischio individuato | Inserire la tipologia di rischio associata (es. funzionale o tecnico) | Descrivere il rischio/criticità individuata | 1 – Very Low | Valutare il rischio in base all’indice di probabilità (%) | Riportare la figura competente della gestione del rischio | Descrivere l’azione individuata per il contenimento del rischio | Riportare la data in cui si prevede la risoluzione del problema |
| Inserire il codice univoco del rischio/criticità individuata | Inserire il titolo del rischio individuato | Inserire la tipologia di rischio associata (es. funzionale o tecnico) | Descrivere il rischio/criticità individuata | 2 - Low | Valutare il rischio in base all’indice di probabilità (%) | Riportare la figura competente della gestione del rischio | Descrivere l’azione individuata per il contenimento del rischio | Riportare la data in cui si prevede la risoluzione del problema |
| Inserire il codice univoco del rischio/criticità individuata | Inserire il titolo del rischio individuato | Inserire la tipologia di rischio associata (es. funzionale o tecnico) | Descrivere il rischio/criticità individuata | 3 - Medium | Valutare il rischio in base all’indice di probabilità (%) | Riportare la figura competente della gestione del rischio | Descrivere l’azione individuata per il contenimento del rischio | Riportare la data in cui si prevede la risoluzione del problema |
| Inserire il codice univoco del rischio/criticità individuata | Inserire il titolo del rischio individuato | Inserire la tipologia di rischio associata (es. funzionale o tecnico) | Descrivere il rischio/criticità individuata | 4 - High | Valutare il rischio in base all’indice di probabilità (%) | Riportare la figura competente della gestione del rischio | Descrivere l’azione individuata per il contenimento del rischio | Riportare la data in cui si prevede la risoluzione del problema |
| Inserire il codice univoco del rischio/criticità individuata | Inserire il titolo del rischio individuato | Inserire la tipologia di rischio associata (es. funzionale o tecnico) | Descrivere il rischio/criticità individuata | 5 - Critical | Valutare il rischio in base all’indice di probabilità (%) | Riportare la figura competente della gestione del rischio | Descrivere l’azione individuata per il contenimento del rischio | Riportare la data in cui si prevede la risoluzione del problema |

Tabella 11 – Valutazione rischi/criticità