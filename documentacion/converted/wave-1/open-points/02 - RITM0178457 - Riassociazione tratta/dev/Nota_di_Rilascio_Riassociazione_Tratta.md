Nota di rilascio EAM

| Roma, 29/04/2026 | Redatto da:<br>Indra<br><br>Approvato da:<br>Antonino Carcione (Terna) |
| --- | --- |

1.	Aggiunta variabili pipeline	3

2.	Deploy codice Connettore	4

3.	Backup database EAMES	5

4.	Abilitazione Admin Mode	6

5.	Installazione pacchetti Migration Manager	6

6.	Disabilitazione Admin Mode	9

7.	Esecuzione script SQL	9

8.	Eliminazione sigoption obsolete	9

9.	Profilazione funzionalità	10

10.	Rollback installazione	13

Versioni documento

| Versione | data | Descrizione |
| --- | --- | --- |
| TRAP | 29/04/2026 | Versione per installazione in ambiente di esercizio |
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

# Aggiunta variabili pipeline

Si richiede la creazione della seguente variabile sulla pipeline EAM Esercizio: VariabiliDevOpsOperations - EAM Esercizio.xlsx

# Deploy codice Connettore

In allegato uno screen per la verifica del CAST per la build #2.0.2602.01:

Progetto: EAM

Pipeline: EAM Esercizio

Collegarsi a DevOps tramite il seguente url http://10.145.130.92:8080/tfs/DefaultCollection/EAM/

Spostarsi sul tab Releases e filtrare per la pipeline EAM Esercizio.

Selezionare la pipeline e cliccare sull’azione Create Release.

Verrà aperta una scheda. Verificare che il numero della build sia quello corretto #2.0.26082.01 (branch open-points) e cliccare Create (il nome della pipeline e il numero build nell’immagine sono solo di esempio).

Attendere il completamento dell’operazione di deploy prima di proseguire al punto successivo.

# Backup database EAMES

Prima di procedere con l’esecuzione della seguente nota, verificare che il backup del database

schema MAXIMO dell’istanza DB

bsznoeamal01-a.servizi.prv

service name EAMES

port:1521

Sia stato effettuato e reso permanente (RITM0208251).

# Abilitazione Admin Mode

Accedere a Maximo Manage usando un’utenza amministrativa tramite il seguente url https://masbus.manage.mas.servizi.prv/maximo.

Accedere all’applicazione Database Configuration sotto System Configuration > Platform Configuration.

Selezionare l’azione Manage Admin Mode situata nel menu Common Actions.

Verrà aperta una dialog. Selezionare Turn Admin Mode ON.

Attendere che tutti i pod siano in admin mode prima di procedere.

# Installazione pacchetti Migration Manager

In questa fase occorre installare il pacchetto del Migration Manager X3_ANA_20260302_qsznoeamal07_EAMPREX_MAXIMO_20260320101955.zip allegato alla nota di rilascio seguendo la procedura indicata di seguito.

Accedere a Maximo Manage usando un’utenza amministrativa tramite il seguente url https://masbus.manage.mas.servizi.prv/maximo.

Accedere all’applicazione Migration Manager sotto System Configuration > Migration.

Selezionare l’azione Upload Package situata nel menu Common Actions.

Nella dialog che si apre, cliccare sul pulsante Browse.

Si aprirà una finestra in cui sarà possibile navigare le cartelle del sistema. Navigare fino alla cartella contenente il pacchetto e selezionarlo. Confermare tramite il pulsante OK

Selezionare l’azione Deploy Package situata nel menu Common Actions.

Spuntare il flag Do you have a current backup? e verificare che il pacchetto indicato nella tabella Packages corrisponda (il nome del file nell’immagine è solo di esempio). Selezionare il pulsante Deploy.

Verrà aperta una dialog e avviato il processo di installazione del pacchetto al termine del quale sarà possibile verificare il successo della operazione. Al termine della installazione cliccare OK (il nome del file nell’immagine è solo di esempio).

Il processo d’installazione del pacchetto è terminato.

# Disabilitazione Admin Mode

Accedere a Maximo Manage usando un’utenza amministrativa tramite il seguente url https://masbus.manage.mas.servizi.prv/maximo.

Accedere all’applicazione Database Configuration sotto System Configuration > Platform Configuration.

Selezionare l’azione Manage Admin Mode situata nel menu Common Actions.

Verrà aperta una dialog. Selezionare Turn Admin Mode OFF.

Attendere che tutti i pod siano usciti dall’admin mode prima di procedere.

# Esecuzione script SQL

Collegarsi al database:

schema MAXIMO dell’istanza DB

bsznoeamal01-a.servizi.prv

service name EAMES

port:1521

Eseguire lo script X3_SQL_DELETE_APPAUTH.sql

# Eliminazione sigoption obsolete

Accedere a Maximo Manage usando un’utenza amministrativa tramite il seguente url https://masbus.manage.mas.servizi.prv/maximo.

Accedere all’applicazione Application Designer sotto il modulo System Configuration > Platform Configuration.

Filtrare per applicazione X3LINEE ed accedervi.

Selezionare l’azione Add/Modify Signature Options. Verrà aperta una dialog, filtrare per i seguenti record e premere invio:

X3ENTRAESCI, X3ENTRAESCIDIALOG

Cliccare sul cestino alla destra dei due record ed infine cliccare OK.

# Profilazione funzionalità

Accedere a Maximo Manage usando un’utenza amministrativa tramite il seguente url https://masbus.manage.mas.servizi.prv/maximo.

Accedere all’applicazione Security Groups sotto il modulo Security.

All’interno dell’applicazione, filtrare per i gruppi di sicurezza elencati:

trn-eam-admin-mas-ese, trn-eam-rtn-cntr-1-ese, trn-eam-rtn-cntr-2-ese, trn-eam-rtn-ui-ese

Accedere al primo record della lista.

Spostarsi sul tab Applications e aprire il filtro nella sezione principale.

Inserire nel campo descrizione l’applicazione Linee e premere Invio.

Scorrere verso il basso fino alla sezione delle azioni. Espandere il filtro ed inserire i seguenti valori:

Riassocia tratta (aerea/cavo), Azione di cancel su dialog entra esci, Azione di salvataggio su dialog entra esci, Verifica il SAPID nella preview dell'entra-esci, Verifica se sono presenti errori nella preview

Attivare tutti i radio button e salvare il record.

Una volta salvato, utilizzare la freccia in alto a destra per passare al record successivo e ripetere le operazioni riportate sopra per gli altri gruppi di sicurezza.

# Rollback installazione

In caso di eventuale errore di installazione si richiede di ripristinare la seguente versione:

Progetto: EAM

Pipeline: EAM Esercizio

Release: #2.0.25167.01