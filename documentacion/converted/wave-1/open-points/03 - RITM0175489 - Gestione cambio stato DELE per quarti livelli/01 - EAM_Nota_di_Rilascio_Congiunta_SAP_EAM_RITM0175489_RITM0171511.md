Nota di rilascio EAM

| Roma, 14/04/2026 | Redatto da:<br>Indra<br><br>Approvato da:<br>Antonino Carcione (Terna) |
| --- | --- |

1.	Disattivazione Cron Task Connettore	4

2.	Esecuzione MXLoader	4

3.	Installazione Pacchetto Migration Manager	7

4.	Esecuzione Script SQL	9

5.	Step di installazione SAP	10

6.	Attivazione Cron Task Connettore	10

Versioni documento

| Versione | data | Descrizione |
| --- | --- | --- |
| TRAP | 14/04/2026 | Versione per installazione in ambiente di esercizio |
|  |  |  |
|  |  |  |

Glossario

| Termine | Definizione |
| --- | --- |
|  |  |
|  |  |
|  |  |
|  |  |
|  |  |

# Disattivazione Cron Task Connettore

Accedere ad IBM Mas Manage tramite il seguente link https://masbus.manage.mas.servizi.prv/maximo/

Accedere all’applicazione Cron Task Setup e filtrare per il cron task X3SAPCONNECTOR.

Selezionare il risultato per accedere al cron task.

Individuare il radio button “Active?” e disattivarlo. Salvare il record.

# Esecuzione MXLoader

Aprire con Excel il file MxLoader MxLoader Connettore - DELE - Gestione Stati.xlsm

Accedere in EAM tramite apposito link https://masbus.manage.mas.servizi.prv/maximo e ricercare l’applicazione API Keys per generare la chiave dell’utenza con cui si sta eseguendo l’accesso, cliccando su ADD Api key:

Filtrare per la matricola es. MAXADMIN (cercare l’utenza in elenco cliccando sull’apposita icona evidenziata) e cliccare su Create per generare la chiave da utilizzare nel file Mxloader:

Nel foglio “Config” Inserire l’API Key di un’utenza amministrativa nel parametro Password

Cliccare su Test Connection, se la chiamata di test va a buon fine si otterrà una risposta analoga alla seguente (la parte importante è Connection Successful)

Spostarsi sullo sheet 1.X3MAXIMOSAPMAP e selezionare il menu MxLoader.

Cliccare sull’azione Run

Ripetere la stessa operazione anche per il secondo foglio 2.X3SAPASSETSPECRULE.

Al termine dell’operazione verificare che nella risposta sia indicato che stati aggiornate tutte le righe presenti nel file.

Successivamente accendere in EAM all’applicazione API Keys per rimuovere la chiave creata, cliccando sull’apposito cestino:

# Installazione Pacchetto Migration Manager

In questa fase occorre installare il pacchetto del Migration Manager X3_CON_20260414_qsznoeamal07_EAMPREX_MAXIMO_20260414145042.zip allegato alla nota di rilascio seguendo la procedura indicata di seguito.

Accedere a Maximo Manage usando un’utenza amministrativa tramite il seguente url https://masbus.manage.mas.servizi.prv/maximo.

Accedere all’applicazione Migration Manager sotto System Configuration > Migration.

Selezionare l’azione Upload Package situata nel menu Common Actions.

Nella dialog che si apre, cliccare sul pulsante Browse.

Si aprirà una finestra in cui sarà possibile navigare le cartelle del sistema. Navigare fino alla cartella contenente il pacchetto e selezionarlo. Confermare tramite il pulsante OK

Selezionare l’azione Deploy Package situata nel menu Common Actions.

Spuntare il flag Do you have a current backup? e verificare che il pacchetto indicato nella tabella Packages corrisponda (il nome del file nell’immagine è solo di esempio). Selezionare il pulsante Deploy.

Verrà aperta una dialog e avviato il processo di installazione del pacchetto al termine del quale sarà possibile verificare il successo della operazione. Al termine della installazione cliccare OK (il nome del file nell’immagine è solo di esempio).

Il processo d’installazione del pacchetto è terminato.

# Esecuzione Script SQL

Collegarsi al database:

schema MAXIMO dell’istanza DB

bsznoeamal01-a.servizi.prv

service name EAMES

port:1521

Eseguire lo script X3_CON_SQL_RITM0175489_20260414.sql

# Step di installazione SAP

Trasporto delle TR legate al Change Document 3000000152.
Nessuna altra attività pre o post activity.

# Attivazione Cron Task Connettore

Accedere ad IBM Mas Manage tramite il seguente link https://masbus.manage.mas.servizi.prv/maximo/

Accedere all’applicazione Cron Task Setup e filtrare per il cron task X3SAPCONNECTOR.

Selezionare il risultato per accedere al cron task.

Individuare il radio button “Active?” e disattivarlo. Salvare il record.

Dopo aver salvato individuare nel menù More Actions sulla sinistra l’azione Reload Request.

Verrà aperta una dialog. Spuntare la casella della riga mostrata con nome X3SAPCONNECTOR e premere OK.