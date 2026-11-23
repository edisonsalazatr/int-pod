Test X4FISICO – OdL singoli e aggregatori (mck)

09/11/2026 · Giuseppe Cotrufo

# Contesto

In mck il calcolo del fisico (X4FISICO) è stato riscritto per correggere i padri aggregatori con fisico diverso dalla somma dei figli (INC0738622, OdL 11544305). Questo documento riporta i test sugli OdL singoli (13 casi, tutti superati) e sugli OdL aggregatori (4 casi, tutti superati).

Script attivi in mck (schema MAXIMO2) al salvataggio di WORKORDER:

| Launch point | Script | Ruolo |
| --- | --- | --- |
| X4CALCFISICOWO (id 346) | X4CALCFISICOWO | calcolo del fisico pianificato (X4FISICOPLAN) |
| X4CALCFISICOWO (id 1358) | X4CALCFISICO | nuovo calcolo del fisico (X4FISICO), solo al cambio stato, aggregatori esclusi |

I test sugli OdL singoli sono stati eseguiti da Monitoraggio OdL → Modifica stato, portando ogni OdL da AVVIATO a ESEGUITO. I test sugli aggregatori sono stati eseguiti con l'utenza XT8001 dai flussi "Stato eseguito massivo" (Monitoraggio OdL, X4ESEGMASSIVO) e "Passaggio di stato in ESEGUITO" (Area di validazione OdL, X4ESEGUITOMASS).

# Esito test OdL singoli

13 OdL singoli sono passati in ESEGUITO il 09/11/2026 tra le 10:52 e le 11:00 con fisico uguale al valore atteso. Il valore atteso è calcolato con la stessa logica di getMainAncestorNum: campata o tratta lette direttamente, sostegno ed estremo risolti tramite il prefisso di 16 caratteri di X3SAPID.

| Caso | OdL | UdM | Classe asset | 1° livello | Campata usata | Atteso | Ottenuto | Esito |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Campata | PL_2746207 | KM | CAMPATA AEREA | sé stessa | 18430114 | 0,58 | 0,58 | OK |
| Estremo cavo | 4618697 | KM | ESTREMO CAVO | sé stesso | 30016502 (via SAP) | 0,479 | 0,479 | OK |
| Tratta cavo | 4627878 | QTY | TRATTA CAVO | sé stessa | — | 0,43 | 0,43 | OK |
| Campata, 3° livello | PRL-23847B1-082-BIS-COND | KM | CONDUTTORI | 13815165 | 13815165 | 0,11196 | 0,11196 | OK |
| Campata, 4° livello | PRL-23024D1-244-----FDG | KM | FUNE DI GUARDIA | 22507106 | 22507106 | 0,06862 | 0,06862 | OK |
| Campata, 4° livello | PL_1820512 | KM | DISTANZIATORI | 13910281 | 13910281 | 0,7971 | 0,7971 | OK |
| Sostegno, 3° livello | DVTT2_16462564 | QTY | ATTACCHI CONDUTTORE | 18506236 | — | 1 | 1 | OK |
| Sostegno, 4° livello | AVTT3_16200236 | KM | STRUTTURA | 18573926 | 18426195 (via SAP) | 0,186 | 0,186 | OK |
| Sostegno, 4° livello | PL_644591 | KM | ISOLATORE CONDUTTORE | 14106096 | 13815232 (via SAP) | 0,171 | 0,171 | OK |
| Estremo, 3° livello | CON0047037 | QTY | TERMINALI | 14224872 | — | 1 | 1 | OK |
| Tratta, 3° livello | PRL-23981B1-BK2600 | KM | CAVI | 18593906 | 18593906 | 0,35 | 0,35 | OK |
| Tratta, 4° livello | D177124895 | KM | CAVO DI POTENZA | 14219715 | 14219715 | 0,455 | 0,455 | OK |
| Terreni e infrastrutture | 4624491 | ETT | TERRENI E INFRASTRUTTURE | stazione | — | 0,66 | 0,66 | OK |

Su AVTT3_16200236 il pianificato vale 0,19 e il fisico 0,186: il pianificato è arrotondato, la campata è la stessa.

# Query di verifica

Tutte le query sono sullo schema MAXIMO2 di mck, site SIT01.

## Reset prima del test

update workorder set X4FISICO = 0, status = 'AVVIATO', X4GEOCALLCLOSED = 1

where wonum in ('PL_2746207','4618697','4627878','PRL-23847B1-082-BIS-COND','PRL-23024D1-244-----FDG',

'PL_1820512','DVTT2_16462564','AVTT3_16200236','PL_644591','CON0047037',

'PRL-23981B1-BK2600','D177124895','4624491');

## Esito dopo il passaggio in ESEGUITO

SELECT w.WONUM, w.STATUS, w.X4UM, w.X4FISICOPLAN, w.X4FISICO, w.CHANGEDATE, w.CHANGEBY,

(SELECT MAX(s.CHANGEDATE) FROM MAXIMO2.WOSTATUS s

WHERE s.WONUM = w.WONUM AND s.SITEID = w.SITEID AND s.STATUS = 'ESEGUITO') ULTIMO_ESEGUITO

FROM MAXIMO2.WORKORDER w

WHERE w.SITEID = 'SIT01'

AND w.WONUM IN ('PL_2746207','4618697','4627878','PRL-23847B1-082-BIS-COND','PRL-23024D1-244-----FDG',

'PL_1820512','DVTT2_16462564','AVTT3_16200236','PL_644591','CON0047037',

'PRL-23981B1-BK2600','D177124895','4624491')

ORDER BY w.WONUM;

## Campata attesa per un OdL (stessa logica di getMainAncestorNum)

SELECT w.WONUM, a.ASSETNUM, ca.CLASSIFICATIONID CLASSE_ASSET,

NVL(a.ANCESTOR, a.ASSETNUM) L1, cx.CLASSIFICATIONID CLASSE_L1,

CASE WHEN cx.CLASSIFICATIONID IN ('CAMPATA AEREA','TRATTA CAVO') THEN NVL(a.ANCESTOR, a.ASSETNUM)

ELSE (SELECT MIN(r.TARGETASSETNUM) KEEP (DENSE_RANK FIRST ORDER BY r.X3SAPID)

FROM MAXIMO2.ASSETLOCRELATION r

WHERE r.X3SAPID IN (SELECT SUBSTR(r2.X3SAPID,1,16) FROM MAXIMO2.ASSETLOCRELATION r2

WHERE r2.TARGETASSETNUM = NVL(a.ANCESTOR, a.ASSETNUM)

AND r2.SITEID = a.SITEID)) END CAMPATA_ATTESA

FROM MAXIMO2.WORKORDER w

JOIN MAXIMO2.ASSET a ON a.ASSETNUM = w.ASSETNUM AND a.SITEID = w.SITEID

JOIN MAXIMO2.CLASSSTRUCTURE ca ON ca.CLASSSTRUCTUREID = a.CLASSSTRUCTUREID

JOIN MAXIMO2.ASSET x ON x.ASSETNUM = NVL(a.ANCESTOR, a.ASSETNUM) AND x.SITEID = a.SITEID

JOIN MAXIMO2.CLASSSTRUCTURE cx ON cx.CLASSSTRUCTUREID = x.CLASSSTRUCTUREID

WHERE w.SITEID = 'SIT01' AND w.WONUM = :wonum;

## Lunghezza della campata o superficie (KM: fisico atteso = NUMVALUE / 1000)

SELECT ASSETNUM, ASSETATTRID, NUMVALUE

FROM MAXIMO2.ASSETSPEC

WHERE SITEID = 'SIT01' AND ASSETATTRID IN ('LIN_LUNGHEZZA','STZ_SUPERFICIE_AREA_VERDE')

AND ASSETNUM = :campata;

# Test OdL aggregatori

4 aggregatori sono stati eseguiti l'11/11/2026 con il fisico del padre uguale alla somma dei fisici dei figli. Il meccanismo verificato è lo stesso in tutti i casi: quando un figlio passa in ESEGUITO, X4CALCFISICO calcola il fisico del figlio (KM = LIN_LUNGHEZZA della campata / 1000, QTY = fisico pianificato) e lo somma al fisico del padre. Il padre aggregatore è escluso dal calcolo diretto: quando passa in ESEGUITO il suo fisico resta la somma dei figli.

| Caso | Flusso | Padre | UdM | Figli | Padre atteso | Padre ottenuto | Esito |
| --- | --- | --- | --- | --- | --- | --- | --- |
| Campata | Stato eseguito massivo (X4ESEGMASSIVO) | HVTT2_8425531 | KM | 2 | 0,796 | 0,796 | OK |
| Campata | Stato eseguito massivo (X4ESEGMASSIVO) | FVTT5_8431621 | KM | 3 | 1,1448 | 1,1448 | OK |
| Struttura su sostegno | Area di validazione OdL (X4ESEGUITOMASS) | 4621359 | QTY | 3 | 3 | 3 | OK |
| Attacchi su sostegno (via SAP) | Semaforo sul figlio + Salva (X4ESEWOFIGLIO) | 4758638 | KM | 6 | 1,46004 | 1,46004 | OK |

## Caso 1 – HVTT2_8425531: figli su campata, Stato eseguito massivo

Ogni figlio è una campata aerea (asset di 2° livello, senza ancestor): il fisico del figlio è la sua LIN_LUNGHEZZA divisa per 1000. Il padre vale 0,449 + 0,347 = 0,796 km.

| Figlio | Campata | LIN_LUNGHEZZA (m) | Fisico atteso | Fisico ottenuto | ESEGUITO (WOSTATUS) |
| --- | --- | --- | --- | --- | --- |
| HVTT2_8425531_FCNG_001 | 18452493 | 449 | 0,449 | 0,449 | 11/11 19:22:34, XT8001 |
| HVTT2_8425531_FCNG_002 | 18452550 | 347 | 0,347 | 0,347 | 11/11 19:23:38, XT8001 |
| HVTT2_8425531 (padre) | — | — | 0,796 | 0,796 | 11/11 19:24:18, MAXADMIN (escalation X4WOESE) |

Il padre è passato in ESEGUITO dopo i figli tramite l'escalation X4WOESE e il fisico è rimasto 0,796: il padre non viene ricalcolato come OdL singolo.

## Caso 2 – FVTT5_8431621: figli su campata, Stato eseguito massivo

Tre figli su campate aeree, eseguiti dal massivo in 9 secondi. Il padre vale 0,33105 + 0,48709 + 0,32666 = 1,1448 km; il pianificato del padre (1,14) è arrotondato.

| Figlio | Campata | LIN_LUNGHEZZA (m) | Fisico atteso | Fisico ottenuto | ESEGUITO (WOSTATUS) |
| --- | --- | --- | --- | --- | --- |
| FVTT5_8431621_FCNG_001 | 18470283 | 331,05 | 0,33105 | 0,33105 | 11/11 19:24:41, XT8001 |
| FVTT5_8431621_FCNG_002 | 18470307 | 487,09 | 0,48709 | 0,48709 | 11/11 19:24:32, XT8001 |
| FVTT5_8431621_FCNG_003 | 18470331 | 326,66 | 0,32666 | 0,32666 | 11/11 19:24:36, XT8001 |
| FVTT5_8431621 (padre) | — | — | 1,1448 | 1,1448 | 11/11 19:25:18, MAXADMIN (escalation X4WOESE) |

Su FVTT5_8431621_FCNG_002 il valore a DB è 0,48708999999999997: è la rappresentazione in virgola mobile di 0,48709, a video compare 0,48709.

## Caso 3 – 4621359: figli QTY su struttura di sostegno, Area di validazione OdL

Tre figli QTY con fisico pianificato 1 ciascuno: il fisico del figlio è il pianificato, il padre vale 1 + 1 + 1 = 3. Il padre è stato selezionato in Area di validazione OdL con la query "OdL per eseguito massivo" e portato in ESEGUITO con "Passaggio di stato in ESEGUITO": figli e padre risultano eseguiti nello stesso secondo dall'utenza XT8001.

| Figlio | Sostegno (1° livello) | Fisico pianificato | Fisico atteso | Fisico ottenuto | ESEGUITO (WOSTATUS) |
| --- | --- | --- | --- | --- | --- |
| PRL-23011A1-221-A---DIGIL | 14140391 | 1 | 1 | 1 | 11/11 20:57:30, XT8001 |
| PRL-23011A1-230-----DIGIL | 14131716 | 1 | 1 | 1 | 11/11 20:57:30, XT8001 |
| PRL-23011A1-264-----DIGIL | 14134459 | 1 | 1 | 1 | 11/11 20:57:30, XT8001 |
| 4621359 (padre) | — | 3 | 3 | 3 | 11/11 20:57:30, XT8001 |

Con la nuova versione di X4ESEGUITOMASS ogni figlio viene salvato in un set dedicato e il padre viene riletto da DB dopo i figli, quindi trova già la somma.

## Caso 4 – 4758638: figli su attacchi di sostegno, semaforo sul figlio e Salva

I figli sono attacchi conduttore di un sostegno: la campata non è l'asset stesso ma viene trovata tramite il prefisso di 16 caratteri del codice SAP del sostegno. Ogni figlio prende la lunghezza della propria campata e la somma al padre: dopo il primo figlio (4758658) il padre valeva 0,2029, dopo il sesto 0,2068 + 0,31676 + 0,2029 + 0,2889 + 0,19978 + 0,2449 = 1,46004 km, pari al fisico pianificato del padre. I figli sono stati eseguiti dalla tabella OdL Aggregati del padre tra le 21:22 e le 21:25 dell'11/11/2026.

| Figlio | Sostegno | Campata (via SAP) | LIN_LUNGHEZZA (m) | Fisico atteso | ESEGUITO (WOSTATUS) | Fisico ottenuto |
| --- | --- | --- | --- | --- | --- | --- |
| 4758656 | 14134161 | 13833529 | 206,8 | 0,2068 | 21:24:50, XT8001 | 0,2068 |
| 4758657 | 14128298 | 13827583 | 316,76 | 0,31676 | 21:25:23, XT8001 | 0,31676 |
| 4758658 | 14129103 | 13834736 | 202,9 | 0,2029 | 21:22:03, XT8001 | 0,2029 |
| 4758659 | 14131031 | 13828136 | 288,9 | 0,2889 | 21:25:02, XT8001 | 0,2889 |
| 4758660 | 14128665 | 13833814 | 199,78 | 0,19978 | 21:25:13, XT8001 | 0,19978 |
| 4758661 | 14130390 | 13827621 | 244,9 | 0,2449 | 21:25:46, XT8001 | 0,2449 |
| 4758638 (padre) | — | — | — | 1,46004 | AVVIATO (passa in ESEGUITO con l'escalation X4WOESE) | 1,46004 |

Per preparare il caso è stato necessario allineare in mck la data di fine prevista dell'OdL (TARGCOMPDATE) alla data di fine della risorsa pianificata: con le date originali il cambio stato veniva bloccato dal controllo BMXZZ7150E sulle date dei Labor, che non riguarda il calcolo del fisico.

Campata attesa per i figli su sostegno (via prefisso SAP):

SELECT f.WONUM, f.STATUS, f.X4FISICO, NVL(a.ANCESTOR, a.ASSETNUM) SOSTEGNO,

(SELECT MIN(r.TARGETASSETNUM) KEEP (DENSE_RANK FIRST ORDER BY r.X3SAPID)

FROM MAXIMO2.ASSETLOCRELATION r

WHERE r.X3SAPID IN (SELECT SUBSTR(r2.X3SAPID,1,16) FROM MAXIMO2.ASSETLOCRELATION r2

WHERE r2.TARGETASSETNUM = NVL(a.ANCESTOR, a.ASSETNUM) AND r2.SITEID = a.SITEID)) CAMPATA

FROM MAXIMO2.WORKORDER f

JOIN MAXIMO2.ASSET a ON a.ASSETNUM = f.ASSETNUM AND a.SITEID = f.SITEID

WHERE f.SITEID = 'SIT01' AND f.ISTASK = 0 AND f.PARENT = '4758638'

ORDER BY f.WONUM;

## Query di evidenza – aggregatori

Stato e fisico di padri e figli:

SELECT w.WONUM, w.PARENT, w.STATUS, w.X4UM, w.X4FISICOPLAN, w.X4FISICO, w.CHANGEDATE, w.CHANGEBY

FROM MAXIMO2.WORKORDER w

WHERE w.SITEID = 'SIT01' AND w.ISTASK = 0

AND (w.WONUM IN ('HVTT2_8425531','FVTT5_8431621','4621359','4758638')

OR w.PARENT IN ('HVTT2_8425531','FVTT5_8431621','4621359','4758638'))

ORDER BY NVL(w.PARENT, w.WONUM), w.PARENT NULLS FIRST, w.WONUM;

Padre uguale alla somma dei figli eseguiti (ESITO = OK):

SELECT p.WONUM PADRE, p.STATUS, p.X4UM, p.X4FISICO FISICO_PADRE,

SUM(CASE WHEN f.STATUS = 'ESEGUITO' THEN NVL(f.X4FISICO,0) ELSE 0 END) SOMMA_FIGLI_ESEGUITI,

CASE WHEN ABS(NVL(p.X4FISICO,0) - SUM(CASE WHEN f.STATUS = 'ESEGUITO' THEN NVL(f.X4FISICO,0) ELSE 0 END)) < 0.00001

THEN 'OK' ELSE 'KO' END ESITO

FROM MAXIMO2.WORKORDER p

JOIN MAXIMO2.WORKORDER f ON f.PARENT = p.WONUM AND f.SITEID = p.SITEID AND f.ISTASK = 0

WHERE p.SITEID = 'SIT01' AND p.WONUM IN ('HVTT2_8425531','FVTT5_8431621','4621359','4758638')

GROUP BY p.WONUM, p.STATUS, p.X4UM, p.X4FISICO

ORDER BY p.WONUM;

Campata e lunghezza usate per ogni figlio KM (fisico atteso = LIN_LUNGHEZZA / 1000):

SELECT f.PARENT, f.WONUM, f.X4FISICO, NVL(a.ANCESTOR, a.ASSETNUM) CAMPATA, s.NUMVALUE LIN_LUNGHEZZA,

s.NUMVALUE / 1000 FISICO_ATTESO

FROM MAXIMO2.WORKORDER f

JOIN MAXIMO2.ASSET a ON a.ASSETNUM = f.ASSETNUM AND a.SITEID = f.SITEID

LEFT JOIN MAXIMO2.ASSETSPEC s ON s.ASSETNUM = NVL(a.ANCESTOR, a.ASSETNUM) AND s.SITEID = a.SITEID

AND s.ASSETATTRID = 'LIN_LUNGHEZZA'

WHERE f.SITEID = 'SIT01' AND f.ISTASK = 0 AND f.PARENT IN ('HVTT2_8425531','FVTT5_8431621')

ORDER BY f.PARENT, f.WONUM;

Sequenza dei cambi stato (figli prima del padre, utente e orario):

SELECT s.WONUM, s.STATUS, s.CHANGEDATE, s.CHANGEBY, s.MEMO

FROM MAXIMO2.WOSTATUS s

WHERE s.SITEID = 'SIT01' AND s.STATUS = 'ESEGUITO'

AND s.CHANGEDATE >= TO_DATE('2026-11-11','YYYY-MM-DD')

AND (s.WONUM IN ('HVTT2_8425531','FVTT5_8431621','4621359','4758638')

OR s.WONUM IN (SELECT WONUM FROM MAXIMO2.WORKORDER

WHERE SITEID = 'SIT01' AND PARENT IN ('HVTT2_8425531','FVTT5_8431621','4621359','4758638')))

ORDER BY s.CHANGEDATE;