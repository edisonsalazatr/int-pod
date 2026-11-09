# EAM_Analisi Specifiche Tecniche_ReportisticaFuoriMAS_V2.0

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Reportistica Fuori Mas |

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
| Riesaminato da: | Nome Cognome | gg/mm/aaaa |  |
| Approvato da: | Nome Cognome | gg/mm/aaaa |  |

# Glossario

| Termine | Definizione |
| --- | --- |
| Piano di Lavoro (PdL) | Oggetto Maximo utilizzato per modellare l’attività del catalogo |
| Ordine di Lavoro (OdL) | Oggetto Maximo utilizzato per modellare le richieste di lavoro da eseguire |
| Configurazione | Implementazione Standard sull’applicativo IBM Maximo |
| Sviluppo | Implementazione Custom sull’applicativo IBM Maximo |
| Configurazione Database | Applicazione che consente di creare, modificare e gestire tabelle, attributi, indici e relazioni all'interno del database di Maximo. Qui è possibile definire la struttura del database e configurare i dati in modo da adattarsi alle specifiche esigenze dell'organizzazione |
| Application Designer | Applicazione che consente di creare e personalizzare l'interfaccia grafica delle applicazioni presenti in Maximo. Gli utenti possono aggiungere tabelle, sezioni e campi per adattare l'aspetto e il comportamento delle applicazioni in base alle specifiche esigenze aziendali |
| Conditional Expression Manager | Applicazione che permette di creare e modificare espressioni in linguaggio SQL utili per gestire le regole di visibilità e/o editabilità di determinati oggetti all'interno di Maximo |
| Escalation | Le escalation in IBM Maximo vengono utilizzate per automatizzare attività e monitorare record sensibili al tempo. Quest’ultime possono essere attivate quando un record raggiunge un punto di escalation definito. I punti di escalation rappresentano le condizioni che devono essere soddisfatte |
| Script di Automazione | Applicazione che permette di creare script in diversi linguaggi di programmazione al fine di automatizzare processi, eseguire calcoli complessi e personalizzare il comportamento delle applicazioni |
| Relazione | Le relazioni consentono di stabilire collegamenti tra più tabelle al fine di recuperare specifici dati mediante l'utilizzo di clausole "WHERE" all'interno di un'istruzione SQL. Questa funzionalità permette di definire una tabella di destinazione, chiamata "Figlio", sulla quale viene eseguita la clausola "WHERE", mentre la tabella di partenza è chiamata "Padre" |
| Relazione.Attributo | Convenzione utilizzata in Application Designer per fare riferimento a un attributo specifico di una tabella nel database di Maximo relazionata a quella corrente |
| Sigoption | Le sigoption (opzione di firma) permettono di specificare i privilegi per l’utilizzo di applicazioni, opzioni di menu ed elementi della barra degli strumenti. Inoltre, esistono sigoption condizionali che consentono di concedere o revocare dinamicamente i privilegi di un utente in base a espressioni condizionali. |
| MX Loader | Foglio excel che consente di: Caricare dati in Maximo Scaricare determinati dati presenti nel DB Maximo, tramite l’impostazione di opportune query all’interno del file |

# Obiettivo del documento

Il presente documento ha l’obiettivo descrivere l’architettura tecnica ed applicativa degli estrattori realizzati nel sistema EAM nell’ambito delle attività di produzione report gestiti dal sistema Data Platform in ambito asset management

## Introduzione al processo di produzione report fuori MAS

### Data Platform

Il sistema Data Platform avrà l’accountability di produrre un set di report con dati estratti tra l’altro dalla piattaforma EAM. Per farlo il sistema Data Platform sulla base dei requisiti raccolti nell’ambito dello stream di analisi ha richiesto al team EAM di:

contribuire alle fasi di analisi indicando la mappatura ove possibile dei campi presenti nel report con i campi presenti nel DB EAM (vedi xls analisi allegato in appendice)

rendere disponibile i dati individuato al bullet 1 attraverso delle viste che il sistema EAM dovrà aggiornare quotidianamente con profondità storica concordata (7g).

### EAM

Il sistema EAM ha l’accountability di rendere disponibile al sistema Data Platform i dati concordati utili alla produzione della reportistica di cui è accountable il sistema Data Platform in ambito Asset management. Per farlo il sistema EAM ha prodotto il mapping dei campi richiesti al punto 1 del capitolo 2.1.1 e prodotto degli estrattori per rendere disponibile al sistema Data Platform i dati richiesti come punto 2 capitolo 2.1.1.

Architettura applicativa

Disegno HL

### Componenti applicative

Di seguito è riportata la descrizione delle componenti applicative riportate nel disegno HL del

paragrafo 2.2.1:

Data platform: è il sistema accountable della produzione di un set di report in ambito Asset Managment. Per produrre i report si interfaccia con il sistema EAM, Tagetik ed altri sistemi per richiederne i dati necessari.

eam-reporting-loader: è il componente applicativo della piattaforma EAM incaricato dell’estrazione dei dati e della notifica a Data Platform dell’avvenuto aggiornamento. È stato realizzato un pod java based inserito nel project eam.

La sua funzione principale è quella di schedulare ed eseguire, con cadenza giornaliera, l’aggiornamento delle viste materializzate presenti nel database EAM, assicurando che riflettano fedelmente lo stato corrente delle tabelle fisiche operative con la profondità storica concordata. La profondità storica è configurata come variabile d’ambiente, pertanto risulta facilmente modificabile per necessità future.

Al termine di ogni aggiornamento, in caso di esito positivo, il eam-reporting-loader invia una notifica sincrona a Data Platform, segnalando che i dati aggiornati sono disponibili. Questo consente a Data Platform di procedere all’interrogazione delle viste di frontiera, da cui vengono estratti i dati necessari alla reportistica esterna al sistema MAS (Maximo Application Suite).In caso di esito negativo, il eam-reporting-loader invia comunque una notifica a Data Platform, segnalando che l’aggiornamento non è avvenuto correttamente

Database EAM: contiene le tabelle operative su cui si basa l’intero processo di estrazione. Per ottimizzare l’accesso ai dati e ridurre il carico sul database, è stata implementata una struttura a due livelli:

Viste materializzate, aggiornate quotidianamente mediante gli script eseguiti dal pod eam-reporting-loader , che rappresentano uno snapshot dei dati operativi.

Viste di frontiera, costruite a partire dalle viste materializzate, che vengono interrogate direttamente da Data Platform.

Questa architettura offre numerosi vantaggi:

Prestazioni migliorate: l’accesso ai dati avviene su strutture ottimizzate per la lettura.

Separazione dei ruoli: la gestione del dato rimane in capo al sistema EAM, mentre Data Platform accede in sola lettura alle viste esposte.

Flessibilità evolutiva: eventuali modifiche strutturali alle tabelle fisiche del database potranno essere assorbite a livello delle viste materializzate, senza impattare le viste esposte a Data Platform (purché lo schema resti compatibile).

In sintesi, questa suddivisione consente di bilanciare l’aggiornamento quotidiano dei dati con prestazioni di estrazione elevate, garantendo al contempo un forte disaccoppiamento tra il sistema operativo Maximo e la Data Platform.

### Data Model

#### Tabelle fisiche

X4_DP_REPORTING_TRACE:

Questa tabella di tracing registra al suo interno informazioni legate al refresh delle viste materializzate necessarie per i report fuori MAS giornalieri

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| DP_CORRELATION_ID | chiave identificativa univoca del refresh | VARCHAR2(50) | Y |
| EXECUTION_STARTDATE | Data e ora di inizio esecuzione del refresh | DATE | Y |
| EXECUTION_ENDDATE | Data e ora di fine esecuzione del refresh | DATE | N |
| DATE_REFRESH | Data del refresh | DATE | Y |
| STATUS | Stato del refresh | VARCHAR2(150) | N |

#### Viste Materializzate

Le viste materializzate hanno gli stessi campi della tabella su cui sono basate a meno delle colonne di tipo BLOB, CLOB (escluse su richiesta). Data Platform ha fatto richiesta di escludere questi campi per non appesantire estrattori e tabelle lette. Inoltre, hanno un campo aggiuntivo denominato DP_CORRELATION_ID, il quale rappresenta la chiave univoca di un record della tabella X4_DP_REPORTING_TRACE

RPT_DP_WORKORDER

Questa vista materializzata rappresenta le informazioni di testata dell’ordine di lavoro

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| WONUM | Identifies the work order. | VARCHAR2(25) | Y |
| PARENT | Parent of the work order shown in the Work Order field. When this field is blank, the work order in the Work Order field is a top-level work order. To assign a work order to a parent, select Assign to New Parent from the Select Action menu. | VARCHAR2(25) | N |
| STATUS | Status of the work order, for example, in progress, waiting on material, waiting for approval, completed, or closed. | VARCHAR2(16) | Y |
| STATUSDATE | Date the work order status was last changed. | DATE | Y |
| WORKTYPE | Identifies the work order's type. Some example types are: preventive maintenance, corrective maintenance, emergency maintenace, capital project, and event report. | VARCHAR2(5) | N |
| DESCRIPTION | Describes the work order. To enter or view additional information, click the Long Description button. | VARCHAR2(100) | N |
| ASSETNUM | Identifies the asset. | VARCHAR2(25) | N |
| LOCATION | Identifies the work order's location. This is not necessarily the asset's location, however, if an asset is entered, its location will default here | VARCHAR2(25) | N |
| DJPERROR | The error message when trying to apply the dynamic job plan | VARCHAR2(400) | N |
| FAILDATE | Actual Failure Date | DATE | N |
| CHANGEBY | Modified By | VARCHAR2(100) | N |
| CHANGEDATE | Date the work order was last modified. | DATE | N |
| ESTDUR | Estimated remaining number of hours needed to complete the work. | FLOAT | Y |
| ESTLABHRS | Estimated Labor Hours | FLOAT | Y |
| ESTMATCOST | Estimated Material Cost | NUMBER(10,2) | Y |
| ESTLABCOST | Estimated Labor Cost | NUMBER(10,2) | Y |
| ESTTOOLCOST | Estimated Tool Cost | NUMBER(10,2) | Y |
| PMNUM | Identifies the preventive maintenance record from which the work order was generated. | VARCHAR2(8) | N |
| ACTLABHRS | Actual Labor Hours | FLOAT | Y |
| ACTMATCOST | Actual Material Cost | NUMBER(10,2) | Y |
| ACTLABCOST | Actual Labor Cost | NUMBER(10,2) | Y |
| ACTTOOLCOST | Actual Tool Cost | NUMBER(10,2) | Y |
| HASCHILDREN | Y if WO has children N if it has no children | NUMBER | Y |
| OUTLABCOST | Outside Labor Cost | NUMBER(10,2) | Y |
| OUTMATCOST | Outside Material Cost | NUMBER(10,2) | Y |
| OUTTOOLCOST | Outside Tool Cost | NUMBER(10,2) | Y |
| HISTORYFLAG | Yes/No field or check box that specifies whether a record is a history record or is currently active. History records are complete, closed, canceled, etc. If a Y is in the History? field or the check box is selected, the record is a history record. On the List tab or the More Search Fields dialog box, you can enter a Y in the History? field to include only history records in the search results. If an N is in the field, Maximo includes only currently active records in the search results. If the field is empty (null), Maximo includes history and active records in the search results. | NUMBER | Y |
| CONTRACT | Identifies the warranty contract for the asset. | VARCHAR2(8) | N |
| WOPRIORITY | Identifies the importance of the work order, from 0-999, where 0 is the lowest priority and 999 is the highest. | NUMBER | N |
| TARGCOMPDATE | Date the work order is targeted to be completed. | DATE | N |
| TARGSTARTDATE | Date the work order is targeted to begin. If the work order is generated from a PM, the date is supplied by the PM work order generation process. | DATE | N |
| WOEQ1 | Extra Field Copied From Asset | VARCHAR2(10) | N |
| WOEQ2 | Extra Field Copied From Asset | VARCHAR2(10) | N |
| WOEQ3 | Extra Field Copied From Asset | VARCHAR2(10) | N |
| WOEQ4 | Extra Field Copied From Asset | VARCHAR2(10) | N |
| WOEQ5 | Extra Field Copied From Asset | NUMBER(10,2) | N |
| WOEQ6 | Extra Field Copied From Asset | DATE | N |
| WOEQ7 | Extra Field Copied From Asset | NUMBER(15,2) | N |
| WOEQ8 | Extra Field Copied From Asset | VARCHAR2(10) | N |
| WOEQ9 | Extra Field Copied From Asset | VARCHAR2(20) | N |
| WOEQ10 | Extra Field Copied From Asset | VARCHAR2(10) | N |
| WOEQ11 | Extra Field Copied From Asset | VARCHAR2(10) | N |
| WOEQ12 | Extra Field Copied From Asset | NUMBER(10,2) | N |
| REPORTEDBY | Identifies the person reporting the work order. | VARCHAR2(82) | N |
| REPORTDATE | The date and time the work order was reported. | DATE | N |
| PHONE | The phone number (usually a work site telephone number) associated with the work order. If the Reported By person has a phone number associated with it, that phone number is the default for this field. | VARCHAR2(20) | N |
| PROBLEMCODE | The problem being reported based on the defined Failure Class. This is usually the code in the second level of the failure reporting hierarchy. | VARCHAR2(8) | N |
| CALENDAR | Calendar to determine shift work is to be done on | VARCHAR2(8) | N |
| DOWNTIME | Does the Asset have to be down for this work order? | NUMBER | Y |
| ACTSTART | Date and time the actual work begain. | DATE | N |
| ACTFINISH | Date and time the actual work was completed. | DATE | N |
| SCHEDSTART | Date and time the work is scheduled to begin. | DATE | N |
| SCHEDFINISH | Date and time the work is scheduled to be completed. | DATE | N |
| REMDUR | Indicates the number of remaining hours needed to complete the work. You can modify this value until the work order is closed. | FLOAT | N |
| CREWID | ID for a given crew within an craft | VARCHAR2(12) | N |
| LASTCOPYLINKDATE | The date time doclinks was copied from other related objects like asset,location,jobplan,safetyplan etc. | DATE | N |
| WOEQ13 | Extra Field | DATE | N |
| WOEQ14 | Extra Field | NUMBER(15,2) | N |
| WOJP1 | Extra Field | VARCHAR2(15) | N |
| WOJP2 | Extra Field | VARCHAR2(10) | N |
| WOJP3 | Extra Field | VARCHAR2(10) | N |
| WOJP4 | Extra Field | NUMBER(10,2) | N |
| WOJP5 | Extra Field | DATE | N |
| WOL1 | Extra Field | VARCHAR2(10) | N |
| WOL2 | Extra Field | VARCHAR2(10) | N |
| WOL3 | Extra Field | NUMBER(10,2) | N |
| WOL4 | Extra Field | DATE | N |
| WOLABLNK | Labor Code Link | VARCHAR2(8) | N |
| RESPONDBY | A Calculated field specifying when by a breakdown should be responded to. | DATE | N |
| ASSETLOCPRIORITY | Identifies the priority level copied from the asset or location record and used to schedule the work order. Values from 0-999 are valid, but we recommend limiting your range of values to 0-9, where 0 designates the lowest priority. We also recommend assigning priority values only to locations. Entering a value will update the CalcPriority field according to the calculation option in the PriCalc table. MAXIMO uses this value to update the Respond By date. | NUMBER | N |
| CALCPRIORITY | Calculated Work Priority | NUMBER | N |
| CHARGESTORE | Check box that Indicates whether charges on the work order should be added to the asset record. If the check box is selected, the charges will be added to the asset; if the check box is cleared, the charges will not be added to the asset. If the asset on the work order is a non-capitalized rotating asset not located in an operating location or a storeroom, Maximo selects the check box; otherwise this check box is cleared. | NUMBER | Y |
| FAILURECODE | Failure class of the defined work asset. The failure class is the top level of the failure hierarchy. | VARCHAR2(8) | N |
| WOLO1 | Extra Field | VARCHAR2(10) | N |
| WOLO2 | Extra Field | VARCHAR2(10) | N |
| WOLO3 | Extra Field | VARCHAR2(10) | N |
| WOLO4 | Extra Field | VARCHAR2(10) | N |
| WOLO5 | Extra Field | VARCHAR2(10) | N |
| WOLO6 | Extra Field | NUMBER(10,2) | N |
| WOLO7 | Extra Field | DATE | N |
| WOLO8 | Extra Field | NUMBER(15,2) | N |
| WOLO9 | Extra Field | VARCHAR2(10) | N |
| WOLO10 | Extra Field | NUMBER | N |
| GLACCOUNT | General ledger account code to which work order costs are charged. The GL account consists of up to four components: cost center, activity, resource, and element, each separated by a hyphen. If the work order was generated from a PM, Maximo copies the GL account from the PM. This field is read-only if the Charge to Store? check box is selected. | VARCHAR2(110) | N |
| ESTSERVCOST | Total estimated service cost against this work order. | NUMBER(10,2) | Y |
| ACTSERVCOST | Total actual service cost against this work order. | NUMBER(10,2) | Y |
| DISABLED | Is this record active? | NUMBER | Y |
| ESTATAPPRLABHRS | Estimate of the work order's labor hours at the time the work order was approved. | FLOAT | Y |
| ESTATAPPRLABCOST | Cost estimate of the work order's labor at the time the work order was approved. | NUMBER(10,2) | Y |
| ESTATAPPRMATCOST | Cost estimate of the work order's material at the time the work order was approved. | NUMBER(10,2) | Y |
| ESTATAPPRTOOLCOST | Cost estimate of the work order's tools at the time the work order was approved. | NUMBER(10,2) | Y |
| ESTATAPPRSERVCOST | Cost estimate of the work order's services when the work order is approved. | NUMBER(10,2) | Y |
| WOSEQUENCE | Indicates sequence in which to execute the work orders in a work order hierarchy. | NUMBER | N |
| HASFOLLOWUPWORK | Specifies whether the work order has follow up work. If the check box is selected, there is follow up work. If the check box is cleared (the default), there is no follow up work. | NUMBER | Y |
| WORTS1 | Crossover field from Route_Stop.RTS1 | VARCHAR2(10) | N |
| WORTS2 | Crossover field from Route_Stop.RTS2 | VARCHAR2(10) | N |
| WORTS3 | Crossover field from Route_Stop.RTS3 | VARCHAR2(10) | N |
| WORTS4 | Crossover field from Route_Stop.RTS4 | DATE | N |
| WORTS5 | Crossover field from Route_Stop.RTS5 | NUMBER(15,2) | N |
| SOURCESYSID | Source System ID | VARCHAR2(10) | N |
| OWNERSYSID | Owner System ID | VARCHAR2(10) | N |
| PMDUEDATE | From the original Next Due Date of the PM | DATE | N |
| PMEXTDATE | From the Extended Date of the PM | DATE | N |
| PMNEXTDUEDATE | From the next Next Due Date of the PM | DATE | N |
| WORKLOCATION | Identifier of the Work Location that is to be responsible for the work. | VARCHAR2(25) | N |
| EXTERNALREFID | External Reference ID | VARCHAR2(10) | N |
| SENDERSYSID | Column used by ERP-Integration (APIs) | VARCHAR2(50) | N |
| FINCNTRLID | Financial Control Identifier | VARCHAR2(8) | N |
| GENERATEDFORPO | Work order for PO | VARCHAR2(8) | N |
| GENFORPOLINEID | POLINEID number that required the work | NUMBER | N |
| ORGID | Organization Identifier | VARCHAR2(8) | Y |
| SITEID | Identifies the site. | VARCHAR2(8) | Y |
| TASKID | Identifies the task. | NUMBER | N |
| INSPECTOR | Identifies the inspector assigned to the work order | VARCHAR2(100) | N |
| MEASUREMENTVALUE | Recorded Measurement | NUMBER(15,3) | N |
| MEASUREDATE | Date/Time when measurement was taken or when observation was taken. This field becomes read-only when you enter a measurement and save the record. | DATE | N |
| WORKPACKMTLSTATUS | The availability status of all materials on the current work order and related child records. The status is determined by the planned materials on a work order. The completion of the child records does not always determine the overall status of the materials in the work package. | VARCHAR2(20) | N |
| POINTNUM | The point number on the asset from which the measurement is taken. | VARCHAR2(8) | N |
| WOJO1 | Extra Field | VARCHAR2(10) | N |
| WOJO2 | Extra Field | VARCHAR2(10) | N |
| WOJO3 | Extra Field | VARCHAR2(10) | N |
| WOJO4 | Extra Field | NUMBER(15,2) | N |
| WOJO5 | Extra Field | VARCHAR2(10) | N |
| WOJO6 | Extra Field | VARCHAR2(10) | N |
| WOJO7 | Extra Field | VARCHAR2(10) | N |
| WOJO8 | Extra Field | VARCHAR2(10) | N |
| ISTASK | Specifies whether the work order is a task. If the check box is selected, the work order is a task. If the check box is cleared, the work order is not a task. | NUMBER | Y |
| WOCLASS | Identifies the work order's class. | VARCHAR2(16) | Y |
| ONBEHALFOF | Identifies for whom this work order has been created. For example, if someone did not have access to a computer, another user could create the work order on behalf of that person. | VARCHAR2(82) | N |
| VENDOR | Identifies the vendor responsible for the work. | VARCHAR2(12) | N |
| ORIGRECORDCLASS | Originating record's class, for example: change, release, incident, problem, or work order. | VARCHAR2(16) | N |
| ORIGRECORDID | Identifies the source record that was used to create this record. | VARCHAR2(25) | N |
| JUSTIFYPRIORITY | Describes why the work order received the priority it has been assigned. To enter or view additional information, click the Long Description button. | VARCHAR2(50) | N |
| RISK | Defines the risk level of the work order. Risk may be a numeric value or a term such as High, Medium, Low, or some other value based upon your business definitions. | VARCHAR2(10) | N |
| ENVIRONMENT | This field defines the environment in which the change is taking place. | VARCHAR2(50) | N |
| SUPERVISOR | Supervisor of the work. Maximo copies this information from the job plan, if there is one. You also can select a supervisor. | VARCHAR2(100) | N |
| WOACCEPTSCHARGES | Check box specifies whether or not the work order accepts charges. If the check box is selected (the default), the work order accepts charges. If the check box is cleared, the work order does not accept charges, and you cannot enter charges on the work order. | NUMBER | Y |
| OWNER | The person currently responsible for the work order. | VARCHAR2(100) | N |
| CLASSSTRUCTUREID | The Class Structure ID. | VARCHAR2(26) | N |
| PARENTCHGSSTATUS | Specifies whether the work order's status will change when its parent work order's status changes. If the check box is selected (the default), the work order's status will change when the parent work order's status changes. If the check box is cleared, the work order's status will not change when the parent work order's status changes. | NUMBER | Y |
| OWNERGROUP | The group currently responsible for the work order. | VARCHAR2(10) | N |
| COMMODITYGROUP | Defines the service group for the work order, for example: IT, production, facility, or fleet. | VARCHAR2(8) | N |
| COMMODITY | Defines the service for the work order, for example: customer support, painting, print, telecommunications, and welding. | VARCHAR2(8) | N |
| WORKORDERID | Unique Identifier | NUMBER | Y |
| WHOMISCHANGEFOR | Identifies whom a change is for. | VARCHAR2(20) | N |
| REASONFORCHANGE | Describes the reason for the change. To enter or view additional information, click the Long Description button. | VARCHAR2(20) | N |
| VERIFICATION | Used to verify that the Change was successful or describe how to determine if the Change was successful | VARCHAR2(20) | N |
| PERSONGROUP | Identifies the person group responsible for the work. Person groups are set up in the Person Groups application. | VARCHAR2(10) | N |
| LEAD | Lead person responsible for the work. | VARCHAR2(100) | N |
| LANGCODE | Language Column | VARCHAR2(4) | Y |
| INTERRUPTIBLE | Specifies whether the work order is allowed to be stopped once the work has begun and then restarted. If the check box is selected, the work can be stopped. If the check box is cleared, the work cannot be stopped and restarted. | NUMBER | Y |
| WOGROUP | This column is used to enhance performance of the Work Orders app. | VARCHAR2(25) | N |
| HASLD | Boolean flag to indicate if there is any long description for this record | NUMBER | Y |
| ROUTE | add route field | VARCHAR2(8) | N |
| ROUTESTOPID | add a new field | NUMBER | N |
| CINUM | Configuration Item | VARCHAR2(12) | N |
| JOBTASKID | Unique Identifier | NUMBER | N |
| WOISSWAP | Identifies if the Move is a Move or a Swap | NUMBER | Y |
| NEWCHILDCLASS | Identifies the child work order class. | VARCHAR2(16) | Y |
| SUSPENDFLOW | Flag used to suspend and resume flow control on a parent record. Rolls down the hierarchy to all its child records. | NUMBER | Y |
| FLOWCONTROLLED | Flag used to indicate a records participation in flow control. When applied to a parent, it rolls down the hierarchy to all its child records. | NUMBER | Y |
| FLOWACTIONASSIST | Suppresses the automatic firing of the action in flow control | NUMBER | Y |
| LAUNCHENTRYNAME | Launch Entry | VARCHAR2(32) | N |
| TARGETDESC | Temporary target description for targeting CIs | VARCHAR2(50) | N |
| FLOWACTION | The action to be performed (if any) when the workorder is started via process flow control | VARCHAR2(30) | N |
| FIRSTAPPRSTATUS | Identifies the first status change which approved the work order. This value is used to determine if the work order record has ever been approved. Work orders that have been approved are not allowed to be deleted. | VARCHAR2(16) | N |
| CALCORGID | Organization associated with the calculation calendar, and used with the Calendar and Shift fields to calculate the Target Contact, Target Response, and Target Resolution dates on a ticket or work order. Click the Detail Menu button to select an organization or go to the Organizations application to create one. | VARCHAR2(8) | N |
| CALCCALENDAR | Business days and times used to calculate the Target Contact, Target Response, and Target Resolution dates on a ticket or work order. Click the Detail Menu to select a calendar or go to the Calendars application to create one. | VARCHAR2(8) | N |
| CALCSHIFT | Along with the calculation calendar, sets the business hours that calculate the Target Contact, Target Response, and Target Resolution dates on a ticket or work order. Click the Select Value button to choose a shift. | VARCHAR2(8) | N |
| REPFACSITEID | The site for the repair facility. | VARCHAR2(8) | N |
| REPAIRFACILITY | Specifies the repair facility location. A repair facility can take ownership of work orders from multiple sites in the same organization. User security can be configured to give permission to view work orders in multiple sites if the work orders are owned by a single repair facility. | VARCHAR2(25) | N |
| GENFORPOREVISION | Revision number of the purchase order. Indicates how many times a purchase order has been revised. | NUMBER | N |
| STOREROOMMTLSTATUS | The availability status of storeroom materials on the current work order. | VARCHAR2(20) | N |
| DIRISSUEMTLSTATUS | The availability status of direct issue materials on the current work order. | VARCHAR2(20) | N |
| PCTASKCOMP | Specifies the percent complete as calculated by tasks completed vs. planned. | NUMBER | N |
| IGNORESRMAVAIL | Indicates whether to ignore the availability of items in the storeroom for the work order status approval. | NUMBER | Y |
| IGNOREDIAVAIL | Indicates whether to ignore the availability of direct issues for the work order status approval. | NUMBER | Y |
| ESTINTLABCOST | The estimated cost of internal labor for the task on the current work order. | NUMBER(10,2) | N |
| ESTINTLABHRS | The estimated hours of internal labor that are required for the task on the current work order. | FLOAT | N |
| ESTOUTLABHRS | The estimated hours of external labor that are required for the task on the current work order. | FLOAT | N |
| ESTOUTLABCOST | The estimated hours of external labor that are required for the task on the current work order. | NUMBER(10,2) | N |
| ACTINTLABCOST | The cost of internal labor for the task on the current work order. | NUMBER(10,2) | N |
| ACTINTLABHRS | The hours of internal labor that are required for the task on the current work order. | FLOAT | N |
| ACTOUTLABHRS | The hours of external labor that are required for the task on the current work order. | FLOAT | N |
| ACTOUTLABCOST | The cost of external labor that are required for the task on the current work order. | NUMBER(10,2) | N |
| ESTATAPPRINTLABCOST | The cost of internal labor for the task on the current work order at the time it was approved. | NUMBER(10,2) | N |
| ESTATAPPRINTLABHRS | The hours of internal labor for the task on the current work order at the time it was approved. | FLOAT | N |
| ESTATAPPROUTLABHRS | The hours of internal labor for the task on the current work order at the time it was approved. | FLOAT | N |
| ESTATAPPROUTLABCOST | The cost of external labor for the task on the current work order at the time it was approved. | NUMBER(10,2) | N |
| ASSIGNEDOWNERGROUP | Assigned Owner Group of the workorder record. This group has overall responsibility for the solution. Use the Select Action menu to assign an owner group. You can enter a value either in this field or the Owner field. | VARCHAR2(10) | N |
| AVAILSTATUSDATE | The date and time that the material status was last updated. | DATE | N |
| PCACTHRS | Specifies the percent complete as calculated by actual hours vs. planned hours. | NUMBER | N |
| NESTEDJPINPROCESS | Record will be locked while the jobplan containing nested jobplans is in the process of being applied to the record. | NUMBER | Y |
| INSPFORMNUM | The number of the inspection form. | VARCHAR2(12) | N |
| PLUSCFREQUENCY | Number of time units (days, weeks, months, or years) to elapse between work orders you generate from this PM. The count begins at the value in the Last Target Start Date (if Use Target Start? is selected or Last Completion Date (if Use Target Start? is not the selected field. This value may differ differ from the Regulatory Frequency to facilitate scheduling. For example, a regulatory frequency of 31 days may have a frequency of 28 days. | NUMBER | N |
| PLUSCFREQUNIT | Defines the time units (days, weeks, months, or years) for the Frequency field on time-based PMs. | VARCHAR2(8) | N |
| PLUSCISMOBILE | Workorder was added by mobile | NUMBER | Y |
| PLUSCLOOP | Select this check box to identify the work order as a loop calibration. May be selected when a value is entered in the Location field. | NUMBER | Y |
| PLUSCNEXTDATE | Date the next work order is scheduled to be generated from the PM. The Next Due Date field is NULL unless there is an integer value in the Frequency field. | DATE | N |
| PLUSCOVERDUEDATE | Displays the over due date for the calibration, if the work order is not completed. | DATE | N |
| PLUSCPHYLOC | Enter a detailed description of the location of the asset or specify the location information using a hierarchy that you created. Using a hierarchy provides consistent identification and groups assets efficiently. | VARCHAR2(250) | N |
| UNITSOFWORK | Work Unit | VARCHAR2(16) | N |
| DJPAPPLIED | This flag can be set to YES, NO, ERROR | VARCHAR2(5) | Y |
| INCTASKSINSCHED | Indicates that this work order's tasks will be included in the schedule view. | NUMBER | Y |
| SNECONSTRAINT | The earliest date at which work should start. Scheduled dates should be after this date. | DATE | N |
| FNLCONSTRAINT | The date that work should be completed by. Scheduled dates should be before this date. | DATE | N |
| CREWWORKGROUP | Resource pool that will perform the work order. | VARCHAR2(10) | N |
| REQASSTDWNTIME | Indicates if the asset requires downtime while work is performed. | NUMBER | Y |
| APPTREQUIRED | Indicates if an appointment is required for the work order. Tasks that are created for this work order inherit this value, but can be modified. | NUMBER | Y |
| AOS | Indicates that the work cannot be performed while the asset is operating. | NUMBER | Y |
| AMS | Indicates that the work must be performed during a dedicated maintenance window. | NUMBER | Y |
| LOS | Indicates that the work must be performed when the location is not in use, and available for maintenance. | NUMBER | Y |
| LMS | Indicates that the work must be performed during a dedicated maintenance window. | NUMBER | Y |
| REPAIRLOCFLAG | Indicates whether the repair location is required. | NUMBER | Y |
| INTSHIFT | Specifies the shift on which the work will be interrupted in the Graphical Scheduling application. | VARCHAR2(8) | N |
| PCPCHANGEBY | Physical percent complete - Who last changed | VARCHAR2(100) | N |
| PCPCHANGEDATE | Physical percent complete - When last changed | DATE | N |
| PCPHYS | Specifies the user-defined percent complete as entered manually. | NUMBER | N |
| JPNUM | Identifies the work order's job plan. When you assign a job plan to a work order, Maximo copies the job plan operations, material, labor, and tool information to the work order's work plan. Maximo also copies job plan data into these fields: Interruptible?, Crew, Supervisor, and WO Priority. If there is an existing association between the job plan and a safety plan when the job plan is used on the asset or location, Maximo also copies the safety plan to the work order. In addition, Maximo automatically copies a single measurement point if there is an existing association between the asset on the work order and the point name on the job plan. | VARCHAR2(16) | N |
| SPLANREVIEWDATE | Does this Work Order safetplan is reviwed? | DATE | N |
| MILESTONE | Indicates if this Work Order is a milestone. | NUMBER | Y |
| ROWSTAMP | #N/A | VARCHAR2(40) | Y |
| ANYWHEREREFID | Anywhere Reference ID | NUMBER | N |
| PLUSCJPREVNUM | Displays the revision number of the Job Plan the work order was generated with. | NUMBER | N |
| X4POLICY | X4policy | VARCHAR2(20) | N |
| X4FLAGFISICO | Conferma delle misure del consuntivo fisico | NUMBER | Y |
| X4ANR | ANR | VARCHAR2(15) | N |
| X4CAPEX | CAPEX | VARCHAR2(15) | N |
| X4TIPOSPESA | Tipo spesa | VARCHAR2(5) | N |
| X4COIN | Codice Coin | VARCHAR2(15) | N |
| X4WBE | WBE | VARCHAR2(20) | N |
| X4WBEPIAN | WBE Pianificata | VARCHAR2(20) | N |
| X4WBS | WBS | VARCHAR2(20) | N |
| X4WBSPIAN | WBS Pianificata | VARCHAR2(20) | N |
| X4ODM | OdM | VARCHAR2(20) | N |
| X4NTK | NTK | VARCHAR2(20) | N |
| X4TIPOORDINE | Tipo ordine | VARCHAR2(15) | N |
| X4NORDINEINTERNO | Ordine Interno | VARCHAR2(20) | N |
| X4CODDELINDISP | Codice Delibera Indisponibilità | VARCHAR2(25) | N |
| X4DFINEINDISP | Data Fine Indisponibilità | DATE | N |
| X4DINIZIOINDISP | Data Inizio Indisponibilità | DATE | N |
| X4UM | Unità di misura | VARCHAR2(5) | N |
| X4STORNOSAP | Storno totale SAP | VARCHAR2(10) | N |
| X4PERIMETROINT | Perimetro Intervento | VARCHAR2(25) | N |
| X4TIPOODL | Tipo OdL | VARCHAR2(15) | N |
| X4TRAINANTE | Trainante / trainato | VARCHAR2(10) | N |
| X4FISICO | Fisico | FLOAT | N |
| X4DATEMOD | Data pianificata modificata | NUMBER | Y |
| X4TIPOOI | Tipo ordine interno | VARCHAR2(15) | N |
| X4NOPERAZIONE | Numero operazione | VARCHAR2(20) | N |
| X4ASSIEME | Assieme | VARCHAR2(35) | N |
| X4SOTTOASSIEME | Sottoassieme | VARCHAR2(50) | N |
| TOTALWORKUNITS | Total Work Units for WO, including header asset and assets in MultiAssetLocCI table | NUMBER(10,2) | N |
| X4NODM | Numero ordine di manutenzione | VARCHAR2(20) | N |
| X4PREOWNERGROUP | Campo di lavoro del WF | VARCHAR2(10) | N |
| X4MODULICAR | Moduli caricati | NUMBER | Y |
| X4SAPCONSOK | Conferma Ore da SAP | NUMBER | Y |
| X4ANAGRAFICAUPDATE | Anagrafica Aggiornata | NUMBER | Y |
| X4SERBATOIO | Serbatoio | NUMBER | Y |
| X4BURKS | Società | VARCHAR2(10) | N |
| X4FISICOPLAN | Fisico pianificato | FLOAT | N |
| X4NOTE | Note | VARCHAR2(150) | N |
| X4NATURALAVORO | Natura lavoro | VARCHAR2(15) | N |
| X4INFO1 | Informazioni OdL | VARCHAR2(150) | N |
| X4AGGREGATI | OdL Aggregatore | NUMBER | Y |
| X4GEOCALLCLOSED | Geocall closed | NUMBER | Y |
| X4TRONCONUM | X4tronconum | VARCHAR2(25) | N |
| X4TITRIS | TITRIS | VARCHAR2(150) | N |
| X4STATUSINDISP | Stato indisponibilità | VARCHAR2(20) | N |
| X4SAPIDASSET | SAPID Asset | VARCHAR2(30) | N |
| X4CLASSASSET | Classificazione Asset | VARCHAR2(192) | N |
| X4MICROCONTROLLO | X4microcontrollo | VARCHAR2(15) | N |
| X4CLUSTERINDISP | Cluster titolare dell'indisponibilità | VARCHAR2(20) | N |
| X4ESITOINDISP | Esito richiesta indisponibilità | VARCHAR2(50) | N |
| X4MOTIVOINDISP | Motivazione annullamento indisponibilità | VARCHAR2(15) | N |
| X4IDERRORINDISP | Codice errore indisponibilità | VARCHAR2(20) | N |
| X4DESCERRORINDISP | Descrizione errore indisponibilità | VARCHAR2(50) | N |
| X4SEDEINDISP | Sede di indisponibilità | VARCHAR2(20) | N |
| X4SEDEINDISPL1 | Sede di indisponibilità di primo livello | VARCHAR2(20) | N |
| X4POLICYID | ID della policy numerico | NUMBER | N |
| X4DATAMODELLO | Data modello | DATE | N |
| X4TPREP | Tempo Preparazione | FLOAT | N |
| X4TTRASF | Tempo Trasferimento | FLOAT | N |
| X4TEMPOPAUSA | Tempo pausa | FLOAT | N |
| X4TOPER | Tempo Operazione | FLOAT | N |
| X4EFFORTO | Effort operai | FLOAT | N |
| X4EFFORTI | Effort impiegati | FLOAT | N |
| X4EFFORTQ | Effort quadri | FLOAT | N |
| X4CAPACITYEROSAO | Capacity erosa operai da OdL a serbatoio | NUMBER | N |
| X4CAPACITYRESIDUAO | Capacity residua operai da OdL a serbatoio | NUMBER | N |
| X4CAPACITYEROSAI | Capacity erosa impiegati da OdL a serbatoio | NUMBER | N |
| X4CAPACITYRESIDUAI | Capacity residua impiegati del serbatoio | NUMBER | N |
| X4CAPACITYEROSAQ | Capacity erosa quadri da OdL a serbatoio | NUMBER | N |
| X4CAPACITYRESIDUAQ | Capacity residua quadri del serbatoio | NUMBER | N |
| X4OUTSOURCING | Outsourcing | NUMBER | Y |
| X4PIANMPE | Pianificato MPE | VARCHAR2(2) | N |
| X4DATAULTIMAPIAN | Data ultima pianificazione | DATE | N |
| X4INIZIOESCLUSIONE | inizio periodo esclusione | DATE | N |
| X4FINEESCLUSIONE | Data fine periodo esclusione | DATE | N |
| OBSERVATION | Describes the visual inspection data for this operation. If the work order has an associated work plan, Maximo copies this information from the Plans tab. You can edit this field until the work order is closed. | VARCHAR2(8) | N |
| BACKOUTPLAN | Describes the back out plan in the event the change needs to be reversed. | VARCHAR2(50) | N |
| AMCREW | Defines the Crew who will be copied to the work order created with this job plan. | VARCHAR2(8) | N |
| X4TIPOATTIVITA | Tipo attività | VARCHAR2(15) | N |
| X4TECMIG |  | NUMBER | Y |
| X4SOTTOPROGRAMMA |  | VARCHAR2(50) | N |
| X4NOTEMIG | Note MIG | VARCHAR2(250) | N |
| X4ORDINEPREVALENTE |  | VARCHAR2(25) | N |
| X4ERRSAP |  | VARCHAR2(250) | N |
| X4DSS |  | NUMBER | Y |
| X4PRIMAELEGGIBILITA |  | DATE | N |
| X4PMI |  | NUMBER | Y |
| X4STATOGEOCALL |  | VARCHAR2(30) | N |
| ID_COMPONENTE |  | VARCHAR2(10) | N |
| X3CAMPONOTE |  | VARCHAR2(150) | N |
| X3DATAMODELLO |  | DATE | N |
| X3DATAPIANMOD |  | NUMBER | Y |
| X3NOTECHIUSURA |  | VARCHAR2(150) | N |
| X3TEMPOPAUSA |  | FLOAT | N |
| X3TPREP |  | FLOAT | N |
| X3TRAINANTE |  | NUMBER | Y |
| X3TTRASF |  | FLOAT | N |
| X3UM |  | VARCHAR2(5) | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta: CHANGEDATE.

La logica di cancellazione è: Logica, sul campo Status.

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe che hanno changedate>= sysdate -7g.

RPT_DP_WPLABOR

Questa vista materializzata rappresenta le risorse pianificate

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| WONUM | Work Order | VARCHAR2(25) | Y |
| LABORCODE | Labor | VARCHAR2(8) | N |
| LABORHRS | Regular Hours | FLOAT | Y |
| RATE | Rate | NUMBER(10,2) | Y |
| WPL1 | Wpl1 | VARCHAR2(10) | N |
| WPL2 | Wpl2 | DATE | N |
| WPL3 | Wpl3 | VARCHAR2(10) | N |
| WPL4 | Wpl4 | VARCHAR2(10) | N |
| WPL5 | Wpl5 | VARCHAR2(10) | N |
| WPL6 | Wpl6 | NUMBER(15,2) | N |
| WPLABORID | WP Labor ID | VARCHAR2(20) | Y |
| VENDOR | Vendor | VARCHAR2(12) | N |
| RATEHASCHANGED | Rate Changed | NUMBER | Y |
| WPL7 | WPL7 | VARCHAR2(10) | N |
| WPL8 | WPL8 | VARCHAR2(10) | N |
| WPL9 | WPL9 | VARCHAR2(10) | N |
| ORGID | Organization | VARCHAR2(8) | Y |
| SITEID | Site | VARCHAR2(8) | Y |
| WPLABORUID | WPLABORUID | NUMBER | Y |
| CRAFT | Craft | VARCHAR2(8) | N |
| SKILLLEVEL | Skill Level | VARCHAR2(15) | N |
| CONTRACTNUM | Labor Contract | VARCHAR2(8) | N |
| QUANTITY | Quantity | NUMBER | Y |
| JOBLABORID | Job Labor ID | NUMBER | N |
| AMCREW | Crew | VARCHAR2(8) | N |
| AMCREWTYPE | Crew Type | VARCHAR2(8) | N |
| CREWWORKGROUP | Crew Work Group | VARCHAR2(10) | N |
| APPTREQUIRED | Appointment Required | NUMBER | Y |
| ROWSTAMP | Chiave tecnica maximo | VARCHAR2(40) | Y |
| X4TITRIS | Titris | VARCHAR2(10) | N |
| X4DATAINIZIORISORSA | Data inizio risorsa | DATE | N |
| X4DATAFINERISORSA | Data fine risorsa | DATE | N |
| X4OREESEC | Ore esecuzione | FLOAT | N |
| X4OREPOSTESEC | Ore post esecuzione | FLOAT | N |
| X4OREPREESE | Ore pre esecuzione | FLOAT | N |
| X4ORETRASF | Ore trasferimento | FLOAT | N |
| X4CHANGEDATE | Change date | DATE | N |
| X3DATAINIZIORISORSA |  | DATE | N |
| X3DATAFINERISORSA |  | DATE | N |
| X3OREPRESE |  | FLOAT | N |
| X3OREESEC |  | FLOAT | N |
| X3OREPOSTESEC |  | FLOAT | N |
| X3ORETRASF |  | FLOAT | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta: X4CHANGEDATE.

La logica di cancellazione è: Fisica.

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe delle tabelle che sono riferite a WORKORDER che hanno changedate>= sysdate -7g.La In caso di cancellazione il record su questa tabella vengono cencellati fisicamente, generando l’impossibilità / criticità da parte di DP di individuare i record cancellati tra un aggiornamento ed un’altro. L’azione mitigativa per ovviare a questo problema legato alla cancellazione fisica su WPLABOR sarà la seguente:

Attiviamo Audit sulla tabella WPLABOR. La storicizzazione parte per ogni campo usato.

Quindi vengono aggiunte righe sulla tabella A_WPLABOR con anche i dati delle righe cancellate, in modo da mantenerne lo storico, il quale viene esposto anch’esso in un apposita vista. Eventuali righe cancellate potranno essere individuate tramite filtro effettuato sulla tabella di audit filtrando per campo EAUDITTYPE= D e creando una vista materializzata dedicata a questi record.

RPT_DP_WPSERVICE

Questa vista materializzata rappresenta le risorse esterne pianificate

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| WPM5 | Wpm4 | NUMBER(10,2) | N |
| WPM6 | Wpm6 | VARCHAR2(10) | N |
| X4DATAFINERISORSA | Data fine | DATE | N |
| X4DATAINIZIORISORSA | Data inizio | DATE | N |
| AMCREW | Crew | VARCHAR2(8) | N |
| AMCREWTYPE | Crew Type | VARCHAR2(8) | N |
| CATALOGCODE | Catalog # | VARCHAR2(30) | N |
| CONDITIONCODE | Condition Code | VARCHAR2(30) | N |
| DESCRIPTION | Description | VARCHAR2(40) | N |
| DIRECTREQ | Direct Issue | NUMBER | Y |
| HASLD | Has Long Description | NUMBER | Y |
| HOURS | Tool Hours | FLOAT(126) | Y |
| ISSUETO | Issue To | VARCHAR2(8) | N |
| ITEMNUM | Service | VARCHAR2(30) | N |
| ITEMQTY | Quantity | NUMBER(15,2) | N |
| ITEMSETID | Item Set | VARCHAR2(8) | N |
| JOBITEMID | Job Item ID | NUMBER | N |
| LANGCODE | Language Code | VARCHAR2(4) | Y |
| LINECOST | Line Cost | NUMBER(10,2) | N |
| LINETYPE | Line Type | VARCHAR2(15) | Y |
| LOCATION | Storeroom | VARCHAR2(25) | N |
| MANUFACTURER | Manufacturer | VARCHAR2(12) | N |
| MKTPLCITEM | Marketplace Item | NUMBER | Y |
| MODELNUM | Model | VARCHAR2(8) | N |
| ORDERUNIT | Order Unit | VARCHAR2(16) | N |
| ORGID | Organization | VARCHAR2(8) | Y |
| PR | PR | VARCHAR2(8) | N |
| PRLINENUM | PR Line | NUMBER | N |
| RATE | Rate | NUMBER(10,2) | Y |
| RATEHASCHANGED | Rate Changed | NUMBER | Y |
| REQUESTBY | Requested By | VARCHAR2(100) | N |
| REQUESTNUM | Request | VARCHAR2(20) | N |
| REQUIREDATE | Required Date | DATE | N |
| RESTYPE | Reservation Type | VARCHAR2(16) | N |
| ROWSTAMP | Campo tecnico maximo | VARCHAR2(40) | Y |
| SITEID | Site | VARCHAR2(8) | Y |
| STORELOCSITE | Storeroom Site | VARCHAR2(8) | N |
| UNITCOST | Unit Cost | NUMBER(10,2) | N |
| UNITCOSTHASCHANGED | Unit Cost Changed | NUMBER | Y |
| VENDOR | Vendor | VARCHAR2(12) | N |
| VENDORPACKCODE | Vendor Pack Code | VARCHAR2(12) | N |
| VENDORPACKQUANTITY | Vendor Pack Quantity | VARCHAR2(12) | N |
| VENDORUNITCOST | Vendor Unit Cost | NUMBER(10,2) | N |
| VENDORWAREHOUSE | Vendor Warehouse | VARCHAR2(12) | N |
| WONUM | Work Order | VARCHAR2(25) | Y |
| WPITEMID | WPITEMID | NUMBER | Y |
| WPLABORREF | Crew Reference | NUMBER | N |
| WPM1 | Wpm1 | VARCHAR2(10) | N |
| WPM2 | Wpm2 | VARCHAR2(10) | N |
| WPM3 | Wpm3 | NUMBER(15,2) | N |
| WPM4 | Wpm4 | VARCHAR2(10) | N |
| X4CHANGEDATE | Data modifica | DATE | Y |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta:

X4CHANGEDATE.

La logica di cancellazione è: Fisica.

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe delle tabelle che sono riferite a WORKORDER che hanno changedate>= sysdate -7g.

In caso di cancellazione il record su questa tabella vengono cencellati fisicamente, generando l’impossibilità / criticità da parte di DP di individuare i record cancellati tra un aggiornamento ed un altro.L’azione mitigativa per ovviare a questo problema legato allacancellazionfisica su WPSERVICE sarà la seguente:

Attiviamo Audit sulla tabella WPSERVICE. La storicizzazione parte per ogni campo usato.

Quindi vengono aggiunte righe sulla tabella A_WPSERVICE con anche i dati delle righe cancellate, in modo da mantenerne lo storico, il quale viene esposto anch’esso in un apposita vista. Eventuali righe cancellate potranno essere individuate tramite filtro effettuato sulla tabella di audit filtrando per campo EAUDITTYPE= D e creando una vista materializzata dedicata a questi record.

RPT_DP_ASSIGNMENT

Questa vista materializzata rappresenta le assegnazioni delle risorse dell’odl

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| WPLABORID | WP Labor ID | VARCHAR2(20) | N |
| WONUM | Work Order | VARCHAR2(25) | Y |
| CRAFT | Craft | VARCHAR2(8) | N |
| LABORHRS | Hours | FLOAT | N |
| STATUS | Status | VARCHAR2(12) | Y |
| SCHEDULEDATE | Scheduled Start | DATE | N |
| LABORCODE | Labor | VARCHAR2(8) | N |
| STARTDATE | Start Date | DATE | N |
| FINISHDATE | Scheduled Finish | DATE | N |
| ORGID | Organization | VARCHAR2(8) | Y |
| SITEID | Site | VARCHAR2(8) | N |
| ASSIGNMENTID | ASSIGNMENTID | NUMBER | Y |
| SKILLLEVEL | Skill Level | VARCHAR2(15) | N |
| CONTRACTNUM | Contract | VARCHAR2(8) | N |
| VENDOR | Vendor | VARCHAR2(12) | N |
| WORKDATE | Work Date | DATE | N |
| PARENTASSIGNID | Parent Assignment ID | NUMBER | N |
| CREWWORKGROUP | Crew Work Group | VARCHAR2(10) | N |
| AMCREW | Crew | VARCHAR2(8) | N |
| AMCREWTYPE | Crew Type | VARCHAR2(8) | N |
| APPTREQUIRED | Appointment Required | NUMBER | Y |
| APPOINTMENT | Schedule Lock | NUMBER | Y |
| WORKLOG | WORKLOG | NUMBER | Y |
| CHANGEBY | Change By | VARCHAR2(100) | N |
| CHANGEDATE | Changed Date | DATE | N |
| ROWSTAMP | Campo tecnico Maximo | VARCHAR2(40) | Y |
| RESOURCELOCK | Resource Lock | NUMBER | Y |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta:

CHANGEDATE.

La logica di cancellazione è: Fisica.

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe delle tabelle che sono riferite a WORKORDER che hanno changedate>= sysdate -7g.

L’azione mitigativa della cancellazione fisica su ASSIGNMENT sarà la seguente:

Attiviamo Audit sulla tabella ASSIGNMENT. La storicizzazione parte per ogni campo usato.

Quindi vengono aggiunte righe sulla tabella A_ASSIGNMENT con anche i dati delle righe cancellate, in modo da mantenerne lo storico, il quale viene esposto anch’esso in un apposita vista

RPT_DP_LABTRANS

Questa vista materializzata rappresenta le consuntivazioni dell’ordine di lavoro

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| TRANSDATE | Transaction Date | DATE | Y |
| LABORCODE | Labor | VARCHAR2(8) | Y |
| CRAFT | Craft | VARCHAR2(8) | Y |
| PAYRATE | Rate | NUMBER(10,2) | Y |
| ASSETNUM | Asset | VARCHAR2(25) | N |
| REGULARHRS | Regular Hours | FLOAT | Y |
| ENTERBY | Entered By | VARCHAR2(100) | Y |
| ENTERDATE | Entered Date | DATE | Y |
| STARTDATE | Data inizio | DATE | Y |
| STARTTIME | Ora inizio | DATE | N |
| FINISHDATE | Data fine | DATE | N |
| FINISHTIME | Ora fine | DATE | N |
| TRANSTYPE | Type | VARCHAR2(16) | Y |
| OUTSIDE | Outside | NUMBER | Y |
| MEMO | Memo | VARCHAR2(100) | N |
| ROLLUP | Rolled Up | NUMBER | Y |
| GLDEBITACCT | GL Debit Account | VARCHAR2(110) | N |
| LINECOST | Line Cost | NUMBER(10,2) | Y |
| GLCREDITACCT | GL Credit Account | VARCHAR2(110) | N |
| FINANCIALPERIOD | Financial Period | VARCHAR2(6) | N |
| PONUM | PO | VARCHAR2(8) | N |
| POLINENUM | PO Line | NUMBER | N |
| LOCATION | Location | VARCHAR2(25) | N |
| GENAPPRSERVRECEIPT | Approved | NUMBER | Y |
| PAYMENTTRANSDATE | Recorded as Received | DATE | N |
| EXCHANGERATE2 | Secondary Exchange Rate | NUMBER(14,7) | N |
| LINECOST2 | Premium Code | NUMBER(10,2) | N |
| LABTRANSID | ID | NUMBER | Y |
| SERVRECTRANSID | Service Receipt ID | NUMBER | N |
| SOURCESYSID | Source System ID | VARCHAR2(10) | N |
| OWNERSYSID | Owner System ID | VARCHAR2(10) | N |
| EXTERNALREFID | External Reference ID | VARCHAR2(10) | N |
| SENDERSYSID | Sender System ID | VARCHAR2(50) | N |
| FINCNTRLID | FCID | VARCHAR2(8) | N |
| ORGID | Organization | VARCHAR2(8) | Y |
| SITEID | Site | VARCHAR2(8) | Y |
| REFWO | Activity | VARCHAR2(25) | N |
| ENTEREDASTASK | Entered as Task | NUMBER | Y |
| TICKETID | Ticket | VARCHAR2(10) | N |
| TICKETCLASS | Ticket Class | VARCHAR2(16) | N |
| CONTRACTNUM | Contract | VARCHAR2(8) | N |
| SKILLLEVEL | Skill Level | VARCHAR2(15) | N |
| TIMERSTATUS | Timer Status | VARCHAR2(16) | N |
| INVOICENUM | Invoice | VARCHAR2(8) | N |
| INVOICELINENUM | Invoice Line | NUMBER | N |
| REVISIONNUM | Revision | NUMBER | N |
| PREMIUMPAYCODE | Premium Pay Code | VARCHAR2(8) | N |
| PREMIUMPAYHOURS | Premium Pay Hours | FLOAT | N |
| PREMIUMPAYRATE | Premium Pay Rate | NUMBER(10,2) | N |
| VENDOR | Vendor | VARCHAR2(12) | N |
| PREMIUMPAYRATETYPE | Premium Rate Type | VARCHAR2(10) | N |
| POREVISIONNUM | PO Revision | NUMBER | N |
| STARTDATETIME | Start Date Time | DATE | N |
| FINISHDATETIME | Start Date Time | DATE | N |
| CREWWORKGROUP | Crew Work Group | VARCHAR2(10) | N |
| AMCREW | Crew | VARCHAR2(8) | N |
| AMCREWTYPE | Crew Type | VARCHAR2(8) | N |
| POSITION | Position | VARCHAR2(20) | N |
| ROWSTAMP | campo tecnico maximo | VARCHAR2(40) | Y |
| ANYWHEREREFID | Anywhere Ref ID | NUMBER | N |
| X3TPAUSA | Tempo Pausa | FLOAT | N |
| X3TTRASFERIMENTO | Tempo Trasferimento | FLOAT | N |
| X4CHECKSAP | Check SAP | VARCHAR2(10) | N |
| X4ERRSAP | Errore SAP | VARCHAR2(200) | N |
| LAVORO_EFFETTIVO_PRE | Lavoro effettivo pre | FLOAT | N |
| X4STORNO | X4storno | VARCHAR2(10) | N |
| X4ORETRASF | Ore trasferimento | FLOAT | N |
| X4OREUFF | Ore in ufficio | FLOAT | N |
| X4ESITOSAPDESC | Descrizione errore SAP | VARCHAR2(2000) | N |
| X4IDCONSGEO | ID Cons Geocall | VARCHAR2(50) | N |
| X4ORETOT | Ore totali | FLOAT | N |
| X4CHANGEDATE | Change Date | DATE | N |
| APPROVER |  | VARCHAR2(100) | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta: X4CHANGEDATE.

La logica di cancellazione è: Logica, REFWO/X4STORNO. Valore X4STORNO In (P, T, S) per indicare che la riga è cancellata

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe che hanno x4changedate>= sysdate -7g.

RPT_DP_X4INTERFSTAT

Questa vista materializzata rappresenta la consistenza delle interferenze

| Campo | Descrizione | Tipo | Obbligatorio |
| --- | --- | --- | --- |
| X4INTERFSTATID | X4interfstatid | NUMBER | Y |
| DESCRIPTION | Description | VARCHAR2(200) | N |
| ASSETNUM | Asset | VARCHAR2(25) | N |
| STATUS | Status | VARCHAR2(15) | N |
| ULTIMARILEVAZIONE | Ultima rilevazione | DATE | N |
| PRIMARILEVAZIONE | Prima rilevazione | DATE | N |
| SITEID | Siteid | VARCHAR2(8) | N |
| ROWSTAMP | Campo tecnico Maximo | VARCHAR2(40) | Y |
| IDINTERFERENZA | ID Interferenza | NUMBER | N |
| GRAVITA | Gravita | VARCHAR2(50) | N |
| ODLRISOLUTORE | ODL Risolutore | VARCHAR2(25) | N |
| IDINTERFERENZA_EXT | Idinterferenza_ext | VARCHAR2(50) | N |
| CAUSALE | Causale | VARCHAR2(50) | N |
| FASE | Fase | VARCHAR2(4) | N |
| NONCONFERMATO | Non confermato | NUMBER | Y |
| ASSETNUM1LIV | Asset primo livello | VARCHAR2(25) | N |
| CHANGEBY | Changeby | VARCHAR2(50) | N |
| HASLD | Hasld | NUMBER | N |
| CHANGEDATE | Changedate | DATE | N |
| X4NOTEMIG | Note MIG | VARCHAR2(250) | N |
| NUMERO_COLLISIONI |  | NUMBER | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta: CHANGEDATE.

La logica di cancellazione è: Logica, sul campo Status.

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe che hanno changedate>= sysdate -7g.

RPT_DP_X4DIFFORSTAT

Questa vista materializzata rappresenta la consistenza delle difformità

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| X4DIFFORSTATID | X4difforstatid | NUMBER | Y |
| DESCRIPTION | Description | VARCHAR2(50) | N |
| ASSETNUM | Assetnum | VARCHAR2(25) | N |
| SITEID | Siteid | VARCHAR2(8) | N |
| IDDIFFORMITA | Iddifformita | NUMBER | N |
| STATUS | Status | VARCHAR2(15) | N |
| ULTIMARILEVAZIONE | Ultima rilevazione | DATE | N |
| PRIMARILEVAZIONE | Prima rilevazione | DATE | N |
| HASLD | Hasld | NUMBER | Y |
| ROWSTAMP | Campo tecnico Maximo | VARCHAR2(40) | Y |
| GRAVITA | Gravita | VARCHAR2(50) | N |
| CONFERMATO | Confermato | NUMBER | Y |
| NONCONFERMATO | Non confermato | NUMBER | Y |
| ODLRISOLUTORE | ODL Risolutore | VARCHAR2(25) | N |
| IDDIFFORMITA_EXT | Iddifformita_ext | VARCHAR2(30) | N |
| CAUSALE | Causale | VARCHAR2(50) | N |
| FASE | Fase | VARCHAR2(4) | N |
| CHANGEBY | Matricola validatore | VARCHAR2(100) | N |
| IDMISURA | ID misura | VARCHAR2(30) | N |
| VALMISURA | Valore misura | NUMBER | N |
| CHANGEDATE | Changedate | DATE | N |
| X4NOTEMIG | Note MIG | VARCHAR2(250) | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta: CHANGEDATE.

La logica di cancellazione è: Logica, sul campo Status.

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe che hanno changedate>= sysdate -7g.

RPT_DP_JOBPLAN

Questa vista materializzata rappresenta i piani di lavoro

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| HASNESTEDJP | Nested Job Plans | NUMBER | Y |
| JOBPLANID | JOBPLANID | NUMBER | Y |
| JPNUM | Job Plan | VARCHAR2(16) | Y |
| X4ATTCAR | Attività caricabile manualmente | NUMBER | Y |
| DESCRIPTION | Description | VARCHAR2(150) | N |
| JPDURATION | Duration | FLOAT | Y |
| LABORCODE | Lead | VARCHAR2(100) | N |
| DOWNTIME | Downtime | NUMBER | Y |
| SUPERVISOR | Supervisor | VARCHAR2(100) | N |
| CALENDAR | Calendar | VARCHAR2(8) | N |
| PRIORITY | WO Priority | NUMBER | N |
| CREWID | Crew | VARCHAR2(12) | N |
| ORGID | Organization | VARCHAR2(8) | N |
| SITEID | Site | VARCHAR2(8) | N |
| OWNER | Owner | VARCHAR2(100) | N |
| PERSONGROUP | Work Group | VARCHAR2(10) | N |
| OWNERGROUP | Owner Group | VARCHAR2(10) | N |
| LANGCODE | Language Code | VARCHAR2(4) | Y |
| STATUS | Status | VARCHAR2(16) | Y |
| INTERRUPTIBLE | Interruptible | NUMBER | Y |
| HASLD | Has Long Description | NUMBER | Y |
| CLASSSTRUCTUREID | Class Structure | VARCHAR2(26) | N |
| TEMPLATETYPE | Template Type | VARCHAR2(16) | Y |
| SUSPENDFLOW | Suspend Flow Control | NUMBER | Y |
| FLOWCONTROLLED | Flow Controlled | NUMBER | Y |
| FLOWACTION | Flow Action | VARCHAR2(30) | N |
| FLOWACTIONASSIST | Flow Action Assist | NUMBER | Y |
| LAUNCHENTRYNAME | Launch Entry Name | VARCHAR2(32) | N |
| ASSIGNEDOWNERGROUP | Assigned Owner Group | VARCHAR2(10) | N |
| INSPFORMNUM | Inspection Form | VARCHAR2(12) | N |
| MODELID | Import ID | VARCHAR2(45) | N |
| PLUSCCHANGEBY | Changed By | VARCHAR2(100) | Y |
| PLUSCCHANGEDATE | Changed Date | DATE | Y |
| PLUSCREVCOM | Description | VARCHAR2(100) | N |
| PLUSCREVNUM | Revision | NUMBER | Y |
| PLUSCSTATUSDATE | Last status change date | DATE | N |
| UNITSOFWORK | Work Unit | VARCHAR2(16) | N |
| UNITSOFWORKMULTIPLIER | Work Unit | NUMBER(10,2) | N |
| CALCMETHOD | Calculation | VARCHAR2(15) | Y |
| CALCLEVEL | Level | VARCHAR2(8) | N |
| CALCAPPLYTO | Type | VARCHAR2(10) | Y |
| CALCEXPRESSION | Expression | VARCHAR2(100) | N |
| DYNAMIC | Is dynamic | NUMBER | Y |
| APPLYNONDYNAMICCALC | Ignore linear measures | NUMBER | Y |
| INCTASKSINSCHED | Include Tasks in Schedule | NUMBER | Y |
| STCONOFFSET | Start Constraint Offset | FLOAT | N |
| FNCONOFFSET | Finish Constraint Offset | FLOAT | N |
| AMCREW | Crew | VARCHAR2(8) | N |
| CREWWORKGROUP | Crew Work Group | VARCHAR2(10) | N |
| REQASSTDWNTIME | Require Asset Downtime | NUMBER | Y |
| APPTREQUIRED | Appointment Required | NUMBER | Y |
| AOS | Requires Asset Downtime | NUMBER | Y |
| AMS | Requires Asset Maintenance Window | NUMBER | Y |
| LOS | Requires Location Downtime | NUMBER | Y |
| LMS | Requires Location Maintenance Window | NUMBER | Y |
| REPAIRLOCFLAG | Repair Facility Required | NUMBER | Y |
| INTSHIFT | Interruptible shift | VARCHAR2(8) | N |
| ROWSTAMP | Campo tecnico Maximo | VARCHAR2(40) | Y |
| X3CLASSECOMP | Classe Componente | VARCHAR2(20) | N |
| X3TIPOCOMP | Tipo componente | VARCHAR2(20) | N |
| X3NORMDIRIF | Normativa di riferimento | VARCHAR2(15) | N |
| X4TIPUDB | Tipologie Unità di Budget | VARCHAR2(10) | N |
| X4INDISPONIBILITA | Indisponibilità | NUMBER | Y |
| X4ATTPREVMETODO | Attività prevalente per metodo | NUMBER | Y |
| X4EFFORT | Effort | FLOAT | N |
| X4DATAINIZIO | Data inizio validità | DATE | N |
| X4DATAFINE | Data fine validità | DATE | N |
| X4ATTALL | ID Attività Standard | VARCHAR2(15) | N |
| X4UM | UM | VARCHAR2(3) | N |
| X4MACROATTIVITA | ID Macro-Attività | VARCHAR2(16) | N |
| X4NOTE | Note | VARCHAR2(150) | N |
| X4TEMPOVAL | Max. tempo di validazione attività | NUMBER | N |
| X4ASSIEME | Assieme | VARCHAR2(35) | N |
| X4SOTTOASSIEME | Sottoassieme | VARCHAR2(50) | N |
| X4TIPOLOGIAASSET | Tipologia Asset | VARCHAR2(192) | N |
| X4PROGRAMMA | Programma | VARCHAR2(20) | N |
| X4ELIVE | Elive | NUMBER | Y |
| X4KPI | X4kpi | NUMBER(10,2) | N |
| X4LIVELLOATTIVITA | X4livelloattivita | NUMBER | N |
| X4WORKFLOW | ID Workflow | NUMBER | N |
| X4OUTSOURCING | Outsourcing | NUMBER | Y |
| X4ATRIDONEITA |  | VARCHAR2(15) | N |
| X4MXL |  | NUMBER | Y |
| X4TOLLERANZA |  | NUMBER | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta: PLUSCHANGEDATE.

La logica di cancellazione è: Logica, sul campo Status. Valore STATUS In (CANCEL) per indicare che la riga è cancellata

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe che hanno pluschangedate>= sysdate -7g.

RPT_DP_ASSET

Questa vista materializzata rappresenta gli asset

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| ASSETNUM | ID Asset | VARCHAR2(25) | Y |
| PARENT | ID Padre dell’asset | VARCHAR2(25) | N |
| SERIALNUM | Numero seriale | VARCHAR2(64) | N |
| ASSETTAG | Tag Asset | VARCHAR2(64) | N |
| LOCATION | Locazione | VARCHAR2(25) | N |
| DESCRIPTION | Descrizione | VARCHAR2(40) | N |
| VENDOR | Venditore | VARCHAR2(12) | N |
| FAILURECODE | Codice fallimento | VARCHAR2(8) | N |
| MANUFACTURER | Produttore | VARCHAR2(12) | N |
| PURCHASEPRICE | Prezzo acquisto | NUMBER(10,2) | Y |
| REPLACECOST | Prezzo sostituzione | NUMBER(10,2) | Y |
| INSTALLDATE | Data installazione | DATE | N |
| WARRANTYEXPDATE |  | DATE | N |
| TOTALCOST | Costo totale | NUMBER(10,2) | Y |
| YTDCOST | Costo YTD | NUMBER(10,2) | Y |
| BUDGETCOST | Costo Budget | NUMBER(10,2) | Y |
| CALNUM | Num. Calcolo | VARCHAR2(8) | N |
| ISRUNNING | Flag Is Running | NUMBER | Y |
| ITEMNUM | Numero Item | VARCHAR2(30) | N |
| UNCHARGEDCOST | Costi uncharged | NUMBER(10,2) | Y |
| TOTUNCHARGEDCOST | Costi uncharged totali | NUMBER(10,2) | Y |
| TOTDOWNTIME | Downtime totale | FLOAT | Y |
| STATUSDATE | Data stato | DATE | N |
| CHANGEDATE | Data ultima modifica | DATE | Y |
| CHANGEBY | Autore ultima modifica | VARCHAR2(100) | Y |
| EQ1 |  | VARCHAR2(10) | N |
| EQ2 |  | VARCHAR2(10) | N |
| EQ3 |  | VARCHAR2(10) | N |
| EQ4 |  | VARCHAR2(10) | N |
| EQ5 |  | NUMBER(10,2) | N |
| EQ6 |  | DATE | N |
| EQ7 |  | NUMBER(15,2) | N |
| EQ8 |  | VARCHAR2(10) | N |
| EQ9 |  | VARCHAR2(30) | N |
| EQ10 |  | VARCHAR2(10) | N |
| EQ11 |  | VARCHAR2(10) | N |
| EQ12 |  | NUMBER(10,2) | N |
| EQ23 |  | DATE | N |
| EQ24 |  | NUMBER(15,2) | N |
| PRIORITY | Priorità | NUMBER | N |
| INVCOST | Costo inv. | NUMBER(10,2) | Y |
| GLACCOUNT |  | VARCHAR2(110) | N |
| ROTSUSPACCT |  | VARCHAR2(110) | N |
| CHILDREN | Figlio? | NUMBER | Y |
| BINNUM |  | VARCHAR2(8) | N |
| DISABLED | Disabilitato? | NUMBER | Y |
| CLASSSTRUCTUREID | ID Classstructure | VARCHAR2(26) | N |
| SOURCESYSID | ID SOURCESYS | VARCHAR2(10) | N |
| OWNERSYSID | ID OWNERSYS | VARCHAR2(10) | N |
| EXTERNALREFID | ID EXTERNALREF | VARCHAR2(10) | N |
| SITEID | ID Sito | VARCHAR2(8) | Y |
| ORGID | ID Organizzazione | VARCHAR2(8) | Y |
| AUTOWOGEN |  | NUMBER | Y |
| ITEMSETID | ID ITEMSET | VARCHAR2(8) | N |
| CONDITIONCODE |  | VARCHAR2(30) | N |
| GROUPNAME | Nome gruppo | VARCHAR2(10) | N |
| ASSETTYPE | Tipo asset | VARCHAR2(50) | N |
| USAGE |  | VARCHAR2(15) | N |
| STATUS | Stato | VARCHAR2(20) | N |
| MAINTHIERCHY |  | NUMBER | Y |
| ASSETID | ID Asset | NUMBER | Y |
| MOVED |  | NUMBER | Y |
| ASSETUID | UID ASSET | NUMBER | Y |
| LANGCODE |  | VARCHAR2(4) | Y |
| TOOLRATE |  | NUMBER(10,2) | N |
| ITEMTYPE | Tipo Item | VARCHAR2(15) | N |
| ANCESTOR |  | VARCHAR2(25) | N |
| SENDERSYSID | ID SENDERSYS | VARCHAR2(50) | N |
| SHIFTNUM |  | VARCHAR2(8) | N |
| TOOLCONTROLACCOUNT |  | VARCHAR2(110) | N |
| HASLD | Campo di sistema | NUMBER | Y |
| DIRECTION |  | VARCHAR2(30) | N |
| STARTMEASURE | Misura iniziale | NUMBER(10,2) | N |
| ENDMEASURE | Misura finale | NUMBER(10,2) | N |
| ISLINEAR |  | NUMBER | Y |
| ENDDESCRIPTION | Descrizione finale | VARCHAR2(100) | N |
| STARTDESCRIPTION | Descrizione iniziale | VARCHAR2(100) | N |
| LRM |  | VARCHAR2(25) | N |
| DEFAULTREPFACSITEID | ID DEFAULTREPFACSITEID | VARCHAR2(8) | N |
| DEFAULTREPFAC |  | VARCHAR2(25) | N |
| RETURNEDTOVENDOR |  | NUMBER | Y |
| DEPRECATIONPENDING |  | NUMBER | N |
| ASSETHEALTH |  | NUMBER | N |
| EXPECTEDLIFE |  | NUMBER | N |
| INDUSTRYHEALTHSCORE |  | FLOAT | N |
| INDUSTRYHEALTHGRADE |  | VARCHAR2(20) | N |
| ESTENDOLIFE |  | DATE | N |
| EXPECTEDLIFEDATE |  | DATE | N |
| DEVICEID | ID DEVICE | VARCHAR2(50) | N |
| DEVICETYPE | Tipo DEVICE | VARCHAR2(50) | N |
| WELL_MAINTAINED |  | NUMBER | N |
| PMNEXTFAILDT |  | DATE | N |
| TLOAMHASH |  | VARCHAR2(192) | N |
| TLOAMPARTITION |  | NUMBER | Y |
| MODELID | ID MODEL | VARCHAR2(45) | N |
| PRODUCTID | ID PRODUCT | VARCHAR2(12) | N |
| PLUSASSETDEPT |  | VARCHAR2(80) | N |
| PLUSCLASS |  | VARCHAR2(12) | N |
| PLUSDUEDATE |  | DATE | N |
| PLUSCISCONDESC |  | VARCHAR2(250) | N |
| PLUSCISCONTAM |  | NUMBER | Y |
| PLUSCISINHOUSECAL |  | NUMBER | Y |
| PLUSCISMTE |  | NUMBER | Y |
| PLUSCISMTECLASS |  | VARCHAR2(80) | N |
| PLUSCLOOPNUM |  | VARCHAR2(15) | N |
| PLUSCMODELNUM |  | VARCHAR2(8) | N |
| PLUSCOPRGEEU |  | VARCHAR2(15) | N |
| PLUSCOPRGEFROM |  | VARCHAR2(15) | N |
| PLUSCOPRGETO |  | VARCHAR2(15) | N |
| PLUSCPPHYLOC |  | VARCHAR2(250) | N |
| PLUSCPMEXTDATE |  | NUMBER | Y |
| PLUSCSOLUTION |  | NUMBER | Y |
| PLUSCSUMDIR |  | VARCHAR2(12) | N |
| PLUSCSUMEU |  | VARCHAR2(15) | N |
| PLUSCSUMREAD |  | VARCHAR2(15) | N |
| PLUSCSUMSPAN |  | VARCHAR2(15) | N |
| PLUSCSUMURV |  | VARCHAR2(15) | N |
| PLUSCSVENDOR |  | VARCHAR2(12) | N |
| ISCALIBRATION |  | NUMBER | Y |
| TEMPLATEID | ID TEMPLATE | VARCHAR2(12) | N |
| PLUSCLPLOC |  | VARCHAR2(25) | N |
| SADDRESSCODE |  | VARCHAR2(25) | N |
| X3SAPID | SAP ID | VARCHAR2(30) | N |
| X3SWERK | Dipartimento di trasmisione | VARCHAR2(4) | N |
| X3ZZSOCP | Società proprietaria | VARCHAR2(4) | N |
| X3KOSTL | Centro di costo | VARCHAR2(10) | N |
| X3MSGRP |  | VARCHAR2(8) | N |
| X3BUKRS | Società | VARCHAR2(4) | N |
| X3KOKRS | Centro di costo | VARCHAR2(4) | N |
| X3INGRP | Unità impianti dell’asset | VARCHAR2(3) | N |
| X3ROOTASSETNUM |  | VARCHAR2(25) | N |
| X3GEWRK | Centro di lavoro responsabile | VARCHAR2(8) | N |
| LOADED |  | NUMBER | Y |
| X3EQFNR |  | VARCHAR2(30) | N |
| X3DADOPPIATERNA |  | NUMBER | Y |
| X3SAPIDETL |  | VARCHAR2(30) | N |
| X3SAPIDEQ |  | VARCHAR2(30) | N |
| X3FUNZIONEELEMENTO | Funzione elemento | VARCHAR2(100) | N |
| X3RIFERIMENTOFASE | Riferimento fase | VARCHAR2(4) | N |
| X3COSTRUTTORECOMPONENTE | Costruttore componente | VARCHAR2(30) | N |
| X3TIPOCOMPONENTE | Tipo componente | VARCHAR2(20) | N |
| METHODNAME | Nome metodo | VARCHAR2(50) | N |
| LASTAHSCOREDATE |  | DATE | N |
| AHCAWONUM |  | VARCHAR2(25) | N |
| AHCAWOSTATUS |  | VARCHAR2(16) | N |
| AHCAPMNUM |  | VARCHAR2(8) | N |
| AHCAJPNUM |  | VARCHAR2(20) | N |
| AHCAINSPFORMNUM |  | VARCHAR2(12) | N |
| AHCAREVISION |  | NUMBER | N |
| AHCARESULTNUM |  | VARCHAR2(12) | N |
| AHCAINSPSTATUS |  | VARCHAR2(20) | N |
| AHCALASTCOMPDATE |  | DATE | N |
| AHCAPMNEXTDUEDATE |  | DATE | N |
| AHCAWORKTYPE |  | VARCHAR2(5) | N |
| ADJUSTEDLIFEDATE |  | DATE | N |
| ROWSTAMP | Campo di sistema | VARCHAR2(40) | Y |
| X4LOTTOTECNICO | Lotto tecnico omogeneo | VARHCAR2(10) | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta: CHANGEDATE.

La logica di cancellazione è: Logica, sul campo Status.

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe che hanno changedate>= sysdate -7g.

RPT_DP_ASSETSPEC

Questa vista materializzata rappresenta le caratteristiche asset

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| ASSETNUM | Numero Asset | VARCHAR2(25) | Y |
| ASSETATTRID | ID Attributo | VARCHAR2(50) | Y |
| CLASSSTRUCTUREID | ID CLASSSTRUCTURE | VARCHAR2(26) | Y |
| INHERITEDFROMITEM |  | NUMBER | Y |
| ITEMSPECVALCHANGED |  | NUMBER | Y |
| DISPLAYSEQUENCE |  | NUMBER | Y |
| NUMVALUE | Valore numeric | NUMBER(30,10) | N |
| MEASUREUNITID | Unità di misura | VARCHAR2(16) | N |
| ALNVALUE | Valore alfanumerico | VARCHAR2(254) | N |
| CHANGEDATE | Data ultima modifica | DATE | Y |
| CHANGEBY | Autore ultima modifica | VARCHAR2(100) | Y |
| ES01 |  | VARCHAR2(10) | N |
| ES02 |  | VARCHAR2(10) | N |
| ES03 |  | VARCHAR2(10) | N |
| ES04 |  | DATE | N |
| ES05 |  | NUMBER(15,2) | N |
| SITEID | ID Sito | VARCHAR2(8) | Y |
| ORGID | ID Organizzazione | VARCHAR2(8) | Y |
| SECTION |  | VARCHAR2(10) | N |
| ASSETSPECID |  | NUMBER | Y |
| MANDATORY |  | NUMBER | Y |
| TABLEVALUE | Valore tabella | VARCHAR2(254) | N |
| LINKEDTOATTRIBUTE |  | VARCHAR2(50) | N |
| LINKEDTOSECTION |  | VARCHAR2(10) | N |
| STARTASSETFEATUREID | ID STARTASSETFEATURE | NUMBER | N |
| STARTMEASURE |  | NUMBER(10,2) | N |
| STARTOFFSET |  | NUMBER(10,2) | N |
| ENDASSETFEATUREID | ID ENDASSETFEATURE | NUMBER | N |
| ENDMEASURE |  | NUMBER(10,2) | N |
| ENDOFFSET |  | NUMBER(10,2) | N |
| CONTINUOUS |  | NUMBER | Y |
| ENDYOFFSET |  | NUMBER(10,2) | N |
| STARTYOFFSET |  | NUMBER(10,2) | N |
| ENDZOFFSET |  | NUMBER(10,2) | N |
| STARTZOFFSET |  | NUMBER(10,2) | N |
| STARTYOFFSETREF |  | VARCHAR2(30) | N |
| STARTZOFFSETREF |  | VARCHAR2(30) | N |
| ENDYOFFSETREF |  | VARCHAR2(30) | N |
| ENDZOFFSETREF |  | VARCHAR2(30) | N |
| BASEMEASUREUNITID | ID BASEMEASUREUNIT | VARCHAR2(16) | N |
| STARTBASEMEASURE |  | NUMBER(10,2) | N |
| ENDBASEMEASURE |  | NUMBER(10,2) | N |
| STARTMEASUREUNITID | ID STARTMEASURE | VARCHAR2(16) | N |
| ENDMEASUREUNITID | ID ENDMEASUREUNIT | VARCHAR2(16) | N |
| STARTOFFSETUNITID | ID STARTOFFSETUNIT | VARCHAR2(16) | N |
| ENDOFFSETUNITID | ID ENDOFFSETUNIT | VARCHAR2(16) | N |
| LINEARASSETSPECID | ID LINEARASSETSPEC | NUMBER | Y |
| ROWSTAMP | Campo di sistema | VARCHAR2(40) | Y |
| DATEVALUE | Valore data | DATE | N |
| X3DATAMODIFICA | Data modifica | DATE | N |
| ANYWHEREREFID | ID ANYWHEREF | NUMBER | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta: CHANGEDATE.

La logica di cancellazione è: Logica, sul campo Status e Assetnum della tabella ASSET.

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe che hanno changedate>= sysdate -7g.

RPT_DP_PERSONGROUP

Questa vista materializzata rappresenta UI e gruppi utenti

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| PERSONGROUPID | ID PERSONGROUP | NUMBER | Y |
| PERSONGROUP | Gruppo utenti | VARCHAR2(10) | Y |
| DESCRIPTION | Descrizione | VARCHAR2(100) | N |
| LANGCODE |  | VARCHAR2(4) | Y |
| HASLD | Campo di sistema | NUMBER | Y |
| ISCREWWORKGROUP |  | NUMBER | Y |
| ROWSTAMP | Campo di sistema | VARCHAR2(40) | Y |
| X3PARENTPERSONGROUP | Gruppo di persone padre | VARCHAR2(10) | N |
| X4UDB | Flag per specificare se persongroup è un UDB | NUMBER | Y |
| X4TIPUDB | Tipologie Unità di budget | VARCHAR2(10) | N |
| X4TRASC |  | VARCHAR2(50) | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata non sono presenti campi tecnici usabili per estrarre in modalità delta.

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Sarà aggiornata ogni giorno la tabella

RPT_DP_X4POLICY

Questa vista materializzata rappresenta le policy

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| X4POLICYID | ID X4POLICY | NUMBER | Y |
| DESCRIPTION | Descrizione | VARCHAR2(250) | N |
| CHANGEDBY | Modificato da… | VARCHAR2(100) | N |
| CHANGEDDATE | Data ultima modifica | DATE | N |
| DATAFINEVAL | Data fine validazione | DATE | N |
| DATAINIZIOVAL | Data inizio validazione | DATE | N |
| DIFFERIBILITA | Differibilità | NUMBER | N |
| ELEGGIBILITA | Eleggibilità | NUMBER | Y |
| FREQUENZA | Frequenza | NUMBER | N |
| IDATTIVITA | ID Attività output | VARCHAR2(20) | N |
| IDPOLICY | ID Policy | VARCHAR2(20) | N |
| IDTIMING | ID Timing | VARCHAR2(3) | N |
| LASTREVIEWDATE | Data ultima revisione | DATE | N |
| MAXANTICIPO | Massimo anticipo | NUMBER | N |
| MAXPOSTICIPO | Massimo posticipo | NUMBER | N |
| TIPOLOGIAASSET | Elemento Asset | VARCHAR2(192) | N |
| HASLD | Campo di sistema | NUMBER | Y |
| ROWSTAMP | Campo di sistema | VARCHAR2(40) | Y |
| LIVPOLICY | Livello Policy | NUMBER | N |
| LIVCOMB | Livello Combinazione input | NUMBER | N |
| UM | Unità di misura | VARCHAR2(3) | N |
| TIPOPOLICY | Tipo Policy | VARCHAR2(15) | N |
| POLICYAGGR | Policy aggregata | NUMBER | Y |
| LIVAGGR | Livello aggregazione policy | NUMBER | N |
| CAMPAGNA | Campagna | NUMBER | Y |
| COMBINPUT | Combinazione input(formula) | VARCHAR2(1000) | N |
| STATUS | Stato | VARCHAR2(20) | N |
| X4RIFTEMPORALE | Riferimento temporale | VARCHAR2(50) | N |
| X4SOTTOPROGRAMMA | Sotto-programma | VARCHAR2(50) | N |
| X4CLUSTER | Cluster | VARCHAR2(50) | N |
| SITEID | Site ID associato alla tipologia di asset | VARCHAR2(8) | N |
| APARTIREDA | A partire da | DATE | N |
| VERSION | Versione della policy | NUMBER | N |
| IDMACROATTIVITA | ID Macro-attività associata alla policy | VARCHAR2(16) | N |
| LOGICATRONCO | Flag se policy è soggetta tronco | NUMBER | Y |
| X4MXL | Campo di riferimento per logiche DSS su inserimento massivo | NUMBER | Y |
| X4MXL2 | Campo di inserimento massivo | NUMBER | Y |
| COMBINPUT_S | Valore di combinput ripulito dai caratteri speciali | VARCHAR2(1000) | N |
| HASINPUTS |  | NUMBER | Y |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta: CHANGEDATE.

La logica di cancellazione è: Logica, sul campo Status. Valore STATUS In (NON ATTIVA) per indicare che la riga è cancellata

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe che hanno changedate>= sysdate -7g.

RPT_DP_ALNDOMAIN

Questa vista materializzata rappresenta i domini

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| DOMAINID | ID univoco del dominio | VARCHAR2(18) | Y |
| VALUE | Valore del dominio | VARCHAR2(254) | Y |
| DESCRIPTION | Descrizione | VARCHAR2(140) | N |
| SITEID | Sede dell’asset | VARCHAR2(8) | N |
| ORGID | ID Organizzazione | VARCHAR2(8) | N |
| ALNDOMAINID | ID ALNDOMAIN | NUMBER | Y |
| VALUEID | Concat DomainID\|Value | VARCHAR2(300) | Y |
| ROWSTAMP | Campo di sistema | VARCHAR2(40) | Y |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata non sono presenti campi tecnici usabili per estrarre in modalità delta.

Ogni giorno questa vista materializzata viene rigenerata, estraendo i dati dalla rispettiva tabella sul DB EAM

RPT_DP_ITEM

Questa vista materializzata rappresenta gli item

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| ITEMNUM | Resource ID | VARCHAR2(30) | Y |
| DESCRIPTION | Descrizione | VARCHAR2(40) | N |
| ROTATING |  | NUMBER | Y |
| LOTTYPE |  | VARCHAR2(16) | Y |
| CAPITALIZED |  | NUMBER | Y |
| MSDSNUM |  | VARCHAR2(10) | N |
| OUTSIDE |  | NUMBER | Y |
| IN19 |  | VARCHAR2(10) | N |
| IN20 |  | VARCHAR2(10) | N |
| IN21 |  | VARCHAR2(10) | N |
| IN22 |  | DATE | N |
| IN23 |  | NUMBER(15,2) | N |
| SPAREPARTAUTOADD |  | NUMBER | Y |
| CLASSSTRUCTUREID | ID CLASSSTRUCTURE | VARCHAR2(26) | N |
| INSPECTIONREQUIRED |  | NUMBER | Y |
| SOURCESYSID | ID SOURCESYS | VARCHAR2(10) | N |
| OWNERSYSID | ID OWNERSYS | VARCHAR2(10) | N |
| EXTERNALREFID | ID EXTERNALREF | VARCHAR2(10) | N |
| IN24 |  | VARCHAR2(10) | N |
| IN25 |  | VARCHAR2(10) | N |
| IN26 |  | VARCHAR2(10) | N |
| IN27 |  | VARCHAR2(10) | N |
| SENDERSYSID | ID SENDERSYS | VARCHAR2(50) | N |
| ITEMSETID | ID ITEMSET | VARCHAR2(8) | Y |
| ORDERUNIT |  | VARCHAR2(16) | N |
| ISSUEUNIT |  | VARCHAR2(16) | N |
| CONDITIONENABLED |  | NUMBER | Y |
| GROUPNAME | Nome gruppo merce | VARCHAR2(10) | N |
| METERNAME |  | VARCHAR2(10) | N |
| COMMODITYGROUP | Gruppo merce | VARCHAR2(8) | N |
| COMMODITY |  | VARCHAR2(8) | N |
| ITEMTYPE | Tipo Item | VARCHAR2(15) | Y |
| PRORATE |  | NUMBER | Y |
| ITEMID | ID Item | NUMBER | Y |
| ISKIT |  | NUMBER | Y |
| LANGCODE |  | VARCHAR2(4) | Y |
| ATTACHONISSUE |  | NUMBER | Y |
| HASLD | Campo di sistema | NUMBER | Y |
| MAXISSUE |  | NUMBER(15,2) | N |
| STATUS | Stato | VARCHAR2(16) | Y |
| STATUSDATE |  | DATE | Y |
| HARDRESISSUE |  | NUMBER | Y |
| TAXEXEMPT |  | NUMBER | Y |
| RECEIPTTOLERANCE |  | NUMBER(15,2) | N |
| PLUSCISINHOUSECAL |  | NUMBER | Y |
| PLUSCISMTE |  | NUMBER | Y |
| PLUSCISMTECLASS |  | VARCHAR2(80) | N |
| PLUSCSOLUTION |  | NUMBER | Y |
| ISCREW |  | NUMBER | Y |
| X3DEFAULTCHILD |  | NUMBER | Y |
| X3PARENTITEM |  | VARCHAR2(30) | N |
| ROWSTAMP | Campo di sistema | VARCHAR2(40) | Y |
| X4DC | VdC | VARCHAR2(20) | N |
| X4TRISORSA | Tipo Risorsa | VARCHAR2(20) | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata non sono presenti campi tecnici usabili per estrarre in modalità delta.

Ogni giorno questa vista materializzata viene rigenerata, estraendo i dati dalla rispettiva tabella sul DB EAM

RPT_DP_CLASSSTRUCTURE

Questa vista materializzata rappresenta la classificazione asset

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| CLASSSTRUCTUREID | ID CLASSSTRUCTURE | VARCHAR2(26) | Y |
| DESCRIPTION | Descrizione | VARCHAR2(254) | N |
| GENASSETDESC |  | NUMBER | Y |
| ORGID | ID Organizzazione | VARCHAR2(8) | N |
| PARENT |  | VARCHAR2(26) | N |
| CLASSIFICATIONID | Classificazione asset associato all’OdL e mostrato nella sezione “Perimetro di pianificazione” del MPE | VARCHAR2(192) | Y |
| USECLASSINDESC |  | NUMBER | Y |
| TYPE | Tipo | VARCHAR2(10) | N |
| SITEID | ID Sito | VARCHAR2(8) | N |
| CLASSSTRUCTUREUID | UID CLASSSTRUCTURE | NUMBER | Y |
| HASCHILDREN |  | NUMBER | Y |
| LANGCODE |  | VARCHAR2(4) | Y |
| HASLD | Campo di sistema | NUMBER | Y |
| SHOWINASSETOPO |  | NUMBER | N |
| SHOW |  | NUMBER | Y |
| SORTORDER |  | NUMBER | N |
| ROWSTAMP | Campo di sistema | VARCHAR2(40) | Y |
| X3GESTSTAT |  | NUMBER | Y |
| X3CLASSIDREL |  | VARCHAR2(30) | N |
| X3MAXNUM |  | NUMBER | N |
| X4ELEMENTO |  | VARCHAR2(40) | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata non sono presenti campi tecnici usabili per estrarre in modalità delta.

Ogni giorno questa vista materializzata viene rigenerata, estraendo i dati dalla rispettiva tabella sul DB EAM

RPT_DP_X4DIFFOR_WO

Questa vista materializzata rappresenta le difformità rivelate

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| X4DIFFOR_WOID | ID X4DIFFOR_WO | NUMBER | Y |
| ASSETNUM | Asset | VARCHAR2(25) | N |
| IDDIFFORMITA | ID Difformità | VARCHAR2(30) | N |
| GRAVITA | Gravita | VARCHAR2(50) | N |
| SITEID | ID Sito | VARCHAR2(8) | N |
| STATUS | Stato | VARCHAR2(50) | N |
| WONUM | Ordine di Lavoro | VARCHAR2(25) | N |
| ROWSTAMP | Campo di sistema | VARCHAR2(40) | Y |
| DESCRIPTION | Descrizione | VARCHAR2(50) | N |
| DIFFORMITA | Difformità | VARCHAR2(50) | N |
| X4DIFFORSTATID | ID X4DIFFORSTAT | NUMBER | N |
| DATAULTIMARIL | Data ultimo rilevamento | DATE | N |
| RISOLTA | Flag | NUMBER | Y |
| FASE | Fase | VARCHAR2(4) | N |
| IDMISURA | ID Misura | VARCHAR2(30) | N |
| VALMISURA | Valore misura | NUMBER | N |
| X4NOTEMIG | Note MIG | VARCHAR2(250) | N |
| NOTE | Note | VARCHAR2(150) | N |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

In questa vista materializzata sono presenti campi tecnici usabili per estrarre in modalità delta: DATAULTIMARIL.

La logica di cancellazione non è prevista.

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Ogni giorno saranno copiate le righe che hanno changedate>= sysdate -7g.

RPT_DP_A_WPLABOR

Questa vista materializzata rappresenta lo storico WPLABOR

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| AMCREW |  | VARCHAR2(8) | N |
| AMCREWTYPE |  | VARCHAR2(8) | N |
| APPTREQUIRED |  | NUMBER | N |
| CONTRACTNUM |  | VARCHAR2(8) | N |
| CRAFT | Qualifica | VARCHAR2(8) | N |
| CREWWORKGROUP |  | VARCHAR2(10) | N |
| JOBLABORID | ID JOBLABOR | NUMBER | N |
| LABORCODE | Codice Labor | VARCHAR2(8) | N |
| LABORHRS | Durata esecuzione | FLOAT | N |
| ORGID | ID Organizzazione | VARCHAR2(8) | N |
| QUANTITY | Quantità | NUMBER | N |
| RATE | Tariffa oraria | NUMBER(10,2) | N |
| RATEHASCHANGED |  | NUMBER | N |
| SITEID | ID Sito | VARCHAR2(8) | N |
| SKILLLEVEL |  | VARCHAR2(15) | N |
| VENDOR |  | VARCHAR2(12) | N |
| WONUM |  | VARCHAR2(25) | N |
| WPL1 |  | VARCHAR2(10) | N |
| WPL2 |  | DATE | N |
| WPL3 |  | VARCHAR2(10) | N |
| WPL4 |  | VARCHAR2(10) | N |
| WPL5 |  | VARCHAR2(10) | N |
| WPL6 |  | NUMBER(15,2) | N |
| WPL7 |  | VARCHAR2(10) | N |
| WPL8 |  | VARCHAR2(10) | N |
| WPL9 |  | VARCHAR2(10) | N |
| WPLABORID | ID WPLABOR | VARCHAR2(20) | N |
| WPLABORUID | UID WPLABOR | NUMBER | N |
| X3DATAFINERISORSA | Data fine risorsa | DATE | N |
| X3DATAINIZIORISORSA | Data inizio risorsa | DATE | N |
| X3OREESEC | Ore esecuzione | FLOAT | N |
| X3OREPOSTESEC |  | FLOAT | N |
| X3OREPREESE |  | FLOAT | N |
| X3ORETRASF | Ore trasferimento | FLOAT | N |
| X4CHANGEDATE |  | DATE | N |
| X4DATAFINERISORSA | Data fine risorsa | DATE | N |
| X4DATAINIZIORISORSA | Data inizio risorsa | DATE | N |
| X4OREESEC | Ore esecuzione | FLOAT | N |
| X4OREPOSTESEC |  | FLOAT | N |
| X4OREPREESE |  | FLOAT | N |
| X4ORETRASF | Ore trasferimento | FLOAT | N |
| X4TITRIS | Titris della risorsa | VARCHAR2(10) | N |
| EAUDITUSERNAME |  | VARCHAR2(100) | Y |
| EAUDITTIMESTAMP |  | DATE | Y |
| EAUDITTYPE |  | VARCHAR2(1) | Y |
| EAUDITTRANSID |  | VARCHAR2(40) | Y |
| ESIGTRANSID |  | VARCHAR2(40) | N |
| ROWSTAMP | Campo di sistema | VARCHAR2(40) | Y |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Vengono riportate le sole righe cancellate (ottenute filtrando per campo EAUDITTYPE= D) con profondità 7g.

RPT_DP_A_WPSERVICE

Questa vista materializzata rappresenta lo storico WPSERVICE

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| AMCREW |  | VARCHAR2(8) | N |
| AMCREWTYPE |  | VARCHAR2(8) | N |
| CATALOGCODE |  | VARCHAR2(30) | N |
| CONDITIONCODE |  | VARCHAR2(30) | N |
| DESCRIPTION | Descrizione | VARCHAR2(40) | N |
| DIRECTREQ |  | NUMBER | N |
| HASLD | Campo di sistema | NUMBER | N |
| HOURS |  | FLOAT | N |
| ISSUETO |  | VARCHAR2(8) | N |
| ITEMNUM | Codice risorsa esterna/ricavo | VARCHAR2(30) | N |
| ITEMQTY |  | NUMBER(15,2) | N |
| ITEMSETID | ID ITEMSET | VARCHAR2(8) | N |
| JOBITEMID | IT JOBITEM | NUMBER | N |
| LANGCODE |  | VARCHAR2(4) | N |
| LINECOST | Costo linea | NUMBER(10,2) | N |
| LINETYPE | Tipo linea | VARCHAR2(15) | N |
| LOCATION |  | VARCHAR2(25) | N |
| MANUFACTURER |  | VARCHAR2(12) | N |
| MKTPLCITEM |  | NUMBER | N |
| MODELNUM |  | VARCHAR2(8) | N |
| ORDERUNIT |  | VARCHAR2(16) | N |
| ORGID | ID Organizzazione | VARCHAR2(8) | N |
| PR |  | VARCHAR2(8) | N |
| PRLINENUM |  | NUMBER | N |
| RATE |  | NUMBER(10,2) | N |
| RATEHASCHANGED |  | NUMBER | N |
| REQUESTBY |  | VARCHAR2(100) | N |
| REQUESTNUM |  | VARCHAR2(20) | N |
| REQUIREDATE |  | DATE | N |
| RESTYPE |  | VARCHAR2(16) | N |
| SITEID | ID Sito | VARCHAR2(8) | N |
| STORELOCSITE |  | VARCHAR2(8) | N |
| UNITCOST |  | NUMBER(10,2) | N |
| UNITCOSTHASCHANGED |  | NUMBER | N |
| VENDOR | Venditore | VARCHAR2(12) | N |
| VENDORPACKCODE |  | VARCHAR2(12) | N |
| VENDORPACKQUANTITY |  | VARCHAR2(12) | N |
| VENDORUNITCOST |  | NUMBER(10,2) | N |
| VENDORWAREHOUSE |  | VARCHAR2(12) | N |
| WONUM |  | VARCHAR2(25) | N |
| WPITEMID | ID WPITEM | NUMBER | N |
| WPLABORREF |  | NUMBER | N |
| WPM1 |  | VARCHAR2(10) | N |
| WPM2 |  | VARCHAR2(10) | N |
| WPM3 |  | NUMBER(15,2) | N |
| WPM4 |  | VARCHAR2(10) | N |
| WPM5 |  | NUMBER(10,2) | N |
| WPM6 |  | VARCHAR2(10) | N |
| X4DATAFINERISORSA | Data fine | DATE | N |
| X4DATAINIZIORISORSA | Data inizio | DATE | N |
| EAUDITUSERNAME |  | VARCHAR2(100) | Y |
| EAUDITTIMESTAMP |  | DATE | Y |
| EAUDITTYPE |  | VARCHAR2(1) | Y |
| EAUDITTRANSID | ID EAUDITTRANS | VARCHAR2(40) | Y |
| ESIGTRANSID | ID ESIGTRANS | VARCHAR2(40) | N |
| ROWSTAMP | Campo di sistema | VARCHAR2(40) | Y |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Vengono riportate le sole righe cancellate (ottenute filtrando per campo EAUDITTYPE= D) con profondità 7g.

RPT_DP_A_ASSIGNMENT

Questa vista materializzata rappresenta lo storico ASSIGNMENT

| Campo | Descrizione | Tipo | Obbligatorietà |
| --- | --- | --- | --- |
| AMCREW |  | VARCHAR2(8) | N |
| AMCREWTYPE |  | VARCHAR2(8) | N |
| APPOINTMENT |  | NUMBER | N |
| APPTREQUIRED |  | NUMBER | N |
| ASSIGNMENTID | ID ASSIGNMENT | NUMBER | N |
| CHANGEBY | Autore ultima modifica | VARCHAR2(100) | N |
| CHANGEDATE | Data ultima modifica | DATE | N |
| CONTRACTNUM |  | VARCHAR2(8) | N |
| CRAFT |  | VARCHAR2(8) | N |
| CREWWORKGROUP |  | VARCHAR2(10) | N |
| FINISHDATE |  | DATE | N |
| LABORCODE | Matricola | VARCHAR2(8) | N |
| LABORHRS |  | FLOAT | N |
| ORGID | ID Organizzazione | VARCHAR2(8) | N |
| PARENTASSIGNID | ID PARENTASSIGN | NUMBER | N |
| RESOURCELOCK |  | NUMBER | N |
| SCHEDULEDATE |  | DATE | N |
| SITEID |  | VARCHAR2(8) | N |
| SKILLLEVEL |  | VARCHAR2(15) | N |
| STARTDATE |  | DATE | N |
| STATUS | Stato dell’assignment | VARCHAR2(12) | N |
| VENDOR |  | VARCHAR2(12) | N |
| WONUM | ID OdL | VARCHAR2(25) | N |
| WORKDATE |  | DATE | N |
| WORKLOG |  | NUMBER | N |
| WPLABORID | ID WPLABOR | VARCHAR2(20) | N |
| EAUDITUSERNAME |  | VARCHAR2(100) | Y |
| EAUDITTIMESTAMP |  | DATE | Y |
| EAUDITTYPE |  | VARCHAR2(1) | Y |
| EAUDITTRANSID | ID EAUDITTRANS | VARCHAR2(40) | Y |
| ESIGTRANSID | ID ESIGTRANS | VARCHAR2(40) | N |
| ROWSTAMP | Campo di sistema | VARCHAR2(40) | Y |
| DP_CORRELATION_ID | Codice identificativo di X4_DP_REPORTING_TRACE | VARCHAR2(50) | N |

Il campo tecnico sarà copiato su tabelle di staging dedicate alla reportistica con la seguente logica:

Vengono riportate le sole righe cancellate (ottenute filtrando per campo EAUDITTYPE= D) con profondità 7g.

Ecco un esempio di script SQL eseguito per creare una vista materializzata:

| CREATE MATERIALIZED VIEW RPT_DP_WPLABOR BUILD IMMEDIATE REFRESH COMPLETE ON DEMAND WITH ROWID AS SELECT W.*, D.DP_CORRELATION_ID FROM WPLABOR W LEFT JOIN ( SELECT DP_CORRELATION_ID, DATE_REFRESH FROM ( SELECT DP_CORRELATION_ID, DATE_REFRESH, ROW_NUMBER() OVER (ORDER BY DATE_REFRESH DESC, DP_CORRELATION_ID DESC) AS rn FROM X4_DP_REPORTING_TRACE ) WHERE rn = 1 ) D ON 1=1 WHERE W.X4CHANGEDATE >= SYSDATE - ${DAYS_BACK}; |
| --- |

Le viste materializzate non si aggiornano automaticamente alla modifica dei dati presenti nelle tabelle del database EAM, ma vengono aggiornate con frequenza giornaliera dal pod eam-reporting-loader, il quale si occupa di inviare le richieste necessarie all’aggiornamento dei dati delle viste materializzate.

Come si può notare, per la creazione della vista materializzata viene usato un parametro configurabile(attualmente configurato per avere profondità di 7gg).

Inoltre, quando viene aggiornata la vista materializzata, tutti i dati presenti vengono sostituiti con quelli più recenti provenienti dal database EAM, garantendo un allineamento costante e una maggiore coerenza con le tabelle originali.

Al momento della creazione, i dati sono immediatamente disponibili nella vista materializzata, evitando ritardi nella reportistica esterna e assicurando fin da subito l’accesso alle informazioni necessarie.

#### Viste

Le viste hanno gli stessi campi della vista materializzata su cui sono basate

RPT_DP_WORKORDER_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_WORKORDER

RPT_DP_WPLABOR_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_WPLABOR

RPT_DP_WPSERVICE_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_WPSERVICE

RPT_DP_ASSIGNMENT_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_ASSIGNMENT

RPT_DP_LABTRANS_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_LABTRANS

RPT_DP_X4INTERFSTAT_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_X4INTERFSTAT

RPT_DP_X4DIFFORSTAT_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_X4DIFFORSTAT

RPT_DP_JOBPLAN_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_JOBPLAN

RPT_DP_ASSET_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_ASSET

RPT_DP_ASSETSPEC_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_ASSETSPEC

RPT_DP_PERSONGROUP_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_PERSONGROUP

RPT_DP_X4POLICY_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_X4POLICY

RPT_DP_ALNDOMAIN_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_ALNDOMAIN

RPT_DP_ITEM_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_ITEM

RPT_DP_CLASSSTRUCTURE_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_CLASSSTRUCTURE

RPT_DP_X4DIFFOR_WO_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_X4DIFFOR_WO

RPT_DP_A_WPLABOR_WO_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_A_WPLABOR

RPT_DP_A_WPSERVICE_WO_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_A_WPSERVICE

RPT_DP_A_ASSIGNMENT_WO_SOURCE

Questa vista è basata sulla vista materializzata RPT_DP_A_ASSIGNMENT

Ecco un esempio di script SQL eseguito per creare una vista:

| CREATE VIEW RPT_DP_WPLABOR_SOURCE AS SELECT * FROM RPT_DP_WPLABOR; |
| --- |

Come si può notare dal codice dell’esempio, le viste di frontiera vengono create semplicemente recuperando i dati delle viste materializzate realizzate in precedenza. Quindi i dati sono esattamente gli stessi delle viste materializzate, che a loro volta estraggono giornalmente i dati dalle tabelle del database EAM, garantendo una solida coerenza tra i dati delle viste di frontiera, delle viste materializzate e delle tabelle originali.

Un esempio tipico di estrazione è il seguente:

| SELECT * FROM RPT_DP_WPLABOR_SOURCE; |
| --- |

La vista RPT_DP_WPLABOR_SOURCE è definita come una semplice query su una vista materializzata (RPT_DP_WPLABOR). In questo modo, Data Platform può accedere direttamente ai dati aggiornati senza dover gestire la complessità della struttura sottostante del database EAM o della logica che li ha generati.

### Estrattori – descrizione flusso applicativo

#### Allineamento DB Maximo e DB Staging ad uso e consumo di Data platform

L’allineamento tra il database Maximo (EAM) e il database di staging è garantito da un processo schedulato che, con frequenza giornaliera, esegue la rigenerazione delle viste materializzate a partire dalle tabelle operative di Maximo. Questo processo, gestito dal pod eam-reporting-loader, ha lo scopo di produrre uno snapshot giornaliero coerente e completo dello stato dei dati nel database Maximo, da rendere disponibile a Data Platform per finalità di reportistica e analisi.

Per assicurare che questo aggiornamento venga eseguito una sola volta per ciclo giornaliero, viene utilizzato un meccanismo di lock distribuito basato su ShedLock, il quale coordina l’esecuzione del processo, impedendo l’avvio contemporaneo dell’aggiornamento delle viste da parte di più istanze, evitando così conflitti e sovraccarichi sul database.

Lo schedulatore si occuperà, dopo aver inizializzato le connessioni di verificare tramite tabella di tracing X4_DP_REPORTING_TRACE che non sono presenti attività di aggiornamento. Se non sono presenti attività di schedulazione sarà staccato un nuovo id sulla tabella e passato alle viste materializzate.

Questo codice univoco rappresenterà la chiave di correlazione presente sulla tabella di tracciamento e sulle viste materializzate. Inserito / aggiornato in apertura ed in chiusura della lavorazione.

| @Component public class MaterializedViewRefresher { private final DataSource dataSource; public MaterializedViewRefresher(DataSource dataSource) { this.dataSource = dataSource; } public void refresh(String viewName) { try (Connection conn = dataSource.getConnection(); CallableStatement stmt = conn.prepareCall("{call DBMS_MVIEW.REFRESH(?)}")) { stmt.setString(1, viewName); stmt.execute(); } catch (SQLException e) { throw new RuntimeException("Errore durante il refresh della vista materializzata " + viewName, e); } } } |
| --- |

Il pod si occuperà poi di refreshare le viste materalizzate attraverso il component MaterializedViewRefresher. Questo componente viene invocato N volte quante le viste materializzate su cui effettuare comando di refresh.L’invocazione del componente avviene N volte in parallelo poiché viene ogni volta gestita tramite l’utilizzo di CompletableFuture e di una thread pool(la cui dimensione è stata resa configurabile) A ogni chiamata, si apre dinamicamente una nuova connessione al database tramite dataSource che ne impedisce la concorrenza tra gli N thread. L’uso di try-with-resources garantisce la chiusura sicura delle risorse JDBC.

Nel riquadro sottostante è descritta la logica all’interno del service springboot, richiamato durante la schedulazione principale dell’applicativo

| @Service public class ViewRefreshService { private final MaterializedViewRefresher refresher; @Value("${app.view-refresh.thread-pool-size}") private int threadPoolSize; private ExecutorService executor; public ViewRefreshService(MaterializedViewRefresher refresher) { this.refresher = refresher; } @PostConstruct public void init() { this.executor = Executors.newFixedThreadPool(threadPoolSize); } public CompletableFuture<Boolean> refreshAllViews(List<String> views) { List<CompletableFuture<Boolean>> futures = views.stream() .map(view -> CompletableFuture.supplyAsync(() -> { try { refresher.refresh(view); return true; } catch (Exception e) { System.err.println("Errore nel refresh della vista " + view + ": " + e.getMessage()); return false; } }, executor)) .collect(Collectors.toList()); return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])) .thenApply(v -> futures.stream().allMatch(CompletableFuture::join)); } @PreDestroy public void shutdown() { if (executor != null && !executor.isShutdown()) { executor.shutdown(); System.out.println("ExecutorService chiuso correttamente."); } } } |
| --- |

Lo springboot service viene invocato dallo scheduler principale e riceve una lista di viste materializzate da aggiornare. Ogni vista viene processata in parallelo tramite un ExecutorService a thread pool fisso, la cui dimensione è configurabile per singolo ambiente mediante application.properties o ConfigMap nelncontainer. L’uso di CompletableFuture consente di eseguire i refresh in modo asincrono, e attende il completamento di tutte le operazioni per restituire un risultato aggregato. Il servizio considera il refresh completato con successo solo se tutte le viste vengono aggiornate senza errori. Nel codice la join dei thread è gestita tramite CompletableFuture.allOf(...) che attende il completamento di tutti i task paralleli e tramite join() sui singoli future si recuperano gli esiti. Il thread pool viene chiuso correttamente al termine del ciclo di vita dell'applicazione, tramite il metodo annotato con @PreDestroy.

Verrà notificata tramite scrittura su tabella X4_DP_REPORTING_TRACE il completamento della schedulazione reportistica una volta completate con successo gli aggiornamenti.

Al termine del task del refreshAllViews (metodo riportato nel riquadro sovrastante), l’algoritmo procederà all’aggiornamento del record in tabella X4_DP_REPORTING_TRACE, corrispondente all'ID di schedulazione in atto (campo DP_CORRELATION_ID), modificandone lo stato mediante il metodo upsert esposto nel Service ReportingTraceService sopra descritto.

In particolare, viene quindi modificato lo stato dei campi:

DATE_REFRESH: aggiornato con la data e ora correnti di esecuzione;

STATUS: aggiornato con lo stato della schedulazione. Il dominio di questo campo è il seguente: “STARTED”, “FINISHED” e “ERROR”

A questo punto verrà inviata via chiamata APIREST all’endpoint esposto da DATAPLATFORM

#### Notifica avvenuto aggiornamento dati a Data Platform

Una volta completato con successo l’aggiornamento delle viste materializzate, il pod eam-reporting-loader invia una notifica a Data Platform per segnalare che i dati sono pronti per essere estratti dalle apposite viste di frontiera. Questa notifica rappresenta un passaggio fondamentale del processo, in quanto consente a Data Platform di avviare l’estrazione dei dati necessari per la reportistica e le analisi esterne all’ambiente MAS (Maximo Application Suite).

La notifica viene trasmessa tramite l’invocazione di un’API REST da parte del pod eam-reporting-loader. Questa API comunica a Data Platform che i dati aggiornati sono disponibili e pronti per la lettura all’interno delle viste di frontiera esposte dal database EAM.

| @Autowired private RestTemplate restTemplate; public void sendRestNotification(String codiceReport, String status,String dataReport) { Map<String, Object> requestBody = Map.of( "status", status, "codiceReport", codiceReport, “dataReport”,dataReport ); try { restTemplate.postForEntity("http://endpoint-ancora-da-definire/api/notify",requestBody, Void.class); System.out.println("Notifica REST inviata con successo"); } catch (Exception e) { System.err.println("Errore invio notifica REST: " + e.getMessage()); } } |
| --- |

La notifica oltre ad un messaggio di corretto aggiornamento contiene anche una data corrispondente a quella del giorno in cui è avvenuto l’aggiornamento, uno status, l’orario di inizio e fine esecuzione aggiornamento e un correlation id, in modo tale da permettere a Data Platform di sapere a quale snapshot giornaliero fare riferimento per l’estrazione dei dati.

Una volta ricevuta la notifica, Data Platform procederà all’estrazione dei dati necessari per la reportistica fuori MAS.

Nota Importante: il meccanismo di notifica è per RTI il più adeguato in un’architettura che ha l’obiettivo di eliminare inutili polling. Come riportato è stato previsto che a valle del refresh DP venga notificata l’avvenuta attività, al fine di evitare un polling continuo sulle tabelle. È stato inoltre previsto una tabella di tracciamento X4_DP_REPORTING_TRACE degli aggiornamenti che avvengono sulla piattaforma. Questa architettura permette in modo anche graduale di integrare le 2 piattaforme in ambito notifica aggiornamento.

Per chiarire meglio: la sola tabella X4_DP_REPORTING_TRACE permette di riuscire a tenere traccia dell’avvenuto completamento aggiornamento e può essere solo in una fase transitoria interrogata in polling. Una volta abilitata la notifica tramite api esposta da DataPlatform, DP potrà progressivamente usare tutti i campi da EAM e contenuti nel JSON. In una prima fase potrebbe elaborare la sola chiave della tabella X4_DP_REPORTING_TRACE (DP_CORRELATION_ID) inviata all’interno del body dell’api rest e continuare a fare pollling sulla tabella oppure usare tutte le info inviate da EAM al fine di non utilizzare più il polling su EAM (soluzione che questo disegno architetturale si prefigge). Questa flessibilità architetturale è volta ad evitare constraint sul piano di integrazione di data platform ed evitare dipendenze gestionali.

#### Data Platform estrae dalle tabelle di frontiera i dati

L’estrazione dei dati da parte di Data Platform avviene in modo semplice e controllato, grazie alla predisposizione di viste di frontiera.

Un esempio di estrazione della data dell’ultimo aggiornamento è il seguente:

| SELECT w.*, r.DATE_REFRESH FROM RPT_DP_WPLABOR_SOURCE w LEFT JOIN X4_DP_REPORTING_TRACE r ON w.DP_CORRELATION_ID = r.DP_CORRELATION_ID ; |
| --- |

Quindi per effettuare l’estrazione dei dati e della data dell’ultimo aggiornamento, verrà effettuata un’operazione di JOIN sul codice indicativo della data, presente in ogni vista di frontiera.

Appendice

File xlsx dedicato al mapping dei campi tecnici:
