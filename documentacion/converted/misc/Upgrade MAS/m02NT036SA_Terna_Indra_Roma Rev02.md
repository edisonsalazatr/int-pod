| Accordo Di Interconnessione<br>fra<br>Terna S.p.A.<br>e<br>Indra Italia S.p.A.  <br>ALLEGATO TECNICO |
| --- |

| ISC - Uso RISERVATO |
| --- |

Storia delle revisioni

| Rev. n° | Data | Descrizione |
| --- | --- | --- |
| 01 | 10/11/2025 | Attivazione flussi per nuovo AQ |
| 02 | 08/05/2026 | Aggiunti flussi per Maximo |

Lista di distribuzione Terna

| Gruppo/Nominativo destinatario | Ambito di circolazione |
| --- | --- |
| SDS-ITD-ESP-SOL-Application Operations & Cloud | Controllata e limitata |
| SDS-ITD-CS | Controllata e limitata |
|  |  |
|  |  |
|  |  |

Lista di distribuzione Indra Italia S.p.A

| Gruppo/Nominativo destinatario | Ambito di circolazione |
| --- | --- |
| Francesco Giorgio Munafò | Controllata e limitata |
| Salvatore Oteri | Controllata e limitata |
|  |  |
|  |  |
|  |  |

Sistemi di gestione e/o modelli organizzativi di riferimento:

| x | Sistema di Gestione per la Qualità |
| --- | --- |
|  | Sistema di Gestione Ambientale |
|  | Sistema di Gestione per la sicurezza e la tutela della salute sui luoghi di lavoro |
|  | Sistema di Gestione degli Incidenti Rilevanti - Seveso |
| x | Sistema di Gestione per la sicurezza delle informazioni |
|  | Sistema di Gestione dell'energia consumata per usi propri |
|  | Sistema di Gestione per il Laboratorio LST |
|  | Sistema di Gestione per il Centro di Taratura |
|  | Sistema di Gestione Anticorruzione |
|  | Sistema di Gestione degli Asset |
|  | Sistema di Gestione della Prevenzione e Controllo delle Infezioni |
|  | Business Continuity Management |
|  | Modello Privacy |
|  | Modello 262 |
|  | Modello 231 |

(Apporre una “X” nella colonna di sinistra in riferimento alla riga interessata)

SOMMARIO

1	Generalità	4

1.1	Introduzione	4

2	Riferimento ADI	5

3	Dettagli dell’interconnessione	6

3.1	Tipologia di interconnessione e altri dettagli	6

3.2	Rete interconnessa lato Terna	6

3.3	Rete interconnessa lato Terzo	6

3.4	Topologia	6

3.5	Indirizzi IP/Protocolli /Porte abilitati	7

4	Punto di Contatto	12

4.1	Presidio monitoraggio sull’interconnessione	12

# Generalità

## Introduzione

Nell’ambito del contratto di servizi corrispondente all’Ordine 6300000029 il presente accordo di interconnessione (nel seguito “ADI”) fra:

Terna, rappresentata da Pietro Caminiti

e

INDRA ITALIA S.P.A. rappresentata da Erminio Polito,

Il presente Allegato Tecnico definisce le modalità tecniche con cui sarà realizzata l’interconnessione e le configurazioni che saranno effettuate da Terna e da INDRA ITALIA S.P.A. per garantire le comunicazioni.

La sottoscrizione dell’ADI e del presente Allegato non costituisce in alcun modo un’autorizzazione all’accesso alle reti di Terna né ai sistemi ed ai dati in esse contenuti.

Il presente Allegato Tecnico ha validità esclusivamente nell’ambito dell’Accordo di Interconnessione, definito in termini di obiettivi dell’interconnessione, servizi, infrastrutture e relative considerazioni di rischio. Qualora nel corso del ciclo di vita dell’interconnessione dovessero emergere esigenze di modifica di tale ambito, entrambi i documenti dovranno essere aggiornati.

# Riferimento ADI

Il presente Allegato Tecnico riporta il dettaglio tecnico pertinente all’Accordo di Interconnessione m01NT036SA_Terna_INDRA - Responsabilità e Impegni per la sicurezza dell’interconnessione INDRA_v1.0 adeguatamente archiviato e gestito dalla struttura SDS-ITD-CS.

# Dettagli dell’interconnessione

## Tipologia di interconnessione e altri dettagli

Tipologia di interconnessione permanente, attivata tramite richiesta alla struttura di TLC-ING e con periodicità concorde al contratto di supporto stipulato con il fornitore INDRA ITALIA S.P.A. – Rete interconnessa lato Terna

## Rete interconnessa lato Terna

La topologia di rete, per l’interconnessione alla rete di INDRA ITALIA S.P.A., prevede l’utilizzo della connessione Internet di Terna presso la sede di Roma Nord.

Al fine di garantire autenticità, riservatezza e non ripudio delle formazioni scambiate tra le due reti, si è deciso di utilizzare per l'interconnessione una VPN site-to-site che utilizza un sistema di autenticazione tra i gateway ad encryption dei dati trasmessi.

Nello specifico la VPN è basata su IPSEC, con protocollo IKEv1/IKEv2 e modalità di autenticazione SHA-1 con chiave pre-coondivisa (Pre-shared Key).

La topologia prevede che il tunnel VPN, proveniente dal gateway INDRA ITALIA S.P.A., venga terminato sul Cluster Firewall VPN CheckPoint presso la sede di Roma Nord.

Per l’accesso verso sistemi indicati, il traffico verrà autorizzato, una volta decriptato, da diversi Cluster Firewall presenti nell’infrastruttura di rete Terna.

Sarà cura di Terna fornire i parametri di interconnessione per la VPN.

## Rete interconnessa lato Terzo

IP Pubblico firewall perimetrale sede di Roma 151.14.27.21/29

L'indirizzo con cui INDRA ITALIA S.P.A. dovrà presentarsi nella VPN è 192.168.21.178

## Topologia

La rete locale degli uffici di INDRA ITALIA S.P.A., di Via del Serafico 200 – Roma 00142 è segmentata in Vlan dedicate per ogni progetto, su segmenti separati si trovano le Vlan server e DMZ.

Tutta l’infrastruttura di rete si basa su Cisco sia per la parte di distribuzione che Core, i firewall di DMZ sono Juniper.

## Indirizzi IP/Protocolli /Porte abilitati

In calce si riportano gli indirizzi IP dei sistemi che saranno raggiunti dalla VPN:

| Nome Macchina | Indirizzo IP | Porta | NOTE | Rev |
| --- | --- | --- | --- | --- |
| https://console-openshift-console.apps.dev.azopencs.servizi.prv | 10.178.191.71 | TCP/443 | IBM Maximo - DEV | 1 |
| dsznoeamal02.servizi.prv | 10.178.189.5 | TCP/1521 | IBM Maximo Database - DEV | 1 |
| dsznoeamal01.servizi.prv | 10.178.189.4 | TCP/27017 | IBM Maximo Database - DEV | 1 |
| https://console-openshift-console.apps.qua.azopencs.servizi.prv | 10.178.187.71 | TCP/443 | IBM Maximo - QUA | 1 |
| qsznoeamal02-a.servizi.prv/qsznoeamal02-b.servizi.prv/ qsznoeamal02-c.servizi.prv/ | 10.178.185.8, 10.178.185.4, 10.178.185.6 | TCP/27017 | IBM Maximo Database - QUA | 1 |
| qsznoeamal01-a.servizi.prv/ qsznoeamal01-b.servizi.prv/<br>qsznoeamal03-a.servizi.prv/ qsznoeamal03-b.servizi.prv/ qsznoeamal04.servizi.prv/ qsznoeamal07.servizi.prv/ qsznoeamal08-a.servizi.prv | 10.178.185.5, 10.178.185.7, 10.178.185.18, 10.178.185.19,<br>10.178.185.21,<br>10.178.185.20,<br>10.178.185.31 | TCP/1521 | IBM Maximo Database - QUA | 1 |
| https://console-openshift-console.apps.bus.azopencs.servizi.prv/ | 10.178.63.71,<br>10.178.58.8 | TCP/443 | IBM Maximo - BUS | 1 |
| bsznoeamal01-a.servizi.prv, bsznoeamal01-b.servizi.prv | 10.178.58.10 | TCP/1521 | IBM Maximo Database - BUS | 1 |
| gswvatfsat19.ternaren.prv | 10.145.130.92 | TCP/9091<br>TCP/8080 | DevOps Cast | 1 |
| dsznoelval00 | 10.178.189.28 | 1521,2484 | Database | 1 |
| https://console-openshift-console.apps.quashr.aro.servizi.prv/dashboards | 10.178.124.254 | 443 | ARO Azure PaaS | 1 |
| qsznoelval00.Servizi.prv | 10.178.185.38 | 1521,2484 | Database | 1 |
| qsznoelval01.Servizi.prv | 10.178.185.39 | 1521,2484 | Database | 1 |
| vdi.ternaren.prv | 10.145.137.223 | 443 | CAST Cloud | 1 |
| dsznocasaw00 | 10.178.152.5 | 8084/2284 | TCP VDI in desktop remoto | 1 |
| dsznocasaw01 | 10.178.156.12 | 2284 | TCP VDI in desktop remoto | 1 |
| dsznocasaw02 | 10.178.156.14 | 2284 | TCP VDI in desktop remoto | 1 |
| dsznocasaw03 | 10.178.156.8 | 2284 | TCP VDI in desktop remoto | 1 |
| dsznocasaw04 | 10.178.156.10 | 8084/2284 | TCP VDI in desktop remoto | 1 |
|  |  |  |  |  |

Aggiunti IP Per IBM Maximo

| Nome Macchina | Indirizzo IP | Porta | NOTE | Rev |
| --- | --- | --- | --- | --- |
| Infrastruttura VDI Horizon SSI | 10.250.4.28/32 | CP/443<br>TCP/4172<br>TCP/8443<br>TCP/22443<br>TCP/32111 | VDI per SSI | 2 |
| Infrastruttura VDI Horizon SSI | 10.250.3.128/25 | CP/443<br>TCP/4172<br>TCP/8443<br>TCP/22443<br>TCP/32111 | VDI per SSI | 2 |

# Punto di Contatto

Terna ed il Terzo si impegnano a rendere disponibile un proprio Punto di Contatto (PdC) per la segnalazione e la risoluzione degli incidenti di sicurezza sull’interconnessione, secondo i prospetti riportati di seguito.

| PdC Terna | PdC Terna | PdC Terna | PdC Terna | PdC Terna | PdC Terna | PdC Terna |
| --- | --- | --- | --- | --- | --- | --- |
| periodo/orario | identificativo | telefono | telefono | referente | e-mail | e-mail |
| H 24 - 7/7 | PdC CERT | 06/83139241 | 06/83139241 | Reperibile del CERT | cert@terna.it | cert@terna.it |
| PdC Indra Italia S.p.A | PdC Indra Italia S.p.A | PdC Indra Italia S.p.A | PdC Indra Italia S.p.A | PdC Indra Italia S.p.A | PdC Indra Italia S.p.A | PdC Indra Italia S.p.A |
| periodo/orario | identificativo | identificativo | telefono | referente | referente | e-mail |
| (Lun-Ven<br>09.00- 18.00) | Assistenza Tecnica | Assistenza Tecnica | +3906412110645 | CAU Italia | CAU Italia | cauitalia@minsait.com |

## Presidio monitoraggio sull’interconnessione

Il CERT predisposto in qualità di Punto di Contatto Terna, svolge l’attività di raccolta di segnalazioni e risoluzione di anomalie, eventi e/o incidenti di sicurezza.

Nel caso in cui il Terzo dovesse avere dei problemi di connessione, causati da eventi e/o incidenti di sicurezza, dovrà inoltrare le richieste alla struttura del CERT di Terna, la quale si occuperà di risolvere la problematica.