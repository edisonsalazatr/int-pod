# EAM_Analisi_Tecnica_SNOW_V4.0 (1)

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Specifica Tecnica – ServiceNow |

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

# ServiceNow: Integrazione

# Alimentazione da fonti esterne – ServiceNow

# Sviluppi Maximo not embedded

# Configurazione Maximo

Per poter leggere e scrivere oggetti di Maximo, verranno elencati i Object Structure:

|  |  |
| --- | --- |

# Flussi d’integrazioni

In questa sezione si prefigge di descrivere il flusso che intercorre tra EAM e il sistema ServiceNow e le relative interazioni.

Di seguito diagramma di chiamate tra EAM, SAP (Connettore live in produzione e non in ambito flusso SNOW ma riportato per completezza) e ServiceNow:

# Logica d’attivazione e tipologia d’interazione

Il diagramma riportato in precedenza descrive le interazioni tra EAM e i vari sistemi.

EAM attraverso il connettore (linea rossa) invia a SAP gli elementi di rete adeguando le informazioni in funzione della gerarchia di SAP.

Su EAM gli Asset vengono identificati attraverso una chiave univoca chiamata AssetNum. Su Sap gli stessi elementi vengono identificati attraverso un codice univoco che si chiama SAP Id.

EAM detiene il master dell’associazione tra le 2 chiavi. Ogni volta che viene creato un elemento gerarchico di primo livello una linea (intesa come sede tecnica di primo livello) o una stazione (intesa come sede tecnica di primo livello) il connettore si fa carico di creare il correspettivo elemento su EAM.

Viceversa, ogni volta che su piattaforma EAM viene creato o modifica un elemento di rete, il connettore salva l’elemento su SAP ed eventualmente aggiorna il master delle associazioni.

Gli elementi di rete creati o modificati devono essere inviati al sistema ServiceNow. A tale scopo ServiceNow mette a disposizione una API di allineamento:

ModificaAnagrafica

Il servizio ModificaAnagrafica verrà invocata da EAM ogni qual volta un elemento di rete/Asset verrà creato o modificato sulla piattaforma EAM IBM Maximo e attraverso la tecnologia di “Publish Channels” pubblicherà le informazioni necessarie dell’Asset su coda kafka usata dal pod eam-integration-extended. Il listener in ascolto su coda kafka dovrà acquisire tutte le informazioni ed inviarle a ServiceNow.

EAM permette di collezionare in un unico punto centrale tutte le esigenze di manutenzione sugli impianti esistenti o relative modifiche e/o costruzione di nuovi impianti che coinvolgono il personale della Unità impiantistiche.

Inoltre per gestire il caricamento iniziali in ambito del sistema SNOW è stato disegnato il flusso

ModificaAnagraficaMassiva

Tale flusso prevede di inviare il caricamento iniziale in formato CSV scambiato tra gdl EAM e gdl SNOW tramite cartella condivisa Teams Terna.

Di seguito lista dei flussi:

| Nome flusso | Tipologia d’integrazione | Tecnologia | Autenticazione |
| --- | --- | --- | --- |
| Modifica Anagrafica | APIM | Rest/Json | OAuth2 |
| ModificaAnagraficaMassiva | N.A. | CSV | N.A. |

Per ogni flusso verranno dettagliate le logiche di business implementate.

# Modifica Anagrafica

Ogni qualvolta che l’anagrafica subisce una modifica o si ha una variazione del set informativo viene inviato un nuovo messaggio su coda kafka “eam-asset-modify-extended”; Il listener in ascolto sulla stessa allineerà il sistema ServiceNow. La logica di allineamento dell’anagrafica a ServiceNow è automatica a fronte di un adeguamento manuale eseguito a GUI.

Per l’elemento ottenuto bisogna effettuare una chiamata Rest/Json in OAuh2.

L’Object Structure dedicato per questa API è X4APIASSETADDRESS.

Il formato del payload dell’oggetto ottenuto in input sulla coda kafka conterrà i seguenti campi:

{

"parent": "1723257",

"x3ingrp": "UPM",

"status": "DATT",

"assetnum": "1723262",

"x3gewrk": "BVTT3_1",

"x3ingrp": "bvtt3",

"classstructureid": "1039",

}

Successivamente si determinano gli elementi di gerarchia per ServiceNow:

SAPID

SAPID2

codicePadre

codicePadre2

radice

radice2

Di seguito verranno esposte le varie logiche in funzione della tipologia (Linee, Stazioni).

Linee:

Per tutti gli elementi della gerarchia possiamo valorizzare i seguenti elementi a prescindere:

codiceOggetto = < ASSETNUM >

descrizione = < DESCRIPTION >

codicePadre = < PARENT >

Se il campo PARENT è NULL allora l’elemento è un 1° o un 2° livello gerarchico. Pertanto, si applicano le seguenti logiche:

Se CLASSSTRUCTUREID è un 2° livello allora bisogna eseguire la seguente query:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE AL.TARGETASSETNUM='< ASSETNUM >'

A valle di questa query si esegue la seguente assegnazione:

codicePadre = < SOURCEASSETNUM prima riga>

radice = < SOURCEASSETNUM prima riga>

SAPID = < X3SAPID prima riga>

codicePadre2 = < SOURCEASSETNUM seconda riga, se esiste>

radice2 = < SOURCEASSETNUM seconda riga, se esiste>

SAPID2 = < X3SAPID seconda riga, se esiste>

Altrimenti (CLASSSTRUCTUREID è un 1° livello) allora

radice = NULL

radice2 = NULL

codicePadre2 = NULL

SAPID = < X3SAPID >

Se il campo PARENT è valorizzato e X3ROOTASSETNUM è valorizzato allora siamo in presenza di un 3° livello. La logica da applicare è la seguente:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE TARGETASSETNUM='< PARENT >'

AND SOURCEASSETNUM = '< X3ROOTASSETNUM >'

e si valorizza i seguenti campi:

radice = i-X3ROOTASSETNUM

radice2 = NULL

codicePadre2 = NULL

SAPID = X3SAPID

SAPID2 = NULL

In tutte le altre casistiche si applicano le seguenti logiche:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE AL.TARGETASSETNUM='< ANCESTOR >'

e si valorizzano i campi nel seguente modo:

radice = < SOURCEASSETNUM prima riga>

SAPID = < X3SAPID >

radice2 = < SOURCEASSETNUM seconda riga, se presente>

SAPID2 = < X3SAPID seconda riga, se presente>

codicePadre2 = NULL

Stazioni:

Per tutti gli elementi della gerarchia si valorizzano i campi nel seguente modo:

codiceOggetto = < ASSETNUM>

SAPID = < X3SAPID/X3SAPIDEQ della query iniziale o prima SAPID in gerarchia>

SAPID2 = NULL

codicePadre = < PARENT >

radice = < ANCESTOR >

radice2 = NULL

codicePadre2 = NULL

Il campo X3SAPID può essere NULL, pertanto è necessario iterare sulla tabella ASSET usando il campo PARENT fino ad individuare un SAPID.

Il campo X3INGRP può essere NULL, pertanto è necessario eseguire la seguente query per poterlo recuperare dalla tabella ASSET attraverso il campo ANCESTOR.

SELECT X3INGRP FROM ASSET

WHERE ASSETNUM = ‘< ANCESTOR >’

La struttura Json messa a disposizione da ServiceNow è la seguente:

| Campo interfaccia | Descrizione funzionale | Tipo | Obbligatorio | Note |
| --- | --- | --- | --- | --- |
| assetnum | Codice identificativo dell'asset | String(25) | Y | Campo assetnum dell’oggetto ottenuto in input. |
| changeDate | Data ultimo cambiamento | String(25) | N | Campo changedate dell’oggetto ottenuto in input formato “YYYY-MM-DD HH:MI:ss” |
| ingrp | Unità impianti dell'asset. Informazione trasmessa da EAM ma che non verrà considerata da Geocall in fase di importazione. | String(3) | N | Campo x3ingrp dell’oggetto ottenuto in input. |
| gewrk | Unità impianti dell'asset. Informazione trasmessa da EAM ma che non verrà considerata da Geocall in fase di importazione. | String(8) | N | Campo x3gewrk dell’oggetto ottenuto in input. |
| installDate | Data che indica In funzione dal | String(25) | N | Valorizzare con <installdate> formato “YYYY-MM-DD HH:MI:ss” |
| sequence1 | Numero sequenza secondo livello linea 1 | String(50) | N | Valorizzare con <x3sequence> |
| sequence2 | Numero sequenza secondo livello linea 2 solo in caso di doppia terna | String(50) | N | Valorizzare con <x3sequence> |
| status | Stato dell'asset | String(20) | N | Campo status dell’oggetto ottenuto in input. |
| statusDate | Data ultimo cambiamento stato | String(25) | N | Valorizzare con <statusdate> formato “YYYY-MM-DD HH:MI:ss” |
| swerk | Divisione di ubicazione | String(4) | N | Valorizzare con <swerk> |
| swerkDescription | Descrizione divisione di ubicazione | String(100) | N | Valorizzare con <swerkdescription> |
| classificationId | Classificazione dell'asset | String(20) | Y | Campo classstructureid dell’oggetto ottenuto in input. |
| rootSapId1 | SAP id linea/stazione | String(30) | N | Valorizzare con <x3sapid> |
| rootSapId2 | SAP id linea/stazione | String(30) | N | Valorizzare con <x3sapid> |
| description | Descrizione dell’oggetto tecnico (sede tecnica o equipment) | String(40) | N | Campo description dell’oggetto ottenuto in input. |
| costruttoreComponente | Costruttore componente | String(30) | N | Valorizzare con <x3costruttorecomponente> |
| costruttoreComponenteDescr | Descrizione costruttore componente | String(140) | N | Valorizzare con <description> |
| funzioneElemento | Funzione elemento | String(100) | N | Valorizzare con <x3funzioneelemento> |
| riferimentoFase | Riferimento fase | String(4) | N | Valorizzare con <x3riferimentofase> |
| tipoComponente | Tipo componente | String(20) | N | Valorizzare con <x3tipocomponente> |
| sapId | Precedente codice identificativo dell'asset | String(30) | Y | Valorizzare con <SAPID> |
| sapId2 | Codice identificativo SAP per elementi in doppia terna | String(30) | N | Valorizzare con <SAPID2> |
| codicePadre | Codice identificativo del padre dell'asset Informazione relativa al padre dell’oggetto trasmesso, tale informazione è necessaria per la ricostruzione della struttura della consistenza impiantistica. | String(25) | N | Valorizzare con <codicePadre> |
| codiceRadice | Informazione relativa alla radice dell’oggetto trasmesso. Necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | String(25) | N | Valorizzare con <radice> |
| codicePadre2 | Informazione relativa al secondo padre dell’oggetto trasmesso, tale informazione è necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | String(25) | N | Valorizzare con <codicePadre2> |
| codiceRadice2 | Informazione relativa al secondo padre dell’oggetto trasmesso, tale informazione è necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | String(25) | N | Valorizzare con <radice2> |
| [attributes] |  |  | N | Nodo con lista degli attributi |
| assetAttrId | Id Attributo Asset | String(50) | N | Valorizzare con <assetattrId> |
| value | Valore Stringa Attributo | String(254) | N | Verrà popolato con il valore dell’attrinbuto, quindi in caso di Stringa una Stringa, in caso di Numeri un Numero ed in caso di Date delle Date. |
| [/attributes] |  |  |  |  |

ServiceNow per ogni chiamata può risponde con un ESITO che può assumere i seguenti valori:

OK – Elaborazione OK

KO – Elaborazione KO. A valle di errore ServiceNow fornisce informazioni aggiuntive di codice e descrizione errore (CODICE_ERRORE, DESCRIZIONE_ERRORE)

In tutti i casi verrà salvato l’esito della elaborazione sulla tabella X4ESITOINTSNOW. A tale scopo useremo il business Object Structure X4ESITOINTSNOW.

L’URI da invocare è la seguente:

POST https://<HOST:PORT>/maximo/api/os/X4ESITOINTSNOW

La request da passare all’API è la seguente:

{

"start_elab": "2024-06-21T15:30:08+00:00",

"esito": "KO",

"idinterfaccia": "SEND_ASSET_TO_SERVICENOW",

"desc_esito": "XYZ",

"source": "EAM",

"dest": "SERVICENOW",

"uniqueid": "HUDISAIU383KO9",

"end_elab": "2024-06-21T15:30:08+00:00"

}

In caso di KO, usando i dati salvati si dovrà riconciliare l’operazione su ServiceNow rilanciando la request verso ServiceNow. Vedere paragrafo Schedulatore per Rielaborare i messaggi in errore.

# Inserimento Anagrafica Massiva

Il flusso serve ad allineare le sedi tecniche di ServiceNow con i dati di EAM. Il flusso è one-shot, verrà eseguito una volta sola.

Per ogni riga della tabella ASSET è necessario recuperare le seguenti informazioni:

SAPID

SAPID2

codicePadre

codicePadre2

radice

radice2

descrizionePadre

tipoImpiantoPadre

Di seguito verranno esposte le varie logiche in funzione della tipologia (Linee, Stazioni).

Linee:

Per tutti gli elementi della gerarchia possiamo valorizzare i seguenti elementi a prescindere:

codiceOggetto = < i-ASSETNUM >

descrizione = < i-DESCRIPTION >

codicePadre = < i-PARENT >

Se il campo PARENT è NULL allora l’elemento è un 1° o un 2° livello gerarchico. Pertanto, si applicano le seguenti logiche:

Se CLASSSTRUCTUREID è un 2° livello allora bisogna eseguire la seguente query:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID,

FROM ASSETLOCRELATION AL

WHERE AL.TARGETASSETNUM='< i-ASSETNUM >'

A valle di questa query si esegue la seguente assegnazione:

codicePadre = < SOURCEASSETNUM prima riga>

radice = < SOURCEASSETNUM prima riga>

SAPID = < X3SAPID prima riga>

codicePadre2 = < SOURCEASSETNUM seconda riga, se esiste>

radice2 = < SOURCEASSETNUM seconda riga, se esiste>

SAPID2 = < X3SAPID seconda riga, se esiste>

Altrimenti (CLASSSTRUCTUREID è un 1° livello) allora

radice = < i-ASSETNUM >

radice2 = NULL

codicePadre2 = NULL

SAPID = < i-X3SAPID >

Se il campo i-PARENT è valorizzato e i-X3ROOTASSETNUM è valorizzato allora siamo in presenza di 3° livello. La logica da applicare è la seguente:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE TARGETASSETNUM='< i-PARENT >'

AND SOURCEASSETNUM = '< i-X3ROOTASSETNUM >'

e si valorizza i seguenti campi:

radice = i-X3ROOTASSETNUM

radice2 = NULL

codicePadre2 = NULL

SAPID = X3SAPID

SAPID2 = NULL

In tutte le altre casistiche si applicano le seguenti logiche:

SELECT AL.SOURCEASSETNUM, AL.TARGETASSETNUM, AL.X3SAPID

FROM ASSETLOCRELATION AL

WHERE AL.TARGETASSETNUM='< i-ANCESTOR >'

e si valorizzano i campi nel seguente modo:

radice = < SOURCEASSETNUM prima riga>

SAPID = < X3SAPID >

radice2 = < SOURCEASSETNUM seconda riga, se presente>

SAPID2 = < X3SAPID seconda riga, se presente>

codicePadre2 = NULL

Stazioni:

Per tutti gli elementi della gerarchia si valorizzano i campi nel seguente modo:

codiceOggetto = < i-ASSETNUM>

SAPID = < X3SAPID/X3SAPIDEQ della query iniziale o primo SAPID in gerarchia>

SAPID2 = NULL

codicePadre = < PARENT >

codicePadre2 = NULL

radice = < ANCESTOR >

radice2 = NULL

codicePadre2 = NULL

Il campo X3SAPID può essere NULL, pertanto è necessario iterare sulla tabella ASSET usando il campo PARENT fino ad individuare un SAPID.

EAM con le informazioni individuate produrrà un CSV che avrà il seguente tracciato (verrà rispettato l’ordine che viene riportato qui):

| Campo interfaccia | Descrizione funzionale | Obbligatorio | Note |
| --- | --- | --- | --- |
| assetnum | Codice identificativo dell'asset | Y | Campo assetnum dell’oggetto ottenuto in input. |
| changeDate | Data ultimo cambiamento | N | Campo changedate dell’oggetto ottenuto in input |
| ingrp | Unità impianti dell'asset. Informazione trasmessa da EAM ma che non verrà considerata da Geocall in fase di importazione. | N | Campo x3ingrp dell’oggetto ottenuto in input. |
| gewrk | Unità impianti dell'asset. Informazione trasmessa da EAM ma che non verrà considerata da Geocall in fase di importazione. | N | Campo x3gewrk dell’oggetto ottenuto in input. |
| installDate | Data che indica In funzione dal | N | Valorizzare con <installdate> |
| sequence1 | Numero sequenza secondo livello linea 1 | N | Valorizzare con <x3sequence> |
| sequence2 | Numero sequenza secondo livello linea 2 solo in caso di doppia terna | N | Valorizzare con <x3sequence> |
| status | Stato dell'asset | N | Campo status dell’oggetto ottenuto in input. |
| statusDate | Data ultimo cambiamento stato | N | Valorizzare con <statusdate> |
| swerk | Divisione di ubicazione | N | Valorizzare con <swerk> |
| swerkDescription | Descrizione divisione di ubicazione | N | Valorizzare con <swerkdescription> |
| classificationId | Classificazione dell'asset | Y | Campo classstructureid dell’oggetto ottenuto in input. |
| rootSapId1 | SAP id linea/stazione | N | Valorizzare con <x3sapid> |
| rootSapId2 | SAP id linea/stazione | N | Valorizzare con <x3sapid> |
| description | Descrizione dell’oggetto tecnico (sede tecnica o equipment) | N | Campo description dell’oggetto ottenuto in input. |
| costruttoreComponente | Costruttore componente | N | Valorizzare con <x3costruttorecomponente> |
| costruttoreComponenteDescr | Descrizione costruttore componente | N | Valorizzare con <description> |
| funzioneElemento | Funzione elemento | N | Valorizzare con <x3funzioneelemento> |
| riferimentoFase | Riferimento fase | N | Valorizzare con <x3riferimentofase> |
| tipoComponente | Tipo componente | N | Valorizzare con <x3tipocomponente> |
| sapId | Precedente codice identificativo dell'asset | Y | Valorizzare con <SAPID> |
| sapId2 | Codice identificativo SAP per elementi in doppia terna | N | Valorizzare con <SAPID2> |
| codicePadre | Codice identificativo del padre dell'asset Informazione relativa al padre dell’oggetto trasmesso, tale informazione è necessaria per la ricostruzione della struttura della consistenza impiantistica. | N | Valorizzare con <codicePadre> |
| codiceRadice | Informazione relativa alla radice dell’oggetto trasmesso. Necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | N | Valorizzare con <radice> |
| codicePadre2 | Informazione relativa al secondo padre dell’oggetto trasmesso, tale informazione è necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | N | Valorizzare con <codicePadre2> |
| codiceRadice2 | Informazione relativa al secondo padre dell’oggetto trasmesso, tale informazione è necessaria per la gestione della “Doppia Terna”. Nel caso di sedi tecniche padre (radice dell’albero) il tag in ambito non dovrà essere valorizzato. | N | Valorizzare con <radice2> |
| LIN_LUNGHEZZA | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_CONDUTTORE_AEREO | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_ANNO_INSTALLAZIONE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_RIF_FUNE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_TIPO_FUNE_GUARDIA | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_COSTRUTTORE_GIUNTO | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_TIPO_GIUNTO | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_COSTRUTTORE_TERMINALE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_TIPO_TERMINALE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_RIF_ASSET_ARM_COND | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_TIPO_ISOLATORI_LIN | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_NUM_CATENE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_TIPO_ISOLATORI_DER | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_RIF_ASSET_ARM_FDG | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_TIPO_ISOLATORI | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_TIPO_PALIFICAZIONE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_TIPO_SOSTEGNO | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_STRUTTURA_SOSTEGNO | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_ANNO_COSTRUZIONE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_COSTRUTTORE_CAVO | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_CONDUTTORE_CAVO | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_ISOLAMENTO_CAVO | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_TIPO_CONDUTTORE_CAVO | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_SEZIONE_CONDUTTORE_CAVO | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_RIF_FASE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_RIF_POSIZIONE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| COORDINATE_LONG | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| COORDINATE_LAT | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| LIN_ELEMENTO_INDISP | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| STZ_DATA_COSTRUZIONE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| STZ_TENSIONE_NOMINALE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| STZ_FUNZIONAMENTO_COMANDI | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| STZ_TIPO_COMPARTO | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| STZ_POTENZA_NOMINALE | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |
| STZ_ELEMENTO_INDISP | Attributo | N | Sarà valorizzato solo se presente per quell’Asset e valorizzato su EAM altrimenti sarà NULL. |

Per facilitare la fruibilità del tracciato, è presente un esempio di CSV che verrà creato tramite questo servizio, che viene riportato di seguito:

Per poter creare questi n CSV sarà messa a disposizione da EAM un’API REST che una volta chiamata produrrà questi ultimi nel pacchetto “PKG_X4SNOW”, dove l’elaborazione sarà in modalità Asincrona. Di seguito verranno descritti i dettagli sul comportamento dell’API.

L’API è esposta al seguente URI ad uso esclusivamente interno EAM:

GET https://<host:port>/eam/snow/modificaAnagraficaMassiva

Quest’ultima non richiede parametri in input poiché alla sua chiamata eseguirà la seguente logica:

Vengono eseguite le query descritte nel paragrafo superiore per recuperare le informazioni sugli elementi da inserire nel CSV.

In base alla quantità di informazioni recuperate dalle query viene deciso se continuare a compilare il CSV corrente oppure se passare al successivo.

L’API si comporta in modo asincrono, quindi restituirà la response in questo modo:

{

"esito": "OK"

}

I CSV prodotti seguiranno questa naming convention: SERVICENOW_YYYYMMGG_<ora_elaborazione>_<progressivo>.csv

Dove:

<ora_elaborazione> = HHMMSS che rappresenta l’orario di inizio elaborazione del flusso.

<progressivo> = progressivo numero da 1 a N

La creazione dei CSV dovrà seguire i seguenti vincoli:

Devono avere un numero massimo di record pari a 500.000.

All'interno di uno stesso file deve essere presente l'intera struttura di un albero di consistenza.

Le radici e i nodi padre dovranno essere riportati nel file in modo ordinato, poiché ServiceNow elaborerà il file seguendo l'ordine indicato. Ciò garantirà che, nel momento in cui verrà elaborato un nodo figlio, i relativi nodi padre e le radici saranno già stati precedentemente elaborati e importati sul DB di ServiceNow.

Gli elementi di doppia campata devono essere presenti all’interno dello stesso file.

# Schedulatore per Rielaborare i messaggi in errore

In qualsiasi invocazione del flusso verso ServiceNow, per il quale si è riscontrato un errore applicativo/comunicazione si provvede a realizzare un motore che rilancerà per n-volte il messaggio a ServiceNow nel tentativo di ottenere un esito positivo nella comunicazione. Il numero di tentativi dev’essere configurabile attraverso variabili d’ambiente di OpenShift.

Il motore sfrutterà i dati presenti sulla tabella X4ESITOINTSNOW cercando i record che hanno ESITO = KO, PARENT = null.

Per ogni record individuato si provvederà ad acquisire il dato presente in MESSAGE ed inserirlo nell’opportuna coda kafka in modo da rilanciare l’elaborazione (campo KAFKA_TOPIC).

# Modello Relazione

Di seguito verranno riportate le tabelle usate all’interno dei flussi di sprint 3 con i relativi dettagli:

X4RENAMESAPIDSNOW:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4RENAMESAPIDSNOWID | NUMBER | Y | PK |
| ASSETNUM | VARCHAR(30) | Y | AssetNum modificato |
| SITEID | VARCHAR(30) | Y | SiteId Associato |
| SAPID_ORIG | VARCHAR(30) | Y | Vecchio SapId |
| SAPID_DEST | VARCHAR(30) | Y | Nuovo SapId |
| UPDATETIME | DATE | N | Data creazione/modifica |
| DA_NOTIFICARE_SNOW | VARCHAR(1) | N | Record da elaborare e notificare a ServiceNow. Può assumere valore Y o N |

ASSET:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| CLASSSTRUCTUREID | VARCHAR2(20) | Y | Classificazione dell'asset |
| STATUS | VARCHAR2(20) | Y | Stato dell'asset |
| ASSETNUM | VARCHAR2(25) | Y | Codice identificativo dell'asset |
| PARENT | VARCHAR2(25) | N | Codice identificativo del padre dell'asset |
| DESCRIPTION | VARCHAR2(40) | N | Descrizione dell’oggetto tecnico (sede tecnica o equipment) |
| X3INGRP | VARCHAR2(3) | N | Unità impianti dell'asset. |
| X3GEWRK | VARCHAR2(8) | N | Unità impianti dell'asset. |
| X3SAPID | VARCHAR2(30) | N | SapId |
| SITEID | VARCHAR2(8) | Y | Sede dell'asset |
| CHANGEDATE | VARCHAR2(25) | N | Data ultima modifica Asset |
| INSTALLDATE | VARCHAR2(25) | N | Data installazione Asset |
| STATUSDATE | VARCHAR2(25) | N | Data Stato Asset |
| X3SWERK | VARCHAR2(4) | N | Divisione di ubicazione |
| X3SAPID | VARCHAR2(30) | N | SAP id linea/stazione |
| X3COSTRUTTORECOMPONENTE | VARCHAR2(30) | N | Costruttore componente |
| X3FUNZIONEELEMENTO | VARCHAR2(100) | N | Funzione elemento |
| X3RIFERIMENTOFASE | VARCHAR2(4) | N | Riferimento fase |
| X3TIPOCOMPONENTE | VARCHAR2(20) | N | Tipo componente |
| X3ROOTASSETNUM | VARCHAR2(25) | N | Codice Radice (ASSETNUM primo livello) |

ASSETLOCRELATION:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X3SEQUENCE | VARCHAR2(50) | N | Numero sequenza secondo livello linea |
| X3SAPID | VARCHAR2(30) | N | Chiave SAP |
| SOURCEASSETNUM | VARCHAR2(25) | N | AssetNum Padre/Radice |

PERSONGROUP:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| DESCRIPTION | VARCHAR2(100) | N | Descrizione divisione di ubicazione |

CLASSSTRUCTURE:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| CLASSIFICATIONID | VARCHAR2(20) | N | Classificazione |

ALNDOMAIN:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| DESCRIPTION | VARCHAR2(140) | N | Descrizione costruttore componente |

X4ESITOINTSNOW:

| Campo | Tipo | Obbligatorio | Descrizione |
| --- | --- | --- | --- |
| X4ESITOINTSNOWID | NUMBER | Y | PK |
| IDINTERFACCIA | VARCHAR2(50) | N | Nome interfaccia |
| ESITO | VARCHAR2(10) | N | ESITO (Es. OK o KO) |
| COD_ESITO | VARCHAR2(30) | N | Codice d’errore |
| DESC_ESITO | VARCHAR2(2000) | N | Descrizione d’errore |
| UNIQUEID | VARCHAR2(50) | Y | Chiave d’integrazione per flusso |
| ROWSTAMP | VARCHAR2(40) | Y |  |
| PARENT | NUMBER | N | Parent è FK al campo X4ESITOINTID |
| START_ELAB | DATE | N | Data inizio elaborazione |
| END_ELAB | DATE | N | Data fine elaborazione |
| KAFKA_TOPIC | VARCHAR2(50) | N | Coda Kafka di elaborazione |
| MESSAGE | CLOB | N | Messaggio Json d’input. Viene salvato se necessario |
