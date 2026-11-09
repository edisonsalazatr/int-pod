# Analisi_tecnica_complessiva_DataModelEAM_W2_v2

| Analisi tecnica complessiva Riepilogo Data Model EAM |
| --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Riepilogo Data Model EAM |

Storia delle revisioni

| Rev.n° | Motivo della revisione | Data | Owner |
| --- | --- | --- | --- |
|  |  |  |  |

Approvazione ed emissione

|  | Data | Firma |
| --- | --- | --- |
| Redatto da: |  |  |
| Riesaminato da: |  |  |
| Approvato da: |  |  |

# Obiettivo del documento

Il presente documento ha l’obiettivo di riepilogare il data model disegnato per la wave 2. Comprende i data model già presenti in altri file di analisi tecnica relativi alla documentazione di progetto in ambito w2.

Saranno quindi riportati, in forma sintetica e strutturata, tutti i data model descritti nella documentazione esistente. I modelli dati verranno rappresentati mediante tabelle che conterranno le seguenti colonne:

Campo: nome dell'attributo

Tipo: tipo e lunghezza dell'attributo (se specificata)

Descrizione: descrizione testuale dell'attributo

Obbligatorietà: Campo obbligatorio o opzionale (Y=Sì, N=No)

Nota: Qualora il tipo Maximo o SQL non fosse indicato, ciò significa che non è stato specificato nelle fonti documentali precedenti.

# Riepilogo dei data model EAM

## WORKORDER: Data model OdL

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | Identificativo dell’Asset | N |
| DESCRIPTION | ALN(270) | VARCHAR2(270) | Descrizione OdL | N |
| FNLCONSTRAINT | DATETIME | DATE | Data di massimo positicipo attività | N |
| SCHEDFINISH | DATETIME | DATE | Data schedulata fine | N |
| SCHEDSTART | DATETIME | DATE | Data schedulata inizio | N |
| SNECONSTRAINT | DATETIME | DATE | Data non prima di | N |
| TARGCOMPDATE | DATETIME | DATE | Data di fine lavoro pianificata | N |
| TARGSTARTDATE | DATETIME | DATE | Data di inizio lavoro pianificata | N |
| WONUM | UPPER(25) | VARCHAR2(25) | Ordine di lavoro | Y |
| STATUS | UPPER(16) | VARCHAR2(16) | Stato OdL | Y |
| STATUSDATE | DATE | DATE | Orario in cui è stato modificato l’OdL | Y |
| X4AGGREGATI | YORN(1) | NUMBER | OdL aggregatore | Y |
| X4NATURALAVORO | ALN(15) | VARCHAR2(15) | Assistenza/Sorveglianza | N |
| X4DATAMODELLO | DATE | DATE | Data Modello OdL | N |
| X4POLICY | ALN(20) | VARCHAR2(20) | Identificativo della Policy associata | N |
| X4TTRASF | DURATION(8) | FLOAT | Tempo trasferimento | N |
| X4FISICO | FLOAT | FLOAT | Fisico consuntivato | N |
| X4TRAINANTE | ALN(10) | VARCHAR2(10) | Trainante | N |
| X4UM | ALN(5) | VARCHAR2(5) | Unità di misura | N |
| X4TEMPOPAUSA | DURATION | FLOAT | Tempo pausa | N |
| X4TIPOSPESA | ALN(5) | VARCHAR2(5) | Tipo spesa(CAPEX,OPEX,ANR) | N |
| X4COIN | ALN(25) | VARCHAR2(25) | Codice Coin | N |
| X4NTK | ALN(20) | VARCHAR2(20) | Identificativo NTK SAP | N |
| X4TIPOOI | ALN(15) | VARCHAR2(15) | Tipo ordine interno | N |
| X4NORDINEINTERNO | ALN(20) | VARCHAR2(20) | ID Ordine interno SAP | N |
| X4TIPOATTIVITA | ALN(15) | VARCHAR2(15) | Tipo attività contabile | N |
| X4TIPOORDINE | ALN(15) | VARCHAR2(15) | Tipo ordine contabile | N |
| X4WBS | ALN(20) | VARCHAR2(20) | WBS contabile | N |
| X4WBSPIAN | ALN(20) | VARCHAR2(20) | WBS pianificata | N |
| X4WBE | ALN(20) | VARCHAR2(20) | WBE contabile | N |
| X4WBEPIAN | ALN(20) | VARCHAR2(20) | WBE pianificata | N |
| X4ODM | ALN(20) | VARCHAR2(20) | Identificativo OdM SAP | N |
| X4SERBATOIO | YORN | NUMBER | OdL Serbatoio | Y |
| CHANGEBY | UPPER(100) | VARCHAR2(100) | Matricola | N |
| CHANGEDATE | DATETIME | DATE | Data ultima modifica | N |
| X4MODULICAR | YORN | NUMBER | Moduli caricati | Y |
| X4SAPCONSOK | YORN | NUMBER | Conferma Ore da SAP | Y |
| X4ANAGRAFICAUPDATE | YORN | NUMBER | Anagrafica aggiornata | Y |
| X4DINIZIOINDISP | DATETIME | DATE | Data inizio indisponibilità | N |
| X4DFINEINDISP | DATETIME | DATE | Data fine indisponibilità | N |
| X4STATUSINDISP | ALN(20) | VARCHAR2(20) | Stato richiesta indisponibilità | N |
| JPNUM | ALN(20) | VARCHAR2(20) | Identificativo PdL | N |
| ESTDUR | DURATION | FLOAT | Durata pianificata | Y |
| X4OUTSOURCING | YORN | NUMBER | OdL OUTSOURCING | Y |
| X4STORNOSAP | UPPER(10) | VARCHAR2(10) | Storno totale SAP | N |
| SITEID | UPPER(8) | VARCHAR2(8) | Sede OdL | Y |
| X3TPREP | FLOAT | FLOAT | Tempo di preparazione totale | N |
| X3TTRASF | DURATION | FLOAT | Tempo spostamento | N |
| ACTSTART | DATETIME | DATE | Data di inizio lavoro effettiva | N |
| ACTFINISH | DATETIME | DATE | Data di fine lavoro effettiva | N |
| WOPRIORITY | INTEGER | NUMBER | Priorità OdL | N |
| EXTERNALREFID | ALN(10) | VARCHAR2(10) | Chiave sistema esterno | N |
| LEAD | UPPER(100) | VARCHAR2(100) | Preposto | N |
| X4GEOCALLCLOSED | YORN | NUMBER | Flag per indicare ordine “Chiuso” in GeoCall | Y |
| X4CLUSTERINDISP | ALN(50) | VARCHAR2(50) | ID Cluster titolare dell’indisponibilità | N |
| X4SAPIDASSET | UPPER(30) | VARCHAR2(30) | ID SAP dell’asset | N |
| X4TRONCONUM | UPPER(25) | VARCHAR2(25) | Codice del tronco | N |
| X4MOTIVOINDISP | ALN(15) | VARCHAR2(15) | Motivo dell’annullamento dell’indisponibilità | N |
| X4ESITOINDISP | UPPER(50) | VARCHAR2(50) | Esito indisponibilità | N |
| X4DESCERRORINDISP | ALN(50) | VARCHAR2(50) | Descrizione errore dell’indisponibilità | N |
| X4SEDEINDISP | ALN(20) | VARCHAR2(20) | Sede di indisponibilità | N |
| X4PIANMPE | UPPER(2) | VARCHAR2(2) | Pianificato MPE, necessario per indicare se l’OdL è stato pianificato precedentemente da MPE | N |
| X4DATAULTIMAPIAN | DATETIME | DATE | Data ultima pianificazione | N |
| OWNERGROUP | UPPER(20) | VARCHAR2(20) | ID del Responsabile attività definito in OdL | N |
| X4INIZIOESCLUSIONE | DATETIME | DATE | Inizio periodo esclusione | N |
| X4FINEESCLUSIONE | DATETIME | DATE | Fine periodo esclusione | N |
| X4EFFORTO | DURATION | FLOAT | Effort Operaio | N |
| X4EFFORTI | DURATION | FLOAT | Effort Impiegato | N |
| X4EFFORTQ | DURATION | FLOAT | Effort Quadro | N |
| CLASSSTRUCTUREID | UPPER(26) | VARCHAR2(26) | Tipologia attività | N |
| X4ASSIEME | ALN(35) | VARCHAR2(35) | Assieme del PdL associato | N |
| X4SOTTOASSIEME | ALN(50) | VARCHAR2(50) | Sottoassieme del PdL associato | N |
| REPORTDATE | DATETIME | DATE | Data di creazione dell’OdL | N |
| X4NOTE | ALN(150) | VARCHAR2(150) | Note | N |
| X4PERIMETROINT | ALN(25) | VARCHAR2(25) | Perimetro di intervento | N |
| X4TIPOODL | ALN(15) | VARCHAR2(15) | Tipologia OdL | N |
| X4SOTTOPROGRAMMA | ALN(50) | VARCHAR2(50) | Cluster di caratterizzazione su alcuni asset specifici | N |
| X4PMI | YORN(1) | NUMBER | OdL per piano (PMI/Programmi) | Y |
| X4CAPACITYEROSAO | INTEGER | NUMBER | Capacity erosa operai da OdL a serbatoio | N |
| X4CAPACITYRESIDUAO | INTEGER | NUMBER | Capacity residua operai da OdL a serbatoio | N |
| X4CAPACITYRESIDUAI | INTEGER | NUMBER | Capacity residua impiegati da OdL a serbatoio | N |
| X4CAPACITYEROSAI | INTEGER | NUMBER | Capacity erosa impiegati da OdL a serbatoio | N |
| X4CAPACITYEROSAQ | INTEGER | NUMBER | Capacity erosa quadri da OdL a serbatoio | N |
| PARENT | UPPER(25) | VARCHAR2(25) | Ordine di lavoro genitore | N |
| X4CODDELINDISP | ALN(25) | VARCHAR2(25) | Codice Delibera della Indisponibilità | N |
| X4FLAGFISICO | INTEGER | NUMBER | Conferme misure consultivo fisico | Y |
| REPORTEDBY | VARCHAR | VARCHAR2(82) | Utente segnalatore | N |
| X4TECMIG | YORN | NUMBER | Identificare ordine sui tronchi(1=tronco di ispezione) | Y |
| X4NOTEMIG | ALN(250) | VARCHAR2(250) | Note | N |
| X4ORDINEPREVALENTE | ALN(25) | VARCHAR2(25) | Per ordini DSS di prevalenza indica il wonum prevalente | N |
| WORKTYPE | UPPER(5) | VARCHAR2(5) | Identifies the work order's type. Some example types are: preventive maintenance, corrective maintenance, emergency maintenace, capital project, and event report. | N |
| LOCATION | UPPER(25) | VARCHAR2(25) | Identifies the work order's location. This is not necessarily the asset's location, however, if an asset is entered, its location will default here | N |
| DJPERROR | ALN(400) | VARCHAR2(400) | The error message when trying to apply the dynamic job plan | N |
| FAILDATE | DATETIME | DATE | Actual Failure Date | N |
| ESTLABHRS | DURATION | FLOAT | Estimated Labor Hours | Y |
| ESTMATCOST | AMOUNT | NUMBER(10,2) | Estimated Material Cost | Y |
| ESTLABCOST | AMOUNT | NUMBER(10,2) | Estimated Labor Cost | Y |
| ESTTOOLCOST | AMOUNT | NUMBER(10,2) | Estimated Tool Cost | Y |
| PMNUM | UPPER(8) | VARCHAR2(8) | Identifies the preventive maintenance record from which the work order was generated. | N |
| ACTLABHRS | DURATION | FLOAT | Actual Labor Hours | Y |
| ACTMATCOST | AMOUNT | NUMBER(10,2) | Actual Material Cost | Y |
| ACTLABCOST | AMOUNT | NUMBER(10,2) | Actual Labor Cost | Y |
| ACTTOOLCOST | AMOUNT | NUMBER(10,2) | Actual Tool Cost | Y |
| HASCHILDREN | YORN | NUMBER | Y if WO has children N if it has no children | Y |
| OUTLABCOST | AMOUN | NUMBER(10,2) | Outside Labor Cost | Y |
| OUTMATCOST | AMOUNT | NUMBER(10,2) | Outside Material Cost | Y |
| OUTTOOLCOST | AMOUNT | NUMBER(10,2) | Outside Tool Cost | Y |
| HISTORYFLAG | YORN | NUMBER | Yes/No field or check box that specifies whether a record is a history record or is currently active. History records are complete, closed, canceled, etc. If a Y is in the History? field or the check box is selected, the record is a history record. On the List tab or the More Search Fields dialog box, you can enter a Y in the History? field to include only history records in the search results. If an N is in the field, Maximo includes only currently active records in the search results. If the field is empty (null), Maximo includes history and active records in the search results. | Y |
| CONTRACT | UPPER(8) | VARCHAR2(8) | Identifies the warranty contract for the asset. | N |
| WOEQ1 | ALN(10) | VARCHAR2(10) | Extra Field Copied From Asset | N |
| WOEQ2 | ALN(10) | VARCHAR2(10) | Extra Field Copied From Asset | N |
| WOEQ3 | ALN(10) | VARCHAR2(10) | Extra Field Copied From Asset | N |
| WOEQ4 | ALN(10) | VARCHAR2(10) | Extra Field Copied From Asset | N |
| WOEQ5 | AMOUNT | NUMBER(10,2) | Extra Field Copied From Asset | N |
| WOEQ6 | DATETIME | DATE | Extra Field Copied From Asset | N |
| WOEQ7 | AMOUNT | NUMBER(15,2) | Extra Field Copied From Asset | N |
| WOEQ8 | ALN(10) | VARCHAR2(10) | Extra Field Copied From Asset | N |
| WOEQ9 | ALN(30) | VARCHAR2(30) | Extra Field Copied From Asset | N |
| WOEQ10 | ALN(10) | VARCHAR2(10) | Extra Field Copied From Asset | N |
| WOEQ11 | ALN(10) | VARCHAR2(10) | Extra Field Copied From Asset | N |
| WOEQ12 | AMOUNT | NUMBER(10,2) | Extra Field Copied From Asset | N |
| WOLABLNK | UPPER(8) | VARCHAR2(8) |  | N |
| OBSERVATION | ALN(8) | VARCHAR2(8) |  | N |
| BACKOUTPLAN | ALN(50) | VARCHAR2(50) |  | N |
| AMCREW | UPPER(8) | VARCHAR2(8) |  | N |
| PHONE | ALN(20) | VARCHAR2(20) | The phone number (usually a work site telephone number) associated with the work order. If the Reported By person has a phone number associated with it, that phone number is the default for this field. | N |
| PROBLEMCODE | UPPER(8) | VARCHAR2(8) | The problem being reported based on the defined Failure Class. This is usually the code in the second level of the failure reporting hierarchy. | N |
| CALENDAR | UPPER(8) | VARCHAR2(8) | Calendar to determine shift work is to be done on | Y |
| DOWNTIME | YORN | NUMBER | Does the Asset have to be down for this work order? | Y |
| REMDUR | DURATION | FLOAT | Indicates the number of remaining hours needed to complete the work. You can modify this value until the work order is closed. | N |
| CREWID | ALN(12) | VARCHAR2(12) | ID for a given crew within an craft | N |
| LASTCOPYLINKDATE | DATETIME | DATE | The date time doclinks was copied from other related objects like asset,location,jobplan,safetyplan etc. | N |
| WOEQ13 | DATETIME | DATE | Extra Field | N |
| WOEQ14 | AMOUNT | NUMBER(15,2) | Extra Field | N |
| WOJP1 | ALN(20) | VARCHAR2(20) | Extra Field | N |
| WOJP2 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOJP3 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOJP4 | AMOUNT | NUMBER(10,2) | Extra Field | N |
| WOJP5 | DATETIME | DATE | Extra Field | N |
| WOL1 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOL2 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOL3 | AMOUNT | NUMBER(10,2) | Extra Field | N |
| WOL4 | DATETIME | DATE | Extra Field | N |
| RESPONDBY | DATETIME | DATE | A Calculated field specifying when by a breakdown should be responded to. | N |
| ASSETLOCPRIORITY | INTEGER | NUMBER | Identifies the priority level copied from the asset or location record and used to schedule the work order. Values from 0-999 are valid, but we recommend limiting your range of values to 0-9, where 0 designates the lowest priority. We also recommend assigning priority values only to locations. Entering a value will update the CalcPriority field according to the calculation option in the PriCalc table. MAXIMO uses this value to update the Respond By date. | N |
| CALCPRIORITY | INTEGER | NUMBER | Calculated Work Priority | N |
| CHARGESTORE | YORN | NUMBER | Check box that Indicates whether charges on the work order should be added to the asset record. If the check box is selected, the charges will be added to the asset; if the check box is cleared, the charges will not be added to the asset. If the asset on the work order is a non-capitalized rotating asset not located in an operating location or a storeroom, Maximo selects the check box; otherwise this check box is cleared. | Y |
| FAILURECODE | UPPER(8) | VARCHAR2(8) | Failure class of the defined work asset. The failure class is the top level of the failure hierarchy. | N |
| WOLO1 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOLO2 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOLO3 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOLO4 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOLO5 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOLO6 | AMOUNT | NUMBER(10,2) | Extra Field | N |
| WOLO7 | DATETIME | DATE | Extra Field | N |
| WOLO8 | AMOUNT | NUMBER(15,2) | Extra Field | N |
| WOLO9 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOLO10 | INTEGER | NUMBER | Extra Field | N |
| GLACCOUNT | ALN(110) | VARCHAR2(110) | General ledger account code to which work order costs are charged. The GL account consists of up to four components: cost center, activity, resource, and element, each separated by a hyphen. If the work order was generated from a PM, Maximo copies the GL account from the PM. This field is read-only if the Charge to Store? check box is selected. | N |
| ESTSERVCOST | AMOUNT | NUMBER(10,2) | Total estimated service cost against this work order. | Y |
| ACTSERVCOST | AMOUNT | NUMBER(10,2) | Total actual service cost against this work order. | Y |
| DISABLED | YORN | NUMBER | Is this record active? | Y |
| ESTATAPPRLABHRS | DURATION | FLOAT | Estimate of the work order's labor hours at the time the work order was approved. | Y |
| ESTATAPPRLABCOST | AMOUNT | NUMBER(10,2) | Cost estimate of the work order's labor at the time the work order was approved. | Y |
| ESTATAPPRMATCOST | AMOUNT | NUMBER(10,2) | Cost estimate of the work order's material at the time the work order was approved. | Y |
| ESTATAPPRTOOLCOST | AMOUNT | NUMBER(10,2) | Cost estimate of the work order's tools at the time the work order was approved. | Y |
| ESTATAPPRSERVCOST | AMOUNT | NUMBER(10,2) | Cost estimate of the work order's services when the work order is approved. | Y |
| WOSEQUENCE | INTEGER | NUMBER | Indicates sequence in which to execute the work orders in a work order hierarchy. | N |
| HASFOLLOWUPWORK | YORN | NUMBER | Specifies whether the work order has follow up work. If the check box is selected, there is follow up work. If the check box is cleared (the default), there is no follow up work. | Y |
| WORTS1 | ALN(10) | VARCHAR2(10) | Crossover field from Route_Stop.RTS1 | N |
| WORTS2 | ALN(10) | VARCHAR2(10) | Crossover field from Route_Stop.RTS2 | N |
| WORTS3 | ALN(10) | VARCHAR2(10) | Crossover field from Route_Stop.RTS3 | N |
| WORTS4 | DATETIME | DATE | Crossover field from Route_Stop.RTS4 | N |
| WORTS5 | AMOUNT | NUMBER(15,2) | Crossover field from Route_Stop.RTS5 | N |
| SOURCESYSID | ALN(10) | VARCHAR2(10) | Source System ID | N |
| OWNERSYSID | ALN(10) | VARCHAR2(10) | Owner System ID | N |
| PMDUEDATE | DATETIME | DATE | From the original Next Due Date of the PM | N |
| PMEXTDATE | DATETIME | DATE | From the Extended Date of the PM | N |
| PMNEXTDUEDATE | DATETIME | DATE | From the next Next Due Date of the PM | N |
| WORKLOCATION | UPPER(25) | VARCHAR2(25) | Identifier of the Work Location that is to be responsible for the work. | N |
| SENDERSYSID | ALN(50) | VARCHAR2(50) | Column used by ERP-Integration (APIs) | N |
| FINCNTRLID | ALN(8) | VARCHAR2(8) | Financial Control Identifier | N |
| GENERATEDFORPO | UPPER(8) | VARCHAR2(8) | Work order for PO | N |
| GENFORPOLINEID | BIGINT | NUMBER | POLINEID number that required the work | N |
| ORGID | UPPER(8) | VARCHAR2(8) | Organization Identifier | Y |
| TASKID | INTEGER | NUMBER | Identifies the task. | N |
| INSPECTOR | UPPER(100) | VARCHAR2(100) | Identifies the inspector assigned to the work order | N |
| MEASUREMENTVALUE | AMOUNT | NUMBER(15,3) | Recorded Measurement | N |
| MEASUREDATE | DATETIME | DATE | Date/Time when measurement was taken or when observation was taken. This field becomes read-only when you enter a measurement and save the record. | N |
| WORKPACKMTLSTATUS | UPPER(20) | VARCHAR2(20) | The availability status of all materials on the current work order and related child records. The status is determined by the planned materials on a work order. The completion of the child records does not always determine the overall status of the materials in the work package. | N |
| POINTNUM | ALN(8) | VARCHAR2(8) | The point number on the asset from which the measurement is taken. | N |
| WOJO1 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOJO2 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOJO3 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOJO4 | AMOUNT | NUMBER(15,2) | Extra Field | N |
| WOJO5 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOJO6 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOJO7 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| WOJO8 | ALN(10) | VARCHAR2(10) | Extra Field | N |
| ISTASK | YORN | NUMBER | Specifies whether the work order is a task. If the check box is selected, the work order is a task. If the check box is cleared, the work order is not a task. | Y |
| WOCLASS | UPPER(16) | VARCHAR2(16) | Identifies the work order's class. | Y |
| ONBEHALFOF | ALN(82) | VARCHAR2(82) | Identifies for whom this work order has been created. For example, if someone did not have access to a computer, another user could create the work order on behalf of that person. | N |
| VENDOR | UPPER(12) | VARCHAR2(12) | Identifies the vendor responsible for the work. | N |
| ORIGRECORDCLASS | UPPER(16) | VARCHAR2(16) | Originating record's class, for example: change, release, incident, problem, or work order. | N |
| ORIGRECORDID | UPPER(25) | VARCHAR2(25) | Identifies the source record that was used to create this record. | N |
| JUSTIFYPRIORITY | UPPER(50) | VARCHAR2(50) | Describes why the work order received the priority it has been assigned. To enter or view additional information, click the Long Description button. | N |
| RISK | ALN(10) | VARCHAR2(10) | Defines the risk level of the work order. Risk may be a numeric value or a term such as High, Medium, Low, or some other value based upon your business definitions. | N |
| ENVIRONMENT | ALN(50) | VARCHAR2(50) | This field defines the environment in which the change is taking place. | N |
| SUPERVISOR | UPPER(100) | VARCHAR2(100) | Supervisor of the work. Maximo copies this information from the job plan, if there is one. You also can select a supervisor. | N |
| WOACCEPTSCHARGES | YORN | NUMBER | Check box specifies whether or not the work order accepts charges. If the check box is selected (the default), the work order accepts charges. If the check box is cleared, the work order does not accept charges, and you cannot enter charges on the work order. | Y |
| OWNER | UPPER(100) | VARCHAR2(100) | The person currently responsible for the work order. | N |
| PARENTCHGSSTATUS | YORN | NUMBER | Specifies whether the work order's status will change when its parent work order's status changes. If the check box is selected (the default), the work order's status will change when the parent work order's status changes. If the check box is cleared, the work order's status will not change when the parent work order's status changes. | Y |
| COMMODITYGROUP | UPPER(8) | VARCHAR2(8) | Defines the service group for the work order, for example: IT, production, facility, or fleet. | N |
| COMMODITY | UPPER(8) | VARCHAR2(8) | Defines the service for the work order, for example: customer support, painting, print, telecommunications, and welding. | N |
| WORKORDERID | BIGINT | NUMBER | Unique Identifier | Y |
| WHOMISCHANGEFOR | ALN(20) | VARCHAR2(20) | Identifies whom a change is for. | N |
| REASONFORCHANGE | ALN(20) | VARCHAR2(20) | Describes the reason for the change. To enter or view additional information, click the Long Description button. | N |
| VERIFICATION | ALN(20) | VARCHAR2(20) | Used to verify that the Change was successful or describe how to determine if the Change was successful | N |
| PERSONGROUP | UPPER(20) | VARCHAR2(20) | Identifies the person group responsible for the work. Person groups are set up in the Person Groups application. | N |
| LANGCODE |  | VARCHAR2(4) | Language Column | Y |
| INTERRUPTIBLE | YORN | NUMBER | Specifies whether the work order is allowed to be stopped once the work has begun and then restarted. If the check box is selected, the work can be stopped. If the check box is cleared, the work cannot be stopped and restarted. | Y |
| WOGROUP | UPPER(25) | VARCHAR2(25) | This column is used to enhance performance of the Work Orders app. | N |
| HASLD |  | NUMBER | Boolean flag to indicate if there is any long description for this record | Y |
| ROUTE | UPPER(8) | VARCHAR2(8) | add route field | N |
| ROUTESTOPID | BIGINT | NUMBER | add a new field | N |
| CINUM | UPPER(12) | VARCHAR2(12) | Configuration Item | N |
| JOBTASKID | BIGINT | NUMBER | Unique Identifier | N |
| WOISSWAP | YORN | NUMBER | Identifies if the Move is a Move or a Swap | Y |
| NEWCHILDCLASS | UPPER(16) | VARCHAR2(16) | Identifies the child work order class. | Y |
| SUSPENDFLOW | YORN | NUMBER | Flag used to suspend and resume flow control on a parent record. Rolls down the hierarchy to all its child records. | Y |
| FLOWCONTROLLED | YORN | NUMBER | Flag used to indicate a records participation in flow control. When applied to a parent, it rolls down the hierarchy to all its child records. | Y |
| FLOWACTIONASSIST | YORN | NUMBER | Suppresses the automatic firing of the action in flow control | Y |
| LAUNCHENTRYNAME | ALN(32) | VARCHAR2(32) | Launch Entry | N |
| TARGETDESC | ALN(50) | VARCHAR2(50) | Temporary target description for targeting Cis | N |
| FLOWACTION | ALN(30) | VARCHAR2(30) | The action to be performed (if any) when the workorder is started via process flow control | N |
| FIRSTAPPRSTATUS | ALN(16) | VARCHAR2(16) | Identifies the first status change which approved the work order. This value is used to determine if the work order record has ever been approved. Work orders that have been approved are not allowed to be deleted. | N |
| CALCORGID | ALN(8) | VARCHAR2(8) | Organization associated with the calculation calendar, and used with the Calendar and Shift fields to calculate the Target Contact, Target Response, and Target Resolution dates on a ticket or work order. Click the Detail Menu button to select an organization or go to the Organizations application to create one. | N |
| CALCCALENDAR | UPPER(8) | VARCHAR2(8) | Business days and times used to calculate the Target Contact, Target Response, and Target Resolution dates on a ticket or work order. Click the Detail Menu to select a calendar or go to the Calendars application to create one. | N |
| CALCSHIFT | UPPER(8) | VARCHAR2(8) | Along with the calculation calendar, sets the business hours that calculate the Target Contact, Target Response, and Target Resolution dates on a ticket or work order. Click the Select Value button to choose a shift. | N |
| REPFACSITEID | ALN(8) | VARCHAR2(8) | The site for the repair facility. | N |
| REPAIRFACILITY | UPPER(25) | VARCHAR2(25) | Specifies the repair facility location. A repair facility can take ownership of work orders from multiple sites in the same organization. User security can be configured to give permission to view work orders in multiple sites if the work orders are owned by a single repair facility. | N |
| GENFORPOREVISION | BIGINT | NUMBER | Revision number of the purchase order. Indicates how many times a purchase order has been revised. | N |
| STOREROOMMTLSTATUS | UPPER(20) | VARCHAR2(20) | The availability status of storeroom materials on the current work order. | N |
| DIRISSUEMTLSTATUS | UPPER(20) | VARCHAR2(20) | The availability status of direct issue materials on the current work order. | N |
| PCTASKCOMP | YORN | NUMBER | Specifies the percent complete as calculated by tasks completed vs. planned. | N |
| IGNORESRMAVAIL | YORN | NUMBER | Indicates whether to ignore the availability of items in the storeroom for the work order status approval. | Y |
| IGNOREDIAVAIL | YORN | NUMBER | Indicates whether to ignore the availability of direct issues for the work order status approval. | Y |
| ESTINTLABCOST | AMOUNT | NUMBER(10,2) | The estimated cost of internal labor for the task on the current work order. | N |
| ESTINTLABHRS | DURATION | FLOAT | The estimated hours of internal labor that are required for the task on the current work order. | N |
| ESTOUTLABHRS | DURATION | FLOAT | The estimated hours of external labor that are required for the task on the current work order. | N |
| ESTOUTLABCOST | AMOUNT | NUMBER(10,2) | The estimated hours of external labor that are required for the task on the current work order. | N |
| ACTINTLABCOST | AMOUNT | NUMBER(10,2) | The cost of internal labor for the task on the current work order. | N |
| ACTINTLABHRS | DURATION | FLOAT | The hours of internal labor that are required for the task on the current work order. | N |
| ACTOUTLABHRS | DURATION | FLOAT | The hours of external labor that are required for the task on the current work order. | N |
| ACTOUTLABCOST | AMOUNT | NUMBER(10,2) | The cost of external labor that are required for the task on the current work order. | N |
| ESTATAPPRINTLABCOST | AMOUNT | NUMBER(10,2) | The cost of internal labor for the task on the current work order at the time it was approved. | N |
| ESTATAPPRINTLABHRS | DURATION | FLOAT | The hours of internal labor for the task on the current work order at the time it was approved. | N |
| ESTATAPPROUTLABHRS | DURATION | FLOAT | The hours of internal labor for the task on the current work order at the time it was approved. | N |
| ESTATAPPROUTLABCOST | AMOUNT | NUMBER(10,2) | The cost of external labor for the task on the current work order at the time it was approved. | N |
| ASSIGNEDOWNERGROUP | UPPER(20) | VARCHAR2(20) | Assigned Owner Group of the workorder record. This group has overall responsibility for the solution. Use the Select Action menu to assign an owner group. You can enter a value either in this field or the Owner field. | N |
| AVAILSTATUSDATE | DATETIME | DATE | The date and time that the material status was last updated. | N |
| PCACTHRS | INTEGER | NUMBER | Specifies the percent complete as calculated by actual hours vs. planned hours. | N |
| NESTEDJPINPROCESS | YORN | NUMBER | Record will be locked while the jobplan containing nested jobplans is in the process of being applied to the record. | Y |
| INSPFORMNUM | ALN(12) | VARCHAR2(12) | The number of the inspection form. | N |
| PLUSCFREQUENCY | BIGINT | NUMBER | Number of time units (days, weeks, months, or years) to elapse between work orders you generate from this PM. The count begins at the value in the Last Target Start Date (if Use Target Start? is selected or Last Completion Date (if Use Target Start? is not the selected field. This value may differ differ from the Regulatory Frequency to facilitate scheduling. For example, a regulatory frequency of 31 days may have a frequency of 28 days. | N |
| PLUSCFREQUNIT | ALN(8) | VARCHAR2(8) | Defines the time units (days, weeks, months, or years) for the Frequency field on time-based PMs. | N |
| PLUSCISMOBILE | YORN | NUMBER | Workorder was added by mobile | Y |
| PLUSCLOOP | YORN | NUMBER | Select this check box to identify the work order as a loop calibration. May be selected when a value is entered in the Location field. | Y |
| PLUSCNEXTDATE | DATETIME | DATE | Date the next work order is scheduled to be generated from the PM. The Next Due Date field is NULL unless there is an integer value in the Frequency field. | N |
| PLUSCOVERDUEDATE | DATETIME | DATE | Displays the over due date for the calibration, if the work order is not completed. | N |
| PLUSCPHYLOC | ALN(250) | VARCHAR2(250) | Enter a detailed description of the location of the asset or specify the location information using a hierarchy that you created. Using a hierarchy provides consistent identification and groups assets efficiently. | N |
| UNITSOFWORK | ALN(16) | VARCHAR2(16) | Work Unit | N |
| DJPAPPLIED | UPPER(5) | VARCHAR2(5) | This flag can be set to YES, NO, ERROR | Y |
| INCTASKSINSCHED | YORN | NUMBER | Indicates that this work order's tasks will be included in the schedule view. | Y |
| CREWWORKGROUP | UPPER(20) | VARCHAR2(20) | Resource pool that will perform the work order. | N |
| REQASSTDWNTIME | YORN | NUMBER | Indicates if the asset requires downtime while work is performed. | Y |
| APPTREQUIRED | YORN | NUMBER | Indicates if an appointment is required for the work order. Tasks that are created for this work order inherit this value, but can be modified. | Y |
| AOS | YORN | NUMBER | Indicates that the work cannot be performed while the asset is operating. | Y |
| AMS | YORN | NUMBER | Indicates that the work must be performed during a dedicated maintenance window. | Y |
| LOS | YORN | NUMBER | Indicates that the work must be performed when the location is not in use, and available for maintenance. | Y |
| LMS | YORN | NUMBER | Indicates that the work must be performed during a dedicated maintenance window. | Y |
| REPAIRLOCFLAG | YORN | NUMBER | Indicates whether the repair location is required. | Y |
| INTSHIFT | UPPER(8) | VARCHAR2(8) | Specifies the shift on which the work will be interrupted in the Graphical Scheduling application. | N |
| PCPCHANGEBY | UPPER(100) | VARCHAR2(100) | Physical percent complete - Who last changed | N |
| PCPCHANGEDATE | DATETIME | DATE | Physical percent complete - When last changed | N |
| PCPHYS | INTEGER | NUMBER | Specifies the user-defined percent complete as entered manually. | N |
| SPLANREVIEWDATE | DATETIME | DATE | Does this Work Order safetplan is reviwed? | N |
| MILESTONE | YORN | NUMBER | Indicates if this Work Order is a milestone. | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di Sistema | Y |
| ANYWHEREREFID | BIGINT | NUMBER | Anywhere Reference ID | N |
| PLUSCJPREVNUM | INTEGER | NUMBER | Displays the revision number of the Job Plan the work order was generated with. | N |
| X4ANR | ALN(15) | VARCHAR2(15) | ANR | N |
| X4CAPEX | ALN(15) | VARCHAR2(15) | CAPEX | N |
| X4DATEMOD | YORN | NUMBER | Data pianificata modificata | Y |
| X4INFO1 | ALN(150) | VARCHAR2(150) | Informazioni OdL | N |
| X4BURKS | UPPER(10) | VARCHAR2(10) | Società | N |
| X4FISICOPLAN | FLOAT | FLOAT | Fisico pianificato | N |
| X4TITRIS | ALN(150) | VARCHAR2(150) | TITRIS | N |
| X4CLASSASSET | ALN(192) | VARCHAR2(192) | Classificazione Asset | N |
| X4MICROCONTROLLO | ALN(15) | VARCHAR2(15) | X4microcontrollo | N |
| X4IDERRORINDISP | ALN(20) | VARCHAR2(20) | Codice errore indisponibilità | N |
| X4SEDEINDISPL1 | ALN(20) | VARCHAR2(20) | Sede di indisponibilità di primo livello | N |
| X4POLICYID | INTEGER | NUMBER | ID della policy numerico | N |
| X3CAMPONOTE | ALN(150) | VARCHAR2(150) |  | N |
| X3DATAMODELLO | DATETIME | DATE |  | N |
| X3DATAPIANMOD | YORN | NUMBER |  | Y |
| X3NOTECHIUSURA | ALN(150) | VARCHAR2(150) |  | N |
| X3TEMPOPAUSA | DURATION | FLOAT |  | N |
| X3TRAINANTE | YORN | NUMBER |  | Y |
| X3UM | ALN(5) | VARCHAR2(5) |  | N |
| X4STATOGEOCALL | ALN(30) | VARCHAR2(30) | Stato Geocall | N |
| GEOMETRY | BLOB | BLOB |  | N |
| ID_COMPONENTE | ALN(10) | VARCHAR2(10) |  | N |
| TOTALWORKUNITS | DECIMAL | NUMBER(10,2) |  | N |
| X4CAPACITYRESIDUAQ | INTEGER | NUMBER |  | N |
| X4COMPMOBILE | YORN | NUMBER |  | Y |
| X4DSS | YORN | NUMBER |  | Y |
| X4ERRSAP | ALN(250) | VARCHAR2(250) |  | N |
| X4MATRICOLAESITO | UPPER(100) | VARCHAR2(100) |  | N |
| X4NOPERAZIONE | ALN(20) | VARCHAR2(20) |  | N |
| X4PREOWNERGROUP | UPPER(20) | VARCHAR2(20) |  | N |
| X4PRIMAELEGGIBILITA | DATETIME | DATE |  | N |
| X4STORNOGEOCALL | UPPER(10) | VARCHAR2(10) |  | N |
| X4TOPER | DURATION | FLOAT |  | N |
| X4TPREP | DURATION | FLOAT |  | N |
| X4SCENARIOTRONCHIMIG | INTEGER | NUMBER |  | N |

## WOACTIVITY: Data model Task dei singoli ordini di lavoro

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| WONUM | UPPER(25) | VARCHAR2(25) | ID OdL | Y |
| PARENT | UPPER(25) | VARCHAR2(25) | ID OdL padre | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | N |
| STATUS | UPPER(16) | VARCHAR2(16) | Stato OdL | N |
| X4MICROCONTROLLO | ALN(15) | VARCHAR2(15) | ID Micro-controllo | N |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | Asset su cui esercitare il micro-controllo | N |
| ACTFINISH | DATETIME | DATE |  | N |
| ACTINTLABCOST | AMOUNT | NUMBER(10,2) |  | N |
| ACTSTART | DATETIME | DATE |  | N |
| ACTINTLABHRS | DURATION | FLOAT(126) |  | N |
| ACTLABCOST | AMOUNT | NUMBER(10,2) |  | Y |
| ACTLABHRS | DURATION | FLOAT(126) |  | Y |
| ACTMATCOST | AMOUNT | NUMBER(10,2) |  | Y |
| ACTOUTLABCOST | AMOUNT | NUMBER(10,2) |  | N |
| ACTOUTLABHRS | DURATION | FLOAT(126) |  | N |
| ACTSERVCOST | AMOUNT | NUMBER(10,2) |  | Y |
| ASSETLOCPRIORITY | INTEGER | NUMBER |  | N |
| ACTTOOLCOST | AMOUNT | NUMBER(10,2) |  | Y |
| AMCREW | UPPER | VARCHAR2(8) |  | N |
| AMS | YORN | NUMBER |  | Y |
| ANYWHEREREFID | BIGINT | NUMBER(19) |  | N |
| AOS | YORN | NUMBER |  | Y |
| APPTREQUIRED | YORN | NUMBER |  | Y |
| CALCORGID | UPPER | VARCHAR2(8) |  | N |
| ASSIGNEDOWNERGROUP | UPPER | VARCHAR2(20) |  | N |
| AVAILSTATUSDATE | DATETIME | DATE |  | N |
| BACKOUTPLAN | ALN | VARCHAR2(50) |  | N |
| CALCCALENDAR | UPPER | VARCHAR2(8) |  | N |
| ESTATAPPRMATCOST | AMOUNT | NUMBER(10,2) |  | Y |
| ESTDUR | DURATION | FLOAT(126) |  | Y |
| CALCPRIORITY | INTEGER | NUMBER |  | N |
| CALCSHIFT | UPPER | VARCHAR2(8) |  | N |
| CALENDAR | UPPER | VARCHAR2(8) |  | N |
| CHANGEBY | UPPER | VARCHAR2(100) |  | N |
| CHANGEDATE | DATETIME | DATE |  | N |
| CHARGESTORE | YORN | NUMBER |  | Y |
| CINUM | UPPER | VARCHAR2(12) |  | N |
| CLASSSTRUCTUREID | UPPER | VARCHAR2(26) |  | N |
| COMMODITY | UPPER | VARCHAR2(8) |  | N |
| COMMODITYGROUP | UPPER | VARCHAR2(8) |  | N |
| CONTRACT | UPPER | VARCHAR2(8) |  | N |
| CREWID | ALN | VARCHAR2(12) |  | N |
| CREWWORKGROUP | UPPER | VARCHAR2(20) |  | N |
| DESCRIPTION | ALN | VARCHAR2(270) |  | N |
| DIRISSUEMTLSTATUS | ALN | VARCHAR2(20) |  | N |
| DISABLED | YORN | NUMBER |  | Y |
| DJPAPPLIED | UPPER | VARCHAR2(5) |  | Y |
| DJPERROR | ALN | VARCHAR2(400) |  | N |
| DOWNTIME | YORN | NUMBER |  | Y |
| ENVIRONMENT | ALN | VARCHAR2(50) |  | N |
| ESTATAPPRINTLABCOST | AMOUNT | NUMBER(10,2) |  | N |
| ESTATAPPRINTLABHRS | DURATION | FLOAT(126) |  | N |
| ESTATAPPRLABCOST | AMOUNT | NUMBER(10,2) |  | Y |
| ESTATAPPRLABHRS | DURATION | FLOAT(126) |  | Y |
| ESTATAPPROUTLABCOST | AMOUNT | NUMBER(10,2) |  | N |
| ESTATAPPROUTLABHRS | DURATION | FLOAT(126) |  | N |
| ESTATAPPRSERVCOST | AMOUNT | NUMBER(10,2) |  | Y |
| ESTATAPPRTOOLCOST | AMOUNT | NUMBER(10,2) |  | Y |
| ESTINTLABCOST | AMOUNT | NUMBER(10,2) |  | N |
| ESTINTLABHRS | DURATION | FLOAT(126) |  | N |
| ESTLABCOST | AMOUNT | NUMBER(16,2) |  | Y |
| ESTLABHRS | DURATION | FLOAT(126) |  | Y |
| ESTMATCOST | AMOUNT | NUMBER(10,2) |  | Y |
| ESTOUTLABCOST | AMOUNT | NUMBER(10,2) |  | N |
| ESTOUTLABHRS | DURATION | FLOAT(126) |  | N |
| ESTSERVCOST | AMOUNT | NUMBER(10,2) |  | Y |
| ESTTOOLCOST | AMOUNT | NUMBER(10,2) |  | Y |
| FINCNTRLID | UPPER | VARCHAR2(8) |  | N |
| EXTERNALREFID | ALN | VARCHAR2(10) |  | N |
| FAILDATE | DATETIME | DATE |  | N |
| FAILURECODE | UPPER | VARCHAR2(8) |  | N |
| GENERATEDFORPO | UPPER | VARCHAR2(8) |  | N |
| GENFORPOLINEID | BIGINT | NUMBER |  | N |
| GENFORPOREVISION | INTEGER | NUMBER |  | N |
| FIRSTAPPRSTATUS | UPPER | VARCHAR2(16) |  | N |
| FLOWACTION | UPPER | VARCHAR2(30) |  | N |
| FLOWACTIONASSIST | YORN | NUMBER |  | Y |
| FLOWCONTROLLED | YORN | NUMBER |  | Y |
| FNLCONSTRAINT | DATETIME | DATE |  | N |
| REMDUR | DURATION | FLOAT(126) |  | N |
| GEOMETRY | BLOB | BLOB |  | N |
| GLACCOUNT | GL | VARCHAR2(110) |  | N |
| HASCHILDREN | YORN | NUMBER |  | Y |
| HASFOLLOWUPWORK | YORN | NUMBER |  | Y |
| HASLD | YORN | NUMBER |  | Y |
| HISTORYFLAG | YORN | NUMBER |  | Y |
| ID_COMPONENTE | ALN | VARCHAR2(10) |  | N |
| IGNOREDIAVAIL | YORN | NUMBER |  | Y |
| IGNORESRMAVAIL | YORN | NUMBER |  | Y |
| LMS | YORN | NUMBER |  | Y |
| INCTASKSINSCHED | YORN | NUMBER |  | Y |
| OUTTOOLCOST | AMOUNT | NUMBER(10,2) |  | Y |
| INSPECTOR | UPPER | VARCHAR2(100) |  | N |
| INSPFORMNUM | UPPER | VARCHAR2(12) |  | N |
| INTERRUPTIBLE | YORN | NUMBER |  | Y |
| INTSHIFT | UPPER | VARCHAR2(8) |  | N |
| ISTASK | YORN | NUMBER |  | Y |
| JOBTASKID | BIGINT | NUMBER(19) |  | N |
| LEAD | UPPER | VARCHAR2(100) |  | N |
| JPNUM | ALN | VARCHAR2(20) |  | N |
| JUSTIFYPRIORITY | ALN | VARCHAR2(50) |  | N |
| LANGCODE | UPPER | VARCHAR2(4) |  | Y |
| LASTCOPYLINKDATE | DATETIME | DATE |  | N |
| LAUNCHENTRYNAME | UPPER | VARCHAR2(32) |  | N |
| LOCATION | UPPER | VARCHAR2(25) |  | N |
| LOS | YORN | NUMBER |  | Y |
| MEASUREDATE | DATETIME | DATE |  | N |
| MEASUREMENTVALUE | DECIMAL | NUMBER |  | N |
| MILESTONE | YORN | NUMBER |  | Y |
| NESTEDJPINPROCESS | YORN | NUMBER |  | Y |
| NEWCHILDCLASS | UPPER | VARCHAR2(16) |  | Y |
| OBSERVATION | ALN | VARCHAR2(8) |  | N |
| ONBEHALFOF | ALN | VARCHAR2(82) |  | N |
| PLUSCISMOBILE | YORN | NUMBER |  | Y |
| ORGID | UPPER | VARCHAR2(8) |  | Y |
| ORIGRECORDCLASS | UPPER | VARCHAR2(16) |  | N |
| ORIGRECORDID | UPPER | VARCHAR2(25) |  | N |
| OUTLABCOST | AMOUNT | NUMBER(16,2) |  | Y |
| OUTMATCOST | AMOUNT | NUMBER(16,2) |  | Y |
| OWNER | UPPER | VARCHAR2(100) |  | N |
| OWNERGROUP | UPPER | VARCHAR2(20) |  | N |
| OWNERSYSID | ALN | VARCHAR2(10) |  | N |
| PMDUEDATE | DATE | DATE |  | N |
| PARENTCHGSSTATUS | YORN | NUMBER |  | Y |
| PCACTHRS | INTEGER | NUMBER |  | N |
| PCPCHANGEBY | UPPER | VARCHAR2(100) |  | N |
| PCPCHANGEDATE | DATETIME | DATE |  | N |
| PCPHYS | INTEGER | NUMBER |  | N |
| PCTASKCOMP | INTEGER | NUMBER |  | N |
| PERSONGROUP | UPPER | VARCHAR2(20) |  | N |
| PHONE | ALN | VARCHAR2(20) |  | N |
| PROBLEMCODE | UPPER | VARCHAR2(8) |  | N |
| PLUSCFREQUENCY | INTEGER | NUMBER |  | N |
| PLUSCFREQUNIT | UPPER | VARCHAR2(8) |  | N |
| PLUSCJPREVNUM | INTEGER | NUMBER |  | N |
| PLUSCLOOP | YORN | NUMBER |  | Y |
| PLUSCNEXTDATE | DATETIME | DATE |  | N |
| PLUSCOVERDUEDATE | DATETIME | DATE |  | N |
| PLUSCPHYLOC | ALN | VARCHAR2(250) |  | N |
| PMEXTDATE | DATE | DATE |  | N |
| PMNEXTDUEDATE | DATE | DATE |  | N |
| PMNUM | UPPER | VARCHAR2(8) |  | N |
| POINTNUM | UPPER | VARCHAR2(8) |  | N |
| REASONFORCHANGE | UPPER | VARCHAR2(20) |  | N |
| REPAIRFACILITY | UPPER | VARCHAR2(25) |  | N |
| REPAIRLOCFLAG | YORN | NUMBER |  | Y |
| REQASSTDWNTIME | YORN | NUMBER |  | Y |
| REPFACSITEID | UPPER | VARCHAR2(8) |  | N |
| REPORTDATE | DATETIME | DATE |  | N |
| REPORTEDBY | ALN | VARCHAR2(82) |  | N |
| SENDERSYSID | ALN | VARCHAR2(50) |  | N |
| RESPONDBY | DATETIME | DATE |  | N |
| RISK | ALN | VARCHAR2(10) |  | N |
| ROUTE | UPPER | VARCHAR2(8) |  | N |
| ROUTESTOPID | BIGINT | NUMBER(19) |  | N |
| SCHEDFINISH | DATETIME | DATE |  | N |
| SCHEDSTART | DATETIME | DATE |  | N |
| SNECONSTRAINT | DATETIME | DATE |  | N |
| SOURCESYSID | ALN | VARCHAR2(10) |  | N |
| SPLANREVIEWDATE | DATETIME | DATE |  | N |
| STATUSDATE | DATETIME | DATE |  | Y |
| WOEQ14 | DECIMAL | NUMBER |  | N |
| WOEQ7 | DECIMAL | NUMBER |  | N |
| WOJP1 | ALN | VARCHAR2(20) |  | N |
| X4CLUSTERINDISP | ALN | VARCHAR2(50) |  | N |
| X4DATEMOD | YORN | NUMBER |  | Y |
| STOREROOMMTLSTATUS | ALN | VARCHAR2(20) |  | N |
| SUPERVISOR | UPPER | VARCHAR2(100) |  | N |
| SUSPENDFLOW | YORN | NUMBER |  | Y |
| TARGCOMPDATE | DATETIME | DATE |  | N |
| TARGETDESC | ALN | VARCHAR2(50) |  | N |
| TARGSTARTDATE | DATETIME | DATE |  | N |
| TASKID | INTEGER | NUMBER |  | N |
| TOTALWORKUNITS | DECIMAL | NUMBER |  | N |
| UNITSOFWORK | UPPER | VARCHAR2(16) |  | N |
| VENDOR | UPPER | VARCHAR2(12) |  | N |
| VERIFICATION | UPPER | VARCHAR2(20) |  | N |
| WOEQ8 | ALN | VARCHAR2(10) |  | N |
| WHOMISCHANGEFOR | UPPER | VARCHAR2(20) |  | N |
| WOACCEPTSCHARGES | YORN | NUMBER |  | Y |
| X3NOTECHIUSURA | ALN | VARCHAR2(150) |  | N |
| WOCLASS | UPPER | VARCHAR2(16) |  | Y |
| WOEQ1 | ALN | VARCHAR2(10) |  | N |
| WOEQ10 | ALN | VARCHAR2(10) |  | N |
| WOEQ9 | ALN | VARCHAR2(30) |  | N |
| WOEQ11 | ALN | VARCHAR2(10) |  | N |
| WOEQ12 | AMOUNT | NUMBER(16,2) |  | N |
| WOEQ13 | DATETIME | DATE |  | N |
| WOEQ2 | ALN | VARCHAR2(10) |  | N |
| WOEQ3 | ALN | VARCHAR2(10) |  | N |
| WOEQ4 | ALN | VARCHAR2(10) |  | N |
| WOEQ5 | AMOUNT | NUMBER(16,2) |  | N |
| WOEQ6 | DATETIME | DATE |  | N |
| WOGROUP | UPPER | VARCHAR2(25) |  | N |
| WOISSWAP | YORN | NUMBER |  | Y |
| WOJO1 | ALN | VARCHAR2(10) |  | N |
| WOJO2 | ALN | VARCHAR2(10) |  | N |
| WOJO3 | ALN | VARCHAR2(10) |  | N |
| WOSEQUENCE | INTEGER | NUMBER |  | N |
| WOJO4 | DECIMAL | NUMBER |  | N |
| WOJO5 | ALN | VARCHAR2(10) |  | N |
| WOJO6 | ALN | VARCHAR2(10) |  | N |
| WOJO7 | ALN | VARCHAR2(10) |  | N |
| WOJO8 | ALN | VARCHAR2(10) |  | N |
| WOJP2 | ALN | VARCHAR2(10) |  | N |
| WOJP3 | ALN | VARCHAR2(10) |  | N |
| WOJP4 | AMOUNT | NUMBER(16,2) |  | N |
| WOJP5 | DATETIME | DATE |  | N |
| WOL1 | ALN | VARCHAR2(10) |  | N |
| WOL2 | ALN | VARCHAR2(10) |  | N |
| WOL3 | AMOUNT | NUMBER(16,2) |  | N |
| WORTS3 | ALN | VARCHAR2(10) |  | N |
| WOL4 | DATETIME | DATE |  | N |
| WOLABLNK | UPPER | VARCHAR2(8) |  | N |
| WOLO1 | ALN | VARCHAR2(10) |  | N |
| WOLO10 | INTEGER | NUMBER |  | N |
| WOLO2 | ALN | VARCHAR2(10) |  | N |
| WOLO3 | ALN | VARCHAR2(10) |  | N |
| WOLO4 | ALN | VARCHAR2(10) |  | N |
| WOLO5 | ALN | VARCHAR2(10) |  | N |
| WOLO6 | AMOUNT | NUMBER(16,2) |  | N |
| WOLO7 | DATETIME | DATE |  | N |
| WOLO8 | DECIMAL | NUMBER |  | N |
| WOLO9 | ALN | VARCHAR2(10) |  | N |
| WOPRIORITY | INTEGER | NUMBER |  | N |
| WORKLOCATION | UPPER | VARCHAR2(25) |  | N |
| WORKORDERID | BIGINT | NUMBER(19) |  | Y |
| WORKPACKMTLSTATUS | ALN | VARCHAR2(20) |  | N |
| WORKTYPE | UPPER | VARCHAR2(5) |  | N |
| WORTS1 | ALN | VARCHAR2(10) |  | N |
| X4BURKS | ALN | VARCHAR2(10) |  | N |
| X4CAPACITYEROSAI | INTEGER | NUMBER |  | N |
| X4CAPACITYEROSAO | INTEGER | NUMBER |  | N |
| X4CAPACITYEROSAQ | INTEGER | NUMBER |  | N |
| WORTS2 | ALN | VARCHAR2(10) |  | N |
| WORTS4 | DATETIME | DATE |  | N |
| WORTS5 | DECIMAL | NUMBER |  | N |
| X3CAMPONOTE | ALN | VARCHAR2(150) |  | N |
| X3DATAMODELLO | DATE | DATE |  | N |
| X3DATAPIANMOD | YORN | NUMBER |  | Y |
| X3TEMPOPAUSA | DURATION | FLOAT |  | N |
| X3TPREP | DURATION | FLOAT |  | N |
| X3TRAINANTE | YORN | NUMBER |  | Y |
| X3TTRASF | DURATION | FLOAT |  | N |
| X3UM | ALN | VARCHAR2(5) |  | N |
| X4AGGREGATI | YORN | NUMBER |  | Y |
| X4ANAGRAFICAUPDATE | YORN | NUMBER |  | Y |
| X4ANR | ALN | VARCHAR2(15) |  | N |
| X4ASSIEME | ALN | VARCHAR2(35) |  | N |
| X4CAPACITYRESIDUAI | INTEGER | NUMBER |  | N |
| X4CAPACITYRESIDUAO | INTEGER | NUMBER |  | N |
| X4CAPACITYRESIDUAQ | INTEGER | NUMBER |  | N |
| X4CAPEX | ALN | VARCHAR2(15) |  | N |
| X4CLASSASSET | UPPER | VARCHAR2(192) |  | N |
| X4CODDELINDISP | ALN | VARCHAR2(25) |  | N |
| X4COIN | ALN | VARCHAR2(25) |  | N |
| X4COMPMOBILE | YORN | NUMBER |  | Y |
| X4DATAMODELLO | DATE | DATE |  | N |
| X4DATAULTIMAPIAN | DATETIME | DATE |  | N |
| X4DESCERRORINDISP | ALN | VARCHAR2(50) |  | N |
| X4DFINEINDISP | DATETIME | DATE |  | N |
| X4FINEESCLUSIONE | DATE | DATE |  | N |
| X4DINIZIOINDISP | DATETIME | DATE |  | N |
| X4DSS | YORN | NUMBER |  | Y |
| X4EFFORTI | DURATION | FLOAT |  | N |
| X4EFFORTO | DURATION | FLOAT |  | N |
| X4EFFORTQ | DURATION | FLOAT |  | N |
| X4ERRSAP | ALN | VARCHAR2(250) |  | N |
| X4ESITOINDISP | UPPER | VARCHAR2(50) |  | N |
| X4MOTIVOINDISP | ALN | VARCHAR2(15) |  | N |
| X4FISICO | FLOAT | FLOAT |  | N |
| X4FISICOPLAN | FLOAT | FLOAT |  | N |
| X4FLAGFISICO | YORN | NUMBER |  | Y |
| X4GEOCALLCLOSED | YORN | NUMBER |  | Y |
| X4IDERRORINDISP | ALN | VARCHAR2(20) |  | N |
| X4INFO1 | ALN | VARCHAR2(150) |  | N |
| X4INIZIOESCLUSIONE | DATE | DATE |  | N |
| X4MATRICOLAESITO | UPPER | VARCHAR2(100) |  | N |
| X4MODULICAR | YORN | NUMBER |  | Y |
| X4NTK | ALN | VARCHAR2(20) |  | N |
| X4NATURALAVORO | ALN | VARCHAR2(15) |  | N |
| X4NOPERAZIONE | ALN | VARCHAR2(20) |  | N |
| X4NORDINEINTERNO | ALN | VARCHAR2(20) |  | N |
| X4NOTE | ALN | VARCHAR2(150) |  | N |
| X4NOTEMIG | ALN | VARCHAR2(250) |  | N |
| X4ODM | ALN | VARCHAR2(20) |  | N |
| X4PERIMETROINT | ALN | VARCHAR2(25) |  | N |
| X4ORDINEPREVALENTE | ALN | VARCHAR2(25) |  | N |
| X4OUTSOURCING | YORN | NUMBER |  | Y |
| X4POLICYID | INTEGER | NUMBER |  | N |
| X4PREOWNERGROUP | UPPER | VARCHAR2(20) |  | N |
| X4SAPCONSOK | YORN | NUMBER |  | Y |
| X4WBSPIAN | ALN | VARCHAR2(20) |  | N |
| X4PIANMPE | UPPER | VARCHAR2(2) |  | N |
| X4POLICY | ALN | VARCHAR2(20) |  | N |
| X4PMI | YORN | NUMBER |  | Y |
| X4PRIMAELEGGIBILITA | DATETIME | DATE |  | N |
| X4SAPIDASSET | UPPER | VARCHAR2(30) |  | N |
| X4SEDEINDISP | ALN | VARCHAR2(20) |  | N |
| X4WBS | ALN | VARCHAR2(20) |  | N |
| X4SCENARIOTRONCHIMIG | INTEGER | NUMBER |  | N |
| X4SEDEINDISPL1 | ALN | VARCHAR2(20) |  | N |
| X4SERBATOIO | YORN | NUMBER |  | Y |
| X4SOTTOASSIEME | ALN | VARCHAR2(50) |  | N |
| X4SOTTOPROGRAMMA | ALN | VARCHAR2(50) |  | N |
| X4STORNOSAP | UPPER | VARCHAR2(10) |  | N |
| X4TECMIG | YORN | NUMBER |  | Y |
| X4TEMPOPAUSA | DURATION | FLOAT |  | N |
| X4TIPOATTIVITA | ALN | VARCHAR2(15) |  | N |
| X4STATOGEOCALL | ALN | VARCHAR2(30) |  | N |
| X4STATUSINDISP | ALN | VARCHAR2(20) |  | N |
| X4STORNOGEOCALL | UPPER | VARCHAR2(10) |  | N |
| X4TIPOODL | ALN | VARCHAR2(15) |  | N |
| X4TIPOOI | ALN | VARCHAR2(15) |  | N |
| X4TIPOORDINE | ALN | VARCHAR2(15) |  | N |
| X4TIPOSPESA | ALN | VARCHAR2(5) |  | N |
| X4TITRIS | ALN | VARCHAR2(150) |  | N |
| X4TOPER | DURATION | FLOAT |  | N |
| X4TPREP | DURATION | FLOAT |  | N |
| X4TRAINANTE | ALN | VARCHAR2(10) |  | N |
| X4TRONCONUM | UPPER | VARCHAR2(25) |  | N |
| X4TTRASF | DURATION | FLOAT |  | N |
| X4UM | ALN | VARCHAR2(5) |  | N |
| X4WBE | ALN | VARCHAR2(20) |  | N |
| X4WBEPIAN | ALN | VARCHAR2(20) |  | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## WPLABOR: Data model Gestione della pianificazione delle risorse interne

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| LABORCODE | UPPER(8) | VARCHAR2(8) | Codice | N |
| CRAFT | UPPER(8) | VARCHAR2(8) | Qualifica | N |
| LABORHRS | DURATION | FLOAT | Durata esecuzione | Y |
| QUANTITY | INT | NUMBER | Quantità | Y |
| X4TITRIS | UPPER(10) | VARCHAR2(10) | Titris della risorsa | N |
| X4DATAINIZIORISORSA | DATETIME | DATE | Data inizio risorsa | N |
| X4DATAFINERISORSA | DATETIME | DATE | Data fine risorsa | N |
| RATE | AMOUNT | NUMBER(10,2) | Tariffa oraria | Y |
| X4OREESEC | DURATION | FLOAT | Ore esecuzione | N |
| X4ORETRASF | DURATION | FLOAT | Ore trasferimento | N |
| X4OREPREESE | DURATION | FLOAT |  | N |
| WONUM | UPPER(25) | VARCHAR2(25) |  | Y |
| WPL1 | ALN(10) | VARCHAR2(10) |  | N |
| WPL2 | DATETIME | DATE |  | N |
| WPL3 | ALN(10) | VARCHAR2(10) |  | N |
| WPL4 | ALN(10) | VARCHAR2(10) |  | N |
| WPL5 | ALN(10) | VARCHAR2(10) |  | N |
| WPL6 | DECIMAL | NUMBER |  | N |
| WPL7 | ALN(10) | VARCHAR2(10) |  | N |
| WPL8 | ALN(10) | VARCHAR2(10) |  | N |
| WPL9 | ALN(10) | VARCHAR2(10) |  | N |
| WPLABORID | ALN(20) | VARCHAR2(20) | ID WPLABOR | Y |
| VENDOR | UPPER(12) | VARCHAR2(12) |  | N |
| RATEHASCHANGED | YORN | NUMBER |  | Y |
| ORGID | UPPER(8) | VARCHAR2(8) |  | Y |
| SITEID | UPPER(8) | VARCHAR2(8) |  | Y |
| WPLABORUID | BIGINT | NUMBER | UID WPLAABOUR | Y |
| SKILLLEVEL | UPPER(15) | VARCHAR2(15) |  | N |
| CONTRACTNUM | UPPER(8) | VARCHAR2(8) |  | N |
| JOBLABORID | BIGINT | NUMBER | ID JOBLABOR | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| AMCREW | UPPER(8) | VARCHAR2(8) |  | N |
| AMCREWTYPE | UPPER(8) | VARCHAR2(8) |  | N |
| CREWWORKGROUP | UPPER(20) | VARCHAR2(20) |  | N |
| APPTREQUIRED | YORN | NUMBER |  | Y |
| X4CHANGEDATE | DATETIME | DATE |  | N |
| X3DATAINIZIORISORSA | DATETIME | DATE |  | N |
| X3DATAFINERISORSA | DATETIME | DATE |  | N |
| X3OREPREESE | DURATION | FLOAT |  | N |
| X3OREESEC | DURATION | FLOAT |  | N |
| X3OREPOSTESEC | DURATION | FLOAT |  | N |
| X4OREPOSTESEC | DURATION | FLOAT |  | N |
| X3ORETRASF | DURATION | FLOAT |  | N |
| X4TECMIG | YORN | NUMBER |  | Y |
| X4KEYMIG | INTEGER | NUMBER |  | N |

## ASSIGNMENT: Data model Operazioni risorse

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| WONUM | UPPER(25) | VARCHAR2(25) | ID OdL | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | ID sede tecnica | N |
| ASSIGNMENTID | BIGINT | NUMBER | ID Assignment | Y |
| STATUS | UPPER(12) | VARCHAR2(12) | Stato dell’assignment | Y |
| LABORCODE | UPPER(8) | VARCHAR2(8) | Matricola | N |
| ORGID | UPPER(8) | VARCHAR2(8) | Organizzazione | Y |
| WPLABORID | ALN(20) | VARCHAR2(20) | ID WPLABOR | N |
| LABORHRS | DURATION | FLOAT | Labor | N |
| CRAFT | UPPER(8) | VARCHAR2(8) | Craft | N |
| SCHEDULEDATE | DATETIME | DATE | Scheduled Start | N |
| STARTDATE | DATETIME | DATE | Start Date | N |
| FINISHDATE | DATETIME | DATE | Scheduled Finish | N |
| SKILLLEVEL | UPPER(15) | VARCHAR2(15) | Skill Level | N |
| CONTRACTNUM | UPPER(8) | VARCHAR2(8) | Contract | N |
| VENDOR | UPPER(12) | VARCHAR2(12) | Vendor | N |
| WORKDATE | DATETIME | DATE | Work Date | N |
| PARENTASSIGNID | BIGINT | NUMBER | Parent Assignment ID | N |
| CREWWORKGROUP | UPPER(20) | VARCHAR2(20) | Crew Work Group | N |
| AMCREW | UPPER(8) | VARCHAR2(8) | Crew | N |
| AMCREWTYPE | UPPER(8) | VARCHAR2(8) | Crew Type | N |
| APPTREQUIRED | YORN | NUMBER | Appointment required | Y |
| APPOINTMENT | YORN | NUMBER | Schedule Lock | Y |
| WORKLOG | YORN | NUMBER | Worklog | Y |
| CHANGEBY | UPPER(100) | VARCHAR2(100) | Change by | N |
| CHANGEDATE | DATETIME | DATE | Changed Date | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo tecnico maximo | Y |
| RESOURCELOCK | YORN | NUMBER | Resource Lock | Y |
| X4TECMIG | YORN | NUMBER |  | Y |
| X4KEYMIG | INTEGER | NUMBER |  | N |
| X4PREPOSTO | YORN | NUMBER |  | Y |

## JOBPLAN: Data model Gestione dei PdL

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| JPNUM | UPPER(12) | VARCHAR2(12) | ID Attività | Y |
| DESCRIPTION | ALN(150) | VARCHAR2(150) | Descrizione attività | N |
| CLASSSTRUCTUREID | UPPER(26) | VARCHAR2(26) | Tipologia attività | N |
| PLUSCREVNUM | INT | NUMBER | Numero revisione | Y |
| STATUS | UPPER(16) | VARCHAR2(16) | Stato | Y |
| X4ASSIEME | ALN(35) | VARCHAR2(35) | Assieme | N |
| X4ATTALL | UPPER(15) | VARCHAR2(15) | ID Attività Standard | N |
| X4ATTCAR | YORN(1) | NUMBER | Attività caricabile manualmente | Y |
| X4DATAFINE | DATE | DATE | Data fine validità | N |
| X4DATAINIZIO | DATE | DATE | Data inizio validità | N |
| X4MACROATTIVITA | UPPER(16) | VARCHAR2(16) | ID Macro-Attività | N |
| X4PROGRAMMA | ALN(20) | VARCHAR2(20) | Programma | N |
| X4SOTTOASSIEME | ALN(50) | VARCHAR2(50) | Sottoassieme | N |
| X4TEMPOVAL | INT(12) | NUMBER | Max. tempo di validazione attività | N |
| X4TIPOLOGIAASSET | UPPER(192) | VARCHAR2(192) | Tipologia Asset | N |
| X4EFFORT | DURATION | FLOAT | Effort | N |
| X4UM | ALN(3) | VARCHAR2(3) | UM | N |
| X4INDISPONIBILITA | YORN(1) | NUMBER | Indisponibilità | N |
| OWNERGROUP | UPPER(10) | VARCHAR2(10) | ID del Responsabile attività definito in OdL | N |
| JPDURATION | DURATION | FLOAT | Durata pianificata | Y |
| X4TIPUDB | UPPER(10) | VARCHAR2(10) | Tipologia Unità di budget | N |
| X4OUTSOURCING | YORN | NUMBER | Modalità di gestione IN/OUT | Y |
| HASNESTEDJP | YORN | NUMBER | Nested Job Plans | Y |
| JOBPLANID | BIGINT | NUMBER | ID JOBPLAN | Y |
| LABORCODE | UPPER(100) | VARCHAR2(100) | Lead | N |
| DOWNTIME | YORN | NUMBER | Downtime | Y |
| SUPERVISOR | UPPER(100) | VARCHAR2(100) | Supervisor | N |
| CALENDAR | UPPER(8) | VARCHAR2(8) | Calendar | N |
| PRIORITY | INTEGER | NUMBER | WO Priority | N |
| CREWID | ALN(12) | VARCHAR2(12) | Crew | N |
| ORGID | UPPER(8) | VARCHAR2(8) | Organization | N |
| SITEID | UPPER(8) | VARCHAR2(8) | Site | N |
| OWNER | UPPER(100) | VARCHAR2(100) | Owner | N |
| PERSONGROUP | UPPER(20) | VARCHAR2(20) | Work group | N |
| LANGCODE |  | VARCHAR2(4) | Language code | Y |
| INTERRUPTIBLE | YORN | NUMBER | Interruptible | Y |
| HASLD |  | NUMBER | Has Long Description | Y |
| TEMPLATETYPE | ALN(16) | VARCHAR2(16) | Template type | Y |
| SUSPENDFLOW | YORN | NUMBER | Suspend Flow Control | Y |
| FLOWCONTROLLED | YORN | NUMBER | Flow Controlled | Y |
| FLOWACTION | UPPER(30) | VARCHAR2(30) | Flow Action | N |
| FLOWACTIONASSIST | YORN | NUMBER | Flow Action Assist | Y |
| LAUNCHENTRYNAME | UPPER(32) | VARCHAR2(32) | Launch entry name | N |
| ASSIGNEDOWNERGROUP | UPPER(10) | VARCHAR2(10) | Assigned Owner Group | N |
| INSPFORNUM |  | VARCHAR2(12) | Inspection Form | N |
| MODELID | ALN(45) | VARCHAR2(45) | Import ID | N |
| PLUSCCHANGEBY | UPPER(100) | VARCHAR2(100) | Changed By | Y |
| PLUSCCHANGEDATE | DATETIME | DATE | Changed Date | Y |
| PLUSCREVCOM | UPPER(100) | VARCHAR2(100) | Description | N |
| PLUSCSTATUSDATE | DATETIME | DATE | Last status change date | N |
| UNITSOFWORK | UPPER(16) | VARCHAR2(16) | Work Unit | N |
| UNITSOFWORKMULTIPLIER | AMOUNT | NUMBER(10,2) | Work Unit | N |
| CALCMETHOD | UPPER(15) | VARCHAR2(15) | Calculation | Y |
| CALCLEVEL | UPPER(8) | VARCHAR2(8) | Level | N |
| CALCAPPLYTO | UPPER(10) | VARCHAR2(10) | Type | Y |
| CALCEXPRESSION | UPPER(100) | VARCHAR2(100) | Expression | N |
| DYNAMIC | YORN | NUMBER | Is Dynamic | Y |
| APPLYNONDYNAMICCALC | YORN | NUMBER | Ignore linear measures | Y |
| INCTASKINSCHED | YORN | NUMBER | Include Tasks in Schedule | Y |
| STCONFFSET | DURATION | FLOAT | Start Constraint Offset | N |
| FNCONOFFSET | DURATION | FLOAT | Finish Constraint Offset | N |
| REQASSTDWNTIME | YORN | NUMBER | Require Asset Downtime | Y |
| APPTREQUIRED | YORN | NUMBER | Appointment Required | Y |
| AOS | YORN | NUMBER | Requires Asset Downtime | Y |
| AMS | YORN | NUMBER | Requires Asset Maintenance Window | Y |
| LOS | YORN | NUMBER | Requires Location Downtime | Y |
| LMS | YORN | NUMBER | Requires Location Maintenance Window | Y |
| REPAIRLOCFLAG | YORN | NUMBER | Repair Facility Required | Y |
| INTSHIFT | UPPER(8) | VARCHAR2(8) | Interruptible shift | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo tecnico Maximo | Y |
| X3CLASSECOMP | ALN(20) | VARCHAR2(20) | Classe Componente | N |
| X3TIPOCOMP | ALN(20) | VARCHAR2(20) | Tipo componente | N |
| X3NORMDIRIF | ALN(15) | VARCHAR2(15) | Normativa di riferimento | N |
| X4ATTPREVMETODO | YORN | NUMBER | Attività prevalente per metodo | Y |
| X4NOTE | ALN(150) | VARCHAR2(150) | Note | N |
| X4ELIVE | YORN | NUMBER | Elive | Y |
| X4KPI | AMOUNT | NUMBER(10,2) | X4KPI | N |
| X4LIVELLOATTIVITA | INTEGER | NUMBER | X4LIVELLOATTIVITA | N |
| X4WORKFLOW | INTEGER | NUMBER | ID WORKFLOW | N |
| X4ATRIDONEITA | ALN(15) | VARCHAR2(15) |  | N |
| X4MXL | YORN | NUMBER |  | Y |
| X4TOLLERANZA | INTEGER | NUMBER |  | N |
| AMCREW | UPPER(8) | VARCHAR2(8) |  | N |
| CREWWORKGROUP | UPPER(20) | VARCHAR2(20) |  | N |

## JOBTASK: Data model Step operativi necessari per completare il piano di lavoro

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| JPNUM | UPPER(12) | VARCHAR2(12) | ID PdL | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | Sede piano di lavoro | N |
| PLUSCJPREVNUM | INT | NUMBER | Revisione del piano | Y |
| DESCRIPTION | ALN(100) | VARCHAR2(100) | Descrizione del task | N |
| TASKDURATION | DURATION | FLOAT | Durata stimata per completare attività | Y |
| X3WORKFLOW | ALN(50) | VARCHAR2(50) |  | N |
| X4MICROCONTROLLO | ALN(15) | VARCHAR2(15) |  | N |
| NESTEDJOBPLANID | BIGINT | NUMBER |  | N |
| NESTEDJPNUM | ALN(20) | VARCHAR2(20) |  | N |
| CALCAPPLYTO | UPPER(10) | VARCHAR2(10) |  | Y |
| JOBTASKID | BIGINT | NUMBER |  | Y |
| PLUSCREVSTATUS | INTEGER | NUMBER |  | N |
| FLOWACTIONASSIST | YORN | NUMBER |  | Y |
| LAUNCHENTRYNAME | UPPER(32) | VARCHAR2(32) |  | N |
| JO6 | ALN(10) | VARCHAR2(10) |  | N |
| INTSHIFT | UPPER(8) | VARCHAR2(8) |  | N |
| CLASSSTRUCTUREID | UPPER(26) | VARCHAR2(26) |  | N |
| APPTREQUIRED | YORN | NUMBER |  | Y |
| INCTASKSINSCHED | YORN | NUMBER |  | Y |
| PREDECESSORTASKS | ALN(256) | VARCHAR2(256) |  | N |
| ASSIGNEDOWNERGROUP | UPPER(20) | VARCHAR2(20) |  | N |
| TASKSEQUENCE | INTEGER | NUMBER |  | N |
| JO7 | ALN(10) | VARCHAR2(10) |  | N |
| ORGID | UPPER(8) | VARCHAR2(8) |  | N |
| FLOWACTION | UPPER(30) | VARCHAR2(30) |  | N |
| CREWWORKGROUP | UPPER(20) | VARCHAR2(20) |  | N |
| JO8 | ALN(10) | VARCHAR2(10) |  | N |
| JPTASK | INTEGER | NUMBER |  | Y |
| CONDITIONNUM | UPPER(12) | VARCHAR2(12) |  | N |
| FLOWCONTROLLED | YORN | NUMBER |  | Y |
| OWNER | UPPER(100) | VARCHAR2(100) |  | N |
| PLUSCNESTEDJPREVNUM | INTEGER | NUMBER |  | N |
| JO5 | ALN(10) | VARCHAR2(10) |  | N |
| JO4 | DECIMAL | NUMBER |  | N |
| CALCEXPRESSION | ALN(100) | VARCHAR2(100) |  | N |
| CALCMETHOD | UPPER(15) | VARCHAR2(15) |  | Y |
| CALCLEVEL | UPPER(8) | VARCHAR2(8) |  | N |
| TASKDURATION | DURATION | FLOAT |  | Y |
| JO1 | ALN(10) | VARCHAR2(10) |  | N |
| JO2 | ALN(10) | VARCHAR2(10) |  | N |
| JO3 | ALN(10) | VARCHAR2(10) |  | N |
| SUSPENDFLOW | YORN | NUMBER |  | Y |
| JOBPLANID | BIGINT | NUMBER |  | Y |
| OWNERGROUP | UPPER(20) | VARCHAR2(20) |  | N |
| METERNAME | UPPER(10) | VARCHAR2(10) |  | N |
| INSPFORMNUM | UPPER(12) | VARCHAR2(12) |  | N |
| DESCRIPTION | ALN(100) | VARCHAR2(100) |  | N |
| INTERRUPTIBLE | YORN | NUMBER |  | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| LANGCODE |  | VARCHAR2(4) | Campo di sistema | Y |

## X4MICROCONTROLLI: Data model Catalogo micro-controlli

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ASSIEME | ALN(20) | VARCHAR2(20) | Assieme | N |
| CONTROLLO | ALN(3) | VARCHAR2(2,3) | Controllo | N |
| DESCRIPTION | ALN(270) | VARCHAR2(50) | Descrizione | N |
| FREQUENZA | INT | NUMBER | Frequenza | N |
| IDMICROCONTROLLO | ALN(15) | VARCHAR2(15) | ID Microcontrollo | N |
| SOTTOASSIEME | ALN(20) | VARCHAR2(20) | Sottoassieme | N |
| TIPOLOGIAASSET | UPPER(192) | VARCHAR2(192) | Tipologia Asset | N |
| TCOMPONENTE | ALN(20) | VARCHAR2(20) | Tipo Componente | N |
| TCOMANDO | ALN(20) | VARCHAR2(20) | Tipo Comando | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| STATUS | ALN(15) | VARCHAR2(15) | Stato | N |
| X4MICROCONTROLLIID | BIGINT | NUMBER | ID X4MICROCONTROLLI | Y |
| X4MXL | YORN | NUMBER |  | Y |

## X4WOINDISP : Data Model Delibere di indisponibilità ricevute da Agile

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DATAINIZIO | DATETIME |  | Data inizio |  |
| DATAFINE | DATETIME |  | Data fine |  |
| IDREQUEST | ALN(25) |  | ID richiesta indisponibilità |  |
| STATUS | ALN(15) |  | Stato richiesta |  |

## X3CATDIFFORMITA: Data Model Catalogo difformità(è presente anche X4CATDIFFOR con più informazioni)

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X3CATDIFFORMITAID | BIGINT | NUMBER | ID X3CATDIFFORMITA | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione difformità | N |
| TIPOLOGIA | ALN(20) | VARCHAR2(20) | Tipologia difformità | N |
| TIPOLOGIAASSET | UPPER(192) | VARCHAR2(192) |  | N |
| SOTTOASSIEME | ALN(20) | VARCHAR2(20) |  | N |
| IDMISURA | ALN(20) | VARCHAR2(20) |  | N |
| IDDIFFORMITA | ALN(20) | VARCHAR2(20) |  | N |
| ASSIEME | ALN(20) | VARCHAR2(20) |  | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| TEMPOVALID |  | NUMBER |  | N |

## X3CATASSOCIAZIONE: Data Model Associazione tra IDORIGINE(JPNUM o un X4MICROCONTROLLO) e le difformità rilevabili per quel record

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | N |
| X3CATASSOCIAZIONEID | BIGINT | NUMBER | ID X3CATASSOCIAZIONE | Y |
| IDDIFFORMITA | ALN(20) | VARCHAR2(20) | ID Difformità | N |
| IDINTERFERENZA | ALN(50) | VARCHAR2(50) | ID Interferenza | N |
| DESCRIPTION | ALN(150) | VARCHAR2(150) | Descrizione | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| IDORIGINE | ALN(20) | VARCHAR2(20) | ID Origine | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| TIPO | ALN(10) | VARCHAR2(10) | Tipologia di associazione | N |
| TIPOLOGIAASSET | UPPER(192) | VARCHAR2(192) | Tipologia asset | N |
| IDUNIVOCOTARGET | BIGINT | NUMBER | ID Target | N |
| TIPOORIGINE | ALN(15) | VARCHAR2(15) | Origine dell’associazione (C=Controllo) | N |

## X4ATTPREVALENTI: Data model che contiene per ogni JPNUM le attività di cui è prevalente (che devono essere chiuse al momento della creazione)

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ATTIVITAPREV | ALN(12) | VARCHAR2(15) | Attività Prevalente | N |
| JPNUM | ALN(12) | VARCHAR2(15) | PdL | N |
| DESCRIPTION | UPPER(250) | VARCHAR2(250) | Descrizione | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | Sede dell’asset | N |
| X4ATTPREVALENTEID | BIGINT | NUMBER | ID X4ATTPREVALENTEID | Y |

## X4METODI: Data Model Catalogo Metodi

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDMETODO | ALN(20) | VARCHAR2(20) | ID Metodo | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| DURATA | DURATION | FLOAT | Durata | N |
| TOTRIS | INT | NUMBER | Numero totale risorse | N |

## ADDRESS: Data Model Indirizzo

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ORGID | UPPER(8) | VARCHAR2(8) | Codice organizzazione | Y |
| ADDRESSCODE | ALN(30) | VARCHAR2(30) | Codice indirizzo | Y |
| ADDRESSID | BIGINT | NUMBER | ID ADDRESS | Y |
| DESCRIPTION | ALN(100) | VARCHAR2(100) | Descrizione | N |
| ADDRESS1 | ALN(50) | VARCHAR2(50) | Indirizzo1 | N |
| TAX1CODE | UPPER(8) | VARCHAR2(8) | Codice postale1 | N |
| CHANGEBY | UPPER(100) | VARCHAR2(100) | Autore modifica | N |
| CHANGEDATE | DATETIME | DATE | Data ultima modifica | Y |
| ADDRESS2 |  | VARCHAR2(50) |  | N |
| ADDRESS3 |  | VARCHAR2(50) |  | N |
| ADDRESS4 |  | VARCHAR2(50) |  | N |
| ADDRESS5 |  | VARCHAR2(50) |  | N |
| HASLD |  | NUMBER |  | Y |
| LANGCODE |  | VARCHAR2(4) |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| TAX2CODE |  | VARCHAR2(8) |  | N |
| TAX3CODE |  | VARCHAR2(8) |  | N |
| TAX4CODE |  | VARCHAR2(8) |  | N |
| TAX5CODE |  | VARCHAR2(8) |  | N |

## CLASSANCESTOR: Data Model Albero gerarchico classificazioni

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CLASSANCESTORID | BIGINT | NUMBER | ID CLASSANCESTOR | Y |
| CLASSSTRUCTUREID | UPPER(26) | VARCHAR2(26) | ID CLASSSTRUCTURE | N |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) | ID CLASSIFICATION | N |
| ANCESTOR | UPPER(26) | VARCHAR2(26) | Ancestor | N |
| ANCESTORCLASSID |  | VARCHAR2(192) |  | N |
| HIERARCHYLEVELS | INTEGER | NUMBER | Livelli gerarchia | N |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | N |
| SITEID | UPPER(8) | VARCHAR2(40) | ID Sito | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |

## CLASSSTRUCTURE: Data model Gestione classificazione asset

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) | Classificazione asset associato all’OdL e mostrato nella sezione “Perimetro di pianificazione” del MPE | Y |
| CLASSSTRUCTUREID | UPPER(26) | VARCHAR2(26) | ID Interno e univoco della classificazione | Y |
| DESCRIPTION | ALN(254) | VARCHAR2(254) | Descrizione | N |
| GENASSETDESC | YORN | NUMBER |  | Y |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | N |
| PARENT | UPPER(26) | VARCHAR2(26) |  | N |
| USECLASSINDESC | YORN | NUMBER |  | Y |
| TYPE | UPPER(10) | VARCHAR2(10) | Tipo | N |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | N |
| CLASSSTRUCTUREUID | BIGINT | NUMBER | Classstructure UID | Y |
| HASCHILDREN | YORN | NUMBER |  | Y |
| LANGCODE |  | VARCHAR2(4) | Language code | Y |
| HASLD |  | NUMBER | Campo di sistema | Y |
| SHOWINASSETTOPO | YORN | NUMBER |  | N |
| SHOW | YORN | NUMBER |  | Y |
| SORTORDER | INTEGER | NUMBER |  | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| X3GESTSTAT | YORN | NUMBER |  | Y |
| X3CLASSIDREL | ALN(30) | VARCHAR2(30) |  | N |
| X3MAXNUM | INTEGER | NUMBER |  | N |
| X4ELEMENTO | ALN(65) | VARCHAR2(65) |  | N |

## SERVICEADDRESS: Data Model Indirizi dei servizi

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ADDRESSCODE | ALN(25) | VARCHAR2(25) | Codice address | Y |
| SERVICEADDRESSID | BIGINT | NUMBER | ID SERVICEADDRESS | Y |
| CITY | UPPER(40) | VARCHAR2(40) | Città | N |
| COUNTRY | UPPER(3) | VARCHAR2(3) | Paese | N |
| DESCRIPTION | ALN(40) | VARCHAR2(40) | Descrizione | N |
| TIMEZONE | ALN(32) | VARCHAR2(32) | Fuso orario | N |
| LATITUDEY | AMOUNT | NUMBER(18,10) | Latitudine | N |
| LONGITUDEX | AMOUNT | NUMBER(18,10) | Longitudine | N |
| ADDRESSLINE2 |  | VARCHAR2(56) |  | N |
| ADDRESSLINE3 |  | VARCHAR2(56) |  | N |
| ANYWHEREREFID |  | NUMBER |  | N |
| COUNTY |  | VARCHAR2(3) |  | N |
| DIRECTIONS |  | VARCHAR2(500) |  | N |
| FORMATTEDADDRESS |  | VARCHAR2(150) |  | N |
| GEOCODE |  | VARCHAR2(11) |  | N |
| GEOMETRY |  | BLOB |  | N |
| HASLD |  | NUMBER |  | Y |
| ISWEATHERZONE |  | NUMBER |  | Y |
| LANGCODE |  | VARCHAR2(4) |  | Y |
| OBJECTNAME |  | VARCHAR2(30) |  | N |
| ORGID |  | VARCHAR2(8) |  | Y |
| PARENT |  | VARCHAR2(25) |  | N |
| POSTALCODE |  | VARCHAR2(10) |  | N |
| REFERENCEPOINT |  | VARCHAR2(50) |  | N |
| REGIONDISTRICT |  | VARCHAR2(3) |  | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| SITEID |  | VARCHAR2(8) |  | N |
| STADDRDIRPRFX |  | VARCHAR2(25) |  | N |
| STADDRDIRSFX |  | VARCHAR2(25) |  | N |
| STADDRNUMBER |  | VARCHAR2(9) |  | N |
| STADDRSTREET |  | VARCHAR2(25) |  | N |
| STADDRSTTYPE |  | VARCHAR2(25) |  | N |
| STADDRUNITNUM |  | VARCHAR2(6) |  | N |
| STATEPROVINCE |  | VARCHAR2(25) |  | N |
| STREETADDRESS |  | VARCHAR2(60) |  | N |
| WKID |  | NUMBER |  | N |

## ASSET: Data model Campi per la gestione dell’anagrafica di linee e stazioni

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | ID Asset | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | Sede dell’asset | Y |
| CLASSSTRUCTUREID | UPPER(26) | VARCHAR2(20) | ID della Classificazione dell’asset | N |
| STATUS | ALN(20) | VARCHAR2(20) | Stato dell’asset | N |
| PARENT | UPPER(25) | VARCHAR2(25) | ID padre dell’asset | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione dell’oggetto tecnico(sede tecnica o equipment) | N |
| X3INGRP | ALN(3) | VARCHAR2(3) | Unità impianti dell’asset | N |
| X3GEWRK | ALN(8) | VARCHAR2(8) | Centro di lavoro responsabile | N |
| X3SAPID | UPPER(30) | VARCHAR2(30) | SAP ID | N |
| INSTALLDATE | DATETIME | DATE | Data di installazione | N |
| X3SWERK | ALN(4) | VARCHAR2(4) | Dipartimento di trasmissione | N |
| X3ZZSOCP | ALN(4) | VARCHAR2(4) | Società proprietaria |  |
| X4LOTTOTECNICO | INTEGER | VARCHAR2(10) | Lotto tecnico omogeneo |  |
| X3BUKRS | ALN(4) | VARCHAR2(4) | Società | N |
| X3COSTRUTTORECOMPONENTE | ALN(30) | VARCHAR2(30) | Costruttore componente | N |
| X3FUNZIONEELEMENTO | ALN(100) | VARCHAR2(100) | Funzione elemento | N |
| X3RIFERIMENTOFASE | ALN(4) | VARCHAR2(4) | Riferimento fase | N |
| X3TIPOCOMPONENTE | ALN(20) | VARCHAR2(20) | Tipo componente | N |
| X3KOKRS | ALN(4) | VARCHAR2(4) | Controlling Area | N |
| X3KOSTL | ALN(10) | VARCHAR2(10) | Centro di costo | N |
| SERIALNUM | UPPER(64) | VARCHAR2(64) | Numero seriale | N |
| ASSETTAG | ALN(64) | VARCHAR2(64) | Tag Asset | N |
| LOCATION | UPPER(25) | VARCHAR2(25) | Locazione | N |
| VENDOR | UPPER(12) | VARCHAR2(12) | Venditore | N |
| FAILURECODE | UPPER(8) | VARCHAR2(8) | Codice fallimento | N |
| MANUFACTURER | UPPER(12) | VARCHAR2(12) | Produttore | N |
| PURCHASEPRICE | AMOUNT | NUMBER(10,2) | Prezzo acquisto | Y |
| REPLACECOST | AMOUNT | NUMBER(10,2) | Prezzo sostituzione | Y |
| TOTALCOST | AMOUNT | NUMBER(10,2) | Costo totale | Y |
| YTDCOST | AMOUNT | NUMBER(10,2) | Costo YTD | Y |
| BUDGETCOST | AMOUNT | NUMBER(10,2) | Costo budget | Y |
| CALNUM | ALN(8) | VARCHAR2(8) | Num. calcolo | N |
| ISRUNNING | YORN | NUMBER | Flag is running | Y |
| ITEMNUM | UPPER(30) | VARCHAR2(30) | Numero item | N |
| UNCHARGEDCOST | AMOUNT | NUMBER(10,2) | Costi uncharged | Y |
| TOTUNCHARGEDCOST | AMOUNT | NUMBER(10,2) | Costi uncharged totali | Y |
| TOTDOWNTIME | DURATION | FLOAT | Downtime totale | Y |
| STATUSDATE | DATETIME | DATE | Data stato | N |
| PRIORITY | YORN | NUMBER | Priorità | N |
| INVCOST | AMOUNT | NUMBER(10,2) | Costo inv. | N |
| CHILDREN | YORN | NUMBER | Figlio? | Y |
| DISABLED | YORN | NUMBER | Disabilitato? | Y |
| SOURCESYSID | ALN(10) | VARCHAR2(10) | ID SOURCESYS | N |
| OWNERSYSID | ALN(10) | VARCHAR2(10) | ID OWNERSYS | N |
| EXTERNALREFID | ALN(10) | VARCHAR2(10) | ID EXTERNALREF | N |
| WARRANTYEXPDATE | DATETIME | DATE |  | N |
| CHANGEDATE | DATETIME | DATE | Data ultima modifica | Y |
| CHANGEBY | UPPER(100) | VARCHAR2(100) | Autore ultima modifica | Y |
| EQ1 | ALN(10) | VARCHAR2(10) |  | N |
| EQ2 | ALN(10) | VARCHAR2(10) |  | N |
| EQ3 | ALN(10) | VARCHAR2(10) |  | N |
| EQ4 | ALN(10) | VARCHAR2(10) |  | N |
| EQ5 | AMOUNT | NUMBER(10,2) |  | N |
| EQ6 | DATETIME | DATE |  | N |
| EQ7 | AMOUNT | NUMBER(15,2) |  | N |
| EQ8 | ALN(10) | VARCHAR2(10) |  | N |
| EQ9 | ALN(30) | VARCHAR2(30) |  | N |
| EQ10 | ALN(10) | VARCHAR2(10) |  | N |
| EQ11 | ALN(10) | VARCHAR2(10) |  | N |
| EQ12 | AMOUNT | NUMBER(10,2) |  | N |
| EQ23 | DATETIME | DATE |  | N |
| EQ24 | AMOUNT | NUMBER(15,2) |  | N |
| GLACCOUNT | ALN(110) | VARCHAR2(110) |  | N |
| ROTSUSPACCT | ALN(110) | VARCHAR2(110) |  | N |
| BINNUM | ALN(8) | VARCHAR2(8) |  | N |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | Y |
| AUTOWOGEN | YORN | NUMBER |  | Y |
| ITEMSETID | ALN(8) | VARCHAR2(8) | ID ITEMSET | N |
| CONDITIONCODE | ALN(30) | VARCHAR2(30) |  | N |
| GROUPNAME | UPPER(10) | VARCHAR2(10) | Nome gruppo | N |
| ASSETTYPE | ALN(50) | VARCHAR2(50) | Tipo asset | N |
| USAGE | UPPER(15) | VARCHAR2(15) |  | N |
| MAINTHIERCHY | YORN | NUMBER |  | Y |
| ASSETID | BIGINT | NUMBER | ID Asset | Y |
| MOVED | YORN | NUMBER |  | Y |
| ASSETUID | BIGINT | NUMBER | UID Asset | Y |
| LANGCODE |  | VARCHAR2(4) | Language code | Y |
| TOOLRATE | AMOUNT | NUMBER(10,2) |  | N |
| ITEMTYPE | UPPER(15) | VARCHAR2(15) | Tipo item | N |
| ANCESTOR | UPPER(25) | VARCHAR2(25) |  | N |
| SENDERSYSID | ALN(50) | VARCHAR2(50) | ID Sendersys | N |
| SHIFTNUM | UPPER(8) | VARCHAR2(8) |  | N |
| TOOLCONTROLACCOUNT | UPPER(110) | VARCHAR2(110) |  | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| DIRECTION | UPPER(30) | VARCHAR2(30) |  | N |
| STARTMEASURE | AMOUNT | NUMBER(10,2) | Misura iniziale | N |
| ENDMEASURE | AMOUNT | NUMBER(10,2) | Misura finale | N |
| ISLINEAR | YORN | NUMBER |  | Y |
| ENDDESCRIPTION | ALN(100) | VARCHAR2(100) | Descrizione finale | N |
| STARTDESCRIPTION | ALN(100) | VARCHAR2(100) | Descrizione iniziale | N |
| LRM | UPPER(25) | VARCHAR2(25) |  | N |
| DEFAULTREPFACSITEID | UPPER(8) | VARCHAR2(8) | ID DEFAULTREPFACSITEID | N |
| DEFAULTREPFAC | UPPER(8) | VARCHAR2(25) |  | N |
| RETURNEDTOVENDOR | YORN | NUMBER |  | Y |
| DEPRECIATIONPENDING | YORN | NUMBER |  | N |
| ASSETHEALTH | INTEGER | NUMBER |  | N |
| EXPECTEDLIFE | INTEGER | NUMBER |  | N |
| INDUSTRYHEALTHSCORE | FLOAT | FLOAT |  | N |
| INDUSTRYHEALTHGRADE | ALN(20) | VARCHAR2(20) |  | N |
| ESTENDOFLIFE | DATETIME | DATE |  | N |
| EXPECTEDLIFEDATE | DATETIME | DATE |  | N |
| DEVICEID | UPPER(50) | VARCHAR2(50) | ID Device | N |
| DEVICETYPE | UPPER(50) | VARCHAR2(50) | Tipo Device | N |
| WELL_MAINTAINED | YORN | NUMBER |  | N |
| PMNEXTFAILDT | DATETIME | DATE |  | N |
| TLOAMHASH | UPPER(192) | VARCHAR2(192) |  | N |
| TLOAMPARTITION | YORN | NUMBER |  | Y |
| MODELID | ALN(45) | VARCHAR2(45) | ID Model | N |
| PRODUCTID | ALN(12) | VARCHAR2(12) | ID Prodotto | N |
| PLUSCASSETDEPT | UPPER(80) | VARCHAR2(80) |  | N |
| PLUSCCLASS | UPPER(12) | VARCHAR2(12) |  | N |
| PLUSCDUEDATE | DATETIME | DATE |  | N |
| PLUSCISCONDESC | ALN(250) | VARCHAR2(250) |  | N |
| PLUSCISCONTAM | YORN | NUMBER |  | Y |
| PLUSCISINHOUSECAL | YORN | NUMBER |  | Y |
| PLUSCISMTE | YORN | NUMBER |  | Y |
| PLUSCISMTECLASS | UPPER(80) | VARCHAR2(80) |  | N |
| PLUSCLOOPNUM | UPPER(15) | VARCHAR2(15) |  | N |
| PLUSCMODELNUM | UPPER(8) | VARCHAR2(8) |  | N |
| PLUSCOPRGEEU | UPPER(15) | VARCHAR2(15) |  | N |
| PLUSCOPRGEFROM | UPPER(15) | VARCHAR2(15) |  | N |
| PLUSCOPRGETO | UPPER(15) | VARCHAR2(15) |  | N |
| PLUSCPHYLOC | UPPER(250) | VARCHAR2(250) |  | N |
| PLUSCPMEXTDATE | YORN | NUMBER |  | Y |
| PLUSCSOLUTION | YORN | NUMBER |  | Y |
| PLUSCSUMDIR | UPPER(12) | VARCHAR2(12) |  | N |
| PLUSCSUMEU | UPPER(15) | VARCHAR2(15) |  | N |
| PLUSCSUMREAD | UPPER(15) | VARCHAR2(15) |  | N |
| PLUSCSUMSPAN | UPPER(15) | VARCHAR2(15) |  | N |
| PLUSCSUMURV | UPPER(15) | VARCHAR2(15) |  | N |
| PLUSCVENDOR | UPPER(12) | VARCHAR2(12) |  | N |
| ISCALIBRATION | YORN | NUMBER |  | Y |
| TEMPLATEID | ALN(12) | VARCHAR2(12) | ID Template | N |
| PLUSCLPLOC | UPPER(25) | VARCHAR2(25) |  | N |
| SADDRESSCODE | UPPER(25) | VARCHAR2(25) |  | N |
| X3MSGRP | UPPER(25) | VARCHAR2(8) |  | N |
| X3ROOTASSETNUM | UPPER(25) | VARCHAR2(25) |  | N |
| X3EQFNR | UPPER(30) | VARCHAR2(30) |  | N |
| X3DADOPPIATERNA | YORN | NUMBER |  | Y |
| X3SAPIDETL | UPPER(30) | VARCHAR2(30) |  | N |
| X3SAPIDEQ | UPPER(30) | VARCAR2(30) |  | N |
| METHODNAME | UPPER(50) | VARCHAR2(50) | Nome metodo | N |
| LASTAHSCOREDATE | DATETIME | DATE |  | N |
| AHCAWONUM | UPPER(25) | VARCHAR2(25) |  | N |
| AHCAWOSTATUS | UPPER(16) | VARCHAR2(16) |  | N |
| AHCAPMNUM | UPPER(8) | VARCHAR2(8) |  | N |
| AHCAJPNUM | ALN(20) | VARCHAR2(20) |  | N |
| AHCAINSPFORMNUM | UPPER(12) | VARCHAR2(12) |  | N |
| AHCAREVISION | INTEGER | NUMBER |  | N |
| AHCARESULTNUM | UPPER(12) | VARCHAR2(12) |  | N |
| AHCAINSPSTATUS | UPPER(20) | VARCHAR2(20) |  | N |
| AHCALASTCOMPDATE | DATETIME | DATE |  | N |
| AHCAPMNEXTDUEDATE | DATETIME | DATE |  | N |
| AHCAWORKTYPE | UPPER(5) | VARCHAR2(5) |  | N |
| ADJUSTEDLIFEDATE | DATETIME | DATE |  | N |
| LOADED |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| GEOMETRY |  | BLOB |  | N |
| X3TEST |  | VARCHAR2(50) |  | N |
| X3SKIPSAPCON |  | NUMBER |  | Y |

## X4CATMOS: Data model Catalogo attività standard

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDATTSTANDARD | ALN(15) | VARCHAR2(15) | ID attività standard | N |
| SPESAPREVALENTE | ALN(15) | VARCHAR2(15) | Tipo spesa prevalente | N |
| COINPREVALENTE | ALN(15) | VARCHAR2(15) | Coin prevalente | N |
| X4CATMOSID | BIGINT | NUMBER | ID univoco/chiave della tabella X4CATMOS | Y |
| DESCRIPTION | ALN(70) | VARCHAR2(70) | Descrizione | N |
| OPEX | ALN(15) | VARCHAR2(15) | Opx | N |
| ANR | ALN(15) | VARCHAR2(15) | Anr | N |
| CAPEX | ALN(15) | VARCHAR2(15) | Capex | N |
| UDB | UPPER(10) | VARCHAR2(10) | Unità di budget | N |
| HASLD |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| ANR2 |  | VARCHAR2(15) |  | N |

## X4CATMOSCOIN: Data model Coin multivalore

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4CATMOSID | BIGINT | NUMBER | ID univoco/chiave della tabella X4CATMOS | N |
| TIPOSPESA | ALN(15) | VARCHAR2(15) | Tipo spesa | N |
| COIN | ALN(15) | VARCHAR2(15) | Codice Coin | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| X4CATMOSCOINID | BIGINT | NUMBER | ID X4CATMOSCOIN | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| HASLD |  | NUMBER |  | Y |

## X4COIN: Data model Coin

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDCOIN | ALN(15) | VARCHAR2(15) | Codice Coin | N |
| TIPOATTIVITA | ALN(15) | VARCHAR2(15) | Tipo Attività PM | N |
| TIPOORDINE | ALN(15) | VARCHAR2(15) | Tipo Ordine | N |
| TIPOSPESA | ALN(15) | VARCHAR2(15) | Tipo spesa | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| X4COINID | BIGINT | NUMBER | ID X4COIN | Y |
| HASLD |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| UDB |  | VARCHAR2(15) |  | N |

## X4WBE: Data model WBE

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDWBE | ALN(20) | VARCHAR2(20) | ID CBE | N |
| IDWBS | ALN(20) | VARCHAR2(20) | ID CBS di appartenenza | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione CBE | N |
| PIANIFICAZIONE | YORN(1) | NUMBER | Elemento di tipo pianificazione | Y |
| STATOSISTEMASAP | UPPER(100) | VARCHAR2(100) | Stato del sistema elemento in SAP | N |
| STATOUTENTESAP | UPPER(100) | VARCHAR2(100) | Stato utente elemento in SAP | N |
| CDCRESP | UPPER(15) | VARCHAR2(15) | Codice del centro di costo dell’elemento cbs responsabile dell’attività richiesta | N |
| CDCRICH | UPPER(15) | VARCHAR2(15) | Codice del centro di costo dell’elemento cbs richiedente dell’attività richiesta | N |
| SOCRICH | UPPER(15) | VARCHAR2(15) | Società dell’elemento cbs richiedente dell’attività richiesta | N |
| CREATEDATE | DATETIME | DATE | Data di creazione della cbs | N |
| CHANGEDATE | DATETIME | DATE | Data dell’ultima modifica della cbs | N |
| TIPOPROG | ALN(15) | VARCHAR2(15) | Tipologia di progetto a cui appartiene la cbs | N |
| PROFITCENTER | UPPER(15) | VARCHAR2(15) | Profit Center cbs | N |
| SEDETECNICA | UPPER(30) | VARCHAR2(30) | Sede tecnica a cui è collegata la cbs | N |
| MOTINVESTIMENTO | ALN(50) | VARCHAR2(50) | Indica un motivo di investimento | N |
| CODINCENTIVAZIONE | ALN(50) | VARCHAR2(50) | Cluster | N |
| CODINTERVENTO | ALN(50) | VARCHAR2(50) | Codice intervento | N |
| TIPOSPESA | ALN(15) | VARCHAR2(5) | Tipo spesa(popolato per WBE di pianificazione) | N |
| COIN | ALN(15) | VARCHAR2(15) | Codice COIN | N |
| X4WBEID | BIGINT | NUMBER | ID X4WBE | Y |
| STATOSISTEMA | UPPER(500) | VARCHAR2(500) | Stato del sistema | N |
| HASLD |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |

## X4NTK: Data model Network

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4NTKID | ALN(20) | NUMBER | ID Network | Y |
| IDNTK | ALN(20) | VARCHAR2(20) | ID Network | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione NTK | N |
| WBE | ALN(20) | VARCHAR2(50) | ID CBE | N |
| STATOSISTEMASAP | UPPER(80) | VARCHAR2(80) | Stato del sistema elemento in SAP | N |
| STATOUTENTESAP | UPPER(80) | VARCHAR2(80) | Stato utente elemento in SAP | N |
| PROGETTO | ALN(20) | VARCHAR2(20) | Progetto a cui è legata NTW | N |
| SOCIETA | UPPER(15) | VARCHAR2(15) | Società | N |
| PROFITCENTER | UPPER(15) | VARCHAR2(15) | Profit Center | N |
| TIPO | UPPER(10) | VARCHAR2(10) | Tipo NTW | N |
| CREATEDATE | DATETIME | DATE | Data creazione | N |
| CHANGEDDATE | DATETIME | DATE | Data ultima modifica | N |
| CHANGEDBY | UPPER(20) | VARCHAR2(20) | Autore creazione/modifica | N |
| NOPERAZIONE | UPPER(20) | VARCHAR2(20) | N. operazione del network | N |
| STATOSISTEMA | UPPER(500) | VARCHAR2(500) | Stato del sistema | N |
| CDCRESP | ALN(29) | VARCHAR2(29) | Codice responsabile | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| HASLD |  | NUMBER |  | Y |

## X4OPNTK: Data model Operazioni NTK

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDNTK | ALN(20) | VARCHAR2(20) | ID Network | N |
| IDOPERAZIONE | ALN(5) | VARCHAR2(5) | ID Operazione | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione Operazione | N |
| STATO | UPPER(8) | VARCHAR2(80) | Stato Operazione | N |
| CDLRESP | UPPER(15) | VARCHAR2(15) | CdL responsabile dell’operazione | N |
| CREATEDATE | DATETIME | DATE | Data creazione | N |
| CHANGEDDATE | DATETIME | DATE | Data ultima modifica | N |
| CHANGEDBY | UPPER(20) | VARCHAR2(20) | Autore creazione/modifica | N |
| X4OPNTKID | BIGINT | NUMBER | ID OPNTK | Y |
| WBE | ALN(50) | VARCHAR2(50) | WBE | N |
| STATOSISTEMA | ALN(500) | VARCHAR2(500) | Stato del sistema | N |
| X4NTKID |  | NUMBER |  | N |
| CDCRESP |  | VARCHAR2(20) |  | N |
| HASLD |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |

## X4ODM: Data model Ordini di manutenzione

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDODM | ALN(20) | VARCHAR2(20) | ID OdM | N |
| X4ODMID | BIGINT | NUMBER | ID univoco OdM | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione ODM | N |
| TIPOATTIVITA | ALN(20) | VARCHAR2(20) | Tipo attività | N |
| TIPOORDINE | ALN(20) | VARCHAR2(20) | Tipo ordine | N |
| STATOSISTEMA | UPPER(8) | VARCHAR2(8) | Stato del sistema elemento in SAP | N |
| STATOUTENTE | UPPER(8) | VARCHAR2(8) | Stato utente elemento in SAP | N |
| SEDETECNICA | UPPER(30) | VARCHAR2(30) | Sede Tecnica | N |
| CDCRESP | UPPER(15) | VARCHAR2(15) | Centro di costo responsabile | N |
| CDCCOSTI | UPPER(15) | VARCHAR2(15) | Centro di costo per scarico costi | N |
| PROFITCENTER | UPPER(15) | VARCHAR2(15) | Profit Center | N |
| CREATEDBY | UPPER(20) | VARCHAR2(20) | Autore creazione/modifica | N |
| CREATEDDATE | DATETIME | DATE | Data creazione ODM | N |
| CHANGEDDATE | DATETIME | DATE | Data ultima modifica ODM | N |
| CDLRESP | UPPER(15) | VARCHAR2(15) | Centro di lavoro responsabile dell’attività | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| WBE | ALN(40) | VARCHAR2(40) | WBE | N |
| KONTY | ALN(10) | VARHCHAR2(10) | KONTY | N |
| AUFNR | ALN(12) | VARCHAR2(12) | AUFNR | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |

## X4ODMOP: Data model operazioni ODM

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDODM | ALN(20) | VARCHAR2(20) | ID ODM | N |
| NOPERAZIONE | UPPER(20) | VARCHAR2(20) | Numero operazione ODM | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| X4ODMOPID | BIGINT | NUMBER | ID X4ODMOP | Y |
| X4ODMID | BIGINT | NUMBER | ID X4ODM | N |
| HASLD |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |

## X4ORDINT: Data model Ordini interni

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDOI | ALN(20) | VARCHAR2(20) | ID Ordine Interno | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| STATOSISTEMA | UPPER(8) | VARCHAR2(8) | Stato del sistema elemento in SAP | N |
| STATOUTENTE | UPPER(8) | VARCHAR2(8) | Stato utente elemento in SAP | N |
| TIPOOI | UPPER(5) | VARCHAR2(5) | Tipo OI | N |
| CATORD | UPPER(5) | VARCHAR2(5) | Categoria Ordine | N |
| CREATEDDATE | DATETIME | DATE | Data creazione | N |
| CHANGEDDATE | DATETIME | DATE | Data modifica | N |
| SOCIETA | UPPER(15) | VARCHAR2(15) | Società | N |
| CDCRESP | UPPER(15) | VARCHAR2(15) | Codice del centro di costo responsabile | N |
| CODCLIENTE | UPPER(30) | VARCHAR2(30) | Codice cliente | N |
| CDCCOSTI | UPPER(15) | VARCHAR2(15) | Centro di costo per scarico costi | N |
| PROFITCENTER | UPPER(15) | VARCHAR2(15) | Profit Center | N |
| CLASSEOGGETTO | UPPER(15) | VARCHAR2(15) | Classe Oggetto | N |
| WBE | ALN(20) | VARCHAR2(20) | WBE | N |
| IDODM | ALN(20) | VARCHAR2(20) | ID OdM | N |
| CLASSEOGGETTO | UPPER(15) | VARCHAR2(15) | Classe oggetto | N |
| SEDETECNICA |  | VARCHAR2(30) |  | N |
| X4ORDINTID |  | NUMBER |  | Y |
| HASLD |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |

## LABTRANS: Data Model Consuntivi delle risorse

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| SITEID | UPPER(8) | VARCHAR2(8) | Identificativo del sito | Y |
| REFWO | UPPER(25) | VARCHAR2(25) | OdL di riferimento | N |
| GENAPPRSERVRECEIPT | YORN | NUMBER | Inviato a SAP? | Y |
| LABORCODE | UPPER(8) | VARCHAR2(8) | Codice del lavoratore associato alla transazione | Y |
| STARTDATE | DATE | DATE | Data di inizio | Y |
| REGULARHRS | DURATION | FLOAT | Ore di Lavoro pianificate per l’intervento | Y |
| X4CHECKSAP | UPPER(10) | VARCHAR2(10) | Esito SAP | N |
| X4ERRSAP | ALN(200) | VARCHAR2(200) | Errore SAP | N |
| X4ESITOSAPDESC | ALN(2000) | VARCHAR2(2000) | Descrizione Errore SAP | N |
| STARTTIME | TIME | DATE | Ora di inizio | N |
| FINISHTIME | TIME | DATE | Ora di fine | N |
| X4ORETRASF | DURATION | FLOAT | Ore trasferimento | N |
| X4OREUFF | DURATION | FLOAT | Ore in ufficio | N |
| X4ORETOT | DURATION | FLOAT | Ore totali | N |
| LINECOST | AMOUNT | NUMBER(10,2) | Costo risorsa | Y |
| LABTRANSID | BIGINT | NUMBER | ID LabTrans | Y |
| STARTDATETIME | DATETIME | DATE | Data inizio effettiva(formato YYMMDD) | N |
| FINISHDATETIME | DATETIME | DATE | Data fine effettiva(formato YYMMDD) | N |
| AMCREW | UPPER(8) | VARCHAR2(8) | Codice della squadra di manodopera associata all’attività | N |
| AMCREWTYPE | UPPER(8) | VARCHAR2(8) | Tipi di squadra alla manodopera | N |
| ANYWHEREREFID | BIGINT | NUMBER | Riferimento all’applicazione mobile Maximo Anywhere | N |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | Numero identificativo del bene(asset) su cui è stata eseguita l’attività | N |
| CONTRACTNUM | UPPER(8) | VARCHAR2(8) | Numero del contratto associato alla transazione di manodopera | N |
| CRAFT | UPPER(8) | VARCHAR2(8) | Qualifica del lavoratore associato alla transazione | Y |
| CREWWORKGROUP | UPPER(10) | VARCHAR2(10) | Gruppo di lavoro alla squadra | N |
| ENTERBY | UPPER(100) | VARCHAR2(100) | Utente che ha inserito la transazione | Y |
| ENTERDATE | DATETIME | DATE | Data di inserimento della transazione | Y |
| ENTEREDASTASK | YORN | NUMBER | Indicazione se la transazione è stata inserita come attività specifica | Y |
| EXCHANGERATE2 | AMOUNT | NUMBER(14,7) | Tasso di cambio utilizzato per il calcolo dei costi in valuta estera | N |
| EXTERNALREFID | UPPER(10) | VARCHAR2(10) | Riferimento esterno per integrazioni con sistemi esterni | N |
| FINANCIALPERIOD | ALN(6) | VARCHAR2(6) | Periodo finanziario di competenza per la transazione | N |
| FINCNTRLID | UPPER(8) | VARCHAR2(8) | ID Controllo finanziario associato | N |
| GLCREDITACCT |  | VARCHAR2(110) | Conto di accredito del libro contabile associato alla transazione | N |
| GLDEBITACCT |  | VARCHAR2(110) | Conto di addebito del libro contabile associato alla transazione | N |
| INVOICELINENUM | INTEGER | NUMBER | Numero di riga della fattura associata alla transazione | N |
| INVOICENUM | ALN(8) | VARCHAR2(8) | Numero della fattura associata | N |
| LAVORO_EFFETTIVO_PRE | DURATION | FLOAT | Tempo di lavoro effettivo precedente all’aggiornamento | N |
| LINECOST2 | AMOUNT | NUMBER(10,2) | Secondo costo di linea, per transazioni in valuta estera | N |
| LOCATION | UPPER(25) | VARCHAR2(25) | Località associata all’attività | N |
| MEMO | ALN(100) | VARCHAR2(100) | Note | N |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione responsabile della transazione | Y |
| OUTSIDE | YORN | NUMBER | Indicazione se la transazione è stata eseguita da risorse esterne | Y |
| OWNERSYSID | UPPER(10) | VARCHAR2(10) | ID Sistema proprietario | N |
| VENDOR | UPPER(12) | VARCHAR2(12) | Fornitore associato alla transazione di manodopera | N |
| PAYMENTTRANSDATE | DATETIME | DATE | Data di transazione del pagamento | N |
| PAYRATE | AMOUNT | NUMBER(10,2) | Tariffa di pagamento oraria per il lavoratore | Y |
| POLINENUM | INTEGER | NUMBER | Numero di linea dell’ordine di acquisto associato | N |
| PONUM | UPPER(8) | VARCHAR2(8) | Numero dell’ordine di acquisto associato | N |
| POREVISIONNUM | INTEGER | NUMBER | Numero di revisione dell’ordine di acquisto | N |
| POSITION | UPPER(20) | VARCHAR2(20) | Posizione lavorativa del lavoratore associato | N |
| PREMIUMPAYCODE | UPPER(8) | VARCHAR2(8) | Codice per la tariffa maggiorata | N |
| PREMIUMPAYHOURS | DURATION | FLOAT | Ore di lavoro con la tariffa maggiorata | N |
| PREMIUMPAYRATE | AMOUNT | NUMBER(10,2) | Tariffa maggiorata applicata | N |
| PREMIUMPAYRATETYPE | UPPER(10) | VARCHAR2(10) | Tipo di tariffa maggiorata(ad es. straordinari, notturni) | N |
| REFWO | UPPER(25) | VARCHAR2(25) | Riferimento all’ordine di lavoro | N |
| REGULARHRS | DURATION | FLOAT | Ore di lavoro regolari senza maggiorazioni | Y |
| REVISIONNUM | INTEGER | NUMBER | Numero di revisione della transazione di manodopera | N |
| ROLLUP | YORN | NUMBER | Indicazione se la transazione è inclusa nel calcolo dei costi complessivi | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| SENDERSYSID | ALN(50) | VARCHAR2(50) | ID del sistema che ha inviato la transazione | N |
| SERVRECTRANSID | BIGINT | NUMBER | ID della transazione di ricezione servizi | N |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | Y |
| SKILLLEVEL | UPPER(15) | VARCHAR2(15) | Livello di competenza richiesto per l’attività | N |
| SOURCESYSID | UPPER(10) | VARCHAR2(10) | ID del sistema sorgente della transazione | N |
| TICKETCLASS | UPPER(16) | VARCHAR2(16) | Classe del ticket associato alla transazione (ad es. problema, richiesta) | N |
| TICKETID | UPPER(10) | VARCHAR2(10) | ID Ticket associato | N |
| TIMERSTATUS | UPPER(16) | VARCHAR2(16) | Stato del timer associato alla transazione di manodopera | N |
| TRANSDATE | DATETIME | DATE | Data della transazione | Y |
| TRANSTYPE | UPPER(16) | VARCHAR2(16) | Tipo di transazione(ad es. normale,straordinaria) | Y |
| FINISHDATE | DATETIME | DATE | Data fine | N |
| X3TPAUSA | DURATION | FLOAT | Tempo pausa | N |
| X3TTRASFERIMENTO | DURATION | FLOAT | Tempo trasferimento | N |
| X4STORNO | UPPER(10) | VARCHAR2(10) | X4Storno | N |
| X4IDCONSGEO |  | VARCHAR2(50) | ID Cons Geocall | N |
| X4CHANGEDATE | DATETIME | DATE | Change date | N |
| APPROVER | UPPER(100) | VARCHAR2(100) |  | N |
| X4KEYMIG |  | NUMBER |  | N |
| X4TECMIG |  | NUMBER |  | Y |

## PERSONGROUP: Data model Gestione gruppi di persone

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4UDB | YORN | NUMBER | Flag per specificare se persoungroup è una UDB | Y |
| X4TIPUDB | UPPER(10) | VARCHAR2(10) | Tipologie Unità di budget | N |
| X3PARENTPERSONGROUP | UPPER(20) | VARCHAR2(20) | Gruppo di persone padre | N |
| PERSONGROUPID | BIGINT | NUMBER | ID PERSONGROUP | Y |
| PERSONGROUP | UPPER(20) | VARCHAR2(20) | Gruppo utenti | Y |
| DESCRIPTION | ALN(100) | VARCHAR2(100) | Descrizione | N |
| LANGCODE |  | VARCHAR2(4) | Language code | Y |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ISCREWWORKGROUP | YORN | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| X4TRASC | ALN(50) | VARCHAR2(50) |  | N |
| RESPOSTATUTORY |  | VARCHAR2(50) |  | N |
| X4RESPOTAGETIK |  | VARCHAR2(20) |  | N |
| X4TITRISORSA |  | VARCHAR2(20) |  | N |

## PERSONGROUPTEAM: Data model Relazione tra risorse e gruppi di persone Maximo(UdB)

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| GROUPDEFAULT | YORN | NUMBER |  | Y |
| ORGDEFAULT | YORN | NUMBER |  | Y |
| PERSONGROUP | UPPER(20) | VARCHAR2(20) |  | N |
| PERSONGROUPTEAMID | BIGINT | NUMBER |  | Y |
| RESPPARTY | UPPER(100) | VARCHAR2(100) |  | Y |
| RESPPARTYGROUP | UPPER(100) | VARCHAR2(100) |  | Y |
| RESPPARTYGROUPSEQ | SMALLINT | NUMBER |  | Y |
| RESPPARTYSEQ | SMALLINT | NUMBER |  | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| SITEDEFAULT | YORN | NUMBER |  | Y |
| USEFORORG | UPPER(8) | VARCHAR2(8) |  | N |
| USEFORSITE | UPPER(8) | VARCHAR2(8) |  | N |
| X4TIPO |  | VARCHAR2(3) |  | N |

## X4CAPACITY: Data model Gestione capacity UdB

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4CAPACITYID | BIGINT | NUMBER | ID univoco/chiave della tabella X4CAPACITY | Y |
| UDB | UPPER(20) | VARCHAR2(20) | UdB | N |
| ANNO | INTEGER | NUMBER | Anno | N |
| SCENARIO | ALN(25) | VARCHAR2(25) | Scenario | N |
| STATUS | ALN(20) | VARCHAR2(20) | Stato | N |
| CHANGEDATE | DATETIME | DATE | Data modifica | N |
| CHANGEDBY | ALN(100) | VARCHAR2(100) | Modificato da | N |
| DESCRIPTION |  | VARCHAR2(50) |  | N |
| HASLD |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |

## X4CAPACITYDETAILS: Data model Capacity per singola competenza

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4CAPACITYID | BIGINT | NUMBER | ID capacity | N |
| X4CAPACITYDETAILSID | BIGINT | NUMBER | ID univoco/chiave della tabella X4CAPACITYDETAILS | Y |
| MESE | ALN(15) | VARCHAR2(15) | Mese di riferimento | N |
| ORE | FLOAT | FLOAT(126) | Quantità Ore Capacity | N |
| CRAFT | UPPER(8) | VARCHAR2(8) | Competenza(Operaio,Impiegato,Quadro) | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione del campo | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| ORGID | UPPER(8) | VARCHAR2(8) |  | N |

## X4CAPACITYPM: Data model Competenza UdB calcolate per ogni mese

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4CAPACITYID | BIGINT | NUMBER | ID capacity | N |
| CAPACITYHTEO | FLOAT | FLOAT | Capacity teorica | N |
| HPROPRIETEO | FLOAT | FLOAT | Ore proprie teoriche | N |
| CEDUTOTEO | FLOAT | FLOAT | Ore cedute teoriche | N |
| RICEVUTOTEO | FLOAT | FLOAT | Ore ricevute teoriche | N |
| FLAVOROTEO | FLOAT | FLOAT | Ore forza Lavoro teorica | N |
| HPROPRIEPLAN | FLOAT | FLOAT | Ore proprie pianificate puntuali | N |
| CEDUTOPLAN | FLOAT | FLOAT | Ore cedute pianificate | N |
| SATURAZIONEPLAN | FLOAT | FLOAT | Percentuale saturazione pianificata | N |
| RICEVUTOPLAN | FLOAT | FLOAT | Ore ricevute pianificate | N |
| CONTOBASEPLAN | FLOAT | FLOAT | Ore conto base pianificato | N |
| FLAVOROPLAN | FLOAT | FLOAT | Ore forza Lavoro pianificata | N |
| DELTACEDUTO | FLOAT | FLOAT | Ore cedute(da pianificare) | N |
| DELTARICEVUTO | FLOAT | FLOAT | Ore ricevute(da pianificare) | N |
| MESE | ALN(15) | VARCHAR2(15) | Mese | N |
| CRAFT | UPPER(8) | VARCHAR2(8) | Competenza(Operaio,Impiegato,Quadro) | N |
| SERBATOIO | FLOAT | FLOAT | Ore proprie pianificate serbatoio | N |
| HASLD |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| DESCRIPTION |  | VARCHAR2(50) |  | N |
| X4CAPACITYPMID |  | NUMBER |  | Y |

## X4CAPACITYEXC: Data model Scambio Ore Capacity

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4CAPACITYID | BIGINT | NUMBER | ID capacity | N |
| CRAFT | UPPER(8) | VARCHAR2(8) | Competenza(Operaio,Impiegato,Quadro) | N |
| QTYINV | INT | NUMBER | Quantità proposta | N |
| QTYACC | INT | NUMBER | Quantità accettata | N |
| MESE | ALN(15) | VARCHAR2(15) | Mese | N |
| DESTINATARIO | ALN(10) | VARCHAR2(10) | Destinatario | N |
| STATUS | ALN(15) | VARCHAR2(15) | Stato | N |
| DESCRIPTION |  | VARCHAR2(50) |  | N |
| X4CAPACITYEXCID |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| HASLD |  | NUMBER |  | Y |

## ITEM: Data Model Catalogo risorse esterne-ricavi

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ITEMNUM | UPPER(30) | VARCHAR2(30) | Resource ID | Y |
| DESCRIPTION | ALN(40) | VARCHAR2(40) | Descrizione | N |
| COMMODITYGROUP | UPPER(8) | VARCHAR2(8) | Gruppo merce | N |
| X4VDC | UPPER(20) | VARCHAR2(20) | VdC | N |
| X4TRISORSA | ALN(10) | VARCHAR2(20) | Tipo Risorsa | N |
| ITEMSETID | UPPER(8) | VARCHAR2(8) | ID item set | Y |
| ITEMID | BIGINT | NUMBER | ID ITEM | Y |
| ITEMTYPE | UPPER(15) | VARCHAR2(15) | Tipo Item | Y |
| CLASSSTRUCTUREID | UPPER(26) | VARCHAR2(26) | ID CLASSSTRUCTURE | N |
| STATUS | UPPER(16) | VARCHAR2(16) | Stato | Y |
| ROTATING | YORN | NUMBER |  | Y |
| LOTTYPE | UPPER(16) | VARCHAR2(16) |  | Y |
| CAPITALIZED | YORN | NUMBER |  | Y |
| MSDSNUM | ALN(10) | VARCHAR2(10) |  | N |
| OUTSIDE | YORN | NUMBER |  | Y |
| IN19 | ALN(10) | VARCHAR2(10) |  | N |
| IN20 | ALN(10) | VARCHAR2(10) |  | N |
| IN21 | ALN(10) | VARCHAR2(10) |  | N |
| IN22 | DATETIME | DATE |  | N |
| IN23 | DECIMAL | NUMBER(15,2) |  | N |
| SPAREPARTAUTOADD | YORN | NUMBER |  | Y |
| INSPECTIONREQUIRED | YORN | NUMBER |  | Y |
| SOURCESYSID | UPPER(10) | VARCHAR2(10) | ID SOURCESYS | N |
| OWNERSYSID | UPPER(10) | VARCHAR2(10) | ID OWNERSYS | N |
| EXTERNALREFID | UPPER(10) | VARCHAR2(10) | ID EXTERNALREF | N |
| IN24 | ALN(10) | VARCHAR2(10) |  | N |
| IN25 | ALN(10) | VARCHAR2(10) |  | N |
| IN26 | ALN(10) | VARCHAR2(10) |  | N |
| IN27 | ALN(10) | VARCHAR2(10) |  | N |
| SENDERSYSID | UPPER(50) | VARCHAR2(50) | ID SENDERSYS | N |
| ORDERUNIT | UPPER(16) | VARCHAR2(16) |  | N |
| ISSUEUNIT | UPPER(16) | VARCHAR2(16) |  | N |
| CONDITIONENABLED | YORN | NUMBER |  | Y |
| GROUPNAME | UPPER(10) | VARCHAR2(10) | Nome gruppo merce | N |
| METERNAME | UPPER(10) | VARCHAR2(10) |  | N |
| COMMODITY | UPPER(8) | VARCHAR2(8) |  | N |
| PRORATE | YORN | NUMBER |  | Y |
| ISKIT | YORN | NUMBER |  | Y |
| LANGCODE |  | VARCHAR2(4) | Language code | Y |
| ATTACHONISSUE | YORN | NUMBER |  | Y |
| HASLD |  | NUMBER | Campo di sistema | Y |
| MAXISSUE | AMOUNT | NUMBER(15,2) |  | N |
| STATUSDATE | DATETIME | DATE |  | Y |
| HARDRESISSUE | YORN | NUMBER |  | Y |
| TAXEXEMPT | YORN | NUMBER |  | Y |
| RECEIPTTOLERANCE | AMOUNT | NUMBER(15,2) |  | N |
| PLUSCISINHOUSECAL | YORN | NUMBER |  | Y |
| PLUSCISMTE | YORN | NUMBER |  | Y |
| PLUSCISMTECLASS | UPPER(80) | VARCHAR2(80) |  | N |
| PLUSCSOLUTION | YORN | NUMBER |  | Y |
| ISCREW | YORN | NUMBER |  | Y |
| X3DEFAULTCHILD | YORN | NUMBER |  | Y |
| X3PARENTITEM | UPPER(30) | VARCHAR2(30) |  | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## WPSERVICE: Data Model Pianificazioni servizi(risorse esterne e ricavi)

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ITEMNUM | UPPER(30) | VARCHAR2(30) | Codice risorsa esterna/ricavo | N |
| LINECOST | AMOUNT | NUMBER(10,2) | Costo linea | N |
| X4DATAINIZIORISORSA | DATETIME | DATE | Data inizio | N |
| X4DATAFINERISORSA | DATETIME | DATE | Data fine | N |
| ITEMSETID | UPPER(8) | VARCHAR2(8) | ID item set | N |
| WPM1 | ALN(10) | VARCHAR2(10) | WPM1 | N |
| WPM2 | ALN(10) | VARCHAR2(10) | WPM2 | N |
| WPM3 | AMOUNT | NUMBER(15,2) | WPM3 | N |
| WPM4 | ALN(10) | VARCHAR2(10) | WPM4 | N |
| WPM5 | AMOUNT | NUMBER(10,2) | WPM5 | N |
| WPM6 | ALN(10) | VARCHAR2(10) | WPM6 | N |
| AMCREW | UPPER(8) | VARCHAR2(8) | Crew | N |
| AMCREWTYPE | UPPER(8) | VARCHAR2(8) | Crew type | N |
| CATALOGCODE | ALN(30) | VARCHAR2(30) | Catalog # | N |
| CONDITIONCODE | UPPER(30) | VARCHAR2(30) | Condition Code | N |
| DESCRIPTION | ALN(40) | VARCHAR2(40) | Text description | N |
| DIRECTREQ | YORN | NUMBER | Direct Issue | Y |
| HASLD |  | NUMBER | Has Long Description | Y |
| HOURS | DURATION | FLOAT | Tool Hours | Y |
| ISSUETO | UPPER(8) | VARCHAR2(8) | Issue To | N |
| ITEMQTY | DECIMAL | NUMBER(15,2) | Quantity | N |
| JOBITEMID | BIGINT | NUMBER | Job Item ID | N |
| LANGCODE |  | VARCHAR2(4) | Language code | Y |
| LINETYPE | UPPER(15) | VARCHAR2(15) | Line Type | Y |
| LOCATION | UPPER(25) | VARCHAR2(25) | Storeroom | N |
| MANUFACTURER | UPPER(12) | VARCHAR2(12) | Manufacturer | N |
| MKTPLCITEM | YORN | NUMBER | Marketplace Item | Y |
| MODELNUM | ALN(8) | VARCHAR2(8) | Model | N |
| ORDERUNIT | UPPER(16) | VARCHAR2(16) | Order unit | N |
| ORGID | UPPER(8) | VARCHAR2(8) | Organization ID | Y |
| PR | UPPER(8) | VARCHAR2(8) | PR | N |
| PRLINENUM | INTEGER | NUMBER | PR Line | N |
| RATE | AMOUNT | NUMBER(10,2) | Rate | Y |
| RATEHASCHANGED | YORN | NUMBER | Rate changed | Y |
| REQUESTBY | UPPER(100) | VARCHAR2(100) | Requested by | N |
| REQUESTNUM | UPPER(20) | VARCHAR2(20) | Request | N |
| REQUIREDATE | DATETIME | DATE | Required Date | N |
| RESTYPE | UPPER(16) | VARCHAR2(16) | Reservation type | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo tecnico maximo | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | Site | Y |
| STORELOCSITE | UPPER(8) | VARCHAR2(8) | Storeroom site | N |
| UNITCOST | AMOUNT | NUMBER(10,2) | Unit cost | N |
| UNITCOSTHASCHANGED | YORN | NUMBER | Unit cost changed | Y |
| VENDOR | UPPER(12) | VARCHAR2(12) | Vendor | N |
| VENDORPACKCODE | ALN(12) | VARCHAR2(12) | Vendor pack code | N |
| VENDORPACKQUANTITY | ALN(12) | VARCHAR2(12) | Vendor Pack Quantity | N |
| VENDORUNITCOST | AMOUNT | NUMBER(10,2) | Vendor Unit cost | N |
| VENDORWAREHOUSE | ALN(12) | VARCHAR2(12) | Vendor Warehouse | N |
| WONUM | UPPER(25) | VARCHAR2(25) | Work Order | Y |
| WPITEMID | BIGINT | NUMBER | ID WPITEM | Y |
| WPLABORREF |  | NUMBER | Crew reference | N |
| X4CHANGEDATE | DATETIME | DATE | Data modifica | Y |

## JOBSERVICE: Data Model Gestione risorse esterne nel piano di lavoro

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4KPI | AMOUNT | NUMBER(16,2) | KPI economico risorse esterne | N |
| ITEMNUM | UPPER(30) | VARCHAR2(30) | Codice risorsa esterna | Y |
| X4TOLLERANZA | INTEGER | NUMBER | Range di tolleranza | N |
| AMCREW |  | VARCHAR2(8) |  | N |
| AMCREWTYPE |  | VARCHAR2(8) |  | N |
| CALCAPPLYTO |  | VARCHAR2(10) |  | Y |
| CALCEXPRESSION |  | VARCHAR2(100) |  | N |
| CALCEXPRESSIONHOURS |  | VARCHAR2(100) |  | N |
| CALCLEVEL |  | VARCHAR2(8) |  | N |
| CALCMETHOD |  | VARCHAR2(15) |  | Y |
| CONDITIONCODE |  | VARCHAR2(30) |  | N |
| CONDITIONNUM |  | VARCHAR2(12) |  | N |
| DIRECTREQ |  | NUMBER |  | Y |
| HOURS |  | FLOAT |  | Y |
| ITEMQTY |  | NUMBER(15,2) |  | Y |
| ITEMSETID |  | VARCHAR2(8) |  | Y |
| JM1 |  | VARCHAR2(10) |  | N |
| JM2 |  | VARCHAR2(10) |  | N |
| JM3 |  | NUMBER(15,2) |  | N |
| JM4 |  | VARCHAR2(10) |  | N |
| JM5 |  | NUMBER(10,2) |  | N |
| JM6 |  | VARCHAR2(10) |  | N |
| JOBITEMID |  | NUMBER |  | Y |
| JOBLABORREF |  | NUMBER |  | N |
| JOBPLANID |  | NUMBER |  | Y |
| JPNUM |  | VARCHAR2(20) |  | Y |
| JPTASK |  | NUMBER |  | N |
| LINETYPE |  | VARCHAR2(15) |  | Y |
| LOCATION |  | VARCHAR2(25) |  | N |
| ORGID |  | VARCHAR2(8) |  | N |
| PLUSCJPREVNUM |  | NUMBER |  | Y |
| RATE |  | NUMBER(10,2) |  | Y |
| RESERVEREQ |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| SITEID |  | VARCHAR2(8) |  | N |
| STORELOCSITE |  | VARCHAR2(8) |  | N |
| VENDOR |  | VARCHAR2(12) |  | N |

## X4CRAFTPERYEAR: Data model Gestione tariffario risorse

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| PAYRATE | AMOUNT | NUMBER(10,2) | Tariffa oraria | N |
| CRAFT | UPPER(8) | VARCHAR2(8) | Competenza(Operaio,Impiegato,Quadro) | N |
| UDB | UPPER(20) | VARCHAR2(20) | UdB di riferimento | N |
| ANNO | INTEGER | NUMBER | Anno di riferimento | N |
| SCENARIO | ALN(25) | VARCHAR2(25) | Scenario importato | N |
| X4CRAFTPERYEARID | BIGINT | NUMBER | ID X4CRAFTPERYEAR | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| ORGID |  | VARCHAR2(8) |  | N |

## JOBLABOR: Data Model Gestione risorse interne nel piano di lavoro

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| LABORHRS | DURATION | FLOAT | Ore figura | Y |
| JOBLABORID | BIGINT | NUMBER | ID JOBLABOR | Y |
| AMCREW |  | VARCHAR2(8) |  | N |
| AMCREWTYPE |  | VARCHAR2(8) |  | N |
| APPTREQUIRED |  | NUMBER |  | Y |
| CALCAPPLYTO |  | VARCHAR2(10) |  | Y |
| CALCEXPRESSION |  | VARCHAR2(100) |  | N |
| CALCEXPRESSIONHOURS |  | VARCHAR2(100) |  | N |
| CALCLEVEL |  | VARCHAR2(8) |  | N |
| CALCMETHOD |  | VARCHAR2(15) |  | Y |
| CONDITIONNUM |  | VARCHAR2(12) |  | N |
| CONTRACTNUM |  | VARCHAR2(8) |  | N |
| CRAFT |  | VARCHAR2(8) |  | N |
| CREWWORKGROUP |  | VARCHAR2(20) |  | N |
| JL1 |  | VARCHAR2(10) |  | N |
| JL2 |  | DATE |  | N |
| JL3 |  | VARCHAR2(10) |  | N |
| JL4 |  | VARCHAR2(10) |  | N |
| JL5 |  | VARCHAR2(10) |  | N |
| JL6 |  | NUMBER(15,2) |  | N |
| JL7 |  | VARCHAR2(10) |  | N |
| JL8 |  | VARCHAR2(10) |  | N |
| JL9 |  | VARCHAR2(10) |  | N |
| JOBPLANID |  | NUMBER |  | Y |
| JPNUM |  | VARCHAR2(20) |  | Y |
| JPTASK |  | NUMBER |  | N |
| LABORCODE |  | VARCHAR2(8) |  | N |
| ORGID |  | VARCHAR2(8) |  | Y |
| PLUSCJPREVNUM |  | NUMBER |  | Y |
| QUANTITY |  | NUMBER |  | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| SITEID |  | VARCHAR2(8) |  | N |
| SKILLLEVEL |  | VARCHAR2(15) |  | N |
| VENDOR |  | VARCHAR2(12) |  | N |

## X4SERBATOIO: Data Model Gestione Serbatoi

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDSERBATOIO | ALN(50) | VARCHAR2(50) | Codice serbatoio univoco | N |
| ANNO | INT | NUMBER | Anno | N |
| UDB | UPPER(20) | VARCHAR2(20) | UdB di ripartizione del serbatoio | N |
| X4SERBATOIOID | BIGINT | NUMBER | ID X4SERBATOIO | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| TOTCOIN |  | VARCHAR2(15) |  | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| HASLD |  | NUMBER |  | Y |

## X4SERBATOIODETAILS: Data Model Gestione ripartizione effort e fisico

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| FISICO | YORN | NUMBER | Quantità di fisico del serbatoio | Y |
| COIN | ALN(15) | VARCHAR2(15) | Codice COIN | N |
| TERNA | INT | NUMBER | Percentuale TERNA | N |
| CRAFT | UPPER(8) | VARCHAR2(8) | Competenza(Operaio,Impiegato,Quadro) | N |
| RETE | INT | NUMBER | Percentuale RETE | N |
| GENNAIO | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| FEBBRAIO | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| MARZO | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| APRILE | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| MAGGIO | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| GIUGNO | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| LUGLIO | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| AGOSTO | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| SETTEMBRE | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| OTTOBRE | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| NOVEMBRE | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| DICEMBRE | ALN(10) | VARCHAR2(10) | Percentuale ripartita per il mese | N |
| X4SERBATOIOID | BIGINT | NUMBER | ID X4SERBATOIO | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| X4SERBATOIODETAILSID |  | NUMBER |  | Y |
| SOCIETA |  | VARCHAR2(5) |  | N |
| TOTALE |  | FLOAT |  | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| HASLD |  | NUMBER |  | Y |

## X4MATRICEDEST: Data Model Gestione del tempo di trasferimento dell’ordine di lavoro

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ORIGINE | ALN(20) | VARCHAR2(20) | Origine | N |
| DESTINAZIONE | UPPER(30) | VARCHAR2(30) | Destinazione | N |
| TTRASF | DURATION | FLOAT | Tempo di trasferimento | N |
| X4MATRICEDESTID | BIGINT | NUMBER | ID X4MATRICEDEST | Y |
| DESCRIPTION |  | VARCHAR2(50) |  | N |
| HASLD |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |

## WOSTATUS: Data Model Storico dello stato dell’ordine

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| STATUS | UPPER(16) | VARCHAR2(16) | Stato OdL | Y |
| WONUM | UPPER(25) | VARCHAR2(25) | Ordine di lavoro | Y |
| CHANGEBY | UPPER(100) | VARCHAR2(100) | Matricola | Y |
| CHANGEDATE | DATETIME | DATE | Data modifica | Y |
| MEMO | ALN(50) | VARCHAR2(50) | Note | N |
| GLACCOUNT |  | VARCHAR2(110) | Codice di conto contabile | N |
| FINCNTRLID | UPPER(8) | VARCHAR2(8) | ID controllo finanziario Maximo | N |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Sito asset | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | Y |
| WOSTATUSID | BIGINT | NUMBER | Stato dell’ordine di lavoro | Y |
| PARENT | UPPER(25) | VARCHAR2(25) | Stato padre del record | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| X4KEYMIG |  | NUMBER |  | N |
| X4TECMIG |  | NUMBER |  | Y |

## X4ESITOINT: Data Model Gestione Esito Integrazioni

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DESC_ESITO | ALN(2000) | VARCHAR2(2000) | Descrizione esito | N |
| DEST | ALN(50) | VARCHAR2(50) | Destinazione | N |
| END_ELAB | DATETIME | DATE | Fine Elaborazione | N |
| ESITO | ALN(10) | VARCHAR2(10) | Esito(Es. KO o OK) | N |
| CODESITO | ALN(30) | VARCHAR2(30) | Codice d’errore | N |
| IDINTERFACCIA | ALN(50) | VARCHAR2(50) | ID Interfaccia | N |
| PARENT | INT | NUMBER | Parent | N |
| SOURCE | ALN(50) | VARCHAR2(50) | Partenza | N |
| START_ELAB | DATETIME | DATE | Inizio Elaborazione | N |
| UNIQUEID | ALN(50) | VARCHAR2(50) | ID Univoco per il flusso | N |
| X4ESITOINTID | BIGINT | NUMBER | X4EsitointID | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | N |
| KAFKATOPIC | ALN(50) | VARCHAR2(50) | Codice Kafka di elaborazione | N |
| MESSAGE | CLOB | CLOB | Messaggio Json d’input. Viene salvato se necessario | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| ASSETNUM |  | VARCHAR2(25) |  | N |
| WONUM |  | VARCHAR2(25) |  | N |
| HASLD |  | NUMBER |  | Y |

## X4TECPATR:Data Model Intersezione tecnico-patrimoniale

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CBSPIAN | ALN(20) | VARCHAR2(20) | CBS pianificata | N |
| IMPORTOPIAN | FLOAT | FLOAT | Importo pianificato | N |
| INDICATOREPIAN | UPPER(150) | VARCHAR2(150) | Indicatore pianificato | N |
| UMPIAN | ALN(4) | VARCHAR2(4) | Unità di misura pianificata | N |
| MESEPIAN | ALN(10) | VARCHAR2(10) | Mese pianificato | N |
| ANNOPIAN | INT | NUMBER | Anno pianificato | N |
| CBSCONS | ALN(20) | VARCHAR2(20) | CBS consuntivata | N |
| IMPORTOCONS | FLOAT | FLOAT | Importo consuntivato | N |
| INDICATORECONS | UPPER(150) | VARCHAR2(150) | Indicatore consuntivato | N |
| UMCONS | ALN(4) | VARCHAR2(4) | Unità di misura consuntivata | N |
| MESECONS | ALN(10) | VARCHAR2(10) | Mese consuntivato | N |
| ANNOCONS | INT | NUMBER | Anno consuntivato | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| X4TECPATRID |  | NUMBER |  | Y |

## X4RENAMESAPIDGEO: Data Model SAP di Geocall

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4RENAMESAPIDGEOID | BIGINT | NUMBER | PK | Y |
| ASSETNUM | ALN(30) | VARCHAR(30) | AssetNum modificato | N |
| SITEID | ALN(30) | VARCHAR(30) | SiteId Associato | N |
| SAPID_ORIG | ALN(30) | VARCHAR(30) | Vecchio Sapid | N |
| SAPID_DEST | ALN(30) | VARCHAR(30) | Nuovo Sapid | N |
| UPDATETIME | DATETIME | DATE | Data creazione/modifica | N |
| DA_NOTIFICARE_GEOCALL | ALN(1) | VARCHAR(1) | Record da elaborare e modificare a Geocall. Può assumere valore Y o N | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |

## LABOR: Data Model Risorse(Matricole) terna

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| AVAILFACTOR | AMOUNT | NUMBER(15,2) |  | Y |
| CREWID | ALN(12) | VARCHAR(12) |  | N |
| CREWWORKGROUP | UPPER(20) | VARCHAR(20) |  | N |
| ENDLOCATION | UPPER(25) | VARCHAR(25) |  | N |
| ENDLOCSITEID | UPPER(8) | VARCHAR(8) |  | N |
| EXTERNALREFID | ALN(10) | VARCHAR(10) |  | N |
| LABINVENTORYLOC | UPPER(25) | VARCHAR(25) |  | N |
| LABINVENTORYSITE | UPPER(8) | VARCHAR(8) |  | N |
| LABORCODE | UPPER(8) | VARCHAR(8) |  | Y |
| LABORID | BIGINT | NUMBER |  | Y |
| LBSDATAFROMWO | YORN | NUMBER |  | Y |
| LBSINTERVAL | INTEGER | NUMBER |  | N |
| ORGID | UPPER(8) | VARCHAR(8) |  | Y |
| OWNERSYSID | UPPER(10) | VARCHAR(10) |  | N |
| PERSONID | UPPER(100) | VARCHAR(100) |  | Y |
| REPORTEDHRS | DURATION | FLOAT |  | Y |
| ROWSTAMP |  | VARCHAR(40) |  | Y |
| SENDERSYSID | UPPER(50) | VARCHAR(50) |  | N |
| SOURCESYSID | UPPER(10) | VARCHAR(10) |  | N |
| STARTLOCATION | UPPER(25) | VARCHAR(25) |  | N |
| STARTLOCSITEID | UPPER(8) | VARCHAR(8) |  | N |
| STATUS | UPPER(12) | VARCHAR(12) |  | N |
| TYPE | UPPER(16) | VARCHAR(16) |  | N |
| WORKLOCATION | UPPER(25) | VARCHAR(25) |  | N |
| WORKSITE | UPPER(8) | VARCHAR(8) |  | N |
| YTDHRSREFUSED | DURATION | FLOAT |  | Y |
| YTDOTHRS | DURATION | FLOAT |  | Y |

## ASSETSPEC: Data Model Gestione delle specifiche tecniche degli asset

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| TABLEVALUE | ALN(254) | VARCHAR(254) | Valore tabella | N |
| ASSETATTRID | UPPER(50) | VARCHAR(50) | ID Attributo | Y |
| DATEVALUE | DATETIME | DATE | Valore data | N |
| ALNVALUE | ALN(254) | VARCHAR(254) | Valore alfanumerico | N |
| NUMVALUE | DECIMAL | NUMBER(30,10) | Valore numeric | N |
| MEASUREUNITID | UPPER(16) | VARCHAR(16) | Unità di misura | N |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | Numero Asset | Y |
| CLASSSTRUCTUREID | UPPER(26) | VARCHAR2(26) | ID Classtructure | Y |
| INHERITEDFROMITEM | YORN | NUMBER |  | Y |
| ITEMSPECVALCHANGED | YORN | NUMBER |  | Y |
| DISPLAYSEQUENCE | SMALLINT | NUMBER |  | Y |
| CHANGEDATE | DATETIME | DATE |  | Y |
| CHANGEBY | UPPER(100) | VARCHAR2(100) |  | Y |
| ES01 | ALN(10) | VARCHAR2(10) |  | N |
| ES02 | ALN(10) | VARCHAR2(10) |  | N |
| ES03 | ALN(10) | VARCHAR2(10) |  | N |
| ES04 | DATETIME | DATE |  | N |
| ES05 | DECIMAL | NUMBER(15,2) |  | N |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | Y |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | Y |
| SECTION | UPPER(30) | VARCHAR2(10) |  | N |
| ASSETSPECID | BIGINT | NUMBER |  | Y |
| MANDATORY | YORN | NUMBER |  | Y |
| LINKEDTOATTRIBUTE | UPPER(50) | VARCHAR2(50) |  | N |
| LINKEDTOSECTION | UPPER(30) | VARCHAR2(30) |  | N |
| STARTASSETFEATUREID | BIGINT | NUMBER | ID StartAssetFeature | N |
| STARTMEASURE | DECIMAL | NUMBER(10,2) |  | N |
| STARTOFFSET | DECIMAL | NUMBER(10,2) |  | M |
| ENDASSETFEATUREID | BIGINT | NUMBER | ID EndAssetFeature | N |
| ENDMEASURE | DECIMAL | NUMBER(10,2) |  | N |
| ENDOFFSET | DECIMAL | NUMBER(10,2) |  | N |
| CONTINUOUS | YORN | NUMBER |  | Y |
| ENDYOFFSET | DECIMAL | NUMBER(10,2) |  | N |
| STARTYOFFSET | DECIMAL | NUMBER(10,2) |  | N |
| ENDZOFFSET | DECIMAL | NUMBER(10,2) |  | N |
| STARTZOFFSET | DECIMAL | NUMBER(10,2) |  | N |
| STARTYOFFSETREF | ALN(30) | VARCHAR2(30) |  | N |
| STARTZOFFSETREF | ALN(30) | VARCHAR2(30) |  | N |
| ENDYOFFSETREF | ALN(30) | VARCHAR2(30) |  | N |
| ENDZOFFSETREF | ALN(30) | VARCHAR2(30) |  | N |
| BASEMEASUREUNITID | UPPER(16) | VARCHAR2(16) | ID BaseMeasureUnit | N |
| STARTBASEMEASURE | DECIMAL | NUMBER(10,2) |  | N |
| ENDBASEMEASURE | DECIMAL | NUMBER(10,2) |  | N |
| STARTMEASUREUNITID | UPPER(16) | VARCHAR2(16) | ID StartMeasureUnit | N |
| ENDMEASUREUNITID | UPPER(16) | VARCHAR2(16) | ID EndMeasureUnit | N |
| STARTOFFSETUNITID | UPPER(16) | VARCHAR2(16) | ID StartOffsetUnit | N |
| ENDOFFSETUNITID | UPPER(16) | VARCHAR2(16) | ID EndOffsetUnit | N |
| LINEARASSETSPECID | BIGINT | NUMBER | ID LinearAssetSpec | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| X3DATAMODIFICA | DATETIME | DATE | Data modifica | N |
| ANYWHEREREFID | BIGINT | VARCHAR2(50) | ID AnywhereRef | N |

## ASSETATTRIBUTE: Data Model Gestione degli attribute dell’asset

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DATATYPE | UPPER(8) | VARCHAR(8) | Tipo di valore | Y |
| DESCRIPTION | ALN(100) | VARCHAR(100) | Descrizione | N |
| ASSETATTRIBUTEID |  | NUMBER |  | Y |
| ASSETATTRID |  | VARCHAR2(50) |  | Y |
| ATTRDESCPREFIX |  | VARCHAR2(8) |  | N |
| DOMAINID |  | VARCHAR2(18) |  | N |
| MEASUREUNITID |  | VARCHAR2(16) |  | N |
| ORGID |  | VARCHAR2(8) |  | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| SITEID |  | VARCHAR2(8) |  | N |
| X3GRUPPO_RESP |  | VARCHAR2(20) |  | N |
| X3LENGTH |  | NUMBER |  | N |
| X3SCALE |  | NUMBER |  | N |

## X3SAPASSET: Data Model Asset SAP

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| MAPPINGERROR | YORN | NUMBER | Errore di mapping | Y |
| PARENTSTRNO | UPPER(50) | VARCHAR2(50) | Parent | N |
| ROOTASSETTYPE | UPPER(1) | VARCHAR2(1) | Tipo asset root | Y |
| ROOTSTRNO | UPPER(50) | VARCHAR2(50) | Root | Y |
| SAPCLASS | UPPER(50) | VARCHAR2(50) | Classe SAP | Y |
| SAPLEVEL | INTEGER | NUMBER | Livello SAP | Y |
| SAPOBJECT | UPPER(1) | VARCHAR2(1) | Oggetto SAP | Y |
| SAPUID | UPPER(30) | VARCHAR2(30) | UID SAP | N |
| X3SAPASSETID | BIGINT | NUMBER | ID X3SAPASSETID | Y |
| DUMMY |  | NUMBER |  | Y |
| ERROR |  | VARCHAR2(500) |  | N |
| ERRORFLAG |  | NUMBER |  | Y |
| HASLD |  | NUMBER |  | Y |
| HISTORYFLAG |  | NUMBER |  | Y |
| INVERSIONFLAG |  | NUMBER |  | Y |
| OLDSTRNO |  | VARCHAR2(50) |  | N |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |
| SAPPARENTUID |  | VARCHAR2(50) |  | N |
| SKIPSTEP2 |  | NUMBER |  | Y |
| STEP3PENDING |  | NUMBER |  | Y |
| STRNO |  | VARCHAR2(50) |  | Y |
| SYNCHACTION |  | VARCHAR2(50) |  | N |
| WARNINGS |  | VARCHAR2(500) |  | N |

## X3SAPTOMAXOBJECT: Data Model Oggetto SAP Maximo

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| HIERARCHYPATH | UPPER(250) | VARCHAR2(250) | Gerarchia | Y |
| MAXOBJECT | UPPER(30) | VARCHAR2(30) | Max Object | Y |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | Y |
| ROOTASSETNUM | UPPER(25) | VARCHAR2(25) | Numero asset root | Y |
| SAPASSETID | BIGINT | NUMBER | ID SAPASSET | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | Y |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) | ID Classificazione | N |
| X3SAPTOMAXOBJECTID | BIGINT | NUMBER | ID X3SAPTOMAXOBJECT | Y |
| OBJECTNUM |  | VARCHAR2(25) |  | Y |
| ROWSTAMP |  | VARCHAR2(40) |  | Y |

## X4INTERF_WO: Data Model Gestione delle rilevazioni di interferenze

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4INTERFSTATID | BIGINT | NUMBER | ID X4INTERFSTAT | N |
| DESCRIPTION | ALN(250) | VARCHAR2(250) | Descrizione dell’interferenza | N |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | Asset interferenza | N |
| WONUM | UPPER(25) | VARCHAR2(25) | Wonum collegato | N |
| SITEID | UPPER(8) | VARCHAR2(8) | Sede dell’asset | N |
| IDINTERFERENZA | ALN(50) | VARCHAR2(50) | ID tabellare dell’interferenza | N |
| STATO | ALN(50) | VARCHAR2(50) | Stato della difformità/interferenza | N |
| PRIMARILEVAZIONE | DATETIME | DATE | Data della prima rilevazione | N |
| ULTIMARILEVAZIONE | DATETIME | DATE | Data dell’ultima rilevazione | N |
| GRAVITA | ALN(50) | VARCHAR2(50) | Gravità | N |
| RISOLTA | YORN | NUMBER | Risolta | N |
| IDMISURA | ALN(30) | VARCHAR2(30) | ID Misura | N |
| VALMISURA | ALN(50) | VARCHAR2(50) | Valore Misura | N |
| X4NOTEMIG | ALN(250) | VARCHAR2(250) | Note MIG | N |
| NOTE | ALN(150) | VARCHAR2(150) | Note | N |
| IDINTERFERENZA_EXT |  | VARCHAR2(50) | Codice identificativo dell’interferenza | N |
| NUMERO_COLLISIONI | INTEGER | NUMBER | Numero collisioni rilevate | N |
| FASE | ALN(4) | VARCHAR2(4) | Fase | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4CATMODELLO: Data Model Catalogo modelli-target

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDTARGET | ALN(50) | VARCHAR2(50) | ID Target |  |
| DESCRIPTION | ALN(150) | VARCHAR2(150) | Descrizione Target | N |
| TIPOLOGIAASSET | UPPER(192) | VARCHAR2(192) | Tipologia asset | N |
| NEWVALUE | INTEGER | INT | Valore target massivo | N |
| X4CATMODELLOID | BIGINT | NUMBER | ID univoco/chiave della tabella X4CATMODELLO | Y |
| IDMODELLO | UPPER(20) | VARCHAR2(20) | ID Modello | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4CATMODELLOASSET: Data Model Gestione Catalogo modelli-target e Asset

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4CATMODELLOID | BIGINT | NUMBER | ID univoco/chiave della tabella X4CATMODELLO | N |
| X4CATMODELLOASSETID | BIGINT | NUMBER | ID X4CATMODELLOASSET | Y |
| VALUE | ALN(50) | VARCHAR2(50) | Valore attributo | N |
| ATTRIBUTO | UPPER(50) | VARCHAR2(50) | Attributo | N |
| IDCLUSTER | INT | NUMBER | Modello | N |
| N1 | FLOAT | FLOAT | Valore target | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| HASLD |  | NUMBER | Campo di sistema | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| SELEG | AMOUNT | NUMBER(4,2) | Seleg | N |

## X4CATINTERF: Data Model Catalogo interferenze

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ASSIEME | ALN(30) | VARCHAR2(30) | Assieme | N |
| DESCRIPTION | ALN(250) | VARCHAR2(250) | Descrizione interferenza | N |
| IDINTERFERENZA | ALN(20) | VARCHAR2(50) | ID Interferenza | N |
| IDMISURA | ALN(50) | VARCHAR2(50) | ID Misura | N |
| SOTTOASSIEME | ALN(50) | VARCHAR2(50) | Sottoassieme | N |
| STATUS | ALN(20) | VARCHAR2(20) | Stato interferenza | N |
| TEMPOVAL | INT | NUMBER | Massimo tempo di validazione | N |
| TIPOLOGIAASSET | UPPER(192) | VARCHAR2(192) | Tipologia asset | N |
| CHANGEBY | UPPER(100) |  | Modificato da | N |
| CHANGEDATE | DATETIME |  | Data modifica | N |
| GENMISURA | YORN | NUMBER | Flag di sussistenza logica di interferenza | Y |
| DOCUFOTOOBBLIGA | YORN | NUMBER | Obbligatorietà foto | Y |
| FASE | YORN | NUMBER | Fase | Y |
| X4CATINTERFID | BIGINT(19) | NUMBER | ID univoco/chiave della tabella X4CATINTERF | Y |
| HASLD |  | NUMBER | Campo di sistema | Y |
| LOGICAINT | ALN(500) | VARCHAR2(500) | Logica di calcolo dell’interferenza | N |
| MISURA | YORN | NUMBER | Valore della misura | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| STATUS | ALN(20) | VARCHAR2(20) | Stato dell’interferenza | N |

## ASSETLOCRELATION: Data Model Gestione delle relazioni tra gli asset

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X3SAPID | UPPER(30) | VARCHAR2(30) | SAP ID della campata | N |
| TARGETASSETNUM | UPPER(25) | VARCHAR2(25) | Asset relazionato gerarchicamente superiore | N |
| SOURCEASSETNUM | UPPER(25) | VARCHAR2(25) | Asset relazionato gerarchicamente inferiore | N |

## X4GEOCALLADDRESS: Data Model Gerarchia EAM combinate allo stradario e coordinate geografiche di SISNET

| Campo | TIPO MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| TIPO_OGGETTO | ALN(20) | VARCHAR(20) | Tipo oggetto | N |
| STATO_OGGETTO | ALN(20) | VARCHAR(20) | Stato oggetto | N |
| CODICE_OGGETTO | ALN(30) | VARCHAR(30) | Codice oggetto – SAPID | N |
| CODICE_PADRE | ALN(30) | VARCHAR(30) | Codice Padre – SAPID | N |
| CODICE_RADICE | ALN(30) | VARCHAR(30) | Codice Radice della gerarchia | N |
| CODICE_SAP | ALN(30) | VARCHAR(30) | Codice SAP | N |
| CODICE_PADRE2 | ALN(30) | VARCHAR2(30) | Codice Padre - SAPID | N |
| CODICE_RADICE2 | ALN(30) | VARCHAR(30) | Codice Radice per gli elementi in doppia terna | N |
| CODICE_SAP2 | ALN(30) | VARCHAR(30) | Codice SAP2 per gli elementi in doppia terna | N |
| DESCRIZIONE | ALN(50) | VARCHAR(50) | Descrizione | N |
| FILIALE | ALN(20) | VARCHAR(20) | Filiale | N |
| TOPONIMO | ALN(20) | VARCHAR(20) | Toponimo | N |
| VIA | ALN(60) | VARCHAR(60) | Via | N |
| CIVICO | ALN(15) | VARCHAR(15) | Civico | N |
| LOCALITA | ALN(5) | VARCHAR(5) | Località | N |
| PROVINCIA | ALN(25) | VARCHAR(25) | Provincia | N |
| CAP | ALN(10) | VARCHAR(10) | CAP | N |
| COMUNE | ALN(40) | VARCHAR(40) | Comune | N |
| ISTAT | ALN(20) | VARCHAR(20) | ISTAT | N |
| LATITUDINE1 | DECIMAL | NUMBER | Latitudine | N |
| LONGITUDINE1 | DECIMAL | NUMBER | Longitudine | N |
| LATITUDINE2 | DECIMAL | NUMBER | Latitudine2 opzionale | N |
| LONGITUDINE2 | DECIMAL | NUMBER | Longitudine2 opzionale | N |

## X4SISNETADDRESS: Data Model Informazioni ricevute da SISNET

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4SISNETADDRESSID |  | NUMBER | ID X4SISNETADDRESS | Y |
| TIPO_OGGETTO |  | VARCHAR(20) | Tipo Oggetto | N |
| STATO_OGGETTO |  | VARCHAR(20) | Stato Oggetto | N |
| CODICE_OGGETTO |  | VARCHAR(30) | Codice Oggetto – SAPID | Y |
| CODICE_PADRE |  | VARCHAR(30) | Codice Padre - SAPID | N |
| CODICE_RADICE |  | VARCHAR(30) | Codice Radice – SAPID | N |
| LIVELLO |  | VARCHAR(1) | Livello(1,2,3) | N |
| DESCRIZIONE |  | VARCHAR(40) | Descrizione | N |
| AREA_LAVORAZIONE |  | VARCHAR(20) | Area di lavorazione | N |
| CENTRO_DI_LAVORO |  | VARCHAR(20) | Centro di Lavoro | N |
| FILIALE |  | VARCHAR(20) | Filiale | N |
| TOPONIMO |  | VARCHAR(20) | Toponimo | N |
| VIA |  | VARCHAR(60) | Via | N |
| CIVICO |  | VARCHAR(15) | Civico | N |
| LOCALITA |  | VARCHAR(5) | Località | N |
| PROVINCIA |  | VARCHAR(25) | Provincia | N |
| CAP |  | VARCHAR(10) | CAP | N |
| COMUNE |  | VARCHAR2(40) | Comune | N |
| ISTAT |  | VARCHAR(20) | ISTAT | N |
| LATITUDINE1 |  | NUMBER | Latitudine | N |
| LONGITUDINE1 |  | NUMBER | Longitudine | N |
| LATITUDINE2 |  | NUMBER | Latitudine2 Opzionale | N |
| LONGITUDINE2 |  | NUMBER | Longitudine2 Opzionale | N |
| CREATION_DATE |  | DATE | Data Creazione Record | N |
| UPDATE_DATE |  | DATE | Data Aggiornamento Record | N |
| DA_NOTIFICARE_GEOCALL |  | NUMBER | Indica se il record è da notificare a Geocall | N |
| NOTE |  | VARCHAR2(500) | Note | N |

## X4REQUESTTAGETIK: Data Model Richieste Tagetik, è realizzato come oggetto non persistente

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CONTO | ALN(8) |  | Conto richiesta |  |
| SCENARIO | ALN(20) | VARCHAR2(20) | Scenario richiesta |  |
| TITRIS | ALN(20) | VARCHAR2(20) | Titris richiesta |  |

## X4DIFFOR_WO: Data Model Difformità rilevate

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | Asset | N |
| IDDIFFORMITA | ALN(30) | VARCHAR2(30) | ID Difformità | N |
| DIFFORMITA | ALN(50) | VARCHAR2(50) | Difformità | N |
| GRAVITA | ALN(100) | VARCHAR2(100) | Gravita | N |
| STATUS | ALN(50) | VARCHAR2(50) | Stato | N |
| WONUM | UPPER(25) | VARCHAR2(25) | Ordine di Lavoro | N |
| IDMISURA | ALN(30) | VARCHAR2(30) | ID Misura | N |
| VALMISURA | INTEGER | NUMBER | Valore misura | N |
| FASE | UPPER(4) | VARCHAR2(4) | Fase | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo sistema | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Site associato all’assetnum | N |
| X4DIFFOR_WOID | BIGINT | NUMBER | ID X4DIFFOR_WO | Y |
| X4DIFFORSTATID | BIGINT | NUMBER | ID X4DIFFORSTAT | N |
| DATAULTIMARIL | DATETIME | DATE | Data ultimo rilevamento | N |
| RISOLTA | YORN | NUMBER | Flag | Y |
| X4NOTEMIG | ALN(250) | VARCHAR2(250) | Note MIG | N |
| NOTE | ALN(150) | VARCHAR2(150) | Note | N |
| X4TECMIG |  | NUMBER |  | Y |
| X4KEYMIG |  | NUMBER |  | N |

## X4CATDIFFOR: Data Model Catalogo difformità

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| TIPOLOGIAASSET | UPPER(192) | VARCHAR2(192) | Tipologia Asset | N |
| DESCRIPTION | ALN(250) | VARCHAR2(250) | Descrizione difformità | N |
| ASSIEME | ALN(50) | VARCHAR2(50) | Assieme | N |
| IDDIFFORMITA | ALN(30) | VARCHAR2(30) | ID Difformità | N |
| IDMISURA | ALN(100) | VARCHAR2(100) | ID Misura | N |
| SOTTOASSIEME | ALN(60) | VARCHAR2(60) | Sottoassieme | N |
| STATUS | ALN(15) | VARCHAR2(15) | Stato difformità | Y |
| TEMPOVALID | INTEGER | NUMBER | Massimo tempo di validazione | N |
| TIPOLOGIA | ALN(30) | VARCHAR2(30) | Tipologia difformità | N |
| CHANGEBY | UPPER(100) |  | Modificato da | N |
| CHANGEDATE | DATETIME |  | Data modifica | N |
| GENMISURA | YORN | NUMBER | Generazione da misura | Y |
| DOCUFOTOOBBLIGA | YORN | NUMBER | Obbligatorietà foto | Y |
| X4CATDIFFORID | BIGINT(19) | NUMBER | ID univoco/chiave della tabella X4CATDIFFOR | Y |
| FASE | YORN | NUMBER | Fase | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| PESOAPERTE | FLOAT | FLOAT | Peso delle difformità per HI in apertura | Y |
| PESOCHIUSE | FLOAT | FLOAT | Peso delle difformità per HI in chiusura | Y |
| LOGICADIFF | ALN(150) | VARCHAR2(150) | Logica generazione della difformità | N |

## X4DIFFORSTAT: Data Model Gestione difformità relazionate all’anagrafica

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | Asset | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| IDDIFFORMITA | BIGINT | NUMBER | ID della difformità legata | N |
| IDDIFFORMITA_EXT | ALN(30) | VARCHAR(30) | ID descrittivo difformità | N |
| STATUS | ALN(15) | VARCHAR2(15) | Stato difformità | N |
| GRAVITA | ALN(50) | VARCHAR2(50) | Gravita difformità | N |
| PRIMARILEVAZIONE | DATETIME | DATE | Data Prima rilevazione | N |
| ULTIMARILEVAZIONE | DATETIME | DATE | Data Ultima rilevazione | N |
| IDMISURA | ALN(30) | VARCHAR2(30) | ID Misura | N |
| VALMISURA | INTEGER | NUMBER | Valore misura | N |
| FASE | UPPER(4) | VARCHAR2(4) | Fase | N |
| ODLRISOLUTORE | UPPER(25) | VARCHAR2(25) | OdL Risolutore | N |
| CAUSALE | ALN(50) | VARCHAR2(50) | Causale di superamento | N |
| CHANGEBY | UPPER(100) | VARCHAR2(100) | Matricola validatore | N |
| NONCONFERMATO | YORN | NUMBER | Non confermato? | Y |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | Sede dell’asset |  |
| X4DIFFORSTATID | BIGINT | NUMBER | ID X4DIFFORSTAT | Y |
| CHANGEDATE | DATETIME | DATE | Data ultima modifica | N |
| X4NOTEMIG | ALN(250) | VARCHAR2(250) | Note MIG | N |
| MATRICOLARILEVATORE |  | VARCHAR2(50) |  | N |
| MATRICOLARISOLUTORE |  | VARCHAR2(50) |  | N |
| MATRICOLASUPERATA |  | VARCHAR2(50) |  | N |
| X4IDDIFFSORGMIG |  | VARCHAR2(100) |  | N |
| X4TECMIG |  | NUMBER |  | Y |
| X4KEYMIG |  | NUMBER |  | N |

## X4MISUREWO: Data Model Misure rilevate all’interno degli OdL

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | Asset | N |
| IDMISURA | ALN(20) | VARCHAR2(20) | ID Misura | N |
| VALUE | FLOAT | FLOAT | Valore misura | N |
| FASE | UPPER(4) | VARCHAR2(4) | Fase | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| DIFFORMITA | ALN(30) | VARCHAR2(30) | ID Difformità | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di Sistema | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | N |
| WONUM | UPPER(25) | VARCHAR2(25) | Numero OdL | N |
| X4MISUREWOID | BIGINT | NUMBER | ID X4MISUREWO | Y |

## X4CATMISURE: Data Model Catalogo Misure e Variabili di stato

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDMISURA | ALN(20) | VARCHAR2(20) | ID Misura | N |
| DESCRIPTION | ALN(200) | VARCHAR2(200) | Descrizione misura / variabile | N |
| UM | ALN(40) | VARCHAR2(40) | Unità di misura | N |
| CALCOLO | ALN(150) | VARCHAR2(150) | Calcolo | N |
| TIPOLOGIAASSET | UPPER(192) | VARCHAR2(192) | Tipologia asset | N |
| RESET | YORN | NUMBER | Reset | Y |
| TIPOPARAMETRO | ALN(20) | VARCHAR2(20) | Tipo parametro | N |
| PERIODOCALCOLO | INTEGER | NUMBER | Periodo di clcolo(gg) | N |
| X4MISURAFORMULA | ALN(3500) | VARCHAR2(3500) | Formula della misura | N |
| X4CATMISUREID | BIGINT | NUMBER | ID X4CATMISURE | Y |
| DEC_INPUT |  | VARCHAR2(50) | Descrizione dell’input | N |
| INPUT |  | VARCHAR2(30) | Codice dell’input | N |
| TIPOLOGIAINPUT |  | VARCHAR2(50) | Tipologia input | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| RESET_T |  | NUMBER | Giorni per la prossima misurazione | N |
| RESETAC | ALN(20) | VARCHAR2(20) | AC Reset | N |
| EVENTORESET |  | VARCHAR2(300) | Evento reset | N |
| X4MXL | YORN | NUMBER | Flag che indica se caricati da MXloader o meno | Y |

## X4CATMISURECLASSIFICATIONS: Data Model Relazione tra misure/variabili e classificazioni

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) | Classificazione asset | N |
| X4CATMISUREID | BIGINT | NUMBER | ID univoco/chiave della tabella X4CATMISURE | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |

## X4CATMISUREPARAMETRI: Data Model Relazione tra parametri(di calcolo e di reset) e variabili

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CALCOLO_RESET | UPPER(8) | VARCHAR2(8) | Calcolo/Reset | Y |
| PARAMETER | ALN(100) | VARCHAR2(100) | Parametro | N |
| PARAMETER_TYPE | ALN(50) | VARCHAR2(50) | Tipo parametro | N |
| X4CATMISUREID | BIGINT | NUMBER | ID univoco/chiave della tabella X4CATMISURE | N |
| X4CATMISUREPARAMETRIID | BIGINT | NUMBER | ID univoco/chiave della tabella X4CATMISUREPARAMETRI | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |

## X4CATMISUREATTIVITASTATO: Data Model Relazione tra stati e parametri di calcolo di tipo “attività”

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| STATO | UPPER(16) | VARCHAR2(16) | Stato | N |
| X4CATMISUREPARAMETRIID | BIGINT | NUMBER | ID univoco/chiave della tabella X4CATMISUREPARAMETRI | N |
| X4CATMISUREATTIVITASTATOID | BIGINT | NUMBER | ID univoco/chiave della tabella X4CATMISUREATTIVITASTATO | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |

## X4DIFFATT: Data Model Difformità risolvibili da ogni PdL

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DIFFORMITA | ALN(20) | VARCHAR2(20) | ID Difformità | N |
| JPNUM | ALN(20) | VARCHAR2(20) | ID attività | N |
| ATTIVITA | ALN(20) | VARCHAR2(20) | ID PdL | N |
| CHANGEBY | ALN(100) | VARCHAR2(100) | Utente modificatore | N |
| CHANGEDDATE | DATETIME | DATE | Data di modifica | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di Sistema | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | N |
| X4DIFFATID | BIGINT | NUMBER | ID X4DIFFATID | Y |
| DESCRIPTION | ALN(150) | VARCHAR2(150) | Descrizione | N |
| DIFFORMITAID | BIGINT | NUMBER | ID Difformità | N |

## X4POLICY: Data Model Catalogo delle policy

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DATAFINEVAL | DATETIME | DATE | Data fine validazione | N |
| DATAINIZIOVAL | DATETIME | DATE | Data inizio validazione | N |
| X4POLICYID | BIGINT | NUMBER | ID univoco/chiave della tabella X4POLICY | Y |
| CHANGEDBY | UPPER(100) | VARCHAR2(100) | Modificato da… | N |
| CHANGEDDATE | DATETIME | DATE | Data modifica | N |
| LASTREVIEWDATE | DATETIME | DATE | Data ultima revisione | N |
| DIFFERIBILITA | INTEGER | NUMBER | Differibilità | N |
| FREQUENZA | INTEGER | NUMBER | Frequenza | N |
| IDMACROATTIVITA | UPPER(16) | VARCHAR2(16) | ID Macro-Attività associata alla policy | N |
| IDPOLICY | ALN(20) | VARCHAR2(20) | ID Policy | N |
| IDTIMING | ALN(3) | VARCHAR2(3) | ID Timing | N |
| MAXANTICIPO | INTEGER | NUMBER | Massimo anticipo | N |
| MAXPOSTICIPO | INTEGER | NUMBER | Massimo posticipo | N |
| STATUS | ALN(20) | VARCHAR2(20) | Stato | N |
| TIPOLOGIAASSET | UPPER(192) | VARCHAR2(192) | Elemento Asset | N |
| LIVPOLICY | INTEGER | NUMBER | Livello Policy | N |
| LIVCOMB | INTEGER | NUMBER | Livello combinazione input | N |
| X4RIFTEMPORALE | ALN(50) | VARCHAR2(50) | Riferimento temporale | Y |
| TIPOPOLICY | ALN(15) | VARCHAR2(15) | Tipo Policy | N |
| POLICYAGGR | YORN | NUMBER | Policy aggregata | Y |
| LIVAGGR | INT | NUMBER | Livello aggregazione policy | N |
| COMBINPUT | UPPER(1000) | VARCHAR2(1000) | Combinazione input (formula) | N |
| COMBINPUT_S | ALN(1000) | VARCHAR2(1000) | Valore di combinput ripulito dai caratteri speciali | N |
| CAMPAGNA | YORN | NUMBER | Campagna | Y |
| APARTIREDA | DATETIME | DATE | A partire da | Y |
| X4CLUSTER | ALN(50) | VARCHAR2(50) | Cluster | Y |
| X4SOTTOPROGRAMMA | ALN(50) | VARCHAR2(50) | Sotto-programma | Y |
| ELEGGIBILITA | YORN | NUMBER | Eleggibilità | Y |
| DESCRIPTION | ALN(250) | VARCHAR2(250) | Descrizione | N |
| IDATTIVITA | ALN(20) | VARCHAR2(20) | ID Attività output | N |
| UM | ALN(3) | VARCHAR2(3) | Unità di misura | N |
| SITEID | UPPER(8) | VARCHAR2(8) | Site ID associato alla tipologia di asset | N |
| VERSION | INTEGER | NUMBER | Versione della policy | N |
| LOGICATRONCO | YORN | NUMBER | Flag se policy è soggetta tronco | Y |
| X4MXL | YORN | NUMBER | Campo di riferimento per logiche DSS su inserimento massivo | Y |
| X4MXL2 | YORN | NUMBER | Campo di inserimento massivo | Y |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| HASINPUTS | YORN | NUMBER |  | Y |
| X4CARICAMENTOMXLOADER |  | VARCHAR2(1) |  | N |

## X4POLICYINPUT: Data Model Input delle policy

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| IDINPUT | ALN(25) | VARCHAR2(25) | ID Input | N |
| IDINPUT_EXT | ALN(50) | VARCHAR2(50) | ID Descrittivo input | N |
| IDPOLICY | BIGINT | NUMBER | ID policy | N |
| NINPUT | INTEGER | NUMBER | N Input | N |
| TARGETRAFFRONTO | ALN(50) | VARCHAR2(50) | ID Target raffronto | N |
| TARGETRAFFRONTO_EXT | ALN(50) | VARCHAR2(50) | ID descrittivo target raffronto | N |
| TIPOLOGIAINPUT | ALN(20) | VARCHAR2(20) | Tipologia Input | N |
| IDMISURA | ALN(25) | VARCHAR2(25) | ID Misura | N |
| IDMISURA_EXT | ALN(50) | VARCHAR2(50) | ID Descrittivo Misura | N |
| OPERATORE | ALN(15) | VARCHAR2(15) | Operatore logico | N |
| X4AND | ALN(50) | VARCHAR2(50) | AND | N |
| X4OR | ALN(50) | VARCHAR2(50) | OR | N |
| X4EQUAL | ALN(20) | VARCHAR2(20) | Uguale | N |
| X4NOTEQUAL | ALN(20) | VARCHAR2(20) | Diverso | N |
| TARGETNUM | FLOAT | FLOAT | Target di raffronto numerico | N |
| LIVINPUT | INTEGER | NUMBER | Livello Input | N |
| DATA | DATE | DATE | Data | N |
| VERSION | INTEGER | NUMBER | Versione della policy | N |
| X4POLICYINPUTID | BIGINT | NUMBER | ID X4POLICYINPUT | Y |
| X4POLICYID | BIGINT | NUMBER | ID X4POLICY | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| HASLD |  | NUMBER | Campo di sistema | Y |

## X4POLICYCLASSIFICATIONS: Data Model Classificazioni Policy

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obligatorietà |
| --- | --- | --- | --- | --- |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) | Classificazione | N |
| X4POLICYID | BIGINT | NUMBER | X4policy ID | N |
| X4POLICYCLASSIFICATIONSID | BIGINT | NUMBER | ID X4POLICYCLASSIFICATIONS | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4POLICYAGGREGATEELEG: Data Model Parametri di eleggibilità per le policy aggregate delle linee

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obligatorietà |
| --- | --- | --- | --- | --- |
| PARAMETRO | ALN(100) | VARCHAR2(100) | Parametro | N |
| TIPOLOGIAASSET | ALN(192) | VARCHAR2(192) | Tipologia asset | N |
| VALUE | BIGINT(19) | NUMBER | Valore del parametro della soglia di eleggibilità | N |
| X4POLICYAGGREGATEELEGID | BIGINT | NUMBER | ID X4POLICYAGGREGATEELEG | Y |

## X4POLICYAGGREGATEKP: Data Model Gestione degli S eleggibilità stazioni nell’applicazione delle policy

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| KP | INT | NUMBER | Kp | N |
| PARAMELEG | DECIMAL | NUMBER(16,2) | Parametro eleggibile | N |
| SELEG | DECIMAL | NUMBER(16,2) | Soglia eleggibile | N |
| TIPOLOGIAASSET | UPPER(192) | VARCHAR2(192) | Tipologia asset | N |
| X4POLICYAGGREGATEDKPID | BIGINT | NUMBER | ID X4POLICYAGGREGATEDKP | Y |

## X4TABIDONEITA: Data Model Idoneità Policy su Asset

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| TIPOLOGIAASSET | UPPER(192) | VARCHAR2(192) | Classificazione Asset | N |
| STATOIDONEO | ALN(50) | VARCHAR2(50) | Stato idoneo | N |
| X4TABIDONEITAID | BIGINT | NUMBER | ID X4TABIDONEITA | Y |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| X4MXL | YORN | NUMBER | Campo di riferimento per logiche DSS su inserimento massivo | Y |

## X4TABECCEZIONI: Data Model Eccezioni Idoneità Policy su Asset

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDATTIVITA | BIGINT | NUMBER | ID PdL | Y |
| IDATTIVITA_EXT | ALN(25) | VARCHAR2(25) | ID Descrittivo PdL | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | Y |
| TIPOLOGIAASSET | ALN(192) | VARCHAR2(192) | Classificazione asset | Y |
| STATOIDONEO | ALN(25) | VARCHAR2(25) | Stato di idoneItà | Y |
| X4TABECCEZIONIID | BIGINT | NUMBER | ID X4TABECCEZIONI | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| X4MXL | YORN | NUMBER | Campo di riferimento per logiche DSS su inserimento massivo | Y |

## X4TABTERZOLIVELLO: Data Model Terzo Livello gerarchia

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| TIPO | ALN(50) | VARCHAR2(50) | Tipo | N |
| LIV3TIPOLOGIAASSET | ALN(50) | VARCHAR2(50) | Classificazione asset terzo livello | N |
| LIV4TIPOLOGIAASSET | ALN(50) | VARCHAR2(50) | Classificazione asset quarto livello | N |
| X4TABTERZOLIVELLOID | BIGINT | NUMBER | ID X4TABTERZOLIVELLO | Y |
| X4MXL | YORN | NUMBER | Campo di riferimento per logiche DSS su inserimento massivo | Y |

## AHMETHODOLOGY: Data model metodi AH

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| METHODNAME | UPPER(50) | VARCHAR2(50) | Nome | Y |
| DESCRIPTION | ALN(200) | VARCHAR2(200) | Descrizione | N |
| USEWITH | UPPER(100) | VARCHAR2(100) | Oggetto(Asset o Location) | Y |
| BESTSCORE | DECIMAL(10) | NUMBER(10,2) | Valore migliore | N |
| WORSTSCORE | DECIMAL(10) | NUMBER(10,2) | Valore peggiore | N |
| ACTIVE | YORN(1) | NUMBER | Attivo? | Y |
| CONVERSIONFORMULANAME | UPPER(50) | VARCHAR2(50) | Nome formula di normalizzazione | N |
| CONVERSIONFORMULA | ALN(1000) | VARCHAR2(1000) | Formula di normalizzazione | N |
| AHMETHODOLOGYID | BIGINT(19) | NUMBER | ID AHMETHODOLOGY | Y |
| AUTOCONVERSION | YORN(1) | NUMBER | Conversione automatica? | N |
| METHODFORMULA | ALN(1000) | VARCHAR2(1000) | Formula principale che descrive il calcolo della salute dell’asset | N |
| CALCTYPE | UPPER(100) | VARCHAR2(100) | Tipo di calcolo | Y |
| SCOREDEPENDENCIES | ALN(500) |  | Dipendenze score | N |
| EXTFUNCTIONNAME | ALN(200) | VARCHAR2(200) |  | N |
| EXTMODELNAME | ALN(200) | VARCHAR2(200) |  | N |
| FACTORCARDINALITY | ALN(20) | VARCHAR2(20) | N fattori utilizzati dalla metodologia(N° indicatori per calcolo stato salute) | Y |
| ISATTRIBUTE | YORN | NUMBER | Flag per indicare se metodo è legato a singolo attributo | Y |
| METHODDISPLAYNAME | ALN(200) | VARCHAR2(200) | Nome descrittivo metodologia | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| SCORETYPE | UPPER(50) | VARCHAR2(50) | Tipo di score(Rischio salute etc etc) | N |

## AHSCORE: Data Model Gestione degli score degli asset

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ATTRIBUTENAME | UPPER(50) | VARCHAR2(50) | HI di riferimento | N |
| SITEID | UPPER(8) | VARCHAR2(8) | Site | N |
| OBJECTNAME | UPPER(30) | VARCHAR2(30) | Nome Oggetto(Asset o Location) | N |
| OWNERRECORDID | BIGINT(19) | NUMBER | ID univoco OWNERRECORD | N |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | N |
| VALUE | DECIMAL(12) | NUMBER(12,2) | Valore finale per calcolo asset | N |
| ORIGINALVALUE | INTEGER | NUMBER | Valore precedente alla misurazione/calcolo | N |
| PREVIOUSVALUE | DECIMAL(12) | NUMBER(12,2) | Valore precedente | N |
| CHANGEDATE | DATETIME(10) | DATE | Data cambio | N |
| CHANGEBY | UPPER(100) | VARCHAR2(100) | Cambiato da | N |
| LASTSUCCESSDATE | DATETIME(10) | DATE | Data ultimo calcolo | N |
| METHODNAME | UPPER(50) | VARCHAR2(50) | Nome metodo | N |
| AHSCOREID | BIGINT(19) | NUMBER | ID AHSCOREID | Y |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | Identificativo univoco dell’asset associato | N |
| ENTERDATE | DATETIME | DATE | Data inserimento punteggio calcolato | N |
| EXPVALINPUTTYPE | UPPER(10) | VARCHAR2(10) | Input atteso | N |
| FORMULA | ALN(1000) | VARCHAR2(1000) | Formula utilizzata dalla metodologia | N |
| FUTURESCOREBATCHID | BIGINT | NUMBER | Campo per calcoli previsionali futuri | N |
| LOCATION | UPPER(25) | VARCHAR2(25) | Location associata al punteggio | N |
| MEMO | ALN(1000) | VARCHAR2(1000) | Note aggiuntive | N |
| MSGID | ALN(100) | VARCHAR2(100) | ID messaggio associato al calcolo | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | N |
| SUCCESS | YORN | NUMBER | Flag se calcolo è andato a buon fine | Y |
| TODATE | DATETIME | DATE | Data del punteggio registrato | N |

## EXPGROUP: Data Model Scoring group

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| EXPGROUPNAME | UPPER(20) | VARCHAR2(20) | ID scouring group | Y |
| DESCRIPTION | ALN(200) | VARCHAR2(200) | Descrizione | N |
| USEWITH | UPPER(100) | VARCHAR2(100) | Oggetto(Asset o Location) | Y |
| INSTANCENAME | ALN(50) | VARCHAR2(50) | Nome crontask | N |
| GROUPLABEL | ALN(200) | VARCHAR2(200) | Label del group | Y |

## AHHISTORY: Data Model Storico AH

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| AHHISTORYID | BIGINT(19) | NUMBER | ID AHHISTORYID | Y |
| ATTRIBUTENAME | UPPER(50) | VARCHAR2(50) | ID Metodo | N |
| CHANGEBY | UPPER(100) | VARCHAR2(100) | Cambiato da | N |
| CHANGEDATE | DATETIME(10) | DATE | Data cambio | N |
| ENTERDATE | DATETIME(10) | DATE | Data primo inserimento | N |
| OBJECTNAME | UPPER(30) | VARCHAR2(30) | Oggetto(Asset o Location) | N |
| ORGID | UPPER(8) | VARCHAR2(8) | Organizzazione | N |
| OWNERRECORDID | BIGINT(19) | NUMBER | ID univoco dell’oggetto(asset) | N |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Site | N |
| TODATE | DATETIME(10) | DATE | Data calcolo | N |
| VALUE | DECIMAL(12) | NUMBER(12,2) | Valore calcolato | N |

## AHFACTORSCORE: Data Model Gestione degli score “factor” degli asset

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CURVALFORMULA | ALN(1000) | VARCHAR2(1000) | Formula | N |
| WEIGHT | DECIMAL(5) | NUMBER(5,2) | Peso | N |
| AHFACTORSCOREID | BIGINT | NUMBER | ID AHFACTORSCORE | Y |

## DATIREPORTELIVE: Data Model Dati Report

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ID | BIGINT | NUMBER | ID DATIREPORTLIVE | Y |
| NOME_FILE | ALN(100) | VARCHAR2(100) | Nome del file a cui appartengono i record | Y |
| ID_INT | DECIMAL | NUMBER | Progressivo numerico della campata | N |
| CAMPATA | ALN(100) | VARCHAR2(100) | SAP ID della campata | Y |
| DIST_INT | DECIMAL | NUMBER | Distanza in m dalla catenaria | Y |
| PROGRES | DECIMAL | NUMBER | Distanza in m dell’interferenza dal 1° sostegno di campata | N |
| PRIORITA | ALN(50) | VARCHAR2(50) | Gravità dell’interferenza | Y |
| TEMP_AMB | DECIMAL | NUMBER | Temperatura ambientale | N |
| C_EST_INT | DECIMAL | NUMBER | Coordinate Est | N |
| C_NORD_INT | DECIMAL | NUMBER | Coordinate Nord | N |
| WONUM | ALN(10) | VARCHAR2(10) | Numero identificativo dell’ordine di lavoro | Y |
| COD_GRIGLIA | ALN(40) | VARCHAR2(40) | Codice della macrocella di appartenenza del taglio | N |
| T_S | DECIMAL | NUMBER | Indica se il taglio è singolo (1 si, 0 no) | N |
| T_A | DECIMAL | NUMBER | Indica se il taglio è areale(1 si, 0 no) | N |
| EST_CEN | DECIMAL | NUMBER | Longitudine, nel sistema di riferimento della linea, del centroide dell’aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | N |
| NORD_CEN | DECIMAL | NUMBER | Latitudine, nel sistema di riferimento della linea, del centroide dell’aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | N |
| X_CEN | DECIMAL | NUMBER | Coordinata X, nel sistema di riferimento della linea, del centroide dell’aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | N |
| Y_CEN | DECIMAL | NUMBER | Coordinata Y, nel sistema di riferimento della linea, del centroide dell’aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | N |
| ANNO_COMP | DECIMAL | NUMBER | Anno di riferimento | N |
| PERIODO_COMP | ALN(40) | VARCHAR2(40) | Periodo di riferimento | N |
| IN_FASCIA | DECIMAL | NUMBER | Indica se il taglio è in fascia oppure no(1 si, 0 no), i tagli fuori fascia sono individuati con l’algoritmo del ribaltamento | N |
| CAT_VERT | DECIMAL | NUMBER | Indica se il taglio è individuato con riferimento alla catenaria verticale oppure no(1 si, 0 no) | N |
| CAT_SBAND | DECIMAL | NUMBER | Indica se il taglio è individuato con riferimento alla catenaria sbandata oppure no(1si, 0 no) | N |
| RIBALT | DECIMAL | NUMBER | Indica se il taglio è individuato con l’algoritmo del ribaltamento oppure no(1si, 0 no) | N |
| TIPOLOGIA | DECIMAL | NUMBER | Indica la tipologia nel report | Y |

## SCARTIREPORTELIVE: Data Model Scarti Report

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ID | BIGINT | NUMBER | ID SCARTIREPORTLIVE | Y |
| NOME_FILE | ALN(100) | VARCHAR2(100) | Nome del file a cui appartengono i record | Y |
| DESC_SCARTO | ALN(500) | VARCHAR2(500) | Motivo dello scarto della campata/wonum | Y |
| ID_INT | DECIMAL | NUMBER | Progressivo numerico della campata | N |
| CAMPATA | ALN(100) | VARCHAR2(100) | SAP ID della campata | Y |
| DIST_INT | DECIMAL | NUMBER | Distanza in m dalla campata | Y |
| PROGRES | DECIMAL | NUMBER | Distanza in m dell’interferenza dal 1° sostengo di campata | N |
| PRIORITA | ALN(50) | VARCHAR2(50) | Gravità dell’interferenza | Y |
| TEMP_AMB | DECIMAL | NUMBER | Temperatura ambientale | N |
| C_EST_INT | DECIMAL | NUMBER | Coordinate Est | N |
| C_NORD_INT | DECIMAL | NUMBER | Coordinate Nord | N |
| WONUM | ALN(10) | VARCHAR2(10) | Numero identificativo dell’ordine di lavoro | Y |
| COD_GRIGLIA | ALN(40) | VARCHAR2(40) | Codice della macro-cella di appartenenza del taglio | N |
| T_S | DECIMAL | NUMBER | Indica se il taglio è singolo(1 si, 0 no) | N |
| T_A | DECIMAL | NUMBER | Indica se il taglio è areae(1 si, 0 no) | N |
| EST_CEN | DECIMAL | NUMBER | Longitudine, nel sistema di riferimento della linea, del centroide dell’aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | N |
| NORD_CEN | DECIMAL | NUMBER | Latitudine, nel sistema di riferimento della linea, del centroide dell’aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | N |
| X_CEN | DECIMAL | NUMBER | Coordinata X, nel sistema di riferimento della linea, del centroide dell’aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | N |
| Y_CEN | DECIMAL | NUMBER | Coordinata Y, nel sistema di riferimento della linea, del centroide dell’aggregazione di celle che rappresenta il taglio se questo è singolo oppure del centroide della macro-cella se è un taglio areale | N |
| ANNO_COMP | DECIMAL | NUMBER | Anno di riferimento | N |
| PERIODO_COMP | ALN(40) | VARCHAR2(40) | Periodo di riferimento | N |
| IN_FASCIA | DECIMAL | NUMBER | Indica se il taglio è in fascia oppure no (1 si, 0 no). I tagli fuori fascia sono individuati con l’algoritmo del ribaltamento | N |
| CAT_VERT | DECIMAL | NUMBER | Indica se il taglio è individuato con riferimento alla catenaria verticale oppure no(1si , 0 no) | N |
| CAT_SBAND | DECIMAL | NUMBER | Indica se il taglio è individuato con riferimento alla catenaria sbandata oppure no(1si , 0 no) | N |
| RIBALT | DECIMAL | NUMBER | Indica se il taglio è individuato con l’algoritmo del ribaltamento oppure no (1 si, 0 no) | N |
| TIPOLOGIA | ALN(40) | VARCHAR2(40) | Tipologia del report. Una stringa che contiene numeri da 1 a 3 concatenati con questo dominio: 1)Catenaria verticale 2)Catenaria sbandata 3)Ribaltamento | Y |

## X4INDISPWO: Data Model Richieste di indisponibilità

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4CLUSTER | ALN(50) | VARCHAR2(50) | Cluster titolare | N |
| INIZIOINDISPONIBILITA | DATETIME | DATE | Data inizio indisponibilità | N |
| FINEINDISPONIBILITA | DATETIME | DATE | Data fine indisponibilità | N |
| STATUS | ALN(20) | VARCHAR2(20) | Stato richiesta | N |
| WONUM | UPPER(25) | VARCHAR2(25) | Ordine di lavoro relazionato | N |
| SITEID | UPPER(8) | VARCHAR2(8) | Sito dell’ordine di lavoro relazionato | N |
| ESITO | ALN(2) | VARCHAR2(2) | Esito | N |
| MOTIVOANN | ALN(150) | VARCHAR2(150) | Motivo dell’annullamento | N |
| IDERRORE | ALN(20) | VARCHAR2(20) | Codice errore | N |
| DESCERRORE | ALN(50) | VARCHAR2(20) | Descrizione errore | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Codice della delibera dell’indisponibilità | N |
| X4INDISPWOID | BIGINT | NUMBER | ID X4INDISPWO | Y |

## ASSETANCESTOR: Data Model Ancestor Asset

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ANCESTOR | UPPER(25) | VARCHAR2(25) | Ancestor | Y |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | Numero Asset | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | Y |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | Y |
| HIERARCHYLEVELS | INTEGER | NUMBER | Livello gerarchia | Y |
| ASSETANCESTORID | BIGINT | NUMBER | ID ASSETANCESTOR | Y |

## X4MPE: Data Model Gestione esecuzioni MPE , fa da fonte dati principale per la costruzione del payload JSON che viene inviato al MPE per effettuare l’elaborazione

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| INIZIOPEROTT | DATETIME | DATE | Inizio periodo da ottimizzare, inserito manualmente dall’utente | N |
| FINEPEROTT | DATETIME | DATE | Fine periodo da ottimizzare, inserito manualmente dall’utente | N |
| UDB | UPPER(10) | VARCHAR2(10) | Unità di Budget | N |
| X4IMPEID | BIGINT | NUMBER | ID univoco/chiave della tabella X4MPE | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione di esecuzione del motore | N |
| IDRUN | BIGINT | NUMBER | Chiave tecnica della run univoca | N |

## X4MPEODL: Data Model OdL che escono dal modello MPE a seguito della riorganizzazione algoritmica

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| WONUM | UPPER(25) | VARCHAR2(25) | ID OdL | N |
| SITEID | UPPER(8) | VARCHAR2(8) | SITE ID | N |
| X4MPEID | BIGINT | NUMBER | ID MPE | N |
| DATAINIZIOPROP | DATETIME | DATE | Data inizio proposta da MPE | N |
| DATAFINEPROPR | DATETIME | DATE | Data fine proposta da MPE | N |
| IDRUN | BIGINT | NUMBER | Chiave tecnica della run univoca | N |
| X4DATAULTIMAPIAN | DATETIME | DATE | Data di ultima pianificazione | N |
| X4PIANMPE | YORN | MPE | OdL pianificato dall’MPE precedentemente | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione OdL | N |
| TARGSTARTDATE | DATETIME | DATE | Data di inizio lavoro pianificata definita dall’utente | N |
| TARGCOMPDATE | DATETIME | DATE | Data di fine lavoro pianificata definita dall’utente | N |
| X4MPEODLID | BIGINT | NUMBER | ID X4MPEODL | Y |

## X4TARGETDIFFOR: Data Model Catalogo gravità difformità

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| VALUE | ALN(100) | VARCHAR2(100) | Target/Gravità | N |
| X4CATDIFFORID | BIGINT(19) | NUMBER | ID univoco/chiave della tabella X4CATDIFFOR | N |
| NINPUT | INT | NUMBER | Numero logica di generazione | N |
| X4TARGETDIFFORID | BIGINT | NUMBER | ID X4TARGETDIFFOR | Y |

## X4CARDIFFOR: Data Model Gestione caratteristiche asset inerenti alle difformità

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ATTRIBUTE | UPPER(50) | VARCHAR2(50) | Attributo di classificazione asset | N |
| VALUE | ALN(254) | VARCHAR2(254) | Valore | N |
| X4CATDIFFORID | BIGINT(19) | NUMBER | ID univoco/chiave della tabella X4CATDIFFOR | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |

## X4DIFFORCLASSIFICATIONS: Data Model Relazione tra Difformità e Classificazioni

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) | Classificazione asset | N |
| X4CATDIFFORID | BIGINT(19) | NUMBER | ID univoco/chiave della tabella X4CATDIFFOR | N |
| X4DIFFORCLASSIFICATIONSID | BIGINT | NUMBER | ID X4DIFFORCLASSIFICATIONS | Y |

## X4MISUREDIFFORMITA: Data Model Relazione tra Misure e Difformità

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4MISUREDIFFORMITAID | BIGINT | NUMBER | ID univoco/chiave della tabella X4MISUREDIFFORMITA | Y |
| IDMISURA | ALN(20) | VARCHAR2(20) | ID Misura rilevabile contestualmente all’apertura della difformità in opposizione di fase alla logica di generazione. Sarà usato quando su catalogo difformità | N |
| X4CATDIFFORID | BIGINT(19) | NUMBER | ID univoco/chiave della tabella X4CATDIFFOR | N |
| NINPUT | INTEGER | NUMBER | Numero logica di generazione | N |
| LOGICAGENERAZIONE | ALN(250) | VARCHAR2(250) | Logica di generazione | N |
| DESCRIPTION | ALN(200) | VARCHAR2(200) | Descrizione logica di calcolo | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4MISUREDIFFORPARAMETRI: Data Model Relazione tra parametri di input e formula a testo libero

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4MISUREDIFFORMITAID | BIGINT | NUMBER | ID univoco/chiave della tabella X4MISUREDIFFORMITA | N |
| TIPOPARAMETRO | ALN(20) | VARCHAR2(20) | Tipo parametro | N |
| PARAMETRO | ALN(20) | VARCHAR2(20) | Parametro | N |
| NINPUT | INTEGER | NUMBER | Numero logica di generazione | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4TARGETINTERF: Data Model Target/gravità inerenti alle interferenze

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| VALUE | ALN(50) | VARCHAR2(50) | Target/Gravità | N |
| NINPUT | INTEGER | NUMBER | Numero logica di generazione | N |
| X4CATINTERFID | BIGINT(19) | NUMBER | ID univoco/chiave della tabella X4CATINTERF | N |
| X4TARGETINTERFID | BIGINT | NUMBER | ID X4TARGETINTERF | Y |

## X4CARDINTERF: Data Model Caratteristiche asset inerenti alle interferenze

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ATTRIBUTE | UPPER(50) | VARCHAR2(50) | Attributo di classificazione asset | N |
| VALUE | ALN(254) | VARCHAR2(254) | Valore | Y |
| X4CATINTERFID | BIGINT(19) | NUMBER | ID univoco/chiave della tabella X4CATINTERF | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| X4CARDINTERFID | BIGINT | NUMBER | ID X4CARDINTERF | Y |

## X4INTERFATT: Data Model Attività che risolvono le interferenze

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ATTIVITA | ALN(15) | VARCHAR2(20) | ID Attività | N |
| INTERFERENZA | ALN(50) | VARCHAR2(50) | Interferenza | N |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | N |
| INTERFERENZAID | BIGINT | NUMBER | ID Interferenza | N |
| CHANGEDBY | UPPER(100) | VARCHAR2(100) | Utente ultima modifica | N |
| CHANGEDDATE | DATETIME | DATE | Data ultima modifica | N |
| X4INTERFATTID | BIGINT | NUMBER | ID X4INTERFATT | Y |

## X4INTERFCLASSIFICATIONS: Data Model Relazione tra Interferenze e Classificazioni

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) | Classificazione asset | N |
| X4CATINTERFID | BIGINT(19) | NUMBER | ID univoco/chiave della tabella X4CATINTERF | N |
| X4INTERFCLASSIFICATIONSID | BIGINT | NUMBER | ID X4INTERCLASSIFICATIONS | Y |

## X4MISUREINTERFERENZA: Data Model Relazione tra Misure e Interferenze

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDMISURA | ALN(20) | VARCHAR2(20) | ID Misura | N |
| NINPUT | INTEGER | NUMBER | Numero logica di generazione | N |
| LOGICAGENERAZIONE | ALN(250) | VARCHAR2(250) | Logica di generazione | N |
| X4CATINTERFID | BIGINT(19) | NUMBER | ID univoco/chiave della tabella X4CATINTERF | N |
| DESCRIPTION | ALN(200) | VARCHAR2(200) | Descrizione | N |
| X4MISUREINTERFERENZAID | BIGINT | NUMBER | ID X4MISUREINTERFERENZA | Y |

## X3EQTYPBZ: Data Model Gestione della tabella di mapping “Costruttore componente/ Tipo”

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ZHERST | ALN(30) | VARCHAR2(30) | Codice componente costruttore | N |
| ZTYPBZ | ALN(20) | VARCHAR2(20) | Descrizione modello componente | N |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) | Classificazione equipment | N |
| TIPO | ALN(3) | VARCHAR2(3) | Tipo | N |
| X3EQTYPBZID | BIGINT | NUMBER | ID X3EQTYPBZ | Y |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |

## X4CATASSOCIAZIONE: Data Model Catalogo Associazione Microcontrolli-Difformità/Interferenze

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDDIFFORMITA | ALN(20) |  | ID Difformità | N |
| IDORIGINE | ALN(20) |  | ID Origine | N |
| IDINTERFERENZA | ALN(20) |  | ID Interferenza | N |

## X4CARDMICRO: Data Model Gestione delle caratteristiche asset inerenti ai microcontrolli

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDMICROCONTROLLO | ALN(15) | NUMBER | ID Microcontrollo | N |
| ATTRIBUTE | UPPER(50) | VARCHAR2(50) | Attributo di classificazione asset | N |
| VALUE | ALN(254) | VARCHAR2(254) | Valore | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| X4CARDMICROID | BIGINT | NUMBER | ID X4CARDMICRO | Y |

## X4INTERFSTAT: Data Model Interferenze relazionate all’anagrafica

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDINTERFERENZA | BIGINT | NUMBER | ID Interferenza | N |
| GRAVITA | ALN(50) | VARCHAR2(50) | Gravità | N |
| STATUS | ALN(15) | VARCHAR2(15) | Stato | N |
| PRIMARILEVAZIONE | DATETIME | DATE | Data Prima rilevazione | N |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | Asset | N |
| NONCONFERMATO | YORN | NUMBER | Stato se confermato o meno | Y |
| DESCRIPTION | ALN(200) | VARCHAR2(200) | Descrizione | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| IDINTERFERENZA_EXT | ALN(50) | VARCHAR2(50) | ID Interferenza descrittivo | N |
| ODLRISOLUTORE | UPPER(25) | VARCHAR2(25) | OdL risolutore | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | N |
| ULTIMARILEVAZIONE | DATETIME | DATE | Data ultima rilevazione | N |
| CAUSALE | ALN(50) | VARCHAR2(50) | Causale di superamento | N |
| FASE | ALN(4) | VARCHAR2(4) | Fase dell’asset | N |
| ASSETNUM1LIV | UPPER(25) | VARCHAR2(25) | Asset di primo livello | N |
| CHANGEBY | UPPER(50) | VARCHAR2(50) | Utente modificatore | N |
| CHANGEDATE | DATETIME | DATE | Data ultima modifica | N |
| X4INTERFSTATID | BIGINT | NUMBER | ID X4INTERFSTAT | Y |
| X4NOTEMIG | ALN(250) | VARCHAR2(250) | Note | N |
| NUMERO_COLLISIONI | INTEGER | NUMBER | Numero collisioni | N |
| IDMISURA |  | VARCHAR2(30) |  | N |
| MATRICOLARILEVATORE |  | VARCHAR2(100) |  | N |
| MATRICOLARISOLUTORE |  | VARCHAR2(100) |  | N |
| MATRICOLASUPERATA |  | VARCHAR2(100) |  | N |
| VALMISURA |  | FLOAT |  | N |
| X4IDINTSORGMIG |  | VARCHAR2(100) |  | N |
| X4KEYMIG |  | NUMBER |  | N |
| X4TECMIG |  | NUMBER |  | Y |

## X4MISUREASSET: Data Model Misure relazionate all’anagrafica

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDMISURA | ALN(50) | VARCHAR2(50) | ID Misura | N |
| IDMISURA_EXT | ALN(20) | VARCHAR2(20) | ID descrittivo Misura | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione | N |
| VALORE | FLOAT(8) | FLOAT | Valore di rilevazione | N |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | ID descrittivo dell’asset | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| IDDIFFORMITA_EXT | ALN(30) | VARCHAR2(30) | ID descrittivo difformità | N |
| PRIMARILEVAZIONE | DATETIME | DATE | Data della prima rilevazione | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | Sito fisico dell’asset | N |
| ULTIMARILEVAZIONE | DATETIME | DATE | Data dell’ultima rilevazione | N |
| X4MISUREASSETID | BIGINT | NUMBER | ID X4MISUREASSET | Y |

## WORKLOG: Data Model Gestione segnalazioni dal campo

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DESCRIPTION_LONGDESCRIPTION | LONGALN(32000) | VARCHAR2(32000) | Nota segnalazione | N |
| DESCRIPTION | ALN(100) | VARCHAR2(100) | Descrizione | N |
| X4GESTITO | YORN |  | Stato della segnalazione | N |
| MODIFYBY | UPPER(100) | VARCHAR2(100) | Utente modificatore | Y |
| MODIFYDATE | DATETIME | DATE | Data ultima modifica | Y |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | N |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | N |

## X4TABPAROPINT: Data Model Gestione parametrica delle opere interferenti

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| TIPOLOGIA | ALN(50) | VARCHAR2(50) | Tipologia | N |
| SOTTOTIPOLOGIA | ALN(50) | VARCHAR2(50) | Sottotipologia | N |
| CRITICITA | INT | NUMBER | Criticità | N |
| SOGLIA | INT | NUMBER | Soglia | N |
| X4TABPAROPINTID | BIGINT | NUMBER | ID X4TABPAROPINT | Y |

## X4JOBPLANLASSIFICATIONS: Data Model Gestione legame tra PdL e classificazioni asset ammissibili

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) | Classificazione asset ammissibile | N |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | N |
| JPNUM | ALN(20) | VARCHAR2(20) | ID PdL | N |
| X4JOBPLANLASSIFICATIONSID | BIGINT | NUMBER | ID X4JOBPLANLASSIFICATIONS | Y |

## X4ATTIVITAASSOCIAZIONE: Data Model Associazione delle attività sulle macro-attività

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4ATTIVITAASSOCIAZIONEID | BIGINT | NUMBER | ID X4ATTASSOCIAZIONE | Y |
| MACROATTIVITA_EXT | ALN(16) | VARCHAR2(16) | ID descrittivo Macro-attività | N |
| ATTIVITA | INTEGER | NUMBER | ID attività Jobplan | N |
| ATTIVITA_EXT | ALN(18) | VARCHAR2(18) | ID descrittivo attività | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | NUMBER | Campo di sistema | Y |
| X4TIPOLOGIAASSET | ALN(192) | VARCHAR2(192) | Tipologia Asset | N |

## X4CONDIZIONEASSOCIAZIONE: Data Model Catalogo delle logiche utilizzate da X4ATTIVITAASSOCIAZIONE

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4CONDIZIONEASSOCIAZIONEID | BIGINT | NUMBER | ID X4CONDIZIONEASSOCIAZIONE | Y |
| X4ATTASSOCIAZIONEID | BIGINT | NUMBER | ID X4ATTASSOCIAZIONEID | N |
| TARGET | ALN(50) | VARCHAR2(50) | Target di raffronto | N |
| VALUE | ALN(50) | VARCHAR2(50) | Valore da raffrontare | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| ROWSTAMP |  | NUMBER | Campo di sistema | Y |

## MAXPROPVALUE: Data Model Parametri relativi alla quota percentuale di Capacity da assegnare a ogni priorità a livello mensile

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| PROPNAME | ALN(50) | VARCHAR2(50) | Nome Parametro | Y |
| PROPVALUE | ALN(512) | VARCHAR2(512) | Valore Parametro | N |
| MAXPROPVALUEID | BIGINT | NUMBER | ID MAXPROPVALUE | Y |

## X4_DSS_POLICY_RUNNER: Data Model Informazioni delle esecuzioni del DSS

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4_DSS_POLICY_RUNNER_ID | BIGINT | NUMBER | ID X4_DSS_POLICY_RUNNER | Y |
| X4POLICYID | BIGINT | NUMBER | ID X4POLICY | N |
| IDPOLICY | ALN(20) | VARCHAR2(20) | Nome della policy | N |
| DATA_ELABORAZIONE | DATETIME | DATE | Data elaborazione | N |
| IDMICROATTIVITA | ALN(16) | VARCHAR2(16) | Macro-attività | N |
| CORRELATION_ID | ALN(40) | VARCHAR2(40) | Correlation id di elaborazione. Generato dal DSS | N |
| DESCRIPTION | ALN(250) | VARCHAR(250) | Descrizione | N |

## X4_DSS_POLICY_ERROR: Data Model Errori di elaborazione in fase di innesco richiesta di policy permettendo di dare evidenza della mancata elaborazione di una policy

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DSS_POLICY_ERROR_ID | BIGINT | NUMBER | ID Errore di elaborazione | Y |
| POLICY_ID | BIGINT | NUMBER | Riferimento ID Policy | N |
| ERROR_TIMESTAMP | DATETIME | DATE | Data in cui si è verificato l’errore | N |
| ERROR_DESCRIPTION | ALN(4000) | VARCHAR2(4000) | Descrizione errore | N |

## X4_DSS_WORKED_HISTORY: Data Model Eventi di calcolo su un asset o gruppo di asset per l’istanza di calcolo policy richiesta

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ID_DSS_WORKED |  | NUMBER | ID Evento | Y |
| STATUS |  | VARCHAR2(20) | Stato elaborazione | N |
| POLICY_ID |  | VARCHAR2(20) | ID della policy che è stata verificata | N |
| ASSETNUM |  | VARCHAR2(25) | Assetnum su cui è stata effettuata la verifica della policy | N |
| ORDERNUM |  | VARCHAR2(25) | Potrebbe non essere generato ordine da cui nullable | N |
| DSS_PC_CORRELATION_ID |  | VARCHAR2(40) | UUID – Policy calculation correlation id è la chiave di aggregazione che permette di ricondurre 2 calcoli eseguiti su 2 o più worker alla stessa richiesta di calcolo policy effettuata su più asset | N |
| TOTAL_RELATED_MESSAGE |  | NUMBER | Il numero complessivo di messaggi inviati verso il podworker | N |
| PROGRESSIVE_RELATED_MESSAGE |  | NUMBER | Il numero progressivo del messaggio inviati verso il podworker | N |
| TRIGGED_ENTITY |  | VARCHAR2(20) | Usato per scopi futuri entità che ha scatenato il calcolo | N |
| CREATED_BY |  | VARCHAR2(20) | Pod che ha lanciato l’elaborazione | N |
| CREATED_TIMESTAMP |  | DATE | Timestamp di creazione | N |
| CHANGED_BY |  | VARCHAR2(20) | Pod che ha eseguito l’elaborazione | N |
| CHANGED_TIMESTAMP |  | DATE | Timestamp di modifica | N |
| CODE |  | VARCHAR2(20) | Codice errore presentato | N |
| DESCRIPTION |  | VARCHAR2(4000) | Descrizione errore presentazione | N |
| RECOVERY_COUNT |  | NUMBER | Indica il numero di riesecuzione | N |
| X4_DSS_POLICY_RUNNER_ID |  | NUMBER | ID X4_DSS_POLICY_RUNNER | N |
| ERROR_FLAG |  | VARCHAR2(2) | Esito elaborazione DSS | N |
| SHORT_DESCRIPTION |  | VARCHAR2(200) | Descrizione errore presentato al centro di controllo | N |
| TRONCONUM |  | VARCHAR2(25) | Numero del tronco | N |

## X4_DSS_ORDERS: Data Model Interscambio tra Worker e Manager, usato durante l’elaborazione del tronco d’ispezione. Contiene la lista degli ordini da creare o aggiornare a valle dell’esecuzione delle policy

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4_DSS_ORDERS_ID |  | NUMBER | ID X4_DSS_ORDERS_ID | Y |
| ID_DSS_WORKED |  | NUMBER | ID X4_DSS_WORKED_HISTORY | N |
| DSS_PC_CORRELATION_ID |  | VARCHAR2(40) | Chiave di correlazione con l’esecuzione della policy | Y |
| ASSETNUM |  | VARCHAR2(25) | Asset associato all’ordine da creare o aggiornare | N |
| JPNUM |  | VARCHAR2(20) | Attività associata all’ordine da creare o aggiornare | N |
| DATA_MODELLO |  | DATE | Data modello per l’ordine da creare o aggiornare | N |
| DATA_NON_PRIMA_DI |  | DATE | Data non prima di per l’ordine da creare o aggiornare | N |
| DATA_MAX_POSTICIPO |  | DATE | Data max posticipo per l’ordine da creare o aggiornare | N |
| DATA_PIANIF_INIZIO |  | DATE | Data pianifica inizio per l’ordine da creare o aggiornare | N |
| DATA_PIANIF_FINE |  | DATE | Data pianificata fine per l’ordine da creare o aggiornare | N |
| TO_CREATE |  | VARCHAR2(1) | Y o N e indica se l’ordine dev’essere creato o aggiornato | N |
| X4POLICYID |  | NUMBER | ID X4POLICY | N |
| X4POLICY |  | VARCHAR2 | Codice della policy elaborata | N |
| X4TRAINANE |  | VARCHAR2(10) | Può assumere valore TRAINATO o TRAINANTE | N |
| WONUM |  | VARCHAR2(25) | Wonum da creare | N |
| WOPARENT |  | VARCHAR2(25) | Wonum parent a cui legare l’ordine | N |
| TRONCONUM |  | VARCHAR2(25) | Numero del tronco | N |
| STATUS |  | VARCHAR2(16) | Stato dell’ordine da creare | N |
| X4NOTE |  | VARCHAR2(150) | Noe della policy usata per la prevalenza | N |
| X4WOPREVALENTE |  | VARCHAR2(25) | Wonum dell’ordine prevalente | N |
| SAPIDASSET |  | VARCHAR2(30) | SAPID del tronco | N |
| X4AGGREGATI |  | NUMBER | 1 oppure 0 sta ad indicare se è un ordine aggregatore | N |
| DATA_CREATION |  | DATE | Data creazione del record | N |
| TRATTA |  | VARCHAR2(4000) | Tratta di tesatura | N |

## X4_DSS_ORDERS_TRONCHI: Data Model Interscambio tra Worker e Manager, usato durante l’elaborazione del tronco d’ispezione. Contiene la lista tronchi associato ad una determinata campata o tratta cavo

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4_DSS_ORDERS_ID |  | NUMBER | ID composto della tabella | Y |
| X3TRONCHIID |  | NUMBER | ID Composto della tabella. Contiene ID della tabella X3TRONCHI | Y |
| DSS_PC_CORRELATION_ID |  | VARCHAR2(40) | Chiave di correlazione con l’esecuzione della policy | Y |
| DATA_CREATION |  | DATE | Data creazione del record | N |

## X3TRONCHI: Data Model Tronchi

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X3TRONCHIID |  | NUMBER | ID X3TRONCHI | Y |
| ASSETNUM |  | VARCHAR2(25) | Numero Asset | Y |
| SAPID |  | VARCHAR2(30) | ID SAP | N |
| SITEID |  | VARCHAR2(8) | ID Sito | N |
| TRONCONUM |  | VARCHAR2(25) | Numero Tronco | Y |
| ORGID |  | VARCHAR2(8) | ID Organizzazione | Y |
| LUNGHEZZATRONCO |  | VARCHAR2(25) | Lunghezza tronco | N |
| ZONATRONCO |  | VARCHAR2(30) | Zona tronco | N |

## X4_DSS_AGGREGATE: Data Model Interscambio tra Worker e Manager, usato durante l’elaborazione delle policy aggregate delle linee

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4_DSS_AGGREGATE_ID |  | NUMBER | ID X4_DSS_AGGREGATE_ID | Y |
| ID_DSS_WORKED |  | NUMBER | ID X4_DSS_WORKED_HISTORY | N |
| DSS_PC_CORRELATION_ID |  | VARCHAR2(40) | Chiave di correlazione con l’esecuzione della policy | Y |
| ASSET |  | VARCHAR2(25) | Asset elaborato | N |
| KELIGIBILITA |  | NUMBER | Kelegibilità associato all’asset calcolato | N |
| ASSETLV2 |  | VARCHAR2(25) | Asset di secondo livello in gerarchia | N |
| LINEA |  | VARCHAR2(25) | Linea | N |
| TRATTA |  | VARCHAR2(4000) | Tratta di tesatura associato all’ASSET | N |
| DATA_CREATION |  | DATE | Data creazione del record | N |

## MAXDOMAIN: Data Model Anagrafica Domini

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CHANGED | ALN(1) | VARCHAR2(1) | Flag per verifica modifica record | N |
| DESCRIPTION | ALN(109) | VARCHAR2(109) | Descrizione del dominio | N |
| DOMAINID | UPPER(18) | VARCHAR2(18) | ID Dominio | Y |
| DOMAINTYPE | UPPER(20) | VARCHAR2(20) | Tipo dominio in Maximo | Y |
| INTERNAL | INTEGER | NUMBER |  | Y |
| LENGHT | INTEGER | NUMBER | Lunghezza del DomainType | N |
| MAXDOMAINID | BIGINT | NUMBER | ID della tabella MAXDOMAIN | Y |
| MAXTYPE | UPPER(8) | VARCHAR2(8) | Tipo dato in Maximo del dominio | N |
| NEVERCACHE | YORN | NUMBER |  | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| SCALE | INTEGER | NUMBER |  | N |

## ALNDOMAIN: Data Model Valori dominio alfanumerico

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DOMAINID | UPPER(18) | VARCHAR2(18) | ID univoco del dominio | Y |
| VALUE | ALN(254) | VARCHAR2(254) | Valore del dominio | Y |
| DESCRIPTION | ALN(140) | VARCHAR2(140) | Descrizione | N |
| SITEID | UPPER(8) | VARCHAR2(8) | Sede dell’asset | N |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | N |
| ALNDOMAINID | BIGINT | NUMBER | ID ALNDOMAIN | Y |
| VALUEID | ALN(300) | VARCHAR2(300) | Concat DomainID\|Value | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## NUMERICDOMAIN: Data Model Valori dominio numerico

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DESCRIPTION | ALN(127) | VARCHAR2(127) | Descrizione del valore numerico | N |
| DOMAINID | UPPER(18) | VARCHAR2(18) | ID Dominio | Y |
| NUMERICDOMAINID | BIGINT | NUMBER | ID NUMERICODOMAIN | Y |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| SITEID | UPPER(8) | VARCHAR2(8) | Sede dell’asset | N |
| VALUE | DECIMAL | NUMBER(30,10) | Valore numerico del dominio | Y |
| VALUEID | ALN(300) | VARCHAR2(300) | Concat DOMAINID\|VALUE | Y |

## CLASSSPEC: Data Model Associazione Attributi e domini

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CLASSIFICATIONID |  | VARCHAR2(192) | Classificazione per ID del nodo | Y |
| CLASSSTRUCTUREID | UPPER(26) | VARCHAR2(26) | ID del nodo | Y |
| CLASSSPECID | BIGINT | NUMBER | ID CLASSSPEC | N |
| DESCRIPTION |  | VARCHAR2(150) | Descrizione | N |
| GENASSETDESC |  | VARCHAR2(150) | Descrizione dell’asset generata | N |
| HASCHILDREN | YORN | NUMBER | Flag per verificare che reord abbia record collegati in tabella | Y |
| HASLD |  | NUMBER | Campo di sistema | Y |
| LANGCODE |  | VARCHAR2(4) | Codice codifica Lingua | Y |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzatore | N |
| PARENT | UPPER(26) | VARCHAR2(26) | Correlato | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| SHOW | YORN | NUMBER | Flag per mostrare/non mostrare record | Y |
| SHOWINASSETTOPO | YORN | NUMBER | Flag per mostrare/non mostrare record in assetti | N |
| SITEID | UPPER(8) | VARCHAR2(8) | Sede dell’asset | N |
| SORTORDER |  | NUMBER | Ordine di sorting | N |
| TYPE |  | VARCHAR2(10) | Definisce il tipo dato del nodo | N |
| USECLASSICINDESC |  | NUMBER | Flag per mostrare descrizione della classificazione | Y |
| X3CLASSIDREL |  | VARCHAR2(30) | Classificazioni relazionate | N |
| X3GESTSTAT |  | NUMBER | Gestione degli stati | Y |
| X3MAXNUM |  | NUMBER | Massima Numerosità | N |
| X4ELEMENTO |  | VARCHAR2(20) | Cluster elemento | N |

## X4_MICROCONTROLLI_MIG_BATCH: Data Model Identificativi Ordini interessati dai micro-controlli

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ID | BIGINT | NUMBER | ID X4_MICROCONTROLLI_MIG_BATCH | Y |
| WONUM | UPPER(25) | VARCHAR2(25) | ID OdL | Y |
| ASSETNUM | UPPER(25) | VARCHAR2(25) | ID Asset | Y |
| JPNUM | ALN(20) | VARCHAR2(20) | ID Attività | Y |
| STATUS | UPPER(16) | VARCHAR2(16) | Lo stato può assumere il valore tra [READY,RUNNING,DONE,ERROR] | Y |
| CREATED_BY | ALN(20) | VARCHAR2(20) | Autore | N |
| CREATED_TIMESTAMP | DATETIME | DATE | Data creazione | Y |
| CHANGED_BY | ALN(20) | VARCHAR2(20) | Utente modifica | N |
| CHANGED_TIMESTAMP | DATETIME | DATE | Data modifica | N |
| MESSAGE_CODE | ALN(20) | VARCHAR2(20) | Codice restituito dall’elaborazione | N |
| MESSAGE_TEXT | ALN(4000) | VARCHAR2(4000) | Messaggio restituito dall’elaborazione | N |

## X4MPEINPUT: Data Model OdL nel perimetro di pianificazione per ogni richiesta

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4MPEINPUTID | BIGINT | NUMBER | ID X4MPEINPUT | Y |
| X4MPEID | BIGINT | NUMBER | ID X4MPE | N |
| IDRUN | BIGINT | NUMBER | Chiave tecnica della run univoca | N |
| WONUM | UPPER(25) | VARCHAR2(25) | ID OdL | N |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) | Descrizione OdL | N |

## X4MPE_STATI: Data Model Stato in cui si trovano le richieste MPE

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4MPEID | BIGINT | NUMBER | Chiave richiesta, id sequence incrementale | Y |
| XMPE_STATIID | BIGINT | NUMBER | ID XMP3_STATI | N |
| IDRUN | BIGINT | NUMBER | Chiave tecnica della run univoca | N |
| ID_CELERY | ALN(36) | VARCHAR2(36) | UUID4 identificativo del task, autogenerato da Celery in caso di corretto incodamento del task | N |
| STATO | ALN(155) | VARCHAR2(155) | Stato del task ( Un valore fra ‘IN CODA’, ‘IN ELABORAZIONE’, ELABORATA CON SUCCESSO’, ‘ELABORAZIONE FALLITA’, ‘INVIO NON PREVISTO’, ‘APPROVATA’, ‘SCARTATA’) | N |
| USERID | ALN(155) | VARCHAR2(155) | Utente | N |
| TIMESTAMP_INSERIMENTO | DATETIME | DATE | Timestamp della richiesta | N |
| TIMESTAMP_MODIFICA | DATETIME | DATE | Timestamp della modifica | N |
| ACTUAL | YORN | NUMBER | Flag Y/N ad indicare quale sia l’ultima versione per X4MPEID | Y |
| JSON_RICHIESTA | CLOB | CLOB | JSON contenente i filtri applicati dall’utente per la ricerca | N |
| UDB | ALN(155) | VARCHAR2(155) | Unità di Budget | N |
| DESCRIZIONE_ERRORE | ALN(155) | VARCHAR2(155) | Descrizione dell’errore occorso in caso di ELABORAZIONE FALLITA | N |
| TIMESTAMP_PARTENZA_TASK | DATETIME | DATE | Timestamp della partenza effettiva del task | N |
| TIMESTAMP_FINE_TASK | DATETIME | DATE | Timestamp della fine effettiva del task | N |
| METRICHE_PIANF | CLOB | CLOB | JSON contenente le metriche di pianificazione | N |

## A_WPLABOR: Data Model storico WPLABOR

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| AMCREW | UPPER(8) | VARCHAR2(8) |  | N |
| AMCREWTYPE | UPPER(8) | VARCHAR2(8) |  | N |
| APPTREQUIRED | YORN | NUMBER |  | N |
| CONTRACTNUM | UPPER(8) | VARCHAR2(8) |  | N |
| CRAFT | UPPER(8) | VARCHAR2(8) | Qualifica | N |
| CREWWORKGROUP | UPPER(25) | VARCHAR2(10) |  | N |
| JOBLABORID | BIGINT | NUMBER | ID JOBLABOR | N |
| LABORCODE | UPPER(8) | VARCHAR2(8) | Codice Labor | N |
| LABORHRS | DURATION | FLOAT | Durata esecuzione | N |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | N |
| QUANTITY | INTEGER | NUMBER | Quantità | N |
| RATE | AMOUNT | NUMBER(10,2) | Tariffa oraria | N |
| RATEHASCHANGED | YORN | NUMBER |  | N |
| SITEID | UPPER(8) | VARCHAR2(8) | ID Sito | N |
| SKILLLEVEL | UPPER(15) | VARCHAR2(15) |  | N |
| VENDOR | UPPER(12) | VARCHAR2(12) |  | N |
| WONUM | UPPER(25) | VARCHAR2(25) |  | N |
| WPL1 | ALN(10) | VARCHAR2(10) |  | N |
| WPL2 | DATETIME | DATE |  | N |
| WPL3 | ALN(10) | VARCHAR2(10) |  | N |
| WPL4 | ALN(10) | VARCHAR2(10) |  | N |
| WPL5 | ALN(10) | VARCHAR2(10) |  | N |
| WPL6 | DECIMAL | NUMBER(15,2) |  | N |
| WPL7 | ALN(10) | VARCHAR2(10) |  | N |
| WPL8 | ALN(10) | VARCHAR2(10) |  | N |
| WPL9 | ALN(10) | VARCHAR2(10) |  | N |
| WPLABORID | ALN(20) | VARCHAR2(20) | ID WPLABOR | N |
| WPLABORUID | BIGINT | NUMBER | UID WPLABOR | N |
| X3DATAFINERISORSA | DATETIME | DATE | Data fine risorsa | N |
| X3DATAINIZIORISORSA | DATETIME | DATE | Data inizio risorsa | N |
| X3OREESEC | DURATION | FLOAT | Ore esecuzione | N |
| X3OREPOSTESEC | DURATION | FLOAT |  | N |
| X3OREPREESE | DURATION | FLOAT |  | N |
| X3ORETRASF | DURATION | FLOAT | Ore trasferimento | N |
| X4CHANGEDATE | DATETIME | DATE |  | N |
| X4DATAFINERISORSA | DATETIME | DATE | Data fine risorsa | N |
| X4DATAINIZIORISORSA | DATETIME | DATE | Data inizio risorsa | N |
| X4OREESEC | DURATION | FLOAT | Ore esecuzione | N |
| X4OREPOSTESEC | DURATION | FLOAT |  | N |
| X4OREPREESE | DURATION | FLOAT |  | N |
| X4ORETRASF | DURATION | FLOAT | Ore trasferimento | N |
| X4TITRIS | UPPER(20) | VARCHAR2(20) | Titris della risorsa | N |
| EAUDITUSERNAME | UPPER(100) | VARCHAR2(100) |  | Y |
| EAUDITTIMESTAMP | DATETIME | DATE |  | Y |
| EAUDITTYPE | UPPER(1) | VARCHAR2(1) |  | Y |
| EAUDITTRANSID | ALN(40) | VARCHAR2(40) |  | Y |
| ESIGTRANSID | ALN(40) | VARCHAR2(40) |  | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| X4KEYMIG |  | NUMBER |  | N |
| X4TECMIG |  | NUMBER |  | Y |

## A_WPSERVICE: Data model storico WPSERVICE

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| AMCREW |  | VARCHAR2(8) |  | N |
| AMCREWTYPE |  | VARCHAR2(8) |  | N |
| CATALOGCODE |  | VARCHAR2(30) |  | N |
| CONDITIONCODE |  | VARCHAR2(30) |  | N |
| DESCRIPTION |  | VARCHAR2(40) | Descrizione | N |
| DIRECTREQ |  | NUMBER |  | N |
| HASLD |  | NUMBER | Campo di sistema | N |
| HOURS |  | FLOAT |  | N |
| ISSUETO |  | VARCHAR2(8) |  | N |
| ITEMNUM |  | VARCHAR2(30) | Codice risorsa esterna/ricavo | N |
| ITEMQTY |  | NUMBER(15,2) |  | N |
| ITEMSETID |  | VARCHAR2(8) | ID ITEMSET | N |
| JOBITEMID |  | NUMBER | IT JOBITEM | N |
| LANGCODE |  | VARCHAR2(4) |  | N |
| LINECOST |  | NUMBER(10,2) | Costo linea | N |
| LINETYPE |  | VARCHAR2(15) | Tipo linea | N |
| LOCATION |  | VARCHAR2(25) |  | N |
| MANUFACTURER |  | VARCHAR2(12) |  | N |
| MKTPLCITEM |  | NUMBER |  | N |
| MODELNUM |  | VARCHAR2(8) |  | N |
| ORDERUNIT |  | VARCHAR2(16) |  | N |
| ORGID |  | VARCHAR2(8) | ID Organizzazione | N |
| PR |  | VARCHAR2(8) |  | N |
| PRLINENUM |  | NUMBER |  | N |
| RATE |  | NUMBER(10,2) |  | N |
| RATEHASCHANGED |  | NUMBER |  | N |
| REQUESTBY |  | VARCHAR2(10) |  | N |
| REQUESTNUM |  | VARCHAR2(20) |  | N |
| REQUIREDATE |  | DATE |  | N |
| RESTYPE |  | VARCHAR2(16) |  | N |
| SITEID |  | VARCHAR2(8) | ID Sito | N |
| STORELOCSITE |  | VARCHAR2(8) |  | N |
| UNITCOST |  | NUMBER(10,2) |  | N |
| UNITCOSTHASCHANGED |  | NUMBER |  | N |
| VENDOR |  | VARCHAR2(12) | Venditore | N |
| VENDORPACKCODE |  | VARCHAR2(12) |  | N |
| VENDORPACKQUANTITY |  | VARCHAR2(12) |  | N |
| VENDORUNITCOST |  | NUMBER(10,2) |  | N |
| VENDORWAREHOUSE |  | VARCHAR2(12) |  | N |
| WONUM |  | VARCHAR2(25) |  | N |
| WPITEMID |  | NUMBER | ID WPITEM | N |
| WPLABORREF |  | NUMBER |  | N |
| WPM1 |  | VARCHAR2(10) |  | N |
| WPM2 |  | VARCHAR2(10) |  | N |
| WPM3 |  | NUMBER(15,2) |  | N |
| WPM4 |  | VARCHAR2(10) |  | N |
| WPM5 |  | NUMBER(10,2) |  | N |
| WPM6 |  | VARCHAR2(10) |  | N |
| X4DATAFINERISORSA |  | DATE | Data fine | N |
| X4DATAINIZIORISORSA |  | DATE | Data inizio | N |
| EAUDITUSERNAME |  | VARCHAR2(100) |  | Y |
| EAUDITTIMESTAMP |  | DATE |  | Y |
| EAUDITTYPE |  | VARCHAR2(1) |  | Y |
| EAUDITTRANSID |  | VARCHAR2(40) | ID EAUDITTRANS | Y |
| ESIGTRANSID |  | VARCHAR2(40) | ID ESIGTRANS | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## A_ASSIGNMENT: Data model storico ASSIGNMENT

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| AMCREW | UPPER(8) | VARCHAR2(8) |  | N |
| AMCREWTYPE | UPPER(8) | VARCHAR2(8) |  | N |
| APPOINTMENT | YORN | NUMBER |  | N |
| APPTREQUIRED | YORN | NUMBER |  | N |
| ASSIGNMENTID | BIGINT | NUMBER | ID ASSIGNMENT | N |
| CHANGEBY | UPPER(100) | VARCHAR2(100) | Autore ultima modifica | N |
| CHANGEDATE | DATETIME | DATE | Data ultima modifica | N |
| CONTRACTNUM | UPPER(8) | VARCHAR2(8) |  | N |
| CRAFT | UPPER(8) | VARCHAR2(8) |  | N |
| CREWWORKGROUP | UPPER(20) | VARCHAR2(20) |  | N |
| FINISHDATE | DATETIME | DATE |  | N |
| LABORCODE | UPPER(8) | VARCHAR2(8) | Matricola | N |
| LABORHRS | DURATION | FLOAT |  | N |
| ORGID | UPPER(8) | VARCHAR2(8) | ID Organizzazione | N |
| PARENTASSIGNID | BIGINT | NUMBER | ID PARENTASSIGN | N |
| RESOURCELOCK | YORN | NUMBER |  | N |
| SCHEDULEDATE | DATETIME | DATE |  | N |
| SITEID | UPPER(8) | VARCHAR2(8) |  | N |
| SKILLLEVEL | UPPER(15) | VARCHAR2(15) |  | N |
| STARTDATE | DATETIME | DATE |  | N |
| STATUS | UPPER(12) | VARCHAR2(12) | Stato dell’assignment | N |
| VENDOR | UPPER(12) | VARCHAR2(12) |  | N |
| WONUM | UPPER(25) | VARCHAR2(25) | ID OdL | N |
| WORKDATE | DATETIME | DATE |  | N |
| WORKLOG | YORN | NUMBER |  | N |
| WPLABORID | ALN(20) | VARCHAR2(20) | ID WPLABOR | N |
| EAUDITUSERNAME | UPPER(100) | VARCHAR2(100) |  | Y |
| EAUDITTIMESTAMP | DATETIME | DATE |  | Y |
| EAUDITTYPE | UPPER(1) | VARCHAR2(1) |  | Y |
| EAUDITTRANSID | ALN(40) | VARCHAR2(40) | ID EAUDITTRANS | Y |
| ESIGTRANSID | ALN(40) | VARCHAR2(40) | ID ESIGTRANS | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| X4TECMIG |  | NUMBER |  | Y |
| X4KEYMIG |  | NUMBER |  | N |
| X4PREPOSTO |  | NUMBER |  | N |

## X4_DP_REPORTING_TRACE: Data model Tracing refresh viste materializzate per la reportistica fuori MAS

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DP_CORRELATION_ID |  | VARCHAR2(50) | chiave identificativa univoca del refresh | Y |
| EXECUTION_STARTDATE |  | DATE | Data e ora di inizio esecuzione del refresh | Y |
| EXECUTION_ENDDATE |  | DATE | Data e ora di fine esecuzione del refresh | N |
| DATE_REFRESH |  | DATE | Data del refresh | Y |
| STATUS |  | VARCHAR2(150) | Stato del refresh | N |

## X4_SAPFILTER

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ID |  | NUMBER |  | Y |
| FILTRO |  | VARCHAR2(10) |  | Y |
| TIPO_SPESA |  | VARCHAR2(10) |  | N |
| TIPO_ORDINE |  | VARCHAR2(10) |  | N |
| TIPO_ATTIVITA |  | VARCHAR2(10) |  | N |
| STATO |  | VARCHAR2(10) |  | N |
| LIVELLO |  | NUMBER |  | N |

## X4_DSS_AGG_POLICY_CRON

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4_DSS_AGG_POLICY_CRONID |  | NUMBER |  | Y |
| STATUS |  | VARCHAR2(100) |  | Y |
| ELABORATION_DATE |  | DATE |  | Y |

## X4_DSS_EAM_DICTIONARY

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DSS_KEY |  | VARCHAR2(512) |  | Y |
| EAM_VAL |  | VARCHAR2(4000) |  | Y |

## X4_STATI_SAP

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| STATO |  | VARCHAR2(50) |  | Y |
| CODICE |  | VARCHAR2(50) |  | Y |
| DESCRIZIONE |  | VARCHAR2(4000) |  | N |

## X4BEARERTOKEN

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4BEARERTOKENID |  | NUMBER |  | Y |
| URI |  | VARCHAR2(500) |  | Y |
| CLIENT_NAME |  | VARCHAR2(20) |  | Y |

## X4CATMODELLOCLASSIFICATIONS

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) |  | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) |  | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| X4CATMODELLOCLASSIFICATIONSID | BIGINT | NUMBER |  | Y |
| X4CATMODELLOID | BIGINT | NUMBER |  | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4COSTI_TAGETIK

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| X4COSTITAGETIKID | BIGINT | NUMBER |  | Y |
| ATTIVITA | ALN(20) | VARCHAR2(20) |  | N |
| CDC_RESP_TGK | ALN(15) | VARCHAR2(15) |  | N |
| CLIENTE | ALN(10) | VARCHAR2(10) |  | N |
| CONTO_CE_TGK | ALN(15) | VARCHAR2(15) |  | N |
| ENTITY | ALN(15) | VARCHAR2(15) |  | N |
| ESERCIZIO | ALN(4) | VARCHAR2(4) |  | N |
| GRUPPO_MERCI | ALN(20) | VARCHAR2(20) |  | N |
| IMPIANTO | ALN(20) | VARCHAR2(20) |  | N |
| IMPORTO | DECIMAL | NUMBER(16,2) |  | N |
| INDICATORE_STAT | ALN(20) | VARCHAR2(20) |  | N |
| MM_NUM_ORDINE | ALN(10) | VARCHAR2(10) |  | N |
| NETWORK | ALN(20) | VARCHAR2(20) |  | N |
| OGGETTO_CONTAB | ALN(25) | VARCHAR2(25) |  | N |
| OGGETTO_CONTAB_MIT | ALN(25) | VARCHAR2(25) |  | N |
| OPERAZIONE_NET | ALN(4) | VARCHAR2(4) |  | N |
| ORD_TYPE | ALN(10) | VARCHAR2(10) |  | N |
| PERIODO | ALN(10) | VARCHAR2(10) |  | N |
| QUANTITA | DECIMAL | NUMBER(16,2) |  | N |
| RISORSA_INT_EST | ALN(20) | VARCHAR2(20) |  | N |
| SCENARIO_BW | ALN(15) | VARCHAR2(15) |  | N |
| SOCIETA_RICH | ALN(10) | VARCHAR2(10) |  | N |
| TIPO_IMPIANTO | ALN(10) | VARCHAR2(10) |  | N |
| TIPO_OGG_CONTAB | ALN(15) | VARCHAR2(15) |  | N |
| VDC_BW | ALN(20) | VARCHAR2(20) |  | N |
| CDC_RESP_ORG | ALN(30) | VARCHAR2(30) |  | N |
| CO_NUM_DOC | ALN(30) | VARCHAR2(30) |  | N |
| TITOLARE_RIS | ALN(30) | VARCHAR2(30) |  | N |
| UNIT | ALN(3) | VARCHAR2(3) |  | N |
| CREATE_TIMESTAMP |  | TIMESTAMP |  | N |
| UPDATE_TIMESTAMP |  | TIMESTAMP |  | N |

## X4DIFFERIBILITA

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DIFFERIBILITA | ALN(3) | VARCHAR2(3) |  | N |
| WONUM | UPPER(25) | VARCHAR2(25) |  | N |
| X4DIFFERIBILITAID | BIGINT | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di stato | Y |

## X4DUMPANAGRAFICA

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| TIPO |  | VARCHAR2(20) |  | N |
| STATO |  | VARCHAR2(20) |  | N |
| LIVELLO |  | VARCHAR2(1) |  | N |
| CODICE |  | VARCHAR2(30) |  | N |
| CODICE_PADRE |  | VARCHAR2(30) |  | N |
| CODICE_RADICE |  | VARCHAR2(30) |  | N |
| DESCRIZIONE |  | VARCHAR2(40) |  | N |
| AREA_LAV |  | VARCHAR2(20) |  | N |
| UNITA_LAV |  | VARCHAR2(20) |  | N |
| LAT1 |  | VARCHAR2(200) |  | N |
| LONG1 |  | VARCHAR2(200) |  | N |
| LAT2 |  | VARCHA2(200) |  | N |
| LONG2 |  | VARCHAR2(200) |  | N |
| TRIF |  | VARCHAR2(20) |  | N |
| TRASMISS |  | VARCHAR2(20) |  | N |
| ERRORE |  | VARCHAR2(200) |  | N |

## X4HICALCSTAGING

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ASSETNUM | UPPER(25) | VARCHAR2(25) |  | N |
| CHUNK_ID | INTEGER | NUMBER |  | N |
| CHUNK_ID_RECUPERO | INTEGER | NUMBER |  | N |
| CREATEDATE | DATETIME | DATE |  | N |
| ELAPSED | INTEGER | NUMBER |  | N |
| ELAPSEDALN | ALN(15) | VARCHAR2(15) |  | N |
| HICALCID | ALN(20) | VARCHAR2(20) |  | N |
| LASTUPDATE | DATETIME | DATE |  | N |
| RESULT | ALN(50) | VARCHAR2(50) |  | N |
| RESULT_DESCRIPTION | ALN(200) | VARCHAR2(200) |  | N |
| SITEID | UPPER(8) | VARCHAR2(8) |  | N |
| STEP_TIMEOUT_FLAG | UPPER(1) | VARCHAR2(1) |  | N |
| WORKFLOW_RUN_ID | INTEGER | NUMBER |  | N |
| X4HICALSTAGINGID | BIGINT | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| LOADED | INTEGER | NUMBER |  | N |

## X4HICLASSIFICATIONS

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) |  | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) |  | N |
| SCORETYPE | UPPER(50) | VARCHAR2(50) |  | N |
| X4HICLASSIFICATIONSID | BIGINT | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4MICROCLASSIFICATIONS

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) |  | N |
| DESCRIPTION | ALN(50) | VARCHAR2(50) |  | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| X4MICROCLASSIFICATIONSID | BIGINT | NUMBER |  | Y |
| X4MICROCONTROLLIID | BIGINT | NUMBER |  | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4MISUREANOMALIE

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| IDMISURA | BIGINT | NUMBER |  | N |
| IDUNIVOCO | BIGINT | NUMBER |  | N |
| X4MISUREANOMALIEID | BIGINT | NUMBER |  | Y |
| TIPOLOGIA | ALN(20) | VARCHAR2(20) |  | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4MISUREINTERFPARAMETRI

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DESCRIPTION | ALN(50) | VARCHAR2(50) |  | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| NINPUT | INTEGER | NUMBER |  | N |
| PARAMETRO | ALN(20) | VARCHAR2(20) |  | N |
| TIPOPARAMETRO | ALN(20) | VARCHAR2(20) |  | N |
| X4MISUREINTERFERENZAID | BIGINT | NUMBER |  | N |
| X4MISUREINTERFPARAMETRIID | BIGINT | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4MPEINPUTVINCOLI

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DESCRIPTION | ALN(50) | VARCHAR2(50) |  | N |
| FINEPEROTT | DATETIME | DATE |  | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| INIZIOPEROTT | DATETIME | DATE |  | N |
| PERCENTUALE_2 | FLOAT | FLOAT |  | N |
| PERCENTUALE_3 | FLOAT | FLOAT |  | N |
| X4MPEID | BIGINT | NUMBER |  | N |
| X4MPEINPUTVINCOLIID | BIGINT | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4ODLDIFFERIBILITAEXT

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| EAM_WO | UPPER(25) | VARCHAR2(25) |  | N |
| MR_RESULT | UPPER(2) | VARCHAR2(2) |  | N |
| UNITA_DI_MISURA | UPPER(2) | VARCHAR2(2) |  | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| X4ODLDIFFERIBILITAEXTID | BIGINT | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| VALUE | INTEGER | NUMBER | Valore | N |

## X4PESO

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DESCRIPTION | ALN(50) | VARCHAR2(50) |  | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| PESO | INTEGER | NUMBER |  | N |
| SOGLIA | INTEGER | NUMBER |  | N |
| X4PESOID | BIGINT | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4RICAVIODL

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CODICE | UPPER(8) | VARCHAR2(8) |  | N |
| DATAFINE | DATETIME | DATE |  | N |
| DATAINIZIO | DATETIME | DATE |  | N |
| DESCRIPTION | ALN(40) | VARCHAR2(40) |  | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| IMPORTO | AMOUNT | NUMBER(10,2) |  | N |
| SITEID | UPPER(8) | VARCHAR2(8) |  | N |
| WONUM | UPPER(10) | VARCHAR2(10) |  | N |
| X4RICAVIODLID | BIGINT | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4RILEVAZIONIODL

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ASSETDESC | ALN(40) | VARCHAR2(40) |  | N |
| ASSETNUM | UPPER(25) | VARCHAR2(25) |  | N |
| CLASSIFICATIONID | UPPER(192) | VARCHAR2(192) |  | N |
| DESCRIPTION | ALN(250) | VARCHAR2(250) |  | N |
| ID | ALN(30) | VARCHAR2(30) |  | N |
| TIPO | ALN(5) | VARCHAR2(5) |  | N |
| WONUM | UPPER(25) | VARCHAR2(25) |  | N |
| WORKORDERID | BIGINT | NUMBER |  | N |
| X4RILEVAZIONIODLID | BIGINT | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |
| RIFERIMENTO | UPPER(10) | VARCHAR2(10) |  | N |

## X4SCENARI

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| DESCRIPTION | ALN(50) | VARCHAR2(50) |  | N |
| IDSCENARIO | ALN(20) | VARCHAR2(20) |  | N |
| X4SCENARIOID | BIGINT | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4STALLIWO

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| SAPID | UPPER(30) | VARCHAR2(30) |  | N |
| SITEID | UPPER(8) | VARCHAR2(8) |  | N |
| WONUM | UPPER(25) | VARCHAR2(25) |  | N |
| X4STALLIWOID | BIGINT | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4VARIABILIERRORE

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| ASSETNUM |  | VARCHAR2(25) |  | N |
| CREATION_DATE |  | DATE |  | N |
| ERROR |  | VARCHAR2(400) |  | N |
| ERRORCODE |  | VARCHAR2(10) |  | N |
| HASLD |  | NUMBER | Campo di sistema | Y |
| HIST |  | NUMBER |  | Y |
| LAST_RETRY_DATE |  | DATE |  | N |
| ORGID |  | VARCHAR2(8) |  | N |
| RETRY |  | NUMBER |  | N |
| SITEID |  | VARCHAR2(8) |  | N |
| X4CATMISUREID |  | NUMBER |  | N |
| X4VARIABILIERROREID |  | NUMBER |  | Y |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

## X4VARIABILIERRORECAT

| Campo | Tipo MAXIMO | Tipo SQL | Descrizione | Obbligatorietà |
| --- | --- | --- | --- | --- |
| CALCOLO |  | VARCHAR2(150) |  | N |
| ERRORCODE |  | VARCHAR2(10) |  | Y |
| ERRORMESSAGE |  | VARCHAR2(400) |  | N |
| MAX_RETRY |  | NUMBER |  | N |
| X4VARIABILIERRORECATID |  | NUMBER |  | N |
| ROWSTAMP |  | VARCHAR2(40) | Campo di sistema | Y |

# MODELLO E/R

In allegato il modello E/R (in formato svg) dei data model descritti in questa Analisi tecnica:
