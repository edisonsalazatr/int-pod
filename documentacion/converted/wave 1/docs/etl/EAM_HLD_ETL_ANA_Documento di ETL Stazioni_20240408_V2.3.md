# EAM_HLD_ETL_ANA_Documento di ETL Stazioni_20240408_V2.3

|  | ETL Stazioni – Documento di High Level Design |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Relazione: | FUNCTIONAL CONVERSION DESIGN DOCUMENT (FCDD) |

Storia delle Revisioni

| Rev.n° | Motivo della Revisione | Data |
| --- | --- | --- |
| 1.0 | Primo rilascio RTI | 04/12/2023 |
| 1.1 | Revisione ICT | 05/12/2023 |
| 1.2 | Secondo rilascio RTI | 14/12/2023 |
| 1.3 | Revisione ICT | 15/12/2023 |
| 1.4 | Terzo rilascio RTI | 22/12/2023 |
| 1.5 | Revisione ICT | 04/01/2024 |
| 1.6 | Quarto rilascio RTI | 15/01/2024 |
| 1.8 | Revisione ICT | 29/01/2024 |
| 2.0 | Quinto rilascio RTI | 15/03/2024 |
| 2.3 | Revisione ICT: adattamento HLD al nuovo design ETL stazioni | 8/04/2023 |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |
|  |  |  |

Approvazione ed emissione

|  |  | Data | Firma |
| --- | --- | --- | --- |
| Redatto da: | RTI | 01/12/2023 |  |
| Riesaminato da: | RTI |  |  |
| Approvato da: |  |  |  |

# Introduzione

Il presente documento ha l’obiettivo di dettagliare il processo di conversione funzionale (Functional Conversion Design, FCD). La definizione del processo di conversione funzionale è uno degli step della progettualità per l’implementazione della soluzione di asset management, come di seguito riportato.

Questo documento è una prima fase di progettazione di alto livello (high-level) in cui viene specificata la procedura di importazione SAP-EAM sulla base di una serie di assunzioni, regole e criteri di trasformazione discussi nella prima fase (wave-1) relativi al caricamento degli asset di tipo STAZIONE. Questo documento è stato redatto sulla base delle versioni dei documenti indicati nella sezione Documenti correlati con particolare enfasi alle specifiche di mapping fornite dal business per asset (Documento #1) e caratteristiche (Documento #2). Potrebbero essere necessari aggiornamenti a questo documento qualora siano generate nuove versioni dei documenti di riferimento.

Tale documento potrà essere modificato durante lo sviluppo del progetto al fine di registrare eventuali modifiche necessarie, rilevate in fasi successive rispetto al disegno. Ulteriori dettagli tecnici saranno forniti successivamente tramite documento di Low Level Design.

I documenti citati all’interno di questo HLD sono consultabili accedendo alla repository condivisa: Documenti correlati.

# Processo ETL

Il processo di ETL (extract-transform-load) consiste in una prima fase di estrazione che prevede l’estrazione dei dati SAP (sedi tecniche, equipment, caratteristiche e anagrafiche accessorie) oggetto di elaborazione da parte della procedura. L’estrazione è realizzata mediante estrattori che partendo dalla base dati SAP esporteranno dati grezzi (raw data) in tabelle di interfaccia accessibili dallo schema del database EAM. I dati esportati saranno oggetto di un secondo step di trasformazione verso tabelle custom definite nello schema del database EAM. L’obiettivo di questa fase è di normalizzare e arricchire i dati con informazioni specifiche del data model EAM. Ad esempio, verrà identificata la classificazione EAM della sede tecnica/equipment in base a STRNO/RBNR. Infine, il processo prevede l’ultimo step di caricamento degli asset EAM.

Ciascuna fase sarà realizzata mediante un flusso dedicato che sarà in grado di operare su un gruppo di stazioni fornito come input. L’esecuzione delle varie fasi su blocchi di molteplici stazioni apre la strada all’avvio parallelo di caricamenti su segmenti di dati che potranno via via essere automaticamente determinati da un orchestratore in base alle stazioni in attesa di caricamento, alle stazioni che hanno generato scarti e ai conseguenti ricaricamenti in seguito alle opportune bonifiche sul dato.

# Estrazione

Il primo step del processo (estrazione) consiste nel caricamento dei dati SAP raw nelle tabelle di interfaccia presenti nel database EAM. Il popolamento di queste tabelle sarà eseguito in due fasi:

estrazione dati SAP in un database locale (noto come database on-premise).

ribaltamento dati via DB-link da database on-premise nelle tabelle di interfaccia presenti sul database EAM.

Il ribaltamento si rende necessario in quanto la procedura di trasformazione subirebbe impatti prestazionali non trascurabili accedendo alle tabelle on-premise collegate via DB-link. Pertanto, così come già previsto per le linee, si è optato per il ribaltamento una tantum in locale dei dati estratti, permettendo così allo step di trasformazione di operare su queste tabelle efficientemente. Per maggiori dettagli si rimanda al documento #4.

La struttura delle tabelle sul database EAM riflette l’esatta struttura dei dati SAP. Il contenuto di queste tabelle non sarà oggetto di manipolazione in loco, ma costituirà l’input per la fase successiva di trasformazione.

Si riporta di seguito l’elenco delle tabelle di interfaccia.

| Tabella | Dati SAP | Descrizione |
| --- | --- | --- |
| X3SEDITECHSAP_STZ | Sedi tecniche | Questa tabella conterrà le sedi tecniche estratte da SAP. |
| X3CARATSAP_STZ | Caratteristiche sedi tecniche ed equipment | Questa tabella conterrà le caratteristiche estratte da SAP. |
| X3EQUIPMENTSAP_STZ | Equipment | Questa tabella conterrà gli equipment estratti da SAP. |

# Trasformazione

Il secondo step del processo (trasformazione) consiste in una prima elaborazione dei dati estratti al fine di arricchire il dato con informazioni funzionali allo step successivo di caricamento. Le tabelle prodotte dallo step di estrazione costituiscono l’input di questo step. Lo step di trasformazione opererà il ribaltamento dei dati di input nelle corrispondenti tabelle di output effettuando opportune trasformazioni che verranno dettagliate di seguito.

Si riporta nella tabella seguente la corrispondenza delle tabelle di input e quelle di output.

| Tabella di input | Tabella di output |
| --- | --- |
| X3SEDITECHSAP_STZ | X3ETLSTSAPS2 |
| X3CARATSAP_STZ | X3ETLCARATSAPS2 |
| X3EQUIPMENTSAP_STZ | X3ETLEQSAPS2 |

Tabella 1. Tabelle di trasformazione

Si riportano di seguito le principali trasformazioni per le singole tabelle.

| Colonna di input | Colonna di output | Nota |
| --- | --- | --- |
|  | X3ETLSTSAPS2ID | Generato da sequenza DB |
| EQFNR | EQFNR |  |
| BUKRS | BUKRS |  |
| CITY | CITY |  |
| COUNTRY | COUNTRY |  |
| FLTYP | FLTYP |  |
| GEWRK | GEWRK |  |
| INBDT | INBDT |  |
| INGRP | INGRP |  |
| KOKRS | KOKRS |  |
| KOSTL | KOSTL |  |
| LIVELLOSEDETECNICA | SAPLEVEL |  |
| MSGRP | MSGRP |  |
| NAME1 | NAME1 |  |
| NAME2 | NAME2 |  |
| PLTXT | PLTXT |  |
| POST_CODE1 | POST_CODE1 |  |
| REGION | REGION |  |
| STREET | STREET |  |
| STRNO | STRNO |  |
| STTXU | STTXU | PROG, CANT, DATT: INAT ESER: DATT ATTV: ATTV NADI: NADI NAND: NAND CEAL: CEAL DEMO: DEMO |
| SWERK | SWERK |  |
| TPLMA | TPLMA |  |
| TPLNR | TPLNR |  |
| ZZSOCP | ZZSOCP |  |
| STRNO | ROOTSTRNO | substr(STRNO,1,6) |
| STRNO | SAPCLASS | Ricavato mediante criteri Sysnet a partire da STRNO |
| STRNO | CLASSSTRUCTUREID | Ricavato mediante criteri Sysnet a partire da STRNO |

Tabella 2. Trasformazione sedi tecniche

| Colonna di input | Colonna di output | Nota |
| --- | --- | --- |
|  | X3ETLEQSAPS2ID | Generato da sequenza DB |
| EQUNR | EQUNR | substr(EQUNR,9) |
| RBNR | RBNR |  |
| EQKTX | EQKTX |  |
| STTXU | STTXU |  |
| TPLNR | TPLNR |  |
| TPLNR | PARENTSTRNO | SUBSTR(TPLNR,1,14) |
| INBDT | INBDT |  |
| POSNR | POSNR |  |
| HERST | HERST |  |
| TYPBZ | TYPBZ |  |
| MAPAR | MAPAR |  |
| BAUJJ | BAUJJ |  |
| BAUMM | BAUMM |  |
| SERGE | SERGE |  |
| TPLNR | ROOTSTRNO | substr(TPLNR,1,6) |
| RBNR | CLASSSTRUCTUREID | Mapping tramite RBNR e STRNO della ST superiore |
| RBNR | SAPCLASS | Mapping tramite RBNR e STRNO della ST superiore |

Tabella 3. Trasformazione equipment

A valle del ribaltamento lo step di trasformazione eseguirà ulteriori trasformazioni/check. Ogni operazione di check comporterà il popolamento di eventuali anomalie dello step di trasformazione nella tabella X3ETLSTZSCARTI che permetterà di identificare le stazioni che verranno caricate parzialmente, ossia al netto degli “scarti”.

| Colonna di input | Colonna di output | Nota |
| --- | --- | --- |
|  | X3ETLCARATSAPS2ID | Generato da sequenza DB |
| ATNAM | NAME |  |
| OBJECT_ID | ROOTSTRNO | SUBSTR(OBJECT_ID, 1, 6) |
| STRNO | OBJECT_ID |  |
| OBJNR | OBJNR |  |
| KLASSE | CLASS |  |
| CHAR_TYP | CHAR_TYP |  |
| NUM_TYP | NUM_TYP |  |
| DEC_TYP | DEC_TYP |  |
| DAT_TYP | DAT_TYP |  |
| BOO_TYP | BOO_TYP |  |

Table 4. Trasformazione caratteristiche ST

| Colonna di input | Colonna di output | Nota |
| --- | --- | --- |
|  | X3ETLCARATSAPS2ID | Generato da sequenza DB |
| ATNAM | NAME |  |
| ROOTSTRNO (EQ) | ROOTSTRNO |  |
| OBJNR | OBJNR | SUBSTR(OBJECTNR, 11) |
| KLASSE | CLASS |  |
| CHAR_TYP | CHAR_TYP |  |
| NUM_TYP | NUM_TYP |  |
| DEC_TYP | DEC_TYP |  |
| DAT_TYP | DAT_TYP |  |
| BOO_TYP | BOO_TYP |  |

Table 5. Trasformazione caratteristiche EQ

# Caricamento

Lo step di caricamento si basa sui dati trasformati contenuti nelle tabelle elencate nel paragrafo 3. La procedura scarta a priori dati non correttamente trasformati e richiede, a parità di stazione, la rimozione di eventuali precedenti caricamenti non andati a buon fine (rimozione eseguita automaticamente dalla procedura stessa).

Il caricamento si articola in tre macro-fasi di seguito elencate:

Creazione asset EAM

Valorizzazione specifiche tecniche asset

Popolamento strutture dati connettore

Di seguito verranno forniti i dettagli delle tre macro-fasi.

## Creazione asset EAM

Questa macro-fase popola le tabelle EAM relative agli asset EAM partendo dalle tabelle di trasformazione delle sedi tecniche e degli equipment. La creazione avviene partendo dalla creazione degli asset di primo livello e poi a seguire fino al quarto livello per tutte le sedi tecniche ed equipment che hanno un mapping univoco fra il data-model SAP e EAM. Eventuali casi speciali (come, ad esempio, lo split dei trasformatori in macchina, sistema di raffreddamento e variatore) vengono gestiti a valle della creazione di asset univocamente mappati. Questa macro-fase ha in carico anche il popolamento di strutture dati accessorie quali ad esempio tabelle di struttura gerarchica (asset ancestors), transazioni, audit, storico asset.

## Valorizzazione specifiche tecniche asset

Questa macro-fase popola le tabelle EAM relative alle specifiche tecniche degli asset partendo dalle tabelle di trasformazione delle caratteristiche tecniche. Questa fase si avvale della tabella di regole X3ETLSTZRULES dedotta dalle regole di mapping fornite nel documento #2. In linea di massima questa tabella indica per ogni caratteristica SAP la corrispondente specifica tecnica EAM e la regola di mapping da adottare. Le seguenti regole di mapping sono state identificate:

SameValue: il valore della caratteristica SAP della Sede Tecnica deve essere riportato nella corrispondente specifica tecnica EAM (al netto di eventuali conversioni di tipo)

SameValueEq: il valore della caratteristica SAP dell’Equipment deve essere riportato nella corrispondente specifica tecnica EAM (al netto di eventuali conversioni di tipo)

SameValueChildren: il valore della caratteristica SAP deve essere riportato nella corrispondente specifica tecnica EAM degli asset figli della ST aventi una determinata classificazione (al netto di eventuali conversioni di tipo)

RifAssetEq: il valore della caratteristica SAP dell’Equipment deve essere convertito in riferimento asset (ASSETNUM) e riportato nella corrispondente specifica tecnica EAM

SameValueAttrToSpecEq: il valore dell’attributo di testata SAP dell’Equipment deve essere riportato nella corrispondente specifica tecnica EAM

SameValueClassEq: il valore della caratteristica SAP dell’Equipment deve essere riportato nella corrispondente specifica tecnica EAM del corretto tipo di asset (macchina ,variatore o sistema di raffreddamento)

SameValueAllEq: il valore della caratteristica SAP dell’Equipment deve essere riportato nella corrispondente specifica tecnica EAM (al netto di eventuali conversioni di tipo). A differenza della regola SameValueEq che si applica a Equipment specifici, questa è valida per tutti

Eventuali gestioni particolari (non assimilabili a casi generali) sono gestite a valle della elaborazione delle regole di mapping. Si riporta la lista dei casi particolari

valorizzazione degli attributi HERST e TYPBZ

valorizzazione degli attributi BAUJJ e BAUMM

valorizzazione dell’attributo POSNR

## Popolamento strutture dati connettore

Questa macro-fase popola le tabelle EAM relative al connettore (X3SAPASSET e X3SAPTOMAXOBJECT) creando i legami fra l’anagrafica SAP ed EAM.

# Documenti correlati

I documenti sotto menzionati sono consultabili alla seguente repository condivisa: Documenti correlati.

| ID | Nome documento | Descrizione |
| --- | --- | --- |
| 1 | EAM Stazioni - Classificazione asset integrata con HVDC e ICS_20240326.xlsx | Mapping sede tecniche ed equipment SAP verso asset EAM |
| 2 | Mapping Anagrafica EAM Stazioni_Dati 20240328.xlsx | Mapping caratteristiche sedi tecniche ed equipment SAP verso specifiche tecniche asset EAM |
| 3 | Terna - SAP DB Connection to Oracle |  |
