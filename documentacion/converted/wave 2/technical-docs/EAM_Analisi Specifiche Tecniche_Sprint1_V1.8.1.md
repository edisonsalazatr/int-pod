# EAM_Analisi Specifiche Tecniche_Sprint1_V1.8.1

|  |  |
| --- | --- |

Identificazione del documento

| Nome progetto: | ENTERPRISE ASSET MANAGEMENT |
| --- | --- |
| Documento: | Analisi Tecnica |

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
| Job Task (JT) | Metodi/micro-controlli associati a un PdL |
| Configurazione | Implementazione Standard sull’applicativo IBM Maximo |
| Sviluppo | Implementazione Custom sull’applicativo IBM Maximo |
| Configurazione Database | Applicazione che consente di creare, modificare e gestire tabelle, attributi, indici e relazioni all'interno del database di Maximo. Qui è possibile definire la struttura del database e configurare i dati in modo da adattarsi alle specifiche esigenze dell'organizzazione |
| Application Designer | Applicazione che consente di creare e personalizzare l'interfaccia grafica delle applicazioni presenti in Maximo. Gli utenti possono aggiungere tabelle, sezioni e campi per adattare l'aspetto e il comportamento delle applicazioni in base alle specifiche esigenze aziendali |
| Conditional Expression Manager | Applicazione che permette di creare e modificare espressioni in linguaggio SQL utili per gestire le regole di visibilità e/o editabilità di determinati oggetti all'interno di Maximo |
| Escalation | Meccanismo di automazione che permette di gestire il tempo di risposta di determinate azioni e attività |
| Script di Automazione | Applicazione che permette di creare script in diversi linguaggi di programmazione al fine di automatizzare processi, eseguire calcoli complessi e personalizzare il comportamento delle applicazioni |
| Relazione | Le relazioni consentono di stabilire collegamenti tra più tabelle al fine di recuperare specifici dati mediante l'utilizzo di clausole "WHERE" all'interno di un'istruzione SQL. Questa funzionalità permette di definire una tabella di destinazione, chiamata "Figlio", sulla quale viene eseguita la clausola "WHERE", mentre la tabella di partenza è chiamata "Padre" |
| Relazione.Attributo | Convenzione utilizzata in Application Designer per fare riferimento a un attributo specifico di una tabella nel database di Maximo relazionata a quella corrente |
| Sigoption | Le sigoption (opzione di firma) permettono di specificare i privilegi per l’utilizzo di applicazioni, opzioni di menu e elementi della barra degli strumenti. Inoltre, esistono sigoption condizionali che consentono di concedere o revocare dinamicamente i privilegi di un utente in base a espressioni condizionali. |

# Obiettivo del documento

Il presente documento ha l’obiettivo di indirizzare le tematiche dello Sprint 1, già descritte ad alto livello nel documento “EAM_Analisi funzionale_Sprint_Complessiva_v2.0.docx”, verso un design più dettagliato e tecnico, orientato all’implementazione effettiva del software.

Pertanto, verranno riportate e descritte all’interno del presente documento tutte le configurazioni implementate sulla piattaforma EAM di Terna (ambiente Dev) e gli sviluppi custom da implementare.

Nel dettaglio, ciascun paragrafo (dedicato a una specifica funzionalità nell’applicativo) presenta due sotto-paragrafi: nel primo sono illustrate le configurazioni eseguite sul software (e.g., estensione del modello dati, configurazione dell’applicazione, gestione della condizionalità, etc.), nel secondo gli sviluppi necessari per soddisfare le funzionalità richieste durante lo sprint.

…

# Sprint 1: Catalogo e modellizzazione Attività

Nel modello Terna, le macro-attività/attività rappresentano l’entità centrale del processo di manutenzione che sarà gestito in EAM (attività di controllo, sostituzione, etc.). Le attività sono rappresentate dall’associazione di un workflow (sequenza di metodi) ad una macro-attività. I metodi sono caratterizzati da attributi che ne definiscono i dettagli in termini di risorse, attrezzature e durate necessarie per espletarli.

In generale, i temi di basso livello inclusi nello Sprint 1 sono i seguenti:

Modellizzazione delle attività (struttura relazionale, tipologia, anagrafica, prevalenza).

Gestione catalogo attività (struttura catalogo, aggiunta/modifica/disattivazione attività nel catalogo).

Anagrafica OdL.

Aggregazione OdL.

Di seguito viene riportato uno schema del data model e quindi delle sue entità che sono state configurate o discusse (anche parzialmente) all’interno del documento:

Figura 1 - Data model relazionale

Di seguito viene proposta una tabella riassuntiva delle entità sopra riportate:

| Oggetto | Descrizione funzionale |
| --- | --- |
| WORKORDER | Tabella che contiene le informazioni generali dell'ordine di lavoro. |
| WOACTIVITY | Vista della tabella workorder. Contenuti i singoli task dell'ordine di lavoro. |
| WPLABOR | Tabella dove vengono inserite le pianificazioni delle risorse |
| ASSIGNMENT | Tabella dove vengono inserite le schedulazioni/assegnazioni delle risorse |
| JOBPLAN | Piano di lavoro applicabile all'OdL. Insieme di informazioni standard che vengono riportate nell'OdL al momento di associazione |
| JOBTASK | Step operativi necessari per completare il piano di lavoro. Tabella dove verranno registrati i microcontrolli associati all’attività |
| X3MICROCONTROLLI | Anagrafica dei microcontrolli richiamati all'interno del jobtask |
| X3CATDIFFORMITA | Tabella contenente il catalogo delle difformità |
| X3CATINTERF | Tabella contenente il catalogo delle interferenze |
| X3CATASSOCIAZIONE | Tabella in cui viene relazionato ad un IDORIGINE (che può essere un JPNUM o un MICROCONTROLLO) e le difformità rilevabili per quel record |
| X3ATTPREVALENTI | Tabella che per ogni PdL (JPNUM) tiene al proprio interno le attività di cui è prevalente |
| X3POLICY | Tabella contenente il catalogo delle policy |
| X3DIFFATT | Tabella in cui viene proposto per un piano di lavoro le anomalie risolvibili da quel PdL |

## Informazioni Attività

Il Catalogo Attività sarà organizzato in diversi tab, ognuno dei quali contenente un determinato set di informazioni, nel dettaglio:

Tab “Attività”, dove verranno riportate le informazioni anagrafiche relative alla tipologia di attività presa in considerazione.

Tab “Rilevazioni”, dove verranno riportate le interferenze e le difformità rilevabili dall’attività.

Tab “Workflow”, dove verrà riportato il workflow dei metodi con le relative risorse e attrezzature associate.

Tab “Micro-controlli”, dove verranno associati i micro-controlli necessari.

D’altro canto, il Catalogo Metodi e il Catalogo Micro-controlli sono caratterizzati da un singolo tab principale contenente tutte le informazioni di interesse.

### Configurazioni

Per la gestione dei dati inerenti al catalogo delle attività e ai cataloghi satellite è stato necessario configurare il data model per poter consentire il corretto posizionamento dei dati all’interno del database sfruttato dall’applicativo.

Di seguito viene riportato in formato tabellare gli oggetti e gli attributi creati o configurati:

| OGGETTO | ATTRIBUTO | DESCRIZIONE ATTRIBUTO | TIPO ATTRIBUTO | LUNGHEZZA |
| --- | --- | --- | --- | --- |
| JOBPLAN | JPNUM | ID Attività | UPPER | 12 |
| JOBPLAN | DESCRIPTION | Descrizione attività | ALN | 100 |
| JOBPLAN | CLASSSTRUCTUREID | Tipologia attività | UPPER | 20 |
| JOBPLAN | PLUSCREVNUM | Numero revisione | INT |  |
| JOBPLAN | STATUS | Stato | UPPER | 16 |
| JOBPLAN | X4ASSIEME | Assieme | ALN | 20 |
| JOBPLAN | X4ATTALL | ID Attività Standard | UPPER | 12 |
| JOBPLAN | X4ATTCAR | Attività caricabile manualmente | YORN |  |
| JOBPLAN | X4CLASSECOMP | Classe Componente | ALN | 20 |
| JOBPLAN | X4DATAFINE | Data fine validità | DATE |  |
| JOBPLAN | X4DATAINIZIO | Data inizio validità | DATE |  |
| JOBPLAN | X4GENATTFITT | Generazione attività fittizia | YORN |  |
| JOBPLAN | X4MACROATTIVITA | ID Macro-Attività | UPPER | 12 |
| JOBPLAN | X4NORMDIRIF | Normativa di riferimento | ALN | 15 |
| JOBPLAN | X4PROGRAMMA | Programma | ALN | 20 |
| JOBPLAN | X4SOTTOASSIEME | Sottoassieme | ALN | 50 |
| JOBPLAN | X4TEMPOVAL | Max. tempo di validazione attività | INT | 12 |
| JOBPLAN | X4TIPOCOMP | Tipo componente | ALN | 20 |
| JOBPLAN | X4TIPOLOGIAASSET | Tipologia Asset | UPPER | 192 |
| JOBPLAN | X4ZONIZZAZIONE | Attività soggetta a zonizzazione | YORN |  |
| JOBPLAN | X4EFFORT | Effort | DURATION |  |
| JOBPLAN | X4UM | UM | ALN | 3 |
| JOBPLAN | X4ATTPREVMETODO | Attività prevalente per metodo | YORN |  |
| JOBPLAN | X4ATRIDONEITA | Idoneo UF/SPCC | ALN | 15 |
| X4ATTPREVALENTI | ATTIVITAPREV | Attività Prevalente | ALN | 12 |
| X4ATTPREVALENTI | JPNUM | PdL | ALN | 12 |
| X4METODI | IDMETODO | ID Metodo | ALN | 20 |
| X4METODI | DESCRIPTION | Descrizione | ALN | 50 |
| X4METODI | DURATA | Durata | DURATION |  |
| X4METODI | TOTRIS | Numero totale risorse | INT |  |
| X4MICROCONTROLLI | ASSIEME | Assieme | ALN | 20 |
| X4MICROCONTROLLI | CONTROLLO | Controllo | ALN | 3 |
| X4MICROCONTROLLI | DESCRIPTION | Descrizione | ALN | 270 |
| X4MICROCONTROLLI | FREQUENZA | Frequenza | INT |  |
| X4MICROCONTROLLI | IDMICROCONTROLLO | ID Microcontrollo | ALN | 15 |
| X4MICROCONTROLLI | SOTTOASSIEME | Sottoassieme | ALN | 50 |
| X4MICROCONTROLLI | TIPOLOGIAASSET | Tipologia Asset | UPPER | 192 |
| X4MICROCONTROLLI | TCOMPONENTE | Tipo Componente | ALN | 20 |
| X4MICROCONTROLLI | TCOMANDO | Tipo Comando | ALN | 20 |
| WORKORDER | ASSETNUM | Asset | UPPER | 25 |
| WORKORDER | DESCRIPTION | Descrizione | ALN | 100 |
| WORKORDER | FNLCONSTRAINT | Data di massimo posticipo attività | DATETIME |  |
| WORKORDER | SCHEDFINISH | Data schedulata fine | DATETIME |  |
| WORKORDER | SCHEDSTART | Data schedulata inizio | DATETIME |  |
| WORKORDER | SNECONSTRAINT | Data non prima di | DATETIME |  |
| WORKORDER | TARGCOMPDATE | Data fine pianificata | DATETIME |  |
| WORKORDER | TARGSTARTDATE | Data inizio pianificata | DATETIME |  |
| WORKORDER | WONUM | N. OdL | UPPER |  |
| WORKORDER | STATUS | Stato | UPPER | 16 |
| WORKORDER | X4AGGREGATI | OdL aggregante? | YORN | 1 |
| WORKORDER | X4NATURALAVORO | Assistenza/Sorveglianza | ALN |  |
| WORKORDER | X4DATAMODELLO | Data Modello | DATE |  |
| WORKORDER | X4POLICY | Policy | UPPER | 20 |
| WORKORDER | X4TTRASF | Tempo trasferimento | DURATION | 8 |
| WORKORDER | X4FISICO | Fisico | NUM |  |
| WORKORDER | X4FISICOPLAN | Fisico pianificato | NUM |  |
| WORKRODER | X4PERIMETROINT | Perimetro intervento | UPPER | 20 |
| WORKORDER | X4PMI | OdL per piano (PMI) | YORN |  |
| WORKORDER | X4TRAINANTE | Trainante | YORN |  |
| WORKORDER | X4UM | Unità di Misura | ALN | 5 |
| WORKORDER | X4DATAPIANMOD | Data pianificata modificata | YORN |  |
| WORKORDER | X4TEMPOPAUSA | Tempo Pausa | DURATION |  |
| WORKORDER | X4DATASCHED | Data schedulazione | DATETIME |  |
| WORKORDER | X4OUTSOURCING | Modalità di gestione IN/OUT | YORN |  |
| X4WOINDISP | DATAINIZIO | Data inizio indisponibilità | DATETIME |  |
| X4WOINDISP | DATAFINE | Data fine indisponibilità | DATETIME |  |
| X4WOINDISP | IDREQUEST | Id richiesta indisponibilità | ALN | 25 |
| X4WOINDISP | STATUS | Stato richiesta | ALN | 15 |
| WPLABOR | LABORCODE | Codice | UPPER | 8 |
| WPLABOR | CRAFT | Qualifica | UPPER | 8 |
| WPLABOR | LABORHRS | Durata | DURATION |  |
| WPLABOR | QUANTITY | Quantità | INT |  |
| WPLABOR | X4TITRIS | Titris della risorsa | UPPER | 8 |
| WPLABOR | X4DATAINIZIORISORSA | Data inizio risorsa | DATETIME |  |
| WPLABOR | X4DATAFINERISORSA | Data fine risorsa | DATETIME |  |
| X4CONDIZIONEASSOCIAZIONE | TARGET | Target di raffronto | ALN | 50 |
| X4CONDIZIONEASSOCIAZIONE | VALUE | Valore da confrontare | ALN | 50 |

Per permettere la visione dei campi necessari all’interno del Catalogo Attività, si dovrà prevedere la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “X4CATATTIVITA” come duplicato dell’applicazione standard “JOBPLAN”, si dovranno configurare:

I campi di testata del tab “Attività”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID attività | JPNUM | DESCRIPTION |  |  |
| Livello attività | X4CLASSANCESTOR.HIERARCHYLEVELS |  |  |  |
| Revisione | PLUSCREVNUM | PLUSCREVCOM |  |  |
| ID Macro-Attività | X4MACROATTIVITA |  | VALUELIST |  |
| Allegati |  |  |  |  |

I campi della sezione “Dettagli”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Tipologia Asset | X4TIPOLOGIAASSET |  | CLASSIFICATION |  |
| Assieme | X4ASSIEME |  | VALUELIST | X4READSTATUS |
| Programma | X4PROGRAMMA |  | VALUELIST | X4NOFITT |
| Stato | STATUS |  |  |  |
| Sottoassieme | X4SOTTOASSIEME |  | VALUELIST | X4READSTATUS |
| Massimo tempo di validazione (giorni)​ | X4TEMPOVAL |  |  |  |
| Durata pianificata | JPDURATION |  |  |  |
| Effort | X4EFFORT |  |  | X4NOFITT |
| Attività caricabile manualmente? | X4ATTCAR |  |  | X4MANUALE |
| ID Attività standard | X4ATTALL | DESCRIPTION | VALUELIST | X4ATTST |
| Tipologia attività | CLASSSTRUCTURE.HIERARCHYPATH |  | CLASSIFICATION |  |
| Attività soggetta a zonizzazione? | X4ZONIZZAZIONE |  |  | X4ZONIZZ |
| Data inizio validità | X4DATAINIZIO |  | DATELOOKUP |  |
| Data fine validità | X4DATAFINE |  | DATELOOKUP |  |
| Componente tipo | X4TIPOCOMP |  | VALUELIST | X4CONTRLEGGE |
| Normativa di riferimento | X4NORMDIRIF |  |  | X4CONTRLEGGE |
| Classe componente | X4CLASSECOMP |  | VALUELIST | X4CONTRLEGGE |
| Unità di misura | X4UM |  | VALUELIST | X4NOFITT |
| Livello anagrafico associato | X3CLASSANCESTOR.HIERARCHYLEVELS |  |  |  |
| Gerarchia anagrafica associata | X3CLASSASSET.HIERARCHYPATH |  |  |  |
| Note | DESCRIPTION_LONGDESCRIPTION |  | VALUELIST | X4NOFITT |
| Attività per metodo prevalente? | X4ATTPREVMETODO |  |  | X4NOFORMPI |
| Idoneo UF/SPCC | X4ATRIDONEITA |  | VALUELIST | X4TERZOLIVSTZ |

La sigoption “X4CONTRLEGGE” verifica se il tipo di controllo è “CONTROLLO DI LEGGE”; quando si verifica questa condizione, i campi che hanno assegnata tale sigoption verranno mostrati in schermata.

I campi della tabella “Difformità risolte”, caratterizzata dalla relazione “X4DIFFATT” e dalla sigoption “X4DIFFRIS”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Difformità | DIFFORMITA |  | X4IDDIFFORMITA |  |
| Descrizione | DIFFORMITA.DESCRIPTION |  |  |  |
| Asset di quarto livello | DIFFORMITA.TIPOLOGIAASSET |  |  |  |

I campi della tabella “Attività Prevalenti”, caratterizzata dalla relazione “X4ATTPREVALENTI” e dalla sigoption ”X4ATTPREV”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Attività | ATTIVITAPREV |  | JOBPLAN |  |
| Descrizione | ATTIVITA.DESCRIPTION |  |  |  |
| Asset di quarto livello | ATTIVITA.TIPOLOGIAASSET |  |  |  |

I campi della tabella “Attributi per selezione metodo”, caratterizzata dalla relazione “X4CONDIZIONEASSOCIAZIONE”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attributo | TARGET |  | X4ATTRIBUTE |  |
| Valore target | VALUE |  | VALUELIST |  |

I campi della tabella “Risorse pianificate”, caratterizzata dalla relazione “JOBLABOR” e dalla sigoption “X4NOFITT”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPTASK |  |  |  |
| Qualifica | CRAFT |  | CRAFTRATE |  |
| Risorse | LABORCODE |  | LABORONLY |  |
| Quantità | QUANTITY |  |  |  |
| Ore | LABORHRS |  |  |  |
| Costo riga | LINECOST |  |  |  |

I campi di testata del tab “Rilevazioni”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPNUM | DESCRIPTION |  |  |
| Revisione | PLUSCREVNUM | PLUSCREVCOM |  |  |

I campi della tabella “Interferenze Rilevabili”, caratterizzata dalla relazione “X4ASSOCIAZIONEINT”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Interferenza | IDINTERFERENZA |  | X4IDINTERFERENZA |  |
| Descrizione | INTERFERENZA.DESCRIPTION |  |  |  |
| Asset di quarto livello | INTERFERENZA.TIPOLOGIAASSET |  |  |  |

I campi della tabella “Difformità Rilevabili”, caratterizzata dalla relazione “X4ASSOCIAZIONEDIFF” e dalla sigoption “X4DIFFRIL”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Difformità | IDDIFFORMITA |  | X4IDDIFFORMITA |  |
| Descrizione | DIFFORMITA.DESCRIPTION |  |  |  |
| Asset di quarto livello | DIFFORMITA.TIPOLOGIAASSET |  |  |  |

I campi della tabella “Difformità Rilevabili da Microcontrolli”, caratterizzata dalla relazione “X4ASSOCIAZIONEDIFFMICR”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Difformità | IDDIFFORMITA |  | X4 |  |
| Descrizione | DIFFORMITA.DESCRIPTION |  |  |  |
| Asset di quarto livello | DIFFORMITA.TIPOLOGIAASSET |  |  |  |

I campi della tabella “Interferenze Rilevabili da Microcontrolli”, caratterizzata dalla relazione “X4ASSOCIAZIONEINTMICR”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Interferenza | IDINTERFERENZA |  |  |  |
| Descrizione | INTERFERENZA.DESCRIPTION |  |  |  |
| Asset di quarto livello | INTERFERENZA.TIPOLOGIAASSET |  |  |  |

I campi di testata del tab “Workflow”, caratterizzato dalla sigoption “X4CLEGGEAFITT”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPNUM | DESCRIPTION |  |  |
| Revisione | PLUSCREVNUM | PLUSCREVCOM |  |  |

I campi della tabella “Workflow”, caratterizzata dalla relazione “X4WORKFLOW”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPTASK |  |  |  |
| ID Metodo | X4WORKFLOW |  | X4METODI |  |
| Descrizione | X4METODO.DESCRIPTION |  | LONGDESC |  |
| N° totale di risorse | X4METODO.TOTRIS |  |  |  |
| Durata | X4METODO.DURATA |  |  |  |

I campi di testata del tab “Micro-controlli”, caratterizzato dalla sigoption “X4CONTRPERIODICO”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPNUM | DESCRIPTION |  |  |
| Revisione | PLUSCREVNUM | PLUSCREVCOM |  |  |

I campi della tabella “Micro-controllo”, caratterizzata dalla relazione “X4MICROCONTR”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | JPTASK |  |  |  |
| ID Microcontrollo | X4MICROCONTROLLO |  | X4MICROCONTROLLO |  |
| Descrizione | X4MICROCONTROLLO.DESCRIPTION |  | LONGDESC |  |
| Asset di quarto livello | X4MICROCONTROLLO.TIPOLOGIAASSET |  |  |  |

Inoltre, per permettere la visione dei campi necessari all’interno del Catalogo Metodi si ha la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione “X4CATMETODI” con oggetto principale “X4METODI”, si dovranno configurare:

I campi di testata del tab “Metodo”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Metodo | IDMETODO | DESCRIPTION |  |  |
| Durata | DURATA |  |  |  |

I campi della tabella “Risorse”, caratterizzata dalla relazione “LABOR”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Qualifica | CRAFT |  | CRAFTRATE |  |
| Quantità | QUANTITY |  |  |  |

I campi della tabella “Mezzi”, caratterizzata dalla relazione “MEZZI”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Mezzi | ITEMNUM |  | TOOLS |  |
| Quantità | ITEMQTY |  |  |  |

I campi della tabella “Strumenti”, caratterizzata dalla relazione “STRUMENTI”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Strumenti | ITEMNUM |  | TOOLS |  |
| Quantità | ITEMQTY |  |  |  |

Infine, per permettere la visione dei campi necessari all’interno del Catalogo Micro-controlli si ha la seguente configurazione a sistema. In particolare, a seguito della creazione dell’applicazione “X4MICROCONTROLLI” con oggetto principale “X4MICROCONTROLLI”, si configurano:

I campi di testata del tab “Microcontrollo”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Microcontrollo | IDMICROCONTROLLO | DESCRIPTION |  |  |
| Frequenza (M) | FREQUENZA |  |  |  |
| Controllo | CONTROLLO |  | VALUELIST |  |
| Assieme | ASSIEME |  | VALUELIST |  |
| Tipologia Asset | TIPOLOGIAASSET |  | CLASSIFICATION |  |
| Sottoassieme | SOTTOASSIEME |  | VALUELIST |  |
| Tipo componente | TCOMPONENTE |  |  |  |
| Tipo comando | TCOMANDO |  |  |  |

I campi della tabella “Difformità Rilevabili”, caratterizzata dalla relazione “X4ASSOCDIFFORMITA”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Difformità | IDDIFFORMITA |  | X4IDDIFFORMITA |  |
| Descrizione | DIFFORMITA.DESCRIPTION |  |  |  |

I campi della tabella “Interferenze Rilevabili”, caratterizzata dalla relazione “X4ASSOCINTERFERENZA”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Interferenza | IDINTERFERENZA |  | X4IDINTERFERENZA |  |
| Descrizione | INTERFERENZA.DESCRIPTION |  |  |  |

Catalogo Metodi

Creazione della tabella “X4METODI” per contenere al proprio interno le informazioni del catalogo dei Metodi. Inoltre, viene creata la tabella “X4ATTRMETODI”, in relazione con la tabella “X4METODI”, per contenere al proprio interno tutti gli attributi di interesse (i.e., mezzi, qualifiche, attrezzature, strumenti).

Difformità risolte

Creazione della tabella “X4DIFFRISOLTE” per contenere al proprio interno le difformità risolte per singola attività. Tale tabella è stata relazionata a “JOBPLAN” mediante la relazione “X4DIFFATT” e tiene conto anche della revisione del PdL a cui è legata la difformità.

Attività prevalenti

Creazione della tabella “X4ATTPREV” per contenere al proprio interno le attività prevalenti del PdL. Si relaziona al record di Piano di Lavoro mediante relazione X4ATTPREVALENTI. Questa tabella contiene i riferimenti agli identificativi univoci dei PdL che dovranno, al momento della generazione dell’OdL, essere presi in considerazione per poter chiudere tutti gli OdL ancora aperti.

In aggiunta, per garantire l’associazione di un micro-controllo/PdL a una difformità/interferenza si integra la seguente configurazione a sistema:

Associazione Micro-controllo/PdL – Difformità/Interferenza

Creazione della tabella “X4CATASSOCIAZIONE” per mettere in relazione un “IDORIGINE” (contenente l'ID del Micro-controllo o del PdL) con un “IDDIFFORMITA” o un “IDINTERFERENZA”. Risulta necessaria la definizione di un attributo per specificare il tipo di relazione (i.e., se l'origine è MC o PdL), in modo da creare tre indici univoci: “IDORIGINE – IDINTERFERENZA”, “IDORIGINE – IDDIFFORMITA”, “IDORIGINE – TIPO”.

Le difformità o interferenze rilevabili sono definite da un dominio e lookup associata che permette di selezionare tutte le difformità o interferenze applicabili definite nel paragrafo successivo.

### Sviluppi

Per permettere la funzionalità di disattivazione di un micro-controllo si dovrà prevedere il seguente sviluppo a sistema:

Disattivazione Micro-controllo

Creazione di un warning di avviso alla disattivazione di un Micro-controllo (MC). Il sistema verifica che nella tabella delle associazioni MC/PdL - Difformità (X4CATASSOCIAZIONE) se il record ID univoco di Difformità (IDDIFFORMITA) o Interferenza (IDINTERFERENZA) sia presente o meno ma con altro IDORIGINE rispetto a quello del PdL/MC corrente. Se ciò non avviene, viene generato un warning per l’utente che ha effettuato l’eliminazione dell’associazione.

Per permettere la valorizzazione automatica dei campi del Catalogo attività si prevedono i seguenti sviluppi a sistema:

Auto-compilazione campi Catalogo attività

Gerarchia anagrafica associata: popolato automaticamente tramite relazione dal sistema sulla base del livello anagrafico associato.

Livello attività: popolamento automatico tramite relazione, corrisponde al livello anagrafico associato.

ID attività: L'ID attività (JPNUM) è generato automaticamente dal sistema tramite uno script di automazione al momento della creazione del record. Per le Stazioni, la struttura è tipicamente AT.S.codice_elemento.progressivo_macroattività.progressivo. Per le Linee, la struttura è più articolata e può includere più livelli: AT.L.codice_secondo_livello.codice_terzo_livello.codice_quarto_livello.progressivo. Questi codici vengono assegnati in modo univoco in base alla CLASSSTRUCTURE dell'asset. Il numero progressivo, viene calcolato dinamicamente dallo script. Il sistema analizza i JOBPLAN esistenti con lo stesso codice base e assegna il primo progressivo disponibile, incrementando l’ultimo valore utilizzato, in modo da garantire l’univocità dell’identificativo JPNUM. Se non esiste lo stesso codice base, il sistema assegna 0.

Effort: popolato dal sistema, calcolato come prodotto tra le risorse e la durata, nel dettaglio la moltiplicazione tra LABORHRS e il campo QUANTITY di tutte le righe di WPLABOR collegate al record di ordine di lavoro valutato.

Descrizione attività standard: popolato automaticamente per relazione sulla base del campo definito sul catalogo standard sulla base dell’ID attività standard scelta nel campo “ID attività standard”.

## Struttura Attività

La struttura dell’attività è modellata in IBM Maximo tramite il Piano di Lavoro (PdL), il quale contiene al suo interno tutte le informazioni legate all’attività (cluster attività, difformità risolte, livello anagrafico associato, etc.), il workflow dei metodi e i micro-controlli associati all’attività.

Sia i metodi che i micro-controlli sono modellizzati attraverso il Job Task, il quale permette di gestire entrambi, separatamente, come oggetti strettamente connessi al PdL a cui sono associati.

### Configurazioni

Per permettere la gestione / visibilità del catalogo delle attività, di quello dei metodi e del catalogo dei micro-controlli si configura il sistema come segue:

Permessi utenti

La visibilità e la navigazione dell’applicazione è modulata tra i vari utenti tramite la creazione di opportune opzioni di firma (sigoption). I grant necessari per avere la visualizzazione dell’applicazione verranno forniti soltanto ai gruppi utenti di Asset Management.

Pre-filtraggio catalogo attività

Sono fornite a sistema un totale di 3 query predefinite dell’applicazione che permettono di poter prendere visione di tutte le policy dedicate rispettivamente a: Linee in Cavo, Linee Aeree e Stazioni.

### Sviluppi

Per garantire la corretta generazione di OdL in presenza di metodi e/o micro-controlli si dovranno prevedere i seguenti sviluppi a sistema:

Dominio crossover X4JOBTASK

Creazione di un dominio di tipo crossover assegnato all’attributo standard JOBTASKID della vista WOACTIVITY per migrare automaticamente le informazioni inserite nel JOBTASK verso la tabella WORKORDER al momento dell’assegnazione del PdL all’OdL.

Attributi del catalogo attività condizionali

Sulla base della tipologia di attività, i campi del Piano di Lavoro sono mostrati in modo condizionale. Per ottenere questa funzionalità è stata creata una SIGOPTION con nome uguale alla tipologia di attività e ne è stata assegnata una condizione. Al verificarsi della condizione (che verifica sempre che l’attributo CLASSSTRUCTURE sia uguale ad una specifica tipologia di attività), il campo viene mostrato. Se ciò non accade, il campo viene nascosto.

Si fa rifermento alla “Tabella 1” presente nel paragrafo “3.3 Informazioni che caratterizzano le attività” del documento “EAM_Analisi funzionale.docx” per una rappresentazione esaustiva delle casistiche.

## Processo di aggiunta e modifica di attività

L’applicativo IBM Maximo consente agli utenti abilitati di aggiungere attività al Catalogo Attività, qualora sia necessario. Al fine di prevenire possibili disallineamenti tra i cataloghi e garantire che il processo di aggiunta di una nuova attività avvenga efficacemente, si propone che il processo venga eseguito rispettando una sequenza logica di azioni prestabilite (Par. 3.4 - “EAM_Analisi Funzionale.docx”).

Oltre ad aggiungere una nuova attività, Maximo consente all’utente la possibilità di apportare modifiche alla maggior parte delle informazioni presenti nei PdL già esistenti.

### Configurazioni

Per permettere la propagazione delle modifiche apportate ai campi del Catalogo Attività su Ordini di Lavoro non ancora schedulati si ha la seguente configurazione a sistema:

Modifica Tipologia attività

Implementazione di una sigoption con condizione aggregata tale da rendere il campo di tipologia attività READ-ONLY dopo la sua compilazione.

Modifica Livello anagrafico associato, ID macro-attività e ID attività standard

I campi risultano modificabili solo nel caso in cui il PdL si trovi in stato “bozza”; una volta attivato il sistema li renderà read-only.

### Sviluppi

Per permettere la funzionalità di modifica delle informazioni del Catalogo Attività si dovranno prevedere i seguenti sviluppi a sistema:

Modifica / eliminazione difformità risolte

Implementazione di uno script relativo alla modifica / eliminazione di una riga della tabella delle difformità risolte. Nel caso in cui l'ID della difformità che si cerca di cancellare dalla tabella di associazione attività-difformità (X4DIFFATT) non è presente in altre righe della tabella, il sistema dovrà restituire un warning per informare l'utente che nessun'altra attività potrà risolvere la difformità eliminata.

Inserimento nuove difformità/interferenze risolte

All’interno della tabella X4DIFFATT è presente un dominio nel campo IDDIFFORMITA che permette di poter selezionare le difformità risolvibili secondo alcune logiche sotto descritte: il dominio , seleziona le difformità aventi status = 'ATTIVO' ed esclude quelle già legate al job plan aperto dall’utente (IDDIFFORMITA NOT IN (SELECT difformita FROM x4diffatt WHERE jpnum = :jpnum)), applicando un filtro articolato sulla colonna TIPOLOGIAASSET basato sul valore :&OWNER&.X4TIPOLOGIAASSET del contesto chiamante; la prima condizione è diretta, ovvero accetta le righe in cui TIPOLOGIAASSET è uguale alla tipologia selezionata, ma in alternativa sono gestite altre casistiche derivate da logiche di ereditarietà classifica e relazioni padre-figlio: se X4TIPOLOGIAASSET è ‘CAMPATA AEREA’, sono accettati tutti gli asset classificati come ‘CAMPATA AEREA’ o come figli di ‘SOSTEGNO AEREO’ o ‘CAMPATA AEREA’, verificati via CLASSANCESTOR; se è ‘TRATTA CAVO’, viene applicata logica simile includendo discendenti di ‘TRATTA CAVO’ e ‘ESTREMO CAVO’; in alternativa, se la X4TIPOLOGIAASSET corrisponde a un elemento x4elemento di una classe a livello 2 della gerarchia (HIERARCHYLEVELS=2), vengono inclusi gli asset con TIPOLOGIAASSET discendente diretto (via PARENT) e non inclusi tra i figli della tabella X3TABVERITASTAZIONI rispetto al padre corrispondente alla X4TIPOLOGIAASSET, costruendo così una logica di esclusione rispetto a un mapping personalizzato delle classi; infine, viene gestato un ulteriore ramo per le X4TIPOLOGIAASSET che si trovano a livello 3 e discendono da ‘STAZIONE’ oppure appartengono alla gerarchia dei ‘DISPOSITIVI E COMPONENTI’: in tal caso, vengono inclusi i figli diretti nella gerarchia (PARENT) oppure i figli transitivi derivati da una doppia interrogazione sulla tabella X3TABVERITASTAZIONI, ricostruendo la discendenza della classe corrente fino ai nodi foglia.

Quanto riportato è speculare anche per le interferenze, quindi per il campo IDINTERFERENZA della X4INTERFATT.

Inserimento nuove difformità rilevate

All’interno della tabella X4CATASSOCIAZIONE è presente un dominio nel campo IDDIFFORMITA che permette di poter selezionare le difformità risolvibili secondo alcune logiche sotto descritte: il dominio filtra le difformità attive rilevanti per la tipologia asset, tenendo conto sia del parametro TIPOLOGIAASSET che del campo X4TIPOLOGIAASSET del JOBPLAN associato. La logica include più condizioni OR per coprire vari casi di confronto tra tipologie e gerarchie classstructure. Viene accettata una corrispondenza diretta tra TIPOLOGIAASSET e il campo tipologiaasset della tabella, oppure una corrispondenza gerarchica tramite la tabella CLASSANCESTOR nel caso in cui TIPOLOGIAASSET sia 'CAMPATA AEREA', estendendo il confronto anche ai discendenti della classificazione 'SOSTEGNO AEREO'. In alternativa, viene accettata una corrispondenza tra X4TIPOLOGIAASSET del jobplan e il campo tipologiaasset della difformità, oppure un confronto di secondo livello gerarchico (HIERARCHYLEVELS=2), in cui si verifica che il tipologiaasset sia un figlio diretto della classe rappresentata da TIPOLOGIAASSET o JOBPLAN.X4TIPOLOGIAASSET, ma non sia un discendente vietato secondo la tabella X3TABVERITASTAZIONI. Se la X4TIPOLOGIAASSET appartiene a un terzo livello (HIERARCHYLEVELS=3) oppure discende dalla classificazione 'DISPOSITIVI E COMPONENTI', viene accettato come valido se tipologiaasset ha lo stesso padre nella classstructure oppure se è un figlio conforme secondo X3TABVERITASTAZIONI rispetto alla stessa X4TIPOLOGIAASSET. Infine, vengono escluse tutte le difformità già associate al jobplan in X3CATASSOCIAZIONE, dove idorigine coincide con il jpnum corrente e il tipo è 'D'. La clausola finale AND status='ATTIVO' garantisce che solo le difformità attive siano incluse.

Modifica / propagazione Assieme, Sottoassieme, Descrizione, Classe componente, Componente tipo e Normativa di riferimento

I campi risultano modificabili solo nel caso in cui il PdL è in stato “pending review”. La modifica è propagata soltanto tramite il tasto “propaga modifiche”.

Modifica / propagazione Attività prevalenti, Note, Micro-controlli e Metodi

I campi risultano modificabili solo nel caso in cui il PdL si trova in stato “pending review”. La modifica è propagata in automatico agli ordini di lavoro dopo il salvataggio e il passaggio di stato in “attivo”.

Salvataggio difformità risolte

Creazione di una condizione associata allo stato “ATTIVO” del jobplan (nel dominio degli status dei jobplan). Nel caso in cui l'attività sia una Manutenzione su Condizione o Sostituzione (CLASSSTRUCTUREID del record di JOBTASK valutato) e non è presente nessun record relazionato al jobplan corrente che si cerca di salvare nella tabella delle difformità risolte, il sistema dovrà inibire il passaggio del PdL allo stato “ATTIVO".

Modifica campo “Massima validazione”

Nel caso in cui il tempo di massima validazione venga aggiornato, uno script di automazione verificherà il cambio effettuato. Se il nuovo tempo di validazione inserito dall’utente è minore di quello precedente, allora il sistema verifica la presenza di OdL nella tabella WORKORDER aventi JPNUM uguale a quello dell’attività corrente e, nel caso in cui la data corrente sia maggiore della data di generazione dell’OdL sommata al nuovo tempo di massima validità, allora ne imposta lo stato in aperto.

Creazione centro di controllo per revisione incongruenze

Creazione di un centro di controllo nel quale vengono rilevate le possibili incongruenze tra i cataloghi per estrarre le difformità non più risolvibili ma rilevabili e le difformità non più rilevabili da attività. Viene dunque creato un centro di controllo denominato “X4RILEVAZIONI” al cui interno viene definito un result set basato sull’applicazione X4CATDIFF con query denominata “INCDIFF” che rileva le due casistiche. Le colonne dei result set sono le seguenti: ID Difformità, Tipologia asset, Tipologia difformità, Descrizione della difformità. Selezionando una qualsiasi riga, il cdc è stato configurato per spostare l’utente direttamente nell’applicazione “Catalogo Difformità” con il record selezionato già aperto.

Disattivazione attività

Nel momento in cui viene effettuato il cambio di stato di un’attività in stato “Non attivo”, il sistema effettua diverse operazioni inerenti il catalogo delle policy contestualmente al cambio di stato avvenuto nel catalogo delle attività.

Nel dettaglio, verrà effettuata una query nella tabella X4POLICY (contenente le policy censite al sistema) per estrarre quelle policy in cui l’ID univoco dell’attività appena disattivata è presente nel campo X4ATTOUTPUT. Parallelamente viene effettuata un’altra query nella tabella X4INPUTPOLICY per estrarre quelle righe della tabella in cui l’attributo X4IDINPUT è pari all’id dell’attività appena disattivata e non sono presenti altre righe in tabella per stesso record di X4IDPOLICY. In entrambi i casi verranno disattivate le policy presenti nell’attributo “X4IDPOLICY” di entrambe le tabelle. Tale disattivazione avverrà con un cambio stato della policy.

Inoltre, per permettere la propagazione delle modifiche apportate al PdL, ovvero la sua nuova revisione, si dovrà prevedere il seguente sviluppo a sistema:

Propagazione PdL revisionato

Implementazione di un pulsante con action nel PdL che consentirà di selezionare tutti gli OdL caratterizzati dal medesimo PdL e inserire la nuova revisione. Inoltre, se è presente una richiesta di indisponibilità per l’OdL a cui si sta modificando il PdL associato, allora il sistema dovrà mostrare un service warning per fornire un avvertimento all'utente (gli OdL soggetti a questo tipo di propagazione dovranno essere tracciati a sistema).

## Informazioni associate al Monitoraggio degli OdL

Il Monitoraggio degli OdL sarà organizzato in diversi tab, ognuno dei quali contenente un determinato set di informazioni, nel dettaglio:

Tab “Ordine di lavoro”, dove verranno riportate le informazioni relative all’ordine di lavoro.

Tab “Date”, dove verranno riportate le informazioni relative alla programmazione.

### Configurazioni

Per permettere la visione dei campi necessari all’interno dell’Ordine di lavoro, si implementa la seguente configurazione a sistema. In particolare, a seguito della creazione della vista “WOTRACK_D” come duplicato dell’applicazione standard “WOTRACK”, si configurano:

I campi di testata del tab “Ordine di lavoro”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID OdL | WONUM | DESCRIPTION |  |  |
| Allegati |  |  |  |  |
| OdL padre | PARENT |  | WORKORDERISTASK |  |
| Classe | WOCLASS |  |  |  |
| Stato | STATUS |  |  |  |
| Perimetro intervento | CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Data stato | STATUSDATE |  |  |  |
| Tipo OdL | CLASSSTRUCTURE.HIERARCHYPATH |  | CLASSIFICATION |  |
| Descrizione classe | CLASSSTRUCTURE.DESCCRIPTION_CLASS |  | CLASSIFICATION |  |
| OdL Aggregante? | X4AGGREGATI |  |  |  |
| OdL Serbatoio? | APPTREQUIRED |  |  |  |
| Fisico | X4FISICO |  |  | X4SERBATOIO |
| Fisico pianificato | X4FISICOPLAN |  |  |  |
| ODL per piano (PMI)? | X4PMI |  |  | X4SERBATOIO |
| Trainante/trainato | X4TRAINANTE |  |  |  |
| Perimetro Intervento | X4PERIMETROINT |  |  |  |
| Note | DESCRIPTION_LONGDESCRIPTION |  |  |  |

I campi della sezione “Dettagli lavoro”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID PdL | JPNUM | JOBPLAN.DESCRIPTION | WO_JOBPLAN |  |
| N. revisione piano di lavoro | PLUSCJPREVNUM |  |  |  |
| ID Policy | X4POLICY | DESCRIPTION |  | X4SERBMAN |
| Sottoassieme | JOBPLAN.X4SOTTOASSIEME |  |  |  |
| ID Macro-attività | JOBPLAN.X4MACROATTIVITA |  |  |  |
| Assieme | JOBPLAN.X4ASSIEME |  |  |  |
| Tipologia PdL | JOBPLAN.CLASSSTRUCTURE.HIERARCHYPATH |  |  |  |
| Programma | JOBPLAN.X4PROGRAMMA |  |  |  |
| Modalità di gestione IN/OUT? | X4OUTSOURCING |  |  |  |
| Assistenza/Sorveglianza | X4NATURALAVORO |  | X4NATURALAVORO |  |
| Unità di misura | X4UM |  |  |  |

I campi della sezione “Dettagli asset”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| ID Asset | ASSETNUM | ASSET.DESCRIPTION | ASSET | X4SERBATOIO |
| SAP ID Asset | ASSET.X3SAPID |  |  | X4SERBATOIO |
| Gerarchia Asset | ASSET.CLASSSTRUCTURE.HIERARCHYPATH |  |  | X4SERBATOIO |
| Gerarchia Responsabile asset | ASSET.X3ASSET1LVL.X3INGRP |  |  |  |

I campi della sezione “Responsabilità”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Riportato da | REPORTEDBY |  | PERSONNAME |  |
| Responsabile Attività | OWNERGROUP |  |  |  |
| Data di report | REPORTDATE |  | DATELOOKUP |  |
| Responsabile Asset | ASSET.X3INGRP |  |  |  |
| Telefono | PHONE |  |  |  |

I campi di testata del tab “Date”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Data Modello DSS | X4DATAMODELLO |  |  |  |
| Data esecuzione | ACTSTART |  | DATELOOKUP |  |
| Data pianificata inizio | TARGSTARTDATE |  | DATELOOKUP |  |
| Data chiusura | ACTFINISH |  | DATELOOKUP |  |
| Data pianificata fine | TARGCOMPDATE |  | DATELOOKUP |  |
| Durata pianificata | JPDURATION |  |  |  |
| Data non prima di | SNECONSTRAINT |  | DATELOOKUP |  |
| Data di massimo posticipo attività | FNLCONSTRAINT |  | DATELOOKUP |  |
| Tempo Preparazione | X4TPREP |  |  |  |
| Tempo Operazione | X4TOPER |  |  |  |
| Tempo Trasferimento | X4TTRASF |  |  |  |
| Data pianificata modificata? | X4DATAPIANMOD |  |  |  |
| Data schedulazione | X4DATASCHED |  | DATELOOKUP |  |
| Tempo pausa | X4TEMPOPAUSA |  |  |  |
| Tempo esecuzione | ACTLABHRS |  |  |  |

I campi di testata in sola lettura del tab “Indisponibilità”, mostrati tramite relazione X4WOINDISP (utilizzata per mostrare il contenuto della tabella X4WOINDISP dove verranno registrate le delibere di indisponibilità ricevute da Agile):

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Data Inizio Indisponibilità | X4WOINDISP.DATAINIZIO |  |  |  |
| Data fine Indisponibilità | X4WOINDISP.DATAFINE |  |  |  |
| ID Richiesta indisponibilità | X4WOINDISP.IDREQUEST |  |  |  |
| Stato richiesta | X4WOINDISP.STATUS |  |  |  |

I campi di testata del tab “Piani”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Ordine di lavoro | WONUM | DESCRIPTION |  |  |
| Stato | STATUS |  |  |  |
| Data pianificata inizio | TARGSTARTDATE |  | DATELOOKUP |  |
| Data pianificata fine | TARGFINISHDATE |  | DATELOOKUP |  |

All’interno del tab “Piani” sono inoltre presenti le seguenti tabelle:

Tabella degli OdL figli inclusi nell’OdL:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | TASKID |  |  |  |
| Asset | ASSETNUM |  |  |  |
| SAPID | ASSET.ALLSAPIDASSET.STRNO |  |  |  |
| Descrizione | DESCRIPTION |  |  |  |
| Durata | ESTDUR |  | DATELOOKUP |  |
| Predecessore | PREDESSORWOS |  | SELECTPREDWOSSCHPLUS |  |

Tabella dei microcontrolli inclusi nell’OdL:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Attività | TASKID |  |  |  |
| Descrizione | DESCRIPTION |  |  |  |
| Asset di quarto livello | ASSETNUM |  |  |  |
| SAPID | ASSET.ALLSAPIDASSET.STRNO |  |  |  |
| IDMICROCONTROLLO | X4MICROCONTROLLO |  |  |  |

I campi del tab “Piani” della tabella “Labor”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Qualifica | CRAFT |  | CRAFTRATE |  |
| Quantità | QUANTITY |  |  |  |
| Ore ordinarie | LABORHRS |  |  |  |
| Titris | X4TITRIS |  | UDB |  |
| Tariffa | PAYRATE |  |  |  |
| Data inizio risorsa | X4DATAINIZIORISORSA |  | DATELOOKUP |  |
| Data fine risorsa | X4DATAFINERISORSA |  | DATELOOKUP |  |

I campi della tabella “Risorse esterne”:

| CAMPO | ATTRIBUTO | Attributo parte 2 di tipo Descrizione | LOOKUP | SIGOPTION |
| --- | --- | --- | --- | --- |
| Codice | LABORCODE |  | X4VENDORCRAFT |  |
| Importo | LINECOST |  |  |  |
| Data inizio risorsa | X4DATAINIZIORISORSA |  | DATELOOKUP |  |
| Data fine risorsa | X4DATAFINERISORSA |  | DATELOOKUP |  |

### Sviluppi

Popolamento automatico del tempo di trasferimento OdL

Alla generazione dell’ordine di lavoro e sulla base del TITRIS di appartenenza, il dato del tempo di trasferimento viene calcolato automaticamente. Nel dettaglio, verrà creata una tabella di mapping che, data ogni UdB possibile all’interno del sistema, abbia associato un tempo di trasferimento verso ogni specifica sede tecnica di primo livello. Tale tabella sarà, inoltre, modificabile dall’utente (sia in termini di creazione di nuove righe che di modifica/eliminazione di righe esistenti) mediante applicazione dedicata.

Inizializzazione OdL

Alla creazione di un nuovo OdL, un automation script controlla se il campo JPNUM è stato modificato e se il campo X4PERIMETROINT è ancora nullo, in tal caso recupera il job plan associato all’ordine di lavoro, risale alla classificazione del job plan tramite la relazione CLASSSTRUCTURE, e utilizza la funzione evaluateClass per determinare il valore del perimetro interno, che viene poi salvato nel campo X4PERIMETROINT dell’ordine, a questo punto se il campo OWNERGROUP non è valorizzato, il codice cerca di assegnarlo automaticamente risalendo al job plan, da lì alla classificazione e infine alla tabella X4CATMOS delle attività standard, per ottenere il tipo di UDB, quindi recupera l’asset collegato all’ordine e la sua gerarchia utilizzando una relazione che interroga sia gli antenati diretti (campo ancestor) che quelli derivati dalla relazione assetlocrelation basata sul campo X3SAPID, dopodiché costruisce dinamicamente un filtro per cercare nella tabella x4capacity un gruppo di appartenenza (persongroup) coerente con il tipo di UDB, l’anno dell’ordine e la posizione dell’asset nella gerarchia, e se lo trova lo imposta come OWNERGROUP dell’ordine di lavoro e aggiorna anche il campo X4WBSTASK con il valore di WBS associato al gruppo e all’anno corrente calcolato tramite la funzione evalwbe, successivamente se viene modificato il campo X4COIN, il codice interroga la tabella X4COINTIPOATTIVITA utilizzando come chiave il valore del COIN selezionato per ottenere il tipo di attività e, se non già valorizzato, anche il tipo di ordine, quindi imposta questi valori sia sull’ordine principale che su eventuali ordini figli di tipo attività (ottenuti dalla relazione WOACTIVITY), infine se né X4COIN né X4TIPOSPESA sono stati ancora valorizzati, il codice cerca di determinarli automaticamente a partire dall’attività standard associata al job plan, leggendo i valori prevalenti dalla tabella X4CATMOS e propagandoli sia sull’ordine principale che su quelli figli, completando i dati anche con i campi X4TIPOATTIVITA e, solo se vuoto, X4TIPOORDINE, in modo da garantire la completezza delle informazioni economiche e funzionali necessarie per la successiva gestione del ciclo di vita dell’ordine di lavoro.

## Modalità di accorpamento e logiche di gestione OdL aggregatori

L’applicativo IBM Maximo consente agli utenti abilitati di accorpare più Ordini di Lavoro in un singolo OdL aggregatore. Quest’ultimo dovrà essere creato manualmente e presenterà lo stesso corredo informativo di un normale Ordine di Lavoro, al netto del flag “OdL aggregatore” attivato.

### Configurazioni

Campi obbligatori per gestione OdL Aggregati

Inserimento di una sigoption e di una condizione associata per rendere obbligatori i campi di “PdL”, “COIN”, “Programma”, “Assistenza/sorveglianza”,”Asset” dopo la selezione del flag ”OdL aggregante?”.

Configurazione applicazione gestione OdL

Per garantire una corretta visualizzazione delle pianificazioni e consuntivazioni degli OdL aggregati all’ordine di lavoro aggregante è stata configurata l’applicazione “Monitoraggio OdL” come segue: sono duplicate le tabelle di gestione di pianificazioni e consuntivazioni. A queste tabelle viene modificata la relazione che mostra i record sottostanti in modo tale che il sistema estragga tutte le pianificazioni e tutte le consuntivazioni presenti negli OdL aggregati. Viene inoltre applicata un’apposita sigoption denominata “X4ODLAGG” che mostra le due tabelle duplicate nel caso in cui l’OdL sia un OdL aggregante, se ciò non accade invece le nasconde. Per ottenere questa funzionalità viene applicata una condizione omonima alla sigoption che verifica se il valore dell’attributo X4AGGREGATI è pari a 1.

### Sviluppi

Per permettere la funzionalità di aggregazione degli OdL, si prevedono i seguenti sviluppi a sistema:

Attributi dell’ordine di lavoro condizionali

Sulla base della tipologia di ordine di lavoro, i campi dell’OdL sono mostrati in modo condizionale. Per ottenere questa funzionalità è stata creata una SIGOPTION con nome uguale alla tipologia di ordine di lavoro e ne è stata assegnata una condizione. Al verificarsi della condizione (che verifica se l’ordine è di tipo serbatoio e se l’odl è stato creato o meno dal DSS), il campo viene mostrato. Se ciò non accade, il campo viene nascosto.

Si fa rifermento alla “Tabella 1” presente nel paragrafo “4.2 Informazioni associate agli OdL” del documento “EAM_Analisi funzionale.docx” per una rappresentazione esaustiva delle casistiche.

Associazione OdL aggregati

Dopo la selezione del flag ”OdL aggregante?”, nella lookup di selezione degli OdL aggregati si dovrà poter filtrare per gli OdL che siano riconducibili alla stessa sede tecnica di primo o secondo livello di riferimento (necessario selezionare assetnum dell'OdL aggregante), OdL con stesso programma e OdL con stesso PdL.

La Clausola where dovrà essere tale che gli OdL mostrati siano: OdL con JPNUM, programma uguale all'OdL, asset di riferimento degli OdL aggregabili figli dell’asset selezionato nell’OdL aggregatore.

Gestione CoIn

Quando viene aggregato un nuovo OdL all'OdL aggregante, se l'OdL aggregato ha CoIn diversa da quella dell'OdL aggregante, l'OdL aggregato eredita la CoIn dell'aggregatore. Pertanto il record di OdL figlio eredita il campo X4COIN dal padre.A seguito di questa operazione effettuata dal sistema, viene inoltre mostrato un warning che avvisa l’utente della modifica effettuata.

Gestione Assistenza/Sorveglianza

Quando viene aggregato un nuovo OdL all'OdL aggregante, se l'OdL aggregato ha valore di Assistenza/Sorveglianza diversa da quella dell'OdL aggregante, viene mostrato un warning di avviso. Pertanto, per un nuovo record sulla relatedrecord, se il wonum nell'attributo relatedreckey ha valore differente per l'attributo “X4NATURALAVORO” rispetto al wonum impostato su recordkey, allora viene evocato un service warning che avvisa l'utente.

Allineamento Outsourcing

Quando viene modificato uno dei due campi X4OUTSOURCING oppure X4NATURALAVORO su un ordine di lavoro che ha figli e si è nel contesto di aggiornamento normale (escludendo validazioni), il codice accede al set dei figli non di tipo task tramite la relazione CHILDNOTASK e, se il set non è vuoto, scorre ciascun figlio e per ognuno imposta i campi X4NATURALAVORO e X4OUTSOURCING con gli stessi valori dell’ordine padre, garantendo quindi l’allineamento automatico di queste informazioni tra il workorder principale e i suoi figli, e alla fine salva le modifiche propagate al set dei figli.

…
