Indicazioni per una corretta gestione degli Incident / Problem    (aggiornato al 2026-10-29)

INCIDENT:

Inserire sul campo TAG le seguenti priorità in seguito ad analisi dell’Incidente:

Bloccante

Significativa

Lieve

Estetica

Correlazione Padre / figlio:

Associare eventuali Incidet Figli all’Incident Padre la cui risoluzione risolve automaticamente anche tutti i figli. Qualora siano invece necessarie analisi o bonifiche specifiche sui singoli ticket, questi dovranno essere gestiti individualmente oppure associati ad un Problem, se presente e pertinente.

Indicent:

Eventuali Bug che rientrano nell’AMS (di lieve entità:  <=1gg di effort) NON devono essere censiti come PROBLEM ma devono essere risolti all’interno dell’AMS; in caso di bug che risolvono più incident si potrà utilizzare il meccanismo della correlazione tra Incident (Padre/Figlio) ma SOLO se la risoluzione del bug (Indicent) primario NON richiede bonifiche sui bug correlati.

“Errata apertura”

Incident aperti per “Errata apertura”: riportare sulle note di risoluzione la seguente dicitura:

“Errata operatività utente”: per gli incident riconducibili a un errato utilizzo dell’applicativo da parte dell’utente.

“Richiesta di supporto”: per le richieste di supporto impropriamente aperte come incident.

“Attività catalogo”: per gli incident aperti per gestire attività di modifica catalogo (Business su Business).

PROBLEM:

Associazione degli Incident ad un Problem: dovranno essere ricondotti ad un Problem gli incident la cui risoluzione richiede un intervento strutturato, con analisi della root cause, attività di sviluppo e test IT/UAT, etc.

Gestione Problem: Ricordiamo che la chiusura definitiva del Problem resta di competenza ICT, così da verificare preventivamente che tutti gli incident associati siano stati gestiti e i relativi stati correttamente aggiornati.