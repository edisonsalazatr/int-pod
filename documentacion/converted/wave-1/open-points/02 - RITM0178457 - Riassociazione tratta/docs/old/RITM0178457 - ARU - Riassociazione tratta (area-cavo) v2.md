| ANALISI REQUISITI UTENTE<br>EAM Riassociazione tratta (area/cavo) |
| --- |

ISC – Uso INTERNO

Storia delle revisioni

| Rev. N° | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 1 | Marco Lerro | 09/01/2026 | Prima emissione |
| 2 | Marco Lerro | 26/01/2026 | Aggiunta scenari e correzioni |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

Indice

1.	Generalità	4

1.1.	Contesto di riferimento	4

1.1.1.	Committente	4

1.1.2.	Vincoli temporali e di pianificazione	4

1.1.3.	Trattamento dei dati personali	4

1.1.4.	Classificazione del sistema/applicazione	5

2.	Requisiti Funzionali	6

2.1.	Lista Requisiti Funzionali	6

3.	Descrizione della soluzione	8

3.1.	Operatività sul sistema	8

3.2.	Gestione sequenza POSNR	8

3.3.	Desiderata utente	9

4.	Scenari di utilizzo	10

4.1.	Scenari standard	11

4.1.1.	Riassociazione di una porzione di linea in una linea vuota	11

4.1.2.	Riassociazione di una porzione di linea a inizio altra linea	11

4.1.3.	Riassociazione di una porzione di linea a una linea in cavo	12

4.2.	Scenari speciali	12

4.2.1.	La riassociazione di una linea con degli ammazzettamenti	12

4.2.2.	Gestione della doppia terna	13

4.2.3.	La riassociazione totale	14

4.2.4.	La riassociazione di una porzione di derivazione	15

4.2.5.	La riassociazione di una porzione di dorsale	15

4.3.	Scenari esclusi	16

4.3.1.	Sezionamento tratta in cavo	16

4.3.2.	Sezionamento interno ad uno split o una derivazione con rientro	16

4.3.3.	Selezione asset precedenti a sostegno da cui partono più campate	16

4.3.4.	Selezione asset precedenti a sostegno di derivazione	16

4.3.5.	Aggancio a un sostegno da cui partono più campate	17

5.	Documenti allegati	18

# Generalità

## Contesto di riferimento

Terna è il principale proprietario della Rete elettrica di Trasmissione Nazionale (RTN), una delle più moderne e tecnologiche in Europa ed è uno tra gli operatori europei più importanti di reti per la trasmissione dell’energia in alta e altissima tensione (Transmission System Operator - TSO) per chilometri di linee gestite.

In qualità di Transmission System Operator (TSO), Terna ha, tra gli altri compiti, il mandato istituzionale e la responsabilità sulle seguenti quattro aree:

Assicurare la sicurezza operativa del sistema elettrico nazionale;

Mantenere costantemente l’equilibrio tra domanda ed offerta di energia;

Assicurare e mantenere l’adeguatezza del sistema di trasmissione nel medio e lungo termine;

Migliorare l’efficienza del mercato elettrico.

In questo contesto, l’implementazione di un sistema EAM (Enterprise Asset Management) come IBM Maximo è parte di un percorso di digitalizzazione e innovazione dei processi. Il sistema mira – fra le altre cose - a integrare la pianificazione e la consuntivazione delle attività tecnico-economiche, garantendo una gestione avanzata dei processi di manutenzione e investimento. La presente fornitura si inserisce in questo percorso, con un focus specifico sui collaudi funzionali e contraddittori delle soluzioni sviluppate.

### Committente

SDS-ITD-Soluzioni per RTN IRP

### Vincoli temporali e di pianificazione

Il periodo temporale di riferimento va indicativamente da Febbraio 2026 e Maggio 2026. Eventuali ripianificazioni andranno concordate col PM di progetto.

### Trattamento dei dati personali

| Trattamento dati personali | Trattamento dati personali | Trattamento dati personali |
| --- | --- | --- |
| Richiesta | Check | Note |
| Il sistema tratta dati personali? | __ Si |  |
| Il sistema tratta dati personali? | __ No |  |
| Il sistema tratta dati particolari? | __ Si |  |
| Il sistema tratta dati particolari? | __ No |  |
| Il sistema tratta dati giudiziari? | __ Si |  |
| Il sistema tratta dati giudiziari? | __ No |  |

### Classificazione del sistema/applicazione

| Trattamento dati personali | Trattamento dati personali | Trattamento dati personali | Trattamento dati personali |
| --- | --- | --- | --- |
| Richiesta | Impatto Alto | Impatto Medio | Impatto Basso |
| Com’è stata classificata l’applicazione/il sistema | __ | __ | __ |

# Requisiti Funzionali

## Lista Requisiti Funzionali

Nell’ambito del progetto di realizzazione della piattaforma di Asset Management basata su IBM Maximo Application Suite (MAS) per Terna S.p.A., l’utente richiede la ristrutturazione della funzionalità Riassociazione tratta (area/cavo) con i seguenti obiettivi:

copertura dei 15 casi possibili identificati dal business in cui è applicabile la funzionalità (si rimanda per la lista completa dei casi al deck dettagliato condiviso con business).

realizzazione di una maschera operativa che permetta di indicare chiaramente e coerentemente i parametri della riassociazione

verifica della consistenza della struttura della linea e applicabilità della procedura di riassociazione con l’obiettivo di intercettare preventivamente eventuali condizioni non supportate

prestazioni adeguate

La funzione di riassociazione dovrà gestire varie configurazioni possibili che coprono:

presenza della sola dorsale sia semplice che mista

presenza ammazzettamenti

linee in doppia terna

derivazioni

La funzionalità sarà attivabile da una linea (linea ricevente) e basata su una maschera di input che presenterà i seguenti campi:

Da Linea (SAP id): SAP id linea da cui attingere la tratta (linea cedente). Sarà possibile selezionare la linea mediante dialog di ricerca che presenterà i seguenti campi per facilitarne la ricerca: asset, SAP id, descrizione, divisione, unità impianti.

Sostegno/Estremo terminale (SAP id): sostegno/estremo terminale della linea cedente identificativo della tratta oggetto della riassociazione. Sarà possibile selezionare l’asset mediante un elenco, ordinato per sequenza, degli elementi che in base alle regole, è possibile selezionare per indicare il punto da cui selezionare gli asset per la riassociazione. I dati mostrati sono: asset, SAP id, sequenza, POSNR, descrizione e unità impianti. L’asset selezionato è incluso nella riassociazione.

Includi asset successivi/Includi asset precedenti: scelta esclusiva per indicare, rispetto a sostegno/estremo terminale, se la tratta da riassociare è la parte successiva o precedente.

Aggancio da inizio linea: indica che la tratta verrà posizionata ad inizio della linea ricevente.

Sostegno/Estremo terminale di aggancio (SAP id): sostegno/estremo terminale della linea ricevente a destra del quale verrà inserita la tratta dalla linea cedente. Sarà possibile selezionare l’asset mediante un elenco, ordinato per sequenza, degli elementi che in base alle regole, è possibile selezionare per indicare il punto da cui selezionare gli asset per la riassociazione. I dati mostrati sono: asset, SAP id, sequenza, POSNR, descrizione e unità impianti. L’asset selezionato è incluso nella riassociazione.

Al fine di garantire la corretta riassociazione, verrà mostrata una preview della stessa prima di eseguire la riassociazione. La finalità di questa preview è quella di fornire all’utente un quadro chiaro dei soli dati che saranno riassociati (compresi eventuali elementi nuovi), con la possibilità di aggiornare preventivamente la componente variabile dei SAP id delle campate (quelle dei sostegni sono da queste dipendenti). Per quanto riguarda la data [In funzione dal], per gli asset riassociati non varia, mentre per gli eventuali asset nuovi, dovrà essere inserita.

Prima di salvare l’utente cliccherà un pulsante per eseguire la verifica dei dati; se la verifica fallisce, sarà avvisato del motivo, con la possibilità di aggiornare quanto necessario, se la verifica da esito positivo, potrà salvare. Tutti i dati della maschera di preview sono in memoria temporanea e il tentativo di salvataggio di dati parzialmente incorretti provocherà l’annullamento dell’intera operazione, preservando la forma originale delle linee coinvolte dalla richiesta di modifica.

Si riassume per sinteticità gli oggetti dello sviluppo:

Interfaccia di configurazione dei parametri della riassociazione con dialog di selezione rapida.

Interfaccia di preview della operazione che permetta di validare quanto configurato dall’utente.

Interfaccia per l’impostazione preventiva dei SAP id della nuova tratta al fine di impostare i SAP id desiderati in anticipo evitando successive ricodifiche.

Flusso di riassociazione che supporta sia casistiche base (dorsale sia in area che miste) che casistiche avanzate (tratti ammazzettati, derivazioni, doppia terna).

Gestione transazionalità della riassociazione al fine di non modificare in modo inconsistente l’anagrafica nel caso di situazioni di errore, preservando la forma originale delle linee coinvolte dalla richiesta di modifica.

Modifiche dello scenario speciale di riassociazione del connettore al fine di supportare tutte le casistiche previste.

Prestazioni adeguate rispetto ai volumi di modifiche generati dalla riassociazione.

Attività di test (unit-test, SIT e UAT) comprensivi di scenari end-to-end Maximo/SAP. L’attività prevederà anche la creazione laddove necessario di linee configurate secondo gli scenari previsti.

Di seguito i deliverable previsti:

Per gli UAT sarà fornito il test-book per la verifica di tutti gli scenari previsti.

Aggiornamento della documentazione di disegno.

Predisposizione della nota di rilascio corredata dei pacchetti di rilascio basati su migration manager.

Piano attività in formato Gantt.

# Descrizione della soluzione

## Operatività sul sistema

In questo paragrafo si descrive l’operatività a sistema con la quale l’utente eseguirà la funzione di riassociazione. Nell’immagine seguente l’ipotesi di gestione dei dati richiesti in input per eseguire la funzionalità. Il simbolo  di colore nero rappresenta l’opzione selezionata e di colore grigio quella non selezionata; una scelta è obbligatoria e il default è quella di Includi asset successivi.

Figura 1 – Dati rilevanti per la riassociazione

Richiamando la funzionalità di riassociazione ad altra linea, dalla linea che deve ricevere gli elementi, si apre una box di dialogo per la gestione delle informazioni qui riportate. Se il check box [Aggancio da inizio linea] è impostato, allora il campo [Sostegno/Estremo terminale di aggancio] sarà vuoto e in sola lettura. Per la compilazione del campo [Da Linea] viene mostrato l’elenco delle linee, con le colonne: SAPID, Descrizione, Divisione e Unità impianti.

Per la compilazione del campo [Sostegno/Estremo terminale] viene mostrato l’elenco, ordinato per sequenza, degli elementi che in base alle regole, è possibile selezionare per indicare il punto da cui selezionare gli asset per la riassociazione. I dati mostrati sono: Asset, SAP ID, Sequenza, POSNR, descrizione e unità impianti. L’asset selezionato è incluso nella riassociazione.

Per la compilazione del campo [Sostegno/Estremo terminale di aggancio] viene mostrato l’elenco, ordinato per sequenza, degli elementi che in base alle regole, è possibile selezionare per indicare il punto di aggancio. I dati mostrati sono: Asset, SAP ID, Sequenza, POSNR, descrizione e unità impianti.

Nel medesimo dialogo della funzione di riassociazione, in basso, è presente la tabella della preview dei dati che verranno generati. La finalità di questa preview è quella di fornire all’utente un quadro chiaro dei soli dati che saranno riassociati (compresi eventuali elementi nuovi), con la possibilità di aggiornare preventivamente la componente variabile dei SAP ID delle campate (quelle dei sostegni sono da queste dipendenti). Per quanto riguarda la data [In funzione dal], per gli asset riassociati non varia, mentre per gli eventuali asset nuovi, dovrà essere inserita.

Prima di salvare l’utente cliccherà un pulsante per eseguire la verifica dei dati; se la verifica fallisce, sarà avvisato del motivo, con la possibilità di aggiornare quanto necessario, se la verifica da esito positivo, potrà salvare. Tutti i dati della maschera di preview sono in una memoria temporanea e il tentativo di salvataggio di dati parzialmente incorretti provocherà l’annullamento dell’intera operazione, preservando la forma originale delle linee coinvolte dalla richiesta di modifica.

## Gestione sequenza POSNR

Attualmente il POSNR delle derivazioni viene generato proseguendo la numerazione massima raggiunta nel complessivo dalla linea (dorsale + derivazioni). Questo comportamento dovrà essere modificato, riprendendo la numerazione da zero per ogni derivazione e sub derivazione, come nell’esempio di seguito riportato.

Il nuovo comportamento per la generazione del POSNR varrà solo per la funzionalità di riassociazione asset oggetto di questo documento e solo quando la riassociazione coinvolgerà anche le derivazioni. Questo nuovo comportamento non sarà replicato sulle altre funzioni di Maximo e tantomeno sono previste delle bonifiche sul complessivo delle linee.

Figura 2 - Modifica del calcolo del POSNR

## Desiderata utente

In questo paragrafo sono riportati quei desiderata dell’utente che esprimono specifiche necessità di dettaglio alle quali è possibile dare una risposta di fattibilità solamente in una fase avanzata dell’implementazione, essendo simulabili solo avendo a disposizione una quota rilevante della soluzione già sviluppata.

Nello specifico devono essere indagate le possibilità di:

Mostrare l’intera linea nell’elenco di selezione per popolare [Sostegno/Estremo terminale], bloccando le selezioni non consentite.

Incorporare la funzione del pulsante di verifica nel pulsante di OK, prevenendo comunque la perdita dei dati in caso di incoerenze, ma gestendo un messaggio di blocco. In caso affermativo il pulsante di verifica verrebbe eliminato.

# Scenari di utilizzo

I seguenti scenari sono stati pensati per spiegare graficamente le tipologie di situazioni che la funzionalità dovrà affrontare. In ogni scenario è visualizzata sia la situazione precedente alla modifica che quella successiva, sia per la linea che cede che per quella che acquisisce. Gli scenari esclusi vengono bloccati, non permettendo l’esecuzione della riassociazione. Dovessero emergere con l’uso ulteriori scenari non gestiti, ovvero situazioni di riassociazione che portano a inconsistenze nei dati, verranno impedite estendendo i controlli di esclusione.

In tutti gli scenari è stata impiegata una simbologia di forme e colori, come riportato nell’immagine seguente:

Figura 3 - Legenda forme & colori

L’esecuzione di questa funzionalità, come effetto aggiuntivo alla riassociazione, può provocare:

L’eliminazione di una campata, con conseguente cambio stato in demolito e disassociazione dalla linea.

La sola disassociazione della campata dalla linea, in caso di doppia terna, sino a quando la stessa rimane associata all’altra linea; il cambio stato in demolito avverrà solo quando non saranno più presenti relazioni della campata con altre linee

La creazione di una campata avviene come conseguenza del collegamento del sostegno presente nella linea di destinazione con il sostegno della linea da riassociare; la campata, creata completa degli asset definiti nel modello standard per la singola terna, dovrà essere successivamente arricchita dagli utenti con le informazioni anagrafiche specifiche, ossia quelle gestite negli attributi.

Eventuali scenari non previsti devono essere considerati di default non gestiti dalla funzionalità di riassociazione e quindi bloccati; uno scenario non previsto non potrà automaticamente essere considerato uno scenario da aggiungere, in quanto sarà necessario passare attraverso la fase di formalizzazione del requisito.

## Scenari standard

Gli scenari standard riguardano le possibilità di operare sulle linee in singola terna, sia in aereo che in cavo. In questo paragrafo sono stati inseriti 3 dei 14 scenari analizzati nel documento di richiesta funzionale approvato; per l’elenco completo vedere il documento ApprofondimentoRiassociazione.pptx.

### Riassociazione di una porzione di linea in una linea vuota

Figura 4 - Riassociazione di una porzione di linea in una linea vuota

### Riassociazione di una porzione di linea a inizio altra linea

Figura 5 - Riassociazione di una porzione di linea a inizio altra linea

### Riassociazione di una porzione di linea a una linea in cavo

Figura 6 - Riassociazione di una porzione di linea a una linea in cavo

## Scenari speciali

Gli scenari speciali riguardano le possibilità di operare sulle linee in doppia terna oppure ammazzettate o riassociare un’intera linea, oppure operare sulle derivazioni.

### La riassociazione di una linea con degli ammazzettamenti

Figura 7 - La riassociazione di una linea con degli ammazzettamenti

### Gestione della doppia terna

In Figura 8 e Figura 9 sono mostrate due operazioni di riassociazione che insistono sulla medesima campata in doppia terna; la prima operazione non cambia lo stato della campata 4, ma provoca la sola disassociazione dalla linea C, mentre la seconda operazione, oltre a provocare la disassociazione della campata 4 dalla linea A ne modifica anche lo stato in demolito. Lo stato non può essere variato per gli elementi in doppia terna, in quanto è un’informazione condivisa tra le due linee di appartenenza.

Figura 8 - Gestione della doppia terna caso a-1

Figura 9 - Gestione della doppia terna caso a-2

Figura 10 - Gestione della doppia terna caso b

Figura 11 - Gestione della doppia terna caso c

### La riassociazione totale

La riassociazione totale dei secondi livelli di una linea. Il primo livello vuoto resta in gestione all’utente.

Figura 12 - La riassociazione totale

### La riassociazione di una porzione di derivazione

La riassociazione di una porzione di derivazione ad una linea provocherà l’elezione della derivazione a dorsale, tranne le sue eventuali SUB derivazioni che rimarranno derivazioni.

Figura 13 - La riassociazione di una porzione di derivazione

### La riassociazione di una porzione di dorsale

La riassociazione di una porzione di dorsale ad una derivazione provocherà la modifica della dorsale a derivazione, tranne le sue eventuali SUB derivazioni che rimarranno derivazioni.

Figura 14 - La riassociazione di una porzione di dorsale

## Scenari esclusi

L’impedimento dell’esecuzione della funzione di riassociazione può avvenire con differenti modalità:

A priori, non sono mostrando nei domini di selezione (Sostegno/Estremo di selezione o aggancio) gli asset contrari alle regole prestabilite.

Successivamente alle scelte fatte, dopo l’azione sul pulsante di verifica dei dati, oppure, in ultimo, dopo l’azione sul pulsante OK per applicare le modifiche.

### Sezionamento tratta in cavo

Non deve essere permesso il sezionamento di una linea eseguito in una porzione di tratta in cavo. Questo si ottiene escludendo la possibilità di selezionare gli estremi cavo che hanno come figlio un giunto, sia indicando gli asset da riassociare che scegliendo il punto di aggancio sulla linea di destinazione. La verifica deve tenere anche conto che il “lato” della riassociazione non può essere quello dove è presente la tratta cavo.

### Sezionamento interno ad uno split o una derivazione con rientro

Non deve essere consentito né il sezionamento all’interno di uno split e né all’interno di una derivazione con rientro. Vedi Figura 15, caso 1.

### Selezione asset precedenti a sostegno da cui partono più campate

Non deve essere possibile eseguire la riassociazione che inizia ad un sostegno da cui partono più campate, selezionando [Includi asset precedenti]. Vedi Figura 15, caso 2.

### Selezione asset precedenti a sostegno di derivazione

Non deve essere consentito indicare un sostegno o un estremo terminale riferito ad una derivazione, selezionando [Includi asset precedenti]. Sono i casi 3 e 4 della figura seguente.

Figura 15 - Scenari esclusi a

### Aggancio a un sostegno da cui partono più campate

Non deve essere permesso l’aggancio su un sostegno di una linea da cui parte una o più derivazioni. L’azione porterebbe a delle ambiguità non risolvibili senza l’aggiunta di altri dati in input, i quali potrebbero però portare gli utenti a commettere errori usando la funzione di riassociazione.

Figura 16 - Scenari esclusi b

# Documenti allegati