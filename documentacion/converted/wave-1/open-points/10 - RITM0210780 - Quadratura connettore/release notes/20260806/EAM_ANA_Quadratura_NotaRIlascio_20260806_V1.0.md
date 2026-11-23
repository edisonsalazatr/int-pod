Nota di rilascio tool di quadratura

| Roma, 04/08/2026 | Redatto da:<br>Indra <br><br>Approvato da: |
| --- | --- |

1.	Deploy codice Connettore	4

2.	Stop Tomcat Geocall	5

3.	Stop dei PoD Integration EAM / DSS	5

4.	Attivazione modalità amministrativa	7

5.	Installazione	8

6.	Modifica pianificazione cron task	11

7.	Disattivazione modalità amministrativa	13

8.	Start dei PoD Integration	15

9.	Start Tomcat Geocall	15

10.	Pulizia tabella LOGINBLOCK	16

11.	Rollback installazione	16

Versioni documento

| Versione | data | Descrizione |
| --- | --- | --- |
| 1.0 | 04/08/2026 | Versione per installazione in ambiente di esercizio |
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

# Deploy codice Connettore

In allegato uno screen per la verifica del CAST per la build #2.0.26215.02:

Progetto: EAM

Pipeline: EAM Esercizio

Collegarsi a DevOps tramite il seguente URL http://10.145.130.92:8080/tfs/DefaultCollection/EAM/

Spostarsi sul tab Releases e filtrare per la pipeline EAM Esercizio.

Selezionare la pipeline e cliccare sull’azione Create Release.

Verrà aperta una scheda. Verificare che il numero della build sia quello corretto #2.0.26215.02 (branch feature/tqa) e cliccare Create (il nome della pipeline e il numero build nell’immagine sono solo di esempio).

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

Accedere ad IBM Mas Manage utilizzando il seguente url https://masbus.manage.mas.servizi.prv/maximo

Navigare nell’applicazione Database Configuration in System Configuration > Platform Configuration.

Avviare modalità amministrativa selezionando l’azione Manage Admin Mode.

Nella dialog che si apre selezionare il pulsante Turn Admin Mode ON.

La modalità amministrativa richiede alcuni secondi affinché tutti i server entrino in questa modalità. Per verificare che sia completamente impostata, selezionare l’azione Manage Admin Mode; se verrà mostrato il messaggio della figura successiva la modalità amministrativa non è ancora completamente attiva. Attendere fino a quando il messaggio della figura successiva non sarà più mostrato.

# Configurazione system properties

Accedere all’applicazione System Properties sotto il modulo System Configuration > Platform Configuration.

Cliccare il tasto “+” per creare una nuova proprietà.

Compilare la nuova riga nel seguente modo:

Property name: xq.diff.stop

Description: Se impostato ad 1 il tool di quadratura, se in esecuzione, terminerà appena possibile

Global value: 0

Aggiungere nuovamente una nuova riga e compilarla nel seguente modo:

Property name: xq.diff.runningmaxhours

Description: Numero di ore massimo per l'esecuzione del tool di quadratura. 0 indica nessun limite

Global value: 6

Salvare e filtrare per le due nuove proprietà create e selezionarle: xq.diff.stop, xq.diff.runningmaxhours e poi cliccare Live Refresh

# Modifica pianificazione cron task e attivazione

Accedere all’applicazione Cron Task Setup sotto il modulo System Configuration > Platform Configuration

Filtrare per il cron task XQDIFFRUNNER

Cliccare sull’oggetto. Una volta all’interno selezionare l’icona di pianificazione del cron task

Impostare la pianificazione per girare il cron task ogni giorno alle ore 22 come mostrato nell’immagine sottostante:

Cliccare su OK per confermare la modifica.

Dopodiché attivare il cron task spuntando il flag Active? e salvare il record.

Selezionare l’azione Reload request nel menù More Actions. Selezionare il record del cron task e premere ok.

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

# Start Tomcat Geocall

Questa attività è regolata da un’altra Change.

# Pulizia tabella LOGINBLOCK

Collegarsi al database:

schema MAXIMO dell’istanza DB

bsznoeamal01-a.servizi.prv

service name EAMES

port:1521

ed eseguire il seguente script delete_loginblock.sql

# Rollback installazione

In caso di eventuale errore di installazione si richiede di ripristinare la seguente versione:

Progetto: EAM

Pipeline: EAM Esercizio

Release: #2.0.25167.01