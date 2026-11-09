"""
X4CHANGE2LVL_SNOW
Inserisce una riga in X4RENAMESAPIDSNOW alla modifica di un Sap Id
v1.0 - 05/11/2025
"""
from psdi.server import MXServer
from psdi.util.logging import MXLoggerFactory
from java.lang import System

prefix = "[ X4CHANGE2LVL_SNOW ] "
logger = MXLoggerFactory.getLogger("maximo.terna")

# Verifica se il Sap Id è cambiato e il valore iniziale non era vuoto
prevSapId = mbo.getMboValue("x3sapid").getInitialValue().asString()
if mbo.isModified("x3sapid") and prevSapId != "":
	start = System.currentTimeMillis()
	# Recupera il nuovo Sap Id
	newSapId = mbo.getString("x3sapid")
	logger.debug(prefix + "PrevSapId: %s - NewSapId: %s"%(prevSapId, newSapId))
	# Recupera tutti gli asset impattati dalla rinomina (il secondo livello e discendenti)
	assetMS = mbo.getMboSet("$RENAMEASSETS", "ASSET", """siteid = :siteid AND ASSETNUM IN (
	SELECT c.assetnum FROM ASSETANCESTOR aa
	JOIN ASSET c ON c.ASSETNUM = aa.ASSETNUM AND c.SITEID = aa.SITEID AND (c.X3ROOTASSETNUM IS NULL OR c.X3ROOTASSETNUM = :sourceassetnum)
	LEFT JOIN ASSET p ON p.ASSETNUM = c.PARENT AND p.SITEID = c.SITEID
	WHERE aa.ANCESTOR = :targetassetnum AND aa.SITEID = :siteid AND (p.X3ROOTASSETNUM IS NULL OR p.X3ROOTASSETNUM = :sourceassetnum)
)""")
	# Aggiunge 1 riga per ogni asset nella tabella X4RENAMESAPIDSNOW
	renameIntMS = mbo.getMboSet("$X4RENAMESAPIDSNOW_RENAME", "X4RENAMESAPIDSNOW", "1=0")
	now = MXServer.getMXServer().getDate()
	for i in range(assetMS.count()):
		asset = assetMS.getMbo(i)
		logger.debug(prefix + "Adding asset %s to X4RENAMESAPIDSNOW"%asset.getString("assetnum"))
		renameInt = renameIntMS.add()
		renameInt.setValue("siteid", asset.getString("siteid"))
		renameInt.setValue("assetnum", asset.getString("assetnum"))
		renameInt.setValue("sapid_orig", prevSapId)
		renameInt.setValue("sapid_dest", newSapId)
		renameInt.setValue("da_notificare_snow", "Y")
		renameInt.setValue("updatetime", now)
	logger.debug(prefix + "Total time execution (s): %s"%((System.currentTimeMillis() - start) / 1000.0))"
X4LOADER_WORKER_LIB,,Draft,"from psdi.server import MXServer
from psdi.util.logging import MXLoggerFactory
from psdi.util import MXException
from java.lang import Exception as JavaException
from java.util import Date, Calendar
from java.math import BigDecimal, BigInteger
from java.lang import Integer, Long, Double, Float, Boolean
from java.util import List
from psdi.mbo import MboConstants
from java.text import SimpleDateFormat
try:
    from com.ibm.json.java import JSONObject
except:
    JSONObject = None

from java.time import LocalDate, LocalDateTime, OffsetDateTime, ZoneId
from java.time.format import DateTimeFormatter, DateTimeFormatterBuilder
from java.time.temporal import ChronoField

logger = MXLoggerFactory.getLogger("maximo.script")

ISO_LOCAL_DT_FLEX = DateTimeFormatterBuilder() \
    .append(DateTimeFormatter.ISO_LOCAL_DATE) \
    .optionalStart().appendLiteral('T').optionalEnd() \
    .optionalStart().appendLiteral(' ').optionalEnd() \
    .optionalStart() \
        .appendValue(ChronoField.HOUR_OF_DAY, 2).appendLiteral(':') \
        .appendValue(ChronoField.MINUTE_OF_HOUR, 2) \
        .optionalStart().appendLiteral(':').appendValue(ChronoField.SECOND_OF_MINUTE, 2).optionalEnd() \
        .optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, True).optionalEnd() \
    .optionalEnd() \
    .toFormatter()
DATE_FIELDS_TO_NORMALIZE = set([
    "TARGSTARTDATE",
    "TARGCOMPDATE",
    "SNECONSTRAINT",
    "FNLCONSTRAINT",
    "X4DATAINIZIORISORSA",
    "X4DATAFINERISORSA",
    "ACTFINISH"
])

IT_DATE_FMT = SimpleDateFormat("dd/MM/yyyy")
IT_DT_MIN_FMT = SimpleDateFormat("dd/MM/yyyy HH:mm")
IT_DT_SEC_FMT = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

IT_DATE_FMT.setLenient(False)
IT_DT_MIN_FMT.setLenient(False)
IT_DT_SEC_FMT.setLenient(False)

def get_mother_assetnums(assetnum, siteid, userInfo):
    """
    Restituisce l'insieme delle LINEE/STAZIONI madri valide dell'asset.

    LINEA:
      L3/L4 -> risale ASSET.PARENT fino a L2
      L2    -> ASSETLOCRELATION.TARGETASSETNUM
               -> uno o piu SOURCEASSETNUM = LINEA
               (gestisce anche campate in doppia terna)

    STAZIONE:
      risale ASSET.PARENT fino alla radice.
    """

    current = str(assetnum).strip()
    visited = set()

    while current:
        if current in visited:
            raise ValueError(
                "Gerarchia asset ciclica rilevata per ASSETNUM '%s'."
                % str(assetnum)
            )

        visited.add(current)

        # ---------------------------------------------------------
        # 1. Verifico se current è un secondo livello di una LINEA
        # ---------------------------------------------------------
        relSet = None
        try:
            relSet = mxs.getMboSet("ASSETLOCRELATION", userInfo)

            relSet.setWhere(
                "targetassetnum='%s'"
                % sql_escape(current)
            )
            relSet.reset()

            source_assetnums = set()

            rel = relSet.moveFirst()

            while rel is not None:
                if not rel.isNull("SOURCEASSETNUM"):
                    source = rel.getString("SOURCEASSETNUM").strip()

                    if source != "":
                        source_assetnums.add(source)

                rel = relSet.moveNext()

            # Se ho trovato una o più LINEE madri,
            # le restituisco tutte.
            if len(source_assetnums) > 0:
                return source_assetnums

        finally:
            try:
                if relSet is not None:
                    relSet.close()
            except:
                pass

        # ---------------------------------------------------------
        # 2. Non è L2 di una linea: provo a risalire ASSET.PARENT
        # ---------------------------------------------------------
        assetSet = None

        try:
            assetSet = mxs.getMboSet("ASSET", userInfo)
            assetSet.setWhere(
                "assetnum='%s' and siteid='%s'"
                % (
                    sql_escape(current),
                    sql_escape(siteid)
                )
            )
            assetSet.reset()

            asset = assetSet.moveFirst()

            if asset is None:
                return set()

            # Se siamo arrivati a una STAZIONE radice
            if not asset.isNull("CLASSIFICATIONID"):
                classification = asset.getString(
                    "CLASSIFICATIONID"
                ).strip().upper()

                if classification == "STAZIONE":
                    return set([
                        asset.getString("ASSETNUM").strip()
                    ])

                # Se viene passato direttamente un asset LINEA L1
                if classification == "LINEA":
                    return set([
                        asset.getString("ASSETNUM").strip()
                    ])

            if asset.isNull("PARENT"):
                return set()

            parent = asset.getString("PARENT").strip()

            if parent == "":
                return set()

            current = parent

        finally:
            try:
                if assetSet is not None:
                    assetSet.close()
            except:
                pass

    return set()


def validate_parent_asset_coherence(payload_obj, siteid, userInfo):
    parent_wonum = get_payload_value_ci(payload_obj, "PARENT")

    if is_blank_domain_value(parent_wonum):
        return

    assetnum = get_payload_value_ci(payload_obj, "ASSETNUM")

    if is_blank_domain_value(assetnum):
        raise ValueError(
            "ASSETNUM mancante: impossibile verificare la coerenza con il PARENT."
        )

    mother_assetnums = get_mother_assetnums(
        assetnum,
        siteid,
        userInfo
    )

    if mother_assetnums is None or len(mother_assetnums) == 0:
        raise ValueError(
            "Impossibile determinare la linea/stazione madre "
            "dell'asset '%s'."
            % str(assetnum)
        )

    parentSet = None

    try:
        parentSet = mxs.getMboSet("WORKORDER", userInfo)
        parentSet.setWhere(
            "wonum='%s' and siteid='%s'"
            % (
                sql_escape(parent_wonum),
                sql_escape(siteid)
            )
        )
        parentSet.reset()

        parent = parentSet.moveFirst()

        if parent is None:
            raise ValueError(
                "Parent '%s' non trovato."
                % str(parent_wonum)
            )

        if parent.isNull("ASSETNUM"):
            raise ValueError(
                "Il parent '%s' non ha ASSETNUM valorizzato."
                % str(parent_wonum)
            )

        parent_assetnum = parent.getString(
            "ASSETNUM"
        ).strip()

        # Normalizzo per confronto case-insensitive.
        valid_mothers_upper = set([
            str(x).strip().upper()
            for x in mother_assetnums
        ])

        if parent_assetnum.upper() not in valid_mothers_upper:
            raise ValueError(
                "Parent non coerente: il WO padre '%s' ha ASSETNUM='%s', "
                "ma l'asset '%s' appartiene alla linea/stazione '%s'."
                % (
                    str(parent_wonum),
                    parent_assetnum,
                    str(assetnum),
                    ", ".join(sorted(mother_assetnums))
                )
            )

    finally:
        try:
            if parentSet is not None:
                parentSet.close()
        except:
            pass
def looks_like_it_date_or_datetime(txt):
    try:
        t = str(txt).strip()
        return len(t) >= 10 and t[2] == "/" and t[5] == "/"
    except:
        return False

def safe_error_string(e):
    if e is None:
        return u""

    # Eccezioni Maximo: mantiene anche {0}, {1}, ... valorizzati
    try:
        if isinstance(e, MXException):
            try:
                e.setForcedLangCode("IT")
            except:
                pass

            msg = e.getDisplayMessage()
            if msg:
                return unicode(msg)
    except:
        pass

    # Eccezione Java generica
    try:
        msg = e.getMessage()
        if msg:
            return unicode(msg)
    except:
        pass

    try:
        return unicode(e)
    except:
        pass

    try:
        return unicode(e.toString())
    except:
        pass

    return u"Errore non dettagliato"
def it_to_date_any(value):
    txt = str(value).strip()

    for fmt in [IT_DT_SEC_FMT, IT_DT_MIN_FMT, IT_DATE_FMT]:
        try:
            return fmt.parse(txt)
        except:
            pass

    return None


def looks_like_excel_serial(value):
    if value is None:
        return False

    try:
        if isinstance(value, Boolean):
            return False
    except:
        pass

    try:
        if isinstance(value, BigDecimal) or isinstance(value, BigInteger):
            return True
    except:
        pass

    try:
        if isinstance(value, Integer) or isinstance(value, Long) or isinstance(value, Double) or isinstance(value, Float):
            return True
    except:
        pass

    try:
        if isinstance(value, (int, long, float)):
            return True
    except:
        pass

    if isinstance(value, basestring):
        t = str(value).strip().replace(",", ".")

        if t == "":
            return False

        if t.count(".") > 1:
            return False

        parts = t.split(".")

        if len(parts) == 1:
            return parts[0].isdigit()

        return parts[0].isdigit() and parts[1].isdigit()

    return False


def excel_serial_to_date(value):
    try:
        if not looks_like_excel_serial(value):
            return None

        txt = str(value).strip().replace(",", ".")
        serial = float(txt)

        if serial <= 0:
            return None

        days = int(serial)
        fraction = serial - days

        # Evita di interpretare valori tipo 2026 come date Excel.
        # 30000 ~= anno 1982, 90000 ~= anno 2146.
        if days < 30000 or days > 90000:
            return None

        millis = int(round(fraction * 86400000))

        if millis >= 86400000:
            days += 1
            millis -= 86400000

        cal = Calendar.getInstance()
        cal.clear()

        # Excel/OLE base date: 1899-12-30.
        # Calendar usa mesi 0-based: 11 = dicembre.
        cal.set(1899, 11, 30, 0, 0, 0)

        cal.add(Calendar.DATE, days)

        hours = millis // 3600000
        millis = millis % 3600000
        minutes = millis // 60000
        millis = millis % 60000
        seconds = millis // 1000
        milliseconds = millis % 1000
        
        cal.set(Calendar.HOUR_OF_DAY, int(hours))
        cal.set(Calendar.MINUTE, int(minutes))
        cal.set(Calendar.SECOND, int(seconds))
        cal.set(Calendar.MILLISECOND, int(milliseconds))

        return cal.getTime()

    except:
        return None
def is_java_list(v):
    return isinstance(v, List)
    
def clear_parent_x4fisicoplan_if_needed(parent_wonum, siteid, userInfo):
    pset = None
    try:
        if parent_wonum is None or str(parent_wonum).strip() == "":
            return

        pset = mxs.getMboSet("WORKORDER", userInfo)
        pset.setWhere(
            "wonum='%s' and siteid='%s'" %
            (str(parent_wonum).replace("'", "''"), str(siteid).replace("'", "''"))
        )
        pset.reset()

        parent = pset.moveFirst()
        if parent is None:
            return

        status = parent.getString("STATUS")
        if status in ("APERTO", "DA VALIDARE"):
            parent.setValueNull("X4FISICOPLAN", 11L)
            pset.save()
    finally:
        try:
            if pset is not None:
                pset.close()
        except:
            pass

def looks_like_iso_date_or_datetime(txt):
    t = str(txt).strip()
    return len(t) >= 10 and t[4] == "-" and t[7] == "-" and not t[1].isalpha()

def iso_to_date_any(s, default_zone):
    if s is None:
        return None
    txt = str(s).strip()
    if txt == "":
        return None

    has_offset = txt.endswith("Z") or ("+" in txt[10:]) or ("-" in txt[10:] and txt.rfind("-") > 10)
    if has_offset:
        odt = OffsetDateTime.parse(txt)
        return Date.from(odt.toInstant())

    if len(txt) == 10:
        ld = LocalDate.parse(txt, DateTimeFormatter.ISO_LOCAL_DATE)
        return Date.from(ld.atStartOfDay(default_zone).toInstant())

    ldt = LocalDateTime.parse(txt, ISO_LOCAL_DT_FLEX)
    return Date.from(ldt.atZone(default_zone).toInstant())

def parse_json(body_str):
    if body_str is None or body_str.strip() == "":
        raise ValueError("BODY vuoto")
    if JSONObject is None:
        raise ValueError("JSON parser non disponibile")
    return JSONObject.parse(body_str)

def is_list_or_object(v):
    return ("java.util.List" in str(v.__class__)) or (isinstance(v, JSONObject))

ALLOWED_X4TIPOSPESA = set(["ANR", "OPEX", "CAPEX"])

def validate_x4tipospesa_payload(payload_obj):
    tipospesa = get_payload_value_ci(payload_obj, "X4TIPOSPESA")

    if is_blank_domain_value(tipospesa):
        return

    if is_list_or_object(tipospesa):
        raise ValueError("X4TIPOSPESA non valido: valore strutturato non ammesso.")

    ts = str(tipospesa).strip().upper()

    if ts not in ALLOWED_X4TIPOSPESA:
        raise ValueError(
            "X4TIPOSPESA non valido: '%s'. Valori ammessi: ANR, OPEX, CAPEX."
            % str(tipospesa)
        )
        
def set_typed(m, attr, value, default_zone):
    if value is None:
        return

    try:
        if str(value.__class__).endswith("JSONNull") or str(value) == "null":
            return
    except:
        pass
    
    uattr = str(attr).strip().upper()
    
    if uattr in DATE_FIELDS_TO_NORMALIZE:
        if isinstance(value, basestring) and str(value).strip() == "":
            return

        d = _coerce_date(value, default_zone)

        if d is None:
            raise ValueError(
                "%s non valido come data: '%s'. Formati ammessi: seriale Excel, ISO, dd/MM/yyyy HH:mm:ss."
                % (uattr, str(value))
            )

        if uattr in RESOURCE_DATE_FIELDS:
            # Durante il caricamento batch non eseguo lo script attributo:
            # il controllo completo verrà effettuato dopo tutte le WPLABOR.
            m.setValue(uattr, d, 11L)
        else:
            m.setValue(uattr, d)
        return


    if isinstance(value, basestring) and looks_like_iso_date_or_datetime(value):
        m.setValue(attr, iso_to_date_any(value, default_zone))
        return
    if str(m.getName()).upper() == "WPITEM" and uattr in ("ITEMQTY", "LINECOST"):
        m.setValue(uattr, str(value), 11L)
        if uattr=="LINECOST":
            qty=m.getDouble("ITEMQTY")
            m.setValue("UNITCOST", value/qty, 11L)
        return

    try:
        if isinstance(value, BigDecimal):
            try:
                if value.scale() == 0:
                    m.setValue(attr, value.longValue())
                else:
                    m.setValue(attr, value.doubleValue())
            except:
                m.setValue(attr, str(value))
            return

        if isinstance(value, BigInteger):
            m.setValue(attr, value.longValue())
            return
    except:
        pass

    if attr == "JPNUM":
        if m.isNull(attr):
            m.setValue(attr, str(value))
        return
    
    if attr == "LINECOST":
        m.setValue(attr, str(value))
        return

    if attr == "ITEMNUM":
        m.setValue(attr, str(value))
        return
    
    if attr=="X4COIN":
        m.setValue(attr, str(value))

    try:
        if isinstance(value, Boolean):
            m.setValue(attr, bool(value.booleanValue()))
            return
    except:
        pass

    try:
        if isinstance(value, (int, long, float)):
            m.setValue(attr, value)
            return
    except:
        pass

    m.setValue(attr, str(value))

def truncate_to_minute(date_value):
    if date_value is None:
        return None

    cal = Calendar.getInstance()
    cal.setTime(date_value)
    cal.set(Calendar.SECOND, 0)
    cal.set(Calendar.MILLISECOND, 0)

    return cal.getTime()


def validate_payload_wplabor_dates(payload_obj, workorder, default_zone):
    if workorder is None:
        return

    # Mantengo le stesse esclusioni dello script attributo.
    if workorder.getBoolean("X4AGGREGATI"):
        return

    if workorder.getBoolean("X4SERBATOIO"):
        return

    payload_wplabor = get_payload_value_ci(
        payload_obj,
        "WPLABOR"
    )

    # Nessuna WPLABOR nel caricamento: nessun controllo da effettuare.
    if payload_wplabor is None:
        return

    if payload_wplabor.isEmpty():
        return

    targ_start = truncate_to_minute(
        workorder.getDate("TARGSTARTDATE")
    )

    targ_finish = truncate_to_minute(
        workorder.getDate("TARGCOMPDATE")
    )

    if targ_start is None or targ_finish is None:
        raise ValueError(
            "TARGSTARTDATE e TARGCOMPDATE devono essere valorizzate "
            "per validare le date delle risorse."
        )

    if targ_start.compareTo(targ_finish) > 0:
        raise ValueError(
            "La data di inizio pianificata non puo essere maggiore "
            "della data di fine pianificata."
        )

    has_start_match = False
    has_finish_match = False

    row_number = 0
    arr_it = payload_wplabor.iterator()

    while arr_it.hasNext():
        row_number += 1
        row = arr_it.next()

        uid_value = get_payload_value_ci(
            row,
            "WPLABORUID"
        )

        uid = normalize_id_value(uid_value)

        if uid == "":
            row_description = "riga %d" % row_number
        else:
            row_description = "WPLABORUID %s" % uid

        raw_start = get_payload_value_ci(
            row,
            "X4DATAINIZIORISORSA"
        )

        raw_finish = get_payload_value_ci(
            row,
            "X4DATAFINERISORSA"
        )

        if is_blank_domain_value(raw_start):
            raise ValueError(
                "%s: X4DATAINIZIORISORSA mancante."
                % row_description
            )

        if is_blank_domain_value(raw_finish):
            raise ValueError(
                "%s: X4DATAFINERISORSA mancante."
                % row_description
            )

        resource_start = _coerce_date(
            raw_start,
            default_zone
        )

        resource_finish = _coerce_date(
            raw_finish,
            default_zone
        )

        if resource_start is None:
            raise ValueError(
                "%s: X4DATAINIZIORISORSA non valida: '%s'."
                % (
                    row_description,
                    str(raw_start)
                )
            )

        if resource_finish is None:
            raise ValueError(
                "%s: X4DATAFINERISORSA non valida: '%s'."
                % (
                    row_description,
                    str(raw_finish)
                )
            )

        resource_start = truncate_to_minute(
            resource_start
        )

        resource_finish = truncate_to_minute(
            resource_finish
        )

        # Inizio risorsa non precedente all'inizio WO.
        if resource_start.compareTo(targ_start) < 0:
            raise ValueError(
                "%s: la data di inizio risorsa %s "
                "non puo essere minore della data di inizio "
                "pianificata %s."
                % (
                    row_description,
                    str(resource_start),
                    str(targ_start)
                )
            )

        # Inizio risorsa non successivo alla fine WO.
        if resource_start.compareTo(targ_finish) > 0:
            raise ValueError(
                "%s: la data di inizio risorsa %s "
                "non puo essere maggiore della data di fine "
                "pianificata %s."
                % (
                    row_description,
                    str(resource_start),
                    str(targ_finish)
                )
            )

        # Fine risorsa non precedente all'inizio WO.
        if resource_finish.compareTo(targ_start) < 0:
            raise ValueError(
                "%s: la data di fine risorsa %s "
                "non puo essere minore della data di inizio "
                "pianificata %s."
                % (
                    row_description,
                    str(resource_finish),
                    str(targ_start)
                )
            )

        # Fine risorsa non successiva alla fine WO.
        if resource_finish.compareTo(targ_finish) > 0:
            raise ValueError(
                "%s: la data di fine risorsa %s "
                "non puo essere maggiore della data di fine "
                "pianificata %s."
                % (
                    row_description,
                    str(resource_finish),
                    str(targ_finish)
                )
            )

        # Inizio risorsa <= fine risorsa.
        if resource_start.compareTo(resource_finish) > 0:
            raise ValueError(
                "%s: la data di inizio risorsa %s "
                "non puo essere maggiore della data di fine risorsa %s."
                % (
                    row_description,
                    str(resource_start),
                    str(resource_finish)
                )
            )

        if resource_start.compareTo(targ_start) == 0:
            has_start_match = True

        if resource_finish.compareTo(targ_finish) == 0:
            has_finish_match = True

    # Controlli globali effettuati solo dopo aver letto tutte
    # le WPLABOR presenti nel body.
    if not has_start_match:
        raise ValueError(
            "Tra le WPLABOR caricate deve esserci almeno una "
            "X4DATAINIZIORISORSA coincidente con TARGSTARTDATE."
        )

    if not has_finish_match:
        raise ValueError(
            "Tra le WPLABOR caricate deve esserci almeno una "
            "X4DATAFINERISORSA coincidente con TARGCOMPDATE."
        )
        
def _cmp_dates(d1, d2):
    return d1.compareTo(d2)
def handle_jpnum_change_on_update(payload_obj, woSet, wo, default_zone):
    if not payload_contains_key_ci(payload_obj, "JPNUM"):
        return wo, False

    new_jpnum = get_payload_value_ci(payload_obj, "JPNUM")

    if is_blank_domain_value(new_jpnum):
        return wo, False

    if is_list_or_object(new_jpnum):
        return wo, False

    new_jpnum_s = str(new_jpnum).strip()

    old_jpnum_s = ""
    try:
        if not wo.isNull("JPNUM"):
            old_jpnum_s = str(wo.getString("JPNUM")).strip()
    except:
        old_jpnum_s = ""
        
    # Validazione: il nuovo JP deve avere la stessa macro attivita del JP attuale
    if old_jpnum_s != "" and ldr.getString("USERID")[0]!="X":
        validate_same_macroattivita_for_jp_change(
            old_jpnum_s,
            new_jpnum_s,
            wo.getUserInfo()
        )

    if old_jpnum_s == new_jpnum_s:
        return wo, False

    wonum = wo.getString("WONUM")
    siteid = wo.getString("SITEID")

    logger.info(
        "Cambio JPNUM su WO %s/%s: vecchio='%s', nuovo='%s'"
        % (wonum, siteid, old_jpnum_s, new_jpnum_s)
    )

    # 1. Rimuovo il work plan vecchio
    if not wo.isNull("PARENT"):
        raise ValueError(
            "Cambio JPNUM non consentito su odl figlio."
        )
    childNoTaskSet=wo.getMboSet("CHILDNOTASK")
    if not childNoTaskSet.isEmpty():
        for i in range (0,childNoTaskSet.count()):
            childNoTask=childNoTaskSet.getMbo(i)
            childNoTask.changeWorkOrderParent(None)
        childNoTaskSet.save()
        childNoTaskSet.close()
        #mbo.getThisMboSet().save()
        wo.getMboSet("$ANCESTORSF$","WOANCESTOR","ancestor=:wonum and wonum!=:wonum").deleteAll()
        wo.setRemoveWorkPlanFlag(False)
        wo.removeWorkPlan()
    else:
        wo.removeWorkPlan()

    # 2. Salvo subito la rimozione.
    # Questo è il punto importante: non fare set nuovo JP nella stessa istanza sporca.
    woSet.save()

    # 3. Ricarico il WO pulito
    woSet.reset()
    wo = woSet.moveFirst()

    if wo is None:
        raise ValueError("WO non trovato dopo removeWorkPlan: %s/%s" % (wonum, siteid))

    # 4. Rendo JPNUM scrivibile, nel caso sia ancora flaggato readonly
    try:
        wo.setFieldFlag("JPNUM", MboConstants.READONLY, False)
    except:
        pass

    # 5. Imposto il nuovo JPNUM
    wo.setValue("JPNUM", new_jpnum_s, MboConstants.NOACCESSCHECK)

    # 6. Verifica immediata: se qui è ancora vuoto, il set non sta passando proprio
    check_jpnum = "" if wo.isNull("JPNUM") else wo.getString("JPNUM")

    if check_jpnum.strip() != new_jpnum_s:
        raise ValueError(
            "Cambio JPNUM fallito subito dopo setValue. Atteso='%s', letto='%s'"
            % (new_jpnum_s, check_jpnum)
        )

    return wo, True
def get_active_jobplan_x4macroattivita(jpnum, userInfo):
    jpSet = None

    try:
        if is_blank_domain_value(jpnum):
            return None

        jpSet = mxs.getMboSet("JOBPLAN", userInfo)

        jpSet.setWhere(
            "jpnum='%s' and status='ACTIVE'" %
            sql_escape(jpnum)
        )

        jpSet.reset()

        jp = jpSet.moveFirst()

        if jp is None:
            return None

        if jp.isNull("X4MACROATTIVITA"):
            return None

        return str(jp.getString("X4MACROATTIVITA")).strip()

    finally:
        try:
            if jpSet is not None:
                jpSet.close()
        except:
            pass
def validate_same_macroattivita_for_jp_change(old_jpnum, new_jpnum, userInfo):
    old_macro = get_active_jobplan_x4macroattivita(old_jpnum, userInfo)
    new_macro = get_active_jobplan_x4macroattivita(new_jpnum, userInfo)

    if is_blank_domain_value(old_macro):
        raise ValueError(
            "Cambio JPNUM non consentito: macro attivita non trovata sul Job Plan attuale '%s'."
            % str(old_jpnum)
        )

    if is_blank_domain_value(new_macro):
        raise ValueError(
            "Cambio JPNUM non consentito: macro attivita non trovata sul nuovo Job Plan '%s'."
            % str(new_jpnum)
        )

    if str(old_macro).strip().upper() != str(new_macro).strip().upper():
        raise ValueError(
            "Cambio JPNUM non consentito: il nuovo Job Plan '%s' ha macro attivita '%s', "
            "diversa da quella del Job Plan attuale '%s' che e '%s'."
            % (
                str(new_jpnum),
                str(new_macro),
                str(old_jpnum),
                str(old_macro)
            )
        )
def _coerce_date(value, default_zone):
    if value is None:
        return None

    try:
        if str(value.__class__).endswith("JSONNull") or str(value) == "null":
            return None
    except:
        pass
    if isinstance(value, Date):
        return value

    # 1. Numero seriale Excel: 46198,5291666667 / 46198.5291666667
    d_excel = excel_serial_to_date(value)
    if d_excel is not None:
        return d_excel

    # 2. Stringhe data
    if isinstance(value, basestring):
        txt = str(value).strip()

        if txt == "":
            return None

        # ISO: 2026-06-25 / 2026-06-25T12:42:00 / offset
        if looks_like_iso_date_or_datetime(txt):
            return iso_to_date_any(txt, default_zone)

        # Italiano: 25/06/2026 / 25/06/2026 12:42 / 25/06/2026 12:42:00
        if looks_like_it_date_or_datetime(txt):
            return it_to_date_any(txt)

    return None

ORDERED_PAIRS = [("FNLCONSTRAINT","SNECONSTRAINT"), ("TARGCOMPDATE", "TARGSTARTDATE")]
LOCKED_FIELDS_IF_ODM_OR_NTK = set([
    "X4COIN",
    "X4WBS",
    "X4WBE"
])

RESOURCE_DATE_FIELDS = set([
    "X4DATAINIZIORISORSA",
    "X4DATAFINERISORSA"
])

def get_payload_value_ci(payload_obj, attr):
    target = str(attr).upper()

    it = payload_obj.keySet().iterator()
    while it.hasNext():
        k = it.next()
        if str(k).upper() == target:
            return payload_obj.get(k)

    return None


def payload_contains_key_ci(payload_obj, attr):
    target = str(attr).upper()

    it = payload_obj.keySet().iterator()
    while it.hasNext():
        k = it.next()
        if str(k).upper() == target:
            return True

    return False


def is_blank_lock_value(v):
    if v is None:
        return True

    try:
        if str(v.__class__).endswith("JSONNull") or str(v) == "null":
            return True
    except:
        pass

    return str(v).strip() == ""


def get_mbo_string_or_blank(mbo, attr):
    try:
        if mbo.isNull(attr):
            return ""
        return str(mbo.getString(attr)).strip()
    except:
        return ""


def values_different(old_value, new_value):
    old_s = "" if old_value is None else str(old_value).strip()
    new_s = "" if new_value is None else str(new_value).strip()

    return old_s != new_s


def validate_locked_fields_if_odm_or_ntk(payload_obj, target_mbo):
    # Se è un WO nuovo, non blocco.
    try:
        if target_mbo.toBeAdded():
            return
    except:
        pass

    odm_valorizzato = False
    ntk_valorizzato = False

    try:
        odm_valorizzato = not target_mbo.isNull("X4ODM")
    except:
        pass

    try:
        ntk_valorizzato = not target_mbo.isNull("X4NTK")
    except:
        pass

    if not odm_valorizzato and not ntk_valorizzato:
        return

    modified_locked_fields = []

    for attr in LOCKED_FIELDS_IF_ODM_OR_NTK:
        # Se il campo non arriva nel payload, non stanno provando a modificarlo.
        if not payload_contains_key_ci(payload_obj, attr):
            continue

        new_value = get_payload_value_ci(payload_obj, attr)
        old_value = get_mbo_string_or_blank(target_mbo, attr)

        # Se arriva vuoto/null e il vecchio valore è vuoto, non è modifica reale.
        # Se arriva vuoto/null ma il vecchio era valorizzato, è tentativo di svuotamento => blocco.
        if is_blank_lock_value(new_value):
            if old_value != "":
                modified_locked_fields.append(attr)
            continue

        if values_different(old_value, new_value):
            modified_locked_fields.append(attr)

    if len(modified_locked_fields) > 0:
        raise ValueError(
            "Non e possibile modificare i campi %s perche l'ODL ha gia ODM o NTK valorizzato."
            % ", ".join(sorted(modified_locked_fields))
        )


def is_blank_domain_value(v):
    if v is None:
        return True

    try:
        if str(v.__class__).endswith("JSONNull") or str(v) == "null":
            return True
    except:
        pass

    return str(v).strip() == ""


def sql_escape(v):
    return str(v).replace("'", "''")

def payload_contains_key_ci(payload_obj, attr):
    target = str(attr).upper()

    it = payload_obj.keySet().iterator()
    while it.hasNext():
        k = it.next()
        if str(k).upper() == target:
            return True

    return False


def get_mbo_string_safe(mbo, attr):
    try:
        if mbo is not None and not mbo.isNull(attr):
            return mbo.getString(attr)
    except:
        pass

    return None


def get_active_jobplan_tipudb(jpnum, userInfo):
    jpSet = None

    try:
        if is_blank_domain_value(jpnum):
            return None

        jpSet = mxs.getMboSet("JOBPLAN", userInfo)
        jpSet.setWhere(
            "jpnum='%s' and status='ACTIVE'" %
            sql_escape(jpnum)
        )
        jpSet.reset()

        jp = jpSet.moveFirst()
        if jp is None:
            return None

        if jp.isNull("X4TIPUDB"):
            return None

        return jp.getString("X4TIPUDB")

    finally:
        try:
            if jpSet is not None:
                jpSet.close()
        except:
            pass


def get_top_asset_udb_values(assetnum, siteid, userInfo):
    assetSet = None

    try:
        if is_blank_domain_value(assetnum) or is_blank_domain_value(siteid):
            return (None, None)

        a = sql_escape(assetnum)
        s = sql_escape(siteid)

        assetSet = mxs.getMboSet("ASSET", userInfo)

        # Primo livello / ancestor più alto
        assetSet.setWhere(
            "assetnum in ("
            "    select ancestor "
            "    from assetancestor "
            "    where assetnum='%s' "
            "    and siteid='%s' "
            "    and hierarchylevels=("
            "        select max(hierarchylevels) "
            "        from assetancestor "
            "        where assetnum='%s' "
            "        and siteid='%s'"
            "    )"
            ") "
            "and siteid='%s'" %
            (a, s, a, s, s)
        )
        assetSet.reset()

        asset = assetSet.moveFirst()

        # Fallback: se non trovo ancestor, uso l'asset stesso
        if asset is None:
            assetSet.setWhere(
                "assetnum='%s' and siteid='%s'" %
                (a, s)
            )
            assetSet.reset()
            asset = assetSet.moveFirst()

        if asset is None:
            return (None, None)

        x3ingrp = None
        x3swerk = None

        if not asset.isNull("X3INGRP"):
            x3ingrp = asset.getString("X3INGRP")

        if not asset.isNull("X3SWERK"):
            x3swerk = asset.getString("X3SWERK")

        return (x3ingrp, x3swerk)

    finally:
        try:
            if assetSet is not None:
                assetSet.close()
        except:
            pass


def find_ownergroup_for_create(jpnum, assetnum, siteid, userInfo):
    pgSet = None

    try:
        if is_blank_domain_value(jpnum):
            return None

        if is_blank_domain_value(assetnum):
            return None

        tipoudb = get_active_jobplan_tipudb(jpnum, userInfo)

        if is_blank_domain_value(tipoudb):
            return None

        tipoudb = str(tipoudb).strip().upper()

        x3ingrp, x3swerk = get_top_asset_udb_values(assetnum, siteid, userInfo)

        pgSet = mxs.getMboSet("PERSONGROUP", userInfo)

        if tipoudb == "UI":
            if is_blank_domain_value(x3ingrp):
                return None

            pgSet.setWhere(
                "x4udb=1 "
                "and x4tipudb='%s' "
                "and x3parentpersongroup='%s'" %
                (
                    sql_escape(tipoudb),
                    sql_escape(x3ingrp)
                )
            )

        else:
            if is_blank_domain_value(x3swerk):
                return None

            pgSet.setWhere(
                "x4udb=1 "
                "and x4tipudb='%s' "
                "and ("
                "    x3parentpersongroup='%s' "
                "    or x3parentpersongroup in ("
                "        select persongroup "
                "        from persongroup "
                "        where x4udb=1 "
                "        and x3parentpersongroup='%s'"
                "    )"
                ")" %
                (
                    sql_escape(tipoudb),
                    sql_escape(x3swerk),
                    sql_escape(x3swerk)
                )
            )

        # Se esiste una priorità vera, meglio usare quella al posto di PERSONGROUP.
        pgSet.setOrderBy("PERSONGROUP")
        pgSet.reset()

        pg = pgSet.moveFirst()

        if pg is None:
            return None

        return pg.getString("PERSONGROUP")

    finally:
        try:
            if pgSet is not None:
                pgSet.close()
        except:
            pass


def set_ownergroup_on_create_if_missing(payload_obj, wo, siteid, userInfo, default_zone, strict):
    if not wo.isNull("OWNERGROUP"):
        return
    if payload_contains_key_ci(payload_obj, "OWNERGROUP"):
        return
    jpnum = get_payload_value_ci(payload_obj, "JPNUM")
    assetnum = get_payload_value_ci(payload_obj, "ASSETNUM")
    x4aggregati = get_payload_value_ci(payload_obj, "X4AGGREGATI")
    if is_blank_domain_value(jpnum):
        jpnum = get_mbo_string_safe(wo, "JPNUM")
    if is_blank_domain_value(assetnum):
        assetnum = get_mbo_string_safe(wo, "ASSETNUM")
    if not is_blank_domain_value(jpnum) and wo.isNull("JPNUM"):
        set_typed(wo, "JPNUM", jpnum, default_zone)
        set_typed(wo, "X4AGGREGATI", x4aggregati, default_zone)

    if not is_blank_domain_value(assetnum) and wo.isNull("ASSETNUM"):
        set_typed(wo, "ASSETNUM", assetnum, default_zone)
    set_typed(wo, "APPTREQUIRED",0,default_zone)

    

    # Li setto prima perché il calcolo OWNERGROUP dipende da JPNUM/ASSETNUM
    # e WBS/WBE potrebbero validare subito dopo.
    if not is_blank_domain_value(jpnum) and wo.isNull("JPNUM"):
        set_typed(wo, "JPNUM", jpnum, default_zone)
        set_typed(wo, "X4AGGREGATI", x4aggregati, default_zone)

    set_typed(wo, "APPTREQUIRED",0,default_zone)

    ownergroup = find_ownergroup_for_create(jpnum, assetnum, siteid, userInfo)


    if is_blank_domain_value(ownergroup):
        if strict:
            raise ValueError(
                "OWNERGROUP non passato e non determinabile automaticamente. "
                "JPNUM='%s', ASSETNUM='%s', SITEID='%s'." %
                (
                    str(jpnum) if jpnum is not None else "",
                    str(assetnum) if assetnum is not None else "",
                    str(siteid) if siteid is not None else ""
                )
            )
        return

    wo.setValue("OWNERGROUP", ownergroup, 11L)

def validate_jpnum_payload(payload_obj, target_mbo):
    jpnum = get_payload_value_ci(payload_obj, "JPNUM")

    # In update, se JPNUM non arriva nel payload,
    # uso quello gia presente sul WORKORDER.
    if is_blank_domain_value(jpnum):
        try:
            if target_mbo is not None and not target_mbo.isNull("JPNUM"):
                jpnum = target_mbo.getString("JPNUM")
        except:
            pass

    if is_blank_domain_value(jpnum):
        return

    if is_list_or_object(jpnum):
        raise ValueError(
            "JPNUM non valido: valore strutturato non ammesso."
        )

    jpnum_s = str(jpnum).strip()
    jpSet = None

    try:
        jpSet = mxs.getMboSet(
            "JOBPLAN",
            target_mbo.getUserInfo()
        )

        jpSet.setWhere(
            "jpnum='%s' and status='ACTIVE'" %
            sql_escape(jpnum_s)
        )
        jpSet.reset()

        if jpSet.isEmpty():
            raise ValueError(
                "JPNUM non valido: Job Plan attivo '%s' non trovato."
                % jpnum_s
            )

    finally:
        try:
            if jpSet is not None:
                jpSet.close()
        except:
            pass


def validate_naturalavoro_outsourcing(payload_obj, target_mbo):
    outsourcing_present = payload_contains_key_ci(payload_obj, "X4OUTSOURCING")
    if not outsourcing_present:
        return
    naturalavoro_present = payload_contains_key_ci(
        payload_obj,
        "X4NATURALAVORO"
    )

    outsourcing_present = payload_contains_key_ci(
        payload_obj,
        "X4OUTSOURCING"
    )

    naturalavoro = get_payload_value_ci(
        payload_obj,
        "X4NATURALAVORO"
    )

    outsourcing = get_payload_value_ci(
        payload_obj,
        "X4OUTSOURCING"
    )

    # Se il campo non arriva nel payload, uso il valore gia presente sul MBO.
    # Se invece arriva esplicitamente vuoto/null, mantengo il vuoto per
    # intercettare correttamente il tentativo di cancellazione.
    if not naturalavoro_present:
        try:
            if (
                target_mbo is not None
                and not target_mbo.isNull("X4NATURALAVORO")
            ):
                naturalavoro = target_mbo.getString(
                    "X4NATURALAVORO"
                )
        except:
            pass

    if not outsourcing_present:
        try:
            if (
                target_mbo is not None
                and not target_mbo.isNull("X4OUTSOURCING")
            ):
                try:
                    outsourcing = target_mbo.getBoolean(
                        "X4OUTSOURCING"
                    )
                except:
                    outsourcing = target_mbo.getString(
                        "X4OUTSOURCING"
                    )
        except:
            pass

    natura = ""

    if not is_blank_domain_value(naturalavoro):
        natura = str(naturalavoro).strip()

    outsourcing_is_one = False

    if not is_blank_domain_value(outsourcing):
        try:
            if isinstance(outsourcing, Boolean):
                outsourcing_is_one = bool(
                    outsourcing.booleanValue()
                )
            else:
                outsourcing_s = str(outsourcing).strip().upper()
                outsourcing_is_one = outsourcing_s in (
                    "1",
                    "TRUE",
                    "Y",
                    "YES",
                    "SI"
                )
        except:
            outsourcing_s = str(outsourcing).strip().upper()
            outsourcing_is_one = outsourcing_s in (
                "1",
                "TRUE",
                "Y",
                "YES",
                "SI"
            )

    if outsourcing_is_one and (
        not naturalavoro_present
        or is_blank_domain_value(naturalavoro)
    ):
        raise ValueError(
            "X4NATURALAVORO deve essere valorizzato nel caricamento "
            "quando X4OUTSOURCING e impostato a 1."
        )

    # X4NATURALAVORO valorizzato richiede X4OUTSOURCING = 1.
    if natura != "" and not outsourcing_is_one:
        raise ValueError(
            "Se X4NATURALAVORO valorizzato, "
            "X4OUTSOURCING deve essere impostato a 1."
        )


def get_jobplan_x4attall(jpnum, userInfo):
    jpSet = None

    try:
        if is_blank_domain_value(jpnum):
            return None

        jpSet = mxs.getMboSet("JOBPLAN", userInfo)

        jpSet.setWhere(
            "jpnum='%s' and status='ACTIVE'" %
            sql_escape(jpnum)
        )
        jpSet.reset()

        jp = jpSet.moveFirst()

        if jp is None:
            return None

        if jp.isNull("X4ATTALL"):
            return None

        return jp.getString("X4ATTALL")

    finally:
        try:
            if jpSet is not None:
                jpSet.close()
        except:
            pass


def x4coin_allowed_for_tipospesa_and_jp(x4coin, tipospesa, jpnum, userInfo):
    chkSet = None

    try:
        if is_blank_domain_value(x4coin):
            return True

        if is_blank_domain_value(tipospesa):
            raise ValueError(
                "X4TIPOSPESA mancante: impossibile validare il valore X4COIN '%s'."
                % str(x4coin)
            )

        ts = str(tipospesa).strip().upper()

        # Replico solo i 3 domini reali.
        # Se X4SEARCHCOIN è vuoto/non filtrato, qui non applico controllo custom.
        if ts not in ("CAPEX", "OPEX", "ANR"):
            return True

        if is_blank_domain_value(jpnum):
            raise ValueError(
                "JPNUM mancante: impossibile validare il valore X4COIN '%s'."
                % str(x4coin)
            )

        x4attall = get_jobplan_x4attall(jpnum, userInfo)

        if is_blank_domain_value(x4attall):
            raise ValueError(
                "Impossibile validare X4COIN '%s': non trovato X4ATTALL sul Job Plan %s."
                % (str(x4coin), str(jpnum))
            )

        chkSet = mxs.getMboSet("X4CATMOSCOIN", userInfo)

        chkSet.setWhere(
            "coin='%s' "
            "and tipospesa='%s' "
            "and x4catmosid in ("
            "    select x4catmosid "
            "    from x4catmos "
            "    where idattstandard='%s'"
            ")" %
            (
                sql_escape(x4coin),
                sql_escape(ts),
                sql_escape(x4attall)
            )
        )

        chkSet.reset()

        return not chkSet.isEmpty()

    finally:
        try:
            if chkSet is not None:
                chkSet.close()
        except:
            pass


def validate_x4coin_payload(payload_obj, target_mbo):
    x4coin = get_payload_value_ci(payload_obj, "X4COIN")
    jpnum = get_payload_value_ci(payload_obj, "JPNUM")
    
    if is_blank_domain_value(x4coin):
        return
    
    if target_mbo:
        if not target_mbo.isNull("PARENT"):
            if not target_mbo.isNull("X4COIN") and x4coin!=target_mbo.getString("X4COIN") and not target_mbo.isNull("PARENT"):
                raise ValueError(
                    "Valore CoIn non modificabile per gli odl figli."
                    )

    

    tipospesa = get_payload_value_ci(payload_obj, "X4TIPOSPESA")

    # Se non arrivano nel payload, provo a leggerli dal WORKORDER esistente.
    if is_blank_domain_value(tipospesa):
        try:
            if not target_mbo.isNull("X4TIPOSPESA"):
                tipospesa = target_mbo.getString("X4TIPOSPESA")
                if is_blank_domain_value(tipospesa):
                    tipospesaSet = mxs.getMboSet("X4CATMOS", sysui)
                    tipospesaSet.setWhere("IDATTSTANDARD in (select X4ATTALL from jobplan where jpnum='%s' and status='ACTIVE')" % jpnum)
                    tipospesaSet.reset()
                    if not tipospesaSet.isEmpty():
                        tipospesa=tipospesaSet.getMbo(0).getString("SPESAPREVALENTE")
                    tipospesaSet.close()
        except:
            pass

    if is_blank_domain_value(jpnum):
        try:
            if not target_mbo.isNull("JPNUM"):
                jpnum = target_mbo.getString("JPNUM")
        except:
            pass

    if not x4coin_allowed_for_tipospesa_and_jp(
        str(x4coin).strip(),
        tipospesa,
        jpnum,
        target_mbo.getUserInfo()
    ):
        raise ValueError(
            "Valore X4COIN non valido: '%s'. "
            "Il valore non e ammesso per X4TIPOSPESA='%s' e Job Plan='%s'."
            % (
                str(x4coin).strip(),
                str(tipospesa).strip() if tipospesa is not None else "",
                str(jpnum).strip() if jpnum is not None else ""
            )
        )
JP_FIELDS_AFTER_JPNUM_CHANGE = ["X4TIPOSPESA", "X4COIN", "X4WBS", "X4WBE"]

def get_deferred_jp_fields(payload_obj, is_create):
    deferred_fields = set()

    # Il rinvio serve solo in update quando viene passato JPNUM.
    if is_create or not payload_contains_key_ci(payload_obj, "JPNUM"):
        return deferred_fields

    new_jpnum = get_payload_value_ci(payload_obj, "JPNUM")

    if is_blank_domain_value(new_jpnum) or is_list_or_object(new_jpnum):
        return deferred_fields

    for attr in JP_FIELDS_AFTER_JPNUM_CHANGE:
        if payload_contains_key_ci(payload_obj, attr):
            deferred_fields.add(attr)

    return deferred_fields

def apply_deferred_jp_fields(payload_obj, target_mbo, deferred_fields, default_zone):
    if deferred_fields is None or len(deferred_fields) == 0:
        return

    # Ordine: TIPOSPESA prima di COIN e WBS prima di WBE.
    for attr in JP_FIELDS_AFTER_JPNUM_CHANGE:
        if attr not in deferred_fields:
            continue

        value = get_payload_value_ci(payload_obj, attr)

        if not is_list_or_object(value):
            set_typed(target_mbo, attr, value, default_zone)
def apply_object_fields(payload_obj, target_mbo, skip_keys, default_zone, deferred_fields=None):
    validate_locked_fields_if_odm_or_ntk(payload_obj, target_mbo)
    validate_x4tipospesa_payload(payload_obj)
    validate_jpnum_payload(payload_obj, target_mbo)
    validate_naturalavoro_outsourcing(payload_obj, target_mbo)
    validate_x4coin_payload(payload_obj, target_mbo)

    present = {}
    it0 = payload_obj.keySet().iterator()
    while it0.hasNext():
        k0 = it0.next()
        present[str(k0).upper()] = k0
    # Le validazioni iniziali sono già state eseguite sull'intero payload.
    # Escludo questi campi solamente dalla fase di setValue precedente al cambio JP.
    if deferred_fields is not None:
        for deferred_attr in deferred_fields:
            deferred_attr_u = str(deferred_attr).upper()
            if deferred_attr_u in present:
                del present[deferred_attr_u]
    
    # Per gli ODL il cui WONUM inizia per "D",
    # ignoro completamente le modifiche a SNECONSTRAINT e FNLCONSTRAINT.
    try:
        is_d_workorder = (
            target_mbo.getName().upper() == "WORKORDER"
            and not target_mbo.isNull("WONUM")
            and target_mbo.getString("WONUM").strip().upper().startswith("D")
        )
    except:
        is_d_workorder = False
    
    if is_d_workorder:
        if "SNECONSTRAINT" in present:
            del present["SNECONSTRAINT"]
    
        if "FNLCONSTRAINT" in present:
            del present["FNLCONSTRAINT"]

    # Le quattro date del WORKORDER vengono applicate per ultime.
    # Le tolgo temporaneamente da "present" per evitare che vengano
    # elaborate prima degli altri attributi.
    deferred_dates = {}

    for date_attr in [
        "TARGSTARTDATE",
        "TARGCOMPDATE",
        "SNECONSTRAINT",
        "FNLCONSTRAINT"
    ]:
        if date_attr in present:
            deferred_dates[date_attr] = present[date_attr]
            del present[date_attr]

    if "JPNUM" in present and ("JPNUM" not in skip_keys):
        jk = present["JPNUM"]
        jv = payload_obj.get(jk)

        if (not is_list_or_object(jv)) and target_mbo.isNull("JPNUM"):
            set_typed(target_mbo, "JPNUM", jv, default_zone)

        del present["JPNUM"]
    
    if "ASSETNUM" in present and ("ASSETNUM" not in skip_keys):
        ak = present["ASSETNUM"]
        av = payload_obj.get(ak)
    
        if not is_list_or_object(av) and target_mbo.isNull("ASSETNUM"):
            set_typed(target_mbo, "ASSETNUM", av, default_zone)
    
        del present["ASSETNUM"]
        
    if "X4SAPIDASSET" in present and ("X4SAPIDASSET" not in skip_keys):
        sk = present["X4SAPIDASSET"]
        sv = payload_obj.get(sk)
    
        if not is_list_or_object(sv):
            set_typed(target_mbo, "X4SAPIDASSET", sv, default_zone)
    
        del present["X4SAPIDASSET"]

    if "X4COIN" in present and "X4TIPOSPESA" in present:
        jk = present["X4TIPOSPESA"]
        jv = payload_obj.get(jk)

        set_typed(target_mbo, "X4TIPOSPESA", jv, default_zone)

        del present["X4TIPOSPESA"]

        jk2 = present["X4COIN"]
        jv2 = payload_obj.get(jk2)

        set_typed(target_mbo, "X4COIN", jv2, default_zone)

        del present["X4COIN"]

    # -------------------------------------------------------------------------
    # X4WBS deve essere impostato prima di X4WBE.
    # Serve soprattutto perché il dominio/validazione di X4WBE puo dipendere
    # dal valore corrente di X4WBS sul WORKORDER.
    # -------------------------------------------------------------------------
    for ordered_attr in ["X4COIN","X4SAPIDASSET","OWNERGROUP","X4WBS", "X4WBE"]:
        if ordered_attr in present:
            k = present[ordered_attr]
            '''if ordered_attr=='X4WBS':
                service.error("TEST","sapid"+wo.getString("X4SAPIDASSET"))'''

            if k in skip_keys:
                del present[ordered_attr]
                continue

            v = payload_obj.get(k)

            if not is_list_or_object(v):
                set_typed(target_mbo, ordered_attr, v, default_zone)

            del present[ordered_attr]
    # -------------------------------------------------------------------------
    # Per WPSERVICE, ITEMQTY deve essere impostato prima di LINECOST.
    # Serve se LINECOST dipende dal valore corrente di ITEMQTY o da logiche Maximo.
    # -------------------------------------------------------------------------
    for ordered_attr in ["ITEMNUM","ITEMQTY","LINECOST"]:
        if ordered_attr in present:
            k = present[ordered_attr]

            if k in skip_keys:
                del present[ordered_attr]
                continue

            v = payload_obj.get(k)

            if not is_list_or_object(v):
                set_typed(target_mbo, ordered_attr, v, default_zone)

            del present[ordered_attr]

    it = payload_obj.keySet().iterator()
    while it.hasNext():
        k = it.next()
        if k in skip_keys:
            continue

        uk = str(k).upper()
        if uk not in present:
            continue

        v = payload_obj.get(k)
        if is_list_or_object(v):
            continue

        set_typed(target_mbo, uk, v, default_zone)
    has_targ_start = "TARGSTARTDATE" in deferred_dates
    has_targ_comp = "TARGCOMPDATE" in deferred_dates
    has_sne = "SNECONSTRAINT" in deferred_dates
    has_fnl = "FNLCONSTRAINT" in deferred_dates

    if has_targ_start and has_targ_comp:
        raw_targ_start = payload_obj.get(deferred_dates["TARGSTARTDATE"])
        raw_targ_comp = payload_obj.get(deferred_dates["TARGCOMPDATE"])

        if is_list_or_object(raw_targ_start) or is_list_or_object(raw_targ_comp):
            raise ValueError("TARGSTARTDATE e TARGCOMPDATE non possono essere valori strutturati.")

        d_targ_start = _coerce_date(raw_targ_start, default_zone)
        d_targ_comp = _coerce_date(raw_targ_comp, default_zone)

        if d_targ_start is None:
            raise ValueError("TARGSTARTDATE non valida come data: '%s'." % str(raw_targ_start))

        if d_targ_comp is None:
            raise ValueError("TARGCOMPDATE non valida come data: '%s'." % str(raw_targ_comp))

        if d_targ_start.compareTo(d_targ_comp) > 0:
            raise ValueError("TARGSTARTDATE %s non puo essere maggiore di TARGCOMPDATE %s." % (str(d_targ_start), str(d_targ_comp)))

        if has_sne != has_fnl:
            raise ValueError("SNECONSTRAINT e FNLCONSTRAINT devono essere passate entrambe oppure entrambe omesse.")

        # Tutte e quattro le date deferred_datesi.
        if has_sne and has_fnl:
            raw_sne = payload_obj.get(deferred_dates["SNECONSTRAINT"])
            raw_fnl = payload_obj.get(deferred_dates["FNLCONSTRAINT"])

            if is_list_or_object(raw_sne) or is_list_or_object(raw_fnl):
                raise ValueError("SNECONSTRAINT e FNLCONSTRAINT non possono essere valori strutturati.")

            d_sne = _coerce_date(raw_sne, default_zone)
            d_fnl = _coerce_date(raw_fnl, default_zone)

            if d_sne is None:
                raise ValueError("SNECONSTRAINT non valida come data: '%s'." % str(raw_sne))

            if d_fnl is None:
                raise ValueError("FNLCONSTRAINT non valida come data: '%s'." % str(raw_fnl))

            if d_sne.compareTo(d_targ_start) > 0:
                raise ValueError("SNECONSTRAINT %s non puo essere maggiore di TARGSTARTDATE %s." % (str(d_sne), str(d_targ_start)))

            if d_targ_comp.compareTo(d_fnl) > 0:
                raise ValueError("TARGCOMPDATE %s non puo essere maggiore di FNLCONSTRAINT %s." % (str(d_targ_comp), str(d_fnl)))

            target_mbo.setValue("FNLCONSTRAINT", d_fnl,11L)
            target_mbo.setValue("SNECONSTRAINT", d_sne,11L)
            target_mbo.setValue("TARGCOMPDATE", d_targ_comp,11L)
            target_mbo.setValue("TARGSTARTDATE", d_targ_start,11L)

            del deferred_dates["FNLCONSTRAINT"]
            del deferred_dates["SNECONSTRAINT"]
            del deferred_dates["TARGCOMPDATE"]
            del deferred_dates["TARGSTARTDATE"]

        # Solo TARGSTARTDATE e TARGCOMPDATE.
        else:
            target_mbo.setValue("TARGCOMPDATE", d_targ_comp, 11L)
            target_mbo.setValue("TARGSTARTDATE", d_targ_start, 11L)

            del deferred_dates["TARGCOMPDATE"]
            del deferred_dates["TARGSTARTDATE"]

    # Caso in cui arrivano solo SNECONSTRAINT e FNLCONSTRAINT.
    if "SNECONSTRAINT" in deferred_dates and "FNLCONSTRAINT" in deferred_dates:
        raw_sne = payload_obj.get(deferred_dates["SNECONSTRAINT"])
        raw_fnl = payload_obj.get(deferred_dates["FNLCONSTRAINT"])

        if is_list_or_object(raw_sne) or is_list_or_object(raw_fnl):
            raise ValueError("SNECONSTRAINT e FNLCONSTRAINT non possono essere valori strutturati.")

        d_sne = _coerce_date(raw_sne, default_zone)
        d_fnl = _coerce_date(raw_fnl, default_zone)

        if d_sne is None:
            raise ValueError("SNECONSTRAINT non valida come data: '%s'." % str(raw_sne))

        if d_fnl is None:
            raise ValueError("FNLCONSTRAINT non valida come data: '%s'." % str(raw_fnl))

        if d_sne.compareTo(d_fnl) > 0:
            raise ValueError("SNECONSTRAINT %s non puo essere maggiore di FNLCONSTRAINT %s." % (str(d_sne), str(d_fnl)))

        target_mbo.setValue("FNLCONSTRAINT", d_fnl)
        target_mbo.setValue("SNECONSTRAINT", d_sne)

        del deferred_dates["FNLCONSTRAINT"]
        del deferred_dates["SNECONSTRAINT"]

def relationship_name_from_json_key(json_key):
    return str(json_key).upper()

def normalize_id_value(v):
    if v is None:
        return ""

    try:
        if str(v.__class__).endswith("JSONNull") or str(v) == "null":
            return ""
    except:
        pass

    try:
        if isinstance(v, BigDecimal) or isinstance(v, BigInteger):
            return str(v.longValue())
    except:
        pass
    s = str(v).strip()

    # Utile se arriva da Excel/MXLoader come 123.0
    if s.endswith(".0"):
        s = s[:-2]
    if s.endswith(",0"):
        s = s[:-2]

    return s.strip()


def get_child_update_id_attr(rel):
    r = str(rel).upper()

    if r == "WPSERVICE":
        return "WPITEMID"

    if r == "WPLABOR":
        return "WPLABORUID"

    return None


def find_child_by_id(childSet, id_attr, id_value):
    wanted = normalize_id_value(id_value)

    if wanted == "":
        return None

    i = 0
    while True:
        m = childSet.getMbo(i)

        if m is None:
            break

        try:
            if not m.isNull(id_attr):
                current = normalize_id_value(m.getString(id_attr))

                if current == wanted:
                    return m
        except:
            pass

        i += 1

    return None
def debug_error(msg):
    try:
        service.error("x3terna", "ErrorMsg", [msg[:3900]])
    except NameError:
        raise ValueError(msg)
def get_child_object_name_for_update(rel):
    r = str(rel).upper()

    if r == "WPSERVICE":
        return "WPITEM"

    if r == "WPLABOR":
        return "WPLABOR"

    return str(rel)


def get_existing_child_direct(parent_mbo, rel, id_attr, id_value):
    id_clean = normalize_id_value(id_value)

    if id_clean == "":
        return None

    parent_wonum = parent_mbo.getString("WONUM")
    parent_siteid = parent_mbo.getString("SITEID")

    r = str(rel).upper()
    obj_name = get_child_object_name_for_update(rel)

    if r == "WPSERVICE":
        where = (
            "wonum='%s' and siteid='%s' and wpitemid=%s"
            % (
                sql_escape(parent_wonum),
                sql_escape(parent_siteid),
                sql_escape(id_clean)
            )
        )

        # Se vuoi essere più restrittivo:
        # where += " and linetype='SERVICE'"

    elif r == "WPLABOR":
        where = (
            "wonum='%s' and siteid='%s' and wplaboruid=%s"
            % (
                sql_escape(parent_wonum),
                sql_escape(parent_siteid),
                sql_escape(id_clean)
            )
        )

    else:
        where = (
            "wonum='%s' and siteid='%s' and %s=%s"
            % (
                sql_escape(parent_wonum),
                sql_escape(parent_siteid),
                str(id_attr),
                sql_escape(id_clean)
            )
        )

    rel_name = "$X4LOAD_%s_%s" % (r, id_clean)

    lookupSet = parent_mbo.getMboSet(rel_name, obj_name, where)
    lookupSet.reset()

    child = lookupSet.moveFirst()

    if child is None:
        raise ValueError(
            "Riga %s non trovata con ricerca diretta. OBJECT=%s WHERE=%s"
            % (str(rel), str(obj_name), str(where))
        )

    return child        
def apply_child_collections(payload_obj, parent_mbo, default_zone):
    from java.util import List

    it = payload_obj.keySet().iterator()

    while it.hasNext():
        k = it.next()
        v = payload_obj.get(k)

        if not isinstance(v, List):
            continue

        rel = relationship_name_from_json_key(k)
        childSet = parent_mbo.getMboSet(rel)

        id_attr = get_child_update_id_attr(rel)

        arr_it = v.iterator()

        while arr_it.hasNext():
            row = arr_it.next()

            child = None
            id_value = None

            if id_attr is not None:
                id_value = get_payload_value_ci(row, id_attr)

                if not is_blank_domain_value(id_value):
                    child = get_existing_child_direct(parent_mbo, rel, id_attr, id_value)

            if child is None:
                child = childSet.add()

                if str(rel).upper() == "WPSERVICE":
                    child.setValue("LINETYPE", "STDSERVICE")
            else:
                if str(rel).upper() == "WPSERVICE":
                    child.setValue("X4CHANGEDATE", MXServer.getMXServer().getDate(),11L)

            skip_child_keys = set([])

            if id_attr is not None:
                skip_child_keys.add(id_attr)
                skip_child_keys.add(id_attr.lower())
                skip_child_keys.add(id_attr.upper())

            apply_object_fields(row, child, skip_child_keys, default_zone)

def workorder_exists(wonum, siteid, userInfo):
    wset = None
    try:
        if wonum is None or str(wonum).strip() == "":
            return False

        wset = mxs.getMboSet("WORKORDER", userInfo)
        wset.setWhere(
            "wonum='%s' and siteid='%s'" %
            (str(wonum).replace("'", "''"), str(siteid).replace("'", "''"))
        )
        wset.reset()
        return not wset.isEmpty()
    finally:
        try:
            if wset is not None:
                wset.close()
        except:
            pass

def get_parent_x4respasset(parent_wonum, siteid, userInfo):
    pset = None

    try:
        if is_blank_domain_value(parent_wonum) or is_blank_domain_value(siteid):
            return None

        pset = mxs.getMboSet("WORKORDER", userInfo)
        pset.setWhere(
            "wonum='%s' and siteid='%s'" %
            (
                sql_escape(parent_wonum),
                sql_escape(siteid)
            )
        )
        pset.reset()

        parent = pset.moveFirst()

        if parent is None:
            return None

        if parent.isNull("X4RESPASSET"):
            return None

        x4respasset = str(parent.getString("X4RESPASSET")).strip()

        if x4respasset == "":
            return None

        return x4respasset

    finally:
        try:
            if pset is not None:
                pset.close()
        except:
            pass


def get_expected_x4respasset_from_asset(payload_obj, target_mbo, siteid, userInfo):
    assetSet = None

    try:
        assetnum = get_payload_value_ci(payload_obj, "ASSETNUM")

        if is_blank_domain_value(assetnum):
            try:
                if target_mbo is not None and not target_mbo.isNull("ASSETNUM"):
                    assetnum = target_mbo.getString("ASSETNUM")
            except:
                pass

        if is_blank_domain_value(assetnum):
            return None

        if is_blank_domain_value(siteid):
            return None

        a = sql_escape(assetnum)
        s = sql_escape(siteid)

        assetSet = mxs.getMboSet("ASSET", userInfo)

        # 1. Prima provo a prendere X3INGRP dall'asset ancestor piu alto.
        assetSet.setWhere(
            "assetnum in ("
            "    select ancestor "
            "    from assetancestor "
            "    where assetnum='%s' "
            "    and siteid='%s' "
            "    and hierarchylevels=("
            "        select max(hierarchylevels) "
            "        from assetancestor "
            "        where assetnum='%s' "
            "        and siteid='%s'"
            "    )"
            ") "
            "and siteid='%s'" %
            (
                a,
                s,
                a,
                s,
                s
            )
        )
        assetSet.reset()

        ancestor_asset = assetSet.moveFirst()

        if ancestor_asset is not None and not ancestor_asset.isNull("X3INGRP"):
            x3ingrp_ancestor = str(ancestor_asset.getString("X3INGRP")).strip()

            if x3ingrp_ancestor != "":
                return x3ingrp_ancestor

        # 2. Se l'ancestor non esiste oppure X3INGRP e' nullo/vuoto,
        # uso X3INGRP dell'asset stesso.
        assetSet.setWhere(
            "assetnum='%s' and siteid='%s'" %
            (
                a,
                s
            )
        )
        assetSet.reset()

        asset = assetSet.moveFirst()

        if asset is None:
            return None

        if asset.isNull("X3INGRP"):
            return None

        x3ingrp_asset = str(asset.getString("X3INGRP")).strip()

        if x3ingrp_asset == "":
            return None

        return x3ingrp_asset

    finally:
        try:
            if assetSet is not None:
                assetSet.close()
        except:
            pass


def validate_parent_x4respasset_if_needed(payload_obj, target_mbo, siteid, userInfo):
    if not payload_contains_key_ci(payload_obj, "PARENT"):
        return

    parent_wonum = get_payload_value_ci(payload_obj, "PARENT")

    if is_blank_domain_value(parent_wonum):
        return

    parent_wonum = str(parent_wonum).strip()

    parent_x4respasset = get_parent_x4respasset(
        parent_wonum,
        siteid,
        userInfo
    )

    if is_blank_domain_value(parent_x4respasset):
        raise ValueError(
            "Parent %s/%s non valido: X4RESPASSET non valorizzato sul parent."
            % (
                parent_wonum,
                str(siteid)
            )
        )

    expected_x4respasset = get_expected_x4respasset_from_asset(
        payload_obj,
        target_mbo,
        siteid,
        userInfo
    )

    if is_blank_domain_value(expected_x4respasset):
        raise ValueError(
            "Impossibile determinare X4RESPASSET del nuovo WORKORDER: "
            "ASSETNUM mancante/non valido oppure X3INGRP non valorizzato su asset/ancestor. "
            "PARENT='%s'."
            % parent_wonum
        )

    if str(parent_x4respasset).strip().upper() != str(expected_x4respasset).strip().upper():
        raise ValueError(
            "Parent non coerente: il parent %s/%s ha X4RESPASSET='%s', "
            "ma l'asset del nuovo WORKORDER determina X4RESPASSET='%s'."
            % (
                parent_wonum,
                str(siteid),
                str(parent_x4respasset),
                str(expected_x4respasset)
            )
        )
def defer_loader(ldrSet, ldr, reason):
    try:
        retry = ldr.getInt("X4RETRYCOUNT")
    except:
        retry = 0

    retry += 1

    try:
        ldr.setValue("X4RETRYCOUNT", retry, 11L)
    except:
        pass

    ldr.setValue("STATUS", "WAITPARENT", 11L)
    ldr.setValue("DESCRIPTION", (reason[:250] if reason else "Parent non pronto"), 11L)
    ldr.setValue("ENDELAB", Date(), 11L)
    ldrSet.save()

def is_parent_not_ready_error(err):
    try:
        msg = str(err)
    except:
        msg = ""

    if "BMXAA4566E" in msg:
        return True
    if "not a valid parent work order" in msg.lower():
        return True
    if "BMXAA8229W" in msg:
        return True
    if "has been updated by another user" in msg.lower():
        return True
    return False
def get_error_message_it(e):
    try:
        if isinstance(e, MXException):
            return mxs.getTaggedMessage(e, "IT")
    except:
        pass

    try:
        if isinstance(e, MXException):
            return mxs.getMessage(e, "IT")
    except:
        pass

    return str(e)

mxs = MXServer.getMXServer()
sysui = mxs.getSystemUserInfo()

xid = x4loaderid
xid_int = int(xid)

ldrSet = None
woSet = None

try:
    ldrSet = mxs.getMboSet("X4LOADER", sysui)
    ldrSet.setWhere("X4LOADERID=%d" % xid_int)
    ldrSet.reset()
    ldr = ldrSet.moveFirst()
    if ldr is None:
        raise ValueError("X4LOADER non trovato: %d" % xid_int)

    if ldr.getString("STATUS") != "INPROG":
        try:
            ldrSet.close()
        except:
            pass
    else:
        body = ldr.getString("BODY")
        orig_user = ldr.getString("USERID")

        payload = parse_json(body)

        siteid = payload.get("siteid")
        wonum = payload.get("wonum")

        parent_wonum = None
        if payload.containsKey("parent") and payload.get("parent") is not None:
            parent_wonum = str(payload.get("parent")).strip()
            if parent_wonum == "":
                parent_wonum = None

        if siteid is None or str(siteid).strip() == "":
            raise ValueError("siteid mancante nel BODY")

        ui = mxs.getUserInfo(orig_user)

        if parent_wonum is not None:
            if not workorder_exists(parent_wonum, siteid, ui):
                defer_loader(
                    ldrSet,
                    ldr,
                    "Parent WO %s non ancora presente/valido" % parent_wonum
                )
                raise SystemExit

        woSet = mxs.getMboSet("WORKORDER", ui)

        is_create = False

        # CREATE vs UPDATE
        if wonum is None or str(wonum).strip() == "":
            wo = woSet.add()
            is_create = True
            if payload.containsKey("orgid") and payload.get("orgid") is not None:
                wo.setValue("ORGID", str(payload.get("orgid")))
        else:
            woSet.setWhere(
                "wonum='%s' and siteid='%s'" %
                (str(wonum).replace("'", "''"), str(siteid).replace("'", "''"))
            )
            woSet.reset()
            wo = woSet.moveFirst()

            if wo is None:
                wo = woSet.add()
                is_create = True
                if payload.containsKey("orgid") and payload.get("orgid") is not None:
                    wo.setValue("ORGID", str(payload.get("orgid")))
                wo.setValue("WONUM", str(payload.get("wonum")))
            else:
                is_create = False

        default_zone = ZoneId.systemDefault()

        if is_create:
            validate_parent_asset_coherence(
                payload,
                siteid,
                ui
            )
            set_ownergroup_on_create_if_missing(
                payload,
                wo,
                siteid,
                ui,
                default_zone,
                True
            )
        
            #validate_parent_x4respasset_if_needed(payload,wo,siteid,ui            )
        
            skip = set(["_action", "action", "wonum", "siteid", "userid", "idcluster", "wpservice", "wplabor"])
        else:
            skip = set(["_action", "action", "wonum", "siteid", "userid", "assetnum", "idcluster", "wpservice", "wplabor", "jpnum"])

        if wo.getBoolean("X4AGGREGATI"):
			skip.update(["X4PMI", "x4pmi"])
			
        deferred_jp_fields = get_deferred_jp_fields(payload, is_create)

        # Esegue prima tutte le validazioni, ma non imposta ancora i quattro campi.
        apply_object_fields(payload, wo, skip, default_zone, deferred_jp_fields)
        # Rimuove il vecchio work plan, ricarica il WO e imposta il nuovo JPNUM.
        wo, jp_changed = handle_jpnum_change_on_update(payload, woSet, wo, default_zone)
        # Imposta i quattro campi sul WO ricaricato, dopo il cambio JPNUM.
        apply_deferred_jp_fields(payload, wo, deferred_jp_fields, default_zone)
        
        validate_payload_wplabor_dates(payload,wo,default_zone)
        apply_child_collections(payload, wo, default_zone)
        wo.validate()

        woSet.save()
        final_wonum = wo.getString("WONUM")
        if parent_wonum is not None:
            clear_parent_x4fisicoplan_if_needed(
                parent_wonum,
                siteid,
                ui
            )
        ldr.setValue("STATUS", "DONE")
        ldr.setValue("DESCRIPTION", "Creato/Aggiornato WORKORDER %s/%s" % (final_wonum, siteid))
        ldr.setValue("ENDELAB", Date())
        ldrSet.save()

except SystemExit:
    pass

except (MXException, JavaException, Exception) as e:
    try:
        if ldrSet is not None:
            ldr = ldrSet.moveFirst()
            if ldr is not None:
                msg = safe_error_string(e)
                
                if msg is None or msg.strip() == "":
                    msg = "Errore"

                if is_parent_not_ready_error(e):
                    defer_loader(
                        ldrSet,
                        ldr,
                        msg if msg else "Parent non ancora valido"
                    )
                else:
                    ldr.setValue("STATUS", "ERR")
                    ldr.setValue("DESCRIPTION", (msg[:250] if msg else "Errore"))
                    ldr.setValue("ENDELAB", Date())
                    ldrSet.save()
    except:
        pass
finally:
    try:
        if woSet is not None:
            woSet.close()
    except:
        pass
    try:
        if ldrSet is not None:
            ldrSet.close()
    except:
        pass