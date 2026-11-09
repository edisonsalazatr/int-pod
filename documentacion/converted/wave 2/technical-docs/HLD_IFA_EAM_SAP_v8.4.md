# HLD_IFA_EAM_SAP_v8.4

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento | Analisi Tecnica – Interface Agreement EAM vs SAP |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data | Owner |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | ACN |  |  |
| Riesaminato da: | ACN | 12/07/2024 |  |
| Approvato da: | TERNA | GG/MM/AAAA |  |

# Glossario

| Termine/Acronimo | Definizione | Definizione |
| --- | --- | --- |
| Piano di Lavoro | Oggetto MAS utilizzato per rappresentare il catalogo delle attività |  |
| Ordine di Lavoro | Oggetto MAS utilizzato per rappresentare e gestire i lavori da eseguire |  |
| Job Task | Oggetto MAS per rappresentare sia i metodi, sia i micro-controlli associati a un PdL |  |
| OdM | Ordine di manutenzione | Ordine di manutenzione |
| NTK/NTW | Network | Network |
| RE | Risorse esterne | Risorse esterne |

# Obiettivi del documento

Nell’ambito del progetto di implementazione della nuova piattaforma IBM Maximo e nello specifico in vista del live di wave2 che prevede la gestione dei processi di Asset Management su EAM, il documento ha lo scopo di descrivere funzionalmente i flussi d’integrazione tra EAM-SAP e di riportare per ogni flusso:

Descrizione funzionale

Logiche d’attivazione

Dati scambiati

qqrazione

# Flussi d’integrazione e logica d’attivazione

Questa sezione si prefigge di descrivere i flussi che intercorrono tra EAM e il sistema SAP e le relative interazioni.

Di seguito diagramma di chiamate tra i sistemi EAM e SAP:

| Id | Nome flusso | Descrizione |
| --- | --- | --- |
| 1 | RichiestaCreazioneOdM | Flusso per la creazione di un ordine di manutenzione in SAP |
| 2 | RichiestaCreazioneNTW | Flusso per la creazione di una network in SAP |
| 3 | InvioConsuntivi | Flusso per l’invio consuntivi a SAP |
| 4 | Storno | Flusso per la comunicazione dello storno parziale o totale delle ore a SAP |
| 5 | AcquisizioneCBS/CBE | Flusso in ingresso verso SAP per l'acquisizione di WBE e WBS in EAM. |
| 6 | AcquisizioneOI | Flusso in ingresso verso SAP per l'acquisizione di Ordini Interni in EAM. |
| 7 | AcquisizioneOdM | Flusso in ingresso verso SAP per l'acquisizione di Ordini di manutenzione in EAM. |
| 8 | AcquisizioneNTW | Flusso in ingresso verso SAP per l'acquisizione di network in EAM. |
| 9 | DisassociazioneOdM | Disassociazione di OdM all'OdL corrente. |
| 10 | DisassociazioneNTW | Disassociazione di NTW all'OdL corrente. |
|  | ControlloPeriodoContabile | Interfaccia che deve verificare in SAP l’apertura o meno del periodo contabile. |
|  |  |  |
|  |  |  |
| 14 | ChiusuraOdM | Flusso in ingresso verso SAP per la cancellazione/chiusura degli OdM |
| 15 | ChiusuraNTW | Flusso in ingresso verso SAP per la cancellazione/chiusura delle NTW |
| 16 | RichiestaConsuntivazioneCallback | Flusso in ingresso verso EAM per la comunicazione dell’esito del flusso InvioConsuntivi |

## Flusso RichiestaCreazioneOdM – Perimetro release 2

### Descrizione funzionale interfaccia

Questa interfaccia svolge la funzione di creazione in SAP di un OdM nel caso in cui l’OdM da associare all’OdL non sia già stato definito in SAP (e quindi acquisito tramite interfaccia massiva).

La funzionalità di creazione di OdM viene innescata manualmente tramite il tasto “Richiesta creazione in SAP”, il quale risulta cliccabile solo se il campo identificativo OdM è vuoto.

Se la generazione va a buon fine, viene visualizzato automaticamente in EAM il codice dell’OdM generato nel campo “Identificativo OdM”.

### Logiche d’attivazione

La funzionalità di creazione di OdM di questa interfaccia viene innescata manualmente tramite il tasto “Richiesta creazione in SAP”, il quale risulta cliccabile solo se il campo identificativo OdM è vuoto.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | Circa 25.000 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| <OdMMaintain> |  | NA |  |
| Istanza | Id riguardante l'attività del centro operativo (nel TO-BE sarà popolato con l’ID EAM) | Y |  |
| OdM | Identificativo univoco creato da SAP dell'oggetto contabile OdM | N |  |
|  |  |  |  |
| InizioCardine | Date pianificate di Inizio attività | Y |  |
| FineCardine | Date pianificate di fine attività | Y |  |
| priorita | Priorità impostata dal sistema chiamate | Y | Costante |
| TipoAttivitaPM | Tipo attività inviata da sistema chiamate | Y |  |
| TipoOrdine | Tipo ordine inviato da sistema chiamante | Y |  |
| Wbs | Campo inviato per l’OdM e coincidente con la CBS di pianificazione lato EAM | Y |  |
| SedeTecnica | SAP ID dell'asset | Y |  |
| CdlResponsabile | Viene trascodificato in SAP e impostato nella testata ordine | Y |  |
| <ScaricoCosti> |  | NA |  |
| OrdineInterno | N. Ordine interno per cui creare OdM | N |  |
| WBS | WBS | N |  |
| </ScaricoCosti> |  | NA |  |
| <Operazioni> |  | NA |  |
| DataTarget | IN SAP non viene utilizzata | N | Sempre blank |
| Durata | Durata pianificata/prevista per l'operazione | Y |  |
| <Personale> | Nodo del personale | NA |  |
|  |  | Y |  |
| OreLavoro | Ore di lavoro pianificate per l'intervento | Y |  |
| OreTrasferimento | Tempo di trasferimento dell'ordine | Y |  |
| </Personale> | - | NA |  |
| </Operazioni> | - | NA |  |
| </OdMMaintain> |  | NA |  |
| Trtyp | Costante del messaggio. Pari ad H in caso di creazione, V in caso di modifica di un record esistente. | Y |  |

## Flusso RichiestaCreazioneNTW – Perimetro release 2

### Descrizione funzionale interfaccia

Questa interfaccia svolge la funzione di creazione in SAP di un NTW nel caso in cui la NTW da associare all’OdL non sia già stata definita in SAP (e quindi acquisita tramite interfaccia massiva).

La funzionalità di creazione di NTW viene innescata manualmente tramite il tasto “Richiesta creazione in SAP”, il quale risulta cliccabile solo se il campo identificativo NTW è vuoto. Se la generazione va a buon fine, viene visualizzato automaticamente in EAM il codice della NTW generato nel campo “Identificativo NTW (ed operazione)”.

### Logiche d’attivazione

La funzionalità di creazione di NTW viene innescata manualmente tramite il tasto “Richiesta creazione in SAP”, il quale risulta cliccabile solo se il campo identificativo NTW è vuoto.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | Circa 12.000 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| <NtwMaintain> |  | NA |  |
| TipoNTW | Chiave univoca al fine di identificare il tipo network di EAM rispetto ad altri | Y |  |
| Network | Codice numerico creato da SAP relativo alla network | N |  |
| Wbs | Attributo fondamentale per le operazioni di network | Y |  |
| Descrizione | Descrizione di tipo statico stabilita da EAM | Y |  |
| CentroLavoro | Codice centro di lavoro | Y |  |
| <Operazioni> | Oggetto che comprende informazioni riguardo il centro di lavoro e il ciclo di lavoro | NA |  |
| Istanza | Id riguardante l'attività del centro operativo | Y |  |
| Operazione | ID dell’operazione | Y |  |
| IdAttivita | Id riguardante l'attività del PdL | Y | (es. 1.15.1) |
|  |  |  |  |
| InizioCardine | Date pianificate di Inizio attività | TBD |  |
| FineCardine | Date pianificate di fine attività | TBD |  |
| DataTarget | IN SAP non viene utilizzata | TBD | Sempre blank |
| Durata | Durata pianificata/prevista per l'operazione | TBD |  |
| <Personale> |  | NA |  |
| Matricola | Codice Matricola per controllo su tabelle custom, utenza di chi fa richiesta per creazione | Y |  |
|  |  |  |  |
| OreLavoro | Ore di lavoro pianificate per l'intervento | Y |  |
| OreTrasferimento | Tempo di trasferimento dell'ordine | Y |  |
| </Personale> |  | NA |  |
| </Operazioni> |  | NA |  |
| <NtwMaintain> |  | NA |  |
| Trtyp | Costante del messaggio. Pari ad H in caso di creazione, V in caso di modifica di un record esistente. | Y |  |

## Flusso InvioConsuntivi – Perimetro release 2

### Descrizione funzionale interfaccia

Nell’immagine sottostante (Figura 10) viene riportata la sezione del ciclo di vita dell’OdL che implica un’integrazione con SAP. In particolare, vengono definite le interfacce EAM-SAP di invio consuntivi ore

Figura - Integrazioni con SAP

| Tipo spesa OdL | Oggetti contabili | Dati tecnici (per ogni matricola) |
| --- | --- | --- |
| CAPEX | Identificativo NTK Numero operazione | Matricola risorsa Data di esecuzione Ore esecuzione Ore trasferimento |
| OPEX | Identificativo OdM | Matricola risorsa Data di esecuzione Ore esecuzione Ore trasferimento Centro di lavoro |
| ANR | Identificativo OdM | Matricola risorsa Data di esecuzione Ore esecuzione Ore trasferimento |

### Logiche d’attivazione

L’invio sarà eseguibile direttamente da EAM tramite l’apposita funzionalità nell’applicazione “consuntivazione risorse”. L’attivazione del flusso sarà dunque manuale e su base dell’input dell’utente.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | Circa 40.000 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Asincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| <ConfermaOre> | Nodo contenente... | Y |  |
| Istanza | Id riguardante l'attività del centro operativo | Y |  |
| Network | Identificativo Univoco Ntw di Sap se presente | Y | Obbligatorio nel caso in cui l’OdM non sia popolato |
| Operazione | Valorizzato nel caso passato il tipo network | Y | Obbligatorio nel caso in cui l’OdM non sia popolato |
| Odm | Identificativo Univoco ODM di Sap se presente | Y | Obbligatorio nel caso in cui la NTK non sia popolata |
| Approvatore | Matricola Terna Approvatore (che effettua l’invio) | Y |  |
| <Wrktime> | Inizio nodo | NA |  |
| Matricola | Codice Matricola consuntivata | Y |  |
| OreLavoro | Ore di lavoro | Y |  |
|  |  |  |  |
|  |  |  |  |
| DataRegistrazione | Data del consuntivo | Y |  |
| EAMKey | Chiave EAM | Y |  |
| </Wrktime> |  | NA |  |
| </ConfermaOre> |  | NA |  |
| Processo | OPEX, CAPEX, ANR | Y |  |

## Flusso CounterOdM – Perimetro release 2

### Descrizione funzionale interfaccia

Per poter comunicare uno storno parziale in SAP è necessario individuare l’identificativo univoco della riga di consuntivo caricata sull’OdM. Questo servizio esposto da SAP servirà dunque per ricavare il counter associato ad un consuntivo e potere abilitare EAM a procedere con la richiesta di storno effettiva.

### Logiche d’attivazione

ll flusso di integrazione CounterOdM verrà triggerato manualmente da parte dell’utente. Tramite UI potrà richiamare la funzione di storno di uno specifico consuntivo (caso di storno parziale). Questo flusso d’integrazione verrà evocato per poter abilitare ed evocare automaticamente il secondo flusso dedicato allo storno.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 100 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| <ZmosWsResearchCounterOdm> | Inizio request | NA |  |
| Aufnr | Numero Ordine di manutenzione SAP | Y |  |
| Zzinstance | ID Istanza (ODL) | Y |  |
| Matricola | Codice Matricola consuntivata | Y |  |
| Isdd | Data inizio effettiva (formato YYYYMMDD) | Y |  |
| Iedd | Data fine effettiva (formato YYYYMMDD) | Y |  |
| </ ZmosWsResearchCounterOdm > | Fine request |  |  |
| Response | Response | Response | Response |
| <Bapiret2> | Contiene l’esito della richiesta |  |  |
| Type | Esito | Y |  |
| Id | NA |  |  |
| Number | NA |  |  |
| Message | Messaggio contenente l’esito | Y |  |
| LogNo |  | N |  |
| LogMsgNo | Contiene in risposta l’odm inviato in request se esito positivo | Y |  |
| MessageV1 | Contiene in risposta l’instanza inviata in request se esito positivo | Y |  |
| MessageV2 | NA |  |  |
| MessageV3 | NA |  |  |
| Messagev4 | NA |  |  |
| Parameter | NA |  |  |
| Row | NA |  |  |
| Field | NA |  |  |
| System | NA |  |  |
| </Bapiret2> |  |  |  |
| OData | Contiene i contatori delle conferme |  |  |
| IAufnr | Numero Ordine di manutenzione SAP | N |  |
| IIedd | Data inizio effettiva (formato YYYYMMDD) | N |  |
| IIsdd | Data fine effettiva (formato YYYYMMDD) | N |  |
| IMatricola | Codice Matricola consuntivata | N |  |
| IZzinstance | ID Istanza (ODL) | N |  |
| Rueck | Numero di conferma | N |  |
| Rmzhl | Contatore progressivo | N |  |

## Flusso Storno – Perimetro release 1

### Descrizione funzionale interfaccia

Nella applicazione di Maximo Web, l’utente ha la possibilità di stornare delle ore (in modo parziale o totale) e comunicare lo storno a SAP.

Nel dettaglio, può essere comunicato mediante apposita sezione a schermo lo storno di una riga di consuntivo oppure lo storno di tutto il consuntivo di un ordine di lavoro (che era stato precedentemente inviato a SAP).

### Logiche d’attivazione

Il flusso di integrazione dedicato allo storno delle ore vs SAP verrà triggerato manualmente da parte dell’utente. Tramite UI potrà richiamare la funzione di storno da applicare ad una singola riga di consuntivo (nel caso di storno parziale); oppure mediante azione laterale anche lo storno di tutte le righe di consuntivo registrate ed inviate verso SAP di un ordine di lavoro (nel caso di storno totale). Se la richiesta di storno proviene da Geocall, il flusso di integrazione (a partire dal counter OdM) verrà automaticamente triggerato.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 100 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| IAufnr | Numero Ordine SAP | Y |  |
| <IDatiConferme> | Inizio nodo... | NA |  |
| Rueck | Numero di conferma | N |  |
| Rmzhl | Contatore progressivo | N |  |
| </IDatiConferme> |  | NA |  |
| IModEsecuzione | Modalità esecuzione storno (se simulazione o reale) | Y |  |
| ITpStorno | Tipologia storno (totale o parziale) | Y |  |
| Izzinstance | Identificativo ordine di lavoro (istanza MAKE) | Y |  |

## Flusso AcquisizioneCBS/CBE – Perimetro release 1

### Descrizione funzionale interfaccia

La valorizzazione delle informazioni relative agli oggetti contabili avviene tramite l’interfaccia di acquisizione massiva da SAP.

WBS / WBE: L'oggetto è acquisito su EAM a seguito dell’evocazione periodica del servizio esposto da SAP.

Le logiche principali di acquisizione sono:

CAPEX: Filtra le WBS / WBE valide che hanno profit center che termina con i caratteri "LIN";

ANR: Filtra le WBS / WBE contabili valide che hanno profit center che termina con “EPC”.

### Logiche d’attivazione

L’interfaccia verrà attivata in modo automatico periodicamente – 1 volta all’ora – per allineare i due sistemi. Per acquisire determinate WBS/WBE da SAP, il sistema EAM invierà alcuni filtri specifici per l'acquisizione:

stato oggetto: può essere APERTO, RIL o AUTO e filtra le WBS/WBE rispettivamente in quelle con stato rilasciato o autorizzato. Il filtro sullo stato verrà applicato soltanto alla prima sincronizzazione tra i due sistemi. A seguito della prima esecuzione il filtro sullo stato verrà eliminato in modo da poter registrare i passaggi di stato successivi.

tipo spesa: può essere CAPEX e filtra WBS/WBE "valide" che hanno "profit center" che termina con i caratteri "LIN", oppure ANR che filtra quelle "contabili valide" che hanno "profit center" che termina con "EPC".

data creazione e data modifica: nel caso di caricamento massivo in EAM da SAP, non saranno inviate la "data creazione" e la "data modifica", consentendo a SAP di effettuare un invio massivo. Per quanto riguarda il flusso previsto per l'allineamento degli oggetti contabili, ad ogni esecuzione saranno importati solo i nuovi oggetti contabili o quelli modificati di recente (acquisizione delta). In questo caso, SAP invierà solo quelli che hanno una data maggiore rispetto alla "data creazione" o “data modifica”.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | 1 all’ora |
| Timing (Giornaliera, Mensile, ecc) | Giornaliera |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| ID Elemento CBS (=CBE) | ID univoco della CBE | Y |  |
| ID CBS | Elemento WBS (identificatore all'interno della gerarchia) | N |  |
| Descrizione CBE | Descrizione testuale della CBE | Y |  |
| Descrizione CBS | Descrizione testuale della CBS | N |  |
| Stato sistema | Visualizza lo stato sistema | Y |  |
| Stato utente | Visualizza lo stato utente | Y |  |
| CDC Responsabile | Codice del centro di costo dell'elemento cbs responsabile dell'attività richiesta | Y |  |
| CdC richiedente | Codice del centro di costo dell'elemento cbs richiedente dell'attività richiesta | Y |  |
| Soc.richiedente | Società dell'elemento cbs richiedente dell'attività richiesta | N |  |
| Data creazione | Data di creazione della cbs | Y |  |
| Data ultima modifica | Data dell'ultima modifica della cbs | Y |  |
| Tipo progetto | Tipologia di progetto a cui appartiene la cbs | Y |  |
| Profit Center | Profit Center cbs | Y |  |
| Sede tecnica | Impianto a cui è collegata la cbs | Y |  |
| Motivo investimento | Indica un motivo di investimento | N |  |
| Cluster | Codice cluster a sostituzione del codice incentivazione | N |  |
| Cod. intervento | Codice intervento | N |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

## Flusso AcquisizioneOI

### Descrizione funzionale interfaccia

La valorizzazione delle informazioni relative agli oggetti contabili avviene tramite l’interfaccia di acquisizione massiva da SAP. L’elenco di OI attivi in SAP (stato RIL) selezionabili è definito in base al tipo di ordine (T06 e T09)

### Logiche d’attivazione

L’interfaccia verrà attivata in modo automatico periodicamente – 1 volta all’ora – per allineare i due sistemi.

Per acquisire determinati ordini interni da SAP, il sistema EAM invierà alcuni filtri specifici per l'acquisizione:

stato oggetto: può essere RIL, ossia ordini in stato rilasciato. Il filtro sullo stato verrà applicato soltanto alla prima sincronizzazione tra i due sistemi. A seguito della prima esecuzione il filtro sullo stato verrà eliminato in modo da poter registrare i passaggi di stato successivi.

tipo ordine: può essere T06 o T09.

data creazione e data modifica: nel caso di primo caricamento in EAM da SAP, non saranno inviate la "data creazione" e la "data modifica", permettendo a SAP di effettuare un invio massivo. Per quanto riguarda il flusso previsto per l'allineamento degli oggetti contabili, ad ogni esecuzione saranno importati solo i nuovi oggetti contabili o quelli modificati di recente (acquisizione delta). In questo caso, SAP invierà solo quelli che hanno una data maggiore rispetto alla "data creazione" o alla “data modifica” specificata nel filtro.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | BWCE |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | 1 all’ora |
| Timing (Giornaliera, Mensile, ecc) | Giornaliera |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| ID | Identificativo univoco dell'OI | Y |  |
| Descrizione | Descrizione OI | Y |  |
| Tipo ordine | Tipo OI | Y |  |
| Cat. Ordine | categoria ordine | Y |  |
| Data acquisizione | Data acquisizione | Y |  |
| Data modifica | Data modifica | Y |  |
| Stato sistema | Visualizza lo stato sistema | Y |  |
| Stato utente | Visualizza lo stato utente | N |  |
| Società | Società | N |  |
| CDC Responsabile | Codice del centro di costo dell'elemento cbs responsabile dell'attività richiesta | Y |  |
| Codice Cliente | Codice Cliente | N |  |
| Centro di costo | Centro di costo per scarico costi | N |  |
| Data creazione | Data di creazione | Y |  |
| Data modifica | Data di ultima modifica | Y |  |
| Profit center | Profit center dell'OI | Y |  |
| Classe ogg. | Identifica la classe oggetto | N |  |

## Flusso AcquisizioneOdM

### Descrizione funzionale interfaccia

La valorizzazione delle informazioni relative agli oggetti contabili avviene tramite l’interfaccia di acquisizione massiva da SAP.

L’interfaccia deve acquisire tutti gli OdM in stato APERTO, RIL, TECO e associati a tutte le combinazioni ammissibili di:

Tipo attività PM;

Tipo ordine.

### Logiche d’attivazione

L’interfaccia verrà attivata in modo automatico periodicamente – 1 volta all’ora – per allineare i due sistemi. Il flusso previsto per l’allineamento degli oggetti contabili importerà, ad ogni esecuzione, soltanto i nuovi oggetti contabili o quelli modificati di recente.

L’interfaccia deve acquisire tutti gli OdM e associati a tutte le combinazioni ammissibili di:

Tipo attività PM (la tipologia di attività svolta)

Tipo ordine (il tipo di OdM)

Stato (che può assumere i valori: APERTO \| RIL \| TECO); Il filtro sullo stato verrà applicato soltanto alla prima sincronizzazione tra i due sistemi. A seguito della prima esecuzione il filtro sullo stato verrà eliminato in modo da poter registrare i passaggi di stato successivi.

data creazione e data modifica: nel caso di primo caricamento da SAP, non saranno inviate la "data creazione" e la "data modifica", permettendo a SAP di effettuare un invio massivo. Per quanto riguarda il flusso previsto per l'allineamento degli oggetti contabili, ad ogni esecuzione saranno importati solo i nuovi oggetti contabili o quelli modificati di recente (acquisizione delta). In questo caso, SAP invierà solo quelli che hanno una data maggiore rispetto alla "data creazione" o alla “data modifica” specificata nel filtro.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | BWCE |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | 1 all’ora |
| Timing (Giornaliera, Mensile, ecc) | Giornaliera |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| ID | ID univoco dell'OdM | Y |  |
| Tipo ordine | Tipo OdM | Y |  |
| Tipo attività PM | Tipo attività | Y |  |
| Testo breve | descrizione ID Odm | Y |  |
| Stato sistema | Visualizza lo stato sistema | Y |  |
| Stato utente | Visualizza lo stato utente | Y |  |
| CLAvResp | Centro di lavoro responsabile dell'attività | Y |  |
| Impianto/sede Tecnica | Codice Impianto/sede tecnica e descrizione | N |  |
| Sede tecnica | Sede tecnica a cui è collegato l'OdM | Y |  |
| Operazione | Numero Operazione | Y |  |
| Desc.Operazione | Desc.Operazione | Y |  |
| Centro di costo responsabile | Centro di costo responsabile | Y |  |
| Centro di costo | Centro di costo per scarico costi | Y |  |
| Profit Center | Profit Center | Y |  |
| Autore/autore modifica | Autore creazione/modifica | Y |  |
| Data creazione | Data creazione ODM | Y |  |
| Data modifica | Data ultima modifica OdM | Y |  |

## Flusso AcquisizioneNTW

### Descrizione funzionale interfaccia

La valorizzazione delle informazioni relative agli oggetti contabili avviene tramite l’interfaccia di acquisizione massiva da SAP. L’interfaccia deve acquisire tutte le NTK valide (non bloccate o chiuse) associate a WBE importate nel primo flusso.

### Logiche d’attivazione

L’interfaccia verrà attivata in modo automatico periodicamente – 1 volta all’ora – per allineare i due sistemi. Il flusso previsto per l’allineamento degli oggetti contabili importerà, ad ogni esecuzione, soltanto i nuovi oggetti contabili o quelli modificati di recente.

L’interfaccia deve acquisire tutte le NTW valide e associate a WBE importate nel primo flusso e a tutte le possibili combinazioni di:

Stato (status diverso da: bloccata \| chiusa); Il filtro sullo stato verrà applicato soltanto alla prima sincronizzazione tra i due sistemi. A seguito della prima esecuzione il filtro sullo stato verrà eliminato in modo da poter registrare i passaggi di stato successivi.

data creazione e data modifica: nel caso di primo caricamento da SAP, non saranno inviate la "data creazione" e la "data modifica", permettendo a SAP di effettuare un invio massivo. Per quanto riguarda il flusso previsto per l'allineamento degli oggetti contabili, ad ogni esecuzione saranno importati solo i nuovi oggetti contabili o quelli modificati di recente (acquisizione delta). In questo caso, SAP invierà solo quelli che hanno una data maggiore rispetto alla "data creazione" o alla “data modifica” specificata nel filtro.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | BWCE |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | 1 all’ora |
| Timing (Giornaliera, Mensile, ecc) | Giornaliera |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| ID | ID Univoco NTW | Y |  |
| Descrizione | Descrizione NTW | Y |  |
| Stato sistema | Visualizza lo stato sistema | Y |  |
| Stato utente | Visualizza lo stato utente | Y |  |
| Progetto | Progetto a cui è legata NTW | Y |  |
| Elemento CBS | Codice CBE a cui è legata | Y |  |
| Società | Società | N |  |
| Profit Center | Profit Center | Y |  |
| CdC Responsabile | = al centro responsabile presente sulla CBE | Y |  |
| Tipo di network | Tipo NTW | Y |  |
| Data creazione | Data creazione NTW | Y |  |
| Data modifica | Data ultima modifica NTW | Y |  |
| Autore creazione/modifica | Autore creazione/modifica | Y |  |
| Operazioni della NTW |  | NA |  |
| ID Operazione | N. Operazione della network | Y |  |
| Descrizione operazione | Descrizione Operazione | Y |  |
| Stato sistema | Visualizza lo stato sistema | Y |  |
| CDL resp | Centro di lavoro Responsabile | Y |  |
| Data creazione | Data creazione NTW | Y |  |
| Data modifica | Data ultima modifica NTW | Y |  |
| Autore creazione/modifica | Autore creazione/modifica | Y |  |

## Flusso DisassociazioneOdM – Perimetro release 1

### Descrizione funzionale interfaccia

Nel caso in cui sia necessario modificare l’OdM associato all’OdL è possibile utilizzare la funzionalità “Disassocia OdM” (innescata dal relativo pulsante) la quale invia a SAP una notifica per la rimozione del link fra Istanza e OdM in SAP e contestualmente de-valorizza il campo “Identificativo OdM” lato EAM. La funzionalità può essere utilizzata solo per gli OdL che non hanno ricevuto consuntivazione in SAP.

### Logiche d’attivazione

È possibile utilizzare la funzionalità “Disassocia OdM” (innescata dal relativo pulsante) la quale invia a SAP una notifica di disassociazione dell’OdM.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | BWCE |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 100 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| Istanza/OdL | ID dell'ordine EAM da disassociare | Y |  |
| OdM | ID dell'OdM per cui si chiede la disassociazione | Y |  |

## Flusso DisassociazioneNTW – Perimetro release 1

### Descrizione funzionale interfaccia

Nel caso in cui sia necessario modificare la NTK associato all’OdL è possibile utilizzare la funzionalità “Disassocia NTK” (innescata dal relativo pulsante) la quale invia a SAP una notifica di rimozione del link fra Istanza e Network in SAP e contestualmente de-valorizza il campo “Identificativo NTK” lato EAM. La funzionalità può essere utilizzata solo per gli OdL che non hanno ricevuto consuntivazione in SAP.

### Logiche d’attivazione

È possibile utilizzare la funzionalità “Disassocia NTK” (innescata dal relativo pulsante) la quale invia a SAP una notifica di disassociazione della Network

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | BWCE |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 100 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| Istanza/OdL | ID dell'ordine EAM da disassociare | Y |  |
| NTK | ID della NTK per cui si chiede la disassociazione | Y |  |

## Flusso ControlloPeriodoContabile

### Descrizione funzionale interfaccia

Per l’invio delle consuntivazioni a SAP è necessario che si verifichi che il periodo contabile su SAP sia aperto.

### Logiche d’attivazione

Click sull’azione “invia a SAP” da parte dell’utente, check preliminare effettuato per l’invio della consuntivazione a SAP.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | Circa 2000 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| IEsercizio | Anno esercizio | Y |  |
| Iperiodo | Periodo dell'esercizio | Y |  |
| Isocieta | Società (campo ereditato dal workorder) | Y |  |
| ITpConto |  | Y |  |

## Intersezione tecnico patrimoniale

### Descrizione funzionale interfaccia

In qualsiasi fase del processo, nell’applicazione “Intersezione tecnica patrimoniale” è possibile consultare la tabella rappresentata in Figura 13 la quale recepisce le quantità sottese a interventi di tipo sostituzione e/o costruzione relative agli OdL con tipo spesa CAPEX.

### Logiche d’attivazione

ll flusso di integrazione "Intersezione tecnico patrimoniale” verrà triggerato manualmente da parte dell’utente. Tramite UI potrà cliccare il pulsante dedicato per l’invio delle informazioni contenute all’interno della tabella.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 100 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
|  |  |  |  |
| indicatore | Componente di cui si calcola la variazione | Y |  |
| importo | Quantità variata | Y |  |
| mese | Mese | Y |  |
| anno | Anno | Y |  |

## Chiusura OdM – Perimetro release 2

### Descrizione funzionale interfaccia

Il flusso invocato da EAM permette di chiudere l’ODM in SAP.

### Logiche d’attivazione

Il flusso di ChiusuraOdM verrà innescato quando un OdL verrà chiuso in EAM, questo farà scattare il flusso che rimuoverà l’OdM associato.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 100 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| odm | Id Odm da cancellare in SAP | Y |  |

## Chiusura NTW – Perimetro release 2

### Descrizione funzionale interfaccia

Il flusso invocato da EAM permette di chiudere NTW in SAP.

### Logiche d’attivazione

Il flusso di ChiusuraNTW verrà innescato quando un OdL verrà chiuso in EAM, questo farà scattare il flusso che rimuoverà la NTW associata.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 100 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | SAP |

### Dati scambiati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| istanza | Id riguardante l'attività del centro operativo corrisponde all’ODL associato alla Network | Y |  |
| network | Id network sap richiesta di cancellare in SAP | Y |  |
| Operazione | Identificativo univoco delle attività comprese negli OdM o nelle NTW (uno o più di uno) | Y |  |

## RichiestaConsuntivazioneCallback – Perimetro release 2

### Descrizione funzionale interfaccia

Il flusso funzionalmente è accoppiato al flusso InvioConsuntivi verso SAP. Il flusso infatti rappresenta la risposta asincrona che il sistema SAP fornirà al sistema EAM dopo la ricezione di un consuntivo con l’esito della consuntivazione.

La mimica infatti di questi 2 flussi prevede che EAM invii a SAP la richiesta di consuntivi mediante il flusso InvioConsutivi descritto nel paragrafo 3.3. SAP dopo aver lavorato in modo asincrono la richiesta di consuntivo restituisce risposta con l’esito dei consuntivi mediante questo flusso.

### Logiche d’attivazione

Il flusso RichiestaConsuntivazioneCallback sarà invocato da SAP verso EAM in modo asicrono, in risposta al flusso InvioConsuntivi.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | SOAP |
| --- | --- |
| BUS | TIBCO |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | Circa 40.000 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Asincrona |
| Rete | Intranet |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |
| Sistema che espone l’api | EAM |

### Dati scambiati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| EAMKey | Chiave EAM | Y |  |
| Result | Risultato della consuntivazione | Y |  |
| error_code | Errore della consuntivazione se presente | N |  |
