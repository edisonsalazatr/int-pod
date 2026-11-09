# HLD_IFA_EAM_ELIVE_v1.4

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento | HLD– Interface Agreement EAM vs ELIVE |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data | Owner |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | ACN |  |  |
| Riesaminato da: | ACN | 02/07/2024 |  |
| Approvato da: | TERNA | GG/MM/AAAA |  |

# Glossario

| Termine | Definizione | Definizione |
| --- | --- | --- |
| Piano di Lavoro | Oggetto MAS utilizzato per rappresentare le attività |  |
| Ordine di Lavoro | Oggetto MAS utilizzato per rappresentare e gestire i lavori da eseguire |  |
| Job Task | Oggetto MAS per rappresentare sia i metodi, sia i micro-controlli associati a un PdL |  |

# Obiettivi del documento

Nell’ambito programma di evolutive previste per la wave 2 del progetto EAM il documento ha lo scopo di descrivere funzionalmente i flussi d’integrazione e di riportante per ogni flusso:

Descrizione funzionale

logiche d’attivazione

dati scambiati

tipologia d’interazione

# Flussi d’integrazione e logica d’attivazione

Questa sezione si prefigge di descrivere i flussi d’integrazione tra EAM e il sistema ELIVE e le relative interazioni. Lo scopo dell’integrazione con Elive è quello di ricevere in modo automatico, tramite un’interfaccia, le misurazioni derivanti dalle ispezioni Lidar. Le misurazioni (distanze tra le piante e gli asset) sono poi convertite e registrate come interferenze arboree da EAM. Le singole interferenze derivanti dall’elaborazione del file delle misure lidar sono associate agli OdL che ne hanno permesso la registrazione. In Figura 9 il processo di gestione delle interferenze registrate tramite misurazioni Lidar.

Figura 9 - Processo di integrazione con Elive

vengono poi convertite in interferenze arboree se soddisfatte determinate logiche di business.

Di seguito diagramma delle chiamate tra i sistemi EAM e ELIVE:

| Id | Nome flusso | Descrizione |
| --- | --- | --- |
| 1 | Invio OdL | Flusso di invio di ordini di lavoro relativo al taglio piante verso ELIVE |
| 2 | Report Taglio Piante | Flusso per l’inserimento a sistema dei dati relativi alle interferenze |

## Flusso INVIO OdL

### Descrizione funzionale interfaccia

L’interfaccia di Invio OdL ha lo scopo di inviare a Elive tutti gli Ordini di Lavoro relativi alle attività di ispezione Lidar generati dal DSS report di Taglio Piante che conterrà le misure rilevate durante le ispezioni, all’Ordine di Lavoro corrispondente.

### Logiche d’attivazione

L’interfaccia di Invio OdL scatta ogni qual volta un Ordine di Lavoro relativo alle attività di Taglio Piante passa dallo stato “In Validazione” allo stato “Aperto”.

l subset di rdini di lavoro da inviare ad Elive è

#### Gestione OdL

Per gestire gli ODL, si riporta di seguito la sequenza operativa:

EAM invia a Elive gli n ODL di taglio piante (anche più ODL per campata ma con “data pianificazione” differente)

Elive calcola il campo “PeriodoEAM” sugli ODL ricevuti:

Ordina gli ODL per Data pianificazione

Associa un numero sequenziale (da 1) a N per tutti gli ordini su quella campata

L’utente Elive lancia l’elaborazione selezionando l’anno Periodo

Elive associa gli ordini alle campate utilizzando le chiavi:

EAM.Campata=Elive.Campata

Anno(EAM.DataPianificazione)=Elive.Anno [si intende l’anno presente nel sinottico]

PeriodoEAM =Elive.Periodo

Si riporta un esempio nella tabella sottostante:

Il modello fin qui descritto si basa sulla modellazione dell’ODL tramite associazione 1 a 1 con le singole ispezioni

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso INVIO OdL

| Protocollo | REST |
| --- | --- |
| BUS | BWCE |
| Tipo Esposizione | API |
| Frequenza Invocazione | 1 al giorno max |
| Timing (Giornaliera, Mensile, ecc) | Giornaliera |
| Modalità Comunicazione | Sincrona |
| Rete | Intranet |
| Modalità di Autenticazione | Strong Authentication (OAuth2) |
| Protocolli Supportati | HTTPS |
| Formato Dati Supportati | JSON |

### Tracciato logico

Per il seguente flusso verranno valorizzati i seguenti campi:

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
| Codice ordine di lavoro / WONUM | Codice dell’ordine di lavoro EAM | Y |
| Codice dell’attività | Codice dal catalogo attività EAM che rappresenta l’operazione da eseguire | Y |
| Codice Campata | SAP Id della campata | Y |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |

### Gestione errori

Il flusso restituirà un errore in caso:

Ordine di lavoro non valorizzato

## Flusso INVIO PROVVEDIMENTI PIANTE

### Descrizione funzionale interfaccia

Lo scopo principale del flusso è l’inserimento automatico dei Report taglio piante.

I Report taglio piante dovranno essere importati su EAM e serviranno alla creazione da parte del DSS delle interferenze rilevate durante le ispezioni Lidar.

Le tipologie di reportistica che il sistema deve poter ricevere in input sono le seguenti sei:

Catenaria verticale;

Catenaria sbandata;

Ribaltamento;

Catenaria verticale Elive 2.0

Catenaria sbandata Elive 2.0;

Ribaltamento Elive 2.0;

I report da 1 a 3 (Report Tagli) e da 4 a 6 (Report Tagli in Griglia) condividono la stessa struttura del tracciato che sarà spiegato nel dettaglio nei paragrafi sottostanti.

Oltre alle informazioni tecniche relative alle interferenze, il report deve contenere sia il riferimento all’Ordine di Lavoro di EAM dal quale è stato effettuato il rilevamento, che il riferimento esplicito alla tipologia di reportistica (se Report Tagli o Report Tagli in Griglia).

Inoltre, nel report il riferimento alla Campata è identificata tramite il SAP ID e dovrà subire una transcodifica durante l’import perché su EAM la chiave dell’asset nell’OdL è identificata dall’assetnum.

### Logiche d’attivazione

Il flusso si innesca ogni qual voltaoperatore deposita uno o più report in un folder specifico.

### Tipologia d’interazione

Di seguito si riportano le informazioni di dettaglio relative al flusso INVIO PROVVEDIMENTI TAGLIO PIANTE:

| Protocollo | REST |
| --- | --- |
| BUS | BWCE |
| Tipo Esposizione | API |
| Frequenza Invocazione |  |
| Timing (Giornaliera, Mensile, ecc) | Giornaliera |
| Modalità Comunicazione |  |
| Rete | Intranet |
| Modalità di Autenticazione | Strong Authentication (OAuth2) |
| Protocolli Supportati | HTTPS |
| Formato Dati Supportati | JSON |

### Tracciato logico

Per i che vanno da 1 a 3 (Catenaria verticale, Catenaria sbandata, Ribaltamento), sarà utilizzato lo stesso report

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
|  |  |  |
| Chiave univoca del record | ID progressivo che identifica univocamente il record all’interno del file | Y |
| Campata | SAP id della campata | Y |
| Nvio Dist int | distanza della pianta che pilota il campo priorità | Y |
| Progres. | Numero progressivo della campata | Y |
| Priorità | priorità dell'intervento - DOMINIO: DIFFERIBILE/URGENTE | Y |
| C. Est int | Coordinata est della pianta | Y |
| C. Nord int | Coordinata nord della pianta | Y |
| Tipologia Report | Dominio di valori [ 1)Catenaria Verticale 2)Catenaria Sbandata 3)Ribaltamento ] | Y |
| Numero Odl Eam | Codice dell’ordine di lavoro EAM | Y |

(Catenaria verticale Elive 2.0, Catenaria sbandata Elive 2.0, Ribaltamento Elive 2.0), sarà utilizzato un report differente

| Dato | Descrizione funzionale | Obbligatorio |
| --- | --- | --- |
|  |  |  |
| Campata | codice della campata | Y |
| Cod_griglia | è il codice della macro-cella di appartenenza del taglio | Y |
| t_s | indica se il taglio è singolo (1 sì, 0 no); | Y |
| t_a | indica se il taglio è areale (1 sì, 0 no); | Y |
| Est_cen | longitudine, nel sistema di riferimento della linea, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | Y |
| Nord_cen | latitudine, nel sistema di riferimento della linea, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | Y |
| X_cen | coordinata x, nel sistema di riferimento locale che ha per origine il sostegno di inizio, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | Y |
| Y_cen | coordinata y, nel sistema di riferimento locale che ha per origine il sostegno di inizio, del centroide dell'aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | Y |
| Dist_int | distanza in metri dal cavo più vicino | Y |
| Anno_comp | anno di riferimento | Y |
| Periodo_comp | Periodo di riferimento | Y |
| In_fascia | indica se il taglio è in fascia oppure no (1 sì, 0 no). I tagli fuori fascia sono individuati con l'algoritmo del ribaltamento; | Y |
| Cat_vert | indica se il taglio è individuato con riferimento alla catenaria verticale oppure no (1 sì, 0 no) | Y |
| Cat_sband | indica se il taglio è individuato con riferimento alla catenaria sbandata oppure no (1 sì, 0 no) | Y |
| ribalt | indica se il taglio è individuato con l'algoritmo del ribaltamento oppure no (1 sì, 0 no) | Y |
| Priorità | indica se il taglio è URGENTE o DIFFERIBILE | Y |
| Tipologia Report | 1)Catenaria Verticale Elive 2.0 2)Catenaria Sbandata Elive 2.0 3)Ribaltamento Elive 2.0 | Y |
| Numero Odl Eam | Codice dell’ordine di lavoro EAM | Y |

### Gestione errori

Il report verrà nel caso in cui si verifichino queste casistiche di errore:

1) Se il SAPID della Campata non è presente in EAM

2) Se il numero dell’ OdL non è presente su EAM

3) L’OdL indicato nel report non ha un Piano di Lavoro relativo ai provvedimenti di taglio piante (lista da ricevere dal business)

4) La coppia OdL - Campata indicata nel report non è presente su EAM.

5) l’OdL non deve trovarsi nello stato “Chiuso Tecnicamente” ma in uno stato precedente perché dallo stato “chiuso Tecnicamente” non è più possibile inserire le difformità.

per l’approvazione di questo documento
