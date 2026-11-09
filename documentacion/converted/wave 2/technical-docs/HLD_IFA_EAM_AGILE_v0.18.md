# HLD_IFA_EAM_AGILE_v0.18

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
| Ordine di Lavoro (OdL) | Dal punto di vista di EAM rappresenta l’intervento di manutenzione, sostituzione e monitoraggio schedulato ed assegnato ad una o più risorse(Titris) secondo la disponibilità la corrispondenza de skills |  |
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
| A | RichiestaPeriodoIndisponibilitàOdL | Invio Ordini di Lavoro che richiedono il periodo d’indisponibilità. |
| B | NotificaPeriodoIndisponibilità | Notifica sulla richiesta di indisponibilità la quale può essere: deliberata sospesa, posticipata, rifiutata etc. |

## RichiestaPeriodoIndisponibilitàOdL

### Descrizione funzionale interfaccia

Il sistema EAM invia ad Agile tutti gli Ordini di Lavoro in cui è necessario che venga richiesta una finestra di indisponibilità della rete per poter effettuare l’attività. L’informazione che quella determinata tipologia di attività potrebbe essere soggetta a gestione dell’indisponibilità dell’asset è specificata in un campo apposito nel Piano di Lavoro all’interno dell’Ordine di lavoro.

Se su EAM vengono apportate delle modifiche ai campi presenti nel tracciato del flusso degli Ordini di Lavoro già precedentemente inviati, l’interfaccia dovrà inviarne gli aggiornamenti all’applicativo affinché sia costantemente allineato.

Nel caso in cui gli Ordini di lavoro su EAM vengano aggregati, su Agile verrà inviato l’Ordine di Lavoro aggregatore contenente anche le informazioni dei figli aggregati e sarà portata avanti la richiesta di indisponibilità solo sull’Ordine di Lavoro padre.

Relativamente agli Ordini di lavoro figli EAM non invierà più aggiornamenti anche in caso di modifica ai campi del tracciato. Su Agile si dovranno implementare delle logiche per inibire le funzionalità di richiesta di indisponibilità sugli Ordini di lavoro figli.

Se in uno degli OdL figli è stata già indicata un Codice della Indisponibilità, non potrà essere aggregato su EAM a meno dell’annullamento della indisponibilità

### 3.1.2 Logiche d’attivazione

L’OdL generato in EAM verrà inviato automaticamente ad Agile per richiedere periodi di indisponibilità di un asset al fine di svolgere l’attività.

L’interfaccia si deve innescare ogni volta che OdL, che contiene un piano di lavoro che prevede indisponibilità, va in stato “Aperto”.

Inoltre, devono essere valorizzate le seguenti informazioni:

Codice Ordine di Lavoro (OdL)

Descrizione ODL

Unità Impianti responsabile dell’attività

Piano di lavoro

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
| Unità Impianti responsabile dell’attività | Unità Impianti che è definita come responsabile dell'attività da catalogo tassonomico (es. DTNVSG) | Y |  |
| Piano di Lavoro | Codice PdL EAM | Y |  |
|  |  |  |  |
| Tipologia attività(aggregatore per AGILE 2.0) | Mapping di aggregazione tra piano di lavoro e tipologia attività su AGILE 2.0 | N |  |
| <Lista di codici di lavoro> | Lista di codici identificativi degli OdL EAM(Lista dedicata agli OdL aggregati/figli) | N |  |
| Codice Ordine di lavoro | Identificativo OdL EAM | N |  |
| </Lista di codici di lavoro> |  |  |  |
| Data inizio pianificata | Data inizio pianificata della singola attività | Y |  |
| Data fine pianificata | Data fine pianificata della singola attività | Y |  |
|  |  |  |  |
| <Sedi tecniche indisponibilità> | Lista di sedi tecniche d’indisponibilità associata alla sede tecnica Reale e presente in anagrafica(sede tecnica indisponibilità master). | Y |  |
| Codice sede tecnica indisponibilità | Codice della sede tecnica di indisponibilità. | Y |  |
| </Sedi tecniche indisponibilita> | - |  |  |
|  |  |  |  |
| <Sedi tecniche interferenti> | Lista delle sedi tecniche interferenti . | N |  |
| Sede tecnica interferente | Codice della sede tecnica interferente. | N |  |
| </Sedi tecniche interferenti> | - | - |  |
| WBS (CBS) | Informazione recuperata dall’OdL, in caso di intervento ad investimento si potrebbe inizialmente inserire la “WBS o WBE prevalente”. | N |  |
| Tipo Spesa |  | Y |  |
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
| Stato | Stato ll'indisponibilità . | Y |  |
| MOD(ex Motivazione) | Descrive la motivazione riguardo sospensione, annullamento etc.. della richiesta di indisponibilità. | N |  |
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
