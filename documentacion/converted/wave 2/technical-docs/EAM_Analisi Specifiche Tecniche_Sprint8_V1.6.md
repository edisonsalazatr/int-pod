# EAM_Analisi Specifiche Tecniche_Sprint8_V1.6

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Specifica Tecnica – Sprint 8 |

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
| Configurazione Database | Applicazione che consente di creare, modificare e gestire tabelle, attributi, indici e relazioni all'interno del database di Maximo. Qui è possibile definire la struttura del database e configurare i dati in modo da adattarsi alle specifiche esigenze dell'organizzazione |
| Application Designer | Applicazione che consente di creare e personalizzare l'interfaccia grafica delle applicazioni presenti in Maximo. Gli utenti possono aggiungere tabelle, sezioni e campi per adattare l'aspetto e il comportamento delle applicazioni in base alle specifiche esigenze aziendali |
| Conditional Expression Manager | Applicazione che permette di creare e modificare espressioni in linguaggio SQL utili per gestire le regole di visibilità e/o editabilità di determinati oggetti all'interno di Maximo |
| Escalation | Le escalation in IBM Maximo vengono utilizzate per automatizzare attività e monitorare record sensibili al tempo. Quest’ultime possono essere attivate quando un record raggiunge un punto di escalation definito. I punti di escalation rappresentano le condizioni che devono essere soddisfatte |
| Script di Automazione | Applicazione che permette di creare script in diversi linguaggi di programmazione al fine di automatizzare processi, eseguire calcoli complessi e personalizzare il comportamento delle applicazioni |
| Relazione | Le relazioni consentono di stabilire collegamenti tra più tabelle al fine di recuperare specifici dati mediante l'utilizzo di clausole "WHERE" all'interno di un'istruzione SQL. Questa funzionalità permette di definire una tabella di destinazione, chiamata "Figlio", sulla quale viene eseguita la clausola "WHERE", mentre la tabella di partenza è chiamata "Padre" |
| Relazione.Attributo | Convenzione utilizzata in Application Designer per fare riferimento a un attributo specifico di una tabella nel database di Maximo relazionata a quella corrente |
| Sigoption | Le sigoption (opzione di firma) permettono di specificare i privilegi per l’utilizzo di applicazioni, opzioni di menu ed elementi della barra degli strumenti. Inoltre, esistono sigoption condizionali che consentono di concedere o revocare dinamicamente i privilegi di un utente in base a espressioni condizionali. |
| MX Loader | Foglio excel che consente di: Caricare dati in Maximo Scaricare determinati dati presenti nel DB Maximo, tramite l’impostazione di opportune query all’interno del file |

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche, già descritte ad alto livello nel documento “EAM_Analisi Funzionale_Sprint_8_v2.3.docx”, verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software.

Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni implementate sulla piattaforma EAM di Terna (ambiente Dev) e gli sviluppi custom da implementare. Ciascun paragrafo (dedicato a una specifica funzionalità nell’applicativo) presenta due sotto-paragrafi: nel primo sono illustrate le configurazioni eseguite sul software (e.g., estensione del modello dati, configurazione dell’applicazione, gestione della condizionalità, etc.), nel secondo gli sviluppi necessari per soddisfare le funzionalità richieste durante lo sprint.

In particolare, i temi inclusi nello Sprint 8 sono i seguenti:

Asset di terzi e Sedi tecniche integrative

MCM e blindati

Lotti tecnici omogenei.

# Sprint 8: Adeguamenti anagrafici per Wave 2

## Asset di terzi

Gli asset di terzi sono degli asset di proprietà di società terze collegati a componenti della rete Terna.

### Configurazioni

Per la gestione dei dati inerenti agli asset di terzi e sedi tecniche integrative è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| ASSET | X3ZZSOCP1 | Società proprietaria | ALN | 4 |
| ASSET | ASSETNUM | Numero identificativo dell’asset | UPPER | 25 |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
|  |  |  |  |  |
| ASSETLOCRELATION | SOURCEASSETNUM1 | Asset relazionato gerarchicamente superiore | UPPER | 25 |
| ASSETLOCRELATION | TARGETASSETNUM1 | Asset relazionato gerarchicamente inferiore | UPPER | 25 |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

ASSET: Tabella standard di Maximo che permette la gestione dell’anagrafica delle linee e stazioni

ASSETLOCRELATION: Tabella standard di Maximo che permette la gestione delle relazioni tra gli asset

Per permettere la visione del campo all’interno dell’applicazione X3LINEE (clone dell’applicazione ASSET), si prevederà la seguente configurazione a sistema, in particolare sarà aggiunta la colonna “Società proprietaria” nelle sezioni relative agli asset di secondo, terzo e quarto livello linee:

Il campo aggiunto alla sezione “Asset di secondo livello”, caratterizzata dalla relazione “ASSETLOCRELATION”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Società proprietaria | TARGETASSET.X3ZZSOCP |  | VALUELIST |  |

La relazione “ASSETLOCRELATION” lega le tabelle ASSET e ASSETLOCRELATION mediante la condizione SOURCEASSETNUM =:ASSETNUM or TARGETASSETNUM =:ASSETNUM.

La relazione “TARGETASSET” lega le tabelle ASSETLOCRELATION e ASSET mediante la condizione ASSETNUM=:TARGETASSETNUM.

Il campo aggiunto alla sezione “Terzo livello linea”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Società proprietaria | X3ZZSOCP |  | VALUELIST |  |

Il campo aggiunto alla sezione “Quarto livello linea”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Società proprietaria | X3ZZSOCP |  | VALUELIST |  |

Per permettere la visione del campo all’interno dell’applicazione X3STAZIONI (clone dell’applicazione ASSET), si prevederà la seguente configurazione a sistema, in particolare sarà aggiunta la colonna “Società proprietaria” nelle sezioni relative agli asset di secondo, terzo e quarto livello stazioni:

Il campo aggiunto alla sezione “Asset di secondo livello”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Società proprietaria | X3ZZSOCP |  | VALUELIST |  |

Il campo aggiunto alla sezione “Terzo livello stazione”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Società proprietaria | X3ZZSOCP |  | VALUELIST |  |

Il campo aggiunto alla sezione “Quarto livello stazione”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Società proprietaria | X3ZZSOCP |  | VALUELIST |  |

Estensione del dominio “X3ZZSOCP”

Il dominio associato al campo “Società proprietaria” è X3ZZSOCP, questo contiene la lista valori: TR01 (Terna S.p.A. Italia), TR26 (Terna Rete Italia S.p.A.) e TR36 (Rete S.r.l. Italia). L’utente abilitato potrà estendere il dominio mediante l’applicazione standard di Maximo “Domains” (DOMAINADM), aggiungendo in una nuova riga un codice alfanumerico di 4 caratteri.

NB: I nuovi codici inseriti dall’utente devono essere censiti precedentemente in SAP, in modo tale che l’asset possa essere veicolato correttamente a SAP mediante connettore.

### Sviluppi

Per permettere il popolamento automatico del campo società proprietaria, si prevede il seguente sviluppo:

Compilazione automatica del campo società proprietaria (X3ZZSOCP)

Il campo società proprietaria (X3ZZSOCP) degli asset di secondo, terzo e quarto livello, viene popolato automaticamente dal sistema mediante automation script prelevando il valore del medesimo campo presente sull’asset di primo livello. Inoltre, il campo può essere modificato dall’utente tramite lista valori.

MCM e blindati

Gli MCM e blindati verranno gestiti in anagrafica come elementi di 4° livello sottostanti al 3° livello aggregatore STALLO o SBARRA.

Configurazioni

Per permettere la gestione degli MCM e blindati, si prevedono le seguenti configurazioni a sistema:

Applicazione “Classificazioni Terna”

Tramite applicazione “Classificazioni Terna” (X3ASSETCAT, clone dell’applicazione standard di gestione delle classificazioni e delle specifiche tecniche degli asset ASSETCAT) vengono create a sistema delle nuove classificazioni come definite di:

COMPARTO MCM, il cui percorso gerarchico di classificazione STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ COMPARTO MCM

COMPARTO AT BLINDATO, il cui percorso gerarchico di classificazione è STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ COMPARTO AT BLINDATO

ARMADIO GIS, il cui percorso gerarchico di classificazione è STAZIONE \ DISPOSITIVI E COMPONENTI \ IMPIANTI BLINDATI \ ARMADIO GIS.

Inoltre, i percorsi gerarchici degli elementi associati alle classificazioni appena citate sono i seguenti:

STAZIONE \ SEZIONE \ STALLO \

STAZIONE \ SEZIONE \ SBARRA \

A tale scopo viene estesa la tabella di verità implementata durante W1

Attributi di classificazione degli elementi di quarto livello

Inserimento di nuovi attributi di classificazione degli asset di 4° livello sottostanti a STALLO e SBARRA e le loro logiche di popolamento:

Tipo di isolamento (STZ_TIPO_ISOLAMENTO), caratteristica ALN valorizzata tramite lista valori: AIS, GIS, MISTO

Progressivo MCM (STZ_PROGRESSIVO_MCM), caratteristica NUM valorizzata manualmente dall’utente (presente solo sugli elementi COMPARTO MCM).

Sviluppi

Per permettere la gestione degli MCM e blindati, si prevedono i seguenti sviluppi a sistema:

Attributi di classificazione degli elementi di terzo livello “STALLO” e “SBARRA”

Inserimento di un nuovo attributo di classificazione degli asset di 3° livello STALLO e SBARRA e le sue logiche di popolamento:

Tipo di isolamento (STZ_TIPO_ISOLAMENTO), caratteristica ALN valorizzata automaticamente dal sistema mediante automation script in base alla tipologia di isolamento (STZ_TIPO_ISOLAMENTO) di asset sottesi, i valori ammissibili sono: AIS, GIS, MISTO. Inoltre, l’attributo viene ricalcolato dal sistema ad ogni inserimento o modifica di asset di quarto livello sottostante che presenta la caratteristica STZ_TIPO_ISOLAMENTO

Attributo di classificazione STZ_COM_RIF degli elementi di quarto livello

Inserimento di un nuovo attributo di classificazione degli asset di 4° livello e la sua logica di popolamento:

Comparto di riferimento (STZ_COM_RIF), caratteristica tabellare valorizzata tramite lista valori dinamica, che mostra tutti gli elementi COMPARTO MCM, COMPARTO AT BLINDATO e ARMADIO GIS sottostanti al medesimo terzo livello dell’asset. Quindi, un dominio con clausola where: ASSETNUM=:PARENT and classstructureid in (select classstructureid from classstructure where classificationid in (‘COMPARTO MCM’,’ COMPARTO AT BLINDATO’,’ ARMADIO GIS’)

Lotti tecnici omogenei

Un lotto tecnico omogeneo rappresenta un insieme di campate appartenenti alla stessa linea e caratterizzate dal medesimo anno di installazione, dallo stesso conduttore e nella quale sono presenti delle opere interferenti con medesima criticità (nel caso di diverse opere interferenti con criticità differente, si considererà quella con gravità maggiore).

### Configurazioni

Per la gestione dei dati inerenti ai lotti tecnici omogenei è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito vengono riportati in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| ASSET | X4LOTTOTECNICO | Lotto tecnico omogeneo | INT |  |
| ASSET | INSTALLDATE | Data di installazione | DATE |  |
| ASSET | ASSETNUM1 | Numero identificativo dell’asset | UPPER | 25 |
| ASSETLOCRELATION | SOURCEASSETNUM1 | Asset relazionato gerarchicamente superiore | UPPER | 25 |
| ASSETLOCRELATION | TARGETASSETNUM1 | Asset relazionato gerarchicamente inferiore | UPPER | 25 |
| CLASSSTRUCTURE | CLASSIFICATIONID1 | Classificazionedell’asset | UPPER | 192 |

Riportiamo di seguito uno schema riassuntivo delle entità e del loro corrispettivo dominio funzionale:

ASSET: Tabella standard di Maximo che permette la gestione dell’anagrafica delle linee e stazioni

ASSETLOCRELATION: Tabella standard di Maximo che permette la gestione delle relazioni tra gli asset

CLASSSTRUCTURE: Tabella standard di Maximo che permette la gestione delle classificazioni degli asset

Per permettere la gestione dei lotti tecnici omogenei, si prevede la seguente configurazione a sistema:

Inizializzazione assegnazione campata – lotto tecnico omogeneo

L’inizializzazione del dato inerente all’assegnazione delle campate ai relativi lotti tecnici omogenei sarà effettuata mediante escalation applicata a tutti gli asset LINEA. Quest’ultima attiverà lo script, descritto nel paragrafo 3.3.2, con le logiche di valorizzazione del campo X4LOTTOTECNICO della tabella ASSET.

Per permettere la valutazione della composizione della linea mediante report, si prevede la seguente configurazione a sistema:

Report “Lotti tecnici – Linea”

Il report “Lotti tecnici – Linea" viene caricato e reso disponibile nell’applicazione “Linee”, eseguibile mediante l’azione “Esegui Report”. La descrizione della logica del report e la sua composizione viene definita nel paragrafo 3.3.2.

### Sviluppi

Per permettere la gestione dei lotti tecnici omogenei, si prevedono i seguenti sviluppi a sistema:

Assegnazione campata – lotto tecnico omogeneo

L’assegnazione di una campata ad un lotto tecnico omogeneo avviene mediante automation script, questo sarà triggerato ad ogni nuovo inserimento di un asset con CLASSIFICATIONID “CAMPATA AEREA”, oppure alla modifica dell'attributo INSTALLDATE della campata stessa. Inoltre, si potrà evocare lo script anche mediante la modifica degli attributi tecnici (specifiche tecniche) degli asset gerarchicamente inferiori alla campata, ovvero: LIN_TIPO_CONDUTTORE_AEREO (dell’elemento CONDUTTORE di 4° livello) e LIN_CRITICITA_LIV3_INT (dell’elemento OPERE INTERFERENTI di 3° livello). L'algoritmo, partendo da una singola campata, prenderà in considerazione tutte le campate presenti all'interno della linea. Nel dettaglio, tramite la tabella ASSETLOCRELATION, il sistema seleziona tutti i TARGETASSETNUM che condividono il SOURCEASSETNUM della campata da cui viene evocato lo script; inoltre, applicherà un secondo filtro all'estrazione, in quanto i TARGETASSETNUM dovranno avere una CLASSIFICATIONID "CAMPATA AEREA". Una volta estratto l'insieme degli asset per cui calcolare il lotto tecnico, lo script effettuerà un primo ciclo tra gli asset e, per ognuno, ne salverà gli attributi interessanti alla valutazione del lotto tecnico omogeneo in un array (ASSETNUM della campata, LIN_TIPO_CONDUTTORE_AEREO, INSTALLDATE, LIN_CRITICITA_LIV3_INT). Una volta terminata questa operazione, lo script di automazione effettuerà un secondo ciclo sull'array sopra citato e mediante l'utilizzo di un dizionario inizialmente vuoto, andrà a censire tutte le combinazioni degli attributi sopra citati e, ad ogni combinazione univoca verrà assegnato un numero (in modo incrementale). Terminata l'operazione, l'automation script imposterà l'attributo X4LOTTOTECNICO delle campate con il numero progressivo del lotto tecnico omogeneo precedentemente calcolato per gli ASSETNUM valutati.

Report “Lotti tecnici – Linea”

Il report viene definito come un’estrazione sulla tabella ASSET di tutti i record che hanno un codice lotto tecnico omogeneo (X4LOTTOTECNICO) diverso da Null.

Nel dettaglio il report viene così impostato:

Parametri di lancio (che vanno a configurare la clausola where di estrazione dinamicamente):

Asset di primo livello (campo SOURCEASSETNUM della tabella ASSETLOCRELATION)

Livello di tensione (attributo LIN_LIVELLO_TENSIONE_ESERCIZIO della linea)

DT (X3SWERK della tabella ASSET)

UI (X3INGRP della tabella ASSET)

Questi filtri permetteranno di estrarre come segue: nel caso in cui il primo attributo sia popolato, verranno estratte a report soltanto gli assetnum della tabella asset che sono presenti come TARGETASSETNUM della tabella ASSETLOCRELATION che hanno come SOURCEASSETNUM il parametro di lancio; nel caso in cui il secondo attributo sia popolato, verranno estratte le linee aventi la specifica tecnica (ASSETSPEC) LIN_LIVELLO_TENSIONE_ESERCIZIO pari a quanto impostato nel parametro; nel caso in cui il terzo attributo sia popolato, verranno estratte le linee aventi l’attributo X3SWERK pari a quanto impostato nel parametro; nel caso in cui il quarto attributo sia popolato, verranno estratte le linee aventi l’attributo X3 pari a quanto impostato nel parametro. Le quattro condizioni interagiscono tra loro con un operatore logico AND.

Il report mostrerà un elenco di tutte le campate (record della tabella ASSET che hanno come classificationid “CAMPATA AEREA”) che soddisfanno le condizioni in input.

I campi in OUTPUT del report sono i seguenti:

DT (X3SWERK)

UI (X3INGRP)

Codice Lotto tecnico omogeneo (X4LOTTOTECNICO)

(SOURCEASSETNUM.X3SAPID - attributo X3SAPID dell’asset designato come linea della campata)

(X3SAPID)

Criticità (record di ASSETSPEC dove assetattrid= LIN_CRITICITA_LIV3_INT e ASSETNUM è un asset figlio della campata in oggetto)

Materiale e diametro conduttore (record di ASSETSPEC dove assetattrid= LIN_TIPO_CONDUTTORE e ASSETNUM è un asset figlio della campata in oggetto)

Anno installazione cond. (record di ASSETSPEC dove assetattrid= LIN_ANNO_INSTALLAZIONE e ASSETNUM è un asset figlio della campata in oggetto e classificazione=CONDUTTORE)

Materiale e Diametro fune di guardia (record di ASSETSPEC dove assetattrid= LIN_TIPO_FUNE_GUARDIA e ASSETNUM è un asset figlio della campata in oggetto)

Anno installazione fdg (record di ASSETSPEC dove assetattrid= LIN_ANNO_INSTALLAZIONE e ASSETNUM è un asset figlio della campata in oggetto e classificazione=FUNE DI GUARDIA)

Posizione (X3POSNR della campata)

HI conduttore (LASTVALUE della tabella AHSCORE dove l’assetnum è uguale al figlio della campata con classificazione CONDUTTORE e l’HI sia pari a “HI CONDUTTORE”).
