# HLD_IFA_EAM_AGILE_v0.21

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento | HLD– Interface Agreement EAM vs AGILE |

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
| Riesaminato da: | ACN | 28/06/2024 |  |
| Approvato da: | Nome Cognome | GG/MM/AAAA |  |

# Glossario

| Termine | Definizione | Definizione |
| --- | --- | --- |
| Piano di Lavoro | Oggetto MAS utilizzato per rappresentare le attività |  |
| Ordine di Lavoro (OdL) | Dal punto di vista di EAM rappresenta l’intervento di manutenzione, sostituzione e monitoraggio schedulato ed assegnato ad una o più risorse(Titris) secondo la disponibilità e la corrispondenza delle skills. |  |
| Job Task | Oggetto MAS per rappresentare sia i metodi, sia i micro-controlli associati a un PdL |  |

# Obiettivi del documento

Nell’ambito del porting delle funzionalità attualmente gestite dai sistemi Make ed MBI il documento ha lo scopo di descrivere funzionalmente i flussi d’integrazione e di riportante per ogni flusso:

Descrizione funzionale

Logiche d’attivazione

Dati scambiati

Tipologia d’interazione

# Flussi d’integrazione e logica d’attivazione

Questa sezione si prefigge di descrivere i flussi che intercorrono tra EAM e il sistema AGILE e le relative interazioni.

Di seguito diagramma di chiamate tra i sistemi EAM e AGILE:

Il sistema Agile 2.0 non sarà allineato all’anagrafica EAM come concordato con il BSN.

| Id | Flusso | Descrizione |
| --- | --- | --- |
| A | RichiestaPeriodoIndisponibilitàOdL | Invio Ordini di Lavoro che richiedono il periodo d’indisponibilità e relative informazioni agli asset oggetto dell’attività. |
| B | NotificaPeriodoIndisponibilità | Notifica sulla richiesta di indisponibilità la quale può essere: deliberata sospesa, posticipata, rifiutata etc. |

## RichiestaPeriodoIndisponibilitàOdL

### Descrizione funzionale interfaccia

Il sistema EAM invia ad Agile tutti gli Ordini di Lavoro e le informazioni relative all’asset oggetto dell’attività in cui è necessario che venga richiesta una finestra di indisponibilità della rete per poter effettuare l’attività. L’informazione che quella determinata tipologia di attività potrebbe essere soggetta a gestione dell’indisponibilità dell’asset è specificata in un campo apposito nel Piano di Lavoro all’interno dell’Ordine di lavoro.

Se su EAM vengono apportate delle modifiche ai campi presenti nel tracciato del flusso degli Ordini di Lavoro già precedentemente inviati, l’interfaccia dovrà inviarne gli aggiornamenti all’applicativo affinché sia costantemente allineato.

Nel caso in cui gli Ordini di lavoro su EAM vengano aggregati, su Agile verrà inviato l’Ordine di Lavoro aggregatore contenente anche le informazioni dei figli aggregati e sarà portata avanti la richiesta di indisponibilità solo sull’Ordine di Lavoro padre.

Relativamente agli Ordini di lavoro figli EAM non invierà più aggiornamenti anche in caso di modifica ai campi del tracciato. Su Agile si dovranno implementare delle logiche per inibire le funzionalità di richiesta di indisponibilità sugli Ordini di lavoro figli.

Se in uno degli OdL figli è stata già indicata un Codice della Indisponibilità, non potrà essere aggregato su EAM a meno dell’annullamento della indisponibilità.

All’interno del messaggio, nello specifico all’interno del nodo denominato “Sedi tecniche indisponibilità”, verranno inserite tutte le sedi tecniche di indisponibilità legate all’asset di cui si fa manutenzione/ispezione come segue:

Nel caso di un OdL generato sul primo livello di tipo Linea, il codice di sede tecnica d’indisponibilità che verrà veicolato ad AGILE sarà eguale al valore dell’attributo LIN_SEDE_INDISP della Linea;

Nel caso di un OdL generato su un asset di secondo, terzo o quarto livello della linea, il codice di sede tecnica che verrà veicolato ad AGILE sarà eguale al valore dell’attributo LIN_SEDE_INDISP dell’asset di terzo livello “CONDUTTORI” o “CAVI” (rispettivamente se è elemento in aereo o in cavo) riconducibile con la seguente logica: viene navigata la gerarchia di un asset per identificare l’elemento con la classificazione “CONDUTTORI” o “CAVI”, se l’elemento viene trovato viene utilizzato il valore di LIN_SEDE_INDISP; nel caso in cui l’odl parta da un asset in gerarchia con SOSTEGNO AEREO o ESTREMO CAVO, viene recuperato l’attributo LIN_SEDE_INDISP del CONDUTTORI o CAVI figli di della CAMPATA AEREA o TRATTA CAVO che ha i primi 16 digit di SAPID eguali al sostegno o estremo in valutazione;

Nel caso di un OdL generato sul primo livello di stazione, il codice di sede tecnica d’indisponibilità che verrà veicolato ad AGILE sarà eguale al valore dell’attributo STZ_SEDE_INDISP della stazione;

Nel caso di un OdL generato su un asset di secondo, terzo o quarto livello della stazione, il codice di sede tecnica d’indisponibilità verrà recuperato secondo questa logica: partendo dall’asset oggetto dell’OdL, si verifica se l’asset detiene l’attributo STZ_SEDE_INDISP, se la condizione è verificata allora viene utilizzata quell’informazione, se la condizione non è verificata viene risalita la gerarchia dell’asset fino a trovare il primo elemento con l’attributo STZ_SEDE_INDISP;

Nell’array delle sedi tecniche interfenti, nel dettaglio, invece vengono riportare le sedi tecniche interferenti delle sedi tecniche reali interferenti rispetto all’asset oggetto dell’ordine e le sedi tecniche degli asset collegati elettricamente all’asset oggetto dell’ordine.

In particolare per le linee verranno riportate le sedi tecniche interferenti delle linee; per stazioni invece le linee collegate elettricamente.

### 3.1.2 Logiche d’attivazione

L’OdL generato in EAM verrà inviato automaticamente ad Agile per richiedere periodi di indisponibilità di un asset al fine di svolgere l’attività.

L’interfaccia si deve innescare ogni volta che OdL, che contiene un piano di lavoro che prevede indisponibilità, va in stato “Aperto”.

Inoltre, devono essere valorizzate le seguenti informazioni:

Codice Ordine di Lavoro (OdL)

Descrizione ODL

Unità Impianti responsabile dell’attività

Piano di lavoro

Richiesta Indisponibilita

Data inizio pianificata

Data fine pianificata

Tipo Spesa

Un’ulteriore modalità di innesco del flusso si verifica quando il set di informazioni dell'OdL inviate ad agile subisce un'aggiornamento/modifica sui suoi campi. In questo caso, entrambi i sistemi devono essere allineati/aggiornati (EAM/AGILE).

Le stesse logiche di innesco fin qui elencate, si applicano agli Ordini di Lavoro aggregatori.

### 3.1.3 Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Approccio tecnologico | SOAP |
| --- | --- |
| BUS | APIM |
| Tipo Esposizione | API |
| Frequenza Invocazione | < 2500 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet / Internet |
| Esposto Da | AGILE 2.0 |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |

### 3.1.4 Dati scambiati

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| Codice Ordine di Lavoro (OdL) | Identificativo OdL EAM | Y |  |
| Descrizione ODL | Descrizione dell'attività generante l'ordine di lavoro | Y |  |
| Stato ODL | Stato EAM dell’ordine di lavoro, ha lo scopo di comunicare l’annullamento della richiesta di indisponibilita. | Y |  |
| esponsabile |  | Y |  |
| Piano di Lavoro | Codice PdL EAM | Y |  |
| Richiesta Indisponibilita | Flag di richiesta d’indisponibilita, ha lo scopo di far percepire ad AGILE la casistica di annullamento della richiesta d’indisponibilità da parte di EAM | Y |  |
| Tipologia attività(aggregatore per AGILE 2.0) | Mapping di aggregazione tra piano di lavoro e tipologia attività su AGILE 2.0 | N |  |
| <Lista di codici di lavoro figli> | Lista di codici identificativi degli OdL EAM(Lista dedicata agli OdL aggregati/figli) | N |  |
| Codice Ordine di lavoro figlio | Identificativo OdL EAM | N |  |
| </Lista di codici di lavoro figli> |  |  |  |
| Data inizio pianificata | Data inizio pianificata della singola attività | Y |  |
| Data fine pianificata | Data fine pianificata della singola attività | Y |  |
| Sede tecnica d’indisponibilità padre | Sede tecnica d’indisponibilità legata all’Asset di tipo Linea o Stazione (gerarchicamente legato all’asset reale dell’ordine di lavoro) | Y |  |
| Sede tecnica indisponibilita | Codice della sede tecnica di indisponibilità associata alla sede tecnica Reale e presente in anagrafica(sede tecnica indisponibilità master). Verranno veicolate nel dettaglio i codici di ST indisponibilità strettamente connesse all’asset oggetto dell’odl. | Y |  |
| <Sedi tecniche interferenti> | Lista delle sedi tecniche interferenti delle sedi tecniche reali Interferenti alla sede oggetto attività. | N |  |
| Sede tecnica interferente | Codice della sede tecnica interferente. | N |  |
| </Sedi tecniche interferenti> | - | - |  |
| WBS (CBS) | Informazione recuperata dall’OdL, in caso di intervento ad investimento si potrebbe inizialmente inserire la “WBS o WBE prevalente”. | N |  |
| Tipo Spesa | Tipo Spesa | Y |  |
| Esito | Esito invio vs Agile (OK, KO) | Y |  |
| Codice errore | Codice dell'errore registrato | N |  |
| Descrizione errore | Descrizione esplicativa dell'errore registrato | N |  |

### 3.1.5 Gestione errori

Dovranno essere gestite le seguenti casistiche d’errore:

Unità Impianti responsabile dell’attività non censita su Agile e quindi non riconosciuta

Sede tecnica non censita su Agile e quindi non riconosciuta

Impossibilità di gestire la richiesta da parte di Agile

## NotificaPeriodoIndisponibilità OdL

### Descrizione funzionale interfaccia

Se su un Ordine di lavoro inviato su Agile viene aperta la richiesta di Indisponibilità, deve essere notificato attraverso il flusso “Notifica Periodo Indisponibilità OdL” su EAM.

La richiesta di indisponibilità avrà un codice univoco, ovvero il codice Cluster di indisponibilità, le date di inizio e fine e lo stato eventualmente corredato da un campo Motivazione.

Ad ogni variazione di date e Stato, il flusso “Notifica Periodo Indisponibilità OdL” aggiornerà EAM per fare in modo che i due applicativi siano sempre allineati.

La stessa logica sarà applicata anche sull’Ordine di Lavoro aggregatore.

### Logiche d’attivazione

A seguito dell’invio dell’ordine di lavoro, in AGILE 2.0 verrà creata la richiesta di indisponibilità.

È previsto che il flusso risponda con un codice di indisponibilità, le date e lo stato della richiesta.

Il flusso si innesca anche ad ogni aggiornamento di stato o di date della delibera di indisponibilità per cui è previsto l’invio dell’aggiornamento dell’Ordine di Lavoro su EAM.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Approccio tecnologico | SOAP |
| --- | --- |
| BUS | APIM |
| Tipo Esposizione | Api |
| Frequenza Invocazione | < 2500 al mese |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet / Internet |
| Esposto Da | EAM |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |

### Dati scambiati

Questa interfaccia consente il trasferimento delle seguenti Informazioni:

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| Codice OdL | Codice dell'ordine di lavoro EAM comunicato con interfaccia RichiestaPeriodoIndisponibilità. | Y |  |
| Codice cluster titolare | Codice della delibera dell'indisponibilità (codice cluster titolare). | Y | Creato da Agile |
| Data inizio programmazione | Data inizio dell'indisponibilità. | N |  |
| Data fine programmazione | Data fine dell'indisponibilità. | N |  |
| Stato Cluster | Stato del cluster relativo all'indisponibilità . | Y |  |
| MOD(ex Motivazione) | Descrive la motivazione riguardo sospensione, annullamento etc.. della richiesta di indisponibilità(Indsponibilità principale). | N |  |
| Esito | Esito invio vs Agile (OK, KO) | Y |  |
| Codice errore | Codice dell'errore registrato su Agile | N |  |
| Descrizione errore | Descrizione esplicativa dell'errore registrato | N |  |

### 3.2.5 Gestione errori

Il flusso deve gestire i seguenti casi di errore:

Codice OdL non riconciliabile (EAM non riconosce l’ordine come inviato).

Stato indisponibilità non riconosciuto nel dominio.

### 5. Assunzioni

Vi è un open point è relativo al seguente tema:

Il BSN lato Dispacciamento attende che venga definita dai colleghi di AM la mappatura tra le nuove voci del catalogo attività e le voci che saranno presenti in Agile 2.0. La richiesta è che l'informazione sia inviata direttamente da EAM senza necessità di alcuna trascodifica in Agile.

Assumiamo che, a fronte della decisione di transcodifica, si aggiunga il campo “ Tipologia attività” al tracciato, con l’informazione presente in Agile 2.0.
