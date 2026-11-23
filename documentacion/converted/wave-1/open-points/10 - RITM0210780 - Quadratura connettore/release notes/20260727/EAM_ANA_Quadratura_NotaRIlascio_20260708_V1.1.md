Nota di rilascio tool di quadratura

| Roma, 13/07/2026 | Redatto da:<br>Indra <br><br>Approvato da: |
| --- | --- |

1.	Stop dei pod Integration	4

2.	Deploy codice Connettore	6

3.	Installazione pacchetti Migration Manager	8

4.	Configurazione System Properties	21

5.	Creazione viste PowerBi	23

6.	Avvio Escalation integrazione Sisnet	23

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

# Stop dei pod Integration

Collegarsi al cluster  https://console-openshift-console.apps.bus.azopencs.servizi.prv/ e spegnere EAM integration collegarsi ad openshift, deployments -> eam-integration

Ricercare -> EAM-integration prima di azzerare tenere traccia del numero dei pod dopo si può procedere con lo spegnimento dei  POD, come nella foto d’esempio,

Una volata ricercato selezioniamo il pod EAM Integration

Per spegnere azzeriamo le istanze dei pod

Per l’integration extended, il dss manager ed  il dss worker ripetere le istruzioni mostrate per EAM integration

# Deploy codice Connettore

In allegato uno screen per la verifica del CAST per la build #2.0.26187.01:

Progetto: EAM

Pipeline: EAM Esercizio

Collegarsi a DevOps tramite il seguente url http://10.145.130.92:8080/tfs/DefaultCollection/EAM/

Spostarsi sul tab Releases e filtrare per la pipeline EAM Esercizio.

Selezionare la pipeline e cliccare sull’azione Create Release.

Verrà aperta una scheda. Verificare che il numero della build sia quello corretto #2.0.26187.01 (branch feature/tqa) e cliccare Create (il nome della pipeline e il numero build nell’immagine sono solo di esempio).

Attendere il completamento dell’operazione di deploy prima di proseguire al punto successivo.

# Installazione pacchetti Migration Manager

In questa fase occorre installare i pacchetti contenuti nel folder mm-packages nell’ordine indicato nella tabella seguente.

Per i pacchetti XQ-040_qsznoeamal08-a_EAMMCK_MAXIMO2_20260708105633.zip e XQ-080_qsznoeamal08-a_EAMMCK_MAXIMO2_20260708105911.zip devono essere eseguite la configurazioni database i cui step vengono spiegati nel paragrafo “Database Configuration”.

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

Accedere all’applicativo IBM Mas Manage con utenza amministrativa utilizzando il seguente link https://masbus.manage.mas.servizi.prv/maximo

Accedere all’applicazione Migration Manager sotto System Configuration > Migration.

Selezionare l’azione Upload Package situata nel menu Common Actions.

Nella dialog che si apre, cliccare sul pulsante Browse.

Si aprirà una finestra in cui sarà possibile navigare le cartelle del sistema. Navigare fino alla cartella contenente i pacchetti e selezionare il pacchetto numero 1. Confermare tramite il pulsante OK.

Selezionare l’azione Deploy Package situata nel menu Common Actions.

Spuntare il flag Do you have a current backup? e verificare che il pacchetto indicato nella tabella Packages corrisponda (il nome del pacchetto nell’immagine sotto è solo di esempio). Selezionare il pulsante Deploy.

Verrà aperta una dialog e avviato il processo di installazione del pacchetto al termine del quale sarà possibile verificare il successo della operazione. Al termine della installazione cliccare OK.

Il processo d’installazione del pacchetto è terminato.

## Database Configuration

Laddove richiesta, la configurazione del database va eseguita dopo l’installazione del pacchetto.

Navigare nell’applicazione Database Configuration sotto System Configuration > Platform Configuration.

Avviare modalità amministrativa selezionando l’azione Manage Admin Mode.

Nella dialog che si apre selezionare il pulsante Turn Admin Mode ON.

La modalità amministrativa richiede alcuni secondi affinché tutti i server entrino in questa modalità. Per verificare che sia completamente impostata, selezionare l’azione Manage Admin Mode; se verrà mostrato il messaggio della figura successiva la modalità amministrativa non è ancora completamente attiva. Attendere fino a quando il messaggio della figura successiva non sarà più mostrato.

Una volta che la modalità amministrativa sarà attiva, selezionare l’azione di menu Apply Configuration Changes. Verrà proposta la finestra Structural Database Configuration. Abilitare il flag Do you have a current backup? e premere il pulsante Start Configuring the Database.

Comparirà la finestra di conferma mostrata nella figura seguente, premere il pulsante OK per chiudere.

La procedura di configurazione verrà avviata; per verificare l’avanzamento premere il pulsante Refresh Status.

Al completamento della configurazione verrà mostrato il messaggio seguente. Verificare che la configurazione sia andata a buon fine come mostrato.

Chiudere la finestra con il pulsante OK.

A questo punto rimuovere la modalità amministrativa selezionando l’azione Manage Admin Mode. Comparirà la finestra seguente.

Cliccare sul pulsante Turn Admin Mode OFF e attendere la conclusione.

A questo punto occorre completare l’installazione del pacchetto che richiedeva la configurazione del database (nelle figure seguenti si riporta ad esempio la procedura per il pacchetto XR-040).

Aprire la applicazione Migration Manager e aprire il pacchetto.

Aprire il tab Package e dal menu Action selezionare l’azione Continue Deployment.

Attendere la conferma.

## Modalità amministrativa

Laddove richiesta, la modalità amministrativa va impostata prima della installazione del pacchetto e tolta dopo l’installazione del pacchetto.

Navigare nell’applicazione Database Configuration sotto System Configuration > Platform Configuration.

Avviare modalità amministrativa selezionando l’azione Manage Admin Mode.

Nella dialog che si apre selezionare il pulsante Turn Admin Mode ON.

La modalità amministrativa richiede alcuni secondi affinché tutti i server entrino in questa modalità. Per verificare che sia completamente impostata, selezionare l’azione Manage Admin Mode; se verrà mostrato il messaggio della figura successiva la modalità amministrativa non è ancora completamente attiva. Attendere fino a quando il messaggio della figura successiva non sarà più mostrato.

Eseguire il rilascio del pacchetto richiesto.

A questo rimuovere la modalità amministrativa selezionando l’azione Manage Admin Mode. Comparirà la finestra seguente.

Cliccare sul pulsante Turn Admin Mode OFF e attendere la conclusione.

# Configurazione System Properties

Andare sull’applicazione System Properties e cercare la proprietà xq.sisnet.db.url  e impostare come global value l’url del database sisnet (disponibile nel Password Manager) come da immagine:

In seguito, cliccare + per creare la System Prop. xq.sisnet.db.password  e settare come global value la password del database di Sisnet (disponibile nel Password Manager) come da immagine:

Al termine selezionare tutte le proprietà che iniziano per xq:

xq.diff.maxthread

xq.recovery.rebind.enabled

xq.sisnet.db.url

xq.sisnet.db.user

xq.sisnet.db.password

poi cliccare live refresh

# Creazione viste PowerBi

Lanciare query nel database di produzione EAMES (schema MAXIMO) riportate nello script SQL query powerbi.sql.

# Avvio Escalation integrazione Sisnet

Accedere all’applicazione ESCALATION.

All’interno dell’applicazione, filtrare per l’escalation XQSISNETREADER_I e accedere al record.

Cliccare sull’azione Activate/Deactivate escalation.