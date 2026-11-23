Nota di rilascio EAM

| Roma, 25/08/2026 | Redatto da:<br>Indra<br><br>Approvato da: |
| --- | --- |

1.	Stop Escalation X4RILEVAZIONIODL	4

2.	Installazione pacchetti Migration Manager	4

3.	Esecuzione script SQL (DB)	6

4.	Attivazione nuove Escalation	6

Versioni documento

| Versione | data | Descrizione |
| --- | --- | --- |
| 1.0 | 25/08/2026 | Versione per installazione in ambiente di esercizio |
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

# Stop Escalation X4RILEVAZIONIODL

Accedere all’applicativo IBM Mas Manage con utenza amministrativa e assicurandosi di lavorare in lingua inglese utilizzando il seguente link:

https://masbus.manage.mas.servizi.prv/maximo

Accedere all’applicazione Escalation sotto System Configuration > Platform Configuration.

All’interno dell’applicazione, filtrare per l’escalation X4RILEVAZIONIODL come indicato in figura e accedere al record.

Cliccare sull’azione Activate/Deactivate escalation e verificare che sia disattivata.

# Installazione pacchetti Migration Manager

In questa fase occorre installare i seguenti pacchetti del Migration Manager:

BUG1456_qsznoeamal08-a_EAMMCK_MAXIMO2_20260821160109.zip

BUG1456_2_qsznoeamal08-a_EAMMCK_MAXIMO2_20260825110934.zip

allegati alla nota di rilascio seguendo la procedura indicata di seguito.

Accedere all’applicazione Migration Manager sotto System Configuration > Migration.

Selezionare l’azione Upload Package situata nel menu Common Actions.

Nella dialog che si apre, cliccare sul pulsante Browse.

Si aprirà una finestra in cui sarà possibile navigare le cartelle del sistema. Navigare fino alla cartella contenente il pacchetto e selezionarlo. Confermare tramite il pulsante OK

Selezionare l’azione Deploy Package situata nel menu Common Actions.

Spuntare il flag Do you have a current backup? e verificare che il pacchetto indicato nella tabella Packages corrisponda (il nome del file nell’immagine è solo di esempio). Selezionare il pulsante Deploy.

Verrà aperta una dialog e avviato il processo di installazione del pacchetto al termine del quale sarà possibile verificare il successo della operazione. Al termine della installazione cliccare OK (il nome del file nell’immagine è solo di esempio).

Il processo d’installazione del pacchetto è terminato.

# Esecuzione script SQL (DB)

Eseguire le query contenute nel seguente file allegato alla nota di rilascio:

bug1456.sql

schema MAXIMO dell’istanza DB

bsznoeamal01-a.servizi.prv

service name EAMES

port:1521

# Attivazione nuove Escalation

Accedere all’applicazione Escalation.

All’interno dell’applicazione, filtrare per l’escalation X4RILODL e accedere al record.

Cliccare sull’azione Activate/Deactivate escalation e verificare che sia attivata.

Ripetere gli step per l’escalation X4RILODL2.