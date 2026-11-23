| ANALISI REQUISITI UTENTE<br>Bonifica stati di quarto livello EAM |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Marco Lerro | 05/08/2026 | Prima emissione |
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

Attività e deliverables	9

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

Nell’ambito del sistema di Asset Management (EAM), l’utente richiede la bonifica degli stati di quarto livello non consistenti con lo stato del corrispondente secondo livello. In dettaglio, sono stati rilevati alla data di scrittura del presente documento i seguenti stati inconsistenti per un totale di 1.443.364 asset di quarto livello.

| STATO LIVELLO 2 | STATO LIVELLO 4 | STATO LIVELLO 4 ATTESO | ASSET CON STATO INCONSISTENTE |
| --- | --- | --- | --- |
| ATTV | DATT | ESER | 1410869 |
| CEAL | DATT | DIST | 17 |
| DEMO | DATT | DIST | 2198 |
| DEMO | ESER | DIST | 850 |
| NADI | DATT | ESER | 296 |
| NAND | DATT | ESER | 29134 |

Sebbene la bonifica si renda necessaria per la totalità degli asset, si riporta il dettaglio delle inconsistenze escludendo gli asset relativi alle opere interferenti con l’obiettivo di indirizzare la bonifica in prima battuta sugli asset critici per il rilevamento di difformità e interferenze. Pertanto, escludendo gli asset classificati come OPERA INTERFERENTE AEREA e OPERA INTERFERENTE CAVO sono state rilevate le seguenti 328.503 inconsistenze, che rappresentano circa il 23% della totalità.

| STATO LIVELLO 2 | STATO LIVELLO 4 | STATO LIVELLO 4 ATTESO | ASSET CON STATO INCONSISTENTE |
| --- | --- | --- | --- |
| ATTV | DATT | ESER | 321483 |
| CEAL | DATT | DIST | 16 |
| DEMO | DATT | DIST | 346 |
| DEMO | ESER | DIST | 824 |
| NADI | DATT | ESER | 45 |
| NAND | DATT | ESER | 5789 |

# Descrizione della soluzione

La bonifica, vista la mole di asset da bonificare, verrà condotta inizialmente per gli asset di quarto livello escludendo gli asset classificati come OPERA INTERFERENTE AEREA e OPERA INTERFERENTE CAVO. In una seconda fase saranno indirizzati anche questi asset.

Data la mole di dati, la soluzione prevederà un automatismo (automation script) che effettuerà i cambi di stato degli asset di quarto livello raggruppando le operazioni per singola linea con l’obiettivo di bonificare gradualmente le linee. L’automatismo identificherà gli asset da bonificare per la linea ed effettuerà il cambio stato per ogni singolo asset secondo le modalità standard di prodotto al fine di innescare tutti i flussi di integrazione, gli automatismi in essere, tracciamento dello storico, e quant’altro.

Si assume che il cambio stato venga gestito correttamente dai sistemi a valle che saranno notificati dal cambiamento di stato. La soluzione garantirà l’utilizzo, dal punto di vista applicativo, della stessa funzionalità di cambio stato presente nella applicazione Linee ed esercitabile dagli utenti. Pertanto, assumendo che i sistemi a valle siano in grado di recepire e gestire il cambio stato innescato da applicazione Linee, la soluzione replicherà lo stesso comportamento.

Ogni cambio stato verrà finalizzato indipendentemente ed un eventuale fallimento non bloccherà il cambio di stato degli asset successivi della linea. Non sono previste operazioni di roll-back del cambiamento stato.

L’automatismo prevederà un meccanismo di esecuzione parallelo per elaborare più linee contemporaneamente. Il livello di parallelismo sarà configurabile. Il livello di parallelismo iniziale sarà identificato in fase di test ed eventualmente corretto in produzione in base ai tempi di riposta rilevati.

L’avvio dell’automatismo di bonifica sarà soggetto a programmazione in una fascia oraria che verrà identificata come non invasiva rispetto alle attività utente e garantirà il termine della esecuzione entro un numero di ore configurabili. Se l’esecuzione dell’automatismo sforerà il numero massimo di ore, non verranno avviate le elaborazioni di altre linee, tuttavia, si porteranno a termine le linee in corso di elaborazione. La procedura valuterà il tempo medio di bonifica di ogni singola linea consentendo l’avvio solo nel caso si valuti possa rientrare nello slot temporale.

Nell’ambito della bonifica di una singola linea, verranno inizialmente bonificati gli asset relativi alle classificazioni di asset non relazionati (ad esempio CONDUTTORE) ed a seguire gli asset relazionati (ad esempio GIUNTO CONDUTTORE). Poiché i cambi di stato saranno atomici per ogni asset, il cambio stato dell’asset non relazionato non implica il cambio stato degli asset relazionati che potenzialmente potrebbe non andare a buon fine. Allo stesso modo, il fallimento del cambio stato di un asset non relazionato non bloccherà il tentativo di cambio stato dei suoi asset relazionati. Questo approccio mira a portare avanti quanto più possibile la bonifica. Si riportano le numeriche delle inconsistenza per singola classificazione degli asset di quarto livello.

| Secondo livello | Stato secondo livello | Quarto livello | Stato quarto livello | Disallineamenti | Stato atteso |
| --- | --- | --- | --- | --- | --- |
| CAMPATA AEREA | ATTV | CONDUTTORE | DATT | 26688 | ESER |
| CAMPATA AEREA | ATTV | ANTIROTAZIONALI CONDUTTORE | DATT | 18079 | ESER |
| CAMPATA AEREA | ATTV | ANTIVIBRANTI CONDUTTORE | DATT | 10246 | ESER |
| CAMPATA AEREA | ATTV | ANTIVIBRANTI FUNE DI GUARDIA | DATT | 4642 | ESER |
| CAMPATA AEREA | ATTV | GIUNTO CONDUTTORE | DATT | 4373 | ESER |
| CAMPATA AEREA | ATTV | FUNE DI GUARDIA | DATT | 2407 | ESER |
| CAMPATA AEREA | ATTV | SFERE DI SEGNALAZIONE FUNE DI GUARDIA | DATT | 1060 | ESER |
| CAMPATA AEREA | ATTV | DISTANZIATORI | DATT | 659 | ESER |
| CAMPATA AEREA | ATTV | GIUNTO FUNE DI GUARDIA | DATT | 242 | ESER |
| CAMPATA AEREA | ATTV | BARRETTA PREFORMATA CONDUTTORE | DATT | 113 | ESER |
| CAMPATA AEREA | ATTV | ANTIROTAZIONALI FUNE DI GUARDIA | DATT | 76 | ESER |
| CAMPATA AEREA | ATTV | WRAPPED CONDUTTORE | DATT | 67 | ESER |
| CAMPATA AEREA | ATTV | DISSUASORI AVIFAUNA FUNE DI GUARDIA | DATT | 15 | ESER |
| CAMPATA AEREA | ATTV | DISTANZIATORI INTERFASE | DATT | 12 | ESER |
| CAMPATA AEREA | ATTV | WRAPPED FDG | DATT | 9 | ESER |
| CAMPATA AEREA | ATTV | BARRETTA PREFORMATA FUNE DI GUARDIA | DATT | 9 | ESER |
| CAMPATA AEREA | ATTV | SFERE DI SEGNALAZIONE CONDUTTORE | DATT | 8 | ESER |
| CAMPATA AEREA | DEMO | CONDUTTORE | ESER | 225 | DIST |
| CAMPATA AEREA | DEMO | ANTIVIBRANTI CONDUTTORE | ESER | 105 | DIST |
| CAMPATA AEREA | DEMO | FUNE DI GUARDIA | ESER | 77 | DIST |
| CAMPATA AEREA | DEMO | ANTIVIBRANTI FUNE DI GUARDIA | ESER | 41 | DIST |
| CAMPATA AEREA | DEMO | CONDUTTORE | DATT | 24 | DIST |
| CAMPATA AEREA | DEMO | GIUNTO CONDUTTORE | ESER | 13 | DIST |
| CAMPATA AEREA | DEMO | ANTIROTAZIONALI CONDUTTORE | DATT | 8 | DIST |
| CAMPATA AEREA | DEMO | SFERE DI SEGNALAZIONE FUNE DI GUARDIA | ESER | 7 | DIST |
| CAMPATA AEREA | DEMO | FUNE DI GUARDIA | DATT | 4 | DIST |
| CAMPATA AEREA | DEMO | WRAPPED CONDUTTORE | ESER | 3 | DIST |
| CAMPATA AEREA | DEMO | ANTIVIBRANTI CONDUTTORE | DATT | 3 | DIST |
| CAMPATA AEREA | DEMO | GIUNTO FUNE DI GUARDIA | ESER | 1 | DIST |
| CAMPATA AEREA | DEMO | ANTIVIBRANTI FUNE DI GUARDIA | DATT | 1 | DIST |
| CAMPATA AEREA | DEMO | ANTIROTAZIONALI CONDUTTORE | ESER | 1 | DIST |
| CAMPATA AEREA | DEMO | ANTIROTAZIONALI FUNE DI GUARDIA | DATT | 1 | DIST |
| CAMPATA AEREA | DEMO | ANTIROTAZIONALI FUNE DI GUARDIA | ESER | 1 | DIST |
| CAMPATA AEREA | NADI | CONDUTTORE | DATT | 3 | ESER |
| CAMPATA AEREA | NADI | SFERE DI SEGNALAZIONE FUNE DI GUARDIA | DATT | 1 | ESER |
| CAMPATA AEREA | NADI | FUNE DI GUARDIA | DATT | 1 | ESER |
| CAMPATA AEREA | NAND | CONDUTTORE | DATT | 150 | ESER |
| CAMPATA AEREA | NAND | ANTIVIBRANTI CONDUTTORE | DATT | 60 | ESER |
| CAMPATA AEREA | NAND | GIUNTO CONDUTTORE | DATT | 56 | ESER |
| CAMPATA AEREA | NAND | ANTIROTAZIONALI CONDUTTORE | DATT | 30 | ESER |
| CAMPATA AEREA | NAND | FUNE DI GUARDIA | DATT | 18 | ESER |
| CAMPATA AEREA | NAND | SFERE DI SEGNALAZIONE FUNE DI GUARDIA | DATT | 9 | ESER |
| CAMPATA AEREA | NAND | GIUNTO FUNE DI GUARDIA | DATT | 9 | ESER |
| CAMPATA AEREA | NAND | ANTIVIBRANTI FUNE DI GUARDIA | DATT | 4 | ESER |
| ESTREMO CAVO | ATTV | GIUNTO CAVO | DATT | 484 | ESER |
| ESTREMO CAVO | ATTV | TERMINALE | DATT | 293 | ESER |
| ESTREMO CAVO | ATTV | CAMERA ISPEZIONABILE | DATT | 74 | ESER |
| ESTREMO CAVO | ATTV | CASSETTA DI SEZIONAMENTO TERMINALI | DATT | 23 | ESER |
| ESTREMO CAVO | ATTV | CASSETTA DI SEZIONAMENTO GIUNTI | DATT | 22 | ESER |
| ESTREMO CAVO | ATTV | SCARICATORE | DATT | 6 | ESER |
| ESTREMO CAVO | CEAL | TERMINALE | DATT | 3 | DIST |
| ESTREMO CAVO | DEMO | TERMINALE | ESER | 12 | DIST |
| ESTREMO CAVO | DEMO | GIUNTO CAVO | ESER | 3 | DIST |
| ESTREMO CAVO | NAND | GIUNTO CAVO | DATT | 18 | ESER |
| ESTREMO CAVO | NAND | TERMINALE | DATT | 3 | ESER |
| SOSTEGNO AEREO | ATTV | ARMAMENTO FUNE DI GUARDIA | DATT | 194753 | ESER |
| SOSTEGNO AEREO | ATTV | ISOLATORE CONDUTTORE | DATT | 27196 | ESER |
| SOSTEGNO AEREO | ATTV | ARMAMENTO CONDUTTORE | DATT | 26813 | ESER |
| SOSTEGNO AEREO | ATTV | GIUNZIONE FIBRA OTTICA | DATT | 1598 | ESER |
| SOSTEGNO AEREO | ATTV | STRUTTURA | DATT | 479 | ESER |
| SOSTEGNO AEREO | ATTV | MONITORAGGIO IOT (DIGIL) | DATT | 337 | ESER |
| SOSTEGNO AEREO | ATTV | ISOLATORE FUNE DI GUARDIA | DATT | 62 | ESER |
| SOSTEGNO AEREO | ATTV | SEZIONATORE | DATT | 48 | ESER |
| SOSTEGNO AEREO | ATTV | NIDO AVIFAUNA | DATT | 8 | ESER |
| SOSTEGNO AEREO | ATTV | SENSORI DI TIRO | DATT | 2 | ESER |
| SOSTEGNO AEREO | ATTV | DISPOSITIVO TLC | DATT | 1 | ESER |
| SOSTEGNO AEREO | ATTV | BOBINA DI SBARRAMENTO OCV | DATT | 1 | ESER |
| SOSTEGNO AEREO | CEAL | ISOLATORE CONDUTTORE | DATT | 3 | DIST |
| SOSTEGNO AEREO | CEAL | ARMAMENTO CONDUTTORE | DATT | 3 | DIST |
| SOSTEGNO AEREO | CEAL | ARMAMENTO FUNE DI GUARDIA | DATT | 2 | DIST |
| SOSTEGNO AEREO | CEAL | STRUTTURA | DATT | 1 | DIST |
| SOSTEGNO AEREO | DEMO | ARMAMENTO FUNE DI GUARDIA | DATT | 239 | DIST |
| SOSTEGNO AEREO | DEMO | ARMAMENTO CONDUTTORE | ESER | 126 | DIST |
| SOSTEGNO AEREO | DEMO | ISOLATORE CONDUTTORE | ESER | 123 | DIST |
| SOSTEGNO AEREO | DEMO | STRUTTURA | ESER | 39 | DIST |
| SOSTEGNO AEREO | DEMO | ARMAMENTO CONDUTTORE | DATT | 15 | DIST |
| SOSTEGNO AEREO | DEMO | ISOLATORE CONDUTTORE | DATT | 15 | DIST |
| SOSTEGNO AEREO | DEMO | ARMAMENTO FUNE DI GUARDIA | ESER | 8 | DIST |
| SOSTEGNO AEREO | DEMO | GIUNZIONE FIBRA OTTICA | ESER | 6 | DIST |
| SOSTEGNO AEREO | DEMO | STRUTTURA | DATT | 4 | DIST |
| SOSTEGNO AEREO | NADI | ARMAMENTO FUNE DI GUARDIA | DATT | 33 | ESER |
| SOSTEGNO AEREO | NADI | ARMAMENTO CONDUTTORE | DATT | 3 | ESER |
| SOSTEGNO AEREO | NADI | ISOLATORE CONDUTTORE | DATT | 3 | ESER |
| SOSTEGNO AEREO | NADI | STRUTTURA | DATT | 1 | ESER |
| SOSTEGNO AEREO | NAND | ARMAMENTO FUNE DI GUARDIA | DATT | 5086 | ESER |
| SOSTEGNO AEREO | NAND | ARMAMENTO CONDUTTORE | DATT | 159 | ESER |
| SOSTEGNO AEREO | NAND | ISOLATORE CONDUTTORE | DATT | 158 | ESER |
| SOSTEGNO AEREO | NAND | STRUTTURA | DATT | 3 | ESER |
| SOSTEGNO AEREO | NAND | SEZIONATORE | DATT | 1 | ESER |
| SOSTEGNO AEREO | NAND | GIUNZIONE FIBRA OTTICA | DATT | 1 | ESER |
| TRATTA CAVO | ATTV | CAVO DI POTENZA | DATT | 620 | ESER |
| TRATTA CAVO | ATTV | SEGNALAZIONE CAVO X | DATT | 492 | ESER |
| TRATTA CAVO | ATTV | GIUNTO DI RIPARAZIONE | DATT | 9 | ESER |
| TRATTA CAVO | ATTV | FIBRA OTTICA CAVO | DATT | 6 | ESER |
| TRATTA CAVO | CEAL | CAVO DI POTENZA | DATT | 3 | DIST |
| TRATTA CAVO | CEAL | SEGNALAZIONE CAVO X | DATT | 1 | DIST |
| TRATTA CAVO | DEMO | CAVO DI POTENZA | DATT | 24 | DIST |
| TRATTA CAVO | DEMO | CAVO DI POTENZA | ESER | 21 | DIST |
| TRATTA CAVO | DEMO | SEGNALAZIONE CAVO X | DATT | 8 | DIST |
| TRATTA CAVO | DEMO | SEGNALAZIONE CAVO X | ESER | 1 | DIST |
| TRATTA CAVO | NAND | CAVO DI POTENZA | DATT | 18 | ESER |
| TRATTA CAVO | NAND | SEGNALAZIONE CAVO X | DATT | 6 | ESER |

L’esito della attività di bonifica verrà tracciata in una tabella dedicata che fornirà i dettagli di bonifica per ogni livello di linea quali:

Dati identificativi della linea (numero asset e SAP ID)

Data inizio bonifica

Data fine bonifica

Numero di asset da bonificare

Numero di asset bonificati con successo

Numero di asset per cui la bonifica è fallita

Campo note con le motivazioni del fallimento del cambio stato per tutti gli asset falliti

Eventuali fallimenti saranno rilevati mediante questa tabella e la loro risoluzione sarà oggetto di analisi di AMS a meno che l’errore non sia imputabile ad un malfunzionamento della procedura. La funzionalità di bonifica sarà in esecuzione per effettuare la scansione di tutte le linee per una volta e non ripeterà l’elaborazione se non dietro richiesta e riconfigurazione. Eventuali riesecuzioni valuteranno solo gli asset in quel momento inconsistenti (dovuti ad un fallimento nella esecuzione precedente o al sopraggiungere di nuove inconsistenze) tentando effettuare nuovamente il cambio stato.

## Attività e deliverables

Nella ipotesi di riuscire a programmare un volume circa 25.000 bonifiche al giorno, si stima di completare tutte le bonifiche (1.443.364) in circa 60 giorni. La prima parte di bonifica (328.503) dovrebbe impiegare circa 15 giorni. Il volume indicato è da confermare e non dipende esclusivamente dalla realizzazione della soluzione ma dai tempi di risposta del sistema e dalle limitazioni di carico indotte o richieste dai sistemi a valle. In ogni caso, la stima di supporto post go-live (inteso come monitoraggio e raccolta metriche settimanali) è di un massimo di 3 mesi.

La RITM prevede le seguenti attività:

Definizione dello scope della bonifica (asset di quarto livello soggetti a bonifica) e verifica dello stesso con business.

Realizzazione della soluzione automatica di bonifica che permetterà la pianificazione delle esecuzioni in più giorni della bonifica di un lotto di linee in base ad una durata giornaliera configurabile. La soluzione permetterà di configurare il parallelismo della bonifica su più linee contemporaneamente. Inoltre, traccerà gli avanzamenti delle bonifiche per ogni linea. La soluzione tenterà di effettuare tutte le bonifiche prescindendo da eventuali situazioni di errore che verranno successivamente analizzate tramite AMS (si sottolinea che vista la natura massiva potrebbe generarsi un carico fuori media verso il team AMS wave 1 non indirizzato da questa RITM).

Collaudo della soluzione (SIT) mirato a verificare il funzionamento della procedura di bonifica contemplando eventuali fallimenti di cambio stato non imputabili alla procedura bensì allo stato della anagrafica. Questi casi non saranno considerati come bug della soluzione. Gli obiettivi del collaudo saranno di verificare l’avvenuto cambio stato supponendo che i sistemi a valle gestiranno correttamente il cambio stato come già previsto. Non saranno verificati i sistemi a valle.

Misurazione delle prestazioni in ambiente di collaudo al fine di determinare il tuning di partenza da adottare in produzione, che poi potrà essere corretto in base ai tempi di risposta monitorati.

Collaudo della soluzione (UAT) per verificare con gli utenti di business il funzionamento della procedura di bonifica contemplando eventuali fallimenti di cambio stato non imputabili alla procedura bensì allo stato della anagrafica.  Questi casi non saranno considerati come bug della soluzione. Gli obiettivi del collaudo saranno di verificare l’avvenuto cambio stato supponendo che i sistemi a valle gestiranno correttamente il cambio stato come già previsto. Non saranno verificati i sistemi a valle.

Rilascio e avvio della soluzione in produzione.

Monitoraggio dell’avanzamento della bonifica su base settimanale con aggiornamento della previsione di fine bonifica.

Riconfigurazione della soluzione per la gestione della bonifica delle opere interferenti.

La RITM prevede i seguenti deliverables:

Piano in formato Gantt dell’attività

Analisi funzionale

Test-book

Pacchetti e nota di rilascio della soluzione

Report settimanale di avanzamento delle bonifiche per un massimo di 3 mesi.