# Test fix GEOCALL changeStatusOdl - Punti 4 e 5 (“EAM Generic error”)

| Servizio | changeStatusOdl - http://soa.terna.it/GEOCALL/Odl_Status/1.0 (microservizio eam-integration, GeoCallHandler) |
| --- | --- |
| Branch fix | GEOCALL_changeStatus_EAM0004_EAM0040_fix_master (da master) |
| Data test | 10/11/2026 |
| Origine | Analisi “Analisi flussi GEOCALL KO - EAM Generic error.xlsx”, righe 4 e 5 (solo errori da fixare nei microservizi) |

## 1. Oggetto delle fix

| Punto | Errore osservato | Casi | Causa nel codice | Fix |
| --- | --- | --- | --- | --- |
| 4 | EAM generic error. Index 0 out of bounds for length 0 | 672 | createChangeStatusDTO() verificava solo getMember() != null; con lista vuota get(0) genera IndexOutOfBoundsException, mappata su EAM_0000 | Controllo isNotEmpty(getMember()): OdL non presente -> EAM_0004 |
| 5 | Modifica non possibile. WORKORDER in stato non idoneo | 1827 | checkStatoNonEseguito() bloccava solo lo stato NON ESEGUITO; ESEGUITO e CHIUSO arrivavano a Maximo | Il controllo blocca con EAM_0040 anche ESEGUITO e CHIUSO |

## 2. Modalita’ del test

La chiamata SOAP changeStatusOdl_Input viene costruita (JAXB) e inviata a GeoCallHandler.changeStatusOdl() tramite il test JUnit GeoCallHandlerChangeStatusOdlTest. Maximo e DB sono simulati (mock): non si tratta di una chiamata verso collaudo. - Pre-fix: codice di master (senza modifiche). - Post-fix: codice del branch di fix. - Le stesse request sono usate nei due casi; request e response sono salvate in docs/test-evidence/{pre-fix,post-fix}.

Comando:

mvn -s ~\.m2\settings-terna.xml test -Dtest=GeoCallHandlerChangeStatusOdlTest "-DargLine=-Devidence.dir=<cartella>"

## 3. Caso A - Punto 4: OdL inesistente (getMember() vuoto)

Precondizione: retrieveWorkorder("PRS-31204A-SAS-LEIN?") restituisce risposta Maximo con member = [].

Request (identica pre e post fix)

<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
<soapenv:Body>
<ns2:changeStatusOdl_Input xmlns:ns2="http://soa.terna.it/GEOCALL/Odl_Status/1.0">
    <CODICE_POSIZIONE>PRS-31204A-SAS-LEIN?</CODICE_POSIZIONE>
    <STATO_OPERAZIONE>ESEGUITO</STATO_OPERAZIONE>
    <DATA_CAMBIO_STATO>2026-11-10T12:15:53.446+01:00</DATA_CAMBIO_STATO>
    <STATO_ORDINE>CHIUSO</STATO_ORDINE>
</ns2:changeStatusOdl_Input>
</soapenv:Body>
</soapenv:Envelope>

PRE-FIX - ERRORE (log: Generic Error: Index: 0)

<ns2:changeStatusOdl_Output xmlns:ns2="http://soa.terna.it/GEOCALL/Odl_Status/1.0">
    <ESITO>KO</ESITO>
    <CODICE_ERRORE>EAM_0000</CODICE_ERRORE>
    <DESCRIZIONE_ERRORE>EAM generic error.</DESCRIZIONE_ERRORE>
</ns2:changeStatusOdl_Output>

POST-FIX - errore gestito correttamente

<ns2:changeStatusOdl_Output xmlns:ns2="http://soa.terna.it/GEOCALL/Odl_Status/1.0">
    <ESITO>KO</ESITO>
    <CODICE_ERRORE>EAM_0004</CODICE_ERRORE>
    <DESCRIZIONE_ERRORE>Work Order code not present in EAM.</DESCRIZIONE_ERRORE>
</ns2:changeStatusOdl_Output>

Esito: la risposta passa da errore generico EAM_0000 (eccezione non gestita) a EAM_0004 con messaggio esplicito.

## 4. Caso B - Punto 5: OdL gia’ in stato ESEGUITO

Precondizione: getWorkorderStatus("WO-ESEGUITO") restituisce stato ESEGUITO; l’OdL esiste in Maximo (mock risponde 200 all’aggiornamento).

Request (identica pre e post fix)

<ns2:changeStatusOdl_Input xmlns:ns2="http://soa.terna.it/GEOCALL/Odl_Status/1.0">
    <CODICE_POSIZIONE>WO-ESEGUITO</CODICE_POSIZIONE>
    <STATO_OPERAZIONE>ESEGUITO</STATO_OPERAZIONE>
    <DATA_CAMBIO_STATO>2026-11-10T12:15:53.431+01:00</DATA_CAMBIO_STATO>
    <STATO_ORDINE>ESEGUITO</STATO_ORDINE>
</ns2:changeStatusOdl_Input>

PRE-FIX - ERRORE: il controllo non blocca, la richiesta di cambio stato prosegue verso Maximo (in produzione e’ qui che si genera l’errore/KO a valle).

<ns2:changeStatusOdl_Output xmlns:ns2="http://soa.terna.it/GEOCALL/Odl_Status/1.0">
    <ESITO>OK</ESITO>
</ns2:changeStatusOdl_Output>

(Il test attende KO EAM_0040: fallisce con expected:<KO> but was:<OK>.)

POST-FIX - bloccato prima di Maximo

<ns2:changeStatusOdl_Output xmlns:ns2="http://soa.terna.it/GEOCALL/Odl_Status/1.0">
    <ESITO>KO</ESITO>
    <CODICE_ERRORE>EAM_0040</CODICE_ERRORE>
    <DESCRIZIONE_ERRORE>Modifica non possibile. WORKORDER in stato non idoneo [wonum: WO-ESEGUITO]</DESCRIZIONE_ERRORE>
</ns2:changeStatusOdl_Output>

## 5. Caso C - Punto 5: OdL gia’ in stato CHIUSO

Stessa request del caso B con CODICE_POSIZIONE = WO-CHIUSO; precondizione: stato CHIUSO.

|  | ESITO | CODICE_ERRORE | Note |
| --- | --- | --- | --- |
| PRE-FIX | OK | - | cambio stato inoltrato a Maximo |
| POST-FIX | KO | EAM_0040 | Modifica non possibile. WORKORDER in stato non idoneo [wonum: WO-CHIUSO]; nessuna chiamata bulk a Maximo (verificato nel test) |

## 6. Casi di controllo

| Scenario | Pre-fix | Post-fix |
| --- | --- | --- |
| OdL NON ESEGUITO | KO EAM_0040 | KO EAM_0040 (invariato) |
| OdL AVVIATO (stato idoneo, regressione) | OK, update bulk invocato | OK, update bulk invocato |

## 7. Riepilogo esiti

| Test | Pre-fix | Post-fix |
| --- | --- | --- |
| punto4_odlInesistenteMemberVuoto_EAM_0004 | FAIL (EAM_0000) | PASS |
| punto5_odlEseguito_EAM_0040 | FAIL (OK) | PASS |
| punto5_odlChiuso_EAM_0040 | FAIL (OK) | PASS |
| punto5_odlNonEseguito_EAM_0040 | PASS | PASS |
| regressione_statoIdoneo_nonBloccato | PASS | PASS |
| Totale | 2/5 PASS | 5/5 PASS |

## 8. Note e passi successivi

Le response complete e i log Maven sono in docs/test-evidence/pre-fix e post-fix.

Test simulato con mock: resta da eseguire lo smoke test SoapUI sul pod di collaudo con le stesse request, verificando anche la riga X4ESITOINT (EAM_0004 / EAM_0040).

Impatto per GeoCall: per questi casi riceve EAM_0004 / EAM_0040 invece di EAM_0000/esito a valle; da comunicare.