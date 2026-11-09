# Terna__Presa in carico del servizio_Verbale di passaggio di consegne_v1.0

| VERBALE DI PASSAGGIO DI CONSEGNE “Presa in carico del Servizio AMS Anagrafica EAM” |
| --- |

Approvazione ed emissione

| Redatto da | Data | Approvato da | Data |
| --- | --- | --- | --- |
| Marco Lerro | 25/11/2025 |  |  |

INDICE

1. Generalità 4

1.1. Contesto di riferimento 4

1.2. Moduli/Processi in ambito 4

1.3. Piano delle interviste 4

1.4. Documentazione raccolta e analizzata 6

2. Anagrafica EAM 7

2.1. Riepilogo 7

2.2. Rischi 7

REVISIONI

| Versione | Autore | Data | Descrizione |
| --- | --- | --- | --- |
| 0.1 | Marco Lerro | 25/11/2026 | Prima stesura |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

ACRONIMI E ABBREVIAZIONI

| Elemento | Definizione |
| --- | --- |
| ASM | Application Management Services |
| EAM | Enterprise Asset Management |
|  |  |
|  |  |

RIFERIMENTI

| Nome documento | Versione | Formato | Descrizione |
| --- | --- | --- | --- |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |
|  |  |  |  |

# Generalità

Contesto di riferimento

In virtù dell’esito di aggiudicazione della gara n° 0000047243, si rende necessario pianificare l’avvicendamento tra il Fornitore Uscente e il personale del fornitore entrante (RTI Indra / Engineering) che prenderà in carico il servizio di gestione ed evoluzione della Piattaforma EAM per le funzionalità di Anagrafica Asset (Linee e Stazioni) comprensiva di moduli aggiuntivi (SmartView e Strong Authentication) e Connettore di sincronizzazione anagrafica EAM/SAP ASIS di Terna.

Il servizio di Presa in carico ha quindi come obiettivo principale il knowledge transfer da parte del fornitore uscente, di terzi e di Terna verso il RTI entrante.

Il presente documento contiene il verbale di fine trasferimento del know-how per ciascun modulo in ambito.

Moduli/Processi in ambito

Di seguito il dettaglio di tutti i moduli in ambito.

| Modulo/processo | Referente Terna | In sviluppo | In collaudo | In preproduzione | In esercizio | In esercizio da | Usato in esercizio | Note |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Anagrafica Asset Linee/Stazioni EAM |  | SI | SI | SI | SI | Gennaio 2025 | Gennaio 2025 |  |
| Connettore per la sincronizzazione Anagrafica Linee/Stazioni EAM / SAP |  | SI | SI | SI | SI | Gennaio 2025 | Gennaio 2025 |  |
| SmartView |  | SI | SI | SI | SI | Marzo 2025 | Marzo 2025 |  |
| Strong Authentication |  | SI | SI | SI | SI | Gennaio 2025 | Gennaio 2025 |  |
|  |  |  |  |  |  |  |  |  |

Tabella 1 - Moduli in ambito

Piano delle interviste

Di seguito il dettaglio delle interviste svolte con il fornitore uscente per ciascun modulo in ambito (le date dove non espresse sono riferite all’anno 2025).

| Modulo /Processo | Meeting | Meeting | Meeting | Meeting | Meeting | Meeting |
| --- | --- | --- | --- | --- | --- | --- |
| Modulo /Processo | Sessione | Data | Agenda | Argomenti non coperti | Mitigation | Due date mitigation |
|  |  |  |  |  |  |  |
| Anagrafica EAM Linee | 1 | 10 Novembre | File di mapping - introduzione e commenti Creazione asset singolo secondo livello Creazione massiva asset secondo livello Creazione asset singolo quarto livello Creazione asset relazionato di quarto livello Sostituzione asset singola di quarto livello |  |  |  |
| Anagrafica EAM Linee | 2 | 12 Novembre | Creazione di una derivazione Creazione doppia Terna Creazione incrocio Ammazzettamento Doppia Terna Disammazzettamento Cambio stato asset di secondo livello Cambio stato asset di quarto livello |  |  |  |
| Anagrafica EAM Linee | 3 | 14 Novembre | Attributi e popolamenti automatici Tronchi di ispezione Ricodifica SapId Cambio tracciato linea Entra esci linee | Attributi e popolamenti automatici | Richiesta sessione di recupero. Supporto Accenture fino a espletamento KT. | 5 Dicembre |
| Anagrafica EAM Linee | 4 | 17 Novembre | Creazione Split Tratta di tesatura Crea legame linee Bonifica sostegni campate doppia terna SAL Ticketing AMS | Crea legame linee Bonifica sostegni campate doppia terna | Richiesta sessione di recupero. Supporto Accenture fino a espletamento KT. | 5 Dicembre |
| Anagrafica EAM Stazioni | 5 | 19 Novembre | File di mapping - introduzione e commenti Gestione tabelle verità Creazione asset di 2,3,4 livello Attributi e popolamenti automatici Cambio stato 1,2,3,4 livello Spostamento Asset | Attributi e popolamenti automatici |  |  |
| Anagrafica EAM Stazioni | 6 | 21 Novembre | Ricodifca SAPID Crea legami linea-stazioni / stazione-stazione Catalogo costruttore componente tipo Funzione elemento fase Abilitazione gruppi-utenti (gruppi di persone) |  |  |  |
| Anagrafica EAM | 7 | 24 Novembre | Fix su Anagrafica EAM indotte da NRT wave 2 e funzionalità wave 2 | Mcm blindati: recupereremo quando Mario rientrerà dalle ferie Asset dismessi: serve sessione di recupero con Michelangelo Frillici Asset fittizi: serve sessione di recupero con Roberta Soriano | Le fix non sono ancora state lasciate su produzione. Si concorderà con Accenture la modalità di retrofit delle fix a completamento delle stesse. Prevista sessione di recupero per gli argomenti non trattati. | 5 Dicembre |
| Anagrafica EAM | 8 | 1° Dicembre | Governance e processi wave 1 |  |  |  |
| AMS Anagrafica EAM | 9 | Da 24 Novembre a 5 Dicembre | Shadowing KT Analisi attività AMS in carico Accenture Previste 10 sessioni da 1h programmate giornalmente |  |  |  |
| Anagrafica EAM Connettore | 10 | 10 Dicembre | Step 3 |  | Supporto Accenture fino a espletamento KT. |  |
| Anagrafica EAM Connettore | 11 | 15 Dicembre | Step 4 |  | Supporto Accenture fino a espletamento KT. |  |
| Anagrafica EAM SmartView | 12 | 17 Dicembre | SmartView |  | Supporto Accenture fino a espletamento KT. |  |
| Anagrafica EAM Strong Authentication | 13 | 17 Dicembre | Strong Authentication |  | Supporto Accenture fino a espletamento KT. |  |
|  |  |  |  |  |  |  |

Tabella 2 - Piano delle interviste

Documentazione raccolta e analizzata

Per completare il knowledge transfer è stata raccolta ed analizzata la documentazione condivisa da Terna e dal fornitore uscente.

Il dettaglio di tutti i documenti raccolti e analizzati è riportato di seguito.

| Modulo/Processo | Documento | Formato | Data di condivisione | Descrizione breve |
| --- | --- | --- | --- | --- |
|  |  |  |  |  |
| Anagrafica EAM | EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Linee_20230713_V11.docx | Word | 11 Novembre | High Level Design Anagrafica Linee |
| Anagrafica EAM | EAM_LLD_GEN_ANA_Documento Anagrafica di LLD_20230703_V7.docx | Word | 11 Novembre | High Level Design Anagrafica Linee |
| Anagrafica EAM | Mapping Anagrafica EAM AM_Linee rev.9_01092025 .xlsx | Excel | 11 Novembre | Mapping Anagrafica Linee EAM/SAP |
| Anagrafica EAM | EAM_HLD_GEN_ANA_Data Model e Funzionalità Applicative Anagrafica Stazioni_20231219_V3.7.docx | Word | 11 Novembre | High Level Design Anagrafica Stazioni |
| Anagrafica EAM | EAM_LLD_GEN_ANA_Documento Anagrafica di LLD Stazioni_20240222_V3.docx | Word | 11 Novembre | High Level Design Anagrafica Stazioni |
| Anagrafica EAM | Mapping Anagrafica EAM AM_Stazioni rev.2_031120255.xlsx | Excel | 11 Novembre | Mapping Anagrafica Stazioni EAM/SAP |
| Anagrafica EAM | Elenco Script Anagrafica.xlsx | Excel | 21 Novembre | Documento interno con elenco automation script (logica applicativa) linee e stazioni. |
|  |  |  |  |  |

Tabella 3 – Documentazione raccolta e analizzata

Al momento è stata raccolta la documentazione tecnica essenziale per l’operatività AMS. È stata inoltrata la richiesta verso Accenture di avere il resto della documentazione tecnica a stretto giro (due date 15 Dicembre).

# Anagrafica EAM

Riepilogo

| Modulo/Processo | Referente Terna | Data presa in carico | In sviluppo | In collaudo | In preproduzione | In esercizio | In esercizio da | Usato in esercizio | Note |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Anagrafica EAM Linee e Stazioni |  | 1 Dicembre 2025 | SI | SI | SI | SI | Gennaio 2025 | SI | Support Accenture per temi non coperti |
| Connettore Anagrafica EAM / SAP |  | 1 Dicembre 2025 | SI | SI | SI | SI | Gennaio 2025 | SI | Support Accenture per temi non coperti |
| SmartView |  | 1 Dicembre 2025 | SI | SI | SI | SI | Gennaio 2025 | SI | Support Accenture per temi non coperti |
| Strong Authentication |  | 1 Dicembre 2025 | SI | SI | SI | SI | Gennaio 2025 | SI | Support Accenture per temi non coperti |

Tabella 4 – Riepilogo moduli

Rischi

Di seguito sono riportati i rischi individuati alla conclusione del passaggio di consegne per il modulo Anagrafica Asset (Linee e Stazioni) comprensivo di moduli aggiuntivi (SmartView e Strong Authentication) e Connettore di sincronizzazione anagrafica EAM/SAP.

| Rischio | Livello | Mitigation | Azione di recupero | Due date azione di recupero |
| --- | --- | --- | --- | --- |
| Argomenti non coperti da KT | Basso | SI | Pianificate sessioni di recupero. Assistenza Accenture in caso di necessità. | 17 Dicembre |
| Documentazione incompleta | Basso | SI | Richiesta documentazione mancante | 15 Dicembre |
| Warm-up team AMS | Basso | SI | Affiancata risorse Indra su parte anagrafica con esperienza solida su Wave 1. Continuità personale AMS su connettore. | 1 Gennaio 2026 |
| Attività pendenti su AMS (Accenture) | Basso | SI | Sessioni di shadowing Accenture / Indra. | 5 Dicembre |
| Attività pendenti su sviluppo Wave 2 (Accenture) | Basso | SI | Pianificata sessione di recupero e definizione processo di retrofit | 5 Dicembre |
| Gestione periodo di transizione Accenture / Indra | Basso | SI | Sessioni di shadowing Accenture / Indra. | 5 Dicembre |
| Disponibilità ambienti AMS | Basso | SI | Concordato con ICT Terna procedura di refresh di ambiente di preproduzione | 2 Dicembre |

Tabella 5 – Rischi
