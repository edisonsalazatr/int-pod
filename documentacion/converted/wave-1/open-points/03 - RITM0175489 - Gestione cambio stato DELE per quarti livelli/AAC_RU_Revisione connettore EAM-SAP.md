|  |
| --- |
| Richiesta di Modifica su SAP S/4<br>Codice RdM: <br>Revisione connettore EAM > SAP per garantire allineamento dati tra i due sistemi |
|  |
| Requisiti utente |
|  |
|  |
|  |
|  |

| Redatto da: | Vaiano Raffaele |  |
| --- | --- | --- |
| Verificato da: |  |  |
| Approvato da: |  |  |

Lista di distribuzione

| Nominativo | Riferimento |
| --- | --- |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |

Modifiche al documento

Le modifiche riportate nella tabella seguente sono riferite alla versione precedente del documento.

| Descrizione cambiamenti | Riferimento |
| --- | --- |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |

Riferimenti

-

Indice

1.	Motivazione della Richiesta	5

2.	Requisito	6

3.	Tempi	7

# Motivazione della Richiesta

Sono stati riscontrati numerosi disallineamenti tra le anagrafiche EAM e SAP, dovute al fatto che SAP applica delle regole interne che non sono coerenti con le regole di EAM, che è il sistema mastership per la gestione dell’anagrafica tecnica.

Lo scopo di tale Requisito è quello di proporre la modifica dei documenti funzionali in modo da rendere le anagrafiche sempre coerenti tra EAM e SAP sia in termini di oggetti tecnici (Sedi Tecniche ed Equipment) sia in termini di attributi (caratteristiche e campi di testata).

# Requisito

Il requisito consiste rivedere qualsiasi tipo di regola introdotta in SAP che possa andare in contrasto con le regole dettate da EAM, che ricordo essere il sistema mastership dell’anagrafica tecnica.

Pertanto, qualsiasi aggiornamento anagrafico inserito in EAM dovrà essere recepito dal connettore EAM > SAP per essere introdotto anche nell’anagrafica SAP.

Di seguito alcuni interventi già individuati per evitare alcuni disallineamenti già emersi durante i primi mesi di utilizzo di EAM:

Va eliminata l’azione di DELETE dai servizi di ZFMPMS4_FUNCLOC_MANAGE e ZFMPMS4_EQUIPMENT_MANAGE;

Quando su EAM viene impostato lo stato utente DELE, su SAP verrà impostato DEMO o DIST, in base alla tipologia di Asset che prevede tali Stati Utente;

Ogni chiamata proveniente da EAM, a prescindere dallo stato dell’asset inviato, farà UPDATE o RENAME, sia per le sedi tecniche sia per gli equipment;

Verificare la corretta applicazione della transazione IL10 (in SAP) quando viene rinominato un SAPID in EAM;

Eliminare la propagazione degli stati automatici in SAP sulle Sedi Tecniche sottostanti.

Parte integrante del seguente requisito è anche l’aggiornamento e condivisione della documentazione funzionale.

# Tempi

Il prima possibile