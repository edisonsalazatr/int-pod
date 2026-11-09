# TERNA_Nota di Rilascio_GestioneAmbientePRB_v1.1

Nota di rilascio Sistema IBM MAXIMO EAM

versione: 1.0

| ROMA, 07/01/2025 | Redatto da: RTI (Accenture, Omninecs) Approvato da: NOME RIFERIMENTO TERNA STRUTTURA TERNA DI RIFERIMENTO |
| --- | --- |

1. Descrizione rilascio 4

2. Attività preliminari 4

3. Step di gestione dell’ambiente PRB 5

4. Rollback dell’installazione 9

Versioni documento

| Versione | data | Descrizione |
| --- | --- | --- |
| 1.0 | 07/01/2025 | Procedura per la gestione dell’ambiente PRB MAS |
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
|  |  |
|  |  |

# Descrizione rilascio

Breve descrizione del contenuto del rilascio (es. Bug da correggere, change/funzionalità da rilasciare): Gestione dell’ambiente PRB MAS

Funzionalità impattate: N/A

Potenziale impatto su altre applicazioni: N/A

Breve descrizione modalità di rilascio (es. automatico/manuale, necessità di autorizzazione a partire, …): Gestione con step automatici e manuali e con necessità di permessi da admin su openshift e sul DB

# Attività preliminari

Per poter procedere all’esecuzione del cambio dominio è necessario aver completato le seguenti attività preliminari:

Approvazione attività (es. allegare mail o verbale di approvazione collaudi, inclusivi dei test sulle procedure di roolback): Autorizzazione ad effettuare la copia dei dati da ESE a PRB

Stato ambiente iniziale di PRB: Servizi Core, Manage spenti. Servizi Prerequisiti del MAS, OCP attivo e DB attivo.

Eventuali esigenze di risorse (indicare l'eventuale variazione nell'utilizzo di risorse per il rilascio o post-rilascio, in termini di CPU, RAM, Spazio, etc.): N/A

Accesso ad OCP con utenza amministrativa sia da Browser che dal Bastion Host: https://console-openshift-console.apps.qua.azopencs.servizi.prv

Accesso al Mas Manage con utenza locale “maxadmin”: https://home.prb.mas.servizi.prv/

Connettività verso le componenti Infra: Bastion Host MongoDB, Oracle:

Mongo PRB: qsznoeamal02-a, qsznoeamal02-b, qsznoeamal02-c

Oracle PRB: qsznoeamal07

Bastion Host con utenza amministrativa PRB: qsznoeamal00.servizi.prv

Accesso allo schema owner MAXIMO del DB Oracle di PRB

# Step di gestione dell’ambiente PRB

Indicare la modalità (es. manuale, TFS, Jenkins) e gli step di dettaglio da eseguire per l’installazione

[Owner attività/step: Terna BSN/Terna IT]

Autorizzazione ad effettuare la copia dei dati da ESE a PRB

[Owner attività/step: Terna Database]

Copia dei dati di ESE su PRB con Manage e Core spento. La copia avviene tramite delphix

[Owner attività/step: Terna Database]

Script automatizzato tramite hooks delphix:

Connettore

Puntamenti per integrazioni verso sistemi esterni

L’attività serve a lanciare le query che servono a configurare il Connettore in modo che eviti di provare a fare invii verso SAP e per sbiancare/cancellare i puntamenti verso i servizi di Tibco.

Gli script da lanciare sono i seguenti (lanciati all’interno dello schema owner MAXIMO):

-- Svuota gli endpoint url verso SAP usati dal Connettore

UPDATE MAXENDPOINTDTL SET VALUE = NULL WHERE PROPERTY = 'ENDPOINTURL' AND ENDPOINTNAME IN ('X3CONSTEP4EQ','X3CONSTEP4EQOAUTH','X3CONSTEP4INV','X3CONSTEP4INVOAUTH','X3CONSTEP4ST','X3CONSTEP4STOAUTH');

-- Stoppa il Connettore

UPDATE CRONTASKINSTANCE c SET ACTIVE = 0 WHERE c.CRONTASKNAME = 'X3SAPCONNECTOR';

-- Resetta le proprietà del Connettore

UPDATE X3CONNECTORPROP

SET VALUE = 0

WHERE NAME IN ('connector.step2.enabled','connector.step3.enabled','connector.step4.enabled','connector.scheduler.enabled','connector.trans.cleanup.enabled','objectchange.cleanup.enabled');

[Owner attività/step: Terna/RTI]

Far ripartire MAS Core e MAS Manage. Lo start avviene tramite la pipeline “EAM Scale Deployments Collaudo”. Per lo start è necessario impostare la variabile ad up

[Owner attività/step: RTI – Ref. Dario D’Ago e Massimo Miserendino]

Nel caso in cui, durante la riattivazione, il MAS Manage va in errore (errore nel log relativo alle cryptokey) allora si lancia da POD “maxinst” ./resetcryptocryptox.sh e ./validatecryptokey.sh.

La technote da seguire per questo passaggio è: How to resolve Crypto and Cryptox ( Encryption ) keys mismatch error during Manage Activation

In seguito all’esecuzione degli script verificare se, ad ogni sync LDAP (impostata ogni 30 minuti su PRB) nei log del pod *groupsync* del Manage si trova un errore simile a “groupsync-agent-manage ERROR (…) Error 400: BXMAA9549E – The API ket token is invalid. Either the token may have expired or the token has been revoked by the administrator”.

In questo caso occorre eseguire gli step di un’altra technote (*) che ricrea le apikey interne necessarie ad aggiornare i gruppi LDAP con gli utenti corretti (in pratica verificare i record della tabella APIKEYTOKEN e, se non presenti, ricrearli tutti con lo script ./insertapikey.sh da eseguire sempre sul pod maxinst).

(*) = https://www.ibm.com/support/pages/node/6829527

Se il Manage non va in errore, questo step si può saltare.

[Owner attività/step: RTI – Ref. Dario D’Ago e Massimo Miserendino]

Clonazione dei gruppi di sicurezza %-ese a %-prb.

Questa operazione è da compiere da dentro il Manage tramite l’utenza Maxadmin e autenticazione locale.

La creazione avviene clonando i gruppi di sicurezza -ese:

Individuazione gruppi da creare come clonazione degli esistenti cercando il suffisso “ese”

Entrare nel gruppo specifico e attivare la funzione “Duplicate Group” nel menu a sinistra

Nel caso esistano già i gruppi “-prb” prima cancellarli con la funzione “Delete Group” nel menu a sinistra e confermando la cancellazione del record nel popup che si visualizza.

Assegnare il nome del gruppo da creare modificando il suffisso “ese” in “prb” come ad esempio: trn-eam-rtn-ui-ese trn-eam-rtn-ui-prb

[Owner attività/step: RTI – Ref. Dario D’Ago e Massimo Miserendino]

Esecuzione degli script per rimozione utenti dai gruppi %-ese.

Gli script riportati di seguito verranno lanciati all’interno dello schema owner MAXIMO:

delete FROM APPLICATIONAUTH where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese');

delete FROM GROUPUSER where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese');

delete FROM GRPREASSIGNAUTH where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese');

delete FROM MAXGROUP where GROUPNAME in(SELECT GROUPNAME FROM MAXGROUP where GROUPNAME like '%-ese');

[Owner attività/step: Terna/RTI]

Una volta terminata l’attività di troubleshooting, fix e deploy della fix, lo spegnimento dell’ambiente. Sarà carico dell’ RTI effettuare lo spegnimento tramite la pipeline EAM Scale Deployments Collaudo impostando come parametro “down”

# Rollback dell’installazione

Dettagliare gli step di esecuzione previsti nella procedura di roll-back che è stata preventivamente oggetto di collaudo.

N/A
