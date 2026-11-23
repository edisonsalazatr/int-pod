Nota di rilascio tool di quadratura

| Roma, 13/07/2026 | Redatto da:<br>Indra <br><br>Approvato da: |
| --- | --- |

1.	Stop Tomcat Geocall	4

2.	Stop dei PoD Integration EAM / DSS	4

3.	Attivazione modalità amministrativa	5

4.	Deploy codice Connettore	7

5.	Back-up del database	9

6.	Installazione	9

7.	Disattivazione modalità amministrativa	19

8.	Start dei PoD Integration	21

9.	Stop Tomcat Geocall	21

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

# Stop Tomcat Geocall

Questa attività è regolata da un’altra Change ed è prerequisito dello step di rilascio successivo.

# Stop dei PoD Integration EAM / DSS

Questa fase prevede il fermo dei seguenti PoD:

eam-integration-ese

eam-integration-extended-ese

dss-manager-ese

dss-worker-ese

Si riporta la procedura per il primo PoD che andrà ripetuta anche per gli altri. Si prega di annotare il numero di istanze per ogni PoD al fine di reimpostarlo in fase di riaccensione degli stessi.

Collegarsi al cluster  https://console-openshift-console.apps.bus.azopencs.servizi.prv/ e navigare in Workloads -> Deployments e selezionare il progetto eam.

Cercare eam-integration, annotare il numero di istanze e successivamente azzerarlo al fine di spegnere il PoD. Attendere lo spegnimento.

# Attivazione modalità amministrativa

Navigare nell’applicazione Database Configuration in System Configuration > Platform Configuration.

Avviare modalità amministrativa selezionando l’azione Manage Admin Mode.

Nella dialog che si apre selezionare il pulsante Turn Admin Mode ON.

La modalità amministrativa richiede alcuni secondi affinché tutti i server entrino in questa modalità. Per verificare che sia completamente impostata, selezionare l’azione Manage Admin Mode; se verrà mostrato il messaggio della figura successiva la modalità amministrativa non è ancora completamente attiva. Attendere fino a quando il messaggio della figura successiva non sarà più mostrato.

# Deploy codice Connettore

In allegato uno screen per la verifica del CAST per la build #2.0.26187.01:

Progetto: EAM

Pipeline: EAM Esercizio

Collegarsi a DevOps tramite il seguente URL http://10.145.130.92:8080/tfs/DefaultCollection/EAM/

Spostarsi sul tab Releases e filtrare per la pipeline EAM Esercizio.

Selezionare la pipeline e cliccare sull’azione Create Release.

Verrà aperta una scheda. Verificare che il numero della build sia quello corretto #2.0.26187.01 (branch feature/tqa) e cliccare Create (il nome della pipeline e il numero build nell’immagine sono solo di esempio).

# Back-up del database

Effettuare un export datapump dello schema MAXIMO service name EAMES.

# Installazione

## Sequenza di installazione pacchetti Migration Manager

Al termine del deploy e del back-up si potrà procedere con questa attività.

In questa fase occorre installare i pacchetti contenuti nel folder mm-packages nell’ordine indicato nella tabella seguente. Per i pacchetti:

XQ-040_qsznoeamal08-a_EAMMCK_MAXIMO2_20260708105633.zip

XQ-080_qsznoeamal08-a_EAMMCK_MAXIMO2_20260708105911.zip

devono essere eseguite la configurazioni database i cui step vengono spiegati nel paragrafo “Database Configuration”.

| Ordine | Nome pacchetto | Database Configuration | Modalità amministrativa | Note |
| --- | --- | --- | --- | --- |
| 1 | XQ-010_qsznoeamal08-a_EAMMCK_MAXIMO2_20260708105323.zip |  |  |  |
| 2 | XQ-040_qsznoeamal08-a_EAMMCK_MAXIMO2_20260708105633.zip | SI |  | Eseguire la configurazione del database dopo l’installazione del pacchetto |
| 3 | XQ-050_qsznoeamal08-a_EAMMCK_MAXIMO2_20260708105755.zip |  |  |  |
| 4 | XQ-080_qsznoeamal08-a_EAMMCK_MAXIMO2_20260708105911.zip | SI |  | Eseguire la configurazione del database dopo l’installazione del pacchetto |
| 5 | XQ-090_qsznoeamal08-a_EAMMCK_MAXIMO2_20260708110037.zip |  |  |  |
| 6 | XQ-100_qsznoeamal08-a_EAMMCK_MAXIMO2_20260710093637.zip |  |  |  |
| 7 | XQ-120_qsznoeamal08-a_EAMMCK_MAXIMO2_20260710095056.zip |  |  |  |
| 8 | XQ-150_qsznoeamal08-a_EAMMCK_MAXIMO2_20260710091703.zip |  |  |  |
| 9 | XQ-160_qsznoeamal08-a_EAMMCK_MAXIMO2_20260710091823.zip |  |  |  |
| 10 | XQ-170_qsznoeamal08-a_EAMMCK_MAXIMO2_20260710093853.zip |  |  |  |

A titolo di esempio verrà illustrata la procedura di installazione dettagliata del primo pacchetto. La procedura sarà applicabile agli altri pacchetti salvo indicazioni specifiche.

## Procedura di installazione di un pacchetto Migration Manager

Accedere all’applicativo IBM Mas Manage con utenza amministrativa utilizzando il seguente link:

https://masbus.manage.mas.servizi.prv/maximo

Accedere all’applicazione Migration Manager in System Configuration > Migration.

Selezionare l’azione Upload Package situata nel menu Common Actions.

Nella dialog che si apre, cliccare sul pulsante Browse.

Si aprirà una finestra in cui sarà possibile navigare le cartelle del sistema. Navigare fino alla cartella contenente i pacchetti e selezionare il pacchetto numero 1. Confermare tramite il pulsante OK.

Selezionare l’azione Deploy Package situata nel menu Common Actions.

Spuntare il flag Do you have a current backup? e verificare che il pacchetto indicato nella tabella Packages corrisponda (il nome del pacchetto nell’immagine sotto è solo di esempio). Selezionare il pulsante Deploy.

Verrà aperta una dialog e avviato il processo di installazione del pacchetto al termine del quale sarà possibile verificare il successo della operazione. Al termine della installazione cliccare OK.

Il processo d’installazione del pacchetto è terminato.

## Database Configuration

Laddove richiesta, la configurazione del database va eseguita dopo l’installazione del pacchetto. Navigare nell’applicazione Database Configuration in System Configuration > Platform Configuration. Selezionare l’azione di menu Apply Configuration Changes.  Verrà proposta la finestra Structural Database Configuration. Abilitare il flag Do you have a current backup? e premere il pulsante Start Configuring the Database.

Comparirà la finestra di conferma mostrata nella figura seguente, premere il pulsante OK per chiudere.

La procedura di configurazione verrà avviata; per verificare l’avanzamento premere il pulsante Refresh Status. Al completamento della configurazione verrà mostrato il messaggio seguente. Verificare che la configurazione sia andata a buon fine come mostrato.

Chiudere la finestra con il pulsante OK.

A questo punto occorre completare l’installazione del pacchetto che richiedeva la configurazione del database (nelle figure seguenti si riporta ad esempio la procedura per il pacchetto XR-040).

Aprire la applicazione Migration Manager e aprire il pacchetto.

Aprire il tab Package e dal menu Action selezionare l’azione Continue Deployment.

Attendere la conferma.

## Configurazione System Properties

Andare sull’applicazione System Properties e cercare la proprietà xq.sisnet.db.url e impostare come global value l’URL del database Sisnet (disponibile nel Password Manager) come da immagine:

In seguito, cliccare + per creare la pproprietà xq.sisnet.db.password  e impostare come global value la password del database di Sisnet (disponibile nel Password Manager) come da immagine:

Al termine selezionare tutte le proprietà che iniziano per xq:

xq.diff.maxthread

xq.recovery.rebind.enabled

xq.sisnet.db.url

xq.sisnet.db.user

xq.sisnet.db.password

poi cliccare Live Refresh

## Avvio Escalation integrazione Sisnet

Accedere all’applicazione Escalation.

All’interno dell’applicazione, filtrare per l’escalation XQSISNETREADER_I e accedere al record.

Cliccare sull’azione Activate/Deactivate escalation.

## Creazione viste PowerBi

Lanciare query nel database di produzione EAMES (schema MAXIMO) riportate nello script SQL query powerbi.sql.

## Recupero in caso di fallimento della procedura di installazione

Nel caso fallissero gli step precedenti senza possibilità di recupero, si procede al ripristino del sistema eseguendo le seguenti fasi.

### Stop del manage

Accedere a PoD maxinst, aprire il terminale ed eseguire il comando managestop.sh. Attendere il fermo dei PoD del Manage.

### Restore del database

Eseguire il restore del database.

### Stop del manage

Accedere a PoD maxinst, aprire il terminale ed eseguire il comando managestart.sh. Attendere il fermo dei PoD del Manage.

# Disattivazione modalità amministrativa

Navigare nell’applicazione Database Configuration sotto System Configuration > Platform Configuration.

Avviare modalità amministrativa selezionando l’azione Manage Admin Mode.

Nella dialog che si apre selezionare il pulsante Turn Admin Mode OFF.

Cliccare sul pulsante Turn Admin Mode OFF e attendere la conclusione.

# Start dei PoD Integration

Impostare il numero di istanza dei PoD al fine di farli ripartire

dss-worker

dss-manager

eam-integration

eam-integration-extended

# Stop Tomcat Geocall

Questa attività è regolata da un’altra Change.