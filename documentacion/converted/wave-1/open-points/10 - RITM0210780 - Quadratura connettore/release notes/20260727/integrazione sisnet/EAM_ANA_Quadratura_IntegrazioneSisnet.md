Nota di rilascio avvio escalation integrazione sisnet

| Roma, 13/07/2026 | Redatto da:<br>Indra <br><br>Approvato da: |
| --- | --- |

1.	Installazione pacchetto	4

2.	Lanciare test connessione a Sisnet	6

3.	Cancellazione script di test	6

4.	Attivazione escalation	7

Versioni documento

| Versione | data | Descrizione |
| --- | --- | --- |
| 1.0 | 17/02/2025 | Versione per installazione in ambiente di esercizio |
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

# Installazione pacchetto

Accedere all’applicativo IBM Mas Manage con utenza amministrativa utilizzando il seguente link:

https://masbus.manage.mas.servizi.prv/maximo

Accedere all’applicazione Migration Manager in System Configuration > Migration.

Selezionare l’azione Upload Package situata nel menu Common Actions.

Nella dialog che si apre, cliccare sul pulsante Browse.

Si aprirà una finestra in cui sarà possibile navigare le cartelle del sistema. Selezionare il pacchetto XQ-010-TESTSISNET_qsznoeamal08-a_EAMMCK_MAXIMO2_20260723111627.zip. Confermare tramite il pulsante OK.

Selezionare l’azione Deploy Package situata nel menu Common Actions.

Spuntare il flag Do you have a current backup? e verificare che il pacchetto indicato nella tabella Packages corrisponda (il nome del pacchetto nell’immagine sotto è solo di esempio). Selezionare il pulsante Deploy.

Verrà aperta una dialog e avviato il processo di installazione del pacchetto al termine del quale sarà possibile verificare il successo della operazione. Al termine della installazione cliccare OK.

Il processo d’installazione del pacchetto è terminato.

# Lanciare test connessione a Sisnet

Lanciare il link presente in questa URL per poter avviare il test di connessione a Sisnet:

https://masbus.manage.mas.servizi.prv/maximo/oslc/script/XQTESTSISNETCONNECTION

Verificare che nella pagina aperta ci sia scritto TEST CONNESSIONE SISNET SUPERATO.

# Cancellazione script di test

Accedere all’applicazione Automation Script.

All’interno dell’applicazione filtrare per lo script XQTESTSISNETCONNECTION e accedere al Record.

Cliccare sull’azione Delete Script

# Attivazione escalation

Accedere all’applicazione Escalation.

All’interno dell’applicazione, filtrare per l’escalation XQSISNETREADER_I e accedere al record.

Cliccare sull’azione Activate/Deactivate escalation.