Nota di rilascio EAM

| Roma, 05/06/2026 | Redatto da:<br>Indra<br><br>Approvato da:<br>Antonino Carcione (Terna) |
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
| TRAP | 05/06/2026 | Versione per installazione in ambiente di esercizio |
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

# Installazione Pacchetto Migration Manager

In questa fase occorre installare il pacchetto del Migration Manager allegato alla nota di rilascio seguendo la procedura indicata di seguito.

Accedere a Maximo Manage usando un’utenza amministrativa tramite il seguente url https://masbus.manage.mas.servizi.prv/maximo.

Accedere all’applicazione Migration Manager sotto System Configuration > Migration.

Selezionare l’azione Upload Package situata nel menu Common Actions.

Nella dialog che si apre, cliccare sul pulsante Browse.

Si aprirà una finestra in cui sarà possibile navigare le cartelle del sistema. Navigare fino alla cartella contenente il pacchetto e selezionarlo. Confermare tramite il pulsante OK

Selezionare l’azione Deploy Package situata nel menu Common Actions.

Spuntare il flag Do you have a current backup? e verificare che il pacchetto indicato nella tabella Packages corrisponda (il nome del file nell’immagine è solo di esempio). Selezionare il pulsante Deploy.

Verrà aperta una dialog e avviato il processo di installazione del pacchetto al termine del quale sarà possibile verificare il successo della operazione. Al termine della installazione cliccare OK (il nome del file nell’immagine è solo di esempio).

Il processo d’installazione del pacchetto è terminato.