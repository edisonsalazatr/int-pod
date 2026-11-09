# EAM_Analisi Specifiche Tecniche_Sprint6_V1.4

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Specifica Tecnica – Sprint 6 |

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
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

# Glossario

| Termine | Definizione |
| --- | --- |
| Piano di Lavoro (PdL) | Oggetto Maximo utilizzato per modellare l’attività del catalogo |
| Ordine di Lavoro (OdL) | Oggetto Maximo utilizzato per modellare le richieste di lavoro da eseguire |
| Configurazione | Implementazione Standard sull’applicativo IBM Maximo |
| Sviluppo | Implementazione Custom sull’applicativo IBM Maximo |
| Configurazione Database | Applicazione che consente di creare, modificare e gestire tabelle, attributi, indici e relazioni all'interno del database di Maximo. Qui èpossibile definire la struttura del database e configurare i dati in modo da adattarsi alle specifiche esigenze dell'organizzazione |
| Application Designer | Applicazione che consente di creare e personalizzare l'interfaccia grafica delle applicazioni presenti in Maximo. Gli utenti possono aggiungere tabelle, sezioni e campi per adattare l'aspetto e il comportamento delle applicazioni in base alle specifiche esigenze aziendali |
| Conditional Expression Manager | Applicazione che permette di creare e modificare espressioni in linguaggio SQL utili per gestire le regole di visibilità e/o editabilità di determinati oggetti all'interno di Maximo |
| Escalation | Le escalation in IBM Maximo vengono utilizzate per automatizzare attività e monitorare record sensibili al tempo. Quest’ultime possono essere attivate quando un record raggiunge un punto di escalation definito. I punti di escalation rappresentano le condizioni che devono essere soddisfatte |
| Script di Automazione | Applicazione che permette di creare script in diversi linguaggi di programmazione al fine di automatizzare processi, eseguire calcoli complessi e personalizzare il comportamento delle applicazioni |
| Relazione | Le relazioni consentono di stabilire collegamenti tra più tabelle al fine di recuperare specifici dati mediante l'utilizzo di clausole "WHERE" all'interno di un'istruzione SQL. Questa funzionalità permette di definire una tabella di destinazione, chiamata "Figlio", sulla quale viene eseguita la clausola "WHERE", mentre la tabella di partenza è chiamata "Padre" |
| Relazione.Attributo | Convenzione utilizzata in Application Designer per fare riferimento a un attributo specifico di una tabella nel database di Maximo relazionata a quella corrente |
| Sigoption | Le sigoption (opzione di firma) permettono di specificare i privilegi per l’utilizzo di applicazioni, opzioni di menu ed elementi della barra degli strumenti. Inoltre, esistono sigoption condizionali che consentono di concedere o revocare dinamicamente i privilegi di un utente in base a espressioni condizionali. |
| MX Loader | Foglio excel che consente di: Caricare dati in Maximo Scaricare determinati dati presenti nel DB Maximo, tramite l’impostazione di opportune query all’interno del file |
| MPE | Motore di pianificazione euristico. Rappresenta lo strumento di ottimizzazione delle date di pianificazione. |

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche, già descritte ad alto livello nel documento “EAM_Analisi Funzionale_Sprint_6_v2.0.docx”, verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software.

Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni implementate sulla piattaforma EAM di Terna (ambiente Dev) e gli sviluppi custom da implementare. Ciascun paragrafo (dedicato a una specifica funzionalità nell’applicativo) presenta due sotto-paragrafi: nel primo sono illustrate le configurazioni eseguite sul software (e.g., estensione del modello dati, configurazione dell’applicazione, gestione della condizionalità, etc.), nel secondo gli sviluppi necessari per soddisfare le funzionalità richieste durante lo sprint.

In particolare, i temi inclusi nello Sprint 6 sono i seguenti:

Motore di pianificazione euristico

Creazione manuale dell’OdL aggregatore

Assegnazioni responsabilità e attività consentite

Requisiti puntuali.

# Sprint 6: Ottimizzatore pianificazione OdL

## Motore di pianificazione euristico

Il Motore di Pianificazione Euristico (MPE) è una funzionalità che permette una pianificazione temporale di massima degli ordini di lavoro all’interno di un intervallo di tempo definito dall’utente, in coerenza con i vincoli di esecuzione delle singole attività e di capacity disponibile.

Configurazioni

Per la gestione dei parametri di ottimizzazione del MPE è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| X4MPE | INIZIOPEROTT | Inizio periodo da ottimizzare |  |  |
| X4MPE | FINEPEROTT | Fine periodo da ottimizzare |  |  |
| X4MPE | UDB | Unità di Budget | UPPER | 10 |
| X4MPE | X4MPEID |  | BIGINT |  |
| X4MPEODL | WONUM | ID OdL | UPPER | 8 |
| X4MPEODL | SITEID | Site id | UPPER | 8 |
| X4MPEODL | X4MPEID | ID MPE | BIGINT |  |
| WORKORDER | X4PIANMPE | Pianificato MPE | UPPER | 2 |
| WORKORDER | X4DATAULTIMAPIAN | Data ultima pianificazione | DATETIME |  |
| X4MPEODL | DATAINIZIOPROP | Data inizio proposta dal MPE | DATETIME |  |
| X4MPEODL | DATAFINEPROP | Data fine proposta dal MPE | DATETIME |  |
| CLASSSTRUCTURE | CLASSIFICATIONID | Classificazione asset associato all’OdL | UPPER | 192 |
| WORKORDER | TARGSTARTDATE | Data pianificata inizio | DATETIME |  |
| WORKORDER | TARGCOMPDATE | Data pianificata fine | DATETIME |  |
| WORKORDER | OWNERGROUP | Responsabile attività | UPPER | 8 |
| WORKORDER | X4DATAMODELLO | Data modello | DATE |  |
| WORKORDER | DESCRIPTION | Descrizione | ALN | 100 |
| WORKORDER | X4INIZIOESCLUSIONE | Inizio periodo esclusione | DATE |  |
| WORKORDER | X4FINEESCLUSIONE | Fine periodo esclusione | DATE |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

I campi X4INIZIOESCLUSIONE e X4FINEESCLUSIONE sono definiti come campi non persistenti del database. Questi campi, dunque, non saranno colonne reali della tabella WORKORDER bensì verranno solo gestiti in GUI per l’invio. Una volta processato l’invio all’MPE verranno svuotati nuovamente. Per permettere la visione dei parametri di esecuzione del MPE, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione custom “X4MPE”, si dovranno configurare i campi di testa del tab “Parametri di esecuzione”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| UdB | UDB |  |  | X4RO |
| Inizio periodo da ottimizzare | INIZIOPEROTT |  |  |  |
| Fine periodo da ottimizzare | FINEPEROTT |  |  |  |

La sigoption X4RO rende il campo non modificabile manualmente dall’utente.

Di seguito i campi della tabella “Perimetro di pianificazione”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID OdL | WORKORDER.WONUM | WORKORDER.DESCRIPTION |  |  |
| Classificazione Asset | WORKORDER.CLASSSTRUCTURE.CLASSIFICATIONID |  |  |  |
| SAP ID asset di primo livello | ASSET.X3PRIMOLIVELLO.X3SAPID |  |  |  |
| Tipo spesa | WORKORDER.X4TIPOSPESA |  |  |  |
| CoIn | WORKORDER.X4COIN |  |  |  |
| Pianificato MPE | WORKORDER.X4PIANMPE |  |  |  |
| Data ultima pianificazione MPE | WORKORDER.X4DATAULTIMAPIAN |  |  |  |
| Data pianificata inizio | WORKORDER.TARGSTARTDATE |  |  |  |
| Data pianificata fine | WORKORDER.TARGCOMPDATE |  |  |  |
| Responsabile attività | WORKORDER.OWNERGROUP |  |  |  |
| Data modello | WORKORDER.X4DATAMODELLO |  |  |  |
| Inizio periodo escluso | WORKORDER.X4INIZIOESCLUSIONE |  |  |  |
| Fine periodo escluso | WORKORDER.X4FINEESCLUSIONE |  |  |  |
| Effort | WORKORDER.JOBPLAN.X4EFFORT |  |  |  |

La relazione X4MPEODL lega le tabelle X4MPE e X4MPEODL mediante l’ID interno (:X4MPEID = X4MPEID); mentre, la relazione WORKORDER lega X4MPEODL a WORKORDER (:WONUM = WONUM and :SITEID = SITEID).

La relazione invece lega il record di X4MPE a tutti i record della tabella WORKORDER che sono sottesi all’UdB selezionata in testata (:UDB=OWNERGROUP and STATUS=’APERTO’ and X4SERBATOIO=0 and not exists(select 1 from x4indispwo where workorder.wonum=x4indispwo.wonum) JPNUM in (select jpnum from jobplan where classstructureid in (select classstructureid from classstructure where classificationid in (‘PRONTO INTERVENTO’)))

Sviluppi

Per permettere il funzionamento del Motore di pianificazione euristico, si prevedono i seguenti sviluppi a sistema:

Parametri di esecuzione

Il campo UDB viene popolato automaticamente dal sistema prelevando l’UdB di appartenenza dell’utente che effettua la pianificazione mediante la query select persongroup from persongroup where x4udb = 1 and persongroup in (select persongroup from persongroupteam where respparty = :&PERSONID&).

Il campo INIZIOPEROTT deve permettere la selezione manuale dei mesi successivi a quello corrente tramite la lookup DATEVALUE; al popolamento del campo, il valore inserito dall’utente verrà valutato da un automation script che bloccherà l’utente nel caso in cui la combinazione mese/anno inserita sia minore o uguale della combinazione mese/anno della data di sistema.

Il campo FINEPEROTT deve permettere la selezione manuale tramite la lookup DATEVALUE.

Il campo PIANMPE non sarà inizialmente popolato/gestito dal sistema, a seguito della prima conferma della proposta di pianificazione tramite il pulsante “Conferma proposta”, il campo verrà valorizzato con “SI” mediante un automation script e, contestualmente, il campo DATAULTIMAPIAN con la SYSDATE.

Pulsante “Pianifica ordini selezionati”

Per poter avviare la pianificazione degli OdL tramite MPE, sarà configurato il pulsante “Pianifica ordini selezionati” nella sezione “Perimetro di pianificazione”. Mediante script di automazione, il sistema invierà all’MPE per lanciare l’ottimizzazione dei record selezionati mediante API esposta opportunamente. A seguito del lancio, il sistema non permetterà di effettuare altre simulazioni impostando in read-only l’applicazione custom X4MPE e, inoltre, inibirà le modifiche manuali al salvataggio sugli OdL dell’UdB dell’utente (select wonum from workorder where siteid=:siteid and ownergroup in (select persongroup from persongroupteam where respparty=:&PERSONID&)).

Pulsante “Download simulazione”

Per permettere all’utente di verificare le date di pianificazione degli OdL proposte dal MPE, sarà configurato il pulsante “Download simulazione”. A seguito del download, il file Excel mostrerà le seguenti informazioni suddivise per mese: ID OdL (WONUM), Priorità (WOPRIORITY), Capacity giornaliera , Data inizio pianificata (WORKORDER.TARGSTARTDATE), Data fine pianificata (WORKORDER.TARGCOMPDATE), Data inizio proposta (DATAINIZIOPROP) e Data fine proposta (DATAFINEPROP).

Pulsanti “Conferma proposta” e “Rifiuta proposta”

Per permettere all’utente di confermare le date di pianificazione degli OdL proposte dal MPE, sarà configurato il pulsante “Conferma proposta”. A seguito della conferma, mediante automation script il sistema sovrascriverà la data di inizio pianificata TARGSTARTDATE e la data di fine pianificata TARGCOMPDATE con le date proposte dal MPE, rispettivamente con DATAINIZIOPROP e DATAFINERPOP. Invece, per consentire all’utente di rifiutare le date proposte dal MPE, sarà configurato il pulsante “Rifiuta proposta”, questo non comporterà nessuna variazione alle date di pianificazione TARGSTARTDATE e TARDGCOMPDATE. A seguito della conferma/rifiuta, saranno eliminate le date presenti nei campi DATAINIZIOPROP e DATAFINEPROP; inoltre, sarà possibile avviare nuove pianificazioni tramite l’applicazione X4MPE e salvare eventuali modifiche sugli OdL dell’UdB.

Creazione manuale dell’OdL aggregatore

L’OdL aggregatore è un ordine di lavoro composto dall’associazione di diversi OdL, ognuno caratterizzato dallo stesso piano di lavoro e riconducibile al medesimo asset di primo livello.

Configurazioni

Per la gestione dell’OdL aggregatore è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| WORKORDER | WONUM | ID OdL | UPPER | 25 |
| WORKORDER | DESCRIPTION | Descrizione OdL | ALN | 100 |
| WORKORDER | X4PROPEDEUTICO | OdL propedeutico | UPPER | 25 |
| WORKORDER | TARGSTARDATE | Data inizio pianificata | DATETIME |  |
| WORKORDER | TARGCOMPDATE | Data fine pianificata | DATETIME |  |
| WORKORDER | STATUS | Stato | UPPER | 16 |
|  |  |  |  |  |

Per permettere la gestione degli OdL relazionabili all’OdL aggregatore, sarà configurata la dialog “OdL relazionabili”

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| OdL | WONUM | DESCRIPTION |  |  |
| OdL propedeutico | X4PROPEDEUTICO | DESCRIPTION |  |  |

Per permettere la visualizzazione degli OdL aggregati, sarà configurata la sezione “OdL aggregati” del tab “Pianificazione” dell’applicazione “WOTRACK_D” caratterizzata dalla relazione “SHOWCHILDREN”

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID OdL | WONUM | DESCRIPTION |  |  |
| ID OdL propedeutico | X4PROPEDEUTICO |  |  |  |
| Data inizio pianificata | TARGSTARTDATE |  |  |  |
| Data fine pianificata | TARGFINISHDATE |  |  |  |
| Stato | STATUS |  |  |  |

La relazione “SHOWCHILDREN” lega la tabella “WORKORDER” alla tabella “WORKORDER” e mostra i figli del “WONUM” (OdL) corrente (OdL che come campo PARENT hanno il WONUM dell’ordine di lavoro aperto al momento nell’applicazione di monitoraggio OdL).

Sviluppi

Creazione manuale OdL aggregatore

Per la creazione di un OdL aggregatore, nell’applicazione “Monitoraggio OdL” si deve impostando il flag “OdL aggregatore (X4AGGREGATI della tabella WORKORDER)” pari a True, inoltre bisogna definire il set minimo di dati, ovvero:

Identificativo PdL (JPNUM della tabella WORKORDER), il quale dovrà essere uguale al PdL degli OdL da aggregare

Asset (ASSETNUM della tabella WORKORDER), gli asset presenti negli OdL aggregati dovranno essere figli dell’asset definito sull’OdL aggregatore

Codice COIN (X4COIN della tabella WORKORDER)

Funzionalità “Relaziona Ordini”

Per permettere la possibilità di aggiungere OdL all’OdL aggregatore sarà implementata la funzionalità “Relaziona Ordini”. Questa mostrerà all’utente la dialog “OdL relazionabili” con tutti gli OdL che si possono relazionare all’OdL aggregatore in stato APERTO. Inoltre, questa sarà pre-filtrata in base al set minimo di dati dell’OdL aggregatore e permette l’inserimento della propedeuticità degli OdL. Alla chiusura della dialog, un automation script imposterà il campo PARENT degli ordini di lavoro selezionati nella dialog con il valore del WONUM dell’ordine di lavoro aperto in schermata. Inoltre imposta il campo X4PROPEDEUTICO con il valore impostato nella riga della dialog del record figlio selezionato.

Funzionalità “Aggrega OdL”

Per attivarele logiche di ottimizzazione delle date dell’OdL aggregatore, sarà implementato il pulsante “Aggrega OdL”. Di seguito vengono riportati gli step del processo:

Il sistema definisce la “Finestra di flessibilità dell’OdL aggregatore”, questa è compresa tra la maggior data “non prima di” (SNECONSTRAINT) e la minor “data di massimo posticipo attività” (FNLCONSTRAINT) degli OdL figli.

Il sistema verifica la presenza di OdL propedeutici (X4PROPEDEUTICO) e posiziona la data di fine pianificata di quest’ultimi in corrispondenza della prima “data di fine pianificata” (TARGCOMPDATE) degli altri OdL.

Il sistema vincola l’OdL dipendente all’OdL propedeutico e li posiziona in modo tale che la data di fine pianificata dell’OdL dipendente coincida con la minor data di fine pianificata degli OdL aggregati. Nel caso in cui la data di inizio dell’OdL propedeutico sia minore della data di inizio della finestra di flessibilità, allora gli OdL vengono spostati rigidamente in avanti per rientrare nella finestra di flessibilità dell’OdL aggregatore.

Il sistema verifica la data di inizio pianificata minore e allinea tutti OdL aggregati a quest’ultima, rispettando i vincoli di propedeuticità tra i vari OdL.

In conclusione, la durata dell’OdL aggregatore viene calcolata come la differenza tra la maggior data di fine pianificata e la minor data di inizio pianificata degli OdL aggregati, non includendo i tempi di trasferimento.

Modifiche dell’OdL aggregatore effettuate da MPE

L'OdL aggregatore, a seguito delle logiche di ottimizzazione delle date dei suoi figli, sarà caratterizzato da una durata definita come la differenza tra la sua TARGCOMPDATE e la sua TARGSTARTDATE. Se tale OdL aggregatore rientrerà in un processo di ottimizzazione, le date che il MPE dovrà considerare sono solo quelle che definiscono la sua durata.

Modifica manuale dell’OdL aggregatore

Di seguito si riportano le possibili modifiche

Modifiche manuali delle date di pianificazione

La modifica delle date di pianificazione avviene agendo sul campo “TARGCOMPDATE”, successivamente il sistema calcola automaticamente il campo “TARGSTARTDATE” come la differenza tra il campo “TARGCOMPDATE” e il campo (durata) “ESTDUR”. Il sistema blocca l’operazione al salvataggio se l’intervallo temporale delle date di pianificazione non rientra nella finestra di flessibilità dell’OdL aggregatore.

Inoltre, si evidenzia che non è possibile modificare durata “ESTDUR” ed “X4EFFORT” direttamente sull’aggregatore (questi risultano in read only), ma solo sugli OdL sottostanti. Nel caso in cui si effettui tale modifica, durata ed effort dell’aggregatore verranno aggiornati automaticamente.

Rimuovere un OdL figlio

La rimozione di OdL figlio dall’OdL aggregatore è possibile solo se lo “STATUS” dell’OdL aggregatore è APERTO. A seguito di ciò, il sistema dovrà impostare lo “STATUS” degli OdL rimossi in APERTO; mentre, a seguito del click sul pulsante “Aggrega OdL” si attivano le logiche di ottimizzazione descritte nel bullet “Funzionalità aggrega OdL”.

Assegnazione responsabilità e attività consentite su OdL

Le figure di responsabilità dell’ordine di lavoro, ovvero responsabile asset, responsabile attività e titolare risorsa, sono caratterizzate da diverse operatività in base al ruolo e alla fase di gestione del singolo OdL.

Configurazioni

Per permettere la gestione dei gruppi di persone di Terna, si prevedono le seguenti configurazioni a sistema:

Applicazione “Gruppi di persone Terna”

L’applicazione “Gruppi di persone Terna” (X3PERSONGR, clone dell’applicazione standard PERSONGROUP) viene estesa per permettere la gestione della seguente struttura organizzativa:

Dipartimento/Distretto di trasmissione

Area tecnica

Realizzazione locale (Titris, Responsabile attività)

Misure e prove locali (Titris, Responsabile attività)

COAM (Titris, Responsabile attività)

Presidio sicurezza e ambiente

Safety e ambiente (Titris)

Monitoraggio cantieri (Titris)

Supporto gestionale (Titris)

Unità impianti (Titris, Responsabile attività, Responsabile asset)

Asset Management

Linee guida e policy (Titris)

Sviluppo e presidio metodi (Titris, Responsabile attività)

Program asset management (Titris)

Avanzamento PAM supporto AM

Predictive center (Titris)

Nel dettaglio viene apportata la seguente modifica alla tabella PERSONGROUP per gestire la tipologia di UdB a sistema del persongroup caricato in EAM:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Tipo UdB | X4TIPUDB |  | X4TIPUDB |  |
|  | X3PARENTPERSONGROUP |  | PERSONGROUP |  |

Mediante attributi viene definita la tipologia dell’UDB la gerarchia tra i vari gruppi X3PARENTPERSONGROUP

Sviluppi

Assegnazione figure di responsabilità OdL

Le logiche di valorizzazione/modifica delle figure di responsabilità dell’OdL sono le seguenti:

Responsabile asset

Il campo responsabile asset dell’OdL corrisponde all’attributo X3INGRP della tabella ASSET. Questo sarà popolato automaticamente dal sistema, nella fase di generazione dell’OdL, mediante la relazione ASSET e non potrà essere modificato dall’utente.

Responsabile attività

Il campo responsabile attività dell’OdL (OWNERGROUP) viene inserito dall’utente nel caso di OdL manuali; nel caso di ordini generati da DSS, il sistema dovrà valorizzare il campo mediante il seguente controllo:

Verificare se il JPNUM associato all’OdL presenta l’attributo X4TIPUDB di JOBPLAN valorizzato (i.e., RL, AT, etc.). Se la condizione è rispettata, dovrà verificare il dipartimento territoriale (X3SWERK) dell’asset dell’OdL e compilare il campo OWNERGROUP con la (e.g., ASSET.X3SWERK = 2608 e JOBPLAN.X4TIPUDB = RL allora WORKORDER.OWNERGROUP = DTSAR-RL); altrimenti, OWNERGROUP ASSET.X3INGRP (responsabile asset).

Inoltre, il campo OWNERGROUP di WORKORDER read-only a seguito del passaggio di stato dell’OdL da SCHEDULABILE a SCHEDULATO.

Titolare risorsa

Il campo titolare risorsa dell’OdL corrisponde all’attributo X4TITRIS delle tabelle WPLABOR (pianificazione) e LABTRANS (consuntivazione). Questo sarà popolato automaticamente dal sistema in WPLABOR, nella fase di generazione dell’OdL, con il valore presente in OWNERGROUP (responsabile attività) e non potrà essere modificato dall’utente a seguito del passaggio di stato dell’OdL da SCHEDULABILE a SCHEDULATO. Invece, in LABTRANS sarà possibile l’inserimento di nuovi X4TITRIS solo se la risorsa non è UI e l’OdL non è CHIUSO.

Attività consentite per figura

L’operatività consentita alle figure di responsabilità dell’OdL è la seguente:

Responsabile asset

Il responsabile asset non deve poter creare OdL (funzionalità “Nuova riga” di WOTRACK_D nascosta); in fase di pianificazione ed esecuzione, potrà solamente visualizzare gli OdL, a meno che il responsabile attività (OWNERGROUP di WORKORDER) sia un soggetto che non opera in Maximo, in tal caso il responsabile asset potrà gestire gli ordini di lavoro.

Responsabile attività

Il responsabile attività deve poter creare OdL (funzionalità “Nuova riga” di WOTRACK_D presente); in fase di pianificazione, potrà modificare la pianificazione (tab “Pianificazione” di WOTRACK_D), cambiare il responsabile attività (OWNERGROUP di WORKORDER), modificare il Titris (X4TITRIS di WPLABOR), avanzare lo stato dell’OdL (mediante workflow dell’ordine di lavoro). Invece, in fase di esecuzione, potrà chiudere gli OdL (mediante workflow dell’ordine di lavoro) e validare le difformità (tab “Avanzamento” di WOTRACK_D; per le ispezioni lidar, solo AM sarà autorizzato a validarle).

Titolare risorsa

Il titolare risorsa non deve poter creare OdL (funzionalità “Nuova riga” di WOTRACK_D nascosta); in fase di pianificazione, potrà solamente visualizzare gli OdL. Invece, in fase di esecuzione, potrà stornare gli OdL (funzionalità “Storno ordine di lavoro parziale e totale” di WOTRACK_D), inserire e inviare a SAP i consuntivi orari (funzionalità “Invia a SAP” di LABTRANS), inserire difformità (tab “Difformità e interferenze” di WOTRACK_D) ed eseguire micro-controlli.

Modifica Responsabile Attività

Il responsabile attività attuale può modificare il campo OWNERGROUP di WORKORDER (Responsabile attività), selezionandolo da una lista pre-filtrata con tutte le UdB del dipartimento territoriale (ASSET.X3SWERK) dell'ASSETNUM dell'OdL corrente; a seguito di ciò, deve confermare l'operazione attivando il workflow "Riassegnazione Ordine". Inoltre, il sistema notifica nel centro di controllo del destinatario l’assegnazione dell’attività.

Requisiti puntuali

Configurazioni

|  |  |  |  |  |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |

Per permettere la gestione dei requisiti puntuali presentati durante lo Sprint 6, si prevede la seguente configurazione a sistema:

Sottoprogramma

Un OdL caratterizzato dalla presenza di una specifica policy (X4POLICY di WORKORDER uguale a IDPOLICY di X4POLICY) dovrà aver valorizzato il campoSottoprogramma con il valore di X4SOTTOPROGRAMMA di X4POLICY.

Sviluppi

Per permettere la gestione dei requisiti puntuali presentati durante lo Sprint 6, si prevedono i seguenti sviluppi a sistema:

Modifica massiva della data pianificata fine

Per permettere la modifica massiva della data pianificata fine si prevede la creazione di unR file MXLoader con le seguenti colonne: OdL (WONUM), Data pianificata fine (TARGCOMPDATE).

Storicizzazione modifiche data pianificata fine

Viene abilitato l’e-audit sul campo data pianificata fine “TARGCOMPDATE” della tabella “WORKORDER” nel tab “Date” dell’applicazione “WOTRACK_D”. Verrà inoltre rappresentata a schermo una tabella che conterrà oltre al campo “TARGCOMPDATE” anche l’username “CHANGEBY” e la data “CHANGEDATE” in cui l’utente ha effettuato una modifica sul campo. La tabella sarà scaricabile in formato Excel in qualsiasi momento.
