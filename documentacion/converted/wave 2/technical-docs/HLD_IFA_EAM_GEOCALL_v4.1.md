# HLD_IFA_EAM_GEOCALL_v4.1

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento | HLD– Interface Agreement EAM vs GEOCALL |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data | Owner |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | Michele Russo | 07/06/2024 |  |
| Riesaminato da: | Luca Martino | 07/06/2024 |  |
| Riesaminato da: | OverIT | xxx |  |
| Riesaminato da (SDS-ITD-Soluzioni per TO/IRP) | Rita Sciavicco |  |  |
| Approvato da: |  |  |  |

# Glossario

| Termine | Definizione |
| --- | --- |
| Piano di Lavoro | Oggetto MAS utilizzato per rappresentare le attività |
| Ordine di Lavoro (OdL) | Dal punto di vista di EAM rappresenta l’intervento di manutenzione che deve essere schedulato ed assegnato ad una risorsa (squadra di operai) secondo la disponibilità, la corrispondenza degli skills e criteri di minimizzazione degli spostamenti basati sulla georeferenziazione degli asset da manutenere e degli operai stessi. |
| OdL aggregato | Ordine di lavoro al cui interno vengono contenuti diversi ordini di lavoro che condividono: id PdL (stessa attività), stesso asset di primo o secondo livello dell’aggregatore, stesso codice CoIn, stesso valore dell’attributo “Assistenza/sorveglianza”. Lato Geocall l’OdL aggregato corrisponde ad un Ordine multi-operazione |
| UI | Unità Impianti |
| DSS | Decision Support System – insieme di algoritmi basati su policy e altri cataloghi che determinano la generazione automatica di OdL (controlli, attività di manutenzione, sostituzione, nuove installazioni, demolizioni) relativi al processo di gestione attività O&M e di investimento Terna |

# Obiettivi del documento

Nell’ambito programma di evolutive previste per la wave 2 del progetto EAM il documento ha lo scopo di descrivere funzionalmente i flussi d’integrazione e di riportante per ogni flusso:

Descrizione funzionale

Logiche d’attivazione

Dati scambiati

Tipologia d’interazione

Gestione errori

# Assunzioni di lavoro e Punti Aperti

Questa versione del documento si basa sulle seguenti assunzioni, fermo restando che la versione finale sarà finalizzata a valle della chiusura dei punti aperti e che pertanto le assunzioni non sono da intendersi vincolanti fino a che non verranno confermate da BNS/ICT.

L’anagrafica su Geocall sarà gerarchica

Ai fini dell’acquisizione delle coordinate geografiche, sarà EAM a recuperare l’informazione da SISNET e veicolarla a Geocall nell’ambito del flusso di aggiornamento anagrafico. Analogamente sarà EAM ad acquisire “one shot” l’intero set di dati da SISNET e renderlo disponibile per l’inizializzazione massiva di Geocall.

La soluzione per la gestione della doppia Terna sarà quella che prevede la replica della modellizzazione EAM anche in Geocall (i.e. gestione della relazione di un asset a due elementi “padre”).

L’interazione con SAP finalizzata alla Consuntivazione ore sarà gestita da EAM: le ore vengono inserite e successivamente validate in Geocall; EAM ricevute le ore validate da Geocall si interfaccia con SAP per la relativa consuntivazione

Ai fii della valorizzazione del SAP_ID da associare all’asset cui riferisce un OdL valgono le seguenti regole:

CASO IN CUI ALL’ASSET EAM CORRISPONDO 2 SAP_ID (es. campata in doppia Terna):

In caso di ispezione, verrà visualizzato il SAP_ID specifico della campata in relazione alla linea di riferimento (sia sull’OdL padre che sui figli/operazioni). Questo meccanismo è applicabile sia al caso in cui l’OdL sia stato aperto sul 1° livello Linea (es. Voli LIDAR) che per gli OdL riferiti al tronco di ispezione

RESTANTI CASI: tra i due SAP_ID possibili per l’asset, si prenderà quello per cui il SAP_ID “radice/1° livello” risulta minore (facendo riferimento ai primi 7 digit del SAP_ID)

La consuntivazione del fisico sarà gestita in EAM. Non si prevede di imputare in Geocall tale informazione

# Flussi d’integrazione e logica d’attivazione

Questa sezione si prefigge di descrivere i flussi che intercorrono tra EAM e il sistema GeoCall e le relative interazioni.

Di seguito diagramma di chiamate tra i sistemi EAM e GeoCall, Sisnet:

Legenda

Flusso connettore

Flusso API

| Id | Flusso | Descrizione |
| --- | --- | --- |
| 1 | Invio Tipo Impianto | servizio esposto da GeoCall e richiamato da EAM al fine di trasferire le “Classificazioni” ossia l'anagrafica dei Tipi Impianto a cui l'oggetto di consistenza impiantistica (sedi tecniche) definito negli OdL sarà associato. |
| 2 | Modifica Anagrafica | Servizio esposto da GeoCall e richiamato da EAM al fine di trasmettere l'anagrafica degli asset per la creazione/modifica della consistenza impiantistica in coerenza con il nuovo modello gerarchico. |
| 3 | Modifica Anagrafica Massiva | Flusso di primo infasamento massivo di Anagrafica in cui EAM trasmetterà a GeoCall l’intera base dati di Anagrafica di Linee e Stazioni secondo il nuovo modello gerarchico (incluse coordinate geografiche) |
| 4 | Flusso Invio OdL a GeoCall | Servizio esposto da Geocall e richiamato da EAM al fine di creare o modificare un OdL sul sistema GeoCall. L’invio dell’OdL a Geocall avverrà tramite selezione manuale o massiva di uno o più OdL che sono in stato “Schedulabile”. |
| 5 | Annullamento/Blocco Ordini di Lavoro | Servizio esposto da Geocall e richiamato da EAM al fine di annullare o un intero OdL sul sistema GeoCall (in caso di ordine multi-operazione vengono annullate anche tutte le operazioni sottese). L’invocazione di Geocall avverrà tramite la funzionalità EAM di “ripianificazione OdL” (con conseguente regressione in stato “Aperto” dell’OdL e, nel caso di aggregato, sia dell’OdL aggregatore che dei “figli”). |
| 6 | Annulla Operazione | Servizio esposto da Geocall e richiamato da EAM al fine di annullare una singola operazione sul sistema GeoCall. L’invocazione di Geocall avverrà tramite la funzionalità di “disconnessione” in EAM di un OdL figlio dall’aggregatore (con conseguente regressione in stato “Aperto” dell’OdL “figlio”). |
| 7 | Ricezione Schedulazione OdL | Flusso di EAM che permette di acquisire le matricole e le date di inizio e fine schedulazione per un OdL schedulato/rischedulato su GeoCall (a seguito della conferma del piano giornaliero) |
| 8 | Ricezione Consuntivazione Ore | Flusso di EAM che ha lo scopo di acquisire da Geocall la consuntivazione oraria delle risorse in riferimento ad una specifica operazione |
| 9 | Notifica Cambio Stato | Interfaccia esposta da EAM per la ricezione dei cambi stati delle operazioni avviate in Geocall sia per ordini mono operazione che ordini aggregati. |
| 10 | Storno | Flusso esposto da Geocall tramite il quale EAM trasmette la richiesta di storno: nel caso di storno parziale, si intende come richiesta di azzeramento dello specifico consuntivo nel caso di storno totale, si intende come richiesta di chiusura dell’OdL (stato Chiuso con marcatura “per Storno”) |

Questi flussi si innestano nel ciclo di vita degli Ordini di lavoro gestiti dalle UI. Il ciclo di vita dell’ordine di lavoro è suddivisibile in 4 workflow:

Creazione OdL da parte del DSS o manualmente

Integrazione con SAP per il recupero dei dati contabili

Richiesta di indisponibilità e interazione con Agile

Invio OdL a Geocall e schedulazione/consuntivazione

Invio delle consuntivazioni a SAP e Tagetik.

In particolare, nel caso di OdL UI è previsa la schedulazione in Geocall al quale vengono inviati da EAM gli OdL pronti per essere schedulati. Nel workflow IV le api riportate permettono anche di contribuire all’avanzamento di stato. Di seguito verrà riportato il collegamento al documento funzionale relativo al workflow che si trova nel capitolo 3. Il documento è allineato alla versione 1.2.

La sequenza logica descritta nel documento mostra il processo nella sua forma più lineare; tuttavia, sono possibili ripianificazioni / rischedulazioni degli ordini con i seguenti meccanismi:

Rischedulazione: In fase di esecuzione tecnica (dopo lo stato “Avviato”) è possibile che si renda necessaria una ri-schedulazione dell’ordine in Geocall dovuta ad una sua esecuzione parziale (passaggio in Geocall allo stato “Da Rischedulare”, non ereditato in EAM).L’operazione verrà quindi impostata nello stato “Da rischedulare” in Geocall e ciò permetterà all’utente di poterla fare rientrare nelle operazioni del piano giornaliero.

Nel caso in cui l’operazione ri-schedulata fosse un’operazione “figlia” di un OdL aggregato, al termine della sua ri-schedulazione verrà nuovamente riportata in stato “Schedulato” in entrambi i sistemi (indipendentemente dallo stato in cui si trovava prima in EAM) ed in EAM non verrà fatto variare lo stato dell’OdL aggregatore; nel caso in cui si parli di una ri-schedulazione dell’unica operazione appartenente ad un OdL mono-operazione, allora in quel caso sarà l’intero OdL EAM a tornare allo stato “Schedulato”. La ri-schedulazione potrà essere effettuata anche nel caso in cui l’OdL presenti dei consuntivi parziali emessi e inviati a SAP.

Ripianificazione: Nel caso in cui fosse necessario ripianificare l’OdL su EAM, modificando i dati di pianificazione, è possibile utilizzare l’apposita funzionalità “Ripianifica OdL” presente su EAM (pulsante dedicato all’interno dell’OdL). La funzionalità “Ripianifica OdL” evocherà il servizio esposto da Geocall “Annulla OdL” e questo verrà riportato in stato “Aperto” su EAM, in modo da poter procedere alla modifica degli elementi di pianificazione (su Geocall vengono annullati sia l’OdL che le operazioni sottese). La ripianificazione è consentita solamente per OdL che sono in stato Schedulabile o Schedulato; si precisa che il controllo sull’impossibilità di annullare ordini in tali stati, ma con almeno un consuntivo va applicato lato EAM. Lo stesso flusso concettualmente verrà utilizzato anche per la “ripianificazione” delle singole operazioni sottostanti ad un OdL aggregato, in questo caso l’operatività lato EAM implica la “disconnessione” dell’OdL figlio dall’OdL aggregatore e la regressione in stato “Aperto”, mentre in Geocall verrà annullata la specifica operazione.

## Invio Tipo Impianto (Perimetro release 1)

### Descrizione funzionale interfaccia

L’interfaccia consentirà di trasferire l'anagrafica dei Tipi Impianto, ovvero le Classificazioni EAM a cui vanno riferiti gli oggetti di consistenza impiantistica (Asset).

### Logiche d’attivazione

Ogni qual volta una nuova classificazione sarà configurata su EAM, il flusso dovrà inviare tutta la gerarchia di classificazione a Geocall.

Nello specifico:

EAM trasmetterà sempre l’intero set di informazioni effettuando n chiamate, una per ogni classificazione EAM (tipo Impianto Geocall):

Per tutte le chiamate, sarà valorizzato un codice univoco comune (i.e. CODICE GRUPPO)

Sulla singola chiamata verrà valorizzato il campo LAST_RECORD che sarà sempre uguale a 0 tranne sull’ultima chiamata dove il valore sarà posto pari ad 1 (per dare evidenza del completamento dell’elaborazione)

GEOCALL, ad ogni nuova elaborazione (N.B. ogni nuova elaborazione ha un Codice Gruppo diverso, per cui EAM dovrà gestire questo progressivo) andrà a sostituire completamente il set anagrafico dei tipi impianto preesistenti con quello nuovo inviato da EAM (i record preesistenti verranno cancellati logicamente)

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 1 al mese |
| Timing (Giornaliera, Mensile, etc) | Su richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati:

| Dato | Descrizione funzionale | Obbligatorio | TIPO | NOTE |
| --- | --- | --- | --- | --- |
| CODICE | Codice tipologia asset | Y | STRING(40) | Es. 1.1.1, 1.1.2, etc.. (in EAM è il codice della classificazione che in Geocall corrisponde al Tipo Impianto) |
| DESCRIZIONE | Descrizione tipologia asset | Y | STRING(80) | Classificazione estesa della tipologia Asset |
| CODICE_GRUPPO | Codice Gruppo. | Y | LONG | Il campo in ambito è un progressivo composto da un numero intero e deve essere lo stesso per tutti i record del medesimo flusso di esportazione (insieme di n chiamate che nel caso specifico corrisponde all’intera anagrafica delle tipologie di impianto). Si precisa quindi che il progressivo in ambito deve essere sempre diverso per ogni esportazione. |
| LAST_RECORD | Last Record: | Y | STRING | Valori ammessi: 0: per tutte le n-1 chiamate della medesima esportazione 1: esclusivamente nell’ultima chiamata dell’esportazione. |
| TIPO_ANAGRAFICA_CLIENTE | Tipo Anagrafica: 1: Utenza 2: Impianto 3: Famiglia (in caso di Stazioni, Linee, Supporto) 4: Proprietario | Y | LONG | Valori ammessi: 3: solo nel caso di Primo Livello (Linea/Stazione). 2: per tutti gli altri Livelli. |
| FAMIGLIA | I valori ammessi sono i seguenti: L per indicare la Linea S per indicare le Stazioni | Y | STRING(20) | I valori ammessi sono i seguenti: L per indicare la Linea S per indicare le Stazioni Si precisa che nel caso in cui vengano trasmessi valori diversi da quelli sopra indicati il task di import Geocall scarterà il relativo tipo impianto |

| Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) |
| --- | --- | --- | --- | --- |
| CAMPO | DESCRIZIONE | OBBLIGATORIO | TIPO | NOTE |
| ESITO | Esito OK/KO | SI | STRING | Valori ammessi: OK, KO |
| CODICE_ERRORE | Codice dell'errore, valorizzato solo in caso di KO | NO | STRING |  |
| DESCRIZIONE_ERRORE | Descrizione dell’errore, valorizzato solo in caso di KO | NO | STRING |  |

### Gestione degli errori

Si prevedono le seguenti tipologie di errori:

Il codice tipo impianto è obbligatorio.

La descrizione per il tipo impianto è obbligatoria.

Il parametro last record è obbligatorio.

Il parametro gruppo è obbligatorio.

Il parametro 'Tipo Anagrafica Cliente' è obbligatorio e deve assumere valore 1, 2, 3 o 4.

Il parametro Famiglia deve assumere come valore 'L' (Linee) 'S' (Stazioni).

## Modifica Anagrafica (Perimetro release 2)

### Descrizione funzionale interfaccia

L’interfaccia consente di inviare le variazioni che subisce l'Anagrafica degli Asset di Linee e Stazioni per fare in modo che Geocall memorizzi la consistenza impiantistica degli Asset di Terna, utile alla gestione dell’Ordine di Lavoro.

### Logiche d’attivazione

Il flusso di integrazione si innesca ogni qual volta l’Anagrafica EAM subisce una modifica e si ha una variazione del set informativo che EAM invia a Geocall. Il servizio sarà invocato indirettamente da EAM attraverso un bus di comunicazione. La logica di attivazione del flusso di integrazione è manuale: l’invio dell’asset creato in EAM verso il sistema Geocall viene triggerato dall’operatività utente sulle applicazioni di gestione anagrafica: avviando le funzionalità di generazione asset (es. Crea asset di secondo livello oppure Inserisci tratta), gli elementi generati verranno veicolati verso Geocall.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | 5000 al mese |
| Timing (Giornaliera, Mensile, etc) | Giornaliero |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |

### Dati scambiati

Il servizio WSDL esposto da Geocall per l’importazione dell’anagrafica impianti è la seguente:

| Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) |
| --- | --- | --- | --- | --- |
| CAMPO | DESCRIZIONE | OBBLIGATORIO | TIPO | NOTE |
| tipoOggetto | Tipo Impianto | S | String | Codice del Tipo Impianto, si precisa che l’anagrafica dei tipi impianto può essere alimentata in Geocall tramite il servizio TipoImpianto o direttamente nell’applicativo Geocall dagli utenti abilitati. Si precisa che eventuali oggetti tecnici che fanno riferimento a tipi impianto non presenti nell’anagrafica Geocall saranno scartati dal task di import in ambito. |
| statoOggetto | Stato Oggetto | S | String | EAM trasmette lo stato dell’asset che dovrà essere ricondotto da geocall ad uno dei due stati “Attivo” o “Non attivo” in base ad un mapping predefinito. (vedi Mapping condiviso) |
| codiceOggetto | Codice Oggetto | S | String | AssetNum (chiave EAM) |
| SAPID | AssetNum | S | String | SAPID (chiave SisnetSAP). Laddove per lo specifico asset non esista un Sap_ID, EAM trasmetterà a Geocall WFM il Sapid del primo asset di livello superiore per il quale l’informazione risulta disponibile (risalendo la gerarchia EAM). |
| SAPID2 | AssetNum | N | String | Campo SAPID (chiave SAP). In presenza di doppia Terna per gli asset che presentano un doppio padre quali campata o fune di guardia, il sistema valorizzerà sia SAP_ID (quello per cui la “radice/1° livello” risulta minore) che SAP_ID2 |
| descrizione | Descrizione | N | String | Descrizione dell’oggetto tecnico (sede tecnica o equipment) |
| codicePadre | Codice Padre | N | String | Informazione relativa al padre dell’oggetto trasmesso, tale informazione è necessaria per la ricostruzione della struttura della consistenza impiantistica. Si precisa che esclusivamente con l’informazione in ambito Geocall andrà a ricostruire la struttura della consistenza impiantistica escludendo pertanto ulteriori logiche. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato |
| codiceRadice | Codice radice | N |  | Informazione relativa alla radice dell’oggetto trasmesso. Necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. |
| codicePadre2 | Codice Padre 2 | N | String | Informazione relativa al secondo padre dell’oggetto trasmesso, tale informazione è necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. |
| codiceRadice2 | Codice Radice 2 | N | String | Informazione relativa al secondo padre dell’oggetto trasmesso, tale informazione è necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. |
| areaLavorazione | Area di Lavoro | NO | String | Informazione trasmessa da EAM ma che non verrà considerata da Geocall in fase di importazione |
| centroDiLavoro | Centro di Lavoro | NO | String | Informazione trasmessa da EAM ma che non verrà considerata da Geocall in fase di importazione |
| filiale | Filiale | S | String | Codice dell’Unità Impianti di riferimento ossia Responsabile per Asset (Centro Operativo in Geocall) |
| indirizzo | Indirizzo | NO | ComplexType |  |
|  |  |  |  |  |
| INDIRIZZO | INDIRIZZO | INDIRIZZO | INDIRIZZO | INDIRIZZO |
| toponimo | Toponimo | NO | String |  |
| via | Via | NO | String |  |
| civico | Civico | NO | String |  |
| località | Località | NO | String |  |
| provincia | Provincia | NO | String |  |
| cap | CAP | NO | String |  |
| comune | Comune | NO | String |  |
| istat | Istat | NO | String |  |
| latitudine1 | Latitudine1 | NO | Double |  |
| longitudine1 | Longitudine1 | NO | Double |  |
| latitudine2 | Latitudine2 | NO | Double |  |
| longitudine2 | Longitudine2 | NO | Double |  |

Si potranno presentare le seguenti casistiche:

| Valorizzazione campi WS | Logica |
| --- | --- |
| codiceOggetto: Linea A codicePadre: NULL codiceRadice: NULL codicePadre2: NULL codiceRadice2: NULL | Tutti i campi che riguardano l’alberatura sono NULL, l’oggetto inviato è una radice (es. Linea A). |
| codiceOggetto: Campata 1-2 codicePadre: Linea A codiceRadice: Linea A codicePadre2: Linea B codiceRadice: Linea B | Tutti i campi che riguardano l’alberatura standard sono valorizzati. Anche il campo del secondo padre è valorizzato, l’oggetto inviato presenta doppio padre e doppia radice. |
| codiceOggetto: Conduttore ABC codicePadre: Campata 1-2 codiceRadice: Linea A codicePadre2: NULL codiceRadice2: NULL | È il caso di un oggetto che presenta un padre con due radici, ma che eredita una sola radice, in particolare, eredita la “prima” radice del padre. |
| codiceOggetto: Fune di guardia codicePadre: Campata 1-2 codiceRadice: Linea A codicePadre2: NULLcodiceRadice2: Linea B | L’oggetto inviato presenta un padre ma due radici. |
| codiceOggetto: Conduttore DEF codicePadre: Campata 1-2 radice: Linea B tipoImpiantoPadre2: NULL codiceRadice2: NULL | È il caso di un oggetto che presenta un padre con due radici, ma che eredita una sola radice, in particolare, eredita la “seconda” radice del padre. Per Geocall il caso è riconoscibile perché la radice passata non corrisponde alla “prima” radice del padre. |

| Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) |
| --- | --- | --- | --- | --- |
| CAMPO | DESCRIZIONE | OBBLIGATORIO | TIPO | NOTE |
| ERR_CODICE | Codice dell'errore | SI | STRING | Codice d’errore di EAM |
| ERR_DESCRIZIONE | Descrizione dell’errore | SI | STRING | Descrizione d’errore |
| ERR_SEVERITY | Severity | SI | STRING | Esito elaborazione: OK o KO |

### Gestione degli errori

| ESITO | CODICE ERRORE | DESCRIZIONE ERRORE | NOTE |
| --- | --- | --- | --- |
| KO | MOD_IMP_001 | Sede tecnica anomalia non specificata per la filiale {{0}} | Quando viene passato un codicePadre che non è presente a sistema, Geocall individua la sede tecnica con tipo impianto “Anomalia” creata ad hoc per ognuna delle unità impianti (filiali). Errore restituito quando la sede tecnica fittizia non è presente. |
| KO | MOD_IMP_002 | Stato impianto non trovato con codice {{0}} | Viene passato uno stato non censito su Geocall. |
| KO | MOD_IMP_003 | Tipo impianto non trovato con codice {{0}} | Viene passato un tipoOggetto non presente su Geocall. |
| KO | MOD_IMP_004 | Latitudine e Longitudine nulle e sede tecnica anomalia non specificata per la filiale {{0}} | Per eventuali impianti trasmessi da EAM senza l’informazione relativa alle coordinate geografiche, in particolare “Latitudine1” e “Longitudine1”, in fase di import Geocall attribuisce le coordinate geografiche della sede tecnica fittizia caratterizzata, come descritto nel primo punto, dal Tipo Impianto “Anomalia”. Errore restituito quando la sede tecnica fittizia non è presente o quando le sue coordinate sono NULL. |
| KO | MOD_IMP_005 | Sede tecnica anomalia non specificata per la filiale {{0}} | Quando viene passato un codicePadre2 che non è presente a sistema, Geocall individua la sede tecnica con tipo impianto “Anomalia” creata ad hoc per ognuna delle unità impianti (filiali). Errore restituito quando la sede tecnica fittizia non è presente. |
| KO | MOD_IMP_006 | Radice errata | Quando viene passato un codiceRadice o codiceRadice2 incoerente con la radice/le radici del padre. |

### Esemplificazione

A titolo esemplificativo – per il caso Doppia Terna:

| Tipologia di asset | Codice Asset | Codice asset padre | Codice asset padre 2 | Codice Radice | Codice Radice 2 |
| --- | --- | --- | --- | --- | --- |
| Campata 1-2 | Assetnum Campata | Assetnum Linea A | Assetnum Linea B | Assetnum Linea A | Assetnum Linea B |
| ConduttoreA/B/C (linea A) | Assetnum Conduttore 1 | Assetnum Campata 1-2 | vuoto | Assetnum Linea A | vuoto |
| Conduttore D/E/F (linea B) | Assetnum Conduttore 2 | Assetnum Campata 1-2 | vuoto | Assetnum Linea B | vuoto |
| Fune di guardia | Assetnum Fune guardia | Assetnum Campata 1-2 | vuoto | Assetnum Linea A | Assetnum Linea B |

A titolo esemplificativo – per il caso Singola Terna:

| Tipologia di asset | Codice Asset | Codice asset padre | Codice asset padre 2 | Codice Radice | Codice Radice 2 |
| --- | --- | --- | --- | --- | --- |
| Campata | Assetnum Campata | Assetnum Linea | vuoto | Assetnum Linea | vuoto |
| Conduttore | Assetnum Conduttore | Assetnum Campata | vuoto | Assetnum Linea | vuoto |
| Fune di guardia | Assetnum Fune guardia | Assetnum Campata | vuoto | Assetnum Linea | vuoto |

Geocall utilizzerà il codiceOggetto trasmesso da EAM per discriminare il caso di nuovo inserimento (codiceOggetto già presente in anagrafica) dal caso di modifica (codiceOggetto già presente in anagrafica)

## Inserimento Anagrafica Massiva (Perimetro release 2)

### Descrizione funzionale interfaccia

Si prevede un flusso di primo infasamento massivo di Anagrafica in cui EAM trasmetterà l’intera base dati di Anagrafica di Linee e Stazioni secondo il nuovo modello gerarchico che prevede quattro livelli gerarchici sia per Linee che per Stazioni.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | Cartella di scambio: Terna metterà a disposizione una cartella condivisa a cui potranno accedere utenti Operation (per ambienti minori di esercizio anche RTI EAM), al fine da permettere a quest’ultimi di depositare i file, e consentire a Geocall la lettura ed il caricamento tramite applicazione. Il server da utilizzare dovrà essere lo stesso sul quale risulta essere installato l’application server di Geocall WFM. Su Geocall WFM sarà parametrizzato il path nei dati fissi, e sarà letto da un task di import. Tale path dovrà essere raggiungibile dalla macchina dove risulta installato l’application server della soluzione Geocall WFM. |
| --- | --- |
| BUS | N.A. |
| Tipo Esposizione | One-shot |
| Frequenza Invocazione | N.A. |
| Timing (Giornaliera, Mensile, etc) | One-shot |
| Modalità Comunicazione | N.A. |
| Rete | Intranet |
| Modalità di Autenticazione | Abilitazioni accesso utenti Accenture per poter depositare files Abilitazione accesso a Geocall WFM; In carico a Terna IT la verifica dei permessi sulle rotte di accesso alla cartella. |
| Protocolli Supportati | Folder |
| Formato Dati Supportati | File in formato csv. |
| Vincoli | I file .csv dovranno essere caratterizzati da un numero massimo di record pari a 500.000. Si precisa che all'interno di uno stesso file dovrà essere presente l'intera struttura di un albero di consistenza pertanto NON sarà consentita l'importazione di un albero suddivisa su più file. Si precisa che anche nel caso di doppia terna, l’intera albero dovrà essere riportato nello stesso CSV. Le radici e i nodi padre dovranno essere riportati nel file in modo ordinato, poiché Geocall elaborerà il file seguendo l'ordine indicato. Ciò garantirà che, nel momento in cui verrà elaborato un nodo figlio, i relativi nodi padre e le radici saranno già stati precedentemente elaborati e importati sul DB di Geocall. L'importazione della struttura in ambiente di produzione sarà schedulato dal sistema un arco temporale all’interno del quale non sarà presente nessun utente collegato (fascia notturna). Tale vincolo sarà necessario per evitare il lock delle tabelle e prevenire situazioni che potrebbero compromettere l'attività di importazione. I medesimi files di tutta la consistenza impiantistica dovranno essere caricati e collaudati in ambiente di collaudo, prima del passaggio in produzione. Tale richiesta è necessaria per poter identificare e risolvere eventuali anomalie nei files prima dell'importazione definitiva in produzione, evitando così problemi durante il go-live. |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati:

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
| tipoOggetto | Classificazione dell'asset | Y |
| statoOggetto | Stato dell’asset | Y |
| codiceOggetto | Codice identificativo dell'asset EAM | Y |
| SAPID | Identificativo SAP | Y |
| SAPID2 | Identificativo SAP 2 | N |
| descrizione | Descrizione dell’oggetto tecnico | N |
| codicePadre | Codice identificativo del padre dell'asset Informazione relativa al padre dell’oggetto trasmesso, tale informazione è necessaria per la ricostruzione della struttura della consistenza impiantistica. | N (Campo non obbligatorio esclusivamente per le radici dei rami come ad esempio le Linee) |
| codiceRadice | Codice radice | N |
| codicePadre2 | Codice Padre2 per asset in doppia terna | N |
| codiceRadice2 | Codice Radice2 per asset in doppia terna | N |
| filiale | Codice dell’Unità Impianti di riferimento | Y |
| toponimo | Salvato ma non utilizzato | N |
| via | Via | N |
| civico | Civico | N |
| localita | Località | N |
| provincia | Provincia | N |
| cap | Cap | N |
| comune | Comune | N |
| istat | Codice ISTAT | N |
| latitudine1 | Coordinata X dell’impianto. Utilizzato per la schedulazione e visualizzazione su mappa. | N |
| longitudine1 | Coordinata Y dell’impianto. Utilizzato per la schedulazione e visualizzazione su mappa. | N |
| latitudine2 | Solo significato informativo. Presenti nel caricamento da csv. | N |
| longitudine2 | Solo significato informativo. Presenti nel caricamento da csv. | N |

### Gestione degli errori

| SEVERITY (Warning o Error) | ESITO | CODICE_ERRORE | DESCRIZIONE_ERRORE | CAUSE |
| --- | --- | --- | --- | --- |
| Error | KO | MOD_IMP_001 | Sede tecnica anomalia non specificata per la filiale {{0}} | Quando viene passato un codicePadre che non è presente a sistema, Geocall individua la sede tecnica con tipo impianto “Anomalia” creata ad hoc per ognuna delle unità impianti (filiali). Errore restituito quando la sede tecnica fittizia non è presente. |
| Error | KO | MOD_IMP_002 | Stato impianto non trovato con codice {{0}} | Viene passato uno stato non censito su Geocall. |
| Error | KO | MOD_IMP_003 | Tipo impianto non trovato con codice {{0}} | Viene passato un tipoOggetto non presente su Geocall. |
| Error | KO | MOD_IMP_004 | Latitudine e Longitudine nulle e sede tecnica anomalia non specificata per la filiale {{0}} | Per eventuali impianti trasmessi da EAM senza l’informazione relativa alle coordinate geografiche, in particolare “Latitudine1” e “Longitudine1”, in fase di import Geocall attribuisce le coordinate geografiche della sede tecnica fittizia caratterizzata, come descritto nel primo punto, dal Tipo Impianto “Anomalia”. Errore restituito quando la sede tecnica fittizia non è presente o quando le sue coordinate sono NULL. |
| Error | KO | MOD_IMP_005 | Sede tecnica anomalia non specificata per la filiale {{0}} | Quando viene passato un codicePadre2 che non è presente a sistema, Geocall individua la sede tecnica con tipo impianto “Anomalia” creata ad hoc per ognuna delle unità impianti (filiali). Errore restituito quando la sede tecnica fittizia non è presente. |
| Error | KO | MOD_IMP_006 | Radice errata | Quando viene passato un codiceRadice o codiceRadice2 incoerente con la radice/le radici del padre. |

### Logiche di interscambio su Geocall

Solo per l’infasamento iniziale è previsto l’adeguamento dell’attuale flusso di “Modifica Anagrafica Massiva” via CSV per inviare a Geocall WFM le nuove chiavi di anagrafica di EAM e relativi SapId e coordinate. Pertanto, verrà utilizzata/modificata la funzionalità di import disponibile su Geocall WFM, adattandola alla nuova anagrafica di EAM. Di seguito si riportano le logiche con cui deve essere popolato il file da parte di EAM.

Nota Bene:

il file dovrà contenere prima tutti gli impianti di livello 1 e a seguire 2, 3, 4…;

il campo codiceOggetto dovrà essere popolato con il campo AssetNum;

il file csv fornito da EAM verrà caricato da Geocall ed elaborato da un task che si occuperà di rinominare gli oggetti ed eventualmente aggiornare i dati e l’alberatura;

le obbligatorietà dei campi rimarranno le stesse del web service “ModificaImpianto”

il flusso di inizializzazione anagrafica sarà lanciato a valle dell’acquisizione in EAM delle anagrafiche SISNET comprensive dei dati delle coordinate geografiche

## Flusso Invio OdL a GeoCall (Perimetro release 2)

### Descrizione funzionale interfaccia

Servizio esposto da Geocall e richiamato da EAM per creare o modificare un OdL sul sistema GeoCall. Il primo invio finalizzato alla creazione dell’OdL in Geocall avverrà tramite selezione manuale/automatica di uno o più OdL EAM che sono in stato “Schedulabile”. Successivamente, tramite il medesimo flusso EAM potrà trasmettere l’aggiornamento di alcune informazioni sugli OdL non inviate a Geocall precedentemente (es. indisponibilità).

### Logiche d’attivazione

La logica di attivazione dell’invio dell’OdL a Geocall è manuale poiché attivata tramite pulsante dall’operatore dopo aver concluso le attività di pianificazione tecnica ed economica. L’operatore invia l’OdL a Geocall in stato “Schedulabile”

Inoltre, l’interfaccia può scattare in modo automatico a seguito della modifica di un OdL già inviato a Geocall in queste casistiche, con il vincolo che l’OdL si trovi in stato Schedulabile o Schedulato (I restanti stati non permettono la modifica di alcun campo):

valorizzazione/modifica del Codice Delibera Indisponibilità (a seguito della delibera verranno inviate le date di inizio e fine indisponibilità e il codice della delibera).

valorizzazione/modifica della Data Inizio Indisponibilità e/o Data Fine Indisponibilità (anche se l’indisponibilità non risulta deliberata)

Ogni qual volta un viene eseguita un’attività da parte dell’utente o dal sistema Agile che può modificare uno dei suddetti campi, in automatico EAM invocherà l’Api di Invio OdL di Geocall per allineare il sistema.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 1.000 al giorno |
| Timing (Giornaliera, Mensile, etc) | Su Richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati:

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
| CODICE_ORDINE_ESTERNO | Codice identificativo dell'ordine di lavoro. Se odl aggregato corrisponde al CODICE_CLUSTER | Y |
| CODICE_CATALOGO | Codice identificativo dell'attività (ID del catalogo attività presente in EAM) | Y |
| DESCRIZIONE_ATTIVITA | Descrizione attività | N |
| <TITRIS> | Lista delle UI. Servirà come indicazione per identificare a quale risorsa potrà essere assegnato l'OdL (sulla base di appartenenza all'UI) | N |
| <UNITA_IMPIANTI> | Lista delle UI e delle risorse a cui potrà essere assegnato l’OdL | Y |
| COD_UNITA_IMPIANTI | Codice dell’UI | N |
| <RISORSE> | Lista delle risorse | N |
| COD_INQUADRAMENTO | Codice dell’inquadramento delle risorse a cui potrà essere assegnato l’OdL | N |
| NUMERO_RISORSE | Numero di risorse | N |
| </RISORSE> |  |  |
| </UNITA_IMPIANTI> |  |  |
| </TITRIS> |  |  |
| DATA_TOLL_SX | Data minima di schedulazione, vincolante in fase di schedulazione (data “inizia non prima di”) | N |
| DATA_TOLL_DX | Data massima di schedulazione, vincolante in fase di schedulazione. (data “massimo posticipo attività) | N |
| PRIORITA | Priorità OdL (informativo per schedulazione manuale) | N |
| COD_SEDE_TECNICA | Codice Asset su cui effettuare manutenzione (assetnum EAM) | Y |
| CODICE_CLUSTER | ID dell’ordine di lavoro. Popolato solo nel caso di OdL aggregato/multi-operazione | N |
| TIPO_SPESA | Tipo spesa | Y |
| CODICE_DELIBERA_INDISPONIBILITA | Codice delibera indisponibilità | N |
| DATA_INIZIO_ATT | Data inizio pianificata | N |
| DATA_FINE_ATT | Data fine pianificata | N |
| STATO_DELIBERA | Stato della delibera d’indisponibilità | N |
| DATA_INIZIO_INDISPONIBILITA | Data di inizio indisponibilità | N |
| DATA_FINE_INDISPONIBILITA | Data di fine indisponibilità | N |
| FLAG_INDISPONIBILITA | Se il campo è valorizzato, l’attività potrebbe avere un’indisponibilità | N |
| <OPERAZIONE> | Nodo contenente tutti gli ODL figli legati all'odl di testata | Y |
| COD_POSIZIONE | Codice dell'ordine di lavoro. Se non è un OdL aggregato, sarà pari al codice ordine esterno. Se il codice ordine esterno è un odl aggregato, sarà presente il singolo OdL figlio in codice posizione. | Y |
| DATA_TOLL_SX | Data minima di schedulazione | N |
| DATA_TOLL_DX | Data massima di schedulazione | N |
| DATA_INIZIO_ATT | Data inizio pianificata | N |
| DATA_FINE_ATT | Data fine pianificata | N |
| PROPEDEUTICITA |  | N |
| DURATA_OP | Durata dell'attività | N |
| COD_SEDE_TECNICA | Codice Asset su cui effettuare manutenzione | Y |
| SAP_ID | Codice SAPID (chiave SAP) dell’asset di riferimento dell’OdL Casistica di asset con Doppio Padre: In caso di ispezione, verrà visualizzato il SAP_ID specifico della campata in relazione alla linea di riferimento (sui figli/operazioni) e l’informazione sarà recuperata in base al Tronco di ispezione (per gli OdL aperti a livello di linea,es. LIDAR, l’informazione è recuperata dall’asset di riferimento dell’OdL Padre) Negli altri casi, tra i due SAP_ID possibili per l’asset, si prenderà quello per cui il SAP_ID “radice/1° livello” risulta minore (facendo riferimento ai primi 7 digit del SAP_ID) Casistica di asset con Unico padre: Va inserito il SapID corrispettivo dell’Asset,se per l’Asset non è presente un SAP_ID (elemento presente in EAM ma non in SAP) si risale la gerarchia EAM e si riporta il SAP_ID del primo asset che contempla l’informazione | Y |
| </OPERAZIONE> |  |  |

Di seguito la struttura di risposta:

| ESITO | Esito invio vs Geocall (OK, KO) | Y |
| --- | --- | --- |
| CODICE_ERRORE | Codice d’errore Geocall | N |
| DESCRIZIONE_ERRORE | Descrizione d’errore Geocall | N |

### Gestione degli errori

Il flusso restituirà un errore in caso:

L'identificativo univoco dell’asset inviato non è presente in GeoCall

L'identificativo univoco dell’asset inviato nelle operazioni non è presente in GeoCall

Codice posizione non univoco

## Annullamento/Blocco Ordini di Lavoro (Perimetro release 1)

### Descrizione funzionale interfaccia

Questa interfaccia permette di interrompere il ciclo di esecuzione dell’OdL in Geocall impostando lo stato ANNULLATO (stato conclusivo per l’OdL Geocall).

L’API lavora sull’intero OdL Geocall e comporta quindi l’annullamento sia dell’OdL che di tutte le operazioni sottese.

La richiesta di annullamento dell’OdL su Geocall sarà attivata da EAM tramite l’apposita funzionalità di cambio stato che riporterà l’OdL in stato Aperto su EAM e contestualmente effettuerà la richiesta di annullamento dell’ordine su Geocall.

La suddetta funzionalità sarà abilitata in EAM solo se l’OdL si trova in stato SCHEDULABILE, SCHEDULATO (quindi prima che l’odl risulti trasferito al mobile Geocall) e risulterà inibita se è presente almeno un consuntivo inviato verso SAP con esito positivo.

In ottica di processo, l’annullamento di un ordine di lavoro Geocall, invocato da EAM, è funzionale all’attività di ripianificazione dell’ordine: una volta riportato l’OdL EAM in stato aperto e annullato l’OdL in Geocall, sarà possibile ripianificare l’ordine in EAM e inviarlo nuovamente a Geocall per poter procedere con la schedulazione. Si precisa che in Geocall verrà creato un nuovo OdL con un nuovo ID Geocall ma che manterrà il medesimo codice esterno EAM del precedente.

### Logiche d’attivazione

La richiesta di annullamento dell’OdL su Geocall sarà attivata quando l’utente EAM esegue la funzionalità di cambio stato, tramite il pulsante “Ripianifica OdL” per passare l’OdL EAM da Schedulato/Schedulabile ad Aperto. A seguito del passaggio dell’OdL EAM in stato Aperto, il sistema EAM genererà un evento che provvederà ad invocare l’Api di Annulla/Blocco OdL di Geocall.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 2.000 al mese |
| Timing (Giornaliera, Mensile, etc) | Su Richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati:

| Dato | Descrizione funzionale | Obbligatorio | TIPO | NOTE |
| --- | --- | --- | --- | --- |
| CODICE_ORDINE_ESTERNO | Identificativo Ordine di lavoro | Y | STRING |  |
| CODICE_CLUSTER | Identificativo Cluster (coincide con il CODICE_ORDINE_ESTERNO; sempre NULL in quanto non necessario) | N | STRING |  |
| TIPO_OPERAZIONE | Tipo di operazione: A: Annullamento B: Blocco (non verrà gestita lato GeoCall) | Y | STRING |  |

| Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) |
| --- | --- | --- | --- | --- |
| CAMPO | DESCRIZIONE | OBBLIGATORIO | TIPO | NOTE |
| ESITO | Esito invio vs Geocall (OK, KO) | Y | STRING |  |
| CODICE_ERRORE | Codice d’errore EAM | N | STRING | valorizzato solo in caso di KO |
| DESCRIZIONE_ERRORE | Descrizione d’errore EAM | N | STRING | valorizzato solo in caso di KO |

### Gestione degli errori

Il flusso restituirà un errore in caso:

Lo stato dell'OdL non consente l'annullamento

Codice ordine di lavoro non presente

Codice cluster non presente

Tipo operazione non ammessa

## Annulla Operazione (Perimetro release 1)

### Descrizione funzionale interfaccia

Lo scopo del flusso è quello di annullare una singola operazione di un OdL già presente su GeoCall. È possibile che una singola attività sottesa ad un OdL aggregatore possa essere annullata senza che questo implichi l’annullamento dell’intero OdL. Si precisa che se il servizio in ambito dovesse essere invocato da EAM per un OdL mono operazione e l’operazione è in uno stato che permette l’annullamento il sistema annulla l’operazione e di conseguenza l’ordine.

EAM inibirà la possibilità di annullare un OdL che presenta almeno un consuntivo inviato con esito positivo verso SAP.

In ottica di processo, l’annullamento dell’operazione in Geocall (stato conclusivo per Geocall) abilita il successivo re-invio dell’ordine “annullato”: una volta riportato in stato aperto l’Odl figlio su EAM ed annullata la corrispondente operazione su Geocall, sarà possibile inoltrare a Geocall l’OdL figlio precedentemente “annullato” sia singolarmente che in associazione ad un nuovo aggregato.

Si precisa che in Geocall verrà creato un Nuovo OdL (se inviato come Ordine singolo) o una nuova Operazione (se inviato come elemento di un Ordine multi-operazione) con un nuovo ID Geocall ma che manterrà il medesimo codice esterno EAM del precedente.

### Logiche d’attivazione

La richiesta di annullamento di un’operazione su Geocall sarà attivata quando l’utente EAM richiama la funzionalità di sgancio di un’odl figlio da un aggregatore tramite apposito pulsante. A fronte di quest’attività, verrà generato un evento da EAM che provvederà ad invocare l’Api di Annulla Operazione OdL di Geocall per allineare il sistema.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 100 al mese |
| Timing (Giornaliera, Mensile, etc) | Su Richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Http |
| Formato Dati Supportati | XML |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati:

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
| CODICE_ORDINE_ESTERNO | Identificativo OdL aggregatore | Y |
| CODICE_POSIZIONE | Identificativo codice posizione (i.e. codice OdL figlio EAM) | Y |
| TIPO_OPERAZIONE | Tipo di operazione. Valori possibili: A: Annullamento | Y |

Di seguito la struttura di risposta:

| Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) | Response Message (Geocall → EAM) |
| --- | --- | --- | --- | --- |
| CAMPO | DESCRIZIONE | OBBLIGATORIO | TIPO | NOTE |
| ESITO | Esito invio vs Geocall (OK, KO) | Y | STRING |  |
| CODICE_ERRORE | Codice d’errore Geocall | N | STRING | valorizzato solo in caso di KO |
| DESCRIZIONE_ERRORE | Descrizione d’errore Geocall | N | STRING | valorizzato solo in caso di KO |

### Gestione degli errori

Il flusso restituirà un errore in caso:

Lo stato della testata dell'OdL con codice esterno xyz non consente l'annullamento.

Codice ordine di lavoro non presente

Codice posizione non presente

Tipo operazione non ammessa

## Ricezione Schedulazione OdL (Perimetro release 1)

### Descrizione funzionale interfaccia

Quando in Geocall viene convalidato il Piano giornaliero e di conseguenza vengono confermate le date di inizio/fine schedulazione dell’OdL/operazioni sottese e le matricole del personale a cui è stato assegnato l’OdL, viene innescato il flusso di “Ricezione Schedulazione OdL” che permetterà di allineare i due sistemi in termini di schedulazione attività e risorse.

Questo flusso potrà essere scatenato sia in caso di prima schedulazione dell’OdL che in caso di rischedulazione.

CASO PRIMA SCHEDULAZIONE

In caso di ordine multi-operazione, questo flusso implica lato EAM l’avanzamento di stato degli OdL “figli”, corrispondenti alle operazioni specificate da Geocall, da Schedulabile a Schedulato

In caso di Ordine mono-operazione questo flusso implica lato EAM l’avanzamento di stato dell’OdL da Schedulabile a Schedulato

CASO RISCHEDULAZIONE

Nel caso di rischedulazione dell’ordine multi-operazione, questo flusso verrà nuovamente evocato per le operazioni sottostanti l’OdL aggregatore da rischedulare. Lo stato dell’ordine di lavoro EAM non varierà a seguito dell’evocazione di questo flusso; verranno tuttavia aggiornate le date di schedulazione. Si precisa che questo vale sia nel caso di ordini già schedulati che vengono poi resi schedulabili (Ordini in stato “FISSATO” ed “INOLTRATO”), che nel caso in cui venga rischedulato un OdL in stato “DA RISCHEDULARE” o “PARZIALMENTE CONSUNTIVATO”.

Nel caso di ordine mono-operazione, questo flusso d’integrazione verrà nuovamente evocato per l’unica operazione dell’ordine a seguito del processo di schedulazione in Geocall (dopo che verrà “fissata” all’interno del piano giornaliero). Lo stato dell’ordine di lavoro EAM non varierà a seguito dell’evocazione di questo flusso; verranno tuttavia aggiornate le date di schedulazione.

### Logiche d’attivazione

Il servizio sarà invocato indirettamente da GeoCall attraverso un bus di comunicazione. Geocall invocherà EAM una volta che il piano giornaliero verrà convalidato (e le operazioni sottese al piano di lavoro passeranno in stato “FISSATO”).

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | 15000 al mese |
| Timing (Giornaliera, Mensile, etc) | Ogni minuto |
| Modalità Comunicazione | Sincrono |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati:

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
| codiceOrdineEsterno | Codice dell'ordine di lavoro, corrisponde al codice wonum di EAM. Nel caso di ordine di lavoro aggregato/multi operazione coincide con il codice del cluster (OdL padre in EAM) | Y |
| Preposto | codice identificativo dell’utente - matricola del preposto (la matricola del proposto NON è presente all’interno dell’array “Squadra” ma va considerato come risorsa assegnataria) | Y |
| <Squadra> | Array delle matricole a cui viene assegnato l’OdL |  |
| Matricole | Lista ID Matricole a cui è stato assegnato l'ODL | Y |
| </Squadra> |  |  |
| <Operazioni> | Array delle operazioni sottese al codice ordine esterno | Y |
| codicePosizione | Codice dell'ordine di lavoro, corrisponde al codice wonum di EAM. Nel caso di ordine mono operazione coincide con il codiceOrdineEsterno - nel caso di ordine multi-operazione riporta il wonum della singola operazione (Odl figlio) | Y |
| dataIniSchedulata | Data di inizio schedulata | Y |
| dataFineSchedulata | Data di fine schedulata | Y |
| </Operazioni> |  |  |

Di seguito la struttura di risposta:

| Response Message (EAM → Geocall) | Response Message (EAM → Geocall) | Response Message (EAM → Geocall) | Response Message (EAM → Geocall) | Response Message (EAM → Geocall) |
| --- | --- | --- | --- | --- |
| CAMPO | DESCRIZIONE | OBBLIGATORIO | TIPO | NOTE |
| Esito | Esito elaborazione: OK o KO | Y | STRING |  |
| codiceErrore | Codice d’errore di EAM | N | STRING | valorizzato solo in caso di KO |
| descrizioneErrore | Descrizione d’errore | N | STRING | valorizzato solo in caso di KO |

### Gestione degli errori

Il flusso restituirà un errore in caso:

Codice ordine di lavoro non presente in EAM

Codice matricola non presente in EAM

Stato veicolato NON consentito in EAM (rispetto al flusso degli stati dell’ordine).

## Ricezione Consuntivazione Ore (Perimetro release 1)

### Descrizione funzionale interfaccia

Il flusso Ricezione consuntivazione OdL Ore ha lo scopo di inviare a EAM l’avanzamento della consuntivazione oraria dell’OdL. Il flusso invierà ad EAM i consuntivi parziali/totali delle ore lavorate sugli OdL con l’associazione delle matricole che hanno effettuato l’attività e le date di esecuzione.

### Logiche d’attivazione

Il servizio SOAP, esposto da EAM, verrà richiamato da Geocall per notificare le ore consuntivate dalle singole risorse sulle operazioni di un ordine di lavoro.

Il servizio sarà invocato indirettamente da GeoCall attraverso un bus di comunicazione.

Il flusso verrà attivato a seguito della validazione in Geocall dei consuntivi orari.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | 30000 al mese |
| Timing (Giornaliera, Mensile, etc) | Ogni minuto |
| Modalità Comunicazione | Sincrono |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati:

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
| codiceOrdineEsterno | Codice identificativo dell'ordine di lavoro (singola operazione) | Y |
| <Consuntivazione> | Nodo di consuntivazione sull'ordine di lavoro. Elemento di ComplexType di molteplicità N | Y |
| IdConsuntivo | ID univoco Geocall del consuntivo | Y |
| matricola | Matricola operatore | Y |
| dataIniEffettiva | Data ed ora di inizio effettiva lavorazione | Y |
| dataFineEffettiva | Data ed ora di fine lavorazione | Y |
| ore | Ore lavorate | Y |
| oreUfficio | Lavoro di preparazione attività svolte in ufficio | N |
| tempoDiTrasferimento | Tempo consuntivato per il trasferimento dell’operaio | N |
| </Consuntivazione> |  |  |

| Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) |
| --- | --- | --- | --- | --- |
| DESCRIZIONE | OBBLIGATORIO | TIPO | NOTE | DESCRIZIONE |
| Esito | Esito elaborazione: OK o KO | Y | STRING |  |
| codiceErrore | Codice d’errore di EAM | N | STRING | valorizzato solo in caso di KO |
| descrizioneErrore | Descrizione d’errore | N | STRING | valorizzato solo in caso di KO |

### Gestione degli errori

Il flusso restituirà un errore in caso:

Codice ordine di lavoro non presente in EAM

Codice matricola non presente in EAM

Date di consuntivazione comunicate nel messaggio nel futuro rispetto alla data di sistema

## Notifica Cambio Stato (Perimetro release 1)

### Descrizione funzionale interfaccia

Il flusso Notifica Cambio Stato ha lo scopo di inviare a EAM il cambio stato per l’OdL o per le singole operazioni (nel caso di ordine multi-operazione). A valle di questa notifica EAM aggiornerà con opportune trasformazioni lo stato su Maximo.

### Logiche d’attivazione

Il servizio SOAP sarà esposto da EAM e sarà invocato indirettamente da GeoCall attraverso un bus di comunicazione.

Geocall invocherà EAM ad ogni cambio di stato, in particolare:

per le operazioni:

Schedulabile

Inoltrat

In Carico

Chius

per l’OdL:

Schedulabile

Inviat

In Carico

Chiuso Tecnicamente

Chius

Parzialmente Consuntivato

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | 30000 al mese |
| Timing (Giornaliera, Mensile, etc) | Ogni minuto |
| Modalità Comunicazione | Sincrono |
| Rete | Intranet |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati:

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
| Codice cluster | ID dell’ordine di lavoro. Popolato solo nel caso di OdL aggregato/multi-operazione | N |
| Codice posizione | Codice dell'ordine di lavoro | Y |
| Stato Operazione | Stato che assume l’operazione in Geocall (In carico, Inoltrato, Chiuso, Schedulabile) | Y |
| causaleEsito | Causale dell’esito di chiusura dell’OdL figlio, specificato solo nello stato “Chiuso”.(ATTIVITA ESEGUITA, NON ESEGUITA) | N |
| Data di cambio stato | Data esecuzione per il cambio di stato | Y |
| Stato Ordine | Stato che assume l’ordine in Geocall, i possibili stati sono: SCHEDULABILE, INVIATO, IN CARICO, CHIUSO, CHIUSO TECNICAMENTE, PARZIALMENTE CONSUNTIVATO | Y |
| notaChiusura | Campo stringa contenente le note di chiusura. | N |

| Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) |
| --- | --- | --- | --- | --- |
| DESCRIZIONE | OBBLIGATORIO | TIPO | NOTE | DESCRIZIONE |
| Esito | Esito elaborazione: OK o KO | Y | STRING |  |
| codiceErrore | Codice d’errore di EAM | N | STRING | valorizzato solo in caso di KO |
| descrizioneErrore | Descrizione d’errore | N | STRING | valorizzato solo in caso di KO |

### Gestione degli errori

Il flusso restituirà un errore in caso:

Codice ordine di lavoro non presente in EAM

Stato non coincidente con nessuno stato predisposto in EAM

## Acquisizione geolocalizzazione SISNET (Perimetro release 2)

### Descrizione funzionale interfaccia

Il flusso Acquisizione geolocalizzazione SISNET ha lo scopo di inviare ad EAM le eventuali modifiche ed aggiornamenti dei dati di geolocalizzazione degli Asset.

### Logiche d’attivazione

Il servizio SOAP sarà esposto da EAM per permettere a SISNET di aggiornare le informazioni di geolocalizzazione sugli Impianti presenti nella banca dati EAM. Qualora il SAPID non sia presente su EAM, bisognerà salvarli su una tabella di staging dedicata che verrà usata per inviare a Geocall i dati dell’anagrafica una volta che gli stessi saranno riconciliati.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 1.000 al giorno |
| Timing (Giornaliera, Mensile, etc) | Su Richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | OAuth2 |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |

### Dati scambiati

Questa interfaccia consente di trasferire i seguenti dati:

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
| tipoOggetto | Tipo Oggetto | N |
| statoOggetto | Stato Oggetto | N |
| codiceOggetto | Codice dell’oggetto, corrisponde al SAPID | N |
| codicePadre | Codice padre dell’oggetto, corrisponde al SAPID del padre | N |
| descrizione | Descrizione | N |
| areaLavorazione | Area di Lavorazione | N |
| centroDiLavoro | Centro di Lavoro | N |
| filiale | Filiale | N |
| INDIRIZZO |  | N |
| toponimo | Toponimo | N |
| via | Via | N |
| civico | Civico | N |
| localita | Località | N |
| provincia | Provincia | N |
| cap | CAP | N |
| comune | Comune | N |
| istat | ISTAT | N |
| latitudine1 | Latitudine | N |
| longitudine1 | Longitudine | N |
| latitudine2 | Latitudine 2 | N |
| longitudine2 | Longitudine 2 | N |
| CARATTERISTICHE |  | N |
| descrizione | Descrizione (Non sarà mai valorizzato) | N |
| valore | Valore (Non sarà mai valorizzato) | N |

### Gestione degli errori

In quanto l’interfaccia non prevede una struttura di ben definita di risposta, non sono previsti errori specializzati. Per questa interfaccia EAM invierà solo una SOAP Fault.

## Storno (Perimetro release 2)

### Descrizione funzionale interfaccia

Il servizio esposto da Geocall sarà richiamato da EAM per richiedere l’azzeramento di uno specifico consuntivo (storno parziale) o la chiusura dell’OdL “per storno” (storno totale) in Geocall.

La richiesta di storno sarà sottomessa dall’utente in EAM (i.e. responsabili delle UdB) e sarà abilitata solo per OdL che hanno attività di tipo Opex.

Sono presenti ulteriori vincoli a seconda se lo storno è di tipo parziale (Singolo consuntivo orario per una singola risorsa) o totale (Storno del fisico e di tutti i consuntivi orari precedentemente caricati):

Storno Parziale:

Il consuntivo da stornare deve risultare già validato.

Storno Totale:

Stato OdL diverso da “ANNULLATO”.

Per l’OdL non devono essere presenti difformità già validate.

Lo storno parziale non comporta un cambio di stato dell’OdL, ma esclusivamente l’azzeramento del consuntivo orario, mentre lo Storno totale oltre all’azzeramento dei consuntivi orari e allo storno del fisico comporta la regressione dell’OdL in stato “aperto”.

In caso di OdL aggregato lo storno totale viene effettuato a livello di OdL padre e implica lo storno ore/storno del fisico per tutti OdL figli.

### Logiche d’attivazione

La soluzione individuata prevede la richiesta di Storno effettuata su EAM. Di seguito breve descrizione del flusso suddiviso tra parziale o totale:

Parziale:

Da Interfaccia Maximo, l’utente seleziona uno specifico consuntivo ore di una specifica risorsa e richiama la funzionalità di storno parziale

EAM in modalità asincrona provvederà ad invocare SAP. La logica che EAM deve implementare prevede preliminarmente l’invocazione del servizio SAP (ZMOS_WS_RESEARCH_COUNTER_ODM) al fine di recuperare il numero di conferma e il contatore progressivo che identificano la riga di consuntivo caricata sull’OdM. Successivamente verrà invocato il vero e proprio servizio di storno (ZMOS_WS_REVERSAL_HOURS_ODM) passando a SAP i dati numero di conferma e il contatore precedentemente recuperati.

A valle delle invocazioni delle API di SAP, solo se concluse con esito positivo, verrà eseguito lo storno ore su EAM e successivamente invocato il servizio di richiesta di storno del consuntivo orario a Geocall.

Totale:

Da Interfaccia Maximo, l’utente seleziona uno specifico OdL e richiama la funzionalità di storno totale

In presenza di consuntivi orari già inviati a SAP verrà invocato il servizio di storno (ZMOS_WS_REVERSAL_HOURS_ODM). Se l’elaborazione non ha esito positivo, l’operazione di storno viene interrotta. In caso contrario viene eseguito su EAM:

storno delle ore

storno del fisico

regressione dell’OdL in stato “Aperto”.

Al termine verrà invocato Geocall per avviare la richiesta di storno totale. Lato Geocall l’ordine verrà portato in stato “CHIUSO PER STORNO” e attenderà da EAM l’invocazione del servizio di creazione OdL per la generazione del nuovo ordine da creare in stato “SCHEDULABILE” (Avrà nuovo ID GeoCall ma con lo stesso ID presente su EAM).

Nel caso in cui non sono presenti i consuntivi orari, EAM non invoca SAP ed effettua solamente lo storno sul fisico e la regressione dell’OdL in stato “Aperto”. Successivamente EAM invocherà Geocall per richiedere lo storno totale.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso:

| Protocollo | SOAP |
| --- | --- |
| BUS | OSB |
| Tipo Esposizione | Web Service |
| Frequenza Invocazione | < 1.000 al giorno |
| Timing (Giornaliera, Mensile, etc) | Su Richiesta |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Basic |
| Protocolli Supportati | Https |
| Formato Dati Supportati | XML |

### Dati scambiati

Di seguito verrà presentato il tracciato tra EAM e Geocall per la notifica dello storno:

Request:

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
| IdConsuntivo | ID univoco Geocall del consuntivo | N (obbligatorio per lo storno parziale) |
| Matricola | Matricola dell’utente riferita al consuntivo | N (obbligatorio per lo storno parziale) |
| TpStorno | Tipologia storno (totale = T; parziale= P) | Y |
| codiceOrdineEsterno | Ordine di lavoro | Y |

Response:

| Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) | Request Message (EAM→ Geocall) |
| --- | --- | --- | --- | --- |
| DESCRIZIONE | OBBLIGATORIO | TIPO | NOTE | DESCRIZIONE |
| Esito | Esito ricezione storno da Geocall (OK, KO) | Y | STRING |  |
| codiceErrore | Codice d’errore di Geocall | N | STRING | valorizzato solo in caso di KO |
| descrizioneErrore | Descrizione esito storno ricevuto da Geocall | N | STRING | valorizzato solo in caso di KO |

### Gestione degli errori

…

Codice OdL non esistente in GeoCall.

ID Consuntivo non esistente in GeoCall.

Matricola non esistente in Geocall.
