# HLD_TAGETIK_EAM_perimetro1_perimetro2_v9

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento | HLD – Flussi di integrazione TAGETIK vs EAM |

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
| Riesaminato da: | ACN | 03/07/2024 |  |
| Revisionato da: | ACN | 19/07/2024 |  |
| Approvato da: | TERNA | GG/MM/AAAA |  |

# Glossario

| Termine | Definizione |
| --- | --- |
| Titris | Unità di Budget, Titris\Area Geografica |
| Capacity | Ammontare di ore per una UdB splittate per Operaio/Quadro/Impiegato |
| Tariffa | Costo Medio orario di una competenza (in uno scenario di pianificazione, per operaio, quadro, impiegato) |
| OI | Ordine Interno |
| CMO | Costo Medio Orario |

# Obiettivi del documento

Nell’ambito del progetto di implementazione della piattaforma IBM Maximo e quindi anche della dismissione dei sistemi Make ed MBI, il documento ha lo scopo di descrivere funzionalmente i flussi d’integrazione tra EAM e Tagetik e di riportarle per ogni flusso:

Descrizione funzionale

Logiche d’attivazione

Dati scambiati

Tipologia d’interazione

Gestione Errori

# Flussi d’integrazione e logica d’attivazione

Questa sezione si prefigge di descrivere i flussi che intercorrono tra i sistemi di TAGETIK ed EAM e le relative interazioni.

Di seguito diagramma di chiamate tra i due sistemi:

| N. Flusso | Nome del flusso | Descrizione funzionale | Esposto da |
| --- | --- | --- | --- |
| 1 | Import Capacity-CMO | Il flusso si occuperà di acquisire, mediante chiamata verso Tagetik, il complesso di dati inerenti alle capacity per unità di budget, suddivisa per mensilità e qualifica nonché i relativi costi medi associati alle figure professionali (Quadri, Impiegati, Operai) | Tagetik |
| 2 | Flusso Recupero costi (Esterni ed Interni) | Il flusso si occuperà di trasferire da Tagetik verso EAM i costi delle risorse esterne ed interne caricati su Tagetik. | Tagetik |
| 3 | Flusso Import Lista Scenari | L’interfaccia di ricezione Scenari permette di trasferire da Tagetik verso EAM la lista completa degli scenari presenti su Tagetik per un determinato anno. | Tagetik |

## Flusso Import Capacity-CMO [perimetro1]

### Descrizione funzionale interfaccia

Tagetik espone un’unica vista complessiva, contenente sia i dati di capacity che i dati relativi al CMO per qualifica.

Per poter differenziare l’attività di importazione dei dati tra capacity e CMO verrà richiesto all’utente di popolare un'unica request che opererà un’azione di filtro in virtù del valore acquisito dal campo “conto”.

Se il campo “conto” assume il valore di “capacity”, il flusso consentirà di importare in fase di pianificazione annuale, la capacity delle ore per unità di budget, suddivisa per mensilità e qualifica. A Tagetik verrà richiesto attraverso la chiamata, di popolare le informazioni necessarie per compilare i dati della capacity annuale o per anni futuri (entro i 5 anni), per uno specifico titris (se specificato nella request dello scambio), oppure per tutte le unità di budget/titris presenti a sistema nello scenario di riferimento (budget, forecast, pre-closing).

Diversamente, se il campo “conto” assume il valore di “CMO” il flusso consentirà di importare i dati di tariffa con i relativi costi medi orari associati alle figure professionali (Quadri, Impiegati, Operai).

### Logiche d’attivazione

Su EAM, l’utente, per poter attivare il flusso su richiesta della Capacity, dovrà accedere all’applicazione Capacity, attivare il pulsante di “Import Capacity”, si aprirà una dialog in cui si dovranno compilare i campi Titris e Scenario di riferimento

Per poter altresì attivare il flusso relativo all’import del CMO, l’utente dovrà accedere all’applicazione Gestione Tariffe, attivare il pulsante di “Import massivo”, si aprirà una dialog in cui si dovranno compilare il campo Scenario di riferimento.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | HTTPS |
| --- | --- |
| BUS | APIM |
| Tipo Esposizione | API |
| Frequenza Invocazione | <2500 |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet/Internet |
| Modalità di Autenticazione | OAuth2 (Solo lato EAM) |
| Approccio trasmissione dati | REST |
| Formato Dati Supportati | JSON |
| Sistema che espone | Tagetik |

### Dati scambiati

Per la richiesta dati, nella request verranno trattati i seguenti campi:

| Dato | Descrizione funzionale | Obbligatorio | Note | Campo Tagetik |
| --- | --- | --- | --- | --- |
| Titris | Unità di Budget | N |  | Prendere a riferimento il campo Titolare Risorsa – Tabella Saldi Lordi, Colonna Q |
| Scenario | Scenario Tagetik | Y | Il campo verrà popolato da GUI da parte dell’utente selezionando uno scenario importato da Tagetik. | Prendere in considerazione il campo Scenario – Tabella Saldi Lordi, Colonna A |
| Conto | Tipo estrazione Dominio [Capacity, CMO] | Y |  | Prendere a riferimento il campo Conto – Tabella Saldi Lordi, Colonna G |

Qualora il campo “conto” assuma il valore di “Capacity”, la response attesa dall’interfaccia

consente di trasferire i seguenti dati Verso EAM:

| Dato | Descrizione funzionale | Obbligatorio | Note | Campo Tagetik |
| --- | --- | --- | --- | --- |
| Titris | Unità di Budget | Y |  | Prendere a riferimento il campo Titolare Risorsa – Tabella Saldi Lordi, Colonna Q |
| Scenario | Scenario Tagetik | Y |  | Prendere in considerazione il campo Scenario – Tabella Saldi Lordi, Colonna A |
| Mese | Mese di riferimento | Y |  | Prendere a riferimento il Campo Periodo – Colonna C Tabella Saldi Lordi |
| Figura Professionale | Qualifica professionale del lavoratore associata al costo medio fornito | Y |  | Prendere a riferimento il Campo Inquadramento – Colonna M Tabella Saldi Lordi/R |
| Capacity relativa alla qualifica | Capacity relativa alla qualifica | Y |  | Prendere a riferimento il Campo Importo in valuta Organizzazione – Colonna W Tabella Saldi Lordi |

Nel momento in cui per una determinata figura professionale (p.e. “Impiegato”) Tagetik esponga più righe a parità di “Anno”, “Conto” e “Scenario”, l’interfaccia restituirà più record che il sistema chiamate EAM raggrupperà.

Qualora il campo “conto” assuma il valore di “CMO”, la response attesa dall’interfaccia consente di trasferire i seguenti dati verso EAM:

| Dato | Descrizione funzionale | Obbligatorio | Note | Campo Tagetik |
| --- | --- | --- | --- | --- |
| Titris | Unità di Budget | Y |  | Prendere a riferimento il campo Titolare Risorsa – Tabella Saldi Lordi, Colonna Q |
| Scenario | Scenario Tagetik | Y |  | Prendere in considerazione il campo Scenario – Tabella Saldi Lordi, Colonna A |
| Figura Professionale | Qualifica professionale del lavoratore associata al costo medio fornito | Y |  | Prendere a riferimento il Campo Inquadramento – Colonna M Tabella Saldi Lordi |
| Costo medio orario | Costo medio orario | Y |  | Prendere a riferimento il Campo Importo in valuta Organizzazione – Colonna W Tabella Saldi Lordi |

### Gestione errori

Le tipologie di errore rinvenibili in questo flusso si elencano come segue:

Richiesta Unità di Budget non presente;

Anno di riferimento eccedente il range stabilito (5 anni): si generalizzerà il messaggio di errore in “Anno di riferimento non disponibile”;

Qualifica non presente.

### Punti di attenzione &Vincoli

Il corredo dimensionale che verrà preso a riferimento in Tagetik sarà quello presente in tabella Dati Saldi Lordi di Reporting; nello specifico puntando ai seguenti campi:

| Codice | Descrizione |
| --- | --- |
| SCENARIO | SCENARIO |
| PERIODO | PERIODO |
| CONTO | CONTO |
| CATEGORIA | CATEGORIA |
| ORGANIZZAZIONE | ORGANIZZAZIONE |
| DEST1 - Oggetto Contabile | DEST1 - Oggetto Contabile |
| DEST2 - Attività | DEST2 - Attività |
| DEST3 - Inquadramento/Risorsa | DEST3 - Inquadramento/Risorsa |
| DEST4 - Cliente | DEST4 - Cliente |
| DEST5 - Titolare Risorsa | DEST5 - Titolare Risorsa |

La corrispondenza dei campi è stata indicata nel dettaglio flussi descritto ai paragrafi precedenti con indicazione anche del campo Tagetik.

In Tagetik verrà creato un ETL che prenderà i dati dalla tabella DATI SALDI LORDI di Reporting (senza alcun tipo di elaborazione) e li trasferirà su un dataset ad-hoc creato per l’occorrenza.

Per creare il dataset nuovo verranno applicati i seguenti filtri sulla tabella DATI SALDI LORDI:

Conto = Capacity e CMO

Organizzazione: solo aziende nel perimetro RTN

Categoria = Amount Opex e $Amount

Frequenza di lancio ETL per popolamento nuovo dataset: Giornaliera

L’ ETL di estrazione dei dati dalla DATI SALDI LORDI di Reporting non eseguirà alcuna logica di elaborazione/calcolo; pertanto, EAM leggerà i dati così come esposti da Tagetik ed eventuali logiche di elaborazione saranno in carico alla piattaforma EAM.

## Flusso Recupero Costi (Esterni ed Interni) [perimetro 2]

### Descrizione funzionale di interfaccia

L’interfaccia di ricezione dei costi esterni ed interni permette di trasferire da Tagetik verso EAM i costi di tutte le risorse esterne ed interne caricati su Tagetik. I costi (sia di risorse interne che esterne) legati agli OdM verranno resi visibili all’interno degli ordini di lavoro a cui sono collegati; i costi legati alle NTK, invece, verranno resi visibili negli ordini di lavoro soltanto nel caso delle risorse interne.

### Logiche di attivazione

L’interfaccia verrà attivata in modo automatico, giornalmente – 1 volta al giorno – per allineare il sistema EAM a quello di Tagetik. Il flusso previsto per l’allineamento delle consuntivazioni importerà, ad ogni esecuzione, soltanto le nuove consuntivazioni.

Le logiche di attivazione prevedono che a regime ogni giorno BWCE inneschi all’aggiornamento dei dati da tagetik ad EAM.

La tabella EAM viene creata con stessa struttura della tabella del DBStaging e popola con i filtri seguenti filtri (**)

- campo TIPO_OGG_CONTAB = ORD oppure WBS

- campo TITOLARE = blank or uno dei campi della tabella custom dei CdC SAP che hanno Area Funzionale= RTN* (tabella verde)

- campo CDC_RESP_ORG = blank or uno dei campi della tabella custom dei CdC SAP che hanno Area Funzionale= RTN* (tabella verde)

Ogni giorno BWCE esegue la query filtrata per (**) ed raggruppata per anno/mese e count per campo CO_NUM_DOC su DBStaging Tagetik e su DB MAS. Compara i risultati verificando il delta. Attraverso la query riesce a verificare se vi sono state delle modifiche o meno sui consuntivi. La query dovrà essere eseguita con una profondità temporale similare al caricamento indicato al punto 0.

I record differenti dovranno essere caricati su DB EAM su tabelle di staging riportando la data aggiornamento come campo aggiuntivo.

Un volta completato l’aggiornamento BWCE comunicherà ad EAM che l’aggiornamento della tabella di staging è completata.

Una volta avuta comunicazione ms eam-reporting-loader recupera la data dell’ultimo run MAS ed esegue in modo transazione la cancellazione del singolo mese e ricaricamento dalla tabella di Staging (tabelle gialle) i record che hanno data modifica maggiore uguale a timestamp run ultima esecuzione import dati su tabelle EAM. A valle della copia dei dati il ms eam-loader provvederà a salvare il nuovo timestamp run ultima esecuzione.

### Tipologia di interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Modalità d’interazione | REST |
| --- | --- |
| BUS | Tibco |
| Tipo Esposizione | API |
| Frequenza Invocazione | 1 al giorno |
| Timing (Giornaliera, Mensile, ecc) | Giornaliera |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | Https |
| Formato Dati Supportati | JSON |
| Sistema che espone l’api | Tagetik |

### Dati scambiati

Questa interfaccia consentirà, a seguito di richiesta, di importare nel sistema EAM i seguenti dati:

| Dato | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| Attività | Macroarea Tipo di Spesa (codice CoIn EAM) | Y |  |
| CDC_RESP_TGK | Centro di costo responsabile Tagetik | N |  |
| CDC_RESP_ORG | Centro di costo responsabile organizzativo | N |  |
| CLIENTE | Cliente su cui scaricare i costi | N |  |
| ESERCIZIO | Anno di esercizio | Y |  |
| OGGETTO_CONTAB | Oggetto contabile su cui scaricato il costo (es. ID di una CBE oppure ID di un CDC) | Y |  |
| TIPO_OGG_CONTAB | Tipo oggetto contabile (ORD or WBS) | Y |  |
| GRUPPO MERCI | Gruppo merci del Resource ID (popolato in caso di ris. Esterna) | N |  |
| IMPIANTO | Impianto della CBS | N |  |
| NETWORK | ID della Network a cui è legato il costo | N |  |
| OGGETTO_CONTAB_MIT | Posizione Network | N |  |
| OPERAZIONE_NET | Operazione Network | N |  |
| MM_NUM_ORDINE | Numero ordine legato alla RE | N |  |
| TITOLARE_RIS | Titolare risorsa | N |  |
| PERIODO | Mese dell’anno di fase (dominio: 2024002 – febbraio 2024) | Y |  |
| SCENARIO_BW | Scenario di riferimento; staticamente sempre EFFETTIVO. | Y |  |
| ENTITY | Società contabili | Y |  |
| SOCIETA_RICH | Società richiedente dell’intervento (TR01, TR36, TR26) | N |  |
| ORD_TYPE | Tipo ordine PM | Y |  |
| TIPO_IMPIANTO | Tipologia impianto | N |  |
| RISORSA_INT_EST | Concatenazione di CDC e inquadramento della risorsa oppure Resource ID della RE | Y |  |
| UNIT | Unità di misura della quantità | Y |  |
| VDC_BW | ID Voce di costo SAP BW | N |  |
| CONTO_CE_TGK | Campo di distinzione tra risorse interne ed esterne (4C0290 ris.int.) | N |  |
| INDICATORE_STAT | ID dell’indicatore statistico (al momento NULL) | N |  |
| CO_NUM_DOC |  | Y |  |
| QUANTITA | Quantità di consuntivo (la cui UM è espressa nel campo UNIT) | Y |  |
| IMPORTO | Importo | Y |  |

### Gestione degli errori

Data non valida

## Flusso Import Lista Scenari [perimetro2]

### Descrizione funzionale di interfaccia

L’interfaccia di ricezione Scenari permette di trasferire da Tagetik verso EAM la lista completa degli scenari presenti su Tagetik.

### Logiche di attivazione

Su EAM, l’utente, per poter attivare il flusso su richiesta degli Scenari, dovrà accedere all’applicazione di “Gestione Capacity” ed attivare l’integrazione mediante apposito pulsante laterale a sinistra (Import scenari da Tagetik)

### Tipologia di interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | HTTPS |
| --- | --- |
| BUS | APIM |
| Tipo Esposizione | API |
| Frequenza Invocazione | <2500 |
| Timing (Giornaliera, Mensile, ecc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet/Internet |
| Modalità di Autenticazione | OAuth2 (Solo lato EAM) |
| Approccio trasmissione dati | REST |
| Formato Dati Supportati | JSON |
| Sistema che espone | Tagetik |

### Dati scambiati

Questa interfaccia consentirà, a seguito di richiesta, di importare nel sistema EAM i seguenti dati:

| Dato | Descrizione funzionale | Obbligatorio | Note | Campo Tagetik |
| --- | --- | --- | --- | --- |
| <Scenari> | Array contenente tutti gli scenari presenti in Tagetik | Y |  |  |
| Scenario | Scenario tagetik | Y |  |  |
| </Scenari> |  |  |  |  |

Per questo flusso sarà utilizzato lo stesso flusso dati Import Capacity-CMO vedi capitolo 3.1
